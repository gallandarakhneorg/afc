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

package org.arakhne.afc.text;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;

import org.junit.Test;

import org.arakhne.afc.testtools.AbstractTestCase;

@SuppressWarnings("all")
public class Base64CoderTest extends AbstractTestCase {

	@Test
	public void decodeString() {
		assertInlineParameterUsage(Base64Coder.class, "decode", String.class); //$NON-NLS-1$
	}

	@Test
	public void decodeStringString() {
		assertInlineParameterUsage(Base64Coder.class, "decodeString", String.class); //$NON-NLS-1$
	}

	@Test
	public void encodeByteArray() {
		assertInlineParameterUsage(Base64Coder.class, "encode", byte[].class); //$NON-NLS-1$
	}

	@Test
	public void encodeStringString() {
		assertInlineParameterUsage(Base64Coder.class, "encodeString", String.class); //$NON-NLS-1$
	}

}
