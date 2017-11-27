/*
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * Copyright (c) 2013, Multiagent Group - IRTES-SET - UTBM
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

import java.lang.ref.WeakReference;
import java.util.UUID;

import javax.vecmath.Tuple2d;

import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus;
import fr.utbm.set.jasim.environment.model.world.MobileEntity1D5;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.geom.transform.Transform1D5;

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
public class InfluencerMobileEntity1D5<INF extends Influence, 
                                       B extends CombinableBounds1D5<RoadSegment>>
extends MobileEntity1D5<B> implements InfluencerEntity<INF> {

	private static final long serialVersionUID = 336348060399262703L;

	/**
	 * The environment in which the agent's body is living
	 */
	private transient WeakReference<BodyContainerEnvironment> situatedEnvironment;
	
	/** Last influence application status.
	 */
	private transient InfluenceApplicationStatus lastInfluenceStatus = InfluenceApplicationStatus.SUCCESS;

	/** Error for the last influence application.
	 */
	private transient Throwable lastInfluenceError = null;

	/**
	 * @param boundClass 
	 * @param type is the type of this entity.
	 * @param roadSegment 
	 * @param roadEntry 
	 * @param convexHull is the convex hull of the entity. All the points will
	 * relative to the specified hull center (after computation).
	 */
	public InfluencerMobileEntity1D5(Semantic type, Class<? extends B> boundClass,  RoadSegment roadSegment, RoadConnection roadEntry, double... convexHull ) {
		super(type, boundClass, roadSegment, roadEntry, convexHull);
	}

	/**
	 * @param boundClass 
	 * @param type is the type of this entity.
	 * @param roadSegment 
	 * @param roadEntry 
	 * @param convexHull is the convex hull of the entity. All the points will
	 * relative to the specified hull center (after computation).
	 */
	public InfluencerMobileEntity1D5(Semantic type, Class<? extends B> boundClass,  RoadSegment roadSegment, RoadConnection roadEntry, Tuple2d... convexHull ) {
		super(type, boundClass, roadSegment, roadEntry, convexHull);
	}



	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param boundClass 
	 * @param type is the type of this entity.
	 * @param roadSegment the segment on which the entity is located
	 * @param roadEntry the entry point of the entity on the segment
	 * @param convexHull is the convex hull of the entity. All the points will
	 * relative to the specified hull center (after computation).
	 */
	public InfluencerMobileEntity1D5(UUID identifier, Semantic type, Class<? extends B> boundClass, RoadSegment roadSegment, RoadConnection roadEntry, double... convexHull) {
		super(identifier, type, boundClass, roadSegment, roadEntry, convexHull);
	}

	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param boundClass 
	 * @param type is the type of this entity.
	 * @param roadSegment the segment on which the entity is located
	 * @param roadEntry the entry point of the entity on the segment
	 * @param convexHull is the convex hull of the entity. All the points will
	 * relative to the specified hull center (after computation).
	 */
	public InfluencerMobileEntity1D5(UUID identifier, Semantic type, Class<? extends B> boundClass, RoadSegment roadSegment, RoadConnection roadEntry, Tuple2d... convexHull) {
		super(identifier, type, boundClass, roadSegment, roadEntry, convexHull);
	}

	/**
	 * @param bounds 
	 * @param type is the type of this entity.
	 * @param roadEntry 
	 */
	public InfluencerMobileEntity1D5(B bounds, Semantic type, RoadConnection roadEntry) {
		super(bounds, type, roadEntry);
	}

	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param bounds 
	 * @param type is the type of this entity.
	 * @param roadEntry is the entry point of the entity on the road segment
	 * relative to the specified hull center (after computation).
	 */
	public InfluencerMobileEntity1D5(UUID identifier, B bounds, Semantic type, RoadConnection roadEntry) {
		super(identifier, bounds, type, roadEntry);
	}

	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param bounds 
	 * @param type is the type of this entity.
	 * relative to the specified hull center (after computation).
	 */
	public InfluencerMobileEntity1D5(UUID identifier, B bounds, Semantic type) {
		super(identifier, bounds, type);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void influence(INF... influence) {
		this.situatedEnvironment.get().getInfluenceCollector().registerInfluence(this,this,influence);
	}
	
	/** Set the environment in which this body is located.
	 * 
	 * @param environment is the environment that is containing this body.
	 */
	public final void setEnvironment(BodyContainerEnvironment environment) {
		this.situatedEnvironment = environment==null ? null : new WeakReference<BodyContainerEnvironment>(environment);
	}
	
	/** Replies the environment in which this body is located.
	 * 
	 * @return the environment reference.
	 */
	protected final BodyContainerEnvironment getEnvironment() {
		return this.situatedEnvironment==null ? null : this.situatedEnvironment.get();
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
	public void setLastInfluenceStatus(InfluenceApplicationStatus status, Throwable e) {
		this.lastInfluenceStatus = status;
		this.lastInfluenceError = e;
	}

	/** Replies the status of the last influence which was tried to be applied in the environment.
	 * 
	 * @return the status
	 */
	public InfluenceApplicationStatus getLastInfluenceStatus() {
		return this.lastInfluenceStatus==null ? InfluenceApplicationStatus.SUCCESS : this.lastInfluenceStatus;
	}

	/** Replies the error of the last influence which was tried to be applied in the environment.
	 * 
	 * @return the error
	 */
	public Throwable getLastInfluenceError() {
		return this.lastInfluenceError;
	}

	/**
	 * Returns the current simulation time.
	 * 
	 * @return the current simulation time.
	 */
	public Clock getSimulationClock() {
		return getEnvironment().getSimulationClock();
	}

	@Override
	protected void onInvalidTransformation(Transform1D5<RoadSegment> transform, IllegalArgumentException error) {
		updateLinearSpeed(0, 0);
		setLastInfluenceStatus(InfluenceApplicationStatus.INVALID_MOVE_VALUE, error);
	}

}
