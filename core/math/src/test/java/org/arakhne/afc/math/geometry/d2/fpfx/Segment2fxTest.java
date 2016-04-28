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
package org.arakhne.afc.math.geometry.d2.fpfx;

import static org.junit.Assert.assertNotNull;

import org.arakhne.afc.math.geometry.d2.afp.AbstractSegment2afpTest;
import org.arakhne.afc.math.geometry.d2.afp.TestShapeFactory;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;

@SuppressWarnings("all")
public class Segment2fxTest extends AbstractSegment2afpTest<Segment2fx, Rectangle2fx> {

	@Override
	protected TestShapeFactory2fx createFactory() {
		return TestShapeFactory2fx.SINGLETON;
	}

	@Test
	public void x1Property() {
		assertEpsilonEquals(0, this.shape.getX1());
		
		DoubleProperty property = this.shape.x1Property();
		assertNotNull(property);
		assertEpsilonEquals(0, property.get());
		
		this.shape.setX1(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getX1());
	}

	@Test
	public void y1Property() {
		assertEpsilonEquals(0, this.shape.getY1());
		
		DoubleProperty property = this.shape.y1Property();
		assertNotNull(property);
		assertEpsilonEquals(0, property.get());
		
		this.shape.setY1(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getY1());
	}

	@Test
	public void x2Property() {
		assertEpsilonEquals(1, this.shape.getX2());
		
		DoubleProperty property = this.shape.x2Property();
		assertNotNull(property);
		assertEpsilonEquals(1, property.get());
		
		this.shape.setX2(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getX2());
	}

	@Test
	public void y2Property() {
		assertEpsilonEquals(1, this.shape.getY2());
		
		DoubleProperty property = this.shape.y2Property();
		assertNotNull(property);
		assertEpsilonEquals(1, property.get());
		
		this.shape.setY2(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getY2());
	}

}