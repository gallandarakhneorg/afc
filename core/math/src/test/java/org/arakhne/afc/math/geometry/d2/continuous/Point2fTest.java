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
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.FunctionalPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Point2fTest extends AbstractMathTestCase {

	@Test
	public void distancePointPoint() {
		Point2D point = new Point2f();
		
		assertEpsilonEquals(0,FunctionalPoint2D.distancePointPoint(0, 0, 0, 0));
		assertEpsilonEquals(Math.sqrt(5),FunctionalPoint2D.distancePointPoint(0, 0, 1, 2));
		assertEpsilonEquals(Math.sqrt(2),FunctionalPoint2D.distancePointPoint(0, 0, 1, 1));
	}

	@Test
	public void distanceSquaredPointPoint() {
		Point2D point = new Point2f();
		
		assertEpsilonEquals(0,FunctionalPoint2D.distanceSquaredPointPoint(0, 0, 0, 0));
		assertEpsilonEquals(5,FunctionalPoint2D.distanceSquaredPointPoint(0, 0, 1, 2));
		assertEpsilonEquals(2,FunctionalPoint2D.distanceSquaredPointPoint(0, 0, 1, 1));
	}

	@Test
	public void distanceL1PointPoint() {
		Point2f point = new Point2f();
		
		assertEpsilonEquals(4,FunctionalPoint2D.distanceL1PointPoint(1.0, 2.0, 3.0, 0));
		assertEpsilonEquals(0,FunctionalPoint2D.distanceL1PointPoint(1.0, 2.0, 1 ,2));
		assertEpsilonEquals(0,FunctionalPoint2D.distanceL1PointPoint(1, 2, 1.0, 2.0));
		assertEpsilonEquals(4,FunctionalPoint2D.distanceL1PointPoint(1.0, 2.0, -1, 0));
	}

	@Test
	public void distanceLinfPointPoint() {
		Point2f point = new Point2f();
		
		assertEpsilonEquals(2,FunctionalPoint2D.distanceLinfPointPoint(1.0,2.0,3.0,0));
		assertEpsilonEquals(0,FunctionalPoint2D.distanceLinfPointPoint(1.0,2.0,1,2));
		assertEpsilonEquals(0,FunctionalPoint2D.distanceLinfPointPoint(1,2,1.0,2.0));
		assertEpsilonEquals(2,FunctionalPoint2D.distanceLinfPointPoint(1.0,2.0,-1,0));
	}

	@Test
	public void testClone() {
		Point2D point = new Point2f(1,2);
		Point2D pointClone = point.clone();
		
		assertTrue(point.equals(pointClone));
	}

	@Test
	public void distanceSquared() {
		int tab[] = {1, 2};
		Point2f point = new Point2f(0, 0);
		Point2f point2 = new Point2f(tab);
		Point2f point3 = new Point2f(1, 1);
				
		assertEpsilonEquals(0,point.distanceSquared(point));
		assertEpsilonEquals(5,point.distanceSquared(point2));
		assertEpsilonEquals(2,point3.distanceSquared(point));
	}

	@Test
	public void distance() {
		int tab[] = {1, 2};
		Point2f point = new Point2f(0, 0);
		Point2f point2 = new Point2f(tab);
		Point2f point3 = new Point2f(1, 1);
				
		assertEpsilonEquals(0,point.distance(point));
		assertEpsilonEquals(2,point.distance(point2));
		assertEpsilonEquals(1,point3.distance(point));
	}

	@Test
	public void distanceL1() {
		Point2f point = new Point2f(1.0, 2.0);
		Point2f point2 = new Point2f(3.0, 0);
		Point2f point3 = new Point2f(-1, 0);
		
		assertEpsilonEquals(4,point.distanceL1(point2));
		assertEpsilonEquals(0,point.distanceL1(point));
		assertEpsilonEquals(4,point.distanceL1(point3));
	}

	@Test
	public void distanceLinf() {
		Point2f point = new Point2f(1.0, 2.0);
		Point2f point2 = new Point2f(3.0, 0);
		Point2f point3 = new Point2f(-1, 0);
		
		assertEpsilonEquals(2,point.distanceLinf(point2));
		assertEpsilonEquals(0,point.distanceLinf(point));
		assertEpsilonEquals(2,point.distanceLinf(point3));
	}

	@Test
	public void getDistanceSquared() {
		int tab[] = {1, 2};
		Point2f point = new Point2f(0, 0);
		Point2f point2 = new Point2f(tab);
		Point2f point3 = new Point2f(1, 1);
				
		assertEpsilonEquals(0,point.getDistanceSquared(point));
		assertEpsilonEquals(5,point.getDistanceSquared(point2));
		assertEpsilonEquals(2,point3.getDistanceSquared(point));
	}

	@Test
	public void getDistance() {
		int tab[] = {1, 2};
		Point2f point = new Point2f(0, 0);
		Point2f point2 = new Point2f(tab);
		Point2f point3 = new Point2f(1, 1);
				
		assertEpsilonEquals(0,point.getDistanceSquared(point));
		assertEpsilonEquals(Math.sqrt(5),point.getDistance(point2));
		assertEpsilonEquals(Math.sqrt(2),point3.getDistance(point));
	}

	@Test
	public void getDistanceL1() {
		Point2f point = new Point2f(1.0, 2.0);
		Point2f point2 = new Point2f(3.0, 0);
		Point2f point3 = new Point2f(-1, 0);
		
		assertEpsilonEquals(4,point.getDistanceL1(point2));
		assertEpsilonEquals(0,point.getDistanceL1(point));
		assertEpsilonEquals(4,point.getDistanceL1(point3));
	}

	@Test
	public void getDistanceLinf() {
		Point2f point = new Point2f(1.0, 2.0);
		Point2f point2 = new Point2f(3.0, 0);
		Point2f point3 = new Point2f(-1, 0);
		
		assertEpsilonEquals(2,point.getDistanceLinf(point2));
		assertEpsilonEquals(0,point.getDistanceLinf(point));
		assertEpsilonEquals(2,point.getDistanceLinf(point3));
	}

	@Test
	public void addPoint2DVector2D() {
		Point2f point = new Point2f(0,0);
		Point2f point2 = new Point2f(-1,0);
		Vector2f vector = new Vector2f(1.2,1.2);
		Vector2f vector2 = new Vector2f(2.0,1.5);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		newPoint.add(point,vector);
		assertTrue(newPoint.equals(new Point2f(1.2,1.2)));
		
		newPoint.add(point2,vector2);
		assertTrue(newPoint.equals(new Point2f(1.0,1.5)));
	}

	@Test
	public void addVector2DPoint2D() {
		Point2f point = new Point2f(0,0);
		Point2f point2 = new Point2f(-1,0);
		Vector2f vector = new Vector2f(1.2,1.2);
		Vector2f vector2 = new Vector2f(2.0,1.5);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		newPoint.add(vector,point);
		assertTrue(newPoint.equals(new Point2f(1.2,1.2)));
		
		newPoint.add(vector2,point2);
		assertTrue(newPoint.equals(new Point2f(1.0,1.5))); 
	}

	@Test
	public void addVector2D() {
		Point2f point = new Point2f(0,0);
		Point2f point2 = new Point2f(-1,0);
		Vector2f vector = new Vector2f(1.2,1.2);
		Vector2f vector2 = new Vector2f(2.0,1.5);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		point.add(vector);
		assertTrue(point.equals(new Point2f(1.2,1.2)));
		
		point2.add(vector2);
		assertTrue(point2.equals(new Point2f(1.0,1.5)));
	}

	@Test
	public void scaleAddIntVector2DPoint2D() {
		Point2f point = new Point2f(-1,0);
		Vector2f vector = new Vector2f(1.0,1.2);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		newPoint.scaleAdd(0,vector,point);
		assertTrue(newPoint.equals(new Point2f(-1,0)));
		
		newPoint.scaleAdd(1,vector,point);
		assertTrue(newPoint.equals(new Point2f(0.0,1.2)));
		
		newPoint.scaleAdd(-1,vector,point);
		assertTrue(newPoint.equals(new Point2f(-2.0,-1.2)));
		
		newPoint.scaleAdd(10,vector,point);
		assertTrue(newPoint.equals(new Point2f(9,12)));
	}

	@Test
	public void scaleAddDoubleVector2DPoint2D() {
		Point2f point = new Point2f(1,0);
		Vector2f vector = new Vector2f(-1,1);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		newPoint.scaleAdd(0.0,vector,point);
		assertTrue(newPoint.equals(new Point2f(1,0)));
		
		newPoint.scaleAdd(1.5,vector,point);
		assertTrue(newPoint.equals(new Point2f(-0.5,1.5)));
		
		newPoint.scaleAdd(-1.5,vector,point);
		assertTrue(newPoint.equals(new Point2f(2.5,-1.5)));
		
		newPoint.scaleAdd(0.1,vector,point);
		assertTrue(newPoint.equals(new Point2f(0.9,0.1)));
		
	}

	@Test
	public void scaleAddIntPoint2DVector2D() {
		Point2f point = new Point2f(1.0,1.2);
		Vector2f vector = new Vector2f(-1,0);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		newPoint.scaleAdd(0,point,vector);
		assertTrue(newPoint.equals(new Point2f(-1,0)));
		
		newPoint.scaleAdd(1,point,vector);
		assertTrue(newPoint.equals(new Point2f(0.0,1.2)));
		
		newPoint.scaleAdd(-1,point,vector);
		assertTrue(newPoint.equals(new Point2f(-2.0,-1.2)));
		
		newPoint.scaleAdd(10,point,vector);
		assertTrue(newPoint.equals(new Point2f(9,12)));
	}

	@Test
	public void scaleAddDoublePoint2DVector2D() {
		Point2f point = new Point2f(-1,1);
		Vector2f vector = new Vector2f(1,0);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		newPoint.scaleAdd(0.0,point,vector);
		assertTrue(newPoint.equals(new Point2f(1,0)));
		
		newPoint.scaleAdd(1.5,point,vector);
		assertTrue(newPoint.equals(new Point2f(-0.5,1.5)));
		
		newPoint.scaleAdd(-1.5,point,vector);
		assertTrue(newPoint.equals(new Point2f(2.5,-1.5)));
		
		newPoint.scaleAdd(0.1,point,vector);
		assertTrue(newPoint.equals(new Point2f(0.9,0.1)));
	}

	@Test
	public void scaleAddIntVector2D() {
		Vector2f vector = new Vector2f(1,0);
		Point2f newPoint = new Point2f(0,0);
		
		newPoint.scaleAdd(0,vector);
		assertTrue(newPoint.equals(new Point2f(1,0)));
		
		newPoint.scaleAdd(1,vector);
		assertTrue(newPoint.equals(new Point2f(2,0)));
		
		newPoint.scaleAdd(-10,vector);
		assertTrue(newPoint.equals(new Point2f(-19,0)));
	}

	@Test
	public void scaleAddDoubleVector2D() {
		Vector2f vector = new Vector2f(1,0);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		newPoint.scaleAdd(0.5,vector);
		assertTrue(newPoint.equals(new Point2f(1,0)));
		
		newPoint.scaleAdd(1.2,vector);
		assertTrue(newPoint.equals(new Point2f(2.2,0.0)));
		
		newPoint.scaleAdd(-10,vector);
		assertTrue(newPoint.equals(new Point2f(-21,0)));
	}

	@Test
	public void subPoint2DVector2D() {
		Point2f point = new Point2f(0,0);
		Point2f point2 = new Point2f(1,0);
		Vector2f vector = new Vector2f(-1.2,-1.2);
		Vector2f vector2 = new Vector2f(2.0,1.5);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		newPoint.sub(point,vector);
		assertTrue(newPoint.equals(new Point2f(1.2,1.2)));
		
		newPoint.sub(point2,vector2);
		assertTrue(newPoint.equals(new Point2f(-1.0,-1.5))); 
	}

	@Test
	public void subVector2D() {
		Point2f point = new Point2f(0,0);
		Point2f point2 = new Point2f(-1,0);
		Vector2f vector = new Vector2f(-1.2,-1.2);
		Vector2f vector2 = new Vector2f(-2.0,-1.5);
		Point2f newPoint = new Point2f(0.0,0.0);
		
		point.sub(vector);
		assertTrue(point.equals(new Point2f(1.2,1.2)));
		
		point2.sub(vector2);
		assertTrue(point2.equals(new Point2f(1.0,1.5)));
	}

	@Test
	public void isCollinearPointsDoubleDoubleDoubleDoubleDoubleDouble() {
		Point2f point = new Point2f();
		
		assertTrue(FunctionalPoint2D.isCollinearPoints(0, 0, 0, 0, 0, 0));
		assertTrue(FunctionalPoint2D.isCollinearPoints(-6, -4, -1, 3, 4, 10));
		assertFalse(FunctionalPoint2D.isCollinearPoints(0, 0, 1, 1, 1, -5));
	}

}