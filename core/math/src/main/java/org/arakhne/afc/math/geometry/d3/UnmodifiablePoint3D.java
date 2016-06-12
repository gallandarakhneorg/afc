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
