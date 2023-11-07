/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import java.util.Collections;
import java.util.List;

import javafx.collections.ObservableList;

/** Simple event for JavaFX changes.
 *
 * @param <E> the type of the changed object.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
public class SimpleListUpdateChange<E> extends ListNonIterableChange<E> {

	/** Constructor.
	 *
	 * @param position the index of change.
	 * @param list the changed list.
	 */
	public SimpleListUpdateChange(int position, ObservableList<E> list) {
		this(position, position + 1, list);
	}

	/** Constructor.
	 *
	 * @param from the index of the first change.
	 * @param to the index + 1 of the last change.
	 * @param list the changed list.
	 */
	public SimpleListUpdateChange(int from, int to, ObservableList<E> list) {
		super(from, to, list);
	}

	@Override
	public List<E> getRemoved() {
		return Collections.<E>emptyList();
	}

	@Override
	public boolean wasUpdated() {
		return true;
	}

}
