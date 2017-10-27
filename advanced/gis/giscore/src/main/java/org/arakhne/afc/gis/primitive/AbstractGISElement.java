/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.gis.primitive;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent;
import org.arakhne.afc.attrs.collection.AttributeChangeListener;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoId;

/** Element of a GIS application.
 *
 * @param <C> is the type of the container of this element.
 * @param <T> is the type of this element, used by the cloning feature.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:methodcount")
public abstract class AbstractGISElement<C extends GISContainer<?>, T extends AbstractGISElement<C, T>>
		implements GISElement, AttributeChangeListener, GISCloneable<T> {

	private static final long serialVersionUID = 5339850330022310748L;

	/** Source of attributes.
	 */
	AttributeCollection attributeSource;

	/** the layer that contains this element.
	 */
	WeakReference<C> mapContainer;

	/** Identifier of this element.
	 */
	private transient UUID uid;

	/** Indicates if the events are fired.
	 */
	private boolean isEventFirable = true;

	/** Create a GISElement with the specified attribute provider.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the attribute provider to use. If <code>null</code> was specified
	 *     the new GISElement will use a default attribute provider (most of the time an
	 * {@link HeapAttributeCollection}
	 * @since 4.0
	 */
	public AbstractGISElement(UUID id, AttributeCollection attributeSource) {
		this.uid = id;
		if ((attributeSource == null) || (attributeSource == this)) {
			this.attributeSource = new HeapAttributeCollection();
		} else {
			this.attributeSource = attributeSource;
		}

		this.attributeSource.addAttributeChangeListener(this);
	}

	@Override
	@Pure
	public boolean isEventFirable() {
		return this.isEventFirable;
	}

	@Override
	public void setEventFirable(boolean isFirable) {
		this.isEventFirable = isFirable;
	}

	/** Clone this object to obtain a valid copy.
	 *
	 * @return a copy
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public T clone() {
		try {
			final T element = (T) super.clone();
			element.attributeSource = this.attributeSource.clone();
			element.attributeSource.addAttributeChangeListener(element);
			element.mapContainer = null;
			return element;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onAttributeChangeEvent(AttributeChangeEvent event) {
		//
	}

	/** Sets the container of this MapElement.
	 *
	 * @param container the new container.
	 * @return the success state of the operation.
	 */
	public boolean setContainer(C container) {
		this.mapContainer = container == null ? null : new WeakReference<>(container);
		return true;
	}

	/** Replies the object which contains this MapElement.
	 *
	 * @return the container or <code>null</code>
	 */
	@Pure
	public C getContainer() {
		return this.mapContainer == null ? null : this.mapContainer.get();
	}

	/** Replies the top-most object which contains this MapElement.
	 *
	 * @return the top container or <code>null</code>
	 */
	@Pure
	public Object getTopContainer() {
		if (this.mapContainer == null) {
			return null;
		}
		final C container = this.mapContainer.get();
		if (container == null) {
			return null;
		}
		if (container instanceof GISContentElement<?>) {
			return ((GISContentElement<?>) container).getTopContainer();
		}
		return container;
	}

	/** Replies the attribute provider associated to this element.
	 *
	 * @return the attributre provider
	 */
	@Pure
	public AttributeCollection getAttributeCollection() {
		if (this.attributeSource == null) {
			throw new NoAttributeProviderFoundException();
		}
		return this.attributeSource;
	}

	/** Replies the attribute container associated to this element.
	 *
	 * @return the attribute container
	 */
	@Pure
	public AttributeProvider getAttributeProvider() {
		if (this.attributeSource == null) {
			throw new NoAttributeProviderFoundException();
		}
		return this.attributeSource;
	}

	@Override
	@Pure
	public final GeoId getGeoId() {
		return getGeoLocation().toGeoId();
	}

	/** Replies an unique identifier for element.
	 *
	 * <p>A Unique IDentifier (UID) must be unique for all the object instances.
	 *
	 * <p>The following code is always true (where the arguments of the
	 * constructors are the list of points of the polyline). It illustrates
	 * that for two elements with the same geo-localized points, they
	 * have the same geo-location identifier (Geo-Id) and they
	 * have different unique ientifier (Uid):
	 * <pre><code>
	 * GISElement obj1 = new MapPolyline(100,10,200,30,300,4);
	 * GISElement obj2 = new MapPolyline(100,10,200,30,300,4);
	 * assert( obj1.getGeoId().equals(obj2.getGeoId()) );
	 * assert( obj2.getGeoId().equals(obj1.getGeoId()) );
	 * assert( ! obj1.getUid().equals(obj2.getUid()) );
	 * assert( ! obj2.getUid().equals(obj1.getUid()) );
	 * </code></pre>
	 *
	 * @return an identifier
	 * @since 4.0
	 */
	@Override
	@Pure
	public UUID getUUID() {
		if (this.uid == null) {
			this.uid = UUID.randomUUID();
		}
		return this.uid;
	}

	/** Set the unique identifier for element.
	 *
	 * <p>A Unique IDentifier (UID) must be unique for all the object instances.
	 *
	 * @param id is the new identifier
	 * @since 4.0
	 */
	protected void setUUID(UUID id) {
		this.uid = id;
	}

	@Override
	public void addAttributeChangeListener(AttributeChangeListener listener) {
		getAttributeCollection().addAttributeChangeListener(listener);
	}

	@Override
	public void removeAttributeChangeListener(AttributeChangeListener listener) {
		getAttributeCollection().removeAttributeChangeListener(listener);
	}

	@Override
	public int copyAttributes(GISElement container) {
		final Collection<Attribute> source = container.getAllAttributes();
		if (source != null) {
			int count = 0;
			final AttributeCollection provider = getAttributeCollection();
			for (final Attribute attr : source) {
				try {
					provider.setAttribute(attr);
					++count;
				} catch (AttributeException e) {
					//
				}
			}
			return count;
		}
		return 0;
	}

	@Override
	@Pure
	public String getName() {
		final AttributeValue val = getAttributeCollection().getAttribute(ATTR_NAME);
		try {
			if (val != null) {
				return val.getString();
			}
		} catch (AttributeException e) {
			//
		}
		return null;
	}

	@Override
	public void setName(String name) {
		try {
			getAttributeCollection().setAttribute(new AttributeImpl(ATTR_NAME, name));
		} catch (AttributeException e) {
			//
		}
	}

	@Override
	@Pure
	public int getAttributeCount() {
		return getAttributeProvider().getAttributeCount();
	}

	@Override
	@Pure
	public boolean hasAttribute(String name) {
		return getAttributeCollection().hasAttribute(name);
	}

	@Override
	@Pure
	public Collection<Attribute> getAllAttributes() {
		return getAttributeCollection().getAllAttributes();
	}

	@Override
	@Pure
	public Iterable<Attribute> attributes() {
		return getAttributeCollection().attributes();
	}

	@Override
	@Pure
	public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
		return getAttributeCollection().getAllAttributesByType();
	}

	@Override
	@Pure
	public Collection<String> getAllAttributeNames() {
		return getAttributeCollection().getAllAttributeNames();
	}

	@Override
	@Pure
	public AttributeValue getAttribute(String name) {
		return getAttributeCollection().getAttribute(name);
	}

	@Override
	@Pure
	public boolean getAttribute(String name, boolean defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public int getAttribute(String name, int defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public long getAttribute(String name, long defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public float getAttribute(String name, float defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public double getAttribute(String name, double defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public String getAttribute(String name, String defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public UUID getAttribute(String name, UUID defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public URL getAttribute(String name, URL defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public URI getAttribute(String name, URI defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public Date getAttribute(String name, Date defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public InetAddress getAttribute(String name, InetAddress defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public InetAddress getAttribute(String name, InetSocketAddress defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public <ET extends Enum<ET>> ET getAttribute(String name, ET defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public Class<?> getAttribute(String name, Class<?> defaultValue) {
		return getAttributeCollection().getAttribute(name, defaultValue);
	}

	@Override
	@Pure
	public AttributeValue getAttribute(String name, AttributeValue default_value) {
		return getAttributeCollection().getAttribute(name, default_value);
	}

	@Override
	@Pure
	public Attribute getAttributeObject(String name) {
		return getAttributeCollection().getAttributeObject(name);
	}

	@Override
	@Pure
	public boolean getAttributeAsBool(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsBool(name);
	}

	@Override
	@Pure
	public int getAttributeAsInt(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsInt(name);
	}

	@Override
	@Pure
	public long getAttributeAsLong(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsLong(name);
	}

	@Override
	@Pure
	public float getAttributeAsFloat(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsFloat(name);
	}

	@Override
	@Pure
	public double getAttributeAsDouble(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsDouble(name);
	}

	@Override
	@Pure
	public String getAttributeAsString(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsString(name);
	}

	@Override
	@Pure
	public UUID getAttributeAsUUID(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsUUID(name);
	}

	@Override
	@Pure
	public URL getAttributeAsURL(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsURL(name);
	}

	@Override
	@Pure
	public URI getAttributeAsURI(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsURI(name);
	}

	@Override
	@Pure
	public Date getAttributeAsDate(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsDate(name);
	}

	@Override
	@Pure
	public InetAddress getAttributeAsInetAddress(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsInetAddress(name);
	}

	@Override
	@Pure
	public <ET extends Enum<ET>> ET getAttributeAsEnumeration(String name, Class<ET> type) throws AttributeException {
		return getAttributeCollection().getAttributeAsEnumeration(name, type);
	}

	@Override
	@Pure
	public Enum<?> getAttributeAsEnumeration(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsEnumeration(name);
	}

	@Override
	@Pure
	public Class<?> getAttributeAsJavaClass(String name) throws AttributeException {
		return getAttributeCollection().getAttributeAsJavaClass(name);
	}

	@Override
	public Attribute setAttribute(String name, AttributeValue value) throws AttributeException {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, boolean value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, int value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, long value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, float value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, double value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, String value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, UUID value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, URL value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, URI value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, Date value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, InetAddress value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, InetSocketAddress value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, Enum<?> value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(String name, Class<?> value) {
		return getAttributeCollection().setAttribute(name, value);
	}

	@Override
	public Attribute setAttribute(Attribute value) throws AttributeException {
		return getAttributeCollection().setAttribute(value);
	}

	@Override
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
		return getAttributeCollection().setAttributeType(name, type);
	}

	@Override
	public boolean removeAttribute(String name) {
		return getAttributeCollection().removeAttribute(name);
	}

	@Override
	public boolean removeAllAttributes() {
		return getAttributeCollection().removeAllAttributes();
	}

	@Override
	public final boolean renameAttribute(String oldname, String newname) {
		return getAttributeCollection().renameAttribute(oldname, newname);
	}

	@Override
	public boolean renameAttribute(String oldname, String newname, boolean overwrite) {
		return getAttributeCollection().renameAttribute(oldname, newname, overwrite);
	}

	@Override
	public void freeMemory() {
		getAttributeCollection().freeMemory();
	}

	@Override
	public void setAttributes(Map<String, Object> content) {
		getAttributeCollection().setAttributes(content);
	}

	@Override
	public void setAttributes(AttributeProvider content) throws AttributeException {
		getAttributeCollection().setAttributes(content);
	}

	@Override
	public void addAttributes(Map<String, Object> content) {
		getAttributeCollection().addAttributes(content);
	}

	@Override
	public void addAttributes(AttributeProvider content) throws AttributeException {
		getAttributeCollection().addAttributes(content);
	}

	@Override
	public void flush() {
		getAttributeCollection().flush();
	}

	@Override
	@Pure
	public Map<String, Object> toMap() {
		return getAttributeCollection().toMap();
	}

	@Override
	public void toMap(Map<String, Object> mapToFill) {
		getAttributeCollection().toMap(mapToFill);
	}

	@Override
	@Pure
	public String hashKey() {
		final StringBuilder buf = new StringBuilder();
		buf.append(getClass().getName());
		buf.append('@');
		buf.append(Integer.toHexString(hashCode()));
		return buf.toString();
	}

	@Override
	@Pure
	public String toString() {
		final GeoId geoId = getGeoId();
		if (geoId != null) {
			return geoId.toString();
		}
		final StringBuilder buf = new StringBuilder();
		final String name = getName();
		final UUID id = getUUID();
		buf.append('<');
		if (name != null) {
			buf.append(name);
			buf.append('|');
		}
		if (id != null) {
			buf.append(id.toString());
			buf.append('|');
		}
		buf.append(hashKey());
		buf.append('>');
		return buf.toString();
	}

}
