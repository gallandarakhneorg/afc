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

package org.arakhne.afc.io.shape;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.io.shape.ESRIFileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ESRIFileUtil")
@SuppressWarnings("all")
public class ESRIFileUtilTest extends AbstractIoShapeTestCase {

	@DisplayName("toESRI_x")
	@Nested
	public class ToESRIx {
		
		@Test
		public void testToESRI_x() throws Exception {
			double n = getRandom().nextDouble();
			assertEpsilonEquals(n, ESRIFileUtil.toESRI_x(n));
		}
	}

	@DisplayName("fromESRI_x")
	@Nested
	public class FromESRIx {

		@Test
		public void testFromESRI_x() throws Exception {
			double n = getRandom().nextDouble();
			assertEpsilonEquals(n, ESRIFileUtil.fromESRI_x(n));
		}
	}

	@DisplayName("toESRI_y")
	@Nested
	public class ToESRIy {

		@Test
		public void testToESRI_y() throws Exception {
			double n = getRandom().nextDouble();
			assertEpsilonEquals(n, ESRIFileUtil.toESRI_y(n));
		}
	}

	@DisplayName("fromESRI_y")
	@Nested
	public class FromESRIy {

		@Test
		public void testFromESRI_y() throws Exception {
			double n = getRandom().nextDouble();
			assertEpsilonEquals(n, ESRIFileUtil.fromESRI_y(n));
		}
	}

	@DisplayName("toESRI_z")
	@Nested
	public class ToESRIz {

		@Test
		public void testToESRI_z() throws Exception {
			double n = getRandom().nextDouble();
			assertEpsilonEquals(n, ESRIFileUtil.toESRI_z(n));
		}
	}

	@DisplayName("fromESRI_z")
	@Nested
	public class FromESRIz {

		@Test
		public void testFromESRI_z() throws Exception {
			double n = getRandom().nextDouble();
			assertEpsilonEquals(n, ESRIFileUtil.fromESRI_z(n));
		}
	}

	@DisplayName("toESRI_m")
	@Nested
	public class ToESRIm {

		@Test
		public void testToESRI_m() throws Exception {
			double n = getRandom().nextDouble();
			assertEpsilonEquals(n, ESRIFileUtil.toESRI_m(n));
		}
	}

	@DisplayName("fromESRI_m")
	@Nested
	public class FromESRI_z {

		@Test
		public void testFromESRI_m() throws Exception {
			double n = getRandom().nextDouble();
			assertEpsilonEquals(n, ESRIFileUtil.fromESRI_m(n));
		}
	}

	@DisplayName("toESRI")
	@Nested
	public class ToESRI {

		@DisplayName("(double)")
		@Nested
		public class Withdouble {

			private double n;

			@BeforeEach
			public void setUp() {
				n = getRandom().nextDouble();
			}

			@DisplayName("finite value")
			@Test
			public void testToESRIDouble_finiteValue() {
				assertEpsilonEquals(n, ESRIFileUtil.toESRI(n));
			}

			@DisplayName("ESRI_NAN")
			@Test
			public void testToESRIDouble_esriNaN() {
				assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(ESRIFileUtil.ESRI_NAN));
			}

			@DisplayName("NaN")
			@Test
			public void testToESRIDouble_nan() {
				assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Double.NaN));
			}

			@DisplayName("POSITIVE_INFINITY")
			@Test
			public void testToESRIDouble_positiveInfinity() {
				assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Double.POSITIVE_INFINITY));
			}

			@DisplayName("NEGATIVE_INFINITY")
			@Test
			public void testToESRIDouble_negativeInfinity() {
				assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Double.NEGATIVE_INFINITY));
			}
		}

		@DisplayName("(float)")
		@Nested
		public class WithFloat {

			private float n;

			@BeforeEach
			public void setUp() {
				n = getRandom().nextFloat();
			}

			@DisplayName("finite value")
			@Test
			public void testToESRIFloat_finiteValue() {
				assertEpsilonEquals(n, ESRIFileUtil.toESRI(n));
			}

			@DisplayName("ESRI_NAN")
			@Test
			public void testToESRIFloat_esriNaN() {
				assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI((float) ESRIFileUtil.ESRI_NAN));
			}

			@DisplayName("NaN")
			@Test
			public void testToESRIFloat_nan() {
				assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Float.NaN));
			}

			@DisplayName("POSITIVE_INFINITY")
			@Test
			public void testToESRIFloat_positiveInfinity() {
				assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Float.POSITIVE_INFINITY));
			}

			@DisplayName("NEGATIVE_INFINITY")
			@Test
			public void testToESRIFloat_negativeInfinity() {
				assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Float.NEGATIVE_INFINITY));
			}
		}
	}

	@DisplayName("isESRINaN")
	@Nested
	public class IsESRINaN {

		@DisplayName("(double)")
		@Nested
		public class Withdouble {

			private double n;

			@BeforeEach
			public void setUp() {
				n = getRandom().nextDouble();
			}

			@DisplayName("finite value")
			@Test
			public void testIsESRINaNDouble_finiteValue() throws Exception {
				assertFalse(ESRIFileUtil.isESRINaN(n));
			}

			@DisplayName("ESRI_NAN")
			@Test
			public void testIsESRINaNDouble_esriNaN() throws Exception {
				assertTrue(ESRIFileUtil.isESRINaN(ESRIFileUtil.ESRI_NAN));
			}

			@DisplayName("NaN")
			@Test
			public void testIsESRINaNDouble_nan() throws Exception {
				assertTrue(ESRIFileUtil.isESRINaN(Double.NaN));
			}

			@DisplayName("POSITIVE_INFINITY")
			@Test
			public void testIsESRINaNDouble_positiveInfinity() throws Exception {
				assertTrue(ESRIFileUtil.isESRINaN(Double.POSITIVE_INFINITY));
			}

			@DisplayName("NEGATIVE_INFINITY")
			@Test
			public void testIsESRINaNDouble_negativeInfinity() throws Exception {
				assertTrue(ESRIFileUtil.isESRINaN(Double.NEGATIVE_INFINITY));
			}
		}

		@DisplayName("(float)")
		@Nested
		public class WithFloat {

			private float n;

			@BeforeEach
			public void setUp() {
				n = getRandom().nextFloat();
			}

			@DisplayName("finite value")
			@Test
			public void testIsESRINaNFloat_finiteValue() {
				assertFalse(ESRIFileUtil.isESRINaN(n));
			}

			@DisplayName("ESRI_NAN")
			@Test
			public void testIsESRINaNFloat_esriNaN() {
				assertTrue(ESRIFileUtil.isESRINaN((float) ESRIFileUtil.ESRI_NAN));
			}

			@DisplayName("NaN")
			@Test
			public void testIsESRINaNFloat_nan() {
				assertTrue(ESRIFileUtil.isESRINaN(Float.NaN));
			}

			@DisplayName("POSITIVE_INFINITY")
			@Test
			public void testIsESRINaNFloat_positiveInfinity() {
				assertTrue(ESRIFileUtil.isESRINaN(Float.POSITIVE_INFINITY));
			}

			@DisplayName("NEGATIVE_INFINITY")
			@Test
			public void testIsESRINaNFloat_negativeInfinity() {
				assertTrue(ESRIFileUtil.isESRINaN(Float.NEGATIVE_INFINITY));
			}
		}
	}

	@DisplayName("fromESRI")
	@Nested
	public class FromESRI {

		@DisplayName("(double)")
		@Nested
		public class WithDouble {

			private double n;

			@BeforeEach
			public void setUp() {
				n = getRandom().nextDouble();
			}

			@DisplayName("finite value")
			@Test
			public void testFromESRIDouble_finiteValue() {
				assertEpsilonEquals(n, ESRIFileUtil.fromESRI(n));
			}

			@DisplayName("ESRI_NAN")
			@Test
			public void testFromESRIDouble_esriNaN() {
				assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(ESRIFileUtil.ESRI_NAN)));
			}

			@DisplayName("NaN")
			@Test
			public void testFromESRIDouble_nan() {
				assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Double.NaN)));
			}

			@DisplayName("POSITIVE_INFINITY")
			@Test
			public void testFromESRIDouble_positiveInfinity() {
				assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Double.POSITIVE_INFINITY)));
			}

			@DisplayName("NEGATIVE_INFINITY")
			@Test
			public void testFromESRIDouble_negativeInfinity() {
				assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Double.NEGATIVE_INFINITY)));
			}
		}

		@DisplayName("(float)")
		@Nested
		public class WithFloat {

			private float n;

			@BeforeEach
			public void setUp() {
				n = getRandom().nextFloat();
			}

			@DisplayName("finite value")
			@Test
			public void testFromESRIFloat_finiteValue() throws Exception {
				assertEpsilonEquals(n, ESRIFileUtil.fromESRI(n));
			}

			@DisplayName("ESRI_NAN")
			@Test
			public void testFromESRIFloat_esriNaN() throws Exception {
				assertTrue(Double.isNaN(ESRIFileUtil.fromESRI((float) ESRIFileUtil.ESRI_NAN)));
			}

			@DisplayName("NaN")
			@Test
			public void testFromESRIFloat_nan() throws Exception {
				assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Float.NaN)));
			}

			@DisplayName("POSITIVE_INFINITY")
			@Test
			public void testFromESRIFloat_positiveInfinity() throws Exception {
				assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Float.POSITIVE_INFINITY)));
			}

			@DisplayName("NEGATIVE_INFINITY")
			@Test
			public void testFromESRIFloat_negativeInfinity() throws Exception {
				assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Float.NEGATIVE_INFINITY)));
			}
		}
	}
}
