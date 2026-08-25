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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileIndexReader;
import org.arakhne.afc.io.shape.ShapeFileIndexRecord;
import org.arakhne.afc.vmutil.Resources;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ShapeFileIndexReader")
@SuppressWarnings("all")
public class ShapeFileIndexReaderTest extends AbstractIoShapeTestCase {

	private static final String TEST_FILE = "org/arakhne/afc/io/shape/test.shx"; //$NON-NLS-1$
	private static final int TEST_FILE_SIZE = 268;
	private static final int TEST_FILE_RECORD_COUNT = 21;
	
	private URL resource;
	private ShapeFileIndexReader reader;
	
	@BeforeEach
	public void setUp() throws Exception {
		resource = Resources.getResource(TEST_FILE);
		assertNotNull(resource);
		reader = new ShapeFileIndexReader(resource);
	}

	@AfterEach
	public void tearDown() throws Exception {
		reader.close();
		reader = null;
		resource = null;
	}

	@DisplayName("getFileSize")
	@Nested
	public class GetFileSize {

		@Test
		public void testGetFileSize() throws Exception {
			assertEquals(TEST_FILE_SIZE, reader.getFileSize());
		}	
	}

	@DisplayName("getRecordCount")
	@Nested
	public class GetRecordCount {

		@Test
		public void testGetRecordCount() throws Exception {
			assertEquals(TEST_FILE_RECORD_COUNT, reader.getRecordCount());
		}	
	}

	@DisplayName("getShapeElementType")
	@Nested
	public class GetShapeElementType {

		@Test
		public void testGetShapeElementType() throws Exception {
			assertEquals(ShapeElementType.POLYGON, reader.getShapeElementType());
		}	
	}

	@DisplayName("read")
	@Nested
	public class Read {

		private ShapeFileIndexRecord obj;

		@BeforeEach
		public void setUp() {
			obj = null;
		}

		@DisplayName("record 0")
		@Test
		public void testRead_record0() throws Exception {
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(0, obj.getRecordIndex());
			assertEquals(100, obj.getOffsetInFile());
			assertEquals(128, obj.getRecordContentLength());
		}

		@DisplayName("record 1")
		@Test
		public void testRead_record1() throws Exception {
			reader.read();
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(1, obj.getRecordIndex());
			assertEquals(236, obj.getOffsetInFile());
			assertEquals(176, obj.getRecordContentLength());
		}

		@DisplayName("record 2")
		@Test
		public void testRead_record2() throws Exception {
			reader.read();
			reader.read();
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(2, obj.getRecordIndex());
			assertEquals(420, obj.getOffsetInFile());
			assertEquals(128, obj.getRecordContentLength());
		}

		@DisplayName("record 3")
		@Test
		public void testRead_record3() throws Exception {
			for (int i = 0; i < 3; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(3, obj.getRecordIndex());
			assertEquals(556, obj.getOffsetInFile());
			assertEquals(144, obj.getRecordContentLength());
		}

		@DisplayName("record 4")
		@Test
		public void testRead_record4() throws Exception {
			for (int i = 0; i < 4; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(4, obj.getRecordIndex());
			assertEquals(708, obj.getOffsetInFile());
			assertEquals(224, obj.getRecordContentLength());
		}

		@DisplayName("record 5")
		@Test
		public void testRead_record5() throws Exception {
			for (int i = 0; i < 5; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(5, obj.getRecordIndex());
			assertEquals(940, obj.getOffsetInFile());
			assertEquals(128, obj.getRecordContentLength());
		}

		@DisplayName("record 6")
		@Test
		public void testRead_record6() throws Exception {
			for (int i = 0; i < 6; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(6, obj.getRecordIndex());
			assertEquals(1076, obj.getOffsetInFile());
			assertEquals(128, obj.getRecordContentLength());
		}

		@DisplayName("record 7")
		@Test
		public void testRead_record7() throws Exception {
			for (int i = 0; i < 7; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(7, obj.getRecordIndex());
			assertEquals(1212, obj.getOffsetInFile());
			assertEquals(128, obj.getRecordContentLength());
		}

		@DisplayName("record 8")
		@Test
		public void testRead_record8() throws Exception {
			for (int i = 0; i < 8; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(8, obj.getRecordIndex());
			assertEquals(1348, obj.getOffsetInFile());
			assertEquals(128, obj.getRecordContentLength());
		}

		@DisplayName("record 9")
		@Test
		public void testRead_record9() throws Exception {
			for (int i = 0; i < 9; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(9, obj.getRecordIndex());
			assertEquals(1484, obj.getOffsetInFile());
			assertEquals(128, obj.getRecordContentLength());
		}

		@DisplayName("record 10")
		@Test
		public void testRead_record10() throws Exception {
			for (int i = 0; i < 10; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(10, obj.getRecordIndex());
			assertEquals(1620, obj.getOffsetInFile());
			assertEquals(272, obj.getRecordContentLength());
		}

		@DisplayName("record 11")
		@Test
		public void testRead_record11() throws Exception {
			for (int i = 0; i < 11; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(11, obj.getRecordIndex());
			assertEquals(1900, obj.getOffsetInFile());
			assertEquals(272, obj.getRecordContentLength());
		}

		@DisplayName("record 12")
		@Test
		public void testRead_record12() throws Exception {
			for (int i = 0; i < 12; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(12, obj.getRecordIndex());
			assertEquals(2180, obj.getOffsetInFile());
			assertEquals(192, obj.getRecordContentLength());
		}

		@DisplayName("record 13")
		@Test
		public void testRead_record13() throws Exception {
			for (int i = 0; i < 13; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(13, obj.getRecordIndex());
			assertEquals(2380, obj.getOffsetInFile());
			assertEquals(304, obj.getRecordContentLength());
		}

		@DisplayName("record 14")
		@Test
		public void testRead_record14() throws Exception {
			for (int i = 0; i < 14; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(14, obj.getRecordIndex());
			assertEquals(2692, obj.getOffsetInFile());
			assertEquals(192, obj.getRecordContentLength());
		}

		@DisplayName("record 15")
		@Test
		public void testRead_record15() throws Exception {
			for (int i = 0; i < 15; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(15, obj.getRecordIndex());
			assertEquals(2892, obj.getOffsetInFile());
			assertEquals(272, obj.getRecordContentLength());
		}

		@DisplayName("record 16")
		@Test
		public void testRead_record16() throws Exception {
			for (int i = 0; i < 16; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(16, obj.getRecordIndex());
			assertEquals(3172, obj.getOffsetInFile());
			assertEquals(320, obj.getRecordContentLength());
		}

		@DisplayName("record 17")
		@Test
		public void testRead_record17() throws Exception {
			for (int i = 0; i < 17; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(17, obj.getRecordIndex());
			assertEquals(3500, obj.getOffsetInFile());
			assertEquals(352, obj.getRecordContentLength());
		}

		@DisplayName("record 18")
		@Test
		public void testRead_record18() throws Exception {
			for (int i = 0; i < 18; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(18, obj.getRecordIndex());
			assertEquals(3860, obj.getOffsetInFile());
			assertEquals(256, obj.getRecordContentLength());
		}

		@DisplayName("record 19")
		@Test
		public void testRead_record19() throws Exception {
			for (int i = 0; i < 19; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(19, obj.getRecordIndex());
			assertEquals(4124, obj.getOffsetInFile());
			assertEquals(240, obj.getRecordContentLength());
		}

		@DisplayName("record 20")
		@Test
		public void testRead_record20() throws Exception {
			for (int i = 0; i < 20; ++i) {
				reader.read();
			}
			obj = reader.read();
			assertNotNull(obj);
			assertEquals(20, obj.getRecordIndex());
			assertEquals(4372, obj.getOffsetInFile());
			assertEquals(384, obj.getRecordContentLength());
		}

		@DisplayName("end of file returns null")
		@Test
		public void testRead_endOfFileReturnsNull() throws Exception {
			for (int i = 0; i < 21; ++i) {
				reader.read();
			}
			assertNull(reader.read());
		}
	}

	@DisplayName("seek")
	@Nested
	public class Seek {

		@DisplayName("Enable seek")
		@Nested
		public class EnabledSeek {

			private ShapeFileIndexRecord obj;

			@BeforeEach
			public void setUp() {
				obj = null;
			}

			@DisplayName("seek(0) read not null")
			@Test
			public void testSeek_seekenabled_seek0_read_notNull() throws Exception {
				reader.seek(0);
				obj = reader.read();
				assertNotNull(obj);
			}

			@DisplayName("seek(0) record index")
			@Test
			public void testSeek_seekenabled_seek0_recordIndex() throws Exception {
				reader.seek(0);
				obj = reader.read();
				assertEquals(0, obj.getRecordIndex());
			}

			@DisplayName("seek(0) offset in file")
			@Test
			public void testSeek_seekenabled_seek0_offsetInFile() throws Exception {
				reader.seek(0);
				obj = reader.read();
				assertEquals(100, obj.getOffsetInFile());
			}

			@DisplayName("seek(0) record content length")
			@Test
			public void testSeek_seekenabled_seek0_recordContentLength() throws Exception {
				reader.seek(0);
				obj = reader.read();
				assertEquals(128, obj.getRecordContentLength());
			}

			@DisplayName("seek(2) read not null")
			@Test
			public void testSeek_seekenabled_seek2_read_notNull() throws Exception {
				reader.seek(2);
				obj = reader.read();
				assertNotNull(obj);
			}

			@DisplayName("seek(2) record index")
			@Test
			public void testSeek_seekenabled_seek2_recordIndex() throws Exception {
				reader.seek(2);
				obj = reader.read();
				assertEquals(2, obj.getRecordIndex());
			}

			@DisplayName("seek(2) offset in file")
			@Test
			public void testSeek_seekenabled_seek2_offsetInFile() throws Exception {
				reader.seek(2);
				obj = reader.read();
				assertEquals(420, obj.getOffsetInFile());
			}

			@DisplayName("seek(2) record content length")
			@Test
			public void testSeek_seekenabled_seek2_recordContentLength() throws Exception {
				reader.seek(2);
				obj = reader.read();
				assertEquals(128, obj.getRecordContentLength());
			}

			@DisplayName("seek(1) read not null")
			@Test
			public void testSeek_seekenabled_seek1_read_notNull() throws Exception {
				reader.seek(1);
				obj = reader.read();
				assertNotNull(obj);
			}

			@DisplayName("seek(1) record index")
			@Test
			public void testSeek_seekenabled_seek1_recordIndex() throws Exception {
				reader.seek(1);
				obj = reader.read();
				assertEquals(1, obj.getRecordIndex());
			}

			@DisplayName("seek(1) offset in file")
			@Test
			public void testSeek_seekenabled_seek1_offsetInFile() throws Exception {
				reader.seek(1);
				obj = reader.read();
				assertEquals(236, obj.getOffsetInFile());
			}

			@DisplayName("seek(1) record content length")
			@Test
			public void testSeek_seekenabled_seek1_recordContentLength() throws Exception {
				reader.seek(1);
				obj = reader.read();
				assertEquals(176, obj.getRecordContentLength());
			}

			@DisplayName("seek(18) read not null")
			@Test
			public void testSeek_seekenabled_seek18_read_notNull() throws Exception {
				reader.seek(18);
				obj = reader.read();
				assertNotNull(obj);
			}

			@DisplayName("seek(18) record index")
			@Test
			public void testSeek_seekenabled_seek18_recordIndex() throws Exception {
				reader.seek(18);
				obj = reader.read();
				assertEquals(18, obj.getRecordIndex());
			}

			@DisplayName("seek(18) offset in file")
			@Test
			public void testSeek_seekenabled_seek18_offsetInFile() throws Exception {
				reader.seek(18);
				obj = reader.read();
				assertEquals(3860, obj.getOffsetInFile());
			}

			@DisplayName("seek(18) record content length")
			@Test
			public void testSeek_seekenabled_seek18_recordContentLength() throws Exception {
				reader.seek(18);
				obj = reader.read();
				assertEquals(256, obj.getRecordContentLength());
			}

			@DisplayName("seek(100000)")
			@Test
			public void testSeek_seekenabled_seek100000() throws Exception {
				assertThrows(EOFException.class, () -> reader.seek(100000));
			}
		}

		@DisplayName("Disable seek")
		@Nested
		public class DisableSeek {

			@BeforeEach
			public void setUp() throws Exception {
				reader.disableSeek();
			}

			@DisplayName("seek(0)")
			@Test
			public void testSeek_seekdisabled_seek0() throws Exception {
				assertThrows(IOException.class, () -> reader.seek(0));
			}

			@DisplayName("seek(2)")
			@Test
			public void testSeek_seekdisabled_seek2() throws Exception {
				assertThrows(IOException.class, () -> reader.seek(2));
			}

			@DisplayName("seek(1)")
			@Test
			public void testSeek_seekdisabled_seek1() throws Exception {
				assertThrows(IOException.class, () -> reader.seek(1));
			}

			@DisplayName("seek(100000)")
			@Test
			public void testSeek_seekdisabled_seek100000() throws Exception {
				assertThrows(IOException.class, () -> reader.seek(100000));
			}
		}
	}

}
