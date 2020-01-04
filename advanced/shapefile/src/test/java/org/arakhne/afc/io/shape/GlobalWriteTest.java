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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AbstractAttributeProvider;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.io.dbase.DBaseFileReader;
import org.arakhne.afc.io.dbase.DBaseFileWriter;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.vmutil.FileSystem;

/** Testing writing of SHP, SHX, nd DBF at the same time.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class GlobalWriteTest extends AbstractIoShapeTest {

	/**
	 */
	public static String ATTR1 = "BOOL1"; //$NON-NLS-1$
	/**
	 */
	public static String ATTR2 = "DOUBLE2"; //$NON-NLS-1$
	/**
	 */
	public static String ATTR3 = "STRING3"; //$NON-NLS-1$
	/**
	 */
	public static String ATTR5 = "INDEX5"; //$NON-NLS-1$

	private Point3d[] data;
	private List<TestingAttributeProvider> attributes;
	private Point3dExporter exporter;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.data = new Point3d[10];
		for(int i=0; i<this.data.length; ++i) {
			this.data[i] = randomPoint3D();
		}
		this.attributes = new ArrayList<>();
		for(int i=0; i<this.data.length; ++i) {
			this.attributes.add(new TestingAttributeProvider(i));
		}
		this.exporter = new Point3dExporter();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.exporter = null;
		this.attributes.clear();
		this.attributes = null;
		this.data = null;
	}
	
	private void assertContent(File shpFile, File shxFile, File dbfFile) throws Exception {
		// Read Shp
		ShapeFileReader<Point3d> shpReader = new ShapeFileReader<>(shpFile, new Point3dImporter());
		Point3d p;
		int i=0;
		while ((p = shpReader.read())!=null) {
			assertEpsilonEquals(this.data[i], p);
			++i;
		}
		shpReader.close();
		
		// Read Dbf
		DBaseFileReader dbfReader = new DBaseFileReader(dbfFile);
		dbfReader.readDBFHeader();
		dbfReader.readDBFFields();
		AttributeProvider attrContainer;
		i = 0;
		while ((attrContainer = dbfReader.readNextAttributeProvider())!=null) {
			assertTrue(attrContainer.getAttributeAsBool(ATTR1));
			assertEpsilonEquals(3.14, attrContainer.getAttributeAsDouble(ATTR2));
			assertEquals("hello world", attrContainer.getAttributeAsString(ATTR3)); //$NON-NLS-1$
			assertEquals(i, attrContainer.getAttributeAsInt(ATTR5));
			++i;
		}
		dbfReader.close();
		
		// Read Shx
		try (ShapeFileIndexReader shxReader = new ShapeFileIndexReader(shxFile)) {
			ShapeFileIndexRecord record;
			shxReader.readHeader();
			int offsetInContent = 0;
			while ((record = shxReader.read())!=null) {
				assertEquals(36, record.getRecordContentLength());
				assertEquals(offsetInContent, record.getOffsetInContent());
				assertEquals(offsetInContent+100, record.getOffsetInFile());
				offsetInContent += 44;
			}
		}
	}
	
	/**
	 * @throws Exception
	 */
	public void testShpShxDbfCreationByHand() throws Exception {
		File shpFile = File.createTempFile(GlobalWriteTest.class.getSimpleName(), ".shp"); //$NON-NLS-1$
		File shxFile = FileSystem.replaceExtension(shpFile, ".shx"); //$NON-NLS-1$
		File dbfFile = FileSystem.replaceExtension(shpFile, ".dbf"); //$NON-NLS-1$
		
		try {
			// Shp writing
			ShapeFileWriter<Point3d> writer = new ShapeFileWriter<>(
					shpFile, ShapeElementType.POINT_Z, this.exporter);
			writer.write(Arrays.asList(this.data));
			writer.close();

			// Shx writing
			ESRIFileUtil.generateShapeFileIndexFromShapeFile(shpFile);

			// Dbf writing
			DBaseFileWriter dbfWriter = new DBaseFileWriter(dbfFile);
			dbfWriter.writeHeader(this.attributes);
			dbfWriter.write(this.attributes);
			dbfWriter.close();

			//
			assertContent(shpFile, shxFile, dbfFile);
		}
		finally {
			shpFile.delete();
			shxFile.delete();
			dbfFile.delete();
		}
	}
	
	/**
	 * @throws Exception
	 */
	public void testShpShxDbfEmbeddedCreation() throws Exception {
		File shpFile = File.createTempFile(GlobalWriteTest.class.getSimpleName(), ".shp"); //$NON-NLS-1$
		File shxFile = FileSystem.replaceExtension(shpFile, ".shx"); //$NON-NLS-1$
		File dbfFile = FileSystem.replaceExtension(shpFile, ".dbf"); //$NON-NLS-1$
		
		try {
			// Create Dbf writer
			DBaseFileWriter dbfWriter = new DBaseFileWriter(dbfFile);
			
			// Create Shx writer
			ShapeFileIndexWriter shxWriter = new ShapeFileIndexWriter(
					shxFile,
					ShapeElementType.POINT_Z,
					this.exporter.getFileBounds());
			
			// Shp writing
			ShapeFileWriter<Point3d> writer = new ShapeFileWriter<>(
					shpFile, ShapeElementType.POINT_Z, this.exporter,
					dbfWriter, shxWriter);
			writer.write(Arrays.asList(this.data));
			writer.close();

			//
			assertContent(shpFile, shxFile, dbfFile);
		}
		finally {
			shpFile.delete();
			shxFile.delete();
			dbfFile.delete();
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Point3dImporter implements ElementFactory<Point3d> {
		
		/**
		 */
		public Point3dImporter() {
			//
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point3d createPoint(AttributeCollection provider, int shapeIndex, ESRIPoint point) {
			return new Point3d(point.getX(), point.getY(), point.getZ());
		}
		
	} // class Point3dImporter

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Point3dExporter implements ElementExporter<Point3d> {
		
		/**
		 */
		public Point3dExporter() {
			//
		}

		@Override
		public AttributeProvider getAttributeProvider(Point3d element) throws IOException {
			for(int i=0; i<GlobalWriteTest.this.data.length; ++i) {
				if (GlobalWriteTest.this.data[i].equals(element)) {
					return GlobalWriteTest.this.attributes.get(i);
				}
			}
			return null;
		}

		@Override
		public AttributeProvider[] getAttributeProviders(
				Collection<? extends Point3d> elements) throws IOException {
			AttributeProvider[] attrs = new AttributeProvider[elements.size()];
			Iterator<? extends Point3d> iterator = elements.iterator();
			int i=0;
			while (iterator.hasNext()) {
				attrs[i] = getAttributeProvider(iterator.next()); 
				++i;
			}
			return attrs;
		}

		@Override
		public ESRIBounds getFileBounds() {
			double minx, miny, minz, maxx, maxy, maxz;
			minx = miny = minz = Double.POSITIVE_INFINITY;
			maxx = maxy = maxz = Double.NEGATIVE_INFINITY;
			for(Point3d p : GlobalWriteTest.this.data) {
				if (p.getX()<minx) minx = p.getX();
				if (p.getY()<miny) miny = p.getY();
				if (p.getZ()<minz) minz = p.getZ();
				if (p.getX()>maxx) maxx = p.getX();
				if (p.getY()>maxy) maxy = p.getY();
				if (p.getZ()>maxz) maxz = p.getZ();
			}
			return new ESRIBounds(minx, maxx, miny, maxy, minz, maxz, Double.NaN, Double.NaN);
		}

		@Override
		public int getGroupCountFor(Point3d element) throws IOException {
			return 1;
		}

		@Override
		public ShapeMultiPatchType getGroupTypeFor(Point3d element, int groupIndex) throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public ESRIPoint getPointAt(Point3d element, int groupIndex, int pointIndex, boolean expectM, boolean expectZ)
				throws IOException {
			if (expectZ)
				return new ESRIPoint(element.getX(), element.getY(), element.getZ());
			return new ESRIPoint(element.getX(), element.getY());
		}

		@Override
		public int getPointCountFor(Point3d element, int groupIndex) throws IOException {
			return 1;
		}
		
	} // class Point3dExporter

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class TestingAttributeProvider extends AbstractAttributeProvider {

		private static final long serialVersionUID = -4650185028340292171L;

		private final Attribute attr1 = new AttributeImpl(ATTR1, true);
		private final Attribute attr2 = new AttributeImpl(ATTR2, 3.14);
		private final Attribute attr3 = new AttributeImpl(ATTR3, "hello world"); //$NON-NLS-1$
		private final Attribute attr5;
		
		/**
		 * @param index
		 */
		public TestingAttributeProvider(int index) {
			this.attr5 = new AttributeImpl(ATTR5, index);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void freeMemory() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<String> getAllAttributeNames() {
			return Arrays.asList(ATTR1, ATTR2, ATTR3, ATTR5);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Collection<Attribute> getAllAttributes() {
			return Arrays.asList(this.attr1, this.attr2, this.attr3, this.attr5);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
			Map<AttributeType, Collection<Attribute>> theMap = new TreeMap<>();
			theMap.put(AttributeType.BOOLEAN, Collections.singleton(this.attr1));
			theMap.put(AttributeType.REAL, Collections.singleton(this.attr2));
			theMap.put(AttributeType.STRING, Collections.singleton(this.attr3));
			theMap.put(AttributeType.INTEGER, Collections.singleton(this.attr5));
			return theMap;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public AttributeValue getAttribute(String name) {
			if (ATTR1.equals(name)) return this.attr1;
			if (ATTR2.equals(name)) return this.attr2;
			if (ATTR3.equals(name)) return this.attr3;
			if (ATTR5.equals(name)) return this.attr5;
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public AttributeValue getAttribute(String name, AttributeValue defaultValue) {
			if (ATTR1.equals(name)) return this.attr1;
			if (ATTR2.equals(name)) return this.attr2;
			if (ATTR3.equals(name)) return this.attr3;
			if (ATTR5.equals(name)) return this.attr5;
			return defaultValue;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getAttributeCount() {
			return 5;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Attribute getAttributeObject(String name) {
			if (ATTR1.equals(name)) return this.attr1;
			if (ATTR2.equals(name)) return this.attr2;
			if (ATTR3.equals(name)) return this.attr3;
			if (ATTR5.equals(name)) return this.attr5;
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasAttribute(String name) {
			if (ATTR1.equals(name)) return true;
			if (ATTR2.equals(name)) return true;
			if (ATTR3.equals(name)) return true;
			if (ATTR5.equals(name)) return true;
			return false;
		}

		@Override
		public void toMap(Map<String, Object> mapToFill) {
			throw new UnsupportedOperationException();
		}
		
	} // class TestingAttributeProvider
}
