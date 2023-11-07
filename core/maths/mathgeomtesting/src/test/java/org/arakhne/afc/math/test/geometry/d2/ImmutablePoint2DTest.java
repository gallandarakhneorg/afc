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

package org.arakhne.afc.math.test.geometry.d2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.ImmutablePoint2D;
import org.arakhne.afc.math.geometry.d2.ImmutableVector2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.test.geometry.d2.AbstractUnmodifiablePoint2DTest;

@SuppressWarnings("all")
public class ImmutablePoint2DTest extends AbstractUnmodifiablePoint2DTest<ImmutablePoint2D, ImmutableVector2D> {
	
	@Override
	public ImmutableVector2D createVector(final double tx, final double ty) {
		return new ImmutableVector2D(tx,  ty);
	}

	@Override
	public ImmutablePoint2D createPoint(final double tx, final double ty) {
		return new ImmutablePoint2D(tx, ty);
	}
	
	@Override
	public Point2D createTuple(double x, double y) {
		return new ImmutablePoint2D(x, y);
	}

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		//
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		//
	}

}
