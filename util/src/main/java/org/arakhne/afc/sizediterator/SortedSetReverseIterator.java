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
import java.util.NoSuchElementException;
import java.util.SortedSet;

/** Iterator which replies the elements of a sorted set in a reverse order.
 * 
 * @param <E> is the type of the objects to iterator on.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ListReverseIterator
 */
public class SortedSetReverseIterator<E> implements SizedIterator<E> {

	private int savedSize;
	private int index;
	private final SortedSet<E> original;
	private SortedSet<E> set;
	private E lastReplied = null;
	
	/**
	 * @param set is the sorted set to iterate on.
	 */
	public SortedSetReverseIterator(SortedSet<E> set) {
		this.savedSize = (set==null) ? 0 : set.size();
		this.index = -1;
		this.original = set;
		this.set = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.set!=null && !this.set.isEmpty(); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() {
		if (this.set==null || this.set.isEmpty()) throw new NoSuchElementException();
		if (this.savedSize!=this.original.size()) throw new ConcurrentModificationException();
		this.lastReplied = this.set.last();
		this.set = this.set.headSet(this.lastReplied);
		if (this.index==-1) this.index = this.savedSize - 1;
		else --this.index;
		return this.lastReplied;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		if (this.lastReplied==null) throw new NoSuchElementException();
		this.original.remove(this.lastReplied);
		this.lastReplied = null;
		--this.savedSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int index() {
		return this.index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int totalSize() {
		return this.original.size();
	}

	@Override
	public int rest() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
