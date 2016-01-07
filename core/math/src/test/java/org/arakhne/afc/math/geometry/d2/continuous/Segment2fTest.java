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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.continuous.Path2f;
import org.arakhne.afc.math.geometry.d2.continuous.PathIterator2f;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Rectangle2f;
import org.arakhne.afc.math.geometry.d2.continuous.Segment2f;
import org.arakhne.afc.math.geometry.d2.continuous.Transform2D;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class Segment2fTest extends AbstractShape2fTestCase<Segment2f> {
	
	@Override
	protected Segment2f createShape() { 
		return new Segment2f(0f, 0f, 1f, 1f);
	}
	
	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.r.isEmpty());
		this.r.clear();
		assertTrue(this.r.isEmpty());
	}

	@Test
	@Override
	public void clear() {
		this.r.clear();
		assertEpsilonEquals(0f, this.r.getX1());
		assertEpsilonEquals(0f, this.r.getY1());
		assertEpsilonEquals(0f, this.r.getX2());
		assertEpsilonEquals(0f, this.r.getY2());
	}

	@Test
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
	@Test
	public void computeCrossingsFromPoint() {
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
	@Test
	public void computeCrossingsFromRect() {
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
	@Test
	public void computeCrossingsFromSegment_0011() {
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
	@Test
	public void computeCrossingsFromSegment_1100() {
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
	@Test
	public void computeCrossingsFromSegment_Others() {
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
	@Test
	public void computeCrossingsFromSegment_0110() {
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
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						20f, -5f, -1f, 1f));

		assertEquals(
				MathConstants.SHAPE_INTERSECTS,
				Segment2f.computeCrossingsFromSegment(
						0,
						0f, 1f, 1f, 0f,
						5f, 10f, .25f, .5f));
	}

	/**
	 */
	@Test
	public void computeCrossingsFromSegment_1001() {
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
				MathConstants.SHAPE_INTERSECTS,
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
	@Test
	public void computeCrossingsFromEllipse_PositiveTest() {
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
	@Test
	public void computeCrossingsFromEllipse_NegativeTest() {
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
	@Test
	public void computeCrossingsFromCircle_PositiveTest() {
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
	@Test
	public void computeCrossingsFromCircle_NegativeTest() {
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
	@Test
	public void intersectsLineLine() {
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
	@Test
	public void intersectsSegmentLine() {
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
	@Test
	public void intersectsSegmentSegmentWithoutEnds() {
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
	@Test
	public void intersectsSegmentSegmentWithEnds() {
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

	@Test
	@Override
	public void distancePoint2D() {
		assertEpsilonEquals(0f, this.r.distance(new Point2f(0f, 0f)));
		assertEpsilonEquals(0f, this.r.distance(new Point2f(.5f, .5f)));
		assertEpsilonEquals(0f, this.r.distance(new Point2f(1f, 1f)));
		
		assertEpsilonEquals(3.733630941f, this.r.distance(new Point2f(2.3f, 4.5f)));

		assertEpsilonEquals(1.414213562f, this.r.distance(new Point2f(2f, 2f)));
	}

	@Test
	@Override
	public void distanceSquaredPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2f(0f, 0f)));
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2f(.5f, .5f)));
		assertEpsilonEquals(0f, this.r.distanceSquared(new Point2f(1f, 1f)));
		
		assertEpsilonEquals(13.94f, this.r.distanceSquared(new Point2f(2.3f, 4.5f)));

		assertEpsilonEquals(2f, this.r.distanceSquared(new Point2f(2f, 2f)));
	}

	@Test
	@Override
	public void distanceL1Point2D() {
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2f(0f, 0f)));
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2f(.5f, .5f)));
		assertEpsilonEquals(0f, this.r.distanceL1(new Point2f(1f, 1f)));
		
		assertEpsilonEquals(4.8f, this.r.distanceL1(new Point2f(2.3f, 4.5f)));

		assertEpsilonEquals(2f, this.r.distanceL1(new Point2f(2f, 2f)));
	}

	@Test
	@Override
	public void distanceLinfPoint2D() {
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2f(0f, 0f)));
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2f(.5f, .5f)));
		assertEpsilonEquals(0f, this.r.distanceLinf(new Point2f(1f, 1f)));
		
		assertEpsilonEquals(3.5f, this.r.distanceLinf(new Point2f(2.3f, 4.5f)));

		assertEpsilonEquals(1f, this.r.distanceLinf(new Point2f(2f, 2f)));
	}

	@Test
	@Override
	public void translateFloatFloat() {
		this.r.translate(3.4f,  4.5f);
		assertEpsilonEquals(3.4f, this.r.getX1());
		assertEpsilonEquals(4.5f, this.r.getY1());
		assertEpsilonEquals(4.4f, this.r.getX2());
		assertEpsilonEquals(5.5f, this.r.getY2());
	}

	/**
	 */
	@Test
	public void setFloatFloatFloatFloat() {
		this.r.set(3.4f,  4.5f, 5.6f, 6.7f);
		assertEpsilonEquals(3.4f, this.r.getX1());
		assertEpsilonEquals(4.5f, this.r.getY1());
		assertEpsilonEquals(5.6f, this.r.getX2());
		assertEpsilonEquals(6.7f, this.r.getY2());
	}

	/**
	 */
	@Test
	public void setPoint2DPoint2D() {
		this.r.set(new Point2f(3.4f,  4.5f), new Point2f(5.6f, 6.7f));
		assertEpsilonEquals(3.4f, this.r.getX1());
		assertEpsilonEquals(4.5f, this.r.getY1());
		assertEpsilonEquals(5.6f, this.r.getX2());
		assertEpsilonEquals(6.7f, this.r.getY2());
	}

	@Test
	@Override
	public void toBoundingBox() {
		AbstractRectangle2F bb = this.r.toBoundingBox();
		assertEpsilonEquals(0f, bb.getMinX());
		assertEpsilonEquals(0f, bb.getMinY());
		assertEpsilonEquals(1f, bb.getMaxX());
		assertEpsilonEquals(1f, bb.getMaxY());
	}

	/**
	 */
	@Test
	public void containsPoint2D() {
		assertTrue(this.r.contains(new Point2f(0f, 0f)));
		assertTrue(this.r.contains(new Point2f(.5f, .5f)));
		assertTrue(this.r.contains(new Point2f(1f, 1f)));
		
		assertFalse(this.r.contains(new Point2f(2.3f, 4.5f)));

		assertFalse(this.r.contains(new Point2f(2f, 2f)));
	}

	/**
	 */
	@Test
	public void containsFloatFloat() {
		assertTrue(this.r.contains(0f, 0f));
		assertTrue(this.r.contains(.5f, .5f));
		assertTrue(this.r.contains(1f, 1f));
		
		assertFalse(this.r.contains(2.3f, 4.5f));

		assertFalse(this.r.contains(2f, 2f));
	}

	/**
	 */
	@Test
	public void getClosestPointTo() {
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
	@Test
	public void getFarthestPointTo() {
		Point2D p;
		
		p = this.r.getFarthestPointTo(new Point2f(0f,0f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());

		p = this.r.getFarthestPointTo(new Point2f(.5f,.5f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());

		p = this.r.getFarthestPointTo(new Point2f(1f,1f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());

		p = this.r.getFarthestPointTo(new Point2f(2f,2f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());

		p = this.r.getFarthestPointTo(new Point2f(-2f,2f));
		assertEpsilonEquals(1f, p.getX());
		assertEpsilonEquals(1f, p.getY());

		p = this.r.getFarthestPointTo(new Point2f(0.1f,1.2f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());

		p = this.r.getFarthestPointTo(new Point2f(10.1f,-.2f));
		assertEpsilonEquals(0f, p.getX());
		assertEpsilonEquals(0f, p.getY());
	}

	/**
	 */
	@Test
	public void getPathIteratorVoid() {
		PathIterator2f pi = this.r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0f,0f);
		assertElement(pi, PathElementType.LINE_TO, 1f,1f);
		assertNoElement(pi);
	}

	/**
	 */
	@Test
	public void getPathIteratorTransform2D() {
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
	@Test
    public void createTransformedPathTransform2D() {
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
	@Test
    public void transformTransform2D() {
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
		this.r.set(new Rectangle2f(10f, 12f, 14f, 16f));
		assertEpsilonEquals(10f, this.r.getX1());
		assertEpsilonEquals(12f, this.r.getY1());
		assertEpsilonEquals(24f, this.r.getX2());
		assertEpsilonEquals(28f, this.r.getY2());
		
		this.r.set(new Rectangle2f(20f, 20f, 20f, 20f));
		assertEpsilonEquals(20f, this.r.getX1());
		assertEpsilonEquals(20f, this.r.getY1());
		assertEpsilonEquals(40f, this.r.getX2());
		assertEpsilonEquals(40f, this.r.getY2());
		
		
		this.r.set(new Rectangle2d(-10f, 19f, 13f, 1f));
		System.out.println(r);
		assertEpsilonEquals(-10f, this.r.getX1());
		assertEpsilonEquals(19f, this.r.getY1());
		assertEpsilonEquals(3f, this.r.getX2());
		assertEpsilonEquals(20f, this.r.getY2());
	}
	
	@Test
	public void interpolateDoubleDoubleDoubleDoubleDouble() {
		assertEpsilonEquals(new Point2f(1, 2),
				Segment2f.interpolate(1.f, 2.f, 3.f, 4.f, 0.f));
		assertEpsilonEquals(new Point2f(1.5f, 2.5f),
				Segment2f.interpolate(1.f, 2.f, 3.f, 4.f, .25f));
		assertEpsilonEquals(new Point2f(2, 3.f),
				Segment2f.interpolate(1.f, 2.f, 3.f, 4.f, .5f));
		assertEpsilonEquals(new Point2f(2.5f, 3.5f),
				Segment2f.interpolate(1.f, 2.f, 3.f, 4.f, .75f));
		assertEpsilonEquals(new Point2f(3, 4),
				Segment2f.interpolate(1.f, 2.f, 3.f, 4.f, 1.f));
	}
	
	@Test
	public void ccwDoubleDoubleDoubleDoubleDoubleDoubleBoolean() {
		Point2f p1 = new Point2f(-100.f, -100.f);
		Point2f p2 = new Point2f(100.f, 100.f);
		Point2f p3;

		p3 = new Point2f(p1);
		assertEquals(
				0,
				Segment2f.ccw(
						p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				0,
				Segment2f.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(p2);
		assertEquals(
				0,
				Segment2f.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				0,
				Segment2f.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(0.f, 0.f);
		assertEquals(
				0,
				Segment2f.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				0,
				Segment2f.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(0.f, 0.f);
		assertEquals(
				0,
				Segment2f.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				0,
				Segment2f.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(-200.f, -200.f);
		assertEquals(
				-1,
				Segment2f.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				1,
				Segment2f.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(200.f, 200.f);
		assertEquals(
				1,
				Segment2f.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				-1,
				Segment2f.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(-200.f, 200.f);
		assertEquals(
				-1,
				Segment2f.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				1,
				Segment2f.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));

		p3 = new Point2f(200.f, -200.f);
		assertEquals(
				1,
				Segment2f.ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(),
						p3.getX(), p3.getY(), 0));
		assertEquals(
				-1,
				Segment2f.ccw(p2.getX(), p2.getY(), p1.getX(), p1.getY(),
						p3.getX(), p3.getY(), 0));
	}

	@Test
	public void computeSidePointLineDoubleDoubleDoubleDoubleDoubleDoubleBoolean() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computeRelativeDistanceLinePointDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computeProjectedPointOnLineDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void isPointClosedToSegment() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void isPointClosedToLine() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void distanceLinePoint() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void distanceSegmentPoint() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void distanceSquaredSegmentPoint() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void distanceSquaredLinePoint() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computeLineLineIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computeLineLineIntersectionFactorDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computeSegmentSegmentIntersectionDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void computeSegmentSegmentIntersectionFactorDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void isParallelLinesDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

	@Test
	public void isColinearLinesDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		throw new UnsupportedOperationException();
	}

}