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

package org.arakhne.afc.math.geometry.d3.dfx;

import static org.junit.Assert.assertNotNull;

import org.arakhne.afc.math.geometry.d2.dfx.Rectangle2dfx;
import org.arakhne.afc.math.geometry.d3.afp.AbstractRectangularPrism3afpTest;
import org.junit.Test;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;

@SuppressWarnings("all")
public class RectangularPrism3dfxTest extends AbstractRectangularPrism3afpTest<RectangularPrism3dfx, RectangularPrism3dfx> {

	@Override
	protected TestShapeFactory3dfx createFactory() {
		return TestShapeFactory3dfx.SINGLETON;
	}

	@Test
	public void minXProperty() {
		assertEpsilonEquals(5, this.shape.getMinX());
		
		DoubleProperty property = this.shape.minXProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		this.shape.setMinX(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getMaxX());
	}

	@Test
	public void maxXProperty() {
		assertEpsilonEquals(10, this.shape.getMaxX());
		
		DoubleProperty property = this.shape.maxXProperty();
		assertNotNull(property);
		assertEpsilonEquals(10, property.get());
		
		this.shape.setMaxX(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(5, this.shape.getMinX());
	}
	
	@Test
	public void minYProperty() {
	    assertEpsilonEquals(8, this.shape.getMinY());
	    
	    DoubleProperty property = this.shape.minYProperty();
	    assertNotNull(property);
	    assertEpsilonEquals(8, property.get());
	    
	    this.shape.setMinY(456.159);
	    assertEpsilonEquals(456.159, property.get());
	    
	    assertEpsilonEquals(456.159, this.shape.getMaxY());
	}
	
	@Test
	public void maxYProperty() {
	    assertEpsilonEquals(18, this.shape.getMaxY());
	    
	    DoubleProperty property = this.shape.maxYProperty();
	    assertNotNull(property);
	    assertEpsilonEquals(18, property.get());
	    
	    this.shape.setMaxY(456.159);
	    assertEpsilonEquals(456.159, property.get());
	    
	    assertEpsilonEquals(8, this.shape.getMinY());
	}

	@Test
	public void minZProperty() {
		assertEpsilonEquals(2, this.shape.getMinZ());
		
		DoubleProperty property = this.shape.minZProperty();
		assertNotNull(property);
		assertEpsilonEquals(2, property.get());
		
		this.shape.setMinZ(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(456.159, this.shape.getMaxZ());
	}

	@Test
	public void maxZProperty() {
		assertEpsilonEquals(10, this.shape.getMaxZ());
		
		DoubleProperty property = this.shape.maxZProperty();
		assertNotNull(property);
		assertEpsilonEquals(10, property.get());
		
		this.shape.setMaxZ(456.159);
		assertEpsilonEquals(456.159, property.get());
		
		assertEpsilonEquals(2, this.shape.getMinZ());
	}

	@Test
	public void widthProperty() {
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
	
	@Test
	public void heightProperty() {
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

	@Test
	public void depthProperty() {
		assertEpsilonEquals(10, this.shape.getDepth());

		ReadOnlyDoubleProperty property = this.shape.depthProperty();
		assertNotNull(property);
		assertEpsilonEquals(10, property.get());
		
		this.shape.setMinZ(9);
		assertEpsilonEquals(1, property.get());

		this.shape.setMinZ(-5);
		assertEpsilonEquals(15, property.get());

		this.shape.setMaxZ(0);
		assertEpsilonEquals(5, property.get());
	}
	
	@Test
	public void boundingBoxProperty() {
		ObjectProperty<RectangularPrism3dfx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		RectangularPrism3dfx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(2, box.getMinZ());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(18, box.getMaxY());
		assertEpsilonEquals(10, box.getMaxZ());
	}

}