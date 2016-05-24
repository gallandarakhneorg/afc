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

import java.lang.ref.PhantomReference;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;

/**
 * A <tt>Map</tt> implementation with {@link PhantomReference phantom values}. An entry in a
 * <tt>PhantomValueTreeMap</tt> will automatically be removed when its value is no
 * longer in ordinary use or null.
 *
 * <p>This class was inspirated from {@link WeakHashMap} and uses a {@link TreeMap}
 * as its internal data structure.
 *
 * <p>This class has a special flag which permits to control the
 * way how the released references are expurged: {@link #isDeeplyExpurge()},
 * {@link #setDeeplyExpurge(boolean)}. If this flag is <code>true</code>,
 * all the released references will be immediately removed from the map even
 * if they are not enqueued by the virtual machine (see {@link #expurge()}.
 * If this flag is <code>false</code>,
 * only the enqueued references will be removed from the map
 * (see {@link #expurgeQueuedReferences()}.
 *
 * <p>If this map does not use a "deep expurge" of the released references,
 * it could contains <code>null</code> values that corresponds to
 * values that are released by the garbage collector. If a "deep expurge"
 * is used, all the values released by the garbage collector will be
 * removed from the map.
 *
 * <p>"Deep expurge" consumes much more time that "No deep expurge". This is the
 * reason why this feature is not activated by default.
 *
 * <p>The "deep expurge" feature was added to fix the uncoherent behavior
 * of the garbage collector which seems to not always enqueued the
 * released values (sometimes the queue is empty even if a value was released).
 *
 * @param <K> is the type of the keys.
 * @param <V> is the type of the values.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.8
 */
public class PhantomValueTreeMap<K, V> extends AbstractPhantomValueMap<K, V> {

    /**
     * Constructs an empty <tt>TreeMap</tt> with the specified comparator.
     *
     * @param comparator the comparator that will be used to order this map.
     *        If <tt>null</tt>, the {@linkplain Comparable natural
     *        ordering} of the keys will be used.
     */
    public PhantomValueTreeMap(Comparator<? super K> comparator) {
    	super(new TreeMap<K, ReferencableValue<K, V>>(comparator));
    }

    /**
     * Constructs an empty <tt>TreeMap</tt>.
     */
    public PhantomValueTreeMap() {
    	super(new TreeMap<K, ReferencableValue<K, V>>());
    }

    /**
     * Constructs a new <tt>TreeMap</tt> with the same mappings as the
     * specified <tt>Map</tt>.
     *
     * @param   map the map whose mappings are to be placed in this map
     * @throws  NullPointerException if the specified map is null
     */
    public PhantomValueTreeMap(Map<? extends K, ? extends V> map) {
        super(new TreeMap<K, ReferencableValue<K, V>>());
        putAll(map);
    }

    /**
     * Constructs a new <tt>TreeMap</tt> with the same mappings and
     * comparator as the specified <tt>Map</tt>.
     *
     * @param   map the map whose mappings are to be placed in this map
     * @throws  NullPointerException if the specified map is null
     */
    public PhantomValueTreeMap(SortedMap<K, ? extends V> map) {
        super(new TreeMap<K, ReferencableValue<K, V>>(map.comparator()));
        putAll(map);
    }

}
