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

import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractImmutablePoint3DTest;
import org.arakhne.afc.math.geometry.d3.d.Sphere3d;
import org.junit.jupiter.api.DisplayName;

@DisplayName("ImmutablePoint3D")
@SuppressWarnings("all")
public class ImmutablePoint3DTest extends AbstractImmutablePoint3DTest {

	@Override
	public Shape3D createSphere(double x, double y, double z, double radius) {
		return new Sphere3d(x, y, z, radius);
	}
	
}
