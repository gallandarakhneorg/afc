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

import java.util.NoSuchElementException;

/** Iterator which is disabling the {@code remove()} function.
 *
 * @param <OBJECT> is the type of the objects to iterator on.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ArraySizedIterator<OBJECT> implements SizedIterator<OBJECT> {

	private final OBJECT[] array;

	private final int length;

	private int index;

	private OBJECT next;

	private int nextIndex;

	/** Construct an iterator.
	 *
	 * @param array the array to iterator on.
	 */
	public ArraySizedIterator(OBJECT[] array) {
		this.array = array;
		this.length = (array != null) ? array.length : 0;
		searchNext();
	}

	private void searchNext() {
		this.next = null;
		OBJECT obj;
		while (this.index >= 0 && this.index < this.length) {
			obj = this.array[this.index++];
			if (obj != null) {
				this.next = obj;
				return;
			}
		}
		this.index = -1;
	}

	@Override
	public boolean hasNext() {
		return this.next != null;
	}

	@Override
	public OBJECT next() {
		if (this.next == null) {
			throw new NoSuchElementException();
		}
		final OBJECT obj = this.next;
		this.nextIndex = this.index - 1;
		searchNext();
		return obj;
	}

	@Override
	public int index() {
		if (this.nextIndex < 0) {
			throw new NoSuchElementException();
		}
		return this.nextIndex;
	}

	@Override
	public int totalSize() {
		return this.length;
	}

	@Override
	public int rest() {
		return this.length - this.nextIndex;
	}

}
