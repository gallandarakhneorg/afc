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

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;

/**
 * Event handler for the unit tests.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class TestEventHandler implements BusChangeListener {

	/**
	 */
	private boolean run;
	/**
	 */
	private final List<BusChangeEvent> busChanged = new ArrayList<>();
	/**
	 */
	private final List<BusChangeEvent> busShapeAttrChanged = new ArrayList<>();
	/**
	 */
	private final List<BusChangeEvent> busShapeChanged = new ArrayList<>();

	/**
	 */
	public TestEventHandler() {
		clear();
	}

	/**
	 */
	public void clear() {
		this.run = false;
		this.busChanged.clear();
		this.busShapeAttrChanged.clear();
		this.busShapeChanged.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBusPrimitiveChanged(BusChangeEvent event) {
		this.run = true;
		this.busChanged.add(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBusPrimitiveGraphicalAttributeChanged(BusChangeEvent event) {
		this.run = true;
		this.busShapeAttrChanged.add(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBusPrimitiveShapeChanged(BusChangeEvent event) {
		this.run = true;
		this.busShapeChanged.add(event);
	}

	/** Assert no event.
	 */
	public void assertNoEvent() {
		assertFalse("Event was fired", this.run); //$NON-NLS-1$
		assertTrue("Event was fired", this.busChanged.isEmpty()); //$NON-NLS-1$
		assertTrue("Event was fired", this.busShapeChanged.isEmpty()); //$NON-NLS-1$
		assertTrue("Event was fired", this.busShapeAttrChanged.isEmpty()); //$NON-NLS-1$
	}

	/** Assert no general event.
	 */
	public void assertNoBusChangedEvent() {
		assertTrue("Event was fired:" //$NON-NLS-1$
				+this.busChanged.toString(),
				this.busChanged.isEmpty());
	}

	/** Assert no shape event.
	 */
	public void assertNoBusShapeChangedEvent() {
		assertTrue("Event was fired:" //$NON-NLS-1$
				+this.busShapeChanged.toString(),
				this.busShapeChanged.isEmpty());
	}

	/** Assert no shape attribute event.
	 */
	public void assertNoBusShapeAttrChangedEvent() {
		assertTrue("Event was fired:" //$NON-NLS-1$
				+this.busShapeAttrChanged.toString(),
				this.busShapeAttrChanged.isEmpty());
	}

	private static BusChangeEvent event(BusChangeEventType desiredType, Object desiredPrimitive, List<BusChangeEvent> events) {
		Iterator<BusChangeEvent> iterator = events.iterator();
		BusChangeEvent event;
		while(iterator.hasNext()) {
			event = iterator.next();
			if (event.getEventType()==desiredType
				&& event.getChangedObject()==desiredPrimitive) {
				iterator.remove();
				return event;
			}
		}
		return null;
	}

	/** Assert the event corresponds to the given parameters.
	 *
	 * @param desiredType is the desired type of event.
	 * @param desiredPrimitive is the desired changed primitive.
	 */
	public void assertBusChangedEvent(BusChangeEventType desiredType, Object desiredPrimitive) {
		assertTrue("Event was not fired", this.run); //$NON-NLS-1$
		assertNotNull("Event not found", event(desiredType, desiredPrimitive, this.busChanged)); //$NON-NLS-1$
	}

	/** Assert the event corresponds to the given parameters.
	 *
	 * @param desiredType is the desired type of event.
	 * @param desiredPrimitive is the desired changed primitive.
	 */
	public void assertBusShapeChangedEvent(BusChangeEventType desiredType, Object desiredPrimitive) {
		assertTrue("Event was not fired", this.run); //$NON-NLS-1$
		assertNotNull("Event not found", event(desiredType, desiredPrimitive, this.busShapeChanged)); //$NON-NLS-1$
	}

	/** Assert the event corresponds to the given parameters.
	 *
	 * @param desiredType is the desired type of event.
	 * @param desiredPrimitive is the desired changed primitive.
	 */
	public void assertBusShapeAttrChangedEvent(BusChangeEventType desiredType, Object desiredPrimitive) {
		assertTrue("Event was not fired", this.run); //$NON-NLS-1$
		assertNotNull("Event not found", event(desiredType, desiredPrimitive, this.busShapeAttrChanged)); //$NON-NLS-1$
	}

	private static void assertTrue(String message, boolean value) {
		if (!value) {
			StringBuilder m = new StringBuilder(message);
			m.append("; expected: "); //$NON-NLS-1$
			m.append(Boolean.TRUE);
			m.append("; actual: "); //$NON-NLS-1$
			m.append(value);
			fail(message);
		}
	}

	private static void assertFalse(String message, boolean value) {
		if (value) {
			StringBuilder m = new StringBuilder(message);
			m.append("; expected: "); //$NON-NLS-1$
			m.append(Boolean.TRUE);
			m.append("; actual: "); //$NON-NLS-1$
			m.append(value);
			fail(message);
		}
	}

	private static void assertNotNull(String message, Object actual) {
		if (actual==null) {
			StringBuilder m = new StringBuilder(message);
			m.append("; no null expected"); //$NON-NLS-1$
			m.append("; actual: "); //$NON-NLS-1$
			m.append(actual);
			fail(m.toString());
		}
	}

}
