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

package org.arakhne.afc.gis.bus.io.xml;

import static org.arakhne.afc.attrs.xml.XMLAttributeUtil.ATTR_GEOID;
import static org.arakhne.afc.attrs.xml.XMLAttributeUtil.ATTR_TYPE;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.ATTR_X;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.ATTR_Y;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.readGISElementAttributes;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.writeGISElementAttributes;
import static org.arakhne.afc.gis.road.io.XMLRoadUtil.NODE_ROAD;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.ATTR_COLOR;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.ATTR_ID;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.ATTR_NAME;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getAttributeColorWithDefault;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getAttributeDoubleWithDefault;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getAttributeEnumWithDefault;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getAttributeUUID;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getAttributeUUIDWithDefault;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getAttributeValueWithDefault;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getElementsFromPath;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getNodeFromPath;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getNodesFromPath;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.toColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.arakhne.afc.gis.bus.network.BusHub;
import org.arakhne.afc.gis.bus.network.BusItinerary;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt.BusItineraryHaltType;
import org.arakhne.afc.gis.bus.network.BusLine;
import org.arakhne.afc.gis.bus.network.BusNetwork;
import org.arakhne.afc.gis.bus.network.BusStop;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.inputoutput.xml.XMLBuilder;
import org.arakhne.afc.inputoutput.xml.XMLResources;
import org.arakhne.afc.inputoutput.xml.XMLUtil;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** This class provides tools to create an XML representation of a bus network
 * or to create a bus network from an XML representation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public final class XMLBusNetworkUtil {

	/** <code>&lt;halts /&gt;</code>. */
	public static final String NODE_BUSHALTS = "halts"; //$NON-NLS-1$

	/** <code>&lt;halt /&gt;</code>. */
	public static final String NODE_BUSHALT = "halt"; //$NON-NLS-1$

	/** <code>&lt;hubs /&gt;</code>. */
	public static final String NODE_BUSHUBS = "hubs"; //$NON-NLS-1$

	/** <code>&lt;hub /&gt;</code>. */
	public static final String NODE_BUSHUB = "hub"; //$NON-NLS-1$

	/** <code>&lt;itineraries /&gt;</code>. */
	public static final String NODE_BUSITINERARIES = "itineraries"; //$NON-NLS-1$

	/** <code>&lt;itinerary /&gt;</code>. */
	public static final String NODE_BUSITINERARY = "itinerary"; //$NON-NLS-1$

	/** <code>&lt;lines /&gt;</code>. */
	public static final String NODE_BUSLINES = "lines"; //$NON-NLS-1$

	/** <code>&lt;line /&gt;</code>. */
	public static final String NODE_BUSLINE = "line"; //$NON-NLS-1$

	/** <code>&lt;busNetwork /&gt;</code>. */
	public static final String NODE_BUSNETWORK = "busNetwork"; //$NON-NLS-1$

	/** <code>&lt;stops /&gt;</code>. */
	public static final String NODE_BUSSTOPS = "stops"; //$NON-NLS-1$

	/** <code>&lt;stop /&gt;</code>. */
	public static final String NODE_BUSSTOP = "stop"; //$NON-NLS-1$

	/** <code>&lt;roads /&gt;</code>. */
	public static final String NODE_ROADS = "roads"; //$NON-NLS-1$

	/** <code>&lt;roadId /&gt;</code>. */
	public static final String ATTR_ROADID = "roadId"; //$NON-NLS-1$

	/** <code>&lt;stopId /&gt;</code>. */
	private static final String ATTR_STOPID = "stopId"; //$NON-NLS-1$

	private XMLBusNetworkUtil() {
		//
	}

	/** Create and reply an XML representation of the given bus network.
	 *
	 * @param busNetwork is the bus network to translate into XML.
	 * @param builder is the tool that permits to create XML elements.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the XML representation of the given bus network.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("checkstyle:npathcomplexity")
	public static Node writeBusNetwork(BusNetwork busNetwork, XMLBuilder builder, XMLResources resources) throws IOException {
		final Element element = builder.createElement(NODE_BUSNETWORK);

		writeGISElementAttributes(element, busNetwork, builder, resources);
		final Integer color = busNetwork.getRawColor();
		if (color != null) {
			element.setAttribute(ATTR_COLOR, toColor(color));
		}

		final Element stopsNode = builder.createElement(NODE_BUSSTOPS);
		for (final BusStop stop : busNetwork.busStops()) {
			final Node stopNode = writeBusStop(stop, builder, resources);
			if (stopNode != null) {
				stopsNode.appendChild(stopNode);
			}
		}
		if (stopsNode.getChildNodes().getLength() > 0) {
			element.appendChild(stopsNode);
		}

		final Element hubsNode = builder.createElement(NODE_BUSHUBS);
		for (final BusHub hub : busNetwork.busHubs()) {
			final Node hubNode = writeBusHub(hub, builder, resources);
			if (hubNode != null) {
				hubsNode.appendChild(hubNode);
			}
		}
		if (hubsNode.getChildNodes().getLength() > 0) {
			element.appendChild(hubsNode);
		}

		final Element linesNode = builder.createElement(NODE_BUSLINES);
		for (final BusLine line : busNetwork.busLines()) {
			final Node lineNode = writeBusLine(line, builder, resources);
			if (lineNode != null) {
				linesNode.appendChild(lineNode);
			}
		}
		if (linesNode.getChildNodes().getLength() > 0) {
			element.appendChild(linesNode);
		}

		return element;
	}

	private static Node writeBusStop(BusStop stop, XMLBuilder builder, XMLResources resources) throws IOException {
		final Element stopNode = builder.createElement(NODE_BUSSTOP);

		writeGISElementAttributes(stopNode, stop, builder, resources);
		final Integer color = stop.getRawColor();
		if (color != null) {
			stopNode.setAttribute(ATTR_COLOR, toColor(color));
		}

		final Point2d p = stop.getPosition2D();
		if (p != null) {
			stopNode.setAttribute(ATTR_X, Double.toString(p.getX()));
			stopNode.setAttribute(ATTR_Y, Double.toString(p.getY()));
		}
		return stopNode;
	}

	private static Node writeBusHub(BusHub hub, XMLBuilder builder, XMLResources resources) throws IOException {
		final Element hubNode = builder.createElement(NODE_BUSHUB);

		writeGISElementAttributes(hubNode, hub, builder, resources);

		final Integer color = hub.getRawColor();
		if (color != null) {
			hubNode.setAttribute(ATTR_COLOR, toColor(color));
		}

		for (final BusStop stop : hub.busStops()) {
			final Element stopNode = builder.createElement(NODE_BUSSTOP);
			stopNode.setAttribute(ATTR_ID, stop.getUUID().toString());
			hubNode.appendChild(stopNode);
		}
		return hubNode;
	}

	private static Node writeBusLine(BusLine line, XMLBuilder builder, XMLResources resources) throws IOException {
		final Element lineNode = builder.createElement(NODE_BUSLINE);

		writeGISElementAttributes(lineNode, line, builder, resources);
		final Integer color = line.getRawColor();
		if (color != null) {
			lineNode.setAttribute(ATTR_COLOR, toColor(color));
		}

		final Element itinerariesNode = builder.createElement(NODE_BUSITINERARIES);
		for (final BusItinerary itinerary : line.busItineraries()) {
			final Node itineraryNode = writeBusItinerary(itinerary, builder, resources);
			if (itineraryNode != null) {
				itinerariesNode.appendChild(itineraryNode);
			}
		}
		if (itinerariesNode.getChildNodes().getLength() > 0) {
			lineNode.appendChild(itinerariesNode);
		}
		return lineNode;
	}

	private static Node writeBusItinerary(BusItinerary itinerary, XMLBuilder builder, XMLResources resources) throws IOException {
		final Element itineraryNode = builder.createElement(NODE_BUSITINERARY);

		writeGISElementAttributes(itineraryNode, itinerary, builder, resources);
		final Integer color = itinerary.getRawColor();
		if (color != null) {
			itineraryNode.setAttribute(ATTR_COLOR, toColor(color));
		}

		final Element roadsNode = builder.createElement(NODE_ROADS);
		for (final RoadSegment road : itinerary.roadSegments()) {
			final Node roadNode = writeItineraryRoad(road, builder);
			if (roadNode != null) {
				roadsNode.appendChild(roadNode);
			}
		}
		if (roadsNode.getChildNodes().getLength() > 0) {
			itineraryNode.appendChild(roadsNode);
		}

		final Element haltsNode = builder.createElement(NODE_BUSHALTS);
		for (final BusItineraryHalt halt : itinerary.busHalts()) {
			final Node haltNode = writeBusItineraryHalt(halt, builder, resources);
			if (haltNode != null) {
				haltsNode.appendChild(haltNode);
			}
		}
		if (haltsNode.getChildNodes().getLength() > 0) {
			itineraryNode.appendChild(haltsNode);
		}

		return itineraryNode;
	}

	private static Node writeBusItineraryHalt(BusItineraryHalt halt, XMLBuilder builder,
			XMLResources resources) throws IOException {
		final Element haltNode = builder.createElement(NODE_BUSHALT);

		writeGISElementAttributes(haltNode, halt, builder, resources);
		final Integer color = halt.getRawColor();
		if (color != null) {
			haltNode.setAttribute(ATTR_COLOR, toColor(color));
		}

		haltNode.setAttribute(ATTR_TYPE, halt.getType().name());
		final BusStop stop = halt.getBusStop();
		if (stop != null) {
			haltNode.setAttribute(ATTR_STOPID, stop.getUUID().toString());
		}
		final RoadSegment road = halt.getRoadSegment();
		if (road != null) {
			haltNode.setAttribute(ATTR_ROADID, road.getUUID().toString());
		}
		return haltNode;
	}

	private static Node writeItineraryRoad(RoadSegment road, XMLBuilder builder) {
		final Element roadNode = builder.createElement(NODE_ROAD);
		roadNode.setAttribute(ATTR_ID, road.getUUID().toString());
		roadNode.setAttribute(ATTR_GEOID, road.getGeoId().toString());
		return roadNode;
	}

	/** Create and reply the bus network which is described by the given XML representation.
	 *
	 * @param xmlNode is the node to explore.
	 * @param roadNetwork is the road network on which the bus network is mapped.
	 * @param pathBuilder is the tool that permits to make absolute paths.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the bus network.
	 * @throws IOException in case of error.
	 */
	public static BusNetwork readBusNetwork(Element xmlNode, RoadNetwork roadNetwork,
			PathBuilder pathBuilder, XMLResources resources) throws IOException {
		final UUID id = getAttributeUUID(xmlNode, NODE_BUSNETWORK, ATTR_ID);

		final BusNetwork busNetwork = new BusNetwork(id, roadNetwork);

		final Node stopsNode = getNodeFromPath(xmlNode, NODE_BUSNETWORK, NODE_BUSSTOPS);
		if (stopsNode != null) {
			for (final Element stopNode : getElementsFromPath(stopsNode, NODE_BUSSTOP)) {
				final BusStop stop = readBusStop(stopNode, pathBuilder, resources);
				if (stop != null) {
					busNetwork.addBusStop(stop);
				}
			}
		}

		final Node hubsNode = getNodeFromPath(xmlNode, NODE_BUSNETWORK, NODE_BUSHUBS);
		if (hubsNode != null) {
			for (final Element hubNode : getElementsFromPath(hubsNode, NODE_BUSHUB)) {
				readBusHub(hubNode, busNetwork, pathBuilder, resources);
			}
		}

		final Node linesNode = getNodeFromPath(xmlNode, NODE_BUSNETWORK, NODE_BUSLINES);
		if (linesNode != null) {
			BusLine line;
			for (final Element lineNode : getElementsFromPath(linesNode, NODE_BUSLINE)) {
				line = readBusLine(lineNode, busNetwork, roadNetwork, pathBuilder, resources);
				if (line != null) {
					busNetwork.addBusLine(line);
				}
			}
		}

		readGISElementAttributes(xmlNode, busNetwork, pathBuilder, resources);
		final Integer color = getAttributeColorWithDefault(xmlNode, null, ATTR_COLOR);
		if (color != null) {
			busNetwork.setColor(color);
		}

		// Force the validity checking to be sure
		// that all the primitives are valid or not
		busNetwork.revalidate();

		return busNetwork;
	}

	private static RoadSegment readItineraryRoad(Element xmlNode, RoadNetwork roadNetwork, Map<UUID, RoadSegment> roadMap) {
		assert roadMap != null;
		final String s = getAttributeValueWithDefault(xmlNode, null, ATTR_GEOID);
		if (s == null || "".equals(s)) { //$NON-NLS-1$
			return null;
		}
		final GeoId gid = GeoId.valueOf(s);
		if (gid == null) {
			return null;
		}
		RoadSegment road = roadNetwork.getRoadSegment(gid);
		if (road != null) {
			final UUID uuid = getAttributeUUIDWithDefault(xmlNode, null, ATTR_ID);
			if (uuid != null) {
				roadMap.put(uuid, road);
			}
		} else {
			//Brutal Find segment
			road = roadNetwork.getRoadSegment(gid);
			final Iterator<RoadSegment> it = roadNetwork.iterator();
			while (it.hasNext()) {
				road = it.next();
				if (road.getGeoId().equals(gid)) {
					return road;
				}
			}
		}
		return road;
	}

	private static BusItineraryHalt readBusItineraryHalt(Element xmlNode, BusItinerary itinerary,
			BusNetwork busNetwork, Map<UUID, RoadSegment> roadMap, PathBuilder pathBuilder,
			XMLResources resources) throws IOException {
		final BusItineraryHaltType type = getAttributeEnumWithDefault(xmlNode, BusItineraryHaltType.class,
				BusItineraryHaltType.STOP_ON_DEMAND, ATTR_TYPE);
		UUID id = getAttributeUUIDWithDefault(xmlNode, null, ATTR_ID);
		if (id == null) {
			throw new IOException("id not found for a bus itinerary"); //$NON-NLS-1$
		}
		final BusItineraryHalt halt = itinerary.addBusHalt(id, type);
		if (halt == null) {
			return null;
		}

		readGISElementAttributes(xmlNode, halt, pathBuilder, resources);
		final Integer color = getAttributeColorWithDefault(xmlNode, null, ATTR_COLOR);
		if (color != null) {
			halt.setColor(color);
		}

		id = getAttributeUUIDWithDefault(xmlNode, null, ATTR_STOPID);
		if (id != null) {
			final BusStop busStop = busNetwork.getBusStop(id);
			if (busStop != null) {
				halt.setBusStop(busStop);
			} else {
				throw new IOException("stop not found for id:" + id); //$NON-NLS-1$
			}
		}

		id = getAttributeUUIDWithDefault(xmlNode, null, ATTR_ROADID);
		if (id != null) {
			final RoadSegment road = roadMap.get(id);
			if (road == null || !itinerary.putHaltOnRoad(halt, road)) {
				throw new IOException("road not in itinerary:" + id); //$NON-NLS-1$
			}
		}

		return halt;
	}

	private static BusItinerary readBusItinerary(Element xmlNode, BusNetwork busNetwork,
			RoadNetwork roadNetwork, PathBuilder pathBuilder, XMLResources resources) throws IOException {
		final UUID id = getAttributeUUIDWithDefault(xmlNode, null, ATTR_ID);
		if (id == null) {
			throw new IOException("id not found for a bus itinerary"); //$NON-NLS-1$
		}
		final BusItinerary itinerary = new BusItinerary(id);

		readGISElementAttributes(xmlNode, itinerary, pathBuilder, resources);
		final Integer color = getAttributeColorWithDefault(xmlNode, null, ATTR_COLOR);
		if (color != null) {
			itinerary.setColor(color);
		}

		final Map<UUID, RoadSegment> roadMap = new TreeMap<>();

		final Node roadsNode = getNodeFromPath(xmlNode, NODE_ROADS);
		if (roadsNode != null) {
			RoadSegment road;
			for (final Element roadNode : getElementsFromPath(roadsNode, NODE_ROAD)) {
				road = readItineraryRoad(roadNode, roadNetwork, roadMap);
				if (road == null) {
					throw new IOException("road segment not found for: " //$NON-NLS-1$
								+ XMLUtil.toString(roadNode));
				}
				itinerary.addRoadSegment(road, false);
			}
		}

		final Node haltsNode = getNodeFromPath(xmlNode, NODE_BUSHALTS);
		if (haltsNode != null) {
			for (final Element haltNode : getElementsFromPath(haltsNode, NODE_BUSHALT)) {
				readBusItineraryHalt(haltNode, itinerary, busNetwork, roadMap, pathBuilder, resources);
			}
		}

		return itinerary;
	}

	private static BusLine readBusLine(Element xmlNode, BusNetwork busNetwork, RoadNetwork roadNetwork,
			PathBuilder pathBuilder, XMLResources resources) throws IOException {
		final UUID id = getAttributeUUIDWithDefault(xmlNode, null, ATTR_ID);
		if (id == null) {
			throw new IOException("id not found for a bus line"); //$NON-NLS-1$
		}
		final BusLine line = new BusLine(id);

		final Node itinerariesNode = getNodeFromPath(xmlNode, NODE_BUSITINERARIES);
		if (itinerariesNode != null) {
			BusItinerary itinerary;
			for (final Element itineraryNode : getElementsFromPath(itinerariesNode, NODE_BUSITINERARY)) {
				itinerary = readBusItinerary(itineraryNode, busNetwork, roadNetwork, pathBuilder, resources);
				if (itinerary != null) {
					line.addBusItinerary(itinerary);
				}
			}
		}

		readGISElementAttributes(xmlNode, line, pathBuilder, resources);
		final Integer color = getAttributeColorWithDefault(xmlNode, null, ATTR_COLOR);
		if (color != null) {
			line.setColor(color);
		}

		return line;
	}

	private static void readBusHub(Element xmlNode, BusNetwork busNetwork, PathBuilder pathBuilder,
			XMLResources resources) throws IOException {
		final List<BusStop> stops = new ArrayList<>();
		for (final Node stopNode : getNodesFromPath(xmlNode, NODE_BUSSTOP)) {
			final UUID id = getAttributeUUIDWithDefault(stopNode, null, ATTR_ID);
			if (id == null) {
				throw new IOException("no UUID for bus stop in bus hub"); //$NON-NLS-1$
			}
			final BusStop stop = busNetwork.getBusStop(id);
			if (stop == null) {
				throw new IOException("bus stop not found: " + id.toString()); //$NON-NLS-1$
			}
			stops.add(stop);
		}

		if (!stops.isEmpty()) {
			final BusStop[] array = new BusStop[stops.size()];
			stops.toArray(array);
			final String name = getAttributeValueWithDefault(xmlNode, null, ATTR_NAME);
			final BusHub hub;
			if (name != null && !"".equals(name)) { //$NON-NLS-1$
				hub = busNetwork.addBusHub(name, array);
			} else {
				hub = busNetwork.addBusHub(array);
			}
			assert hub != null;
			final UUID id = getAttributeUUIDWithDefault(xmlNode, null, ATTR_ID);
			if (id != null) {
				hub.setUUID(id);
			}

			readGISElementAttributes(xmlNode, hub, pathBuilder, resources);
			final Integer color = getAttributeColorWithDefault(xmlNode, null, ATTR_COLOR);
			if (color != null) {
				hub.setColor(color);
			}
		}
	}

	private static BusStop readBusStop(Element xmlNode, PathBuilder pathBuilder, XMLResources resources) throws IOException {
		final UUID id = getAttributeUUID(xmlNode, ATTR_ID);

		final String name = getAttributeValueWithDefault(xmlNode, null, ATTR_NAME);
		final BusStop stop = new BusStop(id, name);
		final double x = getAttributeDoubleWithDefault(xmlNode, Double.NaN, ATTR_X);
		if (!Double.isNaN(x)) {
			final double y = getAttributeDoubleWithDefault(xmlNode, Double.NaN, ATTR_Y);
			if (!Double.isNaN(y)) {
				stop.setPosition(new GeoLocationPoint(x, y));
			}
		}

		readGISElementAttributes(xmlNode, stop, pathBuilder, resources);
		final Integer color = getAttributeColorWithDefault(xmlNode, null, ATTR_COLOR);
		if (color != null) {
			stop.setColor(color);
		}

		return stop;
	}

}
