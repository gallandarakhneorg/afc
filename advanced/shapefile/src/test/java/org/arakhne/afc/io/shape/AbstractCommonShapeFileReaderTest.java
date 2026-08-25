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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@DisplayName("AbstractCommonShapeFileReader")
@SuppressWarnings("all")
public class AbstractCommonShapeFileReaderTest extends AbstractIoShapeTestCase {

	private static final String TEST_FILE = "org/arakhne/afc/io/shape/test.shx"; //$NON-NLS-1$
	private static final int TEST_FILE_SIZE = 268;
	
	private URL resource;
	private AbstractCommonShapeFileReader<Object> reader;
	
	@BeforeEach
	public void setUp() throws Exception {
		resource = Resources.getResource(TEST_FILE);
		assertNotNull(resource);
		reader = new AbstractCommonShapeFileReaderStub(resource);
	}

	@AfterEach
	public void tearDown() throws Exception {
		reader.close();
		reader = null;
		resource = null;
	}

	@DisplayName("isHeaderRead")
	@Nested
	public class IsHeaderRead {

		@DisplayName("initial state")
		@Test
		public void testIsHeaderRead_initialState() throws Exception {
			assertFalse(reader.isHeaderRead());
		}

		@DisplayName("after readHeader")
		@Test
		public void testIsHeaderRead_afterReadHeader() throws Exception {
			reader.readHeader();
			assertTrue(reader.isHeaderRead());
		}
	}

	@DisplayName("getFileSize")
	@Nested
	public class GetFileSize {

		@DisplayName("file size")
		@Test
		public void testGetFileSize_fileSize() throws Exception {
			assertEquals(TEST_FILE_SIZE, reader.getFileSize());
		}
	}

	@DisplayName("getShapeElementType")
	@Nested
	public class GetShapeElementType {

		@DisplayName("shape element type")
		@Test
		public void testGetShapeElementType_shapeElementType() throws Exception {
			assertEquals(ShapeElementType.POLYGON, reader.getShapeElementType());
		}
	}

	@DisplayName("getFileReadingPosition")
	@Nested
	public class GetFileReadingPosition {

		@DisplayName("initial position")
		@Test
		public void testGetFileReadingPosition_initialPosition() throws Exception {
			assertEquals(100, reader.getFileReadingPosition());
		}

		@DisplayName("position after readHeader")
		@Test
		public void testGetFileReadingPosition_afterReadHeader() throws Exception {
			reader.readHeader();
			assertEquals(100, reader.getFileReadingPosition());
		}

		@DisplayName("position after first read")
		@Test
		public void testGetFileReadingPosition_afterFirstRead() throws Exception {
			reader.readHeader();
			reader.read();
			assertEquals(108, reader.getFileReadingPosition());
		}

		@DisplayName("position after second read")
		@Test
		public void testGetFileReadingPosition_afterSecondRead() throws Exception {
			reader.readHeader();
			reader.read();
			reader.read();
			assertEquals(116, reader.getFileReadingPosition());
		}
	}

	@DisplayName("getBoundsFromHeader")
	@Nested
	public class GetBoundsFromHeader {

		private ESRIBounds bounds;

		@BeforeEach
		public void setUp() throws Exception {
			bounds = reader.getBoundsFromHeader();
		}

		@DisplayName("bounds not null")
		@Test
		public void testGetBoundsFromHeader_notNull() throws Exception {
			assertNotNull(bounds);
		}

		@DisplayName("minX")
		@Test
		public void testGetBoundsFromHeader_minX() throws Exception {
			assertEpsilonEquals(936456.700, bounds.getMinX());
		}

		@DisplayName("minY")
		@Test
		public void testGetBoundsFromHeader_minY() throws Exception {
			assertEpsilonEquals(2300653.700, bounds.getMinY());
		}

		@DisplayName("minZ")
		@Test
		public void testGetBoundsFromHeader_minZ() throws Exception {
			assertEpsilonEquals(0., bounds.getMinZ());
		}

		@DisplayName("minM")
		@Test
		public void testGetBoundsFromHeader_minM() throws Exception {
			assertEpsilonEquals(0., bounds.getMinM());
		}

		@DisplayName("maxX")
		@Test
		public void testGetBoundsFromHeader_maxX() throws Exception {
			assertEpsilonEquals(941093.900, bounds.getMaxX());
		}

		@DisplayName("maxY")
		@Test
		public void testGetBoundsFromHeader_maxY() throws Exception {
			assertEpsilonEquals(2308847.400, bounds.getMaxY());
		}

		@DisplayName("maxZ")
		@Test
		public void testGetBoundsFromHeader_maxZ() throws Exception {
			assertEpsilonEquals(0., bounds.getMaxZ());
		}

		@DisplayName("maxM")
		@Test
		public void testGetBoundsFromHeader_maxM() throws Exception {
			assertEpsilonEquals(0., bounds.getMaxM());
		}
	}
	
	@DisplayName("isSeekEnabled")
	@Nested
	public class IsSeekEnabled {

		@DisplayName("initial state")
		@Test
		public void testIsSeekEnabled_initialState() throws Exception {
			assertTrue(reader.isSeekEnabled());
		}

		@DisplayName("after disableSeek")
		@Test
		public void testIsSeekEnabled_afterDisableSeek() throws Exception {
			reader.disableSeek();
			assertFalse(reader.isSeekEnabled());
		}
	}

	@DisplayName("disableSeek")
	@Nested
	public class DisableSeek {

		@DisplayName("initial state")
		@Test
		public void testDisableSeek_initialState() throws Exception {
			assertTrue(reader.isSeekEnabled());
		}

		@DisplayName("after disableSeek")
		@Test
		public void testDisableSeek_afterDisableSeek() throws Exception {
			reader.disableSeek();
			assertFalse(reader.isSeekEnabled());
		}
	}

	@DisplayName("read")
	@Nested
	public class Read {

		private Object obj;

		@BeforeEach
		public void setUp() {
			obj = null;
		}

		@DisplayName("first read not null")
		@Test
		public void testRead_first_notNull() throws Exception {
			obj = reader.read();
			assertNotNull(obj);
		}

		@DisplayName("first read type")
		@Test
		public void testRead_first_type() throws Exception {
			obj = reader.read();
			assertTrue(obj instanceof NumberStub);
		}

		@DisplayName("first read value")
		@Test
		public void testRead_first_value() throws Exception {
			obj = reader.read();
			assertEpsilonEquals(0., ((NumberStub) obj).doubleValue());
		}

		@DisplayName("second read not null")
		@Test
		public void testRead_second_notNull() throws Exception {
			reader.read();
			obj = reader.read();
			assertNotNull(obj);
		}

		@DisplayName("second read type")
		@Test
		public void testRead_second_type() throws Exception {
			reader.read();
			obj = reader.read();
			assertTrue(obj instanceof NumberStub);
		}

		@DisplayName("second read value")
		@Test
		public void testRead_second_value() throws Exception {
			reader.read();
			obj = reader.read();
			assertEpsilonEquals(1., ((NumberStub) obj).doubleValue());
		}

		@DisplayName("third read not null")
		@Test
		public void testRead_third_notNull() throws Exception {
			reader.read();
			reader.read();
			obj = reader.read();
			assertNotNull(obj);
		}

		@DisplayName("third read type")
		@Test
		public void testRead_third_type() throws Exception {
			reader.read();
			reader.read();
			obj = reader.read();
			assertTrue(obj instanceof NumberStub);
		}

		@DisplayName("third read value")
		@Test
		public void testRead_third_value() throws Exception {
			reader.read();
			reader.read();
			obj = reader.read();
			assertEpsilonEquals(2., ((NumberStub) obj).doubleValue());
		}
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		@DisplayName("()")
		@Nested
		public class DefaultIterator {

			private Iterator<Object> iterator;
			private int recordCount;
			private Object obj;

			@BeforeEach
			public void setUp() throws Exception {
				iterator = reader.iterator();
				recordCount = (TEST_FILE_SIZE - 100) / 8;
				obj = null;
			}

			@DisplayName("iterates all records")
			@Test
			public void testIterator_iteratesAllRecords() throws Exception {
				for (int i = 0; i < recordCount; ++i) {
					assertTrue(iterator.hasNext());
					obj = iterator.next();
					assertNotNull(obj);
					assertTrue(obj instanceof NumberStub);
					assertEquals(i, ((Number) obj).intValue());
				}
			}

			@DisplayName("hasNext false at end")
			@Test
			public void testIterator_hasNextFalseAtEnd() throws Exception {
				for (int i = 0; i < recordCount; ++i) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("(Class)")
		@Nested
		public class WithClass {

			private Iterator<SubNumberStub> iterator;
			private int recordCount;
			private SubNumberStub obj;

			@BeforeEach
			public void setUp() throws Exception {
				iterator = reader.iterator(SubNumberStub.class);
				recordCount = (TEST_FILE_SIZE - 100) / 8;
				obj = null;
			}

			@DisplayName("iterates filtered records")
			@Test
			public void testIteratorClass_iteratesFilteredRecords() throws Exception {
				for (int i = 0, j = 1; i < recordCount / 2; ++i, j += 2) {
					assertTrue(iterator.hasNext(), "record #" + Integer.toString(i)); //$NON-NLS-1$
					obj = iterator.next();
					assertNotNull(obj, "record #" + Integer.toString(i)); //$NON-NLS-1$
					assertEquals(j, obj.intValue(), "record #" + Integer.toString(i)); //$NON-NLS-1$
				}
			}

			@DisplayName("hasNext false at end")
			@Test
			public void testIteratorClass_hasNextFalseAtEnd() throws Exception {
				for (int i = 0; i < recordCount / 2; ++i) {
					iterator.next();
				}
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("(boolean)")
		@Nested
		public class WithBoolean {

			@DisplayName("true")
			@Nested
			public class WithTrue {

				private Iterator<Object> iterator;
				private int recordCount;
				private Object obj;

				@BeforeEach
				public void setUp() throws Exception {
					iterator = reader.iterator(true);
					recordCount = (TEST_FILE_SIZE - 100) / 8;
					obj = null;
				}

				@DisplayName("iterates all records")
				@Test
				public void testIteratorBoolean_true_iteratesAllRecords() throws Exception {
					for (int i = 0; i < recordCount; ++i) {
						assertTrue(iterator.hasNext());
						obj = iterator.next();
						assertNotNull(obj);
						assertTrue(obj instanceof NumberStub);
						assertEquals(i, ((Number) obj).intValue());
					}
				}

				@DisplayName("hasNext false at end")
				@Test
				public void testIteratorBoolean_true_hasNextFalseAtEnd() throws Exception {
					for (int i = 0; i < recordCount; ++i) {
						iterator.next();
					}
					assertFalse(iterator.hasNext());
				}
			}

			@DisplayName("false")
			@Nested
			public class WithFalse {

				private Iterator<Object> iterator;
				private int recordCount;
				private Object obj;

				@BeforeEach
				public void setUp() throws Exception {
					iterator = reader.iterator(false);
					recordCount = (TEST_FILE_SIZE - 100) / 8;
					obj = null;
				}

				@DisplayName("iterates all records")
				@Test
				public void testIteratorBoolean_false_iteratesAllRecords() throws Exception {
					for (int i = 0; i < recordCount; ++i) {
						assertTrue(iterator.hasNext());
						obj = iterator.next();
						assertNotNull(obj);
						assertTrue(obj instanceof NumberStub);
						assertEquals(i, ((Number) obj).intValue());
					}
				}

				@DisplayName("hasNext false at end")
				@Test
				public void testIteratorBoolean_false_hasNextFalseAtEnd() throws Exception {
					for (int i = 0; i < recordCount; ++i) {
						iterator.next();
					}
					assertFalse(iterator.hasNext());
				}
			}
		}

		@DisplayName("(Class,boolean)")
		@Nested
		public class WithClassBoolean {

			@DisplayName("true")
			@Nested
			public class WithTrue {

				private Iterator<SubNumberStub> iterator;
				private int recordCount;
				private SubNumberStub obj;

				@BeforeEach
				public void setUp() throws Exception {
					iterator = reader.iterator(SubNumberStub.class, true);
					recordCount = (TEST_FILE_SIZE - 100) / 8;
					obj = null;
				}

				@DisplayName("iterates filtered records")
				@Test
				public void testIteratorClassBoolean_true_iteratesFilteredRecords() throws Exception {
					for (int i = 0, j = 1; i < recordCount / 2; ++i, j += 2) {
						assertTrue(iterator.hasNext());
						obj = iterator.next();
						assertNotNull(obj);
						assertEquals(j, ((Number) obj).intValue());
					}
				}

				@DisplayName("hasNext false at end")
				@Test
				public void testIteratorClassBoolean_true_hasNextFalseAtEnd() throws Exception {
					for (int i = 0; i < recordCount / 2; ++i) {
						iterator.next();
					}
					assertFalse(iterator.hasNext());
				}
			}

			@DisplayName("false")
			@Nested
			public class WithFalse {

				private Iterator<SubNumberStub> iterator;
				private int recordCount;
				private SubNumberStub obj;

				@BeforeEach
				public void setUp() throws Exception {
					iterator = reader.iterator(SubNumberStub.class, false);
					recordCount = (TEST_FILE_SIZE - 100) / 8;
					obj = null;
				}

				@DisplayName("iterates filtered records")
				@Test
				public void testIteratorClassBoolean_false_iteratesFilteredRecords() throws Exception {
					for (int i = 0, j = 1; i < recordCount / 2; ++i, j += 2) {
						assertTrue(iterator.hasNext());
						obj = iterator.next();
						assertNotNull(obj);
						assertEquals(j, ((Number) obj).intValue());
					}
				}

				@DisplayName("hasNext false at end")
				@Test
				public void testIteratorClassBoolean_false_hasNextFalseAtEnd() throws Exception {
					for (int i = 0; i < recordCount / 2; ++i) {
						iterator.next();
					}
					assertFalse(iterator.hasNext());
				}
			}
		}
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
		
		public AbstractCommonShapeFileReaderStub(URL file) throws IOException {
			super(file);
			n = 0;
		}

		@Override
		protected Object readRecord(int recrodNumber) throws EOFException, IOException {
			readLEDouble();
			int r = n;
			n++;
			if (r%2==0) {
				return new NumberStub(r);
			}
			return new SubNumberStub(r);
		}

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
		
		public NumberStub(int i) {
			this.i = i;
		}

		@Override
		public double doubleValue() {
			return i;
		}

		@Override
		public float floatValue() {
			return i;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int intValue() {
			return i;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public long longValue() {
			return i;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return getClass().getName()+"="+Integer.toString(i); //$NON-NLS-1$
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
