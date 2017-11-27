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
package fr.utbm.set.jasim.io.sfg;

import java.util.UUID;

import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.object.Segment1D;

/**
 * Describe a position from a configuration file.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PositionParameter extends AbstractParameter {

	private final double x, y, z;
	private final String road; // Geoid not uuid
	private UUID place = null;
	
	/**
	 * Create a 3D position.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	PositionParameter(double x, double y, double z) {
		super(3f);
		this.x = x;
		this.y = y;
		this.z = z;
		this.road = null;
	}
	
	/**
	 * Create a 2D or 2.5D position.
	 * 
	 * @param x
	 * @param y
	 */
	PositionParameter(double x, double y) {
		super(2f);
		this.x = x;
		this.y = y;
		this.z = Double.NaN;
		this.road = null;
	}

	/**
	 * Create a 1D position.
	 * 
	 * @param roadId is the geoid of the road, not the uuid.
	 * @param curvilineCoord
	 */
	PositionParameter(String roadId, double curvilineCoord) {
		super(1f);
		this.x = curvilineCoord;
		this.y = Double.NaN;
		this.z = Double.NaN;
		this.road = roadId;
	}

	/**
	 * Create a 1.5D position.
	 * 
	 * @param roadId is the geoid of the road, not the uuid.
	 * @param curvilineCoord
	 * @param shiftDistance
	 */
	PositionParameter(String roadId, double curvilineCoord, double shiftDistance) {
		super(1.5f);
		this.x = curvilineCoord;
		this.y = shiftDistance;
		this.z = Double.NaN;
		this.road = roadId;
	}
	
	/** Replies the x coordinate (available on all space dimensions).
	 * 
	 * @return the x coordinate
	 */
	public double getX() {
		return this.x;
	}

	/** Replies the y coordinate (available on 1.5D, 2D and 3D dimensions).
	 * 
	 * @return the y coordinate
	 */
	public double getY() {
		return this.y;
	}

	/** Replies the z coordinate (available on 3D dimension).
	 * 
	 * @return the z coordinate
	 */
	public double getZ() {
		return this.z;
	}

	/** Replies the identifier of the road where this position is located
	 * (available on 1D, or 1.5D dimensions).
	 * 
	 * @return the road identifier
	 */
	public String getRoad() {
		return this.road;
	}

	/** Replies the 1D position.
	 * 
	 * @param segment
	 * @return 1D position
	 */
	public Point1D getPosition1D(Segment1D segment) {
		return new Point1D(segment, this.x);
	}

	/** Replies the 1.5D position.
	 * <p>
	 * The the <var>y</var> coordinate was not
	 * specified, the <var>defaultJuttingDistance</var> parameter
	 * will be used in place.
	 *
	 * @param segment
	 * @param defaultJuttingDistance is the default juttig distance to
	 * use if it was not set.
	 * @return 1.5D position
	 */
	public Point1D5 getPosition1D5(Segment1D segment, double defaultJuttingDistance) {
		if (Double.isNaN(this.y))
			return new Point1D5(segment, this.x, defaultJuttingDistance);
		return new Point1D5(segment, this.x, this.y);
	}

	/** Replies the 2D position.
	 * 
	 * @return 2D position
	 */
	public EuclidianPoint2D getPosition2D() {
		return new EuclidianPoint2D(this.x, this.y);
	}

	/** Replies the 2.5D position.
	 * 
	 * @return 2.5D position
	 */
	public EuclidianPoint3D getPosition2D5() {
		return new EuclidianPoint3D(this.x, this.y, this.z);
	}

	/** Replies the 3D position.
	 * 
	 * @return 3D position
	 */
	public EuclidianPoint3D getPosition3D() {
		return new EuclidianPoint3D(this.x, this.y, this.z);
	}
	
	/** Replies the place where this position lies.
	 * 
	 * @return the place or <code>null</code> if no place was given.
	 */
	public UUID getPlace() {
		return this.place;
	}
	
	/** Set the place where this position lies.
	 * 
	 * @param place
	 */
	void setPlace(UUID place) {
		this.place = (place==null) ? null : place;
	}

}
