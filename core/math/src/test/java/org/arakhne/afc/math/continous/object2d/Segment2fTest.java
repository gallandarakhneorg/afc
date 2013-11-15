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
public class Segment2fTest extends AbstractShape2fTestCase<Segment2f> {
	
	@Override
	protected Segment2f createShape() {
		return new Segment2f(0f, 0f, 1f, 1f);
	}
	
	@Override
	public void testIsEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.clear();
		assertTrue(this.r.isEmpty());
	}

	@Override
	public void testClear() {
		this.r.clear();
		assertEquals(0f, this.r.getX1());
		assertEquals(0f, this.r.getY1());
		assertEquals(0f, this.r.getX2());
		assertEquals(0f, this.r.getY2());
	}

	@Override
	public void testClone() {
		Segment2f b = this.r.clone();

		assertNotSame(b, this.r);
		assertEpsilonEquals(this.r.getX1(), b.getX1());
		assertEpsilonEquals(this.r.getY1(), b.getY1());
		assertEpsilonEquals(this.r.getX2(), b.getX2());
		assertEpsilonEquals(this.r.getY2(), b.getY2());
		
		b.set(this.r.getX1()+1f, this.r.getY1()+1f,
				this.r.getX2()+1f, this.r.getY2()+1f);

		assertNotEpsilonEquals(this.r.getX1(), b.getX1());
		assertNotEpsilonEquals(this.r.getY1(), b.getY1());
		assertNotEpsilonEquals(this.r.getX2(), b.getX2());
		assertNotEpsilonEquals(this.r.getY2(), b.getY2());
	}

	/**
	 */
	public static void testComputeCrossingsFromPoint() {
		assertEquals(
				1,
				Segment2f.computeCrossingsFromPoint(
						0f, 0f,
						10f, -1f, 10f, 1f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromPoint(
						0f, 0f,
						10f, -1f, 10f, -.5f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromPoint(
						0f, 0f,
						-10f, -1f, -10f, 1f));
	}

	/**
	 */
	public static void testComputeCrossingsFromRect() {
		assertEquals(
				2,
				Segment2f.computeCrossingsFromRect(
						0,
						0f, 0f, 1f, 1f,
						10f, -5f, 10f, 5f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromRect(
						0,
						0f, 0f, 1f, 1f,
						10f, -5f, 10f, .5f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromRect(
						0,
						0f, 0f, 1f, 1f,
						10f, -5f, 10f, -1f));
	}
	
	/**
	 */
	public static void testComputeCrossingsFromSegment_0011() {
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						10f, -5f, 10f, -4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						10f, 5f, 10f, 4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						-5f, .5f, 0f, .6f));
		
		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						10f, -1f, 11f, .6f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						10f, -1f, 11f, 2f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						10f, .5f, 11f, 2f));

		assertEquals(
				-1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						10f, 2f, 11f, .6f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						10f, 2f, 11f, -1f));
		assertEquals(
				-1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						10f, .6f, 11f, -1f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						0f, .5f, .25f, .5f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						.75f, .5f, 1f, .5f));

		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						5f, -5f, .75f, .5f));

		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						5f, -5f, 0f, 1f));

		assertEquals(
				2,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						5f, -5f, 1f, 1f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 0f, 1f, 1f,
						-2f, 1f, 5f, -5f));
	}

	/**
	 */
	public static void testComputeCrossingsFromSegment_1100() {
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						10f, -5f, 10f, -4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						10f, 5f, 10f, 4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						-5f, .5f, 0f, .6f));
		
		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						10f, -1f, 11f, .6f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						10f, -1f, 11f, 2f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						10f, .5f, 11f, 2f));

		assertEquals(
				-1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						10f, 2f, 11f, .6f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						10f, 2f, 11f, -1f));
		assertEquals(
				-1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						10f, .6f, 11f, -1f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						0f, .5f, .25f, .5f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						.75f, .5f, 1f, .5f));

		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						5f, -5f, .75f, .5f));

		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						5f, -5f, 0f, 1f));

		assertEquals(
				2,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						5f, -5f, 1f, 1f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 0f, 0f,
						-2f, 1f, 5f, -5f));
	}

	/**
	 */
	public static void testComputeCrossingsFromSegment_Others() {
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						7f, -5f, 1f, 1f,
						4f, -3f, 1f, 1f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromSegment(
						0,
						4f, -3f, 1f, 1f,
						7f, -5f, 1f, 1f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 4f, -3f,
						7f, -5f, 1f, 1f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromSegment(
						0,
						4f, -3f, 1f, 1f,
						1f, 1f, 7f, -5f));
		assertEquals(
				-1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 1f, 4f, -3f,
						1f, 1f, 7f, -5f));
	}

	/**
	 */
	public static void testComputeCrossingsFromSegment_0110() {
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						10f, -5f, 10f, -4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						10f, 5f, 10f, 4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						-5f, .5f, 0f, .6f));
		
		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						10f, -1f, 11f, .6f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						10f, -1f, 11f, 2f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						10f, .5f, 11f, 2f));

		assertEquals(
				-1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						10f, 2f, 11f, .6f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						10f, 2f, 11f, -1f));
		assertEquals(
				-1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						10f, .6f, 11f, -1f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						0f, .5f, .25f, .5f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						.75f, .5f, 1f, .5f));

		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						5f, -.01f, .75f, .5f));

		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						20f, -5f, 0f, 1f));

		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						5f, 10f, .25f, .5f));
	}

	/**
	 */
	public static void testComputeCrossingsFromSegment_1001() {
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						10f, -5f, 10f, -4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						10f, 5f, 10f, 4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						-5f, .5f, 0f, .6f));
		
		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						10f, -1f, 11f, .6f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						10f, -1f, 11f, 2f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						10f, .5f, 11f, 2f));

		assertEquals(
				-1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						10f, 2f, 11f, .6f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						10f, 2f, 11f, -1f));
		assertEquals(
				-1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						10f, .6f, 11f, -1f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						0f, .5f, .25f, .5f));

		assertEquals(
				0,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						.75f, .5f, 1f, .5f));

		assertEquals(
				1,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						20f, -5f, .75f, .5f));

		assertEquals(
				2,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						20f, -5f, 0f, 1f));

		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromSegment(
						0,
						1f, 0f, 0f, 1f,
						5f, 10f, .25f, .5f));
	}

	/**
	 */
	public static void testComputeCrossingsFromEllipse_PositiveTest() {
		assertEquals(
				0,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						-1f, -5f, 5f, -4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						-5f, -1f, -7f, 3f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						10f, -1f, 11f, -2f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						1f, 6f, 3f, 5f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						6f, -1f, 5f, .5f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						6f, -1f, 5f, 2f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						6f, .5f, 5f, 2f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						3f, 0f, .5f, .5f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						3f, 0f, 0f, 1f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						3f, 0f, 0f, 1f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						.5f, -1f, .5f, 2f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 1f, 1f,
						7f, -5f, 1f, 1f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						4f, -3f, 1f, 2f,
						7f, -5f, 1f, 1f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						4f, -3f, 1f, 2f,
						7f, -5f, 4f, 0f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromEllipse(
						0,
						4f, -3f, 1f, 2f,
						7f, -5f, 4.2f, 0f));
	}

	/**
	 */
	public static void testComputeCrossingsFromEllipse_NegativeTest() {
		assertEquals(
				0,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						5f, -4f, -1f, -5f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						-7f, 3f, -5f, -1f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						11f, -2f, 10f, -1f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						3f, 5f, 1f, 6f));
		assertEquals(
				-1,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						5f, .5f, 6f, -1f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						5f, 2f, 6f, -1f));
		assertEquals(
				-1,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						5f, 2f, 6f, .5f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						.5f, .5f, 3f, 0f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						0f, 1f, 3f, 0f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						0f, 1f, 3f, 0f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 2f, 1f,
						.5f, 2f, .5f, -1f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromEllipse(
						0,
						0f, 0f, 1f, 1f,
						1f, 1f, 7f, -5f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromEllipse(
						0,
						4f, -3f, 1f, 2f,
						1f, 1f, 7f, -5f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromEllipse(
						0,
						4f, -3f, 1f, 2f,
						4.2f, 0f, 7f, -5f));
	}

	/**
	 */
	public static void testComputeCrossingsFromCircle_PositiveTest() {
		assertEquals(
				0,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						-1f, -5f, 5f, -4f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						-5f, -1f, -7f, 3f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						10f, -1f, 11f, -2f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						1f, 6f, 3f, 5f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						6f, -1f, 5f, .5f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						6f, -1f, 5f, 2f));
		assertEquals(
				1,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						6f, .5f, 5f, 2f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						3f, 0f, .5f, .5f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						3f, 0f, 0f, 2f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						.5f, -1f, .5f, 4f));
		assertEquals(
				2,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						3f, 0f, 1f, 3f));
	}

	/**
	 */
	public static void testComputeCrossingsFromCircle_NegativeTest() {
		assertEquals(
				0,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						5f, -4f, -1f, -5f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						-7f, -3f, -5f, -1f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						11f, -2f, 10f, -1f));
		assertEquals(
				0,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						3f, 5f,  1f, 6f));
		assertEquals(
				-1,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						5f, .5f, 6f, -1f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						5f, 2f, 6f, -1f));
		assertEquals(
				-1,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						5f, 2f, 6f, .5f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						.5f, .5f, 3f, 0f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						0f, 2f, 3f, 0f));
		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						.5f, 4f, .5f, -1f));
		assertEquals(
				-2,
				Segment2f.computeCrossingsFromCircle(
						0,
						1f, 1f, 1f,
						1f, 3f, 3f, 0f));
	}

	/**
	 */
	public static void testIntersectsLineLine() {
		assertTrue(Segment2f.intersectsLineLine(
				0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Segment2f.intersectsLineLine(
				0f, 0f, 1f, 1f,
				0f, 0f, 2f, 2f));
		assertTrue(Segment2f.intersectsLineLine(
				0f, 0f, 1f, 1f,
				0f, 0f, .5f, .5f));
		assertTrue(Segment2f.intersectsLineLine(
				0f, 0f, 1f, 1f,
				-3f, -3f, .5f, .5f));
		assertTrue(Segment2f.intersectsLineLine(
				0f, 0f, 1f, 1f,
				-3f, -3f, 0f, 0f));
		assertTrue(Segment2f.intersectsLineLine(
				0f, 0f, 1f, 1f,
				-3f, -3f, -1f, -1f));

		assertTrue(Segment2f.intersectsLineLine(
				0f, 0f, 1f, 1f,
				-3f, 0f, 4f, 0f));

		assertFalse(Segment2f.intersectsLineLine(
				0f, 0f, 1f, 1f,
				-3f, 0f, -2f, 1f));

		assertFalse(Segment2f.intersectsLineLine(
				0f, 0f, 1f, 1f,
				10f, 0f, 9f, -1f));
	}

	/**
	 */
	public static void testIntersectsSegmentLine() {
		assertTrue(Segment2f.intersectsSegmentLine(
				0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Segment2f.intersectsSegmentLine(
				0f, 0f, 1f, 1f,
				0f, 0f, 2f, 2f));
		assertTrue(Segment2f.intersectsSegmentLine(
				0f, 0f, 1f, 1f,
				0f, 0f, .5f, .5f));
		assertTrue(Segment2f.intersectsSegmentLine(
				0f, 0f, 1f, 1f,
				-3f, -3f, .5f, .5f));
		assertTrue(Segment2f.intersectsSegmentLine(
				0f, 0f, 1f, 1f,
				-3f, -3f, 0f, 0f));
		assertTrue(Segment2f.intersectsSegmentLine(
				0f, 0f, 1f, 1f,
				-3f, -3f, -1f, -1f));

		assertTrue(Segment2f.intersectsSegmentLine(
				0f, 0f, 1f, 1f,
				-3f, 0f, 4f, 0f));

		assertFalse(Segment2f.intersectsSegmentLine(
				0f, 0f, 1f, 1f,
				-3f, 0f, -2f, 1f));

		assertFalse(Segment2f.intersectsSegmentLine(
				0f, 0f, 1f, 1f,
				10f, 0f, 9f, -1f));
	}

	/**
	 */
	public static void testIntersectsSegmentSegmentWithoutEnds() {
		assertTrue(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				0f, .5f, 1f, .5f));
		assertTrue(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				0f, 0f, 2f, 2f));
		assertTrue(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				0f, 0f, .5f, .5f));
		assertTrue(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				-3f, -3f, .5f, .5f));
		assertFalse(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				-3f, -3f, 0f, 0f));
		assertFalse(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				-3f, -3f, -1f, -1f));

		assertFalse(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				-3f, 0f, 4f, 0f));

		assertFalse(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				-3f, -1f, 4f, -1f));

		assertFalse(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				-3f, -1f, -1f, -1f));

		assertFalse(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				-3f, 0f, -2f, 1f));

		assertFalse(Segment2f.intersectsSegmentSegmentWithoutEnds(
				0f, 0f, 1f, 1f,
				10f, 0f, 9f, -1f));

		assertFalse(
				Segment2f.intersectsSegmentSegmentWithoutEnds(
						7f, -5f, 1f, 1f,
						4f, -3f, 1f, 1f));
	}

	/**
	 */
	public static void testIntersectsSegmentSegmentWithEnds() {
		assertTrue(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				0f, .5f, 1f, .5f));
		assertTrue(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				0f, 0f, 1f, 1f));
		assertTrue(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				0f, 0f, 2f, 2f));
		assertTrue(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				0f, 0f, .5f, .5f));
		assertTrue(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				-3f, -3f, .5f, .5f));
		assertTrue(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				-3f, -3f, 0f, 0f));
		assertFalse(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				-3f, -3f, -1f, -1f));

		assertTrue(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				-3f, 0f, 4f, 0f));

		assertFalse(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				-3f, -1f, 4f, -1f));

		assertFalse(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				-3f, -1f, -1f, -1f));

		assertFalse(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				-3f, 0f, -2f, 1f));

		assertFalse(Segment2f.intersectsSegmentSegmentWithEnds(
				0f, 0f, 1f, 1f,
				10f, 0f, 9f, -1f));

		assertTrue(
				Segment2f.intersectsSegmentSegmentWithEnds(
						7f, -5f, 1f, 1f,
						4f, -3f, 1f, 1f));
	}

	@Override
	public void testDistancePoint2D() {
		assertEpsilonEquals(0f, this.r.distance(new Point2f(0f, 0f)));
		assertEpsilonEquals(0f, this.r.distance(new Point2f(.5f, .5f)));
		assertEpsilonEquals(0f, this.r.distance(new Point2f(1f, 1f)));
		
		assertEpsilonEquals(3.733630941f, this.r.distance(new Point2f(2.3f, 4.5f)));

		assertEpsilonEquals(1.414213562f, this.r.distance(new Point2f(2f, 2f)));
	}

	@Override
	public void testDistanceSquaredPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2f(0f, 0f)));
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2f(.5f, .5f)));
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2f(1f, 1f)));
		
		assertEpsilonEquals(13.94f, this.r.distanceSquared(new Point2f(2.3f, 4.5f)));

		assertEpsilonEquals(2f, this.r.distanceSquared(new Point2f(2f, 2f)));
	}

	@Override
	public void testDistanceL1Point2D() {
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2f(0f, 0f)));
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2f(.5f, .5f)));
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2f(1f, 1f)));
		
		assertEpsilonEquals(4.8f, this.r.distanceL1(new Point2f(2.3f, 4.5f)));

		assertEpsilonEquals(2f, this.r.distanceL1(new Point2f(2f, 2f)));
	}

	@Override
	public void testDistanceLinfPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2f(0f, 0f)));
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2f(.5f, .5f)));
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2f(1f, 1f)));
		
		assertEpsilonEquals(3.5f, this.r.distanceLinf(new Point2f(2.3f, 4.5f)));

		assertEpsilonEquals(1f, this.r.distanceLinf(new Point2f(2f, 2f)));
	}

	@Override
	public void testTranslateFloatFloat() {
		this.r.translate(3.4f,  4.5f);
		assertEpsilonEquals(3.4f, this.r.getX1());
		assertEpsilonEquals(4.5f, this.r.getY1());
		assertEpsilonEquals(4.4f, this.r.getX2());
		assertEpsilonEquals(5.5f, this.r.getY2());
	}

	/**
	 */
	public void testSetFloatFloatFloatFloat() {
		this.r.set(3.4f,  4.5f, 5.6f, 6.7f);
		assertEpsilonEquals(3.4f, this.r.getX1());
		assertEpsilonEquals(4.5f, this.r.getY1());
		assertEpsilonEquals(5.6f, this.r.getX2());
		assertEpsilonEquals(6.7f, this.r.getY2());
	}

	/**
	 */
	public void testSetPoint2DPoint2D() {
		this.r.set(new Point2f(3.4f,  4.5f), new Point2f(5.6f, 6.7f));
		assertEpsilonEquals(3.4f, this.r.getX1());
		assertEpsilonEquals(4.5f, this.r.getY1());
		assertEpsilonEquals(5.6f, this.r.getX2());
		assertEpsilonEquals(6.7f, this.r.getY2());
	}

	@Override
	public void testToBoundingBox() {
		Rectangle2f bb = this.r.toBoundingBox();
		assertEpsilonEquals(0f, bb.getMinX());
		assertEpsilonEquals(0f, bb.getMinY());
		assertEpsilonEquals(1f, bb.getMaxX());
		assertEpsilonEquals(1f, bb.getMaxY());
	}

	/**
	 */
	public void testContainsPoint2D() {
		assertTrue(this.r.contains(new Point2f(0f, 0f)));
		assertTrue(this.r.contains(new Point2f(.5f, .5f)));
		assertTrue(this.r.contains(new Point2f(1f, 1f)));
		
		assertFalse(this.r.contains(new Point2f(2.3f, 4.5f)));

		assertFalse(this.r.contains(new Point2f(2f, 2f)));
	}

	/**
	 */
	public void testContainsFloatFloat() {
		assertTrue(this.r.contains(0f, 0f));
		assertTrue(this.r.contains(.5f, .5f));
		assertTrue(this.r.contains(1f, 1f));
		
		assertFalse(this.r.contains(2.3f, 4.5f));

		assertFalse(this.r.contains(2f, 2f));
	}

	/**
	 */
	public void testGetClosestPointTo() {
		Point2D p;
		
		p = this.r.getClosestPointTo(new Point2f(0f,0f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());

		p = this.r.getClosestPointTo(new Point2f(.5f,.5f));
		assertEpsilonEquals(.5f, p.getX());
		assertEpsilonEquals(.5f, p.getY());

		p = this.r.getClosestPointTo(new Point2f(1f,1f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());

		p = this.r.getClosestPointTo(new Point2f(2f,2f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());

		p = this.r.getClosestPointTo(new Point2f(-2f,2f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());

		p = this.r.getClosestPointTo(new Point2f(0.1f,1.2f));
		assertEpsilonEquals(0.65f, p.getX());
		assertEpsilonEquals(0.65f, p.getY());

		p = this.r.getClosestPointTo(new Point2f(10.1f,-.2f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());
	}

	/**
	 */
	public void testGetPathIteratorVoid() {
		PathIterator2f pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0f,0f);
		assertElement(pi, PathElementType.LINE_TO, 1f,1f);
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
		assertElement(pi, PathElementType.LINE_TO, 1f,1f);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3.4f, 4.5f);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3.4f, 4.5f);
		assertElement(pi, PathElementType.LINE_TO, 4.4f, 5.5f);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.r.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0f, 0f);
		assertElement(pi, PathElementType.LINE_TO, 0f, 1.414213562f);
		assertNoElement(pi);
	}

	/**
	 */
    public void testCreateTransformedPathTransform2D() {
    	Segment2f s;
    	Transform2D tr;
    	
    	tr = new Transform2D();    	
    	s = (Segment2f)this.r.createTransformedShape(tr);
		assertEpsilonEquals(0f, s.getX1());
		assertEpsilonEquals(0f, s.getY1());
		assertEpsilonEquals(1f, s.getX2());
		assertEpsilonEquals(1f, s.getY2());

    	tr = new Transform2D();
    	tr.setTranslation(3.4f, 4.5f);
    	s = (Segment2f)this.r.createTransformedShape(tr);
		assertEpsilonEquals(3.4f, s.getX1());
		assertEpsilonEquals(4.5f, s.getY1());
		assertEpsilonEquals(4.4f, s.getX2());
		assertEpsilonEquals(5.5f, s.getY2());

    	tr = new Transform2D();
    	tr.setRotation(MathConstants.PI);
    	s = (Segment2f)this.r.createTransformedShape(tr);
		assertEpsilonEquals(0f, s.getX1());
		assertEpsilonEquals(0f, s.getY1());
		assertEpsilonEquals(-1f, s.getX2());
		assertEpsilonEquals(-1f, s.getY2());

    	tr = new Transform2D();
    	tr.setRotation(MathConstants.QUARTER_PI);
    	s = (Segment2f)this.r.createTransformedShape(tr);
		assertEpsilonEquals(0f, s.getX1());
		assertEpsilonEquals(0f, s.getY1());
		assertEpsilonEquals(0f, s.getX2());
		assertEpsilonEquals(1.414213562f, s.getY2());
    }

	/**
	 */
    public void testTransformTransform2D() {
    	Segment2f s;
    	Transform2D tr;
    	
    	tr = new Transform2D();
    	s = this.r.clone();
    	s.transform(tr);
		assertEpsilonEquals(0f, s.getX1());
		assertEpsilonEquals(0f, s.getY1());
		assertEpsilonEquals(1f, s.getX2());
		assertEpsilonEquals(1f, s.getY2());

    	tr = new Transform2D();
    	tr.setTranslation(3.4f, 4.5f);
    	s = this.r.clone();
    	s.transform(tr);
		assertEpsilonEquals(3.4f, s.getX1());
		assertEpsilonEquals(4.5f, s.getY1());
		assertEpsilonEquals(4.4f, s.getX2());
		assertEpsilonEquals(5.5f, s.getY2());

    	tr = new Transform2D();
    	tr.setRotation(MathConstants.PI);
    	s = this.r.clone();
    	s.transform(tr);
		assertEpsilonEquals(0f, s.getX1());
		assertEpsilonEquals(0f, s.getY1());
		assertEpsilonEquals(-1f, s.getX2());
		assertEpsilonEquals(-1f, s.getY2());

    	tr = new Transform2D();
    	tr.setRotation(MathConstants.QUARTER_PI);
    	s = this.r.clone();
    	s.transform(tr);
		assertEpsilonEquals(0f, s.getX1());
		assertEpsilonEquals(0f, s.getX2());
		assertEpsilonEquals(1.414213562f, s.getY2());
    }

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

	/**
	 */
	public void testSetShape2f() {
		this.r.set(new Rectangle2f(10, 12, 14, 16));
		assertEpsilonEquals(10f, this.r.getX1());
		assertEpsilonEquals(12f, this.r.getY1());
		assertEpsilonEquals(24f, this.r.getX2());
		assertEpsilonEquals(28f, this.r.getY2());
	}
	
}