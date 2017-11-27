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

package org.arakhne.afc.jasim.motionbehavior.kinematic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.jasim.motionbehavior.AgentMotion;
import org.arakhne.afc.jasim.motionbehavior.kinematic.RandomKinematicAlgorithm;
import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;

@SuppressWarnings("all")
public class RandomKinematicAlgorithmTest extends AbstractMathTestCase {

	private RandomKinematicAlgorithm random;
	
	@Before
	public void setUp() {
		this.random = new RandomKinematicAlgorithm();
	}
	
	@After
	public void tearDown() {
		this.random = null;
	}
	
	@Test
	public void calculate() {
		AgentMotion m = this.random.calculate(new Point2d(1, 2), new Vector2d(3, 4), 5, 6, 7, 8);
		assertNotNull(m);
		assertFpVectorEquals(3.6, 4.8, m.getLinear()); 
	}

}
