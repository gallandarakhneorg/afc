/**
 * 
 * fr.utbm.v3g.core.math.FunctionalPoint2D.java
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
package org.arakhne.afc.math.geometry.d2;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface FunctionalPoint2D extends Point2D {

	
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
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @return <code>true</code> if the three given points are colinear.
	 * @since 3.0
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Pure
	public static boolean isCollinearPoints(double x1, double y1, double x2, double y2, double x3, double y3) {
		// Test if three points are colinears
		// iff det( [ x1 x2 x3 ]
		// [ y1 y2 y3 ]
		// [ 1 1 1 ] ) = 0
		// Do not invoked MathUtil.determinant() to limit computations.
		return MathUtil.isEpsilonZero(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
	}

	/** Compute the distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #distanceSquaredPointPoint(double, double, double, double)
	 * @see #distanceL1PointPoint(double, double, double, double)
	 */
	@Pure
	public static double distancePointPoint(double x1, double y1, double x2, double y2) {
		double dx, dy;
		dx = x1 - x2;
		dy = y1 - y2;
		return Math.sqrt(dx*dx+dy*dy);
	}

	/** Compute the squared distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the squared distance between given points.
	 * @see #distancePointPoint(double, double, double, double)
	 * @see #distanceL1PointPoint(double, double, double, double)
	 */
	@Pure
	public static double distanceSquaredPointPoint(double x1, double y1, double x2, double y2) {
		double dx, dy;
		dx = x1 - x2;
		dy = y1 - y2;
		return dx*dx+dy*dy;
	}

	/** Compute the L-1 (Manhattan) distance between 2 points.
	 * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #distancePointPoint(double, double, double, double)
	 * @see #distanceSquaredPointPoint(double, double, double, double)
	 */
	@Pure
	public static double distanceL1PointPoint(double x1, double y1, double x2, double y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	/** Compute the L-infinite distance between 2 points.
	 * The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the distance between given points.
	 * @see #distancePointPoint(double, double, double, double)
	 * @see #distanceSquaredPointPoint(double, double, double, double)
	 */
	@Pure
	public static double distanceLinfPointPoint(double x1, double y1, double x2, double y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#getDistanceSquared(org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Pure
	@Override
	default double getDistanceSquared(Point2D p1) {
		double dx, dy;
	    dx = this.getX() - p1.getX();  
	    dy = this.getY() - p1.getY();
	    return (dx*dx+dy*dy);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#getDistance(org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Pure
	@Override
	default double getDistance(Point2D p1) {
		double  dx, dy;
	    dx = this.getX() - p1.getX();  
	    dy = this.getY() - p1.getY();
	    return Math.sqrt(dx*dx+dy*dy);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#getDistanceL1(org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Pure
	@Override
	default double getDistanceL1(Point2D p1) {
		return (Math.abs(this.getX() - p1.getX()) + Math.abs(this.getY() - p1.getY()));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#getDistanceLinf(org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Pure
	@Override
	default double getDistanceLinf(Point2D p1) {
		return Math.max(Math.abs(this.getX() - p1.getX()), Math.abs(this.getY() - p1.getY()));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#distanceSquared(org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Pure
	@Override
	default int distanceSquared(Point2D p1) {
		return (int) getDistanceSquared(p1);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#distance(org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Pure
	@Override
	default int distance(Point2D p1) {
		return (int) getDistance(p1);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#distanceL1(org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Pure
	@Override
	default int distanceL1(Point2D p1) {
		return (int) getDistanceL1(p1);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#distanceLinf(org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Pure
	@Override
	default int distanceLinf(Point2D p1) {
		return (int) getDistanceLinf(p1);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#add(org.arakhne.afc.math.geometry.d2.Point2D, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void add(Point2D t1, Vector2D t2) {
		this.setX(t1.getX() + t2.getX());
		this.setY(t1.getY() + t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#add(org.arakhne.afc.math.geometry.d2.Vector2D, org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Override
	default void add(Vector2D t1, Point2D t2) {
		this.setX(t1.getX() + t2.getX());
		this.setY(t1.getY() + t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#add(org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void add(Vector2D t1) {
		this.setX(this.getX() + t1.getX());
		this.setY(this.getY() + t1.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#scaleAdd(int, org.arakhne.afc.math.geometry.d2.Vector2D, org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Override
	default void scaleAdd(int s, Vector2D t1, Point2D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#scaleAdd(double, org.arakhne.afc.math.geometry.d2.Vector2D, org.arakhne.afc.math.geometry.d2.Point2D)
	 */
	@Override
	default void scaleAdd(double s, Vector2D t1, Point2D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#scaleAdd(int, org.arakhne.afc.math.geometry.d2.Point2D, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void scaleAdd(int s, Point2D t1, Vector2D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#scaleAdd(double, org.arakhne.afc.math.geometry.d2.Point2D, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void scaleAdd(double s, Point2D t1, Vector2D t2) {
		this.setX(s * t1.getX() + t2.getX());
		this.setY(s * t1.getY() + t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#scaleAdd(int, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void scaleAdd(int s, Vector2D t1) {
		this.setX(s * this.getX() + t1.getX());
		this.setY(s * this.getY() + t1.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#scaleAdd(double, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void scaleAdd(double s, Vector2D t1) {
		this.setX(s * this.getX() + t1.getX());
		this.setY(s * this.getY() + t1.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#sub(org.arakhne.afc.math.geometry.d2.Point2D, org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void sub(Point2D t1, Vector2D t2) {
		this.setX(t1.getX() - t2.getX());
		this.setY(t1.getY() - t2.getY());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Point2D#sub(org.arakhne.afc.math.geometry.d2.Vector2D)
	 */
	@Override
	default void sub(Vector2D t1) {
		this.setX(this.getX() - t1.getX());
		this.setY(this.getY() - t1.getY());
	}


	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public interface UnmodifiablePoint2f extends Point2D {

		@Override
		default void absolute() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void absolute(Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(double x, double y) {
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
		default void clamp(int min, int max, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(double min, double max, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(int min, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(double min, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(int max, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(double max, Point2D t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void negate(Point2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void negate() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(int s, Point2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(double s, Point2D t1) {
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
		default void set(Tuple2D<?> t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(double x, double y) {
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
		default void sub(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(double x, double y) {
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
		default void interpolate(Point2D t1, Point2D t2, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void interpolate(Point2D t1, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Vector2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Vector2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Vector2D t1, Point2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(int s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scaleAdd(double s, Vector2D t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Point2D t1, Vector2D t2) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(Vector2D t1) {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
