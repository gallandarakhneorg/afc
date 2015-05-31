/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
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
package org.arakhne.afc.math.geometry.coordinatesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Random;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.junit.Test;

/**
 * Unit test for {@link CoordinateSystem2D}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CoordinateSystem2DTest extends AbstractMathTestCase {

	/**
	 */
	@Test
	public void getDefaultSimulationCoordinateSystem() {
		CoordinateSystem2D cs = CoordinateSystemConstants.SIMULATION_2D;
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND,cs);
	}

	/**
	 */
	@Test
	public void fromVectorsDouble() {
		try {
			CoordinateSystem2D.fromVectors(0.);
			fail("Expecting exception of type AssertionError");
		} catch (AssertionError e) {
			// Expected
		}
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.fromVectors(
				1. ));
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem2D.fromVectors(
				-1. ));
	}

	/**
	 */
	@Test
	public void fromVectorsInt() {
		try {
			CoordinateSystem2D.fromVectors(0);
			fail("Expecting exception of type AssertionError");
		} catch (AssertionError e) {
			// Expected
		}
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.fromVectors(
				1 ));
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem2D.fromVectors(
				-1 ));
	}

	/**
	 */
	@Test
	public void toDefaultPoint2f() {
		Point2f pt, pt2, ptInv;
		
		pt = randomPoint2f();
		ptInv = new Point2f(pt.getX(), -pt.getY());
		
		pt2 = new Point2f(pt);
		CoordinateSystem2D.XY_RIGHT_HAND.toDefault(pt2);
		assertEquals(pt, pt2);
		
		pt2 = new Point2f(pt);
		CoordinateSystem2D.XY_LEFT_HAND.toDefault(pt2);
		assertEquals(ptInv, pt2);
	}

	/**
	 */
	@Test
	public void fromDefaultPoint2f() {
		Point2f pt, pt2, ptInv;
		
		pt = randomPoint2f();
		ptInv = new Point2f(pt.getX(), -pt.getY());
		
		pt2 = new Point2f(pt);
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(pt2);
		assertEquals(pt, pt2);
		
		pt2 = new Point2f(pt);
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(pt2);
		assertEquals(ptInv, pt2);
	}

	/**
	 */
	@Test
	public void toDefaultVector2f() {
		Vector2f vt, vt2, vtInv;
		
		vt = randomVector2f();
		vtInv = new Vector2f(vt.getX(), -vt.getY());
		
		vt2 = new Vector2f(vt);
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(vt2);
		assertEquals(vt, vt2);
		
		vt2 = new Vector2f(vt);
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(vt2);
		assertEquals(vtInv, vt2);
	}

	/**
	 */
	@Test
	public void fromDefaultVector2f() {
		Vector2f vt, vt2, vtInv;
		
		vt = randomVector2f();
		vtInv = new Vector2f(vt.getX(), -vt.getY());
		
		vt2 = new Vector2f(vt);
		CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(vt2);
		assertEquals(vt, vt2);
		
		vt2 = new Vector2f(vt);
		CoordinateSystem2D.XY_LEFT_HAND.fromDefault(vt2);
		assertEquals(vtInv, vt2);
	}

	/**
	 */
	@Test
	public void toDefaultDouble() {
		double rot, conv;
		
		rot = this.random.nextDouble()*Math.PI*2.;
		
		conv = CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(rot);
		assertEpsilonEquals(rot, conv);
		
		conv = CoordinateSystem2D.XY_LEFT_HAND.fromDefault(rot);
		assertEpsilonEquals(-rot, conv);
	}

	/**
	 */
	@Test
	public void fromDefaultDouble() {
		double rot, conv;
		
		rot = this.random.nextDouble()*Math.PI*2.;
		
		conv = CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(rot);
		assertEpsilonEquals(rot, conv);
		
		conv = CoordinateSystem2D.XY_LEFT_HAND.fromDefault(rot);
		assertEpsilonEquals(-rot, conv);
	}

	/**
	 */
	@Test
	public void toSystemPoint2fCoordinateSystem2D() {
		Point2f pt, pt2, ptInv;
		
		pt = randomPoint2f();
		ptInv = new Point2f(pt.getX(), -pt.getY());
		
		pt2 = new Point2f(pt);
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(pt2, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEquals(pt, pt2);
		
		pt2 = new Point2f(pt);
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(pt2, CoordinateSystem2D.XY_LEFT_HAND);
		assertEquals(ptInv, pt2);

		pt2 = new Point2f(pt);
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(pt2, CoordinateSystem2D.XY_LEFT_HAND);
		assertEquals(pt, pt2);
		
		pt2 = new Point2f(pt);
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(pt2, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEquals(ptInv, pt2);
	}

	/**
	 */
	@Test
	public void toSystemVector2fCoordinateSystem2D() {
		Vector2f vt, vt2, vtInv;
		
		vt = randomVector2f();
		vtInv = new Vector2f(vt.getX(), -vt.getY());
		
		vt2 = new Vector2f(vt);
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(vt2, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEquals(vt, vt2);
		
		vt2 = new Vector2f(vt);
		CoordinateSystem2D.XY_RIGHT_HAND.toSystem(vt2, CoordinateSystem2D.XY_LEFT_HAND);
		assertEquals(vtInv, vt2);

		vt2 = new Vector2f(vt);
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(vt2, CoordinateSystem2D.XY_LEFT_HAND);
		assertEquals(vt, vt2);
		
		vt2 = new Vector2f(vt);
		CoordinateSystem2D.XY_LEFT_HAND.toSystem(vt2, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEquals(vtInv, vt2);
	}

	/**
	 */
	@Test
	public void toSystemDoubleCoordinateSystem2D() {
		double rot, conv;
		
		rot = this.random.nextDouble()*Math.PI*2.;
		
		conv = CoordinateSystem2D.XY_RIGHT_HAND.toSystem(rot, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(rot, conv);
		
		conv = CoordinateSystem2D.XY_RIGHT_HAND.toSystem(rot, CoordinateSystem2D.XY_LEFT_HAND);
		assertEpsilonEquals(-rot, conv);

		conv = CoordinateSystem2D.XY_LEFT_HAND.toSystem(rot, CoordinateSystem2D.XY_LEFT_HAND);
		assertEpsilonEquals(rot, conv);
		
		conv = CoordinateSystem2D.XY_LEFT_HAND.toSystem(rot, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEpsilonEquals(-rot, conv);
	}

}
