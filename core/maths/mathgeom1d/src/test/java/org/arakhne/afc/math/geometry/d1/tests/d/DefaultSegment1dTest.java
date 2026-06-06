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

package org.arakhne.afc.math.geometry.d1.tests.d;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.d1.d.DefaultSegment1d;
import org.arakhne.afc.math.geometry.d1.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test of basic implementation of a 1.5D segment.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings("all")
public class DefaultSegment1dTest extends AbstractMathTestCase {

	private DefaultSegment1d segment;

	@BeforeEach
	public void setUp() {
		this.segment = new DefaultSegment1d(new Point2d(125.215, 953.1), new Point2d(654.36, 1023.7));
	}
	
	@Test
	@DisplayName("getFirstPoint")
	public void getFirstPoint() {
		assertEpsilonEquals(new Point2d(125.215, 953.1), this.segment.getFirstPoint());
	}

	@Test
	@DisplayName("getLastPoint")
	public void getLastPoint() {
		assertEpsilonEquals(new Point2d(654.36, 1023.7), this.segment.getLastPoint());
	}

	@Test
	@DisplayName("getLength")
	public void getLength() {
		assertEpsilonEquals(533.8340482069310, this.segment.getLength());
	}

	@Test
	@DisplayName("setLength")
	public void setLength() {
		this.segment.setLength(100.);
		assertEpsilonEquals(new Point2d(125.215, 953.1), this.segment.getFirstPoint());
		assertEpsilonEquals(new Point2d(654.36, 1023.7), this.segment.getLastPoint());
		assertEpsilonEquals(100., this.segment.getLength());
	}

	@Test
	@DisplayName("getTangentAt")
	public void getTangentAt() {
		var r = new Random();
		var n = r.nextInt(10) + 25;
		for (var i = 0; i < n; ++i) {
			assertEpsilonEquals(new Vector2d(0.991216281122044, 0.132250837572341), this.segment.getTangentAt(r.nextDouble()*50. - 50.));
		}
	}

	@Test
	@DisplayName("projectsOnPlane(double, Point2D, Vector2D)")
	public void projectsOnPlane_doublePoint2DVector2D() {
		var position = new Point2d();
		var tangent = new Vector2d();
		this.segment.projectsOnPlane(.25, position, tangent);
		assertEpsilonEquals(new Point2d(125.4628040702805, 953.1330627093931), position);
		assertEpsilonEquals(new Vector2d(0.991216281122044, 0.132250837572341), tangent);
	}

	@Test
	@DisplayName("projectsOnPlane(double, double, Point2D, Vector2D)")
	public void projectsOnPlane_doubledoublePoint2DVector2D() {
		var position = new Point2d();
		var tangent = new Vector2d();
		this.segment.projectsOnPlane(12., -5.6, position, tangent);
		assertEpsilonEquals(new Point2d(137.8502000638696, 949.1361988765847), position);
		assertEpsilonEquals(new Vector2d(0.991216281122044, 0.132250837572341), tangent);
	}

	@Test
	@DisplayName("isFirstPointConnectedTo(segment)")
	public void isFirstPointConnectedTo_segment() {
		assertTrue(this.segment.isFirstPointConnectedTo(this.segment));
	}

	@Test
	@DisplayName("isFirstPointConnectedTo(same segment)")
	public void isFirstPointConnectedTo_sameSegment() {
		var sameSegment = new DefaultSegment1d(new Point2d(125.215, 953.1), new Point2d(654.36, 1023.7));
		assertTrue(this.segment.isFirstPointConnectedTo(sameSegment));
	}

	@Test
	@DisplayName("isFirstPointConnectedTo(reversed segment)")
	public void isFirstPointConnectedTo_reversedSegment() {
		var sameSegment = new DefaultSegment1d(new Point2d(654.36, 1023.7), new Point2d(125.215, 953.1));
		assertTrue(this.segment.isFirstPointConnectedTo(sameSegment));
	}

	@Test
	@DisplayName("isFirstPointConnectedTo(connected first)")
	public void isFirstPointConnectedTo_connectedFirst() {
		var sameSegment = new DefaultSegment1d(new Point2d(125.215, 953.1), new Point2d(10000., 10000.));
		assertTrue(this.segment.isFirstPointConnectedTo(sameSegment));
	}

	@Test
	@DisplayName("isFirstPointConnectedTo(connected last)")
	public void isFirstPointConnectedTo_connectedLast() {
		var sameSegment = new DefaultSegment1d(new Point2d(10000., 10000.), new Point2d(125.215, 953.1));
		assertTrue(this.segment.isFirstPointConnectedTo(sameSegment));
	}

	@Test
	@DisplayName("isLastPointConnectedTo(segment)")
	public void isLastPointConnectedTo_segment() {
		assertTrue(this.segment.isLastPointConnectedTo(this.segment));
	}

	@Test
	@DisplayName("isLastPointConnectedTo(same segment)")
	public void isLastPointConnectedTo_sameSegment() {
		var sameSegment = new DefaultSegment1d(new Point2d(125.215, 953.1), new Point2d(654.36, 1023.7));
		assertTrue(this.segment.isLastPointConnectedTo(sameSegment));
	}

	@Test
	@DisplayName("isLastPointConnectedTo(reversed segment)")
	public void isLastPointConnectedTo_reversedSegment() {
		var sameSegment = new DefaultSegment1d(new Point2d(654.36, 1023.7), new Point2d(125.215, 953.1));
		assertTrue(this.segment.isLastPointConnectedTo(sameSegment));
	}

	@Test
	@DisplayName("isLastPointConnectedTo(connected first)")
	public void isLastPointConnectedTo_connectedFirst() {
		var sameSegment = new DefaultSegment1d(new Point2d(654.36, 1023.7), new Point2d(10000., 10000.));
		assertTrue(this.segment.isLastPointConnectedTo(sameSegment));
	}

	@Test
	@DisplayName("isLastPointConnectedTo(connected last)")
	public void isLastPointConnectedTo_connectedLast() {
		var sameSegment = new DefaultSegment1d(new Point2d(10000., 10000.), new Point2d(654.36, 1023.7));
		assertTrue(this.segment.isLastPointConnectedTo(sameSegment));
	}

}

