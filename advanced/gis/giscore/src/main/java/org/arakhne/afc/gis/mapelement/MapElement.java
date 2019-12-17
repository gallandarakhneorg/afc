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

package org.arakhne.afc.gis.mapelement;

import java.io.Serializable;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.attrs.collection.AttributeChangeEvent;
import org.arakhne.afc.attrs.collection.AttributeChangeListener;
import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.primitive.AbstractBoundedGISElement;
import org.arakhne.afc.gis.primitive.FlagContainer;
import org.arakhne.afc.gis.primitive.GISContainer;
import org.arakhne.afc.gis.primitive.GISContentElement;
import org.arakhne.afc.gis.primitive.GISEditable;
import org.arakhne.afc.gis.primitive.GISEditableChangeListener;
import org.arakhne.afc.gis.primitive.GISFlagContainer;
import org.arakhne.afc.inputoutput.xml.XMLUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.ListenerCollection;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * Abstract class for all the elements of a map inside a layer.
 *
 * <p>Since release 1.3, this object allow to store
 * attributes inside a database system.
 *
 * <p>An attribute is a pair name-value which must respect
 * some constraints:
 * <ol>
 * <li>its name must be unique;</li>
 * <li>its value type must be the same as the defined type
 * inside the attribute definition manager.</li>
 * </ol>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class MapElement extends AbstractBoundedGISElement<GISElementContainer<?>, MapElement>
		implements GISContentElement<GISElementContainer<?>>, GISEditable, GISFlagContainer {

	/** Attribute: color of the element.
	 */
	public static final String ATTR_COLOR = "color"; //$NON-NLS-1$

	/** Name of the attribute that indicates if the
	 * element must use its container's color.
	 */
	public static final String ATTR_USE_CONTAINER_COLOR = "useContainerColor";  //$NON-NLS-1$

	/** Name of the attribute that indicates if the
	 * element must use its container's color.
	 */
	public static final String ATTR_VISUALIZATION_TYPE = "visualizationType";  //$NON-NLS-1$

	private static final long serialVersionUID = 637544611540259335L;

	private AttributeListener attributeListener;

	/** Flag associated to this element.
	 */
	private int flag;

	/**
	 * Listener on changes.
	 */
	private ListenerCollection<GISEditableChangeListener> listeners;

	private Integer colorObject;

	private VisualizationType vizualizationType;

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the source of the attributes for this map element.
	 * @since 4.0
	 */
	public MapElement(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
		this.attributeListener = new AttributeListener();
		getAttributeCollection().addAttributeChangeListener(this.attributeListener);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		if (getRawColor() != null) {
			buffer.add("color", XMLUtil.toColor(getRawColor())); //$NON-NLS-1$
		}
		buffer.add("selected", hasFlag(FlagContainer.FLAG_SELECTED)); //$NON-NLS-1$
		buffer.add("readOnly", hasFlag(FlagContainer.FLAG_READONLY)); //$NON-NLS-1$
		buffer.add("visualizationType", getVisualizationType()); //$NON-NLS-1$
	}

	@Override
	@SuppressWarnings("checkstyle:nofinalizer")
	protected void finalize() throws Throwable {
		getAttributeCollection().removeAttributeChangeListener(this.attributeListener);
		super.finalize();
	}

	/** Clone this object to obtain a valid copy.
	 *
	 * @return a copy
	 */
	@Override
	@Pure
	public MapElement clone() {
		final MapElement element = super.clone();
		element.unsetFlag(FLAG_SELECTED);
		element.listeners = null;
		element.attributeListener = new AttributeListener();
		element.getAttributeCollection().addAttributeChangeListener(element.attributeListener);
		return element;
	}

	//------------------------------------------------------------------
	// Methods for a MapElement
	//------------------------------------------------------------------

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

	/** Fire the event that indicates this object has changed.
	 */
	protected final void fireElementChanged() {
		if (this.listeners != null && isEventFirable()) {
			final GISEditableChangeListener[] theListeners = this.listeners.getListeners(GISEditableChangeListener.class);
			for (final GISEditableChangeListener listener : theListeners) {
				listener.editableGISElementHasChanged(this);
			}
		}
	}

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
		this.flag ^= flagIndex;
		if ((flagIndex & FLAG_SELECTED) != 0) {
			fireGraphicalAttributeChanged();
		} else {
			fireElementChanged();
		}
	}

	@Override
	public void setFlag(int flag) {
		final int oldFlag = this.flag;
		this.flag |= flag;
		if (oldFlag != this.flag) {
			if ((flag & FLAG_SELECTED) != 0) {
				fireGraphicalAttributeChanged();
			} else {
				fireElementChanged();
			}
		}
	}

	@Override
	public void unsetFlag(int flagIndex) {
		final int oldFlag = this.flag;
		this.flag = this.flag & (~flagIndex);
		if (oldFlag != this.flag) {
			if ((flagIndex & FLAG_SELECTED) != 0) {
				fireGraphicalAttributeChanged();
			} else {
				fireElementChanged();
			}
		}
	}

	@Override
	@Pure
	public final boolean isReadOnlyObject() {
		return hasFlag(FLAG_READONLY);
	}

	/** Set if this editable object want to be seen as a reed-only object.
	 *
	 * <p>Even if this object replies <code>true</code>, it is possible to change
	 * its attributes. The value replied by this function is just a desire
	 * from this object. It could be used by the GUI to allow edition or not
	 * from a graphical component.
	 *
	 * @param readOnly the read-only flag value.
	 */
	public final void setReadOnlyObject(boolean readOnly) {
		if (readOnly) {
			setFlag(FLAG_READONLY);
		} else {
			unsetFlag(FLAG_READONLY);
		}
	}

	/** This function is invoked by the attribute provider each time
	 * an attribute has changed.
	 *
	 * <p>You should override this method to provide several feedback
	 * to the GISLayerContainer for instance.
	 *
	 * @param name is the name of the attribute that changed
	 */
	protected void onAttributeChanged(String name) {
		if (ATTR_VISUALIZATION_TYPE.equalsIgnoreCase(name)) {
			fireShapeChanged();
		} else if ((ATTR_COLOR.equalsIgnoreCase(name))
				|| (ATTR_USE_CONTAINER_COLOR.equalsIgnoreCase(name))) {
			fireGraphicalAttributeChanged();
		}
		fireElementChanged();
	}

	/** Invoked when one of the graphical attributes of this element
	 * has changed, except those that change the bounding boxes.
	 *
	 * <p>This function does not call {@link #resetBoundingBox()}.
	 *
	 * <p>In the implementation of a MapElement, prefers to call
	 * {@code fireGraphicalAttributeChanged()} or
	 * {@link #fireShapeChanged()} instead of {@link #resetBoundingBox()}.
	 *
	 * <p>If the attributes that change concern the shape (bounding box)
	 * of the element, prefers an invocation of {@link #fireShapeChanged()}
	 * instead of {@code fireGraphicalAttributeChanged()}
	 */
	protected final void fireGraphicalAttributeChanged() {
		if (isEventFirable()) {
			final GISElementContainer<?> container = getContainer();
			if (container != null) {
				container.onMapElementGraphicalAttributeChanged();
			}
		}
	}

	/** Invoked when the shape of this element changed.
	 *
	 * <p>This  method also reset the bounding box to allow
	 * its re-computation (with a call to {@link #resetBoundingBox()}.
	 *
	 * <p>In the implementation of a MapElement, prefers to call
	 * {@link #fireGraphicalAttributeChanged()} or
	 * {@code fireShapeChanged()} instead of {@link #resetBoundingBox()}.
	 *
	 * <p>If the attributes that change does not concern the shape (bounding box)
	 * of the element, prefers an invocation of {@link #fireGraphicalAttributeChanged()}
	 * instead of {@code fireShapeChanged()}
	 */
	protected final void fireShapeChanged() {
		resetBoundingBox();
		if (isEventFirable()) {
			final GISElementContainer<?> container = getContainer();
			if (container != null) {
				container.onMapElementGraphicalAttributeChanged();
			}
		}
	}

	@Override
	public void resetBoundingBox() {
		super.resetBoundingBox();
		final GISElementContainer<?> container = getContainer();
		if (container != null) {
			container.resetBoundingBox();
		}
	}

	/** Replies if the specified objects is the same as this one.
	 */
	@Override
	@Pure
	@SuppressWarnings("checkstyle:equalshashcode")
	public boolean equals(Object obj) {
		if (obj instanceof MapElement) {
			return equals((MapElement) obj);
		}
		return false;
	}

	/** Replies if the specified objects is the same as this one.
	 *
	 * @param element the element to compare to.
	 * @return <code>true</code> if the given element is equal to this object,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public abstract boolean equals(MapElement element);

	@Override
	@Pure
	public abstract int hashCode();

	/**
	 * Replies the distance between this MapElement and
	 * point.
	 *
	 * @param point the point to compute the distance to.
	 * @return the distance. Should be negative depending of the MapElement type.
	 */
	@Pure
	public abstract double getDistance(Point2D<?, ?> point);

	/**
	 * Replies if the specified point (<var>x</var>,<var>y</var>)
	 * was inside the figure of this MapElement.
	 *
	 * @param point is a geo-referenced coordinate
	 * @param delta is the geo-referenced distance that corresponds to a approximation
	 *     distance in the screen coordinate system
	 * @return <code>true</code> if the specified point has a distance nearest than delta
	 *     to this element, otherwise <code>false</code>
	 */
	@Pure
	public abstract boolean contains(Point2D<?, ?> point, double delta);

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
	 *     <code>false</code>
	 */
	@Pure
	public final boolean contains(Point2D<?, ?> point) {
		return contains(point, 0);
	}

	/**
	 * Replies if the specified point (<var>x</var>,<var>y</var>)
	 * was inside the bounds of this MapElement.
	 *
	 * @param x is a geo-referenced coordinate
	 * @param y is a geo-referenced coordinate
	 * @param delta is the geo-referenced distance that corresponds to a approximation
	 *     distance in the screen coordinate system
	 * @return <code>true</code> if the point is inside the bounds of this object,
	 *     otherwise <code>false</code>
	 */
	@Pure
	protected final boolean boundsContains(double x, double y, double delta) {
		final Rectangle2d bounds = getBoundingBox();
		assert bounds != null;
		double dlt = delta;
		if (dlt < 0) {
			dlt = -dlt;
		}
		final Point2d p = new Point2d(x, y);
		if (dlt == 0) {
			return bounds.contains(p);
		}
		p.subX(dlt);
		p.subY(dlt);
		final Point2d p2 = new Point2d(p.getX() + dlt, p.getY() + dlt);
		return bounds.contains(p) && bounds.contains(p2);
	}

	/**
	 * Replies if this element has an intersection
	 * with the specified rectangle.
	 *
	 * @param rectangle the rectangle
	 * @return <code>true</code> if this MapElement is intersecting the specified area,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public abstract boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> rectangle);

	/**
	 * Replies if the bounds of this element has an intersection
	 * with the specified rectangle.
	 *
	 * @param rectangle the rectangle.
	 * @return <code>true</code> if this bounds is intersecting the specified area,
	 *     otherwise <code>false</code>
	 */
	@Pure
	protected final boolean boundsIntersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> rectangle) {
		final Rectangle2d bounds = getBoundingBox();
		assert bounds != null;
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
		final AttributeValue val = getAttributeCollection().getAttribute(ATTR_USE_CONTAINER_COLOR);
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
		if (isContainerColorUsed()) {
			final GISContainer<?> container = getContainer();
			if (container != null) {
				return container.getColor();
			}
		}
		Integer c = getRawColor();
		if (c == null) {
			c = MapElementConstants.getPreferredColor();
		}
		return c;
	}

	@Override
	@Pure
	public Integer getRawColor() {
		if (this.colorObject == null) {
			final AttributeValue val = getAttributeCollection().getAttribute(ATTR_COLOR);
			if (val != null) {
				try {
					this.colorObject = (int) val.getInteger();
				} catch (AttributeException e) {
					//
				}
			}
		}
		return this.colorObject;
	}

	@Override
	public void setColor(int color) {
		try {
			final int c = getColor();
			if (c != color) {
				this.colorObject = color;
				getAttributeCollection().setAttribute(ATTR_COLOR, new AttributeValueImpl(color));
			}
		} catch (AttributeException e) {
			//
		}
	}

	/**
	 * Replies the type of visualization that must be used by
	 * this element.
	 *
	 * @return the type of visualization
	 */
	@Pure
	public VisualizationType getVisualizationType() {
		if (this.vizualizationType == null) {
			final AttributeValue val = getAttributeCollection().getAttribute(ATTR_VISUALIZATION_TYPE);
			if (val != null) {
				try {
					this.vizualizationType = val.getJavaObject();
				} catch (Exception e) {
					//
				}
			}
			if (this.vizualizationType == null) {
				this.vizualizationType = VisualizationType.SHAPE_ONLY;
			}
		}
		return this.vizualizationType;
	}

	/**
	 * Set the type of visualization that must be used by
	 * this element.
	 *
	 * @param type the visualization  type.
	 */
	public void setVisualizationType(VisualizationType type) {
		try {
			if (getVisualizationType() != type) {
				this.vizualizationType = type;
				getAttributeCollection().setAttribute(ATTR_VISUALIZATION_TYPE, new AttributeValueImpl(type));
			}
		} catch (AttributeException e) {
			//
		}
	}

	/**
	 * Describes how the map element was displayed.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	public enum VisualizationType {

		/** Only the shape of the element must be displayed.
		 */
		SHAPE_ONLY,
		/** Only the icon of the element must be displayed.
		 */
		ICON_ONLY,
		/** The shape and the icon of the element must be displayed.
		 */
		SHAPE_AND_ICON;

		/** Replies if this visualization type includes the shape.
		 *
		 * @return <code>true</code> if this type allows to display a shape.
		 */
		@Pure
		public boolean isShape() {
			return this == SHAPE_AND_ICON || this == SHAPE_ONLY;
		}

		/** Replies if this visualization type includes the icon.
		 *
		 * @return <code>true</code> if this type allows to display an icon.
		 */
		@Pure
		public boolean isIcon() {
			return this == SHAPE_AND_ICON || this == ICON_ONLY;
		}

	} /* enum VisualizationType */

	/**
	 * Listener on attribute changes for a MapElement.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class AttributeListener implements AttributeChangeListener, Serializable {

		private static final long serialVersionUID = 4547351217612338903L;

		/** Constructor.
		 */
		AttributeListener() {
			//
		}

		@Override
		public void onAttributeChangeEvent(AttributeChangeEvent event) {
			MapElement.this.onAttributeChanged(event.getName());
		}

	}

}
