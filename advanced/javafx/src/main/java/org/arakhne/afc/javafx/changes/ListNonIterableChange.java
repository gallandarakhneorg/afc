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

package org.arakhne.afc.javafx.changes;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/** Event for JavaFX changes.
 *
 * @param <E> the type of the changed object.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
public abstract class ListNonIterableChange<E> extends ListChangeListener.Change<E> {

	private static final int[] EMPTY_PERM = new int[0];

	private final int from;

	private final int to;

	private boolean invalid = true;

	/** Constructor.
	 *
	 * @param from is the index of the first change.
	 * @param to is the index + 1 of the last change.
	 * @param list is the changed list.
	 */
	protected ListNonIterableChange(int from, int to, ObservableList<E> list) {
		super(list);
		this.from = from;
		this.to = to;
	}

	@Override
	public int getFrom() {
		checkState();
		return this.from;
	}

	@Override
	public int getTo() {
		checkState();
		return this.to;
	}

	@Override
	protected int[] getPermutation() {
		checkState();
		return EMPTY_PERM;
	}

	@Override
	public boolean next() {
		if (this.invalid) {
			this.invalid = false;
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		this.invalid = true;
	}

	/** Check if the state of the change is valid.
	 *
	 * @throws IllegalStateException invalid internal state. You must call {@link #next()} first.
	 */
	public void checkState() {
		if (this.invalid) {
			throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change."); //$NON-NLS-1$
		}
	}

}
