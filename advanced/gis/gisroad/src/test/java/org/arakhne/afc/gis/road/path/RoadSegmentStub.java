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

package org.arakhne.afc.gis.road.path;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.arakhne.afc.attrs.attr.Attribute;
import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.collection.AttributeChangeListener;
import org.arakhne.afc.attrs.collection.AttributeProvider;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationArea;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.gis.road.primitive.RoadType;
import org.arakhne.afc.gis.road.primitive.TrafficDirection;
import org.arakhne.afc.math.geometry.d1.Direction1D;
import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Path2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.graph.DynamicDepthUpdater;
import org.arakhne.afc.math.graph.GraphIterator;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** Stub for RoadSegment.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
class RoadSegmentStub implements RoadSegment {

	private static final long serialVersionUID = 638305637654769751L;

	private final UUID id = UUID.randomUUID();
	private final String label;
	private final GeoLocationArea geolocation;
	private final double length;
	private RoadConnectionStub con1 = null;
	private RoadConnectionStub con2 = null;
	private final List<Point2d> points = new ArrayList<>();

	/**
	 * @param label
	 * @param pts
	 */
	public RoadSegmentStub(String label, double... pts) {
		this.label = label;
		double l = 0.;
		Point2d p,previous = null;
		Rectangle2d bounds = new Rectangle2d();
		boolean first = true;
		for(int i=0; i<pts.length-1; i+=2) {
			p = new Point2d(pts[i], pts[i+1]);
			this.points.add(p);
			if (first) {
				first = false;
				bounds.set(p, p);
			} else {
				bounds.add(p);
			}
			if (previous!=null) {
				l += p.getDistance(previous);
			}
			previous = p;
		}
		this.length = l;
		this.geolocation = new GeoLocationArea(bounds);
	}

	@Override
	public String toString() {
		return JsonBuffer.toString(this);
	}

	@Override
	public UUID getUUID() {
		return this.id;
	}

	@Override
	public RoadSegment clone() {
		try {
			return (RoadSegment)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param con
	 */
	public void addConnection(RoadConnectionStub con) {
		if (this.con1==null) this.con1 = con;
		else if (this.con2==null) this.con2 = con;
		else throw new IllegalArgumentException();
	}

	@Override
	public void addUserData(String id, Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearUserData(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearUserData() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Point2D<?, ?> p, double delta) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Point2D<?, ?> p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsUserData(String id, Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GraphIterator<RoadSegment, RoadConnection> depthIterator(
			double depth, double positionFromStartingPoint,
			RoadConnection startingPoint, boolean allowManyReplies,
			boolean assumeOrientedSegments,
			DynamicDepthUpdater<?, RoadSegment, RoadConnection> dynamicDepthUpdater) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double distance(Point2D<?, ?> p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double distance(Point2D<?, ?> p, double width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double distanceToEnd(Point2D<?, ?> p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double distanceToEnd(Point2D<?, ?> p, double width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point2d getAntepenulvianPoint() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Rectangle2d getBoundingBox() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getDistanceFromStart(double ratio) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getDistanceToEnd(double ratio) {
		throw new UnsupportedOperationException();
	}

	@Override
	public RoadConnection getEndPoint() {
		return this.con2;
	}

	@Override
	public Point2d getFirstPoint() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GeoLocation getGeoLocation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point2d getGeoLocationForDistance(double desiredDistance) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point2d getGeoLocationForDistance(double desiredDistance, double shifting) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point2d getGeoLocationForDistance(double desiredDistance, double shifting, Vector2D<?, ?> tangent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getGeoLocationForDistance(double desiredDistance, Point2D<?, ?> geoLocation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getGeoLocationForDistance(double desiredDistance, double shifting, Point2D<?, ?> geoLocation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getGeoLocationForDistance(double desiredDistance,
			double shifting, Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point2d getGeoLocationForLocationRatio(double ratio) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point2d getGeoLocationForLocationRatio(double ratio, double shifting) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point2d getGeoLocationForLocationRatio(double ratio,
			double shifting, Vector2D<?, ?> tangent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getGeoLocationForLocationRatio(double ratio, Point2D<?, ?> geoLocation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getGeoLocationForLocationRatio(double ratio,
			double shifting, Point2D<?, ?> geoLocation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getGeoLocationForLocationRatio(double ratio,
			double shifting, Point2D<?, ?> geoLocation, Vector2D<?, ?> tangent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getLaneCenter(int laneIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLaneCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Direction1D getLaneDirection(int laneIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getLaneSize(int laneIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point2d getLastPoint() {
		return this.points.get(this.points.size()-1);
	}

	@Override
	public double getLength() {
		return this.length;
	}

	@Override
	public RoadConnection getOtherSidePoint(RoadConnection refPoint) {
		if (this.con1==refPoint) return this.con2;
		if (this.con2==refPoint) return this.con1;
		return null;
	}

	@Override
	public Point2d getPointAt(int index) {
		return this.points.get(index);
	}

	@Override
	public int getPointCount() {
		return this.points.size();
	}

	@Override
	public RoadNetwork getRoadNetwork() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<RoadSegment> getSegmentChain() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<RoadSegment> getSegmentChain(boolean forwardSearch,
			boolean backwardSearch) {
		throw new UnsupportedOperationException();
	}

	@Override
	public RoadConnection getSharedConnectionWith(RoadSegment otherSegment) {
		RoadConnection oc = otherSegment.getBeginPoint();
		if (oc==this.con1) return this.con1;
		if (oc==this.con2) return this.con2;
		oc = otherSegment.getEndPoint();
		if (oc==this.con1) return this.con1;
		if (oc==this.con2) return this.con2;
		return null;
	}

	@Override
	public RoadConnection getBeginPoint() {
		return this.con1;
	}

	@Override
	public <T> T getUserData(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> Collection<? extends T> getUserDataCollection(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getWidth() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getRoadBorderDistance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setWidth(double width) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public RoadSegment getWrappedRoadSegment() {
		return this;
	}

	@Override
	public boolean hasUserData(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> bounds) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isConnectedTo(RoadSegment otherSegment) {
		RoadConnection oc = otherSegment.getBeginPoint();
		if (oc==this.con1 || oc==this.con2) return true;
		oc = otherSegment.getEndPoint();
		if (oc==this.con1 || oc==this.con2) return true;
		return false;
	}

	@Override
	public GraphIterator<RoadSegment, RoadConnection> iterator(
			RoadConnection startingPoint, boolean allowManyReplies,
			boolean assumeOrientedSegments) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GraphIterator<RoadSegment, RoadConnection> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Point2d> pointIterator() {
		return this.points.iterator();
	}

	@Override
	public Iterable<Point2d> points() {
		return this.points;
	}

	@Override
	public boolean removeUserData(String id, Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setUserData(String id, Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GeoId getGeoId() {
		return this.geolocation.toGeoId();
	}

	@Override
	public Vector2d getTangentAt(double arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFirstPointConnectedTo(Segment1D<?, ?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isLastPointConnectedTo(Segment1D<?, ?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void projectsOnPlane(double arg0, Point2D<?, ?> arg1, Vector2D<?, ?> arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void projectsOnPlane(double arg0, double arg1, Point2D<?, ?> arg2, Vector2D<?, ?> arg3) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttributeChangeListener(AttributeChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllAttributes() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAttribute(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttributeChangeListener(AttributeChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renameAttribute(String oldname, String newname) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renameAttribute(String oldname, String newname,
			boolean overwrite) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(Attribute value) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, AttributeValue value)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, boolean value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, int value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, long value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, float value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, double value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, String value) {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, UUID value) {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, URL value) {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, URI value) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Date value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Attribute> attributes() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void freeMemory() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getAllAttributeNames() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Attribute> getAllAttributes() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<AttributeType, Collection<Attribute>> getAllAttributesByType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeValue getAttribute(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AttributeValue getAttribute(String name, AttributeValue defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getAttributeAsBool(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getAttribute(String name, boolean defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getAttributeAsDouble(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getAttribute(String name, double defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getAttributeAsFloat(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getAttribute(String name, float defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttributeAsInt(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttribute(String name, int defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getAttributeAsLong(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getAttribute(String name, long defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAttributeAsString(String name) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAttribute(String name, String defaultValue) {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public UUID getAttributeAsUUID(String name)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public UUID getAttribute(String name, UUID defaultValue) {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public URL getAttributeAsURL(String name)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public URL getAttribute(String name, URL defaultValue) {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public URI getAttributeAsURI(String name)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public URI getAttribute(String name, URI defaultValue) {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public Date getAttributeAsDate(String name)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

    /** {@inheritDoc}
	 */
	@Override
	public Date getAttribute(String name, Date defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttributeCount() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute getAttributeObject(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAttribute(String name) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFlags() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasFlag(int flagIndex) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFlag(int flag) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void switchFlag(int flagIndex) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unsetFlag(int flagIndex) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraversableFrom(RoadConnection point) {
		return point==this.con1 || point==this.con2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TrafficDirection getTrafficDirection() {
		return TrafficDirection.DOUBLE_WAY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTrafficDirection(TrafficDirection direction) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1d getNearestPosition(Point2D<?, ?> pos, double lateralDistance) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RoadType getRoadType() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRoadType(RoadType type) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttributeType(String name, AttributeType type) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRoadNumber() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRoadNumber(String number) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, InetAddress value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, InetSocketAddress value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Enum<?> value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attribute setAttribute(String name, Class<?> value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InetAddress getAttribute(String name, InetAddress defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InetAddress getAttribute(String name, InetSocketAddress defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Enum<T>> T getAttribute(String name, T defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enum<?> getAttributeAsEnumeration(String name)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Enum<T>> T getAttributeAsEnumeration(String name,
			Class<T> type) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getAttribute(String name, Class<?> defaultValue) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getAttributeAsJavaClass(String name)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InetAddress getAttributeAsInetAddress(String name)
			throws AttributeException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEventFirable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEventFirable(boolean isFirable) {
		//
	}

	@Override
	public void setAttributes(Map<String, Object> content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttributes(AttributeProvider content) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAttributes(Map<String, Object> content) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addAttributes(AttributeProvider content) throws AttributeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void toMap(Map<String, Object> mapToFill) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void toPath2D(Path2d path, double startPosition, double endPosition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add("label", this.label); //$NON-NLS-1$
	}

}
