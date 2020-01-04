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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.io.dbase.DBaseFileReader;

/**
 * This class is a shape file reader that instance the default
 * elements.
 *
 * <p>To have a lower memory foot-print, call {@link #disableSeek()}. Indeed,
 * the seek feature forces this reader to maintain a buffer of all the file content.
 *
 * <p>The {@code ShapeFileIndexReader}, passed as parameter of the constructors, is
 * mandatory to seek.
 *
 * <p>The specification of the ESRI Shape file format is described in
 * <a href="./doc-files/esri_specs_0798.pdf">the July 98 specification document</a>.
 *
 * @param <E> is the type of elements which is red by this reader.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ShapeFileReader<E> extends AbstractShapeFileReader<E> {

	/** This object describes how to create instance of shape file elements.
	 */
	protected final ElementFactory<E> factory;

	/** Create a reader of shapes from specified input stream.
	 *
	 * @param inputStream is the stream to read.
	 * @param factory is the object that describes how to create instance of elements.
	 */
	public ShapeFileReader(InputStream inputStream, ElementFactory<E> factory) {
		super(inputStream, null, null);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified input stream.
	 *
	 * @param inputStream is the stream to read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 */
	public ShapeFileReader(InputStream inputStream, DBaseFileReader dbase_importer, ElementFactory<E> factory) {
		super(inputStream, dbase_importer, null);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified input stream.
	 *
	 * @param inputStream is the stream to read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 */
	public ShapeFileReader(InputStream inputStream, ShapeFileIndexReader shxReader, ElementFactory<E> factory) {
		super(inputStream, null, shxReader);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified input stream.
	 *
	 * @param inputStream is the stream to read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 */
	public ShapeFileReader(InputStream inputStream, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader, ElementFactory<E> factory) {
		super(inputStream, dbase_importer, shxReader);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified channel .
	 *
	 * @param channel is the channel to read from.
	 * @param factory is the object that describes how to create instance of elements.
	 */
	public ShapeFileReader(ReadableByteChannel channel, ElementFactory<E> factory) {
		super(channel, null, null);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 */
	public ShapeFileReader(ReadableByteChannel channel, DBaseFileReader dbase_importer, ElementFactory<E> factory) {
		super(channel, dbase_importer, null);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 */
	public ShapeFileReader(ReadableByteChannel channel, ShapeFileIndexReader shxReader, ElementFactory<E> factory) {
		super(channel, null, shxReader);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 */
	public ShapeFileReader(ReadableByteChannel channel, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader, ElementFactory<E> factory) {
		super(channel, dbase_importer, shxReader);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file to open and read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @throws IOException in case of error.
	 */
	public ShapeFileReader(File file, ElementFactory<E> factory) throws IOException {
		super(file, null, null);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file to open and read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public ShapeFileReader(File file, DBaseFileReader dbase_importer, ElementFactory<E> factory) throws IOException {
		super(file, dbase_importer, null);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file to open and read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public ShapeFileReader(File file, ShapeFileIndexReader shxReader, ElementFactory<E> factory) throws IOException {
		super(file, null, shxReader);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file to open and read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public ShapeFileReader(File file, DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader,
			ElementFactory<E> factory) throws IOException {
		super(file, dbase_importer, shxReader);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @throws IOException in case of error.
	 */
	public ShapeFileReader(URL url, ElementFactory<E> factory) throws IOException {
		super(url, null, null);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public ShapeFileReader(URL url, DBaseFileReader dbase_importer, ElementFactory<E> factory) throws IOException {
		super(url, dbase_importer, null);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public ShapeFileReader(URL url, ShapeFileIndexReader shxReader, ElementFactory<E> factory) throws IOException {
		super(url, null, shxReader);
		assert factory != null;
		this.factory = factory;
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param factory is the object that describes how to create instance of elements.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public ShapeFileReader(URL url, DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader,
			ElementFactory<E> factory) throws IOException {
		super(url, dbase_importer, shxReader);
		assert factory != null;
		this.factory = factory;
	}

	@Override
	protected AttributeCollection createAttributeCollection(int elementIndex) {
		return this.factory.createAttributeCollection(elementIndex);
	}

	@Override
	protected E createMultiPoint(AttributeCollection provider, int shapeIndex, ESRIPoint[] points, boolean hasZ) {
		return this.factory.createMultiPoint(provider, shapeIndex, points, hasZ);
	}

	@Override
	protected E createPoint(AttributeCollection provider, int shape_index, ESRIPoint point) {
		return this.factory.createPoint(provider, shape_index, point);
	}

	@Override
	protected E createPolygon(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
		return this.factory.createPolygon(provider, shapeIndex, parts, points, hasZ);
	}

	@Override
	protected E createPolyline(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
		return this.factory.createPolyline(provider, shapeIndex, parts, points, hasZ);
	}

	@Override
	protected E createMultiPatch(AttributeCollection provider, int shapeIndex, int[] parts,
			ShapeMultiPatchType[] partTypes, ESRIPoint[] points) {
		return this.factory.createMultiPatch(provider, shapeIndex, parts, partTypes, points);
	}

	@Override
	protected boolean postAttributeReadingStage(E element) throws IOException {
		super.postAttributeReadingStage(element);
		return this.factory.postAttributeReadingStage(element);
	}

	@Override
	protected boolean postRecordReadingStage(E element) throws IOException {
		if (!super.postRecordReadingStage(element)) {
			return false;
		}
		return this.factory.postEntryReadingStage(element);
	}

	@Override
	protected boolean postShapeReadingStage(E element) throws IOException {
		if (!super.postShapeReadingStage(element)) {
			return false;
		}
		return this.factory.postShapeReadingStage(element);
	}

	@Override
	protected void postHeaderReadingStage() throws IOException {
		super.postHeaderReadingStage();
		this.factory.postHeaderReadingStage();
	}

	@Override
	protected void postReadingStage(boolean success) throws IOException {
		super.postReadingStage(success);
		this.factory.postReadingStage(success);
	}

	@Override
	protected void preReadingStage() throws IOException {
		super.preReadingStage();
		this.factory.preReadingStage();
	}

	@Override
	protected void putAttributeIn(E element, String attributeName, AttributeValue value) {
		this.factory.putAttributeIn(element, attributeName, value);
	}

}
