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

/** Single iterator.
 *
 * @param <OBJECT> is the type of the objects to iterator on.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SingleIterator<OBJECT> implements SizedIterator<OBJECT> {

	private OBJECT object;

	/** Construct an iterator.
	 *
	 * @param obj the object to iterate on.
	 */
	public SingleIterator(OBJECT obj) {
		this.object = obj;
	}

	@Override
	public boolean hasNext() {
		return this.object != null;
	}

	@Override
	public OBJECT next() {
		if (this.object != null) {
			final OBJECT obj = this.object;
			this.object = null;
			return obj;
		}
		throw new NoSuchElementException();
	}

	@Override
	public int index() {
		if (this.object == null) {
			return 0;
		}
		return -1;
	}

	@Override
	public int totalSize() {
		return 1;
	}

	@Override
	public int rest() {
		if (this.object == null) {
			return 0;
		}
		return 1;
	}

}
