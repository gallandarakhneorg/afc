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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.KillInfluence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.InterestFilter;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perceptions;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum3D;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGenerator;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;
import fr.utbm.set.jasim.environment.semantics.BodyType;
import fr.utbm.set.jasim.environment.semantics.ObjectType;

/**
 * This class describes the body of an situated agent inside a 3D environment.
 * The body is the only available interaction mean between
 * an agent and the environment.
 * 
 * @param <INF> The type of influence this body may receive and forward to the collector
 * @param <PT> The type of perception this body may receive
 * @param <B> The type of Bound associated to the 3D representation of this body in its environment
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AgentBody3D<INF extends Influence,
								  PT extends Perception,
								  B extends CombinableBounds3D> 
extends InfluencerMobileEntity3D<INF,B>
implements AgentBody<INF,PT> {
	
	private static final long serialVersionUID = 2488335373861201057L;
	
	private transient Direction3D viewDirectionBuffer3D = null;
	private transient Direction2D viewDirectionBuffer2D = null;
	
	private final PseudoHamelDimension dimension;
	
	/**
	 * The list of the various 3D frustums used by this body to perceive its environment
	 */
	private List<Frustum3D> frustums;
	
	/** Object that permits to filter the perceptions according to the agent's interest.
	 */
	private InterestFilter interestFilter = null;
	
	/** Object that permits to filter the perceptions according to physical constraints.
	 */
	private PhysicalPerceptionAlterator physicalFilter = null;

	/**
	 * Builds a new AgentBody3D
	 * @param ownerIdentifier is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds 
	 * @param type is the type of the body.
	 * @param pivot is the pivot expressed in local coordinates from the entity position. 
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public AgentBody3D(UUID ownerIdentifier, Class<PT> perceptionType, B bounds, BodyType type, Vector3d pivot, Mesh3D convexHull) {
		super(ownerIdentifier, bounds, type, pivot, true, convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}

	/**
	 * Builds a new AgentBody3D
	 * @param ownerIdentifier is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds 
	 * @param types are the types of the body.
	 * @param pivot is the pivot expressed in local coordinates from the entity position. 
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public AgentBody3D(UUID ownerIdentifier, Class<PT> perceptionType, B bounds, ObjectType[] types, Vector3d pivot, Mesh3D convexHull) {
		super(ownerIdentifier, bounds, AgentBodyUtil.extractBodyType(types), pivot, true, convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
		addSemantic(AgentBodyUtil.removeBodyType(types));
	}

	/**
	 * Builds a new AgentBody3D
	 * @param ownerIdentifier is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds 
	 * @param type is the type of the body.
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public AgentBody3D(UUID ownerIdentifier, Class<PT> perceptionType, B bounds, BodyType type, Mesh3D convexHull) {
		super(ownerIdentifier, bounds, type, true, convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
	}
	
	/**
	 * Builds a new AgentBody3D
	 * @param ownerIdentifier is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds 
	 * @param types are the types of the body.
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public AgentBody3D(UUID ownerIdentifier, Class<PT> perceptionType, B bounds, ObjectType[] types, Mesh3D convexHull) {
		super(ownerIdentifier, bounds, AgentBodyUtil.extractBodyType(types), true, convexHull);
		this.dimension = AgentBodyUtil.toDimension(perceptionType);
		addSemantic(AgentBodyUtil.removeBodyType(types));
	}

	/**
	 * Builds a new AgentBody3D
	 * @param ownerIdentifier is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds 
	 * @param pivot is the pivot expressed in local coordinates from the entity position. 
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public AgentBody3D(UUID ownerIdentifier, Class<PT> perceptionType, B bounds, Vector3d pivot, Mesh3D convexHull) {
		this(ownerIdentifier, perceptionType, bounds, BodyType.BODYTYPE_SINGLETON, pivot, convexHull);
	}

	/**
	 * Builds a new AgentBody3D
	 * @param ownerIdentifier is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds 
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public AgentBody3D(UUID ownerIdentifier, Class<PT> perceptionType, B bounds, Mesh3D convexHull) {
		this(ownerIdentifier, perceptionType, bounds, BodyType.BODYTYPE_SINGLETON, convexHull);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void clearBuffers() {
		super.clearBuffers();
    	this.viewDirectionBuffer3D = null;
    	this.viewDirectionBuffer2D = null;
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
	public final Perceptions<PT> getPerceptions() {
		BodyContainerEnvironment env = getEnvironment();
		assert(env!=null);
		PerceptionGenerator generator = env.getPerceptionGenerator();
		assert(generator!=null);
		return generator.getPerceptions(this, (Class<PT>)AgentBodyUtil.toPerceptionType(this.dimension));
	}

	/** {@inheritDoc}
	 */
	@Override
	public Direction1D getViewDirection1D() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getOrientation1D() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Direction1D getViewDirection1D5() {
		return getViewDirection1D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getOrientation1D5() {
		return getOrientation1D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Direction2D getViewDirection2D() {
		if (this.viewDirectionBuffer2D==null) {
	    	this.viewDirectionBuffer2D = new Direction2D(GeometryUtil.rotation2viewVector2D(getQuaternion(), CoordinateSystem3D.getDefaultCoordinateSystem()));
	    	assert(this.viewDirectionBuffer2D!=null);
	    	assert GeometryUtil.epsilonEqualsDistance(this.viewDirectionBuffer2D.length(), 1.)
	    	: "Quaternion="+getQuaternion().toString() //$NON-NLS-1$
	    	+"; View="+this.viewDirectionBuffer2D.toString() //$NON-NLS-1$
	    	+"; Length="+this.viewDirectionBuffer2D.length(); //$NON-NLS-1$
		}
		return new Direction2D(this.viewDirectionBuffer2D);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getOrientation2D() {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		return cs.toCoordinateSystem2D(getQuaternion());
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
		if (this.viewDirectionBuffer3D==null) {
	    	this.viewDirectionBuffer3D = new Direction3D(GeometryUtil.rotation2viewVector3D(getQuaternion(), CoordinateSystem3D.getDefaultCoordinateSystem()));
	    	assert(this.viewDirectionBuffer3D!=null);
	    	assert GeometryUtil.epsilonEqualsDistance(
	    			GeometryUtil.distance(this.viewDirectionBuffer3D.x,this.viewDirectionBuffer3D.y,this.viewDirectionBuffer3D.z), 1.)
	    			: GeometryUtil.distance(this.viewDirectionBuffer3D.x,this.viewDirectionBuffer3D.y,this.viewDirectionBuffer3D.z);
		}
		return new Direction3D(this.viewDirectionBuffer3D);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Quat4d getOrientation3D() {
		return getQuaternion();
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
	public Collection<Frustum3D> getFrustums() {
		if (this.frustums==null) return Collections.emptyList();
		return Collections.unmodifiableList(this.frustums);
	}

	/**
	 * Set the list of the various 3D frustums used by this body to perceive its environment
	 * 
	 * @param frustumList is the list of the various frustums used by this body to perceive its environment
	 */
	public void setFrustums(List<? extends Frustum3D> frustumList) {
		this.frustums = new ArrayList<Frustum3D>(frustumList);
	}

	/**
	 * Set the list of the various 3D frustums used by this body to perceive its environment
	 * 
	 * @param frustumList is the list of the various frustums used by this body to perceive its environment
	 */
	public void setFrustums(Frustum3D... frustumList) {
		this.frustums = new ArrayList<Frustum3D>(Arrays.asList(frustumList));
	}

	/**
	 * Add the list of the various 3D frustums used by this body to perceive its environment.
	 * 
	 * @param frustumList is the list of the various frustums used by this body to perceive its environment
	 */
	public void addFrustums(Frustum3D... frustumList) {
		if (this.frustums==null) 
			this.frustums = new ArrayList<Frustum3D>(Arrays.asList(frustumList));
		else
			this.frustums.addAll(Arrays.asList(frustumList));
	}

	/**
	 * Add the list of the various 3D frustums used by this body to perceive its environment.
	 * 
	 * @param frustumList is the list of the various frustums used by this body to perceive its environment
	 */
	public void addFrustums(Collection<? extends Frustum3D> frustumList) {
		if (this.frustums==null) 
			this.frustums = new ArrayList<Frustum3D>(frustumList);
		else
			this.frustums.addAll(frustumList);
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void translateChildren(Vector3d translation) {
		if (this.frustums!=null) {
			for(Frustum3D f : this.frustums) {
				f.translate(translation);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void setRotationOnChildren(Quat4d rotation) {
		if (this.frustums!=null) {
			for(Frustum3D f : this.frustums) {
				f.setRotation(rotation);
			}
		}
	}

}
