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
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Strings;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.arakhne.afc.attrs.xml.XMLAttributeUtil;
import org.arakhne.afc.gis.coordinate.MapMetricProjection;
import org.arakhne.afc.gis.io.shape.GISShapeFileReader;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapCircle;
import org.arakhne.afc.gis.mapelement.MapComposedElement;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapElementConstants;
import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.mapelement.MapPonctualElement;
import org.arakhne.afc.gis.maplayer.MapElementLayer;
import org.arakhne.afc.gis.primitive.GISElement;
import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.inputoutput.xml.XMLBuilder;
import org.arakhne.afc.inputoutput.xml.XMLResources;
import org.arakhne.afc.inputoutput.xml.XMLResources.Entry;
import org.arakhne.afc.inputoutput.xml.XMLUtil;
import org.arakhne.afc.io.dbase.DBaseFileReader;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/**
 * This class provides tools to create an XML representation of map elements
 * or to create map elements from an XML representation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 * @see XMLUtil
 */
public final class XMLGISElementUtil {

	/** <code>&lt;circle /&gt;</code>. */
	public static final String NODE_CIRCLE = "circle"; //$NON-NLS-1$

	/** <code>&lt;elements /&gt;</code>. */
	public static final String NODE_ELEMENTS = "elements"; //$NON-NLS-1$

	/** <code>&lt;group /&gt;</code>. */
	public static final String NODE_GROUP = "group"; //$NON-NLS-1$

	/** <code>&lt;groups /&gt;</code>. */
	public static final String NODE_GROUPS = "groups"; //$NON-NLS-1$

	/** <code>&lt;multipoint /&gt;</code>. */
	public static final String NODE_MULTIPOINT = "multipoint"; //$NON-NLS-1$

	/** <code>&lt;point /&gt;</code>. */
	public static final String NODE_POINT = "point"; //$NON-NLS-1$

	/** <code>&lt;points /&gt;</code>. */
	public static final String NODE_POINTS = "points"; //$NON-NLS-1$

	/** <code>&lt;polygon /&gt;</code>. */
	public static final String NODE_POLYGON = "polygon"; //$NON-NLS-1$

	/** <code>&lt;polyline /&gt;</code>. */
	public static final String NODE_POLYLINE = "polyline"; //$NON-NLS-1$

	/** <code>doubleFrame=""</code>. */
	public static final String ATTR_DOUBLEFRAME = "doubleFrame"; //$NON-NLS-1$

	/** <code>firstPointIndex=""</code>. */
	public static final String ATTR_FIRSTPOINTINDEX = "firstPointIndex"; //$NON-NLS-1$

	/** <code>lastPointIndex=""</code>. */
	public static final String ATTR_LASTPOINTINDEX = "lastPointIndex"; //$NON-NLS-1$

	/** <code>radius=""</code>. */
	public static final String ATTR_RADIUS = "radius"; //$NON-NLS-1$

	/** <code>size=""</code>. */
	public static final String ATTR_SIZE = "size"; //$NON-NLS-1$

	/** <code>wide=""</code>. */
	public static final String ATTR_WIDE = "wide"; //$NON-NLS-1$

	/** <code>width=""</code>. */
	public static final String ATTR_WIDTH = "width"; //$NON-NLS-1$

	/** <code>x=""</code>. */
	public static final String ATTR_X = "x"; //$NON-NLS-1$

	/** <code>y=""</code>. */
	public static final String ATTR_Y = "y"; //$NON-NLS-1$

	private XMLGISElementUtil() {
		//
	}

	/** Write the XML description for the given map element.
	 *
	 * @param primitive is the map element to output.
	 * @param builder is the tool to create XML nodes.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the XML node of the map element.
	 * @throws IOException in case of error.
	 */
	public static Element writeMapElement(MapElement primitive, XMLBuilder builder, XMLResources resources) throws IOException {
		return writeMapElement(primitive, null, builder, resources);
	}

	/** Write the XML description for the given map element.
	 *
	 * @param primitive is the map element to output.
	 * @param primitiveNodeName is the name of the XML node that should contains the map element data.
	 *     It may be {@code null} to use the default name.
	 * @param builder is the tool to create XML nodes.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the XML node of the map element.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public static Element writeMapElement(MapElement primitive, String primitiveNodeName,
			XMLBuilder builder, XMLResources resources) throws IOException {
		Element primitiveNode = null;
		if (primitive instanceof MapPonctualElement) {
			final MapPonctualElement mpe = (MapPonctualElement) primitive;
			if (mpe instanceof MapPoint) {
				final MapPoint mp = (MapPoint) mpe;
				primitiveNode = builder.createElement(primitiveNodeName != null ? primitiveNodeName : NODE_POINT);
				primitiveNode.setAttribute(ATTR_DOUBLEFRAME, Boolean.toString(mp.isDoubleFramed()));
				primitiveNode.setAttribute(ATTR_SIZE, Double.toString(mp.getPointSize()));
			} else if (mpe instanceof MapCircle) {
				final MapCircle mc = (MapCircle) mpe;
				primitiveNode = builder.createElement(primitiveNodeName != null ? primitiveNodeName : NODE_CIRCLE);
				primitiveNode.setAttribute(ATTR_RADIUS, Double.toString(mc.getRadius()));
			} else {
				throw new IOException("unsupported MapElement type: " + mpe.getClass()); //$NON-NLS-1$
			}
			primitiveNode.setAttribute(ATTR_X, Double.toString(mpe.getX()));
			primitiveNode.setAttribute(ATTR_Y, Double.toString(mpe.getY()));
		} else if (primitive instanceof MapComposedElement) {
			final MapComposedElement mce = (MapComposedElement) primitive;
			if (mce instanceof MapPolyline) {
				final MapPolyline mp = (MapPolyline) mce;
				primitiveNode = builder.createElement(primitiveNodeName != null ? primitiveNodeName : NODE_POLYLINE);
				primitiveNode.setAttribute(ATTR_WIDE, Boolean.toString(mp.isWidePolyline()));
				primitiveNode.setAttribute(ATTR_WIDTH, Double.toString(mp.getWidth()));
			} else if (mce instanceof MapMultiPoint) {
				final MapMultiPoint mmp = (MapMultiPoint) mce;
				primitiveNode = builder.createElement(primitiveNodeName != null ? primitiveNodeName : NODE_MULTIPOINT);
				primitiveNode.setAttribute(ATTR_DOUBLEFRAME, Boolean.toString(mmp.isDoubleFramed()));
				primitiveNode.setAttribute(ATTR_SIZE, Double.toString(mmp.getPointSize()));
			} else if (mce instanceof MapPolygon) {
				primitiveNode = builder.createElement(primitiveNodeName != null ? primitiveNodeName : NODE_POLYGON);
			} else {
				throw new IOException("unsupported MapElement type: " + mce.getClass()); //$NON-NLS-1$
			}

			final Element pointsNode = builder.createElement(NODE_POINTS);
			for (int idxPoint = 0; idxPoint < mce.getPointCount(); ++idxPoint) {
				final Element pointNode = builder.createElement(NODE_POINT);
				final Point2d p = mce.getPointAt(idxPoint);
				pointNode.setAttribute(ATTR_X, Double.toString(p.getX()));
				pointNode.setAttribute(ATTR_Y, Double.toString(p.getY()));
				pointsNode.appendChild(pointNode);
			}
			if (pointsNode.getChildNodes().getLength() > 0) {
				primitiveNode.appendChild(pointsNode);
			}
			final Element groupsNode = builder.createElement(NODE_GROUPS);
			for (int idxGroup = 0; idxGroup < mce.getGroupCount(); ++idxGroup) {
				final Element groupNode = builder.createElement(NODE_GROUP);
				final int idx1 = mce.getFirstPointIndexInGroup(idxGroup);
				final int idx2 = mce.getLastPointIndexInGroup(idxGroup);
				groupNode.setAttribute(ATTR_FIRSTPOINTINDEX, Integer.toString(idx1));
				groupNode.setAttribute(ATTR_LASTPOINTINDEX, Integer.toString(idx2));
				groupsNode.appendChild(groupNode);
			}
			if (groupsNode.getChildNodes().getLength() > 0) {
				primitiveNode.appendChild(groupsNode);
			}
		}

		assert primitiveNode != null;

		writeGISElementAttributes(primitiveNode, primitive, builder, resources);
		final Integer color = primitive.getRawColor();
		if (color != null) {
			primitive.setAttribute(XMLUtil.ATTR_COLOR, XMLUtil.toColor(color));
		}

		return primitiveNode;
	}

	/** Read a map element from the XML description.
	 *
	 * @param element is the XML node to read.
	 * @param elementNodeName is the name of the XML node that should contains the map element data.
	 *     It must be one of {@link #NODE_POINT}, {@link #NODE_CIRCLE}, {@link #NODE_POLYGON}, {@link #NODE_POLYLINE},
	 * {@link #NODE_MULTIPOINT}, or {@code null} for the XML node name itself.
	 * @param pathBuilder is the tool to make paths absolute.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the map element.
	 * @throws IOException in case of error.
	 */
	public static MapElement readMapElement(Element element, String elementNodeName,
			PathBuilder pathBuilder, XMLResources resources) throws IOException {
		return readMapElement(element, elementNodeName, null, pathBuilder, resources);
	}

	/** Read a map element from the XML description.
	 *
	 * @param <T> is the type of the element to create.
	 * @param element is the XML node to read.
	 * @param elementNodeName is the name of the XML node that should contains the map element data.
	 *     It must be one of {@link #NODE_POINT}, {@link #NODE_CIRCLE}, {@link #NODE_POLYGON}, {@link #NODE_POLYLINE},
	 * {@link #NODE_MULTIPOINT}, or {@code null} for the XML node name itself.
	 * @param type is the type of the element to create, or {@code null} to use the default.
	 * @param pathBuilder is the tool to make paths absolute.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the map element.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings({"unchecked", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public static <T extends MapElement> T readMapElement(Element element, String elementNodeName,
			Class<T> type, PathBuilder pathBuilder, XMLResources resources) throws IOException {
		final UUID id = XMLUtil.getAttributeUUIDWithDefault(element, null, XMLUtil.ATTR_ID);

		String nn = elementNodeName;

		if (nn == null || nn.length() == 0) {
			nn = element.getNodeName();
		}

		if (NODE_POINT.equals(nn)) {
			final double x = XMLUtil.getAttributeDouble(element, ATTR_X);
			final double y = XMLUtil.getAttributeDouble(element, ATTR_Y);
			final boolean doubleFrame = XMLUtil.getAttributeBooleanWithDefault(element, false, ATTR_DOUBLEFRAME);
			final double size = XMLUtil.getAttributeDoubleWithDefault(element, GeoLocationUtil.GIS_POINT_SIZE, ATTR_SIZE);
			final MapPoint point;
			if (type != null && MapPoint.class.isAssignableFrom(type)) {
				try {
					if (id != null) {
						final Constructor<T> cons = type.getConstructor(UUID.class, double.class, double.class);
						point = (MapPoint) cons.newInstance(id, x, y);
					} else {
						final Constructor<T> cons = type.getConstructor(double.class, double.class);
						point = (MapPoint) cons.newInstance(x, y);
					}
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable e) {
					throw new IOException(e);
				}
			} else if (id == null) {
				point = new MapPoint(x, y);
			} else {
				point = new MapPoint(id, x, y);
			}
			point.setDoubleFramed(doubleFrame);
			point.setPointSize(size);

			readGISElementAttributes(element, point, pathBuilder, resources);
			final Integer color = XMLUtil.getAttributeColorWithDefault(element, null, XMLUtil.ATTR_COLOR);
			if (color != null) {
				point.setColor(color);
			}

			return (T) point;
		}

		if (NODE_CIRCLE.equals(nn)) {
			final double x = XMLUtil.getAttributeDouble(element, ATTR_X);
			final double y = XMLUtil.getAttributeDouble(element, ATTR_Y);
			final double radius = XMLUtil.getAttributeDoubleWithDefault(element,
					MapElementConstants.getPreferredRadius(), ATTR_RADIUS);
			final MapCircle circle;
			if (type != null && MapCircle.class.isAssignableFrom(type)) {
				try {
					if (id != null) {
						final Constructor<T> cons = type.getConstructor(UUID.class, double.class, double.class);
						circle = (MapCircle) cons.newInstance(id, x, y);
					} else {
						final Constructor<T> cons = type.getConstructor(double.class, double.class);
						circle = (MapCircle) cons.newInstance(x, y);
					}
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable e) {
					throw new IOException(e);
				}
			} else if (id == null) {
				circle = new MapCircle(x, y);
			} else {
				circle = new MapCircle(id, x, y);
			}
			circle.setRadius(radius);

			readGISElementAttributes(element, circle, pathBuilder, resources);
			final Integer color = XMLUtil.getAttributeColorWithDefault(element, null, XMLUtil.ATTR_COLOR);
			if (color != null) {
				circle.setColor(color);
			}

			return (T) circle;
		}
		if (NODE_POLYLINE.equals(nn)) {
			final boolean isWide = XMLUtil.getAttributeBooleanWithDefault(element, false, ATTR_WIDE);
			final MapPolyline polyline;
			if (type != null && MapPolyline.class.isAssignableFrom(type)) {
				try {
					if (id != null) {
						final Constructor<T> cons = type.getConstructor(UUID.class);
						polyline = (MapPolyline) cons.newInstance(id);
					} else {
						polyline = (MapPolyline) type.getDeclaredConstructor().newInstance();
					}
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable e) {
					throw new IOException(e);
				}
			} else if (id == null) {
				polyline = new MapPolyline();
			} else {
				polyline = new MapPolyline(id);
			}
			polyline.setWidePolyline(isWide);
			readMapComposedElementPoints(element, polyline);

			readGISElementAttributes(element, polyline, pathBuilder, resources);
			final Integer color = XMLUtil.getAttributeColorWithDefault(element, null, XMLUtil.ATTR_COLOR);
			if (color != null) {
				polyline.setColor(color);
			}

			return (T) polyline;
		}
		if (NODE_POLYGON.equals(nn)) {
			final MapPolygon polygon;
			if (type != null && MapPolygon.class.isAssignableFrom(type)) {
				try {
					if (id != null) {
						final Constructor<T> cons = type.getConstructor(UUID.class);
						polygon = (MapPolygon) cons.newInstance(id);
					} else {
						polygon = (MapPolygon) type.getDeclaredConstructor().newInstance();
					}
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable e) {
					throw new IOException(e);
				}
			} else if (id == null) {
				polygon = new MapPolygon();
			} else {
				polygon = new MapPolygon(id);
			}
			readMapComposedElementPoints(element, polygon);

			readGISElementAttributes(element, polygon, pathBuilder, resources);
			final Integer color = XMLUtil.getAttributeColorWithDefault(element, null, XMLUtil.ATTR_COLOR);
			if (color != null) {
				polygon.setColor(color);
			}

			return (T) polygon;
		}
		if (NODE_MULTIPOINT.equals(nn)) {
			final boolean doubleFrame = XMLUtil.getAttributeBooleanWithDefault(element, false, ATTR_DOUBLEFRAME);
			final double size = XMLUtil.getAttributeDoubleWithDefault(element, GeoLocationUtil.GIS_POINT_SIZE, ATTR_SIZE);
			final MapMultiPoint multipoint;
			if (type != null && MapMultiPoint.class.isAssignableFrom(type)) {
				try {
					if (id != null) {
						final Constructor<T> cons = type.getConstructor(UUID.class);
						multipoint = (MapMultiPoint) cons.newInstance(id);
					} else {
						multipoint = (MapMultiPoint) type.getDeclaredConstructor().newInstance();
					}
				} catch (AssertionError e) {
					throw e;
				} catch (Throwable e) {
					throw new IOException(e);
				}
			} else if (id == null) {
				multipoint = new MapMultiPoint();
			} else {
				multipoint = new MapMultiPoint(id);
			}
			multipoint.setDoubleFramed(doubleFrame);
			multipoint.setPointSize(size);
			readMapComposedElementPoints(element, multipoint);

			readGISElementAttributes(element, multipoint, pathBuilder, resources);
			final Integer color = XMLUtil.getAttributeColorWithDefault(element, null, XMLUtil.ATTR_COLOR);
			if (color != null) {
				multipoint.setColor(color);
			}

			return (T) multipoint;
		}
		throw new IOException("unable to parse the XML node to retrieve a map element"); //$NON-NLS-1$
	}

	/** Replies the default name of the XML node for the given type of map element.
	 *
	 * @param type the type of the map element.
	 * @return the default XML node name; or {@code null} if the type if unknown.
	 */
	public static String getDefaultMapElementNodeName(Class<? extends MapElement> type) {
		if (MapPolyline.class.isAssignableFrom(type)) {
			return NODE_POLYLINE;
		}
		if (MapPoint.class.isAssignableFrom(type)) {
			return NODE_POINT;
		}
		if (MapPolygon.class.isAssignableFrom(type)) {
			return NODE_POLYGON;
		}
		if (MapCircle.class.isAssignableFrom(type)) {
			return NODE_CIRCLE;
		}
		if (MapMultiPoint.class.isAssignableFrom(type)) {
			return NODE_MULTIPOINT;
		}
		return null;
	}

	private static void readMapComposedElementPoints(Node element, MapComposedElement mapElement) {
		final Node pointNode = XMLUtil.getNodeFromPath(element, NODE_POINTS);
		final Node groupNode = XMLUtil.getNodeFromPath(element, NODE_GROUPS);
		if (pointNode != null && groupNode != null) {
			final Point2d[] points = readPoints(pointNode);
			if (points.length > 0) {
				readGroups(groupNode, points, mapElement);
			}
		}
	}

	private static Point2d[] readPoints(Node element) {
		final List<Element> children = XMLUtil.getElementsFromPath(element, NODE_POINT);
		final Point2d[] points = new Point2d[children.size()];
		Element child;
		for (int i = 0; i < points.length; ++i) {
			child = children.get(i);
			final double x = XMLUtil.getAttributeDouble(child, ATTR_X);
			final double y = XMLUtil.getAttributeDouble(child, ATTR_Y);
			points[i] = new Point2d(x, y);
		}
		return points;
	}

	private static void readGroups(Node element, Point2d[] points, MapComposedElement mapElement) {
		for (final Node group : XMLUtil.getNodesFromPath(element, NODE_GROUP)) {
			final int start = XMLUtil.getAttributeInt(group, ATTR_FIRSTPOINTINDEX);
			final int end = XMLUtil.getAttributeInt(group, ATTR_LASTPOINTINDEX);
			mapElement.addGroup(points[start]);
			for (int idxPts = start + 1; idxPts <= end; ++idxPts) {
				mapElement.addPoint(points[idxPts]);
			}
		}
	}

	/** Write the XML description for the given container of map elements.
	 *
	 * @param xmlNode is the XML node to fill with the container data.
	 * @param primitive is the container of map elements to output.
	 * @param builder is the tool to create XML nodes.
	 * @param pathBuilder is the tool to make paths relative.
	 * @param resources is the tool that permits to gather the resources.
	 * @throws IOException in case of error.
	 */
	public static void writeGISElementContainer(Element xmlNode, GISElementContainer<?> primitive,
			XMLBuilder builder, PathBuilder pathBuilder, XMLResources resources) throws IOException {
		writeGISElementContainer(xmlNode, primitive, null, builder, pathBuilder, resources);
	}

	/** Write the XML description for the given container of map elements.
	 *
	 * @param xmlNode is the XML node to fill with the container data.
	 * @param primitive is the container of map elements to output.
	 * @param elementNodeName is the name of the XML node that should contains the map element data.
	 *     It may be {@code null} to use the default name.
	 * @param builder is the tool to create XML nodes.
	 * @param pathBuilder is the tool to make paths relative.
	 * @param resources is the tool that permits to gather the resources.
	 * @throws IOException in case of error.
	 */
	public static void writeGISElementContainer(Element xmlNode, GISElementContainer<?> primitive,
			String elementNodeName, XMLBuilder builder, PathBuilder pathBuilder, XMLResources resources) throws IOException {
		URL url;
		boolean saveElements = true;

		url = primitive.getElementGeometrySourceURL();
		if (url != null) {
			xmlNode.setAttribute(MapElementLayer.ATTR_ELEMENT_GEOMETRY_URL,
					resources.add(url, MimeName.MIME_SHAPE_FILE.getMimeConstant()));
			saveElements = false;
		}

		MapMetricProjection mapProjection = primitive.getElementGeometrySourceProjection();
		if (mapProjection == null) {
			mapProjection = MapMetricProjection.getDefault();
		}
		xmlNode.setAttribute(MapElementLayer.ATTR_ELEMENT_GEOMETRY_PROJECTION, mapProjection.name());

		url = primitive.getElementAttributeSourceURL();
		if (!saveElements && url != null) {
			xmlNode.setAttribute(MapElementLayer.ATTR_ELEMENT_ATTRIBUTES_URL,
					resources.add(url, MimeName.MIME_DBASE_FILE.getMimeConstant()));
		}

		if (saveElements) {
			final Element elementList = builder.createElement(NODE_ELEMENTS);
			for (final MapElement element : primitive) {
				final Element e = XMLGISElementUtil.writeMapElement(element, elementNodeName, builder, resources);
				if (e != null) {
					elementList.appendChild(e);
				}
			}
			if (elementList.getChildNodes().getLength() > 0) {
				xmlNode.appendChild(elementList);
			}
		}
	}

	/** Read map elements from the XML description.
	 *
	 * @param <T> is the type of the elements inside the container.
	 * @param xmlNode is the XML node to read.
	 * @param primitive is the container of map elements to output.
	 * @param pathBuilder is the tool to make paths absolute.
	 * @param resources is the tool that permits to gather the resources.
	 * @throws IOException in case of error.
	 */
	public static <T extends MapElement> void readGISElementContainer(Element xmlNode,
			GISElementContainer<T> primitive, PathBuilder pathBuilder, XMLResources resources) throws IOException {
		readGISElementContainer(xmlNode, primitive, null, pathBuilder, resources);
	}

	/** Read map elements from the XML description.
	 *
	 * @param <T> is the type of the elements inside the container.
	 * @param xmlNode is the XML node to read.
	 * @param primitive is the container of map elements to output.
	 * @param elementNodeName is the name of the XML node that should contains the map element data.
	 *     It may be {@code null} to use the default name.
	 * @param pathBuilder is the tool to make paths absolute.
	 * @param resources is the tool that permits to gather the resources.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:nestedifdepth", "resource"})
	public static <T extends MapElement> void readGISElementContainer(Element xmlNode,
			GISElementContainer<T> primitive, String elementNodeName, PathBuilder pathBuilder,
			XMLResources resources) throws IOException {
		// Build the reader for the attributes
		DBaseFileReader attrReader = null;
		URL attrSourceURL = null;
		String sattr = XMLUtil.getAttributeValueWithDefault(xmlNode, null, MapElementLayer.ATTR_ELEMENT_ATTRIBUTES_URL);
		if (sattr != null && sattr.length() > 0) {
			if (XMLResources.isStringIdentifier(sattr)) {
				final long id = XMLResources.getNumericalIdentifier(sattr);
				final Entry resource = resources.getResource(id);
				if (resource != null) {
					if (resource.isURL()) {
						attrSourceURL = pathBuilder.makeAbsolute(resource.getURL());
						if (attrSourceURL != null) {
							attrReader = new DBaseFileReader(attrSourceURL);
						}
					} else if (resource.isFile()) {
						attrSourceURL = pathBuilder.makeAbsolute(resource.getFile());
						if (attrSourceURL != null) {
							attrReader = new DBaseFileReader(attrSourceURL);
						}
					} else if (resource.isEmbeddedData()) {
						final ByteArrayInputStream bais = new ByteArrayInputStream(resource.getEmbeddedData());
						attrReader = new DBaseFileReader(bais);
					} else {
						throw new IOException("unsupported resource type: " + sattr); //$NON-NLS-1$
					}
				} else {
					throw new IOException("resource not found: " + sattr); //$NON-NLS-1$
				}
			} else {
				attrSourceURL = pathBuilder.makeAbsolute(sattr);
				if (attrSourceURL != null) {
					attrReader = new DBaseFileReader(attrSourceURL);
				} else {
					throw new IOException("resource not found: " + sattr); //$NON-NLS-1$
				}
			}
		}

		// Retrieve the map projection
		final MapMetricProjection mapProjection = XMLUtil.getAttributeEnumWithDefault(xmlNode, MapMetricProjection.class,
				MapMetricProjection.getDefault(), MapElementLayer.ATTR_ELEMENT_GEOMETRY_PROJECTION);

		// Build the reader for the shapes
		GISShapeFileReader shapeReader = null;
		URL shapeSourceURL = null;
		sattr = XMLUtil.getAttributeValueWithDefault(xmlNode, null, MapElementLayer.ATTR_ELEMENT_GEOMETRY_URL);
		if (sattr != null && sattr.length() > 0) {
			if (XMLResources.isStringIdentifier(sattr)) {
				final long id = XMLResources.getNumericalIdentifier(sattr);
				final Entry resource = resources.getResource(id);
				if (resource != null) {
					if (resource.isURL()) {
						shapeSourceURL = pathBuilder.makeAbsolute(resource.getURL());
						if (shapeSourceURL != null) {
							shapeReader = createShapeReader(shapeSourceURL, primitive.getElementType(), attrReader,
									attrSourceURL, mapProjection);
						}
					} else if (resource.isFile()) {
						shapeSourceURL = pathBuilder.makeAbsolute(resource.getFile());
						if (shapeSourceURL != null) {
							shapeReader = createShapeReader(shapeSourceURL, primitive.getElementType(), attrReader,
									attrSourceURL, mapProjection);
						}
					} else if (resource.isEmbeddedData()) {
						final ByteArrayInputStream bais = new ByteArrayInputStream(resource.getEmbeddedData());
						shapeReader = createShapeReader(bais, primitive.getElementType(), attrReader,
								attrSourceURL, mapProjection);
					} else {
						throw new IOException("unsupported resource type: " + sattr); //$NON-NLS-1$
					}
				} else {
					throw new IOException("resource not found: " + sattr); //$NON-NLS-1$
				}
			} else {
				shapeSourceURL = pathBuilder.makeAbsolute(sattr);
				if (shapeSourceURL != null) {
					shapeReader = createShapeReader(shapeSourceURL, primitive.getElementType(), attrReader,
							attrSourceURL, mapProjection);
				} else {
					throw new IOException("resource not found: " + sattr); //$NON-NLS-1$
				}
			}
		} else {
			attrSourceURL = null;
		}

		primitive.setElementGeometrySource(shapeSourceURL, mapProjection);
		primitive.setElementAttributeSourceURL(attrSourceURL);

		if (shapeReader != null) {
			final Iterator<? extends T> iterator = shapeReader.iterator(primitive.getElementType());
			while (iterator.hasNext()) {
				primitive.addMapElement(iterator.next());
			}
			shapeReader.close();
		}

		// Read embedded elements
		final String enm;
		final String enmDefault = getDefaultMapElementNodeName(primitive.getElementType());
		if (Strings.isNullOrEmpty(elementNodeName)) {
			enm = enmDefault;
		} else {
			enm = elementNodeName;
		}
		T mapElement;
		for (final Element elementNode : XMLUtil.getElementsFromPath(xmlNode, NODE_ELEMENTS, enm)) {
			mapElement = XMLGISElementUtil.readMapElement(elementNode,
					enmDefault, primitive.getElementType(), pathBuilder, resources);
			if (mapElement != null) {
				primitive.addMapElement(mapElement);
			}
		}
	}

	private static <T extends MapElement> GISShapeFileReader createShapeReader(URL shapeURL,
			Class<? extends T> elementType, DBaseFileReader attrReader, URL attrSourceURL,
			MapMetricProjection mapProjection) throws IOException {
		final GISShapeFileReader reader;
		if (attrReader != null) {
			if (attrSourceURL != null) {
				reader = new GISShapeFileReader(
						shapeURL, elementType,
						attrReader, attrSourceURL);
			} else {
				reader = new GISShapeFileReader(
						shapeURL, elementType,
						attrReader);
			}
		} else {
			reader = new GISShapeFileReader(shapeURL, elementType);
		}
		reader.setMapMetricProjection(mapProjection);
		return reader;
	}

	private static <T extends MapElement> GISShapeFileReader createShapeReader(InputStream shapeStream,
			Class<? extends T> elementType, DBaseFileReader attrReader, URL attrSourceURL,
			MapMetricProjection mapProjection) throws IOException {
		final GISShapeFileReader reader;
		if (attrReader != null) {
			if (attrSourceURL != null) {
				reader = new GISShapeFileReader(
						shapeStream, elementType,
						attrReader, attrSourceURL);
			} else {
				reader = new GISShapeFileReader(
						shapeStream, elementType,
						attrReader);
			}
		} else {
			reader = new GISShapeFileReader(shapeStream, elementType);
		}
		reader.setMapMetricProjection(mapProjection);
		return reader;
	}

	/** Write the attributes of the given GISElement in the the XML description.
	 * Only the name, geoId, id, and additional attributes are written. Icon and color are not written.
	 *
	 * @param element is the XML node to fill with the attributes
	 * @param primitive is the map element to output.
	 *     It may be {@code null} to use the default name.
	 * @param builder is the tool to create XML nodes.
	 * @param resources is the tool that permits to gather the resources.
	 * @throws IOException in case of error.
	 */
	public static void writeGISElementAttributes(Element element, GISElement primitive, XMLBuilder builder,
			XMLResources resources) throws IOException {
		XMLAttributeUtil.writeAttributeContainer(element, primitive, builder, resources, false);
		element.setAttribute(XMLUtil.ATTR_NAME, primitive.getName());
		element.setAttribute(XMLAttributeUtil.ATTR_GEOID, primitive.getGeoId().toString());
		element.setAttribute(XMLUtil.ATTR_ID, primitive.getUUID().toString());
	}

	/** Read the attributes of a GISElement from the XML description.
	 * Only the name and additional attributes are read. Id, geoId, icon and color are not read.
	 *
	 * @param element is the XML node to read.
	 * @param primitive is the GISElement to set up.
	 * @param pathBuilder is the tool to make paths absolute.
	 * @param resources is the tool that permits to gather the resources.
	 * @throws IOException in case of error.
	 */
	public static void readGISElementAttributes(Element element, GISElement primitive, PathBuilder pathBuilder,
			XMLResources resources) throws IOException {
		XMLAttributeUtil.readAttributeContainer(element, primitive, pathBuilder, resources, false);
		primitive.setName(XMLUtil.getAttributeValueWithDefault(element, null, XMLUtil.ATTR_NAME));
	}

}
