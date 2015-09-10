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
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.junit.Test;

/**
 * @author $Author: galland$
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
		
		Vector3f product = new Vector3f(vector.getX()+2*vector.getY()+4*vector.getZ(),5*vector.getX()+vector.getY()+3*vector.getZ(),9*vector.getX()-2*vector.getY()+vector.getZ());
		
		assertTrue(product.equals(vector.mul(matrix)));
	}

	@Test
	public void determinantDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void perpVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void signedAngleDoubleDoubleDoubleDoubleDoubleDouble() {
		Vector3f v1 = new Vector3f(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble());
		Vector3f v2 = new Vector3f(this.random.nextDouble(), this.random.nextDouble(), this.random.nextDouble());

		assertEpsilonEquals(
				0.f,
				v1.signedAngle(v1.getX(),v1.getY(),v1.getZ(),v1.getX(),v1.getY(),v1.getZ()));
		assertEpsilonEquals(
				0.f,
				v2.signedAngle(v2.getX(),v2.getY(),v2.getZ(),v2.getX(),v2.getY(),v2.getZ()));

		double sAngle1 = v1.signedAngle(v1.getX(),v1.getY(),v1.getZ(),v2.getX(),v2.getY(),v2.getZ());
		double sAngle2 = v2.signedAngle(v2.getX(),v2.getY(),v2.getZ(),v1.getX(),v1.getY(),v1.getZ());

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

		// redéfinir SIN !!! 
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
		throw new UnsupportedOperationException();
	}

	@Test
	public void crossLeftHandVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void crossLeftHandVector3DVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void crossRightHandVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void crossRightHandVector3DVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void length() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void lengthSquared() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void normalizeVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void normalize() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void turnVectorVector3DDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void addVector3DVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void addVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddIntVector3DVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddDoubleVector3DVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddIntVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddDoubleVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void subVector3DVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void subPoint3DPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void subVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void isUnitVector() {
		// TODO still remain problem here
		Vector3f Test[] = { new Vector3f(-0.72, -0.38, 1.83),
				new Vector3f(0.75, -0.32, 1.83),
				new Vector3f(0.01, 0.81, 1.83),
				new Vector3f(0.18, -0.79, 1.83),
				new Vector3f(-0.76, 0.28, 1.83),
				new Vector3f(-0.81, -0.03, 1.83),
				new Vector3f(-0.1, 0.81, 1.83),
				new Vector3f(-0.6, -0.54, 1.83),
				new Vector3f(-0.65, 0.49, 1.83),
				new Vector3f(-0.49, -0.65, 1.83),
				new Vector3f(0.25, 0.77, 1.83), new Vector3f(-0.14, 0.8, 1.83),
				new Vector3f(0.65, -0.49, 1.83), new Vector3f(0.8, 0.16, 1.83),
				new Vector3f(0.8, 0.16, 1.83),
				new Vector3f(0.36, -1.44, -1.34),
				new Vector3f(0.25, 0.13, -1.98),
				new Vector3f(1.81, -0.52, -0.68),
				new Vector3f(0.57, 0.53, 1.84),
				new Vector3f(1.83, -0.22, 0.78),
				new Vector3f(1.16, 0.42, -1.58),
				new Vector3f(-1.83, -0.63, 0.48),
				new Vector3f(0.06, 0.24, -1.99),
				new Vector3f(-1.73, 0.59, -0.81),
				new Vector3f(-0.15, 1.69, 1.06),
				new Vector3f(-1.57, -1.14, 0.48),
				new Vector3f(-0.05, 0.91, -1.78),
				new Vector3f(1.86, -0.71, 0.14), new Vector3f(0.11, 0.08, 2),
				new Vector3f(-0.4, -0.11, 1.96),
				new Vector3f(1.08, -1.66, 0.31),
				new Vector3f(1.89, 0.61, 0.21),
				new Vector3f(-0.23, -1.09, 1.66),
				new Vector3f(1.77, -0.41, 0.85),
				new Vector3f(0.73, 1.72, -0.72),
				new Vector3f(-1.19, 0.61, 1.49),
				new Vector3f(-0.22, 0.11, 1.99),
				new Vector3f(-0.55, -1.68, 0.94),
				new Vector3f(-0.05, -1.36, 1.46),
				new Vector3f(0.88, -0.53, -1.71),
				new Vector3f(1.73, -0.66, 0.75),
				new Vector3f(-0.83, 1.44, -1.12),
				new Vector3f(-1.5, 1.22, 0.52),
				new Vector3f(0.31, -0.37, -1.94),
				new Vector3f(-1.27, -0.18, -1.53),
				new Vector3f(-0.23, 0.42, 1.94),
				new Vector3f(1.22, 0.17, -1.58),
				new Vector3f(1.38, -1.29, 0.65),
				new Vector3f(-0.93, -1.68, -0.55),
				new Vector3f(-1.81, 0.7, -0.48),
				new Vector3f(0.1, -1.41, 1.41),
				new Vector3f(0.82, -0.57, -1.73),
				new Vector3f(1.27, -0.77, 1.34),
				new Vector3f(-0.37, -0.18, -1.96),
				new Vector3f(-1.65, 0.12, -1.12),
				new Vector3f(0.1, -0.22, 1.99),
				new Vector3f(-0.89, 0.52, -1.71),
				new Vector3f(0.33, -1.88, 0.58),
				new Vector3f(-0.34, -0.07, -1.97),
				new Vector3f(-1.87, 0.33, 0.62), new Vector3f(0.11, -0.08, 2),
				new Vector3f(-1.62, -1.01, -0.58),
				new Vector3f(-0.41, -0.42, -1.91),
				new Vector3f(-0.67, 0.7, -1.75),
				new Vector3f(-1.25, -0.72, -1.39),
				new Vector3f(-0.3, 0.92, 1.75),
				new Vector3f(0.71, -0.12, -1.87),
				new Vector3f(-1.02, 1.7, 0.28),
				new Vector3f(-0.13, -0.29, 1.98),
				new Vector3f(-1.81, 0.73, -0.45),
				new Vector3f(0.15, 0.08, -1.99),
				new Vector3f(0.78, -0.08, -1.84),
				new Vector3f(-0.91, -1.64, 0.68),
				new Vector3f(-1.64, -0.56, -1),
				new Vector3f(-0.43, -1.62, 1.09),
				new Vector3f(0.29, 1.25, -1.53),
				new Vector3f(0.47, -1.88, -0.48),
				new Vector3f(1.91, 0.58, -0.14),
				new Vector3f(-1.9, -0.2, 0.58),
				new Vector3f(-0.44, -1.21, -1.53),
				new Vector3f(-1.1, -0.49, -1.6),
				new Vector3f(-0.92, -1.42, -1.06),
				new Vector3f(0.12, -0.25, -1.98),
				new Vector3f(0.97, -0.84, -1.53),
				new Vector3f(0.94, -1.69, 0.52),
				new Vector3f(-0.24, 0.3, -1.96),
				new Vector3f(-1.91, -0.27, -0.52),
				new Vector3f(0.34, 1.18, -1.58),
				new Vector3f(-0.67, 0.4, 1.84), new Vector3f(0.14, 0.87, -1.8),
				new Vector3f(0.49, 0.48, 1.88),
				new Vector3f(-0.19, 0.33, 1.96),
				new Vector3f(-1.51, -1.23, 0.45),
				new Vector3f(0.27, 0.22, -1.97),
				new Vector3f(-1.22, 0.85, -1.34),
				new Vector3f(0.17, 0.66, 1.88),
				new Vector3f(-0.3, -0.09, -1.98),
				new Vector3f(-0.41, -0.42, -1.91),
				new Vector3f(1.35, 1.44, 0.31),
				new Vector3f(1.18, -1.14, 1.15),
				new Vector3f(-0.89, -0.16, 1.78),
				new Vector3f(-0.18, 1.76, 0.94),
				new Vector3f(1.53, -0.38, 1.23),
				new Vector3f(0.94, 0.03, -1.77),
				new Vector3f(-1.95, -0.45, -0.1),
				new Vector3f(-0.25, -0.77, -1.83),
				new Vector3f(0.23, -1.29, 1.51),
				new Vector3f(0.23, -1.29, 1.51),
				new Vector3f(1.07, -0.75, 1.51),
				new Vector3f(0.79, 1.05, 1.51),
				new Vector3f(-0.99, 0.86, 1.51),
				new Vector3f(0.93, -0.93, 1.51),
				new Vector3f(-1.17, -0.6, 1.51),
				new Vector3f(1.02, -0.83, 1.51),
				new Vector3f(0.02, 1.31, 1.51),
				new Vector3f(-1.19, 0.55, 1.51),
				new Vector3f(-0.81, 1.03, 1.51),
				new Vector3f(1.16, -0.62, 1.51),
				new Vector3f(-0.47, 1.22, 1.51),
				new Vector3f(-0.47, 1.22, 1.51) };
		for (int i = 0; i < Test.length; i++) {
			assertFalse("True " + i, new Vector3f(Test[i].getX(),
					Test[i].getY(), Test[i].getZ()).isUnitVector());
			double d = Test[i].getX() * Test[i].getX() + Test[i].getY()
					* Test[i].getY() + Test[i].getZ() * Test[i].getZ();
			Test[i].normalize();
			assertTrue(
					"False i : " + i + "Test :" + Test[i].toString() + "d : "
							+ d,
							new Vector3f(Test[i].getX() / d,
									Test[i].getY() / d, Test[i].getZ() / d).isUnitVector());
		}
	}

	@Test
	public void isCollinearVectorsDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void setLength() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void crossProductLeftHandVector3DVector3DVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void crossProductRightHandVector3DVector3DVector3D() {
		throw new UnsupportedOperationException();
	}

}