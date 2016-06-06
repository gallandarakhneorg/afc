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
public interface MultiShape2ai<
		ST extends Shape2ai<?, ?, IE, P, V, B>,
		IT extends MultiShape2ai<?, ?, CT, IE, P, V, B>,
		CT extends Shape2ai<?, ?, IE, P, V, B>,
		IE extends PathElement2ai,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2ai<?, ?, IE, P, V, B>>
		extends Shape2ai<ST, IT, IE, P, V, B>,
		MultiShape2D<ST, IT, CT, PathIterator2ai<IE>, P, V, B> {

	@Pure
	@Override
	default boolean intersects(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
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
	default boolean intersects(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
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
	default boolean intersects(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
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
	default boolean intersects(PathIterator2ai<?> iterator) {
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
	@Unefficient
	default boolean intersects(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		if (multishape.toBoundingBox().intersects(toBoundingBox())) {
			for (final CT shape1 : getBackendDataList()) {
				for (final Shape2ai<?, ?, ?, ?, ?, ?> shape2 : multishape.getBackendDataList()) {
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
	default boolean contains(int x, int y) {
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
	default boolean contains(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
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
	default void translate(int dx, int dy) {
		for (final CT shape : getBackendDataList()) {
			shape.translate(dx, dy);
		}
		onBackendDataChange();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Pure
	@Override
	default ST createTransformedShape(Transform2D transform) {
		final MultiShape2ai multishape = getGeomFactory().newMultiShape();
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
	default PathIterator2ai<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new MultiShapePathIterator<>(getBackendDataList(), getGeomFactory());
		}
		return new TransformedMultiShapePathIterator<>(getBackendDataList(), getGeomFactory(), transform);
	}

    /** Replies a path iterator on this shape that is replacing the
     * curves by line approximations.
     *
     * @return the iterator on the approximation.
     * @see #getPathIterator()
     * @see MathConstants#SPLINE_APPROXIMATION_RATIO
     */
    @Pure
    default PathIterator2ai<IE> getFlatteningPathIterator() {
        return new Path2ai.FlatteningPathIterator<>(
                getPathIterator(null),
                MathConstants.SPLINE_APPROXIMATION_RATIO,
                Path2ai.DEFAULT_FLATTENING_LIMIT);
    }

    @Override
	default Iterator<P> getPointIterator() {
		return new MultiShapePointIterator<>(getBackendDataList());
	}

	@Override
	default P getClosestPointTo(Rectangle2ai<?, ?, ?, ?, ?, ?> rectangle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(Circle2ai<?, ?, ?, ?, ?, ?> circle) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(Segment2ai<?, ?, ?, ?, ?, ?> segment) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(MultiShape2ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		throw new UnsupportedOperationException();
	}

	@Override
	default P getClosestPointTo(Path2ai<?, ?, ?, ?, ?, ?> path) {
		throw new UnsupportedOperationException();
	}

	/** Abstract iterator on the path elements of the multishape.
	 *
	 * @param <IE> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractMultiShapePathIterator<IE extends PathElement2ai> implements PathIterator2ai<IE> {

		/** Iterated list.
		 */
		protected final List<? extends Shape2ai<?, ?, IE, ?, ?, ?>> list;

		private final GeomFactory2ai<IE, ?, ?, ?> factory;

		private Iterator<? extends Shape2ai<?, ?, IE, ?, ?, ?>> shapesIterator;

		private PathIterator2ai<IE> shapeIterator;

		private IE next;

		private boolean isCurved;

		private boolean isPolyline;

		private boolean isPolygon;

		private boolean isMultiParts;

		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 */
		public AbstractMultiShapePathIterator(List<? extends Shape2ai<?, ?, IE, ?, ?, ?>> list,
				GeomFactory2ai<IE, ?, ?, ?> factory) {
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
		protected void delayedInit(List<? extends Shape2ai<?, ?, IE, ?, ?, ?>> list) {
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
		protected abstract PathIterator2ai<IE> getPathIteratorFrom(Shape2ai<?, ?, IE, ?, ?, ?> shape);

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
		public GeomFactory2ai<IE, ?, ?, ?> getGeomFactory() {
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
	class MultiShapePathIterator<IE extends PathElement2ai> extends AbstractMultiShapePathIterator<IE> {

		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 */
		public MultiShapePathIterator(List<? extends Shape2ai<?, ?, IE, ?, ?, ?>> list,
				GeomFactory2ai<IE, ?, ?, ?> factory) {
			super(list, factory);
			delayedInit(list);
		}

		@Override
		public PathIterator2ai<IE> restartIterations() {
			return new MultiShapePathIterator<>(this.list, getGeomFactory());
		}

		@Override
		protected PathIterator2ai<IE> getPathIteratorFrom(Shape2ai<?, ?, IE, ?, ?, ?> shape) {
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
	class TransformedMultiShapePathIterator<IE extends PathElement2ai> extends AbstractMultiShapePathIterator<IE> {

		private final Transform2D transform;

		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 * @param transform the transformation to apply.
		 */
		public TransformedMultiShapePathIterator(List<? extends Shape2ai<?, ?, IE, ?, ?, ?>> list,
				GeomFactory2ai<IE, ?, ?, ?> factory, Transform2D transform) {
			super(list, factory);
			assert transform != null : AssertMessages.notNullParameter(2);
			this.transform = transform;
			delayedInit(list);
		}

		@Override
		public PathIterator2ai<IE> restartIterations() {
			return new TransformedMultiShapePathIterator<>(this.list, getGeomFactory(), this.transform);
		}

		@Override
		protected PathIterator2ai<IE> getPathIteratorFrom(Shape2ai<?, ?, IE, ?, ?, ?> shape) {
			return shape.getPathIterator(this.transform);
		}

	}

	/** Iterator on the points of the multishape.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class MultiShapePointIterator<P extends Point2D<? super P, ? super V>,
			V extends Vector2D<? super V, ? super P>>	 implements Iterator<P> {

		private final Iterator<? extends Shape2ai<?, ?, ?, P, V, ?>> elements;

		private Iterator<P> currentIterator;

		private P next;

		/**
		 * @param list the list of the shapes to iterate on.
		 *
		 */
		public MultiShapePointIterator(List<? extends Shape2ai<?, ?, ?, P, V, ?>> list) {
			assert list != null : AssertMessages.notNullParameter();
			this.elements = list.iterator();
			if (this.elements.hasNext()) {
				this.currentIterator = this.elements.next().getPointIterator();
				searchNext();
			}
		}

		private void searchNext() {
			this.next = null;
			while (true) {
				if (this.currentIterator.hasNext()) {
					this.next = this.currentIterator.next();
					return;
				} else if (this.elements.hasNext()) {
					this.currentIterator = this.elements.next().getPointIterator();
				} else {
					return;
				}
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public P next() {
			assert this.next != null : AssertMessages.notNullParameter();
			final P point = this.next;
			searchNext();
			return point;
		}

	}

}
