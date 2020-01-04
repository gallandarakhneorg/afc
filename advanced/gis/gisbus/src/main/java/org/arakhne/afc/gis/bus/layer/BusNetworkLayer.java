/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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
import org.arakhne.afc.gis.bus.network.BusLine;
import org.arakhne.afc.gis.bus.network.BusNetwork;
import org.arakhne.afc.gis.bus.network.BusPrimitiveInvalidity;
import org.arakhne.afc.gis.maplayer.MapLayerContentEvent;
import org.arakhne.afc.gis.maplayer.MultiMapLayer;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class permits to display a bus network.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusNetworkLayer extends MultiMapLayer<BusLineLayer> implements BusLayer {

	private static final long serialVersionUID = 2045535681569299323L;

	private final BusNetwork busNetwork;

	private final AtomicBoolean autoUpdate = new AtomicBoolean(true);

	private final BusChangeListener listener;

	/** Create a new layer.
	 *
	 * @param network is the embedded network
	 */
	public BusNetworkLayer(BusNetwork network) {
		this(null, null, network, true);
	}

	/** Create a new layer.
	 *
	 * @param network is the embedded network
	 * @param autoUpdate indicates if this bus network should be automatically updated
	 *     when a bus line as changed.
	 * @since 4.0
	 */
	public BusNetworkLayer(BusNetwork network, boolean autoUpdate) {
		this(null, null, network, autoUpdate);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param network is the embedded network
	 * @since 4.0
	 */
	public BusNetworkLayer(UUID id, BusNetwork network) {
		this(id, null, network, true);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param network is the embedded network
	 * @param autoUpdate indicates if this bus network should be automatically updated
	 *     when a bus line as changed.
	 * @since 4.0
	 */
	public BusNetworkLayer(UUID id, BusNetwork network, boolean autoUpdate) {
		this(id, null, network, autoUpdate);
	}

	/** Create a new layer.
	 *
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param network is the embedded network
	 */
	public BusNetworkLayer(AttributeCollection attributeProvider, BusNetwork network) {
		this(null, attributeProvider, network, true);
	}

	/** Create a new layer.
	 *
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param network is the embedded network
	 * @param autoUpdate indicates if this bus network should be automatically updated
	 *     when a bus line as changed.
	 * @since 4.0
	 */
	public BusNetworkLayer(AttributeCollection attributeProvider, BusNetwork network, boolean autoUpdate) {
		this(null, attributeProvider, network, autoUpdate);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param network is the embedded network
	 * @since 4.0
	 */
	public BusNetworkLayer(UUID id, AttributeCollection attributeProvider, BusNetwork network) {
		this(id, attributeProvider, network, true);
	}

	/** Create a new layer.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the attribute collection associated to this layer.
	 * @param network is the embedded network
	 * @param autoUpdate indicates if this bus network should be automatically updated
	 *     when a bus line as changed.
	 * @since 4.0
	 */
	@SuppressWarnings("checkstyle:methodlength")
	public BusNetworkLayer(UUID id, AttributeCollection attributeProvider, BusNetwork network, boolean autoUpdate) {
		super(id, extractAttributeCollection(attributeProvider, network));
		assert network != null;
		this.listener = new BusChangeListener() {

			@Override
			public void onBusPrimitiveChanged(BusChangeEvent event) {
				BusNetwork b = event.getBusNetwork();
				if (b == null) {
					if (event.getBusStop() != null) {
						b = event.getBusStop().getBusNetwork();
					}
				}
				if (b == getBusNetwork()) {
					BusNetworkLayer.this.fireBusNetworkChanged();
				}
			}

			@Override
			public void onBusPrimitiveGraphicalAttributeChanged(BusChangeEvent event) {
				BusNetwork b = event.getBusNetwork();
				if (b == null) {
					if (event.getBusStop() != null) {
						b = event.getBusStop().getBusNetwork();
					}
				}
				if (b == getBusNetwork()) {
					BusNetworkLayer.this.resetBoundingBox();
					BusNetworkLayer.this.fireBusNetworkChanged();
				}
			}

			@Override
			@SuppressWarnings("checkstyle:cyclomaticcomplexity")
			public void onBusPrimitiveShapeChanged(BusChangeEvent event) {
				if (event.getBusNetwork() == getBusNetwork()) {
					BusNetworkLayer.this.resetBoundingBox();
					boolean fired = false;
					switch (event.getEventType()) {
					case LINE_ADDED:
						fired = onBusLineAdded(event.getBusLine(), event.getIndex());
						break;
					case LINE_REMOVED:
						fired = onBusLineRemoved(event.getBusLine(), event.getIndex());
						break;
					case LINE_CHANGED:
						fired = onBusLineChanged(event.getBusLine(), event.getIndex());
						break;
					case ALL_LINES_REMOVED:
						fired = onAllBusLineRemoved();
						break;
						//$CASES-OMITTED$
					default:
					}
					if (!fired) {
						BusNetworkLayer.this.fireBusNetworkChanged();
					}
				}
			}
		};
		this.busNetwork = network;
		this.autoUpdate.set(autoUpdate);
		if (autoUpdate) {
			initializeElements();
		}
		this.busNetwork.addBusChangeListener(this.listener);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("line", getBusNetwork()); //$NON-NLS-1$
	}

	private static AttributeCollection extractAttributeCollection(AttributeCollection userDefinedProvider, BusNetwork network) {
		if (userDefinedProvider != null) {
			return userDefinedProvider;
		}
		if (BusLayerConstants.isAttributeExhibitable() && network != null) {
			return network.getAttributeCollection();
		}
		return null;
	}

	@Override
	@SuppressWarnings("checkstyle:nofinalizer")
	@Deprecated(since = "17.0", forRemoval = true)
	protected void finalize() throws Throwable {
		this.busNetwork.removeBusChangeListener(this.listener);
		super.finalize();
	}

	/** Run the initialization of the elements from the current bus line.
	 *
	 * <p>This function is invoked by the constructor of BusLineLayer
	 * if its parameter <var>autoUpdate</var> is <code>true</code>, and
	 * not invoked if this parameter is <code>false</code>.
	 */
	protected void initializeElements() {
		final BusNetwork network = getBusNetwork();
		if (network != null) {
			int i = 0;
			for (final BusLine line : network.busLines()) {
				onBusLineAdded(line, i);
				++i;
			}
		}
	}

	//--------------------------------------
	// Event functions
	//--------------------------------------

	/** Invoked when a bus line was added in the attached network.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus line has been added.
	 *
	 * @param line is the new line.
	 * @param index is the index of the bus line.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	protected boolean onBusLineAdded(BusLine line, int index) {
		if (this.autoUpdate.get()) {
			try {
				addMapLayer(index, new BusLineLayer(line, isLayerAutoUpdated()));
				return true;
			} catch (Throwable exception) {
				//
			}
		}
		return false;
	}

	/** Invoked when a bus line was removed from the attached network.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus line has been removed.
	 *
	 * @param line is the removed line.
	 * @param index is the index of the bus line.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	protected boolean onBusLineRemoved(BusLine line, int index) {
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

	/** Invoked when all the bus lines were removed from the attached network.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus line has been removed.
	 *
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	protected boolean onAllBusLineRemoved() {
		if (this.autoUpdate.get()) {
			final boolean fired = size() > 0;
			clear();
			return fired;
		}
		return false;
	}

	/** Invoked when a bus line was changed in the attached network.
	 *
	 * <p>This function exists to allow be override to provide a specific behaviour
	 * when a bus line has changed.
	 *
	 * @param line is the changed line.
	 * @param index is the index of the bus line.
	 * @return <code>true</code> if the events was fired, otherwise <code>false</code>.
	 */
	@SuppressWarnings("static-method")
	protected boolean onBusLineChanged(BusLine line, int index) {
		return false;
	}

	/** Send an event to notify listeners that the bus network has changed.
	 */
	protected void fireBusNetworkChanged() {
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
		return this.busNetwork.getName();
	}

	@Override
	public Integer getRawColor() {
		return this.busNetwork.getRawColor();
	}

	@Override
	public int getColor() {
		return this.busNetwork.getColor(super.getColor());
	}

	@Override
	public void setName(String name) {
		this.busNetwork.setName(name);
	}

	@Override
	public void setColor(int color) {
		this.busNetwork.setColor(color);
	}

	@Override
	protected Rectangle2d calcBounds() {
		return this.busNetwork.getBoundingBox();
	}

	@Override
	public Shape2d<?> getShape() {
		return getBoundingBox();
	}

	@Override
	public void resetBoundingBox() {
		super.resetBoundingBox();
		this.busNetwork.resetBoundingBox();
	}

	/** Replies the bus network displayed by this layer.
	 *
	 * @return the bus network displayed by this layer.
	 */
	public BusNetwork getBusNetwork() {
		return this.busNetwork;
	}

	@Override
	public boolean isValidLayer() {
		final BusNetwork network = getBusNetwork();
		if (network != null) {
			return network.isValidPrimitive();
		}
		return false;
	}

	@Override
	public BusPrimitiveInvalidity getInvalidityReason() {
		final BusNetwork network = getBusNetwork();
		if (network != null) {
			return network.getInvalidityReason();
		}
		return BusPrimitiveInvalidity.GENERIC_INVALIDITY;
	}

	@Override
	public void revalidate() {
		final BusNetwork network = getBusNetwork();
		if (network != null) {
			network.revalidate();
		}
	}

}
