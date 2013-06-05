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

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;

/** Iterator which replies the elements of a list in a reverse order.
 * 
 * @param <E> is the type of the objects to iterator on.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see SortedSetReverseIterator
 */
public class ListReverseIterator<E> implements SizedIterator<E> {

	private int savedSize;
	private int index;
	private final List<E> list;
	
	/**
	 * @param list is the listto iterate on.
	 */
	public ListReverseIterator(List<E> list) {
		this.savedSize = (list==null) ? 0 : list.size();
		this.index = this.savedSize - 1;
		this.list = list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.list!=null && this.index>=0 && this.index<this.list.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() {
		if (this.list==null) throw new NoSuchElementException();
		if (this.savedSize!=this.list.size()) throw new ConcurrentModificationException();
		if (this.index<0 || this.index>=this.list.size()) throw new NoSuchElementException();
		E elt = this.list.get(this.index);
		--this.index;
		return elt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		int idx = this.index + 1;
		if (idx<0 || idx>=this.list.size())
			throw new NoSuchElementException();
		this.list.remove(this.index+1);
		--this.savedSize;
		if (this.savedSize<0) this.savedSize = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int index() {
		int i = this.index+1;
		if (i<0 || i>=this.list.size()) return -1;
		return i;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int totalSize() {
		return this.list.size();
	}

	@Override
	public int rest() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
