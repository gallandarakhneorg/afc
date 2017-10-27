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

package org.arakhne.afc.gis.bus.network;

import java.lang.ref.WeakReference;
import java.util.EventListener;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent;
import org.arakhne.afc.attrs.collection.AttributeChangeListener;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationArea;
import org.arakhne.afc.gis.location.GeoLocationNowhere;
import org.arakhne.afc.gis.primitive.AbstractBoundedGISElement;
import org.arakhne.afc.gis.primitive.GISEditableChangeListener;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.ListenerCollection;

/**
 * A bus stop is a specific stop of an itinerary. It is situated on a
 * road segment.
 * <h3>Validation</h3>
 * Validity state of the bus primitive depends on the type of that primitive.
 * By default, all bus primitives are invalid. They must implement
 * the {@link #checkPrimitiveValidity()} to verify if the current validity
 * state is correct and change it if necessary.
 *
 * @param <CONTAINER> is the type of the object which could contains a instance of this class.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractBusPrimitive<CONTAINER extends BusContainer<?>>
		extends AbstractBoundedGISElement<CONTAINER, AbstractBusPrimitive<CONTAINER>>
		implements BusPrimitive<CONTAINER> {

	/** Attribute: color of the element.
	 */
	public static final String ATTR_COLOR = "color"; //$NON-NLS-1$

	/** Default color of a map element.
	 */
	public static final int DEFAULT_COLOR = 0x000000;

	private static final long serialVersionUID = 50808872176874668L;

	private final AttributeListener attributeListener;

	private ListenerCollection<EventListener> listeners;

	/** Flag associated to this element.
	 */
	private int flag;

	/** Reason of the invalidity.
	 */
	private BusPrimitiveInvalidity invalidityReason =
			new BusPrimitiveInvalidity(
					BusPrimitiveInvalidityType.VALIDITY_NOT_CHECKED,
					null);

	//------------------------------------------------------------------
	// Constructors
	//------------------------------------------------------------------

	/**
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the provider of attributes used by this bus stop.
	 * @since 2.0
	 */
	AbstractBusPrimitive(UUID id, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
		this.attributeListener = new AttributeListener(this);
		getAttributeCollection().addAttributeChangeListener(this.attributeListener);
	}

	@Override
	@SuppressWarnings("checkstyle:nofinalizer")
	protected void finalize() throws Throwable {
		getAttributeCollection().removeAttributeChangeListener(this.attributeListener);
		super.finalize();
	}

	//------------------------------------------------------------------
	// Feedback
	//------------------------------------------------------------------

	/** This function is invoked by the attribute provider each time
	 * an attribute has changed.
	 *
	 * <p>You should override this method to provide several feedback
	 * to the BusPrimitive for instance.
	 *
	 * @param name is the name of the attribute that changed
	 */
	protected void onAttributeChanged(String name) {
		Object v = null;
		try {
			v = getAttribute(name).getValue();
		} catch (Exception exception) {
			//
		}
		if (ATTR_COLOR.equalsIgnoreCase(name)) {
			fireGraphicalAttributeChanged(name, null, v);
		} else {
			firePrimitiveChanged(name, null, v);
		}
	}

	//------------------------------------------------------------------
	// Listener API
	//------------------------------------------------------------------

	/** Add a listener.
	 *
	 * @param <T> is the type of listener to add.
	 * @param listenerType is the type of listener to add.
	 * @param listener is the new listener.
	 */
	protected final <T extends EventListener> void addListener(Class<T> listenerType, T listener) {
		if (this.listeners == null) {
			this.listeners = new ListenerCollection<>();
		}
		this.listeners.add(listenerType, listener);
	}

	/** Remove a listener.
	 *
	 * @param <T> is the type of listener to add.
	 * @param listenerType is the type of listener to remove.
	 * @param listener is the new listener.
	 */
	protected final <T extends EventListener> void removeListener(Class<T> listenerType, T listener) {
		if (this.listeners != null) {
			this.listeners.remove(listenerType, listener);
			if (this.listeners.isEmpty()) {
				this.listeners = null;
			}
		}
	}

	@Override
	public void addGISEditableChangeListener(GISEditableChangeListener listener) {
		addListener(GISEditableChangeListener.class, listener);
	}

	@Override
	public void removeGISEditableChangeListener(GISEditableChangeListener listener) {
		removeListener(GISEditableChangeListener.class, listener);
	}

	@Override
	public void addBusChangeListener(BusChangeListener listener) {
		addListener(BusChangeListener.class, listener);
	}

	@Override
	public void removeBusChangeListener(BusChangeListener listener) {
		removeListener(BusChangeListener.class, listener);
	}

	/** Replies the index of this primitive in its parent.
	 *
	 * @return the index of this primitive in its parent.
	 */
	@Pure
	public abstract int indexInParent();

	/** Fire the event that indicates this object has changed.
	 */
	protected final void firePrimitiveChanged() {
		final BusChangeEvent event = new BusChangeEvent(
				// source of the event
				this,
				// type of the event
				BusChangeEventType.change(getClass()),
				// subobject
				this,
				// index in parent
				indexInParent(),
				// propertyName
				null,
				// old property value
				null,
				// new property value
				null);
		firePrimitiveChanged(event);
	}

	/** Fire the event that indicates this object has changed.
	 *
	 * @param propertyName is the name of the graphical property.
	 * @param oldValue is the old value of the property.
	 * @param newValue is the new value of the property.
	 */
	protected final void firePrimitiveChanged(String propertyName, Object oldValue, Object newValue) {
		final BusChangeEvent event = new BusChangeEvent(
				// source of the event
				this,
				// type of the event
				BusChangeEventType.change(getClass()),
				// subobject
				this,
				// index in parent
				indexInParent(),
				propertyName,
				oldValue,
				newValue);
		firePrimitiveChanged(event);
	}

	@Override
	public void firePrimitiveChanged(BusChangeEvent event) {
		if (isEventFirable()) {
			final BusContainer<?> container = getContainer();
			if (container != null) {
				container.onBusPrimitiveChanged(event);
			}
			if (this.listeners != null) {
				final GISEditableChangeListener[] theListeners = this.listeners.getListeners(GISEditableChangeListener.class);
				for (final GISEditableChangeListener listener : theListeners) {
					listener.editableGISElementHasChanged(event.getBusPrimitive());
				}
				final BusChangeListener[] llisteners = this.listeners.getListeners(BusChangeListener.class);
				for (final BusChangeListener listener : llisteners) {
					listener.onBusPrimitiveChanged(event);
				}
			}
		}
	}

	/** Fire the event that indicates the validity of the object has changed.
	 *
	 * @param oldReason is the old invalidity reason before the change.
	 * @param newReason is the current invalidity reason after the change.
	 */
	protected final void fireValidityChanged(BusPrimitiveInvalidity oldReason, BusPrimitiveInvalidity newReason) {
		fireValidityChangedFor(this, indexInParent(), oldReason, newReason);
	}

	/** Fire the event that indicates the validity of the object has changed.
	 *
	 * @param changedObject is the object for which the validity has changed.
	 * @param index is the index of the changedObject in its parent.
	 * @param oldReason is the old invalidity reason before the change.
	 * @param newReason is the current invalidity reason after the change.
	 */
	protected void fireValidityChangedFor(Object changedObject, int index, BusPrimitiveInvalidity oldReason,
			BusPrimitiveInvalidity newReason) {
		resetBoundingBox();
		final BusChangeEvent event = new BusChangeEvent(
				// source of the event
				this,
				// type of the event
				BusChangeEventType.VALIDITY,
				changedObject,
				index,
				"validity", //$NON-NLS-1$
				oldReason,
				newReason);
		firePrimitiveChanged(event);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void fireGraphicalAttributeChanged(String propertyName, Object oldValue, Object newValue) {
		final BusChangeEvent event = new BusChangeEvent(this,
				BusChangeEventType.change(getClass()), this,
				indexInParent(),
				propertyName,
				oldValue,
				newValue);
		fireGraphicalAttributeChanged(event);
	}

	@Override
	public void fireGraphicalAttributeChanged(BusChangeEvent event) {
		if (isEventFirable()) {
			final BusContainer<?> container = getContainer();
			if (container != null) {
				container.onBusPrimitiveGraphicalAttributeChanged(event);
			}
			if (this.listeners != null) {
				final BusChangeListener[] llisteners = this.listeners.getListeners(BusChangeListener.class);
				for (final BusChangeListener listener : llisteners) {
					listener.onBusPrimitiveGraphicalAttributeChanged(event);
				}
			}
		}
	}

	@Override
	public void fireShapeChanged() {
		final BusChangeEvent event = new BusChangeEvent(this,
				BusChangeEventType.change(getClass()), this,
				indexInParent(),
				//property name
				"shape", //$NON-NLS-1$
				// old property value
				null,
				// new property value
				null);
		fireShapeChanged(event);
	}

	@Override
	public void fireShapeChanged(BusChangeEvent event) {
		resetBoundingBox();
		if (isEventFirable()) {
			final BusContainer<?> container = getContainer();
			if (container != null) {
				container.onBusPrimitiveShapeChanged(event);
			}
			if (this.listeners != null) {
				final BusChangeListener[] llisteners = this.listeners.getListeners(BusChangeListener.class);
				for (final BusChangeListener listener : llisteners) {
					listener.onBusPrimitiveShapeChanged(event);
				}
			}
		}
	}

	//------------------------------------------------------------------
	// GIS element API
	//------------------------------------------------------------------

	@Override
	public void setUUID(UUID id) {
		assert id != null;
		final UUID old = getUUID();
		if (!id.equals(old)) {
			super.setUUID(id);
			firePrimitiveChanged();
		}
	}

	/** Replies a string representation of this bus stop.
	 */
	@Override
	@Pure
	public String toString() {
		return getName();
	}

	@Override
	@Pure
	public GeoLocation getGeoLocation() {
		final Rectangle2d bounds = getBoundingBox();
		if (bounds == null) {
			return new GeoLocationNowhere(getUUID());
		}
		return new GeoLocationArea(bounds);
	}

	/**
	 * Replies the color of this element or the color of the container.
	 *
	 * @return the color
	 * @see #getRawColor()
	 */
	@Override
	@Pure
	public int getColor() {
		return getColor(DEFAULT_COLOR);
	}

	/**
	 * Replies the color of this element or the color of the container.
	 *
	 * @param defaultColor the default color.
	 * @return the color
	 * @see #getRawColor()
	 */
	@Pure
	public int getColor(int defaultColor) {
		final Integer c = getRawColor();
		if (c != null) {
			return c;
		}
		final BusContainer<?> container = getContainer();
		if (container != null) {
			return container.getColor();
		}
		return defaultColor;
	}

	@Override
	@Pure
	public Integer getRawColor() {
		final AttributeValue val = getAttributeCollection().getAttribute(ATTR_COLOR);
		if (val != null) {
			try {
				return (int) val.getInteger();
			} catch (AttributeException e) {
				//
			}
		}
		return null;
	}

	@Override
	public void setColor(int color) {
		try {
			final int c = getColor();
			if (c != color) {
				getAttributeCollection().setAttribute(ATTR_COLOR, new AttributeValueImpl(color));
			}
		} catch (AttributeException e) {
			//
		}
	}

	@Override
	@Pure
	public boolean isReadOnlyObject() {
		return hasFlag(FLAG_READONLY);
	}

	/** Set if this editable object want to be seen as a reed-only object.
	 *
	 * <p>Even if this object replies <code>true</code>, it is possible to change
	 * its attributes. The value replied by this function is just a desire
	 * from this object. It could be used by the GUI to allow edition or not
	 * from a graphical component.
	 *
	 * @param readOnly the flag.
	 */
	public void setReadOnlyObject(boolean readOnly) {
		if (readOnly) {
			setFlag(FLAG_READONLY);
		} else {
			unsetFlag(FLAG_READONLY);
		}
	}

	//------------------------------------------------------------------
	// GISFlagContainer
	//------------------------------------------------------------------

	@Override
	@Pure
	public int getFlags() {
		return this.flag;
	}

	@Override
	@Pure
	public boolean hasFlag(int flagIndex) {
		return (this.flag & flagIndex) == flagIndex;
	}

	@Override
	public void switchFlag(int flagIndex) {
		final int oldFlag = this.flag;
		this.flag ^= flagIndex;
		if ((flagIndex & FLAG_SELECTED) != 0) {
			fireGraphicalAttributeChanged("flag", Integer.valueOf(oldFlag), Integer.valueOf(this.flag)); //$NON-NLS-1$
		} else {
			firePrimitiveChanged("flag", Integer.valueOf(oldFlag), Integer.valueOf(this.flag)); //$NON-NLS-1$
		}
	}

	@Override
	public void setFlag(int flag) {
		final int oldFlag = this.flag;
		this.flag |= flag;
		if (oldFlag != this.flag) {
			if ((flag & FLAG_SELECTED) != 0) {
				fireGraphicalAttributeChanged("flag", Integer.valueOf(oldFlag), Integer.valueOf(this.flag)); //$NON-NLS-1$
			} else {
				firePrimitiveChanged("flag", Integer.valueOf(oldFlag), Integer.valueOf(this.flag)); //$NON-NLS-1$
			}
		}
	}

	@Override
	public void unsetFlag(int flagIndex) {
		final int oldFlag = this.flag;
		this.flag = this.flag & (~flagIndex);
		if (oldFlag != this.flag) {
			if ((flagIndex & FLAG_SELECTED) != 0) {
				fireGraphicalAttributeChanged("flag", Integer.valueOf(oldFlag), Integer.valueOf(this.flag)); //$NON-NLS-1$
			} else {
				firePrimitiveChanged("flag", Integer.valueOf(oldFlag), Integer.valueOf(this.flag)); //$NON-NLS-1$
			}
		}
	}

	//------------------------------------------------------------------
	// BusPrimitive
	//------------------------------------------------------------------

	@SuppressWarnings("unlikely-arg-type")
	@Override
	@Pure
	public final boolean isValidPrimitive() {
		if (this.invalidityReason != null
				&& this.invalidityReason.equals(BusPrimitiveInvalidityType.VALIDITY_NOT_CHECKED)) {
			checkPrimitiveValidity();
		}
		return this.invalidityReason == null;
	}

	/** Set if this component has invalid
	 * or not.
	 *
	 * <p>This function is supposed to be invoked at least
	 * from {@link #checkPrimitiveValidity()}.
	 *
	 * @param invalidityReason is the reason of the invalidity;
	 *     if <code>null</code> the primitive is valid.
	 */
	@SuppressWarnings("unlikely-arg-type")
	protected final void setPrimitiveValidity(BusPrimitiveInvalidity invalidityReason) {
		if ((invalidityReason == null && this.invalidityReason != null)
				|| (invalidityReason != null
				&& !invalidityReason.equals(BusPrimitiveInvalidityType.VALIDITY_NOT_CHECKED)
				&& !invalidityReason.equals(this.invalidityReason))) {
			final BusPrimitiveInvalidity old = this.invalidityReason;
			this.invalidityReason = invalidityReason;
			fireValidityChanged(old, this.invalidityReason);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	@Pure
	public final BusPrimitiveInvalidity getInvalidityReason() {
		if (this.invalidityReason != null
				&& this.invalidityReason.equals(BusPrimitiveInvalidityType.VALIDITY_NOT_CHECKED)) {
			checkPrimitiveValidity();
		}
		return this.invalidityReason;
	}

	@Override
	public void revalidate() {
		checkPrimitiveValidity();
	}

	/** Check if the validity of this primitive is correctly
	 * set and change its values if necessary. This function
	 * DO NOT revalidate the primitives inside this primitive.
	 *
	 * @see #revalidate()
	 */
	protected abstract void checkPrimitiveValidity();

	@Override
	public final void rebuild() {
		rebuild(false);
	}


	//------------------------------------------------------------------
	// Types
	//------------------------------------------------------------------

	/**
	 * Listener on attribute changes for a BusStop.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class AttributeListener implements AttributeChangeListener {

		private final WeakReference<AbstractBusPrimitive<?>> object;

		/**
		 * @param object is the object associated to the listener.
		 */
		AttributeListener(AbstractBusPrimitive<?> object) {
			this.object = new WeakReference<>(object);
		}

		@Override
		public void onAttributeChangeEvent(AttributeChangeEvent event) {
			final AbstractBusPrimitive<?> bp = this.object.get();
			if (bp != null) {
				bp.onAttributeChanged(event.getName());
			}
		}

	}

}
