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

import java.util.Collection;
import java.util.Iterator;

/** Iterator on collection.
 * 
 * @param <OBJ> is the type of the objects to iterator on.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CollectionSizedIterator<OBJ> implements SizedIterator<OBJ> {

	private final Iterator<OBJ> iterator;
	private int length, index;
	
	/**
	 * @param collection
	 */
	public CollectionSizedIterator(Collection<OBJ> collection) {
		this.iterator = collection.iterator();
		this.length = collection.size();
		this.index = -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OBJ next() {
		OBJ n = this.iterator.next();
		this.index ++;
		return n;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		this.iterator.remove();
		this.length --;
		this.index --;
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
		return this.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int rest() {
		return this.length - (this.index+1);
	}

}
