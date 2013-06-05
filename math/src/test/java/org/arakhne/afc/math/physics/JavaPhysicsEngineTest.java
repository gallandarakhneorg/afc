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

import org.arakhne.afc.math.AbstractRepeatedTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Vector3f;

/**
 * Test for {@link JavaPhysicsEngine}
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JavaPhysicsEngineTest extends AbstractRepeatedTestCase 
implements MathConstants {

	private JavaPhysicsEngine engine;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.engine = new JavaPhysicsEngine();
	}
	
	@Override
	public void tearDown() throws Exception {
		this.engine = null;
		super.setUp();
	}
	
	private static Vector2f makeVect(float x, float y, float n) {
		Vector2f v = new Vector2f(x,y);
		if (v.lengthSquared()!=0.f) v.normalize();
		v.scale(n);
		return v;
	}

	private static Vector3f makeVect(float x, float y, float z, float n) {
		Vector3f v = new Vector3f(x,y,z);
		if (v.lengthSquared()!=0.f) v.normalize();
		v.scale(n);
		return v;
	}

	/**
	 */
	public void testSpeed() {
		assertEpsilonEquals(0.f, this.engine.speed(0.f, 1.f));
		assertEpsilonEquals(PI, this.engine.speed(PI, 1.f));
		assertEpsilonEquals(-PI, this.engine.speed(-PI, 1.f));
		assertEpsilonEquals(0.f, this.engine.speed(0.f, .5f));
		assertEpsilonEquals(TWO_PI, this.engine.speed(PI, .5f));
		assertEpsilonEquals(-TWO_PI, this.engine.speed(-PI, .5f));
	}

	/** 
	 */
	public void testAcceleration() {
		assertEpsilonEquals(0.f, this.engine.acceleration(0.f, 0.f, 1.f));
		assertEpsilonEquals(PI, this.engine.acceleration(0.f, PI, 1.f));
		assertEpsilonEquals(-PI, this.engine.acceleration(0.f, -PI, 1.f));
		assertEpsilonEquals(-PI, this.engine.acceleration(PI, 0.f, 1.f));
		assertEpsilonEquals(0.f, this.engine.acceleration(PI, PI, 1.f));
		assertEpsilonEquals(-TWO_PI, this.engine.acceleration(PI, -PI, 1.f));
		assertEpsilonEquals(PI, this.engine.acceleration(-PI, 0.f, 1.f));
		assertEpsilonEquals(TWO_PI, this.engine.acceleration(-PI, PI, 1.f));
		assertEpsilonEquals(0.f, this.engine.acceleration(-PI, -PI, 1.f));

		assertEpsilonEquals(0.f, this.engine.acceleration(0.f, 0.f, .5f));
		assertEpsilonEquals(TWO_PI, this.engine.acceleration(0.f, PI, .5f));
		assertEpsilonEquals(-TWO_PI, this.engine.acceleration(0.f, -PI, .5f));
		assertEpsilonEquals(-TWO_PI, this.engine.acceleration(PI, 0.f, .5f));
		assertEpsilonEquals(0.f, this.engine.acceleration(PI, PI, .5f));
		assertEpsilonEquals(-2.f*TWO_PI, this.engine.acceleration(PI, -PI, .5f));
		assertEpsilonEquals(TWO_PI, this.engine.acceleration(-PI, 0.f, .5f));
		assertEpsilonEquals(2*TWO_PI, this.engine.acceleration(-PI, PI, .5f));
		assertEpsilonEquals(0.f, this.engine.acceleration(-PI, -PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonLaw() {
		assertEpsilonEquals(0.f, this.engine.motionNewtonLaw(0.f, 0.f, 1.f));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw(0.f, PI, 1.f));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw(0.f, -PI, 1.f));
		assertEpsilonEquals(PI, this.engine.motionNewtonLaw(PI, 0.f, 1.f));
		assertEpsilonEquals(ONE_HALF_PI, this.engine.motionNewtonLaw(PI, PI, 1.f));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw(PI, -PI, 1.f));
		assertEpsilonEquals(-PI, this.engine.motionNewtonLaw(-PI, 0.f, 1.f));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw(-PI, PI, 1.f));
		assertEpsilonEquals(-ONE_HALF_PI, this.engine.motionNewtonLaw(-PI, -PI, 1.f));

		assertEpsilonEquals(0.f, this.engine.motionNewtonLaw(0.f, 0.f, .5f));
		assertEpsilonEquals(QUARTER_PI/2.f, this.engine.motionNewtonLaw(0.f, PI, .5f));
		assertEpsilonEquals(-QUARTER_PI/2.f, this.engine.motionNewtonLaw(0.f, -PI, .5f));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw(PI, 0.f, .5f));
		assertEpsilonEquals(DEMI_PI+QUARTER_PI/2.f, this.engine.motionNewtonLaw(PI, PI, .5f));
		assertEpsilonEquals(THREE_QUARTER_PI/2.f, this.engine.motionNewtonLaw(PI, -PI, .5f));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw(-PI, 0.f, .5f));
		assertEpsilonEquals(-THREE_QUARTER_PI/2.f, this.engine.motionNewtonLaw(-PI, PI, .5f));
		assertEpsilonEquals((-PI-QUARTER_PI)/2.f, this.engine.motionNewtonLaw(-PI, -PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonLaw1D() {
		assertEpsilonEquals(0.f, this.engine.motionNewtonLaw1D(0.f, 0, DEMI_PI, 0.f, -QUARTER_PI, DEMI_PI, 1.f));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonLaw1D(0.f, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, 1.f));
		assertEpsilonEquals(-QUARTER_PI/2.f, this.engine.motionNewtonLaw1D(0.f, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, 1.f));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, 0.f, -QUARTER_PI, DEMI_PI, 1.f));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, 1.f));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, 1.f));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, 0.f, -QUARTER_PI, DEMI_PI, 1.f));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, 1.f));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, 1.f));

		assertEpsilonEquals(0.f, this.engine.motionNewtonLaw1D(0.f,0, DEMI_PI,  0.f, -QUARTER_PI, DEMI_PI, .5f));
		assertEpsilonEquals(QUARTER_PI/4.f, this.engine.motionNewtonLaw1D(0.f, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, .5f));
		assertEpsilonEquals(-QUARTER_PI/8.f, this.engine.motionNewtonLaw1D(0.f, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, .5f));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, 0.f, -QUARTER_PI, DEMI_PI, .5f));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, .5f));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonLaw1D(PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, .5f));
		assertEpsilonEquals(-DEMI_PI/2.f, this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, 0.f, -QUARTER_PI, DEMI_PI, .5f));
		assertEpsilonEquals(-DEMI_PI/2.f, this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, PI, -QUARTER_PI, DEMI_PI, .5f));
		assertEpsilonEquals(-DEMI_PI/2.f, this.engine.motionNewtonLaw1D(-PI, 0, DEMI_PI, -PI, -QUARTER_PI, DEMI_PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonLaw1D5() {
		Vector2f v = new Vector2f();
		Vector2f a = new Vector2f();

		//--- DT: 1

		// velocity = (0,0)
		v.set(0.f,0.f);
		a.set(0.f,0.f);
		assertEpsilonEquals(new Vector2f(0.f, 0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(PI,0.f);
		assertEpsilonEquals(new Vector2f(QUARTER_PI,0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,0.f);
		assertEpsilonEquals(new Vector2f(-QUARTER_PI,0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,PI);
		assertEpsilonEquals(new Vector2f(0.f, QUARTER_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,-PI);
		assertEpsilonEquals(new Vector2f(0.f, -QUARTER_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1,1,QUARTER_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1,-1,QUARTER_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.f);
		a.set(0.f,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI, 0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(PI,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI,0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI-QUARTER_PI/2.f,0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,PI);
		assertEpsilonEquals(makeVect(1.f, .5f, DEMI_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,-PI);
		assertEpsilonEquals(makeVect(1.f, -.5f, DEMI_PI), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));

		// velocity = (1,0)
		v.set(1.f,0.f);
		a.set(-1.f,1.f);
		assertEpsilonEquals(makeVect(1,1,(float) Math.sqrt(.5f)), 
				this.engine.motionNewtonLaw1D5(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.f));
		a.set(-1.f,-1.f);
		assertEpsilonEquals(makeVect(1,-1,(float)Math.sqrt(.5f)), 
				this.engine.motionNewtonLaw1D5(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.f));
				
		//--- DT: 1/2

		// velocity = (0,0)
		v.set(0.f,0.f);
		a.set(0.f,0.f);
		assertEpsilonEquals(new Vector2f(0.f, 0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(PI,0.f);
		assertEpsilonEquals(new Vector2f(PI/16.,0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,0.f);
		assertEpsilonEquals(new Vector2f(-PI/16.,0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(0.f,PI);
		assertEpsilonEquals(new Vector2f(0.f, PI/16.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(0.f,-PI);
		assertEpsilonEquals(new Vector2f(0.f, -PI/16.), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1,1,PI/16.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1,-1,PI/16.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.f);
		a.set(0.f,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI/2.f, 0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(PI,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI/2.f,0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,0.f);
		assertEpsilonEquals(new Vector2f((DEMI_PI-PI/16.)/2.f,0.f), 
				this.engine.motionNewtonLaw1D5(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonLaw2D() {
		Vector2f v = new Vector2f();
		Vector2f a = new Vector2f();

		//--- DT: 1

		// velocity = (0,0)
		v.set(0.f,0.f);
		a.set(0.f,0.f);
		assertEpsilonEquals(new Vector2f(0.f, 0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(PI,0.f);
		assertEpsilonEquals(new Vector2f(QUARTER_PI,0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,0.f);
		assertEpsilonEquals(new Vector2f(-QUARTER_PI,0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,PI);
		assertEpsilonEquals(new Vector2f(0.f, QUARTER_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,-PI);
		assertEpsilonEquals(new Vector2f(0.f, -QUARTER_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1,1,QUARTER_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1,-1,QUARTER_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.f);
		a.set(0.f,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI, 0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(PI,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI,0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI-QUARTER_PI/2.f,0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,PI);
		assertEpsilonEquals(makeVect(1.f, .5f, DEMI_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,-PI);
		assertEpsilonEquals(makeVect(1.f, -.5f, DEMI_PI), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));

		// velocity = (1,0)
		v.set(1.f,0.f);
		a.set(-1.f,1.f);
		assertEpsilonEquals(makeVect(1,1,(float)Math.sqrt(.5f)), 
				this.engine.motionNewtonLaw2D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.f));
		a.set(-1.f,-1.f);
		assertEpsilonEquals(makeVect(1,-1,(float)Math.sqrt(.5f)), 
				this.engine.motionNewtonLaw2D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.f));
				
		//--- DT: 1/2

		// velocity = (0,0)
		v.set(0.f,0.f);
		a.set(0.f,0.f);
		assertEpsilonEquals(new Vector2f(0.f, 0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(PI,0.f);
		assertEpsilonEquals(new Vector2f(PI/16.,0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,0.f);
		assertEpsilonEquals(new Vector2f(-PI/16.,0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(0.f,PI);
		assertEpsilonEquals(new Vector2f(0.f, PI/16.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(0.f,-PI);
		assertEpsilonEquals(new Vector2f(0.f, -PI/16.), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1,1,PI/16.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1,-1,PI/16.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.f);
		a.set(0.f,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI/2.f, 0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(PI,0.f);
		assertEpsilonEquals(new Vector2f(DEMI_PI/2.f,0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,0.f);
		assertEpsilonEquals(new Vector2f((DEMI_PI-PI/16.)/2.f,0.f), 
				this.engine.motionNewtonLaw2D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonLaw3D() {
		Vector3f v = new Vector3f();
		Vector3f a = new Vector3f();

		//--- DT: 1

		// velocity = (0,0)
		v.set(0.f,0.f,0.f);
		a.set(0.f,0.f,0.f);
		assertEpsilonEquals(new Vector3f(0.f, 0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(PI,0.f,0.f);
		assertEpsilonEquals(new Vector3f(QUARTER_PI,0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,0.f,0.f);
		assertEpsilonEquals(new Vector3f(-QUARTER_PI,0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,PI,0.f);
		assertEpsilonEquals(new Vector3f(0.f, QUARTER_PI,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,-PI,0.f);
		assertEpsilonEquals(new Vector3f(0.f, -QUARTER_PI,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,PI,0.f);
		assertEpsilonEquals(makeVect(-1,1,0.f,QUARTER_PI), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,-PI,0.f);
		assertEpsilonEquals(makeVect(-1,-1,0.f,QUARTER_PI), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.f,0.f);
		a.set(0.f,0.f,0.f);
		assertEpsilonEquals(new Vector3f(DEMI_PI, 0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(PI,0.f,0.f);
		assertEpsilonEquals(new Vector3f(DEMI_PI,0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(-PI,0.f,0.f);
		assertEpsilonEquals(new Vector3f(DEMI_PI-QUARTER_PI/2.f,0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,PI,0.f);
		assertEpsilonEquals(makeVect(1.f, .5f,0.f, DEMI_PI), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));
		a.set(0.f,-PI,0.f);
		assertEpsilonEquals(makeVect(1.f, -.5f,0, DEMI_PI), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, 1.f));

		// velocity = (1,0)
		v.set(1.f,0.f,0.f);
		a.set(-1.f,1.f,0.f);
		assertEpsilonEquals(makeVect(1,1,0.f,(float)Math.sqrt(.5f)), 
				this.engine.motionNewtonLaw3D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.f));
		a.set(-1.f,-1.f,0.f);
		assertEpsilonEquals(makeVect(1,-1,0.f,(float)Math.sqrt(.5f)), 
				this.engine.motionNewtonLaw3D(v, 0, SQRT_2, a, -SQRT_2, SQRT_2, 1.f));
				
		//--- DT: 1/2

		// velocity = (0,0)
		v.set(0.f,0.f,0.f);
		a.set(0.f,0.f,0.f);
		assertEpsilonEquals(new Vector3f(0.f, 0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(PI,0.f,0.f);
		assertEpsilonEquals(new Vector3f(PI/16.,0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,0.f,0.f);
		assertEpsilonEquals(new Vector3f(-PI/16.,0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(0.f,PI,0.f);
		assertEpsilonEquals(new Vector3f(0.f, PI/16.,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(0.f,-PI,0.f);
		assertEpsilonEquals(new Vector3f(0.f, -PI/16.,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,PI,0.f);
		assertEpsilonEquals(makeVect(-1,1,0.f,PI/16.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,-PI,0.f);
		assertEpsilonEquals(makeVect(-1,-1,0.f,PI/16.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));

		// velocity = (PI/2,0)
		v.set(DEMI_PI,0.f,0.f);
		a.set(0.f,0.f,0.f);
		assertEpsilonEquals(new Vector3f(DEMI_PI/2.f, 0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(PI,0.f,0.f);
		assertEpsilonEquals(new Vector3f(DEMI_PI/2.f,0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
		a.set(-PI,0.f,0.f);
		assertEpsilonEquals(new Vector3f((DEMI_PI-PI/16.)/2.f,0.f,0.f), 
				this.engine.motionNewtonLaw3D(v, 0, DEMI_PI, a, -QUARTER_PI, DEMI_PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonEuler1Law() {
		assertEpsilonEquals(0.f, this.engine.motionNewtonEuler1Law(0.f, 1.f));
		assertEpsilonEquals(PI, this.engine.motionNewtonEuler1Law(PI, 1.f));
		assertEpsilonEquals(-PI, this.engine.motionNewtonEuler1Law(-PI, 1.f));

		assertEpsilonEquals(0.f, this.engine.motionNewtonEuler1Law(0.f, .5f));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonEuler1Law(PI, .5f));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonEuler1Law(-PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonEuler1Law1D() {
		assertEpsilonEquals(0.f, this.engine.motionNewtonEuler1Law1D(0.f, 0, DEMI_PI, 1.f));
		assertEpsilonEquals(DEMI_PI, this.engine.motionNewtonEuler1Law1D(PI, 0, DEMI_PI, 1.f));
		assertEpsilonEquals(-DEMI_PI, this.engine.motionNewtonEuler1Law1D(-PI, 0, DEMI_PI, 1.f));

		assertEpsilonEquals(0.f, this.engine.motionNewtonEuler1Law1D(0.f, 0, DEMI_PI, .5f));
		assertEpsilonEquals(QUARTER_PI, this.engine.motionNewtonEuler1Law1D(PI, 0, DEMI_PI, .5f));
		assertEpsilonEquals(-QUARTER_PI, this.engine.motionNewtonEuler1Law1D(-PI, 0, DEMI_PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonEuler1Law1D5() {
		Vector2f v = new Vector2f();
		
		v.set(0.f,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,0.f),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.f));
		v.set(0.f,PI);
		assertEpsilonEquals(makeVect(0.f,1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.f));
		v.set(0.f,-PI);
		assertEpsilonEquals(makeVect(0.f,-1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.f));
		v.set(PI,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.f));
		v.set(PI,PI);
		assertEpsilonEquals(makeVect(1.f,1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.f));
		v.set(PI,-PI);
		assertEpsilonEquals(makeVect(1.f,-1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.f));
		v.set(-PI,0.f);
		assertEpsilonEquals(makeVect(-1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.f));
		v.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1.f,1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.f));
		v.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1.f,-1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, 1.f));

		v.set(0.f,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,0.f),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5f));
		v.set(0.f,PI);
		assertEpsilonEquals(makeVect(0.f,1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5f));
		v.set(0.f,-PI);
		assertEpsilonEquals(makeVect(0.f,-1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5f));
		v.set(PI,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5f));
		v.set(PI,PI);
		assertEpsilonEquals(makeVect(1.f,1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5f));
		v.set(PI,-PI);
		assertEpsilonEquals(makeVect(1.f,-1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5f));
		v.set(-PI,0.f);
		assertEpsilonEquals(makeVect(-1.f,0.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5f));
		v.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1.f,1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5f));
		v.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1.f,-1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law1D5(v, 0, DEMI_PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonEuler1Law2D() {
		Vector2f v = new Vector2f();
		
		v.set(0.f,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,0.f),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.f));
		v.set(0.f,PI);
		assertEpsilonEquals(makeVect(0.f,1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.f));
		v.set(0.f,-PI);
		assertEpsilonEquals(makeVect(0.f,-1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.f));
		v.set(PI,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.f));
		v.set(PI,PI);
		assertEpsilonEquals(makeVect(1.f,1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.f));
		v.set(PI,-PI);
		assertEpsilonEquals(makeVect(1.f,-1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.f));
		v.set(-PI,0.f);
		assertEpsilonEquals(makeVect(-1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.f));
		v.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1.f,1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.f));
		v.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1.f,-1.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, 1.f));

		v.set(0.f,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,0.f),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5f));
		v.set(0.f,PI);
		assertEpsilonEquals(makeVect(0.f,1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5f));
		v.set(0.f,-PI);
		assertEpsilonEquals(makeVect(0.f,-1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5f));
		v.set(PI,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5f));
		v.set(PI,PI);
		assertEpsilonEquals(makeVect(1.f,1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5f));
		v.set(PI,-PI);
		assertEpsilonEquals(makeVect(1.f,-1.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5f));
		v.set(-PI,0.f);
		assertEpsilonEquals(makeVect(-1.f,0.f,DEMI_PI/2.f),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5f));
		v.set(-PI,PI);
		assertEpsilonEquals(makeVect(-1.f,1.f,DEMI_PI/2.f),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5f));
		v.set(-PI,-PI);
		assertEpsilonEquals(makeVect(-1.f,-1.f,DEMI_PI/2.f),
				this.engine.motionNewtonEuler1Law2D(v, 0, DEMI_PI, .5f));
	}

	/**
	 */
	public void testMotionNewtonEuler1Law3D() {
		Vector3f v = new Vector3f();
		
		v.set(0.f,0.f,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,0.f,0.f),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.f));
		v.set(0.f,PI,0.f);
		assertEpsilonEquals(makeVect(0.f,1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.f));
		v.set(0.f,-PI,0.f);
		assertEpsilonEquals(makeVect(0.f,-1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.f));
		v.set(PI,0.f,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.f));
		v.set(PI,PI,0.f);
		assertEpsilonEquals(makeVect(1.f,1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.f));
		v.set(PI,-PI,0.f);
		assertEpsilonEquals(makeVect(1.f,-1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.f));
		v.set(-PI,0.f,0.f);
		assertEpsilonEquals(makeVect(-1.f,0.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.f));
		v.set(-PI,PI,0.f);
		assertEpsilonEquals(makeVect(-1.f,1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.f));
		v.set(-PI,-PI,0.f);
		assertEpsilonEquals(makeVect(-1.f,-1.f,0.f,DEMI_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, 1.f));

		v.set(0.f,0.f,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,0.f,0.f),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5f));
		v.set(0.f,PI,0.f);
		assertEpsilonEquals(makeVect(0.f,1.f,0.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5f));
		v.set(0.f,-PI,0.f);
		assertEpsilonEquals(makeVect(0.f,-1.f,0.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5f));
		v.set(PI,0.f,0.f);
		assertEpsilonEquals(makeVect(1.f,0.f,0.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5f));
		v.set(PI,PI,0.f);
		assertEpsilonEquals(makeVect(1.f,1.f,0.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5f));
		v.set(PI,-PI,0.f);
		assertEpsilonEquals(makeVect(1.f,-1.f,0.f,QUARTER_PI),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5f));
		v.set(-PI,0.f,0.f);
		assertEpsilonEquals(makeVect(-1.f,0.f,0.f,DEMI_PI/2.f),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5f));
		v.set(-PI,PI,0.f);
		assertEpsilonEquals(makeVect(-1.f,1.f,0.f,DEMI_PI/2.f),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5f));
		v.set(-PI,-PI,0.f);
		assertEpsilonEquals(makeVect(-1.f,-1.f,0.f,DEMI_PI/2.f),
				this.engine.motionNewtonEuler1Law3D(v, 0, DEMI_PI, .5f));
	}

}