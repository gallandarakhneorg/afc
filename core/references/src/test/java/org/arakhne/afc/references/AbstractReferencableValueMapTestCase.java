/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.references;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author $Author: sgalland$
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
	
	@Override
	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
		this.loosedKeys = new TreeSet<>();
	}
	
	@Override
	@AfterEach
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
