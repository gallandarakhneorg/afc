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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import fr.utbm.set.geom.bounds.bounds1d.BoundingInterval;
import fr.utbm.set.geom.bounds.bounds1d.Bounds1D;
import fr.utbm.set.geom.bounds.bounds1d5.Bounds1D5;
import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.KinematicPerceivable;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.SteeringPerceivable;
import fr.utbm.set.jasim.environment.semantics.ObjectType;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.environment.semantics.SemanticComparator;



/** This interface represents an object in a 1.5D space
 *
 * @param <B> is the type of the bounding box associated to this entity.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class Entity1D5<B extends Bounds1D5<RoadSegment>> extends AbstractWorldEntity<B> {
	
	private static final long serialVersionUID = 2987659016966019651L;
	
	private Set<Semantic> semantics = null;
	
	private final Semantic type;

	/** Bounds of this entity.
	 */
	protected final B bounds;
	
	/** The height of the ground at the entity location.
	 */
	protected double groundHeight;
	
	/**
	 * @param bounds is the bounds of this entity.
	 * @param type is the type of this entity.
	 */ 
	public Entity1D5(B bounds, Semantic type) {
		super(UUID.randomUUID());
		this.bounds = bounds;
		this.type = type==null ? ObjectType.OBJECTTYPE_SINGLETON : type;
		this.groundHeight = 0.;
	}
		
	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param bounds is the bounds of this entity.
	 * @param type is the type of this entity.
	 */ 
	protected Entity1D5(UUID identifier, B bounds, Semantic type) {
		super(identifier);
		this.bounds = bounds;
		this.type = type==null ? ObjectType.OBJECTTYPE_SINGLETON : type;
		this.groundHeight = 0.;
	}

	/** Set the height of the ground at the position of this entity.
	 * 
	 * @param height
	 */
	public void setGroundHeight(double height) {
		this.groundHeight = height;
	}

	/** Replies the height of the ground at the position of this entity.
	 * 
	 * @return the height
	 */
	public double getGroundHeight() {
		return this.groundHeight;
	}

	/**
	 * Returns the Road Segment
	 * @return the road segment on which this entity is located
	 */
	public RoadSegment getRoadSegment() {
		return this.bounds.getSegment();
	}

	/** {@inheritDoc}
	 */
	@Override
	public B getBounds() {
		return this.bounds;
		
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Semantic getType() {
		return this.type;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Collection<? extends Semantic> getAllSemantics() {
		if (this.semantics==null)
			return Collections.emptySet();
		return Collections.unmodifiableSet(this.semantics);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isA(Semantic entityType) {
		return this.semantics!=null && this.semantics.contains(entityType);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addSemantics(Collection<? extends Semantic> semanticalTag) {
		if (this.semantics==null) 
			this.semantics = new TreeSet<Semantic>(new SemanticComparator());
		this.semantics.addAll(semanticalTag);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeSemantic(Semantic semanticalTag) {
		if (this.semantics==null) return;
		this.semantics.remove(semanticalTag);
		if (this.semantics.isEmpty()) this.semantics = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isInfluencable() {
		return (this instanceof Influencable);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Influencable toInfluencable() {
		if (this instanceof Influencable) return (Influencable)this;
		throw new UnsupportedOperationException("entity is not influencable"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isKinematic() {
		return (this instanceof KinematicPerceivable);
	}

	/** {@inheritDoc}
	 */
	@Override
	public KinematicPerceivable toKinematic() {
		if (this instanceof KinematicPerceivable) return (KinematicPerceivable)this;
		throw new UnsupportedOperationException("entity is not kinematic"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isSteering() {
		return (this instanceof SteeringPerceivable);
	}

	/** {@inheritDoc}
	 */
	@Override
	public SteeringPerceivable toSteering() {
		if (this instanceof SteeringPerceivable) return (SteeringPerceivable)this;
		throw new UnsupportedOperationException("entity is not steering"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public final EuclidianPoint3D getPosition3D() {
		EuclidianPoint2D p2d = this.bounds.getPosition().toPoint2D();
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		return cs.fromCoordinateSystem2D(p2d,getGroundHeight());
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final EuclidianPoint3D getPosition2D5() {
		return getPosition3D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getPosition2D() {
		return this.bounds.getPosition().toPoint2D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D5 getPosition1D5() {
		return this.bounds.getPosition();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D getPosition1D() {
		return this.bounds.getPosition().toPoint1D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds1D<RoadSegment> getBounds1D() {
		return new BoundingInterval<RoadSegment>(
						this.bounds.getSegment(), 
						this.bounds.getMinX(),
						this.bounds.getMaxX());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds1D5<RoadSegment> getBounds1D5() {
		return this.bounds;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D getBounds2D() {
		return this.bounds.toBounds2D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Bounds3D getBounds2D5() {
		return getBounds3D();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds3D getBounds3D() {
		return this.bounds.toBounds3D();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString() + " @ " + getPosition1D5(); //$NON-NLS-1$
	}
	
}