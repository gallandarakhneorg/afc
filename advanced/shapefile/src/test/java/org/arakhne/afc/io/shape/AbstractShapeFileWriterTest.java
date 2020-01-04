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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.collection.AttributeProvider;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class AbstractShapeFileWriterTest extends AbstractIoShapeTest {

	private File outputFile;
	private AbstractShapeFileWriterStub writer;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.outputFile = File.createTempFile("unittest", ".shx"); //$NON-NLS-1$ //$NON-NLS-2$
		this.outputFile.deleteOnExit();
		this.writer = new AbstractShapeFileWriterStub(this.outputFile, ShapeElementType.POLYLINE);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.outputFile.delete();
		this.writer.close();
		this.outputFile = null;
		this.writer = null;
	}

	@Test
	public void testGetElementType() {
		assertEquals(ShapeElementType.POLYLINE, this.writer.getElementType());
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
			assertEquals(ShapeElementType.POLYLINE, type);
			this.lastWritten = element.intValue();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected AttributeProvider getAttributeProvider(Integer element)
				throws IOException {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected AttributeProvider[] getAttributeProviders(Collection<? extends Integer> elements) throws IOException {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected int getGroupCountFor(Integer element) throws IOException {
			return 1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ShapeMultiPatchType getGroupTypeFor(Integer element, int groupIndex) throws IOException {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected ESRIPoint getPointAt(Integer element, int groupIndex,
				int pointIndex, boolean expectM, boolean expectZ)
				throws IOException {
			return new ESRIPoint();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected int getPointCountFor(Integer element, int groupIndex)
				throws IOException {
			return 1;
		}

	}
	
}
