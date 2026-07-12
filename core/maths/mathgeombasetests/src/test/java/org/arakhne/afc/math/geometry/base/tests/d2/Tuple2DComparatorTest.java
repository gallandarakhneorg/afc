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

package org.arakhne.afc.math.geometry.base.tests.d2;

import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Tuple2DComparator;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test comparator of Tuple2D on their integer coordinates.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("Tuple2DComparator")
@SuppressWarnings("all")
public class Tuple2DComparatorTest extends AbstractMathTestCase {

	private Tuple2DComparator comparator;

	@BeforeEach
	public void setUp() {
		this.comparator = new Tuple2DComparator();
	}

	@Test
	@DisplayName("null <=> null")
	public void compare_0() {
		assertZero(this.comparator.compare(null, null));
	}

	@Test
	@DisplayName("null <=> (0,0)")
	public void compare_1() {
		var b = new InnerComputationVector2D(0., 0.);
		assertStrictlyNegative(this.comparator.compare(null, b));
	}

	@Test
	@DisplayName("(0,0) <=> null")
	public void compare_2() {
		var a = new InnerComputationVector2D(0., 0.);
		assertStrictlyPositive(this.comparator.compare(a, null));
	}

	@Test
	@DisplayName("(0,0) <=> (0,0)")
	public void compare_3() {
		var a = new InnerComputationVector2D(0., 0.);
		var b = new InnerComputationVector2D(0., 0.);
		assertZero(this.comparator.compare(a, b));
	}

	@Test
	@DisplayName("(-5,0) <=> (0,0)")
	public void compare_4() {
		var a = new InnerComputationVector2D(-5., 0.);
		var b = new InnerComputationVector2D(0., 0.);
		assertStrictlyNegative(this.comparator.compare(a, b));
	}

	@Test
	@DisplayName("(5,0) <=> (0,0)")
	public void compare_5() {
		var a = new InnerComputationVector2D(5., 0.);
		var b = new InnerComputationVector2D(0., 0.);
		assertStrictlyPositive(this.comparator.compare(a, b));
	}

	@Test
	@DisplayName("(-5,-7) <=> (0,0)")
	public void compare_6() {
		var a = new InnerComputationVector2D(-5., -7.);
		var b = new InnerComputationVector2D(0., 0.);
		assertStrictlyNegative(this.comparator.compare(a, b));
	}

	@Test
	@DisplayName("(-5,18) <=> (0,0)")
	public void compare_7() {
		var a = new InnerComputationVector2D(-5., 18.);
		var b = new InnerComputationVector2D(0., 0.);
		assertStrictlyNegative(this.comparator.compare(a, b));
	}

	@Test
	@DisplayName("(5,-7) <=> (0,0)")
	public void compare_8() {
		var a = new InnerComputationVector2D(5., -7.);
		var b = new InnerComputationVector2D(0., 0.);
		assertStrictlyPositive(this.comparator.compare(a, b));
	}

	@Test
	@DisplayName("(5,18) <=> (0,0)")
	public void compare_9() {
		var a = new InnerComputationVector2D(5., 18.);
		var b = new InnerComputationVector2D(0., 0.);
		assertStrictlyPositive(this.comparator.compare(a, b));
	}

	@Test
	@DisplayName("(0,-7) <=> (0,0)")
	public void compare_10() {
		var a = new InnerComputationVector2D(0., -7.);
		var b = new InnerComputationVector2D(0., 0.);
		assertStrictlyNegative(this.comparator.compare(a, b));
	}

	@Test
	@DisplayName("(0,18) <=> (0,0)")
	public void compare_11() {
		var a = new InnerComputationVector2D(0., 18.);
		var b = new InnerComputationVector2D(0., 0.);
		assertStrictlyPositive(this.comparator.compare(a, b));
	}

}
