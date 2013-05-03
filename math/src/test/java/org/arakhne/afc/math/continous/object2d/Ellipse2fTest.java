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
import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.generic.PathElementType;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.matrix.Transform2D;


/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Ellipse2fTest extends AbstractRectangularShape2fTestCase<Ellipse2f> {
	
	@Override
	protected Ellipse2f createShape() {
		return new Ellipse2f(0f, 0f, 2f, 1f);
	}

	@Override
	public void testIsEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.setMinX(1f);
		assertFalse(this.r.isEmpty());
		this.r.setMinY(1f);
		assertFalse(this.r.isEmpty());
		this.r.setMinX(2f);
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
		Ellipse2f b = this.r.clone();

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
	public void testSetMinX() {
		this.r.setMinX(2.3f);
		assertEpsilonEquals(2f, this.r.getMinX());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
		this.r.setMinX(-3.4f);
		assertEpsilonEquals(-3.4f, this.r.getMinX());
		assertEpsilonEquals(2.3f, this.r.getMaxX());
	}

	@Override
	public void testTranslateFloatFloat() {
		this.r.translate(2.3f, 3.4f);
		assertEpsilonEquals(2.3f, this.r.getMinX());
		assertEpsilonEquals(3.4f, this.r.getMinY());
		assertEpsilonEquals(4.3f, this.r.getMaxX());
		assertEpsilonEquals(4.4f, this.r.getMaxY());
	}
	
	@Override
	public void testSetHeight() {
		this.r.setHeight(2.3f);
		assertEpsilonEquals(0f, this.r.getMinX());
		assertEpsilonEquals(0f, this.r.getMinY());
		assertEpsilonEquals(2f, this.r.getMaxX());
		assertEpsilonEquals(2.3f, this.r.getMaxY());
		assertEpsilonEquals(2.3f, this.r.getHeight());
	}

	@Override
	public void testDistancePoint2D() {
		float d;
		d = this.r.distance(new Point2f(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distance(new Point2f(0f,0f));
		// ecenter = (1; 0.5)
		// a = 1
		// b = .5
		// a*a = 1
		// b*b = .25
		// x0 = x - a = 0 - 1 = -1
		// y0 = y - b = 0 - 0.5 = -0.5
		// denom*denom = a*a*y0*y0 + b*b*x0*x0 = 0.5
		// denom = 0.707106781
		// f = (a*b)/denom = 0.707106781
		// x = f * x0 = -0.707106781
		// y = f * y0 = -0.353553391
		// px = x + a = -0.707106781 + 1 = 0.292893219
		// py = y + b = -0.353553391 + 0.5 = 0.146446609
		// Closest point is (0.292893219;0.146446609)
		assertEpsilonEquals(0.327464574f,d);
	}

	@Override
	public void testDistanceSquaredPoint2D() {
		float d;
		d = this.r.distanceSquared(new Point2f(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceSquared(new Point2f(0f,0f));
		// See testDistancePoint2D for details
		// Closest point is (0.292893219;0.146446609)
		assertEpsilonEquals(0.107233047f,d);
	}

	@Override
	public void testDistanceL1Point2D() {
		float d;
		d = this.r.distanceL1(new Point2f(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceL1(new Point2f(0f,0f));
		// See testDistancePoint2D for details
		// Closest point is (0.292893219;0.146446609)
		assertEpsilonEquals(0.439339828f,d);
	}

	@Override
	public void testDistanceLinfPoint2D() {
		float d;
		d = this.r.distanceLinf(new Point2f(.5f,.5f));
		assertEpsilonEquals(0f,d);

		d = this.r.distanceLinf(new Point2f(0f,0f));
		// See testDistancePoint2D for details
		// Closest point is (0.292893219;0.146446609)
		assertEpsilonEquals(0.292893219f,d);
	}
		
	@Override
	public void testToBoundingBox() {
		Rectangle2f b = this.r.toBoundingBox();
		assertEpsilonEquals(0f, b.getMinX());
		assertEpsilonEquals(0f, b.getMinY());
		assertEpsilonEquals(2f, b.getMaxX());
		assertEpsilonEquals(1f, b.getMaxY());
	}
	
	/**
	 */
	public void testContainsFloatFloat() {
		assertFalse(this.r.contains(0f, 0f));
		assertTrue(this.r.contains(.5f, .5f));
		
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
	public void testContainsPoint2D() {
		assertFalse(this.r.contains(new Point2f(0f, 0f)));
		assertTrue(this.r.contains(new Point2f(.5f, .5f)));
		
		assertFalse(this.r.contains(new Point2f(-2.3f, -3.4f)));
		assertFalse(this.r.contains(new Point2f(-2.3f, .5f)));
		assertFalse(this.r.contains(new Point2f(-2.3f, 5.6f)));
		
		assertFalse(this.r.contains(new Point2f(.5f, -3.4f)));
		assertTrue(this.r.contains(new Point2f(.5f, .5f)));
		assertFalse(this.r.contains(new Point2f(.5f, 5.6f)));

		assertFalse(this.r.contains(new Point2f(5.6f, -3.4f)));
		assertFalse(this.r.contains(new Point2f(5.6f, .5f)));
		assertFalse(this.r.contains(new Point2f(5.6f, 5.6f)));
	}

	/**
	 */
	public void testGetClosestPointTo() {
		Point2D p;
		
		p = this.r.getClosestPointTo(new Point2f(.5f, .4f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(.4f, p.getY());
		
		p = this.r.getClosestPointTo(new Point2f(-2.3f, -3.4f));
		// ecenter = (1; 0.5)
		// a = 1
		// b = .5
		// a*a = 1
		// b*b = .25
		// x0 = x - a = -2.3 - 1 = -3.3
		// y0 = y - b = -3.4 - 0.5 = -3.9
		// denom*denom = a*a*y0*y0 + b*b*x0*x0 = 17.9325
		// denom = 4.234678264
		// f = (a*b)/denom = 0.118072724
		// x = f * x0 = -0.389639991
		// y = f * y0 = -0.460483626
		// px = x + a = -0.389639991 + 1 = 0.610360009
		// py = y + b = -0.460483626 + 0.5 = 0.039516374
		assertEpsilonEquals(0.610360009f, p.getX());
		assertEpsilonEquals(0.039516374f, p.getY());
		
		p = this.r.getClosestPointTo(new Point2f(1f, 5.6f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());
	}
	
	/**
	 */
	public void testGetPathIteratorVoid() {
		float vt = .27614236f; // vertical tangent length for the ellipse
		float ht = .5522847f; // horizontal tangent length for the ellipse
		PathIterator2f pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 2f, .5f);
		assertElement(pi, PathElementType.CURVE_TO, 2f, .5f+vt, 1f+ht, 1f, 1f, 1f);
		assertElement(pi, PathElementType.CURVE_TO, 1f-ht, 1f, 0f, .5f+vt, 0f, .5f);
		assertElement(pi, PathElementType.CURVE_TO, 0f, .5f-vt, 1f-ht, 0f, 1f, 0f);
		assertElement(pi, PathElementType.CURVE_TO, 1f+ht, 0f, 2f, .5f-vt, 2f, .5f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}
	
	/**
	 */
	public void testGetPathIteratorTransform2D() {
		Transform2D tr;
		PathIterator2f pi;
		float vt = .27614236f; // vertical tangent length for the ellipse
		float ht = .5522847f; // horizontal tangent length for the ellipse

		tr = new Transform2D();
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 2f, .5f);
		assertElement(pi, PathElementType.CURVE_TO, 2f, .5f+vt, 1f+ht, 1f, 1f, 1f);
		assertElement(pi, PathElementType.CURVE_TO, 1f-ht, 1f, 0f, .5f+vt, 0f, .5f);
		assertElement(pi, PathElementType.CURVE_TO, 0f, .5f-vt, 1f-ht, 0f, 1f, 0f);
		assertElement(pi, PathElementType.CURVE_TO, 1f+ht, 0f, 2f, .5f-vt, 2f, .5f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 5.4f, 5f);
		assertElement(pi, PathElementType.CURVE_TO, 5.4f, 5f+vt, 4.4f+ht, 5.5f, 4.4f, 5.5f);
		assertElement(pi, PathElementType.CURVE_TO, 4.4f-ht, 5.5f, 3.4f, 5f+vt, 3.4f, 5f);
		assertElement(pi, PathElementType.CURVE_TO, 3.4f, 5f-vt, 4.4f-ht, 4.5f, 4.4f, 4.5f);
		assertElement(pi, PathElementType.CURVE_TO, 4.4f+ht, 4.5f, 5.4f, 5f-vt, 5.4f, 5f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 1.06066f, 1.76777f);
		assertElement(pi, PathElementType.CURVE_TO, 0.86540f, 1.96303f, 0.39052f, 1.8047f, 0f, 1.41421f);
		assertElement(pi, PathElementType.CURVE_TO, -0.39052f, 1.02369f, -0.54882f, 0.54882f, -0.35355f, 0.35355f);
		assertElement(pi, PathElementType.CURVE_TO, -0.15829f, 0.15829f, 0.31658f, 0.31658f, 0.70711f, 0.70711f);
		assertElement(pi, PathElementType.CURVE_TO, 1.09763f, 1.09763f, 1.25592f, 1.57250f, 1.06066f, 1.76777f);
		assertElement(pi, PathElementType.CLOSE);
		assertNoElement(pi);
	}

	/**
	 */
	public static void testContainsEllipseRectangle() {
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, .0f, .0f));
		assertFalse(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, .1f, .1f));
		assertTrue(Ellipse2f.containsEllipseRectangle(0f, 0f, 1f, 1f,
				.25f, .25f, .5f, .5f));
	}
	
	/**
	 */
	public static void testIntersectsEllipseEllipse() {
		assertTrue(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertFalse(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				-5f, -5f, .0f, .0f));
		assertFalse(Ellipse2f.intersectsEllipseEllipse(0f, 0f, 1f, 1f,
				-5f, -5f, .1f, .1f));
	}

	/**
	 */
	public static void testIntersectsEllipseRectangle() {
		assertTrue(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertFalse(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, .0f, .0f));
		assertFalse(Ellipse2f.intersectsEllipseRectangle(0f, 0f, 1f, 1f,
				-5f, -5f, .1f, .1f));
	}

	/**
	 */
	public static void testIntersectsEllipseLine() {
		assertTrue(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertTrue(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertTrue(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				-5f, -5f, .0f, .0f));
		assertTrue(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				-5f, -5f, .1f, .1f));
		assertTrue(Ellipse2f.intersectsEllipseLine(0f, 0f, 1f, 1f,
				-5f, -5f, .4f, .3f));

		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.7773438f, -3.0272121f, 6.7890625f, -3.1188917f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.7890625f, -3.1188917f, 6.8007812f, -3.2118688f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.8007812f, -3.2118688f, 6.8125f, -3.3061523f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.8125f, -3.3061523f, 6.8242188f, -3.401752f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.8242188f, -3.401752f, 6.8359375f, -3.4986773f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.8359375f, -3.4986773f, 6.8476562f, -3.5969372f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.8476562f, -3.5969372f, 6.859375f, -3.6965408f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.859375f, -3.6965408f, 6.8710938f, -3.7974977f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.8710938f, -3.7974977f, 6.8828125f, -3.8998175f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.8828125f, -3.8998175f, 6.8945312f, -4.003509f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.8945312f, -4.003509f, 6.90625f, -4.1085815f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.90625f, -4.1085815f, 6.9179688f, -4.2150445f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.9179688f, -4.2150445f, 6.9296875f, -4.3229074f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.9296875f, -4.3229074f, 6.9414062f, -4.4321795f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.9414062f, -4.4321795f, 6.953125f, -4.5428696f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.953125f, -4.5428696f, 6.9648438f, -4.6549873f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.9648438f, -4.6549873f, 6.9765625f, -4.7685423f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.9765625f, -4.7685423f, 6.9882812f, -4.8835435f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				6f, -5f, 1f, 2f, 6.9882812f, -4.8835435f, 7f, -5f));

		assertTrue(Ellipse2f.intersectsEllipseLine(
				0f, 0f, 1f, 2f, .5f, -1f, .5f, 2f));
		assertTrue(Ellipse2f.intersectsEllipseLine(
				0f, 0f, 1f, 2f, .5f, -1f, .5f, -.5f));
	}

	/**
	 */
	public static void testIntersectsEllipseSegment() {
		assertTrue(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				-5f, -5f, 1f, 1f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, 5f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				.5f, .5f, 5f, .6f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				-5f, -5f, 5f, 5f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				-5f, -5f, -4f, -4f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				-5f, -5f, 4f, -4f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				5f, -5f, 6f, 5f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				-5f, -5f, .0f, .0f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				-5f, -5f, .1f, .1f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(0f, 0f, 1f, 1f,
				-5f, -5f, .4f, .3f));
		
		assertFalse(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.7773438f, -3.0272121f, 6.7890625f, -3.1188917f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.7890625f, -3.1188917f, 6.8007812f, -3.2118688f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.8007812f, -3.2118688f, 6.8125f, -3.3061523f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.8125f, -3.3061523f, 6.8242188f, -3.401752f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.8242188f, -3.401752f, 6.8359375f, -3.4986773f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.8359375f, -3.4986773f, 6.8476562f, -3.5969372f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.8476562f, -3.5969372f, 6.859375f, -3.6965408f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.859375f, -3.6965408f, 6.8710938f, -3.7974977f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.8710938f, -3.7974977f, 6.8828125f, -3.8998175f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.8828125f, -3.8998175f, 6.8945312f, -4.003509f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.8945312f, -4.003509f, 6.90625f, -4.1085815f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.90625f, -4.1085815f, 6.9179688f, -4.2150445f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.9179688f, -4.2150445f, 6.9296875f, -4.3229074f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.9296875f, -4.3229074f, 6.9414062f, -4.4321795f));
		assertTrue(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.9414062f, -4.4321795f, 6.953125f, -4.5428696f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.953125f, -4.5428696f, 6.9648438f, -4.6549873f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.9648438f, -4.6549873f, 6.9765625f, -4.7685423f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.9765625f, -4.7685423f, 6.9882812f, -4.8835435f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(
				6f, -5f, 1f, 2f, 6.9882812f, -4.8835435f, 7f, -5f));

		assertTrue(Ellipse2f.intersectsEllipseSegment(
				0f, 0f, 1f, 2f, .5f, -1f, .5f, 2f));
		assertFalse(Ellipse2f.intersectsEllipseSegment(
				0f, 0f, 1f, 2f, .5f, -1f, .5f, -.5f));
	}
	
	/**
	 */
	@Override
	public void testIntersectsPath2f() {
		Path2f p;

		this.r.set(0f, 0f, 1.1f, 1f);
		
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

		this.r.set(0f, 0f, 1.1f, 1f);
		
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