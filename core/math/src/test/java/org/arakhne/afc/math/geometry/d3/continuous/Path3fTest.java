/* 
 * $Id$
 * 
 * Copyright (C) 2015 Hamza JAFFALI.
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
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("static-method")
public class Path3fTest extends AbstractMathTestCase {
	
	
	
	/** Testing the intersection between the path and an AlignedBox3f
     */
	@Test
    public void intersectsAlignedBox() {
		Path3f r = new Path3f();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		AlignedBox3f aB = new AlignedBox3f(new Point3f(0f,0f,0f),new Point3f(2f,2f,2f));
		AlignedBox3f aB2 = new AlignedBox3f(new Point3f(-1f,-1f,-1f),new Point3f(-2f,-2f,-2f));
		
		assertTrue(r.intersects(aB));
		assertFalse(r.intersects(aB2));
    }

	
	@Test
    public void intersectsPath() {
		Path3f r = new Path3f();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		Transform3D t = new Transform3D();
		
		Path3f rTrans = new Path3f();
		rTrans.moveTo(0f, 0f, 0f);
		rTrans.lineTo(-12f, 19f, 0f);
		rTrans.closePath();
		
		assertTrue(r.intersects(r));
		
		assertTrue(rTrans.intersects(r));
		
		t.makeTranslationMatrix(10, 10, 10);
		rTrans.transform(t);
		//System.out.println(rTrans.toString());
		
		assertFalse(rTrans.intersects(r));
		
	}
	
	@Test
    public void intersectsSphere() {
		Path3f r = new Path3f();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		AbstractSphere3F sfere = new Sphere3f(new Point3f(0.5f,0.5f,0.5f),5f);
		
		assertTrue(r.intersects(sfere));
		
		AbstractSphere3F sfere2 = new Sphere3f(new Point3f(20f,20f,20f),0.001);

		assertFalse(r.intersects(sfere2));
		
	}
	
	@Test
    public void intersectsSegment() {
		Path3f r = new Path3f();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		AbstractSegment3F segm = new Segment3f(1,0,1,0,0,0);
		AbstractSegment3F s = new Segment3f(1,0,0,0,0,1);
		AbstractSegment3F segm2 = new Segment3f(new Point3f(20f,20f,20f),new Point3f(30f,30f,30f));
		AbstractSegment3F s2 = new Segment3f(-1,-1,-1,8,-4,1);
		
		assertTrue(r.intersects(segm));
		assertFalse(r.intersects(s));
		assertFalse(r.intersects(segm2));
		assertTrue(r.intersects(s2));
	}
	
	@Test
    public void intersectsTriangle() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void intersectsOrientedBox() {
		Path3f r = new Path3f();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		OrientedBox3f aB = new OrientedBox3f(new Point3f(0f,0f,0f),new Vector3f(2f,0f,0f),new Vector3f(0f,2f,0f),2f,2f,2f);
		OrientedBox3f aB2 = new OrientedBox3f(new Point3f(-1f,-1f,-1f),new Vector3f(-2f,0f,0f),new Vector3f(0f,-2f,0f),1f,1f,1f);
		
		assertTrue(r.intersects(aB));
		assertFalse(r.intersects(aB2));
	}
	
	@Test
    public void intersectsPlane3D() {
		Path3f r = new Path3f();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		Plane3D<AbstractPlane4F> plane = new Plane4f(new Vector3f(1f,1f,1f),new Point3f(0.5f,0.5,0.5));
		Plane3D<AbstractPlane4F> plane2 = new Plane4f(new Vector3f(1f,1f,1f),new Point3f(20f,20f,20f));
		
		assertTrue(r.intersects(plane));
		assertFalse(r.intersects(plane2));
	}
	
	@Test
    public void isPolyline() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void containsDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void equals() {
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3f p3 = p.clone();
		Path3f p2 = new Path3f(p);
		
		
		assertTrue(p2.toString().equals(p.toString()));
		assertTrue(p3.toString().equals(p.toString()));
		assertTrue(p3.toString().equals(p2.toString()));
		
		assertTrue(p3.equals(p));
		
		assertTrue(p2.equals(p3));
		assertTrue(p.equals(p2));
	}
	
	
	@Test
    public void removeDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void containsPoint3D() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void toStringTest() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void isEmptyAndClear() {
		Path3f p = new Path3f();
		
		assertTrue(p.isEmpty());
		
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		assertFalse(p.isEmpty());
		
		p.clear();
		
		assertTrue(p.isEmpty());
	}
		
	@Test
    public void cloneTest() {
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3f p2 = p.clone();
		
		assertTrue(p.equals(p2));
		assertTrue(p2.equals(p));
	}
	
	@Test
    public void getClosestPointTo() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void getFarthestPointTo() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void toBoundingBox() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void distanceSquared() {
		throw new UnsupportedOperationException();
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
    public void transform() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void translate() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void createTransformedShape() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void add() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void removeLast() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void setLastPoint() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void moveTo() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void lineTo() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void quadTo() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void curveTo() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void closePath() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void sizeTest() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void toDoubleArrayTest() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void toPointArrayTest() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void getPointAt() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void getCurrentPoint() {
		throw new UnsupportedOperationException();
	}
	
	
}
