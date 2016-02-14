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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.junit.Test;

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"static-method"})
public class Path3dTest extends AbstractMathTestCase {
	
	
	
	/** Testing the intersection between the path and an AlignedBox3f
     */
	@Test
    public void intersectsAlignedBox() {
		Path3d r = new Path3d();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		AlignedBox3d aB = new AlignedBox3d(new Point3f(0f,0f,0f),new Point3f(2f,2f,2f));
		AlignedBox3d aB2 = new AlignedBox3d(new Point3f(-1f,-1f,-1f),new Point3f(-2f,-2f,-2f));
		
		assertTrue(r.intersects(aB));
		assertFalse(r.intersects(aB2));
    }

	
	@Test
    public void intersectsPath() {
		Path3d r = new Path3d();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		Transform3D t = new Transform3D();
		
		Path3d rTrans = new Path3d();
		rTrans.moveTo(0f, 0f, 0f);
		rTrans.lineTo(-12f, 19f, 0f);
		rTrans.closePath();
		
		Path3d rCurv = new Path3d();
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
		Path3d r = new Path3d();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		AbstractSphere3F sfere = new Sphere3d(new Point3f(0.5f,0.5f,0.5f),5f);
		
		assertTrue(r.intersects(sfere));
		
		AbstractSphere3F sfere2 = new Sphere3d(new Point3d(20f,20f,20f),0.001);

		assertFalse(r.intersects(sfere2));
		
	}
	
	@Test
    public void intersectsSegment() {
		Path3d r = new Path3d();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		AbstractSegment3F segm = new Segment3d(1,0,1,0,0,0);
		AbstractSegment3F s = new Segment3f(1,0,0,0,0,1);
		AbstractSegment3F segm2 = new Segment3d(new Point3f(20f,20f,20f),new Point3f(30f,30f,30f));
		AbstractSegment3F s2 = new Segment3f(-1,-1,-1,8,-4,1);
		
		assertTrue(r.intersects(segm));
		assertFalse(r.intersects(s));
		assertFalse(r.intersects(segm2));
		assertTrue(r.intersects(s2));
	}
	
	@Test
    public void intersectsTriangle() {
		Path3d path1 = new Path3d();
		path1.moveTo(0f, 0f, 0f);
		path1.lineTo(1f, 1f, 1f);
		path1.closePath();
		
		Path3d path2 = new Path3d();
		path2.moveTo(0f, 0f, 0f);
		path2.quadTo(0f, 1f, 0f, 1f, 1f, 0f);
		path2.closePath();
		
		Path3d path3 = new Path3d();
		path3.moveTo(1f, 1f, 1f);
		path1.lineTo(0f, 0f, 1f);
		path3.closePath();
		
		
		AbstractTriangle3F triangle1 = new Triangle3f(-1,-1,-1, 5,5,0, 2,2,2);
		AbstractTriangle3F triangle2 = new Triangle3d(0,0,0, 1,1,1, 1,1,0);
		//AbstractTriangle3F triangle3 = new Triangle3f();
		
		//FIXME Must Be continued 
		assertFalse(path1.intersects(triangle1));
		assertTrue(path3.intersects(triangle2));
		
		
		//assertTrue(path2.intersects(triangle2));
		
		//assertTrue(path3.intersects(triangle3));
	}
	
	@Test
    public void intersectsOrientedBox() {
		Path3d r = new Path3d();
		r.moveTo(0f, 0f, 0f);
		r.lineTo(1f, 1f, 1f);
		r.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		r.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		r.closePath();
		
		OrientedBox3d aB = new OrientedBox3d(new Point3f(0f,0f,0f),new Vector3f(2f,0f,0f),new Vector3f(0f,2f,0f),2f,2f,2f);
		OrientedBox3f aB2 = new OrientedBox3f(new Point3f(-1f,-1f,-1f),new Vector3f(-2f,0f,0f),new Vector3f(0f,-2f,0f),1f,1f,1f);
		
		assertTrue(r.intersects(aB));
		assertFalse(r.intersects(aB2));
	}
	
	@Test
    public void intersectsPlane3D() {
		Path3d r = new Path3d();
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
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3d p2 = new Path3d();
		p2.moveTo(0f, 0f, 0f);
		p2.lineTo(1f, 1f, 1f);
		p2.closePath();
		
		Path3d p3 = new Path3d();
		p3.moveTo(0f, 0f, 0f);
		p3.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p3.closePath();
		
		Path3d p4 = new Path3d();
		p4.moveTo(0f, 0f, 0f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p4.closePath();
		
		
		assertFalse(p.isPolyline());
		assertTrue(p2.isPolyline());
		assertFalse(p3.isPolyline());
		assertFalse(p4.isPolyline());
	}
	
	@SuppressWarnings("boxing")
	@Test
    public void containsDoubleDoubleDouble() {
		Path3d p = new Path3d();
		p.moveTo(0, 0, 0);
		p.lineTo(1, 1, 1);
		p.quadTo(3, 0, 0, 4, 3, 0.5);
		p.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		p.closePath();
		
		AlignedBox3d box = p.toBoundingBox();
		
		Point3f randomP1 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP2 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP3 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP4 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP5 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP6 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP7 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP8 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP9 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP10 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP11 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP12 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP13 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP14 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP15 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP16 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point3f randomP17 = new Point3f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		
		assertEquals(box.contains(randomP1),p.contains(randomP1.getX(),randomP1.getY(),randomP1.getZ()));
		assertEquals(box.contains(randomP2),p.contains(randomP2.getX(),randomP2.getY(),randomP2.getZ()));
		assertEquals(box.contains(randomP3),p.contains(randomP3.getX(),randomP3.getY(),randomP3.getZ()));
		assertEquals(box.contains(randomP4),p.contains(randomP4.getX(),randomP4.getY(),randomP4.getZ()));
		assertEquals(box.contains(randomP5),p.contains(randomP5.getX(),randomP5.getY(),randomP5.getZ()));
		assertEquals(box.contains(randomP6),p.contains(randomP6.getX(),randomP6.getY(),randomP6.getZ()));
		assertEquals(box.contains(randomP7),p.contains(randomP7.getX(),randomP7.getY(),randomP7.getZ()));
		assertEquals(box.contains(randomP8),p.contains(randomP8.getX(),randomP8.getY(),randomP8.getZ()));
		assertEquals(box.contains(randomP9),p.contains(randomP9.getX(),randomP9.getY(),randomP9.getZ()));
		assertEquals(box.contains(randomP10),p.contains(randomP10.getX(),randomP10.getY(),randomP10.getZ()));
		assertEquals(box.contains(randomP11),p.contains(randomP11.getX(),randomP11.getY(),randomP11.getZ()));
		assertEquals(box.contains(randomP12),p.contains(randomP12.getX(),randomP12.getY(),randomP12.getZ()));
		assertEquals(box.contains(randomP13),p.contains(randomP13.getX(),randomP13.getY(),randomP13.getZ()));
		assertEquals(box.contains(randomP14),p.contains(randomP14.getX(),randomP14.getY(),randomP14.getZ()));
		assertEquals(box.contains(randomP15),p.contains(randomP15.getX(),randomP15.getY(),randomP15.getZ()));
		assertEquals(box.contains(randomP16),p.contains(randomP16.getX(),randomP16.getY(),randomP16.getZ()));
		assertEquals(box.contains(randomP17),p.contains(randomP17.getX(),randomP17.getY(),randomP17.getZ()));
	}
	
	@Test
    public void equals() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3d p3 = p.clone();
		Path3d p2 = new Path3d(p,true);
		
		
		assertTrue(p2.toString().equals(p.toString()));
		assertTrue(p3.toString().equals(p.toString()));
		assertTrue(p3.toString().equals(p2.toString()));
		
		assertTrue(p3.equals(p));
		
		assertTrue(p2.equals(p3));
		assertTrue(p.equals(p2));
	}
	
	
	@Test
    public void removeDoubleDoubleDouble() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		assertTrue(p.remove(5, -1, 0));
		assertTrue(p.getCurrentPoint().equals(new Point3f(4,3,0.5)));
		assertTrue(p.remove(1, 1, 1));
		assertTrue(p.size()==3);
		assertFalse(p.remove(35, 35, 35));
	}
	
	@Test
    public void containsControlPoint3D() {
		Point3f p1 = this.randomPoint3f();
		Point3f p2 = this.randomPoint3f();
		Point3f p3 = this.randomPoint3f();
		Point3f p4 = this.randomPoint3f();
		Point3f p5 = this.randomPoint3f();
		Point3f p6 = this.randomPoint3f();
		Point3f p7 = this.randomPoint3f();
		
		Path3d path = new Path3d();
		path.moveTo(p1.getX(),p1.getY(),p1.getZ());
		path.lineTo(p2.getX(),p2.getY(),p2.getZ());
		path.quadTo(p3.getX(),p3.getY(),p3.getZ(),p4.getX(),p4.getY(),p4.getZ());
		path.curveTo(p5.getX(),p5.getY(),p5.getZ(), p6.getX(),p6.getY(),p6.getZ(), p7.getX(),p7.getY(),p7.getZ());
		path.closePath();
		
		assertTrue(path.containsControlPoint(p1));
		assertTrue(path.containsControlPoint(p2));
		assertTrue(path.containsControlPoint(p3));
		assertTrue(path.containsControlPoint(p4));
		assertTrue(path.containsControlPoint(p5));
		assertTrue(path.containsControlPoint(p6));
		assertTrue(path.containsControlPoint(p7));
		
		p1.add(1000,100,100);
		
		assertFalse(path.containsControlPoint(p1));
	}
	
	@Test
    public void toStringTest() {
		Path3d p = new Path3d();
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
		Path3d p = new Path3d();
		
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
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3d p2 = p.clone();
		
		assertTrue(p.equals(p2));
		assertTrue(p2.equals(p));
	}
	
	@Test
    public void getClosestPointTo() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.lineTo(-1f, 1f, 1f);
		p.lineTo(0.5f, -3f, 7f);
		p.lineTo(6f, 2f, 0f);
		p.lineTo(0f, 5f, 0f);
		p.closePath();
		
		List<Point3f> list = new ArrayList<>();
		
		Point3f randomPoint = new Point3f(this.random.nextDouble()*50-25,this.random.nextDouble()*50-25,this.random.nextDouble()*50-25);
		
		list.add((Point3f) (new Segment3f(0f, 0f, 0f,1f, 1f, 1f)).getClosestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(1f, 1f, 1f,-1f, 1f, 1f)).getClosestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(-1f, 1f, 1f,0.5f, -3f, 7f)).getClosestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(0.5f, -3f, 7f,6f, 2f, 0f)).getClosestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(6f, 2f, 0f,0f, 5f, 0f)).getClosestPointTo(randomPoint));
		
		Point3f closestPoint = new Point3f(0,0,0);
		
		for(Point3f point : list) {
			if (point.getDistance(randomPoint)<closestPoint.getDistance(randomPoint)) {
				closestPoint = point;
			}
		}
		
		assertTrue(closestPoint.equals(p.getClosestPointTo(randomPoint)) || p.getClosestPointTo(randomPoint).getDistance(randomPoint)==closestPoint.distance(randomPoint));
	}
	
	@Test
    public void getFarthestPointTo() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.lineTo(-1f, 1f, 1f);
		p.lineTo(0.5f, -3f, 7f);
		p.lineTo(6f, 2f, 0f);
		p.lineTo(0f, 5f, 0f);
		p.closePath();
		
		List<Point3f> list = new ArrayList<>();
		
		Point3f randomPoint = new Point3f(this.random.nextDouble()*50-25,this.random.nextDouble()*50-25,this.random.nextDouble()*50-25);

		list.add((Point3f) (new Segment3f(0f, 0f, 0f,1f, 1f, 1f)).getFarthestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(1f, 1f, 1f,-1f, 1f, 1f)).getFarthestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(-1f, 1f, 1f,0.5f, -3f, 7f)).getFarthestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(0.5f, -3f, 7f,6f, 2f, 0f)).getFarthestPointTo(randomPoint));
		list.add((Point3f) (new Segment3f(6f, 2f, 0f,0f, 5f, 0f)).getFarthestPointTo(randomPoint));
		
		Point3f farthestPoint = new Point3f(0,0,0);
		
		for(Point3f point : list) {
			if (point.getDistance(randomPoint)>farthestPoint.getDistance(randomPoint)) {
				farthestPoint = point;
			}
		}
		
		assertTrue(farthestPoint.equals(p.getFarthestPointTo(randomPoint))|| p.getFarthestPointTo(randomPoint).getDistance(randomPoint)==farthestPoint.distance(randomPoint));
	}
	
	@Test
    public void toBoundingBox() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3d p1 = new Path3d();
		p1.moveTo(0f, 0f, 0f);
		p1.lineTo(1f, 1f, 1f);
		p1.lineTo(-1f, 1f, 1f);
		p1.lineTo(0.5f, -3f, 7f);
		p1.lineTo(6f, 2f, 0f);
		p1.lineTo(0f, 5f, 0f);
		p1.closePath();
		
		Path3d p2 = new Path3d();
		p2.moveTo(-10f, -10f, -10f);
		p2.lineTo(4f, 7f, 3f);
		p2.lineTo(-3f, -7f, 5f);
		p2.lineTo(0.5f, -4f, 7f);
		p2.lineTo(6f, -9f, 0f);
		p2.lineTo(0f, 3f, 3f);
		p2.closePath();
		
		AlignedBox3d pBox = new AlignedBox3d(new Point3f(0,-5,0),new Point3f(7,5,1));
		AlignedBox3d p1Box = new AlignedBox3d(new Point3f(-1,-3,0),new Point3f(6,5,7));
		AlignedBox3d p2Box = new AlignedBox3d(new Point3f(-10,-10,-10),new Point3f(6,7,7));
		
		assertTrue(pBox.equals(p.toBoundingBoxWithCtrlPoints()));
		assertTrue(p1Box.equals(p1.toBoundingBox()));
		assertTrue(p2Box.equals(p2.toBoundingBox()));
	}
	
	@Test
    public void distanceSquared() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3d p1 = new Path3d();
		p1.moveTo(0f, 0f, 0f);
		p1.lineTo(1f, 1f, 1f);
		p1.lineTo(-1f, 1f, 1f);
		p1.lineTo(0.5f, -3f, 7f);
		p1.lineTo(6f, 2f, 0f);
		p1.lineTo(0f, 5f, 0f);
		p1.closePath();
		
		Path3d p2 = new Path3d();
		p2.moveTo(-10f, -10f, -10f);
		p2.lineTo(4f, 7f, 3f);
		p2.lineTo(-3f, -7f, 5f);
		p2.lineTo(0.5f, -4f, 7f);
		p2.lineTo(6f, -9f, 0f);
		p2.lineTo(0f, 3f, 3f);
		p2.closePath();
		
		Point3d randomPoint = new Point3d(randomPoint3f());
		
		Point3d closest =  p.getClosestPointTo(randomPoint);
		Point3d closest1 =  p1.getClosestPointTo(randomPoint);
		Point3d closest2 =  p2.getClosestPointTo(randomPoint);
		
		assertEpsilonEquals(closest.getDistanceSquared(randomPoint),p.distanceSquared(randomPoint));
		assertEpsilonEquals(closest1.getDistanceSquared(randomPoint),p1.distanceSquared(randomPoint));
		assertEpsilonEquals(closest2.getDistanceSquared(randomPoint),p2.distanceSquared(randomPoint));
	}
	
	@Test
    public void distanceL1() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3d p1 = new Path3d();
		p1.moveTo(0f, 0f, 0f);
		p1.lineTo(1f, 1f, 1f);
		p1.lineTo(-1f, 1f, 1f);
		p1.lineTo(0.5f, -3f, 7f);
		p1.lineTo(6f, 2f, 0f);
		p1.lineTo(0f, 5f, 0f);
		p1.closePath();
		
		Path3d p2 = new Path3d();
		p2.moveTo(-10f, -10f, -10f);
		p2.lineTo(4f, 7f, 3f);
		p2.lineTo(-3f, -7f, 5f);
		p2.lineTo(0.5f, -4f, 7f);
		p2.lineTo(6f, -9f, 0f);
		p2.lineTo(0f, 3f, 3f);
		p2.closePath();
		
		Point3d randomPoint = new Point3d(randomPoint3f());
		
		Point3d closest = p.getClosestPointTo(randomPoint);
		Point3d closest1 = p1.getClosestPointTo(randomPoint);
		Point3d closest2 = p2.getClosestPointTo(randomPoint);
		
		assertEpsilonEquals(closest.getDistanceL1(randomPoint),p.distanceL1(randomPoint));
		assertEpsilonEquals(closest1.getDistanceL1(randomPoint),p1.distanceL1(randomPoint));
		assertEpsilonEquals(closest2.getDistanceL1(randomPoint),p2.distanceL1(randomPoint));
	}
	
	@Test
    public void distanceLinf() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3d p1 = new Path3d();
		p1.moveTo(0f, 0f, 0f);
		p1.lineTo(1f, 1f, 1f);
		p1.lineTo(-1f, 1f, 1f);
		p1.lineTo(0.5f, -3f, 7f);
		p1.lineTo(6f, 2f, 0f);
		p1.lineTo(0f, 5f, 0f);
		p1.closePath();
		
		Path3d p2 = new Path3d();
		p2.moveTo(-10f, -10f, -10f);
		p2.lineTo(4f, 7f, 3f);
		p2.lineTo(-3f, -7f, 5f);
		p2.lineTo(0.5f, -4f, 7f);
		p2.lineTo(6f, -9f, 0f);
		p2.lineTo(0f, 3f, 3f);
		p2.closePath();
		
		Point3d randomPoint = new Point3d(randomPoint3f());
		
		Point3d closest =  p.getClosestPointTo(randomPoint);
		Point3d closest1 =  p1.getClosestPointTo(randomPoint);
		Point3d closest2 =  p2.getClosestPointTo(randomPoint);
		
		assertEpsilonEquals(closest.getDistanceLinf(randomPoint),p.distanceLinf(randomPoint));
		assertEpsilonEquals(closest1.getDistanceLinf(randomPoint),p1.distanceLinf(randomPoint));
		assertEpsilonEquals(closest2.getDistanceLinf(randomPoint),p2.distanceLinf(randomPoint));
	}
	
	@Test
    public void transform() {
		Point3f p1 = this.randomPoint3f();
		Point3f p2 = this.randomPoint3f();
		Point3f p3 = this.randomPoint3f();
		Point3f p4 = this.randomPoint3f();
		Point3f p5 = this.randomPoint3f();
		Point3f p6 = this.randomPoint3f();
		Point3f p7 = this.randomPoint3f();
		
		Path3d path = new Path3d();
		path.moveTo(p1.getX(),p1.getY(),p1.getZ());
		path.lineTo(p2.getX(),p2.getY(),p2.getZ());
		path.quadTo(p3.getX(),p3.getY(),p3.getZ(),p4.getX(),p4.getY(),p4.getZ());
		path.curveTo(p5.getX(),p5.getY(),p5.getZ(), p6.getX(),p6.getY(),p6.getZ(), p7.getX(),p7.getY(),p7.getZ());
		path.closePath();
		
		Transform3D trans = new Transform3D(this.randomMatrix4f());
		
		trans.transform(p1);
		trans.transform(p2);
		trans.transform(p3);
		trans.transform(p4);
		trans.transform(p5);
		trans.transform(p6);
		trans.transform(p7);
		
		Path3d pathTrans = new Path3d();
		pathTrans.moveTo(p1.getX(),p1.getY(),p1.getZ());
		pathTrans.lineTo(p2.getX(),p2.getY(),p2.getZ());
		pathTrans.quadTo(p3.getX(),p3.getY(),p3.getZ(),p4.getX(),p4.getY(),p4.getZ());
		pathTrans.curveTo(p5.getX(),p5.getY(),p5.getZ(), p6.getX(),p6.getY(),p6.getZ(), p7.getX(),p7.getY(),p7.getZ());
		pathTrans.closePath();
		
		path.transform(trans);
		
		assertTrue(path.equals(pathTrans));
	}
	
	@Test
    public void translate() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		double dx = this.random.nextDouble()*20;
		double dy = this.random.nextDouble()*20;
		double dz = this.random.nextDouble()*20;
		
		Path3d p2 = new Path3d();
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
		Point3f p1 = this.randomPoint3f();
		Point3f p2 = this.randomPoint3f();
		Point3f p3 = this.randomPoint3f();
		Point3f p4 = this.randomPoint3f();
		Point3f p5 = this.randomPoint3f();
		Point3f p6 = this.randomPoint3f();
		Point3f p7 = this.randomPoint3f();
		
		Path3d path = new Path3d();
		path.moveTo(p1.getX(),p1.getY(),p1.getZ());
		path.lineTo(p2.getX(),p2.getY(),p2.getZ());
		path.quadTo(p3.getX(),p3.getY(),p3.getZ(),p4.getX(),p4.getY(),p4.getZ());
		path.curveTo(p5.getX(),p5.getY(),p5.getZ(), p6.getX(),p6.getY(),p6.getZ(), p7.getX(),p7.getY(),p7.getZ());
		path.closePath();
		
		Transform3D trans = new Transform3D(this.randomMatrix4f());
		
		Path3d transformedShape = (Path3d) path.createTransformedShape(trans);
		
		path.transform(trans);
		
		assertTrue(path.equals(transformedShape));
	}
	
	@Test
    public void add() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		
		Path3d p2 = new Path3d();
		p2.moveTo(7f, -5f, 0f);
		p2.lineTo(4f, 6f, 7f);
		p2.lineTo(0f, 8f, 3.5);
		p2.lineTo(5f, -3f, 2f);
		p2.closePath();
		
		Path3d p3 = new Path3d();
		p3.moveTo(0f, 0f, 0f);
		p3.lineTo(1f, 1f, 1f);
		p3.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p3.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p3.lineTo(4f, 6f, 7f);
		p3.lineTo(0f, 8f, 3.5);
		p3.lineTo(5f, -3f, 2f);
		p3.closePath();
		
		PathIterator3d iterator = p2.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);
		iterator.next();
		p.add(iterator);
		
		assertTrue(p.equals(p3));
		
	}
	
	@Test
    public void removeLast() {
		Path3d p = new Path3d();
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
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		assertTrue(p.getCurrentPoint().equals(new Point3f(7,-5,0)));
		
		p.setLastPoint(2, 2, 2);
		
		assertTrue(p.getCurrentPoint().equals(new Point3f(2,2,2)));
	}
	
	@Test
    public void sizeTest() {
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		p.lineTo(1f, 1f, 1f);
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		p.closePath();
		
		Path3d p2 = new Path3d();
		p2.moveTo(0f, 0f, 0f);
		p2.lineTo(1f, 1f, 1f);
		p2.lineTo(2f, 1f, 1f);
		p2.lineTo(3f, 1f, 1f);
		p2.lineTo(4f, 1f, 1f);
		p2.lineTo(5f, 1f, 1f);
		p2.closePath();
		
		Path3d p3 = new Path3d();
		p3.moveTo(0f, 0f, 0f);
		p3.quadTo(4f, 3f, 0.5, 1f, 1f, 1f);
		p3.quadTo(3f, 3f, 0.5, 2f, 1f, 1f);
		p3.quadTo(5f, 3f, 0.5, 3f, 1f, 1f);
		p3.closePath();
		
		Path3d p4 = new Path3d();
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
		Path3d p = new Path3d();
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
		Path3d p = new Path3d();
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
		Path3d p = new Path3d();
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
		Path3d p = new Path3d();
		p.moveTo(0f, 0f, 0f);
		assertTrue(p.getCurrentPoint().equals(new Point3f(0,0,0)));
		p.lineTo(1f, 1f, 1f);
		assertTrue(p.getCurrentPoint().equals(new Point3f(1,1,1)));
		p.quadTo(3f, 0f, 0f, 4f, 3f, 0.5);
		assertTrue(p.getCurrentPoint().equals(new Point3f(4,3,0.5)));
		p.curveTo(5f, -1f, 0f, 6f, 5f, 0f, 7f, -5f, 0f);
		assertTrue(p.getCurrentPoint().equals(new Point3f(7,-5,0)));
		p.closePath();
		
		assertTrue(p.getCurrentPoint().equals(new Point3f(7,-5,0)));
	}
	
	
}
