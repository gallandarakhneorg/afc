/* 
 * $Id$
 * 
 * Copyright (c) 2016, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports (SET)
 * of Universite de Technologie de Belfort-Montbeliard.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SET.
 *
 * http://www.multiagent.fr/
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
