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

import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRIWords;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRI_m;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRI_x;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRI_y;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRI_z;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.io.dbase.DBaseFileField;
import org.arakhne.afc.io.dbase.DBaseFileReader;
import org.arakhne.afc.io.dbase.DBaseFileRecord;

/**
 * This class is a shape file reader.
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
 * @param <E> is the type of element in the shape file.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class AbstractShapeFileReader<E> extends AbstractCommonShapeFileReader<E> {

	/** Default distance under which a reader considers two points as too nearest (in meters).
	 */
	public static final double DEFAULT_FUSION_DISTANCE = 0.01;

	/** This is the reader of the associated DBase file.
	 */
	private final DBaseFileReader dbfReader;

	/** This is the reader of the associated shx file.
	 */
	private final ShapeFileIndexReader shxReader;

	/** Constructor.
	 * @param inputStream is the stream to read
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param shxReader is the shape file index to reader.
	 */
	public AbstractShapeFileReader(ReadableByteChannel inputStream, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader) {
		super(inputStream);
		this.dbfReader = dbase_importer;
		this.shxReader = shxReader;
	}

	/** Constructor.
	 * @param inputStream is the stream to read
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param shxReader is the shape file index to reader.
	 */
	public AbstractShapeFileReader(InputStream inputStream, DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader) {
		super(inputStream);
		this.dbfReader = dbase_importer;
		this.shxReader = shxReader;
	}

	/** Constructor.
	 * @param file is the file to read
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param shxReader is the shape file index to reader.
	 *  @throws IOException in case of error.
	 */
	public AbstractShapeFileReader(File file, DBaseFileReader dbase_importer, ShapeFileIndexReader shxReader) throws IOException {
		super(file);
		this.dbfReader = dbase_importer;
		this.shxReader = shxReader;
	}

	/** Constructor.
	 * @param inputStream is the stream to read
	 * @param dbase_importer is the dBase reader to use to read the attribute's values.
	 * @param shxReader is the shape file index to reader.
	 *  @throws IOException in case of error.
	 */
	public AbstractShapeFileReader(URL inputStream, DBaseFileReader dbase_importer,
			ShapeFileIndexReader shxReader) throws IOException {
		super(inputStream);
		this.dbfReader = dbase_importer;
		this.shxReader = shxReader;
	}

	@Override
	protected void postHeaderReadingStage() throws IOException {
		super.postHeaderReadingStage();
		if (this.dbfReader != null) {
			this.dbfReader.readDBFHeader();
			this.dbfReader.readDBFFields();
		}
	}

	@Override
	public void close() throws IOException {
		super.close();
		if (this.dbfReader != null) {
			this.dbfReader.close();
		}
	}

	@Override
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	protected E readRecord(int recordIndex) throws EOFException, IOException {
		E createdElement = null;

		// Read the index of the element for the current record. The record numbers always
		// begin at 1.
		// Byte 0:		Integer		Big Endian
		final int elementIndex = readBEInt();
		if (elementIndex - 1 != recordIndex) {
			throw new ShapeFileFormatException();
		}
		// Read the record length.
		// The content length for a record is the length of the record contents section measured in
		// 16-bit words. Each record, therefore, contributes (4 + content length) 16-bit words
		// toward the total length of the file, as stored at Byte 24 in the file header.
		// Byte 4:		Integer		Big Endian
		int recordLength = readBEInt() + 4;
		// translate into byte count
		recordLength = fromESRIWords(recordLength);

		// Read the record type (Little Endian Integer)
		final ShapeElementType recordType = ShapeElementType.fromESRIInteger(readLEInt());

		if (recordType != ShapeElementType.NULL && recordType != this.expectedShapeType) {
			throw new InvalidShapeTypeException(recordType.name());
		}

		switch (recordType) {
		//-----------------------------
		// Null Shape
		case NULL:
			createdElement = readNullShape(elementIndex);
			break;

			//-----------------------------
			// Point*
		case POINT:
		case POINT_Z:
		case POINT_M:
			createdElement = readPoint(elementIndex, recordType);
			break;

			//-----------------------------
			// Polyline*
			// Polygon*
			// MultiPoint*
		case POLYLINE:
		case POLYLINE_Z:
		case POLYLINE_M:
		case POLYGON:
		case POLYGON_Z:
		case POLYGON_M:
		case MULTIPOINT:
		case MULTIPOINT_Z:
		case MULTIPOINT_M:
			createdElement = readPolyElement(elementIndex, recordType);
			break;

			//-----------------------------
			// Multipatch
		case MULTIPATCH:
			createdElement = readMultiPatch(elementIndex, recordType);
			break;

			//$CASES-OMITTED$
		default:
			throw new InvalidShapeTypeException(recordType.name());
		}

		if (createdElement != null) {
			try {
				if (postShapeReadingStage(createdElement)) {
					//
					// Create the attributes from the dBase file
					readAttributesFromDBaseFile(createdElement);

					if (postAttributeReadingStage(createdElement)) {
						return createdElement;
					}
				}
			} catch (ClassCastException exception) {
				//
			}
		} else {
			// Skip the attributes
			skipAttributesFromDBaseFile();
		}

		return null;
	}

	/** Called just after the dBase attributes was red.
	 *
	 * @param element_representation is the value returned by the reading function.
	 * @return <code>true</code> if the object is assumed to be valid (ie. it will be replies by
	 *     the reading function), otherwise <code>false</code>.
	 * @throws IOException in case of error.
	 */
	protected boolean postAttributeReadingStage(E element_representation) throws IOException {
		return true;
	}

	/** Called just after the shape was red but before the attributes were retreived.
	 *
	 * @param element_representation is the value returned by the reading function.
	 * @return <code>true</code> if the object is assumed to be valid (ie. it will be replies by
	 *     the reading function), otherwhise <code>false</code>.
	 * @throws IOException in case of error.
	 */
	protected boolean postShapeReadingStage(E element_representation) throws IOException {
		return true;
	}

	/**
	 * Read a null shape.
	 *
	 * @param elementIndex is the index of the element inside the shape file
	 * @return an object representing the creating point, depending of your implementation.
	 *      This value will be passed to {@link #postRecordReadingStage(E)}.
	 * @throws IOException in case of error.
	 */
	private E readNullShape(int elementIndex) throws IOException {
		return null;
	}

	/**
	 * Read a point.
	 *
	 * @param elementIndex is the index of the element inside the shape file
	 * @param type is the type of the element to read.
	 * @return an object representing the creating point, depending of your implementation.
	 *      This value will be passed to {@link #postRecordReadingStage(E)}.
	 */
	private E readPoint(int elementIndex, ShapeElementType type) throws IOException {
		final boolean hasZ = type.hasZ();
		final boolean hasM = type.hasM();

		// Read coordinates
		final double x = fromESRI_x(readLEDouble());
		final double y = fromESRI_y(readLEDouble());
		double z = 0;
		double measure = Double.NaN;

		if (hasZ) {
			z = fromESRI_z(readLEDouble());
		}

		if (hasM) {
			measure = fromESRI_m(readLEDouble());
		}

		// Create the point
		if (!Double.isNaN(x) && !Double.isNaN(y)) {
			return createPoint(createAttributeCollection(elementIndex), elementIndex, new ESRIPoint(x, y, z, measure));
		}
		return null;
	}

	/** Create a point instance.
	 *
	 * @param provider is the attribute provider which must be used by the new element.
	 * @param shape_index is the index of the element in the shape file.
	 * @param point is the location of the point.
	 * @return an object representing the creating point, depending of your implementation.
	 *     This value will be passed to {@link #postRecordReadingStage(Object)}.
	 */
	protected abstract E createPoint(AttributeCollection provider, int shape_index, ESRIPoint point);

	/**
	 * Read a polyelement.
	 *
	 * @param elementIndex is the index of the element inside the shape file
	 * @return an object representing the creating point, depending of your implementation.
	 *     This value will be passed to {@link #postRecordReadingStage(E)}.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	private E readPolyElement(int elementIndex, ShapeElementType type) throws IOException {
		final boolean hasZ = type.hasZ();
		final boolean hasM = type.hasM();

		// Ignore the bounds stored inside the file
		skipBytes(8 * 4);

		// Count of parts
		final int numParts;
		if (type == ShapeElementType.MULTIPOINT || type == ShapeElementType.MULTIPOINT_Z
				|| type == ShapeElementType.MULTIPOINT_M) {
			numParts = 0;
		} else {
			numParts = readLEInt();
		}

		// Count of points
		final int numPoints = readLEInt();

		// Read the parts' indexes
		final int[] parts = new int[numParts];
		for (int idxParts = 0; idxParts < numParts; ++idxParts) {
			parts[idxParts] = readLEInt();
		}

		// Read the points
		final ESRIPoint[] points = new ESRIPoint[numPoints];

		for (int idxPoints = 0; idxPoints < numPoints; ++idxPoints) {
			// Read coordinates
			final double x = fromESRI_x(readLEDouble());
			final double y = fromESRI_y(readLEDouble());

			// Create point
			if (!Double.isNaN(x) && !Double.isNaN(y)) {
				points[idxPoints] = new ESRIPoint(x, y);
			} else {
				throw new ShapeFileException("invalid (x,y) coordinates"); //$NON-NLS-1$
			}
		} // for i= 0 to numPoints

		if (hasZ) {
			// Zmin and Zmax: 2*Double - ignored
			skipBytes(2 * 8);
			// Z array: numpoints*Double
			double z;
			for (int i = 0; i < numPoints; ++i) {
				z = fromESRI_z(readLEDouble());
				if (!Double.isNaN(z)) {
					points[i].setZ(z);
				}
			}
		}

		if (hasM) {
			// Mmin and Mmax: 2*Double - ignored
			skipBytes(2 * 8);
			// M array: numpoints*Double
			double measure;
			for (int i = 0; i < numPoints; ++i) {
				measure = fromESRI_m(readLEDouble());
				if (!Double.isNaN(measure)) {
					points[i].setM(measure);
				}
			}
		}

		// Create the instance of the element
		E newElement = null;
		switch (type) {
		case POLYGON_Z:
		case POLYGON_M:
		case POLYGON:
			newElement = createPolygon(createAttributeCollection(elementIndex), elementIndex, parts, points, hasZ);
			break;
		case POLYLINE_Z:
		case POLYLINE_M:
		case POLYLINE:
			newElement = createPolyline(createAttributeCollection(elementIndex), elementIndex, parts, points, hasZ);
			break;
		case MULTIPOINT:
		case MULTIPOINT_M:
		case MULTIPOINT_Z:
			newElement = createMultiPoint(createAttributeCollection(elementIndex), elementIndex, points, hasZ);
			break;
			//$CASES-OMITTED$
		default:
		}

		return newElement;
	}

	/**
	 * Read a multipatch.
	 *
	 * <p>A MultiPatch consists of a number of surface patches. Each surface patch describes a
	 * surface. The surface patches of a MultiPatch are referred to as its parts, and the type of
	 * part controls how the order of vertices of an MultiPatch part is interpreted.
	 *
	 * <p>A single Triangle Strip, or Triangle Fan, represents a single surface patch.
	 *
	 * <p>A sequence of parts that are rings can describe a polygonal surface patch with holes. The
	 * sequence typically consists of an Outer Ring, representing the outer boundary of the
	 * patch, followed by a number of Inner Rings representing holes. When the individual
	 * types of rings in a collection of rings representing a polygonal patch with holes are
	 * unknown, the sequence must start with First Ring, followed by a number of Rings. A
	 * sequence of Rings not preceded by an First Ring is treated as a sequence of Outer Rings
	 * without holes.
	 *
	 * @param elementIndex is the index of the element inside the shape file
	 * @param type is the type of the shape element to extract.
	 * @return an object representing the creating multipatch, depending of your implementation.
	 *      This value will be passed to {@link #postRecordReadingStage(E)}.
	 */
	private E readMultiPatch(int elementIndex, ShapeElementType type) throws IOException {
		// Ignore the bounds stored inside the file
		skipBytes(8 * 4);

		// Read the part count
		final int partCount = readLEInt();

		// Read the point count
		final int pointCount = readLEInt();

		// Read the parts' indexes
		final int[] parts = new int[partCount];
		for (int idxParts = 0; idxParts < partCount; ++idxParts) {
			parts[idxParts] = readLEInt();
		}

		// Read the parts' types
		final ShapeMultiPatchType[] partTypes = new ShapeMultiPatchType[partCount];
		for (int idxParts = 0; idxParts < partCount; ++idxParts) {
			partTypes[idxParts] = ShapeMultiPatchType.fromESRIInteger(readLEInt());
		}

		// Read the points
		final ESRIPoint[] points = new ESRIPoint[pointCount];

		for (int idxPoints = 0; idxPoints < pointCount; ++idxPoints) {
			// Read coordinates
			final double x = fromESRI_x(readLEDouble());
			final double y = fromESRI_y(readLEDouble());

			// Create point
			if (!Double.isNaN(x) && !Double.isNaN(y)) {
				points[idxPoints] = new ESRIPoint(x, y);
			} else {
				throw new InvalidNumericValueException(
						Double.isNaN(x) ? x : y);
			}
		} // for i= 0 to numPoints

		// Zmin and Zmax: 2*Double - ignored
		skipBytes(2 * 8);

		// Z array: numpoints*Double
		double z;
		for (int i = 0; i < pointCount; ++i) {
			z = fromESRI_z(readLEDouble());
			if (!Double.isNaN(z)) {
				points[i].setZ(z);
			}
		}

		// Mmin and Mmax: 2*Double - ignored
		skipBytes(2 * 8);

		// M array: numpoints*Double
		double measure;
		for (int i = 0; i < pointCount; ++i) {
			measure = fromESRI_m(readLEDouble());
			if (!Double.isNaN(measure)) {
				points[i].setM(measure);
			}
		}

		// Create the instance of the element
		return createMultiPatch(
				createAttributeCollection(elementIndex),
				elementIndex,
				parts,
				partTypes,
				points);
	}

	/** Create a polyline.
	 *
	 * @param provider is the attribute provider which must be used by the new element.
	 * @param shapeIndex is the index of the element in the shape file.
	 * @param parts is the list of the parts, ie the index of the first point in the parts.
	 * @param points is the list of the points.
	 * @param hasZ indicates if the z-coordinates were set.
	 * @return an object representing the creating element, depending of your implementation.
	 *     This value will be passed to {@link #postRecordReadingStage(Object)}.
	 */
	protected abstract E createPolyline(AttributeCollection provider, int shapeIndex,
			int[] parts, ESRIPoint[] points, boolean hasZ);

	/** Create a polygon.
	 *
	 * @param provider is the attribute provider which must be used by the new element.
	 * @param shapeIndex is the index of the element in the shape file.
	 * @param parts is the list of the parts, ie the index of the first point in the parts.
	 * @param points is the list of the points.
	 * @param hasZ indicates if the z-coordinates were set.
	 * @return an object representing the creating element, depending of your implementation.
	 *     This value will be passed to {@link #postRecordReadingStage(Object)}.
	 */
	protected abstract E createPolygon(AttributeCollection provider, int shapeIndex,
			int[] parts, ESRIPoint[] points, boolean hasZ);

	/** Create a multipoint.
	 *
	 * @param provider is the attribute provider which must be used by the new element.
	 * @param shapeIndex is the index of the element in the shape file.
	 * @param points is the list of the points.
	 * @param hasZ indicates if the z-coordinates were set.
	 * @return an object representing the creating element, depending of your implementation.
	 *     This value will be passed to {@link #postRecordReadingStage(Object)}.
	 */
	protected abstract E createMultiPoint(AttributeCollection provider, int shapeIndex,
			ESRIPoint[] points, boolean hasZ);

	/**
	 * Create a multipatch.
	 *
	 * <p>A MultiPatch consists of a number of surface patches. Each surface patch describes a
	 * surface. The surface patches of a MultiPatch are referred to as its parts, and the type of
	 * part controls how the order of vertices of an MultiPatch part is interpreted.
	 *
	 * <p>A single Triangle Strip, or Triangle Fan, represents a single surface patch.
	 *
	 * <p>A sequence of parts that are rings can describe a polygonal surface patch with holes. The
	 * sequence typically consists of an Outer Ring, representing the outer boundary of the
	 * patch, followed by a number of Inner Rings representing holes. When the individual
	 * types of rings in a collection of rings representing a polygonal patch with holes are
	 * unknown, the sequence must start with First Ring, followed by a number of Rings. A
	 * sequence of Rings not preceded by an First Ring is treated as a sequence of Outer Rings
	 * without holes.
	 *
	 * @param provider is the attribute provider which must be used by the new element.
	 * @param shapeIndex is the index of the element in the shape file.
	 * @param parts is the list of the parts, ie the index of the first point in the parts.
	 * @param partTypes is the list of the types of the parts.
	 * @param points is the list of the points.
	 * @return an object representing the creating multipatch, depending of your implementation.
	 *     This value will be passed to {@link #postRecordReadingStage(Object)}.
	 */
	protected abstract E createMultiPatch(AttributeCollection provider, int shapeIndex,
			int[] parts, ShapeMultiPatchType[] partTypes, ESRIPoint[] points);

	/** Create the attribute values that correspond to the dBase content.
	 *
	 * @param created_element is an object representing the creating point, depending of your implementation.
	 *     This value will be passed to {@link #postRecordReadingStage(Object)}.
	 * @throws IOException in case of error.
	 */
	private void readAttributesFromDBaseFile(E created_element) throws IOException {
		// Read the DBF entry
		if (this.dbfReader != null) {

			final List<DBaseFileField> dbfColumns = this.dbfReader.getDBFFields();

			// Read the record even if the shape element was not inserted into
			// the database. It is necessary to not have inconsistancy between
			// the shape entries and the dbase entries.
			final DBaseFileRecord record = this.dbfReader.readNextDBFRecord();

			if (record != null) {
				// Add the dBase values
				for (final DBaseFileField dbfColumn : dbfColumns) {
					// Test if the column was marked as selected.
					// A column was selected if the user want to import the column
					// values into the database.
					if (this.dbfReader.isColumnSelectable(dbfColumn)) {

						final Object fieldValue = record.getFieldValue(dbfColumn.getColumnIndex());
						final AttributeValueImpl attr = new AttributeValueImpl();
						attr.castAndSet(dbfColumn.getAttributeType(), fieldValue);

						putAttributeIn(created_element, dbfColumn.getName(), attr);
					}
				}
			}
		}
	}

	/** Invoked to put an attribute in the element.
	 *
	 * @param element is the element in which the attribute should be put.
	 * @param attributeName is the name of the attribute.
	 * @param value is the value of the attribute.
	 */
	protected abstract void putAttributeIn(E element, String attributeName, AttributeValue value);

	/** Skip the attribute values that correspond to the dBase content.
	 */
	private void skipAttributesFromDBaseFile() throws IOException {
		//
		// Skip the DBF entry
		if (this.dbfReader != null) {
			this.dbfReader.readNextDBFRecord();
		}
	}

	/** Create an attribute provider which will be used by a new element.
	 *
	 * @param elementIndex is the index of the element for which an attribute provider must be created.
	 * @return the new attribute provider which will be passed to one of the creation functions.
	 * @see #createMultiPoint(AttributeCollection, int, ESRIPoint[], boolean)
	 * @see #createPoint(AttributeCollection, int, ESRIPoint)
	 * @see #createPolygon(AttributeCollection, int, int[], ESRIPoint[], boolean)
	 * @see #createPolyline(AttributeCollection, int, int[], ESRIPoint[], boolean)
	 */
	protected abstract AttributeCollection createAttributeCollection(int elementIndex);

	@Override
	public void seek(int recordIndex) throws IOException {
		if (this.shxReader != null) {
			this.shxReader.seek(recordIndex);
			final ShapeFileIndexRecord shxRecord = this.shxReader.read();
			if (shxRecord != null) {
				assert shxRecord.getRecordIndex() == recordIndex;
				readHeader();
				setReadingPosition(
						recordIndex,
						shxRecord.getOffsetInContent());
				return;
			}
		}
		throw new SeekOperationDisabledException();
	}

	@Override
	public boolean isSeekEnabled() {
		return this.shxReader != null && super.isSeekEnabled();
	}

}
