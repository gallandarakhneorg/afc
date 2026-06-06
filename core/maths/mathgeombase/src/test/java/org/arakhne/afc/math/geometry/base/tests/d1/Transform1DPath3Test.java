/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.base.tests.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d1.Direction1D;
import org.arakhne.afc.math.geometry.base.d1.Point1D;
import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d1.Transform1D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
@DisplayName("Transform1D with 3-element path")
public class Transform1DPath3Test extends AbstractMathTestCase {

	private Transform1D<Segment1DStub> transform;

	private Segment1DStub segment1;
	
	private Segment1DStub segment2;

	private Segment1DStub segment3;

	@BeforeEach
	public void setUp() {
		this.segment1 = new Segment1DStub(1, 2, 34, 48);
		this.segment2 = new Segment1DStub(12, 127, 34, 48);
		this.segment3 = new Segment1DStub(12, 127, 300, 500);
		this.segment1.setLastConnection(this.segment2);
		this.segment2.setFirstConnection(this.segment3);
		this.segment2.setLastConnection(this.segment1);
		this.segment3.setFirstConnection(this.segment2);
		this.transform = new Transform1D();
	}
	
	@Test
	@DisplayName("getCurvilineTransformation w/o set")
	public void getCurvilineTransformation() {
		assertEpsilonEquals(0., this.transform.getCurvilineTransformation());
	}

	@Pure
	@DisplayName("getShiftTransformation w/o set")
	public void getShiftTransformation() {
		assertEpsilonEquals(0., this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("isIdentity w/o set")
	public void isIdentity() {
        assertTrue(this.transform.isIdentity());
	}

	@Test
	@DisplayName("isIdentity w/ set")
	public void isIdentity_set() {
		this.transform.setTranslation(12.5, -4.6);
		assertFalse(this.transform.isIdentity());
	}

	@Test
	@DisplayName("isIdentity w/ identity set")
	public void isIdentity_identityset() {
		this.transform.setTranslation(0., 0.);
		assertTrue(this.transform.isIdentity());
	}

	@Test
	@DisplayName("hasPath w/o set")
	public void hasPath() {
		assertFalse(this.transform.hasPath());
	}

	@Test
	@DisplayName("getPath w/o set")
	public void getPath() {
		assertTrue(this.transform.getPath().isEmpty());
	}

	@Test
	@DisplayName("getFirstSegmentPathDirection w/o set")
	public void getFirstSegmentPathDirection() {
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
	}

	@Test
	@DisplayName("setPath(segments)")
	public void setPath_segments() {
		this.transform.setPath(Arrays.asList(this.segment1, this.segment2, this.segment3));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(0., this.transform.getCurvilineTransformation());
		assertEpsilonEquals(0., this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setPath(segments, null)")
	public void setPath_segmentsnull() {
		this.transform.setPath(Arrays.asList(this.segment1, this.segment2, this.segment3), null);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(0., this.transform.getCurvilineTransformation());
		assertEpsilonEquals(0., this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setPath(segments, SEGMENT_DIRECTION)")
	public void setPath_segmentssegmentdirection() {
		this.transform.setPath(Arrays.asList(this.segment1, this.segment2, this.segment3), Direction1D.SEGMENT_DIRECTION);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(0., this.transform.getCurvilineTransformation());
		assertEpsilonEquals(0., this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setPath(segments, REVERSED_DIRECTION)")
	public void setPath_segmentsreverseddirection() {
		this.transform.setPath(Arrays.asList(this.segment1, this.segment2, this.segment3), Direction1D.REVERTED_DIRECTION);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(0., this.transform.getCurvilineTransformation());
		assertEpsilonEquals(0., this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setPath(segments, REVERSED_DIRECTION)")
	public void setPath_segmentsbothdirection() {
		this.transform.setPath(Arrays.asList(this.segment1, this.segment2, this.segment3), Direction1D.BOTH_DIRECTIONS);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(0., this.transform.getCurvilineTransformation());
		assertEpsilonEquals(0., this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setIdentity w/o set")
	public void setIdentity_noset() {
		this.transform.setIdentity();
		assertTrue(this.transform.isIdentity());
		assertEpsilonEquals(0., this.transform.getCurvilineTransformation());
		assertEpsilonEquals(0., this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setIdentity w/ set")
	public void setIdentity_set() {
		this.transform.setTranslation(12.5, -4.6);
		assertFalse(this.transform.isIdentity());
		this.transform.setIdentity();
		assertTrue(this.transform.isIdentity());
		assertEpsilonEquals(0., this.transform.getCurvilineTransformation());
		assertEpsilonEquals(0., this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(124.5, 458.4)")
	public void setTranslation_doubledouble() {
		this.transform.setTranslation(124.5, 458.4);
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(Tuple2D)")
	public void setTranslation_tuple() {
		var tuple = new Vector1DStub(124.5, 458.4, this.segment1);
		this.transform.setTranslation(tuple);
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, 124.5, 458.4)")
	public void setTranslation_segmentsdoubledouble() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, tuple)")
	public void setTranslation_segmentstuple() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, null, 124.5, 458.4)")
	public void setTranslation_segmentsnulldoubledouble() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), null, 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, null, tuple)")
	public void setTranslation_segmentsnulltuple() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3),
				null, new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, SEGMENT_DIRECTION, 124.5, 458.4)")
	public void setTranslation_segmentssegmentdirectiondoubledouble() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.SEGMENT_DIRECTION, 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, SEGMENT_DIRECTION, tuple)")
	public void setTranslation_segmentsssegmentdirectiontuple() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.SEGMENT_DIRECTION, new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, REVERTED_DIRECTION, 124.5, 458.4)")
	public void setTranslation_segmentsreverteddirectiondoubledouble() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.REVERTED_DIRECTION, 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, REVERTED_DIRECTION, tuple)")
	public void setTranslation_segmentsreverteddirectiontuple() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.REVERTED_DIRECTION, new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, BOTH_DIRECTIONS, 124.5, 458.4)")
	public void setTranslation_segmentsbothdirectionsdoubledouble() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.BOTH_DIRECTIONS, 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("setTranslation(segments, BOTH_DIRECTIONS, tuple)")
	public void setTranslation_segmentsbothdirectionstuple() {
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.BOTH_DIRECTIONS, new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(124.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(458.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(124.5, 458.4)")
	public void translate_doubledouble() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(124.5, 458.4);
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(Tuple2D)")
	public void translate_tuple() {
		this.transform.setTranslation(4, 6);
		var tuple = new Vector1DStub(124.5, 458.4, this.segment1);
		this.transform.translate(tuple);
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, 124.5, 458.4)")
	public void translate_segmentsdoubledouble() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3), 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, tuple)")
	public void translate_segmentstuple() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3), new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, null, 124.5, 458.4)")
	public void translate_segmentsnulldoubledouble() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3), null, 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, null, tuple)")
	public void translate_segmentsnulltuple() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3),
				null, new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, SEGMENT_DIRECTION, 124.5, 458.4)")
	public void translate_segmentssegmentdirectiondoubledouble() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.SEGMENT_DIRECTION, 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, SEGMENT_DIRECTION, tuple)")
	public void translate_segmentsssegmentdirectiontuple() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.SEGMENT_DIRECTION, new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, REVERTED_DIRECTION, 124.5, 458.4)")
	public void translate_segmentsreverteddirectiondoubledouble() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.REVERTED_DIRECTION, 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, REVERTED_DIRECTION, tuple)")
	public void translate_segmentsreverteddirectiontuple() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.REVERTED_DIRECTION, new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, BOTH_DIRECTIONS, 124.5, 458.4)")
	public void translate_segmentsbothdirectionsdoubledouble() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.BOTH_DIRECTIONS, 124.5, 458.4);
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("translate(segments, BOTH_DIRECTIONS, tuple)")
	public void translate_segmentsbothdirectionstuple() {
		this.transform.setTranslation(4, 6);
		this.transform.translate(Arrays.asList(this.segment1, this.segment2, this.segment3),
				Direction1D.BOTH_DIRECTIONS, new Vector1DStub(124.5, 458.4, this.segment1));
		assertTrue(this.transform.hasPath());
		var actual = this.transform.getPath();
		assertEquals(3, actual.size());
		assertSame(this.segment1, actual.get(0));
		assertSame(this.segment2, actual.get(1));
		assertSame(this.segment3, actual.get(2));
		assertSame(Direction1D.SEGMENT_DIRECTION, this.transform.getFirstSegmentPathDirection());
		assertEpsilonEquals(128.5, this.transform.getCurvilineTransformation());
		assertEpsilonEquals(464.4, this.transform.getShiftTransformation());
	}

	@Test
	@DisplayName("transform({s1, 0, 0}) w/o set")
	public void transform_s1_0_0_noset() {
		var pt = new Point1DStub(0., 0., this.segment1);
		var index = this.transform.transform(pt);
		assertEquals(0, index);
		assertEpsilonEquals(0., pt.getX());
		assertEpsilonEquals(0., pt.getY());
	}

	@Test
	@DisplayName("transform({s1, 2, 4}) w/o set")
	public void transform_s1_2_4_noset() {
		var pt = new Point1DStub(2., 4., this.segment1);
		var index = this.transform.transform(pt);
		assertEquals(0, index);
		assertEpsilonEquals(2., pt.getX());
		assertEpsilonEquals(4., pt.getY());
	}

	@Test
	@DisplayName("transform({s2, 0, 0}) w/o set")
	public void transform_s2_0_0_noset() {
		var pt = new Point1DStub(0., 0., this.segment2);
		var index = this.transform.transform(pt);
		assertEquals(0, index);
		assertEpsilonEquals(0., pt.getX());
		assertEpsilonEquals(0., pt.getY());
	}

	@Test
	@DisplayName("transform({s2, 2, 4}) w/o set")
	public void transform_s2_2_4_noset() {
		var pt = new Point1DStub(2., 4., this.segment2);
		var index = this.transform.transform(pt);
		assertEquals(0, index);
		assertEpsilonEquals(2., pt.getX());
		assertEpsilonEquals(4., pt.getY());
	}

	@Test
	@DisplayName("transform({s1, 1, 2}) to s1 w/ set")
	public void transform_s1_1_2_s1_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(1, 2, this.segment1);
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 2.5, -6.3);
		var index = this.transform.transform(pt);
		assertEquals(0, index);
		assertEpsilonEquals(3.5, pt.getX());
		assertEpsilonEquals(-4.3, pt.getY());
	}

	@Test
	@DisplayName("transform({s1, 0, 0}) to s2 w/ set")
	public void transform_s1_0_0_s2_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(0., 0., this.segment1);
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 60, -6.3);
		var index = this.transform.transform(pt);
		assertEquals(1, index);
		assertEpsilonEquals(78.6188, pt.getX());
		assertEpsilonEquals(6.3, pt.getY());
	}

	@Test
	@DisplayName("transform({s1, 2, 4}) to s2 w/ set")
	public void transform_s1_2_4_s2_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(2., 4., this.segment1);
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 60, -6.3);
		var index = this.transform.transform(pt);
		assertEquals(1, index);
		assertEpsilonEquals(76.6188, pt.getX());
		assertEpsilonEquals(2.3, pt.getY());
	}

	@Test
	@DisplayName("transform({s1, 0, 0}) to s3 w/ set")
	public void transform_s1_0_0_s3_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(0., 0., this.segment1);
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 150., -6.3);
		var index = this.transform.transform(pt);
		assertEquals(2, index);
		assertEpsilonEquals(11.381183247, pt.getX());
		assertEpsilonEquals(-6.3, pt.getY());
	}
	
	@Test
	@DisplayName("transform({s1, 2, 4}) to s3 w/ set")
	public void transform_s1_2_4_s3_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(2., 4., this.segment1);
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 150., -6.3);
		var index = this.transform.transform(pt);
		assertEquals(2, index);
		assertEpsilonEquals(13.381183247, pt.getX());
		assertEpsilonEquals(-2.3, pt.getY());
	}

	@Test
	@DisplayName("transform({s1, 0, 0}, move) w/o set")
	public void transform_move_s1_0_0_noset() {
		var pt = new Point1DStub(0., 0., this.segment1);
		var move = new InnerComputationVector2D();
		var index = this.transform.transform(pt, move);
		assertEquals(0, index);
		assertEpsilonEquals(0., pt.getX());
		assertEpsilonEquals(0., pt.getY());
		assertEpsilonEquals(0., move.getX());
		assertEpsilonEquals(0., move.getY());
	}

	@Test
	@DisplayName("transform({s1, 2, 4}, move) w/o set")
	public void transform_move_s1_2_4_noset() {
		var pt = new Point1DStub(2., 4., this.segment1);
		var move = new InnerComputationVector2D();
		var index = this.transform.transform(pt, move);
		assertEquals(0, index);
		assertEpsilonEquals(2., pt.getX());
		assertEpsilonEquals(4., pt.getY());
		assertEpsilonEquals(0., move.getX());
		assertEpsilonEquals(0., move.getY());
	}

	@Test
	@DisplayName("transform({s2, 0, 0}, move) w/o set")
	public void transform_move_s2_0_0_noset() {
		var pt = new Point1DStub(0., 0., this.segment2);
		var move = new InnerComputationVector2D();
		var index = this.transform.transform(pt, move);
		assertEquals(0, index);
		assertEpsilonEquals(0., pt.getX());
		assertEpsilonEquals(0., pt.getY());
		assertEpsilonEquals(0., move.getX());
		assertEpsilonEquals(0., move.getY());
	}

	@Test
	@DisplayName("transform({s2, 2, 4}, move) w/o set")
	public void transform_move_s2_2_4_noset() {
		var pt = new Point1DStub(2., 4., this.segment2);
		var move = new InnerComputationVector2D();
		var index = this.transform.transform(pt, move);
		assertEquals(0, index);
		assertEpsilonEquals(2., pt.getX());
		assertEpsilonEquals(4., pt.getY());
		assertEpsilonEquals(0., move.getX());
		assertEpsilonEquals(0., move.getY());
	}

	@Test
	@DisplayName("transform({s1, 1, 2}, move) to s1 w/ set")
	public void transform_move_s1_1_2_s1_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(1, 2, this.segment1);
		var move = new InnerComputationVector2D();
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 2.5, -6.3);
		var index = this.transform.transform(pt, move);
		assertEquals(0, index);
		assertEpsilonEquals(3.5, pt.getX());
		assertEpsilonEquals(-4.3, pt.getY());
		assertEpsilonEquals(2.5, move.getX());
		assertEpsilonEquals(6.3, move.getY());
	}

	@Test
	@DisplayName("transform({s1, 0, 0}, move) to s2 w/ set")
	public void transform_move_s1_0_0_s2_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(0., 0., this.segment1);
		var move = new InnerComputationVector2D();
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 60, -6.3);
		var index = this.transform.transform(pt, move);
		assertEquals(1, index);
		assertEpsilonEquals(78.6188, pt.getX());
		assertEpsilonEquals(6.3, pt.getY());
		assertEpsilonEquals(60., move.getX());
		assertEpsilonEquals(6.3, move.getY());
	}

	@Test
	@DisplayName("transform({s1, 2, 4}, move) to s2 w/ set")
	public void transform_move_s1_2_4_s2_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(2., 4., this.segment1);
		var move = new InnerComputationVector2D();
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 60, -6.3);
		var index = this.transform.transform(pt, move);
		assertEquals(1, index);
		assertEpsilonEquals(76.6188, pt.getX());
		assertEpsilonEquals(2.3, pt.getY());
		assertEpsilonEquals(60., move.getX());
		assertEpsilonEquals(6.3, move.getY());
	}

	@Test
	@DisplayName("transform({s1, 0, 0}, move) to s3 w/ set")
	public void transform_move_s1_0_0_s3_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(0., 0., this.segment1);
		var move = new InnerComputationVector2D();
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 150., -6.3);
		var index = this.transform.transform(pt, move);
		assertEquals(2, index);
		assertEpsilonEquals(11.381183247, pt.getX());
		assertEpsilonEquals(-6.3, pt.getY());
		assertEpsilonEquals(150., move.getX());
		assertEpsilonEquals(6.3, move.getY());
	}
	
	@Test
	@DisplayName("transform({s1, 2, 4}, move) to s3 w/ se")
	public void transform_move_s1_2_4_s3_set() {
		var st = this.segment1.getFirstPoint();
		var pt = new Point1DStub(2., 4., this.segment1);
		var move = new InnerComputationVector2D();
		this.transform.setTranslation(Arrays.asList(this.segment1, this.segment2, this.segment3), 150., -6.3);
		var index = this.transform.transform(pt, move);
		assertEquals(2, index);
		assertEpsilonEquals(13.381183247, pt.getX());
		assertEpsilonEquals(-2.3, pt.getY());
		assertEpsilonEquals(150., move.getX());
		assertEpsilonEquals(6.3, move.getY());
	}

	@Test
	@DisplayName("toTransform2D(s1) w/o set")
	public void toTransform2D_segment1_noset() {
		var tr = this.transform.toTransform2D(this.segment1);
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(0., tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(0., tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(s2) w/o set")
	public void toTransform2D_segment2_noset() {
		var tr = this.transform.toTransform2D(this.segment2);
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(0., tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(0., tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(s3) w/o set")
	public void toTransform2D_segment3_noset() {
		var tr = this.transform.toTransform2D(this.segment3);
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(0., tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(0., tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(double,double,double,double) w/ s1")
	public void toTransform2D_doubledoubledoubledouble_segment1() {
		this.transform.setTranslation(30, -6.3);
		var tr = this.transform.toTransform2D(1, 2, 34, 48);
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(22.60622724251894, tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(20.70382790354465, tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(double,double,double,double) w/o s2")
	public void toTransform2D_doubledoubledoubledouble_segment2() {
		this.transform.setTranslation(30, -6.3);
		var tr = this.transform.toTransform2D(12, 127, 34, 48);
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(1.979121129718103e+00, tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(-3.059040829335077e+01, tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(double,double,double,double) w/o s3")
	public void toTransform2D_doubledoubledoubledouble_segment3() {
		this.transform.setTranslation(30, -6.3);
		var tr = this.transform.toTransform2D(12, 127, 300, 500);
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(23.32092976075642, tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(19.89533199255206, tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(Point2D,Point2D) w/ s1")
	public void toTransform2D_Point2DPoint2D_segment1() {
		this.transform.setTranslation(30, -6.3);
		var tr = this.transform.toTransform2D(new InnerComputationPoint2D(1, 2), new InnerComputationPoint2D(34, 48));
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(22.60622724251894, tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(20.70382790354465, tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(Point2D,Point2D) w/o s2")
	public void toTransform2D_Point2DPoint2D_segment2() {
		this.transform.setTranslation(30, -6.3);
		var tr = this.transform.toTransform2D(new InnerComputationPoint2D(12, 127), new InnerComputationPoint2D(34, 48));
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(1.979121129718103e+00, tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(-3.059040829335077e+01, tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(Point2D,Point2D) w/o s3")
	public void toTransform2D_Point2DPoint2D_segment3() {
		this.transform.setTranslation(30, -6.3);
		var tr = this.transform.toTransform2D(new InnerComputationPoint2D(12, 127), new InnerComputationPoint2D(300, 500));
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(23.32092976075642, tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(19.89533199255206, tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(Segment1D) w/ s1")
	public void toTransform2D_Segment1D_segment1() {
		this.transform.setTranslation(30, -6.3);
		var tr = this.transform.toTransform2D(this.segment1);
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(22.60622724251894, tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(20.70382790354465, tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(Segment1D) w/o s2")
	public void toTransform2D_Segment1D_segment2() {
		this.transform.setTranslation(30, -6.3);
		var tr = this.transform.toTransform2D(this.segment2);
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(1.979121129718103e+00, tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(-3.059040829335077e+01, tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

	@Test
	@DisplayName("toTransform2D(Segment1D) w/o s3")
	public void toTransform2D_Segment1D_segment3() {
		this.transform.setTranslation(30, -6.3);
		var tr = this.transform.toTransform2D(this.segment3);
		assertEpsilonEquals(1., tr.getM00());
		assertEpsilonEquals(0., tr.getM01());
		assertEpsilonEquals(23.32092976075642, tr.getM02());
		assertEpsilonEquals(0., tr.getM10());
		assertEpsilonEquals(1., tr.getM11());
		assertEpsilonEquals(19.89533199255206, tr.getM12());
		assertEpsilonEquals(0., tr.getM20());
		assertEpsilonEquals(0., tr.getM21());
		assertEpsilonEquals(1., tr.getM22());
	}

}