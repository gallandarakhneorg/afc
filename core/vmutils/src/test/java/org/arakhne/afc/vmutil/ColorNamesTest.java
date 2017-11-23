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

package org.arakhne.afc.vmutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
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
		"barkblue", //$NON-NLS-1$
		"darkcyan", //$NON-NLS-1$
		"darkgoldenrod", //$NON-NLS-1$
		"dargray", //$NON-NLS-1$
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

	@Test
	public void getColorNames() {
		Set<String> names = new TreeSet<>(ColorNames.getColorNames());
		for (String nm : NAMES) {
			assertTrue("Missed name: " + nm, names.remove(nm)); //$NON-NLS-1$
		}
		assertEquals(0, names.size());
	}

	@Test
	public void getColorFromNameStringInt() {
		for (String nm : NAMES) {
			assertNotEquals("Invalid color: " + nm, 0xFFFFFF, ColorNames.getColorFromName(nm, 0xFFFFFF)); //$NON-NLS-1$
		}
	}

	@Test
	public void getColorFromNameString() {
		for (String nm : NAMES) {
			assertNotNull("Invalid color: " + nm, ColorNames.getColorFromName(nm)); //$NON-NLS-1$
		}
	}

}
