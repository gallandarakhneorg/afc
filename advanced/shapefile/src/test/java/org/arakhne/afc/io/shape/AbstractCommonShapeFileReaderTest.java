/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.io.shape.AbstractCommonShapeFileReader;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.vmutil.Resources;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class AbstractCommonShapeFileReaderTest extends AbstractIoShapeTest {

	private static final String TEST_FILE = "org/arakhne/afc/io/shape/test.shx"; //$NON-NLS-1$
	private static final int TEST_FILE_SIZE = 268;
	
	private URL resource;
	private AbstractCommonShapeFileReader<Object> reader;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.resource = Resources.getResource(TEST_FILE);
		assertNotNull(this.resource);
		this.reader = new AbstractCommonShapeFileReaderStub(this.resource);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.reader.close();
		this.reader = null;
		this.resource = null;
	}

	@Test
	public void testIsHeaderRead() throws Exception {
		assertFalse(this.reader.isHeaderRead());
		this.reader.readHeader();
		assertTrue(this.reader.isHeaderRead());
	}

	@Test
	public void testGetFileSize() throws Exception {
		assertEquals(TEST_FILE_SIZE, this.reader.getFileSize());
	}

	@Test
	public void testGetShapeElementType() throws Exception {
		assertEquals(ShapeElementType.POLYGON, this.reader.getShapeElementType());
	}

	@Test
	public void testGetFileReadingPosition() throws Exception {
		assertEquals(100, this.reader.getFileReadingPosition());
		this.reader.readHeader();
		assertEquals(100, this.reader.getFileReadingPosition());
		this.reader.read();
		assertEquals(108, this.reader.getFileReadingPosition());
		this.reader.read();
		assertEquals(116, this.reader.getFileReadingPosition());
	}

	@Test
	public void testGetBoundsFromHeader() throws Exception {
		ESRIBounds bounds = this.reader.getBoundsFromHeader();
		assertNotNull(bounds);
		assertEpsilonEquals(936456.700, bounds.getMinX());
		assertEpsilonEquals(2300653.700, bounds.getMinY());
		assertEpsilonEquals(0., bounds.getMinZ());
		assertEpsilonEquals(0., bounds.getMinM());
		assertEpsilonEquals(941093.900, bounds.getMaxX());
		assertEpsilonEquals(2308847.400, bounds.getMaxY());
		assertEpsilonEquals(0., bounds.getMaxZ());
		assertEpsilonEquals(0., bounds.getMaxM());
	}

	@Test
	public void testIsSeekEnabled() throws Exception {
		assertTrue(this.reader.isSeekEnabled());
		this.reader.disableSeek();
		assertFalse(this.reader.isSeekEnabled());
	}

	@Test
	public void testDisableSeek() throws Exception {
		assertTrue(this.reader.isSeekEnabled());
		this.reader.disableSeek();
		assertFalse(this.reader.isSeekEnabled());
	}

	@Test
	public void testRead() throws Exception {
		Object obj = this.reader.read();
		assertNotNull(obj);
		assertTrue(obj instanceof NumberStub);
		assertEpsilonEquals(0., ((NumberStub)obj).doubleValue());
		
		obj = this.reader.read();
		assertNotNull(obj);
		assertTrue(obj instanceof NumberStub);
		assertEpsilonEquals(1., ((NumberStub)obj).doubleValue());

		obj = this.reader.read();
		assertNotNull(obj);
		assertTrue(obj instanceof NumberStub);
		assertEpsilonEquals(2., ((NumberStub)obj).doubleValue());
	}

	@Test
	public void testIterator() throws Exception {
		Iterator<Object> iterator = this.reader.iterator();
		int recordCount = (TEST_FILE_SIZE - 100) / 8;
		Object obj;
		
		for(int i=0; i<recordCount; ++i) {
			assertTrue(iterator.hasNext());
			obj = iterator.next();
			assertNotNull(obj);
			assertTrue(obj instanceof NumberStub);
			assertEquals(i, ((Number)obj).intValue());
		}
		
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorClass() throws Exception {
		Iterator<SubNumberStub> iterator = this.reader.iterator(SubNumberStub.class);
		int recordCount = (TEST_FILE_SIZE - 100) / 8;
		SubNumberStub obj;
		
		for(int i=0, j=1; i<recordCount/2; ++i, j+=2) {
			assertTrue(iterator.hasNext(), "record #"+Integer.toString(i)); //$NON-NLS-1$
			obj = iterator.next();
			assertNotNull(obj, "record #"+Integer.toString(i)); //$NON-NLS-1$
			assertEquals(j, obj.intValue(), "record #"+Integer.toString(i)); //$NON-NLS-1$
		}
		
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorBoolean_true() throws Exception {
		Iterator<Object> iterator = this.reader.iterator(true);
		int recordCount = (TEST_FILE_SIZE - 100) / 8;
		Object obj;
		
		for(int i=0; i<recordCount; ++i) {
			assertTrue(iterator.hasNext());
			obj = iterator.next();
			assertNotNull(obj);
			assertTrue(obj instanceof NumberStub);
			assertEquals(i, ((Number)obj).intValue());
		}
		
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorBoolean_false() throws Exception {
		Iterator<Object> iterator = this.reader.iterator(false);
		int recordCount = (TEST_FILE_SIZE - 100) / 8;
		Object obj;
		
		for(int i=0; i<recordCount; ++i) {
			assertTrue(iterator.hasNext());
			obj = iterator.next();
			assertNotNull(obj);
			assertTrue(obj instanceof NumberStub);
			assertEquals(i, ((Number)obj).intValue());
		}
		
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorClassBoolean_true() throws Exception {
		Iterator<SubNumberStub> iterator = this.reader.iterator(SubNumberStub.class, true);
		int recordCount = (TEST_FILE_SIZE - 100) / 8;
		SubNumberStub obj;
		
		for(int i=0,j=1; i<recordCount/2; ++i,j+=2) {
			assertTrue(iterator.hasNext());
			obj = iterator.next();
			assertNotNull(obj);
			assertEquals(j, ((Number)obj).intValue());
		}
		
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratorClassBoolean_false() throws Exception {
		Iterator<SubNumberStub> iterator = this.reader.iterator(SubNumberStub.class, false);
		int recordCount = (TEST_FILE_SIZE - 100) / 8;
		SubNumberStub obj;
		
		for(int i=0,j=1; i<recordCount/2; ++i, j+=2) {
			assertTrue(iterator.hasNext());
			obj = iterator.next();
			assertNotNull(obj);
			assertEquals(j, ((Number)obj).intValue());
		}
		
		assertFalse(iterator.hasNext());
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class AbstractCommonShapeFileReaderStub
			extends AbstractCommonShapeFileReader<Object> {

		private int n;
		
		/**
		 * @param file
		 * @throws IOException
		 */
		public AbstractCommonShapeFileReaderStub(URL file) throws IOException {
			super(file);
			this.n = 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Object readRecord(int recrodNumber) throws EOFException, IOException {
			readLEDouble();
			int r = this.n;
			this.n++;
			if (r%2==0) {
				return new NumberStub(r);
			}
			return new SubNumberStub(r);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void seek(int recordIndex) throws IOException {
			//
		}
		
	}
	
	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class NumberStub extends Number {

		private static final long serialVersionUID = 5745047564433042208L;
		
		private final int i;
		
		/**
		 * @param i
		 */
		public NumberStub(int i) {
			this.i = i;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double doubleValue() {
			return this.i;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public float floatValue() {
			return this.i;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int intValue() {
			return this.i;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long longValue() {
			return this.i;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return getClass().getName()+"="+Integer.toString(this.i); //$NON-NLS-1$
		}
		
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class SubNumberStub extends NumberStub {

		private static final long serialVersionUID = 1038836401248766447L;

		/**
		 * @param i
		 */
		public SubNumberStub(int i) {
			super(i);
		}

	}

}
