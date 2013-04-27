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
 * version 2.1 of the License, or (at your option) any later version.
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
class CacheProviderStub extends BufferedAttributeCollection {

	private final Map<String, Attribute> attributes = new HashMap<String,Attribute>();
	
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
		return new ArrayList<String>(this.attributes.keySet());
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
public class BufferedAttributeCollectionTest extends AbstractAttributeCollectionTest<CacheProviderStub> {

	/**
	 */
	public BufferedAttributeCollectionTest() {
		super("BufferedAttributeProviderTest"); //$NON-NLS-1$
	}
	
	@Override
	protected CacheProviderStub setUpTestCase() throws Exception {
		return new CacheProviderStub(this.baseData);
	}
	
}

	