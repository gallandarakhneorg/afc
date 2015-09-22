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

import static org.junit.Assert.*;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d3.FunctionalPoint3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Point3fTest extends AbstractMathTestCase {
	
	@Test 
	public void testClone() {
		Point3f point = this.randomPoint3f();
		Point3f pointClone = point.clone();
		
		assertTrue(point.equals(pointClone));
	}

	@Test
	public void distanceSquaredPoint3D() {
		int tab[] = {1,2,3};
		Point3f point = new Point3f(1.0,2.0,3.0);
		Point3f point2 = new Point3f(tab);
		Point3f point3 = new Point3f(0,0,0);
		Point3f point4 = new Point3f(-1,0,1);
		
		assertEpsilonEquals(14,point.distanceSquared(point3));
		assertEpsilonEquals(0,point.distanceSquared(point2));
		assertEpsilonEquals(0,point2.distanceSquared(point));
		assertEpsilonEquals(12,point.distanceSquared(point4));
	}

	@Test
	public void distancePoint3D() {
		int tab[] = {1,2,3};
		Point3f point = new Point3f(1.0,2.0,3.0);
		Point3f point2 = new Point3f(tab);
		Point3f point3 = new Point3f(0,0,0);
		Point3f point4 = new Point3f(-1,0,1);
		
		assertEpsilonEquals(3,point.distance(point3));
		assertEpsilonEquals(0,point.distance(point2));
		assertEpsilonEquals(0,point2.distance(point));
		assertEpsilonEquals(3,point.distance(point4));
	}

	@Test
	public void distanceL1Point3D() {
		int tab[] = {1,2,3};
		Point3f point = new Point3f(1.0,2.0,3.0);
		Point3f point2 = new Point3f(tab);
		Point3f point3 = new Point3f(0,0,0);
		Point3f point4 = new Point3f(-1,0,1);
		
		assertEpsilonEquals(6,point.distanceL1(point3));
		assertEpsilonEquals(0,point.distanceL1(point2));
		assertEpsilonEquals(0,point2.distanceL1(point));
		assertEpsilonEquals(6,point.distanceL1(point4));
	}

	@Test
	public void distanceLinfPoint3D() {
		int tab[] = {1,2,3};
		Point3f point = new Point3f(1.0,2.0,3.0);
		Point3f point2 = new Point3f(tab);
		Point3f point3 = new Point3f(0,0,0);
		Point3f point4 = new Point3f(-1,0,1);
		
		assertEpsilonEquals(3,point.distanceLinf(point3));
		assertEpsilonEquals(0,point.distanceLinf(point2));
		assertEpsilonEquals(0,point2.distanceLinf(point));
		assertEpsilonEquals(2,point.distanceLinf(point4));
	}

	@Test
	public void addPoint3DVector3D() {
		Point3f point = new Point3f(0,0,0);
		Point3f point2 = new Point3f(-1,0,1);
		Vector3f vector = new Vector3f(1.2,1.2,1.2);
		Vector3f vector2 = new Vector3f(2.0,1.5,5.125);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		newPoint.add(point,vector);
		assertTrue(newPoint.equals(new Point3f(1.2,1.2,1.2)));
		
		newPoint.add(point2,vector2);
		assertTrue(newPoint.equals(new Point3f(1.0,1.5,6.125)));  	
	}

	@Test
	public void addVector3DPoint3D() {
		Point3f point = new Point3f(0,0,0);
		Point3f point2 = new Point3f(-1,0,1);
		Vector3f vector = new Vector3f(1.2,1.2,1.2);
		Vector3f vector2 = new Vector3f(2.0,1.5,5.125);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		newPoint.add(vector,point);
		assertTrue(newPoint.equals(new Point3f(1.2,1.2,1.2)));
		
		newPoint.add(vector2,point2);
		assertTrue(newPoint.equals(new Point3f(1.0,1.5,6.125))); 
	}

	@Test
	public void addVector3D() {
		Point3f point = new Point3f(0,0,0);
		Point3f point2 = new Point3f(-1,0,1);
		Vector3f vector = new Vector3f(1.2,1.2,1.2);
		Vector3f vector2 = new Vector3f(2.0,1.5,5.125);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		point.add(vector);
		assertTrue(point.equals(new Point3f(1.2,1.2,1.2)));
		
		point2.add(vector2);
		assertTrue(point2.equals(new Point3f(1.0,1.5,6.125))); 
	}

	@Test
	public void scaleAddIntVector3DPoint3D() {
		Point3f point = new Point3f(-1,0,1);
		Vector3f vector = new Vector3f(1.0,1.2,1.0);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		newPoint.scaleAdd(0,vector,point);
		assertTrue(newPoint.equals(new Point3f(-1,0,1)));
		
		newPoint.scaleAdd(1,vector,point);
		assertTrue(newPoint.equals(new Point3f(0.0,1.2,2.0)));
		
		newPoint.scaleAdd(-1,vector,point);
		assertTrue(newPoint.equals(new Point3f(-2.0,-1.2,0.0)));
		
		newPoint.scaleAdd(10,vector,point);
		assertTrue(newPoint.equals(new Point3f(9,12,11)));
	}

	@Test
	public void scaleAddDoubleVector3DPoint3D() {
		Point3f point = new Point3f(1,0,1);
		Vector3f vector = new Vector3f(-1,1,1);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		newPoint.scaleAdd(0.0,vector,point);
		assertTrue(newPoint.equals(new Point3f(1,0,1)));
		
		newPoint.scaleAdd(1.5,vector,point);
		assertTrue(newPoint.equals(new Point3f(-0.5,1.5,2.5)));
		
		newPoint.scaleAdd(-1.5,vector,point);
		assertTrue(newPoint.equals(new Point3f(2.5,-1.5,-0.5)));
		
		newPoint.scaleAdd(0.1,vector,point);
		assertTrue(newPoint.equals(new Point3f(0.9,0.1,1.1)));
	}

	@Test
	public void scaleAddIntPoint3DVector3D() {
		Point3f point = new Point3f(1.0,1.2,1.0);
		Vector3f vector = new Vector3f(-1,0,1);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		newPoint.scaleAdd(0,point,vector);
		assertTrue(newPoint.equals(new Point3f(-1,0,1)));
		
		newPoint.scaleAdd(1,point,vector);
		assertTrue(newPoint.equals(new Point3f(0.0,1.2,2.0)));
		
		newPoint.scaleAdd(-1,point,vector);
		assertTrue(newPoint.equals(new Point3f(-2.0,-1.2,0.0)));
		
		newPoint.scaleAdd(10,point,vector);
		assertTrue(newPoint.equals(new Point3f(9,12,11)));
	}

	@Test
	public void scaleAddDoublePoint3DVector3D() {
		Point3f point = new Point3f(-1,1,1);
		Vector3f vector = new Vector3f(1,0,1);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		newPoint.scaleAdd(0.0,point,vector);
		assertTrue(newPoint.equals(new Point3f(1,0,1)));
		
		newPoint.scaleAdd(1.5,point,vector);
		assertTrue(newPoint.equals(new Point3f(-0.5,1.5,2.5)));
		
		newPoint.scaleAdd(-1.5,point,vector);
		assertTrue(newPoint.equals(new Point3f(2.5,-1.5,-0.5)));
		
		newPoint.scaleAdd(0.1,point,vector);
		assertTrue(newPoint.equals(new Point3f(0.9,0.1,1.1)));
	}

	@Test
	public void scaleAddIntVector3D() {
		Vector3f vector = new Vector3f(1,0,1);
		Point3f newPoint = new Point3f(0,0,0);
		
		newPoint.scaleAdd(0,vector);
		assertTrue(newPoint.equals(new Point3f(1,0,1)));
		
		newPoint.scaleAdd(1,vector);
		assertTrue(newPoint.equals(new Point3f(2,0,2)));
		
		newPoint.scaleAdd(-10,vector);
		assertTrue(newPoint.equals(new Point3f(-19,0,-19)));
		
		
	}

	@Test
	public void scaleAddDoubleVector3D() {
		Vector3f vector = new Vector3f(1,0,1);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		newPoint.scaleAdd(0.5,vector);
		assertTrue(newPoint.equals(new Point3f(1,0,1)));
		
		newPoint.scaleAdd(1.2,vector);
		assertTrue(newPoint.equals(new Point3f(2.2,0.0,2.2)));
		
		newPoint.scaleAdd(-10,vector);
		assertTrue(newPoint.equals(new Point3f(-21,0,-21)));
	}

	@Test
	public void subPoint3DVector3D() {
		Point3f point = new Point3f(0,0,0);
		Point3f point2 = new Point3f(1,0,-1);
		Vector3f vector = new Vector3f(-1.2,-1.2,-1.2);
		Vector3f vector2 = new Vector3f(2.0,1.5,5.125);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		newPoint.sub(point,vector);
		assertTrue(newPoint.equals(new Point3f(1.2,1.2,1.2)));
		
		newPoint.sub(point2,vector2);
		assertTrue(newPoint.equals(new Point3f(-1.0,-1.5,-6.125))); 
	}

	@Test
	public void subVector3D() {
		Point3f point = new Point3f(0,0,0);
		Point3f point2 = new Point3f(-1,0,1);
		Vector3f vector = new Vector3f(-1.2,-1.2,-1.2);
		Vector3f vector2 = new Vector3f(-2.0,-1.5,-5.125);
		Point3f newPoint = new Point3f(0.0,0.0,0.0);
		
		point.sub(vector);
		assertTrue(point.equals(new Point3f(1.2,1.2,1.2)));
		
		point2.sub(vector2);
		assertTrue(point2.equals(new Point3f(1.0,1.5,6.125)));
	}

	@Test
	public void isCollinearPointsDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		Point3f point = new Point3f();
		
		assertTrue(FunctionalPoint3D.isCollinearPoints(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		assertTrue(FunctionalPoint3D.isCollinearPoints(-6, -4, 2, -1, 3, -1, 4, 10, -4, 0));
		assertTrue(FunctionalPoint3D.isCollinearPoints(-6, -4, 2, -1, 3, -1, 4, 10, -4.001, 0.01));
		assertFalse(FunctionalPoint3D.isCollinearPoints(0, 0, 0, 1, 1, 1, 1, 1, -5, 0));
	}

	@Test
	public void distancePointPoint() {
		Point3f point = new Point3f();
		
		assertEpsilonEquals(0,FunctionalPoint3D.distancePointPoint(0, 0, 0, 0, 0, 0));
		assertEpsilonEquals(Math.sqrt(14),FunctionalPoint3D.distancePointPoint(0, 0, 0, 1, 2, 3));
		assertEpsilonEquals(Math.sqrt(3),FunctionalPoint3D.distancePointPoint(0, 0, 0, 1, 1, 1));
	}

	@Test
	public void distanceSquaredPointPoint() {
		Point3f point = new Point3f();
		
		assertEpsilonEquals(0,FunctionalPoint3D.distanceSquaredPointPoint(0, 0, 0, 0, 0, 0));
		assertEpsilonEquals(14,FunctionalPoint3D.distanceSquaredPointPoint(0, 0, 0, 1, 2, 3));
		assertEpsilonEquals(3,FunctionalPoint3D.distanceSquaredPointPoint(0, 0, 0, 1, 1, 1));
	}

	@Test
	public void distanceL1PointPoint() {
		Point3f point = new Point3f();
		
		assertEpsilonEquals(6,FunctionalPoint3D.distanceL1PointPoint(1.0,2.0,3.0,0,0,0));
		assertEpsilonEquals(0,FunctionalPoint3D.distanceL1PointPoint(1.0,2.0,3.0,1,2,3));
		assertEpsilonEquals(0,FunctionalPoint3D.distanceL1PointPoint(1,2,3,1.0,2.0,3.0));
		assertEpsilonEquals(6,FunctionalPoint3D.distanceL1PointPoint(1.0,2.0,3.0,-1,0,1));
	}

	@Test
	public void distanceLinfPointPoint() {
		Point3f point = new Point3f();
				
		assertEpsilonEquals(3,FunctionalPoint3D.distanceLinfPointPoint(1.0,2.0,3.0,0,0,0));
		assertEpsilonEquals(0,FunctionalPoint3D.distanceLinfPointPoint(1.0,2.0,3.0,1,2,3));
		assertEpsilonEquals(0,FunctionalPoint3D.distanceLinfPointPoint(1,2,3,1.0,2.0,3.0));
		assertEpsilonEquals(2,FunctionalPoint3D.distanceLinfPointPoint(1.0,2.0,3.0,-1,0,1));
	}

}