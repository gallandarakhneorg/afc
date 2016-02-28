/* 
 * $Id$
 * 
 * Copyright (C) 2005-2009 Stephane GALLAND.
 * Copyright (C) 2011-12 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
 * <p>
 * This abstract implementation does not decide if the map is based on a tree or on a hashtable;
 * it does not impose soft or weak references.
 *
 * @param <K> is the type of the keys.
 * @param <V> is the type of the values.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.8
 */
public abstract class AbstractReferencedValueMap<K,V> extends AbstractMap<K,V> {

	/** Defines the NULL object inside a WeakValueMap.
	 * 
	 * @see #maskNull(Object)
	 */
	protected static final Object NULL_VALUE = new Object();

	/** Mask the null values given by the used of this map.
	 * <p>
	 * This method replaces the <code>null</code> value by
	 * the internal representation {@link #NULL_VALUE}.
	 *
	 * @param <MV> is the type of the value.
	 * @param value is the value given by the user of this map.
	 * @return the internal representation of the value.
	 * @see #unmaskNull(Object)
	 */
	@SuppressWarnings("unchecked")
	protected static <MV> MV maskNull(MV value) {
		return (value==null) ? (MV)NULL_VALUE : value;
	}

	/** Unmask the null values given by the used of this map.
	 * <p>
	 * This method replaces the internal representation
	 * {@link #NULL_VALUE} of null values by its user representation
	 * <code>null</code>.
	 * 
	 * @param <MV> is the type of the value.
	 * @param value is the value given by the user of this map.
	 * @return the internal representation of the value.
	 * @see #maskNull(Object)
	 */
	protected static <MV> MV unmaskNull(MV value) {
		return (value==NULL_VALUE) ? null : value;
	}

	/**
	 * Reallocates the array being used within toArray when the iterator
	 * returned more elements than expected, and finishes filling it from
	 * the iterator.
	 *
	 * @param r the array, replete with previously stored elements
	 * @param it the in-progress iterator over this collection
	 * @return array containing the elements in the given array, plus any
	 *         further elements returned by the iterator, trimmed to size
	 */
	@SuppressWarnings("unchecked")
	static <T> T[] finishToArray(T[] r, Iterator<?> it) {
		T[] rp = r;
		int i = rp.length;
		while (it.hasNext()) {
			int cap = rp.length;
			if (i == cap) {
				int newCap = ((cap / 2) + 1) * 3;
				if (newCap <= cap) { // integer overflow
					if (cap == Integer.MAX_VALUE)
						throw new OutOfMemoryError("Required array size too large"); //$NON-NLS-1$
					newCap = Integer.MAX_VALUE;
				}
				rp = Arrays.copyOf(rp, newCap);
			}
			rp[++i] = (T)it.next();
		}
		// trim if overallocated
		return (i == rp.length) ? rp : Arrays.copyOf(rp, i);
	}

	private boolean autoExpurge = false;
	private final ReferenceQueue<V> queue = new ReferenceQueue<>();

	/** Internal map.
	 */
	protected final Map<K,ReferencableValue<K,V>> map;

	/**
	 * Constructs an empty <tt>Map</tt>.
	 *
	 * @param  map is the map instance to use to store the entries.
	 * @throws IllegalArgumentException if the initial capacity is negative
	 *         or the load factor is nonpositive
	 */
	public AbstractReferencedValueMap(Map<K,ReferencableValue<K,V>> map) {
		this.map = map;
	}

	/** Clean the references that was marked as released inside
	 * the queue.
	 */
	protected final void expurgeNow() {
		if (this.autoExpurge)
			expurge();
		else
			expurgeQueuedReferences();
	}

	/** Replies if this map expurge all the released references
	 * even if they are not enqueued by the virtual machine
	 * 
	 * @return <code>true</code> is the values are deeply expurged when they
	 * are released from the moemory, otherwise <code>false</code>
	 */
	public final boolean isDeeplyExpurge() {
		return this.autoExpurge;
	}

	/** Set if this map expurge all the released references
	 * even if they are not enqueued by the virtual machine
	 * 
	 * @param deeplyExpurge must be <code>true</code> to
	 * expurge all the released values, otherwise <code>false</code>
	 * to expurge only the enqueued values.
	 * @return the old value of this flag
	 */
	public final boolean setDeeplyExpurge(boolean deeplyExpurge) {
		boolean old = this.autoExpurge;
		this.autoExpurge = deeplyExpurge;
		return old;
	}

	/** Clean the references that was marked as released inside
	 * the queue.
	 */
	public final void expurgeQueuedReferences() {
		Reference<? extends V> o;
		while((o = this.queue.poll()) != null) {
			if (o instanceof ReferencableValue<?,?>) {
				this.map.remove(((ReferencableValue<?,?>)o).getKey());
			}
			o.clear();
		}
	}

	/** Clean the references that was released.
	 */
	public final void expurge() {
		Reference<? extends V> o;

		Iterator<Entry<K,ReferencableValue<K,V>>> iter = this.map.entrySet().iterator();
		Entry<K,ReferencableValue<K,V>> entry;
		ReferencableValue<K,V> value;
		while (iter.hasNext()) {
			entry = iter.next();
			if (entry!=null) {
				value = entry.getValue();
				if ((value!=null)&&
						((value.isEnqueued())||(value.get()==null))) {
					value.enqueue();
					value.clear();
				}
			}
		}
		entry = null;
		value = null;

		while((o = this.queue.poll()) != null) {
			if (o instanceof ReferencableValue<?,?>) {
				this.map.remove(((ReferencableValue<?,?>)o).getKey());
			}
			o.clear();
		}
	}

	/** Create a storage object that permits to put the specified
	 * elements inside this map.
	 * 
	 * @param k is the key associated to the value
	 * @param v is the value
	 * @param refQueue is the reference queue to use
	 * @return the new storage object
	 */
	protected abstract ReferencableValue<K,V> makeValue(K k, V v, ReferenceQueue<V> refQueue);

	/** Create a storage object that permits to put the specified
	 * elements inside this map.
	 * 
	 * @param k is the key associated to the value
	 * @param v is the value
	 * @return the new storage object
	 */
	protected final ReferencableValue<K,V> makeValue(K k, V v) {
		return makeValue(k, v, this.queue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final V put(K key, V value) {
		expurgeNow();
		ReferencableValue<K,V> ret = this.map.put(key, makeValue(key, value, this.queue));
		if(ret == null) return null;
		return ret.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Set<Entry<K,V>> entrySet() {
		expurgeNow();
		return new InnerEntrySet();
	}	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object o) {
		expurgeNow();
		return super.equals(o);
	}

	/**
	 * {@inheritDoc}
	 */
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
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static interface ReferencableValue<K,V> extends Entry<K,V> {

		/**
		 * @return if the value is enqueued into a reference queue.
		 */
		public boolean isEnqueued();

		/**
		 * @return the weak/soft reference.
		 */
		public V get();

		/**
		 * @return if the value was enqueued
		 */
		public boolean enqueue();

		/**
		 */
		public void clear();

	} // interface ReferencableValue

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class InnerEntrySet implements Set<Entry<K,V>> {

		/**
		 */
		public InnerEntrySet() {
			//
		}

		@Override
		public final boolean add(java.util.Map.Entry<K, V> e) {
			K key = e.getKey();
			V value = e.getValue();
			return AbstractReferencedValueMap.this.map.put(key, makeValue(key, value)) == null;
		}

		@Override
		public final boolean addAll(Collection<? extends java.util.Map.Entry<K, V>> c) {
			boolean changed = true;
			for(java.util.Map.Entry<K, V> entry : c) {
				changed = add(entry) | changed;
			}
			return changed;
		}

		@Override
		public final void clear() {
			AbstractReferencedValueMap.this.map.clear();
		}

		@Override
		public final boolean contains(Object o) {
			if (o instanceof Entry<?,?>) {
				try {
					expurgeNow();
					return AbstractReferencedValueMap.this.map.containsKey(((Entry<?,?>)o).getKey());
				}
				catch(AssertionError e) {
					throw e;
				}
				catch(Throwable exception) {
					//
				}
			}
			return false;
		}

		@Override
		public final boolean containsAll(Collection<?> c) {
			boolean ok;
			expurgeNow();
			for(Object o : c) {
				ok = false;
				if (o instanceof Entry<?,?>) {
					try {
						ok = AbstractReferencedValueMap.this.map.containsKey(((Entry<?,?>)o).getKey());
					}
					catch(AssertionError e) {
						throw e;
					}
					catch(Throwable exception) {
						//
					}
				}
				if (!ok) return false;
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
		public final boolean remove(Object o) {
			if (o instanceof Entry<?,?>) {
				try {
					return AbstractReferencedValueMap.this.map.remove(((Entry<?,?>)o).getKey())!=null;
				}
				catch(AssertionError e) {
					throw e;
				}
				catch(Throwable exception) {
					//
				}
			}
			return false;
		}

		@Override
		public final boolean removeAll(Collection<?> c) {
			boolean changed = true;
			for(Object o : c) {
				changed = remove(o) || changed;
			}
			return changed;
		}

		@Override
		public final boolean retainAll(Collection<?> c) {
			expurgeNow();
			Collection<K> keys = AbstractReferencedValueMap.this.map.keySet();
			Iterator<K> iterator = keys.iterator();
			K key;
			boolean changed = false;
			while (iterator.hasNext()) {
				key = iterator.next();
				if (!c.contains(key)) {
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
			Object[] tab = new Object[AbstractReferencedValueMap.this.map.size()];
			return toArray(tab);
		}

		@SuppressWarnings("unchecked")
		@Override
		public final <T> T[] toArray(T[] a) {
			expurgeNow();
			// Estimate size of array; be prepared to see more or fewer elements
			int size = AbstractReferencedValueMap.this.map.size();
			T[] r = a.length >= size ? a :
				(T[])Array.newInstance(a.getClass().getComponentType(), size);
			Iterator<Entry<K,V>> it = iterator();

			for (int i=0; i<r.length; ++i) {
				if (!it.hasNext()) { // fewer elements than expected
					if (a != r) {
						return Arrays.copyOf(r, i);
					}
					r[i] = null; // null-terminate
					return r;
				}
				r[i] = (T)it.next();
			}
			return it.hasNext() ? finishToArray(r, it) : r;
		}

	} // class InnerEntrySet

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class InnerIterator implements Iterator<Entry<K,V>> {

		private final Iterator<Entry<K,ReferencableValue<K,V>>> originalIterator;
		private Entry<K,V> next = null;
		private boolean nextSearchProceeded = false;
		private boolean enableRemove = false;

		public InnerIterator() {
			this.originalIterator = AbstractReferencedValueMap.this.map.entrySet().iterator();
		}

		private void searchNext() {
			if (!this.nextSearchProceeded) {
				this.nextSearchProceeded = true;
				this.next = null;
				Entry<K,ReferencableValue<K,V>> originalNext;
				ReferencableValue<K,V> wValue;
				while (this.next==null && this.originalIterator.hasNext()) {
					originalNext = this.originalIterator.next();
					if (originalNext!=null) {
						wValue = originalNext.getValue();
						if (wValue!=null) {
							this.next = new InnerEntry(
									originalNext.getKey(),
									wValue.getValue(),
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
			assert(this.nextSearchProceeded);
			this.enableRemove = false;
			return this.next!=null;
		}

		@Override
		public java.util.Map.Entry<K, V> next() {
			searchNext();
			assert(this.nextSearchProceeded);
			Entry<K,V> cnext = this.next;

			// Reset the research flags
			this.next = null;
			this.nextSearchProceeded = false;
			this.enableRemove = true;

			return cnext;
		}

		@Override
		public void remove() {
			if (!this.enableRemove)
				throw new IllegalStateException("you must not invoke the remove function between hasNext and next functions."); //$NON-NLS-1$
			this.originalIterator.remove();
		}

	} // class InnerIterator

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class InnerEntry implements Entry<K,V> {

		private final Entry<K,ReferencableValue<K,V>> original;
		private final K key;
		private V value;

		public InnerEntry(K k, V v, Entry<K,ReferencableValue<K,V>> o) {
			this.original = o;
			this.key = k;
			this.value = v;
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

	} // class InnerEntry

	/**
	 * Value stored in a {@link AbstractReferencedValueMap} inside a {@link SoftReference}.
	 * 
	 * @param <VK> is the type of the key associated to the value.
	 * @param <VV> is the type of the value.
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class SoftReferencedValue<VK,VV> extends SoftReference<VV> implements ReferencableValue<VK,VV> {

		private final VK k;

		/**
		 * @param k is the key.
		 * @param v is the value.
		 * @param queue is the memory-release listener.
		 */
		public SoftReferencedValue(VK k, VV v, ReferenceQueue<VV> queue) {
			super(maskNull(v), queue);
			this.k = k;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append('{');
			VK key = getKey();
			buffer.append(key==null ? null : key.toString());
			buffer.append('=');
			if (isEnqueued()) {
				buffer.append("Q#"); //$NON-NLS-1$
			}
			else {
				buffer.append("P#"); //$NON-NLS-1$
			}
			VV v = getValue();
			buffer.append((v==null ? null : v.toString()));
			buffer.append('}');
			return buffer.toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VK getKey() {
			return this.k;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VV getValue() {
			return unmaskNull(get());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VV setValue(VV o) {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public int hashCode() { 
			Object val = getValue();
			return (getKey()==null   ? 0 : getKey().hashCode()) ^
					(val==null ? 0 : val.hashCode()); 
		}

		/**
		 * {@inheritDoc}
		 *
		 * @param o {@inheritDoc}
		 * @return {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object o) {
			if (o instanceof Entry) {
				Entry<VK,VV> e = (Entry<VK,VV>)o;
				Object e1val = getValue();
				Object e2val = e.getValue();
				return  (getKey()==null ?
						e.getKey()==null : getKey().equals(e.getKey()))  &&
						(e1val==null ? e2val==null : e1val.equals(e2val));
			}
			return false;
		}

	} // class SoftReferencedValue

	/**
	 * Value stored in a {@link AbstractReferencedValueMap} inside a {@link WeakReference}.
	 * 
	 * @param <VK> is the type of the key associated to the value.
	 * @param <VV> is the type of the value.
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class WeakReferencedValue<VK,VV> extends WeakReference<VV> implements ReferencableValue<VK,VV> {

		private final VK k;

		/**
		 * @param k is the key.
		 * @param v is the value.
		 * @param queue is the memory-release listener.
		 */
		public WeakReferencedValue(VK k, VV v, ReferenceQueue<VV> queue) {
			super(maskNull(v), queue);
			this.k = k;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append('{');
			VK key = getKey();
			buffer.append(key==null ? null : key.toString());
			buffer.append('=');
			if (isEnqueued()) {
				buffer.append("Q#"); //$NON-NLS-1$
			}
			else {
				buffer.append("P#"); //$NON-NLS-1$
			}
			VV v = getValue();
			buffer.append((v==null ? null : v.toString()));
			buffer.append('}');
			return buffer.toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VK getKey() {
			return this.k;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VV getValue() {
			return unmaskNull(get());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VV setValue(VV o) {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public int hashCode() { 
			Object val = getValue();
			return (getKey()==null   ? 0 : getKey().hashCode()) ^
					(val==null ? 0 : val.hashCode()); 
		}

		/**
		 * {@inheritDoc}
		 *
		 * @param o {@inheritDoc}
		 * @return {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object o) {
			if (o instanceof Entry) {
				Entry<VK,VV> e = (Entry<VK,VV>)o;
				Object e1val = getValue();
				Object e2val = e.getValue();
				return  (getKey()==null ?
						e.getKey()==null : getKey().equals(e.getKey()))  &&
						(e1val==null ? e2val==null : e1val.equals(e2val));
			}
			return false;
		}

	} // class WeakReferencedValue

	/**
	 * Value stored in a {@link AbstractReferencedValueMap} inside a {@link PhantomReference}.
	 * 
	 * @param <VK> is the type of the key associated to the value.
	 * @param <VV> is the type of the value.
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class PhantomReferencedValue<VK,VV> extends PhantomReference<VV> implements ReferencableValue<VK,VV> {

		private final VK k;

		/**
		 * @param k is the key.
		 * @param v is the value.
		 * @param queue is the memory-release listener.
		 */
		public PhantomReferencedValue(VK k, VV v, ReferenceQueue<VV> queue) {
			super(maskNull(v), queue);
			this.k = k;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append('{');
			VK key = getKey();
			buffer.append(key==null ? null : key.toString());
			buffer.append('=');
			if (isEnqueued()) {
				buffer.append("Q#"); //$NON-NLS-1$
			}
			else {
				buffer.append("P#"); //$NON-NLS-1$
			}
			VV v = getValue();
			buffer.append((v==null ? null : v.toString()));
			buffer.append('}');
			return buffer.toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VK getKey() {
			return this.k;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VV getValue() {
			return unmaskNull(get());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VV setValue(VV o) {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @return {@inheritDoc}
		 */
		@Override
		public int hashCode() { 
			Object val = getValue();
			return (getKey()==null   ? 0 : getKey().hashCode()) ^
					(val==null ? 0 : val.hashCode()); 
		}

		/**
		 * {@inheritDoc}
		 *
		 * @param o {@inheritDoc}
		 * @return {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object o) {
			if (o instanceof Entry) {
				Entry<VK,VV> e = (Entry<VK,VV>)o;
				Object e1val = getValue();
				Object e2val = e.getValue();
				return  (getKey()==null ?
						e.getKey()==null : getKey().equals(e.getKey()))  &&
						(e1val==null ? e2val==null : e1val.equals(e2val));
			}
			return false;
		}

	} // class PhantomReferencedValue

}
