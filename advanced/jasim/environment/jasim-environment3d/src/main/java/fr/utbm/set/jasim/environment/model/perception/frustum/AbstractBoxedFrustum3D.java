/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.lang.ref.SoftReference;
import java.util.UUID;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds3d.AbstractBounds3D;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.object.Line3D;
import fr.utbm.set.geom.plane.PlanarClassificationType;
import fr.utbm.set.geom.plane.Plane;
import fr.utbm.set.geom.plane.Plane4d;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.math.MathUtil;

/**
 * A frustum represented by a box composed of six planes.
 * Abstract class for a frustum represented by a box composed of six planes.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: jdemange$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractBoxedFrustum3D
extends AbstractBounds3D
implements Frustum3D {

	private static final long serialVersionUID = 3873871632635174834L;

	/** Index of the left plane.
	 */
	public static final int LEFT = 0;

	/** Index of the right plane.
	 */
	public static final int RIGHT = 1;

	/** Index of the top plane.
	 */
	public static final int TOP = 2;

	/** Index of the bottom plane.
	 */
	public static final int BOTTOM = 3;

	/** Index of the front plane.
	 */
	public static final int FRONT = 4;

	/** Index of the back plane.
	 */
	public static final int BACK = 5;

	/** Index of the front right top point.
	 */
	public static final int POINT_INDEX_FRONT_RIGHT_TOP = 0;

	/** Index of the front left top point.
	 */
	public static final int POINT_INDEX_FRONT_LEFT_TOP = 1;

	/** Index of the front left bottom point.
	 */
	public static final int POINT_INDEX_FRONT_LEFT_BOTTOM = 2;

	/** Index of the front right bottom point.
	 */
	public static final int POINT_INDEX_FRONT_RIGHT_BOTTOM = 3;

	/** Index of the back right top point.
	 */
	public static final int POINT_INDEX_BACK_RIGHT_TOP = 4;

	/** Index of the back left top point.
	 */
	public static final int POINT_INDEX_BACK_LEFT_TOP = 5;

	/** Index of the back left bottom point.
	 */
	public static final int POINT_INDEX_BACK_LEFT_BOTTOM = 6;

	/** Index of the back right bottom point.
	 */
	public static final int POINT_INDEX_BACK_RIGHT_BOTTOM = 7;

	/** Cache for the eye position.
	 */
	private transient SoftReference<EuclidianPoint3D> cachedEye = null;

	/** Cache for the lower point.
	 */
	private transient SoftReference<AlignedBoundingBox> aabb = null;	

	/** Cache for the frustum orientation.
	 */
	private transient SoftReference<Direction3D> cachedOrientation = null;

	/** Cache for the points.
	 */
	private transient SoftReference<Point3d[]> cachedPoints = null;

	/** List of plane composing the frustum.
	 */
	protected Plane4d[] planes = new Plane4d[6];
	
	private final UUID id;

	/**
	 * @param identifier
	 */
	public AbstractBoxedFrustum3D(UUID identifier) {
		this.id = identifier;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractBoxedFrustum3D clone() {
		AbstractBoxedFrustum3D clone = (AbstractBoxedFrustum3D)super.clone();
		clone.planes = new Plane4d[6];
		for(int i=0; i<6; ++i) {
			clone.planes[i] = this.planes[i].clone();
		}
		return clone;
	}
	
	/** Update the points coordinates from the plane equations.
	 */
	private void updatePoints(Point3d[] points) {
		Line3D lineL = this.planes[LEFT].getIntersection(this.planes[FRONT]);
		assert(lineL!=null);
		Line3D lineR = this.planes[RIGHT].getIntersection(this.planes[FRONT]);
		assert(lineR!=null);

		points[POINT_INDEX_FRONT_RIGHT_TOP] = this.planes[TOP].getIntersection(lineR);
		assert(points[POINT_INDEX_FRONT_RIGHT_TOP]!=null);
		points[POINT_INDEX_FRONT_LEFT_TOP] = this.planes[TOP].getIntersection(lineL);
		assert(points[POINT_INDEX_FRONT_LEFT_TOP]!=null);
		points[POINT_INDEX_FRONT_LEFT_BOTTOM] = this.planes[BOTTOM].getIntersection(lineL);
		assert(points[POINT_INDEX_FRONT_LEFT_BOTTOM]!=null);
		points[POINT_INDEX_FRONT_RIGHT_BOTTOM] = this.planes[BOTTOM].getIntersection(lineR);
		assert(points[POINT_INDEX_FRONT_RIGHT_BOTTOM]!=null);

		lineL = this.planes[LEFT].getIntersection(this.planes[BACK]);
		assert(lineL!=null);
		lineR = this.planes[RIGHT].getIntersection(this.planes[BACK]);
		assert(lineR!=null);

		points[POINT_INDEX_BACK_RIGHT_TOP] = this.planes[TOP].getIntersection(lineR);
		assert(points[POINT_INDEX_BACK_RIGHT_TOP]!=null);
		points[POINT_INDEX_BACK_LEFT_TOP] = this.planes[TOP].getIntersection(lineL);
		assert(points[POINT_INDEX_BACK_LEFT_TOP]!=null);
		points[POINT_INDEX_BACK_LEFT_BOTTOM] = this.planes[BOTTOM].getIntersection(lineL);
		assert(points[POINT_INDEX_BACK_LEFT_BOTTOM]!=null);
		points[POINT_INDEX_BACK_RIGHT_BOTTOM] = this.planes[BOTTOM].getIntersection(lineR);
		assert(points[POINT_INDEX_BACK_RIGHT_BOTTOM]!=null);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector3d getSize() {
		double lx, ly, lz, ux, uy, uz;
		lx = ly = lz = Double.POSITIVE_INFINITY;
		ux = uy = uz = Double.NEGATIVE_INFINITY;
		for (Point3d p : getPoints()) {
			if (p.x<lx) lx = p.x;
			if (p.y<ly) ly = p.y;
			if (p.z<lz) lz = p.z;
			if (p.x>ux) ux = p.x;
			if (p.y>uy) uy = p.y;
			if (p.z>uz) uz = p.z;
		}
		return new Vector3d(ux-lx, uy-ly, uz-lz);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSizeX() {
		double l, u;
		l = Double.POSITIVE_INFINITY;
		u = Double.NEGATIVE_INFINITY;
		for (Point3d p : getPoints()) {
			if (p.x<l) l = p.x;
			if (p.x>u) u = p.x;
		}
		return u-l;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSizeY() {
		double l, u;
		l = Double.POSITIVE_INFINITY;
		u = Double.NEGATIVE_INFINITY;
		for (Point3d p : getPoints()) {
			if (p.y<l) l = p.y;
			if (p.y>u) u = p.y;
		}
		return u-l;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSizeZ() {
		double l, u;
		l = Double.POSITIVE_INFINITY;
		u = Double.NEGATIVE_INFINITY;
		for (Point3d p : getPoints()) {
			if (p.z<l) l = p.z;
			if (p.z>u) u = p.z;
		}
		return u-l;
	}

	/** Clear the cached values.
	 */
	protected void clearCachedValues() {
		this.cachedEye = null;
		this.cachedOrientation = null;
		this.cachedPoints = null;
		this.aabb = null;
	}

	/** Replies the frustum points.
	 * 
	 * @return all the corner points of the box corner. 
	 */
	public Point3d[] getPoints() {
		Point3d[] pts = this.cachedPoints==null ? null : this.cachedPoints.get();
		if (pts==null) {
			pts = new Point3d[8];
			updatePoints(pts);
			this.cachedPoints = new SoftReference<Point3d[]>(pts);
		}
		return pts;
	}   

	/** {@inheritDoc}
	 */
	@Override
	public PlanarClassificationType classifiesAgainst(Plane plane) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getEye() {
		EuclidianPoint3D p = this.cachedEye==null ? null : this.cachedEye.get();
		if (p!=null) return p;
		Line3D intersectionLine = this.planes[LEFT].getIntersection(this.planes[RIGHT]);
		if (intersectionLine==null) throw new RuntimeException("planes do not converge"); //$NON-NLS-1$
		Point3d i = this.planes[TOP].getIntersection(intersectionLine);
		if (i==null) throw new RuntimeException("planes do not converge"); //$NON-NLS-1$
		p = new EuclidianPoint3D(i);
		this.cachedEye = new SoftReference<EuclidianPoint3D>(p);
		return p;
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * @return {@code 0}
	 */
	@Override
	public double getNearDistance() {
		return 0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void rotate(Quat4d rotation) {
		Matrix4d mat = GeometryUtil.lookAt(getViewDirection(), CoordinateSystem3D.getDefaultCoordinateSystem());
		Matrix4d mat2 = new Matrix4d();
		mat2.set(rotation);
		mat.mul(mat2);
		Quat4d q = new Quat4d();
		mat.get(q);
		setRotation(q);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void transform(Transform3D trans) {
		Quat4d q = new Quat4d();
		trans.get(q);
		rotate(q);
		translate(trans.getTranslationVector());
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void translate(Vector3d t) {
		for(Plane4d p : this.planes) {
			p.translate(t);
		}
		clearCachedValues();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		Vector3d v = getSize();
		return v.x<=0. || v.y<=0. || v.z<=0.;
	}
	
	/**
	 * Replies the minimal squared distance between a point an a 4-side polygon.
	 * 
	 * @param x is the coordinate of the point.
	 * @param y is the coordinate of the point.
	 * @param z is the coordinate of the point.
	 * @param plane is the equation of the plane on which the polygon lies.
	 * @param p1 is the first point of the polygon.
	 * @param p2 is the second point of the polygon.
	 * @param p3 is the third point of the polygon.
	 * @param p4 is the fourth point of the polygon.
	 * @return the minimal distance.
	 */
	private static double distanceSquaredToRect(Point3d p,
			Plane plane,
			Point3d p1, Point3d p2, Point3d p3, Point3d p4) {

		Point3d proj = GeometryUtil.projectsPointOnPlaneIn3d(p, plane);

		double a = 0.;

		a += GeometryUtil.angle(p1.x-proj.x, p1.y-proj.y, p1.z-proj.z,
				p2.x-proj.x, p2.y-proj.y, p2.z-proj.z);

		a += GeometryUtil.angle(p2.x-proj.x, p2.y-proj.y, p2.z-proj.z,
				p3.x-proj.x, p3.y-proj.y, p3.z-proj.z);

		a += GeometryUtil.angle(p3.x-proj.x, p3.y-proj.y, p3.z-proj.z,
				p4.x-proj.x, p4.y-proj.y, p4.z-proj.z);

		a += GeometryUtil.angle(p4.x-proj.x, p4.y-proj.y, p4.z-proj.z,
				p1.x-proj.x, p1.y-proj.y, p1.z-proj.z);

		a = GeometryUtil.clampRadian0To2PI(a);

		// Is the projected point inside the rectangle?
		if (GeometryUtil.epsilonEqualsZeroRadian(a)) {
			// Yes, compute the distance between the point and its projection
			return GeometryUtil.distanceSquared(p,proj);
		}

		// Projected point outside the rectangle.
		// Compute the best distance to the bounds of the rectangle.
		return MathUtil.min(
				GeometryUtil.distanceSquaredPointSegment(p.x, p.y, p.z, 
						p1.x, p1.y, p1.z, p2.x, p2.y, p2.z),
						GeometryUtil.distanceSquaredPointSegment(p.x, p.y, p.z, 
								p2.x, p2.y, p2.z, p3.x, p3.y, p3.z),
								GeometryUtil.distanceSquaredPointSegment(p.x, p.y, p.z, 
										p3.x, p3.y, p3.z, p4.x, p4.y, p4.z),
										GeometryUtil.distanceSquaredPointSegment(p.x, p.y, p.z, 
												p4.x, p4.y, p4.z, p1.x, p1.y, p1.z));

	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double distanceSquared(Point3d point) {
		if (intersects(point)) return 0.;

		Point3d[] pts = getPoints();
		double min, d;

		min = distanceSquaredToRect(point,
				this.planes[FRONT],
				pts[POINT_INDEX_FRONT_LEFT_BOTTOM],
				pts[POINT_INDEX_FRONT_LEFT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_BOTTOM]);
		if (min<=0.) return 0.;

		d = distanceSquaredToRect(point,
				this.planes[BACK],
				pts[POINT_INDEX_BACK_LEFT_BOTTOM],
				pts[POINT_INDEX_BACK_LEFT_TOP],
				pts[POINT_INDEX_BACK_RIGHT_TOP],
				pts[POINT_INDEX_BACK_RIGHT_BOTTOM]);
		if (d<=0.) return 0.;
		if (d<min) min = d;

		d = distanceSquaredToRect(point,
				this.planes[LEFT],
				pts[POINT_INDEX_BACK_LEFT_BOTTOM],
				pts[POINT_INDEX_BACK_LEFT_TOP],
				pts[POINT_INDEX_FRONT_LEFT_TOP],
				pts[POINT_INDEX_FRONT_LEFT_BOTTOM]);
		if (d<=0.) return 0.;
		if (d<min) min = d;

		d = distanceSquaredToRect(point,
				this.planes[RIGHT],
				pts[POINT_INDEX_BACK_RIGHT_BOTTOM],
				pts[POINT_INDEX_BACK_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_BOTTOM]);
		if (d<=0.) return 0.;
		if (d<min) min = d;

		d = distanceSquaredToRect(point,
				this.planes[TOP],
				pts[POINT_INDEX_BACK_LEFT_TOP],
				pts[POINT_INDEX_BACK_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_LEFT_TOP]);
		if (d<=0.) return 0.;
		if (d<min) min = d;

		d = distanceSquaredToRect(point,
				this.planes[BOTTOM],
				pts[POINT_INDEX_BACK_LEFT_BOTTOM],
				pts[POINT_INDEX_BACK_RIGHT_BOTTOM],
				pts[POINT_INDEX_FRONT_RIGHT_BOTTOM],
				pts[POINT_INDEX_FRONT_LEFT_BOTTOM]);
		if (d<=0.) return 0.;
		if (d<min) min = d;

		return min;
	}

	/**
	 * Replies the nearest point to a given point on a 4-side polygon.
	 * 
	 * @param nearest is the point to fill with nearest coordinates.
	 * @param reference is the reference point.
	 * @param plane is the equation of the plane on which the polygon lies.
	 * @param p1 is the first point of the polygon.
	 * @param p2 is the second point of the polygon.
	 * @param p3 is the third point of the polygon.
	 * @param p4 is the fourth point of the polygon.
	 * @return distance
	 */
	private static double nearToRect(
			Tuple3d nearest, Point3d reference,
			Plane plane,
			Point3d p1, Point3d p2, Point3d p3, Point3d p4) {

		Point3d proj = GeometryUtil.projectsPointOnPlaneIn3d(reference, plane);

		double a = 0.;

		a += GeometryUtil.angle(p1.x-proj.x, p1.y-proj.y, p1.z-proj.z,
				p2.x-proj.x, p2.y-proj.y, p2.z-proj.z);

		a += GeometryUtil.angle(p2.x-proj.x, p2.y-proj.y, p2.z-proj.z,
				p3.x-proj.x, p3.y-proj.y, p3.z-proj.z);

		a += GeometryUtil.angle(p3.x-proj.x, p3.y-proj.y, p3.z-proj.z,
				p4.x-proj.x, p4.y-proj.y, p4.z-proj.z);

		a += GeometryUtil.angle(p4.x-proj.x, p4.y-proj.y, p4.z-proj.z,
				p1.x-proj.x, p1.y-proj.y, p1.z-proj.z);

		a = GeometryUtil.clampRadian0To2PI(a);

		// Is the projected point inside the rectangle?
		if (GeometryUtil.epsilonEqualsZeroRadian(a)) {
			// Yes, replies the projection
			nearest.set(proj);
			return proj.distance(reference);
		}
		// Projected point outside the rectangle.
		// Compute the best distance to the bounds of the rectangle.
		double d, dmin;
		Point3d p = new Point3d();
		
		GeometryUtil.getNearestPointOnSegment(
				reference.x, reference.y, reference.z, 
				p1.x, p1.y, p1.z, p2.x, p2.y, p2.z,
				p);
		nearest.set(p);
		dmin = p.distance(reference);
		
		GeometryUtil.getNearestPointOnSegment(
				reference.x, reference.y, reference.z, 
				p2.x, p2.y, p2.z, p3.x, p3.y, p3.z,
				p);
		d = p.distance(reference);
		if (dmin>d) {
			dmin = d;
			nearest.set(p);
		}

		GeometryUtil.getNearestPointOnSegment(
				reference.x, reference.y, reference.z, 
				p3.x, p3.y, p3.z, p4.x, p4.y, p4.z,
				p);
		d = p.distance(reference);
		if (dmin>d) {
			dmin = d;
			nearest.set(p);
		}
		
		GeometryUtil.getNearestPointOnSegment(
				reference.x, reference.y, reference.z, 
				p4.x, p4.y, p4.z, p1.x, p1.y, p1.z,
				p);
		d = p.distance(reference);
		if (dmin>d) {
			dmin = d;
			nearest.set(p);
		}
		
		return dmin;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D nearestPoint(Point3d reference) {
		EuclidianPoint3D nearest = new EuclidianPoint3D();
		if (intersects(reference)) {
			nearest.set(reference);
		}
		else {
			Point3d near = new Point3d();
			Point3d[] pts = getPoints();
			double dmin, d;
			dmin = nearToRect(nearest,
					reference,
					this.planes[FRONT],
					pts[POINT_INDEX_FRONT_LEFT_BOTTOM],
					pts[POINT_INDEX_FRONT_LEFT_TOP],
					pts[POINT_INDEX_FRONT_RIGHT_TOP],
					pts[POINT_INDEX_FRONT_RIGHT_BOTTOM]);

			d = nearToRect(near,
					reference,
					this.planes[BACK],
					pts[POINT_INDEX_BACK_LEFT_BOTTOM],
					pts[POINT_INDEX_BACK_LEFT_TOP],
					pts[POINT_INDEX_BACK_RIGHT_TOP],
					pts[POINT_INDEX_BACK_RIGHT_BOTTOM]);
			if (d<dmin) {
				dmin = d;
				nearest.set(near);
			}

			d = nearToRect(near,
					reference,
					this.planes[LEFT],
					pts[POINT_INDEX_BACK_LEFT_BOTTOM],
					pts[POINT_INDEX_BACK_LEFT_TOP],
					pts[POINT_INDEX_FRONT_LEFT_TOP],
					pts[POINT_INDEX_FRONT_LEFT_BOTTOM]);
			if (d<dmin) {
				dmin = d;
				nearest.set(near);
			}

			d = nearToRect(near,
					reference,
					this.planes[RIGHT],
					pts[POINT_INDEX_BACK_RIGHT_BOTTOM],
					pts[POINT_INDEX_BACK_RIGHT_TOP],
					pts[POINT_INDEX_FRONT_RIGHT_TOP],
					pts[POINT_INDEX_FRONT_RIGHT_BOTTOM]);
			if (d<dmin) {
				dmin = d;
				nearest.set(near);
			}

			d = nearToRect(near,
					reference,
					this.planes[TOP],
					pts[POINT_INDEX_BACK_LEFT_TOP],
					pts[POINT_INDEX_BACK_RIGHT_TOP],
					pts[POINT_INDEX_FRONT_RIGHT_TOP],
					pts[POINT_INDEX_FRONT_LEFT_TOP]);
			if (d<dmin) {
				dmin = d;
				nearest.set(near);
			}

			d = nearToRect(near,
					reference,
					this.planes[BOTTOM],
					pts[POINT_INDEX_BACK_LEFT_BOTTOM],
					pts[POINT_INDEX_BACK_RIGHT_BOTTOM],
					pts[POINT_INDEX_FRONT_RIGHT_BOTTOM],
					pts[POINT_INDEX_FRONT_LEFT_BOTTOM]);
			if (d<dmin) {
				nearest.set(near);
			}
		}
		
		return nearest;
	}
	
	/**
	 * Replies the maximal squared distance between a point an a 4-side polygon.
	 * 
	 * @param x is the coordinate of the point.
	 * @param y is the coordinate of the point.
	 * @param z is the coordinate of the point.
	 * @param plane is the equation of the plane on which the polygon lies.
	 * @param p1 is the first point of the polygon.
	 * @param p2 is the second point of the polygon.
	 * @param p3 is the third point of the polygon.
	 * @param p4 is the fourth point of the polygon.
	 * @return the maximal distance.
	 */
	private static double distanceMaxSquaredToRect(double x, double y, double z,
			Plane plane,
			Point3d p1, Point3d p2, Point3d p3, Point3d p4) {
		// Projected point outside the rectangle.
		// Compute the best distance to the bounds of the rectangle.
		return MathUtil.max(
				GeometryUtil.distanceSquaredPointSegment(x, y, z, 
						p1.x, p1.y, p1.z, p2.x, p2.y, p2.z),
						GeometryUtil.distanceSquaredPointSegment(x, y, z, 
								p2.x, p2.y, p2.z, p3.x, p3.y, p3.z),
								GeometryUtil.distanceSquaredPointSegment(x, y, z, 
										p3.x, p3.y, p3.z, p4.x, p4.y, p4.z),
										GeometryUtil.distanceSquaredPointSegment(x, y, z, 
												p4.x, p4.y, p4.z, p1.x, p1.y, p1.z));

	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceMaxSquared(Point3d point) {
		Point3d[] pts = getPoints();
		double max, d;

		max = distanceMaxSquaredToRect(point.x,point.y,point.z,
				this.planes[FRONT],
				pts[POINT_INDEX_FRONT_LEFT_BOTTOM],
				pts[POINT_INDEX_FRONT_LEFT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_BOTTOM]);

		d = distanceMaxSquaredToRect(point.x,point.y,point.z,
				this.planes[BACK],
				pts[POINT_INDEX_BACK_LEFT_BOTTOM],
				pts[POINT_INDEX_BACK_LEFT_TOP],
				pts[POINT_INDEX_BACK_RIGHT_TOP],
				pts[POINT_INDEX_BACK_RIGHT_BOTTOM]);
		if (d>max) max = d;

		d = distanceMaxSquaredToRect(point.x,point.y,point.z,
				this.planes[LEFT],
				pts[POINT_INDEX_BACK_LEFT_BOTTOM],
				pts[POINT_INDEX_BACK_LEFT_TOP],
				pts[POINT_INDEX_FRONT_LEFT_TOP],
				pts[POINT_INDEX_FRONT_LEFT_BOTTOM]);
		if (d>max) max = d;

		d = distanceMaxSquaredToRect(point.x,point.y,point.z,
				this.planes[RIGHT],
				pts[POINT_INDEX_BACK_RIGHT_BOTTOM],
				pts[POINT_INDEX_BACK_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_BOTTOM]);
		if (d>max) max = d;

		d = distanceMaxSquaredToRect(point.x,point.y,point.z,
				this.planes[TOP],
				pts[POINT_INDEX_BACK_LEFT_TOP],
				pts[POINT_INDEX_BACK_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_LEFT_TOP]);
		if (d>max) max = d;

		d = distanceMaxSquaredToRect(point.x,point.y,point.z,
				this.planes[BOTTOM],
				pts[POINT_INDEX_BACK_LEFT_BOTTOM],
				pts[POINT_INDEX_BACK_RIGHT_BOTTOM],
				pts[POINT_INDEX_FRONT_RIGHT_BOTTOM],
				pts[POINT_INDEX_FRONT_LEFT_BOTTOM]);
		if (d>max) max = d;

		return max;
	}

	/**
	 * Replies the farest point to a given point on a 4-side polygon.
	 * 
	 * @param farest is the point to fill with farest coordinates.
	 * @param reference is the reference point.
	 * @param p1 is the first point of the polygon.
	 * @param p2 is the second point of the polygon.
	 * @param p3 is the third point of the polygon.
	 * @param p4 is the fourth point of the polygon.
	 * @return distance
	 */
	private static double farToRect(
			Tuple3d farest, Point3d reference,
			Point3d p1, Point3d p2, Point3d p3, Point3d p4) {
		// Projected point outside the rectangle.
		// Compute the best distance to the bounds of the rectangle.
		double d, dmax;
		Point3d p = new Point3d();
		
		GeometryUtil.getFarestPointOnSegment(
				reference.x, reference.y, reference.z, 
				p1.x, p1.y, p1.z, p2.x, p2.y, p2.z,
				p);
		farest.set(p);
		dmax = p.distance(reference);
		
		GeometryUtil.getFarestPointOnSegment(
				reference.x, reference.y, reference.z, 
				p2.x, p2.y, p2.z, p3.x, p3.y, p3.z,
				p);
		d = p.distance(reference);
		if (dmax<d) {
			dmax = d;
			farest.set(p);
		}

		GeometryUtil.getFarestPointOnSegment(
				reference.x, reference.y, reference.z, 
				p3.x, p3.y, p3.z, p4.x, p4.y, p4.z,
				p);
		d = p.distance(reference);
		if (dmax<d) {
			dmax = d;
			farest.set(p);
		}
		
		GeometryUtil.getFarestPointOnSegment(
				reference.x, reference.y, reference.z, 
				p4.x, p4.y, p4.z, p1.x, p1.y, p1.z,
				p);
		d = p.distance(reference);
		if (dmax<d) {
			farest.set(p);
		}
		
		return dmax;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D farestPoint(Point3d reference) {
		EuclidianPoint3D farest = new EuclidianPoint3D();
		
		Point3d near = new Point3d();
		Point3d[] pts = getPoints();
		
		double dmax, d;
		
		dmax = farToRect(farest,
				reference,
				pts[POINT_INDEX_FRONT_LEFT_BOTTOM],
				pts[POINT_INDEX_FRONT_LEFT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_BOTTOM]);

		d = farToRect(near,
				reference,
				pts[POINT_INDEX_BACK_LEFT_BOTTOM],
				pts[POINT_INDEX_BACK_LEFT_TOP],
				pts[POINT_INDEX_BACK_RIGHT_TOP],
				pts[POINT_INDEX_BACK_RIGHT_BOTTOM]);
		if (d>dmax) {
			dmax = d;
			farest.set(near);
		}

		d = farToRect(near,
				reference,
				pts[POINT_INDEX_BACK_LEFT_BOTTOM],
				pts[POINT_INDEX_BACK_LEFT_TOP],
				pts[POINT_INDEX_FRONT_LEFT_TOP],
				pts[POINT_INDEX_FRONT_LEFT_BOTTOM]);
		if (d>dmax) {
			dmax = d;
			farest.set(near);
		}

		d = farToRect(near,
				reference,
				pts[POINT_INDEX_BACK_RIGHT_BOTTOM],
				pts[POINT_INDEX_BACK_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_BOTTOM]);
		if (d>dmax) {
			dmax = d;
			farest.set(near);
		}

		d = farToRect(near,
				reference,
				pts[POINT_INDEX_BACK_LEFT_TOP],
				pts[POINT_INDEX_BACK_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_RIGHT_TOP],
				pts[POINT_INDEX_FRONT_LEFT_TOP]);
		if (d>dmax) {
			dmax = d;
			farest.set(near);
		}

		d = farToRect(near,
				reference,
				pts[POINT_INDEX_BACK_LEFT_BOTTOM],
				pts[POINT_INDEX_BACK_RIGHT_BOTTOM],
				pts[POINT_INDEX_FRONT_RIGHT_BOTTOM],
				pts[POINT_INDEX_FRONT_LEFT_BOTTOM]);
		if (d>dmax) {
			farest.set(near);
		}
		
		return farest;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getCenter() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return null;
		return box.getCenter();
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getUpper() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return null;
		return box.getUpper();
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getLower() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return null;
		return box.getLower();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point3d lower, Point3d upper) {
		AlignedBoundingBox box = toBoundingBox();
		if (box!=null) {
			box.getLowerUpper(lower, upper);
		}
	}


	/**
	 * Compute the intersection between this frustum and the specified OBB using the Separating Axis Test method
	 * see Geometric Tools for Computer Graphics, pages 624-626
	 */
	@Override
	public IntersectionType classifies(Point3d centerOBB, Vector3d[] axisOBB, double[] extentOBB) {
		//Computation of Frustum characteristics
		AxisAngle4d view = this.getViewDirection();
		Vector3d d = new Vector3d(view.x,view.y,view.z);
		Vector3d up = this.planes[TOP].getNormal();
		GeometryUtil.perpendicularize(d, up);
		Vector3d left = MathUtil.crossProductRightHand(d.x, d.y, d.z, up.x, up.y, up.z);					
		double n = this.getNearDistance();
		double l = this.cachedPoints.get()[POINT_INDEX_BACK_LEFT_BOTTOM].distance(this.cachedPoints.get()[POINT_INDEX_BACK_RIGHT_BOTTOM])/2;//demi extent en largeur sur le near
		double mu = this.cachedPoints.get()[POINT_INDEX_BACK_LEFT_BOTTOM].distance(this.cachedPoints.get()[POINT_INDEX_BACK_LEFT_TOP])/2;// demi extent en hauteur sur le near

		//************************************************************************************
		//Computation of the 26 vectors to test
		Vector3d[] m  = new Vector3d[26];

		//d	
		m[0] = new Vector3d(d);

		//+n*L-l*D
		m[1] = new Vector3d(left);
		m[1].scale(n);
		Vector3d v2bis = new Vector3d(d);
		v2bis.scale(l);
		m[1].sub(v2bis);

		//-n*L-l*D
		m[2] = new Vector3d(left);
		m[2].scale(n);
		m[2].negate();
		Vector3d v3bis = new Vector3d(d);
		v3bis.scale(l);
		m[2].sub(v3bis);

		//+n*U-mu*D
		m[3] = new Vector3d(up);
		m[3].scale(n);
		Vector3d v4bis = new Vector3d(d);
		v4bis.scale(mu);
		m[3].sub(v4bis);

		//-n*U-mu*D
		m[4] = new Vector3d(up);
		m[4].scale(n);
		m[4].negate();
		Vector3d v5bis = new Vector3d(d);
		v5bis.scale(mu);
		m[4].sub(v5bis);

		//Ai
		m[5] = new Vector3d(axisOBB[0]);			
		m[6] = new Vector3d(axisOBB[1]);
		m[7] = new Vector3d(axisOBB[2]);

		//L*Ai
		m[8] = new Vector3d(); m[8].cross(left,axisOBB[0]);
		m[9] = new Vector3d(); m[9].cross(left,axisOBB[1]);
		m[10] = new Vector3d(); m[10].cross(left,axisOBB[2]);

		//U*Ai
		m[11] = new Vector3d(); m[11].cross(up,axisOBB[0]);
		m[12] = new Vector3d(); m[12].cross(up,axisOBB[1]);
		m[13] = new Vector3d(); m[13].cross(up,axisOBB[2]);

		// (+l*L + mu*U + nD) * Ai
		Vector3d coef = new Vector3d(left);
		coef.scale(l);
		Vector3d coefBis = new Vector3d(up);
		coefBis.scale(mu);
		Vector3d coefThird = new Vector3d(d);
		coefThird.scale(n);			
		coef.add(coefBis);
		coef.add(coefThird);

		m[14] = new Vector3d(); m[14].cross(coef,axisOBB[0]);
		m[15] = new Vector3d(); m[15].cross(coef,axisOBB[1]);
		m[16] = new Vector3d(); m[16].cross(coef,axisOBB[2]);

		// (-l*L + mu*U + nD) * Ai
		coef = new Vector3d(left);
		coef.scale(l);
		coef.negate();
		coefBis = new Vector3d(up);
		coefBis.scale(mu);
		coefThird = new Vector3d(d);
		coefThird.scale(n);			
		coef.add(coefBis);
		coef.add(coefThird);

		m[17] = new Vector3d(); m[17].cross(coef,axisOBB[0]);
		m[18] = new Vector3d(); m[18].cross(coef,axisOBB[1]);
		m[19] = new Vector3d(); m[19].cross(coef,axisOBB[2]);

		// (+l*L - mu*U + nD) * Ai
		coef = new Vector3d(left);
		coef.scale(l);
		coefBis = new Vector3d(up);
		coefBis.scale(mu);
		coefBis.negate();
		coefThird = new Vector3d(d);
		coefThird.scale(n);			
		coef.add(coefBis);
		coef.add(coefThird);

		m[20] = new Vector3d(); m[20].cross(coef,axisOBB[0]);
		m[21] = new Vector3d(); m[21].cross(coef,axisOBB[1]);
		m[22] = new Vector3d(); m[22].cross(coef,axisOBB[2]);

		// (-l*L - mu*U + nD) * Ai
		coef = new Vector3d(left);
		coef.scale(l);
		coef.negate();
		coefBis = new Vector3d(up);
		coefBis.scale(mu);
		coefBis.negate();
		coefThird = new Vector3d(d);
		coefThird.scale(n);			
		coef.add(coefBis);
		coef.add(coefThird);

		m[23] = new Vector3d(); m[23].cross(coef,axisOBB[0]);
		m[24] = new Vector3d(); m[24].cross(coef,axisOBB[1]);
		m[25] = new Vector3d(); m[25].cross(coef,axisOBB[2]);			

		//************************************************************************************
		//Computation of d, R, m0, m1 for the 26 cases

		double[] dd = new double[26];
		//d = M.(C-E)
		Vector3d CminusE = new Vector3d(centerOBB);
		CminusE.sub(getEye());

		//R = sum(Ei*|m.Ai|,0,2)
		double[] R = new double[26];

		//p=l*|m.Left|+mu*|m.Up|
		double[] p = new double[26];
		double[] M0 = new double[26];
		double[] M1 = new double[26];

		double farDivNear = this.getFarDistance()/this.getNearDistance();
		Vector3d tempVec;
		double dotProd, tmp;
		for(int i =0; i<26; ++i) {
			dd[i] = m[i].dot(CminusE);
			R[i] = extentOBB[0] * Math.abs(m[i].dot(axisOBB[0])) + extentOBB[1] * Math.abs(m[i].dot(axisOBB[1])) + extentOBB[2] * Math.abs(m[i].dot(axisOBB[2]));
			p[i] = (l*Math.abs(m[i].dot(left))) + (mu*Math.abs(m[i].dot(up)));

			//tempVec = nm.d
			tempVec = new Vector3d(m[i]);
			tempVec.scale(n);
			dotProd = tempVec.dot(d);
			tmp = dotProd - p[i];
			if (tmp < 0) {
				M0[i] = farDivNear*(tmp);
			} else {
				M0[i] = tmp;
			}

			tmp = dotProd + p[i];
			if (tmp > 0) {
				M1[i] = farDivNear*(tmp);
			} else {
				M1[i] = tmp;
			}	

			//Intersection between OBB and Frustum intervals
			if (((dd[i]+R[i] < M0[i]) || (dd[i]-R[i] < M1[i]))) {
				return IntersectionType.OUTSIDE;
			}
		}
		//If no intersection are found between interval it means that the OBB and Frustum do not intersect 
		return IntersectionType.SPANNING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d centerOBB, Vector3d[] axisOBB, double[] extentOBB) {
		//Computation of Frustum characteristics
		AxisAngle4d view = this.getViewDirection();
		Vector3d d = new Vector3d(view.x,view.y,view.z);
		Vector3d up = this.planes[TOP].getNormal();
		GeometryUtil.perpendicularize(d, up);
		Vector3d left = MathUtil.crossProductRightHand(d.x, d.y, d.z, up.x, up.y, up.z);					
		double n = this.getNearDistance();
		double l = this.cachedPoints.get()[POINT_INDEX_BACK_LEFT_BOTTOM].distance(this.cachedPoints.get()[POINT_INDEX_BACK_RIGHT_BOTTOM])/2;//demi extent en largeur sur le near
		double mu = this.cachedPoints.get()[POINT_INDEX_BACK_LEFT_BOTTOM].distance(this.cachedPoints.get()[POINT_INDEX_BACK_LEFT_TOP])/2;// demi extent en hauteur sur le near

		//************************************************************************************
		//Computation of the 26 vectors to test
		Vector3d[] m  = new Vector3d[26];

		//d	
		m[1] = new Vector3d(d);

		//+n*L-l*D
		m[2] = new Vector3d(left);
		m[2].scale(n);
		Vector3d v2bis = new Vector3d(d);
		v2bis.scale(l);
		m[2].sub(v2bis);

		//-n*L-l*D
		m[3] = new Vector3d(left);
		m[3].scale(n);
		m[3].negate();
		Vector3d v3bis = new Vector3d(d);
		v3bis.scale(l);
		m[3].sub(v3bis);

		//+n*U-mu*D
		m[4] = new Vector3d(up);
		m[4].scale(n);
		Vector3d v4bis = new Vector3d(d);
		v4bis.scale(mu);
		m[4].sub(v4bis);

		//-n*U-mu*D
		m[5] = new Vector3d(up);
		m[5].scale(n);
		m[5].negate();
		Vector3d v5bis = new Vector3d(d);
		v5bis.scale(mu);
		m[5].sub(v5bis);

		//Ai
		m[6] = new Vector3d(axisOBB[0]);			
		m[7] = new Vector3d(axisOBB[1]);
		m[8] = new Vector3d(axisOBB[2]);

		//L*Ai
		m[9] = new Vector3d(); m[9].cross(left,axisOBB[0]);
		m[10] = new Vector3d(); m[10].cross(left,axisOBB[1]);
		m[11] = new Vector3d(); m[11].cross(left,axisOBB[2]);

		//U*Ai
		m[12] = new Vector3d(); m[12].cross(up,axisOBB[0]);
		m[13] = new Vector3d(); m[13].cross(up,axisOBB[1]);
		m[14] = new Vector3d(); m[14].cross(up,axisOBB[2]);

		// (+l*L + mu*U + nD) * Ai
		Vector3d coef = new Vector3d(left);
		coef.scale(l);
		Vector3d coefBis = new Vector3d(up);
		coefBis.scale(mu);
		Vector3d coefThird = new Vector3d(d);
		coefThird.scale(n);			
		coef.add(coefBis);
		coef.add(coefThird);

		m[15] = new Vector3d(); m[15].cross(coef,axisOBB[0]);
		m[16] = new Vector3d(); m[16].cross(coef,axisOBB[1]);
		m[17] = new Vector3d(); m[17].cross(coef,axisOBB[2]);

		// (-l*L + mu*U + nD) * Ai
		coef = new Vector3d(left);
		coef.scale(l);
		coef.negate();
		coefBis = new Vector3d(up);
		coefBis.scale(mu);
		coefThird = new Vector3d(d);
		coefThird.scale(n);			
		coef.add(coefBis);
		coef.add(coefThird);

		m[18] = new Vector3d(); m[18].cross(coef,axisOBB[0]);
		m[19] = new Vector3d(); m[19].cross(coef,axisOBB[1]);
		m[20] = new Vector3d(); m[20].cross(coef,axisOBB[2]);

		// (+l*L - mu*U + nD) * Ai
		coef = new Vector3d(left);
		coef.scale(l);
		coefBis = new Vector3d(up);
		coefBis.scale(mu);
		coefBis.negate();
		coefThird = new Vector3d(d);
		coefThird.scale(n);			
		coef.add(coefBis);
		coef.add(coefThird);

		m[21] = new Vector3d(); m[21].cross(coef,axisOBB[0]);
		m[22] = new Vector3d(); m[22].cross(coef,axisOBB[1]);
		m[23] = new Vector3d(); m[23].cross(coef,axisOBB[2]);

		// (-l*L - mu*U + nD) * Ai
		coef = new Vector3d(left);
		coef.scale(l);
		coef.negate();
		coefBis = new Vector3d(up);
		coefBis.scale(mu);
		coefBis.negate();
		coefThird = new Vector3d(d);
		coefThird.scale(n);			
		coef.add(coefBis);
		coef.add(coefThird);

		m[24] = new Vector3d(); m[24].cross(coef,axisOBB[0]);
		m[25] = new Vector3d(); m[25].cross(coef,axisOBB[1]);
		m[26] = new Vector3d(); m[26].cross(coef,axisOBB[2]);			

		//************************************************************************************
		//Computation of d, R, m0, m1 for the 26 cases

		double[] dd = new double[26];
		//d = M.(C-E)
		Vector3d CminusE = new Vector3d(centerOBB);
		CminusE.sub(getEye());

		//R = sum(Ei*|m.Ai|,0,2)
		double[] R = new double[26];

		//p=l*|m.Left|+mu*|m.Up|
		double[] p = new double[26];
		double[] M0 = new double[26];
		double[] M1 = new double[26];

		double farDivNear = this.getFarDistance()/this.getNearDistance();
		Vector3d tempVec;
		double dotProd, tmp;
		for(int i =0; i<26; ++i) {
			dd[i] = m[i].dot(CminusE);
			R[i] = extentOBB[0] * Math.abs(m[i].dot(axisOBB[0])) + extentOBB[1] * Math.abs(m[i].dot(axisOBB[1])) + extentOBB[2] * Math.abs(m[i].dot(axisOBB[2]));
			p[i] = (l*Math.abs(m[i].dot(left))) + (mu*Math.abs(m[i].dot(up)));

			//tempVec = nm.d
			tempVec = new Vector3d(m[i]);
			tempVec.scale(n);
			dotProd = tempVec.dot(d);
			tmp = dotProd - p[i];
			if (tmp < 0) {
				M0[i] = farDivNear*(tmp);
			} else {
				M0[i] = tmp;
			}

			tmp = dotProd + p[i];
			if (tmp > 0) {
				M1[i] = farDivNear*(tmp);
			} else {
				M1[i] = tmp;
			}	

			//Intersection between OBB and Frustum intervals
			if (((dd[i]+R[i] < M0[i]) || (dd[i]-R[i] < M1[i]))) {
				return true;
			}
		}
		//If no intersection are found between interval it means that the OBB and Frustum do not intersect 
		return false;
	}

	/**
	 * Replies the aligned bounding box that is enclosing this frustum.
	 * 
	 * @return the aligned bounding box that is enclosing this frustum.
	 */
	public AlignedBoundingBox toBoundingBox() {
		AlignedBoundingBox box = null;
		if (this.aabb!=null) box = this.aabb.get();
		if (box==null) {
			box = new AlignedBoundingBox();
			box.combine(getPoints());
			this.aabb = new SoftReference<AlignedBoundingBox>(box);
		}
		return box;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D toBounds2D() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D toBounds2D(CoordinateSystem3D system) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d lower, Point3d upper) {			
		if (toBoundingBox().classifies(lower, upper)==IntersectionType.OUTSIDE) return IntersectionType.OUTSIDE;

		int count = 0;

		for (int i = 0; i < 6; ++i) {
			switch(this.planes[i].classifies(lower.x, lower.y, lower.z, upper.x, upper.y, upper.z)) {
			case BEHIND:
				return IntersectionType.OUTSIDE;
			case IN_FRONT_OF:
				++count;
				break;
			case COINCIDENT:
			default:
				break;
			}
		}

		if (count==6)  return IntersectionType.INSIDE;

		for(Point3d pts : getPoints()) {
			if ((lower.x>pts.x)||(pts.x>upper.x)
					||
					(lower.y>pts.y)||(pts.y>upper.y)
					||
					(lower.z>pts.z)||(pts.x>upper.z)) {
				return IntersectionType.SPANNING;
			}
		}

		return IntersectionType.ENCLOSING;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d lower, Point3d upper) {			
		if (!toBoundingBox().intersects(lower, upper)) return false;

		for (int i = 0; i < 6; ++i) {
			if (this.planes[i].classifies(lower.x, lower.y, lower.z, upper.x, upper.y, upper.z)==
				PlanarClassificationType.BEHIND)
				return false;
		}

		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d point) {
		boolean coincident = false;
		for (int p = 0; p < 6; ++p) {
			switch(this.planes[p].classifies(point)) {
			case BEHIND:
				return IntersectionType.OUTSIDE;
			case COINCIDENT:
				coincident = true;
				break;
			default:
				//
			}
		}
		return (coincident) ? IntersectionType.SPANNING : IntersectionType.INSIDE;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d point) {
		for (int p = 0; p < 6; ++p) {
			if (this.planes[p].classifies(point)==PlanarClassificationType.BEHIND)
				return false;
		}
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d center, double radius) {
		int count = 0;

		for (int i = 0; i < 6; ++i) {
			double d = this.planes[i].distanceTo(center);
			if (d >= radius)
				++count;
			else if (d <= -radius)
				return IntersectionType.OUTSIDE;
		}

		if (count==0) return IntersectionType.ENCLOSING;
		return (count == 6) ? IntersectionType.INSIDE : IntersectionType.SPANNING;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d center, double radius) {
		for (int i = 0; i < 6; ++i) {
			double d = this.planes[i].distanceTo(center);
			if (d <= -radius) return false;
		}
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Plane plane) {
		throw new UnsupportedOperationException();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean intersects(Plane plane) {
		throw new UnsupportedOperationException();
	}

	/** Replies the orientation of the frustum.
	 * The orientation of the frustum is the direction at which 
	 * the eye is looking and the rotation angle around this
	 * view axis.
	 * 
	 * @return the orientation.
	 */
	public final Direction3D getViewDirection() {
		Direction3D orientation = this.cachedOrientation==null ? null : this.cachedOrientation.get();
		if (orientation!=null) return orientation;

		// Compute the view direction
		Point3d eye = getEye();
		Point3d projectionFar = GeometryUtil.projectsPointOnPlaneIn3d(eye, this.planes[BACK]);
		Vector3d viewVector = new Vector3d(
				projectionFar.x - eye.x,
				projectionFar.y - eye.y,
				projectionFar.z - eye.z);
		viewVector.normalize();

		// Compute the rolling angle
		CoordinateSystem3D system = CoordinateSystem3D.getDefaultCoordinateSystem();
		Vector3d noneUp = GeometryUtil.getUpVectorForView(viewVector.x,viewVector.y,viewVector.z, system);
		Vector3d currentUp = this.planes[BOTTOM].getNormal();
		GeometryUtil.perpendicularize(viewVector, currentUp);

		double angle = GeometryUtil.signedAngle(
				noneUp.x,noneUp.y,noneUp.z,
				currentUp.x,currentUp.y,currentUp.z);

		orientation = new Direction3D(viewVector, angle);
		this.cachedOrientation = new SoftReference<Direction3D>(orientation);
		return orientation;
	}

	/** Replies the plane at the specified index.
	 * 
	 * @param planeIndex is the index of the plane
	 * @return the plane at the specified index
	 * @throws IndexOutOfBoundsException if the given index is outside the scope.
	 */
	public Plane4d getPlane(int planeIndex) {
		return this.planes[planeIndex];
	}

	/** Replies the planes of this frustum.
	 * 
	 * @return the planes
	 */
	public Plane4d[] getPlanes() {
		return this.planes;
	}

	/** Replies the point at the specified index.
	 * 
	 * @param pointIndex is the index of the point
	 * @return the point at the specified index
	 * @throws IndexOutOfBoundsException if the given index is outside the scope.
	 */
	public Point3d getPoint(int pointIndex) {
		Point3d[] pts = getPoints();
		return pts[pointIndex];
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append("Frustum[\n\tLeft:"); //$NON-NLS-1$
		sb.append(this.planes[LEFT]);
		sb.append("\n\tRight:"); //$NON-NLS-1$
		sb.append(this.planes[RIGHT]);
		sb.append("\n\tBottom:"); //$NON-NLS-1$
		sb.append(this.planes[BOTTOM]);
		sb.append("\n\tTop:"); //$NON-NLS-1$
		sb.append(this.planes[TOP]);
		sb.append("\n\tFar:"); //$NON-NLS-1$
		sb.append(this.planes[BACK]);
		sb.append("\n\tNear:"); //$NON-NLS-1$
		sb.append(this.planes[FRONT]);
		sb.append(']');
		return sb.toString();
	}

}

