/*
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.sizediterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/** Iterator which is replying the elements from the given iterators in turn
 * only if they are of the given type.
 * 
 * @param <OBJ> is the type of the objects to iterator on.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class FilteredIteratorIterator<OBJ> implements Iterator<OBJ> {

	private final List<Iterator<?>> iterators = new LinkedList<Iterator<?>>();
	private final Class<? extends OBJ> type;
	private OBJ object = null;
	
	/**
	 * @param type is the type of the elements to filter.
	 * @param iterator1 is the first iterator.
	 * @param iterator2 is the second iterator.
	 */
	public FilteredIteratorIterator(Class<? extends OBJ> type, Iterator<?> iterator1, Iterator<?> iterator2) {
		this(type, Arrays.asList(iterator1, iterator2));
	}

	/**
	 * @param type is the type of the elements to filter.
	 * @param iterators are the iterators.
	 */
	public FilteredIteratorIterator(Class<? extends OBJ> type, Collection<Iterator<?>> iterators) {
		this.type = type;
		this.iterators.addAll(iterators);
		searchNext();
	}
	
	private void searchNext() {
		Iterator<?> it;
		this.object = null;
		while (this.object==null && !this.iterators.isEmpty()) {
			it = this.iterators.get(0);
			if (it==null || !it.hasNext()) {
				this.iterators.remove(0);
			}
			else {
				Object o = it.next();
				if (this.type.isInstance(o))
					this.object = this.type.cast(o);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.object!=null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OBJ next() {
		if (this.object==null) throw new NoSuchElementException();
		OBJ elt = this.object;
		searchNext();
		return elt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
