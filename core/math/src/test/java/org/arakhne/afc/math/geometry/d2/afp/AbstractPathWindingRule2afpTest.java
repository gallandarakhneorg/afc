/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.afp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.PathWindingRuleTestRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractPathWindingRule2afpTest<P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, ?, P, V, B>> extends AbstractMathTestCase {
	
	@Rule
	public final PathWindingRuleTestRule csTestRule = new PathWindingRuleTestRule();
	
	/** Is the shape to test.
	 */
	protected Path2afp<?, ?, ?, P, V, B> shape;
	
	/** Shape factory.
	 */
	protected TestShapeFactory<P, V, B> factory;

	protected abstract TestShapeFactory<P, V, B> createFactory();

	@Before
	public void setUp() throws Exception {
		this.factory = createFactory();
		this.shape = this.factory.createPath(PathWindingRuleTestRule.CURRENT_RULE);
		this.shape.moveTo(1, -3);
		this.shape.lineTo(4, -4);
		this.shape.lineTo(6, -2);
		this.shape.lineTo(7, 2);
		this.shape.lineTo(5, 5);
		this.shape.lineTo(4, 1);
		this.shape.lineTo(5, 0);
		this.shape.lineTo(12, -1);
		this.shape.lineTo(13, -4);
		this.shape.lineTo(10, -5);
		this.shape.lineTo(4, -2);
		this.shape.lineTo(1, 2);
		this.shape.lineTo(3, 4);
		this.shape.lineTo(9, 4);
		this.shape.lineTo(9, -3);
		this.shape.lineTo(4, -6);
		this.shape.closePath();
	}
	
	@After
	public void tearDown() throws Exception {
		this.shape = null;
		this.factory = null;
	}
	
	@Test
    public void containsPoint_outsideExternal_evenOdd() {
		Assume.assumeTrue(this.shape.getWindingRule() == PathWindingRule.EVEN_ODD);
		assertFalse(this.shape.contains(0, 0));
		assertFalse(this.shape.contains(-4, 10));
		assertFalse(this.shape.contains(2, -2));
		assertFalse(this.shape.contains(10, 0));
		assertFalse(this.shape.contains(8, -5));
    }
   
	@Test
    public void containsPoint_outsideExternal_nonZero() {
		Assume.assumeTrue(this.shape.getWindingRule() == PathWindingRule.NON_ZERO);
		assertFalse(this.shape.contains(0, 0));
		assertFalse(this.shape.contains(-4, 10));
		assertFalse(this.shape.contains(2, -2));
		assertFalse(this.shape.contains(10, 0));
		assertFalse(this.shape.contains(8, -5));
    }

	@Test
    public void containsPoint_outsideInternal_evenOdd() {
		Assume.assumeTrue(this.shape.getWindingRule() == PathWindingRule.EVEN_ODD);
		assertFalse(this.shape.contains(6, 2));
		assertFalse(this.shape.contains(5, 2));
    }

	@Test
    public void containsPoint_outsideInternal_nonZero() {
		Assume.assumeTrue(this.shape.getWindingRule() == PathWindingRule.NON_ZERO);
		assertFalse(this.shape.contains(6, 2));
		assertFalse(this.shape.contains(5, 2));
    }

	@Test
    public void containsPoint_inside_evenOdd() {
		Assume.assumeTrue(this.shape.getWindingRule() == PathWindingRule.EVEN_ODD);
		assertTrue(this.shape.contains(3, 2));
		assertTrue(this.shape.contains(5, 4.1));
		assertTrue(this.shape.contains(8, 3));
		assertTrue(this.shape.contains(11, -3));
		assertTrue(this.shape.contains(5, -4));
    }

	@Test
    public void containsPoint_inside_nonZero() {
		Assume.assumeTrue(this.shape.getWindingRule() == PathWindingRule.NON_ZERO);
		assertTrue(this.shape.contains(3, 2));
		assertTrue(this.shape.contains(5, 4.1));
		assertTrue(this.shape.contains(8, 3));
		assertTrue(this.shape.contains(11, -3));
		assertTrue(this.shape.contains(5, -4));
    }

	@Test
    public void containsPoint_insideWindingZone_evenOdd() {
		Assume.assumeTrue(this.shape.getWindingRule() == PathWindingRule.EVEN_ODD);
		assertFalse(this.shape.contains(7, -1));
		assertFalse(this.shape.contains(8, -2));
		assertFalse(this.shape.contains(6, -2.5));
    }

	@Test
    public void containsPoint_insideWindingZone_nonZero() {
		Assume.assumeTrue(this.shape.getWindingRule() == PathWindingRule.NON_ZERO);
		assertTrue(this.shape.contains(7, -1));
		assertTrue(this.shape.contains(8, -2));
		assertTrue(this.shape.contains(6, -2.5));
    }

}