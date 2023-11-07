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

package org.arakhne.afc.math.geometry.d3.dfx;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.test.geometry.AbstractUnmodifiablePoint3DTest;

@SuppressWarnings("all")
@Disabled("temporary")
public class UnmodifiablePoint3dfxTest extends AbstractUnmodifiablePoint3DTest<Point3dfx, Vector3dfx> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Point3D createTuple(double x, double y, double z) {
		return new Point3dfx(x, y, z).toUnmodifiable();
	}
	
	@Override
	public Vector3dfx createVector(double x, double y, double z) {
		return new Vector3dfx(x, y, z);
	}

	@Override
	public Point3dfx createPoint(double x, double y, double z) {
		return new Point3dfx(x, y, z);
	}

	@Override
	public void operator_andShape3D() {
		Shape3D shape = new Sphere3dfx(5, 8, 0, 5);
		assertFalse(createPoint(0,0, 0).operator_and(shape));
		assertFalse(createPoint(11,10, 0).operator_and(shape));
		assertFalse(createPoint(11,50, 0).operator_and(shape));
		assertFalse(createPoint(9,12, 0).operator_and(shape));
		assertTrue(createPoint(9,11, 0).operator_and(shape));
		assertTrue(createPoint(8,12, 0).operator_and(shape));
		assertTrue(createPoint(3,7, 0).operator_and(shape));
		assertFalse(createPoint(10,11, 0).operator_and(shape));
		assertTrue(createPoint(9,10, 0).operator_and(shape));
	}
	
	@Override
	public void operator_upToShape3D() {
		Shape3D shape = new Sphere3dfx(5, 8, 0, 5);
		assertEpsilonEquals(3.74643, createPoint(.5,.5, 0).operator_upTo(shape));
		assertEpsilonEquals(7.9769, createPoint(-1.2,-3.4, 0).operator_upTo(shape));
		assertEpsilonEquals(1.6483, createPoint(-1.2,5.6, 0).operator_upTo(shape));
		assertEpsilonEquals(0, createPoint(7.6,5.6, 0).operator_upTo(shape));
	}

}
