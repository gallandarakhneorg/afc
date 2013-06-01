/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
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

package org.arakhne.afc.math.bounds.bounds1f5;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.bounds.bounds1f.BoundingInterval;
import org.arakhne.afc.math.bounds.bounds2f.OrientedBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds3f.OrientedBoundingBox;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.object.Point1D5;
import org.arakhne.afc.math.object.Segment1D;
import org.arakhne.afc.math.system.CoordinateSystem2D;
import org.arakhne.afc.math.system.CoordinateSystem3D;

/**
 * An implementation of bounds in a 1D space.
 * 
 * @param <S> is the type of the segment to reply
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoundingRect1D5<S extends Segment1D> extends AbstractCombinableBounds1D5<S> {

	private static final long serialVersionUID = 214113191540045281L;

	/**
	 * Create a unset box.
	 * 
	 * @param segment is the segment on which this bounds must lies.
	 */
	public BoundingRect1D5(S segment) {
		super(segment);
	}
	
	/**
	 * Create a box from a set of points.
	 *
	 * @param segment is the segment on which this bounds must lies.
	 * @param points is the set of points.
	 */
	public BoundingRect1D5(S segment, float... points) {
		super(segment, points);
	}
		
	/**
	 * Create a box from a set of points.
	 *
	 * @param segment is the segment on which this bounds must lies.
	 * @param points is the set of points.
	 */
	public BoundingRect1D5(S segment, Tuple2f... points) {
		super(segment, points);
	}
	
	/**
	 * Creates a BoundingRect from a segment, a position on it, a width and a length
	 * @param segment the segment on which lies the bounds
	 * @param center the curviline position of the center of the bounds
	 * @param length the length of the boundingrect
	 * @param width the width of the boundingrect
	 */
	public BoundingRect1D5(S segment, Tuple2f center, float length, float width) {
		super(segment);
		this.jutting = center.getY();
		float halfLength = length/2;
		this.lower = center.getX() - halfLength;
		this.upper = center.getX() + halfLength;
		this.lateralSize = width;
	}
	
	/**
	 * Creates a BoundingRect from a segment, a position on it, a width and a length
	 * @param center the curviline position of the center of the bounds
	 * @param length the length of the boundingrect
	 * @param width the width of the boundingrect
	 */
	@SuppressWarnings("unchecked")
	public BoundingRect1D5(Point1D5 center, float length, float width) {
		super((S)center.getSegment());
		this.jutting = center.getJuttingDistance();
		float halfLength = length/2;
		this.lower = center.getCurvilineCoordinate() - halfLength;
		this.upper = center.getCurvilineCoordinate() + halfLength;
		this.lateralSize = width;
	}

	/**
	 * Creates a BoundingRect from a width and a length
	 * @param length the length of the boundingrect
	 * @param width the width of the boundingrect
	 */
	public BoundingRect1D5(float length, float width) {
		super();
		this.jutting = 0;
		float halfLength = length/2;
		this.lower = -halfLength;
		this.upper = halfLength;
		this.lateralSize = width;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BoundingRect1D5<S> clone() {
		return (BoundingRect1D5<S>)super.clone();
	}

	/** {@inheritDoc}
	 */
	@Override
	public BoundingInterval<S> toBounds1D() {
		if (!isInit()) return new BoundingInterval<S>(getSegment());
		return new BoundingInterval<S>(getSegment(), getMinX(), getMaxX());
	}

	/** {@inheritDoc}
	 */
	@Override
	public OrientedBoundingRectangle toBounds2D(CoordinateSystem2D system) {
		if (!isInit()) return new OrientedBoundingRectangle();
		S sgmt = getSegment();
		if (sgmt==null) return null;

		Point1D5 position = getPosition();
		Point2f pos2d = new Point2f();
		Vector2f tangent = new Vector2f();
		
		sgmt.projectsOnPlane(
				position.getCurvilineCoordinate(),
				position.getJuttingDistance(),
				pos2d, tangent, system);
		
		Vector2f perp = MathUtil.perpendicularVector(tangent, system);
		
		return new OrientedBoundingRectangle(
				pos2d,
				new Vector2f[] {
					tangent,
					perp
				},
				new float[] {
						getSizeX()/2.f, 
						getSizeY()/2.f
				}
			);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public OrientedBoundingBox toBounds3D(float posZ, float sizeZ, CoordinateSystem3D system) {	
		if (!isInit()) return new OrientedBoundingBox();
		S sgmt = getSegment();
		if (sgmt==null) return null;

		CoordinateSystem2D cs2d = system.toCoordinateSystem2D();
		
		Point1D5 position = getPosition();
		Point2f pos2d = new Point2f();
		Vector2f tangent = new Vector2f();
		
		sgmt.projectsOnPlane(
				position.getCurvilineCoordinate(),
				position.getJuttingDistance(),
				pos2d, tangent, cs2d);
		
		Vector2f perp = MathUtil.perpendicularVector(tangent, cs2d);
		
		return new OrientedBoundingBox(
				system.fromCoordinateSystem2D(pos2d, posZ),
				new Vector3f[] {
					system.fromCoordinateSystem2D(tangent),
					system.fromCoordinateSystem2D(perp),
					system.getUpVector()
				},
				new float[] {
						getSizeX()/2.f, 
						getSizeY()/2.f,
						sizeZ/2.f,
				}
			);
	}

}
