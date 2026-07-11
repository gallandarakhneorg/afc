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

import static org.arakhne.afc.math.MathConstants.DEMI_PI;
import static org.arakhne.afc.math.MathConstants.ONE_HALF_PI;
import static org.arakhne.afc.math.MathConstants.PI;
import static org.arakhne.afc.math.MathConstants.QUARTER_PI;
import static org.arakhne.afc.math.MathConstants.SQRT_2;
import static org.arakhne.afc.math.MathConstants.THREE_QUARTER_PI;
import static org.arakhne.afc.math.MathConstants.TWO_PI;

import org.arakhne.afc.math.geometry.base.d1.InnerComputationVector1D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationVector3D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link JavaPhysicsEngine}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("JavaPhysicsEngine")
@SuppressWarnings("all")
public class JavaPhysicsEngineTest extends AbstractMathTestCase {

	private JavaPhysicsEngine engine;
	
	@BeforeEach
	public void setUp() throws Exception {
		engine = new JavaPhysicsEngine();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		engine = null;
	}
	
	private Vector2d makeVect(double x, double y, double n) {
		Vector2d v = new Vector2d(x,y);
		if (v.getLengthSquared()!=0.) v.normalize();
		v.scale(n);
		return v;
	}

	private InnerComputationVector3D makeVect(double x, double y, double z, double n) {
		final var v = new InnerComputationVector3D(x,y,z);
		if (v.getLengthSquared()!=0.) v.normalize();
		v.scale((double) n);
		return v;
	}

	@DisplayName("speed")
	@Nested
	public class Speed {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(0., engine.speed(0., 1.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEpsilonEquals(PI, engine.speed(PI, 1.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(-PI, engine.speed(-PI, 1.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEpsilonEquals(0., engine.speed(0., .5));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEpsilonEquals(TWO_PI, engine.speed(PI, .5));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEpsilonEquals(-TWO_PI, engine.speed(-PI, .5));
		}

	}

	@DisplayName("acceleration")
	@Nested
	public class Acceleration {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(0., engine.acceleration(0., 0., 1.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEpsilonEquals(PI, engine.acceleration(0., PI, 1.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(-PI, engine.acceleration(0., -PI, 1.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEpsilonEquals(-PI, engine.acceleration(PI, 0., 1.));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEpsilonEquals(0., engine.acceleration(PI, PI, 1.));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEpsilonEquals(-TWO_PI, engine.acceleration(PI, -PI, 1.));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEpsilonEquals(PI, engine.acceleration(-PI, 0., 1.));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEpsilonEquals(TWO_PI, engine.acceleration(-PI, PI, 1.));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEpsilonEquals(0., engine.acceleration(-PI, -PI, 1.));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEpsilonEquals(0., engine.acceleration(0., 0., .5));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertEpsilonEquals(TWO_PI, engine.acceleration(0., PI, .5));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertEpsilonEquals(-TWO_PI, engine.acceleration(0., -PI, .5));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertEpsilonEquals(-TWO_PI, engine.acceleration(PI, 0., .5));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertEpsilonEquals(0., engine.acceleration(PI, PI, .5));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertEpsilonEquals(-2.*TWO_PI, engine.acceleration(PI, -PI, .5));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertEpsilonEquals(TWO_PI, engine.acceleration(-PI, 0., .5));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertEpsilonEquals(2*TWO_PI, engine.acceleration(-PI, PI, .5));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertEpsilonEquals(0., engine.acceleration(-PI, -PI, .5));
		}

	}

	@DisplayName("motionNewtonLaw")
	@Nested
	public class MotionNewtonLaw {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(0., engine.motionNewtonLaw(0., 0., 1.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEpsilonEquals(DEMI_PI, engine.motionNewtonLaw(0., PI, 1.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(-DEMI_PI, engine.motionNewtonLaw(0., -PI, 1.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEpsilonEquals(PI, engine.motionNewtonLaw(PI, 0., 1.));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEpsilonEquals(ONE_HALF_PI, engine.motionNewtonLaw(PI, PI, 1.));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEpsilonEquals(DEMI_PI, engine.motionNewtonLaw(PI, -PI, 1.));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEpsilonEquals(-PI, engine.motionNewtonLaw(-PI, 0., 1.));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEpsilonEquals(-DEMI_PI, engine.motionNewtonLaw(-PI, PI, 1.));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEpsilonEquals(-ONE_HALF_PI, engine.motionNewtonLaw(-PI, -PI, 1.));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEpsilonEquals(0., engine.motionNewtonLaw(0., 0., .5));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertEpsilonEquals(QUARTER_PI/2., engine.motionNewtonLaw(0., PI, .5));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertEpsilonEquals(-QUARTER_PI/2., engine.motionNewtonLaw(0., -PI, .5));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertEpsilonEquals(DEMI_PI, engine.motionNewtonLaw(PI, 0., .5));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertEpsilonEquals(DEMI_PI+QUARTER_PI/2., engine.motionNewtonLaw(PI, PI, .5));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertEpsilonEquals(THREE_QUARTER_PI/2., engine.motionNewtonLaw(PI, -PI, .5));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertEpsilonEquals(-DEMI_PI, engine.motionNewtonLaw(-PI, 0., .5));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertEpsilonEquals(-THREE_QUARTER_PI/2., engine.motionNewtonLaw(-PI, PI, .5));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertEpsilonEquals((-PI-QUARTER_PI)/2., engine.motionNewtonLaw(-PI, -PI, .5));
		}

	}

	@DisplayName("motionNewtonLaw1D")
	@Nested
	public class MotionNewtonLaw1D {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(0., engine.motionNewtonLaw1D(0., 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, 1.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEpsilonEquals(QUARTER_PI, engine.motionNewtonLaw1D(0., 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, 1.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(-QUARTER_PI/2., engine.motionNewtonLaw1D(0., 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, 1.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEpsilonEquals(DEMI_PI, engine.motionNewtonLaw1D(PI, 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, 1.));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEpsilonEquals(DEMI_PI, engine.motionNewtonLaw1D(PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, 1.));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEpsilonEquals(DEMI_PI, engine.motionNewtonLaw1D(PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, 1.));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertEpsilonEquals(-DEMI_PI, engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, 1.));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertEpsilonEquals(-DEMI_PI, engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, 1.));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEpsilonEquals(-DEMI_PI, engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, 1.));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEpsilonEquals(0., engine.motionNewtonLaw1D(0.,0, DEMI_PI,  0., -QUARTER_PI, DEMI_PI, .5));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertEpsilonEquals(QUARTER_PI/4., engine.motionNewtonLaw1D(0., 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, .5));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertEpsilonEquals(-QUARTER_PI/8., engine.motionNewtonLaw1D(0., 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, .5));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertEpsilonEquals(QUARTER_PI, engine.motionNewtonLaw1D(PI, 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, .5));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertEpsilonEquals(QUARTER_PI, engine.motionNewtonLaw1D(PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, .5));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertEpsilonEquals(QUARTER_PI, engine.motionNewtonLaw1D(PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, .5));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertEpsilonEquals(-DEMI_PI/2., engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, .5));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertEpsilonEquals(-DEMI_PI/2., engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, .5));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertEpsilonEquals(-DEMI_PI/2., engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, .5));
		}

	}

	@DisplayName("motionNewtonLaw1D5")
	@Nested
	public class MotionNewtonLaw1D5 {

		private InnerComputationVector1D v;
		private InnerComputationVector1D a;
		private InnerComputationVector1D r;

		@BeforeEach
		public void setUp() {
			this.v = new InnerComputationVector1D(null);
			this.a = new InnerComputationVector1D(null);
			this.r = new InnerComputationVector1D(null);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			v.set(0.,0.);
			a.set(0.,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(0., 0.), r);
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			v.set(0.,0.);
			a.set(PI,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(QUARTER_PI,0.), r);
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			v.set(0.,0.);
			a.set(-PI,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(-QUARTER_PI,0.), r);
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			v.set(0.,0.);
			a.set(0.,PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(0., QUARTER_PI), r);
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			v.set(0.,0.);
			a.set(0.,-PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(0., -QUARTER_PI), r);
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			v.set(0.,0.);
			a.set(-PI,PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1,1,QUARTER_PI), r);
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			v.set(0.,0.);
			a.set(-PI,-PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1,-1,QUARTER_PI), r);
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			v.set(DEMI_PI,0.);
			a.set(0.,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(DEMI_PI, 0.), r);
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			v.set(DEMI_PI,0.);
			a.set(PI,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(DEMI_PI,0.), r);
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			v.set(DEMI_PI,0.);
			a.set(-PI,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(DEMI_PI-QUARTER_PI/2.,0.), r);
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			v.set(DEMI_PI,0.);
			a.set(0.,PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1., .5, DEMI_PI), r);
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			v.set(DEMI_PI,0.);
			a.set(0.,-PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1., -.5, DEMI_PI), r);
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			v.set(1.,0.);
			a.set(-1.,1.);
			engine.motionNewtonLaw1D5(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
			assertEpsilonEquals(makeVect(1,1,Math.sqrt(.5)), r);
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			v.set(1.,0.);
			a.set(-1.,-1.);
			engine.motionNewtonLaw1D5(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
			assertEpsilonEquals(makeVect(1,-1,Math.sqrt(.5)), r);
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			v.set(0.,0.);
			a.set(0.,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(0., 0.), r);
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			v.set(0.,0.);
			a.set(PI,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(PI/16.,0.), r);
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			v.set(0.,0.);
			a.set(-PI,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(-PI/16.,0.), r);
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			v.set(0.,0.);
			a.set(0.,PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(0., PI/16.), r);
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			v.set(0.,0.);
			a.set(0.,-PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(0., -PI/16.), r);
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			v.set(0.,0.);
			a.set(-PI,PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1,1,PI/16.), r);
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			v.set(0.,0.);
			a.set(-PI,-PI);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1,-1,PI/16.), r);
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			v.set(DEMI_PI,0.);
			a.set(0.,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(DEMI_PI/2., 0.), r);
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			v.set(DEMI_PI,0.);
			a.set(PI,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(DEMI_PI/2.,0.), r);
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			v.set(DEMI_PI,0.);
			a.set(-PI,0.);
			engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d((DEMI_PI-PI/16.)/2.,0.), r);
		}

	}

	@DisplayName("motionNewtonLaw2D")
	@Nested
	public class MotionNewtonLaw2D {

		private Vector2d v;
		private Vector2d a;
		private Vector2d r;

		@BeforeEach
		public void setUp() {
			this.v = new Vector2d();
			this.a = new Vector2d();
			this.r = new Vector2d();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			v.set(0.,0.);
			a.set(0.,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(0., 0.), r);
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			v.set(0.,0.);
			a.set(PI,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(QUARTER_PI,0.), r);
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			v.set(0.,0.);
			a.set(-PI,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(-QUARTER_PI,0.), r);
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			v.set(0.,0.);
			a.set(0.,PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(0., QUARTER_PI), r);
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			v.set(0.,0.);
			a.set(0.,-PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(0., -QUARTER_PI), r);
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			v.set(0.,0.);
			a.set(-PI,PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1,1,QUARTER_PI), r);
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			v.set(0.,0.);
			a.set(-PI,-PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1,-1,QUARTER_PI), r);
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			v.set(DEMI_PI,0.);
			a.set(0.,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(DEMI_PI, 0.), r);
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			v.set(DEMI_PI,0.);
			a.set(PI,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(DEMI_PI,0.), r);
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			v.set(DEMI_PI,0.);
			a.set(-PI,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new Vector2d(DEMI_PI-QUARTER_PI/2.,0.), r);
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			v.set(DEMI_PI,0.);
			a.set(0.,PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1., .5, DEMI_PI), r);
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			v.set(DEMI_PI,0.);
			a.set(0.,-PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1., -.5, DEMI_PI), r);
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			v.set(1.,0.);
			a.set(-1.,1.);
			engine.motionNewtonLaw2D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
			assertEpsilonEquals(makeVect(1,1,Math.sqrt(.5)), r);
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			v.set(1.,0.);
			a.set(-1.,-1.);
			engine.motionNewtonLaw2D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
			assertEpsilonEquals(makeVect(1,-1,Math.sqrt(.5)), r);
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			v.set(0.,0.);
			a.set(0.,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(0., 0.), r);
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			v.set(0.,0.);
			a.set(PI,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(PI/16.,0.), r);
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			v.set(0.,0.);
			a.set(-PI,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(-PI/16.,0.), r);
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			v.set(0.,0.);
			a.set(0.,PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(0., PI/16.), r);
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			v.set(0.,0.);
			a.set(0.,-PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(0., -PI/16.), r);
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			v.set(0.,0.);
			a.set(-PI,PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1,1,PI/16.), r);
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			v.set(0.,0.);
			a.set(-PI,-PI);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1,-1,PI/16.), r);
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			v.set(DEMI_PI,0.);
			a.set(0.,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(DEMI_PI/2., 0.), r);
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			v.set(DEMI_PI,0.);
			a.set(PI,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d(DEMI_PI/2.,0.), r);
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			v.set(DEMI_PI,0.);
			a.set(-PI,0.);
			engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new Vector2d((DEMI_PI-PI/16.)/2.,0.), r);
		}

	}

	@DisplayName("motionNewtonLaw3D")
	@Nested
	public class MotionNewtonLaw3D {

		private InnerComputationVector3D v;
		private InnerComputationVector3D a;
		private InnerComputationVector3D r;

		@BeforeEach
		public void setUp() {
			this.v = new InnerComputationVector3D();
			this.a = new InnerComputationVector3D();
			this.r = new InnerComputationVector3D();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			v.set(0.,0.,0.);
			a.set(0.,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new InnerComputationVector3D(0., 0.,0.), r);
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			v.set(0.,0.,0.);
			a.set(PI,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new InnerComputationVector3D(QUARTER_PI,0.,0.), r);
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			v.set(0.,0.,0.);
			a.set(-PI,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new InnerComputationVector3D(-QUARTER_PI,0.,0.), r);
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			v.set(0.,0.,0.);
			a.set(0.,PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new InnerComputationVector3D(0., QUARTER_PI,0.), r);
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			v.set(0.,0.,0.);
			a.set(0.,-PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new InnerComputationVector3D(0., -QUARTER_PI,0.), r);
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			v.set(0.,0.,0.);
			a.set(-PI,PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1,1,0.,QUARTER_PI), r);
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			v.set(0.,0.,0.);
			a.set(-PI,-PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1,-1,0.,QUARTER_PI), r);
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			v.set(DEMI_PI,0.,0.);
			a.set(0.,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new InnerComputationVector3D(DEMI_PI, 0.,0.), r);
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			v.set(DEMI_PI,0.,0.);
			a.set(PI,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new InnerComputationVector3D(DEMI_PI,0.,0.), r);
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			v.set(DEMI_PI,0.,0.);
			a.set(-PI,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(new InnerComputationVector3D(DEMI_PI-QUARTER_PI/2.,0.,0.), r);
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			v.set(DEMI_PI,0.,0.);
			a.set(0.,PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1., .5,0., DEMI_PI), r);
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			v.set(DEMI_PI,0.,0.);
			a.set(0.,-PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1., -.5,0, DEMI_PI), r);
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			v.set(1.,0.,0.);
			a.set(-1.,1.,0.);
			engine.motionNewtonLaw3D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
			assertEpsilonEquals(makeVect(1,1,0.,Math.sqrt(.5)), r);
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			v.set(1.,0.,0.);
			a.set(-1.,-1.,0.);
			engine.motionNewtonLaw3D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
			assertEpsilonEquals(makeVect(1,-1,0.,Math.sqrt(.5)), r);
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			v.set(0.,0.,0.);
			a.set(0.,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new InnerComputationVector3D(0., 0.,0.), r);
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			v.set(0.,0.,0.);
			a.set(PI,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new InnerComputationVector3D(PI/16.,0.,0.), r);
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			v.set(0.,0.,0.);
			a.set(-PI,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new InnerComputationVector3D(-PI/16.,0.,0.), r);
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			v.set(0.,0.,0.);
			a.set(0.,PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new InnerComputationVector3D(0., PI/16.,0.), r);
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			v.set(0.,0.,0.);
			a.set(0.,-PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new InnerComputationVector3D(0., -PI/16.,0.), r);
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			v.set(0.,0.,0.);
			a.set(-PI,PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1,1,0.,PI/16.), r);
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			v.set(0.,0.,0.);
			a.set(-PI,-PI,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1,-1,0.,PI/16.), r);
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			v.set(DEMI_PI,0.,0.);
			a.set(0.,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new InnerComputationVector3D(DEMI_PI/2., 0.,0.), r);
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			v.set(DEMI_PI,0.,0.);
			a.set(PI,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new InnerComputationVector3D(DEMI_PI/2.,0.,0.), r);
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			v.set(DEMI_PI,0.,0.);
			a.set(-PI,0.,0.);
			engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
			assertEpsilonEquals(new InnerComputationVector3D((DEMI_PI-PI/16.)/2.,0.,0.), r);
		}

	}

	@DisplayName("motionNewtonEuler1Law")
	@Nested
	public class MotionNewtonEuler1Law {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(0., engine.motionNewtonEuler1Law(0., 1.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEpsilonEquals(PI, engine.motionNewtonEuler1Law(PI, 1.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(-PI, engine.motionNewtonEuler1Law(-PI, 1.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEpsilonEquals(0., engine.motionNewtonEuler1Law(0., .5));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEpsilonEquals(DEMI_PI, engine.motionNewtonEuler1Law(PI, .5));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEpsilonEquals(-DEMI_PI, engine.motionNewtonEuler1Law(-PI, .5));
		}

	}

	@DisplayName("motionNewtonEuler1Law1D")
	@Nested
	public class MotionNewtonEuler1Law1D {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEpsilonEquals(0., engine.motionNewtonEuler1Law1D(0., 0, DEMI_PI, 1.));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEpsilonEquals(DEMI_PI, engine.motionNewtonEuler1Law1D(PI, 0, DEMI_PI, 1.));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEpsilonEquals(-DEMI_PI, engine.motionNewtonEuler1Law1D(-PI, 0, DEMI_PI, 1.));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEpsilonEquals(0., engine.motionNewtonEuler1Law1D(0., 0, DEMI_PI, .5));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertEpsilonEquals(QUARTER_PI, engine.motionNewtonEuler1Law1D(PI, 0, DEMI_PI, .5));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertEpsilonEquals(-QUARTER_PI, engine.motionNewtonEuler1Law1D(-PI, 0, DEMI_PI, .5));
		}

	}

	@DisplayName("motionNewtonEuler1Law1D5")
	@Nested
	public class MotionNewtonEuler1Law1D5 {

		private InnerComputationVector1D v;
		private InnerComputationVector1D r;

		@BeforeEach
		public void setUp() {
			this.v = new InnerComputationVector1D(null);
			this.r = new InnerComputationVector1D(null);
		}
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			v.set(0.,0.);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,0.,0.), r);
		}
		
		@DisplayName("#2")
		@Test
		public void test_2() {
			v.set(0.,PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(0.,1.,DEMI_PI), r);
		}
		
		@DisplayName("#3")
		@Test
		public void test_3() {
			v.set(0.,-PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(0.,-1.,DEMI_PI), r);
		}
		
		@DisplayName("#4")
		@Test
		public void test_4() {
			v.set(PI,0.);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,0.,DEMI_PI), r);
		}
		
		@DisplayName("#5")
		@Test
		public void test_5() {
			v.set(PI,PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,1.,DEMI_PI), r);
		}
		
		@DisplayName("#6")
		@Test
		public void test_6() {
			v.set(PI,-PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,-1.,DEMI_PI), r);
		}
		
		@DisplayName("#7")
		@Test
		public void test_7() {
			v.set(-PI,0.);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1.,0.,DEMI_PI), r);
		}
		
		@DisplayName("#8")
		@Test
		public void test_8() {
			v.set(-PI,PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1.,1.,DEMI_PI), r);
		}
		
		@DisplayName("#9")
		@Test
		public void test_9() {
			v.set(-PI,-PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1.,-1.,DEMI_PI), r);
		}
		
		@DisplayName("#10")
		@Test
		public void test_10() {
			v.set(0.,0.);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,0.,0.), r);
		}
		
		@DisplayName("#11")
		@Test
		public void test_11() {
			v.set(0.,PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(0.,1.,QUARTER_PI), r);
		}
		
		@DisplayName("#12")
		@Test
		public void test_12() {
			v.set(0.,-PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(0.,-1.,QUARTER_PI), r);
		}
		
		@DisplayName("#13")
		@Test
		public void test_13() {
			v.set(PI,0.);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,0.,QUARTER_PI), r);
		}
		
		@DisplayName("#14")
		@Test
		public void test_14() {
			v.set(PI,PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,1.,QUARTER_PI), r);
		}
		
		@DisplayName("#15")
		@Test
		public void test_15() {
			v.set(PI,-PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,-1.,QUARTER_PI), r);
		}
		
		@DisplayName("#16")
		@Test
		public void test_16() {
			v.set(-PI,0.);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1.,0.,QUARTER_PI), r);
		}
		
		@DisplayName("#17")
		@Test
		public void test_17() {
			v.set(-PI,PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1.,1.,QUARTER_PI), r);
		}
		
		@DisplayName("#18")
		@Test
		public void test_18() {
			v.set(-PI,-PI);
			engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1.,-1.,QUARTER_PI), r);
		}

	}

	@DisplayName("motionNewtonEuler1Law2D")
	@Nested
	public class MotionNewtonEuler1Law2D {

		private Vector2d v;
		private Vector2d r;

		@BeforeEach
		public void setUp() {
			this.v = new Vector2d();
			this.r = new Vector2d();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			v.set(0.,0.);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,0.,0.), r);
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			v.set(0.,PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(0.,1.,DEMI_PI), r);
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			v.set(0.,-PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(0.,-1.,DEMI_PI), r);
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			v.set(PI,0.);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,0.,DEMI_PI), r);
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			v.set(PI,PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,1.,DEMI_PI), r);
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			v.set(PI,-PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,-1.,DEMI_PI), r);
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			v.set(-PI,0.);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1.,0.,DEMI_PI), r);
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			v.set(-PI,PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1.,1.,DEMI_PI), r);
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			v.set(-PI,-PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1.,-1.,DEMI_PI), r);
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			v.set(0.,0.);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,0.,0.), r);
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			v.set(0.,PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(0.,1.,QUARTER_PI), r);
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			v.set(0.,-PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(0.,-1.,QUARTER_PI), r);
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			v.set(PI,0.);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,0.,QUARTER_PI), r);
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			v.set(PI,PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,1.,QUARTER_PI), r);
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			v.set(PI,-PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,-1.,QUARTER_PI), r);
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			v.set(-PI,0.);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1.,0.,DEMI_PI/2.), r);
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			v.set(-PI,PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1.,1.,DEMI_PI/2.), r);
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			v.set(-PI,-PI);
			engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1.,-1.,DEMI_PI/2.), r);
		}

	}

	@DisplayName("motionNewtonEuler1Law3D")
	@Nested
	public class MotionNewtonEuler1Law3D {

		private InnerComputationVector3D v;
		private InnerComputationVector3D r;

		@BeforeEach
		public void setUp() {
			this.v = new InnerComputationVector3D();
			this.r = new InnerComputationVector3D();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			v.set(0.,0.,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,0.,0.,0.), r);
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			v.set(0.,PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(0.,1.,0.,DEMI_PI), r);
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			v.set(0.,-PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(0.,-1.,0.,DEMI_PI), r);
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			v.set(PI,0.,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,0.,0.,DEMI_PI), r);
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			v.set(PI,PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,1.,0.,DEMI_PI), r);
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			v.set(PI,-PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(1.,-1.,0.,DEMI_PI), r);
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			v.set(-PI,0.,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1.,0.,0.,DEMI_PI), r);
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			v.set(-PI,PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1.,1.,0.,DEMI_PI), r);
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			v.set(-PI,-PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
			assertEpsilonEquals(makeVect(-1.,-1.,0.,DEMI_PI), r);
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			v.set(0.,0.,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,0.,0.,0.), r);
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			v.set(0.,PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(0.,1.,0.,QUARTER_PI), r);
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			v.set(0.,-PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(0.,-1.,0.,QUARTER_PI), r);
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			v.set(PI,0.,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,0.,0.,QUARTER_PI), r);
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			v.set(PI,PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,1.,0.,QUARTER_PI), r);
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			v.set(PI,-PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(1.,-1.,0.,QUARTER_PI), r);
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			v.set(-PI,0.,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1.,0.,0.,DEMI_PI/2.), r);
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			v.set(-PI,PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1.,1.,0.,DEMI_PI/2.), r);
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			v.set(-PI,-PI,0.);
			engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
			assertEpsilonEquals(makeVect(-1.,-1.,0.,DEMI_PI/2.), r);
		}

	}

}