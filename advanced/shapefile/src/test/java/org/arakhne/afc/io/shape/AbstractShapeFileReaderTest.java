/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.vmutil.Resources;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class AbstractShapeFileReaderTest extends AbstractIoShapeTest {

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

	@Test
	public void testGetShapeElementType_noshx() throws Exception {
		AbstractShapeFileReader<Integer> reader = createNoShx();
		assertEquals(ShapeElementType.POLYGON, reader.getShapeElementType());
	}

	@Test
	public void testGetShapeElementType_shx() throws Exception {
		AbstractShapeFileReader<Integer> reader = createShx();
		assertEquals(ShapeElementType.POLYGON, reader.getShapeElementType());
	}

	@Test
	public void testIsSeekEnabled_noshx() throws Exception {
		AbstractShapeFileReader<Integer> reader = createNoShx();
		assertFalse(reader.isSeekEnabled());
		reader.disableSeek();
		assertFalse(reader.isSeekEnabled());
	}

	@Test
	public void testIsSeekEnabled_shx() throws Exception {
		AbstractShapeFileReader<Integer> reader = createShx();
		assertTrue(reader.isSeekEnabled());
		reader.disableSeek();
		assertFalse(reader.isSeekEnabled());
	}

	@Test
	public void testSeek_noshx() throws Exception {
		AbstractShapeFileReader<Integer> reader = createNoShx();
		
		try {
			reader.seek(0);
			fail("IOException was expected"); //$NON-NLS-1$
		}
		catch(IOException exception) {
			// Expected exception
		}

		try {
			reader.seek(18);
			fail("IOException was expected"); //$NON-NLS-1$
		}
		catch(IOException exception) {
			// Expected exception
		}

		try {
			reader.seek(9);
			fail("IOException was expected"); //$NON-NLS-1$
		}
		catch(IOException exception) {
			// Expected exception
		}
	}

	@Test
	public void testSeek_shx() throws Exception {
		AbstractShapeFileReader<Integer> reader = createShx();
		
		reader.seek(0);
		assertEquals(Integer.valueOf(5), reader.read());

		reader.seek(1);
		assertEquals(Integer.valueOf(8), reader.read());

		reader.seek(2);
		assertEquals(Integer.valueOf(5), reader.read());

		try {
			reader.seek(1000);
			fail("EOFException was expected"); //$NON-NLS-1$
		}
		catch(EOFException exception) {
			//Expected exception
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
