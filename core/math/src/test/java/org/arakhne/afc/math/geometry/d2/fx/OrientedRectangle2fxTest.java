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

import static org.junit.Assert.assertNotNull;

import org.arakhne.afc.math.geometry.d2.afp.AbstractOrientedRectangle2afpTest;
import org.arakhne.afc.math.geometry.d2.afp.TestShapeFactory;
import org.arakhne.afc.math.geometry.d2.fx.OrientedRectangle2fx;
import org.arakhne.afc.math.geometry.d2.fx.ReadOnlyUnitVectorProperty;
import org.arakhne.afc.math.geometry.d2.fx.Rectangle2fx;
import org.arakhne.afc.math.geometry.d2.fx.UnitVectorProperty;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;

@SuppressWarnings("all")
public class OrientedRectangle2fxTest extends AbstractOrientedRectangle2afpTest<OrientedRectangle2fx, Rectangle2fx> {

	@Override
	protected TestShapeFactory2fx createFactory() {
		return TestShapeFactory2fx.SINGLETON;
	}

	@Test
	public void centerXProperty() {
		assertEpsilonEquals(cx, this.shape.getCenterX());
		
		DoubleProperty property = this.shape.centerXProperty();
		assertNotNull(property);
		assertEpsilonEquals(cx, property.get());
		
		this.shape.setCenterX(456.159);
		assertEpsilonEquals(456.159, property.get());
	}

	@Test
	public void centerYProperty() {
		assertEpsilonEquals(cy, this.shape.getCenterY());
		
		DoubleProperty property = this.shape.centerYProperty();
		assertNotNull(property);
		assertEpsilonEquals(cy, property.get());
		
		this.shape.setCenterY(456.159);
		assertEpsilonEquals(456.159, property.get());
	}

	@Test
	public void firstAxisProperty_setObject() {
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		
		UnitVectorProperty property = this.shape.firstAxisProperty();
		assertNotNull(property);
		assertEpsilonEquals(ux, property.getX());
		assertEpsilonEquals(uy, property.getY());
		
		this.shape.setFirstAxis(0.500348, 0.865824);
		assertEpsilonEquals(0.500348, property.getX());
		assertEpsilonEquals(0.865824, property.getY());
	}

	@Test
	public void firstAxisProperty_setProperty() {
		assertEpsilonEquals(ux, this.shape.getFirstAxisX());
		assertEpsilonEquals(uy, this.shape.getFirstAxisY());
		
		UnitVectorProperty property = this.shape.firstAxisProperty();
		assertNotNull(property);
		assertEpsilonEquals(ux, property.getX());
		assertEpsilonEquals(uy, property.getY());
		
		property.set(0.500348, 0.865824);
		assertEpsilonEquals(0.500348, property.getX());
		assertEpsilonEquals(0.865824, property.getY());
	}

	@Test(expected = AssertionError.class)
	public void firstAxisProperty_setProperty_notUnitVector() {
		UnitVectorProperty property = this.shape.firstAxisProperty();
		property.set(456.159, 789.357);
	}

	@Test
	public void firstAxisExtentProperty() {
		assertEpsilonEquals(e1, this.shape.getFirstAxisExtent());
		
		DoubleProperty property = this.shape.firstAxisExtentProperty();
		assertNotNull(property);
		assertEpsilonEquals(e1, property.get());
		
		this.shape.setFirstAxisExtent(456.159);
		assertEpsilonEquals(456.159, property.get());
	}

	@Test
	public void secondAxisProperty() {
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		
		ReadOnlyUnitVectorProperty property = this.shape.secondAxisProperty();
		assertNotNull(property);
		assertEpsilonEquals(vx, property.getX());
		assertEpsilonEquals(vy, property.getY());
	}

	@Test
	public void secondAxisExtentProperty() {
		assertEpsilonEquals(e2, this.shape.getSecondAxisExtent());
		
		DoubleProperty property = this.shape.secondAxisExtentProperty();
		assertNotNull(property);
		assertEpsilonEquals(e2, property.get());
		
		this.shape.setSecondAxisExtent(456.159);
		assertEpsilonEquals(456.159, property.get());
	}

	@Test
	public void boundingBoxProperty() {
		ObjectProperty<Rectangle2fx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		Rectangle2fx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(pEx, box.getMinX());
		assertEpsilonEquals(pFy, box.getMinY());
		assertEpsilonEquals(pGx, box.getMaxX());
		assertEpsilonEquals(pHy, box.getMaxY());
	}

}
