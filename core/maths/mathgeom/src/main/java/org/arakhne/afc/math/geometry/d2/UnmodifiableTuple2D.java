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

package org.arakhne.afc.math.geometry.d2;

/** Unmodifiable 2D tuple.
 *
 * @param <RT> is the type of data that can be returned by this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface UnmodifiableTuple2D<RT extends Tuple2D<? super RT>> extends Tuple2D<RT> {

	@Override
	default void absolute() {
		throw new UnsupportedOperationException();
	}

	@Override
	default void absolute(Tuple2D<?> tuple)  {
		throw new UnsupportedOperationException();
	}

	@Override
	default void add(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void add(double x, double y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void addX(int x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void addX(double x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void addY(int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void addY(double y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clamp(int min, int max) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clamp(double min, double max) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clamp(int min, int max, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clamp(double min, double max, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clampMin(int min) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clampMin(double min) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clampMin(int min, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clampMin(double min, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clampMax(int max) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clampMax(double max) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clampMax(int max, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clampMax(double max, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void negate(Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void negate() {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scale(int scale, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scale(double scale, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scale(int scale) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scale(double scale) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(double x, double y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(int[] tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(double[] tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setX(int x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setX(double x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setY(int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void setY(double y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void sub(int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void sub(double x, double y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void subX(int x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void subX(double x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void subY(int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void subY(double y) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void interpolate(Tuple2D<?> tuple1, Tuple2D<?> tuple2, double alpha) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void interpolate(Tuple2D<?> tuple, double alpha) {
		throw new UnsupportedOperationException();
	}

}
