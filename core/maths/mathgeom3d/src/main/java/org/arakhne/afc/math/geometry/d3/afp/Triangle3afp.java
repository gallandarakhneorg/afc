/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d3.afp;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.BoundsReceiver3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Plane3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/** Functional interface that represented a 3D triangle.
 *
 * @param <ST> is the general type of all the shapes.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings({"checkstyle:methodcount", "checkstyle:magicnumber"})
public interface Triangle3afp<
		ST extends Shape3afp<?, IE, P, V, Q, B>,
		IT extends Triangle3afp<?, ?, IE, P, V, Q, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends AlignedBox3afp<?, IE, P, V, Q, B>>
	extends TransformableShape3afp<ST, IT, IE, P, V, Q, B> {

	@SuppressWarnings({"checkstyle:booleanexpressioncomplexity", "checkstyle:cyclomaticcomplexity"})
	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		if (shape == this) {
			return true;
		}

		final var ax = getX1();
		final var ay = getY1();
		final var az = getZ1();
		final var bx = getX2();
		final var by = getY2();
		final var bz = getZ2();
		final var cx = getX3();
		final var cy = getY3();
		final var cz = getZ3();

		final var ox1 = shape.getX1();
		final var oy1 = shape.getY1();
		final var oz1 = shape.getZ1();
		final var ox2 = shape.getX2();
		final var oy2 = shape.getY2();
		final var oz2 = shape.getZ2();
		final var ox3 = shape.getX3();
		final var oy3 = shape.getY3();
		final var oz3 = shape.getZ3();

		// Order-independent equality: same 3 vertices in any permutation.
		return
			// A -> O1
			ax == ox1 && ay == oy1 && az == oz1
				&& bx == ox2 && by == oy2 && bz == oz2
				&& cx == ox3 && cy == oy3 && cz == oz3
			|| ax == ox1 && ay == oy1 && az == oz1
				&& bx == ox3 && by == oy3 && bz == oz3
				&& cx == ox2 && cy == oy2 && cz == oz2

			// A -> O2
			|| ax == ox2 && ay == oy2 && az == oz2
				&& bx == ox1 && by == oy1 && bz == oz1
				&& cx == ox3 && cy == oy3 && cz == oz3
			|| ax == ox2 && ay == oy2 && az == oz2
				&& bx == ox3 && by == oy3 && bz == oz3
				&& cx == ox1 && cy == oy1 && cz == oz1

			// A -> O3
			|| ax == ox3 && ay == oy3 && az == oz3
				&& bx == ox1 && by == oy1 && bz == oz1
				&& cx == ox2 && cy == oy2 && cz == oz2
			|| ax == ox3 && ay == oy3 && az == oz3
				&& bx == ox2 && by == oy2 && bz == oz2
				&& cx == ox1 && cy == oy1 && cz == oz1;
	}

	/** Replies if the triangle intersects the aligned box.
	 *
	 * <a href="https://fileadmin.cs.lth.se/cs/Personal/Tomas_Akenine-Moller/code/tribox_tam.pdf">Tomas Akenine-Moller.
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
	 * @return {@code true} if the triangle and aligned box are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Unefficient
	@Pure
	static boolean intersectsTriangleAlignedBox(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double minx, double miny, double minz,
			double maxx, double maxy, double maxz) {
		return MollerAlgorithmTools.intersectsTriangleAlignedBox(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				minx, miny, minz,
				maxx, maxy, maxz);
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
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
	@Pure
	static void findsClosestPointTrianglePoint(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double px, double py, double pz,
			Point3D<?, ?, ?> closestPoint) {
		EricsonAlgorithmTools.findsClosestPointTrianglePoint(tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, px, py, pz, closestPoint);
	}

	/** Replies the closest point from the triangle to the segment.
	 * The closest point is always located in the triangle.
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
	 * @param epsilon the epsilon value that is used for testing equalities.
	 * @param closestPointOnTriangle the point on the triangle set with the
	 *     closest coordinates. It could be {@code null}.
	 * @param closestPointOnSegment the point on the segment set with the
	 *     closest coordinates. It could be {@code null}.
	 */
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
	@Pure
	static void findsClosestPointTriangleSegment(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			double epsilon,
			Point3D<?, ?, ?> closestPointOnTriangle,
			Point3D<?, ?, ?> closestPointOnSegment) {
		EricsonAlgorithmTools.findsClosestPointTriangleSegment(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				sx1, sy1, sz1,
				sx2, sy2, sz2,
				epsilon,
				closestPointOnTriangle,
				closestPointOnSegment);
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
	 * @param epsilon the epsilon value that is used for testing inequalities.
	 * @return the squared distance.
	 * @see "https://github.com/juj/MathGeoLib"
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Pure
	@Unefficient
	static double calculatesDistanceSquaredTriangleSegment(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			double epsilon) {
		final var tr = new InnerComputationPoint3D();
		final var sg = new InnerComputationPoint3D();
		findsClosestPointTriangleSegment(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				sx1, sy1, sz1,
				sx2, sy2, sz2,
				epsilon,
				tr, sg);
		return tr.getDistanceSquared(sg);
	}

	/** Replies if two coplanar triangles intersect.
	 * Triangles intersect even if they are connected by two of their
	 * edges.
	 *
	 * <p><a href="https://fileadmin.cs.lth.se/cs/Personal/Tomas_Akenine-Moller/pubs/tritri.pdf">Triangle/triangle
	 * intersection test routine, by Tomas Moller, 1997.
	 * See article "A Fast Triangle-Triangle Intersection Test",
	 * Journal of Graphics Tools, 2(2), 1997.</a>
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
	 * @return {@code true} if the two triangles are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Pure
	static boolean intersectsCoplanarTriangleTriangle(
			double v1x, double v1y, double v1z,
			double v2x, double v2y, double v2z,
			double v3x, double v3y, double v3z,
			double u1x, double u1y, double u1z,
			double u2x, double u2y, double u2z,
			double u3x, double u3y, double u3z) {
		return MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
				v1x, v1y, v1z, v2x, v2y, v2z, v3x, v3y, v3z,
				u1x, u1y, u1z, u2x, u2y, u2z, u3x, u3y, u3z);
	}

	/** Replies if the triangle intersects the sphere.
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
	 * @return {@code true} if the triangle and sphere are intersecting.
	 */
	@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:parameternumber"})
	@Unefficient
	@Pure
	static boolean intersectsTriangleSphere(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double centerx, double centery, double centerz,
			double radius) {
		final var point = new InnerComputationPoint3D();
		findsClosestPointTrianglePoint(tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, centerx, centery, centerz, point);
		final var distance = Point3D.getDistanceSquaredPointPoint(
				centerx, centery, centerz,
				point.getX(), point.getY(), point.getZ());
		return distance <= radius * radius;
	}

	/** Replies if the triangle intersects the segment.
	 *
	 * <p><a href="https://doi.org/10.1016/j.comgeo.2009.10.001">Juan J. Jimenez,
	 * Rafael J. Segura, Francisco R. Feito.
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
	 * @return {@code true} if the triangle and segment are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Pure
	static boolean intersectsTriangleSegment(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2) {
		final var factor = JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				sx1, sy1, sz1,
				sx2, sy2, sz2,
				GeomConstants.UNIT_VECTOR_EPSILON);
		return !Double.isNaN(factor);
	}

	/**
	 * Tests if the point {@code (px,py,pz)}
	 * lies inside a 3D triangle
	 * given by {@code (x1,y1,z1)}, {@code (x2,y2,z2)}
	 * and {@code (x3,y3,z3)} points.
	 *
	 * <p><strong>Caution: Tests are "epsiloned."</strong>
	 *
	 * <p>Parameter <var>forceCoplanar</var> has a deep influence on the function
	 * result. It indicates if coplanarity test must be done or not.
	 * Following table explains this influence:
	 *
	 * <table border="1" width="100%">
	 * <thead>
	 * <tr>
	 * <tr>Point is coplanar?</tr>
	 * <tr>Point projection on plane is inside triangle?</tr>
	 * <tr><var>forceCoplanar</var></tr>
	 * <tr>{@code intersectsPointTrangle()} Result</tr>
	 * </tr>
	 * </thead>
	 * <tbody>
	 * <tr>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * </tr>
	 * <tr>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * <td>{@code true}</td>
	 * </tr>
	 * <tr>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * </tr>
	 * <tr>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * <td>{@code false}</td>
	 * <td>{@code false}</td>
	 * </tr>
	 * <tr>
	 * <td>{@code false}</td>
	 * <td>{@code true}</td>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * </tr>
	 * <tr>
	 * <td>{@code false}</td>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * <td>{@code true}</td>
	 * </tr>
	 * <tr>
	 * <td>{@code false}</td>
	 * <td>{@code false}</td>
	 * <td>{@code true}</td>
	 * <td>{@code false}</td>
	 * </tr>
	 * <tr>
	 * <td>{@code false}</td>
	 * <td>{@code false}</td>
	 * <td>{@code false}</td>
	 * <td>{@code false}</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * <p><strong>Trigonometric Method (Slowest)</strong>
	 *
	 * <p>A common way to check if a point is in a triangle is to
	 * find the vectors connecting the point to each of the
	 * triangle's three vertices and sum the angles between
	 * those vectors. If the sum of the angles is 2*pi
	 * then the point is inside the triangle, otherwise it
	 * is not. <em>It works, but it is very slow.</em>
	 *
	 * <p>
	 * The advantage of the method above is that it's very simple to understand so that once
	 * you read it you should be able to remember it forever and code it up at
	 * any time without having to refer back to anything.
	 *
	 * <p><strong>Barycenric Method (Fastest)</strong>
	 *
	 * <p>There's another method that is also as easy conceptually but executes faster.
	 * The downside is there's a little more math involved, but once you see
	 * it worked out it should be no problem.
	 *
	 * <p>So remember that the three points of the triangle define a plane in space.
	 * Pick one of the points and we can consider all other locations on the plane
	 * as relative to that point. Let's select A -- it'll be our origin on the
	 * plane. Now what we need are basis vectors so we can give coordinate
	 * values to all the locations on the plane.
	 * We'll pick the two edges of the triangle that touch A,
	 * (C - A) and (B - A).
	 * Now we can get to any point on the plane just by starting at A
	 * and walking some distance along (C - A) and then from there walking
	 * some more in the direction (B - A).
	 *
	 * <p>With that in mind we can now describe any point on the plane as:<br>
	 * P = A + u * (C - A) + v * (B - A)
	 *
	 * <p>Notice now that if u or v < 0 then we've walked in the wrong direction
	 * and must be outside the triangle. Also if u or v > 1 then we've
	 * walked too far in a direction and are outside the triangle.
	 * Finally if u + v > 1 then we've crossed the edge BC again leaving the triangle.
	 *
	 * <p>Given u and v we can easily calculate the point P with the above
	 * equation, but how can we go in the reverse direction and calculate
	 * u and v from a given point P?<br>
	 * P = A + u * (C - A) + v * (B - A)       // Original equation<br>
	 * (P - A) = u * (C - A) + v * (B - A)     // Subtract A from both sides<br>
	 * v2 = u * v0 + v * v1                    // Substitute v0, v1, v2 for less writing
	 *
	 * <p>We have two unknowns (u and v) so we need two equations to solve
	 * for them.  Dot both sides by v0 to get one and dot both sides by
	 * v1 to get a second.<br>
	 * (v2) . v0 = (u * v0 + v * v1) . v0<br>
	 * (v2) . v1 = (u * v0 + v * v1) . v1<br>
	 *
	 * <p>Distribute v0 and v1<br>
	 * v2 . v0 = u * (v0 . v0) + v * (v1 . v0)<br>
	 * v2 . v1 = u * (v0 . v1) + v * (v1 . v1)
	 *
	 * <p>Now we have two equations and two unknowns and can solve one
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
	 * @param forceCoplanar is {@code true} to force to test
	 *     to consider the given point is coplanar to the triangle, {@code false}
	 *     to not consider coplanarity of the point.
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters
	 * @return {@code true} if the points is coplanar - or not,
	 *     depending on <var>forceCoplanar</var> - to the triangle and
	 *     lies inside it, otherwise {@code false}
	 */
	@Unefficient
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean containsTrianglePoint(
			double ax, double ay, double az,
			double bx, double by, double bz,
			double cx, double cy, double cz,
			double px, double py, double pz,
			boolean forceCoplanar, double epsilon) {
		return IntersectionTools.containsTrianglePoint(ax, ay, az, bx, by, bz, cx, cy, cz, px, py, pz, forceCoplanar, epsilon);
	}

	@Override
	default Shape3DType getType() {
		return Shape3DType.TRIANGLE;
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
		return containsTrianglePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				x, y, z,
				true, GeomConstants.DISTANCE_EPSILON);
	}

	@Override
	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		return box.isDegeneratedPoint() && contains(box.getMinX(), box.getMinY(), box.getMinZ());
	}

	/** Change the coordinates of the three points of the triangle.
	 *
     * @param x1 x coordinate of the first point.
     * @param y1 y coordinate of the first point.
     * @param z1 z coordinate of the first point.
     * @param x2 x coordinate of the second point.
     * @param y2 y coordinate of the second point.
     * @param z2 z coordinate of the second point.
     * @param x3 x coordinate of the third point.
     * @param y3 y coordinate of the third point.
     * @param z3 z coordinate of the third point.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	void set(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3);

	/** Change the coordinates of the three points of the triangle.
	 *
    * @param p1 the first point.
    * @param p2 the second point.
    * @param p3 the third point.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	default void set(Point3D<?, ?, ?> p1, Point3D<?, ?, ?> p2, Point3D<?, ?, ?> p3) {
		assert p1 != null : AssertMessages.notNullParameter(0);
		assert p2 != null : AssertMessages.notNullParameter(1);
		assert p3 != null : AssertMessages.notNullParameter(2);
		set(
				p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(),
				p3.getX(), p3.getY(), p3.getZ());
	}

	@Override
	default void set(IT triangle) {
		assert triangle != null : AssertMessages.notNullParameter();
		set(
				triangle.getX1(), triangle.getY1(), triangle.getZ1(),
				triangle.getX2(), triangle.getY2(), triangle.getZ2(),
				triangle.getX3(), triangle.getY3(), triangle.getZ3());
	}

	/** Replies the plane on which this triangle is coplanar.
	 *
	 * @return the coplanar plane to this triangle
	 */
	@Pure
	default Plane3D<?, ?, P, V, Q> getPlane() {
		return getGeomFactory().newPlane(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3());
	}

	@Override
	default void clear() {
		set(0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	/**
	 * Replies the first point of the triangle.
	 *
	 * @return the first point of the triangle.
	 */
	@Pure
	default P getP1() {
		return getGeomFactory().newPoint(getX1(), getY1(), getZ1());
	}

	/**
	 * Change the first point of the triangle.
	 *
	 * @param point the point.
	 */
	default void setP1(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		setP1(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Change the first point of the triangle.
	 *
	 * @param x x coordinate of the new point.
	 * @param y y coordinate of the new point.
	 * @param z z coordinate of the new point.
	 */
	void setP1(double x, double y, double z);

	/**
	 * Replies the second point of the triangle.
	 *
	 * @return the second point of the triangle.
	 */
	@Pure
	default P getP2() {
		return getGeomFactory().newPoint(getX2(), getY2(), getZ2());
	}

	/**
	 * Change the second point of the triangle.
	 *
	 * @param point the point.
	 */
	default void setP2(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		setP2(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Change the second point of the triangle.
	 *
	 * @param x x coordinate of the new point.
	 * @param y y coordinate of the new point.
	 * @param z z coordinate of the new point.
	 */
	void setP2(double x, double y, double z);

	/**
	 * Replies the third point of the triangle.
	 *
	 * @return the third point of the triangle.
	 */
	@Pure
	default P getP3() {
		return getGeomFactory().newPoint(getX3(), getY3(), getZ3());
	}

	/**
	 * Change the third point of the triangle.
	 *
	 * @param point the point.
	 */
	default void setP3(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		setP3(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Change the third point of the triangle.
	 *
	 * @param x x coordinate of the new point.
	 * @param y y coordinate of the new point.
	 * @param z z coordinate of the new point.
	 */
	void setP3(double x, double y, double z);

	/** Replies the x coordinate of the first point.
	 *
	 * @return the x coordinate of the first point.
	 */
	@Pure
	double getX1();

	/** Change the x coordinate of the first point.
	 *
	 * @param x the x coordinate of the first point.
	 */
	void setX1(double x);

	/** Replies the y coordinate of the first point.
	 *
	 * @return the y coordinate of the first point.
	 */
	@Pure
	double getY1();

	/** Change the y coordinate of the first point.
	 *
	 * @param y the y coordinate of the first point.
	 */
	void setY1(double y);

	/** Replies the z coordinate of the first point.
	 *
	 * @return the z coordinate of the first point.
	 */
	@Pure
	double getZ1();

	/** Change the z coordinate of the first point.
	 *
	 * @param z the z coordinate of the first point.
	 */
	void setZ1(double z);

	/** Replies the x coordinate of the second point.
	 *
	 * @return the x coordinate of the second point.
	 */
	@Pure
	double getX2();

	/** Change the x coordinate of the second point.
	 *
	 * @param x the x coordinate of the second point.
	 */
	void setX2(double x);

	/** Replies the y coordinate of the second point.
	 *
	 * @return the y coordinate of the second point.
	 */
	@Pure
	double getY2();

	/** Change the y coordinate of the second point.
	 *
	 * @param y the y coordinate of the second point.
	 */
	void setY2(double y);

	/** Replies the z coordinate of the second point.
	 *
	 * @return the z coordinate of the second point.
	 */
	@Pure
	double getZ2();

	/** Change the z coordinate of the second point.
	 *
	 * @param z the z coordinate of the second point.
	 */
	void setZ2(double z);

	/** Replies the x coordinate of the third point.
	 *
	 * @return the x coordinate of the third point.
	 */
	@Pure
	double getX3();

	/** Change the x coordinate of the third point.
	 *
	 * @param x the x coordinate of the third point.
	 */
	void setX3(double x);

	/** Replies the y coordinate of the third point.
	 *
	 * @return the y coordinate of the third point.
	 */
	@Pure
	double getY3();

	/** Change the y coordinate of the third point.
	 *
	 * @param y the y coordinate of the third point.
	 */
	void setY3(double y);

	/** Replies the z coordinate of the third point.
	 *
	 * @return the z coordinate of the third point.
	 */
	@Pure
	double getZ3();

	/** Change the z coordinate of the third point.
	 *
	 * @param z the z coordinate of the third point.
	 */
	void setZ3(double z);

	/** Replies the segment between P1 and P2.
	 *
	 * @return the segment between P1 and P2.
	 */
	@Pure
	default Segment3afp<?, ?, IE, P, V, Q, B> getS1() {
		return getGeomFactory().newSegment(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	/** Replies the segment between P2 and P3.
	 *
	 * @return the segment between P2 and P3.
	 */
	@Pure
	default Segment3afp<?, ?, IE, P, V, Q, B> getS2() {
		return getGeomFactory().newSegment(getX2(), getY2(), getZ2(), getX3(), getY3(), getZ3());
	}

	/** Replies the segment between P3 and P1.
	 *
	 * @return the segment between P3 and P1.
	 */
	@Pure
	default Segment3afp<?, ?, IE, P, V, Q, B> getS3() {
		return getGeomFactory().newSegment(getX3(), getY3(), getZ3(), getX1(), getY1(), getZ1());
	}

	@Pure
	@Override
	default String toGeogebra() {
		final var buffer = new StringBuilder();
		buffer.append("(") //$NON-NLS-1$
			.append(getX1()).append(",") //$NON-NLS-1$
			.append(getY1()).append(",") //$NON-NLS-1$
			.append(getZ1()).append("),(") //$NON-NLS-1$
			.append(getX2()).append(",") //$NON-NLS-1$
			.append(getY2()).append(",") //$NON-NLS-1$
			.append(getZ2()).append("),(") //$NON-NLS-1$
			.append(getX3()).append(",") //$NON-NLS-1$
			.append(getY3()).append(",") //$NON-NLS-1$
			.append(getZ3()).append(")"); //$NON-NLS-1$
		return buffer.toString();
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?, ?> point) {
		final var c = getClosestPointTo(point);
		return c.getDistanceL1(point);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?, ?> point) {
		final var c = getClosestPointTo(point);
		return c.getDistanceLinf(point);
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		if (containsTrianglePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				point.getX(), point.getY(), point.getZ(),
				false, 0)) {
			final var n = getNormal();
			var dist = n.getX() * getX1() + n.getY() * getY1() + n.getZ() * getZ1();
			dist = n.getX() * point.getX() + n.getY() * point.getY() + n.getZ() * point.getZ() - dist;
			return dist * dist;
		}
		final var d1 = Segment3afp.calculatesDistanceSquaredSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				point.getX(), point.getY(), point.getZ());
		final var d2 = Segment3afp.calculatesDistanceSquaredSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX3(), getY3(), getZ3(),
				point.getX(), point.getY(), point.getZ());
		final var d3 = Segment3afp.calculatesDistanceSquaredSegmentPoint(
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				point.getX(), point.getY(), point.getZ());
		return MathUtil.min(d1, d2, d3);
	}

	/** Replies the normal vector associated with this triangle.
	 *
	 * @return the normal vector.
	 */
	@Pure
	V getNormal();

	/** Flip the normal of the triangle. This function exchanges P2 and P3 coordinates.
	 */
	default void flipNormal() {
		set(
				getX1(), getY1(), getZ1(),
				getX3(), getY3(), getZ3(),
				getX2(), getY2(), getZ2());
	}

	/**
	 * Replies the orientation of the triangle. The orientation is the rotation from
	 * the up vector to the normal of the triangle.
	 *
	 * @return the orientation.
	 */
	@Pure
	Q getOrientation();

	/**
	 * Change the orientation of the triangle according to the up vector of the given
	 * coordinate system.
	 *
	 * @param system the coordinate system to use.
	 */
	void setOrientationFromCoordinateSystem(CoordinateSystem3D system);

	/** Replies the pivot point associated to this triangle.
	 * The default pivot point is (0, 0, 0).
	 *
	 * @return the pivot point or {@code null} if there is no pivot point specified.
	 */
	@Pure
	Point3D<?, ?, ?> getPivot();

	/** Change the pivot point for this triangle.
	 *
	 * @param x x coordinate of the new pivot point.
	 * @param y y coordinate of the new pivot point.
	 * @param z z coordinate of the new pivot point.
	 */
	void setPivot(double x, double y, double z);

	/** Change the pivot point for this triangle.
	 *
	 * @param point is the new pivot point. If {@code null}, the default pivot point
	 *     is restored to the origin.
	 */
	void setPivot(Point3D<?, ?, ?> point);

	@Override
	default void transform(Transform3D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final var pt = new InnerComputationPoint3D(getX1(), getY1(), getZ1());
		transform.transform(pt);
		final var x1 = pt.getX();
		final var y1 = pt.getY();
		final var z1 = pt.getZ();

		pt.set(getX2(), getY2(), getZ2());
		transform.transform(pt);
		final var x2 = pt.getX();
		final var y2 = pt.getY();
		final var z2 = pt.getZ();

		pt.set(getX3(), getY3(), getZ3());
		transform.transform(pt);
		final var x3 = pt.getX();
		final var y3 = pt.getY();
		final var z3 = pt.getZ();

		final var pivot = getPivot();
		if (pivot != null) {
			pt.set(pivot);
			transform.transform(pt);
			setPivot(pt.getX(), pt.getY(), pt.getZ());
		}
		set(x1, y1, z1, x2, y2, z2, x3, y3, z3);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	default ST createTransformedShape(Transform3D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final var p1 = new InnerComputationPoint3D(getX1(), getY1(), getZ1());
		transform.transform(p1);
		final var p2 = new InnerComputationPoint3D(getX2(), getY2(), getZ2());
		transform.transform(p2);
		final var p3 = new InnerComputationPoint3D(getX3(), getY3(), getZ3());
		transform.transform(p3);
		final var shape = getGeomFactory().newTriangle(
				p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(),
				p3.getX(), p3.getY(), p3.getZ());
		final var pivot = getPivot();
		if (pivot != null) {
			final var pt = new InnerComputationPoint3D(pivot);
			transform.transform(pt);
			shape.setPivot(pt.getX(), pt.getY(), pt.getZ());
		}
		return (ST) shape;
	}

	@Override
	default void translate(double dx, double dy, double dz) {
		final var x1 = getX1() + dx;
		final var y1 = getY1() + dy;
		final var z1 = getZ1() + dz;
		final var x2 = getX2() + dx;
		final var y2 = getY2() + dy;
		final var z2 = getZ2() + dz;
		final var x3 = getX3() + dx;
		final var y3 = getY3() + dy;
		final var z3 = getZ3() + dz;
		final var pivot = getPivot();
		if (pivot != null) {
			setPivot(pivot.getX() + dx, pivot.getY() + dy, pivot.getZ() + dz);
		}
		set(x1, y1, z1, x2, y2, z2, x3, y3, z3);
	}

	/** Rotate the triangle around its pivot point.
	 * By default, the pivot point is the first point of the triangle.
	 *
	 * @param rotation the rotation.
	 * @see #getPivot()
	 * @see #getP1()
	 */
	default void rotate(Quaternion<?, ?, ?> rotation) {
		assert rotation != null : AssertMessages.notNullParameter(0);
		TransformTools.rotateAroundOrigin(this, rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW());
	}

	/** Rotate the segment around a given pivot point.
	 * The default pivot point of the segment is its first point.
	 *
	 * @param rotation the rotation.
	 * @param pivot the pivot point. If {@code null} the triangle's point is used.
	 * @see #getPivot()
	 * @see #getP1()
	 */
	default void rotate(Quaternion<?, ?, ?> rotation, Point3D<?, ?, ?> pivot) {
		assert rotation != null : AssertMessages.notNullParameter(0);
		if (pivot == null) {
			final var piv = getPivot();
			if (piv == null) {
				TransformTools.rotateAroundOrigin(
						this,
						rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW());
			} else {
				TransformTools.rotateAroundPivot(
						this,
						rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW(),
						piv.getX(), piv.getY(), piv.getZ());
			}
		} else {
			TransformTools.rotateAroundPivot(
					this,
					rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW(),
					pivot.getX(), pivot.getY(), pivot.getZ());
		}
	}

	@Pure
	@Override
	default B toBoundingBox() {
		final var box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}

	@Pure
	@Override
	default void toBoundingBox(BoundsReceiver3D box) {
		assert box != null : AssertMessages.notNullParameter();
		final var rangex = MathUtil.getMinMax(getX1(), getX2(), getX3());
		final var rangey = MathUtil.getMinMax(getY1(), getY2(), getY3());
		final var rangez = MathUtil.getMinMax(getZ1(), getZ2(), getZ3());
		box.setFromCorners(
				rangex.getMin(), rangey.getMin(), rangez.getMin(),
				rangex.getMax(), rangey.getMax(), rangez.getMax());
	}

	/**
	 * Checks if the projection of a point on the triangle's plane is inside the triangle.
	 *
	 * @param point is the the point to project on the triangle's plane.
	 * @return {@code true} if the projection of the point is in the triangle, otherwise {@code false}.
	 * @see #getPlane()
	 */
	@Pure
	default boolean containsProjectionOf(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return containsProjectionOf(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Checks if the projection of a point on the triangle's plane is inside the triangle.
	 *
	 * @param x x coordinate of the point to project on the triangle's plane.
	 * @param y y coordinate of the point to project on the triangle's plane.
	 * @param z z coordinate of the point to project on the triangle's plane.
	 * @return {@code true} if the projection of the point is in the triangle, otherwise {@code false}.
	 * @see #getPlane()
	 */
	@Pure
	default boolean containsProjectionOf(double x, double y, double z) {
		final var proj = getPlane().getProjection(x, y, z);
		if (proj == null) {
			return false;
		}
		return contains(proj);
	}

	@Pure
	@Override
	default boolean isEmpty() {
		// A triangle is empty iff its 3 points are collinear (area == 0),
		// including special cases where 2 or 3 points are identical.
		final var ay = getY1();
		final var az = getZ1();
		final var by = getY2();
		final var bz = getZ2();
		final var cy = getY3();
		final var cz = getZ3();

		// AB = B - A
		final var aby = by - ay;
		final var abz = bz - az;
		// AC = C - A
		final var acy = cy - ay;
		final var acz = cz - az;
		// |AB x AC|^2 (proportional to squared area)
		final var nx = aby * acz - abz * acy;

		if (nx != 0.) {
			return false;
		}

		final var ax = getX1();
		final var bx = getX2();
		final var cx = getX3();

		// AB = B - A
		final var abx = bx - ax;
		// AC = C - A
		final var acx = cx - ax;
		// |AB x AC|^2 (proportional to squared area)
		final var ny = abz * acx - abx * acz;

		if (ny != 0.) {
			return false;
		}

		final var nz = abx * acy - aby * acx;

		return nz == 0.;
	}

	@Pure
	@Override
	default boolean isDegeneratedPoint() {
		return getX1() == getX2() && getX1() == getX3()
				&& getY1() == getY2() && getY1() == getY3()
				&& getZ1() == getZ2() && getZ1() == getZ3();
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var c = getGeomFactory().newPoint();
		findsClosestPointTrianglePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				point.getX(), point.getY(), point.getZ(),
				c);
		return c;
	}

	@Pure
	@Override
	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		final var c = getGeomFactory().newPoint();
		findsClosestPointTrianglePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				sphere.getX(), sphere.getY(), sphere.getZ(),
				c);
		return c;
	}

	@Pure
	@Override
	default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox) {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		findsClosestPointTriangleSegment(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2(),
				GeomConstants.DISTANCE_EPSILON,
				point, null);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	default P getClosestPointTo(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> path) {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		final var px = point.getX();
		final var py = point.getY();
		final var pz = point.getZ();
		final var d1 = Point3D.getDistanceSquaredPointPoint(getX1(), getY1(), getZ1(), px, py, pz);
		final var d2 = Point3D.getDistanceSquaredPointPoint(getX2(), getY2(), getZ2(), px, py, pz);
		final var d3 = Point3D.getDistanceSquaredPointPoint(getX3(), getY3(), getZ3(), px, py, pz);
		if (d1 >= d2) {
			if (d3 >= d1) {
				return getP3();
			}
			return getP1();
		} else if (d3 >= d2) {
			return getP3();
		}
		return getP2();
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return intersectsTriangleSphere(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				sphere.getX(), sphere.getY(), sphere.getZ(),
				sphere.getRadius());
	}

	@Pure
	@Override
	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		return intersectsTriangleAlignedBox(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				prism.getMinX(), prism.getMinY(), prism.getMinZ(),
				prism.getMaxX(), prism.getMaxY(), prism.getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return intersectsTriangleSegment(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
	}

	@Pure
	@Override
	default boolean intersects(Triangle3afp<?, ?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null :  AssertMessages.notNullParameter();
		return intersectsCoplanarTriangleTriangle(
				getX1(), getY1(), getY1(),
				getX2(), getY2(), getY3(),
				getX3(), getY3(), getY3(),
				triangle.getX1(), triangle.getY1(), triangle.getZ1(),
				triangle.getX2(), triangle.getY2(), triangle.getZ2(),
				triangle.getX3(), triangle.getY3(), triangle.getZ3());
	}

	@Pure
	@Override
	default boolean intersects(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		//TODO: return Path3afp.intersectsPathIteratorTriangle();
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		//TODO: return Path3afp.intersectsPathIteratorTriangle();
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		//TODO: return MultiShape3afp.intersectsMultiShapeTriangle();
		throw new UnsupportedOperationException();
	}

	/** Tools for transforms.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	final class TransformTools {

		private TransformTools() {
			//
		}

		/** Rotate the triangle around the origin point.
		 * The quaternion must be not nul and normalized.
		 *
		 * @param triangle the triangle to rotate.
		 * @param x x coordinate for the rotation quaternion.
		 * @param y y coordinate for the rotation quaternion.
		 * @param z z coordinate for the rotation quaternion.
		 * @param w w coordinate for the rotation quaternion.
		 */
		public static void rotateAroundOrigin(Triangle3afp<?, ?, ?, ?, ?, ?, ?> triangle,
				double x, double y, double z, double w) {
			assert triangle != null : AssertMessages.notNullParameter(0);
			assert x != 0. || y != 0. || z != 0. || w != 0. : AssertMessages.invalidValue(1);
			assert MathUtil.isEpsilonEqual(1., x * x + y * y + z * z + w * w, GeomConstants.DISTANCE_EPSILON)
				: AssertMessages.normalizedParameter(1);
			final var tr = new Transform3D();
			tr.setRotation(x, y, z, w);

			final var v = new InnerComputationVector3D();

			v.set(triangle.getX1(), triangle.getY1(), triangle.getZ1());
			tr.transform(v);
			final var x1 = v.getX();
			final var y1 = v.getY();
			final var z1 = v.getZ();

			v.set(triangle.getX2(), triangle.getY2(), triangle.getZ2());
			tr.transform(v);
			final var x2 = v.getX();
			final var y2 = v.getY();
			final var z2 = v.getZ();

			v.set(triangle.getX3(), triangle.getY3(), triangle.getZ3());
			tr.transform(v);
			final var x3 = v.getX();
			final var y3 = v.getY();
			final var z3 = v.getZ();

			triangle.set(x1, y1, z1, x2, y2, z2, x3, y3, z3);
		}

		/** Rotate the triangle around the given pivot point.
		 * The quaternion must be not nul and normalized.
		 *
		 * @param triangle the triangle to rotate.
		 * @param qx x coordinate for the rotation quaternion.
		 * @param qy y coordinate for the rotation quaternion.
		 * @param qz z coordinate for the rotation quaternion.
		 * @param qw w coordinate for the rotation quaternion.
		 * @param px x coordinate of the pivot point.
		 * @param py y coordinate of the pivot point.
		 * @param pz z coordinate of the pivot point.
		 */
		public static void rotateAroundPivot(Triangle3afp<?, ?, ?, ?, ?, ?, ?> triangle,
				double qx, double qy, double qz, double qw,
				double px, double py, double pz) {
			assert triangle != null : AssertMessages.notNullParameter(0);
			assert qx != 0. || qy != 0. || qz != 0. || qw != 0. : AssertMessages.invalidValue(1);
			assert MathUtil.isEpsilonEqual(1., qx * qx + qy * qy + qz * qz + qw * qw, GeomConstants.DISTANCE_EPSILON)
				: AssertMessages.normalizedParameter(1);
			final var tr = new Transform3D();
			tr.setRotation(qx, qy, qz, qw);

			final var v = new InnerComputationVector3D();

			v.set(triangle.getX1() - px, triangle.getY1() - py, triangle.getZ1() - pz);
			tr.transform(v);
			final var x1 = px + v.getX();
			final var y1 = py + v.getY();
			final var z1 = pz + v.getZ();

			v.set(triangle.getX2() - px, triangle.getY2() - py, triangle.getZ2() - pz);
			tr.transform(v);
			final var x2 = px + v.getX();
			final var y2 = py + v.getY();
			final var z2 = pz + v.getZ();

			v.set(triangle.getX3() - px, triangle.getY3() - py, triangle.getZ3() - pz);
			tr.transform(v);
			final var x3 = px + v.getX();
			final var y3 = py + v.getY();
			final var z3 = pz + v.getZ();

			triangle.set(x1, y1, z1, x2, y2, z2, x3, y3, z3);
		}
	}

	/** Utility class for the Badouel's algorithm.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	final class BadouelAlgorithmTools {

		private BadouelAlgorithmTools() {
			//
		}

		/** Replies the intersection factor of the given segment
		 * when it is intersecting the triangle.
		 *
		 * <p>This function implements a <strong>fixed version</strong> of the Badouel algorithm:
		 * D. Badouel, An efficient ray-polygon intersection, in: Graphics Gems, Academic Press, 1990.
		 * The original algorithm is summarized in the
		 * <a href="https://doi.org/10.1016/j.comgeo.2009.10.001">article of Jimenez</a>.
		 *
		 * <p>If the segment and the triangle are not intersecting, this
		 * function replies {@link Double#NaN}.
		 * If the segment and the triangle are intersecting,
		 * this function replies the factor of the line's equation that
		 * permits to retreive the intersection point from the segment definition.
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
		 * @param epsilon the epsilon value that is used for testing inequalities.
		 * @return the factor that permits to compute the intersection point,
		 *     {@link Double#NaN} when no intersection.
		 * @see JimenezAlgorithmTools#calculatesIntersectionFactorTriangleSegment(double, double, double, double, double, double, double,
		 *     double, double, double, double, double, double, double, double, double)
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:localfinalvariablename", "checkstyle:npathcomplexity"})
		@Pure
		public static double calculatesIntersectionFactorTriangleSegment(
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double sx1, double sy1, double sz1,
				double sx2, double sy2, double sz2,
				double epsilon) {
			// From original algorithm, input is: Triangle (V1, V2, V3); Segment(Q1, Q2)
			// E1 = V2 - V1
			final var E1_x = tx2 - tx1;
			final var E1_y = ty2 - ty1;
			final var E1_z = tz2 - tz1;
			// E2 = V3 - V1
			final var E2_x = tx3 - tx1;
			final var E2_y = ty3 - ty1;
			final var E2_z = tz3 - tz1;
			// N = E1 x E2
			final var N = new InnerComputationVector3D();
			Vector3D.crossProduct(
					E1_x, E1_y, E1_z,
					E2_x, E2_y, E2_z,
					N);
			// D = Q2 - Q1
			final var D_x = sx2 - sx1;
			final var D_y = sy2 - sy1;
			final var D_z = sz2 - sz1;
			// T = V1 - Q1
			final var T_x = tx1 - sx1;
			final var T_y = ty1 - sy1;
			final var T_z = tz1 - sz1;
			// det = N . D
			final var det = Vector3D.dotProduct(N.getX(), N.getY(), N.getZ(), D_x, D_y, D_z);
			// Rejection 1
			if (det > -epsilon && det < epsilon) {
				// Rejection 1: the segment and the triangle are parallel (including coplanar).
				// The problem should be solved in 2D in the triangle's plane.
				// The Badouel algorithm is adapted.
				//
				// The segment's direction is orthogonal to the triangle's normal,
				// i.e. the segment is parallel to the triangle's plane. That alone
				// does not mean the segment lies IN the plane: it could be parallel
				// to, but offset from, the plane. The actual coplanarity test is
				// whether a point of the segment satisfies the plane's equation:
				// N . (V1 - Q1) ~= 0.
				final var planeDistance = Vector3D.dotProduct(N.getX(), N.getY(), N.getZ(), T_x, T_y, T_z);
				if (planeDistance > -epsilon && planeDistance < epsilon) {
					return IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
							tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
							sx1, sy1, sz1, sx2, sy2, sz2,
							epsilon);
				}
				return Double.NaN;
			}
			// t_param = N . T / det
			final var t_param = Vector3D.dotProduct(N.getX(), N.getY(), N.getZ(), T_x, T_y, T_z) / det;
			// Rejection 2
			if (t_param < 0. || t_param > 1.) {
				return Double.NaN;
			}

			final double Q1_j;
			final double Q1_k;
			final double D_j;
			final double D_k;
			final double V1_j;
			final double V1_k;
			final double V2_j;
			final double V2_k;
			final double V3_j;
			final double V3_k;

			if (N.getX() > N.getY()) {
				if (N.getX() > N.getZ()) {
					// j=y, k=z
					Q1_j = sy1;
					Q1_k = sz1;
					D_j = D_y;
					D_k = D_z;
					V1_j = ty1;
					V1_k = tz1;
					V2_j = ty2;
					V2_k = tz2;
					V3_j = ty3;
					V3_k = tz3;
				} else {
					// j=x, k=y
					Q1_j = sx1;
					Q1_k = sy1;
					D_j = D_x;
					D_k = D_y;
					V1_j = tx1;
					V1_k = ty1;
					V2_j = tx2;
					V2_k = ty2;
					V3_j = tx3;
					V3_k = ty3;
				}
			} else {
				if (N.getY() < N.getZ()) {
					// j=x, k=y
					Q1_j = sx1;
					Q1_k = sy1;
					D_j = D_x;
					D_k = D_y;
					V1_j = tx1;
					V1_k = ty1;
					V2_j = tx2;
					V2_k = ty2;
					V3_j = tx3;
					V3_k = ty3;
				} else {
					// j=x, k=z
					Q1_j = sx1;
					Q1_k = sz1;
					D_j = D_x;
					D_k = D_z;
					V1_j = tx1;
					V1_k = tz1;
					V2_j = tx2;
					V2_k = tz2;
					V3_j = tx3;
					V3_k = tz3;
				}
			}

			final var P_j = Q1_j + D_j * t_param;
			final var P_k = Q1_k + D_k * t_param;
			final var u1 = P_j - V1_j;
			final var v1 = P_k - V1_k;
			final var u2 = V2_j - V1_j;
			final var u3 = V3_j - V1_j;
			final var v2 = V2_k - V1_k;
			final var v3 = V3_k - V1_k;

			final double alpha;
			final double beta;
			if (u2 > -epsilon && u2 < epsilon) {
				beta = u1 / u3;
				// Rejection 3
				if (beta < 0. || beta > 1.) {
					return Double.NaN;
				}
				alpha = (v1 - beta * v3) / v2;
			} else {
				beta = (v1 * u2 - u1 * v2) / (v3 * u2 - u3 * v2);
				// Rejection 3
				if (beta < 0. || beta > 1.) {
					return Double.NaN;
				}
				alpha = (u1 - beta * u3) / u2;
			}

			// Rejection 4
			if (alpha < 0. || alpha + beta > 1.) {
				return Double.NaN;
			}

			return t_param;
		}
	}

	/** Utility class for the Moller's algorithm.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	final class MollerAlgorithmTools {

		private MollerAlgorithmTools() {
			//
		}

		/** Replies if the triangle intersects the aligned box.
		 *
		 * <a href="https://fileadmin.cs.lth.se/cs/Personal/Tomas_Akenine-Moller/code/tribox_tam.pdf">Tomas Akenine-Moller.
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
		 * @return {@code true} if the triangle and aligned box are intersecting.
		 */
		@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:returncount", "checkstyle:parameternumber"})
		@Pure
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

			final var halfsizex = (maxx - minx) / 2.;
			final var halfsizey = (maxy - miny) / 2.;
			final var halfsizez = (maxz - minz) / 2.;

			final var boxcenterx = minx + halfsizex;
			final var boxcentery = miny + halfsizey;
			final var boxcenterz = minz + halfsizez;

			// This is the fastest branch on Sun
			// move everything so that the boxcenter is in (0,0,0)
			final var v0x = tx1 - boxcenterx;
			final var v0y = ty1 - boxcentery;
			final var v0z = tz1 - boxcenterz;
			final var v1x = tx2 - boxcenterx;
			final var v1y = ty2 - boxcentery;
			final var v1z = tz2 - boxcenterz;
			final var v2x = tx3 - boxcenterx;
			final var v2y = ty3 - boxcentery;
			final var v2z = tz3 - boxcenterz;

			// compute triangle edges
			final var e0x = v1x - v0x;
			final var e0y = v1y - v0y;
			final var e0z = v1z - v0z;
			final var e1x = v2x - v1x;
			final var e1y = v2y - v1y;
			final var e1z = v2z - v1z;
			final var e2x = v0x - v2x;
			final var e2y = v0y - v2y;
			final var e2z = v0z - v2z;


			// Bullet 3:
			// test the 9 tests first (this was faster)
			var fex = Math.abs(e0x);
			var fey = Math.abs(e0y);
			var fez = Math.abs(e0z);

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

			// test in X-direction
			var min = MathUtil.min(v0x, v1x, v2x);
			var max = MathUtil.max(v0x, v1x, v2x);
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

			final var normalx = v1y * v2z - v1z * v2y;
			final var normaly = v1z * v2x - v1x * v2z;
			final var normalz = v1x * v2y - v1y * v2x;

			return mollerAlgorithmPlaneBoxOverlap(
					normalx, normaly, normalz,
					v0x, v0y, v0z,
					halfsizex, halfsizey, halfsizez);
		}

		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:parametername"})
		private static boolean mollerAlgorithmAxisTestX01(double a, double b, double fa, double fb,
				double v0y, double v0z, double v2y, double v2z, double halfsizey, double halfsizez) {
			final var p0 = a * v0y - b * v0z;
			final var p2 = a * v2y - b * v2z;
			final double min;
			final double max;
			if (p0 < p2) {
				min = p0;
				max = p2;
			} else {
				min = p2;
				max = p0;
			}
			final var rad = fa * halfsizey + fb * halfsizez;
			return min > rad || max < -rad;
		}

		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:parametername"})
		private static boolean mollerAlgorithmAxisTestY02(double a, double b, double fa, double fb,
				double v0x, double v0z, double v2x, double v2z, double halfsizex, double halfsizez) {
			final var p0 = -a * v0x + b * v0z;
			final var p2 = -a * v2x + b * v2z;
			final double min;
			final double max;
			if (p0 < p2) {
				min = p0;
				max = p2;
			} else {
				min = p2;
				max = p0;
			}
			final var rad = fa * halfsizex + fb * halfsizez;
			return min > rad || max < -rad;
		}

		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:parametername"})
		private static boolean mollerAlgorithmAxisTestZ12(double a, double b, double fa, double fb,
				double v1x, double v1y, double v2x, double v2y, double halfsizex, double halfsizey) {
			final var p1 = a * v1x - b * v1y;
			final var p2 = a * v2x - b * v2y;
			final double min;
			final double max;
			if (p2 < p1) {
				min = p2;
				max = p1;
			} else {
				min = p1;
				max = p2;
			}
			final var rad = fa * halfsizex + fb * halfsizey;
			return min > rad || max < -rad;
		}

		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:parametername"})
		private static boolean mollerAlgorithmAxisTestZ0(double a, double b, double fa, double fb,
				double v0x, double v0y, double v1x, double v1y, double halfsizex, double halfsizey) {
			final var p0 = a * v0x - b * v0y;
			final var p1 = a * v1x - b * v1y;
			final double min;
			final double max;
			if (p0 < p1) {
				min = p0;
				max = p1;
			} else {
				min = p1;
				max = p0;
			}
			final var rad = fa * halfsizex + fb * halfsizey;
			return min > rad || max < -rad;
		}

		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:parametername"})
		private static boolean mollerAlgorithmAxisTestX02(double a, double b, double fa, double fb,
				double v0y, double v0z, double v1y, double v1z, double halfsizey, double halfsizez) {
			final var p0 = a * v0y - b * v0z;
			final var p1 = a * v1y - b * v1z;
			final double min;
			final double max;
			if (p0 < p1) {
				min = p0;
				max = p1;
			} else {
				min = p1;
				max = p0;
			}
			final var rad = fa * halfsizey + fb * halfsizez;
			return min > rad || max < -rad;
		}

		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:parametername"})
		private static boolean mollerAlgorithmAxisTestY01(double a, double b, double fa, double fb,
				double v0x, double v0z, double v1x, double v1z, double halfsizex, double halfsizez) {
			final var p0 = -a * v0x + b * v0z;
			final var p1 = -a * v1x + b * v1z;
			final double min;
			final double max;
			if (p0 < p1) {
				min = p0;
				max = p1;
			} else {
				min = p1;
				max = p0;
			}
			final var rad = fa * halfsizex + fb * halfsizez;
			return min > rad || max < -rad;
		}

		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:parametername"})
		private static boolean mollerAlgorithmPlaneBoxOverlap(
				double normalx, double normaly, double normalz,
				double vertx, double verty, double vertz,
				double maxboxx, double maxboxy, double maxboxz) {
			final double vminx;
			final double vmaxx;
			final double vminy;
			final double vmaxy;
			final double vminz;
			final double vmaxz;

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

			if (Vector3D.dotProduct(normalx, normaly, normalz, vminx, vminy, vminz) > 0.) {
				return false;
			}

			if (Vector3D.dotProduct(normalx, normaly, normalz, vmaxx, vmaxy, vmaxz) > 0.) {
				return true;
			}

			return false;
		}

		/** Replies if two coplanar triangles intersect.
		 * Triangles intersect even if they are connected by two of their
		 * edges.
		 *
		 * <p><a href="https://fileadmin.cs.lth.se/cs/Personal/Tomas_Akenine-Moller/pubs/tritri.pdf">Triangle/triangle
		 * intersection test routine, by Tomas Moller, 1997.
		 * See article "A Fast Triangle-Triangle Intersection Test",
		 * Journal of Graphics Tools, 2(2), 1997.</a>
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
		 * @return {@code true} if the two triangles are intersecting.
		 */
		@SuppressWarnings("checkstyle:parameternumber")
		@Pure
		public static boolean intersectsCoplanarTriangleTriangle(
				double v1x, double v1y, double v1z,
				double v2x, double v2y, double v2z,
				double v3x, double v3y, double v3z,
				double u1x, double u1y, double u1z,
				double u2x, double u2y, double u2z,
				double u3x, double u3y, double u3z) {
			// first project onto an axis-aligned plane, that maximizes the area
			// of the triangles, compute indices: i0,i1.
			var nx = v1y * (v2z - v3z) + v2y * (v3z - v1z) + v3y * (v1z - v2z);
			var ny = v1z * (v2x - v3x) + v2z * (v3x - v1x) + v3z * (v1x - v2x);
			var nz = v1x * (v2y - v3y) + v2x * (v3y - v1y) + v3x * (v1y - v2y);

			nx = nx < 0 ? -nx : nx;
			ny = ny < 0 ? -ny : ny;
			nz = nz < 0 ? -nz : nz;

			final int i0;
			final int i1;

			if (nx > ny) {
				if (nx > nz) {
					// nx is greatest
					i0 = 1;
					i1 = 2;
				} else {
					// nz is greatest
					i0 = 0;
					i1 = 1;
				}
			} else {
				/* nx<=ny */
				if (nz > ny) {
					// nz is greatest
					i0 = 0;
					i1 = 1;
				} else {
					// ny is greatest
					i0 = 0;
					i1 = 2;
				}
			}

			final var tv1 = new double[] {v1x, v1y, v1z};
			final var tv2 = new double[] {v2x, v2y, v2z};
			final var tv3 = new double[] {v3x, v3y, v3z};
			final var tu1 = new double[] {u1x, u1y, u1z};
			final var tu2 = new double[] {u2x, u2y, u2z};
			final var tu3 = new double[] {u3x, u3y, u3z};

			// test all edges of triangle 1 against the edges of triangle 2
			return intersectsCoplanarTriangle(i0, i1, 0, tv1, tv2, tu1, tu2, tu3)
					|| intersectsCoplanarTriangle(i0, i1, 0, tv2, tv3, tu1, tu2, tu3)
					|| intersectsCoplanarTriangle(i0, i1, 0, tv3, tv1, tu1, tu2, tu3)
					// finally, test if tri1 is totally contained in tri2 or vice versa
					|| containsTrianglePoint(i0, i1, tv1, tu1, tu2, tu3)
					|| containsTrianglePoint(i0, i1, tu1, tv1, tv2, tv3);
		}

		/** Replies if a point is inside a triangle.
		 *
		 */
		private static boolean containsTrianglePoint(
				int i0, int i1, double[] v, double[] u1, double[] u2, double[] u3) {
			// is T1 completly inside T2?
			// check if V0 is inside tri(U0,U1,U2)
			var a = u2[i1] - u1[i1];
			var b = -(u2[i0] - u1[i0]);
			var c = -a * u1[i0] - b * u1[i1];
			final var d0 = a * v[i0] + b * v[i1] + c;

			a = u3[i1] - u2[i1];
			b = -(u3[i0] - u2[i0]);
			c = -a * u2[i0] - b * u2[i1];
			final var d1 = a * v[i0] + b * v[i1] + c;

			a = u1[i1] - u2[i1];
			b = -(u1[i0] - u3[i0]);
			c = -a * u3[i0] - b * u3[i1];
			final var d2 = a * v[i0] + b * v[i1] + c;

			return (d0 * d1) > 0. && (d0 * d2) > 0.;
		}

		/** Replies if coplanar segment-triangle intersect.
		 */
		private static boolean intersectsCoplanarTriangle(
				int i0, int i1, int con, double[] s1, double[] s2, double[] u1, double[] u2, double[] u3) {
			final var ax = s2[i0] - s1[i0];
			final var ay = s2[i1] - s1[i1];
			return intersectEdgeEdge(i0, i1, con, ax, ay, s1, u1, u2)
					|| intersectEdgeEdge(i0, i1, con, ax, ay, s1, u2, u3)
					|| intersectEdgeEdge(i0, i1, con, ax, ay, s1, u3, u1);
		}

		/** This edge to edge test is based on Franlin Antonio's gem:
		 * "Faster Line Segment Intersection", in Graphics Gems III,
		 * pp. 199-202.
		 */
		@Pure
		private static boolean intersectEdgeEdge(
				int i0, int i1, int con, double ax, double ay, double[] v, double[] u1, double[] u2) {
			// [v,b] is the segment that contains the point v
			// [c,d] is the segment [u1,u2]

			// A is the vector (v,b)
			// B is the vector (d,c)
			// C is the vector (c,v)

			final var bx = u1[i0] - u2[i0];
			final var by = u1[i1] - u2[i1];
			final var cx = v[i0]  - u1[i0];
			final var cy = v[i1]  - u1[i1];

			final var f = ay * bx - ax * by;
			// Line equation: V+d*A
			final var d = by * cx - bx * cy;

			var up = false;
			var down = false;

			if (f > 0) {
				switch (con) {
				case 1:
					// First point must be ignored
					down = d > 0;
					up = d <= f;
					break;
				case 2:
					// Second point must be ignored
					down = d >= 0;
					up = d < f;
					break;
				case 3:
					// First and Second points must be ignored
					down = d > 0;
					up = d < f;
					break;
				default:
					down = d >= 0;
					up = d <= f;
				}
			} else if (f < 0) {
				switch (con) {
				case 1:
					// First point must be ignored
					down = d >= f;
					up = d < 0;
					break;
				case 2:
					// Second point must be ignored
					down = d > f;
					up = d <= 0;
					break;
				case 3:
					// First and Second points must be ignored
					down = d > f;
					up = d < 0;
					break;
				default:
					down = d >= f;
					up = d <= 0;
				}
			}

			if (up && down) {
				final var e = ax * cy - ay * cx;
				if (f >= 0) {
					return e >= 0 && e <= f;
				}
				return e >= f && e <= 0;
			}

			return false;
		}
	}

	/** Utility class for the Jimenez's algorithm.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	final class JimenezAlgorithmTools {

		private JimenezAlgorithmTools() {
			//
		}

		/** Replies the intersection factor between a triangle and a segment on the segment itself.
		 *
		 * <p>This function implements <strong>a fixed version</strong> of the algorithm provided by Jimenez et al.:<br/>
		 * <a href="https://doi.org/10.1016/j.comgeo.2009.10.001">Juan J. Jimenez,
		 * Rafael J. Segura, Francisco R. Feito.
		 * "A robust segment/triangle intersection algorithm for interference tests. Efficiency study".
		 * Computational Geometry 43 (2010) pp 474-492. 2010.</a>
		 *
		 * <p>If the segment and the triangle are not intersecting, this
		 * function replies {@link Double#NaN}.
		 * Otherwise the replied value is the factor that could be used
		 * for computing the intersection point. Value of zero means that
		 * the intersection point is the first point of the segment.
		 * Value of 1 means that the intersection point is the second point
		 * of the segment. Value in (0;1) means the intersection point
		 * is located on the segment.
		 *
		 * <p>If the segment is coplanar to the triangle and has multiple
		 * intersection points, then one is selected and its factor is replied.
		 * This specific behavior is not implemented on the original source algorithm.
		 *
		 * <p><strong>The algorithm of Jimenez et al. is faster than the algorithm of Badouel et al.</strong>
		 *
		 * <p><strong>CAUTION:</strong> The Jimenez's algorithm is defined according to a right-handed coordinate system.
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
		 * @param epsilon the epsilon value that is used for testing inequalities.
		 * @return the factor that permits to compute the intersection point,
		 *     {@link Double#NaN} when no intersection.
		 * @see BadouelAlgorithmTools#calculatesIntersectionFactorTriangleSegment(double, double, double, double, double, double, double,
		 *     double, double, double, double, double, double, double, double)
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:returncount", "checkstyle:localfinalvariablename",
			"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
		@Pure
		public static double calculatesIntersectionFactorTriangleSegment(
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double sx1, double sy1, double sz1,
				double sx2, double sy2, double sz2,
				double epsilon) {
			// From original algorithm, input is: Triangle (V1, V2, V3); Segment(Q1, Q2)
			// A = Q1 - V3
			final var A_x = sx1 - tx3;
			final var A_y = sy1 - ty3;
			final var A_z = sz1 - tz3;
			// B = V1 - V3
			final var B_x = tx1 - tx3;
			final var B_y = ty1 - ty3;
			final var B_z = tz1 - tz3;
			// C = V2 - V3
			final var C_x = tx2 - tx3;
			final var C_y = ty2 - ty3;
			final var C_z = tz2 - tz3;
			// W1 = B x C
			final var W1 = new InnerComputationVector3D();
			Vector3D.crossProductRightHand(B_x, B_y, B_z, C_x, C_y, C_z, W1);
			// w = A . W1
			final var w = Vector3D.dotProduct(A_x, A_y, A_z, W1.getX(), W1.getY(), W1.getZ());
			// D = Q2 - V3
			final var D_x = sx2 - tx3;
			final var D_y = sy2 - ty3;
			final var D_z = sz2 - tz3;
			// s = D. W1
			final var s = Vector3D.dotProduct(D_x, D_y, D_z, W1.getX(), W1.getY(), W1.getZ());

			final var wpositive = w > epsilon;
			final var wnegative = w < -epsilon;
			if (wpositive || wnegative) {

				// Rejection 2
				if (wpositive && s > epsilon || wnegative && s < -epsilon) {
					return Double.NaN;
				}
				// W2 = A x D
				final var W2 = new InnerComputationVector3D();
				Vector3D.crossProductRightHand(A_x, A_y, A_z, D_x, D_y, D_z, W2);
				// t = W2 . C
				final var t = Vector3D.dotProduct(W2.getX(), W2.getY(), W2.getZ(), C_x, C_y, C_z);
				// Rejection 3
				if (wpositive && t < -epsilon || wnegative && t > epsilon) {
					return Double.NaN;
				}
				// u = -W2 . B
				final var u = -Vector3D.dotProduct(W2.getX(), W2.getY(), W2.getZ(), B_x, B_y, B_z);
				// Rejection 4
				if (wpositive && u < -epsilon || wnegative && u > epsilon) {
					return Double.NaN;
				}
				// Rejection 5
				final var sum = s + t + u;
				if (wpositive && w < sum || wnegative && w > sum) {
					return Double.NaN;
				}

			} else {

				// W is null, swap Q1, Q2
				final var spositive = s > epsilon;
				final var snegative = s < -epsilon;
				if (spositive || snegative) {
					// W2 = D x A
					final var W2 = new InnerComputationVector3D();
					Vector3D.crossProductRightHand(D_x, D_y, D_z, A_x, A_y, A_z, W2);
					// t = W2 . C
					final var t = Vector3D.dotProduct(W2.getX(), W2.getY(), W2.getZ(), C_x, C_y, C_z);
					// Rejection 3
					if (spositive && t < -epsilon || wnegative && t > epsilon) {
						return Double.NaN;
					}
					// u = -W2 . B
					final var u = -Vector3D.dotProduct(W2.getX(), W2.getY(), W2.getZ(), B_x, B_y, B_z);
					// Rejection 4
					if (spositive && u < -epsilon || wnegative && u > epsilon) {
						return Double.NaN;
					}
					// Rejection 5
					final var sum = t + u;
					// Fix: remove the negation below to obtain correct rejection test.
					// This is a difference with the algorithm from Jimenez's paper.
					if (spositive && s < sum || wnegative && s > sum) {
						return Double.NaN;
					}

				} else {
					// Rejection 1: the segment and the triangle are coplanar.
					// The problem should be solved in 2D in the triangle's plane.
					// The Jimenez algorithm is adapted.
					return IntersectionTools.calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
							tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
							sx1, sy1, sz1, sx2, sy2, sz2,
							epsilon);
				}
			}
			// Fix: add the negation below to obtain correct t_param value. This is a difference with the algorithm from Jimenez's paper.
			final var t_param = -w / (s - w);
			return t_param;
		}

	}

	/** Utility class related to intersection.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	final class IntersectionTools {

		private IntersectionTools() {
			//
		}

		/**
		 * Tests if the point {@code (px,py,pz)}
		 * lies inside a 3D triangle
		 * given by {@code (x1,y1,z1)}, {@code (x2,y2,z2)}
		 * and {@code (x3,y3,z3)} points.
		 *
		 * <p><strong>Caution: Tests are "epsiloned."</strong>
		 *
		 * <p>Parameter <var>forceCoplanar</var> has a deep influence on the function
		 * result. It indicates if coplanarity test must be done or not.
		 * Following table explains this influence:
		 *
		 * <table border="1" width="100%">
		 * <thead>
		 * <tr>
		 * <tr>Point is coplanar?</tr>
		 * <tr>Point projection on plane is inside triangle?</tr>
		 * <tr><var>forceCoplanar</var></tr>
		 * <tr>{@code intersectsPointTrangle()} Result</tr>
		 * </tr>
		 * </thead>
		 * <tbody>
		 * <tr>
		 * <td>{@code true}</td>
		 * <td>{@code true}</td>
		 * <td>{@code true}</td>
		 * <td>{@code true}</td>
		 * </tr>
		 * <tr>
		 * <td>{@code true}</td>
		 * <td>{@code true}</td>
		 * <td>{@code false}</td>
		 * <td>{@code true}</td>
		 * </tr>
		 * <tr>
		 * <td>{@code true}</td>
		 * <td>{@code false}</td>
		 * <td>{@code true}</td>
		 * <td>{@code false}</td>
		 * </tr>
		 * <tr>
		 * <td>{@code true}</td>
		 * <td>{@code false}</td>
		 * <td>{@code false}</td>
		 * <td>{@code false}</td>
		 * </tr>
		 * <tr>
		 * <td>{@code false}</td>
		 * <td>{@code true}</td>
		 * <td>{@code true}</td>
		 * <td>{@code false}</td>
		 * </tr>
		 * <tr>
		 * <td>{@code false}</td>
		 * <td>{@code true}</td>
		 * <td>{@code false}</td>
		 * <td>{@code true}</td>
		 * </tr>
		 * <tr>
		 * <td>{@code false}</td>
		 * <td>{@code false}</td>
		 * <td>{@code true}</td>
		 * <td>{@code false}</td>
		 * </tr>
		 * <tr>
		 * <td>{@code false}</td>
		 * <td>{@code false}</td>
		 * <td>{@code false}</td>
		 * <td>{@code false}</td>
		 * </tr>
		 * </tbody>
		 * </table>
		 *
		 * <p><strong>Trigonometric Method (Slowest)</strong>
		 *
		 * <p>A common way to check if a point is in a triangle is to
		 * find the vectors connecting the point to each of the
		 * triangle's three vertices and sum the angles between
		 * those vectors. If the sum of the angles is 2*pi
		 * then the point is inside the triangle, otherwise it
		 * is not. <em>It works, but it is very slow.</em>
		 *
		 * <p>
		 * The advantage of the method above is that it's very simple to understand so that once
		 * you read it you should be able to remember it forever and code it up at
		 * any time without having to refer back to anything.
		 *
		 * <p><strong>Barycenric Method (Fastest)</strong>
		 *
		 * <p>There's another method that is also as easy conceptually but executes faster.
		 * The downside is there's a little more math involved, but once you see
		 * it worked out it should be no problem.
		 *
		 * <p>So remember that the three points of the triangle define a plane in space.
		 * Pick one of the points and we can consider all other locations on the plane
		 * as relative to that point. Let's select A -- it'll be our origin on the
		 * plane. Now what we need are basis vectors so we can give coordinate
		 * values to all the locations on the plane.
		 * We'll pick the two edges of the triangle that touch A,
		 * (C - A) and (B - A).
		 * Now we can get to any point on the plane just by starting at A
		 * and walking some distance along (C - A) and then from there walking
		 * some more in the direction (B - A).
		 *
		 * <p>With that in mind we can now describe any point on the plane as:<br>
		 * P = A + u * (C - A) + v * (B - A)
		 *
		 * <p>Notice now that if u or v < 0 then we've walked in the wrong direction
		 * and must be outside the triangle. Also if u or v > 1 then we've
		 * walked too far in a direction and are outside the triangle.
		 * Finally if u + v > 1 then we've crossed the edge BC again leaving the triangle.
		 *
		 * <p>Given u and v we can easily calculate the point P with the above
		 * equation, but how can we go in the reverse direction and calculate
		 * u and v from a given point P?<br>
		 * P = A + u * (C - A) + v * (B - A)       // Original equation<br>
		 * (P - A) = u * (C - A) + v * (B - A)     // Subtract A from both sides<br>
		 * v2 = u * v0 + v * v1                    // Substitute v0, v1, v2 for less writing
		 *
		 * <p>We have two unknowns (u and v) so we need two equations to solve
		 * for them.  Dot both sides by v0 to get one and dot both sides by
		 * v1 to get a second.<br>
		 * (v2) . v0 = (u * v0 + v * v1) . v0<br>
		 * (v2) . v1 = (u * v0 + v * v1) . v1<br>
		 *
		 * <p>Distribute v0 and v1<br>
		 * v2 . v0 = u * (v0 . v0) + v * (v1 . v0)<br>
		 * v2 . v1 = u * (v0 . v1) + v * (v1 . v1)
		 *
		 * <p>Now we have two equations and two unknowns and can solve one
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
		 * @param forceCoplanar is {@code true} to force to test
		 *     to consider the given point is coplanar to the triangle, {@code false}
		 *     to not consider coplanarity of the point.
		 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters
		 * @return {@code true} if the points is coplanar - or not,
		 *     depending on <var>forceCoplanar</var> - to the triangle and
		 *     lies inside it, otherwise {@code false}
		 */
		@Unefficient
		@Pure
		@SuppressWarnings("checkstyle:parameternumber")
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
			final var v0x = cx - ax;
			final var v0y = cy - ay;
			final var v0z = cz - az;
			// v1 = B - A
			final var v1x = bx - ax;
			final var v1y = by - ay;
			final var v1z = bz - az;
			// v2 = P - A
			final var v2x = px - ax;
			final var v2y = py - ay;
			final var v2z = pz - az;

			//
			// Compute dot products
			//
			// dot01 = dot(v0, v0)
			var dot00 = Vector3D.dotProduct(v0x, v0y, v0z, v0x, v0y, v0z);
			// dot01 = dot(v0, v1)
			var dot01 = Vector3D.dotProduct(v0x, v0y, v0z, v1x, v1y, v1z);
			// dot02 = dot(v0, v2)
			var dot02 = Vector3D.dotProduct(v0x, v0y, v0z, v2x, v2y, v2z);
			// dot11 = dot(v1, v1)
			var dot11 = Vector3D.dotProduct(v1x, v1y, v1z, v1x, v1y, v1z);
			// dot12 = dot(v1, v2)
			var dot12 = Vector3D.dotProduct(v1x, v1y, v1z, v2x, v2y, v2z);

			//
			// Compute barycentric coordinates
			//
			final var invDenom = 1. / (dot00 * dot11 - dot01 * dot01);
			final var u = (dot11 * dot02 - dot01 * dot12) * invDenom;
			final var v = (dot00 * dot12 - dot01 * dot02) * invDenom;

			// Check if point is in triangle
			if (MathUtil.compareEpsilon(u, 0., epsilon) >= 0
					&& MathUtil.compareEpsilon(v, 0., epsilon) >= 0
					&& MathUtil.compareEpsilon(u + v, 1., epsilon) <= 0) {
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
					dot11 = -(dot00 * ax + dot01 * ay + dot02 * az);
					dot12 = dot00 * px + dot01 * py + dot02 * pz + dot11;

					return MathUtil.isEpsilonZero(dot12, epsilon);
				}
				return true;
			}
			return false;
		}

		/** Replies the intersection factor between a triangle and a segment on the segment itself
		 * <strong>assuming that the segment is coplanar to the triangle</strong>.
		 *
		 * <p>If the segment and the triangle are not intersecting, this
		 * function replies {@link Double#NaN}.
		 * Otherwise the replied value is the factor that could be used
		 * for computing the intersection point. Value of zero means that
		 * the intersection point is the first point of the segment.
		 * Value of 1 means that the intersection point is the second point
		 * of the segment. Value in (0;1) means the intersection point
		 * is located on the segment.
		 *
		 * <p>If the segment is coplanar to the triangle and has multiple
		 * intersection points, then one is selected and its factor is replied.
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
		 * @param epsilon the epsilon value that is used for testing inequalities.
		 * @return the parameter of the intersection point.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
		@Unefficient
		@Pure
		public static double calculatesIntersectionFactorTriangleSegmentWhenOnPlane(
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double sx1, double sy1, double sz1,
				double sx2, double sy2, double sz2,
				double epsilon) {
			// Segment is assumed coplanar to the triangle plane.
			// First, check whether one of the endpoints is inside the triangle.
			if (containsTrianglePoint(tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, sx1, sy1, sz1, true, epsilon)) {
				return 0.;
			}
			if (containsTrianglePoint(tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, sx2, sy2, sz2, true, epsilon)) {
				return 1.;
			}

			var bestFactor = Double.NaN;
			var best = Double.POSITIVE_INFINITY;

			// Intersect segment with each triangle edge, and keep the smallest valid factor on the segment.
			var factor = segmentSegmentIntersectionFactorOnPlane(
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx1, ty1, tz1, tx2, ty2, tz2,
					epsilon);
			if (!Double.isNaN(factor) && factor >= -epsilon && factor <= 1. + epsilon && factor < best) {
				best = factor;
				bestFactor = Math.clamp(factor, 0., 1.);
			}

			factor = segmentSegmentIntersectionFactorOnPlane(
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx2, ty2, tz2, tx3, ty3, tz3,
					epsilon);
			if (!Double.isNaN(factor) && factor >= -epsilon && factor <= 1. + epsilon && factor < best) {
				best = factor;
				bestFactor = Math.clamp(factor, 0., 1.);
			}

			factor = segmentSegmentIntersectionFactorOnPlane(
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx3, ty3, tz3, tx1, ty1, tz1,
					epsilon);
			if (!Double.isNaN(factor) && factor >= -epsilon && factor <= 1. + epsilon && factor < best) {
				best = factor;
				bestFactor = Math.clamp(factor, 0., 1.);
			}

			return bestFactor;
		}

		@SuppressWarnings({"checkstyle:parameternumber"})
		private static double segmentSegmentIntersectionFactorOnPlane(
				double s1x, double s1y, double s1z,
				double s2x, double s2y, double s2z,
				double t1x, double t1y, double t1z,
				double t2x, double t2y, double t2z,
				double epsilon) {

			final var dsx = s2x - s1x;
			final var dsy = s2y - s1y;
			final var dsz = s2z - s1z;

			final var dax = t2x - t1x;
			final var day = t2y - t1y;
			final var daz = t2z - t1z;

			final var absNx = Math.abs(dsy * daz - dsz * day);
			final var absNy = Math.abs(dsz * dax - dsx * daz);
			final var absNz = Math.abs(dsx * day - dsy * dax);

			// Project to the plane with the largest normal component.
			if (absNx >= absNy && absNx >= absNz) {
				return segmentSegmentIntersection2D(
						s1y, s1z, s2y, s2z,
						t1y, t1z, t2y, t2z,
						epsilon);
			}
			if (absNy >= absNz) {
				return segmentSegmentIntersection2D(
						s1x, s1z, s2x, s2z,
						t1x, t1z, t2x, t2z,
						epsilon);
			}
			return segmentSegmentIntersection2D(
					s1x, s1y, s2x, s2y,
					t1x, t1y, t2x, t2y,
					epsilon);
		}

		@SuppressWarnings({"checkstyle:parameternumber"})
		private static double segmentSegmentIntersection2D(
				double a1x, double a1y, double a2x, double a2y,
				double b1x, double b1y, double b2x, double b2y,
				double epsilon) {

			final var rX = a2x - a1x;
			final var rY = a2y - a1y;
			final var sX = b2x - b1x;
			final var sY = b2y - b1y;

			final var qpx = b1x - a1x;
			final var qpy = b1y - a1y;

			final var rxs = rX * sY - rY * sX;
			final var qpxr = qpx * rY - qpy * rX;

			// Parallel or collinear
			if (Math.abs(rxs) <= epsilon) {
				if (Math.abs(qpxr) > epsilon) {
					return Double.NaN;
				}

				// Collinear overlap: return first overlapping point on the first segment.
				final var rr = rX * rX + rY * rY;
				if (rr <= epsilon) {
					// Degenerate first segment
					return Double.NaN;
				}

				final var t0 = ((b1x - a1x) * rX + (b1y - a1y) * rY) / rr;
				final var t1 = ((b2x - a1x) * rX + (b2y - a1y) * rY) / rr;

				final var min = MathUtil.max(0., t0, t1);
				final var max = MathUtil.min(1., t0, t1);

				if (max < -epsilon || min > 1. + epsilon) {
					return Double.NaN;
				}
				return Math.clamp(min, 0., 1.);
			}

			final var t = ((b1x - a1x) * sY - (b1y - a1y) * sX) / rxs;
			final var u = ((b1x - a1x) * rY - (b1y - a1y) * rX) / rxs;

			if (t < -epsilon || t > 1. + epsilon || u < -epsilon || u > 1. + epsilon) {
				return Double.NaN;
			}
			return Math.clamp(t, 0., 1.);
		}
	}

	/** Utility class for the Ericson's book.
	 *
	 * <p>Source:
	 * <a href="https://books.google.fr/books/about/Real_Time_Collision_Detection.html">
	 * Christer Ericson, "Real-Time Collision Detection", 1st Edition, Taylor and Francis Group.
	 * ISBN 9781558607323. 2005.</a>
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 * @see "http://realtimecollisiondetection.net/blog/?p=103"
	 */
	final class EricsonAlgorithmTools {

		private EricsonAlgorithmTools() {
			//
		}

		/**
		 * Computes the closest points between a triangle and a segment.
		 *
		 * <p>The algorithm is based on the feature‑test approach described in
		 * "Real‑Time Collision Detection" by Christer Ericson (Section 5.1.3),
		 * and uses the point‑to‑triangle distance routine from David Eberly’s
		 * <em>Geometric Tools</em> (GTEngine).  All degenerate cases (zero‑area triangle,
		 * zero‑length segment) are handled gracefully.
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
		 * @param closestPointOnTriangle the point on the triangle set with the
		 *     closest coordinates. It could be {@code null}.
		 * @param closestPointOnSegment the point on the segment set with the
		 *     closest coordinates. It could be {@code null}.
		 * @param epsilon the epsilon value that is used for testing equalities.
		 * @return {@code true} if the triangle and segment intersect (distance ≤ 1e‑12)
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
		@Pure
		@Unefficient
		public static boolean findsClosestPointTriangleSegment(
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double sx1, double sy1, double sz1,
				double sx2, double sy2, double sz2,
				double epsilon,
				Point3D<?, ?, ?> closestPointOnTriangle,
				Point3D<?, ?, ?> closestPointOnSegment) {
			// best[0] = squared distance, best[1..3] = triangle point, best[4..6] = segment point
			final var best = new double[7];
			best[0] = Double.POSITIVE_INFINITY;

			// 1. Triangle vertices to segment
			pointToSegment(best, tx1, ty1, tz1, sx1, sy1, sz1, sx2, sy2, sz2, epsilon);
			pointToSegment(best, tx2, ty2, tz2, sx1, sy1, sz1, sx2, sy2, sz2, epsilon);
			pointToSegment(best, tx3, ty3, tz3, sx1, sy1, sz1, sx2, sy2, sz2, epsilon);

			// 2. Segment endpoints to triangle
			pointToTriangle(best, sx1, sy1, sz1, tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, epsilon);
			pointToTriangle(best, sx2, sy2, sz2, tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, epsilon);

			// 3. Segment to each triangle edge
			segmentSegment(best,
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx1, ty1, tz1, tx2, ty2, tz2, epsilon);
			segmentSegment(best,
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx2, ty2, tz2, tx3, ty3, tz3, epsilon);
			segmentSegment(best,
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx3, ty3, tz3, tx1, ty1, tz1, epsilon);

			// 4. Face‑interior case (segment parallel to triangle plane)
			handleFaceInterior(best,
					tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
					sx1, sy1, sz1, sx2, sy2, sz2, epsilon);

			if (Double.isInfinite(best[0])) {
				// should never happen
				return false;
			}

			if (closestPointOnTriangle != null) {
				closestPointOnTriangle.setX(best[1]);
				closestPointOnTriangle.setY(best[2]);
				closestPointOnTriangle.setZ(best[3]);
			}
			if (closestPointOnSegment != null) {
				closestPointOnSegment.setX(best[4]);
				closestPointOnSegment.setY(best[5]);
				closestPointOnSegment.setZ(best[6]);
			}

			return best[0] <= epsilon * epsilon;
		}

		/** Updates {@code best} if the candidate pair is closer.
		 */
		private static void evaluateCandidate(double[] best,
				double triX, double triY, double triZ,
				double segX, double segY, double segZ) {
			final var dx = triX - segX;
			final var dy = triY - segY;
			final var dz = triZ - segZ;
			final var sq = dx * dx + dy * dy + dz * dz;
			if (sq < best[0]) {
				best[0] = sq;
				best[1] = triX;
				best[2] = triY;
				best[3] = triZ;
				best[4] = segX;
				best[5] = segY;
				best[6] = segZ;
			}
		}

		/** Closest point on segment (a→b) to point P.
		 */
		@SuppressWarnings("checkstyle:parameternumber")
		private static void pointToSegment(double[] best,
				double px, double py, double pz,
				double ax, double ay, double az,
				double bx, double by, double bz,
				double epsilon) {
			final var dx = bx - ax;
			final var dy = by - ay;
			final var dz = bz - az;
			final var len2 = dx * dx + dy * dy + dz * dz;
			if (len2 < epsilon) {
				// segment degenerates to a point
				evaluateCandidate(best, px, py, pz, ax, ay, az);
				return;
			}
			var t = ((px - ax) * dx + (py - ay) * dy + (pz - az) * dz) / len2;
			t = MathUtil.clamp(t, 0, 1);
			final var cx = ax + t * dx;
			final var cy = ay + t * dy;
			final var cz = az + t * dz;
			evaluateCandidate(best, px, py, pz, cx, cy, cz);
		}

		/** Closest point on triangle ABC to point P (Eberly's algorithm).
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
		private static void pointToTriangle(double[] best,
				double px, double py, double pz,
				double ax, double ay, double az,
				double bx, double by, double bz,
				double cx, double cy, double cz,
				double epsilon) {
			final var abx = bx - ax;
			final var aby = by - ay;
			final var abz = bz - az;

			final var acx = cx - ax;
			final var acy = cy - ay;
			final var acz = cz - az;

			final var apx = px - ax;
			final var apy = py - ay;
			final var apz = pz - az;

			final var d1 = Vector3D.dotProduct(abx, aby, abz, apx, apy, apz);
			final var d2 = Vector3D.dotProduct(acx, acy, acz, apx, apy, apz);
			if (d1 <= 0 && d2 <= 0) {
				evaluateCandidate(best, ax, ay, az, px, py, pz);
				return;
			}

			final var bpx = px - bx;
			final var bpy = py - by;
			final var bpz = pz - bz;
			final var d3 = Vector3D.dotProduct(abx, aby, abz, bpx, bpy, bpz);
			final var d4 = Vector3D.dotProduct(acx, acy, acz, bpx, bpy, bpz);
			if (d3 >= 0 && d4 <= d3) {
				evaluateCandidate(best, bx, by, bz, px, py, pz);
				return;
			}

			final var cpx = px - cx;
			final var cpy = py - cy;
			final var cpz = pz - cz;
			final var d5 = Vector3D.dotProduct(abx, aby, abz, cpx, cpy, cpz);
			final var d6 = Vector3D.dotProduct(acx, acy, acz, cpx, cpy, cpz);
			if (d6 >= 0 && d5 <= d6) {
				evaluateCandidate(best, cx, cy, cz, px, py, pz);
				return;
			}

			final var vc = d1 * d4 - d3 * d2;
			if (vc <= 0 && d1 >= 0 && d3 <= 0) {
				final var v = d1 / (d1 - d3);
				final var ex = ax + v * abx;
				final var ey = ay + v * aby;
				final var ez = az + v * abz;
				evaluateCandidate(best, ex, ey, ez, px, py, pz);
				return;
			}

			final var vb = d5 * d2 - d1 * d6;
			if (vb <= 0 && d2 >= 0 && d6 <= 0) {
				final var v = d2 / (d2 - d6);
				final var ex = ax + v * acx;
				final var ey = ay + v * acy;
				final var ez = az + v * acz;
				evaluateCandidate(best, ex, ey, ez, px, py, pz);
				return;
			}

			final var va = d3 * d6 - d5 * d4;
			if (va <= 0 && (d4 - d3) >= 0 && (d5 - d6) >= 0) {
				final var v = (d4 - d3) / ((d4 - d3) + (d5 - d6));
				final var ex = bx + v * (cx - bx);
				final var ey = by + v * (cy - by);
				final var ez = bz + v * (cz - bz);
				evaluateCandidate(best, ex, ey, ez, px, py, pz);
				return;
			}

			// Inside triangle: project onto plane
			final var nx = aby * acz - abz * acy;
			final var ny = abz * acx - abx * acz;
			final var nz = abx * acy - aby * acx;
			final var n2 = nx * nx + ny * ny + nz * nz;
			if (n2 > epsilon) {
				final var dotn = Vector3D.dotProduct(nx, ny, nz, apx, apy, apz);
				final var qx = px - nx * dotn / n2;
				final var qy = py - ny * dotn / n2;
				final var qz = pz - nz * dotn / n2;
				evaluateCandidate(best, qx, qy, qz, px, py, pz);
			}
		}

		/** Closest points between two segments (a→b) and (c→d).
		 */
		@SuppressWarnings("checkstyle:parameternumber")
		private static void segmentSegment(double[] best,
				double ax, double ay, double az,
				double bx, double by, double bz,
				double cx, double cy, double cz,
				double dx, double dy, double dz,
				double epsilon) {
			// endpoint pairs
			pointToSegment(best, ax, ay, az, cx, cy, cz, dx, dy, dz, epsilon);
			pointToSegment(best, bx, by, bz, cx, cy, cz, dx, dy, dz, epsilon);
			pointToSegment(best, cx, cy, cz, ax, ay, az, bx, by, bz, epsilon);
			pointToSegment(best, dx, dy, dz, ax, ay, az, bx, by, bz, epsilon);

			// interior-interior (infinite lines)
			final var ux = bx - ax;
			final var uy = by - ay;
			final var uz = bz - az;

			final var vx = dx - cx;
			final var vy = dy - cy;
			final var vz = dz - cz;

			final var wx = ax - cx;
			final var wy = ay - cy;
			final var wz = az - cz;

			final var a = Vector3D.dotProduct(ux, uy, uz, ux, uy, uz);
			final var b = Vector3D.dotProduct(ux, uy, uz, vx, vy, vz);
			final var c = Vector3D.dotProduct(vx, vy, vz, vx, vy, vz);
			final var d = Vector3D.dotProduct(ux, uy, uz, wx, wy, wz);
			final var e = Vector3D.dotProduct(vx, vy, vz, wx, wy, wz);
			final var denom = a * c - b * b;
			if (Math.abs(denom) > epsilon) {
				final var s = (b * e - c * d) / denom;
				final var t = (a * e - b * d) / denom;
				if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
					final var px = ax + s * ux;
					final var py = ay + s * uy;
					final var pz = az + s * uz;
					final var qx = cx + t * vx;
					final var qy = cy + t * vy;
					final var qz = cz + t * vz;
					evaluateCandidate(best, qx, qy, qz, px, py, pz);
				}
			}
		}

		/**
		 * Handles the case where the segment is parallel to the triangle plane
		 * and its projection onto the plane lies inside the triangle.
		 */
		@SuppressWarnings("checkstyle:parameternumber")
		private static void handleFaceInterior(double[] best,
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double sx1, double sy1, double sz1,
				double sx2, double sy2, double sz2,
				double epsilon) {
			final var abx = tx2 - tx1;
			final var aby = ty2 - ty1;
			final var abz = tz2 - tz1;
			final var acx = tx3 - tx1;
			final var acy = ty3 - ty1;
			final var acz = tz3 - tz1;
			final var nx = aby * acz - abz * acy;
			final var ny = abz * acx - abx * acz;
			final var nz = abx * acy - aby * acx;
			final var n2 = nx * nx + ny * ny + nz * nz;
			if (n2 < epsilon) {
				// degenerate triangle
				return;
			}

			final var segDirX = sx2 - sx1;
			final var segDirY = sy2 - sy1;
			final var segDirZ = sz2 - sz1;
			final var dotDir = Vector3D.dotProduct(segDirX, segDirY, segDirZ, nx, ny, nz);
			if (Math.abs(dotDir) >= epsilon) {
				// not parallel
				return;
			}

			// Project midpoint of segment onto the plane
			final var midX = (sx1 + sx2) * .5;
			final var midY = (sy1 + sy2) * .5;
			final var midZ = (sz1 + sz2) * .5;
			final var midD = Vector3D.dotProduct(nx, ny, nz, midX - tx1, midY - ty1, midZ - tz1);
			final var projX = midX - nx * midD / n2;
			final var projY = midY - ny * midD / n2;
			final var projZ = midZ - nz * midD / n2;

			// Barycentric coordinates of projected midpoint
			final var qx = projX - tx1;
			final var qy = projY - ty1;
			final var qz = projZ - tz1;
			final var d1 = Vector3D.dotProduct(abx, aby, abz, qx, qy, qz);
			final var d2 = Vector3D.dotProduct(acx, acy, acz, qx, qy, qz);
			final var d11 = Vector3D.dotProduct(abx, aby, abz, abx, aby, abz);
			final var d22 = Vector3D.dotProduct(acx, acy, acz, acx, acy, acz);
			final var d12 = Vector3D.dotProduct(abx, aby, abz, acx, acy, acz);
			final var det = d11 * d22 - d12 * d12;
			if (Math.abs(det) < epsilon) {
				return;
			}

			final var beta = (d22 * d1 - d12 * d2) / det;
			final var gamma = (d11 * d2 - d12 * d1) / det;
			final var alpha = 1 - beta - gamma;

			if (beta >= -epsilon && gamma >= -epsilon && alpha >= -epsilon) {
				// Projected midpoint is inside the triangle
				// perpendicular distance squared
				final var distSq = (midD * midD) / n2;
				if (distSq < best[0]) {
					best[0] = distSq;
					best[1] = projX;
					best[2] = projY;
					best[3] = projZ;
					best[4] = midX;
					best[5] = midY;
					best[6] = midZ;
				}
			}
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
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
		@Unefficient
		@Pure
		public static void findsClosestPointTrianglePoint(
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double px, double py, double pz,
				Point3D<?, ?, ?> closestPoint) {
			// AB = V2 - V1, AC = V3 - V1, AP = P - V1
			final var abx = tx2 - tx1;
			final var aby = ty2 - ty1;
			final var abz = tz2 - tz1;
			final var acx = tx3 - tx1;
			final var acy = ty3 - ty1;
			final var acz = tz3 - tz1;
			final var apx = px - tx1;
			final var apy = py - ty1;
			final var apz = pz - tz1;

			// Check if P in vertex region outside V1
			final var d1 = Vector3D.dotProduct(abx, aby, abz, apx, apy, apz);
			final var d2 = Vector3D.dotProduct(acx, acy, acz, apx, apy, apz);
			if (d1 <= 0. && d2 <= 0.) {
				// barycentric (1,0,0)
				closestPoint.set(tx1, ty1, tz1);
				return;
			}

			// Check if P in vertex region outside V2
			final var bpx = px - tx2;
			final var bpy = py - ty2;
			final var bpz = pz - tz2;
			final var d3 = Vector3D.dotProduct(abx, aby, abz, bpx, bpy, bpz);
			final var d4 = Vector3D.dotProduct(acx, acy, acz, bpx, bpy, bpz);
			if (d3 >= 0. && d4 <= d3) {
				// barycentric (0,1,0)
				closestPoint.set(tx2, ty2, tz2);
				return;
			}

			// Check if P in edge region of V1-V2; if so, project P onto V1-V2
			final var vc = d1 * d4 - d3 * d2;
			if (vc <= 0. && d1 >= 0. && d3 <= 0.) {
				final var v = d1 / (d1 - d3);
				// barycentric (1-v,v,0)
				closestPoint.set(tx1 + v * abx, ty1 + v * aby, tz1 + v * abz);
				return;
			}

			// Check if P in vertex region outside V3
			final var cpx = px - tx3;
			final var cpy = py - ty3;
			final var cpz = pz - tz3;
			final var d5 = Vector3D.dotProduct(abx, aby, abz, cpx, cpy, cpz);
			final var d6 = Vector3D.dotProduct(acx, acy, acz, cpx, cpy, cpz);
			if (d6 >= 0. && d5 <= d6) {
				// barycentric (0,0,1)
				closestPoint.set(tx3, ty3, tz3);
				return;
			}

			// Check if P in edge region of V1-V3; if so, project P onto V1-V3
			final var vb = d5 * d2 - d1 * d6;
			if (vb <= 0. && d2 >= 0. && d6 <= 0.) {
				final var w = d2 / (d2 - d6);
				// barycentric (1-w,0,w)
				closestPoint.set(tx1 + w * acx, ty1 + w * acy, tz1 + w * acz);
				return;
			}

			// Check if P in edge region of V2-V3; if so, project P onto V2-V3
			final var va = d3 * d6 - d5 * d4;
			final var d4d3 = d4 - d3;
			final var d5d6 = d5 - d6;
			if (va <= 0. && d4d3 >= 0. && d5d6 >= 0.) {
				final var w = d4d3 / (d4d3 + d5d6);
				// barycentric (0,1-w,w)
				closestPoint.set(tx2 + w * (tx3 - tx2), ty2 + w * (ty3 - ty2), tz2 + w * (tz3 - tz2));
				return;
			}

			// P is inside the face region. Compute the closest point via its
			// barycentric coordinates (u,v,w), u = 1 - v - w.
			final var denom = 1. / (va + vb + vc);
			final var v = vb * denom;
			final var w = vc * denom;
			closestPoint.set(tx1 + abx * v + acx * w, ty1 + aby * v + acy * w, tz1 + abz * v + acz * w);
		}
	}

}
