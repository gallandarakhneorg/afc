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

package org.arakhne.afc.math.geometry.d3.afp;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp.CrossingComputationType;

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
public interface Shape3afp<
		ST extends Shape3afp<?, ?, IE, P, V, B>,
		IT extends Shape3afp<?, ?, IE, P, V, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3afp<?, ?, IE, P, V, B>>
		extends Shape3D<ST, IT, PathIterator3afp<IE>, P, V, B> {

	@Pure
	@Override
	default boolean contains(Point3D<?, ?> point) {
		assert point != null : "Point must be not null"; //$NON-NLS-1$
		return contains(point.getX(), point.getY(), point.getZ());
	}

	/** Replies if the given shape is inside this shape.
    *
    * @param shape the shape to be inside.
    * @return <code>true</code> if the given shape is inside this
    *     shape, otherwise <code>false</code>.
    * @since 14.0
    */
	@Pure
	@Unefficient
	default boolean contains(Shape3afp<?, ?, ?, ?, ?, B> shape) {
	    assert shape != null : "Shape must be not null"; //$NON-NLS-1$
        if (isEmpty()) {
            return false;
        }
        if (shape instanceof RectangularPrism3afp) {
            return contains((RectangularPrism3afp<?, ?, ?, ?, ?, B>) shape);
        }
	    final PathIterator3afp<?> iterator = getPathIterator();
        final int crossings;
        if (shape instanceof Sphere3afp) {
            final Sphere3afp<?, ?, ?, ?, ?, B> sphere = (Sphere3afp<?, ?, ?, ?, ?, B>) shape;
            crossings = Path3afp.computeCrossingsFromSphere(
                    0, iterator,
                    sphere.getCenterX(), sphere.getCenterY(), sphere.getCenterZ(), sphere.getRadius(),
                    CrossingComputationType.STANDARD);
        } else if (shape instanceof Segment3afp) {
            final Segment3afp<?, ?, ?, ?, ?, B> segment = (Segment3afp<?, ?, ?, ?, ?, B>) shape;
            crossings = Path3afp.computeCrossingsFromSegment(
                    0, iterator,
                    segment.getX1(), segment.getY1(), segment.getZ1(),
                    segment.getX2(), segment.getY2(), segment.getZ2(),
                    CrossingComputationType.STANDARD);
        } else if (!iterator.isPolygon()) {
            // Only a polygon can contain another shape.
            return false;
        } else {
            final PathIterator3afp<?> shapePathIterator = shape.getPathIterator();
            final B shapeBounds = shape.toBoundingBox();
            crossings = Path3afp.computeCrossingsFromPath(
                    0, iterator,
                    new PathShadow3afp<>(shapePathIterator, shapeBounds),
                    CrossingComputationType.STANDARD);
        }

        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        return crossings != MathConstants.SHAPE_INTERSECTS
                && (crossings & mask) != 0;
	}

	/** Replies if the given rectangular prism is inside this shape.
	 *
	 * @param rectangularPrism the rectangular prism.
	 * @return <code>true</code> if the given rectangle is inside this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(RectangularPrism3afp<?, ?, ?, ?, ?, B> rectangularPrism);

	/** Replies if the given point is inside this shape.
	 *
     * @param x x coordinate of the point to test.
     * @param y y coordinate of the point to test.
     * @param z z coordinate of the point to test.
	 * @return <code>true</code> if the given point is inside this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(double x, double y, double z);

	/** Translate the shape.
	 *
	 * @param dx x translation.
	 * @param dy y translation.
	 * @param dz z translation.
	 */
	void translate(double dx, double dy, double dz);

	@Pure
	@Override
	default void translate(Vector3D<?, ?> vector) {
	    assert vector != null : "Vector must be not null"; //$NON-NLS-1$
	    translate(vector.getX(), vector.getY(), vector.getZ());
	}

	@Pure
	@Unefficient
	@Override
	default boolean intersects(Shape3D<?, ?, ?, ?, ?, ?> shape) {
		if (shape instanceof MultiShape3afp) {
			return intersects((MultiShape3afp<?, ?, ?, ?, ?, ?, ?>) shape);
		}
        if (shape instanceof Sphere3afp) {
			return intersects((Sphere3afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Path3afp) {
			return intersects((Path3afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof PathIterator3afp) {
			return intersects((PathIterator3afp<?>) shape);
		}
		if (shape instanceof RectangularPrism3afp) {
			return intersects((RectangularPrism3afp<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Segment3afp) {
			return intersects((Segment3afp<?, ?, ?, ?, ?, ?>) shape);
		}
		return intersects(getPathIterator());
	}


	/** Replies if this shape is intersecting the given circle.
	 *
	 * @param sphere the sphere
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere);

	/** Replies if this shape is intersecting the given Prism.
	 *
	 * @param prism the prism
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Prism3afp<?, ?, ?, ?, ?, ?> prism);

	/** Replies if this shape is intersecting the given line.
	 *
	 * @param segment the segment
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?> segment);


	/** Replies if this shape is intersecting the given path.
	 *
	 * @param path the path
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : "Path must be not null"; //$NON-NLS-1$
		return intersects(path.getPathIterator());
	}

	/** Replies if this shape is intersecting the shape representing the given path iterator.
	 *
	 * @param iterator the iterator
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(PathIterator3afp<?> iterator);

	/** Replies if this shape is intersecting the given multishape.
	 *
	 * @param multishape the multishape
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape);

	@Override
	GeomFactory3afp<IE, P, V, B> getGeomFactory();

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return (ST) clone();
		}
		final PathIterator3afp<?> pi = getPathIterator(transform);
		final GeomFactory3afp<IE, P, V, B> factory = getGeomFactory();
		final Path3afp<?, ?, ?, P, V, ?> newPath = factory.newPath(pi.getWindingRule());
		PathElement3afp element;
		while (pi.hasNext()) {
			element = pi.next();
            switch (element.getType()) {
			case MOVE_TO:
				newPath.moveTo(element.getToX(), element.getToY(), element.getToZ());
				break;
			case LINE_TO:
				newPath.lineTo(element.getToX(), element.getToY(), element.getToZ());
				break;
			case QUAD_TO:
                newPath.quadTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getToX(), element.getToY(),
                        element.getToZ());
				break;
			case CURVE_TO:
                newPath.curveTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getCtrlX2(),
                        element.getCtrlY2(), element.getCtrlZ2(), element.getToX(), element.getToY(), element.getToZ());
				break;
			case CLOSE:
				newPath.closePath();
				break;
            case ARC_TO:
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
