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
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.bounds.AbstractBounds;
import org.arakhne.afc.math.bounds.bounds2f.Bounds2D;
import org.arakhne.afc.math.bounds.bounds3f.Bounds3D;
import org.arakhne.afc.math.intersection.IntersectionType;
import org.arakhne.afc.math.intersection.IntersectionUtil;
import org.arakhne.afc.math.object.Point1D;
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
public abstract class AbstractBounds1D<S extends Segment1D>
extends AbstractBounds<Point1D,Point1D,Float>
implements Bounds1D<S> {

	private static final long serialVersionUID = 6366460429638667601L;

	/** Segment on which this bounds lies.
	 */
	S segment;
	
	/** Lower position.
	 */
	float lower;
	
	/** Upper position.
	 */
	float upper;

	/**
	 * Create a unset box.
	 * 
	 * @param segment is the segment on which this bound lies.
	 */
	public AbstractBounds1D(S segment) {
		assert(segment!=null);
		this.segment = segment;
		this.lower = this.upper = Float.NaN;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.lower==this.upper;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getCenterX() {
		return (this.lower+this.upper)/2.f;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D getCenter() {
		return new Point1D(this.segment, (this.lower+this.upper)/2.f);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getMinX() {
		return this.lower;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getMaxX() {
		return this.upper;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point1D lower, Point1D upper) {
		assert(lower!=null);
		assert(upper!=null);
		lower.set(this.segment, this.lower);
		upper.set(this.segment, this.upper);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D getLower() {
		return new Point1D(this.segment, this.lower);
	}

	/** {@inheritDoc}
	 */
	@Override
	public PseudoHamelDimension getMathematicalDimension() {
		return PseudoHamelDimension.DIMENSION_1D;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D getUpper() {
		return new Point1D(this.segment, this.upper);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Float getSize() {
		return Float.valueOf(this.upper - this.lower);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public S getSegment() {
		return this.segment;
	}
	
	/** Replies if this bounds is equal to the given object.
	 * 
	 * @param obj
	 * @return <code>true</code> if the bounds are equal, otherwise <code>false</code>
	 */
	@Override
	public boolean equals(Object obj) {
		if (!isInit()) return false;
		if (obj==this) return true;
		if (obj instanceof Bounds1D<?>) {
			Bounds1D<?> bb = (Bounds1D<?>)obj;
			Segment1D sgmt = bb.getSegment();
			if (sgmt==getSegment()) {
				float l = bb.getMinX();
				float u = bb.getMaxX();
				
				return (MathUtil.epsilonEquals(this.lower, l)
						&&
						MathUtil.epsilonEquals(this.upper, u));
			}
		}
		return false;
	}
	
	/** Replies the distance between the given point and the
	 * nearest point of this bounds.
	 *
	 * @param reference
	 * @return the smallest distance from the point to the bounds.
	 */
	@Override
	public float distance(Point1D reference) {
		if (!isInit()) return Float.NaN;
		Segment1D os = reference.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float c = reference.getCurvilineCoordinate();
			if (c<this.lower) return this.lower - c;
			if (c>this.upper) return c - this.upper;
			return 0.f; 
		}
		return Float.NaN;
	}

	/** Replies the distance between the given point and the
	 * nearest point of this bounds.
	 *
	 * @param reference
	 * @return the smallest distance from the point to the bounds.
	 */
	@Override
	public float distanceSquared(Point1D reference) {
		float d = distance(reference);
		return d * d;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D nearestPoint(Point1D reference) {
		if (!isInit()) return null;
		Segment1D os = reference.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float nc;
			float c = reference.getCurvilineCoordinate();
			if (c<this.lower) nc = this.lower;
			else if (c>this.upper) nc = this.upper;
			else nc = c;
			
			return new Point1D(os, nc);
		}
		return null;
	}

	/** Replies the distance between the given point and the
	 * farest point of this bounds.
	 *
	 * @param reference
	 * @return the distance from the point to the bounds.
	 */
	@Override
	public float distanceMax(Point1D reference) {
		if (!isInit()) return Float.NaN;
		Segment1D os = reference.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float c = reference.getCurvilineCoordinate();
			if (c<this.lower) return this.upper - c;
			if (c>this.upper) return c - this.lower;
			return Math.max(this.upper-c, c-this.lower); 
		}
		return Float.NaN;
	}

	/** Replies the distance between the given point and the
	 * farest point of this bounds.
	 *
	 * @param reference
	 * @return the distance from the point to the bounds.
	 */
	@Override
	public float distanceMaxSquared(Point1D reference) {
		float d = distanceMax(reference);
		return d * d;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D farestPoint(Point1D reference) {
		if (!isInit()) return null;
		Segment1D os = reference.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float nc = (this.lower+this.upper)/2.f;
			if (reference.getCurvilineCoordinate()<=nc) nc = this.upper;
			else nc = this.lower;
			
			return new Point1D(os, nc);
		}
		return null;
	}

	//-------------------------------------------------
	// IntersectionClassifier
	//-------------------------------------------------

	@Override
	public IntersectionType classifies(Bounds1D<S> box) {
		assert(box!=null);
		if (!isInit()) return IntersectionType.OUTSIDE;
		return classifies(box.getLower(), box.getUpper());
	}

	@Override
	public boolean intersects(Bounds1D<S> box) {
		assert(box!=null);
		if (!isInit()) return false;
		return intersects(box.getLower(), box.getUpper());
	}

	@Override
	public IntersectionType classifies(Point1D p) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		Segment1D os = p.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float c = p.getCurvilineCoordinate();
			if (this.lower<=c && c<=this.upper)
				return IntersectionType.INSIDE;
		}
		return IntersectionType.OUTSIDE;
	}

	@Override
	public IntersectionType classifies(Point1D l, Point1D u) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		assert(l.getCurvilineCoordinate()<=u.getCurvilineCoordinate());
		assert((l.getSegment()==null&&u.getSegment()==l.getSegment())
				||
			   (l.getSegment()!=null&&l.getSegment().equals(u.getSegment())));
		Segment1D os = l.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			return IntersectionUtil.classifiesAlignedSegments(
					l.getCurvilineCoordinate(), u.getCurvilineCoordinate(),
					this.lower, this.upper);
		}
		return IntersectionType.OUTSIDE;
	}

	@Override
	public boolean intersects(Point1D p) {
		if (!isInit()) return false;
		Segment1D os = p.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float c = p.getCurvilineCoordinate();
			return (this.lower<=c && c<=this.upper);
		}
		return false;
	}

	@Override
	public boolean intersects(Point1D l, Point1D u) {
		if (!isInit()) return false;
		assert(l.getCurvilineCoordinate()<=u.getCurvilineCoordinate());
		assert((l.getSegment()==null&&u.getSegment()==l.getSegment())
				||
			   (l.getSegment()!=null&&l.getSegment().equals(u.getSegment())));
		Segment1D os = l.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			return IntersectionUtil.intersectsAlignedSegments(
					l.getCurvilineCoordinate(), u.getCurvilineCoordinate(),
					this.lower, this.upper);
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D toBounds2D() {
		return toBounds2D(CoordinateSystem2D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds3D toBounds3D() {
		return toBounds3D(0.f, 0.f, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds3D toBounds3D(CoordinateSystem3D system) {
		return toBounds3D(0.f, 0.f, 0.f, system);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D toBounds2D(CoordinateSystem2D system) {
		return toBounds2D(0.f, system);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds3D toBounds3D(float posZ, float sizeZ, CoordinateSystem3D system) {
		return toBounds3D(0.f, posZ, sizeZ, system);
	}

	@Override
	public float area() {
		return this.getSize();
	}

}
