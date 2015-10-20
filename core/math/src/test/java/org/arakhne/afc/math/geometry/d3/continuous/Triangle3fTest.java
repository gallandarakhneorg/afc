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

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
package org.arakhne.afc.math.geometry.d3.continuous;

import static org.arakhne.afc.math.MathConstants.PI;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d2.continuous.AbstractShape2fTestCase;
import org.arakhne.afc.math.geometry.d2.continuous.Segment2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
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
public class Triangle3fTest extends AbstractShape3fTestCase<Triangle3f> {
	
	@Override
	protected Triangle3f createShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Test
	@Override
	public void testClone() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void containsPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void testEquals() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void testHashCode() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void toBoundingBoxAlignedBox3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void translateVector3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void translateDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void containsDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsAlignedBox3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsSphere3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsSegment3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsTriangle3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsCapsule3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsOrientedBox3f() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsAbstractPlane3D() {
		throw new UnsupportedOperationException();
	}
	
	
	
	@Test
	public void mollerAlgorithmAxisTestX01() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void mollerAlgorithmAxisTestX02() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void mollerAlgorithmAxisTestY02() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void mollerAlgorithmAxisTestY01() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void mollerAlgorithmAxisTestZ12() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void mollerAlgorithmAxisTestZ0() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void mollerAlgorithmPlaneBoxOverlap() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void intersectsTriangleAlignedBox() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void toPlane() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void computeClosestPointTrianglePoint() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void distanceSquaredTriangleSegment() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void intersectsTriangleCapsule() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void intersectsCoplanarTriangleTriangle() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void intersectsTriangleSphere() {
		throw new UnsupportedOperationException();
	}
	
	@Test
	public void intersectsTriangleOrientedBox() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void intersectsTriangleSegment() {
		throw new UnsupportedOperationException();
	}
	
	
	
	@Test
	public void getTriangleSegmentIntersectionFactorWithJimenezAlgorithm() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void getTriangleSegmentIntersectionFactorWithBadouelAlgorithm() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void containsTrianglePoint() {
		Point3f p1 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p2 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p3 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f m = new Point3f(Math.random(),Math.random(),Math.random());
		Triangle3f t1 = new Triangle3f(p1,p2,p3);
		
		
		
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void intersectsCoplanarTriangle() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void intersectEdgeEdge() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void containsTrianglePoint3D() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void contains() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void getNormal() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void getPlane() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void clearBufferedData() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void toStringTest() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void distancePoint3D() {
		throw new UnsupportedOperationException();
	}
	

	@Test
	public void distanceSquaredPoint3D() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void distanceL1Point3D() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void distanceLinfPoint3D() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void getOrientation() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void getPivot() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void transform() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void translate() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void rotate() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void rotatePoint3D() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void toBoundingBox() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void equals() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void containsProjectionOfPoint3D() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void isEmpty() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void getClosestPointToPoint3D() {
		//COMPREHENSION OF THE METHOD IS FALSE, SO THE TEST IS FALSE TOO
		Point3f p1 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p2 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p3 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f m = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f closest;
		Triangle3f t1 = new Triangle3f(p1,p2,p3);
		
		double distance1 = p1.distanceSquared(m);
		double distance2 = p2.distanceSquared(m);
		double distance3 = p3.distanceSquared(m);
		double distanceMin = Math.min(Math.min(distance2, distance1),distance3);
		
		if (distanceMin==distance1) {
			closest = p1;
		} else if (distanceMin==distance2) {
			closest = p2;
		} else {
			closest = p3;
		}
		
		assertTrue(closest.equals(t1.getClosestPointTo(m)));
		assertTrue(p2.equals(t1.getClosestPointTo(m)));
		assertTrue(p3.equals(t1.getClosestPointTo(m)));
	}
	
	
	@Test
	public void getFarthestPointToPoint3D() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void getGroundHeight() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void getPointOnGround() {
		throw new UnsupportedOperationException();
	}
	
	
	@Test
	public void getSegment1() {
		Point3f p1 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p2 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p3 = new Point3f(Math.random(),Math.random(),Math.random());
		Triangle3f t1 = new Triangle3f(p1,p2,p3);
		
		AbstractSegment3F segment = new Segment3f(p1,p2);
		
		assertTrue(t1.getSegment1().equals(segment));
	}
	
	
	@Test
	public void getSegment2() {
		Point3f p1 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p2 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p3 = new Point3f(Math.random(),Math.random(),Math.random());
		Triangle3f t1 = new Triangle3f(p1,p2,p3);
		
		AbstractSegment3F segment = new Segment3f(p2,p3);
		
		assertTrue(t1.getSegment2().equals(segment));
	}
	
	
	@Test
	public void getSegment3() {
		Point3f p1 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p2 = new Point3f(Math.random(),Math.random(),Math.random());
		Point3f p3 = new Point3f(Math.random(),Math.random(),Math.random());
		Triangle3f t1 = new Triangle3f(p1,p2,p3);
		
		AbstractSegment3F segment = new Segment3f(p3,p1);
		
		assertTrue(t1.getSegment3().equals(segment));
	}
	
}
