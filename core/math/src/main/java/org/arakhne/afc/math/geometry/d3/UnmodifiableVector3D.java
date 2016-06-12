/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

/** Unmodifiable 3D Vector.
 *
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RP> is the type of point that can be returned by this tuple.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface UnmodifiableVector3D<RV extends Vector3D<? super RV, ? super RP>,
	RP extends Point3D<? super RP, ? super RV>> extends UnmodifiableTuple3D<RV>, Vector3D<RV, RP> {
	@Override
	default void add(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void add(Vector3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(int scale, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(double scale, Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(int scale, Vector3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(double scale, Vector3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void sub(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void sub(Point3D<?, ?> point1, Point3D<?, ?> point2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void sub(Vector3D<?, ?> vector1) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void cross(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void crossLeftHand(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void crossRightHand(Vector3D<?, ?> vector1, Vector3D<?, ?> vector2) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void normalize(Vector3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void normalize() {
		throw new UnsupportedOperationException();
	}

	@Override
	default void turnVector(Vector3D<?, ?> axis, double angle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setLength(double newLength) {
		throw new UnsupportedOperationException();
	}

	@Override
	default UnmodifiableVector3D<RV, RP> toUnmodifiable() {
		return this;
	}

	@Override
	default void operator_add(Vector3D<?, ?> v) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void operator_remove(Vector3D<?, ?> v) {
		throw new UnsupportedOperationException();
	}
}
