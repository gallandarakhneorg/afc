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

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * A sized iterator that is based on the used of
 * a collection of sized iterators.
 *
 * @param <M> is the type of element.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MultiSizedIterator<M> implements SizedIterator<M> {

	private final LinkedList<SizedIterator<? extends M>> iterators = new LinkedList<>();

	private int total;

	private int returned;

	private SizedIterator<? extends M> iterator;

	private boolean update;

	private M next;

	/** Construct an iterator.
	 */
	public MultiSizedIterator() {
		//
	}

	/** Add an iterator in the list of iterators.
	 *
	 * @param iterator the iterator.
	 */
	public void addIterator(SizedIterator<? extends M> iterator) {
		if (this.iterators.add(iterator)) {
			this.total += iterator.totalSize();
			this.update = true;
		}
	}

	private void searchNext() {
		this.next = null;
		while ((this.iterator == null || !this.iterator.hasNext()) && (!this.iterators.isEmpty())) {
			this.iterator = this.iterators.removeFirst();
		}
		if (this.iterator != null && this.iterator.hasNext()) {
			this.next = this.iterator.next();
		}
		this.update = false;
	}

	@Pure
	@Override
	public boolean hasNext() {
		if (this.next == null && this.update) {
			searchNext();
		}
		return this.next != null;
	}

	@Override
	public M next() {
		if (this.next == null && this.update) {
			searchNext();
		}
		final M v = this.next;
		if (v == null) {
			throw new NoSuchElementException();
		}
		++this.returned;
		searchNext();
		return v;
	}

	@Pure
	@Override
	public int totalSize() {
		return this.total;
	}

	@Pure
	@Override
	public int rest() {
		return this.total - this.returned;
	}

	@Pure
	@Override
	public int index() {
		return totalSize() - rest();
	}

}
