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

import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.d.AlignedBox3d;
import org.arakhne.afc.math.geometry.d3.d.Path3d;
import org.arakhne.afc.math.geometry.d3.d.Plane3d;
import org.arakhne.afc.math.geometry.d3.d.PlaneXY3d;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPath3dTestCase;
import org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPlane3DTestCase;
import org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPlane3dTestCase;
import org.arakhne.afc.math.geometry.d3.tests.afp.AbstractPlaneXY3dTestCase;
import org.arakhne.afc.math.geometry.d3.tests.afp.TestShapeFactory3d;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
@DisplayName("Plane3d")
public class Plane3dTest extends AbstractPlane3dTestCase<Plane3d, AlignedBox3d> {

	@Override
	protected TestShapeFactory3d<? extends Point3D, ? extends Vector3D, ? extends Quaternion, AlignedBox3d> createFactory() {
		return new BaseTestShapeFactory3d();
	}

	@Override
	protected Plane3d createTestPlane(double a, double b, double c, double d) {
		return new Plane3d(a, b, c, d);
	}

}
