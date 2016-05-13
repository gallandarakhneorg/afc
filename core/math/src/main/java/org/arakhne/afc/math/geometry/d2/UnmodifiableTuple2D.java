/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
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
	default void clampMin(int min) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clampMin(double min) {
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
	default void clamp(int min, int max, Tuple2D<?> tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void clamp(double min, double max, Tuple2D<?> tuple) {
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
