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
 *
 * <p>This class was inspirated from {@link WeakHashMap} and uses a {@link TreeSet}
 * as its internal data structure.
 *
 * <p>This class has a special flag which permits to control the
 * way how the released references are expurged: {@link #isDeeplyExpurge()},
 * {@link #setDeeplyExpurge(boolean)}. If this flag is <code>true</code>,
 * all the released references will be immediately removed from the map even
 * if they are not enqueued by the virtual machine (see {@link #expurge()}.
 * If this flag is <code>false</code>,
 * only the enqueued references will be removed from the map
 * (see {@link #expurgeQueuedReferences()}.
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
 * @param <E> is the type of the values.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.9
 */
public class SoftTreeSet<E> extends AbstractReferencedSet<E, ComparableSoftReference<E>> {

	/** Constructs an empty <tt>TreeSet</tt>.
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
	 * @param collection collection whose elements will comprise the new set
	 * @throws ClassCastException if the elements in {@code c} are
	 *         not {@link Comparable}, or are not mutually comparable
	 * @throws NullPointerException if the specified collection is null
	 */
	public SoftTreeSet(Collection<? extends E> collection) {
		super(new TreeSet<ComparableSoftReference<E>>(), ComparableSoftReference.class);
		addAll(collection);
	}

	/**
	 * Constructs a new tree set containing the same elements and
	 * using the same ordering as the specified sorted set.
	 *
	 * @param set sorted set whose elements will comprise the new set
	 * @throws NullPointerException if the specified sorted set is null
	 */
	@SuppressWarnings("unchecked")
	public SoftTreeSet(SortedSet<? extends E> set) {
		this((Comparator<? super E>) set.comparator());
		addAll(set);
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
		super(new TreeSet<>(
				new ReferenceComparator<E, ComparableSoftReference<E>>(comparator)),
				ComparableSoftReference.class);
	}

	@Override
	protected final ComparableSoftReference<E> createReference(E element) {
		return new ComparableSoftReference<>(element);
	}

}
