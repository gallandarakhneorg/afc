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

package org.arakhne.afc.math.bounds.bounds1f;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.bounds.bounds1f5.BoundingRect1D5;
import org.arakhne.afc.math.bounds.bounds2f.OrientedBoundingRectangle;
import org.arakhne.afc.math.bounds.bounds3f.OrientedBoundingBox;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.object.Point1D;
import org.arakhne.afc.math.object.Segment1D;
import org.arakhne.afc.math.system.CoordinateSystem2D;
import org.arakhne.afc.math.system.CoordinateSystem3D;

/**
 * An implementation of bounds in a 1D space.
 * 
 * @param <S> is the type of the segment to reply
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoundingInterval<S extends Segment1D> extends AbstractCombinableBounds1D<S> {

	private static final long serialVersionUID = -75374486190742772L;

	/**
	 * Create a unset box.
	 * 
	 * @param segment is the segment on which this bound lies.
	 */
	public BoundingInterval(S segment) {
		super(segment);
	}
	
	/**
	 * Create a box from a set of points.
	 *
	 * @param segment is the segment on which this bound lies.
	 * @param points is the set of points.
	 */
	public BoundingInterval(S segment, float... points) {
		super(segment, points);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BoundingInterval<S> clone() {
		return (BoundingInterval<S>)super.clone();
	}
			
	/** {@inheritDoc}
	 */
	@Override
	public BoundingRect1D5<S> toBounds1D5() {
		if (!isInit()) return new BoundingRect1D5<S>(getSegment());
		return new BoundingRect1D5<S>(getSegment(), getMinX(), getMaxX());
	}

	/** {@inheritDoc}
	 */
	@Override
	public BoundingRect1D5<S> toBounds1D5(float juttingDistance, float lateralSize) {
		if (!isInit()) return new BoundingRect1D5<S>(getSegment());
		float demiSize = Math.abs(lateralSize / 2.f);
		Point2f l = new Point2f(getMinX(), juttingDistance - demiSize);
		Point2f u = new Point2f(getMaxX(), juttingDistance + demiSize);
		return new BoundingRect1D5<S>(getSegment(), l, u);
	}

	/** {@inheritDoc}
	 */
	@Override
	public OrientedBoundingRectangle toBounds2D(float lateralSize, CoordinateSystem2D system) {
		if (!isInit()) return new OrientedBoundingRectangle();
		S sgmt = getSegment();
		if (sgmt==null) return null;

		Point1D position = getPosition();
		Point2f pos2d = new Point2f();
		Vector2f tangent = new Vector2f();
		
		sgmt.projectsOnPlane(
				position.getCurvilineCoordinate(),
				pos2d, tangent, system);
		
		Vector2f perp = MathUtil.perpendicularVector(tangent, system);
		
		return new OrientedBoundingRectangle(
				pos2d,
				new Vector2f[] {
					tangent,
					perp
				},
				new float[] {
						getSize()/2.f, 
						lateralSize/2.f
				}
			);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public OrientedBoundingBox toBounds3D(float lateralSize, float posZ, float sizeZ, CoordinateSystem3D system) {
		if (!isInit()) return new OrientedBoundingBox();
		S sgmt = getSegment();
		if (sgmt==null) return null;

		CoordinateSystem2D cs2d = system.toCoordinateSystem2D();
		
		Point1D position = getPosition();
		Point2f pos2d = new Point2f();
		Vector2f tangent = new Vector2f();
		
		sgmt.projectsOnPlane(
				position.getCurvilineCoordinate(),
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
						getSize()/2.f, 
						lateralSize/2.f,
						sizeZ/2.f,
				}
			);
	}

}
