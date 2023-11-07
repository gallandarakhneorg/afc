/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.gis.road.path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.AbstractGisTest;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.testtools.AbstractTestCase;

/** Stub for RoadConnection.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
class RoadConnectionStub implements RoadConnection {

	private final UUID uid = UUID.randomUUID();
	private final String label;
	private final List<RoadSegmentStub> segments = new ArrayList<>();
	private final Point2d position = new Point2d();

	/**
	 * @param label
	 * @param x
	 * @param y
	 */
	public RoadConnectionStub(String label, double x, double y) {
		this.label = label;
		this.position.set(x,y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RoadConnection getWrappedRoadConnection() {
		return this;
	}

	/**
	 * Add segment
	 *
	 * @param segment
	 */
	public void addSegment(RoadSegmentStub segment) {
		this.segments.add(segment);
		segment.addConnection(this);
	}

	@Override
	public String toString() {
		return this.label;
	}

	@Override
	public RoadSegment getConnectedSegment(int index)
			throws ArrayIndexOutOfBoundsException {
		return this.segments.get(index);
	}

	@Override
	public int getConnectedSegmentCount() {
		return this.segments.size();
	}

	@Override
	public Iterable<RoadSegment> getConnectedSegments() {
		return Iterables.filter(this.segments, RoadSegment.class);
	}

	@Override
	public Iterable<RoadSegment> getConnectedSegmentsStartingFrom(
			RoadSegment startingSegment) {
		return Iterables.filter(this.segments, RoadSegment.class);
	}

	@Override
	public Iterable<RoadSegment> getConnectedSegmentsStartingFromInReverseOrder(RoadSegment startingPoint) {
		return Iterables.filter(Lists.reverse(this.segments), RoadSegment.class);
	}

	@Override
	public RoadSegment getOtherSideSegment(RoadSegment refSegment) {
		if (this.segments.size()==2) {
			if (this.segments.get(0)==refSegment) return this.segments.get(1);
			if (this.segments.get(1)==refSegment) return this.segments.get(0);
		}
		return null;
	}

	@Override
	public Point2d getPoint() {
		return this.position;
	}

	@Override
	public GeoLocationPoint getGeoLocation() {
		return new GeoLocationPoint(this.position.getX(), this.position.getY());
	}

    /** {@inheritDoc}
	 */
	@Override
	public UUID getUUID() {
		return this.uid;
	}

	@Override
	public boolean isConnectedSegment(RoadSegment segment) {
		return this.segments.contains(segment);
	}

	@Override
	public boolean isEmpty() {
		return this.segments.isEmpty();
	}

	@Override
	public boolean isFinalConnectionPoint() {
		return this.segments.size()<=0;
	}

	@Override
	public boolean isNearPoint(Point2D<?, ?> point) {
		return this.position.epsilonEquals(point, AbstractTestCase.EPSILON);
	}

	@Override
	public boolean isReallyCulDeSac() {
		return this.segments.size()<=0;
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment, ClockwiseBoundType boundType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart, ClockwiseBoundType boundType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			ClockwiseBoundType boundType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment, CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart, CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			RoadSegment endSegment, ClockwiseBoundType boundType,
			CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart, RoadSegment endSegment,
			Boolean endSegmentConnectedByItsStart,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
			ClockwiseBoundType boundType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, ClockwiseBoundType boundType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment,
			CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
			CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, ClockwiseBoundType boundType,
			CoordinateSystem2D system) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<? extends GraphPoint.GraphPointConnection<RoadConnection, RoadSegment>> getConnections() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<? extends GraphPoint.GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFrom(
			RoadSegment arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<? extends GraphPoint.GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFromInReverseOrder(
			RoadSegment arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int compareTo(GraphPoint<RoadConnection, RoadSegment> o) {
		return toString().compareTo(o.toString());
	}

	@Override
	public int compareTo(Point2D<?, ?> o) {
		return AbstractGisTest.epsilonCompare(this.position, o);
	}

}
