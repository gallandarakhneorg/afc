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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.bus.network.BusChangeEvent;
import org.arakhne.afc.gis.bus.network.BusChangeListener;
import org.arakhne.afc.gis.bus.network.BusItinerary;
import org.arakhne.afc.gis.bus.network.BusItineraryHalt;
import org.arakhne.afc.gis.bus.network.BusPrimitiveInvalidity;
import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.gis.maplayer.MapLayerContentEvent;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class permits to display a bus itinerary.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusItineraryLayer extends MapLayer implements BusLayer {

	private static final long serialVersionUID = -8814149624018230169L;

	private final BusItinerary busItinerary;

	private final AtomicBoolean autoUpdate = new AtomicBoolean(true);

	private final BusChangeListener listener;

	/** Create a new layer.
	 *
	 * @param itinerary is the embedded itinerary
	 */
	public BusItineraryLayer(BusItinerary itinerary) {
		this(null, null, itinerary, true);
	}

	/** Create a new layer.
	 *
	 * @param itinerary is the embedded itinerary
	 * @param autoUpdate indicates if this layer is automatically updated
	 *     when the given itinerary has changed.
	 * @since 4.0
	 */
	public BusItineraryLayer(BusItinerary itinerary, boolean autoUpdate) {
		this(null, null, itinerary, autoUpdate);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param itinerary is the embedded itinerary
	 * @since 4.0
	 */
	public BusItineraryLayer(UUID id, BusItinerary itinerary) {
		this(id, null, itinerary, true);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param itinerary is the embedded itinerary
	 * @param autoUpdate indicates if this layer is automatically updated
	 *     when the given itinerary has changed.
	 * @since 4.0
	 */
	public BusItineraryLayer(UUID id, BusItinerary itinerary, boolean autoUpdate) {
		this(id, null, itinerary, autoUpdate);
	}

	/** Create a new layer.
	 *
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param itinerary is the itinerary to put in the layer.
	 */
	public BusItineraryLayer(AttributeCollection attributeProvider, BusItinerary itinerary) {
		this(null, attributeProvider, itinerary, true);
	}

	/** Create a new layer.
	 *
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param itinerary is the itinerary to put in the layer.
	 * @param autoUpdate indicates if this layer is automatically updated
	 *     when the given itinerary has changed.
	 * @since 4.0
	 */
	public BusItineraryLayer(AttributeCollection attributeProvider, BusItinerary itinerary, boolean autoUpdate) {
		this(null, attributeProvider, itinerary, autoUpdate);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param itinerary is the itinerary to put in the layer.
	 * @since 4.0
	 */
	public BusItineraryLayer(UUID id, AttributeCollection attributeProvider, BusItinerary itinerary) {
		this(id, attributeProvider, itinerary, true);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param itinerary is the itinerary to put in the layer.
	 * @param autoUpdate indicates if this layer is automatically updated
	 *     when the given itinerary has changed.
	 * @since 4.0
	 */
	@SuppressWarnings({"checkstyle:methodlength"})
	public BusItineraryLayer(UUID id, AttributeCollection attributeProvider, BusItinerary itinerary,
			boolean autoUpdate) {
		super(id, extractAttributeCollection(attributeProvider, itinerary));
		assert itinerary != null;
		this.listener = new BusChangeListener() {
			@Override
			public void onBusPrimitiveChanged(BusChangeEvent event) {
				BusItinerary i = event.getBusItinerary();
				if (i == null) {
					final BusItineraryHalt h = event.getBusHalt();
					if (h != null) {
						i = h.getContainer();
					}
				}
				if (i == getBusItinerary()) {
					BusItineraryLayer.this.fireBusItineraryChanged();
				}
			}

			@Override
			public void onBusPrimitiveGraphicalAttributeChanged(BusChangeEvent event) {
				BusItinerary i = event.getBusItinerary();
				if (i == null) {
					final BusItineraryHalt h = event.getBusHalt();
					if (h != null) {
						i = h.getContainer();
					}
				}
				if (i == getBusItinerary()) {
					BusItineraryLayer.this.resetBoundingBox();
					BusItineraryLayer.this.fireBusItineraryChanged();
				}
			}

			@Override
			@SuppressWarnings({"checkstyle:cyclomaticcomplexity"})
			public void onBusPrimitiveShapeChanged(BusChangeEvent event) {
				BusItinerary i = event.getBusItinerary();
				if (i == null) {
					final BusItineraryHalt h = event.getBusHalt();
					if (h != null) {
						i = h.getContainer();
					}
				}
				if (i == getBusItinerary()) {
					BusItineraryLayer.this.resetBoundingBox();
					boolean fired = false;
					switch (event.getEventType()) {
					case ITINERARY_HALT_ADDED:
						fired = onBusItineraryHaltAdded(event.getBusHalt(), event.getIndex(), event);
						break;
					case ITINERARY_HALT_REMOVED:
						fired = onBusItineraryHaltRemoved(event.getBusHalt(), event.getIndex(), event);
						break;
					case ITINERARY_HALT_CHANGED:
						fired = onBusItineraryHaltChanged(event.getBusHalt(), event.getIndex(), event);
						break;
					case ALL_ITINERARY_HALTS_REMOVED:
						fired = onAllBusItineraryHaltRemoved(event);
						break;
					case ITINERARY_INVERTED:
						fired = onBusItineraryInverted(event);
						break;
						//$CASES-OMITTED$
					default:
					}
					if (!fired) {
						BusItineraryLayer.this.fireBusItineraryChanged();
					}
				}
			}
		};
		this.busItinerary = itinerary;
		this.autoUpdate.set(autoUpdate);
		if (autoUpdate) {
			initializeElements();
		}
		this.busItinerary.addBusChangeListener(this.listener);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("itinerary", getBusItinerary()); //$NON-NLS-1$
	}

	@Pure
	private static AttributeCollection extractAttributeCollection(AttributeCollection userDefinedProvider,
			BusItinerary itinerary) {
		if (userDefinedProvider != null) {
			return userDefinedProvider;
		}
		if (BusLayerConstants.isAttributeExhibitable() && itinerary != null) {
			return itinerary.getAttributeCollection();
		}
		return null;
	}

	@Override
	@SuppressWarnings("checkstyle:nofinalizer")
	protected void finalize() throws Throwable {
		this.busItinerary.removeBusChangeListener(this.listener);
		super.finalize();
	}

	/** Run the initialization of the elements from the current bus itinerary.
	 *
	 * <p>This function is invoked by the constructor of BusItineraryLayer
	 * if its parameter <var>autoUpdate</var> is <code>true</code>, and
	 * not invoked if this parameter is <code>false</code>.
	 */
	protected void initializeElements() {
		final BusItinerary itinerary = getBusItinerary();
		if (itinerary != null) {
			int i = 0;
			for (final BusItineraryHalt halt : itinerary.busHalts()) {
				onBusItineraryHaltAdded(halt, i, null);
				++i;
			}
		}
	}

	//--------------------------------------
	// Event functions
	//--------------------------------------

	/** Invoked when a bus itinerary is inverted.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus itinerary has been inverted.
	 *
	 * @param event is the source of the event.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	@SuppressWarnings("static-method")
	protected boolean onBusItineraryInverted(BusChangeEvent event) {
		return false;
	}

	/** Invoked when a bus itinerary halt was added in the attached itinerary.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus itinerary halt has been added.
	 *
	 * @param halt is the new itinerary halt.
	 * @param index is the index of the bus halt.
	 * @param event is the source of the event.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	@SuppressWarnings("static-method")
	protected boolean onBusItineraryHaltAdded(BusItineraryHalt halt, int index, BusChangeEvent event) {
		return false;
	}

	/** Invoked when a bus itinerary halt was removed from the attached itinerary.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus itinerary halt has been removed.
	 *
	 * @param halt is the removed itinerary halt.
	 * @param index is the index of the bus halt.
	 * @param event is the source of the event.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	@SuppressWarnings("static-method")
	protected boolean onBusItineraryHaltRemoved(BusItineraryHalt halt, int index, BusChangeEvent event) {
		return false;
	}

	/** Invoked when all the bus itinerary halts were removed from the attached itinerary.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus itinerary halt has been removed.
	 *
	 * @param event is the source of the event.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	@SuppressWarnings("static-method")
	protected boolean onAllBusItineraryHaltRemoved(BusChangeEvent event) {
		return false;
	}

	/** Invoked when a bus itinerary halt was changed in the attached itinerary.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus itinerary halt has changed.
	 *
	 * @param halt is the changed itinerary halt.
	 * @param index is the index of the bus halt.
	 * @param event is the source of the event.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	@SuppressWarnings("static-method")
	protected boolean onBusItineraryHaltChanged(BusItineraryHalt halt, int index, BusChangeEvent event) {
		return false;
	}

	/** Send an event to notify listeners that the bus itinerary has changed.
	 */
	protected void fireBusItineraryChanged() {
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
	@Pure
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
	@Pure
	public String getName() {
		return this.busItinerary.getName();
	}

	@Override
	@Pure
	public Integer getRawColor() {
		return this.busItinerary.getRawColor();
	}

	@Override
	@Pure
	public int getColor() {
		return this.busItinerary.getColor();
	}

	@Override
	public void setName(String name) {
		this.busItinerary.setName(name);
	}

	@Override
	public void setColor(int color) {
		this.busItinerary.setColor(color);
	}

	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		return this.busItinerary.getBoundingBox();
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

	@Override
	public void resetBoundingBox() {
		super.resetBoundingBox();
		this.busItinerary.resetBoundingBox();
	}

	/** Replies the bus itinerary displayed by this layer.
	 *
	 * @return the bus itinerary displayed by this layer.
	 */
	@Pure
	public BusItinerary getBusItinerary() {
		return this.busItinerary;
	}

	@Override
	@Pure
	public boolean isValidLayer() {
		final BusItinerary itinerary = getBusItinerary();
		if (itinerary != null) {
			return itinerary.isValidPrimitive();
		}
		return false;
	}

	@Override
	@Pure
	public BusPrimitiveInvalidity getInvalidityReason() {
		final BusItinerary itinerary = getBusItinerary();
		if (itinerary != null) {
			return itinerary.getInvalidityReason();
		}
		return BusPrimitiveInvalidity.GENERIC_INVALIDITY;
	}

	@Override
	public void revalidate() {
		final BusItinerary itinerary = getBusItinerary();
		if (itinerary != null) {
			itinerary.revalidate();
		}
	}

}
