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

import java.util.EventObject;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeValue;

/**
 * This interface representes a listener on the attribute changes
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttributeChangeEvent extends EventObject { 

	private static final long serialVersionUID = -3573147826440564995L;
	
	private final String name;
	private final String oldName;
	private final AttributeValue oldValue;
	private final AttributeValue newValue;
	private final Type type; 

	/** Create an event that describes a value update only.
	 * 
	 * @param source
	 * @param type
	 * @param oldName
	 * @param oldValue
	 * @param currentName
	 * @param currentValue
	 */
	public AttributeChangeEvent(Object source, Type type, String oldName, AttributeValue oldValue, String currentName, AttributeValue currentValue) {
		super(source);
		this.name = currentName;
		this.oldName = oldName;
		this.oldValue = oldValue;
		this.newValue = currentValue;
		this.type = type;
	}

	/** Replies the name of the changed attributes.
	 * 
	 * @return the name attribute, or <code>null</code> if this
	 * event has no name attribute.
	 */
	public String getName() {
		return this.name;
	}

	/** Replies the old name of the changed attributes.
	 * 
	 * @return the old name attribute, or <code>null</code> if this
	 * event has no old name attribute.
	 */
	public String getOldName() {
		return this.oldName;
	}

	/** Replies the old value of the attribute.
	 * 
	 * @return the attribute value or <code>null</code>
	 */
	public AttributeValue getOldValue() {
		return this.oldValue;
	}

	/** Replies the new value of the attribute.
	 * 
	 * @return the attribute value, never <code>null</code>
	 */
	public AttributeValue getValue() {
		return this.newValue;
	}

	/** Replies the changed attribute.
	 * 
	 * @return the attribute, never <code>null</code>
	 */
	public Attribute getAttribute() {
		return new AttributeImpl(this.name,this.newValue);
	}
	
	/** Replies the type of event.
	 * 
	 * @return the type of the event, neither <code>null</code>
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * This interface representes a listener on the attribute changes
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum Type {
		/** An attribute value has changed.
		 */
		VALUE_UPDATE,
		/** An attribute was removed.
		 */
		REMOVAL,
		/** an attribute was added.
		 */
		ADDITION,
		/** An attribute was renamed.
		 */
		RENAME,
		/** All attributes were removed.
		 */
		REMOVE_ALL;
	}

}
