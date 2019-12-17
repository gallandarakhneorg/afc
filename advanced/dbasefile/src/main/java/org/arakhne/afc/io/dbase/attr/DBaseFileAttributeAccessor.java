/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AbstractBufferedAttributeProvider;
import org.arakhne.afc.util.OutputParameter;

/**
 * This class permits to access to the attributes
 * stored inside a dBase file.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see DBaseFileAttributeProvider
 * @see DBaseFileAttributeProvider
 */
class DBaseFileAttributeAccessor extends AbstractBufferedAttributeProvider {

	private static final long serialVersionUID = -9217458236045169471L;

	private int recordNumber;

	/** Is the pool of attribute providers.
	 */
	private DBaseFileAttributePool pool;

	/** Constructor.
	 *
	 * @param pool is the pool associated to this accessor.
	 * @param recordNumber is the record index of this accessor.
	 */
	DBaseFileAttributeAccessor(DBaseFileAttributePool pool, int recordNumber) {
		this.recordNumber = recordNumber;
		this.pool = pool;
	}

	/** Replies the URL where the dBase file is located.
	 *
	 * @return the URL where the dBase file is located.
	 */
	@Pure
	public URL getResource() {
		return this.pool.getResource();
	}

	/** Replies record number that is red by this container.
	 *
	 * <p>This number could changed between two call to this function.
	 *
	 * @return the record number ({@code 0..recordCount-1}).
	 */
	@Pure
	public int getRecordNumber() {
		return this.recordNumber;
	}

	@Override
	@Pure
	public int getAttributeCount() {
		return this.pool.getAttributeCount();
	}

	@Override
	@Pure
	public Collection<String> getAllAttributeNames() {
		return this.pool.getAllAttributeNames(this.recordNumber);
	}

	@Override
	protected AttributeValue loadValue(String name) throws AttributeException {
		// Get the value and its type and force a cast before replying
		// to limit the problems of cast by the attribute container.
		final OutputParameter<AttributeType> param = new OutputParameter<>();
		final Object value = this.pool.getRawValue(this.recordNumber, name, param);
		return new AttributeValueImpl(param.get(), value);
	}

	@Override
	@Pure
	public void toMap(Map<String, Object> mapToFill) {
		for (final Attribute attr : attributes()) {
			try {
				mapToFill.put(attr.getName(), attr.getValue());
			} catch (Exception e) {
				//
			}
		}
	}

}
