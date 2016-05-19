/* 
 * $Id$
 * 
 * Copyright (c) 2016, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports (SET)
 * of Universite de Technologie de Belfort-Montbeliard.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SET.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.geometry.d3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.arakhne.afc.math.Unefficient;
import org.eclipse.xtext.xbase.lib.Pure;

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
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface MultiShape3D<
		ST extends Shape3D<?, ?, I, P, V, B>,
		IT extends MultiShape3D<?, ?, CT, I, P, V, B>,
		CT extends Shape3D<?, ?, I, P, V, B>,
		I extends PathIterator3D<?>,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends Shape3D<?, ?, I, P, V, B>> extends Shape3D<ST, IT, I, P, V, B>, List<CT> {
	
	/** Get the first shape in this multishape that is containing the given point.
	 *
	 * @param point the point.
	 * @return the shape, or <code>null</code> if no shape contains the given point.
	 */
	@Pure
	default CT getFirstShapeContaining(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		if (toBoundingBox().contains(point)) {
			for (CT shape : getBackendDataList()) {
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
	default List<CT> getShapesContaining(Point3D<?, ?> point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		List<CT> list = new ArrayList<>();
		if (toBoundingBox().contains(point)) {
			for (CT shape : getBackendDataList()) {
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
	default void set(IT s) {
		assert (s != null) : "Multishape must be not null"; //$NON-NLS-1$
		List<CT> backend = getBackendDataList();
		backend.clear();
		backend.addAll(s.getBackendDataList());
		onBackendDataChange();
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
	default P getClosestPointTo(Point3D<?, ?> point) {
		P closestPoint = null;
		double minDist = Double.POSITIVE_INFINITY;
		for (CT shape : getBackendDataList()) {
			P close = shape.getClosestPointTo(point);
			double dist = close.getDistanceSquared(point);
			if (dist < minDist) {
				minDist = dist;
				closestPoint = close;
			}
		}
		return closestPoint;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> point) {
		P farthestPoint = null;
		double maxDist = Double.NEGATIVE_INFINITY;
		for (CT shape : getBackendDataList()) {
			P far = shape.getFarthestPointTo(point);
			double dist = far.getDistanceSquared(point);
			if (dist > maxDist) {
				maxDist = dist;
				farthestPoint = far;
			}
		}
		return farthestPoint;
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> point) {
		double minDist = Double.POSITIVE_INFINITY;
		for (CT shape : getBackendDataList()) {
			double dist = shape.getDistanceSquared(point);
			if (dist < minDist) {
				minDist = dist;
			}
		}
		return minDist;
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> point) {
		double minDist = Double.POSITIVE_INFINITY;
		for (CT shape : getBackendDataList()) {
			double dist = shape.getDistanceL1(point);
			if (dist < minDist) {
				minDist = dist;
			}
		}
		return minDist;
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> point) {
		double minDist = Double.POSITIVE_INFINITY;
		for (CT shape : getBackendDataList()) {
			double dist = shape.getDistanceLinf(point);
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
	default <T> T[] toArray(T[] a) {
		return getBackendDataList().toArray(a);
	}

	@Pure
	@Override
	default int size() {
		return getBackendDataList().size();
	}

	@Pure
	@Override
	default boolean contains(Object o) {
		return getBackendDataList().contains(o);
	}

	@Pure
	@Override
	default Iterator<CT> iterator() {
		return new BackendIterator<>(this, getBackendDataList().listIterator());
	}

	@Override
	default boolean add(CT e) {
		if (getBackendDataList().add(e)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Override
	default boolean remove(Object o) {
		if (getBackendDataList().remove(o)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Pure
	@Override
	default boolean containsAll(Collection<?> c) {
		return getBackendDataList().containsAll(c);
	}

	@Override
	default boolean addAll(Collection<? extends CT> c) {
		if (getBackendDataList().addAll(c)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Override
	default boolean addAll(int index, Collection<? extends CT> c) {
		if (getBackendDataList().addAll(index, c)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Override
	default boolean removeAll(Collection<?> c) {
		if (getBackendDataList().removeAll(c)) {
			onBackendDataChange();
			return true;
		}
		return false;
	}

	@Override
	default boolean retainAll(Collection<?> c) {
		if (getBackendDataList().retainAll(c)) {
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

	@Override
	default CT set(int index, CT element) {
		CT old = getBackendDataList().set(index, element);
		onBackendDataChange();
		return old;
	}

	@Override
	default void add(int index, CT element) {
		getBackendDataList().add(index, element);
		onBackendDataChange();
	}

	@Override
	default CT remove(int index) {
		CT removed = getBackendDataList().remove(index);
		onBackendDataChange();
		return removed;
	}

	@Pure
	@Override
	default int indexOf(Object o) {
		return getBackendDataList().indexOf(o);
	}

	@Pure
	@Override
	default int lastIndexOf(Object o) {
		return getBackendDataList().lastIndexOf(o);
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
	class BackendIterator<CT extends Shape3D<?, ?, ?, ?, ?, ?>> implements ListIterator<CT> {

		private final MultiShape3D<?, ?, CT, ?, ?, ?, ?> backend;
		private final ListIterator<CT> iterator;
		
		/**
		 * @param backend the associated backend.
		 * @param iterator the original iterator.
		 */
		public BackendIterator(MultiShape3D<?, ?, CT, ?, ?, ?, ?> backend, ListIterator<CT> iterator) {
			assert (backend != null) : "Backend must be not null"; //$NON-NLS-1$
			assert (iterator != null) : "Iterator must be not null"; //$NON-NLS-1$
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
		public void set(CT e) {
			this.iterator.set(e);
			this.backend.onBackendDataChange();
		}

		@Override
		public void add(CT e) {
			this.iterator.add(e);
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
	class BackendList<CT extends Shape3D<?, ?, ?, ?, ?, ?>> implements List<CT> {

		private final MultiShape3D<?, ?, CT, ?, ?, ?, ?> backend;
		
		private final List<CT> list;
		
		/**
		 * @param backend the associated backend.
		 * @param list the original list.
		 */
		public BackendList(MultiShape3D<?, ?, CT, ?, ?, ?, ?> backend, List<CT> list) {
			assert (backend != null) : "Backend must be not null"; //$NON-NLS-1$
			assert (list != null) : "List must be not null"; //$NON-NLS-1$
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
		public boolean contains(Object o) {
			return this.list.contains(o);
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
		public <T> T[] toArray(T[] a) {
			return this.list.toArray(a);
		}

		@Override
		public boolean add(CT e) {
			if (this.list.add(e)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (this.list.remove(o)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Pure
		@Override
		public boolean containsAll(Collection<?> c) {
			return this.list.containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends CT> c) {
			if (this.list.addAll(c)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public boolean addAll(int index, Collection<? extends CT> c) {
			if (this.list.addAll(index, c)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			if (this.list.removeAll(c)) {
				this.backend.onBackendDataChange();
				return true;
			}
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			if (this.list.retainAll(c)) {
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
			CT old = this.list.set(index, element);
			this.backend.onBackendDataChange();
			return old;
		}

		@Override
		public void add(int index, CT element) {
			this.list.add(index, element);
			this.backend.onBackendDataChange();
		}

		@Override
		public CT remove(int index) {
			CT old = this.list.remove(index);
			this.backend.onBackendDataChange();
			return old;
		}

		@Pure
		@Override
		public int indexOf(Object o) {
			return this.list.indexOf(o);
		}

		@Pure
		@Override
		public int lastIndexOf(Object o) {
			return this.list.lastIndexOf(o);
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
