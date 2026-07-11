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

package org.arakhne.afc.math.physics;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;

import org.arakhne.afc.math.geometry.base.d1.Vector1D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PhysicsUtil")
@SuppressWarnings("all")
public class PhysicsUtilTest extends AbstractTestCase {

	@DisplayName("acceleration")
	@Test
	public void acceleration() {
		assertInlineParameterUsage(PhysicsUtil.class, "acceleration", double.class, double.class, double.class); //$NON-NLS-1$
	}

	@DisplayName("motionNewtonEuler1Law")
	@Test
	public void motionNewtonEuler1Law() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law", double.class, double.class); //$NON-NLS-1$
	}

	@DisplayName("motionNewtonEuler1Law1D")
	@Test
	public void motionNewtonEuler1Law1D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law1D", //$NON-NLS-1$
				double.class, double.class, double.class, double.class);
	}

	@DisplayName("motionNewtonEuler1Law1D5")
	@Test
	public void motionNewtonEuler1Law1D5Vector1D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law1D5", //$NON-NLS-1$
				Vector1D.class, double.class, double.class, double.class, Vector1D.class);
	}

	@DisplayName("motionNewtonEuler1Law2D")
	@Test
	public void motionNewtonEuler1Law2D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law2D", //$NON-NLS-1$
				Vector2D.class, double.class, double.class, double.class, Vector2D.class);
	}
	
	@DisplayName("motionNewtonEuler1Law2D5")
	@Test
	public void motionNewtonEuler1Law2D5() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law2D5", //$NON-NLS-1$
				Vector3D.class, double.class, double.class, double.class, Vector3D.class);
	}

	@DisplayName("motionNewtonEuler1Law3D")
	@Test
	public void motionNewtonEuler1Law3D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law3D", //$NON-NLS-1$
				Vector3D.class, double.class, double.class, double.class, Vector3D.class);
	}

	@DisplayName("motionNewtonLaw")
	@Test
	public void motionNewtonLaw() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw", //$NON-NLS-1$
				double.class, double.class, double.class);
	}

	@DisplayName("motionNewtonLaw1D")
	@Test
	public void motionNewtonLaw1D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw1D", //$NON-NLS-1$
				double.class, double.class, double.class, double.class, double.class, double.class, double.class);
	}

	@DisplayName("motionNewtonLaw1D5")
	@Test
	public void motionNewtonLaw1D5() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw1D5", //$NON-NLS-1$
				Vector1D.class, double.class, double.class, Vector1D.class, double.class, double.class, double.class, Vector1D.class);
	}

	@DisplayName("motionNewtonLaw2D")
	@Test
	public void motionNewtonLaw2D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw2D", //$NON-NLS-1$
				Vector2D.class, double.class, double.class, Vector2D.class, double.class, double.class, double.class, Vector2D.class);
	}

	@DisplayName("motionNewtonLaw2D5")
	@Test
	public void motionNewtonLaw2D5() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw2D5", //$NON-NLS-1$
				Vector3D.class, double.class, double.class, Vector3D.class, double.class, double.class, double.class, Vector3D.class);
	}

	@DisplayName("motionNewtonLaw3D")
	@Test
	public void motionNewtonLaw3D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw3D", //$NON-NLS-1$
				Vector3D.class, double.class, double.class, Vector3D.class, double.class, double.class, double.class, Vector3D.class);
	}

	@DisplayName("speed")
	@Test
	public void speed() {
		assertInlineParameterUsage(PhysicsUtil.class, "speed", double.class, double.class); //$NON-NLS-1$
	}
	
}
