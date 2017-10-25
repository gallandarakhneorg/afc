/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.util.Collections;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.util.UnmodifiableIterator;

/**
 * Class that make unmodifiable a sized iterator.
 *
 * <p>A sized iterator is an Iterator that is able to
 * reply the size of the iterated collection and
 * the number of elements that may be encountered
 * in the next iterations.
 *
 * <p>This class provides features closed to {@link Collections#unmodifiableCollection(java.util.Collection)}
 * but on sized iterators.
 *
 * <p>This iterator disables the use of the function {@link #remove()}.
 * This iterator implementation is a iterator; see
 * {@link UnmodifiableIterator} for an utility class on a standard Iterator.
 *
 * @param <M> is the type of element.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see UnmodifiableIterator
 */
public class UnmodifiableSizedIterator<M> implements SizedIterator<M> {

	private final SizedIterator<M> original;

	/** Construct an iterator.
	 *
	 * @param iterator the iterator.
	 */
	public UnmodifiableSizedIterator(SizedIterator<M> iterator) {
		assert iterator != null;
		this.original = iterator;
	}

	@Pure
	@Override
	public boolean hasNext() {
		return this.original.hasNext();
	}

	@Override
	public M next() {
		return this.original.next();
	}

	@Pure
	@Override
	public int rest() {
		return this.original.rest();
	}

	@Pure
	@Override
	public int index() {
		return this.original.index();
	}

	@Pure
	@Override
	public int totalSize() {
		return this.original.totalSize();
	}

}
