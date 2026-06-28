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

package org.arakhne.afc.math.geometry.d3.tests.general;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.ai.AlignedBox3ai;
import org.arakhne.afc.math.geometry.d3.ai.MultiShape3ai;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
@DisplayName("Shape2DType")
public class Shape3DTypeTest {

	@Test
	@DisplayName("ALIGNED_BOX.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_ALIGNED_BOX() {
		assertEquals(AlignedBox3afp.class, Shape3DType.ALIGNED_BOX.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("ALIGNED_BOX.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_ALIGNED_BOX() {
		assertEquals(AlignedBox3ai.class, Shape3DType.ALIGNED_BOX.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("SPHERE.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_SPHERE() {
		assertEquals(Sphere3afp.class, Shape3DType.SPHERE.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("SPHERE.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_SPHERE() {
		assertEquals(Sphere3ai.class, Shape3DType.SPHERE.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("SEGMENT.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_SEGMENT() {
		assertEquals(Segment3afp.class, Shape3DType.SEGMENT.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("SEGMENT.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_SEGMENT() {
		assertEquals(Segment3ai.class, Shape3DType.SEGMENT.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("PATH.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_PATH() {
		assertEquals(Path3afp.class, Shape3DType.PATH.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("PATH.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_PATH() {
		assertEquals(Path3ai.class, Shape3DType.PATH.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("MULTISHAPE.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_MULTISHAPE() {
		assertEquals(MultiShape3afp.class, Shape3DType.MULTISHAPE.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("MULTISHAPE.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_MULTISHAPE() {
		assertEquals(MultiShape3ai.class, Shape3DType.MULTISHAPE.getPreferredDiscreteShapeType());
	}

}
