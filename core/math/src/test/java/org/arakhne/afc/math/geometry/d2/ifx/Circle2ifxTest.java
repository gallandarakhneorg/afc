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
import org.arakhne.afc.math.geometry.d2.ai.AbstractCircle2aiTest;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.ai.TestShapeFactory;
import org.arakhne.afc.math.geometry.d2.i.Rectangle2i;
import org.arakhne.afc.math.geometry.d2.i.TestShapeFactory2i;
import org.junit.Test;

import javafx.beans.property.IntegerProperty;

@SuppressWarnings("all")
public class Circle2ifxTest extends AbstractCircle2aiTest<Circle2ifx, Rectangle2ifx> {

	@Override
	protected TestShapeFactory<Rectangle2ifx> createFactory() {
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