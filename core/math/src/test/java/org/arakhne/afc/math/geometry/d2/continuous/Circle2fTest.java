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
package org.arakhne.afc.math.geometry.d2.continuous;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.continuous.Circle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Path2f;
import org.arakhne.afc.math.geometry.d2.continuous.PathIterator2f;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Rectangle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Transform2D;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Circle2fTest extends AbstractShape2fTestCase<Circle2f> {
	
	@Override
	protected Circle2f createShape() {
		return new Circle2f(0f, 0f, 1f);
	}
	
	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.setRadius(1234.56f);
		assertFalse(this.r.isEmpty());
		this.r.setRadius(-1234.56f);
		assertFalse(this.r.isEmpty());
		this.r.setRadius(0f);
		assertTrue(this.r.isEmpty());
		this.r.setRadius(-0f);
		assertTrue(this.r.isEmpty());
		this.r.setRadius(+0f);
		assertTrue(this.r.isEmpty());
		this.r.setRadius(789.1011f);
		assertFalse(this.r.isEmpty());
	}
	
	@Test
	@Override
	public void clear() {
		this.r.clear();
		assertEpsilonEquals(0f, this.r.getX());
		assertEpsilonEquals(0f, this.r.getY());
		assertEpsilonEquals(0f, this.r.getRadius());
	}
	
	/**
	 */
	@Test
	public void containsCircleRectangle() {
		assertTrue(Circle2f.containsCircleRectangle(0f, 0f, 1f, 0f, 0f, .5f, .5f));
		assertFalse(Circle2f.containsCircleRectangle(0f, 0f, 1f, 0f, 0f, 1f, 1f));
		assertFalse(Circle2f.containsCircleRectangle(0f, 0f, 1f, 0f, 0f, .5f, 1f));
	}

	/**
	 */
	@Test
	public void intersectsCircleCircle() {
		assertFalse(Circle2f.intersectsCircleCircle(
				0f, 0f, 1f,
				10f, 10f, 1f));
		assertTrue(Circle2f.intersectsCircleCircle(
				0f, 0f, 1f,
				0f, 0f, 1f));
		assertTrue(Circle2f.intersectsCircleCircle(
				0f, 0f, 1f,
				0f, .5f, 1f));
		assertTrue(Circle2f.intersectsCircleCircle(
				0f, 0f, 1f,
				.5f, 0f, 1f));
		assertTrue(Circle2f.intersectsCircleCircle(
				0f, 0f, 1f,
				.5f, .5f, 1f));
		assertFalse(Circle2f.intersectsCircleCircle(
				0f, 0f, 1f,
				2f, 0f, 1f));
	}

	/**
	 */
	@Test
	public void intersectsCircleRectangle() {
		assertFalse(Circle2f.intersectsCircleRectangle(
				0f, 0f, 1f,
				-5f, -5f, -4f, -4f));
		assertTrue(Circle2f.intersectsCircleRectangle(
				0f, 0f, 1f,
				-5f, -5f, 5f, 5f));
		assertTrue(Circle2f.intersectsCircleRectangle(
				0f, 0f, 1f,
				-5f, -5f, .5f, .5f));
		assertFalse(Circle2f.intersectsCircleRectangle(
				0f, 0f, 1f,
				-5f, -5f, .5f, -4f));
		assertFalse(Circle2f.intersectsCircleRectangle(
				0f, 0f, 1f,
				20f, .5f, 21f, 1.5f));
	}

	/**
	 */
	@Test
	public void intersectsCircleLine() {
		assertTrue(Circle2f.intersectsCircleLine(
				0f, 0f, 1f,
				-5f, -5f, -4f, -4f));
		assertTrue(Circle2f.intersectsCircleLine(
				0f, 0f, 1f,
				-5f, -5f, 5f, 5f));
		assertTrue(Circle2f.intersectsCircleLine(
				0f, 0f, 1f,
				-5f, -5f, .5f, .5f));
		assertFalse(Circle2f.intersectsCircleLine(
				0f, 0f, 1f,
				-5f, -5f, .5f, -4f));
		assertFalse(Circle2f.intersectsCircleLine(
				0f, 0f, 1f,
				20f, .5f, 21f, 1.5f));
	}

	/**
	 */
	@Test
	public void intersectsCircleSegment() {
		assertFalse(Circle2f.intersectsCircleSegment(
				0f, 0f, 1f,
				-5f, -5f, -4f, -4f));
		assertTrue(Circle2f.intersectsCircleSegment(
				0f, 0f, 1f,
				-5f, -5f, 5f, 5f));
		assertTrue(Circle2f.intersectsCircleSegment(
				0f, 0f, 1f,
				-5f, -5f, .5f, .5f));
		assertFalse(Circle2f.intersectsCircleSegment(
				0f, 0f, 1f,
				-5f, -5f, .5f, -4f));
		assertFalse(Circle2f.intersectsCircleSegment(
				0f, 0f, 1f,
				20f, .5f, 21f, 1.5f));
		assertTrue(Circle2f.intersectsCircleSegment(
				1f, 1f, 1f,
				.5f, -1f, .5f, 4f));
	}

	@Test
	@Override
	public void distancePoint2D() {
		double d;
		d = this.r.distance(new Point2fx(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distance(new Point2fx(-1.2f,-3.4f));
		//d = sqrt( 1.2*1.2 + 3.4*3.4 ) - 1 = sqrt( 1.44 + 11.56 ) - 1 = sqrt( 13 ) - 1
		assertEpsilonEquals(2.605551275f,d);

		d = this.r.distance(new Point2fx(-1.2f,5.6f));
		//d = sqrt( 1.2*1.2 + 5.6*5.6 ) - 1 = sqrt( 1.44 + 31.36 ) - 1 = sqrt( 32.8 ) - 1
		assertEpsilonEquals(4.727128425f,d);

		d = this.r.distance(new Point2fx(7.6f,5.6f));
		//d = sqrt( 7.6*7.6 + 5.6*5.6 ) - 1 = sqrt( 57.76 + 31.36 ) - 1 = sqrt( 89.12 ) - 1
		assertEpsilonEquals(8.440338977f,d);
	}

	@Test
	@Override
	public void distanceSquaredPoint2D() {
		double sd;
		//d*d = sd
		sd = this.r.distanceSquared(new Point2fx(.5f,.5f));
		assertEpsilonEquals(0f,sd);

		sd = this.r.distanceSquared(new Point2fx(-1.2f,-3.4f));
		//d = sqrt( 1.2*1.2 + 3.4*3.4 ) - 1 = sqrt( 1.44 + 11.56 ) - 1 = sqrt( 13 ) - 1
		assertEpsilonEquals(6.788897449f,sd);

		sd = this.r.distanceSquared(new Point2fx(-1.2f,5.6f));
		//d = sqrt( 1.2*1.2 + 5.6*5.6 ) - 1 = sqrt( 1.44 + 31.36 ) - 1 = sqrt( 32.8 ) - 1
		assertEpsilonEquals(22.345743149f,sd);

		sd = this.r.distanceSquared(new Point2fx(7.6f,5.6f));
		//d = sqrt( 7.6*7.6 + 5.6*5.6 ) - 1 = sqrt( 57.76 + 31.36 ) - 1 = sqrt( 89.12 ) - 1
		assertEpsilonEquals(71.239322046f,sd);
	}

	@Test
	@Override
	public void distanceL1Point2D() {
		double d;
		d = this.r.distanceL1(new Point2fx(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceL1(new Point2fx(-1.2f,-3.4f));
		// closest point: (-0.332820118; -0.942990334)
		// vec(p->closest point) = (0.867179882; 2.457009666)
		// d = 0.867179882 + 2.457009666 
		assertEpsilonEquals(3.324189548f,d);

		d = this.r.distanceL1(new Point2fx(-1.2f,5.6f));
		// closest point: (-0.209529089; 0.977802414)
		// vec(p->closest point) = (0.990470911; -4.622197586)
		// d = 0.990470911 + 4.62219758
		assertEpsilonEquals(5.612668491f,d);

		d = this.r.distanceL1(new Point2fx(7.6f,5.6f));
		// length = 9.440338977
		// closest point: (0.805055837; 0.593199038)
		// vec(p->closest point) = (-6.794944163; -5.006800962)
		// d = 6.794944163 + 5.006800962
		assertEpsilonEquals(11.801745125f,d);
	}

	@Test
	@Override
	public void distanceLinfPoint2D() {
		double d;
		d = this.r.distanceLinf(new Point2fx(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceLinf(new Point2fx(-1.2f,-3.4f));
		// closest point: (-0.332820118; -0.942990334)
		// vec(p->closest point) = (0.867179882; 2.457009666)
		// d = max(0.867179882; 2.457009666) 
		assertEpsilonEquals(2.457009666f,d);

		d = this.r.distanceLinf(new Point2fx(-1.2f,5.6f));
		// closest point: (-0.209529089; 0.977802414)
		// vec(p->closest point) = (0.990470911; -4.622197586)
		// d = max(0.990470911; 4.62219758)
		assertEpsilonEquals(4.62219758f,d);

		d = this.r.distanceLinf(new Point2fx(7.6f,5.6f));
		// length = 9.440338977
		// closest point: (0.805055837; 0.593199038)
		// vec(p->closest point) = (-6.794944163; -5.006800962)
		// d = max(6.794944163; 5.006800962)
		assertEpsilonEquals(6.794944163f,d);
	}
		
	@Override
	public void toBoundingBox() {
		Rectangle2f b = this.r.toBoundingBox();
		assertEpsilonEquals(-1f, b.getMinX());
		assertEpsilonEquals(-1f, b.getMinY());
		assertEpsilonEquals(1f, b.getMaxX());
		assertEpsilonEquals(1f, b.getMaxY());
	}
	
	@Override
	public void testClone() {
		Circle2f b = this.r.clone();

		assertNotSame(b, this.r);
		assertEpsilonEquals(this.r.getRadius(), b.getRadius());
		assertEpsilonEquals(this.r.getCenter(), b.getCenter());
		
		b.set(this.r.getX()+1f, this.r.getX()+1f, this.r.getRadius()+1f);

		assertNotEpsilonEquals(this.r.getRadius(), b.getRadius());
		assertNotEpsilonEquals(this.r.getCenter(), b.getCenter());
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
	}
	
	/**
	 */
	@Test
	public void containsPoint2D() {
		assertTrue(this.r.contains(new Point2fx(0f, 0f)));
		
		assertFalse(this.r.contains(new Point2fx(-2.3f, -3.4f)));
		assertFalse(this.r.contains(new Point2fx(-2.3f, .5f)));
		assertFalse(this.r.contains(new Point2fx(-2.3f, 5.6f)));
		
		assertFalse(this.r.contains(new Point2fx(.5f, -3.4f)));
		assertTrue(this.r.contains(new Point2fx(.5f, .5f)));
		assertFalse(this.r.contains(new Point2fx(.5f, 5.6f)));

		assertFalse(this.r.contains(new Point2fx(5.6f, -3.4f)));
		assertFalse(this.r.contains(new Point2fx(5.6f, .5f)));
		assertFalse(this.r.contains(new Point2fx(5.6f, 5.6f)));
	}

	/**
	 */
	@Test
	public void getClosestPointTo() {
		Point2D p;
		
		p = this.r.getClosestPointTo(new Point2fx(0f, 0f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		
		p = this.r.getClosestPointTo(new Point2fx(.5f, .1f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(.1f, p.getY());

		p = this.r.getClosestPointTo(new Point2fx(-1.2f,-3.4f));
		assertEpsilonEquals(-0.332820118f, p.getX());
		assertEpsilonEquals(-0.942990334f, p.getY());

		p = this.r.getClosestPointTo(new Point2fx(-1.2f,5.6f));
		assertEpsilonEquals(-0.209529089f, p.getX());
		assertEpsilonEquals(0.977802414f, p.getY());

		p = this.r.getClosestPointTo(new Point2fx(7.6f,5.6f));
		assertEpsilonEquals(0.805055837f, p.getX());
		assertEpsilonEquals(0.593199038f, p.getY());
	}
	
	/**
	 */
	@Test
	public void getFarthestPointTo() {
		Point2D p;
		
		p = this.r.getClosestPointTo(new Point2fx(0f, 0f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(0f, p.getY());
		
		p = this.r.getClosestPointTo(new Point2fx(.5f, .1f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(.1f, p.getY());

		p = this.r.getClosestPointTo(new Point2fx(-1.2f,-3.4f));
		assertEpsilonEquals(-0.332820118f, p.getX());
		assertEpsilonEquals(-0.942990334f, p.getY());

		p = this.r.getClosestPointTo(new Point2fx(-1.2f,5.6f));
		assertEpsilonEquals(-0.209529089f, p.getX());
		assertEpsilonEquals(0.977802414f, p.getY());

		p = this.r.getClosestPointTo(new Point2fx(7.6f,5.6f));
		assertEpsilonEquals(0.805055837f, p.getX());
		assertEpsilonEquals(0.593199038f, p.getY());
	}

	/**
	 */
	@Test
	public void getPathIteratorVoid() {
		double t = .5522848f; // tangent length for a circle
		PathIterator2f pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 1f, 0f);
		assertElement(pi, PathElementType.CURVE_TO, 1f, t, t, 1f, 0f, 1f);
		assertElement(pi, PathElementType.CURVE_TO, -t, 1f, -1f, t, -1f, 0f);
		assertElement(pi, PathElementType.CURVE_TO, -1f, -t, -t, -1f, 0f, -1f);
		assertElement(pi, PathElementType.CURVE_TO, t, -1f, 1f, -t, 1f, 0f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}
	
	/**
	 */
	@Test
	public void getPathIteratorTransform2D() {
		Transform2D tr;
		PathIterator2f pi;
		double t = .5522848f; // tangent length for a circle when the circle is not rotated
		double h = .707106781f; // hypothenus of 1x1
		double r = .390524327f;
		
		tr = new Transform2D();
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 1f, 0f);
		assertElement(pi, PathElementType.CURVE_TO, 1f, t, t, 1f, 0f, 1f);
		assertElement(pi, PathElementType.CURVE_TO, -t, 1f, -1f, t, -1f, 0f);
		assertElement(pi, PathElementType.CURVE_TO, -1f, -t, -t, -1f, 0f, -1f);
		assertElement(pi, PathElementType.CURVE_TO, t, -1f, 1f, -t, 1f, 0f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 4.4f, 4.5f);
		assertElement(pi, PathElementType.CURVE_TO, 4.4f, 4.5f+t, 3.4f+t, 5.5f, 3.4f, 5.5f);
		assertElement(pi, PathElementType.CURVE_TO, 3.4f-t, 5.5f, 2.4f, 4.5f+t, 2.4f, 4.5f);
		assertElement(pi, PathElementType.CURVE_TO, 2.4f, 4.5f-t, 3.4f-t, 3.5f, 3.4f, 3.5f);
		assertElement(pi, PathElementType.CURVE_TO, 3.4f+t, 3.5f, 4.4f, 4.5f-t, 4.4f, 4.5f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, h, h);
		assertElement(pi, PathElementType.CURVE_TO, h-r, h+r, -h+r, h+r, -h, h);
		assertElement(pi, PathElementType.CURVE_TO, -h-r, h-r, -h-r, -h+r, -h, -h);
		assertElement(pi, PathElementType.CURVE_TO, -h+r, -h-r, h-r, -h-r, h, -h);
		assertElement(pi, PathElementType.CURVE_TO, h+r, -h+r, h+r, h-r, h, h);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void translateFloatFloat() {
		this.r.translate(2.3f, 3.4f);
		assertEpsilonEquals(2.3f, this.r.getX());
		assertEpsilonEquals(3.4f, this.r.getY());
		assertEpsilonEquals(1f, this.r.getRadius());
	}

	/**
	 */
	@Test
	public void setFloatFloatFloat() {
		this.r.set(3.4f, 4.5f, 5.6f);
		assertEpsilonEquals(3.4f, this.r.getX());
		assertEpsilonEquals(4.5f, this.r.getY());
		assertEpsilonEquals(5.6f, this.r.getRadius());

		this.r.set(3.4f, 4.5f, -7.8f);
		assertEpsilonEquals(3.4f, this.r.getX());
		assertEpsilonEquals(4.5f, this.r.getY());
		assertEpsilonEquals(7.8f, this.r.getRadius());
	}

	/**
	 */
	@Test
	public void setPoint2DFloat() {
		this.r.set(new Point2fx(3.4f, 4.5f), 5.6f);
		assertEpsilonEquals(3.4f, this.r.getX());
		assertEpsilonEquals(4.5f, this.r.getY());
		assertEpsilonEquals(5.6f, this.r.getRadius());

		this.r.set(new Point2fx(3.4f, 4.5f), -7.8f);
		assertEpsilonEquals(3.4f, this.r.getX());
		assertEpsilonEquals(4.5f, this.r.getY());
		assertEpsilonEquals(7.8f, this.r.getRadius());
	}

	/**
	 */
	@Test
	public void setCenterFloatFloat() {
		this.r.setCenter(3.4f, 4.5f);
		assertEpsilonEquals(3.4f, this.r.getX());
		assertEpsilonEquals(4.5f, this.r.getY());
	}

	/**
	 */
	@Test
	public void setCenterPoint2D() {
		this.r.setCenter(new Point2fx(3.4f, 4.5f));
		assertEpsilonEquals(3.4f, this.r.getX());
		assertEpsilonEquals(4.5f, this.r.getY());
	}

	/**
	 */
	@Test
	public void setRadius() {
		this.r.setRadius(5.6f);
		assertEpsilonEquals(5.6f, this.r.getRadius());
		this.r.setRadius(-7.8f);
		assertEpsilonEquals(7.8f, this.r.getRadius());
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
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

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
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

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
		assertTrue(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));

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
		assertTrue(this.r.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIterator()));

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
	public void setShape2f() {
		this.r.set(new Rectangle2f(10, 12, 14, 16));
		assertEpsilonEquals(17f, this.r.getX());
		assertEpsilonEquals(20f, this.r.getY());
		assertEpsilonEquals(7f, this.r.getRadius());
	}

	@Test
	public void containsCirclePoint() {
		throw new UnsupportedOperationException();
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
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

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
		assertTrue(this.r.intersects(p));
		p.closePath();
		assertTrue(this.r.intersects(p));

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
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));

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
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));
		p.closePath();
		assertTrue(this.r.intersects(p.getPathIteratorProperty()));

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

}