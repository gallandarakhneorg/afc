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
	 * @return {@code true} if the triangle and capsule are intersecting.
	 * @see "https://github.com/juj/MathGeoLib"
	 */
	@Pure
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
	
	@Pure
	@Override
	public boolean intersects(AbstractCapsule3F s) {
		return intersectsTriangleCapsule(
				getX1(), getY1(), getY1(),
				getX2(), getY2(), getY3(),
				getX3(), getY3(), getY3(),
				s.getMedialX1(), s.getMedialY1(), s.getMedialZ1(),
				s.getMedialX2(), s.getMedialY2(), s.getMedialZ2(),
				s.getRadius());
	}
	
	@Pure
	@Override
	public boolean intersects(AbstractOrientedBox3F s) {
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
	 * @return {@code true} if the triangle and oriented box are intersecting.
	 */
	@Pure
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
		double ntx1 = FunctionalVector3D.dotProduct(nx, ny, nz, ax1, ay1, az1);
		double nty1 = FunctionalVector3D.dotProduct(nx, ny, nz, ax2, ay2, az2);
		double ntz1 = FunctionalVector3D.dotProduct(nx, ny, nz, ax3, ay3, az3);

		nx = tx2 - cx;
		ny = ty2 - cy;
		nz = tz2 - cz;
		double ntx2 = FunctionalVector3D.dotProduct(nx, ny, nz, ax1, ay1, az1);
		double nty2 = FunctionalVector3D.dotProduct(nx, ny, nz, ax2, ay2, az2);
		double ntz2 = FunctionalVector3D.dotProduct(nx, ny, nz, ax3, ay3, az3);

		nx = tx3 - cx;
		ny = ty3 - cy;
		nz = tz3 - cz;
		double ntx3 = FunctionalVector3D.dotProduct(nx, ny, nz, ax1, ay1, az1);
		double nty3 = FunctionalVector3D.dotProduct(nx, ny, nz, ax2, ay2, az2);
		double ntz3 = FunctionalVector3D.dotProduct(nx, ny, nz, ax3, ay3, az3);

		// Test intersection
		return intersectsTriangleAlignedBox(
				ntx1, nty1, ntz1, ntx2, nty2, ntz2, ntx3, nty3, ntz3,
				-ae1, -ae2, -ae3, ae1, ae2, ae3);
	}
}
