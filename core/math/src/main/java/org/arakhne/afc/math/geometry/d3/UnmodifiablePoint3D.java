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

import org.eclipse.xtext.xbase.lib.Pure;

/** Unmodifiable3D Point.
 * 
 * @param <RP> is the type of points that can be returned by this tuple.
 * @param <RV> is the type of vectors that can be returned by this tuple.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface UnmodifiablePoint3D<RP extends Point3D<? super RP, ? super RV>,
		RV extends Vector3D<? super RV, ? super RP>> extends UnmodifiableTuple3D<RP>, Point3D<RP, RV> {
	
	@Override
	default void add(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void add(Vector3D<?, ?> point, Point3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}
	

	@Override
	default void add(Vector3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(int scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(double scale, Vector3D<?, ?> vector, Point3D<?, ?> point) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(int scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(double scale, Point3D<?, ?> point, Vector3D<?, ?> vector) {
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
	default void sub(Point3D<?, ?> point, Vector3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void sub(Vector3D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	default UnmodifiablePoint3D<RP, RV> toUnmodifiable() {
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
