///* 
// * $Id$
// * 
// * Copyright (C) 2013 Christophe BOHRHAUER.
// * 
// * This library is free software; you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public
// * License as published by the Free Software Foundation; either
// * version 2.1 of the License, or (at your option) any later version.
// * 
// * This library is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// * Lesser General Public License for more details.
// * 
// * You should have received a copy of the GNU Lesser General Public
// * License along with this library; if not, write to the Free Software
// * Foundation, Inc.f, 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// * This program is free software; you can redistribute it and/or modify
// */
package org.arakhne.afc.math;

import org.arakhne.afc.math.geometry.GeometryUtil;
import org.arakhne.afc.math.geometry.system.CoordinateSystemNotFoundException;
import org.arakhne.afc.math.geometry2d.Point2D;
import org.arakhne.afc.math.geometry2d.continuous.Point2f;
import org.arakhne.afc.math.geometry2d.continuous.Vector2f;
import org.arakhne.afc.math.geometry3d.continuous.Point3f;
import org.arakhne.afc.math.geometry3d.continuous.Vector3f;

/**
 * Test for {@link GeometryUtil}
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GeometryUtilTest extends AbstractMathTestCase {

	/**
	 * Also test the returnDistance of DistanceSquaredPointSegment.
	 */
	public void testDistancePointSegmentFloatFloatFloatFloatFloatFloatPoint2D_returnDistance() {
		float px, py, x1, y1, x2, y2, expected;

		x1 = y1 = x2 = y2 = 5;
		px = -2;
		py = 2;
		assertEpsilonEquals(GeometryUtil.distancePointPoint(x1, y1, px, py),
				GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, null));

		x1 = y1 = 5;
		x2 = 8;
		y2 = 3;

		px = 5.46f;
		py = 2.7f;
		expected = 1.658553586713435f;
		assertEpsilonEquals(expected,
				GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, null));

		px = 7.78f;
		py = 3.84f;
		expected = 0.576888204074238f;
		assertEpsilonEquals(expected,
				GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, null));

		px = 5.12f;
		py = 5.84f;
		expected = 0.848528137423857f;
		assertEpsilonEquals(expected,
				GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, null));

		px = 9;
		py = 3;
		expected = 1.f;
		assertEpsilonEquals(expected,
				GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, null));

		px = 6.56f;
		py = 3.96f;
		expected = 0f;
		assertEpsilonEquals(expected,
				GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, null));
	}

	/**
	 * Also test the closestPoint of DistanceSquaredPointSegment.
	 */
	public void testDistancePointSegmentFloatFloatFloatFloatFloatFloatPoint2D_closestPoint() {
		float px, py, x1, y1, x2, y2;
		Point2f nearestPoint, expected;

		nearestPoint = new Point2f();

		x1 = y1 = x2 = y2 = 5;
		px = -2;
		py = 2;
		GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, nearestPoint);
		assertEpsilonEquals(new Point2f(5, 5), nearestPoint);

		x1 = y1 = 5;
		x2 = 8;
		y2 = 3;

		px = 5.46f;
		py = 2.7f;
		expected = new Point2f(6.38, 4.08);
		GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, nearestPoint);
		assertEpsilonEquals(expected, nearestPoint);

		px = 7.78f;
		py = 3.84f;
		expected = new Point2f(7.46, 3.36);
		GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, nearestPoint);
		assertEpsilonEquals(expected, nearestPoint);

		px = 5.12f;
		py = 5.84f;
		expected = new Point2f(5, 5);
		GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, nearestPoint);
		assertEpsilonEquals(expected, nearestPoint);

		px = 9;
		py = 3;
		expected = new Point2f(8, 3);
		GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, nearestPoint);
		assertEpsilonEquals(expected, nearestPoint);

		px = 6.56f;
		py = 3.96f;
		expected = new Point2f(6.56, 3.96);
		GeometryUtil.distancePointSegment(px, py, x1, y1, x2, y2, nearestPoint);
		assertEpsilonEquals(expected, nearestPoint);
	}

	/**
	 */
	public void testDistancePointSegmentFloatFloatFloatFloatFloatFloatFloatFloatFloat() {

		//Giving points P1(1,-1,1), P2(-1,1,0) and M(x,y,z). Let's make the segment P1P2. 3 cases. 

		//The point is before P1 i.e. giving the affine hyperplane define by P1 and  vector P1P2.
		assertEpsilonEquals(GeometryUtil.distancePointPoint(1, -1, 1, -1, -2, 4), GeometryUtil.distancePointSegment(-1, -2, 4, 1, -1, 1,-1,1,0));
		//TODO : test if this distance remains equal when moving the point on the half sphere  (P1, distance(P1,M))
		//The point is after P2
		assertEpsilonEquals(GeometryUtil.distancePointPoint(-1, 1, 0, -1, 2, -4), GeometryUtil.distancePointSegment(-1, 2, -4, 1, -1, 1,-1,1,0));
		//TODO c.f. previous note

		//The point is between P1 and P2.
		assertEpsilonEquals(GeometryUtil.distancePointLine(0, 0, 0, 1, -1, 1,-1,1,0), GeometryUtil.distancePointSegment(0, 0, 0, 1, -1, 1,-1,1,0));
		//TODO test if this distance remain the same when rotate the point around the line P1P2

	}

	/**
	 */
	public void testDistancePointLineFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = new Point2f(0.5f, 0.5f);
		Point2f p2 = new Point2f(-1,1);
		Point2f p3 = new Point2f(1, -1);

		float a1 = (p3.getY() - p2.getY()) / (p3.getX() - p2.getX());
		float b1 = p2.getY() - a1 * p2.getX();

		float a2 = -1.f / a1;
		float b2 = p1.getY() - a2 * p1.getX();

		float yi = (b1 - ((a1 * b2) / a2)) / (1.f - (a1 / a2));
		float xi = (yi - b2) / a2;

		float xx = xi - p1.getX();
		float yy = yi - p1.getY();
		float distance = (float) Math.sqrt(xx * xx + yy * yy);

		assertEpsilonEquals(distance, GeometryUtil.distancePointLine(p1.getX(),
				p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY()));

		assertEpsilonEquals(distance, GeometryUtil.distancePointLine(-0.5f, -0.5f, p2.getX(), p2.getY(), p3.getX(), p3.getY()));

	}

	/**
	 */
	public void testgetIntersectionFactorSegmentSegmentFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(1.f, 1.f,
				2.f, 2.f, 1.f, 1.f, 2.f, 2.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(1.f, 1.f,
				2.f, 2.f, 1.f, 1.f, -2.f, -2.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(1.f, 1.f,
				2.f, 2.f, 2.f, 2.f, 1.f, 1.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(1.f, 1.f,
				2.f, 2.f, 1.f, 2.f, 2.f, 3.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(1.f, 1.f,
				2.f, 2.f, 1.f, 1.f, 3.f, 3.f));

		assertEpsilonEquals(0.f,
				GeometryUtil.getIntersectionFactorSegmentSegment(1.f, 1.f, 2.f,
						2.f, 1.f, 1.f, 3.f, 9.f));

		assertEpsilonEquals(1.f,
				GeometryUtil.getIntersectionFactorSegmentSegment(1.f, 1.f, 2.f,
						2.f, 10.f, 0.f, 2.f, 2.f));

		assertEpsilonEquals(.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(-1.f, -1.f,
						1.f, 1.f, -1.f, 1.f, 1.f, -1.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(0.f, 0.f,
				1.f, 0.f, 1.5f, 0.f, 10.f, 0.f));

		assertNaN(GeometryUtil.getIntersectionFactorSegmentSegment(-1.f, -1.f,
				1.f, 1.f, 2.f, 10.f, 20.f, -20.f));

		assertEpsilonEquals(.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(-50.f, -50.f,
						50.f, 50.f, -50.f, 50.f, 50.f, -50.f));

		assertEpsilonEquals(.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(-50.f, -50.f,
						50.f, 50.f, 50.f, -50.f, -50.f, 50.f));
	}

	/**
	 */
	public void testGetIntersectionPointLineLineFloatFloatFloatFloatFloatFloatFloatFloat() {
		
		Point2f p;
		GeometryUtil.getIntersectionPointLineLine(-1f, -1f, 1f,
				1f, 1f, -1f, 1f, -1f, p);
		assertNull(p);
		
		p = new Point2f();

		GeometryUtil.getIntersectionPointLineLine(1f, -1f, 1f,
				1f, 1f, -1f, 1f, -1f, p);
		assertEpsilonEquals(new Point2f(0,0),p);

		
		GeometryUtil.getIntersectionPointLineLine(1f, -1f, 2f,
				0f, 0f, 3f, 1f, 4f, p);
		assertNull(p);

		p = new Point2f();
		GeometryUtil.getIntersectionPointLineLine(-1f, 1f,  1f,
				1f, -1f, -1f, 2f, 0f, p);
		assertEpsilonEquals(new Point2f(0, 0), p);

		assertEpsilonEquals(new Point2f(2.f, 44.5f),
				(Point2f) GeometryUtil.getIntersectionPointLineLine(2.f, -32.f,
						2.f, 100.f, 101.f, 44.5f, 100.f, 44.5f, null));

		// Equation resolution with GNU Octave.
		//
		// Linear Equation Resolution:
		// a.getX() + b.getY() = c
		// d.getX() + e.getY() = f
		//
		// A = [ a b ]
		// [ d e ]
		//
		// b = [ c ]
		// [ f ]
		//
		// A \ b = [ x ]
		// [ y ]
		//
		// Back to line intersection problem:
		//
		// P1 + m.(P2-P1) = I
		// P3 + n.(P4-P3.f) = I
		//
		// m.(x2-x1) - n.(x4-x3.f) = x3-x1
		// m.(y2-y1) - n.(y4-y3.f) = y3-y1
		//
		// A = [ (x2-x1) (x3-x4) ]
		// [ (y2-y1) (y3-y4) ]
		//
		// b = [ x3-x1 ]
		// [ y3-y1 ]
		//
		// R = A \ b = [ m ]
		// [ n ]
		//
		// [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ]
		// [ y ] [ y1 ] [ y2-y1 ]

		// Octave Code:
		// output_precision(16)
		// P = { -2.f, 3.f, -1.f, 6f, -3.f, 28.f, 0.f, 24. }
		// x1=P{1}, y1=P{2}, x2=P{3}, y2=P{4}, x3=P{5}, y3=P{6}, x4=P{7},
		// y4=P{8}
		// A = [ x2-x1, x3-x4; y2-y1, y3-y4 ]
		// b = [ x3-x1; y3-y1 ]
		// R = A \ b
		// I = [ float(x1 + R(1) * (x2-x1)), float(y1 + R(1) * (y2-y1)) ]
		assertEpsilonEquals(new Point2f(3.461538461538462e+00,
				1.938461538461539e+01),
				(Point2f) GeometryUtil.getIntersectionPointLineLine(-2.f, 3.f,
						-1.f, 6.f, -3.f, 28.f, 0.f, 24.f, null));
	}

	/**
	 */
	public void testDistanceRelativeLinePointFloatFloatFloatFloatFloatFloat() {
		float px, py, x1, y1, x2, y2, expected;

		x1 = y1 = x2 = y2 = 5;
		px = -2;
		py = 2;

		assertEpsilonEquals(GeometryUtil.distancePointPoint(x1, y1, px, py),
				GeometryUtil.distanceRelativePointLine(px, py, x1, y1, x2, y2));

		x1 = y1 = 5;
		x2 = 8;
		y2 = 3;

		px = 5.46f;
		py = 2.7f;
		expected = 1.658553586713435f;
		assertEpsilonEquals(expected,
				GeometryUtil.distanceRelativePointLine(px, py, x1, y1, x2, y2));
		assertEpsilonEquals(-expected,
				GeometryUtil.distanceRelativePointLine(px, py, x2, y2, x1, y1));

		px = 7.78f;
		py = 3.84f;
		expected = -0.576888204074238f; //TODO : revérifier
		assertEpsilonEquals(expected,
				GeometryUtil.distanceRelativePointLine(px, py, x1, y1, x2, y2));
		assertEpsilonEquals(-expected,
				GeometryUtil.distanceRelativePointLine(px, py, x2, y2, x1, y1));

		px = 5.12f;
		py = 5.84f;
		expected = -0.765486270790817f;
		assertEpsilonEquals(expected,
				GeometryUtil.distanceRelativePointLine(px, py, x1, y1, x2, y2));
		assertEpsilonEquals(-expected,
				GeometryUtil.distanceRelativePointLine(px, py, x2, y2, x1, y1));

		px = 9;
		py = 3;
		expected = - 0.55470019622523f;
		assertEpsilonEquals(expected,
				GeometryUtil.distanceRelativePointLine(px, py, x1, y1, x2, y2));
		assertEpsilonEquals(-expected,
				GeometryUtil.distanceRelativePointLine(px, py, x2, y2, x1, y1));

		px = 6.56f;
		py = 3.96f;
		expected = 0f;
		assertEpsilonEquals(expected,
				GeometryUtil.distanceRelativePointLine(px, py, x1, y1, x2, y2));
		assertEpsilonEquals(-expected,
				GeometryUtil.distanceRelativePointLine(px, py, x2, y2, x1, y1));
		//TODO rajouter test f(a,bc) = -f(a,c,b)
	}

	/**
	 */
	public void testGetLineLineIntersectionFactorFloatFloatFloatFloatFloatFloatFloatFloat() {
		assertNaN(GeometryUtil.getIntersectionFactorLineLine(1.f, 1.f, 2.f,
				2.f, 1.f, 1.f, 2.f, 2.f));

		assertNaN(GeometryUtil.getIntersectionFactorLineLine(1.f, 1.f, 2.f,
				2.f, 1.f, 4.f, 2.f, 5.f));

		assertEpsilonEquals(.5f, GeometryUtil.getIntersectionFactorLineLine(
				-1.f, -1.f, 1.f, 1.f, -1.f, 1.f, 1.f, -1.f));

		assertEpsilonEquals(.579545455f,
				GeometryUtil.getIntersectionFactorLineLine(2.f, -32.f, 2.f,
						100.f, 101.f, 44.5f, 100, 44.5f));

		// Equation resolution with GNU Octave.
		//
		// Linear Equation Resolution:
		// a.getX() + b.getY() = c
		// d.getX() + e.getY() = f
		//
		// A = [ a b ]
		// [ d e ]
		//
		// b = [ c ]
		// [ f ]
		//
		// A \ b = [ x ]
		// [ y ]
		//
		// Back to line intersection problem:
		//
		// P1 + m.(P2-P1) = I
		// P3 + n.(P4-P3.f) = I
		//
		// m.(x2-x1) - n.(x4-x3.f) = x3-x1
		// m.(y2-y1) - n.(y4-y3.f) = y3-y1
		//
		// A = [ (x2-x1) (x3-x4) ]
		// [ (y2-y1) (y3-y4) ]
		//
		// b = [ x3-x1 ]
		// [ y3-y1 ]
		//
		// R = A \ b = [ m ]
		// [ n ]
		//
		// [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ]
		// [ y ] [ y1 ] [ y2-y1 ]

		// Octave Code:
		// output_precision(16)
		// P = { -2.f, 3.f, -1.f, 6f, -3.f, 28.f, 0.f, 24. }
		// x1=P{1}, y1=P{2}, x2=P{3}, y2=P{4}, x3=P{5}, y3=P{6}, x4=P{7},
		// y4=P{8}
		// A = [ x2-x1, x3-x4; y2-y1, y3-y4 ]
		// b = [ x3-x1; y3-y1 ]
		// R = A \ b
		// I = [ float(x1 + R(1) * (x2-x1)), float(y1 + R(1) * (y2-y1)) ]
		assertEpsilonEquals(5.461538461538462e+00f,
				GeometryUtil.getIntersectionFactorLineLine(-2.f, 3.f, -1.f, 6f,
						-3.f, 28.f, 0.f, 24.f));
	}

	/**
	 */
	public void testclosestPointPointSolidEllipseFloatFloatFloatFloatFloatFloatFloatBoolean() {
		
		float ex, ey, ew, eh, px, py;
		
		px = 4;
		py = 5;
		ex = 1;
		ey = 3;
		ew = 0;
		eh = 0;
		
		assertEpsilonEquals(new Point2f(1, 3), (Point2f)GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, true, null));
		
		
		eh = 4;
		Point2D pts = new Point2f();
		GeometryUtil.distancePointSegment(px, py, ex, ey, ex, ey+eh, pts);
		assertEpsilonEquals((Point2f)pts, (Point2f)GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, true, null));
		
		eh = 0;
		ew = 2;
		GeometryUtil.distancePointSegment(px, py, ex, ey, ex+ew, ey, pts);
		assertEpsilonEquals((Point2f)pts, (Point2f)GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, true, null));
		
		
		ex = 6;
		ey = 2;
		ew = 9.4f;
		eh = 4;
		
		
		px = 6;
		py = 4;	
		assertNull(GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, true, null));
		assertEpsilonEquals(new Point2f(6,4), (Point2f)GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, false, null));
		px = 8;
		py = 4;	
		assertNull(GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, true, null));
		assertEpsilonEquals(new Point2f(8,4), (Point2f)GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, false, null));
	
		px = 7;
		py = 1;	
		assertEpsilonEquals(new Point2f(7.804173750531761,2.424716120899507), (Point2f)GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, true, null));

	}

	/**
	 */
	public void testIsParallelLinesFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = new Point2f(-100.f, -100.f);
		Point2f p2 = new Point2f(100.f, 100.f);
		Point2f p3, p4;

		p3 = new Point2f(p1);
		p4 = new Point2f(p2);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(),
				p4.getY(), 0));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(),
				p3.getY(), 0));

		p3 = new Point2f(p1.getX() + 100.f, p1.getY() + 100.f);
		p4 = new Point2f(p2.getX() + 100.f, p2.getY() + 100.f);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(),
				p4.getY(), 0));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(),
				p3.getY(), 0));

		p3 = new Point2f(p1.getX() - 100.f, p1.getY() - 100.f);
		p4 = new Point2f(p2.getX() - 100.f, p2.getY() - 100.f);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(),
				p4.getY(), 0));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(),
				p3.getY(), 0));

		p3 = new Point2f(p1.getX() + 100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(),
				p4.getY(), 0));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(),
				p3.getY(), 0));

		p3 = new Point2f(p1.getX() - 100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(),
				p4.getY(), 0));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(),
				p3.getY(), 0));

		p3 = new Point2f(0.f, 0.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(),
				p4.getY(), 0));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(),
				p3.getY(), 0));

		p3 = new Point2f(100.f, -10.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(),
				p4.getY(), 0));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(),
				p3.getY(), 0));

		p3 = new Point2f(p1.getX() - 100.f, p1.getY());
		p4 = new Point2f(p2.getX() - 100.f, p2.getY());
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(),
				p4.getY(), 0));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(),
				p3.getY(), 0));

		p3 = new Point2f(p1.getX(), p1.getY() + 100);
		p4 = new Point2f(p2.getX(), p2.getY() + 100);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(),
				p4.getY(), 0));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p4.getX(), p4.getY(), p3.getX(),
				p3.getY(), 0));
	}

	/**
	 */
	public void testIsParallelLinesFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = new Point3f(-100.f, -100.f, 50.f);
		Point3f p2 = new Point3f(100.f, 100.f, 50.f);
		Point3f p3, p4;

		p3 = new Point3f(p1);
		p4 = new Point3f(p2);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p4.getX(),
				p4.getY(), p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX() + 100.f, p1.getY() + 100.f, p1.getZ());
		p4 = new Point3f(p2.getX() + 100.f, p2.getY() + 100.f, p2.getZ());
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p4.getX(),
				p4.getY(), p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX() - 100.f, p1.getY() - 100.f, p1.getZ());
		p4 = new Point3f(p2.getX() - 100.f, p2.getY() - 100.f, p2.getZ());
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p4.getX(),
				p4.getY(), p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX() + 100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p4.getX(),
				p4.getY(), p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX() - 100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p4.getX(),
				p4.getY(), p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(0.f, 0.f, 0.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p4.getX(),
				p4.getY(), p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(100.f, -10.f, 50.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p4.getX(),
				p4.getY(), p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 0.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p4.getX(),
				p4.getY(), p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 1.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertTrue(GeometryUtil.isParallelLines(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p4.getX(),
				p4.getY(), p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));
	}

	/**
	 */
	public void testIsCollinearPointsFloatFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = new Point2f(100.f, 100.f);
		Point2f p2 = new Point2f(-100.f, -100.f);
		Point2f p3;

		p3 = new Point2f(p1);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(p2);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(0.f, 0.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(-50.f, 50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(50.f, -50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(-1000.f, -1000.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(256.f, 256.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p2.getX(), p2.getY(), p3.getX(), p3.getY(), 0));
	}

	/**
	 */
	public void testIsCollinearPointsFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = new Point3f(100.f, 100.f, 50.f);
		Point3f p2 = new Point3f(-100.f, -100.f, 50.f);
		Point3f p3;

		// Numerical application from:
		// http://www.jtaylor1142001.net/calcjat/Solutions/3Dim/Collinear.htm
		assertTrue(GeometryUtil.isCollinearPoints(2.f, -1.f, 4.f, -4.f, -4.f,
				2.f, 14.f, 5.f, 8.f, 0));
		assertFalse(GeometryUtil.isCollinearPoints(2.f, -1.f, 4.f, -4.f, -4.f,
				2.f, 14.f, 5.f, 7.f, 0));

		p3 = new Point3f(p1);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p2);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(0.f, 0.f, 50.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(-50.f, 50.f, 50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(50.f, -50.f, 50.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(-1000.f, -1000.f, 50.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(256.f, 256.f, 50.f);
		assertTrue(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1);
		p3.setZ(0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p2);
		p3.setZ(0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(0.f, 0.f, 0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(-50.f, 50.f, 0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(50.f, -50.f, 0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(-1000.f, -1000.f, 0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(256.f, 256.f, 0.f);
		assertFalse(GeometryUtil.isCollinearPoints(p1.getX(), p1.getY(),
				p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
				p3.getY(), p3.getZ(), 0));
	}

	/**
	 */
	public void testIsEqualLinesFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = new Point2f(-100.f, -100.f);
		Point2f p2 = new Point2f(100.f, 100.f);
		Point2f p3, p4;

		p3 = new Point2f(p1);
		p4 = new Point2f(p2);
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY(), 0));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(p1.getX() + 100.f, p1.getY() + 100.f);
		p4 = new Point2f(p2.getX() + 100.f, p2.getY() + 100.f);
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY(), 0));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(p1.getX() - 100.f, p1.getY() - 100.f);
		p4 = new Point2f(p2.getX() - 100.f, p2.getY() - 100.f);
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY(), 0));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(p1.getX() + 100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(p1.getX() - 100.f, p1.getY());
		p4 = new Point2f(p2);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(0.f, 0.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(100.f, -10.f);
		p4 = new Point2f(-100.f, 100.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(p1.getX() - 100.f, p1.getY());
		p4 = new Point2f(p2.getX() - 100.f, p2.getY());
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY(), 0));

		p3 = new Point2f(p1.getX(), p1.getY() + 100);
		p4 = new Point2f(p2.getX(), p2.getY() + 100);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p2.getX(),
				p2.getY(), p4.getX(), p4.getY(), p3.getX(), p3.getY(), 0));
	}

	/**
	 */
	public void testIsCollinearLinesFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		Point3f p1 = new Point3f(-100.f, -100.f, 50.f);
		Point3f p2 = new Point3f(100.f, 100.f, 50.f);
		Point3f p3, p4;

		p3 = new Point3f(p1);
		p4 = new Point3f(p2);
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX() + 100.f, p1.getY() + 100.f, p1.getZ());
		p4 = new Point3f(p2.getX() + 100.f, p2.getY() + 100.f, p2.getZ());
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX() - 100.f, p1.getY() - 100.f, p1.getZ());
		p4 = new Point3f(p2.getX() - 100.f, p2.getY() - 100.f, p2.getZ());
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertTrue(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX() + 100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX() - 100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(0.f, 0.f, 50.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(100.f, -10.f, 50.f);
		p4 = new Point3f(-100.f, 100.f, 50.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX() - 100.f, p1.getY(), p1.getZ());
		p4 = new Point3f(p2.getX() - 100.f, p2.getY(), p2.getZ());
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p3 = new Point3f(p1.getX(), p1.getY() + 100, p1.getZ());
		p4 = new Point3f(p2.getX(), p2.getY() + 100, p2.getZ());
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 0.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));

		p1 = new Point3f(1.f, 1.f, 0.f);
		p2 = new Point3f(2.f, 2.f, 0.f);
		p3 = new Point3f(1.f, 1.f, 1.f);
		p4 = new Point3f(2.f, 2.f, 1.f);
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p3.getX(), p3.getY(),
				p3.getZ(), p4.getX(), p4.getY(), p4.getZ(), 0));
		assertFalse(GeometryUtil.isEqualLines(p1.getX(), p1.getY(), p1.getZ(),
				p2.getX(), p2.getY(), p2.getZ(), p4.getX(), p4.getY(),
				p4.getZ(), p3.getX(), p3.getY(), p3.getZ(), 0));
	}

	/**
	 */
	public void testClipSegmentToRectangle2DPoint2Dfloatfloatfloatfloat() {

		float rminx, rminy, rmaxx, rmaxy;
		Point2f point1, point2;

		rminx = rminy = 1.f;
		rmaxx = 3.f;
		rmaxy = 5.f;

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(3f, 0f);

		GeometryUtil.clipSegmentToAlignedRectangle(point1, point2, rminx, rminy,
				rmaxx, rmaxy);
		assertEpsilonEquals(new Point2f(1, 2), point1);
		assertEpsilonEquals(new Point2f(2, 1), point2);

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(0.5f, 0f);

		GeometryUtil.clipSegmentToAlignedRectangle(point1, point2, rminx, rminy,
				rmaxx, rmaxy);
		assertEquals(point1,new Point2f(0f, 3f));
		assertEquals(point2, new Point2f(0.5f, 0f));

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(6f, 0f);

		GeometryUtil.clipSegmentToAlignedRectangle(point1, point2, rminx, rminy,
				rmaxx, rmaxy);
		assertEpsilonEquals(new Point2f(1, 2.5f), point1);
		assertEpsilonEquals(new Point2f(3, 1.5), point2);

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(4f, 3f);

		GeometryUtil.clipSegmentToAlignedRectangle(point1, point2, rminx, rminy,
				rmaxx, rmaxy);
		assertEpsilonEquals(new Point2f(1, 3), point1);
		assertEpsilonEquals(new Point2f(3, 3), point2);

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(5f, 6f);

		GeometryUtil.clipSegmentToAlignedRectangle(point1, point2, rminx, rminy,
				rmaxx, rmaxy);
		assertEpsilonEquals(new Point2f(1f, 3.6f), point1);
		assertEpsilonEquals(new Point2f(3, 4.8), point2);

	}

	/** 
	 */
	public static void testCcwFloatFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = new Point2f(-100.f, -100.f);
		Point2f p2 = new Point2f(100.f, 100.f);
		Point2f p3;

		p3 = new Point2f(p1);
		assertEquals(
				0,
				GeometryUtil.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				0,
				GeometryUtil.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(p2);
		assertEquals(
				0,
				GeometryUtil.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				0,
				GeometryUtil.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(0.f, 0.f);
		assertEquals(
				0,
				GeometryUtil.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				0,
				GeometryUtil.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(0.f, 0.f);
		assertEquals(
				0,
				GeometryUtil.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				0,
				GeometryUtil.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(-200.f, -200.f);
		assertEquals(
				-1,
				GeometryUtil.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				1,
				GeometryUtil.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(200.f, 200.f);
		assertEquals(
				1,
				GeometryUtil.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				-1,
				GeometryUtil.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(-200.f, 200.f);
		assertEquals(
				-1,
				GeometryUtil.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				1,
				GeometryUtil.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(200.f, -200.f);
		assertEquals(
				1,
				GeometryUtil.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				-1,
				GeometryUtil.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));
	}

	/**
	 */
	public void testSignedAngleFloatFloatFloatFloat() {
		Vector2f v1 = new Vector2f(RANDOM.nextFloat(), RANDOM.nextFloat());
		Vector2f v2 = new Vector2f(RANDOM.nextFloat(), RANDOM.nextFloat());

		assertEpsilonEquals(
				0.f,
				GeometryUtil.signedAngle(v1.getX(), v1.getY(), v1.getX(),
						v1.getY()));
		assertEpsilonEquals(
				0.f,
				GeometryUtil.signedAngle(v2.getX(), v2.getY(), v2.getX(),
						v2.getY()));

		float sAngle1 = GeometryUtil.signedAngle(v1.getX(), v1.getY(),
				v2.getX(), v2.getY());
		float sAngle2 = GeometryUtil.signedAngle(v2.getX(), v2.getY(),
				v1.getX(), v1.getY());

		assertEpsilonEquals(-sAngle1, sAngle2);
		assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
		assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));

		float sin = v1.getX() * v2.getY() - v1.getY() * v2.getX();

		if (sin < 0) {
			assertTrue(sAngle1 <= 0);
			assertTrue(sAngle2 >= 0);
		} else {
			assertTrue(sAngle1 >= 0);
			assertTrue(sAngle2 <= 0);
		}
	}

	/**
	 */
	public void testIsCollinearVectorsFloatFloatFloatFloat() {
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 2.f, 2.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, -1.f, -1.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, -2.f, -2.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 0.f, 0.f, 0.f));

		assertTrue(GeometryUtil.isCollinearVectors(1.f, -1.f, 1.f, -1.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, -1.f, -1.f, 1.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(-1.f, 1.f, -1.f, 1.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(-1.f, 1.f, 1.f, -1.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(-1.f, -1.f, -1.f, -1.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(-1.f, -1.f, 1.f, 1.f, 0.f));

		assertFalse(GeometryUtil.isCollinearVectors(1.f, -1.f, 1.f, 1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 2.f, -234.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(234.f, -345.f, -3456.f,
				2348.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(-53.f, 7853.f, -4562.f,
				-234.f, 0.f));
	}

	/**
	 */
	public void testIsCollinearVectorsFloatFloatFloatFloatFloatFloat() {
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, -1.f,
				-1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, -1.f,
				0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, -1.f,
				1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 0.f,
				-1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 0.f,
				0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 0.f,
				1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 1.f,
				-1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 1.f,
				0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, -1.f, 1.f,
				1.f, 0.f));

		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, -1.f,
				-1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, -1.f,
				0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, -1.f,
				1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 0.f,
				-1.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 0.f,
				0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 0.f,
				1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 1.f,
				-1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 1.f,
				0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 0.f, 1.f,
				1.f, 0.f));

		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, -1.f,
				-1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, -1.f,
				0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, -1.f,
				1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 0.f,
				-1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 0.f,
				0.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 0.f,
				1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 1.f,
				-1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 1.f,
				0.f, 0.f));
		assertTrue(GeometryUtil.isCollinearVectors(1.f, 1.f, 1.f, 1.f, 1.f,
				1.f, 0.f));

		assertTrue(GeometryUtil.isCollinearVectors(1234.f, 3245.f, 456.f,
				2468.f, 6490.f, 912.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1234.f, 3245.f, 456.f,
				2468.f, 6490.f, 914.f, 0.f));

		assertFalse(GeometryUtil.isCollinearVectors(1.f, 1.f, 0.f, 1.f, 1.f,
				2.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(1.f, 0.f, 1.f, 1.f, 2.f,
				1.f, 0.f));
		assertFalse(GeometryUtil.isCollinearVectors(0.f, 1.f, 1.f, 2.f, 1.f,
				1.f, 0.f));
	}

	/**
	 */
	public void testGetPointProjectionFactorOnSegmentFloatFloatFloatFloatFloatFloat() {
		Point2f p1 = new Point2f(RANDOM.nextFloat(), RANDOM.nextFloat());
		Point2f p2 = new Point2f(RANDOM.nextFloat(), RANDOM.nextFloat());
		Point2f p3 = new Point2f(RANDOM.nextFloat(), RANDOM.nextFloat());

		float expected;

		/*
		 * L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 )
		 *
		 * (Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay)
		 * r = -------------------------------
		 * L^2
		 */
		float L = (float) Math.sqrt(Math.pow(p3.getX()-p2.getX(), 2.f) +
				Math.pow(p3.getY()-p2.getY(), 2.f));
		expected = ((p1.getX()-p2.getX())*(p3.getX()-p2.getX()) +
				(p1.getY()-p2.getY())*(p3.getY()-p2.getY())) / (L*L);

		float actual = GeometryUtil.getPointProjectionFactorOnSegment(p1.getX(),
				p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());

		assertEpsilonEquals(expected, actual);


		p1 = new Point2f(-100.f,-100.f);
		p2 = new Point2f(100.f,100.f);

		p3 = new Point2f(p1);
		assertEpsilonEquals(0.f,GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(1.f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(p2);
		assertEquals(1.f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(-0.f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(0.f, 0.f);
		assertEquals(.5f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(.5f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(-100.f, 100.f);
		assertEquals(.5f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(.5f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(100.f, -100.f);
		assertEquals(.5f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(.5f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(-300, -300.f);
		assertEquals(-1.f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(2.f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(300, 300.f);
		assertEquals(2.f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(-1.f, GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
				p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));
	}	

	/**
	 */
	public void testClosestFarthestPointsOBBPointFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatPoint3DPoint3D(){

		Point3f q1P1, q2P1, q1P2, q2P2, q1P3, q2P3, q1P4, q2P4;

		q1P1 = new Point3f(); q2P1 = new Point3f(); q1P2 = new Point3f(); q2P2 = new Point3f();
		q1P3 = new Point3f(); q2P3 = new Point3f(); q1P4 = new Point3f(); q2P4 = new Point3f();
		
		
		Point3f center = new Point3f(1.5,3,4.5);
		Vector3f axe1 = new Vector3f(1,1,1);
		Vector3f axe2 = new Vector3f(1,1,-2);
		Vector3f axe3 = new Vector3f(-3,3,0);
		Vector3f extents = new Vector3f(1,2,3);
		Vector3f axis[] = new Vector3f[]{axe1, axe2 , axe3 };
		float ext[] = new float[]{extents.getX(),extents.getY(),extents.getZ()};
	
		axe1.normalize();
		axe2.normalize();
		axe3.normalize();

		//Giving a cuboïd
		//					    E +---------+ F
		//					     /|        /|
		//					    / |       / |
		//					   /  |      /  |
		//					  / H +-----/---+ G
		//  			   A +---------+ B / 
		//					 |  /      |  /  
		//					 | /       | /   
		//					 |/        |/    
		//					 +---------+
		//					D	        C
		//axe1 = vect AB	(normalize) * extentx
		//axe2 = vect AD	(normalize)	* extenty
		//axe3 = vect AE	(normalize) * extentz
		//
		//Let's name the closest point of A c(A) and the farthest one f(A)
		//Let's test all pathologics points (Like p = A,B,..G or p is on (AB),(AE)...
		Vector3f AB = new Vector3f(axe1); AB.scale(extents.getX());
		Vector3f AD = new Vector3f(axe2); AD.scale(extents.getY());
		Vector3f AE = new Vector3f(axe3); AE.scale(extents.getZ());
		Point3f A = new Point3f(1,2,3);
		Point3f B = new Point3f(A); B.add(AB);
		Point3f C = new Point3f(B); C.add(AD);
		Point3f D = new Point3f(A); D.add(AD);
		Point3f E = new Point3f(A); E.add(AE);
		Point3f F = new Point3f(B); F.add(AE);
		Point3f G = new Point3f(C); G.add(AE);
		Point3f H = new Point3f(D); H.add(AE);

		Point3f P1 = new Point3f();
		Point3f P2 = new Point3f();
		Point3f P3 = new Point3f();
		Point3f P4 = new Point3f();
		
		P1.scaleAdd(-2, AB, A);
		P2.scaleAdd(-2, AB, D);
		P3.scaleAdd(-2, AB, H);
		P4.scaleAdd(-2, AB, E);
		
		AB.scale(0.5f);

		for(int i = -2; i <= 2 ;i++){
			
			
			//fail(" P1 : " + P1.toString() + "P2 : " + P2.toString() + 	"P3 : " + P3.toString() + 	"P4 : " + P4.toString());	
			/*GeometryUtil.closestFarthestPointsOBBPoint(center.getX(), center.getY(), center.getZ(), axe1.getX(),axe1.getY(),axe1.getZ(),axe2.getX(),axe2.getY(),axe2.getZ(),
					axe3.getX(),axe3.getY(),axe3.getZ(), extents.getX(),extents.getY(),extents.getZ(),P.getX(),P.getY(),P.getZ(), q1, q2);*/
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P1, q1P1, q2P1);
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P2, q1P2, q2P2);
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P3, q1P3, q2P3);
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P4, q1P4, q2P4);
			
			if(i<0){
				assertEpsilonEquals("i :"+i,A, q1P1);
				assertEpsilonEquals("i :"+i,B, q2P1);
				
				assertEpsilonEquals("i :"+i,D, q1P2);
				assertEpsilonEquals("i :"+i,C, q2P2);
				
				assertEpsilonEquals("i :"+i,H, q1P3);
				assertEpsilonEquals("i :"+i,G, q2P3);
				
				assertEpsilonEquals("i :"+i,E, q1P4);
				assertEpsilonEquals("i :"+i,F, q2P4);
				
			}else if(i == 0){
				assertEpsilonEquals("i :"+i,q1P1, q1P1);
				assertEpsilonEquals("i :"+i,q2P1, q2P1);
				
				assertEpsilonEquals("i :"+i,q1P2, q1P2);
				assertEpsilonEquals("i :"+i,q2P2, q2P2);
				
				assertEpsilonEquals("i :"+i,q1P3, q1P3);
				assertEpsilonEquals("i :"+i,q2P3, q2P3);
				
				assertEpsilonEquals("i :"+i,q1P4, q1P4);
				assertEpsilonEquals("i :"+i,q2P4, q2P4);
			}else{
				assertEpsilonEquals("i :"+i,B, q1P1);
				assertEpsilonEquals("i :"+i,A, q2P1);
				
				assertEpsilonEquals("i :"+i,C, q1P2);
				assertEpsilonEquals("i :"+i,D, q2P2);
				
				assertEpsilonEquals("i :"+i,G, q1P3);
				assertEpsilonEquals("i :"+i,H, q2P3);
				
				assertEpsilonEquals("i :"+i,F, q1P4);
				assertEpsilonEquals("i :"+i,E, q2P4);
			}

			P1.add(AB);
			P2.add(AB);
			P3.add(AB);
			P4.add(AB);	
		}
		
		P1.scaleAdd(-2, AD, A);
		P2.scaleAdd(-2, AD, B);
		P3.scaleAdd(-2, AD, F);
		P4.scaleAdd(-2, AD, E);
		
		AD.scale(0.5f);

		for(int i = -2; i <= 2 ;i++){
			
			/*GeometryUtil.closestFarthestPointsOBBPoint(center.getX(), center.getY(), center.getZ(), axe1.getX(),axe1.getY(),axe1.getZ(),axe2.getX(),axe2.getY(),axe2.getZ(),
					axe3.getX(),axe3.getY(),axe3.getZ(), extents.getX(),extents.getY(),extents.getZ(),P.getX(),P.getY(),P.getZ(), q1, q2);*/
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P1, q1P1, q2P1);
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P2, q1P2, q2P2);
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P3, q1P3, q2P3);
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P4, q1P4, q2P4);
			
			if(i<=0){
				assertEpsilonEquals("i :"+i,A, q1P1);
				assertEpsilonEquals("i :"+i,D, q2P1);
				
				assertEpsilonEquals("i :"+i,B, q1P2);
				assertEpsilonEquals("i :"+i,C, q2P2);
				
				assertEpsilonEquals("i :"+i,F, q1P3);
				assertEpsilonEquals("i :"+i,G, q2P3);
				
				assertEpsilonEquals("i :"+i,E, q1P4);
				assertEpsilonEquals("i :"+i,H, q2P4);
				
			}else if(i == 0){
				assertEpsilonEquals("i :"+i,q1P1, q1P1);
				assertEpsilonEquals("i :"+i,q2P1, q2P1);
				
				assertEpsilonEquals("i :"+i,q1P2, q1P2);
				assertEpsilonEquals("i :"+i,q2P2, q2P2);
				
				assertEpsilonEquals("i :"+i,q1P3, q1P3);
				assertEpsilonEquals("i :"+i,q2P3, q2P3);
				
				assertEpsilonEquals("i :"+i,q1P4, q1P4);
				assertEpsilonEquals("i :"+i,q2P4, q2P4);
			}else{
				assertEpsilonEquals("i :"+i,D, q1P1);
				assertEpsilonEquals("i :"+i,A, q2P1);
				
				assertEpsilonEquals("i :"+i,C, q1P2);
				assertEpsilonEquals("i :"+i,B, q2P2);
				
				assertEpsilonEquals("i :"+i,G, q1P3);
				assertEpsilonEquals("i :"+i,F, q2P3);
				
				assertEpsilonEquals("i :"+i,H, q1P4);
				assertEpsilonEquals("i :"+i,E, q2P4);
			}

			P1.add(AD);
			P2.add(AD);
			P3.add(AD);
			P4.add(AD);			
		}	
		
		P1.scaleAdd(-2, AE, A);
		P2.scaleAdd(-2, AE, B);
		P3.scaleAdd(-2, AE, C);
		P4.scaleAdd(-2, AE, D);
		
		AE.scale(0.5f);

		for(int i = -2; i <= 2 ;i++){
			
			/*GeometryUtil.closestFarthestPointsOBBPoint(center.getX(), center.getY(), center.getZ(), axe1.getX(),axe1.getY(),axe1.getZ(),axe2.getX(),axe2.getY(),axe2.getZ(),
					axe3.getX(),axe3.getY(),axe3.getZ(), extents.getX(),extents.getY(),extents.getZ(),P.getX(),P.getY(),P.getZ(), q1, q2);*/
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P1, q1P1, q2P1);
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P2, q1P2, q2P2);
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P3, q1P3, q2P3);
			GeometryUtil.computeClosestFarestOBBPoints(center, axis, ext, P4, q1P4, q2P4);
			
			if(i<=0){
				assertEpsilonEquals("i :"+i,A, q1P1);
				assertEpsilonEquals("i :"+i,E, q2P1);
				
				assertEpsilonEquals("i :"+i,B, q1P2);
				assertEpsilonEquals("i :"+i,F, q2P2);
				
				assertEpsilonEquals("i :"+i,C, q1P3);
				assertEpsilonEquals("i :"+i,G, q2P3);
				
				assertEpsilonEquals("i :"+i,D, q1P4);
				assertEpsilonEquals("i :"+i,H, q2P4);
				
			}else if(i == 0){
				assertEpsilonEquals("i :"+i,q1P1, q1P1);
				assertEpsilonEquals("i :"+i,q2P1, q2P1);
				
				assertEpsilonEquals("i :"+i,q1P2, q1P2);
				assertEpsilonEquals("i :"+i,q2P2, q2P2);
				
				assertEpsilonEquals("i :"+i,q1P3, q1P3);
				assertEpsilonEquals("i :"+i,q2P3, q2P3);
				
				assertEpsilonEquals("i :"+i,q1P4, q1P4);
				assertEpsilonEquals("i :"+i,q2P4, q2P4);
			}else{
				assertEpsilonEquals("i :"+i,E, q1P1);
				assertEpsilonEquals("i :"+i,A, q2P1);
				
				assertEpsilonEquals("i :"+i,F, q1P2);
				assertEpsilonEquals("i :"+i,B, q2P2);
				
				assertEpsilonEquals("i :"+i,G, q1P3);
				assertEpsilonEquals("i :"+i,C, q2P3);
				
				assertEpsilonEquals("i :"+i,H, q1P4);
				assertEpsilonEquals("i :"+i,D, q2P4);
			}

			P1.add(AE);
			P2.add(AE);
			P3.add(AE);
			P4.add(AE);				
		}
		
		/***/
		
		
	}
	
	public void testClosestFarthestPointsOBRPointFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatPoint2dPoint2d(){
		float rminx, rminy, rmaxx, rmaxy;
		Point2f point1, point2;

		rminx = rminy = 1.f;
		rmaxx = 3.f;
		rmaxy = 5.f;
		Point2f center = new Point2f(2,2);
		Vector2f axe1 = new Vector2f(3/5,4/5);
		Vector2f axe2 = new Vector2f(4/5,-3/5);
		Vector2f extents = new Vector2f(1,2);
		Vector2f axis[] = new Vector2f[]{axe1, axe2};
		float ext[] = new float[]{extents.getX(),extents.getY()};
	
		Point3f P1 = new Point3f();
		Point3f P2 = new Point3f();
		Point3f P3 = new Point3f();
		Point3f P4 = new Point3f();
		
		P1.scaleAdd(-2, AB, A);
		P2.scaleAdd(-2, AB, D);
		P3.scaleAdd(-2, AB, H);
		P4.scaleAdd(-2, AB, E);
		
		AB.scale(0.5f);

		for(int i = -2; i <= 2 ;i++){
				
			GeometryUtil.closestFarthestPointsOBBPoint(
					center.getX(), center.getY(), axe1.getX(),axe1.getY(), axe2.getX(),axe2.getY(),
					extents.getX(),extents.getY(), P1.getX(),P2.getY(), q1, q2);
			
			if(i<0){
				assertEpsilonEquals("i :"+i,A, q1P1);
				assertEpsilonEquals("i :"+i,B, q2P1);
				
				assertEpsilonEquals("i :"+i,D, q1P2);
				assertEpsilonEquals("i :"+i,C, q2P2);
				
				assertEpsilonEquals("i :"+i,H, q1P3);
				assertEpsilonEquals("i :"+i,G, q2P3);
				
				assertEpsilonEquals("i :"+i,E, q1P4);
				assertEpsilonEquals("i :"+i,F, q2P4);
				
			}else if(i == 0){
				assertEpsilonEquals("i :"+i,q1P1, q1P1);
				assertEpsilonEquals("i :"+i,q2P1, q2P1);
				
				assertEpsilonEquals("i :"+i,q1P2, q1P2);
				assertEpsilonEquals("i :"+i,q2P2, q2P2);
				
				assertEpsilonEquals("i :"+i,q1P3, q1P3);
				assertEpsilonEquals("i :"+i,q2P3, q2P3);
				
				assertEpsilonEquals("i :"+i,q1P4, q1P4);
				assertEpsilonEquals("i :"+i,q2P4, q2P4);
			}else{
				assertEpsilonEquals("i :"+i,B, q1P1);
				assertEpsilonEquals("i :"+i,A, q2P1);
				
				assertEpsilonEquals("i :"+i,C, q1P2);
				assertEpsilonEquals("i :"+i,D, q2P2);
				
				assertEpsilonEquals("i :"+i,G, q1P3);
				assertEpsilonEquals("i :"+i,H, q2P3);
				
				assertEpsilonEquals("i :"+i,F, q1P4);
				assertEpsilonEquals("i :"+i,E, q2P4);
			}

			P1.scaleAdd(-2, AD, A);
			P2.scaleAdd(-2, AD, B);
			P3.scaleAdd(-2, AD, F);
			P4.scaleAdd(-2, AD, E);			
		}
		
		P1.scaleAdd(-2, AD, A);
		P2.scaleAdd(-2, AD, B);
		P3.scaleAdd(-2, AD, F);
		P4.scaleAdd(-2, AD, E);
		
		AD.scale(0.5f);
	}

	// */
	// public void testDistanceFloatFloatFloatFloatFloatFloat() {
	// Point3f p1 = randomPoint3D();
	// Point3f p2 = randomPoint3D();
	//
	// assertZero(MathUtil.distancePointPoint(p1.getX(),p1.getY(),p1.getZ(),
	// p1.getX(),p1.getY(),p1.getZ()));
	// assertZero(MathUtil.distancePointPoint(p2.getX(),p2.getY(),p2.getZ(),
	// p2.getX(),p2.getY(),p2.getZ()));
	//
	// float dx = p1.getX() - p2.getX();
	// float dy = p1.getY() - p2.getY();
	// float dz = p1.getZ() - p2.getZ();
	// float expectedDistance = (float) Math.sqrt((dx * dx) + (dy * dy) + (dz *
	// dz));
	//
	// assertEquals(expectedDistance,
	// MathUtil.distancePointPoint(p1.getX(),p1.getY(),p1.getZ(),
	// p2.getX(),p2.getY(),p2.getZ()));
	// assertEquals(expectedDistance,
	// MathUtil.distancePointPoint(p2.getX(),p2.getY(),p2.getZ(),
	// p1.getX(),p1.getY(),p1.getZ()));
	// }
	//
	// /**
	// */
	// public void testDistanceFloatFloat() {
	// Vector2f p1 = randomVector2D();
	// assertEquals(p1.length(), MathUtil.distance(p1.getX(),p1.getY()));
	// }
	//
	// /**
	// */
	// public void testDistanceFloatFloatFloat() {
	// Vector3f p1 = randomVector3D();
	// assertEquals(p1.length(),
	// MathUtil.distance(p1.getX(),p1.getY(),p1.getZ()));
	// }
	//
	// /**
	// */
	// public void testDistanceFloatFloatFloatFloat() {
	// Point2f p1 = randomPoint2D();
	// Point2f p2 = randomPoint2D();
	//
	// assertZero(MathUtil.distance(p1.getX(),p1.getY(), p1.getX(),p1.getY()));
	// assertZero(MathUtil.distance(p2.getX(),p2.getY(), p2.getX(),p2.getY()));
	//
	// float dx = p1.getX() - p2.getX();
	// float dy = p1.getY() - p2.getY();
	// float expectedDistance = (float) Math.sqrt((dx * dx) + (dy * dy));
	//
	// assertEquals(expectedDistance, MathUtil.distance(p1.getX(),p1.getY(),
	// p2.getX(),p2.getY()));
	// assertEquals(expectedDistance, MathUtil.distance(p2.getX(),p2.getY(),
	// p1.getX(),p1.getY()));
	// }
	//
	// /**
	// */
	// public void testDistanceSquaredFloatFloatFloatFloatFloatFloat() {
	// Point3f p1 = randomPoint3D();
	// Point3f p2 = randomPoint3D();
	//
	// assertZero(MathUtil.distanceSquaredPointPoint(p1.getX(),p1.getY(),p1.getZ(),
	// p1.getX(),p1.getY(),p1.getZ()));
	// assertZero(MathUtil.distanceSquaredPointPoint(p2.getX(),p2.getY(),p2.getZ(),
	// p2.getX(),p2.getY(),p2.getZ()));
	//
	// float dx = p1.getX() - p2.getX();
	// float dy = p1.getY() - p2.getY();
	// float dz = p1.getZ() - p2.getZ();
	// float expectedDistance = (dx * dx) + (dy * dy) + (dz * dz);
	//
	// assertEquals(expectedDistance,
	// MathUtil.distanceSquaredPointPoint(p1.getX(),p1.getY(),p1.getZ(),
	// p2.getX(),p2.getY(),p2.getZ()));
	// assertEquals(expectedDistance,
	// MathUtil.distanceSquaredPointPoint(p2.getX(),p2.getY(),p2.getZ(),
	// p1.getX(),p1.getY(),p1.getZ()));
	// }
	//
	// /**
	// */
	// public void testDistanceSquaredFloatFloatFloatFloat() {
	// Point2f p1 = randomPoint2D();
	// Point2f p2 = randomPoint2D();
	//
	// assertZero(MathUtil.distanceSquared(p1.getX(),p1.getY(),
	// p1.getX(),p1.getY()));
	// assertZero(MathUtil.distanceSquared(p2.getX(),p2.getY(),
	// p2.getX(),p2.getY()));
	//
	// float dx = p1.getX() - p2.getX();
	// float dy = p1.getY() - p2.getY();
	// float expectedDistance = (dx * dx) + (dy * dy);
	//
	// assertEquals(expectedDistance,
	// MathUtil.distanceSquared(p1.getX(),p1.getY(), p2.getX(),p2.getY()));
	// assertEquals(expectedDistance,
	// MathUtil.distanceSquared(p2.getX(),p2.getY(), p1.getX(),p1.getY()));
	// }
	//

	//
	// /**
	// */
	// public void
	// testDistancePointLineFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
	// Point3f p1 = randomPoint3D();
	// Point3f p2 = randomPoint3D();
	// Point3f p3 = randomPoint3D();
	//
	// Vector3f v1 = new
	// Vector3f(p3.getX()-p2.getX(),p3.getY()-p2.getY(),p3.getZ()-p2.getZ());
	// Vector3f v2 = new
	// Vector3f(p1.getX()-p2.getX(),p1.getY()-p2.getY(),p1.getZ()-p2.getZ());
	//
	// Vector3f v3 = new Vector3f();
	// v3.cross(v1, v2);
	//
	// float distance = v3.length() / v1.length();
	//
	// assertEpsilonEquals(distance, MathUtil.distancePointLine(p1.getX(),
	// p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ(), p3.getX(),
	// p3.getY(), p3.getZ()));
	// }
	//
	//

	//

	//
	// /**
	// */
	// public void testSignedAngleFloatFloatFloatFloatFloatFloat() {
	// {
	// // top-right quarter
	// Vector3f v1 = new Vector3f(this.RANDOM.nextFloat(),
	// this.RANDOM.nextFloat(), 0);
	// // top-left quarter
	// Vector3f v2 = new Vector3f(-this.RANDOM.nextFloat(),
	// this.RANDOM.nextFloat(), 0);
	//
	// float sAngle1 = GeometryUtil.signedAngle(v1.getX(), v1.getY(), v1.getZ(),
	// v2.getX(), v2.getY(), v2.getZ());
	// float sAngle2 = GeometryUtil.signedAngle(v2.getX(), v2.getY(), v2.getZ(),
	// v1.getX(), v1.getY(), v1.getZ());
	//
	// assertEpsilonEquals(-sAngle1, sAngle2);
	// assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
	// assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
	//
	// assertPositive(sAngle1);
	// assertNegative(sAngle2);
	// }
	//
	// {
	// // top-right quarter
	// Vector3f v1 = new Vector3f(this.RANDOM.nextFloat(),
	// this.RANDOM.nextFloat(), 0);
	// // bottom-right quarter
	// Vector3f v2 = new Vector3f(this.RANDOM.nextFloat(),
	// -this.RANDOM.nextFloat(), 0);
	//
	// float sAngle1 = GeometryUtil.signedAngle(v1.getX(), v1.getY(), v1.getZ(),
	// v2.getX(), v2.getY(), v2.getZ());
	// float sAngle2 = GeometryUtil.signedAngle(v2.getX(), v2.getY(), v2.getZ(),
	// v1.getX(), v1.getY(), v1.getZ());
	//
	// assertEpsilonEquals(-sAngle1, sAngle2);
	// assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
	// assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
	//
	// assertNegative(sAngle1);
	// assertPositive(sAngle2);
	// }
	//
	// {
	// Vector3f v1 = randomVector3D();
	// Vector3f v2 = randomVector3D();
	//
	// assertEpsilonEquals(0.f, GeometryUtil.signedAngle(v1.getX(), v1.getY(),
	// v1.getZ(), v1.getX(), v1.getY(), v1.getZ()));
	// assertEpsilonEquals(0.f, GeometryUtil.signedAngle(v2.getX(), v2.getY(),
	// v2.getZ(), v2.getX(), v2.getY(), v2.getZ()));
	//
	// float sAngle1 = GeometryUtil.signedAngle(v1.getX(), v1.getY(), v1.getZ(),
	// v2.getX(), v2.getY(), v2.getZ());
	// float sAngle2 = GeometryUtil.signedAngle(v2.getX(), v2.getY(), v2.getZ(),
	// v1.getX(), v1.getY(), v1.getZ());
	//
	// assertEpsilonEquals(-sAngle1, sAngle2);
	// assertEpsilonEquals(v1.angle(v2), Math.abs(sAngle1));
	// assertEpsilonEquals(v2.angle(v1), Math.abs(sAngle2));
	//
	// if (MathUtil.determinant(v1.getX(), v1.getY(), v1.getZ(), v2.getX(),
	// v2.getY(), v2.getZ())<0.f) {
	// assertNegative(sAngle1);
	// assertPositive(sAngle2);
	// }
	// else {
	// assertPositive(sAngle1);
	// assertNegative(sAngle2);
	// }
	// }
	// }
	//
	// /**
	// */
	// public void
	// testDistanceSegmentSegmentFloatFloatFloatFloatFloatFloatFloatFloat() {
	// Point2f s11 = randomPoint2D();
	// Point2f s12 = randomPoint2D();
	// Point2f s21 = randomPoint2D();
	// Point2f s22 = randomPoint2D();
	//
	// // Distance between the same segment twice times is zero
	// assertEpsilonEquals(0.f, MathUtil.distanceSegmentSegment(s11.getX(),
	// s11.getY(), s12.getX(), s12.getY(), s11.getX(), s11.getY(), s12.getX(),
	// s12.getY()));
	// assertEpsilonEquals(0.f, MathUtil.distanceSegmentSegment(s21.getX(),
	// s21.getY(), s22.getX(), s22.getY(), s21.getX(), s21.getY(), s22.getX(),
	// s22.getY()));
	//
	// float distance = MathUtil.distanceSegmentSegment(s11.getX(), s11.getY(),
	// s12.getX(), s12.getY(), s21.getX(), s21.getY(), s22.getX(), s22.getY());
	//
	// // The order of the segments' points is not significant
	// assertEpsilonEquals(distance, MathUtil.distanceSegmentSegment(s12.getX(),
	// s12.getY(), s11.getX(), s11.getY(), s21.getX(), s21.getY(), s22.getX(),
	// s22.getY()));
	// assertEpsilonEquals(distance, MathUtil.distanceSegmentSegment(s11.getX(),
	// s11.getY(), s12.getX(), s12.getY(), s22.getX(), s22.getY(), s21.getX(),
	// s21.getY()));
	// assertEpsilonEquals(distance, MathUtil.distanceSegmentSegment(s12.getX(),
	// s12.getY(), s11.getX(), s11.getY(), s22.getX(), s22.getY(), s21.getX(),
	// s21.getY()));
	//
	// // Does the segments intersecting?
	// float pCD = (s21.getY()-s22.getY())/(s21.getX()-s22.getX());
	// float pAB = (s11.getY()-s12.getY())/(s11.getX()-s12.getX());
	// float oCD = s21.getY()-pCD*s21.getX();
	// float oAB = s11.getY()-pAB*s11.getX();
	// float Sx = (oAB-oCD)/(pCD-pAB);
	// float Sy = pCD*Sx+oCD;
	//
	// if((Sx<s11.getX() && Sx<s12.getX())|(Sx>s11.getX() && Sx>s12.getX()) |
	// (Sx<s21.getX() && Sx<s22.getX())|(Sx>s21.getX() && Sx>s22.getX())
	// | (Sy<s11.getY() && Sy<s12.getY())|(Sy>s11.getY() && Sy>s12.getY()) |
	// (Sy<s21.getY() && Sy<s22.getY())|(Sy>s21.getY() && Sy>s22.getY())) {
	// // Compute the expected distance
	// float expectedDistance = MathUtil.min(
	// MathUtil.distancePointSegment(s11.getX(), s11.getY(), s21.getX(),
	// s21.getY(), s22.getX(), s22.getY()),
	// MathUtil.distancePointSegment(s12.getX(), s12.getY(), s21.getX(),
	// s21.getY(), s22.getX(), s22.getY()),
	// MathUtil.distancePointSegment(s21.getX(), s21.getY(), s11.getX(),
	// s11.getY(), s12.getX(), s12.getY()),
	// MathUtil.distancePointSegment(s22.getX(), s22.getY(), s11.getX(),
	// s11.getY(), s12.getX(), s12.getY()));
	//
	// // Test if the expected distance is equal to the computed distance.
	// assertEpsilonEquals(expectedDistance, distance);
	// }
	// else {
	// assertEpsilonEquals(0.f, distance);
	// }
	// }
	//
	// /**
	// */
	// public void testAngleFloatFloatFloatFloatFloatFloat() {
	// Vector3f v1 = randomVector3D();
	// Vector3f v2 = randomVector3D();
	//
	// float angle1 = MathUtil.angle(v1.getX(),v1.getY(),v1.getZ(),
	// v2.getX(),v2.getY(),v2.getZ());
	// float angle2 = MathUtil.angle(v2.getX(),v2.getY(),v2.getZ(),
	// v1.getX(),v1.getY(),v1.getZ());
	//
	// assertPositive(angle1);
	// assertEpsilonEquals(angle1, angle2);
	// assertEpsilonEquals(angle1, v1.angle(v2));
	// assertEpsilonEquals(angle2, v2.angle(v1));
	// }
	//
	// /**
	// */
	// public void testAngleFloatFloatFloatFloat() {
	// Vector2f v1 = randomVector2D();
	// Vector2f v2 = randomVector2D();
	//
	// float angle1 = MathUtil.angle(v1.getX(),v1.getY(), v2.getX(),v2.getY());
	// float angle2 = MathUtil.angle(v2.getX(),v2.getY(), v1.getX(),v1.getY());
	//
	// assertPositive(angle1);
	// assertEpsilonEquals(angle1, angle2);
	// assertEpsilonEquals(angle1, v1.angle(v2));
	// assertEpsilonEquals(angle2, v2.angle(v1));
	// }

	// /**
	// */
	// public void
	// testGetLineLineIntersectionPointFloatFloatFloatFloatFloatFloatFloatFloat()
	// {
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// 1.f, 1.f, 2.f, 2.f,
	// 1.f, 1.f, 2.f, 2.f));
	//
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// 1.f, 1.f, 2.f, 2.f,
	// 1.f, 4.f, 2.f, 5.f));
	//
	// assertEpsilonEquals(new Point2f(),
	// MathUtil.getLineLineIntersectionPoint(
	// -1.f, -1.f, 1.f, 1.f,
	// -1.f, 1.f, 1.f, -1.f));
	//
	//
	// assertEpsilonEquals(new Point2f(2.f,44.5f),
	// MathUtil.getLineLineIntersectionPoint(
	// 2.f, -32.f, 2.f, 100.f,
	// 101.f, 44.5f, 100.f, 44.5f));
	//
	// // Equation resolution with GNU Octave.
	// //
	// // Linear Equation Resolution:
	// // a.getX() + b.getY() = c
	// // d.getX() + e.getY() = f
	// //
	// // A = [ a b ]
	// // [ d e ]
	// //
	// // b = [ c ]
	// // [ f ]
	// //
	// // A \ b = [ x ]
	// // [ y ]
	// //
	// // Back to line intersection problem:
	// //
	// // P1 + m.(P2-P1) = I
	// // P3 + n.(P4-P3.f) = I
	// //
	// // m.(x2-x1) - n.(x4-x3.f) = x3-x1
	// // m.(y2-y1) - n.(y4-y3.f) = y3-y1
	// //
	// // A = [ (x2-x1) (x3-x4) ]
	// // [ (y2-y1) (y3-y4) ]
	// //
	// // b = [ x3-x1 ]
	// // [ y3-y1 ]
	// //
	// // R = A \ b = [ m ]
	// // [ n ]
	// //
	// // [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ]
	// // [ y ] [ y1 ] [ y2-y1 ]
	//
	// // Octave Code:
	// // output_precision(16)
	// // P = { -2.f, 3.f, -1.f, 6f, -3.f, 28.f, 0.f, 24. }
	// // x1=P{1}, y1=P{2}, x2=P{3}, y2=P{4}, x3=P{5}, y3=P{6}, x4=P{7}, y4=P{8}
	// // A = [ x2-x1, x3-x4; y2-y1, y3-y4 ]
	// // b = [ x3-x1; y3-y1 ]
	// // R = A \ b
	// // I = [ float(x1 + R(1) * (x2-x1)), float(y1 + R(1) * (y2-y1)) ]
	// assertEpsilonEquals(new Point2f(3.461538461538462e+00,
	// 1.938461538461539e+01),
	// MathUtil.getLineLineIntersectionPoint(
	// -2.f, 3.f, -1.f, 6.f,
	// -3.f, 28.f, 0.f, 24.f));
	// }
	//
	// /**
	// */
	// public void
	// testGetLineLineIntersectionFactorFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat()
	// {
	// assertNaN(GeometryUtil.getLineLineIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f));
	//
	// assertNaN(GeometryUtil.getLineLineIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 4.f, 0.f, 2.f, 5.f, 0.f));
	//
	// assertEpsilonEquals(.5f,
	// GeometryUtil.getLineLineIntersectionFactor(
	// -1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
	// -1.f, 1.f, 0.f, 1.f, -1.f, 0.f));
	//
	//
	// assertEpsilonEquals(.579545455f,
	// GeometryUtil.getLineLineIntersectionFactor(
	// 2.f, -32.f, 0.f, 2.f, 100.f, 0.f,
	// 101.f, 44.5f, 0.f, 100.f, 44.5f, 0.f));
	//
	// assertEpsilonEquals(
	// 0.f,
	// GeometryUtil.getLineLineIntersectionFactor(
	// 0.f, 1.f, 1.f, 0.f, 2.f, 2.f,
	// 0.f, 1.f, 1.f, 1.f, 2.f, 2.f));
	//
	// assertEpsilonEquals(
	// 0.f,
	// GeometryUtil.getLineLineIntersectionFactor(
	// 1.f, 0.f, 1.f, 2.f, 0.f, 2.f,
	// 1.f, 0.f, 1.f, 2.f, 1.f, 2.f));
	//
	// assertEpsilonEquals(
	// 0.f,
	// GeometryUtil.getLineLineIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 2.f, 2.f, 1.f));
	//
	// assertNaN(
	// GeometryUtil.getLineLineIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 1.f, 2.f, 2.f, 1.f));
	//
	// // Equation resolution with GNU Octave.
	// //
	// // Linear Equation Resolution:
	// // a.getX() + b.getY() + c.getZ() = d
	// // e.getX() + f.getY() + g.getZ() = h
	// // i.getX() + j.getY() + k.getZ() = l
	// //
	// // A = [ a b c ]
	// // [ e f g ]
	// // [ i j k ]
	// //
	// // b = [ d ]
	// // [ h ]
	// // [ l ]
	// //
	// // A \ b = [ x ]
	// // [ y ]
	// // [ z ]
	// //
	// // Back to line intersection problem:
	// //
	// // P1 + m.(P2-P1) = I
	// // P3 + n.(P4-P3.f) = I
	// //
	// // m.(x2-x1) - n.(x4-x3.f) = x3-x1
	// // m.(y2-y1) - n.(y4-y3.f) = y3-y1
	// // m.(z2-z1) - n.(z4-z3.f) = z3-z1
	// //
	// // A = [ (x2-x1) (x3-x4) 0 ]
	// // [ (y2-y1) (y3-y4) 0 ]
	// // [ (z2-z1) (z3-z4) 0 ]
	// //
	// // b = [ x3-x1 ]
	// // [ y3-y1 ]
	// // [ z3-z1 ]
	// //
	// // R = A \ b = [ m ]
	// // [ n ]
	// // [ _ ]
	// //
	// // [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ]
	// // [ y ] [ y1 ] [ y2-y1 ]
	// // [ z ] [ z1 ] [ z2-z1 ]
	//
	// // Octave Code:
	// // output_precision(16)
	// // P = { -2.f, 3.f, 0.f, -1.f, 6f, 0.f, -3.f, 28.f, 0.f, 0.f, 24.f, 0. }
	// // x1=P{1}, y1=P{2}, z1=P{3}, x2=P{4}, y2=P{5}, z2=P{6}, x3=P{7},
	// y3=P{8}, z3=P{9}, x4=P{10}, y4=P{11}, z4=P{12}
	// // A = [ x2-x1, x3-x4f, 0.; y2-y1, y3-y4f, 0.; z2-z1, z3-z4f, 0. ]
	// // b = [ x3-x1; y3-y1; z3-z1 ]
	// // R = A \ b
	// // I = [ x1 + R(1) * (x2-x1), y1 + R(1) * (y2-y1), z1 + R(1) * (z2-z1) ]
	// assertEpsilonEquals(5.461538461538462e+00f,
	// GeometryUtil.getLineLineIntersectionFactor(
	// -2.f, 3.f, 0.f, -1.f, 6f, 0.f,
	// -3.f, 28.f, 0.f, 0.f, 24.f, 0.f));
	// }
	//
	// /**
	// */
	// public void
	// testGetLineLineIntersectionPointFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat()
	// {
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f));
	//
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 4.f, 0.f, 2.f, 5.f, 0.f));
	//
	// assertEpsilonEquals(new Point3f(),
	// MathUtil.getLineLineIntersectionPoint(
	// -1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
	// -1.f, 1.f, 0.f, 1.f, -1.f, 0.f));
	//
	//
	// assertEpsilonEquals(new Point3f(2.f,44.5f,0.f),
	// MathUtil.getLineLineIntersectionPoint(
	// 2.f, -32.f, 0.f, 2.f, 100.f, 0.f,
	// 101.f, 44.5f, 0.f, 100.f, 44.5f, 0.f));
	//
	// assertEpsilonEquals(
	// new Point3f(0.f, 1.f, 1.f),
	// MathUtil.getLineLineIntersectionPoint(
	// 0.f, 1.f, 1.f, 0.f, 2.f, 2.f,
	// 0.f, 1.f, 1.f, 1.f, 2.f, 2.f));
	//
	// assertEpsilonEquals(
	// new Point3f(1.f, 0.f, 1.f),
	// MathUtil.getLineLineIntersectionPoint(
	// 1.f, 0.f, 1.f, 2.f, 0.f, 2.f,
	// 1.f, 0.f, 1.f, 2.f, 1.f, 2.f));
	//
	// assertEpsilonEquals(
	// new Point3f(1.f, 1.f, 0.f),
	// MathUtil.getLineLineIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 2.f, 2.f, 1.f));
	//
	// assertNull(
	// MathUtil.getLineLineIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 1.f, 2.f, 2.f, 1.f));
	//
	// // Equation resolution with GNU Octave.
	// //
	// // Linear Equation Resolution:
	// // a.getX() + b.getY() + c.getZ() = d
	// // e.getX() + f.getY() + g.getZ() = h
	// // i.getX() + j.getY() + k.getZ() = l
	// //
	// // A = [ a b c ]
	// // [ e f g ]
	// // [ i j k ]
	// //
	// // b = [ d ]
	// // [ h ]
	// // [ l ]
	// //
	// // A \ b = [ x ]
	// // [ y ]
	// // [ z ]
	// //
	// // Back to line intersection problem:
	// //
	// // P1 + m.(P2-P1) = I
	// // P3 + n.(P4-P3.f) = I
	// //
	// // m.(x2-x1) - n.(x4-x3.f) = x3-x1
	// // m.(y2-y1) - n.(y4-y3.f) = y3-y1
	// // m.(z2-z1) - n.(z4-z3.f) = z3-z1
	// //
	// // A = [ (x2-x1) (x3-x4) 0 ]
	// // [ (y2-y1) (y3-y4) 0 ]
	// // [ (z2-z1) (z3-z4) 0 ]
	// //
	// // b = [ x3-x1 ]
	// // [ y3-y1 ]
	// // [ z3-z1 ]
	// //
	// // R = A \ b = [ m ]
	// // [ n ]
	// // [ _ ]
	// //
	// // [ x ] = P1 + m * (P2 - P1) = [ x1 ] + m * [ x2-x1 ]
	// // [ y ] [ y1 ] [ y2-y1 ]
	// // [ z ] [ z1 ] [ z2-z1 ]
	//
	// // Octave Code:
	// // output_precision(16)
	// // P = { -2.f, 3.f, 0.f, -1.f, 6f, 0.f, -3.f, 28.f, 0.f, 0.f, 24.f, 0. }
	// // x1=P{1}, y1=P{2}, z1=P{3}, x2=P{4}, y2=P{5}, z2=P{6}, x3=P{7},
	// y3=P{8}, z3=P{9}, x4=P{10}, y4=P{11}, z4=P{12}
	// // A = [ x2-x1, x3-x4f, 0.; y2-y1, y3-y4f, 0.; z2-z1, z3-z4f, 0. ]
	// // b = [ x3-x1; y3-y1; z3-z1 ]
	// // R = A \ b
	// // I = [ x1 + R(1) * (x2-x1), y1 + R(1) * (y2-y1), z1 + R(1) * (z2-z1) ]
	// assertEpsilonEquals(new Point3f(3.461538461538461e+00,
	// 1.938461538461538e+01, 0.f),
	// MathUtil.getLineLineIntersectionPoint(
	// -2.f, 3.f, 0.f, -1.f, 6f, 0.f,
	// -3.f, 28.f, 0.f, 0.f, 24.f, 0.f));
	//
	// // P = { 1.f, 1.f, 0.f, 2.f, 2.f, 0.f, 1.f, 1.f, 0.f, 2.f, 2.f, 1. }
	// assertEpsilonEquals(new Point3f(1.f, 1.f, 0.f),
	// MathUtil.getLineLineIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 2.f, 2.f, 1.f));
	//
	// //
	// // Values from Olivier L. (Points are not coplanars)
	// //
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// -10,-10,0,
	// 1, 1, 3,
	// -3, 5f, 3,
	// 3, -5f, 3.f));
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// 1, 1, 3,
	// -10,-10,0,
	// -3, 5f, 3,
	// 3, -5f, 3.f));
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// 1, 1, 3,
	// -10,-10,0,
	// 3, -5f, 3,
	// -3, 5f, 3.f));
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// -10,-10,0,
	// 1, 1, 3,
	// 3, -5f, 3,
	// -3, 5f, 3.f));
	// //
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// -3, 5f, 3,
	// 3, -5f, 3,
	// -10,-10,0,
	// 1, 1, 3.f));
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// 3, -5f, 3,
	// -3, 5f, 3,
	// -10,-10,0,
	// 1, 1, 3.f));
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// 3, -5f, 3,
	// -3, 5f, 3,
	// 1, 1, 3,
	// -10,-10,0));
	// assertNull(MathUtil.getLineLineIntersectionPoint(
	// -3, 5f, 3,
	// 3, -5f, 3,
	// 1, 1, 3,
	// -10,-10,0));
	// }
	//
	// /**
	// */
	// public void testIsCoplanarPointsFloatFloatArray() {
	// //
	// // Values from Olivier L. (Points are not coplanars)
	// //
	// Point3f s1 = new Point3f(-10,-10,0);
	// Point3f e1 = new Point3f(1, 1, 3.f);
	// Point3f s2 = new Point3f(-3, 5f, 3.f);
	// Point3f e2 = new Point3f(3, -5f, 3.f);
	//
	// assertTrue(MathUtil.isCoplanarPoints(
	// 0.f, //epsilon
	// s1));
	//
	// assertTrue(MathUtil.isCoplanarPoints(
	// 0.f, //epsilon
	// s1, e1));
	//
	// assertTrue(MathUtil.isCoplanarPoints(
	// 0.f, //epsilon
	// s1, e1,
	// s2));
	// assertTrue(MathUtil.isCoplanarPoints(
	// 0.f, //epsilon
	// s1, e1,
	// e2));
	//
	// assertFalse(MathUtil.isCoplanarPoints(
	// 0.f, //epsilon
	// s1, e1,
	// s2, e2));
	// assertFalse(MathUtil.isCoplanarPoints(
	// 0.f, //epsilon
	// e1, s1,
	// s2, e2));
	// assertFalse(MathUtil.isCoplanarPoints(
	// 0.f, //epsilon
	// e1, s1,
	// e2, s2));
	// assertFalse(MathUtil.isCoplanarPoints(
	// 0.f, //epsilon
	// s1, e1,
	// e2, s2));
	// }
	//

	//
	// /**
	// */
	// public void
	// testGetSegmentSegmentIntersectionPointFloatFloatFloatFloatFloatFloatFloatFloat()
	// {
	// assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
	// 1.f, 1.f, 2.f, 2.f,
	// 1.f, 1.f, 2.f, 2.f));
	//
	// assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
	// 1.f, 1.f, 2.f, 2.f,
	// 1.f, 1.f, -2.f, -2.f));
	//
	// assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
	// 1.f, 1.f, 2.f, 2.f,
	// 2.f, 2.f, 1.f, 1.f));
	//
	// assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
	// 1.f, 1.f, 2.f, 2.f,
	// 1.f, 2.f, 2.f, 3.f));
	//
	// assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
	// 1.f, 1.f, 2.f, 2.f,
	// 1.f, 1.f, 3.f, 3.f));
	//
	// assertEpsilonEquals(new Point2f(1.f, 1.f),
	// GeometryUtil.getIntersectionPointSegmentSegment(
	// 1.f, 1.f, 2.f, 2.f,
	// 1.f, 1.f, 3.f, 9.f));
	//
	// assertEpsilonEquals(new Point2f(2.f, 2.f),
	// GeometryUtil.getIntersectionPointSegmentSegment(
	// 1.f, 1.f, 2.f, 2.f,
	// 10.f, 0.f, 2.f, 2.f));
	//
	// assertEpsilonEquals(new Point2f(0.f,0.f),
	// GeometryUtil.getIntersectionPointSegmentSegment(
	// -1.f, -1.f, 1.f, 1.f,
	// -1.f, 1.f, 1.f, -1.f));
	//
	// assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
	// 0.f, 0.f, 1.f, 0.f,
	// 1.5f, 0.f, 10.f, 0.f));
	//
	// assertNull(GeometryUtil.getIntersectionPointSegmentSegment(
	// -1.f, -1.f, 1.f, 1.f,
	// 2.f, 10.f, 20.f, -20.f));
	//
	// assertEpsilonEquals(
	// new Point2f(0.f, 0.f),
	// GeometryUtil.getIntersectionPointSegmentSegment(
	// -50.f, -50.f, 50.f, 50.f,
	// -50.f, 50.f, 50.f, -50.f));
	//
	// assertEpsilonEquals(
	// new Point2f(0.f, 0.f),
	// GeometryUtil.getIntersectionPointSegmentSegment(
	// -50.f, -50.f, 50.f, 50.f,
	// 50.f, -50.f, -50.f, 50.f));
	// }
	//
	// /**
	// */
	// public void
	// testGetSegmentSegmentIntersectionFactorFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat()
	// {
	// assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f));
	//
	// assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, -2.f, -2.f, 0.f));
	//
	// assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 2.f, 2.f, 0.f, 1.f, 1.f, 0.f));
	//
	// assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 2.f, 0.f, 2.f, 3.f, 0.f));
	//
	// assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 3.f, 3.f, 0.f));
	//
	// assertEpsilonEquals(0.f,
	// GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 3.f, 9.f, 0.f));
	//
	// assertEpsilonEquals(1.f,
	// GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 10.f, 0.f, 0.f, 2.f, 2.f, 0.f));
	//
	// assertEpsilonEquals(.5f,
	// GeometryUtil.getSegmentSegmentIntersectionFactor(
	// -1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
	// -1.f, 1.f, 0.f, 1.f, -1.f, 0.f));
	//
	// assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 0.f, 0.f, 0.f, 1.f, 0.f, 0.f,
	// 1.5f, 0.f, 0.f, 10.f, 0.f, 0.f));
	//
	// assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
	// -1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
	// 2.f, 10.f, 0.f, 20.f, -20.f, 0.f));
	//
	// assertNaN(GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 0.f, 1.f, 2.f, 0.f, 2.f,
	// 1.f, 0.f, 1.f, -2.f, 0.f, -2.f));
	//
	// assertEpsilonEquals(0.f,
	// GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 1.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 1.f, 2.f, 2.f, 2.f));
	//
	// assertEpsilonEquals(0.f,
	// GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 1.f, 2.f, 0.f, 2.f,
	// 1.f, 1.f, 1.f, 2.f, 2.f, 2.f));
	//
	// assertEpsilonEquals(0.f,
	// GeometryUtil.getSegmentSegmentIntersectionFactor(
	// 1.f, 1.f, 1.f, 0.f, 2.f, 2.f,
	// 1.f, 1.f, 1.f, 2.f, 2.f, 2.f));
	//
	// assertEpsilonEquals(.5f,
	// GeometryUtil.getSegmentSegmentIntersectionFactor(
	// -50.f, -50.f, 0.f, 50.f, 50.f, 0.f,
	// -50.f, 50.f, 0.f, 50.f, -50.f, 0.f));
	//
	// assertEpsilonEquals(.5f,
	// GeometryUtil.getSegmentSegmentIntersectionFactor(
	// -50.f, -50.f, 0.f, 50.f, 50.f, 0.f,
	// 50.f, -50.f, 0.f, -50.f, 50.f, 0.f));
	// }
	//
	// /**
	// */
	// public void
	// testGetSegmentSegmentIntersectionPointFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat()
	// {
	// assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f));
	//
	// assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, -2.f, -2.f, 0.f));
	//
	// assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 2.f, 2.f, 0.f, 1.f, 1.f, 0.f));
	//
	// assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 2.f, 0.f, 2.f, 3.f, 0.f));
	//
	// assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 3.f, 3.f, 0.f));
	//
	// assertEpsilonEquals(new Point3f(1.f, 1.f, 0.f),
	// GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 0.f, 3.f, 9.f, 0.f));
	//
	// assertEpsilonEquals(new Point3f(2.f, 2.f, 0.f),
	// GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 0.f, 2.f, 2.f, 0.f,
	// 10.f, 0.f, 0.f, 2.f, 2.f, 0.f));
	//
	// assertEpsilonEquals(new Point3f(0.f, 0.f, 0.f),
	// GeometryUtil.getSegmentSegmentIntersectionPoint(
	// -1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
	// -1.f, 1.f, 0.f, 1.f, -1.f, 0.f));
	//
	// assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 0.f, 0.f, 0.f, 1.f, 0.f, 0.f,
	// 1.5f, 0.f, 0.f, 10.f, 0.f, 0.f));
	//
	// assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
	// -1.f, -1.f, 0.f, 1.f, 1.f, 0.f,
	// 2.f, 10.f, 0.f, 20.f, -20.f, 0.f));
	//
	// assertNull(GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 0.f, 1.f, 2.f, 0.f, 2.f,
	// 1.f, 0.f, 1.f, -2.f, 0.f, -2.f));
	//
	// assertEpsilonEquals(new Point3f(1.f, 1.f, 1.f),
	// GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 1.f, 2.f, 2.f, 0.f,
	// 1.f, 1.f, 1.f, 2.f, 2.f, 2.f));
	//
	// assertEpsilonEquals(new Point3f(1.f, 1.f, 1.f),
	// GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 1.f, 2.f, 0.f, 2.f,
	// 1.f, 1.f, 1.f, 2.f, 2.f, 2.f));
	//
	// assertEpsilonEquals(new Point3f(1.f, 1.f, 1.f),
	// GeometryUtil.getSegmentSegmentIntersectionPoint(
	// 1.f, 1.f, 1.f, 0.f, 2.f, 2.f,
	// 1.f, 1.f, 1.f, 2.f, 2.f, 2.f));
	//
	// assertEpsilonEquals(
	// new Point3f(0.f, 0.f, 0.f),
	// GeometryUtil.getSegmentSegmentIntersectionPoint(
	// -50.f, -50.f, 0.f, 50.f, 50.f, 0.f,
	// -50.f, 50.f, 0.f, 50.f, -50.f, 0.f));
	//
	// assertEpsilonEquals(
	// new Point3f(0.f, 0.f, 0.f),
	// GeometryUtil.getSegmentSegmentIntersectionPoint(
	// -50.f, -50.f, 0.f, 50.f, 50.f, 0.f,
	// 50.f, -50.f, 0.f, -50.f, 50.f, 0.f));
	// }
	//
	// /**
	// */
	// public void testInterpolateLineFloatFloatFloatFloatFloatFloatFloat() {
	// assertEpsilonEquals(new Point3f(-1,-3,8), MathUtil.interpolateLine(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, -1.f));
	// assertEpsilonEquals(new Point3f(0,-.5f,6), MathUtil.interpolateLine(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, -.5f));
	// assertEpsilonEquals(new Point3f(1,2,4), MathUtil.interpolateLine(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, 0.f));
	// assertEpsilonEquals(new Point3f(1.5f,3.25f,3.f),
	// MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .25f));
	// assertEpsilonEquals(new Point3f(2,4.5f,2), MathUtil.interpolateLine(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, .5f));
	// assertEpsilonEquals(new Point3f(2.5f,5.75f,1),
	// MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .75f));
	// assertEpsilonEquals(new Point3f(3,7f,0), MathUtil.interpolateLine(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, 1));
	// assertEpsilonEquals(new Point3f(4f,9.5f,-2),
	// MathUtil.interpolateLine(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, 1.5f));
	// assertEpsilonEquals(new Point3f(5f,12,-4), MathUtil.interpolateLine(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, 2.f));
	// }
	//
	// /**
	// */
	// public void testInterpolateSegmentFloatFloatFloatFloatFloatFloatFloat() {
	// assertEpsilonEquals(new Point3f(1,2,4), MathUtil.interpolateSegment(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, -1.f));
	// assertEpsilonEquals(new Point3f(1,2,4), MathUtil.interpolateSegment(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, -.5f));
	// assertEpsilonEquals(new Point3f(1,2,4), MathUtil.interpolateSegment(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, 0.f));
	// assertEpsilonEquals(new Point3f(1.5f,3.25f,3.f),
	// MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .25f));
	// assertEpsilonEquals(new Point3f(2,4.5f,2),
	// MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .5f));
	// assertEpsilonEquals(new Point3f(2.5f,5.75f,1),
	// MathUtil.interpolateSegment(1.f, 2.f, 4.f, 3.f, 7.f, 0.f, .75f));
	// assertEpsilonEquals(new Point3f(3,7f,0), MathUtil.interpolateSegment(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, 1));
	// assertEpsilonEquals(new Point3f(3,7f,0), MathUtil.interpolateSegment(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, 1.5f));
	// assertEpsilonEquals(new Point3f(3,7f,0), MathUtil.interpolateSegment(1.f,
	// 2.f, 4.f, 3.f, 7.f, 0.f, 2.f));
	// }
	//
	// /**
	// */
	// public void testInterpolateLineFloatFloatFloatFloatFloat() {
	// assertEpsilonEquals(new Point2f(-1,0), MathUtil.interpolateLine(1.f, 2.f,
	// 3.f, 4.f, -1.f));
	// assertEpsilonEquals(new Point2f(0,1), MathUtil.interpolateLine(1.f, 2.f,
	// 3.f, 4.f, -.5f));
	// assertEpsilonEquals(new Point2f(1,2), MathUtil.interpolateLine(1.f, 2.f,
	// 3.f, 4.f, 0.f));
	// assertEpsilonEquals(new Point2f(1.5f,2.5f), MathUtil.interpolateLine(1.f,
	// 2.f, 3.f, 4.f, .25f));
	// assertEpsilonEquals(new Point2f(2,3.f), MathUtil.interpolateLine(1.f,
	// 2.f, 3.f, 4.f, .5f));
	// assertEpsilonEquals(new Point2f(2.5f,3.5f), MathUtil.interpolateLine(1.f,
	// 2.f, 3.f, 4.f, .75f));
	// assertEpsilonEquals(new Point2f(3,4), MathUtil.interpolateLine(1.f, 2.f,
	// 3.f, 4.f, 1.f));
	// assertEpsilonEquals(new Point2f(4f,5f), MathUtil.interpolateLine(1.f,
	// 2.f, 3.f, 4.f, 1.5f));
	// assertEpsilonEquals(new Point2f(5f,6), MathUtil.interpolateLine(1.f, 2.f,
	// 3.f, 4.f, 2.f));
	// }
	//
	// /**
	// */
	// public void testInterpolateSegmentFloatFloatFloatFloatFloat() {
	// assertEpsilonEquals(new Point2f(1,2), MathUtil.interpolateSegment(1.f,
	// 2.f, 3.f, 4.f, -1.f));
	// assertEpsilonEquals(new Point2f(1,2), MathUtil.interpolateSegment(1.f,
	// 2.f, 3.f, 4.f, -.5f));
	// assertEpsilonEquals(new Point2f(1,2), MathUtil.interpolateSegment(1.f,
	// 2.f, 3.f, 4.f, 0.f));
	// assertEpsilonEquals(new Point2f(1.5f,2.5f),
	// MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, .25f));
	// assertEpsilonEquals(new Point2f(2,3.f), MathUtil.interpolateSegment(1.f,
	// 2.f, 3.f, 4.f, .5f));
	// assertEpsilonEquals(new Point2f(2.5f,3.5f),
	// MathUtil.interpolateSegment(1.f, 2.f, 3.f, 4.f, .75f));
	// assertEpsilonEquals(new Point2f(3,4), MathUtil.interpolateSegment(1.f,
	// 2.f, 3.f, 4.f, 1.f));
	// assertEpsilonEquals(new Point2f(3,4), MathUtil.interpolateSegment(1.f,
	// 2.f, 3.f, 4.f, 1.5f));
	// assertEpsilonEquals(new Point2f(3,4), MathUtil.interpolateSegment(1.f,
	// 2.f, 3.f, 4.f, 2.f));
	// }
	//
	//
	//
	// /**
	// */
	// public void testClosestPointSegmentSegment() {
	// Point3f nearest1 = new Point3f();
	// Point3f nearest2 = new Point3f();
	// OutputParameter<Float> s = new OutputParameter<Float>();
	// OutputParameter<Float> t = new OutputParameter<Float>();
	//
	// float d = MathUtil.closestPointSegmentSegment(new Point3f(-3,3,0), new
	// Point3f(-1,1,0), new Point3f(3,3,0), new Point3f(1,1,0), s, t, nearest1,
	// nearest2);
	// assertEpsilonEquals(4.0f,d);
	// assertEpsilonEquals(new Point3f(-1,1,0),nearest1);
	// assertEpsilonEquals(new Point3f(1,1,0),nearest2);
	//
	// d = MathUtil.closestPointSegmentSegment(new Point3f(-4f,5f,0), new
	// Point3f(1,5f,0), new Point3f(-3.5f,7f,0), new Point3f(-1,6f,0), s, t,
	// nearest1, nearest2);
	// assertEpsilonEquals(1.0f,d);
	// assertEpsilonEquals(new Point3f(-1,5f,0),nearest1);
	// assertEpsilonEquals(new Point3f(-1,6f,0),nearest2);
	//
	// d = MathUtil.closestPointSegmentSegment(new Point3f(-1,-3.5f,0), new
	// Point3f(1.5f,-3.5f,0), new Point3f(1,-1,0), new Point3f(4f,-4f,0), s, t,
	// nearest1, nearest2);
	// assertEpsilonEquals(2.0f,d);
	// assertEpsilonEquals(new Point3f(1.5f,-3.5f,0),nearest1);
	// assertEpsilonEquals(new Point3f(2.5f,-2.5f,0),nearest2);
	//
	// d = MathUtil.closestPointSegmentSegment(new Point3f(-4f,-1,0), new
	// Point3f(-1,-2.5f,0), new Point3f(-5f,-2,0), new Point3f(-1,-1,0), s, t,
	// nearest1, nearest2);
	// assertEpsilonEquals(0.0f,d);
	// assertEpsilonEquals(new Point3f(-3,-1.5f,0),nearest1);
	// assertEpsilonEquals(new Point3f(-3,-1.5f,0),nearest2);
	// }
	//
	//
}
