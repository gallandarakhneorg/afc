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

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A <tt>Map</tt> implementation with <em>weak/soft values</em>. An entry in a
 * <tt>AbstractReferencedValueMap</tt> will automatically be removed when its value is no
 * longer in ordinary use or <code>null</code>.
 *
 * <p>This abstract implementation does not decide if the map is based on a tree or on a hashtable;
 * it does not impose soft or weak references.
 *
 * @param <K> is the type of the keys.
 * @param <V> is the type of the values.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.8
 */
public abstract class AbstractReferencedValueMap<K, V> extends AbstractMap<K, V> {

	/** Defines the NULL object inside a WeakValueMap.
	 *
	 * @see #maskNull(Object)
	 */
	protected static final Object NULL_VALUE = new Object();

	/** Internal map.
	 */
	protected final Map<K, ReferencableValue<K, V>> map;

	private boolean autoExpurge;

	private final ReferenceQueue<V> queue = new ReferenceQueue<>();

	/**
	 * Constructs an empty <tt>Map</tt>.
	 *
	 * @param  map is the map instance to use to store the entries.
	 * @throws IllegalArgumentException if the initial capacity is negative
	 *         or the load factor is nonpositive
	 */
	public AbstractReferencedValueMap(Map<K, ReferencableValue<K, V>> map) {
		this.map = map;
	}

	/** Mask the null values given by the used of this map.
	 *
	 * <p>This method replaces the <code>null</code> value by
	 * the internal representation {@link #NULL_VALUE}.
	 *
	 * @param <VALUET> is the type of the value.
	 * @param value is the value given by the user of this map.
	 * @return the internal representation of the value.
	 * @see #unmaskNull(Object)
	 */
	@SuppressWarnings("unchecked")
	protected static <VALUET> VALUET maskNull(VALUET value) {
		return (value == null) ? (VALUET) NULL_VALUE : value;
	}

	/** Unmask the null values given by the used of this map.
	 *
	 * <p>This method replaces the internal representation
	 * {@link #NULL_VALUE} of null values by its user representation
	 * <code>null</code>.
	 *
	 * @param <VALUET> is the type of the value.
	 * @param value is the value given by the user of this map.
	 * @return the internal representation of the value.
	 * @see #maskNull(Object)
	 */
	protected static <VALUET> VALUET unmaskNull(VALUET value) {
		return (value == NULL_VALUE) ? null : value;
	}

	/**
	 * Reallocates the array being used within toArray when the iterator
	 * returned more elements than expected, and finishes filling it from
	 * the iterator.
	 *
	 * @param <T> the type of the elements in the array.
	 * @param array the array, replete with previously stored elements
	 * @param it the in-progress iterator over this collection
	 * @return array containing the elements in the given array, plus any
	 *         further elements returned by the iterator, trimmed to size
	 */
	@SuppressWarnings("unchecked")
	static <T> T[] finishToArray(T[] array, Iterator<?> it) {
		T[] rp = array;
		int i = rp.length;
		while (it.hasNext()) {
			final int cap = rp.length;
			if (i == cap) {
				int newCap = ((cap / 2) + 1) * 3;
				if (newCap <= cap) {
					// integer overflow
					if (cap == Integer.MAX_VALUE) {
						throw new OutOfMemoryError("Required array size too large"); //$NON-NLS-1$
					}
					newCap = Integer.MAX_VALUE;
				}
				rp = Arrays.copyOf(rp, newCap);
			}
			rp[++i] = (T) it.next();
		}
		// trim if overallocated
		return (i == rp.length) ? rp : Arrays.copyOf(rp, i);
	}

	/** Clean the references that was marked as released inside
	 * the queue.
	 */
	protected final void expurgeNow() {
		if (this.autoExpurge) {
			expurge();
		} else {
			expurgeQueuedReferences();
		}
	}

	/** Replies if this map expurge all the released references
	 * even if they are not enqueued by the virtual machine.
	 *
	 * @return <code>true</code> is the values are deeply expurged when they
	 *     are released from the moemory, otherwise <code>false</code>.
	 */
	public final boolean isDeeplyExpurge() {
		return this.autoExpurge;
	}

	/** Set if this map expurge all the released references
	 * even if they are not enqueued by the virtual machine.
	 *
	 * @param deeplyExpurge must be <code>true</code> to
	 *     expurge all the released values, otherwise <code>false</code>
	 *     to expurge only the enqueued values.
	 * @return the old value of this flag
	 */
	public final boolean setDeeplyExpurge(boolean deeplyExpurge) {
		final boolean old = this.autoExpurge;
		this.autoExpurge = deeplyExpurge;
		return old;
	}

	/** Clean the references that was marked as released inside
	 * the queue.
	 */
	public final void expurgeQueuedReferences() {
		Reference<? extends V> reference;
		while ((reference = this.queue.poll()) != null) {
			if (reference instanceof ReferencableValue<?, ?>) {
				this.map.remove(((ReferencableValue<?, ?>) reference).getKey());
			}
			reference.clear();
		}
	}

	/** Clean the references that was released.
	 */
	public final void expurge() {
		final Iterator<Entry<K, ReferencableValue<K, V>>> iter = this.map.entrySet().iterator();
		Entry<K, ReferencableValue<K, V>> entry;
		ReferencableValue<K, V> value;
		while (iter.hasNext()) {
			entry = iter.next();
			if (entry != null) {
				value = entry.getValue();
				if ((value != null)
						&& ((value.isEnqueued()) || (value.get() == null))) {
					value.enqueue();
					value.clear();
				}
			}
		}
		entry = null;
		value = null;
		Reference<? extends V> reference;
		while ((reference = this.queue.poll()) != null) {
			if (reference instanceof ReferencableValue<?, ?>) {
				this.map.remove(((ReferencableValue<?, ?>) reference).getKey());
			}
			reference.clear();
		}
	}

	/** Create a storage object that permits to put the specified
	 * elements inside this map.
	 *
	 * @param key is the key associated to the value
	 * @param value is the value
	 * @param refQueue is the reference queue to use
	 * @return the new storage object
	 */
	protected abstract ReferencableValue<K, V> makeValue(K key, V value, ReferenceQueue<V> refQueue);

	/** Create a storage object that permits to put the specified
	 * elements inside this map.
	 *
	 * @param key is the key associated to the value
	 * @param value is the value
	 * @return the new storage object
	 */
	protected final ReferencableValue<K, V> makeValue(K key, V value) {
		return makeValue(key, value, this.queue);
	}

	@Override
	public final V put(K key, V value) {
		expurgeNow();
		final ReferencableValue<K, V> ret = this.map.put(key, makeValue(key, value, this.queue));
		if (ret == null) {
			return null;
		}
		return ret.getValue();
	}

	@Override
	public final Set<Entry<K, V>> entrySet() {
		expurgeNow();
		return new InnerEntrySet();
	}

	@Override
	public final boolean equals(Object obj) {
		expurgeNow();
		return super.equals(obj);
	}

	@Override
	public final int hashCode() {
		expurgeNow();
		return super.hashCode();
	}

	/**
	 * This interface provides information about the pairs inside
	 * a map with weak/soft reference values.
	 *
	 * @param <K> is the type of the map keys.
	 * @param <V> is the type of the map values.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected interface ReferencableValue<K, V> extends Entry<K, V> {

		/**
		 * @return if the value is enqueued into a reference queue.
		 */
		boolean isEnqueued();

		/**
		 * @return the weak/soft reference.
		 */
		V get();

		/** Enqueue the value.
		 *
		 * @return if the value was enqueued
		 */
		boolean enqueue();

		/** Clear the reference.
		 */
		void clear();

	}

	/** Internal implementation of a set.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class InnerEntrySet implements Set<Entry<K, V>> {

		/** Constructor.
		 */
		InnerEntrySet() {
			//
		}

		@Override
		public final boolean add(java.util.Map.Entry<K, V> element) {
			final K key = element.getKey();
			final V value = element.getValue();
			return AbstractReferencedValueMap.this.map.put(key, makeValue(key, value)) == null;
		}

		@Override
		public final boolean addAll(Collection<? extends java.util.Map.Entry<K, V>> collection) {
			boolean changed = true;
			for (final java.util.Map.Entry<K, V> entry : collection) {
				changed = add(entry) | changed;
			}
			return changed;
		}

		@Override
		public final void clear() {
			AbstractReferencedValueMap.this.map.clear();
		}

		@Override
		public final boolean contains(Object element) {
			if (element instanceof Entry<?, ?>) {
				try {
					expurgeNow();
					return AbstractReferencedValueMap.this.map.containsKey(((Entry<?, ?>) element).getKey());
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable exception) {
					//
				}
			}
			return false;
		}

		@Override
		public final boolean containsAll(Collection<?> collection) {
			boolean ok;
			expurgeNow();
			for (final Object o : collection) {
				ok = false;
				if (o instanceof Entry<?, ?>) {
					try {
						ok = AbstractReferencedValueMap.this.map.containsKey(((Entry<?, ?>) o).getKey());
					} catch (AssertionError e) {
						throw e;
					} catch (Throwable exception) {
						//
					}
				}
				if (!ok) {
					return false;
				}
			}
			return true;
		}

		@Override
		public final boolean isEmpty() {
			expurgeNow();
			return AbstractReferencedValueMap.this.map.isEmpty();
		}

		@Override
		public final Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new InnerIterator();
		}

		@Override
		public final boolean remove(Object element) {
			if (element instanceof Entry<?, ?>) {
				try {
					return AbstractReferencedValueMap.this.map.remove(((Entry<?, ?>) element).getKey()) != null;
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable exception) {
					//
				}
			}
			return false;
		}

		@Override
		public final boolean removeAll(Collection<?> collection) {
			boolean changed = true;
			for (final Object o : collection) {
				changed = remove(o) || changed;
			}
			return changed;
		}

		@Override
		public final boolean retainAll(Collection<?> collection) {
			expurgeNow();
			final Collection<K> keys = AbstractReferencedValueMap.this.map.keySet();
			final Iterator<K> iterator = keys.iterator();
			K key;
			boolean changed = false;
			while (iterator.hasNext()) {
				key = iterator.next();
				if (!collection.contains(key)) {
					iterator.remove();
					changed = true;
				}
			}
			return changed;
		}

		@Override
		public final int size() {
			expurgeNow();
			return AbstractReferencedValueMap.this.map.size();
		}

		@Override
		public final Object[] toArray() {
			expurgeNow();
			final Object[] tab = new Object[AbstractReferencedValueMap.this.map.size()];
			return toArray(tab);
		}

		@SuppressWarnings("unchecked")
		@Override
		public final <T> T[] toArray(T[] array) {
			expurgeNow();
			// Estimate size of array; be prepared to see more or fewer elements
			final int size = AbstractReferencedValueMap.this.map.size();
			final T[] r = array.length >= size ? array
				: (T[]) Array.newInstance(array.getClass().getComponentType(), size);
			final Iterator<Entry<K, V>> it = iterator();

			for (int i = 0; i < r.length; ++i) {
				if (!it.hasNext()) {
					// fewer elements than expected
					if (array != r) {
						return Arrays.copyOf(r, i);
					}
					// null-terminate
					r[i] = null;
					return r;
				}
				r[i] = (T) it.next();
			}
			return it.hasNext() ? finishToArray(r, it) : r;
		}

	}

	/** Internal implementation of an iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class InnerIterator implements Iterator<Entry<K, V>> {

		private final Iterator<Entry<K, ReferencableValue<K, V>>> originalIterator;

		private Entry<K, V> next;

		private boolean nextSearchProceeded;

		private boolean enableRemove;

		InnerIterator() {
			this.originalIterator = AbstractReferencedValueMap.this.map.entrySet().iterator();
		}

		private void searchNext() {
			if (!this.nextSearchProceeded) {
				this.nextSearchProceeded = true;
				this.next = null;
				Entry<K, ReferencableValue<K, V>> originalNext;
				ReferencableValue<K, V> wvalue;
				while (this.next == null && this.originalIterator.hasNext()) {
					originalNext = this.originalIterator.next();
					if (originalNext != null) {
						wvalue = originalNext.getValue();
						if (wvalue != null) {
							this.next = new InnerEntry(
									originalNext.getKey(),
									wvalue.getValue(),
									originalNext);
							return;
						}
					}
					// Remove the original entry because the pointer was lost.
					this.originalIterator.remove();
				}
			}
		}

		@Override
		public boolean hasNext() {
			searchNext();
			assert this.nextSearchProceeded;
			this.enableRemove = false;
			return this.next != null;
		}

		@Override
		public java.util.Map.Entry<K, V> next() {
			searchNext();
			assert this.nextSearchProceeded;
			final Entry<K, V> cnext = this.next;

			// Reset the research flags
			this.next = null;
			this.nextSearchProceeded = false;
			this.enableRemove = true;

			return cnext;
		}

		@Override
		public void remove() {
			if (!this.enableRemove) {
				throw new IllegalStateException(
						"you must not invoke the remove function between hasNext and next functions."); //$NON-NLS-1$
			}
			this.originalIterator.remove();
		}

	}

	/** Internal implementation of a map entry.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class InnerEntry implements Entry<K, V> {

		private final Entry<K, ReferencableValue<K, V>> original;

		private final K key;

		private V value;

		InnerEntry(K key, V value, Entry<K, ReferencableValue<K, V>> original) {
			this.original = original;
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.value;
		}

		@Override
		public V setValue(V value) {
			this.value = value;
			return this.original.getValue().setValue(value);
		}

	}

	/**
	 * Value stored in a {@link AbstractReferencedValueMap} inside a {@link SoftReference}.
	 *
	 * @param <VKT> is the type of the key associated to the value.
	 * @param <VVT> is the type of the value.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class SoftReferencedValue<VKT, VVT> extends SoftReference<VVT> implements ReferencableValue<VKT, VVT> {

		private final VKT key;

		/**
		 * @param key is the key.
		 * @param value is the value.
		 * @param queue is the memory-release listener.
		 */
		public SoftReferencedValue(VKT key, VVT value, ReferenceQueue<VVT> queue) {
			super(maskNull(value), queue);
			this.key = key;
		}

		@Override
		public String toString() {
			final StringBuilder buffer = new StringBuilder();
			buffer.append('{');
			final VKT key = getKey();
			buffer.append(key == null ? null : key.toString());
			buffer.append('=');
			if (isEnqueued()) {
				buffer.append("Q#"); //$NON-NLS-1$
			} else {
				buffer.append("P#"); //$NON-NLS-1$
			}
			final VVT v = getValue();
			buffer.append(v == null ? null : v.toString());
			buffer.append('}');
			return buffer.toString();
		}

		@Override
		public VKT getKey() {
			return this.key;
		}

		@Override
		public VVT getValue() {
			return unmaskNull(get());
		}

		@Override
		public VVT setValue(VVT value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int hashCode() {
			final Object val = getValue();
			return (getKey() == null   ? 0 : getKey().hashCode())
					^ (val == null ? 0 : val.hashCode());
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Entry) {
				final Entry<VKT, VVT> e = (Entry<VKT, VVT>) obj;
				final Object e1val = getValue();
				final Object e2val = e.getValue();
				return  (getKey() == null
						? e.getKey() == null
						: getKey().equals(e.getKey()))
						&& (e1val == null ? e2val == null : e1val.equals(e2val));
			}
			return false;
		}

	} // class SoftReferencedValue

	/**
	 * Value stored in a {@link AbstractReferencedValueMap} inside a {@link WeakReference}.
	 *
	 * @param <VKT> is the type of the key associated to the value.
	 * @param <VVT> is the type of the value.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class WeakReferencedValue<VKT, VVT> extends WeakReference<VVT> implements ReferencableValue<VKT, VVT> {

		private final VKT key;

		/**
		 * @param key is the key.
		 * @param value is the value.
		 * @param queue is the memory-release listener.
		 */
		public WeakReferencedValue(VKT key, VVT value, ReferenceQueue<VVT> queue) {
			super(maskNull(value), queue);
			this.key = key;
		}

		@Override
		public String toString() {
			final StringBuilder buffer = new StringBuilder();
			buffer.append('{');
			final VKT key = getKey();
			buffer.append(key == null ? null : key.toString());
			buffer.append('=');
			if (isEnqueued()) {
				buffer.append("Q#"); //$NON-NLS-1$
			} else {
				buffer.append("P#"); //$NON-NLS-1$
			}
			final VVT v = getValue();
			buffer.append(v == null ? null : v.toString());
			buffer.append('}');
			return buffer.toString();
		}

		@Override
		public VKT getKey() {
			return this.key;
		}

		@Override
		public VVT getValue() {
			return unmaskNull(get());
		}

		@Override
		public VVT setValue(VVT value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int hashCode() {
			final Object val = getValue();
			return (getKey() == null ? 0 : getKey().hashCode())
					^ (val == null ? 0 : val.hashCode());
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Entry) {
				final Entry<VKT, VVT> e = (Entry<VKT, VVT>) obj;
				final Object e1val = getValue();
				final Object e2val = e.getValue();
				return  (getKey() == null
						? e.getKey() == null : getKey().equals(e.getKey()))
						&& (e1val == null ? e2val == null : e1val.equals(e2val));
			}
			return false;
		}

	}

	/**
	 * Value stored in a {@link AbstractReferencedValueMap} inside a {@link PhantomReference}.
	 *
	 * @param <VKT> is the type of the key associated to the value.
	 * @param <VVT> is the type of the value.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class PhantomReferencedValue<VKT, VVT> extends PhantomReference<VVT> implements ReferencableValue<VKT, VVT> {

		private final VKT key;

		/**
		 * @param key is the key.
		 * @param value is the value.
		 * @param queue is the memory-release listener.
		 */
		public PhantomReferencedValue(VKT key, VVT value, ReferenceQueue<VVT> queue) {
			super(maskNull(value), queue);
			this.key = key;
		}

		@Override
		public String toString() {
			final StringBuilder buffer = new StringBuilder();
			buffer.append('{');
			final VKT key = getKey();
			buffer.append(key == null ? null : key.toString());
			buffer.append('=');
			if (isEnqueued()) {
				buffer.append("Q#"); //$NON-NLS-1$
			} else {
				buffer.append("P#"); //$NON-NLS-1$
			}
			final VVT v = getValue();
			buffer.append(v == null ? null : v.toString());
			buffer.append('}');
			return buffer.toString();
		}

		@Override
		public VKT getKey() {
			return this.key;
		}

		@Override
		public VVT getValue() {
			return unmaskNull(get());
		}

		@Override
		public VVT setValue(VVT value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int hashCode() {
			final Object val = getValue();
			return (getKey() == null ? 0 : getKey().hashCode())
					^ (val == null ? 0 : val.hashCode());
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Entry) {
				final Entry<VKT, VVT> e = (Entry<VKT, VVT>) obj;
				final Object e1val = getValue();
				final Object e2val = e.getValue();
				return  (getKey() == null
						? e.getKey() == null : getKey().equals(e.getKey()))
						&& (e1val == null ? e2val == null : e1val.equals(e2val));
			}
			return false;
		}

	}

}
