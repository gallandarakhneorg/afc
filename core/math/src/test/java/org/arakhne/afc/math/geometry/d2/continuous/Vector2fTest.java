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
package org.arakhne.afc.math.geometry.d2.continuous;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import static org.arakhne.afc.math.MathConstants.PI;

import org.arakhne.afc.math.geometry.d2.FunctionalVector2D;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Vector2fTest extends AbstractMathTestCase {

	@Test
	public void testClone() {
		Vector2f r = new Vector2f(this.random.nextDouble(),this.random.nextDouble());
		Vector2f b = r.clone();
		
		assertNotSame(b, r);
		assertEpsilonEquals(r.getX(), b.getX());
		assertEpsilonEquals(r.getY(), b.getY());
		
		
		b.set(r.getX()+1f, r.getY()+1f);

		assertNotEpsilonEquals(r.getX(), b.getX());
		assertNotEpsilonEquals(r.getY(), b.getY());
	}

	@Test
	public void angleVector2D() {
		Vector2f vector = new Vector2f(1,2);
		Vector2f vector2 = new Vector2f(-2,1);
		Vector2f vector3 = new Vector2f(1,1);
		Vector2f vector4 = new Vector2f(1,0);
		
		assertEpsilonEquals(PI/2f,vector.angle(vector2));
		assertEpsilonEquals(PI/4f,vector4.angle(vector3));
		assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),vector4.angle(vector));
		assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),vector4.angle(vector2));
		assertEpsilonEquals(0,vector.angle(vector));
	}

	@Test
	public void dotVector2D() {
		Vector2f vector = new Vector2f(1,2);
		Vector2f vector2 = new Vector2f(3,4);
		Vector2f vector3 = new Vector2f(1,-2);
		
		assertEpsilonEquals(5,vector.dot(vector));
		assertEpsilonEquals(11,vector.dot(vector2));
		assertEpsilonEquals(-3,vector.dot(vector3));
	}

	@Test
	public void perpVector2D() {
		Vector2f vector = new Vector2f(1,2);
		Vector2f vector2 = new Vector2f(3,4);
		Vector2f vector3 = new Vector2f(1,-2);
		
		assertEpsilonEquals(0,vector.perp(vector));
		assertEpsilonEquals(-2,vector.perp(vector2));
		assertEpsilonEquals(-4,vector.perp(vector3));
	}

	@Test
	public void mulMatrix2f() {
		Vector2f vector = new Vector2f(0,0);
		Vector2f vector2 = new Vector2f(1,1);
		Vector2f vector3 = new Vector2f(1,-1);
		
		Matrix2f matrix = new Matrix2f();
		Matrix2f matrix2 = new Matrix2f(0.5,3,2,0);
		
		assertTrue(vector2.mul(matrix).equals(new Vector2f(0,0)));
		assertTrue(vector.mul(matrix2).equals(new Vector2f(0,0)));
		assertTrue(vector2.mul(matrix2).equals(new Vector2f(3.5,2)));
		assertTrue(vector3.mul(matrix2).equals(new Vector2f(-2.5,2)));
		
		matrix.setIdentity();
		assertTrue(vector2.mul(matrix).equals(new Vector2f(1,1)));
		
	}

	@Test
	public void perpendicularize() {
		Vector2f vector = new Vector2f(1,2);
		Vector2f vector2 = new Vector2f(0,0);
		Vector2f vector3 = new Vector2f(1,1);
		
		vector.perpendicularize();
		vector2.perpendicularize();
		vector3.perpendicularize();
		
		assertTrue(vector.equals(new Vector2f(-2,1)));
		assertFalse(vector.equals(new Vector2f(2,-1)));
		assertTrue(vector2.equals(new Vector2f(0,0)));
		assertTrue(vector3.equals(new Vector2f(-1,1)));
		
	}

	@Test
	public void length() {
		Vector2f vector = new Vector2f(1,2);
		Vector2f vector2 = new Vector2f(0,0);
		Vector2f vector3 = new Vector2f(-1,1);
		
		assertEpsilonEquals(Math.sqrt(5),vector.length());
		assertEpsilonEquals(0,vector2.length());
		assertEpsilonEquals(Math.sqrt(2),vector3.length());
	}

	@Test
	public void lengthSquared() {
		Vector2f vector = new Vector2f(1,2);
		Vector2f vector2 = new Vector2f(0,0);
		Vector2f vector3 = new Vector2f(Math.sqrt(2)/2,Math.sqrt(2)/2);
		
		assertEpsilonEquals(5,vector.lengthSquared());
		assertEpsilonEquals(0,vector2.lengthSquared());
		assertEpsilonEquals(1,vector3.lengthSquared());
	}

	@Test (expected = ArithmeticException.class)
	public void normalizeVector2D() {
		Vector2f vector = new Vector2f(1,2);
		Vector2f vector2 = new Vector2f(0,0);
		Vector2f vector3 = new Vector2f(-1,1);
		
		vector.normalize(vector);
		assertTrue(vector.equals(new Vector2f((int)(1/Math.sqrt(5)),(int)(2/Math.sqrt(5)))));
		
		vector.normalize(vector2);
		
		
		vector.normalize(vector3);
		assertTrue(vector.equals(new Vector2f((int)(-1/Math.sqrt(2)),(int)(1/Math.sqrt(2)))));	
	}

	@Test (expected = ArithmeticException.class)
	public void normalize() {
		Vector2f vector = new Vector2f(1,2);
		Vector2f vector2 = new Vector2f(0,0);
		Vector2f vector3 = new Vector2f(-1,1);
		
		vector.normalize();
		vector2.normalize();
		vector3.normalize();
		
		assertTrue(vector.equals(new Vector2f(1/Math.sqrt(5),2/Math.sqrt(5))));
		assertTrue(vector3.equals(new Vector2f(-1/Math.sqrt(2),1/Math.sqrt(2))));
	}

	@Test
	public void signedAngleVector2D() {
		Vector2f v1 = new Vector2f(this.random.nextDouble(), this.random.nextDouble());
		Vector2f v2 = new Vector2f(this.random.nextDouble(), this.random.nextDouble());

		assertEpsilonEquals(
				0.f,
				v1.signedAngle(v1));
		assertEpsilonEquals(
				0.f,
				v2.signedAngle(v2));

		double sAngle1 = v1.signedAngle(v2);
		double sAngle2 = v2.signedAngle(v1);

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	@Test
	
	public void turnVectorDouble() {
		Vector2f vector = new Vector2f(this.random.nextDouble(), this.random.nextDouble());
		Vector2f vector2 = vector.clone();;
		
		double angle = this.random.nextDouble();
		vector.turnVector(angle);
		
		assertEpsilonEquals(angle,vector2.angle(vector)); 
	}

	@Test
	public void toOrientationVector() {
		Vector2f vector = new Vector2f(this.random.nextDouble(), this.random.nextDouble());
		Vector2f base1 = new Vector2f(1,0);
		double angle = this.random.nextDouble();
		double length = vector.length();
		
		vector = FunctionalVector2D.toOrientationVector(angle);
		
		assertEpsilonEquals(1,vector.length());
		assertEpsilonEquals(Math.cos(angle),Math.cos(base1.signedAngle(vector)));
		assertEpsilonEquals(Math.sin(angle),Math.sin(base1.signedAngle(vector)));
	}
	
	@Test
	public void getOrientationAngle() {
		Vector2f vector = new Vector2f();
		Vector2f base1 = new Vector2f(1,0);
		double angle = this.random.nextDouble();
		double angle2;
		
		vector = FunctionalVector2D.toOrientationVector(angle);
		
		angle2 = vector.getOrientationAngle();
		
		assertEpsilonEquals(Math.cos(angle),Math.cos(angle2));
		assertEpsilonEquals(Math.sin(angle),Math.sin(angle2));
	}

	@Test
	public void addVector2DVector2D() {
		Vector2f vector = new Vector2f(0,0);
		Vector2f vector2 = new Vector2f(-1,0);
		Vector2f vector3 = new Vector2f(1.2,1.2);
		Vector2f vector4 = new Vector2f(2.0,1.5);
		Vector2f vector5 = new Vector2f(0.0,0.0);
		
		vector5.add(vector3,vector);
		assertTrue(vector5.equals(new Vector2f(1.2,1.2)));
		
		vector5.add(vector4,vector2);
		assertTrue(vector5.equals(new Vector2f(1.0,1.5))); 
	}

	@Test
	public void addVector2D() {
		Vector2f vector = new Vector2f(0,0);
		Vector2f vector2 = new Vector2f(-1,0);
		Vector2f vector3 = new Vector2f(1.2,1.2);
		Vector2f vector4 = new Vector2f(2.0,1.5);
		
		vector.add(vector3);
		assertTrue(vector.equals(new Vector2f(1.2,1.2)));
		
		vector2.add(vector4);
		assertTrue(vector2.equals(new Vector2f(1.0,1.5)));
	}

	@Test
	public void scaleAddIntVector2DVector2D() {
		Vector2f vector = new Vector2f(-1,0);
		Vector2f vector2 = new Vector2f(1.0,1.2);
		Vector2f vector3 = new Vector2f(0.0,0.0);
		
		vector3.scaleAdd(0,vector2,vector);
		assertTrue(vector3.equals(new Vector2f(-1,0)));
		
		vector3.scaleAdd(1,vector2,vector);
		assertTrue(vector3.equals(new Vector2f(0.0,1.2)));
		
		vector3.scaleAdd(-1,vector2,vector);
		assertTrue(vector3.equals(new Vector2f(-2.0,-1.2)));
		
		vector3.scaleAdd(10,vector2,vector);
		assertTrue(vector3.equals(new Vector2f(9,12)));
	}

	@Test
	public void scaleAddDoubleVector2DVector2D() {
		Vector2f point = new Vector2f(1,0);
		Vector2f vector = new Vector2f(-1,1);
		Vector2f newPoint = new Vector2f(0.0,0.0);
		
		newPoint.scaleAdd(0.0,vector,point);
		assertTrue(newPoint.equals(new Vector2f(1,0)));
		
		newPoint.scaleAdd(1.5,vector,point);
		assertTrue(newPoint.equals(new Vector2f(-0.5,1.5)));
		
		newPoint.scaleAdd(-1.5,vector,point);
		assertTrue(newPoint.equals(new Vector2f(2.5,-1.5)));
		
		newPoint.scaleAdd(0.1,vector,point);
		assertTrue(newPoint.equals(new Vector2f(0.9,0.1)));
	}

	@Test
	public void scaleAddIntVector2D() {
		Vector2f vector = new Vector2f(1,0);
		Vector2f newPoint = new Vector2f(0,0);
		
		newPoint.scaleAdd(0,vector);
		assertTrue(newPoint.equals(new Vector2f(1,0)));
		
		newPoint.scaleAdd(1,vector);
		assertTrue(newPoint.equals(new Vector2f(2,0)));
		
		newPoint.scaleAdd(-10,vector);
		assertTrue(newPoint.equals(new Vector2f(-19,0)));
	}

	@Test
	public void scaleAddDoubleVector2D() {
		Vector2f vector = new Vector2f(1,0);
		Vector2f newPoint = new Vector2f(0.0,0.0);
		
		newPoint.scaleAdd(0.5,vector);
		assertTrue(newPoint.equals(new Vector2f(1,0)));
		
		newPoint.scaleAdd(1.2,vector);
		assertTrue(newPoint.equals(new Vector2f(2.2,0.0)));
		
		newPoint.scaleAdd(-10,vector);
		assertTrue(newPoint.equals(new Vector2f(-21,0)));
	}

	@Test
	public void subVector2DVector2D() {
		Vector2f point = new Vector2f(0,0);
		Vector2f point2 = new Vector2f(1,0);
		Vector2f vector = new Vector2f(-1.2,-1.2);
		Vector2f vector2 = new Vector2f(2.0,1.5);
		Vector2f newPoint = new Vector2f(0.0,0.0);
		
		newPoint.sub(point,vector);
		assertTrue(newPoint.equals(new Vector2f(1.2,1.2)));
		
		newPoint.sub(point2,vector2);
		assertTrue(newPoint.equals(new Vector2f(-1.0,-1.5))); 
	}

	@Test
	public void subPoint2DPoint2D() {
		Point2f point = new Point2f(0,0);
		Point2f point2 = new Point2f(1,0);
		Point2f vector = new Point2f(-1.2,-1.2);
		Point2f vector2 = new Point2f(2.0,1.5);
		Vector2f newPoint = new Vector2f(0.0,0.0);
		
		newPoint.sub(point,vector);
		assertTrue(newPoint.equals(new Vector2f(1.2,1.2)));
		
		newPoint.sub(point2,vector2);
		assertTrue(newPoint.equals(new Vector2f(-1.0,-1.5))); 
	}
	
	@Test
	public void subVector2D() {
		Vector2f point = new Vector2f(0,0);
		Vector2f point2 = new Vector2f(-1,0);
		Vector2f vector = new Vector2f(-1.2,-1.2);
		Vector2f vector2 = new Vector2f(-2.0,-1.5);
		Vector2f newPoint = new Vector2f(0.0,0.0);
		
		point.sub(vector);
		assertTrue(point.equals(new Vector2f(1.2,1.2)));
		
		point2.sub(vector2);
		assertTrue(point2.equals(new Vector2f(1.0,1.5)));
	}

	@Test
	public void setLength() {
		Vector2f vector = new Vector2f(this.random.nextDouble(), this.random.nextDouble());
		Vector2f vector2 = new Vector2f(0,0);
		Vector2f oldVector = vector.clone();
		
		double newLength = this.random.nextDouble();
		
		vector.setLength(newLength);
		vector2.setLength(newLength);
		
		assertEpsilonEquals(vector.angle(oldVector), 0);
		assertEpsilonEquals(vector.length()*oldVector.length()/newLength,oldVector.length());
		assertTrue(vector2.equals(new Vector2f(newLength,0)));
	}

	@Test
	public void isUnitVector() {
		// TODO still remain problem here
		Vector2f vector = new Vector2f(7.15161,6.7545);
		Vector2f vector2 = new Vector2f(1,1);
		
		vector.normalize();
		vector2.setLength(1.);
		
		assertTrue(vector.isUnitVector());
		assertTrue(vector2.isUnitVector());
		assertTrue((new Vector2f(Math.sqrt(2)/2,Math.sqrt(2)/2)).isUnitVector());
		assertTrue((new Vector2f(1,0)).isUnitVector());
		
	}

	@Test
	public void angleOfVectorDoubleDoubleDoubleDouble() {
		
		
		double x1 = this.random.nextDouble();
		double y1 = this.random.nextDouble();
		double x2 = this.random.nextDouble();
		double y2 = this.random.nextDouble();
		
		Vector2f vector = new Vector2f(x2-x1,y2-y1);
		
		assertEpsilonEquals((new Vector2f(1,0)).signedAngle(vector),FunctionalVector2D.angleOfVector(x1, y1, x2, y2));
		
	}

	@Test
	public void angleOfVectorDoubleDouble() {
		Vector2f temp = new Vector2f();
		
		assertEpsilonEquals(Math.acos(1/Math.sqrt(5)),FunctionalVector2D.angleOfVector(1,2));
		assertEpsilonEquals(PI/2f+Math.acos(1/Math.sqrt(5)),FunctionalVector2D.angleOfVector(-2,1));
		assertEpsilonEquals(PI/4f,FunctionalVector2D.angleOfVector(1,1));
		
	}

	@Test
	public void signedAngleDoubleDoubleDoubleDouble() {
		Vector2f v1 = new Vector2f(this.random.nextDouble(), this.random.nextDouble());
		Vector2f v2 = new Vector2f(this.random.nextDouble(), this.random.nextDouble());

		assertEpsilonEquals(
				0.f,
				FunctionalVector2D.signedAngle(v1.getX(), v1.getY(), v1.getX(),
						v1.getY()));
		assertEpsilonEquals(
				0.f,
				FunctionalVector2D.signedAngle(v2.getX(), v2.getY(), v2.getX(),
						v2.getY()));

		double sAngle1 = FunctionalVector2D.signedAngle(v1.getX(), v1.getY(),
				v2.getX(), v2.getY());
		double sAngle2 = FunctionalVector2D.signedAngle(v2.getX(), v2.getY(),
				v1.getX(), v1.getY());

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

		double sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	@Test
	public void perpProductDoubleDoubleDoubleDouble() {
		Vector2f vector = new Vector2f();
		
		assertEpsilonEquals(0,FunctionalVector2D.perpProduct(1, 2, 1, 2));
		assertEpsilonEquals(-2,FunctionalVector2D.perpProduct(1, 2, 3, 4));
		assertEpsilonEquals(-4,FunctionalVector2D.perpProduct(1, 2, 1, -2));	
	}

	@Test
	public void dotProductDoubleDoubleDoubleDouble() {
Vector2f vector = new Vector2f();
		
		assertEpsilonEquals(5,FunctionalVector2D.dotProduct(1, 2, 1, 2));
		assertEpsilonEquals(11,FunctionalVector2D.dotProduct(1, 2, 3, 4));
		assertEpsilonEquals(-3,FunctionalVector2D.dotProduct(1, 2, 1, -2));
	}

}