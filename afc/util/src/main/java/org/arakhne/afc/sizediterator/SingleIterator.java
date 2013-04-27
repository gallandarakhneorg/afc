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

/** Single iterator.
 * 
 * @param <OBJ> is the type of the objects to iterator on.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SingleIterator<OBJ> implements SizedIterator<OBJ> {

	private OBJ object;
	
	/**
	 * @param obj
	 */
	public SingleIterator(OBJ obj) {
		this.object = obj;
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
		if (this.object!=null) {
			OBJ obj = this.object;
			this.object = null;
			return obj;
		}
		throw new NoSuchElementException();
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
		if (this.object==null)
			return 0;
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int totalSize() {
		return 1;
	}

	@Override
	public int rest() {
		if (this.object==null) return 0;
		return 1;
	}

}
