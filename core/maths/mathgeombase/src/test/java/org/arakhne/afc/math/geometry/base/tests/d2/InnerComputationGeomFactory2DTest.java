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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.ImmutableGeomFactory2D;
import org.arakhne.afc.math.geometry.base.d2.ImmutablePoint2D;
import org.arakhne.afc.math.geometry.base.d2.ImmutableVector2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationGeomFactory2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Transform2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.matrix.SingularMatrixException;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public class InnerComputationGeomFactory2DTest extends AbstractMathTestCase {

	private InnerComputationGeomFactory2D factory;
	
	@BeforeEach
	public void setUp() {
		this.factory = new InnerComputationGeomFactory2D();
	}
	
	@Test
	@DisplayName("convertToPoint((Point2D) null)")
	public void convertToPoint_nullPoint2D() {
		var expected = new InnerComputationPoint2D(0, 0);
		var actual = this.factory.convertToPoint((Point2D) null);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToPoint((Point2D) expected)")
	public void convertToPoint_expectedPoint2D() {
		var expected = new InnerComputationPoint2D(123., 486.);
		var actual = this.factory.convertToPoint(expected);
		assertSame(expected, actual);
	}

	@Test
	@DisplayName("convertToPoint((Point2D) immutable)")
	public void convertToPoint_immutablePoint2D() {
		var expected = new ImmutablePoint2D(123., 486.);
		var actual = this.factory.convertToPoint(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Point2D) null)")
	public void convertToVector_nullPoint2D() {
		var expected = new InnerComputationPoint2D(0, 0);
		var actual = this.factory.convertToVector((Point2D) null);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Point2D) expected)")
	public void convertToVector_expectedPoint2D() {
		var expected = new InnerComputationPoint2D(123., 486.);
		var actual = this.factory.convertToVector(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Point2D) immutable)")
	public void convertToVector_immutablePoint2D() {
		var expected = new ImmutablePoint2D(123., 486.);
		var actual = this.factory.convertToVector(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToPoint((Vector2D) null)")
	public void convertToPoint_nullVector2D() {
		var expected = new InnerComputationVector2D(0, 0);
		var actual = this.factory.convertToPoint((Vector2D) null);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToPoint((Vector2D) expected)")
	public void convertToPoint_expectedVector2D() {
		var expected = new InnerComputationVector2D(123., 486.);
		var actual = this.factory.convertToPoint(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToPoint((Vector2D) immutable)")
	public void convertToPoint_immutableVector2D() {
		var expected = new ImmutableVector2D(123., 486.);
		var actual = this.factory.convertToPoint(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Vector2D) null)")
	public void convertToVector_nullVector2D() {
		var expected = new InnerComputationVector2D(0, 0);
		var actual = this.factory.convertToVector((Vector2D) null);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Vector2D) expected)")
	public void convertToVector_expectedVector() {
		var expected = new InnerComputationVector2D(123., 486.);
		var actual = this.factory.convertToVector(expected);
		assertSame(expected, actual);
	}

	@Test
	@DisplayName("convertToVector((Vector2D) immutable)")
	public void convertToVector_immutableVector2D() {
		var expected = new ImmutableVector2D(123., 486.);
		var actual = this.factory.convertToVector(expected);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("newPoint")
	public void newPoint() {
		var expected = new ImmutablePoint2D(0., 0.);
		var actual = this.factory.newPoint();
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("newPoint(123.568, 457.584)")
	public void newPoint_doubledouble() {
		var expected = new ImmutablePoint2D(123.568, 457.584);
		var actual = this.factory.newPoint(123.568, 457.584);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("newPoint(123, 457)")
	public void newPoint_intint() {
		var expected = new ImmutablePoint2D(123, 457);
		var actual = this.factory.newPoint(123, 457);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("newVector")
	public void newVector() {
		var expected = new ImmutableVector2D(0., 0.);
		var actual = this.factory.newVector();
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("newVector(123.568, 457.584)")
	public void newVector_doubledouble() {
		var expected = new ImmutableVector2D(123.568, 457.584);
		var actual = this.factory.newVector(123.568, 457.584);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

	@Test
	@DisplayName("newVector(123, 457)")
	public void newVector_intint() {
		var expected = new ImmutableVector2D(123, 457);
		var actual = this.factory.newVector(123, 457);
		assertNotSame(expected, actual);
		assertEpsilonEquals(expected, actual);
	}

}