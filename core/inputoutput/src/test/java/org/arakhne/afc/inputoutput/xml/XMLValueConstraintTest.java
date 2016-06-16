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

package org.arakhne.afc.inputoutput.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

@SuppressWarnings("all")
public class XMLValueConstraintTest {

	private String name;

	private UUID value;

	private XMLValueConstraint<UUID> constraint;
	
	@Before
	public void setUp() {
		this.name = UUID.randomUUID().toString();
		this.value = UUID.randomUUID();
		this.constraint = new XMLValueConstraint<UUID>(this.name, this.value) {

			@Override
			protected UUID convertValue(String stringValue) {
				return UUID.fromString(stringValue);
			}
			
		};
	}
	
	@After
	public void tearDown() {
		this.constraint = null;
		this.name = null;
	}
	
	@Test
	public void isValidElement_valid() {
		Element element = mock(Element.class);
		when(element.getAttribute(anyString())).then((inv) -> {
			if (this.name.equals(inv.getArgumentAt(0, String.class))) {
				return this.value.toString();
			}
			fail("Invalid attribute name"); //$NON-NLS-1$
			return null;
		});
		assertTrue(this.constraint.isValidElement(element));
	}

	@Test
	public void isValidElement_invalid() {
		Element element = mock(Element.class);
		when(element.getAttribute(anyString())).then((inv) -> {
			if (this.name.equals(inv.getArgumentAt(0, String.class))) {
				return UUID.randomUUID().toString();
			}
			fail("Invalid attribute name"); //$NON-NLS-1$
			return null;
		});
		assertFalse(this.constraint.isValidElement(element));
	}

}
