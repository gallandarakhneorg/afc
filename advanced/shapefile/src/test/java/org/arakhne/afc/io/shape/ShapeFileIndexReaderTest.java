/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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
import static org.junit.jupiter.api.Assertions.fail;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
@SuppressWarnings("all")
public class ShapeFileIndexReaderTest extends AbstractIoShapeTest {

	private static final String TEST_FILE = "org/arakhne/afc/io/shape/test.shx"; //$NON-NLS-1$
	private static final int TEST_FILE_SIZE = 268;
	private static final int TEST_FILE_RECORD_COUNT = 21;
	
	private URL resource;
	private ShapeFileIndexReader reader;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.resource = Resources.getResource(TEST_FILE);
		assertNotNull(this.resource);
		this.reader = new ShapeFileIndexReader(this.resource);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.reader.close();
		this.reader = null;
		this.resource = null;
	}

	@Test
	public void testGetFileSize() throws Exception {
		assertEquals(TEST_FILE_SIZE, this.reader.getFileSize());
	}	
	
	@Test
	public void testGetRecordCount() throws Exception {
		assertEquals(TEST_FILE_RECORD_COUNT, this.reader.getRecordCount());
	}	

	@Test
	public void testGetShapeElementType() throws Exception {
		assertEquals(ShapeElementType.POLYGON, this.reader.getShapeElementType());
	}	

	@Test
	public void testRead() throws Exception {
		ShapeFileIndexRecord obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(0, obj.getRecordIndex());
		assertEquals(100, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());
		
		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(1, obj.getRecordIndex());
		assertEquals(236, obj.getOffsetInFile());
		assertEquals(176, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(2, obj.getRecordIndex());
		assertEquals(420, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(3, obj.getRecordIndex());
		assertEquals(556, obj.getOffsetInFile());
		assertEquals(144, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(4, obj.getRecordIndex());
		assertEquals(708, obj.getOffsetInFile());
		assertEquals(224, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(5, obj.getRecordIndex());
		assertEquals(940, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(6, obj.getRecordIndex());
		assertEquals(1076, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(7, obj.getRecordIndex());
		assertEquals(1212, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(8, obj.getRecordIndex());
		assertEquals(1348, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(9, obj.getRecordIndex());
		assertEquals(1484, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(10, obj.getRecordIndex());
		assertEquals(1620, obj.getOffsetInFile());
		assertEquals(272, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(11, obj.getRecordIndex());
		assertEquals(1900, obj.getOffsetInFile());
		assertEquals(272, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(12, obj.getRecordIndex());
		assertEquals(2180, obj.getOffsetInFile());
		assertEquals(192, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(13, obj.getRecordIndex());
		assertEquals(2380, obj.getOffsetInFile());
		assertEquals(304, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(14, obj.getRecordIndex());
		assertEquals(2692, obj.getOffsetInFile());
		assertEquals(192, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(15, obj.getRecordIndex());
		assertEquals(2892, obj.getOffsetInFile());
		assertEquals(272, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(16, obj.getRecordIndex());
		assertEquals(3172, obj.getOffsetInFile());
		assertEquals(320, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(17, obj.getRecordIndex());
		assertEquals(3500, obj.getOffsetInFile());
		assertEquals(352, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(18, obj.getRecordIndex());
		assertEquals(3860, obj.getOffsetInFile());
		assertEquals(256, obj.getRecordContentLength());

		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(19, obj.getRecordIndex());
		assertEquals(4124, obj.getOffsetInFile());
		assertEquals(240, obj.getRecordContentLength());

		obj = this.reader.read();
		assertEquals(20, obj.getRecordIndex());
		assertNotNull(obj);
		assertEquals(4372, obj.getOffsetInFile());
		assertEquals(384, obj.getRecordContentLength());

		assertNull(this.reader.read());
	}

	@Test
	public void testSeek_seekenabled() throws Exception {
		ShapeFileIndexRecord obj;
		
		this.reader.seek(0);
		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(0, obj.getRecordIndex());
		assertEquals(100, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		this.reader.seek(2);
		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(2, obj.getRecordIndex());
		assertEquals(420, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		this.reader.seek(1);
		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(1, obj.getRecordIndex());
		assertEquals(236, obj.getOffsetInFile());
		assertEquals(176, obj.getRecordContentLength());

		this.reader.seek(2);
		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(2, obj.getRecordIndex());
		assertEquals(420, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		this.reader.seek(0);
		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(0, obj.getRecordIndex());
		assertEquals(100, obj.getOffsetInFile());
		assertEquals(128, obj.getRecordContentLength());

		this.reader.seek(18);
		obj = this.reader.read();
		assertNotNull(obj);
		assertEquals(18, obj.getRecordIndex());
		assertEquals(3860, obj.getOffsetInFile());
		assertEquals(256, obj.getRecordContentLength());

		try {
			this.reader.seek(100000);
			fail("EOFException was expected"); //$NON-NLS-1$
		}
		catch (EOFException exception) {
			// Expected exception
		}
	}

	@Test
	public void testSeek_seekdisabled() throws Exception {
		this.reader.disableSeek();
		
		try {
			this.reader.seek(0);
			fail("IOException was expected"); //$NON-NLS-1$
		}
		catch (IOException exception) {
			// Expected exception
		}

		try {
			this.reader.seek(2);
			fail("IOException was expected"); //$NON-NLS-1$
		}
		catch (IOException exception) {
			// Expected exception
		}

		try {
			this.reader.seek(1);
			fail("IOException was expected"); //$NON-NLS-1$
		}
		catch (IOException exception) {
			// Expected exception
		}

		try {
			this.reader.seek(100000);
			fail("IOException was expected"); //$NON-NLS-1$
		}
		catch (IOException exception) {
			// Expected exception
		}
	}

}
