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

import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

/** Sized iterator on an empty collection.
 *
 * <p>A sized iterator is an Iterator that is able to
 * reply the size of the iterated collection and
 * the number of elements that may be encountered
 * in the next iterations.
 *
 * @param <M> is the type of element.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EmptyIterator<M> implements SizedIterator<M> {

	/** Singleton.
	 */
	@SuppressWarnings("rawtypes")
	private static final EmptyIterator SINGLETON = new EmptyIterator();

	/** Construct an iterator.
	 */
	protected EmptyIterator() {
		//
	}

	/** Replies the singleton.
	 * @param <M> is the type of the element to iterate on.
	 * @return the singleton.
	 */
	@Pure
	@SuppressWarnings("unchecked")
	public static <M> EmptyIterator<M> singleton() {
		return SINGLETON;
	}

	@Pure
	@Override
	public final boolean hasNext() {
		return false;
	}

	@Override
	public final M next() {
		throw new NoSuchElementException();
	}

	@Pure
	@Override
	public final int rest() {
		return 0;
	}

	@Pure
	@Override
	public final int index() {
		return -1;
	}

	@Pure
	@Override
	public final int totalSize() {
		return 0;
	}

}
