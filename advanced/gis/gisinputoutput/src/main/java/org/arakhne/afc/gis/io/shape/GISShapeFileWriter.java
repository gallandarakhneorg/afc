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

package org.arakhne.afc.gis.io.shape;

import static org.arakhne.afc.gis.io.shape.GISShapeFileConstants.ID_ATTR;
import static org.arakhne.afc.gis.io.shape.GISShapeFileConstants.UUID_ATTR;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeImpl;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AbstractAttributeProvider;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.gis.coordinate.MapMetricProjection;
import org.arakhne.afc.gis.mapelement.MapComposedElement;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.mapelement.MapPonctualElement;
import org.arakhne.afc.io.dbase.DBaseFileWriter;
import org.arakhne.afc.io.shape.AbstractShapeFileWriter;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ESRIPoint;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.io.shape.ShapeFileException;
import org.arakhne.afc.io.shape.ShapeFileIndexWriter;
import org.arakhne.afc.io.shape.ShapeMultiPatchType;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.vmutil.locale.Locale;

/** Writer of GIS elements in a ESRi shapefile.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GISShapeFileWriter extends AbstractShapeFileWriter<MapElement> {

	private final Rectangle2d bounds;

	private MapMetricProjection mapProjection = MapMetricProjection.getDefault();

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(File shapeName, ShapeElementType elementType, Rectangle2d bounds) throws IOException {
		this(shapeName, elementType, bounds, null, null);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(URL shapeName, ShapeElementType elementType, Rectangle2d bounds) throws IOException {
		this(shapeName, elementType, bounds, null, null);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(OutputStream stream, ShapeElementType elementType, Rectangle2d bounds) throws IOException {
		this(stream, elementType, bounds, null, null);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType, Rectangle2d bounds) throws IOException {
		this(channel, elementType, bounds, null, null);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(File shapeName, ShapeElementType elementType, Rectangle2d bounds,
			ShapeFileIndexWriter shxWriter) throws IOException {
		this(shapeName, elementType, bounds, null, shxWriter);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(URL shapeName, ShapeElementType elementType, Rectangle2d bounds,
			ShapeFileIndexWriter shxWriter) throws IOException {
		this(shapeName, elementType, bounds, null, shxWriter);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(OutputStream stream, ShapeElementType elementType, Rectangle2d bounds,
			ShapeFileIndexWriter shxWriter) throws IOException {
		this(stream, elementType, bounds, null, shxWriter);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType, Rectangle2d bounds,
			ShapeFileIndexWriter shxWriter) throws IOException {
		this(channel, elementType, bounds, null, shxWriter);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param dbfWriter is the writer used to export the attributes
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(File shapeName, ShapeElementType elementType, Rectangle2d bounds,
			DBaseFileWriter dbfWriter) throws IOException {
		this(shapeName, elementType, bounds, dbfWriter, null);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param dbfWriter is the writer used to export the attributes
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(URL shapeName, ShapeElementType elementType, Rectangle2d bounds,
			DBaseFileWriter dbfWriter) throws IOException {
		this(shapeName, elementType, bounds, dbfWriter, null);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param dbfWriter is the writer used to export the attributes
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(OutputStream stream, ShapeElementType elementType, Rectangle2d bounds,
			DBaseFileWriter dbfWriter) throws IOException {
		this(stream, elementType, bounds, dbfWriter, null);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param dbfWriter is the writer used to export the attributes
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType, Rectangle2d bounds,
			DBaseFileWriter dbfWriter) throws IOException {
		this(channel, elementType, bounds, dbfWriter, null);
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(File shapeName, ShapeElementType elementType, Rectangle2d bounds,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(shapeName, elementType, dbfWriter, shxWriter);
		this.bounds = bounds;
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(URL shapeName, ShapeElementType elementType, Rectangle2d bounds,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(shapeName, elementType, dbfWriter, shxWriter);
		this.bounds = bounds;
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(OutputStream stream, ShapeElementType elementType, Rectangle2d bounds,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(stream, elementType, dbfWriter, shxWriter);
		this.bounds = bounds;
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * <p><var>elementType</var> may be computed with {@link #toESRI(Class)}.
	 *
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds of the world to put in the file's header.
	 * @param dbfWriter is the writer used to export the attributes
	 * @param shxWriter is the writer used to create the index
	 * @throws IOException in case of error.
	 */
	public GISShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType, Rectangle2d bounds,
			DBaseFileWriter dbfWriter, ShapeFileIndexWriter shxWriter) throws IOException {
		super(channel, elementType, dbfWriter, shxWriter);
		this.bounds = bounds;
	}

	/**
	 * Replies the type of map element which is corresponding to the given GIS class.
	 *
	 * @param type the type of element.
	 * @return the type of map element.
	 * @throws IllegalArgumentException if the given type is not supported by the I/O API.
	 * @since 4.0
	 */
	@Pure
	public static ShapeElementType toESRI(Class<? extends MapElement> type) {
		if (MapPolyline.class.isAssignableFrom(type)) {
			return ShapeElementType.POLYLINE;
		}
		if (MapPolygon.class.isAssignableFrom(type)) {
			return ShapeElementType.POLYGON;
		}
		if (MapMultiPoint.class.isAssignableFrom(type)) {
			return ShapeElementType.MULTIPOINT;
		}
		if (MapPoint.class.isAssignableFrom(type)) {
			return ShapeElementType.POINT;
		}
		throw new IllegalArgumentException();
	}

	@Override
	protected AttributeProvider getAttributeProvider(MapElement element) throws IOException {
		return new AttributeProviderWrapper(element.getAttributeProvider(), element);
	}

	@Override
	protected AttributeProvider[] getAttributeProviders(Collection<? extends MapElement> elements) throws IOException {
		final AttributeProvider[] containers = new AttributeProvider[elements.size()];
		int idx = 0;
		for (final MapElement elt : elements) {
			containers[idx] = new AttributeProviderWrapper(elt.getAttributeProvider(), elt);
			++idx;
		}
		return containers;
	}

	@Override
	protected int getGroupCountFor(MapElement element) throws IOException {
		if (element == null) {
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "INVALID_NULL_VALUE")); //$NON-NLS-1$
		}
		final ShapeElementType type = getElementType();
		if (type.isElementCollectionType()) {
			if (element instanceof MapComposedElement) {
				final MapComposedElement elt = (MapComposedElement) element;
				return elt.getGroupCount();
			}
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "INVALID_SHAPE_TYPE", //$NON-NLS-1$
					MapComposedElement.class.getName(), element.getClass().getName()));
		} else if (type.isPonctualElementType()) {
			if (element instanceof MapPonctualElement) {
				return 1;
			}
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "INVALID_SHAPE_TYPE", //$NON-NLS-1$
					MapPonctualElement.class.getName(), element.getClass().getName()));
		} else {
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "UNEXPECTED_SHAPE_TYPE", //$NON-NLS-1$
					MapPonctualElement.class.getName()));
		}
	}

	@Override
	protected ShapeMultiPatchType getGroupTypeFor(MapElement element, int groupIndex) throws IOException {
		return ShapeMultiPatchType.OUTER_RING;
	}

	@Override
	protected ESRIPoint getPointAt(MapElement element, int groupIndex,
			int pointIndex, boolean expectM, boolean expectZ)
					throws IOException {
		if (element == null) {
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "INVALID_NULL_VALUE")); //$NON-NLS-1$
		}
		final ShapeElementType type = getElementType();
		if (type.isElementCollectionType()) {
			if (element instanceof MapComposedElement) {
				final MapComposedElement elt = (MapComposedElement) element;
				final Point2d p = elt.getPointAt(groupIndex, pointIndex);
				if (p != null) {
					return doProjection(p);
				}
			}
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "INVALID_SHAPE_TYPE", //$NON-NLS-1$
					MapComposedElement.class.getName(), element.getClass().getName()));
		} else if (type.isPonctualElementType()) {
			if (element instanceof MapPonctualElement) {
				final MapPonctualElement elt = (MapPonctualElement) element;
				final Point2d p = elt.getPoint();
				if (p != null) {
					return doProjection(p);
				}
			}
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "INVALID_SHAPE_TYPE", //$NON-NLS-1$
					MapPonctualElement.class.getName(), element.getClass().getName()));
		} else {
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "UNEXPECTED_SHAPE_TYPE", //$NON-NLS-1$
					MapPonctualElement.class.getName()));
		}
	}

	@Override
	protected int getPointCountFor(MapElement element, int groupIndex) throws IOException {
		if (element == null) {
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "INVALID_NULL_VALUE")); //$NON-NLS-1$
		}
		final ShapeElementType type = getElementType();
		if (type.isElementCollectionType()) {
			if (element instanceof MapComposedElement) {
				final MapComposedElement elt = (MapComposedElement) element;
				return elt.getPointCountInGroup(groupIndex);
			}
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "INVALID_SHAPE_TYPE", //$NON-NLS-1$
					MapComposedElement.class.getName(), element.getClass().getName()));
		} else if (type.isPonctualElementType()) {
			if (element instanceof MapPonctualElement) {
				return 1;
			}
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "INVALID_SHAPE_TYPE", //$NON-NLS-1$
					MapComposedElement.class.getName(), element.getClass().getName()));
		} else {
			throw new ShapeFileException(Locale.getString(
					GISShapeFileWriter.class, "UNEXPECTED_SHAPE_TYPE", //$NON-NLS-1$
					MapPonctualElement.class.getName()));
		}
	}

	@Override
	protected ESRIBounds getFileBounds() {
		return new ESRIBounds(
				this.bounds.getMinX(), this.bounds.getMaxX(),
				this.bounds.getMinY(), this.bounds.getMaxY());
	}

	private ESRIPoint doProjection(Point2d proj) {
		assert this.mapProjection != null;
		assert proj != null;
		final MapMetricProjection def = MapMetricProjection.getDefault();
		if (this.mapProjection != def) {
			return new ESRIPoint(def.convertTo(this.mapProjection, proj));
		}
		return new ESRIPoint(proj);
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

	/** Internal provider.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class AttributeProviderWrapper extends AbstractAttributeProvider {

		private static final long serialVersionUID = 2180695328719426273L;

		private final AttributeProvider wrappedContainer;

		private final MapElement element;

		private final boolean addedId;

		private final boolean addedUuid;

		/** Constructor.
		 * @param wrappedContainer the wrapped container.
		 * @param element the associated element.
		 */
		AttributeProviderWrapper(AttributeProvider wrappedContainer, MapElement element) {
			this.wrappedContainer = wrappedContainer;
			this.element = element;
			if (element.hasAttribute(ID_ATTR)) {
				this.addedId = false;
				this.addedUuid = !element.hasAttribute(UUID_ATTR);
			} else {
				this.addedId = true;
				this.addedUuid = false;
			}
		}

		@Override
		public void freeMemory() {
			this.wrappedContainer.freeMemory();
		}

		@Override
		@Pure
		public Collection<String> getAllAttributeNames() {
			Collection<String> names = this.wrappedContainer.getAllAttributeNames();
			if (this.addedId) {
				names = new ArrayList<>(names);
				names.add(ID_ATTR);
			} else if (this.addedUuid) {
				names = new ArrayList<>(names);
				names.add(UUID_ATTR);
			}
			return names;
		}

		@Override
		@Pure
		public Collection<Attribute> getAllAttributes() {
			final Collection<Attribute> attrs = this.wrappedContainer.getAllAttributes();
			if (this.addedId) {
				attrs.add(new AttributeImpl(ID_ATTR, this.element.getUUID()));
			} else if (this.addedUuid) {
				attrs.add(new AttributeImpl(UUID_ATTR, this.element.getUUID()));
			}
			return attrs;
		}

		@Override
		@Pure
		public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
			final Map<AttributeType, Collection<Attribute>> attrs = this.wrappedContainer.getAllAttributesByType();
			if (this.addedId) {
				Collection<Attribute> attrList = attrs.get(AttributeType.UUID);
				if (attrList == null) {
					attrList = new ArrayList<>();
					attrs.put(AttributeType.UUID, attrList);
				}
				attrList.add(new AttributeImpl(ID_ATTR, this.element.getUUID()));
			} else if (this.addedUuid) {
				Collection<Attribute> attrList = attrs.get(AttributeType.UUID);
				if (attrList == null) {
					attrList = new ArrayList<>();
					attrs.put(AttributeType.UUID, attrList);
				}
				attrList.add(new AttributeImpl(UUID_ATTR, this.element.getUUID()));
			}
			return attrs;
		}

		@Override
		@Pure
		public AttributeValue getAttribute(String name) {
			if ((this.addedId && ID_ATTR.equalsIgnoreCase(name))
					|| (this.addedUuid && UUID_ATTR.equalsIgnoreCase(name))) {
				return new AttributeValueImpl(this.element.getUUID());
			}
			return this.wrappedContainer.getAttribute(name);
		}

		@Override
		@Pure
		public AttributeValue getAttribute(String name, AttributeValue defaultValue) {
			if ((this.addedId && ID_ATTR.equalsIgnoreCase(name))
					|| (this.addedUuid && UUID_ATTR.equalsIgnoreCase(name))) {
				return new AttributeValueImpl(this.element.getUUID());
			}
			return this.wrappedContainer.getAttribute(name, defaultValue);
		}

		@Override
		@Pure
		public int getAttributeCount() {
			return this.wrappedContainer.getAttributeCount()
					+ (this.addedId ? 1 : 0)
					+ (this.addedUuid ? 1 : 0);
		}

		@Override
		@Pure
		public Attribute getAttributeObject(String name) {
			if ((this.addedId && ID_ATTR.equalsIgnoreCase(name))
					|| (this.addedUuid && UUID_ATTR.equalsIgnoreCase(name))) {
				return new AttributeImpl(name, this.element.getUUID());
			}
			return this.wrappedContainer.getAttributeObject(name);
		}

		@Override
		@Pure
		public boolean hasAttribute(String name) {
			if ((this.addedId && ID_ATTR.equalsIgnoreCase(name))
					|| (this.addedUuid && UUID_ATTR.equalsIgnoreCase(name))) {
				return true;
			}
			return this.wrappedContainer.hasAttribute(name);
		}

		@Override
		@Pure
		public void toMap(Map<String, Object> mapToFill) {
			this.wrappedContainer.toMap(mapToFill);
			if (this.addedId) {
				mapToFill.put(ID_ATTR, this.element.getUUID());
			}
			if (this.addedUuid) {
				mapToFill.put(UUID_ATTR, this.element.getUUID());
			}
		}

	} // class AttributeCointainerWrapper

}
