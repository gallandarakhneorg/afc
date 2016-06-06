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

package org.arakhne.afc.math.geometry.d2.dfx;

import static org.junit.Assert.assertNotNull;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d2.afp.AbstractParallelogram2afpTest;

@SuppressWarnings("all")
public class Parallelogram2dfxTest extends AbstractParallelogram2afpTest<Parallelogram2dfx, Rectangle2dfx> {

	@Override
	protected TestShapeFactory2dfx createFactory() {
		return TestShapeFactory2dfx.SINGLETON;
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
	public void secondAxisProperty_setObject() {
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		
		UnitVectorProperty property = this.shape.secondAxisProperty();
		assertNotNull(property);
		assertEpsilonEquals(vx, property.getX());
		assertEpsilonEquals(vy, property.getY());
		
		this.shape.setSecondAxis(0.500348, 0.865824);
		assertEpsilonEquals(0.500348, property.getX());
		assertEpsilonEquals(0.865824, property.getY());
	}

	@Test
	public void secondAxisProperty_setProperty() {
		assertEpsilonEquals(vx, this.shape.getSecondAxisX());
		assertEpsilonEquals(vy, this.shape.getSecondAxisY());
		
		UnitVectorProperty property = this.shape.secondAxisProperty();
		assertNotNull(property);
		assertEpsilonEquals(vx, property.getX());
		assertEpsilonEquals(vy, property.getY());
		
		property.set(0.500348, 0.865824);
		assertEpsilonEquals(0.500348, property.getX());
		assertEpsilonEquals(0.865824, property.getY());
	}

	@Test(expected = AssertionError.class)
	public void secondAxisProperty_setProperty_notUnitVector() {
		UnitVectorProperty property = this.shape.secondAxisProperty();
		property.set(456.159, 789.357);
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
		ObjectProperty<Rectangle2dfx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		Rectangle2dfx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(pHx, box.getMinX());
		assertEpsilonEquals(pEy, box.getMinY());
		assertEpsilonEquals(pFx, box.getMaxX());
		assertEpsilonEquals(pGy, box.getMaxY());
	}

}
