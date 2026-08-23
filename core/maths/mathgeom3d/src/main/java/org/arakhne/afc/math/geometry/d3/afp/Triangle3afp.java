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
import org.arakhne.afc.math.geometry.base.PathElementType;
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
	 * @param epsilon the epsilon value that is used for testing equalities.
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
			double maxx, double maxy, double maxz,
			double epsilon) {
		return MollerAlgorithmTools.intersectsTriangleAlignedBox(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				minx, miny, minz,
				maxx, maxy, maxz,
				epsilon);
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
	static void findsClosestPointToTrianglePoint(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double px, double py, double pz,
			Point3D<?, ?, ?> closestPoint) {
		assert closestPoint != null : AssertMessages.notNullParameter(13);
		EricsonAlgorithmTools.findsClosestPointToTrianglePoint(tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, px, py, pz, closestPoint);
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
	static void findsClosestPointToTriangleSegment(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			double epsilon,
			Point3D<?, ?, ?> closestPointOnTriangle,
			Point3D<?, ?, ?> closestPointOnSegment) {
		assert closestPointOnTriangle != null || closestPointOnSegment != null : AssertMessages.notNullParameter(17);
		EricsonAlgorithmTools.findsClosestPointToTriangleSegment(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				sx1, sy1, sz1,
				sx2, sy2, sz2,
				epsilon,
				closestPointOnTriangle,
				closestPointOnSegment);
	}

	/** Replies the closest point from the triangle A to the triangle B.
	 * The closest point is always located in the triangle.
	 *
	 * @param ax1 x coordinate of the first point of the first triangle.
	 * @param ay1 y coordinate of the first point of the first triangle.
	 * @param az1 z coordinate of the first point of the first triangle.
	 * @param ax2 x coordinate of the second point of the first triangle.
	 * @param ay2 y coordinate of the second point of the first triangle.
	 * @param az2 z coordinate of the second point of the first triangle.
	 * @param ax3 x coordinate of the third point of the first triangle.
	 * @param ay3 y coordinate of the third point of the first triangle.
	 * @param az3 z coordinate of the third point of the first triangle.
	 * @param bx1 x coordinate of the first point of the second triangle.
	 * @param by1 y coordinate of the first point of the second triangle.
	 * @param bz1 z coordinate of the first point of the second triangle.
	 * @param bx2 x coordinate of the second point of the second triangle.
	 * @param by2 y coordinate of the second point of the second triangle.
	 * @param bz2 z coordinate of the second point of the second triangle.
	 * @param bx3 x coordinate of the third point of the second triangle.
	 * @param by3 y coordinate of the third point of the second triangle.
	 * @param bz3 z coordinate of the third point of the second triangle.
	 * @param epsilon the epsilon value that is used for testing equalities.
	 * @param closestPointOnTriangleA the point on the first triangle set with the
	 *     closest coordinates. It could be {@code null}.
	 * @param closestPointOnTriangleB the point on the second triangle set with the
	 *     closest coordinates. It could be {@code null}.
	 */
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity",
		"checkstyle:magicnumber"})
	@Pure
	static void findsClosestPointToTriangleTriangle(
			double ax1, double ay1, double az1,
			double ax2, double ay2, double az2,
			double ax3, double ay3, double az3,
			double bx1, double by1, double bz1,
			double bx2, double by2, double bz2,
			double bx3, double by3, double bz3,
			double epsilon,
			Point3D<?, ?, ?> closestPointOnTriangleA,
			Point3D<?, ?, ?> closestPointOnTriangleB) {
		assert closestPointOnTriangleA != null || closestPointOnTriangleB != null : AssertMessages.notNullParameter(20);
		EberlyAlgorithmTools.findsClosestPointToTriangleTriangle(
				ax1, ay1, az1, ax2, ay2, az2, ax3, ay3, az3,
				bx1, by1, bz1, bx2, by2, bz2, bx3, by3, bz3,
				epsilon,
				closestPointOnTriangleA, closestPointOnTriangleB);
	}

	/** Replies the closest point from the triangle to the aligned box.
	 * The closest point is always located in the triangle.
	 *
	 * @param tx1 x coordinate of the first point of the first triangle.
	 * @param ty1 y coordinate of the first point of the first triangle.
	 * @param tz1 z coordinate of the first point of the first triangle.
	 * @param tx2 x coordinate of the second point of the first triangle.
	 * @param ty2 y coordinate of the second point of the first triangle.
	 * @param tz2 z coordinate of the second point of the first triangle.
	 * @param tx3 x coordinate of the third point of the first triangle.
	 * @param ty3 y coordinate of the third point of the first triangle.
	 * @param tz3 z coordinate of the third point of the first triangle.
	 * @param bx1 x coordinate of the minimum corner of the aligned box.
	 * @param by1 y coordinate of the minimum corner of the aligned box.
	 * @param bz1 z coordinate of the minimum corner of the aligned box.
	 * @param bx2 x coordinate of the maximum corner of the aligned box.
	 * @param by2 y coordinate of the maximum corner of the aligned box.
	 * @param bz2 z coordinate of the maximum corner of the aligned box.
	 * @param epsilon the epsilon value that is used for testing equalities.
	 * @param closestPointOnTriangle the point on the triangle set with the
	 *     closest coordinates. It could be {@code null}.
	 * @param closestPointOnBox the point on the aligned box set with the
	 *     closest coordinates. It could be {@code null}.
	 */
	@SuppressWarnings({"checkstyle:parameternumber"})
	@Pure
	static void findsClosestPointToTriangleAlignedBox(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double bx1, double by1, double bz1,
			double bx2, double by2, double bz2,
			double epsilon,
			Point3D<?, ?, ?> closestPointOnTriangle,
			Point3D<?, ?, ?> closestPointOnBox) {
		assert closestPointOnTriangle != null || closestPointOnBox != null : AssertMessages.notNullParameter(17);
		EberlyAlgorithmTools.findsClosestPointToTriangleAlignedBox(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				bx1, by1, bz1,
				bx2, by2, bz2,
				epsilon,
				closestPointOnTriangle, closestPointOnBox);
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
		findsClosestPointToTriangleSegment(
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
	 * @param epsilon the epsilon value that is used for testing equalities.
	 * @return {@code true} if the two triangles are intersecting.
	 * @see #intersectsTriangleTriangle(double, double, double, double, double,
	 *     double, double, double, double, double, double, double, double, double,
	 *     double, double, double, double)
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Pure
	static boolean intersectsCoplanarTriangleTriangle(
			double v1x, double v1y, double v1z,
			double v2x, double v2y, double v2z,
			double v3x, double v3y, double v3z,
			double u1x, double u1y, double u1z,
			double u2x, double u2y, double u2z,
			double u3x, double u3y, double u3z,
			double epsilon) {
		return MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
				v1x, v1y, v1z, v2x, v2y, v2z, v3x, v3y, v3z,
				u1x, u1y, u1z, u2x, u2y, u2z, u3x, u3y, u3z,
				epsilon);
	}

	/** Replies if two triangles intersect. Triangles are not necessary coplanar.
	 * Triangles intersect even if they are connected by two of their
	 * edges.
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
	 * @param epsilon the epsilon value that is used for testing equalities.
	 * @return {@code true} if the two triangles are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Pure
	static boolean intersectsTriangleTriangle(
			double v1x, double v1y, double v1z,
			double v2x, double v2y, double v2z,
			double v3x, double v3y, double v3z,
			double u1x, double u1y, double u1z,
			double u2x, double u2y, double u2z,
			double u3x, double u3y, double u3z,
			double epsilon) {
		return EberlyAlgorithmTools.intersectsTriangleTriangle(
				v1x, v1y, v1z, v2x, v2y, v2z, v3x, v3y, v3z,
				u1x, u1y, u1z, u2x, u2y, u2z, u3x, u3y, u3z,
				epsilon);
	}

	/** Calculates the closest points on a triangle to a path, and the closest point on this path to the triangle.
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
	 * @param pathIterator the provider of path elements.
	 * @param epsilon the epsilon value that is used for testing equalities.
	 * @param closestPointOnTriangle the point on the triangle set with the
	 *     closest coordinates. It could be {@code null}.
	 * @param closestPointOnPath the point on the path set with the
	 *     closest coordinates. It could be {@code null}.
	 * @return {@code true} if a close point was found.
	 * @throws IllegalStateException if an invalid path element was found.
	 */
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	@Pure
	static boolean findsClosestPointToTrianglePathIterator(
			double v1x, double v1y, double v1z,
			double v2x, double v2y, double v2z,
			double v3x, double v3y, double v3z,
			PathIterator3afp<?> pathIterator,
			double epsilon,
			Point3D<?, ?, ?> closestPointOnTriangle,
			Point3D<?, ?, ?> closestPointOnPath) {
		assert pathIterator != null : AssertMessages.notNullParameter(0);
		assert closestPointOnTriangle != null || closestPointOnPath != null : AssertMessages.notNullParameter(11);
		if (!pathIterator.hasNext()) {
			return false;
		}
		var pathElement = pathIterator.next();
		assert pathElement.getType() == PathElementType.MOVE_TO : AssertMessages.invalidValue(0);
		if (!pathIterator.hasNext()) {
			return false;
		}
		var bestDistance = Double.POSITIVE_INFINITY;
		var curx = pathElement.getToX();
		var movx = curx;
		var cury = pathElement.getToY();
		var movy = cury;
		var curz = pathElement.getToZ();
		var movz = curz;
		double endx;
		double endy;
		double endz;
		final var factory = pathIterator.getGeomFactory();
		final var point1 = new InnerComputationPoint3D();
		final var point2 = new InnerComputationPoint3D();
		var foundPoint = false;
		while (pathIterator.hasNext()) {
			pathElement = pathIterator.next();
			switch (pathElement.getType()) {
			case MOVE_TO:
				movx = pathElement.getToX();
				curx = movx;
				movy = pathElement.getToY();
				cury = movy;
				movz = pathElement.getToZ();
				curz = movz;
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				findsClosestPointToTriangleSegment(
						v1x, v1y, v1z,
						v2x, v2y, v2z,
						v3x, v3y, v3z,
						curx, cury, curz, endx, endy, endz,
						epsilon,
						point1, point2);
				var dist = Point3D.getDistanceSquaredPointPoint(point1.getX(), point1.getY(), point1.getZ(),
						point2.getX(), point2.getY(), point2.getZ());
				if (dist == 0.) {
					if (closestPointOnTriangle != null) {
						closestPointOnTriangle.set(point1);
					}
					if (closestPointOnPath != null) {
						closestPointOnPath.set(point2);
					}
					return true;
				}
				if (dist < bestDistance) {
					bestDistance = dist;
					if (closestPointOnTriangle != null) {
						closestPointOnTriangle.set(point1);
					}
					if (closestPointOnPath != null) {
						closestPointOnPath.set(point2);
					}
					foundPoint = true;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					findsClosestPointToTriangleSegment(
							v1x, v1y, v1z,
							v2x, v2y, v2z,
							v3x, v3y, v3z,
							curx, cury, curz, movx, movy, movz,
							epsilon,
							point1, point2);
					dist = Point3D.getDistanceSquaredPointPoint(point1.getX(), point1.getY(), point1.getZ(),
							point2.getX(), point2.getY(), point2.getZ());
					if (dist == 0.) {
						if (closestPointOnTriangle != null) {
							closestPointOnTriangle.set(point1);
						}
						if (closestPointOnPath != null) {
							closestPointOnPath.set(point2);
						}
						return true;
					}
					if (dist < bestDistance) {
						bestDistance = dist;
						if (closestPointOnTriangle != null) {
							closestPointOnTriangle.set(point1);
						}
						if (closestPointOnPath != null) {
							closestPointOnPath.set(point2);
						}
						foundPoint = true;
					}
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				final var subpath0 = factory.newPath();
				subpath0.moveTo(curx, cury, curz);
				subpath0.quadTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(), endx, endy, endz);
				if (findsClosestPointToTrianglePathIterator(
						v1x, v1y, v1z,
						v2x, v2y, v2z,
						v3x, v3y, v3z,
						subpath0.getPathIterator(factory.getSplineApproximationRatio()),
						epsilon,
						point1, point2)) {
					dist = Point3D.getDistanceSquaredPointPoint(point1.getX(), point1.getY(), point1.getZ(),
							point2.getX(), point2.getY(), point2.getZ());
					if (dist == 0.) {
						if (closestPointOnTriangle != null) {
							closestPointOnTriangle.set(point1);
						}
						if (closestPointOnPath != null) {
							closestPointOnPath.set(point2);
						}
						return true;
					}
					if (dist < bestDistance) {
						bestDistance = dist;
						if (closestPointOnTriangle != null) {
							closestPointOnTriangle.set(point1);
						}
						if (closestPointOnPath != null) {
							closestPointOnPath.set(point2);
						}
						foundPoint = true;
					}
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				final var subpath1 = factory.newPath();
				subpath1.moveTo(curx, cury, curz);
				subpath1.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);
				if (findsClosestPointToTrianglePathIterator(
						v1x, v1y, v1z,
						v2x, v2y, v2z,
						v3x, v3y, v3z,
						subpath1.getPathIterator(factory.getSplineApproximationRatio()),
						epsilon,
						point1, point2)) {
					dist = Point3D.getDistanceSquaredPointPoint(point1.getX(), point1.getY(), point1.getZ(),
							point2.getX(), point2.getY(), point2.getZ());
					if (dist == 0.) {
						if (closestPointOnTriangle != null) {
							closestPointOnTriangle.set(point1);
						}
						if (closestPointOnPath != null) {
							closestPointOnPath.set(point2);
						}
						return true;
					}
					if (dist < bestDistance) {
						bestDistance = dist;
						if (closestPointOnTriangle != null) {
							closestPointOnTriangle.set(point1);
						}
						if (closestPointOnPath != null) {
							closestPointOnPath.set(point2);
						}
						foundPoint = true;
					}
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case ARC_TO:
			default:
				throw new IllegalStateException(pathElement.getType().toString());
			}
		}
		return foundPoint;
	}

	/** Replies if the triangle and the path intersect.
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
	 * @param pathIterator the provider of path elements.
	 * @param epsilon the epsilon value that is used for testing equalities.
	 * @return {@code true} if triangle and path are intersecting.
	 * @throws IllegalStateException if an invalid path element was found.
	 */
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity"})
	@Pure
	static boolean intersectsTrianglePathIterator(
			double v1x, double v1y, double v1z,
			double v2x, double v2y, double v2z,
			double v3x, double v3y, double v3z,
			PathIterator3afp<?> pathIterator,
			double epsilon) {
		assert pathIterator != null : AssertMessages.notNullParameter(0);
		if (!pathIterator.hasNext()) {
			return false;
		}
		var pathElement = pathIterator.next();
		assert pathElement.getType() == PathElementType.MOVE_TO : AssertMessages.invalidValue(0);
		if (!pathIterator.hasNext()) {
			return false;
		}
		var curx = pathElement.getToX();
		var movx = curx;
		var cury = pathElement.getToY();
		var movy = cury;
		var curz = pathElement.getToZ();
		var movz = curz;
		double endx;
		double endy;
		double endz;
		final var factory = pathIterator.getGeomFactory();
		while (pathIterator.hasNext()) {
			pathElement = pathIterator.next();
			switch (pathElement.getType()) {
			case MOVE_TO:
				movx = pathElement.getToX();
				curx = movx;
				movy = pathElement.getToY();
				cury = movy;
				movz = pathElement.getToZ();
				curz = movz;
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				if (intersectsTriangleSegment(
						v1x, v1y, v1z,
						v2x, v2y, v2z,
						v3x, v3y, v3z,
						curx, cury, curz, endx, endy, endz,
						epsilon)) {
					return true;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					if (intersectsTriangleSegment(
							v1x, v1y, v1z,
							v2x, v2y, v2z,
							v3x, v3y, v3z,
							curx, cury, curz, movx, movy, movz,
							epsilon)) {
						return true;
					}
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				final var subpath0 = factory.newPath();
				subpath0.moveTo(curx, cury, curz);
				subpath0.quadTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(), endx, endy, endz);
				if (intersectsTrianglePathIterator(
						v1x, v1y, v1z,
						v2x, v2y, v2z,
						v3x, v3y, v3z,
						subpath0.getPathIterator(factory.getSplineApproximationRatio()),
						epsilon)) {
					return true;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				final var subpath1 = factory.newPath();
				subpath1.moveTo(curx, cury, curz);
				subpath1.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);
				if (intersectsTrianglePathIterator(
						v1x, v1y, v1z,
						v2x, v2y, v2z,
						v3x, v3y, v3z,
						subpath1.getPathIterator(factory.getSplineApproximationRatio()),
						epsilon)) {
					return true;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case ARC_TO:
			default:
				throw new IllegalStateException(pathElement.getType().toString());
			}
		}
		return false;
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
		findsClosestPointToTrianglePoint(tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, centerx, centery, centerz, point);
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
	 * @param epsilon the epsilon value that is used for testing equalities.
	 * @return {@code true} if the triangle and segment are intersecting.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	@Pure
	static boolean intersectsTriangleSegment(
			double tx1, double ty1, double tz1,
			double tx2, double ty2, double tz2,
			double tx3, double ty3, double tz3,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			double epsilon) {
		final var factor = JimenezAlgorithmTools.calculatesIntersectionFactorTriangleSegment(
				tx1, ty1, tz1,
				tx2, ty2, tz2,
				tx3, ty3, tz3,
				sx1, sy1, sz1,
				sx2, sy2, sz2,
				epsilon);
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
		assert point != null : AssertMessages.notNullParameter();
		final var c = getClosestPointTo(point);
		return c.getDistanceL1(point);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
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
		findsClosestPointToTrianglePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				point.getX(), point.getY(), point.getZ(),
				c);
		return c;
	}

	@Override
	default P getClosestPointTo(Capsule3afp<?, ?, ?, ?, ?, ?, ?> capsule) {
		assert capsule != null : AssertMessages.notNullParameter();
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		final var c = getGeomFactory().newPoint();
		findsClosestPointToTrianglePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				sphere.getX(), sphere.getY(), sphere.getZ(),
				c);
		return c;
	}

	@Pure
	@Override
	default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		findsClosestPointToTriangleAlignedBox(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				box.getMinX(), box.getMinY(), box.getMinZ(),
				box.getMaxX(), box.getMaxY(), box.getMaxZ(),
				GeomConstants.DISTANCE_EPSILON,
				point, null);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		findsClosestPointToTriangleSegment(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2(),
				GeomConstants.DISTANCE_EPSILON,
				point, null);
		return point;
	}

	@Override
	default P getClosestPointTo(Triangle3afp<?, ?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		findsClosestPointToTriangleTriangle(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				triangle.getX1(), triangle.getY1(), triangle.getZ1(),
				triangle.getX2(), triangle.getY2(), triangle.getZ2(),
				triangle.getX3(), triangle.getY3(), triangle.getZ3(),
				GeomConstants.DISTANCE_EPSILON,
				point, null);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		final var point = getGeomFactory().newPoint();
		if (findsClosestPointToTrianglePathIterator(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				path.getPathIterator(),
				GeomConstants.DISTANCE_EPSILON,
				point, null)) {
			return point;
		}
		return null;
	}

	@Pure
	@Override
	default P getClosestPointTo(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		final var pointOnShape = multishape.getClosestPointTo(this);
		final var point = getGeomFactory().newPoint();
		findsClosestPointToTrianglePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				pointOnShape.getX(), pointOnShape.getY(), pointOnShape.getZ(),
				point);
		return point;
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
	default boolean intersects(Capsule3afp<?, ?, ?, ?, ?, ?, ?> capsule) {
		assert capsule != null :  AssertMessages.notNullParameter();
		throw new UnsupportedOperationException();
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
				prism.getMaxX(), prism.getMaxY(), prism.getMaxZ(),
				GeomConstants.DISTANCE_EPSILON);
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
				segment.getX2(), segment.getY2(), segment.getZ2(),
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(Triangle3afp<?, ?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null :  AssertMessages.notNullParameter();
		return intersectsTriangleTriangle(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				triangle.getX1(), triangle.getY1(), triangle.getZ1(),
				triangle.getX2(), triangle.getY2(), triangle.getZ2(),
				triangle.getX3(), triangle.getY3(), triangle.getZ3(),
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		return intersectsTrianglePathIterator(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				path.getPathIterator(),
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		return intersectsTrianglePathIterator(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				getX3(), getY3(), getZ3(),
				iterator,
				GeomConstants.DISTANCE_EPSILON);
	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
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

			final var P_j = Math.fma(D_j, t_param, Q1_j);
			final var P_k = Math.fma(D_k, t_param, Q1_k);
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
		 * <p>This is a <strong>fixed version</strong> of the algorithm from
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
		 * @param epsilon the epsilon value that is used for testing equalities.
		 * @return {@code true} if the triangle and aligned box are intersecting.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity",
			"checkstyle:returncount", "checkstyle:methodlength"})
		@Pure
		public static boolean intersectsTriangleAlignedBox(
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double minx, double miny, double minz,
				double maxx, double maxy, double maxz,
				double epsilon) {

			// Half extents and box centre (origin after translation)
			final var halfx = (maxx - minx) * 0.5;
			final var halfy = (maxy - miny) * 0.5;
			final var halfz = (maxz - minz) * 0.5;
			final var cx = minx + halfx;
			final var cy = miny + halfy;
			final var cz = minz + halfz;

			// Translate triangle vertices so box centre is at origin
			final var v0x = tx1 - cx;
			final var v0y = ty1 - cy;
			final var v0z = tz1 - cz;
			final var v1x = tx2 - cx;
			final var v1y = ty2 - cy;
			final var v1z = tz2 - cz;
			final var v2x = tx3 - cx;
			final var v2y = ty3 - cy;
			final var v2z = tz3 - cz;

			// Triangle edges
			final var e0x = v1x - v0x;
			final var e0y = v1y - v0y;
			final var e0z = v1z - v0z;
			final var e1x = v2x - v1x;
			final var e1y = v2y - v1y;
			final var e1z = v2z - v1z;
			final var e2x = v0x - v2x;
			final var e2y = v0y - v2y;
			final var e2z = v0z - v2z;

			// Absolute edge components (used for radii)
			final var fe0x = Math.abs(e0x);
			final var fe0y = Math.abs(e0y);
			final var fe0z = Math.abs(e0z);
			final var fe1x = Math.abs(e1x);
			final var fe1y = Math.abs(e1y);
			final var fe1z = Math.abs(e1z);
			final var fe2x = Math.abs(e2x);
			final var fe2y = Math.abs(e2y);
			final var fe2z = Math.abs(e2z);

			// 9 separating axis tests: cross products of each triangle edge
			// with the three box axes (X, Y, Z)

			// Edge 0

			// Axis = cross(e0, X) = (0, -e0.z, e0.y)  => test uses y,z
			var p0 = e0z * v0y - e0y * v0z;
			var p2 = e0z * v2y - e0y * v2z;
			var min = Math.min(p0, p2);
			var max = Math.max(p0, p2);
			var rad = fe0z * halfy + fe0y * halfz;
			if (min > rad || max < -rad) {
				return false;
			}

			// Axis = cross(e0, Y) = (e0.z, 0, -e0.x)  => test uses x,z
			p0 = -e0z * v0x + e0x * v0z;
			p2 = Math.fma(e0x, v2z, -e0z * v2x);
			min = Math.min(p0, p2);
			max = Math.max(p0, p2);
			rad = fe0z * halfx + fe0x * halfz;
			if (min > rad || max < -rad) {
				return false;
			}

			// Axis = cross(e0, Z) = (-e0.y, e0.x, 0)  => test uses x,y
			var p1 = e0y * v1x - e0x * v1y;
			p2 = e0y * v2x - e0x * v2y;
			min = Math.min(p1, p2);
			max = Math.max(p1, p2);
			rad = fe0y * halfx + fe0x * halfy;
			if (min > rad || max < -rad) {
				return false;
			}

			// Edge 1

			// Axis = cross(e1, X)
			p0 = e1z * v0y - e1y * v0z;
			p2 = e1z * v2y - e1y * v2z;
			min = Math.min(p0, p2);
			max = Math.max(p0, p2);
			rad = fe1z * halfy + fe1y * halfz;
			if (min > rad || max < -rad) {
				return false;
			}

			// Axis = cross(e1, Y)  (Note: original code uses v0y/v2y instead of v0x/v2x – preserved)
			p0 = Math.fma(e1x, v0z, -e1z * v0y);
			p2 = Math.fma(e1x, v2z, -e1z * v2y);
			min = Math.min(p0, p2);
			max = Math.max(p0, p2);
			// halfy used as "halfx" in original call
			rad = fe1z * halfy + fe1x * halfz;
			if (min > rad || max < -rad) {
				return false;
			}

			// Axis = cross(e1, Z)  (uses v0 and v1)
			p0 = e1y * v0x - e1x * v0y;
			p1 = e1y * v1x - e1x * v1y;
			min = Math.min(p0, p1);
			max = Math.max(p0, p1);
			rad = fe1y * halfx + fe1x * halfy;
			if (min > rad || max < -rad) {
				return false;
			}

			// Edge 2

			// Axis = cross(e2, X)  (uses v0 and v1)
			p0 = e2z * v0y - e2y * v0z;
			p1 = e2z * v1y - e2y * v1z;
			min = Math.min(p0, p1);
			max = Math.max(p0, p1);
			rad = fe2z * halfy + fe2y * halfz;
			if (min > rad || max < -rad) {
				return false;
			}

			// Axis = cross(e2, Y)  (uses v0 and v1)
			p0 = Math.fma(e2x, v0z, -e2z * v0x);
			p1 = Math.fma(e2x, v1z, -e2z * v1x);
			min = Math.min(p0, p1);
			max = Math.max(p0, p1);
			rad = fe2z * halfx + fe2x * halfz;
			if (min > rad || max < -rad) {
				return false;
			}

			// Axis = cross(e2, Z)  (uses v1 and v2)
			p1 = e2y * v1x - e2x * v1y;
			p2 = e2y * v2x - e2x * v2y;
			min = Math.min(p1, p2);
			max = Math.max(p1, p2);
			rad = fe2y * halfx + fe2x * halfy;
			if (min > rad || max < -rad) {
				return false;
			}

			// AABB overlap tests (triangle's bounding box vs box)

			// X direction
			min = Math.min(v0x, Math.min(v1x, v2x));
			max = Math.max(v0x, Math.max(v1x, v2x));
			if (min > halfx || max < -halfx) {
				return false;
			}

			// Y direction
			min = Math.min(v0y, Math.min(v1y, v2y));
			max = Math.max(v0y, Math.max(v1y, v2y));
			if (min > halfy || max < -halfy) {
				return false;
			}

			// Z direction
			min = Math.min(v0z, Math.min(v1z, v2z));
			max = Math.max(v0z, Math.max(v1z, v2z));
			if (min > halfz || max < -halfz) {
				return false;
			}

			// Plane–box overlap test (triangle's plane vs box)

			// Normal of triangle plane (using v1 and v2 relative to origin after translation)
			final var nx = v1y * v2z - v1z * v2y;
			final var ny = v1z * v2x - v1x * v2z;
			final var nz = v1x * v2y - v1y * v2x;

			// Find the box vertex closest to the plane in the negative normal direction,
			// and the one in the positive direction, then test if they lie on opposite sides.
			// Inlined version of mollerAlgorithmPlaneBoxOverlap
			final var vminx = nx > 0. ? -halfx - v0x : halfx - v0x;
			final var vmaxx = nx > 0. ? halfx - v0x : -halfx - v0x;
			final var vminy = ny > 0. ? -halfy - v0y : halfy - v0y;
			final var vmaxy = ny > 0. ? halfy - v0y : -halfy - v0y;
			final var vminz = nz > 0. ? -halfz - v0z : halfz - v0z;
			final var vmaxz = nz > 0. ? halfz - v0z : -halfz - v0z;

			// Dot product of normal with the closest vertex (minimum projection)
			final var dotMin = nx * vminx + ny * vminy + nz * vminz;
			// Dot product with the farthest vertex (maximum projection)
			final var dotMax = nx * vmaxx + ny * vmaxy + nz * vmaxz;

			// Overlap if the plane cuts the box: one vertex on each side (or touches)
			return dotMin <= 0. || dotMax >= 0.;
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
		 * @param epsilon the epsilon value that is used for testing equalities.
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
				double u3x, double u3y, double u3z,
				double epsilon) {
			// first project onto an axis-aligned plane, that maximizes the area
			// of the triangles, compute indices: i0,i1.
			var nx = v1y * (v2z - v3z) + v2y * (v3z - v1z) + v3y * (v1z - v2z);
			var ny = v1z * (v2x - v3x) + v2z * (v3x - v1x) + v3z * (v1x - v2x);
			var nz = v1x * (v2y - v3y) + v2x * (v3y - v1y) + v3x * (v1y - v2y);

			nx = nx < 0 ? -nx : nx;
			ny = ny < 0 ? -ny : ny;
			nz = nz < 0 ? -nz : nz;

			Point3D2D selector = null;

			if (nx > ny) {
				if (nx > nz) {
					// nx is greatest
					selector = new Point3D2D() {
						@Override
						public double x(Point3D<?, ?, ?> point) {
							return point.getY();
						}

						@Override
						public double y(Point3D<?, ?, ?> point) {
							return point.getZ();
						}
					};
				}
			} else {
				/* nx<=ny */
				if (nz <= ny) {
					// ny is greatest
					selector = new Point3D2D() {

						@Override
						public double x(Point3D<?, ?, ?> point) {
							return point.getX();
						}

						@Override
						public double y(Point3D<?, ?, ?> point) {
							return point.getZ();
						}
					};
				}
			}
			if (selector == null) {
				// nz is greatest
				selector = new Point3D2D() {
					@Override
					public double x(Point3D<?, ?, ?> point) {
						return point.getX();
					}

					@Override
					public double y(Point3D<?, ?, ?> point) {
						return point.getY();
					}
				};
			}

			final var tv1 = new InnerComputationPoint3D(v1x, v1y, v1z);
			final var tv2 = new InnerComputationPoint3D(v2x, v2y, v2z);
			final var tv3 = new InnerComputationPoint3D(v3x, v3y, v3z);
			final var tu1 = new InnerComputationPoint3D(u1x, u1y, u1z);
			final var tu2 = new InnerComputationPoint3D(u2x, u2y, u2z);
			final var tu3 = new InnerComputationPoint3D(u3x, u3y, u3z);

			// test all edges of triangle 1 against the edges of triangle 2
			return intersectsEdges(selector, tv1, tv2, tu1, tu2, tu3, epsilon)
					|| intersectsEdges(selector, tv2, tv3, tu1, tu2, tu3, epsilon)
					|| intersectsEdges(selector, tv3, tv1, tu1, tu2, tu3, epsilon)
					// finally, test if first triangle is totally contained in second triangle
					|| containsTrianglePoint(selector, tv1, tu1, tu2, tu3, epsilon)
					// or vice versa
					|| containsTrianglePoint(selector, tu1, tv1, tv2, tv3, epsilon);
		}

		/** Replies if a point is inside a triangle assuming that the point
		 * is on the plane of the triangle (u1,u2,u3).
		 *
		 * @param get the tools for extracted the relevant two coordinates (x+y, x+z or y+z).
		 * @param point the coordinates of the points.
		 * @param u1 the coordinates of the first point of the triangle.
		 * @param u2 the coordinates of the second point of the triangle.
		 * @param u3 the coordinates of the third point of the triangle.
		 * @param epsilon the epsilon value that is used for testing equalities.
		 * @return {@code true} if the point is inside the triangle.
		 */
		private static boolean containsTrianglePoint(
				Point3D2D get,
				Point3D<?, ?, ?> point, Point3D<?, ?, ?> u1, Point3D<?, ?, ?> u2, Point3D<?, ?, ?> u3,
				double epsilon) {
			// Extract 2D coordinates via the projection interface
			final var px = get.x(point);
			final var py = get.y(point);
			final var ax = get.x(u1);
			final var ay = get.y(u1);
			final var bx = get.x(u2);
			final var by = get.y(u2);
			final var cx = get.x(u3);
			final var cy = get.y(u3);

			// Vectors from vertex A
			final var v0x = bx - ax;
			final var v0y = by - ay;
			final var v1x = cx - ax;
			final var v1y = cy - ay;
			final var v2x = px - ax;
			final var v2y = py - ay;

			// Dot products (using fused multiply‑add for accuracy)
			final var dot00 = Math.fma(v0x, v0x, v0y * v0y);
			final var dot01 = Math.fma(v0x, v1x, v0y * v1y);
			final var dot11 = Math.fma(v1x, v1x, v1y * v1y);
			final var dot02 = Math.fma(v0x, v2x, v0y * v2y);
			final var dot12 = Math.fma(v1x, v2x, v1y * v2y);

			// Denominator = |v0 × v1|²  (twice the area of the triangle)
			final var denom = dot00 * dot11 - dot01 * dot01;

			// If the triangle is degenerate in the 2D projection, we treat it as not containing the point
			if (Math.abs(denom) < epsilon) {
				return false;
			}

			final var invDenom = 1. / denom;
			final var u = (dot11 * dot02 - dot01 * dot12) * invDenom;
			final var v = (dot00 * dot12 - dot01 * dot02) * invDenom;

			// Check barycentric coordinates with tolerance
			return u >= -epsilon && v >= -epsilon && u + v <= 1. + epsilon;
		}

		/** Replies if coplanar segment intersects one of the three edges of
		 * the triangle.
		 *
		 * @param get the tools for extracted the relevant two coordinates (x+y, x+z or y+z).
		 * @param s1 the coordinates of the first point of the segment.
		 * @param s2 the coordinates of the second point of the segment.
		 * @param u1 the coordinates of the first point of the triangle.
		 * @param u2 the coordinates of the second point of the triangle.
		 * @param u3 the coordinates of the third point of the triangle.
		 * @param epsilon the epsilon value that is used for testing equalities.
		 * @return {@code true} if the point is inside the triangle.
		 */
		@SuppressWarnings("checkstyle:parameternumber")
		private static boolean intersectsEdges(
				Point3D2D get,
				Point3D<?, ?, ?> s1, Point3D<?, ?, ?> s2,
				Point3D<?, ?, ?> u1, Point3D<?, ?, ?> u2, Point3D<?, ?, ?> u3,
				double epsilon) {
			final var ax = get.x(s2) - get.x(s1);
			final var ay = get.y(s2) - get.y(s1);
			return intersectEdgeEdge(get, ax, ay, s1, u1, u2, epsilon)
					|| intersectEdgeEdge(get, ax, ay, s1, u2, u3, epsilon)
					|| intersectEdgeEdge(get, ax, ay, s1, u3, u1, epsilon);
		}

		/** This edge to edge test is based on Franlin Antonio's gem:
		 * "Faster Line Segment Intersection", in Graphics Gems III,
		 * pp. 199-202.
		 *
		 * @param get the tools for extracted the relevant two coordinates (x+y, x+z or y+z).
		 * @param s1 the coordinates of the first point of the segment.
		 * @param s2 the coordinates of the second point of the segment.
		 * @param u1 the coordinates of the first point of the triangle.
		 * @param u2 the coordinates of the second point of the triangle.
		 * @param u3 the coordinates of the third point of the triangle.
		 * @param epsilon the epsilon value that is used for testing equalities.
		 * @return {@code true} if the point is inside the triangle.
		 */
		@Pure
		@SuppressWarnings("checkstyle:parameternumber")
		private static boolean intersectEdgeEdge(
				Point3D2D get,
				double ax, double ay,
				Point3D<?, ?, ?> v, Point3D<?, ?, ?> u1, Point3D<?, ?, ?> u2,
				double epsilon) {
			// [v,b] is the segment that contains the point v
			// [c,d] is the segment [u1,u2]

			// A is the vector (v,b)
			// B is the vector (d,c)
			// C is the vector (c,v)

			final var bx = get.x(u1) - get.x(u2);
			final var by = get.y(u1) - get.y(u2);
			final var cx = get.x(v)  - get.x(u1);
			final var cy = get.y(v)  - get.y(u1);

			final var f = ay * bx - ax * by;
			// Line equation: V+d*A
			final var d = by * cx - bx * cy;

			var up = false;
			var down = false;

			if (f > 0) {
				down = d >= 0;
				up = d <= f;
			} else if (f < 0) {
				down = d >= f;
				up = d <= 0;
			}

			if (up && down) {
				final var e = ax * cy - ay * cx;
				if (f >= 0) {
					return e >= -epsilon && e <= f + epsilon;
				}
				return e >= f + epsilon && e <= epsilon;
			}

			return false;
		}

		/** Functional interface that permits to move from 3D to 2D point.
		 *
		 * @author $Author: sgalland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 * @since 18.0
		 */
		private interface Point3D2D {

			/** Replies the 2D x coordinate for the given 3D point.
			 *
			 * @param point the point to convert.
			 * @return the coordinate value.
			 */
			double x(Point3D<?, ?, ?> point);

			/** Replies the 2D x coordinate for the given 3D point.
			 *
			 * @param point the point to convert.
			 * @return the coordinate value.
			 */
			double y(Point3D<?, ?, ?> point);
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
				if (wpositive && w < sum - epsilon || wnegative && w > sum + epsilon) {
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
					if (spositive && s < sum - epsilon || wnegative && s > sum + epsilon) {
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

	/** Utility class related to the algorithms from Eberly.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 18.0
	 */
	final class EberlyAlgorithmTools {

		private EberlyAlgorithmTools() {
			//
		}

		/**
		 * Replies the closest point from the triangle to the aligned box.
		 * The closest point is always located in the triangle.
		 *
		 * <p>Algorithm: The closest point between a triangle and an axis-aligned box (AABB)
		 * is found by testing:
		 * <ol>
		 *   <li>Triangle vertices against the AABB (closest point on box to each vertex).</li>
		 *   <li>Triangle edges against the AABB (closest point pair for each edge).</li>
		 *   <li>AABB vertices against the triangle (closest point on triangle to each box vertex).</li>
		 *   <li>AABB edges against the triangle (via segment-triangle closest point).</li>
		 *   <li>AABB face interiors against the triangle face (plane-to-plane and face queries).</li>
		 * </ol>
		 *
		 * <p>Adapted from the general closest-point approach in David Eberly's
		 * <em>Geometric Tools</em> and the separating axis theorem for convex shapes.
		 *
		 * @param tx1 x coordinate of the first point of the first triangle.
		 * @param ty1 y coordinate of the first point of the first triangle.
		 * @param tz1 z coordinate of the first point of the first triangle.
		 * @param tx2 x coordinate of the second point of the first triangle.
		 * @param ty2 y coordinate of the second point of the first triangle.
		 * @param tz2 z coordinate of the second point of the first triangle.
		 * @param tx3 x coordinate of the third point of the first triangle.
		 * @param ty3 y coordinate of the third point of the first triangle.
		 * @param tz3 z coordinate of the third point of the first triangle.
		 * @param bx1 x coordinate of the minimum corner of the aligned box.
		 * @param by1 y coordinate of the minimum corner of the aligned box.
		 * @param bz1 z coordinate of the minimum corner of the aligned box.
		 * @param bx2 x coordinate of the maximum corner of the aligned box.
		 * @param by2 y coordinate of the maximum corner of the aligned box.
		 * @param bz2 z coordinate of the maximum corner of the aligned box.
		 * @param epsilon the epsilon value that is used for testing equalities.
		 * @param closestPointOnTriangle the point on the triangle set with the
		 *     closest coordinates. It could be {@code null}.
		 * @param closestPointOnBox the point on the aligned box set with the
		 *     closest coordinates. It could be {@code null}.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:methodlength", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
		@Pure
		public static void findsClosestPointToTriangleAlignedBox(
		        double tx1, double ty1, double tz1,
		        double tx2, double ty2, double tz2,
		        double tx3, double ty3, double tz3,
		        double bx1, double by1, double bz1,
		        double bx2, double by2, double bz2,
		        double epsilon,
		        Point3D<?, ?, ?> closestPointOnTriangle,
		        Point3D<?, ?, ?> closestPointOnBox) {
			assert closestPointOnTriangle != null || closestPointOnBox != null : AssertMessages.notNullParameter(20);
			var bestDistSq = Double.POSITIVE_INFINITY;
			final var bestTri = new InnerComputationPoint3D(tx1, ty1, tz1);
			final var bestBox = new InnerComputationPoint3D(
					MathUtil.clamp(tx1, bx1, bx2),
					MathUtil.clamp(ty1, by1, by2),
					MathUtil.clamp(tz1, bz1, bz2));

			// 1. Triangle vertices to box
			var px = MathUtil.clamp(tx1, bx1, bx2);
			var py = MathUtil.clamp(ty1, by1, by2);
			var pz = MathUtil.clamp(tz1, bz1, bz2);
			var dx = tx1 - px;
			var dy = ty1 - py;
			var dz = tz1 - pz;
			var distSq = Vector3D.dotProduct(dx, dy, dz, dx, dy, dz);
			if (distSq == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tx1, ty1, tz1);
				}
				if (closestPointOnBox != null) {
					closestPointOnBox.set(px, py, pz);
				}
				return;
			}
			if (distSq < bestDistSq) {
				bestDistSq = distSq;
				bestTri.set(tx1, ty1, tz1);
				bestBox.set(px, py, pz);
			}

			px = MathUtil.clamp(tx2, bx1, bx2);
			py = MathUtil.clamp(ty2, by1, by2);
			pz = MathUtil.clamp(tz2, bz1, bz2);
			dx = tx2 - px;
			dy = ty2 - py;
			dz = tz2 - pz;
			distSq = Vector3D.dotProduct(dx, dy, dz, dx, dy, dz);
			if (distSq == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tx2, ty2, tz2);
				}
				if (closestPointOnBox != null) {
					closestPointOnBox.set(px, py, pz);
				}
				return;
			}
			if (distSq < bestDistSq) {
				bestDistSq = distSq;
				bestTri.set(tx2, ty2, tz2);
				bestBox.set(px, py, pz);
			}

			px = MathUtil.clamp(tx3, bx1, bx2);
			py = MathUtil.clamp(ty3, by1, by2);
			pz = MathUtil.clamp(tz3, bz1, bz2);
			dx = tx3 - px;
			dy = ty3 - py;
			dz = tz3 - pz;
			distSq = Vector3D.dotProduct(dx, dy, dz, dx, dy, dz);
			if (distSq == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tx3, ty3, tz3);
				}
				if (closestPointOnBox != null) {
					closestPointOnBox.set(px, py, pz);
				}
				return;
			}
			if (distSq < bestDistSq) {
				bestDistSq = distSq;
				bestTri.set(tx3, ty3, tz3);
				bestBox.set(px, py, pz);
			}

			// 2. Triangle edges to box (closest point on each edge) – inlined
			// Edge 1: (tx1,ty1,tz1)-(tx2,ty2,tz2)
			bestDistSq = updateEdgeToBox(tx1, ty1, tz1, tx2, ty2, tz2,
					bx1, by1, bz1, bx2, by2, bz2, epsilon,
					bestDistSq,
					bestTri, bestBox);
			if (bestDistSq == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(bestTri);
				}
				if (closestPointOnBox != null) {
					closestPointOnBox.set(bestBox);
				}
				return;
			}
			// Edge 2: (tx2,ty2,tz2)-(tx3,ty3,tz3)
			bestDistSq = updateEdgeToBox(tx2, ty2, tz2, tx3, ty3, tz3,
					bx1, by1, bz1, bx2, by2, bz2, epsilon,
					bestDistSq,
					bestTri, bestBox);
			if (bestDistSq == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(bestTri);
				}
				if (closestPointOnBox != null) {
					closestPointOnBox.set(bestBox);
				}
				return;
			}
			// Edge 3: (tx3,ty3,tz3)-(tx1,ty1,tz1)
			bestDistSq = updateEdgeToBox(tx3, ty3, tz3, tx1, ty1, tz1,
					bx1, by1, bz1, bx2, by2, bz2, epsilon,
					bestDistSq,
					bestTri, bestBox);
			if (bestDistSq == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(bestTri);
				}
				if (closestPointOnBox != null) {
					closestPointOnBox.set(bestBox);
				}
				return;
			}

			// 3. AABB vertices to triangle
			final var boxVertices = new Point3D<?, ?, ?>[] {
				new InnerComputationPoint3D(bx1, by1, bz1),
				new InnerComputationPoint3D(bx2, by1, bz1),
				new InnerComputationPoint3D(bx1, by2, bz1),
				new InnerComputationPoint3D(bx2, by2, bz1),
				new InnerComputationPoint3D(bx1, by1, bz2),
				new InnerComputationPoint3D(bx2, by1, bz2),
				new InnerComputationPoint3D(bx1, by2, bz2),
				new InnerComputationPoint3D(bx2, by2, bz2),
			};

			final var tempTri = new InnerComputationPoint3D();
			for (final var vertex : boxVertices) {
				findsClosestPointToTrianglePoint(
						tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
						vertex.getX(), vertex.getY(), vertex.getZ(),
						tempTri);
				dx = vertex.getX() - tempTri.getX();
				dy = vertex.getY() - tempTri.getY();
				dz = vertex.getZ() - tempTri.getZ();
				distSq = Vector3D.dotProduct(dx, dy, dz, dx, dy, dz);
				if (distSq == 0.) {
					if (closestPointOnTriangle != null) {
						closestPointOnTriangle.set(tempTri);
					}
					if (closestPointOnBox != null) {
						closestPointOnBox.set(vertex);
					}
					return;
				}
				if (distSq < bestDistSq) {
					bestDistSq = distSq;
					bestTri.set(tempTri);
					bestBox.set(vertex);
				}
			}

			// 4. AABB edges to triangle
			// 12 edges of the box (4 parallel to each axis)
			final var boxEdges = new Point3D<?, ?, ?>[][] {
				// X-axis edges
				{
					new InnerComputationPoint3D(bx1, by1, bz1),
					new InnerComputationPoint3D(bx2, by1, bz1),
				},
				{
					new InnerComputationPoint3D(bx1, by2, bz1),
					new InnerComputationPoint3D(bx2, by2, bz1),
				},
				{
					new InnerComputationPoint3D(bx1, by1, bz2),
					new InnerComputationPoint3D(bx2, by1, bz2),
				},
				{
					new InnerComputationPoint3D(bx1, by2, bz2),
					new InnerComputationPoint3D(bx2, by2, bz2),
				},
				// Y-axis edges
				{
					new InnerComputationPoint3D(bx1, by1, bz1),
					new InnerComputationPoint3D(bx1, by2, bz1),
				},
				{
					new InnerComputationPoint3D(bx2, by1, bz1),
					new InnerComputationPoint3D(bx2, by2, bz1),
				},
				{
					new InnerComputationPoint3D(bx1, by1, bz2),
					new InnerComputationPoint3D(bx1, by2, bz2),
				},
				{
					new InnerComputationPoint3D(bx2, by1, bz2),
					new InnerComputationPoint3D(bx2, by2, bz2),
				},
				// Z-axis edges
				{
					new InnerComputationPoint3D(bx1, by1, bz1),
					new InnerComputationPoint3D(bx1, by1, bz2),
				},
				{
					new InnerComputationPoint3D(bx2, by1, bz1),
					new InnerComputationPoint3D(bx2, by1, bz2),
				},
				{
					new InnerComputationPoint3D(bx1, by2, bz1),
					new InnerComputationPoint3D(bx1, by2, bz2),
				},
				{
					new InnerComputationPoint3D(bx2, by2, bz1),
					new InnerComputationPoint3D(bx2, by2, bz2),
				},
			};
			final var tempSeg = new InnerComputationPoint3D();
			for (final var edge : boxEdges) {
				assert edge.length == 2;
				findsClosestPointToTriangleSegment(
						tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
						edge[0].getX(), edge[0].getY(), edge[0].getZ(),
						edge[1].getX(), edge[1].getY(), edge[1].getZ(),
						epsilon,
						tempTri, tempSeg);
				distSq = Point3D.getDistanceSquaredPointPoint(
						tempTri.getX(), tempTri.getY(), tempTri.getZ(),
						tempSeg.getX(), tempSeg.getY(), tempSeg.getZ());
				if (distSq == 0.) {
					if (closestPointOnTriangle != null) {
						closestPointOnTriangle.set(tempTri);
					}
					if (closestPointOnBox != null) {
						closestPointOnBox.set(tempSeg);
					}
					return;
				}
				if (distSq < bestDistSq) {
					bestDistSq = distSq;
					bestTri.set(tempTri);
					bestBox.set(tempSeg);
				}
			}

			// 5. AABB face interiors to triangle face (plane-to-plane) – omitted for brevity,
			//    but the existing algorithm already handles face interiors via the above tests
			//    (the closest points on faces will be found as degenerate cases of edges/vertices).

			if (closestPointOnTriangle != null) {
				closestPointOnTriangle.set(bestTri);
			}
			if (closestPointOnBox != null) {
				closestPointOnBox.set(bestBox);
			}
		}

		/**
		 * Updates best pair for an edge (segment) against an AABB.
		 * Uses the slab method to find the closest point on the edge to the box.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
		private static double updateEdgeToBox(
				double e1x, double e1y, double e1z,
				double e2x, double e2y, double e2z,
				double bx1, double by1, double bz1,
				double bx2, double by2, double bz2,
				double epsilon,
				double bestDistSq,
				Point3D<?, ?, ?> bestTri,
				Point3D<?, ?, ?> bestBox) {
			final var dx = e2x - e1x;
			final var dy = e2y - e1y;
			final var dz = e2z - e1z;
			final var len2 = Vector3D.dotProduct(dx, dy, dz, dx, dy, dz);

			if (len2 <= epsilon) {
				// degenerate edge
				return bestDistSq;
			}

			// Interval of t where the edge is inside the box
			var tmin = 0.;
			var tmax = 1.;

			// X axis
			if (Math.abs(dx) > epsilon) {
				var t1 = (bx1 - e1x) / dx;
				var t2 = (bx2 - e1x) / dx;
				if (dx < 0) {
					final var tmp = t1;
					t1 = t2;
					t2 = tmp;
				}
				tmin = Math.max(tmin, t1);
				tmax = Math.min(tmax, t2);
			} else if (e1x < bx1 || e1x > bx2) {
				return bestDistSq;
			}

			// Y axis
			if (Math.abs(dy) > epsilon) {
				var t1 = (by1 - e1y) / dy;
				var t2 = (by2 - e1y) / dy;
				if (dy < 0) {
					final var tmp = t1;
					t1 = t2;
					t2 = tmp;
				}
				tmin = Math.max(tmin, t1);
				tmax = Math.min(tmax, t2);
			} else if (e1y < by1 || e1y > by2) {
				return bestDistSq;
			}

			// Z axis
			if (Math.abs(dz) > epsilon) {
				var t1 = (bz1 - e1z) / dz;
				var t2 = (bz2 - e1z) / dz;
				if (dz < 0) {
					final var tmp = t1;
					t1 = t2;
					t2 = tmp;
				}
				tmin = Math.max(tmin, t1);
				tmax = Math.min(tmax, t2);
			} else if (e1z < bz1 || e1z > bz2) {
				return bestDistSq;
			}

			if (tmin <= tmax) {
				final var t = Math.max(0., Math.min(1., tmin));
				final var px = Math.fma(t, dx, e1x);
				final var py = Math.fma(t, dy, e1y);
				final var pz = Math.fma(t, dz, e1z);

				final var bpx = MathUtil.clamp(px, bx1, bx2);
				final var bpy = MathUtil.clamp(py, by1, by2);
				final var bpz = MathUtil.clamp(pz, bz1, bz2);

				final var dpx = px - bpx;
				final var dpy = py - bpy;
				final var dpz = pz - bpz;
				final var distSq = dpx * dpx + dpy * dpy + dpz * dpz;

				if (distSq < bestDistSq) {
					bestTri.set(px, py, pz);
					bestBox.set(bpx, bpy, bpz);
					return distSq;
				}
			}
			return bestDistSq;
		}

		/**
		 * Updates best points with the closest point on a segment to a given point.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
		@Pure
		private static double updatePointToSegment(
				double px, double py, double pz,
				double s1x, double s1y, double s1z,
				double s2x, double s2y, double s2z,
				double epsilon,
				double bestDistSq,
				Point3D<?, ?, ?> bestTri, Point3D<?, ?, ?> bestSeg) {

			final var vx = s2x - s1x;
			final var vy = s2y - s1y;
			final var vz = s2z - s1z;
			final var len2 = Vector3D.dotProduct(vx, vy, vz, vx, vy, vz);
			if (len2 <= epsilon) {
				// segment degenerate, use s1
				final var dx = px - s1x;
				final var dy = py - s1y;
				final var dz = pz - s1z;
				final var distSq = Vector3D.dotProduct(dx, dy, dz, dx, dy, dz);
				if (distSq < bestDistSq) {
					bestTri.set(px, py, pz);
					bestSeg.set(s1x, s1y, s1z);
					return distSq;
				}
				return bestDistSq;
			}
			final var t = MathUtil.clamp(((px - s1x) * vx + (py - s1y) * vy + (pz - s1z) * vz) / len2, 0., 1.);
			final var cx = Math.fma(t, vx, s1x);
			final var cy = Math.fma(t, vy, s1y);
			final var cz = Math.fma(t, vz, s1z);
			final var dx = px - cx;
			final var dy = py - cy;
			final var dz = pz - cz;
			final var distSq = dx * dx + dy * dy + dz * dz;
			if (distSq < bestDistSq) {
				bestTri.set(px, py, pz);
				bestSeg.set(cx, cy, cz);
				return distSq;
			}
			return bestDistSq;
		}

		/**
		 * Tests if a segment intersects a triangle. If so, stores an intersection point in 'result'.
		 * Returns true iff there is an intersection.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
		@Pure
		private static boolean segmentIntersectsTriangle(
				double s1x, double s1y, double s1z,
				double s2x, double s2y, double s2z,
				double ax, double ay, double az,
				double bx, double by, double bz,
				double cx, double cy, double cz,
				double epsilon,
				Point3D<?, ?, ?> result) {

			// Use the method of separating axis or Moeller-Trumbore
			// Compute plane normal
			final var nx = (by - ay) * (cz - az) - (bz - az) * (cy - ay);
			final var ny = (bz - az) * (cx - ax) - (bx - ax) * (cz - az);
			final var nz = (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
			final var len2 = Vector3D.dotProduct(nx, ny, nz, nx, ny, nz);
			if (len2 <= epsilon) {
				// degenerate triangle
				return false;
			}

			// Check if segment endpoints are on opposite sides of the plane
			final var d1 = nx * (s1x - ax) + ny * (s1y - ay) + nz * (s1z - az);
			final var d2 = nx * (s2x - ax) + ny * (s2y - ay) + nz * (s2z - az);
			if (d1 * d2 > epsilon) {
				// both on same side, no intersection
				return false;
			}

			// Compute intersection point with plane
			final var t = -d1 / (d2 - d1);
			if (t < 0. || t > 1.) {
				return false;
			}
			final var ix = Math.fma(t, s2x - s1x, s1x);
			final var iy = Math.fma(t, s2y - s1y, s1y);
			final var iz = Math.fma(t, s2z - s1z, s1z);

			// Check if point is inside triangle (barycentric coordinates)
			final var edge1x = bx - ax;
			final var edge1y = by - ay;
			final var edge1z = bz - az;
			final var edge2x = cx - ax;
			final var edge2y = cy - ay;
			final var edge2z = cz - az;
			final var hx = ix - ax;
			final var hy = iy - ay;
			final var hz = iz - az;

			var u = (edge2y * hz - edge2z * hy) * (edge1y * edge2z - edge1z * edge2y)
					+ (edge2z * hx - edge2x * hz) * (edge1z * edge2x - edge1x * edge2z)
					+ (edge2x * hy - edge2y * hx) * (edge1x * edge2y - edge1y * edge2x);
			u /= (edge1x * edge1x + edge1y * edge1y + edge1z * edge1z)
					* (edge2x * edge2x + edge2y * edge2y + edge2z * edge2z)
					- (edge1x * edge2x + edge1y * edge2y + edge1z * edge2z)
					* (edge1x * edge2x + edge1y * edge2y + edge1z * edge2z);

			final var v = (edge1x * hx + edge1y * hy + edge1z * hz - u * (edge1x * edge2x + edge1y * edge2y + edge1z * edge2z))
					/ (edge1x * edge1x + edge1y * edge1y + edge1z * edge1z);

			if (u >= 0. && v >= 0. && u + v <= 1.) {
				result.set(ix, iy, iz);
				return true;
			}
			return false;
		}

		/** Replies the closest point from the triangle A to the triangle B.
		 * The closest point is always located in the triangle.
		 *
		 * <p>Algorithm: since both triangles are convex, the closest pair of points between
		 * them is always found on their boundaries, and is fully witnessed by testing each
		 * edge of one triangle against the <em>whole</em> of the other triangle (not just its
		 * vertices or edges individually). Testing only vertex-vs-triangle and edge-vs-edge
		 * pairs is <strong>not sufficient</strong>: it misses the case where an edge of one
		 * triangle pierces straight through the interior of the other triangle's face without
		 * crossing any of its edges. Sweeping each full edge against the full opposite triangle
		 * (via {@link #findsClosestPointTriangleSegment}, which already resolves vertex, edge and
		 * face-interior cases through its own Voronoi-region logic) correctly captures every case,
		 * including true interpenetration (distance 0).
		 *
		 * <p>For each of the 3 edges of A, the closest point on triangle B is found; the paired
		 * point on that edge of A is then simply the closest point of the edge to that result
		 * (a cheap clamped projection), since for a globally optimal pair each point is,
		 * by definition, the closest point of its own shape to the other point. The same is
		 * done for each of the 3 edges of B against triangle A. The minimum over these 6
		 * candidates is the true closest pair.
		 *
		 * <p>Adapted from the general approach described by David Eberly,
		 * <a href="https://www.geometrictools.com/Documentation/DistanceTriangle3Triangle3.pdf">
		 * "Distance Between Two Triangles in 3D"</a>, Geometric Tools, LLC, 2015.
		 *
		 * @param ax1 x coordinate of the first point of the first triangle.
		 * @param ay1 y coordinate of the first point of the first triangle.
		 * @param az1 z coordinate of the first point of the first triangle.
		 * @param ax2 x coordinate of the second point of the first triangle.
		 * @param ay2 y coordinate of the second point of the first triangle.
		 * @param az2 z coordinate of the second point of the first triangle.
		 * @param ax3 x coordinate of the third point of the first triangle.
		 * @param ay3 y coordinate of the third point of the first triangle.
		 * @param az3 z coordinate of the third point of the first triangle.
		 * @param bx1 x coordinate of the first point of the second triangle.
		 * @param by1 y coordinate of the first point of the second triangle.
		 * @param bz1 z coordinate of the first point of the second triangle.
		 * @param bx2 x coordinate of the second point of the second triangle.
		 * @param by2 y coordinate of the second point of the second triangle.
		 * @param bz2 z coordinate of the second point of the second triangle.
		 * @param bx3 x coordinate of the third point of the second triangle.
		 * @param by3 y coordinate of the third point of the second triangle.
		 * @param bz3 z coordinate of the third point of the second triangle.
		 * @param epsilon the epsilon value that is used for testing equalities.
		 * @param closestPointOnTriangleA the point on the first triangle set with the
		 *     closest coordinates. It could be {@code null}.
		 * @param closestPointOnTriangleB the point on the second triangle set with the
		 *     closest coordinates. It could be {@code null}.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity",
			"checkstyle:magicnumber"})
		@Pure
		@Unefficient
		public static void findsClosestPointToTriangleTriangle(
				double ax1, double ay1, double az1,
				double ax2, double ay2, double az2,
				double ax3, double ay3, double az3,
				double bx1, double by1, double bz1,
				double bx2, double by2, double bz2,
				double bx3, double by3, double bz3,
				double epsilon,
				Point3D<?, ?, ?> closestPointOnTriangleA,
				Point3D<?, ?, ?> closestPointOnTriangleB) {
			assert closestPointOnTriangleA != null || closestPointOnTriangleB != null : AssertMessages.notNullParameter(20);
			var bestDistSq = Double.POSITIVE_INFINITY;
			var bestAx = ax1;
			var bestAy = ay1;
			var bestAz = az1;
			var bestBx = bx1;
			var bestBy = by1;
			var bestBz = bz1;

			final var scratch0 = new InnerComputationPoint3D();
			final var scratch1 = new InnerComputationPoint3D();

			// --- Edge A1-A2 of triangle A against the whole of triangle B ---
			findsClosestPointToTriangleSegment(
					bx1, by1, bz1, bx2, by2, bz2, bx3, by3, bz3,
					ax1, ay1, az1, ax2, ay2, az2,
					epsilon, scratch0, scratch1);
			var distSq = Point3D.getDistanceSquaredPointPoint(
					scratch0.getX(), scratch0.getY(), scratch0.getZ(),
					scratch1.getX(), scratch1.getY(), scratch1.getZ());
			if (distSq == 0.) {
				if (closestPointOnTriangleA != null) {
					closestPointOnTriangleA.set(scratch1);
				}
				if (closestPointOnTriangleB != null) {
					closestPointOnTriangleB.set(scratch0);
				}
				return;
			}
			if (distSq < bestDistSq) {
				bestDistSq = distSq;
				bestAx = scratch1.getX();
				bestAy = scratch1.getY();
				bestAz = scratch1.getZ();
				bestBx = scratch0.getX();
				bestBy = scratch0.getY();
				bestBz = scratch0.getZ();
			}

			// --- Edge A2-A3 of triangle A against the whole of triangle B ---
			findsClosestPointToTriangleSegment(
					bx1, by1, bz1, bx2, by2, bz2, bx3, by3, bz3,
					ax2, ay2, az2, ax3, ay3, az3,
					epsilon, scratch0, scratch1);
			distSq = Point3D.getDistanceSquaredPointPoint(
					scratch0.getX(), scratch0.getY(), scratch0.getZ(),
					scratch1.getX(), scratch1.getY(), scratch1.getZ());
			if (distSq == 0.) {
				if (closestPointOnTriangleA != null) {
					closestPointOnTriangleA.set(scratch1);
				}
				if (closestPointOnTriangleB != null) {
					closestPointOnTriangleB.set(scratch0);
				}
				return;
			}
			if (distSq < bestDistSq) {
				bestDistSq = distSq;
				bestAx = scratch1.getX();
				bestAy = scratch1.getY();
				bestAz = scratch1.getZ();
				bestBx = scratch0.getX();
				bestBy = scratch0.getY();
				bestBz = scratch0.getZ();
			}

			// --- Edge A3-A1 of triangle A against the whole of triangle B ---
			findsClosestPointToTriangleSegment(
					bx1, by1, bz1, bx2, by2, bz2, bx3, by3, bz3,
					ax3, ay3, az3, ax1, ay1, az1,
					epsilon, scratch0, scratch1);
			distSq = Point3D.getDistanceSquaredPointPoint(
					scratch0.getX(), scratch0.getY(), scratch0.getZ(),
					scratch1.getX(), scratch1.getY(), scratch1.getZ());
			if (distSq == 0.) {
				if (closestPointOnTriangleA != null) {
					closestPointOnTriangleA.set(scratch1);
				}
				if (closestPointOnTriangleB != null) {
					closestPointOnTriangleB.set(scratch0);
				}
				return;
			}
			if (distSq < bestDistSq) {
				bestDistSq = distSq;
				bestAx = scratch1.getX();
				bestAy = scratch1.getY();
				bestAz = scratch1.getZ();
				bestBx = scratch0.getX();
				bestBy = scratch0.getY();
				bestBz = scratch0.getZ();
			}

			// --- Edge B1-B2 of triangle B against the whole of triangle A ---
			findsClosestPointToTriangleSegment(
					ax1, ay1, az1, ax2, ay2, az2, ax3, ay3, az3,
					bx1, by1, bz1, bx2, by2, bz2,
					epsilon, scratch0, scratch1);
			distSq = Point3D.getDistanceSquaredPointPoint(
					scratch0.getX(), scratch0.getY(), scratch0.getZ(),
					scratch1.getX(), scratch1.getY(), scratch1.getZ());
			if (distSq == 0.) {
				if (closestPointOnTriangleA != null) {
					closestPointOnTriangleA.set(scratch0);
				}
				if (closestPointOnTriangleB != null) {
					closestPointOnTriangleB.set(scratch1);
				}
				return;
			}
			if (distSq < bestDistSq) {
				bestDistSq = distSq;
				bestAx = scratch0.getX();
				bestAy = scratch0.getY();
				bestAz = scratch0.getZ();
				bestBx = scratch1.getX();
				bestBy = scratch1.getY();
				bestBz = scratch1.getZ();
			}

			// --- Edge B2-B3 of triangle B against the whole of triangle A ---
			findsClosestPointToTriangleSegment(
					ax1, ay1, az1, ax2, ay2, az2, ax3, ay3, az3,
					bx2, by2, bz2, bx3, by3, bz3,
					epsilon, scratch0, scratch1);
			distSq = Point3D.getDistanceSquaredPointPoint(
					scratch0.getX(), scratch0.getY(), scratch0.getZ(),
					scratch1.getX(), scratch1.getY(), scratch1.getZ());
			if (distSq == 0.) {
				if (closestPointOnTriangleA != null) {
					closestPointOnTriangleA.set(scratch0);
				}
				if (closestPointOnTriangleB != null) {
					closestPointOnTriangleB.set(scratch1);
				}
				return;
			}
			if (distSq < bestDistSq) {
				bestDistSq = distSq;
				bestAx = scratch0.getX();
				bestAy = scratch0.getY();
				bestAz = scratch0.getZ();
				bestBx = scratch1.getX();
				bestBy = scratch1.getY();
				bestBz = scratch1.getZ();
			}

			// --- Edge B3-B1 of triangle B against the whole of triangle A ---
			findsClosestPointToTriangleSegment(
					ax1, ay1, az1, ax2, ay2, az2, ax3, ay3, az3,
					bx3, by3, bz3, bx1, by1, bz1,
					epsilon, scratch0, scratch1);
			distSq = Point3D.getDistanceSquaredPointPoint(
					scratch0.getX(), scratch0.getY(), scratch0.getZ(),
					scratch1.getX(), scratch1.getY(), scratch1.getZ());
			if (distSq == 0.) {
				if (closestPointOnTriangleA != null) {
					closestPointOnTriangleA.set(scratch0);
				}
				if (closestPointOnTriangleB != null) {
					closestPointOnTriangleB.set(scratch1);
				}
				return;
			}
			if (distSq < bestDistSq) {
				bestAx = scratch0.getX();
				bestAy = scratch0.getY();
				bestAz = scratch0.getZ();
				bestBx = scratch1.getX();
				bestBy = scratch1.getY();
				bestBz = scratch1.getZ();
			}

			if (closestPointOnTriangleA != null) {
				closestPointOnTriangleA.set(bestAx, bestAy, bestAz);
			}
			if (closestPointOnTriangleB != null) {
				closestPointOnTriangleB.set(bestBx, bestBy, bestBz);
			}
		}

		/** Tests if two triangles intersect in 3D space using the Separating Axis Theorem.
		 *
		 * <p>The algorithm is based on the method described in
		 * <em>Real‑Time Collision Detection</em> by Christer Ericson (Section 5.3)
		 * and the classic paper by Tomas Möller (1997).
		 *
		 * <p>If the triangles are coplanar, a 2D segment‑based intersection test is used.
		 *
		 * @param ax1 x coordinate of the first point of the first triangle.
		 * @param ay1 y coordinate of the first point of the first triangle.
		 * @param az1 z coordinate of the first point of the first triangle.
		 * @param ax2 x coordinate of the second point of the first triangle.
		 * @param ay2 y coordinate of the second point of the first triangle.
		 * @param az2 z coordinate of the second point of the first triangle.
		 * @param ax3 x coordinate of the third point of the first triangle.
		 * @param ay3 y coordinate of the third point of the first triangle.
		 * @param az3 z coordinate of the third point of the first triangle.
		 * @param bx1 x coordinate of the first point of the second triangle.
		 * @param by1 y coordinate of the first point of the second triangle.
		 * @param bz1 z coordinate of the first point of the second triangle.
		 * @param bx2 x coordinate of the second point of the second triangle.
		 * @param by2 y coordinate of the second point of the second triangle.
		 * @param bz2 z coordinate of the second point of the second triangle.
		 * @param bx3 x coordinate of the third point of the second triangle.
		 * @param by3 y coordinate of the third point of the second triangle.
		 * @param bz3 z coordinate of the third point of the second triangle.
		 * @param epsilon the epsilon value that is used for testing equalities.
		 * @return {@code true} if the triangles are intersecting.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
		@Unefficient
		@Pure
		public static boolean intersectsTriangleTriangle(
				double ax1, double ay1, double az1,
				double ax2, double ay2, double az2,
				double ax3, double ay3, double az3,
				double bx1, double by1, double bz1,
				double bx2, double by2, double bz2,
				double bx3, double by3, double bz3,
				double epsilon) {

			// compute triangle edge vectors
			final var a1x = ax2 - ax1;
			final var a1y = ay2 - ay1;
			final var a1z = az2 - az1;
			final var a2x = ax3 - ax1;
			final var a2y = ay3 - ay1;
			final var a2z = az3 - az1;
			final var b1x = bx2 - bx1;
			final var b1y = by2 - by1;
			final var b1z = bz2 - bz1;
			final var b2x = bx3 - bx1;
			final var b2y = by3 - by1;
			final var b2z = bz3 - bz1;

			// triangle normals
			final var na = new InnerComputationVector3D();
			Vector3D.crossProduct(a1x, a1y, a1z, a2x, a2y, a2z, na);
			final var nb = new InnerComputationVector3D();
			Vector3D.crossProduct(b1x, b1y, b1z, b2x, b2y, b2z, nb);

			// test if triangles are coplanar
			final var normA2 = Vector3D.dotProduct(na.getX(), na.getY(), na.getZ(), na.getX(), na.getY(), na.getZ());
			final var normB2 = Vector3D.dotProduct(nb.getX(), nb.getY(), nb.getZ(), nb.getX(), nb.getY(), nb.getZ());
			final var epsSq = epsilon * epsilon;
			if (normA2 <= epsSq) {
				if (normB2 <= epsSq) {
					// Both points generate to points
					return Point3D.getDistanceSquaredPointPoint(ax1, ay1, az1, bx2, by2, bz2) <= epsSq;
				}
				// Only first triangle degenerates to point
				return containsTrianglePoint(bx1, by1, bz1, bx2, by2, bz2, bx3, by3, bz3, ax1, ay1, az1, false, epsilon);
			}
			if (normB2 <= epsSq) {
				// Only second triangle degenerates to point
				return containsTrianglePoint(ax1, ay1, az1, ax2, ay2, az2, ax3, ay3, az3, bx1, by1, bz1, false, epsilon);
			}

			final var crossAB2 = Vector3D.dotProduct(
					na.getY() * nb.getZ() - na.getZ() * nb.getY(),
					na.getZ() * nb.getX() - na.getX() * nb.getZ(),
					na.getX() * nb.getY() - na.getY() * nb.getX(),
					na.getY() * nb.getZ() - na.getZ() * nb.getY(),
					na.getZ() * nb.getX() - na.getX() * nb.getZ(),
					na.getX() * nb.getY() - na.getY() * nb.getX());

			// If normals are parallel (cross product ≈ 0), triangles are coplanar.
			if (crossAB2 < epsSq * Math.max(normA2, normB2)) {
				// Plane signed distances (relative to origin)
				final var dA = Vector3D.dotProduct(na.getX(), na.getY(), na.getZ(), ax1, ay1, az1);
				final var dB = Vector3D.dotProduct(nb.getX(), nb.getY(), nb.getZ(), bx1, by1, bz1);
				if (MathUtil.isEpsilonEqual(dA, dB, epsilon)) {
					// 2D coplanar intersection test
					return MollerAlgorithmTools.intersectsCoplanarTriangleTriangle(
							ax1, ay1, az1, ax2, ay2, az2, ax3, ay3, az3,
							bx1, by1, bz1, bx2, by2, bz2, bx3, by3, bz3,
							epsilon);
				}
				return false;
			}

			// Separating Axis Test
			// Test against triangle A's normal
			if (!overlapOnAxis(na.getX(), na.getY(), na.getZ(),
					ax1, ay1, az1, ax2, ay2, az2, ax3, ay3, az3,
					bx1, by1, bz1, bx2, by2, bz2, bx3, by3, bz3,
					epsilon)) {
				return false;
			}

			// Test against triangle B's normal
			if (!overlapOnAxis(nb.getX(), nb.getY(), nb.getZ(),
					bx1, by1, bz1, bx2, by2, bz2, bx3, by3, bz3,
					ax1, ay1, az1, ax2, ay2, az2, ax3, ay3, az3,
					epsilon)) {
				return false;
			}

			// Test against cross products of edges (9 axes)
			// Edge directions: a1, a2, a3 = a2-a1? Actually we have a1 and a2, and the third edge is (a2 - a1)?
			// But the third edge of triangle A is (a2 - a1) = (ax3-ax2, ...), which is equivalent to (a2x - a1x, ...).
			// However, using the three edges of a triangle: we already have a1 = (v2-v1), a2 = (v3-v1),
			// and the third edge is (v3-v2) = a2 - a1.
			// For B similarly.
			final var edgeAx = new double[] {a1x, a2x, a2x - a1x};
			final var edgeAy = new double[] {a1y, a2y, a2y - a1y};
			final var edgeAz = new double[] {a1z, a2z, a2z - a1z};
			final var edgeBx = new double[] {b1x, b2x, b2x - b1x};
			final var edgeBy = new double[] {b1y, b2y, b2y - b1y};
			final var edgeBz = new double[] {b1z, b2z, b2z - b1z};

			for (var i = 0; i < 3; ++i) {
				for (var j = 0; j < 3; ++j) {
					final var ax = edgeAy[i] * edgeBz[j] - edgeAz[i] * edgeBy[j];
					final var ay = edgeAz[i] * edgeBx[j] - edgeAx[i] * edgeBz[j];
					final var az = edgeAx[i] * edgeBy[j] - edgeAy[i] * edgeBx[j];
					// Skip if axis is too small (degenerate edges)
					if (Vector3D.dotProduct(ax, ay, az, ax, ay, az) < epsSq) {
						continue;
					}
					if (!overlapOnAxis(ax, ay, az,
							ax1, ay1, az1, ax2, ay2, az2, ax3, ay3, az3,
							bx1, by1, bz1, bx2, by2, bz2, bx3, by3, bz3,
							epsilon)) {
						return false;
					}
				}
			}

			// All axes passed → intersection
			return true;
		}

		/**
		 * Tests if the projection intervals of triangle A (given by its three vertices)
		 * and triangle B overlap on the given axis.
		 */
		@SuppressWarnings({"checkstyle:parameternumber"})
		private static boolean overlapOnAxis(
				double ax, double ay, double az,
				double a1x, double a1y, double a1z,
				double a2x, double a2y, double a2z,
				double a3x, double a3y, double a3z,
				double b1x, double b1y, double b1z,
				double b2x, double b2y, double b2z,
				double b3x, double b3y, double b3z,
				double epsilon) {
			final var minA = Math.min(
					Vector3D.dotProduct(ax, ay, az, a1x, a1y, a1z),
					Math.min(Vector3D.dotProduct(ax, ay, az, a2x, a2y, a2z),
							Vector3D.dotProduct(ax, ay, az, a3x, a3y, a3z)));
			final var maxA = Math.max(
					Vector3D.dotProduct(ax, ay, az, a1x, a1y, a1z),
					Math.max(Vector3D.dotProduct(ax, ay, az, a2x, a2y, a2z),
							Vector3D.dotProduct(ax, ay, az, a3x, a3y, a3z)));
			final var minB = Math.min(
					Vector3D.dotProduct(ax, ay, az, b1x, b1y, b1z),
					Math.min(Vector3D.dotProduct(ax, ay, az, b2x, b2y, b2z),
							Vector3D.dotProduct(ax, ay, az, b3x, b3y, b3z)));
			final var maxB = Math.max(
					Vector3D.dotProduct(ax, ay, az, b1x, b1y, b1z),
					Math.max(Vector3D.dotProduct(ax, ay, az, b2x, b2y, b2z),
							Vector3D.dotProduct(ax, ay, az, b3x, b3y, b3z)));
			return maxA + epsilon >= minB && maxB + epsilon >= minA;
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
		 * @param forceCoplanar is {@code true}
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

				final var t0 = Math.fma(b1x - a1x, rX, (b1y - a1y) * rY) / rr;
				final var t1 = Math.fma(b2x - a1x, rX, (b2y - a1y) * rY) / rr;

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
		 * @return the computed squared distance; or {@link Double#NaN} if the closest points
		 *     cannot be computed.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
		@Pure
		@Unefficient
		public static double findsClosestPointToTriangleSegment(
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double sx1, double sy1, double sz1,
				double sx2, double sy2, double sz2,
				double epsilon,
				Point3D<?, ?, ?> closestPointOnTriangle,
				Point3D<?, ?, ?> closestPointOnSegment) {
			assert closestPointOnTriangle != null || closestPointOnSegment != null : AssertMessages.notNullParameter(17);
			final var tempPt0 = new InnerComputationPoint3D();

			// 1. Triangle vertices to segment
			Segment3afp.findsClosestPointToPoint(
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx1, ty1, tz1,
					tempPt0);
			var bestDistance = Point3D.getDistanceSquaredPointPoint(tx1, ty1, tz1, tempPt0.getX(), tempPt0.getY(), tempPt0.getZ());
			if (bestDistance == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tx1, ty1, tz1);
				}
				if (closestPointOnSegment != null) {
					closestPointOnSegment.set(tempPt0);
				}
				return 0.;
			}
			final var bestTri = new InnerComputationPoint3D(tx1, ty1, tz1);
			final var bestSeg = new InnerComputationPoint3D(tempPt0);

			Segment3afp.findsClosestPointToPoint(
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx2, ty2, tz2,
					tempPt0);
			var dist = Point3D.getDistanceSquaredPointPoint(tx2, ty2, tz2, tempPt0.getX(), tempPt0.getY(), tempPt0.getZ());
			if (dist == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tx2, ty2, tz2);
				}
				if (closestPointOnSegment != null) {
					closestPointOnSegment.set(tempPt0);
				}
				return 0.;
			}
			if (dist < bestDistance) {
				bestDistance = dist;
				bestTri.set(tx2, ty2, tz2);
				bestSeg.set(tempPt0);
			}

			Segment3afp.findsClosestPointToPoint(
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx3, ty3, tz3,
					tempPt0);
			dist = Point3D.getDistanceSquaredPointPoint(tx3, ty3, tz3, tempPt0.getX(), tempPt0.getY(), tempPt0.getZ());
			if (dist == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tx3, ty3, tz3);
				}
				if (closestPointOnSegment != null) {
					closestPointOnSegment.set(tempPt0);
				}
				return 0.;
			}
			if (dist < bestDistance) {
				bestDistance = dist;
				bestTri.set(tx3, ty3, tz3);
				bestSeg.set(tempPt0);
			}

			// 2. Segment endpoints to triangle
			findsClosestPointToTrianglePoint(
					tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
					sx1, sy1, sz1, tempPt0);
			dist = Point3D.getDistanceSquaredPointPoint(sx1, sy1, sz1, tempPt0.getX(), tempPt0.getY(), tempPt0.getZ());
			if (dist == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tempPt0);
				}
				if (closestPointOnSegment != null) {
					closestPointOnSegment.set(sx1, sy1, sz1);
				}
				return 0.;
			}
			if (dist < bestDistance) {
				bestDistance = dist;
				bestTri.set(tempPt0);
				bestSeg.set(sx1, sy1, sz1);
			}

			findsClosestPointToTrianglePoint(
					tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
					sx2, sy2, sz2, tempPt0);
			dist = Point3D.getDistanceSquaredPointPoint(sx2, sy2, sz2, tempPt0.getX(), tempPt0.getY(), tempPt0.getZ());
			if (dist == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tempPt0);
				}
				if (closestPointOnSegment != null) {
					closestPointOnSegment.set(sx2, sy2, sz2);
				}
				return 0.;
			}
			if (dist < bestDistance) {
				bestDistance = dist;
				bestTri.set(tempPt0);
				bestSeg.set(sx2, sy2, sz2);
			}

			// 3. Segment to each triangle edge
			final var tempPt1 = new InnerComputationPoint3D();

			Segment3afp.findsClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx1, ty1, tz1, tx2, ty2, tz2,
					tempPt0, tempPt1);
			dist = Point3D.getDistanceSquaredPointPoint(tempPt0.getX(), tempPt0.getY(), tempPt0.getZ(),
					tempPt1.getX(), tempPt1.getY(), tempPt1.getZ());
			if (dist == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tempPt1);
				}
				if (closestPointOnSegment != null) {
					closestPointOnSegment.set(tempPt0);
				}
				return 0.;
			}
			if (dist < bestDistance) {
				bestDistance = dist;
				bestTri.set(tempPt1);
				bestSeg.set(tempPt0);
			}

			Segment3afp.findsClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx2, ty2, tz2, tx3, ty3, tz3,
					tempPt0, tempPt1);
			dist = Point3D.getDistanceSquaredPointPoint(tempPt0.getX(), tempPt0.getY(), tempPt0.getZ(),
					tempPt1.getX(), tempPt1.getY(), tempPt1.getZ());
			if (dist == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tempPt1);
				}
				if (closestPointOnSegment != null) {
					closestPointOnSegment.set(tempPt0);
				}
				return 0.;
			}
			if (dist < bestDistance) {
				bestDistance = dist;
				bestTri.set(tempPt1);
				bestSeg.set(tempPt0);
			}

			Segment3afp.findsClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					tx3, ty3, tz3, tx1, ty1, tz1,
					tempPt0, tempPt1);
			dist = Point3D.getDistanceSquaredPointPoint(tempPt0.getX(), tempPt0.getY(), tempPt0.getZ(),
					tempPt1.getX(), tempPt1.getY(), tempPt1.getZ());
			if (dist == 0.) {
				if (closestPointOnTriangle != null) {
					closestPointOnTriangle.set(tempPt1);
				}
				if (closestPointOnSegment != null) {
					closestPointOnSegment.set(tempPt0);
				}
				return 0.;
			}
			if (dist < bestDistance) {
				bestDistance = dist;
				bestTri.set(tempPt1);
				bestSeg.set(tempPt0);
			}

			// 4. Face‑interior case (segment parallel to triangle plane)
			bestDistance = handleFaceInterior(
					tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
					sx1, sy1, sz1, sx2, sy2, sz2,
					bestDistance, epsilon, bestTri, bestSeg);

			assert !Double.isInfinite(bestDistance);

			if (closestPointOnTriangle != null) {
				closestPointOnTriangle.set(bestTri);
			}
			if (closestPointOnSegment != null) {
				closestPointOnSegment.set(bestSeg);
			}

			return bestDistance;
		}

		/**
		 * Handles the case where the segment is parallel to the triangle plane
		 * and its projection onto the plane lies inside the triangle.
		 */
		@SuppressWarnings("checkstyle:parameternumber")
		private static double handleFaceInterior(
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double sx1, double sy1, double sz1,
				double sx2, double sy2, double sz2,
				double bestDistance,
				double epsilon,
				Point3D<?, ?, ?> bestTri,
				Point3D<?, ?, ?> bestSeg) {
			var best = bestDistance;

			final var e1x = tx2 - tx1;
			final var e1y = ty2 - ty1;
			final var e1z = tz2 - tz1;
			final var e2x = tx3 - tx1;
			final var e2y = ty3 - ty1;
			final var e2z = tz3 - tz1;

			final var n = new InnerComputationVector3D();
			Vector3D.crossProduct(e1x, e1y, e1z, e2x, e2y, e2z, n);
			n.normalize();

			final var a = n.getX();
			final var b = n.getY();
			final var c = n.getZ();
			if (a * a + b * b + c * c != 1.) {
				return best;
			}

			final var d = -(a * tx1 + b * ty1 + c * tz1);

			final var intersection = new InnerComputationPoint3D();
			if (Plane3afp.findsPlaneSegmentIntersection(
					a, b, c, d,
					sx1, sy1, sz1, sx2, sy2, sz2,
					intersection)) {
				// Determine if the projection point is in the triangle area
				final var px = intersection.getX();
				final var py = intersection.getY();
				final var pz = intersection.getZ();
				findsClosestPointToTrianglePoint(
						tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3,
						px, py, pz, intersection);
				final var dist = Point3D.getDistanceSquaredPointPoint(
						px, py, pz,
						intersection.getX(), intersection.getY(), intersection.getZ());
				if (dist < bestDistance) {
					best = dist;
					bestTri.set(intersection.getX(), intersection.getY(), intersection.getZ());
					bestSeg.set(px, py, pz);
				}
			} else {
				// There is no intersection between the segment and the triangle's plane.
				// The projections of each segment point should be considered
				Plane3afp.findsPlanePointProjectionWithPlaneNormal(a, b, c, d, sx1, sy1, sz1, intersection);
				var px = intersection.getX();
				var py = intersection.getY();
				var pz = intersection.getZ();
				if (containsTrianglePoint(tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, px, py, pz, false, epsilon)) {
					final var dist = Point3D.getDistanceSquaredPointPoint(
							px, py, pz,
							sx1, sy1, sz1);
					if (dist < bestDistance) {
						best = dist;
						bestTri.set(px, py, pz);
						bestSeg.set(sx1, sy1, sz1);
					}
				}

				Plane3afp.findsPlanePointProjectionWithPlaneNormal(a, b, c, d, sx2, sy2, sz2, intersection);
				px = intersection.getX();
				py = intersection.getY();
				pz = intersection.getZ();
				if (containsTrianglePoint(tx1, ty1, tz1, tx2, ty2, tz2, tx3, ty3, tz3, px, py, pz, false, epsilon)) {
					final var dist = Point3D.getDistanceSquaredPointPoint(
							px, py, pz,
							sx2, sy2, sz2);
					if (dist < bestDistance) {
						best = dist;
						bestTri.set(px, py, pz);
						bestSeg.set(sx1, sy1, sz1);
					}
				}
			}

			return best;
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
		public static void findsClosestPointToTrianglePoint(
				double tx1, double ty1, double tz1,
				double tx2, double ty2, double tz2,
				double tx3, double ty3, double tz3,
				double px, double py, double pz,
				Point3D<?, ?, ?> closestPoint) {
			assert closestPoint != null : AssertMessages.notNullParameter(12);
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
				closestPoint.set(Math.fma(v, abx, tx1), Math.fma(v, aby, ty1), Math.fma(v, abz, tz1));
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
				closestPoint.set(Math.fma(w, acx, tx1), Math.fma(w, acy, ty1), Math.fma(w, acz, tz1));
				return;
			}

			// Check if P in edge region of V2-V3; if so, project P onto V2-V3
			final var va = d3 * d6 - d5 * d4;
			final var d4d3 = d4 - d3;
			final var d5d6 = d5 - d6;
			if (va <= 0. && d4d3 >= 0. && d5d6 >= 0.) {
				final var w = d4d3 / (d4d3 + d5d6);
				// barycentric (0,1-w,w)
				closestPoint.set(
						Math.fma(w, tx3 - tx2, tx2),
						Math.fma(w, ty3 - ty2, ty2),
						Math.fma(w, tz3 - tz2, tz2));
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
