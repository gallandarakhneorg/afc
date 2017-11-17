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

package org.arakhne.afc.math.geometry.d3;

import java.io.Serializable;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonableObject;

/** 3D shape.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <I> is the type of the iterator used to obtain the elements of the path.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Shape3D<
	ST extends Shape3D<?, ?, I, P, V, B>,
	IT extends Shape3D<?, ?, I, P, V, B>,
	I extends PathIterator3D<?>,
	P extends Point3D<? super P, ? super V>,
	V extends Vector3D<? super V, ? super P>,
	B extends Shape3D<?, ?, I, P, V, B>>
	extends Cloneable, Serializable, JsonableObject {

	/** Replies the geometry factory associated to this point.
	 *
	 * @return the factory.
	 */
	@Pure
	GeomFactory3D<V, P> getGeomFactory();

	/** Replies if this shape is empty.
	 * The semantic associated to the state "empty"
	 * depends on the implemented shape. See the
	 * subclasses for details.
	 *
	 * @return <code>true</code> if the shape is empty;
	 * <code>false</code> otherwise.
	 */
	@Pure
	boolean isEmpty();

	/** Clone this shape.
	 *
	 * @return the clone.
	 */
	@Pure
	IT clone();

	/** Replies this shape as the same path iterator as the given one.
	 *
	 * <p>The equality test does not flatten the paths. It means that
	 * is function has is functionnality equivalent to: <pre><code>
	 * PathIterator2D it = this.getPathIterator();
	 * while (it.hasNext() &amp;&amp; pathIterator.hasNext()) {
	 *   PathElement2D e1 = it.next();
	 *   PathElement2D e2 = it.next();
	 *   if (!e1.equals(e2)) return false;
	 * }
	 * return !it.hasNext() &amp;&amp; !pathIterator.hasNext();
	 * </code></pre>
	 *
	 * @param pathIterator the path iterator to compare to the one of this shape.
	 * @return <code>true</code> if the path iterator of this shape replies the same
	 *     elements as the given path iterator.
	 */
	@Pure
	default boolean equalsToPathIterator(PathIterator3D<?> pathIterator) {
		final I localIterator = getPathIterator();
		if (pathIterator == null) {
			return false;
		}
		PathElement3D element1;
		PathElement3D element2;
		while (localIterator.hasNext() && pathIterator.hasNext()) {
			element1 = localIterator.next();
			element2 = pathIterator.next();
			if (!Objects.equals(element1, element2)) {
				return false;
			}
		}
		return !localIterator.hasNext() && !pathIterator.hasNext();
	}

	/** Replies this shape is equal to the given shape.
	 *
	 * @param shape the shape to compare to.
	 * @return <code>true</code> if this shape is equal is equal to the given path.
	 */
	@Pure
	boolean equalsToShape(IT shape);

	/** Reset this shape to be equivalent to
	 * an just-created instance of this shape type.
	 */
	void clear();

	/** Replies if the given point is inside this shape.
	 *
	 * @param point the point to search for.
	 * @return <code>true</code> if the given shape is intersecting this
	 *     shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(Point3D<?, ?> point);

    /** Replies if this shape is inside the given shape.
     *
     * <p>You must use the containing functions with a specific parameter type in place of
     * this general function. Indeed, the implementation of this function is unefficient due
     * to the tests against the types of the given shape, and the cast operators.
     *
     * @param shape the shape to compare to.
     * @return <code>true</code> if the given shape is inside this shape;
     * <code>false</code> otherwise.
     */
    @Pure
    @Unefficient
    boolean contains(Shape3D<?, ?, ?, ?, ?, ?> shape);

	/** Replies the point on the shape that is closest to the given point.
	 *
	 * @param point the point.
	 * @return the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@Pure
	P getClosestPointTo(Point3D<?, ?> point);

    /** Replies the point on the shape that is closest to the given shape.
     *
     * <p>If the two shapes are intersecting, the replied point is always at the intersection
     * of the two shapes. This function does not enforce the meaning of the replied point
     * in the case of shape intersection. In other words, this function is warranting that
     * the reply point is the either the penetration point, nor a perimeter point, nor any point
     * with a specific meaning.
     *
     * @param shape the shape.
     * @return the closest point on the shape.
     */
    @Pure
    @Unefficient
    P getClosestPointTo(Shape3D<?, ?, ?, ?, ?, ?> shape);

	/** Replies the point on the shape that is farthest the given point.
	 *
	 * @param point the point.
	 * @return the farthest point on the shape.
	 */
	@Pure
	P getFarthestPointTo(Point3D<?, ?> point);

	/** Replies the minimal distance from this shape to the given point.
	 *
	 * @param point the point.
	 * @return the minimal distance between this shape and the point.
	 */
	@Pure
	default double getDistance(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return Math.sqrt(getDistanceSquared(point));
	}

    /** Replies the minimal distance from this shape to the given shape.
     *
     * @param shape the shape.
     * @return the minimal distance between this shape and the given shape.
     */
    @Pure
    @Unefficient
    default double getDistance(Shape3D<?, ?, ?, ?, ?, ?> shape) {
        assert shape != null : AssertMessages.notNullParameter();
        return Math.sqrt(getDistanceSquared(shape));
    }

	/** Replies the squared value of the minimal distance from this shape to the given point.
	 *
	 * @param point the point.
	 * @return squared value of the minimal distance between this shape and the point.
	 */
	@Pure
	double getDistanceSquared(Point3D<?, ?> point);

    /** Replies the squared value of the minimal distance from this shape to the given shape.
     *
     * @param shape the shape.
     * @return squared value of the minimal distance between this shape and the given shape.
     */
    @Pure
    @Unefficient
    double getDistanceSquared(Shape3D<?, ?, ?, ?, ?, ?> shape);

	/**
	 * Computes the L-1 (Manhattan) distance between this shape and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * @param point the point
	 * @return the distance.
	 */
	@Pure
	double getDistanceL1(Point3D<?, ?> point);

	/**
	 * Computes the L-infinite distance between this shape and
	 * point p1.  The L-infinite distance is equal to
	 * MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2)].
	 * @param point the point.
	 * @return the distance.
	 */
	@Pure
	double getDistanceLinf(Point3D<?, ?> point);

	/** Set this shape with the attributes of the given shape.
	 *
	 * @param shape the shape.
	 */
	void set(IT shape);

	/** Replies an iterator on the path elements.
	 *
	 * <p>The iterator for this class is not multi-threaded safe.
	 *
	 * @return an iterator on the path elements.
	 */
	@Pure
	default I getPathIterator() {
		return getPathIterator(null);
	}

	/** Replies the elements of the paths.
	 *
	 * @param transform is the transformation to apply to the path.
	 * @return the elements of the path.
	 */
	@Pure
	I getPathIterator(Transform3D transform);

	/** Apply the transformation to the shape and reply the result.
	 * This function does not change the current shape.
	 *
	 * @param transform is the transformation to apply to the shape.
	 * @return the result of the transformation.
	 */
	@Pure
	ST createTransformedShape(Transform3D transform);

	/** Translate the shape.
	 *
	 * @param vector the translation vector
	 */
	void translate(Vector3D<?, ?> vector);

	/** Replies the bounding box of this shape.
	 *
	 * @return the bounding box of this shape.
	 */
	@Pure
	B toBoundingBox();

	/** Replies the bounds of the shape.
	 *
	 * @param box is set with the bounds of the shape.
	 */
	void toBoundingBox(B box);

	/** Replies if this shape is intersecting the given shape.
	 *
	 * <p>You must use the intersection functions with a specific parameter type in place of
	 * this general function. Indeed, the implementation of this function is unefficient due
	 * to the tests against the types of the given shape, and the cast operators.
	 *
	 * @param shape the shape to compare to
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	@Unefficient
	boolean intersects(Shape3D<?, ?, ?, ?, ?, ?> shape);

	/** Translate this shape by adding the given vector: {@code this += v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #translate(Vector3D)
	 */
	default void operator_add(Vector3D<?, ?> v) {
		translate(v);
	}

	/** Create a new shape by translating this shape of the given vector: {@code this + v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the transformed shape.
	 * @see #translate(Vector3D)
	 */
	@Pure
	default IT operator_plus(Vector3D<?, ?> v) {
		final IT clone = clone();
		clone.translate(v);
		return clone;
	}

	/** Translate this shape by substracting the given vector: {@code this -= v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @see #translate(Vector3D)
	 */
	default void operator_remove(Vector3D<?, ?> v) {
		final Vector3D<?, ?> negate = getGeomFactory().newVector();
		negate.negate(v);
		translate(negate);
	}

	/** Create a new shape by translating this shape of the given vector: {@code this - v}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param v the vector
	 * @return the transformed shape.
	 * @see #translate(Vector3D)
	 */
	@Pure
	default IT operator_minus(Vector3D<?, ?> v) {
		final IT clone = clone();
		final Vector3D<?, ?> negate = getGeomFactory().newVector();
		negate.negate(v);
		clone.translate(negate);
		return clone;
	}

	/** Create a new shape by applying the given transformation: {@code this * t}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param t the transformation
	 * @return the transformed shape.
	 * @see #createTransformedShape(Transform3D)
	 */
	@Pure
	default ST operator_multiply(Transform3D t) {
		return createTransformedShape(t);
	}

	/** Replies if the given point is inside the shape: {@code this && b}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param point the point to test.
	 * @return <code>true</code> if the point is inside the shape. Otherwise, <code>false</code>.
	 * @see #createTransformedShape(Transform3D)
	 */
	@Pure
	default boolean operator_and(Point3D<?, ?> point) {
		return contains(point);
	}

	/** Replies if the given shape has an intersection with this shape: {@code this && b}
	 *
	 * <p>You must use the intersection functions with a specific parameter type in place of
	 * this general function. Indeed, the implementation of this function is unefficient due
	 * to the tests against the types of the given shape, and the cast operators.
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param shape the shape to test.
	 * @return <code>true</code> if the shapes are intersecting. Otherwise, <code>false</code>.
	 * @see #intersects(Shape3D)
	 */
	@Pure
	@Unefficient
	default boolean operator_and(Shape3D<?, ?, ?, ?, ?, ?> shape) {
		return intersects(shape);
	}

	/** Replies the distance between the given point and this shape: {@code this .. p}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param pt the point to test.
	 * @return the distance.
	 * @see #getDistance(Point3D)
	 */
	@Pure
	default double operator_upTo(Point3D<?, ?> pt) {
		return getDistance(pt);
	}
}
