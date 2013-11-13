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
package org.arakhne.afc.math.geometry.continuous.object1d5;

import java.lang.ref.WeakReference;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.geometry.continuous.object1d.Point1D;
import org.arakhne.afc.math.geometry.continuous.object1d.Segment1D;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object3d.Tuple3f;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;

/**
 * This class represents a 1.5D point.
 * <p>
 * A 1.5D point is defined by its curviline position on a graph segment, and
 * by a jutting/shifting distance. The jutting distance is positive or
 * negative according to the side vector of the current {@link CoordinateSystem2D}.
 * <p>
 * Caution: the instances of this classes have a weak reference to the segments.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Point1D5 implements Comparable<Point1D5> {

	private static final long serialVersionUID = 6638632686245720846L;
	
	private float curviline;
	private float jutting;
	private WeakReference<Segment1D> segment;
	
	/**
	 * Constructs and initializes a Point1D5 from the specified coordinates.
	 * 
	 * @param segment
	 *            the segment
	 * @param curviline
	 *            the curviline coordinate
	 * @param jutting
	 *            the jutting coordinate
	 */
	public Point1D5(Segment1D segment, float curviline, float jutting) {
		this.curviline = curviline;
		this.jutting = jutting;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Constructs and initializes a Point1D5 from the specified array.
	 * 
	 * @param segment
	 *            the segment
	 * @param p
	 *            the array of length 2 containing curviline and jutting coordinates in order
	 */
	public Point1D5(Segment1D segment, float... p) {
		this.curviline = (p!=null && p.length>0) ? p[0] : 0.f;
		this.jutting = (p!=null && p.length>1) ? p[1] : 0.f;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Constructs and initializes a Point1D5 from the specified Tuple2f.
	 * 
	 * @param segment
	 *            the segment
	 * @param t1
	 *            the Tuple2f containing the initialization coordinates
	 */
	public Point1D5(Segment1D segment, Tuple2f<?> t1) {
		this.curviline = (t1!=null) ? t1.getX() : 0.f;
		this.jutting = (t1!=null) ? t1.getY() : 0.f;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Constructs and initializes a Point1D5 to (0,0).
	 * 
	 * @param segment
	 *            the segment
	 */
	public Point1D5(Segment1D segment) {
		this.curviline = 0.f;
		this.jutting = 0.f;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Constructs and initializes a Point1D5.
	 * 
	 * @param point is the point to copy.
	 */
	public Point1D5(Point1D5 point) {
		assert(point!=null);
		this.curviline = point.curviline;
		this.jutting = point.jutting;
		Segment1D sgmt = point.getSegment();
		this.segment = (sgmt==null) ? null : new WeakReference<Segment1D>(sgmt);
	}
	
	/**
	 * Constructs and initializes a Point1D5.
	 * 
	 * @param point is the point to copy.
	 */
	public Point1D5(Point1D point) {
		assert(point!=null);
		this.curviline = point.getCurvilineCoordinate();
		this.jutting = 0.f;
		Segment1D sgmt = point.getSegment();
		this.segment = (sgmt==null) ? null : new WeakReference<Segment1D>(sgmt);
	}

	/** Construct an empty point.
	 */
	protected Point1D5() {
		this.curviline = this.jutting = 0.f;
		this.segment = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Object o = clone(getClass());
		if (o==null) throw new CloneNotSupportedException();
		return o;
	}
	
	/**
	 * Clone this object if possible.
	 * 
	 * @param <T> is the desired type of the result.
	 * @param type is the desired type of the result.
	 * @return the clone or <code>null</code>
	 */
	public <T extends Point1D5> T clone(Class<T> type) {
		assert(type!=null);
		Class<? extends Point1D5> myType = getClass();
		if (type.isAssignableFrom(myType)) {
			try {
				Point1D5 p = myType.newInstance();
				p.set(getSegment(), this.curviline, this.jutting);
				return type.cast(p);
			}
			catch(AssertionError e) {
				throw e;
			}
			catch(Throwable _) {
				// Ignore this exception
			}
		}
		return null;
	}

	/** Clamp the curviline coordinate to the segment.
	 * 
	 * @return the amount that was removed from the coordinate. If the amount
	 * is negative it means that the coordinate was negative and clamped to zero.
	 * If the amount if positive it means that the coordinate was greater than
	 * the segment length and clamped to this length. If the amount
	 * is equal to zero it mean that the coordinate was not clamped.
	 */
	public float clamp() {
		float clamped = 0.f;
		if (this.curviline<0.) {
			clamped = this.curviline;
			this.curviline = 0.f;
		}
		else {
			float length = 0.f;
			Segment1D sgmt = getSegment();
			if (sgmt!=null) length = sgmt.getLength();
			if (this.curviline>length) {
				clamped = this.curviline - length;
				this.curviline = length;
			}
		}
		return clamped;
	}
	
	/** Replies if this pint is located on the segment.
	 * <p>
	 * The point is located on the segment only if its curviline
	 * coordinate is in <code>[0;length]</code>, where
	 * <code>length</code> is the length of the segment.
	 * 
	 * @return <code>true</code> if the point is on the segment,
	 * otherwise <code>false</code>
	 */
	public final boolean isOnSegment() {
		if (this.curviline>=0.) {
			float length = 0.f;
			Segment1D sgmt = getSegment();
			if (sgmt!=null) length = sgmt.getLength();
			return (this.curviline<=length); 
		}
		return false;
	}

	/** Replies if this point is located on the same segment as the given one.
	 * 
	 * @param p
	 * @return <code>true</code> if the points are on the same segment,
	 * otherwise <code>false</code>
	 */
	public final boolean isOnSameSegment(Point1D5 p) {
		assert(p!=null);
		return getSegment() == p.getSegment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object o) {
		if (o instanceof Point1D5) {
			return equals((Point1D5)o);
		}
		return false;
	}

	/** Replies if this point is equals to the given point.
	 * 
	 * @param p
	 * @return <code>true</code> if this point has the same coordinates
	 * on the same segment as for the given point.
	 */
	public boolean equals(Point1D5 p) {
		return (p!=null && getSegment()==p.getSegment()
				&& this.curviline==p.curviline && this.jutting==p.jutting);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Point1D5 p) {
		if (p==null) return -1;
		Segment1D mySegment = getSegment();
		Segment1D otherSegment = p.getSegment();
		if (mySegment==otherSegment) {
			int cmp = Float.compare(this.curviline, p.curviline);
			if (cmp==0) return Float.compare(this.jutting, p.jutting);
			return cmp;
		}
		int h1 = (mySegment!=null) ? mySegment.hashCode() : 0;
		int h2 = (otherSegment!=null) ? otherSegment.hashCode() : 0;
		return h1 - h2;
	}

	/** Returns <code>true</code> if the L-infinite distance between this point and point
	 * <var>p</var> is less than or equal to the epsilon parameter, 
	 * otherwise returns <code>false</code>. The L-infinite
     * distance is equal to MAX[abs(curviline-curviline), abs(curviline-curviline)].
     * <p>
     * If the points are not on the same segments, they are not equals.
     * 
     * @param p the point to be compared to this point
     * @param epsilon the threshold value.
     * @return <code>true</code> if the ponits are equals, otherwise <code>false</code>
     */
	public boolean epsilonEquals(Point1D5 p, float epsilon) {
		assert(p!=null);
		return (getSegment()==p.getSegment()) &&
				(Math.abs(p.curviline - this.curviline) <= epsilon) &&
				(Math.abs(p.jutting - this.jutting) <= epsilon);
	}

	/**
	 * Returns a hash number based on the data values in this object. 
	 * Two different Point1D5 objects with identical data  values
	 * (ie, returns true for equals(Point1D5) ) will return the same hash number.
	 * Two vectors with different data members may return the same hash value,
	 * although this is not likely.
	 */
	@Override
	public int hashCode() {
		Segment1D mySegment = getSegment();
		long sbits = (mySegment==null) ? 0l : mySegment.hashCode();
		long xbits = (long) this.curviline;
		long ybits = (long) this.jutting;
		return (int)(sbits ^ (sbits >> 32) ^ xbits ^ (xbits >> 32) ^ ybits ^ (ybits >> 32));
	}	
	
	/** Add the given values to this point.
	 * 
	 * @param curvilineMove is the quantity to add to the curviline coordinate.
	 * @param juttingMove is the quantity to add to the jutting coordinate.
	 */
	public final void add(float curvilineMove, float juttingMove) {
		this.curviline += curvilineMove;
		this.jutting += juttingMove;
	}

	/** Add the given values to this point.
	 * 
	 * @param curvilineMove is the quantity to add to the curviline coordinate.
	 */
	public final void add(float curvilineMove) {
		this.curviline += curvilineMove;
	}

	/** Add the given values to this point.
	 * 
	 * @param move is the quantity to add to the curviline coordinate.
	 */
	public final void add(Tuple2f<?> move) {
		if (move!=null) {
			this.curviline += move.getX();
			this.jutting += move.getY();
		}
	}

	/** Substract the given values to this point.
	 * 
	 * @param move is the quantity to substract to the curviline coordinate.
	 */
	public final void sub(Tuple2f<?> move) {
		if (move!=null) {
			this.curviline -= move.getX();
			this.jutting -= move.getY();
		}
	}

	/** Substract the given values to this point.
	 * 
	 * @param curvilineMove is the quantity to substract to the curviline coordinate.
	 * @param juttingMove is the quantity to substract to the jutting coordinate.
	 */
	public final void sub(float curvilineMove, float juttingMove) {
		this.curviline -= curvilineMove;
		this.jutting -= juttingMove;
	}

	/** Substract the given values to this point.
	 * 
	 * @param curvilineMove is the quantity to substract to the curviline coordinate.
	 */
	public final void sub(float curvilineMove) {
		this.curviline -= curvilineMove;
	}

	/**
	 * Computes the square of the distance between this point and point p1.
	 * 
	 * @param p1
	 *            the other point
	 * @return the square of the distance
	 */
	public final float distanceSquared(Point1D5 p1) {
		if (isOnSameSegment(p1)) {
			float a = this.curviline - p1.curviline;
			float b = this.jutting - p1.jutting;
			return a*a+b*b;
		}
		return Float.POSITIVE_INFINITY;
	}

	/**
	 * Computes the distance between this point and point p1.
	 * 
	 * @param p1
	 *            the other point
	 * @return the distance or {@link float#POSITIVE_INFINITY} if
	 * not on the same segments.
	 */
	public final float distance(Point1D5 p1) {
		if (isOnSameSegment(p1)) {
			float a = this.curviline - p1.curviline;
			float b = this.jutting - p1.jutting;
			return (float) Math.sqrt(a*a+b*b);
		}
		return Float.POSITIVE_INFINITY;
	}

	/**
	 * Computes the L-1 (Manhattan) distance between this point and point p1.
	 * The L-1 distance is equal to abs(curviline1-curviline2) + abs(jutting1-jutting2).
	 * 
	 * @param p1
	 *            the other point
	 * @return the manhatton distance or {@link float#POSITIVE_INFINITY} if
	 * not on the same segments.
	 */
	public final float distanceL1(Point1D5 p1) {
		if (isOnSameSegment(p1)) {
			return (Math.abs(this.curviline - p1.curviline) + Math.abs(this.jutting - p1.jutting));
		}
		return Float.POSITIVE_INFINITY;
	}

	/**
	 * Computes the L-infinite distance between this point and point p1. The
	 * L-infinite distance is equal to MAX[abs(curviline1-curviline2), abs(jutting1-jutting2)].
	 * 
	 * @param p1
	 *            the other point
	 * @return the L-infinite distance or {@link float#POSITIVE_INFINITY} if
	 * not on the same segments.
	 */
	public final float distanceLinf(Point1D5 p1) {
		if (isOnSameSegment(p1)) {
			return (Math.max(Math.abs(this.curviline - p1.curviline), Math.abs(this.jutting - p1.jutting)));
		}
		return Float.POSITIVE_INFINITY;
	}
	
	/**
	 * Computes the curviline distance between this point and point p1.
	 * The curviline distance is equal to abs(curviline1-curviline2).
	 * 
	 * @param p1
	 *            the other point
	 * @return the curviline distance or {@link float#POSITIVE_INFINITY} if
	 * not on the same segments.
	 */
	public final float distanceCurviline(Point1D5 p1) {
		return isOnSameSegment(p1) ? Math.abs(this.curviline - p1.curviline) : Float.POSITIVE_INFINITY;
	}

	/**
	 * Computes the jutting distance between this point and point p1.
	 * The jutting distance is equal to abs(jutting1-jutting2).
	 * 
	 * @param p1
	 *            the other point
	 * @return the jutting distance or {@link float#POSITIVE_INFINITY} if
	 * not on the same segments.
	 */
	public final float distanceJutt(Point1D5 p1) {
		return isOnSameSegment(p1) ? Math.abs(this.jutting - p1.jutting) : Float.POSITIVE_INFINITY;
	}

	/** Replies the curviline coordinate.
	 *
	 * @return the curviline coordinate.
	 */
	public final float getCurvilineCoordinate() {
		return this.curviline;
	}

	/** Set the curviline coordinate.
	 *
	 * @param curviline is the curviline coordinate.
	 */
	public final void setCurvilineCoordinate(float curviline) {
		this.curviline = curviline;
	}

	/** Replies the jutting distance.
	 *
	 * @return the jutting distance.
	 */
	public final float getJuttingDistance() {
		return this.jutting;
	}

	/** Set the jutting distance.
	 *
	 * @param jutting is the jutting distance.
	 */
	public final void setJuttingDistance(float jutting) {
		this.jutting = jutting;
	}

	/** Replies the segment.
	 *
	 * @return the segment or <code>null</code> if the weak reference has lost the segment.
	 */
	public final Segment1D getSegment() {
		return this.segment==null ? null : this.segment.get();
	}

	/** Set the segment.
	 *
	 * @param segment is the segment.
	 */
	public final void setSegment(Segment1D segment) {
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param segment
	 *            the segment
	 * @param curviline
	 *            the curviline coordinate
	 * @param jutting
	 *            the jutting coordinate
	 */
	public final void set(Segment1D segment, float curviline, float jutting) {
		this.curviline = curviline;
		this.jutting = jutting;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param curviline
	 *            the curviline coordinate
	 * @param jutting
	 *            the jutting coordinate
	 */
	public final void set(float curviline, float jutting) {
		this.curviline = curviline;
		this.jutting = jutting;
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param segment
	 *            the segment
	 * @param p are the coordinates of the point.
	 */
	public final void set(Segment1D segment, float... p) {
		this.curviline = (p!=null && p.length>0) ? p[0] : 0.f;
		this.jutting = (p!=null && p.length>1) ? p[1] : 0.f;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param p are the coordinates of the point.
	 */
	public final void set(float... p) {
		this.curviline = (p!=null && p.length>0) ? p[0] : 0.f;
		this.jutting = (p!=null && p.length>1) ? p[1] : 0.f;
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param segment
	 *            the segment
	 * @param p are the coordinates of the point.
	 */
	public final void set(Segment1D segment, Tuple2f<?> p) {
		this.curviline = (p!=null) ? p.getX() : 0.f;
		this.jutting = (p!=null) ? p.getY() : 0.f;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param p are the coordinates of the point.
	 */
	public final void set(Tuple2f<?> p) {
		this.curviline = (p!=null) ? p.getX() : 0.f;
		this.jutting = (p!=null) ? p.getY() : 0.f;
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param p are the coordinates of the point.
	 */
	public final void set(Point1D5 p) {
		this.curviline = (p!=null) ? p.curviline : 0.f;
		this.jutting = (p!=null) ? p.jutting : 0.f;
		Segment1D sgmt = (p!=null) ? p.getSegment() : null;
		this.segment = (sgmt==null) ? null : new WeakReference<Segment1D>(sgmt);
	}

	/** Replies the 1D point which is corresponding to this 1.5D point.
	 * 
	 * @return the 1D point which is corresponding to this 1.5D point.
	 */
	public Point1D toPoint1D() {
		return new Point1D(this);
	}
}
