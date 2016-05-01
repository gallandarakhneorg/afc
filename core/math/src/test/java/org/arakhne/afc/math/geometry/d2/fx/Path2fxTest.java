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
package org.arakhne.afc.math.geometry.d2.fx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.afp.AbstractPath2afpTest;
import org.arakhne.afc.math.geometry.d2.fx.Path2fx;
import org.arakhne.afc.math.geometry.d2.fx.Rectangle2fx;
import org.junit.Test;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;

@SuppressWarnings("all")
public class Path2fxTest extends AbstractPath2afpTest<Path2fx, Rectangle2fx> {

	@Override
	protected TestShapeFactory2fx createFactory() {
		return TestShapeFactory2fx.SINGLETON;
	}

	@Test
	public void boundingBoxProperty() {
		ObjectProperty<Rectangle2fx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		Rectangle2fx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
	}
	
	@Test
	public void controlPointBoundingBoxProperty() {
		ObjectProperty<Rectangle2fx> property = this.shape.controlPointBoundingBoxProperty();
		assertNotNull(property);
		Rectangle2fx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(5, box.getMaxY());
	}
	
	@Test
	public void coordinatesProperty() {
		ReadOnlyListProperty<Double> property = this.shape.coordinatesProperty();
		assertNotNull(property);
		assertEquals(14, property.size());
		assertEpsilonEquals(0, property.get(0));
		assertEpsilonEquals(0, property.get(1));
		assertEpsilonEquals(1, property.get(2));
		assertEpsilonEquals(1, property.get(3));
		assertEpsilonEquals(3, property.get(4));
		assertEpsilonEquals(0, property.get(5));
		assertEpsilonEquals(4, property.get(6));
		assertEpsilonEquals(3, property.get(7));
		assertEpsilonEquals(5, property.get(8));
		assertEpsilonEquals(-1, property.get(9));
		assertEpsilonEquals(6, property.get(10));
		assertEpsilonEquals(5, property.get(11));
		assertEpsilonEquals(7, property.get(12));
		assertEpsilonEquals(-5, property.get(13));
	}
	
	@Test
	public void isCurvedProperty() {
		BooleanProperty property = this.shape.isCurvedProperty();
		assertNotNull(property);
		assertTrue(property.get());
	}
	
	@Test
	public void isEmptyProperty() {
		BooleanProperty property = this.shape.isEmptyProperty();
		assertNotNull(property);
		assertFalse(property.get());
	}
	
	@Test
	public void isMultiPartsProperty() {
		BooleanProperty property = this.shape.isMultiPartsProperty();
		assertNotNull(property);
		assertFalse(property.get());
	}
	
	@Test
	public void isPolygonProperty() {
		BooleanProperty property = this.shape.isPolygonProperty();
		assertNotNull(property);
		assertFalse(property.get());
	}
	
	@Test
	public void isPolylineProperty() {
		BooleanProperty property = this.shape.isPolylineProperty();
		assertNotNull(property);
		assertFalse(property.get());
	}
	
	@Test
	public void lengthProperty() {
		DoubleProperty property = this.shape.lengthProperty();
		assertNotNull(property);
		assertEpsilonEquals(14.71628, property.get());
	}
	
	@Test
	public void typesProperty() {
		ReadOnlyListProperty<PathElementType> property = this.shape.typesProperty();
		assertNotNull(property);
		assertEquals(4, property.size());
		assertSame(PathElementType.MOVE_TO, property.get(0));
		assertSame(PathElementType.LINE_TO, property.get(1));
		assertSame(PathElementType.QUAD_TO, property.get(2));
		assertSame(PathElementType.CURVE_TO, property.get(3));
		this.shape.closePath();
		assertEquals(5, property.size());
		assertSame(PathElementType.CLOSE, property.get(4));
	}
	
	@Test
	public void windingRuleProperty() {
		ObjectProperty<PathWindingRule> property = this.shape.windingRuleProperty();
		assertNotNull(property);
		assertSame(PathWindingRule.NON_ZERO, property.get());
		this.shape.setWindingRule(PathWindingRule.EVEN_ODD);
		assertSame(PathWindingRule.EVEN_ODD, property.get());
		property.set(PathWindingRule.NON_ZERO);
		assertSame(PathWindingRule.NON_ZERO, property.get());
	}

}