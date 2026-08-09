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

package org.arakhne.afc.math.geometry.d3.tests.d;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractOrientedPoint3DTestCase;
import org.arakhne.afc.math.geometry.d3.d.OrientedPoint3d;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Sphere3d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
@DisplayName("OrientedPoint3d")
public class OrientedPoint3dTest extends AbstractOrientedPoint3DTestCase<OrientedPoint3d, Vector3d, Quaternion4d> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Vector3d createVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Override
	public OrientedPoint3d createTuple(double x, double y, double z) {
		return new OrientedPoint3d(x, y, z);
	}

	@Override
	public Shape3D createSphere(double x, double y, double z, double radius) {
		return new Sphere3d(x, y, z, radius);
	}

}
