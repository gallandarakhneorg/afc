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
package org.arakhne.afc.sizediterator;

import java.util.Collections;

import org.arakhne.afc.util.UnmodifiableIterator;

/**
 * Class that make unmodifiable a sized iterator.
 * <p>
 * A sized iterator is an Iterator that is able to
 * reply the size of the iterated collection and
 * the number of elements that may be encountered
 * in the next iterations.
 * <p>
 * This class provides features closed to {@link Collections#unmodifiableCollection(java.util.Collection)}
 * but on sized iterators.
 * <p>
 * This iterator disables the use of the function {@link #remove()}.
 * This iterator implementation is a iterator; see
 * {@link UnmodifiableIterator} for an utility class on a standard Iterator.
 * 
 * @param <M> is the type of element.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see UnmodifiableIterator
 */
public class UnmodifiableSizedIterator<M>
implements SizedIterator<M> {

	private final SizedIterator<M> original;

	/**
	 * @param iterator
	 */
	public UnmodifiableSizedIterator(SizedIterator<M> iterator) {
		assert(iterator!=null);
		this.original = iterator;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int rest() {
		return this.original.rest();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int index() {
		return this.original.index();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int totalSize() {
		return this.original.totalSize();
	}

}
