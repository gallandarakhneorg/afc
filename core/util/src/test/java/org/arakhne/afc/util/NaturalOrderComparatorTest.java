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

package org.arakhne.afc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("NaturalOrderComparator")
@SuppressWarnings("all")
public class NaturalOrderComparatorTest {

	private NaturalOrderComparator<Object> comparator;
	private Integer o1;
	private Integer o2;
	private Integer o3;
	private Integer o4;
	private Float v1;
	private Double v2;
	private String v3;
	private Object v4;

	@BeforeEach
	public void setUp() {
		o1 = 1;
		o2 = 2;
		o3 = 3;
		o4 = 10;
		v1 = 3f;
		v2 = 3.;
		v3 = "3";
		v4 = new Object();
		comparator = new NaturalOrderComparator<>();
	}

	@DisplayName("#1")
	@Test
	public void compare_1() {
		assertEquals(0, comparator.compare(o1, o1));
	}

	@DisplayName("#2")
	@Test
	public void compare_2() {
		assertEquals(-1, comparator.compare(o1, o2));
	}

	@DisplayName("#3")
	@Test
	public void compare_3() {
		assertEquals(-1, comparator.compare(o1, o3));
	}

	@DisplayName("#4")
	@Test
	public void compare_4() {
		assertEquals(-1, comparator.compare(o1, o4));
	}

	@DisplayName("#5")
	@Test
	public void compare_5() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o1, v1));
	}

	@DisplayName("#6")
	@Test
	public void compare_6() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o1, v2));
	}

	@DisplayName("#7")
	@Test
	public void compare_7() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o1, v3));
	}

	@DisplayName("#8")
	@Test
	public void compare_8() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o1, v4));
	}

	@DisplayName("#9")
	@Test
	public void compare_9() {
		assertEquals(1, comparator.compare(o2, o1));
	}

	@DisplayName("#10")
	@Test
	public void compare_10() {
		assertEquals(0, comparator.compare(o2, o2));
	}

	@DisplayName("#11")
	@Test
	public void compare_11() {
		assertEquals(-1, comparator.compare(o2, o3));
	}

	@DisplayName("#12")
	@Test
	public void compare_12() {
		assertEquals(-1, comparator.compare(o2, o4));
	}

	@DisplayName("#13")
	@Test
	public void compare_13() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o2, v1));
	}

	@DisplayName("#14")
	@Test
	public void compare_14() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o2, v2));
	}

	@DisplayName("#15")
	@Test
	public void compare_15() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o2, v3));
	}

	@DisplayName("#16")
	@Test
	public void compare_16() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o2, v4));
	}

	@DisplayName("#17")
	@Test
	public void compare_17() {
		assertEquals(1, comparator.compare(o3, o1));
	}

	@DisplayName("#18")
	@Test
	public void compare_18() {
		assertEquals(1, comparator.compare(o3, o2));
	}

	@DisplayName("#19")
	@Test
	public void compare_19() {
		assertEquals(0, comparator.compare(o3, o3));
	}

	@DisplayName("#20")
	@Test
	public void compare_20() {
		assertEquals(-1, comparator.compare(o3, o4));
	}

	@DisplayName("#21")
	@Test
	public void compare_21() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o3, v1));
	}

	@DisplayName("#22")
	@Test
	public void compare_22() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o3, v2));
	}

	@DisplayName("#23")
	@Test
	public void compare_23() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o3, v3));
	}

	@DisplayName("#24")
	@Test
	public void compare_24() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o3, v4));
	}

	@DisplayName("#25")
	@Test
	public void compare_25() {
		assertEquals(1, comparator.compare(o4, o1));
	}

	@DisplayName("#26")
	@Test
	public void compare_26() {
		assertEquals(1, comparator.compare(o4, o2));
	}

	@DisplayName("#27")
	@Test
	public void compare_27() {
		assertEquals(1, comparator.compare(o4, o3));
	}

	@DisplayName("#28")
	@Test
	public void compare_28() {
		assertEquals(0, comparator.compare(o4, o4));
	}

	@DisplayName("#29")
	@Test
	public void compare_29() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o4, v1));
	}

	@DisplayName("#30")
	@Test
	public void compare_30() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o4, v2));
	}

	@DisplayName("#31")
	@Test
	public void compare_31() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o4, v3));
	}

	@DisplayName("#32")
	@Test
	public void compare_32() {
		assertThrows(UnsupportedNaturalOrderException.class, () -> comparator.compare(o4, v4));
	}

}
