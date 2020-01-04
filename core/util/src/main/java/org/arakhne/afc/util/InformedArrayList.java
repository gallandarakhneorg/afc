/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.util;

import java.util.ArrayList;
import java.util.Collection;

import org.arakhne.afc.vmutil.ReflectionUtil;

/**
 * This is an array list which knows that is the type
 * of their elements.
 *
 * <p>The type of the elements is automatically computed
 * when adding an element inside the list.
 *
 * <p>For the removal actions, a flag is available to
 * force this informed array list to
 * compute the best fitting type after the removal action.
 * If this flag is <code>false</code>, this class will
 * keep unchanged the previously computed type.
 * By default, this flag is <code>false</code> (see
 * {@link #isTypeRecomputedAfterRemoval()}).
 *
 * @param <E> is the type of the list's elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class InformedArrayList<E> extends ArrayList<E> implements InformedIterable<E> {

	private static final long serialVersionUID = -921267155141517977L;

	private Class<? extends E> clazz;

	private boolean updateWhenRemove;

	/**
	 * Create an empty list.
	 */
	public InformedArrayList() {
		super();
	}

	/**
	 * Create an empty list with the given initial capacity.
	 *
	 * @param initialCapacity is the initial capacity.
	 */
	public InformedArrayList(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Create a list with the given collection.
	 *
	 * @param collection is the list of initial elements.
	 */
	public InformedArrayList(Collection<? extends E> collection) {
		super(collection);
		this.clazz = extractClassFrom(collection);
	}

	/** Constructor.
	 *
	 * @param clazz is the initial top type of the array's elements.
	 */
	public InformedArrayList(Class<? extends E> clazz) {
		this.clazz = clazz;
	}

	/** Constructor.
	 * @param initialCapacity is the initial capacity.
	 * @param clazz is the initial top type of the array's elements.
	 */
	public InformedArrayList(int initialCapacity, Class<? extends E> clazz) {
		super(initialCapacity);
		this.clazz = clazz;
	}

	/** Constructor.
	 * @param collection is the list of initial elements.
	 * @param clazz is the initial top type of the array's elements.
	 */
	public InformedArrayList(Collection<? extends E> collection, Class<? extends E> clazz) {
		super(collection);
		this.clazz = clazz;
	}

	/** Extract the upper class that contains all the elements of
	 * this array.
	 *
	 * @param <E> is the type of the list's elements.
	 * @param collection is the collection to scan
	 * @return the top class of all the elements.
	 */
	@SuppressWarnings("unchecked")
	protected static <E> Class<? extends E> extractClassFrom(Collection<? extends E> collection) {
		Class<? extends E> clazz = null;
		for (final E elt : collection) {
			clazz = (Class<? extends E>) ReflectionUtil.getCommonType(clazz, elt.getClass());
		}
		return clazz == null ? (Class<E>) Object.class : clazz;
	}

	/** Replies if this informed array list is trying to
	 * compute the best fitting type after a removal.
	 * If <code>false</code>, this class will keep unchanged
	 * the previously computed type.
	 *
	 * @return <code>true</code> if the type is recomputed
	 *     after a removal, <code>false</code> if not.
	 */
	public boolean isTypeRecomputedAfterRemoval() {
		return this.updateWhenRemove;
	}

	/** Set if this informed array list is trying to
	 * compute the best fitting type after a removal.
	 * If <code>false</code>, this class will keep unchanged
	 * the previously computed type.
	 *
	 * @param update is <code>true</code> if the type is recomputed
	 *     after a removal, <code>false</code> if not.
	 */
	public void setTypeRecomputedAfterRemoval(boolean update) {
		this.updateWhenRemove = update;
	}

	/** Update the component type information with
	 * the type of the new array element.
	 *
	 * @param newElement is the element for which the known top type in this array must be eventually updated.
	 */
	@SuppressWarnings("unchecked")
	protected final void updateComponentType(E newElement) {
		final Class<? extends E> lclazz = (Class<? extends E>) newElement.getClass();
		this.clazz = (Class<? extends E>) ReflectionUtil.getCommonType(this.clazz, lclazz);
	}

	/** Update the component type information with
	 * the type of the new array element.
	 *
	 * @param newElements are the elements for which the known top type in this array must be eventually updated.
	 */
	@SuppressWarnings("unchecked")
	protected final void updateComponentType(Collection<? extends E> newElements) {
		final Class<? extends E> lclazz = extractClassFrom(newElements);
		this.clazz = (Class<? extends E>) ReflectionUtil.getCommonType(this.clazz, lclazz);
	}

	@Override
	public Class<? extends E> getElementType() {
		return this.clazz;
	}

	/** Change the class of the elements in this iterable object.
	 *
	 * @param type the type of the elements.
	 * @since 15.0
	 */
	public void setElementType(Class<? extends E> type) {
		this.clazz = type;
	}

	@Override
	public boolean add(E elt) {
		if (super.add(elt)) {
			if (elt != null) {
				updateComponentType(elt);
			}
			return true;
		}
		return false;
	}

	@Override
	public void add(int index, E element) {
		super.add(index, element);
		if (element != null) {
			updateComponentType(element);
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		if (super.addAll(collection)) {
			updateComponentType(collection);
			return true;
		}
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		if (super.addAll(index, collection)) {
			updateComponentType(collection);
			return true;
		}
		return false;
	}

	@Override
	public void clear() {
		super.clear();
		this.clazz = null;
	}

	@Override
	public E remove(int index) {
		final E e = super.remove(index);
		if (isEmpty()) {
			this.clazz = null;
		} else if (isTypeRecomputedAfterRemoval()) {
			this.clazz = extractClassFrom(this);
		}
		return e;
	}

	@Override
	public boolean remove(Object obj) {
		if (super.remove(obj)) {
			if (isEmpty()) {
				this.clazz = null;
			} else if (isTypeRecomputedAfterRemoval()) {
				this.clazz = extractClassFrom(this);
			}
			return true;
		}
		return false;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
		if (isEmpty()) {
			this.clazz = null;
		} else if (isTypeRecomputedAfterRemoval()) {
			this.clazz = extractClassFrom(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public E set(int index, E element) {
		final E elt = super.set(index, element);
		if (element != null) {
			if (size() == 1) {
				this.clazz = (Class<? extends E>) element.getClass();
			} else {
				updateComponentType(element);
			}
		}
		return elt;
	}

}
