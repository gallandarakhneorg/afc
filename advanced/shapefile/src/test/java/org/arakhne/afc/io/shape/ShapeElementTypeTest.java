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

import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class ShapeElementTypeTest extends AbstractIoShapeTest {

	@Test
	public void testFromESRIInteger() {
		int n = getRandom().nextInt(50) - getRandom().nextInt(50);
		ShapeElementType expected;
		switch(n) {
		case 0:
			expected = ShapeElementType.NULL;
			break;
		case 1:
			expected = ShapeElementType.POINT;
			break;
		case 3:
			expected = ShapeElementType.POLYLINE;
			break;
		case 5:
			expected = ShapeElementType.POLYGON;
			break;
		case 8:
			expected = ShapeElementType.MULTIPOINT;
			break;
		case 11:
			expected = ShapeElementType.POINT_Z;
			break;
		case 13:
			expected = ShapeElementType.POLYLINE_Z;
			break;
		case 15:
			expected = ShapeElementType.POLYGON_Z;
			break;
		case 18:
			expected = ShapeElementType.MULTIPOINT_Z;
			break;
		case 21:
			expected = ShapeElementType.POINT_M;
			break;
		case 23:
			expected = ShapeElementType.POLYLINE_M;
			break;
		case 25:
			expected = ShapeElementType.POLYGON_M;
			break;
		case 28:
			expected = ShapeElementType.MULTIPOINT_M;
			break;
		case 31:
			expected = ShapeElementType.MULTIPATCH;
			break;
		default:
			expected = ShapeElementType.UNSUPPORTED;
		}

		assertEquals(expected, ShapeElementType.fromESRIInteger(n));
	}

	@Test
	public void testHasZ() {
		assertTrue(ShapeElementType.MULTIPATCH.hasZ());
		assertFalse(ShapeElementType.MULTIPOINT.hasZ());
		assertFalse(ShapeElementType.MULTIPOINT_M.hasZ());
		assertTrue(ShapeElementType.MULTIPOINT_Z.hasZ());
		assertFalse(ShapeElementType.NULL.hasZ());
		assertFalse(ShapeElementType.POINT.hasZ());
		assertFalse(ShapeElementType.POINT_M.hasZ());
		assertTrue(ShapeElementType.POINT_Z.hasZ());
		assertFalse(ShapeElementType.POLYGON.hasZ());
		assertFalse(ShapeElementType.POLYGON_M.hasZ());
		assertTrue(ShapeElementType.POLYGON_Z.hasZ());
		assertFalse(ShapeElementType.POLYLINE.hasZ());
		assertFalse(ShapeElementType.POLYLINE_M.hasZ());
		assertTrue(ShapeElementType.POLYLINE_Z.hasZ());
		assertFalse(ShapeElementType.UNSUPPORTED.hasZ());
	}

	@Test
	public void testHasM() {
		assertTrue(ShapeElementType.MULTIPATCH.hasM());
		assertFalse(ShapeElementType.MULTIPOINT.hasM());
		assertTrue(ShapeElementType.MULTIPOINT_M.hasM());
		assertTrue(ShapeElementType.MULTIPOINT_Z.hasM());
		assertFalse(ShapeElementType.NULL.hasM());
		assertFalse(ShapeElementType.POINT.hasM());
		assertTrue(ShapeElementType.POINT_M.hasM());
		assertTrue(ShapeElementType.POINT_Z.hasM());
		assertFalse(ShapeElementType.POLYGON.hasM());
		assertTrue(ShapeElementType.POLYGON_M.hasM());
		assertTrue(ShapeElementType.POLYGON_Z.hasM());
		assertFalse(ShapeElementType.POLYLINE.hasM());
		assertTrue(ShapeElementType.POLYLINE_M.hasM());
		assertTrue(ShapeElementType.POLYLINE_Z.hasM());
		assertFalse(ShapeElementType.UNSUPPORTED.hasM());
	}

}
