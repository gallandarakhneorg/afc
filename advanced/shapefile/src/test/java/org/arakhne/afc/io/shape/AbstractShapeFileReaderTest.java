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

package org.arakhne.afc.io.shape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;

import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("AbstractShapeFileReader")
@SuppressWarnings("all")
public class AbstractShapeFileReaderTest extends AbstractIoShapeTestCase {

	private static final String SHP_TEST_FILE = "org/arakhne/afc/io/shape/test.shp"; //$NON-NLS-1$
	private static final String SHX_TEST_FILE = "org/arakhne/afc/io/shape/test.shx"; //$NON-NLS-1$

	private URL shpResource;
	private URL shxResource;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.shxResource = Resources.getResource(SHX_TEST_FILE);
		assertNotNull(this.shxResource);
		this.shpResource = Resources.getResource(SHP_TEST_FILE);
		assertNotNull(this.shpResource);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.shxResource = null;
		this.shpResource = null;
	}
	
	private AbstractShapeFileReader<Integer> createNoShx() throws IOException {
		return new AbstractShapeFileReaderStub(this.shpResource, null);
	}

	private AbstractShapeFileReader<Integer> createShx() throws IOException {
		ShapeFileIndexReader shxReader = new ShapeFileIndexReader(this.shxResource);
		return new AbstractShapeFileReaderStub(this.shpResource, shxReader);
	}

	@DisplayName("getShapeElementType")
	@Nested
	public class GetShapeElementType {

		@DisplayName("Without SHX")
		@Nested
		public class WithoutShx {

			private AbstractShapeFileReader<Integer> reader;

			@BeforeEach
			public void setUp() throws Exception {
				reader = createNoShx();
			}

			@DisplayName("shape element type")
			@Test
			public void testGetShapeElementType_noshx_shapeElementType() throws Exception {
				assertEquals(ShapeElementType.POLYGON, reader.getShapeElementType());
			}
		}

		@DisplayName("With SHX")
		@Nested
		public class WithShx {

			private AbstractShapeFileReader<Integer> reader;

			@BeforeEach
			public void setUp() throws Exception {
				reader = createShx();
			}

			@DisplayName("shape element type")
			@Test
			public void testGetShapeElementType_shx_shapeElementType() throws Exception {
				assertEquals(ShapeElementType.POLYGON, reader.getShapeElementType());
			}
		}
	}

	@DisplayName("isSeekEnabled")
	@Nested
	public class IsSeekEnabled {

		@DisplayName("Without SHX")
		@Nested
		public class WithoutShx {

			private AbstractShapeFileReader<Integer> reader;

			@BeforeEach
			public void setUp() throws Exception {
				reader = createNoShx();
			}

			@DisplayName("initial state")
			@Test
			public void testIsSeekEnabled_noshx_initialState() throws Exception {
				assertFalse(reader.isSeekEnabled());
			}

			@DisplayName("after disableSeek")
			@Test
			public void testIsSeekEnabled_noshx_afterDisableSeek() throws Exception {
				reader.disableSeek();
				assertFalse(reader.isSeekEnabled());
			}
		}
		
		@DisplayName("With SHX")
		@Nested
		public class WithShx {

			private AbstractShapeFileReader<Integer> reader;

			@BeforeEach
			public void setUp() throws Exception {
				reader = createShx();
			}

			@DisplayName("initial state")
			@Test
			public void testIsSeekEnabled_shx_initialState() throws Exception {
				assertTrue(reader.isSeekEnabled());
			}

			@DisplayName("after disableSeek")
			@Test
			public void testIsSeekEnabled_shx_afterDisableSeek() throws Exception {
				reader.disableSeek();
				assertFalse(reader.isSeekEnabled());
			}
		}
	}

	@DisplayName("seek")
	@Nested
	public class Seek {

		@DisplayName("Without SHX")
		@Nested
		public class WithoutShx {

			private AbstractShapeFileReader<Integer> reader;

			@BeforeEach
			public void setUp() throws Exception {
				reader = createNoShx();
			}

			@DisplayName("seek(0)")
			@Test
			public void testSeek_noshx_seek0() throws Exception {
				assertThrows(IOException.class, () -> reader.seek(0));
			}

			@DisplayName("seek(18)")
			@Test
			public void testSeek_noshx_seek18() throws Exception {
				assertThrows(IOException.class, () -> reader.seek(18));
			}

			@DisplayName("seek(9)")
			@Test
			public void testSeek_noshx_seek9() throws Exception {
				assertThrows(IOException.class, () -> reader.seek(9));
			}
		}
		
		@DisplayName("With SHX")
		@Nested
		public class WithShx {

			private AbstractShapeFileReader<Integer> reader;

			@BeforeEach
			public void setUp() throws Exception {
				reader = createShx();
			}

			@DisplayName("seek(0) then read")
			@Test
			public void testSeek_shx_seek0_thenRead() throws Exception {
				reader.seek(0);
				assertEquals(Integer.valueOf(5), reader.read());
			}

			@DisplayName("seek(1) then read")
			@Test
			public void testSeek_shx_seek1_thenRead() throws Exception {
				reader.seek(1);
				assertEquals(Integer.valueOf(8), reader.read());
			}

			@DisplayName("seek(2) then read")
			@Test
			public void testSeek_shx_seek2_thenRead() throws Exception {
				reader.seek(2);
				assertEquals(Integer.valueOf(5), reader.read());
			}

			@DisplayName("seek(1000)")
			@Test
			public void testSeek_shx_seek1000() throws Exception {
				assertThrows(EOFException.class, () -> reader.seek(1000));
			}
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class AbstractShapeFileReaderStub extends AbstractShapeFileReader<Integer> {

		/**
		 * @param file
		 * @param shxReader
		 * @throws IOException 
		 */
		public AbstractShapeFileReaderStub(URL file, ShapeFileIndexReader shxReader) throws IOException {
			super(file, null, shxReader);
		}

		@Override
		protected Integer createPolygon(AttributeCollection provider,
				int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
			return Integer.valueOf(points.length);
		}

		@Override
		protected AttributeCollection createAttributeCollection(int elementIndex) {
			return null;
		}

		@Override
		protected Integer createMultiPatch(AttributeCollection provider,
				int shapeIndex, int[] parts, ShapeMultiPatchType[] partTypes,
				ESRIPoint[] points) {
			return null;
		}

		@Override
		protected Integer createMultiPoint(AttributeCollection provider,
				int shapeIndex, ESRIPoint[] points, boolean hasZ) {
			return null;
		}

		@Override
		protected Integer createPoint(AttributeCollection provider,
				int shapeIndex, ESRIPoint point) {
			return null;
		}

		@Override
		protected Integer createPolyline(AttributeCollection provider,
				int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
			return null;
		}

		@Override
		protected void putAttributeIn(Integer element, String attributeName,
				AttributeValue value) {
			//
		}

	}
	
}
