/* 
 * $Id$
 * 
 * Copyright (c) 2009, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.gis.road;

import javax.vecmath.Point2d;

/**
 * Stub for RoadConnection class.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RoadConnectionStub extends StandardRoadConnection {

	private final String label;
	private final Point2d position;
	
	/**
	 * @param label
	 * @param pos
	 */
	public RoadConnectionStub(String label, Point2d pos) {
		this.label = label;
		this.position = pos;
	}

	/**
	 * @param label
	 * @param x
	 * @param y
	 */
	public RoadConnectionStub(String label, double x, double y) {
		this.label = label;
		this.position = new Point2d(x,y);
	}

	/**
	 * @param pos
	 */
	public RoadConnectionStub(Point2d pos) {
		this(null, pos);
	}

	/**
	 * @param x
	 * @param y
	 */
	public RoadConnectionStub(double x, double y) {
		this(null, x, y);
	}

	@Override
	public Point2d getPoint() {
		return this.position;
	}

	@Override
	public String toString() {
		if (this.label!=null) {
			StringBuilder str = new StringBuilder();
			str.append(this.label);
			str.append(" => "); //$NON-NLS-1$
			str.append(super.toString());
			return str.toString();
		}
		return super.toString();
	}
	
}
