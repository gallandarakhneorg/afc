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

package org.arakhne.afc.math.geometry.d2.ai;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D shape with 2d floating coordinates.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Shape2ai<
		ST extends Shape2ai<?, ?, IE, P, V, B>,
		IT extends Shape2ai<?, ?, IE, P, V, B>,
		IE extends PathElement2ai,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2ai<?, ?, IE, P, V, B>>
		extends Shape2D<ST, IT, PathIterator2ai<IE>, P, V, B> {

	/** Replies an iterator on the points covered by the perimeter of this shape.
	 *
	 * <p>The implementation of the iterator depends on the shape type.
	 * There is no warranty about the order of the points.
	 *
	 * @return an iterator on the points that are located at the perimeter of the shape.
	 */
	@Pure
	Iterator<P> getPointIterator();

	@Pure
	@Override
	default boolean contains(Point2D<?, ?> pt) {
		return contains(pt.ix(), pt.iy());
	}

	/** Replies if the given point is inside this shape.
	 *
	 * @param x x coordinate of the point to test.
	 * @param y y coordinate of the point to test.
	 * @return <code>true</code> if the given point is inside this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(int x, int y);

	/** Replies if the given rectangle is inside this shape.
	 *
	 * @param box the rectangle to test.
	 * @return <code>true</code> if the given box is inside the shape.
	 */
	@Pure
	boolean contains(Rectangle2ai<?, ?, ?, ?, ?, ?> box);

	@Pure
	@Override
	default void translate(Vector2D<?, ?> vector) {
		translate(vector.ix(), vector.iy());
	}

	/** Translate the shape.
	 *
	 * @param dx x translation.
	 * @param dy y translation.
	 */
	void translate(int dx, int dy);

	@Pure
	@Override
	default B toBoundingBox() {
		final B box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}

	@Pure
	@Unefficient
	@Override
	default boolean intersects(Shape2D<?, ?, ?, ?, ?, ?> shape) {
		if (shape instanceof Circle2ai) {
			return intersects((Circle2ai<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Path2ai) {
			return intersects((Path2ai<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof PathIterator2ai) {
			return intersects((PathIterator2ai<?>) shape);
		}
		if (shape instanceof Rectangle2ai) {
			return intersects((Rectangle2ai<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Segment2ai) {
			return intersects((Segment2ai<?, ?, ?, ?, ?, ?>) shape);
		}
		return intersects(getPathIterator());
	}

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param rectangle the rectangle.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle);

	/** Replies if this shape is intersecting the given circle.
	 *
	 * @param circle the circle.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Circle2ai<?, ?, ?, ?, ?, ?> circle);

	/** Replies if this shape is intersecting the given segment.
	 *
	 * @param segment the segment.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Segment2ai<?, ?, ?, ?, ?, ?> segment);

	/** Replies if this shape is intersecting the given multishape.
	 *
	 * @param multishape the multishape.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape);

	/** Replies if this shape is intersecting the given path.
	 *
	 * @param path the path.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path2ai<?, ?, ?, ?, ?, ?> path) {
		return intersects(path.getPathIterator());
	}

	/** Replies if this shape is intersecting the path described by the given iterator.
	 *
	 * @param iterator the path iterator.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(PathIterator2ai<?> iterator);

	@Override
	GeomFactory2ai<IE, P, V, B> getGeomFactory();

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return (ST) clone();
		}
		final PathIterator2ai<?> pi = getPathIterator(transform);
		final GeomFactory2ai<IE, P, V, B> factory = getGeomFactory();
		final Path2ai<?, ?, ?, P, V, ?> newPath = factory.newPath(pi.getWindingRule());
		while (pi.hasNext()) {
			final PathElement2ai e = pi.next();
			switch (e.getType()) {
			case MOVE_TO:
				newPath.moveTo(e.getToX(), e.getToY());
				break;
			case LINE_TO:
				newPath.lineTo(e.getToX(), e.getToY());
				break;
			case QUAD_TO:
				newPath.quadTo(e.getCtrlX1(), e.getCtrlY1(), e.getToX(), e.getToY());
				break;
			case CURVE_TO:
				newPath.curveTo(e.getCtrlX1(), e.getCtrlY1(), e.getCtrlX2(), e.getCtrlY2(), e.getToX(), e.getToY());
				break;
			case ARC_TO:
				newPath.arcTo(e.getToX(), e.getToY(), e.getRadiusX(), e.getRadiusY(), e.getRotationX(),
						e.getLargeArcFlag(), e.getSweepFlag());
				break;
			case CLOSE:
				newPath.closePath();
				break;
			default:
			}
		}
		return (ST) newPath;
	}

}
