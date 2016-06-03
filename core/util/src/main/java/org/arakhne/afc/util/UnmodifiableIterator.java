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

package org.arakhne.afc.util;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.sizediterator.UnmodifiableSizedIterator;

/**
 * Class that make unmodifiable an iterator.
 *
 * <p>This class provides features closed to {@link Collections#unmodifiableCollection(java.util.Collection)}
 * but on iterators.
 *
 * <p>This iterator disables the use of the function {@link #remove()}.
 * This iterator implementation is not a sized iterator; see
 * {@link UnmodifiableSizedIterator} instead.
 *
 * @param <M> is the type of element.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see UnmodifiableSizedIterator
 */
public class UnmodifiableIterator<M> implements Iterator<M> {

	private final Iterator<M> original;

	/** Construct an iterator.
	 *
	 * @param iterator the iterator.
	 */
	public UnmodifiableIterator(Iterator<M> iterator) {
		assert iterator != null;
		this.original = iterator;
	}

	/** Construct an iterator.
	 *
	 * @param iterable the iterable.
	 */
	public UnmodifiableIterator(Iterable<M> iterable) {
		assert iterable != null;
		this.original = iterable.iterator();
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

}
