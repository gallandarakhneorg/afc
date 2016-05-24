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

package org.arakhne.afc.sizediterator;

import java.util.Collection;
import java.util.Iterator;


/**
 * Sized iterator on a collection that can be changed through the iterator.
 *
 * <p>A sized iterator is an Iterator that is able to
 * reply the size of the iterated collection and
 * the number of elements that may be encountered
 * in the next iterations.
 *
 * <p>This iterator enables the use of the function {@link #remove()}.
 * See {@link UnmodifiableCollectionSizedIterator} for an sized iterator
 * that disables this function.
 *
 * @param <M> is the type of element.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see UnmodifiableCollectionSizedIterator
 */
public class ModifiableCollectionSizedIterator<M> implements SizedIterator<M> {

	private final ModifiableCollectionSizedIteratorOwner<M> owner;

	private int total;

	private int rest;

	private final Iterator<M> iterator;

	private M lastReplied;

	/** Construct an iterator.
	 *
	 * @param collection the collection to iterate on.
	 */
	public ModifiableCollectionSizedIterator(Collection<M> collection) {
		this(collection, null);
	}

	/** Construct an iterator.
	 *
	 * @param collection the collection to iterate on.
	 * @param owner the owner of the iterator.
	 */
	public ModifiableCollectionSizedIterator(Collection<M> collection, ModifiableCollectionSizedIteratorOwner<M> owner) {
		assert collection != null;
		this.owner = owner;
		this.total = collection.size();
		this.rest = this.total;
		this.iterator = collection.iterator();
	}

	@Override
	public int rest() {
		return this.rest;
	}

	@Override
	public int index() {
		return this.total - this.rest;
	}

	@Override
	public int totalSize() {
		return this.total;
	}

	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public M next() {
		final M elt = this.iterator.next();
		--this.rest;
		this.lastReplied = elt;
		return elt;
	}

	@Override
	public void remove() {
		this.iterator.remove();
		final M data = this.lastReplied;
		this.lastReplied = null;
		--this.total;
		--this.rest;
		if (data != null && this.owner != null) {
			this.owner.onRemoveFromIterator(data);
		}
	}

}
