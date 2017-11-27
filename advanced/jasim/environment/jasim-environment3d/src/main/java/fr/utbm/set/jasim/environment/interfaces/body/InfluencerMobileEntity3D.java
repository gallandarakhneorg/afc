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
package fr.utbm.set.jasim.environment.interfaces.body;

import java.util.UUID;

import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.environment.time.Clock;

/**
 * This class describes an object which could send influences
 * to the environment.
 * 
 * @param <INF> The type of influence this body may receive and forward to the collector
 * @param <B> The type of Bound associated to the 3D representation of this body in its environment
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class InfluencerMobileEntity3D<INF extends Influence, B extends CombinableBounds3D>
extends MobileEntity3D<B>
implements InfluencerEntity<INF> {
	
	private static final long serialVersionUID = -4203572408834537363L;

	/**
	 * The environment in which the agent's body is living
	 */
	private transient BodyContainerEnvironment situatedEnvironment;
	
	/** Last influence application status.
	 */
	private transient InfluenceApplicationStatus lastInfluenceStatus = InfluenceApplicationStatus.SUCCESS;

	/** Error for last influence application.
	 */
	private transient Throwable lastInfluenceError = null;

	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param type is the type of this entity.
	 * @param bounds 
	 * @param pivot is the pivot point located according to the entity position.
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 * @param convexHull is the convex hull of the entity. All the points are
	 * relative to the specified pivot
	 */
	public InfluencerMobileEntity3D(UUID identifier, B bounds, Semantic type, Vector3d pivot, boolean onGround, Mesh3D convexHull) {
		super(identifier, bounds, type, pivot, onGround, convexHull);
	}

	/**
	 * @param bounds 
	 * @param type is the type of this entity.
	 * @param pivot is the pivot point located according to the entity position.
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 * @param convexHull is the convex hull of the entity. All the points are
	 * relative to the specified pivot
	 */
	public InfluencerMobileEntity3D(B bounds, Semantic type, Vector3d pivot, boolean onGround, Mesh3D convexHull) {
		super(bounds, type, pivot, onGround, convexHull);
	}

	/**
	 * @param bounds 
	 * @param type is the type of this entity.
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public InfluencerMobileEntity3D(B bounds, Semantic type, boolean onGround, Mesh3D convexHull) {
		super(bounds, type, onGround, convexHull);
	}
	
	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param type is the type of this entity.
	 * @param bounds 
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public InfluencerMobileEntity3D(UUID identifier, B bounds, Semantic type, boolean onGround, Mesh3D convexHull) {
		super(identifier, bounds, type, onGround, convexHull);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void influence(INF... influence) {
		BodyContainerEnvironment env = getEnvironment();
		assert(env!=null);
		InfluenceCollector influenceCollector = env.getInfluenceCollector();
		assert(influenceCollector!=null);
		influenceCollector.registerInfluence(this,this,influence);
	}
	
	/** Set the environment in which this body is located.
	 * 
	 * @param environment is the environment that is containing this body.
	 */
	public final void setEnvironment(BodyContainerEnvironment environment) {
		this.situatedEnvironment = environment;
	}
	
	/** Replies the environment in which this body is located.
	 * 
	 * @return the environment reference.
	 */
	protected final BodyContainerEnvironment getEnvironment() {
		return this.situatedEnvironment;
	}

	/**
	 * Returns the current simulation time.
	 * 
	 * @return the current simulation time.
	 */
	public Clock getSimulationClock() {
		return getEnvironment().getSimulationClock();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setLastInfluenceStatus(InfluenceApplicationStatus status) {
		this.lastInfluenceStatus = status;
		this.lastInfluenceError = null;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setLastInfluenceStatus(InfluenceApplicationStatus status, Throwable error) {
		this.lastInfluenceStatus = status;
		this.lastInfluenceError = error;
	}

	/** Replies the status of the last influence which was tried to be applied in the environment.
	 * 
	 * @return the status
	 */
	public InfluenceApplicationStatus getLastInfluenceStatus() {
		return this.lastInfluenceStatus==null ? InfluenceApplicationStatus.SUCCESS : this.lastInfluenceStatus;
	}

	/** Replies the error for the last influence which was tried to be applied in the environment.
	 * 
	 * @return the error or <code>null</code>.
	 */
	public Throwable getLastInfluenceError() {
		return this.lastInfluenceError;
	}

}
