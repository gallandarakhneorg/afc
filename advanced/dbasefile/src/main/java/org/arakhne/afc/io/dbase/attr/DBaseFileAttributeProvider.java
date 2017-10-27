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

package org.arakhne.afc.io.dbase.attr;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AbstractAttributeProvider;

/**
 * This class permits to access to the attributes
 * stored inside a dBase file.
 *
 * <p>This class uses a syndicate to access to a single dBase file,
 * assuming that an attribute provider
 * accesses only one record of the dBase file.
 *
 * <p>This class is a container of attributes that access to
 * one record of a dBase file.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see DBaseFileAttributeProvider
 * @see DBaseFileAttributeCollection
 */
public class DBaseFileAttributeProvider extends AbstractAttributeProvider {

	private static final long serialVersionUID = 7759049010460115596L;

	private DBaseFileAttributeAccessor accessor;

	/**
	 * @param accessor is the accessor that permits to obtain attributes from a dBase file.
	 */
	DBaseFileAttributeProvider(DBaseFileAttributeAccessor accessor) {
		assert accessor != null;
		this.accessor = accessor;
	}

	@Override
	@SuppressWarnings("checkstyle:nofinalizer")
	public void finalize() throws Throwable {
		this.accessor = null;
		super.finalize();
	}

	/** Replies the URL where the dBase file is located.
	 *
	 * @return the URL where the dBase file is located.
	 */
	@Pure
	public URL getResource() {
		return this.accessor.getResource();
	}

	/** Replies the index of the attributes inside the dBase file.
	 *
	 * @return the index of the attributes inside the dBase file.
	 */
	@Pure
	public int getRecordNumber() {
		return this.accessor.getRecordNumber();
	}

	@Override
	public void freeMemory() {
		this.accessor.freeMemory();
	}

	@Override
	@Pure
	public Collection<String> getAllAttributeNames() {
		return this.accessor.getAllAttributeNames();
	}

	@Override
	@Pure
	public Collection<Attribute> getAllAttributes() {
		return this.accessor.getAllAttributes();
	}

	@Override
	@Pure
	public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
		return this.accessor.getAllAttributesByType();
	}

	@Override
	@Pure
	public AttributeValue getAttribute(String name) {
		return this.accessor.getAttribute(name);
	}

	@Override
	@Pure
	public AttributeValue getAttribute(String name, AttributeValue default_value) {
		return this.accessor.getAttribute(name, default_value);
	}

	@Override
	@Pure
	public Attribute getAttributeObject(String name) {
		return this.accessor.getAttributeObject(name);
	}

	/**
	 * Replies the object directly form the dBase file.
	 *
	 * @param name the name.
	 * @return the object directly form the dBase file.
	 */
	protected final Attribute getAttributeObjectFromDBase(String name) {
		return this.accessor.getAttributeObject(name);
	}

	@Override
	@Pure
	public boolean hasAttribute(String name) {
		return this.accessor.hasAttribute(name);
	}

	/**
	 * Replies if the specified attribute exists inside the dBase file.
	 *
	 * @param name the name.
	 * @return <code>true</code> if the given attribute exists inside the dBase file,
	 *     otherwise <code>false</code>
	 */
	@Pure
	protected final boolean hasAttributeInDBase(String name) {
		return this.accessor.hasAttribute(name);
	}

	@Override
	@Pure
	public int getAttributeCount() {
		return this.accessor.getAttributeCount();
	}

	@Override
	@Pure
	public void toMap(Map<String, Object> mapToFill) {
		this.accessor.toMap(mapToFill);
	}

}
