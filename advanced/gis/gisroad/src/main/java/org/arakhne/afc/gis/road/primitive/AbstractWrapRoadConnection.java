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

package org.arakhne.afc.gis.road.primitive;

import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.graph.GraphPoint;

/** This class provides the base implementation
 * of a road connection wrapped to an other road connection.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractWrapRoadConnection implements RoadConnection {

	/** Wrapped road connection.
	 */
	protected RoadConnection connection;

	/**
	 * @param con is the wrapped connection.
	 */
	public AbstractWrapRoadConnection(RoadConnection con) {
		assert con != null;
		this.connection = con;
	}

	@Override
	@Pure
	public final RoadConnection getWrappedRoadConnection() {
		assert this.connection != null;
		return this.connection.getWrappedRoadConnection();
	}

	@Override
	@Pure
	public final Point2d getPoint() {
		return this.connection.getPoint();
	}

	@Override
	@Pure
	public final GeoLocationPoint getGeoLocation() {
		return this.connection.getGeoLocation();
	}

	@Override
	@Pure
	public final UUID getUUID() {
		return this.connection.getUUID();
	}

	@Override
	@Pure
	public final boolean isNearPoint(Point2D<?, ?> point) {
		return this.connection.isNearPoint(point);
	}

	@Override
	@Pure
	public final int compareTo(GraphPoint<RoadConnection, RoadSegment> point) {
		return this.connection.compareTo(point);
	}

	@Override
	@Pure
	public final int compareTo(Point2D<?, ?> pts) {
		return this.connection.compareTo(pts);
	}

	@Override
	@Pure
	public final boolean isEmpty() {
		return getConnectedSegmentCount() > 0;
	}

	@Override
	@Pure
	public final boolean isFinalConnectionPoint() {
		return getConnectedSegmentCount() <= 1;
	}

	@Override
	@Pure
	public final boolean isReallyCulDeSac() {
		return this.connection.isReallyCulDeSac();
	}

	@Override
	@Pure
	public String toString() {
		return this.connection.toString();
	}

}

