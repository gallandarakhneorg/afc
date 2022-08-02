/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d3.afp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.RectangularPrism3afp;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public abstract class AbstractPathWindingRule3afpTest<P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3afp<?, ?, ?, P, V, B>> extends AbstractMathTestCase {
	
	/** Is the shape to test.
	 */
	protected Path3afp<?, ?, ?, P, V, B> shape;
	
	/** Shape factory.
	 */
	protected TestShapeFactory3afp<P, V, B> factory;

	protected abstract TestShapeFactory3afp<P, V, B> createFactory();

	@BeforeEach
	public void setUp() throws Exception {
		this.factory = createFactory();
	}

	protected void createTestShape(PathWindingRule rule) {
		this.shape = this.factory.createPath(rule);
		this.shape.moveTo(1, -3, 0);
		this.shape.lineTo(4, -4, 0);
		this.shape.lineTo(6, -2, 0);
		this.shape.lineTo(7, 2, 0);
		this.shape.lineTo(5, 5, 0);
		this.shape.lineTo(4, 1, 0);
		this.shape.lineTo(5, 0, 0);
		this.shape.lineTo(12, -1, 0);
		this.shape.lineTo(13, -4, 0);
		this.shape.lineTo(10, -5, 0);
		this.shape.lineTo(4, -2, 0);
		this.shape.lineTo(1, 2, 0);
		this.shape.lineTo(3, 4, 0);
		this.shape.lineTo(9, 4, 0);
		this.shape.lineTo(9, -3, 0);
		this.shape.lineTo(4, -6, 0);
		this.shape.closePath();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		this.shape = null;
		this.factory = null;
	}
	
	@ParameterizedTest
	@EnumSource(PathWindingRule.class)
    public void containsPoint_outsideExternal_evenOdd(PathWindingRule rule) {
		createTestShape(rule);
		assumeTrue(this.shape.getWindingRule() == PathWindingRule.EVEN_ODD);
		assertFalse(this.shape.contains(0, 0, 0));
		assertFalse(this.shape.contains(-4, 10, 0));
		assertFalse(this.shape.contains(2, -2, 0));
		assertFalse(this.shape.contains(10, 0, 0));
		assertFalse(this.shape.contains(8, -5, 0));
    }
   
	@ParameterizedTest
	@EnumSource(PathWindingRule.class)
    public void containsPoint_outsideExternal_nonZero(PathWindingRule rule) {
		createTestShape(rule);
		assumeTrue(this.shape.getWindingRule() == PathWindingRule.NON_ZERO);
		assertFalse(this.shape.contains(0, 0, 0));
		assertFalse(this.shape.contains(-4, 10, 0));
		assertFalse(this.shape.contains(2, -2, 0));
		assertFalse(this.shape.contains(10, 0, 0));
		assertFalse(this.shape.contains(8, -5, 0));
    }

	@ParameterizedTest
	@EnumSource(PathWindingRule.class)
    public void containsPoint_outsideInternal_evenOdd(PathWindingRule rule) {
		createTestShape(rule);
		assumeTrue(this.shape.getWindingRule() == PathWindingRule.EVEN_ODD);
		assertFalse(this.shape.contains(6, 2, 0));
		assertFalse(this.shape.contains(5, 2, 0));
    }

	@ParameterizedTest
	@EnumSource(PathWindingRule.class)
    public void containsPoint_outsideInternal_nonZero(PathWindingRule rule) {
		createTestShape(rule);
		assumeTrue(this.shape.getWindingRule() == PathWindingRule.NON_ZERO);
		assertFalse(this.shape.contains(6, 2, 0));
		assertFalse(this.shape.contains(5, 2, 0));
    }

	@ParameterizedTest
	@EnumSource(PathWindingRule.class)
    public void containsPoint_inside_evenOdd(PathWindingRule rule) {
		createTestShape(rule);
		assumeTrue(this.shape.getWindingRule() == PathWindingRule.EVEN_ODD);
		assertTrue(this.shape.contains(3, 2, 0));
		assertTrue(this.shape.contains(5, 4.1, 0));
		assertTrue(this.shape.contains(8, 3, 0));
		assertTrue(this.shape.contains(11, -3, 0));
		assertTrue(this.shape.contains(5, -4, 0));
    }

	@ParameterizedTest
	@EnumSource(PathWindingRule.class)
    public void containsPoint_inside_nonZero(PathWindingRule rule) {
		createTestShape(rule);
		assumeTrue(this.shape.getWindingRule() == PathWindingRule.NON_ZERO);
		assertTrue(this.shape.contains(3, 2, 0));
		assertTrue(this.shape.contains(5, 4.1, 0));
		assertTrue(this.shape.contains(8, 3, 0));
		assertTrue(this.shape.contains(11, -3, 0));
		assertTrue(this.shape.contains(5, -4, 0));
    }

	@ParameterizedTest
	@EnumSource(PathWindingRule.class)
    public void containsPoint_insideWindingZone_evenOdd(PathWindingRule rule) {
		createTestShape(rule);
		assumeTrue(this.shape.getWindingRule() == PathWindingRule.EVEN_ODD);
		assertFalse(this.shape.contains(7, -1, 0));
		assertFalse(this.shape.contains(8, -2, 0));
		assertFalse(this.shape.contains(6, -2.5, 0));
    }

	@ParameterizedTest
	@EnumSource(PathWindingRule.class)
    public void containsPoint_insideWindingZone_nonZero(PathWindingRule rule) {
		createTestShape(rule);
		assumeTrue(this.shape.getWindingRule() == PathWindingRule.NON_ZERO);
		assertTrue(this.shape.contains(7, -1, 0));
		assertTrue(this.shape.contains(8, -2, 0));
		assertTrue(this.shape.contains(6, -2.5, 0));
    }

}
