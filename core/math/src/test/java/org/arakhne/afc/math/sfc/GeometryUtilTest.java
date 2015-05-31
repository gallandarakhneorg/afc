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
package org.arakhne.afc.math.sfc;

import org.arakhne.afc.math.geometry.sfc.geometry.GeometryUtil;
import org.arakhne.afc.math.geometry.sfc.geometry2d.continuous.Point2f;
import org.arakhne.afc.math.geometry.sfc.geometry2d.continuous.Vector2f;
import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.Point3f;
import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.Vector3f;
import org.arakhne.afc.math.sfc.MathUtil;

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

		float x1, y1, x2, y2;

		x1 = 3;
		y1 = 3;
		x2 = 3;
		y2 = 3;
		assertEpsilonEquals(1,
				GeometryUtil.distancePointSegment(4, 3, x1, y1, x2, y2, null));

		Point2f[] Test = { new Point2f(3, 2), new Point2f(7, 1),
				new Point2f(7, 3), new Point2f(8, -2), new Point2f(3, 3),
				new Point2f(8, -1), new Point2f(9, 6), new Point2f(8, 1),
				new Point2f(6, 7),
				new Point2f(8.33826080789595, 2.4286488907037),
				new Point2f(5, 6), new Point2f(8, 3), new Point2f(3, 5),
				new Point2f(11, 4), new Point2f(2, 6), new Point2f(5, 3),
				new Point2f(2, 8), new Point2f(3, 2.59154609004047),
				new Point2f(1, 5), new Point2f(2.9, 2.8), new Point2f(6, 8),
				new Point2f(1, 2.5), new Point2f(0, 3),
				new Point2f(5.40941176470589, 2.60235294117647),
				new Point2f(2, 2), new Point2f(0, 1.5), new Point2f(1, 1.5),
				new Point2f(2, 1.5), new Point2f(3, 1),
				new Point2f(3.2, 0.600000000000001), new Point2f(3.2, 1.2),
				new Point2f(4, 1), new Point2f(5, 2) };
		float[] ResultA = { 0f, 1.940285000290664f, 0f, 5.093248125762993f,
				0.970142500145332f, 4.123105625617661f, 3.605551275463989f,
				2.23606797749979f, 4.123105625617661f, 1.455123389972284f,
				3.395498750508661f, 1f, 2.910427500435995f, 4.123105625617661f,
				4.123105625617661f, 0.485071250072666f, 6.063390625908325f,
				0.573884002743057f, 3.605551275463989f, 0.800367562619899f,
				5.099019513592784f, 2.06155281280883f, 3.16227766016838f,
				0.000000000000003f, 1f, 3.04138126514911f, 2.06155281280883f,
				1.118033988749895f, 1f, 1.414213562373094f, 0.824621125123532f,
				1.212678125181665f, 0.485071250072666f };
		float[] ResultB = { 0f, 4.123105625617661f, 4f, 6.403124237432848f, 0f,
				5.830951894845301f, 6.708203932499369f, 5.099019513592784f, 5f,
				5.338260807895949f, 3.605551275463989f, 5f, 2f,
				8.06225774829855f, 3.16227766016838f, 2f, 5.099019513592784f,
				0f, 2.82842712474619f, 0.1f, 5.830951894845301f, 2f, 3f,
				2.40941176470589f, 1f, 3.04138126514911f, 2.06155281280883f,
				1.118033988749895f, 1f, 1.414213562373094f, 0.824621125123532f,
				1.414213562373095f, 2f };
		float[] ResultC = { 1f, 2f, 0f, 5.099019513592784f, 0f,
				4.123105625617661f, 3.605551275463989f, 2.23606797749979f, 4f,
				1.455123389972284f, 3f, 1f, 2f, 4.123105625617661f,
				3.16227766016838f, 0f, 5.099019513592784f, 0.40845390995953f,
				2.82842712474619f, 0.223606797749979f, 5f, 2.06155281280883f,
				3f, 0.39764705882353f, 1.414213562373095f, 3.354101966249685f,
				2.5f, 1.802775637731995f, 2f, 2.399999999999999f, 1.8f, 2f, 1f };

		// Test for segment a;
		x1 = 3;
		y1 = 2;
		x2 = 7;
		y2 = 3;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(
					"i : " + i,
					ResultA[i],
					GeometryUtil.distancePointSegment(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2, null));
			assertEpsilonEquals(
					ResultA[i],
					GeometryUtil.distancePointSegment(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1, null));
		}

		// Test for segment b;
		x1 = 3;
		y1 = 2;
		x2 = 3;
		y2 = 3;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(
					ResultB[i],
					GeometryUtil.distancePointSegment(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2, null));
			assertEpsilonEquals(
					ResultB[i],
					GeometryUtil.distancePointSegment(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1, null));

		}

		// Test for segment c;
		x1 = 7;
		y1 = 3;
		x2 = 3;
		y2 = 3;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(
					ResultC[i],
					GeometryUtil.distancePointSegment(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2, null));
			assertEpsilonEquals(
					ResultC[i],
					GeometryUtil.distancePointSegment(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1, null));
		}
	}

	/**
	 */
	public void testDistancePointSegmentFloatFloatFloatFloatFloatFloatFloatFloatFloat() {// TODO

		// Giving points P1(1,-1,1), P2(-1,1,0) and M(x,y,z). Let's make the
		// segment P1P2. 3 cases.

		// The point is before P1 i.e. giving the affine hyperplane define by P1
		// and vector P1P2.
		assertEpsilonEquals(
				GeometryUtil.distancePointPoint(1, -1, 1, -1, -2, 4),
				GeometryUtil
						.distancePointSegment(-1, -2, 4, 1, -1, 1, -1, 1, 0));
		// TODO : test if this distance remains equal when moving the point on
		// the half sphere (P1, distance(P1,M))
		// The point is after P2
		assertEpsilonEquals(
				GeometryUtil.distancePointPoint(-1, 1, 0, -1, 2, -4),
				GeometryUtil
						.distancePointSegment(-1, 2, -4, 1, -1, 1, -1, 1, 0));
		// TODO c.f. previous note

		// The point is between P1 and P2.
		assertEpsilonEquals(
				GeometryUtil.distancePointLine(0, 0, 0, 1, -1, 1, -1, 1, 0),
				GeometryUtil.distancePointSegment(0, 0, 0, 1, -1, 1, -1, 1, 0));
		// TODO test if this distance remain the same when rotate the point
		// around the line P1P2

	}

	/**
	 */
	public void testDistancePointLineFloatFloatFloatFloatFloatFloat() {

		float x1, y1, x2, y2;

		Point2f[] Test = { new Point2f(-5, 0), new Point2f(10, 5),
				new Point2f(-5, 5), new Point2f(35, 10), new Point2f(25, 10),
				new Point2f(25, 5), new Point2f(25, 0), new Point2f(-5, -5),
				new Point2f(-10, -5), new Point2f(-15, 0),
				new Point2f(-10, 10), new Point2f(0, 15) };
		float[] ResultA = { 0f, 0f, 4.743416490252569f, 3.16227766016838f, 0f,
				4.743416490252569f, 9.486832980505138f, 4.743416490252569f,
				3.16227766016838f, 3.16227766016838f, 11.067971810589327f,
				12.649110640673518f };
		float[] ResultB = { 5f, 0f, 0f, 5f, 5f, 0f, 5f, 10f, 10f, 5f, 5f, 10 };
		float[] ResultC = { 0f, 15f, 0f, 40f, 30f, 30f, 30f, 0f, 5f, 10f, 5f, 5 };

		// Test for line a;
		x1 = -5;
		y1 = 0;
		x2 = 10;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(
					"i : " + i,
					ResultA[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2));
			assertEpsilonEquals(
					ResultA[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1));
		}

		// Test for line b;
		x1 = -5;
		y1 = 5;
		x2 = 10;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(
					ResultB[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2));
			assertEpsilonEquals(
					ResultB[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1));

		}

		// Test for line c;
		x1 = -5;
		y1 = 0;
		x2 = -5;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(
					ResultC[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2));
			assertEpsilonEquals(
					ResultC[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1));
		}

	}

/*	public void testDistancePointPointFloatFloatFloatFloat() {
		assert (true);
	}

	public void testDistancePointPointFloatFloatFloatFloatFloatFloat() {
		assertTrue(true);
	}

	public void testIsClosedToLinePointLineFloatFloatFloatFloatFloatFloatFloat() {

		assertTrue(true);
	}
*/

	private void testPermutationIntersectionFactorSegSeg(float x1, float y1,
			float x2, float y2, float x3, float y3, float x4, float y4,
			float result) {

		assertEpsilonEquals(result,
				GeometryUtil.getIntersectionFactorSegmentSegment(x1, y1, x2,
						y2, x3, y3, x4, y4));
		assertEpsilonEquals(result,
				GeometryUtil.getIntersectionFactorSegmentSegment(x2, y2, x1,
						y1, x3, y3, x4, y4));
		assertEpsilonEquals(result,
				GeometryUtil.getIntersectionFactorSegmentSegment(x1, y1, x2,
						y2, x4, y4, x3, y3));
		assertEpsilonEquals(result,
				GeometryUtil.getIntersectionFactorSegmentSegment(x2, y2, x1,
						y1, x4, y4, x3, y3));

		assertEpsilonEquals(result,
				GeometryUtil.getIntersectionFactorSegmentSegment(x3, y3, x4,
						y4, x1, y1, x2, y2));
		assertEpsilonEquals(result,
				GeometryUtil.getIntersectionFactorSegmentSegment(x3, y3, x4,
						y4, x2, y2, x1, y1));
		assertEpsilonEquals(result,
				GeometryUtil.getIntersectionFactorSegmentSegment(x4, y4, x3,
						y3, x1, y1, x2, y2));
		assertEpsilonEquals(result,
				GeometryUtil.getIntersectionFactorSegmentSegment(x4, y4, x3,
						y3, x2, y2, x1, y1));
	}

	/**
	 * <img src="./doc-files/truc.png"/>
	 */
	public void testGetIntersectionFactorSegmentSegmentFloatFloatFloatFloatFloatFloatFloatFloat() {

		// ABCD
		testPermutationIntersectionFactorSegSeg(3, 3, 4, 3, 5, 3, 6, 3,
				Float.NaN);
		// PQRS
		testPermutationIntersectionFactorSegSeg(3, 1, 4, 1, 4, 0, 5, 0,
				Float.NaN);
		// TUVW
		testPermutationIntersectionFactorSegSeg(3, -2, 4, -1, 5, -1, 6, -1,
				Float.NaN);
		// M2N2O2P2
		testPermutationIntersectionFactorSegSeg(13, 1, 13, 0, 13, -1, 13, -2,
				Float.NaN);
		// D1E1F1G1
		testPermutationIntersectionFactorSegSeg(7, 5, 8, 5, 7, 3, 8, 4,
				Float.NaN);
		// R1S1T1T1
		testPermutationIntersectionFactorSegSeg(10, 5, 11, 5, 12, 5, 12, 5,
				Float.NaN);
		// W1W1Z1Z1
		testPermutationIntersectionFactorSegSeg(10, 3, 10, 3, 11, 3, 11, 3,
				Float.NaN);
		// EFGH
		testPermutationIntersectionFactorSegSeg(3, 5, 5, 5, 4, 5, 6, 5,
				Float.POSITIVE_INFINITY);
		// LMNO
		testPermutationIntersectionFactorSegSeg(3, 2, 6, 2, 4, 2, 5, 2,
				Float.POSITIVE_INFINITY);
		// B2C2D2E2
		testPermutationIntersectionFactorSegSeg(14, 5, 14, 3, 14, 4, 14, 2,
				Float.POSITIVE_INFINITY);
		// I2J2K2L2
		testPermutationIntersectionFactorSegSeg(12, 3, 12, 0, 12, 2, 12, 1,
				Float.POSITIVE_INFINITY);
		// R2S2R2T2
		testPermutationIntersectionFactorSegSeg(3, 6, 4, 6, 3, 6, 5, 6,
				Float.POSITIVE_INFINITY);

		// IJJK
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(3, 4, 4, 4, 4,
						4, 5, 4));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(4, 4, 3, 4, 4,
						4, 5, 4));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(3, 4, 4, 4, 5,
						4, 4, 4));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(4, 4, 3, 4, 5,
						4, 4, 4));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(4, 4, 5, 4, 3,
						4, 4, 4));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(4, 4, 5, 4, 4,
						4, 3, 4));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(5, 4, 4, 4, 3,
						4, 4, 4));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(5, 4, 4, 4, 4,
						4, 3, 4));
		// K1L1M1N1
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(8, -2, 9, -1,
						8, -1, 10, -1));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(9, -1, 8, -2,
						8, -1, 10, -1));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(8, -2, 9, -1,
						10, -1, 8, -1));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(9, -1, 8, -2,
						10, -1, 8, -1));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(8, -1, 10, -1,
						8, -2, 9, -1));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(8, -1, 10, -1,
						9, -1, 8, -2));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(10, -1, 8, -1,
						8, -2, 9, -1));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(10, -1, 8, -1,
						9, -1, 8, -2));
		// O1P1P1Q1
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(5, 1, 6, 1, 6,
						1, 6, 0));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(6, 1, 5, 1, 6,
						1, 6, 0));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(5, 1, 6, 1, 6,
						0, 6, 1));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(6, 1, 5, 1, 6,
						0, 6, 1));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(6, 1, 6, 0, 5,
						1, 6, 1));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(6, 1, 6, 0, 6,
						1, 5, 1));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(6, 0, 6, 1, 5,
						1, 6, 1));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(6, 0, 6, 1, 6,
						1, 5, 1));
		// ZA2A1B1
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(7, 2, 9, 2, 8,
						2, 7, 1));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(9, 2, 7, 2, 8,
						2, 7, 1));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(7, 2, 9, 2, 7,
						1, 8, 2));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(9, 2, 7, 2, 7,
						1, 8, 2));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(8, 2, 7, 1, 7,
						2, 9, 2));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(8, 2, 7, 1, 9,
						2, 7, 2));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(7, 1, 8, 2, 7,
						2, 9, 2));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(7, 1, 8, 2, 9,
						2, 7, 2));
		// C1H1I1J1
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(7, 0, 9, 0, 7,
						-1, 9, 1));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(9, 0, 7, 0, 7,
						-1, 9, 1));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(7, 0, 9, 0, 9,
						1, 7, -1));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(9, 0, 7, 0, 9,
						1, 7, -1));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(7, -1, 9, 1,
						7, 0, 9, 0));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(7, -1, 9, 1,
						9, 0, 7, 0));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(9, 1, 7, -1,
						7, 0, 9, 0));
		assertEpsilonEquals(0.5f,
				GeometryUtil.getIntersectionFactorSegmentSegment(9, 1, 7, -1,
						9, 0, 7, 0));
		// F2G2G2H2
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(13, 4, 13, 3,
						13, 3, 13, 2));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(13, 3, 13, 4,
						13, 3, 13, 2));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(13, 4, 13, 3,
						13, 2, 13, 3));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(13, 3, 13, 4,
						13, 2, 13, 3));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(13, 3, 13, 2,
						13, 4, 13, 3));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(13, 3, 13, 2,
						13, 3, 13, 4));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(13, 2, 13, 3,
						13, 4, 13, 3));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(13, 2, 13, 3,
						13, 3, 13, 4));

		// U1V1V1V1
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(10, 4, 11, 4,
						11, 4, 11, 4));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(11, 4, 10, 4,
						11, 4, 11, 4));
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(10, 4, 11, 4,
						11, 4, 11, 4));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(11, 4, 11, 4,
						11, 4, 10, 4));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(11, 4, 11, 4,
						10, 4, 11, 4));
		assertEpsilonEquals(0,
				GeometryUtil.getIntersectionFactorSegmentSegment(11, 4, 11, 4,
						11, 4, 10, 4));

		// Q2
		assertEpsilonEquals(1,
				GeometryUtil.getIntersectionFactorSegmentSegment(10, 1, 10, 1,
						10, 1, 10, 1));

	}

	/**
	 */
	public void testGetIntersectionPointSegmentSegmentFloatFloatFloatFloatFloatFloatFloatFloat() {
		// The function
		// testGetIntersectionFactorSegmentSegmentFloatFloatFloatFloatFloatFloatFloatFloat
		// has already tested all kind of possible
		// permutations so we only have to test one example of every case.

		Point2f p = new Point2f();

		// Intersection
		GeometryUtil.getIntersectionPointSegmentSegment(-1f, -1f, 1f, 1f, -1f,
				1f, 1f, -1f, p);
		assertEpsilonEquals(new Point2f(0, 0), p);
		GeometryUtil.getIntersectionPointSegmentSegment(-1f, -1f, 1f, 1f, 1f,
				1f, 1f, -1f, p);
		assertEpsilonEquals(new Point2f(1, 1), p);
		GeometryUtil.getIntersectionPointSegmentSegment(-1f, -1f, 1f, 1f, -1f,
				-1f, 1f, -1f, p);
		assertEpsilonEquals(new Point2f(-1, -1), p);
		GeometryUtil.getIntersectionPointSegmentSegment(-1f, -1f, 2f, 2f, -1f,
				1f, 1f, -1f, p);
		assertEpsilonEquals(new Point2f(0, 0), p);
		// NaN
		GeometryUtil.getIntersectionPointSegmentSegment(-1f, -1f, -1f, 1f, 1f,
				1f, 1f, -1f, p);
		assertEpsilonEquals(new Point2f(Float.NaN, Float.NaN), p);
		// PositiveInfinity
		GeometryUtil.getIntersectionPointSegmentSegment(-1f, -1f, 1f, 1f, -1f,
				-1f, 2f, 2f, p);
		assertEpsilonEquals(new Point2f(Float.POSITIVE_INFINITY,
				Float.POSITIVE_INFINITY), p);
	}

	private void testPermutationIntersectionPointLineLine(float x1, float y1,
			float x2, float y2, float x3, float y3, float x4, float y4,
			Point2f result) {

		Point2f p = new Point2f();

		GeometryUtil.getIntersectionPointLineLine(x1, y1, x2, y2, x3, y3, x4,
				y4, p);
		assertEpsilonEquals(result, p);
		GeometryUtil.getIntersectionPointLineLine(x2, y2, x1, y1, x3, y3, x4,
				y4, p);
		assertEpsilonEquals(result, p);

		GeometryUtil.getIntersectionPointLineLine(x1, y1, x2, y2, x4, y4, x3,
				y3, p);
		assertEpsilonEquals(result, p);

		GeometryUtil.getIntersectionPointLineLine(x2, y2, x1, y1, x4, y4, x3,
				y3, p);
		assertEpsilonEquals(result, p);
		GeometryUtil.getIntersectionPointLineLine(x3, y3, x4, y4, x1, y1, x2,
				y2, p);
		assertEpsilonEquals(result, p);

		GeometryUtil.getIntersectionPointLineLine(x3, y3, x4, y4, x2, y2, x1,
				y1, p);
		assertEpsilonEquals(result, p);

		GeometryUtil.getIntersectionPointLineLine(x4, y4, x3, y3, x1, y1, x2,
				y2, p);
		assertEpsilonEquals(result, p);

		GeometryUtil.getIntersectionPointLineLine(x4, y4, x3, y3, x2, y2, x1,
				y1, p);
		assertEpsilonEquals(result, p);

	}

	/**
	 */
	public void testGetIntersectionPointLineLineFloatFloatFloatFloatFloatFloatFloatFloat() {

		// Null param Test
		Point2f p;

		p = new Point2f(Float.NaN, Float.NaN); // NaN case
		// PQRS
		testPermutationIntersectionPointLineLine(3, 1, 4, 1, 4, 0, 5, 0, p);

		p = new Point2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
		// M2N2O2P2
		testPermutationIntersectionPointLineLine(13, 1, 13, 0, 13, -1, 13, -2,
				p);
		// EFGH
		testPermutationIntersectionPointLineLine(3, 5, 5, 5, 4, 5, 6, 5, p);
		// LMNO
		testPermutationIntersectionPointLineLine(3, 2, 6, 2, 4, 2, 5, 2, p);
		// B2C2D2E2
		testPermutationIntersectionPointLineLine(14, 5, 14, 3, 14, 4, 14, 2, p);
		// I2J2K2L2
		testPermutationIntersectionPointLineLine(12, 3, 12, 0, 12, 2, 12, 1, p);
		// R2S2R2T2
		testPermutationIntersectionPointLineLine(3, 6, 4, 6, 3, 6, 5, 6, p);
		// ABCD
		testPermutationIntersectionPointLineLine(3, 3, 4, 3, 5, 3, 6, 3, p);
		// IJJK
		testPermutationIntersectionPointLineLine(5, 4, 4, 4, 4, 4, 3, 4, p);
		// F2G2G2H2
		testPermutationIntersectionPointLineLine(13, 4, 13, 3, 13, 3, 13, 2, p);

		// TUVW
		testPermutationIntersectionPointLineLine(3, -2, 4, -1, 5, -1, 6, -1,
				new Point2f(4, -1));
		// D1E1F1G1
		testPermutationIntersectionPointLineLine(7, 5, 8, 5, 7, 3, 8, 4,
				new Point2f(9, 5));
		// K1L1M1N1
		testPermutationIntersectionPointLineLine(8, -2, 9, -1, 8, -1, 10, -1,
				new Point2f(9, -1));
		// O1P1P1Q1
		testPermutationIntersectionPointLineLine(5, 1, 6, 1, 6, 1, 6, 0,
				new Point2f(6, 1));
		// ZA2A1B1
		testPermutationIntersectionPointLineLine(7, 2, 9, 2, 8, 2, 7, 1,
				new Point2f(8, 2));
		// C1H1I1J1
		testPermutationIntersectionPointLineLine(7, 0, 9, 0, 7, -1, 9, 1,
				new Point2f(8, 0));
	}

	/**
	 * 
	 */
	public void testDistanceRelativeLinePointFloatFloatFloatFloatFloatFloat() {
		float x1, y1, x2, y2;

		Point2f[] Test = { new Point2f(-5, 0), new Point2f(10, 5),
				new Point2f(-5, 5), new Point2f(35, 10), new Point2f(25, 10),
				new Point2f(25, 5), new Point2f(25, 0), new Point2f(-5, -5),
				new Point2f(-10, -5), new Point2f(-15, 0),
				new Point2f(-10, 10), new Point2f(0, 15) };
		float[] ResultA = { 0f, 0f, 4.743416490252569f, -3.16227766016838f, 0f,
				-4.743416490252569f, -9.486832980505138f, -4.743416490252569f,
				-3.16227766016838f, 3.16227766016838f, 11.067971810589327f,
				12.649110640673518f };
		float[] ResultB = { -5, 0, 0, 5, 5, 0, -5, -10, -10, -5, 5, 10 };
		float[] ResultC = { 0, -15, 0, -40, -30, -30, -30, 0, 5, 10, 5, -5 };

		// Test for line a;
		x1 = -5;
		y1 = 0;
		x2 = 10;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals("i : " + i, -ResultA[i],
					GeometryUtil.distanceRelativePointLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2));
			assertEpsilonEquals(ResultA[i],
					GeometryUtil.distanceRelativePointLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1));
		}

		// Test for line b;
		x1 = -5;
		y1 = 5;
		x2 = 10;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(-ResultB[i],
					GeometryUtil.distanceRelativePointLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2));
			assertEpsilonEquals(ResultB[i],
					GeometryUtil.distanceRelativePointLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1));

		}

		// Test for line c;
		x1 = -5;
		y1 = 0;
		x2 = -5;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(-ResultC[i],
					GeometryUtil.distanceRelativePointLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2));
			assertEpsilonEquals(ResultC[i],
					GeometryUtil.distanceRelativePointLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1));
		}
	}

	public void testGetPointProjectionFactorOnLineFloatFloatFloatFloatFloatFloat() {
		float x1, y1, x2, y2;

		Point2f[] Test = { new Point2f(-5, 0), new Point2f(10, 5),
				new Point2f(-5, 5), new Point2f(35, 10), new Point2f(25, 10),
				new Point2f(25, 5), new Point2f(25, 0), new Point2f(-5, -5),
				new Point2f(-10, -5), new Point2f(-15, 0),
				new Point2f(-10, 10), new Point2f(0, 15) };
		float[] ResultA = { 0f, 1f, 0.1f, 2.6f, 2f, 1.9f, 1.8f, -0.1f, -0.4f,
				-0.6f, -0.1f, 0.6f };
		float[] ResultB = { 0f, 1f, 0f, 2.666666666666666f, 2f, 2f, 2f, 0f,
				-0.333333333333333f, -0.666666666666667f, -0.333333333333333f,
				0.333333333333333f };
		float[] ResultC = { 0f, 1f, 1f, 2f, 2f, 1f, 0f, -1f, -1f, 0f, 2f, 3f };

		// Test for line a;
		x1 = -5;
		y1 = 0;
		x2 = 10;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals("i : " + i, ResultA[i],
					GeometryUtil.getPointProjectionFactorOnLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2));
			assertEpsilonEquals(1 - ResultA[i],
					GeometryUtil.getPointProjectionFactorOnLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1));
		}

		// Test for line b;
		x1 = -5;
		y1 = 5;
		x2 = 10;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals("i : " + i, ResultB[i],
					GeometryUtil.getPointProjectionFactorOnLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2));
			assertEpsilonEquals(1 - ResultB[i],
					GeometryUtil.getPointProjectionFactorOnLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1));

		}

		// Test for line c;
		x1 = -5;
		y1 = 0;
		x2 = -5;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals("i : " + i, ResultC[i],
					GeometryUtil.getPointProjectionFactorOnLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2));
			assertEpsilonEquals(1 - ResultC[i],
					GeometryUtil.getPointProjectionFactorOnLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1));
		}
	}

	private void testPermutationIntersectionFactorLineLine(float x1, float y1,
			float x2, float y2, float x3, float y3, float x4, float y4,
			float result1, float result2) {

		assertEpsilonEquals(result1,
				GeometryUtil.getIntersectionFactorLineLine(x1, y1, x2, y2, x3,
						y3, x4, y4));
		assertEpsilonEquals(1 - result1,
				GeometryUtil.getIntersectionFactorLineLine(x2, y2, x1, y1, x3,
						y3, x4, y4));
		assertEpsilonEquals(result1,
				GeometryUtil.getIntersectionFactorLineLine(x1, y1, x2, y2, x4,
						y4, x3, y3));
		assertEpsilonEquals(1 - result1,
				GeometryUtil.getIntersectionFactorLineLine(x2, y2, x1, y1, x4,
						y4, x3, y3));

		assertEpsilonEquals(result2,
				GeometryUtil.getIntersectionFactorLineLine(x3, y3, x4, y4, x1,
						y1, x2, y2));
		assertEpsilonEquals(result2,
				GeometryUtil.getIntersectionFactorLineLine(x3, y3, x4, y4, x2,
						y2, x1, y1));
		assertEpsilonEquals(1 - result2,
				GeometryUtil.getIntersectionFactorLineLine(x4, y4, x3, y3, x1,
						y1, x2, y2));
		assertEpsilonEquals(1 - result2,
				GeometryUtil.getIntersectionFactorLineLine(x4, y4, x3, y3, x2,
						y2, x1, y1));

	}

	/**
	 */
	public void testGetIntersectionFactorLineLineFloatFloatFloatFloatFloatFloatFloatFloat() {

		// PQRS
		assertEpsilonEquals(Float.NaN,
				GeometryUtil.getIntersectionFactorLineLine(3, 1, 4, 1, 4, 0, 5,
						0));

		float result = Float.POSITIVE_INFINITY;
		// M2N2O2P2
		assertEpsilonEquals(result, GeometryUtil.getIntersectionFactorLineLine(
				13, 1, 13, 0, 13, -1, 13, -2));
		// EFGH
		assertEpsilonEquals(result, GeometryUtil.getIntersectionFactorLineLine(
				3, 5, 5, 5, 4, 5, 6, 5));
		// LMNO
		assertEpsilonEquals(result, GeometryUtil.getIntersectionFactorLineLine(
				3, 2, 6, 2, 4, 2, 5, 2));
		// B2C2D2E2
		assertEpsilonEquals(result, GeometryUtil.getIntersectionFactorLineLine(
				14, 5, 14, 3, 14, 4, 14, 2));
		// I2J2K2L2
		assertEpsilonEquals(result, GeometryUtil.getIntersectionFactorLineLine(
				12, 3, 12, 0, 12, 2, 12, 1));
		// R2S2R2T2
		assertEpsilonEquals(result, GeometryUtil.getIntersectionFactorLineLine(
				3, 6, 4, 6, 3, 6, 5, 6));
		// ABCD
		assertEpsilonEquals(result, GeometryUtil.getIntersectionFactorLineLine(
				3, 3, 4, 3, 5, 3, 6, 3));
		// IJJK
		assertEpsilonEquals(result, GeometryUtil.getIntersectionFactorLineLine(
				5, 4, 4, 4, 4, 4, 3, 4));
		// F2G2G2H2
		assertEpsilonEquals(result, GeometryUtil.getIntersectionFactorLineLine(
				13, 4, 13, 3, 13, 3, 13, 2));

		float x1, x2, x3, x4, y1, y2, y3, y4;

		x1 = 3;
		y1 = -2;
		x2 = 4;
		y2 = -1;
		x3 = 5;
		y3 = -1;
		x4 = 6;
		y4 = -1;

		// TUVW
		testPermutationIntersectionFactorLineLine(x1, y1, x2, y2, x3, y3, x4,
				y4, 1, -1);

		// D1E1F1G1
		testPermutationIntersectionFactorLineLine(7, 5, 8, 5, 7, 3, 8, 4, 2, 2);
		// K1L1M1N1
		testPermutationIntersectionFactorLineLine(8, -2, 9, -1, 8, -1, 10, -1,
				1, 0.5f);
		// O1P1P1Q1
		testPermutationIntersectionFactorLineLine(5, 1, 6, 1, 6, 1, 6, 0, 1, 0);
		// C1H1I1J1
		testPermutationIntersectionFactorLineLine(7, 0, 9, 0, 7, -1, 9, 1,
				0.5f, 0.5f);
	}

	public void testIsInsidePointEllipseFloatFloatFloatFloatFloatFloat() { //10^-6
		
		
		Point2f[] TestIn = {new Point2f(-0.9876117708, -1.1815573459), new Point2f(-0.8657034434, -1.5916961588), new Point2f(-0.6185501243, -1.9759750103), new Point2f(-0.2569535996, -2.3175990706), new Point2f(0.2032826274, -2.6016377289), new Point2f(0.7420440253, -2.815677133), new Point2f(1.3357841356, -2.9503627338), new Point2f(1.9585536664, -2.9998081243), new Point2f(2.5831346008, -2.9618523036), new Point2f(3.1822297551, -2.8381541232), new Point2f(3.729655796, -2.6341197872), new Point2f(4.2014875789, -2.3586665751), new Point2f(4.5771037907, -2.023833114), new Point2f(4.8400882005, -1.644253233), new Point2f(4.9789471276, -1.2365163942), new Point2f(4.9876117708, -0.8184426541), new Point2f(4.8657034434, -0.4083038412), new Point2f(4.6185501243, -0.0240249897), new Point2f(4.2569535996, 0.3175990706), new Point2f(3.7967173726, 0.6016377289), new Point2f(3.2579559747, 0.815677133), new Point2f(2.6642158644, 0.9503627338), new Point2f(2.0414463336, 0.9998081243), new Point2f(1.4168653992, 0.9618523036), new Point2f(0.8177702449, 0.8381541232), new Point2f(0.270344204, 0.6341197872), new Point2f(-0.2014875789, 0.3586665751), new Point2f(-0.5771037907, 0.023833114), new Point2f(-0.8400882005, -0.355746767), new Point2f(-0.9789471276, -0.7634836058)};
		Point2f[] TestOut = {new Point2f(-0.9965254145, -1.0962506915), new Point2f(-0.9010266787, -1.50948916), new Point2f(-0.6787391564, -1.9004605074), new Point2f(-0.3393778789, -2.2520774097), new Point2f(0.1022254374, -2.5489725211), new Point2f(0.6267706087, -2.7781701005), new Point2f(1.2113324939, -2.9296531139), new Point2f(1.8303629335, -2.9968010267), new Point2f(2.456807327, -2.976679153), new Point2f(3.0632870482, -2.8701669151), new Point2f(3.6232960232, -2.6819194089), new Point2f(4.1123591726, -2.4201639539), new Point2f(4.5091020899, -2.0963405194), new Point2f(4.7961852059, -1.7246017433), new Point2f(4.9610616108, -1.321194394), new Point2f(4.9965254145, -0.9037493085), new Point2f(4.9010266787, -0.49051084), new Point2f(4.6787391564, -0.0995394926), new Point2f(4.3393778789, 0.2520774097), new Point2f(3.8977745626, 0.5489725211), new Point2f(3.3732293913, 0.7781701005), new Point2f(2.7886675061, 0.9296531139), new Point2f(2.1696370665, 0.9968010267), new Point2f(1.543192673, 0.976679153), new Point2f(0.9367129518, 0.8701669151), new Point2f(0.3767039768, 0.6819194089), new Point2f(-0.1123591726, 0.4201639539), new Point2f(-0.5091020899, 0.0963405194), new Point2f(-0.7961852059, -0.2753982567), new Point2f(-0.9610616108, -0.678805606)};
		
		for(int i = 0 ; i < TestIn.length; i++){
			assertTrue("i " + i, GeometryUtil.isInsidePointEllipse(TestIn[i].getX(), TestIn[i].getY(), -1, -3, 6, 4));
		}
		
		for(int i = 0 ; i < TestOut.length; i++){
			assertFalse("i " + i , GeometryUtil.isInsidePointEllipse(TestOut[i].getX(), TestOut[i].getY(), -1, -3, 6, 4));
		}
		
	}

	public void testIsInsidePointCircleFloatFloatFloatFloatFloatFloat() {
		Point2f TestIn[] = { new Point2f(0.0033621225, 1.836055875),
				new Point2f(0.1247841494, 1.0086907087),
				new Point2f(0.4155717042, 0.2246505154),
				new Point2f(0.8630159756, -0.4817983864),
				new Point2f(1.4475615018, -1.0797807898),
				new Point2f(2.1436608383, -1.5431619942),
				new Point2f(2.9208911039, -1.8516900174),
				new Point2f(3.7452836064, -1.9918807045),
				new Point2f(4.5808084375, -1.9576070496),
				new Point2f(5.3909491529, -1.7503669759),
				new Point2f(6.1402987158, -1.3792178691),
				new Point2f(6.7961069546, -0.8603807262),
				new Point2f(7.3297119022, -0.2165312199),
				new Point2f(7.7177924619, 0.5241913369),
				new Point2f(7.9433876511, 1.3294138139),
				new Point2f(7.9966378775, 2.163944125),
				new Point2f(7.8752158506, 2.9913092913),
				new Point2f(7.5844282958, 3.7753494846),
				new Point2f(7.1369840244, 4.4817983864),
				new Point2f(6.5524384982, 5.0797807898),
				new Point2f(5.8563391617, 5.5431619942),
				new Point2f(5.0791088961, 5.8516900174),
				new Point2f(4.2547163936, 5.9918807045),
				new Point2f(3.4191915625, 5.9576070496),
				new Point2f(2.6090508471, 5.7503669759),
				new Point2f(1.8597012842, 5.3792178691),
				new Point2f(1.2038930454, 4.8603807262),
				new Point2f(0.6702880978, 4.2165312199),
				new Point2f(0.2822075381, 3.4758086631),
				new Point2f(0.0566123489, 2.6705861861) };
		Point2f TestOut[] = { new Point2f(0.0025653572, 1.8567370362),
				new Point2f(0.1197049401, 1.0287542802),
				new Point2f(0.4064320369, 0.2432196227),
				new Point2f(0.8502152974, -0.4655353024),
				new Point2f(1.4316592638, -1.0665345038),
				new Point2f(2.1253520447, -1.5335114325),
				new Point2f(2.9009759367, -1.8460569559),
				new Point2f(3.7246324541, -1.990511335),
				new Point2f(4.5603238545, -1.9605612201),
				new Point2f(5.3715264138, -1.757515575),
				new Point2f(6.1227866875, -1.3902484687),
				new Point2f(6.7812709966, -0.8748112361),
				new Point2f(7.3182004172, -0.2337309576),
				new Point2f(7.7101085571, 0.5049740823),
				new Point2f(7.9398671499, 1.3090189288),
				new Point2f(7.9974346428, 2.1432629638),
				new Point2f(7.8802950599, 2.9712457198),
				new Point2f(7.5935679631, 3.7567803773),
				new Point2f(7.1497847026, 4.4655353024),
				new Point2f(6.5683407362, 5.0665345038),
				new Point2f(5.8746479553, 5.5335114325),
				new Point2f(5.0990240633, 5.8460569559),
				new Point2f(4.2753675459, 5.990511335),
				new Point2f(3.4396761455, 5.9605612201),
				new Point2f(2.6284735862, 5.757515575),
				new Point2f(1.8772133125, 5.3902484687),
				new Point2f(1.2187290034, 4.8748112361),
				new Point2f(0.6817995828, 4.2337309576),
				new Point2f(0.2898914429, 3.4950259177),
				new Point2f(0.0601328501, 2.6909810712) };

		for (int i = 0; i < TestIn.length; i++) {
			assertTrue("Rectangle (" + TestIn[i].toString(), GeometryUtil.isInsidePointCircle(TestIn[i].getX(), TestIn[i].getY(), 4, 2, 4));
		}
		for (int i = 0; i < TestOut.length; i++) {
			assertFalse("Rectangle (" + TestIn[i].toString(), GeometryUtil.isInsidePointCircle(TestOut[i].getX(), TestOut[i].getY(), 4, 2, 4));
		}	
	}

	/**
	 * Also Test ShallowEllipse
	 */
	public void testClosestPointPointSolidEllipseFloatFloatFloatFloatFloatFloatFloatBoolean() {

		float ex, ey, ew, eh, px, py;

		Point2f pts = new Point2f();

		ex = 2.5f;
		ey = 1.17712f;
		ew = 4;
		eh = 2.64575131106f;

		// Test1 Point on ellipse bound
		px = 6.316f;
		py = 1.94575f;
		GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, pts,
				0.000000001f);
		assertEpsilonEquals(new Point2f(px, py), pts);

		// Test2 Point inside ellipse
		px = 5;
		py = 2;
		GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, pts,
				0.000000001f);
		assertEpsilonEquals(new Point2f(px, py), pts);

		// Test3
		px = 6.5f;
		py = 4.5f;
		GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, pts,
				0.000000001f);
		assertEpsilonEquals(new Point2f(5.86362, 3.46772), pts);

		// Test3_2 Every point on a given Normal has the same projection (We
		// also test precision here)
		px = 6.97520365979807f;
		py = 5.27083136139976f;
		GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, pts,
				0.0000000000000001f);
		assertEpsilonEquals(new Point2f(5.863619553435492, 3.4677225840349),
				pts);

		// Test3
		px = 6.5f;
		py = 4.5f;
		GeometryUtil.closestPointPointSolidEllipse(px, py, ex, ey, ew, eh, pts,
				0.000000001f);
		assertEpsilonEquals(new Point2f(5.86362, 3.46772), pts);

		Point2f[] testList = { new Point2f(6.5, 4.5),
				new Point2f(6.318077910688174, 4.666700881678807),
				new Point2f(6.122319150690555, 4.816911861358276),
				new Point2f(5.914213562373095, 4.949489742783178),
				new Point2f(5.695344954920479, 5.063425528223155),
				new Point2f(5.467379050591901, 5.157852097554699),
				new Point2f(5.232050807568878, 5.232050807568877),
				new Point2f(4.991151215875892, 5.285456961280076),
				new Point2f(4.746513666864877, 5.317664105611034),
				new Point2f(4.5, 5.32842712474619),
				new Point2f(4.253486333135123, 5.317664105611035),
				new Point2f(4.008848784124108, 5.285456961280076),
				new Point2f(3.767949192431123, 5.232050807568878),
				new Point2f(3.532620949408099, 5.157852097554699),
				new Point2f(3.304655045079521, 5.063425528223155),
				new Point2f(3.085786437626905, 4.949489742783178),
				new Point2f(2.877680849309445, 4.816911861358276),
				new Point2f(2.681922089311826, 4.666700881678808),
				new Point2f(2.5, 4.5),
				new Point2f(2.333299118321193, 4.318077910688175),
				new Point2f(2.183088138641724, 4.122319150690555),
				new Point2f(2.050510257216822, 3.914213562373095),
				new Point2f(1.936574471776846, 3.69534495492048),
				new Point2f(1.842147902445301, 3.467379050591902),
				new Point2f(1.767949192431123, 3.232050807568878),
				new Point2f(1.714543038719924, 2.991151215875893),
				new Point2f(1.682335894388965, 2.746513666864877),
				new Point2f(1.67157287525381, 2.5),
				new Point2f(1.682335894388965, 2.253486333135123),
				new Point2f(1.714543038719924, 2.00884878412411),
				new Point2f(1.767949192431123, 1.767949192431122),
				new Point2f(1.842147902445301, 1.532620949408099),
				new Point2f(1.936574471776845, 1.304655045079521),
				new Point2f(2.050510257216822, 1.085786437626906),
				new Point2f(2.183088138641723, 0.877680849309446),
				new Point2f(2.333299118321193, 0.681922089311825),
				new Point2f(2.5, 0.5),
				new Point2f(2.681922089311825, 0.333299118321193),
				new Point2f(2.877680849309444, 0.183088138641724),
				new Point2f(3.085786437626905, 0.050510257216822),
				new Point2f(3.30465504507952, -0.063425528223154),
				new Point2f(3.532620949408098, -0.157852097554699),
				new Point2f(3.767949192431122, -0.232050807568877),
				new Point2f(4.008848784124107, -0.285456961280076),
				new Point2f(4.008848784124107, -0.285456961280076),
				new Point2f(4.253486333135122, -0.317664105611035),
				new Point2f(4.5, -0.32842712474619),
				new Point2f(4.746513666864876, -0.317664105611035),
				new Point2f(4.99115121587589, -0.285456961280076),
				new Point2f(5.232050807568876, -0.232050807568878),
				new Point2f(5.4673790505919, -0.1578520975547),
				new Point2f(5.695344954920477, -0.063425528223155),
				new Point2f(5.914213562373096, 0.050510257216822),
				new Point2f(6.122319150690555, 0.183088138641723),
				new Point2f(6.318077910688174, 0.333299118321192),
				new Point2f(6.5, 0.5),
				new Point2f(6.666700881678807, 0.681922089311825),
				new Point2f(6.816911861358276, 0.877680849309444),
				new Point2f(6.949489742783177, 1.085786437626904),
				new Point2f(7.063425528223153, 1.304655045079519),
				new Point2f(7.157852097554699, 1.532620949408097),
				new Point2f(7.232050807568878, 1.767949192431123),
				new Point2f(7.285456961280076, 2.008848784124108),
				new Point2f(7.317664105611035, 2.253486333135122),
				new Point2f(7.32842712474619, 2.499999999999999),
				new Point2f(7.317664105611035, 2.746513666864876),
				new Point2f(7.285456961280076, 2.99115121587589),
				new Point2f(7.232050807568878, 3.232050807568876),
				new Point2f(7.1578520975547, 3.467379050591899),
				new Point2f(7.063425528223155, 3.695344954920477),
				new Point2f(6.949489742783178, 3.914213562373095),
				new Point2f(6.816911861358276, 4.122319150690555),
				new Point2f(6.666700881678808, 4.318077910688174),
				new Point2f(6.5, 4.5) };

		Point2f[] resultList = {
				new Point2f(5.863619553435486, 3.467722584034897),
				new Point2f(5.734164655271129, 3.540968996469049),
				new Point2f(5.596983026166917, 3.606130577794494),
				new Point2f(5.453019291956925, 3.663031588245388),
				new Point2f(5.303211950177797, 3.711506137562896),
				new Point2f(5.148504972259641, 3.751401442051113),
				new Point2f(4.98985418851975, 3.782582846201552),
				new Point2f(4.82822947860083, 3.804939123715212),
				new Point2f(4.664613801482725, 3.818387179343805),
				new Point2f(4.5, 3.822875655532295),
				new Point2f(4.335386198517282, 3.818387179343806),
				new Point2f(4.17177052139917, 3.804939123715212),
				new Point2f(4.010145811480251, 3.782582846201552),
				new Point2f(3.85149502774036, 3.751401442051113),
				new Point2f(3.696788049822203, 3.711506137562896),
				new Point2f(3.546980708043075, 3.663031588245388),
				new Point2f(3.403016973833083, 3.606130577794494),
				new Point2f(3.265835344728871, 3.540968996469049),
				new Point2f(3.136380446564514, 3.467722584034897),
				new Point2f(3.015620563879194, 3.386577824677272),
				new Point2f(2.904570849910018, 3.29774059024935),
				new Point2f(2.804319803455639, 3.20145744246047),
				new Point2f(2.716052548076207, 3.098055203420963),
				new Point2f(2.641058209512213, 2.988002813583489),
				new Point2f(2.580701727169776, 2.871992857741957),
				new Point2f(2.536338166506605, 2.751026072571346),
				new Point2f(2.509159821625298, 2.626463356645044),
				new Point2f(2.5, 2.5),
				new Point2f(2.509159821625298, 2.373536643354956),
				new Point2f(2.536338166506605, 2.248973927428656),
				new Point2f(2.580701727169776, 2.128007142258043),
				new Point2f(2.641058209512213, 2.011997186416511),
				new Point2f(2.716052548076206, 1.901944796579038),
				new Point2f(2.804319803455638, 1.798542557539531),
				new Point2f(2.904570849910018, 1.70225940975065),
				new Point2f(3.015620563879194, 1.613422175322728),
				new Point2f(3.136380446564514, 1.532277415965103),
				new Point2f(3.265835344728872, 1.45903100353095),
				new Point2f(3.403016973833083, 1.393869422205506),
				new Point2f(3.546980708043078, 1.336968411754611),
				new Point2f(3.696788049822204, 1.288493862437104),
				new Point2f(3.851495027740357, 1.248598557948888),
				new Point2f(4.010145811480251, 1.217417153798448),
				new Point2f(4.171770521399165, 1.195060876284789),
				new Point2f(4.171770521399165, 1.195060876284789),
				new Point2f(4.335386198517276, 1.181612820656195),
				new Point2f(4.5, 1.177124344467705),
				new Point2f(4.664613801482719, 1.181612820656194),
				new Point2f(4.82822947860083, 1.195060876284788),
				new Point2f(4.98985418851975, 1.217417153798448),
				new Point2f(5.148504972259642, 1.248598557948888),
				new Point2f(5.303211950177795, 1.288493862437104),
				new Point2f(5.453019291956925, 1.336968411754612),
				new Point2f(5.596983026166916, 1.393869422205506),
				new Point2f(5.734164655271129, 1.459031003530951),
				new Point2f(5.863619553435486, 1.532277415965103),
				new Point2f(5.984379436120806, 1.613422175322728),
				new Point2f(6.095429150089982, 1.702259409750649),
				new Point2f(6.195680196544361, 1.79854255753953),
				new Point2f(6.283947451923793, 1.901944796579036),
				new Point2f(6.358941790487787, 2.011997186416511),
				new Point2f(6.419298272830224, 2.128007142258043),
				new Point2f(6.463661833493395, 2.248973927428656),
				new Point2f(6.490840178374701, 2.373536643354955),
				new Point2f(6.5, 2.5),
				new Point2f(6.490840178374702, 2.626463356645044),
				new Point2f(6.463661833493395, 2.751026072571344),
				new Point2f(6.419298272830224, 2.871992857741956),
				new Point2f(6.358941790487788, 2.988002813583488),
				new Point2f(6.283947451923795, 3.098055203420961),
				new Point2f(6.195680196544362, 3.20145744246047),
				new Point2f(6.095429150089982, 3.29774059024935),
				new Point2f(5.984379436120807, 3.386577824677272),
				new Point2f(5.863619553435486, 3.467722584034897) };

		for (int i = 0; i < 73; i++) {
			GeometryUtil.closestPointPointSolidEllipse(testList[i].getX(),
					testList[i].getY(), ex, ey, ew, eh, pts, 0.000000001f);

			assertEpsilonEquals("i :" + i, resultList[i], pts);
		}
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
	public void testIsEqualsLinesFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
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
	public void testClipSegmentToAlignedRectangle2DPoint2Dfloatfloatfloatfloat() {

		float rminx, rminy, extentx, extenty;
		Point2f point1, point2;

		rminx = rminy = 1.f;
		extentx = 2.f;
		extenty = 4.f;

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(3f, 0f);

		GeometryUtil.clipSegmentToRectangle(point1, point2, rminx, rminy,
				extentx, extenty);
		assertEpsilonEquals(new Point2f(1, 2), point1);
		assertEpsilonEquals(new Point2f(2, 1), point2);

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(0.5f, 0f);

		GeometryUtil.clipSegmentToRectangle(point1, point2, rminx, rminy,
				extentx, extenty);
		assertEquals(point1, new Point2f(0f, 3f));
		assertEquals(point2, new Point2f(0.5f, 0f));

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(6f, 0f);

		GeometryUtil.clipSegmentToRectangle(point1, point2, rminx, rminy,
				extentx, extenty);
		assertEpsilonEquals(new Point2f(1, 2.5f), point1);
		assertEpsilonEquals(new Point2f(3, 1.5), point2);

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(4f, 3f);

		GeometryUtil.clipSegmentToRectangle(point1, point2, rminx, rminy,
				extentx, extenty);
		assertEpsilonEquals(new Point2f(1, 3), point1);
		assertEpsilonEquals(new Point2f(3, 3), point2);

		point1 = new Point2f(0f, 3f);
		point2 = new Point2f(5f, 6f);

		GeometryUtil.clipSegmentToRectangle(point1, point2, rminx, rminy,
				extentx, extenty);
		assertEpsilonEquals(new Point2f(1f, 3.6f), point1);
		assertEpsilonEquals(new Point2f(3, 4.8), point2);

	}

	public void testClipSegmentToOBRPoint2DPoint2DFloatFloatFLoatFloatFloatFloatFLoatFloat() {
		assert(false);
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
		 * (Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay) r = -------------------------------
		 * L^2
		 */
		float L = (float) Math.sqrt(Math.pow(p3.getX() - p2.getX(), 2.f)
				+ Math.pow(p3.getY() - p2.getY(), 2.f));
		expected = ((p1.getX() - p2.getX()) * (p3.getX() - p2.getX()) + (p1
				.getY() - p2.getY()) * (p3.getY() - p2.getY()))
				/ (L * L);

		float actual = GeometryUtil.getPointProjectionFactorOnSegment(
				p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(),
				p3.getY());

		assertEpsilonEquals(expected, actual);

		p1 = new Point2f(-100.f, -100.f);
		p2 = new Point2f(100.f, 100.f);

		p3 = new Point2f(p1);
		assertEpsilonEquals(
				0.f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(
				1.f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(p2);
		assertEquals(
				1.f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(
				-0.f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(0.f, 0.f);
		assertEquals(
				.5f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(
				.5f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(-100.f, 100.f);
		assertEquals(
				.5f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(
				.5f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(100.f, -100.f);
		assertEquals(
				.5f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(
				.5f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(-300, -300.f);
		assertEquals(
				-1.f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(
				2.f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));

		p3 = new Point2f(300, 300.f);
		assertEquals(
				2.f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY()));
		assertEquals(
				-1.f,
				GeometryUtil.getPointProjectionFactorOnSegment(p3.getX(),
						p3.getY(), p2.getX(), p2.getY(), p1.getX(), p1.getY()));
	}

	public void testGetPointProjectionFactorOnSegmentFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		assert(false);
	}

	public void testClosestFarthestPointsOBRPointFloatFloatFloatFloatFloatFloatFloatPoint2DPoint2D() {

		Point2f farthest[] = { new Point2f(3, 1), new Point2f(-2, 1),
				new Point2f(2, 3), new Point2f(2, 3), new Point2f(-2, 1),
				new Point2f(3, 1), new Point2f(-1, -1), new Point2f(3, 1),
				new Point2f(3, 1), new Point2f(-1, -1), new Point2f(3, 1),
				new Point2f(3, 1), new Point2f(2, 3), new Point2f(2, 3),
				new Point2f(2, 3), new Point2f(-2, 1), new Point2f(-2, 1),
				new Point2f(-1, -1), new Point2f(-1, -1), new Point2f(-2, 1),
				new Point2f(-2, 1), new Point2f(-1, -1), new Point2f(-1, -1),
				new Point2f(-2, 1), new Point2f(3, 1), new Point2f(3, 1),
				new Point2f(3, 1), new Point2f(-1, -1), new Point2f(-1, -1) };
		Point2f nearest[] = { new Point2f(-2, 1), new Point2f(1, 0),
				new Point2f(-1, -1), new Point2f(0, -0.5), new Point2f(3, 1),
				new Point2f(-1.8, 0.6), new Point2f(2, 3),
				new Point2f(-1, 1.5), new Point2f(-2, 1),
				new Point2f(2.2, 2.6), new Point2f(-2, 1),
				new Point2f(-1.6, 0.2), new Point2f(-1, -1),
				new Point2f(-1, -1), new Point2f(0.2, -0.4), new Point2f(3, 1),
				new Point2f(3, 1), new Point2f(2, 3), new Point2f(2, 3),
				new Point2f(2.6, 1.8), new Point2f(3, 1), new Point2f(2, 3),
				new Point2f(0, 2), new Point2f(1, 0), new Point2f(-1.2, 1.4),
				new Point2f(0, 1), new Point2f(-1.5, 0), new Point2f(0, 2),
				new Point2f(2.5, 2) };
		Point2f test[] = { new Point2f(-2, 1), new Point2f(1, 0),
				new Point2f(-1, -1), new Point2f(0, -0.5), new Point2f(3, 1),
				new Point2f(-1.8, 0.6), new Point2f(2, 3),
				new Point2f(-1, 1.5), new Point2f(-5, 2),
				new Point2f(2.2, 2.6), new Point2f(-4, 0), new Point2f(-4, -1),
				new Point2f(-3, -2), new Point2f(0, -3), new Point2f(1, -2),
				new Point2f(4, -1), new Point2f(5, 2), new Point2f(4, 4),
				new Point2f(1, 5), new Point2f(5, 3), new Point2f(5, 0),
				new Point2f(3, 5), new Point2f(-1, 4), new Point2f(2, -2),
				new Point2f(-2, 3), new Point2f(0, 1), new Point2f(-1.5, 0),
				new Point2f(0, 2), new Point2f(2.5, 2) };

		Point2f q1, q2;

		q1 = new Point2f();
		q2 = new Point2f();

		for (int i = 0; i < test.length; i++) {
			GeometryUtil.closestFarthestPointsPointOBR(test[i].getX(),
					test[i].getY(), 0.5f, 1f, 0.894427191f, 0.4472135955f,
					-0.4472135955f, 0.894427191f, 4.472135955f / 2,
					2.2360679775f / 2, q1, q2);
			assertEpsilonEquals("P" + test[i].toString(), nearest[i], q1);
			assertEpsilonEquals("P" + test[i].toString(), farthest[i], q2);
		}
	}

	/**
	 */
	public void testClosestFarthestPointsOBBPointFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatPoint3DPoint3D() {

		Point3f q1P1, q2P1, q1P2, q2P2, q1P3, q2P3, q1P4, q2P4, q1P5, q2P5, q1P6, q2P6, q1P7, q2P7;

		q1P1 = new Point3f();
		q2P1 = new Point3f();
		q1P2 = new Point3f();
		q2P2 = new Point3f();
		q1P3 = new Point3f();
		q2P3 = new Point3f();
		q1P4 = new Point3f();
		q2P4 = new Point3f();
		q1P5 = new Point3f();
		q2P5 = new Point3f();
		q1P6 = new Point3f();
		q2P6 = new Point3f();
		q1P7 = new Point3f();
		q2P7 = new Point3f();

		Point3f center = new Point3f(0.6362632533, 3.7575835969, 2.4721785537);
		Vector3f extents = new Vector3f(0.5, 1, 1.5);

		Vector3f axe1 = new Vector3f(1, 1, 1);
		Vector3f axe2 = new Vector3f(1, 1, -2);
		Vector3f axe3 = new Vector3f(-1, 1, 0);

		axe1.normalize();
		axe2.normalize();
		axe3.normalize();

		// Giving a cuboïd
		// E +---------+ F
		// /| /|
		// / | / |
		// / | / |
		// / H +-----/---+ G
		// A +---------+ B /
		// | / | /
		// | / | /
		// |/ | /
		// +---------+
		// D C
		// axe1 = vect AB (normalize) * 2*extentx
		// axe2 = vect AD (normalize) * 2*extenty
		// axe3 = vect AE (normalize) * 2*extentz
		//
		// Let's name the closest point of A c(A) and the farthest one f(A)
		// Let's test all pathologics points (Like p = A,B,..G or p is on
		// (AB),(AE)...
		Vector3f AB = new Vector3f(axe1);
		AB.scale(2 * extents.getX());
		Vector3f AD = new Vector3f(axe2);
		AD.scale(2 * extents.getY());
		Vector3f AE = new Vector3f(axe3);
		AE.scale(2 * extents.getZ());
		Point3f A = new Point3f(1, 2, 3);
		Point3f B = new Point3f(A);
		B.add(AB);
		Point3f C = new Point3f(B);
		C.add(AD);
		Point3f D = new Point3f(A);
		D.add(AD);
		Point3f E = new Point3f(A);
		E.add(AE);
		Point3f F = new Point3f(B);
		F.add(AE);
		Point3f G = new Point3f(C);
		G.add(AE);
		Point3f H = new Point3f(D);
		H.add(AE);

		Point3f P1 = new Point3f();
		Point3f P2 = new Point3f();
		Point3f P3 = new Point3f();
		Point3f P4 = new Point3f();
		Point3f P5 = new Point3f();
		Point3f P6 = new Point3f();
		Point3f P7 = new Point3f();

		Point3f PTest1, PTest2, PTest3, PTest4, PTest5, PTest6, PTest7;
		PTest1 = new Point3f();
		PTest2 = new Point3f();
		PTest3 = new Point3f();
		PTest4 = new Point3f();
		PTest5 = new Point3f();
		PTest6 = new Point3f();
		PTest7 = new Point3f();

		P1.scaleAdd(-1, AB, A);
		P2.scaleAdd(-1, AB, D);
		P3.scaleAdd(-1, AB, H);
		P4.scaleAdd(-1, AB, E);
		P5.scaleAdd(-1.5f, AB, center);
		P6.scaleAdd(-extents.getZ(), axe3, A);
		P6.scaleAdd(extents.getY(), axe2, P6);
		P6.scaleAdd(-1, AB, P6);
		P7.scaleAdd(extents.getZ(), axe3, E);
		P7.scaleAdd(extents.getY(), axe2, P7);
		P7.scaleAdd(-1, AB, P7);

		AB.scale(0.5f);

		for (int i = -3; i <= 3; i++) {

			GeometryUtil.closestFarthestPointsPointOBB(P1.getX(), P1.getY(),
					P1.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P1, q2P1);
			GeometryUtil.closestFarthestPointsPointOBB(P2.getX(), P2.getY(),
					P2.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P2, q2P2);
			GeometryUtil.closestFarthestPointsPointOBB(P3.getX(), P3.getY(),
					P3.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P3, q2P3);
			GeometryUtil.closestFarthestPointsPointOBB(P4.getX(), P4.getY(),
					P4.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P4, q2P4);
			GeometryUtil.closestFarthestPointsPointOBB(P5.getX(), P5.getY(),
					P5.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P5, q2P5);
			GeometryUtil.closestFarthestPointsPointOBB(P6.getX(), P6.getY(),
					P6.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P6, q2P6);
			GeometryUtil.closestFarthestPointsPointOBB(P7.getX(), P7.getY(),
					P7.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P7, q2P7);

			// Test closest point
			PTest1.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AB, A);
			PTest2.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AB, D);
			PTest3.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AB, H);
			PTest4.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AB, E);
			PTest5.scaleAdd(MathUtil.clamp(i, -1, 1), AB, center);
			PTest6.scaleAdd(0.5f, AD, A);
			PTest6.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AB, PTest6);
			PTest7.scaleAdd(0.5f, AD, E);
			PTest7.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AB, PTest7);

			assertEpsilonEquals(PTest1, q1P1);
			assertEpsilonEquals(PTest2, q1P2);
			assertEpsilonEquals(PTest3, q1P3);
			assertEpsilonEquals(PTest4, q1P4);
			assertEpsilonEquals(PTest5, q1P5);
			assertEpsilonEquals(PTest6, q1P6);
			assertEpsilonEquals(PTest7, q1P7);

			// The following code is necessary to handle the little
			// indeterminacy due to the approximation of the computing of
			// d1,d2,d3 in closestFarthestPointsPointOBB.
			// We have to recompute d1,d2,d3 when the approximation can make
			// them positive or negative
			// (when points are exactly on the median plane splitting the OBB in
			// to equal part)
			// A pathological point is the OBB's center. Actually we may have d1
			// = d2 = d3 = 0 but approximation can make on of us slightly
			// positive or negative.

			float dx1, dy1, dz1, dx2, dy2, dz2, dx3, dy3, dz3, dx4, dy4, dz4, dx5, dy5, dz5, dx6, dy6, dz6, dx7, dy7, dz7;

			dx1 = P1.getX() - center.getX();
			dy1 = P1.getY() - center.getY();
			dz1 = P1.getZ() - center.getZ();
			dx2 = P2.getX() - center.getX();
			dy2 = P2.getY() - center.getY();
			dz2 = P2.getZ() - center.getZ();
			dx3 = P3.getX() - center.getX();
			dy3 = P3.getY() - center.getY();
			dz3 = P3.getZ() - center.getZ();
			dx4 = P4.getX() - center.getX();
			dy4 = P4.getY() - center.getY();
			dz4 = P4.getZ() - center.getZ();
			dx5 = P5.getX() - center.getX();
			dy5 = P5.getY() - center.getY();
			dz5 = P5.getZ() - center.getZ();
			dx6 = P6.getX() - center.getX();
			dy6 = P6.getY() - center.getY();
			dz6 = P6.getZ() - center.getZ();
			dx7 = P7.getX() - center.getX();
			dy7 = P7.getY() - center.getY();
			dz7 = P7.getZ() - center.getZ();

			float d11 = MathUtil.dotProduct(dx1, dy1, dz1, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d12 = MathUtil.dotProduct(dx2, dy2, dz2, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d13 = MathUtil.dotProduct(dx3, dy3, dz3, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d14 = MathUtil.dotProduct(dx4, dy4, dz4, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d15 = MathUtil.dotProduct(dx5, dy5, dz5, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d25 = MathUtil.dotProduct(dx5, dy5, dz5, axe2.getX(),
					axe2.getY(), axe2.getZ());
			float d35 = MathUtil.dotProduct(dx5, dy5, dz5, axe3.getX(),
					axe3.getY(), axe3.getZ());
			float d16 = MathUtil.dotProduct(dx6, dy6, dz6, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d26 = MathUtil.dotProduct(dx6, dy6, dz6, axe2.getX(),
					axe2.getY(), axe2.getZ());
			float d17 = MathUtil.dotProduct(dx7, dy7, dz7, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d27 = MathUtil.dotProduct(dx7, dy7, dz7, axe2.getX(),
					axe2.getY(), axe2.getZ());

			// Test farthest point
			if (i < 0) {
				PTest1 = new Point3f(G);
				PTest2 = new Point3f(F);
				PTest3 = new Point3f(B);
				PTest4 = new Point3f(C);
				if (d25 >= 0) {
					if (d35 >= 0)
						PTest5 = new Point3f(B);
					else
						PTest5 = new Point3f(F);
				} else {
					if (d35 >= 0)
						PTest5 = new Point3f(C);
					else
						PTest5 = new Point3f(G);
				}
				PTest6 = (d26 >= 0) ? new Point3f(F) : new Point3f(G);
				PTest7 = (d27 >= 0) ? new Point3f(B) : new Point3f(C);

			} else if (i == 0) {

				PTest1 = (d11 >= 0) ? new Point3f(H) : new Point3f(G);
				PTest2 = (d12 >= 0) ? new Point3f(E) : new Point3f(F);
				PTest3 = (d13 >= 0) ? new Point3f(A) : new Point3f(B);
				PTest4 = (d14 >= 0) ? new Point3f(D) : new Point3f(C);

				if (d15 >= 0) {
					if (d25 >= 0) {
						if (d35 >= 0)
							PTest5 = new Point3f(A);
						else
							PTest5 = new Point3f(E);
					} else {
						if (d35 >= 0)
							PTest5 = new Point3f(D);
						else
							PTest5 = new Point3f(H);
					}
				} else {
					if (d25 >= 0) {
						if (d35 >= 0)
							PTest5 = new Point3f(B);
						else
							PTest5 = new Point3f(F);
					} else {
						if (d35 >= 0)
							PTest5 = new Point3f(C);
						else
							PTest5 = new Point3f(G);
					}
				}

				if (d16 >= 0) {
					if (d26 >= 0)
						PTest6 = new Point3f(E);
					else
						PTest6 = new Point3f(H);
				} else {
					if (d26 >= 0)
						PTest6 = new Point3f(F);
					else
						PTest6 = new Point3f(G);
				}

				if (d17 >= 0) {
					if (d27 >= 0)
						PTest7 = new Point3f(A);
					else
						PTest7 = new Point3f(D);
				} else {
					if (d27 >= 0)
						PTest7 = new Point3f(B);
					else
						PTest7 = new Point3f(C);
				}
			} else {
				PTest1 = new Point3f(H);
				PTest2 = new Point3f(E);
				PTest3 = new Point3f(A);
				PTest4 = new Point3f(D);
				if (d25 >= 0) {
					if (d35 >= 0)
						PTest5 = new Point3f(A);
					else
						PTest5 = new Point3f(E);
				} else {
					if (d35 >= 0)
						PTest5 = new Point3f(D);
					else
						PTest5 = new Point3f(H);
				}
				PTest6 = (d26 >= 0) ? new Point3f(E) : new Point3f(H);
				PTest7 = (d27 >= 0) ? new Point3f(A) : new Point3f(D);
			}

			assertEpsilonEquals(PTest1, q2P1);
			assertEpsilonEquals(PTest2, q2P2);
			assertEpsilonEquals(PTest3, q2P3);
			assertEpsilonEquals(PTest4, q2P4);
			assertEpsilonEquals(PTest5, q2P5);
			assertEpsilonEquals(PTest6, q2P6);
			assertEpsilonEquals(PTest7, q2P7);

			P1.add(AB);
			P2.add(AB);
			P3.add(AB);
			P4.add(AB);
			P5.add(AB);
			P6.add(AB);
			P7.add(AB);
		}

		P6.scaleAdd(-extents.getZ(), axe3, A);
		P6.scaleAdd(extents.getX(), axe1, P6);
		P7.scaleAdd(extents.getZ(), axe3, E);
		P7.scaleAdd(extents.getX(), axe1, P7);

		P1.scaleAdd(-1, AD, A);
		P2.scaleAdd(-1, AD, B);
		P3.scaleAdd(-1, AD, F);
		P4.scaleAdd(-1, AD, E);
		P5.scaleAdd(-1.5f, AD, center);
		P6.scaleAdd(-1, AD, P6);
		P7.scaleAdd(-1, AD, P7);

		AD.scale(0.5f);

		for (int i = -3; i <= 3; i++) {

			GeometryUtil.closestFarthestPointsPointOBB(P1.getX(), P1.getY(),
					P1.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P1, q2P1);
			GeometryUtil.closestFarthestPointsPointOBB(P2.getX(), P2.getY(),
					P2.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P2, q2P2);
			GeometryUtil.closestFarthestPointsPointOBB(P3.getX(), P3.getY(),
					P3.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P3, q2P3);
			GeometryUtil.closestFarthestPointsPointOBB(P4.getX(), P4.getY(),
					P4.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P4, q2P4);
			GeometryUtil.closestFarthestPointsPointOBB(P5.getX(), P5.getY(),
					P5.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P5, q2P5);
			GeometryUtil.closestFarthestPointsPointOBB(P6.getX(), P6.getY(),
					P6.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P6, q2P6);
			GeometryUtil.closestFarthestPointsPointOBB(P7.getX(), P7.getY(),
					P7.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P7, q2P7);

			// Test closest point
			PTest1.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AD, A);
			PTest2.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AD, B);
			PTest3.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AD, F);
			PTest4.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AD, E);
			PTest5.scaleAdd(MathUtil.clamp(i, -1, 1), AD, center);
			PTest6.scaleAdd(1, AB, A);
			PTest6.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AD, PTest6);
			PTest7.scaleAdd(1, AB, E);
			PTest7.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AD, PTest7);

			assertEpsilonEquals(PTest1, q1P1);
			assertEpsilonEquals(PTest2, q1P2);
			assertEpsilonEquals(PTest3, q1P3);
			assertEpsilonEquals(PTest4, q1P4);
			assertEpsilonEquals(PTest5, q1P5);
			assertEpsilonEquals(PTest6, q1P6);
			assertEpsilonEquals(PTest7, q1P7);

			// The following code is necessary to handle the little
			// indeterminacy due to the approximation of the computing of
			// d1,d2,d3 in closestFarthestPointsPointOBB.
			// We have to recompute d1,d2,d3 when the approximation can make
			// them positive or negative
			// (when points are exactly on the median plane splitting the OBB in
			// to equal part)
			// A pathological point is the OBB's center. Actually we may have d1
			// = d2 = d3 = 0 but approximation can make on of us slightly
			// positive or negative.

			float dx1, dy1, dz1, dx2, dy2, dz2, dx3, dy3, dz3, dx4, dy4, dz4, dx5, dy5, dz5, dx6, dy6, dz6, dx7, dy7, dz7;

			dx1 = P1.getX() - center.getX();
			dy1 = P1.getY() - center.getY();
			dz1 = P1.getZ() - center.getZ();
			dx2 = P2.getX() - center.getX();
			dy2 = P2.getY() - center.getY();
			dz2 = P2.getZ() - center.getZ();
			dx3 = P3.getX() - center.getX();
			dy3 = P3.getY() - center.getY();
			dz3 = P3.getZ() - center.getZ();
			dx4 = P4.getX() - center.getX();
			dy4 = P4.getY() - center.getY();
			dz4 = P4.getZ() - center.getZ();
			dx5 = P5.getX() - center.getX();
			dy5 = P5.getY() - center.getY();
			dz5 = P5.getZ() - center.getZ();
			dx6 = P6.getX() - center.getX();
			dy6 = P6.getY() - center.getY();
			dz6 = P6.getZ() - center.getZ();
			dx7 = P7.getX() - center.getX();
			dy7 = P7.getY() - center.getY();
			dz7 = P7.getZ() - center.getZ();

			float d21 = MathUtil.dotProduct(dx1, dy1, dz1, axe2.getX(),
					axe2.getY(), axe2.getZ());
			float d22 = MathUtil.dotProduct(dx2, dy2, dz2, axe2.getX(),
					axe2.getY(), axe2.getZ());
			float d23 = MathUtil.dotProduct(dx3, dy3, dz3, axe2.getX(),
					axe2.getY(), axe2.getZ());
			float d24 = MathUtil.dotProduct(dx4, dy4, dz4, axe2.getX(),
					axe2.getY(), axe2.getZ());
			float d15 = MathUtil.dotProduct(dx5, dy5, dz5, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d25 = MathUtil.dotProduct(dx5, dy5, dz5, axe2.getX(),
					axe2.getY(), axe2.getZ());
			float d35 = MathUtil.dotProduct(dx5, dy5, dz5, axe3.getX(),
					axe3.getY(), axe3.getZ());
			float d16 = MathUtil.dotProduct(dx6, dy6, dz6, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d26 = MathUtil.dotProduct(dx6, dy6, dz6, axe2.getX(),
					axe2.getY(), axe2.getZ());
			float d17 = MathUtil.dotProduct(dx7, dy7, dz7, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d27 = MathUtil.dotProduct(dx7, dy7, dz7, axe2.getX(),
					axe2.getY(), axe2.getZ());

			// Test farthest point
			if (i < 0) {
				PTest1 = new Point3f(G);
				PTest2 = new Point3f(H);
				PTest3 = new Point3f(D);
				PTest4 = new Point3f(C);
				if (d15 >= 0) {
					if (d35 >= 0)
						PTest5 = new Point3f(D);
					else
						PTest5 = new Point3f(H);
				} else {
					if (d35 >= 0)
						PTest5 = new Point3f(C);
					else
						PTest5 = new Point3f(G);
				}
				PTest6 = (d16 >= 0) ? new Point3f(H) : new Point3f(G);
				PTest7 = (d17 >= 0) ? new Point3f(D) : new Point3f(C);

			} else if (i == 0) {

				PTest1 = (d21 >= 0) ? new Point3f(F) : new Point3f(G);
				PTest2 = (d22 >= 0) ? new Point3f(E) : new Point3f(H);
				PTest3 = (d23 >= 0) ? new Point3f(A) : new Point3f(D);
				PTest4 = (d24 >= 0) ? new Point3f(B) : new Point3f(C);

				if (d15 >= 0) {
					if (d25 >= 0) {
						if (d35 >= 0)
							PTest5 = new Point3f(A);
						else
							PTest5 = new Point3f(E);
					} else {
						if (d35 >= 0)
							PTest5 = new Point3f(D);
						else
							PTest5 = new Point3f(H);
					}
				} else {
					if (d25 >= 0) {
						if (d35 >= 0)
							PTest5 = new Point3f(B);
						else
							PTest5 = new Point3f(F);
					} else {
						if (d35 >= 0)
							PTest5 = new Point3f(C);
						else
							PTest5 = new Point3f(G);
					}
				}

				if (d16 >= 0) {
					if (d26 >= 0)
						PTest6 = new Point3f(E);
					else
						PTest6 = new Point3f(H);
				} else {
					if (d26 >= 0)
						PTest6 = new Point3f(F);
					else
						PTest6 = new Point3f(G);
				}

				if (d17 >= 0) {
					if (d27 >= 0)
						PTest7 = new Point3f(A);
					else
						PTest7 = new Point3f(D);
				} else {
					if (d27 >= 0)
						PTest7 = new Point3f(B);
					else
						PTest7 = new Point3f(C);
				}

			} else {
				PTest1 = new Point3f(F);
				PTest2 = new Point3f(E);
				PTest3 = new Point3f(A);
				PTest4 = new Point3f(B);
				if (d15 >= 0) {
					if (d35 >= 0)
						PTest5 = new Point3f(A);
					else
						PTest5 = new Point3f(E);
				} else {
					if (d35 >= 0)
						PTest5 = new Point3f(B);
					else
						PTest5 = new Point3f(F);
				}
				PTest6 = (d16 >= 0) ? new Point3f(E) : new Point3f(F);
				PTest7 = (d17 >= 0) ? new Point3f(A) : new Point3f(B);
			}

			assertEpsilonEquals(PTest1, q2P1);
			assertEpsilonEquals(PTest2, q2P2);
			assertEpsilonEquals(PTest3, q2P3);
			assertEpsilonEquals(PTest4, q2P4);
			assertEpsilonEquals(PTest5, q2P5);
			assertEpsilonEquals(PTest6, q2P6);
			assertEpsilonEquals(PTest7, q2P7);

			P1.add(AD);
			P2.add(AD);
			P3.add(AD);
			P4.add(AD);
			P5.add(AD);
			P6.add(AD);
			P7.add(AD);
		}

		P6 = new Point3f(A);
		P6.sub(AB);
		P6.add(AD);
		P7 = new Point3f(B);
		P7.add(AB);
		P7.add(AD);

		P6.scaleAdd(-extents.getX(), axe1, C);
		P6.scaleAdd(extents.getY(), axe2, P6);
		P7.scaleAdd(-extents.getX(), axe1, B);
		P7.scaleAdd(-extents.getY(), axe2, P7);

		P1.scaleAdd(-1, AE, A);
		P2.scaleAdd(-1, AE, B);
		P3.scaleAdd(-1, AE, C);
		P4.scaleAdd(-1, AE, D);
		P5.scaleAdd(-1.5f, AE, center);
		P6.scaleAdd(-1, AE, P6);
		P7.scaleAdd(-1, AE, P7);

		AE.scale(0.5f);

		for (int i = -3; i <= 3; i++) {
			GeometryUtil.closestFarthestPointsPointOBB(P1.getX(), P1.getY(),
					P1.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P1, q2P1);
			GeometryUtil.closestFarthestPointsPointOBB(P2.getX(), P2.getY(),
					P2.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P2, q2P2);
			GeometryUtil.closestFarthestPointsPointOBB(P3.getX(), P3.getY(),
					P3.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P3, q2P3);
			GeometryUtil.closestFarthestPointsPointOBB(P4.getX(), P4.getY(),
					P4.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P4, q2P4);
			GeometryUtil.closestFarthestPointsPointOBB(P5.getX(), P5.getY(),
					P5.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P5, q2P5);
			GeometryUtil.closestFarthestPointsPointOBB(P6.getX(), P6.getY(),
					P6.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P6, q2P6);
			GeometryUtil.closestFarthestPointsPointOBB(P7.getX(), P7.getY(),
					P7.getZ(), center.getX(), center.getY(), center.getZ(),
					axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
					axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
					axe3.getZ(), extents.getX(), extents.getY(),
					extents.getZ(), q1P7, q2P7);

			// Test closest point
			PTest1.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AE, A);
			PTest2.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AE, B);
			PTest3.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AE, C);
			PTest4.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AE, D);
			PTest5.scaleAdd(MathUtil.clamp(i, -1, 1), AE, center);
			PTest6.scaleAdd(1, AB, D);
			PTest6.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AE, PTest6);
			PTest7.scaleAdd(1, AB, A);
			PTest7.scaleAdd(1 + MathUtil.clamp(i, -1, 1), AE, PTest7);

			assertEpsilonEquals(PTest1, q1P1);
			assertEpsilonEquals(PTest2, q1P2);
			assertEpsilonEquals(PTest3, q1P3);
			assertEpsilonEquals(PTest4, q1P4);
			assertEpsilonEquals(PTest5, q1P5);
			assertEpsilonEquals(PTest6, q1P6);
			assertEpsilonEquals(PTest7, q1P7);

			// The following code is necessary to handle the little
			// indeterminacy due to the approximation of the computing of
			// d1,d2,d3 in closestFarthestPointsPointOBB.
			// We have to recompute d1,d2,d3 when the approximation can make
			// them positive or negative
			// (when points are exactly on the median plane splitting the OBB in
			// to equal part)
			// A pathological point is the OBB's center. Actually we may have d1
			// = d2 = d3 = 0 but approximation can make on of us slightly
			// positive or negative.

			float dx1, dy1, dz1, dx2, dy2, dz2, dx3, dy3, dz3, dx4, dy4, dz4, dx5, dy5, dz5, dx6, dy6, dz6, dx7, dy7, dz7;

			dx1 = P1.getX() - center.getX();
			dy1 = P1.getY() - center.getY();
			dz1 = P1.getZ() - center.getZ();
			dx2 = P2.getX() - center.getX();
			dy2 = P2.getY() - center.getY();
			dz2 = P2.getZ() - center.getZ();
			dx3 = P3.getX() - center.getX();
			dy3 = P3.getY() - center.getY();
			dz3 = P3.getZ() - center.getZ();
			dx4 = P4.getX() - center.getX();
			dy4 = P4.getY() - center.getY();
			dz4 = P4.getZ() - center.getZ();
			dx5 = P5.getX() - center.getX();
			dy5 = P5.getY() - center.getY();
			dz5 = P5.getZ() - center.getZ();
			dx6 = P6.getX() - center.getX();
			dy6 = P6.getY() - center.getY();
			dz6 = P6.getZ() - center.getZ();
			dx7 = P7.getX() - center.getX();
			dy7 = P7.getY() - center.getY();
			dz7 = P7.getZ() - center.getZ();

			float d31 = MathUtil.dotProduct(dx1, dy1, dz1, axe3.getX(),
					axe3.getY(), axe3.getZ());
			float d32 = MathUtil.dotProduct(dx2, dy2, dz2, axe3.getX(),
					axe3.getY(), axe3.getZ());
			float d33 = MathUtil.dotProduct(dx3, dy3, dz3, axe3.getX(),
					axe3.getY(), axe3.getZ());
			float d34 = MathUtil.dotProduct(dx4, dy4, dz4, axe3.getX(),
					axe3.getY(), axe3.getZ());
			float d15 = MathUtil.dotProduct(dx5, dy5, dz5, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d25 = MathUtil.dotProduct(dx5, dy5, dz5, axe2.getX(),
					axe2.getY(), axe2.getZ());
			float d35 = MathUtil.dotProduct(dx5, dy5, dz5, axe3.getX(),
					axe3.getY(), axe3.getZ());
			float d16 = MathUtil.dotProduct(dx6, dy6, dz6, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d36 = MathUtil.dotProduct(dx6, dy6, dz6, axe3.getX(),
					axe3.getY(), axe3.getZ());
			float d17 = MathUtil.dotProduct(dx7, dy7, dz7, axe1.getX(),
					axe1.getY(), axe1.getZ());
			float d37 = MathUtil.dotProduct(dx7, dy7, dz7, axe3.getX(),
					axe3.getY(), axe3.getZ());

			// Test farthest point
			if (i < 0) {
				PTest1 = new Point3f(G);
				PTest2 = new Point3f(H);
				PTest3 = new Point3f(E);
				PTest4 = new Point3f(F);
				if (d15 >= 0) {
					if (d25 >= 0)
						PTest5 = new Point3f(E);
					else
						PTest5 = new Point3f(H);
				} else {
					if (d25 >= 0)
						PTest5 = new Point3f(F);
					else
						PTest5 = new Point3f(G);
				}
				PTest6 = (d16 >= 0) ? new Point3f(E) : new Point3f(F);
				PTest7 = (d17 >= 0) ? new Point3f(H) : new Point3f(G);

			} else if (i == 0) {

				PTest1 = (d31 >= 0) ? new Point3f(C) : new Point3f(G);
				PTest2 = (d32 >= 0) ? new Point3f(D) : new Point3f(H);
				PTest3 = (d33 >= 0) ? new Point3f(A) : new Point3f(E);
				PTest4 = (d34 >= 0) ? new Point3f(B) : new Point3f(F);

				if (d15 >= 0) {
					if (d25 >= 0) {
						if (d35 >= 0)
							PTest5 = new Point3f(A);
						else
							PTest5 = new Point3f(E);
					} else {
						if (d35 >= 0)
							PTest5 = new Point3f(D);
						else
							PTest5 = new Point3f(H);
					}
				} else {
					if (d25 >= 0) {
						if (d35 >= 0)
							PTest5 = new Point3f(B);
						else
							PTest5 = new Point3f(F);
					} else {
						if (d35 >= 0)
							PTest5 = new Point3f(C);
						else
							PTest5 = new Point3f(G);
					}
				}

				if (d16 >= 0) {
					if (d36 >= 0)
						PTest6 = new Point3f(A);
					else
						PTest6 = new Point3f(E);
				} else {
					if (d36 >= 0)
						PTest6 = new Point3f(B);
					else
						PTest6 = new Point3f(F);
				}

				if (d17 >= 0) {
					if (d37 >= 0)
						PTest7 = new Point3f(D);
					else
						PTest7 = new Point3f(H);
				} else {
					if (d37 >= 0)
						PTest7 = new Point3f(C);
					else
						PTest7 = new Point3f(G);
				}

			} else {
				PTest1 = new Point3f(C);
				PTest2 = new Point3f(D);
				PTest3 = new Point3f(A);
				PTest4 = new Point3f(B);
				if (d15 >= 0) {
					if (d35 >= 0)
						PTest5 = new Point3f(A);
					else
						PTest5 = new Point3f(D);
				} else {
					if (d35 >= 0)
						PTest5 = new Point3f(B);
					else
						PTest5 = new Point3f(C);
				}
				PTest6 = (d16 >= 0) ? new Point3f(A) : new Point3f(B);
				PTest7 = (d17 >= 0) ? new Point3f(D) : new Point3f(C);
			}

			assertEpsilonEquals(PTest1, q2P1);
			assertEpsilonEquals(PTest2, q2P2);
			assertEpsilonEquals(PTest3, q2P3);
			assertEpsilonEquals(PTest4, q2P4);
			assertEpsilonEquals(PTest5, q2P5);
			assertEpsilonEquals(PTest6, q2P6);
			assertEpsilonEquals(PTest7, q2P7);

			P1.add(AE);
			P2.add(AE);
			P3.add(AE);
			P4.add(AE);
			P5.add(AE);
			P6.add(AE);
			P7.add(AE);
		}

		// There's nothing for it but to test the eight remaining points
		P1.scaleAdd(-1, AB, A);
		P1.scaleAdd(-1, AD, P1);
		P1.scaleAdd(-1, AE, P1);
		P2.scaleAdd(1, AB, B);
		P2.scaleAdd(-1, AD, P2);
		P2.scaleAdd(-1, AE, P2);
		P3.scaleAdd(1, AB, C);
		P3.scaleAdd(1, AD, P3);
		P3.scaleAdd(-1, AE, P3);
		P4.scaleAdd(-1, AB, D);
		P4.scaleAdd(1, AD, P4);
		P4.scaleAdd(-1, AE, P4);
		P5.scaleAdd(-1, AB, E);
		P5.scaleAdd(-1, AD, P5);
		P5.scaleAdd(1, AE, P5);
		P6.scaleAdd(1, AB, F);
		P6.scaleAdd(-1, AD, P6);
		P6.scaleAdd(1, AE, P6);
		P7.scaleAdd(1, AB, G);
		P7.scaleAdd(1, AD, P7);
		P7.scaleAdd(1, AE, P7);
		PTest1.scaleAdd(-1, AB, H);
		PTest1.scaleAdd(1, AD, PTest1);
		PTest1.scaleAdd(1, AE, PTest1);

		GeometryUtil.closestFarthestPointsPointOBB(P1.getX(), P1.getY(),
				P1.getZ(), center.getX(), center.getY(), center.getZ(),
				axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
				axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
				axe3.getZ(), extents.getX(), extents.getY(), extents.getZ(),
				q1P1, q2P1);
		GeometryUtil.closestFarthestPointsPointOBB(P2.getX(), P2.getY(),
				P2.getZ(), center.getX(), center.getY(), center.getZ(),
				axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
				axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
				axe3.getZ(), extents.getX(), extents.getY(), extents.getZ(),
				q1P2, q2P2);
		GeometryUtil.closestFarthestPointsPointOBB(P3.getX(), P3.getY(),
				P3.getZ(), center.getX(), center.getY(), center.getZ(),
				axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
				axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
				axe3.getZ(), extents.getX(), extents.getY(), extents.getZ(),
				q1P3, q2P3);
		GeometryUtil.closestFarthestPointsPointOBB(P4.getX(), P4.getY(),
				P4.getZ(), center.getX(), center.getY(), center.getZ(),
				axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
				axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
				axe3.getZ(), extents.getX(), extents.getY(), extents.getZ(),
				q1P4, q2P4);
		GeometryUtil.closestFarthestPointsPointOBB(P5.getX(), P5.getY(),
				P5.getZ(), center.getX(), center.getY(), center.getZ(),
				axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
				axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
				axe3.getZ(), extents.getX(), extents.getY(), extents.getZ(),
				q1P5, q2P5);
		GeometryUtil.closestFarthestPointsPointOBB(P6.getX(), P6.getY(),
				P6.getZ(), center.getX(), center.getY(), center.getZ(),
				axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
				axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
				axe3.getZ(), extents.getX(), extents.getY(), extents.getZ(),
				q1P6, q2P6);
		GeometryUtil.closestFarthestPointsPointOBB(P7.getX(), P7.getY(),
				P7.getZ(), center.getX(), center.getY(), center.getZ(),
				axe1.getX(), axe1.getY(), axe1.getZ(), axe2.getX(),
				axe2.getY(), axe2.getZ(), axe3.getX(), axe3.getY(),
				axe3.getZ(), extents.getX(), extents.getY(), extents.getZ(),
				q1P7, q2P7);
		GeometryUtil.closestFarthestPointsPointOBB(PTest1.getX(),
				PTest1.getY(), PTest1.getZ(), center.getX(), center.getY(),
				center.getZ(), axe1.getX(), axe1.getY(), axe1.getZ(),
				axe2.getX(), axe2.getY(), axe2.getZ(), axe3.getX(),
				axe3.getY(), axe3.getZ(), extents.getX(), extents.getY(),
				extents.getZ(), PTest2, PTest3);
		// Closest
		assertEpsilonEquals(A, q1P1);
		assertEpsilonEquals(B, q1P2);
		assertEpsilonEquals(C, q1P3);
		assertEpsilonEquals(D, q1P4);
		assertEpsilonEquals(E, q1P5);
		assertEpsilonEquals(F, q1P6);
		assertEpsilonEquals(G, q1P7);
		assertEpsilonEquals(H, PTest2);

		// Farthest
		assertEpsilonEquals(G, q2P1);
		assertEpsilonEquals(H, q2P2);
		assertEpsilonEquals(E, q2P3);
		assertEpsilonEquals(F, q2P4);
		assertEpsilonEquals(C, q2P5);
		assertEpsilonEquals(D, q2P6);
		assertEpsilonEquals(A, q2P7);
		assertEpsilonEquals(B, PTest3);
	}

	/**
 */
	public void testClosestPointsSegmentSegmentFloatFloatFloatFloatFloatFloatFloatFloatPoint3fPoint3f() {
		Point3f nearest1 = new Point3f();
		Point3f nearest2 = new Point3f();

		// Test 0 (One segment degenerate into a point)
		GeometryUtil.closestPointsSegmentSegment(1f, 2f, 3f, 1f, 2f,
				3f, 1.5f, -1f, -0.5f, 0.4f, 0.5f, 0.6f, nearest1, nearest2);

		assertEpsilonEquals(new Point3f(1f, 2f, 3f), nearest1);
		assertEpsilonEquals(new Point3f(0.4f, 0.5f, 0.6f), nearest2);
		// f(A1,A1,B2,B1,n1,n2) = f(A1,A1,B1,B2,n1,n2);
		GeometryUtil.closestPointsSegmentSegment(1f, 2f, 3f, 1f, 2f, 3f,
				0.4f, 0.5f, 0.6f, 1.5f, -1f, -0.5f, nearest1, nearest2);

		assertEpsilonEquals(new Point3f(1f, 2f, 3f), nearest1);
		assertEpsilonEquals(new Point3f(0.4f, 0.5f, 0.6f), nearest2);

		// The other degenerate
		GeometryUtil.closestPointsSegmentSegment(1f, 2f, 3f, 0f, 1f, 2f,
				0.4f, 0.5f, 0.6f, 0.4f, 0.5f, 0.6f, nearest1, nearest2);
		assertEpsilonEquals(new Point3f(0f, 1f, 2f), nearest1);
		assertEpsilonEquals(new Point3f(0.4f, 0.5f, 0.6f), nearest2);

		// f(A1,A2,B1,B1,n1,n2) = f(A2, A1,B1,B1,n1,n2);
		 GeometryUtil.closestPointsSegmentSegment(0f, 1f, 2f, 1f, 2f, 3f,
				0.4f, 0.5f, 0.6f, 0.4f, 0.5f, 0.6f, nearest1, nearest2);
		assertEpsilonEquals(new Point3f(0f, 1f, 2f), nearest1);
		assertEpsilonEquals(new Point3f(0.4f, 0.5f, 0.6f), nearest2);

		// Both degenerate
		GeometryUtil.closestPointsSegmentSegment(0f, 1f, 2f, 0f, 1f, 2f,
				0.4f, 0.5f, 0.6f, 0.4f, 0.5f, 0.6f, nearest1, nearest2);

		assertEpsilonEquals(new Point3f(0f, 1f, 2f), nearest1);
		assertEpsilonEquals(new Point3f(0.4f, 0.5f, 0.6f), nearest2);

		// Test 1
		GeometryUtil.closestPointsSegmentSegment(1f, 2f, 3f, 0f, 1f, 2f,
				1.5f, -1f, -0.5f, 0.4f, 0.5f, 0.6f, nearest1, nearest2);

		assertEpsilonEquals(new Point3f(0f, 1f, 2f), nearest1);
		assertEpsilonEquals(new Point3f(0.4f, 0.5f, 0.6f), nearest2);

		// f(A1,A2,B1,B2,n1,n2) = f(B1,B2,A1,A2,n1,n2);
		GeometryUtil.closestPointsSegmentSegment(1.5f, -1f, -0.5f, 0.4f,
				0.5f, 0.6f, 1f, 2f, 3f, 0f, 1f, 2f, nearest1, nearest2);
		assertEpsilonEquals(new Point3f(0.4f, 0.5f, 0.6f), nearest1);
		assertEpsilonEquals(new Point3f(0f, 1f, 2f), nearest2);

		// f(A2,A1,B2,B1,n1,n2) = f(A1,A2,B1,B2,n1,n2);
		GeometryUtil.closestPointsSegmentSegment(0f, 1f, 2f, 1f, 2f, 3f,
				0.4f, 0.5f, 0.6f, 1.5f, -1f, -0.5f, nearest1, nearest2);

		assertEpsilonEquals(new Point3f(0f, 1f, 2f), nearest1);
		assertEpsilonEquals(new Point3f(0.4f, 0.5f, 0.6f), nearest2);

		GeometryUtil.closestPointsSegmentSegment(1f, 2f, 3f, 0f, 1f, 2f,
				0.5f, 1.5f, 2.5f, 0.4f, 0.5f, 1f, nearest1, nearest2);
		assertEpsilonEquals(new Point3f(0.5f, 1.5f, 2.5f), nearest1);
		assertEpsilonEquals(new Point3f(0.5f, 1.5f, 2.5f), nearest2);

		GeometryUtil.closestPointsSegmentSegment(1f, 1f, 2f, 3f, 3f, 2f,
				1f, 3f, 3f, 3f, 1f, 3f, nearest1, nearest2);
		assertEpsilonEquals(new Point3f(2f, 2f, 2f), nearest1);
		assertEpsilonEquals(new Point3f(2f, 2f, 3f), nearest2);
	}

	private void testPermutationClosestSegSeg(float x1, float y1,
			float x2, float y2, float x3, float y3, float x4, float y4,
			Point2f res1, Point2f res2) {

		Point2f p1, p2;
		
		p1 = new Point2f();
		p2 = new Point2f();
		GeometryUtil.closestPointsSegmentSegment(x1, y1, x2, y2, x3, y3, x4,
				y4, p1, p2);
		assertEpsilonEquals(res1, p1);
		assertEpsilonEquals(res2, p2);
		GeometryUtil.closestPointsSegmentSegment(x2, y2, x1, y1, x3, y3, x4,
				y4, p1, p2);
		assertEpsilonEquals(res1, p1);
		assertEpsilonEquals(res2, p2);
		GeometryUtil.closestPointsSegmentSegment(x1, y1, x2, y2, x4, y4, x3,
				y3, p1, p2);
		assertEpsilonEquals(res1, p1);
		assertEpsilonEquals(res2, p2);
		GeometryUtil.closestPointsSegmentSegment(x2, y2, x1, y1, x4, y4, x3,
				y3, p1, p2);
		assertEpsilonEquals(res1, p1);
		assertEpsilonEquals(res2, p2);
		GeometryUtil.closestPointsSegmentSegment(x3, y3, x4, y4, x1, y1, x2,
				y2, p1, p2);
		assertEpsilonEquals(res2, p1);
		assertEpsilonEquals(res1, p2);
		GeometryUtil.closestPointsSegmentSegment(x3, y3, x4, y4, x2, y2, x1,
				y1, p1, p2);
		assertEpsilonEquals(res2, p1);
		assertEpsilonEquals(res1, p2);
		GeometryUtil.closestPointsSegmentSegment(x4, y4, x3, y3, x1, y1, x2,
				y2, p1, p2);
		assertEpsilonEquals(res2, p1);
		assertEpsilonEquals(res1, p2);
		GeometryUtil.closestPointsSegmentSegment(x4, y4, x3, y3, x2, y2, x1,
				y1, p1, p2);
		assertEpsilonEquals(res2, p1);
		assertEpsilonEquals(res1, p2);
	}
	
	void testPermutationClosestSegSegSpecial(float x1, float y1,
			float x2, float y2, float x3, float y3, float x4, float y4,
			Point2f res1, Point2f res2) {
	Point2f p1, p2;
		
	p1 = new Point2f();
	p2 = new Point2f();
		
	GeometryUtil.closestPointsSegmentSegment(x1, y1, x2, y2, x3, y3, x4,
			y4, p1, p2);
	assertEpsilonEquals(res1, p1);
	assertEpsilonEquals(res1, p2);
	GeometryUtil.closestPointsSegmentSegment(x2, y2, x1, y1, x3, y3, x4,
			y4, p1, p2);
	assertEpsilonEquals(res2, p1);
	assertEpsilonEquals(res2, p2);
	GeometryUtil.closestPointsSegmentSegment(x1, y1, x2, y2, x4, y4, x3,
			y3, p1, p2);
	assertEpsilonEquals(res1, p1);
	assertEpsilonEquals(res1, p2);
	GeometryUtil.closestPointsSegmentSegment(x2, y2, x1, y1, x4, y4, x3,
			y3, p1, p2);
	assertEpsilonEquals(res2, p1);
	assertEpsilonEquals(res2, p2);
	GeometryUtil.closestPointsSegmentSegment(x3, y3, x4, y4, x1, y1, x2,
			y2, p1, p2);
	assertEpsilonEquals(res1, p1);
	assertEpsilonEquals(res1, p2);
	GeometryUtil.closestPointsSegmentSegment(x3, y3, x4, y4, x2, y2, x1,
			y1, p1, p2);
	assertEpsilonEquals(res1, p1);
	assertEpsilonEquals(res1, p2);
	GeometryUtil.closestPointsSegmentSegment(x4, y4, x3, y3, x1, y1, x2,
			y2, p1, p2);
	assertEpsilonEquals(res2, p1);
	assertEpsilonEquals(res2, p2);
	GeometryUtil.closestPointsSegmentSegment(x4, y4, x3, y3, x2, y2, x1,
			y1, p1, p2);
	assertEpsilonEquals(res2, p1);
	assertEpsilonEquals(res2, p2);
}

	public void testClosestPointsSegmentSegmentFloatFloatFloatFloatFloatFloatPoint2fPoint2f(){
		
		Point2f p1; 
		Point2f p2;
		
		// ABCD
		p1 = new Point2f(4,3);
		p2 = new Point2f(5,3);
		testPermutationClosestSegSeg(3, 3, 4, 3, 5, 3, 6, 3,
				p1, p2);
		
		// PQRS
		p1 = new Point2f(4,1);
		p2 = new Point2f(4,0);
		testPermutationClosestSegSeg(3, 1, 4, 1, 4, 0, 5, 0,
				p1, p2);
		
		// TUVW
		p1 = new Point2f(4,-1);
		p2 = new Point2f(5,-1);
		testPermutationClosestSegSeg(3, -2, 4, -1, 5, -1, 6, -1, p1, p2);
		
		// M2N2O2P2
		p1 = new Point2f(13,0);
		p2 = new Point2f(13,-1);
		testPermutationClosestSegSeg(13, 1, 13, 0, 13, -1, 13, -2, p1, p2);
		
		// D1E1F1G1
		p1 = new Point2f(8,5);
		p2 = new Point2f(8,4);
		testPermutationClosestSegSeg(7, 5, 8, 5, 7, 3, 8, 4, p1, p2);
		
		// R1S1T1T1
		p1 = new Point2f(11,5);
		p2 = new Point2f(12,5);
		testPermutationClosestSegSeg(10, 5, 11, 5, 12, 5, 12, 5, p1, p2);
		
		// W1W1Z1Z1
		p1 = new Point2f(10,3);
		p2 = new Point2f(11,3);
		testPermutationClosestSegSeg(10, 3, 10, 3, 11, 3, 11, 3, p1, p2);
		
		// EFGH
		p1 = new Point2f(4,5);
		p2 = new Point2f(5,5);
		testPermutationClosestSegSegSpecial(3, 5, 5, 5, 4, 5, 6, 5, p1, p2);
		// LMNO
		p1 = new Point2f(4,2);
		p2 = new Point2f(5,2);
		testPermutationClosestSegSegSpecial(3, 2, 6, 2, 4, 2, 5, 2, p1, p2);
	
		// B2C2D2E2
		p1 = new Point2f(14,4);
		p2 = new Point2f(14,3);
		testPermutationClosestSegSegSpecial(14, 5, 14, 3, 14, 4, 14, 2, p1, p2);
		
		// I2J2K2L2
		p1 = new Point2f(12,2);
		p2 = new Point2f(12,1);
		testPermutationClosestSegSegSpecial(12, 3, 12, 0, 12, 2, 12, 1, p1, p2);
		
		// R2S2R2T2
		p1 = new Point2f(3,6);
		p2 = new Point2f(4,6);
		testPermutationClosestSegSegSpecial(3, 6, 4, 6, 3, 6, 5, 6, p1, p2);
		
		// IJJK
		p1 = new Point2f(4,4);
		p2 = new Point2f(4,4);
		testPermutationClosestSegSeg(3, 4, 4, 4, 4, 4, 5, 4, p1, p2);
		
		// K1L1M1N1
		p1 = new Point2f(9,-1);
		p2 = new Point2f(9,-1);
		testPermutationClosestSegSeg(8, -2, 9, -1, 8, -1, 10, -1, p1, p2);
		
		// O1P1P1Q1
		p1 = new Point2f(6,1);
		p2 = new Point2f(6,1);
		testPermutationClosestSegSeg(5, 1, 6, 1, 6, 1, 6, 0, p1, p2);
		
		// ZA2A1B1
		p1 = new Point2f(8,2);
		p2 = new Point2f(8,2);
		testPermutationClosestSegSeg(7, 2, 9, 2, 8, 2, 7, 1, p1, p2);
		
		// C1H1I1J1
		p1 = new Point2f(8,0);
		p2 = new Point2f(8,0);
		testPermutationClosestSegSeg(7, 0, 9, 0, 7, -1, 9, 1, p1, p2);
		
		// F2G2G2H2
		p1 = new Point2f(13,3);
		p2 = new Point2f(13,3);
		testPermutationClosestSegSeg(13, 4, 13, 3, 13, 3, 13, 2, p1, p2);
		
		// U1V1V1V1
		p1 = new Point2f(11,4);
		p2 = new Point2f(11,4);
		testPermutationClosestSegSeg(10, 4, 11, 4, 11, 4, 11, 4, p1, p2);
		
		// Q2
		p1 = new Point2f(10,1);
		p2 = new Point2f(10,1);
		testPermutationClosestSegSeg(10, 1, 10, 1, 10, 1, 10, 1, p1, p2);
	}
	
	private void testPermutationDistanceSegSeg(float x1, float y1,
			float x2, float y2, float x3, float y3, float x4, float y4,
			float result) {

		assertEpsilonEquals(result,
				GeometryUtil.distanceSegmentSegment(x1, y1, x2,
						y2, x3, y3, x4, y4));
		assertEpsilonEquals(result,
				GeometryUtil.distanceSegmentSegment(x2, y2, x1,
						y1, x3, y3, x4, y4));
		assertEpsilonEquals(result,
				GeometryUtil.distanceSegmentSegment(x1, y1, x2,
						y2, x4, y4, x3, y3));
		assertEpsilonEquals(result,
				GeometryUtil.distanceSegmentSegment(x2, y2, x1,
						y1, x4, y4, x3, y3));

		assertEpsilonEquals(result,
				GeometryUtil.distanceSegmentSegment(x3, y3, x4,
						y4, x1, y1, x2, y2));
		assertEpsilonEquals(result,
				GeometryUtil.distanceSegmentSegment(x3, y3, x4,
						y4, x2, y2, x1, y1));
		assertEpsilonEquals(result,
				GeometryUtil.distanceSegmentSegment(x4, y4, x3,
						y3, x1, y1, x2, y2));
		assertEpsilonEquals(result,
				GeometryUtil.distanceSegmentSegment(x4, y4, x3,
						y3, x2, y2, x1, y1));
	}
	
	public void testDistanceSegmentSegmentFloatFloatFloatFloatFloatFloatFloatFloat() {
		// ABCD
		testPermutationDistanceSegSeg(3, 3, 4, 3, 5, 3, 6, 3,
				1);
		// PQRS
		testPermutationDistanceSegSeg(3, 1, 4, 1, 4, 0, 5, 0,
				1);
		// TUVW
		testPermutationDistanceSegSeg(3, -2, 4, -1, 5, -1, 6, -1,
				1);
		// M2N2O2P2
		testPermutationDistanceSegSeg(13, 1, 13, 0, 13, -1, 13, -2,
				1);
		// D1E1F1G1
		testPermutationDistanceSegSeg(7, 5, 8, 5, 7, 3, 8, 4,
				1);
		// R1S1T1T1
		testPermutationDistanceSegSeg(10, 5, 11, 5, 12, 5, 12, 5,
				1);
		// W1W1Z1Z1
		testPermutationDistanceSegSeg(10, 3, 10, 3, 11, 3, 11, 3,
				1);
		// EFGH
		testPermutationDistanceSegSeg(3, 5, 5, 5, 4, 5, 6, 5,
				0);
		// LMNO
		testPermutationDistanceSegSeg(3, 2, 6, 2, 4, 2, 5, 2,
				0);
		// B2C2D2E2
		testPermutationDistanceSegSeg(14, 5, 14, 3, 14, 4, 14, 2,
				0);
		// I2J2K2L2
		testPermutationDistanceSegSeg(12, 3, 12, 0, 12, 2, 12, 1,
				0);
		// R2S2R2T2
		testPermutationDistanceSegSeg(3, 6, 4, 6, 3, 6, 5, 6,
				0);

		// IJJK
		testPermutationDistanceSegSeg(3, 4, 4, 4, 4, 4, 5, 4, 0);

		// K1L1M1N1
		testPermutationDistanceSegSeg(8, -2, 9, -1, 8, -1, 10, -1, 0);

		// O1P1P1Q1
		testPermutationDistanceSegSeg(5, 1, 6, 1, 6, 1, 6, 0, 0);

		// ZA2A1B1
		testPermutationDistanceSegSeg(7, 2, 9, 2, 8, 2, 7, 1, 0);

		// C1H1I1J1
		testPermutationDistanceSegSeg(7, 0, 9, 0, 7, -1, 9, 1, 0);

		// F2G2G2H2
		testPermutationDistanceSegSeg(13, 4, 13, 3, 13, 3, 13, 2, 0);

		// U1V1V1V1
		testPermutationDistanceSegSeg(10, 4, 11, 4, 11, 4, 11, 4, 0);

		// Q2
		testPermutationDistanceSegSeg(10, 1, 10, 1, 10, 1, 10, 1, 0);
	}

	public void testIsInsideRectangleCircleFloatFloatFloatFloatFloatFloatFloat(){

		Point2f Test[] = { new Point2f(0.00000000000000, 4.00000000000000),
				new Point2f(0.000657949575463502, 3.93717274034993),
				new Point2f(0.00263150970342485, 3.87437303881240),
				new Point2f(0.00591981471518555, 3.81162844141206),
				new Point2f(0.0105214222514869, 3.74896647000306),
				new Point2f(0.0164343138951804, 3.68641461019704),
				new Point2f(0.0236558960565665, 3.62400029930709),
				new Point2f(0.0321830011110356, 3.56175091431277),
				new Point2f(0.0420118887884855, 3.49969375985170),
				new Point2f(0.0531382478139345, 3.43785605624283),
				new Point2f(0.0655571977985834, 3.37626492754672),
				new Point2f(0.0792632913805199, 3.31494738966803),
				new Point2f(0.0942505166141073, 3.25393033850544),
				new Point2f(0.110512299607026, 3.19324053815420),
				new Point2f(0.128041507403799, 3.13290460916659),
				new Point2f(0.146830451114540, 3.07294901687516),
				new Point2f(0.166870889287557, 3.01340005978425),
				new Point2f(0.188154031524327, 2.95428385803455),
				new Point2f(0.210670542335247, 2.89562634194597),
				new Point2f(0.234410545234500, 2.83745324064369),
				new Point2f(0.259363627072198, 2.77979007077260),
				new Point2f(0.285518842601942, 2.72266212530478),
				new Point2f(0.312864719281762, 2.66609446244522),
				new Point2f(0.341389262306357, 2.61011189464042),
				new Point2f(0.371079959868411, 2.55473897769486),
				new Point2f(0.401923788646685, 2.50000000000000),
				new Point2f(0.433907219518481, 2.44591897188061),
				new Point2f(0.467016223493956, 2.39251961506301),
				new Point2f(0.501236277869702, 2.33982535226997),
				new Point2f(0.536552372598890, 2.28785929694671),
				new Point2f(0.572949016875159, 2.23664424312258),
				new Point2f(0.610410245927413, 2.18620265541288),
				new Point2f(0.648919628022482, 2.13655665916507),
				new Point2f(0.688460271672633, 2.08772803075393),
				new Point2f(0.729014833044732, 2.03973818802969),
				new Point2f(0.770565523567818, 1.99260818092343),
				new Point2f(0.813094117735767, 1.94635868221394),
				new Point2f(0.856581961101591, 1.90100997845991),
				new Point2f(0.901009978459905, 1.85658196110159),
				new Point2f(0.946358682213935, 1.81309411773577),
				new Point2f(0.992608180923426, 1.77056552356782),
				new Point2f(1.03973818802968, 1.72901483304473),
				new Point2f(1.08772803075393, 1.68846027167264),
				new Point2f(1.13655665916507, 1.64891962802248),
				new Point2f(1.18620265541288, 1.61041024592741),
				new Point2f(1.23664424312258, 1.57294901687516),
				new Point2f(1.28785929694671, 1.53655237259889),
				new Point2f(1.33982535226997, 1.50123627786970),
				new Point2f(1.39251961506301, 1.46701622349396),
				new Point2f(1.44591897188061, 1.43390721951848),
				new Point2f(1.50000000000000, 1.40192378864669),
				new Point2f(1.55473897769485, 1.37107995986841),
				new Point2f(1.61011189464042, 1.34138926230636),
				new Point2f(1.66609446244522, 1.31286471928176),
				new Point2f(1.72266212530478, 1.28551884260194),
				new Point2f(1.77979007077260, 1.25936362707220),
				new Point2f(1.83745324064369, 1.23441054523450),
				new Point2f(1.89562634194597, 1.21067054233525),
				new Point2f(1.95428385803455, 1.18815403152433),
				new Point2f(2.01340005978425, 1.16687088928756),
				new Point2f(2.07294901687516, 1.14683045111454),
				new Point2f(2.13290460916658, 1.12804150740380),
				new Point2f(2.19324053815420, 1.11051229960703),
				new Point2f(2.25393033850544, 1.09425051661411),
				new Point2f(2.31494738966803, 1.07926329138052),
				new Point2f(2.37626492754672, 1.06555719779858),
				new Point2f(2.43785605624282, 1.05313824781394),
				new Point2f(2.49969375985169, 1.04201188878849),
				new Point2f(2.56175091431276, 1.03218300111104),
				new Point2f(2.62400029930709, 1.02365589605657),
				new Point2f(2.68641461019704, 1.01643431389518),
				new Point2f(2.74896647000305, 1.01052142225149),
				new Point2f(2.81162844141206, 1.00591981471519),
				new Point2f(2.87437303881240, 1.00263150970343),
				new Point2f(2.93717274034993, 1.00065794957546),
				new Point2f(3.00000000000000, 1.00000000000000) };
		float TestWidth[] = { 6.00000000000f, 5.99868410084f, 5.99473698059f,
				5.98816037056f, 5.97895715549f, 5.96713137220f, 5.95268820788f,
				5.93563399777f, 5.91597622242f, 5.89372350437f, 5.86888560440f,
				5.84147341723f, 5.81149896677f, 5.77897540078f, 5.74391698519f,
				5.70633909777f, 5.66625822142f, 5.62369193695f, 5.57865891532f,
				5.53117890953f, 5.48127274585f, 5.42896231479f, 5.37427056143f,
				5.31722147538f, 5.25784008026f, 5.19615242270f, 5.13218556096f,
				5.06596755301f, 4.99752744426f, 4.92689525480f, 4.85410196624f,
				4.77917950814f, 4.70216074395f, 4.62307945665f, 4.54197033391f,
				4.45886895286f, 4.37381176452f, 4.28683607779f, 4.19798004308f,
				4.10728263557f, 4.01478363815f, 3.92052362394f, 3.82454393849f,
				3.72688668166f, 3.62759468917f, 3.52671151375f, 3.42428140610f,
				3.32034929546f, 3.21496076987f, 3.10816205623f, 3.00000000000f,
				2.89052204461f, 2.77977621071f, 2.66781107510f, 2.55467574939f,
				2.44041985845f, 2.32509351871f, 2.20874731610f, 2.09143228393f,
				1.97319988043f, 1.85410196624f, 1.73419078166f, 1.61351892369f,
				1.49213932298f, 1.37010522066f, 1.24747014490f, 1.12428788751f,
				1.00061248029f, 0.876498171374f, 0.751999401385f,
				0.627170779605f, 0.502067059993f, 0.376743117175f,
				0.251253922375f, 0.125654519300f, 0.00000000000000f };
		float TestHeight[] = { 0.00000000000000f, 0.125654519300141f,
				0.251253922375193f, 0.376743117175891f, 0.502067059993895f,
				0.627170779605926f, 0.751999401385827f, 0.876498171374475f,
				1.00061248029662f, 1.12428788751435f, 1.24747014490656f,
				1.37010522066394f, 1.49213932298913f, 1.61351892369160f,
				1.73419078166683f, 1.85410196624969f, 1.97319988043150f,
				2.09143228393090f, 2.20874731610807f, 2.32509351871263f,
				2.44041985845481f, 2.55467574939044f, 2.66781107510957f,
				2.77977621071918f, 2.89052204461030f, 3.00000000000001f,
				3.10816205623878f, 3.21496076987399f, 3.32034929546007f,
				3.42428140610660f, 3.52671151375484f, 3.62759468917426f,
				3.72688668166987f, 3.82454393849214f, 3.92052362394064f,
				4.01478363815316f, 4.10728263557214f, 4.19798004308020f,
				4.28683607779682f, 4.37381176452847f, 4.45886895286437f,
				4.54197033391054f, 4.62307945665474f, 4.70216074395504f,
				4.77917950814518f, 4.85410196624969f, 4.92689525480223f,
				4.99752744426060f, 5.06596755301210f, 5.13218556096304f,
				5.19615242270664f, 5.25784008026318f, 5.31722147538730f,
				5.37427056143648f, 5.42896231479612f, 5.48127274585561f,
				5.53117890953101f, 5.57865891532951f, 5.62369193695135f,
				5.66625822142489f, 5.70633909777093f, 5.74391698519241f,
				5.77897540078596f, 5.81149896677179f, 5.84147341723897f,
				5.86888560440284f, 5.89372350437214f, 5.91597622242304f,
				5.93563399777794f, 5.95268820788687f, 5.96713137220965f,
				5.97895715549703f, 5.98816037056964f, 5.99473698059316f,
				5.99868410084908f, 6.00000000000000f };

		for (int i = 0; i < Test.length; i++) {
			assertTrue("Rectangle (" + Test[i].toString() + "," + TestWidth[i] +"," +  TestHeight[i] + ")", GeometryUtil.isInsideRectangleCircle(Test[i].getX(), Test[i].getY(), TestWidth[i], TestHeight[i], 3, 4, 3));
			
			//Some random tests
			assertTrue("Rectangle (" + Test[i].toString() + "," + TestWidth[i] +"," +  TestHeight[i] + ")", GeometryUtil.isInsideRectangleCircle((float)Math.ceil(Test[i].getX()), (float)Math.ceil(Test[i].getY()), (float)Math.floor(TestWidth[i])-1, (float)Math.floor(TestHeight[i]-1), 3, 4, 3));
			if((float)Math.floor(Test[i].getX()) != Test[i].getX()&&(float)Math.floor(Test[i].getY())!=Test[i].getY())
				assertFalse("Rectangle (" + Test[i].toString() + "," + TestWidth[i] +"," +  TestHeight[i] + ")", GeometryUtil.isInsideRectangleCircle((float)Math.floor(Test[i].getX()), (float)Math.floor(Test[i].getY()), (float)Math.ceil(TestWidth[i]), (float)Math.ceil(TestHeight[i]), 3, 4, 3));
		}
	}

	public void testIsInsideRectangleEllipseFloatFloatFloatFloatFloatFloatFloatFloat(){
		Point2f Test[] = { new Point2f(1, 2.99975),
				new Point2f(1.00066, 2.95292), new Point2f(1.00265, 2.90612),
				new Point2f(1.00594, 2.85935), new Point2f(1.01055, 2.81264),
				new Point2f(1.01647, 2.76602), new Point2f(1.0237, 2.7195),
				new Point2f(1.03223, 2.6731), new Point2f(1.04207, 2.62685),
				new Point2f(1.0532, 2.58076), new Point2f(1.06563, 2.53485),
				new Point2f(1.07934, 2.48915), new Point2f(1.09433, 2.44367),
				new Point2f(1.1106, 2.39844), new Point2f(1.12814, 2.35347),
				new Point2f(1.14693, 2.30878), new Point2f(1.16698, 2.2644),
				new Point2f(1.18827, 2.22034), new Point2f(1.21079, 2.17662),
				new Point2f(1.23454, 2.13326), new Point2f(1.2595, 2.09028),
				new Point2f(1.28566, 2.0477), new Point2f(1.31301, 2.00554),
				new Point2f(1.34154, 1.96382), new Point2f(1.37124, 1.92255),
				new Point2f(1.40209, 1.88175), new Point2f(1.43408, 1.84144),
				new Point2f(1.46719, 1.80165), new Point2f(1.50142, 1.76237),
				new Point2f(1.53674, 1.72364), new Point2f(1.57314, 1.68547),
				new Point2f(1.61061, 1.64788), new Point2f(1.64913, 1.61088),
				new Point2f(1.68867, 1.57449), new Point2f(1.72923, 1.53872),
				new Point2f(1.77079, 1.50359), new Point2f(1.81332, 1.46913),
				new Point2f(1.85681, 1.43533), new Point2f(1.90125, 1.40222),
				new Point2f(1.9466, 1.36981), new Point2f(1.99286, 1.33811),
				new Point2f(2.03999, 1.30715), new Point2f(2.08798, 1.27692),
				new Point2f(2.13682, 1.24745), new Point2f(2.18647, 1.21876),
				new Point2f(2.23691, 1.19084), new Point2f(2.28813, 1.16371),
				new Point2f(2.3401, 1.13739), new Point2f(2.3928, 1.11189),
				new Point2f(2.4462, 1.08722), new Point2f(2.50029, 1.06338),
				new Point2f(2.55503, 1.0404), new Point2f(2.61041, 1.01827),
				new Point2f(2.66639, 0.99702), new Point2f(2.72296, 0.97664),
				new Point2f(2.78009, 0.95715), new Point2f(2.83776, 0.93856),
				new Point2f(2.89594, 0.92087), new Point2f(2.9546, 0.90409),
				new Point2f(3.01371, 0.88823), new Point2f(3.07327, 0.8733),
				new Point2f(3.13322, 0.8593), new Point2f(3.19356, 0.84624),
				new Point2f(3.25425, 0.83412), new Point2f(3.31604, 0.82282),
				new Point2f(3.37736, 0.81262), new Point2f(3.43895, 0.80338),
				new Point2f(3.5008, 0.79511), new Point2f(3.56286, 0.7878),
				new Point2f(3.62511, 0.78146), new Point2f(3.68753, 0.77609),
				new Point2f(3.75008, 0.7717), new Point2f(3.81274, 0.76829),
				new Point2f(3.87627, 0.76583), new Point2f(3.93908, 0.76439),
				new Point2f(4.0019, 0.76393), new Point2f(4.06473, 0.76445),
				new Point2f(4.12753, 0.76595), new Point2f(4.12753, 0.76595) };
		float TestWidth[] = { 6f, 5.99867f, 5.99471f, 5.98812f, 5.9789f,
				5.96706f, 5.9526f, 5.93554f, 5.91587f, 5.8936f, 5.86875f,
				5.84132f, 5.81133f, 5.7788f, 5.74372f, 5.70613f, 5.66604f,
				5.62346f, 5.57841f, 5.53092f, 5.481f, 5.42868f, 5.37397f,
				5.31691f, 5.25752f, 5.19582f, 5.13184f, 5.06561f, 4.99716f,
				4.92652f, 4.85371f, 4.77878f, 4.70175f, 4.62266f, 4.54154f,
				4.45842f, 4.37336f, 4.28637f, 4.1975f, 4.1068f, 4.01429f,
				3.92002f, 3.82403f, 3.72637f, 3.62706f, 3.52617f, 3.42374f,
				3.3198f, 3.2144f, 3.10759f, 2.99942f, 2.88994f, 2.77919f,
				2.66722f, 2.55407f, 2.43981f, 2.32448f, 2.20813f, 2.09081f,
				1.97257f, 1.85347f, 1.73355f, 1.61288f, 1.4915f, 1.36793f,
				1.24528f, 1.12209f, 0.99841f, 0.87429f, 0.74978f, 0.62495f,
				0.49984f, 0.37451f, 0.24745f, 0.12185f, 0.00381f, 0.12946f,
				0.25506f, 0.25506f };
		float TestHeight[] = { 0.0005f, 0.09415f, 0.18777f, 0.2813f, 0.37471f,
				0.46796f, 0.561f, 0.65379f, 0.7463f, 0.83848f, 0.93029f,
				1.0217f, 1.11266f, 1.20312f, 1.29306f, 1.38244f, 1.4712f,
				1.55933f, 1.64676f, 1.73348f, 1.81943f, 1.90459f, 1.98891f,
				2.07236f, 2.1549f, 2.2365f, 2.31711f, 2.39671f, 2.47526f,
				2.55272f, 2.62906f, 2.70424f, 2.77825f, 2.85103f, 2.92256f,
				2.99281f, 3.06175f, 3.12934f, 3.19557f, 3.26039f, 3.32378f,
				3.38571f, 3.44616f, 3.50509f, 3.56249f, 3.61833f, 3.67257f,
				3.72521f, 3.77621f, 3.82556f, 3.87323f, 3.9192f, 3.96345f,
				4.00597f, 4.04672f, 4.0857f, 4.12289f, 4.15827f, 4.19183f,
				4.22354f, 4.25341f, 4.28141f, 4.30753f, 4.33176f, 4.35436f,
				4.37476f, 4.39323f, 4.40979f, 4.4244f, 4.43708f, 4.44781f,
				4.45659f, 4.46342f, 4.46833f, 4.47121f, 4.47214f, 4.47109f,
				4.46809f, 4.46809f };

		for (int i = 0; i < Test.length; i++) {
			assertTrue("Rectangle (" + Test[i].toString() + "," + TestWidth[i] +"," +  TestHeight[i] + ")", GeometryUtil.isInsideRectangleEllipse(Test[i].getX(), Test[i].getY(), TestWidth[i], TestHeight[i], 1, 0.76f, 6 , 4.472135955f));
			
			//Some random tests
			assertTrue("Rectangle (" + Test[i].toString() + "," + TestWidth[i] +"," +  TestHeight[i] + ")", GeometryUtil.isInsideRectangleEllipse((float)Math.ceil(Test[i].getX()), (float)Math.ceil(Test[i].getY()), (float)Math.floor(TestWidth[i])-1, (float)Math.floor(TestHeight[i]-1),1, 0.76f, 6 , 4.472135955f));
			if((float)Math.floor(Test[i].getX()) != Test[i].getX()&&(float)Math.floor(Test[i].getY())!=Test[i].getY()){
				assertFalse("Rectangle (" + Test[i].toString() + "," + TestWidth[i] +"," +  TestHeight[i] + ")", GeometryUtil.isInsideRectangleEllipse((float)Math.floor(Test[i].getX()), (float)Math.floor(Test[i].getY()), (float)Math.ceil(TestWidth[i]), (float)Math.ceil(TestHeight[i]),1, 0.76f, 6 , 4.472135955f));

			}
		}
	}
	
	public void testGetPointSideOfLineFloatFloatFloatFloatFloatFloatFloat(){
		float x1, y1, x2, y2;

		Point2f[] Test = { new Point2f(-5, 0), new Point2f(10, 5),
				new Point2f(-5, 5), new Point2f(35, 10), new Point2f(25, 10),
				new Point2f(25, 5), new Point2f(25, 0), new Point2f(-5, -5),
				new Point2f(-10, -5), new Point2f(-15, 0),
				new Point2f(-10, 10), new Point2f(0, 15) };
		float[] ResultA = {0, 0, -1, 1, 0, 1, 1, 1, 1, -1, -1, -1};
		float[] ResultB = {1, 0, 0, -1, -1, 0, 1, 1, 1, 1, -1, -1};
		float[] ResultC = {0, 1, 0, 1, 1, 1, 1, 0, -1, -1, -1, 1};

		// Test for line a;
		x1 = -5;
		y1 = 0;
		x2 = 10;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals("i : " + i, ResultA[i],
					GeometryUtil.getPointSideOfLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2, 0.000001f));
			assertEpsilonEquals(- ResultA[i],
					GeometryUtil.getPointSideOfLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1, 0.000001f));
		}

		// Test for line b;
		x1 = -5;
		y1 = 5;
		x2 = 10;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals("i : " + i, ResultB[i],
					GeometryUtil.getPointSideOfLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2, 0.000001f));
			assertEpsilonEquals(- ResultB[i],
					GeometryUtil.getPointSideOfLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1, 0.000001f));

		}

		// Test for line c;
		x1 = -5;
		y1 = 0;
		x2 = -5;
		y2 = 5;

		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals("i : " + i, ResultC[i],
					GeometryUtil.getPointSideOfLine(Test[i].getX(),
							Test[i].getY(), x1, y1, x2, y2, 0.000001f));
			assertEpsilonEquals(- ResultC[i],
					GeometryUtil.getPointSideOfLine(Test[i].getX(),
							Test[i].getY(), x2, y2, x1, y1, 0.000001f));
		}
	}
	
	public  void testdistancePointLineFloatFloatFloatFloatFloatFloatFloatFloatFloat(){
		
		Point3f Test[] = { new Point3f(1.781647, 2.819595, 1.666684),
				new Point3f(0.534186, 3.853968, 4.914417),
				new Point3f(0.104251, 0.147336, 2.330839),
				new Point3f(1.334119, 0.992426, 3.515241),
				new Point3f(-0.504207, 0.544427, 4.043304),
				new Point3f(1.074041, 0.020599, 2.302883),
				new Point3f(1.581475, 3.93706, 2.63732),
				new Point3f(-0.141716, 3.185309, 2.538548),
				new Point3f(0.062452, 1.701414, 1.165931),
				new Point3f(1.362414, 0.10608, 3.216567),
				new Point3f(1.994536, 0.933138, 1.823487),
				new Point3f(1.688033, 1.658933, 2.710954),
				new Point3f(0.430065, 3.027444, 3.217156),
				new Point3f(0.787565, 1.949064, 2.204631),
				new Point3f(-0.388556, 1.792134, 2.605656),
				new Point3f(1.196283, 1.249554, 2.528066),
				new Point3f(0.632463, 0.083071, 1.011022),
				new Point3f(0.982919, 2.865157, 1.176078),
				new Point3f(0.24711, 0.901469, 3.974773),
				new Point3f(1.561027, 1.006844, 4.554246),
				new Point3f(-0.859095, 0.558243, 2.977581),
				new Point3f(-0.631967, 1.58364, 1.149658),
				new Point3f(1.50361, 0.459107, 1.333775),
				new Point3f(1.885626, 3.464167, 4.962201),
				new Point3f(1.228518, 0.13961, 3.417692),
				new Point3f(1.903288, 3.435143, 2.288384),
				new Point3f(0.574934, 3.295545, 3.643004),
				new Point3f(-0.031332, 1.523636, 2.256836),
				new Point3f(1.062675, 3.38768, 4.104871),
				new Point3f(-0.546983, 2.335789, 4.554966),
				new Point3f(-0.443972, 0.454836, 3.910119),
				new Point3f(-0.929131, 0.718302, 3.263279),
				new Point3f(0.110078, 0.732242, 1.767889),
				new Point3f(0.915957, 2.13152, 1.052043),
				new Point3f(-0.524048, 1.304174, 4.475334),
				new Point3f(-0.705156, 3.750799, 4.379853),
				new Point3f(-0.525437, 2.565687, 2.904608),
				new Point3f(-0.556823, 2.878974, 4.958734),
				new Point3f(-0.51838, 1.801779, 1.481956),
				new Point3f(1.851033, 1.605928, 4.801894),
				new Point3f(0.830992, 1.02815, 1.379705),
				new Point3f(0.648869, 3.372581, 3.257961),
				new Point3f(-0.405865, 0.174045, 3.20988),
				new Point3f(-0.100907, 2.407387, 3.203084),
				new Point3f(-0.503757, 1.838201, 2.97777),
				new Point3f(1.27428, 2.441399, 3.862973),
				new Point3f(0.678076, 2.452613, 4.474404),
				new Point3f(-0.812004, 0.16131, 4.511708),
				new Point3f(1.833763, 1.705085, 2.298207),
				new Point3f(1.152526, 1.800888, 4.723661),
				new Point3f(0, 1, 2), new Point3f(1, 1, 2),
				new Point3f(1, 3, 2), new Point3f(1, 3, 4) };
		float[] ExportResult_a = { 1.849871757685111f, 4.079088108255692f,
				0.91459845878779f, 1.515259929370866f, 2.093475100101503f,
				1.025165562477593f, 3.005411490295463f, 2.250690865886517f,
				1.089794795434902f, 1.509678188187469f, 0.188752129029052f,
				0.969354573210959f, 2.364740556905133f, 0.970873999166215f,
				0.997143652786298f, 0.584064122568747f, 1.348642382370137f,
				2.039033618343013f, 1.977229566714498f, 2.55425516909568f,
				1.07275992589675f, 1.031366649918447f, 0.85814974688221f,
				3.853148552585275f, 1.658349046179362f, 2.452159611833006f,
				2.822939778146356f, 0.583231850460861f, 3.183001368055157f,
				2.883085762456088f, 1.986393309759424f, 1.294305835204724f,
				0.354358381987784f, 1.476130070233989f, 2.493952732878472f,
				3.637388546747515f, 1.808228806217012f, 3.504946529610972f,
				0.954577996172654f, 2.866663344102338f, 0.620933417948334f,
				2.685443441795414f, 1.464927054984309f, 1.851526202575864f,
				1.287872310945848f, 2.355482854093827f, 2.869278599750292f,
				2.648032853528067f, 0.7655535723083f, 2.838970030039944f, 0f,
				0f, 2f, 2.82842712474619f };
		
		float[] ExportResult_b = {0.84974795584632f, 2.951407991533024f, 0.954893038471849f, 1.551641322678022f, 2.537268203258182f, 0.31180150956979f, 0.862722410758524f, 1.262358654646135f, 1.25485750149768f, 1.269401107958001f, 1.010078558561165f, 0.989365958179783f, 1.343984610239641f, 0.294961820217465f, 1.514895031172787f, 0.563365526496785f, 1.055064420238404f, 0.824099036915467f, 2.113426543229974f, 2.615133633917204f, 2.100452053865072f, 1.840216782352829f, 0.835151951877621f, 3.091758104424892f, 1.435991324203597f, 0.948205959905336f, 1.697098479279267f, 1.062831324867686f, 2.105803904039025f, 2.986805595187775f, 2.394495715374116f, 2.305953218736668f, 0.919693798176872f, 0.951675207041772f, 2.906888494225398f, 2.927670966134172f, 1.773491943774485f, 3.343322409832022f, 1.604321479734034f, 2.928287409788356f, 0.642907140331324f, 1.306047034636195f, 1.854795409910484f, 1.630768939397915f, 1.793688737754965f, 1.883055473725881f, 2.495257545222938f, 3.097101156449365f, 0.885487523920016f, 2.727928412476581f, 1f, 0f, 0f, 2f};
		float[] ExportResult_c = {0.802195734614689f, 0.972750753081178f, 2.989993004656867f, 2.035187674794882f, 2.87966620586102f, 2.980320853277714f, 1.102812141402605f, 1.156656755540294f, 1.601662215231414f, 2.916524793962156f, 2.293691431369965f, 1.507265772044864f, 0.570595370960018f, 1.072191729739136f, 1.84038801318961f, 1.761416536485621f, 2.939992897510129f, 0.135920547416496f, 2.22950122450314f, 2.070609121747752f, 3.068943053573005f, 2.160877585308571f, 2.590320495527339f, 0.999892202072303f, 2.869503690261436f, 1.002635846852186f, 0.517714159919352f, 1.800915421312172f, 0.392713557220782f, 1.683547639602159f, 2.926245875465696f, 2.987924393013484f, 2.436121407616624f, 0.872536954087906f, 2.280032484106312f, 1.863130731520738f, 1.586059842168006f, 1.561520139481076f, 1.934221132456421f, 1.633307658793346f, 1.97907961602458f, 0.51196638632043f, 3.156339344913661f, 1.250274526021386f, 1.900279461408243f, 0.622305869810819f, 0.635033534189337f, 3.367717240523022f, 1.540118695878341f, 1.208773663354724f, 2.23606797749979f, 2f, 0f, 0f};
		float[] ExportResult_d = {2.021115170460357f, 0.857052542382904f, 0.865624824117816f, 1.404960815517366f, 1.960616710440337f, 1.481850130805481f, 1.750715709499657f, 1.39911163917859f, 1.089486380924771f, 1.92026646994343f, 1.939381126138067f, 1.269140498745203f, 0.67629350228104f, 0.7060805833231f, 0.708180495532664f, 0.964891267260491f, 1.046758951125117f, 2.01983170350417f, 1.48090621052989f, 1.999554591163298f, 1.372529858849708f, 1.14551325387531f, 1.704443098026449f, 0.610572243286756f, 1.910328116398042f, 1.905920606912313f, 0.601756496484251f, 0.284908737824744f, 0.207942337163455f, 1.672123876656571f, 1.887394239000927f, 1.555541634247377f, 0.223031722985518f, 1.683750209304384f, 1.917722494702737f, 1.892399981834062f, 1.174658489188761f, 1.831926125366413f, 1.086125783000856f, 1.816145088344235f, 1.030648913468112f, 0.825050884759237f, 1.515314632954893f, 0.724969356596922f, 0.908352259431268f, 0.517121688278709f, 0.77717577382247f, 2.637792389846648f, 1.519889105230232f, 1.383472558130165f, 0f, 0.942809041582063f, 1.49071198499986f, 0f};
		
		Point3f s1, s2;
		
		s1 = new Point3f(0,1,2);
		s2 = new Point3f(1,1,2);
		
		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(ExportResult_a[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), Test[i].getZ(), s1.getX(),
							s1.getY(), s1.getZ(), s2.getX(),
							s2.getY(), s2.getZ()));
			assertEpsilonEquals(ExportResult_a[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), Test[i].getZ(), s2.getX(),
							s2.getY(), s2.getZ(), s1.getX(),
							s1.getY(), s1.getZ()));
		}

		s1 = new Point3f(1,1,2);
		s2 = new Point3f(1,3,2);
		
		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(ExportResult_b[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), Test[i].getZ(), s1.getX(),
							s1.getY(), s1.getZ(), s2.getX(),
							s2.getY(), s2.getZ()));
			assertEpsilonEquals(ExportResult_b[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), Test[i].getZ(), s2.getX(),
							s2.getY(), s2.getZ(), s1.getX(),
							s1.getY(), s1.getZ()));
		}
		
		s1 = new Point3f(1,3,2);
		s2 = new Point3f(1,3,4);
		
		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(ExportResult_c[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), Test[i].getZ(), s1.getX(),
							s1.getY(), s1.getZ(), s2.getX(),
							s2.getY(), s2.getZ()));
			assertEpsilonEquals(ExportResult_c[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), Test[i].getZ(), s2.getX(),
							s2.getY(), s2.getZ(), s1.getX(),
							s1.getY(), s1.getZ()));
		}
		
		s1 = new Point3f(1,3,4);
		s2 = new Point3f(0,1,2);
		
		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals(ExportResult_d[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), Test[i].getZ(), s1.getX(),
							s1.getY(), s1.getZ(), s2.getX(),
							s2.getY(), s2.getZ()));
			assertEpsilonEquals(ExportResult_d[i],
					GeometryUtil.distancePointLine(Test[i].getX(),
							Test[i].getY(), Test[i].getZ(), s2.getX(),
							s2.getY(), s2.getZ(), s1.getX(),
							s1.getY(), s1.getZ()));
		}
	}
	
	public void testClosestPointPointCircleFloatFloatFloatFloatFloatPoint2f() {

		Point2f test[] = { new Point2f(4, -2), new Point2f(9, -2),
				new Point2f(4, 2), new Point2f(-1, -2), new Point2f(4, -6),
				new Point2f(7, -2), new Point2f(4, 1), new Point2f(3, -1),
				new Point2f(8, 2), new Point2f(1.44, 3.92), new Point2f(0, -4),
				new Point2f(8, -5), new Point2f(12, 2), new Point2f(1, 2) };
		Point2f closest[] = { new Point2f(7, -2), new Point2f(7, -2),
				new Point2f(4, 1), new Point2f(1, -2), new Point2f(4, -5),
				new Point2f(7, -2), new Point2f(4, 1), new Point2f(1.88, 0.12),
				new Point2f(6.12, 0.12), new Point2f(2.81, 0.75),
				new Point2f(1.32, -3.34), new Point2f(6.4, -3.8),
				new Point2f(6.68, -0.66), new Point2f(2.2, 0.4) };

		for (int i = 0; i < test.length; i++) {
			GeometryUtil.closestPointPointCircle(test[i].getX(),
					test[i].getY(), 4, -2, 3, closest[i]);
		}

	}
	
	public void testIsInsideRectangleRectangleFloatFloatFloatFloatFloatFloatFloatFloat(){
		assert(true);
	}
	
	public void testIsInsidePointRectangleFloatFloatFloatFloatFloatFloat(){
		assert(true);
	}
	
	public void testDistanceSquaredPointRectangleFloatFloatFloatFloatFloatFloat(){
		
		Point2f[] Test = {new Point2f(4, 2), new Point2f(4, 0), new Point2f(9, 0), new Point2f(9, 2), new Point2f(1, 3), new Point2f(0, 2), new Point2f(0, 1), new Point2f(1, 0), new Point2f(4, -2), new Point2f(6, -2), new Point2f(9, -1), new Point2f(11, -3), new Point2f(11, 0), new Point2f(10, 1), new Point2f(13, 2), new Point2f(11, 3), new Point2f(9, 3), new Point2f(8, 4), new Point2f(4, 3), new Point2f(5, 1), new Point2f(6, 2), new Point2f(4, 1.52), new Point2f(5.48, 0), new Point2f(9, 0.74)};
		float[] ExportResult = {0, 0, 0, 0, 10, 16, 16, 9, 4, 4, 1, 13, 4, 1, 16, 5, 1, 4, 1, 0, 0, 0, 0, 0};

		
		for (int i = 0; i < Test.length; i++) {
			assertEpsilonEquals( i + "" , ExportResult[i], GeometryUtil.distanceSquaredPointRectangle(Test[i].getX(),
					Test[i].getY(),4f,0f,5f,2f));
		}
		
	}
	
	public void testClosestPointPointRectangleFloatFloatFloatFloatFloatFloatPoint2D() {
		assertTrue(true);
	}
	
	public void testIsInsideRectangleRoundRectangleFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat(){
				
		Point2f[] Test = { new Point2f(-1.4142135624, 3.960846501),
				new Point2f(-1.4142135624, 3.9224093998),
				new Point2f(-1.4142135624, 3.8839722987),
				new Point2f(-1.4142135624, 3.8070980963),
				new Point2f(-1.4142135624, 3.8070980963),
				new Point2f(-1.4142135624, 3.7494424445),
				new Point2f(-1.4142135624, 3.730223894),
				new Point2f(-1.4142135624, 3.6917867928),
				new Point2f(-1.4142135624, 3.6149125904),
				new Point2f(-1.4142135624, 3.5764754893),
				new Point2f(-1.4142135624, 3.4803827363),
				new Point2f(-1.4142135624, 3.3842899834),
				new Point2f(-1.4142135624, 3.307415781),
				new Point2f(-1.4142135624, 3.1728859269),
				new Point2f(-1.4142135624, 3.0960117246),
				new Point2f(-1.4142135624, 2.980700421),
				new Point2f(-1.4142135624, 2.8653891175),
				new Point2f(-1.4142135624, 2.7308592634),
				new Point2f(-1.4142135624, 2.5386737575),
				new Point2f(-1.4142135624, 2.4425810046),
				new Point2f(-1.4142135624, 2.2311769481),
				new Point2f(-1.4142135624, 2.0197728917),
				new Point2f(-1.3601410833, 1.7261239026),
				new Point2f(-1.2101040618, 1.4824827734),
				new Point2f(-1.0778642753, 1.352617345),
				new Point2f(-0.9718513594, 1.2735342626),
				new Point2f(-0.832961366, 1.1918615952),
				new Point2f(-0.8240649786, 1.1873140487),
				new Point2f(-0.7543022847, 1.154119375),
				new Point2f(-0.5387685655, 1.0754113258),
				new Point2f(-0.3053389827, 1.0235861263),
				new Point2f(-0.1575299627, 1.0062232869),
				new Point2f(0.1602304922, 1), new Point2f(0.4292902004, 1),
				new Point2f(0.6791313581, 1), new Point2f(1, 1),
				new Point2f(1.2749064263, 1), new Point2f(1.332562078, 1),
				new Point2f(1.5055290333, 1), new Point2f(1.7938072921, 1),
				new Point2f(2, 1), new Point2f(2.1973968545, 1),
				new Point2f(2.4280194615, 1), new Point2f(2.639423518, 1),
				new Point2f(3, 1) };
		float[] TestWidth = { 5.8284271247f, 5.8284271247f, 5.8284271247f,
				5.8284271247f, 5.8284271247f, 5.8284271247f, 5.8284271247f,
				5.8284271247f, 5.8284271247f, 5.8284271247f, 5.8284271247f,
				5.8284271247f, 5.8284271247f, 5.8284271247f, 5.8284271247f,
				5.8284271247f, 5.8284271247f, 5.8284271247f, 5.8284271247f,
				5.8284271247f, 5.8284271247f, 5.8284271247f, 5.7202821667f,
				5.4202081236f, 5.1557285506f, 4.9437027188f, 4.665922732f,
				4.6481299573f, 4.5086045693f, 4.077537131f, 3.6106779654f,
				3.3150599254f, 2.8397695808f, 2.5707098726f, 2.3208687149f,
				2.000000073f, 1.7250936333f, 1.6674379816f, 1.4944710263f,
				1.2061927675f, 1.000000073f, 0.8026032051f, 0.5719805981f,
				0.3605765416f, 0.000000073f };
		float[] TestHeight = { 0.039153499f, 0.0775906002f, 0.1160277013f,
				0.1929019037f, 0.1929019037f, 0.2505575555f, 0.269776106f,
				0.3082132072f, 0.3850874096f, 0.4235245107f, 0.5196172637f,
				0.6157100166f, 0.692584219f, 0.8271140731f, 0.9039882754f,
				1.019299579f, 1.1346108825f, 1.2691407366f, 1.4613262425f,
				1.5574189954f, 1.7688230519f, 1.9802271083f, 2.5477521947f,
				3.0350344531f, 3.2947653101f, 3.4529314748f, 3.6162768097f,
				3.6253719027f, 3.6917612499f, 3.8491773483f, 3.9528277475f,
				3.9875534261f, 4f, 4f, 4f, 4f, 4f, 4f, 4f, 4f, 4f, 4f, 4f, 4f,
				4 };
				
		for (int i = 0; i < Test.length; i++) {
			
			assertTrue("Rectangle (" + Test[i].toString() + "," + TestWidth[i]
					+ "," + TestHeight[i] + ")",
					GeometryUtil.isInsideRectangleRoundRectangle(
							Test[i].getX(), Test[i].getY(), TestWidth[i],
							TestHeight[i], -1.4142135624f, 1, 5.8284271247f, 4,
							1.4142135624f, 1));

			// Some random tests
			assertTrue("Rectangle (" + Test[i].toString() + "," + TestWidth[i]
					+ "," + TestHeight[i] + ")",
					GeometryUtil.isInsideRectangleRoundRectangle(
							(float) Math.ceil(Test[i].getX()),
							(float) Math.ceil(Test[i].getY()),
							 MathUtil.clamp((float)Math.floor(TestWidth[i]) - 1,0,TestWidth[i]),
							 MathUtil.clamp((float)Math.floor(TestHeight[i]) - 1,0,TestWidth[i]),
							-1.4142135624f, 1f, 5.8284271247f, 4,
							1.4142135624f, 1));
			if ((float) Math.floor(Test[i].getX()) != Test[i].getX()
					&& (float) Math.floor(Test[i].getY()) != Test[i].getY()) {
				assertFalse("Rectangle (" + Test[i].toString() + ","
						+ TestWidth[i] + "," + TestHeight[i] + ")",
						GeometryUtil.isInsideRectangleRoundRectangle(
							(float) Math.floor(Test[i].getX()),
							(float) Math.floor(Test[i].getY()),
							(float) Math.ceil(TestWidth[i]),
							(float) Math.ceil(TestHeight[i]),
							-1.4142135624f, 1f, 5.8284271247f, 4,
							1.4142135624f, 1));

			}
		}

	}
	
	public void isInsidePointRoundRectangleFloatFloatFloatFloatFloatFloatFloatFloat(){

	
	}
	
	public void testClosestPointPointRoundRectangleFloatFloatFloatFloatFloatFloatFloatFloatPoint2D() {
		Point2f test[] = { new Point2f(3.998731949, 4.7680505239),
				new Point2f(3.757485275, 4.7574067381),
				new Point2f(3.5180842844, 4.725777422),
				new Point2f(3.2823509634, 4.6734032938),
				new Point2f(3.0520793849, 4.6006829523),
				new Point2f(2.8290220547, 4.5081698433),
				new Point2f(2.6148765737, 4.3965680472),
				new Point2f(2.4112727183, 4.2667269212),
				new Point2f(2.2197600368, 4.1196346347),
				new Point2f(2.0417960564, 3.9564106487),
				new Point2f(1.8787351904, 3.7782971964),
				new Point2f(1.7318184305, 3.5866498286),
				new Point2f(1.6021639018, 3.3829270976),
				new Point2f(1.4907583537, 3.1686794564),
				new Point2f(1.3984496497, 2.9455374588),
				new Point2f(1.3259403147, 2.7151993502),
				new Point2f(1.2737821886, 2.4794181427),
				new Point2f(1.2423722261, 2.2399882737),
				new Point2f(1.2319494761, 1.998731949),
				new Point2f(1.2425932619, 1.757485275),
				new Point2f(1.274222578, 1.5180842844),
				new Point2f(1.3265967062, 1.2823509634),
				new Point2f(1.3993170477, 1.0520793849),
				new Point2f(1.4918301567, 0.8290220547),
				new Point2f(1.6034319528, 0.6148765737),
				new Point2f(1.7332730788, 0.4112727183),
				new Point2f(1.8803653653, 0.2197600368),
				new Point2f(2.0435893513, 0.0417960564),
				new Point2f(2.2217028036, -0.1212648096),
				new Point2f(2.4133501714, -0.2681815695),
				new Point2f(2.6170729024, -0.3978360982),
				new Point2f(2.8313205436, -0.5092416463),
				new Point2f(3.0544625412, -0.6015503503),
				new Point2f(3.2848006498, -0.6740596853),
				new Point2f(3.2848006498, -0.6740596853),
				new Point2f(3.5205818573, -0.7262178114),
				new Point2f(3.7600117263, -0.7576277739),
				new Point2f(4.001268051, -0.7680505239),
				new Point2f(4.242514725, -0.7574067381),
				new Point2f(4.4819157156, -0.725777422),
				new Point2f(4.7176490366, -0.6734032938),
				new Point2f(4.9479206151, -0.6006829523),
				new Point2f(5.1709779453, -0.5081698433),
				new Point2f(5.3851234263, -0.3965680472),
				new Point2f(5.5887272817, -0.2667269212),
				new Point2f(5.7802399632, -0.1196346347),
				new Point2f(5.9582039436, 0.0435893513),
				new Point2f(6.1212648096, 0.2217028036),
				new Point2f(6.2681815695, 0.4133501714),
				new Point2f(6.3978360982, 0.6170729024),
				new Point2f(6.5092416463, 0.8313205436),
				new Point2f(6.6015503503, 1.0544625412),
				new Point2f(6.6740596853, 1.2848006498),
				new Point2f(6.7262178114, 1.5205818573),
				new Point2f(6.7576277739, 1.7600117263),
				new Point2f(6.7680505239, 2.001268051),
				new Point2f(6.7574067381, 2.242514725),
				new Point2f(6.725777422, 2.4819157156),
				new Point2f(6.6734032938, 2.7176490366),
				new Point2f(6.6006829523, 2.9479206151),
				new Point2f(6.5081698433, 3.1709779453),
				new Point2f(6.3965680472, 3.3851234263),
				new Point2f(6.2667269212, 3.5887272817),
				new Point2f(6.1196346347, 3.7802399632),
				new Point2f(5.9564106487, 3.9582039436),
				new Point2f(5.7782971964, 4.1212648096),
				new Point2f(5.5866498286, 4.2681815695),
				new Point2f(5.3829270976, 4.3978360982),
				new Point2f(5.1686794564, 4.5092416463),
				new Point2f(4.9455374588, 4.6015503503),
				new Point2f(4.7151993502, 4.6740596853),
				new Point2f(4.4794181427, 4.7262178114),
				new Point2f(4.2399882737, 4.7576277739),
				new Point2f(3.998731949, 4.7680505239),
				new Point2f(3.998731949, 4.7680505239), new Point2f(2, 3),
				new Point2f(6, 3), new Point2f(6, 1),
				new Point2f(2.087868, 2.912132), new Point2f(2, 1),
				new Point2f(2.6749183834, 2.4640360626), new Point2f(2.3, 2.7),
				new Point2f(2.1211074395, 2.8437105757), new Point2f(2.3, 3),
				new Point2f(1.6021639018, 3.3829270976), new Point2f(2.3, 1.3),
				new Point2f(2.3, 1), new Point2f(5.7, 1.3),
				new Point2f(5.7, 1), new Point2f(5.7, 2.7),
				new Point2f(5.7, 3), new Point2f(2, 1.3), new Point2f(2, 2.7),
				new Point2f(6, 2.6999999157), new Point2f(6, 1.2999999118),
				new Point2f(4, 2), new Point2f(3.998731949, 3),
				new Point2f(3.998731949, 4.7680505239),
				new Point2f(3.998731949, 4.7680505239),
				new Point2f(2.1048181986, 2.974513857), new Point2f(2, 1.1),
				new Point2f(5.95, 1.05), new Point2f(5.95, 2.95) };

		Point2f Result[] = { new Point2f(3.998731949, 3),
				new Point2f(3.757485275, 3), new Point2f(3.5180842844, 3),
				new Point2f(3.2823509634, 3), new Point2f(3.0520793849, 3),
				new Point2f(2.8290220547, 3), new Point2f(2.6148765737, 3),
				new Point2f(2.4112727183, 3),
				new Point2f(2.2830705528, 2.9995219421),
				new Point2f(2.2396093238, 2.9938587522),
				new Point2f(2.1908324834, 2.979432377),
				new Point2f(2.1381372594, 2.9525875159),
				new Point2f(2.0855899363, 2.9098292749),
				new Point2f(2.0403959228, 2.8503519973),
				new Point2f(2.0105431684, 2.778833639),
				new Point2f(2.0000365166, 2.704680668),
				new Point2f(2, 2.4794181427), new Point2f(2, 2.2399882737),
				new Point2f(2, 1.998731949), new Point2f(2, 1.757485275),
				new Point2f(2, 1.5180842844),
				new Point2f(2.0000492993, 1.2945615133),
				new Point2f(2.0107575045, 1.220383552),
				new Point2f(2.0408029147, 1.1489474562),
				new Point2f(2.0861182022, 1.0896322825),
				new Point2f(2.1386997566, 1.0470529077),
				new Point2f(2.1913692701, 1.0203585072),
				new Point2f(2.2400940145, 1.0060420559),
				new Point2f(2.2834980813, 1.0004541994),
				new Point2f(2.4133501714, 1), new Point2f(2.6170729024, 1),
				new Point2f(2.8313205436, 1), new Point2f(3.0544625412, 1),
				new Point2f(3.2848006498, 1), new Point2f(3.2848006498, 1),
				new Point2f(3.5205818573, 1), new Point2f(3.7600117263, 1),
				new Point2f(4.001268051, 1), new Point2f(4.242514725, 1),
				new Point2f(4.4819157156, 1), new Point2f(4.7176490366, 1),
				new Point2f(4.9479206151, 1), new Point2f(5.1709779453, 1),
				new Point2f(5.3851234263, 1), new Point2f(5.5887272817, 1),
				new Point2f(5.7169294472, 1.0004780579),
				new Point2f(5.7603906762, 1.0061412478),
				new Point2f(5.8091675166, 1.020567623),
				new Point2f(5.8618627406, 1.0474124841),
				new Point2f(5.9144100637, 1.0901707251),
				new Point2f(5.9596040772, 1.1496480027),
				new Point2f(5.9894568316, 1.221166361),
				new Point2f(5.9999634834, 1.295319332),
				new Point2f(6, 1.5205818573), new Point2f(6, 1.7600117263),
				new Point2f(6, 2.001268051), new Point2f(6, 2.242514725),
				new Point2f(6, 2.4819157156),
				new Point2f(5.9999507007, 2.7054384867),
				new Point2f(5.9892424955, 2.779616448),
				new Point2f(5.9591970853, 2.8510525438),
				new Point2f(5.9138817978, 2.9103677175),
				new Point2f(5.8613002434, 2.9529470923),
				new Point2f(5.8086307299, 2.9796414928),
				new Point2f(5.7599059855, 2.9939579441),
				new Point2f(5.7165019187, 2.9995458006),
				new Point2f(5.5866498286, 3), new Point2f(5.3829270976, 3),
				new Point2f(5.1686794564, 3), new Point2f(4.9455374588, 3),
				new Point2f(4.7151993502, 3), new Point2f(4.4794181427, 3),
				new Point2f(4.2399882737, 3), new Point2f(3.998731949, 3),
				new Point2f(3.998731949, 3),
				new Point2f(2.0878679656, 2.9121320344),
				new Point2f(5.9121320344, 2.9121320344),
				new Point2f(5.9121320344, 1.0878679656),
				new Point2f(2.0878679656, 2.9121320344),
				new Point2f(2.0878679656, 1.0878679656),
				new Point2f(2.6749183834, 2.4640360626), new Point2f(2.3, 2.7),
				new Point2f(2.1211074395, 2.8437105757), new Point2f(2.3, 3),
				new Point2f(2.0855899363, 2.9098292749), new Point2f(2.3, 1.3),
				new Point2f(2.3, 1), new Point2f(5.7, 1.3),
				new Point2f(5.7, 1), new Point2f(5.7, 2.7),
				new Point2f(5.7, 3), new Point2f(2, 1.3), new Point2f(2, 2.7),
				new Point2f(6, 2.6999999157), new Point2f(6, 1.2999999118),
				new Point2f(4, 2), new Point2f(3.998731949, 3),
				new Point2f(3.998731949, 3), new Point2f(3.998731949, 3),
				new Point2f(2.1261594585, 2.944498397),
				new Point2f(2.0503849117, 1.1335899411),
				new Point2f(5.9121320344, 1.0878679656),
				new Point2f(5.9121320344, 2.9121320344) };

		Point2f closest = new Point2f();

		for (int i = 0; i < test.length; i++) {
			GeometryUtil.closestPointPointRoundRectangle(test[i].getX(),
					test[i].getY(), 2, 1, 4, 2, 0.3f, 0.3f, closest, 1e-12f);
			assertEpsilonEquals(test[i].toString() + "," + Result[i].toString()
					+ ", " + closest.toString(), Result[i], closest);
		}

	}

	public void testComputeCrossingsFromPointFloatFloatFloatFloatFloatFloat(){

		Point2f[] Test = { new Point2f(-1, 4), new Point2f(0, 4),
				new Point2f(-4, 4), new Point2f(-2, 4), new Point2f(-2, 5),
				new Point2f(0, 5), new Point2f(-2, 3), new Point2f(0, 3),
				new Point2f(0, 2), new Point2f(1, 3), new Point2f(0, 6),
				new Point2f(1, 5), new Point2f(-1.5, 3.5),
				new Point2f(-1.5, 4.5), new Point2f(2, 3), new Point2f(3, 4),
				new Point2f(4, 4), new Point2f(3, 5), new Point2f(5, 3),
				new Point2f(6, 5), new Point2f(8, 3), new Point2f(7, 5),
				new Point2f(9, 3), new Point2f(9, 5), new Point2f(10, 4),
				new Point2f(11, 4) };
		
		assertEquals(0,GeometryUtil.computeCrossingsFromPoint(Test[Test.length/2 -1 ].getX(), Test[Test.length/2 -1].getY(),Test[Test.length/2].getX(), Test[Test.length/2].getY(), -1, 4));
		assertEquals(0,GeometryUtil.computeCrossingsFromPoint(Test[Test.length/2].getX(), Test[Test.length/2].getY(),Test[Test.length/2 -1].getX(), Test[Test.length/2 -1].getY(), -1, 4));
		
		for(int i = 0; i <= 7;i++){
			
			assertEquals(0,GeometryUtil.computeCrossingsFromPoint(Test[2*i].getX(), Test[2*i].getY(),Test[2*i+1].getX(), Test[2*i+1].getY(), -1, 4));
			assertEquals(0,GeometryUtil.computeCrossingsFromPoint(Test[2*i+1].getX(), Test[2*i+1].getY(),Test[2*i].getX(), Test[2*i].getY(), -1, 4));
		}
		
		for(int i = 8; i < (Test.length-1)/2;i++){
			
			assertEquals("i" + i , 1,GeometryUtil.computeCrossingsFromPoint(Test[2*i].getX(), Test[2*i].getY(),Test[2*i+1].getX(), Test[2*i+1].getY(), -1, 4));
			assertEquals(-1,GeometryUtil.computeCrossingsFromPoint(Test[2*i+1].getX(), Test[2*i+1].getY(),Test[2*i].getX(), Test[2*i].getY(), -1, 4));
		}
	}

	public void testIsInsidePointSegmentFloatFloatFloatFloatFloatFloat(){
		
		Point2f[] test = {new Point2f(0, 2), new Point2f(1.31, 2), new Point2f(2, 2), new Point2f(2, 1.39), new Point2f(2, 0), new Point2f(0.78, 1.22), new Point2f(-1, 2), new Point2f(-1, 3), new Point2f(1, 3), new Point2f(2, 3), new Point2f(3, 2), new Point2f(3, 1), new Point2f(3, -1), new Point2f(2, -1), new Point2f(1, 0), new Point2f(1.5, 1)};
		
		boolean b = true;
		
		for(int i = 0; i < test.length; i++){
						
			if(i == 3 )
				b = false;			
				
			assertEquals(b , GeometryUtil.isInsidePointSegment(test[i].getX(), test[i].getY(), 0f, 2f, 2f, 2f, epsilon));
			assertEquals(b , GeometryUtil.isInsidePointSegment(test[i].getX(), test[i].getY(), 2f, 2f, 0f, 2f, epsilon));
		}
		
		for(int i = 0; i < test.length; i++){
			
			if(i == 2 )
				b = true;	
			
			if(i == 5)
				b = false;
				
			assertEquals(b , GeometryUtil.isInsidePointSegment(test[i].getX(), test[i].getY(), 2f, 0f, 2f, 2f, epsilon));
			assertEquals(b , GeometryUtil.isInsidePointSegment(test[i].getX(), test[i].getY(), 2f, 2f, 2f, 0f, epsilon));
		}
		
		b = true;
		
		for(int i = 0; i < test.length; i++){
			
			if(i == 4 || i == 5)
				b = true;
				
			assertEquals("i" + i , b , GeometryUtil.isInsidePointSegment(test[i].getX(), test[i].getY(),  2f, 0f, 0f, 2f, epsilon));
			assertEquals(b , GeometryUtil.isInsidePointSegment(test[i].getX(), test[i].getY(),  0f, 2f, 2f, 0f, epsilon));
			
			b =false; 
		}
	}

	public void testClosestPointPointSegmentFloatFloatFloatFloatFloatFloatPoint2D(){
		
		Point2f[] test = {new Point2f(3, 2), new Point2f(7, 1), new Point2f(7, 3), new Point2f(8, -2), new Point2f(3, 3), new Point2f(8, -1), new Point2f(9, 6), new Point2f(8, 1), new Point2f(6, 7), new Point2f(8.33826080789595, 2.4286488907037), new Point2f(5, 6), new Point2f(8, 3), new Point2f(3, 5), new Point2f(11, 4), new Point2f(2, 6), new Point2f(5, 3), new Point2f(2, 8), new Point2f(3, 2.59154609004047), new Point2f(1, 5), new Point2f(2.9, 2.8), new Point2f(6, 8), new Point2f(1, 2.5), new Point2f(0, 3), new Point2f(5.40941176470589, 2.60235294117647), new Point2f(2, 2), new Point2f(0, 1.5), new Point2f(1, 1.5), new Point2f(2, 1.5), new Point2f(3, 1), new Point2f(3.2, 0.600000000000001), new Point2f(3.2, 1.2), new Point2f(4, 1), new Point2f(5, 2)};
		Point2f[] closesta = {new Point2f(3, 2), new Point2f(6.529411764705882, 2.882352941176471), new Point2f(7, 3), new Point2f(6.764705882352941, 2.941176470588236), new Point2f(3.235294117647059, 2.058823529411764), new Point2f(7, 3), new Point2f(7, 3), new Point2f(7, 3), new Point2f(7, 3), new Point2f(7, 3), new Point2f(5.823529411764706, 2.705882352941177), new Point2f(7, 3), new Point2f(3.705882352941177, 2.176470588235294), new Point2f(7, 3), new Point2f(3, 2), new Point2f(5.117647058823529, 2.529411764705882), new Point2f(3.470588235294118, 2.117647058823529), new Point2f(3.13918731530364, 2.03479682882591), new Point2f(3, 2), new Point2f(3.094117647058823, 2.023529411764706), new Point2f(7, 3), new Point2f(3, 2), new Point2f(3, 2), new Point2f(5.409411764705889, 2.602352941176472), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3.705882352941177, 2.176470588235294), new Point2f(4.882352941176471, 2.470588235294118)};
		Point2f[] closestb = {new Point2f(3, 3), new Point2f(7, 3), new Point2f(7, 3), new Point2f(7, 3), new Point2f(3, 3), new Point2f(7, 3), new Point2f(7, 3), new Point2f(7, 3), new Point2f(6, 3), new Point2f(7, 3), new Point2f(5, 3), new Point2f(7, 3), new Point2f(3, 3), new Point2f(7, 3), new Point2f(3, 3), new Point2f(5, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(6, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(5.40941176470589, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3.2, 3), new Point2f(3.2, 3), new Point2f(4, 3), new Point2f(5, 3)};
		Point2f[] closestc = {new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 3), new Point2f(3, 2), new Point2f(3, 3), new Point2f(3, 2), new Point2f(3, 3), new Point2f(3, 2), new Point2f(3, 3), new Point2f(3, 2.4286488907037), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 3), new Point2f(3, 2.59154609004047), new Point2f(3, 3), new Point2f(3, 2.8), new Point2f(3, 3), new Point2f(3, 2.5), new Point2f(3, 3), new Point2f(3, 2.60235294117647), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2), new Point2f(3, 2)};
	
		Point2f p1 = new Point2f();
		Point2f p2 = new Point2f();
		
		for(int i = 0 ; i < test.length ; i++){
			
			GeometryUtil.closestPointPointSegment(test[i].getX(), test[i].getY(), 3f, 2f, 7f, 3f, p1);
			GeometryUtil.closestPointPointSegment(test[i].getX(), test[i].getY(), 7f, 3f, 3f, 2f, p2);
		
		
			assertEpsilonEquals(closesta[i] , p1);
			assertEpsilonEquals(closesta[i] , p2);
		}
		
		for(int i = 0 ; i < test.length ; i++){
			
			GeometryUtil.closestPointPointSegment(test[i].getX(), test[i].getY(), 3f, 3f, 7f, 3f, p1);
			GeometryUtil.closestPointPointSegment(test[i].getX(), test[i].getY(), 7f, 3f, 3f, 3f, p2);
		
		
			assertEpsilonEquals(closestb[i] , p1);
			assertEpsilonEquals(closestb[i] , p2);

		}
		
		for(int i = 0 ; i < test.length ; i++){
			
			GeometryUtil.closestPointPointSegment(test[i].getX(), test[i].getY(), 3f, 2f, 3f, 3f, p1);
			GeometryUtil.closestPointPointSegment(test[i].getX(), test[i].getY(), 3f, 3f, 3f, 2f, p2);
		
		
			assertEpsilonEquals(closestc[i] , p1);
			assertEpsilonEquals(closestc[i] , p2);

		}
	}

	public void testIsInsidePointPointOrientedRectangleFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat(){
		
		Point2f[] In = 
			{new Point2f(-0.999999, -1), new Point2f(-0.999999, -1), new Point2f(-0.1999991333, -0.4666668), new Point2f(0.6000007333, 0.0666664), new Point2f(1.4000006, 0.5999996), new Point2f(2.2000004667, 1.1333328), new Point2f(3.0000003333, 1.666666), new Point2f(3.8000002, 2.1999992), new Point2f(4.6000000667, 2.7333324), new Point2f(5.1333332667, 2.7999990667), new Point2f(5.3999998, 2.3999992), new Point2f(5.6666663333, 1.9999993333), new Point2f(5.9333328667, 1.5999994667), new Point2f(6.1999994, 1.1999996), new Point2f(6.4666659333, 0.7999997333), new Point2f(6.7333324667, 0.3999998667), new Point2f(6.999999, 0), new Point2f(6.1999991333, -0.5333332), new Point2f(5.3999992667, -1.0666664), new Point2f(4.5999994, -1.5999996), new Point2f(3.7999995333, -2.1333328), new Point2f(2.9999996667, -2.666666), new Point2f(2.1999998, -3.1999992), new Point2f(1.3999999333, -3.7333324), new Point2f(0.8666667333, -3.7999990667), new Point2f(0.6000002, -3.3999992), new Point2f(0.3333336667, -2.9999993333), new Point2f(0.0666671333, -2.5999994667), new Point2f(-0.1999994, -2.1999996), new Point2f(-0.4666659333, -1.7999997333), new Point2f(-0.7333324667, -1.3999998667), new Point2f(-0.999999, -1), new Point2f(-0.999999, -1), new Point2f(-0.999999, -1)};
		Point2f[] Out =
				{new Point2f(-1.000001, -1), new Point2f(-1.000001, -1), new Point2f(-1.000001, -1), new Point2f(-0.2000008667, -0.4666665333), new Point2f(0.5999992667, 0.0666669333), new Point2f(1.3999994, 0.6000004), new Point2f(2.1999995333, 1.1333338667), new Point2f(2.9999996667, 1.6666673333), new Point2f(3.7999998, 2.2000008), new Point2f(4.5999999333, 2.7333342667), new Point2f(5.1333334, 2.8000009333), new Point2f(5.4000002, 2.4000008), new Point2f(5.666667, 2.0000006667), new Point2f(5.9333338, 1.6000005333), new Point2f(6.2000006, 1.2000004), new Point2f(6.4666674, 0.8000002667), new Point2f(6.7333342, 0.4000001333), new Point2f(7.000001, 0), new Point2f(6.2000008667, -0.5333334667), new Point2f(5.4000007333, -1.0666669333), new Point2f(4.6000006, -1.6000004), new Point2f(3.8000004667, -2.1333338667), new Point2f(3.0000003333, -2.6666673333), new Point2f(2.2000002, -3.2000008), new Point2f(1.4000000667, -3.7333342667), new Point2f(0.8666666, -3.8000009333), new Point2f(0.5999998, -3.4000008), new Point2f(0.333333, -3.0000006667), new Point2f(0.0666662, -2.6000005333), new Point2f(-0.2000006, -2.2000004), new Point2f(-0.4666674, -1.8000002667), new Point2f(-0.7333342, -1.4000001333), new Point2f(-1.000001, -1), new Point2f(-1.000001, -1)};
		float d = 3.6055512755f;
		
		for(int i = 0 ; i < In.length; i++){
			assertEquals(true, GeometryUtil.isInsidePointOrientedRectangle(In[i].getX(), In[i].getY(), 1,-4, 3/d,2/d, -2/d, 3/d, 2*d,d));
		}
		
		for(int i = 0 ; i < Out.length; i++){
			assertEquals(false, GeometryUtil.isInsidePointOrientedRectangle(Out[i].getX(), Out[i].getY(), 1,-4, 3/d,2/d, -2/d, 3/d, 2*d,d));
		}	
	}
	
}
