/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.perception.algorithm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.model.ground.PerceivableGround;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum3D;
import fr.utbm.set.jasim.environment.model.perception.percepts.DefaultGroundPerception;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D1D;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D1D5;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D2D;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D3D;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.cullers.FrustumCuller;
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.TreeManipulator3D;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.util.error.ErrorLogger;

/**
 * Abstract implementation of a perception algorithm which
 * is treating the agents and their frustums in parallel.
 * 
 * @param <SB> is the bounds of the static entities.
 * @param <MB> is the bounds of the mobile entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractParallelPerceptionAlgorithm<SB extends CombinableBounds3D, MB extends CombinableBounds3D>
implements PerceptionAlgorithm<SB,MB> {

	private final int threadCount;
	private ExecutorService executors;
	
	/**
	 * @param threadCount is the count of thread used to compute perceptions.
	 */
	public AbstractParallelPerceptionAlgorithm(int threadCount) {
		this.threadCount = threadCount;
		this.executors = Executors.newFixedThreadPool(this.threadCount);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void computePerceptions(
			Map<AgentBody3D<?,?,MB>, PerceptionList3D<SB,MB,?>> population, 
			PerceivableGround ground,
			StaticPerceptionTree<CombinableBounds3D, SB, Entity3D<SB>, ?,?> staticPerceptionTree,
			DynamicPerceptionTree<CombinableBounds3D, MB, MobileEntity3D<MB>,
			  ?,?,?, ? extends TreeManipulator3D<MB,?,?>> dynamicPerceptionTree) {
		if (this.executors==null) {
			this.executors = Executors.newFixedThreadPool(this.threadCount);
		}
		
		AgentBody3D<?,?,MB> body;
		PseudoHamelDimension dimension;
		PerceptionList3D<SB,MB,?> list;
		Collection<? extends Frustum3D> frustums;
		Iterator<? extends Frustum3D> frustumIterator;
		Frustum3D frustum;
		AtomicInteger terminatedTasks = new AtomicInteger(0);
		int expectedTasks = 0;
		
		for(Entry<AgentBody3D<?,?,MB>, PerceptionList3D<SB,MB,?>> entry : population.entrySet()) {
			body = entry.getKey();
			dimension = body.getPreferredMathematicalDimension();
			list = null;
			switch(dimension) {
			case DIMENSION_1D:
				list = new PerceptionList3D1D<SB,MB>();
				break;
			case DIMENSION_1D5:
				list = new PerceptionList3D1D5<SB,MB>();
				break;
			case DIMENSION_2D:
				list = new PerceptionList3D2D<SB,MB>();
				break;
			case DIMENSION_2D5:
			case DIMENSION_3D:
				list = new PerceptionList3D3D<SB,MB>();
				break;
			default:
			}
			if (list!=null) {
				population.put(body, list);
				frustums = body.getFrustums();			
				frustumIterator = frustums.iterator();
				while (frustumIterator.hasNext()) {			
					frustum = frustumIterator.next();
					if (staticPerceptionTree!=null) {
						++expectedTasks;
						this.executors.submit(
								new StaticPerceptionTask(terminatedTasks, body, frustum, staticPerceptionTree, list));
					}
					if (dynamicPerceptionTree!=null) {
						++expectedTasks;
						this.executors.submit(
								new DynamicPerceptionTask(terminatedTasks, body, frustum, dynamicPerceptionTree, list));
					}
					if (ground!=null) {
						++expectedTasks;
						this.executors.submit(
								new GroundPerceptionTask(terminatedTasks, body, ground, list));
					}
				}
			}
		}
		// Wait for the termination of the tasks
		long startTime = System.currentTimeMillis();
		long currentTime;
		do {
			Thread.yield();
			currentTime = System.currentTimeMillis();
		}
		while (terminatedTasks.get()<expectedTasks && (currentTime-startTime)<900000); // 15mn
		if (terminatedTasks.get()<expectedTasks) {
			ErrorLogger.logWarning(this, "Perception computations are missed."); //$NON-NLS-1$
			try {
				this.executors.shutdownNow();
			}
			catch(Throwable _) {
				//
			}
			this.executors = null;
		}
	}

	/**
	 * Create a frustum culler for the given static tree according to the given frustum and the filters.
	 * 
	 * @param tree is the tree to traverse.
	 * @param entity is the perceiver.
	 * @param frustum is the frustum to use.
	 * @param physicFilter is the physical attribute filter.
	 * @param interestFilter is the interest filter.
	 * @return the frustum culler.
	 */
	protected abstract FrustumCuller<CombinableBounds3D,SB,Entity3D<SB>,?,?> createStaticCuller(
			StaticPerceptionTree<CombinableBounds3D, SB, Entity3D<SB>,?,?> tree,
			AgentBody3D<?,?,MB> entity,
			Frustum3D frustum,
			PhysicalPerceptionAlterator physicFilter,
			InterestFilter interestFilter);

	/**
	 * Create a frustum culler for the given static tree according to the given frustum and the filters.
	 *
	 * @param tree is the tree to traverse.
	 * @param entity is the perceiver.
	 * @param frustum is the frustum to use.
	 * @param physicFilter is the physical attribute filter.
	 * @param interestFilter is the interest filter.
	 * @return the frustum culler.
	 */
	protected abstract FrustumCuller<CombinableBounds3D,MB,MobileEntity3D<MB>,?,?> createDynamicCuller(
			DynamicPerceptionTree<CombinableBounds3D, MB, MobileEntity3D<MB>,
			  ?,?,?, ? extends TreeManipulator3D<MB,?,?>> tree,
			AgentBody3D<?,?,MB> entity,
			Frustum3D frustum,
			PhysicalPerceptionAlterator physicFilter,
			InterestFilter interestFilter);

	/**
	 * Run a perception task on a static tree.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class StaticPerceptionTask extends AbstractPerceptionTask {
		
		private final AgentBody3D<?,?,MB> body; 
		private final StaticPerceptionTree<CombinableBounds3D, SB, Entity3D<SB>, ?,?> staticPerceptionTree;
		private final PerceptionList3D<SB,?,?> list;
		private final Frustum3D frustum;

		/**
		 * @param token is the token used to detect the termination of the task.
		 * @param body is the body of the perceiver.
		 * @param frustum is the frustum to use for the perception.
		 * @param staticPerceptionTree is the perception tree to traverse.
		 * @param list is the perception list to fill.
		 */
		StaticPerceptionTask(
				AtomicInteger token,
				AgentBody3D<?,?,MB> body,
				Frustum3D frustum,
				StaticPerceptionTree<CombinableBounds3D, SB, Entity3D<SB>, ?,?> staticPerceptionTree,
				PerceptionList3D<SB,?,?> list) {
			super(token);
			this.body = body;
			this.frustum = frustum;
			this.list = list;
			this.staticPerceptionTree = staticPerceptionTree;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void runPerceptionTask() {
			if (this.staticPerceptionTree!=null) {
				CullingResult<? extends Entity3D<? extends CombinableBounds3D>> current;

				PhysicalPerceptionAlterator  physicFilter = this.body.getPerceptionFilter();
				InterestFilter interestFilter = this.body.getInterestFilter();

				FrustumCuller<CombinableBounds3D,SB,Entity3D<SB>,?,?> culler =
					createStaticCuller(
							this.staticPerceptionTree,
							this.body,
							this.frustum,
							physicFilter,
							interestFilter);
				
				while (culler.hasNext()) {
					current = culler.next();
					if (current.isVisible()) {
						this.list.addStaticPerception(current);
					}
				}
			}
		}
		
	} // class class StaticPerceptionTask

	/**
	 * Run a perception task on a dynamic tree.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class DynamicPerceptionTask extends AbstractPerceptionTask {
		
		private final AgentBody3D<?,?,MB> body; 
		private final PerceptionList3D<?,MB,?> list;
		private final Frustum3D frustum;
		private final DynamicPerceptionTree<CombinableBounds3D, MB, MobileEntity3D<MB>,
						?,?,?, ? extends TreeManipulator3D<MB,?,?>> dynamicPerceptionTree;

		/**
		 * @param token is the token used to detect the termination of the task.
		 * @param body is the body of the perceiver.
		 * @param frustum is the frustum to use for the perception.
		 * @param dynamicPerceptionTree is the perception tree to traverse.
		 * @param list is the perception list to fill.
		 */
		DynamicPerceptionTask(
				AtomicInteger token,
				AgentBody3D<?,?,MB> body,
				Frustum3D frustum,
				DynamicPerceptionTree<CombinableBounds3D, MB, MobileEntity3D<MB>,
							?,?,?, ? extends TreeManipulator3D<MB,?,?>> dynamicPerceptionTree,
				PerceptionList3D<?,MB,?> list) {
			super(token);
			this.body = body;
			this.frustum = frustum;
			this.list = list;
			this.dynamicPerceptionTree = dynamicPerceptionTree;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void runPerceptionTask() {
			if (this.dynamicPerceptionTree!=null) {
				Entity3D<? extends CombinableBounds3D> perceivedEntity;

				CullingResult<? extends Entity3D<? extends CombinableBounds3D>> current;

				PhysicalPerceptionAlterator  physicFilter = this.body.getPerceptionFilter();
				InterestFilter interestFilter = this.body.getInterestFilter();

				FrustumCuller<CombinableBounds3D,MB,MobileEntity3D<MB>,?,?> culler =
					createDynamicCuller(
							this.dynamicPerceptionTree,
							this.body,
							this.frustum,
							physicFilter,
							interestFilter);
				while (culler.hasNext()) {
					current = culler.next();
					if (current.isVisible()) {
						perceivedEntity = current.getCulledObject();
						// ensure that the perceived object is not the perceiver
						if (!perceivedEntity.getIdentifier().equals(this.body.getIdentifier())) {
							this.list.addDynamicPerception(current);
						}
					}
				}
			}
		}
		
	} // class class DynamicPerceptionTask

	/**
	 * Run a perception task on ground.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class GroundPerceptionTask extends AbstractPerceptionTask {

		private final AgentBody3D<?,?,?> body;
		private final PerceivableGround ground;
		private final PerceptionList3D<?,?,?> list;

		/**
		 * @param token is the token used to detect the termination of the task.
		 * @param body is the perceiver.
		 * @param ground is the ground to perceived on.
		 * @param list is the list to fill with perceptions.
		 */
		public GroundPerceptionTask(
				AtomicInteger token,
				AgentBody3D<?,?,?> body,
				PerceivableGround ground,
				PerceptionList3D<?,?,?> list) {
			super(token);
			this.body = body;
			this.ground = ground;
			this.list = list;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void runPerceptionTask() {
			if (this.ground!=null) {
				Point2d position = this.body.getPosition2D();
				this.list.setGroundPerception(new DefaultGroundPerception(this.ground,position.x,position.y));
			}
		}
		
	} // class GroundPerceptionTask
	
	/**
	 * Abstract implementation of a perception task.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static abstract class AbstractPerceptionTask implements Runnable {

		private AtomicInteger token;
		
		/**
		 * @param token is the token used to detect the termination of the task.
		 */
		public AbstractPerceptionTask(AtomicInteger token) {
			this.token = token;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void run() {
			try {
				runPerceptionTask();
			}
			catch(Throwable e) {
				ErrorLogger.logCriticalError(this, e);
			}
			this.token.incrementAndGet();
			this.token = null;
		}
		
		/** Run the perception task.
		 */
		protected abstract void runPerceptionTask();
		
	} // class AbstractPerceptionTask

}

