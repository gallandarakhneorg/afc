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

package org.arakhne.afc.gis;

import java.io.InputStream;
import java.util.UUID;

import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeNotInitializedException;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.InvalidAttributeTypeException;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.io.shape.AbstractShapeFileReader;
import org.arakhne.afc.io.shape.ESRIPoint;
import org.arakhne.afc.io.shape.ShapeMultiPatchType;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class TestGISReader extends AbstractShapeFileReader<MapElement> {

	/** Name of the attribute which is containing the unique identifier
	 * of a map element.
	 */
	public static final String UUID_ATTR = "UUID"; //$NON-NLS-1$

	/** Name of the attribute which is containing the identifier
	 * of a map element.
	 */
	public static final String ID_ATTR = "ID"; //$NON-NLS-1$

	/**
	 * @param is
	 */
	public TestGISReader(InputStream is) {
		super(is, null, null);
	}

	/** Extract the UUID from the attributes.
	 *
	 * @param provider is a provider of attributes
	 * @return the uuid if found or <code>null</code> if none.
	 */
	protected static UUID extractUUID(AttributeCollection provider) {
		AttributeValue value;

		value = provider.getAttribute(UUID_ATTR);
		if (value!=null) {
			try {
				return value.getUUID();
			}
			catch (InvalidAttributeTypeException e) {
				//
			}
			catch (AttributeNotInitializedException e) {
				//
			}
		}

		value = provider.getAttribute(ID_ATTR);
		if (value!=null) {
			try {
				return value.getUUID();
			}
			catch (InvalidAttributeTypeException e) {
				//
			}
			catch (AttributeNotInitializedException e) {
				//
			}
		}

		return null;
	}

	@Override
	protected AttributeCollection createAttributeCollection(int elementIndex) {
		return new HeapAttributeCollection();
	}

	@Override
	protected MapElement createMultiPoint(AttributeCollection provider, int shapeIndex, ESRIPoint[] points, boolean hasZ) {
		UUID id = extractUUID(provider);
		MapMultiPoint elt = new MapMultiPoint(id, provider);
		for(ESRIPoint p : points) {
			elt.addPoint(p.getX(), p.getY());
		}
		return elt;
	}

	@Override
	protected MapElement createPoint(AttributeCollection provider, int shape_index, ESRIPoint point) {
		UUID id = extractUUID(provider);
		return new MapPoint(id, provider, point.getX(), point.getY());
	}

	@Override
	protected MapElement createPolygon(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
		UUID id = extractUUID(provider);
		MapPolygon elt = new MapPolygon(id, provider);

		int start,end;
		for(int i=0; i<parts.length; ++i) {
			start = parts[i];
			end = ((i==parts.length-1) ? points.length : parts[i+1]) - 1;
			elt.addGroup(points[start].getX(), points[start].getY());

			for(int j=parts[i]+1; j<=end; ++j) {
				elt.addPoint(points[j].getX(), points[j].getY());
			}
		}

		return elt;
	}

	@Override
	protected MapElement createPolyline(AttributeCollection provider, int shapeIndex, int[] parts, ESRIPoint[] points, boolean hasZ) {
		UUID id = extractUUID(provider);

		MapPolyline elt = new MapPolyline(id, provider);

		int start,end;
		for(int i=0; i<parts.length; ++i) {
			start = parts[i];
			end = ((i==parts.length-1) ? points.length : parts[i+1]) - 1;
			elt.addGroup(points[start].getX(), points[start].getY());

			for(int j=parts[i]+1; j<=end; ++j) {
				elt.addPoint(points[j].getX(), points[j].getY());
			}
		}

		return elt;
	}

	@Override
	protected MapElement createMultiPatch(AttributeCollection provider, int shapeIndex, int[] parts, ShapeMultiPatchType[] partTypes, ESRIPoint[] points) {
		return null;
	}

	@Override
	protected void putAttributeIn(MapElement element, String attributeName, AttributeValue value) {
		try {
			element.setAttribute(attributeName, value);
		}
		catch (AttributeException e) {
			throw new RuntimeException(e);
		}
	}

}
