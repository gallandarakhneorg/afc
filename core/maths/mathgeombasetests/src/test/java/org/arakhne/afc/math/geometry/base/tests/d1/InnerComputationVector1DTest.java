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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.d1.InnerComputationGeomFactory1D;
import org.arakhne.afc.math.geometry.base.d1.InnerComputationVector1D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InnerComputationVector1D")
@SuppressWarnings("all")
public class InnerComputationVector1DTest extends AbstractMathTestCase {

	private Segment1DStub segment;

	private InnerComputationVector1D vector;
	
	@BeforeEach
	public void setUp() {
		this.segment = new Segment1DStub();
		this.vector = new InnerComputationVector1D(this.segment, 145.669, 4853.1456);
	}

	@Test
	@DisplayName("getGeomFactory")
	public void getGeomFactory() {
		assertNotNull(this.vector.getGeomFactory());
		assertSame(InnerComputationGeomFactory1D.SINGLETON, this.vector.getGeomFactory());
	}

	@Test
	@DisplayName("getSegment")
	public void getSegment() {
		assertSame(this.segment, this.vector.getSegment());
	}

	@Test
	@DisplayName("getX")
	public void getX() {
		assertEpsilonEquals(145.669, this.vector.getX());
	}

	@Test
	@DisplayName("ix")
	public void ix() {
		assertEquals(146, this.vector.ix());
	}

	@Test
	@DisplayName("getY")
	public void getY() {
		assertEpsilonEquals(4853.1456, this.vector.getY());
	}

	@Test
	@DisplayName("iy")
	public void iy() {
		assertEquals(4853, this.vector.iy());
	}

	@Test
	@DisplayName("setX")
	public void setX() {
		this.vector.setX(456.852);
		assertEpsilonEquals(456.852, this.vector.getX());
	}
	
	@Test
	@DisplayName("setY")
	public void setY() {
		this.vector.setY(456.852);
		assertEpsilonEquals(456.852, this.vector.getY());
	}

}