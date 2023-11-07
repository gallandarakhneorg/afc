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

package org.arakhne.afc.inputoutput.xml;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

@SuppressWarnings("all")
public class XMLIdentifierConstraintTest {

	private UUID id;

	private XMLIdentifierConstraint constraint;
	
	@BeforeEach
	public void setUp() {
		this.id = UUID.randomUUID();
		this.constraint = new XMLIdentifierConstraint(this.id);
	}
	
	@AfterEach
	public void tearDown() {
		this.constraint = null;
		this.id = null;
	}
	
	@Test
	public void isValidElement_valid() {
		Element element = mock(Element.class);
		when(element.getAttribute(anyString())).then((inv) -> {
			if (XMLUtil.ATTR_ID.equals(inv.getArgument(0))) {
				return this.id.toString();
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
			if (XMLUtil.ATTR_ID.equals(inv.getArgument(0))) {
				return UUID.randomUUID().toString();
			}
			fail("Invalid attribute name"); //$NON-NLS-1$
			return null;
		});
		assertFalse(this.constraint.isValidElement(element));
	}

}
