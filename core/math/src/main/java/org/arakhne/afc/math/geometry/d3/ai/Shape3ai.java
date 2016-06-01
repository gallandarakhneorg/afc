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

package org.arakhne.afc.math.geometry.d3.ai;

import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** 3D shape with 3 integer coordinates.
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
public interface Shape3ai<
		ST extends Shape3ai<?, ?, IE, P, V, B>,
		IT extends Shape3ai<?, ?, IE, P, V, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, IE, P, V, B>>
		extends Shape3D<ST, IT, PathIterator3ai<IE>, P, V, B> {

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
	default boolean contains(Point3D<?, ?> point) {
		return contains(point.ix(), point.iy(), point.iz());
	}

	/** Replies if the given point is inside this shape.
	 *
	 * @param x x coordinate of the point to test.
	 * @param y y coordinate of the point to test.
	 * @param z z coordinate of the point to test.
	 * @return <code>true</code> if the given point is inside this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure boolean contains(int x, int y, int z);

	/** Replies if the given rectangle is inside this shape.
	 *
	 * @param box the rectangle to test.
	 * @return <code>true</code> if the given box is inside the shape.
	 */
	@Pure boolean contains(RectangularPrism3ai<?, ?, ?, ?, ?, ?> box);

	@Pure
	@Override
	default void translate(Vector3D<?, ?> vector) {
		translate(vector.ix(), vector.iy(), vector.iz());
	}

    /** Translate the shape.
     *
     * @param dx x translation.
     * @param dy y translation.
     * @param dz z translation.
     */
    void translate(int dx, int dy, int dz);

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
	default boolean intersects(Shape3D<?, ?, ?, ?, ?, ?> shape) {
		if (shape instanceof Sphere3ai) {
			return intersects((Sphere3ai<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Path3ai) {
			return intersects((Path3ai<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof PathIterator3ai) {
			return intersects((PathIterator3ai<?>) shape);
		}
		if (shape instanceof RectangularPrism3ai) {
			return intersects((RectangularPrism3ai<?, ?, ?, ?, ?, ?>) shape);
		}
		if (shape instanceof Segment3ai) {
			return intersects((Segment3ai<?, ?, ?, ?, ?, ?>) shape);
		}
		return intersects(getPathIterator());
	}


    /** Replies if this shape is intersecting the given rectangular prism.
     *
     * @param rectangularPrism the rectangular prism.
     * @return <code>true</code> if this shape is intersecting the given shape;
     * <code>false</code> if there is no intersection.
     */
    @Pure boolean intersects(RectangularPrism3ai<?, ?, ?, ?, ?, ?> rectangularPrism);

	/** Replies if this shape is intersecting the given sphere.
	 *
	 * @param sphere the sphere
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure boolean intersects(Sphere3ai<?, ?, ?, ?, ?, ?> sphere);

	/** Replies if this shape is intersecting the given segment.
	 *
	 * @param segment the segment
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure boolean intersects(Segment3ai<?, ?, ?, ?, ?, ?> segment);

	/** Replies if this shape is intersecting the given multishape.
	 *
	 * @param multishape the ùmultishape
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure boolean intersects(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> multishape);

	/** Replies if this shape is intersecting the given path.
	 *
	 * @param path the path
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path3ai<?, ?, ?, ?, ?, ?> path) {
		return intersects(path.getPathIterator(/*MathConstants.SPLINE_APPROXIMATION_RATIO*/));
	}

	/** Replies if this shape is intersecting the path described by the given iterator.
	 *
	 * @param pathIterator the path Iterator
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure boolean intersects(PathIterator3ai<?> pathIterator);

	@Override
	GeomFactory3ai<IE, P, V, B> getGeomFactory();

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return (ST) clone();
		}
		final PathIterator3ai<?> pi = getPathIterator(transform);
		final GeomFactory3ai<IE, P, V, B> factory = getGeomFactory();
		final Path3ai<?, ?, ?, P, V, ?> newPath = factory.newPath(pi.getWindingRule());
		PathElement3ai element;
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

}
