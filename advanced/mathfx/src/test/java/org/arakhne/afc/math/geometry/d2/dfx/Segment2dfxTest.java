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

package org.arakhne.afc.math.geometry.d2.dfx;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.test.geometry.d2.afp.AbstractSegment2afpTest;

@SuppressWarnings("all")
public class Segment2dfxTest extends AbstractSegment2afpTest<Segment2dfx, Rectangle2dfx> {

	@Override
	protected TestShapeFactory2dfx createFactory() {
		return TestShapeFactory2dfx.SINGLETON;
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void x1Property(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getX1());
		
		DoubleProperty property = this.shape.x1Property();
		assertNotNull(property);
		assertEpsilonEquals(0, property.get());
		
		this.shape.setX1(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getX1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void y1Property(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getY1());
		
		DoubleProperty property = this.shape.y1Property();
		assertNotNull(property);
		assertEpsilonEquals(0, property.get());
		
		this.shape.setY1(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getY1());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void x2Property(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getX2());
		
		DoubleProperty property = this.shape.x2Property();
		assertNotNull(property);
		assertEpsilonEquals(1, property.get());
		
		this.shape.setX2(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getX2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void y2Property(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getY2());
		
		DoubleProperty property = this.shape.y2Property();
		assertNotNull(property);
		assertEpsilonEquals(1, property.get());
		
		this.shape.setY2(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getY2());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void boundingBoxProperty(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		ObjectProperty<Rectangle2dfx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		Rectangle2dfx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(0, box.getMinY());
		assertEpsilonEquals(1, box.getMaxX());
		assertEpsilonEquals(1, box.getMaxY());
	}

}
