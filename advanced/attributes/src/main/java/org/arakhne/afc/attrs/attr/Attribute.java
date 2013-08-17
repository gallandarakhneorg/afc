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
package org.arakhne.afc.attrs.attr;

import java.util.Comparator;


/**
 * This interface contains a metadata with a name.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Attribute extends AttributeValue, AttributeConstants {

	/**
	 * Replies a comparator suitable for attribute based on the names
	 * of the attributes only.
	 * 
	 * @return a comparator, never <code>null</code>
	 * @see #valueComparator()
	 * @see #comparator()
	 */
	public Comparator<? extends Attribute> nameComparator();

	/**
	 * Replies a comparator suitable for attribute based on the names
	 * of the attributes only.
	 * 
	 * @return a comparator, never <code>null</code>
	 * @see #nameComparator()
	 * @see #valueComparator()
	 */
	public Comparator<? extends Attribute> comparator();

	/** The this value with the content of the specified one.
	 * 
	 * @param value
	 * @throws InvalidAttributeTypeException
	 */
	public void setAttribute(Attribute value) throws InvalidAttributeTypeException;

	/**
	 * Replies the name of the metadata.
	 * 
	 * @return the name of the attribute.
	 */
	public String getName() ;

	/**
	 * Set the name of this metadata.
	 * 
	 * @param name
	 */
	public void setName(String name) ;
	
}