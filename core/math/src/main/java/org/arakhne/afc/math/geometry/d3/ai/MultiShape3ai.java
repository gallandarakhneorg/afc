/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.MultiShape3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

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
public interface MultiShape3ai<
		ST extends Shape3ai<?, ?, IE, P, V, B>,
		IT extends MultiShape3ai<?, ?, CT, IE, P, V, B>,
		CT extends Shape3ai<?, ?, IE, P, V, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, IE, P, V, B>>
		extends Shape3ai<ST, IT, IE, P, V, B>,
		MultiShape3D<ST, IT, CT, PathIterator3ai<IE>, P, V, B> {

	@Pure
	@Override
	default boolean intersects(Sphere3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Circle must be not null"; //$NON-NLS-1$
		if (s.intersects(toBoundingBox())) {
			for (CT shape : getBackendDataList()) {
				if (shape.intersects(s)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(RectangularPrism3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Rectangle must be not null"; //$NON-NLS-1$
		if (s.intersects(toBoundingBox())) {
			for (CT shape : getBackendDataList()) {
				if (shape.intersects(s)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Segment3ai<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Segment must be not null"; //$NON-NLS-1$
		if (s.intersects(toBoundingBox())) {
			for (CT shape : getBackendDataList()) {
				if (shape.intersects(s)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3ai<?> iterator) {
		if (toBoundingBox().intersects(iterator)) {
			for (CT shape : getBackendDataList()) {
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
	default boolean intersects(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "MultiShape must be not null"; //$NON-NLS-1$
		if (s.toBoundingBox().intersects(toBoundingBox())) {
			for (CT shape1 : getBackendDataList()) {
				for (Shape3ai<?, ?, ?, ?, ?, ?> shape2 : s.getBackendDataList()) {
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
			for (CT shape : getBackendDataList()) {
				if (shape.contains(x, y)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean contains(RectangularPrism3ai<?, ?, ?, ?, ?, ?> r) {
		assert (r != null) : "Rectangle must be not null"; //$NON-NLS-1$
		if (r.intersects(toBoundingBox())) {
			for (CT shape : getBackendDataList()) {
				if (shape.contains(r)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	default void translate(int dx, int dy) {
		for (CT shape : getBackendDataList()) {
			shape.translate(dx, dy);
		}
		onBackendDataChange();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Pure
	@Override
	default ST createTransformedShape(Transform2D transform) {
		MultiShape3ai multishape = getGeomFactory().newMultiShape();
		for (CT shape : getBackendDataList()) {
			multishape.add(shape.createTransformedShape(transform));
		}
		return (ST) multishape;
	}

	@Unefficient
	@Pure
	@Override
	default CT getFirstShapeIntersecting(ST shape) {
		assert (shape != null) : "Input shape must be not null"; //$NON-NLS-1$
		if (shape.intersects(toBoundingBox())) {
			for (CT innerShape : getBackendDataList()) {
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
		assert (shape != null) : "Shape must be not null"; //$NON-NLS-1$
		List<CT> list = new ArrayList<>();
		if (shape.intersects(toBoundingBox())) {
			for (CT subshape : getBackendDataList()) {
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
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		Iterator<CT> iterator = getBackendDataList().iterator();
		if (iterator.hasNext()) {
			iterator.next().toBoundingBox(box);
			B subbounds = getGeomFactory().newBox();
			while (iterator.hasNext()) {
				CT element = iterator.next();
				element.toBoundingBox(subbounds);
				box.setUnion(subbounds);
			}
		}
	}

	@Override
	default PathIterator3ai<IE> getPathIterator(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return new MultiShapePathIterator<>(getBackendDataList(), getGeomFactory());
		}
		return new TransformedMultiShapePathIterator<>(getBackendDataList(), getGeomFactory(), transform);
	}

	@Override
	default Iterator<P> getPointIterator() {
		return new MultiShapePointIterator<>(getBackendDataList());
	}
	
	/** Abstract iterator on the path elements of the multishape.
	 * 
	 * @param <IE> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	abstract class AbstractMultiShapePathIterator<IE extends PathElement3ai> implements PathIterator3ai<IE> {
		
		private final GeomFactory3ai<IE, ?, ?, ?> factory;
		
		private Iterator<? extends Shape3ai<?, ?, IE, ?, ?, ?>> shapesIterator;
		
		private PathIterator3ai<IE> shapeIterator;
		
		private IE next;
				
		private boolean isCurved;

		private boolean isPolyline;

		private boolean isPolygon;

		private boolean isMultiParts;

		/** Iterated list.
		 */
		protected final List<? extends Shape3ai<?, ?, IE, ?, ?, ?>> list;
		
		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 */
		public AbstractMultiShapePathIterator(List<? extends Shape3ai<?, ?, IE, ?, ?, ?>> list,
				GeomFactory3ai<IE, ?, ?, ?> factory) {
			assert (list != null) : "List of shapes must be not null"; //$NON-NLS-1$
			assert (factory != null) : "Shape factory must be not null"; //$NON-NLS-1$
			this.list = list;
			this.factory = factory;
			this.shapesIterator = list.iterator();
		}
		
		/** Initialization.
		 * 
		 * @param list the list to iterate on.
		 */
		protected void delayedInit(List<? extends Shape3ai<?, ?, IE, ?, ?, ?>> list) {
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
		protected abstract PathIterator3ai<IE> getPathIteratorFrom(Shape3ai<?, ?, IE, ?, ?, ?> shape);
		
		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public IE next() {
			assert (this.next != null) : "No such element"; //$NON-NLS-1$
			IE elementToReturn = this.next;
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
			assert (this.shapeIterator != null);
			assert (this.shapeIterator.hasNext());
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
		public GeomFactory3ai<IE, ?, ?, ?> getGeomFactory() {
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
	class MultiShapePathIterator<IE extends PathElement3ai> extends AbstractMultiShapePathIterator<IE> {
		
		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 */
		public MultiShapePathIterator(List<? extends Shape3ai<?, ?, IE, ?, ?, ?>> list,
				GeomFactory3ai<IE, ?, ?, ?> factory) {
			super(list, factory);
			delayedInit(list);
		}
		
		@Override
		public PathIterator3ai<IE> restartIterations() {
			return new MultiShapePathIterator<>(this.list, getGeomFactory());
		}

		@Override
		protected PathIterator3ai<IE> getPathIteratorFrom(Shape3ai<?, ?, IE, ?, ?, ?> shape) {
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
	class TransformedMultiShapePathIterator<IE extends PathElement3ai> extends AbstractMultiShapePathIterator<IE> {

		private final Transform2D transform;
		
		/**
		 * @param list the list of the shapes to iterate on.
		 * @param factory the factory of path elements.
		 * @param transform the transformation to apply.
		 */
		public TransformedMultiShapePathIterator(List<? extends Shape3ai<?, ?, IE, ?, ?, ?>> list,
				GeomFactory3ai<IE, ?, ?, ?> factory, Transform2D transform) {
			super(list, factory);
			assert (transform != null) : "Transformation must be not null"; //$NON-NLS-1$
			this.transform = transform;
			delayedInit(list);
		}

		@Override
		public PathIterator3ai<IE> restartIterations() {
			return new TransformedMultiShapePathIterator<>(this.list, getGeomFactory(), this.transform);
		}

		@Override
		protected PathIterator3ai<IE> getPathIteratorFrom(Shape3ai<?, ?, IE, ?, ?, ?> shape) {
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

		private final Iterator<? extends Shape3ai<?, ?, ?, P, V, ?>> elements;
		
		private Iterator<P> currentIterator;
		
		private P next;
		
		/**
		 * @param list the list of the shapes to iterate on.
		 * 
		 */
		public MultiShapePointIterator(List<? extends Shape3ai<?, ?, ?, P, V, ?>> list) {
			assert (list != null) : "List of elements must be not null"; //$NON-NLS-1$
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
			assert (this.next != null) : "No such element"; //$NON-NLS-1$
			P point = this.next;
			searchNext();
			return point;
		}
	
	}

}