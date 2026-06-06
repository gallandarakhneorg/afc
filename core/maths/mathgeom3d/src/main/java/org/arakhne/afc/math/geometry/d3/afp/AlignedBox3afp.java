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

import org.arakhne.afc.math.GeogebraUtil;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.a.Shape3DType;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** Functional interface that represented a 3D rectangular prism or box that is aligned on the global axes.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public interface AlignedBox3afp<
		IT extends AlignedBox3afp<?, IE, P, V, Q, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends AlignedBox3afp<?, IE, P, V, Q, B>>
	extends Box3afp<IT, IE, P, V, Q, B> {

	@Override
	default Shape3DType getType() {
		return Shape3DType.ALIGNED_BOX;
	}

	/** Compute the point on the aligned box that is the closest to the given point.
	 *
	 * @param rminx the minimum x coordinate of the aligned box.
	 * @param rminy the minimum y coordinate of the aligned box.
	 * @param rminz the minimum z coordinate of the aligned box.
	 * @param rmaxx the maximum x coordinate of the aligned box.
	 * @param rmaxy the maximum y coordinate of the aligned box.
	 * @param rmaxz the maximum z coordinate of the aligned box.
	 * @param px the x coordinate of the point.
	 * @param py the y coordinate of the point.
	 * @param pz the z coordinate of the point.
	 * @param closest is set with the closest point on the aligned box.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static void findsClosestPointAlignedBoxPoint(double rminx, double rminy, double rminz, double rmaxx, double rmaxy,
			double rmaxz, double px, double py, double pz, Point3D<?, ?, ?> closest) {
		assert rmaxx >= rminx : AssertMessages.lowerEqualParameters(0, Double.valueOf(rminx), 3, Double.valueOf(rmaxx));
		assert rmaxy >= rminy : AssertMessages.lowerEqualParameters(1, Double.valueOf(rminy), 4, Double.valueOf(rmaxy));
		assert rmaxz >= rminz : AssertMessages.lowerEqualParameters(2, Double.valueOf(rminy), 5, Double.valueOf(rmaxy));
		assert closest != null : AssertMessages.notNullParameter(9);
		final double x;
		if (px < rminx) {
			x = rminx;
		} else if (px > rmaxx) {
			x = rmaxx;
		} else {
			x = px;
		}
		final double y;
		if (py < rminy) {
			y = rminy;
		} else if (py > rmaxy) {
			y = rmaxy;
		} else {
			y = py;
		}
		final double z;
		if (pz < rminz) {
			z = rminz;
		} else if (pz > rmaxz) {
			z = rmaxz;
		} else {
			z = pz;
		}
		closest.set(x, y, z);
	}

	/** Compute the point on the first aligned box that is the closest to the second aligned box.
	 *
	 * @param rminx1 the minimum x coordinate of the first aligned box.
	 * @param rminy1 the minimum y coordinate of the first aligned box.
	 * @param rminz1 the minimum z coordinate of the first aligned box.
	 * @param rmaxx1 the maximum x coordinate of the first aligned box.
	 * @param rmaxy1 the maximum y coordinate of the first aligned box.
	 * @param rmaxz1 the maximum z coordinate of the first aligned box.
	 * @param rminx2 the minimum x coordinate of the second aligned box.
	 * @param rminy2 the minimum y coordinate of the second aligned box.
	 * @param rminz2 the minimum z coordinate of the second aligned box.
	 * @param rmaxx2 the maximum x coordinate of the second aligned box.
	 * @param rmaxy2 the maximum y coordinate of the second aligned box.
	 * @param rmaxz2 the maximum z coordinate of the second aligned box.
	 * @param closest is set with the closest point on the first aligned box.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static void findsClosestPointAlignedBoxAlignedBox(
			double rminx1, double rminy1, double rminz1, double rmaxx1, double rmaxy1, double rmaxz1,
			double rminx2, double rminy2, double rminz2, double rmaxx2, double rmaxy2, double rmaxz2,
			Point3D<?, ?, ?> closest) {
		assert rmaxx1 >= rminx1 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rminx1), 3, Double.valueOf(rmaxx1));
		assert rmaxy1 >= rminy1 : AssertMessages.lowerEqualParameters(1, Double.valueOf(rminy1), 4, Double.valueOf(rmaxy1));
		assert rmaxz1 >= rminz1 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rminz1), 5, Double.valueOf(rmaxz1));
		assert rmaxx2 >= rminx2 : AssertMessages.lowerEqualParameters(6, Double.valueOf(rminx2), 9, Double.valueOf(rmaxx2));
		assert rmaxy2 >= rminy2 : AssertMessages.lowerEqualParameters(7, Double.valueOf(rminy2), 10, Double.valueOf(rmaxy2));
		assert rmaxz2 >= rminz2 : AssertMessages.lowerEqualParameters(8, Double.valueOf(rminz2), 11, Double.valueOf(rmaxz2));
		assert closest != null : AssertMessages.notNullParameter(9);
		final double px;
		final var cx = (rminx2 + rmaxx2) / 2.;
		if (cx <= rminx1) {
			px = rminx1;
		} else if (cx >= rmaxx1) {
			px = rmaxx1;
		} else {
			px = cx;
		}
		final double py;
		final var cy = (rminy2 + rmaxy2) / 2.;
		if (cy <= rminy1) {
			py = rminy1;
		} else if (cy >= rmaxy1) {
			py = rmaxy1;
		} else {
			py = cy;
		}
		final double pz;
		final var cz = (rminz2 + rmaxz2) / 2.;
		if (cz <= rminz1) {
			pz = rminz1;
		} else if (cz >= rmaxz1) {
			pz = rmaxz1;
		} else {
			pz = cz;
		}
		closest.set(px, py, pz);
	}

	/** Compute the point on the first aligned box that is the farthest to the second aligned box.
	 *
	 * @param rminx1 the minimum x coordinate of the first aligned box.
	 * @param rminy1 the minimum y coordinate of the first aligned box.
	 * @param rminz1 the minimum z coordinate of the first aligned box.
	 * @param rmaxx1 the maximum x coordinate of the first aligned box.
	 * @param rmaxy1 the maximum y coordinate of the first aligned box.
	 * @param rmaxz1 the maximum z coordinate of the first aligned box.
	 * @param rminx2 the minimum x coordinate of the second aligned box.
	 * @param rminy2 the minimum y coordinate of the second aligned box.
	 * @param rminz2 the minimum z coordinate of the second aligned box.
	 * @param rmaxx2 the maximum x coordinate of the second aligned box.
	 * @param rmaxy2 the maximum y coordinate of the second aligned box.
	 * @param rmaxz2 the maximum z coordinate of the second aligned box.
	 * @param farthest is set with the farthest point on the first aligned box.
	 * @since 18.0
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static void findsFarthestPointAlignedBoxAlignedBox(
			double rminx1, double rminy1, double rminz1, double rmaxx1, double rmaxy1, double rmaxz1,
			double rminx2, double rminy2, double rminz2, double rmaxx2, double rmaxy2, double rmaxz2,
			Point3D<?, ?, ?> farthest) {
		assert rmaxx1 >= rminx1 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rminx1), 3, Double.valueOf(rmaxx1));
		assert rmaxy1 >= rminy1 : AssertMessages.lowerEqualParameters(1, Double.valueOf(rminy1), 4, Double.valueOf(rmaxy1));
		assert rmaxz1 >= rminz1 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rminz1), 5, Double.valueOf(rmaxz1));
		assert rmaxx2 >= rminx2 : AssertMessages.lowerEqualParameters(6, Double.valueOf(rminx2), 9, Double.valueOf(rmaxx2));
		assert rmaxy2 >= rminy2 : AssertMessages.lowerEqualParameters(7, Double.valueOf(rminy2), 10, Double.valueOf(rmaxy2));
		assert rmaxz2 >= rminz2 : AssertMessages.lowerEqualParameters(8, Double.valueOf(rminz2), 11, Double.valueOf(rmaxz2));
		assert farthest != null : AssertMessages.notNullParameter(9);
		final double px;
		final var bx = (rminx1 + rmaxx1) / 2.;
		final var cx = (rminx2 + rmaxx2) / 2.;
		if (cx <= bx) {
			px = rmaxx1;
		} else {
			px = rminx1;
		}
		final double py;
		final var by = (rminy1 + rmaxy1) / 2.;
		final var cy = (rminy2 + rmaxy2) / 2.;
		if (cy <= by) {
			py = rmaxy1;
		} else {
			py = rminy1;
		}
		final double pz;
		final var bz = (rminz1 + rmaxz1) / 2.;
		final var cz = (rminz2 + rmaxz2) / 2.;
		if (cz <= bz) {
			pz = rmaxz1;
		} else {
			pz = rminz1;
		}
		farthest.set(px, py, pz);
	}

	/** Compute the point on the aligned box that is the closest to the sphere.
	 * This function replies the closest point to the sphere center, i.e.,
	 * it is equivalent to {@link #findsClosestPointAlignedBoxPoint(double, double, double,
	 * double, double, double, double, double, double, Point3D)}
	 *
	 * @param rminx the minimum x coordinate of the aligned box.
	 * @param rminy the minimum y coordinate of the aligned box.
	 * @param rminz the minimum z coordinate of the aligned box.
	 * @param rmaxx the maximum x coordinate of the aligned box.
	 * @param rmaxy the maximum y coordinate of the aligned box.
	 * @param rmaxz the maximum z coordinate of the aligned box.
	 * @param sx the x coordinate of the sphere center.
	 * @param sy the y coordinate of the sphere center.
	 * @param sz the z coordinate of the sphere center.
	 * @param radius is the radius of the sphere.
	 * @param closest is set with the closest point on the aligned box.
	 * @see #findsClosestPointAlignedBoxSphere(double, double, double, double, double, double, double, double, double, double, Point3D)
	 */
	@Inline("findsClosestPointAlignedBoxPoint($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)")
	@SuppressWarnings("checkstyle:parameternumber")
	static void findsClosestPointAlignedBoxSphere(
			double rminx, double rminy, double rminz, double rmaxx, double rmaxy, double rmaxz,
			double sx, double sy, double sz, double radius,
			Point3D<?, ?, ?> closest) {
		findsClosestPointAlignedBoxPoint(rminx, rminy, rminz, rmaxx, rmaxy, rmaxz, sx, sy, sz, closest);
	}

	/** Compute the point on the aligned box that is the farthest to the sphere.
	 * This function replies the farthest point to the sphere center, i.e.,
	 * it is equivalent to {@link #findsFarthestPointAlignedBoxPoint(double, double,
	 * double, double, double, double, double, double, double, Point3D)}
	 *
	 * @param rminx the minimum x coordinate of the aligned box.
	 * @param rminy the minimum y coordinate of the aligned box.
	 * @param rminz the minimum z coordinate of the aligned box.
	 * @param rmaxx the maximum x coordinate of the aligned box.
	 * @param rmaxy the maximum y coordinate of the aligned box.
	 * @param rmaxz the maximum z coordinate of the aligned box.
	 * @param sx the x coordinate of the sphere center.
	 * @param sy the y coordinate of the sphere center.
	 * @param sz the z coordinate of the sphere center.
	 * @param radius is the radius of the sphere.
	 * @param farthest is set with the farthest point on the aligned box.
	 * @see #findsFarthestPointAlignedBoxPoint(double, double, double, double, double, double, double, double, double, Point3D)
	 */
	@Inline("findsFarthestPointAlignedBoxPoint($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)")
	@SuppressWarnings("checkstyle:parameternumber")
	static void findsFarthestPointAlignedBoxSphere(
			double rminx, double rminy, double rminz, double rmaxx, double rmaxy, double rmaxz,
			double sx, double sy, double sz, double radius,
			Point3D<?, ?, ?> farthest) {
		findsFarthestPointAlignedBoxPoint(rminx, rminy, rminz, rmaxx, rmaxy, rmaxz, sx, sy, sz, farthest);
	}

	/** Compute the point on the aligned box that is the closest to the segment.
	 *
	 * @param rminx the minimum x coordinate of the aligned box.
	 * @param rminy the minimum y coordinate of the aligned box.
	 * @param rminz the minimum z coordinate of the aligned box.
	 * @param rmaxx the maximum x coordinate of the aligned box.
	 * @param rmaxy the maximum y coordinate of the aligned box.
	 * @param rmaxz the maximum z coordinate of the aligned box.
	 * @param sx1 the x coordinate of the first point of the segment.
	 * @param sy1 the y coordinate of the first point of the segment.
	 * @param sz1 the z coordinate of the first point of the segment.
	 * @param sx2 the x coordinate of the second point of the segment.
	 * @param sy2 the y coordinate of the second point of the segment.
	 * @param sz2 the z coordinate of the second point of the segment.
	 * @param closest is set with the closest point on the aligned box.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static void findsClosestPointAlignedBoxSegment(
			double rminx, double rminy, double rminz, double rmaxx, double rmaxy, double rmaxz,
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			Point3D<?, ?, ?> closest) {
		assert rmaxx >= rminx : AssertMessages.lowerEqualParameters(0, Double.valueOf(rminx), 3, Double.valueOf(rmaxx));
		assert rmaxy >= rminy : AssertMessages.lowerEqualParameters(1, Double.valueOf(rminy), 4, Double.valueOf(rmaxy));
		assert rmaxz >= rminz : AssertMessages.lowerEqualParameters(2, Double.valueOf(rminz), 5, Double.valueOf(rmaxz));
		assert closest != null : AssertMessages.notNullParameter(12);
		final var code1 = MathUtil.getCohenSutherlandCode3D(sx1, sy1, sz1, rminx, rminy, rminz, rmaxx, rmaxy, rmaxz);
		final var code2 = MathUtil.getCohenSutherlandCode3D(sx2, sy2, sz2, rminx, rminy, rminz, rmaxx, rmaxy, rmaxz);
		final var tmp1 = new InnerComputationPoint3afp();
		final var zone = Segment3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
				rminx, rminy, rminz, rmaxx, rmaxy, rmaxz,
				sx1, sy1, sz1, sx2, sy2, sz2,
				code1, code2,
				tmp1, null);
		final double closex;
		final double closey;
		final double closez;
		if ((zone & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
			closex = rminx;
			if (sx1 >= sx2) {
				closey = MathUtil.clamp(sy1, rminy, rmaxy);
				closez = MathUtil.clamp(sz1, rminz, rmaxz);
			} else {
				closey = MathUtil.clamp(sy2, rminy, rmaxy);
				closez = MathUtil.clamp(sz2, rminz, rmaxz);
			}
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
			closex = rmaxx;
			if (sx1 <= sx2) {
				closey = MathUtil.clamp(sy1, rminy, rmaxy);
				closez = MathUtil.clamp(sz1, rminz, rmaxz);
			} else {
				closey = MathUtil.clamp(sy2, rminy, rmaxy);
				closez = MathUtil.clamp(sz2, rminz, rmaxz);
			}
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
			closey = rminy;
			if (sy1 >= sy2) {
				closex = MathUtil.clamp(sx1, rminx, rmaxx);
				closez = MathUtil.clamp(sz1, rminz, rmaxz);
			} else {
				closex = MathUtil.clamp(sx2, rminx, rmaxx);
				closez = MathUtil.clamp(sz2, rminz, rmaxz);
			}
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
			closey = rmaxy;
			if (sy1 <= sy2) {
				closex = MathUtil.clamp(sx1, rminx, rmaxx);
				closez = MathUtil.clamp(sz1, rminz, rmaxz);
			} else {
				closex = MathUtil.clamp(sx2, rminx, rmaxx);
				closez = MathUtil.clamp(sz2, rminz, rmaxz);
			}
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_FRONT) != 0) {
			closez = rminz;
			if (sz1 >= sz2) {
				closex = MathUtil.clamp(sx1, rminx, rmaxx);
				closey = MathUtil.clamp(sy1, rminy, rmaxy);
			} else {
				closex = MathUtil.clamp(sx2, rminx, rmaxx);
				closey = MathUtil.clamp(sy2, rminy, rmaxy);
			}
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_BACK) != 0) {
			closez = rmaxz;
			if (sy1 <= sy2) {
				closex = MathUtil.clamp(sx1, rminx, rmaxx);
				closey = MathUtil.clamp(sy1, rminy, rmaxy);
			} else {
				closex = MathUtil.clamp(sx2, rminx, rmaxx);
				closey = MathUtil.clamp(sy2, rminy, rmaxy);
			}
		} else {
			closex = tmp1.getX();
			closey = tmp1.getY();
			closez = tmp1.getZ();
		}
		closest.set(closex, closey, closez);
	}

	/** Compute the point on the aligned box that is the farthest to the given point.
	 *
	 * @param rminx the minimum x coordinate of the aligned box.
	 * @param rminy the minimum y coordinate of the aligned box.
	 * @param rminz the minimum z coordinate of the aligned box.
	 * @param rmaxx the maximum x coordinate of the aligned box.
	 * @param rmaxy the maximum y coordinate of the aligned box.
	 * @param rmaxz the maximum z coordinate of the aligned box.
	 * @param px the x coordinate of the point.
	 * @param py the y coordinate of the point.
	 * @param pz the z coordinate of the point.
	 * @param farthest is set with the farthest point on the aligned box.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static void findsFarthestPointAlignedBoxPoint(double rminx, double rminy, double rminz, double rmaxx, double rmaxy,
			double rmaxz, double px, double py, double pz, Point3D<?, ?, ?> farthest) {
		assert rmaxx >= rminx : AssertMessages.lowerEqualParameters(0, Double.valueOf(rminx), 3, Double.valueOf(rmaxx));
		assert rmaxy >= rminy : AssertMessages.lowerEqualParameters(1, Double.valueOf(rminy), 4, Double.valueOf(rmaxy));
		assert rmaxz >= rminz : AssertMessages.lowerEqualParameters(2, Double.valueOf(rminy), 5, Double.valueOf(rmaxy));
		assert farthest != null : AssertMessages.notNullParameter(9);
		final double x;
		if (px <= (rminx + rmaxx) / 2.) {
			x = rmaxx;
		} else {
			x = rminx;
		}
		final double y;
		if (py <= (rminy + rmaxy) / 2.) {
			y = rmaxy;
		} else {
			y = rminy;
		}
		final double z;
		if (pz <= (rminz + rmaxz) / 2.) {
			z = rmaxz;
		} else {
			z = rminz;
		}
		farthest.set(x, y, z);
	}

	/** Replies if two aligned boxs are intersecting.
	 *
	 * @param x1 is the first corner of the first aligned box.
	 * @param y1 is the first corner of the first aligned box.
	 * @param z1 is the first corner of the first aligned box.
	 * @param x2 is the second corner of the first aligned box.
	 * @param y2 is the second corner of the first aligned box.
	 * @param z2 is the second corner of the first aligned box.
	 * @param x3 is the first corner of the second aligned box.
	 * @param y3 is the first corner of the second aligned box.
	 * @param z3 is the first corner of the second aligned box.
	 * @param x4 is the second corner of the second aligned box.
	 * @param y4 is the second corner of the second aligned box.
	 * @param z4 is the second corner of the second aligned box.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 *     {@code false}
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean intersectsAlignedBoxAlignedBox(double x1, double y1, double z1, double x2, double y2, double z2, double x3,
			double y3, double z3, double x4, double y4, double z4) {
		assert x1 <= x2 : AssertMessages.lowerEqualParameters(0, Double.valueOf(x1), 3, Double.valueOf(x2));
		assert y1 <= y2 : AssertMessages.lowerEqualParameters(1, Double.valueOf(y1), 4, Double.valueOf(y2));
		assert z1 <= z2 : AssertMessages.lowerEqualParameters(2, Double.valueOf(z1), 5, Double.valueOf(z2));
		assert x3 <= x4 : AssertMessages.lowerEqualParameters(6, Double.valueOf(x3), 9, Double.valueOf(x4));
		assert y3 <= y4 : AssertMessages.lowerEqualParameters(7, Double.valueOf(y3), 10, Double.valueOf(y4));
		assert z3 <= z4 : AssertMessages.lowerEqualParameters(8, Double.valueOf(z3), 11, Double.valueOf(z4));
		return x2 > x3 && x1 < x4 && y2 > y3 && y1 < y4 && z2 > z3 && z1 < z4;
	}

	/** Replies if an aligned box and a line are intersecting.
	 *
	 * @param rx1 is the first corner of the aligned box.
	 * @param ry1 is the first corner of the aligned box.
	 * @param rz1 is the first corner of the aligned box.
	 * @param rx2 is the second corner of the aligned box.
	 * @param ry2 is the second corner of the aligned box.
	 * @param rz2 is the second corner of the aligned box.
	 * @param sx1 is the first point of the line.
	 * @param sy1 is the first point of the line.
	 * @param sz1 is the first point of the line.
	 * @param sx2 is the second point of the line.
	 * @param sy2 is the second point of the line.
	 * @param sz2 is the second point of the line.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 *     {@code false}
	 * @see "./doc-files/IntersectionLineBox.pdf"
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:localvariablename", "checkstyle:localfinalvariablename"})
	static boolean intersectsAlignedBoxLine(
			double rx1, double ry1, double rz1, double rx2, double ry2, double rz2,
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2) {
		assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rx1), 3, Double.valueOf(rx2));
		assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, Double.valueOf(ry1), 4, Double.valueOf(ry2));
		assert rz1 <= rz2 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rz1), 5, Double.valueOf(rz2));

		// Get the centered form of the aligned box
		// C: center point of the aligned box
		// e: extent of the aligned box
		final var Cx = (rx2 + rx1) * .5;
		final var Cy = (ry2 + ry1) * .5;
		final var Cz = (rz2 + rz1) * .5;
		final var ex = (rx2 - rx1) * .5;
		final var ey = (ry2 - ry1) * .5;
		final var ez = (rz2 - rz1) * .5;

		// Transform the line to the aligned-box coordinate system
		// P: the reference point of the line
		// D: line direction vector
		final var Px = sx1 - Cx;
		final var Py = sy1 - Cy;
		final var Pz = sz1 - Cz;
		final var Dx = sx2 - sx1;
		final var Dy = sy2 - sy1;
		final var Dz = sz2 - sz1;

		// Cross product D x P
		final var DxPx = Dy * Pz - Dz * Py;
        final var DxPy = Dz * Px - Dx * Pz;
        final var DxPz = Dx * Py - Dy * Px;

        final var Dxabs = Math.abs(Dx);
        final var Dyabs = Math.abs(Dy);
        final var Dzabs = Math.abs(Dz);

        return Math.abs(DxPx) <= ey * Dzabs + ez * Dyabs
        		&& Math.abs(DxPy) <= ex * Dzabs + ez * Dxabs
        		&& Math.abs(DxPz) <= ex * Dyabs + ey * Dxabs;
	}

	/** Replies if the aligned box is intersecting the segment.
	 *
	 * @param rx1 is the first corner of the aligned box.
	 * @param ry1 is the first corner of the aligned box.
	 * @param rz1 is the first corner of the aligned box.
	 * @param rx2 is the second corner of the aligned box.
	 * @param ry2 is the second corner of the aligned box.
	 * @param rz2 is the second corner of the aligned box.
	 * @param sx1 is the first point of the segment.
	 * @param sy1 is the first point of the segment.
	 * @param sz1 is the first point of the segment.
	 * @param sx2 is the second point of the segment.
	 * @param sy2 is the second point of the segment.
	 * @param sz2 is the second point of the segment.
	 * @return {@code true} if the two shapes are intersecting; otherwise
	 *     {@code false}
	 * @see "./doc-files/IntersectionLineBox.pdf"
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:localvariablename", "checkstyle:localfinalvariablename"})
	static boolean intersectsAlignedBoxSegment(
			double rx1, double ry1, double rz1, double rx2, double ry2, double rz2,
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2) {
		assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rx1), 3, Double.valueOf(rx2));
		assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, Double.valueOf(ry1), 4, Double.valueOf(ry2));
		assert rz1 <= rz2 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rz1), 5, Double.valueOf(rz2));

		// Get the centered form of the aligned box
		// C: center point of the aligned box
		// boxE: extent of the aligned box
		final var Cx = (rx2 + rx1) * .5;
		final var Cy = (ry2 + ry1) * .5;
		final var Cz = (rz2 + rz1) * .5;
		final var boxEx = (rx2 - rx1) * .5;
		final var boxEy = (ry2 - ry1) * .5;
		final var boxEz = (rz2 - rz1) * .5;

		// Transform the segment to the aligned-box coordinate system and
		// convert it to the center-direction-extent form of the segment
		// P: center point of the center in the aligned-box coordinate system
		// D: unit vector of the direction of the segment
		// segE: extent of the segment from its center
		final var Px = (sx1 + sx2) * .5 - Cx;
		final var Py = (sy1 + sy2) * .5 - Cy;
		final var Pz = (sz1 + sz2) * .5 - Cz;
		final var Dx = sx2 - sx1;
		final var Dy = sy2 - sy1;
		final var Dz = sz2 - sz1;
		final var lengthD = Math.sqrt(Dx * Dx + Dy * Dy + Dz * Dz);
		final var uDx = Dx / lengthD;
		final var uDy = Dy / lengthD;
		final var uDz = Dz / lengthD;
		final var segE = lengthD * .5;

        final var uDxabs = Math.abs(uDx);
        final var uDyabs = Math.abs(uDy);
        final var uDzabs = Math.abs(uDz);

        final var Pxabs = Math.abs(Px);
        final var Pyabs = Math.abs(Py);
        final var Pzabs = Math.abs(Pz);

        final var projx = boxEx + segE * uDxabs;
        final var projy = boxEy + segE * uDyabs;
        final var projz = boxEz + segE * uDzabs;

        // Test if the segment is tootally outside the box and don't touch it
        if (Pxabs > projx || Pyabs > projy || Pzabs > projz) {
			return false;
		}
        // Special case: the intersection point between the segment and the box lies
        // on the bounds of the box and one end of the segment
        if (Pxabs == projx && Pyabs == projy && Pzabs == projz) {
			return false;
		}

		// Do a regular line-box intersection
		// Cross product D x P
		final var DxPx = Dy * Pz - Dz * Py;
        final var DxPy = Dz * Px - Dx * Pz;
        final var DxPz = Dx * Py - Dy * Px;

        final var Dxabs = Math.abs(Dx);
        final var Dyabs = Math.abs(Dy);
        final var Dzabs = Math.abs(Dz);

        return Math.abs(DxPx) <= boxEy * Dzabs + boxEz * Dyabs
        		&& Math.abs(DxPy) <= boxEx * Dzabs + boxEz * Dxabs
        		&& Math.abs(DxPz) <= boxEx * Dyabs + boxEy * Dxabs;
	}

	/** Replies if a aligned box is inside in the aligned box.
	 *
	 * @param enclosingX1 is the lowest corner of the enclosing-candidate aligned box.
	 * @param enclosingY1 is the lowest corner of the enclosing-candidate aligned box.
	 * @param enclosingZ1 is the lowest corner of the enclosing-candidate aligned box.
	 * @param enclosingX2 is the highest corner of the enclosing-candidate aligned box.
	 * @param enclosingY2 is the highest corner of the enclosing-candidate aligned box.
	 * @param enclosingZ2 is the highest corner of the enclosing-candidate aligned box.
	 * @param innerX1 is the lowest corner of the inner-candidate aligned box.
	 * @param innerY1 is the lowest corner of the inner-candidate aligned box.
	 * @param innerZ1 is the lowest corner of the inner-candidate aligned box.
	 * @param innerX2 is the highest corner of the inner-candidate aligned box.
	 * @param innerY2 is the highest corner of the inner-candidate aligned box.
	 * @param innerZ2 is the highest corner of the inner-candidate aligned box.
	 * @return {@code true} if the given aligned box is inside the ellipse;
	 *     otherwise {@code false}.
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean containsAlignedBoxAlignedBox(double enclosingX1, double enclosingY1, double enclosingZ1,
			double enclosingX2, double enclosingY2, double enclosingZ2,
			double innerX1, double innerY1, double innerZ1,
			double innerX2, double innerY2, double innerZ2) {
		assert enclosingX1 <= enclosingX2
				: AssertMessages.lowerEqualParameters(0, Double.valueOf(enclosingX1), 3, Double.valueOf(enclosingX2));
		assert enclosingY1 <= enclosingY2
				: AssertMessages.lowerEqualParameters(1, Double.valueOf(enclosingY1), 4, Double.valueOf(enclosingY2));
		assert enclosingZ1 <= enclosingZ2
				: AssertMessages.lowerEqualParameters(2, Double.valueOf(enclosingZ1), 5, Double.valueOf(enclosingZ2));
		assert innerX1 <= innerX2 : AssertMessages.lowerEqualParameters(0, Double.valueOf(innerX1), 3, Double.valueOf(innerX2));
		assert innerY1 <= innerY2 : AssertMessages.lowerEqualParameters(1, Double.valueOf(innerY1), 4, Double.valueOf(innerY2));
		assert innerZ1 <= innerZ2 : AssertMessages.lowerEqualParameters(2, Double.valueOf(innerZ1), 5, Double.valueOf(innerZ2));
		return innerX1 >= enclosingX1
				&& innerY1 >= enclosingY1
				&& innerZ1 >= enclosingZ1
				&& innerX2 <= enclosingX2
				&& innerY2 <= enclosingY2
				&& innerZ2 <= enclosingZ2;
	}

	/** Replies if a point is inside in the aligned box.
	 *
	 * @param rx1 is the lowest corner of the aligned box.
	 * @param ry1 is the lowest corner of the aligned box.
	 * @param rz1 is the lowest corner of the aligned box.
	 * @param rx2 is the highest corner of the aligned box.
	 * @param ry2 is the highest corner of the aligned box.
	 * @param rz2 is the highest corner of the aligned box.
	 * @param px is the point.
	 * @param py is the point.
	 * @param pz is the point.
	 * @return {@code true} if the given point is inside the aligned box;
	 *     otherwise {@code false}.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:localvariablename", "checkstyle:localfinalvariablename"})
	static boolean containsAlignedBoxPoint(
			double rx1, double ry1, double rz1, double rx2, double ry2, double rz2,
			double px, double py, double pz) {
		assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rx1), 3, Double.valueOf(rx2));
		assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, Double.valueOf(ry1), 4, Double.valueOf(ry2));
		assert rz1 <= rz2 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rz1), 5, Double.valueOf(rz2));
		return px >= rx1 && px <= rx2 && py >= ry1 && py <= ry2 && pz >= rz1 && pz <= rz2;
	}

	/** Calculates and replies the square distance between the aligned box and the point.
	 *
	 * @param rx1 is the lowest corner of the aligned box.
	 * @param ry1 is the lowest corner of the aligned box.
	 * @param rz1 is the lowest corner of the aligned box.
	 * @param rx2 is the highest corner of the aligned box.
	 * @param ry2 is the highest corner of the aligned box.
	 * @param rz2 is the highest corner of the aligned box.
	 * @param px is the point.
	 * @param py is the point.
	 * @param pz is the point.
	 * @return the square distance between aligned box and the point.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:localvariablename", "checkstyle:localfinalvariablename"})
	static double calculatesAlignedBoxPointDistanceSquared(
			double rx1, double ry1, double rz1, double rx2, double ry2, double rz2,
			double px, double py, double pz) {
		assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rx1), 3, Double.valueOf(rx2));
		assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, Double.valueOf(ry1), 4, Double.valueOf(ry2));
		assert rz1 <= rz2 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rz1), 5, Double.valueOf(rz2));
		final double dx;
		if (px < rx1) {
			dx = rx1 - px;
		} else if (px > rx2) {
			dx = px - rx2;
		} else {
			dx = 0.;
		}
		final double dy;
		if (py < ry1) {
			dy = ry1 - py;
		} else if (py > ry2) {
			dy = py - ry2;
		} else {
			dy = 0.;
		}
		final double dz;
		if (pz < rz1) {
			dz = rz1 - pz;
		} else if (pz > rz2) {
			dz = pz - rz2;
		} else {
			dz = 0.;
		}
		return dx * dx + dy * dy + dz * dz;
	}

	/** Calculates and replies the L1 distance between the aligned box and the point.
	 * The L-1 distance is equal to abs(x1-x2) + abs(y1-y2) + abs(z1-z2).
	 *
	 * @param rx1 is the lowest corner of the aligned box.
	 * @param ry1 is the lowest corner of the aligned box.
	 * @param rz1 is the lowest corner of the aligned box.
	 * @param rx2 is the highest corner of the aligned box.
	 * @param ry2 is the highest corner of the aligned box.
	 * @param rz2 is the highest corner of the aligned box.
	 * @param px is the point.
	 * @param py is the point.
	 * @param pz is the point.
	 * @return the L1 distance between aligned box and the point.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:localvariablename", "checkstyle:localfinalvariablename"})
	static double calculatesAlignedBoxPointDistanceL1(
			double rx1, double ry1, double rz1, double rx2, double ry2, double rz2,
			double px, double py, double pz) {
		assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rx1), 3, Double.valueOf(rx2));
		assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, Double.valueOf(ry1), 4, Double.valueOf(ry2));
		assert rz1 <= rz2 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rz1), 5, Double.valueOf(rz2));
		final double dx;
		if (px < rx1) {
			dx = rx1 - px;
		} else if (px > rx2) {
			dx = px - rx2;
		} else {
			dx = 0.;
		}
		final double dy;
		if (py < ry1) {
			dy = ry1 - py;
		} else if (py > ry2) {
			dy = py - ry2;
		} else {
			dy = 0.;
		}
		final double dz;
		if (pz < rz1) {
			dz = rz1 - pz;
		} else if (pz > rz2) {
			dz = pz - rz2;
		} else {
			dz = 0.;
		}
		return dx + dy + dz;
	}

	/** Calculates and replies the L-infinity distance between the aligned box and the point.
	 * The L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2)].
	 *
	 * @param rx1 is the lowest corner of the aligned box.
	 * @param ry1 is the lowest corner of the aligned box.
	 * @param rz1 is the lowest corner of the aligned box.
	 * @param rx2 is the highest corner of the aligned box.
	 * @param ry2 is the highest corner of the aligned box.
	 * @param rz2 is the highest corner of the aligned box.
	 * @param px is the point.
	 * @param py is the point.
	 * @param pz is the point.
	 * @return the L-infinity distance between aligned box and the point.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:localvariablename", "checkstyle:localfinalvariablename"})
	static double calculatesAlignedBoxPointDistanceLinf(
			double rx1, double ry1, double rz1, double rx2, double ry2, double rz2,
			double px, double py, double pz) {
		assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rx1), 3, Double.valueOf(rx2));
		assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, Double.valueOf(ry1), 4, Double.valueOf(ry2));
		assert rz1 <= rz2 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rz1), 5, Double.valueOf(rz2));
		final double dx;
		if (px < rx1) {
			dx = rx1 - px;
		} else if (px > rx2) {
			dx = px - rx2;
		} else {
			dx = 0.;
		}
		final double dy;
		if (py < ry1) {
			dy = ry1 - py;
		} else if (py > ry2) {
			dy = py - ry2;
		} else {
			dy = 0.;
		}
		final double dz;
		if (pz < rz1) {
			dz = rz1 - pz;
		} else if (pz > rz2) {
			dz = pz - rz2;
		} else {
			dz = 0f;
		}
		return MathUtil.max(dx, dy, dz);
	}

	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		if (shape == this) {
			return true;
		}
		return getMinX() == shape.getMinX()
				&& getMinY() == shape.getMinY()
				&& getMinZ() == shape.getMinZ()
				&& getMaxX() == shape.getMaxX()
				&& getMaxY() == shape.getMaxY()
				&& getMaxZ() == shape.getMaxZ();
	}

	@Override
	default void set(IT shape) {
		assert shape != null : AssertMessages.notNullParameter();
		setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMinZ(), shape.getMaxX(), shape.getMaxY(), shape.getMaxZ());
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return calculatesAlignedBoxPointDistanceSquared(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				pt.getX(), pt.getY(), pt.getZ());
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?, ?> pt) {
		assert pt != null :  AssertMessages.notNullParameter();
		return calculatesAlignedBoxPointDistanceL1(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				pt.getX(), pt.getY(), pt.getZ());
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?, ?> pt) {
		assert pt != null :  AssertMessages.notNullParameter();
		return calculatesAlignedBoxPointDistanceLinf(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				pt.getX(), pt.getY(), pt.getZ());
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
		return containsAlignedBoxPoint(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				x, y, z);
	}

	@Pure
	@Override
	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox) {
		assert AlignedBox != null :  AssertMessages.notNullParameter();
		return containsAlignedBoxAlignedBox(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
				AlignedBox.getMinX(), AlignedBox.getMinY(), AlignedBox.getMinZ(), AlignedBox.getMaxX(),
				AlignedBox.getMaxY(), AlignedBox.getMaxZ());
	}

	/** Add the given coordinate in the aligned box.
	 *
	 * <p>The corners of the aligned boxs are moved to
	 * enclose the given coordinate.
	 *
	 * @param pt the point
	 */
	default void add(Point3D<?, ?, ?> pt) {
		assert pt != null :  AssertMessages.notNullParameter();
		add(pt.getX(), pt.getY(), pt.getZ());
	}

	/** Add the given coordinate in the aligned box.
	 *
	 * <p>The corners of the aligned boxs are moved to
	 * enclose the given coordinates.
	 *
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 */
	default void add(double x, double y, double z) {
		if (x < getMinX()) {
			setMinX(x);
		} else if (x > getMaxX()) {
			setMaxX(x);
		}
		if (y < getMinY()) {
			setMinY(y);
		} else if (y > getMaxY()) {
			setMaxY(y);
		}
		if (z < getMinZ()) {
			setMinZ(z);
		} else if (z > getMaxZ()) {
			setMaxZ(z);
		}
	}

	/** Compute and replies the union of this aligned box and the given aligned box.
	 * This function does not change this aligned box.
	 *
	 * <p>It is equivalent to (where {@code ur} is the union):
	 * <pre><code>
	 * aligned box2f ur = new aligned box2f(this);
	 * ur.setUnion(r);
	 * </code></pre>
	 *
	 * @param box the box
	 * @return the union of this aligned box and the given aligned box.
	 * @see #setUnion(Box3afp)
	 */
	@Pure
	default B createUnion(Box3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null :  AssertMessages.notNullParameter();
		final B rr = getGeomFactory().newBox();
		rr.setFromCorners(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
		rr.setUnion(box);
		return rr;
	}

	/** Compute and replies the intersection of this aligned box and the given aligned box.
	 * This function does not change this aligned box.
	 *
	 * <p>It is equivalent to (where {@code ir} is the intersection):
	 * <pre><code>
	 * aligned box2f ir = new aligned box2f(this);
	 * ir.setIntersection(r);
	 * </code></pre>
	 *
	 * @param box the box
	 * @return the union of this aligned box and the given aligned box.
	 * @see #setIntersection(Box3afp)
	 */
	@Pure
	default B createIntersection(Box3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null :  AssertMessages.notNullParameter();
		final B rr = getGeomFactory().newBox();
		final var x1 = Math.max(getMinX(), box.getMinX());
		final var y1 = Math.max(getMinY(), box.getMinY());
		final var z1 = Math.max(getMinZ(), box.getMinZ());
		final var x2 = Math.min(getMaxX(), box.getMaxX());
		final var y2 = Math.min(getMaxY(), box.getMaxY());
		final var z2 = Math.min(getMaxZ(), box.getMaxZ());
		if (x1 <= x2 && y1 <= y2 && z1 <= z2) {
			rr.setFromCorners(x1, y1, z1, x2, y2, z2);
		} else {
			rr.clear();
		}
		return rr;
	}

	/** Compute the union of this aligned box and the given aligned box and
	 * change this aligned box with the result of the union.
	 *
	 * @param box the box
	 * @see #createUnion(Box3afp)
	 */
	default void setUnion(Box3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null :  AssertMessages.notNullParameter();
		setFromCorners(
				Math.min(getMinX(), box.getMinX()),
				Math.min(getMinY(), box.getMinY()),
				Math.min(getMinZ(), box.getMinZ()),
				Math.max(getMaxX(), box.getMaxX()),
				Math.max(getMaxY(), box.getMaxY()),
				Math.max(getMaxZ(), box.getMaxZ()));
	}

	/** Compute the intersection of this aligned box and the given aligned box.
	 * This function changes this aligned box.
	 *
	 * <p>If there is no intersection, this aligned box is cleared.
	 *
	 * @param box the box
	 * @see #createIntersection(Box3afp)
	 * @see #clear()
	 */
	default void setIntersection(Box3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null :  AssertMessages.notNullParameter();
		final var x1 = Math.max(getMinX(), box.getMinX());
		final var y1 = Math.max(getMinY(), box.getMinY());
		final var z1 = Math.max(getMinZ(), box.getMinZ());
		final var x2 = Math.min(getMaxX(), box.getMaxX());
		final var y2 = Math.min(getMaxY(), box.getMaxY());
		final var z2 = Math.min(getMaxZ(), box.getMaxZ());
		if (x1 <= x2 && y1 <= y2 && z1 <= z2) {
			setFromCorners(x1, y1, z1, x2, y2, z2);
		} else {
			clear();
		}
	}

	@Pure
	@Override
	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null :  AssertMessages.notNullParameter();
		return intersectsAlignedBoxAlignedBox(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				box.getMinX(), box.getMinY(), box.getMinZ(),
				box.getMaxX(), box.getMaxY(), box.getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null :  AssertMessages.notNullParameter();
		return Sphere3afp.intersectsSphereAlignedBox(
				sphere.getX(), sphere.getY(), sphere.getZ(),
				sphere.getRadius(),
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null :  AssertMessages.notNullParameter();
		return intersectsAlignedBoxSegment(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null :  AssertMessages.notNullParameter();
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		//TODO
		return false;

	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null :  AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	/** Move this rectangular prism to avoid collision
	 * with the reference rectangular box.
	 *
	 * @param reference is the rectangular prism we want to avoid collision with.
	 * @param result the displacement vector.
	 */
	default void avoidCollisionWith(AlignedBox3afp<?, ?, ?, ?, ?, ?> reference, Vector3D<?, ?, ?> result) {
		assert reference != null :  AssertMessages.notNullParameter();
		assert result != null : AssertMessages.notNullParameter();
		final var dx1 = reference.getMaxX() - getMinX();
		final var dx2 = reference.getMinX() - getMaxX();
		final var dy1 = reference.getMaxY() - getMinY();
		final var dy2 = reference.getMinY() - getMaxY();
		final var dz1 = reference.getMaxZ() - getMinZ();
		final var dz2 = reference.getMinZ() - getMaxZ();

		final var absdx1 = Math.abs(dx1);
		final var absdx2 = Math.abs(dx2);
		final var absdy1 = Math.abs(dy1);
		final var absdy2 = Math.abs(dy2);
		final var absdz1 = Math.abs(dz1);
		final var absdz2 = Math.abs(dz2);

		final var min = MathUtil.min(absdx1, absdx2, absdy1, absdy2, absdz1, absdz2);

		var dx = 0.;
		var dy = 0.;
		var dz = 0.;

		if (min == absdy1) {
			dy = dy1;
		} else if (min == absdy2) {
			dy = dy2;
		} else if (min == absdz1) {
			dz = dz1;
		} else if (min == absdz2) {
			dz = dz2;
		} else if (min == absdx1) {
			dx = dx1;
		} else {
			dx = dx2;
		}
		set(
				getMinX() + dx,
				getMinY() + dy,
				getMinZ() + dz,
				getWidth(),
				getHeight(),
				getDepth());
		if (result != null) {
			result.set(dx, dy, dz);
		}
	}

	/** Move this aligned box to avoid collision
	 * with the reference aligned box.
	 *
	 * @param reference is the aligned box to avoid collision with.
	 * @param displacementDirection is the direction of the allowed displacement (it is an input).
	 *     This vector is set according to the result before returning.
	 * @param result the displacement vector.
	 */
	default void avoidCollisionWith(AlignedBox3afp<?, ?, ?, ?, ?, ?> reference, Vector3D<?, ?, ?> displacementDirection,
			Vector3D<?, ?, ?> result) {
		assert reference != null : AssertMessages.notNullParameter(0);
		assert result != null : AssertMessages.notNullParameter(2);
		if (displacementDirection == null) {
			avoidCollisionWith(reference, result);
		} else if (displacementDirection.getLengthSquared() == 0) {
			avoidCollisionWith(reference, displacementDirection);
			if (result != null) {
				result.set(displacementDirection);
			}
		} else {
			final var dx1 = reference.getMaxX() - getMinX();
			final var dx2 = reference.getMinX() - getMaxX();
			final var dy1 = reference.getMaxY() - getMinY();
			final var dy2 = reference.getMinY() - getMaxY();
			final var dz1 = reference.getMaxZ() - getMinZ();
			final var dz2 = reference.getMinZ() - getMaxZ();

			final var absdx1 = Math.abs(dx1);
			final var absdx2 = Math.abs(dx2);
			final var absdy1 = Math.abs(dy1);
			final var absdy2 = Math.abs(dy2);
			final var absdz1 = Math.abs(dz1);
			final var absdz2 = Math.abs(dz2);

			final double dx;
			final double dy;
			final double dz;

			if (displacementDirection.getX() < 0) {
				dx = -Math.min(absdx1, absdx2);
			} else {
				dx = Math.min(absdx1, absdx2);
			}

			if (displacementDirection.getY() < 0) {
				dy = -Math.min(absdy1, absdy2);
			} else {
				dy = Math.min(absdy1, absdy2);
			}

			if (displacementDirection.getZ() < 0) {
				dz = -Math.min(absdz1, absdz2);
			} else {
				dz = Math.min(absdz1, absdz2);
			}

			set(
					getMinX() + dx,
					getMinY() + dy,
					getMinZ() + dz,
					getWidth(),
					getHeight(),
					getDepth());

			displacementDirection.set(dx, dy, dz);
			if (result != null) {
				result.set(dx, dy, dz);
			}
		}
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P cp = getGeomFactory().newPoint();
		findsClosestPointAlignedBoxPoint(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				pt.getX(), pt.getY(), pt.getZ(), cp);
		return cp;
	}

	@Override
	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		final P cp = getGeomFactory().newPoint();
		findsClosestPointAlignedBoxSphere(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				sphere.getX(), sphere.getY(), sphere.getZ(),
				sphere.getRadius(), cp);
		return cp;
	}

	@Override
	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		//TODO Path3afp.getClosestPointTo(getPathIterator(), path.getPathIterator(), point);
		return point;
	}

	@Override
	default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		findsClosestPointAlignedBoxAlignedBox(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
				box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ(),
				point);
		return point;
	}

	@Override
	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		findsClosestPointAlignedBoxSegment(
				getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
				segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2(), point);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		findsFarthestPointAlignedBoxPoint(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
	}

	/** Replies this aligned box with a Geogebra-compatible form.
	 *
	 * @return the Geogebra representation of the aligned box.
	 * @since 18.0
	 */
	default String toGeogebra() {
		return GeogebraUtil.toPrismDefinition(3,
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMinZ(),
				getMinX(), getMaxY(), getMinZ(),
				getMinX(), getMinY(), getMaxZ());
	}

}
