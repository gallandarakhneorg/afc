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

package org.arakhne.afc.math.geometry.d2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.Unefficient;

/** Container for grouping of shapes.
 *
 * <p>The coordinates of the shapes inside the multishape are global. They are not relative to the multishape.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this multishape.
 * @param <CT> is the type of the shapes that are inside this multishape.
 * @param <I> is the type of the iterator used to obtain the elements of the path.
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
public interface MultiShape2D<
		ST extends Shape2D<?, ?, I, P, V, B>,
		IT extends MultiShape2D<?, ?, CT, I, P, V, B>,
		CT extends Shape2D<?, ?, I, P, V, B>,
		I extends PathIterator2D<?>,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Shape2D<?, ?, I, P, V, B>> extends Shape2D<ST, IT, I, P, V, B>, List<CT> {

	/** Get the first shape in this multishape that is containing the given point.
	 *
	 * @param point the point.
	 * @return the shape, or <code>null</code> if no shape contains the given point.
	 */
	@Pure
	default CT getFirstShapeContaining(Point2D<?, ?> point) {
		assert point != null : "Point must be not null"; 
		if (toBoundingBox().contains(point)) {
			for (final CT shape : getBackendDataList()) {
				if (shape.contains(point)) {
					return shape;
				}
			}
		}
		return null;
	}

	/** Get the shapes in this multishape that are containing the given point.
	 *
	 * @param point the point.
	 * @return the shapes, or an empty list.
	 */
	@Pure
	@Unefficient
	default List<CT> getShapesContaining(Point2D<?, ?> point) {
		assert point != null : "Point must be not null"; 
		final List<CT> list = new ArrayList<>();
		if (toBoundingBox().contains(point)) {
			for (final CT shape : getBackendDataList()) {
				if (shape.contains(point)) {
					list.add(shape);
				}
			}
		}
		return list;
	}

	/** Get the first shape in this multishape that is intersecting the given shape.
	 *
	 * @param shape the shape.
	 * @return the shape, or <code>null</code> if no shape intersecting the given shape.
	 */
	@Pure
	CT getFirstShapeIntersecting(ST shape);

	/** Get the shapes in this multishape that are intersecting the given shape.
	 *
	 * @param shape the shape.
	 * @return the shapes, or an empty list.
	 */
	@Pure
	List<CT> getShapesIntersecting(ST shape);

	/** Replies the list that contains the backend data.
	 *
	 * <p>Use this function with caution. Indeed, any change made in the replied list
	 * has no consequence on the internal attributes of this multishape object.
	 *
	 * @return the backend data list.
	 */
	@Pure
	List<CT> getBackendDataList();

	/** Invoked each time the backend data has changed.
	 */
	default void onBackendDataChange() {
		//
	}

	@Override
	default void set(IT shape) {
		assert shape != null : "Multishape must be not null"; 
		final List<CT> backend = getBackendDataList();
		backend.clear();
		backend.addAll(shape.getBackendDataList());
		onBackendDataChange();
	}

	@Override
	default CT set(int index, CT element) {
		final CT old = getBackendDataList().set(index, element);
		onBackendDataChange();
		return old;
	}

	@Override
	@Pure
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		return getBackendDataList().equals(shape.getBackendDataList());
	}

	@Override
	default void clear() {
		getBackendDataList().clear();
		onBackendDataChange();
	}

	@Pure
	@Override
	default boolean isEmpty() {
		if (getBackendDataList().isEmpty()) {
			return true;
		}
		return toBoundingBox().isEmpty();
	}

	@Pure
	@Override
	default P getClosestPointTo(Point2D<?, ?> point) {
		P closestPoint = null;
		double minDist = Double.POSITIVE_INFINITY;
		for (final CT shape : getBackendDataList()) {
			final P close = shape.getClosestPointTo(point);
			final double dist = close.getDistanceSquared(point);
			if (dist < minDist) {
				minDist = dist;
				closestPoint = close;
			}
		}
		return closestPoint;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point2D<?, ?> point) {
		P farthestPoint = null;
		double maxDist = Double.NEGATIVE_INFINITY;
		for (final CT shape : getBackendDataList()) {
			final P far = shape.getFarthestPointTo(point);
			final double dist = far.getDistanceSquared(point);
			if (dist > maxDist) {
				maxDist = dist;
				farthestPoint = far;
			}
		}
		return farthestPoint;
	}

	@Pure
	@Override
	default double getDistanceSquared(Point2D<?, ?> point) {
		double minDist = Double.POSITIVE_INFINITY;
		for (final CT shape : getBackendDataList()) {
			final double dist = shape.getDistanceSquared(point);
			if (dist < minDist) {
				minDist = dist;
			}
		}
		return minDist;
	}

	@Pure
	@Override
	default double getDistanceL1(Point2D<?, ?> point) {
		double minDist = Double.POSITIVE_INFINITY;
		for (final CT shape : getBackendDataList()) {
			final double dist = shape.getDistanceL1(point);
			if (dist < minDist) {
				minDist = dist;
			}
		}
		return minDist;
	}

	@Pure
	@Override
	default double getDistanceLinf(Point2D<?, ?> point) {
		double minDist = Double.POSITIVE_INFINITY;
		for (final CT shape : getBackendDataList()) {
			final double dist = shape.getDistanceLinf(point);
			if (dist < minDist) {
				minDist = dist;
			}
		}
		return minDist;
	}

	@Pure
	@Override
	default Object[] toArray() {
		return getBackendDataList().toArray();
	}

	@Override
	default <T> T[] toArray(T[] array) {
		return getBackendDataList().toArray(array);
	}

	@Pure
	@Override
	default int size() {
		return getBackendDataList().size();
	}

	@Pure
	@Override
	default boolean contains(Object obj) {
		return getBackendDataList().contains(obj);
	}

	@Pure
	@Override
	default Iterator<CT> iterator() {
		return new BackendIterator<>(this, getBackendDataList().listIterator());
	}

	@Override
	default boolean add(CT element) {
		if (getBackendDataList().add(element)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Override
	default void add(int index, CT element) {
		getBackendDataList().add(index, element);
		onBackendDataChange();
	}

	@Override
	default boolean remove(Object element) {
		if (getBackendDataList().remove(element)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Override
	default CT remove(int index) {
		final CT removed = getBackendDataList().remove(index);
		onBackendDataChange();
		return removed;
	}

	@Pure
	@Override
	default boolean containsAll(Collection<?> collection) {
		return getBackendDataList().containsAll(collection);
	}

	@Override
	default boolean addAll(Collection<? extends CT> collection) {
		if (getBackendDataList().addAll(collection)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Override
	default boolean addAll(int index, Collection<? extends CT> collection) {
		if (getBackendDataList().addAll(index, collection)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Override
	default boolean removeAll(Collection<?> collection) {
		if (getBackendDataList().removeAll(collection)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Override
	default boolean retainAll(Collection<?> collection) {
		if (getBackendDataList().retainAll(collection)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Pure
	@Override
	default CT get(int index) {
		return getBackendDataList().get(index);
	}

	@Pure
	@Override
	default int indexOf(Object obj) {
		return getBackendDataList().indexOf(obj);
	}

	@Pure
	@Override
	default int lastIndexOf(Object obj) {
		return getBackendDataList().lastIndexOf(obj);
	}

	@Pure
	@Override
	default ListIterator<CT> listIterator() {
		return new BackendIterator<>(this, getBackendDataList().listIterator());
	}

	@Pure
	@Override
	default ListIterator<CT> listIterator(int index) {
		return new BackendIterator<>(this, getBackendDataList().listIterator(index));
	}

	@Override
	default List<CT> subList(int fromIndex, int toIndex) {
		return new BackendList<>(this, getBackendDataList().subList(fromIndex, toIndex));
	}

	/** Iterator on elements of a list that is able to notify the backend when
	 * the iterator has change the backend data.
	 *
	 * @param <CT> the type of the iterated shapes.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class BackendIterator<CT extends Shape2D<?, ?, ?, ?, ?, ?>> implements ListIterator<CT> {

		private final MultiShape2D<?, ?, CT, ?, ?, ?, ?> backend;

		private final ListIterator<CT> iterator;

		/**
		 * @param backend the associated backend.
		 * @param iterator the original iterator.
		 */
		public BackendIterator(MultiShape2D<?, ?, CT, ?, ?, ?, ?> backend, ListIterator<CT> iterator) {
			assert backend != null : "Backend must be not null"; 
			assert iterator != null : "Iterator must be not null"; 
			this.backend = backend;
			this.iterator = iterator;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public CT next() {
			return this.iterator.next();
		}

		@Override
		public void remove() {
			this.iterator.remove();
			this.backend.onBackendDataChange();
		}

		@Pure
		@Override
		public boolean hasPrevious() {
			return this.iterator.hasPrevious();
		}

		@Override
		public CT previous() {
			return this.iterator.previous();
		}

		@Pure
		@Override
		public int nextIndex() {
			return this.iterator.nextIndex();
		}

		@Pure
		@Override
		public int previousIndex() {
			return this.iterator.previousIndex();
		}

		@Override
		public void set(CT element) {
			this.iterator.set(element);
			this.backend.onBackendDataChange();
		}

		@Override
		public void add(CT element) {
			this.iterator.add(element);
			this.backend.onBackendDataChange();
		}

	}

	/** View on a list that is able to notify the backend when
	 * the view has change the backend data.
	 *
	 * @param <CT> the type of the shapes in the list view.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class BackendList<CT extends Shape2D<?, ?, ?, ?, ?, ?>> implements List<CT> {

		private final MultiShape2D<?, ?, CT, ?, ?, ?, ?> backend;

		private final List<CT> list;

		/**
		 * @param backend the associated backend.
		 * @param list the original list.
		 */
		public BackendList(MultiShape2D<?, ?, CT, ?, ?, ?, ?> backend, List<CT> list) {
			assert backend != null : "Backend must be not null"; 
			assert list != null : "List must be not null"; 
			this.backend = backend;
			this.list = list;
		}

		@Pure
		@Override
		public int size() {
			return this.list.size();
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return this.list.isEmpty();
		}

		@Pure
		@Override
		public boolean contains(Object obj) {
			return this.list.contains(obj);
		}

		@Pure
		@Override
		public Iterator<CT> iterator() {
			return new BackendIterator<>(this.backend, this.list.listIterator());
		}

		@Pure
		@Override
		public Object[] toArray() {
			return this.list.toArray();
		}

		@Override
		public <T> T[] toArray(T[] array) {
			return this.list.toArray(array);
		}

		@Override
		public boolean add(CT element) {
			if (this.list.add(element)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public void add(int index, CT element) {
			this.list.add(index, element);
			this.backend.onBackendDataChange();
		}

		@Override
		public boolean remove(Object obj) {
			if (this.list.remove(obj)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public CT remove(int index) {
			final CT old = this.list.remove(index);
			this.backend.onBackendDataChange();
			return old;
		}

		@Pure
		@Override
		public boolean containsAll(Collection<?> collection) {
			return this.list.containsAll(collection);
		}

		@Override
		public boolean addAll(Collection<? extends CT> collection) {
			if (this.list.addAll(collection)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public boolean addAll(int index, Collection<? extends CT> collection) {
			if (this.list.addAll(index, collection)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> collection) {
			if (this.list.removeAll(collection)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> collection) {
			if (this.list.retainAll(collection)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public void clear() {
			this.list.clear();
			this.backend.onBackendDataChange();
		}

		@Pure
		@Override
		public CT get(int index) {
			return this.list.get(index);
		}

		@Override
		public CT set(int index, CT element) {
			final CT old = this.list.set(index, element);
			this.backend.onBackendDataChange();
			return old;
		}

		@Pure
		@Override
		public int indexOf(Object obj) {
			return this.list.indexOf(obj);
		}

		@Pure
		@Override
		public int lastIndexOf(Object obj) {
			return this.list.lastIndexOf(obj);
		}

		@Pure
		@Override
		public ListIterator<CT> listIterator() {
			return new BackendIterator<>(this.backend, this.list.listIterator());
		}

		@Override
		public ListIterator<CT> listIterator(int index) {
			return new BackendIterator<>(this.backend, this.list.listIterator(index));
		}

		@Override
		public List<CT> subList(int fromIndex, int toIndex) {
			return new BackendList<>(this.backend, this.list.subList(fromIndex, toIndex));
		}

	}

}
