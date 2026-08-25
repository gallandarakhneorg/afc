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

package org.arakhne.afc.math.geometry.d2.tests.general;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.Circle2ai;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.general.Shape2DType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Shape2DType")
@SuppressWarnings("all")
public class Shape2DTypeTest {

	@Test
	@DisplayName("RECTANGLE.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_RECTANGLE() {
		assertEquals(Rectangle2afp.class, Shape2DType.RECTANGLE.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("RECTANGLE.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_RECTANGLE() {
		assertEquals(Rectangle2ai.class, Shape2DType.RECTANGLE.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("ORIENTED_RECTANGLE.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_ORIENTED_RECTANGLE() {
		assertEquals(OrientedRectangle2afp.class, Shape2DType.ORIENTED_RECTANGLE.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("ORIENTED_RECTANGLE.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_ORIENTED_RECTANGLE() {
		assertNull(Shape2DType.ORIENTED_RECTANGLE.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("ROUND_RECTANGLE.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_ROUND_RECTANGLE() {
		assertEquals(RoundRectangle2afp.class, Shape2DType.ROUND_RECTANGLE.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("ROUND_RECTANGLE.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_ROUND_RECTANGLE() {
		assertNull(Shape2DType.ROUND_RECTANGLE.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("CIRCLE.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_CIRCLE() {
		assertEquals(Circle2afp.class, Shape2DType.CIRCLE.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("CIRCLE.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_CIRCLE() {
		assertEquals(Circle2ai.class, Shape2DType.CIRCLE.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("ELLIPSE.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_ELLIPSE() {
		assertEquals(Ellipse2afp.class, Shape2DType.ELLIPSE.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("ELLIPSE.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_ELLIPSE() {
		assertNull(Shape2DType.ELLIPSE.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("SEGMENT.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_SEGMENT() {
		assertEquals(Segment2afp.class, Shape2DType.SEGMENT.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("SEGMENT.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_SEGMENT() {
		assertEquals(Segment2ai.class, Shape2DType.SEGMENT.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("TRIANGLE.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_TRIANGLE() {
		assertEquals(Triangle2afp.class, Shape2DType.TRIANGLE.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("TRIANGLE.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_TRIANGLE() {
		assertNull(Shape2DType.TRIANGLE.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("PATH.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_PATH() {
		assertEquals(Path2afp.class, Shape2DType.PATH.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("PATH.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_PATH() {
		assertEquals(Path2ai.class, Shape2DType.PATH.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("MULTISHAPE.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_MULTISHAPE() {
		assertEquals(MultiShape2afp.class, Shape2DType.MULTISHAPE.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("MULTISHAPE.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_MULTISHAPE() {
		assertEquals(MultiShape2ai.class, Shape2DType.MULTISHAPE.getPreferredDiscreteShapeType());
	}

	@Test
	@DisplayName("PARALLELOGRAM.getPreferredContinuousShapeType")
	public void getPreferredContinuousShapeType_PARALLELOGRAM() {
		assertEquals(Parallelogram2afp.class, Shape2DType.PARALLELOGRAM.getPreferredContinuousShapeType());
	}

	@Test
	@DisplayName("PARALLELOGRAM.getPreferredDiscreteShapeType")
	public void getPreferredDiscreteShapeType_PARALLELOGRAM() {
		assertNull(Shape2DType.PARALLELOGRAM.getPreferredDiscreteShapeType());
	}

}
