/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.coordinatesystem;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.ImmutableVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Quaternion.AxisAngle;
import org.arakhne.afc.math.geometry.d3.Quaternion.QuaternionComponents;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.InnerComputationGeomFactory3afp;
import org.arakhne.afc.math.geometry.d3.afp.InnerComputationPoint3afp;
import org.arakhne.afc.math.geometry.d3.afp.InnerComputationQuaternionafp;
import org.arakhne.afc.math.geometry.d3.afp.InnerComputationVector3afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Represents the different kind of 3D referencials and provides the conversion utilities.
 *
 * <p>A referencial axis is expressed by the front, left and top directions. For example {@code XYZ_LEFT_HAND} is for the
 * coordinate system with front direction along {@code +/-X} axis, left direction along the {@code +/-Y} axis and top
 * direction along the {@code +/-Z} axis according to a "left-hand" heuristic.
 *
 * <p>The default coordinate system is:
 * <ul>
 * <li>front: {@code (1, 0, 0)}</li>
 * <li>left: {@code (0, 1, 0)}</li>
 * <li>top: {@code (0, 0, 1)}</li>
 * </ul>
 *
 * <h3>Rotations</h3>
 *
 * <p>Rotations in a 3D coordinate system follow the right/left hand rules
 * (assuming that {@code OX}, {@code OY} and {@code OZ} are the three axis of the coordinate system):
 * <table width="100%" summary="Rotations">
 * <tr>
 * <td>Right-handed rule:</td>
 * <td>
 * <ul>
 * <li>axis cycle is: {@code OX} &gt; {@code OY} &gt; {@code OZ} &gt; {@code OX} &gt; {@code OY};</li>
 * <li>when rotating around {@code OX}, positive rotation angle is going from {@code OY} to {@code OZ}</li>
 * <li>when rotating around {@code OY}, positive rotation angle is going from {@code OZ} to {@code OX}</li>
 * <li>when rotating around {@code OZ}, positive rotation angle is going from {@code OX} to {@code OY}</li>
 * </ul><br>
 * <img width="200" src="doc-files/rotation_right.png" alt="[Right-handed Rotation Rule]">
 * </td>
 * </tr><tr>
 * <td>Left-handed rule:</td>
 * <td>
 * <ul>
 * <li>axis cycle is: {@code OX} &gt; {@code OY} &gt; {@code OZ} &gt; {@code OX} &gt; {@code OY};</li>
 * <li>when rotating around {@code OX}, positive rotation angle is going from {@code OY} to {@code OZ}</li>
 * <li>when rotating around {@code OY}, positive rotation angle is going from {@code OZ} to {@code OX}</li>
 * <li>when rotating around {@code OZ}, positive rotation angle is going from {@code OX} to {@code OY}</li>
 * </ul><br>
 * <img width="200" src="doc-files/rotation_left.png" alt="[Left-handed Rotation Rule]">
 * </td>
 * </tr></table>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public enum CoordinateSystem3D implements CoordinateSystem {

	/**
	 * Left handed XZY coordinate system.
	 *
	 * <p><a href="doc-files/xzy_left.png"><img src="doc-files/xzy_left.png" width="200" alt=
	 * "[Left Handed XZY Coordinate System]"></a>
	 */
	XZY_LEFT_HAND {
		@Override
		public Vector3D<?, ?, ?> getLeftVector() {
			return new ImmutableVector3D(0, 0, 1);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getLeftVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 0, 1);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getRightVector() {
			return new ImmutableVector3D(0, 0, -1);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getRightVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 0, -1);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getUpVector() {
			return new ImmutableVector3D(0, 1, 0);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getUpVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 1, 0);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getDownVector() {
			return new ImmutableVector3D(0, -1, 0);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getDownVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, -1, 0);
			return vectorToFill;
		}

		@Override
		public boolean isRightHanded() {
			return false;
		}

		@Override
		public boolean isLeftHanded() {
			return true;
		}

		@Override
		public CoordinateSystem2D toCoordinateSystem2D() {
			return CoordinateSystem2D.XY_RIGHT_HAND;
		}

		@Override
		public void toSystem(Tuple3D<?> tuple, CoordinateSystem3D targetCoordinateSystem) {
			assert tuple != null : AssertMessages.notNullParameter(0);
			assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
			switch (targetCoordinateSystem) {
			case XZY_RIGHT_HAND:
				tuple.setY(-tuple.getY());
				break;
			case XYZ_LEFT_HAND:
				double oy = tuple.getY();
				tuple.setY(tuple.getZ());
				tuple.setZ(-oy);
				break;
			case XYZ_RIGHT_HAND:
				oy = tuple.getY();
				tuple.setY(tuple.getZ());
				tuple.setZ(oy);
				break;
			default:
			}
		}

		@Override
		public QuaternionComponents toSystem(QuaternionComponents rotation, CoordinateSystem3D targetCoordinateSystem) {
			assert rotation != null : AssertMessages.notNullParameter(0);
			assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
			final AxisAngle axisangle = Quaternion.computeAxisAngle(rotation.x(), rotation.y(), rotation.z(), rotation.w());
			double x = axisangle.x();
			double y = axisangle.y();
			double z = axisangle.z();
			double a = axisangle.angle();
			switch (targetCoordinateSystem) {
			case XZY_RIGHT_HAND:
				z = -z;
				a = -a;
				break;
			case XYZ_LEFT_HAND:
				double oy = y;
				y = -z;
				z = oy;
				a = -a;
				break;
			case XYZ_RIGHT_HAND:
				oy = y;
				y = z;
				z = oy;
				break;
			default:
			}
			return Quaternion.computeWithAxisAngle(x, y, z, a);
		}

		@Override
		public boolean isZOnUp() {
			return false;
		}

		@Override
		public boolean isYOnUp() {
			return true;
		}

		@Override
		public boolean isZOnSide() {
			return true;
		}

		@Override
		public boolean isYOnSide() {
			return false;
		}

		@Override
		public void toCoordinateSystem2D(Tuple3D<?> point, Tuple2D<?> result) {
			assert point != null : AssertMessages.notNullParameter(0);
			assert result != null : AssertMessages.notNullParameter(1);
			result.set(point.getX(), point.getZ());
		}

		@Override
		protected double toCoordinateSystem2DAngleFromTransformation(Transform3D mat) {
			assert mat != null : AssertMessages.notNullParameter(0);
			final InnerComputationVector3afp ptR = new InnerComputationVector3afp(1., 0., 0.);
			mat.transform(ptR);
			return Vector2D.signedAngle(ptR.getX(), ptR.getZ(), 1., 0.);
		}

		@Override
		public void fromCoordinateSystem2D(Tuple2D<?> point, double height, Tuple3D<?> result) {
			assert point != null : AssertMessages.notNullParameter(0);
			assert result != null : AssertMessages.notNullParameter(2);
			result.set(point.getX(), height, point.getY());
		}

		@Override
		public void fromCoordinateSystem2D(double rotation, Quaternion<?, ?, ?> quaternion) {
			assert quaternion != null : AssertMessages.notNullParameter(1);
			quaternion.setAxisAngle(0., 1., 0., rotation);
		}

		@Override
		public double getSideCoordinate(double x, double y, double z) {
			return z;
		}

		@Override
		public double getVerticalCoordinate(double x, double y, double z) {
			return y;
		}

		@Override
		public void setSideCoordinate(Tuple3D<?> tuple, double value) {
			tuple.setZ(value);
		}

		@Override
		public void setVerticalCoordinate(Tuple3D<?> tuple, double value) {
			tuple.setY(value);
		}

		@Override
		public void addSideCoordinate(Tuple3D<?> tuple, double value) {
			tuple.addZ(value);
		}

		@Override
		public void addVerticalCoordinate(Tuple3D<?> tuple, double value) {
			tuple.addY(value);
		}
	},

	/**
	 * Left handed XYZ coordinate system.
	 *
	 * <p><a href="doc-files/xyz_left.png"><img src="doc-files/xyz_left.png" width="200" alt=
	 * "[Left Handed XYZ Coordinate System]"></a>
	 */
	XYZ_LEFT_HAND {
		@Override
		public Vector3D<?, ?, ?> getLeftVector() {
			return new ImmutableVector3D(0, -1, 0);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getLeftVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, -1, 0);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getRightVector() {
			return new ImmutableVector3D(0, 1, 0);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getRightVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 1, 0);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getUpVector() {
			return new ImmutableVector3D(0, 0, 1);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getUpVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 0, 1);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getDownVector() {
			return new ImmutableVector3D(0, 0, -1);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getDownVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 0, -1);
			return vectorToFill;
		}

		@Override
		public boolean isRightHanded() {
			return false;
		}

		@Override
		public boolean isLeftHanded() {
			return true;
		}

		@Override
		public CoordinateSystem2D toCoordinateSystem2D() {
			return CoordinateSystem2D.XY_LEFT_HAND;
		}

		@Override
		public void toSystem(Tuple3D<?> tuple, CoordinateSystem3D targetCoordinateSystem) {
			assert tuple != null : AssertMessages.notNullParameter(0);
			assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
			switch (targetCoordinateSystem) {
			case XYZ_RIGHT_HAND:
				tuple.setY(-tuple.getY());
				break;
			case XZY_LEFT_HAND:
				double oy = tuple.getY();
				tuple.setY(tuple.getZ());
				tuple.setZ(-oy);
				break;
			case XZY_RIGHT_HAND:
				oy = tuple.getY();
				tuple.setY(tuple.getZ());
				tuple.setZ(oy);
				break;
			default:
			}
		}

		@Override
		public QuaternionComponents toSystem(QuaternionComponents rotation, CoordinateSystem3D targetCoordinateSystem) {
			assert rotation != null : AssertMessages.notNullParameter(0);
			assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
			final AxisAngle axisangle = Quaternion.computeAxisAngle(rotation.x(), rotation.y(), rotation.z(), rotation.w());
			double x = axisangle.x();
			double y = axisangle.y();
			double z = axisangle.z();
			double a = axisangle.angle();
			switch (targetCoordinateSystem) {
			case XYZ_RIGHT_HAND:
				y = -y;
				a = -a;
				break;
			case XZY_LEFT_HAND:
				double oy = y;
				y = z;
				z = -oy;
				a = -a;
				break;
			case XZY_RIGHT_HAND:
				oy = y;
				y = z;
				z = oy;
				break;
			default:
			}
			return Quaternion.computeWithAxisAngle(x, y, z, a);
		}

		@Override
		public boolean isZOnUp() {
			return true;
		}

		@Override
		public boolean isYOnUp() {
			return false;
		}

		@Override
		public boolean isZOnSide() {
			return false;
		}

		@Override
		public boolean isYOnSide() {
			return true;
		}

		@Override
		public void toCoordinateSystem2D(Tuple3D<?> point, Tuple2D<?> result) {
			assert point != null : AssertMessages.notNullParameter(0);
			assert result != null : AssertMessages.notNullParameter(1);
			result.set(point.getX(), point.getY());
		}

		@Override
		protected double toCoordinateSystem2DAngleFromTransformation(Transform3D mat) {
			assert mat != null : AssertMessages.notNullParameter(0);
			final InnerComputationVector3afp ptR = new InnerComputationVector3afp(1., 0., 0.);
			mat.transform(ptR);
			return Vector2D.signedAngle(1., 0., ptR.getX(), ptR.getY());
		}

		@Override
		public void fromCoordinateSystem2D(Tuple2D<?> point, double height, Tuple3D<?> result) {
			assert point != null : AssertMessages.notNullParameter(0);
			assert result != null : AssertMessages.notNullParameter(2);
			result.set(point.getX(), point.getY(), height);
		}

		@Override
		public void fromCoordinateSystem2D(double rotation, Quaternion<?, ?, ?> quaternion) {
			assert quaternion != null : AssertMessages.notNullParameter(1);
			quaternion.setAxisAngle(0., 0., 1., rotation);			
		}

		@Override
		public double getSideCoordinate(double x, double y, double z) {
			return y;
		}

		@Override
		public double getVerticalCoordinate(double x, double y, double z) {
			return z;
		}

		@Override
		public void setSideCoordinate(Tuple3D<?> tuple, double value) {
			tuple.setY(value);
		}

		@Override
		public void setVerticalCoordinate(Tuple3D<?> tuple, double value) {
			tuple.setZ(value);
		}

		@Override
		public void addSideCoordinate(Tuple3D<?> tuple, double value) {
			tuple.addY(value);
		}

		@Override
		public void addVerticalCoordinate(Tuple3D<?> tuple, double value) {
			tuple.addZ(value);
		}
	},

	/**
	 * Right handed XZY coordinate system.
	 *
	 * <p><a href="doc-files/xzy_right.png"><img src="doc-files/xzy_right.png" width="200" alt=
	 * "[Right Handed XZY Coordinate System]"></a>
	 */
	XZY_RIGHT_HAND {
		@Override
		public Vector3D<?, ?, ?> getLeftVector() {
			return new ImmutableVector3D(0, 0, -1);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getLeftVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 0, -1);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getRightVector() {
			return new ImmutableVector3D(0, 0, 1);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getRightVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 0, 1);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getUpVector() {
			return new ImmutableVector3D(0, 1, 0);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getUpVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 1, 0);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getDownVector() {
			return new ImmutableVector3D(0, -1, 0);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getDownVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, -1, 0);
			return vectorToFill;
		}

		@Override
		public boolean isRightHanded() {
			return true;
		}

		@Override
		public boolean isLeftHanded() {
			return false;
		}

		@Override
		public CoordinateSystem2D toCoordinateSystem2D() {
			return CoordinateSystem2D.XY_LEFT_HAND;
		}

		@Override
		public void toSystem(Tuple3D<?> tuple, CoordinateSystem3D targetCoordinateSystem) {
			assert tuple != null : AssertMessages.notNullParameter(0);
			assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
			switch (targetCoordinateSystem) {
			case XZY_LEFT_HAND:
				tuple.setY(-tuple.getY());
				break;
			case XYZ_RIGHT_HAND:
				double oy = tuple.getY();
				tuple.setY(tuple.getZ());
				tuple.setZ(-oy);
				break;
			case XYZ_LEFT_HAND:
				oy = tuple.getY();
				tuple.setY(tuple.getZ());
				tuple.setZ(oy);
				break;
			default:
			}
		}


		@Override
		public QuaternionComponents toSystem(QuaternionComponents rotation, CoordinateSystem3D targetCoordinateSystem) {
			assert rotation != null : AssertMessages.notNullParameter(0);
			assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
			final AxisAngle axisangle = Quaternion.computeAxisAngle(rotation.x(), rotation.y(), rotation.z(), rotation.w());
			double x = axisangle.x();
			double y = axisangle.y();
			double z = axisangle.z();
			double a = axisangle.angle();
			switch (targetCoordinateSystem) {
			case XZY_LEFT_HAND:
				z = -z;
				a = -a;
				break;
			case XYZ_RIGHT_HAND:
				double oy = y;
				y = -z;
				z = oy;
				a = -a;
				break;
			case XYZ_LEFT_HAND:
				oy = y;
				y = z;
				z = oy;
				break;
			default:
			}
			return Quaternion.computeWithAxisAngle(x, y, z, a);
		}

		@Override
		public boolean isZOnUp() {
			return false;
		}

		@Override
		public boolean isYOnUp() {
			return true;
		}

		@Override
		public boolean isZOnSide() {
			return true;
		}

		@Override
		public boolean isYOnSide() {
			return false;
		}

		@Override
		public void toCoordinateSystem2D(Tuple3D<?> point, Tuple2D<?> result) {
			assert point != null : AssertMessages.notNullParameter(0);
			assert result != null : AssertMessages.notNullParameter(1);
			result.set(point.getX(), point.getZ());
		}

		@Override
		protected double toCoordinateSystem2DAngleFromTransformation(Transform3D mat) {
			assert mat != null : AssertMessages.notNullParameter(0);
			final InnerComputationVector3afp ptR = new InnerComputationVector3afp(1., 0., 0.);
			mat.transform(ptR);
			return Vector2D.signedAngle(ptR.getX(), ptR.getZ(), 1., 0.);
		}

		@Override
		public void fromCoordinateSystem2D(Tuple2D<?> point, double height, Tuple3D<?> result) {
			assert point != null : AssertMessages.notNullParameter(0);
			assert result != null : AssertMessages.notNullParameter(2);
			result.set(point.getX(), height, point.getY());
		}

		@Override
		public void fromCoordinateSystem2D(double rotation, Quaternion<?, ?, ?> quaternion) {
			assert quaternion != null : AssertMessages.notNullParameter(1);
			quaternion.setAxisAngle(0., 1., 0., rotation);
		}

		@Override
		public double getSideCoordinate(double x, double y, double z) {
			return z;
		}

		@Override
		public double getVerticalCoordinate(double x, double y, double z) {
			return y;
		}

		@Override
		public void setSideCoordinate(Tuple3D<?> tuple, double value) {
			tuple.setZ(value);
		}

		@Override
		public void setVerticalCoordinate(Tuple3D<?> tuple, double value) {
			tuple.setY(value);
		}

		@Override
		public void addSideCoordinate(Tuple3D<?> tuple, double value) {
			tuple.addZ(value);
		}

		@Override
		public void addVerticalCoordinate(Tuple3D<?> tuple, double value) {
			tuple.addY(value);
		}
	},

	/**
	 * Right handed XYZ coordinate system.
	 *
	 * <p><a href="doc-files/xyz_right.png"><img src="doc-files/xyz_right.png" width="200" alt=
	 * "[Right Handed XYZ Coordinate System]"></a>
	 */
	XYZ_RIGHT_HAND {
		@Override
		public Vector3D<?, ?, ?> getLeftVector() {
			return new ImmutableVector3D(0, 1, 0);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getLeftVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 1, 0);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getRightVector() {
			return new ImmutableVector3D(0, -1, 0);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getRightVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, -1, 0);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getUpVector() {
			return new ImmutableVector3D(0, 0, 1);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getUpVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 0, 1);
			return vectorToFill;
		}

		@Override
		public Vector3D<?, ?, ?> getDownVector() {
			return new ImmutableVector3D(0, 0, -1);
		}

		@Override
		public <T extends Vector3D<?, ?, ?>> T getDownVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 0, -1);
			return vectorToFill;
		}

		@Override
		public boolean isRightHanded() {
			return true;
		}

		@Override
		public boolean isLeftHanded() {
			return false;
		}

		@Override
		public CoordinateSystem2D toCoordinateSystem2D() {
			return CoordinateSystem2D.XY_RIGHT_HAND;
		}

		@Override
		public void toSystem(Tuple3D<?> tuple, CoordinateSystem3D targetCoordinateSystem) {
			assert tuple != null : AssertMessages.notNullParameter(0);
			assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
			switch (targetCoordinateSystem) {
			case XYZ_LEFT_HAND:
				tuple.setY(-tuple.getY());
				break;
			case XZY_RIGHT_HAND:
				double oy = tuple.getY();
				tuple.setY(tuple.getZ());
				tuple.setZ(-oy);
				break;
			case XZY_LEFT_HAND:
				oy = tuple.getY();
				tuple.setY(tuple.getZ());
				tuple.setZ(oy);
				break;
			default:
			}
		}

		@Override
		public QuaternionComponents toSystem(QuaternionComponents rotation, CoordinateSystem3D targetCoordinateSystem) {
			assert rotation != null : AssertMessages.notNullParameter(0);
			assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
			final AxisAngle axisangle = Quaternion.computeAxisAngle(rotation.x(), rotation.y(), rotation.z(), rotation.w());
			double x = axisangle.x();
			double y = axisangle.y();
			double z = axisangle.z();
			double a = axisangle.angle();
			switch (targetCoordinateSystem) {
			case XYZ_LEFT_HAND:
				y = -y;
				a = -a;
				break;
			case XZY_RIGHT_HAND:
				double oy = y;
				y = z;
				z = -oy;
				a = -a;
				break;
			case XZY_LEFT_HAND:
				oy = y;
				y = z;
				z = oy;
				break;
			default:
			}
			return Quaternion.computeWithAxisAngle(x, y, z, a);
		}

		@Override
		public boolean isZOnUp() {
			return true;
		}

		@Override
		public boolean isYOnUp() {
			return false;
		}

		@Override
		public boolean isZOnSide() {
			return false;
		}

		@Override
		public boolean isYOnSide() {
			return true;
		}

		@Override
		public void toCoordinateSystem2D(Tuple3D<?> point, Tuple2D<?> result) {
			assert point != null : AssertMessages.notNullParameter(0);
			assert result != null : AssertMessages.notNullParameter(1);
			result.set(point.getX(), point.getY());
		}

		@Override
		protected double toCoordinateSystem2DAngleFromTransformation(Transform3D mat) {
			assert mat != null : AssertMessages.notNullParameter(0);
			final InnerComputationVector3afp ptR = new InnerComputationVector3afp(1., 0., 0.);
			mat.transform(ptR);
			return Vector2D.signedAngle(1., 0., ptR.getX(), ptR.getY());
		}

		@Override
		public void fromCoordinateSystem2D(Tuple2D<?> point, double height, Tuple3D<?> result) {
			assert point != null : AssertMessages.notNullParameter(0);
			assert result != null : AssertMessages.notNullParameter(2);
			result.set(point.getX(), point.getY(), height);
		}

		@Override
		public void fromCoordinateSystem2D(double rotation, Quaternion<?, ?, ?> quaternion) {
			assert quaternion != null : AssertMessages.notNullParameter(1);
			quaternion.setAxisAngle(0., 0., 1., rotation);
		}

		@Override
		public double getSideCoordinate(double x, double y, double z) {
			return y;
		}

		@Override
		public double getVerticalCoordinate(double x, double y, double z) {
			return z;
		}

		@Override
		public void setSideCoordinate(Tuple3D<?> tuple, double value) {
			tuple.setY(value);
		}

		@Override
		public void setVerticalCoordinate(Tuple3D<?> tuple, double value) {
			tuple.setZ(value);
		}

		@Override
		public void addSideCoordinate(Tuple3D<?> tuple, double value) {
			tuple.addY(value);
		}

		@Override
		public void addVerticalCoordinate(Tuple3D<?> tuple, double value) {
			tuple.addZ(value);
		}
	};

	private static CoordinateSystem3D userDefault;

	@Pure
	@Override
	public abstract boolean isRightHanded();

	@Pure
	@Override
	public abstract boolean isLeftHanded();

	/**
	 * Replies the preferred coordinate system.
	 *
	 * @return the preferred coordinate system.
	 */
	@Pure
	public static CoordinateSystem3D getDefaultCoordinateSystem() {
		if (userDefault != null) {
			return userDefault;
		}
		return CoordinateSystemConstants.SIMULATION_3D;
	}

	@Pure
	@Override
	public final int getDimensions() {
		return 3;
	}

	/** Set the default coordinate system.
	 *
	 * @param system is the new default coordinate system.
	 * @see #getDefaultCoordinateSystem()
	 */
	public static void setDefaultCoordinateSystem(CoordinateSystem3D system) {
		CoordinateSystem3D.userDefault = system;
	}

	/** Replies the front vector.
	 *
	 * @return the front vector.
	 * @since 18.0
	 */
	@Pure
	@Inline(value = "new ImmutableVector3D(1, 0, 0)", imported = {ImmutableVector3D.class})
	public static Vector3D<?, ?, ?> getViewVector() {
		return new ImmutableVector3D(1, 0, 0);
	}

	/** Replies the front vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the front vector values.
	 * @return {@code vectorToFill}.
	 * @since 18.0
	 */
	public static <T extends Vector3D<?, ?, ?>> T getViewVector(T vectorToFill) {
		assert vectorToFill != null;
		vectorToFill.set(1, 0, 0);
		return vectorToFill;
	}

	/** Replies the back vector.
	 *
	 * @return the back vector.
	 * @since 18.0
	 */
	@Pure
	@Inline(value = "new ImmutableVector3D(-1, 0, 0)", imported = {ImmutableVector3D.class})
	public static Vector3D<?, ?, ?> getBackVector() {
		return new ImmutableVector3D(-1, 0, 0);
	}

	/** Replies the back vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the back vector values.
	 * @return {@code vectorToFill}.
	 * @since 18.0
	 */
	public static <T extends Vector3D<?, ?, ?>> T getBackVector(T vectorToFill) {
		assert vectorToFill != null;
		vectorToFill.set(-1, 0, 0);
		return vectorToFill;
	}

	/** Replies the left vector.
	 *
	 * @return the left vector.
	 * @throws CoordinateSystemNotFoundException if the source coordinate system is undetermined.
	 * @since 18.0
	 */
	@Pure
	public abstract Vector3D<?, ?, ?> getLeftVector();

	/** Replies the left vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the left vector values.
	 * @return the left vector.
	 * @throws CoordinateSystemNotFoundException if the source coordinate system is undetermined.
	 * @since 18.0
	 */
	public abstract <T extends Vector3D<?, ?, ?>> T getLeftVector(T vectorToFill);

	/** Replies the right vector.
	 *
	 * @return the right vector.
	 * @throws CoordinateSystemNotFoundException if the source coordinate system is undetermined.
	 * @since 18.0
	 */
	@Pure
	public abstract Vector3D<?, ?, ?> getRightVector();;

	/** Replies the right vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the right vector values.
	 * @return the right vector.
	 * @throws CoordinateSystemNotFoundException if the source coordinate system is undetermined.
	 * @since 18.0
	 */
	public abstract <T extends Vector3D<?, ?, ?>> T getRightVector(T vectorToFill);

	/** Replies the up vector.
	 *
	 * @return the up vector.
	 * @throws CoordinateSystemNotFoundException if the source coordinate system is undetermined.
	 * @since 18.0
	 */
	@Pure
	public abstract Vector3D<?, ?, ?> getUpVector();

	/** Replies the up vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the up vector values.
	 * @return the up vector.
	 * @throws CoordinateSystemNotFoundException if the source coordinate system is undetermined.
	 * @since 18.0
	 */
	public abstract <T extends Vector3D<?, ?, ?>> T getUpVector(T vectorToFill);

	/** Replies the down vector.
	 *
	 * @return the down vector.
	 * @throws CoordinateSystemNotFoundException if the source coordinate system is undetermined.
	 * @since 18.0
	 */
	@Pure
	public abstract Vector3D<?, ?, ?> getDownVector();

	/** Replies the down vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the down vector values.
	 * @return the down vector.
	 * @throws CoordinateSystemNotFoundException if the source coordinate system is undetermined.
	 * @since 18.0
	 */
	public abstract <T extends Vector3D<?, ?, ?>> T getDownVector(T vectorToFill);

	/** Replies if the z coordinate is the up direction.
	 * 
	 * @return {@code true} if z coordiante is up.
	 * @since 18.0
	 */
	@Pure
	public abstract boolean isZOnUp();

	/** Replies if the y coordinate is the up direction.
	 * 
	 * @return {@code true} if y coordiante is up.
	 * @since 18.0
	 */
	@Pure
	public abstract boolean isYOnUp();

	/** Replies if the z coordinate is the side direction (left or right).
	 * 
	 * @return {@code true} if z coordiante is side.
	 * @since 18.0
	 */
	@Pure
	public abstract boolean isZOnSide();

	/** Replies if the y coordinate is the side direction (left or right).
	 * 
	 * @return {@code true} if y coordiante is side.
	 * @since 18.0
	 */
	@Pure
	public abstract boolean isYOnSide();

	/** Replies the 3D coordinate system which is corresponding to
	 * this 2D coordinate system.
	 *
	 * @return the 3D coordinate system
	 * @throws CoordinateSystemNotFoundException if the source coordinate system is undetermined.
	 * @since 18.0
	 */
	@Pure
	public abstract CoordinateSystem2D toCoordinateSystem2D();

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 *
	 * <p>The front vector is assumed to be always {@code (1, 0, 0)},
	 * the left vector is {@code (0, ly, 0) or {@code (0, 0, lz)}}.
	 * If {@code ly} is equal to zero, then the {@code lz} coordinate is used for determining
	 * the coordinate system.
	 *
	 * @param ly y coordinate to left.
	 * @param lz z coordinate to left.
	 * @return the coordinate system which is corresponding to the specified vector.
	 * @since 18.0
	 */
	@Pure
	public static CoordinateSystem3D fromVectors(int ly, int lz) {
		if (ly == 0) {
			assert lz != 0;
			return (lz < 0) ? XZY_RIGHT_HAND : XZY_LEFT_HAND;
		}
		return (ly < 0) ? XYZ_LEFT_HAND : XYZ_RIGHT_HAND;
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 *
	 * <p>The front vector is assumed to be always {@code (1, 0, 0)},
	 * the left vector is {@code (0, ly, 0) or {@code (0, 0, lz)}}.
	 * If {@code ly} is equal to zero, then the {@code lz} coordinate is used for determining
	 * the coordinate system.
	 *
	 * @param ly y coordinate to left.
	 * @param lz z coordinate to left.
	 * @return the coordinate system which is corresponding to the specified vector.
	 * @since 18.0
	 */
	@Pure
	public static CoordinateSystem3D fromVectors(double ly, double lz) {
		if (ly == 0.) {
			assert lz != 0.;
			return (lz < 0.) ? XZY_RIGHT_HAND : XZY_LEFT_HAND;
		}
		return (ly < 0.) ? XYZ_LEFT_HAND : XYZ_RIGHT_HAND;
	}

	/** Convert the specified point or vector from the current coordinate system
	 * to the specified coordinate system.
	 *
	 * @param tuple is the point/vector to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 * @since 18.0
	 */
	public abstract void toSystem(Tuple3D<?> tuple, CoordinateSystem3D targetCoordinateSystem);

	/** Convert the specified rotation from the current coordinate system
	 * to the specified coordinate system.
	 *
	 * @param rotation is the rotation to convert and the result of the conversion.
	 * @param targetCoordinateSystem is the target coordinate system.
	 * @since 18.0
	 */
	@Pure
	public void toSystem(Quaternion<?, ?, ?> rotation, CoordinateSystem3D targetCoordinateSystem) {
		QuaternionComponents comps = new QuaternionComponents(rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW());
		comps = toSystem(comps, targetCoordinateSystem);
		rotation.set(comps.x(), comps.y(), comps.z(), comps.w());
	}

	/** Convert the specified rotation from the current coordinate system
	 * to the specified coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 * @return the converted rotation
	 * @since 18.0
	 */
	@Pure
	public abstract QuaternionComponents toSystem(QuaternionComponents rotation, CoordinateSystem3D targetCoordinateSystem);

	/** Convert the specified transformation matrix from the current coordinate system
	 * to the specified coordinate system.
	 *
	 * @param matrix is the matrix to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 * @since 18.0
	 */
	public void toSystem(Transform3D matrix, CoordinateSystem3D targetCoordinateSystem) {
		assert matrix != null : AssertMessages.notNullParameter(0);
		assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);

		final InnerComputationPoint3afp translation = new InnerComputationPoint3afp(
				matrix.getTranslationX(), matrix.getTranslationY(), matrix.getTranslationZ());
		final InnerComputationQuaternionafp rotation = matrix.getRotation(InnerComputationGeomFactory3afp.SINGLETON);

		toSystem(translation, targetCoordinateSystem);
		toSystem(rotation, targetCoordinateSystem);

		matrix.setTranslation(translation);
		matrix.setRotation(rotation);
	}

	/** Convert the specified point into the default coordinate system.
	 *
	 * @param point is the point to convert
	 * @since 18.0
	 */
	@Inline(value = "toSystem($1, $2.getDefaultCoordinateSystem())", imported = CoordinateSystem3D.class)
	public void toDefault(Point3D<?, ?, ?> point) {
		toSystem(point, getDefaultCoordinateSystem());
	}

	/** Convert the specified vector from the default coordinate system.
	 *
	 * @param vector is the vector to convert
	 * @since 18.0
	 */
	@Inline(value = "toSystem($1, $2.getDefaultCoordinateSystem())", imported = CoordinateSystem3D.class)
	public void toDefault(Vector3D<?, ?, ?> vector) {
		toSystem(vector, getDefaultCoordinateSystem());
	}

	/** Convert the specified transformation matrix from the default coordinate system.
	 *
	 * @param matrix is the matrix to convert
	 * @since 18.0
	 */
	@Inline(value = "toSystem($1, $2.getDefaultCoordinateSystem())", imported = CoordinateSystem3D.class)
	public void toDefault(Transform3D matrix) {
		toSystem(matrix, getDefaultCoordinateSystem());
	}

	/** Convert the specified rotation into the default coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 * @return the converted rotation
	 * @since 18.0
	 */
	@Inline(value = "toSystem($1, $2.getDefaultCoordinateSystem())", imported = CoordinateSystem3D.class)
	public void toDefault(Quaternion<?, ?, ?> rotation) {
		toSystem(rotation, getDefaultCoordinateSystem());
	}

	/** Convert the specified point into the default coordinate system.
	 *
	 * @param point is the point to convert
	 * @since 18.0
	 */
	@Inline(value = "$2.getDefaultCoordinateSystem().toSystem($1, $0)", imported = CoordinateSystem3D.class)
	public void fromDefault(Point3D<?, ?, ?> point) {
		getDefaultCoordinateSystem().toSystem(point, this);
	}

	/** Convert the specified vector from the default coordinate system.
	 *
	 * @param vector is the vector to convert
	 * @since 18.0
	 */
	@Inline(value = "$2.getDefaultCoordinateSystem().toSystem($1, $0)", imported = CoordinateSystem3D.class)
	public void fromDefault(Vector3D<?, ?, ?> vector) {
		getDefaultCoordinateSystem().toSystem(vector, this);
	}

	/** Convert the specified transformation matrix from the default coordinate system.
	 *
	 * @param matrix is the matrix to convert
	 * @since 18.0
	 */
	@Inline(value = "$2.getDefaultCoordinateSystem().toSystem($1, $0)", imported = CoordinateSystem3D.class)
	public void fromDefault(Transform3D matrix) {
		getDefaultCoordinateSystem().toSystem(matrix, this);
	}

	/** Convert the specified rotation into the default coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 * @return the converted rotation
	 * @since 18.0
	 */
	@Inline(value = "$2.getDefaultCoordinateSystem().toSystem($1, $0)", imported = CoordinateSystem3D.class)
	public void fromDefault(Quaternion<?, ?, ?> rotation) {
		getDefaultCoordinateSystem().toSystem(rotation, this);
	}

	/** Convert the specified tuple from the current 3D coordinate system
	 * to the specified 2D coordinate system.
	 * 
	 * @param point is the point to convert
	 * @param result the 2D point.
	 * @since 18.0
	 */
	@Pure
	public abstract void toCoordinateSystem2D(Tuple3D<?> point, Tuple2D<?> result) ;

	/** Convert the specified quaternion from the current 3D coordinate system
	 * to the specified 2D coordinate system.
	 * 
	 * @param rotation is the quaternion to convert
	 * @return the 2D rotation.
	 * @since 18.0
	 */
	@Pure
	public double toCoordinateSystem2D(Quaternion<?, ?, ?> rotation) {
		final Transform3D trans = new Transform3D();
		trans.makeRotationMatrix(rotation);
		return toCoordinateSystem2DAngleFromTransformation(trans);
	}

	/** Convert the rotation in the specified transformation matrix from the current 3D coordinate system
	 * to the specified 2D coordinate system.
	 * 
	 * @param matrix is the 3D rotation matrix to convert.
	 * @return the 2D rotation.
	 * @since 18.0
	 */
	@Pure
	protected abstract double toCoordinateSystem2DAngleFromTransformation(Transform3D mat);

	/** Convert the specified tuple from the specified 2D coordinate system
	 * to the current 3D coordinate system.
	 * 
	 * @param point is the 2D point to convert
	 * @param result the 3D point.
	 * @since 18.0
	 */
	@Pure
	@Inline("fromCoordinateSystem2D($1, 0, $2)")
	public void fromCoordinateSystem2D(Tuple2D<?> point, Tuple3D<?> result) {
		fromCoordinateSystem2D(point, 0, result);
	}

	/** Convert the specified tuple from the specified 2D coordinate system
	 * to the current 3D coordinate system.
	 * 
	 * @param point is the 2D point to convert
	 * @param height the height value. 
	 * @param result the 3D point.
	 * @since 18.0
	 */
	@Pure
	public abstract void fromCoordinateSystem2D(Tuple2D<?> point, double height, Tuple3D<?> result) ;

	/** Convert the specified 2D rotation angle from the specified 2D coordinate system
	 * to the current 3D coordinate system.
	 * 
	 * @param rotation is the 2D rotation to convert
	 * @param quaternion the 3D quaternion.
	 * @since 18.0
	 */
	@Pure
	public abstract void fromCoordinateSystem2D(double rotation, Quaternion<?, ?, ?> quaternion);

	/** Extract the side coordinate from the 3 coordinates.
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @return one of the provided coordinates that corresponds to the side direction.
	 * @since 18.0
	 */
	@Pure
	public abstract double getSideCoordinate(double x, double y, double z);

	/** Extract the side coordinate from the 3 coordinates.
	 * 
	 * @param coordinates the coordinates.
	 * @return one of the provided coordinates that corresponds to the side direction.
	 * @since 18.0
	 */
	@Pure
	@Inline("getSideCoordinate(($1).getX(), ($1).getY(), ($1).getZ())")
	public double getSideCoordinate(Tuple3D<?> coordinates) {
		return getSideCoordinate(coordinates.getX(), coordinates.getY(), coordinates.getZ());
	}

	/** Extract the vertical coordinate from the 3 coordinates.
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @return one of the provided coordinates that corresponds to the vertical direction.
	 * @since 18.0
	 */
	@Pure
	public abstract double getVerticalCoordinate(double x, double y, double z);

	/** Extract the vertical coordinate from the 3 coordinates.
	 * 
	 * @param coordinates the coordinates.
	 * @return one of the provided coordinates that corresponds to the vertical direction.
	 * @since 18.0
	 */
	@Pure
	@Inline("getVerticalCoordinate(($1).getX(), ($1).getY(), ($1).getZ())")
	public double getVerticalCoordinate(Tuple3D<?> coordinates) {
		return getVerticalCoordinate(coordinates.getX(), coordinates.getY(), coordinates.getZ());
	}

	/** Change the side coordinate of the given tuple.
	 * 
	 * @param tuple the tuple to change.
	 * @param value the side coordinate to put in the given tuple.
	 * @since 18.0
	 */
	public abstract void setSideCoordinate(Tuple3D<?> tuple, double value);

	/** Change the vertical coordinate of the given tuple.
	 * 
	 * @param tuple the tuple to change.
	 * @param value the vertical coordinate to put in the given tuple.
	 * @since 18.0
	 */
	public abstract void setVerticalCoordinate(Tuple3D<?> tuple, double value);

	/** Add value to the side coordinate of the given tuple.
	 * 
	 * @param tuple the tuple to change.
	 * @param value the side coordinate to put in the given tuple.
	 * @since 18.0
	 */
	public abstract void addSideCoordinate(Tuple3D<?> tuple, double value);

	/** Add value to the vertical coordinate of the given tuple.
	 * 
	 * @param tuple the tuple to change.
	 * @param value the vertical coordinate to put in the given tuple.
	 * @since 18.0
	 */
	public abstract void addVerticalCoordinate(Tuple3D<?> tuple, double value);

}

