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

package org.arakhne.afc.math.geometry.d3.tests.afp;

import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_BACK;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_BOTTOM;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_FRONT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_INSIDE;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_LEFT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_RIGHT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_TOP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("all")
public abstract class AbstractAlignedBox3dTestCase<T extends AlignedBox3afp<T, ?, ?, ?, ?, B>,
B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractBox3dTestCase<T, B> {

	@Override
	protected final T createShape() {
		return (T) createAlignedBox(5, 8, 0, 5, 10, 10);
	}

	@DisplayName("intersectsAlignedBoxAlignedBox(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticIntersectsAlignedBoxAlignedBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 0, 0, 0, 1, 1, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(0, 0, 0, 1, 1, 1, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 0, 20, 0, 1, 22, 10));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(0, 20, 0, 10, 22, 1, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 0, 0, 0, 5, 100, 10));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(0, 0, 0, 5, 100, 10, 5, 8, 0, 10, 18, 10));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 0, 0, 0, 5.1, 100, 10));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(0, 0, 0, 5.1, 100, 10, 5, 8, 0, 10, 18, 10));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 9, 0, 9.5, 15, 10));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 9, 0, 9.5, 15, 10, 5, 8, 0, 10, 18, 10));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 9, 0, 9.5, 15, 10));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 9, 0, 9.5, 15, 10, 5, 8, 0, 10, 18, 10));

		//

		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 12, 9.5, 15, 13));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 0, 12, 9.5, 15, 13, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 10, 9.5, 15, 11));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 0, 10, 9.5, 15, 11, 5, 8, 0, 10, 18, 10));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 9, 9.5, 15, 10));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 0, 9, 9.5, 15, 10, 5, 8, 0, 10, 18, 10));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 4, 9.5, 15, 5));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 0, 4, 9.5, 15, 5, 5, 8, 0, 10, 18, 10));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 0, 9.5, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 0, 0, 9.5, 15, 1, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, -1, 9.5, 15, 0));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 0, -1, 9.5, 15, 0, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, -2, 9.5, 15, -1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 0, -2, 9.5, 15, -1, 5, 8, 0, 10, 18, 10));

		//

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 9, 1, 7, 10, 2));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 9, 1, 7, 10, 2, 5, 8, 0, 10, 18, 10));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 4, 7, -1, 11, 19, 11));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(4, 7, -1, 11, 19, 11, 5, 8, 0, 10, 18, 10));
	}

	@DisplayName("intersectsAlignedBoxLine(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticIntersectsAlignedBoxLine(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 1, 1, 1, 10, 2, 2));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 1, 1, 1, 2, 2, 2));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 1, 1, 1, 2, 5, 2));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 1, 1, 1, 2, 10, 2));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 5, 3, 3));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 5, 14, 3));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 5, 27, 3));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 6, 3, 3));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 6, 14, 3));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 6, 27, 3));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0, 12, 22, 13));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(5, 8, 0, 10, 18, 10, /* */ 11, 21, 12, 12, 22, 13));
	}

	@DisplayName("intersectsAlignedBoxSegment(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticIntersectsAlignedBoxSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 1, 1, 1, 10, 2, 2));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 1, 1, 1, 2, 2, 2));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 1, 1, 1, 2, 5, 2));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 1, 1, 1, 2, 10, 2));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 5, 3, 3));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 5, 14, 3));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 5, 27, 3));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 6, 3, 3));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 6, 14, 3));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 5, 0, 3, 6, 27, 3));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0, 12, 22, 13));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(5, 8, 0, 10, 18, 10, /* */ 11, 21, 12, 12, 22, 13));
	}

	@DisplayName("containsAlignedBoxAlignedBox(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticContainsAlignedBoxAlignedBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 0, 0, 0, 1, 1, 1));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(0, 0, 0, 1, 1, 1, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 0, 20, 0, 1, 22, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(0, 20, 0, 10, 22, 1, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 0, 0, 0, 5, 100, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(0, 0, 0, 5, 100, 10, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 0, 0, 0, 5.1, 100, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(0, 0, 0, 5.1, 100, 10, 5, 8, 0, 10, 18, 10));

		assertTrue(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 9, 0, 9.5, 15, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 9, 0, 9.5, 15, 10, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 9, 0, 9.5, 15, 11));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 9, 0, 9.5, 15, 11, 5, 8, 0, 10, 18, 10));

		//

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 12, 9.5, 15, 13));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 0, 12, 9.5, 15, 13, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 10, 9.5, 15, 11));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 0, 10, 9.5, 15, 11, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 9, 9.5, 15, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 0, 9, 9.5, 15, 10, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 4, 9.5, 15, 5));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 0, 4, 9.5, 15, 5, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, 0, 9.5, 15, 1));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 0, 0, 9.5, 15, 1, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, -1, 9.5, 15, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 0, -1, 9.5, 15, 0, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 0, -2, 9.5, 15, -1));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 0, -2, 9.5, 15, -1, 5, 8, 0, 10, 18, 10));

		//

		assertTrue(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 6, 9, 1, 7, 10, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 9, 1, 7, 10, 2, 5, 8, 0, 10, 18, 10));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 10, 4, 7, -1, 11, 19, 11));
		assertTrue(AlignedBox3afp.containsAlignedBoxAlignedBox(4, 7, -1, 11, 19, 11, 5, 8, 0, 10, 18, 10));
	}

	@DisplayName("containsAlignedBoxPoint(double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticContainsAlignedBoxPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 0, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 0, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 0, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 0, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 0, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 0, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 0, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 0, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 0, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 0, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 0, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 0, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 0, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 0, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 0, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 0, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 0, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 0, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 0, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 0, 20));

		//

		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 8, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 8, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 8, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 8, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 8, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 8, -10));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 8, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 8, 2));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 8, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 8, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 8, -10));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 8, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 8, 2));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 8, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 8, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 8, -10));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 8, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 8, 2));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 8, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 8, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 8, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 8, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 8, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 8, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 8, 20));

		//

		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 14, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 14, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 14, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 14, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 14, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 14, -10));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 14, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 14, 2));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 14, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 14, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 14, -10));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 14, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 14, 2));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 14, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 14, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 14, -10));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 14, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 14, 2));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 14, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 14, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 14, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 14, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 14, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 14, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 14, 20));

		//

		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 18, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 18, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 18, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 18, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 18, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 18, -10));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 18, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 18, 2));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 18, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 18, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 18, -10));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 18, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 18, 2));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 18, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 18, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 18, -10));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 18, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 18, 2));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 18, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 18, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 18, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 18, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 18, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 18, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 18, 20));

		//

		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 27, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 27, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 27, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 27, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 27, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 27, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 27, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 27, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 27, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 5, 27, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 27, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 27, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 27, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 27, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 6.5, 27, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 27, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 27, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 27, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 27, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 10, 27, 20));
		//
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 27, -10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 27, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 27, 2));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 27, 10));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 15, 27, 20));
	}

	@DisplayName("equals(Object)")
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createAlignedBox(0, 8, 0, 5, 12, 1)));
		assertFalse(this.shape.equals(createAlignedBox(5, 8, 0, 5, 0, 1)));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 1)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createAlignedBox(5, 8, 0, 5, 10, 10)));
	}

	@DisplayName("equalsToShape(Shape3D)")
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createAlignedBox(0, 8, 0, 5, 12, 0)));
		assertFalse(this.shape.equalsToShape((T) createAlignedBox(5, 8, 0, 5, 0, 0)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createAlignedBox(5, 8, 0, 5, 10, 10)));
	}

	@DisplayName("add(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void addPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.add(createPoint(123.456, 456.789, 0));
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());

		this.shape.add(createPoint(-123.456, 456.789, 0));
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());

		this.shape.add(createPoint(-123.456, -456.789, 0));
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(-456.789, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());
	}

	@DisplayName("add(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void addDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.add(123.456, 456.789, 0);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());

		this.shape.add(-123.456, 456.789, 0);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());

		this.shape.add(-123.456, -456.789, 3);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(-456.789, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());
	}

	@DisplayName("setUnion(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setUnion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setUnion(createAlignedBox(0, 0, 0, 12, 1, 0));
		assertEpsilonEquals(0, this.shape.getMinX());
		assertEpsilonEquals(0, this.shape.getMinY());
		assertEpsilonEquals(12, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@DisplayName("createUnion(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void createUnion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B union = this.shape.createUnion(createAlignedBox(0, 0, 0, 12, 1, 2));
		assertNotSame(this.shape, union);
		assertEpsilonEquals(0, union.getMinX());
		assertEpsilonEquals(0, union.getMinY());
		assertEpsilonEquals(0, union.getMinZ());
		assertEpsilonEquals(12, union.getMaxX());
		assertEpsilonEquals(18, union.getMaxY());
		assertEpsilonEquals(10, union.getMaxZ());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());
	}

	@DisplayName("setIntersection(Box3afp) without intersection")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setIntersection_noIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setIntersection(createAlignedBox(0, 0, 0, 12, 1, 0));
		assertTrue(this.shape.isEmpty());
	}

	@DisplayName("setIntersection(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setIntersection_intersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setIntersection(createAlignedBox(0, 0, 0, 7, 10, 2));
		assertFalse(this.shape.isEmpty());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(7, this.shape.getMaxX());
		assertEpsilonEquals(10, this.shape.getMaxY());
		assertEpsilonEquals(2, this.shape.getMaxZ());
	}

	@DisplayName("createIntersection(Box3afp) without intersection")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void createIntersection_noIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.createIntersection(createAlignedBox(0, -1, 0, 12, 1, 2));
		assertNotSame(this.shape, box);
		assertTrue(box.isEmpty());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());

		box = this.shape.createIntersection(createAlignedBox(0, 7, 0, 12, 1, 2));
		assertNotSame(this.shape, box);
		assertTrue(box.isEmpty());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());
	}

	@DisplayName("createIntersection(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void createIntersection_intersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.createIntersection(createAlignedBox(0, 0, 0, 7, 10, 2));
		assertNotSame(this.shape, box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(0, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(10, box.getMaxY());
		assertEpsilonEquals(2, box.getMaxZ());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());
	}

	@DisplayName("avoidCollisionWith(AlignedBox3afp,Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void avoidCollisionWithAlignedBox3afpVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B r = createAlignedBox(0, 0, 0, 7, 10, 1);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector3D v = createVector(Double.NaN, Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, v);

		assertEpsilonEquals(0, v.getX());
		assertEpsilonEquals(0, v.getY());
		assertEpsilonEquals(1, v.getZ());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(1, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(11, this.shape.getMaxZ());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@DisplayName("avoidCollisionWith(AlignedBox3afp,Vector3D,Vector3D) null displacement")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void avoidCollisionWithAlignedBox3afpVector3DVector3D_nullDisplacement(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B r = createAlignedBox(0, 0, 0, 7, 10, 1);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector3D v = createVector(Double.NaN, Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, null, v);

		assertEpsilonEquals(0, v.getX());
		assertEpsilonEquals(0, v.getY());
		assertEpsilonEquals(1, v.getZ());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(1, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(11, this.shape.getMaxZ());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@DisplayName("avoidCollisionWith(AlignedBox3afp,Vector3D,Vector3D) no displacement")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void avoidCollisionWithAlignedBox3afpVector3DVector3D_noDisplacement(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B r = createAlignedBox(0, 0, 0, 7, 10, 1);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector3D v1 = createVector(0, 0, 0);
		Vector3D v2 = createVector(Double.NaN, Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, v1, v2);

		assertEpsilonEquals(0, v1.getX());
		assertEpsilonEquals(0, v1.getY());
		assertEpsilonEquals(1, v1.getZ());
		assertEpsilonEquals(0, v2.getX());
		assertEpsilonEquals(0, v2.getY());
		assertEpsilonEquals(1, v2.getZ());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(1, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(11, this.shape.getMaxZ());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@DisplayName("avoidCollisionWith(AlignedBox3afp,Vector3D,Vector3D) displacement")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void avoidCollisionWithAlignedBox3afpVector3DVector3D_givenDisplacement(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B r = createAlignedBox(0, 0, 0, 7, 10, 1);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector3D v1 = createVector(-4, 4, 0);
		Vector3D v2 = createVector(Double.NaN, Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, v1, v2);

		assertEpsilonEquals(-2, v1.getX());
		assertEpsilonEquals(2, v1.getY());
		assertEpsilonEquals(1, v1.getZ());
		assertEpsilonEquals(-2, v2.getX());
		assertEpsilonEquals(2, v2.getY());
		assertEpsilonEquals(1, v2.getZ());
		assertEpsilonEquals(3, this.shape.getMinX());
		assertEpsilonEquals(10, this.shape.getMinY());
		assertEpsilonEquals(1, this.shape.getMinZ());
		assertEpsilonEquals(8, this.shape.getMaxX());
		assertEpsilonEquals(20, this.shape.getMaxY());
		assertEpsilonEquals(11, this.shape.getMaxZ());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@DisplayName("getClosestPointTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getClosestPointToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = this.shape.getClosestPointTo(createPoint(0, 0, -5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 0, -5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 100, -5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 100, -5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 10, -5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 0, -5));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(154, 17, -5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(9, 154, -5));
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(8, 18, -5));
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 12, -5));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(0, p.getZ());

		//

		p = this.shape.getClosestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 0, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 100, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 100, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 10, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 0, 0));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(154, 17, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(9, 154, 0));
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(8, 18, 0));
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 12, 0));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(0, p.getZ());

		//

		p = this.shape.getClosestPointTo(createPoint(0, 0, 5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 0, 5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 100, 5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 100, 5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 10, 5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 0, 5));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(154, 17, 5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(9, 154, 5));
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(8, 18, 5));
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 12, 5));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(5, p.getZ());

		//

		p = this.shape.getClosestPointTo(createPoint(0, 0, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 0, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 100, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 100, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 10, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 0, 10));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(154, 17, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(9, 154, 10));
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(8, 18, 10));
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 12, 10));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = this.shape.getClosestPointTo(createPoint(0, 0, 21));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 0, 21));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 100, 21));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 100, 21));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 10, 21));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 0, 21));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(154, 17, 21));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(9, 154, 21));
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(8, 18, 21));
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 12, 21));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(10, p.getZ());
	}

	@DisplayName("getFarthestPointTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = this.shape.getFarthestPointTo(createPoint(0, 0, -5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 0, -5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 100, -5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 100, -5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 10, -5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 0, -5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(154, 17, -5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(9, 154, -5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(8, 18, -5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 12, -5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = this.shape.getFarthestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 0, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 100, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 100, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 10, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 0, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(154, 17, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(9, 154, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(8, 18, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 12, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = this.shape.getFarthestPointTo(createPoint(0, 0, 5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 0, 5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 100, 5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 100, 5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 10, 5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 0, 5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(154, 17, 5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(9, 154, 5));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, 5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 12, 5));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = this.shape.getFarthestPointTo(createPoint(0, 0, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 0, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 100, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 100, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 10, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 0, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(154, 17, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(9, 154, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(8, 18, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 12, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		//

		p = this.shape.getFarthestPointTo(createPoint(0, 0, 21));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 0, 21));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 100, 21));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 100, 21));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 10, 21));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 0, 21));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(154, 17, 21));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(9, 154, 21));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(8, 18, 21));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 12, 21));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("getDistance(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistancePoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(9.43398, this.shape.getDistance(createPoint(0, 0, 0)));
		assertEpsilonEquals(90.35486, this.shape.getDistance(createPoint(100, 0, 0)));
		assertEpsilonEquals(121.75385, this.shape.getDistance(createPoint(100, 100, 0)));
		assertEpsilonEquals(82.1523, this.shape.getDistance(createPoint(0, 100, 0)));
		assertEpsilonEquals(5, this.shape.getDistance(createPoint(0, 10, 0)));
		assertEpsilonEquals(8, this.shape.getDistance(createPoint(7, 0, 0)));
		assertEpsilonEquals(144, this.shape.getDistance(createPoint(154, 17, 0)));
		assertEpsilonEquals(136, this.shape.getDistance(createPoint(9, 154, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(8, 18, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(7, 12, 0)));
	}

	@DisplayName("getDistanceSquared(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceSquaredPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(88.99998, this.shape.getDistanceSquared(createPoint(0, 0, 0)));
		assertEpsilonEquals(8164, this.shape.getDistanceSquared(createPoint(100, 0, 0)));
		assertEpsilonEquals(14823.99999, this.shape.getDistanceSquared(createPoint(100, 100, 0)));
		assertEpsilonEquals(6749, this.shape.getDistanceSquared(createPoint(0, 100, 0)));
		assertEpsilonEquals(25, this.shape.getDistanceSquared(createPoint(0, 10, 0)));
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createPoint(7, 0, 0)));
		assertEpsilonEquals(20736, this.shape.getDistanceSquared(createPoint(154, 17, 0)));
		assertEpsilonEquals(18496, this.shape.getDistanceSquared(createPoint(9, 154, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(8, 18, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(7, 12, 0)));
	}

	@DisplayName("getDistanceL1(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getDistanceL1(createPoint(0, 0, 0)));
		assertEpsilonEquals(98, this.shape.getDistanceL1(createPoint(100, 0, 0)));
		assertEpsilonEquals(172, this.shape.getDistanceL1(createPoint(100, 100, 0)));
		assertEpsilonEquals(87, this.shape.getDistanceL1(createPoint(0, 100, 0)));
		assertEpsilonEquals(5, this.shape.getDistanceL1(createPoint(0, 10, 0)));
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(7, 0, 0)));
		assertEpsilonEquals(144, this.shape.getDistanceL1(createPoint(154, 17, 0)));
		assertEpsilonEquals(136, this.shape.getDistanceL1(createPoint(9, 154, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(8, 18, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(7, 12, 0)));
	}

	@DisplayName("getDistanceLinf(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(0, 0, 0)));
		assertEpsilonEquals(90, this.shape.getDistanceLinf(createPoint(100, 0, 0)));
		assertEpsilonEquals(90, this.shape.getDistanceLinf(createPoint(100, 100, 0)));
		assertEpsilonEquals(82, this.shape.getDistanceLinf(createPoint(0, 100, 0)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(0, 10, 0)));
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(7, 0, 0)));
		assertEpsilonEquals(144, this.shape.getDistanceLinf(createPoint(154, 17, 0)));
		assertEpsilonEquals(136, this.shape.getDistanceLinf(createPoint(9, 154, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(8, 18, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(7, 12, 0)));
	}

	@DisplayName("set(AlignedBox3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createAlignedBox(123.456, 456.789, 0, 789.123, 159.753, 658.5));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(456.789, this.shape.getMinY());
		assertEpsilonEquals(0., this.shape.getMinZ());
		assertEpsilonEquals(912.579, this.shape.getMaxX());
		assertEpsilonEquals(616.542, this.shape.getMaxY());
		assertEpsilonEquals(658.5, this.shape.getMaxZ());
	}

	@DisplayName("contains(AlignedBox3afp")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void containsAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createAlignedBox(0, 0, 0, 1, 1, 0)));
		assertFalse(createAlignedBox(0, 0, 0, 1, 1, 0).contains(this.shape));

		assertFalse(this.shape.contains(createAlignedBox(0, 20, 0, 1, 2, 0)));
		assertFalse(createAlignedBox(0, 20, 0, 1, 2, 0).contains(this.shape));

		assertFalse(this.shape.contains(createAlignedBox(0, 0, 0, 5, 100, 0)));
		assertFalse(createAlignedBox(0, 0, 0, 5, 100, 0).contains(this.shape));

		assertFalse(this.shape.contains(createAlignedBox(0, 0, 0, 5.1, 100, 0)));
		assertFalse(createAlignedBox(0, 0, 0, 5.1, 100, 0).contains(this.shape));

		assertTrue(this.shape.contains(createAlignedBox(6, 9, 0, .5, 9, 0)));
		assertFalse(createAlignedBox(6, 9, 0, .5, 9, 0).contains(this.shape));
	}

	@DisplayName("intersects(AlignedBox3afp")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createAlignedBox(0, 0, 0, 1, 1, 0)));
		assertFalse(createAlignedBox(0, 0, 0, 1, 1, 0).intersects(this.shape));

		assertFalse(this.shape.intersects(createAlignedBox(0, 20, 0, 1, 2, 0)));
		assertFalse(createAlignedBox(0, 20, 0, 1, 2, 0).intersects(this.shape));

		assertFalse(this.shape.intersects(createAlignedBox(0, 0, 0, 5, 100, 0)));
		assertFalse(createAlignedBox(0, 0, 0, 5, 100, 0).intersects(this.shape));

		assertTrue(this.shape.intersects(createAlignedBox(0, 0, 0, 5.1, 100, 1)));
		assertTrue(createAlignedBox(0, 0, 0, 5.1, 100, 1).intersects(this.shape));

		assertTrue(this.shape.intersects(createAlignedBox(6, 9, 0, .5, 9, 1)));
		assertTrue(createAlignedBox(6, 9, 0, .5, 9, 1).intersects(this.shape));

		assertTrue(this.shape.intersects(createAlignedBox(0, 0, 0, 5.1, 8.1, 1)));
		assertTrue(createAlignedBox(0, 0, 0, 5.1, 8.1, 1).intersects(this.shape));
	}

	@DisplayName("intersects(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSphere(0, 0, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 0, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 5, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 7, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(8, 8, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(8, 10, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(8, 18, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 19, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 25, 0, 1)));

		assertFalse(this.shape.intersects(createSphere(0, 0, 5, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 0, 5, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 5, 5, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 7, 5, 1)));
		assertTrue(this.shape.intersects(createSphere(8, 8, 5, 1)));
		assertTrue(this.shape.intersects(createSphere(8, 10, 5, 1)));
		assertTrue(this.shape.intersects(createSphere(8, 18, 5, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 19, 5, 1)));
		assertFalse(this.shape.intersects(createSphere(8, 25, 5, 1)));
	}

	@DisplayName("intersects(MultiShape3D)")
	public abstract void intersectsMultiShape3D(CoordinateSystem3D cs);

	@DisplayName("intersects(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSegment(20, 45, 0, 43, 15, 0)));
		assertTrue(this.shape.intersects(createSegment(20, 55, 0, 43, 15, 0)));
		assertTrue(this.shape.intersects(createSegment(20, 0, 0, 43, 15, 0)));
		assertTrue(this.shape.intersects(createSegment(0, 45, 0, 43, 15, 0)));
		assertTrue(this.shape.intersects(createSegment(20, 45, 0, 60, 15, 0)));
		assertTrue(this.shape.intersects(createSegment(5, 45, 0, 30, 55, 0)));
		assertTrue(this.shape.intersects(createSegment(40, 55, 0, 60, 15, 0)));
		assertTrue(this.shape.intersects(createSegment(40, 0, 0, 60, 40, 0)));
		assertTrue(this.shape.intersects(createSegment(0, 40, 0, 20, 0, 0)));
		assertTrue(this.shape.intersects(createSegment(0, 45, 0, 100, 15, 0)));
		assertTrue(this.shape.intersects(createSegment(20, 100, 0, 43, 0, 0)));
		assertFalse(this.shape.intersects(createSegment(20, 100, 0, 43, 101, 0)));
		assertFalse(this.shape.intersects(createSegment(100, 45, 0, 102, 15, 0)));
		assertFalse(this.shape.intersects(createSegment(20, 0, 0, 43, -2, 0)));
		assertFalse(this.shape.intersects(createSegment(-100, 45, 0, -48, 15, 0)));
		assertFalse(this.shape.intersects(createSegment(-100, 60, 0, -98, 61, 0)));
	}
	@DisplayName("intersects(Path3afp)")

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsPath3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp p;

		p = createPath();
		p.moveTo(-20, -20, 0);
		p.lineTo(-20, 20, 0);
		p.lineTo(20, 20, 0);
		p.lineTo(20, -20, 0);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, -20, 0);
		p.lineTo(5, 8, 0);
		p.lineTo(-20, 20, 0);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, -20, 0);
		p.lineTo(20, 20, 0);
		p.lineTo(-20, 20, 0);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, -20, 0);
		p.lineTo(-20, 20, 0);
		p.lineTo(20, -20, 0);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, 20, 0);
		p.lineTo(10, 8, 0);
		p.lineTo(20, 18, 0);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, 20, 0);
		p.lineTo(20, 18, 0);
		p.lineTo(10, 8, 0);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));
	}

	@DisplayName("intersects(PathIterator3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsPathIterator3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, B> p;

		p = createPath();
		p.moveTo(-20, -20, 0);
		p.lineTo(-20, 20, 0);
		p.lineTo(20, 20, 0);
		p.lineTo(20, -20, 0);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, -20, 0);
		p.lineTo(5, 8, 0);
		p.lineTo(-20, 20, 0);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, -20, 0);
		p.lineTo(20, 20, 0);
		p.lineTo(-20, 20, 0);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, -20, 0);
		p.lineTo(-20, 20, 0);
		p.lineTo(20, -20, 0);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, 20, 0);
		p.lineTo(10, 8, 0);
		p.lineTo(20, 18, 0);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, 20, 0);
		p.lineTo(20, 18, 0);
		p.lineTo(10, 8, 0);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));
	}

	@DisplayName("contains(double,double,double")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void containsDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(10, 12, 0, 30, 25, 0);
		assertFalse(this.shape.contains(20, 45, 0));
		assertFalse(this.shape.contains(20, 55, 0));
		assertFalse(this.shape.contains(20, 0, 0));
		assertFalse(this.shape.contains(0, 45, 0));
		assertFalse(this.shape.contains(5, 45, 0));
		assertFalse(this.shape.contains(40, 55, 0));
		assertFalse(this.shape.contains(40, 0, 0));
		assertFalse(this.shape.contains(0, 40, 0));
		assertFalse(this.shape.contains(20, 100, 0));
		assertFalse(this.shape.contains(100, 45, 0));
		assertFalse(this.shape.contains(-100, 45, 0));
		assertFalse(this.shape.contains(-100, 60, 0));
		assertTrue(this.shape.contains(10, 12, 0));
		assertTrue(this.shape.contains(40, 12, 0));
		assertTrue(this.shape.contains(40, 37, 0));
		assertTrue(this.shape.contains(10, 37, 0));
		assertTrue(this.shape.contains(35, 24, 0));
	}

	@DisplayName("contains(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(10, 12, 0, 30, 25, 0);
		assertFalse(this.shape.contains(createPoint(20, 45, 0)));
		assertFalse(this.shape.contains(createPoint(20, 55, 0)));
		assertFalse(this.shape.contains(createPoint(20, 0, 0)));
		assertFalse(this.shape.contains(createPoint(0, 45, 0)));
		assertFalse(this.shape.contains(createPoint(5, 45, 0)));
		assertFalse(this.shape.contains(createPoint(40, 55, 0)));
		assertFalse(this.shape.contains(createPoint(40, 0, 0)));
		assertFalse(this.shape.contains(createPoint(0, 40, 0)));
		assertFalse(this.shape.contains(createPoint(20, 100, 0)));
		assertFalse(this.shape.contains(createPoint(100, 45, 0)));
		assertFalse(this.shape.contains(createPoint(-100, 45, 0)));
		assertFalse(this.shape.contains(createPoint(-100, 60, 0)));
		assertTrue(this.shape.contains(createPoint(10, 12, 0)));
		assertTrue(this.shape.contains(createPoint(40, 12, 0)));
		assertTrue(this.shape.contains(createPoint(40, 37, 0)));
		assertTrue(this.shape.contains(createPoint(10, 37, 0)));
		assertTrue(this.shape.contains(createPoint(35, 24, 0)));
	}

	@DisplayName("inflate(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void inflate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.inflate(1, 2, 0, 3, 4, 4);
		assertEpsilonEquals(4, this.shape.getMinX());
		assertEpsilonEquals(6, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(13, this.shape.getMaxX());
		assertEpsilonEquals(22, this.shape.getMaxY());
		assertEpsilonEquals(14, this.shape.getMaxZ());

		this.shape.inflate(1, 2, 10, 3, 4, 0);
		assertEpsilonEquals(3, this.shape.getMinX());
		assertEpsilonEquals(4, this.shape.getMinY());
		assertEpsilonEquals(-10, this.shape.getMinZ());
		assertEpsilonEquals(16, this.shape.getMaxX());
		assertEpsilonEquals(26, this.shape.getMaxY());
		assertEpsilonEquals(14, this.shape.getMaxZ());
	}

	@DisplayName("intersects(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(this.shape.intersects((Shape3D) createAlignedBox(0, 0, 0, 5, 100, 0)));
		assertTrue(this.shape.intersects((Shape3D) createAlignedBox(0, 0, 0, 5.1, 100, 1)));

		assertFalse(this.shape.intersects((Shape3D) createSphere(-1, 1, -1, 5)));
		assertTrue(this.shape.intersects((Shape3D) createSphere(-1, 1, -1, 10)));

		assertFalse(this.shape.intersects((Shape3D) createSegment(18, 0, 3, 11, 18, 5)));
		assertTrue(this.shape.intersects((Shape3D) createSegment(18, 0, 3, 11, 18, 5)));
	}

	@DisplayName("b += Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(123.456, 456.789, 3.1));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(3.1, this.shape.getMinZ());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
		assertEpsilonEquals(13.1, this.shape.getMaxZ());
	}

	@DisplayName("b + Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(123.456, 456.789, 3.1));
		assertNotSame(this.shape, shape);
		assertEpsilonEquals(128.456, shape.getMinX());
		assertEpsilonEquals(464.789, shape.getMinY());
		assertEpsilonEquals(3.1, shape.getMinZ());
		assertEpsilonEquals(133.456, shape.getMaxX());
		assertEpsilonEquals(474.789, shape.getMaxY());
		assertEpsilonEquals(13.1, shape.getMaxZ());
	}

	@DisplayName("b -= Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(123.456, 456.789, 547.5));
		assertEpsilonEquals(-118.456, this.shape.getMinX());
		assertEpsilonEquals(-448.789, this.shape.getMinY());
		assertEpsilonEquals(-547.5, this.shape.getMinZ());
		assertEpsilonEquals(-113.456, this.shape.getMaxX());
		assertEpsilonEquals(-438.789, this.shape.getMaxY());
		assertEpsilonEquals(-537.5, this.shape.getMaxZ());
	}

	@DisplayName("b - Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(123.456, 456.789, 5));
		assertNotSame(this.shape, shape);
		assertEpsilonEquals(-118.456, shape.getMinX());
		assertEpsilonEquals(-448.789, shape.getMinY());
		assertEpsilonEquals(-5, shape.getMinZ());
		assertEpsilonEquals(-113.456, shape.getMaxX());
		assertEpsilonEquals(-438.789, shape.getMaxY());
		assertEpsilonEquals(5, shape.getMaxZ());
	}

	@DisplayName("b && Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(10, 12, 0, 30, 25, 0);
		assertFalse(this.shape.operator_and(createPoint(20, 45, 0)));
		assertFalse(this.shape.operator_and(createPoint(20, 55, 0)));
		assertFalse(this.shape.operator_and(createPoint(20, 0, 0)));
		assertFalse(this.shape.operator_and(createPoint(0, 45, 0)));
		assertFalse(this.shape.operator_and(createPoint(5, 45, 0)));
		assertFalse(this.shape.operator_and(createPoint(40, 55, 0)));
		assertFalse(this.shape.operator_and(createPoint(40, 0, 0)));
		assertFalse(this.shape.operator_and(createPoint(0, 40, 0)));
		assertFalse(this.shape.operator_and(createPoint(20, 100, 0)));
		assertFalse(this.shape.operator_and(createPoint(100, 45, 0)));
		assertFalse(this.shape.operator_and(createPoint(-100, 45, 0)));
		assertFalse(this.shape.operator_and(createPoint(-100, 60, 0)));
		assertTrue(this.shape.operator_and(createPoint(10, 12, 0)));
		assertTrue(this.shape.operator_and(createPoint(40, 12, 0)));
		assertTrue(this.shape.operator_and(createPoint(40, 37, 0)));
		assertTrue(this.shape.operator_and(createPoint(10, 37, 0)));
		assertTrue(this.shape.operator_and(createPoint(35, 24, 0)));
	}

	@DisplayName("b && Shape3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(createAlignedBox(0, 0, 0, 5.1, 100, 0).operator_and(this.shape));
	}

	@DisplayName("b .. Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(9.43398, this.shape.operator_upTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(90.35486, this.shape.operator_upTo(createPoint(100, 0, 0)));
		assertEpsilonEquals(121.75385, this.shape.operator_upTo(createPoint(100, 100, 0)));
		assertEpsilonEquals(82.1523, this.shape.operator_upTo(createPoint(0, 100, 0)));
		assertEpsilonEquals(5, this.shape.operator_upTo(createPoint(0, 10, 0)));
		assertEpsilonEquals(8, this.shape.operator_upTo(createPoint(7, 0, 0)));
		assertEpsilonEquals(144, this.shape.operator_upTo(createPoint(154, 17, 0)));
		assertEpsilonEquals(136, this.shape.operator_upTo(createPoint(9, 154, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(8, 18, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(7, 12, 0)));
	}

	@DisplayName("getCenter")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getCenter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p = this.shape.getCenter();
		assertNotNull(p);
		assertEpsilonEquals(7.5, p.getX());
		assertEpsilonEquals(13, p.getY());
		assertEpsilonEquals(5, p.getZ());
	}

	@DisplayName("getCenterX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getCenterX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7.5, this.shape.getCenterX());
	}

	@DisplayName("getCenterY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getCenterY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getCenterY());
	}

	@DisplayName("getCenterZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getCenterZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getCenterZ());
	}

	@DisplayName("setCenter(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setCenterDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setCenter(145, -47, 14);
		assertEpsilonEquals(142.5, this.shape.getMinX());
		assertEpsilonEquals(-52, this.shape.getMinY());
		assertEpsilonEquals(9, this.shape.getMinZ());
		assertEpsilonEquals(147.5, this.shape.getMaxX());
		assertEpsilonEquals(-42, this.shape.getMaxY());
		assertEpsilonEquals(19, this.shape.getMaxZ());
	}

	@DisplayName("setCenterX(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setCenterXDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setCenterX(145);
		assertEpsilonEquals(142.5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(147.5, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());
	}

	@DisplayName("setCenterY(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setCenterYDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setCenterY(-47);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(-52, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(-42, this.shape.getMaxY());
		assertEpsilonEquals(10, this.shape.getMaxZ());
	}

	@DisplayName("setCenterZ(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setCenterZDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setCenterZ(-47);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(-52, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(-42, this.shape.getMaxZ());
	}

	@DisplayName("set(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(1, 2, -1, 1, 5, 6);
		assertEpsilonEquals(1, this.shape.getMinX());
		assertEpsilonEquals(2, this.shape.getMinY());
		assertEpsilonEquals(-1, this.shape.getMinZ());
		assertEpsilonEquals(2, this.shape.getMaxX());
		assertEpsilonEquals(7, this.shape.getMaxY());
		assertEpsilonEquals(5, this.shape.getMaxZ());

		Vector3D v = createVector(1, 1, 1).toUnitVector();
		this.shape.set(1, 2, v.getX(), v.getY(), 5, 6);
		assertEpsilonEquals(1, this.shape.getMinX());
		assertEpsilonEquals(2, this.shape.getMinY());
		assertEpsilonEquals(0.5773502691896258, this.shape.getMinZ());
		assertEpsilonEquals(1.5773502691896258, this.shape.getMaxX());
		assertEpsilonEquals(7, this.shape.getMaxY());
		assertEpsilonEquals(6.5773502691896258, this.shape.getMaxZ());
	}

	@DisplayName("calculatesAlignedBoxPointDistanceSquared(double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticCalculatesAlignedBoxPointDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(114., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 0, -5));
		assertEpsilonEquals(8189., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 0, -5));
		assertEpsilonEquals(14849., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 100, -5));
		assertEpsilonEquals(6774., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 100, -5));
		assertEpsilonEquals(50., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 10, -5));
		assertEpsilonEquals(89., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 0, -5));
		assertEpsilonEquals(20761., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 154, 17, -5));
		assertEpsilonEquals(18521., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 9, 154, -5));
		assertEpsilonEquals(25., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 8, 18, -5));
		assertEpsilonEquals(25., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 12, -5));

		//

		assertEpsilonEquals(89., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0));
		assertEpsilonEquals(8164., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 0, 0));
		assertEpsilonEquals(14824., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 100, 0));
		assertEpsilonEquals(6749., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 100, 0));
		assertEpsilonEquals(25., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 10, 0));
		assertEpsilonEquals(64., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 0, 0));
		assertEpsilonEquals(20736., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 154, 17, 0));
		assertEpsilonEquals(18496., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 9, 154, 0));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 8, 18, 0));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 12, 0));

		//

		assertEpsilonEquals(89., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 0, 5));
		assertEpsilonEquals(8164., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 0, 5));
		assertEpsilonEquals(14824., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 100, 5));
		assertEpsilonEquals(6749., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 100, 5));
		assertEpsilonEquals(25., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 10, 5));
		assertEpsilonEquals(64., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 0, 5));
		assertEpsilonEquals(20736., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 154, 17, 5));
		assertEpsilonEquals(18496., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 9, 154, 5));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 8, 18, 5));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 12, 5));

		//

		assertEpsilonEquals(89., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 0, 10));
		assertEpsilonEquals(8164., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 0, 10));
		assertEpsilonEquals(14824., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 100, 10));
		assertEpsilonEquals(6749., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 100, 10));
		assertEpsilonEquals(25., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 10, 10));
		assertEpsilonEquals(64., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 0, 10));
		assertEpsilonEquals(20736., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 154, 17, 10));
		assertEpsilonEquals(18496., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 9, 154, 10));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 8, 18, 10));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 12, 10));

		//

		assertEpsilonEquals(210., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 0, 21));
		assertEpsilonEquals(8285., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 0, 21));
		assertEpsilonEquals(14945., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 100, 100, 21));
		assertEpsilonEquals(6870., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 100, 21));
		assertEpsilonEquals(146., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 0, 10, 21));
		assertEpsilonEquals(185., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 0, 21));
		assertEpsilonEquals(20857., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 154, 17, 21));
		assertEpsilonEquals(18617., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 9, 154, 21));
		assertEpsilonEquals(121., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 8, 18, 21));
		assertEpsilonEquals(121., AlignedBox3afp.calculatesAlignedBoxPointDistanceSquared(5, 8, 0, 10, 18, 10, /* */ 7, 12, 21));
	}

	@DisplayName("calculatesAlignedBoxPointDistanceL1(double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticCalculatesAlignedBoxPointDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(18., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 0, -5));
		assertEpsilonEquals(103., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 0, -5));
		assertEpsilonEquals(177., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 100, -5));
		assertEpsilonEquals(92., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 100, -5));
		assertEpsilonEquals(10., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 10, -5));
		assertEpsilonEquals(13., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 0, -5));
		assertEpsilonEquals(149., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 154, 17, -5));
		assertEpsilonEquals(141., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 9, 154, -5));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 8, 18, -5));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 12, -5));

		//

		assertEpsilonEquals(13., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0));
		assertEpsilonEquals(98., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 0, 0));
		assertEpsilonEquals(172., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 100, 0));
		assertEpsilonEquals(87., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 100, 0));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 10, 0));
		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 0, 0));
		assertEpsilonEquals(144., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 154, 17, 0));
		assertEpsilonEquals(136., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 9, 154, 0));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 8, 18, 0));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 12, 0));

		//

		assertEpsilonEquals(13., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 0, 5));
		assertEpsilonEquals(98., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 0, 5));
		assertEpsilonEquals(172., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 100, 5));
		assertEpsilonEquals(87., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 100, 5));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 10, 5));
		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 0, 5));
		assertEpsilonEquals(144., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 154, 17, 5));
		assertEpsilonEquals(136., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 9, 154, 5));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 8, 18, 5));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 12, 5));

		//

		assertEpsilonEquals(13., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 0, 10));
		assertEpsilonEquals(98., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 0, 10));
		assertEpsilonEquals(172., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 100, 10));
		assertEpsilonEquals(87., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 100, 10));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 10, 10));
		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 0, 10));
		assertEpsilonEquals(144., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 154, 17, 10));
		assertEpsilonEquals(136., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 9, 154, 10));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 8, 18, 10));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 12, 10));

		//

		assertEpsilonEquals(24., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 0, 21));
		assertEpsilonEquals(109., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 0, 21));
		assertEpsilonEquals(183., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 100, 100, 21));
		assertEpsilonEquals(98., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 100, 21));
		assertEpsilonEquals(16., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 0, 10, 21));
		assertEpsilonEquals(19., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 0, 21));
		assertEpsilonEquals(155., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 154, 17, 21));
		assertEpsilonEquals(147., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 9, 154, 21));
		assertEpsilonEquals(11., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 8, 18, 21));
		assertEpsilonEquals(11., AlignedBox3afp.calculatesAlignedBoxPointDistanceL1(5, 8, 0, 10, 18, 10, /* */ 7, 12, 21));
	}

	@DisplayName("calculatesAlignedBoxPointDistanceLinf(double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticCalculatesAlignedBoxPointDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 0, -5));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 0, -5));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 100, -5));
		assertEpsilonEquals(82., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 100, -5));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 10, -5));
		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 0, -5));
		assertEpsilonEquals(144., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 154, 17, -5));
		assertEpsilonEquals(136., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 9, 154, -5));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 8, 18, -5));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 12, -5));

		//

		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 0, 0));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 100, 0));
		assertEpsilonEquals(82., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 100, 0));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 10, 0));
		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 0, 0));
		assertEpsilonEquals(144., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 154, 17, 0));
		assertEpsilonEquals(136., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 9, 154, 0));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 8, 18, 0));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 12, 0));

		//

		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 0, 5));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 0, 5));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 100, 5));
		assertEpsilonEquals(82., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 100, 5));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 10, 5));
		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 0, 5));
		assertEpsilonEquals(144., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 154, 17, 5));
		assertEpsilonEquals(136., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 9, 154, 5));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 8, 18, 5));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 12, 5));

		//

		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 0, 10));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 0, 10));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 100, 10));
		assertEpsilonEquals(82., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 100, 10));
		assertEpsilonEquals(5., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 10, 10));
		assertEpsilonEquals(8., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 0, 10));
		assertEpsilonEquals(144., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 154, 17, 10));
		assertEpsilonEquals(136., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 9, 154, 10));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 8, 18, 10));
		assertEpsilonEquals(0., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 12, 10));

		//

		assertEpsilonEquals(11., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 0, 21));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 0, 21));
		assertEpsilonEquals(90., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 100, 100, 21));
		assertEpsilonEquals(82., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 100, 21));
		assertEpsilonEquals(11., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 0, 10, 21));
		assertEpsilonEquals(11., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 0, 21));
		assertEpsilonEquals(144., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 154, 17, 21));
		assertEpsilonEquals(136., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 9, 154, 21));
		assertEpsilonEquals(11., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 8, 18, 21));
		assertEpsilonEquals(11., AlignedBox3afp.calculatesAlignedBoxPointDistanceLinf(5, 8, 0, 10, 18, 10, /* */ 7, 12, 21));
	}

	@DisplayName("findsClosestPointAlignedBoxPoint(double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticFindsClosestPointAlignedBoxPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, -5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, -5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, -5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, -5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, -5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, -5, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, -5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, -5, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, -5, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, -5, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(0, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, 0, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, 0, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, 0, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, 0, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, 0, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, 0, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, 0, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, 0, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, 0, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(0, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, 5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, 5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, 5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, 5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, 5, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, 5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, 5, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, 5, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, 5, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(5, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 10, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, 10, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, 10, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, 10, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, 10, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, 10, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, 10, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, 10, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, 10, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, 10, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 21, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, 21, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, 21, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, 21, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, 21, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, 21, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, 21, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, 21, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, 21, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, 21, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(10, p.getZ());
	}

	@DisplayName("findsFarthestPointAlignedBoxPoint(double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticFindsFarthestPointAlignedBoxPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, -5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, -5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, -5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, -5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, -5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, -5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, -5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, -5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, -5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, -5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, 0, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, 0, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, 0, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, 0, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, 0, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, 0, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, 0, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, 0, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, 0, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, 5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, 5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, 5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, 5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, 5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, 5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, 5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, 5, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, 5, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 10, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, 10, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, 10, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, 10, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, 10, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, 10, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, 10, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, 10, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, 10, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, 10, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 0, 21, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 0, 21, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 100, 100, 21, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 100, 21, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 0, 10, 21, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 0, 21, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 154, 17, 21, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 9, 154, 21, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 8, 18, 21, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxPoint(5, 8, 0, 10, 18, 10, /* */ 7, 12, 21, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	@DisplayName("findsClosestPointAlignedBoxSphere(double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticFindsClosestPointAlignedBoxSphere(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, -5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, -5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, -5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, -5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, -5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, -5, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, -5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, -5, 2, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, -5, 2, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, -5, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(0, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, 0, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, 0, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, 0, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, 0, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, 0, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, 0, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, 0, 2, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, 0, 2, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, 0, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(0, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, 5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, 5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, 5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, 5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, 5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, 5, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, 5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, 5, 2, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, 5, 2, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(5, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, 5, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(5, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, 10, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, 10, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, 10, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, 10, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, 10, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, 10, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, 10, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, 10, 2, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, 10, 2, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, 10, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, 21, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, 21, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, 21, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, 21, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, 21, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, 21, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, 21, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, 21, 2, p);
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, 21, 2, p);
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, 21, 2, p);
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(10, p.getZ());
	}

	@DisplayName("findsFarthestPointAlignedBoxSphere(double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticFindsFarthestPointAlignedBoxSphere(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, -5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, -5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, -5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, -5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, -5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, -5,2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, -5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, -5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, -5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, -5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, 0, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, 0, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, 0, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, 0, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, 0, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, 0, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, 0, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, 0, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, 0, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, 0, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, 5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, 5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, 5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, 5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, 5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, 5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, 5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, 5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, 5, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, 5, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(10, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, 10, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, 10, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, 10, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, 10, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, 10, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, 10, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, 10, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, 10, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, 10, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, 10, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		//

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 0, 21, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 0, 21, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 100, 100, 21, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 100, 21, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 0, 10, 21, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 0, 21, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 154, 17, 21, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 9, 154, 21, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 8, 18, 21, 2, p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(0, p.getZ());

		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxSphere(5, 8, 0, 10, 18, 10, /* */ 7, 12, 21, 2, p);
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(0, p.getZ());
	}

	private static String readCsvCell(String[] columns, int index) {
		try {
			return columns[index].replaceAll("^\"", "").replaceAll("\"$", "");
		} catch (IndexOutOfBoundsException ex) {
			return "0";
		}
	}

	private static double readCsvDouble(String[] columns, int index) {
		return Double.parseDouble(readCsvCell(columns, index));
	}

	private static int readCsvCSC(String[] columns, int index) {
		final String strCode = readCsvCell(columns, index);
		int code = COHEN_SUTHERLAND_INSIDE;
		for (final char it : strCode.toCharArray()) {
			switch (Character.toLowerCase(it)) {
			case 'l':
				code |= COHEN_SUTHERLAND_LEFT;
				break;
			case 'r':
				code |= COHEN_SUTHERLAND_RIGHT;
				break;
			case 'd':
				code |= COHEN_SUTHERLAND_BOTTOM;
				break;
			case 't':
				code |= COHEN_SUTHERLAND_TOP;
				break;
			case 'f':
				code |= COHEN_SUTHERLAND_FRONT;
				break;
			case 'b':
				code |= COHEN_SUTHERLAND_BACK;
				break;
			}
		}
		return code;
	}

	private static String toStringCSC(int code) {
		if (code == 0) {
			return "I";
		}
		final StringBuilder strCode = new StringBuilder();
		if ((code & COHEN_SUTHERLAND_LEFT) != 0) {
			strCode.append('L');
		} else if ((code & COHEN_SUTHERLAND_RIGHT) != 0) {
			strCode.append('R');
		}
		if ((code & COHEN_SUTHERLAND_BOTTOM) != 0) {
			strCode.append('D');
		} else if ((code & COHEN_SUTHERLAND_TOP) != 0) {
			strCode.append('T');
		}
		if ((code & COHEN_SUTHERLAND_FRONT) != 0) {
			strCode.append('F');
		} else if ((code & COHEN_SUTHERLAND_BACK) != 0) {
			strCode.append('B');
		}
		return strCode.toString();
	}

	private static Stream<Arguments> staticFindsCloseFarePointAlignedBoxAlignedBoxArguments(String csvFileBasename) throws Exception {
		final List<Arguments> args = new ArrayList<>();
		final URL resource = Resources.getResource(AbstractAlignedBox3dTestCase.class, csvFileBasename);
		assertNotNull(resource);
		try (final BufferedReader csvReader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
			String line = csvReader.readLine();
			while (line != null) {
				final String[] columns = line.split(",");
				args.add(Arguments.of(
						readCsvCell(columns, 0),
						createTmpPoint(5, 8, 0),
						createTmpPoint(10, 18, 10),
						createTmpPoint(readCsvDouble(columns, 1), readCsvDouble(columns, 2), readCsvDouble(columns, 3)),
						createTmpPoint(readCsvDouble(columns, 4), readCsvDouble(columns, 5), readCsvDouble(columns, 6)),
						createTmpPoint(readCsvDouble(columns, 7), readCsvDouble(columns, 8), readCsvDouble(columns, 9))));
				line = csvReader.readLine();
			}
		}
		return args.stream();
	}

	private static Stream<Arguments> staticFindsClosestPointAlignedBoxAlignedBoxArguments() throws Exception {
		return staticFindsCloseFarePointAlignedBoxAlignedBoxArguments("staticFindsClosestPointAlignedBoxAlignedBoxArguments.csv");
	}

	@DisplayName("findsClosestPointAlignedBoxAlignedBox(double,double,double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("staticFindsClosestPointAlignedBoxAlignedBoxArguments")
	public final void staticFindsClosestPointAlignedBoxAlignedBox(String label,
			Point3D min1, Point3D max1, Point3D min2, Point3D max2, Point3D expected) {
		Point3D p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxAlignedBox(
				min1.getX(), min1.getY(), min1.getZ(), max1.getX(), max1.getY(), max1.getZ(),
				min2.getX(), min2.getY(), min2.getZ(), max2.getX(), max2.getY(), max2.getZ(),
				p);
		assertEpsilonEquals(expected, p, label);
		assertEpsilonInRange(min1.getX(), max1.getX(), p.getX());
		assertEpsilonInRange(min1.getY(), max1.getY(), p.getY());
		assertEpsilonInRange(min1.getZ(), max1.getZ(), p.getZ());
	}

	private static Stream<Arguments> staticFindsFarthestPointAlignedBoxAlignedBoxArguments() throws Exception {
		return staticFindsCloseFarePointAlignedBoxAlignedBoxArguments("staticFindsFarthestPointAlignedBoxAlignedBoxArguments.csv");
	}

	@DisplayName("findsFarthestPointAlignedBoxAlignedBox(double,double,double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("staticFindsFarthestPointAlignedBoxAlignedBoxArguments")
	public final void staticFindsFarthestPointAlignedBoxAlignedBox(String label,
			Point3D min1, Point3D max1, Point3D min2, Point3D max2, Point3D expected) {
		Point3D p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsFarthestPointAlignedBoxAlignedBox(
				min1.getX(), min1.getY(), min1.getZ(), max1.getX(), max1.getY(), max1.getZ(),
				min2.getX(), min2.getY(), min2.getZ(), max2.getX(), max2.getY(), max2.getZ(),
				p);
		assertEpsilonEquals(expected, p, label);
		assertEpsilonInRange(min1.getX(), max1.getX(), p.getX());
		assertEpsilonInRange(min1.getY(), max1.getY(), p.getY());
		assertEpsilonInRange(min1.getZ(), max1.getZ(), p.getZ());
	}


	private static Stream<Arguments> staticReducesCohenSutherlandZoneAlignedBoxSegmentArguments() throws Exception {
		final List<Arguments> args = new ArrayList<>();
		final URL resource = Resources.getResource(AbstractAlignedBox3dTestCase.class, "staticReducesCohenSutherlandZoneAlignedBoxSegmentArguments.csv");
		assertNotNull(resource);
		try (final BufferedReader csvReader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
			// Read the header
			csvReader.readLine();
			String line = csvReader.readLine();
			while (line != null) {
				final String[] columns = line.split(",");
				args.add(Arguments.of(
						readCsvCell(columns, 0),
						createTmpPoint(5, 8, 0),
						createTmpPoint(10, 18, 10),
						createTmpPoint(readCsvDouble(columns, 1), readCsvDouble(columns, 2), readCsvDouble(columns, 3)),
						readCsvCSC(columns, 4),
						createTmpPoint(readCsvDouble(columns, 5), readCsvDouble(columns, 6), readCsvDouble(columns, 7)),
						readCsvCSC(columns, 8),
						readCsvCSC(columns, 9),
						createTmpPoint(readCsvDouble(columns, 10), readCsvDouble(columns, 11), readCsvDouble(columns, 12)),
						createTmpPoint(readCsvDouble(columns, 13), readCsvDouble(columns, 14), readCsvDouble(columns, 15))));
				line = csvReader.readLine();
			}
		}
		return args.stream();
	}

	private static Stream<Arguments> staticFindsCloseFarePointAlignedBoxSegmentArguments(String csvFileBasename) throws Exception {
		final List<Arguments> args = new ArrayList<>();
		final URL resource = Resources.getResource(AbstractAlignedBox3dTestCase.class, csvFileBasename);
		assertNotNull(resource);
		try (final BufferedReader csvReader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
			String line = csvReader.readLine();
			while (line != null) {
				final String[] columns = line.split(",");
				args.add(Arguments.of(
						readCsvCell(columns, 0),
						createTmpPoint(5, 8, 0),
						createTmpPoint(10, 18, 10),
						createTmpPoint(readCsvDouble(columns, 1), readCsvDouble(columns, 2), readCsvDouble(columns, 3)),
						createTmpPoint(readCsvDouble(columns, 4), readCsvDouble(columns, 5), readCsvDouble(columns, 6)),
						createTmpPoint(readCsvDouble(columns, 7), readCsvDouble(columns, 8), readCsvDouble(columns, 9))));
				line = csvReader.readLine();
			}
		}
		return args.stream();
	}

	private static Stream<Arguments> staticFindsClosestPointAlignedBoxSegmentArguments() throws Exception {
		return staticFindsCloseFarePointAlignedBoxSegmentArguments("staticFindsClosestPointAlignedBoxSegmentArguments.csv");
	}

	@DisplayName("findsFarthestPointAlignedBoxSegment(double,double,double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void staticFindsFarthestPointAlignedBoxSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		fail("TODO");
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("reducesCohenSutherlandZoneAlignedBoxSegment w/ special cases")
	public final void reducesCohenSutherlandZoneAlignedBoxSegment(CoordinateSystem3D cs) {
		var result1 = new InnerComputationPoint3D();
		var result2 = new InnerComputationPoint3D();

		var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
				1, 2, 3, 2, 3, 4,
				0, 0, 0, 4, 2.4, 5,
				COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
				COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
				result1, result2);
		assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		assertEpsilonEquals(createPoint(2.4, 1.44, 3), result1);
		assertEpsilonEquals(createPoint(4, 2.4, 5), result2);
	}

	@DisplayName("reducesCohenSutherlandZoneAlignedBoxSegment")
	@Nested
	public class ReduceCohenSutherlandZoneAlignedBoxSegment {

		private InnerComputationPoint3D result1;

		private InnerComputationPoint3D result2;

		@BeforeEach
		public void setUp() {
			this.result1 = new InnerComputationPoint3D();
			this.result2 = new InnerComputationPoint3D();
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-BOTTOM-FRONT")
		public final void ltb_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-BOTTOM-FRONT")
		public final void lbf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-BOTTOM-FRONT")
		public final void ibf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-BOTTOM-FRONT")
		public final void rbf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, .5, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-BOTTOM-FRONT")
		public final void lif_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, .5, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-BOTTOM-FRONT")
		public final void iif_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, .5, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-BOTTOM-FRONT")
		public final void rif_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, .5, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-BOTTOM-FRONT")
		public final void ltf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, .5, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-BOTTOM-FRONT")
		public final void itf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, .5, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-BOTTOM-FRONT")
		public final void rtf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, .5, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-BOTTOM-FRONT")
		public final void lbi_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, .5, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-BOTTOM-FRONT")
		public final void ibi_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, .5, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-BOTTOM-FRONT")
		public final void rbi_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, .5, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-BOTTOM-FRONT")
		public final void lii_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, .5, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-BOTTOM-FRONT")
		public final void iii_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, .5, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.3, 2.2, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-BOTTOM-FRONT")
		public final void rii_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, .5, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-BOTTOM-FRONT")
		public final void lti_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, .5, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-BOTTOM-FRONT")
		public final void iti_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, .5, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-BOTTOM-FRONT")
		public final void rti_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, .5, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-BOTTOM-FRONT")
		public final void lbf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-BOTTOM-FRONT")
		public final void ibf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-BOTTOM-FRONT")
		public final void rbf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 1.6, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-BOTTOM-FRONT")
		public final void lif_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 1.6, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-BOTTOM-FRONT")
		public final void iif_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 1.6, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-BOTTOM-FRONT")
		public final void rif_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 1.6, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-BOTTOM-FRONT")
		public final void ltf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 1.6, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-BOTTOM-FRONT")
		public final void itf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 1.6, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-BOTTOM-FRONT")
		public final void rtf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 1.6, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-BOTTOM-FRONT")
		public final void lbi_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 1.6, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-BOTTOM-FRONT")
		public final void ibi_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 1.6, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-BOTTOM-FRONT")
		public final void rbi_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 1.6, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-BOTTOM-FRONT")
		public final void lii_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 1.6, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-BOTTOM-FRONT")
		public final void iii_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 1.6, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.52, 2.2, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-BOTTOM-FRONT")
		public final void rii_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 1.6, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-BOTTOM-FRONT")
		public final void lti_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 1.6, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE| COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-BOTTOM-FRONT")
		public final void iti_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 1.6, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-BOTTOM-FRONT")
		public final void rti_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 1.6, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-BOTTOM-FRONT")
		public final void lbf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 4, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-BOTTOM-FRONT")
		public final void ibf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 4, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-BOTTOM-FRONT")
		public final void rbf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 4, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-BOTTOM-FRONT")
		public final void lif_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 4, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-BOTTOM-FRONT")
		public final void iif_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 4, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-BOTTOM-FRONT")
		public final void rif_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 4, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-BOTTOM-FRONT")
		public final void ltf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 4, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-BOTTOM-FRONT")
		public final void itf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 4, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-BOTTOM-FRONT")
		public final void rtf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 4, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-BOTTOM-FRONT")
		public final void lbi_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 4, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-BOTTOM-FRONT")
		public final void ibi_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 4, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-BOTTOM-FRONT")
		public final void rbi_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 4, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-BOTTOM-FRONT")
		public final void lii_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 4, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-BOTTOM-FRONT")
		public final void iii_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 4, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(2, 2.2, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-BOTTOM-FRONT")
		public final void rii_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 4, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-BOTTOM-FRONT")
		public final void lti_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 4, 1, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-BOTTOM-FRONT")
		public final void iti_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 4, 1, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-BOTTOM-FRONT")
		public final void rti_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 4, 1, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-INSIDE-FRONT")
		public final void lbf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-INSIDE-FRONT")
		public final void ibf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-INSIDE-FRONT")
		public final void rbf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, .5, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-INSIDE-FRONT")
		public final void lif_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, .5, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-INSIDE-FRONT")
		public final void iif_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, .5, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-INSIDE-FRONT")
		public final void rif_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, .5, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-INSIDE-FRONT")
		public final void ltf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, .5, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-INSIDE-FRONT")
		public final void itf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, .5, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-INSIDE-FRONT")
		public final void rtf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, .5, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-INSIDE-FRONT")
		public final void lbi_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, .5, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-INSIDE-FRONT")
		public final void ibi_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, .5, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-INSIDE-FRONT")
		public final void rbi_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, .5, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-INSIDE-FRONT")
		public final void lii_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, .5, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-INSIDE-FRONT")
		public final void iii_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, .5, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.3, 2.48, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-INSIDE-FRONT")
		public final void rii_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, .5, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-INSIDE-FRONT")
		public final void lti_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, .5, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-INSIDE-FRONT")
		public final void iti_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, .5, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-INSIDE-FRONT")
		public final void rti_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, .5, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-INSIDE-FRONT")
		public final void lbf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-INSIDE-FRONT")
		public final void ibf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-INSIDE-FRONT")
		public final void rbf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-INSIDE-FRONT")
		public final void lif_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-INSIDE-FRONT")
		public final void iif_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-INSIDE-FRONT")
		public final void rif_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-INSIDE-FRONT")
		public final void ltf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-INSIDE-FRONT")
		public final void itf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-INSIDE-FRONT")
		public final void rtf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-INSIDE-FRONT")
		public final void lbi_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-INSIDE-FRONT")
		public final void ibi_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-INSIDE-FRONT")
		public final void rbi_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-INSIDE-FRONT")
		public final void lii_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-INSIDE-FRONT")
		public final void iii_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.52, 2.48, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-INSIDE-FRONT")
		public final void rii_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-INSIDE-FRONT")
		public final void lti_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-INSIDE-FRONT")
		public final void iti_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-INSIDE-FRONT")
		public final void rti_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 1.6, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGH-INSIDE-FRONT")
		public final void lbf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 4, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGH-INSIDE-FRONT")
		public final void ibf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 4, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGH-INSIDE-FRONT")
		public final void rbf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 4, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGH-INSIDE-FRONT")
		public final void lif_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 4, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGH-INSIDE-FRONT")
		public final void iif_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 4, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGH-INSIDE-FRONT")
		public final void rif_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 4, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGH-INSIDE-FRONT")
		public final void ltf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 4, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGH-INSIDE-FRONT")
		public final void itf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 4, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGH-INSIDE-FRONT")
		public final void rtf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 4, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGH-INSIDE-FRONT")
		public final void lbi_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 4, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGH-INSIDE-FRONT")
		public final void ibi_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 4, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGH-INSIDE-FRONT")
		public final void rbi_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 4, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGH-INSIDE-FRONT")
		public final void lii_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 4, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGH-INSIDE-FRONT")
		public final void iii_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 4, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(2, 2.48, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGH-INSIDE-FRONT")
		public final void rii_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 4, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGH-INSIDE-FRONT")
		public final void lti_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 4, 2.4, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGH-INSIDE-FRONT")
		public final void iti_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 4, 2.4, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGH-INSIDE-FRONT")
		public final void rti_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 4, 2.4, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-TOP-FRONT")
		public final void lbf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-TOP-FRONT")
		public final void ibf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-TOP-FRONT")
		public final void rbf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, .5, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-TOP-FRONT")
		public final void lif_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, .5, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-TOP-FRONT")
		public final void iif_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, .5, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-TOP-FRONT")
		public final void rif_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, .5, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-TOP-FRONT")
		public final void ltf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, .5, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-TOP-FRONT")
		public final void itf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, .5, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-TOP-FRONT")
		public final void rtf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, .5, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-TOP-FRONT")
		public final void lbi_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, .5, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-TOP-FRONT")
		public final void ibi_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, .5, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-TOP-FRONT")
		public final void rbi_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, .5, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-TOP-FRONT")
		public final void lii_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, .5, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-TOP-FRONT")
		public final void iii_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, .5, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.3, 3, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-TOP-FRONT")
		public final void rii_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, .5, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-TOP-FRONT")
		public final void lti_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, .5, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-TOP-FRONT")
		public final void iti_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, .5, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-TOP-FRONT")
		public final void rti_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, .5, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-TOP-FRONT")
		public final void lbf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-TOP-FRONT")
		public final void ibf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-TOP-FRONT")
		public final void rbf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 1.6, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-TOP-FRONT")
		public final void lif_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 1.6, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-TOP-FRONT")
		public final void iif_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 1.6, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-TOP-FRONT")
		public final void rif_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 1.6, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-TOP-FRONT")
		public final void ltf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 1.6, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-TOP-FRONT")
		public final void itf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 1.6, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-TOP-FRONT")
		public final void rtf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 1.6, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-TOP-FRONT")
		public final void lbi_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 1.6, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-TOP-FRONT")
		public final void ibi_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 1.6, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-TOP-FRONT")
		public final void rbi_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 1.6, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-TOP-FRONT")
		public final void lii_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 1.6, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-TOP-FRONT")
		public final void iii_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 1.6, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.52, 3, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-TOP-FRONT")
		public final void rii_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 1.6, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-TOP-FRONT")
		public final void lti_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 1.6, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-TOP-FRONT")
		public final void iti_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 1.6, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-TOP-FRONT")
		public final void rti_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 1.6, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-TOP-FRONT")
		public final void lbf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 4, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-TOP-FRONT")
		public final void ibf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 4, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-TOP-FRONT")
		public final void rbf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 4, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-TOP-FRONT")
		public final void lif_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 4, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-TOP-FRONT")
		public final void iif_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 4, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-TOP-FRONT")
		public final void rif_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 4, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-TOP-FRONT")
		public final void ltf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 4, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-TOP-FRONT")
		public final void itf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 4, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-TOP-FRONT")
		public final void rtf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 4, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-TOP-FRONT")
		public final void lbi_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 4, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-TOP-FRONT")
		public final void ibi_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 4, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-TOP-FRONT")
		public final void rbi_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 4, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-TOP-FRONT")
		public final void lii_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 4, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-TOP-FRONT")
		public final void iii_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 4, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(2, 3, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-TOP-FRONT")
		public final void rii_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 4, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-TOP-FRONT")
		public final void lti_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 4, 5, 1,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-TOP-FRONT")
		public final void iti_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 4, 5, 1,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-TOP-FRONT")
		public final void rti_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 4, 5, 1,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-BOTTOM-INSIDE")
		public final void lbf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-BOTTOM-INSIDE")
		public final void ibf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-BOTTOM-INSIDE")
		public final void rbf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, .5, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-BOTTOM-INSIDE")
		public final void lif_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, .5, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-BOTTOM-INSIDE")
		public final void iif_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, .5, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-BOTTOM-INSIDE")
		public final void rif_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, .5, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-BOTTOM-INSIDE")
		public final void ltf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, .5, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-BOTTOM-INSIDE")
		public final void itf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, .5, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-BOTTOM-INSIDE")
		public final void rtf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, .5, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-BOTTOM-INSIDE")
		public final void lbi_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, .5, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-BOTTOM-INSIDE")
		public final void ibi_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, .5, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-BOTTOM-INSIDE")
		public final void rbi_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, .5, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-BOTTOM-INSIDE")
		public final void lii_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, .5, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-BOTTOM-INSIDE")
		public final void iii_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, .5, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.166666666, 2, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-BOTTOM-INSIDE")
		public final void rii_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, .5, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-BOTTOM-INSIDE")
		public final void lti_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, .5, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-BOTTOM-INSIDE")
		public final void iti_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, .5, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1, 3, 3.35), result1);
			assertEpsilonEquals(createPoint(1, 3, 3.35), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-BOTTOM-INSIDE")
		public final void rti_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, .5, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(2, 2.33333333, 3.3), result1);
			assertEpsilonEquals(createPoint(2, 2.33333333, 3.3), result1);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-BOTTOM-INSIDE")
		public final void lbf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-BOTTOM-INSIDE")
		public final void ibf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-BOTTOM-INSIDE")
		public final void rbf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-BOTTOM-INSIDE")
		public final void lif_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-BOTTOM-INSIDE")
		public final void iif_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-BOTTOM-INSIDE")
		public final void rif_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-BOTTOM-INSIDE")
		public final void ltf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-BOTTOM-INSIDE")
		public final void itf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-BOTTOM-INSIDE")
		public final void rtf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void lbi_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void ibi_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void rbi_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void lii_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void iii_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.5333333333, 2, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void rii_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void lti_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1, 2.5, 3.3125), result1);
			assertEpsilonEquals(createPoint(1.2, 2, 3.275), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void iti_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.55, 3, 3.35), result1);
			assertEpsilonEquals(createPoint(1.575, 2, 3.275), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void rti_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 1.6, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-BOTTOM-INSIDE")
		public final void lbf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 4, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-BOTTOM-INSIDE")
		public final void ibf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 4, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-BOTTOM-INSIDE")
		public final void rbf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 4, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-BOTTOM-INSIDE")
		public final void lif_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 4, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-BOTTOM-INSIDE")
		public final void iif_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 4, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-BOTTOM-INSIDE")
		public final void rif_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 4, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-BOTTOM-INSIDE")
		public final void ltf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 4, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-BOTTOM-INSIDE")
		public final void itf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 4, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-BOTTOM-INSIDE")
		public final void rtf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 4, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void lbi_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 4, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void ibi_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 4, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void rbi_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 4, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void lii_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 4, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1, 2.125, 3.425), result1);
			assertEpsilonEquals(createPoint(1.33333333, 2, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void iii_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 4, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(2, 2.2, 3.44), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void rii_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 4, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void lti_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 4, 1, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(2, 3, 3.35), result1);
			assertEpsilonEquals(createPoint(2, 3, 3.35), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void iti_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 4, 1, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void rti_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 4, 1, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-INSIDE-INSIDE")
		public final void lbf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-INSIDE-INSIDE")
		public final void ibf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-INSIDE-INSIDE")
		public final void rbf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-INSIDE-INSIDE")
		public final void lif_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-INSIDE-INSIDE")
		public final void iif_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-INSIDE-INSIDE")
		public final void rif_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-INSIDE-INSIDE")
		public final void ltf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-INSIDE-INSIDE")
		public final void itf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-INSIDE-INSIDE")
		public final void rtf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-INSIDE-INSIDE")
		public final void lbi_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-INSIDE-INSIDE")
		public final void ibi_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-INSIDE-INSIDE")
		public final void rbi_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.25, 2, 3.25), result1);
			assertEpsilonEquals(createPoint(1, 2.1333333, 3.2333333), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-INSIDE-INSIDE")
		public final void lii_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-INSIDE-INSIDE")
		public final void iii_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1, 2.45, 3.35), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-INSIDE-INSIDE")
		public final void rii_lii(CoordinateSystem3D cs) {

			// P1: RIGHT/INSIDE/INSIDE
			// P2: LEFT/INSIDE/INSIDE
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(2, 2.433333, 3.3), result1);
			assertEpsilonEquals(createPoint(1, 2.411111111, 3.233333333), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-INSIDE-INSIDE")
		public final void lti_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-INSIDE-INSIDE")
		public final void iti_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-INSIDE-INSIDE")
		public final void rti_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, .5, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.53846154, 3, 3.269231), result1);
			assertEpsilonEquals(createPoint(1, 2.6888888, 3.23333333), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-INSIDE-INSIDE")
		public final void lbf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.25, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-INSIDE-INSIDE")
		public final void ibf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.59375, 2.25, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-INSIDE-INSIDE")
		public final void rbf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.8125, 2.25, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-INSIDE-INSIDE")
		public final void lif_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.40625, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-INSIDE-INSIDE")
		public final void iif_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.59375, 2.40625, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-INSIDE-INSIDE")
		public final void rif_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.8125, 2.40625, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-INSIDE-INSIDE")
		public final void ltf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5625, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-INSIDE-INSIDE")
		public final void itf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.59375, 2.5625, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-INSIDE-INSIDE")
		public final void rtf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.8125, 2.5625, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-INSIDE-INSIDE")
		public final void lbi_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.3333333, 2, 3.25), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-INSIDE-INSIDE")
		public final void ibi_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.583333333, 2, 3.25), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-INSIDE-INSIDE")
		public final void rbi_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(2, 2.1176471, 3.23529412), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-INSIDE-INSIDE")
		public final void lii_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1, 2.4375, 3.3125), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-INSIDE-INSIDE")
		public final void iii_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-INSIDE-INSIDE")
		public final void rii_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(2, 2.41176471, 3.23529412), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-INSIDE-INSIDE")
		public final void lti_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.230769231, 3, 3.26923077), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-INSIDE-INSIDE")
		public final void iti_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5769231, 3, 3.26923077), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-INSIDE-INSIDE")
		public final void rti_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 1.6, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(2, 2.70588235, 3.23529412), result1);
			assertEpsilonEquals(createPoint(1.6, 2.4, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-INSIDE-INSIDE")
		public final void lbf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-INSIDE-INSIDE")
		public final void ibf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-INSIDE-INSIDE")
		public final void rbf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-INSIDE-INSIDE")
		public final void lif_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-INSIDE-INSIDE")
		public final void iif_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-INSIDE-INSIDE")
		public final void rif_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-INSIDE-INSIDE")
		public final void ltf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-INSIDE-INSIDE")
		public final void itf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-INSIDE-INSIDE")
		public final void rtf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-INSIDE-INSIDE")
		public final void lbi_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-INSIDE-INSIDE")
		public final void ibi_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-INSIDE-INSIDE")
		public final void rbi_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-INSIDE-INSIDE")
		public final void lii_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1, 2.475, 3.425), result1);
			assertEpsilonEquals(createPoint(2, 2.45, 3.35), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-INSIDE-INSIDE")
		public final void iii_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(2, 2.48, 3.44), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-INSIDE-INSIDE")
		public final void rii_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-INSIDE-INSIDE")
		public final void lti_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-INSIDE-INSIDE")
		public final void iti_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-INSIDE-INSIDE")
		public final void rti_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 4, 2.4, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-TOP-INSIDE")
		public final void lbf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-TOP-INSIDE")
		public final void ibf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-TOP-INSIDE")
		public final void rbf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, .5, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-TOP-INSIDE")
		public final void lif_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, .5, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-TOP-INSIDE")
		public final void iif_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, .5, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-TOP-INSIDE")
		public final void rif_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, .5, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-TOP-INSIDE")
		public final void ltf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, .5, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-TOP-INSIDE")
		public final void itf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, .5, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-TOP-INSIDE")
		public final void rtf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, .5, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-TOP-INSIDE")
		public final void lbi_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, .5, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-TOP-INSIDE")
		public final void ibi_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, .5, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.1, 2, 3.38), result1);
			assertEpsilonEquals(createPoint(1, 2.5, 3.349999999), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-TOP-INSIDE")
		public final void rbi_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, .5, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-TOP-INSIDE")
		public final void lii_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, .5, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-TOP-INSIDE")
		public final void iii_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, .5, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.3, 3, 3.44), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-TOP-INSIDE")
		public final void rii_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, .5, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-TOP-INSIDE")
		public final void lti_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, .5, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-TOP-INSIDE")
		public final void iti_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, .5, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-TOP-INSIDE")
		public final void rti_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, .5, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-TOP-INSIDE")
		public final void lbf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-TOP-INSIDE")
		public final void ibf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-TOP-INSIDE")
		public final void rbf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-TOP-INSIDE")
		public final void lif_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-TOP-INSIDE")
		public final void iif_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-TOP-INSIDE")
		public final void rif_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-TOP-INSIDE")
		public final void ltf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-TOP-INSIDE")
		public final void itf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-TOP-INSIDE")
		public final void rtf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-TOP-INSIDE")
		public final void lbi_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-TOP-INSIDE")
		public final void ibi_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.54, 2, 3.38), result1);
			assertEpsilonEquals(createPoint(1.56, 3, 3.32), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-TOP-INSIDE")
		public final void rbi_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-TOP-INSIDE")
		public final void lii_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-TOP-INSIDE")
		public final void iii_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.52, 3, 3.44), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-TOP-INSIDE")
		public final void rii_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-TOP-INSIDE")
		public final void lti_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-TOP-INSIDE")
		public final void iti_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-TOP-INSIDE")
		public final void rti_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 1.6, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-TOP-INSIDE")
		public final void lbf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 4, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-TOP-INSIDE")
		public final void ibf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 4, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-TOP-INSIDE")
		public final void rbf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 4, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-TOP-INSIDE")
		public final void lif_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 4, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-TOP-INSIDE")
		public final void iif_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 4, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-TOP-INSIDE")
		public final void rif_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 4, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-TOP-INSIDE")
		public final void ltf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 4, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-TOP-INSIDE")
		public final void itf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 4, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-TOP-INSIDE")
		public final void rtf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 4, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-TOP-INSIDE")
		public final void lbi_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 4, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.6, 2, 3.38), result1);
			assertEpsilonEquals(createPoint(2, 2.5, 3.35), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-TOP-INSIDE")
		public final void ibi_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 4, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-TOP-INSIDE")
		public final void rbi_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 4, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-TOP-INSIDE")
		public final void lii_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 4, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-TOP-INSIDE")
		public final void iii_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 4, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(2, 3, 3.44), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-TOP-INSIDE")
		public final void rii_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 4, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-TOP-INSIDE")
		public final void lti_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 4, 5, 3.2,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-TOP-INSIDE")
		public final void iti_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 4, 5, 3.2,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-TOP-INSIDE")
		public final void rti_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 4, 5, 3.2,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-BOTTOM-BACK")
		public final void lbf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-BOTTOM-BACK")
		public final void ibf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-BOTTOM-BACK")
		public final void rbf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, .5, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-BOTTOM-BACK")
		public final void lif_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, .5, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-BOTTOM-BACK")
		public final void iif_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, .5, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-BOTTOM-BACK")
		public final void rif_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, .5, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-BOTTOM-BACK")
		public final void ltf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, .5, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-BOTTOM-BACK")
		public final void itf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, .5, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-BOTTOM-BACK")
		public final void rtf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, .5, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(2, 2.3333333, 3.33333333), result1);
			assertEpsilonEquals(createPoint(1.625, 2, 3.75), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-BOTTOM-BACK")
		public final void lbi_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, .5, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-BOTTOM-BACK")
		public final void ibi_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, .5, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-BOTTOM-BACK")
		public final void rbi_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, .5, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-BOTTOM-BACK")
		public final void lii_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, .5, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-BOTTOM-BACK")
		public final void iii_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, .5, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.166666666, 2, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-BOTTOM-BACK")
		public final void rii_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, .5, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-BOTTOM-BACK")
		public final void lti_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, .5, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-BOTTOM-BACK")
		public final void iti_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, .5, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-BOTTOM-BACK")
		public final void rti_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, .5, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-BOTTOM-BACK")
		public final void lbf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-BOTTOM-BACK")
		public final void ibf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-BOTTOM-BACK")
		public final void rbf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 1.6, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-BOTTOM-BACK")
		public final void lif_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 1.6, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-BOTTOM-BACK")
		public final void iif_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 1.6, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.56, 1.6, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-BOTTOM-BACK")
		public final void rif_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 1.6, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-BOTTOM-BACK")
		public final void ltf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 1.6, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1, 2.5, 3.125), result1);
			assertEpsilonEquals(createPoint(1.2, 2, 3.75), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-BOTTOM-BACK")
		public final void itf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 1.6, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.56, 2.6, 3), result1);
			assertEpsilonEquals(createPoint(1.575, 2, 3.75), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-BOTTOM-BACK")
		public final void rtf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 1.6, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-BOTTOM-BACK")
		public final void lbi_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 1.6, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-BOTTOM-BACK")
		public final void ibi_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 1.6, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-BOTTOM-BACK")
		public final void rbi_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 1.6, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-BOTTOM-BACK")
		public final void lii_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 1.6, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-BOTTOM-BACK")
		public final void iii_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 1.6, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.5333333333, 2, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-BOTTOM-BACK")
		public final void rii_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 1.6, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-BOTTOM-BACK")
		public final void lti_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 1.6, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-BOTTOM-BACK")
		public final void iti_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 1.6, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-BOTTOM-BACK")
		public final void rti_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 1.6, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-BOTTOM-BACK")
		public final void lbf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 4, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-BOTTOM-BACK")
		public final void ibf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 4, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-BOTTOM-BACK")
		public final void rbf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 4, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-BOTTOM-BACK")
		public final void lif_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 4, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-BOTTOM-BACK")
		public final void iif_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 4, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-BOTTOM-BACK")
		public final void rif_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 4, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-BOTTOM-BACK")
		public final void ltf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 4, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-BOTTOM-BACK")
		public final void itf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 4, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-BOTTOM-BACK")
		public final void rtf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 4, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-BOTTOM-BACK")
		public final void lbi_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 4, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-BOTTOM-BACK")
		public final void ibi_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 4, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-BOTTOM-BACK")
		public final void rbi_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 4, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-BOTTOM-BACK")
		public final void lii_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 4, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1, 2.125, 3.875), result1);
			assertEpsilonEquals(createPoint(1.33333333, 2, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-BOTTOM-BACK")
		public final void iii_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 4, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(2, 2.2, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-BOTTOM-BACK")
		public final void rii_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 4, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-BOTTOM-BACK")
		public final void lti_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 4, 1, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-BOTTOM-BACK")
		public final void iti_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 4, 1, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-BOTTOM-BACK")
		public final void rti_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 4, 1, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-INSIDE-BACK")
		public final void lbf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-INSIDE-BACK")
		public final void ibf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-INSIDE-BACK")
		public final void rbf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, .5, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-INSIDE-BACK")
		public final void lif_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, .5, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-INSIDE-BACK")
		public final void iif_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, .5, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-INSIDE-BACK")
		public final void rif_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, .5, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(2, 2.4333333, 3.33333333), result1);
			assertEpsilonEquals(createPoint(1.4, 2.42, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-INSIDE-BACK")
		public final void ltf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, .5, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-INSIDE-BACK")
		public final void itf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, .5, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-INSIDE-BACK")
		public final void rtf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, .5, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.53846153846, 3, 3.8461538462), result1);
			assertEpsilonEquals(createPoint(1.4, 2.92, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-INSIDE-BACK")
		public final void lbi_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, .5, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-INSIDE-BACK")
		public final void ibi_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, .5, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-INSIDE-BACK")
		public final void rbi_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, .5, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-INSIDE-BACK")
		public final void lii_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, .5, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-INSIDE-BACK")
		public final void iii_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, .5, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.166666666, 2.4666666666, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-INSIDE-BACK")
		public final void rii_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, .5, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-INSIDE-BACK")
		public final void lti_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, .5, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-INSIDE-BACK")
		public final void iti_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, .5, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-INSIDE-BACK")
		public final void rti_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, .5, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-INSIDE-BACK")
		public final void lbf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-INSIDE-BACK")
		public final void ibf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-INSIDE-BACK")
		public final void rbf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-INSIDE-BACK")
		public final void lif_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1, 2.4375, 3.125), result1);
			assertEpsilonEquals(createPoint(1.28, 2.42, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-INSIDE-BACK")
		public final void iif_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.56, 2.44, 3), result1);
			assertEpsilonEquals(createPoint(1.58, 2.42, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-INSIDE-BACK")
		public final void rif_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-INSIDE-BACK")
		public final void ltf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.23076923, 3, 3.846153846), result1);
			assertEpsilonEquals(createPoint(1.28, 2.92, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-INSIDE-BACK")
		public final void itf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.576923077, 3, 3.8461538462), result1);
			assertEpsilonEquals(createPoint(1.58, 2.92, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-INSIDE-BACK")
		public final void rtf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-INSIDE-BACK")
		public final void lbi_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("ISIDE-BOTTOM-INSIDE INSIDE-INSIDE-BACK")
		public final void ibi_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-INSIDE-BACK")
		public final void rbi_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-INSIDE-BACK")
		public final void lii_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-INSIDE-BACK")
		public final void iii_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.53333333333, 2.46666666666666, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-INSIDE-BACK")
		public final void rii_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-INSIDE-BACK")
		public final void lti_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-INSIDE-BACK")
		public final void iti_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-INSIDE-BACK")
		public final void rti_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 1.6, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-INSIDE-BACK")
		public final void lbf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 4, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-INSIDE-BACK")
		public final void ibf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 4, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-INSIDE-BACK")
		public final void rbf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 4, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-INSIDE-BACK")
		public final void lif_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 4, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-INSIDE-BACK")
		public final void iif_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 4, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-INSIDE-BACK")
		public final void rif_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 4, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-INSIDE-BACK")
		public final void ltf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 4, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-INSIDE-BACK")
		public final void itf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 4, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-INSIDE-BACK")
		public final void rtf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 4, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-INSIDE-BACK")
		public final void lbi_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 4, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-INSIDE-BACK")
		public final void ibi_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 4, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-INSIDE-BACK")
		public final void rbi_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 4, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-INSIDE-BACK")
		public final void lii_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 4, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1, 2.475, 3.875), result1);
			assertEpsilonEquals(createPoint(1.33333333, 2.4666666666, 4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-INSIDE-BACK")
		public final void iii_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 4, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(2, 2.48, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-INSIDE-BACK")
		public final void rii_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 4, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-INSIDE-BACK")
		public final void lti_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 4, 2.4, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-INSIDE-BACK")
		public final void iti_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 4, 2.4, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-INSIDE-BACK")
		public final void rti_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 4, 2.4, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-TOP-BACK")
		public final void lbf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-TOP-BACK")
		public final void ibf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-TOP-BACK")
		public final void rbf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, .5, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-TOP-BACK")
		public final void lif_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, .5, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-TOP-BACK")
		public final void iif_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, .5, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-TOP-BACK")
		public final void rif_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, .5, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-TOP-BACK")
		public final void ltf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, .5, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-TOP-BACK")
		public final void itf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, .5, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-TOP-BACK")
		public final void rtf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, .5, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-TOP-BACK")
		public final void lbi_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, .5, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-TOP-BACK")
		public final void ibi_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, .5, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-TOP-BACK")
		public final void rbi_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, .5, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-TOP-BACK")
		public final void lii_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, .5, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-TOP-BACK")
		public final void iii_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, .5, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.3, 3, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-TOP-BACK")
		public final void rii_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, .5, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-TOP-BACK")
		public final void lti_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, .5, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-TOP-BACK")
		public final void iti_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, .5, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-TOP-BACK")
		public final void rti_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, .5, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-TOP-BACK")
		public final void lbf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-TOP-BACK")
		public final void ibf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.56, 3, 3), result1);
			assertEpsilonEquals(createPoint(1.56, 3, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-TOP-BACK")
		public final void rbf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 1.6, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-TOP-BACK")
		public final void lif_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 1.6, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-TOP-BACK")
		public final void iif_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 1.6, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-TOP-BACK")
		public final void rif_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 1.6, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-TOP-BACK")
		public final void ltf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 1.6, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-TOP-BACK")
		public final void itf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 1.6, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-TOP-BACK")
		public final void rtf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 1.6, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-TOP-BACK")
		public final void lbi_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 1.6, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-TOP-BACK")
		public final void ibi_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 1.6, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-TOP-BACK")
		public final void rbi_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 1.6, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-TOP-BACK")
		public final void lii_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 1.6, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-TOP-BACK")
		public final void iii_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 1.6, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(1.52, 3, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-TOP-BACK")
		public final void rii_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 1.6, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-TOP-BACK")
		public final void lti_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 1.6, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-TOP-BACK")
		public final void iti_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 1.6, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-TOP-BACK")
		public final void rti_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 1.6, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-TOP-BACK")
		public final void lbf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 4, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-TOP-BACK")
		public final void ibf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 4, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-TOP-BACK")
		public final void rbf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 0, 4, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-TOP-BACK")
		public final void lif_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 0, 4, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-TOP-BACK")
		public final void iif_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 0, 4, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-TOP-BACK")
		public final void rif_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 0, 4, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-TOP-BACK")
		public final void ltf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 0, 4, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-TOP-BACK")
		public final void itf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 0, 4, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-TOP-BACK")
		public final void rstf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 0, 4, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_FRONT,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-TOP-BACK")
		public final void lbi_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.5, 4, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-TOP-BACK")
		public final void ibi_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.5, 4, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_BACK, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-TOP-BACK")
		public final void rbi_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 0, 3.5, 4, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_BOTTOM | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-TOP-BACK")
		public final void lii_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.5, 3.5, 4, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-TOP-BACK")
		public final void iii_rtb(CoordinateSystem3D cs) {
			// P1: INSIDE/INSIDE/INSIDE
			// P2: RIGHT/TOP/BACK
			var zone = AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.5, 3.5, 4, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
			assertEpsilonEquals(createPoint(1.5, 2.5, 3.5), result1);
			assertEpsilonEquals(createPoint(2, 3, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-TOP-BACK")
		public final void rii_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 2.5, 3.5, 4, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-TOP-BACK")
		public final void lti_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 5, 3.5, 4, 5, 5,
					COHEN_SUTHERLAND_LEFT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-TOP-BACK")
		public final void iti_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 5, 3.5, 4, 5, 5,
					COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_INSIDE | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-TOP-BACK")
		public final void rti_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			var zone =  AlignedBox3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					5, 5, 3.5, 4, 5, 5,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE,
					COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_BACK,
					result1, result2);
			assertEquals(COHEN_SUTHERLAND_RIGHT | COHEN_SUTHERLAND_TOP | COHEN_SUTHERLAND_INSIDE, zone);
		}

	}

	private static InnerComputationPoint3D reset(InnerComputationPoint3D p) {
		p.set(Double.NaN, Double.NaN, Double.NaN);
		return p;
	}

	@DisplayName("findsClosestPointAlignedBoxSegment(bx1,by1,bz1,bx2,by2,bz2, sx1,sy1,sz1,sx2,sy2,sz2, Point3D,Point3D)")
	@Nested
	public class FindsClosestPointAlignedBoxSegment {
	
		private InnerComputationPoint3D result1;

		private InnerComputationPoint3D result2;
		
		@BeforeEach
		public void setUp() {
			this.result1 = new InnerComputationPoint3D();
			this.result2 = new InnerComputationPoint3D();
		}
		
		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-BOTTOM-FRONT")
		public final void lbf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-BOTTOM-FRONT")
		public final void ibf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-BOTTOM-FRONT")
		public final void rbf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-BOTTOM-FRONT")
		public final void lif_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-BOTTOM-FRONT")
		public final void iif_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-BOTTOM-FRONT")
		public final void rif_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.6149425287, 1.0804597701, 0.9425287356), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-BOTTOM-FRONT")
		public final void ltf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.4781523096, 1.11360799, 0.9563046192), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-BOTTOM-FRONT")
		public final void itf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.6255707763, 1.3264840183, 0.8744292237), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-BOTTOM-FRONT")
		public final void rtf_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.7721088435, 1.3537414966, 0.8639455782), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-BOTTOM-FRONT")
		public final void lbi_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.2467902996, 0.4935805991, 2.2154065621), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-BOTTOM-FRONT")
		public final void ibi_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0621301775147929,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.0621301775, 0.4378698225, 2.349112426), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-BOTTOM-FRONT")
		public final void rbi_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6242603550295858,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.624260355, 0.4378698225, 2.349112426), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-BOTTOM-FRONT")
		public final void lii_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.059900166389351,3.0), result1);
			assertEpsilonEquals(createPoint(0.1214642263, 2.0599001664, 2.8169717138), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-BOTTOM-FRONT")
		public final void iii_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.41666666666667, 2.283333333333, 3.2), result1);
			assertEpsilonEquals(createPoint(1.41666666666667, 2.283333333333, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-BOTTOM-FRONT")
		public final void rii_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.1188524590163933,3.0), result1);
			assertEpsilonEquals(createPoint(2.0983606557, 2.118852459, 2.9180327869), result2);
		}


		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-BOTTOM-FRONT")
		public final void lti_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.968386023294509,3.0), result1);
			assertEpsilonEquals(createPoint(0.1214642263, 2.9683860233, 2.8169717138), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-BOTTOM-FRONT")
		public final void iti_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2987220447284344,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.2987220447, 3.0766773163, 2.9169329073), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-BOTTOM-FRONT")
		public final void rti_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.0738498789, 3.0460048426, 2.8886198547), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK LEFT-BOTTOM-FRONT")
		public final void lbb_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.3043478261, 0.6086956522, 2.5652173913), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK LEFT-BOTTOM-FRONT")
		public final void ibb_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.9166666667, 0.5833333333, 2.6666666667), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK LEFT-BOTTOM-FRONT")
		public final void rbb_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3235294117647058,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.3235294118, 0.5882352941, 2.6470588235), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK LEFT-BOTTOM-FRONT")
		public final void lib_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.0814479638009047), result1);
			assertEpsilonEquals(createPoint(0.2398190045, 1.7285067873, 3.0814479638), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK LEFT-BOTTOM-FRONT")
		public final void iib_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2321428571428572,2.025,3.928571428571429), result1);
			assertEpsilonEquals(createPoint(1.2321428571428572,2.025,3.928571428571429), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK LEFT-BOTTOM-FRONT")
		public final void rib_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9642857142857144,2.025,3.928571428571429), result1);
			assertEpsilonEquals(createPoint(1.9642857143, 2.025, 3.9285714286), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK LEFT-BOTTOM-FRONT")
		public final void ltb_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.24, 3), result1);
			assertEpsilonEquals(createPoint(0.2615384615, 2.24, 2.9076923077), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK LEFT-BOTTOM-FRONT")
		public final void itb_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.125, 2.625, 3.5), result1);
			assertEpsilonEquals(createPoint(1.125, 2.625, 3.5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK LEFT-BOTTOM-FRONT")
		public final void rtb_lbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, .5, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.75, 2.625, 3.5), result1);
			assertEpsilonEquals(createPoint(1.75, 2.625, 3.5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-BOTTOM-FRONT")
		public final void lbf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-BOTTOM-FRONT")
		public final void ibf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-BOTTOM-FRONT")
		public final void rbf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-BOTTOM-FRONT")
		public final void lif_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-BOTTOM-FRONT")
		public final void iif_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-BOTTOM-FRONT")
		public final void rif_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-BOTTOM-FRONT")
		public final void ltf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4762886597938145,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.4762886598, 1.2010309278, 0.9226804124), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-BOTTOM-FRONT")
		public final void itf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.592268041237, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.592268041237, 1.201030927835, 0.92268041237), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-BOTTOM-FRONT")
		public final void rtf_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6695876288659797, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.6695876289, 1.2010309278, 0.9226804124), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-BOTTOM-FRONT")
		public final void lbi_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.782832618, 0.4892703863, 2.225751073), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-BOTTOM-FRONT")
		public final void ibi_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.54378698225,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.54378698224, 0.4378698225, 2.34911242604), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-BOTTOM-FRONT")
		public final void rbi_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.0945838838, 0.4504623514, 2.3188903567), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-BOTTOM-FRONT")
		public final void lii_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.4856031128, 1.9750972763, 2.6715953307), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-BOTTOM-FRONT")
		public final void iii_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.508333333333, 2.283333333333, 3.2), result1);
			assertEpsilonEquals(createPoint(1.508333333333, 2.283333333333, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-BOTTOM-FRONT")
		public final void rii_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.2921453693, 2.0766705744, 2.8457209848), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-BOTTOM-FRONT")
		public final void lti_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.8, 3), result1);
			assertEpsilonEquals(createPoint(0.4923076923, 2.8, 2.6615384615), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-BOTTOM-FRONT")
		public final void iti_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5201277955271566,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.5201277955271566, 3.076677316294, 2.91693290735), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-BOTTOM-FRONT")
		public final void rti_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.2994748687, 3.0207051763, 2.8652663166), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK INSIDE-BOTTOM-FRONT")
		public final void lbb_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.9488752556, 0.5930470348, 2.62781186094), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK INSIDE-BOTTOM-FRONT")
		public final void ibb_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.55882353, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.55882352941, 0.5882352941, 2.64705882), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK INSIDE-BOTTOM-FRONT")
		public final void rbb_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9705882352941178,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.9705882353, 0.5882352941, 2.6470588235), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK INSIDE-BOTTOM-FRONT")
		public final void lib_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.0884955752212386), result1);
			assertEpsilonEquals(createPoint(0.7646017699, 1.7309734513, 3.0884955752), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK INSIDE-BOTTOM-FRONT")
		public final void iib_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.52678571429,2.025,3.9285714286), result1);
			assertEpsilonEquals(createPoint(1.52678571429,2.025,3.9285714286), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK INSIDE-BOTTOM-FRONT")
		public final void rib_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.5415162454873648), result1);
			assertEpsilonEquals(createPoint(2.1718411552, 1.8895306859, 3.5415162455), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-BOTTOM-FRONT")
		public final void ltb_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.2551724137931033,3.0), result1);
			assertEpsilonEquals(createPoint(0.8275862069, 2.2551724138, 2.9310344828), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK INSIDE-BOTTOM-FRONT")
		public final void itb_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5375, 2.625, 3.5), result1);
			assertEpsilonEquals(createPoint(1.5375, 2.625, 3.5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK INSIDE-BOTTOM-FRONT")
		public final void rtb_ibf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 1.6, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.2930398572278405,3.0), result1);
			assertEpsilonEquals(createPoint(2.0475907198, 2.2930398572, 2.989292088), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-BOTTOM-FRONT")
		public final void lbf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-BOTTOM-FRONT")
		public final void ibf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-BOTTOM-FRONT")
		public final void rbf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-BOTTOM-FRONT")
		public final void lif_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.3748148148, 1.2125925926, 0.8481481481), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-BOTTOM-FRONT")
		public final void iif_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.6769892473, 1.1324731183, 0.9053763441), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-BOTTOM-FRONT")
		public final void rif_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-BOTTOM-FRONT")
		public final void ltf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.2902564103, 1.4733333333, 0.8179487179), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-BOTTOM-FRONT")
		public final void itf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.5743915344, 1.4512169312, 0.8264550265), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-BOTTOM-FRONT")
		public final void rtf_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.7678980892, 1.2782165605, 0.8929936306), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-BOTTOM-FRONT")
		public final void lbi_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2260355029585799, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.226035503, 0.4378698225, 2.349112426), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-BOTTOM-FRONT")
		public final void ibi_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.0553846154, 0.4272189349, 2.3746745562), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-BOTTOM-FRONT")
		public final void rbi_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.6230656934, 0.4102189781, 2.4154744526), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-BOTTOM-FRONT")
		public final void lii_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.777377892, 2.011311054, 2.7336760925), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-BOTTOM-FRONT")
		public final void iii_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6083333333333, 2.283333333333, 3.2), result1);
			assertEpsilonEquals(createPoint(1.6083333333333, 2.283333333333, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-BOTTOM-FRONT")
		public final void rii_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2.4, 3.4), result1);
			assertEpsilonEquals(createPoint(2.5, 2.4, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-BOTTOM-FRONT")
		public final void lti_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.8811764705882354,3.0), result1);
			assertEpsilonEquals(createPoint(0.7741176471, 2.8811764706, 2.7364705882), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-BOTTOM-FRONT")
		public final void iti_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.7616613418530351,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.7616613419, 3.0766773163, 2.9169329073), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-BOTTOM-FRONT")
		public final void rti_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.5563838224, 3.1113402062, 2.9489294211), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK RIGHT-BOTTOM-FRONT")
		public final void lbb_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6470588235294117, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.6470588235, 0.5882352941, 2.6470588235), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK RIGHT-BOTTOM-FRONT")
		public final void ibb_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.2407704655, 0.569823435, 2.72070626), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK RIGHT-BOTTOM-FRONT")
		public final void rbb_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.6729081334, 0.5763604447, 2.6945582212), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK RIGHT-BOTTOM-FRONT")
		public final void lib_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.6285714285714286), result1);
			assertEpsilonEquals(createPoint(0.96, 1.92, 3.6285714286), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK RIGHT-BOTTOM-FRONT")
		public final void iib_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8482142857142856,2.025,3.928571428571429), result1);
			assertEpsilonEquals(createPoint(1.8482142857142856,2.025,3.928571428571429), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK RIGHT-BOTTOM-FRONT")
		public final void rib_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0650093225605968,4.0), result1);
			assertEpsilonEquals(createPoint(2.5717837166, 2.0650093226, 4.0428837787), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK RIGHT-BOTTOM-FRONT")
		public final void ltb_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1999999999999997,2.4857142857142858,3.2857142857142856), result1);
			assertEpsilonEquals(createPoint(1.1999999999999997,2.4857142857142858,3.2857142857142856), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK RIGHT-BOTTOM-FRONT")
		public final void itb_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9125,2.7750000000000004,3.730769230769231), result1);
			assertEpsilonEquals(createPoint(1.9125,2.7750000000000004,3.730769230769231), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK RIGHT-BOTTOM-FRONT")
		public final void rtb_rbf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 2.8, 1, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.977874456183965,4.0), result1);
			assertEpsilonEquals(createPoint(2.5717837166, 2.9778744562, 4.0428837787), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-INSIDE-FRONT")
		public final void lbf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-INSIDE-FRONT")
		public final void ibf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-INSIDE-FRONT")
		public final void rbf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-INSIDE-FRONT")
		public final void lif_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-INSIDE-FRONT")
		public final void iif_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(0.5, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-INSIDE-FRONT")
		public final void rif_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-INSIDE-FRONT")
		public final void ltf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-INSIDE-FRONT")
		public final void itf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-INSIDE-FRONT")
		public final void rtf_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-INSIDE-FRONT")
		public final void lbi_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.2578947368, 1.3926315789, 2.1621052632), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-INSIDE-FRONT")
		public final void ibi_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0126436781609196,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.0126436782, 1.315862069, 2.2303448276), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-INSIDE-FRONT")
		public final void rbi_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5252873563218392,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.5252873563, 1.315862069, 2.2303448276), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-INSIDE-FRONT")
		public final void lii_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.4728785357737104,3.0), result1);
			assertEpsilonEquals(createPoint(0.1214642263, 2.4728785358, 2.8169717138), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-INSIDE-FRONT")
		public final void iii_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4166666667,2.425,3.2), result1);
			assertEpsilonEquals(createPoint(1.4166666667, 2.425, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-INSIDE-FRONT")
		public final void rii_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.460245901639344,3.0), result1);
			assertEpsilonEquals(createPoint(2.0983606557, 2.4602459016, 2.9180327869), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-INSIDE-FRONT")
		public final void lti_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.146627566, 3.3360703812, 2.6961876833), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-INSIDE-FRONT")
		public final void iti_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.271689497716895,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.2716894977, 3.3945205479, 2.8520547945), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-INSIDE-FRONT")
		public final void rti_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.0269631031, 3.3871333964, 2.8323557237), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK LEFT-INSIDE-FRONT")
		public final void lbb_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.295242141, 1.5943075616, 2.6380628717), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK LEFT-INSIDE-FRONT")
		public final void ibb_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.9277480445, 1.54508028, 2.7109921779), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK LEFT-INSIDE-FRONT")
		public final void rbb_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3492915414340922,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.3492915414, 1.5534564191, 2.6985830829), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK LEFT-INSIDE-FRONT")
		public final void lib_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.556923076923077,3.0), result1);
			assertEpsilonEquals(createPoint(0.2615384615, 2.5569230769, 2.9076923077), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK LEFT-INSIDE-FRONT")
		public final void iib_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.125, 2.5125, 3.5), result1);
			assertEpsilonEquals(createPoint(1.125, 2.5125, 3.5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK LEFT-INSIDE-FRONT")
		public final void rib_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.75, 2.5125, 3.5), result1);
			assertEpsilonEquals(createPoint(1.75, 2.5125, 3.5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK LEFT-INSIDE-FRONT")
		public final void ltb_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.264947245, 3.123094959, 2.8804220399), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK LEFT-INSIDE-FRONT")
		public final void itb_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.9924199888, 3.1431779899, 2.9696799551), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK LEFT-INSIDE-FRONT")
		public final void rtb_lif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, .5, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4839381320642475,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.4839381321, 3.1427721594, 2.9678762641), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-INSIDE-FRONT")
		public final void lbf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-INSIDE-FRONT")
		public final void ibf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-INSIDE-FRONT")
		public final void rbf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-INSIDE-FRONT")
		public final void lif_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-INSIDE-FRONT")
		public final void iif_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-INSIDE-FRONT")
		public final void rif_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-INSIDE-FRONT")
		public final void ltf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-INSIDE-FRONT")
		public final void itf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-INSIDE-FRONT")
		public final void rtf_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(1.6, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-INSIDE-FRONT")
		public final void lbi_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.8158872518, 1.3768097373, 2.1761691224), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-INSIDE-FRONT")
		public final void ibi_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.548735632183908,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.5487356322, 1.315862069, 2.2303448276), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-INSIDE-FRONT")
		public final void rbi_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.0577922078, 1.3266233766, 2.2207792208), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-INSIDE-FRONT")
		public final void lii_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.4923076923076923,3.0), result1);
			assertEpsilonEquals(createPoint(0.4923076923, 2.4923076923, 2.6615384615), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-INSIDE-FRONT")
		public final void iii_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5083333333333333,2.425,3.2), result1);
			assertEpsilonEquals(createPoint(1.5083333333333333,2.425,3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-INSIDE-FRONT")
		public final void rii_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.4643835616438357,3.0), result1);
			assertEpsilonEquals(createPoint(2.3068493151, 2.4643835616, 2.8849315068), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-INSIDE-FRONT")
		public final void lti_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.543263965, 3.2944140197, 2.5851040526), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-INSIDE-FRONT")
		public final void iti_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5228310502283104,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.5228310502, 3.3945205479, 2.8520547945), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-INSIDE-FRONT")
		public final void rti_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.262195122, 3.362195122, 2.7658536585), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK INSIDE-INSIDE-FRONT")
		public final void lbb_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.9284332689, 1.5667311412, 2.6789168279), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK INSIDE-INSIDE-FRONT")
		public final void ibb_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.55753542293, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.5575354229, 1.5534564191, 2.6985830829), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK INSIDE-INSIDE-FRONT")
		public final void rbb_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.982780083, 1.551659751, 2.7012448133), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK INSIDE-INSIDE-FRONT")
		public final void lib_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.5551724137931036,3.0), result1);
			assertEpsilonEquals(createPoint(0.8275862069, 2.5551724138, 2.9310344828), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK INSIDE-INSIDE-FRONT")
		public final void iib_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5375,2.5124999999999997,3.5), result1);
			assertEpsilonEquals(createPoint(1.5375,2.5124999999999997,3.5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK INSIDE-INSIDE-FRONT")
		public final void rib_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.550803093396788,3.0), result1);
			assertEpsilonEquals(createPoint(2.0475907198, 2.5508030934, 2.989292088), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-INSIDE-FRONT")
		public final void ltb_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.8375838926, 3.1288590604, 2.9060402685), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK INSIDE-INSIDE-FRONT")
		public final void itb_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5508030933967878,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.5508030934, 3.1427721594, 2.9678762641), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK INSIDE-INSIDE-FRONT")
		public final void rtb_iif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 1.6, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.0408059024, 3.1408059024, 2.9591373439), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-INSIDE-FRONT")
		public final void lbf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.6266968325791864,3.0), result1);
			assertEpsilonEquals(createPoint(2.7239819005, 2.6266968326, 0.9728506787), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-INSIDE-FRONT")
		public final void ibf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-INSIDE-FRONT")
		public final void rbf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-INSIDE-FRONT")
		public final void lif_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.6918552036199097,3.0), result1);
			assertEpsilonEquals(createPoint(2.7239819005, 2.6918552036, 0.9728506787), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-INSIDE-FRONT")
		public final void iif_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-INSIDE-FRONT")
		public final void rif_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-INSIDE-FRONT")
		public final void ltf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.7244343891402716,3.0), result1);
			assertEpsilonEquals(createPoint(2.7239819005, 2.7244343891, 0.9728506787), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-INSIDE-FRONT")
		public final void itf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-INSIDE-FRONT")
		public final void rtf_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2.7, 3), result1);
			assertEpsilonEquals(createPoint(2.8, 2.7, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-INSIDE-FRONT")
		public final void lbi_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3645977011494246,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.3645977011, 1.315862069, 2.2303448276), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-INSIDE-FRONT")
		public final void ibi_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.1182496608, 1.2840569878, 2.2586160109), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-INSIDE-FRONT")
		public final void rbi_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.6417808219, 1.2760273973, 2.2657534247), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-INSIDE-FRONT")
		public final void lii_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.482941176470588,3.0), result1);
			assertEpsilonEquals(createPoint(0.7741176471, 2.4829411765, 2.7364705882), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-INSIDE-FRONT")
		public final void iii_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6083333333333334,2.425,3.2), result1);
			assertEpsilonEquals(createPoint(1.6083333333, 2.425, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-INSIDE-FRONT")
		public final void rii_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2.4, 3.4), result1);
			assertEpsilonEquals(createPoint(2.5, 2.4, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-INSIDE-FRONT")
		public final void lti_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.8355308813, 3.3314365024, 2.6838306731), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-INSIDE-FRONT")
		public final void iti_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.7968036529680365,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.796803653, 3.3945205479, 2.8520547945), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-INSIDE-FRONT")
		public final void rti_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.5608108108, 3.4175675676, 2.9135135135), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK RIGHT-INSIDE-FRONT")
		public final void lbb_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.610991841992271,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.610991842, 1.5534564191, 2.6985830829), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK RIGHT-INSIDE-FRONT")
		public final void ibb_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.231184948, 1.5186148919, 2.7502001601), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK RIGHT-INSIDE-FRONT")
		public final void rbb_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.6700171086, 1.5301539778, 2.7331052181), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK RIGHT-INSIDE-FRONT")
		public final void lib_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1999999999999997,2.5285714285714285,3.2857142857142856), result1);
			assertEpsilonEquals(createPoint(1.2, 2.5285714286, 3.2857142857), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK RIGHT-INSIDE-FRONT")
		public final void iib_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9125,2.4951923076923075,3.730769230769231), result1);
			assertEpsilonEquals(createPoint(1.9125,2.4951923076923075,3.730769230769231), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK RIGHT-INSIDE-FRONT")
		public final void rib_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.4717837165941576,4.0), result1);
			assertEpsilonEquals(createPoint(2.5717837166, 2.4717837166, 4.0428837787), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK RIGHT-INSIDE-FRONT")
		public final void ltb_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4224866151100537,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.4224866151, 3.1427721594, 2.9678762641), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK RIGHT-INSIDE-FRONT")
		public final void itb_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3.096), result1);
			assertEpsilonEquals(createPoint(2.1188, 3.1716, 3.096), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK RIGHT-INSIDE-FRONT")
		public final void rtb_rif(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 2.8, 2.7, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,3.0,3.2666666666666613), result1);
			assertEpsilonEquals(createPoint(2.63, 3.21, 3.2666666667), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-TOP-FRONT")
		public final void lbf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.3523809524, 3.5238095238, 0.7047619048), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-TOP-FRONT")
		public final void ibf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.8148148148, 3.4259259259, 0.6851851852), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-TOP-FRONT")
		public final void rbf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1153846153846154,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.1153846154, 3.4615384615, 0.6923076923), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-TOP-FRONT")
		public final void lif_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.315855181, 4.0424469413, 0.631710362), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-TOP-FRONT")
		public final void iif_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.9223744292, 3.901826484, 0.5776255708), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-TOP-FRONT")
		public final void rif_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3247422680412373,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.324742268, 3.9278350515, 0.587628866), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-TOP-FRONT")
		public final void ltf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.4143302181, 4.7601246106, 0.8286604361), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-TOP-FRONT")
		public final void itf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.8282828283, 4.5404040404, 0.6717171717), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-TOP-FRONT")
		public final void rtf_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0405405405405403,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.0405405405, 4.6216216216, 0.7297297297), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-TOP-FRONT")
		public final void lbi_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.1847791035, 1.8477910351, 2.5130603031), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-TOP-FRONT")
		public final void ibi_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1436931079323798,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.1436931079, 1.7815344603, 2.544863459), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-TOP-FRONT")
		public final void rbi_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.7873862158647595,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.7873862159, 1.7815344603, 2.544863459), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-TOP-FRONT")
		public final void lii_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.1182458888, 3.0148786218, 2.8324197338), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-TOP-FRONT")
		public final void iii_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4166666666666667,2.6166666666666663,3.2), result1);
			assertEpsilonEquals(createPoint(1.4166666666666667,2.6166666666666663,3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-TOP-FRONT")
		public final void rii_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.0738498789, 2.9539951574, 2.8886198547), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-TOP-FRONT")
		public final void lti_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(0, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-TOP-FRONT")
		public final void iti_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(1.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-TOP-FRONT")
		public final void rti_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,3.0,3.3355704697986575), result1);
			assertEpsilonEquals(createPoint(2.4463087248, 3.6375838926, 3.3355704698), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK LEFT-TOP-FRONT")
		public final void lbb_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.6153846153846154,3.0), result1);
			assertEpsilonEquals(createPoint(0.2615384615, 2.6153846154, 2.9076923077), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK LEFT-TOP-FRONT")
		public final void ibb_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.05, 2.25, 3.2), result1);
			assertEpsilonEquals(createPoint(1.05, 2.25, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK LEFT-TOP-FRONT")
		public final void rbb_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2.25, 3.2), result1);
			assertEpsilonEquals(createPoint(1.6, 2.25, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK LEFT-TOP-FRONT")
		public final void lib_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.82453637660485), result1);
			assertEpsilonEquals(createPoint(0.1469329529, 3.1640513552, 3.8245363766), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK LEFT-TOP-FRONT")
		public final void iib_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2557117750439368,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(1.255711775, 3.0351493849, 4.0228471002), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK LEFT-TOP-FRONT")
		public final void rib_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 4), result1);
			assertEpsilonEquals(createPoint(2.009715994, 3.0373692078, 4.019431988), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK LEFT-TOP-FRONT")
		public final void ltb_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(0.1004942339, 3.881383855, 4.1960461285), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK LEFT-TOP-FRONT")
		public final void itb_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.324053452115813,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(1.3240534521, 3.846325167, 4.2962138085), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK LEFT-TOP-FRONT")
		public final void rtb_ltf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, .5, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 4), result1);
			assertEpsilonEquals(createPoint(2.121129326, 3.8652094718, 4.2422586521), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-TOP-FRONT")
		public final void lbf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1076923076923075,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.1076923077, 3.4615384615, 0.6923076923), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-TOP-FRONT")
		public final void ibf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5692307692307692,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.5692307692, 3.4615384615, 0.6923076923), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-TOP-FRONT")
		public final void rbf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.876923076923077,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.8769230769, 3.4615384615, 0.6923076923), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-TOP-FRONT")
		public final void lif_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.9550387597, 3.9519379845, 0.5968992248), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-TOP-FRONT")
		public final void iif_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5587628865979382,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.5587628866, 3.9278350515, 0.587628866), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-TOP-FRONT")
		public final void rif_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9711340206185568,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.9711340206, 3.9278350515, 0.587628866), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-TOP-FRONT")
		public final void ltf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1675675675675674,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.1675675676, 4.6216216216, 0.7297297297), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-TOP-FRONT")
		public final void itf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5729729729729731,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.572972973, 4.6216216216, 0.7297297297), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-TOP-FRONT")
		public final void rtf_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8432432432432433,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.8432432432, 4.6216216216, 0.7297297297), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-TOP-FRONT")
		public final void lbi_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.6031212485, 1.8847539016, 2.4953181273), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-TOP-FRONT")
		public final void ibi_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5356306892067622,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.5356306892, 1.7815344603, 2.544863459), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-TOP-FRONT")
		public final void rbi_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.1747228381, 1.8070953437, 2.532594235), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-TOP-FRONT")
		public final void lii_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.4371352785, 3.1103448276, 2.7442970822), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-TOP-FRONT")
		public final void iii_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5083333333333333,2.6166666666666663,3.2), result1);
			assertEpsilonEquals(createPoint(1.5083333333333333,2.6166666666666663,3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-TOP-FRONT")
		public final void rii_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.2994748687, 2.9792948237, 2.8652663166), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-TOP-FRONT")
		public final void lti_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.2677042802, 3.8342412451, 2.9984435798), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-TOP-FRONT")
		public final void iti_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(1.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-TOP-FRONT")
		public final void rti_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(2.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK INSIDE-TOP-FRONT")
		public final void lbb_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.9035812672, 2.8236914601, 2.741046832), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK INSIDE-TOP-FRONT")
		public final void ibb_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.545, 2.25, 3.2), result1);
			assertEpsilonEquals(createPoint(1.545, 2.25, 3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK INSIDE-TOP-FRONT")
		public final void rbb_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.51338488994646,3.0), result1);
			assertEpsilonEquals(createPoint(2.0475907198, 2.5133848899, 2.989292088), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK INSIDE-TOP-FRONT")
		public final void lib_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.643776824034335), result1);
			assertEpsilonEquals(createPoint(0.5424892704, 3.2815450644, 3.643776824), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK INSIDE-TOP-FRONT")
		public final void iib_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5244288224956062,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(1.5244288225, 3.0351493849, 4.0228471002), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK INSIDE-TOP-FRONT")
		public final void rib_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,3.0,3.9379128137384414), result1);
			assertEpsilonEquals(createPoint(2.2610303831, 3.0903566711, 3.9379128137), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-TOP-FRONT")
		public final void ltb_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(0.3711500975, 3.9247563353, 4.0721247563), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK INSIDE-TOP-FRONT")
		public final void itb_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5175946547884187,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(1.5175946548, 3.846325167, 4.2962138085), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK INSIDE-TOP-FRONT")
		public final void rtb_itf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 1.6, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 4), result1);
			assertEpsilonEquals(createPoint(2.3269046351, 3.8692594566, 4.2306872669), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-TOP-FRONT")
		public final void lbf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9384615384615387,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.9384615384615387,3.461538461538462,0.6923076923076924), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-TOP-FRONT")
		public final void ibf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.3755868545, 3.3676417479, 0.6735283496), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-TOP-FRONT")
		public final void rbf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.705251054043695,3.420850900728249,0.6841701801456498), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-TOP-FRONT")
		public final void lif_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6453608247422677,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.645360824742268,3.927835051546392,0.5876288659793815), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-TOP-FRONT")
		public final void iif_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.2167195767, 3.8334391534, 0.5513227513), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-TOP-FRONT")
		public final void rif_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.6685350318471337,3.8606369426751592,0.5617834394904458), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-TOP-FRONT")
		public final void ltf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.011851851851852,4.605925925925926,0.7185185185185186), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-TOP-FRONT")
		public final void itf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.285591397849462,4.446021505376344,0.6043010752688172), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-TOP-FRONT")
		public final void rtf_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(2.6977049180327866,4.522622950819672,0.659016393442623), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-TOP-FRONT")
		public final void lbi_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.998134715025907,1.7823834196891197,2.5444559585492224), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-TOP-FRONT")
		public final void ibi_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(1.9651155624, 1.7889060092, 2.5413251156), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-TOP-FRONT")
		public final void rbi_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(2.605121555915721,1.7520259319286868,2.5590275526742303), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-TOP-FRONT")
		public final void lii_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.731630648330059,3.0793713163064833,2.772888015717092), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-TOP-FRONT")
		public final void iii_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6083333333333332,2.6166666666666663,3.2), result1);
			assertEpsilonEquals(createPoint(1.6083333333333332,2.6166666666666663,3.2), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-TOP-FRONT")
		public final void rii_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2.4, 3.4), result1);
			assertEpsilonEquals(createPoint(2.5, 2.4, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-TOP-FRONT")
		public final void lti_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(0.5254498714652955,3.862724935732648,2.9496143958868895), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-TOP-FRONT")
		public final void iti_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(1.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-TOP-FRONT")
		public final void rti_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(2.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK RIGHT-TOP-FRONT")
		public final void lbb_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4, 2.5, 3), result1);
			assertEpsilonEquals(createPoint(1.4, 2.5, 3), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK RIGHT-TOP-FRONT")
		public final void ibb_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.403896590483327), result1);
			assertEpsilonEquals(createPoint(2.0187336081, 1.9951292619, 3.4038965905), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK RIGHT-TOP-FRONT")
		public final void rbb_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.429653248306098), result1);
			assertEpsilonEquals(createPoint(2.6177760063770426,1.9629334396173777,3.429653248306098), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK RIGHT-TOP-FRONT")
		public final void lib_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.805479452054795), result1);
			assertEpsilonEquals(createPoint(0.8361643835616436,3.1764383561643834,3.8054794520547945), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK RIGHT-TOP-FRONT")
		public final void iib_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8175746924428822,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(1.8175746924, 3.0351493849, 4.0228471002), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK RIGHT-TOP-FRONT")
		public final void rib_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 4), result1);
			assertEpsilonEquals(createPoint(2.5710284463894966,3.015579868708971,4.052954048140044), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK RIGHT-TOP-FRONT")
		public final void ltb_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(0.6468217054263565,3.9234108527131784,4.075968992248062), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK RIGHT-TOP-FRONT")
		public final void itb_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.7287305122494432,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(1.7287305122494432,3.846325167037862,4.296213808463252), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK RIGHT-TOP-FRONT")
		public final void rtb_rtf(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 2.8, 5, 1,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 4), result1);
			assertEpsilonEquals(createPoint(2.550027700831025,3.8334626038781163,4.332963988919667), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-BOTTOM-INSIDE")
		public final void lbf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-BOTTOM-INSIDE")
		public final void ibf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-BOTTOM-INSIDE")
		public final void rbf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-BOTTOM-INSIDE")
		public final void lif_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.3741741741741742,1.3523123123123122,2.843723723723724), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-BOTTOM-INSIDE")
		public final void iif_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.783908046, 1.3974712644, 2.7211494253), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-BOTTOM-INSIDE")
		public final void rif_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0414634146341466,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.0414634146, 1.3790243902, 2.7712195122), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-BOTTOM-INSIDE")
		public final void ltf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.37435897435897436,1.6533333333333333,2.845128205128205), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-BOTTOM-INSIDE")
		public final void itf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(0.7765765765765766,1.7190990990990993,2.7490090090090087), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-BOTTOM-INSIDE")
		public final void rtf_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0320754716981135,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(1.0320754716981135,1.6916981132075477,2.789056603773584), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-BOTTOM-INSIDE")
		public final void lbi_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-BOTTOM-INSIDE")
		public final void ibi_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-BOTTOM-INSIDE")
		public final void rbi_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-BOTTOM-INSIDE")
		public final void lii_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.591855203619909), result1);
			assertEpsilonEquals(createPoint(0.2398190045, 1.7285067873, 3.5918552036), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-BOTTOM-INSIDE")
		public final void iii_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3571428571428572,2.2,3.4571428571428573), result1);
			assertEpsilonEquals(createPoint(1.3571428571428572,2.2,3.4571428571428573), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-BOTTOM-INSIDE")
		public final void rii_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9642857142857144,2.025,3.507142857142857), result1);
			assertEpsilonEquals(createPoint(1.9642857142857144,2.025,3.507142857142857), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-BOTTOM-INSIDE")
		public final void lti_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.665905848787446), result1);
			assertEpsilonEquals(createPoint(0.332382311, 1.8716119829, 3.6659058488), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-BOTTOM-INSIDE")
		public final void iti_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1346153846153846,2.6500000000000004,3.546153846153846), result1);
			assertEpsilonEquals(createPoint(1.1346153846153846,2.6500000000000004,3.546153846153846), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-BOTTOM-INSIDE")
		public final void rti_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6346153846153846,2.475,3.573076923076923), result1);
			assertEpsilonEquals(createPoint(1.6346153846153846,2.475,3.573076923076923), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK LEFT-BOTTOM-INSIDE")
		public final void lbb_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK LEFT-BOTTOM-INSIDE")
		public final void ibb_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK LEFT-BOTTOM-INSIDE")
		public final void rbb_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK LEFT-BOTTOM-INSIDE")
		public final void lib_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(0.30958904109589036,1.533150684931507,4.256986301369863), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK LEFT-BOTTOM-INSIDE")
		public final void iib_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(0.9863636363636364,1.6809090909090911,4.383636363636364), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK LEFT-BOTTOM-INSIDE")
		public final void rib_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4647058823529417,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(1.4647058824, 1.6752941176, 4.3788235294), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK LEFT-BOTTOM-INSIDE")
		public final void ltb_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(0.3467455621301775,1.796923076923077,4.167810650887574), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK LEFT-BOTTOM-INSIDE")
		public final void itb_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(0.8630434782608696,1.943913043478261,4.235652173913043), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK LEFT-BOTTOM-INSIDE")
		public final void rtb_lbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, .5, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.192682926829269,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(1.192682926829269,1.9004878048780496,4.215609756097561), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-BOTTOM-INSIDE")
		public final void lbf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-BOTTOM-INSIDE")
		public final void ibf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-BOTTOM-INSIDE")
		public final void rbf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-BOTTOM-INSIDE")
		public final void lif_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1668292682926829,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-BOTTOM-INSIDE")
		public final void iif_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5729268292682927,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-BOTTOM-INSIDE")
		public final void rif_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-BOTTOM-INSIDE")
		public final void ltf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1555555555555554,1.7222222222222225,2.744444444444444), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-BOTTOM-INSIDE")
		public final void itf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5733962264150945,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-BOTTOM-INSIDE")
		public final void rtf_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void lbi_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void ibi_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void rbi_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void lii_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.591150442477876), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void iii_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5142857142857142,2.2,3.4571428571428573), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void rii_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void lti_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.6472103004291845), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void iti_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5423076923076922,2.5,3.569230769230769), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-BOTTOM-INSIDE")
		public final void rti_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK INSIDE-BOTTOM-INSIDE")
		public final void lbb_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK INSIDE-BOTTOM-INSIDE")
		public final void ibb_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK INSIDE-BOTTOM-INSIDE")
		public final void rbb_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 1.6, 1, 3.81,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK INSIDE-BOTTOM-INSIDE")
		public final void lib_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK INSIDE-BOTTOM-INSIDE")
		public final void iib_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.551764705882353,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK INSIDE-BOTTOM-INSIDE")
		public final void rib_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-BOTTOM-INSIDE")
		public final void ltb_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK INSIDE-BOTTOM-INSIDE")
		public final void itb_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5653658536585366,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK INSIDE-BOTTOM-INSIDE")
		public final void rtb_ibi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 1.6, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-BOTTOM-INSIDE")
		public final void lbf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.266968325791855), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-BOTTOM-INSIDE")
		public final void ibf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.743494423791819), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-BOTTOM-INSIDE")
		public final void rbf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-BOTTOM-INSIDE")
		public final void lif_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-BOTTOM-INSIDE")
		public final void iif_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-BOTTOM-INSIDE")
		public final void rif_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-BOTTOM-INSIDE")
		public final void ltf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-BOTTOM-INSIDE")
		public final void itf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-BOTTOM-INSIDE")
		public final void rtf_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void lbi_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.743891402714932), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void ibi_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.7940520446096655), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void rbi_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void lii_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.537142857142857), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void iii_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6857142857142857,2.2,3.4571428571428573), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void rii_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void lti_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3615384615384616,2.335714285714286,3.594505494505494), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void iti_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9,2.8,3.523076923076923), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-BOTTOM-INSIDE")
		public final void rti_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK RIGHT-BOTTOM-INSIDE")
		public final void lbb_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.9683257918552037), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK RIGHT-BOTTOM-INSIDE")
		public final void ibb_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.8178438661710032), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK RIGHT-BOTTOM-INSIDE")
		public final void rbb_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK RIGHT-BOTTOM-INSIDE")
		public final void lib_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK RIGHT-BOTTOM-INSIDE")
		public final void iib_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK RIGHT-BOTTOM-INSIDE")
		public final void rib_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK RIGHT-BOTTOM-INSIDE")
		public final void ltb_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8302439024390245,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK RIGHT-BOTTOM-INSIDE")
		public final void itb_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.063258785942492,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK RIGHT-BOTTOM-INSIDE")
		public final void rtb_rbi(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 2.8, 1, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-INSIDE-INSIDE")
		public final void lbf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-INSIDE-INSIDE")
		public final void ibf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-INSIDE-INSIDE")
		public final void rbf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.1084598698481565,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-INSIDE-INSIDE")
		public final void lif_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-INSIDE-INSIDE")
		public final void iif_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.6312176165803107,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-INSIDE-INSIDE")
		public final void rif_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-INSIDE-INSIDE")
		public final void ltf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-INSIDE-INSIDE")
		public final void itf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-INSIDE-INSIDE")
		public final void rtf_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-INSIDE-INSIDE")
		public final void lbi_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-INSIDE-INSIDE")
		public final void ibi_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.684680337756333), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-INSIDE-INSIDE")
		public final void rbi_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0092592592592595,2.0125,3.6981481481481477), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-INSIDE-INSIDE")
		public final void lii_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-INSIDE-INSIDE")
		public final void iii_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.25,2.4749999999999996,3.5), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-INSIDE-INSIDE")
		public final void rii_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-INSIDE-INSIDE")
		public final void lti_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-INSIDE-INSIDE")
		public final void iti_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.6298342541436464), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-INSIDE-INSIDE")
		public final void rti_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK LEFT-INSIDE-INSIDE")
		public final void lbb_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK LEFT-INSIDE-INSIDE")
		public final void ibb_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK LEFT-INSIDE-INSIDE")
		public final void rbb_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.084558823529412,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK LEFT-INSIDE-INSIDE")
		public final void lib_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK LEFT-INSIDE-INSIDE")
		public final void iib_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.609016393442623,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK LEFT-INSIDE-INSIDE")
		public final void rib_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK LEFT-INSIDE-INSIDE")
		public final void ltb_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 3.8), result1);
			assertEpsilonEquals(createPoint(.5, 2.7, 3.8), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK LEFT-INSIDE-INSIDE")
		public final void itb_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK LEFT-INSIDE-INSIDE")
		public final void rtb_lii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, .5, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-INSIDE-INSIDE")
		public final void lbf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.431578947368421,2.4157894736842107,3.4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-INSIDE-INSIDE")
		public final void ibf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5894736842105264,2.4157894736842107,3.4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-INSIDE-INSIDE")
		public final void rbf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6947368421052633,2.4157894736842107,3.4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-INSIDE-INSIDE")
		public final void lif_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.431578947368421,2.668421052631579,3.4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-INSIDE-INSIDE")
		public final void iif_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5894736842105264,2.668421052631579,3.4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-INSIDE-INSIDE")
		public final void rif_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-INSIDE-INSIDE")
		public final void ltf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.431578947368421,2.794736842105263,3.4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-INSIDE-INSIDE")
		public final void itf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5894736842105264,2.794736842105263,3.4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-INSIDE-INSIDE")
		public final void rtf_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-INSIDE-INSIDE")
		public final void lbi_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3925925925925926,2.35,3.748148148148148), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-INSIDE-INSIDE")
		public final void ibi_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.587037037037037,2.35,3.748148148148148), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-INSIDE-INSIDE")
		public final void rbi_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.7166666666666668,2.35,3.748148148148148), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-INSIDE-INSIDE")
		public final void lii_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3,2.64375,3.7249999999999996), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-INSIDE-INSIDE")
		public final void iii_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.55,2.55,3.5999999999999996), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-INSIDE-INSIDE")
		public final void rii_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-INSIDE-INSIDE")
		public final void lti_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3333333333333335,2.85,3.7333333333333334), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-INSIDE-INSIDE")
		public final void iti_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5833333333333335,2.85,3.7333333333333334), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-INSIDE-INSIDE")
		public final void rti_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK INSIDE-INSIDE-INSIDE")
		public final void lbb_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4666666666666663,2.4749999999999996,3.9), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK INSIDE-INSIDE-INSIDE")
		public final void ibb_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5916666666666668,2.4749999999999996,3.9), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK INSIDE-INSIDE-INSIDE")
		public final void rbb_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6750000000000003,2.4749999999999996,3.9), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK INSIDE-INSIDE-INSIDE")
		public final void lib_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4666666666666668,2.675,3.9), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK INSIDE-INSIDE-INSIDE")
		public final void iib_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5916666666666668,2.675,3.9), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK INSIDE-INSIDE-INSIDE")
		public final void rib_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-INSIDE-INSIDE")
		public final void ltb_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4666666666666668,2.7750000000000004,3.9), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK INSIDE-INSIDE-INSIDE")
		public final void itb_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5916666666666668,2.7750000000000004,3.9), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK INSIDE-INSIDE-INSIDE")
		public final void rtb_iii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 1.6, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-INSIDE-INSIDE")
		public final void lbf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-INSIDE-INSIDE")
		public final void ibf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-INSIDE-INSIDE")
		public final void rbf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0905024088093596,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-INSIDE-INSIDE")
		public final void lif_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.628904847396768,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-INSIDE-INSIDE")
		public final void iif_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.62411655300682,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-INSIDE-INSIDE")
		public final void rif_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-INSIDE-INSIDE")
		public final void ltf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.9132854578096947,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-INSIDE-INSIDE")
		public final void itf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-INSIDE-INSIDE")
		public final void rtf_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-INSIDE-INSIDE")
		public final void lbi_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.6908129543952413), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-INSIDE-INSIDE")
		public final void ibi_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.669487750556793), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-INSIDE-INSIDE")
		public final void rbi_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-INSIDE-INSIDE")
		public final void lii_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5,2.560714285714286,3.614285714285714), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-INSIDE-INSIDE")
		public final void iii_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.75,2.457692307692308,3.476923076923077), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-INSIDE-INSIDE")
		public final void rii_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-INSIDE-INSIDE")
		public final void lti_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9333333333333336,2.9785714285714286,3.676190476190476), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-INSIDE-INSIDE")
		public final void iti_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,3.0,3.5904), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-INSIDE-INSIDE")
		public final void rti_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK RIGHT-INSIDE-INSIDE")
		public final void lbb_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK RIGHT-INSIDE-INSIDE")
		public final void ibb_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK RIGHT-INSIDE-INSIDE")
		public final void rbb_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK RIGHT-INSIDE-INSIDE")
		public final void lib_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.6198275862068967,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK RIGHT-INSIDE-INSIDE")
		public final void iib_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.5773162939297123,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK RIGHT-INSIDE-INSIDE")
		public final void rib_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK RIGHT-INSIDE-INSIDE")
		public final void ltb_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.940517241379311,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK RIGHT-INSIDE-INSIDE")
		public final void itb_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK RIGHT-INSIDE-INSIDE")
		public final void rtb_rii(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 2.8, 2.7, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-TOP-INSIDE")
		public final void lbf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-TOP-INSIDE")
		public final void ibf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-TOP-INSIDE")
		public final void rbf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1612576064908722,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-TOP-INSIDE")
		public final void lif_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-TOP-INSIDE")
		public final void iif_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-TOP-INSIDE")
		public final void rif_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-TOP-INSIDE")
		public final void ltf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-TOP-INSIDE")
		public final void itf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-TOP-INSIDE")
		public final void rtf_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-TOP-INSIDE")
		public final void lbi_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.6455445544554452), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-TOP-INSIDE")
		public final void ibi_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.05, 2.25, 3.58), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-TOP-INSIDE")
		public final void rbi_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5,2.5,3.5999999999999996), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-TOP-INSIDE")
		public final void lii_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.5175463623395147), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-TOP-INSIDE")
		public final void iii_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3846153846153846,2.7,3.446153846153846), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-TOP-INSIDE")
		public final void rii_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-TOP-INSIDE")
		public final void lti_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-TOP-INSIDE")
		public final void iti_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(1.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-TOP-INSIDE")
		public final void rti_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK LEFT-TOP-INSIDE")
		public final void lbb_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK LEFT-TOP-INSIDE")
		public final void ibb_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK LEFT-TOP-INSIDE")
		public final void rbb_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2745839636913767,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK LEFT-TOP-INSIDE")
		public final void lib_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK LEFT-TOP-INSIDE")
		public final void iib_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1634146341463414,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK LEFT-TOP-INSIDE")
		public final void rib_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK LEFT-TOP-INSIDE")
		public final void ltb_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK LEFT-TOP-INSIDE")
		public final void itb_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3941176470588235,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK LEFT-TOP-INSIDE")
		public final void rtb_lti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, .5, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-TOP-INSIDE")
		public final void lbf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0709939148073024,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-TOP-INSIDE")
		public final void ibf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5669371196754565,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-TOP-INSIDE")
		public final void rbf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8975659229208925,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-TOP-INSIDE")
		public final void lif_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-TOP-INSIDE")
		public final void iif_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5611320754716982,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-TOP-INSIDE")
		public final void rif_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-TOP-INSIDE")
		public final void ltf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0302439024390244,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-TOP-INSIDE")
		public final void itf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.564390243902439,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-TOP-INSIDE")
		public final void rtf_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-TOP-INSIDE")
		public final void lbi_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.6409288824383164), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-TOP-INSIDE")
		public final void ibi_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.55, 2.5, 3.6), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-TOP-INSIDE")
		public final void rbi_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.98,2.8888888888888893,3.631111111111111), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-TOP-INSIDE")
		public final void lii_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.5356223175965664), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-TOP-INSIDE")
		public final void iii_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5115384615384615,2.7,3.4461538461538463), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-TOP-INSIDE")
		public final void rii_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-TOP-INSIDE")
		public final void lti_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.4672566371681413), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-TOP-INSIDE")
		public final void iti_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(1.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-TOP-INSIDE")
		public final void rti_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK INSIDE-TOP-INSIDE")
		public final void lbb_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK INSIDE-TOP-INSIDE")
		public final void ibb_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5612708018154313,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK INSIDE-TOP-INSIDE")
		public final void rbb_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9485627836611197,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK INSIDE-TOP-INSIDE")
		public final void lib_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK INSIDE-TOP-INSIDE")
		public final void iib_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5336585365853659,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK INSIDE-TOP-INSIDE")
		public final void rib_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-TOP-INSIDE")
		public final void ltb_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK INSIDE-TOP-INSIDE")
		public final void itb_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5105882352941178,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK INSIDE-TOP-INSIDE")
		public final void rtb_iti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 1.6, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-TOP-INSIDE")
		public final void lbf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.874239350912779,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-TOP-INSIDE")
		public final void ibf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-TOP-INSIDE")
		public final void rbf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-TOP-INSIDE")
		public final void lif_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.711698113207547,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-TOP-INSIDE")
		public final void iif_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-TOP-INSIDE")
		public final void rif_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-TOP-INSIDE")
		public final void ltf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8029268292682925,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-TOP-INSIDE")
		public final void itf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-TOP-INSIDE")
		public final void rtf_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-TOP-INSIDE")
		public final void lbi_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4,2.5,3.6), result1);
			assertEpsilonEquals(createPoint(1.4, 2.5, 3.6), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-TOP-INSIDE")
		public final void ibi_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.559610340951667), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-TOP-INSIDE")
		public final void rbi_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-TOP-INSIDE")
		public final void lii_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.5194520547945203), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-TOP-INSIDE")
		public final void iii_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.65,2.7,3.4461538461538463), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-TOP-INSIDE")
		public final void rii_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-TOP-INSIDE")
		public final void lti_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3.48), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-TOP-INSIDE")
		public final void iti_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(1.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-TOP-INSIDE")
		public final void rti_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK RIGHT-TOP-INSIDE")
		public final void lbb_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.715582450832073,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK RIGHT-TOP-INSIDE")
		public final void ibb_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.955271565495211,4.0), result1);
			assertEpsilonEquals(createPoint(2.268370607, 2.9552715655, 4.2907348243), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK RIGHT-TOP-INSIDE")
		public final void rbb_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK RIGHT-TOP-INSIDE")
		public final void lib_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK RIGHT-TOP-INSIDE")
		public final void iib_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.937560975609756,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK RIGHT-TOP-INSIDE")
		public final void rib_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK RIGHT-TOP-INSIDE")
		public final void ltb_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK RIGHT-TOP-INSIDE")
		public final void itb_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6376470588235295,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK RIGHT-TOP-INSIDE")
		public final void rtb_rti(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 2.8, 5, 3.8,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-BOTTOM-BACK")
		public final void lbf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-BOTTOM-BACK")
		public final void ibf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-BOTTOM-BACK")
		public final void rbf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-BOTTOM-BACK")
		public final void lif_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(0.2951120911429621,1.573686144799706,2.951120911429621), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-BOTTOM-BACK")
		public final void iif_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(0.9256080114449213,1.59585121602289,2.8719599427753932), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-BOTTOM-BACK")
		public final void rif_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-BOTTOM-BACK")
		public final void ltf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.323823109843081), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-BOTTOM-BACK")
		public final void itf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.05, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-BOTTOM-BACK")
		public final void rtf_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-BOTTOM-BACK")
		public final void lbi_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-BOTTOM-BACK")
		public final void ibi_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-BOTTOM-BACK")
		public final void rbi_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-BOTTOM-BACK")
		public final void lii_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-BOTTOM-BACK")
		public final void iii_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3571428571428572,2.2,3.6285714285714286), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-BOTTOM-BACK")
		public final void rii_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-BOTTOM-BACK")
		public final void lti_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.249110320284698,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-BOTTOM-BACK")
		public final void iti_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1971153846153846,2.8125,3.8846153846153846), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-BOTTOM-BACK")
		public final void rti_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK LEFT-BOTTOM-BACK")
		public final void lbb_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK LEFT-BOTTOM-BACK")
		public final void ibb_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK LEFT-BOTTOM-BACK")
		public final void rbb_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK LEFT-BOTTOM-BACK")
		public final void lib_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK LEFT-BOTTOM-BACK")
		public final void iib_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2142857142857144,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK LEFT-BOTTOM-BACK")
		public final void rib_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK LEFT-BOTTOM-BACK")
		public final void ltb_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK LEFT-BOTTOM-BACK")
		public final void itb_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2692307692307692,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK LEFT-BOTTOM-BACK")
		public final void rtb_lbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, .5, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-BOTTOM-BACK")
		public final void lbf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3538461538461537,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-BOTTOM-BACK")
		public final void ibf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5846153846153848,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-BOTTOM-BACK")
		public final void rbf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.7384615384615385,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-BOTTOM-BACK")
		public final void lif_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(0.9300813008130082,1.5861788617886179,2.90650406504065), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-BOTTOM-BACK")
		public final void iif_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5577151335311574,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-BOTTOM-BACK")
		public final void rif_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-BOTTOM-BACK")
		public final void ltf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.090128755364804), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-BOTTOM-BACK")
		public final void itf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.560769230769231,2.02,3.0384615384615383), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-BOTTOM-BACK")
		public final void rtf_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-BOTTOM-BACK")
		public final void lbi_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3303370786516853,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-BOTTOM-BACK")
		public final void ibi_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5831460674157303,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-BOTTOM-BACK")
		public final void rbi_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.751685393258427,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-BOTTOM-BACK")
		public final void lii_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-BOTTOM-BACK")
		public final void iii_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5142857142857142,2.2,3.6285714285714286), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-BOTTOM-BACK")
		public final void rii_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-BOTTOM-BACK")
		public final void lti_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.3000000000000003,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-BOTTOM-BACK")
		public final void iti_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5302884615384613,2.8125,3.8846153846153846), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-BOTTOM-BACK")
		public final void rti_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK INSIDE-BOTTOM-BACK")
		public final void lbb_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK INSIDE-BOTTOM-BACK")
		public final void ibb_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(1.6, 1, 5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK INSIDE-BOTTOM-BACK")
		public final void rbb_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 1.6, 1, 3.81,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK INSIDE-BOTTOM-BACK")
		public final void lib_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK INSIDE-BOTTOM-BACK")
		public final void iib_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5285714285714285,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK INSIDE-BOTTOM-BACK")
		public final void rib_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-BOTTOM-BACK")
		public final void ltb_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK INSIDE-BOTTOM-BACK")
		public final void itb_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5615384615384615,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK INSIDE-BOTTOM-BACK")
		public final void rtb_ibb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 1.6, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-BOTTOM-BACK")
		public final void lbf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-BOTTOM-BACK")
		public final void ibf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-BOTTOM-BACK")
		public final void rbf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-BOTTOM-BACK")
		public final void lif_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6160237388724035,2.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-BOTTOM-BACK")
		public final void iif_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-BOTTOM-BACK")
		public final void rif_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-BOTTOM-BACK")
		public final void ltf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.7015384615384617,2.02,3.0384615384615383), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-BOTTOM-BACK")
		public final void itf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0754589733982765,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-BOTTOM-BACK")
		public final void rtf_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-BOTTOM-BACK")
		public final void lbi_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-BOTTOM-BACK")
		public final void ibi_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-BOTTOM-BACK")
		public final void rbi_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-BOTTOM-BACK")
		public final void lii_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.9485714285714284), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-BOTTOM-BACK")
		public final void iii_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6857142857142855,2.2,3.628571428571428), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-BOTTOM-BACK")
		public final void rii_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-BOTTOM-BACK")
		public final void lti_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.025,2.6482142857142854,3.9857142857142858), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-BOTTOM-BACK")
		public final void iti_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8937499999999998,2.8125,3.8846153846153846), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-BOTTOM-BACK")
		public final void rti_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK RIGHT-BOTTOM-BACK")
		public final void lbb_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK RIGHT-BOTTOM-BACK")
		public final void ibb_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK RIGHT-BOTTOM-BACK")
		public final void rbb_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK RIGHT-BOTTOM-BACK")
		public final void lib_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4494117647058822,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK RIGHT-BOTTOM-BACK")
		public final void iib_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8714285714285714,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK RIGHT-BOTTOM-BACK")
		public final void rib_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK RIGHT-BOTTOM-BACK")
		public final void ltb_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.723076923076923,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK RIGHT-BOTTOM-BACK")
		public final void itb_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.6,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK RIGHT-BOTTOM-BACK")
		public final void rtb_rbb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 2.8, 1, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-INSIDE-BACK")
		public final void lbf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.1920792079207922,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-INSIDE-BACK")
		public final void ibf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.0,3.558504221954161), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-INSIDE-BACK")
		public final void rbf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0092592592592595,2.0125,3.7268518518518516), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-INSIDE-BACK")
		public final void lif_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.6435643564356437,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-INSIDE-BACK")
		public final void iif_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.578846153846154,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-INSIDE-BACK")
		public final void rif_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-INSIDE-BACK")
		public final void ltf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.869306930693069,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-INSIDE-BACK")
		public final void itf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-INSIDE-BACK")
		public final void rtf_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-INSIDE-BACK")
		public final void lbi_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-INSIDE-BACK")
		public final void ibi_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-INSIDE-BACK")
		public final void rbi_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2086294416243655,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-INSIDE-BACK")
		public final void lii_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.5558718861209964,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-INSIDE-BACK")
		public final void iii_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3125,2.45625,3.7), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-INSIDE-BACK")
		public final void rii_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-INSIDE-BACK")
		public final void lti_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-INSIDE-BACK")
		public final void iti_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0548961424332344,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-INSIDE-BACK")
		public final void rti_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK LEFT-INSIDE-BACK")
		public final void lbb_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK LEFT-INSIDE-BACK")
		public final void ibb_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK LEFT-INSIDE-BACK")
		public final void rbb_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0185185185185188,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK LEFT-INSIDE-BACK")
		public final void lib_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK LEFT-INSIDE-BACK")
		public final void iib_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.55, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK LEFT-INSIDE-BACK")
		public final void rib_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK LEFT-INSIDE-BACK")
		public final void ltb_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.7, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK LEFT-INSIDE-BACK")
		public final void itb_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK LEFT-INSIDE-BACK")
		public final void rtb_lib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, .5, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-INSIDE-BACK")
		public final void lbf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2325925925925927,2.08,3.8518518518518516), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-INSIDE-BACK")
		public final void ibf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5770370370370372,2.08,3.8518518518518516), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-INSIDE-BACK")
		public final void rbf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8066666666666666,2.08,3.8518518518518516), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-INSIDE-BACK")
		public final void lif_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1400000000000001,2.61375,3.5625), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-INSIDE-BACK")
		public final void iif_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.57,2.6100000000000003,3.5), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-INSIDE-BACK")
		public final void rif_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-INSIDE-BACK")
		public final void ltf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1733333333333336,2.94,3.666666666666667), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-INSIDE-BACK")
		public final void itf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5733333333333333,2.9400000000000004,3.666666666666667), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-INSIDE-BACK")
		public final void rtf_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-INSIDE-BACK")
		public final void lbi_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0330964467005075,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-INSIDE-BACK")
		public final void ibi_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5645685279187818,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-INSIDE-BACK")
		public final void rbi_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9188832487309644,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-INSIDE-BACK")
		public final void lii_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.5500000000000003,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-INSIDE-BACK")
		public final void iii_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.51875,2.45625,3.7), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-INSIDE-BACK")
		public final void rii_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-INSIDE-BACK")
		public final void lti_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-INSIDE-BACK")
		public final void iti_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5445103857566767,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-INSIDE-BACK")
		public final void rti_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK INSIDE-INSIDE-BACK")
		public final void lbb_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1851851851851851,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK INSIDE-INSIDE-BACK")
		public final void ibb_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.574074074074074,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK INSIDE-INSIDE-BACK")
		public final void rbb_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8333333333333335,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK INSIDE-INSIDE-BACK")
		public final void lib_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.5875, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK INSIDE-INSIDE-BACK")
		public final void iib_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 2.4, 4), result1);
			assertEpsilonEquals(createPoint(1.5, 2.4, 5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK INSIDE-INSIDE-BACK")
		public final void rib_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-INSIDE-BACK")
		public final void ltb_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0666666666666669,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK INSIDE-INSIDE-BACK")
		public final void itb_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5666666666666669,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK INSIDE-INSIDE-BACK")
		public final void rtb_iib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 1.6, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-INSIDE-BACK")
		public final void lbf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.6351619299405167), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-INSIDE-BACK")
		public final void ibf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.3685968819599093), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-INSIDE-BACK")
		public final void rbf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.0,3.556910569105689), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-INSIDE-BACK")
		public final void lif_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8399999999999999,2.597142857142857,3.2857142857142856), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-INSIDE-BACK")
		public final void iif_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.5759085799925066,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-INSIDE-BACK")
		public final void rif_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-INSIDE-BACK")
		public final void ltf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9333333333333336,2.9785714285714286,3.4523809523809534), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-INSIDE-BACK")
		public final void itf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-INSIDE-BACK")
		public final void rtf_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-INSIDE-BACK")
		public final void lbi_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8079187817258886,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-INSIDE-BACK")
		public final void ibi_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-INSIDE-BACK")
		public final void rbi_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-INSIDE-BACK")
		public final void lii_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.025,2.5098214285714286,3.9857142857142858), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-INSIDE-BACK")
		public final void iii_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.74375,2.45625,3.7), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-INSIDE-BACK")
		public final void rii_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-INSIDE-BACK")
		public final void lti_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.246290801186944,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-INSIDE-BACK")
		public final void iti_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-INSIDE-BACK")
		public final void rti_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK RIGHT-INSIDE-BACK")
		public final void lbb_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK RIGHT-INSIDE-BACK")
		public final void ibb_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK RIGHT-INSIDE-BACK")
		public final void rbb_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK RIGHT-INSIDE-BACK")
		public final void lib_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.6142857142857143,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK RIGHT-INSIDE-BACK")
		public final void iib_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.5153846153846153,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK RIGHT-INSIDE-BACK")
		public final void rib_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK RIGHT-INSIDE-BACK")
		public final void ltb_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.9571428571428573,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK RIGHT-INSIDE-BACK")
		public final void itb_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK RIGHT-INSIDE-BACK")
		public final void rtb_rib(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 2.8, 2.7, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT LEFT-TOP-BACK")
		public final void lbf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.0693069306930694), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT LEFT-TOP-BACK")
		public final void ibf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.980769230769231,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT LEFT-TOP-BACK")
		public final void rbf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2999999999999998,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT LEFT-TOP-BACK")
		public final void lif_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT LEFT-TOP-BACK")
		public final void iif_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT LEFT-TOP-BACK")
		public final void rif_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT LEFT-TOP-BACK")
		public final void ltf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT LEFT-TOP-BACK")
		public final void itf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT LEFT-TOP-BACK")
		public final void rtf_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE LEFT-TOP-BACK")
		public final void lbi_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,2.5978647686832734,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE LEFT-TOP-BACK")
		public final void ibi_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.102322206095791,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE LEFT-TOP-BACK")
		public final void rbi_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.704644412191582,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE LEFT-TOP-BACK")
		public final void lii_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.87018544935806), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE LEFT-TOP-BACK")
		public final void iii_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.3846153846153846,2.7,3.5846153846153843), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE LEFT-TOP-BACK")
		public final void rii_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE LEFT-TOP-BACK")
		public final void lti_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE LEFT-TOP-BACK")
		public final void iti_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(1.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE LEFT-TOP-BACK")
		public final void rti_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK LEFT-TOP-BACK")
		public final void lbb_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK LEFT-TOP-BACK")
		public final void ibb_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1, 2, 4), result1);
			assertEpsilonEquals(createPoint(1.1, 2, 5), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK LEFT-TOP-BACK")
		public final void rbb_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.7, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK LEFT-TOP-BACK")
		public final void lib_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK LEFT-TOP-BACK")
		public final void iib_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.2692307692307692,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK LEFT-TOP-BACK")
		public final void rib_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK LEFT-TOP-BACK")
		public final void ltb_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK LEFT-TOP-BACK")
		public final void itb_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK LEFT-TOP-BACK")
		public final void rtb_ltb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, .5, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT INSIDE-TOP-BACK")
		public final void lbf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.0116110304789547), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT INSIDE-TOP-BACK")
		public final void ibf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.56, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT INSIDE-TOP-BACK")
		public final void rbf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.9600000000000002,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT INSIDE-TOP-BACK")
		public final void lif_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT INSIDE-TOP-BACK")
		public final void iif_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.552141057934509,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT INSIDE-TOP-BACK")
		public final void rif_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT INSIDE-TOP-BACK")
		public final void ltf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT INSIDE-TOP-BACK")
		public final void itf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5525222551928786,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT INSIDE-TOP-BACK")
		public final void rtf_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE INSIDE-TOP-BACK")
		public final void lbi_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2.5, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE INSIDE-TOP-BACK")
		public final void ibi_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.539767779390421,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE INSIDE-TOP-BACK")
		public final void rbi_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.091988130563799,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE INSIDE-TOP-BACK")
		public final void lii_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE INSIDE-TOP-BACK")
		public final void iii_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5115384615384615,2.7,3.5846153846153843), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE INSIDE-TOP-BACK")
		public final void rii_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE INSIDE-TOP-BACK")
		public final void lti_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.669026548672566), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE INSIDE-TOP-BACK")
		public final void iti_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(1.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE INSIDE-TOP-BACK")
		public final void rti_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK INSIDE-TOP-BACK")
		public final void lbb_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK INSIDE-TOP-BACK")
		public final void ibb_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.54, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK INSIDE-TOP-BACK")
		public final void rbb_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.777777777777782,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK INSIDE-TOP-BACK")
		public final void lib_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK INSIDE-TOP-BACK")
		public final void iib_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.523076923076923,3.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK INSIDE-TOP-BACK")
		public final void rib_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK INSIDE-TOP-BACK")
		public final void ltb_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK INSIDE-TOP-BACK")
		public final void itb_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK INSIDE-TOP-BACK")
		public final void rtb_itb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 1.6, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-FRONT RIGHT-TOP-BACK")
		public final void lbf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 0, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.6799999999999997,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-FRONT RIGHT-TOP-BACK")
		public final void ibf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 0, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.9318096665417763,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-FRONT RIGHT-TOP-BACK")
		public final void rbf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 0, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2.0,2.959346353128736,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-FRONT RIGHT-TOP-BACK")
		public final void lif_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 0, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4599496221662467,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-FRONT RIGHT-TOP-BACK")
		public final void iif_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 0, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-FRONT RIGHT-TOP-BACK")
		public final void rif_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 0, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-FRONT RIGHT-TOP-BACK")
		public final void ltf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 0, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.4706231454005936,3.0,3.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-FRONT RIGHT-TOP-BACK")
		public final void itf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 0, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 3, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-FRONT RIGHT-TOP-BACK")
		public final void rtf_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 0, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-INSIDE RIGHT-TOP-BACK")
		public final void lbi_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 3.4, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.1134978229317851,2.0,4.0), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-INSIDE RIGHT-TOP-BACK")
		public final void ibi_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 3.4, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-INSIDE RIGHT-TOP-BACK")
		public final void rbi_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 3.4, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-INSIDE RIGHT-TOP-BACK")
		public final void lii_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 3.4, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.8778082191780823), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-INSIDE RIGHT-TOP-BACK")
		public final void iii_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 3.4, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.65,2.7,3.5846153846153843), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-INSIDE RIGHT-TOP-BACK")
		public final void rii_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 3.4, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-INSIDE RIGHT-TOP-BACK")
		public final void lti_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 3.4, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.0,3.0,3.7199999999999998), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-INSIDE RIGHT-TOP-BACK")
		public final void iti_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 3.4, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 3.4), result1);
			assertEpsilonEquals(createPoint(1.5, 3.6, 3.4), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-INSIDE RIGHT-TOP-BACK")
		public final void rti_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 3.4, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-BOTTOM-BACK RIGHT-TOP-BACK")
		public final void lbb_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 0, 5, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.12, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-BOTTOM-BACK RIGHT-TOP-BACK")
		public final void ibb_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 0, 5, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-BOTTOM-BACK RIGHT-TOP-BACK")
		public final void rbb_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 0, 5, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(2, 2, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-INSIDE-BACK RIGHT-TOP-BACK")
		public final void lib_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 2.4, 5, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-INSIDE-BACK RIGHT-TOP-BACK")
		public final void iib_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 2.4, 5, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.8, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-INSIDE-BACK RIGHT-TOP-BACK")
		public final void rib_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 2.4, 5, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("LEFT-TOP-BACK RIGHT-TOP-BACK")
		public final void ltb_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					0, 3.6, 5, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("INSIDE-TOP-BACK RIGHT-TOP-BACK")
		public final void itb_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					1.5, 3.6, 5, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1.5, 3, 4), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

		@ParameterizedTest(name = "{index} => {0}")
		@EnumSource(CoordinateSystem3D.class)
		@DisplayName("RIGHT-TOP-BACK RIGHT-TOP-BACK")
		public final void rtb_rtb(CoordinateSystem3D cs) {
			CoordinateSystem3D.setDefaultCoordinateSystem(cs);
			AlignedBox3afp.findsClosestPointAlignedBoxSegment(
					1, 2, 3, 2, 3, 4,
					2.5, 3.6, 5, 2.8, 5, 5,
					reset(result1), reset(result2));
			assertEpsilonEquals(createPoint(1, 2, 3), result1);
			assertEpsilonEquals(createPoint(.5, 1, 1), result2);
		}

	}

}
