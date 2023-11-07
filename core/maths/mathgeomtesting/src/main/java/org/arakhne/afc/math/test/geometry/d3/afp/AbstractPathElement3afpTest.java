/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.GeomFactory3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.math.geometry.d3.d.GeomFactory3d;
import org.arakhne.afc.math.test.AbstractMathTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("all")
public abstract class AbstractPathElement3afpTest<T extends PathElement3afp, P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>>
		extends AbstractMathTestCase {

	protected static GeomFactory3afp<?, ?, ?, ?, ?> FACTORY;

	private static Stream<Arguments> proposeArguments() {
		final GeomFactory3afp<?, ?, ?, ?, ?> factory = FACTORY;
		final List<Arguments> args = new ArrayList<>();
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			args.add(Arguments.of(cs, factory.newMovePathElement(3, 2, 1), PathElementType.MOVE_TO));
			args.add(Arguments.of(cs, factory.newLinePathElement(6, 5, 4, 3, 2, 1), PathElementType.LINE_TO));
			args.add(Arguments.of(cs, factory.newCurvePathElement(9, 8, 7, 6, 5, 4, 3, 2, 1), PathElementType.QUAD_TO));
			args.add(Arguments.of(cs, factory.newCurvePathElement(12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1), PathElementType.CURVE_TO));
			args.add(Arguments.of(cs, factory.newClosePathElement(6, 5, 4, 3, 2, 1), PathElementType.CLOSE));
		}
		return args.stream();
	}

	@DisplayName("getToX")
	@ParameterizedTest(name = "{index} => {0} - {1} - {2}")
	@MethodSource("proposeArguments")
	public void getToX(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3, element.getToX());
	}

	@DisplayName("getToXY")
	@ParameterizedTest(name = "{index} => {0} - {1} - {2}")
	@MethodSource("proposeArguments")
	public void getToY(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2, element.getToY());
	}

	@DisplayName("getToZ")
	@ParameterizedTest(name = "{index} => {0} - {1} - {2}")
	@MethodSource("proposeArguments")
	public void getToZ(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, element.getToZ());
	}

	@DisplayName("getType")
	@ParameterizedTest(name = "{index} => {0} - {1} - {2}")
	@MethodSource("proposeArguments")
	public void getType(CoordinateSystem3D cs, PathElement3afp element, PathElementType type) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(type, element.getType());
	}

}
