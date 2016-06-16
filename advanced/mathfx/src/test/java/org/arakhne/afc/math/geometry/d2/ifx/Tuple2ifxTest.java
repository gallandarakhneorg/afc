/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.ifx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javafx.beans.property.IntegerProperty;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d2.AbstractTuple2DTest;

@SuppressWarnings("all")
public class Tuple2ifxTest extends AbstractTuple2DTest<Tuple2ifx> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Tuple2ifx createTuple(double x, double y) {
		return new Tuple2ifx(x, y);
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
	public void xPropertySetter() {
		assertEquals(1, this.t.ix());
		assertEquals(-2, this.t.iy());
		IntegerProperty property = this.t.xProperty();
		property.set(345);
		assertEquals(345, this.t.ix());
		assertEquals(-2, this.t.iy());
	}

	@Test
	public void yPropertySetter() {
		assertEquals(1, this.t.ix());
		assertEquals(-2, this.t.iy());
		IntegerProperty property = this.t.yProperty();
		property.set(345);
		assertEquals(1, this.t.ix());
		assertEquals(345, this.t.iy());
	}

}
