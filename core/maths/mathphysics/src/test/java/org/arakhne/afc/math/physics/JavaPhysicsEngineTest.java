/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import org.arakhne.afc.math.geometry.d1.d.Vector1d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.test.AbstractMathTestCase;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link JavaPhysicsEngine}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class JavaPhysicsEngineTest extends AbstractMathTestCase {

	private JavaPhysicsEngine engine;
	
	/**
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.engine = new JavaPhysicsEngine();
	}
	
	/**
	 * @throws Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.engine = null;
	}
	
	private Vector2d makeVect(double x, double y, double n) {
		Vector2d v = new Vector2d(x,y);
		if (v.getLengthSquared()!=0.) v.normalize();
		v.scale(n);
		return v;
	}

	private Vector3d makeVect(double x, double y, double z, double n) {
		final Vector3d v = new Vector3d(x,y,z);
		if (v.getLengthSquared()!=0.) v.normalize();
		v.scale((double) n);
		return v;
	}

	/**
	 */
	@Test
	public void speed() {
		assertEpsilonEquals(0., this.engine.speed(0., 1.));
		assertEpsilonEquals(PI, this.engine.speed(PI, 1.));
		assertEpsilonEquals(-PI, this.engine.speed(-PI, 1.));
		assertEpsilonEquals(0., this.engine.speed(0., .5));
		assertEpsilonEquals(TWO_PI, this.engine.speed(PI, .5));
		assertEpsilonEquals(-TWO_PI, this.engine.speed(-PI, .5));
	}

	/** 
	 */
	@Test
	public void acceleration() {
		assertEpsilonEquals(0., this.engine.acceleration(0., 0., 1.));
		assertEpsilonEquals(PI, this.engine.acceleration(0., PI, 1.));
		assertEpsilonEquals(-PI, this.engine.acceleration(0., -PI, 1.));
		assertEpsilonEquals(-PI, this.engine.acceleration(PI, 0., 1.));
		assertEpsilonEquals(0., this.engine.acceleration(PI, PI, 1.));
		assertEpsilonEquals(-TWO_PI, this.engine.acceleration(PI, -PI, 1.));
		assertEpsilonEquals(PI, this.engine.acceleration(-PI, 0., 1.));
		assertEpsilonEquals(TWO_PI, this.engine.acceleration(-PI, PI, 1.));
		assertEpsilonEquals(0., this.engine.acceleration(-PI, -PI, 1.));

		assertEpsilonEquals(0., this.engine.acceleration(0., 0., .5));
		assertEpsilonEquals(TWO_PI, this.engine.acceleration(0., PI, .5));
		assertEpsilonEquals(-TWO_PI, this.engine.acceleration(0., -PI, .5));
		assertEpsilonEquals(-TWO_PI, this.engine.acceleration(PI, 0., .5));
		assertEpsilonEquals(0., this.engine.acceleration(PI, PI, .5));
		assertEpsilonEquals(-2.*TWO_PI, this.engine.acceleration(PI, -PI, .5));
		assertEpsilonEquals(TWO_PI, this.engine.acceleration(-PI, 0., .5));
		assertEpsilonEquals(2*TWO_PI, this.engine.acceleration(-PI, PI, .5));
		assertEpsilonEquals(0., this.engine.acceleration(-PI, -PI, .5));
	}

	/**
	 */
	@Test
	public void motionNewtonLaw() {
		assertEpsilonEquals(0., this.engine.motionNewtonLaw(0., 0., 1.));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw(0., PI, 1.));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw(0., -PI, 1.));
		assertEpsilonEquals(PI, this.engine.motionNewtonLaw(PI, 0., 1.));
		assertEpsilonEquals(ONE_HALF_PI, this.engine.motionNewtonLaw(PI, PI, 1.));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw(PI, -PI, 1.));
		assertEpsilonEquals(-PI, this.engine.motionNewtonLaw(-PI, 0., 1.));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw(-PI, PI, 1.));
		assertEpsilonEquals(-ONE_HALF_PI, this.engine.motionNewtonLaw(-PI, -PI, 1.));

		assertEpsilonEquals(0., this.engine.motionNewtonLaw(0., 0., .5));
		assertEpsilonEquals(QUARTER_PI/2., this.engine.motionNewtonLaw(0., PI, .5));
		assertEpsilonEquals(-QUARTER_PI/2., this.engine.motionNewtonLaw(0., -PI, .5));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw(PI, 0., .5));
		assertEpsilonEquals(DEMI_PI+QUARTER_PI/2., this.engine.motionNewtonLaw(PI, PI, .5));
		assertEpsilonEquals(THREE_QUARTER_PI/2., this.engine.motionNewtonLaw(PI, -PI, .5));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw(-PI, 0., .5));
		assertEpsilonEquals(-THREE_QUARTER_PI/2., this.engine.motionNewtonLaw(-PI, PI, .5));
		assertEpsilonEquals((-PI-QUARTER_PI)/2., this.engine.motionNewtonLaw(-PI, -PI, .5));
	}

	/**
	 */
	@Test
	public void motionNewtonLaw1D() {
		assertEpsilonEquals(0., this.engine.motionNewtonLaw1D(0., 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, 1.));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonLaw1D(0., 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, 1.));
		assertEpsilonEquals(-QUARTER_PI/2., this.engine.motionNewtonLaw1D(0., 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, 1.));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, 1.));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, 1.));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, 1.));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, 1.));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, 1.));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, 1.));

		assertEpsilonEquals(0., this.engine.motionNewtonLaw1D(0.,0, DEMI_PI,  0., -QUARTER_PI, DEMI_PI, .5));
		assertEpsilonEquals(QUARTER_PI/4., this.engine.motionNewtonLaw1D(0., 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, .5));
		assertEpsilonEquals(-QUARTER_PI/8., this.engine.motionNewtonLaw1D(0., 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, .5));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, .5));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, .5));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, .5));
		assertEpsilonEquals(-DEMI_PI/2., this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, 0., -QUARTER_PI, DEMI_PI, .5));
		assertEpsilonEquals(-DEMI_PI/2., this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, .5));
		assertEpsilonEquals(-DEMI_PI/2., this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, .5));
	}

	/**
	 */
	@Test
	public void motionNewtonLaw1D5() {
		Vector1d v = new Vector1d();
		Vector1d a = new Vector1d();
		Vector1d r = new Vector1d();

		//--- DT: 1

		// velocity = (0,0)
		v.set(0.,0.);
		a.set(0.,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(0., 0.), r);
		a.set(PI,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(QUARTER_PI,0.), r);
		a.set(-PI,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(-QUARTER_PI,0.), r);
		a.set(0.,PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(0., QUARTER_PI), r);
		a.set(0.,-PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(0., -QUARTER_PI), r);
		a.set(-PI,PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1,1,QUARTER_PI), r);
		a.set(-PI,-PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1,-1,QUARTER_PI), r);

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.);
		a.set(0.,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(DEMI_PI, 0.), r);
		a.set(PI,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(DEMI_PI,0.), r);
		a.set(-PI,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(DEMI_PI-QUARTER_PI/2.,0.), r);
		a.set(0.,PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1., .5, DEMI_PI), r);
		a.set(0.,-PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1., -.5, DEMI_PI), r);

		// velocity = (1,0)
		v.set(1.,0.);
		a.set(-1.,1.);
		this.engine.motionNewtonLaw1D5(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
		assertEpsilonEquals(makeVect(1,1,Math.sqrt(.5)), r);
		a.set(-1.,-1.);
		this.engine.motionNewtonLaw1D5(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
		assertEpsilonEquals(makeVect(1,-1,Math.sqrt(.5)), r);
				
		//--- DT: 1/2

		// velocity = (0,0)
		v.set(0.,0.);
		a.set(0.,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(0., 0.), r);
		a.set(PI,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(PI/16.,0.), r);
		a.set(-PI,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(-PI/16.,0.), r);
		a.set(0.,PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(0., PI/16.), r);
		a.set(0.,-PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(0., -PI/16.), r);
		a.set(-PI,PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1,1,PI/16.), r);
		a.set(-PI,-PI);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1,-1,PI/16.), r);

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.);
		a.set(0.,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(DEMI_PI/2., 0.), r);
		a.set(PI,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(DEMI_PI/2.,0.), r);
		a.set(-PI,0.);
		this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d((DEMI_PI-PI/16.)/2.,0.), r);
	}

	/**
	 */
	@Test
	public void motionNewtonLaw2D() {
		Vector2d v = new Vector2d();
		Vector2d a = new Vector2d();
		Vector2d r = new Vector2d();

		//--- DT: 1

		// velocity = (0,0)
		v.set(0.,0.);
		a.set(0.,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(0., 0.), r);
		a.set(PI,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(QUARTER_PI,0.), r);
		a.set(-PI,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(-QUARTER_PI,0.), r);
		a.set(0.,PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(0., QUARTER_PI), r);
		a.set(0.,-PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(0., -QUARTER_PI), r);
		a.set(-PI,PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1,1,QUARTER_PI), r);
		a.set(-PI,-PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1,-1,QUARTER_PI), r);

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.);
		a.set(0.,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(DEMI_PI, 0.), r);
		a.set(PI,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(DEMI_PI,0.), r);
		a.set(-PI,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector2d(DEMI_PI-QUARTER_PI/2.,0.), r);
		a.set(0.,PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1., .5, DEMI_PI), r);
		a.set(0.,-PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1., -.5, DEMI_PI), r);

		// velocity = (1,0)
		v.set(1.,0.);
		a.set(-1.,1.);
		this.engine.motionNewtonLaw2D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
		assertEpsilonEquals(makeVect(1,1,Math.sqrt(.5)), r);
		a.set(-1.,-1.);
		this.engine.motionNewtonLaw2D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
		assertEpsilonEquals(makeVect(1,-1,Math.sqrt(.5)), r);
				
		//--- DT: 1/2

		// velocity = (0,0)
		v.set(0.,0.);
		a.set(0.,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(0., 0.), r);
		a.set(PI,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(PI/16.,0.), r);
		a.set(-PI,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(-PI/16.,0.), r);
		a.set(0.,PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(0., PI/16.), r);
		a.set(0.,-PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(0., -PI/16.), r);
		a.set(-PI,PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1,1,PI/16.), r);
		a.set(-PI,-PI);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1,-1,PI/16.), r);

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.);
		a.set(0.,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(DEMI_PI/2., 0.), r);
		a.set(PI,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d(DEMI_PI/2.,0.), r);
		a.set(-PI,0.);
		this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector2d((DEMI_PI-PI/16.)/2.,0.), r);
	}

	/**
	 */
	@Test
	public void motionNewtonLaw3D() {
		Vector3d v = new Vector3d();
		Vector3d a = new Vector3d();
		Vector3d r = new Vector3d();

		//--- DT: 1

		// velocity = (0,0)
		v.set(0.,0.,0.);
		a.set(0.,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector3d(0., 0.,0.), r);
		a.set(PI,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector3d(QUARTER_PI,0.,0.), r);
		a.set(-PI,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector3d(-QUARTER_PI,0.,0.), r);
		a.set(0.,PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector3d(0., QUARTER_PI,0.), r);
		a.set(0.,-PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector3d(0., -QUARTER_PI,0.), r);
		a.set(-PI,PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1,1,0.,QUARTER_PI), r);
		a.set(-PI,-PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1,-1,0.,QUARTER_PI), r);

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.,0.);
		a.set(0.,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector3d(DEMI_PI, 0.,0.), r);
		a.set(PI,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector3d(DEMI_PI,0.,0.), r);
		a.set(-PI,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(new Vector3d(DEMI_PI-QUARTER_PI/2.,0.,0.), r);
		a.set(0.,PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1., .5,0., DEMI_PI), r);
		a.set(0.,-PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1., -.5,0, DEMI_PI), r);

		// velocity = (1,0)
		v.set(1.,0.,0.);
		a.set(-1.,1.,0.);
		this.engine.motionNewtonLaw3D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
		assertEpsilonEquals(makeVect(1,1,0.,Math.sqrt(.5)), r);
		a.set(-1.,-1.,0.);
		this.engine.motionNewtonLaw3D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1., r);
		assertEpsilonEquals(makeVect(1,-1,0.,Math.sqrt(.5)), r);
				
		//--- DT: 1/2

		// velocity = (0,0)
		v.set(0.,0.,0.);
		a.set(0.,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector3d(0., 0.,0.), r);
		a.set(PI,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector3d(PI/16.,0.,0.), r);
		a.set(-PI,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector3d(-PI/16.,0.,0.), r);
		a.set(0.,PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector3d(0., PI/16.,0.), r);
		a.set(0.,-PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector3d(0., -PI/16.,0.), r);
		a.set(-PI,PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1,1,0.,PI/16.), r);
		a.set(-PI,-PI,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1,-1,0.,PI/16.), r);

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.,0.);
		a.set(0.,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector3d(DEMI_PI/2., 0.,0.), r);
		a.set(PI,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector3d(DEMI_PI/2.,0.,0.), r);
		a.set(-PI,0.,0.);
		this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5, r);
		assertEpsilonEquals(new Vector3d((DEMI_PI-PI/16.)/2.,0.,0.), r);
	}

	/**
	 */
	@Test
	public void motionNewtonEuler1Law() {
		assertEpsilonEquals(0., this.engine.motionNewtonEuler1Law(0., 1.));
		assertEpsilonEquals(PI, this.engine.motionNewtonEuler1Law(PI, 1.));
		assertEpsilonEquals(-PI, this.engine.motionNewtonEuler1Law(-PI, 1.));

		assertEpsilonEquals(0., this.engine.motionNewtonEuler1Law(0., .5));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonEuler1Law(PI, .5));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonEuler1Law(-PI, .5));
	}

	/**
	 */
	@Test
	public void motionNewtonEuler1Law1D() {
		assertEpsilonEquals(0., this.engine.motionNewtonEuler1Law1D(0., 0, DEMI_PI, 1.));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonEuler1Law1D(PI, 0, DEMI_PI, 1.));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonEuler1Law1D(-PI, 0, DEMI_PI, 1.));

		assertEpsilonEquals(0., this.engine.motionNewtonEuler1Law1D(0., 0, DEMI_PI, .5));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonEuler1Law1D(PI, 0, DEMI_PI, .5));
		assertEpsilonEquals(-QUARTER_PI, this.engine.motionNewtonEuler1Law1D(-PI, 0, DEMI_PI, .5));
	}

	/**
	 */
	@Test
	public void motionNewtonEuler1Law1D5() {
		Vector1d v = new Vector1d();
		Vector1d r = new Vector1d();
		
		v.set(0.,0.);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,0.,0.), r);
		v.set(0.,PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(0.,1.,DEMI_PI), r);
		v.set(0.,-PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(0.,-1.,DEMI_PI), r);
		v.set(PI,0.);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,0.,DEMI_PI), r);
		v.set(PI,PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,1.,DEMI_PI), r);
		v.set(PI,-PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,-1.,DEMI_PI), r);
		v.set(-PI,0.);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1.,0.,DEMI_PI), r);
		v.set(-PI,PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1.,1.,DEMI_PI), r);
		v.set(-PI,-PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1.,-1.,DEMI_PI), r);

		v.set(0.,0.);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,0.,0.), r);
		v.set(0.,PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(0.,1.,QUARTER_PI), r);
		v.set(0.,-PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(0.,-1.,QUARTER_PI), r);
		v.set(PI,0.);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,0.,QUARTER_PI), r);
		v.set(PI,PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,1.,QUARTER_PI), r);
		v.set(PI,-PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,-1.,QUARTER_PI), r);
		v.set(-PI,0.);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1.,0.,QUARTER_PI), r);
		v.set(-PI,PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1.,1.,QUARTER_PI), r);
		v.set(-PI,-PI);
		this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1.,-1.,QUARTER_PI), r);
	}

	/**
	 */
	@Test
	public void motionNewtonEuler1Law2D() {
		Vector2d v = new Vector2d();
		Vector2d r = new Vector2d();
		
		v.set(0.,0.);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,0.,0.), r);
		v.set(0.,PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(0.,1.,DEMI_PI), r);
		v.set(0.,-PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(0.,-1.,DEMI_PI), r);
		v.set(PI,0.);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,0.,DEMI_PI), r);
		v.set(PI,PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,1.,DEMI_PI), r);
		v.set(PI,-PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,-1.,DEMI_PI), r);
		v.set(-PI,0.);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1.,0.,DEMI_PI), r);
		v.set(-PI,PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1.,1.,DEMI_PI), r);
		v.set(-PI,-PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1.,-1.,DEMI_PI), r);

		v.set(0.,0.);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,0.,0.), r);
		v.set(0.,PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(0.,1.,QUARTER_PI), r);
		v.set(0.,-PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(0.,-1.,QUARTER_PI), r);
		v.set(PI,0.);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,0.,QUARTER_PI), r);
		v.set(PI,PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,1.,QUARTER_PI), r);
		v.set(PI,-PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,-1.,QUARTER_PI), r);
		v.set(-PI,0.);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1.,0.,DEMI_PI/2.), r);
		v.set(-PI,PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1.,1.,DEMI_PI/2.), r);
		v.set(-PI,-PI);
		this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1.,-1.,DEMI_PI/2.), r);
	}

	/**
	 */
	@Test
	public void motionNewtonEuler1Law3D() {
		Vector3d v = new Vector3d();
		Vector3d r = new Vector3d();
		
		v.set(0.,0.,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,0.,0.,0.), r);
		v.set(0.,PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(0.,1.,0.,DEMI_PI), r);
		v.set(0.,-PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(0.,-1.,0.,DEMI_PI), r);
		v.set(PI,0.,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,0.,0.,DEMI_PI), r);
		v.set(PI,PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,1.,0.,DEMI_PI), r);
		v.set(PI,-PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(1.,-1.,0.,DEMI_PI), r);
		v.set(-PI,0.,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1.,0.,0.,DEMI_PI), r);
		v.set(-PI,PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1.,1.,0.,DEMI_PI), r);
		v.set(-PI,-PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1., r);
		assertEpsilonEquals(makeVect(-1.,-1.,0.,DEMI_PI), r);

		v.set(0.,0.,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,0.,0.,0.), r);
		v.set(0.,PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(0.,1.,0.,QUARTER_PI), r);
		v.set(0.,-PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(0.,-1.,0.,QUARTER_PI), r);
		v.set(PI,0.,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,0.,0.,QUARTER_PI), r);
		v.set(PI,PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,1.,0.,QUARTER_PI), r);
		v.set(PI,-PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(1.,-1.,0.,QUARTER_PI), r);
		v.set(-PI,0.,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1.,0.,0.,DEMI_PI/2.), r);
		v.set(-PI,PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1.,1.,0.,DEMI_PI/2.), r);
		v.set(-PI,-PI,0.);
		this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5, r);
		assertEpsilonEquals(makeVect(-1.,-1.,0.,DEMI_PI/2.), r);
	}

}