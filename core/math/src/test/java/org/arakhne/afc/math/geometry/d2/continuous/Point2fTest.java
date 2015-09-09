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

import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
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
		Point2f point = new Point2f();
		
		assertEpsilonEquals(0,point.distancePointPoint(0, 0, 0, 0));
		assertEpsilonEquals(Math.sqrt(5),point.distancePointPoint(0, 0, 1, 2));
		assertEpsilonEquals(Math.sqrt(2),point.distancePointPoint(0, 0, 1, 1));
	}

	@Test
	public void distanceSquaredPointPoint() {
		Point2f point = new Point2f();
		
		assertEpsilonEquals(0,point.distanceSquaredPointPoint(0, 0, 0, 0));
		assertEpsilonEquals(5,point.distanceSquaredPointPoint(0, 0, 1, 2));
		assertEpsilonEquals(2,point.distanceSquaredPointPoint(0, 0, 1, 1));
	}

	@Test
	public void distanceL1PointPoint() {
		Point2f point = new Point2f();
		
		assertEpsilonEquals(4,point.distanceL1PointPoint(1.0, 2.0, 3.0, 0));
		assertEpsilonEquals(0,point.distanceL1PointPoint(1.0, 2.0, 1 ,2));
		assertEpsilonEquals(0,point.distanceL1PointPoint(1, 2, 1.0, 2.0));
		assertEpsilonEquals(4,point.distanceL1PointPoint(1.0, 2.0, -1, 0));
	}

	@Test
	public void distanceLinfPointPoint() {
		Point2f point = new Point2f();
		
		assertEpsilonEquals(2,point.distanceLinfPointPoint(1.0,2.0,3.0,0));
		assertEpsilonEquals(0,point.distanceLinfPointPoint(1.0,2.0,1,2));
		assertEpsilonEquals(0,point.distanceLinfPointPoint(1,2,1.0,2.0));
		assertEpsilonEquals(2,point.distanceLinfPointPoint(1.0,2.0,-1,0));
	}

	@Test
	public void testClone() {
		Point2f point = new Point2f(1,2);
		Point2f pointClone = point.clone();
		
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
				
		assertEpsilonEquals(0,point.distanceSquared(point));
		assertEpsilonEquals(5,point.distanceSquared(point2));
		assertEpsilonEquals(2,point3.distanceSquared(point));
	}

	@Test
	public void distanceL1() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void distanceLinf() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getDistanceSquared() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getDistance() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getDistanceL1() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void getDistanceLinf() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void addPoint2DVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void addVector2DPoint2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void addVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddIntVector2DPoint2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddDoubleVector2DPoint2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddIntPoint2DVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddDoublePoint2DVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddIntVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void scaleAddDoubleVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void subPoint2DVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void subVector2D() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void isCollinearPointsDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void isCollinearVectorsDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

}