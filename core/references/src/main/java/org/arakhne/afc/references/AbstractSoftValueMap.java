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

package org.arakhne.afc.references;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;

/**
 * A <tt>Map</tt> implementation with {@link SoftReference soft values}. An entry in a
 * <tt>AbstractSoftValueMap</tt> will automatically be removed when its value is no
 * longer in ordinary use or <code>null</code>.
 *
 * <p>This abstract implementation does not decide if the map is based on a tree or on a hashtable.
 *
 * @param <K> is the type of the keys.
 * @param <V> is the type of the values.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.8
 */
public abstract class AbstractSoftValueMap<K, V> extends AbstractReferencedValueMap<K, V> {

	/** Constructs an empty <tt>Map</tt>.
	 *
	 * @param  map is the map instance to use to store the entries.
	 * @throws IllegalArgumentException if the initial capacity is negative
	 *         or the load factor is nonpositive
	 */
	public AbstractSoftValueMap(Map<K, ReferencableValue<K, V>> map) {
		super(map);
	}

	@Override
	protected final ReferencableValue<K, V> makeValue(K key, V value, ReferenceQueue<V> queue) {
		return new SoftReferencedValue<>(key, value, queue);
	}

}
