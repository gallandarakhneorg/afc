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

package org.arakhne.afc.gis.maplayer;

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
import org.arakhne.afc.attrs.collection.AttributeChangeListener;
import org.arakhne.afc.attrs.collection.AttributeCollection;

/** Abstract Unit tests for MapElement layers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
abstract class AbstractAttributeCollectionStub implements AttributeCollection {

	private static final long serialVersionUID = 8399374155932327279L;

	@Override
	public AbstractAttributeCollectionStub clone() {
		try {
			return (AbstractAttributeCollectionStub)super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addAttributeChangeListener(AttributeChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAllAttributes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAttribute(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttributeChangeListener(AttributeChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean renameAttribute(String oldname, String newname) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, AttributeValue value) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, int value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, long value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, double value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, UUID value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, URI value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, URL value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, Date value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, InetAddress value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, InetSocketAddress value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, Enum<?> value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(String name, Class<?> value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttribute(Attribute value) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Attribute> attributes() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return a similar instance (aka a clone or something very near).
	 */
	public AttributeCollection createSimilarInstance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void freeMemory() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getAttributeCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<String> getAllAttributeNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Attribute> getAllAttributes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public AttributeValue getAttribute(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AttributeValue getAttribute(String name, AttributeValue default_value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getAttributeAsBool(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getAttribute(String name, boolean defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getAttributeAsDouble(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getAttribute(String name, double defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getAttributeAsFloat(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getAttribute(String name, float defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getAttributeAsInt(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getAttribute(String name, int defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getAttributeAsLong(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getAttribute(String name, long defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAttributeAsString(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAttribute(String name, String defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UUID getAttributeAsUUID(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public UUID getAttribute(String name, UUID defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getAttributeAsURL(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getAttribute(String name, URL defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public URI getAttributeAsURI(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public URI getAttribute(String name, URI defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InetAddress getAttributeAsInetAddress(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enum<?> getAttributeAsEnumeration(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <ET extends Enum<ET>> ET getAttributeAsEnumeration(String name, Class<ET> type) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?> getAttributeAsJavaClass(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getAttributeAsDate(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getAttribute(String name, Date defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InetAddress getAttribute(String name, InetAddress defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InetAddress getAttribute(String name, InetSocketAddress defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <ET extends Enum<ET>> ET getAttribute(String name, ET defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?> getAttribute(String name, Class<?> defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attribute getAttributeObject(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasAttribute(String name) {
		throw new UnsupportedOperationException();
	}

}
