/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.afp;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.d1.Point1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.Shape1D;
import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 2D shape with 2D floating coordinates.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <S> is the type of the segments.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface Shape1afp<
		ST extends Shape1afp<?, ?, P, V, S, B>,
		IT extends Shape1afp<?, ?, P, V, S, B>,
		P extends Point1D<? super P, ? super V, ? super S>,
		V extends Vector1D<? super V, ? super P, ? super S>,
		S extends Segment1D<?, ?>,
		B extends Rectangle1afp<?, ?, P, V, S, B>>
		extends Shape1D<ST, IT, P, V, S, B> {

	@Pure
	@Override
	default boolean contains(Point1D<?, ?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return contains(pt.getSegment(), pt.getX(), pt.getY());
	}

	@Pure
	@Unefficient
	@Override
	default boolean contains(Shape1D<?, ?, ?, ?, ?, ?> shape) {
		assert shape != null : AssertMessages.notNullParameter();
		if (isEmpty()) {
			return false;
		}
		if (shape instanceof Rectangle1afp) {
			return contains((Rectangle1afp<?, ?, ?, ?, ?, ?>) shape);
		}
		return false;
	}

	/** Replies if the given rectangle is inside this shape.
	 *
	 * @param rectangle the rectangle.
	 * @return <code>true</code> if the given rectangle is inside this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(Rectangle1afp<?, ?, ?, ?, ?, ?> rectangle);

	/** Replies if the given point is inside this shape.
	 *
	 * @param segment the segment to test.
	 * @param x x coordinate of the point to test.
	 * @param y y coordinate of the point to test.
	 * @return <code>true</code> if the given point is inside this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(Segment1D<?, ?> segment, double x, double y);

	/** Translate the shape.
	 *
	 * @param dx x translation.
	 * @param dy y translation.
	 */
	void translate(double dx, double dy);

	@Pure
	@Override
	default void translate(Vector1D<?, ?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		translate(vector.getX(), vector.getY());
	}

	@Pure
	@Unefficient
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
	default boolean intersects(Shape1D<?, ?, ?, ?, ?, ?> shape) {
		if (shape instanceof Rectangle1afp) {
			return intersects((Rectangle1afp<?, ?, ?, ?, ?, ?>) shape);
		}
		return false;
	}

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param rectangle the rectangle.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Rectangle1afp<?, ?, ?, ?, ?, ?> rectangle);

	@Pure
	@Unefficient
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
	default double getDistanceSquared(Shape1D<?, ?, ?, ?, ?, ?> shape) {
		if (shape instanceof Rectangle1afp) {
			return getDistanceSquared((Rectangle1afp<?, ?, ?, ?, ?, ?>) shape);
		}
		throw new IllegalArgumentException();
	}

	/** Replies the minimum distance between this shape and the given rectangle.
	 *
	 * @param rectangle the rectangle.
	 * @return the minimum distance between the two shapes.
	 */
	@Pure
	default double getDistanceSquared(Rectangle1afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		return rectangle.getDistanceSquared(getClosestPointTo(rectangle));
	}

	@Pure
	@Unefficient
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
	default P getClosestPointTo(Shape1D<?, ?, ?, ?, ?, ?> shape) {
		if (shape instanceof Rectangle1afp) {
			return getClosestPointTo((Rectangle1afp<?, ?, ?, ?, ?, ?>) shape);
		}
		throw new IllegalArgumentException();
	}

	/** Replies the closest point on this shape to the given rectangle.
	 *
	 * <p>If the two shapes are intersecting, the replied point is always at the intersection
	 * of the two shapes. This function does not enforce the meaning of the replied point
	 * in the case of shape intersection. In other words, this function is warranting that
	 * the reply point is the either the penetration point, nor a perimeter point, nor any point
	 * with a specific meaning.
	 *
	 * @param rectangle the rectangle.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(Rectangle1afp<?, ?, ?, ?, ?, ?> rectangle);

	@Override
	GeomFactory1afp<P, V, S, B> getGeomFactory();

	@Override
	default B toBoundingBox() {
		final B box = getGeomFactory().newBox(getSegment());
		toBoundingBox(box);
		return box;
	}

}
