/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;

/**
 * This interface representes an object that owns a
 * collection of attributes with a reading and a
 * writing API.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AttributeCollection extends AttributeProvider {

	/** Make a deep copy of this object and replies the copy.
	 *
	 * @return the deep copy.
	 */
	@Pure
	@Override
	AttributeCollection clone();

	/** Set the content of this collection from the given map.
	 * Any previous content of this attribute collection will
	 * be lost.
	 * This function is equivalent to:
	 * <pre><code>
	 * this.removeAllAttributes();
	 * this.addAttributes(content);
	 * </code></pre>
	 *
	 * @param content is the content.
	 * @see #addAttributes(Map)
	 */
	void setAttributes(Map<String, Object> content);

	/** Set the content of this collection from the given map.
	 * Any previous content of this attribute collection will
	 * be lost.
	 * This function is equivalent to:
	 * <pre><code>
	 * this.removeAllAttributes();
	 * this.addAttributes(content);
	 * </code></pre>
	 *
	 * @param content is the content.
	 * @throws AttributeException if one attribute from the content cannot be inserted.
	 * @see #addAttributes(AttributeProvider)
	 */
	void setAttributes(AttributeProvider content) throws AttributeException;

	/** Put the values given as parameter in this attribute provider.
	 * Any previous content of this attribute collection will remain
	 * if the keys are not inside the given content.
	 * If the values from the given content will be used to overwrite
	 * any existing value.
	 *
	 * @param content is the content to add inside.
	 * @see #setAttributes(Map)
	 */
	void addAttributes(Map<String, Object> content);

	/** Put the values given as parameter in this attribute provider.
	 * Any previous content of this attribute collection will remain
	 * if the keys are not inside the given content.
	 * If the values from the given content will be used to overwrite
	 * any existing value.
	 *
	 * @param content is the content to add inside.
	 * @throws AttributeException if one attribute from the content cannot be inserted.
	 * @see #addAttributes(AttributeProvider)
	 */
	void addAttributes(AttributeProvider content) throws AttributeException;

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 * @throws AttributeException on error.
	 */
	Attribute setAttribute(String name, AttributeValue value) throws AttributeException;

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, boolean value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, int value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, long value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, float value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, double value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, String value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, UUID value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, URL value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, URI value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 * @deprecated since 13.0
	 */
	@Deprecated
	Attribute setAttribute(String name, Image value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, Date value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 * @deprecated since 13.0
	 */
	@Deprecated
	Attribute setAttribute(String name, Color value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, InetAddress value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, InetSocketAddress value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, Enum<?> value);

	/** Set the value for the given attribute.
	 *
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	Attribute setAttribute(String name, Class<?> value);

	/** Set the value for the given attribute.
	 *
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 * @throws AttributeException on error.
	 */
	Attribute setAttribute(Attribute value) throws AttributeException;

	/** Set the type of the attribute with the given name.
	 *
	 * @param name is the name of the attribute
	 * @param type is the desired type.
	 * @return the changed attribute or <code>null</code>
	 * @throws AttributeException on error.
	 * @since 4.0
	 */
	Attribute setAttributeType(String name, AttributeType type) throws AttributeException;

	/** Remove the given attribute.
	 *
	 * @param name is the name of the attribute to remove.
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 */
	boolean removeAttribute(String name);

	/** Remove all the attributes.
	 *
	 * @return <code>false</code> if something wrong appends
	 */
	boolean removeAllAttributes();

	/** Rename the attribute.
	 *
	 * <p>If a attribute named <code>newname</code> already exists,
	 * this function will reply <code>false</code>.
	 *
	 * <p>This function is equivalent to {@code renameAttribute(oldname, newname, false)}.
	 *
	 * @param oldname is the name of the attribute to rename.
	 * @param newname is the new name of the attribute.
	 * @return <code>false</code> if something wrong appends
	 */
	boolean renameAttribute(String oldname, String newname);

	/** Rename the attribute .
	 *
	 * @param oldname is the name of the attribute to rename.
	 * @param newname is the new name of the attribute.
	 * @param overwrite must be <code>true</code> if the value of an
	 *     existing attribute named by <code>newname</code> must be
	 *     overwritten by the value of the attribute named <code>oldname</code>.
	 * @return <code>false</code> if something wrong appends
	 */
	boolean renameAttribute(String oldname, String newname, boolean overwrite);

	/** Add a listener on the attribute value changes.
	 *
	 * @param listener the listener.
	 */
	void addAttributeChangeListener(AttributeChangeListener listener);

	/** Remove a listener on the attribute value changes.
	 *
	 * @param listener the listener.
	 */
	void removeAttributeChangeListener(AttributeChangeListener listener);

	/** Replies if the events are fired by this container.
	 *
	 * @return <code>true</code> if the events are fired; otherwise <code>false</code>
	 *     if events are not fired.
	 */
	@Pure
	boolean isEventFirable();

	/** Set if the events are fired by this container.
	 *
	 * @param isFirable is <code>true</code> if the events are fired; otherwise <code>false</code>
	 *     if events are not fired.
	 */
	void setEventFirable(boolean isFirable);

	/** Force this provider to synchronized the memory state of the attributes
	 * with a remote storage area.
	 */
	void flush();

}
