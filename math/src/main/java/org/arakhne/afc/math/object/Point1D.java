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

import java.lang.ref.WeakReference;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.euclide.EuclidianPoint;
import org.arakhne.afc.math.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.system.CoordinateSystem2D;
import org.arakhne.afc.math.system.CoordinateSystem3D;


/**
 * This class represents a 1D point.
 * <p>
 * A 1D point is defined by its curviline position on a graph segment.
 * <p>
 * Caution: the instances of this classes have a weak reference to the segments.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Point1D implements EuclidianPoint, Comparable<Point1D> {

	private static final long serialVersionUID = -6045082782547903670L;
	
	private float curviline;
	private WeakReference<Segment1D> segment;
	
	/**
	 * Constructs and initializes a Point1D5 from the specified coordinates.
	 * 
	 * @param segment
	 *            the segment
	 * @param curviline
	 *            the curviline coordinate
	 */
	public Point1D(Segment1D segment, float curviline) {
		this.curviline = curviline;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Constructs and initializes a Point1D5 to (0,0).
	 * 
	 * @param segment
	 *            the segment
	 */
	public Point1D(Segment1D segment) {
		this.curviline = 0.f;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Constructs and initializes a Point1D5 to (0,0).
	 * 
	 * @param point is the point to copy.
	 */
	public Point1D(Point1D point) {
		assert(point!=null);
		this.curviline = point.curviline;
		Segment1D sgmt = point.getSegment();
		this.segment = (sgmt==null) ? null : new WeakReference<Segment1D>(sgmt);
	}
	
	/**
	 * Constructs and initializes a Point1D5 to (0,0).
	 * 
	 * @param point is the point to copy.
	 */
	public Point1D(Point1D5 point) {
		assert(point!=null);
		this.curviline = point.getCurvilineCoordinate();
		Segment1D sgmt = point.getSegment();
		this.segment = (sgmt==null) ? null : new WeakReference<Segment1D>(sgmt);
	}

	/** Construct an empty point.
	 */
	protected Point1D() {
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
	public <T extends Point1D> T clone(Class<T> type) {
		assert(type!=null);
		Class<? extends Point1D> myType = getClass();
		if (type.isAssignableFrom(myType)) {
			try {
				Point1D p = myType.newInstance();
				p.set(getSegment(), this.curviline);
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
	public final boolean isOnSameSegment(Point1D p) {
		assert(p!=null);
		return getSegment() == p.getSegment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object o) {
		if (o instanceof Point1D) {
			return equals((Point1D)o);
		}
		return false;
	}

	/** Replies if this point is equals to the given point.
	 * 
	 * @param p
	 * @return <code>true</code> if this point has the same coordinates
	 * on the same segment as for the given point.
	 */
	public boolean equals(Point1D p) {
		return (p!=null && getSegment()==p.getSegment()
				&& this.curviline==p.curviline);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Point1D p) {
		if (p==null) return -1;
		Segment1D mySegment = getSegment();
		Segment1D otherSegment = p.getSegment();
		if (mySegment==otherSegment) {
			return Float.compare(this.curviline, p.curviline);
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
	public boolean epsilonEquals(Point1D p, float epsilon) {
		assert(p!=null);
		return (getSegment()==p.getSegment()) &&
				(Math.abs(p.curviline - this.curviline) <= epsilon);
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
		return (int)(sbits ^ (sbits >> 32) ^ xbits ^ (xbits >> 32));
	}	
	
	/** Add the given values to this point.
	 * 
	 * @param curvilineMove is the quantity to add to the curviline coordinate.
	 */
	public final void add(float curvilineMove) {
		this.curviline += curvilineMove;
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
	public final float distanceSquared(Point1D p1) {
		if (isOnSameSegment(p1)) {
			float a = this.curviline - p1.curviline;
			return a*a;
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
	public final float distance(Point1D p1) {
		if (isOnSameSegment(p1)) {
			return Math.abs(this.curviline - p1.curviline);
		}
		return Float.POSITIVE_INFINITY;
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
	 */
	public final void set(Segment1D segment, float curviline) {
		this.curviline = curviline;
		assert(segment!=null);
		this.segment = new WeakReference<Segment1D>(segment);
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param curviline
	 *            the curviline coordinate
	 */
	public final void set(float curviline) {
		this.curviline = curviline;
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param p are the coordinates of the point.
	 */
	public final void set(Point1D p) {
		this.curviline = (p!=null) ? p.curviline : 0.f;
		Segment1D sgmt = (p!=null) ? p.getSegment() : null;
		this.segment = (sgmt==null) ? null : new WeakReference<Segment1D>(sgmt);
	}

	/**
	 * Set this point from the given informations.
	 * 
	 * @param p are the coordinates of the point.
	 */
	public final void set(Point1D5 p) {
		this.curviline = (p!=null) ? p.getCurvilineCoordinate() : 0.f;
		Segment1D sgmt = (p!=null) ? p.getSegment() : null;
		this.segment = (sgmt==null) ? null : new WeakReference<Segment1D>(sgmt);
	}
	
	/** Replies the 1.5D point which is corresponding to this 1D point.
	 * 
	 * @return the 1.5D point which is corresponding to this 1D point.
	 */
	public Point1D5 toPoint1D5() {
		return new Point1D5(this);
	}

	/** Replies the 2D point which is corresponding to this 1D point.
	 * <p>
	 * Use the coordinate system replied by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 * 
	 * @return the 2D point which is corresponding to this 1D point or <code>null</code>
	 * if this point is not associated to a segment.
	 */
	public EuclidianPoint2D toPoint2D() {
		return toPoint2D(CoordinateSystem2D.getDefaultCoordinateSystem());
	}

	/** Replies the 3D point which is corresponding to this 1D point.
	 * <p>
	 * Use the coordinate system replied by {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 * 
	 * @return the 3D point which is corresponding to this 1D point or <code>null</code>
	 * if this point is not associated to a segment.
	 */
	public EuclidianPoint3D toPoint3D() {
		return toPoint3D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** Replies the 2D point which is corresponding to this 1D point.
	 * 
	 * @param system is the coordinate to use as target.
	 * @return the 2D point which is corresponding to this 1D point or <code>null</code>
	 * if this point is not associated to a segment.
	 */
	public EuclidianPoint2D toPoint2D(CoordinateSystem2D system) {
		Segment1D sgmt = getSegment();
		if (sgmt!=null) {
			EuclidianPoint2D position = new EuclidianPoint2D();
			sgmt.projectsOnPlane(this.curviline, position, null, system);
			return position;
		}
		return null;
	}

	/** Replies the 3D point which is corresponding to this 1D point.
	 * 
	 * @param system is the coordinate to use as target.
	 * @return the 3D point which is corresponding to this 1D point or <code>null</code>
	 * if this point is not associated to a segment.
	 */
	public EuclidianPoint3D toPoint3D(CoordinateSystem3D system) {
		Segment1D sgmt = getSegment();
		if (sgmt!=null) {
			EuclidianPoint2D position = new EuclidianPoint2D();
			sgmt.projectsOnPlane(this.curviline, position, null, system.toCoordinateSystem2D());
			return system.fromCoordinateSystem2D(position);
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append('(');
		buf.append(this.curviline);
		buf.append('/');
		Segment1D sgmt = getSegment();
		buf.append(sgmt!=null ? sgmt.toString() : null);
		buf.append(')');
		return buf.toString();
	}

	@Override
	public int compareCoordinateTo(int coordinateIndex, float value) {
		if (coordinateIndex==0)
			return Float.compare(this.curviline, value);
		throw new IndexOutOfBoundsException(coordinateIndex+"!=0"); //$NON-NLS-1$
	}

	@Override
	public int compareCoordinateToEpsilon(int coordinateIndex, float value) {
		if (coordinateIndex==0)
			return MathUtil.epsilonCompareToDistance(this.curviline, value);
		throw new IndexOutOfBoundsException(coordinateIndex+"!=0"); //$NON-NLS-1$
	}

	@Override
	public PseudoHamelDimension getMathematicalDimension() {
		return PseudoHamelDimension.DIMENSION_1D;
	}

	@Override
	public float getCoordinate(int coordinateIndex) {
		return (coordinateIndex==0) ? this.curviline : Float.NaN;
	}
	
	@Override
	public void setCoordinate(int coordinateIndex, float value) {
		if (coordinateIndex==0) this.curviline = value;
	}

	@Override
	public void toTuple2f(Tuple2f<?> tuple) {
		tuple.setX(this.curviline);
		tuple.setY(Float.NaN);
	}

	@Override
	public void toTuple3f(Tuple3f<?> tuple) {
		tuple.setX(this.curviline);
		tuple.setY(Float.NaN);
		tuple.setZ(Float.NaN);
	}

}
