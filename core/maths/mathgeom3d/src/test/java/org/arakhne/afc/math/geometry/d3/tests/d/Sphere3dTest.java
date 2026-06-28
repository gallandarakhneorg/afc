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

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.d.AlignedBox3d;
import org.arakhne.afc.math.geometry.d3.d.Sphere3d;
import org.arakhne.afc.math.geometry.d3.tests.afp.AbstractSphere3dTestCase;
import org.arakhne.afc.math.geometry.d3.tests.afp.TestShapeFactory3d;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
@DisplayName("Sphere3d")
public class Sphere3dTest extends AbstractSphere3dTestCase<Sphere3d, AlignedBox3d> {

	@Override
	protected TestShapeFactory3d createFactory() {
		return new BaseTestShapeFactory3d();
	}

	@Override
	@Test
	public void getClosestPointToShape3D(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getClosestPointToMultiShape3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void intersectsMultiShape3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

}
