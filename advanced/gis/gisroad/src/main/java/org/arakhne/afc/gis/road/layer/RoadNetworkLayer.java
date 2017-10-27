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

package org.arakhne.afc.gis.road.layer;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.coordinate.MapMetricProjection;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.maplayer.MapElementLayer;
import org.arakhne.afc.gis.road.RoadPolyline;
import org.arakhne.afc.gis.road.StandardRoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadNetworkListener;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * This class permits to display a road network.
 *
 * @author $Author: olamotte$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class RoadNetworkLayer extends MapElementLayer<RoadPolyline> {

	private static final long serialVersionUID = -1457398279058798135L;

	private StandardRoadNetwork roadNetwork;

	private RoadNetworkListener listener;

	//--------------------------------------
	// Constructors
	//--------------------------------------

	/** Create a new layer.
	 *
	 * @param network is the embedded network
	 */
	public RoadNetworkLayer(StandardRoadNetwork network) {
		this(null, null, network);
	}

	/** Create a new layer.
	 *
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param network is the embedded network
	 */
	public RoadNetworkLayer(AttributeCollection attributeProvider, StandardRoadNetwork network) {
		this(null, attributeProvider, network);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param network is the embedded network
	 * @since 4.0
	 */
	public RoadNetworkLayer(UUID id, StandardRoadNetwork network) {
		this(id, null, network);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param network is the embedded network
	 * @since 4.0
	 */
	public RoadNetworkLayer(UUID id, AttributeCollection attributeProvider, StandardRoadNetwork network) {
		super(id, attributeProvider);
		init(network);
	}

	@Override
	@SuppressWarnings("checkstyle:nofinalizer")
	protected void finalize() throws Throwable {
		this.roadNetwork.removeRoadNetworkListener(this.listener);
		for (final RoadSegment segment : this.roadNetwork) {
			if (segment instanceof MapPolyline) {
				((MapPolyline) segment).setContainer(null);
			}
		}
		this.roadNetwork.setContainer(null);
		super.finalize();
	}

	private void init(StandardRoadNetwork network) {
		this.listener = new RoadNetworkListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void onRoadSegmentAdded(RoadNetwork theNetwork, RoadSegment newSegment) {
				if (theNetwork == RoadNetworkLayer.this.roadNetwork) {
					if (newSegment instanceof MapPolyline) {
						((MapPolyline) newSegment).setContainer(RoadNetworkLayer.this);
					}
					RoadNetworkLayer.this.fireLayerContentChangedEvent();
				}
			}

			@Override
			@SuppressWarnings("synthetic-access")
			public void onRoadSegmentChanged(RoadNetwork theNetwork, RoadSegment changedSegment) {
				if (theNetwork == RoadNetworkLayer.this.roadNetwork) {
					if (changedSegment instanceof MapPolyline) {
						((MapPolyline) changedSegment).setContainer(RoadNetworkLayer.this);
					}
					RoadNetworkLayer.this.fireLayerContentChangedEvent();
				}
			}

			@Override
			@SuppressWarnings("synthetic-access")
			public void onRoadSegmentRemoved(RoadNetwork theNetwork, RoadSegment newSegment) {
				if (theNetwork == RoadNetworkLayer.this.roadNetwork) {
					if (newSegment instanceof MapPolyline) {
						((MapPolyline) newSegment).setContainer(null);
					}
					RoadNetworkLayer.this.fireLayerContentChangedEvent();
				}
			}
		};

		this.roadNetwork = network;
		this.roadNetwork.setContainer(this);
		for (final RoadSegment segment : network) {
			if (segment instanceof MapPolyline) {
				((MapPolyline) segment).setContainer(this);
			}
		}
		this.roadNetwork.addRoadNetworkListener(this.listener);

		// Be sure that the layer has the same pointers to the Shape and dBase files
		try {
			setElementGeometrySource(
					this.roadNetwork.getAttributeAsURL(ATTR_ELEMENT_GEOMETRY_URL),
					this.roadNetwork.getAttributeAsEnumeration(ATTR_ELEMENT_GEOMETRY_PROJECTION, MapMetricProjection.class));
		} catch (Throwable exception) {
			//
		}
		try {
			setElementAttributeSourceURL(
					this.roadNetwork.getAttributeAsURL(ATTR_ELEMENT_ATTRIBUTES_URL));
		} catch (Throwable exception) {
			//
		}
	}

	@Override
	public void setElementGeometrySource(URL url, MapMetricProjection mapProjection) {
		super.setElementGeometrySource(url, mapProjection);
		if (url == null) {
			this.roadNetwork.removeAttribute(ATTR_ELEMENT_GEOMETRY_URL);
		} else {
			this.roadNetwork.setAttribute(ATTR_ELEMENT_GEOMETRY_URL, url);
		}
		if (mapProjection == null) {
			this.roadNetwork.removeAttribute(ATTR_ELEMENT_GEOMETRY_PROJECTION);
		} else {
			this.roadNetwork.setAttribute(ATTR_ELEMENT_GEOMETRY_PROJECTION, mapProjection);
		}
	}

	@Override
	public void setElementAttributeSourceURL(URL url) {
		super.setElementAttributeSourceURL(url);
		if (url == null) {
			this.roadNetwork.removeAttribute(ATTR_ELEMENT_ATTRIBUTES_URL);
		} else {
			this.roadNetwork.setAttribute(ATTR_ELEMENT_ATTRIBUTES_URL, url);
		}
	}

	@Override
	@Pure
	public RoadNetworkLayer clone() {
		final RoadNetworkLayer clone = (RoadNetworkLayer) super.clone();

		StandardRoadNetwork clonedNetwork = null;
		if (this.roadNetwork != null) {
			clonedNetwork = this.roadNetwork.clone();
		}
		clone.init(clonedNetwork);
		return clone;
	}

	//--------------------------------------
	// Getter functions
	//--------------------------------------

	@Override
	@Pure
	public RoadPolyline getMapElementAt(int index) {
		final Iterator<RoadSegment> segments = this.roadNetwork.iterator();
		for (int i = 0; i < index - 1 && segments.hasNext(); ++i) {
			segments.next();
		}
		if (segments.hasNext()) {
			return (RoadPolyline) segments.next();
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	@Pure
	public Class<? extends RoadPolyline> getElementType() {
		return RoadPolyline.class;
	}

	/** Replies the road network displayed by this layer.
	 *
	 * @return the road network displayed by this layer.
	 */
	@Pure
	public RoadNetwork getRoadNetwork() {
		return this.roadNetwork;
	}

	@Override
	@Pure
	public Rectangle2d getBoundingBox() {
		return this.roadNetwork.getBoundingBox();
	}

	@Override
	public Shape2d<?> getShape() {
		return this.roadNetwork.getShape();
	}

	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		return this.roadNetwork.getBoundingBox();
	}

	//--------------------------------------
	// MapLayer functions
	//--------------------------------------

	@Override
	@Pure
	public String getName() {
		String name = this.roadNetwork.getName();
		if (name != null && !"".equals(name)) { //$NON-NLS-1$
			return name;
		}
		name = super.getName();
		if (name != null && !"".equals(name)) { //$NON-NLS-1$
			return name;
		}
		return Locale.getString("NAME_TEMPLATE"); //$NON-NLS-1$
	}

	//--------------------------------------
	// AbstractMapElementLayer functions
	//--------------------------------------

	@Override
	@Pure
	public int size() {
		return this.roadNetwork.getSegmentCount();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public Collection<RoadPolyline> getAllMapElements() {
		return (Collection<RoadPolyline>) this.roadNetwork.getRoadSegments();
	}

	@Override
	@Pure
	public int getMapElementCount() {
		return this.roadNetwork.getSegmentCount();
	}

	@Override
	public boolean addMapElements(Collection<? extends RoadPolyline> segments) {
		boolean changed = false;
		for (final RoadPolyline segment : segments) {
			try {
				this.roadNetwork.addRoadSegment(segment);
				segment.setContainer(this);
				changed = true;
			} catch (RoadNetworkException e) {
				//
			}
		}
		if (changed) {
			resetBoundingBox();
			fireLayerContentChangedEvent();
		}
		return changed;
	}

	@Override
	public boolean addMapElement(RoadPolyline segment) {
		try {
			this.roadNetwork.addRoadSegment(segment);
			segment.setContainer(this);
			resetBoundingBox();
			fireLayerContentChangedEvent();
			return true;
		} catch (RoadNetworkException e) {
			//
		}
		return false;
	}

	@Override
	public boolean removeMapElement(MapElement segment) {
		if (segment instanceof RoadPolyline) {
			if (this.roadNetwork.removeRoadSegment((RoadPolyline) segment)) {
				segment.setContainer(null);
				resetBoundingBox();
				fireLayerContentChangedEvent();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAllMapElements() {
		if (!this.roadNetwork.isEmpty()) {
			final Iterator<RoadSegment> iterator = this.roadNetwork.iterator();
			RoadSegment segment;
			RoadPolyline road;
			boolean hasChanged = false;
			while (iterator.hasNext()) {
				segment = iterator.next();
				if (segment instanceof RoadPolyline) {
					road = (RoadPolyline) segment;
					road.setContainer(null);
					iterator.remove();
					hasChanged = true;
				}
			}
			if (hasChanged) {
				resetBoundingBox();
				fireLayerContentChangedEvent();
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public Iterator<RoadPolyline> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return (Iterator<RoadPolyline>) this.roadNetwork.iterator(bounds);
	}

	@Override
	@Pure
	public Iterator<RoadPolyline> iterator() {
		return new IteratorWrapper(this.roadNetwork.iterator());
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class IteratorWrapper implements Iterator<RoadPolyline> {

		private final Iterator<RoadSegment> iterator;

		private RoadPolyline lastReplied;

		IteratorWrapper(Iterator<RoadSegment> iterator) {
			this.iterator = iterator;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public RoadPolyline next() {
			this.lastReplied = (RoadPolyline) this.iterator.next();
			return this.lastReplied;
		}

		@SuppressWarnings("synthetic-access")
		@Override
		public void remove() {
			final RoadPolyline removed = this.lastReplied;
			this.lastReplied = null;
			this.iterator.remove();
			if (removed == null) {
				throw new NoSuchElementException();
			}
			removed.setContainer(null);
			resetBoundingBox();
			fireLayerContentChangedEvent();
		}

	} /* class IteratorWrapper */

}
