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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeError;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent.Type;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;

/**
 * This class contains a collection of attribute providers and
 * tries to gather the data.
 * This class contains a collection of AttributeProviders and
 * exhibites the values of the attributes of all these providers.
 * This class follows the following rules (in that order)
 * to retreive the value of an attribute:<ol>
 * <li>If the attribute is defined in none of the containers, throws the standard exception;</li>
 * <li>If the attribute is defined in only one of the containers, replies the attribute value itself;</li>
 * <li>If the attribute is defined in more than one container:<ol>
 * 		<li>if all the values are equal, then replies one of the attribute values;</li>
 * 		<li>if the values are not equal and all the values have equivalent types (as replied
 * 			by {@link AttributeType#isAssignableFrom(AttributeType)}), then replies an
 * 			attribute value with a "undefined" value and of the type of one of the values;</li>
 * 		<li>if the values are not equal and one of the value has not an equivalent type to
 * 			the others (as replied by {@link AttributeType#isAssignableFrom(AttributeType)}),
 * 			then replies an attribute value with a "undefined" value and of the type OBJECT.</li>
 * 		</ol></li>
 * </ol>
 * <p>
 * If an attribute is set from this AttributeProviderContainer, all the containers inside it
 * are changed.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class MultiAttributeCollection extends MultiAttributeProvider implements AttributeCollection {

	private static final long serialVersionUID = 6542692326662357040L;

	/**
	 * Boolean indicates that enable event handling from the providers, or not.
	 */
	AtomicBoolean runProviderEvents = new AtomicBoolean(true);

	private Handler eventHandler = new Handler();
	private Collection<AttributeChangeListener> listeners = null;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MultiAttributeCollection clone() {
		MultiAttributeCollection clone = (MultiAttributeCollection)super.clone();
		clone.runProviderEvents = new AtomicBoolean(true);
		clone.eventHandler = new Handler();
		clone.listeners = (this.listeners==null) ? null : new ArrayList<AttributeChangeListener>(this.listeners);
		for(AttributeProvider c : clone.containers()) {
			if (c instanceof AttributeCollection) {
				((AttributeCollection)c).addAttributeChangeListener(clone.eventHandler);
			}
		}
		return clone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAttributeContainer(AttributeProvider container) {
		if (super.addAttributeContainer(container)) {
			if (container instanceof AttributeCollection)
				((AttributeCollection)container).addAttributeChangeListener(this.eventHandler);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAttributeContainer(AttributeProvider container) {
		if (super.removeAttributeContainer(container)) {
			if (container instanceof AttributeCollection)
				((AttributeCollection)container).removeAttributeChangeListener(this.eventHandler);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttributeChangeListener(AttributeChangeListener listener) {
		if (this.listeners==null)
			this.listeners = new ArrayList<AttributeChangeListener>();
		this.listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttributeChangeListener(AttributeChangeListener listener) {
		if (this.listeners!=null) {
			this.listeners.add(listener);
			if (this.listeners.isEmpty())
				this.listeners = null;
		}
	}

	/** Notifies the listeners about the change of an attribute.
	 * 
	 * @param event
	 */
	protected void fireAttributeChange(AttributeChangeEvent event) {
		if (this.listeners!=null && isEventFirable()) {
			AttributeChangeListener[] list = new AttributeChangeListener[this.listeners.size()];
			this.listeners.toArray(list);
			for(AttributeChangeListener listener : list) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() {
		for(AttributeProvider c : containers()) {
			if (c instanceof AttributeCollection) {
				((AttributeCollection)c).flush();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllAttributes() {
		boolean b = this.runProviderEvents.getAndSet(false);
		try {
			boolean changed = false;
			for(AttributeProvider c : containers()) {
				if (c instanceof AttributeCollection) {
					changed = ((AttributeCollection)c).removeAllAttributes() | changed;
				}
			}
			if (changed) {
				this.cache.clear();
				this.names = null;
				fireAttributeChange(new AttributeChangeEvent(this, Type.REMOVE_ALL, null, null, null, null));
			}		
			return changed;
		}
		finally {
			this.runProviderEvents.set(b);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAttribute(String name) {
		boolean b = this.runProviderEvents.getAndSet(false);
		try {
			boolean changed = false;
			ManyValueAttributeValue oldValue = new ManyValueAttributeValue();
			for(AttributeProvider c : containers()) {
				assign(oldValue, c.getAttribute(name));
				if (c instanceof AttributeCollection) {
					changed = ((AttributeCollection)c).removeAttribute(name) | changed;
				}
			}
			if (changed) {
				AttributeValue value = canonize(oldValue);
				this.cache.remove(name);
				if (this.names!=null) {
					this.names.remove(name);
					if (this.names.isEmpty()) this.names = null;
				}
				fireAttributeChange(new AttributeChangeEvent(this, Type.REMOVAL, name, value, null, null));
			}
			return changed;
		}
		finally {
			this.runProviderEvents.set(b);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renameAttribute(String oldname, String newname) {
		boolean b = this.runProviderEvents.getAndSet(false);
		try {
			boolean changed = false;
			ManyValueAttributeValue currentValue = new ManyValueAttributeValue();
			for(AttributeProvider c : containers()) {
				assign(currentValue, c.getAttribute(oldname));
				if (c instanceof AttributeCollection) {
					changed = ((AttributeCollection)c).renameAttribute(oldname, newname) | changed;
				}
			}
			if (changed) {
				AttributeValue cValue = canonize(currentValue);
				this.cache.remove(oldname);
				this.cache.remove(newname);
				if (this.names!=null) {
					this.names.remove(oldname);
					this.names.add(newname);
				}
				fireAttributeChange(new AttributeChangeEvent(this, Type.RENAME, oldname, cValue, newname, cValue));
			}
			return changed;
		}
		finally {
			this.runProviderEvents.set(b);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renameAttribute(String oldname, String newname, boolean overwrite) {
		boolean b = this.runProviderEvents.getAndSet(false);
		try {
			boolean changed = false;
			ManyValueAttributeValue currentValue = new ManyValueAttributeValue();
			for(AttributeProvider c : containers()) {
				assign(currentValue, c.getAttribute(oldname));
				if (c instanceof AttributeCollection) {
					changed = ((AttributeCollection)c).renameAttribute(oldname, newname, overwrite) | changed;
				}
			}
			if (changed) {
				AttributeValue cValue = canonize(currentValue);
				this.cache.remove(oldname);
				if (this.names!=null) {
					this.names.remove(oldname);
					this.names.add(newname);
				}
				fireAttributeChange(new AttributeChangeEvent(this, Type.RENAME, oldname, cValue, newname, cValue));
			}
			return changed;
		}
		finally {
			this.runProviderEvents.set(b);
		}
	}
	
	@Override
	public void addAttributes(AttributeProvider content)
			throws AttributeException {
		for(Attribute attr : content.attributes()) {
			setAttribute(attr);
		}
	}
	
	@Override
	public void addAttributes(Map<String, Object> content) {
		AttributeType type;
		Object rawValue;
		for(Entry<String,Object> entry : content.entrySet()) {
			rawValue = entry.getValue();
			type = AttributeType.fromValue(rawValue);
			rawValue = type.cast(rawValue);
			try {
				setAttribute(entry.getKey(), new AttributeValueImpl(type, rawValue));
			}
			catch (AttributeException _) {
				// should never occur
			}
		}
	}
	
	@Override
	public void setAttributes(AttributeProvider content)
			throws AttributeException {
		removeAllAttributes();
		for(Attribute attr : content.attributes()) {
			setAttribute(attr);
		}
	}
	
	@Override
	public void setAttributes(Map<String, Object> content) {
		removeAllAttributes();
		AttributeType type;
		Object value;
		for(Entry<String,Object> entry : content.entrySet()) {
			value = entry.getValue();
			type = AttributeType.fromValue(value);
			value = type.cast(value);
			try {
				setAttribute(entry.getKey(), new AttributeValueImpl(type, value));
			}
			catch (AttributeException e) {
				throw new AttributeError(e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, AttributeValue value) throws AttributeException {
		boolean b = this.runProviderEvents.getAndSet(false);
		try {
			boolean changed = false;
			Attribute attr;
			ManyValueAttributeValue oldValue = new ManyValueAttributeValue();
			ManyValueAttributeValue newValue = new ManyValueAttributeValue();
			for(AttributeProvider c : containers()) {
				assign(oldValue, c.getAttribute(name));
				if (c instanceof AttributeCollection) {
					attr = ((AttributeCollection)c).setAttribute(name, value);
					assign(newValue, attr);
					if (attr!=null) changed = true;
				}
				else {
					assign(newValue, null);
				}
			}
			if (changed) {
				AttributeValue oValue = canonize(oldValue);
				AttributeValue nValue = canonize(newValue);
				this.cache.put(name, nValue);
				AttributeChangeEvent event;
				if (nValue==null) {
					event = new AttributeChangeEvent(this, Type.REMOVAL, name, oValue, null, null);
				}
				else if (oValue!=null && oValue.isAssigned()) {
					event = new AttributeChangeEvent(this, Type.VALUE_UPDATE, name, oValue, name, nValue);
				}
				else {
					event = new AttributeChangeEvent(this, Type.ADDITION, null, null, name, nValue);
				}
				fireAttributeChange(event);
				return new AttributeImpl(name, value);
			}
			return null;
		}
		finally {
			this.runProviderEvents.set(b);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, boolean value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, int value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, long value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, float value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, double value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, String value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, InetAddress value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, InetSocketAddress value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Enum<?> value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Class<?> value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, UUID value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, URL value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, URI value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Image value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Date value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Color value) {
		try {
			return setAttribute(name, new AttributeValueImpl(value));
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(Attribute value) throws AttributeException {
		if (value==null) return null;
		try {
			return setAttribute(value.getName(), value);
		}
		catch (AttributeException _) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
		boolean b = this.runProviderEvents.getAndSet(false);
		try {
			ManyValueAttributeValue oldValue = new ManyValueAttributeValue();
			ManyValueAttributeValue attr = new ManyValueAttributeValue();
			Attribute a;
			boolean changed = false;
			for(AttributeProvider c : containers()) {
				assign(oldValue, c.getAttribute(name));
				if (c instanceof AttributeCollection) {
					a = ((AttributeCollection)c).setAttributeType(name, type);
					assign(attr, a);
					if (a!=null) changed = true;
				}
			}
			AttributeValue cValue = canonize(attr);
			if (changed) {
				AttributeValue oValue = canonize(oldValue);
				this.cache.put(name, cValue);
				fireAttributeChange(new AttributeChangeEvent(this, Type.VALUE_UPDATE, name, oValue, name, cValue));
			}
			return new AttributeImpl(name, cValue);
		}
		finally {
			this.runProviderEvents.set(b);
		}
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private class Handler implements AttributeChangeListener {

		/**
		 */
		public Handler() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onAttributeChangeEvent(AttributeChangeEvent event) {
			if (MultiAttributeCollection.this.runProviderEvents.get()) {
				switch(event.getType()) {
				case ADDITION:
					MultiAttributeCollection.this.cache.remove(event.getName());
					if (MultiAttributeCollection.this.names!=null)
						MultiAttributeCollection.this.names.add(event.getName());
					break;
				case REMOVAL:
					MultiAttributeCollection.this.cache.remove(event.getOldName());
					if (MultiAttributeCollection.this.names!=null)
						MultiAttributeCollection.this.names.remove(event.getOldName());
					break;
				case REMOVE_ALL:
					freeMemory();
					break;
				case RENAME:
					MultiAttributeCollection.this.cache.remove(event.getOldName());
					MultiAttributeCollection.this.cache.remove(event.getName());
					if (MultiAttributeCollection.this.names!=null) {
						MultiAttributeCollection.this.names.remove(event.getOldName());
						MultiAttributeCollection.this.names.remove(event.getName());
					}
					break;
				case VALUE_UPDATE:
					MultiAttributeCollection.this.cache.remove(event.getName());
					break;
				default:
				}
			}
		}

	}

}

