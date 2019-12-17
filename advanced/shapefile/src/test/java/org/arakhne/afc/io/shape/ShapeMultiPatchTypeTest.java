/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class ShapeMultiPatchTypeTest extends AbstractIoShapeTest {

	@Test
	public void testFromESRIInteger() throws Exception {
		int n = getRandom().nextInt(5) - getRandom().nextInt(5);
		ShapeMultiPatchType expected;
		switch(n) {
		case 0:
			expected = ShapeMultiPatchType.TRIANGLE_STRIP;
			break;
		case 1:
			expected = ShapeMultiPatchType.TRIANGLE_FAN;
			break;
		case 2:
			expected = ShapeMultiPatchType.OUTER_RING;
			break;
		case 3:
			expected = ShapeMultiPatchType.INNER_RING;
			break;
		case 4:
			expected = ShapeMultiPatchType.FIRST_RING;
			break;
		case 5:
			expected = ShapeMultiPatchType.RING;
			break;
		default:
			expected = null;
		}
		
		if (expected==null) {
			try {
				ShapeMultiPatchType.fromESRIInteger(n);
				fail("ShapeFileException was expected"); //$NON-NLS-1$
			}
			catch(ShapeFileException exception) {
				// expected exception
			}
		}
		else
			assertEquals(expected, ShapeMultiPatchType.fromESRIInteger(n));
	}

}
