/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
import java.util.Iterator;
import java.util.Map;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Class that map the keys of a Map to an unmodifiable  sized iterator.
 *
 * <p>A sized iterator is an Iterator that is able to
 * reply the size of the iterated collection and
 * the number of elements that may be encountered
 * in the next iterations.
 *
 * <p>This class provides features closed to {@link Collections#unmodifiableMap(Map)}
 * but with a sized iterators on the keys of the map.
 * To obtain similar features on the map values, see {@link UnmodifiableMapValueSizedIterator}.
 *
 * <p>This iterator disables the use of the function {@link #remove()}.
 *
 * @param <K> is the type of keys.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see UnmodifiableMapValueSizedIterator
 */
public class UnmodifiableMapKeySizedIterator<K> implements SizedIterator<K> {

	private final int total;

	private int rest;

	private final Iterator<K> iterator;

	/** Construct an iterator.
	 *
	 * @param map the map to iterate on.
	 */
	public UnmodifiableMapKeySizedIterator(Map<K, ?> map) {
		assert map != null;
		this.total = map.size();
		this.rest = this.total;
		this.iterator = map.keySet().iterator();
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
	public K next() {
		final K elt = this.iterator.next();
		--this.rest;
		return elt;
	}

}
