/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class QuaternionTest extends AbstractMathTestCase {
	
	@Test
	public void testClone() {
		Quaternion q1 = new Quaternion(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Quaternion q2 = q1.clone();
		
		//assertTrue(q1.equals(q2));
	}

	@Test
	public void getX() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		
		assertEpsilonEquals(a,q1.getX()*Math.sqrt(a*a+b*b+c*c+d*d));
	}

	@Test
	public void setX() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d); 
		
		double set = this.random.nextDouble();
		
		q1.setX(set);
		
		assertEpsilonEquals(set,q1.getX());
	}

	@Test
	public void getY() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		
		assertEpsilonEquals(b,q1.getY()*Math.sqrt(a*a+b*b+c*c+d*d));
	}

	@Test
	public void setY() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d); 
		
		double set = this.random.nextDouble();
		
		q1.setY(set);
		
		assertEpsilonEquals(set,q1.getY());
	}

	@Test
	public void getZ() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		
		assertEpsilonEquals(c,q1.getZ()*Math.sqrt(a*a+b*b+c*c+d*d));
	}

	@Test
	public void setZ() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d); 
		
		double set = this.random.nextDouble();
		
		q1.setZ(set);
		
		assertEpsilonEquals(set,q1.getZ());
	}

	@Test
	public void getW() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		
		assertEpsilonEquals(d,q1.getW()*Math.sqrt(a*a+b*b+c*c+d*d));
	}

	@Test
	public void setW() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d); 
		
		double set = this.random.nextDouble();
		
		q1.setW(set);
		
		assertEpsilonEquals(set,q1.getW());
	}

	@Test
	public void testEquals() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		Quaternion q2 = new Quaternion(q1);
		
		assertTrue(q1.equals(q2));
		
		q2.set(b,a,d,c);
		
		assertFalse(q1.equals(q2));
	}

	@Test
	public void epsilonEquals() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void testHashCode() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void conjugateQuaternion() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		Quaternion conjugate = q1.clone();
		
		conjugate.setX(-conjugate.getX());
		conjugate.setY(-conjugate.getY());
		conjugate.setZ(-conjugate.getZ());
		
		q1.conjugate(q1);
		assertTrue(conjugate.equals(q1));
	}

	@Test
	public void conjugate() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		Quaternion conjugate = q1.clone();
		
		conjugate.setX(-conjugate.getX());
		conjugate.setY(-conjugate.getY());
		conjugate.setZ(-conjugate.getZ());
		
		q1.conjugate();
		assertTrue(conjugate.equals(q1));
	}

	@Test
	public void mulQuaternionQuaternion() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double aP = this.random.nextDouble();
		double bP = this.random.nextDouble();
		double cP = this.random.nextDouble();
		double dP = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		Quaternion q2 = new Quaternion(aP,bP,cP,dP);
		
		Quaternion mul = new Quaternion();
				
		mul.setW(q1.getW()*q2.getW()-q1.getX()*q2.getX()-q1.getY()*q2.getY()-q1.getZ()*q2.getZ());
		mul.setX(q1.getW()*q2.getX()+q1.getX()*q2.getW()+q1.getY()*q2.getZ()-q1.getZ()*q2.getY());
		mul.setY(q1.getW()*q2.getY()+q1.getY()*q2.getW()-q1.getX()*q2.getZ()+q1.getZ()*q2.getX());
		mul.setZ(q1.getZ()*q2.getW()+q1.getW()*q2.getZ()+q1.getX()*q2.getY()-q1.getY()*q2.getX());
	
		q1.mul(q1,q2);
		assertTrue(mul.equals(q1));
	}


	@Test
	public void mulQuaternion() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double aP = this.random.nextDouble();
		double bP = this.random.nextDouble();
		double cP = this.random.nextDouble();
		double dP = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		Quaternion q2 = new Quaternion(aP,bP,cP,dP);
		
		Quaternion mul = new Quaternion();
				
		mul.setW(q1.getW()*q2.getW()-q1.getX()*q2.getX()-q1.getY()*q2.getY()-q1.getZ()*q2.getZ());
		mul.setX(q1.getW()*q2.getX()+q1.getX()*q2.getW()+q1.getY()*q2.getZ()-q1.getZ()*q2.getY());
		mul.setY(q1.getW()*q2.getY()+q1.getY()*q2.getW()-q1.getX()*q2.getZ()+q1.getZ()*q2.getX());
		mul.setZ(q1.getZ()*q2.getW()+q1.getW()*q2.getZ()+q1.getX()*q2.getY()-q1.getY()*q2.getX());
	
		q1.mul(q2);
		assertTrue(mul.equals(q1));
	}  

	@Test
	public void mulInverseQuaternionQuaternion() {   
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double aP = this.random.nextDouble();
		double bP = this.random.nextDouble();
		double cP = this.random.nextDouble();
		double dP = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		Quaternion q2 = new Quaternion(aP,bP,cP,dP);
		Quaternion mulInv = new Quaternion();
		
		mulInv.mulInverse(q1, q2);
		
		q2.inverse();
		q1.mul(q2);
		
		assertTrue(q1.equals(mulInv));
		
		
	}

	@Test
	public void mulInverseQuaternion() {  
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		double aP = this.random.nextDouble();
		double bP = this.random.nextDouble();
		double cP = this.random.nextDouble();
		double dP = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		Quaternion q2 = new Quaternion(aP,bP,cP,dP);
		Quaternion cloneQ1 = q1.clone();
		
		q1.mulInverse(q2);
		
		q2.inverse();
		cloneQ1.mul(q2);
		
		assertTrue(q1.equals(cloneQ1));
	}

	@Test
	public void inverseQuaternion() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		
		Quaternion inv = q1.clone();
		inv.conjugate();
		double normSquared = inv.getW()*inv.getW() + inv.getX()*inv.getX() + inv.getY()*inv.getY() + inv.getZ()*inv.getZ();
		
		inv.setW(inv.getW()/normSquared);
		inv.setX(inv.getX()/normSquared);
		inv.setY(inv.getY()/normSquared);
		inv.setZ(inv.getZ()/normSquared);
		
		q1.inverse(q1);
		
		assertTrue(q1.equals(inv));
	}

	@Test
	public void inverse() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion(a,b,c,d);
		
		Quaternion inv = q1.clone();
		inv.conjugate();
		double normSquared = inv.getW()*inv.getW() + inv.getX()*inv.getX() + inv.getY()*inv.getY() + inv.getZ()*inv.getZ();
		
		inv.setW(inv.getW()/normSquared);
		inv.setX(inv.getX()/normSquared);
		inv.setY(inv.getY()/normSquared);
		inv.setZ(inv.getZ()/normSquared);
		
		q1.inverse();
		
		assertTrue(q1.equals(inv));
		
	}

	@Test
	public void normalizeQuaternion() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion();
		
		q1.setW(a);
		q1.setX(b);
		q1.setY(c);
		q1.setZ(d);
		
		Quaternion q2 = new Quaternion(b,c,d,a);
		
		q1.normalize(q1);
		
		assertTrue(q1.equals(q2));
	}

	@Test
	public void normalize() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble();
		double d = this.random.nextDouble();
		Quaternion q1 = new Quaternion();
		
		q1.setW(a);
		q1.setX(b);
		q1.setY(c);
		q1.setZ(d);
		
		Quaternion q2 = new Quaternion(b,c,d,a);
		
		q1.normalize();
		
		assertTrue(q1.equals(q2));
	}

	@Test
	public void setFromMatrixMatrix4f() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setFromMatrixMatrix3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setQuaternion() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setAxisAngleVector3DDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setAxisAngleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getAxis() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getAngle() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void interpolateQuaternionTestDouble() {
		throw new UnsupportedOperationException();
	}


	@Test
	public void interpolateQuaternionQuaternionDouble() { 
		throw new UnsupportedOperationException();
	}

	@Test
	public void setEulerAnglesEulerAngles() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setEulerAnglesDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setEulerAnglesDoubleDoubleDoubleCoordinateSystem3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getEulerAnglesCoordinateSystem3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getEulerAngles() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getAxisAngle() {
		throw new UnsupportedOperationException();
	}

}