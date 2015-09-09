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
package org.arakhne.afc.math.geometry.d3.continuous;

import java.lang.ref.SoftReference;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.util.Pair;


/**
 * A triangle in space. It is defined by three points.
 * <p>
 * A triangle is transformable. So it has a position given
 * by its first point, an orientation given its normal
 * and no scale factor.
 * 
 * @author $Author: olamotte$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Triangle3f extends AbstractShape3f<Triangle3f> {

	private static final long serialVersionUID = 566634934114601285L;

	private static boolean mollerAlgorithmAxisTestX01(double a, double b, double fa, double fb,
			double v0y, double v0z, double v2y, double v2z, double halfsizey, double halfsizez) {
		double p0 = a * v0y - b * v0z;
		double p2 = a*v2y - b*v2z;
		double min, max;
		if (p0 < p2) {
			min = p0;
			max = p2;
		} else {
			min = p2;
			max = p0;
		}
		double rad = fa * halfsizey + fb * halfsizez;
		return (min > rad || max < -rad);
	}

	private static boolean mollerAlgorithmAxisTestX02(double a, double b, double fa, double fb,
			double v0y, double v0z, double v1y, double v1z, double halfsizey, double halfsizez) {
		double p0 = a * v0y - b * v0z;
		double p1 = a * v1y - b * v1z;
		double min, max;
		if (p0 < p1) {
			min = p0;
			max = p1;
		} else {
			min = p1;
			max = p0;
		}
		double rad = fa * halfsizey + fb * halfsizez;
		return (min > rad || max < -rad);
	}

	private static boolean mollerAlgorithmAxisTestY02(double a, double b, double fa, double fb,
			double v0x, double v0z, double v2x, double v2z, double halfsizex, double halfsizez) {
		double p0 = - a *v0x + b * v0z;
		double p2 = -a * v2x + b * v2z;
		double min, max;
		if (p0 < p2) {
			min = p0;
			max = p2;
		} else {
			min = p2;
			max = p0;
		}
		double rad = fa * halfsizex + fb * halfsizez;
		return (min > rad || max < -rad);
	}

	private static boolean mollerAlgorithmAxisTestY01(double a, double b, double fa, double fb,
			double v0x, double v0z, double v1x, double v1z, double halfsizex, double halfsizez) {
		double p0 = -a * v0x + b * v0z;
		double p1 = -a * v1x + b * v1z;
		double min, max;
		if (p0 < p1) {
			min = p0;
			max = p1;
		} else {
			min = p1;
			max = p0;
		}
		double rad = fa * halfsizex + fb * halfsizez;
		return (min > rad || max < -rad);
	}

	private static boolean mollerAlgorithmAxisTestZ12(double a, double b, double fa, double fb,
			double v1x, double v1y, double v2x, double v2y, double halfsizex, double halfsizey) {
		double p1 = a * v1x - b * v1y;
		double p2 = a * v2x - b * v2y;
		double min, max;
		if (p2 < p1) {
			min = p2;
			max = p1;
		} else {
			min = p1;
			max = p2;
		}
		double rad = fa * halfsizex + fb * halfsizey;
		return (min > rad || max < -rad);
	}

	private static boolean mollerAlgorithmAxisTestZ0(double a, double b, double fa, double fb,
			double v0x, double v0y, double v1x, double v1y, double halfsizex, double halfsizey) {
		double p0 = a * v0x - b * v0y;
		double p1 = a * v1x - b * v1y;
		double min, max;
		if (p0 < p1) {
			min = p0;
			max = p1;
		} else {
			min = p1;
			max = p0;
		}
		double rad = fa * halfsizex + fb * halfsizey;
		return (min > rad || max < -rad);
	}

	private static boolean mollerAlgorithmPlaneBoxOverlap(
			double normalx, double normaly, double normalz,
			double vertx, double verty, double vertz,
			double maxboxx, double maxboxy, double maxboxz) {
		double vminx, vmaxx, vminy, vmaxy, vminz, vmaxz;

		if (normalx > 0.) {
			vminx = -maxboxx - vertx;
			vmaxx = maxboxx - vertx;
		} else {
			vminx = maxboxx - vertx;
			vmaxx = -maxboxx - vertx;
		}

		if (normaly > 0.) {
			vminy = -maxboxy - verty;
			vmaxy = maxboxy - verty;
		} else {
			vminy = maxboxy - verty;
			vmaxy = -maxboxy - verty;
		}

		if (normalz > 0.) {
			vminz = -maxboxz - vertz;
			vmaxz = maxboxz - vertz;
		} else {
			vminz = maxboxz - vertz;
			vmaxz = -maxboxz - vertz;
		}

		if (Vector3f.dotProduct(normalx, normaly, normalz, vminx, vminy, vminz) > 0.) {
			return false;
		}

		if (Vector3f.dotProduct(normalx, normaly, normalz, vmaxx, vmaxy, vmaxz) > 0.) {
			return true;
		}

		return false;
	}

	/** Replies if the triangle intersects the aligned box.
	 * 
	 * <a href="./doc-files/aabb_triangle_intersection.pdf"> Tomas Akenine-Moller.
	 * "Fast 3D Triangle-Box Overlap Testing". Journal of Graphics Tools 6, pp. 29-33. 2001.</a>
	 * 
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param minx x coordinate of the lower corner of the aligned box.
	 * @param miny y coordinate of the lower corner of the aligned box.
	 * @param minz z coordinate of the lower corner of the aligned box.
	 * @param maxx x coordinate of the upper corner of the aligned box.
	 * @param maxy y coordinate of the upper corner of the aligned box.
	 * @param maxz z coordinate of the upper corner of the aligned box.
	 * @return <code>true</code> if the triangle and aligned box are intersecting.
	 */
	public static boolean intersectsTriangleAlignedBox(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double minx, double miny, double minz, 
			double maxx, double maxy, double maxz) {

		// use separating axis theorem to test overlap between triangle and box
		// need to test for overlap in these directions:
		// 1) the {x,y,z}-directions (actually, since we use the AABB of the triangle
		//    we do not even need to test these)
		// 2) normal of the triangle
		// 3) crossproduct (edge from tri, {x,y,z}-directin)
		//    this gives 3x3=9 more tests

		double halfsizex = (maxx - minx) / 2.;
		double halfsizey = (maxy - miny) / 2.;
		double halfsizez = (maxz - minz) / 2.;

		double boxcenterx = minx + halfsizex;
		double boxcentery = miny + halfsizey;
		double boxcenterz = minz + halfsizez;

		// This is the fastest branch on Sun
		// move everything so that the boxcenter is in (0,0,0)
		double v0x = tx1 - boxcenterx;
		double v0y = ty1 - boxcentery;
		double v0z = tz1 - boxcenterz;
		double v1x = tx2 - boxcenterx;
		double v1y = ty2 - boxcentery;
		double v1z = tz2 - boxcenterz;
		double v2x = tx3 - boxcenterx;
		double v2y = ty3 - boxcentery;
		double v2z = tz3 - boxcenterz;

		// compute triangle edges
		double e0x = v1x - v0x;
		double e0y = v1y - v0y;
		double e0z = v1z - v0z;
		double e1x = v2x - v1x;
		double e1y = v2y - v1y;
		double e1z = v2z - v1z;
		double e2x = v0x - v2x;
		double e2y = v0y - v2y;
		double e2z = v0z - v2z;


		// Bullet 3:
		// test the 9 tests first (this was faster)
		double fex = Math.abs(e0x);
		double fey = Math.abs(e0y);
		double fez = Math.abs(e0z);

		//AXISTEST_X01(e0[Z], e0[Y], fez, fey);
		if (mollerAlgorithmAxisTestX01(
				e0z, e0y, fez, fey,
				v0y, v0z, v2y, v2z, halfsizey, halfsizez)) {
			return false;
		}

		//AXISTEST_Y02(e0[Z], e0[X], fez, fex);
		if (mollerAlgorithmAxisTestY02(
				e0z, e0x, fez, fex,
				v0x, v0z, v2x, v2z, halfsizex, halfsizez)) {
			return false;
		}

		//AXISTEST_Z12(e0[Y], e0[X], fey, fex);
		if (mollerAlgorithmAxisTestZ12(
				e0y, e0x, fey, fex,
				v1x, v1y, v2x, v2y, halfsizex, halfsizey)) {
			return false;
		}


		fex = Math.abs(e1x);
		fey = Math.abs(e1y);
		fez = Math.abs(e1z);

		//AXISTEST_X01(e1[Z], e1[Y], fez, fey);
		if (mollerAlgorithmAxisTestX01(
				e1z, e1y, fez, fey,
				v0y, v0z, v2y, v2z, halfsizey, halfsizez)) {
			return false;
		}

		//AXISTEST_Y02(e1[Z], e1[X], fez, fex);
		if (mollerAlgorithmAxisTestY02(
				e1z, e1x, fez, fex,
				v0y, v0z, v2y, v2z, halfsizey, halfsizez)) {
			return false;
		}

		//AXISTEST_Z0(e1[Y], e1[X], fey, fex);
		if (mollerAlgorithmAxisTestZ0(
				e1y, e1x, fey, fex,
				v0x, v0y, v1x, v1y, halfsizex, halfsizey)) {
			return false;
		}

		fex = Math.abs(e2x);
		fey = Math.abs(e2y);
		fez = Math.abs(e2z);

		//AXISTEST_X2(e2[Z], e2[Y], fez, fey);
		if (mollerAlgorithmAxisTestX02(
				e2z, e2y, fez, fey,
				v0y, v0z, v1y, v1z, halfsizey, halfsizez)) {
			return false;
		}

		//AXISTEST_Y1(e2[Z], e2[X], fez, fex);
		if (mollerAlgorithmAxisTestY01(
				e2z, e2x, fez, fex,
				v0x, v0z, v1x, v1z, halfsizex, halfsizez)) {
			return false;
		}

		//AXISTEST_Z12(e2[Y], e2[X], fey, fex);
		if (mollerAlgorithmAxisTestZ12(
				e2y, e2x, fey, fex,
				v1x, v1y, v2x, v2y, halfsizex, halfsizey)) {
			return false;
		}

		// Bullet 1:
		// first test overlap in the {x,y,z}-directions
		// find min, max of the triangle each direction, and test for overlap in
		// that direction -- this is equivalent to testing a minimal AABB around
		// the triangle against the AABB

		double min, max;

		// test in X-direction
		min = MathUtil.min(v0x, v1x, v2x);
		max = MathUtil.max(v0x, v1x, v2x);
		if (min > halfsizex || max < -halfsizex) {
			return false;
		}

		// test in Y-direction
		min = MathUtil.min(v0y, v1y, v2y);
		max = MathUtil.max(v0y, v1y, v2y);
		if (min > halfsizey || max < -halfsizey) {
			return false;
		}

		// test in Z-direction
		min = MathUtil.min(v0z, v1z, v2z);
		max = MathUtil.max(v0z, v1z, v2z);
		if (min > halfsizez || max < -halfsizez) {
			return false;
		}

		// Bullet 2:
		// test if the box intersects the plane of the triangle
		// compute plane equation of triangle: normal*x+d=0

		double normalx = v1y * v2z - v1z * v2y;
		double normaly = v1z * v2x - v1x * v2z;
		double normalz = v1x * v2y - v1y * v2x;

		return mollerAlgorithmPlaneBoxOverlap(
				normalx, normaly, normalz,
				v0x, v0y, v0z,
				halfsizex, halfsizey, halfsizez);
	}

	/** Compute the plane of the given triangle.
	 * 
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @return the plane.
	 */
	protected static Plane3D<?> toPlane(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3) {
		Vector3f norm = new Vector3f();
		Vector3f.crossProduct(
				tx2 - tx1,
				ty2 - ty1,
				tz2 - tz1,
				tx3 - tx1,
				ty3 - ty1,
				tz3 - tz1,
				norm);
		assert(norm!=null);
		if (norm.getY()==0. && norm.getZ()==0.)
			return new PlaneYZ4f(tx1);
		if (norm.getX()==0. && norm.getZ()==0.)
			return new PlaneXZ4f(ty1);
		if (norm.getX()==0. && norm.getY()==0.)
			return new PlaneXY4f(tz1);
		return new Plane4f(tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3);
	}

	/** Replies the closest point from the triangle to the point.
	 * 
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param px x coordinate of the point.
	 * @param py y coordinate of the point.
	 * @param pz z coordinate of the point.
	 * @param closestPoint the point set with the closest coordinates.
	 * @see "https://github.com/juj/MathGeoLib"
	 */
	@Unefficient
	public static void computeClosestPointTrianglePoint(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double px, double py, double pz,
			Point3D closestPoint) {
		if (containsTrianglePoint(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				px, py, pz,
				false, MathConstants.EPSILON)) {
			closestPoint.set(toPlane(
					tx1, ty1, tz1,
					tx2, ty2, tz2,
					tx3, ty3, tz3).getProjection(px, py, pz));
			return;
		}

		double f;

		f = Segment3f.getPointProjectionFactorOnSegmentLine(
				px, py, pz,
				tx1, ty1, tz1,
				tx2, ty2, tz2);
		double p1x = tx1 + f * (tx2 - tx1);
		double p1y = ty1 + f * (ty2 - ty1);
		double p1z = tz1 + f * (tz2 - tz1); 

		f = Segment3f.getPointProjectionFactorOnSegmentLine(
				px, py, pz,
				tx2, ty2, tz2,
				tx3, ty3, tz3);
		double p2x = tx2 + f * (tx3 - tx2);
		double p2y = ty2 + f * (ty3 - ty2);
		double p2z = tz2 + f * (tz3 - tz2); 

		f = Segment3f.getPointProjectionFactorOnSegmentLine(
				px, py, pz,
				tx3, ty3, tz3,
				tx1, ty1, tz1);
		double p3x = tx3 + f * (tx1 - tx3);
		double p3y = ty3 + f * (ty1 - ty3);
		double p3z = tz3 + f * (tz1 - tz3); 

		double d1 = Point3f.distanceSquaredPointPoint(px, py, pz, p1x, p1y, p1z);
		double d2 = Point3f.distanceSquaredPointPoint(px, py, pz, p2x, p2y, p3z);
		double d3 = Point3f.distanceSquaredPointPoint(px, py, pz, p3x, p3y, p3z);

		if (d1 <= d2) {
			if (d1 <= d3) {
				closestPoint.set(p1x, p1y, p1z);
			} else {
				closestPoint.set(p3x, p3y, p3z);
			}
		} if (d2 <= d3) {
			closestPoint.set(p2x, p2y, p2z);
		} else {
			closestPoint.set(p3x, p3y, p3z);
		}
	}

	/** Replies the squared distance from the triangle to the segment.
	 * 
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @return the squared distance.
	 * @see "https://github.com/juj/MathGeoLib"
	 */
	@Unefficient
	public static double distanceSquaredTriangleSegment(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2) {
		double t = getTriangleSegmentIntersectionFactorWithJimenezAlgorithm(
				tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, sx1, sy1, sz1, sx2, sy2, sz2);
		if (Double.isInfinite(t) || (!Double.isNaN(t) && t >= 0. && t <= 1.)) {
			return 0.;
		}

		double d1 = Segment3f.distanceSquaredSegmentSegment(
				tx1, ty1, tz1, tx2, ty2, tz2,
				sx1, sy1, sz1, sx2, sy2, sz2);
		double d2 = Segment3f.distanceSquaredSegmentSegment(
				tx1, ty1, tz1, tx3, ty3, tz3,
				sx1, sy1, sz1, sx2, sy2, sz2);
		double d3 = Segment3f.distanceSquaredSegmentSegment(
				tx2, ty2, tz2, tx3, ty3, tz3,
				sx1, sy1, sz1, sx2, sy2, sz2);

		return MathUtil.min(d1, d2, d3);
	}

	/** Replies if the triangle intersects the capsule.
	 * 
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param cx1 x coordinate of the first point of the capsule medial.
	 * @param cy1 y coordinate of the first point of the capsule medial.
	 * @param cz1 z coordinate of the first point of the capsule medial.
	 * @param cx2 x coordinate of the second point of the capsule medial.
	 * @param cy2 y coordinate of the second point of the capsule medial.
	 * @param cz2 z coordinate of the second point of the capsule medial.
	 * @param radius radius of the capsule.
	 * @return <code>true</code> if the triangle and capsule are intersecting.
	 * @see "https://github.com/juj/MathGeoLib"
	 */
	@Unefficient
	public static boolean intersectsTriangleCapsule(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double cx1, double cy1, double cz1,
			double cx2, double cy2, double cz2,
			double radius) {
		double d = distanceSquaredTriangleSegment(
				tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
				cx1, cy1, cz1, cx2, cy2, cz2);
		return d < (radius * radius);
	}

	/** Replies if two coplanar triangles intersect.
	 * Triangles intersect even if they are connected by two of their
	 * edges.
	 * <p>
	 * Triangle/triangle intersection test routine,
	 * by Tomas Moller, 1997.
	 * See article "A Fast Triangle-Triangle Intersection Test",
	 * Journal of Graphics Tools, 2(2), 1997.
	 * 
	 * @param v1x x coordinate of the first point of the first triangle.
	 * @param v1y y coordinate of the first point of the first triangle.
	 * @param v1z z coordinate of the first point of the first triangle.
	 * @param v2x x coordinate of the second point of the first triangle.
	 * @param v2y y coordinate of the second point of the first triangle.
	 * @param v2z z coordinate of the second point of the first triangle.
	 * @param v3x x coordinate of the third point of the first triangle.
	 * @param v3y y coordinate of the third point of the first triangle.
	 * @param v3z z coordinate of the third point of the first triangle.
	 * @param u1x x coordinate of the first point of the second triangle.
	 * @param u1y y coordinate of the first point of the second triangle.
	 * @param u1z z coordinate of the first point of the second triangle.
	 * @param u2x x coordinate of the second point of the second triangle.
	 * @param u2y y coordinate of the second point of the second triangle.
	 * @param u2z z coordinate of the second point of the second triangle.
	 * @param u3x x coordinate of the third point of the second triangle.
	 * @param u3y y coordinate of the third point of the second triangle.
	 * @param u3z z coordinate of the third point of the second triangle.
	 * @return <code>true</code> if the two triangles are intersecting.
	 */
	public static boolean intersectsCoplanarTriangleTriangle(
			double v1x, double v1y, double v1z,
			double v2x, double v2y, double v2z,
			double v3x, double v3y, double v3z,
			double u1x, double u1y, double u1z, 
			double u2x, double u2y, double u2z,
			double u3x, double u3y, double u3z) {

		int i0, i1;

		// first project onto an axis-aligned plane, that maximizes the area
		// of the triangles, compute indices: i0,i1.
		{
			double nx = v1y * (v2z - v3z) + v2y * (v3z - v1z) + v3y * (v1z - v2z);
			double ny = v1z * (v2x - v3x) + v2z * (v3x - v1x) + v3z * (v1x - v2x);
			double nz = v1x * (v2y - v3y) + v2x * (v3y - v1y) + v3x * (v1y - v2y);

			nx = (nx<0) ? -nx : nx;
			ny = (ny<0) ? -ny : ny;
			nz = (nz<0) ? -nz : nz;

			if(nx>ny) {
				if(nx>nz) {
					// nx is greatest
					i0 = 1;
					i1 = 2;
				}
				else {
					// nz is greatest
					i0 = 0;
					i1 = 1;
				}
			}
			else {   /* nx<=ny */
				if(nz>ny) {
					// nz is greatest
					i0 = 0;
					i1 = 1;                                           
				}
				else {
					// ny is greatest
					i0 = 0;
					i1 = 2;
				}
			}
		}

		double[] tv1 = new double[] {v1x,v1y,v1z};
		double[] tv2 = new double[] {v2x,v2y,v2z};
		double[] tv3 = new double[] {v3x,v3y,v3z};
		double[] tu1 = new double[] {u1x,u1y,u1z};
		double[] tu2 = new double[] {u2x,u2y,u2z};
		double[] tu3 = new double[] {u3x,u3y,u3z};

		// test all edges of triangle 1 against the edges of triangle 2
		if (intersectsCoplanarTriangle(i0,i1,0,tv1,tv2,tu1,tu2,tu3)) return true;
		if (intersectsCoplanarTriangle(i0,i1,0,tv2,tv3,tu1,tu2,tu3)) return true;
		if (intersectsCoplanarTriangle(i0,i1,0,tv3,tv1,tu1,tu2,tu3)) return true;

		// finally, test if tri1 is totally contained in tri2 or vice versa
		if (Triangle3f.containsTrianglePoint(i0,i1,tv1,tu1,tu2,tu3)) return true;
		if (Triangle3f.containsTrianglePoint(i0,i1,tu1,tv1,tv2,tv3)) return true;

		return false;
	}

	/** Replies if the triangle intersects the sphere.
	 * 
	 * <a href="http://www.amazon.com/exec/obidos/tg/detail/-/1558607323">Christer Ericson.
	 * "Real-Time Collision Detection".
	 * The Morgan Kaufmann Series in Interactive 3-D Technology. ISBN 1558607323. 2004.</a> 
	 * 
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param centerx x coordinate of the center of the sphere.
	 * @param centery y coordinate of the center of the sphere.
	 * @param centerz z coordinate of the center of the sphere.
	 * @param radius the radius of the sphere.
	 * @return <code>true</code> if the triangle and sphere are intersecting.
	 * @see "http://realtimecollisiondetection.net/blog/?p=103"
	 */
	public static boolean intersectsTriangleSphere(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double centerx, double centery, double centerz, 
			double radius) {
		// A = A - P
		double ax = tx1 - centerx;
		double ay = ty1 - centery;
		double az = tz1 - centerz;
		// B = B - P
		double bx = tx2 - centerx;
		double by = ty2 - centery;
		double bz = tz2 - centerz;
		// C = C - P
		double cx = tx3 - centerx;
		double cy = ty3 - centery;
		double cz = tz3 - centerz;
		// rr = r * r
		double rr = radius * radius;
		// V = cross(B - A, C - A)
		Vector3f V = new Vector3f();
		Vector3f.crossProduct(bx - ax, by - ay, bz - az, cx - ax, cy - ay, cz - az, V);
		// d = dot(A, V)
		double d = Vector3f.dotProduct(ax, ay, az, V.getX(), V.getY(), V.getZ());
		// e = dot(V, V)
		double e = V.dot(V);
		// sep1 = d * d > rr * e
		if (d * d > rr * e) {
			return false;
		}
		// aa = dot(A, A)
		double aa = Vector3f.dotProduct(ax, ay, az, ax, ay, az);
		// ab = dot(A, B)
		double ab = Vector3f.dotProduct(ax, ay, az, bx, by, bz);
		// ac = dot(A, C)
		double ac = Vector3f.dotProduct(ax, ay, az, cx, cy, cz);
		// bb = dot(B, B)
		double bb = Vector3f.dotProduct(bx, by, bz, bx, by, bz);
		// bc = dot(B, C)
		double bc = Vector3f.dotProduct(bx, by, bz, cx, cy, cz);
		// cc = dot(C, C)
		double cc = Vector3f.dotProduct(cx, cy, cz, cx, cy, cz);
		// sep2 = (aa > rr) & (ab > aa) & (ac > aa)
		if ((aa > rr) && (ab > aa) && (ac > aa)) {
			return false;
		}
		// sep3 = (bb > rr) & (ab > bb) & (bc > bb)
		if ((bb > rr) && (ab > bb) && (bc > bb)) {
			return false;
		}
		// sep4 = (cc > rr) & (ac > cc) & (bc > cc)
		if ((cc > rr) && (ac > cc) && (bc > cc)) {
			return false;
		}

		// AB = B - A
		double abx = bx - ax;
		double aby = by - ay;
		double abz = bz - az;
		// d1 = ab - aa
		double d1 = ab - aa;
		// e1 = dot(AB, AB)
		double e1 = Vector3f.dotProduct(abx, aby, abz, abx, aby, abz);
		// Q1 = A * e1 - d1 * AB
		double q1x = ax * e1 - d1 * abx;
		double q1y = ay * e1 - d1 * aby;
		double q1z = az * e1 - d1 * abz;
		// QC = C * e1 - Q1
		double qcx = cx * e1 - q1x;
		double qcy = cy * e1 - q1y;
		double qcz = cz * e1 - q1z;
		// sep5 = [dot(Q1, Q1) > rr * e1 * e1] & [dot(Q1, QC) > 0]
		if ((Vector3f.dotProduct(q1x, q1y, q1z, q1x, q1y, q1z) > (rr * e1 * e1))
				&& (Vector3f.dotProduct(q1x, q1y, q1z, qcx, qcy, qcz) > 0.)) {
			return false;
		}

		// BC = C - B
		double bcx = cx - bx;
		double bcy = cy - by;
		double bcz = cz - bz;
		// d2 = bc - bb
		double d2 = bc - bb;
		// e2 = dot(BC, BC)
		double e2 = Vector3f.dotProduct(bcx, bcy, bcz, bcx, bcy, bcz);
		// Q2 = B * e2 - d2 * BC
		double q2x = bx * e2 - d2 * bcx;
		double q2y = by * e2 - d2 * bcy;
		double q2z = bz * e2 - d2 * bcz;
		// QA = A * e2 - Q2
		double qax = ax * e2 - q2x;
		double qay = ay * e2 - q2y;
		double qaz = az * e2 - q2z;
		// sep6 = [dot(Q2, Q2) > rr * e2 * e2] & [dot(Q2, QA) > 0]
		if ((Vector3f.dotProduct(q2x, q2y, q2z, q2x, q2y, q2z) > (rr * e2 * e2))
				&& (Vector3f.dotProduct(q1x, q2y, q2z, qax, qay, qaz) > 0.)) {
			return false;
		}

		// CA = A - C
		double cax = ax - cx;
		double cay = ay - cy;
		double caz = az - cz;
		// d3 = ac - cc
		double d3 = ac - cc;
		// e3 = dot(CA, CA)
		double e3 = Vector3f.dotProduct(cax, cay, caz, cax, cay, caz);
		// Q3 = C * e3 - d3 * CA
		double q3x = cx * e3 - d3 * cax;
		double q3y = cy * e3 - d3 * cay;
		double q3z = cz * e3 - d3 * caz;
		// QB = B * e3 - Q3
		double qbx = bx * e3 - q3x;
		double qby = by * e3 - q3y;
		double qbz = bz * e3 - q3z;
		// sep7 = [dot(Q3, Q3) > rr * e3 * e3] & [dot(Q3, QB) > 0]
		if ((Vector3f.dotProduct(q3x, q3y, q3z, q3x, q3y, q3z) > (rr * e3 * e3))
				&& (Vector3f.dotProduct(q3x, q3y, q3z, qbx, qby, qbz) > 0.)) {
			return false;
		}

		return true;
	}

	/** Replies if the triangle intersects the oriented box.
	 * 
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param cx x coordinate of the center of the oriented box.
	 * @param cy y coordinate of the center of the oriented box.
	 * @param cz z coordinate of the center of the oriented box.
	 * @param ax1 x coordinate of the first axis of the oriented box.
	 * @param ay1 y coordinate of the first axis of the oriented box.
	 * @param az1 z coordinate of the first axis of the oriented box.
	 * @param ax2 x coordinate of the second axis of the oriented box.
	 * @param ay2 y coordinate of the second axis of the oriented box.
	 * @param az2 z coordinate of the second axis of the oriented box.
	 * @param ax3 x coordinate of the third axis of the oriented box.
	 * @param ay3 y coordinate of the third axis of the oriented box.
	 * @param az3 z coordinate of the third axis of the oriented box.
	 * @param ae1 the extent of the first axis.
	 * @param ae2 the extent of the second axis.
	 * @param ae3 the extent of the third axis.
	 * @return <code>true</code> if the triangle and oriented box are intersecting.
	 */
	public static boolean intersectsTriangleOrientedBox(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double cx, double cy, double cz, 
			double ax1, double ay1, double az1,
			double ax2, double ay2, double az2,
			double ax3, double ay3, double az3,
			double ae1, double ae2, double ae3) {
		double nx, ny, nz;
		// Translate the triangle into the oriented box frame.
		nx = tx1 - cx;
		ny = ty1 - cy;
		nz = tz1 - cz;
		double ntx1 = Vector3f.dotProduct(nx, ny, nz, ax1, ay1, az1);
		double nty1 = Vector3f.dotProduct(nx, ny, nz, ax2, ay2, az2);
		double ntz1 = Vector3f.dotProduct(nx, ny, nz, ax3, ay3, az3);

		nx = tx2 - cx;
		ny = ty2 - cy;
		nz = tz2 - cz;
		double ntx2 = Vector3f.dotProduct(nx, ny, nz, ax1, ay1, az1);
		double nty2 = Vector3f.dotProduct(nx, ny, nz, ax2, ay2, az2);
		double ntz2 = Vector3f.dotProduct(nx, ny, nz, ax3, ay3, az3);

		nx = tx3 - cx;
		ny = ty3 - cy;
		nz = tz3 - cz;
		double ntx3 = Vector3f.dotProduct(nx, ny, nz, ax1, ay1, az1);
		double nty3 = Vector3f.dotProduct(nx, ny, nz, ax2, ay2, az2);
		double ntz3 = Vector3f.dotProduct(nx, ny, nz, ax3, ay3, az3);

		// Test intersection
		return intersectsTriangleAlignedBox(
				ntx1, nty1, ntz1, ntx2, nty2, ntz2, ntx3, nty3, ntz3,
				-ae1, -ae2, -ae3, ae1, ae2, ae3);
	}

	/** Replies if the triangle intersects the segment.
	 * 
	 * Source: <a href="./doc-files/segment_triangle_intersection.pdf">
	 * Juan J. Jimenez, Rafael J. Segura, Francisco R. Feito.
	 * "A robust segment/triangle intersection algorithm for interference tests. Efficiency study".
	 * Computational Geometry 43 (2010) pp 474-492. 2010.</a>
	 * 
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @return <code>true</code> if the triangle and segment are intersecting.
	 */
	public static boolean intersectsTriangleSegment(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2) {
		double factor = getTriangleSegmentIntersectionFactorWithJimenezAlgorithm(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				sx1, sy1, sz1,
				sx2, sy2, sz2);
		return !Double.isNaN(factor);
	}

	/** Replies the baricentric coordinates (in the triangle) of the
	 * intersection between a triangle and a segment.
	 * <p>
	 * If the segment and the triangle are not intersecting, this
	 * function replies {@link Double#NaN}.
	 * Otherwise the replied value is the factor that could be used
	 * for computing the intersection point. Value of zero means that
	 * the intersection point is the first point of the segment.
	 * Value of 1 means that the intersection point is the second point
	 * of the segment. Value in (0;1) means the intersection point
	 * is located on the segment.
	 *
	 * This function implements the algorithm provided by Jimenez et al.:<br/>
	 * <a href="./doc-files/segment_triangle_intersection.pdf">
	 * Juan J. Jimenez, Rafael J. Segura, Francisco R. Feito.
	 * "A robust segment/triangle intersection algorithm for interference tests. Efficiency study".
	 * Computational Geometry 43 (2010) pp 474-492. 2010.</a>
	 * 
	 * <strong>The algorithm of Jimenez et al. is faster than the algorithm of Badouel et al.</strong> 
	 * 
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @return the barycentric coordinates.
	 * @see #getTriangleSegmentIntersectionFactorWithBadouelAlgorithm(double, double, double, double, double, double, double, double, double, double, double, double, double, double, double)
	 */
	public static double getTriangleSegmentIntersectionFactorWithJimenezAlgorithm(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2) {
		// Input: Triangle (V1, V2, V3); Segment(Q1, Q2)

		// A = Q1 - V3
		double Ax = sx1 - tx3;
		double Ay = sy1 - ty3;
		double Az = sz1 - tz3;
		// B = V1 - V3
		double Bx = tx1 - tx3;
		double By = ty1 - ty3;
		double Bz = tz1 - tz3;
		// C = V2 - V3
		double Cx = tx2 - tx3;
		double Cy = ty2 - ty3;
		double Cz = tz2 - tz3;
		// W1 = B x C
		Vector3f W1 = new Vector3f();
		Vector3f.crossProduct(Bx, By, Bz, Cx, Cy, Cz, W1);
		// w = A . W1
		double w = Vector3f.dotProduct(Ax, Ay, Az, W1.getX(), W1.getY(), W1.getZ());
		// D = Q2 - Q1
		double Dx = sx2 - sx1;
		double Dy = sy2 - sy1;
		double Dz = sz2 - sz1;
		// s = D . W1
		double t, u;
		double s = Vector3f.dotProduct(Dx, Dy, Dz, W1.getX(), W1.getY(), W1.getZ());

		int cmp = MathUtil.compareEpsilon(w, 0.);
		if (cmp > 0) {
			// Case: if (w > epsilon)
			//
			// if (s > epsilon) rejection 2
			if (!MathUtil.isEpsilonZero(s)) {
				return Double.NaN;
			}
			// W2 = A x D
			Vector3f W2 = new Vector3f();
			Vector3f.crossProduct(Ax, Ay, Az, Dx, Dy, Dz, W2);
			// t = W2 . C
			t = Vector3f.dotProduct(W2.getX(), W2.getY(), W2.getZ(), Cx, Cy, Cz);
			// if (t < -epsilon) rejection 3
			if ( t < -MathConstants.EPSILON) {
				return Double.NaN;
			}
			// u = - W2 . B
			u = - Vector3f.dotProduct(W2.getX(), W2.getY(), W2.getZ(), Bx, By, Bz);
			// if ( u < -epsilon) rejection 4
			if (u < -MathConstants.EPSILON) {
				return Double.NaN;
			}
			// if (w < s + t + u) rejection 5
			if (w < (s + t + u)) {
				return Double.NaN;
			}
		} else if (cmp < 0) {
			// Case: if (w < -epsilon)
			//
			// if (s < -epsilon) rejection 2
			if (s < -MathConstants.EPSILON) {
				return Double.NaN;
			}
			// W2 = A x D
			Vector3f W2 = new Vector3f();
			Vector3f.crossProduct(Ax, Ay, Az, Dx, Dy, Dz, W2);
			// t = W2 . C
			t = Vector3f.dotProduct(W2.getX(), W2.getY(), W2.getZ(), Cx, Cy, Cz);
			// if (t > epsilon) rejection 3
			if ( t > MathConstants.EPSILON) {
				return Double.NaN;
			}
			// u = - W2 . B
			u = - Vector3f.dotProduct(W2.getX(), W2.getY(), W2.getZ(), Bx, By, Bz);
			// if ( u > epsilon) rejection 4
			if (u > MathConstants.EPSILON) {
				return Double.NaN;
			}
			// if (w > s + t + u) rejection 5
			if (w > (s + t + u)) {
				return Double.NaN;
			}
		} else {
			// Case: if (-epsilon <= w <= epsilon)
			// Swap Q1, Q2
			if (s > MathConstants.EPSILON) {
				// if (s > epsilon)
				//
				// W2 = D x A
				Vector3f W2 = new Vector3f();
				Vector3f.crossProduct(Dx, Dy, Dz, Ax, Ay, Az, W2);
				// t = W2 . C
				t = Vector3f.dotProduct(W2.getX(), W2.getY(), W2.getZ(), Cx, Cy, Cz);
				// if (t < -epsilon) rejection 3
				if (t < -MathConstants.EPSILON) {
					return Double.NaN;
				}
				// u = - W2 . B
				u = - Vector3f.dotProduct(W2.getX(), W2.getY(), W2.getZ(), Bx, By, Bz);
				// if ( u < -epsilon) rejection 4
				if (u < -MathConstants.EPSILON) {
					return Double.NaN;
				}
				// if (-s < t + u) rejection 5
				if (-s < (t + u)) {
					return Double.NaN;
				}
			} else if (s < -MathConstants.EPSILON) {
				// if (s < -epsilon)
				//
				// W2 = D x A
				Vector3f W2 = new Vector3f();
				Vector3f.crossProduct(Dx, Dy, Dz, Ax, Ay, Az, W2);
				// t = W2 . C
				t = Vector3f.dotProduct(W2.getX(), W2.getY(), W2.getZ(), Cx, Cy, Cz);
				// if (t > epsilon) rejection 3
				if (t > MathConstants.EPSILON) {
					return Double.NaN;
				}
				// u = - W2 . B
				u = - Vector3f.dotProduct(W2.getX(), W2.getY(), W2.getZ(), Bx, By, Bz);
				// if ( u > epsilon) rejection 4
				if (u > MathConstants.EPSILON) {
					return Double.NaN;
				}
				// if (-s > t + u) rejection 5
				if (-s > (t + u)) {
					return Double.NaN;
				}
			} else {
				// rejection 1
				return Double.NaN;
			}
		}
		//		double tt = 1. / (s-w);
		double t_param = w / (s-w);
		//		double alpha = tt * t;
		//		double beta = tt * u;
		return t_param;
	}

	/** Replies the intersection factor of the given segment
	 * when it is intersecting the triangle.
	 * <p>
	 * If the segment and the triangle are not intersecting, this
	 * function replies {@link Double#NaN}.
	 * If the segment and the triangle are intersecting, 
	 * this function replies the factor of the line's equation that
	 * permits to retreive the intersection point from the segment definition.
	 * 
	 * This function implements the Badouel algorithm:
	 * D. Badouel, An efficient ray-polygon intersection, in: Graphics Gems, Academic Press, 1990.
	 * 
	 * <strong>The algorithm of Jimenez et al. is faster than the algorithm of Badouel et al.</strong> 
	 *
	 * @param tx1 x coordinate of the first point of the triangle.
	 * @param ty1 y coordinate of the first point of the triangle.
	 * @param tz1 z coordinate of the first point of the triangle.
	 * @param tx2 x coordinate of the second point of the triangle.
	 * @param ty2 y coordinate of the second point of the triangle.
	 * @param tz2 z coordinate of the second point of the triangle.
	 * @param tx3 x coordinate of the third point of the triangle.
	 * @param ty3 y coordinate of the third point of the triangle.
	 * @param tz3 z coordinate of the third point of the triangle.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @return the factor that permits to compute the intersection point,
	 * {@link Double#NaN} when no intersection, {@link Double#POSITIVE_INFINITY}
	 * when an infinite number of intersection points.
	 * @see #getTriangleSegmentIntersectionFactorWithJimenezAlgorithm(double, double, double, double, double, double, double, double, double, double, double, double, double, double, double)
	 */
	public static double getTriangleSegmentIntersectionFactorWithBadouelAlgorithm(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2) {
		// Compute the vectors of the triangle
		double ux = tx2 - tx1;
		double uy = ty2 - ty1;
		double uz = tz2 - tz1;

		double vx = tx3 - tx1;
		double vy = ty3 - ty1;
		double vz = tz3 - tz1;

		// Components of the triangle's plane equation
		double a = uy * vz - uz * vy;
		double b = uz * vx - ux * vz;
		double c = ux * vy - uy * vx;
		double d = - a * tx1 - b * ty1 - c * tz1;

		double factor = Plane4f.getIntersectionFactorPlaneSegment(
				a, b, c, d,
				sx1, sy1, sz1, sx2, sy2, sz2);

		if (Double.isInfinite(factor)) {
			// The problem may be expressed as a 2D intersection between the segment and the triangle
			factor = Double.NaN;
		} else if (!Double.isNaN(factor)) {
			// Compute the vector from the first point of the triangle and the point I.
			double wx = (sx1 + factor * (sx2 - sx1)) - tx1;
			double wy = (sy1 + factor * (sy2 - sy1)) - ty1;
			double wz = (sz1 + factor * (sz2 - sz1)) - tz1;

			// Compute dot products
			double uv = Vector3f.dotProduct(ux, uy, uz, vx, vy, vz);
			double uu = Vector3f.dotProduct(ux, uy, uz, ux, uy, uz);
			double vv = Vector3f.dotProduct(vx, vy, vz, vx, vy, vz);
			double wv = Vector3f.dotProduct(wx, wy, wz, vx, vy, vz);
			double wu = Vector3f.dotProduct(wx, wy, wz, ux, uy, uz);

			// Compute the barycentric coordinates (s, t),
			// using the generalized perp operator on the point I.
			double denom = uv * uv - uu * vv;
			assert (denom != 0.);
			double s = (uv * wv) - (vv * wu) / denom;
			if (s >= 0. && s <= 1) {
				double t = (uv * wu) - (uu * wv) / denom;
				if (t < 0. || t >= 1.) {
					factor = Double.NaN;
				}
			} else {
				factor = Double.NaN;
			}
		}

		return factor;
	}

	/** Replies if a point is inside a triangle.
	 *
	 */
	private static boolean containsTrianglePoint(
			int i0, int i1, double[] v, double[] u1, double[] u2, double[] u3) {
		// is T1 completly inside T2?
		// check if V0 is inside tri(U0,U1,U2)
		double a  = u2[i1] - u1[i1];
		double b  = -(u2[i0] - u1[i0]);
		double c  = -a * u1[i0] - b * u1[i1];
		double d0 = a * v[i0] + b * v[i1] + c;

		a         = u3[i1] - u2[i1];
		b         = -(u3[i0] - u2[i0]);
		c         = -a * u2[i0] - b * u2[i1];
		double d1 = a * v[i0] + b * v[i1] + c;

		a         = u1[i1] - u2[i1];
		b         = -(u1[i0] - u3[i0]);
		c         = -a * u3[i0] - b * u3[i1];
		double d2 = a * v[i0] + b * v[i1] + c;

		return ((d0*d1>0.)&&(d0*d2>0.0));
	}

	/** Replies if coplanar segment-triangle intersect.
	 */
	private static boolean intersectsCoplanarTriangle(
			int i0, int i1, int con, double[] s1, double[] s2, double[] u1, double[] u2, double[] u3) {
		double Ax,Ay;

		Ax = s2[i0] - s1[i0];
		Ay = s2[i1] - s1[i1];

		// test edge U0,U1 against V0,V1
		if (intersectEdgeEdge(i0, i1, con, Ax, Ay, s1, u1, u2)) return true;
		// test edge U1,U2 against V0,V1
		if (intersectEdgeEdge(i0, i1, con, Ax, Ay, s1, u2, u3)) return true;
		// test edge U2,U1 against V0,V1
		if (intersectEdgeEdge(i0, i1, con, Ax, Ay, s1, u3, u1)) return true;

		return false;
	}

	/** This edge to edge test is based on Franlin Antonio's gem:
	 * "Faster Line Segment Intersection", in Graphics Gems III,
	 * pp. 199-202.
	 */   
	private static boolean intersectEdgeEdge(
			int i0, int i1, int con, double Ax, double Ay, double[] v, double[] u1, double[] u2) {
		// [v,b] is the segment that contains the point v
		// [c,d] is the segment [u1,u2]

		// A is the vector (v,b)
		// B is the vector (d,c)
		// C is the vector (c,v)

		double Bx = u1[i0] - u2[i0];
		double By = u1[i1] - u2[i1];
		double Cx = v[i0]  - u1[i0];
		double Cy = v[i1]  - u1[i1];

		// 

		double f = Ay * Bx - Ax * By;
		double d = By * Cx - Bx * Cy; // Line equation: V+d*A

		boolean up = false;
		boolean down = false;

		if (f>0) {
			switch(con) {
			case 1: // First point must be ignored
				down = (d>0);
				up = (d<=f);
				break;
			case 2: // Second point must be ignored
				down = (d>=0);
				up = (d<f);
				break;
			case 3: // First and Second points must be ignored
				down = (d>0);
				up = (d<f);
				break;
			default:
				down = (d>=0);
				up = (d<=f);
			}
		}
		else if (f<0) {
			switch(con) {
			case 1: // First point must be ignored
				down = (d>=f);
				up = (d<0);
				break;
			case 2: // Second point must be ignored
				down = (d>f);
				up = (d<=0);
				break;
			case 3: // First and Second points must be ignored
				down = (d>f);
				up = (d<0);
				break;
			default:
				down = (d>=f);
				up = (d<=0);
			}
		}

		if (up&&down) {
			double e = Ax * Cy - Ay * Cx;
			if (f>=0) return ((e>=0)&&(e<=f));
			return ((e>=f)&&(e<=0));
		}

		return false;
	}

	/**
	 * Tests if the point {@code (px,py,pz)} 
	 * lies inside a 3D triangle
	 * given by {@code (x1,y1,z1)}, {@code (x2,y2,z2)}
	 * and {@code (x3,y3,z3)} points.
	 * <p>
	 * <strong>Caution: Tests are "epsiloned."</strong>
	 * <p>
	 * Parameter <var>forceCoplanar</var> has a deep influence on the function
	 * result. It indicates if coplanarity test must be done or not.
	 * Following table explains this influence:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th>Point is coplanar?</th>
	 * <th>Point projection on plane is inside triangle?</th>
	 * <th><var>forceCoplanar</var></th>
	 * <th><code>intersectsPointTrangle()</code> Result</th>
	 * </tr>
	 * </thead>
	 * <tr>
	 * <td><code>true</code></td>
	 * <td><code>true</code></td>
	 * <td><code>true</code></td>
	 * <td><code>true</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>true</code></td>
	 * <td><code>true</code></td>
	 * <td><code>false</code></td>
	 * <td><code>true</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>true</code></td>
	 * <td><code>false</code></td>
	 * <td><code>true</code></td>
	 * <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>true</code></td>
	 * <td><code>false</code></td>
	 * <td><code>false</code></td>
	 * <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>false</code></td>
	 * <td><code>true</code></td>
	 * <td><code>true</code></td>
	 * <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>false</code></td>
	 * <td><code>true</code></td>
	 * <td><code>false</code></td>
	 * <td><code>true</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>false</code></td>
	 * <td><code>false</code></td>
	 * <td><code>true</code></td>
	 * <td><code>false</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>false</code></td>
	 * <td><code>false</code></td>
	 * <td><code>false</code></td>
	 * <td><code>false</code></td>
	 * </tr>
	 * </table>
	 * <p>
	 * <strong>Trigonometric Method (Slowest)</strong>
	 * <p>
	 * A common way to check if a point is in a triangle is to 
	 * find the vectors connecting the point to each of the 
	 * triangle's three vertices and sum the angles between 
	 * those vectors. If the sum of the angles is 2*pi 
	 * then the point is inside the triangle, otherwise it 
	 * is not. <em>It works, but it is very slow.</em>
	 * <p>
	 * <center><img src="doc-files/point_segment.gif" alt="Point-Segment Intersection Picture 1">
	 * <img src="doc-files/point_segment_2.jpg" alt="Point-Segment Intersection Picture 2"></center>
	 * <p>
	 * The advantage of the method above is that it's very simple to understand so that once 
	 * you read it you should be able to remember it forever and code it up at 
	 * any time without having to refer back to anything.
	 * <p>
	 * <strong>Barycenric Method (Fastest)</strong>
	 * <p>
	 * There's another method that is also as easy conceptually but executes faster.
	 * The downside is there's a little more math involved, but once you see 
	 * it worked out it should be no problem.
	 * <p>
	 * So remember that the three points of the triangle define a plane in space. 
	 * Pick one of the points and we can consider all other locations on the plane 
	 * as relative to that point. Let's select A -- it'll be our origin on the 
	 * plane. Now what we need are basis vectors so we can give coordinate 
	 * values to all the locations on the plane. 
	 * We'll pick the two edges of the triangle that touch A, 
	 * (C - A) and (B - A). 
	 * Now we can get to any point on the plane just by starting at A 
	 * and walking some distance along (C - A) and then from there walking 
	 * some more in the direction (B - A).
	 * <p>
	 * <center><img src="doc-files/point_segment_3.png" alt="Point-Segment Intersection Picture 3"></center>
	 * <p>
	 * With that in mind we can now describe any point on the plane as:<br>
	 * P = A + u * (C - A) + v * (B - A)
	 * <p>
	 * Notice now that if u or v < 0 then we've walked in the wrong direction 
	 * and must be outside the triangle. Also if u or v > 1 then we've 
	 * walked too far in a direction and are outside the triangle. 
	 * Finally if u + v > 1 then we've crossed the edge BC again leaving the triangle.
	 * <p>
	 * Given u and v we can easily calculate the point P with the above 
	 * equation, but how can we go in the reverse direction and calculate 
	 * u and v from a given point P?<br>
	 * P = A + u * (C - A) + v * (B - A)       // Original equation<br>
	 * (P - A) = u * (C - A) + v * (B - A)     // Subtract A from both sides<br>
	 * v2 = u * v0 + v * v1                    // Substitute v0, v1, v2 for less writing
	 * <p>
	 * We have two unknowns (u and v) so we need two equations to solve
	 * for them.  Dot both sides by v0 to get one and dot both sides by
	 * v1 to get a second.<br>
	 * (v2) . v0 = (u * v0 + v * v1) . v0<br>
	 * (v2) . v1 = (u * v0 + v * v1) . v1<br>
	 * <p>
	 * Distribute v0 and v1<br>
	 * v2 . v0 = u * (v0 . v0) + v * (v1 . v0)<br>
	 * v2 . v1 = u * (v0 . v1) + v * (v1 . v1)
	 * <p>
	 * Now we have two equations and two unknowns and can solve one
	 * equation for one variable and substitute into the other.  Or
	 * fire up GNU Octave and save some handwriting.<br>
	 * Solve[v2.v0 == {u(v0.v0) + v(v1.v0), v2.v1 == u(v0.v1) + v(v1.v1)}, {u, v}]<br>
	 * u = ((v1.v1)(v2.v0)-(v1.v0)(v2.v1)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))<br>
	 * v = ((v0.v0)(v2.v1)-(v0.v1)(v2.v0)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))
	 *
	 * @param ax the X coordinate of the first point of the triangle
	 * @param ay the Y coordinate of the first point of the triangle
	 * @param az the Z coordinate of the first point of the triangle
	 * @param bx the X coordinate of the second point of the triangle
	 * @param by the Y coordinate of the second point of the triangle
	 * @param bz the Z coordinate of the second point of the triangle
	 * @param cx the X coordinate of the third point of the triangle
	 * @param cy the Y coordinate of the third point of the triangle
	 * @param cz the Z coordinate of the third point of the triangle
	 * @param px the X coordinate of the point
	 * @param py the Y coordinate of the point
	 * @param pz the Z coordinate of the point
	 * @param forceCoplanar is <code>true</code> to force to test
	 * if the given point is coplanar to the triangle, <code>false</code>
	 * to not consider coplanarity of the point.
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the points is coplanar - or not,
	 * depending on <var>forceCoplanar</var> - to the triangle and
	 * lies inside it, otherwise <code>false</code>
	 * @since 3.0
	 */
	public static boolean containsTrianglePoint(
			double ax, double ay, double az,
			double bx, double by, double bz,
			double cx, double cy, double cz,
			double px, double py, double pz,
			boolean forceCoplanar, double epsilon) {

		//
		// Compute vectors        
		//
		// v0 = C - A
		double v0x = cx - ax;
		double v0y = cy - ay;
		double v0z = cz - az;
		// v1 = B - A
		double v1x = bx - ax;
		double v1y = by - ay;
		double v1z = bz - az;
		// v2 = P - A
		double v2x = px - ax;
		double v2y = py - ay;
		double v2z = pz - az;

		//
		// Compute dot products
		//
		// dot01 = dot(v0, v0)
		double dot00 = Vector3f.dotProduct(v0x, v0y, v0z, v0x, v0y, v0z);
		// dot01 = dot(v0, v1)
		double dot01 = Vector3f.dotProduct(v0x, v0y, v0z, v1x, v1y, v1z);
		// dot02 = dot(v0, v2)
		double dot02 = Vector3f.dotProduct(v0x, v0y, v0z, v2x, v2y, v2z);
		// dot11 = dot(v1, v1)
		double dot11 = Vector3f.dotProduct(v1x, v1y, v1z, v1x, v1y, v1z);
		// dot12 = dot(v1, v2)
		double dot12 = Vector3f.dotProduct(v1x, v1y, v1z, v2x, v2y, v2z);

		//
		// Compute barycentric coordinates
		//
		double invDenom = 1. / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

		// Check if point is in triangle
		if ((MathUtil.compareEpsilon(u, 0., epsilon) >= 0)
				&& (MathUtil.compareEpsilon(v, 0., epsilon) >= 0) 
				&& (MathUtil.compareEpsilon(u + v, 1., epsilon) <= 0)) {
			if (forceCoplanar) {
				// Triangle's plane equation:
				// nx = ay * (bz - cz) + by * (cz - az) + cy * (az - bz)
				// ny = az * (bx - cx) + bz * (cx - ax) + cz * (ax - bx)
				// nz = ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)
				// d = - (nx * ax + ny * ay + nz * az)

				// Reuse the dot* variables to prevent memory allocation
				dot00 = ay * (bz - cz) + by * v0z - cy * v1z;
				dot01 = az * (bx - cx) + bz * v0x - cz * v1x;
				dot02 = ax * (by - cy) + bx * v0y - cx * v1y;
				dot11 = - (dot00 * ax + dot01 * ay + dot02 * az);
				dot12 = dot00 * px + dot01 * py + dot02 * pz + dot11;

				return MathUtil.isEpsilonZero(dot12, epsilon);
			}
			return true;
		}
		return false;
	}

	//---------------------------------------------------------------------------------------	

	/** First point.
	 */
	protected final Point3f p1;

	/** Second point.
	 */
	protected final Point3f p2;

	/** Third point.
	 */
	protected final Point3f p3;

	private SoftReference<Vector3f> normal = null;
	private Point3f pivot = null;
	private SoftReference<Quaternion> orientation = null;

	/**
	 * Construct a triangle 3D.
	 * This constructor does not copy the given points.
	 * The triangle's points will be references to the
	 * given points.
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public Triangle3f(Point3f p1, Point3f p2, Point3f p3) {
		this(p1, p2, p3, false);
	}

	/**
	 * Construct a triangle 3D.
	 * This constructor does not copy the given points.
	 * The triangle's points will be references to the
	 * given points.
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param copyPoints indicates if the given points may be copied
	 * or referenced by this triangle. If <code>true</code> points
	 * will be copied, <code>false</code> points will be referenced.
	 */
	public Triangle3f(Point3f p1, Point3f p2, Point3f p3, boolean copyPoints) {
		if (copyPoints) {
			this.p1 = new Point3f(p1);
			this.p2 = new Point3f(p2);
			this.p3 = new Point3f(p3);
		}
		else {
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
		}
	}

	/**
	 * Construct a triangle 3D.
	 * 
	 * @param p1x is the x coordinate of the first point.
	 * @param p1y is the y coordinate of the first point.
	 * @param p1z is the z coordinate of the first point.
	 * @param p2x is the x coordinate of the first point.
	 * @param p2y is the y coordinate of the first point.
	 * @param p2z is the z coordinate of the first point.
	 * @param p3x is the x coordinate of the first point.
	 * @param p3y is the y coordinate of the first point.
	 * @param p3z is the z coordinate of the first point.
	 */
	public Triangle3f(
			double p1x, double p1y, double p1z,
			double p2x, double p2y, double p2z,
			double p3x, double p3y, double p3z) {
		this.p1 = new Point3f(p1x, p1y, p1z);
		this.p2 = new Point3f(p2x, p2y, p2z);
		this.p3 = new Point3f(p3x, p3y, p3z);
	}

	/**
	 * Checks if a point is inside this triangle.
	 * 
	 * @param point is the the point
	 * @return <code>true</code> if the point is in the triangle , otherwise <code>false</code>.
	 */
	public boolean contains(Point3f point) {
		return containsTrianglePoint(
				this.p1.getX(), this.p1.getY(), this.p1.getZ(), 
				this.p2.getX(), this.p2.getY(), this.p2.getZ(), 
				this.p3.getX(), this.p3.getY(), this.p3.getZ(),
				point.getX(), point.getY(), point.getZ(),
				true, 0.);
	}

	/** Replies the normal to this triangle face.
	 * 
	 * @return the normal.
	 */
	public Vector3f getNormal() {
		Vector3f v = null;
		if (this.normal!=null) {
			v = this.normal.get();
		}
		if (v==null) {
			v = new Vector3f();
			Vector3f.crossProduct(
					this.p2.getX() - this.p1.getX(),
					this.p2.getY() - this.p1.getY(),
					this.p2.getZ() - this.p1.getZ(),
					this.p3.getX() - this.p1.getX(),
					this.p3.getY() - this.p1.getY(),
					this.p3.getZ() - this.p1.getZ(),
					v);
			v.normalize();
			this.normal = new SoftReference<>(v);
		}
		return v;
	}

	/** Replies the plane on which this triangle is coplanar.
	 * 
	 * @return the coplanar plane to this triangle
	 */
	public Plane3D<?> getPlane() {
		return toPlane(
				getX1(), getY1(), getZ1(),
				getX1(), getY1(), getZ1(),
				getX1(), getY1(), getZ1());
	}

	/** Clear any buffered data.
	 *
	 * By default, the normal and the orientation are bufferred.
	 */
	protected void clearBufferedData() {
		this.normal = null;
		this.orientation = null;
	}

	@Override
	public void clear() {
		this.p1.set(0., 0., 0.);
		this.p2.set(0., 0., 0.);
		this.p3.set(0., 0., 0.);
		this.pivot.set(0., 0., 0.);
		clearBufferedData();
	}

	/**
	 * Replies the first point of the triangle.
	 * 
	 * @return the first point of the triangle.
	 */
	public Point3f getP1() {
		return this.p1;
	}

	/**
	 * Change the first point of the triangle.
	 * 
	 * @param point
	 */
	public void setP1(Point3D point) {
		setP1(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Change the first point of the triangle.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setP1(double x, double y, double z) {
		this.p1.set(x, y, z);
		clearBufferedData();
	}

	/**
	 * Replies the second point of the triangle.
	 * 
	 * @return the second point of the triangle.
	 */
	public Point3f getP2() {
		return this.p2;
	}

	/**
	 * Change the second point of the triangle.
	 * 
	 * @param point
	 */
	public void setP2(Point3D point) {
		setP2(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Change the second point of the triangle.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setP2(double x, double y, double z) {
		this.p2.set(x, y, z);
		clearBufferedData();
	}

	/**
	 * Replies the third point of the triangle.
	 * 
	 * @return the third point of the triangle.
	 */
	public Point3f getP3() {
		return this.p3;
	}

	/**
	 * Change the third point of the triangle.
	 * 
	 * @param point
	 */
	public void setP3(Point3D point) {
		setP3(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Change the third point of the triangle.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setP3(double x, double y, double z) {
		this.p3.set(x, y, z);
		clearBufferedData();
	}

	/** Replies the x coordinate of the first point.
	 * 
	 * @return the x coordinate of the first point.
	 */
	public double getX1() {
		return this.p1.getX();
	}

	/** Replies the y coordinate of the first point.
	 * 
	 * @return the y coordinate of the first point.
	 */
	public double getY1() {
		return this.p1.getY();
	}

	/** Replies the z coordinate of the first point.
	 * 
	 * @return the z coordinate of the first point.
	 */
	public double getZ1() {
		return this.p1.getZ();
	}

	/** Replies the x coordinate of the second point.
	 * 
	 * @return the x coordinate of the second point.
	 */
	public double getX2() {
		return this.p2.getX();
	}

	/** Replies the y coordinate of the second point.
	 * 
	 * @return the y coordinate of the second point.
	 */
	public double getY2() {
		return this.p2.getY();
	}

	/** Replies the z coordinate of the second point.
	 * 
	 * @return the z coordinate of the second point.
	 */
	public double getZ2() {
		return this.p2.getZ();
	}

	/** Replies the x coordinate of the third point.
	 * 
	 * @return the x coordinate of the third point.
	 */
	public double getX3() {
		return this.p3.getX();
	}

	/** Replies the y coordinate of the third point.
	 * 
	 * @return the y coordinate of the third point.
	 */
	public double getY3() {
		return this.p3.getY();
	}

	/** Replies the z coordinate of the third point.
	 * 
	 * @return the z coordinate of the third point.
	 */
	public double getZ3() {
		return this.p3.getZ();
	}

	/** Set the points of the triangle.
	 *
	 * @param p1 the first point.
	 * @param p2 the second point.
	 * @param p3 the third point.
	 */
	public void set(Point3D p1, Point3D p2, Point3D p3) {
		this.p1.set(p1);
		this.p2.set(p2);
		this.p3.set(p3);
		clearBufferedData();
	}

	@Override
	public void set(Shape3f s) {
		if (s instanceof Triangle3f) {
			Triangle3f t = (Triangle3f) s;
			set(t.getP1(), t.getP2(), t.getP3());
		} else {
			AlignedBox3f r = s.toBoundingBox();
			this.p1.set(r.getMinX(), r.getMinY(), r.getMinZ());
			this.p2.set(r.getMaxX(), r.getMaxY(), r.getMinZ());
			this.p3.set(r.getMaxX(), r.getMaxY(), r.getMaxZ());
			clearBufferedData();
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("["); //$NON-NLS-1$
		buffer.append(this.p1.getX());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p1.getY());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p1.getZ());
		buffer.append("]-["); //$NON-NLS-1$
		buffer.append(this.p2.getX());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p2.getY());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p2.getZ());
		buffer.append("]-["); //$NON-NLS-1$
		buffer.append(this.p3.getX());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p3.getY());
		buffer.append(";"); //$NON-NLS-1$
		buffer.append(this.p3.getZ());
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

	@Override
	public double distance(Point3D p) {
		return Math.sqrt(distanceSquared(p));
	}

	@Override
	public double distanceSquared(Point3D p) {
		if (containsTrianglePoint(
				this.p1.getX(), this.p1.getY(), this.p1.getZ(),
				this.p2.getX(), this.p2.getY(), this.p2.getZ(),
				this.p3.getX(), this.p3.getY(), this.p3.getZ(),
				p.getX(), p.getY(), p.getZ(),
				false, 0)) {
			Vector3f n = getNormal();
			double d = -(n.getX() * this.p1.getX() + n.getY() * this.p1.getY() + n.getZ() * this.p1.getZ());
			d = n.getX() * p.getX() + n.getY() * p.getY() + n.getZ() * p.getZ() + d;
			return d * d;
		}
		double d1 = Segment3f.distanceSquaredSegmentPoint(
				this.p1.getX(), this.p1.getY(), this.p1.getZ(),
				this.p2.getX(), this.p2.getY(), this.p2.getZ(),
				p.getX(), p.getY(), p.getZ());
		double d2 = Segment3f.distanceSquaredSegmentPoint(
				this.p1.getX(), this.p1.getY(), this.p1.getZ(),
				this.p3.getX(), this.p3.getY(), this.p3.getZ(),
				p.getX(), p.getY(), p.getZ());
		double d3 = Segment3f.distanceSquaredSegmentPoint(
				this.p2.getX(), this.p2.getY(), this.p2.getZ(),
				this.p3.getX(), this.p3.getY(), this.p3.getZ(),
				p.getX(), p.getY(), p.getZ());
		return MathUtil.min(d1, d2, d3);
	}

	@Override
	public double distanceL1(Point3D p) {
		Point3D c = getClosestPointTo(p);
		return c.distanceL1(p);
	}

	@Override
	public double distanceLinf(Point3D p) {
		Point3D c = getClosestPointTo(p);
		return c.distanceLinf(p);
	}

	/**
	 * Replies the orientation of the triangle.
	 * 
	 * @return the orientation.
	 */
	public Quaternion getOrientation() {
		Quaternion orient = null;
		if (this.orientation!=null) {
			orient = this.orientation.get();
		}
		if (orient==null) {
			Vector3f norm = getNormal();
			assert(norm!=null);
			CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
			assert(cs!=null);
			Vector3f up = cs.getUpVector();
			assert(up!=null);
			Vector3f axis = new Vector3f();
			Vector3f.crossProduct(
					up.getX(), up.getY(), up.getZ(),
					norm.getX(), norm.getY(), norm.getZ(),
					cs, axis);
			axis.normalize();
			orient = new Quaternion();
			orient.setAxisAngle(
					axis, 
					Vector3f.signedAngle(
							up.getX(), up.getY(), up.getZ(),
							norm.getX(), norm.getY(), norm.getZ()));
			this.orientation = new SoftReference<>(orient);
		}
		return orient;
	}

	/** Replies the pivot point associated to this triangle.
	 * The default pivot point is the first point of the triangle.
	 *
	 * @return the pivot point.
	 */
	public Point3f getPivot() {
		return this.pivot==null ? this.p1.clone() : this.pivot.clone();
	}

	/** Change the pivot point for this triangle.
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPivot(double x, double y, double z) {
		this.pivot = new Point3f(x, y, z);
	}

	/** Change the pivot point for this triangle.
	 *
	 * @param point is the new pivot point. If <code>null</code>, the default pivot point
	 * is restored (the first point of the triangle).
	 */
	public void setPivot(Point3f point) {
		if (point == null) {
			this.pivot = null;
		} else if (this.pivot == null) {
			this.pivot = new Point3f(point);
		} else {
			this.pivot.set(point);
		}
	}

	@Override
	public void transform(Transform3D trans) {
		trans.transform(this.p1);
		trans.transform(this.p2);
		trans.transform(this.p3);
		if (this.pivot != null) {
			trans.transform(this.pivot);
		}
		clearBufferedData();
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		this.p1.setX(this.p1.getX() + dx);
		this.p1.setY(this.p1.getY() + dy);
		this.p1.setZ(this.p1.getZ() + dz);
		this.p2.setX(this.p2.getX() + dx);
		this.p2.setY(this.p2.getY() + dy);
		this.p2.setZ(this.p2.getZ() + dz);
		this.p3.setX(this.p3.getX() + dx);
		this.p3.setY(this.p3.getY() + dy);
		this.p3.setZ(this.p3.getZ() + dz);
		clearBufferedData();
	}

	/** Rotate the segment around its pivot point.
	 * The pivot point is the first point of the segment.
	 *
	 * @param rotation the rotation.
	 */
	public void rotate(Quaternion rotation) {
		Transform3D tr = new Transform3D();
		tr.setRotation(rotation);
		transform(tr);
	}

	/** Rotate the segment around a given pivot point.
	 * The default pivot point of the segment is its first point.
	 *
	 * @param rotation the rotation.
	 * @param pivot the pivot point. If <code>null</code> the default pivot point is used.
	 */
	public void rotate(Quaternion rotation, Point3D pivot) {
		if (pivot==null) {
			rotate(rotation);
		} else {
			assert(pivot!=null);

			Transform3D tr = new Transform3D();
			tr.setRotation(rotation);
			Vector3f v = new Vector3f();

			v.sub(this.p1, pivot);
			tr.transform(v);
			this.p1.add(pivot, v);

			v.sub(this.p2, pivot);
			tr.transform(v);
			this.p2.add(pivot, v);

			v.sub(this.p3, pivot);
			tr.transform(v);
			this.p3.add(pivot, v);

			clearBufferedData();
		}
	}

	@Override
	public AlignedBox3f toBoundingBox() {
		AlignedBox3f b = new AlignedBox3f();
		toBoundingBox(b);
		return b;
	}

	@Override
	public void toBoundingBox(AlignedBox3f box) {
		Pair<Double, Double> p = new Pair<>();
		MathUtil.getMinMax(this.p1.getX(), this.p2.getX(), this.p3.getX(), p);
		box.setX(p.getA(), p.getB());
		MathUtil.getMinMax(this.p1.getY(), this.p2.getY(), this.p3.getY(), p);
		box.setY(p.getA(), p.getB());
		MathUtil.getMinMax(this.p1.getZ(), this.p2.getZ(), this.p3.getZ(), p);
		box.setZ(p.getA(), p.getB());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Triangle3f) {
			Triangle3f t3f = (Triangle3f) obj;
			return ((getP1().equals(t3f.getP1())) &&
					(getP2().equals(t3f.getP2())) &&
					(getP3().equals(t3f.getP3())));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(getX1());
		bits = 31L * bits + doubleToLongBits(getY1());
		bits = 31L * bits + doubleToLongBits(getZ1());
		bits = 31L * bits + doubleToLongBits(getX2());
		bits = 31L * bits + doubleToLongBits(getY2());
		bits = 31L * bits + doubleToLongBits(getZ2());
		bits = 31L * bits + doubleToLongBits(getX3());
		bits = 31L * bits + doubleToLongBits(getY3());
		bits = 31L * bits + doubleToLongBits(getZ3());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public boolean contains(double x, double y, double z) {
		return containsTrianglePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				x, y, z,
				true, MathConstants.EPSILON);
	}

	/**
	 * Checks if the projection of a point on the triangle's plane is inside the triangle.
	 * 
	 * @param point is the the point
	 * @return <code>true</code> if the projection of the point is in the triangle, otherwise <code>false</code>.
	 */
	public boolean containsProjectionOf(Point3f point) {
		return containsProjectionOf(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Checks if the projection of a point on the triangle's plane is inside the triangle.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return <code>true</code> if the projection of the point is in the triangle, otherwise <code>false</code>.
	 */
	public boolean containsProjectionOf(double x, double y, double z) {
		Point3f proj = getPlane().getProjection(x, y, z);
		if (proj == null) {
			return false;
		}
		return contains(proj);
	}

	@Override
	public boolean isEmpty() {
		return this.p1.equals(this.p2) && this.p1.equals(this.p3);
	}

	@Unefficient
	@Override
	public Point3D getClosestPointTo(Point3D p) {
		Point3f c = new Point3f();
		computeClosestPointTrianglePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				p.getX(), p.getY(), p.getZ(),
				c);
		return c;
	}

	@Override
	public Point3D getFarthestPointTo(Point3D p) {
		double d1 = this.p1.distanceSquared(p);
		double d2 = this.p2.distanceSquared(p);
		double d3 = this.p3.distanceSquared(p);
		if (d1 >= d2) {
			if (d3 >= d1) {
				return this.p3.clone();
			}
			return this.p1.clone();
		} else if (d3 >= d2) {
			return this.p3.clone();
		}
		return this.p2.clone();
	}

	/**
	 * Replies the height of the projection on the triangle that is representing a ground.
	 * <p>
	 * Assuming that the triangle is representing a face of a terrain/ground,
	 * this function compute the height of the ground just below the given position.
	 * The input of this function is the coordinate of a point on the horizontal plane.
	 * The up coordinate is given by {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 * 
	 * @param x is the x-coordinate on point to project on the horizontal plane.
	 * @param y is the y-coordinate of point to project on the horizontal plane.
	 * @return the height of the face at the given coordinate.
	 */
	public double getGroundHeight(double x, double y) {
		return getGroundHeight(x, y, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the height of the projection on the triangle that is representing a ground.
	 * <p>
	 * Assuming that the triangle is representing a face of a terrain/ground,
	 * this function compute the height of the ground just below the given position.
	 * The input of this function is the coordinate of a point on the horizontal plane.
	 * 
	 * @param x is the x-coordinate on point to project on the horizontal plane.
	 * @param y is the y-coordinate of point to project on the horizontal plane.
	 * @param system is the coordinate system to use for determining the up coordinate.
	 * @return the height of the face at the given coordinate.
	 */
	public double getGroundHeight(double x, double y, CoordinateSystem3D system) {
		assert(system!=null);
		int idx = system.getHeightCoordinateIndex();
		assert(idx==1 || idx==2);
		Point3f p1 = getP1();
		assert(p1!=null);
		Vector3f v = getNormal();
		assert(v!=null);

		if (idx==1 && v.getY()==0.)
			return p1.getY();
		if (idx==2 && v.getZ()==0.)
			return p1.getZ();

		double d = -(v.getX() * p1.getX() + v.getY() * p1.getY() + v.getZ() * p1.getZ());

		if (idx==2)
			return -(v.getX() * x + v.getY() * y + d) / v.getZ();

		return -(v.getX() * x + v.getZ() * y + d) / v.getY();
	}

	/**
	 * Replies the height of the projection on the triangle that is representing a ground.
	 * <p>
	 * Assuming that the triangle is representing a face of a terrain/ground,
	 * this function compute the height of the ground just below the given position.
	 * The input of this function is the coordinate of a point on the horizontal plane.
	 * 
	 * @param point is the point to project on the triangle.
	 * @return the height of the face at the given coordinate.
	 */
	public double getGroundHeight(Point2D point) {
		return getGroundHeight(point.getX(), point.getY());
	}

	/**
	 * Replies the height of the projection on the triangle that is representing a ground.
	 * <p>
	 * Assuming that the triangle is representing a face of a terrain/ground,
	 * this function compute the height of the ground just below the given position.
	 * The input of this function is the coordinate of a point on the horizontal plane.
	 * 
	 * @param point is the point to project on the triangle.
	 * @param system is the coordinate system to use for determining the up coordinate.
	 * @return the height of the face at the given coordinate.
	 */
	public double getGroundHeight(Point2D point, CoordinateSystem3D system) {
		return getGroundHeight(point.getX(), point.getY(), system);
	}

	/**
	 * Replies the projection on the triangle that is representing a ground.
	 * <p>
	 * Assuming that the triangle is representing a face of a terrain/ground,
	 * this function compute the position on the ground just below the given position.
	 * The input of this function is the coordinate of a point on the horizontal plane.
	 * 
	 * @param x is the x-coordinate on point to project on the horizontal plane.
	 * @param y is the y-coordinate of point to project on the horizontal plane.
	 * @return the position on the ground.
	 */
	public Point3f getPointOnGround(double x, double y) {
		return getPointOnGround(x, y, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Replies the projection on the triangle that is representing a ground.
	 * <p>
	 * Assuming that the triangle is representing a face of a terrain/ground,
	 * this function compute the position on the ground just below the given position.
	 * The input of this function is the coordinate of a point on the horizontal plane.
	 * 
	 * @param x is the x-coordinate on point to project on the horizontal plane.
	 * @param y is the y-coordinate of point to project on the horizontal plane.
	 * @param system is the coordinate system to use for determining the up coordinate.
	 * @return the position on the ground.
	 */
	public Point3f getPointOnGround(double x, double y, CoordinateSystem3D system) {
		assert(system!=null);
		int idx = system.getHeightCoordinateIndex();
		assert(idx==1 || idx==2);
		Point3f p1 = getP1();
		assert(p1!=null);
		Vector3f v = getNormal();
		assert(v!=null);

		if (idx==1 && v.getY()==0.)
			return new Point3f(x, p1.getY(), y);
		if (idx==2 && v.getZ()==0.)
			return new Point3f(x, y, p1.getZ());

		double d = -(v.getX() * p1.getX() + v.getY() * p1.getY() + v.getZ() * p1.getZ());

		if (idx==2)
			return new Point3f(x, y, -(v.getX() * x + v.getY() * y + d) / v.getZ());

		return new Point3f(x, -(v.getX() * x + v.getZ() * y + d) / v.getY(), y);
	}

	/**
	 * Replies the projection on the triangle that is representing a ground.
	 * <p>
	 * Assuming that the triangle is representing a face of a terrain/ground,
	 * this function compute the position on the ground just below the given position.
	 * The input of this function is the coordinate of a point on the horizontal plane.
	 * 
	 * @param point is the point to project on the triangle.
	 * @return the position on the ground.
	 */
	public Point3f getPointOnGround(Point2D point) {
		return getPointOnGround(point.getX(), point.getY());
	}

	/**
	 * Replies the projection on the triangle that is representing a ground.
	 * <p>
	 * Assuming that the triangle is representing a face of a terrain/ground,
	 * this function compute the position on the ground just below the given position.
	 * The input of this function is the coordinate of a point on the horizontal plane.
	 * 
	 * @param point is the point to project on the triangle.
	 * @param system is the coordinate system to use for determining the up coordinate.
	 * @return the position on the ground.
	 */
	public Point3f getPointOnGround(Point2D point, CoordinateSystem3D system) {
		return getPointOnGround(point.getX(), point.getY(), system);
	}

	@Override
	public boolean intersects(AlignedBox3f s) {
		return intersectsTriangleAlignedBox(
				getX1(), getY1(), getY1(),
				getX2(), getY2(), getY3(),
				getX3(), getY3(), getY3(),
				s.getMinX(), s.getMinY(), s.getMinZ(),
				s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}

	@Override
	public boolean intersects(Sphere3f s) {
		return intersectsTriangleSphere(
				getX1(), getY1(), getY1(),
				getX2(), getY2(), getY3(),
				getX3(), getY3(), getY3(),
				s.getX(), s.getY(), s.getZ(),
				s.getRadius());
	}

	@Override
	public boolean intersects(Segment3f s) {
		return intersectsTriangleSegment(
				getX1(), getY1(), getY1(),
				getX2(), getY2(), getY3(),
				getX3(), getY3(), getY3(),
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2());
	}

	@Override
	public boolean intersects(Triangle3f s) {
		return intersectsCoplanarTriangleTriangle(
				getX1(), getY1(), getY1(),
				getX2(), getY2(), getY3(),
				getX3(), getY3(), getY3(),
				s.getX1(), s.getY1(), s.getY1(),
				s.getX2(), s.getY2(), s.getY3(),
				s.getX3(), s.getY3(), s.getY3());
	}

	@Override
	public boolean intersects(Capsule3f s) {
		return intersectsTriangleCapsule(
				getX1(), getY1(), getY1(),
				getX2(), getY2(), getY3(),
				getX3(), getY3(), getY3(),
				s.getMedialX1(), s.getMedialY1(), s.getMedialZ1(),
				s.getMedialX2(), s.getMedialY2(), s.getMedialZ2(),
				s.getRadius());
	}

	@Override
	public boolean intersects(OrientedBox3f s) {
		return intersectsTriangleOrientedBox(
				getX1(), getY1(), getY1(),
				getX2(), getY2(), getY3(),
				getX3(), getY3(), getY3(),
				s.getCenterX(), s.getCenterY(), s.getCenterZ(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisZ(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisZ(),
				s.getThirdAxisX(), s.getThirdAxisY(), s.getThirdAxisZ(),
				s.getFirstAxisExtent(), s.getSecondAxisExtent(), s.getThirdAxisExtent());
	}

	@Override
	public boolean intersects(Plane3D<?> p) {
		return p.intersects(getSegment1())
				|| p.intersects(getSegment2())
				|| p.intersects(getSegment3());
	}

	/** Replies the segment between P1 and P2.
	 *
	 * @return the segment between P1 and P2.
	 */
	public Segment3f getSegment1() {
		return new Segment3f(getP1(), getP2());
	}

	/** Replies the segment between P2 and P3.
	 *
	 * @return the segment between P2 and P3.
	 */
	public Segment3f getSegment2() {
		return new Segment3f(getP2(), getP3());
	}

	/** Replies the segment between P3 and P1.
	 *
	 * @return the segment between P3 and P1.
	 */
	public Segment3f getSegment3() {
		return new Segment3f(getP3(), getP1());
	}

	public static boolean overlapsCoplanarTriangle(Point3f point3f, Point3f point3f2, Point3f point3f3,
			Point3f point3f4, Point3f point3f5, Point3f point3f6) {
		// TODO Auto-generated method stub
		return false;
	}

}
