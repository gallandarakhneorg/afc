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

package org.arakhne.afc.io.shape;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.WritableByteChannel;
import java.util.Collection;

import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.io.dbase.DBaseFileWriter;

/**
 * This class permits to write an ESRI Shape file.
 *
 * <p>The specification of the ESRI Shape file format is described in
 * <a href="./doc-files/esri_specs_0798.pdf">the July 98 specification document</a>.
 *
 * @param <E> is the type of element to write inside the ESRI shape file.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ShapeFileWriter<E> extends AbstractShapeFileWriter<E> {

	private final ElementExporter<E> exporter;

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(File shapeName, ShapeElementType elementType, ElementExporter<E> exporter) throws IOException {
		this(shapeName, elementType, exporter, null, null);
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(URL shapeName, ShapeElementType elementType, ElementExporter<E> exporter) throws IOException {
		this(shapeName, elementType, exporter, null, null);
	}

	/** Constructor.
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(OutputStream stream, ShapeElementType elementType, ElementExporter<E> exporter) throws IOException {
		this(stream, elementType, exporter, null, null);
	}

	/** Constructor.
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType,
			ElementExporter<E> exporter) throws IOException {
		this(channel, elementType, exporter, null, null);
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(File shapeName, ShapeElementType elementType, ElementExporter<E> exporter,
			ShapeFileIndexWriter shxWriter) throws IOException {
		this(shapeName, elementType, exporter, null, shxWriter);
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(URL shapeName, ShapeElementType elementType, ElementExporter<E> exporter,
			ShapeFileIndexWriter shxWriter) throws IOException {
		this(shapeName, elementType, exporter, null, shxWriter);
	}

	/** Constructor.
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(OutputStream stream, ShapeElementType elementType, ElementExporter<E> exporter,
			ShapeFileIndexWriter shxWriter) throws IOException {
		this(stream, elementType, exporter, null, shxWriter);
	}

	/** Constructor.
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType, ElementExporter<E> exporter,
			ShapeFileIndexWriter shxWriter) throws IOException {
		this(channel, elementType, exporter, null, shxWriter);
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param dbfWriter is the writer used to export the attributes
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(File shapeName, ShapeElementType elementType, ElementExporter<E> exporter,
			DBaseFileWriter dbfWriter) throws IOException {
		this(shapeName, elementType, exporter, dbfWriter, null);
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param dbfWriter is the writer used to export the attributes
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(URL shapeName, ShapeElementType elementType, ElementExporter<E> exporter,
			DBaseFileWriter dbfWriter) throws IOException {
		this(shapeName, elementType, exporter, dbfWriter, null);
	}

	/** Constructor.
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param dbfWriter is the writer used to export the attributes
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(OutputStream stream, ShapeElementType elementType, ElementExporter<E> exporter,
			DBaseFileWriter dbfWriter) throws IOException {
		this(stream, elementType, exporter, dbfWriter, null);
	}

	/** Constructor.
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param dbfWriter is the writer used to export the attributes
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType, ElementExporter<E> exporter,
			DBaseFileWriter dbfWriter) throws IOException {
		this(channel, elementType, exporter, dbfWriter, null);
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(File shapeName, ShapeElementType elementType, ElementExporter<E> exporter,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(shapeName, elementType, dbfWriter, shxWriter);
		assert exporter != null;
		this.exporter = exporter;
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(URL shapeName, ShapeElementType elementType, ElementExporter<E> exporter,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(shapeName, elementType, dbfWriter, shxWriter);
		assert exporter != null;
		this.exporter = exporter;
	}

	/** Constructor.
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(OutputStream stream, ShapeElementType elementType,
			ElementExporter<E> exporter, DBaseFileWriter dbfWriter,
			ShapeFileIndexWriter shxWriter) throws IOException {
		super(stream, elementType, dbfWriter, shxWriter);
		assert exporter != null;
		this.exporter = exporter;
	}

	/** Constructor.
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param exporter is the exporter object which will invoked to retreive element data.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public ShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType, ElementExporter<E> exporter,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(channel, elementType, dbfWriter, shxWriter);
		assert exporter != null;
		this.exporter = exporter;
	}

	@Override
	protected AttributeProvider[] getAttributeProviders(Collection<? extends E> elements) throws IOException {
		return this.exporter.getAttributeProviders(elements);
	}

	@Override
	protected AttributeProvider getAttributeProvider(E element) throws IOException {
		return this.exporter.getAttributeProvider(element);
	}

	@Override
	protected int getGroupCountFor(E element) throws IOException {
		return this.exporter.getGroupCountFor(element);
	}

	@Override
	protected ESRIPoint getPointAt(E element, int groupIndex, int pointIndex, boolean expectM,
			boolean expectZ) throws IOException {
		return this.exporter.getPointAt(element, groupIndex, pointIndex, expectM, expectZ);
	}

	@Override
	protected int getPointCountFor(E element, int groupIndex) throws IOException {
		return this.exporter.getPointCountFor(element, groupIndex);
	}

	@Override
	protected ShapeMultiPatchType getGroupTypeFor(E element, int groupIndex) throws IOException {
		return this.exporter.getGroupTypeFor(element, groupIndex);
	}

	@Override
	protected ESRIBounds getFileBounds() {
		return this.exporter.getFileBounds();
	}

}
