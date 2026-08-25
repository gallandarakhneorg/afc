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
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ESRIFileUtil;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileIndexReader;
import org.arakhne.afc.io.shape.ShapeFileIndexRecord;
import org.arakhne.afc.io.shape.ShapeFileIndexWriter;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ShapeFileIndexWriter")
@SuppressWarnings("all")
public class ShapeFileIndexWriterTest extends AbstractIoShapeTestCase {

	private ShapeFileIndexRecord[] records;
	private ESRIBounds bounds;
	private File outputFile;
	private ShapeFileIndexWriter writer;
	private int fileSize;
	
	@BeforeEach
	public void setUp() throws Exception {
		records = new ShapeFileIndexRecord[10];
		int offset = ESRIFileUtil.HEADER_BYTES;
		for(int i=0; i<records.length; ++i) {
			int length = getRandom().nextInt(255)+9;
			// ensure even length
			length = (length/2) * 2;
			records[i] = new ShapeFileIndexRecord(offset, length, false, i);
			offset += length;
		}
		fileSize = ESRIFileUtil.HEADER_BYTES + records.length * 8;
		outputFile = File.createTempFile("unittest", ".shx");  //$NON-NLS-1$//$NON-NLS-2$
		outputFile.deleteOnExit();
		bounds = new ESRIBounds(
				933441.7999726594, 942959.9281735239,
				-2309936.5466717673, -2292935.499999934,
				123., 3456.,
				-2445., 45567.);
		writer = new ShapeFileIndexWriter(outputFile, ShapeElementType.POLYGON, bounds);
	}

	@AfterEach
	public void tearDown() throws Exception {
		writer.close();
		outputFile.delete();
		records = null;
		outputFile = null;
		bounds = null;
		writer = null;
	}

	private void assertContent() throws Exception {
		ShapeFileIndexReader reader = new ShapeFileIndexReader(outputFile);
		
		ESRIBounds b = reader.getBoundsFromHeader();
		assertNotNull(b);
		assertEpsilonEquals(bounds.getMinX(), b.getMinX());
		assertEpsilonEquals(bounds.getMaxX(), b.getMaxX());
		assertEpsilonEquals(bounds.getMinY(), b.getMinY());
		assertEpsilonEquals(bounds.getMaxY(), b.getMaxY());
		assertEpsilonEquals(bounds.getMinZ(), b.getMinZ());
		assertEpsilonEquals(bounds.getMaxZ(), b.getMaxZ());
		assertEpsilonEquals(bounds.getMinM(), b.getMinM());
		assertEpsilonEquals(bounds.getMaxM(), b.getMaxM());
		
		assertEquals(ShapeElementType.POLYGON, reader.getShapeElementType());
		
		assertEquals(fileSize, reader.getFileSize());
		
		ShapeFileIndexRecord record;
		for(int i=0; i<records.length; ++i) {
			record = reader.read();
			assertNotNull(record);
			assertEquals(records[i].getOffsetInFile(), record.getOffsetInFile());
			assertEquals(records[i].getRecordContentLength(), record.getRecordContentLength());
		}
		assertNull(reader.read());
		reader.close();
	}
	
	@DisplayName("getFileBounds")
	@Nested
	public class GetFileBounds {
		@Test
		public void testGetFileBounds() {
			assertSame(bounds, writer.getFileBounds());
		}
	}

	@DisplayName("write")
	@Nested
	public class Write {

		@DisplayName("(Collection)")
		@Nested
		public class WithCollection {

			@Test
			public void testWriteCollection() throws Exception {
				writer.write(Arrays.asList(records));
				writer.close();
				assertContent();
			}
		}

		@DisplayName("(FileRecord)")
		@Nested
		public class WithFileRecord {

			@Test
			public void testWriteShapeFileRecord() throws Exception {
				for(int i=0; i<records.length; ++i) {
					writer.write(records[i]);
				}
				writer.close();
				assertContent();
			}
		}

		@DisplayName("(int)")
		@Nested
		public class WithInt {

			@Test
			public void testWriteInt() throws Exception {
				for(int i=0; i<records.length; ++i) {
					writer.write(records[i].getEntireRecordLength());
				}
				writer.close();
				assertContent();
			}
		}
	}

}
