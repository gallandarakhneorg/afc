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
		super("BufferedAttributeContainerTest"); //$NON-NLS-1$
	}
	
	@Override
	protected ReadOnlyCacheProviderStub setUpTestCase() throws Exception {
		return new ReadOnlyCacheProviderStub(this.baseData);
	}
	
}

	