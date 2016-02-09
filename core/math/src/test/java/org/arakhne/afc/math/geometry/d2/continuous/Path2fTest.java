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
package org.arakhne.afc.math.geometry.d2.continuous;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.junit.Test;

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"static-method"})
public class Path2fTest extends AbstractMathTestCase{
	
	
	@Test
    public void intersectsRectangle() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		p.closePath();
		
		
		Rectangle2f aB = new Rectangle2f(0,0,2,2);
		Rectangle2d aB2 = new Rectangle2d(new Point2d(-1f,-1f),new Point2d(-2f,-2f));
		
		assertTrue(p.intersects(aB));
		assertFalse(p.intersects(aB2));
   }

	
	@Test
    public void intersectsPath() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		p.closePath();
		
		
		Transform2D t = new Transform2D();
		
		Path2f rTrans = new Path2f();
		rTrans.moveTo(-1f, -1f);
		rTrans.lineTo(8f, -4f);
		rTrans.closePath();
		
		Path2f rCurv = new Path2f();
		rCurv.moveTo(0f, 0f);
		rCurv.curveTo(5,-1f,-5f,1,-12f, 19f);
		rCurv.closePath();
		
		assertTrue(p.intersects(p));
		
		assertTrue(rTrans.intersects(p));
		assertTrue(rCurv.intersects(p));
	
		t.makeTranslationMatrix(20, 20);
		rTrans.transform(t);
		rCurv.transform(t);
		
		assertFalse(rTrans.intersects(p));
		assertFalse(rCurv.intersects(p));
	}
	
	@Test
    public void intersectsCircle() {
		Path2f r = new Path2f();
		r.moveTo(0f, 0f);
		r.lineTo(1f, 1f);
		r.quadTo(3f, 0f, 4f, 3f);
		r.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		r.closePath();
		
		Circle2f sfere = new Circle2f(new Point2f(0.5f,0.5f),5f);
		
		assertTrue(r.intersects(sfere));
		
		Circle2f sfere2 = new Circle2f(new Point2f(20f,20f),0.001);

		assertFalse(r.intersects(sfere2));
		
	}
	
	@Test
    public void intersectsSegment() {
		Path2f r = new Path2f();
		r.moveTo(0f, 0f);
		r.lineTo(1f, 1f);
		r.quadTo(3f, 0f, 4f, 3f);
		r.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		r.closePath();
		
		Segment2f segm = new Segment2f(1,0,0,1);
		Segment2f s = new Segment2f(1,0,-1,-1);
		Segment2f segm2 = new Segment2f(new Point2f(20f,20f),new Point2f(30f,30f));
		Segment2f s2 = new Segment2f(2,2,8,-4);
		Segment2f s3 = new Segment2f(1,0,7,0);
		Segment2f s4 = new Segment2f(3,3,3,0);
		Segment2f s5 = new Segment2f(4,0,0,-1);
		
		
		//FIXME Problem with Path segment intersection ----> segment extended by the right side
		assertTrue(r.intersects(segm));
		assertFalse(r.intersects(s));
		assertFalse(r.intersects(segm2));
		assertTrue(r.intersects(s2));
		assertTrue(r.intersects(s3));
		assertTrue(r.intersects(s4));
		assertFalse(r.intersects(s5));
		
	}
	
//	@Test
//    public void intersectsPathIterator() {
//		Path3f path1 = new Path3f();
//		path1.moveTo(0f, 0f, 0f);
//		path1.lineTo(1f, 1f, 1f);
//		path1.closePath();
//		
//		Path3f path2 = new Path3f();
//		path2.moveTo(0f, 0f, 0f);
//		path2.quadTo(0f, 1f, 0f, 1f, 1f, 0f);
//		path2.closePath();
//		
//		Path3f path3 = new Path3f();
//		path3.moveTo(1f, 1f, 1f);
//		path1.lineTo(0f, 0f, 1f);
//		path3.closePath();
//		
//		
//		AbstractTriangle3F triangle1 = new Triangle3f(-1,-1,-1, 5,5,0, 2,2,2);
//		AbstractTriangle3F triangle2 = new Triangle3f(0,0,0, 1,1,1, 1,1,0);
//		//AbstractTriangle3F triangle3 = new Triangle3f();
//		
//		//FIXME Must Be continued 
//		assertFalse(path1.intersects(triangle1));
//		assertTrue(path3.intersects(triangle2));
//		
//		
//		//assertTrue(path2.intersects(triangle2));
//		
//		//assertTrue(path3.intersects(triangle3));
//	}
//	
	@Test
    public void intersectsEllipse() {
		Path2f r = new Path2f();
		r.moveTo(0f, 0f);
		r.lineTo(1f, 1f);
		r.quadTo(3f, 0f, 4f, 3f);
		r.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		r.closePath();
		
		Ellipse2f ellipse = new Ellipse2f(new Point2f(1,0), new Point2f(3,2));
		Ellipse2f ellipse2 = new Ellipse2f();
		ellipse2.setFromCenter(4, 3, 3, 2.5);
		Ellipse2f ellipse3 = new Ellipse2f();
		ellipse3.setFromCenter(0, -3, -2, -4);
		
		assertTrue(r.intersects(ellipse));
		assertTrue(r.intersects(ellipse2));	
		assertFalse(r.intersects(ellipse3));
	}
	
	@Test
	public void isPolyline() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		p.closePath();
		

		Path2f p2 = new Path2f();
		p2.moveTo(0f, 0f);
		p2.lineTo(1f, 1f);
		p2.closePath();

		Path2f p3 = new Path2f();
		p3.moveTo(0f, 0f);
		p3.quadTo(3f, 0f, 4f, 3f);
		p3.closePath();

		Path2f p4 = new Path2f();
		p4.moveTo(0f, 0f);
		p4.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		p4.closePath();

		assertFalse(p.isPolyline());
		assertTrue(p2.isPolyline());
		assertFalse(p3.isPolyline());
		assertFalse(p4.isPolyline());
	}
	
	@SuppressWarnings("boxing")
	@Test
    public void containsDoubleDoubleDouble() {
		Path2f p = new Path2f();
		p.moveTo(0, 0);
		p.lineTo(1, 1);
		p.quadTo(3, 0, 4, 3);
		p.curveTo(5, -1, 6, 5, 7, -5);
		p.closePath();
		
		Rectangle2f box = p.toBoundingBox();
		
		Point2f randomP1 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP2 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP3 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP4 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP5 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP6 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP7 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP8 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP9 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP10 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP11 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP12 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP13 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP14 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP15 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP16 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		Point2f randomP17 = new Point2f(this.random.nextDouble()*20-10,this.random.nextDouble()*20-10);
		
		assertEquals(box.contains(randomP1),p.contains(randomP1.getX(),randomP1.getY()));
		assertEquals(box.contains(randomP2),p.contains(randomP2.getX(),randomP2.getY()));
		assertEquals(box.contains(randomP3),p.contains(randomP3.getX(),randomP3.getY()));
		assertEquals(box.contains(randomP4),p.contains(randomP4.getX(),randomP4.getY()));
		assertEquals(box.contains(randomP5),p.contains(randomP5.getX(),randomP5.getY()));
		assertEquals(box.contains(randomP6),p.contains(randomP6.getX(),randomP6.getY()));
		assertEquals(box.contains(randomP7),p.contains(randomP7.getX(),randomP7.getY()));
		assertEquals(box.contains(randomP8),p.contains(randomP8.getX(),randomP8.getY()));
		assertEquals(box.contains(randomP9),p.contains(randomP9.getX(),randomP9.getY()));
		assertEquals(box.contains(randomP10),p.contains(randomP10.getX(),randomP10.getY()));
		assertEquals(box.contains(randomP11),p.contains(randomP11.getX(),randomP11.getY()));
		assertEquals(box.contains(randomP12),p.contains(randomP12.getX(),randomP12.getY()));
		assertEquals(box.contains(randomP13),p.contains(randomP13.getX(),randomP13.getY()));
		assertEquals(box.contains(randomP14),p.contains(randomP14.getX(),randomP14.getY()));
		assertEquals(box.contains(randomP15),p.contains(randomP15.getX(),randomP15.getY()));
		assertEquals(box.contains(randomP16),p.contains(randomP16.getX(),randomP16.getY()));
		assertEquals(box.contains(randomP17),p.contains(randomP17.getX(),randomP17.getY()));
		
		
	}
	
	@Test
    public void equals() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		p.closePath();
		
		Path2f p3 = p.clone();
		Path2f p2 = new Path2f(p);
		
		
		assertTrue(p2.toString().equals(p.toString()));
		assertTrue(p3.toString().equals(p.toString()));
		assertTrue(p3.toString().equals(p2.toString()));
		
		assertTrue(p3.equals(p));
		
		assertTrue(p2.equals(p3));
		assertTrue(p.equals(p2));
	}
	
	
	@Test
    public void removeDoubleDoubleDouble() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		p.closePath();
		
		assertTrue(p.remove(5, -1));
		assertTrue(p.getCurrentPoint().equals(new Point2f(4,3)));
		assertTrue(p.remove(1, 1));
		assertTrue(p.size()==3);
		assertFalse(p.remove(35, 35));
	}
	
	@Test
    public void containsControlPoint2D() {
		Point2f p1 = this.randomPoint2f();
		Point2f p2 = this.randomPoint2f();
		Point2f p3 = this.randomPoint2f();
		Point2f p4 = this.randomPoint2f();
		Point2f p5 = this.randomPoint2f();
		Point2f p6 = this.randomPoint2f();
		Point2f p7 = this.randomPoint2f();
		
		Path2f path = new Path2f();
		path.moveTo(p1.getX(),p1.getY());
		path.lineTo(p2.getX(),p2.getY());
		path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		path.closePath();
		
		assertTrue(path.containsControlPoint(p1));
		assertTrue(path.containsControlPoint(p2));
		assertTrue(path.containsControlPoint(p3));
		assertTrue(path.containsControlPoint(p4));
		assertTrue(path.containsControlPoint(p5));
		assertTrue(path.containsControlPoint(p6));
		assertTrue(path.containsControlPoint(p7));
		
		p1.add(1000,100);
		
		assertFalse(path.containsControlPoint(p1));
	}
	
	@Test
    public void toStringTest() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		p.closePath();
		
		assertTrue(p.toString().equals(
				"[0.0, 0.0, " //$NON-NLS-1$
				+ "1.0, 1.0, " //$NON-NLS-1$
				+ "3.0, 0.0, 4.0, 3.0, " //$NON-NLS-1$
				+ "5.0, -1.0, 6.0, 5.0, 7.0, -5.0]")); //$NON-NLS-1$
	}
	
	@Test
    public void isEmptyAndClear() {
		Path2f p = new Path2f();
		
		assertTrue(p.isEmpty());
		
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		p.closePath();
		
		assertFalse(p.isEmpty());
	
		p.clear();
		
		assertTrue(p.isEmpty());
	}
		
	@Test
    public void cloneTest() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f,7f, -5f);
		p.closePath();
		
		Path2f p2 = p.clone();
		
		assertTrue(p.equals(p2));
		assertTrue(p2.equals(p));
	}
	
	@Test
	public void getClosestPointTo() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.lineTo(-1f, 1f);
		p.lineTo(0.5f, -3f);
		p.lineTo(6f, 2f);
		p.lineTo(0f, 5f);
		p.closePath();

		List<Point2f> list = new ArrayList<>();

		Point2f randomPoint = new Point2f(this.random.nextDouble()*50-25,this.random.nextDouble()*50-25);

		list.add((Point2f) (new Segment2f(0f, 0f,1f, 1f)).getClosestPointTo(randomPoint));
		list.add((Point2f) (new Segment2f(1f, 1f,-1f, 1f)).getClosestPointTo(randomPoint));
		list.add((Point2f) (new Segment2f(-1f, 1f,0.5f, -3f)).getClosestPointTo(randomPoint));
		list.add((Point2f) (new Segment2f(0.5f, -3f,6f, 2f)).getClosestPointTo(randomPoint));
		list.add((Point2f) (new Segment2f(6f, 2f,0f, 5f)).getClosestPointTo(randomPoint));

		Point2f closestPoint = new Point2f(0,0);

		for(Point2f point : list) {
			if (point.getDistance(randomPoint)<closestPoint.getDistance(randomPoint)) {
				closestPoint = point;
			}
		}

		assertTrue(closestPoint.equals(p.getClosestPointTo(randomPoint))|| p.getClosestPointTo(randomPoint).getDistance(randomPoint)==closestPoint.getDistance(randomPoint));
	}

	@Test
    public void getFarthestPointTo() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.lineTo(-1f, 1f);
		p.lineTo(0.5f, -3f);
		p.lineTo(6f, 2f);
		p.lineTo(0f, 5f);
		p.closePath();
		
		List<Point2f> list = new ArrayList<>();
		
		Point2f randomPoint = new Point2f(this.random.nextDouble()*50-25,this.random.nextDouble()*50-25);

		list.add((Point2f) (new Segment2f(0f, 0f,1f, 1f)).getFarthestPointTo(randomPoint));
		list.add((Point2f) (new Segment2f(1f, 1f,-1f, 1f)).getFarthestPointTo(randomPoint));
		list.add((Point2f) (new Segment2f(-1f, 1f,0.5f, -3f)).getFarthestPointTo(randomPoint));
		list.add((Point2f) (new Segment2f(0.5f, -3f,6f, 2f)).getFarthestPointTo(randomPoint));
		list.add((Point2f) (new Segment2f(6f, 2f,0f, 5f)).getFarthestPointTo(randomPoint));
		
		Point2f farthestPoint = new Point2f(0,0);
	
		for(Point2f point : list) {
			if (point.getDistance(randomPoint)>farthestPoint.getDistance(randomPoint)) {
				farthestPoint = point;
			}
		}
		
		assertTrue(farthestPoint.equals(p.getFarthestPointTo(randomPoint))|| p.getFarthestPointTo(randomPoint).getDistance(randomPoint)==farthestPoint.getDistance(randomPoint));
	}
	
	//FIXME PROBLEM HERE WITH THE INSTANCIANTION OF RECTANGLES
	@Test
    public void toBoundingBox() {
		Path2f p = new Path2f();
		p.moveTo( 0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		Path2f p1 = new Path2f();
		p1.moveTo(0f, 0f);
		p1.lineTo(1f, 1f);
		p1.lineTo(-1f, 1f);
		p1.lineTo(0.5f, -3f);
		p1.lineTo(6f, 2f);
		p1.lineTo(0f, 5f);
		p1.closePath();
		
		Path2f p2 = new Path2f();
		p2.moveTo(-10f, -10f);
		p2.lineTo(4f, 7f);
		p2.lineTo(-3f, -7f);
		p2.lineTo(0.5f, -4f);
		p2.lineTo(6f, -9f);
		p2.lineTo(0f, 3f);
		p2.closePath();
		
		Rectangle2f pBox = new Rectangle2f(new Point2f(0,-5),new Point2f(7,5));
		Rectangle2f p1Box = new Rectangle2f(new Point2f(-1,-3),new Point2f(6,5));
		Rectangle2f p2Box = new Rectangle2f(new Point2f(-10,-10),new Point2f(6,7));
		
	/*	System.out.println(pBox.toString());
		System.out.println(p.toBoundingBoxWithCtrlPoints());
		System.out.println();
		System.out.println(p1Box);
		System.out.println(p1.toBoundingBoxWithCtrlPoints());
		System.out.println();
		System.out.println(p2Box);
		System.out.println(p2.toBoundingBoxWithCtrlPoints());
		*/
		assertTrue(pBox.equals(p.toBoundingBoxWithCtrlPoints()));
		assertTrue(p1Box.equals(p1.toBoundingBox()));
		assertTrue(p2Box.equals(p2.toBoundingBox()));
	}
	
	@Test
    public void distanceSquared() {
		Path2f p = new Path2f();
		p.moveTo( 0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		Path2f p1 = new Path2f();
		p1.moveTo(0f, 0f);
		p1.lineTo(1f, 1f);
		p1.lineTo(-1f, 1f);
		p1.lineTo(0.5f, -3f);
		p1.lineTo(6f, 2f);
		p1.lineTo(0f, 5f);
		p1.closePath();
		
		Path2f p2 = new Path2f();
		p2.moveTo(-10f, -10f);
		p2.lineTo(4f, 7f);
		p2.lineTo(-3f, -7f);
		p2.lineTo(0.5f, -4f);
		p2.lineTo(6f, -9f);
		p2.lineTo(0f, 3f);
		p2.closePath();
		
		Point2f randomPoint = randomPoint2f();
		
		Point2f closest = (Point2f) p.getClosestPointTo(randomPoint);
		Point2f closest1 = (Point2f) p1.getClosestPointTo(randomPoint);
		Point2f closest2 = (Point2f) p2.getClosestPointTo(randomPoint);
		
		assertEpsilonEquals(closest.getDistanceSquared(randomPoint),p.distanceSquared(randomPoint));
		assertEpsilonEquals(closest1.getDistanceSquared(randomPoint),p1.distanceSquared(randomPoint));
		assertEpsilonEquals(closest2.getDistanceSquared(randomPoint),p2.distanceSquared(randomPoint));
	}
	
	@Test
    public void distanceL1() {
		Path2f p = new Path2f();
		p.moveTo( 0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		Path2f p1 = new Path2f();
		p1.moveTo(0f, 0f);
		p1.lineTo(1f, 1f);
		p1.lineTo(-1f, 1f);
		p1.lineTo(0.5f, -3f);
		p1.lineTo(6f, 2f);
		p1.lineTo(0f, 5f);
		p1.closePath();
		
		Path2f p2 = new Path2f();
		p2.moveTo(-10f, -10f);
		p2.lineTo(4f, 7f);
		p2.lineTo(-3f, -7f);
		p2.lineTo(0.5f, -4f);
		p2.lineTo(6f, -9f);
		p2.lineTo(0f, 3f);
		p2.closePath();
		
		Point2f randomPoint = randomPoint2f();
		
		Point2f closest = (Point2f) p.getClosestPointTo(randomPoint);
		Point2f closest1 = (Point2f) p1.getClosestPointTo(randomPoint);
		Point2f closest2 = (Point2f) p2.getClosestPointTo(randomPoint);
		
		assertEpsilonEquals(closest.getDistanceL1(randomPoint),p.distanceL1(randomPoint));
		assertEpsilonEquals(closest1.getDistanceL1(randomPoint),p1.distanceL1(randomPoint));
		assertEpsilonEquals(closest2.getDistanceL1(randomPoint),p2.distanceL1(randomPoint));
	}
	
	@Test
    public void distanceLinf() {
		Path2f p = new Path2f();
		p.moveTo( 0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		Path2f p1 = new Path2f();
		p1.moveTo(0f, 0f);
		p1.lineTo(1f, 1f);
		p1.lineTo(-1f, 1f);
		p1.lineTo(0.5f, -3f);
		p1.lineTo(6f, 2f);
		p1.lineTo(0f, 5f);
		p1.closePath();
		
		Path2f p2 = new Path2f();
		p2.moveTo(-10f, -10f);
		p2.lineTo(4f, 7f);
		p2.lineTo(-3f, -7f);
		p2.lineTo(0.5f, -4f);
		p2.lineTo(6f, -9f);
		p2.lineTo(0f, 3f);
		p2.closePath();
		
		Point2f randomPoint = randomPoint2f();
		
		Point2f closest = (Point2f) p.getClosestPointTo(randomPoint);
		Point2f closest1 = (Point2f) p1.getClosestPointTo(randomPoint);
		Point2f closest2 = (Point2f) p2.getClosestPointTo(randomPoint);
		
		assertEpsilonEquals(closest.getDistanceLinf(randomPoint),p.distanceLinf(randomPoint));
		assertEpsilonEquals(closest1.getDistanceLinf(randomPoint),p1.distanceLinf(randomPoint));
		assertEpsilonEquals(closest2.getDistanceLinf(randomPoint),p2.distanceLinf(randomPoint));
	}
	
	@Test
    public void transform() {
		Point2f p1 = this.randomPoint2f();
		Point2f p2 = this.randomPoint2f();
		Point2f p3 = this.randomPoint2f();
		Point2f p4 = this.randomPoint2f();
		Point2f p5 = this.randomPoint2f();
		Point2f p6 = this.randomPoint2f();
		Point2f p7 = this.randomPoint2f();
		
		Path2f path = new Path2f();
		path.moveTo(p1.getX(),p1.getY());
		path.lineTo(p2.getX(),p2.getY());
		path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		path.closePath();
		
		Transform2D trans = new Transform2D(this.randomMatrix3f());
		
		trans.transform(p1);
		trans.transform(p2);
		trans.transform(p3);
		trans.transform(p4);
		trans.transform(p5);
		trans.transform(p6);
		trans.transform(p7);
		
		Path2f pathTrans = new Path2f();
		pathTrans.moveTo(p1.getX(),p1.getY());
		pathTrans.lineTo(p2.getX(),p2.getY());
		pathTrans.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		pathTrans.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		pathTrans.closePath();
		
		path.transform(trans);
		
		assertTrue(path.equals(pathTrans));
	}
	
	@Test
    public void translate() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		
		double dx = this.random.nextDouble()*20;
		double dy = this.random.nextDouble()*20;
		
		Path2f p2 = new Path2f();
		p2.moveTo(0f+dx, 0f+dy);
		p2.lineTo(1f+dx, 1f+dy);
		p2.quadTo(3f+dx, 0f+dy, 4f+dx, 3f+dy);
		p2.curveTo(5f+dx, -1f+dy, 6f+dx, 5f+dy, 7f+dx, -5f+dy);
		
		p.translate(dx, dy);
		
		assertTrue(p.equals(p2));		
	}
	
	@Test
	public void createTransformedShape() {
		Point2f p1 = this.randomPoint2f();
		Point2f p2 = this.randomPoint2f();
		Point2f p3 = this.randomPoint2f();
		Point2f p4 = this.randomPoint2f();
		Point2f p5 = this.randomPoint2f();
		Point2f p6 = this.randomPoint2f();
		Point2f p7 = this.randomPoint2f();
		
		Path2f path = new Path2f();
		path.moveTo(p1.getX(),p1.getY());
		path.lineTo(p2.getX(),p2.getY());
		path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		path.closePath();
		
		Transform2D trans = new Transform2D(this.randomMatrix3f());
		
		Path2f transformedShape = (Path2f) path.createTransformedShape(trans);
		
		path.transform(trans);
	
		assertTrue(path.equals(transformedShape));
	}
	
	@Test
    public void add() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		
		Path2f p2 = new Path2f();
		p2.moveTo(7f, -5f);
		p2.lineTo(4f, 6f);
		p2.lineTo(0f, 8f);
		p2.lineTo(5f, -3f);
		p2.closePath();
		
		Path2f p3 = new Path2f();
		p3.moveTo(0f, 0f);
		p3.lineTo(1f, 1f);
		p3.quadTo(3f, 0f, 4f, 3f);
		p3.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p3.lineTo(4f, 6f);
		p3.lineTo(0f, 8f);
		p3.lineTo(5f, -3f);
		p3.closePath();
		
		PathIterator2f iterator = p2.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		iterator.next();
		p.add(iterator);
		
		assertTrue(p.equals(p3));
		
	}
	
	@Test
    public void removeLast() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
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
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		assertTrue(p.getCurrentPoint().equals(new Point2f(7,-5)));
		
		p.setLastPoint(2, 2);
		
		assertTrue(p.getCurrentPoint().equals(new Point2f(2,2)));
	}
	
	@Test
    public void sizeTest() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		Path2f p2 = new Path2f();
		p2.moveTo(0f, 0f);
		p2.lineTo(1f, 1f);
		p2.lineTo(2f, 1f);
		p2.lineTo(3f, 1f);
		p2.lineTo(4f, 1f);
		p2.lineTo(5f, 1f);
		p2.closePath();
	
		Path2f p3 = new Path2f();
		p3.moveTo(0f, 0f);
		p3.quadTo(4f, 3f, 1f, 1f);
		p3.quadTo(3f, 3f, 2f, 1f);
		p3.quadTo(5f, 3f, 0.5, 3f);
		p3.closePath();
		
		Path2f p4 = new Path2f();
		p4.moveTo(0f, 0f);
		p4.curveTo(5f, -1f, 0f, 6f, 5f, 0f);
		p4.curveTo(6f, 5f, 0f, 10f, -5f, 20f);
		p4.curveTo(6f, 5f, 0f, 5f, -5f, 30f);
		p4.curveTo(6f, 5f, 0f, 3f, -7f, 11f);
		p4.curveTo(6f, 5f, 0f, 9f, -10f, 16f);
		p4.curveTo(6f, 5f, 0f, 0f, -10f, 1f);
		p4.curveTo(6f, 5f, 0f, 3f, 3f, 3f);
		p.closePath();
		
		assertEpsilonEquals(p.size(),7);
		assertEpsilonEquals(p2.size(),6);
		assertEpsilonEquals(p3.size(),7);
		assertEpsilonEquals(p4.size(),22);		
}
	
	@Test
    public void toDoubleArrayTest() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		double [] array = p.toDoubleArray();
		
		assertTrue(new Point2f(array[0],array[1]).equals(new Point2f(0,0)));
		assertTrue(new Point2f(array[2],array[3]).equals(new Point2f(1,1)));
		assertTrue(new Point2f(array[4],array[5]).equals(new Point2f(3,0)));
		assertTrue(new Point2f(array[6],array[7]).equals(new Point2f(4,3)));
		assertTrue(new Point2f(array[8],array[9]).equals(new Point2f(5,-1)));
		assertTrue(new Point2f(array[10],array[11]).equals(new Point2f(6,5)));
		assertTrue(new Point2f(array[12],array[13]).equals(new Point2f(7,-5)));
	}
	
	@Test
    public void toPointArrayTest() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		Point2D [] array = p.toPointArray();
		
		assertTrue(array[0].equals(new Point2f(0,0)));
		assertTrue(array[1].equals(new Point2f(1,1)));
		assertTrue(array[2].equals(new Point2f(3,0)));
		assertTrue(array[3].equals(new Point2f(4,3)));
		assertTrue(array[4].equals(new Point2f(5,-1)));
		assertTrue(array[5].equals(new Point2f(6,5)));
		assertTrue(array[6].equals(new Point2f(7,-5)));
	}
	
	@Test
    public void getPointAt() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		assertTrue(p.getPointAt(0).equals(new Point2f(0,0)));
		assertTrue(p.getPointAt(1).equals(new Point2f(1,1)));
		assertTrue(p.getPointAt(2).equals(new Point2f(3,0)));
		assertTrue(p.getPointAt(3).equals(new Point2f(4,3)));
		assertTrue(p.getPointAt(4).equals(new Point2f(5,-1)));
		assertTrue(p.getPointAt(5).equals(new Point2f(6,5)));
		assertTrue(p.getPointAt(6).equals(new Point2f(7,-5)));
	}
	
	@Test
    public void getCurrentPoint() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		assertTrue(p.getCurrentPoint().equals(new Point2f(0,0)));
		p.lineTo(1f, 1f);
		assertTrue(p.getCurrentPoint().equals(new Point2f(1,1)));
		p.quadTo(3f, 0f, 4f, 3f);
		assertTrue(p.getCurrentPoint().equals(new Point2f(4,3)));
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		assertTrue(p.getCurrentPoint().equals(new Point2f(7,-5)));
		p.closePath();
		
		assertTrue(p.getCurrentPoint().equals(new Point2f(7,-5)));
	}
	
	
	@Test
    public void containsRectangle() {
		throw new UnsupportedOperationException();
	}
	
	@Test
    public void toFloatArray() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		double [] array = p.toFloatArray();
		
		assertTrue(new Point2f(array[0],array[1]).equals(new Point2f(0,0)));
		assertTrue(new Point2f(array[2],array[3]).equals(new Point2f(1,1)));
		assertTrue(new Point2f(array[4],array[5]).equals(new Point2f(3,0)));
		assertTrue(new Point2f(array[6],array[7]).equals(new Point2f(4,3)));
		assertTrue(new Point2f(array[8],array[9]).equals(new Point2f(5,-1)));
		assertTrue(new Point2f(array[10],array[11]).equals(new Point2f(6,5)));
		assertTrue(new Point2f(array[12],array[13]).equals(new Point2f(7,-5)));
	}
	
	@Test
    public void getCoordAt() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.quadTo(3f, 0f, 4f, 3f);
		p.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		p.closePath();
		
		assertTrue(p.getCoordAt(0)==0);
		assertTrue(p.getCoordAt(1)==0);
		assertTrue(p.getCoordAt(2)==1);
		assertTrue(p.getCoordAt(3)==1);
		assertTrue(p.getCoordAt(4)==3);
		assertTrue(p.getCoordAt(5)==0);
		assertTrue(p.getCoordAt(6)==4);
		assertTrue(p.getCoordAt(7)==3);
		assertTrue(p.getCoordAt(8)==5);
		assertTrue(p.getCoordAt(9)==-1);
		assertTrue(p.getCoordAt(10)==6);
		assertTrue(p.getCoordAt(11)==5);
		assertTrue(p.getCoordAt(12)==7);
		assertTrue(p.getCoordAt(13)==-5);
	}
	
	@Test
    public void length() {
		Path2f p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.lineTo(2f, 0f);
		
		assertEpsilonEquals(2*Math.sqrt(2), p.length());
		
		p = new Path2f();
		p.moveTo(0f, 0f);
		p.lineTo(1f, 1f);
		p.lineTo(2f, 0f);
		p.closePath();
		
		assertEpsilonEquals(2*Math.sqrt(2)+2, p.length());
		
		
		p = new Path2f();
		p.moveTo(0f, 0f);
		p.quadTo(1f, 1f,2f,0f);
		//p.closePath();
		
		assertTrue(MathUtil.isEpsilonEqual(p.length(),2.29559,0.02));
		
		p = new Path2f();
		p.moveTo(0f, 0f);
		p.quadTo(1f, 1f,2f,0f);
		p.closePath();
		
		assertTrue(MathUtil.isEpsilonEqual(p.length(),2.29559+2,0.02));
				
	}
	
}


