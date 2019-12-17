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

package org.arakhne.afc.math.test.geometry.d2.d;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.d.Circle2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.test.geometry.d2.AbstractUnmodifiablePoint2DTest;

@SuppressWarnings("all")
public class UnmodifiablePoint2dTest extends AbstractUnmodifiablePoint2DTest<Point2d, Vector2d> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Point2D createTuple(double x, double y) {
		return new Point2d(x, y).toUnmodifiable();
	}
	
	@Override
	public Vector2d createVector(double x, double y) {
		return new Vector2d(x, y);
	}

	@Override
	public Point2d createPoint(double x, double y) {
		return new Point2d(x, y);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Shape2D shape = new Circle2d(5, 8, 5);
		assertFalse(createPoint(0,0).operator_and(shape));
		assertFalse(createPoint(11,10).operator_and(shape));
		assertFalse(createPoint(11,50).operator_and(shape));
		assertFalse(createPoint(9,12).operator_and(shape));
		assertTrue(createPoint(9,11).operator_and(shape));
		assertTrue(createPoint(8,12).operator_and(shape));
		assertTrue(createPoint(3,7).operator_and(shape));
		assertFalse(createPoint(10,11).operator_and(shape));
		assertTrue(createPoint(9,10).operator_and(shape));
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Shape2D shape = new Circle2d(5, 8, 5);
		assertEpsilonEquals(3.74643, createPoint(.5,.5).operator_upTo(shape));
		assertEpsilonEquals(7.9769, createPoint(-1.2,-3.4).operator_upTo(shape));
		assertEpsilonEquals(1.6483, createPoint(-1.2,5.6).operator_upTo(shape));
		assertEpsilonEquals(0, createPoint(7.6,5.6).operator_upTo(shape));
	}

}
