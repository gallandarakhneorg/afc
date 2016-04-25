/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.attrs.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.attrs.attr.Attribute;

/**
 * Iterator on attributes.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeIterator implements Iterator<Attribute> {
	
	private AttributeProvider provider;
	private final ArrayList<String> names = new ArrayList<String>();
	private String lastName = null; 
	
	/**
	 * Create an iterator on the given container.
	 * 
	 * @param provider is the container of attributes.
	 */
	public AttributeIterator(AttributeProvider provider) {
		this.provider = provider;
		this.names.addAll(provider.getAllAttributeNames());
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if ((this.provider==null)||(this.names.isEmpty())) {
			this.provider = null;
			return false;
		}
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Attribute next() {
		if ((this.provider==null)||(this.names.isEmpty()))
			throw new NoSuchElementException();
		
		String name = this.names.remove(0);
		if (name==null)
			throw new NoSuchElementException();
		
		Attribute attr = this.provider.getAttributeObject(name);
		if (attr==null)
			throw new NoSuchElementException();
		
		this.lastName = name;
		
		if (this.names.isEmpty())
			this.provider = null;
		
		return attr;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void remove() {
		if ((this.provider==null)||(this.lastName==null))
			throw new NoSuchElementException();
		if (this.provider instanceof AttributeCollection)
			((AttributeCollection)this.provider).removeAttribute(this.lastName);
		this.lastName = null;
	}
	
}
