/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d2.ifx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.AbstractRectangle2aiTest;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.ai.TestShapeFactory;
import org.junit.Test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;

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