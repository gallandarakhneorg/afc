/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		InputStream is = Resources.getResourceAsStream(TEST_FILENAME);
		if (is==null) throw new FileNotFoundException(TEST_FILENAME);
		return is;
	}
	
	@Before
	public void setUp() throws Exception {
		this.reader = new DBaseFileReader(openTestStream());
	}
	
	@After
	public void tearDown() throws Exception {
		this.reader.close();
		this.reader = null;
	}
	
	

	@Test
	public void testAddRemoveOptionInteger() {
		assertTrue(this.reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		this.reader.removeOption(DBaseFileReader.OPTION_DECODE_STRING);
		assertFalse(this.reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		this.reader.addOption(DBaseFileReader.OPTION_DECODE_STRING);
		assertTrue(this.reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
	}

	@Test
	public void testSetOptionInteger() {
		assertTrue(this.reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		this.reader.setOption(DBaseFileReader.OPTION_DECODE_STRING, false);
		assertFalse(this.reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
		this.reader.setOption(DBaseFileReader.OPTION_DECODE_STRING, true);
		assertTrue(this.reader.hasOption(DBaseFileReader.OPTION_DECODE_STRING));
	}

	@Test
    public void testIsClosed() throws Exception {
    	assertFalse(this.reader.isClosed());
    	this.reader.close();
    	assertTrue(this.reader.isClosed());
    }

	@Test
    public void testGetDBFFieldCount() throws Exception {
    	try {
    		this.reader.getDBFFieldCount();
    		fail("MustCallReadHeaderFunctionException is expected"); //$NON-NLS-1$
    	}
    	catch(MustCallReadHeaderFunctionException exception) {
    		// Expected exception
    	}
    	this.reader.readDBFHeader();
    	assertEquals(4, this.reader.getDBFFieldCount());
    }

	@Test
    public void testGetDBFRecordCount() throws Exception {
    	try {
    		this.reader.getDBFRecordCount();
    		fail("MustCallReadHeaderFunctionException is expected"); //$NON-NLS-1$
    	}
    	catch(MustCallReadHeaderFunctionException exception) {
    		// Expected exception
    	}
    	this.reader.readDBFHeader();
    	assertEquals(21, this.reader.getDBFRecordCount());
    }

	@Test
    public void testGetDBFRecordSize() throws Exception {
    	try {
    		this.reader.getDBFRecordSize();
    		fail("MustCallReadHeaderFunctionException is expected"); //$NON-NLS-1$
    	}
    	catch(MustCallReadHeaderFunctionException exception) {
    		// Expected exception
    	}
    	this.reader.readDBFHeader();
    	assertEquals(94, this.reader.getDBFRecordSize());
    }

	@Test
    public void testGetDBFFieldNameInteger() throws Exception {
		assertNull(this.reader.getDBFFieldName(0));
    	this.reader.readDBFHeader();
		assertNull(this.reader.getDBFFieldName(0));
    	assertNotNull(this.reader.readDBFFields());
    	assertEquals("SOURCE", this.reader.getDBFFieldName(0)); //$NON-NLS-1$
    	assertEquals("CATEGORIE", this.reader.getDBFFieldName(1)); //$NON-NLS-1$
    	assertEquals("NATURE", this.reader.getDBFFieldName(2)); //$NON-NLS-1$
    	assertEquals("HAUTEUR", this.reader.getDBFFieldName(3)); //$NON-NLS-1$
		assertNull(this.reader.getDBFFieldName(4));
    }

	@Test
    public void testGetDBFFieldIndexString() throws Exception {
		assertEquals(-1, this.reader.getDBFFieldIndex("SOURCE")); //$NON-NLS-1$
    	this.reader.readDBFHeader();
		assertEquals(-1, this.reader.getDBFFieldIndex("SOURCE")); //$NON-NLS-1$
    	assertNotNull(this.reader.readDBFFields());
		assertEquals(0, this.reader.getDBFFieldIndex("SOURCE")); //$NON-NLS-1$
		assertEquals(1, this.reader.getDBFFieldIndex("CATEGORIE")); //$NON-NLS-1$
		assertEquals(2, this.reader.getDBFFieldIndex("NATURE")); //$NON-NLS-1$
		assertEquals(3, this.reader.getDBFFieldIndex("HAUTEUR")); //$NON-NLS-1$
		assertEquals(-1, this.reader.getDBFFieldIndex("NOFIELD")); //$NON-NLS-1$
    }

	@Test
    public void testGetDBFFieldTypeInteger() throws Exception {
		assertNull(this.reader.getDBFFieldType(0));
    	this.reader.readDBFHeader();
		assertNull(this.reader.getDBFFieldType(0));
    	assertNotNull(this.reader.readDBFFields());
    	assertEquals(DBaseFieldType.STRING, this.reader.getDBFFieldType(0));
    	assertEquals(DBaseFieldType.STRING, this.reader.getDBFFieldType(1));
    	assertEquals(DBaseFieldType.STRING, this.reader.getDBFFieldType(2));
    	assertEquals(DBaseFieldType.NUMBER, this.reader.getDBFFieldType(3));
		assertNull(this.reader.getDBFFieldType(4));
    }

	@Test
    public void testGetDBFFields() throws Exception {
    	assertNull(this.reader.getDBFFields());
    	this.reader.readDBFHeader();
    	List<DBaseFileField> fields = this.reader.getDBFFields();
    	assertNotNull(fields);
    	assertEquals(4, fields.size());
    	assertEquals("SOURCE", fields.get(0).getName()); //$NON-NLS-1$
    	assertEquals("CATEGORIE", fields.get(1).getName()); //$NON-NLS-1$
    	assertEquals("NATURE", fields.get(2).getName()); //$NON-NLS-1$
    	assertEquals("HAUTEUR", fields.get(3).getName()); //$NON-NLS-1$
    }

	@Test
    public void testReadNextDBFRecord() throws Exception {
    	try {
    		this.reader.readNextDBFRecord();
    		fail("MustCallReadHeaderFunctionException is expected"); //$NON-NLS-1$
    	}
    	catch(MustCallReadHeaderFunctionException exception) {
    		// Expected exception
    	}
    	
    	this.reader.readDBFHeader();
    	this.reader.readDBFFields();
    	
    	int offset = 161; // header length
    	
    	DBaseFileRecord record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(0, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, record.getFieldValue(2));
    	assertEquals(15., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(1, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, record.getFieldValue(2));
    	assertEquals(7., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(2, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, record.getFieldValue(2));
    	assertEquals(11., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(3, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(5., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(4, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(23., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(5, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(6., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(6, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(6., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(7, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(7., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(8, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(5., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(9, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(2., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(10, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(14., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(11, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(27., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(12, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(8., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(13, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(14., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(14, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(11., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(15, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(30., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(16, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(23., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(17, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(35., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(18, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(18., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(19, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(15., record.getFieldValue(3));

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(20, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(11., record.getFieldValue(3));

    	assertNull(this.reader.readNextDBFRecord());
    }

	@Test
    public void testSkipInteger() throws Exception {
    	this.reader.readDBFHeader();
    	this.reader.readDBFFields();
    	
    	int offset = 161; // header length
    	
    	DBaseFileRecord record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(0, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, record.getFieldValue(2));
    	assertEquals(15., record.getFieldValue(3));

    	offset += 5*94;
    	this.reader.skip(5);
    	
    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(6, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(6., record.getFieldValue(3));

    	offset += 8*94;
    	this.reader.skip(8);
    	
    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(15, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(30., record.getFieldValue(3));
    	
    	offset += 2*94;
    	this.reader.skip(2);

    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(18, record.getRecordIndex());
    	assertEquals(offset, record.getRecordOffset());
    	offset += 94; // record length
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(18., record.getFieldValue(3));

    	try {
    		this.reader.skip(8);
    		fail("EOFException was expected"); //$NON-NLS-1$
    	}
    	catch(EOFException exception) {
    		// Expected exception
    	}
    }
    
	@Test
    public void testSeekInteger() throws Exception {
    	this.reader.readDBFHeader();
    	this.reader.readDBFFields();
    	
    	DBaseFileRecord record;
    	int boffset = 161; // header length
    	int length = 94; // record length
    	
    	this.reader.seek(17);
    	
    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(17, record.getRecordIndex());
    	assertEquals(boffset+length*17, record.getRecordOffset());
    	assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(35., record.getFieldValue(3));

    	this.reader.seek(0);
    	
    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(0, record.getRecordIndex());
    	assertEquals(boffset, record.getRecordOffset());
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, record.getFieldValue(2));
    	assertEquals(15., record.getFieldValue(3));

    	this.reader.seek(7);
    	
    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(7, record.getRecordIndex());
    	assertEquals(boffset+length*7, record.getRecordOffset());
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(7., record.getFieldValue(3));

    	this.reader.seek(10);
    	
    	record = this.reader.readNextDBFRecord();
    	assertNotNull(record);
    	assertEquals(10, record.getRecordIndex());
    	assertEquals(boffset+length*10, record.getRecordOffset());
    	assertEquals(BDTOPO_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(14., record.getFieldValue(3));
    }
    
	@Test
    public void testReadNextAttributeProvider() throws Exception {
    	try {
    		this.reader.readNextAttributeProvider();
    		fail("MustCallReadHeaderFunctionException is expected"); //$NON-NLS-1$
    	}
    	catch(MustCallReadHeaderFunctionException exception) {
    		// Expected exception
    	}
    	
    	this.reader.readDBFHeader();
    	this.reader.readDBFFields();
    	
    	AttributeProvider attrs;
    	
    	attrs= this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttributeObject("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttributeObject("NATURE").getValue()); //$NON-NLS-1$
    	assertEquals(15., attrs.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$
    	assertEquals(7., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$
    	assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(5., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(23., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(6., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(6., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(7., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(5., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(2., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(14., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(27., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(8., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(14., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(30., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(23., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(35., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(18., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(15., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	attrs = this.reader.readNextAttributeProvider();
    	assertNotNull(attrs);
    	assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertNull(this.reader.readNextAttributeProvider());
    }

	@Test
    public void testReadRestOfDBFRecords() throws Exception {
    	try {
    		this.reader.readRestOfDBFRecords();
    		fail("MustCallReadHeaderFunctionException is expected"); //$NON-NLS-1$
    	}
    	catch(MustCallReadHeaderFunctionException exception) {
    		// Expected exception
    	}
    	
    	this.reader.readDBFHeader();
    	this.reader.readDBFFields();
    	this.reader.skip(19);
    	
    	DBaseFileRecord record;
    	List<DBaseFileRecord> records = this.reader.readRestOfDBFRecords();
    	assertNotNull(records);
    	assertEquals(2, records.size());
    	
    	record = records.get(0);
    	assertNotNull(record);
    	assertEquals(19, record.getRecordIndex());
    	assertEquals(161+94*19, record.getRecordOffset());
    	assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(15., record.getFieldValue(3));

    	record = records.get(1);
    	assertNotNull(record);
    	assertEquals(20, record.getRecordIndex());
    	assertEquals(161+94*20, record.getRecordOffset());
    	assertEquals(PHOTOGRAMMETRIE_VALUE, record.getFieldValue(0));
    	assertEquals("Religieux", record.getFieldValue(1)); //$NON-NLS-1$
    	assertEquals("Eglise", record.getFieldValue(2)); //$NON-NLS-1$
    	assertEquals(11., record.getFieldValue(3));
    }

	@Test
    public void testColumnSelection() throws Exception {
    	assertFalse(this.reader.isColumnSelectable(null));
    	
    	this.reader.readDBFHeader();
    	this.reader.readDBFFields();
    	
    	List<DBaseFileField> fields = this.reader.getDBFFields();
    	
    	assertFalse(this.reader.isColumnSelectable(null));
    	assertTrue(this.reader.isColumnSelectable(fields.get(0)));
    	assertTrue(this.reader.isColumnSelectable(fields.get(1)));
    	assertTrue(this.reader.isColumnSelectable(fields.get(2)));
    	assertTrue(this.reader.isColumnSelectable(fields.get(3)));

    	this.reader.selectColumn(fields.get(2));
    	
    	assertFalse(this.reader.isColumnSelectable(null));
    	assertFalse(this.reader.isColumnSelectable(fields.get(0)));
    	assertFalse(this.reader.isColumnSelectable(fields.get(1)));
    	assertTrue(this.reader.isColumnSelectable(fields.get(2)));
    	assertFalse(this.reader.isColumnSelectable(fields.get(3)));

    	this.reader.selectColumn(fields.get(0));
    	
    	assertFalse(this.reader.isColumnSelectable(null));
    	assertTrue(this.reader.isColumnSelectable(fields.get(0)));
    	assertFalse(this.reader.isColumnSelectable(fields.get(1)));
    	assertTrue(this.reader.isColumnSelectable(fields.get(2)));
    	assertFalse(this.reader.isColumnSelectable(fields.get(3)));

    	this.reader.selectAllColumns();
    	
    	assertFalse(this.reader.isColumnSelectable(null));
    	assertTrue(this.reader.isColumnSelectable(fields.get(0)));
    	assertTrue(this.reader.isColumnSelectable(fields.get(1)));
    	assertTrue(this.reader.isColumnSelectable(fields.get(2)));
    	assertTrue(this.reader.isColumnSelectable(fields.get(3)));
    }

	@Test
    public void testIterator() throws Exception {
    	Iterator<AttributeProvider> iterator;
    	
    	AttributeProvider attrs;
    	
    	iterator = this.reader.iterator();
    	
    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttributeObject("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttributeObject("NATURE").getValue()); //$NON-NLS-1$
    	assertEquals(15., attrs.getAttributeObject("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$
    	assertEquals(7., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(BATIMENT_RELIGIEUX_VALUE, attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$
    	assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(5., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(23., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(6., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(6., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(7., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(5., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(2., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(14., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(27., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(8., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(14., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(30., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(23., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(35., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(BDTOPO_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(18., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(15., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertTrue(iterator.hasNext());
    	attrs = iterator.next();
    	assertNotNull(attrs);
    	assertEquals(PHOTOGRAMMETRIE_VALUE, attrs.getAttribute("SOURCE").getValue()); //$NON-NLS-1$
    	assertEquals("Religieux", attrs.getAttribute("CATEGORIE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals("Eglise", attrs.getAttribute("NATURE").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
    	assertEquals(11., attrs.getAttribute("HAUTEUR").getValue()); //$NON-NLS-1$

    	assertFalse(iterator.hasNext());
    }

}
