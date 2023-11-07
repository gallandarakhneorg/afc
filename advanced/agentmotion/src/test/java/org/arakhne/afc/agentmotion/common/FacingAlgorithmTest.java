/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.agentmotion.kinematic.AligningKinematicAlgorithm;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public class FacingAlgorithmTest extends AbstractMathTestCase {

	private AligningKinematicAlgorithm align;
	
	private FacingAlgorithm face;

	@BeforeEach
	public void setUp() {
		this.align = new AligningKinematicAlgorithm(.5);
		this.face = new FacingAlgorithm(this.align);
	}
	
	@AfterEach
	public void tearDown() {
		this.face = null;
		this.align = null;
	}
	
	@Test
	public void calculate_far() {
		assertEpsilonEquals(-1.5708, this.face.calculate(new Point2d(1, 2), new Vector2d(-3, 5), 3, 4, new Point2d(6, 5)));
	}

	@Test
	public void calculate_close() {
		assertZero(this.face.calculate(new Point2d(1, 2), new Vector2d(5, 3.1), 3, 4, new Point2d(6, 5)));
	}

}
