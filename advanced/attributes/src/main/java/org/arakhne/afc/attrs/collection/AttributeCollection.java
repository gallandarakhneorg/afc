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
	@Override
	public AttributeCollection clone();
	
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
	public void setAttributes(Map<String,Object> content);

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
	public void setAttributes(AttributeProvider content) throws AttributeException;

	/** Put the values given as parameter in this attribute provider.
	 * Any previous content of this attribute collection will remain
	 * if the keys are not inside the given content.
	 * If the values from the given content will be used to overwrite
	 * any existing value.
	 * 
	 * @param content is the content to add inside.
	 * @see #setAttributes(Map)
	 */
	public void addAttributes(Map<String,Object> content);

	/** Put the values given as parameter in this attribute provider.
	 * Any previous content of this attribute collection will remain
	 * if the keys are not inside the given content.
	 * If the values from the given content will be used to overwrite
	 * any existing value.
	 * 
	 * @param content is the content to add inside.
	 * @see #addAttributes(AttributeProvider)
	 * @throws AttributeException if one attribute from the content cannot be inserted. 
	 */
	public void addAttributes(AttributeProvider content) throws AttributeException;

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 * @throws AttributeException on error.
	 */
	public Attribute setAttribute(String name, AttributeValue value) throws AttributeException;

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, boolean value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, int value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, long value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, float value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, double value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, String value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, UUID value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, URL value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, URI value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, Image value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, Date value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, Color value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, InetAddress value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, InetSocketAddress value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, Enum<?> value);

	/** Set the value for the given attribute.
	 * 
	 * @param name is the name of the attribute to set.
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 */
	public Attribute setAttribute(String name, Class<?> value);

	/** Set the value for the given attribute.
	 * 
	 * @param value is the value to store.
	 * @return the changed attribute or <code>null</code>
	 * @throws AttributeException 
	 */
	public Attribute setAttribute(Attribute value) throws AttributeException;

	/** Set the type of the attribute with the given name.
	 * 
	 * @param name is the name of the attribute
	 * @param type is the desired type.
	 * @return the changed attribute or <code>null</code>
	 * @throws AttributeException 
	 * @since 4.0
	 */
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException;

	/** Remove the given attribute.
	 * 
	 * @param name is the name of the attribute to remove.
	 * @return <code>true</code> on success, otherwhise <code>false</code>
	 */
	public boolean removeAttribute(String name);

	/** Remove all the attributes.
	 * 
	 * @return <code>false</code> if something wrong appends
	 */
	public boolean removeAllAttributes();
	
	/** Rename the attribute.
	 * <p>
	 * If a attribute named <code>newname</code> already exists,
	 * this function will reply <code>false</code>.
	 * <p>
	 * This function is equivalent to {@code renameAttribute(oldname,newname,false)}.
	 * 
	 * @param oldname is the name of the attribute to rename.
	 * @param newname is the new name of the attribute.
	 * @return <code>false</code> if something wrong appends
	 */
	public boolean renameAttribute(String oldname, String newname);

	/** Rename the attribute .
	 *
	 * @param oldname is the name of the attribute to rename.
	 * @param newname is the new name of the attribute.
	 * @param overwrite must be <code>true</code> if the value of an
	 * existing attribute named by <code>newname</code> must be
	 * overwritten by the value of the attribute named <code>oldname</code>. 
	 * @return <code>false</code> if something wrong appends
	 */
	public boolean renameAttribute(String oldname, String newname, boolean overwrite);

	/** Add a listener on the attribute value changes.
	 * 
	 * @param listener
	 */
	public void addAttributeChangeListener(AttributeChangeListener listener);

	/** Remove a listener on the attribute value changes.
	 * 
	 * @param listener
	 */
	public void removeAttributeChangeListener(AttributeChangeListener listener);
	
	/** Replies if the events are fired by this container.
	 * 
	 * @return <code>true</code> if the events are fired; otherwise <code>false</code>
	 * if events are not fired.
	 */
	public boolean isEventFirable();
	
	/** Set if the events are fired by this container.
	 * 
	 * @param isFirable is <code>true</code> if the events are fired; otherwise <code>false</code>
	 * if events are not fired.
	 */
	public void setEventFirable(boolean isFirable);

	/** Force this provider to synchronized the memory state of the attributes
	 * with a remote storage area.
	 */
	public void flush();

}
