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

package org.arakhne.afc.io.dbase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;
import org.arakhne.afc.vmutil.locale.Locale;
 
/** Unit test for DBaseFileFileReader
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("DBaseFileReader")
@SuppressWarnings("all")
public class DBaseFileReaderTest extends AbstractTestCase {
	
	private static final String PHOTOGRAMMETRIE_VALUE;
	private static final String BDTOPO_VALUE;
	private static final String BATIMENT_RELIGIEUX_VALUE;
	
	static {
		PHOTOGRAMMETRIE_VALUE = Locale.getStringWithDefaultFrom(
				"org/arakhne/afc/io/dbase/test", //$NON-NLS-1$
				"PHOTOGRAMMETRIE", //$NON-NLS-1$
				null);
		assert(PHOTOGRAMMETRIE_VALUE!=null);
		BDTOPO_VALUE = Locale.getStringWithDefaultFrom(
				"org/arakhne/afc/io/dbase/test", //$NON-NLS-1$
				"BDTOPO", //$NON-NLS-1$
				null);
		assert(BDTOPO_VALUE!=null);
		BATIMENT_RELIGIEUX_VALUE = Locale.getStringWithDefaultFrom(
				"org/arakhne/afc/io/dbase/test", //$NON-NLS-1$
				"BATIMENT_RELIGIEUX", //$NON-NLS-1$
				null);
		assert(BATIMENT_RELIGIEUX_VALUE!=null);
	}

	private static final String TEST_FILENAME = "org/arakhne/afc/io/dbase/test.dbf"; //$NON-NLS-1$

	private DBaseFileReader reader;
	
	private static InputStream openTestStream() throws IOException {
		final URL url = Resources.getResource(TEST_FILENAME);
		if (url == null) throw new FileNotFoundException(TEST_FILENAME);
		final InputStream is = url.openStream();
		return is;
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		final InputStream is = openTestStream();
		reader = new DBaseFileReader(is);
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		reader.close();
		reader = null;
	}
	
	@DisplayName("removeOption")
	@Nested
	public class RemoveOption {

		@DisplayName("testAddRemoveOptionInteger_1")
		@Test
		public void testAddRemoveOptionInteger_1() {
			assertTrue(reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		}

		@DisplayName("testAddRemoveOptionInteger_2")
		@Test
		public void testAddRemoveOptionInteger_2() {
			reader.removeOption(DBaseFileReader.OPTION_DECODE_STRING);
			assertFalse(reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		}

		@DisplayName("testAddRemoveOptionInteger_3")
		@Test
		public void testAddRemoveOptionInteger_3() {
			reader.removeOption(DBaseFileReader.OPTION_DECODE_STRING);
			reader.addOption(DBaseFileReader.OPTION_DECODE_STRING);
			assertTrue(reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		}
	}

	@DisplayName("setOption")
	@Nested
	public class SetOption {

		@DisplayName("testSetOptionInteger_1")
		@Test
		public void testSetOptionInteger_1() {
			assertTrue(reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		}

		@DisplayName("testSetOptionInteger_2")
		@Test
		public void testSetOptionInteger_2() {
			reader.setOption(DBaseFileReader.OPTION_DECODE_STRING, false);
			assertFalse(reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		}

		@DisplayName("testSetOptionInteger_3")
		@Test
		public void testSetOptionInteger_3() {
			reader.setOption(DBaseFileReader.OPTION_DECODE_STRING, false);
			reader.setOption(DBaseFileReader.OPTION_DECODE_STRING, true);
			assertTrue(reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		}
	}

	@DisplayName("isClosed")
	@Nested
	public class IsClosed {

		@DisplayName("testIsClosed_1")
		@Test
		public void testIsClosed_1() throws Exception {
			assertFalse(reader.isClosed());
		}

		@DisplayName("testIsClosed_2")
		@Test
		public void testIsClosed_2() throws Exception {
			reader.close();
			assertTrue(reader.isClosed());
		}
	}

	@DisplayName("getDBFFieldCount")
	@Nested
	public class GetDBFFieldCount {

		@DisplayName("testGetDBFFieldCount_1")
		@Test
		public void testGetDBFFieldCount_1() {
			assertThrows(MustCallReadHeaderFunctionException.class, () -> reader.getDBFFieldCount());
		}

		@DisplayName("testGetDBFFieldCount_2")
		@Test
		public void testGetDBFFieldCount_2() throws Exception {
			reader.readDBFHeader();
			assertEquals(4, reader.getDBFFieldCount());
		}
	}

	@DisplayName("getDBFRecordCount")
	@Nested
	public class GetDBFRecordCount {

		@DisplayName("testGetDBFRecordCount_1")
		@Test
		public void testGetDBFRecordCount_1() {
			assertThrows(MustCallReadHeaderFunctionException.class, () -> reader.getDBFRecordCount());
		}

		@DisplayName("testGetDBFRecordCount_2")
		@Test
		public void testGetDBFRecordCount_2() throws Exception {
			reader.readDBFHeader();
			assertEquals(21, reader.getDBFRecordCount());
		}
	}

	@DisplayName("getDBFRecordSize")
	@Nested
	public class GetDBFRecordSize {

		@DisplayName("testGetDBFRecordSize_1")
		@Test
		public void testGetDBFRecordSize_1() {
			assertThrows(MustCallReadHeaderFunctionException.class, () -> reader.getDBFRecordSize());
		}

		@DisplayName("testGetDBFRecordSize_2")
		@Test
		public void testGetDBFRecordSize_2() throws Exception {
			reader.readDBFHeader();
			assertEquals(94, reader.getDBFRecordSize());
		}
	}
	
	@DisplayName("getDBFFieldName")
	@Nested
	public class GetDBFFieldName {

		@DisplayName("testGetDBFFieldNameInteger_1")
		@Test
		public void testGetDBFFieldNameInteger_1() {
			assertNull(reader.getDBFFieldName(0));
		}

		@DisplayName("testGetDBFFieldNameInteger_2")
		@Test
		public void testGetDBFFieldNameInteger_2() throws Exception {
			reader.readDBFHeader();
			assertNull(reader.getDBFFieldName(0));
		}

		@DisplayName("testGetDBFFieldNameInteger_3")
		@Test
		public void testGetDBFFieldNameInteger_3() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals("SOURCE", reader.getDBFFieldName(0)); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldNameInteger_4")
		@Test
		public void testGetDBFFieldNameInteger_4() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals("CATEGORIE", reader.getDBFFieldName(1)); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldNameInteger_5")
		@Test
		public void testGetDBFFieldNameInteger_5() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals("NATURE", reader.getDBFFieldName(2)); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldNameInteger_6")
		@Test
		public void testGetDBFFieldNameInteger_6() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals("HAUTEUR", reader.getDBFFieldName(3)); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldNameInteger_7")
		@Test
		public void testGetDBFFieldNameInteger_7() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertNull(reader.getDBFFieldName(4));
		}
	}

	@DisplayName("getDBFFieldIndex")
	@Nested
	public class GetDBFFieldIndex {

		@DisplayName("testGetDBFFieldIndexString_1")
		@Test
		public void testGetDBFFieldIndexString_1() {
			assertEquals(-1, reader.getDBFFieldIndex("SOURCE")); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldIndexString_2")
		@Test
		public void testGetDBFFieldIndexString_2() throws Exception {
			reader.readDBFHeader();
			assertEquals(-1, reader.getDBFFieldIndex("SOURCE")); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldIndexString_3")
		@Test
		public void testGetDBFFieldIndexString_3() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals(0, reader.getDBFFieldIndex("SOURCE")); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldIndexString_4")
		@Test
		public void testGetDBFFieldIndexString_4() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals(1, reader.getDBFFieldIndex("CATEGORIE")); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldIndexString_5")
		@Test
		public void testGetDBFFieldIndexString_5() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals(2, reader.getDBFFieldIndex("NATURE")); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldIndexString_6")
		@Test
		public void testGetDBFFieldIndexString_6() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals(3, reader.getDBFFieldIndex("HAUTEUR")); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFieldIndexString_7")
		@Test
		public void testGetDBFFieldIndexString_7() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals(-1, reader.getDBFFieldIndex("NOFIELD")); //$NON-NLS-1$
		}
	}

	@DisplayName("getDBFFieldType")
	@Nested
	public class GetDBFFieldType {

		@DisplayName("testGetDBFFieldTypeInteger_1")
		@Test
		public void testGetDBFFieldTypeInteger_1() {
			assertNull(reader.getDBFFieldType(0));
		}

		@DisplayName("testGetDBFFieldTypeInteger_2")
		@Test
		public void testGetDBFFieldTypeInteger_2() throws Exception {
			reader.readDBFHeader();
			assertNull(reader.getDBFFieldType(0));
		}

		@DisplayName("testGetDBFFieldTypeInteger_3")
		@Test
		public void testGetDBFFieldTypeInteger_3() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals(DBaseFieldType.STRING, reader.getDBFFieldType(0));
		}

		@DisplayName("testGetDBFFieldTypeInteger_4")
		@Test
		public void testGetDBFFieldTypeInteger_4() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals(DBaseFieldType.STRING, reader.getDBFFieldType(1));
		}

		@DisplayName("testGetDBFFieldTypeInteger_5")
		@Test
		public void testGetDBFFieldTypeInteger_5() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals(DBaseFieldType.STRING, reader.getDBFFieldType(2));
		}

		@DisplayName("testGetDBFFieldTypeInteger_6")
		@Test
		public void testGetDBFFieldTypeInteger_6() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertEquals(DBaseFieldType.NUMBER, reader.getDBFFieldType(3));
		}

		@DisplayName("testGetDBFFieldTypeInteger_7")
		@Test
		public void testGetDBFFieldTypeInteger_7() throws Exception {
			reader.readDBFHeader();
			assertNotNull(reader.readDBFFields());
			assertNull(reader.getDBFFieldType(4));
		}
	}

	@DisplayName("getDBFFields")
	@Nested
	public class GetDBFFields {

		@DisplayName("testGetDBFFields_1")
		@Test
		public void testGetDBFFields_1() {
			assertNull(reader.getDBFFields());
		}

		@DisplayName("testGetDBFFields_2")
		@Test
		public void testGetDBFFields_2() throws Exception {
			reader.readDBFHeader();
			List<DBaseFileField> fields = reader.getDBFFields();
			assertNotNull(fields);
		}

		@DisplayName("testGetDBFFields_3")
		@Test
		public void testGetDBFFields_3() throws Exception {
			reader.readDBFHeader();
			List<DBaseFileField> fields = reader.getDBFFields();
			assertEquals(4, fields.size());
		}

		@DisplayName("testGetDBFFields_4")
		@Test
		public void testGetDBFFields_4() throws Exception {
			reader.readDBFHeader();
			List<DBaseFileField> fields = reader.getDBFFields();
			assertEquals("SOURCE", fields.get(0).getName()); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFields_5")
		@Test
		public void testGetDBFFields_5() throws Exception {
			reader.readDBFHeader();
			List<DBaseFileField> fields = reader.getDBFFields();
			assertEquals("CATEGORIE", fields.get(1).getName()); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFields_6")
		@Test
		public void testGetDBFFields_6() throws Exception {
			reader.readDBFHeader();
			List<DBaseFileField> fields = reader.getDBFFields();
			assertEquals("NATURE", fields.get(2).getName()); //$NON-NLS-1$
		}

		@DisplayName("testGetDBFFields_7")
		@Test
		public void testGetDBFFields_7() throws Exception {
			reader.readDBFHeader();
			List<DBaseFileField> fields = reader.getDBFFields();
			assertEquals("HAUTEUR", fields.get(3).getName()); //$NON-NLS-1$
		}
	}

	@DisplayName("readNextDBFRecord")
	@Nested
	public class ReadNextDBFRecord {

		@DisplayName("testReadNextDBFRecord_1")
		@Test
		public void testReadNextDBFRecord_1() {
			assertThrows(MustCallReadHeaderFunctionException.class, () -> reader.readNextDBFRecord());
		}

		@DisplayName("testReadNextDBFRecord_2")
		@Test
		public void testReadNextDBFRecord_2() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertNotNull(record);
		}

		@DisplayName("testReadNextDBFRecord_3")
		@Test
		public void testReadNextDBFRecord_3() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(0, record.getRecordIndex());
		}

		@DisplayName("testReadNextDBFRecord_4")
		@Test
		public void testReadNextDBFRecord_4() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			int offset = 161;
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(offset, record.getRecordOffset());
		}

		@DisplayName("testReadNextDBFRecord_5")
		@Test
		public void testReadNextDBFRecord_5() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
		}

		@DisplayName("testReadNextDBFRecord_6")
		@Test
		public void testReadNextDBFRecord_6() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
		}

		@DisplayName("testReadNextDBFRecord_7")
		@Test
		public void testReadNextDBFRecord_7() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(BATIMENT_RELIGIEUX_VALUE, record.getFieldValue(2));
		}

		@DisplayName("testReadNextDBFRecord_8")
		@Test
		public void testReadNextDBFRecord_8() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(15., record.getFieldValue(3));
		}

		@DisplayName("testReadNextDBFRecord_9")
		@Test
		public void testReadNextDBFRecord_9() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = null;
			for (int i = 0; i <= 20; ++i) {
				record = reader.readNextDBFRecord();
			}
			assertNotNull(record);
		}

		@DisplayName("testReadNextDBFRecord_10")
		@Test
		public void testReadNextDBFRecord_10() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = null;
			for (int i = 0; i <= 20; ++i) {
				record = reader.readNextDBFRecord();
			}
			assertEquals(20, record.getRecordIndex());
		}

		@DisplayName("testReadNextDBFRecord_11")
		@Test
		public void testReadNextDBFRecord_11() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = null;
			for (int i = 0; i <= 20; ++i) {
				record = reader.readNextDBFRecord();
			}
			assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
		}

		@DisplayName("testReadNextDBFRecord_12")
		@Test
		public void testReadNextDBFRecord_12() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = null;
			for (int i = 0; i <= 20; ++i) {
				record = reader.readNextDBFRecord();
			}
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
		}

		@DisplayName("testReadNextDBFRecord_13")
		@Test
		public void testReadNextDBFRecord_13() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = null;
			for (int i = 0; i <= 20; ++i) {
				record = reader.readNextDBFRecord();
			}
			assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
		}

		@DisplayName("testReadNextDBFRecord_14")
		@Test
		public void testReadNextDBFRecord_14() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = null;
			for (int i = 0; i <= 20; ++i) {
				record = reader.readNextDBFRecord();
			}
			assertEquals(11., record.getFieldValue(3));
		}

		@DisplayName("testReadNextDBFRecord_15")
		@Test
		public void testReadNextDBFRecord_15() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			for (int i = 0; i <= 20; ++i) {
				reader.readNextDBFRecord();
			}
			assertNull(reader.readNextDBFRecord());
		}
	}

	@DisplayName("skip")
	@Nested
	public class Skip {

		@DisplayName("testSkipInteger_1")
		@Test
		public void testSkipInteger_1() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertNotNull(record);
		}

		@DisplayName("testSkipInteger_2")
		@Test
		public void testSkipInteger_2() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(0, record.getRecordIndex());
		}

		@DisplayName("testSkipInteger_3")
		@Test
		public void testSkipInteger_3() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			int offset = 161;
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(offset, record.getRecordOffset());
		}

		@DisplayName("testSkipInteger_4")
		@Test
		public void testSkipInteger_4() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
		}

		@DisplayName("testSkipInteger_5")
		@Test
		public void testSkipInteger_5() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
		}

		@DisplayName("testSkipInteger_6")
		@Test
		public void testSkipInteger_6() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(BATIMENT_RELIGIEUX_VALUE, record.getFieldValue(2));
		}

		@DisplayName("testSkipInteger_7")
		@Test
		public void testSkipInteger_7() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(15., record.getFieldValue(3));
		}

		@DisplayName("testSkipInteger_8")
		@Test
		public void testSkipInteger_8() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			int offset = 161 + 94; // after first record
			reader.readNextDBFRecord();

			offset += 5 * 94;
			reader.skip(5);

			DBaseFileRecord record = reader.readNextDBFRecord();
			assertNotNull(record);
			assertEquals(6, record.getRecordIndex());
			assertEquals(offset, record.getRecordOffset());
			assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
			assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
			assertEquals(6., record.getFieldValue(3));
		}

		@DisplayName("testSkipInteger_9")
		@Test
		public void testSkipInteger_9() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			int offset = 161 + 94; // after first record
			reader.readNextDBFRecord();

			offset += 5 * 94;
			reader.skip(5);
			reader.readNextDBFRecord();
			offset += 94;

			offset += 8 * 94;
			reader.skip(8);

			DBaseFileRecord record = reader.readNextDBFRecord();
			assertNotNull(record);
			assertEquals(15, record.getRecordIndex());
			assertEquals(offset, record.getRecordOffset());
			assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
			assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
			assertEquals(30., record.getFieldValue(3));
		}

		@DisplayName("testSkipInteger_10")
		@Test
		public void testSkipInteger_10() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			reader.readNextDBFRecord(); // index 0
			reader.skip(5); // to index 6
			reader.readNextDBFRecord(); // index 6
			reader.skip(8); // to index 15
			reader.readNextDBFRecord(); // index 15
			reader.skip(2); // to index 18

			DBaseFileRecord record = reader.readNextDBFRecord();
			assertNotNull(record);
			assertEquals(18, record.getRecordIndex());
			assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
			assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
			assertEquals(18., record.getFieldValue(3));
		}

		@DisplayName("testSkipInteger_11")
		@Test
		public void testSkipInteger_11() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			assertThrows(EOFException.class, () -> reader.skip(30));
		}
	}

	@DisplayName("seek")
	@Nested
	public class Seek {

		@DisplayName("testSeekInteger_1")
		@Test
		public void testSeekInteger_1() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			reader.seek(17);

			DBaseFileRecord record = reader.readNextDBFRecord();
			assertNotNull(record);
		}

		@DisplayName("testSeekInteger_2")
		@Test
		public void testSeekInteger_2() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			int boffset = 161;
			int length = 94;

			reader.seek(17);

			DBaseFileRecord record = reader.readNextDBFRecord();
			assertEquals(17, record.getRecordIndex());
			assertEquals(boffset + length * 17, record.getRecordOffset());
			assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
			assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
			assertEquals(35., record.getFieldValue(3));
		}

		@DisplayName("testSeekInteger_3")
		@Test
		public void testSeekInteger_3() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			int boffset = 161;

			reader.seek(0);

			DBaseFileRecord record = reader.readNextDBFRecord();
			assertNotNull(record);
			assertEquals(0, record.getRecordIndex());
			assertEquals(boffset, record.getRecordOffset());
			assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
			assertEquals(BATIMENT_RELIGIEUX_VALUE, record.getFieldValue(2));
			assertEquals(15., record.getFieldValue(3));
		}

		@DisplayName("testSeekInteger_4")
		@Test
		public void testSeekInteger_4() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			int boffset = 161;
			int length = 94;

			reader.seek(7);

			DBaseFileRecord record = reader.readNextDBFRecord();
			assertNotNull(record);
			assertEquals(7, record.getRecordIndex());
			assertEquals(boffset + length * 7, record.getRecordOffset());
			assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
			assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
			assertEquals(7., record.getFieldValue(3));
		}

		@DisplayName("testSeekInteger_5")
		@Test
		public void testSeekInteger_5() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			int boffset = 161;
			int length = 94;

			reader.seek(10);

			DBaseFileRecord record = reader.readNextDBFRecord();
			assertNotNull(record);
			assertEquals(10, record.getRecordIndex());
			assertEquals(boffset + length * 10, record.getRecordOffset());
			assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
			assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
			assertEquals(14., record.getFieldValue(3));
		}
	}

	@DisplayName("readNextAttributeProvider")
	@Nested
	public class ReadNextAttributeProvider {

		@DisplayName("testReadNextAttributeProvider_1")
		@Test
		public void testReadNextAttributeProvider_1() {
			assertThrows(MustCallReadHeaderFunctionException.class, () -> reader.readNextAttributeProvider());
		}

		@DisplayName("testReadNextAttributeProvider_2")
		@Test
		public void testReadNextAttributeProvider_2() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = reader.readNextAttributeProvider();
			assertNotNull(attrs);
		}

		@DisplayName("testReadNextAttributeProvider_3")
		@Test
		public void testReadNextAttributeProvider_3() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = reader.readNextAttributeProvider();
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testReadNextAttributeProvider_4")
		@Test
		public void testReadNextAttributeProvider_4() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = reader.readNextAttributeProvider();
			assertEquals("Religieux", attrs.getAttributeObject("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("testReadNextAttributeProvider_5")
		@Test
		public void testReadNextAttributeProvider_5() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = reader.readNextAttributeProvider();
			assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttributeObject("NATURE").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testReadNextAttributeProvider_6")
		@Test
		public void testReadNextAttributeProvider_6() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = reader.readNextAttributeProvider();
			assertEquals(15., attrs.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testReadNextAttributeProvider_7")
		@Test
		public void testReadNextAttributeProvider_7() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = null;
			for (int i = 0; i <= 20; ++i) {
				attrs = reader.readNextAttributeProvider();
			}
			assertNotNull(attrs);
		}

		@DisplayName("testReadNextAttributeProvider_8")
		@Test
		public void testReadNextAttributeProvider_8() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = null;
			for (int i = 0; i <= 20; ++i) {
				attrs = reader.readNextAttributeProvider();
			}
			assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testReadNextAttributeProvider_9")
		@Test
		public void testReadNextAttributeProvider_9() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = null;
			for (int i = 0; i <= 20; ++i) {
				attrs = reader.readNextAttributeProvider();
			}
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("testReadNextAttributeProvider_10")
		@Test
		public void testReadNextAttributeProvider_10() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = null;
			for (int i = 0; i <= 20; ++i) {
				attrs = reader.readNextAttributeProvider();
			}
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("testReadNextAttributeProvider_11")
		@Test
		public void testReadNextAttributeProvider_11() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			AttributeProvider attrs = null;
			for (int i = 0; i <= 20; ++i) {
				attrs = reader.readNextAttributeProvider();
			}
			assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testReadNextAttributeProvider_12")
		@Test
		public void testReadNextAttributeProvider_12() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();

			for (int i = 0; i <= 20; ++i) {
				reader.readNextAttributeProvider();
			}
			assertNull(reader.readNextAttributeProvider());
		}
	}

	@DisplayName("readRestOfDBFRecords")
	@Nested
	public class ReadRestOfDBFRecords {

		@DisplayName("testReadRestOfDBFRecords_1")
		@Test
		public void testReadRestOfDBFRecords_1() {
			assertThrows(MustCallReadHeaderFunctionException.class, () -> reader.readRestOfDBFRecords());
		}

		@DisplayName("testReadRestOfDBFRecords_2")
		@Test
		public void testReadRestOfDBFRecords_2() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			reader.skip(19);

			List<DBaseFileRecord> records = reader.readRestOfDBFRecords();
			assertNotNull(records);
		}

		@DisplayName("testReadRestOfDBFRecords_3")
		@Test
		public void testReadRestOfDBFRecords_3() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			reader.skip(19);

			List<DBaseFileRecord> records = reader.readRestOfDBFRecords();
			assertEquals(2, records.size());
		}

		@DisplayName("testReadRestOfDBFRecords_4")
		@Test
		public void testReadRestOfDBFRecords_4() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			reader.skip(19);

			List<DBaseFileRecord> records = reader.readRestOfDBFRecords();
			DBaseFileRecord record = records.get(0);

			assertNotNull(record);
			assertEquals(19, record.getRecordIndex());
			assertEquals(161 + 94 * 19, record.getRecordOffset());
			assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
			assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
			assertEquals(15., record.getFieldValue(3));
		}

		@DisplayName("testReadRestOfDBFRecords_5")
		@Test
		public void testReadRestOfDBFRecords_5() throws Exception {
			reader.readDBFHeader();
			reader.readDBFFields();
			reader.skip(19);

			List<DBaseFileRecord> records = reader.readRestOfDBFRecords();
			DBaseFileRecord record = records.get(1);

			assertNotNull(record);
			assertEquals(20, record.getRecordIndex());
			assertEquals(161 + 94 * 20, record.getRecordOffset());
			assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
			assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
			assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
			assertEquals(11., record.getFieldValue(3));
		}
	}

	@DisplayName("isColumnSelectable")
	@Nested
	public class IsColumnSelectable {

		@DisplayName("#1")
		@Test
	    public void testColumnSelection_1() throws Exception {
	    	assertFalse(reader.isColumnSelectable(null));
		}

		@DisplayName("#2")
		@Test
	    public void testColumnSelection_2() throws Exception {
			reader.readDBFHeader();
	    	reader.readDBFFields();
	    	List<DBaseFileField> fields = reader.getDBFFields();
	    	assertFalse(reader.isColumnSelectable(null));
		}

		@DisplayName("#3")
		@Test
	    public void testColumnSelection_3() throws Exception {
			reader.readDBFHeader();
	    	reader.readDBFFields();
	    	List<DBaseFileField> fields = reader.getDBFFields();
	    	assertTrue(reader.isColumnSelectable(fields.get(0)));
		}

		@DisplayName("#4")
		@Test
	    public void testColumnSelection_4() throws Exception {
			reader.readDBFHeader();
	    	reader.readDBFFields();
	    	List<DBaseFileField> fields = reader.getDBFFields();
	    	assertTrue(reader.isColumnSelectable(fields.get(1)));
		}

		@DisplayName("#5")
		@Test
	    public void testColumnSelection_5() throws Exception {
			reader.readDBFHeader();
	    	reader.readDBFFields();
	    	List<DBaseFileField> fields = reader.getDBFFields();
	    	assertTrue(reader.isColumnSelectable(fields.get(2)));
		}

		@DisplayName("#6")
		@Test
	    public void testColumnSelection_6() throws Exception {
			reader.readDBFHeader();
	    	reader.readDBFFields();
	    	List<DBaseFileField> fields = reader.getDBFFields();
	    	assertTrue(reader.isColumnSelectable(fields.get(3)));
		}
	}

	@DisplayName("selectColumn")
	@Nested
	public class SelectColumn {

		private List<DBaseFileField> fields;
		
		@BeforeEach
		public void setUp() throws Exception {
			reader.readDBFHeader();
	    	reader.readDBFFields();
	    	fields = reader.getDBFFields();
		}
		
		@DisplayName("#1")
		@Test
	    public void testColumnSelection() throws Exception {
	    	reader.selectColumn(fields.get(2));	    	
	    	assertFalse(reader.isColumnSelectable(null));
	    	assertFalse(reader.isColumnSelectable(fields.get(0)));
	    	assertFalse(reader.isColumnSelectable(fields.get(1)));
	    	assertTrue(reader.isColumnSelectable(fields.get(2)));
	    	assertFalse(reader.isColumnSelectable(fields.get(3)));
		}
		
		@DisplayName("#2")
		@Test
	    public void testColumnSelection_2() throws Exception {
	    	reader.selectColumn(fields.get(0));
	    	assertFalse(reader.isColumnSelectable(null));
	    	assertTrue(reader.isColumnSelectable(fields.get(0)));
	    	assertFalse(reader.isColumnSelectable(fields.get(1)));
	    	assertFalse(reader.isColumnSelectable(fields.get(2)));
	    	assertFalse(reader.isColumnSelectable(fields.get(3)));
		}
		
		@DisplayName("#3")
		@Test
	    public void testColumnSelection_3() throws Exception {
	    	reader.selectColumn(fields.get(2));	    	
	    	reader.selectColumn(fields.get(0));
	    	assertFalse(reader.isColumnSelectable(null));
	    	assertTrue(reader.isColumnSelectable(fields.get(0)));
	    	assertFalse(reader.isColumnSelectable(fields.get(1)));
	    	assertTrue(reader.isColumnSelectable(fields.get(2)));
	    	assertFalse(reader.isColumnSelectable(fields.get(3)));
		}
	}

	@DisplayName("selectAllColumns")
	@Nested
	public class SelectAllColumns {

		private List<DBaseFileField> fields;
		
		@BeforeEach
		public void setUp() throws Exception {
			reader.readDBFHeader();
	    	reader.readDBFFields();
	    	fields = reader.getDBFFields();
		}
		
		@DisplayName("#1")
		@Test
	    public void testColumnSelection() throws Exception {
	    	reader.selectAllColumns();
	    	assertFalse(reader.isColumnSelectable(null));
	    	assertTrue(reader.isColumnSelectable(fields.get(0)));
	    	assertTrue(reader.isColumnSelectable(fields.get(1)));
	    	assertTrue(reader.isColumnSelectable(fields.get(2)));
	    	assertTrue(reader.isColumnSelectable(fields.get(3)));
	    }
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		@DisplayName("testIterator_1")
		@Test
		public void testIterator_1() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();
			assertTrue(iterator.hasNext());
		}

		@DisplayName("testIterator_2")
		@Test
		public void testIterator_2() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			AttributeProvider attrs = iterator.next();
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttributeObject("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttributeObject("NATURE").getValue()); //$NON-NLS-1$
			assertEquals(15., attrs.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_3")
		@Test
		public void testIterator_3() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			assertTrue(iterator.hasNext());
			iterator.next(); // record 0

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 1
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$
			assertEquals(7., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_4")
		@Test
		public void testIterator_4() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 2; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 2
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$
			assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_5")
		@Test
		public void testIterator_5() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 3; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 3
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_6")
		@Test
		public void testIterator_6() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 4; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 4
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(23., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_7")
		@Test
		public void testIterator_7() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 5; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 5
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(6., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_8")
		@Test
		public void testIterator_8() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 6; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 6
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(6., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_9")
		@Test
		public void testIterator_9() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 7; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 7
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(7., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_10")
		@Test
		public void testIterator_10() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 8; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 8
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(5., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_11")
		@Test
		public void testIterator_11() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 9; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 9
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(2., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_12")
		@Test
		public void testIterator_12() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 10; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 10
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(14., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_13")
		@Test
		public void testIterator_13() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 11; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 11
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(27., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_14")
		@Test
		public void testIterator_14() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 12; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 12
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(8., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_15")
		@Test
		public void testIterator_15() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 13; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 13
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(14., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_16")
		@Test
		public void testIterator_16() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 14; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 14
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_17")
		@Test
		public void testIterator_17() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 15; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 15
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(30., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_18")
		@Test
		public void testIterator_18() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 16; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 16
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(23., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_19")
		@Test
		public void testIterator_19() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 17; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 17
			assertNotNull(attrs);
			assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(35., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_20")
		@Test
		public void testIterator_20() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 18; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 18
			assertNotNull(attrs);
			assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(18., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_21")
		@Test
		public void testIterator_21() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 19; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 19
			assertNotNull(attrs);
			assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(15., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_22")
		@Test
		public void testIterator_22() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();

			for (int i = 0; i < 20; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}

			assertTrue(iterator.hasNext());
			AttributeProvider attrs = iterator.next(); // record 20
			assertNotNull(attrs);
			assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
			assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$
		}

		@DisplayName("testIterator_23")
		@Test
		public void testIterator_23() throws Exception {
			Iterator<AttributeProvider> iterator = reader.iterator();
			for (int i = 0; i < 21; ++i) {
				assertTrue(iterator.hasNext());
				iterator.next();
			}
			assertFalse(iterator.hasNext());
		}
	}
}
