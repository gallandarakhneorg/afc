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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.base.d1.Direction1D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class Direction1DTest extends AbstractMathTestCase {

	@Test
	@DisplayName("isSegmentDirection")
	public void isSegmentDirection() {
		assertTrue(Direction1D.SEGMENT_DIRECTION.isSegmentDirection());
		assertFalse(Direction1D.REVERTED_DIRECTION.isSegmentDirection());
		assertTrue(Direction1D.BOTH_DIRECTIONS.isSegmentDirection());
	}

	@Test
	@DisplayName("isRevertedSegmentDirection")
	public void isRevertedSegmentDirection() {
		assertFalse(Direction1D.SEGMENT_DIRECTION.isRevertedSegmentDirection());
		assertTrue(Direction1D.REVERTED_DIRECTION.isRevertedSegmentDirection());
		assertTrue(Direction1D.BOTH_DIRECTIONS.isRevertedSegmentDirection());
	}

	@Test
	@DisplayName("fromInteger")
	public void fromInteger() {
		assertSame(Direction1D.REVERTED_DIRECTION, Direction1D.fromInteger(-5));
		assertSame(Direction1D.REVERTED_DIRECTION, Direction1D.fromInteger(-1));
		assertSame(Direction1D.BOTH_DIRECTIONS, Direction1D.fromInteger(0));
		assertSame(Direction1D.SEGMENT_DIRECTION, Direction1D.fromInteger(1));
		assertSame(Direction1D.SEGMENT_DIRECTION, Direction1D.fromInteger(5));
	}

	@Test
	@DisplayName("toInteger")
	public void toInteger() {
		assertEquals(1, Direction1D.SEGMENT_DIRECTION.toInteger());
		assertEquals(0, Direction1D.BOTH_DIRECTIONS.toInteger());
		assertEquals(-1, Direction1D.REVERTED_DIRECTION.toInteger());
	}

}