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

package org.arakhne.afc.math.geometry.d3;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;

/** Unmodifiable quaternion.
 *
 * @param <RP> is the type of point that can be returned by this quaternion.
 * @param <RV> is the type of vector that can be returned by this quaternion.
 * @param <RQ> is the type of quaternion that can be returned by this quaternion.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public interface UnmodifiableQuaternion<RP extends Point3D<? super RP, ? super RV, ? super RQ>, RV extends Vector3D<? super RV, ? super RP, ? super RQ>, RQ extends Quaternion<? super RP, ? super RV, ? super RQ>> extends Quaternion<RP, RV, RQ> {

	@Override
	default void setX(double x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setX(int x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setY(double y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setY(int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setZ(double z) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setZ(int z) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setW(double w) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setW(int w) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void conjugate(Quaternion<?, ?, ?> q1) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void conjugate() {
		throw new UnsupportedOperationException();
	}

	@Override
	default void mul(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void mul(Quaternion<?, ?, ?> q1) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void mulInverse(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void mulInverse(Quaternion<?, ?, ?> q1) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void inverse(Quaternion<?, ?, ?> q1) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void inverse() {
		throw new UnsupportedOperationException();
	}

	@Override
	default void normalize(Quaternion<?, ?, ?> q1) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void normalize() {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(double x, double y, double z, double w) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(Quaternion<?, ?, ?> quat) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setAxisAngle(Vector3D<?, ?, ?> axis, double angle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setAxisAngle(double x1, double y1, double z1, double angle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setAxisAngle(AxisAngle axisangle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void interpolate(Quaternion<?, ?, ?> q1, double alpha) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void interpolate(Quaternion<?, ?, ?> q1, Quaternion<?, ?, ?> q2, double alpha) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setEulerAngles(EulerAngles angles) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setEulerAngles(double attitude, double bank, double heading) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setEulerAngles(double attitude, double bank, double heading, CoordinateSystem3D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	default UnmodifiableQuaternion<RP, RV, RQ> toUnmodifiable() {
		return this;
	}

}
