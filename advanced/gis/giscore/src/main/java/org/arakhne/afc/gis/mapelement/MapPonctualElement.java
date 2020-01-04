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

import java.util.Objects;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class is for a map element that owns one geo-referenced point.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class MapPonctualElement extends MapElement {

	private static final long serialVersionUID = 8751041185366169944L;

	/** Geo-location.
	 */
	private final Point2d position = new Point2d();

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param geoX is a location component of the point in the geo-referenced space.
	 * @param geoY is a location component of the point in the geo-referenced space.
	 * @since 4.0
	 */
	public MapPonctualElement(UUID id, double geoX, double geoY) {
		this(id, null, geoX, geoY);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param geo_location is a location component of the point in the geo-referenced space.
	 * @since 4.0
	 */
	public MapPonctualElement(UUID id, Point2D<?, ?> geo_location) {
		this(id, null, geo_location);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param geoX is a location component of the point in the geo-referenced space.
	 * @param geoY is a location component of the point in the geo-referenced space.
	 * @since 4.0
	 */
	public MapPonctualElement(UUID id, AttributeCollection attributeSource, double geoX, double geoY) {
		super(id, attributeSource);
		this.position.set(geoX, geoY);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param geo_location is a location component of the point in the geo-referenced space.
	 * @since 4.0
	 */
	public MapPonctualElement(UUID id, AttributeCollection attributeSource, Point2D<?, ?> geo_location) {
		super(id, attributeSource);
		this.position.set(geo_location);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("position", getPoint()); //$NON-NLS-1$
	}

	@Override
	@SuppressWarnings("checkstyle:covariantequals")
	@Pure
	public boolean equals(MapElement element) {
		if (element instanceof MapPonctualElement) {
			final MapPonctualElement e = (MapPonctualElement) element;
			return this.position.epsilonEquals(e.position, MapElementConstants.POINT_FUSION_DISTANCE);
		}
		return false;
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		return Objects.hash(this.position);
	}

	@Override
	@Pure
	public GeoLocation getGeoLocation() {
		return new GeoLocationPoint(this.position.getX(), this.position.getY());
	}

	/** Replies the point coordinates.
	 *
	 * @return the point
	 */
	@Pure
	public Point2d getPoint() {
		return this.position;
	}

	/** Replies the x coordinate of the point.
	 *
	 * @return x
	 */
	@Pure
	public double getX() {
		return this.position.getX();
	}

	/** Replies the y coordinate of the point.
	 *
	 * @return y
	 */
	@Pure
	public double getY() {
		return this.position.getY();
	}

	/** Set the position of this point.
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void setLocation(double x, double y) {
		this.position.set(x, y);
		fireShapeChanged();
		fireElementChanged();
	}

	/** Set the position of this point.
	 *
	 * @param location the point.
	 */
	public void setLocation(Point2D<?, ?> location) {
		this.position.set(location);
		fireShapeChanged();
		fireElementChanged();
	}

	/**
	 * Replies the distance between this MapPoint and
	 * <var>another_point</var>.
	 *
	 * <p>Equivalent to:
	 * <pre>
	 * return another_point.distance(getX(),getY());
	 * </pre>
	 *
	 * @param <OT> is the type of the given point.
	 * @param another_point another point.
	 * @return the distance between the given point and this point.
	 */
	@Pure
	public <OT extends MapPonctualElement> double distance(OT another_point) {
		return another_point.getDistance(this.position);
	}

	@Pure
	@Override
	public double getDistance(Point2D<?, ?> point) {
		return this.position.getDistance(point);
	}

	/** Replies if the specified point (<var>x</var>,<var>y</var>)
	 * was inside the figure of this MapElement.
	 *
	 * @param point is a geo-referenced coordinate
	 * @param delta is the geo-referenced distance that corresponds to a approximation
	 *     distance in the screen coordinate system
	 * @return <code>true</code> if the specified point has a distance nearest than delta
	 *     to this element, otherwise <code>false</code>
	 */
	@Pure
	@Override
	public boolean contains(Point2D<?, ?> point, double delta) {
		return this.position.getDistance(point) <= delta;
	}

}
