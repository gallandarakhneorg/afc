/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.system;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.util.ref.AbstractTestCase;

/**
 * Unit test for {@link CoordinateSystem2D}.
 * 
 * @author $Author: cbohrhaure$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CoordinateSystem2DTest extends AbstractTestCase {

	/**
	 */
	public void testGetDefaultSimulationCoordinateSystem() {
		CoordinateSystem2D cs = CoordinateSystemConstants.SIMULATION_2D;
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND,cs);
	}

	private void runTestFromVectors(Class<?>[] types) {
		assertException(AssertionError.class, CoordinateSystem2D.class, "fromVectors", types, //$NON-NLS-1$
				0 );
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.fromVectors(
				1 ));
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem2D.fromVectors(
				-1 ));
	}
	
	/**
	 */
	public void testFromVectorsFloat() {
		runTestFromVectors(new Class<?>[] {float.class});
	}

	/**
	 */
	public void testFromVectorsInt() {
		runTestFromVectors(new Class<?>[] {int.class});
	}

	/**
	 */
	public void testToDefaultPoint2f() {
		Point2f pt, pt2, ptInv;
		
		pt = randomPoint2F();
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
	public void testToDefaultPoint2d() {
		Point2f pt, pt2, ptInv;
		
		pt = randomPoint2D();
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
	public void testFromDefaultPoint2d() {
		Point2f pt, pt2, ptInv;
		
		pt = randomPoint2D();
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
	public void testFromDefaultPoint2f() {
		Point2f pt, pt2, ptInv;
		
		pt = randomPoint2F();
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
	public void testToDefaultVector2d() {
		Vector2f vt, vt2, vtInv;
		
		vt = randomVector2D();
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
	public void testToDefaultVector2f() {
		Vector2f vt, vt2, vtInv;
		
		vt = randomVector2F();
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
	public void testFromDefaultVector2d() {
		Vector2f vt, vt2, vtInv;
		
		vt = randomVector2D();
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
	public void testFromDefaultVector2f() {
		Vector2f vt, vt2, vtInv;
		
		vt = randomVector2F();
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
	public void testToDefaultFloat() {
		float rot, conv;
		
		rot = this.RANDOM.nextFloat()*MathConstants.TWO_PI;
		
		conv = CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(rot);
		assertEquals(rot, conv);
		
		conv = CoordinateSystem2D.XY_LEFT_HAND.fromDefault(rot);
		assertEquals(-rot, conv);
	}

	/**
	 */
	public void testFromDefaultFloat() {
		float rot, conv;
		
		rot = this.RANDOM.nextFloat()*MathConstants.TWO_PI;
		
		conv = CoordinateSystem2D.XY_RIGHT_HAND.fromDefault(rot);
		assertEquals(rot, conv);
		
		conv = CoordinateSystem2D.XY_LEFT_HAND.fromDefault(rot);
		assertEquals(-rot, conv);
	}

	/**
	 */
	public void testToSystemPoint2dCoordinateSystem2D() {
		Point2f pt, pt2, ptInv;
		
		pt = randomPoint2D();
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
	public void testToSystemPoint2fCoordinateSystem2D() {
		Point2f pt, pt2, ptInv;
		
		pt = randomPoint2F();
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
	public void testToSystemVector2dCoordinateSystem2D() {
		Vector2f vt, vt2, vtInv;
		
		vt = randomVector2D();
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
	public void testToSystemVector2fCoordinateSystem2D() {
		Vector2f vt, vt2, vtInv;
		
		vt = randomVector2F();
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
	public void testToSystemFloatCoordinateSystem2D() {
		float rot, conv;
		
		rot = this.RANDOM.nextFloat()*MathConstants.TWO_PI;
		
		conv = CoordinateSystem2D.XY_RIGHT_HAND.toSystem(rot, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEquals(rot, conv);
		
		conv = CoordinateSystem2D.XY_RIGHT_HAND.toSystem(rot, CoordinateSystem2D.XY_LEFT_HAND);
		assertEquals(-rot, conv);

		conv = CoordinateSystem2D.XY_LEFT_HAND.toSystem(rot, CoordinateSystem2D.XY_LEFT_HAND);
		assertEquals(rot, conv);
		
		conv = CoordinateSystem2D.XY_LEFT_HAND.toSystem(rot, CoordinateSystem2D.XY_RIGHT_HAND);
		assertEquals(-rot, conv);
	}

}
