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

package org.arakhne.afc.math.geometry.continuous.object1d5.bounds;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.geometry.AbstractBounds;
import org.arakhne.afc.math.geometry.continuous.intersection.ClassifierUtil;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionType;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionUtil;
import org.arakhne.afc.math.geometry.continuous.object1d.Point1D;
import org.arakhne.afc.math.geometry.continuous.object1d.Segment1D;
import org.arakhne.afc.math.geometry.continuous.object1d5.Point1D5;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object2d.bounds.Bounds2D;
import org.arakhne.afc.math.geometry.continuous.object3d.bounds.Bounds3D;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;

/**
 * An implementation of bounds in a 1.5D space.
 *
 * @param <S> is the type of the segment to reply
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractBounds1D5<S extends Segment1D>
extends AbstractBounds<Point1D5,Point1D5,Tuple2f<?>>
implements Bounds1D5<S> {

	private static final long serialVersionUID = -2488051681289133579L;

	/** Segment on which this bounds lies.
	 */
	S segment;
	
	/** Lower position.
	 */
	float lower;
	
	/** Upper position.
	 */
	float upper;

	/** Jutting distance.
	 */
	float jutting;
	
	/** Lateral size.
	 */
	float lateralSize;

	/**
	 * Create a unset box.
	 */
	public AbstractBounds1D5() {
		this.segment = null;
		this.lower = this.upper = Float.NaN;
	}

	/**
	 * Create a unset box.
	 * 
	 * @param segment is the segment on which this bound lies.
	 */
	public AbstractBounds1D5(S segment) {
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
	public final float getCenterX() {
		return (this.lower+this.upper)/2.f;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getCenterY() {
		return getJutting();
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Point1D5 getCenter() {
		return new Point1D5(this.segment, (this.lower+this.upper)/2.f, this.jutting);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getMinX() {
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
	public final float getMinY() {
		return this.jutting - this.lateralSize / 2.f;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getMaxY() {
		return this.jutting + this.lateralSize / 2.f;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point1D5 lower, Point1D5 upper) {
		assert(lower!=null);
		assert(upper!=null);
		lower.set(this.segment, this.lower, this.jutting - this.lateralSize/2.f);
		upper.set(this.segment, this.upper, this.jutting + this.lateralSize/2.f);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Point1D5 getLower() {
		return new Point1D5(this.segment, this.lower, this.jutting - this.lateralSize/2.f);
	}

	/** {@inheritDoc}
	 */
	@Override
	public PseudoHamelDimension getMathematicalDimension() {
		return PseudoHamelDimension.DIMENSION_1D5;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D5 getUpper() {
		return new Point1D5(this.segment, this.upper, this.jutting + this.lateralSize/2.f);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getJutting() {
		return this.jutting;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getLateralSize() {
		return this.lateralSize;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final float getSizeX() {
		return this.upper - this.lower;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final float getSizeY() {
		return getLateralSize();
	}

	/** {@inheritDoc}
	 */	
	@Override
	public Tuple2f<?> getSize() {
		return new Vector2f(this.upper - this.lower, getLateralSize());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final S getSegment() {
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
		if (obj instanceof Bounds1D5<?>) {
			Bounds1D5<?> bb = (Bounds1D5<?>)obj;
			Segment1D sgmt = bb.getSegment();
			if (sgmt==getSegment()) {
				float l = bb.getMinX();
				float u = bb.getMaxX();
				float j = bb.getJutting();
				float s = bb.getLateralSize();
				
				return (MathUtil.epsilonEquals(this.lower, l)
						&&
						MathUtil.epsilonEquals(this.upper, u)
						&&
						MathUtil.epsilonEquals(this.jutting, j)
						&&
						MathUtil.epsilonEquals(this.lateralSize, s));
			}
		}
		return false;
	}

	/** {@inheritDoc}
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
	
	/** Replies the squared distance between the given point and the
	 * nearest point of this bounds.
	 *
	 * @param reference
	 * @return the smallest squared distance from the point to the bounds.
	 */
	public float distanceSquared(Point1D reference) {
		float d = distance(reference);
		return d * d;
	}

	/** {@inheritDoc}
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

	/** Replies the squared distance between the given point and the
	 * farest point of this bounds.
	 *
	 * @param reference
	 * @return the squared distance from the point to the bounds.
	 */
	public float distanceMaxSquared(Point1D reference) {
		float d = distanceMax(reference);
		return d * d;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point1D5 reference) {
		if (!isInit()) return Float.NaN;
		Segment1D os = reference.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float d;
			float c = reference.getCurvilineCoordinate();
			if (c<this.lower) d = this.lower - c;
			else if (c>this.upper) d = c - this.upper;
			else d = 0.f;
			
			float jup = this.jutting+this.lateralSize/2.f;
			float jdown = this.jutting-this.lateralSize/2.f;
			
			float d2;
			c = reference.getJuttingDistance();
			if (c<jdown) d2 = jdown - c;
			else if (c>jup) d2 = c - jup;
			else d2 = 0.f;

			return d*d+d2*d2;
		}
		return Float.NaN;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D5 nearestPoint(Point1D5 reference) {
		Segment1D os = reference.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float nc;
			float c = reference.getCurvilineCoordinate();
			if (c<this.lower) nc = this.lower;
			else if (c>this.upper) nc = this.upper;
			else nc = c;
			
			float jup = this.jutting+this.lateralSize/2.f;
			float jdown = this.jutting-this.lateralSize/2.f;
			
			float nj;
			c = reference.getJuttingDistance();
			if (c<jdown) nj = jdown;
			else if (c>jup) nj = jup;
			else nj = c;

			return new Point1D5(os, nc, nj);
		}
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point1D5 reference) {
		if (!isInit()) return Float.NaN;
		Segment1D os = reference.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float d;
			float c = reference.getCurvilineCoordinate();
			if (c<this.lower) d = this.upper - c;
			else if (c>this.upper) d = c - this.lower;
			else d = Math.max(this.upper-c, c-this.lower);
			
			float jup = this.jutting+this.lateralSize/2.f;
			float jdown = this.jutting-this.lateralSize/2.f;
			
			float d2;
			c = reference.getJuttingDistance();
			if (c<jdown) d2 = jup - c;
			else if (c>jup) d2 = c - jdown;
			else d2 = Math.max(jup-c, c-jdown);

			return d*d+d2*d2;
		}
		return Float.NaN;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point1D5 farestPoint(Point1D5 reference) {
		if (!isInit()) return null;
		Segment1D os = reference.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float nc = (this.lower+this.upper)/2.f;
			if (reference.getCurvilineCoordinate()<nc) nc = this.upper;
			else nc = this.lower;
			
			float nj;
			if (reference.getJuttingDistance()<this.jutting)
				nj = this.jutting + this.lateralSize / 2.f;
			else
				nj = this.jutting - this.lateralSize / 2.f;
			return new Point1D5(os, nc, nj);
		}
		return null;
	}

	//---------------------------------------------
	// IntersectionClassifier
	//---------------------------------------------
	
	@Override
	public IntersectionType classifies(Bounds1D5<S> box) {
		assert(box!=null);
		if (!isInit()) return IntersectionType.OUTSIDE;
		return classifies(box.getLower(), box.getUpper());
	}

	@Override
	public boolean intersects(Bounds1D5<S> box) {
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
			return ClassifierUtil.classifiesAlignedSegments(
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

	@Override
	public IntersectionType classifies(Point1D5 p) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		Segment1D os = p.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float c = p.getCurvilineCoordinate();
			float j = p.getJuttingDistance();
			float demil = this.lateralSize/2.f;
			if (this.lower<=c && c<=this.upper
				&&
				(this.jutting-demil)<=j && j<=(this.jutting+demil))
				return IntersectionType.INSIDE;
		}
		return IntersectionType.OUTSIDE;
	}

	@Override
	public IntersectionType classifies(Point1D5 l, Point1D5 u) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		assert(l.getCurvilineCoordinate()<=u.getCurvilineCoordinate());
		assert((l.getSegment()==null&&u.getSegment()==l.getSegment())
				||
			   (l.getSegment()!=null&&l.getSegment().equals(u.getSegment())));
		Segment1D os = l.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float demil = this.lateralSize/2.f;
			return ClassifierUtil.classifiesAlignedRectangles(
					l.getCurvilineCoordinate(), l.getJuttingDistance(),  
					u.getCurvilineCoordinate(), u.getJuttingDistance(),
					this.lower, this.jutting-demil,
					this.upper, this.jutting+demil);
		}
		return IntersectionType.OUTSIDE;
	}

	@Override
	public boolean intersects(Point1D5 p) {
		if (!isInit()) return false;
		Segment1D os = p.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float c = p.getCurvilineCoordinate();
			float j = p.getJuttingDistance();
			float demil = this.lateralSize/2.f;
			return (this.lower<=c && c<=this.upper
					&&
					(this.jutting-demil)<=j && j<=(this.jutting+demil));
		}
		return false;
	}

	@Override
	public boolean intersects(Point1D5 l, Point1D5 u) {
		if (!isInit()) return false;
		assert(l.getCurvilineCoordinate()<=u.getCurvilineCoordinate());
		assert((l.getSegment()==null&&u.getSegment()==l.getSegment())
				||
			   (l.getSegment()!=null&&l.getSegment().equals(u.getSegment())));
		Segment1D os = l.getSegment();
		if ((this.segment==null&&os==this.segment)||(this.segment!=null&&this.segment.equals(os))) {
			float demil = this.lateralSize/2.f;
			return IntersectionUtil.intersectsAlignedRectangles(
					l.getCurvilineCoordinate(), l.getJuttingDistance(),  
					u.getCurvilineCoordinate(), u.getJuttingDistance(),
					this.lower, this.jutting-demil,
					this.upper, this.jutting+demil);
		}
		return false;
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
		return toBounds3D(0.f, 0.f, system);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D toBounds2D() {
		return toBounds2D(CoordinateSystem2D.getDefaultCoordinateSystem());
	}

	@Override
	public float area() {
		return this.getSizeX();
	}

}
