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
package org.arakhne.afc.attrs.attr;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.ui.vector.Color;
import org.arakhne.afc.ui.vector.Image;

/**
 * This class contains a metadata value.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("deprecation")
public interface AttributeValue extends Cloneable, Serializable {

	/**
	 * Replies a comparator suitable for attribute values.
	 * 
	 * @return a comparator, never <code>null</code>
	 */
	public Comparator<? extends AttributeValue> valueComparator();

	/**
	 * Replies if this attribute type is
	 * a base type, ie. a number, a boolean
	 * or a string.
	 * <p>
	 * The following code is always <code>true</code>:<br>
	 * <code>isObjectValue() == !isBaseValue()</code>
	 * 
	 * @return <code>true</code> if this attribute is containing a base type value,
	 * otherwise <code>false</code>
	 * @see #isNullAllowed()
	 * @see #isObjectValue()
	 */
	public boolean isBaseType();

	/**
	 * Replies the type of this metadata.
	 * 
	 * @return the type of the attribute
	 */
	public AttributeType getType() ; 
	
	/**
	 * Change the type of this attribute.
	 * <p>
	 * The exception will be generated in case
	 * the current value could not be casted
	 * to the new type.
	 * 
	 * @param type is the new type of this attribute
	 * @throws InvalidAttributeTypeException if the current value was incompatible with the given type. 
	 */
	public void setType(AttributeType type) throws InvalidAttributeTypeException; 

	/**
	 * Change the type of this attribute.
	 * <p>
	 * The value could be lost in case the type was incompatible
	 * with the value.
	 * 
	 * @param type is the new type of this attribute
	 * @return <code>true</code> if the cast was sucessfully done,
	 *         otherwhise, if the value was lost because of the
	 *         cast operation.
	 */
	public boolean cast(AttributeType type); 

	/**
	 * Change the type of this attribute and set its value.
	 * 
	 * @param type is the new type of this attribute
	 * @param value is the new value.
	 */
	public void castAndSet(AttributeType type, Object value); 

	/** Replies the value attribute stored in the implementation of this interface.
	 * In opposite than {@link #getJavaObject()}, this function replies
	 * the value for all attribute type.
	 * 
	 * @return the raw value of this attribute
	 * @throws InvalidAttributeTypeException
	 * @throws AttributeNotInitializedException
	 */
	public Object getValue() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/** Replies the type of the internal value of this implementation of AttributeValue.
	 * 
	 * @return the type of the value stored inside this attribute value implementation.
	 * @since 4.0
	 */
	public Class<?> getInternalStorageType();

	/** The this value with the content of the specified one.
	 * 
	 * @param value
	 */
	public void setValue(AttributeValue value);
	
	/** The this value with the content of the specified one.
	 * <p>
	 * The type of the attribute will be detected from the type
	 * of the object.
	 * 
	 * @param value
	 */
	public void setValue(Object value);

	/** Set the value to its default.
	 */
	public void setToDefault() ;
	
	/** Set the value to its default if not init.
	 */
	public void setToDefaultIfUninitialized() ;

	/**
	 * Replies the value of this metadata.
	 *
	 * @return the value
	 * @throws InvalidAttributeTypeException
	 * @throws AttributeNotInitializedException
	 */
	public long getInteger() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param value
	 */
	public void setInteger(int value);

	/**
	 * Set the value of this metadata.
	 * 
	 * @param value
	 */
	public void setInteger(long value);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return the value
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public double getReal() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param value
	 */
	public void setReal(double value);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return the value
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public String getString() throws InvalidAttributeTypeException, AttributeNotInitializedException;
	
	/**
	 * Set the value of this metadata.
	 * 
	 * @param value
	 */
	public void setString(String value);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return the value
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public Date getDate() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param value
	 */
	public void setDate(Date value);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return the value
	 * @throws InvalidAttributeTypeException
	 * @throws AttributeNotInitializedException
	 */
	public boolean getBoolean() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param value
	 */
	public void setBoolean(boolean value);

	/**
	 * Replies of the value of this attribute is
	 * a data object ie, java object or icon.
	 * <p>
	 * The following code is always <code>true</code>:<br>
	 * <code>isObjectValue() == !isBaseValue()</code>
	 * 
	 * @return <code>true</code> if this attribute contains a object as value (ie, not a base type),
	 * otherwise <code>false</code>
	 * @see #isBaseType()
	 * @see #isNullAllowed()
	 */
	public boolean isObjectValue();

	/**
	 * Replies the value of this metadata.
	 * In opposite than {@link #getValue()}, this function replies
	 * the value only if this attribute value if of type
	 * {@link AttributeType#OBJECT}.
	 *
	 * @param <T> is the type of the value to reply
	 * @return the value
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 * @see #getValue()
	 */
	public <T extends Object> T getJavaObject() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param <T> is the type of the new value
	 * @param value
	 */
	public <T extends Object> void setJavaObject(T value);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return a timestamp with a precision in milliseconds
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public long getTimestamp() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param value
	 */
	public void setTimestamp(long value);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return a 3d point
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public Point3D getPoint3D() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param p
	 */
	public void setPoint3D(Point3D p);

	/**
	 * Set the value of this metadata.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPoint3D(float x, float y, float z);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return a 2d point
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public Point2D getPoint() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param p
	 */
	public void setPoint(Point2D p);

	/**
	 * Set the value of this metadata.
	 * 
	 * @param x
	 * @param y
	 */
	public void setPoint(float x, float y);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return a color
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 * @deprecated
	 */
	@Deprecated
	public Color getColor() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param c
	 * @deprecated
	 */
	@Deprecated
	public void setColor(Color c);

	/**
	 * Set the value of this metadata.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColor(float r, float g, float b);

	/**
	 * Set the value of this metadata.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setColor(float r, float g, float b, float a);

	/**
	 * Set the value of this metadata.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColor(int r, int g, int b);

	/**
	 * Set the value of this metadata.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setColor(int r, int g, int b, int a);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return an icon
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 * @deprecated
	 */
	@Deprecated
	public Image getImage() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param c
	 * @deprecated
	 */
	@Deprecated
	public void setImage(Image c);
	
	/**
	 * Replies the value of this metadata.
	 * 
	 * @return an uuid
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 * @since 4.0
	 */
	public UUID getUUID() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param u
	 * @since 4.0
	 */
	public void setUUID(UUID u);
	
	/**
	 * Replies the value of this metadata.
	 * 
	 * @return an url
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 * @since 4.0
	 */
	public URL getURL() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param u
	 * @since 4.0
	 */
	public void setURL(URL u);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return an uri
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 * @since 4.0
	 */
	public URI getURI() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param u
	 * @since 4.0
	 */
	public void setURI(URI u);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return a list of 3d points
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public Point3D[] getPolyline3D() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param p
	 */
	public void setPolyline3D(Point3D... p);

	/**
	 * Set the value of this metadata.
	 * 
	 * @param p
	 */
	public void setPolyline3D(Collection<? extends Point3D> p);

	/**
	 * Add a point to the end of the polyline
	 * 
	 * @param p
	 */
	public void addToPolyline3D(Point3D... p);

	/**
	 * Add a point to the end of the polyline
	 * 
	 * @param p
	 */
	public void addToPolyline3D(Collection<? extends Point3D> p);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return a list of 2d points
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public Point2D[] getPolyline() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param p
	 */
	public void setPolyline(Point2D... p);

	/**
	 * Set the value of this metadata.
	 * 
	 * @param p
	 */
	public void setPolyline(Collection<? extends Point2D> p);

	/**
	 * Add a point to the end of the polyline
	 * 
	 * @param p
	 */
	public void addToPolyline(Point2D... p);

	/**
	 * Add a point to the end of the polyline
	 * 
	 * @param p
	 */
	public void addToPolyline(Collection<? extends Point2D> p);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return an Internet address.
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public InetAddress getInetAddress() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param a
	 */
	public void setInetAddress(InetAddress a);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return an enumeration.
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public Enum<?> getEnumeration() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Replies the value of this metadata.
	 * 
	 * @param <T> is the type of the enumeration to reply.
	 * @param type is the type of the enumeration to reply.
	 * @return an enumeration.
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public <T extends Enum<T>> T getEnumeration(Class<T> type) throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param e
	 */
	public void setEnumeration(Enum<?> e);

	/**
	 * Replies the value of this metadata.
	 * 
	 * @return a Java type.
	 * @throws AttributeNotInitializedException
	 * @throws InvalidAttributeTypeException
	 */
	public Class<?> getJavaClass() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 * 
	 * @param e
	 */
	public void setJavaClass(Class<?> e);

	/**
	 * Replies if a value was affected to this attribute.
	 * 
	 * @return <code>true</code> if this attribute is containing a value,
	 * otherwise <code>false</code>
	 */
	public boolean isAssigned();

	/**
	 * Replies if a null value is allowed for this attribute.
	 * <p>
	 * If {@link #isBaseType()} replies <code>true</code>,
	 * this function must always replies <code>false</code>.
	 * 
	 * @return <code>true</code> if <code>null</code> is assigned to this attribute,
	 * otherwise <code>false</code>
	 * @see #isBaseType()
	 * @see #isObjectValue()
	 */
	public boolean isNullAllowed();

	/**
	 * Set this attribute value uninitialized.
	 */
	public void uninitializeValue();

	/** Force this attribute to put its value into a storage system.
	 * <p>
	 * By default, this function does nothing. It is dependant of the application
	 * implementation.
	 * 
	 * @return <code>true</code> if the value was written, otherwhise <code>false</code>
	 */
	public boolean flush();

	/** Replies if a value of the given attribute type may
	 * be cast to a value of this attribute type.
	 * <p>
	 * Caution: even if isAssignableFrom is replying <code>true</code>,
	 * the {@link AttributeValue#cast(AttributeType)} and
	 * {@link AttributeValue#castAndSet(AttributeType, Object)} may fail
	 * if the target type does not support a specifical value of the
	 * source type. The isAssignableFrom function replies <code>true</code>
	 * if a least one value of the source type is assignable to a value
	 * of the target type.
	 * <p>
	 * This function is equivalent to:
	 * <code>this.getType().isAssignableFrom(type)</code>
	 * 
	 * @param type
	 * @return <code>true</code> if a value of the given
	 * <var>type</var> may be cast to a value of the same type as this;
	 * otherwise <code>false</code>.
	 * @since 4.0
	 */
	public boolean isAssignableFrom(AttributeType type);
	
	/** Replies if a value of the given attribute type may
	 * be cast to a value of this attribute type.
	 * <p>
	 * Caution: even if isAssignableFrom is replying <code>true</code>,
	 * the {@link AttributeValue#cast(AttributeType)} and
	 * {@link AttributeValue#castAndSet(AttributeType, Object)} may fail
	 * if the target type does not support a specifical value of the
	 * source type. The isAssignableFrom function replies <code>true</code>
	 * if a least one value of the source type is assignable to a value
	 * of the target type.
	 * <p>
	 * This function is equivalent to:
	 * <code>this.getType().isAssignableFrom(value.getType())</code>
	 * 
	 * @param value
	 * @return <code>true</code> if the given value may be cast to
	 * a value of the same type as this;
	 * otherwise <code>false</code>.
	 * @since 4.0
	 */
	public boolean isAssignableFrom(AttributeValue value);

}