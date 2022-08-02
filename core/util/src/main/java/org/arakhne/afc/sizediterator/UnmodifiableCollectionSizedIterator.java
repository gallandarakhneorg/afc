/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Sized iterator on a collection that cannot be changed through the iterator.
 *
 * <p>A sized iterator is an Iterator that is able to
 * reply the size of the iterated collection and
 * the number of elements that may be encountered
 * in the next iterations.
 *
 * <p>This iterator disables the use of the function {@link #remove()}.
 * See {@link ModifiableCollectionSizedIterator} for an sized iterator
 * that enables this function.
 *
 * @param <M> is the type of element.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ModifiableCollectionSizedIterator
 */
public class UnmodifiableCollectionSizedIterator<M> implements SizedIterator<M> {

	private final int total;

	private int rest;

	private final Iterator<M> iterator;

	/** Construct an iterator.
	 *
	 * @param collection the collection to iterate on.
	 */
	public UnmodifiableCollectionSizedIterator(Collection<M> collection) {
		assert collection != null;
		this.total = collection.size();
		this.rest = this.total;
		this.iterator = collection.iterator();
	}

	@Pure
	@Override
	public int rest() {
		return this.rest;
	}

	@Pure
	@Override
	public int index() {
		return this.total - this.rest;
	}

	@Pure
	@Override
	public int totalSize() {
		return this.total;
	}

	@Pure
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public M next() {
		final M elt = this.iterator.next();
		--this.rest;
		return elt;
	}

}
