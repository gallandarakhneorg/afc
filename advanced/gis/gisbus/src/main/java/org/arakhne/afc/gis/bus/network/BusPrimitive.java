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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.BoundedGISElement;
import org.arakhne.afc.gis.primitive.GISEditable;
import org.arakhne.afc.gis.primitive.GISEditableChangeListener;
import org.arakhne.afc.gis.primitive.GISFlagContainer;

/**
 * A bus primitive is an object which is composing a bus network.
 *
 * @param <CONTAINER> is the type of the object which could contains a instance of this class.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface BusPrimitive<CONTAINER extends BusContainer<?>> extends BoundedGISElement, GISEditable, GISFlagContainer {

	/** Attribute: color of the element.
	 */
	String ATTR_COLOR = "color"; //$NON-NLS-1$

	/** Default color of a map element.
	 */
	int DEFAULT_COLOR = 0xFF0000;

	//------------------------------------------------------------------
	// Getter Setter
	//------------------------------------------------------------------

	/** Replies if this primitive is marked as valid.
	 *
	 * <p>The validity of a primitive depends on the type of
	 * this primitive. Please refers to the documentation
	 * of that primitive.
	 *
	 * @return <code>true</code> if the primitive is valid, otherwise <code>false</code>
	 */
	@Pure
	boolean isValidPrimitive();

	/** Replies the explanation of the invalidity
	 * of the primitive.
	 *
	 * @return the invalidity reason.
	 */
	@Pure
	BusPrimitiveInvalidity getInvalidityReason();

	/** Check if the validity of this primitive is correctly
	 * set and change its values if necessary. This function
	 * revalidate also all the primitives inside this primitive.
	 */
	void revalidate();

	/** Rebuild the primitive and all the primitive inside.
	 * Rebuilding means to reset all the internal values
	 * and recompute their values.
	 *
	 * <p>This function should be invoked when the bus primitive
	 * was construct with its flag replied by {@link #isEventFirable()}
	 * set to <code>false</code>.
	 * This function permits to finalize the construction.
	 *
	 * <p>In addition, this function automatically
	 * invoke {@link #resetBoundingBox()} and does the same
	 * job as {@link #revalidate()} (but does not invoke it).
	 * This function invokes {@link #setEventFirable(boolean)}
	 * with <code>true</code> as parameter.
	 *
	 * <p>This function does not fire any event is
	 * {@link #isEventFirable()} replies <code>true</code>
	 * when {@link #rebuild()} is invoked
	 *
	 * @since 4.0
	 * @see #rebuild(boolean)
	 */
	void rebuild();

	/** Rebuild the primitive and all the primitive inside.
	 * Rebuilding means to reset all the internal values
	 * and recompute their values.
	 *
	 * <p>This function should be invoked when the bus primitive
	 * was construct with its flag replied by {@link #isEventFirable()}
	 * set to <code>false</code>.
	 * This function permits to finalize the construction.
	 *
	 * <p>In addition, this function automatically
	 * invoke {@link #resetBoundingBox()} and does the same
	 * job as {@link #revalidate()} (but does not invoke it).
	 * This function invokes {@link #setEventFirable(boolean)}
	 * with <code>true</code> as parameter.
	 *
	 * @param fireEvents indicates if the events should be fired.
	 * @since 4.0
	 * @see #rebuild()
	 */
	void rebuild(boolean fireEvents);

	/** Replies the bus network in which this primitive is.
	 *
	 * @return the bus network in which this primitive is.
	 */
	@Pure
	BusNetwork getBusNetwork();

	//------------------------------------------------------------------
	// Listener API
	//------------------------------------------------------------------

	@Override
	void addGISEditableChangeListener(GISEditableChangeListener listener);

	@Override
	void removeGISEditableChangeListener(GISEditableChangeListener listener);

	/** Add listener.
	 *
	 * @param listener the listener.
	 */
	void addBusChangeListener(BusChangeListener listener);

	/** Remove listener.
	 *
	 * @param listener the listener.
	 */
	void removeBusChangeListener(BusChangeListener listener);

	/** Fire the event that indicates this object has changed.
	 *
	 * @param event the event
	 */
	void firePrimitiveChanged(BusChangeEvent event);

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
	 *
	 * @param propertyName is the name of the graphical property.
	 * @param oldValue is the old value of the property.
	 * @param newValue is the new value of the property.
	 */
	void fireGraphicalAttributeChanged(String propertyName, Object oldValue, Object newValue);

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
	 *
	 * @param event the event.
	 */
	void fireGraphicalAttributeChanged(BusChangeEvent event);

	/** Invoked when the shape of this element changed.
	 *
	 * <p>This  method also reset the bounding box to allow
	 * its re-computation (with a call to {@link #resetBoundingBox()}.
	 *
	 * <p>In the implementation of a BusPrimitive, prefers to call
	 * {@link #fireGraphicalAttributeChanged(String, Object, Object)} or
	 * {@code fireShapeChanged()} instead of {@link #resetBoundingBox()}.
	 *
	 * <p>If the attributes that change does not concern the shape (bounding box)
	 * of the element, prefers an invocation of {@link #fireGraphicalAttributeChanged(String, Object, Object)}
	 * instead of {@code fireShapeChanged()}
	 */
	void fireShapeChanged();

	/** Invoked when the shape of this element changed.
	 *
	 * <p>This  method also reset the bounding box to allow
	 * its re-computation (with a call to {@link #resetBoundingBox()}.
	 *
	 * <p>In the implementation of a BusPrimitive, prefers to call
	 * {@link #fireGraphicalAttributeChanged(String, Object, Object)} or
	 * {@code fireShapeChanged()} instead of {@link #resetBoundingBox()}.
	 *
	 * <p>If the attributes that change does not concern the shape (bounding box)
	 * of the element, prefers an invocation of {@link #fireGraphicalAttributeChanged(String, Object, Object)}
	 * instead of {@code fireShapeChanged()}
	 *
	 * @param event the event.
	 */
	void fireShapeChanged(BusChangeEvent event);

}
