/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeValue;

/**
 * Stub for CacheProvider.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
class CacheProviderStub extends BufferedAttributeCollection {

	private static final long serialVersionUID = -1506117010970624098L;
	
	private final Map<String, Attribute> attributes = new HashMap<>();
	
	/**
	 */
	public CacheProviderStub() {
		//
	}

	/**
	 * @param attributes
	 */
	public CacheProviderStub(Attribute...attributes) {
		for (Attribute attr : attributes) {
			this.attributes.put(attr.getName(), attr);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttributeCount() {
		return this.attributes.size();
	}

	@Override
	public Collection<String> getAllAttributeNames() {
		return new ArrayList<>(this.attributes.keySet());
	}

	@Override
	protected AttributeValue loadValue(String name) throws AttributeException {
		Attribute attr = this.attributes.get(name);
		if (attr!=null) return attr;
		throw new NoAttributeFoundException(name);
	}

	@Override
	protected boolean removeAllValues() throws AttributeException {
		this.attributes.clear();
		return true;
	}

	@Override
	protected AttributeValue removeValue(String name) throws AttributeException {
		return this.attributes.remove(name);
	}

	@Override
	protected void saveValue(String name, AttributeValue value) throws AttributeException {
		Attribute attr = this.attributes.get(name);
		if (attr!=null) {
			attr.setValue(value);
		}
		else {
			this.attributes.put(name, new AttributeImpl(name,value));
		}
	}

	@Override
	public void setAttributes(Map<String, Object> content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttributes(AttributeProvider content)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAttributes(Map<String, Object> content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAttributes(AttributeProvider content)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void toMap(Map<String, Object> mapToFill) {
		for(Attribute attr : this.attributes.values()) {
			if (attr.isAssigned()) {
				try {
					mapToFill.put(attr.getName(), attr.getValue());
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

}

/**
 * Test for BufferedAttributeProvider.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class BufferedAttributeCollectionTest extends AbstractAttributeCollectionTest<CacheProviderStub> {

	/**
	 */
	public BufferedAttributeCollectionTest() {
		super("BufferedAttributeProviderTest");  //$NON-NLS-1$
	}
	
	@Override
	protected CacheProviderStub setUpTestCase() throws Exception {
		return new CacheProviderStub(this.baseData);
	}
	
}

	