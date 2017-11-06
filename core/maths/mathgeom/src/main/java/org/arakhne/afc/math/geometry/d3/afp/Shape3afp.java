/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

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
		assert point != null : AssertMessages.notNullParameter();
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
	@Override
	default boolean contains(Shape3D<?, ?, ?, ?, ?, ?> shape) {
	    assert shape != null : AssertMessages.notNullParameter();
        if (isEmpty()) {
            return false;
        }
        if (shape instanceof RectangularPrism3afp) {
            return contains((RectangularPrism3afp<?, ?, ?, ?, ?, ?>) shape);
        }
	    final PathIterator3afp<?> iterator = getPathIterator();
        final int crossings;
        if (shape instanceof Sphere3afp) {
            final Sphere3afp<?, ?, ?, ?, ?, ?> sphere = (Sphere3afp<?, ?, ?, ?, ?, ?>) shape;
            crossings = Path3afp.computeCrossingsFromSphere(
                    0, iterator,
                    sphere.getCenterX(), sphere.getCenterY(), sphere.getCenterZ(), sphere.getRadius(),
                    CrossingComputationType.STANDARD);
        } else if (shape instanceof Segment3afp) {
            final Segment3afp<?, ?, ?, ?, ?, ?> segment = (Segment3afp<?, ?, ?, ?, ?, ?>) shape;
            crossings = Path3afp.computeCrossingsFromSegment(
                    0, iterator,
                    segment.getX1(), segment.getY1(), segment.getZ1(),
                    segment.getX2(), segment.getY2(), segment.getZ2(),
                    CrossingComputationType.STANDARD);
        } else if (!iterator.isPolygon()) {
            // Only a polygon can contain another shape.
            return false;
        } else {
            final double minX;
            final double minY;
            final double minZ;
            final double maxX;
            final double maxY;
            final double maxZ;
            final Shape3D<?, ?, ?, ?, ?, ?> originalBounds = shape.toBoundingBox();
            if (originalBounds instanceof RectangularPrism3afp) {
                final RectangularPrism3afp<?, ?, ?, ?, ?, ?> rect = (RectangularPrism3afp<?, ?, ?, ?, ?, ?>) originalBounds;
                minX = rect.getMinX();
                minY = rect.getMinY();
                minZ = rect.getMinZ();
                maxX = rect.getMaxX();
                maxY = rect.getMaxY();
                maxZ = rect.getMaxZ();
            } else {
                assert originalBounds instanceof RectangularPrism3ai;
                final RectangularPrism3ai<?, ?, ?, ?, ?, ?> rect = (RectangularPrism3ai<?, ?, ?, ?, ?, ?>) originalBounds;
                minX = rect.getMinX();
                minY = rect.getMinY();
                minZ = rect.getMinZ();
                maxX = rect.getMaxX();
                maxY = rect.getMaxY();
                maxZ = rect.getMaxZ();
            }
            final PathIterator3afp<?> shapePathIterator = iterator.getGeomFactory().convert(shape.getPathIterator());
            crossings = Path3afp.computeCrossingsFromPath(
                    0, iterator,
                    new BasicPathShadow3afp(shapePathIterator, minX, minY, minZ, maxX, maxY, maxZ),
                    CrossingComputationType.STANDARD);
        }

        final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        return crossings != GeomConstants.SHAPE_INTERSECTS
                && (crossings & mask) != 0;
	}

	/** Replies if the given rectangular prism is inside this shape.
	 *
	 * @param rectangularPrism the rectangular prism.
	 * @return <code>true</code> if the given rectangle is inside this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangularPrism);

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
	    assert vector != null : AssertMessages.notNullParameter();
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
	boolean intersects(RectangularPrism3afp<?, ?, ?, ?, ?, ?> prism);

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
		assert path != null : AssertMessages.notNullParameter();
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

    @Pure
    @Unefficient
    @Override
    @SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
    default double getDistanceSquared(Shape3D<?, ?, ?, ?, ?, ?> shape) {
        if (shape instanceof Sphere3afp) {
            return getDistanceSquared((Sphere3afp<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof MultiShape3afp) {
            return getDistanceSquared((MultiShape3afp<?, ?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Path3afp) {
            return getDistanceSquared((Path3afp<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof RectangularPrism3afp) {
            return getDistanceSquared((RectangularPrism3afp<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Segment3afp) {
            return getDistanceSquared((Segment3afp<?, ?, ?, ?, ?, ?>) shape);
        }
        throw new IllegalArgumentException();
    }

    /** Replies the minimum distance between this shape and the given sphere.
     *
     * @param sphere the sphere.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
        assert sphere != null : AssertMessages.notNullParameter();
        return sphere.getDistanceSquared(getClosestPointTo(sphere));
    }

    /** Replies the minimum distance between this shape and the given rectangular prism.
     *
     * @param rectangularPrism the rectangular prism.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangularPrism) {
        assert rectangularPrism != null : AssertMessages.notNullParameter();
        return rectangularPrism.getDistanceSquared(getClosestPointTo(rectangularPrism));
    }

    /** Replies the minimum distance between this shape and the given segment.
     *
     * @param segment the segment.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        return segment.getDistanceSquared(getClosestPointTo(segment));
    }

    /** Replies the minimum distance between this shape and the given path.
     *
     * @param path the path.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(Path3afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        return path.getDistanceSquared(getClosestPointTo(path));
    }

    /** Replies the minimum distance between this shape and the given multishape.
     *
     * @param multishape the multishape.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        double minDist = Double.POSITIVE_INFINITY;
        double dist;
        for (final Shape3afp<?, ?, ?, ?, ?, ?> shape : multishape) {
            dist = getDistanceSquared(shape);
            if (dist < minDist) {
                minDist = dist;
            }
        }
        return minDist;
    }

    @Pure
    @Unefficient
    @Override
    @SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
    default P getClosestPointTo(Shape3D<?, ?, ?, ?, ?, ?> shape) {
        if (shape instanceof Sphere3afp) {
            return getClosestPointTo((Sphere3afp<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof MultiShape3afp) {
            return getClosestPointTo((MultiShape3afp<?, ?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Path3afp) {
            return getClosestPointTo((Path3afp<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof RectangularPrism3afp) {
            return getClosestPointTo((RectangularPrism3afp<?, ?, ?, ?, ?, ?>) shape);
        }
        if (shape instanceof Segment3afp) {
            return getClosestPointTo((Segment3afp<?, ?, ?, ?, ?, ?>) shape);
        }
        throw new IllegalArgumentException();
    }

    /** Replies the closest point on this shape to the given sphere.
     *
     * <p>If the two shapes are intersecting, the replied point is always at the intersection
     * of the two shapes. This function does not enforce the meaning of the replied point
     * in the case of shape intersection. In other words, this function is warranting that
     * the reply point is the either the penetration point, nor a perimeter point, nor any point
     * with a specific meaning.
     *
     * @param sphere the sphere.
     * @return the closest point on the shape; or the point itself
     *     if it is inside the shape.
     */
    @Pure
    @Unefficient
    P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere);

    /** Replies the closest point on this shape to the given rectangular prism.
     *
     * <p>If the two shapes are intersecting, the replied point is always at the intersection
     * of the two shapes. This function does not enforce the meaning of the replied point
     * in the case of shape intersection. In other words, this function is warranting that
     * the reply point is the either the penetration point, nor a perimeter point, nor any point
     * with a specific meaning.
     *
     * @param rectangularPrism the rectangular prism.
     * @return the closest point on the shape; or the point itself
     *     if it is inside the shape.
     */
    @Pure
    P getClosestPointTo(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangularPrism);

    /** Replies the closest point on this shape to the given segment.
     *
     * <p>If the two shapes are intersecting, the replied point is always at the intersection
     * of the two shapes. This function does not enforce the meaning of the replied point
     * in the case of shape intersection. In other words, this function is warranting that
     * the reply point is the either the penetration point, nor a perimeter point, nor any point
     * with a specific meaning.
     *
     * @param segment the segment.
     * @return the closest point on the shape; or the point itself
     *     if it is inside the shape.
     */
    @Pure
    P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?> segment);

    /** Replies the closest point on this shape to the given path.
     *
     * <p>If the two shapes are intersecting, the replied point is always at the intersection
     * of the two shapes. This function does not enforce the meaning of the replied point
     * in the case of shape intersection. In other words, this function is warranting that
     * the reply point is the either the penetration point, nor a perimeter point, nor any point
     * with a specific meaning.
     *
     * @param path the path.
     * @return the closest point on the shape; or the point itself
     *     if it is inside the shape.
     */
    @Pure
    P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path);

    /** Replies the closest point on this shape to the given multishape.
     *
     * <p>If the two shapes are intersecting, the replied point is always at the intersection
     * of the two shapes. This function does not enforce the meaning of the replied point
     * in the case of shape intersection. In other words, this function is warranting that
     * the reply point is the either the penetration point, nor a perimeter point, nor any point
     * with a specific meaning.
     *
     * @param multishape the multishape.
     * @return the closest point on the shape; or the point itself
     *     if it is inside the shape.
     */
    @Pure
    default P getClosestPointTo(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        Shape3afp<?, ?, ?, ?, ?, ?> closest = null;
        double minDist = Double.POSITIVE_INFINITY;
        double dist;
        for (final Shape3afp<?, ?, ?, ?, ?, ?> shape : multishape) {
            dist = getDistanceSquared(shape);
            if (dist < minDist) {
                minDist = dist;
                closest = shape;
            }
        }
        if (closest == null) {
            return getGeomFactory().newPoint();
        }
        return getClosestPointTo(closest);
    }

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
				//$CASES-OMITTED$
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
