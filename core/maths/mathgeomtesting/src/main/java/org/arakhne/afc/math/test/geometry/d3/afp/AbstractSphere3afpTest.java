/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import org.junit.jupiter.api.Disabled;
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
import org.arakhne.afc.math.geometry.d3.afp.RectangularPrism3afp;
import org.arakhne.afc.math.geometry.d3.afp.Shape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;

@SuppressWarnings("all")
public abstract class AbstractSphere3afpTest<T extends Sphere3afp<?, T, ?, ?, ?, B>,
		B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>> extends AbstractShape3afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSphere(5, 8, 0, 5);
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
		assertTrue(this.shape.equals(createSphere(5, 8, 0, 5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createSphere(0, 0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equals(createSphere(5, 8, 0, 6).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 6, 10, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSphere(5, 8, 0, 5).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createSphere(0, 0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSphere(5, 8, 0, 6).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 6, 10, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSphere(5, 8, 0, 5).getPathIterator()));
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
		assertTrue(this.shape.equalsToShape((T) createSphere(5, 8, 0, 5)));
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
	public void containsDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(0, 0, 0));
		assertFalse(this.shape.contains(11, 10, 0));
		assertFalse(this.shape.contains(11, 50, 0));
		assertFalse(this.shape.contains(9, 12, 0));
		assertTrue(this.shape.contains(9,11, 0));
		assertTrue(this.shape.contains(8,12, 0));
		assertTrue(this.shape.contains(3,7, 0));
		assertFalse(this.shape.contains(10,11, 0));
		assertTrue(this.shape.contains(9,10, 0));
		
		this.shape = (T) createSphere(-1,-1,-1,1);
		assertFalse(this.shape.contains(0,0,0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createPoint(0,0, 0)));
		assertFalse(this.shape.contains(createPoint(11,10, 0)));
		assertFalse(this.shape.contains(createPoint(11,50, 0)));
		assertFalse(this.shape.contains(createPoint(9,12, 0)));
		assertTrue(this.shape.contains(createPoint(9,11, 0)));
		assertTrue(this.shape.contains(createPoint(8,12, 0)));
		assertTrue(this.shape.contains(createPoint(3,7, 0)));
		assertFalse(this.shape.contains(createPoint(10,11, 0)));
		assertTrue(this.shape.contains(createPoint(9,10, 0)));
		
		this.shape = (T) createSphere(-1,-1,0,1);
		assertFalse(this.shape.contains(createPoint(0,0, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(5,8, 0));
		assertNotNull(p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		
		p = this.shape.getClosestPointTo(createPoint(10,10, 0));
		assertNotNull(p);
		assertEpsilonEquals(9.6424, p.getX());
		assertEpsilonEquals(9.8570, p.getY());
		
		p = this.shape.getClosestPointTo(createPoint(4,8, 0));
		assertNotNull(p);
		assertEpsilonEquals(4, p.getX());
		assertEpsilonEquals(8, p.getY());
		
		p = this.shape.getClosestPointTo(createPoint(0,0, 0));
		assertNotNull(p);
		assertEpsilonEquals(2.35, p.getX());
		assertEpsilonEquals(3.76, p.getY());

		p = this.shape.getClosestPointTo(createPoint(5,14, 0));
		assertNotNull(p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(13, p.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0, 0));
		assertEpsilonEquals(7.65, p.getX());
		assertEpsilonEquals(12.24, p.getY());
		
		p = this.shape.getFarthestPointTo(createPoint(.5, .1, 0));
		assertEpsilonEquals(7.4748, p.getX());
		assertEpsilonEquals(12.3446, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(7.3889, p.getX());
		assertEpsilonEquals(12.3924, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(9.6628, p.getX());
		assertEpsilonEquals(9.8050, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(1.326, p.getX());
		assertEpsilonEquals(11.3914, p.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double d;
		d = this.shape.getDistance(createPoint(.5,.5, 0));
		assertEpsilonEquals(3.74643,d);

		d = this.shape.getDistance(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(7.9769,d);

		d = this.shape.getDistance(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(1.6483,d);

		d = this.shape.getDistance(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(0,d);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double d;
		d = this.shape.getDistanceSquared(createPoint(.5,.5, 0));
		assertEpsilonEquals(14.03572,d);

		d = this.shape.getDistanceSquared(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(63.631,d);

		d = this.shape.getDistanceSquared(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(2.7169,d);

		d = this.shape.getDistanceSquared(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(0,d);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double d;
		d = this.shape.getDistanceL1(createPoint(.5,.5, 0));
		assertEpsilonEquals(5.14005,d);

		d = this.shape.getDistanceL1(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(10.81872,d);

		d = this.shape.getDistanceL1(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(2.1322,d);

		d = this.shape.getDistanceL1(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(0,d);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double d;
		d = this.shape.getDistanceLinf(createPoint(.5,.5, 0));
		assertEpsilonEquals(3.2125,d);

		d = this.shape.getDistanceLinf(createPoint(-1.2,-3.4, 0));
		assertEpsilonEquals(7.0076,d);

		d = this.shape.getDistanceLinf(createPoint(-1.2,5.6, 0));
		assertEpsilonEquals(1.53716,d);

		d = this.shape.getDistanceLinf(createPoint(7.6,5.6, 0));
		assertEpsilonEquals(0,d);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createSphere(17, 20, 0, 7));
		assertEpsilonEquals(17, this.shape.getX());
		assertEpsilonEquals(20, this.shape.getY());
		assertEpsilonEquals(7, this.shape.getRadius());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(123.456, -789.123, 0);
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(123.456, -789.123, 0));
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(3, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = createRectangularPrism(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(3, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
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

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
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

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
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

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void containsRectangularPrism3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangularPrism(-4, -4, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-5, -5, 0, 10, 10, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-5, -5, 0, 5.5, 5.5, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-5, -4, 0, 5.5, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(20, .5, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-5, -5, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-1, -100, 0, 1, 200, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-1, -100, 0, 1.0001, 200, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-1, 2, 0, 1.0001, 1.0001, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(2, 4, 0, 6, 4, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsRectangularPrism3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createRectangularPrism(-4, -4, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(-5, -5, 0, 10, 10, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-5, -5, 0, 5.5, 5.5, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-5, -4, 0, 5.5, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(20, .5, 0, 1, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-5, -5, 0, 1, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-1, -100, 0, 1, 200, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(-1, -100, 0, 1.0001, 200, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-1, 2, 0, 1.0001, 1.0001, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSphere(10, 10, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(0, 0, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(0, .5, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(.5, 0, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(.5, .5, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(2, 0, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(12, 8, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(12, 8, 0, 2.1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSegment(2, 10, 0, 6, 5, 0)));
		assertTrue(this.shape.intersects(createSegment(2, 10, 0, 8, 14, 0)));
		assertTrue(this.shape.intersects(createSegment(0, 4, 0, 8, 14, 0)));
		assertFalse(this.shape.intersects(createSegment(0, 4, 0, 0, 6, 0)));
		assertFalse(this.shape.intersects(createSegment(0, 4, 0, 0, 12, 0)));
		assertFalse(this.shape.intersects(createSegment(5, 0, 0, 0, 6, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPath3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, -2, 0);
		path.lineTo(-2, 2, 0);
		path.lineTo(2, 2, 0);
		path.lineTo(2, -2, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
		
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 8, 0);
		path.lineTo(0, 14, 0);
		path.lineTo(10, 14, 0);
		path.lineTo(10, 8, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2, 0);
		path.lineTo(12, 14, 0);
		path.lineTo(0, 14, 0);
		assertTrue(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2, 0);
		path.lineTo(-2, 14, 0);
		path.lineTo(12, 14, 0);
		path.lineTo(12, 2, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 0, 0);
		path.lineTo(0, 4, 0);
		path.lineTo(14, 0, 0);
		path.lineTo(14, 4, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-8, -7, 0);
		path.lineTo(24, 14, 0);
		path.lineTo(-16, 14, 0);
		path.lineTo(20, -7, 0);
		path.lineTo(5, 21, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		// EVENB ODD
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, -2, 0);
		path.lineTo(-2, 2, 0);
		path.lineTo(2, 2, 0);
		path.lineTo(2, -2, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 8, 0);
		path.lineTo(0, 14, 0);
		path.lineTo(10, 14, 0);
		path.lineTo(10, 8, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2, 0);
		path.lineTo(12, 14, 0);
		path.lineTo(0, 14, 0);
		assertTrue(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2, 0);
		path.lineTo(-2, 14, 0);
		path.lineTo(12, 14, 0);
		path.lineTo(12, 2, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 0, 0);
		path.lineTo(0, 4, 0);
		path.lineTo(14, 0, 0);
		path.lineTo(14, 4, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-8, -7, 0);
		path.lineTo(24, 14, 0);
		path.lineTo(-16, 14, 0);
		path.lineTo(20, -7, 0);
		path.lineTo(5, 21, 0);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPathIterator3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, -2, 0);
		path.lineTo(-2, 2, 0);
		path.lineTo(2, 2, 0);
		path.lineTo(2, -2, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 8, 0);
		path.lineTo(0, 14, 0);
		path.lineTo(10, 14, 0);
		path.lineTo(10, 8, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2, 0);
		path.lineTo(12, 14, 0);
		path.lineTo(0, 14, 0);
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2, 0);
		path.lineTo(-2, 14, 0);
		path.lineTo(12, 14, 0);
		path.lineTo(12, 2, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 0, 0);
		path.lineTo(0, 4, 0);
		path.lineTo(14, 0, 0);
		path.lineTo(14, 4, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-8, -7, 0);
		path.lineTo(24, 14, 0);
		path.lineTo(-16, 14, 0);
		path.lineTo(20, -7, 0);
		path.lineTo(5, 21, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		// EVENB ODD
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, -2, 0);
		path.lineTo(-2, 2, 0);
		path.lineTo(2, 2, 0);
		path.lineTo(2, -2, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 8, 0);
		path.lineTo(0, 14, 0);
		path.lineTo(10, 14, 0);
		path.lineTo(10, 8, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2, 0);
		path.lineTo(12, 14, 0);
		path.lineTo(0, 14, 0);
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2, 0);
		path.lineTo(-2, 14, 0);
		path.lineTo(12, 14, 0);
		path.lineTo(12, 2, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 0, 0);
		path.lineTo(0, 4, 0);
		path.lineTo(14, 0, 0);
		path.lineTo(14, 4, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-8, -7, 0);
		path.lineTo(24, 14, 0);
		path.lineTo(-16, 14, 0);
		path.lineTo(20, -7, 0);
		path.lineTo(5, 21, 0);
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator3afp) path.getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticContainsCirclePoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticContainsCircleRectangle(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Sphere3afp.containsSphereRectangularPrism(0, 0, 0, 1, 0, 0, 0, .5, .5, 0));
		assertFalse(Sphere3afp.containsSphereRectangularPrism(0, 0, 0, 1, 0, 0, 0, 1, 1, 0));
		assertFalse(Sphere3afp.containsSphereRectangularPrism(0, 0, 0, 1, 0, 0, 0, .5, 1, 0));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsCircleCircle(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsCircleLine(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsCircleRectangle(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(Sphere3afp.intersectsSpherePrism(
				0, 0, 0, 1,
				-5, -5, 0, -4, -4, 0));
		assertTrue(Sphere3afp.intersectsSpherePrism(
				0, 0, 0, 1,
				-5, -5, 0, 5, 5, 0));
		assertTrue(Sphere3afp.intersectsSpherePrism(
				0, 0, 0, 1,
				-5, -5, 0, .5, .5, 0));
		assertFalse(Sphere3afp.intersectsSpherePrism(
				0, 0, 0, 1,
				-5, -5, 0, .5, -4, 0));
		assertFalse(Sphere3afp.intersectsSpherePrism(
				0, 0, 0, 1,
				20, .5, 0, 21, 1.5, 0));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsCircleSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getX());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(8, this.shape.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCenter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D center = this.shape.getCenter();
		assertEpsilonEquals(5, center.getX());
		assertEpsilonEquals(8, center.getY());
		assertEpsilonEquals(0, center.getZ());
	}

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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setCenterDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setCenter(123.456, 789.123, 0);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(0, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setX(123.456);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(0, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    this.shape.setY(123.456);
	    assertEpsilonEquals(5, this.shape.getX());
	    assertEpsilonEquals(123.456, this.shape.getY());
	    assertEpsilonEquals(0, this.shape.getZ());
	    assertEpsilonEquals(5, this.shape.getRadius());
	}

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

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getRadius(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setRadius(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setRadius(123.456);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(0, this.shape.getZ());
		assertEpsilonEquals(123.456, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(123.456, 789.123, 1, 456.789);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(1, this.shape.getZ());
		assertEpsilonEquals(456.789, this.shape.getRadius());
	}

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

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSphere(10, 10, 0, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(123.456, -789.123, 1));
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(1, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(123.456, -789.123, 1));
		assertEpsilonEquals(128.456, shape.getX());
		assertEpsilonEquals(-781.123, shape.getY());
		assertEpsilonEquals(1, shape.getZ());
		assertEpsilonEquals(5, shape.getRadius());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(123.456, -789.123, 1));
		assertEpsilonEquals(-118.456, this.shape.getX());
		assertEpsilonEquals(797.123, this.shape.getY());
		assertEpsilonEquals(1, this.shape.getZ());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(123.456, -789.123, 1));
		assertEpsilonEquals(-118.456, shape.getX());
		assertEpsilonEquals(797.123, shape.getY());
		assertEpsilonEquals(1, shape.getZ());
		assertEpsilonEquals(5, shape.getRadius());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
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

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.operator_and(createPoint(0,0, 0)));
		assertFalse(this.shape.operator_and(createPoint(11,10, 0)));
		assertFalse(this.shape.operator_and(createPoint(11,50, 0)));
		assertFalse(this.shape.operator_and(createPoint(9,12, 0)));
		assertTrue(this.shape.operator_and(createPoint(9,11, 0)));
		assertTrue(this.shape.operator_and(createPoint(8,12, 0)));
		assertTrue(this.shape.operator_and(createPoint(3,7, 0)));
		assertFalse(this.shape.operator_and(createPoint(10,11, 0)));
		assertTrue(this.shape.operator_and(createPoint(9,10, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSphere(10, 10, 0, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3.74643, this.shape.operator_upTo(createPoint(.5,.5, 0)));
		assertEpsilonEquals(7.9769, this.shape.operator_upTo(createPoint(-1.2,-3.4, 0)));
		assertEpsilonEquals(1.6483, this.shape.operator_upTo(createPoint(-1.2,5.6, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(7.6,5.6, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getHorizontalRadius(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getRadius());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getVerticalRadius(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void setFromCenterDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCenter(152, 148, 1, 475, -254, 11);
		assertEpsilonEquals(152, this.shape.getX());
		assertEpsilonEquals(148, this.shape.getY());
		assertEpsilonEquals(1, this.shape.getZ());
		assertEpsilonEquals(323, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void setFromCornersDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setFromCorners(-171, 550, -9, 475, -254, 11);
		assertEpsilonEquals(152, this.shape.getX());
		assertEpsilonEquals(148, this.shape.getY());
		assertEpsilonEquals(1, this.shape.getZ());
		assertEpsilonEquals(323, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMinX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getMinX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinX_noSwap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinX(-41);
		assertEpsilonEquals(-15.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(25.5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinX_swap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinX(41);
		assertEpsilonEquals(25.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(15.5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMaxX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getMaxX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxX_noSwap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxX(41);
		assertEpsilonEquals(20.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(20.5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxX_swap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxX(-41);
		assertEpsilonEquals(-20.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(20.5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMinY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3, this.shape.getMinY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinY_noSwap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinY(-41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(-14, this.shape.getY());
		assertEpsilonEquals(27, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMinY_swap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMinY(41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(27, this.shape.getY());
		assertEpsilonEquals(14, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getMaxY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxY_noSwap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxY(41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(22, this.shape.getY());
		assertEpsilonEquals(19, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setMaxY_swap(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setMaxY(-41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(-19, this.shape.getY());
		assertEpsilonEquals(22, this.shape.getRadius());
	}

}
