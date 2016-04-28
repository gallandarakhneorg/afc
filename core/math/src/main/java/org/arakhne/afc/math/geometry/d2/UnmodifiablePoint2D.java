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

import org.eclipse.xtext.xbase.lib.Pure;

/** Unmodifiable2D Point.
 * 
 * @param <RP> is the type of points that can be returned by this tuple.
 * @param <RV> is the type of vectors that can be returned by this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface UnmodifiablePoint2D<RP extends Point2D<? super RP, ? super RV>,
		RV extends Vector2D<? super RV, ? super RP>> extends UnmodifiableTuple2D<RP>, Point2D<RP, RV> {

	@Override
	default void add(Point2D<?, ?> point, Vector2D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void add(Vector2D<?, ?> vector, Point2D<?, ?> point) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void add(Vector2D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(int scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(double scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(int scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(double scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(int scale, Vector2D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void scaleAdd(double scale, Vector2D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void sub(Point2D<?, ?> point, Vector2D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void sub(Vector2D<?, ?> vector) {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	default UnmodifiablePoint2D<RP, RV> toUnmodifiable() {
		return this;
	}

}
