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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ESRIFileUtil;
import org.arakhne.afc.io.shape.ESRIPoint;
import org.arakhne.afc.io.shape.ElementExporter;
import org.arakhne.afc.io.shape.ElementFactory;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileReader;
import org.arakhne.afc.io.shape.ShapeFileWriter;
import org.arakhne.afc.io.shape.ShapeMultiPatchType;
import org.arakhne.afc.math.geometry.d3.d.Point3d;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ShapeFileWriter")
@SuppressWarnings("all")
public class ShapeFileWriterTest extends AbstractIoShapeTestCase {

	private File outputFile;
	private ShapeFileWriter<Point3d> writer;
	private ESRIBounds bounds;
	private Point3d[] points;
	private int fileSize;
	
	@BeforeEach
	public void setUp() throws Exception {
		points = randomPoints3D();
		
		double x = Double.POSITIVE_INFINITY;
		double X = Double.NEGATIVE_INFINITY;
		double y = Double.POSITIVE_INFINITY;
		double Y = Double.NEGATIVE_INFINITY;
		double z = Double.POSITIVE_INFINITY;
		double Z = Double.NEGATIVE_INFINITY;
		
		for(Point3d p : points) {
			if (p.getX()<x) x = p.getX();
			if (p.getX()>X) X = p.getX();
			if (p.getY()<y) y = p.getY();
			if (p.getY()>Y) Y = p.getY();
			if (p.getZ()<z) z = p.getZ();
			if (p.getZ()>Z) Z = p.getZ();
		}
		
		bounds = new ESRIBounds(x,X,y,Y,z,Z,
				-getRandom().nextDouble() * 1000.,
				getRandom().nextDouble() * 1000.);
		
		outputFile = File.createTempFile("unittest", ".shp"); //$NON-NLS-1$ //$NON-NLS-2$
		outputFile.deleteOnExit();
		
		writer = new ShapeFileWriter<>(
				outputFile, 
				ShapeElementType.POINT, 
				new ElementExporterStub(bounds));
		
		fileSize = ESRIFileUtil.HEADER_BYTES + points.length * (8+20);
	}

	@AfterEach
	public void tearDown() throws Exception {
		outputFile.delete();
		writer.close();
		outputFile = null;
		writer = null;
		bounds = null;
		points = null;
	}

	private void assertContent() throws Exception {
		ShapeFileReader<Point3d> reader = new ShapeFileReader<>(
				outputFile,
				new ElementFactoryStub());
		
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
		
		assertEquals(ShapeElementType.POINT, reader.getShapeElementType());
		
		assertEquals(fileSize, reader.getFileSize());
		
		Point3d pts;
		for(int i=0; i<points.length; ++i) {
			pts = reader.read();
			assertNotNull(pts);
			assertEpsilonEquals(points[i].getX(), pts.getX());
			assertEpsilonEquals(points[i].getY(), pts.getY());
			assertEpsilonEquals(0., pts.getZ());
		}
		assertNull(reader.read());
		reader.close();
	}

	@DisplayName("getFileBounds")
	@Nested
	public class GetFileBounds {

		private ESRIBounds b;

		@BeforeEach
		public void setUp() {
			b = writer.getFileBounds();
		}

		@DisplayName("getMinX")
		@Test
		public void testGetFileBounds_getMinX() {
			assertEpsilonEquals(bounds.getMinX(), b.getMinX());
		}

		@DisplayName("getMaxX")
		@Test
		public void testGetFileBounds_getMaxX() {
			assertEpsilonEquals(bounds.getMaxX(), b.getMaxX());
		}

		@DisplayName("getMinY")
		@Test
		public void testGetFileBounds_getMinY() {
			assertEpsilonEquals(bounds.getMinY(), b.getMinY());
		}

		@DisplayName("getMaxY")
		@Test
		public void testGetFileBounds_getMaxY() {
			assertEpsilonEquals(bounds.getMaxY(), b.getMaxY());
		}

		@DisplayName("getMinZ")
		@Test
		public void testGetFileBounds_getMinZ() {
			assertEpsilonEquals(bounds.getMinZ(), b.getMinZ());
		}

		@DisplayName("getMaxZ")
		@Test
		public void testGetFileBounds_getMaxZ() {
			assertEpsilonEquals(bounds.getMaxZ(), b.getMaxZ());
		}

		@DisplayName("getMinM")
		@Test
		public void testGetFileBounds_getMinM() {
			assertEpsilonEquals(bounds.getMinM(), b.getMinM());
		}

		@DisplayName("getMaxM")
		@Test
		public void testGetFileBounds_getMaxM() {
			assertEpsilonEquals(bounds.getMaxM(), b.getMaxM());
		}
	}

	@DisplayName("write")
	@Nested
	public class Write {

		@DisplayName("(Point)")
		@Nested
		public class FromPoint {
			@Test
			public void testWriteE() throws Exception {
				for(int i=0; i<points.length; ++i) {
					writer.write(points[i]);
				}
				writer.close();
				assertContent();
			}
		}

		@DisplayName("(Collection)")
		@Test
		public void testWriteCollectionOfQextendsE() throws Exception {
			writer.write(Arrays.asList(points));
			writer.close();
			assertContent();
		}
	}


	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class ElementExporterStub implements ElementExporter<Point3d> {

		private final ESRIBounds bounds;
		
		/**
		 * @param b
		 */
		public ElementExporterStub(ESRIBounds b) {
			bounds = new ESRIBounds(b);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public AttributeProvider getAttributeProvider(Point3d element) throws IOException {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public AttributeProvider[] getAttributeProviders(Collection<? extends Point3d> elements) throws IOException {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ESRIBounds getFileBounds() {
			return bounds;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getGroupCountFor(Point3d element) throws IOException {
			return 1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ShapeMultiPatchType getGroupTypeFor(Point3d element, int groupIndex) throws IOException {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ESRIPoint getPointAt(Point3d element, int groupIndex,
				int pointIndex, boolean expectM, boolean expectZ)
				throws IOException {
			return new ESRIPoint(element);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getPointCountFor(Point3d element, int groupIndex)
				throws IOException {
			return 1;
		}
		
	}
	
	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class ElementFactoryStub implements ElementFactory<Point3d> {

		/**
		 */
		public ElementFactoryStub() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point3d createPoint(AttributeCollection provider, int shapeIndex,
				ESRIPoint point) {
			return new Point3d(point);
		}
		
	}
	
}
