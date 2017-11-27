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
package fr.utbm.set.jasim.environment.model.perception.percepts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.utbm.set.collection.CollectionUtil;
import fr.utbm.set.collection.SizedIterator;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.GroundPerception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceptions;
import fr.utbm.set.jasim.environment.model.perception.frustum.CullingResult;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * This class describes a set of perceptions and their respective classifications
 * for a specific frustum.
 * 
 * @param <S> is the common type of all the perceivable entities. 
 * @param <SE> is the type of the static entities in this perception list. 
 * @param <ME> is the type of the mobile entities in this perception list.
 * @param <C> is the type used to store the culling results. 
 * @param <P> is the type of the perceptions from the agent point of view.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class PerceptionList<S extends WorldEntity<?>,
									 SE extends S,
									 ME extends S,
									 C extends CullingResult<? extends S>,
									 P extends Perception>
implements Perceptions<P> {

	private List<C> mobileObjects = new LinkedList<C>();
	private List<C> staticObjects = new LinkedList<C>();
	private GroundPerception ground = null;
	
	/**
	 */
	public PerceptionList() {
		//
	}
	
	/** Replies if this perception list is supporting the given perception type.
	 *
	 * @param type
	 * @return <code>true</code> if the perception list is supporting the given type, otherwise <code>false</code>.
	 */
	public abstract boolean isSupportedPerceptionType(Class<? extends Perception> type);
	
	/** Offer the given perception result.
	 * 
	 * @param result
	 */
	public void addStaticPerception(C result) {
		this.staticObjects.add(result);
	}
	
	/** Offer the given perception result.
	 * 
	 * @param result
	 */
	public void addDynamicPerception(C result) {
		this.mobileObjects.add(result);
	}
	
	/** Clear the perceptions.
	 */
	public void clear() {
		this.staticObjects.clear();
		this.mobileObjects.clear();
		this.ground = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDynamicPerceptCount() {
		return this.mobileObjects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroundPerception getGroundPerception() {
		return this.ground;
	}

	/**
	 * Set the ground perception.
	 * 
	 * @param perception
	 */
	public void setGroundPerception(GroundPerception perception) {
		this.ground = perception;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getStaticPerceptCount() {
		return this.staticObjects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<P> iterator() {
		return CollectionUtil.mergeIterators(getStaticPercepts(), getDynamicPercepts());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<P> getStaticPercepts() {
		return new PerceptionIterator<SE>(this.staticObjects);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<P> getDynamicPercepts() {
		return new PerceptionIterator<ME>(this.mobileObjects);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getObjectPerceptCount() {
		return this.staticObjects.size()+this.mobileObjects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasGroundPercept() {
		return this.ground!=null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasObjectPercept() {
		return (!this.staticObjects.isEmpty()) || (!this.mobileObjects.isEmpty());
	}

	/** Create an agent perception from the given culling result.
	 * 
	 * @param result
	 * @return the agent perception
	 */
	protected abstract P createPerception(C result);
	
	/**
	 * This class describes a set of perceptions and their respective classifications
	 * for a specific frustum.
	 * 
	 * @param <E> is the type of the entities in this iterator. 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public class PerceptionIterator<E extends S> implements SizedIterator<P> {
		
		private final Iterator<C> iterator;
		private int size;
		private int index;
		
		/**
		 * @param list is the perception result.
		 */
		public PerceptionIterator(List<C> list) {
			this.iterator = list.iterator();
			this.size = list.size();
			this.index = -1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int index() {
			return this.index;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			return this.size;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public P next() {
			C result = this.iterator.next();
			this.index ++;
			P percept = createPerception(result);
			if (percept==null) throw new IllegalStateException();
			return percept;
		}

		@Override
		public void remove() {
			this.iterator.remove();
			this.index --;
			this.size --;
		}
		
	}

}