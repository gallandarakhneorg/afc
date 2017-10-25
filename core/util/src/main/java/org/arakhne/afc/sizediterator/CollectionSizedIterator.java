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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.xtext.xbase.lib.Pure;

/** Iterator on collection.
 *
 * @param <OBJECTT> is the type of the objects to iterator on.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CollectionSizedIterator<OBJECTT> implements SizedIterator<OBJECTT> {

	private final Iterator<OBJECTT> iterator;

	private int length;

	private int index;

	/** Construct an iterator.
	 *
	 * @param collection the collection to iterate on.
	 */
	public CollectionSizedIterator(Collection<OBJECTT> collection) {
		this.iterator = collection.iterator();
		this.length = collection.size();
		this.index = -1;
	}

	@Pure
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public OBJECTT next() {
		final OBJECTT n = this.iterator.next();
		++this.index;
		return n;
	}

	@Override
	public void remove() {
		this.iterator.remove();
		--this.length;
		--this.index;
	}

	@Pure
	@Override
	public int index() {
		return this.index;
	}

	@Pure
	@Override
	public int totalSize() {
		return this.length;
	}

	@Pure
	@Override
	public int rest() {
		return this.length - (this.index + 1);
	}

}
