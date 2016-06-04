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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeValue;

/**
 * Stub for BufferedAttributeContrainer.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
class ReadOnlyCacheProviderStub extends AbstractBufferedAttributeProvider {

	private static final long serialVersionUID = 6750547004777893458L;
	
	private final Attribute[] attributes;
	
	/**
	 */
	public ReadOnlyCacheProviderStub() {
		this.attributes = new Attribute[0];
	}

	/**
	 * @param attributes
	 */
	public ReadOnlyCacheProviderStub(Attribute...attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttributeCount() {
		return this.attributes.length;
	}

	@Override
	public Collection<String> getAllAttributeNames() {
		ArrayList<String> list = new ArrayList<String>();
		for (Attribute attr : this.attributes) {
			list.add(attr.getName());
		}
		return list;
	}

	@Override
	protected AttributeValue loadValue(String name) throws AttributeException {
		for (Attribute attr : this.attributes) {
			if (attr.getName().equals(name)) {
				return attr;
			}
		}
		throw new NoAttributeFoundException(name);
	}

	@Override
	public void toMap(Map<String, Object> mapToFill) {
		for(Attribute attr : this.attributes) {
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
 * Test for BufferedAttributeContainer.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AbstractBufferedAttributeProviderTest extends AbstractAttributeProviderTest<ReadOnlyCacheProviderStub> {

	/**
	 */
	public AbstractBufferedAttributeProviderTest() {
		super("BufferedAttributeContainerTest"); 
	}
	
	@Override
	protected ReadOnlyCacheProviderStub setUpTestCase() throws Exception {
		return new ReadOnlyCacheProviderStub(this.baseData);
	}
	
}

	