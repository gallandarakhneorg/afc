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
package fr.utbm.set.jasim.environment.model.world;

import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;

import fr.utbm.set.collection.CollectionUtil;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.InfluencerMobileEntity3D;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceiver;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceptions;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.ground.PerceivableGround;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction3D;
import fr.utbm.set.jasim.environment.model.perception.algorithm.PerceptionAlgorithm;
import fr.utbm.set.jasim.environment.model.perception.percepts.PerceptionList3D;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.StaticPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.manipulators.TreeManipulator3D;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGenerator;
import fr.utbm.set.jasim.environment.model.place.Place3D;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.tree.iterator.DepthFirstNodeOrder;

/**
 * This class manage the perception data structures.
 * 
 * @param <SB> is the bounds of the static entities.
 * @param <MB> is the bounds of the mobile entities.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WorldModelManager3D<SB extends CombinableBounds3D, MB extends CombinableBounds3D> 
implements PerceptionGenerator, 
		   WorldModelActuator<EnvironmentalAction3D, MobileEntity3D<MB>>,
		   WorldModelContainer<Entity3D<SB>, MobileEntity3D<MB>>
{

	/**
	 * The tree in which mobile entities of the environment are stored
	 */
	private DynamicPerceptionTree<CombinableBounds3D,
								  MB,
								  MobileEntity3D<MB>,
								  ?,?,?,
								  ? extends TreeManipulator3D<MB,?,?>> dynamicPerceptionTree;
	
	/**
	 * The tree in which static entities of the environment are stored
	 */
	private StaticPerceptionTree<CombinableBounds3D,
								 SB,
								 Entity3D<SB>,
								 ?,?> staticPerceptionTree;

	/**
	 * Map associating the address of a holon to its 
	 * corresponding computed set of percepts
	 */
	private final Map<AgentBody3D<?,?,MB>, PerceptionList3D<SB,MB,?>> perceptions;
	
	/** Place.
	 */
	private WeakReference<Place3D<SB,MB>> place;
	
	/** Temporary tree manipulator used to apply actions, entity removals and additions.
	 */
	private TreeManipulator3D<MB,?,?> treeManipulator = null;
	
	/** Percpetion algorithm.
	 */
	private final PerceptionAlgorithm<SB,MB> perceptionAlgorithm;

	/**
	 * @param perceptionAlgorithm is the algotihm implementation to use to compute
	 * agent perceptions.
	 */
	public WorldModelManager3D(PerceptionAlgorithm<SB,MB> perceptionAlgorithm) {
		this.perceptionAlgorithm = perceptionAlgorithm;
		this.perceptions = new TreeMap<AgentBody3D<?,?,MB>,PerceptionList3D<SB,MB,?>>(
				new Comparator<AgentBody3D<?,?,MB>>() {				
				@Override
				public int compare(AgentBody3D<?,?,MB> b1, AgentBody3D<?,?,MB> b2) {
					return b1.getIdentifier().compareTo(b2.getIdentifier());
				}});
		
		this.dynamicPerceptionTree = null;
		this.staticPerceptionTree = null;
		this.place = null;
	}
	
	/** Replies the perception algorithm used by this world model manager.
	 * 
	 * @return the perception algorithm used by this world model manager.
	 */
	public PerceptionAlgorithm<SB,MB> getPerceptionAlgorithm() {
		return this.perceptionAlgorithm;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void computeAgentPerceptions() {
		Place3D<SB,MB> thePlace = this.place!=null ? this.place.get() : null;
		PerceivableGround perceivableGround = null;
		if (thePlace!=null) {
			Ground ground = thePlace.getGround();
			if (ground instanceof PerceivableGround) {
				perceivableGround = (PerceivableGround)ground;
			}
		}
	
		if (this.perceptionAlgorithm!=null) {
			this.perceptionAlgorithm.computePerceptions(
					this.perceptions,
					perceivableGround,
					this.staticPerceptionTree,
					this.dynamicPerceptionTree);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <PT extends Perception> Perceptions<PT> getPerceptions(
			Perceiver perceiver, Class<PT> perceptionType) {
		Perceptions<PT> perceptionsToReply = null;
		PerceptionList3D<SB,MB,?> list;
		list = this.perceptions.get(perceiver);
		if (list!=null && list.isSupportedPerceptionType(perceptionType)) {
			perceptionsToReply = (Perceptions<PT>)list;
		}
		return perceptionsToReply;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <WM> WM getInnerWorldModel(Class<WM> type) {
		if (this.dynamicPerceptionTree!=null && type.isInstance(this.dynamicPerceptionTree)) {
			return type.cast(this.dynamicPerceptionTree);
		}
		if (this.staticPerceptionTree!=null && type.isInstance(this.staticPerceptionTree)) {
			return type.cast(this.staticPerceptionTree);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void registerActions(Clock time,
			Iterable<EnvironmentalAction3D> actions) {
		if (this.dynamicPerceptionTree!=null) {
			if (this.treeManipulator==null) {
					this.treeManipulator = this.dynamicPerceptionTree.getManipulator();
					assert(this.treeManipulator!=null);
			}
			
			MobileEntity3D<MB> obj;
			Transform3D transform;
			AxisAngle4d rotation;
	
			for(EnvironmentalAction3D action : actions) {
				obj = action.getEnvironmentalObject(MobileEntity3D.class);
				if (obj!=null) {
					transform = action.getTransformation();
					rotation = transform.getRotation();
					assert(!Double.isNaN(rotation.x)&&!Double.isNaN(rotation.y)&&!Double.isNaN(rotation.z)&&!Double.isNaN(rotation.angle))
						: rotation.toString();
					if (rotation.angle!=0.)
						this.treeManipulator.rotate(rotation.x, rotation.y, rotation.z, rotation.angle, obj);
					assert(!Double.isNaN(transform.getTranslationX())&&!Double.isNaN(transform.getTranslationY())&&!Double.isNaN(transform.getTranslationZ()))
					: rotation.toString();
					this.treeManipulator.translate(
							transform.getTranslationX(),
							transform.getTranslationY(),
							transform.getTranslationZ(),
							obj);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		if (this.treeManipulator!=null) {
			this.treeManipulator.commit();
			this.treeManipulator = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void registerMobileEntity(MobileEntity3D<MB> entity) {
		Place3D<SB,MB> placeInstance = this.place!=null ? this.place.get() : null;
		assert(placeInstance!=null);

		if (this.dynamicPerceptionTree!=null) {
			// Ensure that the body keeps on floor
			Ground ground = placeInstance.getGround();
			if (ground!=null && entity.isOnGround()) {
				Point3d position = entity.getPosition3D();
				double height = ground.getHeightAt(position.x, position.y);
				entity.translate(0, 0, height - position.z);
			}
			
			if (this.treeManipulator==null) {
				this.treeManipulator = this.dynamicPerceptionTree.getManipulator();
				assert(this.treeManipulator!=null);
			}
			
			this.treeManipulator.insert(entity);
			
			if (entity instanceof InfluencerMobileEntity3D) {
				InfluencerMobileEntity3D<?,?> influencer = (InfluencerMobileEntity3D<?,?>)entity;
				influencer.setEnvironment(placeInstance);
			}

			if (entity instanceof AgentBody3D) {
				AgentBody3D<?,?,MB> agBody = (AgentBody3D<?,?,MB>)entity;
				this.perceptions.put(agBody, null);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregisterMobileEntity(MobileEntity3D<MB> entity) {
		if (this.treeManipulator==null) {
			this.treeManipulator = this.dynamicPerceptionTree.getManipulator();
			assert(this.treeManipulator!=null);
		}
		
		this.treeManipulator.remove(entity);
		
		if(entity instanceof AgentBody3D<?,?,?>) {
			this.perceptions.remove(entity);
		}
		
		if(entity instanceof InfluencerMobileEntity3D<?,?>) {
			((InfluencerMobileEntity3D<?,?>)entity).setEnvironment(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<? extends MobileEntity3D<MB>> getMobileEntities() {
		return this.dynamicPerceptionTree==null
		? CollectionUtil.<MobileEntity3D<MB>>emptyIterator() : 
		this.dynamicPerceptionTree.dataDepthFirstIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<? extends Entity3D<SB>> getStaticEntities() {
		return this.staticPerceptionTree==null
		? CollectionUtil.<Entity3D<SB>>emptyIterator() : 
		this.staticPerceptionTree.dataDepthFirstIterator();
	}
		
	/** Initialize this world model.
	 * 
	 * @param dynamicTree is the tree which must contains the mobile entities.
	 * @param staticTree is the tree which must contains the static entities.
	 * @param placeToInit is the place for which this model manager is created.
	 */
	@SuppressWarnings("unchecked")
	public void init(
			DynamicPerceptionTree<CombinableBounds3D,MB,MobileEntity3D<MB>,?,?,?,? extends TreeManipulator3D<MB,?,?>> dynamicTree,
			StaticPerceptionTree<CombinableBounds3D,SB,Entity3D<SB>,?,?> staticTree,
			Place3D<SB,MB> placeToInit) {
		if (staticTree==null || staticTree.getUserDataCount()<=0)
			this.staticPerceptionTree = null;
		else
			this.staticPerceptionTree = staticTree;
		this.dynamicPerceptionTree = dynamicTree;

		this.place = new WeakReference<Place3D<SB,MB>>(placeToInit);
		
		this.perceptions.clear();
		
		if (dynamicTree!=null) {
			Iterator<? extends MobileEntity3D<MB>> iterator = dynamicTree.dataDepthFirstIterator(DepthFirstNodeOrder.PREFIX);
			MobileEntity3D<MB> body;
			
			while (iterator.hasNext()) {
				body = iterator.next();
				if (body instanceof InfluencerMobileEntity3D) {
					// Attach the agent body to its body container
					((InfluencerMobileEntity3D<?,?>)body).setEnvironment(placeToInit);
				}
				if (body instanceof AgentBody3D) {
					//Add the dynamic entities that are AgentBodies to the Maps : body repository and perception repository
					this.perceptions.put((AgentBody3D<?,?,MB>)body, null);
				}
			}
		}
	}
	
	/** Destroy this world model.
	 */
	public void destroy() {
		this.perceptions.clear();
		
		if (this.staticPerceptionTree!=null)
			this.staticPerceptionTree.clear();
		this.staticPerceptionTree = null;
		
		if (this.dynamicPerceptionTree!=null)
			this.dynamicPerceptionTree.clear();
		this.dynamicPerceptionTree = null;
	}

}