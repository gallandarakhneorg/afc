/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.util;

import java.util.Collections;
import java.util.Iterator;

import org.arakhne.afc.sizediterator.UnmodifiableSizedIterator;

/**
 * Class that make unmodifiable an iterator.
 * <p>
 * This class provides features closed to {@link Collections#unmodifiableCollection(java.util.Collection)}
 * but on iterators.
 * <p>
 * This iterator disables the use of the function {@link #remove()}.
 * This iterator implementation is not a sized iterator; see
 * {@link UnmodifiableSizedIterator} instead.
 * 
 * @param <M> is the type of element.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see UnmodifiableSizedIterator
 */
public class UnmodifiableIterator<M>
implements Iterator<M> {

	private final Iterator<M> original;

	/**
	 * @param iterator
	 */
	public UnmodifiableIterator(Iterator<M> iterator) {
		assert(iterator!=null);
		this.original = iterator;
	}
	
	/**
	 * @param iterable
	 */
	public UnmodifiableIterator(Iterable<M> iterable) {
		assert(iterable!=null);
		this.original = iterable.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.original.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public M next() {
		return this.original.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void remove() {
		throw new UnsupportedOperationException();
	}

}
