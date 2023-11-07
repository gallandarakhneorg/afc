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

package org.arakhne.afc.inputoutput.mime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class MimeNameTest {

	@Test
	public void toMimeType() {
		assertEquals(MimeName.MIME_COLLADA.getMimeConstant(), MimeName.MIME_COLLADA.toMimeType().toString());
		assertEquals(MimeName.MIME_PBM.getMimeConstant(), MimeName.MIME_PBM.toMimeType().toString());
		assertEquals(MimeName.MIME_SHAPE_FILE.getMimeConstant(), MimeName.MIME_SHAPE_FILE.toMimeType().toString());
	}

	@Test
	public void getMimeConstant() {
		assertEquals("application/x-collada", MimeName.MIME_COLLADA.getMimeConstant()); //$NON-NLS-1$
		assertEquals("image/x-portable-bitmap", MimeName.MIME_PBM.getMimeConstant()); //$NON-NLS-1$
		assertEquals("application/x-shapefile", MimeName.MIME_SHAPE_FILE.getMimeConstant()); //$NON-NLS-1$
	}

	@Test
	public void isMimeConstant() {
		assertFalse(MimeName.MIME_COLLADA.isMimeConstant(null));
		assertFalse(MimeName.MIME_COLLADA.isMimeConstant("")); //$NON-NLS-1$
		assertTrue(MimeName.MIME_COLLADA.isMimeConstant("application/x-collada")); //$NON-NLS-1$
		assertFalse(MimeName.MIME_COLLADA.isMimeConstant("application/collada")); //$NON-NLS-1$
		assertTrue(MimeName.MIME_PBM.isMimeConstant("image/x-portable-bitmap")); //$NON-NLS-1$
		assertFalse(MimeName.MIME_PBM.isMimeConstant("image/portable-bitmap")); //$NON-NLS-1$
		assertTrue(MimeName.MIME_SHAPE_FILE.isMimeConstant("application/x-shapefile")); //$NON-NLS-1$
		assertFalse(MimeName.MIME_SHAPE_FILE.isMimeConstant("application/shapefile")); //$NON-NLS-1$
	}

	@Test
	public void staticParseMimeType() {
		assertNull(MimeName.parseMimeType(null));
		assertNull(MimeName.parseMimeType("")); //$NON-NLS-1$
		assertEquals(MimeName.MIME_COLLADA.getMimeConstant(), MimeName.parseMimeType("application/x-collada").toString()); //$NON-NLS-1$
		assertEquals(MimeName.MIME_PBM.getMimeConstant(), MimeName.parseMimeType("image/x-portable-bitmap").toString()); //$NON-NLS-1$
		assertEquals(MimeName.MIME_SHAPE_FILE.getMimeConstant(), MimeName.parseMimeType("application/x-shapefile").toString()); //$NON-NLS-1$
	}

	@Test
	public void staticParseMimeName() {
		assertNull(MimeName.parseMimeName(null));
		assertNull(MimeName.parseMimeName("")); //$NON-NLS-1$
		assertSame(MimeName.MIME_COLLADA, MimeName.parseMimeName("application/x-collada")); //$NON-NLS-1$
		assertSame(MimeName.MIME_PBM, MimeName.parseMimeName("image/x-portable-bitmap")); //$NON-NLS-1$
		assertSame(MimeName.MIME_SHAPE_FILE, MimeName.parseMimeName("application/x-shapefile")); //$NON-NLS-1$
	}

}

