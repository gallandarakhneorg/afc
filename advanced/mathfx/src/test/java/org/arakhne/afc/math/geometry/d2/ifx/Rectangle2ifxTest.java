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
import javafx.beans.property.ReadOnlyIntegerProperty;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d2.ai.AbstractRectangle2aiTest;

@SuppressWarnings("all")
public class Rectangle2ifxTest extends AbstractRectangle2aiTest<Rectangle2ifx> {

	@Override
	protected TestShapeFactory2ifx createFactory() {
		return TestShapeFactory2ifx.SINGLETON;
	}

	@Test
	public void minXProperty() {
		IntegerProperty property = this.shape.minXProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.minXProperty();
		assertSame(property, property2);
		assertEquals(5, property.get());
	}

	@Test
	public void minYProperty() {
		IntegerProperty property = this.shape.minYProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.minYProperty();
		assertSame(property, property2);
		assertEquals(8, property.get());
	}

	@Test
	public void maxXProperty() {
		IntegerProperty property = this.shape.maxXProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.maxXProperty();
		assertSame(property, property2);
		assertEquals(15, property.get());
	}

	@Test
	public void maxYProperty() {
		IntegerProperty property = this.shape.maxYProperty();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.maxYProperty();
		assertSame(property, property2);
		assertEquals(13, property.get());
	}

	@Test
	public void minXPropertySetter() {
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
		IntegerProperty property = this.shape.minXProperty();
		property.set(345);
		assertEquals(345, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(345, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@Test
	public void minYPropertySetter() {
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
		IntegerProperty property = this.shape.minYProperty();
		property.set(345);
		assertEquals(5, this.shape.getMinX());
		assertEquals(345, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(345, this.shape.getMaxY());
	}

	@Test
	public void maxXPropertySetter() {
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
		IntegerProperty property = this.shape.maxXProperty();
		property.set(345);
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(345, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@Test
	public void maxYPropertySetter() {
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
		IntegerProperty property = this.shape.maxYProperty();
		property.set(345);
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(345, this.shape.getMaxY());
	}

	@Test
	@Override
	public void testClone() {
		super.testClone();
		Rectangle2ifx clone = this.shape.clone();
		assertNotSame(this.shape.minXProperty(), clone.minXProperty());
		assertNotSame(this.shape.maxXProperty(), clone.maxXProperty());
		assertNotSame(this.shape.minYProperty(), clone.minYProperty());
		assertNotSame(this.shape.maxYProperty(), clone.maxYProperty());
	}

	@Test
	public void widthProperty() {
		assertEpsilonEquals(10, this.shape.getWidth());

		ReadOnlyIntegerProperty property = this.shape.widthProperty();
		assertNotNull(property);
		assertEpsilonEquals(10, property.get());
		
		this.shape.setMinX(7);
		assertEpsilonEquals(8, property.get());

		this.shape.setMinX(-5);
		assertEpsilonEquals(20, property.get());

		this.shape.setMaxX(0);
		assertEpsilonEquals(5, property.get());
	}

	@Test
	public void heightProperty() {
		assertEpsilonEquals(5, this.shape.getHeight());

		ReadOnlyIntegerProperty property = this.shape.heightProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		this.shape.setMinY(9);
		assertEpsilonEquals(4, property.get());

		this.shape.setMinY(-5);
		assertEpsilonEquals(18, property.get());

		this.shape.setMaxY(0);
		assertEpsilonEquals(5, property.get());
	}

}
