/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d1.tests.afp;

import org.arakhne.afc.math.geometry.base.d1.Point1D;
import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d1.Vector1D;
import org.arakhne.afc.math.geometry.d1.afp.Rectangle1afp;
import org.arakhne.afc.math.geometry.d1.afp.Shape1afp;
import org.arakhne.afc.math.geometry.d1.tests.AbstractShape1DTestCase;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("all")
public abstract class AbstractShape1afpTestCase<
			SH extends Shape1afp<?, ? super SH, ?, ?, ? super SG, ?>,
			SG extends Segment1D<?, ?>,
			B extends Rectangle1afp<?, ?, ?, ?, ?, ?>>
		extends AbstractShape1DTestCase<SH, SG> {

	/** Shape factory.
	 */
	protected TestShapeFactory<? extends Point1D, ? extends Vector1D, B> factory;

	@BeforeEach
	@Override
	public void setUp() {
		this.factory = createFactory();
		super.setUp();
	}
	
	protected abstract TestShapeFactory<? extends Point1D, ? extends Vector1D, B> createFactory();

	public final Point1D createPoint(double x, double y) {
		return factory.createPoint(getSG(), x, y);
	}

	public final Vector1D createVector(double x, double y) {
		return factory.createVector(getSG(), x, y);
	}

	public final Rectangle1afp createBox(double x, double y, double width, double height) {
		return this.factory.createBox(getSG(), x, y, width, height);
	}

}
