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

package fr.utbm.set.gis.io.binary;

import java.net.URL;

import org.junit.Test;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class GISLayerWriterReaderTest extends AbstractTestCase {

	private static final URL TEST_SHAPE_FILE = Resources.getResource(GISLayerWriterReaderTest.class, "test.shp"); //$NON-NLS-1$
	private static final URL TEST_DBASE_FILE = Resources.getResource(GISLayerWriterReaderTest.class, "test.dbf"); //$NON-NLS-1$

	@Test
	public void shapeResource() {
		assertNotNull(TEST_SHAPE_FILE);
	}

	@Test
	public void dbaseResource() {
		assertNotNull(TEST_DBASE_FILE);
	}

	/**
	 * Must be implemented.
	 * @throws IOException
	 */
//	public static void testReaderWriter_shapeOnly() throws IOException {
//		System.out.print("Reading "+FileSystem.basename(TEST_SHAPE_FILE)+"..."); //$NON-NLS-1$ //$NON-NLS-2$
//		GISShapeFileReader shapeReader = new GISShapeFileReader(TEST_SHAPE_FILE);
//		ArrayMapElementLayer<MapElement> layer1 = new ArrayMapElementLayer<MapElement>();
//		ArrayMapElementLayer<MapElement> layer2 = new ArrayMapElementLayer<MapElement>();
//		MapElement elt;
//		while ((elt=shapeReader.read())!=null) {
//			layer1.addMapElement(elt);
//			layer2.addMapElement(elt);
//		}
//		shapeReader.close();
//		System.out.println("done"); //$NON-NLS-1$
//
//		File tmpFile = File.createTempFile("unittest", ".gis");  //$NON-NLS-1$//$NON-NLS-2$
//		try {
//			GISLayerWriter layerWriter = new GISLayerWriter(tmpFile);
//			layerWriter.write(layer1);
//			layerWriter.write(layer2);
//			layerWriter.close();
//
//
//			GISLayerReader layerReader = new GISLayerReader(tmpFile);
//			MapLayer l1 = layerReader.read();
//			assertNotNull(l1);
//			MapLayer l2 = layerReader.read();
//			assertNotNull(l2);
//			assertNull(layerReader.read());
//
//
//		}
//		finally {
//			tmpFile.delete();
//		}
//	}


}
