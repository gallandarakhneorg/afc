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

import static org.arakhne.afc.math.MathConstants.PI;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.junit.Test;

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Vector3fTest extends AbstractMathTestCase {

	@Test
	public void testClone() {
		Vector3f r = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f b = r.clone();
		
		assertNotSame(b, r);
		assertEpsilonEquals(r.getX(), b.getX());
		assertEpsilonEquals(r.getY(), b.getY());
		assertEpsilonEquals(r.getZ(), b.getZ());
		 
		b.set(r.getX()+1f, r.getY()+1f, r.getZ()+1f);

		assertNotEpsilonEquals(r.getX(), b.getX());
		assertNotEpsilonEquals(r.getY(), b.getY());
		assertNotEpsilonEquals(r.getZ(), b.getZ());
	}

	@Test
	public void angleVector3D() {
		Vector3f vector = new Vector3f(1,2,0);
		Vector3f vector2 = new Vector3f(-2,1,0);
		Vector3f vector3 = new Vector3f(-1,-1,-1);
		Vector3f vector4 = new Vector3f(1,0,0);
		
		assertEpsilonEquals(PI/2f,vector.angle(vector2));
		assertEpsilonEquals(Math.acos(-1/Math.sqrt(3)),vector4.angle(vector3));
		assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.angle(vector));
		assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.angle(vector2));
		assertEpsilonEquals(0,vector.angle(vector));
	}

	@Test
	public void dotVector3D() {
		Vector3f a = new Vector3f(10*this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble()*this.random.nextDouble());
		Vector3f b = new Vector3f(this.random.nextDouble(),5*this.random.nextDouble(),this.random.nextDouble());
		
		double product = a.getX()*b.getX() + a.getY()*b.getY() + a.getZ()*b.getZ();
		
		assertEpsilonEquals(product,a.dot(b));
		
	}

	@Test
	public void mulMatrix3f() {
		Vector3f vector = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Matrix3f matrix = new Matrix3f(1,2,4,5,1,3,9,-2,1);
		
		Vector3D product = new Vector3f(vector.getX()+2*vector.getY()+4*vector.getZ(),5*vector.getX()+vector.getY()+3*vector.getZ(),9*vector.getX()-2*vector.getY()+vector.getZ());
		
		assertTrue(product.equals(vector.mul(matrix)));
	}

	@Test
	public void determinantDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		double a = this.random.nextDouble();
		double b = this.random.nextDouble();
		double c = this.random.nextDouble(); 
		double d = this.random.nextDouble();
		double e = this.random.nextDouble();
		double f = this.random.nextDouble(); 
		double g = this.random.nextDouble();
		double h = this.random.nextDouble();
		double i = this.random.nextDouble(); 
		Matrix3f matrix = new Matrix3f(a,b,c,
								       d,e,f,
								       g,h,i);
		Vector3f temp = new Vector3f();
		double determinant = a*e*i -a*f*h -b*d*i + b*f*g + c*d*h - c*e*g; 
		
		assertEpsilonEquals(determinant,FunctionalVector3D.determinant(a, b, c, d, e, f, g, h, i));
		
	}

	@Test
	public void perpVector3D() {
		// TODO still remain problem here
		Vector3f v1 = new Vector3f(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble());
		
		double dotProduct = v1.dot(v2);
		double perpProduct = Math.abs(v1.perp(v2));
		double angle = v1.angle(v2);
		
		assertEpsilonEquals(Math.tan(angle),perpProduct/dotProduct);
		
	}

	@Test
	public void signedAngleDoubleDoubleDoubleDoubleDoubleDouble() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble());

		assertEpsilonEquals(
				0.f,
				FunctionalVector3D.signedAngle(v1.getX(),v1.getY(),v1.getZ(),v1.getX(),v1.getY(),v1.getZ()));
		assertEpsilonEquals(
				0.f,
				FunctionalVector3D.signedAngle(v2.getX(),v2.getY(),v2.getZ(),v2.getX(),v2.getY(),v2.getZ()));

		double sAngle1 = FunctionalVector3D.signedAngle(v1.getX(),v1.getY(),v1.getZ(),v2.getX(),v2.getY(),v2.getZ());
		double sAngle2 = FunctionalVector3D.signedAngle(v2.getX(),v2.getY(),v2.getZ(),v1.getX(),v1.getY(),v1.getZ());

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

	}

	@Test
	public void crossVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f cross1 = new Vector3f(-v1.getZ()*v2.getY()+v1.getY()*v2.getZ(),v1.getZ()*v2.getX()-v1.getX()*v2.getZ(),-v1.getY()*v2.getX()+v1.getX()*v2.getY());
		
		assertTrue(cross1.equals(v1.cross(v2)));
		cross1.negate();
		assertTrue(cross1.equals(v2.cross(v1)));	
	}

	@Test
	public void crossVector3DVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f cross1 = new Vector3f(-v1.getZ()*v2.getY()+v1.getY()*v2.getZ(),v1.getZ()*v2.getX()-v1.getX()*v2.getZ(),-v1.getY()*v2.getX()+v1.getX()*v2.getY());
		Vector3f cross2 = new Vector3f();
		cross2.cross(v1, v2);
		
		assertTrue(cross1.equals(cross2));
		cross1.negate();
		cross2.cross(v2, v1);
		assertTrue(cross1.equals(cross2)); 
		cross1.negate();
	}

	@Test
	public void crossLeftHandVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f cross1 = new Vector3f(v1.getZ()*v2.getY()-v1.getY()*v2.getZ(),-v1.getZ()*v2.getX()+v1.getX()*v2.getZ(),v1.getY()*v2.getX()-v1.getX()*v2.getY());
		
		assertTrue(cross1.equals(v1.crossLeftHand(v2)));
		cross1.negate();
		assertTrue(cross1.equals(v2.crossLeftHand(v1)));
	}

	@Test
	public void crossLeftHandVector3DVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f cross1 = new Vector3f(v1.getZ()*v2.getY()-v1.getY()*v2.getZ(),-v1.getZ()*v2.getX()+v1.getX()*v2.getZ(),v1.getY()*v2.getX()-v1.getX()*v2.getY());
		Vector3f cross2 = new Vector3f();
		cross2.crossLeftHand(v1, v2);
		
		assertTrue(cross1.equals(cross2));
		cross1.negate();
		cross2.crossLeftHand(v2, v1);
		assertTrue(cross1.equals(cross2)); 
	}

	@Test
	public void crossRightHandVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f cross1 = new Vector3f(-v1.getZ()*v2.getY()+v1.getY()*v2.getZ(),v1.getZ()*v2.getX()-v1.getX()*v2.getZ(),-v1.getY()*v2.getX()+v1.getX()*v2.getY());
		
		assertTrue(cross1.equals(v1.crossRightHand(v2)));
		cross1.negate();
		assertTrue(cross1.equals(v2.crossRightHand(v1)));
	}

	@Test
	public void crossRightHandVector3DVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f cross1 = new Vector3f(-v1.getZ()*v2.getY()+v1.getY()*v2.getZ(),v1.getZ()*v2.getX()-v1.getX()*v2.getZ(),-v1.getY()*v2.getX()+v1.getX()*v2.getY());
		Vector3f cross2 = new Vector3f();
		cross2.crossRightHand(v1, v2);
		
		assertTrue(cross1.equals(cross2));
		cross1.negate();
		cross2.crossRightHand(v2, v1);
		assertTrue(cross1.equals(cross2)); 
	}

	@Test
	public void length() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		double length = Math.sqrt(v1.getX()*v1.getX() + v1.getY()*v1.getY() + v1.getZ()*v1.getZ());
		
		assertEpsilonEquals(length,v1.length());
	}

	@Test
	public void lengthSquared() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		double lengthSquared = (v1.getX()*v1.getX() + v1.getY()*v1.getY() + v1.getZ()*v1.getZ());
		
		assertEpsilonEquals(lengthSquared,v1.lengthSquared());
	}

	@Test (expected = ArithmeticException.class)
	public void normalizeVector3D() {
		Vector3f vector = new Vector3f(1,2,0);
		Vector3f vector2 = new Vector3f(0,0,0);
		Vector3f vector3 = new Vector3f(-1,1,0);
		
		vector.normalize(vector);
		assertTrue(vector.equals(new Vector3f((int)(1/Math.sqrt(5)),(int)(2/Math.sqrt(5)),0)));
		
		vector.normalize(vector2);
		
		
		vector.normalize(vector3);
		assertTrue(vector.equals(new Vector3f((int)(-1/Math.sqrt(2)),(int)(1/Math.sqrt(2)),0)));	
	}

	@Test (expected = ArithmeticException.class)
	public void normalize() {
		Vector3f vector = new Vector3f(1,2,0);
		Vector3f vector2 = new Vector3f(0,0,0);
		Vector3f vector3 = new Vector3f(-1,1,0);
		
		vector.normalize();
		vector2.normalize();
		vector3.normalize();
		
		assertTrue(vector.equals(new Vector3f(1/Math.sqrt(5),2/Math.sqrt(5),0)));
		assertTrue(vector3.equals(new Vector3f(-1/Math.sqrt(2),1/Math.sqrt(2),0)));
	}

	@Test
	public void turnVectorVector3DDouble() {
		// TODO still remain problem here
		Vector3f vector = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f axis = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		axis.normalize();
		double angle = this.random.nextDouble();
		
		Matrix3f turn = new Matrix3f(
				axis.getX()*axis.getX()+(1-axis.getX()*axis.getX())*Math.cos(angle),
				axis.getX()*axis.getY()*(1-Math.cos(angle))-axis.getZ()*Math.sin(angle),
				axis.getX()*axis.getZ()*(1-Math.cos(angle))+axis.getY()*Math.sin(angle),
				axis.getX()*axis.getY()*(1-Math.cos(angle))+axis.getZ()*Math.sin(angle),
				axis.getY()*axis.getY()+(1-axis.getY()*axis.getY())*Math.cos(angle),
				axis.getY()*axis.getZ()*(1-Math.cos(angle))-axis.getX()*Math.sin(angle),
				axis.getX()*axis.getZ()*(1-Math.cos(angle))-axis.getY()*Math.sin(angle),
				axis.getY()*axis.getZ()*(1-Math.cos(angle))+axis.getX()*Math.sin(angle),
				axis.getZ()*axis.getZ()+(1-axis.getZ()*axis.getZ())*Math.cos(angle));
		
		Vector3f vectorTurned = vector.mul(turn);
		vector.turnVector(axis, angle);
		
		assertEpsilonEquals(vector.length(),vectorTurned.length());
		assertEpsilonEquals(vector.signedAngle(new Vector3f(1,0,0)),vectorTurned.signedAngle(new Vector3f(1,0,0)));
		assertTrue(vector.equals(vectorTurned));
	}

	@Test
	public void addVector3DVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f sum = new Vector3f(v1.getX()+v2.getX(),v1.getY()+v2.getY(),v1.getZ()+v2.getZ());
		
		v1.add(v1, v2);
		assertTrue(sum.equals(v1));
	}

	@Test
	public void addVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f sum = new Vector3f(v1.getX()+v2.getX(),v1.getY()+v2.getY(),v1.getZ()+v2.getZ());
		
		v1.add(v2);
		assertTrue(sum.equals(v1));
	}

	@Test
	public void scaleAddIntVector3DVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		int i = this.random.nextInt();
		
		Vector3f scalarSum = new Vector3f(i*v1.getX()+v2.getX(),i*v1.getY()+v2.getY(),i*v1.getZ()+v2.getZ());
		
		v1.scaleAdd(i,v1, v2);
		assertTrue(scalarSum.equals(v1));
	}

	@Test
	public void scaleAddDoubleVector3DVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		double s = this.random.nextDouble();
		
		Vector3f scalarSum = new Vector3f(s*v1.getX()+v2.getX(),s*v1.getY()+v2.getY(),s*v1.getZ()+v2.getZ());
		
		v1.scaleAdd(s,v1, v2);
		assertTrue(scalarSum.equals(v1));
	}

	@Test
	public void scaleAddIntVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		int i = this.random.nextInt();
		
		Vector3f scalarSum = new Vector3f(i*v1.getX()+v2.getX(),i*v1.getY()+v2.getY(),i*v1.getZ()+v2.getZ());
		
		v1.scaleAdd(i,v2);
		assertTrue(scalarSum.equals(v1));
	}

	@Test
	public void scaleAddDoubleVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		double s = this.random.nextDouble();
		
		Vector3f scalarSum = new Vector3f(s*v1.getX()+v2.getX(),s*v1.getY()+v2.getY(),s*v1.getZ()+v2.getZ());
		
		v1.scaleAdd(s, v2);
		assertTrue(scalarSum.equals(v1));
	}

	@Test
	public void subVector3DVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f sum = new Vector3f(v1.getX()-v2.getX(),v1.getY()-v2.getY(),v1.getZ()-v2.getZ());
		
		v1.sub(v1, v2);
		assertTrue(sum.equals(v1));
	}

	@Test
	public void subPoint3DPoint3D() {
		Point3f v1 = new Point3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Point3f v2 = new Point3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f temp = new Vector3f();
		
		Point3f sum = new Point3f(v1.getX()-v2.getX(),v1.getY()-v2.getY(),v1.getZ()-v2.getZ());
		
		temp.sub(v1, v2);
		assertTrue(sum.equals(temp)); 
	}

	@Test
	public void subVector3D() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		
		Vector3f sum = new Vector3f(v1.getX()-v2.getX(),v1.getY()-v2.getY(),v1.getZ()-v2.getZ());
		
		v1.sub(v2);
		assertTrue(sum.equals(v1));
	}

	@Test
	public void isUnitVector() {
		Vector3f vector = new Vector3f(7.15161,6.7545,-9.1516);
		Vector3f vector2 = new Vector3f(1,1,1);
		
		vector.normalize();
		vector2.setLength(1.);
		
		assertTrue(vector.isUnitVector());
		assertTrue(vector2.isUnitVector());
		assertTrue((new Vector3f(Math.sqrt(2)/2,Math.sqrt(2)/2,0)).isUnitVector());
		assertTrue((new Vector3f(1,0,0)).isUnitVector());
	}

	@Test
	public void isCollinearVectors() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v3 = new Vector3f(0,0,0);
		boolean colinear = (v1.getX()/v2.getX()==v1.getY()/v2.getY()) && (v1.getY()/v2.getY()==v1.getZ()/v2.getZ());
		
		assertTrue(v1.isColinear(v3));
		assertTrue(colinear == v1.isColinear(v2));
		
		
	}

	@Test
	public void setLength() {
		Vector3f vector = new Vector3f(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble());
		Vector3f vector2 = new Vector3f(0,0,0);
		Vector3f oldVector = vector.clone();
		
		double newLength = this.random.nextDouble();
		
		vector.setLength(newLength);
		vector2.setLength(newLength);
		
		assertEpsilonEquals(vector.angle(oldVector), 0);
		assertEpsilonEquals(vector.length()*oldVector.length()/newLength,oldVector.length());
		assertTrue(vector2.equals(new Vector3f(newLength,0,0)));
	}


}