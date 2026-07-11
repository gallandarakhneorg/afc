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
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationQuaternion;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractVector3DTestCase;

@SuppressWarnings("all")
public class InnerComputationVector3DTest extends AbstractVector3DTestCase<InnerComputationVector3D, InnerComputationPoint3D, InnerComputationQuaternion, Vector3D> {

	@Override
	public InnerComputationVector3D createVector(double x, double y, double z) {
		return new InnerComputationVector3D(x, y, z);
	}

	@Override
	public InnerComputationPoint3D createPoint(double x, double y, double z) {
		return new InnerComputationPoint3D(x,  y, z);
	}

	@Override
	public Vector3D createTuple(double x, double y, double z) {
		return new InnerComputationVector3D(x, y, z);
	}

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
}
