/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.vmutil.json.JsonableObject;

/**
 * This interface representes a provider of attributes
 * that provide a reading API only.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AttributeProvider  extends Cloneable, Serializable, JsonableObject {

	/** Make a deep copy of this object and replies the copy.
	 *
	 * @return the deep copy.
	 */
	@Pure
	AttributeProvider clone();

	/** Replies the count of attributes.
	 *
	 * @return the count of attributes.
	 */
	@Pure
	int getAttributeCount();

	/** Replies if the given attribute exists.
	 *
	 * @param name the name.
	 * @return <code>true</code> is an attribute with the given name exists, otherwise <code>false</code>
	 */
	@Pure
	boolean hasAttribute(String name);

	/** Replies all the attributes.
	 *
	 * @return the list of all attributes
	 */
	@Pure
	Collection<Attribute> getAllAttributes();

	/** Replies all the attributes.
	 *
	 * @return an iterable object that contains the attributes.
	 */
	@Pure
	Iterable<Attribute> attributes();

	/** Replies all the attributes sorted by type.
	 *
	 * <p>The keys of the returned hashtable are the types and
	 * the values are array of attributes ({@link java.util.Vector}).
	 *
	 * @return the attributes grouped by type.
	 */
	@Pure
	Map<AttributeType, Collection<Attribute>> getAllAttributesByType();

	/** Replies all the attribute names.
	 * This function never load the attribute values even
	 * if they are not inside the storage layer.
	 *
	 * @return the list of all attribute names.
	 */
	@Pure
	Collection<String> getAllAttributeNames();

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value or <code>null</code>
	 */
	@Pure
	AttributeValue getAttribute(String name);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	boolean getAttribute(String name, boolean defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	int getAttribute(String name, int defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	long getAttribute(String name, long defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	float getAttribute(String name, float defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	double getAttribute(String name, double defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	String getAttribute(String name, String defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	UUID getAttribute(String name, UUID defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	URL getAttribute(String name, URL defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	URI getAttribute(String name, URI defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	Date getAttribute(String name, Date defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value or <code>null</code>
	 */
	@Pure
	AttributeValue getAttribute(String name, AttributeValue defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	InetAddress getAttribute(String name, InetAddress defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	InetAddress getAttribute(String name, InetSocketAddress defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param <T> is the type of the enumeration.
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	<T extends Enum<T>> T getAttribute(String name, T defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	@Pure
	Class<?> getAttribute(String name, Class<?> defaultValue);

	/** Replies the attribute with the given name.
	 *
	 * @param name the name.
	 * @return the attribute or <code>null</code>
	 */
	@Pure
	Attribute getAttributeObject(String name);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	boolean getAttributeAsBool(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	int getAttributeAsInt(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	long getAttributeAsLong(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	float getAttributeAsFloat(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	double getAttributeAsDouble(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	String getAttributeAsString(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	UUID getAttributeAsUUID(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	URL getAttributeAsURL(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	URI getAttributeAsURI(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	Date getAttributeAsDate(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	InetAddress getAttributeAsInetAddress(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	Enum<?> getAttributeAsEnumeration(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param <T> is the type of the enumeration to retreive.
	 * @param name the name.
	 * @param type is the type of the enumeration to retreive.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	<T extends Enum<T>> T getAttributeAsEnumeration(String name, Class<T> type) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	@Pure
	Class<?> getAttributeAsJavaClass(String name) throws AttributeException;

	/** Clean the internal memory-storage structures if they exist.
	 *
	 * <p>This function permits to limit the memory usage without
	 * removing the attribute value from a hard storage area (database,
	 * files...). The attribute which are freed by this method could
	 * be reloaded in memory with a call to a getting method.
	 */
	void freeMemory();

	/** Replies the map of the values stored in this attribute provider.
	 * The replied map is a copy of or an unmodifiable version
	 * of the internal map, if it exists.q
	 *
	 * @return the map, never <code>null</code>.
	 */
	@Pure
	default Map<String, Object> toMap() {
		final Map<String, Object> map = new TreeMap<>();
		toMap(map);
		return map;
	}

	/** Fill the given map with the values stored in this attribute provider.
	 *
	 * @param mapToFill is the map to fill, never <code>null</code>.
	 */
	void toMap(Map<String, Object> mapToFill);

}
