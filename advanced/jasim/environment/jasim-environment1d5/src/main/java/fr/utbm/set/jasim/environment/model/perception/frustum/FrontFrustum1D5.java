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
package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.lang.ref.WeakReference;
import java.util.UUID;

import javax.vecmath.Point2d;

import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.geom.bounds.bounds1d5.BoundingRect1D5;
import fr.utbm.set.geom.object.Point1D5;

/** This frustum is a frustum that permits to perceive at the front
 * of an entity.
 *
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class FrontFrustum1D5 extends BoundingRect1D5<RoadSegment> implements Frustum1D5 {

	private static final long serialVersionUID = -3467511420850661353L;

	private static Point2d mapToBoundsCenter(Point1D5 eyePosition, RoadConnection roadEntry, double forwardDistance) {
		Point2d c = new Point2d();
		
		c.y = eyePosition.getJuttingDistance();

		RoadSegment sgmt = (RoadSegment)eyePosition.getSegment();
		if (roadEntry.equals(sgmt.getBeginPoint())) {
			c.x = eyePosition.getCurvilineCoordinate() + forwardDistance/2.;
		}
		else {
			c.x = eyePosition.getCurvilineCoordinate() - forwardDistance/2.;
		}
		
		return c;
	}
	
	private final UUID id;
	private final double forwardDistance;
	private WeakReference<RoadConnection> roadEntry;
	private transient Point1D5 eyePosition = null;
	
	/**
	 * @param id
	 * @param eyePosition
	 * @param roadEntry
	 * @param forwardDistance
	 * @param sideDistance
	 */
	public FrontFrustum1D5(UUID id, Point1D5 eyePosition, RoadConnection roadEntry, double forwardDistance, double sideDistance) {
		super(
				(RoadSegment)eyePosition.getSegment(),
				mapToBoundsCenter(eyePosition, roadEntry, forwardDistance),
				forwardDistance,
				sideDistance * 2.);
		this.id = id;
		this.forwardDistance = forwardDistance;
		this.roadEntry = new WeakReference<RoadConnection>(roadEntry);
	}
	
	/** Clear internal buffers.
	 */
	protected void clearBuffers() {
		this.eyePosition = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FrontFrustum1D5 clone() {
		try {
			FrontFrustum1D5 clone = (FrontFrustum1D5)super.clone();
			clone.roadEntry = new WeakReference<RoadConnection>(this.roadEntry.get());
			clone.eyePosition = null;
			return clone;
		}
		catch(Throwable e) {
			throw new Error(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getBackwardDistance() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getForwardDistance() {
		return this.forwardDistance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RoadConnection getRoadEntry() {
		return this.roadEntry.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RoadSegment getRoadSegment() {
		return getSegment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RoadConnection getHeadingPoint() {
		RoadSegment s = getSegment();
		RoadConnection r = this.roadEntry.get();
		return s.getOtherSidePoint(r);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSegmentOriented() {
		RoadConnection con = getRoadEntry();
		RoadSegment sgmt = getRoadSegment();
		if (con!=null && sgmt!=null) {
			return con.equals(sgmt.getBeginPoint());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getFarDistance() {
		return this.forwardDistance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getNearDistance() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1D5 getTranslation() {
		return getEye();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1D5 getEye() {
		if (this.eyePosition==null) {
			RoadSegment sgmt = getRoadSegment();
			Point1D5 eye = new Point1D5(sgmt);
			double maxX = getMaxX();
			double minX = getMinX();
			double jCenter = getCenterY();
			
			if (isSegmentOriented()) {
				eye.set(minX, jCenter);
			}
			else {
				eye.set(maxX, jCenter);
			}
			
			this.eyePosition = eye;
		}
		return this.eyePosition;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clamp() {
		super.clamp();
		clearBuffers();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		super.reset();
		clearBuffers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSegment(RoadSegment segment, RoadConnection roadEntry) {
		if (roadEntry.isConnectedSegment(segment)) {
			this.roadEntry = new WeakReference<RoadConnection>(roadEntry);
		}
		else {
			this.roadEntry = new WeakReference<RoadConnection>(segment.getBeginPoint());
		}
		setSegment(segment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point1D5 v) {
		RoadSegment sgmt = (RoadSegment)v.getSegment();
		RoadConnection con;
		
		if (!sgmt.equals(getRoadSegment())) {
			con = sgmt.getBeginPoint();
			setSegment(sgmt, con);
		}
		else {
			con = getRoadEntry();
		}

		double lower, upper;
		if (con.equals(sgmt.getBeginPoint())) {
			lower = v.getCurvilineCoordinate();
			upper = lower + this.forwardDistance;
		}
		else {
			upper = v.getCurvilineCoordinate();
			lower = upper - this.forwardDistance;
		}
		setBox(lower, upper,
				v.getJuttingDistance(),
				getLateralSize());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(RoadSegment segment, RoadConnection roadEntry, Point1D5 v) {
		setSegment(segment, roadEntry);
		double lower, upper;
		if (roadEntry.equals(segment.getBeginPoint())) {
			lower = v.getCurvilineCoordinate();
			upper = lower + this.forwardDistance;
		}
		else {
			upper = v.getCurvilineCoordinate();
			lower = upper - this.forwardDistance;
		}
		setBox(lower, upper,
				v.getJuttingDistance(),
				getLateralSize());
	}
	
}
