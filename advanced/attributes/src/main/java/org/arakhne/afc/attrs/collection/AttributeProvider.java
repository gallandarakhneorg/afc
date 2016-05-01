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
	public AttributeProvider clone();

	/** Replies the count of attributes
	 * 
	 * @return the count of attributes.
	 */
	public int getAttributeCount();

	/** Replies if the given attribute exists.
	 * 
	 * @param name
	 * @return <code>true</code> is an attribute with the given name exists, otherwise <code>false</code>
	 */
	public boolean hasAttribute(String name);

	/** Replies all the attributes.
	 * 
	 * @return the list of all attributes
	 */
	public Collection<Attribute> getAllAttributes();
	
	/** Replies all the attributes.
	 * 
	 * @return an iterable object that contains the attributes.
	 */
	public Iterable<Attribute> attributes();

	/** Replies all the attributes sorted by type.
	 * <p>
	 * The keys of the returned hashtable are the types and
	 * the values are array of attributes ({@link java.util.Vector}).
	 * 
	 * @return the attributes grouped by type.
	 */
	public Map<AttributeType,Collection<Attribute>> getAllAttributesByType();

	/** Replies all the attribute names.
	 * This function never load the attribute values even
	 * if they are not inside the storage layer.
	 * 
	 * @return the list of all attribute names.
	 */
	public Collection<String> getAllAttributeNames();

	/** Replies the value for the given attribute.
	 *
	 * @param name
	 * @return the value or <code>null</code>
	 */
	public AttributeValue getAttribute(String name);

	/** Replies the attribute with the given name.
	 * 
	 * @param name
	 * @return the attribute or <code>null</code>
	 */
	public Attribute getAttributeObject(String name);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public boolean getAttributeAsBool(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public int getAttributeAsInt(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public long getAttributeAsLong(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public float getAttributeAsFloat(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public double getAttributeAsDouble(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public String getAttributeAsString(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public UUID getAttributeAsUUID(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public URL getAttributeAsURL(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public URI getAttributeAsURI(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 * @deprecated
	 */
	@Deprecated
	public Image getAttributeAsImage(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public Date getAttributeAsDate(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 * @deprecated
	 */
	@Deprecated
	public Color getAttributeAsColor(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public InetAddress getAttributeAsInetAddress(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public Enum<?> getAttributeAsEnumeration(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param <T> is the type of the enumeration to retreive.
	 * @param name
	 * @param type is the type of the enumeration to retreive.
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public <T extends Enum<T>> T getAttributeAsEnumeration(String name, Class<T> type) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @return the value
	 * @throws AttributeException if the attribute was never set.
	 */
	public Class<?> getAttributeAsJavaClass(String name) throws AttributeException;

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public boolean getAttribute(String name, boolean defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public int getAttribute(String name, int defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public long getAttribute(String name, long defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public float getAttribute(String name, float defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public double getAttribute(String name, double defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public String getAttribute(String name, String defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public UUID getAttribute(String name, UUID defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public URL getAttribute(String name, URL defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public URI getAttribute(String name, URI defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 * @deprecated
	 */
	@Deprecated
	public Image getAttribute(String name, Image defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public Date getAttribute(String name, Date defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 * @deprecated
	 */
	@Deprecated
	public Color getAttribute(String name, Color defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value or <code>null</code>
	 */
	public AttributeValue getAttribute(String name, AttributeValue defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public InetAddress getAttribute(String name, InetAddress defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public InetAddress getAttribute(String name, InetSocketAddress defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param <T> is the type of the enumeration.
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public <T extends Enum<T>> T getAttribute(String name, T defaultValue);

	/** Replies the value for the given attribute.
	 * 
	 * @param name
	 * @param defaultValue is the default value replied if the attribute
	 * has no value.
	 * @return the value
	 */
	public Class<?> getAttribute(String name, Class<?> defaultValue);

	/** Clean the internal memory-storage structures if they exist.
	 * <p>
	 * This function permits to limit the memory usage without
	 * removing the attribute value from a hard storage area (database,
	 * files...). The attribute which are freed by this method could
	 * be reloaded in memory with a call to a getting method.
	 */
	public void freeMemory();

	/** Replies the map of the values stored in this attribute provider.
	 * The replied map is a copy of or an unmodifiable version
	 * of the internal map, if it exists.q
	 * 
	 * @return the map, never <code>null</code>.
	 */
	public Map<String,Object> toMap();
	
	/** Fill the given map with the values stored in this attribute provider.
	 * 
	 * @param mapToFill is the map to fill, never <code>null</code>.
	 */
	public void toMap(Map<String,Object> mapToFill);

}
