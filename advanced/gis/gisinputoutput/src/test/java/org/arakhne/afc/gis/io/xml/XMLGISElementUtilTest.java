/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.gis.io.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.google.common.io.Resources;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.arakhne.afc.gis.coordinate.MapMetricProjection;
import org.arakhne.afc.gis.mapelement.MapCircle;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.maplayer.ArrayMapElementLayer;
import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.inputoutput.path.SimplePathBuilder;
import org.arakhne.afc.inputoutput.xml.XMLBuilder;
import org.arakhne.afc.inputoutput.xml.XMLResources;
import org.arakhne.afc.inputoutput.xml.XMLUtil;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.OperatingSystem;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class XMLGISElementUtilTest extends AbstractTestCase {

	private final static URL url = Resources.getResource(XMLGISElementUtilTest.class, "test.xml"); //$NON-NLS-1$

	private final static URL url2 = Resources.getResource(XMLGISElementUtilTest.class, "test2.xml"); //$NON-NLS-1$

	private Document document;
	
	@Before
	public void setUp() throws Exception {
		assertNotNull("testing resource not found", url); //$NON-NLS-1$
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		try (InputStream is = url.openStream()) {
			this.document = builder.parse(is);
		}
	}

	@After
	public void tearDown() {
		this.document = null;
	}

	@Test
	public void getDefaultMapElementNodeNodeClass() {
		assertEquals("polyline", XMLGISElementUtil.getDefaultMapElementNodeName(MapPolyline.class)); //$NON-NLS-1$
		assertEquals("polygon", XMLGISElementUtil.getDefaultMapElementNodeName(MapPolygon.class)); //$NON-NLS-1$
		assertEquals("multipoint", XMLGISElementUtil.getDefaultMapElementNodeName(MapMultiPoint.class)); //$NON-NLS-1$
		assertEquals("point", XMLGISElementUtil.getDefaultMapElementNodeName(MapPoint.class)); //$NON-NLS-1$
		assertEquals("circle", XMLGISElementUtil.getDefaultMapElementNodeName(MapCircle.class)); //$NON-NLS-1$
	}

	@Test
	public void readGISElementAttributesElementGISElementPathBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		MapPoint point = new MapPoint(3, 4);
		XMLGISElementUtil.readGISElementAttributes(this.document.getDocumentElement(), point, pathBuilder, res);
		assertEquals("name1", point.getName()); //$NON-NLS-1$
		assertEquals("Text", point.getAttribute("attr1", (String) null)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void writeGISElementAttributesElementGISElementXMLBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		MapPoint point = new MapPoint(3, 4);
		point.setName("name1"); //$NON-NLS-1$
		point.setAttribute("attr1", "Text"); //$NON-NLS-1$ //$NON-NLS-2$

		final String source = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><root><a><b id=\"v\"/></a></root>"; //$NON-NLS-1$
		final Document doc;
		try (ByteArrayInputStream reader = new ByteArrayInputStream(source.getBytes())) {
			doc = XMLUtil.readXML(reader);
		}

		XMLBuilder xmlBuilder = new XMLBuilder() {
			@Override
			public Document getDocument() throws DOMException {
				return doc;
			}
		};

		XMLGISElementUtil.writeGISElementAttributes(doc.getDocumentElement(), point, xmlBuilder, res);
		
		String actual;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			XMLUtil.writeXML(doc, baos);
			baos.flush();
			actual = new String(baos.toByteArray());
		}
		final String id = point.getUUID().toString();
		final String expected = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" //$NON-NLS-1$
				+ "<root geoId=\"39e072ec9ca6460ee679103fd10d53dc#3;4;3;4\" " //$NON-NLS-1$
				+ "id=\"" + id + "\" name=\"name1\">" //$NON-NLS-1$ //$NON-NLS-2$
				+ "<a><b id=\"v\"/></a>" //$NON-NLS-1$
				+ "<attributes><attribute name=\"attr1\" type=\"STRING\" value=\"Text\"/></attributes>" //$NON-NLS-1$
				+ "</root>"; //$NON-NLS-1$
		assertEquals(expected, actual);
	}

	@Test
	public void readMapElementElementStringPathBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		MapElement elt = XMLGISElementUtil.readMapElement(this.document.getDocumentElement(), "point", pathBuilder, res); //$NON-NLS-1$
		assertNotNull(elt);
		MapPoint pts = (MapPoint) elt;
		assertEpsilonEquals(3., pts.getX());
		assertEpsilonEquals(4., pts.getY());
		assertEquals("name1", elt.getName()); //$NON-NLS-1$
		assertEquals("Text", elt.getAttribute("attr1", (String) null)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void readMapElementElementStringClassPathBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		MapElement elt = XMLGISElementUtil.readMapElement(this.document.getDocumentElement(), "point", MapPoint.class, pathBuilder, res); //$NON-NLS-1$
		assertNotNull(elt);
		MapPoint pts = (MapPoint) elt;
		assertEpsilonEquals(3., pts.getX());
		assertEpsilonEquals(4., pts.getY());
		assertEquals("name1", elt.getName()); //$NON-NLS-1$
		assertEquals("Text", elt.getAttribute("attr1", (String) null)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void writeMapElementMapElementXMLBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		MapPoint point = new MapPoint(3, 4);
		point.setName("name1"); //$NON-NLS-1$
		point.setAttribute("attr1", "Text"); //$NON-NLS-1$ //$NON-NLS-2$

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		XMLBuilder xmlBuilder = new XMLBuilder() {
			@Override
			public Document getDocument() throws DOMException {
				return doc;
			}
		};

		Element node = XMLGISElementUtil.writeMapElement(point, xmlBuilder, res);

		String actual;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			XMLUtil.writeXML(node, baos);
			baos.flush();
			actual = new String(baos.toByteArray());
		}
		final String id = point.getUUID().toString();
		final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" //$NON-NLS-1$
				+ "<point doubleFrame=\"false\" geoId=\"39e072ec9ca6460ee679103fd10d53dc#3;4;3;4\" " //$NON-NLS-1$
				+ "id=\"" + id + "\" name=\"name1\" size=\"0.3\" x=\"3.0\" y=\"4.0\">" //$NON-NLS-1$ //$NON-NLS-2$
				+ "<attributes><attribute name=\"attr1\" type=\"STRING\" value=\"Text\"/></attributes>" //$NON-NLS-1$
				+ "</point>"; //$NON-NLS-1$
		assertEquals(expected, actual);
	}

	@Test
	public void writeMapElementMapElementStringXMLBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		MapPoint point = new MapPoint(3, 4);
		point.setName("name1"); //$NON-NLS-1$
		point.setAttribute("attr1", "Text"); //$NON-NLS-1$ //$NON-NLS-2$

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		XMLBuilder xmlBuilder = new XMLBuilder() {
			@Override
			public Document getDocument() throws DOMException {
				return doc;
			}
		};

		Element node = XMLGISElementUtil.writeMapElement(point, "myElement", xmlBuilder, res); //$NON-NLS-1$

		String actual;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			XMLUtil.writeXML(node, baos);
			baos.flush();
			actual = new String(baos.toByteArray());
		}
		final String id = point.getUUID().toString();
		final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" //$NON-NLS-1$
				+ "<myElement doubleFrame=\"false\" geoId=\"39e072ec9ca6460ee679103fd10d53dc#3;4;3;4\" " //$NON-NLS-1$
				+ "id=\"" + id + "\" name=\"name1\" size=\"0.3\" x=\"3.0\" y=\"4.0\">" //$NON-NLS-1$ //$NON-NLS-2$
				+ "<attributes><attribute name=\"attr1\" type=\"STRING\" value=\"Text\"/></attributes>" //$NON-NLS-1$
				+ "</myElement>"; //$NON-NLS-1$
		assertEquals(expected, actual);
	}

	@Test
	public void readGISElementContainerElementGISElementContainerPathBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc;
		try (InputStream is = url2.openStream()) {
			doc = builder.parse(is);
		}

		ArrayMapElementLayer<MapPoint> container = new ArrayMapElementLayer(MapPoint.class);

		XMLGISElementUtil.readGISElementContainer(doc.getDocumentElement(), container, pathBuilder, res);

		MapPoint point1 = container.getMapElementAt(0);
		assertNotNull(point1);
		assertEquals("name1", point1.getName()); //$NON-NLS-1$
		assertEpsilonEquals(3, point1.getX());
		assertEpsilonEquals(4, point1.getY());

		MapPoint point2 = container.getMapElementAt(1);
		assertNotNull(point2);
		assertEquals("name2", point2.getName()); //$NON-NLS-1$
		assertEpsilonEquals(34, point2.getX());
		assertEpsilonEquals(55, point2.getY());
	}

	@Test
	public void readGISElementContainerElementGISElementContainerStringPathBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc;
		try (InputStream is = url2.openStream()) {
			doc = builder.parse(is);
		}

		ArrayMapElementLayer<MapPoint> container = new ArrayMapElementLayer(MapPoint.class);

		XMLGISElementUtil.readGISElementContainer(doc.getDocumentElement(), container, "point", pathBuilder, res); //$NON-NLS-1$

		MapPoint point1 = container.getMapElementAt(0);
		assertNotNull(point1);
		assertEquals("name1", point1.getName()); //$NON-NLS-1$
		assertEpsilonEquals(3, point1.getX());
		assertEpsilonEquals(4, point1.getY());

		MapPoint point2 = container.getMapElementAt(1);
		assertNotNull(point2);
		assertEquals("name2", point2.getName()); //$NON-NLS-1$
		assertEpsilonEquals(34, point2.getX());
		assertEpsilonEquals(55, point2.getY());
	}

	@Test
	public void writeGISElementContainerElementGISElementContainerXMLBuilderPathBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		ArrayMapElementLayer<MapPoint> container = new ArrayMapElementLayer(MapPoint.class);
		MapPoint pts1 = new MapPoint(3, 4);
		pts1.setName("name1"); //$NON-NLS-1$
		container.addMapElement(pts1);
		MapPoint pts2 = new MapPoint(34, 55);
		pts2.setName("name2"); //$NON-NLS-1$
		container.addMapElement(pts2);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		XMLBuilder xmlBuilder = new XMLBuilder() {
			@Override
			public Document getDocument() throws DOMException {
				return doc;
			}
		};

		Element element = xmlBuilder.createElement("doc"); //$NON-NLS-1$
		doc.appendChild(element);

		XMLGISElementUtil.writeGISElementContainer(element, container, xmlBuilder, pathBuilder, res);

		String actual;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			XMLUtil.writeXML(doc, baos);
			baos.flush();
			actual = new String(baos.toByteArray());
		}
		final String id1 = pts1.getUUID().toString();
		final String id2 = pts2.getUUID().toString();
		final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //$NON-NLS-1$
				+ "<doc elementGeometryProjection=\"FRANCE_LAMBERT_2_EXTENDED\"><elements>" //$NON-NLS-1$
				+ "<point doubleFrame=\"false\" geoId=\"39e072ec9ca6460ee679103fd10d53dc#3;4;3;4\" id=\"" //$NON-NLS-1$
				+ id1 + "\" name=\"name1\" size=\"0.3\" x=\"3.0\" y=\"4.0\"/>" //$NON-NLS-1$
				+ "<point doubleFrame=\"false\" geoId=\"68a29c8943226086e04c05f595e8e580#34;55;34;55\" id=\"" //$NON-NLS-1$
				+ id2 + "\" name=\"name2\" size=\"0.3\" x=\"34.0\" y=\"55.0\"/></elements></doc>"; //$NON-NLS-1$
		assertEquals(expected, actual);
	}

	@Test
	public void writeGISElementContainerElementGISElementContainerStringXMLBuilderPathBuilderXMLResources() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		ArrayMapElementLayer<MapPoint> container = new ArrayMapElementLayer(MapPoint.class);
		MapPoint pts1 = new MapPoint(3, 4);
		pts1.setName("name1"); //$NON-NLS-1$
		container.addMapElement(pts1);
		MapPoint pts2 = new MapPoint(34, 55);
		pts2.setName("name2"); //$NON-NLS-1$
		container.addMapElement(pts2);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		XMLBuilder xmlBuilder = new XMLBuilder() {
			@Override
			public Document getDocument() throws DOMException {
				return doc;
			}
		};

		Element element = xmlBuilder.createElement("doc"); //$NON-NLS-1$
		doc.appendChild(element);

		XMLGISElementUtil.writeGISElementContainer(element, container, "point", xmlBuilder, pathBuilder, res); //$NON-NLS-1$

		String actual;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			XMLUtil.writeXML(doc, baos);
			baos.flush();
			actual = new String(baos.toByteArray());
		}
		final String id1 = pts1.getUUID().toString();
		final String id2 = pts2.getUUID().toString();
		final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //$NON-NLS-1$
				+ "<doc elementGeometryProjection=\"FRANCE_LAMBERT_2_EXTENDED\"><elements>" //$NON-NLS-1$
				+ "<point doubleFrame=\"false\" geoId=\"39e072ec9ca6460ee679103fd10d53dc#3;4;3;4\" id=\"" //$NON-NLS-1$
				+ id1 + "\" name=\"name1\" size=\"0.3\" x=\"3.0\" y=\"4.0\"/>" //$NON-NLS-1$
				+ "<point doubleFrame=\"false\" geoId=\"68a29c8943226086e04c05f595e8e580#34;55;34;55\" id=\"" //$NON-NLS-1$
				+ id2 + "\" name=\"name2\" size=\"0.3\" x=\"34.0\" y=\"55.0\"/></elements></doc>"; //$NON-NLS-1$
		assertEquals(expected, actual);
	}

	@Test
	public void writeGISElementContainerElementGISElementContainerXMLBuilderPathBuilderXMLResources_shp() throws Exception {
		PathBuilder pathBuilder = new SimplePathBuilder();
		XMLResources res = new XMLResources(pathBuilder);
		ArrayMapElementLayer<MapPoint> container = new ArrayMapElementLayer(MapPoint.class);
		container.setElementGeometrySource(new URL("file:/path/to/file.shp"), MapMetricProjection.FRANCE_LAMBERT_2); //$NON-NLS-1$
		MapPoint pts1 = new MapPoint(3, 4);
		pts1.setName("name1"); //$NON-NLS-1$
		container.addMapElement(pts1);
		MapPoint pts2 = new MapPoint(34, 55);
		pts2.setName("name2"); //$NON-NLS-1$
		container.addMapElement(pts2);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		XMLBuilder xmlBuilder = new XMLBuilder() {
			@Override
			public Document getDocument() throws DOMException {
				return doc;
			}
		};

		Element element = xmlBuilder.createElement("doc"); //$NON-NLS-1$
		doc.appendChild(element);

		XMLGISElementUtil.writeGISElementContainer(element, container, xmlBuilder, pathBuilder, res);

		String actual;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			XMLUtil.writeXML(doc, baos);
			baos.flush();
			actual = new String(baos.toByteArray());
		}
		final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" //$NON-NLS-1$
				+ "<doc elementGeometryProjection=\"FRANCE_LAMBERT_2\" elementGeometryUrl=\"#resource0\"/>"; //$NON-NLS-1$
		assertEquals(expected, actual);
		if (OperatingSystem.WIN.isCurrentOS()) {
			assertEquals("file:/" + FileSystem.getUserHomeDirectoryName().replaceAll("\\\\",  "/") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ "//path/to/file.shp", res.getResourceURL(0).toExternalForm()); //$NON-NLS-1$
		} else {
			assertEquals("file:/path/to/file.shp", res.getResourceURL(0).toExternalForm()); //$NON-NLS-1$
		}
	}

}
