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

package org.arakhne.afc.gis.maplayer;

import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationArea;
import org.arakhne.afc.gis.location.GeoLocationNowhere;
import org.arakhne.afc.gis.maplayer.MapLayerHierarchyEvent.Type;
import org.arakhne.afc.gis.primitive.AbstractBoundedGISElement;
import org.arakhne.afc.gis.primitive.GISBrowsable;
import org.arakhne.afc.gis.primitive.GISContentElement;
import org.arakhne.afc.gis.primitive.GISEditable;
import org.arakhne.afc.gis.primitive.GISEditableChangeListener;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.util.ListenerCollection;

/**
 * This class represents a layer. A Layer is a container of map elements or other layers.
 *
 * <p>A layer could have several states:
 * <ul>
 * <li>temporary: the layer and its content (including sublayers) are marked has temporary variables ie, the layer is assumed
 * to not be saved when it will be closed. The events on this layers will be also marked has temporary;</li>
 * <li>visible: the layer and its content (including sublayers) are visible or not in a displaying component;</li>
 * <li>clickable: the layer and its content (including sublayers) are accepting mouse events;</li>
 * <li>removable: the layer is removable from its parent. If the layer is marked has removable, it could be
 * removed from its. This state was only useful for GUI purpose.</li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see MultiMapLayer for a container of layer
 * @see ArrayMapElementLayer for a container of map elements
 * @see GridMapElementLayer for a container of map elements
 * @see TreeMapElementLayer for a container of map elements
 */
public abstract class MapLayer extends AbstractBoundedGISElement<GISLayerContainer<?>, MapLayer>
		implements GISBrowsable, GISEditable, GISContentElement<GISLayerContainer<?>> {

	/** Attribute: color of the element.
	 */
	public static final String ATTR_COLOR = "color"; //$NON-NLS-1$

	/** Name of the attribute that indicates if the
	 * element must use its container's color.
	 */
	public static final String ATTR_USE_CONTAINER_COLOR = "useContainerColor";  //$NON-NLS-1$

	/** Name of the attribute that indicates if the
	 * element could be clicked.
	 */
	public static final String ATTR_CLICKABLE = "clickable";  //$NON-NLS-1$

	/** Name of the attribute that indicates if the
	 * element is visible.
	 */
	public static final String ATTR_VISIBLE = "visible";  //$NON-NLS-1$

	/** Name of the attribute that indicates if the
	 * element is removabled from its parent.
	 */
	public static final String ATTR_REMOVABLE = "removable";  //$NON-NLS-1$

	private static final long serialVersionUID = 8089536894326744997L;

	/** List of listeners on the events.
	 */
	protected transient ListenerCollection<?> listeners;

	/** Indicates if this layer is temporary.
	 */
	private final boolean isTemp;

	/** Indicates if this layer could be assumed as read-only element.
	 */
	private boolean readOnly;

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @since 4.0
	 */
	public MapLayer(UUID id, AttributeCollection attributeSource) {
		this(id, attributeSource, false);
	}

	/** Create a new layer with the specified attribute source.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the layer's attributes.
	 * @param isTemp indicates if this layer is temporary.
	 * @since 4.0
	 */
	public MapLayer(UUID id, AttributeCollection attributeSource, boolean isTemp) {
		super(id, attributeSource);
		this.isTemp = isTemp;
	}

	/** Clone this object to obtain a valid copy.
	 *
	 * @return a copy
	 */
	@Override
	@Pure
	public MapLayer clone() {
		final MapLayer layer = super.clone();
		layer.listeners = null;
		return layer;
	}

	/** Set the unique identifier for element.
	 *
	 * <p>A Unique IDentifier (UID) must be unique for all the object instances.
	 *
	 * @param id is the new identifier
	 * @since 4.0
	 */
	@Override
	public final void setUUID(UUID id) {
		final UUID oldId = getUUID();
		if ((oldId != null && !oldId.equals(id)) || (oldId == null && id != null)) {
			super.setUUID(oldId);
			fireElementChanged();
		}
	}

	/** Add a listener on the layer's events.
	 *
	 * @param listener the listener.
	 */
	public final void addLayerListener(MapLayerListener listener) {
		if (this.listeners == null) {
			this.listeners = new ListenerCollection<>();
		}
		this.listeners.add(MapLayerListener.class, listener);
	}

	/** Remove a listener on the layer's events.
	 *
	 * @param listener the listener.
	 */
	public final void removeLayerListener(MapLayerListener listener) {
		if (this.listeners != null) {
			this.listeners.remove(MapLayerListener.class, listener);
			if (this.listeners.isEmpty()) {
				this.listeners = null;
			}
		}
	}

	@Override
	public void addGISEditableChangeListener(GISEditableChangeListener listener) {
		if (this.listeners == null) {
			this.listeners = new ListenerCollection<>();
		}
		this.listeners.add(GISEditableChangeListener.class, listener);
	}

	@Override
	public void removeGISEditableChangeListener(GISEditableChangeListener listener) {
		if (this.listeners != null) {
			this.listeners.remove(GISEditableChangeListener.class, listener);
			if (this.listeners.isEmpty()) {
				this.listeners = null;
			}
		}
	}

	/** Replies the list of listeners on this object.
	 *
	 * @return the list of listeners on this object.
	 */
	@Pure
	protected final MapLayerListener[] getListeners() {
		if (this.listeners == null) {
			return new MapLayerListener[0];
		}
		return this.listeners.getListeners(MapLayerListener.class);
	}

	/** Fire the event that indicates the hierarchy of layers has changed.
	 * Only the {@link MapLayerListener} and the container are notified.
	 *
	 * @param event the event.
	 */
	public void fireLayerHierarchyChangedEvent(MapLayerHierarchyEvent event) {
		if (isEventFirable()) {
			final MapLayerListener[] theListeners = getListeners();
			if (theListeners != null && theListeners.length > 0) {
				for (final MapLayerListener listener : theListeners) {
					listener.onMapLayerHierarchyChanged(event);
				}
			}

			// stop the event firing when it was consumed
			if (event.isConsumed() && event.isDisappearingWhenConsumed()) {
				return;
			}

			final GISLayerContainer<?> container = getContainer();
			if (container != null) {
				container.fireLayerHierarchyChangedEvent(event);
			}
		}
	}

	/** Forward to the parent layer the event that indicates the content of a child layer was changed.
	 * Only the {@link MapLayerListener} and the container are notified.
	 *
	 * @param event the event.
	 */
	public void fireLayerContentChangedEvent(MapLayerContentEvent event) {
		if (isEventFirable()) {
			final MapLayerListener[] theListeners = getListeners();
			if (theListeners != null && theListeners.length > 0) {
				for (final MapLayerListener listener : theListeners) {
					listener.onMapLayerContentChanged(event);
				}
			}

			// stop the event firing when it was consumed
			if (event.isConsumed() && event.isDisappearingWhenConsumed()) {
				return;
			}

			final GISLayerContainer<?> container = getContainer();
			if (container != null) {
				container.fireLayerContentChangedEvent(event);
			}
		}
	}

	/**
	 * Send a repainting query.
	 * Notify listeners about any change in the layer which cause repaint of the panel.
	 */
	public final void repaint() {
		fireLayerContentChangedEvent(new MapLayerContentEvent(this));
	}

	/** Fire the event that indicates this object has changed.
	 * Only the {@link GISEditableChangeListener} are notified.
	 */
	public void fireElementChanged() {
		if (this.listeners != null && isEventFirable()) {
			final GISEditableChangeListener[] theListeners = this.listeners.getListeners(GISEditableChangeListener.class);
			for (final GISEditableChangeListener listener : theListeners) {
				listener.editableGISElementHasChanged(this);
			}
		}
	}

	/** Invoked when the attribute's value changed.
	 */
	@Override
	public void onAttributeChangeEvent(AttributeChangeEvent event) {
		super.onAttributeChangeEvent(event);
		fireLayerAttributeChangedEvent(new MapLayerAttributeChangeEvent(this, event));
		fireElementChanged();
		if (ATTR_VISIBLE.equals(event.getName())) {
			final AttributeValue nValue = event.getValue();
			boolean cvalue;
			try {
				cvalue = nValue.getBoolean();
			} catch (Exception exception) {
				cvalue = isVisible();
			}
			if (cvalue) {
				final GISLayerContainer<?> container = getContainer();
				if (container instanceof GISBrowsable) {
					((GISBrowsable) container).setVisible(cvalue, false);
				}
			}
		}
	}

	/** Forward to the parent layer the event that indicates the content of a child layer was changed.
	 * Only the {@link MapLayerListener} and the container are notified.
	 *
	 * @param event the event
	 */
	public void fireLayerAttributeChangedEvent(MapLayerAttributeChangeEvent event) {
		if (isEventFirable()) {
			final MapLayerListener[] theListeners = getListeners();
			if (theListeners != null && theListeners.length > 0) {
				for (final MapLayerListener listener : theListeners) {
					listener.onMapLayerAttributeChanged(event);
				}
			}

			final GISLayerContainer<?> container = getContainer();
			if (container != null) {
				container.fireLayerAttributeChangedEvent(event);
			}
		}
	}

	/** Forward to the parent layer the event that indicates the content of a child layer was changed.
	 * Only the {@link MapLayerListener} and the container are notified.
	 *
	 * @param attributeName is the name of the changed attribute.
	 * @param oldValue is the old value of the attribute.
	 * @param newValue is the new value of the attribute.
	 */
	public void fireLayerAttributeChangedEvent(String attributeName, Object oldValue, Object newValue) {
		if (isEventFirable()) {
			final AttributeChangeEvent.Type eType;
			if (oldValue == null && newValue != null) {
				eType = AttributeChangeEvent.Type.ADDITION;
			} else if (oldValue != null && newValue == null) {
				eType = AttributeChangeEvent.Type.REMOVAL;
			} else {
				eType = AttributeChangeEvent.Type.VALUE_UPDATE;
			}
			final AttributeChangeEvent e = new AttributeChangeEvent(
					this,
					eType,
					attributeName,
					new AttributeValueImpl(oldValue),
					attributeName,
					new AttributeValueImpl(newValue));
			final MapLayerAttributeChangeEvent le = new MapLayerAttributeChangeEvent(this, e);
			fireLayerAttributeChangedEvent(le);
		}
	}

	/** Clear the current bounding box to force the computation of it at
	 * the next call to {@link #getBoundingBox()}.
	 */
	@Override
	public void resetBoundingBox() {
		super.resetBoundingBox();
		final GISLayerContainer<?> parent = getContainer();
		if (parent != null) {
			parent.resetBoundingBox();
		}
	}

	@Override
	public boolean setContainer(GISLayerContainer<?> parent) {
		final GISLayerContainer<?> oldParent = getContainer();
		if (oldParent != parent) {
			if (parent == null) {
				if (super.setContainer(parent)) {
					fireLayerHierarchyChangedEvent(
							new MapLayerHierarchyEvent(
									oldParent,
									this,
									Type.REMOVED_FROM_PARENT,
									-1,
									isTemporaryLayer()));
					return true;
				}
			} else {
				if (super.setContainer(parent)) {
					fireLayerHierarchyChangedEvent(
							new MapLayerHierarchyEvent(
									parent,
									this,
									Type.ADDED_INTO_PARENT,
									//index
									-1,
									isTemporaryLayer()));
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	public String getParentName() {
		final GISLayerContainer<?> c = getContainer();
		if (c != null) {
			return c.getName();
		}
		return null;
	}

	@Override
	@Pure
	public GeoLocation getGeoLocation() {
		final Shape2d<?> bounds = getBoundingBox();
		if (bounds != null) {
			return new GeoLocationArea(bounds);
		}
		return new GeoLocationNowhere(getUUID());
	}

	/**
	 * Replies if the specified point (<var>x</var>,<var>y</var>)
	 * was inside the figure of this MapElement.
	 *
	 * <p>If this MapElement has no associated figure, this method
	 * always returns <code>false</code>.
	 *
	 * @param point is a geo-referenced coordinate
	 * @param delta is the geo-referenced distance that corresponds to a approximation
	 *     distance in the screen coordinate system
	 * @return <code>true</code> if this MapElement had an associated figure and
	 *     the specified point was inside this bounds of this figure, otherwhise
	 * <code>false</code>
	 */
	@Pure
	public boolean contains(Point2D<?, ?> point, double delta) {
		final Rectangle2d bounds = getBoundingBox();
		if (bounds == null) {
			return false;
		}
		double dlt = delta;
		if (dlt < 0) {
			dlt = -dlt;
		}
		if (dlt == 0) {
			return bounds.contains(point);
		}
		return Circle2afp.intersectsCircleRectangle(
				point.getX(), point.getY(), dlt,
				bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY());
	}

	/**
	 * Replies if the specified point (<var>x</var>,<var>y</var>)
	 * was inside the figure of this MapElement.
	 *
	 * <p>If this MapElement has no associated figure, this method
	 * always returns <code>false</code>.
	 *
	 * @param point is a geo-referenced coordinate
	 * @return <code>true</code> if this MapElement had an associated figure and
	 *     the specified point was inside this bounds of this figure, otherwhise
	 * <code>false</code>
	 */
	@Pure
	public final boolean contains(Point2D<?, ?> point) {
		return contains(point, 0);
	}

	/** Replies the index of this layer in its parent.
	 *
	 * @return the index of this layer in its parent, or
	 * <code>-1</code> if not found.
	 */
	@Pure
	public int indexInParent() {
		final GISLayerContainer<?> container = getContainer();
		if (container != null) {
			return container.indexOf(this);
		}
		return -1;
	}

	/**
	 * Replies if this element has an intersection
	 * with the specified rectangle.
	 *
	 * <p>If this MapElement has no associated figure, this method
	 * always returns <code>false</code>.
	 *
	 *
	 * @param rectangle the rectangle.
	 * @return <code>true</code> if this MapElement has an associated figure and
	 *     the specified rectangle intersecting the figure, otherwhise
	 * <code>false</code>
	 */
	@Pure
	public boolean intersects(Shape2d<?> rectangle) {
		final Shape2d<?> bounds = getBoundingBox();
		if (bounds == null) {
			return false;
		}
		return bounds.intersects(rectangle);
	}

	/**
	 * Set the flag that indicates if this element use its color or
	 * the container's color.
	 *
	 * @param useContainerColor must be <code>true</code> if this element must
	 *     use the container's color, and must be <code>false</code> to use the
	 *     element's color.
	 */
	public void setContainerColorUse(boolean useContainerColor) {
		getAttributeCollection().setAttribute(ATTR_USE_CONTAINER_COLOR, useContainerColor);
	}

	/**
	 * Replies the flag that indicates if this element use its color or
	 * the container's color.
	 *
	 * @return <code>true</code> if this element must
	 *     use the container's color, and <code>false</code> to use the
	 *     element's color.
	 */
	@Pure
	public boolean isContainerColorUsed() {
		final AttributeValue val = getAttributeProvider().getAttribute(ATTR_USE_CONTAINER_COLOR);
		if (val != null) {
			try {
				return val.getBoolean();
			} catch (AttributeException e) {
				//
			}
		}
		return false;
	}

	@Override
	@Pure
	public int getColor() {
		final GISLayerContainer<?> container = getContainer();
		if (container != null && isContainerColorUsed()) {
			return container.getColor();
		}
		return getRawColor();
	}

	@Override
	@Pure
	public Integer getRawColor() {
		final AttributeValue val = getAttributeProvider().getAttribute(ATTR_COLOR);
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
			getAttributeCollection().setAttribute(ATTR_COLOR, new AttributeValueImpl(color));
		} catch (AttributeException e) {
			//
		}
	}

	/**
	 * Set if this layer accepts the user clicks.
	 *
	 * @param clickable is <code>true</code> to set this layer allowing mouse click events.
	 */
	public void setClickable(boolean clickable) {
		try {
			getAttributeCollection().setAttribute(ATTR_CLICKABLE, new AttributeValueImpl(clickable));
		} catch (AttributeException e) {
			//
		}
	}

	/**
	 * Replies if this layer accepts the user clicks.
	 *
	 * @return <code>true</code> if this layer allows mouse click events, otherwise <code>false</code>
	 */
	@Pure
	public boolean isClickable() {
		final AttributeValue val = getAttributeProvider().getAttribute(ATTR_CLICKABLE);
		if (val != null) {
			try {
				return val.getBoolean();
			} catch (AttributeException e) {
				//
			}
		}
		return true;
	}

	@Override
	public final void setVisible(boolean visible) {
		setVisible(visible, true);
	}

	@Override
	public void setVisible(boolean visible, boolean setChildrenVisibility) {
		try {
			final AttributeValue value;
			if (setChildrenVisibility) {
				value = new AttributeValueImpl(visible);
			} else {
				value = new SpecialAttributeValue(visible);
			}
			getAttributeCollection().setAttribute(ATTR_VISIBLE, value);
		} catch (AttributeException e) {
			//
		}
	}

	/**
	 * Replies if this layer was mark as temporary lyer.
	 *
	 * <p>A temporary layer means that any things inside this layer is
	 * assumed to be lost when the layer will be destroyed.
	 *
	 * @return <code>true</code> if this layer is temporary, otherwise <code>false</code>
	 */
	@Pure
	public final boolean isTemporaryLayer() {
		MapLayer layer = this;
		GISLayerContainer<?> container;
		while (layer != null) {
			if (layer.isTemp) {
				return true;
			}
			container = layer.getContainer();
			layer = container instanceof MapLayer ? (MapLayer) container : null;
		}
		return false;
	}

	@Override
	@Pure
	public boolean isReadOnlyObject() {
		return this.readOnly;
	}

	/** Replies if this editable object want to be seen as a reed-only object.
	 *
	 * <p>Even if this object replies <code>true</code>, it is possible to change
	 * its attributes. The value replied by this function is just a desire
	 * from this object. It could be used by the GUI to allow edition or not
	 * from a graphical component.
	 *
	 * @param readOnly the read-only flag.
	 */
	public void setReadOnlyObject(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	@Pure
	public boolean isVisible() {
		final AttributeValue val = getAttributeProvider().getAttribute(ATTR_VISIBLE);
		if (val != null) {
			try {
				return val.getBoolean();
			} catch (AttributeException e) {
				//
			}
		}
		return true;
	}

	/**
	 * Set if this layer is removable from this container.
	 * This removal value permits to the container to be
	 * informed about the desired removal state from
	 * its component. The usage of this state by
	 * the container depends only of its implementation.
	 *
	 * @param removable is <code>true</code> to set this layer to be removable from its container,
	 *     otherwise <code>false</code>
	 */
	public void setRemovable(boolean removable) {
		try {
			getAttributeCollection().setAttribute(ATTR_REMOVABLE, new AttributeValueImpl(removable));
		} catch (AttributeException e) {
			//
		}
	}

	/**
	 * Replies if this layer is removable from this container.
	 * This removal value permits to the container to be
	 * informed about the desired removal state from
	 * its component. The usage of this state by
	 * the container depends only of its implementation.
	 *
	 * @return <code>true</code> if this layer is removable from its container,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isRemovable() {
		final AttributeValue val = getAttributeProvider().getAttribute(ATTR_REMOVABLE);
		if (val != null) {
			try {
				return val.getBoolean();
			} catch (AttributeException e) {
				//
			}
		}
		return true;
	}

	/**
	 * Attribute value with a special meaning in MapLayer.
	 * This meaning depends on the usage of the object.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	protected static class SpecialAttributeValue extends AttributeValueImpl {

		private static final long serialVersionUID = 4671570907546629771L;

		/** Constructor.
		 * @param value the value.
		 */
		public SpecialAttributeValue(boolean value) {
			super(value);
		}

	}

}
