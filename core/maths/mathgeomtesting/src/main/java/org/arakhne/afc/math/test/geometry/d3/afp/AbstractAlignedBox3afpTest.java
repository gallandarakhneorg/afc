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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;

@SuppressWarnings("all")
public abstract class AbstractAlignedBox3afpTest<T extends AlignedBox3afp<?, T, ?, ?, ?, ?, B>,
		B extends AlignedBox3afp<?, ?, ?, ?, ?, ?, B>> extends AbstractBox3afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createAlignedBox(5, 8, 0, 5, 10, 1);
	}

	@DisplayName("intersectsAlignedBoxAlignedBox(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsAlignedBoxAlignedBox() {
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 1, 0, 0, 0, 1, 1, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(0, 0, 0, 1, 1, 1, 5, 8, 0, 10, 18, 1));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 1, 0, 20, 0, 1, 22, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(0, 20, 0, 1, 22, 1, 5, 8, 0, 10, 18, 1));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 1, 0, 0, 0, 5, 100, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxAlignedBox(0, 0, 0, 5, 100, 1, 5, 8, 0, 10, 18, 1));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 1, 0, 0, 0, 5.1, 100, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(0, 0, 0, 5.1, 100, 1, 5, 8, 0, 10, 18, 1));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 1, 6, 9, 0, 9.5, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 9, 0, 9.5, 15, 1, 5, 8, 0, 10, 18, 1));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 1, 6, 9, 0, 9.5, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxAlignedBox(6, 9, 0, 9.5, 15, 1, 5, 8, 0, 10, 18, 1));
	}

	@DisplayName("intersectsAlignedBoxLine(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsAlignedBoxLine() {
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(1, 1, 0, 3, 3, 0, 5, 5, 0, 2, 2, 0));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(1, 1, 1, 3, 3, 3, 5, 5, 5, 2, 2, 2));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(1, 1, 1, 3, 3, 3, 2, 2, 2, 5, 5, 5));
		
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(1, 1, 1, 3, 3, 3, 5, 5, 5, 3, 3, 3));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(1, 1, 1, 3, 3, 3, 3, 3, 3, 5, 5, 5));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(1, 1, 1, 3, 3, 3, 5, 5, 5, 4, 4, 4));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(1, 1, 1, 3, 3, 3, 4, 4, 4, 5, 5, 5));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 20, 45, 0, 43, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 20, 55, 0, 43, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 20, 0, 0, 43, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 0, 45, 0, 43, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 20, 45, 0, 60, 15, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 5, 45, 0, 30, 55, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 40, 55, 0, 60, 15, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 40, 0, 0, 60, 40, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 0, 40, 0, 20, 0, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 0, 45, 0, 100, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 20, 100, 0, 43, 0, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 20, 100, 0, 43, 101, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 100, 45, 0, 102, 15, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 20, 0, 0, 43, -2, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 50, 49, 1, -100, 45, 0, -48, 15, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, -100, 60, 0, -98, 61, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxLine(10, 12, 0, 40, 37, 1, 0, 30, 0, 9, 21, 1));
	}

	@DisplayName("intersectsAlignedBoxSegment(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsAlignedBoxSegment() {
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(1, 1, 0, 3, 3, 0, 5, 5, 0, 2, 2, 0));

		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(1, 1, 1, 3, 3, 3, 5, 5, 5, 2, 2, 2));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(1, 1, 1, 3, 3, 3, 2, 2, 2, 5, 5, 5));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(1, 1, 1, 3, 3, 3, 5, 5, 5, 3, 3, 3));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(1, 1, 1, 3, 3, 3, 3, 3, 3, 5, 5, 5));

		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(1, 1, 1, 3, 3, 3, 5, 5, 5, 4, 4, 4));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(1, 1, 1, 3, 3, 3, 4, 4, 4, 5, 5, 5));
		
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 20, 45, 0, 43, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 20, 55, 0, 43, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 20, 0, 0, 43, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 0, 45, 0, 43, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 20, 45, 0, 60, 15, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 5, 45, 0, 30, 55, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 40, 55, 0, 60, 15, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 40, 0, 0, 60, 40, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 0, 40, 0, 20, 0, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 0, 45, 0, 100, 15, 1));
		assertTrue(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 20, 100, 0, 43, 0, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 20, 100, 0, 43, 101, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 100, 45, 0, 102, 15, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 20, 0, 0, 43, -2, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 50, 49, 1, -100, 45, 0, -48, 15, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, -100, 60, 0, -98, 61, 1));
		assertFalse(AlignedBox3afp.intersectsAlignedBoxSegment(10, 12, 0, 40, 37, 1, 0, 30, 0, 9, 21, 1));
	}

	@DisplayName("containsAlignedBoxAlignedBox(double,double,double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticContainsAlignedBoxAlignedBox() {
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 0, 0, 0, 0, 1, 1, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(0, 0, 0, 1, 1, 0, 5, 8, 0, 10, 18, 0));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 0, 0, 20, 0, 1, 22, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(0, 20, 0, 1, 22, 0, 5, 8, 0, 10, 18, 0));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 0, 0, 0, 0, 5, 100, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(0, 0, 0, 5, 100, 0, 5, 8, 0, 10, 18, 0));

		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 0, 0, 0, 0, 5.1, 100, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(0, 0, 0, 5.1, 100, 0, 5, 8, 0, 10, 18, 0));

		assertTrue(AlignedBox3afp.containsAlignedBoxAlignedBox(5, 8, 0, 10, 18, 0, 6, 9, 0, 9.5, 15, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxAlignedBox(6, 9, 0, 9.5, 15, 0, 5, 8, 0, 10, 18, 0));
	}

	@DisplayName("containsAlignedBoxPoint(double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticContainsAlignedBoxPoint() {
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 20, 45, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 20, 55, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 20, 0, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 0, 45, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 5, 45, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 40, 55, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 40, 0, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 0, 40, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 20, 100, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 100, 45, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 50, 49, 0, -100, 45, 0));
		assertFalse(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, -100, 60, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 10, 12, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 40, 12, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 40, 37, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 10, 37, 0));
		assertTrue(AlignedBox3afp.containsAlignedBoxPoint(10, 12, 0, 40, 37, 0, 35, 24, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createAlignedBox(0, 8, 0, 5, 12, 0)));
		assertFalse(this.shape.equals(createAlignedBox(5, 8, 0, 5, 0, 0)));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createAlignedBox(5, 8, 0, 5, 10, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createAlignedBox(0, 8, 0, 5, 12, 0).getPathIterator()));
		assertFalse(this.shape.equals(createAlignedBox(5, 8, 0, 5, 0, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createAlignedBox(5, 8, 0, 5, 10, 0).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createAlignedBox(0, 8, 0, 5, 12, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createAlignedBox(5, 8, 0, 5, 0, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createAlignedBox(5, 8, 0, 5, 10, 0).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createAlignedBox(0, 8, 0, 5, 12, 0)));
		assertFalse(this.shape.equalsToShape((T) createAlignedBox(5, 8, 0, 5, 0, 0)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createAlignedBox(5, 8, 0, 5, 10, 0)));
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
		assertEpsilonEquals(1, this.shape.getMaxZ());

		this.shape.add(createPoint(-123.456, 456.789, 0));
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());

		this.shape.add(createPoint(-123.456, -456.789, 0));
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(-456.789, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("add(double,double)")
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
		assertEpsilonEquals(1, this.shape.getMaxZ());

		this.shape.add(-123.456, 456.789, 0);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());

		this.shape.add(-123.456, -456.789, 0);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(-456.789, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}
	
	@DisplayName("setUnion(Prism3afp)")
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

	@DisplayName("createUnion(Prism3afp)")
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
		assertEpsilonEquals(2, union.getMaxZ());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("setIntersection(Prism3afp) without intersection")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIntersection_noIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setIntersection(createAlignedBox(0, 0, 0, 12, 1, 0));
		assertTrue(this.shape.isEmpty());
	}

	@DisplayName("setIntersection(Prism3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIntersection_intersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setIntersection(createAlignedBox(0, 0, 0, 7, 10, 2));
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(7, this.shape.getMaxX());
		assertEpsilonEquals(10, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("createIntersection(Prism3afp) without intersection")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void createIntersection_noIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.createIntersection(createAlignedBox(0, 0, 0, 12, 1, 2));
		assertNotSame(this.shape, box);
		assertTrue(box.isEmpty());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("createIntersection(Prism3afp)")
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
		assertEpsilonEquals(1, box.getMaxZ());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(0, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getMaxZ());
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
		assertEpsilonEquals(2, this.shape.getMaxZ());
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
		assertEpsilonEquals(2, this.shape.getMaxZ());
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
		assertEpsilonEquals(2, this.shape.getMaxZ());
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
		assertEpsilonEquals(2, this.shape.getMaxZ());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(8, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 0, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(8, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(100, 100, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(18, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 100, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(18, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(0, 10, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());
		assertEpsilonEquals(10, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 0, 0));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(8, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(154, 17, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());
		assertEpsilonEquals(17, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(9, 154, 0));
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(18, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(8, 18, 0));
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(18, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(7, 12, 0));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
		assertEpsilonEquals(12, p.getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(12, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 0, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(12, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(100, 100, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(12, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 100, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(12, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(0, 10, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(12, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 0, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(12, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(154, 17, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(12, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(9, 154, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(12, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(8, 18, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(12, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(7, 12, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
		assertEpsilonEquals(12, p.getZ());
	}

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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createAlignedBox(123.456, 456.789, 0, 789.123, 159.753, 0));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(456.789, this.shape.getMinY());
		assertEpsilonEquals(456.789, this.shape.getMinZ());
		assertEpsilonEquals(912.579, this.shape.getMaxX());
		assertEpsilonEquals(616.542, this.shape.getMaxY());
		assertEpsilonEquals(616.542, this.shape.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 10, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 10, 18, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 18, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi;
		
		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 10, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 10, 18, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 18, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertNoElement(pi);

		pi = this.shape.getPathIterator(new Transform3D());
		assertElement(pi, PathElementType.MOVE_TO, 5, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 10, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 10, 18, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 18, 0);
		assertElement(pi, PathElementType.CLOSE, 5, 8, 0);
		assertNoElement(pi);

		Transform3D tr;
		tr = new Transform3D();
		tr.setTranslation(123.456, 456.789, 0);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789, 0);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789, 0);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789, 0);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789, 0);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void createTransformedShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		tr = new Transform3D();
		tr.setTranslation(123.456, 456.789, 0);
		PathIterator3afp pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789, 0);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789, 0);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789, 0);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789, 0);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789, 0);
		assertNoElement(pi);
	}

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

		assertTrue(this.shape.intersects(createAlignedBox(0, 0, 0, 5.1, 100, 0)));
		assertTrue(createAlignedBox(0, 0, 0, 5.1, 100, 0).intersects(this.shape));

		assertTrue(this.shape.intersects(createAlignedBox(6, 9, 0, .5, 9, 0)));
		assertTrue(createAlignedBox(6, 9, 0, .5, 9, 0).intersects(this.shape));

		assertTrue(this.shape.intersects(createAlignedBox(0, 0, 0, 5.1, 8.1, 0)));
		assertTrue(createAlignedBox(0, 0, 0, 5.1, 8.1, 0).intersects(this.shape));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(createAlignedBox(0, 0, 0, .5, .5, 0).intersects(createSphere(0, 0, 0, 1)));
		assertTrue(createAlignedBox(0, 0, 0, 1, 1, 0).intersects(createSphere(0, 0, 0, 1)));
		assertTrue(createAlignedBox(0, 0, 0, .5, 1, 0).intersects(createSphere(0, 0, 0, 1)));
		assertFalse(createAlignedBox(0, 0, 0, 1, 1, 0).intersects(createSphere(2, 0, 0, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(20, 45, 0, 43, 15, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(20, 55, 0, 43, 15, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(20, 0, 0, 43, 15, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(0, 45, 0, 43, 15, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(20, 45, 0, 60, 15, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(5, 45, 0, 30, 55, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(40, 55, 0, 60, 15, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(40, 0, 0, 60, 40, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(0, 40, 0, 20, 0, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(0, 45, 0, 100, 15, 0)));
		assertTrue(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(20, 100, 0, 43, 0, 0)));
		assertFalse(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(20, 100, 0, 43, 101, 0)));
		assertFalse(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(100, 45, 0, 102, 15, 0)));
		assertFalse(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(20, 0, 0, 43, -2, 0)));
		assertFalse(createAlignedBox(10, 12, 0, 50, 49, 0).intersects(createSegment(-100, 45, 0, -48, 15, 0)));
		assertFalse(createAlignedBox(10, 12, 0, 40, 37, 0).intersects(createSegment(-100, 60, 0, -98, 61, 0)));
	}

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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsPathIterator3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, ?, B> p;
		
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void inflate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.inflate(1, 2, 0, 3, 4, 0);
		assertEpsilonEquals(4, this.shape.getMinX());
		assertEpsilonEquals(6, this.shape.getMinY());
		assertEpsilonEquals(6, this.shape.getMinZ());
		assertEpsilonEquals(13, this.shape.getMaxX());
		assertEpsilonEquals(22, this.shape.getMaxY());
		assertEpsilonEquals(22, this.shape.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(createAlignedBox(0, 0, 0, 5.1, 100, 0).intersects((Shape3D) this.shape));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(123.456, 456.789, 0));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(464.789, this.shape.getMinZ());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
		assertEpsilonEquals(474.789, this.shape.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(123.456, 456.789, 0));
		assertEpsilonEquals(128.456, shape.getMinX());
		assertEpsilonEquals(464.789, shape.getMinY());
		assertEpsilonEquals(464.789, shape.getMinZ());
		assertEpsilonEquals(133.456, shape.getMaxX());
		assertEpsilonEquals(474.789, shape.getMaxY());
		assertEpsilonEquals(474.789, shape.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(123.456, 456.789, 0));
		assertEpsilonEquals(-118.456, this.shape.getMinX());
		assertEpsilonEquals(-448.789, this.shape.getMinY());
		assertEpsilonEquals(-448.789, this.shape.getMinZ());
		assertEpsilonEquals(-113.456, this.shape.getMaxX());
		assertEpsilonEquals(-438.789, this.shape.getMaxY());
		assertEpsilonEquals(-438.789, this.shape.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(123.456, 456.789, 0));
		assertEpsilonEquals(-118.456, shape.getMinX());
		assertEpsilonEquals(-448.789, shape.getMinY());
		assertEpsilonEquals(-448.789, shape.getMinZ());
		assertEpsilonEquals(-113.456, shape.getMaxX());
		assertEpsilonEquals(-438.789, shape.getMaxY());
		assertEpsilonEquals(-438.789, shape.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		tr = new Transform3D();
		tr.setTranslation(123.456, 456.789, 0);
		PathIterator3afp pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
		assertNoElement(pi);
	}

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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(createAlignedBox(0, 0, 0, 5.1, 100, 0).operator_and(this.shape));
	}

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
		assertEpsilonEquals(.5, p.getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getCenterX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7.5, this.shape.getCenterX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getCenterY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getCenterY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getCenterZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getCenterZ());
	}

	@DisplayName("setCenter(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setCenterDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setCenter(145, -47, 14);
		assertEpsilonEquals(142.5, this.shape.getMinX());
		assertEpsilonEquals(-52, this.shape.getMinY());
		assertEpsilonEquals(13.5, this.shape.getMinZ());
		assertEpsilonEquals(147.5, this.shape.getMaxX());
		assertEpsilonEquals(-42, this.shape.getMaxY());
		assertEpsilonEquals(14.5, this.shape.getMaxZ());
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
		assertEpsilonEquals(1, this.shape.getMaxZ());
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
		assertEpsilonEquals(1, this.shape.getMaxZ());
	}

	@DisplayName("setCenterZ(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setCenterZDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setCenterZ(-47);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(-47.5, this.shape.getMinZ());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertEpsilonEquals(-46.5, this.shape.getMaxZ());
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

}
