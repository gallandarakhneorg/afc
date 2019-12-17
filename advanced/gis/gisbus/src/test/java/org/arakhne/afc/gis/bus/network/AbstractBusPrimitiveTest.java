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

package org.arakhne.afc.gis.bus.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.bus.network.BusChangeEvent.BusChangeEventType;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationNowhere;
import org.arakhne.afc.testtools.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class AbstractBusPrimitiveTest extends AbstractTestCase {

	private final TestEventHandler eventHandler = new TestEventHandler();
	private AbstractBusPrimitive<?> primitive;

	@Before
	public void setUp() throws Exception {
		this.primitive = new BusStop("BUSSTOP"); //$NON-NLS-1$
		this.primitive.addBusChangeListener(this.eventHandler);
		this.eventHandler.clear();
	}

	@After
	public void tearDown() throws Exception {
		this.primitive.removeBusChangeListener(this.eventHandler);
		this.primitive = null;
	}

	@Test
	public void testGetGeoLocation() {
		GeoLocation loc = this.primitive.getGeoLocation();
		assertNotNull(loc);
		assertTrue(loc instanceof GeoLocationNowhere);
	}

	@Test
    public void testGetColor() {
		assertEquals(AbstractBusPrimitive.DEFAULT_COLOR, this.primitive.getColor());
		this.primitive.setColor(0xFF0000);
		assertEquals(0xFF0000, this.primitive.getColor());
    }

	@Test
    public void testGetColorColor() {
		assertEquals(0x123456, this.primitive.getColor(0x123456));
		this.primitive.setColor(0x00FF00);
		assertEquals(0x00FF00, this.primitive.getColor(0x123456));
		assertEquals(0x00FF00, this.primitive.getRawColor().intValue());
    }

	@Test
    public void testGetRawColor() {
		assertNull(this.primitive.getRawColor());
		this.primitive.setColor(0xFF0000);
		assertEquals(0xFF0000, this.primitive.getRawColor().intValue());
	}

	@Test
	public void testSetColorColor() {
		assertEquals(AbstractBusPrimitive.DEFAULT_COLOR, this.primitive.getColor());
		assertNull(this.primitive.getRawColor());
		this.eventHandler.clear();

		this.primitive.setColor(0xFF0000);

		assertEquals(0xFF0000, this.primitive.getColor());
		assertEquals(0xFF0000, this.primitive.getRawColor().intValue());
		this.eventHandler.assertNoBusChangedEvent();
		this.eventHandler.assertNoBusShapeChangedEvent();
		this.eventHandler.assertBusShapeAttrChangedEvent(BusChangeEventType.STOP_CHANGED, this.primitive);
	}

	@Test
    public void testIsReadOnlyObject() {
    	assertFalse(this.primitive.isReadOnlyObject());
    	this.primitive.setReadOnlyObject(true);
    	assertTrue(this.primitive.isReadOnlyObject());
    	this.primitive.setReadOnlyObject(true);
    	assertTrue(this.primitive.isReadOnlyObject());
    	this.primitive.setReadOnlyObject(false);
    	assertFalse(this.primitive.isReadOnlyObject());
	}

	@Test
	public void testGetFlags() {
		assertEquals(0, this.primitive.getFlags());
		this.primitive.setFlag(32);
		assertEquals(32, this.primitive.getFlags());
	}

	@Test
	public void testHasFlagInt() {
		assertFalse(this.primitive.hasFlag(0x02|0x04));
		this.primitive.setFlag(0x02);
		assertFalse(this.primitive.hasFlag(0x02|0x04));
		assertTrue(this.primitive.hasFlag(0x02));
		assertFalse(this.primitive.hasFlag(0x04));
	}

	@Test
	public void testSwitchFlagInt() {
		assertFalse(this.primitive.hasFlag(0x02|0x04));
		this.primitive.switchFlag(0x04);
		assertFalse(this.primitive.hasFlag(0x02|0x04));
		assertTrue(this.primitive.hasFlag(0x04));
		assertFalse(this.primitive.hasFlag(0x02));

		this.primitive.switchFlag(0x04);
		assertFalse(this.primitive.hasFlag(0x02|0x04));
		assertFalse(this.primitive.hasFlag(0x04));
		assertFalse(this.primitive.hasFlag(0x02));
	}

	@Test
	public void testUnsetFlagInt() {
		assertFalse(this.primitive.hasFlag(0x02|0x04));
		this.primitive.setFlag(0x04);
		assertFalse(this.primitive.hasFlag(0x02|0x04));
		assertTrue(this.primitive.hasFlag(0x04));
		assertFalse(this.primitive.hasFlag(0x02));

		this.primitive.unsetFlag(0x04);
		assertFalse(this.primitive.hasFlag(0x02|0x04));
		assertFalse(this.primitive.hasFlag(0x04));
		assertFalse(this.primitive.hasFlag(0x02));
	}

	@Test
	public void testIsValidPrimitive() {
		assertFalse(this.primitive.isValidPrimitive());
	}

}
