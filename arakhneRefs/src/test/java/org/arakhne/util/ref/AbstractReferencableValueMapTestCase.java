/* $Id$
 * 
 * Copyright (C) 2011-12 Stephane GALLAND.
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
package org.arakhne.util.ref;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractReferencableValueMapTestCase extends AbstractMapTestCase<String,String> {

	/** Force the garbarge collector to run.
	 */
	public static void freeMemory() {
		for(int i=0; i<6; ++i) {
			System.gc();
		}
	}
	
	private final boolean looseReferences;
	
	private Set<String> loosedKeys;
	
	/**
	 * @param looseReferences
	 */
	public AbstractReferencableValueMapTestCase(boolean looseReferences) {
		this.looseReferences = looseReferences;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.loosedKeys = new TreeSet<String>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDown() throws Exception {
		this.loosedKeys = null;
		super.tearDown();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createKeyInstance(String prefix) {
		return prefix+UUID.randomUUID().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createValueInstance(String prefix) {
		return prefix+UUID.randomUUID().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initMapWith(Map<String, String> toAdd) {
		super.initMapWith(toAdd);
		if (this.looseReferences) {
			int loosedCount = this.RANDOM.nextInt(this.reference.size())+5;
			for(int i=0; !this.reference.isEmpty() && i<loosedCount; ++i) {
				int idx = this.RANDOM.nextInt(this.reference.size());
				String key = key(this.reference, idx);
				this.loosedKeys.add(key);
				this.reference.remove(key);
			}
			freeMemory();
		}
	}
	
}
