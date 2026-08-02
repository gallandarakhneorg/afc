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

package org.arakhne.afc.vmutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("ColorNames")
@SuppressWarnings("all")
public class ColorNamesTest {

	private static final String[] NAMES = new String[] {
		"transparent", //$NON-NLS-1$
		"aliceblue", //$NON-NLS-1$
		"antiquewhite", //$NON-NLS-1$
		"aqua", //$NON-NLS-1$
		"aquamarine", //$NON-NLS-1$
		"azure", //$NON-NLS-1$
		"beige", //$NON-NLS-1$
		"bisque", //$NON-NLS-1$
		"black", //$NON-NLS-1$
		"blancheddalmond", //$NON-NLS-1$
		"blue", //$NON-NLS-1$
		"blueviolet", //$NON-NLS-1$
		"brown", //$NON-NLS-1$
		"burlywood", //$NON-NLS-1$
		"cadetblue", //$NON-NLS-1$
		"chartreuse", //$NON-NLS-1$
		"chocolate", //$NON-NLS-1$
		"coral", //$NON-NLS-1$
		"cornflowerblue", //$NON-NLS-1$
		"cornsilk", //$NON-NLS-1$
		"crimson", //$NON-NLS-1$
		"cyan", //$NON-NLS-1$
		"darkblue", //$NON-NLS-1$
		"darkcyan", //$NON-NLS-1$
		"darkgoldenrod", //$NON-NLS-1$
		"darkgray", //$NON-NLS-1$
		"darkgreen", //$NON-NLS-1$
		"darkgrey", //$NON-NLS-1$
		"darkkhaki", //$NON-NLS-1$
		"darkmagenta", //$NON-NLS-1$
		"darkolivegreen", //$NON-NLS-1$
		"darkorange", //$NON-NLS-1$
		"darkorchid", //$NON-NLS-1$
		"darkred", //$NON-NLS-1$
		"darksalmon", //$NON-NLS-1$
		"darkseagreen", //$NON-NLS-1$
		"darkslateblue", //$NON-NLS-1$
		"darkslategray", //$NON-NLS-1$
		"darkslategrey", //$NON-NLS-1$
		"darkturquoise", //$NON-NLS-1$
		"darkviolet", //$NON-NLS-1$
		"deeppink", //$NON-NLS-1$
		"darkskyblue", //$NON-NLS-1$
		"dimgray", //$NON-NLS-1$
		"dimgrey", //$NON-NLS-1$
		"dodgerblue", //$NON-NLS-1$
		"firebrick", //$NON-NLS-1$
		"floralwhite", //$NON-NLS-1$
		"forestgreen", //$NON-NLS-1$
		"fuchsia", //$NON-NLS-1$
		"gainsboro", //$NON-NLS-1$
		"ghostwhite", //$NON-NLS-1$
		"gold", //$NON-NLS-1$
		"goldenrod", //$NON-NLS-1$
		"gray", //$NON-NLS-1$
		"green", //$NON-NLS-1$
		"greenyellow", //$NON-NLS-1$
		"grey", //$NON-NLS-1$
		"honeydew", //$NON-NLS-1$
		"hotpink", //$NON-NLS-1$
		"indianred", //$NON-NLS-1$
		"indigo", //$NON-NLS-1$
		"ivory", //$NON-NLS-1$
		"khaki", //$NON-NLS-1$
		"lavender", //$NON-NLS-1$
		"lavenderblush", //$NON-NLS-1$
		"lawngreen", //$NON-NLS-1$
		"lemonchiffon", //$NON-NLS-1$
		"lightblue", //$NON-NLS-1$
		"lightcoral", //$NON-NLS-1$
		"lightcyan", //$NON-NLS-1$
		"lightgoldenrodyyellow", //$NON-NLS-1$
		"lightgray", //$NON-NLS-1$
		"lightgreen", //$NON-NLS-1$
		"lightgrey", //$NON-NLS-1$
		"lightpink", //$NON-NLS-1$
		"lightsalmon", //$NON-NLS-1$
		"lightseagreen", //$NON-NLS-1$
		"lightskyblue", //$NON-NLS-1$
		"lightslategray", //$NON-NLS-1$
		"lightslategrey", //$NON-NLS-1$
		"lightsteelblue", //$NON-NLS-1$
		"lightyellow", //$NON-NLS-1$
		"lime", //$NON-NLS-1$
		"limegreen", //$NON-NLS-1$
		"linen", //$NON-NLS-1$
		"magenta", //$NON-NLS-1$
		"maroon", //$NON-NLS-1$
		"mediumaquamarine", //$NON-NLS-1$
		"mediumblue", //$NON-NLS-1$
		"mediumorchid", //$NON-NLS-1$
		"mediumpurple", //$NON-NLS-1$
		"mediumseagreen", //$NON-NLS-1$
		"mediumslateblue", //$NON-NLS-1$
		"mediumspringgreen", //$NON-NLS-1$
		"mediumturquoise", //$NON-NLS-1$
		"mediumvioletred", //$NON-NLS-1$
		"midnightblue", //$NON-NLS-1$
		"mintcream", //$NON-NLS-1$
		"mistyrose", //$NON-NLS-1$
		"moccasin", //$NON-NLS-1$
		"navajowhite", //$NON-NLS-1$
		"navy", //$NON-NLS-1$
		"oldlace", //$NON-NLS-1$
		"olive", //$NON-NLS-1$
		"olivedrab", //$NON-NLS-1$
		"orange", //$NON-NLS-1$
		"orangered", //$NON-NLS-1$
		"orchid", //$NON-NLS-1$
		"palegoldenrod", //$NON-NLS-1$
		"palegreen", //$NON-NLS-1$
		"paleturquoise", //$NON-NLS-1$
		"palevioletred", //$NON-NLS-1$
		"papayawhip", //$NON-NLS-1$
		"peachpuff", //$NON-NLS-1$
		"peru", //$NON-NLS-1$
		"pink", //$NON-NLS-1$
		"plum", //$NON-NLS-1$
		"powderblue", //$NON-NLS-1$
		"purple", //$NON-NLS-1$
		"red", //$NON-NLS-1$
		"rosybrown", //$NON-NLS-1$
		"royalblue", //$NON-NLS-1$
		"saddlebrown", //$NON-NLS-1$
		"salmon", //$NON-NLS-1$
		"sandybrown", //$NON-NLS-1$
		"seagreen", //$NON-NLS-1$
		"seashell", //$NON-NLS-1$
		"sienna", //$NON-NLS-1$
		"silver", //$NON-NLS-1$
		"skyblue", //$NON-NLS-1$
		"slateblue", //$NON-NLS-1$
		"slategray", //$NON-NLS-1$
		"slategrey", //$NON-NLS-1$
		"snow", //$NON-NLS-1$
		"springgreen", //$NON-NLS-1$
		"steelblue", //$NON-NLS-1$
		"tan", //$NON-NLS-1$
		"teal", //$NON-NLS-1$
		"thistle", //$NON-NLS-1$
		"tomato", //$NON-NLS-1$
		"turquoise", //$NON-NLS-1$
		"violet", //$NON-NLS-1$
		"wheat", //$NON-NLS-1$
		"white", //$NON-NLS-1$
		"whitesmoke", //$NON-NLS-1$
		"yellow", //$NON-NLS-1$
		"yellowgreen", //$NON-NLS-1$
	};
	
	private static Stream<Arguments> provideNames() {
		final List<Arguments> arguments = new ArrayList<>();
		for (final var nm : NAMES) {
			arguments.add(Arguments.of(nm));
		}
		return arguments.stream();
	}

	@DisplayName("getColorNames")
	@Nested
	public class GetColorNames {

		@DisplayName("(String)")
		@ParameterizedTest(name = "{index}: {0} => {1}")
		@MethodSource("org.arakhne.afc.vmutil.ColorNamesTest#provideNames")
		public void getColorNames(String name) {
			var names = ColorNames.getColorNames();
			assertTrue(names.contains(name), "Missed name: " + name); //$NON-NLS-1$
		}

		@DisplayName("size")
		@Test
		public void size() {
			var names = ColorNames.getColorNames();
			assertEquals(148, names.size());
		}
	}

	@DisplayName("getColorFromName")
	@Nested
	public class GetColorFromName {

		@DisplayName("(String,int)")
		@ParameterizedTest(name = "{index}: {0} => {1}")
		@MethodSource("org.arakhne.afc.vmutil.ColorNamesTest#provideNames")
		public void getColorFromNameStringInt(String name) {
			assertNotEquals(0xFFFFFF, ColorNames.getColorFromName(name, 0xFFFFFF), "Invalid color: " + name); //$NON-NLS-1$
		}
	
		@DisplayName("(String)")
		@ParameterizedTest(name = "{index}: {0} => {1}")
		@MethodSource("org.arakhne.afc.vmutil.ColorNamesTest#provideNames")
		public void getColorFromNameString(String name) {
			assertNotNull(ColorNames.getColorFromName(name), "Invalid color: " + name); //$NON-NLS-1$
		}
	}

	@DisplayName("getColorNameFromValue")
	@Nested
	public class GetColorNameFromValue {

		@DisplayName("red")
		public void red(String name) {
			assertEquals("red", ColorNames.getColorNameFromValue(0xFFFF0000)); //$NON-NLS-1$
			assertNull(ColorNames.getColorNameFromValue(0xFF0000));
		}

		@DisplayName("lime")
		public void lime(String name) {
			assertEquals("lime", ColorNames.getColorNameFromValue(0xFF00FF00)); //$NON-NLS-1$
			assertNull(ColorNames.getColorNameFromValue(0x00FF00));
		}

		@DisplayName("blue")
		public void blue(String name) {
			assertEquals("blue", ColorNames.getColorNameFromValue(0xFF0000FF)); //$NON-NLS-1$
			assertNull(ColorNames.getColorNameFromValue(0x0000FF));
		}
	}

}
