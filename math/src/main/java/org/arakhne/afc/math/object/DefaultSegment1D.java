/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.object;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.system.CoordinateSystem2D;

/**
 * This class represents a segment is a 1D or 1.5D coordinate space.
 * <p>
 * A 1D point is defined by its curviline position on a segment.
 * <p>
 * A 1.5D point is defined by its curviline position on a segment, and
 * by a jutting/shifting distance. The jutting distance is positive or
 * negative according to the side vector of the current {@link fr.utbm.set.geom.system.CoordinateSystem2D}.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultSegment1D implements Segment1D {

	private final Point2f firstPt = new Point2f();
	private final Point2f secondPt = new Point2f();
	
	/**
	 * Create a segment on {@code (0;0)}.
	 */
	public DefaultSegment1D() {
		//
	}
	
	/**
	 * Create a segment on between the two given points.
	 * 
	 * @param first
	 * @param second
	 */
	public DefaultSegment1D(Point2f first, Point2f second) {
		this.firstPt.set(first);
		this.secondPt.set(second);
	}

	/**
	 * Create a segment on between the two given points.
	 * 
	 * @param firstX
	 * @param firstY
	 * @param secondX
	 * @param secondY
	 */
	public DefaultSegment1D(float firstX, float firstY, float secondX, float secondY) {
		this.firstPt.set(firstX, firstY);
		this.secondPt.set(secondX, secondY);
	}

	/**
	 * Set the points of this segment.
	 * 
	 * @param first
	 * @param second
	 */
	public void set(Point2f first, Point2f second) {
		this.firstPt.set(first);
		this.secondPt.set(second);
	}

	/**
	 * Set the first point of this segment.
	 * 
	 * @param first
	 */
	public void setFirstPoint(Point2f first) {
		this.firstPt.set(first);
	}

	/**
	 * Set the second point of this segment.
	 * 
	 * @param second
	 */
	public void setLastPoint(Point2f second) {
		this.secondPt.set(second);
	}

	/**
	 * Set the points of the segment
	 * 
	 * @param firstX
	 * @param firstY
	 * @param secondX
	 * @param secondY
	 */
	public void set(float firstX, float firstY, float secondX, float secondY) {
		this.firstPt.set(firstX, firstY);
		this.secondPt.set(secondX, secondY);
	}

	/**
	 * Set the first point of the segment
	 * 
	 * @param firstX
	 * @param firstY
	 */
	public void setFirstPoint(float firstX, float firstY) {
		this.firstPt.set(firstX, firstY);
	}

	/**
	 * Set the second point of the segment
	 * 
	 * @param secondX
	 * @param secondY
	 */
	public void setLastPoint(float secondX, float secondY) {
		this.secondPt.set(secondX, secondY);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getFirstPoint() {
		return new Point2f(this.firstPt);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getLastPoint() {
		return new Point2f(this.secondPt);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getLength() {
		return this.firstPt.distance(this.secondPt);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isFirstPointConnectedTo(Segment1D otherSegment) {
		return this.firstPt.equals(otherSegment.getFirstPoint())
				||
				this.firstPt.equals(otherSegment.getLastPoint());
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isLastPointConnectedTo(Segment1D otherSegment) {
		return this.secondPt.equals(otherSegment.getFirstPoint())
			||
			this.secondPt.equals(otherSegment.getLastPoint());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getTangentAt(float positionOnSegment) {
		Vector2f v = new Vector2f();
		v.sub(this.secondPt, this.firstPt);
		v.normalize();
		return v;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void projectsOnPlane(float positionOnSegment, Point2f position, Vector2f tangent, CoordinateSystem2D system) {
		Vector2f t;
		
		if (tangent==null) t = new Vector2f();
		else t = tangent;
		
		t.sub(this.secondPt, this.firstPt);
		t.normalize();
		
		if (position!=null) {
			t.scale(positionOnSegment);
			position.set(this.firstPt);
			position.add(t);
			t.normalize();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void projectsOnPlane(float positionOnSegment, float shiftDistance, Point2f position, Vector2f tangent, CoordinateSystem2D system) {
		Vector2f t;
		
		if (tangent==null) t = new Vector2f();
		else t = tangent;
		
		t.sub(this.secondPt, this.firstPt);
		t.normalize();
		
		if (position!=null) {
			t.scale(positionOnSegment);
			position.set(this.firstPt);
			position.add(t);
			t.normalize();
			
			Vector2f perp = MathUtil.perpendicularVector(t, system);
			perp.scale(shiftDistance);
			
			position.add(perp);
		}
	}

}
