/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.agentmotion.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

@SuppressWarnings("all")
@Deprecated
public class FleeingAlgorithmTest extends AbstractMathTestCase {

	private FleeingAlgorithm flee;
	
	@Before
	public void setUp() {
		this.flee = new FleeingAlgorithm();
	}
	
	@After
	public void tearDown() {
		this.flee = null;
	}
	
	@Test
	public void calculate() {
		assertFpVectorEquals(-3.42997, -2.05798, this.flee.calculate(new Point2d(1, 2),  3, 4, new Point2d(6, 5)));
	}

}
