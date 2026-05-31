/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D shape with 2D floating coordinates.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Shape3afp<
		IT extends Shape3afp<?, IE, P, V, Q, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends AlignedBox3afp<?, IE, P, V, Q, B>>
		extends Shape3D<IT, PathIterator3afp<IE>, P, V, Q, B> {

	@Pure
	@Override
	default boolean contains(Point3D<?, ?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return contains(point.getX(), point.getY(), point.getZ());
	}

	/** Replies if the given rectangular prism is inside this shape.
	 *
	 * @param AlignedBox the rectangular prism.
	 * @return {@code true} if the given rectangle is inside this
	 *     shape, otherwise {@code false}.
	 */
	@Pure
	boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox);

	/** Replies if the given point is inside this shape.
	 *
     * @param x x coordinate of the point to test.
     * @param y y coordinate of the point to test.
     * @param z z coordinate of the point to test.
	 * @return {@code true} if the given point is inside this
	 *     shape, otherwise {@code false}.
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
	default void translate(Vector3D<?, ?, ?> vector) {
	    assert vector != null : AssertMessages.notNullParameter();
	    translate(vector.getX(), vector.getY(), vector.getZ());
	}

	@Pure
	@Unefficient
	@Override
	default boolean intersects(Shape3D<?, ?, ?, ?, ?, ?> shape) {
    	assert shape != null : AssertMessages.notNullParameter();
    	final var type = shape.getType();
    	assert type != null;
    	assert type.getPreferredContinuousShapeType() != null;
    	assert type.getPreferredContinuousShapeType().isInstance(shape);
    	switch (type) {
		case ALIGNED_BOX:
			return intersects((AlignedBox3afp<?, ?, ?, ?, ?, ?>) shape);
		case MULTISHAPE:
			return intersects((MultiShape3afp<?, ?, ?, ?, ?, ?, ?>) shape);
		case PATH:
			return intersects((Path3afp<?, ?, ?, ?, ?, ?>) shape);
		case SEGMENT:
			return intersects((Segment3afp<?, ?, ?, ?, ?, ?, ?>) shape);
		case SPHERE:
			return intersects((Sphere3afp<?, ?, ?, ?, ?, ?>) shape);
		default:
			break;
    	}
    	throw new IllegalArgumentException();
	}


	/** Replies if this shape is intersecting the given circle.
	 *
	 * @param sphere the sphere
	 * @return {@code true} if this shape is intersecting the given shape;
	 *      {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere);

	/** Replies if this shape is intersecting the given aligned box.
	 *
	 * @param prism the prism
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism);

	/** Replies if this shape is intersecting the given line.
	 *
	 * @param segment the segment
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment);


	/** Replies if this shape is intersecting the given path.
	 *
	 * @param path the path
	 * @return {@code true} if this shape is intersecting the given path;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		return intersects(path.getPathIterator());
	}

	/** Replies if this shape is intersecting the shape representing the given path iterator.
	 *
	 * @param iterator the iterator
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(PathIterator3afp<?> iterator);

	/** Replies if this shape is intersecting the given multishape.
	 *
	 * @param multishape the multishape
	 * @return {@code true} if this shape is intersecting the given shape;
	 *     {@code false} if there is no intersection.
	 */
	@Pure
	boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape);

    @Pure
    @Unefficient
    @Override
    default double getDistanceSquared(Shape3D<?, ?, ?, ?, ?, ?> shape) {
    	assert shape != null : AssertMessages.notNullParameter();
    	final var type = shape.getType();
    	assert type != null;
    	assert type.getPreferredContinuousShapeType() != null;
    	assert type.getPreferredContinuousShapeType().isInstance(shape);
    	switch (type) {
		case ALIGNED_BOX:
            return getDistanceSquared((AlignedBox3afp<?, ?, ?, ?, ?, ?>) shape);
		case MULTISHAPE:
            return getDistanceSquared((MultiShape3afp<?, ?, ?, ?, ?, ?, ?>) shape);
		case PATH:
            return getDistanceSquared((Path3afp<?, ?, ?, ?, ?, ?>) shape);
		case SEGMENT:
            return getDistanceSquared((Segment3afp<?, ?, ?, ?, ?, ?, ?>) shape);
		case SPHERE:
            return getDistanceSquared((Sphere3afp<?, ?, ?, ?, ?, ?>) shape);
		default:
			break;
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
     * @param AlignedBox the rectangular prism.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox) {
        assert AlignedBox != null : AssertMessages.notNullParameter();
        return AlignedBox.getDistanceSquared(getClosestPointTo(AlignedBox));
    }

    /** Replies the minimum distance between this shape and the given segment.
     *
     * @param segment the segment.
     * @return the minimum distance between the two shapes.
     */
    @Pure
    default double getDistanceSquared(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
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
        var minDist = Double.POSITIVE_INFINITY;
        for (final var shape : multishape) {
            final var dist = getDistanceSquared(shape);
            if (dist < minDist) {
                minDist = dist;
            }
        }
        return minDist;
    }

    @Pure
    @Unefficient
    @Override
    default P getClosestPointTo(Shape3D<?, ?, ?, ?, ?, ?> shape) {
    	assert shape != null : AssertMessages.notNullParameter();
    	final var type = shape.getType();
    	assert type != null;
    	assert type.getPreferredContinuousShapeType() != null;
    	assert type.getPreferredContinuousShapeType().isInstance(shape);
    	switch (type) {
		case ALIGNED_BOX:
            return getClosestPointTo((AlignedBox3afp<?, ?, ?, ?, ?, ?>) shape);
		case MULTISHAPE:
            return getClosestPointTo((MultiShape3afp<?, ?, ?, ?, ?, ?, ?>) shape);
		case PATH:
            return getClosestPointTo((Path3afp<?, ?, ?, ?, ?, ?>) shape);
		case SEGMENT:
            return getClosestPointTo((Segment3afp<?, ?, ?, ?, ?, ?, ?>) shape);
		case SPHERE:
            return getClosestPointTo((Sphere3afp<?, ?, ?, ?, ?, ?>) shape);
		default:
			break;
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
     * @param AlignedBox the rectangular prism.
     * @return the closest point on the shape; or the point itself
     *     if it is inside the shape.
     */
    @Pure
    P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox);

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
    P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment);

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
        var minDist = Double.POSITIVE_INFINITY;
        for (final var shape : multishape) {
            final var dist = getDistanceSquared(shape);
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
	GeomFactory3afp<IE, P, V, Q, B> getGeomFactory();

	@Override
	default B toBoundingBox() {
		final var box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}

}
