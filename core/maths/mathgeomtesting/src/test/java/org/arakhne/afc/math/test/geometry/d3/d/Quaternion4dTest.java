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

package org.arakhne.afc.math.test.geometry.d3.d;

import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.test.AbstractMathTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings("all")
public class Quaternion4dTest extends AbstractMathTestCase {

	@DisplayName("clone")
	@Test
	public void testClone() {
		Quaternion4d q1 = new Quaternion4d(getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble(), getRandom().nextDouble());
		Quaternion4d q2 = q1.clone();
		assertEpsilonEquals(q1, q2);
	}

	@Test
	public void getX() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);

		assertEpsilonEquals(a, q1.getX() * Math.sqrt(a * a + b * b + c * c + d * d));
	}

	@DisplayName("setX(double)")
	@Test
	public void setX() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d); 

		double set = getRandom().nextDouble();

		q1.setX(set);

		assertEpsilonEquals(set,q1.getX());
	}

	@Test
	public void getY() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);

		assertEpsilonEquals(b,q1.getY()*Math.sqrt(a*a+b*b+c*c+d*d));
	}

	@DisplayName("setY(double)")
	@Test
	public void setY() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d); 

		double set = getRandom().nextDouble();

		q1.setY(set);

		assertEpsilonEquals(set,q1.getY());
	}

	@Test
	public void getZ() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);

		assertEpsilonEquals(c,q1.getZ()*Math.sqrt(a*a+b*b+c*c+d*d));
	}

	@DisplayName("setZ(double)")
	@Test
	public void setZ() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d); 

		double set = getRandom().nextDouble();

		q1.setZ(set);

		assertEpsilonEquals(set,q1.getZ());
	}

	@Test
	public void getW() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);

		assertEpsilonEquals(d,q1.getW()*Math.sqrt(a*a+b*b+c*c+d*d));
	}

	@DisplayName("setW(double)")
	@Test
	public void setW() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d); 

		double set = getRandom().nextDouble();

		q1.setW(set);

		assertEpsilonEquals(set,q1.getW());
	}

	@DisplayName("set(double,double,double,double)")
	@Test
	public void setDoubleDoubleDoubleDouble() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q = new Quaternion4d(a,b,c,d); 

		double x = getRandom().nextDouble();
		double y = getRandom().nextDouble();
		double z = getRandom().nextDouble();
		double w = getRandom().nextDouble();

		q.set(x, y, z, w);

		assertEpsilonEquals(x, q.getX());
		assertEpsilonEquals(y, q.getY());
		assertEpsilonEquals(z, q.getZ());
		assertEpsilonEquals(w, q.getW());
	}

	@DisplayName("set(Quaternion4d)")
	@Test
	public void setQuaternion4d() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q = new Quaternion4d(a,b,c,d); 

		double x = getRandom().nextDouble();
		double y = getRandom().nextDouble();
		double z = getRandom().nextDouble();
		double w = getRandom().nextDouble();

		q.set(new Quaternion4d(x, y, z, w));

		assertEpsilonEquals(new Quaternion4d(x, y, z, w), q);
	}

	@DisplayName("equals(Quaternion4d)")
	@Test
	public void equalsQuaternion4d() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);
		Quaternion4d q2 = new Quaternion4d(q1);

		assertTrue(q1.equals(q2));

		q2.set(b,a,d,c);

		assertFalse(q1.equals(q2));
	}

	@DisplayName("epsilonEquals(Quaternion4d)")
	@Test
	public void epsilonEquals() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);
		Quaternion4d q2 = new Quaternion4d(q1);

		assertTrue(q1.epsilonEquals(q2, EPSILON));

		q2.set(b,a,d,c);

		assertFalse(q1.epsilonEquals(q2, EPSILON));
	}

	@DisplayName("normalize(Quaternion4d)")
	@Test
	public void normalizeQuaternion4d() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d();

		q1.setW(a * 50.);
		q1.setX(b * 50.);
		q1.setY(c * 50.);
		q1.setZ(d * 50.);

		Quaternion4d q2 = new Quaternion4d(b,c,d,a);
		Quaternion4d q3 = new Quaternion4d();

		q3.normalize(q1);

		assertEpsilonEquals(q2, q3);
	}

	@DisplayName("normalize")
	@Test
	public void normalize() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d();

		q1.setW(a * 50.);
		q1.setX(b * 50.);
		q1.setY(c * 50.);
		q1.setZ(d * 50.);

		Quaternion4d q2 = new Quaternion4d(b,c,d,a);

		q1.normalize();

		assertEpsilonEquals(q2, q1);
	}

	@DisplayName("inverse(Quaternion4d)")
	@Test
	public void inverseQuaternion4d() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);

		Quaternion4d expected = new Quaternion4d(-q1.getX(), -q1.getY(), -q1.getZ(), q1.getW());

		Quaternion4d inv = new Quaternion4d();
		inv.inverse(q1);

		assertEpsilonEquals(expected, inv);
	}

	@DisplayName("inverse")
	@Test
	public void inverse() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);

		Quaternion4d expected = new Quaternion4d(-q1.getX(), -q1.getY(), -q1.getZ(), q1.getW());

		Quaternion4d inv = new Quaternion4d(q1);
		inv.inverse();

		assertEpsilonEquals(expected, inv);
	}

	@DisplayName("conjugate(Quaternion4d)")
	@Test
	public void conjugateQuaternion4d() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);

		Quaternion4d expected = new Quaternion4d(-q1.getX(), -q1.getY(), -q1.getZ(), q1.getW());

		Quaternion4d inv = new Quaternion4d();
		inv.conjugate(q1);

		assertEpsilonEquals(expected, inv);
	}

	@DisplayName("conjugate")
	@Test
	public void conjugate() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);

		Quaternion4d expected = new Quaternion4d(-q1.getX(), -q1.getY(), -q1.getZ(), q1.getW());

		Quaternion4d inv = new Quaternion4d(q1);
		inv.conjugate();

		assertEpsilonEquals(expected, inv);
	}

	@DisplayName("mul(Quaternion4d,Quaternion4d)")
	@Test
	public void mulQuaternion4dQuaternion4d() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		double aP = getRandom().nextDouble();
		double bP = getRandom().nextDouble();
		double cP = getRandom().nextDouble();
		double dP = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);
		Quaternion4d q2 = new Quaternion4d(aP,bP,cP,dP);

		Quaternion4d mul = new Quaternion4d();

		mul.setW(q1.getW()*q2.getW()-q1.getX()*q2.getX()-q1.getY()*q2.getY()-q1.getZ()*q2.getZ());
		mul.setX(q1.getW()*q2.getX()+q1.getX()*q2.getW()+q1.getY()*q2.getZ()-q1.getZ()*q2.getY());
		mul.setY(q1.getW()*q2.getY()+q1.getY()*q2.getW()-q1.getX()*q2.getZ()+q1.getZ()*q2.getX());
		mul.setZ(q1.getZ()*q2.getW()+q1.getW()*q2.getZ()+q1.getX()*q2.getY()-q1.getY()*q2.getX());

		q1.mul(q1,q2);
		assertEpsilonEquals(mul, q1);
	}

	@DisplayName("mul(Quaternion4d)")
	@Test
	public void mulQuaternion4d() {
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		double aP = getRandom().nextDouble();
		double bP = getRandom().nextDouble();
		double cP = getRandom().nextDouble();
		double dP = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);
		Quaternion4d q2 = new Quaternion4d(aP,bP,cP,dP);

		Quaternion4d mul = new Quaternion4d();

		mul.setW(q1.getW()*q2.getW()-q1.getX()*q2.getX()-q1.getY()*q2.getY()-q1.getZ()*q2.getZ());
		mul.setX(q1.getW()*q2.getX()+q1.getX()*q2.getW()+q1.getY()*q2.getZ()-q1.getZ()*q2.getY());
		mul.setY(q1.getW()*q2.getY()+q1.getY()*q2.getW()-q1.getX()*q2.getZ()+q1.getZ()*q2.getX());
		mul.setZ(q1.getZ()*q2.getW()+q1.getW()*q2.getZ()+q1.getX()*q2.getY()-q1.getY()*q2.getX());

		q1.mul(q2);
		assertEpsilonEquals(mul, q1);
	}  

	@DisplayName("mulInverse(Quaternion4d,Quaternion4d)")
	@Test
	public void mulInverseQuaternion4dQuaternion4d() {   
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		double aP = getRandom().nextDouble();
		double bP = getRandom().nextDouble();
		double cP = getRandom().nextDouble();
		double dP = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);
		Quaternion4d q2 = new Quaternion4d(aP,bP,cP,dP);
		Quaternion4d mulInv = new Quaternion4d();

		mulInv.mulInverse(q1, q2);

		q2.inverse();
		q1.mul(q2);

		assertEpsilonEquals(q1, mulInv);
	}

	@DisplayName("mulInverse(Quaternion4d)")
	@Test
	public void mulInverseQuaternion() {  
		double a = getRandom().nextDouble();
		double b = getRandom().nextDouble();
		double c = getRandom().nextDouble();
		double d = getRandom().nextDouble();
		double aP = getRandom().nextDouble();
		double bP = getRandom().nextDouble();
		double cP = getRandom().nextDouble();
		double dP = getRandom().nextDouble();
		Quaternion4d q1 = new Quaternion4d(a,b,c,d);
		Quaternion4d q2 = new Quaternion4d(aP,bP,cP,dP);
		Quaternion4d cloneQ1 = q1.clone();

		q1.mulInverse(q2);

		q2.inverse();
		cloneQ1.mul(q2);

		assertEpsilonEquals(q1, cloneQ1);
	}

}