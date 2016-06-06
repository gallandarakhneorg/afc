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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.MultiShape2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Container for grouping of shapes.
 *
 * <p>The coordinates of the shapes inside the multishape are global. They are not relative to the multishape.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this multishape.
 * @param <CT> is the type of the shapes that are inside this multishape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: tpiotrowski$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface MultiShape2afp<
		ST extends Shape2afp<?, ?, IE, P, V, B>,
		IT extends MultiShape2afp<?, ?, CT, IE, P, V, B>,
		CT extends Shape2afp<?, ?, IE, P, V, B>,
		IE extends PathElement2afp,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, IE, P, V, B>>
		extends Shape2afp<ST, IT, IE, P, V, B>,
		MultiShape2D<ST, IT, CT, PathIterator2afp<IE>, P, V, B> {

	@Pure
	@Override
	default boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		assert ellipse != null : AssertMessages.notNullParameter();
		if (ellipse.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(ellipse)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : AssertMessages.notNullParameter();
		if (circle.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(circle)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		if (rectangle.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(rectangle)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		if (segment.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(segment)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : AssertMessages.notNullParameter();
		if (triangle.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(triangle)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(PathIterator2afp<?> iterator) {
		if (toBoundingBox().intersects(iterator)) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(iterator.restartIterations())) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
		assert orientedRectangle != null : AssertMessages.notNullParameter();
		if (orientedRectangle.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(orientedRectangle)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert parallelogram != null : AssertMessages.notNullParameter();
		if (parallelogram.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(parallelogram)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		assert roundRectangle != null : AssertMessages.notNullParameter();
		if (roundRectangle.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(roundRectangle)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	@Unefficient
	default boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		if (multishape.toBoundingBox().intersects(toBoundingBox())) {
			for (final CT shape1 : getBackendDataList()) {
				for (final Shape2afp<?, ?, ?, ?, ?, ?> shape2 : multishape.getBackendDataList()) {
					if (shape1.intersects(shape2)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean contains(double x, double y) {
		if (toBoundingBox().contains(x, y)) {
			for (final CT shape : getBackendDataList()) {
				if (shape.contains(x, y)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		if (rectangle.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.contains(rectangle)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	default void translate(double dx, double dy) {
		for (final CT shape : getBackendDataList()) {
			shape.translate(dx, dy);
		}
		onBackendDataChange();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Pure
	@Override
	default ST createTransformedShape(Transform2D transform) {
		final MultiShape2afp multishape = getGeomFactory().newMultiShape();
		for (final CT shape : getBackendDataList()) {
			multishape.add(shape.createTransformedShape(transform));
		}
		return (ST) multishape;
	}

	@Unefficient
	@Pure
	@Override
	default CT getFirstShapeIntersecting(ST shape) {
		assert shape != null : AssertMessages.notNullParameter();
		if (shape.intersects(toBoundingBox())) {
			for (final CT innerShape : getBackendDataList()) {
				if (innerShape.intersects(shape)) {
					return innerShape;
				}
			}
		}
		return null;
	}

	@Unefficient
	@Override
	@Pure
	default List<CT> getShapesIntersecting(ST shape) {
		assert shape != null : AssertMessages.notNullParameter();
		final List<CT> list = new ArrayList<>();
		if (shape.intersects(toBoundingBox())) {
			for (final CT subshape : getBackendDataList()) {
				if (subshape.intersects(shape)) {
					list.add(subshape);
				}
			}
		}
		return list;
	}

	@Pure
	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		final Iterator<CT> iterator = getBackendDataList().iterator();
		if (iterator.hasNext()) {
			iterator.next().toBoundingBox(box);
			final B subbounds = getGeomFactory().newBox();
			while (iterator.hasNext()) {
				final CT element = iterator.next();
				element.toBoundingBox(subbounds);
				box.setUnion(subbounds);
			}
		}
	}

	@Override
	default PathIterator2afp<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new MultiShapePathIterator<>(getBackendDataList(), getGeomFactory());
		}
		return new TransformedMultiShapePathIterator<>(getBackendDataList(), getGeomFactory(), transform);
	}

    /** Replies a path iterator on this multishape that is replacing the
     * curves and corner arcs by line approximations.
     *
     * @return the iterator on the approximation.
     * @see #getPathIterator()
     * @see MathConstants#SPLINE_APPROXIMATION_RATIO
     */
    @Pure
    default PathIterator2afp<IE> getFlatteningPathIterator() {
        return new Path2afp.FlatteningPathIterator<>(
                getPathIterator(null),
                MathConstants.SPLINE_APPROXIMATION_RATIO,
                Path2afp.DEFAULT_FLATTENING_LIMIT);
    }

    @Override
	default P getClosestPointTo(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(circle);
			dist = circle.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	@Override
	default P getClosestPointTo(Segment2afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(segment);
			dist = segment.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	@Override
	default P getClosestPointTo(Rectangle2afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(rectangle);
			dist = rectangle.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	@Override
	default P getClosestPointTo(RoundRectangle2afp<?, ?, ?, ?, ?, ?> roundRectangle) {
		assert roundRectangle != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(roundRectangle);
			dist = roundRectangle.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	@Override
	default P getClosestPointTo(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		assert ellipse != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(ellipse);
			dist = ellipse.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	@Override
	default P getClosestPointTo(Triangle2afp<?, ?, ?, ?, ?, ?> triangle) {
		assert triangle != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(triangle);
			dist = triangle.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	@Override
	default P getClosestPointTo(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> orientedRectangle) {
		assert orientedRectangle != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(orientedRectangle);
			dist = orientedRectangle.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	@Override
	default P getClosestPointTo(Parallelogram2afp<?, ?, ?, ?, ?, ?> parallelogram) {
		assert parallelogram != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(parallelogram);
			dist = parallelogram.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	@Override
	default P getClosestPointTo(Path2afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(path);
			dist = path.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	@Override
	default P getClosestPointTo(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		double min = Double.POSITIVE_INFINITY;
		final P closest = getGeomFactory().newPoint();
		P point;
		double dist;
		for (final CT innerShape : getBackendDataList()) {
			point = innerShape.getClosestPointTo(multishape);
			dist = multishape.getDistanceSquared(point);
			if (dist < min) {
				min = dist;
				closest.set(point);
			}
		}
		return closest;
	}

	/** Abstract iterator on the path elements of the multishape.
	 *
	 * @param <IE> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractMultiShapePathIterator<IE extends PathElement2afp> implements PathIterator2afp<IE> {

		/** Iterated list.
		 */
		protected final List<? extends Shape2afp<?, ?, IE, ?, ?, ?>> list;

		private final GeomFactory2afp<IE, ?, ?, ?> factory;

		private Iterator<? extends Shape2afp<?, ?, IE, ?, ?, ?>> shapesIterator;

		private PathIterator2afp<IE> shapeIterator;

		private IE next;

		private boolean isCurved;

		private boolean isPolyline;

		private boolean isPolygon;

		private boolean isMultiParts;

		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 */
		public AbstractMultiShapePathIterator(List<? extends Shape2afp<?, ?, IE, ?, ?, ?>> list,
				GeomFactory2afp<IE, ?, ?, ?> factory) {
			assert list != null : AssertMessages.notNullParameter(0);
			assert factory != null : AssertMessages.notNullParameter(1);
			this.list = list;
			this.factory = factory;
			this.shapesIterator = list.iterator();
		}

		/** Initialization.
		 *
		 * @param list the list to iterate on.
		 */
		protected void delayedInit(List<? extends Shape2afp<?, ?, IE, ?, ?, ?>> list) {
			if (this.shapesIterator.hasNext()) {
				this.shapeIterator = getPathIteratorFrom(this.shapesIterator.next());
				searchNext();
			}
			this.isMultiParts = list.size() > 1
					|| (this.shapeIterator != null
					&& this.shapeIterator.isMultiParts());
			this.isPolygon = list.size() == 1
					&& this.shapeIterator != null
					&& this.shapeIterator.isPolygon();
			this.isPolyline = list.size() == 1
					&& this.shapeIterator != null
					&& this.shapeIterator.isPolyline();
			this.isCurved = list.size() > 0
					&& this.shapeIterator != null
					&& this.shapeIterator.isCurved();
		}

		/** Replies the path iterator of the shape.
		 *
		 * @param shape the shape.
		 * @return the path iterator.
		 */
		protected abstract PathIterator2afp<IE> getPathIteratorFrom(Shape2afp<?, ?, IE, ?, ?, ?> shape);

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public IE next() {
			assert this.next != null : AssertMessages.notNullParameter();
			final IE elementToReturn = this.next;
			searchNext();
			return elementToReturn;
		}

		private void searchNext() {
			this.next = null;
			while (!this.shapeIterator.hasNext()) {
				if (this.shapesIterator.hasNext()) {
					this.shapeIterator = getPathIteratorFrom(this.shapesIterator.next());
					this.isCurved |= this.shapeIterator.isCurved();
				} else {
					return;
				}
			}
			assert this.shapeIterator != null;
			assert this.shapeIterator.hasNext();
			this.next = this.shapeIterator.next();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.EVEN_ODD;
		}

		@Override
		public boolean isPolyline() {
			return this.isPolyline;
		}

		@Override
		public boolean isCurved() {
			return this.isCurved;
		}

		@Override
		public boolean isMultiParts() {
			return this.isMultiParts;
		}

		@Override
		public boolean isPolygon() {
			return this.isPolygon;
		}

		@Override
		public GeomFactory2afp<IE, ?, ?, ?> getGeomFactory() {
			return this.factory;
		}

	}

	/** Iterator on the path elements of the multishape.
	 *
	 * @param <IE> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class MultiShapePathIterator<IE extends PathElement2afp> extends AbstractMultiShapePathIterator<IE> {

		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 */
		public MultiShapePathIterator(List<? extends Shape2afp<?, ?, IE, ?, ?, ?>> list,
				GeomFactory2afp<IE, ?, ?, ?> factory) {
			super(list, factory);
			delayedInit(list);
		}

		@Override
		public PathIterator2afp<IE> restartIterations() {
			return new MultiShapePathIterator<>(this.list, getGeomFactory());
		}

		@Override
		protected PathIterator2afp<IE> getPathIteratorFrom(Shape2afp<?, ?, IE, ?, ?, ?> shape) {
			return shape.getPathIterator();
		}

	}

	/** Iterator on the path elements of the multishape.
	 *
	 * @param <IE> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class TransformedMultiShapePathIterator<IE extends PathElement2afp> extends AbstractMultiShapePathIterator<IE> {

		private final Transform2D transform;

		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 * @param transform the transformation to apply.
		 */
		public TransformedMultiShapePathIterator(List<? extends Shape2afp<?, ?, IE, ?, ?, ?>> list,
				GeomFactory2afp<IE, ?, ?, ?> factory, Transform2D transform) {
			super(list, factory);
			assert transform != null : AssertMessages.notNullParameter(2);
			this.transform = transform;
			delayedInit(list);
		}

		@Override
		public PathIterator2afp<IE> restartIterations() {
			return new TransformedMultiShapePathIterator<>(this.list, getGeomFactory(), this.transform);
		}

		@Override
		protected PathIterator2afp<IE> getPathIteratorFrom(Shape2afp<?, ?, IE, ?, ?, ?> shape) {
			return shape.getPathIterator(this.transform);
		}

	}

}
