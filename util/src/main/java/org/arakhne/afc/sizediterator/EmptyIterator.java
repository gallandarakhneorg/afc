/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2010-2011 Janus Core Developers
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arakhne.afc.sizediterator;

import java.util.NoSuchElementException;


/**
 * Sized iterator on an empty collection.
 * <p>
 * A sized iterator is an Iterator that is able to
 * reply the size of the iterated collection and
 * the number of elements that may be encountered
 * in the next iterations.
 *  
 * @param <M> is the type of element.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EmptyIterator<M>
implements SizedIterator<M> {

	/** Singleton.
	 */
	@SuppressWarnings("rawtypes")
	private static final EmptyIterator SINGLETON = new EmptyIterator();
	
	/** Replies the singleton.
	 * @param <M> is the type of the element to iterate on.
	 * @return the singleton.
	 */
	@SuppressWarnings("unchecked")
	public static <M> EmptyIterator<M> singleton() {
		return SINGLETON;
	}
	
	/**
	 */
	public EmptyIterator() {
		//
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean hasNext() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final M next() {
		throw new NoSuchElementException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void remove() {
		throw new NoSuchElementException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int rest() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int index() {
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int totalSize() {
		return 0;
	}

}
