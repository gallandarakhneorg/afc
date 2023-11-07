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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Shape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;

@SuppressWarnings("all")
public abstract class AbstractSphere3afpTest<T extends Sphere3afp<?, T, ?, ?, ?, ?, B>,
		B extends AlignedBox3afp<?, ?, ?, ?, ?, ?, B>> extends AbstractShape3afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSphere(5, 8, 9, 5);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(5, clone.getX());
		assertEpsilonEquals(8, clone.getY());
		assertEpsilonEquals(9, clone.getZ());
		assertEpsilonEquals(5, clone.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSphere(0, 0, 0, 5)));
		assertFalse(this.shape.equals(createSphere(5, 8, 0, 6)));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 6, 10, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSphere(5, 8, 9, 5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createSphere(0, 0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equals(createSphere(5, 8, 0, 6).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 6, 10, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSphere(5, 8, 9, 5).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createSphere(0, 0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSphere(5, 8, 0, 6).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 6, 10, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSphere(5, 8, 9, 5).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSphere(0, 0, 0, 5)));
		assertFalse(this.shape.equalsToShape((T) createSphere(5, 8, 0, 6)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSphere(5, 8, 9, 5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEpsilonEquals(0, this.shape.getX());
		assertEpsilonEquals(0, this.shape.getY());
		assertEpsilonEquals(0, this.shape.getZ());
		assertEpsilonEquals(0, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(0, 0, 9));
		assertFalse(this.shape.contains(11, 10, 9));
		assertFalse(this.shape.contains(11, 50, 9));
		assertFalse(this.shape.contains(9, 12, 9));
		assertTrue(this.shape.contains(9,11, 9));
		assertTrue(this.shape.contains(8,12, 9));
		assertTrue(this.shape.contains(3,7, 9));
		assertFalse(this.shape.contains(10,11, 9));
		assertTrue(this.shape.contains(9,10, 9));
		
		this.shape = (T) createSphere(-1, -1, -1, 1);
		assertFalse(this.shape.contains(0, 0, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createPoint(0, 0, 9)));
		assertFalse(this.shape.contains(createPoint(11, 10, 9)));
		assertFalse(this.shape.contains(createPoint(11, 50, 9)));
		assertFalse(this.shape.contains(createPoint(9, 12, 9)));
		assertTrue(this.shape.contains(createPoint(9, 11, 9)));
		assertTrue(this.shape.contains(createPoint(8, 12, 9)));
		assertTrue(this.shape.contains(createPoint(3, 7, 9)));
		assertFalse(this.shape.contains(createPoint(10, 11, 9)));
		assertTrue(this.shape.contains(createPoint(9, 10, 9)));
		
		this.shape = (T) createSphere(-1, -1, 0, 1);
		assertFalse(this.shape.contains(createPoint(0, 0, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(5, 8, 0));
		assertNotNull(p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(4, p.getZ());
		
		p = this.shape.getClosestPointTo(createPoint(10, 10, 0));
		assertNotNull(p);
		assertEpsilonEquals(7.383656, p.getX());
		assertEpsilonEquals(8.953462, p.getY());
		assertEpsilonEquals(4.709418, p.getZ());
		
		p = this.shape.getClosestPointTo(createPoint(4, 8, 9));
		assertNotNull(p);
		assertEpsilonEquals(4, p.getX());
		assertEpsilonEquals(8, p.getY());
		assertEpsilonEquals(9, p.getZ());
		
		p = this.shape.getClosestPointTo(createPoint(0, 0, 0));
		assertNotNull(p);
		assertEpsilonEquals(3.082587, p.getX());
		assertEpsilonEquals(4.93214, p.getY());
		assertEpsilonEquals(5.548657, p.getZ());

		p = this.shape.getClosestPointTo(createPoint(5,14, 0));
		assertNotNull(p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10.773501, p.getY());
		assertEpsilonEquals(4.839748, p.getZ());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(6.91741247, p.getX());
		assertEpsilonEquals(11.067859, p.getY());
		assertEpsilonEquals(12.451342, p.getZ());
		
		p = this.shape.getFarthestPointTo(createPoint(.5, .1, 0));
		assertEpsilonEquals(6.758778, p.getX());
		assertEpsilonEquals(11.087634, p.getY());
		assertEpsilonEquals(12.517557, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(6.962969, p.getX());
		assertEpsilonEquals(11.60933, p.getY());
		assertEpsilonEquals(11.849471, p.getZ());

		p = this.shape.getFarthestPointTo(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(7.770508, p.getX());
		assertEpsilonEquals(9.072455, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(3.655715, p.getX());
		assertEpsilonEquals(9.240877, p.getY());
		assertEpsilonEquals(13.653291, p.getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double d;
		d = this.shape.getDistance(createPoint(.5,.5, 0));
		assertEpsilonEquals(7.549900398,d);

		d = this.shape.getDistance(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(10.792403237,d);

		d = this.shape.getDistance(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(6.189280585,d);

		d = this.shape.getDistance(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(4.670573923,d);

		d = this.shape.getDistance(createPoint(5.2,8.2, 9.2));
		assertEpsilonEquals(0.,d);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double d;
		d = this.shape.getDistanceSquared(createPoint(.5,.5, 0));
		assertEpsilonEquals(57.00099602,d);

		d = this.shape.getDistanceSquared(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(116.47596763,d);

		d = this.shape.getDistanceSquared(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(38.30719416,d);

		d = this.shape.getDistanceSquared(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(21.81426077,d);

		d = this.shape.getDistanceSquared(createPoint(5.2,8.2, 9.2));
		assertEpsilonEquals(0.,d);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double d;
		d = this.shape.getDistanceL1(createPoint(.5,.5, 0));
		assertEpsilonEquals(12.633399,d);

		d = this.shape.getDistanceL1(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(18.178229,d);

		d = this.shape.getDistanceL1(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(9.735329,d);

		d = this.shape.getDistanceL1(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(6.761546,d);

		d = this.shape.getDistanceL1(createPoint(7.6,5.6, 9));
		assertEpsilonEquals(0.,d);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double d;
		d = this.shape.getDistanceLinf(createPoint(.5,.5, 0));
		assertEpsilonEquals(5.414314,d);

		d = this.shape.getDistanceLinf(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(7.790669,d);

		d = this.shape.getDistanceLinf(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(4.978293,d);

		d = this.shape.getDistanceLinf(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(4.346708,d);

		d = this.shape.getDistanceLinf(createPoint(7.6,5.6, 9));
		assertEpsilonEquals(0,d);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createSphere(17, 20, 0, 7));
		assertEpsilonEquals(17, this.shape.getX());
		assertEpsilonEquals(20, this.shape.getY());
		assertEpsilonEquals(0, this.shape.getZ());
		assertEpsilonEquals(7, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(123.456, -789.123, 0);
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(123.456, -789.123, 0));
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(3, box.getMinY());
		assertEpsilonEquals(4, box.getMinZ());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
		assertEpsilonEquals(14, box.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = createAlignedBox(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(3, box.getMinY());
		assertEpsilonEquals(4, box.getMinZ());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
		assertEpsilonEquals(14, box.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 10, 8, 0);
		assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 0, 7.761423749153966, 13, 0, 5, 13, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 0, 10.76142374915397, 0, 0, 8, 0);
		assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 0, 5, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 0, 10, 5.238576250846034, 0, 10, 8, 0);
		assertElement(pi, PathElementType.CLOSE, 10, 8, 0);
		assertNoElement(pi);

		Transform3D tr;
		
		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 10, 8, 0);
		assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 0, 7.761423749153966, 13, 0, 5, 13, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 0, 10.76142374915397, 0, 0, 8, 0);
		assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 0, 2.238576250846033, 3, 0, 5, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 0, 10, 5.238576250846034, 0, 10, 8, 0);
		assertElement(pi, PathElementType.CLOSE, 10, 8, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(10, -10, 0);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, -2, 0);
		assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 0, 17.761423749153966, 3, 0, 15, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 0, 10, 0.76142374915397, 0, 10, -2, 0);
		assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 0, 12.238576250846033, -7, 0, 15, -7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 0, 20, -4.761423749153966, 0, 20, -2, 0);
		assertElement(pi, PathElementType.CLOSE, 20, -2, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10, 8, 0);
		assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 0, 7.761423749153966, 13, 0, 5, 13, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 0, 10.76142374915397, 0, 0, 8, 0);
		assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 0, 2.238576250846033, 3, 0, 5, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 0, 10, 5.238576250846034, 0, 10, 8, 0);
		assertElement(pi, PathElementType.CLOSE, 10, 8, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void createTransformedShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		Shape3afp newShape;
		
		newShape = this.shape.createTransformedShape(null);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform3D();
		newShape = this.shape.createTransformedShape(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform3D();
		tr.makeTranslationMatrix(10, -10, 0);
		newShape = this.shape.createTransformedShape(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertTrue(newShape instanceof Path3afp);
		PathIterator3afp pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, -2, 0);
		assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 0, 17.761423749153966, 3, 0, 15, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 0, 10, 0.76142374915397, 0, 10, -2, 0);
		assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 0, 12.238576250846033, -7, 0, 15, -7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 0, 20, -4.761423749153966, 0, 20, -2, 0);
		assertElement(pi, PathElementType.CLOSE, 20, -2, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createAlignedBox(-4, -4, 9, 1, 1, 1)));
		assertFalse(this.shape.contains(createAlignedBox(-5, -5, 9, 10, 10, 1)));
		assertFalse(this.shape.contains(createAlignedBox(-5, -5, 9, 5.5, 5.5, 1)));
		assertFalse(this.shape.contains(createAlignedBox(-5, -4, 9, 5.5, 1, 1)));
		assertFalse(this.shape.contains(createAlignedBox(20, .5, 9, 1, 1, 1)));
		assertFalse(this.shape.contains(createAlignedBox(-5, -5, 9, 1, 1, 1)));
		assertFalse(this.shape.contains(createAlignedBox(-1, -100, 9, 1, 200, 1)));
		assertFalse(this.shape.contains(createAlignedBox(-1, -100, 9, 1.0001, 200, 1)));
		assertFalse(this.shape.contains(createAlignedBox(-1, 2, 9, 1.0001, 1.0001, 1)));
		assertTrue(this.shape.contains(createAlignedBox(2, 6, 7, 1, 1, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsAlignedBox3afp(CoordinateSystem3D cs) {
//		createSphere(5, 8, 9, 5);
//		(0, 3, 4) -> (10, 13, 14)
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
//		(-4, -4, 0) -> (-3, -4, 1)
		assertFalse(this.shape.intersects(createAlignedBox(-4, -4, 0, 1, 1, 1)));
//		(-5, -5, 0) -> (5, 5, 1)
		assertTrue(this.shape.intersects(createAlignedBox(-5, -5, 0, 10, 10, 1)));
		assertFalse(this.shape.intersects(createAlignedBox(-5, -5, 0, 5.5, 5.5, 1)));
		assertFalse(this.shape.intersects(createAlignedBox(-5, -4, 0, 5.5, 1, 1)));
		assertFalse(this.shape.intersects(createAlignedBox(20, .5, 0, 1, 1, 1)));
		assertFalse(this.shape.intersects(createAlignedBox(-5, -5, 0, 1, 1, 1)));
		assertFalse(this.shape.intersects(createAlignedBox(-1, -100, 0, 1, 200, 1)));
		assertTrue(this.shape.intersects(createAlignedBox(-1, -100, 0, 1.0001, 200, 1)));
		assertFalse(this.shape.intersects(createAlignedBox(-1, 2, 0, 1.0001, 1.0001, 1)));

	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSphere(10, 10, 9, 1)));
		assertFalse(this.shape.intersects(createSphere(0, 0, 9, 1)));
		assertFalse(this.shape.intersects(createSphere(0, .5, 9, 1)));
		assertFalse(this.shape.intersects(createSphere(.5, 0, 9, 1)));
		assertFalse(this.shape.intersects(createSphere(.5, .5, 9, 1)));
		assertFalse(this.shape.intersects(createSphere(2, 0, 9, 1)));
		assertFalse(this.shape.intersects(createSphere(12, 8, 9, 2)));
		assertTrue(this.shape.intersects(createSphere(12, 8, 9, 2.1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSegment(2, 10, 9, 6, 5, 9)));
		assertTrue(this.shape.intersects(createSegment(2, 10, 9, 8, 14, 9)));
		assertTrue(this.shape.intersects(createSegment(0, 4, 9, 8, 14, 9)));
		assertFalse(this.shape.intersects(createSegment(0, 4, 9, 0, 6, 9)));
		assertFalse(this.shape.intersects(createSegment(0, 4, 9, 0, 12, 9)));
		assertFalse(this.shape.intersects(createSegment(5, 0, 9, 0, 6, 9)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsPath3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, -2, 9);
		path.lineTo(-2, 2, 9);
		path.lineTo(2, 2, 9);
		path.lineTo(2, -2, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
		
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 8, 9);
		path.lineTo(0, 14, 9);
		path.lineTo(10, 14, 9);
		path.lineTo(10, 8, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2, 9);
		path.lineTo(12, 14, 9);
		path.lineTo(0, 14, 9);
		assertTrue(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2, 9);
		path.lineTo(-2, 14, 9);
		path.lineTo(12, 14, 9);
		path.lineTo(12, 2, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 0, 9);
		path.lineTo(0, 4, 9);
		path.lineTo(14, 0, 9);
		path.lineTo(14, 4, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-8, -7, 9);
		path.lineTo(24, 14, 9);
		path.lineTo(-16, 14, 9);
		path.lineTo(20, -7, 9);
		path.lineTo(5, 21, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		// EVENB ODD
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, -2, 9);
		path.lineTo(-2, 2, 9);
		path.lineTo(2, 2, 9);
		path.lineTo(2, -2, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 8, 9);
		path.lineTo(0, 14, 9);
		path.lineTo(10, 14, 9);
		path.lineTo(10, 8, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2, 9);
		path.lineTo(12, 14, 9);
		path.lineTo(0, 14, 9);
		assertTrue(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2, 9);
		path.lineTo(-2, 14, 9);
		path.lineTo(12, 14, 9);
		path.lineTo(12, 2, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 0, 9);
		path.lineTo(0, 4, 9);
		path.lineTo(14, 0, 9);
		path.lineTo(14, 4, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-8, -7, 9);
		path.lineTo(24, 14, 9);
		path.lineTo(-16, 14, 9);
		path.lineTo(20, -7, 9);
		path.lineTo(5, 21, 9);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsPathIterator3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, -2, 9);
		path.lineTo(-2, 2, 9);
		path.lineTo(2, 2, 9);
		path.lineTo(2, -2, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 8, 9);
		path.lineTo(0, 14, 9);
		path.lineTo(10, 14, 9);
		path.lineTo(10, 8, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2, 9);
		path.lineTo(12, 14, 9);
		path.lineTo(0, 14, 9);
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2, 9);
		path.lineTo(-2, 14, 9);
		path.lineTo(12, 14, 9);
		path.lineTo(12, 2, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 0, 9);
		path.lineTo(0, 4, 9);
		path.lineTo(14, 0, 9);
		path.lineTo(14, 4, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-8, -7, 9);
		path.lineTo(24, 14, 9);
		path.lineTo(-16, 14, 9);
		path.lineTo(20, -7, 9);
		path.lineTo(5, 21, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		// EVENB ODD
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, -2, 9);
		path.lineTo(-2, 2, 9);
		path.lineTo(2, 2, 9);
		path.lineTo(2, -2, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 8, 9);
		path.lineTo(0, 14, 9);
		path.lineTo(10, 14, 9);
		path.lineTo(10, 8, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2, 9);
		path.lineTo(12, 14, 9);
		path.lineTo(0, 14, 9);
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2, 9);
		path.lineTo(-2, 14, 9);
		path.lineTo(12, 14, 9);
		path.lineTo(12, 2, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 0, 9);
		path.lineTo(0, 4, 9);
		path.lineTo(14, 0, 9);
		path.lineTo(14, 4, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-8, -7, 9);
		path.lineTo(24, 14, 9);
		path.lineTo(-16, 14, 9);
		path.lineTo(20, -7, 9);
		path.lineTo(5, 21, 9);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
	}

	@DisplayName("containsSpherePoint(double,double,double,double,double,double,double)")
	@Test
	public void staticContainsSpherePoint() {
		assertTrue(Sphere3afp.containsSpherePoint(0, 0, 0, 1, 0, 0, 0));
		assertTrue(Sphere3afp.containsSpherePoint(0, 0, 0, 1, 1, 0, 0));
		assertTrue(Sphere3afp.containsSpherePoint(0, 0, 0, 1, 0, 1, 0));
		assertFalse(Sphere3afp.containsSpherePoint(0, 0, 0, 1, 1, 1, 0));
		assertFalse(Sphere3afp.containsSpherePoint(0, 0, 0, 1, 1.1, 0, 0));
		assertTrue(Sphere3afp.containsSpherePoint(5, 8, 0, 1, 5, 8, 0));
		assertTrue(Sphere3afp.containsSpherePoint(5, 8, 0, 1, 6, 8, 0));
		assertTrue(Sphere3afp.containsSpherePoint(5, 8, 0, 1, 5, 9, 0));
		assertFalse(Sphere3afp.containsSpherePoint(5, 8, 0, 1, 6, 9, 0));
		assertFalse(Sphere3afp.containsSpherePoint(5, 8, 0, 1, 6.1, 8, 0));
	}

	@DisplayName("containsSphereAlignedBox(double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticContainsSphereAlignedBox() {
		assertTrue(Sphere3afp.containsSphereAlignedBox(0, 0, 0, 1, 0, 0, 0, .5, .5, 0));
		assertFalse(Sphere3afp.containsSphereAlignedBox(0, 0, 0, 1, 0, 0, 0, 1, 1, 0));
		assertFalse(Sphere3afp.containsSphereAlignedBox(0, 0, 0, 1, 0, 0, 0, .5, 1, 0));
	}
	
	@DisplayName("intersectsSphereSphere(double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsSphereSphere() {
		assertFalse(Sphere3afp.intersectsSphereSphere(
				0, 0, 0, 1,
				10, 10, 0, 1));
		assertTrue(Sphere3afp.intersectsSphereSphere(
				0, 0, 0, 1,
				0, 0, 0, 1));
		assertTrue(Sphere3afp.intersectsSphereSphere(
				0, 0, 0, 1,
				0, .5, 0, 1));
		assertTrue(Sphere3afp.intersectsSphereSphere(
				0, 0, 0, 1,
				.5, 0, 0, 1));
		assertTrue(Sphere3afp.intersectsSphereSphere(
				0, 0, 0, 1,
				.5, .5, 0, 1));
		assertFalse(Sphere3afp.intersectsSphereSphere(
				0, 0, 0, 1,
				2, 0, 0, 1));
	}
	
	@DisplayName("intersectsSphereLine(double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsSphereLine() {
		assertTrue(Sphere3afp.intersectsSphereLine(
				0, 0, 0, 1,
				-5, -5, 0, -4, -4, 0));
		assertTrue(Sphere3afp.intersectsSphereLine(
				0, 0, 0, 1,
				-5, -5, 0, 5, 5, 0));
		assertTrue(Sphere3afp.intersectsSphereLine(
				0, 0, 0, 1,
				-5, -5, 0, .5, .5, 0));
		assertFalse(Sphere3afp.intersectsSphereLine(
				0, 0, 0, 1,
				-5, -5, 0, .5, -4, 0));
		assertFalse(Sphere3afp.intersectsSphereLine(
				0, 0, 0, 1,
				20, .5, 0, 21, 1.5, 0));
	}
	
	@DisplayName("intersectsSphereAlignedBox(double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsSphereAlignedBox() {
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				0, 0, 0, 1,
				-5, -5, 0, -4, -4, 0));
		assertTrue(Sphere3afp.intersectsSphereAlignedBox(
				0, 0, 0, 1,
				-5, -5, 0, 5, 5, 0));
		assertTrue(Sphere3afp.intersectsSphereAlignedBox(
				0, 0, 0, 1,
				-5, -5, 0, .5, .5, 0));
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				0, 0, 0, 1,
				-5, -5, 0, .5, -4, 0));
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				0, 0, 0, 1,
				20, .5, 0, 21, 1.5, 0));

		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				-4, -4, 0, -3, -4, 1));
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				-5, -5, 0, 5, 5, 1));
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				-5, -5, 0, 5, 5, 1));
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				-5, -5, 0, .5, .5, 1));
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				-5, -4, 0, .5, -3, 1));
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				20, .5, 0, 21, 1.5, 1));
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				-5, -5, 0, -4, -4, 1));
		assertFalse(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				-1, -100, 0, 0, 100, 1));
		assertTrue(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				-1, -100, 0, .0001, 100, 1));
		assertTrue(Sphere3afp.intersectsSphereAlignedBox(
				5, 8, 9, 5,
				-1, 2, 0, .0001, 3.0001, 1));
	}
	
	@DisplayName("intersectsSphereSegment(double,double,double,double,double,double,double,double,double,double)")
	@Test
	public void staticIntersectsSphereSegment() {
		assertFalse(Sphere3afp.intersectsSphereSegment(
				0, 0, 0, 1,
				-5, -5, 0, -4, -4, 0));
		assertTrue(Sphere3afp.intersectsSphereSegment(
				0, 0, 0, 1,
				-5, -5, 0, 5, 5, 0));
		assertTrue(Sphere3afp.intersectsSphereSegment(
				0, 0, 0, 1,
				-5, -5, 0, .5, .5, 0));
		assertFalse(Sphere3afp.intersectsSphereSegment(
				0, 0, 0, 1,
				-5, -5, 0, .5, -4, 0));
		assertFalse(Sphere3afp.intersectsSphereSegment(
				0, 0, 0, 1,
				20, .5, 0, 21, 1.5, 0));
		assertTrue(Sphere3afp.intersectsSphereSegment(
				1, 1, 0, 1,
				.5, -1, 0, .5, 4, 0));
	}
	
	@DisplayName("getX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getX());
	}
	
	@DisplayName("getY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(8, this.shape.getY());
	}

	@DisplayName("getZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(9, this.shape.getZ());
	}

	@DisplayName("getCenter")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCenter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D center = this.shape.getCenter();
		assertEpsilonEquals(5, center.getX());
		assertEpsilonEquals(8, center.getY());
		assertEpsilonEquals(9, center.getZ());
	}

	@DisplayName("setCenter(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setCenterPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setCenter(createPoint(123.456, 789.123, 0));
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(0, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@DisplayName("setCenter(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setCenterDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setCenter(123.456, 789.123, 0);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(0, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@DisplayName("setX(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setX(123.456);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}
	
	@DisplayName("setY(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    this.shape.setY(123.456);
	    assertEpsilonEquals(5, this.shape.getX());
	    assertEpsilonEquals(123.456, this.shape.getY());
	    assertEpsilonEquals(9, this.shape.getZ());
	    assertEpsilonEquals(5, this.shape.getRadius());
	}

	@DisplayName("setZ(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setZ(123.456);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(123.456, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@DisplayName("getRadius")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getRadius(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@DisplayName("setRadius(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setRadius(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setRadius(123.456);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(123.456, this.shape.getRadius());
	}

	@DisplayName("set(double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(123.456, 789.123, 1, 456.789);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(1, this.shape.getZ());
		assertEpsilonEquals(456.789, this.shape.getRadius());
	}

	@DisplayName("set(Point3D,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setPoint3DDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(createPoint(123.456, 789.123, 1), 456.789);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(1, this.shape.getZ());
		assertEpsilonEquals(456.789, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSphere(10, 10, 9, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(123.456, -789.123, 1));
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(10, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(123.456, -789.123, 1));
		assertEpsilonEquals(128.456, shape.getX());
		assertEpsilonEquals(-781.123, shape.getY());
		assertEpsilonEquals(10, shape.getZ());
		assertEpsilonEquals(5, shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(123.456, -789.123, 1));
		assertEpsilonEquals(-118.456, this.shape.getX());
		assertEpsilonEquals(797.123, this.shape.getY());
		assertEpsilonEquals(8, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(123.456, -789.123, 1));
		assertEpsilonEquals(-118.456, shape.getX());
		assertEpsilonEquals(797.123, shape.getY());
		assertEpsilonEquals(8, shape.getZ());
		assertEpsilonEquals(5, shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		Shape3afp newShape;
		
		newShape = this.shape.operator_multiply(null);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform3D();
		newShape = this.shape.operator_multiply(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform3D();
		tr.makeTranslationMatrix(10, -10, 0);
		newShape = this.shape.operator_multiply(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertInstanceOf(newShape, Sphere3afp.class);
		PathIterator3afp pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, -2, 0);
		assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 0, 17.761423749153966, 3, 0, 15, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 0, 10, 0.76142374915397, 0, 10, -2, 0);
		assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 0, 12.238576250846033, -7, 0, 15, -7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 0, 20, -4.761423749153966, 0, 20, -2, 0);
		assertElement(pi, PathElementType.CLOSE, 20, -2);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.operator_and(createPoint(0,0, 9)));
		assertFalse(this.shape.operator_and(createPoint(11,10, 9)));
		assertFalse(this.shape.operator_and(createPoint(11,50, 9)));
		assertFalse(this.shape.operator_and(createPoint(9,12, 9)));
		assertTrue(this.shape.operator_and(createPoint(9,11, 9)));
		assertTrue(this.shape.operator_and(createPoint(8,12, 9)));
		assertTrue(this.shape.operator_and(createPoint(3,7, 9)));
		assertFalse(this.shape.operator_and(createPoint(10,11, 9)));
		assertTrue(this.shape.operator_and(createPoint(9,10, 9)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSphere(10, 10, 9, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7.5499003, this.shape.operator_upTo(createPoint(.5,.5, 0)));
		assertEpsilonEquals(10.792403, this.shape.operator_upTo(createPoint(-1.2,-3.4, 0)));
		assertEpsilonEquals(6.1892805, this.shape.operator_upTo(createPoint(-1.2,5.6, 0)));
		assertEpsilonEquals(4.6705739, this.shape.operator_upTo(createPoint(7.6,5.6, 0)));
	}

	@DisplayName("setFromCenter(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCenterDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCenter(152, 148, 1, 475, -254, 11);
		assertEpsilonEquals(152, this.shape.getX());
		assertEpsilonEquals(148, this.shape.getY());
		assertEpsilonEquals(1, this.shape.getZ());
		assertEpsilonEquals(10, this.shape.getRadius());
	}

	@DisplayName("setFromCorners(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setFromCornersDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCorners(-171, 550, -9, 475, -254, 11);
		assertEpsilonEquals(152, this.shape.getX());
		assertEpsilonEquals(148, this.shape.getY());
		assertEpsilonEquals(1, this.shape.getZ());
		assertEpsilonEquals(10, this.shape.getRadius());
	}

	@DisplayName("getMinX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMinX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getMinX());
	}

	@DisplayName("setMinX(double) no coord swap")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinX_noSwap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinX(-41);
		assertEpsilonEquals(-15.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(25.5, this.shape.getRadius());
	}

	@DisplayName("setMinX(double) coord swap")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinX_swap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinX(41);
		assertEpsilonEquals(25.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(15.5, this.shape.getRadius());
	}

	@DisplayName("getMaxX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMaxX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getMaxX());
	}

	@DisplayName("setMaxX(double) no coord swap")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxX_noSwap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxX(41);
		assertEpsilonEquals(20.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(20.5, this.shape.getRadius());
	}

	@DisplayName("setMaxX(double) coord swap")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxX_swap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxX(-41);
		assertEpsilonEquals(-20.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(20.5, this.shape.getRadius());
	}

	@DisplayName("getMinY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMinY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3, this.shape.getMinY());
	}

	@DisplayName("setMinY(double) no coord swap")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinY_noSwap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinY(-41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(-14, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(27, this.shape.getRadius());
	}

	@DisplayName("setMinY(double) coord swap")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinY_swap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinY(41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(27, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(14, this.shape.getRadius());
	}

	@DisplayName("getMaxY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMaxY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getMaxY());
	}

	@DisplayName("setMaxY(double) no coord swap")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxY_noSwap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxY(41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(22, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(19, this.shape.getRadius());
	}

	@DisplayName("setMaxY(double) coord swap")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxY_swap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxY(-41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(-19, this.shape.getY());
		assertEpsilonEquals(9, this.shape.getZ());
		assertEpsilonEquals(22, this.shape.getRadius());
	}

}
