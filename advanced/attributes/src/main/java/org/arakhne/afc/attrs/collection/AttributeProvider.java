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

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;

/**
 * This interface representes a provider of attributes
 * that provide a reading API only.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AttributeProvider  extends Cloneable, Serializable {

	/** Make a deep copy of this object and replies the copy.
	 *
	 * @return the deep copy.
	 */
	AttributeProvider clone();

	/** Replies the count of attributes.
	 *
	 * @return the count of attributes.
	 */
	int getAttributeCount();

	/** Replies if the given attribute exists.
	 *
	 * @param name the name.
	 * @return <code>true</code> is an attribute with the given name exists, otherwise <code>false</code>
	 */
	boolean hasAttribute(String name);

	/** Replies all the attributes.
	 *
	 * @return the list of all attributes
	 */
	Collection<Attribute> getAllAttributes();

	/** Replies all the attributes.
	 *
	 * @return an iterable object that contains the attributes.
	 */
	Iterable<Attribute> attributes();

	/** Replies all the attributes sorted by type.
	 *
	 * <p>The keys of the returned hashtable are the types and
	 * the values are array of attributes ({@link java.util.Vector}).
	 *
	 * @return the attributes grouped by type.
	 */
	Map<AttributeType, Collection<Attribute>> getAllAttributesByType();

	/** Replies all the attribute names.
	 * This function never load the attribute values even
	 * if they are not inside the storage layer.
	 *
	 * @return the list of all attribute names.
	 */
	Collection<String> getAllAttributeNames();

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value or <code>null</code>
	 */
	AttributeValue getAttribute(String name);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	boolean getAttribute(String name, boolean defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	int getAttribute(String name, int defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	long getAttribute(String name, long defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	float getAttribute(String name, float defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	double getAttribute(String name, double defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	String getAttribute(String name, String defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	UUID getAttribute(String name, UUID defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	URL getAttribute(String name, URL defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	URI getAttribute(String name, URI defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 * @deprecated since 13.0
	 */
	@Deprecated
	Image getAttribute(String name, Image defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	Date getAttribute(String name, Date defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 * @deprecated since 13.0
	 */
	@Deprecated
	Color getAttribute(String name, Color defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value or <code>null</code>
	 */
	AttributeValue getAttribute(String name, AttributeValue defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	InetAddress getAttribute(String name, InetAddress defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	InetAddress getAttribute(String name, InetSocketAddress defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param <T> is the type of the enumeration.
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	<T extends Enum<T>> T getAttribute(String name, T defaultValue);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @param defaultValue is the default value replied if the attribute
	 *     has no value.
	 * @return the value
	 */
	Class<?> getAttribute(String name, Class<?> defaultValue);

	/** Replies the attribute with the given name.
	 *
	 * @param name the name.
	 * @return the attribute or <code>null</code>
	 */
	Attribute getAttributeObject(String name);

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	boolean getAttributeAsBool(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	int getAttributeAsInt(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	long getAttributeAsLong(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	float getAttributeAsFloat(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	double getAttributeAsDouble(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	String getAttributeAsString(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	UUID getAttributeAsUUID(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	URL getAttributeAsURL(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	URI getAttributeAsURI(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 * @deprecated since 13.0
	 */
	@Deprecated
	Image getAttributeAsImage(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	Date getAttributeAsDate(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 * @deprecated since 13.0
	 */
	@Deprecated
	Color getAttributeAsColor(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	InetAddress getAttributeAsInetAddress(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	Enum<?> getAttributeAsEnumeration(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param <T> is the type of the enumeration to retreive.
	 * @param name the name.
	 * @param type is the type of the enumeration to retreive.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	<T extends Enum<T>> T getAttributeAsEnumeration(String name, Class<T> type) throws AttributeException;

	/** Replies the value for the given attribute.
	 *
	 * @param name the name.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
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
	Map<String, Object> toMap();

	/** Fill the given map with the values stored in this attribute provider.
	 *
	 * @param mapToFill is the map to fill, never <code>null</code>.
	 */
	void toMap(Map<String, Object> mapToFill);

}
