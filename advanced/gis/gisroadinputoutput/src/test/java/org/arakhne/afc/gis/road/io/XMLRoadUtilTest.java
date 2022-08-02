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

package org.arakhne.afc.gis.road.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.inputoutput.path.SimplePathBuilder;
import org.arakhne.afc.inputoutput.xml.XMLBuilder;
import org.arakhne.afc.inputoutput.xml.XMLResources;
import org.arakhne.afc.inputoutput.xml.XMLUtil;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.ColorNames;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("all")
public class XMLRoadUtilTest extends AbstractTestCase {

	@Test
	public void readRoadPolylineElementPathBuilderXMLResources() throws Exception {
		URL xmlFile = Resources.getResource(XMLRoadUtilTest.class, "polyline.xml"); //$NON-NLS-1$
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc;
		try (InputStream is = xmlFile.openStream()) {
			doc = builder.parse(is);
		}
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);

		RoadPolyline polyline = XMLRoadUtil.readRoadPolyline(doc.getDocumentElement(), pathBuilder, res);

		Point2d p;
		Iterator<Point2d> pts = polyline.pointIterator();
		p = pts.next();
		assertEpsilonEquals(1., p.getX());
		assertEpsilonEquals(2., p.getY());
		p = pts.next();
		assertEpsilonEquals(-3., p.getX());
		assertEpsilonEquals(4., p.getY());
		p = pts.next();
		assertEpsilonEquals(5., p.getX());
		assertEpsilonEquals(-6., p.getY());
		assertFalse(pts.hasNext());

		assertEquals("name1", polyline.getName()); //$NON-NLS-1$
		assertTrue(polyline.isWidePolyline());
		assertEquals(ColorNames.getColorFromName("orange").intValue(), polyline.getColor()); //$NON-NLS-1$
		assertEquals("Text", polyline.getAttribute("attr1", (String) null)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void readRoadSegmentElementPathBuilderXMLResources() throws Exception {
		URL xmlFile = Resources.getResource(XMLRoadUtilTest.class, "polyline.xml"); //$NON-NLS-1$
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc;
		try (InputStream is = xmlFile.openStream()) {
			doc = builder.parse(is);
		}
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);

		RoadSegment polyline = XMLRoadUtil.readRoadSegment(doc.getDocumentElement(), pathBuilder, res);

		Point2d p;
		Iterator<Point2d> pts = polyline.pointIterator();
		p = pts.next();
		assertEpsilonEquals(1., p.getX());
		assertEpsilonEquals(2., p.getY());
		p = pts.next();
		assertEpsilonEquals(-3., p.getX());
		assertEpsilonEquals(4., p.getY());
		p = pts.next();
		assertEpsilonEquals(5., p.getX());
		assertEpsilonEquals(-6., p.getY());
		assertFalse(pts.hasNext());

		assertEquals("name1", polyline.getName()); //$NON-NLS-1$
		assertEquals(ColorNames.getColorFromName("orange").intValue(), polyline.getAttribute("color", 0xFFFFFF)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Text", polyline.getAttribute("attr1", (String) null)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void readRoadNetworkElementPathBuilderXMLResources() throws Exception {
		URL xmlFile = Resources.getResource(XMLRoadUtilTest.class, "network.xml"); //$NON-NLS-1$
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc;
		try (InputStream is = xmlFile.openStream()) {
			doc = builder.parse(is);
		}
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);

		Point2d p;
		RoadSegment polyline;

		StandardRoadNetwork network = XMLRoadUtil.readRoadNetwork(doc.getDocumentElement(), pathBuilder, res);

		Collection<? extends RoadSegment> list = network.getRoadSegments();
		assertEquals(2, list.size());
		Iterator<? extends RoadSegment> segmentIterator = list.iterator();
		polyline = segmentIterator.next();

		Iterator<Point2d> pts = polyline.pointIterator();
		p = pts.next();
		assertEpsilonEquals(1., p.getX());
		assertEpsilonEquals(2., p.getY());
		p = pts.next();
		assertEpsilonEquals(-3., p.getX());
		assertEpsilonEquals(4., p.getY());
		p = pts.next();
		assertEpsilonEquals(5., p.getX());
		assertEpsilonEquals(-6., p.getY());
		assertFalse(pts.hasNext());

		assertEquals("name1", polyline.getName()); //$NON-NLS-1$
		assertEquals(ColorNames.getColorFromName("orange").intValue(), polyline.getAttribute("color", 0xFFFFFF)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Text", polyline.getAttribute("attr1", (String) null)); //$NON-NLS-1$ //$NON-NLS-2$

		polyline = segmentIterator.next();

		pts = polyline.pointIterator();
		p = pts.next();
		assertEpsilonEquals(1., p.getX());
		assertEpsilonEquals(2., p.getY());
		p = pts.next();
		assertEpsilonEquals(-1., p.getX());
		assertEpsilonEquals(-2., p.getY());
		p = pts.next();
		assertEpsilonEquals(1., p.getX());
		assertEpsilonEquals(-2., p.getY());
		assertFalse(pts.hasNext());

		assertEquals("name2", polyline.getName()); //$NON-NLS-1$
		assertEquals(ColorNames.getColorFromName("red").intValue(), polyline.getAttribute("color", 0xFFFFFF)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Text2", polyline.getAttribute("attr2", (String) null)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void readRoadNetworkElementRoadNetworkPathBuilderXMLResources() throws Exception {
		URL xmlFile = Resources.getResource(XMLRoadUtilTest.class, "network.xml"); //$NON-NLS-1$
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc;
		try (InputStream is = xmlFile.openStream()) {
			doc = builder.parse(is);
		}
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);

		Point2d p;
		RoadSegment polyline;

		StandardRoadNetwork network = new StandardRoadNetwork(new Rectangle2d(-3, -6, 8, 10));
		XMLRoadUtil.readRoadNetwork(doc.getDocumentElement(), network, pathBuilder, res);

		Collection<? extends RoadSegment> list = network.getRoadSegments();
		assertEquals(2, list.size());
		Iterator<? extends RoadSegment> segmentIterator = list.iterator();
		polyline = segmentIterator.next();

		Iterator<Point2d> pts = polyline.pointIterator();
		p = pts.next();
		assertEpsilonEquals(1., p.getX());
		assertEpsilonEquals(2., p.getY());
		p = pts.next();
		assertEpsilonEquals(-3., p.getX());
		assertEpsilonEquals(4., p.getY());
		p = pts.next();
		assertEpsilonEquals(5., p.getX());
		assertEpsilonEquals(-6., p.getY());
		assertFalse(pts.hasNext());

		assertEquals("name1", polyline.getName()); //$NON-NLS-1$
		assertEquals(ColorNames.getColorFromName("orange").intValue(), polyline.getAttribute("color", 0xFFFFFF)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Text", polyline.getAttribute("attr1", (String) null)); //$NON-NLS-1$ //$NON-NLS-2$

		polyline = segmentIterator.next();

		pts = polyline.pointIterator();
		p = pts.next();
		assertEpsilonEquals(1., p.getX());
		assertEpsilonEquals(2., p.getY());
		p = pts.next();
		assertEpsilonEquals(-1., p.getX());
		assertEpsilonEquals(-2., p.getY());
		p = pts.next();
		assertEpsilonEquals(1., p.getX());
		assertEpsilonEquals(-2., p.getY());
		assertFalse(pts.hasNext());

		assertEquals("name2", polyline.getName()); //$NON-NLS-1$
		assertEquals(ColorNames.getColorFromName("red").intValue(), polyline.getAttribute("color", 0xFFFFFF)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Text2", polyline.getAttribute("attr2", (String) null)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void writeRoadPolylineRoadPolylineXMLBuilderXMLResources() throws Exception {
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
		RoadPolyline road = new RoadPolyline();
		road.addGroup(1, 2);
		road.addPoint(3, 4);
		road.addPoint(5, 6);
		road.addGroup(-1, 2);
		road.addPoint(-3, 4);
		road.addPoint(-5, -6);
		road.setName("myroad"); //$NON-NLS-1$
		road.setColor(0xFFFF0011);
		road.setAttribute("myattr", "thevalue"); //$NON-NLS-1$ //$NON-NLS-2$

		Element element = XMLRoadUtil.writeRoadPolyline(road, xmlBuilder, res);

		doc.appendChild(element);
		String actual;
		try (StringWriter sw = new StringWriter()) {
			XMLUtil.writeXML(doc, sw);
			sw.close();
			actual = sw.toString();
		}
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //$NON-NLS-1$
				+ "<road geoId=\"0ee8691041f80b41e4957e89239b4ff7#-5;-6;5;6\" id=\"" //$NON-NLS-1$
				+ road.getUUID().toString() + "\" name=\"myroad\" wide=\"true\" width=\"6.6\">" //$NON-NLS-1$
				+ "<points><point x=\"1.0\" y=\"2.0\"/><point x=\"3.0\" y=\"4.0\"/>" //$NON-NLS-1$
				+ "<point x=\"5.0\" y=\"6.0\"/><point x=\"-1.0\" y=\"2.0\"/>" //$NON-NLS-1$
				+ "<point x=\"-3.0\" y=\"4.0\"/><point x=\"-5.0\" y=\"-6.0\"/>" //$NON-NLS-1$
				+ "</points><groups><group firstPointIndex=\"0\" lastPointIndex=\"2\"/>" //$NON-NLS-1$
				+ "<group firstPointIndex=\"3\" lastPointIndex=\"5\"/></groups>" //$NON-NLS-1$
				+ "<attributes><attribute name=\"myattr\" type=\"STRING\" value=\"thevalue\"/>" //$NON-NLS-1$
				+ "<attribute name=\"nom_rue_d\" type=\"STRING\" value=\"myroad\"/></attributes></road>", actual); //$NON-NLS-1$
	}

	@Test
	public void writeRoadSegmentRoadSegmentXMLBuilderXMLResources() throws Exception {
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
		RoadPolyline road = new RoadPolyline();
		road.addGroup(1, 2);
		road.addPoint(3, 4);
		road.addPoint(5, 6);
		road.addGroup(-1, 2);
		road.addPoint(-3, 4);
		road.addPoint(-5, -6);
		road.setName("myroad"); //$NON-NLS-1$
		road.setColor(0xFFFF0011);
		road.setAttribute("myattr", "thevalue"); //$NON-NLS-1$ //$NON-NLS-2$

		Element element = XMLRoadUtil.writeRoadSegment(road, xmlBuilder, res);

		doc.appendChild(element);
		String actual;
		try (StringWriter sw = new StringWriter()) {
			XMLUtil.writeXML(doc, sw);
			sw.close();
			actual = sw.toString();
		}
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //$NON-NLS-1$
				+ "<road geoId=\"0ee8691041f80b41e4957e89239b4ff7#-5;-6;5;6\" id=\"" //$NON-NLS-1$
				+ road.getUUID().toString() + "\" name=\"myroad\" wide=\"true\" width=\"6.6\">" //$NON-NLS-1$
				+ "<points><point x=\"1.0\" y=\"2.0\"/><point x=\"3.0\" y=\"4.0\"/>" //$NON-NLS-1$
				+ "<point x=\"5.0\" y=\"6.0\"/><point x=\"-1.0\" y=\"2.0\"/>" //$NON-NLS-1$
				+ "<point x=\"-3.0\" y=\"4.0\"/><point x=\"-5.0\" y=\"-6.0\"/>" //$NON-NLS-1$
				+ "</points><groups><group firstPointIndex=\"0\" lastPointIndex=\"2\"/>" //$NON-NLS-1$
				+ "<group firstPointIndex=\"3\" lastPointIndex=\"5\"/></groups>" //$NON-NLS-1$
				+ "<attributes><attribute name=\"myattr\" type=\"STRING\" value=\"thevalue\"/>" //$NON-NLS-1$
				+ "<attribute name=\"nom_rue_d\" type=\"STRING\" value=\"myroad\"/></attributes></road>", actual); //$NON-NLS-1$
	}

	@Test
	public void writeRoadNetworkElementRoadNetworkURLMapMetricProjectionURLXMLBuilderPathBuilderXMLResources() throws Exception {
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
		Element node = xmlBuilder.createElement("network"); //$NON-NLS-1$
		doc.appendChild(node);
		
		StandardRoadNetwork network = new StandardRoadNetwork(new Rectangle2d(-5, -6, 10, 12));
		
		RoadPolyline road = new RoadPolyline();
		road.addGroup(1, 2);
		road.addPoint(3, 4);
		road.addPoint(5, 6);
		road.addGroup(-1, 2);
		road.addPoint(-3, 4);
		road.addPoint(-5, -6);
		road.setName("myroad"); //$NON-NLS-1$
		road.setColor(0xFFFF0011);
		road.setAttribute("myattr", "thevalue"); //$NON-NLS-1$ //$NON-NLS-2$
		
		network.addRoadPolyline(road);

		XMLRoadUtil.writeRoadNetwork(node, network, null, null, null, xmlBuilder, pathBuilder, res);

		String actual;
		try (StringWriter sw = new StringWriter()) {
			XMLUtil.writeXML(doc, sw);
			sw.close();
			actual = sw.toString();
		}
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //$NON-NLS-1$
				+ "<network elementGeometryProjection=\"FRANCE_LAMBERT_2_EXTENDED\" height=\"18.6\" width=\"16.6\" x=\"-8.3\" y=\"-9.3\"><elements>" //$NON-NLS-1$
				+ "<road geoId=\"0ee8691041f80b41e4957e89239b4ff7#-5;-6;5;6\" id=\"" //$NON-NLS-1$
				+ road.getUUID().toString() + "\" name=\"myroad\" wide=\"true\" width=\"6.6\">" //$NON-NLS-1$
				+ "<points><point x=\"1.0\" y=\"2.0\"/><point x=\"3.0\" y=\"4.0\"/>" //$NON-NLS-1$
				+ "<point x=\"5.0\" y=\"6.0\"/><point x=\"-1.0\" y=\"2.0\"/>" //$NON-NLS-1$
				+ "<point x=\"-3.0\" y=\"4.0\"/><point x=\"-5.0\" y=\"-6.0\"/>" //$NON-NLS-1$
				+ "</points><groups><group firstPointIndex=\"0\" lastPointIndex=\"2\"/>" //$NON-NLS-1$
				+ "<group firstPointIndex=\"3\" lastPointIndex=\"5\"/></groups>" //$NON-NLS-1$
				+ "<attributes><attribute name=\"myattr\" type=\"STRING\" value=\"thevalue\"/>" //$NON-NLS-1$
				+ "<attribute name=\"nom_rue_d\" type=\"STRING\" value=\"myroad\"/></attributes></road>" //$NON-NLS-1$
				+ "</elements></network>", actual); //$NON-NLS-1$
	}

}
