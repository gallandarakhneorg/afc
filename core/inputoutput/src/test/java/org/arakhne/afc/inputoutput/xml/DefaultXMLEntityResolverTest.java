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

package org.arakhne.afc.inputoutput.xml;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;

@SuppressWarnings("all")
public class DefaultXMLEntityResolverTest extends AbstractTestCase {

	private DefaultXMLEntityResolver resolver;
	
	@BeforeEach
	public void setUp() {
		this.resolver = new DefaultXMLEntityResolver();
	}
	
	@AfterEach
	public void tearDown() {
		this.resolver = null;
	}
	
	@Test
	public void resolveEntityStringString_resourceUrl() throws Exception {
		URL resource = Resources.getResource(getClass(), "xmlResource.txt"); //$NON-NLS-1$
		InputSource src = this.resolver.resolveEntity(null, resource.toString());
		assertNotNull(src);
		assertNotNull(src.getByteStream());
	}

	@Test
	public void resolveEntityStringString_resourceName() throws Exception {
		String resource = getClass().getPackage().getName().replace('.', '/') + "/xmlResource.txt"; //$NON-NLS-1$
		InputSource src = this.resolver.resolveEntity(null, resource);
		assertNotNull(src);
		assertNotNull(src.getByteStream());
	}

}
