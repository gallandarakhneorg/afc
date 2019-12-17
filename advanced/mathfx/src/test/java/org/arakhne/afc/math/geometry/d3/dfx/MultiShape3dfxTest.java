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

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import org.junit.Ignore;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d3.afp.AbstractMultiShape3afpTest;

@SuppressWarnings("all")
public class MultiShape3dfxTest extends AbstractMultiShape3afpTest<MultiShape3dfx<Shape3dfx<?>>, Shape3dfx<?>, RectangularPrism3dfx> {

	@Override
	protected TestShapeFactory3dfx createFactory() {
		return TestShapeFactory3dfx.SINGLETON;
	}

	@Test
	@Ignore
	public void boundingBoxProperty() {
		ObjectProperty<RectangularPrism3dfx> property = this.shape.boundingBoxProperty();
		assertNotNull(property);
		RectangularPrism3dfx box = property.get();
		assertNotNull(box);
		assertEpsilonEquals(-7, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(8, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(20, box.getMaxY());
		assertEpsilonEquals(20, box.getMaxZ());
	}
	
	@Test
	public void elementsProperty() {
		ListProperty<Shape3dfx<?>> property = this.shape.elementsProperty();
		assertNotNull(property);
		assertEquals(2, property.size());
		assertSame(this.firstObject, property.get(0));
		assertSame(this.secondObject, property.get(1));
	}

}
