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

package org.arakhne.afc.math.geometry.base.tests.d3;

import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractPoint2DTestCase;
import org.junit.jupiter.api.DisplayName;

@DisplayName("InnerComputationPoint3D")
@SuppressWarnings("all")
public class InnerComputationPoint3DTest extends AbstractPoint2DTestCase<InnerComputationPoint2D, InnerComputationVector2D, Point2D> {

	@Override
	public InnerComputationPoint2D createPoint(double x, double y) {
		return new InnerComputationPoint2D(x, y);
	}

	@Override
	public InnerComputationVector2D createVector(double x, double y) {
		return new InnerComputationVector2D(x, y);
	}

	@Override
	public Point2D createTuple(double x, double y) {
		return new InnerComputationPoint2D(x, y);
	}

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
}
