/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.test.geometry.d2.afp.AbstractRoundRectangle2afpTest;

@SuppressWarnings("all")
public class RoundRectangle2dfxTest extends AbstractRoundRectangle2afpTest<RoundRectangle2dfx, Rectangle2dfx> {

	@Override
	protected TestShapeFactory2dfx createFactory() {
		return TestShapeFactory2dfx.SINGLETON;
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void minXProperty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getMinX());
		
		DoubleProperty property = this.shape.minXProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		this.shape.setMinX(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getMaxX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void maxXProperty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getMaxX());
		
		DoubleProperty property = this.shape.maxXProperty();
		assertNotNull(property);
		assertEpsilonEquals(10, property.get());
		
		this.shape.setMaxX(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(5, this.shape.getMinX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void minYProperty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(8, this.shape.getMinY());
		
		DoubleProperty property = this.shape.minYProperty();
		assertNotNull(property);
		assertEpsilonEquals(8, property.get());
		
		this.shape.setMinY(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void maxYProperty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(18, this.shape.getMaxY());
		
		DoubleProperty property = this.shape.maxYProperty();
		assertNotNull(property);
		assertEpsilonEquals(18, property.get());
		
		this.shape.setMaxY(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(8, this.shape.getMinY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcWidthProperty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.1, this.shape.getArcWidth());
		
		DoubleProperty property = this.shape.arcWidthProperty();
		assertNotNull(property);
		assertEpsilonEquals(.1, property.get());
		
		this.shape.setArcWidth(.125);
		assertEpsilonEquals(.125, property.get());
		
		assertEpsilonEquals(.125, this.shape.getArcWidth());

		property.set(-.2);
		assertEpsilonEquals(0, this.shape.getArcWidth());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcHeightProperty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.2, this.shape.getArcHeight());
		
		DoubleProperty property = this.shape.arcHeightProperty();
		assertNotNull(property);
		assertEpsilonEquals(.2, property.get());
		
		this.shape.setArcHeight(.125);
		assertEpsilonEquals(.125, property.get());
		
		assertEpsilonEquals(.125, this.shape.getArcHeight());

		property.set(-.2);
		assertEpsilonEquals(0, this.shape.getArcHeight());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void widthProperty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getWidth());

		ReadOnlyDoubleProperty property = this.shape.widthProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		this.shape.setMinX(7);
		assertEpsilonEquals(3, property.get());

		this.shape.setMinX(-5);
		assertEpsilonEquals(15, property.get());

		this.shape.setMaxX(0);
		assertEpsilonEquals(5, property.get());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void heightProperty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getHeight());

		ReadOnlyDoubleProperty property = this.shape.heightProperty();
		assertNotNull(property);
		assertEpsilonEquals(10, property.get());
		
		this.shape.setMinY(9);
		assertEpsilonEquals(9, property.get());

		this.shape.setMinY(-5);
		assertEpsilonEquals(23, property.get());

		this.shape.setMaxY(0);
		assertEpsilonEquals(5, property.get());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void boundingBoxProperty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		ObjectProperty<Rectangle2dfx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		Rectangle2dfx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(18, box.getMaxY());
	}

}
