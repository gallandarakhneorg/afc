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

package org.arakhne.afc.gis.io.shape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ShapeElementType;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Group of map elements.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapElementGroup {

	/**
	 * Elements in the group.
	 */
	@SuppressWarnings("checkstyle:visibilitymodifier")
	public final Collection<MapElement> elements;

	private double minx;

	private double miny;

	private double maxx;

	private double maxy;

	/** Constructor.
	 */
	public MapElementGroup() {
		this.elements = new ArrayList<>();
		this.minx = Double.NaN;
		this.maxx = Double.NaN;
		this.miny = Double.NaN;
		this.maxy = Double.NaN;
	}

	/** Replies the shape type that is corresponding to the given element.
	 *
	 * @param element the element to classify.
	 * @return the type of the element
	 */
	@Pure
	public static ShapeElementType classifiesElement(MapElement element) {
		final Class<? extends MapElement> type = element.getClass();
		if (MapMultiPoint.class.isAssignableFrom(type)) {
			return ShapeElementType.MULTIPOINT;
		}
		if (MapPolygon.class.isAssignableFrom(type)) {
			return ShapeElementType.POLYGON;
		}
		if (MapPolyline.class.isAssignableFrom(type)) {
			return ShapeElementType.POLYLINE;
		}
		if (MapPoint.class.isAssignableFrom(type)) {
			return ShapeElementType.POINT;
		}
		return ShapeElementType.UNSUPPORTED;
	}

	/** Replies the shape types that are corresponding to the given elements.
	 *
	 * @param elements the element to classify.
	 * @return the types and the associated elements
	 */
	@Pure
	public static Map<ShapeElementType, MapElementGroup> classifiesElements(Collection<? extends MapElement> elements) {
		return classifiesElements(elements.iterator());
	}

	/** Replies the shape types that are corresponding to the given elements.
	 *
	 * @param elements the elements to classify.
	 * @return the types and the associated elements
	 */
	@Pure
	public static Map<ShapeElementType, MapElementGroup> classifiesElements(Iterator<? extends MapElement> elements) {
		final Map<ShapeElementType, MapElementGroup> classification = new TreeMap<>();
		classifiesElements(classification, elements);
		return classification;
	}

	/** Replies the shape types that are corresponding to the given elements.
	 *
	 * @param classification is the map which will be filled with the classified elements.
	 * @param elements the elements to classify.
	 */
	public static void classifiesElements(Map<ShapeElementType, MapElementGroup> classification,
			Collection<? extends MapElement> elements) {
		classifiesElements(classification, elements.iterator());
	}

	/** Replies the shape types that are corresponding to the given elements.
	 *
	 * @param classification is the map which will be filled with the classified elements.
	 * @param elements the elements to classify.
	 */
	public static void classifiesElements(Map<ShapeElementType, MapElementGroup> classification,
			Iterator<? extends MapElement> elements) {
		ShapeElementType type;
		MapElementGroup group;
		MapElement element;
		while (elements.hasNext()) {
			element = elements.next();
			type = classifiesElement(element);
			if (type != ShapeElementType.UNSUPPORTED) {
				group = classification.get(type);
				if (group == null) {
					group = new MapElementGroup();
					classification.put(type, group);
				}
				group.add(element);
			}
		}
	}


	/** Add the given element into the group.
	 *
	 * @param element the element to add.
	 */
	void add(MapElement element) {
		final Rectangle2d r = element.getBoundingBox();
		if (r != null) {
			if (Double.isNaN(this.minx) || this.minx > r.getMinX()) {
				this.minx = r.getMinX();
			}
			if (Double.isNaN(this.maxx) || this.maxx < r.getMaxX()) {
				this.maxx = r.getMaxX();
			}
			if (Double.isNaN(this.miny) || this.miny > r.getMinY()) {
				this.miny = r.getMinY();
			}
			if (Double.isNaN(this.maxy) || this.maxy < r.getMaxY()) {
				this.maxy = r.getMaxY();
			}
		}
		this.elements.add(element);
	}

	/** Replies the bounds of the group.
	 *
	 * @return the bounds of the group.
	 */
	@Pure
	public ESRIBounds getBounds() {
		return new ESRIBounds(this.minx, this.maxx, this.miny, this.maxy);
	}

}
