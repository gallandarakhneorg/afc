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

package org.arakhne.afc.gis.coordinate;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.vmutil.locale.Locale;

/** This class defines the supported map metric projections in the GIS
 * API.
 *
 * <p>A map metric projection is any method of representing the surface of a
 * sphere or other three-dimensional body on a plane. Map metric projections
 * are necessary for creating maps. All map metric projections distort the
 * surface in some fashion. Depending on the purpose of the map,
 * some distortions are acceptable and others are not; therefore
 * different map projections exist in order to preserve some properties
 * of the sphere-like body at the expense of other properties. There is
 * no limit to the number of possible map metric projections.
 *
 * <p>Details on the algorithms are in <a href="./doc-files/NTG_71.pdf">NTG_71.pdf</a>.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see "./doc-files/NTG_71.pdf"
 */
public enum MapMetricProjection {

	/**
	 * France Lambert I.
	 */
	FRANCE_LAMBERT_1 {
		@Override
		public Point2d convertTo(MapMetricProjection newProjection, Point2D<?, ?> point) {
			switch (newProjection) {
			case FRANCE_LAMBERT_1:
				break;
			case FRANCE_LAMBERT_2:
				return GISCoordinates.L1_L2(point.getX(), point.getY());
			case FRANCE_LAMBERT_2_EXTENDED:
				return GISCoordinates.L1_EL2(point.getX(), point.getY());
			case FRANCE_LAMBERT_3:
				return GISCoordinates.L1_L3(point.getX(), point.getY());
			case FRANCE_LAMBERT_4:
				return GISCoordinates.L1_L4(point.getX(), point.getY());
			case FRANCE_LAMBERT_93:
				return GISCoordinates.L1_L93(point.getX(), point.getY());
			default:
			}
			return Point2d.convert(point);
		}

		@Override
		public GeodesicPosition convertToGeodesicPosition(Point2D<?, ?> point) {
			return GISCoordinates.L1_WSG84(point.getX(), point.getY());
		}

		@Override
		public Point2d convertFromGeodesicPosition(GeodesicPosition gpsPoint) {
			return GISCoordinates.WSG84_L1(gpsPoint.lambda, gpsPoint.phi);
		}
	},

	/**
	 * France Lambert II.
	 */
	FRANCE_LAMBERT_2 {
		@Override
		public Point2d convertTo(MapMetricProjection newProjection, Point2D<?, ?> point) {
			switch (newProjection) {
			case FRANCE_LAMBERT_1:
				return GISCoordinates.L2_L1(point.getX(), point.getY());
			case FRANCE_LAMBERT_2:
				break;
			case FRANCE_LAMBERT_2_EXTENDED:
				return GISCoordinates.L2_EL2(point.getX(), point.getY());
			case FRANCE_LAMBERT_3:
				return GISCoordinates.L2_L3(point.getX(), point.getY());
			case FRANCE_LAMBERT_4:
				return GISCoordinates.L2_L4(point.getX(), point.getY());
			case FRANCE_LAMBERT_93:
				return GISCoordinates.L2_L93(point.getX(), point.getY());
			default:
			}
			return Point2d.convert(point);
		}

		@Override
		public GeodesicPosition convertToGeodesicPosition(Point2D<?, ?> point) {
			return GISCoordinates.L2_WSG84(point.getX(), point.getY());
		}

		@Override
		public Point2d convertFromGeodesicPosition(GeodesicPosition gpsPoint) {
			return GISCoordinates.WSG84_L2(gpsPoint.lambda, gpsPoint.phi);
		}
	},

	/**
	 * Extended France Lambert II.
	 */
	FRANCE_LAMBERT_2_EXTENDED {
		@Override
		public Point2d convertTo(MapMetricProjection newProjection, Point2D<?, ?> point) {
			switch (newProjection) {
			case FRANCE_LAMBERT_1:
				return GISCoordinates.EL2_L1(point.getX(), point.getY());
			case FRANCE_LAMBERT_2:
				return GISCoordinates.EL2_L2(point.getX(), point.getY());
			case FRANCE_LAMBERT_2_EXTENDED:
				break;
			case FRANCE_LAMBERT_3:
				return GISCoordinates.EL2_L3(point.getX(), point.getY());
			case FRANCE_LAMBERT_4:
				return GISCoordinates.EL2_L4(point.getX(), point.getY());
			case FRANCE_LAMBERT_93:
				return GISCoordinates.EL2_L93(point.getX(), point.getY());
			default:
			}
			return Point2d.convert(point);
		}

		@Override
		public GeodesicPosition convertToGeodesicPosition(Point2D<?, ?> point) {
			return GISCoordinates.EL2_WSG84(point.getX(), point.getY());
		}

		@Override
		public Point2d convertFromGeodesicPosition(GeodesicPosition gpsPoint) {
			return GISCoordinates.WSG84_EL2(gpsPoint.lambda, gpsPoint.phi);
		}
	},

	/**
	 * France Lambert III.
	 */
	FRANCE_LAMBERT_3 {
		@Override
		public Point2d convertTo(MapMetricProjection newProjection, Point2D<?, ?> point) {
			switch (newProjection) {
			case FRANCE_LAMBERT_1:
				return GISCoordinates.L3_L1(point.getX(), point.getY());
			case FRANCE_LAMBERT_2:
				return GISCoordinates.L3_L2(point.getX(), point.getY());
			case FRANCE_LAMBERT_2_EXTENDED:
				return GISCoordinates.L3_EL2(point.getX(), point.getY());
			case FRANCE_LAMBERT_3:
				break;
			case FRANCE_LAMBERT_4:
				return GISCoordinates.L3_L4(point.getX(), point.getY());
			case FRANCE_LAMBERT_93:
				return GISCoordinates.L3_L93(point.getX(), point.getY());
			default:
			}
			return Point2d.convert(point);
		}

		@Override
		public GeodesicPosition convertToGeodesicPosition(Point2D<?, ?> point) {
			return GISCoordinates.L3_WSG84(point.getX(), point.getY());
		}

		@Override
		public Point2d convertFromGeodesicPosition(GeodesicPosition gpsPoint) {
			return GISCoordinates.WSG84_L3(gpsPoint.lambda, gpsPoint.phi);
		}
	},

	/**
	 * France Lambert IV.
	 */
	FRANCE_LAMBERT_4 {
		@Override
		public Point2d convertTo(MapMetricProjection newProjection, Point2D<?, ?> point) {
			switch (newProjection) {
			case FRANCE_LAMBERT_1:
				return GISCoordinates.L4_L1(point.getX(), point.getY());
			case FRANCE_LAMBERT_2:
				return GISCoordinates.L4_L2(point.getX(), point.getY());
			case FRANCE_LAMBERT_2_EXTENDED:
				return GISCoordinates.L4_EL2(point.getX(), point.getY());
			case FRANCE_LAMBERT_3:
				return GISCoordinates.L4_L3(point.getX(), point.getY());
			case FRANCE_LAMBERT_4:
				break;
			case FRANCE_LAMBERT_93:
				return GISCoordinates.L4_L93(point.getX(), point.getY());
			default:
			}
			return Point2d.convert(point);
		}

		@Override
		public GeodesicPosition convertToGeodesicPosition(Point2D<?, ?> point) {
			return GISCoordinates.L4_WSG84(point.getX(), point.getY());
		}

		@Override
		public Point2d convertFromGeodesicPosition(GeodesicPosition gpsPoint) {
			return GISCoordinates.WSG84_L4(gpsPoint.lambda, gpsPoint.phi);
		}
	},

	/**
	 * France Lambert 93.
	 */
	FRANCE_LAMBERT_93 {
		@Override
		public Point2d convertTo(MapMetricProjection newProjection, Point2D<?, ?> point) {
			switch (newProjection) {
			case FRANCE_LAMBERT_1:
				return GISCoordinates.L93_L1(point.getX(), point.getY());
			case FRANCE_LAMBERT_2:
				return GISCoordinates.L93_L2(point.getX(), point.getY());
			case FRANCE_LAMBERT_2_EXTENDED:
				return GISCoordinates.L93_EL2(point.getX(), point.getY());
			case FRANCE_LAMBERT_3:
				return GISCoordinates.L93_L3(point.getX(), point.getY());
			case FRANCE_LAMBERT_4:
				return GISCoordinates.L93_L4(point.getX(), point.getY());
			case FRANCE_LAMBERT_93:
				break;
			default:
			}
			return Point2d.convert(point);
		}

		@Override
		public GeodesicPosition convertToGeodesicPosition(Point2D<?, ?> point) {
			return GISCoordinates.L93_WSG84(point.getX(), point.getY());
		}

		@Override
		public Point2d convertFromGeodesicPosition(GeodesicPosition gpsPoint) {
			return GISCoordinates.WSG84_L93(gpsPoint.lambda, gpsPoint.phi);
		}
	};


	/** Replies the default map metric projection.
	 *
	 * @return the default projection, never <code>null</code>.
	 */
	@Pure
	public static MapMetricProjection getDefault() {
		return FRANCE_LAMBERT_2_EXTENDED;
	}

	@Override
	@Pure
	public String toString() {
		return Locale.getString(name());
	}

	/** Convert the given point from the current map projection to
	 * the given map projection.
	 *
	 * @param newProjection is the target projection.
	 * @param point is the point to convert in the current project.
	 * @return the point converted into the target projection; never <code>null</code>.
	 */
	@Pure
	public abstract Point2d convertTo(MapMetricProjection newProjection, Point2D<?, ?> point);

	/** Convert the given point in the current map projection into a GPS point.
	 *
	 * @param point is the point to convert.
	 * @return the GPS position.
	 */
	@Pure
	public abstract GeodesicPosition convertToGeodesicPosition(Point2D<?, ?> point);

	/** Convert the given GPS point into a point in the current map projection.
	 *
	 * @param gpsPoint is the Geodesic point to convert.
	 * @return the projection position.
	 */
	@Pure
	public abstract Point2d convertFromGeodesicPosition(GeodesicPosition gpsPoint);

}
