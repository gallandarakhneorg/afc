/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, 2012, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point2d;
import javax.vecmath.Tuple2d;
import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Direction1D5;
import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.object.Segment1D;
import fr.utbm.set.geom.system.CoordinateSystem2D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.transform.Transform1D5;
import fr.utbm.set.geom.transform.Transformable1D5;
import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.MathUtil;
import fr.utbm.set.math.MeasureUnitUtil;
import fr.utbm.set.math.SpeedUnit;
import fr.utbm.set.physics.PhysicsUtil;



/** Represents a mobile entity in a 1.5D environment.
 * <p>
 * A 1.5D mobile is defined by:
 * <ul>
 * <li>its 1.5D position: a Segment1D (here a RoadSegment), a curviline position on the segment and a jutting distance;</li>
 * <li>its entry point on the Segment1D to define its moving direction.</li>
 * </ul>
 * 
 * @param <B> is the type of the bounding box associated to this entity.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class MobileEntity1D5<B extends CombinableBounds1D5<RoadSegment>>
extends Entity1D5<B>
implements MobileEntity<B>, Transformable1D5<RoadSegment> {

	private static final long serialVersionUID = 4139402584861144368L;

	private transient WeakReference<Place<?,?,? extends MobileEntity<B>>> place;

	/**
	 * The entry point of this entity on the road segment. Must be one of the road connections of the segment.
	 */
	private RoadConnection roadEntry;

	private final Vector2d linearVelocity = new Vector2d();

	private transient double linearAcceleration;

	/** Create the bounds for a set of points.
	 * 
	 * @param <B> is the type of the bounding box associated to this entity.
	 * @param boundClass is the type of the bounding box associated to this entity.
	 * @param segment the road segment on which the box will be
	 * @param lower lower position of the box
	 * @param upper upper position of the box
	 * @param juttingDistance jutting of the box
	 * @param lateralSize lateral size of the box
	 * @return the bounds
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	protected static <B extends CombinableBounds1D5<RoadSegment>> B createBoundsFromBox(Class<? extends B> boundClass, RoadSegment segment, double lower, double upper, double juttingDistance, double lateralSize) {
		try {
			Constructor<? extends B> cons = boundClass.getConstructor(RoadSegment.class);
			B bounds = cons.newInstance(segment);
			bounds.setBox(lower, upper, juttingDistance, lateralSize);
			return bounds;
		}
		catch(Exception _) {
			try {
				Constructor<? extends B> cons = boundClass.getConstructor(Segment1D.class);
				B bounds = cons.newInstance(segment);
				bounds.setBox(lower, upper, juttingDistance, lateralSize);
				return bounds;
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

	}


	/**
	 * Generate a bounding box from a set of points. The lower and upper point will be used to compute the size of the bounding box, and it will have a lateral size and jutting of 0
	 * @param <B>
	 * @param boundClass is the type of the bounding box associated to this entity.
	 * @param roadSegment the segment on which the boxes entity will be located
	 * @param convexHull a list of points composing the hull. The lower and upper point will be taken to compute the bounding box.
	 * @return the bounds
	 * @throws IllegalArgumentException if no points are specified
	 */
	protected static <B extends CombinableBounds1D5<RoadSegment>> B createBoundsFromPoints(Class<? extends B> boundClass, RoadSegment roadSegment, double... convexHull) {
		if ( convexHull == null || convexHull.length == 0)
			throw new IllegalArgumentException();
		double lower, upper;
		lower = upper = convexHull[0];
		for ( int i = 1 ; i < convexHull.length ; ++i )	{
			if ( convexHull[i] < lower )
				lower = convexHull[i];
			if ( convexHull[i] > upper )
				upper = convexHull[i];
		}
		return createBoundsFromBox(boundClass, roadSegment, lower, upper, 0, 0);
	}


	/**
	 * Generate a bounding box from a set of points. The lower and upper point will be used to compute the size of the bounding box, and it will have a lateral size and jutting of 0
	 * @param <B>
	 * @param boundClass is the type of the bounding box associated to this entity.
	 * @param roadSegment the segment on which the boxes entity will be located
	 * @param convexHull a list of points composing the hull. The lower and upper point will be taken to compute the bounding box.
	 * @return the bounds
	 * @throws IllegalArgumentException if no points are specified
	 */
	protected static<B extends CombinableBounds1D5<RoadSegment>> B createBoundsFromPoints(Class<? extends B> boundClass,
			RoadSegment roadSegment, Tuple2d... convexHull) {
		if ( convexHull == null || convexHull.length == 0)
			throw new IllegalArgumentException();
		double lx, ly, ux, uy;
		lx = ux = convexHull[0].x;
		ly = uy = convexHull[0].y;
		for ( int i = 1 ; i < convexHull.length ; ++i )	{
			if ( convexHull[i].x < lx )
				lx = convexHull[i].x;
			if ( convexHull[i].x > ux )
				ux = convexHull[i].x;
			if ( convexHull[i].y < ly )
				ly = convexHull[i].y;
			if ( convexHull[i].y > uy )
				uy = convexHull[i].y;
		}
		double lateralSize = ( uy - ly );
		double jutting = ly + lateralSize/2d;
		return  createBoundsFromBox(boundClass, roadSegment, 
				lx, ux, jutting, lateralSize);
	}


	/**
	 * @param boundClass 
	 * @param type is the type of this entity.
	 * @param roadSegment 
	 * @param roadEntry 
	 * @param convexHull is the convex hull of the entity. All the points will
	 * relative to the specified hull center (after computation).
	 */
	public MobileEntity1D5(Semantic type, Class<? extends B> boundClass,  RoadSegment roadSegment, RoadConnection roadEntry, double... convexHull ) {
		this(createBoundsFromPoints(boundClass, roadSegment, convexHull), type, roadEntry);
	}

	/**
	 * @param boundClass 
	 * @param type is the type of this entity.
	 * @param roadSegment 
	 * @param roadEntry 
	 * @param convexHull is the convex hull of the entity. All the points will
	 * relative to the specified hull center (after computation).
	 */
	public MobileEntity1D5(Semantic type, Class<? extends B> boundClass,  RoadSegment roadSegment, RoadConnection roadEntry, Tuple2d... convexHull ) {
		this(createBoundsFromPoints(boundClass, roadSegment, convexHull), type, roadEntry);
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
	public MobileEntity1D5(UUID identifier, Semantic type, Class<? extends B> boundClass, RoadSegment roadSegment, RoadConnection roadEntry, double... convexHull) {
		this(identifier, createBoundsFromPoints(boundClass, roadSegment, convexHull), type, roadEntry);
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
	public MobileEntity1D5(UUID identifier, Semantic type, Class<? extends B> boundClass, RoadSegment roadSegment, RoadConnection roadEntry, Tuple2d... convexHull) {
		this(identifier, createBoundsFromPoints(boundClass, roadSegment, convexHull), type, roadEntry);
	}



	/**
	 * @param bounds 
	 * @param type is the type of this entity.
	 * @param roadEntry 
	 */
	public MobileEntity1D5(B bounds, Semantic type, RoadConnection roadEntry) {
		super(bounds, type);
		assert(roadEntry != null);
		assert(this.bounds!=null);
		RoadSegment sgmt = this.bounds.getSegment();
		assert(sgmt!=null);
		assert(roadEntry.equals( sgmt.getBeginPoint() ) || roadEntry.equals( sgmt.getEndPoint() ) );
		this.roadEntry = roadEntry;
		this.linearVelocity.set(0, 0);
		this.linearAcceleration = 0.;
	}

	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param bounds 
	 * @param type is the type of this entity.
	 * @param roadEntry is the entry point of the entity on the road segment
	 * relative to the specified hull center (after computation).
	 */
	public MobileEntity1D5(UUID identifier, B bounds, Semantic type, RoadConnection roadEntry) {
		super(identifier, bounds, type);
		assert(roadEntry != null);
		assert(this.bounds!=null);
		RoadSegment sgmt = this.bounds.getSegment();
		assert(sgmt!=null);
		assert(roadEntry.equals( sgmt.getBeginPoint() ) || roadEntry.equals( sgmt.getEndPoint() ) );
		this.roadEntry = roadEntry;
		this.linearVelocity.set(0, 0);
		this.linearAcceleration = 0.;
	}

	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param bounds 
	 * @param type is the type of this entity.
	 */
	public MobileEntity1D5(UUID identifier, B bounds, Semantic type) {
		super(identifier, bounds, type);
		assert(this.bounds!=null);
		this.roadEntry = null;
		this.linearVelocity.set(0, 0);
		this.linearAcceleration = 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Place<?,?,? extends MobileEntity<B>> getPlace() {
		return this.place==null ? null : this.place.get();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPlace(Place<?,?,? extends MobileEntity<B>> place) {
		this.place = (place==null) ? null : new WeakReference<Place<?,?,? extends MobileEntity<B>>>(place);
	}

	/** Clamp the curviline segment to the current segment.
	 * 
	 * @param position
	 * @return the clamped position.
	 */
	protected final double clamp(double position) {
		double length = this.bounds.getSegment().getLength();
		return MathUtil.clamp(position, 0, length);
	}

	/** Clamp the curviline segment to the current segment.
	 * 
	 * @return the clamped position.
	 */
	protected final double clamp() {
		double curviline = clamp(getPosition1D().getCurvilineCoordinate());
		double halfSize = this.bounds.getSizeX()/2.;
		this.bounds.set(curviline-halfSize, curviline+halfSize);
		return curviline;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void setTranslation(double curvilinePos) {
		setTranslation(curvilinePos, this.bounds.getJutting(), true);
	}


	/** {@inheritDoc}
	 */
	@Override
	public final void setTranslation(Tuple2d position) {
		setTranslation(position.x, position.y, true);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void translate(Tuple2d v) {
		translate(null, v.x, v.y);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void translate(double delta) {
		translate(null, delta, 0.);
	}

	/**
	 * Jutts the entity.
	 * @param djutting the value the entity must jutt
	 */
	public final void jutt(double djutting) {
		this.bounds.setJutting(this.bounds.getJutting()+djutting);
	}

	/**
	 * Sets the jutting
	 * @param jutting the new jutting of the entity
	 */
	public final void setJutting(double jutting) {
		this.bounds.setJutting(jutting);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Transform1D5<RoadSegment> getTransformMatrix() {
		Point1D5 position = this.bounds.getPosition();
		return new Transform1D5<RoadSegment>(
				Collections.singletonList((RoadSegment)position.getSegment()),
				position.getCurvilineCoordinate(), position.getJuttingDistance());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Point1D5 getTranslation() {
		return this.bounds.getPosition();
	}

	/**
	 * Defines the road segment on which this entity is placed
	 * @param roadSegment the road segment on which this entity must be located (never <code>null</code>)
	 * @param connection the road entry point (never <code>null</code>)
	 */
	public final void setRoadSegment(RoadSegment roadSegment, RoadConnection connection) {
		assert(roadSegment!=null);
		assert(connection!=null);
		assert(roadSegment.getBeginPoint().equals(connection) || roadSegment.getEndPoint().equals(connection));
		this.roadEntry = connection;
		this.bounds.setSegment(roadSegment);
		clamp();
	}


	/**
	 * Returns the road entry point on the segment of this entity
	 * @return the road connection on which this entity entered the segment
	 */
	public final RoadConnection getRoadEntry() {
		return this.roadEntry;
	}

	/**
	 * Returns the connection of the segment this entity is heading to. ( = the opposite of the entry point of the entity on the road)
	 * @return the connection this entity is heading to.
	 */
	public final RoadConnection getRoadExit() {
		RoadConnection con = getRoadEntry();
		if (con==null) return null;
		RoadSegment sgmt = this.bounds.getSegment();
		if (sgmt==null) return null;
		return sgmt.getOtherSidePoint(con);
	}

	/**
	 * Returns the direction of this entity on the road segment.
	 * @return the direction, or <code>null</code> if the direction
	 * cannot be computed.
	 */
	public final Direction1D getDirectionOnRoad() {
		RoadConnection con = getRoadEntry();
		if (con==null) return null;
		RoadSegment sgmt = this.bounds.getSegment();
		if (sgmt==null) return null;
		if (con.equals(sgmt.getBeginPoint()))
			return Direction1D.SEGMENT_DIRECTION;
		if (con.equals(sgmt.getEndPoint()))
			return Direction1D.REVERTED_DIRECTION;
		return null;
	}

	/**
	 * Makes the entity turn back so it will move in the other direction on its segment.
	 * The entry point on the segment and the jutting distance should be inverted according
	 * to there respective semantics.
	 */
	public final void turnBack() {
		RoadConnection currentEntry = getRoadEntry();
		RoadSegment sgmt = this.bounds.getSegment();
		if (currentEntry!=null && sgmt!=null) {
			RoadConnection otherEntry = sgmt.getOtherSidePoint(currentEntry);
			if (otherEntry!=currentEntry) {
				this.roadEntry = otherEntry;
				this.bounds.setJutting(-this.bounds.getJutting());
				this.linearVelocity.set(0, 0);
				this.linearAcceleration = 0.;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setIdentityTransform() {
		setTranslation(0., 0., true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setTranslation(Point1D5 position) {
		assert(position!=null);
		Segment1D path = position.getSegment();
		if (path!=null && path instanceof RoadSegment)
			this.bounds.setSegment((RoadSegment)path);
		setTranslation(position.getCurvilineCoordinate(), position.getJuttingDistance(), true);
	}

	/**
	 * Set the position of the entity and the entry point on the segment.
	 * 
	 * @param position is the position according to the starting point of the segment.
	 * @param roadEntry is the entry point on the segment.
	 */
	public final void setTranslation(Point1D5 position, RoadConnection roadEntry) {
		if (roadEntry!=null) this.roadEntry = roadEntry;
		setTranslation(position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setTranslation(Tuple2f position) {
		setTranslation(position.x, position.y, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void translate(Tuple2f v) {
		translate(null, v.x, v.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void translate(List<? extends RoadSegment> path, double distance) {
		translate(path, distance, 0.);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void translate(List<? extends RoadSegment> path, Tuple2d dpos) {
		translate(path, dpos.x, dpos.y);
	}


	/** {@inheritDoc}
	 */
	@Override
	public final void translate(List<? extends RoadSegment> path, Tuple2f dpos) {
		translate(path, dpos.x, dpos.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setTransform(Transform1D5<RoadSegment> position) {
		assert(position!=null);
		List<? extends Segment1D> path = position.getPath();
		if (path!=null && !path.isEmpty()) {
			Segment1D requestedSegment = path.get(0);
			if (requestedSegment instanceof RoadSegment) {
				this.bounds.setSegment((RoadSegment)requestedSegment);
			}
		}
		setTranslation(position.getCurvilineTransformation(), position.getJuttingTransformation(), true);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void setTranslation(double curvilinePos, double jutting) {
		setTranslation(curvilinePos, jutting, true);
	}

	private void setTranslation(double curvilinePos, double jutting, boolean updateLinearSpeed) {
		// We clamp the curviline pos between [0;segmentLength]
		double clampedCurvilinePos = clamp(curvilinePos);

		double halfSize = this.bounds.getSizeX()/2.;
		this.bounds.set(clampedCurvilinePos-halfSize, clampedCurvilinePos+halfSize);
		this.bounds.setJutting(jutting);
		if (updateLinearSpeed) {
			this.linearVelocity.set(0, 0);
			this.linearAcceleration = 0.;
		}

		setPositionOnChildren(clampedCurvilinePos, jutting, getRoadSegment(), getRoadEntry());
	}

	@Override
	public final void translate(double dpos, double djutting) {
		translate(null, dpos, djutting);
	}

	/**
	 * Translates this entity's position.
	 * 
	 * @param path a list of segment to follow when the position is greater that the size of the current segment, including the current pos' segment
	 * @param dpos the value to add to the entity's position
	 * @param djutting the value the entity will jutt
	 */
	public final void translate(List<? extends RoadSegment> path, double dpos, double djutting) {
		Transform1D5<RoadSegment> transform;
		if (path==null || path.isEmpty())
			transform = new Transform1D5<RoadSegment>(dpos, djutting);
		else
			transform = new Transform1D5<RoadSegment>(path, dpos, djutting);
		transform(transform);
	}

	@Override
	public void transform(Transform1D5<RoadSegment> transform) {
		Point1D5 position = this.bounds.getPosition();
		RoadConnection entryPoint;
		try {
			entryPoint = transform(position, transform);
		}
		catch(IllegalArgumentException e) {
			onInvalidTransformation(transform, e);
			return;
		}
		
		assert(!Double.isNaN(position.getCurvilineCoordinate())) : 
			"NaN curviline position for entity "+getIdentifier().toString() //$NON-NLS-1$
			+"\nOriginal position: "+this.bounds.getPosition() //$NON-NLS-1$
			+"\nTransformation to apply: " + transform.toString() //$NON-NLS-1$
			+"\nEntry point: "+getRoadEntry(); //$NON-NLS-1$
		
		assert(!Double.isNaN(position.getJuttingDistance())) : 
			"NaN jutting/shifting position for entity "+getIdentifier().toString() //$NON-NLS-1$
			+"\nOriginal position: "+this.bounds.getPosition() //$NON-NLS-1$
			+"\nTransformation to apply: " + transform.toString() //$NON-NLS-1$
			+"\nEntry point: "+getRoadEntry(); //$NON-NLS-1$

		if (entryPoint!=null) {
			this.roadEntry = entryPoint;
		}
		Segment1D path = position.getSegment();
		if (path!=null && path instanceof RoadSegment) {
			RoadSegment segment = (RoadSegment)path;
			this.bounds.setSegment(segment);
		}

		setTranslation(position.getCurvilineCoordinate(), position.getJuttingDistance(), false);
	}
	
	/** Invoked when the transformation given to {@link #transform(Transform1D5)}
	 * cannot be applied.
	 * <p>
	 * The default implementation of this function calls {@link #updateLinearSpeed(double, double)}
	 * with zero as parameters.
	 * 
	 * @param transform is the failing transformation.
	 * @param error is the cause of the error.
	 */
	protected void onInvalidTransformation(Transform1D5<RoadSegment> transform, IllegalArgumentException error) {
		updateLinearSpeed(0, 0);
	}
	
	/**
	 * Applies the given transform to the given point.
	 *
	 * @param position is the position to transform.
	 * @param transform is the transformation to apply.
	 * @return the new entry point that is corresponding to the new position of the mobile entity; or <code>null</code>
	 * if the entry point should not be changed. 
	 * @throws IllegalArgumentException when the given transformation cannot be applied to this object: the entity is not on the first segment of the path...
	 */
	private RoadConnection transform(Point1D5 position, Transform1D5<RoadSegment> transform) {
		RoadConnection entryPoint = getRoadEntry();
		assert(entryPoint!=null);

		// Test if the direction of the first segment of the path
		// corresponds to the current direction of the entity
		if (transform.getFirstSegmentPathDirection().isSegmentDirection()) {
			if (getDirectionOnRoad()!=Direction1D.SEGMENT_DIRECTION) {
				throw new IllegalArgumentException("Invalid path: the entity has not the same direction as the first segment of the path"); //$NON-NLS-1$
			}
		}
		else {
			if (getDirectionOnRoad()!=Direction1D.REVERTED_DIRECTION) {
				throw new IllegalArgumentException("Invalid path: the entity has not the same direction as the first segment of the path"); //$NON-NLS-1$
			}
		}
		
		boolean updateEntryPoint = false;

		Direction1D5 motion = new Direction1D5();
		int index = transform.transform(position, motion);
		
		double dx = motion.getX();
		double dy = motion.getY();

		if (index<0) throw new IllegalArgumentException("The entity is not located on the first segment of the path"); //$NON-NLS-1$

		List<RoadSegment> path = transform.getPath();
		if (path!=null && index>0) {
			// We need to update the entry point of the entity, maybe a bit dirty
			// TODO Reimplement the Transform1D5 API to obtain the new connexion quickly
			updateEntryPoint = true;
			Iterator<RoadSegment> it = path.iterator();
			int i=0;
			while (it.hasNext() && i<index) {
				entryPoint = it.next().getOtherSidePoint(entryPoint);
				if (entryPoint==null)
					throw new IllegalArgumentException("Cannot obtain the new entry point from the path"); //$NON-NLS-1$
				++i;
			}
		}

		// Be sure that the segment on which the entity is located
		// is not a wrapping segment.
		RoadSegment segment = (RoadSegment)position.getSegment();
		RoadSegment wrappedSegment = segment.getWrappedRoadSegment();
		while (wrappedSegment!=segment) {
			segment = wrappedSegment;
			wrappedSegment = segment.getWrappedRoadSegment();
		}
		position.setSegment(segment);

		updateLinearSpeed(dx, dy);

		return updateEntryPoint ? entryPoint : null;
	}

	/** Update the linear speed attribute according to the current simulation clock
	 * and the given movement.
	 * 
	 * @param dx is the curviline motion 
	 * @param dy is the lateral motion 
	 */
	protected void updateLinearSpeed(double dx, double dy) {
		double previousSpeed = getLinearSpeed();
		double speed = 0.;
		double acceleration = 0.;
		Place<?,?,? extends MobileEntity<B>> p = getPlace();
		if (p!=null) {
			Clock clock = p.getSimulationClock();
			if (clock!=null) {
				double dt = clock.getSimulationStepDuration(TimeUnit.SECONDS);
				double l = Math.sqrt(dx*dx+dy*dy);
				speed = PhysicsUtil.speed(l, dt);
				acceleration = PhysicsUtil.acceleration(previousSpeed, speed, dt);
			}
		}

		this.linearVelocity.set(dx, dy);
		if (this.linearVelocity.lengthSquared()!=0) {
			this.linearVelocity.normalize();
			this.linearVelocity.scale(speed);
		}
		this.linearAcceleration = acceleration;
	}

	@Override
	public double getAngularAcceleration() {
		return 0.;
	}

	@Override
	public double getAngularAcceleration(AngularUnit unit) {
		return 0.;
	}

	@Override
	public double getAngularSpeed() {
		return 0;
	}

	@Override
	public double getAngularSpeed(AngularUnit unit) {
		return 0;
	}

	@Override
	public double getLinearAcceleration() {
		return this.linearAcceleration;
	}

	@Override
	public double getLinearAcceleration(SpeedUnit unit) {
		return MeasureUnitUtil.fromMetersPerSecond(this.linearAcceleration, unit);
	}

	@Override
	public double getLinearSpeed() {
		return this.linearVelocity.length();
	}

	@Override
	public double getLinearSpeed(SpeedUnit unit) {
		return MeasureUnitUtil.fromMetersPerSecond(getLinearSpeed(), unit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3d getLinearVelocity3D() {
		Point1D5 position = getPosition1D5();
		RoadSegment segment = (RoadSegment)position.getSegment();
		Vector3d move = new Vector3d();
		CoordinateSystem2D cs2d = CoordinateSystem2D.getDefaultCoordinateSystem();
		CoordinateSystem3D cs3d = CoordinateSystem3D.getDefaultCoordinateSystem();
		Point2d p = segment.getGeoLocationForDistance(
				position.getCurvilineCoordinate()+this.linearVelocity.x,
				position.getJuttingDistance()+this.linearVelocity.y,
				cs2d);
		move.sub(cs3d.fromCoordinateSystem2D(p), position.toPoint3D(cs3d));
		move.normalize();
		move.scale(this.linearVelocity.length());
		return move;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2d getLinearVelocity2D() {
		Point1D5 position = getPosition1D5();
		RoadSegment segment = (RoadSegment)position.getSegment();
		Direction2D move = new Direction2D();
		CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
		Point2d p = segment.getGeoLocationForDistance(
				position.getCurvilineCoordinate()+this.linearVelocity.x,
				position.getJuttingDistance()+this.linearVelocity.y,
				cs);
		move.sub(p, position.toPoint2D(cs));
		move.normalize();
		move.scale(this.linearVelocity.length());
		return move;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2d getLinearVelocity1D5() {
		return new Vector2d(this.linearVelocity);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getLinearVelocity1D() {
		return this.linearVelocity.x;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public AxisAngle4d getAngularVelocity3D() {
		return new AxisAngle4d();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getAngularVelocity2D() {
		return 0;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getAngularVelocity1D5() {
		return 0;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getAngularVelocity1D() {
		return 0;
	}

	/** Invoked when child objects should be transformed with the same parameters as this entity.
	 * 
	 * @param curvilinePos the curviline position independently about the road entry point
	 * @param jutting the lateral position independently about the road entry point
	 * @param roadSegment the road segment.
	 * @param roadEntry the road connection from which this entity was entering on the segment.
	 */
	protected void setPositionOnChildren(double curvilinePos, double jutting, RoadSegment roadSegment, RoadConnection roadEntry) {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString() + " | speed=" + getLinearSpeed() + " | acc=" + getLinearAcceleration();  //$NON-NLS-1$//$NON-NLS-2$
	}
}