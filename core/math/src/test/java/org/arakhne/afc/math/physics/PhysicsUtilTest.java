/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import org.junit.Test;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

@SuppressWarnings("all")
public class PhysicsUtilTest extends AbstractMathTestCase {

	@Test
	public void acceleration() {
		assertInlineParameterUsage(PhysicsUtil.class, "acceleration", double.class, double.class, double.class);
	}

	@Test
	public void motionNewtonEuler1Law() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law", double.class, double.class);
	}

	@Test
	public void motionNewtonEuler1Law1D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law1D",
				double.class, double.class, double.class, double.class);
	}

	@Test
	public void motionNewtonEuler1Law1D5Vector2D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law1D5",
				Vector2D.class, double.class, double.class, double.class, Vector2D.class);
	}

	@Test
	public void motionNewtonEuler1Law2D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law2D",
				Vector2D.class, double.class, double.class, double.class, Vector2D.class);
	}
	@Test
	public void motionNewtonEuler1Law2D5() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law2D5",
				Vector3D.class, double.class, double.class, double.class, Vector3D.class);
	}

	@Test
	public void motionNewtonEuler1Law3D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonEuler1Law3D",
				Vector3D.class, double.class, double.class, double.class, Vector3D.class);
	}

	@Test
	public void motionNewtonLaw() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw",
				double.class, double.class, double.class);
	}

	@Test
	public void motionNewtonLaw1D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw1D",
				double.class, double.class, double.class, double.class, double.class, double.class, double.class);
	}

	@Test
	public void motionNewtonLaw1D5() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw1D5",
				Vector2D.class, double.class, double.class, Vector2D.class, double.class, double.class, double.class, Vector2D.class);
	}

	@Test
	public void motionNewtonLaw2D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw2D",
				Vector2D.class, double.class, double.class, Vector2D.class, double.class, double.class, double.class, Vector2D.class);
	}

	@Test
	public void motionNewtonLaw2D5() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw2D5",
				Vector3D.class, double.class, double.class, Vector3D.class, double.class, double.class, double.class, Vector3D.class);
	}

	@Test
	public void motionNewtonLaw3D() {
		assertInlineParameterUsage(PhysicsUtil.class, "motionNewtonLaw3D",
				Vector3D.class, double.class, double.class, Vector3D.class, double.class, double.class, double.class, Vector3D.class);
	}

	@Test
	public void speed() {
		assertInlineParameterUsage(PhysicsUtil.class, "speed", double.class, double.class);
	}
	
}