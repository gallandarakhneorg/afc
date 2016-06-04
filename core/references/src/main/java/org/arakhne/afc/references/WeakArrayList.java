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

package org.arakhne.afc.references;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * A array-based <tt>List</tt> implementation with <em>weak keys</em>.
 * An entry in a <tt>WeakArrayList</tt> will automatically be removed when
 * it is no longer in ordinary use.
 *
 * <p>The behavior of the <tt>WeakArrayList</tt> class depends in part upon
 * the actions of the garbage collector, so several familiar (though not
 * required) <tt>List</tt> invariants do not hold for this class.  Because
 * the garbage collector may discard values at any time, a
 * <tt>WeakArrayList</tt> may behave as though an unknown thread is silently
 * removing entries.  In particular, even if you synchronize on a
 * <tt>WeakArrayList</tt> instance and invoke none of its mutator methods, it
 * is possible for the <tt>size</tt> method to return smaller values over
 * time, for the <tt>isEmpty</tt> method to return <tt>false</tt> and
 * then <tt>true</tt>, for the <tt>contains</tt> method to return
 * <tt>true</tt> and later <tt>false</tt> for a given value, for the
 * <tt>get</tt> method to return a value for a given key but later return
 * <tt>null</tt>, for the <tt>add</tt> method to return
 * <tt>null</tt> and the <tt>remove</tt> method to return
 * <tt>false</tt> for a value that previously appeared to be in the list.
 *
 * <p>If this map does not use a "deep expurge" of the released references,
 * it could contains <code>null</code> values that corresponds to
 * values that are released by the garbage collector. If a "deep expurge"
 * is used, all the values released by the garbage collector will be
 * removed from the map.
 *
 * <p>"Deep expurge" consumes much more time that "No deep expurge". This is the
 * reason why this feature is not activated by default.
 *
 * <p>The "deep expurge" feature was added to fix the uncoherent behavior
 * of the garbage collector which seems to not always enqueued the
 * released values (sometimes the queue is empty even if a value was released).
 *
 * @param <T> is the type of the array's elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WeakArrayList<T> extends AbstractList<T> {

	/** This value represents a null value given by the user.
	 */
	private static final Object NULL_VALUE = new Object();

	private final transient ReferenceQueue<T> queue = new ReferenceQueue<>();

	private Object[] data;

	private int size;

	private boolean enquedElement;

	private List<ReferenceListener> listeners;

	/**
	 * Constructs an empty list with the specified initial capacity.
	 *
	 * @param   initialCapacity   the initial capacity of the list
	 * @exception IllegalArgumentException if the specified initial capacity
	 *            is negative
	 */
	public WeakArrayList(int initialCapacity) {
		assert initialCapacity >= 0 : AssertMessages.positiveOrZeroParameter();
		this.data = new Object[initialCapacity];
		this.size = 0;
	}

	/**
	 * Constructs an empty list with an initial capacity of ten.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public WeakArrayList() {
		this(10);
	}

	/**
	 * Constructs a list containing the elements of the specified
	 * collection, in the order they are returned by the collection's
	 * iterator.
	 *
	 * @param collection the collection whose elements are to be placed into this list
	 * @throws NullPointerException if the specified collection is null
	 */
	public WeakArrayList(Collection<? extends T> collection) {
		this.data = new Object[collection.size()];
		this.size = this.data.length;
		int i = 0;
		for (final T t : collection) {
			this.data[i] = createRef(t);
			i++;
		}
	}

	/** Replies the null value given by the user by the corresponding null object.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T maskNull(T value) {
		return (value == null) ? (T) NULL_VALUE : value;
	}

	/** Replies the value given by the user.
	 */
	private static <T> T unmaskNull(T value) {
		return (value == NULL_VALUE) ? null : value;
	}

	@Pure
	@Override
	@SuppressWarnings("unchecked")
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		Reference<T> ref;
		T obj;
		for (int i = 0; i < this.size; ++i) {
			ref = (Reference<T>) this.data[i];
			if (this.data[i] == null) {
				obj = null;
			} else {
				obj = ref.get();
			}
			buffer.append('{');
			buffer.append(obj == null ? null : obj.toString());
			buffer.append('}');
		}
		return buffer.toString();
	}

	/** Create and replies the reference for the specified object.
	 *
	 * @param obj is the object on which a weak reference must be attached.
	 * @return the weak reference.
	 */
	private Reference<T> createRef(T obj) {
		return new WeakReference<>(maskNull(obj), this.queue);
	}

	/**
	 * Increases the capacity of this <tt>WeakArrayList</tt> instance, if
	 * necessary, to ensure that it can hold at least the number of elements
	 * specified by the minimum capacity argument.
	 *
	 * @param   minCapacity   the desired minimum capacity
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public void ensureCapacity(int minCapacity) {
		this.modCount++;
		final int oldCapacity = this.data.length;
		if (minCapacity > oldCapacity) {
			final Object[] oldData = this.data;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			// minCapacity is usually close to size, so this is a win:
			this.data = Arrays.copyOf(oldData, newCapacity);
		}
	}

	/**
	 * Trims the capacity of this <tt>WeakArrayList</tt> instance to be the
	 * list's current size.  An application can use this operation to minimize
	 * the storage of an <tt>WeakArrayList</tt> instance.
	 */
	public void trimToSize() {
		this.modCount++;
		final int oldCapacity = this.data.length;
		if (this.size < oldCapacity) {
			this.data = Arrays.copyOf(this.data, this.size);
		}
	}

	/** Clean the references that was released.
	 *
	 * <p>Notifies the listeners if a reference was released.
	 *
	 * @return the size
	 */
	@SuppressWarnings("unchecked")
	public int expurge() {
		// clear out ref queue.
		while (this.queue.poll() != null) {
			this.enquedElement = true;
		}

		int j;

		if (this.enquedElement) {
			// Clear the table
			Reference<? extends T> ref;
			j = 0;
			for (int i = 0; i < this.size; ++i) {
				ref = (Reference<T>) this.data[i];
				if ((ref == null) || (ref.isEnqueued()) || (ref.get() == null)) {
					if (ref != null) {
						ref.clear();
					}
					this.data[i] = null;
				} else {
					if (i != j) {
						this.data[j] = this.data[i];
						this.data[i] = null;
					}
					j++;
				}
			}
			this.enquedElement = false;
		} else {
			j = this.size;
		}

		// Allocation of array may have caused GC, which may have caused
		// additional entries to go stale.  Removing these entries from the
		// reference queue will make them eligible for reclamation.
		while (this.queue.poll() != null) {
			this.enquedElement = true;
		}

		final int oldSize = this.size;
		this.size = j;

		if (j < oldSize) {
			fireReferenceRelease(oldSize - j);
		}

		return this.size;
	}

	/** Verify if the specified index is inside the array.
	 *
	 * @param index is the index totest
	 * @param allowLast indicates if the last elements is assumed to be valid or not.
	 */
	protected void assertRange(int index, boolean allowLast) {
		final int csize = expurge();
		if (index < 0) {
			throw new IndexOutOfBoundsException(Locale.getString("E1", index)); //$NON-NLS-1$
		}
		if (allowLast && (index > csize)) {
			throw new IndexOutOfBoundsException(Locale.getString("E2", csize, index)); //$NON-NLS-1$
		}
		if (!allowLast && (index >= csize)) {
			throw new IndexOutOfBoundsException(Locale.getString("E3", csize, index)); //$NON-NLS-1$
		}
	}

	@Pure
	@Override
	public int size() {
		return expurge();
	}

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	public T get(int index) {
		T value;
		do {
			assertRange(index, false);
			value = ((Reference<T>) this.data[index]).get();
		}
		while (value == null);
		return unmaskNull(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T set(int index, T element) {
		T oldValue;
		Reference<T> ref;
		do {
			assertRange(index, false);
			ref = (Reference<T>) this.data[index];
			oldValue = ref.get();
		}
		while (oldValue == null);
		ref.clear();
		this.data[index] = createRef(element);
		this.modCount++;
		return unmaskNull(oldValue);
	}

	@Override
	public void add(int index, T element) {
		assertRange(index, true);
		ensureCapacity(this.size + 1);
		System.arraycopy(this.data, index, this.data, index + 1, this.size - index);
		this.data[index] = createRef(element);
		this.size++;
		this.modCount++;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T remove(int index) {
		T oldValue;
		Reference<T> ref;
		do {
			assertRange(index, false);
			ref = (Reference<T>) this.data[index];
			oldValue = ref.get();
		}
		while (oldValue == null);
		ref.clear();
		System.arraycopy(this.data, index + 1, this.data, index, this.size - index - 1);
		this.data[this.size - 1] = null;
		this.size--;
		this.modCount++;
		return unmaskNull(oldValue);
	}

	/** Add listener on reference's release.
	 *
	 * @param listener the listener.
	 */
	public void addReferenceListener(ReferenceListener listener) {
		if (this.listeners == null) {
			this.listeners = new LinkedList<>();
		}
		final List<ReferenceListener> list = this.listeners;
		synchronized (list) {
			list.add(listener);
		}
	}

	/** Remove listener on reference's release.
	 *
	 * @param listener the listener.
	 */
	public void removeReferenceListener(ReferenceListener listener) {
		final List<ReferenceListener> list = this.listeners;
		if (list != null) {
			synchronized (list) {
				list.remove(listener);
				if (list.isEmpty()) {
					this.listeners = null;
				}
			}
		}
	}

	/**
	 * Fire the reference release event.
	 *
	 * @param released is the count of released objects.
	 */
	protected void fireReferenceRelease(int released) {
		final List<ReferenceListener> list = this.listeners;
		if (list != null && !list.isEmpty()) {
			for (final ReferenceListener listener : list) {
				listener.referenceReleased(released);
			}
		}
	}

}
