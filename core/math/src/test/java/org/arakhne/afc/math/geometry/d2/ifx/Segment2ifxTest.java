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

import org.arakhne.afc.math.geometry.d2.ai.AbstractSegment2aiTest;
import org.arakhne.afc.math.geometry.d2.ai.TestShapeFactory;
import org.junit.Test;

import javafx.beans.property.IntegerProperty;

@SuppressWarnings("all")
public class Segment2ifxTest extends AbstractSegment2aiTest<Segment2ifx, Rectangle2ifx> {

	@Override
	protected TestShapeFactory2ifx createFactory() {
		return TestShapeFactory2ifx.SINGLETON;
	}

	@Test
	public void x1Property() {
		IntegerProperty property = this.shape.x1Property();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.x1Property();
		assertSame(property, property2);
		assertEquals(0, property.get());
	}

	@Test
	public void y1Property() {
		IntegerProperty property = this.shape.y1Property();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.y1Property();
		assertSame(property, property2);
		assertEquals(0, property.get());
	}

	@Test
	public void x2Property() {
		IntegerProperty property = this.shape.x2Property();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.x2Property();
		assertSame(property, property2);
		assertEquals(10, property.get());
	}

	@Test
	public void y2Property() {
		IntegerProperty property = this.shape.y2Property();
		assertNotNull(property);
		IntegerProperty property2 = this.shape.y2Property();
		assertSame(property, property2);
		assertEquals(5, property.get());
	}

	@Test
	public void x1PropertySetter() {
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
		IntegerProperty property = this.shape.x1Property();
		property.set(345);
		assertEquals(345, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void y1PropertySetter() {
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
		IntegerProperty property = this.shape.y1Property();
		property.set(345);
		assertEquals(0, this.shape.getX1());
		assertEquals(345, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void x2PropertySetter() {
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
		IntegerProperty property = this.shape.x2Property();
		property.set(345);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(345, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
	}

	@Test
	public void y2PropertySetter() {
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(5, this.shape.getY2());
		IntegerProperty property = this.shape.y2Property();
		property.set(345);
		assertEquals(0, this.shape.getX1());
		assertEquals(0, this.shape.getY1());
		assertEquals(10, this.shape.getX2());
		assertEquals(345, this.shape.getY2());
	}

	@Test
	@Override
	public void testClone() {
		super.testClone();
		Segment2ifx clone = this.shape.clone();
		assertNotSame(this.shape.x1Property(), clone.x1Property());
		assertNotSame(this.shape.y1Property(), clone.y1Property());
		assertNotSame(this.shape.x2Property(), clone.x2Property());
		assertNotSame(this.shape.y2Property(), clone.y2Property());
	}

}