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

package org.arakhne.afc.math.geometry.base.tests;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.IntersectionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("IntersectionType")
@SuppressWarnings("all")
public class IntersectionTypeTest {

	@DisplayName("invert(type)")
	@Nested
	public class InvertType {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertSame(IntersectionType.INSIDE, IntersectionType.invert(IntersectionType.ENCLOSING));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.invert(IntersectionType.OUTSIDE));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertSame(IntersectionType.SAME, IntersectionType.invert(IntersectionType.SAME));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertSame(IntersectionType.SPANNING, IntersectionType.invert(IntersectionType.SPANNING));
		}

	}

	@DisplayName("invert")
	@Nested
	public class Invert {
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertSame(IntersectionType.INSIDE, IntersectionType.ENCLOSING.invert());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.invert());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertSame(IntersectionType.SAME, IntersectionType.SAME.invert());
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.invert());
		}
		
	}

	@DisplayName("or(type,type)")
	@Nested
	public class OrTypeType {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertSame(IntersectionType.ENCLOSING, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.INSIDE));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.OUTSIDE));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.SAME));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.ENCLOSING, IntersectionType.SPANNING));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.ENCLOSING));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.INSIDE));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.OUTSIDE));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.SAME));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.INSIDE, IntersectionType.SPANNING));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.ENCLOSING));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.INSIDE));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.SAME));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.OUTSIDE, IntersectionType.SPANNING));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SAME, IntersectionType.ENCLOSING));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.SAME, IntersectionType.INSIDE));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SAME, IntersectionType.OUTSIDE));
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			assertSame(IntersectionType.SAME, IntersectionType.or(IntersectionType.SAME, IntersectionType.SAME));
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SAME, IntersectionType.SPANNING));
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.ENCLOSING));
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			assertSame(IntersectionType.INSIDE, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.INSIDE));
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.OUTSIDE));
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.SAME));
		}

		@DisplayName("#25")
		@Test
		public void test_25() {
			assertSame(IntersectionType.SPANNING, IntersectionType.or(IntersectionType.SPANNING, IntersectionType.SPANNING));
		}

	}

	@DisplayName("or(type)")
	@Nested
	public class OrType {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertSame(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING.or(IntersectionType.ENCLOSING));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertSame(IntersectionType.INSIDE, IntersectionType.ENCLOSING.or(IntersectionType.INSIDE));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.or(IntersectionType.OUTSIDE));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.or(IntersectionType.SAME));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.or(IntersectionType.SPANNING));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.ENCLOSING));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.INSIDE));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.OUTSIDE));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.SAME));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.or(IntersectionType.SPANNING));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertSame(IntersectionType.SPANNING, IntersectionType.OUTSIDE.or(IntersectionType.ENCLOSING));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertSame(IntersectionType.INSIDE, IntersectionType.OUTSIDE.or(IntersectionType.INSIDE));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.or(IntersectionType.OUTSIDE));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertSame(IntersectionType.SPANNING, IntersectionType.OUTSIDE.or(IntersectionType.SAME));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertSame(IntersectionType.SPANNING, IntersectionType.OUTSIDE.or(IntersectionType.SPANNING));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SAME.or(IntersectionType.ENCLOSING));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertSame(IntersectionType.INSIDE, IntersectionType.SAME.or(IntersectionType.INSIDE));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SAME.or(IntersectionType.OUTSIDE));
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			assertSame(IntersectionType.SAME, IntersectionType.SAME.or(IntersectionType.SAME));
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SAME.or(IntersectionType.SPANNING));
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.or(IntersectionType.ENCLOSING));
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			assertSame(IntersectionType.INSIDE, IntersectionType.SPANNING.or(IntersectionType.INSIDE));
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.or(IntersectionType.OUTSIDE));
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.or(IntersectionType.SAME));
		}

		@DisplayName("#25")
		@Test
		public void test_25() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.or(IntersectionType.SPANNING));
		}

	}

	@DisplayName("and(type,type)")
	@Nested
	public class AndTypeType {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertSame(IntersectionType.ENCLOSING, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.INSIDE));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.OUTSIDE));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertSame(IntersectionType.ENCLOSING, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.SAME));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.ENCLOSING, IntersectionType.SPANNING));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.ENCLOSING));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertSame(IntersectionType.INSIDE, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.INSIDE));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.OUTSIDE));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertSame(IntersectionType.INSIDE, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.SAME));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.INSIDE, IntersectionType.SPANNING));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.ENCLOSING));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.INSIDE));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.SAME));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.OUTSIDE, IntersectionType.SPANNING));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertSame(IntersectionType.ENCLOSING, IntersectionType.and(IntersectionType.SAME, IntersectionType.ENCLOSING));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertSame(IntersectionType.INSIDE, IntersectionType.and(IntersectionType.SAME, IntersectionType.INSIDE));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.SAME, IntersectionType.OUTSIDE));
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			assertSame(IntersectionType.SAME, IntersectionType.and(IntersectionType.SAME, IntersectionType.SAME));
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SAME, IntersectionType.SPANNING));
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.ENCLOSING));
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.INSIDE));
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.OUTSIDE));
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.SAME));
		}

		@DisplayName("#25")
		@Test
		public void test_25() {
			assertSame(IntersectionType.SPANNING, IntersectionType.and(IntersectionType.SPANNING, IntersectionType.SPANNING));
		}

	}

	@DisplayName("and(type")
	@Nested
	public class AndType {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertSame(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING.and(IntersectionType.ENCLOSING));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.and(IntersectionType.INSIDE));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertSame(IntersectionType.ENCLOSING, IntersectionType.ENCLOSING.and(IntersectionType.SAME));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertSame(IntersectionType.SPANNING, IntersectionType.ENCLOSING.and(IntersectionType.SPANNING));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertSame(IntersectionType.SPANNING, IntersectionType.INSIDE.and(IntersectionType.ENCLOSING));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.and(IntersectionType.INSIDE));
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.INSIDE.and(IntersectionType.OUTSIDE));
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertSame(IntersectionType.INSIDE, IntersectionType.INSIDE.and(IntersectionType.SAME));
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertSame(IntersectionType.SPANNING, IntersectionType.INSIDE.and(IntersectionType.SPANNING));
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.ENCLOSING));
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.INSIDE));
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.OUTSIDE));
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.SAME));
		}

		@DisplayName("#14")
		@Test
		public void test_14() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.OUTSIDE.and(IntersectionType.SPANNING));
		}

		@DisplayName("#15")
		@Test
		public void test_15() {
			assertSame(IntersectionType.ENCLOSING, IntersectionType.SAME.and(IntersectionType.ENCLOSING));
		}

		@DisplayName("#16")
		@Test
		public void test_16() {
			assertSame(IntersectionType.INSIDE, IntersectionType.SAME.and(IntersectionType.INSIDE));
		}

		@DisplayName("#17")
		@Test
		public void test_17() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.SAME.and(IntersectionType.OUTSIDE));
		}

		@DisplayName("#18")
		@Test
		public void test_18() {
			assertSame(IntersectionType.SAME, IntersectionType.SAME.and(IntersectionType.SAME));
		}

		@DisplayName("#19")
		@Test
		public void test_19() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SAME.and(IntersectionType.SPANNING));
		}

		@DisplayName("#20")
		@Test
		public void test_20() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.and(IntersectionType.ENCLOSING));
		}

		@DisplayName("#21")
		@Test
		public void test_21() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.and(IntersectionType.INSIDE));
		}

		@DisplayName("#22")
		@Test
		public void test_22() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.SPANNING.and(IntersectionType.OUTSIDE));
		}

		@DisplayName("#23")
		@Test
		public void test_23() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.and(IntersectionType.SAME));
		}

		@DisplayName("#24")
		@Test
		public void test_24() {
			assertSame(IntersectionType.SPANNING, IntersectionType.SPANNING.and(IntersectionType.SPANNING));
		}

		@DisplayName("#25")
		@Test
		public void test_25() {
			assertSame(IntersectionType.OUTSIDE, IntersectionType.ENCLOSING.and(IntersectionType.OUTSIDE));
		}

	}

}