/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.continous.object2d;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Segment2f;
import org.arakhne.afc.math.generic.PathElementType;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.matrix.Transform2D;


/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Rectangle2fTest extends AbstractRectangularShape2fTestCase<Rectangle2f> {
	
	@Override
	protected Rectangle2f createShape() {
		return new Rectangle2f(0f, 0f, 1f, 1f);
	}

	@Override
	public void testIsEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.setMinX(1f);
		assertFalse(this.r.isEmpty());
		this.r.setMinY(1f);
		assertTrue(this.r.isEmpty());
		this.r.setMinX(0f);
		assertFalse(this.r.isEmpty());
	}

	@Override
	public void testClear() {
		this.r.clear();
		assertEquals(0f, this.r.getMinX());
		assertEquals(0f, this.r.getMinY());
		assertEquals(0f, this.r.getMaxX());
		assertEquals(0f, this.r.getMaxY());
	}

	@Override
	public void testClone() {
		Rectangle2f b = this.r.clone();

		assertNotSame(b, this.r);
		assertEpsilonEquals(this.r.getMinX(), b.getMinX());
		assertEpsilonEquals(this.r.getMinY(), b.getMinY());
		assertEpsilonEquals(this.r.getMaxX(), b.getMaxX());
		assertEpsilonEquals(this.r.getMaxY(), b.getMaxY());
		
		b.set(this.r.getMinX()+1f, this.r.getMinY()+1f,
				this.r.getWidth()+1f, this.r.getHeight()+1f);

		assertNotEpsilonEquals(this.r.getMinX(), b.getMinX());
		assertNotEpsilonEquals(this.r.getMinY(), b.getMinY());
		assertNotEpsilonEquals(this.r.getMaxX(), b.getMaxX());
		assertNotEpsilonEquals(this.r.getMaxY(), b.getMaxY());
	}

	@Override
	public void testDistancePoint2D() {
		float d;
		d = this.r.distance(new Point2f(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distance(new Point2f(-1.2f,-3.4f));
		//sqrt( 1.2*1.2 + 3.4*3.4 ) = sqrt( 1.44 + 11.56 ) = sqrt( 13 )
		assertEpsilonEquals(3.605551275f,d);

		d = this.r.distance(new Point2f(-1.2f,5.6f));
		//sqrt( 1.2*1.2 + 4.6*4.6 ) = sqrt( 1.44 + 21.16 ) = sqrt( 22.6 )
		assertEpsilonEquals(4.75394573f,d);

		d = this.r.distance(new Point2f(7.6f,5.6f));
		//sqrt( 6.6*6 + 4.6*4.6 ) = sqrt( 43.56 + 21.16 ) = sqrt( 64.72 )
		assertEpsilonEquals(8.044874144f,d);
	}

	@Override
	public void testDistanceSquaredPoint2D() {
		float d;
		d = this.r.distanceSquared(new Point2f(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceSquared(new Point2f(-1.2f,-3.4f));
		//sqrt( 1.2*1.2 + 3.4*3.4 ) = sqrt( 1.44 + 11.56 ) = sqrt( 13 )
		assertEpsilonEquals(13f,d);

		d = this.r.distanceSquared(new Point2f(-1.2f,5.6f));
		//sqrt( 1.2*1.2 + 4.6*4.6 ) = sqrt( 1.44 + 21.16 ) = sqrt( 22.6 )
		assertEpsilonEquals(22.6f,d);

		d = this.r.distanceSquared(new Point2f(7.6f,5.6f));
		//sqrt( 6.6*6 + 4.6*4.6 ) = sqrt( 43.56 + 21.16 ) = sqrt( 64.72 )
		assertEpsilonEquals(64.72f,d);
	}

	@Override
	public void testDistanceL1Point2D() {
		float d;
		d = this.r.distanceL1(new Point2f(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceL1(new Point2f(-1.2f,-3.4f));
		//1.2 + 3.4
		assertEpsilonEquals(4.6f,d);

		d = this.r.distanceL1(new Point2f(-1.2f,5.6f));
		//1.2 + 4.6
		assertEpsilonEquals(5.8f,d);

		d = this.r.distanceL1(new Point2f(7.6f,5.6f));
		//6.6 + 4.6
		assertEpsilonEquals(11.2f,d);
	}

	@Override
	public void testDistanceLinfPoint2D() {
		float d;
		d = this.r.distanceLinf(new Point2f(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceLinf(new Point2f(-1.2f,-3.4f));
		//max( 1.2, 3.4 )
		assertEpsilonEquals(3.4f,d);

		d = this.r.distanceLinf(new Point2f(-1.2f,5.6f));
		//max( 1.2, 4.6 )
		assertEpsilonEquals(4.6f,d);

		d = this.r.distanceLinf(new Point2f(7.6f,5.6f));
		//max( 6.6, 4.6 )
		assertEpsilonEquals(6.6f,d);
	}
		
	/**
	 */
	public void testUnion() {
		// a: 2.3x3.4 - 6.8x9
		// b: 6.5x5.4 - 10.8x8.6
		Rectangle2f a = new Rectangle2f(2.3f, 3.4f, 4.5f, 5.6f);
		Rectangle2f b = new Rectangle2f(6.5f, 5.4f, 4.3f, 3.2f);
		
		Rectangle2f.union(this.r, a, b);
		assertEpsilonEquals(2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(10.8f, this.r.getMaxX());
		assertEpsilonEquals(9f, this.r.getMaxY());

		Rectangle2f.union(this.r, b, a);
		assertEpsilonEquals(2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(10.8f, this.r.getMaxX());
		assertEpsilonEquals(9f, this.r.getMaxY());
	}

	/**
	 */
	public void testIntersection() {
		// a: 2.3x3.4 - 6.8x9
		// b: 6.5x5.4 - 10.8x8.6
		Rectangle2f a = new Rectangle2f(2.3f, 3.4f, 4.5f, 5.6f);
		Rectangle2f b = new Rectangle2f(6.5f, 5.4f, 4.3f, 3.2f);
		
		Rectangle2f.intersection(this.r, a, b);
		assertEpsilonEquals(6.5f, this.r.getMinX());
		assertEpsilonEquals(5.4f, this.r.getMinY());
		assertEpsilonEquals(6.8f, this.r.getMaxX());
		assertEpsilonEquals(8.6f, this.r.getMaxY());

		Rectangle2f.intersection(this.r, b, a);
		assertEpsilonEquals(6.5f, this.r.getMinX());
		assertEpsilonEquals(5.4f, this.r.getMinY());
		assertEpsilonEquals(6.8f, this.r.getMaxX());
		assertEpsilonEquals(8.6f, this.r.getMaxY());
	}

	/**
	 */
	public void testCreateUnion() {
		Rectangle2f rr;
		
		// b: 6.5x5.4 - 10.8x8.6
		Rectangle2f b = new Rectangle2f(6.5f, 5.4f, 4.3f, 3.2f);
		
		rr = this.r.createUnion(b);
		assertEpsilonEquals(0f, rr.getMinX());
		assertEpsilonEquals(0f, rr.getMinY());
		assertEpsilonEquals(10.8f, rr.getMaxX());
		assertEpsilonEquals(8.6f, rr.getMaxY());

		rr = this.r.createUnion(b);
		assertEpsilonEquals(0f, rr.getMinX());
		assertEpsilonEquals(0f, rr.getMinY());
		assertEpsilonEquals(10.8f, rr.getMaxX());
		assertEpsilonEquals(8.6f, rr.getMaxY());
	}

	/**
	 */
	public void testCreateIntersection() {
		Rectangle2f rr;
		
		// b: 6.5x5.4 - 10.8x8.6
		Rectangle2f b = new Rectangle2f(6.5f, 5.4f, 4.3f, 3.2f);
		
		rr = this.r.createIntersection(b);
		assertEpsilonEquals(0f, rr.getMinX());
		assertEpsilonEquals(0f, rr.getMinY());
		assertEpsilonEquals(0f, rr.getMaxX());
		assertEpsilonEquals(0f, rr.getMaxY());

		rr = this.r.createIntersection(b);
		assertEpsilonEquals(0f, rr.getMinX());
		assertEpsilonEquals(0f, rr.getMinY());
		assertEpsilonEquals(0f, rr.getMaxX());
		assertEpsilonEquals(0f, rr.getMaxY());
	}

	@Override
	public void testToBoundingBox() {
		Rectangle2f b = this.r.toBoundingBox();
		assertNotSame(this.r, b);
		assertEpsilonEquals(this.r.getMinX(), b.getMinX());
		assertEpsilonEquals(this.r.getMinY(), b.getMinY());
		assertEpsilonEquals(this.r.getMaxX(), b.getMaxX());
		assertEpsilonEquals(this.r.getMaxY(), b.getMaxY());
	}
	
	/**
	 */
	public void testContainsFloatFloat() {
		assertTrue(this.r.contains(0f, 0f));
		
		assertFalse(this.r.contains(-2.3f, -3.4f));
		assertFalse(this.r.contains(-2.3f, .5f));
		assertFalse(this.r.contains(-2.3f, 5.6f));
		
		assertFalse(this.r.contains(.5f, -3.4f));
		assertTrue(this.r.contains(.5f, .5f));
		assertFalse(this.r.contains(.5f, 5.6f));

		assertFalse(this.r.contains(5.6f, -3.4f));
		assertFalse(this.r.contains(5.6f, .5f));
		assertFalse(this.r.contains(5.6f, 5.6f));

		assertTrue(this.r.contains(.01f, .01f));
	}
	
	/**
	 */
	public void testContainsPoint2D() {
		assertTrue(this.r.contains(new Point2f(0f, 0f)));
		
		assertFalse(this.r.contains(new Point2f(-2.3f, -3.4f)));
		assertFalse(this.r.contains(new Point2f(-2.3f, .5f)));
		assertFalse(this.r.contains(new Point2f(-2.3f, 5.6f)));
		
		assertFalse(this.r.contains(new Point2f(.5f, -3.4f)));
		assertTrue(this.r.contains(new Point2f(.5f, .5f)));
		assertFalse(this.r.contains(new Point2f(.5f, 5.6f)));

		assertFalse(this.r.contains(new Point2f(5.6f, -3.4f)));
		assertFalse(this.r.contains(new Point2f(5.6f, .5f)));
		assertFalse(this.r.contains(new Point2f(5.6f, 5.6f)));

		assertTrue(this.r.contains(new Point2f(.01f, .01f)));
	}

	/**
	 */
	public void testGetClosestPointTo() {
		Point2D p;
		
		p = this.r.getClosestPointTo(new Point2f(0f, 0f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		
		p = this.r.getClosestPointTo(new Point2f(-2.3f, -3.4f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		p = this.r.getClosestPointTo(new Point2f(-2.3f, .5f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(.5f, p.getY());
		p = this.r.getClosestPointTo(new Point2f(-2.3f, 5.6f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(1f, p.getY());
		
		p = this.r.getClosestPointTo(new Point2f(.5f, -3.4f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		p = this.r.getClosestPointTo(new Point2f(.5f, .5f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(.5f, p.getY());
		p = this.r.getClosestPointTo(new Point2f(.5f, 5.6f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(1f, p.getY());

		p = this.r.getClosestPointTo(new Point2f(5.6f, -3.4f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		p = this.r.getClosestPointTo(new Point2f(5.6f, .5f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(.5f, p.getY());
		p = this.r.getClosestPointTo(new Point2f(5.6f, 5.6f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());

		p = this.r.getClosestPointTo(new Point2f(.01f, .01f));
		assertEpsilonEquals(.01f, p.getX());
		assertEpsilonEquals(.01f, p.getY());
	}
	
	/**
	 */
	public void testAddPoint2D() {
		this.r.add(new Point2f(2.3f, 3.4f));
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(3.4f, this.r.getMaxY());

		this.r.add(new Point2f(-2.3f, -3.4f));
		assertEpsilonEquals(-2.3f, this.r.getMinX());
		assertEpsilonEquals(-3.4f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(3.4f, this.r.getMaxY());

		this.r.add(new Point2f(0f, 0f));
		assertEpsilonEquals(-2.3f, this.r.getMinX());
		assertEpsilonEquals(-3.4f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(3.4f, this.r.getMaxY());
	}
	
	/**
	 */
	public void testAddFloatFloat() {
		this.r.add(2.3f, 3.4f);
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(3.4f, this.r.getMaxY());

		this.r.add(-2.3f, -3.4f);
		assertEpsilonEquals(-2.3f, this.r.getMinX());
		assertEpsilonEquals(-3.4f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(3.4f, this.r.getMaxY());

		this.r.add(0f, 0f);
		assertEpsilonEquals(-2.3f, this.r.getMinX());
		assertEpsilonEquals(-3.4f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(3.4f, this.r.getMaxY());
	}
	
	
	/**
	 */
	public void testGetPathIteratorVoid() {
		PathIterator2f pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0f,0f);
		assertElement(pi, PathElementType.LINE_TO, 1f,0f);
		assertElement(pi, PathElementType.LINE_TO, 1f,1f);
		assertElement(pi, PathElementType.LINE_TO, 0f,1f);
		assertElement(pi, PathElementType.LINE_TO, 0f,0f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/**
	 */
	public void testGetPathIteratorTransform2D() {
		Transform2D tr;
		PathIterator2f pi;
		
		tr = new Transform2D();
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0f,0f);
		assertElement(pi, PathElementType.LINE_TO, 1f,0f);
		assertElement(pi, PathElementType.LINE_TO, 1f,1f);
		assertElement(pi, PathElementType.LINE_TO, 0f,1f);
		assertElement(pi, PathElementType.LINE_TO, 0f,0f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3.4f, 4.5f);
		assertElement(pi, PathElementType.LINE_TO, 4.4f, 4.5f);
		assertElement(pi, PathElementType.LINE_TO, 4.4f, 5.5f);
		assertElement(pi, PathElementType.LINE_TO, 3.4f, 5.5f);
		assertElement(pi, PathElementType.LINE_TO, 3.4f, 4.5f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0f, 0f);
		assertElement(pi, PathElementType.LINE_TO, 0.707106781f, 0.707106781f);
		assertElement(pi, PathElementType.LINE_TO, 0f, 1.414213562f);
		assertElement(pi, PathElementType.LINE_TO, -0.707106781f, 0.707106781f);
		assertElement(pi, PathElementType.LINE_TO, 0f, 0f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/**
	 */
	public static void testIntersectsRectangleRectangle() {
		assertTrue(Rectangle2f.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Rectangle2f.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(Rectangle2f.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(Rectangle2f.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(Rectangle2f.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(Rectangle2f.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(Rectangle2f.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(Rectangle2f.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
	}

	/**
	 */
	public static void testContainsRectangleRectangle() {
		assertTrue(Rectangle2f.containsRectangleRectangle(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertFalse(Rectangle2f.containsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertFalse(Rectangle2f.containsRectangleRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertFalse(Rectangle2f.containsRectangleRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertFalse(Rectangle2f.containsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(Rectangle2f.containsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(Rectangle2f.containsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(Rectangle2f.containsRectangleRectangle(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertTrue(Rectangle2f.containsRectangleRectangle(0f, 0f, 1f, 1f,
				.25f, .25f, .5f, .5f));
	}

	/**
	 */
	public static void testIntersectsRectangleLine() {
		assertTrue(Rectangle2f.intersectsRectangleLine(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Rectangle2f.intersectsRectangleLine(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(Rectangle2f.intersectsRectangleLine(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(Rectangle2f.intersectsRectangleLine(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(Rectangle2f.intersectsRectangleLine(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertTrue(Rectangle2f.intersectsRectangleLine(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(Rectangle2f.intersectsRectangleLine(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(Rectangle2f.intersectsRectangleLine(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertFalse(Rectangle2f.intersectsRectangleLine(0f, 0f, 1f, 1f,
				5f, -5f, 5f, 5f));
	}

	/**
	 */
	public static void testIntersectsRectangleSegment() {
		assertTrue(Rectangle2f.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Rectangle2f.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(Rectangle2f.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(Rectangle2f.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(Rectangle2f.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(Rectangle2f.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(Rectangle2f.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(Rectangle2f.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertFalse(Rectangle2f.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				5f, -5f, 5f, 5f));
	}

	/**
	 */
	public void testIntersectsRectangle2f() {
		assertTrue(this.r.intersects(new Rectangle2f(0f, 0f, 1f, 1f)));
		assertTrue(this.r.intersects(new Rectangle2f(-5f, -5f, 6f, 6f)));
		assertTrue(this.r.intersects(new Rectangle2f(.5f, .5f, 4.5f, 4.5f)));
		assertTrue(this.r.intersects(new Rectangle2f(-5f, -5f, 10f, 10f)));
		assertFalse(this.r.intersects(new Rectangle2f(5f, .5f, 5f, .6f)));
		assertFalse(this.r.intersects(new Rectangle2f(-5f, -5f, 1f, 1f)));
		assertFalse(this.r.intersects(new Rectangle2f(-5f, -5f, 10f, 1f)));
		assertFalse(this.r.intersects(new Rectangle2f(5f, -5f, 1f, 10f)));
		assertTrue(this.r.intersects(new Rectangle2f(-5f, -5f, 5.01f, 5.01f)));
	}

	/**
	 */
	public void testIntersectsSegment2f() {
		assertTrue(this.r.intersects(new Segment2f(0f, 0f, 1f, 1f)));
		assertTrue(this.r.intersects(new Segment2f(-5f, -5f, 1f, 1f)));
		assertTrue(this.r.intersects(new Segment2f(.5f, .5f, 5f, 5f)));
		assertTrue(this.r.intersects(new Segment2f(.5f, .5f, 5f, .6f)));
		assertTrue(this.r.intersects(new Segment2f(-5f, -5f, 5f, 5f)));
		assertFalse(this.r.intersects(new Segment2f(-5f, -5f, -4f, -4f)));
		assertFalse(this.r.intersects(new Segment2f(-5f, -5f, 4f, -4f)));
		assertFalse(this.r.intersects(new Segment2f(5f, -5f, 6f, 5f)));
		assertFalse(this.r.intersects(new Segment2f(5f, -5f, 5f, 5f)));
		assertTrue(this.r.intersects(new Segment2f(-1f, -1f, 0.01f, 0.01f)));
	}

	/**
	 */
	public void testIntersectsCircle2f() {
		assertTrue(this.r.intersects(new Circle2f(0f, 0f, 1f)));
		assertFalse(this.r.intersects(new Circle2f(-5f, -5f, 1f)));
		assertTrue(this.r.intersects(new Circle2f(.5f, .5f, 5f)));
		assertFalse(this.r.intersects(new Circle2f(-5f, -5f, 5f)));
		assertFalse(this.r.intersects(new Circle2f(-5f, -5f, 4f)));
		assertFalse(this.r.intersects(new Circle2f(5f, -5f, 6f)));
		assertFalse(this.r.intersects(new Circle2f(5f, -5f, 5f)));
		assertTrue(this.r.intersects(new Circle2f(-1f, -1f, 1.4284f)));
	}

	/**
	 */
	public void testIntersectsEllipse2f() {
		assertTrue(this.r.intersects(new Ellipse2f(0f, 0f, 1f, 1f)));
		assertFalse(this.r.intersects(new Ellipse2f(-5f, -5f, 1f, 1f)));
		assertFalse(this.r.intersects(new Ellipse2f(.5f, .5f, 5f, 5f)));
		assertTrue(this.r.intersects(new Ellipse2f(.5f, .5f, 5f, .6f)));
		assertFalse(this.r.intersects(new Ellipse2f(-5f, -5f, 5f, 5f)));
		assertFalse(this.r.intersects(new Ellipse2f(-5f, -5f, -4f, -4f)));
		assertFalse(this.r.intersects(new Ellipse2f(-5f, -5f, 4f, -4f)));
		assertFalse(this.r.intersects(new Ellipse2f(5f, -5f, 6f, 5f)));
		assertFalse(this.r.intersects(new Ellipse2f(5f, -5f, 5f, 5f)));
		assertTrue(this.r.intersects(new Ellipse2f(-1f, -1f, 1.2f, 1.2f)));
	}

	/**
	 */
	@Override
	public void testIntersectsPath2f() {
		Path2f p;
		
		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));
		
		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(0f, 0f);
		p.lineTo(-2f, 2f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertFalse(this.r.intersects(p));

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(2f, 2f);
		p.lineTo(-2f, 2f);
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertFalse(this.r.intersects(p));

		p = new Path2f();
		p.moveTo(-2f, 2f);
		p.lineTo(1f, 0f);
		p.lineTo(2f, 1f);
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

		p = new Path2f();
		p.moveTo(-2f, 2f);
		p.lineTo(2f, 1f);
		p.lineTo(1f, 0f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));
	}

	/**
	 */
	@Override
	public void testIntersectsPathIterator2f() {
		Path2f p;

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));
		
		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(0f, 0f);
		p.lineTo(-2f, 2f);
		assertFalse(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.r.intersects(p.getPathIterator()));

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(2f, 2f);
		p.lineTo(-2f, 2f);
		assertTrue(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));

		p = new Path2f();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.r.intersects(p.getPathIterator()));

		p = new Path2f();
		p.moveTo(-2f, 2f);
		p.lineTo(1f, 0f);
		p.lineTo(2f, 1f);
		assertTrue(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));

		p = new Path2f();
		p.moveTo(-2f, 2f);
		p.lineTo(2f, 1f);
		p.lineTo(1f, 0f);
		assertFalse(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));
	}

}