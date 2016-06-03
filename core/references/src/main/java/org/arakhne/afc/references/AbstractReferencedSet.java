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
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A <tt>Set</tt> implementation with {@link SoftReference soft values}
 * or {@link WeakReference weak values}. An entry in a
 * <tt>AbstractReferencedSet</tt> will automatically be removed when its value is no
 * longer in ordinary use or <code>null</code>.
 *
 * <p>This abstract implementation does not decide if the map is based on a tree or
 * on a hashtable.
 *
 * @param <E> is the type of the values.
 * @param <R> is the type of the references.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.9
 */
public abstract class AbstractReferencedSet<E, R extends Reference<E>> extends AbstractSet<E> {

	private boolean autoExpurge;

	private final ReferenceQueue<E> queue = new ReferenceQueue<>();

	private final Class<? super R> referenceType;

	private final Set<R> theSet;

	/**
	 * @param theSet is the internal data structure to use.
	 * @param referenceType is the type of the references.
	 */
	public AbstractReferencedSet(Set<R> theSet, Class<? super R> referenceType) {
		assert theSet != null;
		assert referenceType != null;
		this.theSet = theSet;
		this.referenceType = referenceType;
	}

	/** Create a reference on the given object.
	 *
	 * @param element is the element to wrap into a reference
	 * @return the reference of the given element.
	 */
	protected abstract R createReference(E element);

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
	 *     are released from the moemory, otherwise <code>false</code>
	 */
	@Pure
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
		Reference<? extends E> obj;
		while ((obj = this.queue.poll()) != null) {
			obj.clear();
			if (this.referenceType.isInstance(obj)) {
				this.theSet.remove(this.referenceType.cast(obj));
			}
		}
	}

	/** Clean the references that was released.
	 */
	public final void expurge() {
		Reference<? extends E> obj;

		final Iterator<R> iter = this.theSet.iterator();
		R reference;
		while (iter.hasNext()) {
			reference = iter.next();
			if (reference != null
					&& ((reference.isEnqueued()) || (reference.get() == null))) {
				reference.enqueue();
				reference.clear();
			}
		}
		reference = null;

		while ((obj = this.queue.poll()) != null) {
			obj.clear();
			if (this.referenceType.isInstance(obj)) {
				this.theSet.remove(this.referenceType.cast(obj));
			}
		}
	}

	@Pure
	@Override
	public final boolean equals(Object obj) {
		expurgeNow();
		return super.equals(obj);
	}

	@Pure
	@Override
	public final int hashCode() {
		expurgeNow();
		return super.hashCode();
	}

	@Pure
	@Override
	public Iterator<E> iterator() {
		expurgeNow();
		return new InnerIterator();
	}

	@Pure
	@Override
	public int size() {
		expurgeNow();
		return this.theSet.size();
	}

	@Override
	public boolean add(E value) {
		return this.theSet.add(createReference(value));
	}

	/** Iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 5.9
	 */
	private class InnerIterator implements Iterator<E> {

		private final Iterator<R> originalIterator;

		private E next;

		private boolean nextSearchProceeded;

		private boolean enableRemove;

		/** Construct the iterator.
		 */
		@SuppressWarnings("synthetic-access")
		InnerIterator() {
			this.originalIterator = AbstractReferencedSet.this.theSet.iterator();
		}

		private void searchNext() {
			if (!this.nextSearchProceeded) {
				this.nextSearchProceeded = true;
				this.next = null;
				R originalNext;
				E wvalue;
				while (this.next == null && this.originalIterator.hasNext()) {
					originalNext = this.originalIterator.next();
					if (originalNext != null) {
						wvalue = originalNext.get();
						if (wvalue != null) {
							this.next = wvalue;
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
		public E next() {
			searchNext();
			assert this.nextSearchProceeded;
			final E cnext = this.next;

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

	/** Comparator wrapper.
	 *
	 * @param <E> is the type of the values.
	 * @param <R> is the type of the references.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 5.9
	 */
	protected static class ReferenceComparator<E, R extends Reference<E>> implements Comparator<R> {

		private final Comparator<? super E> comparator;

		/** Construct a comparator reference.
		 *
		 * @param comparator the comparator.
		 */
		public ReferenceComparator(Comparator<? super E> comparator) {
			assert comparator != null;
			this.comparator = comparator;
		}

		@Override
		public int compare(R o1, R o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MIN_VALUE;
			}
			if (o2 == null) {
				return Integer.MAX_VALUE;
			}
			return this.comparator.compare(o1.get(), o2.get());
		}

	}

}
