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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Quat4d;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.system.CoordinateSystem2D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.KillInfluence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceptions;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum1D5;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGenerator;
import fr.utbm.set.jasim.environment.semantics.BodyType;
import fr.utbm.set.jasim.environment.semantics.ObjectType;

/**
 * This class describes the body of an situated agent inside a 1D5 environment.
 * The body is the only available interaction mean between
 * an agent and the environment.
 * 
 * @param <INF> The type of influence this body may receive and forward to the collector
 * @param <PT> The type of perception this body may receive
 * @param <B> The type of Bound associated to the 3D representation of this body in its environment
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AgentBody1D5<INF extends Influence, 
		                           PT extends Perception, 
		                           B extends CombinableBounds1D5<RoadSegment>>
extends InfluencerMobileEntity1D5<INF,B>
implements AgentBody<INF,PT> {
		
	private static final long serialVersionUID = 2614406429186329442L;

	private final PseudoHamelDimension dimension;
	
	/**
	 * The list of the various 3D frustums used by this body to perceive its environment
	 */
	private List<Frustum1D5> frustums;
	
	/** Object that permits to filter the perceptions according to the agent's interest.
	 */
	private InterestFilter interestFilter = null;
	
	/** Object that permits to filter the perceptions according to physical constraints.
	 */
	private PhysicalPerceptionAlterator physicalFilter = null;
	
	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param type
	 *            is the type of the body.
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			BodyType type, RoadSegment segment, RoadConnection roadEntry,
			Tuple2d... convexHull) {
		super(ownerIdentifier, type, boundClass, segment, roadEntry, convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param type
	 *            is the type of the body.
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			BodyType type, RoadSegment segment, RoadConnection roadEntry,
			double... convexHull) {
		super(ownerIdentifier, type, boundClass, segment, roadEntry, convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param types
	 *            are the types of the body.
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			ObjectType[] types, RoadSegment segment, RoadConnection roadEntry,
			Tuple2d... convexHull) {
		super(ownerIdentifier, AgentBodyUtil.extractBodyType(types), boundClass, segment, roadEntry, convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
		addSemantic(AgentBodyUtil.removeBodyType(types));
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param types
	 *            are the types of the body.
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			ObjectType[] types, RoadSegment segment, RoadConnection roadEntry,
			double... convexHull) {
		super(ownerIdentifier, AgentBodyUtil.extractBodyType(types), boundClass, segment, roadEntry, convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
		addSemantic(AgentBodyUtil.removeBodyType(types));
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param type
	 *            is the type of the body.
	 * @param roadEntry
	 *            the entry of the body on the segment
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, BodyType type,
			RoadConnection roadEntry) {
		super(ownerIdentifier, bounds, type, roadEntry);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param type
	 *            is the type of the body.
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, BodyType type) {
		super(ownerIdentifier, bounds, type);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param types
	 *            are the types of the body.
	 * @param roadEntry
	 *            the entry of the body on the segment
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, ObjectType[] types,
			RoadConnection roadEntry) {
		super(ownerIdentifier, bounds, AgentBodyUtil.extractBodyType(types), roadEntry);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
		addSemantic(AgentBodyUtil.removeBodyType(types));
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param types
	 *            are the types of the body.
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, ObjectType[] types) {
		super(ownerIdentifier, bounds, AgentBodyUtil.extractBodyType(types));
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
		addSemantic(AgentBodyUtil.removeBodyType(types));
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			RoadSegment segment, RoadConnection roadEntry,
			Tuple2d... convexHull) {
		super(ownerIdentifier, BodyType.BODYTYPE_SINGLETON, boundClass, segment, roadEntry, convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param boundClass
	 * @param segment
	 *            the segment on which the body will lie
	 * @param roadEntry
	 *            the entry of the body on the segment
	 * @param convexHull
	 *            is the convex hull of the entity.
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, Class<? extends B> boundClass,
			RoadSegment segment, RoadConnection roadEntry,
			double... convexHull) {
		super(ownerIdentifier, BodyType.BODYTYPE_SINGLETON, boundClass, segment, roadEntry,
				convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param roadEntry
	 *            the entry of the body on the segment
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, RoadConnection roadEntry) {
		super(ownerIdentifier, bounds, BodyType.BODYTYPE_SINGLETON, roadEntry);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 */
	public AgentBody1D5(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds) {
		super(ownerIdentifier, bounds, BodyType.BODYTYPE_SINGLETON);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}

	/** {@inheritDoc}
	 */
	@Override
	public PseudoHamelDimension getPreferredMathematicalDimension() {
		return this.dimension;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPerceptionFilter(PhysicalPerceptionAlterator filter) {
		this.physicalFilter = filter;
	}

	/** {@inheritDoc}
	 */
	@Override
	public PhysicalPerceptionAlterator getPerceptionFilter() {
		return this.physicalFilter;
	}

	/** {@inheritDoc}
	 */
	@Override
	public InterestFilter getInterestFilter() {
		return this.interestFilter;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setInterestFilter(InterestFilter filter) {
		this.interestFilter = filter;
	}

	/** {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Perceptions<PT> getPerceptions() {
		BodyContainerEnvironment env = getEnvironment();
		if (env!=null) {
			PerceptionGenerator generator = env.getPerceptionGenerator();
			if (generator!=null) {
				return generator.getPerceptions(this, (Class<PT>)AgentBodyUtil.toPerceptionType(this.dimension));
			}
		}
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Direction1D getViewDirection1D() {
		return getViewDirection1D5();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getOrientation1D() {
		return getOrientation1D5();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Direction1D getViewDirection1D5() {
		RoadConnection entry = getRoadEntry();
		if (entry==null) return Direction1D.BOTH_DIRECTIONS;
		RoadSegment sgmt = getRoadSegment();
		assert(sgmt!=null);
		return sgmt.getBeginPoint().equals(entry) ?
				Direction1D.SEGMENT_DIRECTION : Direction1D.REVERTED_DIRECTION;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getOrientation1D5() {
		RoadConnection entry = getRoadEntry();
		if (entry==null) return Double.NaN;
		RoadSegment sgmt = getRoadSegment();
		assert(sgmt!=null);
		return sgmt.getBeginPoint().equals(entry) ? 1. : -1.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Direction2D getViewDirection2D() {
		RoadSegment sgmt = getRoadSegment();
		assert(sgmt!=null);
		RoadConnection entry = getRoadEntry();
		assert(entry!=null);
		Direction2D direction = new Direction2D(sgmt.getTangentAt(getPosition1D().getCurvilineCoordinate()));
		if (!sgmt.getBeginPoint().equals(entry))
			direction.negate();
		return direction;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getOrientation2D() {
		RoadSegment sgmt = getRoadSegment();
		assert(sgmt!=null);
		RoadConnection entry = getRoadEntry();
		assert(entry!=null);
		Direction2D direction = new Direction2D(sgmt.getTangentAt(getPosition1D().getCurvilineCoordinate()));
		if (!sgmt.getBeginPoint().equals(entry))
			direction.negate();
		CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
		Vector2d v = cs.getViewVector();
		return GeometryUtil.clampRadian0To2PI(GeometryUtil.signedAngle(v.x, v.y, direction.x, direction.y));
	}

	/** {@inheritDoc}
	 */
	@Override
	public Direction2D getViewDirection2D5() {
		return getViewDirection2D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getOrientation2D5() {
		return getOrientation2D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Direction3D getViewDirection3D() {
		RoadSegment sgmt = getRoadSegment();
		assert(sgmt!=null);
		RoadConnection entry = getRoadEntry();
		assert(entry!=null);
		Vector2d direction = sgmt.getTangentAt(getPosition1D().getCurvilineCoordinate());
		if (!sgmt.getBeginPoint().equals(entry))
			direction.negate();
		Vector3d v = CoordinateSystem3D.getDefaultCoordinateSystem().fromCoordinateSystem2D(direction);
		return new Direction3D(v, 0.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Quat4d getOrientation3D() {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		double orientation = getOrientation2D();
		AxisAngle4d aa = new AxisAngle4d(cs.getUpVector(), orientation);
		Quat4d q = new Quat4d();
		q.set(aa);
		return q;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void kill() {
		BodyContainerEnvironment env = getEnvironment();
		if (env!=null) {
			InfluenceCollector collector = env.getInfluenceCollector();
			assert(collector!=null);
			collector.registerInfluence(this,this,new KillInfluence(this));
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public Collection<Frustum1D5> getFrustums() {
		if (this.frustums==null) return Collections.emptyList();
		return Collections.unmodifiableList(this.frustums);
	}
	
	/**
	 * Set the list of the various 1.5D frustums used by this body to perceive its environment
	 * 
	 * @param frustumList is the list of the various frustums used by this body to perceive its environment
	 */
	public void setFrustums(List<? extends Frustum1D5> frustumList) {
		this.frustums = new ArrayList<Frustum1D5>(frustumList);
	}

	/**
	 * Set the list of the various 1.5D frustums used by this body to perceive its environment
	 * 
	 * @param frustumList is the list of the various frustums used by this body to perceive its environment
	 */
	public void setFrustums(Frustum1D5... frustumList) {
		this.frustums = new ArrayList<Frustum1D5>(Arrays.asList(frustumList));
	}

	/**
	 * Add the list of the various 1.5D frustums used by this body to perceive its environment.
	 * 
	 * @param frustumList is the list of the various frustums used by this body to perceive its environment
	 */
	public void addFrustums(Frustum1D5... frustumList) {
		if (this.frustums==null) 
			this.frustums = new ArrayList<Frustum1D5>(Arrays.asList(frustumList));
		else
			this.frustums.addAll(Arrays.asList(frustumList));
	}

	/**
	 * Add the list of the various 1.5D frustums used by this body to perceive its environment.
	 * 
	 * @param frustumList is the list of the various frustums used by this body to perceive its environment
	 */
	public void addFrustums(Collection<? extends Frustum1D5> frustumList) {
		if (this.frustums==null) 
			this.frustums = new ArrayList<Frustum1D5>(frustumList);
		else
			this.frustums.addAll(frustumList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setPositionOnChildren(
			double curvilinePos, double jutting,
			RoadSegment roadSegment, RoadConnection roadEntry) {
		if (this.frustums!=null) {
			Point1D5 p = new Point1D5(roadSegment, curvilinePos, jutting);
			for(Frustum1D5 frustum : this.frustums) {
				frustum.setTranslation(roadSegment, roadEntry, p);
			}
		}
	}
	
}
