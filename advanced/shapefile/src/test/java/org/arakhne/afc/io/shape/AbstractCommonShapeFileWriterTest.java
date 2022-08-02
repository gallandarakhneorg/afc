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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.io.shape.AbstractCommonShapeFileWriter;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileIndexFilter;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class AbstractCommonShapeFileWriterTest extends AbstractIoShapeTest {

	private AbstractCommonShapeFileWriterStub writer;
	private File outputFile;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.outputFile = File.createTempFile("test", "."+ShapeFileIndexFilter.EXTENSION_SHX);  //$NON-NLS-1$//$NON-NLS-2$
		this.outputFile.deleteOnExit();
		this.writer = new AbstractCommonShapeFileWriterStub(this.outputFile, ShapeElementType.POLYGON);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.writer.close();
		this.writer = null;
		this.outputFile.delete();
		this.outputFile = null;
	}

	@Test
	public void testGetElementType() {
		assertEquals(ShapeElementType.POLYGON, this.writer.getElementType());
	}

	@Test
	public void testWriteE() throws Exception {
		this.writer.write(1);
		assertEquals(1, this.writer.lastWritten);

		this.writer.write(1024);
		assertEquals(1024, this.writer.lastWritten);
	}

	@Test
	public void testWriteCollection() throws Exception {
		this.writer.write(Arrays.asList(1, 2, 3, 4));
		assertEquals(4, this.writer.lastWritten);
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class AbstractCommonShapeFileWriterStub extends AbstractCommonShapeFileWriter<Integer> {

		/** Last written value.
		 */
		public int lastWritten = Integer.MAX_VALUE;
		
		/**
		 * @param file
		 * @param type
		 * @throws IOException 
		 */
		public AbstractCommonShapeFileWriterStub(File file, ShapeElementType type) throws IOException {
			super(file, type);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ESRIBounds getFileBounds() {
			return new ESRIBounds(
					933441.7999726594, 942959.9281735239,
					-2309936.5466717673, -2292935.499999934,
					0., 0.,
					0., 0.);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void writeElement(int recIndex, Integer element, ShapeElementType type) throws IOException {
			assertEquals(ShapeElementType.POLYGON, type);
			this.lastWritten = element.intValue();
		}
		
	}
	
}
