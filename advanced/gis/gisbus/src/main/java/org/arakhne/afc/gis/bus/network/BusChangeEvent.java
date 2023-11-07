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

package org.arakhne.afc.gis.bus.network;

import java.util.EventObject;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.road.primitive.RoadSegment;

/**
 * Describes an event in a bus network. The source of the event is always the network.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class BusChangeEvent extends EventObject {

	private static final long serialVersionUID = -8957499234869650373L;

	private final BusChangeEventType type;

	private final Object subObject;

	private final int index;

	private final String propertyName;

	private final Object oldValue;

	private final Object newValue;

	/** Constructor.
	 * @param source is the source of the event.
	 * @param type is the type of the event.
	 * @param subObject is a changed subobject.
	 * @param index is the index of the subObject in its parent.
	 * @param propertyName is the name of the changed property, or {@code null}
	 *     if no property changed.
	 * @param oldValue is the old value of the changed property.
	 * @param newValue is the new value of the changed property.
	 */
	public BusChangeEvent(AbstractBusPrimitive<?> source, BusChangeEventType type,
			Object subObject, int index,
			String propertyName,
			Object oldValue, Object newValue) {
		super(source);
		assert type != null;
		this.subObject = subObject;
		this.type = type;
		this.index = index;
		this.propertyName = propertyName;
		if (this.propertyName == null || "".equals(this.propertyName)) { //$NON-NLS-1$
			this.oldValue = null;
			this.newValue = null;
		} else {
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
	}

	/** Replies the type of the event.
	 *
	 * @return the type of the event, never {@code null}.
	 */
	@Pure
	public BusChangeEventType getEventType() {
		return this.type;
	}

	private <T> T getChangedObject(Class<T> requestedType) {
		if (requestedType.isInstance(this.subObject)) {
			return requestedType.cast(this.subObject);
		}
		return null;
	}

	/** Replies the changed object.
	 *
	 * @return the changed object or {@code null}.
	 */
	@Pure
	public Object getChangedObject() {
		return getObject(Object.class);
	}

	private <T> T getSourceObject(Class<T> requestedType) {
		final Object eventSource = getSource();
		if (requestedType.isInstance(eventSource)) {
			return requestedType.cast(eventSource);
		}
		return null;
	}

	private <T> T getObject(Class<T> requestedType) {
		T obj = getChangedObject(requestedType);
		if (obj == null) {
			obj = getSourceObject(requestedType);
		}
		return obj;
	}

	/** Replies the bus network.
	 *
	 * @return the bus network or {@code null}.
	 */
	@Pure
	public BusNetwork getBusNetwork() {
		return getObject(BusNetwork.class);
	}

	/** Replies the bus primitive.
	 *
	 * @return the bus primitive or {@code null}.
	 */
	@Pure
	public BusPrimitive<?> getBusPrimitive() {
		return getObject(BusPrimitive.class);
	}

	/** Replies the bus line.
	 *
	 * @return the bus line or {@code null}.
	 */
	@Pure
	public BusLine getBusLine() {
		return getObject(BusLine.class);
	}

	/** Replies the bus hub.
	 *
	 * @return the bus hub or {@code null}.
	 */
	@Pure
	public BusHub getBusHub() {
		return getObject(BusHub.class);
	}

	/** Replies the bus halt which is the source of this event.
	 *
	 * @return the bus halt or {@code null}.
	 */
	@Pure
	public BusItineraryHalt getBusHalt() {
		return getObject(BusItineraryHalt.class);
	}

	/** Replies the bus stop which is the source of this event.
	 *
	 * @return the bus stop or {@code null}.
	 */
	@Pure
	public BusStop getBusStop() {
		return getObject(BusStop.class);
	}

	/** Replies the bus itinerary  which is the source of this event.
	 *
	 * @return the bus itinerary or {@code null}.
	 */
	@Pure
	public BusItinerary getBusItinerary() {
		return getObject(BusItinerary.class);
	}

	/** Replies the bus itinerary  which is the source of this event.
	 *
	 * @return the bus itinerary or {@code null}.
	 */
	@Pure
	public RoadSegment getRoadSegment() {
		return getObject(RoadSegment.class);
	}

	/**
	 * Describes the type of event.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	public enum BusChangeEventType {
		/**
		 * The validity of an element has changed.
		 */
		VALIDITY,
		/**
		 * Change in a primitive not catched by
		 * one of the other event types.
		 */
		PRIMITIVE_CHANGED,
		/**
		 * Addition of a primitive not catched by
		 * one of the other event types.
		 */
		PRIMITIVE_ADDED,
		/**
		 * Removal of a primitive not catched by
		 * one of the other event types.
		 */
		PRIMITIVE_REMOVED,
		/**
		 * A bus line was added into a bus network.
		 */
		LINE_ADDED,
		/**
		 * A bus line has changed.
		 */
		LINE_CHANGED,
		/**
		 * A bus line was removed from a bus network.
		 */
		LINE_REMOVED,
		/**
		 * A bus itinerary was added into a bus line.
		 */
		ITINERARY_ADDED,
		/**
		 * A bus itinerary was removed from a bus line.
		 */
		ITINERARY_REMOVED,
		/**
		 * The order of the bus halts in an
		 * itinerary is inverted.
		 */
		ITINERARY_INVERTED,
		/**
		 * A bus itinerary has changed.
		 */
		ITINERARY_CHANGED,
		/**
		 * A bus halt was added into a bus itinerary.
		 */
		ITINERARY_HALT_ADDED,
		/**
		 * A bus halt was removed from a bus itinerary.
		 */
		ITINERARY_HALT_REMOVED,
		/**
		 * A bus halt has changed.
		 */
		ITINERARY_HALT_CHANGED,
		/**
		 * A bus hub was added into a bus network.
		 */
		HUB_ADDED,
		/**
		 * A bus hub was removed from a bus network.
		 */
		HUB_REMOVED,
		/**
		 * A bus hub has changed.
		 */
		HUB_CHANGED,
		/**
		 * A bus stop was added into a bus network.
		 */
		STOP_ADDED,
		/**
		 * A bus stop was removed from a bus network.
		 */
		STOP_REMOVED,
		/**
		 * A bus stop has changed.
		 */
		STOP_CHANGED,
		/**
		 * A road segment was added into a bus itinerary.
		 */
		SEGMENT_ADDED,
		/**
		 * A road segment was removed from a bus itinerary.
		 */
		SEGMENT_REMOVED,
		/**
		 * Something change in a bus network.
		 * This type of event is thrown when it is not
		 * related to another type of event.
		 */
		NETWORK_CHANGED,
		/**
		 * All the bus halts were removed from a bus itinerary.
		 */
		ALL_ITINERARY_HALTS_REMOVED,
		/**
		 * All the road segments were removed from a bus itinerary.
		 */
		ALL_SEGMENTS_REMOVED,
		/**
		 * All the bus itineraries were removed from a bus line.
		 */
		ALL_ITINERARIES_REMOVED,
		/**
		 * All the bus lines were removed from a bus network.
		 */
		ALL_LINES_REMOVED,
		/**
		 * All the bus hubs were removed from a bus network.
		 */
		ALL_HUBS_REMOVED,
		/**
		 * All the bus stops were removed from a bus network.
		 */
		ALL_STOPS_REMOVED;

		/** Replies the addition type according to the given type.
		 *
		 * @param type the type.
		 * @return the event type or {@code null}
		 */
		@Pure
		static BusChangeEventType addition(Class<?> type) {
			if (BusHub.class.isAssignableFrom(type)) {
				return HUB_ADDED;
			}
			if (BusStop.class.isAssignableFrom(type)) {
				return STOP_ADDED;
			}
			if (BusLine.class.isAssignableFrom(type)) {
				return LINE_ADDED;
			}
			if (BusItinerary.class.isAssignableFrom(type)) {
				return ITINERARY_ADDED;
			}
			if (BusItineraryHalt.class.isAssignableFrom(type)) {
				return ITINERARY_HALT_ADDED;
			}
			if (RoadSegment.class.isAssignableFrom(type)) {
				return SEGMENT_ADDED;
			}
			return PRIMITIVE_ADDED;
		}

		/** Replies the removal type according to the given type.
		 *
		 * @param type the type.
		 * @return the event type or {@code null}
		 */
		@Pure
		static BusChangeEventType removal(Class<?> type) {
			if (BusHub.class.isAssignableFrom(type)) {
				return HUB_REMOVED;
			}
			if (BusStop.class.isAssignableFrom(type)) {
				return STOP_REMOVED;
			}
			if (BusLine.class.isAssignableFrom(type)) {
				return LINE_REMOVED;
			}
			if (BusItinerary.class.isAssignableFrom(type)) {
				return ITINERARY_REMOVED;
			}
			if (BusItineraryHalt.class.isAssignableFrom(type)) {
				return ITINERARY_HALT_REMOVED;
			}
			if (RoadSegment.class.isAssignableFrom(type)) {
				return SEGMENT_REMOVED;
			}
			return PRIMITIVE_REMOVED;
		}

		/** Replies the change type according to the given type.
		 *
		 * @param type the type.
		 * @return the event type or {@code null}
		 */
		@Pure
		static BusChangeEventType change(Class<?> type) {
			if (BusNetwork.class.isAssignableFrom(type)) {
				return NETWORK_CHANGED;
			}
			if (BusHub.class.isAssignableFrom(type)) {
				return HUB_CHANGED;
			}
			if (BusStop.class.isAssignableFrom(type)) {
				return STOP_CHANGED;
			}
			if (BusLine.class.isAssignableFrom(type)) {
				return LINE_CHANGED;
			}
			if (BusItinerary.class.isAssignableFrom(type)) {
				return ITINERARY_CHANGED;
			}
			if (BusItineraryHalt.class.isAssignableFrom(type)) {
				return ITINERARY_HALT_CHANGED;
			}
			return PRIMITIVE_CHANGED;
		}

	}

	@Override
	@Pure
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append(getClass().getName());
		b.append("[source="); //$NON-NLS-1$
		b.append(getSource());
		b.append("; type="); //$NON-NLS-1$
		b.append(getEventType());
		b.append("; primitive="); //$NON-NLS-1$
		b.append(getBusPrimitive());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

	/** Replies if the index of the event-embedded object in its parent/container.
	 *
	 * @return if the index of the event-embedded object in its parent/container.
	 */
	@Pure
	public int getIndex() {
		return this.index;
	}

	/** Replies the name of the changed property.
	 *
	 * @return the property name or {@code null} if no property has changed.
	 */
	@Pure
	public String getPropertyName() {
		return this.propertyName;
	}

	/** Replies the old value of the property that has changed.
	 *
	 * @return the old value of the property or {@code null} if
	 * {@link #getPropertyName} replies {@code null}.
	 */
	public Object getOldPropertyValue() {
		return this.oldValue;
	}

	/** Replies the new value of the property that has changed.
	 *
	 * @return the new value of the property or {@code null} if
	 * {@link #getPropertyName} replies {@code null}.
	 */
	@Pure
	public Object getNewPropertyValue() {
		return this.newValue;
	}

}
