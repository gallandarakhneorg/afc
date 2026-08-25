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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.io.shape.AbstractShapeFileWriter;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ESRIPoint;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeMultiPatchType;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("AbstractShapeFileWriter")
@SuppressWarnings("all")
public class AbstractShapeFileWriterTest extends AbstractIoShapeTestCase {

	private File outputFile;
	private AbstractShapeFileWriterStub writer;
	
	@BeforeEach
	public void setUp() throws Exception {
		outputFile = File.createTempFile("unittest", ".shx"); //$NON-NLS-1$ //$NON-NLS-2$
		outputFile.deleteOnExit();
		writer = new AbstractShapeFileWriterStub(outputFile, ShapeElementType.POLYLINE);
	}

	@AfterEach
	public void tearDown() throws Exception {
		outputFile.delete();
		writer.close();
		outputFile = null;
		writer = null;
	}

	@DisplayName("getElementType")
	@Nested
	public class GetElementType {

		@Test
		public void testGetElementType() {
			assertEquals(ShapeElementType.POLYLINE, writer.getElementType());
		}
	}

	@DisplayName("write")
	@Nested
	public class Write {

		@DisplayName("(int)")
		@Nested
		public class WithInt {

			@BeforeEach
			public void setUp() {
				// No local variables to move for this test group.
				// Kept to follow the requested pattern.
			}

			@DisplayName("write 1")
			@Test
			public void testWriteE_write1() throws Exception {
				writer.write(1);
				assertEquals(1, writer.lastWritten);
			}

			@DisplayName("write 1024")
			@Test
			public void testWriteE_write1024() throws Exception {
				writer.write(1024);
				assertEquals(1024, writer.lastWritten);
			}
		}

		@DisplayName("(Collection)")
		@Nested
		public class WithCollection {

			private Collection<Integer> values;

			@BeforeEach
			public void setUp() {
				values = Arrays.asList(1, 2, 3, 4);
			}

			@DisplayName("write [1,2,3,4]")
			@Test
			public void testWriteCollection_writeValues() throws Exception {
				writer.write(values);
				assertEquals(4, writer.lastWritten);
			}
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class AbstractShapeFileWriterStub extends AbstractShapeFileWriter<Integer> {

		/** Last written value.
		 */
		public int lastWritten = Integer.MAX_VALUE;
		
		/**
		 * @param file
		 * @param type
		 * @throws IOException 
		 */
		public AbstractShapeFileWriterStub(File file, ShapeElementType type) throws IOException {
			super(file, type, null, null);
		}

		@Override
		protected ESRIBounds getFileBounds() {
			return new ESRIBounds(
					933441.7999726594, 942959.9281735239,
					-2309936.5466717673, -2292935.499999934,
					0., 0.,
					0., 0.);
		}

		@Override
		protected void writeElement(int recIndex, Integer element, ShapeElementType type) throws IOException {
			assertEquals(ShapeElementType.POLYLINE, type);
			lastWritten = element.intValue();
		}

		@Override
		protected AttributeProvider getAttributeProvider(Integer element)
				throws IOException {
			return null;
		}

		@Override
		protected AttributeProvider[] getAttributeProviders(Collection<? extends Integer> elements) throws IOException {
			return null;
		}

		@Override
		protected int getGroupCountFor(Integer element) throws IOException {
			return 1;
		}

		@Override
		protected ShapeMultiPatchType getGroupTypeFor(Integer element, int groupIndex) throws IOException {
			return null;
		}

		@Override
		protected ESRIPoint getPointAt(Integer element, int groupIndex,
				int pointIndex, boolean expectM, boolean expectZ)
				throws IOException {
			return new ESRIPoint();
		}

		@Override
		protected int getPointCountFor(Integer element, int groupIndex)
				throws IOException {
			return 1;
		}

	}
	
}
