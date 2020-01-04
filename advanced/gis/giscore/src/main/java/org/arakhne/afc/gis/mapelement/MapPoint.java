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

package org.arakhne.afc.gis.mapelement;

import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class permits to store a geo-located point.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapPoint extends MapPonctualElement {

	private static final long serialVersionUID = -4668637112283077358L;

	/** Size of the points in meters.
	 */
	private double pointSize = GeoLocationUtil.GIS_POINT_SIZE;

	/** Must the point be drawn with a double frame.
	 */
	private boolean doubleFramed;

	/** Create a new map element.
	 *
	 * @param point is a location component of the point in the geo-referenced space.
	 */
	public MapPoint(Point2D<?, ?> point) {
		this(null, null, point.getX(), point.getY());
	}

	/** Create a new map element.
	 *
	 * @param geoX is a location component of the point in the geo-referenced space.
	 * @param geoY is a location component of the point in the geo-referenced space.
	 */
	public MapPoint(double geoX, double geoY) {
		this(null, null, geoX, geoY);
	}

	/** Create a new map element.
	 *
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param point is a location component of the point in the geo-referenced space.
	 */
	public MapPoint(AttributeCollection attributeSource, Point2D<?, ?> point) {
		this(null, attributeSource, point.getX(), point.getY());
	}

	/** Create a new map element.
	 *
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param geoX is a location component of the point in the geo-referenced space.
	 * @param geoY is a location component of the point in the geo-referenced space.
	 */
	public MapPoint(AttributeCollection attributeSource, double geoX, double geoY) {
		this(null, attributeSource, geoX, geoY);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param point is a location component of the point in the geo-referenced space.
	 * @since 4.0
	 */
	public MapPoint(UUID id, Point2D<?, ?> point) {
		this(id, null, point.getX(), point.getY());
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param geoX is a location component of the point in the geo-referenced space.
	 * @param geoY is a location component of the point in the geo-referenced space.
	 * @since 4.0
	 */
	public MapPoint(UUID id, double geoX, double geoY) {
		this(id, null, geoX, geoY);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param point is a location component of the point in the geo-referenced space.
	 * @since 4.0
	 */
	public MapPoint(UUID id, AttributeCollection attributeSource, Point2D<?, ?> point) {
		this(id, attributeSource, point.getX(), point.getY());
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param geoX is a location component of the point in the geo-referenced space.
	 * @param geoY is a location component of the point in the geo-referenced space.
	 * @since 4.0
	 */
	public MapPoint(UUID id, AttributeCollection attributeSource, double geoX, double geoY) {
		super(id, attributeSource, geoX, geoY);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("doubleFramed", isDoubleFramed()); //$NON-NLS-1$
		buffer.add("pointSize", getPointSize()); //$NON-NLS-1$
	}

	/**
	 * Replies if this element has an intersection
	 * with the specified rectangle.
	 *
	 * @return <code>true</code> if this MapElement is intersecting the specified area,
	 *     otherwise <code>false</code>
	 */
	@Override
	@Pure
	public boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> rectangle) {
		return boundsIntersects(rectangle);
	}

	/** Compute the bounds of this element.
	 * This function does not update the internal
	 * attribute replied by {@link #getBoundingBox()}
	 */
	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		double x = this.getX();
		double y = this.getY();

		final double w = this.doubleFramed ? this.pointSize * 2 : this.pointSize;
		final double h = w;

		x -= w / 2.;
		y -= h / 2.;

		return new Rectangle2d(x, y, w, h);
	}

	/** Set the size of this point in meters.
	 *
	 * @param sizeInMeters the size of the point.
	 */
	public void setPointSize(double sizeInMeters) {
		this.pointSize = sizeInMeters;
		fireShapeChanged();
		fireElementChanged();
	}

	/** Replies the size of this point in meters.
	 *
	 * @return the size of this point in meters.
	 */
	@Pure
	public double getPointSize() {
		return this.pointSize;
	}

	/** Set if this point must be drawn with a
	 * double frame.
	 *
	 * @param doubleFramed is <code>true</code> if the point must have a double frame,
	 *     otherwise <code>false</code>
	 */
	public void setDoubleFramed(boolean doubleFramed) {
		this.doubleFramed = doubleFramed;
		fireShapeChanged();
		fireElementChanged();
	}

	/** Replies if this point is dfouble framed.
	 *
	 * @return <code>true</code> if this point has a double frame, otherwise <code>false</code>
	 */
	@Pure
	public boolean isDoubleFramed() {
		return this.doubleFramed;
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

}
