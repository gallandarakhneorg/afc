/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.attrs.collection;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent.Type;

/**
 * This class implements an abstract object with attributes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractAttributeCollection extends AbstractAttributeProvider implements AttributeCollection {

	private static final long serialVersionUID = 8103647267018484556L;

	private List<AttributeChangeListener> listenerList;

	private boolean isEventFirable = true;

	@Pure
	@Override
	public synchronized boolean isEventFirable() {
		return this.isEventFirable;
	}

	@Override
	public synchronized void setEventFirable(boolean isFirable) {
		this.isEventFirable = isFirable;
	}

	/** Make a deep copy of this object and replies the copy.
	 *
	 * @return the deep copy.
	 */
	@Pure
	@Override
	public AttributeCollection clone() {
		final AbstractAttributeCollection clone = (AbstractAttributeCollection) super.clone();
		clone.listenerList = null;
		return clone;
	}

	/** Fire the addition event.
	 *
	 * @param name is the name of the attribute for which the event occured.
	 * @param attr is the value of the attribute.
	 */
	protected synchronized void fireAttributeAddedEvent(String name, AttributeValue attr) {
		if (this.listenerList != null && isEventFirable()) {
			final AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			final AttributeChangeEvent event = new AttributeChangeEvent(
					this,
					Type.ADDITION,
					null,
					null,
					name,
					attr);
			for (final AttributeChangeListener listener : list) {
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
		if (this.listenerList != null && isEventFirable()) {
			final AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			final AttributeChangeEvent event = new AttributeChangeEvent(
					this,
					Type.VALUE_UPDATE,
					name,
					oldValue,
					name,
					currentValue);
			for (final AttributeChangeListener listener : list) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	/** Fire the all attribute removal event.
	 */
	protected synchronized void fireAttributeClearedEvent() {
		if (this.listenerList != null && isEventFirable()) {
			final AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			final AttributeChangeEvent event = new AttributeChangeEvent(
					this,
					Type.REMOVE_ALL,
					null,
					null,
					null,
					null);
			for (final AttributeChangeListener listener : list) {
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
		if (this.listenerList != null && isEventFirable()) {
			final AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			final AttributeChangeEvent event = new AttributeChangeEvent(
					this,
					Type.REMOVAL,
					name,
					oldValue,
					name,
					oldValue);
			for (final AttributeChangeListener listener : list) {
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
		if (this.listenerList != null && isEventFirable()) {
			final AttributeChangeListener[] list = new AttributeChangeListener[this.listenerList.size()];
			this.listenerList.toArray(list);
			final AttributeChangeEvent event = new AttributeChangeEvent(
					this,
					Type.RENAME,
					oldName,
					attr,
					newName,
					attr);
			for (final AttributeChangeListener listener : list) {
				listener.onAttributeChangeEvent(event);
			}
		}
	}

	@Override
	public synchronized void addAttributeChangeListener(AttributeChangeListener listener) {
		if (listener != null) {
			if (this.listenerList == null) {
				this.listenerList = new LinkedList<>();
			}
			this.listenerList.add(listener);
		}
	}

	@Override
	public synchronized void removeAttributeChangeListener(AttributeChangeListener listener) {
		if (listener != null && this.listenerList != null) {
			this.listenerList.remove(listener);
			if (this.listenerList.isEmpty()) {
				this.listenerList = null;
			}
		}
	}

	@Override
	public final boolean renameAttribute(String oldname, String newname) {
		return renameAttribute(oldname, newname, false);
	}

}

