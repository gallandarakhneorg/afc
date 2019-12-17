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

package org.arakhne.afc.math.geometry.d3.dfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d3.afp.AbstractSphere3afpTest;

@SuppressWarnings("all")
public class Sphere3dfxTest extends AbstractSphere3afpTest<Sphere3dfx, RectangularPrism3dfx> {

	@Override
	protected TestShapeFactory3dfx createFactory() {
		return TestShapeFactory3dfx.SINGLETON;
	}

	@Test
	public void xProperty() {
		assertEpsilonEquals(5, this.shape.getX());
		
		DoubleProperty property = this.shape.xProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		this.shape.setX(456.159);
		assertEpsilonEquals(456.159, property.get());
	}
	
	@Test
	public void yProperty() {
	    assertEpsilonEquals(8, this.shape.getY());
	    
	    DoubleProperty property = this.shape.yProperty();
	    assertNotNull(property);
	    assertEpsilonEquals(8, property.get());
	    
	    this.shape.setY(456.159);
	    assertEpsilonEquals(456.159, property.get());
	}

	@Test
	public void zProperty() {
		assertEpsilonEquals(0, this.shape.getZ());
		
		DoubleProperty property = this.shape.zProperty();
		assertNotNull(property);
		assertEpsilonEquals(0, property.get());
		
		this.shape.setZ(456.159);
		assertEpsilonEquals(456.159, property.get());
	}

	@Test
	public void radiusProperty() {
		assertEpsilonEquals(5, this.shape.getRadius());
		
		DoubleProperty property = this.shape.radiusProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		this.shape.setRadius(456.159);
		assertEpsilonEquals(456.159, property.get());
	}

	@Test
	public void boundingBoxProperty() {
		ObjectProperty<RectangularPrism3dfx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		RectangularPrism3dfx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(3, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
	}

}
