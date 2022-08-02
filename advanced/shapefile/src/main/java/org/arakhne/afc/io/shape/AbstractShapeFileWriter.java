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

import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRIWords;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRI_m;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRI_x;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRI_y;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRI_z;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.io.dbase.DBaseFileWriter;

/**
 * This class permits to write an ESRI Shape file.
 *
 * <p>When a {@link DBaseFileWriter dBase writer} was given, the attributes associated to
 * each element will be put in a dBase file. The columns in the
 * dBase file depends on the attributes which are defined in the elements at
 * the first call to {@link #write(Collection)} or {@link #write(Object)}.
 * Subsequent calls to {@link #write(Collection)} or {@link #write(Object)} do not
 * change the dBase columns. Consequently the subsequent calls call cause
 * several attributes to not be written in the dBase file even if they exists
 * in the element's attribute containers.
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
@SuppressWarnings("checkstyle:magicnumber")
public abstract class AbstractShapeFileWriter<E> extends AbstractCommonShapeFileWriter<E> {

	private final ShapeFileIndexWriter shxWriter;

	private final DBaseFileWriter dbfWriter;

	private AttributeProvider[] attrContainers;

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public AbstractShapeFileWriter(File shapeName, ShapeElementType elementType,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(shapeName, elementType);
		this.dbfWriter = dbfWriter;
		this.shxWriter = shxWriter;
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public AbstractShapeFileWriter(URL shapeName, ShapeElementType elementType,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(shapeName, elementType);
		this.dbfWriter = dbfWriter;
		this.shxWriter = shxWriter;
	}

	/** Constructor.
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public AbstractShapeFileWriter(OutputStream stream, ShapeElementType elementType,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(stream, elementType);
		this.dbfWriter = dbfWriter;
		this.shxWriter = shxWriter;
	}

	/** Constructor.
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public AbstractShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(channel, elementType);
		this.dbfWriter = dbfWriter;
		this.shxWriter = shxWriter;
	}

	/** Replies the count of points inside the given part.
	 * This function is also invoked for a multipoint. In this case,
	 * the group index is always equal to zero.
	 *
	 * @param element is the element from which the point count must be extracted
	 * @param groupIndex is the index of the group from which the point count must be replied.
	 * @return the count of points in the part at the given index.
	 * @throws IOException in case of error.
	 */
	protected abstract int getPointCountFor(E element, int groupIndex) throws IOException;

	/** Replies the type of the given part for a multipatch element.
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
	 * @param element is the element from which the point count must be extracted
	 * @param groupIndex is the index of the group from which the point count must be replied.
	 * @return the type of the part.
	 * @throws IOException in case of error.
	 */
	protected abstract ShapeMultiPatchType getGroupTypeFor(E element, int groupIndex) throws IOException;

	/** Replies all the attribute containers of the given elements.
	 * The attributes will be put in a DBase file header as columns.
	 * The size of the replied array should be the same
	 * as the size of the collection of elements. Otherwise you must be sure that you
	 * are not loosing attributes. Moreover the position of the AttributeProvider
	 * in the array must corresponds to the position of the element record in the
	 * DBF and shape file.
	 *
	 * @param elements are the element from which the attribute containers must be extracted
	 * @return the attribute containers.
	 * @throws IOException in case of error.
	 */
	protected abstract AttributeProvider[] getAttributeProviders(Collection<? extends E> elements) throws IOException;

	/** Replies the attribute container of the given element.
	 * The attributes will be put in a DBase file.
	 *
	 * @param element is the element from which the attribute container must be extracted
	 * @return the attribute container
	 * @throws IOException in case of error.
	 */
	protected abstract AttributeProvider getAttributeProvider(E element) throws IOException;

	/** Replies the count of groups of points.
	 *
	 * @param element is the element from which the group count must be extracted
	 * @return the count of groups of points.
	 * @throws IOException in case of error.
	 */
	protected abstract int getGroupCountFor(E element) throws IOException;

	/** Replies the point inside the given part at the given index.
	 *
	 * <p>This function is also invoked for a simple point. In this case,
	 * the group and point indexes are both equal to zero.
	 * This function is also invoked for a multipoint. In this case,
	 * the group index is always equal to zero.
	 *
	 * @param element is the element from which the point must be extracted
	 * @param groupIndex is the index of the group from which the point must be extracted.
	 * @param pointIndex is the index of the point in the group.
	 * @param expectM indicates if the M value is expected.
	 * @param expectZ indicates if the Z value is expected.
	 * @return the point.
	 * @throws IOException in case of error.
	 */
	protected abstract ESRIPoint getPointAt(E element, int groupIndex, int pointIndex,
			boolean expectM, boolean expectZ) throws IOException;

	private int writeNullShape(int recordIndex) throws IOException {
		// record index starts at 1
		writeBEInt(recordIndex + 1);

		// Content type
		final int recordLength =  4;

		writeBEInt(toESRIWords(recordLength));
		writeLEInt(ShapeElementType.NULL.shapeType);

		//record length + 4 bytes for the record index + 4 bytes for the record length
		return recordLength + 8;
	}

	private int writePoint(int recordIndex, E element, ShapeElementType type) throws IOException {
		final ESRIPoint pts = getPointAt(element, 0, 0, type.hasM(), type.hasZ());

		// record index starts at 1
		writeBEInt(recordIndex + 1);

		int recordLength =
				// Content type
				4
				// X
				+ 8
				// Y
				+ 8;

		if (type.hasZ()) {
			// Z
			recordLength += 8;
		}
		if (type.hasM()) {
			// M
			recordLength += 8;
		}

		writeBEInt(toESRIWords(recordLength));

		writeLEInt(type.shapeType);
		writeLEDouble(toESRI_x(pts.getX()));
		writeLEDouble(toESRI_y(pts.getY()));
		if (type.hasZ()) {
			writeLEDouble(toESRI_z(pts.getZ()));
		}
		if (type.hasM()) {
			writeLEDouble(toESRI_m(pts.getM()));
		}

		//record length + 4 bytes for the record index + 4 bytes for the record length
		return recordLength + 8;
	}

	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	private int writeMultiPoint(int recordIndex, E element, ShapeElementType type) throws IOException {
		final int count = getPointCountFor(element, 0);

		final boolean hasZ = type.hasZ();
		final boolean hasM = type.hasM();

		final ESRIPoint[] points = new ESRIPoint[count];
		double minx = Double.NaN;
		double miny = Double.NaN;
		double maxx = Double.NaN;
		double maxy = Double.NaN;
		double minz = Double.NaN;
		double maxz = Double.NaN;
		double minm = Double.NaN;
		double maxm = Double.NaN;
		for (int i = 0; i < count; ++i) {
			points[i] = getPointAt(element, 0, i, hasM, hasZ);
			if (points[i] != null) {
				if (i == 0) {
					minx = points[i].getX();
					maxx = minx;
					miny = points[i].getY();
					maxy = miny;
					minz = points[i].getZ();
					maxz = minz;
					minm = points[i].getM();
					maxm = minm;
				} else {
					if (points[i].getX() < minx) {
						minx = points[i].getX();
					}
					if (points[i].getX() > maxx) {
						maxx = points[i].getX();
					}
					if (points[i].getY() < miny) {
						miny = points[i].getY();
					}
					if (points[i].getY() > maxy) {
						maxy = points[i].getY();
					}
					if (points[i].getZ() < minz) {
						minz = points[i].getZ();
					}
					if (points[i].getZ() > maxz) {
						maxz = points[i].getZ();
					}
					if (points[i].getM() < minm) {
						minm = points[i].getM();
					}
					if (points[i].getM() > maxm) {
						maxm = points[i].getM();
					}
				}
			}
		}

		// record index starts at 1
		writeBEInt(recordIndex + 1);

		int recordLength =
				// Content type
				4
				// Box
				+ (4 * 8)
				// NumPoints
				+ 4
				// Points
				+ (count * 16);

		if (hasZ) {
			// minZ, maxZ
			recordLength += 16;
			// z coordinates
			recordLength += 8 * count;
		}
		if (hasM) {
			// minM, maxM
			recordLength += 16;
			// m values
			recordLength += 8 * count;
		}

		writeBEInt(toESRIWords(recordLength));

		writeLEInt(type.shapeType);
		writeLEDouble(toESRI_x(minx));
		writeLEDouble(toESRI_x(miny));
		writeLEDouble(toESRI_x(maxx));
		writeLEDouble(toESRI_x(maxy));
		writeLEInt(count);

		for (int i = 0; i < count; ++i) {
			writeLEDouble(toESRI_x(points[i].getX()));
			writeLEDouble(toESRI_y(points[i].getY()));
		}

		if (hasZ) {
			writeLEDouble(toESRI_z(minz));
			writeLEDouble(toESRI_z(maxz));
			for (int i = 0; i < count; ++i) {
				writeLEDouble(toESRI_z(points[i].getZ()));
			}
		}

		if (hasM) {
			writeLEDouble(toESRI_m(minm));
			writeLEDouble(toESRI_m(maxm));
			for (int i = 0; i < count; ++i) {
				writeLEDouble(toESRI_m(points[i].getM()));
			}
		}

		//record length + 4 bytes for the record index + 4 bytes for the record length
		return recordLength + 8;
	}

	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	private int writePolyElement(int recordIndex, E element, ShapeElementType type) throws IOException {
		final int grpCount = getGroupCountFor(element);
		int ptsCount;
		ESRIPoint pts;

		final boolean hasZ = type.hasZ();
		final boolean hasM = type.hasM();

		final int[] parts = new int[grpCount];
		final List<ESRIPoint> points = new ArrayList<>();

		double minx = Double.NaN;
		double miny = Double.NaN;
		double maxx = Double.NaN;
		double maxy = Double.NaN;
		double minz = Double.NaN;
		double maxz = Double.NaN;
		double minm = Double.NaN;
		double maxm = Double.NaN;

		parts[0] = 0;

		for (int i = 0; i < grpCount; ++i) {
			ptsCount = getPointCountFor(element, i);
			if (i < grpCount - 1) {
				parts[i + 1] = parts[i] + ptsCount;
			}
			for (int j = 0; j < ptsCount; ++j) {
				pts = getPointAt(element, i, j, hasM, hasZ);
				if (pts != null) {
					points.add(pts);
					if (j == 0 && i == 0) {
						minx = pts.getX();
						maxx = minx;
						miny = pts.getY();
						maxy = miny;
						minz = pts.getZ();
						maxz = minz;
						minm = pts.getM();
						maxm = minm;
					} else {
						if (pts.getX() < minx) {
							minx = pts.getX();
						}
						if (pts.getX() > maxx) {
							maxx = pts.getX();
						}
						if (pts.getY() < miny) {
							miny = pts.getY();
						}
						if (pts.getY() > maxy) {
							maxy = pts.getY();
						}
						if (pts.getZ() < minz) {
							minz = pts.getZ();
						}
						if (pts.getZ() > maxz) {
							maxz = pts.getZ();
						}
						if (pts.getM() < minm) {
							minm = pts.getM();
						}
						if (pts.getM() > maxm) {
							maxm = pts.getM();
						}
					}
				}
			}
		}

		// record index starts at 1
		writeBEInt(recordIndex + 1);

		int recordLength =
				// Content type
				4
				// Box
				+ (4 * 8)
				// NumParts
				+ 4
				// NumPoints
				+ 4
				// Parts
				+ (grpCount * 4)
				// Points
				+ (points.size() * 16);

		if (hasZ) {
			// minZ, maxZ
			recordLength += 16;
			// z coordinates
			recordLength += 8 * points.size();
		}
		if (hasM) {
			// minM, maxM
			recordLength += 16;
			// m values
			recordLength += 8 * points.size();
		}

		writeBEInt(toESRIWords(recordLength));

		writeLEInt(type.shapeType);
		writeLEDouble(toESRI_x(minx));
		writeLEDouble(toESRI_y(miny));
		writeLEDouble(toESRI_x(maxx));
		writeLEDouble(toESRI_y(maxy));
		writeLEInt(grpCount);
		writeLEInt(points.size());

		for (int i = 0; i < grpCount; ++i) {
			writeLEInt(parts[i]);
		}

		for (final ESRIPoint p : points) {
			writeLEDouble(toESRI_x(p.getX()));
			writeLEDouble(toESRI_y(p.getY()));
		}

		if (hasZ) {
			writeLEDouble(toESRI_z(minz));
			writeLEDouble(toESRI_z(maxz));
			for (final ESRIPoint p : points) {
				writeLEDouble(toESRI_z(p.getZ()));
			}
		}

		if (hasM) {
			writeLEDouble(toESRI_m(minm));
			writeLEDouble(toESRI_m(maxm));
			for (final ESRIPoint p : points) {
				writeLEDouble(toESRI_m(p.getM()));
			}
		}

		//record length + 4 bytes for the record index + 4 bytes for the record length
		return recordLength + 8;
	}

	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	private int writeMultiPatch(int recordIndex, E element) throws IOException {
		final int grpCount = getGroupCountFor(element);
		int ptsCount;
		ESRIPoint pts;

		final int[] parts = new int[grpCount];
		final ShapeMultiPatchType[] partTypes = new ShapeMultiPatchType[grpCount];
		final List<ESRIPoint> points = new ArrayList<>();

		double minx = Double.NaN;
		double miny = Double.NaN;
		double maxx = Double.NaN;
		double maxy = Double.NaN;
		double minz = Double.NaN;
		double maxz = Double.NaN;
		double minm = Double.NaN;
		double maxm = Double.NaN;

		parts[0] = 0;

		for (int i = 0; i < grpCount; ++i) {
			partTypes[i] = getGroupTypeFor(element, i);
			ptsCount = getPointCountFor(element, i);
			if (i < grpCount - 1) {
				parts[i + 1] = parts[i] + ptsCount;
			}
			for (int j = 0; j < ptsCount; ++j) {
				pts = getPointAt(element, i, j, true, true);
				if (pts != null) {
					points.add(pts);
					if (j == 0 && i == 0) {
						minx = pts.getX();
						maxx = minx;
						miny = pts.getY();
						maxy = miny;
						minz = pts.getZ();
						maxz = minz;
						minm = pts.getM();
						maxm = minm;
					} else {
						if (pts.getX() < minx) {
							minx = pts.getX();
						}
						if (pts.getX() > maxx) {
							maxx = pts.getX();
						}
						if (pts.getY() < miny) {
							miny = pts.getY();
						}
						if (pts.getY() > maxy) {
							maxy = pts.getY();
						}
						if (pts.getZ() < minz) {
							minz = pts.getZ();
						}
						if (pts.getZ() > maxz) {
							maxz = pts.getZ();
						}
						if (pts.getM() < minm) {
							minm = pts.getM();
						}
						if (pts.getM() > maxm) {
							maxm = pts.getM();
						}
					}
				}
			}
		}

		// record index starts at 1
		writeBEInt(recordIndex + 1);

		final int recordLength =
				// Content type
				4
				// Box
				+ (4 * 8)
				// NumParts
				+ 4
				// NumPoints
				+ 4
				// Parts
				+ (grpCount * 4)
				// Part types
				+ (grpCount * 4)
				// Points
				+ (points.size() * 16)
				// minZ, maxZ
				+ 16
				// z coordinates
				+ (points.size() * 8)
				// minM, maxM
				+ 16
				// m values
				+ (points.size() * 8);

		writeBEInt(toESRIWords(recordLength));

		writeLEInt(ShapeElementType.MULTIPATCH.shapeType);
		writeLEDouble(toESRI_x(minx));
		writeLEDouble(toESRI_y(miny));
		writeLEDouble(toESRI_x(maxx));
		writeLEDouble(toESRI_y(maxy));
		writeLEInt(grpCount);
		writeLEInt(points.size());

		for (int i = 0; i < grpCount; ++i) {
			writeLEInt(parts[i]);
		}

		for (int i = 0; i < grpCount; ++i) {
			writeLEInt(partTypes[i].partType);
		}

		for (final ESRIPoint p : points) {
			writeLEDouble(toESRI_x(p.getX()));
			writeLEDouble(toESRI_y(p.getY()));
		}

		writeLEDouble(toESRI_z(minz));
		writeLEDouble(toESRI_z(maxz));
		for (final ESRIPoint p : points) {
			writeLEDouble(toESRI_z(p.getZ()));
		}

		writeLEDouble(toESRI_m(minm));
		writeLEDouble(toESRI_m(maxm));
		for (final ESRIPoint p : points) {
			writeLEDouble(toESRI_m(p.getM()));
		}

		//record length + 4 bytes for the record index + 4 bytes for the record length
		return recordLength + 8;
	}

	@Override
	protected void onHeaderWritten(ESRIBounds box, ShapeElementType type, Collection<? extends E> elements) throws IOException {
		super.onHeaderWritten(box, type, elements);
		if (this.shxWriter != null && this.shxWriter.getElementType() != type) {
			throw new InvalidShapeTypeException(this.shxWriter.getElementType().name());
		}
		if (this.dbfWriter != null) {
			this.attrContainers = getAttributeProviders(elements);
			if (this.attrContainers == null) {
				this.attrContainers = new AttributeProvider[0];
			}
			try {
				this.dbfWriter.writeHeader(this.attrContainers);
			} catch (AttributeException e) {
				throw new ShapeFileException(e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		super.close();
		if (this.shxWriter != null) {
			this.shxWriter.close();
		}
		if (this.dbfWriter != null) {
			this.dbfWriter.close();
			this.attrContainers = null;
		}
	}

	private void writeIndexRecord(int recordLength) throws IOException {
		if (this.shxWriter != null) {
			this.shxWriter.write(recordLength);
		}
	}

	private void writeDbfRecord(int recordIndex, E element) throws IOException {
		if (this.dbfWriter != null) {
			try {
				assert this.attrContainers != null;
				assert recordIndex >= 0;
				final AttributeProvider container;
				if (recordIndex < this.attrContainers.length) {
					container = this.attrContainers[recordIndex];
				} else {
					container = getAttributeProvider(element);
				}
				// Assumes that writeRecord() allows null parameter.
				this.dbfWriter.writeRecord(container);
			} catch (AttributeException e) {
				throw new ShapeFileException(e);
			}
		}
	}

	@Override
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	protected void writeElement(int recordIndex, E element, ShapeElementType type) throws IOException {
		int recordLength = 0;
		switch (type) {
		case NULL:
			recordLength = writeNullShape(recordIndex);
			break;
		case POINT:
		case POINT_Z:
		case POINT_M:
			recordLength = writePoint(recordIndex, element, type);
			break;
		case MULTIPOINT:
		case MULTIPOINT_Z:
		case MULTIPOINT_M:
			recordLength = writeMultiPoint(recordIndex, element, type);
			break;
		case POLYGON:
		case POLYLINE:
		case POLYGON_Z:
		case POLYLINE_Z:
		case POLYGON_M:
		case POLYLINE_M:
			recordLength = writePolyElement(recordIndex, element, type);
			break;
		case MULTIPATCH:
			recordLength = writeMultiPatch(recordIndex, element);
			break;
			//$CASES-OMITTED$
		default:
			throw new InvalidShapeTypeException(type.name());
		}

		writeDbfRecord(recordIndex, element);
		writeIndexRecord(recordLength);
	}

}
