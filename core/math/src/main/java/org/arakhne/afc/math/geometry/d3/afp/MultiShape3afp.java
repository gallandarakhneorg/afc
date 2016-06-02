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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.MultiShape3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

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
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface MultiShape3afp<
		ST extends Shape3afp<?, ?, IE, P, V, B>,
		IT extends MultiShape3afp<?, ?, CT, IE, P, V, B>,
		CT extends Shape3afp<?, ?, IE, P, V, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3afp<?, ?, IE, P, V, B>>
		extends Shape3afp<ST, IT, IE, P, V, B>,
		MultiShape3D<ST, IT, CT, PathIterator3afp<IE>, P, V, B> {

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : "Circle must be not null"; //$NON-NLS-1$
		if (sphere.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(sphere)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Prism3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : "Rectangle must be not null"; //$NON-NLS-1$
		if (prism.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.intersects(prism)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : "Segment must be not null"; //$NON-NLS-1$
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
	default boolean intersects(PathIterator3afp<?> iterator) {
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
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : "MultiShape must be not null"; //$NON-NLS-1$
		if (multishape.toBoundingBox().intersects(toBoundingBox())) {
			for (final CT shape1 : getBackendDataList()) {
				for (final Shape3afp<?, ?, ?, ?, ?, ?> shape2 : multishape.getBackendDataList()) {
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
	default boolean contains(double x, double y, double z) {
		if (toBoundingBox().contains(x, y, z)) {
			for (final CT shape : getBackendDataList()) {
				if (shape.contains(x, y, z)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean contains(RectangularPrism3afp<?, ?, ?, ?, ?, B> rectangularPrism) {
		assert rectangularPrism != null : "Rectangle must be not null"; //$NON-NLS-1$
		if (rectangularPrism.intersects(toBoundingBox())) {
			for (final CT shape : getBackendDataList()) {
				if (shape.contains(rectangularPrism)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	default void translate(double dx, double dy, double dz) {
		for (final CT shape : getBackendDataList()) {
			shape.translate(dx, dy, dz);
		}
		onBackendDataChange();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Pure
	@Override
	default ST createTransformedShape(Transform3D transform) {
		final MultiShape3afp multishape = getGeomFactory().newMultiShape();
		for (final CT shape : getBackendDataList()) {
			multishape.add(shape.createTransformedShape(transform));
		}
		return (ST) multishape;
	}

	@Unefficient
	@Pure
	@Override
	default CT getFirstShapeIntersecting(ST shape) {
		assert shape != null : "Input shape must be not null"; //$NON-NLS-1$
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
		assert shape != null : "Shape must be not null"; //$NON-NLS-1$
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
		assert box != null : "Rectangle must be not null"; //$NON-NLS-1$
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
	default PathIterator3afp<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new MultiShapePathIterator<>(getBackendDataList(), getGeomFactory());
		}
		return new TransformedMultiShapePathIterator<>(getBackendDataList(), getGeomFactory(), transform);
	}

	/** Abstract iterator on the path elements of the multishape.
	 *
	 * @param <IE> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractMultiShapePathIterator<IE extends PathElement3afp> implements PathIterator3afp<IE> {

	    /** Iterated list.
	     */
	    protected final List<? extends Shape3afp<?, ?, IE, ?, ?, ?>> list;

		private final GeomFactory3afp<IE, ?, ?, ?> factory;

		private Iterator<? extends Shape3afp<?, ?, IE, ?, ?, ?>> shapesIterator;

		private PathIterator3afp<IE> shapeIterator;

		private IE next;

		private boolean isCurved;

		private boolean isPolyline;

		private boolean isPolygon;

		private boolean isMultiParts;

		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 */
		public AbstractMultiShapePathIterator(List<? extends Shape3afp<?, ?, IE, ?, ?, ?>> list,
				GeomFactory3afp<IE, ?, ?, ?> factory) {
			assert list != null : "List of shapes must be not null"; //$NON-NLS-1$
			assert factory != null : "Shape factory must be not null"; //$NON-NLS-1$
			this.list = list;
			this.factory = factory;
			this.shapesIterator = list.iterator();
		}

		/** Initialization.
		 *
		 * @param list the list to iterate on.
		 */
		protected void delayedInit(List<? extends Shape3afp<?, ?, IE, ?, ?, ?>> list) {
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
		protected abstract PathIterator3afp<IE> getPathIteratorFrom(Shape3afp<?, ?, IE, ?, ?, ?> shape);

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public IE next() {
			assert this.next != null : "No such element"; //$NON-NLS-1$
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
		public GeomFactory3afp<IE, ?, ?, ?> getGeomFactory() {
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
	class MultiShapePathIterator<IE extends PathElement3afp> extends AbstractMultiShapePathIterator<IE> {

		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 */
		public MultiShapePathIterator(List<? extends Shape3afp<?, ?, IE, ?, ?, ?>> list,
				GeomFactory3afp<IE, ?, ?, ?> factory) {
			super(list, factory);
			delayedInit(list);
		}

		@Override
		public PathIterator3afp<IE> restartIterations() {
			return new MultiShapePathIterator<>(this.list, getGeomFactory());
		}

		@Override
		protected PathIterator3afp<IE> getPathIteratorFrom(Shape3afp<?, ?, IE, ?, ?, ?> shape) {
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
	class TransformedMultiShapePathIterator<IE extends PathElement3afp> extends AbstractMultiShapePathIterator<IE> {

		private final Transform3D transform;

		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 * @param transform the transformation to apply.
		 */
		public TransformedMultiShapePathIterator(List<? extends Shape3afp<?, ?, IE, ?, ?, ?>> list,
				GeomFactory3afp<IE, ?, ?, ?> factory, Transform3D transform) {
			super(list, factory);
			assert transform != null : "Transformation must be not null"; //$NON-NLS-1$
			this.transform = transform;
			delayedInit(list);
		}

		@Override
		public PathIterator3afp<IE> restartIterations() {
			return new TransformedMultiShapePathIterator<>(this.list, getGeomFactory(), this.transform);
		}

		@Override
		protected PathIterator3afp<IE> getPathIteratorFrom(Shape3afp<?, ?, IE, ?, ?, ?> shape) {
			return shape.getPathIterator(this.transform);
		}

	}

}
