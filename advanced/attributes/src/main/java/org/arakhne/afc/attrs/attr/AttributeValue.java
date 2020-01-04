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

package org.arakhne.afc.attrs.attr;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.vmutil.json.JsonableObject;

/**
 * This class contains a metadata value.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"checkstyle:methodcount"})
public interface AttributeValue extends Cloneable, Serializable, JsonableObject {

	/**
	 * Replies a comparator suitable for attribute values.
	 *
	 * @return a comparator, never <code>null</code>
	 */
	@Pure
	Comparator<? extends AttributeValue> valueComparator();

	/**
	 * Replies if this attribute type is
	 * a base type, ie. a number, a boolean
	 * or a string.
	 *
	 * <p>The following code is always <code>true</code>:<br>
	 * <code>isObjectValue() == !isBaseValue()</code>
	 *
	 * @return <code>true</code> if this attribute is containing a base type value,
	 *     otherwise <code>false</code>
	 * @see #isNullAllowed()
	 * @see #isObjectValue()
	 */
	@Pure
	boolean isBaseType();

	/**
	 * Replies the type of this metadata.
	 *
	 * @return the type of the attribute
	 */
	@Pure
	AttributeType getType();

	/**
	 * Change the type of this attribute.
	 *
	 * <p>The exception will be generated in case
	 * the current value could not be casted
	 * to the new type.
	 *
	 * @param type is the new type of this attribute
	 * @throws InvalidAttributeTypeException if the current value was incompatible with the given type.
	 */
	void setType(AttributeType type) throws InvalidAttributeTypeException;

	/**
	 * Change the type of this attribute.
	 *
	 * <p>The value could be lost in case the type was incompatible
	 * with the value.
	 *
	 * @param type is the new type of this attribute
	 * @return <code>true</code> if the cast was sucessfully done,
	 *         otherwhise, if the value was lost because of the
	 *         cast operation.
	 */
	boolean cast(AttributeType type);

	/**
	 * Change the type of this attribute and set its value.
	 *
	 * @param type is the new type of this attribute
	 * @param value is the new value.
	 */
	void castAndSet(AttributeType type, Object value);

	/** Replies the value attribute stored in the implementation of this interface.
	 * In opposite than {@link #getJavaObject()}, this function replies
	 * the value for all attribute type.
	 *
	 * @return the raw value of this attribute
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	Object getValue() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/** Replies the type of the internal value of this implementation of AttributeValue.
	 *
	 * @return the type of the value stored inside this attribute value implementation.
	 * @since 4.0
	 */
	@Pure
	Class<?> getInternalStorageType();

	/** Set this value with the content of the specified one.
	 *
	 * @param value the value to copy.
	 */
	void setValue(AttributeValue value);

	/** Set this value with the content of the specified one.
	 *
	 * <p>The type of the attribute will be detected from the type
	 * of the object.
	 *
	 * @param value the value.
	 */
	void setValue(Object value);

	/** Set the value to its default.
	 */
	void setToDefault();

	/** Set the value to its default if not init.
	 */
	void setToDefaultIfUninitialized();

	/**
	 * Replies the value of this metadata.
	 *
	 * @return the value
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	long getInteger() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param value the value.
	 */
	void setInteger(int value);

	/**
	 * Set the value of this metadata.
	 *
	 * @param value the value.
	 */
	void setInteger(long value);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return the value
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	double getReal() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param value the value.
	 */
	void setReal(double value);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return the value
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	String getString() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param value the value.
	 */
	void setString(String value);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return the value
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	Date getDate() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param value the value.
	 */
	void setDate(Date value);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return the value
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	boolean getBoolean() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param value the value.
	 */
	void setBoolean(boolean value);

	/**
	 * Replies of the value of this attribute is
	 * a data object ie, java object or icon.
	 *
	 * <p>The following code is always <code>true</code>:<br>
	 * <code>isObjectValue() == !isBaseValue()</code>
	 *
	 * @return <code>true</code> if this attribute contains a object as value (ie, not a base type),
	 *     otherwise <code>false</code>
	 * @see #isBaseType()
	 * @see #isNullAllowed()
	 */
	@Pure
	boolean isObjectValue();

	/**
	 * Replies the value of this metadata.
	 * In opposite than {@link #getValue()}, this function replies
	 * the value only if this attribute value if of type
	 * {@link AttributeType#OBJECT}.
	 *
	 * @param <T> is the type of the value to reply
	 * @return the value
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 * @see #getValue()
	 */
	@Pure
	<T extends Object> T getJavaObject() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param <T> is the type of the new value
	 * @param value the value.
	 */
	<T extends Object> void setJavaObject(T value);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return a timestamp with a precision in milliseconds
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	long getTimestamp() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param value the value.
	 */
	void setTimestamp(long value);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return a 3d point
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	Point3D<?, ?> getPoint3D() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param pt the point.
	 */
	void setPoint3D(Point3D<?, ?> pt);

	/**
	 * Set the value of this metadata.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 */
	void setPoint3D(float x, float y, float z);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return a 2d point
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	Point2D<?, ?> getPoint() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param pt the point.
	 */
	void setPoint(Point2D<?, ?> pt);

	/**
	 * Set the value of this metadata.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	void setPoint(float x, float y);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return an uuid
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 * @since 4.0
	 */
	@Pure
	UUID getUUID() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param uuid the uuid.
	 * @since 4.0
	 */
	void setUUID(UUID uuid);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return an url
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 * @since 4.0
	 */
	@Pure
	URL getURL() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param url the url.
	 * @since 4.0
	 */
	void setURL(URL url);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return an uri
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 * @since 4.0
	 */
	@Pure
	URI getURI() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param uri the uri.
	 * @since 4.0
	 */
	void setURI(URI uri);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return a list of 3d points
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	Point3D<?, ?>[] getPolyline3D() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param pts the points.
	 */
	void setPolyline3D(Point3D<?, ?>... pts);

	/**
	 * Set the value of this metadata.
	 *
	 * @param pts the points.
	 */
	void setPolyline3D(Collection<? extends Point3D<?, ?>> pts);

	/**
	 * Add a point to the end of the polyline.
	 *
	 * @param pts the points
	 */
	void addToPolyline3D(Point3D<?, ?>... pts);

	/**
	 * Add a point to the end of the polyline.
	 *
	 * @param pts the points.
	 */
	void addToPolyline3D(Collection<? extends Point3D<?, ?>> pts);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return a list of 2d points
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	Point2D<?, ?>[] getPolyline() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param pts the points.
	 */
	void setPolyline(Point2D<?, ?>... pts);

	/**
	 * Set the value of this metadata.
	 *
	 * @param pts the points.
	 */
	void setPolyline(Collection<? extends Point2D<?, ?>> pts);

	/**
	 * Add a point to the end of the polyline.
	 *
	 * @param pts the points
	 */
	void addToPolyline(Point2D<?, ?>... pts);

	/**
	 * Add a point to the end of the polyline.
	 *
	 * @param pts the points.
	 */
	void addToPolyline(Collection<? extends Point2D<?, ?>> pts);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return an Internet address.
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	InetAddress getInetAddress() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param address the address.
	 */
	void setInetAddress(InetAddress address);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return an enumeration.
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	Enum<?> getEnumeration() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Replies the value of this metadata.
	 *
	 * @param <T> is the type of the enumeration to reply.
	 * @param type is the type of the enumeration to reply.
	 * @return an enumeration.
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	<T extends Enum<T>> T getEnumeration(Class<T> type) throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param enumConstant the constant.
	 */
	void setEnumeration(Enum<?> enumConstant);

	/**
	 * Replies the value of this metadata.
	 *
	 * @return a Java type.
	 * @throws InvalidAttributeTypeException when type is invalid.
	 * @throws AttributeNotInitializedException when attribute is not initialized.
	 */
	@Pure
	Class<?> getJavaClass() throws InvalidAttributeTypeException, AttributeNotInitializedException;

	/**
	 * Set the value of this metadata.
	 *
	 * @param type the type.
	 */
	void setJavaClass(Class<?> type);

	/**
	 * Replies if a value was affected to this attribute.
	 *
	 * @return <code>true</code> if this attribute is containing a value,
	 *     otherwise <code>false</code>
	 */
	@Pure
	boolean isAssigned();

	/**
	 * Replies if a null value is allowed for this attribute.
	 *
	 * <p>If {@link #isBaseType()} replies <code>true</code>,
	 * this function must always replies <code>false</code>.
	 *
	 * @return <code>true</code> if <code>null</code> is assigned to this attribute,
	 *     otherwise <code>false</code>
	 * @see #isBaseType()
	 * @see #isObjectValue()
	 */
	@Pure
	boolean isNullAllowed();

	/**
	 * Set this attribute value uninitialized.
	 */
	void uninitializeValue();

	/** Force this attribute to put its value into a storage system.
	 *
	 * <p>By default, this function does nothing. It is dependant of the application
	 * implementation.
	 *
	 * @return <code>true</code> if the value was written, otherwhise <code>false</code>
	 */
	boolean flush();

	/** Replies if a value of the given attribute type may
	 * be cast to a value of this attribute type.
	 *
	 * <p>Caution: even if isAssignableFrom is replying <code>true</code>,
	 * the {@link AttributeValue#cast(AttributeType)} and
	 * {@link AttributeValue#castAndSet(AttributeType, Object)} may fail
	 * if the target type does not support a specifical value of the
	 * source type. The isAssignableFrom function replies <code>true</code>
	 * if a least one value of the source type is assignable to a value
	 * of the target type.
	 *
	 * <p>This function is equivalent to:
	 * <code>this.getType().isAssignableFrom(type)</code>
	 *
	 * @param type th etype.
	 * @return <code>true</code> if a value of the given
	 * {@code type} may be cast to a value of the same type as this;
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 */
	@Pure
	boolean isAssignableFrom(AttributeType type);

	/** Replies if a value of the given attribute type may
	 * be cast to a value of this attribute type.
	 *
	 * <p>Caution: even if isAssignableFrom is replying <code>true</code>,
	 * the {@link AttributeValue#cast(AttributeType)} and
	 * {@link AttributeValue#castAndSet(AttributeType, Object)} may fail
	 * if the target type does not support a specifical value of the
	 * source type. The isAssignableFrom function replies <code>true</code>
	 * if a least one value of the source type is assignable to a value
	 * of the target type.
	 *
	 * <p>This function is equivalent to:
	 * <code>this.getType().isAssignableFrom(value.getType())</code>
	 *
	 * @param value the value.
	 * @return <code>true</code> if the given value may be cast to
	 *     a value of the same type as this;
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 */
	@Pure
	boolean isAssignableFrom(AttributeValue value);

}
