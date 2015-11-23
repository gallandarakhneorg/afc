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

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d3.Point3D;
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
		
		Path3f rCurv = new Path3f();
		rCurv.moveTo(0f, 0f, 0f);
		rCurv.curveTo(5,-1f,0f,-5f,1,0f,-12f, 19f, 0f);
		rCurv.closePath();
		
		assertTrue(r.intersects(r));
		
		assertTrue(rTrans.intersects(r));
		assertTrue(rCurv.intersects(r));
		
		t.makeTranslationMatrix(20, 20, 20);
		rTrans.transform(t);
		rCurv.transform(t);
		
		assertFalse(rTrans.intersects(r));
		assertFalse(rCurv.intersects(r));
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
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3f p2 = new Path3f();
		p2.moveTo(0f, 0f, 0f);
		p2.lineTo(1f, 1f, 1f);
		p2.closePath();
		
		Path3f p3 = new Path3f();
		p3.moveTo(0f, 0f, 0f);
		p3.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p3.closePath();
		
		Path3f p4 = new Path3f();
		p4.moveTo(0f, 0f, 0f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p4.closePath();
		
		
		assertFalse(p.isPolyline());
		assertTrue(p2.isPolyline());
		assertFalse(p3.isPolyline());
		assertFalse(p4.isPolyline());
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
		Path3f p = new Path3f();
		p.moveTo(0, 0, 0);
		p.lineTo(1, 1, 1);
		p.quadTo(3, 0, 0, 4, 3, 0.5);
		p.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		p.closePath();
		
		assertTrue(p.toString().equals(
				"[0.0, 0.0, 0.0, " //$NON-NLS-1$
				+ "1.0, 1.0, 1.0, " //$NON-NLS-1$
				+ "3.0, 0.0, 0.0, 4.0, 3.0, 0.5, " //$NON-NLS-1$
				+ "5.0, -1.0, 0.0, 6.0, 5.0, 0.0, 7.0, -5.0, 0.0]")); //$NON-NLS-1$
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
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.lineTo(-1f, 1f, 1f);
		p.lineTo(0.5f, -3f, 7f);
		p.lineTo(6f, 2f, 0f);
		p.lineTo(0f, 5f, 0f);
		p.closePath();
		
		List<Point3f> list = new ArrayList<>();
		
		Point3f randomPoint = randomPoint3f();
		
		list.add((Point3f) (new Segment3f(0f, 0f, 0f,1f, 1f, 1f)).getClosestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(1f, 1f, 1f,-1f, 1f, 1f)).getClosestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(-1f, 1f, 1f,0.5f, -3f, 7f)).getClosestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(0.5f, -3f, 7f,6f, 2f, 0f)).getClosestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(6f, 2f, 0f,0f, 5f, 0f)).getClosestPointTo(randomPoint));
		
		Point3f closestPoint = new Point3f(0,0,0);
		
		for(Point3f point : list) {
			if (point.distance(randomPoint)<closestPoint.distance(randomPoint)) {
				closestPoint = point;
			}
		}
		
		assertTrue(closestPoint.equals(p.getClosestPointTo(randomPoint)));
	}
	
	@Test
    public void getFarthestPointTo() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void toBoundingBox() {
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3f p1 = new Path3f();
		p1.moveTo(0f, 0f, 0f);
		p1.lineTo(1f, 1f, 1f);
		p1.lineTo(-1f, 1f, 1f);
		p1.lineTo(0.5f, -3f, 7f);
		p1.lineTo(6f, 2f, 0f);
		p1.lineTo(0f, 5f, 0f);
		p1.closePath();
		
		Path3f p2 = new Path3f();
		p2.moveTo(-10f, -10f, -10f);
		p2.lineTo(4f, 7f, 3f);
		p2.lineTo(-3f, -7f, 5f);
		p2.lineTo(0.5f, -4f, 7f);
		p2.lineTo(6f, -9f, 0f);
		p2.lineTo(0f, 3f, 3f);
		p2.closePath();
		
		AlignedBox3f pBox = new AlignedBox3f(new Point3f(0,-5,0),new Point3f(7,5,1));
		AlignedBox3f p1Box = new AlignedBox3f(new Point3f(-1,-3,0),new Point3f(6,5,7));
		AlignedBox3f p2Box = new AlignedBox3f(new Point3f(-10,-10,-10),new Point3f(6,7,7));
		
		assertTrue(pBox.equals(p.toBoundingBoxWithCtrlPoints()));
		assertTrue(p1Box.equals(p1.toBoundingBox()));
		assertTrue(p2Box.equals(p2.toBoundingBox()));
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
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		double dx = this.random.nextDouble()*20;
		double dy = this.random.nextDouble()*20;
		double dz = this.random.nextDouble()*20;
		
		Path3f p2 = new Path3f();
		p2.moveTo(0f+dx, 0f+dy, 0f+dz);
		p2.lineTo(1f+dx, 1f+dy, 1f+dz);
		p2.quadTo(3f+dx, 0f+dy, 0f+dz, 4f+dx, 3f+dy, 0.5+dz);
		p2.curveTo(5f+dx, -1f+dy, 0f+dz, 6f+dx, 5f+dy, 0f+dz, 7f+dx, -5f+dy, 0f+dz);
		p2.closePath();
		
		p.translate(dx, dy, dz);
		
		assertTrue(p.equals(p2));		
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
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		double size = p.size();
		p.removeLast();
		p.removeLast();
		assertEpsilonEquals(p.size(),size-3);
		size = p.size();
		p.removeLast();
		assertEpsilonEquals(p.size(),size-2);
		size = p.size();
		p.removeLast();
		assertEpsilonEquals(p.size(),size-1);
		size = p.size();
		p.removeLast();
		assertEpsilonEquals(p.size(),size-1);
	}
	
	@Test
    public void setLastPoint() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void sizeTest() {
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3f p2 = new Path3f();
		p2.moveTo(0f, 0f, 0f);
		p2.lineTo(1f, 1f, 1f);
		p2.lineTo(2f, 1f, 1f);
		p2.lineTo(3f, 1f, 1f);
		p2.lineTo(4f, 1f, 1f);
		p2.lineTo(5f, 1f, 1f);
		p2.closePath();
		
		Path3f p3 = new Path3f();
		p3.moveTo(0f, 0f, 0f);
		p3.quadTo(4f, 3f, 0.5, 1f, 1f, 1f);
		p3.quadTo(3f, 3f, 0.5, 2f, 1f, 1f);
		p3.quadTo(5f, 3f, 0.5, 3f, 1f, 1f);
		p3.closePath();
		
		Path3f p4 = new Path3f();
		p4.moveTo(0f, 0f, 0f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 10f, -5f, 20f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 5f, -5f, 30f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 3f, -7f, 11f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 9f, -10f, 16f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 0f, -10f, 1f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 3f, 3f, 3f);
		p.closePath();
		
		assertEpsilonEquals(p.size(),7);
		assertEpsilonEquals(p2.size(),6);
		assertEpsilonEquals(p3.size(),7);
		assertEpsilonEquals(p4.size(),22);		
	}
	
	@Test
    public void toDoubleArrayTest() {
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		double [] array = p.toDoubleArray();
		
		assertTrue(new Point3f(array[0],array[1],array[2]).equals(new Point3f(0,0,0)));
		assertTrue(new Point3f(array[3],array[4],array[5]).equals(new Point3f(1,1,1)));
		assertTrue(new Point3f(array[6],array[7],array[8]).equals(new Point3f(3,0,0)));
		assertTrue(new Point3f(array[9],array[10],array[11]).equals(new Point3f(4,3,0.5)));
		assertTrue(new Point3f(array[12],array[13],array[14]).equals(new Point3f(5,-1,0)));
		assertTrue(new Point3f(array[15],array[16],array[17]).equals(new Point3f(6,5,0)));
		assertTrue(new Point3f(array[18],array[19],array[20]).equals(new Point3f(7,-5,0)));
	}
	
	@Test
    public void toPointArrayTest() {
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Point3D [] array = p.toPointArray();
		
		assertTrue(array[0].equals(new Point3f(0,0,0)));
		assertTrue(array[1].equals(new Point3f(1,1,1)));
		assertTrue(array[2].equals(new Point3f(3,0,0)));
		assertTrue(array[3].equals(new Point3f(4,3,0.5)));
		assertTrue(array[4].equals(new Point3f(5,-1,0)));
		assertTrue(array[5].equals(new Point3f(6,5,0)));
		assertTrue(array[6].equals(new Point3f(7,-5,0)));
	}
	
	@Test
    public void getPointAt() {
		Path3f p = new Path3f();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		assertTrue(p.getPointAt(0).equals(new Point3f(0,0,0)));
		assertTrue(p.getPointAt(1).equals(new Point3f(1,1,1)));
		assertTrue(p.getPointAt(2).equals(new Point3f(3,0,0)));
		assertTrue(p.getPointAt(3).equals(new Point3f(4,3,0.5)));
		assertTrue(p.getPointAt(4).equals(new Point3f(5,-1,0)));
		assertTrue(p.getPointAt(5).equals(new Point3f(6,5,0)));
		assertTrue(p.getPointAt(6).equals(new Point3f(7,-5,0)));
	}
	
	@Test
    public void getCurrentPoint() {
		throw new UnsupportedOperationException();
	}
	
	
}
