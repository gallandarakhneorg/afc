/* 
 * $Id$
 * 
 * Copyright (c) 2009-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.physics;

import static org.arakhne.afc.math.MathConstants.DEMI_PI;
import static org.arakhne.afc.math.MathConstants.ONE_HALF_PI;
import static org.arakhne.afc.math.MathConstants.PI;
import static org.arakhne.afc.math.MathConstants.QUARTER_PI;
import static org.arakhne.afc.math.MathConstants.SQRT_2;
import static org.arakhne.afc.math.MathConstants.THREE_QUARTER_PI;
import static org.arakhne.afc.math.MathConstants.TWO_PI;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link JavaPhysicsEngine}
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
	@Before
	public void setUp() throws Exception {
		this.engine = new JavaPhysicsEngine();
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.engine = null;
	}
	
	private Vector2fx makeVect(double x, double y, double n) {
		Vector2fx v = new Vector2fx(x,y);
		if (v.getLengthSquared()!=0.) v.normalize();
		v.scale(n);
		return v;
	}

	private Vector3f makeVect(double x, double y, double z, double n) {
		Vector3f v = new Vector3f(x,y,z);
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
	public void motionNewtonLaw1D5() {
		Vector2fx v = new Vector2fx();
		Vector2fx a = new Vector2fx();

		//--- DT: 1

		// velocity = (0,0)
		v.set(0.,0.);
		a.set(0.,0.);
		assertEpsilonEquals(new Vector2fx(0., 0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(PI,0.);
		assertEpsilonEquals(new Vector2fx(QUARTER_PI,0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,0.);
		assertEpsilonEquals(new Vector2fx(-QUARTER_PI,0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,PI);
		assertEpsilonEquals(new Vector2fx(0., QUARTER_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,-PI);
		assertEpsilonEquals(new Vector2fx(0., -QUARTER_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1,1,QUARTER_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1,-1,QUARTER_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.);
		a.set(0.,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI, 0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(PI,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI,0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI-QUARTER_PI/2.,0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,PI);
		assertEpsilonEquals(makeVect(1., .5, DEMI_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,-PI);
		assertEpsilonEquals(makeVect(1., -.5, DEMI_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));

		// velocity = (1,0)
		v.set(1.,0.);
		a.set(-1.,1.);
		assertEpsilonEquals(makeVect(1,1,Math.sqrt(.5)), 
				this.engine.motionNewtonLaw1D5(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.));
		a.set(-1.,-1.);
		assertEpsilonEquals(makeVect(1,-1,Math.sqrt(.5)), 
				this.engine.motionNewtonLaw1D5(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.));
				
		//--- DT: 1/2

		// velocity = (0,0)
		v.set(0.,0.);
		a.set(0.,0.);
		assertEpsilonEquals(new Vector2fx(0., 0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(PI,0.);
		assertEpsilonEquals(new Vector2fx(PI/16.,0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,0.);
		assertEpsilonEquals(new Vector2fx(-PI/16.,0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(0.,PI);
		assertEpsilonEquals(new Vector2fx(0., PI/16.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(0.,-PI);
		assertEpsilonEquals(new Vector2fx(0., -PI/16.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1,1,PI/16.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1,-1,PI/16.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.);
		a.set(0.,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI/2., 0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(PI,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI/2.,0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,0.);
		assertEpsilonEquals(new Vector2fx((DEMI_PI-PI/16.)/2.,0.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
	}

	/**
	 */
	public void motionNewtonLaw2D() {
		Vector2fx v = new Vector2fx();
		Vector2fx a = new Vector2fx();

		//--- DT: 1

		// velocity = (0,0)
		v.set(0.,0.);
		a.set(0.,0.);
		assertEpsilonEquals(new Vector2fx(0., 0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(PI,0.);
		assertEpsilonEquals(new Vector2fx(QUARTER_PI,0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,0.);
		assertEpsilonEquals(new Vector2fx(-QUARTER_PI,0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,PI);
		assertEpsilonEquals(new Vector2fx(0., QUARTER_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,-PI);
		assertEpsilonEquals(new Vector2fx(0., -QUARTER_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1,1,QUARTER_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1,-1,QUARTER_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.);
		a.set(0.,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI, 0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(PI,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI,0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI-QUARTER_PI/2.,0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,PI);
		assertEpsilonEquals(makeVect(1., .5, DEMI_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,-PI);
		assertEpsilonEquals(makeVect(1., -.5, DEMI_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));

		// velocity = (1,0)
		v.set(1.,0.);
		a.set(-1.,1.);
		assertEpsilonEquals(makeVect(1,1,Math.sqrt(.5)), 
				this.engine.motionNewtonLaw2D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.));
		a.set(-1.,-1.);
		assertEpsilonEquals(makeVect(1,-1,Math.sqrt(.5)), 
				this.engine.motionNewtonLaw2D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.));
				
		//--- DT: 1/2

		// velocity = (0,0)
		v.set(0.,0.);
		a.set(0.,0.);
		assertEpsilonEquals(new Vector2fx(0., 0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(PI,0.);
		assertEpsilonEquals(new Vector2fx(PI/16.,0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,0.);
		assertEpsilonEquals(new Vector2fx(-PI/16.,0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(0.,PI);
		assertEpsilonEquals(new Vector2fx(0., PI/16.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(0.,-PI);
		assertEpsilonEquals(new Vector2fx(0., -PI/16.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1,1,PI/16.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1,-1,PI/16.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.);
		a.set(0.,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI/2., 0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(PI,0.);
		assertEpsilonEquals(new Vector2fx(DEMI_PI/2.,0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,0.);
		assertEpsilonEquals(new Vector2fx((DEMI_PI-PI/16.)/2.,0.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
	}

	/**
	 */
	public void motionNewtonLaw3D() {
		Vector3f v = new Vector3f();
		Vector3f a = new Vector3f();

		//--- DT: 1

		// velocity = (0,0)
		v.set(0.,0.,0.);
		a.set(0.,0.,0.);
		assertEpsilonEquals(new Vector3f(0., 0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(PI,0.,0.);
		assertEpsilonEquals(new Vector3f(QUARTER_PI,0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,0.,0.);
		assertEpsilonEquals(new Vector3f(-QUARTER_PI,0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,PI,0.);
		assertEpsilonEquals(new Vector3f(0., QUARTER_PI,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,-PI,0.);
		assertEpsilonEquals(new Vector3f(0., -QUARTER_PI,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,PI,0.);
		assertEpsilonEquals(makeVect(-1,1,0.,QUARTER_PI), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,-PI,0.);
		assertEpsilonEquals(makeVect(-1,-1,0.,QUARTER_PI), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.,0.);
		a.set(0.,0.,0.);
		assertEpsilonEquals(new Vector3f(DEMI_PI, 0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(PI,0.,0.);
		assertEpsilonEquals(new Vector3f(DEMI_PI,0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(-PI,0.,0.);
		assertEpsilonEquals(new Vector3f(DEMI_PI-QUARTER_PI/2.,0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,PI,0.);
		assertEpsilonEquals(makeVect(1., .5,0., DEMI_PI), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));
		a.set(0.,-PI,0.);
		assertEpsilonEquals(makeVect(1., -.5,0, DEMI_PI), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.));

		// velocity = (1,0)
		v.set(1.,0.,0.);
		a.set(-1.,1.,0.);
		assertEpsilonEquals(makeVect(1,1,0.,Math.sqrt(.5)), 
				this.engine.motionNewtonLaw3D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.));
		a.set(-1.,-1.,0.);
		assertEpsilonEquals(makeVect(1,-1,0.,Math.sqrt(.5)), 
				this.engine.motionNewtonLaw3D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.));
				
		//--- DT: 1/2

		// velocity = (0,0)
		v.set(0.,0.,0.);
		a.set(0.,0.,0.);
		assertEpsilonEquals(new Vector3f(0., 0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(PI,0.,0.);
		assertEpsilonEquals(new Vector3f(PI/16.,0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,0.,0.);
		assertEpsilonEquals(new Vector3f(-PI/16.,0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(0.,PI,0.);
		assertEpsilonEquals(new Vector3f(0., PI/16.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(0.,-PI,0.);
		assertEpsilonEquals(new Vector3f(0., -PI/16.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,PI,0.);
		assertEpsilonEquals(makeVect(-1,1,0.,PI/16.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,-PI,0.);
		assertEpsilonEquals(makeVect(-1,-1,0.,PI/16.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.,0.);
		a.set(0.,0.,0.);
		assertEpsilonEquals(new Vector3f(DEMI_PI/2., 0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(PI,0.,0.);
		assertEpsilonEquals(new Vector3f(DEMI_PI/2.,0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
		a.set(-PI,0.,0.);
		assertEpsilonEquals(new Vector3f((DEMI_PI-PI/16.)/2.,0.,0.), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5));
	}

	/**
	 */
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
	public void motionNewtonEuler1Law1D5() {
		Vector2fx v = new Vector2fx();
		
		v.set(0.,0.);
		assertEpsilonEquals(makeVect(1.,0.,0.),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.));
		v.set(0.,PI);
		assertEpsilonEquals(makeVect(0.,1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.));
		v.set(0.,-PI);
		assertEpsilonEquals(makeVect(0.,-1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.));
		v.set(PI,0.);
		assertEpsilonEquals(makeVect(1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.));
		v.set(PI,PI);
		assertEpsilonEquals(makeVect(1.,1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.));
		v.set(PI,-PI);
		assertEpsilonEquals(makeVect(1.,-1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.));
		v.set(-PI,0.);
		assertEpsilonEquals(makeVect(-1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.));
		v.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1.,1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.));
		v.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1.,-1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.));

		v.set(0.,0.);
		assertEpsilonEquals(makeVect(1.,0.,0.),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5));
		v.set(0.,PI);
		assertEpsilonEquals(makeVect(0.,1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5));
		v.set(0.,-PI);
		assertEpsilonEquals(makeVect(0.,-1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5));
		v.set(PI,0.);
		assertEpsilonEquals(makeVect(1.,0.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5));
		v.set(PI,PI);
		assertEpsilonEquals(makeVect(1.,1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5));
		v.set(PI,-PI);
		assertEpsilonEquals(makeVect(1.,-1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5));
		v.set(-PI,0.);
		assertEpsilonEquals(makeVect(-1.,0.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5));
		v.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1.,1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5));
		v.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1.,-1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5));
	}

	/**
	 */
	public void motionNewtonEuler1Law2D() {
		Vector2fx v = new Vector2fx();
		
		v.set(0.,0.);
		assertEpsilonEquals(makeVect(1.,0.,0.),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.));
		v.set(0.,PI);
		assertEpsilonEquals(makeVect(0.,1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.));
		v.set(0.,-PI);
		assertEpsilonEquals(makeVect(0.,-1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.));
		v.set(PI,0.);
		assertEpsilonEquals(makeVect(1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.));
		v.set(PI,PI);
		assertEpsilonEquals(makeVect(1.,1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.));
		v.set(PI,-PI);
		assertEpsilonEquals(makeVect(1.,-1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.));
		v.set(-PI,0.);
		assertEpsilonEquals(makeVect(-1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.));
		v.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1.,1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.));
		v.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1.,-1.,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.));

		v.set(0.,0.);
		assertEpsilonEquals(makeVect(1.,0.,0.),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5));
		v.set(0.,PI);
		assertEpsilonEquals(makeVect(0.,1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5));
		v.set(0.,-PI);
		assertEpsilonEquals(makeVect(0.,-1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5));
		v.set(PI,0.);
		assertEpsilonEquals(makeVect(1.,0.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5));
		v.set(PI,PI);
		assertEpsilonEquals(makeVect(1.,1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5));
		v.set(PI,-PI);
		assertEpsilonEquals(makeVect(1.,-1.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5));
		v.set(-PI,0.);
		assertEpsilonEquals(makeVect(-1.,0.,DEMI_PI/2.),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5));
		v.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1.,1.,DEMI_PI/2.),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5));
		v.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1.,-1.,DEMI_PI/2.),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5));
	}

	/**
	 */
	public void motionNewtonEuler1Law3D() {
		Vector3f v = new Vector3f();
		
		v.set(0.,0.,0.);
		assertEpsilonEquals(makeVect(1.,0.,0.,0.),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.));
		v.set(0.,PI,0.);
		assertEpsilonEquals(makeVect(0.,1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.));
		v.set(0.,-PI,0.);
		assertEpsilonEquals(makeVect(0.,-1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.));
		v.set(PI,0.,0.);
		assertEpsilonEquals(makeVect(1.,0.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.));
		v.set(PI,PI,0.);
		assertEpsilonEquals(makeVect(1.,1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.));
		v.set(PI,-PI,0.);
		assertEpsilonEquals(makeVect(1.,-1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.));
		v.set(-PI,0.,0.);
		assertEpsilonEquals(makeVect(-1.,0.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.));
		v.set(-PI,PI,0.);
		assertEpsilonEquals(makeVect(-1.,1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.));
		v.set(-PI,-PI,0.);
		assertEpsilonEquals(makeVect(-1.,-1.,0.,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.));

		v.set(0.,0.,0.);
		assertEpsilonEquals(makeVect(1.,0.,0.,0.),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5));
		v.set(0.,PI,0.);
		assertEpsilonEquals(makeVect(0.,1.,0.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5));
		v.set(0.,-PI,0.);
		assertEpsilonEquals(makeVect(0.,-1.,0.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5));
		v.set(PI,0.,0.);
		assertEpsilonEquals(makeVect(1.,0.,0.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5));
		v.set(PI,PI,0.);
		assertEpsilonEquals(makeVect(1.,1.,0.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5));
		v.set(PI,-PI,0.);
		assertEpsilonEquals(makeVect(1.,-1.,0.,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5));
		v.set(-PI,0.,0.);
		assertEpsilonEquals(makeVect(-1.,0.,0.,DEMI_PI/2.),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5));
		v.set(-PI,PI,0.);
		assertEpsilonEquals(makeVect(-1.,1.,0.,DEMI_PI/2.),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5));
		v.set(-PI,-PI,0.);
		assertEpsilonEquals(makeVect(-1.,-1.,0.,DEMI_PI/2.),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5));
	}

}