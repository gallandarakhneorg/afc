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

package org.arakhne.afc.math.test.geometry.d3.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.io.Resources;
import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("all")
public abstract class AbstractAlignedBox3afpTest<T extends AlignedBox3afp<T, ?, ?, ?, ?, B>,
B extends AlignedBox3afp<?, ?, ?, ?, ?, B>> extends AbstractBox3afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createAlignedBox(5, 8, 0, 5, 10, 10);
	}

	@DisplayName("intersectsAlignedBoxAlignedBox(double,double,double,double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsAlignedBoxAlignedBox(CoordinateSystem3D cs) {
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
	public void staticIntersectsAlignedBoxLine(CoordinateSystem3D cs) {
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
	public void staticIntersectsAlignedBoxSegment(CoordinateSystem3D cs) {
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
	public void staticContainsAlignedBoxAlignedBox(CoordinateSystem3D cs) {
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
	public void staticContainsAlignedBoxPoint(CoordinateSystem3D cs) {
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
	public void equalsObject(CoordinateSystem3D cs) {
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
	public void equalsToShape(CoordinateSystem3D cs) {
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
	public void addPoint3D(CoordinateSystem3D cs) {
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
	public void addDoubleDouble(CoordinateSystem3D cs) {
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
	public void setUnion(CoordinateSystem3D cs) {
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
	public void createUnion(CoordinateSystem3D cs) {
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
	public void setIntersection_noIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setIntersection(createAlignedBox(0, 0, 0, 12, 1, 0));
		assertTrue(this.shape.isEmpty());
	}

	@DisplayName("setIntersection(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIntersection_intersection(CoordinateSystem3D cs) {
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
	public void createIntersection_noIntersection(CoordinateSystem3D cs) {
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
	public void createIntersection_intersection(CoordinateSystem3D cs) {
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
	public void avoidCollisionWithAlignedBox3afpVector3D(CoordinateSystem3D cs) {
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
	public void avoidCollisionWithAlignedBox3afpVector3DVector3D_nullDisplacement(CoordinateSystem3D cs) {
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
	public void avoidCollisionWithAlignedBox3afpVector3DVector3D_noDisplacement(CoordinateSystem3D cs) {
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
	public void avoidCollisionWithAlignedBox3afpVector3DVector3D_givenDisplacement(CoordinateSystem3D cs) {
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
	public void getClosestPointTo(CoordinateSystem3D cs) {
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
	public void getFarthestPointTo(CoordinateSystem3D cs) {
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
	public void getDistance(CoordinateSystem3D cs) {
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
	public void getDistanceSquared(CoordinateSystem3D cs) {
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
	public void getDistanceL1(CoordinateSystem3D cs) {
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
	public void getDistanceLinf(CoordinateSystem3D cs) {
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
	public void setIT(CoordinateSystem3D cs) {
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
	public void containsAlignedBox3afp(CoordinateSystem3D cs) {
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
	public void intersectsAlignedBox3afp(CoordinateSystem3D cs) {
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
	public void intersectsSphere3afp(CoordinateSystem3D cs) {
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

	@DisplayName("intersects(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSegment3afp(CoordinateSystem3D cs) {
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
	public void intersectsPath3afp(CoordinateSystem3D cs) {
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
	public void intersectsPathIterator3afp(CoordinateSystem3D cs) {
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
	public void containsDoubleDoubleDouble(CoordinateSystem3D cs) {
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
	public void containsPoint3D(CoordinateSystem3D cs) {
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
	public void inflate(CoordinateSystem3D cs) {
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
	public void intersectsShape3D(CoordinateSystem3D cs) {
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
	public void operator_addVector3D(CoordinateSystem3D cs) {
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
	public void operator_plusVector3D(CoordinateSystem3D cs) {
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
	public void operator_removeVector3D(CoordinateSystem3D cs) {
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
	public void operator_minusVector3D(CoordinateSystem3D cs) {
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
	public void operator_andPoint3D(CoordinateSystem3D cs) {
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
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(createAlignedBox(0, 0, 0, 5.1, 100, 0).operator_and(this.shape));
	}

	@DisplayName("b .. Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
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
	public void getCenter(CoordinateSystem3D cs) {
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
	public void getCenterX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7.5, this.shape.getCenterX());
	}

	@DisplayName("getCenterY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getCenterY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getCenterY());
	}

	@DisplayName("getCenterZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getCenterZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getCenterZ());
	}

	@DisplayName("setCenter(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setCenterDoubleDoubleDouble(CoordinateSystem3D cs) {
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
	public void setCenterXDouble(CoordinateSystem3D cs) {
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
	public void setCenterYDouble(CoordinateSystem3D cs) {
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
	public void setCenterZDouble(CoordinateSystem3D cs) {
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
	public void setDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
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
	public void staticCalculatesAlignedBoxPointDistanceSquared(CoordinateSystem3D cs) {
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
	public void staticCalculatesAlignedBoxPointDistanceL1(CoordinateSystem3D cs) {
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
	public void staticCalculatesAlignedBoxPointDistanceLinf(CoordinateSystem3D cs) {
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
	public void staticFindsClosestPointAlignedBoxPoint(CoordinateSystem3D cs) {
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
	public void staticFindsFarthestPointAlignedBoxPoint(CoordinateSystem3D cs) {
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
	public void staticFindsClosestPointAlignedBoxSphere(CoordinateSystem3D cs) {
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
	public void staticFindsFarthestPointAlignedBoxSphere(CoordinateSystem3D cs) {
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
		int code = MathConstants.COHEN_SUTHERLAND_INSIDE;
		for (final char it : strCode.toCharArray()) {
			switch (Character.toLowerCase(it)) {
			case 'l':
				code |= MathConstants.COHEN_SUTHERLAND_LEFT;
				break;
			case 'r':
				code |= MathConstants.COHEN_SUTHERLAND_RIGHT;
				break;
			case 'd':
				code |= MathConstants.COHEN_SUTHERLAND_BOTTOM;
				break;
			case 't':
				code |= MathConstants.COHEN_SUTHERLAND_TOP;
				break;
			case 'f':
				code |= MathConstants.COHEN_SUTHERLAND_FRONT;
				break;
			case 'b':
				code |= MathConstants.COHEN_SUTHERLAND_BACK;
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
		if ((code & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
			strCode.append('L');
		} else if ((code & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
			strCode.append('R');
		}
		if ((code & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
			strCode.append('D');
		} else if ((code & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
			strCode.append('T');
		}
		if ((code & MathConstants.COHEN_SUTHERLAND_FRONT) != 0) {
			strCode.append('F');
		} else if ((code & MathConstants.COHEN_SUTHERLAND_BACK) != 0) {
			strCode.append('B');
		}
		return strCode.toString();
	}

	private static Stream<Arguments> staticFindsCloseFarePointAlignedBoxAlignedBoxArguments(String csvFileBasename) throws Exception {
		final List<Arguments> args = new ArrayList<>();
		final URL resource = Resources.getResource(AbstractAlignedBox3afpTest.class, csvFileBasename);
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
	public void staticFindsClosestPointAlignedBoxAlignedBox(String label,
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
	public void staticFindsFarthestPointAlignedBoxAlignedBox(String label,
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
		final URL resource = Resources.getResource(AbstractAlignedBox3afpTest.class, "staticReducesCohenSutherlandZoneAlignedBoxSegmentArguments.csv");
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

	@DisplayName("reducesCohenSutherlandZoneAlignedBoxSegment(double,double,double,double,double,double,double,double,double,double,double,double,int,int,Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("staticReducesCohenSutherlandZoneAlignedBoxSegmentArguments")
	public void staticReducesCohenSutherlandZoneAlignedBoxSegment(String label,
			Point3D minBox, Point3D maxBox, Point3D seg1, int code1, Point3D seg2, int code2,
			int expectedCode, Point3D expected1, Point3D expected2) {
		Point3D p1;
		Point3D p2;
		int actualCode;
		
		final int acode1 = MathUtil.getCohenSutherlandCode3D(
				seg1.getX(), seg1.getY(), seg1.getZ(),
				minBox.getX(), minBox.getY(), minBox.getZ(), maxBox.getX(), maxBox.getY(), maxBox.getZ());
		assertEquals(code1, acode1, label + ": Invalid CODE1");
		final int acode2 = MathUtil.getCohenSutherlandCode3D(
				seg2.getX(), seg2.getY(), seg2.getZ(),
				minBox.getX(), minBox.getY(), minBox.getZ(), maxBox.getX(), maxBox.getY(), maxBox.getZ());
		assertEquals(code2, acode2, label + ": Invalid CODE2");

		// [seg1; seg2]
		p1 = createPoint(Double.NaN, Double.NaN, Double.NaN);
		p2 = createPoint(Double.NaN, Double.NaN, Double.NaN);
		actualCode = Segment3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
				minBox.getX(), minBox.getY(), minBox.getZ(), maxBox.getX(), maxBox.getY(), maxBox.getZ(),
				seg1.getX(), seg1.getY(), seg1.getZ(), seg2.getX(), seg2.getY(), seg2.getZ(),
				code1, code2,
				p1, p2);
		assertEquals(expectedCode, actualCode, label + ": Invalid EXP_CODE");
		assertEpsilonEquals(expected1, p1, label + ": Invalid P1_N");
		assertEpsilonEquals(expected2, p2, label + ": Invalid P2_N");

		// [seg2; seg1]
		p1 = createPoint(Double.NaN, Double.NaN, Double.NaN);
		p2 = createPoint(Double.NaN, Double.NaN, Double.NaN);
		actualCode = Segment3afp.reducesCohenSutherlandZoneAlignedBoxSegment(
				minBox.getX(), minBox.getY(), minBox.getZ(), maxBox.getX(), maxBox.getY(), maxBox.getZ(),
				seg2.getX(), seg2.getY(), seg2.getZ(), seg1.getX(), seg1.getY(), seg1.getZ(),
				code2, code1,
				p1, p2);
		assertEquals(expectedCode, actualCode, label + ": Invalid EXP_CODE");
		assertEpsilonEquals(expected2, p1, label + ": Invalid P2_N");
		assertEpsilonEquals(expected1, p2, label + ": Invalid P1_N");
	}

	private static Stream<Arguments> staticFindsCloseFarePointAlignedBoxSegmentArguments(String csvFileBasename) throws Exception {
		final List<Arguments> args = new ArrayList<>();
		final URL resource = Resources.getResource(AbstractAlignedBox3afpTest.class, csvFileBasename);
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

	@DisplayName("findsClosestPointAlignedBoxSegment(double,double,double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("staticFindsClosestPointAlignedBoxSegmentArguments")
	public void staticFindsClosestPointAlignedBoxSegment(String label,
			Point3D minBox, Point3D maxBox, Point3D seg1, Point3D seg2, Point3D expected) {
		Point3D p;

		// [seg1; seg2]
		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSegment(
				minBox.getX(), minBox.getY(), minBox.getZ(), maxBox.getX(), maxBox.getY(), maxBox.getZ(),
				seg1.getX(), seg1.getY(), seg1.getZ(), seg2.getX(), seg2.getY(), seg2.getZ(),
				p);
		assertEpsilonEquals(expected, p, label);
		assertEpsilonInRange(minBox.getX(), maxBox.getX(), p.getX());
		assertEpsilonInRange(minBox.getY(), maxBox.getY(), p.getY());
		assertEpsilonInRange(minBox.getZ(), maxBox.getZ(), p.getZ());

		// [seg2; seg1]
		p = createPoint(Double.NaN, Double.NaN, Double.NaN);
		AlignedBox3afp.findsClosestPointAlignedBoxSegment(
				minBox.getX(), minBox.getY(), minBox.getZ(), maxBox.getX(), maxBox.getY(), maxBox.getZ(),
				seg2.getX(), seg2.getY(), seg2.getZ(), seg1.getX(), seg1.getY(), seg1.getZ(),
				p);
		assertEpsilonEquals(expected, p, label);
		assertEpsilonInRange(minBox.getX(), maxBox.getX(), p.getX());
		assertEpsilonInRange(minBox.getY(), maxBox.getY(), p.getY());
		assertEpsilonInRange(minBox.getZ(), maxBox.getZ(), p.getZ());
	}

	@DisplayName("findsFarthestPointAlignedBoxSegment(double,double,double,double,double,double,double,double,double,double,double,double,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticFindsFarthestPointAlignedBoxSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		fail("TODO");
	}

}
