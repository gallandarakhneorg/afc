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

package org.arakhne.util.ref;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * A <tt>Set</tt> implementation with {@link SoftReference soft values}
 * or {@link WeakReference weak values}. An entry in a
 * <tt>AbstractReferencedSet</tt> will automatically be removed when its value is no
 * longer in ordinary use or <code>null</code>.
 * <p>
 * This abstract implementation does not decide if the map is based on a tree or
 * on a hashtable.
 *
 * @param <E> is the type of the values.
 * @param <R> is the type of the references.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.9
 */
public abstract class AbstractReferencedSet<E,R extends Reference<E>> extends AbstractSet<E> {

	private boolean autoExpurge = false;
	private final ReferenceQueue<E> queue = new ReferenceQueue<E>();

	private final Class<? super R> referenceType;
	private final Set<R> theSet;

	/**
	 * @param theSet is the internal data structure to use.
	 * @param referenceType is the type of the references.
	 */
	public AbstractReferencedSet(Set<R> theSet, Class<? super R> referenceType) {
		assert(theSet!=null);
		assert(referenceType!=null);
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
		Reference<? extends E> o;
		while((o = this.queue.poll()) != null) {
			o.clear();
			if (this.referenceType.isInstance(o)) {
				this.theSet.remove(this.referenceType.cast(o));
			}
		}
	}

	/** Clean the references that was released.
	 */
	public final void expurge() {
		Reference<? extends E> o;

		Iterator<R> iter = this.theSet.iterator();
		R reference;
		while (iter.hasNext()) {
			reference = iter.next();
			if (reference!=null &&
					((reference.isEnqueued())||(reference.get()==null))) {
				reference.enqueue();
				reference.clear();
			}
		}
		reference = null;

		while((o = this.queue.poll()) != null) {
			o.clear();
			if (this.referenceType.isInstance(o)) {
				this.theSet.remove(this.referenceType.cast(o));
			}
		}
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

	@Override
	public Iterator<E> iterator() {
		expurgeNow();
		return new InnerIterator();
	}

	@Override
	public int size() {
		expurgeNow();
		return this.theSet.size();
	}
	
	@Override
	public boolean add(E e) {
		return this.theSet.add(createReference(e));
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 5.9
	 */
	private class InnerIterator implements Iterator<E> {

		private final Iterator<R> originalIterator;
		private E next = null;
		private boolean nextSearchProceeded = false;
		private boolean enableRemove = false;

		@SuppressWarnings("synthetic-access")
		public InnerIterator() {
			this.originalIterator = AbstractReferencedSet.this.theSet.iterator();
		}

		private void searchNext() {
			if (!this.nextSearchProceeded) {
				this.nextSearchProceeded = true;
				this.next = null;
				R originalNext;
				E wValue;
				while (this.next==null && this.originalIterator.hasNext()) {
					originalNext = this.originalIterator.next();
					if (originalNext!=null) {
						wValue = originalNext.get();
						if (wValue!=null) {
							this.next = wValue;
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
		public E next() {
			searchNext();
			assert(this.nextSearchProceeded);
			E cnext = this.next;

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

	/** Comparator wrapper.
	 * 
	 * @param <E> is the type of the values.
	 * @param <R> is the type of the references.
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 5.9
	 */
	protected static class ReferenceComparator<E,R extends Reference<E>> implements Comparator<R> {

		private final Comparator<? super E> comparator;
		
		/**
		 * @param comparator
		 */
		public ReferenceComparator(Comparator<? super E> comparator) {
			assert(comparator!=null);
			this.comparator = comparator;
		}

		@Override
		public int compare(R o1, R o2) {
			if (o1==o2) return 0;
			if (o1==null) return Integer.MIN_VALUE;
			if (o2==null) return Integer.MAX_VALUE;
			return this.comparator.compare(o1.get(), o2.get());
		}
		
	}

}