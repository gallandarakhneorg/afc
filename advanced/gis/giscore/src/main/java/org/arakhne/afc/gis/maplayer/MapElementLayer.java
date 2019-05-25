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

package org.arakhne.afc.gis.maplayer;

import java.net.URL;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.coordinate.MapMetricProjection;
import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.primitive.ChangeListener;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class represents a layer that contains map elements.
 *
 * @param <E> is the type of the elements inside this layer.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class MapElementLayer<E extends MapElement> extends MapLayer implements GISElementContainer<E> {

	/** Name of the attribute which is containing the URL
	 * of the source of the element geometries.
	 */
	public static final String ATTR_ELEMENT_GEOMETRY_URL = "elementGeometryUrl"; //$NON-NLS-1$

	/** Name of the attribute which is containing the type of map
	 * projection for the source file of the element geometries.
	 * @since 4.1
	 */
	public static final String ATTR_ELEMENT_GEOMETRY_PROJECTION = "elementGeometryProjection"; //$NON-NLS-1$

	/** Name of the attribute which is containing the URL
	 * of the source of the element attributes.
	 */
	public static final String ATTR_ELEMENT_ATTRIBUTES_URL = "elementAttributeUrl"; //$NON-NLS-1$

	private static final long serialVersionUID = -398027492247194045L;

	private transient volatile ChangeListener listener;

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @since 4.0
	 */
	public MapElementLayer(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 * @since 4.0
	 */
	public MapElementLayer(UUID id, AttributeCollection attributeSource, boolean isTemp) {
		super(id, attributeSource, isTemp);
	}

	@Override
	public void bindChangeListener(ChangeListener listener) {
		this.listener = listener;
	}

	private void fireChangeListener() {
		if (this.listener != null) {
			this.listener.changed(this);
		}
	}

	@Override
	public void fireElementChanged() {
		super.fireElementChanged();
		fireChangeListener();
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("elements", getAllMapElements()); //$NON-NLS-1$
	}

	/** Fire the event that indicates the content of this layer was changed.
	 */
	protected final void fireLayerContentChangedEvent() {
		fireLayerContentChangedEvent(new MapLayerContentEvent(this));
		fireElementChanged();
	}

	@Override
	public void onMapElementGraphicalAttributeChanged() {
		fireLayerContentChangedEvent();
	}

	@Override
	@Pure
	public URL getElementGeometrySourceURL() {
		return getAttribute(ATTR_ELEMENT_GEOMETRY_URL, (URL) null);
	}

	@Override
	@Pure
	public MapMetricProjection getElementGeometrySourceProjection() {
		return getAttribute(ATTR_ELEMENT_GEOMETRY_PROJECTION, (MapMetricProjection) null);
	}

	@Override
	public void setElementGeometrySource(URL url, MapMetricProjection mapProjection) {
		if (url == null) {
			removeAttribute(ATTR_ELEMENT_GEOMETRY_URL);
			removeAttribute(ATTR_ELEMENT_GEOMETRY_PROJECTION);
		} else {
			setAttribute(ATTR_ELEMENT_GEOMETRY_URL, url);
			if (mapProjection == null) {
				removeAttribute(ATTR_ELEMENT_GEOMETRY_PROJECTION);
			} else {
				setAttribute(ATTR_ELEMENT_GEOMETRY_PROJECTION, mapProjection);
			}
		}
	}

	@Override
	@Pure
	public URL getElementAttributeSourceURL() {
		return getAttribute(ATTR_ELEMENT_ATTRIBUTES_URL, (URL) null);
	}

	@Override
	public void setElementAttributeSourceURL(URL url) {
		if (url == null) {
			removeAttribute(ATTR_ELEMENT_ATTRIBUTES_URL);
		} else {
			setAttribute(ATTR_ELEMENT_ATTRIBUTES_URL, url);
		}
	}

}
