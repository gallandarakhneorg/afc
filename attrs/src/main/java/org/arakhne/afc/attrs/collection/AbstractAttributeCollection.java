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
package org.arakhne.afc.attrs.collection;

import java.util.LinkedList;
import java.util.List;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent.Type;

/**
 * This class implements an abstract object with attributes.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractAttributeCollection extends AbstractAttributeProvider implements AttributeCollection {
	
	private List<AttributeChangeListener> listenerList = null;
	private boolean isEventFirable = true;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isEventFirable() {
		return this.isEventFirable;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void setEventFirable(boolean isFirable) {
		this.isEventFirable = isFirable;
	}
	
	/** Make a deep copy of this object and replies the copy.
	 * 
	 * @return the deep copy.
	 */
	@Override
	public AttributeCollection clone() {
		AbstractAttributeCollection clone = (AbstractAttributeCollection)super.clone();
		clone.listenerList = null;
		return clone;
	}

	/** Fire the addition event.
	 * 
	 * @param name is the name of the attribute for which the event occured.
	 * @param attr is the value of the attribute.
	 */
	protected synchronized void fireAttributeAddedEvent(String name, AttributeValue attr) {
		if (this.listenerList!=null && isEventFirable()) {
			AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			AttributeChangeEvent event = new AttributeChangeEvent(
					this, 				//source
					Type.ADDITION,		//type
					null,				//old name
					null,				//old value
					name,				//current name
					attr);				//current value
			for(AttributeChangeListener listener : list) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** Fire the attribute change event.
	 * 
	 * @param name is the name of the attribute for which the event occured.
	 * @param oldValue is the previous value of the attribute
	 * @param currentValue is the current value of the attribute
	 */
	protected synchronized void fireAttributeChangedEvent(String name, AttributeValue oldValue, AttributeValue currentValue) {
		if (this.listenerList!=null && isEventFirable()) {
			AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			AttributeChangeEvent event = new AttributeChangeEvent(
					this, 				//source
					Type.VALUE_UPDATE,	//type
					name,				//old name
					oldValue,			//old value
					name,				//current name
					currentValue);		//current value
			for(AttributeChangeListener listener : list) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** Fire the all attribute removal event.
	 */
	protected synchronized void fireAttributeClearedEvent() {
		if (this.listenerList!=null && isEventFirable()) {
			AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			AttributeChangeEvent event = new AttributeChangeEvent(
					this, 				//source
					Type.REMOVE_ALL,	//type
					null,				//old name
					null,				//old value
					null,				//current name
					null);				//current value
			for(AttributeChangeListener listener : list) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** Fire the an attribute removal event.
	 * 
	 * @param name is the name of the attribute for which the event occured.
	 * @param oldValue is the previous value of the attribute
	 */
	protected synchronized void fireAttributeRemovedEvent(String name, AttributeValue oldValue) {
		if (this.listenerList!=null && isEventFirable()) {
			AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			AttributeChangeEvent event = new AttributeChangeEvent(
					this, 				//source
					Type.REMOVAL,		//type
					name,				//old name
					oldValue,			//old value
					name,				//current name
					oldValue);			//current value
			for(AttributeChangeListener listener : list) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** Fire the renaming event.
	 * 
	 * @param oldName is the previous name of the attribute (before renaming)
	 * @param newName is the new name of the attribute (after renaming)
	 * @param attr is the value of the attribute.
	 */
	protected synchronized void fireAttributeRenamedEvent(String oldName, String newName, AttributeValue attr) {
		if (this.listenerList!=null && isEventFirable()) {
			AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			AttributeChangeEvent event = new AttributeChangeEvent(
					this, 				//source
					Type.RENAME,		//type
					oldName,			//old name
					attr,				//old value
					newName,			//current name
					attr);				//current value
			for(AttributeChangeListener listener : list) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public synchronized void addAttributeChangeListener(AttributeChangeListener listener) {
		if (listener!=null) {
			if (this.listenerList==null)
				this.listenerList = new LinkedList<AttributeChangeListener>();
			this.listenerList.add(listener);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public synchronized void removeAttributeChangeListener(AttributeChangeListener listener) {
		if (listener!=null) {
			if (this.listenerList!=null) {
				this.listenerList.remove(listener);
				if (this.listenerList.isEmpty())
					this.listenerList = null;
			}
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final boolean renameAttribute(String oldname, String newname) {
		return renameAttribute(oldname, newname, false);
	}

	/** {@inheritDoc}
	 * @deprecated see {@link #addAttributes(AttributeProvider)}
	 */
	@Deprecated
	@Override
	public final void copyFrom(AttributeProvider otherContainer) throws AttributeException {
		for (Attribute attr : otherContainer.attributes()) {
			setAttribute(attr);
		}
	}
	
}

