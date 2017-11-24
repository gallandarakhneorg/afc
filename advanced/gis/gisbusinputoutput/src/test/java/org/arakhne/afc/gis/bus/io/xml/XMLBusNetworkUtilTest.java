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

package org.arakhne.afc.gis.bus.io.xml;

import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.arakhne.afc.gis.bus.network.BusItinerary;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt.BusItineraryHaltType;
import org.arakhne.afc.gis.bus.network.BusLine;
import org.arakhne.afc.gis.bus.network.BusNetwork;
import org.arakhne.afc.gis.bus.network.BusStop;
import org.arakhne.afc.gis.io.shape.GISShapeFileReader;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.inputoutput.path.SimplePathBuilder;
import org.arakhne.afc.inputoutput.xml.XMLBuilder;
import org.arakhne.afc.inputoutput.xml.XMLResources;
import org.arakhne.afc.inputoutput.xml.XMLUtil;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("all")
public class XMLBusNetworkUtilTest extends AbstractTestCase {

	private static final URL shpUrl = Resources.getResource(XMLBusNetworkUtilTest.class, "test.shp"); //$NON-NLS-1$

	private StandardRoadNetwork network;

	@Before
	public void setUp() throws Exception {
		try (GISShapeFileReader reader = new GISShapeFileReader(shpUrl, RoadPolyline.class)) {
			this.network = new StandardRoadNetwork(reader.getBoundsFromHeader().toRectangle2d());
			RoadPolyline line = (RoadPolyline) reader.read();
			while (line != null) {
				this.network.addRoadPolyline(line);
				line = (RoadPolyline) reader.read();
			}
		}
	}

	@After
	public void tearDown() {
		this.network = null;
	}
	
	@Test
	public void writeBusNetworkBusNetworkXMLBuilderXMLResources() throws Exception {
		BusNetwork busNetwork = new BusNetwork(this.network);
		BusStop stop1 = new BusStop("Stop1"); //$NON-NLS-1$
		stop1.setPosition(new GeoLocationPoint(935665.0, 2311306.9));
		busNetwork.addBusStop(stop1);
		BusStop stop2 = new BusStop("Stop2"); //$NON-NLS-1$
		stop2.setPosition(new GeoLocationPoint(935665.0, 2311306.9));
		busNetwork.addBusStop(stop2);
		BusLine line1 = new BusLine("Line1"); //$NON-NLS-1$
		busNetwork.addBusLine(line1);
		BusItinerary itinerary1 = new BusItinerary("Itinerary1"); //$NON-NLS-1$
		line1.addBusItinerary(itinerary1);
		RoadSegment road1 = this.network.getRoadSegment(GeoId.valueOf("ccf6f6d64c911eec7e16dd51c3385a28#935646;2311307;935665;2311320")); //$NON-NLS-1$
		itinerary1.addRoadSegment(road1);
		RoadSegment road2 = this.network.getRoadSegment(GeoId.valueOf("67b453c1f5f2a55c3e91aa0468fc861a#935579;2311319;935647;2311395")); //$NON-NLS-1$
		itinerary1.addRoadSegment(road2);
		RoadSegment road3 = this.network.getRoadSegment(GeoId.valueOf("42bc6f398c0552f66138fe953a9a52d8#935561;2311395;935580;2311432")); //$NON-NLS-1$
		itinerary1.addRoadSegment(road3);
		BusItineraryHalt halt1 = itinerary1.addBusHalt("Halt1", BusItineraryHaltType.SYSTEMATIC_STOP); //$NON-NLS-1$
		halt1.setBusStop(stop1);
		BusItineraryHalt halt2 = itinerary1.addBusHalt("Halt1", BusItineraryHaltType.SYSTEMATIC_STOP); //$NON-NLS-1$
		halt2.setBusStop(stop2);
		busNetwork.revalidate();
		
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		XMLBuilder xmlBuilder = new XMLBuilder() {
			@Override
			public Document getDocument() throws DOMException {
				return doc;
			}
		};
		Element root = xmlBuilder.createElement("root"); //$NON-NLS-1$
		doc.appendChild(root);

		Node result = XMLBusNetworkUtil.writeBusNetwork(busNetwork, xmlBuilder, res);
		root.appendChild(result);

		String actual;
		try (StringWriter sw = new StringWriter()) {
			XMLUtil.writeXML(doc, sw);
			sw.close();
			actual = sw.toString();
		}
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //$NON-NLS-1$
				+ "<root><busNetwork geoId=\"" + busNetwork.getGeoId().toString() //$NON-NLS-1$
				+ "\" id=\"" + busNetwork.getUUID().toString() //$NON-NLS-1$
				+ "\" name=\"\"><stops><stop geoId=\"6ebac3fcff724d6ebe66e7f67f77a2e9#935665;2311307;935665;2311307\" " //$NON-NLS-1$
				+ "id=\"" + stop1.getUUID().toString() + "\" name=\"Stop1\" x=\"935665.0\" y=\"2311307.0\"/>" //$NON-NLS-1$ //$NON-NLS-2$
				+ "<stop geoId=\"6ebac3fcff724d6ebe66e7f67f77a2e9#935665;2311307;935665;2311307\" " //$NON-NLS-1$
				+ "id=\"" + stop2.getUUID().toString() + "\" name=\"Stop2\" x=\"935665.0\" " //$NON-NLS-1$ //$NON-NLS-2$
				+ "y=\"2311307.0\"/></stops><lines><line geoId=\"" + line1.getGeoId().toString() //$NON-NLS-1$
				+ "\" id=\"" + line1.getUUID().toString() + "\" name=\"Line1\"><itineraries>" //$NON-NLS-1$ //$NON-NLS-2$
				+ "<itinerary geoId=\"fbee950d00913708b1d6bece061b87c7#935558;2311303;935669;2311435\" " //$NON-NLS-1$
				+ "id=\"" + itinerary1.getUUID().toString() + "\" name=\"Itinerary1\"><roads>" //$NON-NLS-1$ //$NON-NLS-2$
				+ "<road geoId=\"ccf6f6d64c911eec7e16dd51c3385a28#935646;2311307;935665;2311320\" " //$NON-NLS-1$
				+ "id=\"" + road1.getUUID().toString() + "\"/><road " //$NON-NLS-1$ //$NON-NLS-2$
				+ "geoId=\"67b453c1f5f2a55c3e91aa0468fc861a#935579;2311319;935647;2311395\" " //$NON-NLS-1$
				+ "id=\"" + road2.getUUID().toString() + "\"/><road " //$NON-NLS-1$ //$NON-NLS-2$
				+ "geoId=\"42bc6f398c0552f66138fe953a9a52d8#935561;2311395;935580;2311432\" " //$NON-NLS-1$
				+ "id=\"" + road3.getUUID().toString() + "\"/></roads><halts><halt " //$NON-NLS-1$ //$NON-NLS-2$
				+ "geoId=\"" + halt1.getGeoId().toString() + "\" id=\"" + halt1.getUUID().toString() + "\" " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ "name=\"Halt1\" stopId=\"" + stop1.getUUID().toString() + "\" " //$NON-NLS-1$ //$NON-NLS-2$
				+ "type=\"SYSTEMATIC_STOP\"/><halt geoId=\"" + halt2.getGeoId().toString() + "\" " //$NON-NLS-1$ //$NON-NLS-2$
				+ "id=\"" + halt2.getUUID().toString() + "\" name=\"Halt1\" " //$NON-NLS-1$ //$NON-NLS-2$
				+ "stopId=\"" + stop2.getUUID().toString() + "\" type=\"SYSTEMATIC_STOP\"/>" //$NON-NLS-1$ //$NON-NLS-2$
				+ "</halts></itinerary></itineraries></line></lines></busNetwork></root>", actual); //$NON-NLS-1$
	}

}
