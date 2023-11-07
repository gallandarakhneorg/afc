/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.gis.io.binary;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.io.shape.GISShapeFileReader;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.maplayer.ArrayMapElementLayer;
import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.FileSystem;
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

	private static final URL TEST_SHAPE_FILE;
	private static final URL TEST_DBASE_FILE;

	static {
		TEST_SHAPE_FILE = Resources.getResource(GISLayerWriterReaderTest.class, "test.shp"); //$NON-NLS-1$
		TEST_DBASE_FILE = Resources.getResource(GISLayerWriterReaderTest.class, "test.dbf"); //$NON-NLS-1$
	}
	
	@Test
	public void shapeResource() {
		assertNotNull(TEST_SHAPE_FILE);
	}

	@Test
	public void dbaseResource() {
		assertNotNull(TEST_DBASE_FILE);
	}

	@Test
	public void testReaderWriter_shapeOnly() throws IOException {
		getLogger().info("Reading "+FileSystem.basename(TEST_SHAPE_FILE)+"..."); //$NON-NLS-1$ //$NON-NLS-2$
		ArrayMapElementLayer<MapElement> layer1 = new ArrayMapElementLayer<>();
		ArrayMapElementLayer<MapElement> layer2 = new ArrayMapElementLayer<>();
		try (GISShapeFileReader shapeReader = new GISShapeFileReader(TEST_SHAPE_FILE)) {
			MapElement elt;
			while ((elt=shapeReader.read())!=null) {
				layer1.addMapElement(elt);
				layer2.addMapElement(elt);
			}
		}
		getLogger().info("done"); //$NON-NLS-1$

		File tmpFile = File.createTempFile("unittest", ".gis");  //$NON-NLS-1$//$NON-NLS-2$
		try {
			try (GISLayerWriter layerWriter = new GISLayerWriter(tmpFile)) {
				layerWriter.write(layer1);
				layerWriter.write(layer2);
			}

			try (GISLayerReader layerReader = new GISLayerReader(tmpFile)) {
				MapLayer l1 = layerReader.read();
				assertNotNull(l1);
				MapLayer l2 = layerReader.read();
				assertNotNull(l2);
				assertNull(layerReader.read());
			}
		}
		finally {
			tmpFile.delete();
		}
	}

}
