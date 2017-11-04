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

package org.arakhne.afc.gis.bus.layer;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.bus.network.BusChangeEvent;
import org.arakhne.afc.gis.bus.network.BusChangeListener;
import org.arakhne.afc.gis.bus.network.BusItinerary;
import org.arakhne.afc.gis.bus.network.BusLine;
import org.arakhne.afc.gis.bus.network.BusPrimitiveInvalidity;
import org.arakhne.afc.gis.maplayer.MapLayerContentEvent;
import org.arakhne.afc.gis.maplayer.MultiMapLayer;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;

/**
 * This class permits to display a bus line.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusLineLayer extends MultiMapLayer<BusItineraryLayer> implements BusLayer {

	private static final long serialVersionUID = 4731170172879143872L;

	private final BusLine busLine;

	private final AtomicBoolean autoUpdate = new AtomicBoolean(true);

	private final BusChangeListener listener;

	/** Create a new layer.
	 *
	 * @param line is the embedded line
	 */
	public BusLineLayer(BusLine line) {
		this(null, null, line, true);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param line is the embedded line
	 * @since 4.0
	 */
	public BusLineLayer(UUID id, BusLine line) {
		this(id, null, line, true);
	}

	/** Create a new layer.
	 *
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param line is the embedded line
	 */
	public BusLineLayer(AttributeCollection attributeProvider, BusLine line) {
		this(null, attributeProvider, line, true);
	}

	/** Create a new layer.
	 *
	 * @param line is the embedded line
	 * @param autoUpdate indicates if this layer is automatically updated
	 *     when the given line has changed.
	 * @since 4.0
	 */
	public BusLineLayer(BusLine line, boolean autoUpdate) {
		this(null, null, line, autoUpdate);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param line is the embedded line
	 * @param autoUpdate indicates if this layer is automatically updated
	 *     when the given line has changed.
	 * @since 4.0
	 */
	public BusLineLayer(UUID id, BusLine line, boolean autoUpdate) {
		this(id, null, line, autoUpdate);
	}

	/** Create a new layer.
	 *
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param line is the embedded line
	 * @param autoUpdate indicates if this layer is automatically updated
	 *     when the given line has changed.
	 * @since 4.0
	 */
	public BusLineLayer(AttributeCollection attributeProvider, BusLine line, boolean autoUpdate) {
		this(null, attributeProvider, line, autoUpdate);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param line is the embedded line
	 * @since 4.0
	 */
	public BusLineLayer(UUID id, AttributeCollection attributeProvider, BusLine line) {
		this(id, attributeProvider, line, true);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param line is the embedded line
	 * @param autoUpdate indicates if this layer is automatically updated
	 *     when the given line has changed.
	 * @since 4.0
	 */
	@SuppressWarnings("checkstyle:methodlength")
	public BusLineLayer(UUID id, AttributeCollection attributeProvider, BusLine line, boolean autoUpdate) {
		super(id, extractAttributeCollection(attributeProvider, line));
		assert line != null;
		this.listener = new BusChangeListener() {

			@Override
			public void onBusPrimitiveChanged(BusChangeEvent event) {
				if (event.getBusLine() == getBusLine()) {
					BusLineLayer.this.fireBusLineChanged();
				}
			}

			@Override
			public void onBusPrimitiveGraphicalAttributeChanged(
					BusChangeEvent event) {
				if (event.getBusLine() == getBusLine()) {
					BusLineLayer.this.resetBoundingBox();
					BusLineLayer.this.fireBusLineChanged();
				}
			}

			@Override
			@SuppressWarnings("checkstyle:cyclomaticcomplexity")
			public void onBusPrimitiveShapeChanged(BusChangeEvent event) {
				if (event.getBusLine() == getBusLine()) {
					BusLineLayer.this.resetBoundingBox();
					boolean fired = false;
					switch (event.getEventType()) {
					case ITINERARY_ADDED:
						fired = onBusItineraryAdded(event.getBusItinerary(), event.getIndex());
						break;
					case ITINERARY_REMOVED:
						fired = onBusItineraryRemoved(event.getBusItinerary(), event.getIndex());
						break;
					case ITINERARY_CHANGED:
						fired = onBusItineraryChanged(event.getBusItinerary(), event.getIndex());
						break;
					case ALL_ITINERARIES_REMOVED:
						fired = onAllBusItineraryRemoved();
						break;
						//$CASES-OMITTED$
					default:
					}
					if (!fired) {
						BusLineLayer.this.fireBusLineChanged();
					}
				}
			}
		};
		this.busLine = line;
		this.autoUpdate.set(autoUpdate);
		if (autoUpdate) {
			initializeElements();
		}
		this.busLine.addBusChangeListener(this.listener);
	}

	private static AttributeCollection extractAttributeCollection(AttributeCollection userDefinedProvider, BusLine line) {
		if (userDefinedProvider != null) {
			return userDefinedProvider;
		}
		if (BusLayerConstants.isAttributeExhibitable() && line != null) {
			return line.getAttributeCollection();
		}
		return null;
	}

	@Override
	@SuppressWarnings("checkstyle:nofinalizer")
	protected void finalize() throws Throwable {
		this.busLine.removeBusChangeListener(this.listener);
		super.finalize();
	}

	/** Run the initialization of the elements from the current bus line.
	 *
	 * <p>This function is invoked by the constructor of BusLineLayer
	 * if its parameter <var>autoUpdate</var> is <code>true</code>, and
	 * not invoked if this parameter is <code>false</code>.
	 */
	protected void initializeElements() {
		final BusLine line = getBusLine();
		if (line != null) {
			int i = 0;
			for (final BusItinerary itinerary : line.busItineraries()) {
				onBusItineraryAdded(itinerary, i);
				++i;
			}
		}
	}

	//--------------------------------------
	// Event functions
	//--------------------------------------

	/** Invoked when a bus itinerary was added in the attached line.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus itinerary has been added.
	 *
	 * @param itinerary is the new itinerary.
	 * @param index is the index of the bus itinerary.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	protected boolean onBusItineraryAdded(BusItinerary itinerary, int index) {
		if (this.autoUpdate.get()) {
			try {
				addMapLayer(index, new BusItineraryLayer(itinerary, isLayerAutoUpdated()));
				return true;
			} catch (Throwable exception) {
				//
			}
		}
		return false;
	}

	/** Invoked when a bus itinerary was removed from the attached line.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus itinerary has been removed.
	 *
	 * @param itinerary is the removed itinerary.
	 * @param index is the index of the bus itinerary.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	protected boolean onBusItineraryRemoved(BusItinerary itinerary, int index) {
		if (this.autoUpdate.get()) {
			try {
				removeMapLayerAt(index);
				return true;
			} catch (Throwable exception) {
				//
			}
		}
		return false;
	}

	/** Invoked when all the bus itineraries were removed from the attached line.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus itinerary has been removed.
	 *
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	protected boolean onAllBusItineraryRemoved() {
		if (this.autoUpdate.get()) {
			final boolean fired = size() > 0;
			clear();
			return fired;
		}
		return false;
	}

	/** Invoked when a bus itinerary was changed in the attached line.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus itinerary has changed.
	 *
	 * @param itinerary is the changed itinerary.
	 * @param index is the index of the bus itinerary.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	@SuppressWarnings("static-method")
	protected boolean onBusItineraryChanged(BusItinerary itinerary, int index) {
		return false;
	}

	/** Send an event to notify listeners that the bus line has changed.
	 */
	protected void fireBusLineChanged() {
		fireLayerContentChangedEvent(new MapLayerContentEvent(this));
		fireElementChanged();
	}

	//--------------------------------------
	// Getter functions
	//--------------------------------------

	/** Replies if this layer is automatically updated when
	 * a bus itinerary was removed or added.
	 *
	 * @return <code>true</code> if the layer is automatically updated,
	 * <code>false</code>.
	 */
	public boolean isLayerAutoUpdated() {
		return this.autoUpdate.get();
	}

	/** Set if this layer is automatically updated when
	 * a bus itinerary was removed or added.
	 *
	 * @param update is <code>true</code> if the layer should be automatically updated,
	 * <code>false</code>.
	 */
	public void setLayerAutoUpdated(boolean update) {
		this.autoUpdate.set(update);
	}

	@Override
	public String getName() {
		return this.busLine.getName();
	}

	@Override
	public Integer getRawColor() {
		return this.busLine.getRawColor();
	}

	@Override
	public int getColor() {
		return this.busLine.getColor();
	}

	@Override
	public void setName(String name) {
		this.busLine.setName(name);
	}

	@Override
	public void setColor(int color) {
		this.busLine.setColor(color);
	}

	@Override
	protected Rectangle2d calcBounds() {
		return this.busLine.getBoundingBox();
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

	@Override
	public void resetBoundingBox() {
		super.resetBoundingBox();
		this.busLine.resetBoundingBox();
	}

	/** Replies the bus line displayed by this layer.
	 *
	 * @return the bus line displayed by this layer.
	 */
	public BusLine getBusLine() {
		return this.busLine;
	}

	@Override
	public boolean isValidLayer() {
		final BusLine line = getBusLine();
		if (line != null) {
			return line.isValidPrimitive();
		}
		return false;
	}

	@Override
	public BusPrimitiveInvalidity getInvalidityReason() {
		final BusLine line = getBusLine();
		if (line != null) {
			return line.getInvalidityReason();
		}
		return BusPrimitiveInvalidity.GENERIC_INVALIDITY;
	}

	@Override
	public void revalidate() {
		final BusLine line = getBusLine();
		if (line != null) {
			line.revalidate();
		}
	}

}
