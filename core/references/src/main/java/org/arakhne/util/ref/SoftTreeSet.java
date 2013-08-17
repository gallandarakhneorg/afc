/* 
 * $Id$
 * 
 * Copyright (C) 2005-2007 Stephane GALLAND.
 * Copyright (C) 2011 Stephane GALLAND.
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

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;

/**
 * A <tt>Set</tt> implementation with {@link SoftReference soft values}. An entry in a
 * <tt>SoftTreeSet</tt> will automatically be removed when its value is no
 * longer in ordinary use or null.
 * <p>
 * This class was inspirated from {@link WeakHashMap} and uses a {@link TreeSet}
 * as its internal data structure.
 * <p>
 * This class has a special flag which permits to control the
 * way how the released references are expurged: {@link #isDeeplyExpurge()},
 * {@link #setDeeplyExpurge(boolean)}. If this flag is <code>true</code>,
 * all the released references will be immediately removed from the map even
 * if they are not enqueued by the virtual machine (see {@link #expurge()}.
 * If this flag is <code>false</code>,
 * only the enqueued references will be removed from the map
 * (see {@link #expurgeQueuedReferences()}.
 * <p>
 * If this map does not use a "deep expurge" of the released references,
 * it could contains <code>null</code> values that corresponds to
 * values that are released by the garbage collector. If a "deep expurge"
 * is used, all the values released by the garbage collector will be
 * removed from the map.
 * <p>
 * "Deep expurge" consumes much more time that "No deep expurge". This is the
 * reason why this feature is not activated by default.
 * <p>
 * The "deep expurge" feature was added to fix the uncoherent behavior
 * of the garbage collector which seems to not always enqueued the 
 * released values (sometimes the queue is empty even if a value was released).
 *
 * @param <E> is the type of the values.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.9
 */
public class SoftTreeSet<E> extends AbstractReferencedSet<E,ComparableSoftReference<E>> {
	
	/**
     * Constructs an empty <tt>TreeSet</tt>.
     */
    public SoftTreeSet() {
    	super(new TreeSet<ComparableSoftReference<E>>(), ComparableSoftReference.class);
    }
      
    /**
     * Constructs a new tree set containing the elements in the specified
     * collection, sorted according to the <i>natural ordering</i> of its
     * elements.  All elements inserted into the set must implement the
     * {@link Comparable} interface.  Furthermore, all such elements must be
     * <i>mutually comparable</i>: {@code e1.compareTo(e2)} must not throw a
     * {@code ClassCastException} for any elements {@code e1} and
     * {@code e2} in the set.
     *
     * @param c collection whose elements will comprise the new set
     * @throws ClassCastException if the elements in {@code c} are
     *         not {@link Comparable}, or are not mutually comparable
     * @throws NullPointerException if the specified collection is null
     */
    public SoftTreeSet(Collection<? extends E> c) {
    	super(new TreeSet<ComparableSoftReference<E>>(), ComparableSoftReference.class);
    	addAll(c);
    }

    /**
     * Constructs a new tree set containing the same elements and
     * using the same ordering as the specified sorted set.
     *
     * @param s sorted set whose elements will comprise the new set
     * @throws NullPointerException if the specified sorted set is null
     */
    @SuppressWarnings("unchecked")
	public SoftTreeSet(SortedSet<? extends E> s) {
    	this((Comparator<? super E>)s.comparator());
    	addAll(s);
    }

    /**
     * Constructs a new, empty tree set, sorted according to the specified
     * comparator.  All elements inserted into the set must be <i>mutually
     * comparable</i> by the specified comparator: {@code comparator.compare(e1,
     * e2)} must not throw a {@code ClassCastException} for any elements
     * {@code e1} and {@code e2} in the set.  If the user attempts to add
     * an element to the set that violates this constraint, the
     * {@code add} call will throw a {@code ClassCastException}.
     *
     * @param comparator the comparator that will be used to order this set.
     *        If {@code null}, the {@linkplain Comparable natural
     *        ordering} of the elements will be used.
     */
    public SoftTreeSet(Comparator<? super E> comparator) {
    	super(new TreeSet<ComparableSoftReference<E>>(
    				new ReferenceComparator<E,ComparableSoftReference<E>>(comparator)),
    			ComparableSoftReference.class);
    }

    @Override
    protected final ComparableSoftReference<E> createReference(E element) {
    	return new ComparableSoftReference<E>(element);
    }

}
