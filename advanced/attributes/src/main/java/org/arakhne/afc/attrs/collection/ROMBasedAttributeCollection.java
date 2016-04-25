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

import java.util.Collection;

import org.arakhne.afc.attrs.attr.Attribute;

/**
 * This interface representes a provider of attributes that is
 * partly based on data stored on a ROM (read-only memory).
 * <p>
 * The changed values are stored inside the memory and never
 * written back into the ROM.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ROMBasedAttributeCollection extends AttributeCollection { 

	/** Replies the list of attributes that have
	 * changed.
	 * 
	 * @return the list of the names of the attributes which are stored inside the memory buffer.
	 */
	public Collection<String> getAllBufferedAttributeNames();

	/** Replies the list of attributes that have
	 * changed.
	 * 
	 * @return the list of attributes stored inside the memory buffer.
	 */
	public Collection<Attribute> getAllBufferedAttributes();

	/** Replies if the specified attribute name corresponds to
	 * a buffered attribute value.
	 * 
	 * @param attributeName
	 * @return <code>true</code> if an attribute with the given name
	 * is stored inside the memory buffer, otherwise <code>false</code>
	 */
	public boolean isBufferedAttribute(String attributeName);

	/** Replies the count of buffered attributes.
	 * 
	 * @return the count of buffered attributes.
	 */
	public int getBufferedAttributeCount();

}
