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

package org.arakhne.afc.gis.io.shape;

import static org.arakhne.afc.gis.io.shape.GISShapeFileConstants.ID_ATTR;
import static org.arakhne.afc.gis.io.shape.GISShapeFileConstants.UUID_ATTR;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeNotInitializedException;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.InvalidAttributeTypeException;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.coordinate.MapMetricProjection;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.mapelement.PointFusionValidator;
import org.arakhne.afc.io.dbase.DBaseFileReader;
import org.arakhne.afc.io.dbase.attr.DBaseFileAttributePool;
import org.arakhne.afc.io.shape.AbstractShapeFileReader;
import org.arakhne.afc.io.shape.ESRIPoint;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileIndexReader;
import org.arakhne.afc.io.shape.ShapeMultiPatchType;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** Reader of GIS elements from an ESRi shapefile.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GISShapeFileReader extends AbstractShapeFileReader<MapElement> {

	private final URL dbaseURL;

	private Class<? extends MapElement> elementType;

	private MapMetricProjection mapProjection = MapMetricProjection.getDefault();

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream) throws IOException {
		this(stream, null, null, null, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, DBaseFileReader dbase_importer) throws IOException {
		this(stream, null, dbase_importer, null, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, DBaseFileReader dbase_importer, URL dBaseAttributeSource) throws IOException {
		this(stream, null, dbase_importer, null, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, ShapeFileIndexReader shxReader) throws IOException {
		this(stream, null, null, shxReader, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader) throws IOException {
		this(stream, null, dbase_importer, shxReader, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader, URL dBaseAttributeSource) throws IOException {
		this(stream, null, dbase_importer, shxReader, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param elementType is the type of the elements to create.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, Class<? extends MapElement> elementType) throws IOException {
		this(stream, elementType, null, null, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer) throws IOException {
		this(stream, elementType, dbase_importer, null, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, URL dBaseAttributeSource) throws IOException {
		this(stream, elementType, dbase_importer, null, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param elementType is the type of the elements to create.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, Class<? extends MapElement> elementType,
			ShapeFileIndexReader shxReader) throws IOException {
		this(stream, elementType, null, shxReader, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param stream is the input stream to read from.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader) throws IOException {
		this(stream, elementType, dbase_importer, shxReader, null);
	}

	/** Create a reader of shapes from specified input stream.
	 *
	 * @param stream is the input stream to read from.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(InputStream stream, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader, URL dBaseAttributeSource) throws IOException {
		super(stream, dbase_importer, shxReader);
		this.elementType = elementType;
		this.dbaseURL = dBaseAttributeSource;
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel) throws IOException {
		this(channel, null, null, null, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, DBaseFileReader dbase_importer) throws IOException {
		this(channel, null, dbase_importer, null, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, DBaseFileReader dbase_importer,
			URL dBaseAttributeSource) throws IOException {
		this(channel, null, dbase_importer, null, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, ShapeFileIndexReader shxReader) throws IOException {
		this(channel, null, null, shxReader, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader) throws IOException {
		this(channel, null, dbase_importer, shxReader, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader, URL dBaseAttributeSource) throws IOException {
		this(channel, null, dbase_importer, shxReader, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param elementType is the type of the elements to create.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, Class<? extends MapElement> elementType) throws IOException {
		this(channel, elementType, null, null, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer) throws IOException {
		this(channel, elementType, dbase_importer, null, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, URL dBaseAttributeSource) throws IOException {
		this(channel, elementType, dbase_importer, null, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param elementType is the type of the elements to create.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, Class<? extends MapElement> elementType,
			ShapeFileIndexReader shxReader) throws IOException {
		this(channel, elementType, null, shxReader, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader) throws IOException {
		this(channel, elementType, dbase_importer, shxReader, null);
	}

	/** Create a reader of shapes from specified channel.
	 *
	 * @param channel is the channel to read from.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(ReadableByteChannel channel, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader, URL dBaseAttributeSource) throws IOException {
		super(channel, dbase_importer, shxReader);
		this.elementType = elementType;
		this.dbaseURL = dBaseAttributeSource;
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file) throws IOException {
		this(file, null, null, null, null);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, DBaseFileReader dbase_importer) throws IOException {
		this(file, null, dbase_importer, null, null);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, DBaseFileReader dbase_importer, URL dBaseAttributeSource) throws IOException {
		this(file, null, dbase_importer, null, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, ShapeFileIndexReader shxReader) throws IOException {
		this(file, null, null, shxReader, null);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader) throws IOException {
		this(file, null, dbase_importer, shxReader, null);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader,
			URL dBaseAttributeSource) throws IOException {
		this(file, null, dbase_importer, shxReader, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, Class<? extends MapElement> elementType) throws IOException {
		this(file, elementType, null, null, null);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer) throws IOException {
		this(file, elementType, dbase_importer, null, null);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, URL dBaseAttributeSource) throws IOException {
		this(file, elementType, dbase_importer, null, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, Class<? extends MapElement> elementType,
			ShapeFileIndexReader shxReader) throws IOException {
		this(file, elementType, null, shxReader, null);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, Class<? extends MapElement> elementType, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader) throws IOException {
		this(file, elementType, dbase_importer, shxReader, null);
	}

	/** Create a reader of shapes from specified file.
	 *
	 * @param file is the file of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(File file, Class<? extends MapElement> elementType, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader, URL dBaseAttributeSource) throws IOException {
		super(file, dbase_importer, shxReader);
		this.elementType = elementType;
		this.dbaseURL = dBaseAttributeSource;
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url) throws IOException {
		this(url, null, null, null, null);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, DBaseFileReader dbase_importer) throws IOException {
		this(url, null, dbase_importer, null, null);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, DBaseFileReader dbase_importer, URL dBaseAttributeSource) throws IOException {
		this(url, null, dbase_importer, null, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, ShapeFileIndexReader shxReader) throws IOException {
		this(url, null, null, shxReader, null);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader) throws IOException {
		this(url, null, dbase_importer, shxReader, null);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader,
			URL dBaseAttributeSource) throws IOException {
		this(url, null, dbase_importer, shxReader, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, Class<? extends MapElement> elementType) throws IOException {
		this(url, elementType, null, null, null);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer) throws IOException {
		this(url, elementType, dbase_importer, null, null);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, URL dBaseAttributeSource) throws IOException {
		this(url, elementType, dbase_importer, null, dBaseAttributeSource);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, Class<? extends MapElement> elementType,
			ShapeFileIndexReader shxReader) throws IOException {
		this(url, elementType, null, shxReader, null);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader) throws IOException {
		this(url, elementType, dbase_importer, shxReader, null);
	}

	/** Create a reader of shapes from specified URL.
	 *
	 * @param url is the url of the stream to read.
	 * @param elementType is the type of the elements to create.
	 * @param dbase_importer is the dBASE reader to use to read the attribute's values.
	 * @param shxReader is the shape file index reader used to access to the shape file record indexes.
	 * @param dBaseAttributeSource is the URL of the dBASE file to create a dBASE-based attribute provider.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileReader(URL url, Class<? extends MapElement> elementType,
			DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader, URL dBaseAttributeSource) throws IOException {
		super(url, dbase_importer, shxReader);
		this.elementType = elementType;
		this.dbaseURL = dBaseAttributeSource;
	}

	/**
	 * Replies the type of map element which is corresponding to the given ESRI type.
	 *
	 * @param type the type of element.
	 * @return the type of map element.
	 * @throws IllegalArgumentException if the given type is not supported by the I/O API.
	 * @since 4.0
	 */
	@Pure
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	public static Class<? extends MapElement> fromESRI(ShapeElementType type) {
		switch (type) {
		case MULTIPOINT:
		case MULTIPOINT_M:
		case MULTIPOINT_Z:
			return MapMultiPoint.class;
		case POINT:
		case POINT_M:
		case POINT_Z:
			return MapPoint.class;
		case POLYGON:
		case POLYGON_M:
		case POLYGON_Z:
			return MapPolygon.class;
		case POLYLINE:
		case POLYLINE_M:
		case POLYLINE_Z:
			return MapPolyline.class;
			//$CASES-OMITTED$
		default:
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Replies the type of map element replied by this reader.
	 *
	 * <p>This function retrieve the element type from the header with
	 * {@link #getShapeElementType()} and matches it to the GIS API
	 * with {@link #fromESRI(ShapeElementType)}.
	 *
	 * @return the type of map element; or <code>null</code> if unsupported.
	 * @since 4.0
	 */
	@Pure
	public Class<? extends MapElement> getMapElementType() {
		if (this.elementType != null) {
			return this.elementType;
		}
		try {
			return fromESRI(getShapeElementType());
		} catch (IllegalArgumentException exception) {
			//
		}
		return null;
	}

	/**
	 * Replies the type of map element replied by this reader.
	 *
	 * <p>This function retrieve the element type from the header with
	 * {@link #getShapeElementType()} and matches it to the GIS API
	 * with {@link #fromESRI(ShapeElementType)}.
	 *
	 * @param type the type of map element; or <code>null</code> to use the type within the shape file header.
	 * @since 15.0
	 */
	public void setMapElementType(Class<? extends MapElement> type) {
		this.elementType = type;
	}

	/** Extract the UUID from the attributes.
	 *
	 * @param provider is a provider of attributes
	 * @return the uuid if found or <code>null</code> if none.
	 */
	protected static UUID extractUUID(AttributeProvider provider) {
		AttributeValue value;

		value = provider.getAttribute(UUID_ATTR);
		if (value != null) {
			try {
				return value.getUUID();
			} catch (InvalidAttributeTypeException e) {
				//
			} catch (AttributeNotInitializedException e) {
				//
			}
		}

		value = provider.getAttribute(ID_ATTR);
		if (value != null) {
			try {
				return value.getUUID();
			} catch (InvalidAttributeTypeException e) {
				//
			} catch (AttributeNotInitializedException e) {
				//
			}
		}

		return null;
	}

	@Override
	protected AttributeCollection createAttributeCollection(int elementIndex) {
		if (this.dbaseURL != null) {
			// The argument has values in [1;size]
			// The Dbase attribute pool uses indexes in [0;size)
			final int dbIndex = elementIndex - 1;
			return DBaseFileAttributePool.getCollection(this.dbaseURL, dbIndex);
		}
		return new HeapAttributeCollection();
	}

	@SuppressWarnings("checkstyle:npathcomplexity")
	private <TT extends MapElement> TT createObjectInstance(UUID id,
			AttributeCollection provider, Class<TT> type) {
		MapElement elt = null;
		final Class<? extends MapElement> elementType = getMapElementType();
		if (elementType != null) {
			if (!type.isAssignableFrom(elementType)) {
				throw new RuntimeException("unable to create an instance of " + type.getName()); //$NON-NLS-1$
			}
			try {
				final Constructor<? extends MapElement> cons = elementType.getConstructor(UUID.class,
						AttributeCollection.class);
				elt = cons.newInstance(id, provider);
			} catch (Throwable exception) {
				//
			}
			if (elt == null) {
				try {
					final Constructor<? extends MapElement> cons = elementType.getConstructor(AttributeCollection.class);
					elt = cons.newInstance(provider);
				} catch (Throwable exception) {
					//
				}
			}
			if (elt == null) {
				try {
					final Constructor<? extends MapElement> cons = elementType.getConstructor(UUID.class);
					elt = cons.newInstance(id);
				} catch (Throwable exception) {
					//
				}
			}
			if (elt == null) {
				try {
					elt = elementType.getDeclaredConstructor().newInstance();
				} catch (Throwable exception) {
					//
				}
			}
		}
		if (elt == null || !type.isInstance(elt)) {
			return null;
		}
		return type.cast(elt);
	}

	private void doProjection(ESRIPoint point, Point2d proj) {
		assert this.mapProjection != null;
		final MapMetricProjection def = MapMetricProjection.getDefault();
		proj.setX(point.getX());
		proj.setY(point.getY());
		if (this.mapProjection != def) {
			final Point2d c = this.mapProjection.convertTo(def, proj);
			if (c != proj) {
				proj.setX(c.getX());
				proj.setY(c.getY());
			}
		}
	}

	@Override
	protected MapElement createMultiPoint(AttributeCollection provider, int shapeIndex, ESRIPoint[] points, boolean hasZ) {
		final UUID id = extractUUID(provider);
		MapMultiPoint elt = createObjectInstance(id, provider, MapMultiPoint.class);
		if (elt == null) {
			elt = new MapMultiPoint(id, provider);
		}
		final Point2d proj = new Point2d();
		for (final ESRIPoint p : points) {
			doProjection(p, proj);
			elt.addPoint(proj.getX(), proj.getY());
		}
		return elt.getPointCount() > 1 ? elt : null;
	}

	@Override
	protected MapElement createPoint(AttributeCollection provider, int shape_index, ESRIPoint point) {
		final UUID id = extractUUID(provider);
		MapPoint elt = createObjectInstance(id, provider, MapPoint.class);
		final Point2d proj = new Point2d();
		doProjection(point, proj);
		if (elt == null) {
			elt = new MapPoint(id, provider, proj.getX(), proj.getY());
		} else {
			elt.setLocation(proj.getX(), proj.getY());
		}
		return elt;
	}

	@Override
	protected MapElement createPolygon(AttributeCollection provider, int shapeIndex, int[] parts,
			ESRIPoint[] points, boolean hasZ) {
		final UUID id = extractUUID(provider);
		MapPolygon elt = createObjectInstance(id, provider, MapPolygon.class);
		if (elt == null) {
			elt = new MapPolygon(id, provider);
		}

		final PointFusionValidator validator = elt.getPointFusionValidator();

		final Point2d proj = new Point2d();

		for (int i = 0; i < parts.length; ++i) {
			final int start = parts[i];
			final int end = (i == (parts.length - 1) ? points.length : parts[i + 1]) - 1;

			doProjection(points[start], proj);
			double lastx = proj.getX();
			double lasty = proj.getY();
			elt.addGroup(proj.getX(), proj.getY());

			for (int j = parts[i] + 1; j <= end; ++j) {
				doProjection(points[j], proj);
				if (!validator.isSame(lastx, lasty, proj.getX(), proj.getY())) {
					elt.addPoint(proj.getX(), proj.getY());
					lastx = proj.getX();
					lasty = proj.getY();
				}
			}

			// Remove the newly created part because it contains only one point
			if (elt.getPointCountInGroup(i) <= 1) {
				elt.removeGroupAt(elt.getGroupCount() - 1);
			}
		}

		return elt.getPointCount() > 1 ? elt : null;
	}

	@Override
	protected MapElement createPolyline(AttributeCollection provider, int shapeIndex, int[] parts,
			ESRIPoint[] points, boolean hasZ) {
		final UUID id = extractUUID(provider);
		MapPolyline elt = createObjectInstance(id, provider, MapPolyline.class);
		if (elt == null) {
			elt = new MapPolyline(id, provider);
		}

		final PointFusionValidator validator = elt.getPointFusionValidator();

		final Point2d proj = new Point2d();

		for (int i = 0; i < parts.length; ++i) {
			final int start = parts[i];

			final int end = (i == (parts.length - 1) ? points.length : parts[i + 1]) - 1;

			assert start >= 0 && start < points.length;
			assert end >= 0 && end < points.length;
			assert start <= end;

			doProjection(points[start], proj);
			double lastx = proj.getX();
			double lasty = proj.getY();
			elt.addGroup(proj.getX(), proj.getY());

			for (int j = start + 1; j <= end; ++j) {
				assert points[j] != null;
				doProjection(points[j], proj);
				if (!validator.isSame(lastx, lasty, proj.getX(), proj.getY())) {
					elt.addPoint(proj.getX(), proj.getY());
					lastx = proj.getX();
					lasty = proj.getY();
				}
			}

			// Remove the newly created part because it contains only one point
			if (elt.getPointCountInGroup(i) <= 1) {
				elt.removeGroupAt(elt.getGroupCount() - 1);
			}
		}

		if (elt.getPointCount() > 1) {
			return elt;
		}
		return null;
	}

	@Override
	protected MapElement createMultiPatch(AttributeCollection provider, int shapeIndex, int[] parts,
			ShapeMultiPatchType[] partTypes, ESRIPoint[] points) {
		return null;
	}

	@Override
	protected void putAttributeIn(MapElement element, String attributeName, AttributeValue value) {
		try {
			element.setAttribute(attributeName, value);
		} catch (AttributeException e) {
			//
		}
	}

	/** Set the map metric projection used by the coordinates in the source file.
	 *
	 * @param projection is the map metric projection in the source file.
	 * @since 4.1
	 */
	public void setMapMetricProjection(MapMetricProjection projection) {
		this.mapProjection = projection == null ? MapMetricProjection.getDefault() : projection;
	}

	/** Replies the map metric projection used by the coordinates in the source file.
	 *
	 * @return the map metric projection in the source file.
	 * @since 4.1
	 */
	@Pure
	public MapMetricProjection getMapMetricProjection() {
		return this.mapProjection;
	}

}
