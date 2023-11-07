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

package org.arakhne.afc.math.geometry.d3.ifx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.test.geometry.AbstractTuple3DTest;

@SuppressWarnings("all")
@Disabled("temporary")
public class Tuple3ifxTest extends AbstractTuple3DTest<Tuple3ifx> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Tuple3ifx createTuple(double x, double y, double z) {
		return new Tuple3ifx(x, y, z);
	}

	@Test
	public void xProperty() {
		IntegerProperty property = this.t.xProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.t.xProperty();
		assertSame(property, property2);
		assertEquals(1, property.get());
	}
	
	@Test
	public void yProperty() {
	    IntegerProperty property = this.t.yProperty();
	    assertNotNull(property);
	    IntegerProperty property2 = this.t.yProperty();
	    assertSame(property, property2);
	    assertEquals(-2, property.get());
	}

	@Test
	public void zProperty() {
		IntegerProperty property = this.t.zProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.t.zProperty();
		assertSame(property, property2);
		assertEquals(0, property.get());
	}

	@Test
	public void xPropertySetter() {
		assertEquals(1, this.t.ix());
		assertEquals(-2, this.t.iy());
		assertEquals(0, this.t.iz());
		IntegerProperty property = this.t.xProperty();
		property.set(345);
		assertEquals(345, this.t.ix());
		assertEquals(-2, this.t.iy());
		assertEquals(0, this.t.iz());
	}
	
	@Test
	public void yPropertySetter() {
	    assertEquals(1, this.t.ix());
	    assertEquals(-2, this.t.iy());
	    assertEquals(0, this.t.iz());
	    IntegerProperty property = this.t.yProperty();
	    property.set(345);
	    assertEquals(1, this.t.ix());
	    assertEquals(345, this.t.iy());
	    assertEquals(0, this.t.iz());
	}

	@Test
	public void zPropertySetter() {
		assertEquals(1, this.t.ix());
		assertEquals(-2, this.t.iy());
		assertEquals(0, this.t.iz());
		IntegerProperty property = this.t.zProperty();
		property.set(345);
		assertEquals(1, this.t.ix());
		assertEquals(-2, this.t.iy());
		assertEquals(345, this.t.iz());
	}

}
