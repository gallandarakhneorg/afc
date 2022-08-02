/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import java.util.Iterator;
import java.util.UUID;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.primitive.ChangeListener;

/**
 * This object stands for bus primitives which are also containers of bus primitives.
 *
 * @param <CONTAINER> is the type of the object which could contains an instance of this class.
 * @param <ELEMENT> is the type of the object which could be inside an instance of this class.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractBusContainer<CONTAINER extends BusContainer<?>, ELEMENT extends BusPrimitive<?>>
		extends AbstractBusPrimitive<CONTAINER> implements BusContainer<ELEMENT> {

	private static final long serialVersionUID = -1555006589205326227L;

	private transient volatile ChangeListener listener;

	/** Constructor.
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeProvider is the provider of attributes used by this bus stop.
	 * @since 2.0
	 */
	AbstractBusContainer(UUID id, AttributeCollection attributeProvider) {
		super(id, attributeProvider);
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
	public void fireShapeChanged(BusChangeEvent event) {
		super.fireShapeChanged(event);
		fireChangeListener();
	}

	@Override
	public void fireGraphicalAttributeChanged(BusChangeEvent event) {
		super.fireGraphicalAttributeChanged(event);
		fireChangeListener();
	}

	@Override
	public void onBusPrimitiveShapeChanged(BusChangeEvent event) {
		fireShapeChanged(event);
	}

	@Override
	public void onBusPrimitiveGraphicalAttributeChanged(BusChangeEvent event) {
		fireGraphicalAttributeChanged(event);
	}

	@Override
	public void onBusPrimitiveChanged(BusChangeEvent event) {
		if (event.getEventType() == BusChangeEventType.VALIDITY) {
			checkPrimitiveValidity();
		} else {
			firePrimitiveChanged(event);
		}
	}

	@Override
	public void revalidate() {
		final Iterator<ELEMENT> iterator = iterator();
		ELEMENT element;
		while (iterator.hasNext()) {
			element = iterator.next();
			assert element != null;
			element.revalidate();
		}
		super.revalidate();
	}

}
