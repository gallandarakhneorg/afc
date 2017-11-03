/**
 * 
 * org.arakhne.afc.math.geometry.d2.FunctionalPoint3D.java
 *
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 *
 * Primary author : Olivier LAMOTTE (olivier.lamotte@utbm.fr) - 2015
 *
 */
package org.arakhne.afc.math.geometry.d3;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
public interface FunctionalPoint3D extends Point3D {

	
	
	/**
	 * Replies if three points are colinear, ie. one the same line.
	 * <p>
	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param z1
	 *            is the Z coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param z2
	 *            is the Z coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @param z3
	 *            is the Z coordinate of the third point
	 * @param epsilon is the threshold for accepting colinear points.
	 * @return <code>true</code> if the three given points are colinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Pure
	public static boolean isCollinearPoints(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			double epsilon) {
		double dx1 = x2 - x1;
		double dy1 = y2 - y1;
		double dz1 = z2 - z1;
		double dx2 = x3 - x1;
		double dy2 = y3 - y1;
		double dz2 = z3 - z1;

		double cx = dy1 * dz2 - dy2 * dz1;
		double cy = dx2 * dz1 - dx1 * dz2;
		double cz = dx1 * dy2 - dx2 * dy1;

		double sum = cx * cx + cy * cy + cz * cz;

		return MathUtil.isEpsilonZero(sum, epsilon);
	}

	/** Compute the distance between 2 points.
	 *
	 * @param x1 x position of the first point.
	 * @param y1 y position of the first point.
	 * @param z1 z position of the first point.
	 * @param x2 x position of the second point.
	 * @param y2 y position of the second point.
	 * @param z2 z position of the second point.
	 * @return the distance between given points.
	 * @see #distanceSquaredPointPoint(double, double, double, double, double, double)
	 * @see #distanceL1PointPoint(double, double, double, double, double, double)
	 */
	@Pure
	public static double distancePointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx, dy, dz;
		dx = x1 - x2; 
		dy = y1 - y2;
		dz = z1 - z2;
		return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}

	/** Compute the squared distance between 2 points.
	 *
	 * @param x1 x position of the first point.
	 * @param y1 y position of the first point.
	 * @param z1 z position of the first point.
	 * @param x2 x position of the second point.
	 * @param y2 y position of the second point.
	 * @param z2 z position of the second point.
	 * @return the squared distance between given points.
	 * @see #distancePointPoint(double, double, double, double, double, double)
	 * @see #distanceL1PointPoint(double, double, double, double, double, double)
	 */
	@Pure
	public static double distanceSquaredPointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx, dy, dz;
		dx = x1 - x2;
		dy = y1 - y2;
		dz = z1 - z2;
		return dx*dx+dy*dy+dz*dz;
	}

	/** Compute the L-1 (Manhattan) distance between 2 points.
	 * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2) + abs(z1-z2).
	 *
	 * @param x1 x position of the first point.
	 * @param y1 y position of the first point.
	 * @param z1 z position of the first point.
	 * @param x2 x position of the second point.
	 * @param y2 y position of the second point.
	 * @param z2 z position of the second point.
	 * @return the distance between given points.
	 * @see #distancePointPoint(double, double, double, double, double, double)
	 * @see #distanceSquaredPointPoint(double, double, double, double, double, double)
	 */
	@Pure
	public static double distanceL1PointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2) + Math.abs(z1 - z2);
	}

	/** Compute the L-infinite distance between 2 points.
	 * The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2)]. 
	 *
	 * @param x1 x position of the first point.
	 * @param y1 y position of the first point.
	 * @param z1 z position of the first point.
	 * @param x2 x position of the second point.
	 * @param y2 y position of the second point.
	 * @param z2 z position of the second point.
	 * @return the distance between given points.
	 * @see #distancePointPoint(double, double, double, double, double, double)
	 * @see #distanceSquaredPointPoint(double, double, double, double, double, double)
	 */
	@Pure
	public static double distanceLinfPointPoint(double x1, double y1, double z1, double x2, double y2, double z2) {
		return MathUtil.max(Math.abs(x1 - x2), Math.abs(y1 - y2), Math.abs(z1 - z2));
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D p1) {
		double dx, dy, dz;
	    dx = this.getX() - p1.getX();  
	    dy = this.getY() - p1.getY();
	    dz = this.getZ() - p1.getZ();
	    return (dx*dx+dy*dy+dz*dz);
	}

	@Pure
	@Override    
	default double getDistance(Point3D p1) {
		double  dx, dy, dz;
	    dx = this.getX() - p1.getX();  
	    dy = this.getY() - p1.getY();
	    dz = this.getZ() - p1.getZ();
	    return Math.sqrt(dx*dx+dy*dy+dz*dz);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D p1) {
		return (Math.abs(this.getX() - p1.getX()) + Math.abs(this.getY() - p1.getY()) + Math.abs(this.getZ() - p1.getZ()));
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D p1) {
		return Math.max(Math.max( Math.abs(this.getX() - p1.getX()), Math.abs(this.getY() - p1.getY())), Math.abs(this.getZ() - p1.getZ()));
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#distanceSquared(org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Pure
	@Override
	default int distanceSquared(Point3D p1) {
	    return (int)getDistanceSquared(p1);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#distance(org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Pure
	@Override
	default int distance(Point3D p1) {
		 return (int)getDistance(p1);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#distanceL1(org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Pure
	@Override
	default int distanceL1(Point3D p1) {
		return (int)getDistanceL1(p1);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#distanceLinf(org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Pure
	@Override
	default int distanceLinf(Point3D p1) {
		return (int)getDistanceLinf(p1);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#add(org.arakhne.afc.math.geometry.d3.Point3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void add(Point3D t1, Vector3D t2) {
		this.setX(t1.getX() + t2.getX());
		this.setY(t1.getY() + t2.getY());
		this.setZ(t1.getZ() + t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#add(org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Override
	default void add(Vector3D t1, Point3D t2) {
		this.setX(t1.getX() + t2.getX());
		this.setY(t1.getY() + t2.getY());
		this.setZ(t1.getZ() + t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#add(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void add(Vector3D t1) {
		this.setX(this.getX() + t1.getX());
		this.setY(this.getY() + t1.getY());
		this.setZ(this.getZ() + t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#scaleAdd(int, org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Override
	default void scaleAdd(int s, Vector3D t1, Point3D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
		this.setZ(s * t1.getZ() + t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#scaleAdd(double, org.arakhne.afc.math.geometry.d3.Vector3D, org.arakhne.afc.math.geometry.d3.Point3D)
	 */
	@Override
	default void scaleAdd(double s, Vector3D t1, Point3D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
		this.setZ(s * t1.getZ() + t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#scaleAdd(int, org.arakhne.afc.math.geometry.d3.Point3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void scaleAdd(int s, Point3D t1, Vector3D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
		this.setZ(s * t1.getZ() + t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#scaleAdd(double, org.arakhne.afc.math.geometry.d3.Point3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void scaleAdd(double s, Point3D t1, Vector3D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
		this.setZ(s * t1.getZ() + t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#scaleAdd(int, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void scaleAdd(int s, Vector3D t1) {
		this.setX(s * this.getX() + t1.getX());
		this.setY(s * this.getY() + t1.getY());
		this.setZ(s * this.getZ() + t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#scaleAdd(double, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void scaleAdd(double s, Vector3D t1) {
		this.setX(s * this.getX() + t1.getX());
		this.setY(s * this.getY() + t1.getY());
		this.setZ(s * this.getZ() + t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#sub(org.arakhne.afc.math.geometry.d3.Point3D, org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void sub(Point3D t1, Vector3D t2) {
		this.setX(t1.getX() - t2.getX());
		this.setY(t1.getY() - t2.getY());
		this.setZ(t1.getZ() - t2.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Point3D#sub(org.arakhne.afc.math.geometry.d3.Vector3D)
	 */
	@Override
	default void sub(Vector3D t1) {
		this.setX(this.getX() - t1.getX());
		this.setY(this.getY() - t1.getY());
		this.setZ(this.getZ() - t1.getZ());
	}

	
	public interface UnmodifiablePoint3f extends Point3D {
		
		@Override
		default void absolute() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void absolute(Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(double x, double y, double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addZ(int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addZ(double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(int min, int max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(double min, double max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(int min) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(double min) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(int max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(double max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(int min, int max, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(double min, double max, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(int min, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(double min, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(int max, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(double max, Point3D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void negate(Point3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void negate() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(int s, Point3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(double s, Point3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(int s) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(double s) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(Tuple3D<?> t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(double x, double y, double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(int[] t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(double[] t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setZ(int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setZ(double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(int x, int y, int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(double x, double y, double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subZ(int z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subZ(double z) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void interpolate(Point3D t1, Point3D t2, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void interpolate(Point3D t1, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Point3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Vector3D t1, Point3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Vector3D t1, Point3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Vector3D t1, Point3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Point3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Point3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Vector3D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Point3D t1, Vector3D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Vector3D t1) {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
