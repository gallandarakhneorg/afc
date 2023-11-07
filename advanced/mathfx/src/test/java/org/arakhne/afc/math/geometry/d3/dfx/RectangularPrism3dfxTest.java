/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d3.dfx;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.test.geometry.d3.afp.AbstractAlignedBox3afpTest;

@SuppressWarnings("all")
@Disabled("temporary")
public class AlignedBox3dfxTest extends AbstractAlignedBox3afpTest<AlignedBox3dfx, AlignedBox3dfx> {

	@Override
	protected TestShapeFactory3dfx createFactory() {
		return TestShapeFactory3dfx.SINGLETON;
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void minXProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getMinX());
		
		DoubleProperty property = this.shape.minXProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		this.shape.setMinX(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getMaxX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void maxXProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getMaxX());
		
		DoubleProperty property = this.shape.maxXProperty();
		assertNotNull(property);
		assertEpsilonEquals(10, property.get());
		
		this.shape.setMaxX(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(5, this.shape.getMinX());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void minYProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(8, this.shape.getMinY());
	    
	    DoubleProperty property = this.shape.minYProperty();
	    assertNotNull(property);
	    assertEpsilonEquals(8, property.get());
	    
	    this.shape.setMinY(456.159);
	    assertEpsilonEquals(456.159, property.get());
	    
	    assertEpsilonEquals(456.159, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void maxYProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(18, this.shape.getMaxY());
	    
	    DoubleProperty property = this.shape.maxYProperty();
	    assertNotNull(property);
	    assertEpsilonEquals(18, property.get());
	    
	    this.shape.setMaxY(456.159);
	    assertEpsilonEquals(456.159, property.get());
	    
	    assertEpsilonEquals(8, this.shape.getMinY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void minZProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getMinZ());
		
		DoubleProperty property = this.shape.minZProperty();
		assertNotNull(property);
		assertEpsilonEquals(0, property.get());
		
		this.shape.setMinZ(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void maxZProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getMaxZ());
		
		DoubleProperty property = this.shape.maxZProperty();
		assertNotNull(property);
		assertEpsilonEquals(0, property.get());
		
		this.shape.setMaxZ(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(0, this.shape.getMinZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void widthProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	@EnumSource(CoordinateSystem3D.class)
	public void heightProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	@EnumSource(CoordinateSystem3D.class)
	public void depthProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getDepth());

		ReadOnlyDoubleProperty property = this.shape.depthProperty();
		assertNotNull(property);
		assertEpsilonEquals(0, property.get());
		
		this.shape.setMinZ(9);
		assertEpsilonEquals(0, property.get());

		this.shape.setMinZ(-5);
		assertEpsilonEquals(14, property.get());

		this.shape.setMaxZ(0);
		assertEpsilonEquals(5, property.get());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void boundingBoxProperty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		ObjectProperty<AlignedBox3dfx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		AlignedBox3dfx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(0, box.getMinZ());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(18, box.getMaxY());
		assertEpsilonEquals(0, box.getMaxZ());
	}

}
