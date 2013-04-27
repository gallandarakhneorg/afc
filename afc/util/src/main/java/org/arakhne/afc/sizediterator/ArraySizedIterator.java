/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.sizediterator;

import java.util.NoSuchElementException;

/** Iterator which is disabling the {@code remove()} function.
 * 
 * @param <OBJ> is the type of the objects to iterator on.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ArraySizedIterator<OBJ> implements SizedIterator<OBJ> {

	private final OBJ[] array;
	private final int length;
	private int index = 0;
	private OBJ next;
	private int nextIndex;
	
	/**
	 * @param array
	 */
	public ArraySizedIterator(OBJ[] array) {
		this.array = array;
		this.length = (array!=null) ? array.length : 0;
		searchNext();
	}
	
	private void searchNext() {
		this.next = null;
		OBJ o;
		while (this.index>=0 && this.index<this.length) {
			o = this.array[this.index++];
			if (o!=null) {
				this.next = o;
				return;
			}
		}
		this.index = -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.next!=null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OBJ next() {
		if (this.next==null) throw new NoSuchElementException();
		OBJ o = this.next;
		this.nextIndex = this.index - 1;
		searchNext();
		return o;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int index() {
		if (this.nextIndex<0) throw new NoSuchElementException();
		return this.nextIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int totalSize() {
		return this.length;
	}

	@Override
	public int rest() {
		return this.length - this.nextIndex;
	}

}
