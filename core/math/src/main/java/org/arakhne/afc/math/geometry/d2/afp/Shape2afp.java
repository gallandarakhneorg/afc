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

package org.arakhne.afc.math.geometry.d2.afp;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp.CrossingComputationType;

/** 2D shape with 2D floating coordinates.
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
public interface Shape2afp<
		ST extends Shape2afp<?, ?, IE, P, V, B>,
		IT extends Shape2afp<?, ?, IE, P, V, B>,
		IE extends PathElement2afp,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, IE, P, V, B>>
		extends Shape2D<ST, IT, PathIterator2afp<IE>, P, V, B> {

	@Pure
	@Override
	default boolean contains(Point2D<?, ?> pt) {
		assert pt != null : "Point must be not null"; //$NON-NLS-1$
		return contains(pt.getX(), pt.getY());
	}

	/** Replies if the given shape is inside this shape.
	 *
	 * @param shape the shape to be inside.
	 * @return <code>true</code> if the given shape is inside this
	 *     shape, otherwise <code>false</code>.
	 * @since 13.0
	 */
	@Pure
	@Unefficient
	default boolean contains(Shape2afp<?, ?, ?, ?, ?, B> shape) {
		assert shape != null : "Shape must be not null"; //$NON-NLS-1$
		if (isEmpty()) {
			return false;
		}
		if (shape instanceof Rectangle2afp) {
			return contains((Rectangle2afp<?, ?, ?, ?, ?, B>) shape);
		}
		final PathIterator2afp<?> iterator = getPathIterator();
		final int crossings;
		if (shape instanceof Circle2afp) {
			final Circle2afp<?, ?, ?, ?, ?, B> circle = (Circle2afp<?, ?, ?, ?, ?, B>) shape;
			crossings = Path2afp.computeCrossingsFromCircle(
					0, iterator,
					circle.getCenterX(), circle.getCenterY(), circle.getRadius(),
					CrossingComputationType.STANDARD);
		} else if (shape instanceof Ellipse2afp) {
			final Ellipse2afp<?, ?, ?, ?, ?, B> ellipse = (Ellipse2afp<?, ?, ?, ?, ?, B>) shape;
			crossings = Path2afp.computeCrossingsFromEllipse(
					0, iterator,
					ellipse.getMinX(), ellipse.getMinY(), ellipse.getWidth(), ellipse.getHeight(),
					CrossingComputationType.STANDARD);
		} else if (shape instanceof RoundRectangle2afp) {
			final RoundRectangle2afp<?, ?, ?, ?, ?, B> roundRectangle = (RoundRectangle2afp<?, ?, ?, ?, ?, B>) shape;
			crossings = Path2afp.computeCrossingsFromRoundRect(
					0, iterator,
					roundRectangle.getMinX(), roundRectangle.getMinY(),
					roundRectangle.getMaxX(), roundRectangle.getMaxY(),
					roundRectangle.getArcWidth(), roundRectangle.getArcHeight(),
					CrossingComputationType.STANDARD);
		} else if (shape instanceof Segment2afp) {
			final Segment2afp<?, ?, ?, ?, ?, B> segment = (Segment2afp<?, ?, ?, ?, ?, B>) shape;
			crossings = Path2afp.computeCrossingsFromSegment(
					0, iterator,
					segment.getX1(), segment.getY1(),
					segment.getX2(), segment.getY2(),
					CrossingComputationType.STANDARD);
		} else if (shape instanceof Triangle2afp) {
			final Triangle2afp<?, ?, ?, ?, ?, B> triangle = (Triangle2afp<?, ?, ?, ?, ?, B>) shape;
			crossings = Path2afp.computeCrossingsFromTriangle(
					0, iterator,
					triangle.getX1(), triangle.getY1(),
					triangle.getX2(), triangle.getY2(),
					triangle.getX3(), triangle.getY3(),
					CrossingComputationType.STANDARD);
		} else {
			// General case: use path iterator
			final B shapeBounds = shape.toBoundingBox();
			final PathIterator2afp<?> shapePathIterator = shape.getPathIterator();
			crossings = Path2afp.computeCrossingsFromPath(
					0, iterator,
					new PathShadow2afp<>(shapePathIterator, shapeBounds),
					CrossingComputationType.STANDARD);
		}
		
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		return crossings != MathConstants.SHAPE_INTERSECTS
				&& (crossings & mask) != 0;
	}

	/** Replies if the given rectangle is inside this shape.
	 *
	 * @param rectangle the rectangle.
	 * @return <code>true</code> if the given rectangle is inside this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(Rectangle2afp<?, ?, ?, ?, ?, B> rectangle);

	/** Replies if the given point is inside this shape.
	 *
	 * @param x x coordinate of the point to test.
	 * @param y y coordinate of the point to test.
	 * @return <code>true</code> if the given point is inside this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(double x, double y);

	/** Translate the shape.
	 *
	 * @param dx x translation.
	 * @param dy y translation.
	 */
	void translate(double dx, double dy);

	@Pure
	@Override
	default void translate(Vector2D<?, ?> vector) {
		assert vector != null : "Vector must be not null"; //$NON-NLS-1$
		translate(vector.getX(), vector.getY());
	}

	@Pure
	@Unefficient
	@Override
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
	default boolean intersects(Shape2D<?, ?, ?, ?, ?, ?> shape) {
		if (shape instanceof Circle2afp) {
			return intersects((Circle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Ellipse2afp) {
			return intersects((Ellipse2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof MultiShape2afp) {
			return intersects((MultiShape2afp<?, ?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof OrientedRectangle2afp) {
			return intersects((OrientedRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Parallelogram2afp) {
			return intersects((Parallelogram2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Path2afp) {
			return intersects((Path2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof PathIterator2afp) {
			return intersects((PathIterator2afp<?>) shape);
		}
		if (shape instanceof Rectangle2afp) {
			return intersects((Rectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof RoundRectangle2afp) {
			return intersects((RoundRectangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Segment2afp) {
			return intersects((Segment2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Triangle2afp) {
			return intersects((Triangle2afp<?, ?, ?, ?, ?, ?>) shape);
		}
		return intersects(getPathIterator());
	}

	/** Replies if this shape is intersecting the given ellipse.
	 *
	 * @param ellipse the ellipse.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse);

	/** Replies if this shape is intersecting the given circle.
	 *
	 * @param circle the circle.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle);

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param rectangle the rectangle.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle);

	/** Replies if this shape is intersecting the given line.
	 *
	 * @param segment the segment.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment);

	/** Replies if this shape is intersecting the given triangle.
	 *
	 * @param triangle the triangle.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle);

	/** Replies if this shape is intersecting the given path.
	 *
	 * @param path the other path.
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path2afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : "Path must be not null"; //$NON-NLS-1$
		return intersects(path.getPathIterator());
	}

	/** Replies if this shape is intersecting the shape representing the given path iterator.
	 *
	 * @param iterator the path iterator.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(PathIterator2afp<?> iterator);

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param orientedRectangle the oriented rectangle.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle);

	/** Replies if this shape is intersecting the given parallelogram.
	 *
	 * @param parallelogram the parallelogram.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram);

	/** Replies if this shape is intersecting the given rectangle.
	 *
	 * @param roundRectangle the round rectangle.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle);

	/** Replies if this shape is intersecting the given multishape.
	 *
	 * @param multishape the multishape.
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape);

	@Override
	GeomFactory2afp<IE, P, V, B> getGeomFactory();

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return (ST) clone();
		}
		final PathIterator2afp<?> pi = getPathIterator(transform);
		final GeomFactory2afp<IE, P, V, B> factory = getGeomFactory();
		final Path2afp<?, ?, ?, P, V, ?> newPath = factory.newPath(pi.getWindingRule());
		while (pi.hasNext()) {
			final PathElement2afp e = pi.next();
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

	@Override
	default B toBoundingBox() {
		final B box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}

}
