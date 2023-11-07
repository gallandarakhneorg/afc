/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import java.util.EventObject;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeValue;

/**
 * This interface representes a listener on the attribute changes.
 *
 * @author $Author: sgalland$
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
	 * @param source the source of the event.
	 * @param type the type of event.
	 * @param oldName the previous name.
	 * @param oldValue the previous value.
	 * @param currentName the current name.
	 * @param currentValue the current value.
	 */
	public AttributeChangeEvent(Object source, Type type, String oldName, AttributeValue oldValue,
			String currentName, AttributeValue currentValue) {
		super(source);
		this.name = currentName;
		this.oldName = oldName;
		this.oldValue = oldValue;
		this.newValue = currentValue;
		this.type = type;
	}

	/** Replies the name of the changed attributes.
	 *
	 * @return the name attribute, or {@code null} if this
	 *     event has no name attribute.
	 */
	@Pure
	public String getName() {
		return this.name;
	}

	/** Replies the old name of the changed attributes.
	 *
	 * @return the old name attribute, or {@code null} if this
	 *     event has no old name attribute.
	 */
	@Pure
	public String getOldName() {
		return this.oldName;
	}

	/** Replies the old value of the attribute.
	 *
	 * @return the attribute value or {@code null}
	 */
	@Pure
	public AttributeValue getOldValue() {
		return this.oldValue;
	}

	/** Replies the new value of the attribute.
	 *
	 * @return the attribute value, never {@code null}
	 */
	@Pure
	public AttributeValue getValue() {
		return this.newValue;
	}

	/** Replies the changed attribute.
	 *
	 * @return the attribute, never {@code null}
	 */
	@Pure
	public Attribute getAttribute() {
		return new AttributeImpl(this.name, this.newValue);
	}

	/** Replies the type of event.
	 *
	 * @return the type of the event, neither {@code null}
	 */
	@Pure
	public Type getType() {
		return this.type;
	}

	/**
	 * This interface representes a listener on the attribute changes.
	 *
	 * @author $Author: sgalland$
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
