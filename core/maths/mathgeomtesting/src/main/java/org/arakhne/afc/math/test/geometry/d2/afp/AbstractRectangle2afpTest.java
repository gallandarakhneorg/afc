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

package org.arakhne.afc.math.test.geometry.d2.afp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;

@SuppressWarnings("all")
public abstract class AbstractRectangle2afpTest<T extends Rectangle2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractRectangularShape2afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createRectangle(5, 8, 5, 10);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsRectangleRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1));
		assertFalse(Rectangle2afp.intersectsRectangleRectangle(0, 0, 1, 1, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 20, 1, 22));
		assertFalse(Rectangle2afp.intersectsRectangleRectangle(0, 20, 1, 22, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 5, 100));
		assertFalse(Rectangle2afp.intersectsRectangleRectangle(0, 0, 5, 100, 5, 8, 10, 18));

		assertTrue(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 0, 0, 5.1, 100));
		assertTrue(Rectangle2afp.intersectsRectangleRectangle(0, 0, 5.1, 100, 5, 8, 10, 18));

		assertTrue(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		assertTrue(Rectangle2afp.intersectsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));

		assertTrue(Rectangle2afp.intersectsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		assertTrue(Rectangle2afp.intersectsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsRectangleLine(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 45, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 55, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 0, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 45, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 45, 60, 15));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 5, 45, 30, 55));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 40, 55, 60, 15));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 40, 0, 60, 40));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 40, 20, 0));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 45, 100, 15));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 100, 43, 0));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 100, 43, 101));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 100, 45, 102, 15));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 20, 0, 43, -2));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 50, 49, -100, 45, -48, 15));
		assertFalse(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, -100, 60, -98, 61));
		assertTrue(Rectangle2afp.intersectsRectangleLine(10, 12, 40, 37, 0, 30, 9, 21));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsRectangleSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 45, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 55, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 0, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 45, 43, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 45, 60, 15));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 5, 45, 30, 55));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 40, 55, 60, 15));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 40, 0, 60, 40));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 40, 20, 0));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 45, 100, 15));
		assertTrue(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 100, 43, 0));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 100, 43, 101));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 100, 45, 102, 15));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 20, 0, 43, -2));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 50, 49, -100, 45, -48, 15));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, -100, 60, -98, 61));
		assertFalse(Rectangle2afp.intersectsRectangleSegment(10, 12, 40, 37, 0, 30, 9, 21));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticReducesCohenSutherlandZoneRectangleSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p1 = createPoint(0, 0);
		Point2D p2 = createPoint(0, 0);
		
		assertEquals(0,
				Rectangle2afp.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 45, 43, 15,
				MathUtil.getCohenSutherlandCode(20, 45, 0, 12, 40, 37),
				MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
				p1, p2));
		assertFpPointEquals(26.13333, 37, p1);
		assertFpPointEquals(40, 18.91304, p2);

		assertEquals(0, 
				Rectangle2afp.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 55, 43, 15,
				MathUtil.getCohenSutherlandCode(20, 55, 0, 12, 40, 37),
				MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
				p1, p2));
		assertFpPointEquals(30.35, 37, p1);
		assertFpPointEquals(40, 20.21739, p2);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsRectangleRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1));
		assertFalse(Rectangle2afp.containsRectangleRectangle(0, 0, 1, 1, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 20, 1, 22));
		assertFalse(Rectangle2afp.containsRectangleRectangle(0, 20, 1, 22, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 5, 100));
		assertFalse(Rectangle2afp.containsRectangleRectangle(0, 0, 5, 100, 5, 8, 10, 18));

		assertFalse(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 0, 0, 5.1, 100));
		assertFalse(Rectangle2afp.containsRectangleRectangle(0, 0, 5.1, 100, 5, 8, 10, 18));

		assertTrue(Rectangle2afp.containsRectangleRectangle(5, 8, 10, 18, 6, 9, 9.5, 15));
		assertFalse(Rectangle2afp.containsRectangleRectangle(6, 9, 9.5, 15, 5, 8, 10, 18));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsRectanglePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 55));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 0));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 0, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 5, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 55));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 0));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 0, 40));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 20, 100));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 100, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 50, 49, -100, 45));
		assertFalse(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, -100, 60));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 10, 12));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 12));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 40, 37));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 10, 37));
		assertTrue(Rectangle2afp.containsRectanglePoint(10, 12, 40, 37, 35, 24));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createRectangle(0, 8, 5, 12)));
		assertFalse(this.shape.equals(createRectangle(5, 8, 5, 0)));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createRectangle(5, 8, 5, 10)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createRectangle(0, 8, 5, 12).getPathIterator()));
		assertFalse(this.shape.equals(createRectangle(5, 8, 5, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createRectangle(5, 8, 5, 10).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createRectangle(0, 8, 5, 12).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createRectangle(5, 8, 5, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createRectangle(5, 8, 5, 10).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createRectangle(0, 8, 5, 12)));
		assertFalse(this.shape.equalsToShape((T) createRectangle(5, 8, 5, 0)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createRectangle(5, 8, 5, 10)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.add(createPoint(123.456, 456.789));
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());

		this.shape.add(createPoint(-123.456, 456.789));
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());

		this.shape.add(createPoint(-123.456, -456.789));
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(-456.789, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.add(123.456, 456.789);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());

		this.shape.add(-123.456, 456.789);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());

		this.shape.add(-123.456, -456.789);
		assertEpsilonEquals(-123.456, this.shape.getMinX());
		assertEpsilonEquals(-456.789, this.shape.getMinY());
		assertEpsilonEquals(123.456, this.shape.getMaxX());
		assertEpsilonEquals(456.789, this.shape.getMaxY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setUnion(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setUnion(createRectangle(0, 0, 12, 1));
		assertEpsilonEquals(0, this.shape.getMinX());
		assertEpsilonEquals(0, this.shape.getMinY());
		assertEpsilonEquals(12, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createUnion(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B union = this.shape.createUnion(createRectangle(0, 0, 12, 1));
		assertNotSame(this.shape, union);
		assertEpsilonEquals(0, union.getMinX());
		assertEpsilonEquals(0, union.getMinY());
		assertEpsilonEquals(12, union.getMaxX());
		assertEpsilonEquals(18, union.getMaxY());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIntersection_noIntersection(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setIntersection(createRectangle(0, 0, 12, 1));
		assertTrue(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIntersection_intersection(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setIntersection(createRectangle(0, 0, 7, 10));
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(7, this.shape.getMaxX());
		assertEpsilonEquals(10, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createIntersection_noIntersection(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.createIntersection(createRectangle(0, 0, 12, 1));
		assertNotSame(this.shape, box);
		assertTrue(box.isEmpty());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createIntersection_intersection(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.createIntersection(createRectangle(0, 0, 7, 10));
		assertNotSame(this.shape, box);
		assertEpsilonEquals(5, box.getMinX());
		assertEpsilonEquals(8, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(10, box.getMaxY());
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void avoidCollisionWithRectangle2afpVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector2D v = createVector(Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, v);
		
		assertEpsilonEquals(2, v.getX());
		assertEpsilonEquals(0, v.getY());
		assertEpsilonEquals(7, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(12, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void avoidCollisionWithRectangle2afpVector2DVector2D_nullDisplacement(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector2D v = createVector(Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, null, v);
		
		assertEpsilonEquals(2, v.getX());
		assertEpsilonEquals(0, v.getY());
		assertEpsilonEquals(7, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(12, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void avoidCollisionWithRectangle2afpVector2DVector2D_noDisplacement(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector2D v1 = createVector(0, 0);
		Vector2D v2 = createVector(Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, v1, v2);
		
		assertEpsilonEquals(2, v2.getX());
		assertEpsilonEquals(0, v2.getY());
		assertEpsilonEquals(7, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(12, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void avoidCollisionWithRectangle2afpVector2DVector2D_givenDisplacement(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B r = createRectangle(0, 0, 7, 10);
		assertTrue(this.shape.intersects(r));
		assertTrue(r.intersects(this.shape));

		Vector2D v1 = createVector(-4, 4);
		Vector2D v2 = createVector(Double.NaN, Double.NaN);
		this.shape.avoidCollisionWith(r, v1, v2);
		
		assertEpsilonEquals(-2, v1.getX());
		assertEpsilonEquals(2, v1.getY());
		assertEpsilonEquals(-2, v2.getX());
		assertEpsilonEquals(2, v2.getY());
		assertEpsilonEquals(3, this.shape.getMinX());
		assertEpsilonEquals(10, this.shape.getMinY());
		assertEpsilonEquals(8, this.shape.getMaxX());
		assertEpsilonEquals(20, this.shape.getMaxY());
		assertFalse(this.shape.intersects(r));
		assertFalse(r.intersects(this.shape));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointRectanglePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 0, 0, p);
		assertFpPointEquals(5, 8, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 100, 0, p);
		assertFpPointEquals(10, 8, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 100, 100, p);
		assertFpPointEquals(10, 18, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 0, 100, p);
		assertFpPointEquals(5, 18, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 0, 10, p);
		assertFpPointEquals(5, 10, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 7, 0, p);
		assertFpPointEquals(7, 8, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 154, 17, p);
		assertFpPointEquals(10, 17, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 9, 154, p);
		assertFpPointEquals(9, 18, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 8, 18, p);
		assertFpPointEquals(8, 18, p);

		p = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectanglePoint(5, 8, 10, 18, 7, 12, p);
		assertFpPointEquals(7, 12, p);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = this.shape.getClosestPointTo(createPoint(0, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getClosestPointTo(createPoint(100, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getClosestPointTo(createPoint(100, 100));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getClosestPointTo(createPoint(0, 100));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getClosestPointTo(createPoint(0, 10));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(10, p.getY());

		p = this.shape.getClosestPointTo(createPoint(7, 0));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getClosestPointTo(createPoint(154, 17));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(17, p.getY());

		p = this.shape.getClosestPointTo(createPoint(9, 154));
		assertEpsilonEquals(9, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getClosestPointTo(createPoint(8, 18));
		assertEpsilonEquals(8, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getClosestPointTo(createPoint(7, 12));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(12, p.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFarthestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(100, 0));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(100, 100));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(0, 100));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(0, 10));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7, 0));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(154, 17));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(9, 154));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(8, 18));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7, 12));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(18, p.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistance(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(9.43398, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(90.35486, this.shape.getDistance(createPoint(100, 0)));
		assertEpsilonEquals(121.75385, this.shape.getDistance(createPoint(100, 100)));
		assertEpsilonEquals(82.1523, this.shape.getDistance(createPoint(0, 100)));
		assertEpsilonEquals(5, this.shape.getDistance(createPoint(0, 10)));
		assertEpsilonEquals(8, this.shape.getDistance(createPoint(7, 0)));
		assertEpsilonEquals(144, this.shape.getDistance(createPoint(154, 17)));
		assertEpsilonEquals(136, this.shape.getDistance(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(7, 12)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDistanceSquaredRectanglePoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(88.99998, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 0, 0));
		assertEpsilonEquals(8164, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 100, 0));
		assertEpsilonEquals(14823.99999, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 100, 100));
		assertEpsilonEquals(6749, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 0, 100));
		assertEpsilonEquals(25, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 0, 10));
		assertEpsilonEquals(64, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 7, 0));
		assertEpsilonEquals(20736, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 154, 17));
		assertEpsilonEquals(18496, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 9, 154));
		assertEpsilonEquals(0, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 8, 18));
		assertEpsilonEquals(0, Rectangle2afp.calculatesDistanceSquaredRectanglePoint(5, 8, 10, 18, 7, 12));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(88.99998, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(8164, this.shape.getDistanceSquared(createPoint(100, 0)));
		assertEpsilonEquals(14823.99999, this.shape.getDistanceSquared(createPoint(100, 100)));
		assertEpsilonEquals(6749, this.shape.getDistanceSquared(createPoint(0, 100)));
		assertEpsilonEquals(25, this.shape.getDistanceSquared(createPoint(0, 10)));
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createPoint(7, 0)));
		assertEpsilonEquals(20736, this.shape.getDistanceSquared(createPoint(154, 17)));
		assertEpsilonEquals(18496, this.shape.getDistanceSquared(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(7, 12)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceL1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(98, this.shape.getDistanceL1(createPoint(100, 0)));
		assertEpsilonEquals(172, this.shape.getDistanceL1(createPoint(100, 100)));
		assertEpsilonEquals(87, this.shape.getDistanceL1(createPoint(0, 100)));
		assertEpsilonEquals(5, this.shape.getDistanceL1(createPoint(0, 10)));
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(7, 0)));
		assertEpsilonEquals(144, this.shape.getDistanceL1(createPoint(154, 17)));
		assertEpsilonEquals(136, this.shape.getDistanceL1(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(7, 12)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceLinf(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(90, this.shape.getDistanceLinf(createPoint(100, 0)));
		assertEpsilonEquals(90, this.shape.getDistanceLinf(createPoint(100, 100)));
		assertEpsilonEquals(82, this.shape.getDistanceLinf(createPoint(0, 100)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(0, 10)));
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(7, 0)));
		assertEpsilonEquals(144, this.shape.getDistanceLinf(createPoint(154, 17)));
		assertEpsilonEquals(136, this.shape.getDistanceLinf(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(7, 12)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createRectangle(123.456, 456.789, 789.123, 159.753));
		assertEpsilonEquals(123.456, this.shape.getMinX());
		assertEpsilonEquals(456.789, this.shape.getMinY());
		assertEpsilonEquals(912.579, this.shape.getMaxX());
		assertEpsilonEquals(616.542, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 18);
		assertElement(pi, PathElementType.LINE_TO, 5, 18);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi;
		
		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 18);
		assertElement(pi, PathElementType.LINE_TO, 5, 18);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertNoElement(pi);

		pi = this.shape.getPathIterator(new Transform2D());
		assertElement(pi, PathElementType.MOVE_TO, 5, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 8);
		assertElement(pi, PathElementType.LINE_TO, 10, 18);
		assertElement(pi, PathElementType.LINE_TO, 5, 18);
		assertElement(pi, PathElementType.CLOSE, 5, 8);
		assertNoElement(pi);

		Transform2D tr;
		tr = new Transform2D();
		tr.setTranslation(123.456, 456.789);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createTransformedShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr;
		tr = new Transform2D();
		tr.setTranslation(123.456, 456.789);
		PathIterator2afp pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangle(0, 0, 1, 1)));
		assertFalse(createRectangle(0, 0, 1, 1).contains(this.shape));

		assertFalse(this.shape.contains(createRectangle(0, 20, 1, 2)));
		assertFalse(createRectangle(0, 20, 1, 2).contains(this.shape));

		assertFalse(this.shape.contains(createRectangle(0, 0, 5, 100)));
		assertFalse(createRectangle(0, 0, 5, 100).contains(this.shape));

		assertFalse(this.shape.contains(createRectangle(0, 0, 5.1, 100)));
		assertFalse(createRectangle(0, 0, 5.1, 100).contains(this.shape));

		assertTrue(this.shape.contains(createRectangle(6, 9, .5, 9)));
		assertFalse(createRectangle(6, 9, .5, 9).contains(this.shape));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createCircle(0, 0, 1)));
		assertFalse(this.shape.contains(createCircle(0, 20, 1)));
		assertFalse(this.shape.contains(createCircle(0, 0, 5)));
		assertFalse(this.shape.contains(createCircle(0, 0, 5.1)));
		assertTrue(this.shape.contains(createCircle(6, 9, .5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createRectangle(0, 0, 1, 1)));
		assertFalse(createRectangle(0, 0, 1, 1).intersects(this.shape));

		assertFalse(this.shape.intersects(createRectangle(0, 20, 1, 2)));
		assertFalse(createRectangle(0, 20, 1, 2).intersects(this.shape));

		assertFalse(this.shape.intersects(createRectangle(0, 0, 5, 100)));
		assertFalse(createRectangle(0, 0, 5, 100).intersects(this.shape));

		assertTrue(this.shape.intersects(createRectangle(0, 0, 5.1, 100)));
		assertTrue(createRectangle(0, 0, 5.1, 100).intersects(this.shape));

		assertTrue(this.shape.intersects(createRectangle(6, 9, .5, 9)));
		assertTrue(createRectangle(6, 9, .5, 9).intersects(this.shape));

		assertTrue(this.shape.intersects(createRectangle(0, 0, 5.1, 8.1)));
		assertTrue(createRectangle(0, 0, 5.1, 8.1).intersects(this.shape));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
		assertFalse(createRectangle(-6, -2, 1, 1).intersects(triangle));
		assertFalse(createRectangle(-6, 6, 1, 1).intersects(triangle));
		assertFalse(createRectangle(6, 6, 1, 1).intersects(triangle));
		assertFalse(createRectangle(-16, 0, 1, 1).intersects(triangle));
		assertFalse(createRectangle(12, 12, 1, 1).intersects(triangle));
		assertFalse(createRectangle(0, -6, 1, 1).intersects(triangle));
		assertTrue(createRectangle(-4, 2, 1, 1).intersects(triangle));
		assertTrue(createRectangle(-4, 4, 1, 1).intersects(triangle));
		assertTrue(createRectangle(0, 6, 1, 1).intersects(triangle));
		assertTrue(createRectangle(2, 4, 1, 1).intersects(triangle));
		assertFalse(createRectangle(5, 8, 1, 1).intersects(triangle));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(createRectangle(0, 0, .5, .5).intersects(createCircle(0, 0, 1)));
		assertTrue(createRectangle(0, 0, 1, 1).intersects(createCircle(0, 0, 1)));
		assertTrue(createRectangle(0, 0, .5, 1).intersects(createCircle(0, 0, 1)));
		assertFalse(createRectangle(0, 0, 1, 1).intersects(createCircle(2, 0, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(createRectangle(0, 0, 1, 1).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -5, 1, 1).intersects(createEllipse(0, 0, 1, 1)));
		assertTrue(createRectangle(.5, .5, 5, 5).intersects(createEllipse(0, 0, 1, 1)));
		assertTrue(createRectangle(.5, .5, 5, .6).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -5, 5, 5).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-9, -9, 4, 4).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -9, 4, 4).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(5, -5, 6, 5).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -5, 0, 0).intersects(createEllipse(0, 0, 1, 1)));
		assertFalse(createRectangle(-5, -5, .1, .1).intersects(createEllipse(0, 0, 1, 1)));
		assertTrue(createRectangle(.25, .25, .5, .5).intersects(createEllipse(0, 0, 1, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 45, 43, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 55, 43, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 0, 43, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(0, 45, 43, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 45, 60, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(5, 45, 30, 55)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(40, 55, 60, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(40, 0, 60, 40)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(0, 40, 20, 0)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(0, 45, 100, 15)));
		assertTrue(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 100, 43, 0)));
		assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 100, 43, 101)));
		assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(100, 45, 102, 15)));
		assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(20, 0, 43, -2)));
		assertFalse(createRectangle(10, 12, 50, 49).intersects(createSegment(-100, 45, -48, 15)));
		assertFalse(createRectangle(10, 12, 40, 37).intersects(createSegment(-100, 60, -98, 61)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp p;
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(5, 8);
		p.lineTo(-20, 20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(20, 20);
		p.lineTo(-20, 20);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(10, 8);
		p.lineTo(20, 18);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(20, 18);
		p.lineTo(10, 8);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPathIterator2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp<?, ?, ?, ?, ?, B> p;
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(5, 8);
		p.lineTo(-20, 20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(20, 20);
		p.lineTo(-20, 20);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(10, 8);
		p.lineTo(20, 18);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(20, 18);
		p.lineTo(10, 8);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		OrientedRectangle2afp obr = createOrientedRectangle(
				6, 9,
				0.894427190999916, -0.447213595499958, 13.999990000000002,
				12.999989999999997);
		assertFalse(createRectangle(0, -5, 2, 1).intersects(obr));
		assertTrue(createRectangle(0, -4.5, 2, 1).intersects(obr));
		assertTrue(createRectangle(0, -4, 2, 1).intersects(obr));
		assertTrue(createRectangle(4, 4, 2, 1).intersects(obr));
		assertFalse(createRectangle(20, -2, 2, 1).intersects(obr));
		assertTrue(createRectangle(-15, -10, 50, 50).intersects(obr));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void intersectsParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Parallelogram2afp para = createParallelogram(
				6, 9, 2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
				-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
		assertFalse(createRectangle(0, 0, 1, 1).intersects(para));
		assertTrue(createRectangle(0, 2, 1, 1).intersects(para));
		assertTrue(createRectangle(-5.5, 8.5, 1, 1).intersects(para));
		assertFalse(createRectangle(-6, 16, 1, 1).intersects(para));
		assertFalse(createRectangle(146, 16, 1, 1).intersects(para));
		assertTrue(createRectangle(12, 14, 1, 1).intersects(para));
		assertTrue(createRectangle(0, 8, 1, 1).intersects(para));
		assertTrue(createRectangle(10, -1, 1, 1).intersects(para));
		assertTrue(createRectangle(-15, -10, 35, 40).intersects(para));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 1, 1, .1, .2)));
		assertFalse(createRoundRectangle(0, 0, 1, 1, .1, .2).intersects(this.shape));

		assertFalse(this.shape.intersects(createRoundRectangle(0, 20, 1, 2, .1, .2)));
		assertFalse(createRoundRectangle(0, 20, 1, 2, .1, .2).intersects(this.shape));

		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 5, 100, .1, .2)));
		assertFalse(createRoundRectangle(0, 0, 5, 100, .1, .2).intersects(this.shape));

		assertTrue(this.shape.intersects(createRoundRectangle(0, 0, 5.1, 100, .1, .2)));
		assertTrue(createRoundRectangle(0, 0, 5.1, 100, .1, .2).intersects(this.shape));

		assertTrue(this.shape.intersects(createRoundRectangle(6, 9, .5, 9, .1, .2)));
		assertTrue(createRoundRectangle(6, 9, .5, 9, .1, .2).intersects(this.shape));

		assertTrue(this.shape.intersects(createRoundRectangle(0, 0, 5.1, 8.1, .1, .2)));
		assertTrue(createRoundRectangle(0, 0, 5.1, 8.1, .1, .2).intersects(this.shape));

		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 5.01, 8.01, .1, .2)));
		assertFalse(createRoundRectangle(0, 0, 5.01, 8.01, .1, .2).intersects(this.shape));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(10, 12, 30, 25);
		assertFalse(this.shape.contains(20, 45));
		assertFalse(this.shape.contains(20, 55));
		assertFalse(this.shape.contains(20, 0));
		assertFalse(this.shape.contains(0, 45));
		assertFalse(this.shape.contains(5, 45));
		assertFalse(this.shape.contains(40, 55));
		assertFalse(this.shape.contains(40, 0));
		assertFalse(this.shape.contains(0, 40));
		assertFalse(this.shape.contains(20, 100));
		assertFalse(this.shape.contains(100, 45));
		assertFalse(this.shape.contains(-100, 45));
		assertFalse(this.shape.contains(-100, 60));
		assertTrue(this.shape.contains(10, 12));
		assertTrue(this.shape.contains(40, 12));
		assertTrue(this.shape.contains(40, 37));
		assertTrue(this.shape.contains(10, 37));
		assertTrue(this.shape.contains(35, 24));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(10, 12, 30, 25);
		assertFalse(this.shape.contains(createPoint(20, 45)));
		assertFalse(this.shape.contains(createPoint(20, 55)));
		assertFalse(this.shape.contains(createPoint(20, 0)));
		assertFalse(this.shape.contains(createPoint(0, 45)));
		assertFalse(this.shape.contains(createPoint(5, 45)));
		assertFalse(this.shape.contains(createPoint(40, 55)));
		assertFalse(this.shape.contains(createPoint(40, 0)));
		assertFalse(this.shape.contains(createPoint(0, 40)));
		assertFalse(this.shape.contains(createPoint(20, 100)));
		assertFalse(this.shape.contains(createPoint(100, 45)));
		assertFalse(this.shape.contains(createPoint(-100, 45)));
		assertFalse(this.shape.contains(createPoint(-100, 60)));
		assertTrue(this.shape.contains(createPoint(10, 12)));
		assertTrue(this.shape.contains(createPoint(40, 12)));
		assertTrue(this.shape.contains(createPoint(40, 37)));
		assertTrue(this.shape.contains(createPoint(10, 37)));
		assertTrue(this.shape.contains(createPoint(35, 24)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void inflate(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.inflate(1, 2, 3, 4);
		assertEpsilonEquals(4, this.shape.getMinX());
		assertEpsilonEquals(6, this.shape.getMinY());
		assertEpsilonEquals(13, this.shape.getMaxX());
		assertEpsilonEquals(22, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(createRectangle(0, 0, 5.1, 100).intersects((Shape2D) this.shape));
		assertTrue(createRectangle(.25, .25, .5, .5).intersects((Shape2D) createEllipse(0, 0, 1, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(123.456, 456.789));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_plus(createVector(123.456, 456.789));
		assertEpsilonEquals(128.456, shape.getMinX());
		assertEpsilonEquals(464.789, shape.getMinY());
		assertEpsilonEquals(133.456, shape.getMaxX());
		assertEpsilonEquals(474.789, shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(123.456, 456.789));
		assertEpsilonEquals(-118.456, this.shape.getMinX());
		assertEpsilonEquals(-448.789, this.shape.getMinY());
		assertEpsilonEquals(-113.456, this.shape.getMaxX());
		assertEpsilonEquals(-438.789, this.shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T shape = this.shape.operator_minus(createVector(123.456, 456.789));
		assertEpsilonEquals(-118.456, shape.getMinX());
		assertEpsilonEquals(-448.789, shape.getMinY());
		assertEpsilonEquals(-113.456, shape.getMaxX());
		assertEpsilonEquals(-438.789, shape.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr;
		tr = new Transform2D();
		tr.setTranslation(123.456, 456.789);
		PathIterator2afp pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 128.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 464.789);
		assertElement(pi, PathElementType.LINE_TO, 133.456, 474.789);
		assertElement(pi, PathElementType.LINE_TO, 128.456, 474.789);
		assertElement(pi, PathElementType.CLOSE, 128.456, 464.789);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(10, 12, 30, 25);
		assertFalse(this.shape.operator_and(createPoint(20, 45)));
		assertFalse(this.shape.operator_and(createPoint(20, 55)));
		assertFalse(this.shape.operator_and(createPoint(20, 0)));
		assertFalse(this.shape.operator_and(createPoint(0, 45)));
		assertFalse(this.shape.operator_and(createPoint(5, 45)));
		assertFalse(this.shape.operator_and(createPoint(40, 55)));
		assertFalse(this.shape.operator_and(createPoint(40, 0)));
		assertFalse(this.shape.operator_and(createPoint(0, 40)));
		assertFalse(this.shape.operator_and(createPoint(20, 100)));
		assertFalse(this.shape.operator_and(createPoint(100, 45)));
		assertFalse(this.shape.operator_and(createPoint(-100, 45)));
		assertFalse(this.shape.operator_and(createPoint(-100, 60)));
		assertTrue(this.shape.operator_and(createPoint(10, 12)));
		assertTrue(this.shape.operator_and(createPoint(40, 12)));
		assertTrue(this.shape.operator_and(createPoint(40, 37)));
		assertTrue(this.shape.operator_and(createPoint(10, 37)));
		assertTrue(this.shape.operator_and(createPoint(35, 24)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(createRectangle(0, 0, 5.1, 100).operator_and(this.shape));
		assertTrue(createRectangle(.25, .25, .5, .5).operator_and(createEllipse(0, 0, 1, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(9.43398, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(90.35486, this.shape.operator_upTo(createPoint(100, 0)));
		assertEpsilonEquals(121.75385, this.shape.operator_upTo(createPoint(100, 100)));
		assertEpsilonEquals(82.1523, this.shape.operator_upTo(createPoint(0, 100)));
		assertEpsilonEquals(5, this.shape.operator_upTo(createPoint(0, 10)));
		assertEpsilonEquals(8, this.shape.operator_upTo(createPoint(7, 0)));
		assertEpsilonEquals(144, this.shape.operator_upTo(createPoint(154, 17)));
		assertEpsilonEquals(136, this.shape.operator_upTo(createPoint(9, 154)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(8, 18)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(7, 12)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCenter(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p = this.shape.getCenter();
		assertNotNull(p);
		assertEpsilonEquals(7.5, p.getX());
		assertEpsilonEquals(13, p.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCenterX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7.5, this.shape.getCenterX());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCenterY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(13, this.shape.getCenterY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setCenterDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setCenter(145,  -47);
		assertEpsilonEquals(142.5, this.shape.getMinX());
		assertEpsilonEquals(-52, this.shape.getMinY());
		assertEpsilonEquals(147.5, this.shape.getMaxX());
		assertEpsilonEquals(-42, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setCenterXDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setCenterX(145);
		assertEpsilonEquals(142.5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(147.5, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setCenterYDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setCenterY(-47);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(-52, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(-42, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstAxis(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D v = this.shape.getFirstAxis();
		assertNotNull(v);
		assertEpsilonEquals(1, v.getX());
		assertEpsilonEquals(0, v.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstAxisX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getFirstAxisX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstAxisY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getFirstAxisY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getSecondAxis(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D v = this.shape.getSecondAxis();
		assertNotNull(v);
		assertEpsilonEquals(0, v.getX());
		assertEpsilonEquals(1, v.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getSecondAxisX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getSecondAxisX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getSecondAxisY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(1, this.shape.getSecondAxisY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFirstAxisExtent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2.5, this.shape.getFirstAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFirstAxisExtent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setFirstAxisExtent(124);
		assertEpsilonEquals(-116.5, this.shape.getMinX());
		assertEpsilonEquals(8, this.shape.getMinY());
		assertEpsilonEquals(131.5, this.shape.getMaxX());
		assertEpsilonEquals(18, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getSecondAxisExtent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getSecondAxisExtent());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setSecondAxisExtent(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.setSecondAxisExtent(124);
		assertEpsilonEquals(5, this.shape.getMinX());
		assertEpsilonEquals(-111, this.shape.getMinY());
		assertEpsilonEquals(10, this.shape.getMaxX());
		assertEpsilonEquals(137, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setFirstAxisDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D v = createVector(1,  1).toUnitVector();
		this.shape.setFirstAxis(v.getX(), v.getY(), 5);
		assertEpsilonEquals(3.96447, this.shape.getMinX());
		assertEpsilonEquals(9.46446, this.shape.getMinY());
		assertEpsilonEquals(11.03553, this.shape.getMaxX());
		assertEpsilonEquals(16.53553, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setSecondAxisDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Vector2D v = createVector(-1,  1).toUnitVector();
		this.shape.setSecondAxis(v.getX(), v.getY(), 6);
		assertEpsilonEquals(3.25736, this.shape.getMinX());
		assertEpsilonEquals(8.75736, this.shape.getMinY());
		assertEpsilonEquals(11.74264, this.shape.getMaxX());
		assertEpsilonEquals(17.24264, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set(1, 2, -1, 0, 5, 6);
		assertEpsilonEquals(-4, this.shape.getMinX());
		assertEpsilonEquals(-4, this.shape.getMinY());
		assertEpsilonEquals(6, this.shape.getMaxX());
		assertEpsilonEquals(8, this.shape.getMaxY());

		Vector2D v = createVector(1,  1).toUnitVector();
		this.shape.set(1, 2, v.getX(), v.getY(), 5, 6);
		assertEpsilonEquals(-3.24264, this.shape.getMinX());
		assertEpsilonEquals(-2.24264, this.shape.getMinY());
		assertEpsilonEquals(5.24264, this.shape.getMaxX());
		assertEpsilonEquals(6.24264, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isCCW(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.isCCW());
		assertTrue(createRectangle(
				4.7, 15,
				18.02776, 20).isCCW());
		assertTrue(createRectangle(
				-10, -3,
				2, 1).isCCW());
		assertTrue(createRectangle(
				-10, 7,
				1, 2).isCCW());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createCircle(0, 0, 1)));
		assertFpPointEquals(5, 14, this.shape.getClosestPointTo(createCircle(-2, 14, 1)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createCircle(-1, 21, 1)));
		assertFpPointEquals(7, 18, this.shape.getClosestPointTo(createCircle(7, 21, 1)));
		assertClosestPointInBothShapes(this.shape, createCircle(9, 13, 1));
		assertFpPointEquals(8, 8, this.shape.getClosestPointTo(createCircle(8, 4, 1)));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createCircle(20, 0, 1)));
		assertFpPointEquals(10, 14, this.shape.getClosestPointTo(createCircle(19, 14, 1)));
		assertFpPointEquals(10, 18, this.shape.getClosestPointTo(createCircle(18, 21, 1)));
		assertClosestPointInBothShapes(this.shape, createCircle(5, 18, 1));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createCircle(4.3, 7, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredCircle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(71.13204, this.shape.getDistanceSquared(createCircle(0, 0, 1)));
		assertEpsilonEquals(36, this.shape.getDistanceSquared(createCircle(-2, 14, 1)));
		assertEpsilonEquals(32.58359, this.shape.getDistanceSquared(createCircle(-1, 21, 1)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createCircle(7, 21, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(9, 13, 1)));
		assertEpsilonEquals(9, this.shape.getDistanceSquared(createCircle(8, 4, 1)));
		assertEpsilonEquals(139.3875, this.shape.getDistanceSquared(createCircle(20, 0, 1)));
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createCircle(19, 14, 1)));
		assertEpsilonEquals(56.91199, this.shape.getDistanceSquared(createCircle(18, 21, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(5, 18, 1)));
		assertEpsilonEquals(0.04869, this.shape.getDistanceSquared(createCircle(4.3, 7, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointRectangleRectangle(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 0, 0, 1, 1, result);
		assertFpPointEquals(5, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 0, 12, 1, 13, result);
		assertFpPointEquals(5, 12.5, result);
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 0, 21, 1, 22, result);
		assertFpPointEquals(5, 18, result);
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 7, 0, 8, 1, result);
		assertFpPointEquals(7.5, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 8, 12, 9, 13, result);
		assertFpPointEquals(8.5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 9, 21, 10, 22, result);
		assertFpPointEquals(9.5, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 15, 0, 16, 1, result);
		assertFpPointEquals(10, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 16, 12, 17, 13, result);
		assertFpPointEquals(10, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 17, 21, 18, 22, result);
		assertFpPointEquals(10, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 1, 12, 11, 13, result);
		assertFpPointEquals(6, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 1, 12, 11, 32, result);
		assertFpPointEquals(6, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 8, 0, 9, 20, result);
		assertFpPointEquals(8.5, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 8, 0, 18, 20, result);
		assertFpPointEquals(10, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 6, 10, 7, 11, result);
		assertFpPointEquals(6.5, 10.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4, 12, 5, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.1, 12, 5.1, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.2, 12, 5.2, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.3, 12, 5.3, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.4, 12, 5.4, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.5, 12, 5.5, 13, result);
		assertFpPointEquals(5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.6, 12, 5.6, 13, result);
		assertFpPointEquals(5.1, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.7, 12, 5.7, 13, result);
		assertFpPointEquals(5.2, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.8, 12, 5.8, 13, result);
		assertFpPointEquals(5.3, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 4.9, 12, 5.9, 13, result);
		assertFpPointEquals(5.4, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 5, 12, 6, 13, result);
		assertFpPointEquals(5.5, 12.5, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleRectangle(5, 8, 10, 18, 5.1, 12, 6.1, 13, result);
		assertFpPointEquals(5.6, 12.5, result);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createRectangle(0, 0, 1, 1)));
		assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createRectangle(0, 12, 1, 1)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createRectangle(0, 21, 1, 1)));
		assertFpPointEquals(7.5, 8, this.shape.getClosestPointTo(createRectangle(7, 0, 1, 1)));
		assertClosestPointInBothShapes(this.shape, createRectangle(8, 12, 1, 1));
		assertFpPointEquals(9.5, 18, this.shape.getClosestPointTo(createRectangle(9, 21, 1, 1)));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createRectangle(15, 0, 1, 1)));
		assertFpPointEquals(10, 12.5, this.shape.getClosestPointTo(createRectangle(16, 12, 1, 1)));
		assertFpPointEquals(10, 18, this.shape.getClosestPointTo(createRectangle(17, 21, 1, 1)));
		assertFpPointEquals(6, 12.5, this.shape.getClosestPointTo(createRectangle(1, 12, 10, 1)));
		assertClosestPointInBothShapes(this.shape, createRectangle(1, 12, 10, 20));
		assertClosestPointInBothShapes(this.shape, createRectangle(8, 0, 1, 20));
		assertClosestPointInBothShapes(this.shape, createRectangle(8, 0, 10, 20));
		assertClosestPointInBothShapes(this.shape, createRectangle(6, 10, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.1, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.2, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.3, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.4, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.5, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.6, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.7, 12, 1, 1));
		assertClosestPointInBothShapes( this.shape, createRectangle(4.8, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(4.9, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(5, 12, 1, 1));
		assertClosestPointInBothShapes(this.shape, createRectangle(5.1, 12, 1, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(65, this.shape.getDistanceSquared(createRectangle(0, 0, 1, 1)));
		assertEpsilonEquals(16, this.shape.getDistanceSquared(createRectangle(0, 12, 1, 1)));
		assertEpsilonEquals(25, this.shape.getDistanceSquared(createRectangle(0, 21, 1, 1)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createRectangle(7, 0, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(8, 12, 1, 1)));
		assertEpsilonEquals(9, this.shape.getDistanceSquared(createRectangle(9, 21, 1, 1)));
		assertEpsilonEquals(74, this.shape.getDistanceSquared(createRectangle(15, 0, 1, 1)));
		assertEpsilonEquals(36, this.shape.getDistanceSquared(createRectangle(16, 12, 1, 1)));
		assertEpsilonEquals(58, this.shape.getDistanceSquared(createRectangle(17, 21, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(1, 12, 10, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(1, 12, 10, 20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(8, 0, 1, 20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(8, 0, 10, 20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(6, 10, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.1, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.2, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.3, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.4, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.5, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.6, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.7, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.8, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4.9, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(5, 12, 1, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(5.1, 12, 1, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointRectangleSegment(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D<?, ?> result;
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 0, 0, 2, 1, result);
		assertFpPointEquals(5, 8, result);
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 0, 0, 1, 2, result);
		assertFpPointEquals(5, 8, result);
		
		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 1, 10, 4, 14, result);
		assertFpPointEquals(5, 14, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 1, 10, 6, 14, result);
		assertFpPointEquals(5, 13.2, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 1, 10, 18, 14, result);
		assertFpPointEquals(5, 10.94118, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 7, 10, 18, 14, result);
		assertFpPointEquals(7, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 17, 10, 18, 14, result);
		assertFpPointEquals(10, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 0, 9, 6, result);
		assertFpPointEquals(9, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 0, 9, 16, result);
		assertFpPointEquals(7.5, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 0, 9, 21, result);
		assertFpPointEquals(7.14286, 8, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 10, 9, 21, result);
		assertFpPointEquals(6, 10, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 6, 19, 9, 21, result);
		assertFpPointEquals(6, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 8, 20, 14, 8, result);
		assertFpPointEquals(9, 18, result);

		result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleSegment(5, 8, 10, 18, 10.593538844843389, 12.775717435385788, 9.484138452392932, 14.439818024061475, result);
		assertFpPointEquals(10, 13.66603, result);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createSegment(0, 0, 2, 1)));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createSegment(0, 0, 1, 2)));
		assertFpPointEquals(5, 14, this.shape.getClosestPointTo(createSegment(1, 10, 4, 14)));
		assertClosestPointInBothShapes(this.shape, createSegment(1, 10, 6, 14));
		assertClosestPointInBothShapes(this.shape, createSegment(1, 10, 18, 14));
		assertClosestPointInBothShapes(this.shape, createSegment(7, 10, 18, 14));
		assertFpPointEquals(10, 10, this.shape.getClosestPointTo(createSegment(17, 10, 18, 14)));
		assertFpPointEquals(9, 8, this.shape.getClosestPointTo(createSegment(6, 0, 9, 6)));
		assertClosestPointInBothShapes(this.shape, createSegment(6, 0, 9, 16));
		assertClosestPointInBothShapes(this.shape, createSegment(6, 0, 9, 21));
		assertClosestPointInBothShapes(this.shape, createSegment(6, 10, 9, 21));
		assertFpPointEquals(6, 18, this.shape.getClosestPointTo(createSegment(6, 19, 9, 21)));
		assertClosestPointInBothShapes(this.shape, createSegment(8, 20, 14, 8));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredSegment2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(58, this.shape.getDistanceSquared(createSegment(0, 0, 2, 1)));
		assertEpsilonEquals(52, this.shape.getDistanceSquared(createSegment(0, 0, 1, 2)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createSegment(1, 10, 4, 14)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(1, 10, 6, 14)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(1, 10, 18, 14)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(7, 10, 18, 14)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createSegment(17, 10, 18, 14)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createSegment(6, 0, 9, 6)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(6, 0, 9, 16)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(6, 0, 9, 21)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(6, 10, 9, 21)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createSegment(6, 19, 9, 21)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(8, 20, 14, 8)));
	}

	protected MultiShape2afp createTestMultiShape(double x, double y) {
		MultiShape2afp multishape = createMultiShape();
		multishape.add(createCircle(x - 5, y + 4, 1));
		multishape.add(createSegment(x + 4, y + 2, x + 8, y - 1));
		return multishape;
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestMultiShape(-10, 7)));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestMultiShape(0, -4)));
		assertClosestPointInBothShapes(this.shape, createTestMultiShape(4, 6));
		assertFpPointEquals(5, 10, this.shape.getClosestPointTo(createTestMultiShape(8, 6)));
		assertClosestPointInBothShapes(this.shape, createTestMultiShape(9, 6));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredMultiShape2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(53, this.shape.getDistanceSquared(createTestMultiShape(-10, 7)));
		assertEpsilonEquals(101, this.shape.getDistanceSquared(createTestMultiShape(0, -4)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(4, 6)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createTestMultiShape(8, 6)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(9, 6)));
	}
	
	protected Triangle2afp createTestTriangle(double dx,  double dy) {
		return createTriangle(dx, dy, dx + 3, dy + 3, dx - 1, dy + 1);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestTriangle(0, 4)));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createTestTriangle(10, 6)));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(4, 16));
		assertFpPointEquals(6, 18, this.shape.getClosestPointTo(createTestTriangle(6, 19)));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(10.5, 17.1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredTriangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getDistanceSquared(createTestTriangle(0, 4)));
		assertEpsilonEquals(.2, this.shape.getDistanceSquared(createTestTriangle(10, 6)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(4, 16)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createTestTriangle(6, 19)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(10.5, 17.1)));
	}

	protected Path2afp createSimpleTestPath(double dx, double dy, boolean close) {
		Path2afp path = createPath();
		path.moveTo(dx + 8, dy + 4);
		path.lineTo(dx - 2, dy + 10);
		path.lineTo(dx + 6, dy + 26);
		path.lineTo(dx + 18, dy + 18);
		if (close) {
			path.closePath();
		}
		return path;
	}

	protected Path2afp createComplexTestPath(double dx, double dy, boolean close, PathWindingRule rule) {
		Path2afp path = createPath(rule);
		path.moveTo(dx, dy);
		path.lineTo(dx - 12, dy + 8);
		path.lineTo(dx - 8, dy + 18);
		path.lineTo(dx + 4, dy + 18);
		path.lineTo(dx - 2, dy);
		path.lineTo(dx - 16, dy + 8);
		path.lineTo(dx - 8, dy + 24);
		path.lineTo(dx + 6, dy + 20);
		if (close) {
			path.closePath();
		}
		return path;
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createSimpleTestPath(0, 0, false)));
		assertClosestPointInBothShapes(this.shape, createSimpleTestPath(0, 0, true));
		assertClosestPointInBothShapes(this.shape, createSimpleTestPath(0, 10, false));
		assertClosestPointInBothShapes(this.shape, createSimpleTestPath(0, 10, true));
		assertFpPointEquals(10, 12, this.shape.getClosestPointTo(createSimpleTestPath(14, 2, false)));
		assertFpPointEquals(10, 12, this.shape.getClosestPointTo(createSimpleTestPath(14, 2, true)));
		//
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createComplexTestPath(12, 2, false, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createComplexTestPath(12, 2, true, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(3, 8, false, PathWindingRule.EVEN_ODD)));
		assertClosestPointInBothShapes(this.shape, createComplexTestPath(3, 8, true, PathWindingRule.EVEN_ODD));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(-2, 8, false, PathWindingRule.EVEN_ODD)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(-2, 8, true, PathWindingRule.EVEN_ODD)));
		//
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createComplexTestPath(12, 2, false, PathWindingRule.NON_ZERO)));
		assertClosestPointInBothShapes(this.shape, createComplexTestPath(12, 2, true, PathWindingRule.NON_ZERO));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(3, 8, false, PathWindingRule.NON_ZERO)));
		assertClosestPointInBothShapes(this.shape, createComplexTestPath(3, 8, true, PathWindingRule.NON_ZERO));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(-2, 8, false, PathWindingRule.NON_ZERO)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createComplexTestPath(-2, 8, true, PathWindingRule.NON_ZERO)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredPath2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3.55882, this.shape.getDistanceSquared(createSimpleTestPath(0, 0, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSimpleTestPath(0, 0, true)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSimpleTestPath(0, 10, false)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSimpleTestPath(0, 10, true)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createSimpleTestPath(14, 2, false)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createSimpleTestPath(14, 2, true)));
		//
		assertEpsilonEquals(1.23077, this.shape.getDistanceSquared(createComplexTestPath(12, 2, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(1.23077, this.shape.getDistanceSquared(createComplexTestPath(12, 2, true, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(.4, this.shape.getDistanceSquared(createComplexTestPath(3, 8, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createComplexTestPath(3, 8, true, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(28.9, this.shape.getDistanceSquared(createComplexTestPath(-2, 8, false, PathWindingRule.EVEN_ODD)));
		assertEpsilonEquals(14.6789, this.shape.getDistanceSquared(createComplexTestPath(-2, 8, true, PathWindingRule.EVEN_ODD)));
		//
		assertEpsilonEquals(1.23077, this.shape.getDistanceSquared(createComplexTestPath(12, 2, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createComplexTestPath(12, 2, true, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(.4, this.shape.getDistanceSquared(createComplexTestPath(3, 8, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createComplexTestPath(3, 8, true, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(28.9, this.shape.getDistanceSquared(createComplexTestPath(-2, 8, false, PathWindingRule.NON_ZERO)));
		assertEpsilonEquals(14.6789, this.shape.getDistanceSquared(createComplexTestPath(-2, 8, true, PathWindingRule.NON_ZERO)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createEllipse(0, 0, 2, 1)));
		assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createEllipse(2, 12, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createEllipse(6, 16, 2, 1));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createEllipse(9.897519745562938, 7.003543789189412, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createEllipse(9.5, 9.5, 2, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredEllipse2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(61.90769, this.shape.getDistanceSquared(createEllipse(0, 0, 2, 1)));
		assertEpsilonEquals(1, this.shape.getDistanceSquared(createEllipse(2, 12, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(6, 16, 2, 1)));
		assertEpsilonEquals(0.047502, this.shape.getDistanceSquared(createEllipse(9.897519745562938, 7.003543789189412, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(9.5, 9.5, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createRoundRectangle(0, 12, 1, 1, .1, .1)));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createRoundRectangle(0, 21, 1, 1, .1, .1)));
		assertFpPointEquals(7.5, 8, this.shape.getClosestPointTo(createRoundRectangle(7, 0, 1, 1, .1, .1)));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(8, 12, 1, 1, .1, .1));
		assertFpPointEquals(9.5, 18, this.shape.getClosestPointTo(createRoundRectangle(9, 21, 1, 1, .1, .1)));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createRoundRectangle(15, 0, 1, 1, .1, .1)));
		assertFpPointEquals(10, 12.5, this.shape.getClosestPointTo(createRoundRectangle(16, 12, 1, 1, .1, .1)));
		assertFpPointEquals(10, 18, this.shape.getClosestPointTo(createRoundRectangle(17, 21, 1, 1, .1, .1)));
		assertFpPointEquals(6, 12.5, this.shape.getClosestPointTo(createRoundRectangle(1, 12, 10, 1, .1, .1)));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(1, 12, 10, 20, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(8, 0, 1, 20, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(8, 0, 10, 20, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(6, 10, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.1, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.2, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.3, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.4, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.5, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.6, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.7, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.8, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(4.9, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(5, 12, 1, 1, .1, .1));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(5.1, 12, 1, 1, .1, .1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRoundRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(65.59024, this.shape.getDistanceSquared(createRoundRectangle(0, 0, 1, 1, .1, .1)));
		assertEpsilonEquals(16, this.shape.getDistanceSquared(createRoundRectangle(0, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(25.40199, this.shape.getDistanceSquared(createRoundRectangle(0, 21, 1, 1, .1, .1)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createRoundRectangle(7, 0, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(8, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(9, this.shape.getDistanceSquared(createRoundRectangle(9, 21, 1, 1, .1, .1)));
		assertEpsilonEquals(74.68163, this.shape.getDistanceSquared(createRoundRectangle(15, 0, 1, 1, .1, .1)));
		assertEpsilonEquals(36, this.shape.getDistanceSquared(createRoundRectangle(16, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(58.48055, this.shape.getDistanceSquared(createRoundRectangle(17, 21, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(1, 12, 10, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(1, 12, 10, 20, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(8, 0, 1, 20, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(8, 0, 10, 20, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(6, 10, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.1, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.2, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.3, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.4, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.5, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.6, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.7, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.8, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(4.9, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(5, 12, 1, 1, .1, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(5.1, 12, 1, 1, .1, .1)));
	}

	protected Parallelogram2afp createTestParallelogram(double cx, double cy) {
		Vector2D u = createVector(5, 1).toUnitVector();
		Vector2D v = createVector(4, -6).toUnitVector();
		return createParallelogram(cx, cy, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1);
	}
	
	private Point2D<?, ?> runFindsClosestPointRectangleParallelogram(double cx, double cy) {
		Parallelogram2afp p = createTestParallelogram(cx, cy);
		Point2D<?, ?> result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleParallelogram(5, 8, 10, 18,
				p.getCenterX(), p.getCenterY(), p.getFirstAxisX(), p.getFirstAxisY(), p.getFirstAxisExtent(),
				p.getSecondAxisX(), p.getSecondAxisY(), p.getSecondAxisExtent(), result);
		return result;
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointRectangleParallelogram(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, runFindsClosestPointRectangleParallelogram(0, 0));
		assertFpPointEquals(5, 9.56018, runFindsClosestPointRectangleParallelogram(2, 10));
		assertFpPointEquals(10, 18, runFindsClosestPointRectangleParallelogram(14, 18));
		assertFpPointEquals(9.48414, 14.43982, runFindsClosestPointRectangleParallelogram(12, 14));
		assertFpPointEquals(10, 8, runFindsClosestPointRectangleParallelogram(14, 2));
		// In multishape.ggb
		Point2D<?, ?> result = createPoint(Double.NaN, Double.NaN);
		Rectangle2afp.findsClosestPointRectangleParallelogram(
				5, 8, 7, 9,
				9, 5,
				-0.624695047554424, 0.780868809443031, 2,
				0.894427190999917, -0.447213595499958, 1,
				result);
		assertFpPointEquals(6.85618, 8, result);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestParallelogram(0, 0)));
		assertFpPointEquals(5, 9.56018, this.shape.getClosestPointTo(createTestParallelogram(2, 10)));
		assertFpPointEquals(10, 18, this.shape.getClosestPointTo(createTestParallelogram(14, 18)));
		assertClosestPointInBothShapes(this.shape, createTestParallelogram(12, 14));
		assertFpPointEquals(10, 8, this.shape.getClosestPointTo(createTestParallelogram(14, 2)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredParallelogram2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(58.82387, this.shape.getDistanceSquared(createTestParallelogram(0, 0)));
		assertEpsilonEquals(0.23439, this.shape.getDistanceSquared(createTestParallelogram(2, 10)));
		assertEpsilonEquals(2.39611, this.shape.getDistanceSquared(createTestParallelogram(14, 18)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(12, 14)));
		assertEpsilonEquals(33.11829, this.shape.getDistanceSquared(createTestParallelogram(14, 2)));
	}

	protected OrientedRectangle2afp createTestOrientedRectangle(double cx, double cy) {
		Vector2D u = createVector(5, 1).toUnitVector();
		return createOrientedRectangle(cx, cy, u.getX(), u.getY(), 2, 1);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(5, 8, this.shape.getClosestPointTo(createTestOrientedRectangle(0, 0)));
		assertFpPointEquals(5, 9.41165, this.shape.getClosestPointTo(createTestOrientedRectangle(2, 10)));
		assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(12, 8));
		assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(8, 12));
		assertFpPointEquals(5, 18, this.shape.getClosestPointTo(createTestOrientedRectangle(6, 20)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredOrientedRectangle2afp(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(54.38454, this.shape.getDistanceSquared(createTestOrientedRectangle(0, 0)));
		assertEpsilonEquals(0.71018, this.shape.getDistanceSquared(createTestOrientedRectangle(2, 10)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(12, 8)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(8, 12)));
		assertEpsilonEquals(0.58529, this.shape.getDistanceSquared(createTestOrientedRectangle(6, 20)));
	}

}