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

import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.ATTR_WIDTH;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.ATTR_X;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.ATTR_Y;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.NODE_POLYLINE;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.readGISElementContainer;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.readMapElement;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.writeGISElementContainer;
import static org.arakhne.afc.gis.io.xml.XMLGISElementUtil.writeMapElement;
import static org.arakhne.afc.inputoutput.xml.XMLUtil.getAttributeDoubleWithDefault;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.Iterators;
import org.w3c.dom.Element;

import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.gis.coordinate.MapMetricProjection;
import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.maplayer.MapElementLayer;
import org.arakhne.afc.gis.primitive.ChangeListener;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.inputoutput.xml.XMLBuilder;
import org.arakhne.afc.inputoutput.xml.XMLResources;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/**
 * This class provides tools to create an XML representation of roads
 * or to create roads from an XML representation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public final class XMLRoadUtil {

	/** <code>&lt;circle /&gt;</code>. */
	public static final String NODE_ROAD = "road"; //$NON-NLS-1$

	/** <code>height=""</code>. */
	public static final String ATTR_HEIGHT = "height"; //$NON-NLS-1$

	private XMLRoadUtil() {
		//
	}

	/** Write the XML description for the given road.
	 *
	 * @param primitive is the road to output.
	 * @param builder is the tool to create XML nodes.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the XML node of the map element.
	 * @throws IOException in case of error.
	 */
	public static Element writeRoadPolyline(RoadPolyline primitive, XMLBuilder builder,
			XMLResources resources) throws IOException {
		return writeMapElement(primitive, NODE_ROAD, builder, resources);
	}

	/** Read a road from the XML description.
	 *
	 * @param element is the XML node to read.
	 * @param pathBuilder is the tool to make paths absolute.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the road.
	 * @throws IOException in case of error.
	 */
	public static RoadPolyline readRoadPolyline(Element element, PathBuilder pathBuilder,
			XMLResources resources) throws IOException {
		return readMapElement(element, NODE_POLYLINE,
				RoadPolyline.class, pathBuilder, resources);
	}

	/** Write the XML description for the given road.
	 *
	 * @param primitive is the road to output.
	 * @param builder is the tool to create XML nodes.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the XML node of the map element.
	 * @throws IOException in case of error.
	 */
	public static Element writeRoadSegment(RoadSegment primitive, XMLBuilder builder, XMLResources resources) throws IOException {
		if (primitive instanceof RoadPolyline) {
			return writeRoadPolyline((RoadPolyline) primitive, builder, resources);
		}
		throw new IOException("unsupported type of primitive: " + primitive); //$NON-NLS-1$
	}

	/** Read a road from the XML description.
	 *
	 * @param element is the XML node to read.
	 * @param pathBuilder is the tool to make paths absolute.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the road.
	 * @throws IOException in case of error.
	 */
	public static RoadSegment readRoadSegment(Element element, PathBuilder pathBuilder,
			XMLResources resources) throws IOException {
		return readRoadPolyline(element, pathBuilder, resources);
	}

	/** Write the XML description for the given container of roads.
	 *
	 * @param xmlNode is the XML node to fill with the container data.
	 * @param primitive is the container of roads to output.
	 * @param geometryURL is the URL of the file that contains the geometry of the roads.
	 *     If <code>null</code>, the road will be directly written in the XML document.
	 * @param mapProjection is the map projection to use to write the geometry shapes.
	 * @param attributeURL is the URL of the file that contains the attributes of the roads.
	 *     This parameter is used only if <var>geometryURL</var> is not <code>null</code>.
	 * @param builder is the tool to create XML nodes.
	 * @param pathBuilder is the tool to make paths relative.
	 * @param resources is the tool that permits to gather the resources.
	 * @throws IOException in case of error.
	 */
	public static void writeRoadNetwork(Element xmlNode, RoadNetwork primitive, URL geometryURL,
			MapMetricProjection mapProjection, URL attributeURL, XMLBuilder builder,
			PathBuilder pathBuilder, XMLResources resources) throws IOException {
		final ContainerWrapper w = new ContainerWrapper(primitive);
		w.setElementGeometrySource(geometryURL, mapProjection);
		w.setElementAttributeSourceURL(attributeURL);
		writeGISElementContainer(xmlNode,
				w, NODE_ROAD, builder, pathBuilder, resources);
		final Rectangle2d bounds = primitive.getBoundingBox();
		if (bounds != null) {
			xmlNode.setAttribute(ATTR_X, Double.toString(bounds.getMinX()));
			xmlNode.setAttribute(ATTR_Y, Double.toString(bounds.getMinY()));
			xmlNode.setAttribute(ATTR_WIDTH, Double.toString(bounds.getWidth()));
			xmlNode.setAttribute(ATTR_HEIGHT, Double.toString(bounds.getHeight()));
		}
	}

	/** Read the roads from the XML description.
	 *
	 * @param xmlNode is the XML node to fill with the container data.
	 * @param primitive is the container of roads to read.
	 * @param pathBuilder is the tool to make paths relative.
	 * @param resources is the tool that permits to gather the resources.
	 * @throws IOException in case of error.
	 */
	public static void readRoadNetwork(Element xmlNode, RoadNetwork primitive, PathBuilder pathBuilder,
			XMLResources resources) throws IOException {
		final ContainerWrapper w = new ContainerWrapper(primitive);
		readGISElementContainer(xmlNode,
				w, NODE_ROAD, pathBuilder, resources);
		URL u;

		// Force the primitive to have the pointer to the Shape and dBase files

		u = w.getElementGeometrySourceURL();
		if (u != null) {
			primitive.setAttribute(MapElementLayer.ATTR_ELEMENT_GEOMETRY_URL, u);
		} else {
			primitive.removeAttribute(MapElementLayer.ATTR_ELEMENT_GEOMETRY_URL);
		}

		u = w.getElementAttributeSourceURL();
		if (u != null) {
			primitive.setAttribute(MapElementLayer.ATTR_ELEMENT_ATTRIBUTES_URL, u);
		} else {
			primitive.removeAttribute(MapElementLayer.ATTR_ELEMENT_ATTRIBUTES_URL);
		}

		final MapMetricProjection projection = w.getElementGeometrySourceProjection();
		if (projection != null) {
			primitive.setAttribute(MapElementLayer.ATTR_ELEMENT_GEOMETRY_PROJECTION, projection);
		} else {
			primitive.removeAttribute(MapElementLayer.ATTR_ELEMENT_GEOMETRY_PROJECTION);
		}
	}

	/** Read the roads from the XML description.
	 *
	 * @param xmlNode is the XML node to fill with the container data.
	 * @param pathBuilder is the tool to make paths relative.
	 * @param resources is the tool that permits to gather the resources.
	 * @return the road network.
	 * @throws IOException in case of error.
	 */
	public static StandardRoadNetwork readRoadNetwork(Element xmlNode, PathBuilder pathBuilder,
			XMLResources resources) throws IOException {
		final double x = getAttributeDoubleWithDefault(xmlNode, Double.NaN, ATTR_X);
		final double y = getAttributeDoubleWithDefault(xmlNode, Double.NaN, ATTR_Y);
		final double width = getAttributeDoubleWithDefault(xmlNode, Double.NaN, ATTR_WIDTH);
		final double height = getAttributeDoubleWithDefault(xmlNode, Double.NaN, ATTR_HEIGHT);

		if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(width) || Double.isNaN(height)) {
			throw new IOException("invalid road network bounds"); //$NON-NLS-1$
		}

		final Rectangle2d bounds = new Rectangle2d(x, y, width, height);
		final StandardRoadNetwork roadNetwork = new StandardRoadNetwork(bounds);

		readRoadNetwork(xmlNode, roadNetwork, pathBuilder, resources);

		return roadNetwork;
	}

	/** Wrapper of a Road polyline container.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 15.0
	 */
	private static class ContainerWrapper implements  GISElementContainer<RoadPolyline> {

		private URL shapeURL;

		private MapMetricProjection shapeProjection;

		private URL attributeURL;

		private final RoadNetwork container;

		/** Constructor.
		 *
		 * @param container the container.
		 */
		ContainerWrapper(RoadNetwork container) {
			this.container = container;
		}

		@Override
		public boolean addMapElement(RoadPolyline element) {
			try {
				this.container.addRoadSegment(element);
				return true;
			} catch (RoadNetworkException e) {
				return false;
			}
		}

		@Override
		public boolean addMapElements(Collection<? extends RoadPolyline> elements) {
			boolean changed = false;
			for (final RoadSegment s : elements) {
				try {
					this.container.addRoadSegment(s);
					changed = true;
				} catch (RoadNetworkException e) {
					//
				}
			}
			return changed;
		}

		@Override
		@SuppressWarnings("unchecked")
		public Collection<RoadPolyline> getAllMapElements() {
			return (Collection<RoadPolyline>) this.container.getRoadSegments();
		}

		@Override
		public URL getElementAttributeSourceURL() {
			return this.attributeURL;
		}

		@Override
		public URL getElementGeometrySourceURL() {
			return this.shapeURL;
		}

		@Override
		public MapMetricProjection getElementGeometrySourceProjection() {
			return this.shapeProjection;
		}

		@Override
		public Class<? extends RoadPolyline> getElementType() {
			return RoadPolyline.class;
		}

		@Override
		public RoadPolyline getMapElementAt(int index) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getMapElementCount() {
			return this.container.getSegmentCount();
		}

		@Override
		public String getName() {
			return this.container.getName();
		}

		@Override
		public void onMapElementGraphicalAttributeChanged() {
			//
		}

		@Override
		public boolean removeMapElement(MapElement element) {
			if (element instanceof RoadSegment) {
				return this.container.removeRoadSegment((RoadSegment) element);
			}
			return false;
		}

		@Override
		public boolean removeAllMapElements() {
			return this.container.clear();
		}

		@Override
		public void setElementAttributeSourceURL(URL url) {
			this.attributeURL = url;
		}

		@Override
		public void setElementGeometrySource(URL url, MapMetricProjection mapProjection) {
			this.shapeURL = url;
			this.shapeProjection = mapProjection;
		}

		@Override
		public Rectangle2d getBoundingBox() {
			return this.container.getBoundingBox();
		}

		@Override
		public int getColor() {
			try {
				return this.container.getAttributeAsInt(MapElement.ATTR_COLOR);
			} catch (AttributeException e) {
				return 0;
			}
		}

		@Override
		public void resetBoundingBox() {
			this.container.resetBoundingBox();
		}

		@Override
		public int size() {
			return this.container.getSegmentCount();
		}

		@Override
		public Iterator<RoadPolyline> iterator() {
			return Iterators.filter(this.container.iterator(), RoadPolyline.class);
		}

		@Override
		public Iterator<RoadPolyline> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
			return Iterators.filter(this.container.iterator(bounds), RoadPolyline.class);
		}

		@Override
		public void bindChangeListener(ChangeListener listener) {
			throw new UnsupportedOperationException();
		}

	}

}
