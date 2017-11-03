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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Segment3fTest extends AbstractShape3fTestCase<AbstractSegment3F> {

	
	@Override
	protected AbstractSegment3F createShape() {
		return new Segment3f(Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random());
	}

	@Test
	@Override
	public void testClone() {
		AbstractSegment3F b = this.r.clone();

		assertNotSame(b, this.r);
		assertEpsilonEquals(this.r.getX1(), b.getX1());
		assertEpsilonEquals(this.r.getY1(), b.getY1());
		assertEpsilonEquals(this.r.getZ1(), b.getZ1());
		assertEpsilonEquals(this.r.getX2(), b.getX2());
		assertEpsilonEquals(this.r.getY2(), b.getY2());
		assertEpsilonEquals(this.r.getZ2(), b.getZ2());
		assertTrue(this.r.getP1().equals(b.getP1()));
		assertTrue(this.r.getP2().equals(b.getP2()));
		assertTrue(this.r.getDirection().equals(b.getDirection()));
	}

	@Test
	@Override
	public void distancePoint3D() {
		AbstractSegment3F seg = new Segment3f(this.random.nextDouble()*20,this.random.nextDouble()*20,this.random.nextDouble()*20,this.random.nextDouble()*20,this.random.nextDouble()*20,this.random.nextDouble()*20);
		Point3f pnt = new Point3f(this.random.nextDouble()*20,this.random.nextDouble()*20,this.random.nextDouble()*20);
		
		Vector3f vect = new Vector3f(seg.getP1().getX()-pnt.getX(),seg.getP1().getY()-pnt.getY(),seg.getP1().getZ()-pnt.getZ());
		double angle = vect.angle(seg.getDirection());
		double distance = vect.length()*Math.sin(angle);
		
		assertEpsilonEquals(distance,seg.distanceLine(pnt));
	}

	@Test
	@Override
	public void containsPoint3D() {
		AbstractSegment3F seg = new Segment3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Point3f p = new Point3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		Vector3f v1 = new Vector3f(p.x-seg.getX1(),p.y-seg.getY1(),p.y-seg.getY1());
		Vector3f v2 = new Vector3f(p.x-seg.getX1(),p.y-seg.getY1(),p.y-seg.getY1());
		
		Vector3f v3 = (Vector3f) seg.getDirection();
		
		assertTrue(v1.isColinear(v3)==seg.contains(p));
		assertTrue(v2.isColinear(v3)==seg.contains(p));		
	}

	@Test
	@Override
	public void testEquals() {
		AbstractSegment3F seg = new Segment3f(this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble(),this.random.nextDouble());
		AbstractSegment3F seg2 = seg.clone();
		
		assertTrue(seg2.equals(seg));
	}

	@Test
	@Override
	public void testHashCode() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void toBoundingBox() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void toBoundingBoxAlignedBox3x() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void distanceSquaredPoint3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void distanceL1Point3D() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void distanceLinfPoint3D() {
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
	public void intersectsAlignedBox3x() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsSphere3x() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsSegment3x() {
		Segment3f s1 = new Segment3f(0,0,0,10,10,0);
		Segment3f s2 = new Segment3f(0,10,0,10,0,0);
		Segment3f s3 = new Segment3f(0,0,0,12,0,0);
		Segment3f s4 = new Segment3f(-3,7,14,12,-13,-13);
		Segment3f s5 = new Segment3f(0,1/2f,1/3f,1,1/2f,2/3f);
		Segment3f s6 = new Segment3f(-1/2f,2/3f,-1/3f,1/2f,-2/3f,1/3f);
		Segment3f s7 = new Segment3f(10,10,0,0,0,10);
		Segment3f s8 = new Segment3f(1,1,1,11,11,1);
		
		assertTrue(s1.intersects(s2));
		assertTrue(s1.intersects(s3));
		assertFalse(s1.intersects(s4));
		assertFalse(s1.intersects(s5));
		assertTrue(s1.intersects(s6));
		assertTrue(s1.intersects(s7));
		assertFalse(s1.intersects(s8));
		
		assertTrue(s2.intersects(s3));
		assertFalse(s2.intersects(s4));
		assertFalse(s2.intersects(s5));
		assertFalse(s2.intersects(s6));
		assertFalse(s2.intersects(s7));
		assertFalse(s2.intersects(s8));
		
		assertFalse(s3.intersects(s4));
		assertFalse(s3.intersects(s5));
		assertTrue(s3.intersects(s6));
		assertFalse(s3.intersects(s7));
		assertFalse(s3.intersects(s8));
		
		assertFalse(s4.intersects(s5));
		assertFalse(s4.intersects(s6));
		assertFalse(s4.intersects(s7));
		assertFalse(s4.intersects(s8));
		
		assertFalse(s5.intersects(s6));
		assertFalse(s5.intersects(s7));
		assertFalse(s5.intersects(s8));
		
		assertFalse(s6.intersects(s7));
		assertFalse(s6.intersects(s8));
		
		assertTrue(s7.intersects(s8));
	}

	@Test
	@Override
	public void intersectsTriangle3x() {
		Segment3f s1 = new Segment3f(0,0,0,10,10,0);
		
		AbstractTriangle3F t1 = new Triangle3f(10,10,0, 0,0,10, 10,10,10);
		
		assertTrue(s1.intersects(t1));
	}

	@Test
	@Override
	public void intersectsCapsule3x() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsOrientedBox3x() {
		throw new UnsupportedOperationException();
	}

	@Test
	@Override
	public void intersectsAbstractPlane3D() {
		throw new UnsupportedOperationException();
	}

	
}
