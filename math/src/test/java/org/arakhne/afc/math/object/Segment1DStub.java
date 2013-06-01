/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
 * Stub for Segment1D.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Segment1DStub implements Segment1D {

	private final String identifier;
	private final Point2f firstPoint;
	private final Point2f lastPoint;
	private final float length;
	private Segment1D firstSegment;
	private Segment1D lastSegment;
	
	/**
	 * @param id
	 * @param first
	 * @param last
	 */
	public Segment1DStub(String id, Point2f first, Point2f last) {
		this.identifier = id;
		this.firstPoint = first;
		this.lastPoint = last;
		this.length = first.distance(last);
	}
	
	/** Connects this segment by its starts.
	 * 
	 * @param segment
	 */
	public void connectWith(Segment1DStub segment) {
		if (MathUtil.epsilonEquals(getFirstPoint(), segment.getFirstPoint())) {
			this.firstSegment = segment;
			segment.firstSegment = this;
		}
		else if (MathUtil.epsilonEquals(getFirstPoint(), segment.getLastPoint())) {
			this.firstSegment = segment;
			segment.lastSegment = this;
		}
		if (MathUtil.epsilonEquals(getLastPoint(), segment.getFirstPoint())) {
			this.lastSegment = segment;
			segment.firstSegment = this;			
		}
		else if (MathUtil.epsilonEquals(getLastPoint(), segment.getLastPoint())) {
			this.lastSegment = segment;
			segment.lastSegment = this;			
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getLength() {
		return this.length;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getFirstPoint() {
		return this.firstPoint;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point2f getLastPoint() {
		return this.lastPoint;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isFirstPointConnectedTo(Segment1D otherSegment) {
		return (this.firstSegment!=null && this.firstSegment.equals(otherSegment));
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isLastPointConnectedTo(Segment1D otherSegment) {
		return (this.lastSegment!=null && this.lastSegment.equals(otherSegment));
	}
	
	@Override
	public String toString() {
		return "["+this.identifier+"->"+this.length+"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Segment1DStub) {
			return this.identifier.equals(((Segment1DStub)o).identifier);
		}
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return this.identifier.hashCode();
	}
	
	@Override
	public Vector2f getTangentAt(float positionOnSegment) {
		return null;
	}

	@Override
	public void projectsOnPlane(float positionOnSegment, Point2f position,
			Vector2f tangent, CoordinateSystem2D system) {
		//
	}

	@Override
	public void projectsOnPlane(float positionOnSegment, float shiftDistance,
			Point2f position, Vector2f tangent, CoordinateSystem2D system) {
		//
	}

}
