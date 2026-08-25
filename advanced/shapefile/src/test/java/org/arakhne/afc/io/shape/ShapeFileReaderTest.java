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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.io.shape.ESRIPoint;
import org.arakhne.afc.io.shape.ElementFactory;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileIndexReader;
import org.arakhne.afc.io.shape.ShapeFileReader;
import org.arakhne.afc.vmutil.Resources;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ShapeFileReader")
@SuppressWarnings("all")
public class ShapeFileReaderTest extends AbstractIoShapeTestCase {

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
	
	private ShapeFileReader<Integer> createNoShx() throws IOException {
		return new ShapeFileReader<>(this.shpResource, new NullFactory());
	}

	private ShapeFileReader<Integer> createShx() throws IOException {
		ShapeFileIndexReader shxReader = new ShapeFileIndexReader(this.shxResource);
		return new ShapeFileReader<>(this.shpResource, null, shxReader, new NullFactory());
	}

	@DisplayName("getShapeElementType")
	@Nested
	public class getShapeElementType {

		@DisplayName("Without SHX")
		@Test
		public void testGetShapeElementType_noshx() throws Exception {
			ShapeFileReader<Integer> reader = createNoShx();
			assertEquals(ShapeElementType.POLYGON, reader.getShapeElementType());
		}

		@DisplayName("With SHX")
		@Test
		public void testGetShapeElementType_shx() throws Exception {
			ShapeFileReader<Integer> reader = createShx();
			assertEquals(ShapeElementType.POLYGON, reader.getShapeElementType());
		}
	}

	@DisplayName("isSeekEnabled")
	@Nested
	public class IsSeekEnabled {

		private ShapeFileReader<Integer> readerWithoutShx;
		private ShapeFileReader<Integer> readerWithShx;

		@BeforeEach
		public void setUp() throws Exception {
			readerWithoutShx = createNoShx();
			readerWithShx = createShx();
		}

		@DisplayName("Without SHX - initial state")
		@Test
		public void testIsSeekEnabled_noshx_initialState() throws Exception {
			assertFalse(readerWithoutShx.isSeekEnabled());
		}

		@DisplayName("Without SHX - after disableSeek")
		@Test
		public void testIsSeekEnabled_noshx_afterDisableSeek() throws Exception {
			readerWithoutShx.disableSeek();
			assertFalse(readerWithoutShx.isSeekEnabled());
		}

		@DisplayName("With SHX - initial state")
		@Test
		public void testIsSeekEnabled_shx_initialState() throws Exception {
			assertTrue(readerWithShx.isSeekEnabled());
		}

		@DisplayName("With SHX - after disableSeek")
		@Test
		public void testIsSeekEnabled_shx_afterDisableSeek() throws Exception {
			readerWithShx.disableSeek();
			assertFalse(readerWithShx.isSeekEnabled());
		}
	}

	@DisplayName("seek")
	@Nested
	public class Seek {

		private ShapeFileReader<Integer> readerWithoutShx;
		private ShapeFileReader<Integer> readerWithShx;

		@BeforeEach
		public void setUp() throws Exception {
			readerWithoutShx = createNoShx();
			readerWithShx = createShx();
		}

		@DisplayName("Without SHX - seek(0)")
		@Test
		public void testSeek_noshx_seek0() throws Exception {
			assertThrows(IOException.class, () -> readerWithoutShx.seek(0));
		}

		@DisplayName("Without SHX - seek(18)")
		@Test
		public void testSeek_noshx_seek18() throws Exception {
			assertThrows(IOException.class, () -> readerWithoutShx.seek(18));
		}

		@DisplayName("Without SHX - seek(9)")
		@Test
		public void testSeek_noshx_seek9() throws Exception {
			assertThrows(IOException.class, () -> readerWithoutShx.seek(9));
		}

		@DisplayName("With SHX - seek(0) then read")
		@Test
		public void testSeek_shx_seek0_read() throws Exception {
			readerWithShx.seek(0);
			assertEquals(Integer.valueOf(5), readerWithShx.read());
		}

		@DisplayName("With SHX - seek(1) then read")
		@Test
		public void testSeek_shx_seek1_read() throws Exception {
			readerWithShx.seek(1);
			assertEquals(Integer.valueOf(8), readerWithShx.read());
		}

		@DisplayName("With SHX - seek(2) then read")
		@Test
		public void testSeek_shx_seek2_read() throws Exception {
			readerWithShx.seek(2);
			assertEquals(Integer.valueOf(5), readerWithShx.read());
		}

		@DisplayName("With SHX - seek(1000)")
		@Test
		public void testSeek_shx_seek1000() throws Exception {
			assertThrows(EOFException.class, () -> readerWithShx.seek(1000));
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class NullFactory implements ElementFactory<Integer> {

		/**
		 */
		public NullFactory() {
			//
		}
		
		@Override
		public Integer createPolygon(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
			return Integer.valueOf(points.length);
		}		
		
	}
	
}
