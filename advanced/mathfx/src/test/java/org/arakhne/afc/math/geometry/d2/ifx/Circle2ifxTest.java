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

package org.arakhne.afc.math.geometry.d2.ifx;

import javafx.beans.property.IntegerProperty;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d2.ai.AbstractCircle2aiTest;

@SuppressWarnings("all")
public class Circle2ifxTest extends AbstractCircle2aiTest<Circle2ifx, Rectangle2ifx> {

	@Override
	protected TestShapeFactory2ifx createFactory() {
		return TestShapeFactory2ifx.SINGLETON;
	}
	
	@Test
	public void xProperty() {
		IntegerProperty property = this.shape.xProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.xProperty();
		assertSame(property, property2);
		assertEquals(5, property.get());
	}

	@Test
	public void yProperty() {
		IntegerProperty property = this.shape.yProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.yProperty();
		assertSame(property, property2);
		assertEquals(8, property.get());
	}

	@Test
	public void radiusProperty() {
		IntegerProperty property = this.shape.radiusProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.radiusProperty();
		assertSame(property, property2);
		assertEquals(5, property.get());
	}

	@Test
	public void xPropertySetter() {
		assertEquals(5, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
		IntegerProperty property = this.shape.xProperty();
		property.set(345);
		assertEquals(345, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
	}

	@Test
	public void yPropertySetter() {
		assertEquals(5, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
		IntegerProperty property = this.shape.yProperty();
		property.set(345);
		assertEquals(5, this.shape.getX());
		assertEquals(345, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
	}

	@Test
	public void radiusPropertySetter() {
		assertEquals(5, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
		IntegerProperty property = this.shape.radiusProperty();
		property.set(345);
		assertEquals(5, this.shape.getX());
		assertEquals(8, this.shape.getY());
		assertEquals(345, this.shape.getRadius());
	}

	@Test
	@Override
	public void testClone() {
		super.testClone();
		Circle2ifx clone = this.shape.clone();
		assertNotSame(this.shape.xProperty(), clone.xProperty());
		assertNotSame(this.shape.yProperty(), clone.yProperty());
		assertNotSame(this.shape.radiusProperty(), clone.radiusProperty());
	}

}
