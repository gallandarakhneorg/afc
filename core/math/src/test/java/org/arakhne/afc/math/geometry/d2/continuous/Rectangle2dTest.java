/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2016 Stephane GALLAND.
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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.junit.Test;

/**
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("static-method")
public class Rectangle2dTest extends AbstractRectangularShape2fTestCase<Rectangle2d> {
	
	@Override
	protected Rectangle2d createShape() {
		return new Rectangle2d(0f, 0f, 1f, 1f);
	}

	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.setFromCorners(1f,this.r.getMinY(),this.r.getMaxX(),this.r.getMaxY());
		assertFalse(this.r.isEmpty());
		this.r.setFromCorners(this.r.getMinX(),1f,this.r.getMaxX(),this.r.getMaxY());
		assertTrue(this.r.isEmpty());
		this.r.setFromCorners(0f,this.r.getMinY(),this.r.getMaxX(),this.r.getMaxY());
		assertFalse(this.r.isEmpty());
	}

	@Test
	@Override
	public void clear() {
		this.r.clear();
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(0f, this.r.getMaxX());
		assertEpsilonEquals(0f, this.r.getMaxY());
	}

	@Test
	@Override
	public void testClone() {
		Rectangle2d b = this.r.clone();

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

	@Test
	@Override
	public void distancePoint2D() {
		double d;
		d = this.r.distance(new Point2d(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distance(new Point2d(-1.2f,-3.4f));
		//sqrt( 1.2*1.2 + 3.4*3.4 ) = sqrt( 1.44 + 11.56 ) = sqrt( 13 )
		assertEpsilonEquals(3.605551275f,d);

		d = this.r.distance(new Point2d(-1.2f,5.6f));
		//sqrt( 1.2*1.2 + 4.6*4.6 ) = sqrt( 1.44 + 21.16 ) = sqrt( 22.6 )
		assertEpsilonEquals(4.75394573f,d);

		d = this.r.distance(new Point2d(7.6f,5.6f));
		//sqrt( 6.6*6 + 4.6*4.6 ) = sqrt( 43.56 + 21.16 ) = sqrt( 64.72 )
		assertEpsilonEquals(8.044874144f,d);
	}

	@Test
	@Override
	public void distanceSquaredPoint2D() {
		double d;
		d = this.r.distanceSquared(new Point2d(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceSquared(new Point2d(-1.2f,-3.4f));
		//sqrt( 1.2*1.2 + 3.4*3.4 ) = sqrt( 1.44 + 11.56 ) = sqrt( 13 )
		assertEpsilonEquals(13f,d);

		d = this.r.distanceSquared(new Point2d(-1.2f,5.6f));
		//sqrt( 1.2*1.2 + 4.6*4.6 ) = sqrt( 1.44 + 21.16 ) = sqrt( 22.6 )
		assertEpsilonEquals(22.6f,d);

		d = this.r.distanceSquared(new Point2d(7.6f,5.6f));
		//sqrt( 6.6*6 + 4.6*4.6 ) = sqrt( 43.56 + 21.16 ) = sqrt( 64.72 )
		assertEpsilonEquals(64.72f,d);
	}

	@Test
	@Override
	public void distanceL1Point2D() {
		double d;
		d = this.r.distanceL1(new Point2d(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceL1(new Point2d(-1.2f,-3.4f));
		//1.2 + 3.4
		assertEpsilonEquals(4.6f,d);

		d = this.r.distanceL1(new Point2d(-1.2f,5.6f));
		//1.2 + 4.6
		assertEpsilonEquals(5.8f,d);

		d = this.r.distanceL1(new Point2d(7.6f,5.6f));
		//6.6 + 4.6
		assertEpsilonEquals(11.2f,d);
	}

	@Test
	@Override
	public void distanceLinfPoint2D() {
		double d;
		d = this.r.distanceLinf(new Point2d(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceLinf(new Point2d(-1.2f,-3.4f));
		//max( 1.2, 3.4 )
		assertEpsilonEquals(3.4f,d);

		d = this.r.distanceLinf(new Point2d(-1.2f,5.6f));
		//max( 1.2, 4.6 )
		assertEpsilonEquals(4.6f,d);

		d = this.r.distanceLinf(new Point2d(7.6f,5.6f));
		//max( 6.6, 4.6 )
		assertEpsilonEquals(6.6f,d);
	}
		
	/**
	 */
	@Test
	public void union() {
		// a: 2.3x3.4 - 6.8x9
		// b: 6.5x5.4 - 10.8x8.6
		Rectangle2d a = new Rectangle2d(2.3f, 3.4f, 4.5f, 5.6f);
		Rectangle2d b = new Rectangle2d(6.5f, 5.4f, 4.3f, 3.2f);
		
		AbstractRectangle2F.union(this.r, a, b);
		assertEpsilonEquals(2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(10.8f, this.r.getMaxX());
		assertEpsilonEquals(9f, this.r.getMaxY());

		AbstractRectangle2F.union(this.r, b, a);
		assertEpsilonEquals(2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(10.8f, this.r.getMaxX());
		assertEpsilonEquals(9f, this.r.getMaxY());
	}

	/**
	 */
	@Test
	public void intersection() {
		// a: 2.3x3.4 - 6.8x9
		// b: 6.5x5.4 - 10.8x8.6
		Rectangle2d a = new Rectangle2d(2.3f, 3.4f, 4.5f, 5.6f);
		Rectangle2d b = new Rectangle2d(6.5f, 5.4f, 4.3f, 3.2f);
		
		AbstractRectangle2F.intersection(this.r, a, b);
		assertEpsilonEquals(6.5f, this.r.getMinX());
		assertEpsilonEquals(5.4f, this.r.getMinY());
		assertEpsilonEquals(6.8f, this.r.getMaxX());
		assertEpsilonEquals(8.6f, this.r.getMaxY());

		AbstractRectangle2F.intersection(this.r, b, a);
		assertEpsilonEquals(6.5f, this.r.getMinX());
		assertEpsilonEquals(5.4f, this.r.getMinY());
		assertEpsilonEquals(6.8f, this.r.getMaxX());
		assertEpsilonEquals(8.6f, this.r.getMaxY());
	}

	/**
	 */
	@Test
	public void createUnion() {
		Rectangle2d rr;
		
		// b: 6.5x5.4 - 10.8x8.6
		Rectangle2d b = new Rectangle2d(6.5f, 5.4f, 4.3f, 3.2f);
		
		rr = new Rectangle2d(this.r.createUnion(b));
		assertEpsilonEquals(0f, rr.getMinX());
		assertEpsilonEquals(0f, rr.getMinY());
		assertEpsilonEquals(10.8f, rr.getMaxX());
		assertEpsilonEquals(8.6f, rr.getMaxY());

		rr = new Rectangle2d(this.r.createUnion(b));
		assertEpsilonEquals(0f, rr.getMinX());
		assertEpsilonEquals(0f, rr.getMinY());
		assertEpsilonEquals(10.8f, rr.getMaxX());
		assertEpsilonEquals(8.6f, rr.getMaxY());
	}

	/**
	 */
	@Test
	public void createIntersection() {
		Rectangle2d rr;
		
		// b: 6.5x5.4 - 10.8x8.6
		Rectangle2d b = new Rectangle2d(6.5f, 5.4f, 4.3f, 3.2f);
		
		rr = new Rectangle2d(this.r.createIntersection(b));
		assertEpsilonEquals(0f, rr.getMinX());
		assertEpsilonEquals(0f, rr.getMinY());
		assertEpsilonEquals(0f, rr.getMaxX());
		assertEpsilonEquals(0f, rr.getMaxY());

		rr = new Rectangle2d(this.r.createIntersection(b));
		assertEpsilonEquals(0f, rr.getMinX());
		assertEpsilonEquals(0f, rr.getMinY());
		assertEpsilonEquals(0f, rr.getMaxX());
		assertEpsilonEquals(0f, rr.getMaxY());
	}

	@Test
	@Override
	public void toBoundingBox() {
		AbstractRectangle2F<?> b = this.r.toBoundingBox();
		assertTrue(this.r.equals(b));
	}
	
	/**
	 */
	@Test
	public void containsFloatFloat() {
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
	@Test
	public void containsPoint2D() {
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
	@Test
	public void getClosestPointTo() {
		Point2D p;
		
		p = this.r.getClosestPointTo(new Point2d(0f, 0f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		
		p = this.r.getClosestPointTo(new Point2d(-2.3f, -3.4f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		p = this.r.getClosestPointTo(new Point2d(-2.3f, .5f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(.5f, p.getY());
		p = this.r.getClosestPointTo(new Point2d(-2.3f, 5.6f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(1f, p.getY());
		
		p = this.r.getClosestPointTo(new Point2d(.5f, -3.4f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		p = this.r.getClosestPointTo(new Point2d(.5f, .5f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(.5f, p.getY());
		p = this.r.getClosestPointTo(new Point2d(.5f, 5.6f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(1f, p.getY());

		p = this.r.getClosestPointTo(new Point2d(5.6f, -3.4f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		p = this.r.getClosestPointTo(new Point2d(5.6f, .5f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(.5f, p.getY());
		p = this.r.getClosestPointTo(new Point2d(5.6f, 5.6f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());

		p = this.r.getClosestPointTo(new Point2d(.01f, .01f));
		assertEpsilonEquals(.01f, p.getX());
		assertEpsilonEquals(.01f, p.getY());
	}
	
	/**
	 */
	@Test
	public void getFarthestPointTo() {
		Point2D p;
		
		p = this.r.getFarthestPointTo(new Point2d(0f, 0f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());
		
		p = this.r.getFarthestPointTo(new Point2d(-2.3f, -3.4f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());
		p = this.r.getFarthestPointTo(new Point2d(-2.3f, .5f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());
		p = this.r.getFarthestPointTo(new Point2d(-2.3f, 5.6f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		
		p = this.r.getFarthestPointTo(new Point2d(.5f, -3.4f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());
		p = this.r.getFarthestPointTo(new Point2d(.5f, .5f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());
		p = this.r.getFarthestPointTo(new Point2d(.5f, 5.6f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(0f, p.getY());

		p = this.r.getFarthestPointTo(new Point2d(5.6f, -3.4f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(1f, p.getY());
		p = this.r.getFarthestPointTo(new Point2d(5.6f, .5f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(1f, p.getY());
		p = this.r.getFarthestPointTo(new Point2d(5.6f, 5.6f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());

		p = this.r.getFarthestPointTo(new Point2d(.01f, .01f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());
	}

	/**
	 */
	@Test
	public void addPoint2D() {
		this.r.add(new Point2d(2.3f, 3.4f));
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(3.4f, this.r.getMaxY());

		this.r.add(new Point2d(-2.3f, -3.4f));
		assertEpsilonEquals(-2.3f, this.r.getMinX());
		assertEpsilonEquals(-3.4f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(3.4f, this.r.getMaxY());

		this.r.add(new Point2d(0f, 0f));
		assertEpsilonEquals(-2.3f, this.r.getMinX());
		assertEpsilonEquals(-3.4f, this.r.getMinY());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		assertEpsilonEquals(3.4f, this.r.getMaxY());
	}
	
	/**
	 */
	@Test
	public void addFloatFloat() {
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
	@Test
	public void getPathIteratorVoid() {
		PathIterator2d pi = this.r.getPathIteratorProperty();
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
	@Test
	public void getPathIteratorTransform2D() {
		Transform2D tr;
		PathIterator2d pi;
		
		tr = new Transform2D();
		pi = this.r.getPathIteratorProperty(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0f,0f);
		assertElement(pi, PathElementType.LINE_TO, 1f,0f);
		assertElement(pi, PathElementType.LINE_TO, 1f,1f);
		assertElement(pi, PathElementType.LINE_TO, 0f,1f);
		assertElement(pi, PathElementType.LINE_TO, 0f,0f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.r.getPathIteratorProperty(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3.4f, 4.5f);
		assertElement(pi, PathElementType.LINE_TO, 4.4f, 4.5f);
		assertElement(pi, PathElementType.LINE_TO, 4.4f, 5.5f);
		assertElement(pi, PathElementType.LINE_TO, 3.4f, 5.5f);
		assertElement(pi, PathElementType.LINE_TO, 3.4f, 4.5f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.r.getPathIteratorProperty(tr);
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
	@Test
	public void intersectsRectangleRectangle() {
		assertTrue(AbstractRectangle2F.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(AbstractRectangle2F.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(AbstractRectangle2F.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(AbstractRectangle2F.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(AbstractRectangle2F.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(AbstractRectangle2F.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(AbstractRectangle2F.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(AbstractRectangle2F.intersectsRectangleRectangle(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
	}

	/**
	 */
	@Test
	public void containsRectangleRectangle() {
		assertTrue(AbstractRectangle2F.containsRectangleRectangle(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertFalse(AbstractRectangle2F.containsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertFalse(AbstractRectangle2F.containsRectangleRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertFalse(AbstractRectangle2F.containsRectangleRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertFalse(AbstractRectangle2F.containsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(AbstractRectangle2F.containsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(AbstractRectangle2F.containsRectangleRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(AbstractRectangle2F.containsRectangleRectangle(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertTrue(AbstractRectangle2F.containsRectangleRectangle(0f, 0f, 1f, 1f,
				.25f, .25f, .5f, .5f));
	}

	/**
	 */
	@Test
	public void containsRectanglePoint() {
		assertTrue(AbstractRectangle2F.containsRectanglePoint(1f, 1f,
				0f, 0f, 1f, 1f));
		assertFalse(AbstractRectangle2F.containsRectanglePoint(1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(AbstractRectangle2F.containsRectanglePoint(1f, 1f,
				.5f, .5f, 5f, 5f));
		assertFalse(AbstractRectangle2F.containsRectanglePoint(1f, 1f,
				.5f, .5f, 5f, .1f));
		assertFalse(AbstractRectangle2F.containsRectanglePoint(1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(AbstractRectangle2F.containsRectanglePoint(1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(AbstractRectangle2F.containsRectanglePoint(1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(AbstractRectangle2F.containsRectanglePoint(1f, 1f,
				5f, -5f, 6f, 5f));
		assertTrue(AbstractRectangle2F.containsRectanglePoint(0.5f, 0.5f,
				.25f, .25f, .5f, .5f));
	}

	/**
	 */
	@Test
	public void intersectsRectangleLine() {
		assertTrue(AbstractRectangle2F.intersectsRectangleLine(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(AbstractRectangle2F.intersectsRectangleLine(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(AbstractRectangle2F.intersectsRectangleLine(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(AbstractRectangle2F.intersectsRectangleLine(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(AbstractRectangle2F.intersectsRectangleLine(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertTrue(AbstractRectangle2F.intersectsRectangleLine(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(AbstractRectangle2F.intersectsRectangleLine(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(AbstractRectangle2F.intersectsRectangleLine(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertFalse(AbstractRectangle2F.intersectsRectangleLine(0f, 0f, 1f, 1f,
				5f, -5f, 5f, 5f));
	}

	/**
	 */
	@Test
	public void intersectsRectangleSegment() {
		assertTrue(AbstractRectangle2F.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				0.5f, 0.5f, 1.5f, 1.5f));
		assertTrue(AbstractRectangle2F.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(AbstractRectangle2F.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(AbstractRectangle2F.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(AbstractRectangle2F.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(AbstractRectangle2F.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(AbstractRectangle2F.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(AbstractRectangle2F.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertFalse(AbstractRectangle2F.intersectsRectangleSegment(0f, 0f, 1f, 1f,
				5f, -5f, 5f, 5f));
	}

	/**
	 */
	@Test
	public void intersectsRectangle2d() {
		assertTrue(this.r.intersects(new Rectangle2d(0f, 0f, 1f, 1f)));
		assertTrue(this.r.intersects(new Rectangle2d(-5f, -5f, 6f, 6f)));
		assertTrue(this.r.intersects(new Rectangle2d(.5f, .5f, 4.5f, 4.5f)));
		assertTrue(this.r.intersects(new Rectangle2d(-5f, -5f, 10f, 10f)));
		assertFalse(this.r.intersects(new Rectangle2d(5f, .5f, 5f, .6f)));
		assertFalse(this.r.intersects(new Rectangle2d(-5f, -5f, 1f, 1f)));
		assertFalse(this.r.intersects(new Rectangle2d(-5f, -5f, 10f, 1f)));
		assertFalse(this.r.intersects(new Rectangle2d(5f, -5f, 1f, 10f)));
		assertTrue(this.r.intersects(new Rectangle2d(-5f, -5f, 5.01f, 5.01f)));
	}

	/**
	 */
	@Test
	public void intersectsSegment2d() {
		assertTrue(this.r.intersects(new Segment2d(0.5f, 0f, 1.5f, 1.5f)));
		assertTrue(this.r.intersects(new Segment2d(-5f, -5f, 1f, 1f)));
		assertTrue(this.r.intersects(new Segment2d(.5f, .5f, 5f, 5f)));
		assertTrue(this.r.intersects(new Segment2d(.5f, .5f, 5f, .6f)));
		assertTrue(this.r.intersects(new Segment2d(-5f, -5f, 5f, 5f)));
		assertFalse(this.r.intersects(new Segment2d(-5f, -5f, -4f, -4f)));
		assertFalse(this.r.intersects(new Segment2d(-5f, -5f, 4f, -4f)));
		assertFalse(this.r.intersects(new Segment2d(5f, -5f, 6f, 5f)));
		assertFalse(this.r.intersects(new Segment2d(5f, -5f, 5f, 5f)));
		assertTrue(this.r.intersects(new Segment2d(-1f, -1f, 0.01f, 0.01f)));
	}

	/**
	 */
	@Test
	public void intersectsCircle2d() {
		assertTrue(this.r.intersects(new Circle2d(0f, 0f, 1f)));
		assertFalse(this.r.intersects(new Circle2d(-5f, -5f, 1f)));
		assertTrue(this.r.intersects(new Circle2d(.5f, .5f, 5f)));
		assertFalse(this.r.intersects(new Circle2d(-5f, -5f, 5f)));
		assertFalse(this.r.intersects(new Circle2d(-5f, -5f, 4f)));
		assertFalse(this.r.intersects(new Circle2d(5f, -5f, 6f)));
		assertFalse(this.r.intersects(new Circle2d(5f, -5f, 5f)));
		assertTrue(this.r.intersects(new Circle2d(-1f, -1f, 1.4284f)));
	}

	/**
	 */
	@Test
	public void intersectsEllipse2d() {
		assertTrue(this.r.intersects(new Ellipse2d(0f, 0f, 1f, 1f)));
		assertFalse(this.r.intersects(new Ellipse2d(-5f, -5f, 1f, 1f)));
		assertFalse(this.r.intersects(new Ellipse2d(.5f, .5f, 5f, 5f)));
		assertTrue(this.r.intersects(new Ellipse2d(.5f, .5f, 5f, .6f)));
		assertFalse(this.r.intersects(new Ellipse2d(-5f, -5f, 5f, 5f)));
		assertFalse(this.r.intersects(new Ellipse2d(-5f, -5f, -4f, -4f)));
		assertFalse(this.r.intersects(new Ellipse2d(-5f, -5f, 4f, -4f)));
		assertFalse(this.r.intersects(new Ellipse2d(5f, -5f, 6f, 5f)));
		assertFalse(this.r.intersects(new Ellipse2d(5f, -5f, 5f, 5f)));
		assertTrue(this.r.intersects(new Ellipse2d(-1f, -1f, 1.2f, 1.2f)));
	}

	/**
	 */
	@Test
	@Override
	public void intersectsPath2f() {
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
	@Test
	@Override
	public void intersectsPathIterator2f() {
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

	/**
	 */
	@Test
	@Override
	public void intersectsPath2d() {
		Path2d p;
		
		p = new Path2d();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));
		
		p = new Path2d();
		p.moveTo(-2f, -2f);
		p.lineTo(0f, 0f);
		p.lineTo(-2f, 2f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertFalse(this.r.intersects(p));

		p = new Path2d();
		p.moveTo(-2f, -2f);
		p.lineTo(2f, 2f);
		p.lineTo(-2f, 2f);
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

		p = new Path2d();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertFalse(this.r.intersects(p));

		p = new Path2d();
		p.moveTo(-2f, 2f);
		p.lineTo(1f, 0f);
		p.lineTo(2f, 1f);
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

		p = new Path2d();
		p.moveTo(-2f, 2f);
		p.lineTo(2f, 1f);
		p.lineTo(1f, 0f);
		assertFalse(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));
	}

	/**
	 */
	@Test
	@Override
	public void intersectsPathIterator2d() {
		Path2d p;

		p = new Path2d();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p.getPathIteratorProperty()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));
		
		p = new Path2d();
		p.moveTo(-2f, -2f);
		p.lineTo(0f, 0f);
		p.lineTo(-2f, 2f);
		assertFalse(this.r.intersects(p.getPathIteratorProperty()));
		p.closePath();
		assertFalse(this.r.intersects(p.getPathIteratorProperty()));

		p = new Path2d();
		p.moveTo(-2f, -2f);
		p.lineTo(2f, 2f);
		p.lineTo(-2f, 2f);
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));

		p = new Path2d();
		p.moveTo(-2f, -2f);
		p.lineTo(-2f, 2f);
		p.lineTo(2f, -2f);
		assertFalse(this.r.intersects(p.getPathIteratorProperty()));
		p.closePath();
		assertFalse(this.r.intersects(p.getPathIteratorProperty()));

		p = new Path2d();
		p.moveTo(-2f, 2f);
		p.lineTo(1f, 0f);
		p.lineTo(2f, 1f);
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));

		p = new Path2d();
		p.moveTo(-2f, 2f);
		p.lineTo(2f, 1f);
		p.lineTo(1f, 0f);
		assertFalse(this.r.intersects(p.getPathIteratorProperty()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));
	}

	/**
	 */
	@Test
	public void setShape2f() {
		this.r.set(new Circle2f(10, 12, 14));
		assertEpsilonEquals(-4f, this.r.getMinX());
		assertEpsilonEquals(-2f, this.r.getMinY());
		assertEpsilonEquals(24f, this.r.getMaxX());
		assertEpsilonEquals(26f, this.r.getMaxY());
	}
	
	/**
	 */
	@Test
	public void testModifyProperties() {
		Point2d min = new Point2d(0,0);
		Point2d max = new Point2d(1,1);
		
		Rectangle2d test = new Rectangle2d(min,max);
		
		assertTrue(test.equals(new Rectangle2f(0,0,1,1)));
		
		min.set(2, 2);
		max.set(4, 4);
		
		assertTrue(test.equals(new Rectangle2f(2,2,2,2)));
		
		test.setFromCorners(0,0,10,5);
		
		assertTrue(min.equals(new Point2f(0,0)));
		assertTrue(max.equals(new Point2f(10,5)));
		
		Rectangle2d rectangle = new Rectangle2d(-1,-1,2,2);
		test = new Rectangle2d(rectangle);
		
		assertTrue(test.equals(new Rectangle2f(-1,-1,2,2)));
		
		rectangle.setFromCorners(-10, 10, 10, -10);
		
		assertTrue(test.equals(new Rectangle2f(-10,-10,20,20)));
		
		rectangle.setFromCornersProperties(min, max);	
		min.set(2, 2);
		max.set(4, 4);
		
		assertTrue(test.equals(new Rectangle2f(-10,-10,20,20)));
		assertTrue(rectangle.equals(new Rectangle2f(2,2,2,2)));
		
		test.setFromCornersProperties(min, max);	
		test.setFromCorners(0, 0, 1, 1);
		
		assertTrue(rectangle.equals(new Rectangle2f(0,0,1,1)));
		
		assertTrue(min.equals(new Point2f(0,0)));
		assertTrue(max.equals(new Point2f(1,1)));
		
	}

}