/*
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
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

/** Iterator which is replying the elements from the given iterators in turn.
 * 
 * @param <OBJ> is the type of the objects to iterator on.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class IteratorIterator<OBJ> implements Iterator<OBJ> {

	/** Available iterators.
	 */
	final List<Iterator<? extends OBJ>> iterators = new LinkedList<Iterator<? extends OBJ>>();
	
	/**
	 * @param iterator1 is the first iterator.
	 * @param iterator2 is the second iterator.
	 */
	@SuppressWarnings("unchecked")
	public IteratorIterator(Iterator<? extends OBJ> iterator1, Iterator<? extends OBJ> iterator2) {
		this(Arrays.asList(iterator1, iterator2));
	}

	/**
	 * @param iterators are the iterators.
	 */
	public IteratorIterator(Collection<Iterator<? extends OBJ>> iterators) {
		this.iterators.addAll(iterators);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		while (!this.iterators.isEmpty()) {
			Iterator<? extends OBJ> it = this.iterators.get(0);
			if (it!=null && it.hasNext()) return true;
			this.iterators.remove(0);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OBJ next() {
		while (!this.iterators.isEmpty()) {
			Iterator<? extends OBJ> it = this.iterators.get(0);
			if (it!=null && it.hasNext()) {
				return it.next();
			}
			this.iterators.remove(0);
		}
		throw new NoSuchElementException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		if (!this.iterators.isEmpty()) {
			Iterator<? extends OBJ> it = this.iterators.get(0);
			if (it!=null) {
				it.remove();
			}
			else {
				throw new NoSuchElementException();
			}
		}
		else {
			throw new NoSuchElementException();
		}
	}

}
