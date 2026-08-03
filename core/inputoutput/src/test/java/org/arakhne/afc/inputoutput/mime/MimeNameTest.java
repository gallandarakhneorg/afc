/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MimeName")
@SuppressWarnings("all")
public class MimeNameTest {

	@DisplayName("toMimeType")
	@Nested
	public class ToMimeType {

		@DisplayName("#1")
		@Test
		public void toMimeType_1() {
			assertEquals(MimeName.MIME_COLLADA.getMimeConstant(), MimeName.MIME_COLLADA.toMimeType().toString());
		}

		@DisplayName("#2")
		@Test
		public void toMimeType_2() {
			assertEquals(MimeName.MIME_PBM.getMimeConstant(), MimeName.MIME_PBM.toMimeType().toString());
		}

		@DisplayName("#3")
		@Test
		public void toMimeType_3() {
			assertEquals(MimeName.MIME_SHAPE_FILE.getMimeConstant(), MimeName.MIME_SHAPE_FILE.toMimeType().toString());
		}
	}

	@DisplayName("getMimeConstant")
	@Nested
	public class GetMimeConstant {

		@DisplayName("#1")
		@Test
		public void getMimeConstant_1() {
			assertEquals("application/x-collada", MimeName.MIME_COLLADA.getMimeConstant()); //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void getMimeConstant_2() {
			assertEquals("image/x-portable-bitmap", MimeName.MIME_PBM.getMimeConstant()); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void getMimeConstant_3() {
			assertEquals("application/x-shapefile", MimeName.MIME_SHAPE_FILE.getMimeConstant()); //$NON-NLS-1$
		}
	}

	@DisplayName("isMimeConstant")
	@Nested
	public class IsMimeConstant {

		@DisplayName("#1")
		@Test
		public void isMimeConstant_1() {
			assertFalse(MimeName.MIME_COLLADA.isMimeConstant(null));
		}

		@DisplayName("#2")
		@Test
		public void isMimeConstant_2() {
			assertFalse(MimeName.MIME_COLLADA.isMimeConstant("")); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void isMimeConstant_3() {
			assertTrue(MimeName.MIME_COLLADA.isMimeConstant("application/x-collada")); //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void isMimeConstant_4() {
			assertFalse(MimeName.MIME_COLLADA.isMimeConstant("application/collada")); //$NON-NLS-1$
		}

		@DisplayName("#5")
		@Test
		public void isMimeConstant_5() {
			assertTrue(MimeName.MIME_PBM.isMimeConstant("image/x-portable-bitmap")); //$NON-NLS-1$
		}

		@DisplayName("#6")
		@Test
		public void isMimeConstant_6() {
			assertFalse(MimeName.MIME_PBM.isMimeConstant("image/portable-bitmap")); //$NON-NLS-1$
		}

		@DisplayName("#7")
		@Test
		public void isMimeConstant_7() {
			assertTrue(MimeName.MIME_SHAPE_FILE.isMimeConstant("application/x-shapefile")); //$NON-NLS-1$
		}

		@DisplayName("#8")
		@Test
		public void isMimeConstant_8() {
			assertFalse(MimeName.MIME_SHAPE_FILE.isMimeConstant("application/shapefile")); //$NON-NLS-1$
		}
	}

	@DisplayName("parseMimeType")
	@Nested
	public class ParseMimeType {

		@DisplayName("#1")
		@Test
		public void staticParseMimeType_1() {
			assertNull(MimeName.parseMimeType(null));
		}

		@DisplayName("#2")
		@Test
		public void staticParseMimeType_2() {
			assertNull(MimeName.parseMimeType("")); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void staticParseMimeType_3() {
			assertEquals(MimeName.MIME_COLLADA.getMimeConstant(), MimeName.parseMimeType("application/x-collada").toString()); //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void staticParseMimeType_4() {
			assertEquals(MimeName.MIME_PBM.getMimeConstant(), MimeName.parseMimeType("image/x-portable-bitmap").toString()); //$NON-NLS-1$
		}

		@DisplayName("#5")
		@Test
		public void staticParseMimeType_5() {
			assertEquals(MimeName.MIME_SHAPE_FILE.getMimeConstant(), MimeName.parseMimeType("application/x-shapefile").toString()); //$NON-NLS-1$
		}
	}

	@DisplayName("parseMimeName")
	@Nested
	public class ParseMimeName {

		@DisplayName("#1")
		@Test
		public void staticParseMimeName_1() {
			assertNull(MimeName.parseMimeName(null));
		}

		@DisplayName("#2")
		@Test
		public void staticParseMimeName_2() {
			assertNull(MimeName.parseMimeName("")); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void staticParseMimeName_3() {
			assertSame(MimeName.MIME_COLLADA, MimeName.parseMimeName("application/x-collada")); //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void staticParseMimeName_4() {
			assertSame(MimeName.MIME_PBM, MimeName.parseMimeName("image/x-portable-bitmap")); //$NON-NLS-1$
		}

		@DisplayName("#5")
		@Test
		public void staticParseMimeName_5() {
			assertSame(MimeName.MIME_SHAPE_FILE, MimeName.parseMimeName("application/x-shapefile")); //$NON-NLS-1$
		}
	}

}

