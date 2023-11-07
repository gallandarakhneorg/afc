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

import static org.arakhne.afc.math.geometry.GeomConstants.SHAPE_INTERSECTS;
import static org.arakhne.afc.math.geometry.GeomConstants.SPLINE_APPROXIMATION_RATIO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPath3afpTest<T extends Path3afp<?, T, ?, ?, ?, ?, B>, B extends AlignedBox3afp<?, ?, ?, ?, ?, ?, B>>
extends AbstractShape3afpTest<T, B> {

	@Override
	protected T createShape() {
		T path = (T) createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(1, 1, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		return path;
	}

	@DisplayName("calculatesCrossingsFromPoint(int,PathIterator3afp,double,double,double,CrossComputingType) with null type")
	@Test
	public void staticCalculatesCrossingsFromPoint_null() {
		assertEquals(-1, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, 0, null));
		assertEquals(-1, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, 0, null));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, 0, null));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, 0, null));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, 0, null));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, 0, null));
		assertEquals(-1, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, 0, null));
	}

	@DisplayName("calculatesCrossingsFromPoint(int,PathIterator3afp,double,double,double,CrossComputingType) with STANDARD type")
	@Test
	public void staticCalculatesCrossingsFromPoint_standard() {
		assertEquals(-1, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, 0, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, 0, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, 0, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, 0, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, 0, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, 0, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, 0, CrossingComputationType.STANDARD));
	}

	@DisplayName("calculatesCrossingsFromPoint(int,PathIterator3afp,double,double,double,CrossComputingType) with AUTO_CLOSE type")
	@Test
	public void staticCalculatesCrossingsFromPoint_autoClose() {
		assertEquals(-1, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(-1, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, 0, CrossingComputationType.AUTO_CLOSE));
	}

	@DisplayName("calculatesCrossingsFromPoint(int,PathIterator3afp,double,double,double,CrossComputingType) with SIMPLE type")
	@Test
	public void staticCalculatesCrossingsFromPoint_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.calculatesCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@DisplayName("calculatesControlPointBoundingBox(PathIterator3afp,AlignedBox3afp)")
	@Test
	public void staticCalculatesControlPointBoundingBox() {
		B box = createAlignedBox(0, 0, 0, 0, 0, 0);
		Path3afp.calculatesControlPointBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(5, box.getMaxY());
	}

	@DisplayName("calculatesDrawableElementBoundingBox(PathIterator3afp,AlignedBox3afp)")
	@Test
	public void staticCalculatesDrawableElementBoundingBox() {
		B box = createAlignedBox(0, 0, 0, 0, 0, 0);
		Path3afp.calculatesDrawableElementBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
	}

	@DisplayName("containsPoint(PathIterator3afp,double,double,double)")
	@Test
	public void staticContainsPoint() {
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), -5, 1, 0));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 3, 6, 0));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 3, -10, 0));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 11, 1, 0));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 4, 1, 0));
		assertTrue(Path3afp.containsPoint(this.shape.getPathIterator(), 4, 3, 0));
		this.shape.closePath();
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), -5, 1, 0));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 3, 6, 0));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 3, -10, 0));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 11, 1, 0));
		assertTrue(Path3afp.containsPoint(this.shape.getPathIterator(), 4, 1, 0));
		assertTrue(Path3afp.containsPoint(this.shape.getPathIterator(), 4, 3, 0));
	}

	@DisplayName("getClosestPointTo(PathIterator3afp,double,double,double,Point3D) open path")
	@Test
	public void staticGetClosestPointTo_open() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(2.56307, result.getX());
		assertEpsilonEquals(0.91027, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
	}

	@DisplayName("getClosestPointTo(PathIterator3afp,double,double,double,Point3D) close path")
	@Test
	public void staticGetClosestPointTo_close() {
		this.shape.closePath();

		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(3, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(2.55405, result.getX());
		assertEpsilonEquals(-1.82432, result.getY());
	}

	@DisplayName("getFarthestPointTo(PathIterator3afp,double,double,double,Point3D) open path")
	@Test
	public void staticGetFarthestPointTo_open() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@DisplayName("getFarthestPointTo(PathIterator3afp,double,double,double,Point3D) close path")
	@Test
	public void staticGetFarthestPointTo_close() {
		this.shape.closePath();

		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@DisplayName("intersectsPathIteratorRectangle(PathIterator3afp,double,double,double,double,double,double) open path")
	@Test
	public void staticIntersectsPathIteratorRectangle_open() {
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 0, 2, 1, 0));
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 0, 2, 1, 0));
	}

	@DisplayName("intersectsPathIteratorRectangle(PathIterator3afp,double,double,double,double,double,double) close path")
	@Test
	public void staticIntersectsPathIteratorRectangle_close() {
		this.shape.closePath();
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 0, 2, 1, 0));
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 0, 2, 1, 0));
	}

	@DisplayName("calculatesLength(PathIterator3afp) open path")
	@Test
	public void staticCalculatesLength_open() {
		assertEpsilonEquals(14.71628, Path3afp.calculatesLength(this.shape.getPathIterator()));
	}

	@DisplayName("calculatesLength(PathIterator3afp) close path")
	@Test
	public void staticCalculatesLength_close() {
		this.shape.closePath();
		assertEpsilonEquals(23.31861, Path3afp.calculatesLength(this.shape.getPathIterator()));
	}

	@DisplayName("add(Iterator) open path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addIterator_open(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5, 0);
		p2.lineTo(4, 6, 0);
		p2.lineTo(0, 8, 0);
		p2.lineTo(5, -3, 0);
		p2.closePath();

		Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 0, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, -3, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@DisplayName("add(Iterator) close-after path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addIterator_closeAfter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5, 0);
		p2.lineTo(4, 6, 0);
		p2.lineTo(0, 8, 0);
		p2.lineTo(5, -3, 0);
		p2.closePath();

		Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		this.shape.closePath();

		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 0, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, -3, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@DisplayName("add(Iterator) close-before path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void addIterator_closeBefore(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();

		Path3afp<?, ?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5, 0);
		p2.lineTo(4, 6, 0);
		p2.lineTo(0, 8, 0);
		p2.lineTo(5, -3, 0);
		p2.closePath();

		Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 0, 8, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, -3, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@DisplayName("curveTo(double,double,double,double,double,double,double,double,double) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void curveToDoubleDoubleDoubleDoubleDoubleDouble_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(15, 145, 0, 50, 20, 0, 0, 0, 0);
		});
	}
	
	@DisplayName("curveTo(double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void curveToDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.curveTo(123.456, 456.789, 0, 789.123, 159.753, 0, 456.852, 963.789, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CURVE_TO, 123.456, 456.789, 0, 789.123, 159.753, 0, 456.852, 963.789, 0);
		assertNoElement(pi);
	}
	
	@DisplayName("curveTo(Point3D,Point3D,Point3D) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void curveToPoint3DPoint3DPoint3D_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(createPoint(15, 145, 0), createPoint(50, 20, 0), createPoint(0, 0, 0));
		});
	}

	@DisplayName("curveTo(Point3D,Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void curveToPoint3DPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.curveTo(createPoint(123.456, 456.789, 0), createPoint(789.123, 159.753, 0), createPoint(456.852, 963.789, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CURVE_TO, 123.456, 456.789, 0, 789.123, 159.753, 0, 456.852, 963.789, 0);
		assertNoElement(pi);
	}

	@DisplayName("getCoordAt(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCoordAt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.getCoordAt(0)==0);
		assertTrue(this.shape.getCoordAt(1)==0);
		assertTrue(this.shape.getCoordAt(2)==0);
		assertTrue(this.shape.getCoordAt(3)==1);
		assertTrue(this.shape.getCoordAt(4)==1);
		assertTrue(this.shape.getCoordAt(5)==0);
		assertTrue(this.shape.getCoordAt(6)==3);
		assertTrue(this.shape.getCoordAt(7)==0);
		assertTrue(this.shape.getCoordAt(8)==0);
		assertTrue(this.shape.getCoordAt(9)==4);
		assertTrue(this.shape.getCoordAt(10)==3);
		assertTrue(this.shape.getCoordAt(11)==0);
		assertTrue(this.shape.getCoordAt(12)==5);
		assertTrue(this.shape.getCoordAt(13)==-1);
		assertTrue(this.shape.getCoordAt(14)==0);
		assertTrue(this.shape.getCoordAt(15)==6);
		assertTrue(this.shape.getCoordAt(16)==5);
		assertTrue(this.shape.getCoordAt(17)==0);
		assertTrue(this.shape.getCoordAt(18)==7);
		assertTrue(this.shape.getCoordAt(19)==-5);
		assertTrue(this.shape.getCoordAt(20)==0);
	}

	@DisplayName("toBoundingBox(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = createAlignedBox(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
		assertEpsilonEquals(3, box.getMaxZ());
	}

	@DisplayName("toBoundingBox")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
		assertEpsilonEquals(3, box.getMaxZ());
	}

	@DisplayName("getPathIterator(double) open path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPathIteratorDouble_open(CoordinateSystem3D cs) throws Exception {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp<?> pi = this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO);
		//String str = generateGeogebraPolygon(pi);
		//System.out.println(str);
		assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.0, 1.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, 0.0);
		/*assertElement(pi, PathElementType.LINE_TO, 4.0029296875, 1.1429190635681152E-5, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.005859375, 4.565715789794922E-5, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.01171875, 1.8215179443359375E-4, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.0234375, 7.2479248046875E-4, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.046875, 0.00286865234375, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.09375, 0.01123046875, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.1875, 0.04296875, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.28125, 0.0869140625, 0.017578125);
		assertElement(pi, PathElementType.LINE_TO, 4.3046875, 0.06964111328125, 0.02655029296875);
		assertElement(pi, PathElementType.LINE_TO, 4.31640625, 0.09035491943359375, 0.0463409423828125);
		assertElement(pi, PathElementType.LINE_TO, 4.3193359375, 0.07635855674743652, 0.05133771896362305);
		assertElement(pi, PathElementType.LINE_TO, 4.322265625, 0.09907722473144531, 0.053791046142578125);
		assertElement(pi, PathElementType.LINE_TO, 4.3251953125, 0.10297822952270508, 0.056801795959472656);
		assertElement(pi, PathElementType.LINE_TO, 4.328125, 0.11328125, 0.0322265625);
		assertElement(pi, PathElementType.LINE_TO, 4.33984375, 0.08628082275390625, 0.039825439453125);
		assertElement(pi, PathElementType.LINE_TO, 4.345703125, 0.10619926452636719, 0.059169769287109375);
		assertElement(pi, PathElementType.LINE_TO, 4.3486328125, 0.11415767669677734, 0.065887451171875);
		assertElement(pi, PathElementType.LINE_TO, 4.3515625, 0.12774658203125, 0.0413818359375);
		assertElement(pi, PathElementType.LINE_TO, 4.357421875, 0.09561920166015625, 0.047824859619140625);
		assertElement(pi, PathElementType.LINE_TO, 4.3603515625, 0.11478376388549805, 0.06645679473876953);
		assertElement(pi, PathElementType.LINE_TO, 4.36328125, 0.135345458984375, 0.0465087890625);
		assertElement(pi, PathElementType.LINE_TO, 4.369140625, 0.13924789428710938, 0.04923248291015625);
		assertElement(pi, PathElementType.LINE_TO, 4.3720703125, 0.14122772216796875, 0.050640106201171875);
		assertElement(pi, PathElementType.LINE_TO, 4.375, 0.15625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.3779296875, 2.484917640686035E-4, 4.5299530029296875E-5);
		assertElement(pi, PathElementType.LINE_TO, 4.380859375, 9.808540344238281E-4, 1.79290771484375E-4);
		assertElement(pi, PathElementType.LINE_TO, 4.38671875, 0.003818511962890625, 7.01904296875E-4);
		assertElement(pi, PathElementType.LINE_TO, 4.3984375, 0.014434814453125, 0.002685546875);
		assertElement(pi, PathElementType.LINE_TO, 4.421875, 0.051025390625, 0.009765625);
		assertElement(pi, PathElementType.LINE_TO, 4.4453125, 0.09454345703125, 0.0352783203125);
		assertElement(pi, PathElementType.LINE_TO, 4.45703125, 0.11737060546875, 0.05218505859375);
		assertElement(pi, PathElementType.LINE_TO, 4.4599609375, 0.09577798843383789, 0.05885887145996094);
		assertElement(pi, PathElementType.LINE_TO, 4.462890625, 0.12892532348632812, 0.06142425537109375);
		assertElement(pi, PathElementType.LINE_TO, 4.4658203125, 0.1347064971923828, 0.0661773681640625);
		assertElement(pi, PathElementType.LINE_TO, 4.46875, 0.150390625, 0.03125);
		assertElement(pi, PathElementType.LINE_TO, 4.4921875, 0.13323974609375, 0.05908203125);
		assertElement(pi, PathElementType.LINE_TO, 4.498046875, 0.12296104431152344, 0.07830810546875);
		assertElement(pi, PathElementType.LINE_TO, 4.5009765625, 0.14888906478881836, 0.1071004867553711);
		assertElement(pi, PathElementType.LINE_TO, 4.50390625, 0.1749725341796875, 0.103485107421875);
		assertElement(pi, PathElementType.LINE_TO, 4.5068359375, 0.15397357940673828, 0.11333179473876953);
		assertElement(pi, PathElementType.LINE_TO, 4.509765625, 0.1926422119140625, 0.1209869384765625);
		assertElement(pi, PathElementType.LINE_TO, 4.5126953125, 0.20057201385498047, 0.12835121154785156);
		assertElement(pi, PathElementType.LINE_TO, 4.515625, 0.21826171875, 0.0927734375);
		assertElement(pi, PathElementType.LINE_TO, 4.52734375, 0.180877685546875, 0.108551025390625);
		assertElement(pi, PathElementType.LINE_TO, 4.5302734375, 0.16729497909545898, 0.1241464614868164);
		assertElement(pi, PathElementType.LINE_TO, 4.533203125, 0.21397781372070312, 0.14199066162109375);
		assertElement(pi, PathElementType.LINE_TO, 4.5361328125, 0.2273244857788086, 0.15399742126464844);
		assertElement(pi, PathElementType.LINE_TO, 4.5390625, 0.2490234375, 0.11865234375);
		assertElement(pi, PathElementType.LINE_TO, 4.544921875, 0.20206832885742188, 0.1296844482421875);
		assertElement(pi, PathElementType.LINE_TO, 4.5478515625, 0.2318124771118164, 0.1589832305908203);
		assertElement(pi, PathElementType.LINE_TO, 4.55078125, 0.263519287109375, 0.13018798828125);
		assertElement(pi, PathElementType.LINE_TO, 4.5537109375, 0.21191787719726562, 0.1392192840576172);
		assertElement(pi, PathElementType.LINE_TO, 4.556640625, 0.2705230712890625, 0.13555908203125);
		assertElement(pi, PathElementType.LINE_TO, 4.5595703125, 0.2739582061767578, 0.13813400268554688);
		assertElement(pi, PathElementType.LINE_TO, 4.5625, 0.296875, 0.0625);
		assertElement(pi, PathElementType.LINE_TO, 4.609375, 0.234375, 0.091796875);
		assertElement(pi, PathElementType.LINE_TO, 4.62109375, 0.208160400390625, 0.12225341796875);
		assertElement(pi, PathElementType.LINE_TO, 4.6240234375, 0.19533538818359375, 0.14392662048339844);
		assertElement(pi, PathElementType.LINE_TO, 4.626953125, 0.2543182373046875, 0.17181396484375);
		assertElement(pi, PathElementType.LINE_TO, 4.6298828125, 0.2734394073486328, 0.19080734252929688);
		assertElement(pi, PathElementType.LINE_TO, 4.6328125, 0.301513671875, 0.15673828125);
		assertElement(pi, PathElementType.LINE_TO, 4.638671875, 0.25514984130859375, 0.1730194091796875);
		assertElement(pi, PathElementType.LINE_TO, 4.6416015625, 0.291290283203125, 0.20943450927734375);
		assertElement(pi, PathElementType.LINE_TO, 4.64453125, 0.32940673828125, 0.1807861328125);
		assertElement(pi, PathElementType.LINE_TO, 4.6474609375, 0.27403831481933594, 0.19216537475585938);
		assertElement(pi, PathElementType.LINE_TO, 4.650390625, 0.3417510986328125, 0.190338134765625);
		assertElement(pi, PathElementType.LINE_TO, 4.6533203125, 0.3474769592285156, 0.19440460205078125);
		assertElement(pi, PathElementType.LINE_TO, 4.65625, 0.375, 0.109375);
		assertElement(pi, PathElementType.LINE_TO, 4.6796875, 0.283935546875, 0.13330078125);
		assertElement(pi, PathElementType.LINE_TO, 4.685546875, 0.253082275390625, 0.1633453369140625);
		assertElement(pi, PathElementType.LINE_TO, 4.6884765625, 0.2998485565185547, 0.21342849731445312);
		assertElement(pi, PathElementType.LINE_TO, 4.69140625, 0.3475341796875, 0.1953125);
		assertElement(pi, PathElementType.LINE_TO, 4.6943359375, 0.2969493865966797, 0.21112060546875);
		assertElement(pi, PathElementType.LINE_TO, 4.697265625, 0.3727264404296875, 0.216583251953125);
		assertElement(pi, PathElementType.LINE_TO, 4.7001953125, 0.383453369140625, 0.2243499755859375);
		assertElement(pi, PathElementType.LINE_TO, 4.703125, 0.416015625, 0.13671875);
		assertElement(pi, PathElementType.LINE_TO, 4.7060546875, 0.15416431427001953, 0.1387500762939453);
		assertElement(pi, PathElementType.LINE_TO, 4.708984375, 0.19773101806640625, 0.1438140869140625);
		assertElement(pi, PathElementType.LINE_TO, 4.7119140625, 0.24752426147460938, 0.1710357666015625);
		assertElement(pi, PathElementType.LINE_TO, 4.71484375, 0.31060791015625, 0.1568603515625);
		assertElement(pi, PathElementType.LINE_TO, 4.7177734375, 0.2774085998535156, 0.18626785278320312);
		assertElement(pi, PathElementType.LINE_TO, 4.720703125, 0.37164306640625, 0.21624755859375);
		assertElement(pi, PathElementType.LINE_TO, 4.7236328125, 0.3951683044433594, 0.23566436767578125);
		assertElement(pi, PathElementType.LINE_TO, 4.7265625, 0.43701171875, 0.1513671875);
		assertElement(pi, PathElementType.LINE_TO, 4.7294921875, 0.2122058868408203, 0.15787124633789062);
		assertElement(pi, PathElementType.LINE_TO, 4.732421875, 0.3244171142578125, 0.169342041015625);
		assertElement(pi, PathElementType.LINE_TO, 4.7353515625, 0.38397216796875, 0.2271270751953125);
		assertElement(pi, PathElementType.LINE_TO, 4.73828125, 0.4476318359375, 0.158935546875);
		assertElement(pi, PathElementType.LINE_TO, 4.744140625, 0.452972412109375, 0.16278076171875);
		assertElement(pi, PathElementType.LINE_TO, 4.7470703125, 0.45565032958984375, 0.1647186279296875);
		assertElement(pi, PathElementType.LINE_TO, 4.75, 0.5, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.7529296875, 1.8186867237091064E-4, 3.415346145629883E-5);
		assertElement(pi, PathElementType.LINE_TO, 4.755859375, 7.225275039672852E-4, 1.3589859008789062E-4);
		assertElement(pi, PathElementType.LINE_TO, 4.76171875, 0.0028505325317382812, 5.37872314453125E-4);
		assertElement(pi, PathElementType.LINE_TO, 4.7734375, 0.01108551025390625, 0.002105712890625);
		assertElement(pi, PathElementType.LINE_TO, 4.796875, 0.04180908203125, 0.008056640625);
		assertElement(pi, PathElementType.LINE_TO, 4.84375, 0.14697265625, 0.029296875);
		assertElement(pi, PathElementType.LINE_TO, 4.85546875, 0.07076072692871094, 0.03900146484375);
		assertElement(pi, PathElementType.LINE_TO, 4.861328125, 0.1063532829284668, 0.06095409393310547);
		assertElement(pi, PathElementType.LINE_TO, 4.8642578125, 0.1250629425048828, 0.07538938522338867);
		assertElement(pi, PathElementType.LINE_TO, 4.8671875, 0.1515350341796875, 0.06134033203125);
		assertElement(pi, PathElementType.LINE_TO, 4.873046875, 0.14083337783813477, 0.08479118347167969);
		assertElement(pi, PathElementType.LINE_TO, 4.8759765625, 0.1737459897994995, 0.12052321434020996);
		assertElement(pi, PathElementType.LINE_TO, 4.87890625, 0.20755386352539062, 0.11548614501953125);
		assertElement(pi, PathElementType.LINE_TO, 4.8818359375, 0.18152904510498047, 0.12843585014343262);
		assertElement(pi, PathElementType.LINE_TO, 4.884765625, 0.23300743103027344, 0.13878631591796875);
		assertElement(pi, PathElementType.LINE_TO, 4.8876953125, 0.245011568069458, 0.14932584762573242);
		assertElement(pi, PathElementType.LINE_TO, 4.890625, 0.2703857421875, 0.103271484375);
		assertElement(pi, PathElementType.LINE_TO, 4.90234375, 0.230133056640625, 0.12888336181640625);
		assertElement(pi, PathElementType.LINE_TO, 4.9052734375, 0.2127748727798462, 0.1518557071685791);
		assertElement(pi, PathElementType.LINE_TO, 4.908203125, 0.28034114837646484, 0.17926979064941406);
		assertElement(pi, PathElementType.LINE_TO, 4.9111328125, 0.30132079124450684, 0.1983942985534668);
		assertElement(pi, PathElementType.LINE_TO, 4.9140625, 0.33428955078125, 0.151611328125);
		assertElement(pi, PathElementType.LINE_TO, 4.919921875, 0.2736806869506836, 0.170379638671875);
		assertElement(pi, PathElementType.LINE_TO, 4.9228515625, 0.3185279369354248, 0.21499967575073242);
		assertElement(pi, PathElementType.LINE_TO, 4.92578125, 0.36635589599609375, 0.1777496337890625);
		assertElement(pi, PathElementType.LINE_TO, 4.9287109375, 0.2960953712463379, 0.19243383407592773);
		assertElement(pi, PathElementType.LINE_TO, 4.931640625, 0.3823089599609375, 0.19109344482421875);
		assertElement(pi, PathElementType.LINE_TO, 4.9345703125, 0.39023828506469727, 0.19777965545654297);
		assertElement(pi, PathElementType.LINE_TO, 4.9375, 0.42578125, 0.09375);
		assertElement(pi, PathElementType.LINE_TO, 4.9404296875, 0.09563127160072327, 0.09418213367462158);
		assertElement(pi, PathElementType.LINE_TO, 4.943359375, 0.10106396675109863, 0.0954446792602539);
		assertElement(pi, PathElementType.LINE_TO, 4.94921875, 0.12131690979003906, 0.10025787353515625);
		assertElement(pi, PathElementType.LINE_TO, 4.9609375, 0.1905059814453125, 0.11761474609375);
		assertElement(pi, PathElementType.LINE_TO, 4.97265625, 0.2719383239746094, 0.16924285888671875);
		assertElement(pi, PathElementType.LINE_TO, 4.9755859375, 0.24852275848388672, 0.1861412525177002);
		assertElement(pi, PathElementType.LINE_TO, 4.978515625, 0.31402015686035156, 0.20258331298828125);
		assertElement(pi, PathElementType.LINE_TO, 4.9814453125, 0.33510279655456543, 0.2205357551574707);
		assertElement(pi, PathElementType.LINE_TO, 4.984375, 0.3726806640625, 0.171875);
		assertElement(pi, PathElementType.LINE_TO, 4.99609375, 0.3447151184082031, 0.224517822265625);
		assertElement(pi, PathElementType.LINE_TO, 4.9990234375, 0.32943928241729736, 0.2576427459716797);
		assertElement(pi, PathElementType.LINE_TO, 5.001953125, 0.41471195220947266, 0.30274391174316406);
		assertElement(pi, PathElementType.LINE_TO, 5.0048828125, 0.4440317153930664, 0.33360958099365234);
		assertElement(pi, PathElementType.LINE_TO, 5.0078125, 0.484771728515625, 0.29400634765625);
		assertElement(pi, PathElementType.LINE_TO, 5.013671875, 0.4286823272705078, 0.32054710388183594);
		assertElement(pi, PathElementType.LINE_TO, 5.0166015625, 0.4791266918182373, 0.37302255630493164);
		assertElement(pi, PathElementType.LINE_TO, 5.01953125, 0.53155517578125, 0.341583251953125);
		assertElement(pi, PathElementType.LINE_TO, 5.0224609375, 0.4632275104522705, 0.3585519790649414);
		assertElement(pi, PathElementType.LINE_TO, 5.025390625, 0.5523166656494141, 0.3613700866699219);
		assertElement(pi, PathElementType.LINE_TO, 5.0283203125, 0.5619611740112305, 0.3701057434082031);
		assertElement(pi, PathElementType.LINE_TO, 5.03125, 0.5986328125, 0.267578125);
		assertElement(pi, PathElementType.LINE_TO, 5.0341796875, 0.27364838123321533, 0.26855480670928955);
		assertElement(pi, PathElementType.LINE_TO, 5.037109375, 0.2904367446899414, 0.27129459381103516);
		assertElement(pi, PathElementType.LINE_TO, 5.0400390625, 0.31341683864593506, 0.2829887866973877);
		assertElement(pi, PathElementType.LINE_TO, 5.04296875, 0.34763336181640625, 0.28092193603515625);
		assertElement(pi, PathElementType.LINE_TO, 5.048828125, 0.4137601852416992, 0.31934547424316406);
		assertElement(pi, PathElementType.LINE_TO, 5.0517578125, 0.4476296901702881, 0.34438657760620117);
		assertElement(pi, PathElementType.LINE_TO, 5.0546875, 0.49676513671875, 0.30877685546875);
		assertElement(pi, PathElementType.LINE_TO, 5.060546875, 0.4611063003540039, 0.3492870330810547);
		assertElement(pi, PathElementType.LINE_TO, 5.0634765625, 0.5210700035095215, 0.41442298889160156);
		assertElement(pi, PathElementType.LINE_TO, 5.06640625, 0.5817794799804688, 0.3956451416015625);
		assertElement(pi, PathElementType.LINE_TO, 5.0693359375, 0.5214745998382568, 0.41652822494506836);
		assertElement(pi, PathElementType.LINE_TO, 5.072265625, 0.6157093048095703, 0.4265022277832031);
		assertElement(pi, PathElementType.LINE_TO, 5.0751953125, 0.6302437782287598, 0.4382143020629883);
		assertElement(pi, PathElementType.LINE_TO, 5.078125, 0.6708984375, 0.3349609375);
		assertElement(pi, PathElementType.LINE_TO, 5.0810546875, 0.3563019037246704, 0.3376636505126953);
		assertElement(pi, PathElementType.LINE_TO, 5.083984375, 0.4096040725708008, 0.34447479248046875);
		assertElement(pi, PathElementType.LINE_TO, 5.0869140625, 0.4706451892852783, 0.37833070755004883);
		assertElement(pi, PathElementType.LINE_TO, 5.08984375, 0.5477676391601562, 0.362640380859375);
		assertElement(pi, PathElementType.LINE_TO, 5.0927734375, 0.5086724758148193, 0.39896488189697266);
		assertElement(pi, PathElementType.LINE_TO, 5.095703125, 0.6230220794677734, 0.4370231628417969);
		assertElement(pi, PathElementType.LINE_TO, 5.0986328125, 0.6521425247192383, 0.4617271423339844);
		assertElement(pi, PathElementType.LINE_TO, 5.1015625, 0.70294189453125, 0.3626708984375);
		assertElement(pi, PathElementType.LINE_TO, 5.1044921875, 0.43554210662841797, 0.37071752548217773);
		assertElement(pi, PathElementType.LINE_TO, 5.107421875, 0.569976806640625, 0.3851356506347656);
		assertElement(pi, PathElementType.LINE_TO, 5.1103515625, 0.6415190696716309, 0.45488834381103516);
		assertElement(pi, PathElementType.LINE_TO, 5.11328125, 0.717803955078125, 0.374755859375);
		assertElement(pi, PathElementType.LINE_TO, 5.1162109375, 0.5801253318786621, 0.3950767517089844);
		assertElement(pi, PathElementType.LINE_TO, 5.119140625, 0.7249107360839844, 0.38028717041015625);
		assertElement(pi, PathElementType.LINE_TO, 5.1220703125, 0.7283744812011719, 0.3829078674316406);
		assertElement(pi, PathElementType.LINE_TO, 5.125, 0.78125, 0.1875);
		assertElement(pi, PathElementType.LINE_TO, 5.1279296875, 0.188188374042511, 0.1876017451286316);
		assertElement(pi, PathElementType.LINE_TO, 5.130859375, 0.1902146339416504, 0.18790197372436523);
		assertElement(pi, PathElementType.LINE_TO, 5.13671875, 0.19804763793945312, 0.18906784057617188);
		assertElement(pi, PathElementType.LINE_TO, 5.1484375, 0.227203369140625, 0.193450927734375);
		assertElement(pi, PathElementType.LINE_TO, 5.16015625, 0.2670173645019531, 0.21340179443359375);
		assertElement(pi, PathElementType.LINE_TO, 5.166015625, 0.2904367446899414, 0.2285480499267578);
		assertElement(pi, PathElementType.LINE_TO, 5.1689453125, 0.30314040184020996, 0.23764562606811523);
		assertElement(pi, PathElementType.LINE_TO, 5.171875, 0.326416015625, 0.208740234375);
		assertElement(pi, PathElementType.LINE_TO, 5.177734375, 0.24894094467163086, 0.21769237518310547);
		assertElement(pi, PathElementType.LINE_TO, 5.1806640625, 0.28333544731140137, 0.2386493682861328);
		assertElement(pi, PathElementType.LINE_TO, 5.18359375, 0.3270530700683594, 0.23805999755859375);
		assertElement(pi, PathElementType.LINE_TO, 5.1865234375, 0.31568264961242676, 0.26050448417663574);
		assertElement(pi, PathElementType.LINE_TO, 5.189453125, 0.38053131103515625, 0.2892875671386719);
		assertElement(pi, PathElementType.LINE_TO, 5.1923828125, 0.4046480655670166, 0.3110384941101074);
		assertElement(pi, PathElementType.LINE_TO, 5.1953125, 0.440582275390625, 0.27410888671875);
		assertElement(pi, PathElementType.LINE_TO, 5.201171875, 0.3981008529663086, 0.2978382110595703);
		assertElement(pi, PathElementType.LINE_TO, 5.2041015625, 0.44664525985717773, 0.346221923828125);
		assertElement(pi, PathElementType.LINE_TO, 5.20703125, 0.49887847900390625, 0.3167266845703125);
		assertElement(pi, PathElementType.LINE_TO, 5.2099609375, 0.4370872974395752, 0.3343977928161621);
		assertElement(pi, PathElementType.LINE_TO, 5.212890625, 0.5278606414794922, 0.3395805358886719);
		assertElement(pi, PathElementType.LINE_TO, 5.2158203125, 0.5421929359436035, 0.35115909576416016);
		assertElement(pi, PathElementType.LINE_TO, 5.21875, 0.583984375, 0.251953125);
		assertElement(pi, PathElementType.LINE_TO, 5.2421875, 0.517791748046875, 0.32110595703125);
		assertElement(pi, PathElementType.LINE_TO, 5.248046875, 0.4877433776855469, 0.37044715881347656);
		assertElement(pi, PathElementType.LINE_TO, 5.2509765625, 0.5546653270721436, 0.4449009895324707);
		assertElement(pi, PathElementType.LINE_TO, 5.25390625, 0.6216888427734375, 0.4337158203125);
		assertElement(pi, PathElementType.LINE_TO, 5.2568359375, 0.5647876262664795, 0.4584999084472656);
		assertElement(pi, PathElementType.LINE_TO, 5.259765625, 0.6642436981201172, 0.4763374328613281);
		assertElement(pi, PathElementType.LINE_TO, 5.2626953125, 0.6828536987304688, 0.4935894012451172);
		assertElement(pi, PathElementType.LINE_TO, 5.265625, 0.726806640625, 0.39794921875);
		assertElement(pi, PathElementType.LINE_TO, 5.2685546875, 0.4199877977371216, 0.4012415409088135);
		assertElement(pi, PathElementType.LINE_TO, 5.271484375, 0.4750833511352539, 0.4096851348876953);
		assertElement(pi, PathElementType.LINE_TO, 5.2744140625, 0.5385308265686035, 0.4459228515625);
		assertElement(pi, PathElementType.LINE_TO, 5.27734375, 0.6183242797851562, 0.4334259033203125);
		assertElement(pi, PathElementType.LINE_TO, 5.2802734375, 0.5815939903259277, 0.4718003273010254);
		assertElement(pi, PathElementType.LINE_TO, 5.283203125, 0.6983489990234375, 0.5142288208007812);
		assertElement(pi, PathElementType.LINE_TO, 5.2861328125, 0.7298083305358887, 0.5420846939086914);
		assertElement(pi, PathElementType.LINE_TO, 5.2890625, 0.78240966796875, 0.4481201171875);
		assertElement(pi, PathElementType.LINE_TO, 5.2919921875, 0.5211031436920166, 0.4567399024963379);
		assertElement(pi, PathElementType.LINE_TO, 5.294921875, 0.6559543609619141, 0.4726295471191406);
		assertElement(pi, PathElementType.LINE_TO, 5.2978515625, 0.7284908294677734, 0.5436534881591797);
		assertElement(pi, PathElementType.LINE_TO, 5.30078125, 0.8057098388671875, 0.466400146484375);
		assertElement(pi, PathElementType.LINE_TO, 5.3037109375, 0.6710782051086426, 0.48720264434814453);
		assertElement(pi, PathElementType.LINE_TO, 5.306640625, 0.8160972595214844, 0.47356414794921875);
		assertElement(pi, PathElementType.LINE_TO, 5.3095703125, 0.8209409713745117, 0.47658348083496094);
		assertElement(pi, PathElementType.LINE_TO, 5.3125, 0.875, 0.28125);
		assertElement(pi, PathElementType.LINE_TO, 5.3154296875, 0.2837638258934021, 0.28155040740966797);
		assertElement(pi, PathElementType.LINE_TO, 5.318359375, 0.2910151481628418, 0.28241729736328125);
		assertElement(pi, PathElementType.LINE_TO, 5.3212890625, 0.3014940023422241, 0.2872445583343506);
		assertElement(pi, PathElementType.LINE_TO, 5.32421875, 0.3179893493652344, 0.28564453125);
		assertElement(pi, PathElementType.LINE_TO, 5.330078125, 0.3546476364135742, 0.3033123016357422);
		assertElement(pi, PathElementType.LINE_TO, 5.3330078125, 0.3761787414550781, 0.31685924530029297);
		assertElement(pi, PathElementType.LINE_TO, 5.3359375, 0.409637451171875, 0.296630859375);
		assertElement(pi, PathElementType.LINE_TO, 5.34765625, 0.5142135620117188, 0.3541717529296875);
		assertElement(pi, PathElementType.LINE_TO, 5.3505859375, 0.47205400466918945, 0.37591981887817383);
		assertElement(pi, PathElementType.LINE_TO, 5.353515625, 0.5673599243164062, 0.39182281494140625);
		assertElement(pi, PathElementType.LINE_TO, 5.3564453125, 0.5936999320983887, 0.4119729995727539);
		assertElement(pi, PathElementType.LINE_TO, 5.359375, 0.646240234375, 0.3251953125);
		assertElement(pi, PathElementType.LINE_TO, 5.37109375, 0.5764541625976562, 0.386932373046875);
		assertElement(pi, PathElementType.LINE_TO, 5.3740234375, 0.5460402965545654, 0.4330720901489258);
		assertElement(pi, PathElementType.LINE_TO, 5.376953125, 0.6734905242919922, 0.4909934997558594);
		assertElement(pi, PathElementType.LINE_TO, 5.3798828125, 0.7129297256469727, 0.5297813415527344);
		assertElement(pi, PathElementType.LINE_TO, 5.3828125, 0.77203369140625, 0.4503173828125);
		assertElement(pi, PathElementType.LINE_TO, 5.3857421875, 0.5247106552124023, 0.4610104560852051);
		assertElement(pi, PathElementType.LINE_TO, 5.388671875, 0.6627044677734375, 0.4821891784667969);
		assertElement(pi, PathElementType.LINE_TO, 5.3916015625, 0.7391171455383301, 0.558588981628418);
		assertElement(pi, PathElementType.LINE_TO, 5.39453125, 0.819671630859375, 0.4906005859375);
		assertElement(pi, PathElementType.LINE_TO, 5.3974609375, 0.6935582160949707, 0.5132904052734375);
		assertElement(pi, PathElementType.LINE_TO, 5.400390625, 0.8391609191894531, 0.5041427612304688);
		assertElement(pi, PathElementType.LINE_TO, 5.4033203125, 0.8476943969726562, 0.5090065002441406);
		assertElement(pi, PathElementType.LINE_TO, 5.40625, 0.904296875, 0.31640625);
		assertElement(pi, PathElementType.LINE_TO, 5.4091796875, 0.32581377029418945, 0.31740689277648926);
		assertElement(pi, PathElementType.LINE_TO, 5.412109375, 0.3517951965332031, 0.32015419006347656);
		assertElement(pi, PathElementType.LINE_TO, 5.4150390625, 0.38703370094299316, 0.33682775497436523);
		assertElement(pi, PathElementType.LINE_TO, 5.41796875, 0.440032958984375, 0.3293609619140625);
		assertElement(pi, PathElementType.LINE_TO, 5.423828125, 0.5403919219970703, 0.3835258483886719);
		assertElement(pi, PathElementType.LINE_TO, 5.4267578125, 0.5913138389587402, 0.4190511703491211);
		assertElement(pi, PathElementType.LINE_TO, 5.4296875, 0.66748046875, 0.3519287109375);
		assertElement(pi, PathElementType.LINE_TO, 5.435546875, 0.5967159271240234, 0.4105186462402344);
		assertElement(pi, PathElementType.LINE_TO, 5.4384765625, 0.6908340454101562, 0.5109004974365234);
		assertElement(pi, PathElementType.LINE_TO, 5.44140625, 0.7866363525390625, 0.468353271484375);
		assertElement(pi, PathElementType.LINE_TO, 5.4443359375, 0.6772971153259277, 0.4987802505493164);
		assertElement(pi, PathElementType.LINE_TO, 5.447265625, 0.8313331604003906, 0.5047683715820312);
		assertElement(pi, PathElementType.LINE_TO, 5.4501953125, 0.8494634628295898, 0.5165309906005859);
		assertElement(pi, PathElementType.LINE_TO, 5.453125, 0.9130859375, 0.328125);
		assertElement(pi, PathElementType.LINE_TO, 5.4560546875, 0.3630239963531494, 0.3316469192504883);
		assertElement(pi, PathElementType.LINE_TO, 5.458984375, 0.4500293731689453, 0.34023284912109375);
		assertElement(pi, PathElementType.LINE_TO, 5.4619140625, 0.5488791465759277, 0.39319896697998047);
		assertElement(pi, PathElementType.LINE_TO, 5.46484375, 0.6742095947265625, 0.3607177734375);
		assertElement(pi, PathElementType.LINE_TO, 5.4677734375, 0.6031804084777832, 0.4181976318359375);
		assertElement(pi, PathElementType.LINE_TO, 5.470703125, 0.7911109924316406, 0.47409820556640625);
		assertElement(pi, PathElementType.LINE_TO, 5.4736328125, 0.8348350524902344, 0.5091972351074219);
		assertElement(pi, PathElementType.LINE_TO, 5.4765625, 0.9156494140625, 0.331787109375);
		assertElement(pi, PathElementType.LINE_TO, 5.4794921875, 0.4531278610229492, 0.34362316131591797);
		assertElement(pi, PathElementType.LINE_TO, 5.482421875, 0.6762466430664062, 0.36344146728515625);
		assertElement(pi, PathElementType.LINE_TO, 5.4853515625, 0.7924356460571289, 0.4758434295654297);
		assertElement(pi, PathElementType.LINE_TO, 5.48828125, 0.9163818359375, 0.3328857421875);
		assertElement(pi, PathElementType.LINE_TO, 5.494140625, 0.9165878295898438, 0.3332061767578125);
		assertElement(pi, PathElementType.LINE_TO, 5.4970703125, 0.9166450500488281, 0.3332977294921875);
		assertElement(pi, PathElementType.LINE_TO, 5.5, 1.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 5.5029296875, 4.5552849769592285E-5, 1.1414289474487305E-5);
		assertElement(pi, PathElementType.LINE_TO, 5.505859375, 1.8131732940673828E-4, 4.553794860839844E-5);
		assertElement(pi, PathElementType.LINE_TO, 5.51171875, 7.181167602539062E-4, 1.811981201171875E-4);
		assertElement(pi, PathElementType.LINE_TO, 5.5234375, 0.00281524658203125, 7.171630859375E-4);
		assertElement(pi, PathElementType.LINE_TO, 5.546875, 0.01080322265625, 0.0028076171875);
		assertElement(pi, PathElementType.LINE_TO, 5.59375, 0.03955078125, 0.0107421875);
		assertElement(pi, PathElementType.LINE_TO, 5.6875, 0.12890625, 0.0390625);
		assertElement(pi, PathElementType.LINE_TO, 5.78125, 0.212890625, 0.109375);
		assertElement(pi, PathElementType.LINE_TO, 5.8046875, 0.186370849609375, 0.12744140625);
		assertElement(pi, PathElementType.LINE_TO, 5.81640625, 0.21587371826171875, 0.1593475341796875);
		assertElement(pi, PathElementType.LINE_TO, 5.8193359375, 0.1983790397644043, 0.16651582717895508);
		assertElement(pi, PathElementType.LINE_TO, 5.822265625, 0.22784423828125, 0.17131805419921875);
		assertElement(pi, PathElementType.LINE_TO, 5.8251953125, 0.23303556442260742, 0.17611026763916016);
		assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.245849609375, 0.14697265625);
		assertElement(pi, PathElementType.LINE_TO, 5.83984375, 0.2116241455078125, 0.1578216552734375);
		assertElement(pi, PathElementType.LINE_TO, 5.845703125, 0.2348155975341797, 0.18191909790039062);
		assertElement(pi, PathElementType.LINE_TO, 5.8486328125, 0.24385309219360352, 0.1902475357055664);
		assertElement(pi, PathElementType.LINE_TO, 5.8515625, 0.2587890625, 0.163818359375);
		assertElement(pi, PathElementType.LINE_TO, 5.857421875, 0.2223834991455078, 0.17128753662109375);
		assertElement(pi, PathElementType.LINE_TO, 5.8603515625, 0.2427058219909668, 0.1916799545288086);
		assertElement(pi, PathElementType.LINE_TO, 5.86328125, 0.2640533447265625, 0.171112060546875);
		assertElement(pi, PathElementType.LINE_TO, 5.869140625, 0.26630401611328125, 0.17431640625);
		assertElement(pi, PathElementType.LINE_TO, 5.8720703125, 0.26731395721435547, 0.1757678985595703);
		assertElement(pi, PathElementType.LINE_TO, 5.875, 0.28125, 0.125);
		assertElement(pi, PathElementType.LINE_TO, 5.8779296875, 0.1251349151134491, 0.12505674362182617);
		assertElement(pi, PathElementType.LINE_TO, 5.880859375, 0.12553000450134277, 0.12522506713867188);
		assertElement(pi, PathElementType.LINE_TO, 5.88671875, 0.1270427703857422, 0.125885009765625);
		assertElement(pi, PathElementType.LINE_TO, 5.8984375, 0.1325531005859375, 0.12841796875);
		assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.1502685546875, 0.1376953125);
		assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.1865234375, 0.16796875);
		assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.1484375, 0.234375);
		assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.00390625, 0.203125);
		assertElement(pi, PathElementType.LINE_TO, 6.1796875, 0.04583740234375, 0.17578125);
		assertElement(pi, PathElementType.LINE_TO, 6.185546875, 0.06836509704589844, 0.1476287841796875);
		assertElement(pi, PathElementType.LINE_TO, 6.1884765625, 0.025332927703857422, 0.10175609588623047);
		assertElement(pi, PathElementType.LINE_TO, 6.19140625, -0.0189971923828125, 0.115447998046875);
		assertElement(pi, PathElementType.LINE_TO, 6.1943359375, 0.023222923278808594, 0.09995746612548828);
		assertElement(pi, PathElementType.LINE_TO, 6.197265625, -0.046966552734375, 0.0920257568359375);
		assertElement(pi, PathElementType.LINE_TO, 6.2001953125, -0.05970478057861328, 0.08226966857910156);
		assertElement(pi, PathElementType.LINE_TO, 6.203125, -0.09228515625, 0.1572265625);
		assertElement(pi, PathElementType.LINE_TO, 6.21484375, -0.013519287109375, 0.132171630859375);
		assertElement(pi, PathElementType.LINE_TO, 6.2177734375, 0.015245914459228516, 0.1024923324584961);
		assertElement(pi, PathElementType.LINE_TO, 6.220703125, -0.07762527465820312, 0.07028961181640625);
		assertElement(pi, PathElementType.LINE_TO, 6.2236328125, -0.1035909652709961, 0.04836463928222656);
		assertElement(pi, PathElementType.LINE_TO, 6.2265625, -0.1474609375, 0.12451171875);
		assertElement(pi, PathElementType.LINE_TO, 6.2294921875, 0.06384515762329102, 0.11707687377929688);
		assertElement(pi, PathElementType.LINE_TO, 6.232421875, -0.048717498779296875, 0.1030731201171875);
		assertElement(pi, PathElementType.LINE_TO, 6.2353515625, -0.11056995391845703, 0.04298973083496094);
		assertElement(pi, PathElementType.LINE_TO, 6.23828125, -0.177093505859375, 0.10516357421875);
		assertElement(pi, PathElementType.LINE_TO, 6.2412109375, -0.067962646484375, 0.08630561828613281);
		assertElement(pi, PathElementType.LINE_TO, 6.244140625, -0.1924896240234375, 0.0946044921875);
		assertElement(pi, PathElementType.LINE_TO, 6.2470703125, -0.2003498077392578, 0.08906936645507812);
		assertElement(pi, PathElementType.LINE_TO, 6.25, -0.25, 0.25);
		assertElement(pi, PathElementType.LINE_TO, 6.2529296875, 0.24964727461338043, 0.24993166327476501);
		assertElement(pi, PathElementType.LINE_TO, 6.255859375, 0.24859726428985596, 0.24972796440124512);
		assertElement(pi, PathElementType.LINE_TO, 6.26171875, 0.24445438385009766, 0.24892234802246094);
		assertElement(pi, PathElementType.LINE_TO, 6.2734375, 0.22834014892578125, 0.2457733154296875);
		assertElement(pi, PathElementType.LINE_TO, 6.296875, 0.16754150390625, 0.2337646484375);
		assertElement(pi, PathElementType.LINE_TO, 6.3203125, 0.0821533203125, 0.18927001953125);
		assertElement(pi, PathElementType.LINE_TO, 6.326171875, 0.1042027473449707, 0.1717967987060547);
		assertElement(pi, PathElementType.LINE_TO, 6.3291015625, 0.06936156749725342, 0.1376206874847412);
		assertElement(pi, PathElementType.LINE_TO, 6.33203125, 0.031101226806640625, 0.15538787841796875);
		assertElement(pi, PathElementType.LINE_TO, 6.3349609375, 0.06987380981445312, 0.141648530960083);
		assertElement(pi, PathElementType.LINE_TO, 6.337890625, 0.0031681060791015625, 0.1349029541015625);
		assertElement(pi, PathElementType.LINE_TO, 6.3408203125, -0.011479616165161133, 0.12361574172973633);
		assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.04638671875, 0.1904296875);
		assertElement(pi, PathElementType.LINE_TO, 6.3466796875, 0.18406754732131958, 0.18902736902236938);
		assertElement(pi, PathElementType.LINE_TO, 6.349609375, 0.16636896133422852, 0.18503618240356445);
		assertElement(pi, PathElementType.LINE_TO, 6.35546875, 0.10528945922851562, 0.17058181762695312);
		assertElement(pi, PathElementType.LINE_TO, 6.361328125, 0.03200674057006836, 0.12552356719970703);
		assertElement(pi, PathElementType.LINE_TO, 6.3642578125, -0.006599545478820801, 0.09582877159118652);
		assertElement(pi, PathElementType.LINE_TO, 6.3671875, -0.061309814453125, 0.124847412109375);
		assertElement(pi, PathElementType.LINE_TO, 6.373046875, -0.03978300094604492, 0.0764608383178711);
		assertElement(pi, PathElementType.LINE_TO, 6.3759765625, -0.1081535816192627, 0.0025148391723632812);
		assertElement(pi, PathElementType.LINE_TO, 6.37890625, -0.17862319946289062, 0.01319122314453125);
		assertElement(pi, PathElementType.LINE_TO, 6.3818359375, -0.12472736835479736, -0.013833284378051758);
		assertElement(pi, PathElementType.LINE_TO, 6.384765625, -0.2325429916381836, -0.03545188903808594);
		assertElement(pi, PathElementType.LINE_TO, 6.3876953125, -0.2581756114959717, -0.057691097259521484);
		assertElement(pi, PathElementType.LINE_TO, 6.390625, -0.31201171875, 0.03857421875);
		assertElement(pi, PathElementType.LINE_TO, 6.40234375, -0.23144149780273438, -0.0161590576171875);
		assertElement(pi, PathElementType.LINE_TO, 6.4052734375, -0.19502747058868408, -0.06515741348266602);
		assertElement(pi, PathElementType.LINE_TO, 6.408203125, -0.33951282501220703, -0.12374305725097656);
		assertElement(pi, PathElementType.LINE_TO, 6.4111328125, -0.38504648208618164, -0.1649627685546875);
		assertElement(pi, PathElementType.LINE_TO, 6.4140625, -0.456512451171875, -0.06549072265625);
		assertElement(pi, PathElementType.LINE_TO, 6.4169921875, -0.1575603485107422, -0.07907509803771973);
		assertElement(pi, PathElementType.LINE_TO, 6.419921875, -0.3292808532714844, -0.10658836364746094);
		assertElement(pi, PathElementType.LINE_TO, 6.4228515625, -0.4269134998321533, -0.2034296989440918);
		assertElement(pi, PathElementType.LINE_TO, 6.42578125, -0.5313873291015625, -0.12396240234375);
		assertElement(pi, PathElementType.LINE_TO, 6.4287109375, -0.3811056613922119, -0.1563568115234375);
		assertElement(pi, PathElementType.LINE_TO, 6.431640625, -0.5694065093994141, -0.15465927124023438);
		assertElement(pi, PathElementType.LINE_TO, 6.4345703125, -0.5885429382324219, -0.1703357696533203);
		assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.66796875, 0.0546875);
		assertElement(pi, PathElementType.LINE_TO, 6.4404296875, 0.0502932071685791, 0.05370527505874634);
		assertElement(pi, PathElementType.LINE_TO, 6.443359375, 0.03759193420410156, 0.05083513259887695);
		assertElement(pi, PathElementType.LINE_TO, 6.44921875, -0.0098419189453125, 0.039890289306640625);
		assertElement(pi, PathElementType.LINE_TO, 6.455078125, -0.07561826705932617, 0.0037717819213867188);
		assertElement(pi, PathElementType.LINE_TO, 6.4580078125, -0.11453402042388916, -0.02309250831604004);
		assertElement(pi, PathElementType.LINE_TO, 6.4609375, -0.172607421875, 3.96728515625E-4);
		assertElement(pi, PathElementType.LINE_TO, 6.4638671875, -0.06220799684524536, -0.014894843101501465);
		assertElement(pi, PathElementType.LINE_TO, 6.466796875, -0.18439531326293945, -0.050421714782714844);
		assertElement(pi, PathElementType.LINE_TO, 6.4697265625, -0.269977331161499, -0.13389873504638672);
		assertElement(pi, PathElementType.LINE_TO, 6.47265625, -0.3657493591308594, -0.12009429931640625);
		assertElement(pi, PathElementType.LINE_TO, 6.4755859375, -0.30961501598358154, -0.1601407527923584);
		assertElement(pi, PathElementType.LINE_TO, 6.478515625, -0.4664011001586914, -0.19861793518066406);
		assertElement(pi, PathElementType.LINE_TO, 6.4814453125, -0.5171148777008057, -0.24118471145629883);
		assertElement(pi, PathElementType.LINE_TO, 6.484375, -0.60791015625, -0.123291015625);
		assertElement(pi, PathElementType.LINE_TO, 6.4873046875, -0.16485315561294556, -0.13364088535308838);
		assertElement(pi, PathElementType.LINE_TO, 6.490234375, -0.26926088333129883, -0.16136646270751953);
		assertElement(pi, PathElementType.LINE_TO, 6.4931640625, -0.3926093578338623, -0.24071598052978516);
		assertElement(pi, PathElementType.LINE_TO, 6.49609375, -0.5449409484863281, -0.24900054931640625);
		assertElement(pi, PathElementType.LINE_TO, 6.4990234375, -0.5071747303009033, -0.32967114448547363);
		assertElement(pi, PathElementType.LINE_TO, 6.501953125, -0.7176170349121094, -0.4387474060058594);
		assertElement(pi, PathElementType.LINE_TO, 6.5048828125, -0.7905957698822021, -0.5141148567199707);
		assertElement(pi, PathElementType.LINE_TO, 6.5078125, -0.893035888671875, -0.41339111328125);
		assertElement(pi, PathElementType.LINE_TO, 6.5107421875, -0.5323711633682251, -0.4346444606781006);
		assertElement(pi, PathElementType.LINE_TO, 6.513671875, -0.7545862197875977, -0.4799633026123047);
		assertElement(pi, PathElementType.LINE_TO, 6.5166015625, -0.8833432197570801, -0.6127204895019531);
		assertElement(pi, PathElementType.LINE_TO, 6.51953125, -1.0181503295898438, -0.5321502685546875);
		assertElement(pi, PathElementType.LINE_TO, 6.5224609375, -0.8451278209686279, -0.5760102272033691);
		assertElement(pi, PathElementType.LINE_TO, 6.525390625, -1.0758075714111328, -0.5838813781738281);
		assertElement(pi, PathElementType.LINE_TO, 6.5283203125, -1.1032767295837402, -0.6075658798217773);
		assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.201171875, -0.34375);
		assertElement(pi, PathElementType.LINE_TO, 6.5341796875, -0.3602370619773865, -0.34648728370666504);
		assertElement(pi, PathElementType.LINE_TO, 6.537109375, -0.4058833122253418, -0.35418128967285156);
		assertElement(pi, PathElementType.LINE_TO, 6.5400390625, -0.46850359439849854, -0.38622212409973145);
		assertElement(pi, PathElementType.LINE_TO, 6.54296875, -0.5617637634277344, -0.3813323974609375);
		assertElement(pi, PathElementType.LINE_TO, 6.5458984375, -0.5653444528579712, -0.4280095100402832);
		assertElement(pi, PathElementType.LINE_TO, 6.548828125, -0.7430582046508789, -0.4869861602783203);
		assertElement(pi, PathElementType.LINE_TO, 6.5517578125, -0.8363900184631348, -0.5560798645019531);
		assertElement(pi, PathElementType.LINE_TO, 6.5546875, -0.971649169921875, -0.4609375);
		assertElement(pi, PathElementType.LINE_TO, 6.5576171875, -0.6063855886459351, -0.4954352378845215);
		assertElement(pi, PathElementType.LINE_TO, 6.560546875, -0.8797178268432617, -0.5731430053710938);
		assertElement(pi, PathElementType.LINE_TO, 6.5634765625, -1.0460031032562256, -0.753049373626709);
		assertElement(pi, PathElementType.LINE_TO, 6.56640625, -1.2152328491210938, -0.7034759521484375);
		assertElement(pi, PathElementType.LINE_TO, 6.5693359375, -1.0524744987487793, -0.7625565528869629);
		assertElement(pi, PathElementType.LINE_TO, 6.572265625, -1.3158111572265625, -0.7933273315429688);
		assertElement(pi, PathElementType.LINE_TO, 6.5751953125, -1.3601088523864746, -0.8290224075317383);
		assertElement(pi, PathElementType.LINE_TO, 6.578125, -1.477294921875, -0.54736328125);
		assertElement(pi, PathElementType.LINE_TO, 6.5810546875, -0.6083686351776123, -0.555593729019165);
		assertElement(pi, PathElementType.LINE_TO, 6.583984375, -0.7609539031982422, -0.5765171051025391);
		assertElement(pi, PathElementType.LINE_TO, 6.5869140625, -0.9364182949066162, -0.6747708320617676);
		assertElement(pi, PathElementType.LINE_TO, 6.58984375, -1.1582794189453125, -0.6338348388671875);
		assertElement(pi, PathElementType.LINE_TO, 6.5927734375, -1.0513756275177002, -0.7392611503601074);
		assertElement(pi, PathElementType.LINE_TO, 6.595703125, -1.380514144897461, -0.8523750305175781);
		assertElement(pi, PathElementType.LINE_TO, 6.5986328125, -1.4684109687805176, -0.9274492263793945);
		assertElement(pi, PathElementType.LINE_TO, 6.6015625, -1.61865234375, -0.652099609375);
		assertElement(pi, PathElementType.LINE_TO, 6.6044921875, -0.8632004261016846, -0.6768703460693359);
		assertElement(pi, PathElementType.LINE_TO, 6.607421875, -1.253671646118164, -0.7226181030273438);
		assertElement(pi, PathElementType.LINE_TO, 6.6103515625, -1.4647154808044434, -0.9285612106323242);
		assertElement(pi, PathElementType.LINE_TO, 6.61328125, -1.6903839111328125, -0.705657958984375);
		assertElement(pi, PathElementType.LINE_TO, 6.6162109375, -1.3021087646484375, -0.7679224014282227);
		assertElement(pi, PathElementType.LINE_TO, 6.619140625, -1.7265701293945312, -0.73284912109375);
		assertElement(pi, PathElementType.LINE_TO, 6.6220703125, -1.7447576522827148, -0.7465763092041016);
		assertElement(pi, PathElementType.LINE_TO, 6.625, -1.90625, -0.1875);
		assertElement(pi, PathElementType.LINE_TO, 6.6279296875, -0.18999579548835754, -0.187930166721344);
		assertElement(pi, PathElementType.LINE_TO, 6.630859375, -0.19734978675842285, -0.18920183181762695);
		assertElement(pi, PathElementType.LINE_TO, 6.63671875, -0.2258319854736328, -0.19415664672851562);
		assertElement(pi, PathElementType.LINE_TO, 6.642578125, -0.26738643646240234, -0.21464157104492188);
		assertElement(pi, PathElementType.LINE_TO, 6.6455078125, -0.2929013967514038, -0.23090195655822754);
		assertElement(pi, PathElementType.LINE_TO, 6.6484375, -0.3322906494140625, -0.212921142578125);
		assertElement(pi, PathElementType.LINE_TO, 6.66015625, -0.47914886474609375, -0.288482666015625);
		assertElement(pi, PathElementType.LINE_TO, 6.6630859375, -0.43821895122528076, -0.31856822967529297);
		assertElement(pi, PathElementType.LINE_TO, 6.666015625, -0.566004753112793, -0.3456897735595703);
		assertElement(pi, PathElementType.LINE_TO, 6.6689453125, -0.6132535934448242, -0.3800783157348633);
		assertElement(pi, PathElementType.LINE_TO, 6.671875, -0.6983642578125, -0.279541015625);
		assertElement(pi, PathElementType.LINE_TO, 6.6748046875, -0.32094407081604004, -0.2886608839035034);
		assertElement(pi, PathElementType.LINE_TO, 6.677734375, -0.42590904235839844, -0.3130521774291992);
		assertElement(pi, PathElementType.LINE_TO, 6.6806640625, -0.5514625310897827, -0.3900282382965088);
		assertElement(pi, PathElementType.LINE_TO, 6.68359375, -0.7110595703125, -0.38983917236328125);
		assertElement(pi, PathElementType.LINE_TO, 6.6865234375, -0.6719332933425903, -0.47227025032043457);
		assertElement(pi, PathElementType.LINE_TO, 6.689453125, -0.9085264205932617, -0.5792102813720703);
		assertElement(pi, PathElementType.LINE_TO, 6.6923828125, -0.9982402324676514, -0.6605086326599121);
		assertElement(pi, PathElementType.LINE_TO, 6.6953125, -1.13092041015625, -0.53076171875);
		assertElement(pi, PathElementType.LINE_TO, 6.6982421875, -0.6879080533981323, -0.5591864585876465);
		assertElement(pi, PathElementType.LINE_TO, 6.701171875, -0.9843740463256836, -0.6204071044921875);
		assertElement(pi, PathElementType.LINE_TO, 6.7041015625, -1.1636149883270264, -0.7993769645690918);
		assertElement(pi, PathElementType.LINE_TO, 6.70703125, -1.3567123413085938, -0.6969146728515625);
		assertElement(pi, PathElementType.LINE_TO, 6.7099609375, -1.136803150177002, -0.763486385345459);
		assertElement(pi, PathElementType.LINE_TO, 6.712890625, -1.47064208984375, -0.7874374389648438);
		assertElement(pi, PathElementType.LINE_TO, 6.7158203125, -1.527529239654541, -0.833888053894043);
		assertElement(pi, PathElementType.LINE_TO, 6.71875, -1.6845703125, -0.478515625);
		assertElement(pi, PathElementType.LINE_TO, 6.7216796875, -0.5051213502883911, -0.48448169231414795);
		assertElement(pi, PathElementType.LINE_TO, 6.724609375, -0.5788202285766602, -0.5014104843139648);
		assertElement(pi, PathElementType.LINE_TO, 6.7275390625, -0.6807585954666138, -0.5574166774749756);
		assertElement(pi, PathElementType.LINE_TO, 6.73046875, -0.8307876586914062, -0.5623397827148438);
		assertElement(pi, PathElementType.LINE_TO, 6.7333984375, -0.8480333089828491, -0.6408708095550537);
		assertElement(pi, PathElementType.LINE_TO, 6.736328125, -1.1274423599243164, -0.7483196258544922);
		assertElement(pi, PathElementType.LINE_TO, 6.7392578125, -1.280916452407837, -0.8687520027160645);
		assertElement(pi, PathElementType.LINE_TO, 6.7421875, -1.49603271484375, -0.75177001953125);
		assertElement(pi, PathElementType.LINE_TO, 6.7451171875, -0.97319495677948, -0.809668779373169);
		assertElement(pi, PathElementType.LINE_TO, 6.748046875, -1.3902616500854492, -0.9427967071533203);
		assertElement(pi, PathElementType.LINE_TO, 6.7509765625, -1.64882230758667, -1.229766845703125);
		assertElement(pi, PathElementType.LINE_TO, 6.75390625, -1.9088516235351562, -1.1913299560546875);
		assertElement(pi, PathElementType.LINE_TO, 6.7568359375, -1.6964914798736572, -1.2889370918273926);
		assertElement(pi, PathElementType.LINE_TO, 6.759765625, -2.0826549530029297, -1.3632011413574219);
		assertElement(pi, PathElementType.LINE_TO, 6.7626953125, -2.1603236198425293, -1.4349966049194336);
		assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.3359375, -1.0751953125);
		assertElement(pi, PathElementType.LINE_TO, 6.7685546875, -1.1625326871871948, -1.0890836715698242);
		assertElement(pi, PathElementType.LINE_TO, 6.771484375, -1.381178855895996, -1.1249847412109375);
		assertElement(pi, PathElementType.LINE_TO, 6.7744140625, -1.6340606212615967, -1.271064281463623);
		assertElement(pi, PathElementType.LINE_TO, 6.77734375, -1.9522018432617188, -1.228240966796875);
		assertElement(pi, PathElementType.LINE_TO, 6.7802734375, -1.814516305923462, -1.3828973770141602);
		assertElement(pi, PathElementType.LINE_TO, 6.783203125, -2.279703140258789, -1.5583992004394531);
		assertElement(pi, PathElementType.LINE_TO, 6.7861328125, -2.4112024307250977, -1.6760330200195312);
		assertElement(pi, PathElementType.LINE_TO, 6.7890625, -2.62640380859375, -1.3184814453125);
		assertElement(pi, PathElementType.LINE_TO, 6.7919921875, -1.6102542877197266, -1.3554692268371582);
		assertElement(pi, PathElementType.LINE_TO, 6.794921875, -2.1508560180664062, -1.4259452819824219);
		assertElement(pi, PathElementType.LINE_TO, 6.7978515625, -2.4465508460998535, -1.7166051864624023);
		assertElement(pi, PathElementType.LINE_TO, 6.80078125, -2.761871337890625, -1.4244384765625);
		assertElement(pi, PathElementType.LINE_TO, 6.8037109375, -2.241894245147705, -1.5132827758789062);
		assertElement(pi, PathElementType.LINE_TO, 6.806640625, -2.826923370361328, -1.4730148315429688);
		assertElement(pi, PathElementType.LINE_TO, 6.8095703125, -2.8587188720703125, -1.4960823059082031);
		assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.0859375, -0.734375);
		assertElement(pi, PathElementType.LINE_TO, 6.8154296875, -0.7457404732704163, -0.7360502481460571);
		assertElement(pi, PathElementType.LINE_TO, 6.818359375, -0.7785611152648926, -0.740910530090332);
		assertElement(pi, PathElementType.LINE_TO, 6.8212890625, -0.8262169361114502, -0.7636795043945312);
		assertElement(pi, PathElementType.LINE_TO, 6.82421875, -0.9009132385253906, -0.7591934204101562);
		assertElement(pi, PathElementType.LINE_TO, 6.8271484375, -0.9114580154418945, -0.7962543964385986);
		assertElement(pi, PathElementType.LINE_TO, 6.830078125, -1.0684070587158203, -0.8428497314453125);
		assertElement(pi, PathElementType.LINE_TO, 6.8330078125, -1.1671044826507568, -0.9065194129943848);
		assertElement(pi, PathElementType.LINE_TO, 6.8359375, -1.318878173828125, -0.82305908203125);
		assertElement(pi, PathElementType.LINE_TO, 6.8388671875, -0.9927825927734375, -0.8607804775238037);
		assertElement(pi, PathElementType.LINE_TO, 6.841796875, -1.3227577209472656, -0.9465885162353516);
		assertElement(pi, PathElementType.LINE_TO, 6.8447265625, -1.5491111278533936, -1.162865161895752);
		assertElement(pi, PathElementType.LINE_TO, 6.84765625, -1.8038177490234375, -1.09832763671875);
		assertElement(pi, PathElementType.LINE_TO, 6.8505859375, -1.6246917247772217, -1.199014663696289);
		assertElement(pi, PathElementType.LINE_TO, 6.853515625, -2.053403854370117, -1.2791938781738281);
		assertElement(pi, PathElementType.LINE_TO, 6.8564453125, -2.178163528442383, -1.376810073852539);
		assertElement(pi, PathElementType.LINE_TO, 6.859375, -2.419189453125, -1.00439453125);
		assertElement(pi, PathElementType.LINE_TO, 6.8623046875, -1.1180541515350342, -1.0294749736785889);
		assertElement(pi, PathElementType.LINE_TO, 6.865234375, -1.4031505584716797, -1.0959758758544922);
		assertElement(pi, PathElementType.LINE_TO, 6.8681640625, -1.7374632358551025, -1.3040156364440918);
		assertElement(pi, PathElementType.LINE_TO, 6.87109375, -2.1523590087890625, -1.3007965087890625);
		assertElement(pi, PathElementType.LINE_TO, 6.8740234375, -2.0235698223114014, -1.514051914215088);
		assertElement(pi, PathElementType.LINE_TO, 6.876953125, -2.606393814086914, -1.7870597839355469);
		assertElement(pi, PathElementType.LINE_TO, 6.8798828125, -2.794137477874756, -1.9727468490600586);
		assertElement(pi, PathElementType.LINE_TO, 6.8828125, -3.071044921875, -1.630615234375);
		assertElement(pi, PathElementType.LINE_TO, 6.8857421875, -1.9716308116912842, -1.6829776763916016);
		assertElement(pi, PathElementType.LINE_TO, 6.888671875, -2.606008529663086, -1.7893447875976562);
		assertElement(pi, PathElementType.LINE_TO, 6.8916015625, -2.9633851051330566, -2.148587226867676);
		assertElement(pi, PathElementType.LINE_TO, 6.89453125, -3.3406219482421875, -1.859771728515625);
		assertElement(pi, PathElementType.LINE_TO, 6.8974609375, -2.7870025634765625, -1.971480369567871);
		assertElement(pi, PathElementType.LINE_TO, 6.900390625, -3.4595260620117188, -1.94976806640625);
		assertElement(pi, PathElementType.LINE_TO, 6.9033203125, -3.5145578384399414, -1.987722396850586);
		assertElement(pi, PathElementType.LINE_TO, 6.90625, -3.787109375, -1.13671875);
		assertElement(pi, PathElementType.LINE_TO, 6.9091796875, -1.1825634241104126, -1.1425936222076416);
		assertElement(pi, PathElementType.LINE_TO, 6.912109375, -1.3093385696411133, -1.1589069366455078);
		assertElement(pi, PathElementType.LINE_TO, 6.9150390625, -1.4821019172668457, -1.2430572509765625);
		assertElement(pi, PathElementType.LINE_TO, 6.91796875, -1.7411270141601562, -1.2149810791015625);
		assertElement(pi, PathElementType.LINE_TO, 6.9208984375, -1.7374143600463867, -1.340710163116455);
		assertElement(pi, PathElementType.LINE_TO, 6.923828125, -2.2372093200683594, -1.4901885986328125);
		assertElement(pi, PathElementType.LINE_TO, 6.9267578125, -2.490731716156006, -1.670792579650879);
		assertElement(pi, PathElementType.LINE_TO, 6.9296875, -2.86578369140625, -1.3658447265625);
		assertElement(pi, PathElementType.LINE_TO, 6.9326171875, -1.7799220085144043, -1.4580473899841309);
		assertElement(pi, PathElementType.LINE_TO, 6.935546875, -2.5559730529785156, -1.6625251770019531);
		assertElement(pi, PathElementType.LINE_TO, 6.9384765625, -3.020145893096924, -2.1589651107788086);
		assertElement(pi, PathElementType.LINE_TO, 6.94140625, -3.49371337890625, -1.97552490234375);
		assertElement(pi, PathElementType.LINE_TO, 6.9443359375, -2.9877848625183105, -2.1318607330322266);
		assertElement(pi, PathElementType.LINE_TO, 6.947265625, -3.7420310974121094, -2.1835403442382812);
		assertElement(pi, PathElementType.LINE_TO, 6.9501953125, -3.847623825073242, -2.259033203125);
		assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.17138671875, -1.3818359375);
		assertElement(pi, PathElementType.LINE_TO, 6.9560546875, -1.5556890964508057, -1.401869297027588);
		assertElement(pi, PathElementType.LINE_TO, 6.958984375, -1.9898395538330078, -1.4517402648925781);
		assertElement(pi, PathElementType.LINE_TO, 6.9619140625, -2.4859113693237305, -1.7224769592285156);
		assertElement(pi, PathElementType.LINE_TO, 6.96484375, -3.1145782470703125, -1.579620361328125);
		assertElement(pi, PathElementType.LINE_TO, 6.9677734375, -2.7822389602661133, -1.8723230361938477);
		assertElement(pi, PathElementType.LINE_TO, 6.970703125, -3.7221221923828125, -2.1697235107421875);
		assertElement(pi, PathElementType.LINE_TO, 6.9736328125, -3.9561567306518555, -2.362253189086914);
		assertElement(pi, PathElementType.LINE_TO, 6.9765625, -4.3734130859375, -1.518310546875);
		assertElement(pi, PathElementType.LINE_TO, 6.9794921875, -2.125990390777588, -1.5830087661743164);
		assertElement(pi, PathElementType.LINE_TO, 6.982421875, -3.2467689514160156, -1.6968765258789062);
		assertElement(pi, PathElementType.LINE_TO, 6.9853515625, -3.841419219970703, -2.2734947204589844);
		assertElement(pi, PathElementType.LINE_TO, 6.98828125, -4.477264404296875, -1.59075927734375);
		assertElement(pi, PathElementType.LINE_TO, 6.9912109375, -3.3151674270629883, -1.7586231231689453);
		assertElement(pi, PathElementType.LINE_TO, 6.994140625, -4.529991149902344, -1.6282196044921875);
		assertElement(pi, PathElementType.LINE_TO, 6.9970703125, -4.556577682495117, -1.6473045349121094);
		assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 0.0);
		assertNoElement(pi);*/
		throw new RuntimeException("DBG");
	}

	@DisplayName("getPathIterator(double) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPathIteratorDouble_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		PathIterator3afp pi = this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.0, 1.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.24609375, 0.890625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.71484375, 0.765625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 2.15234375, 0.765625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 2.55859375, 0.890625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 2.93359375, 1.140625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 3.27734375, 1.515625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 3.58984375, 2.015625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 3.734375, 2.3125, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 3.87109375, 2.640625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.0029296875, 1.1429190635681152E-5, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.005859375, 4.565715789794922E-5, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.01171875, 1.8215179443359375E-4, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.0234375, 7.2479248046875E-4, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.046875, 0.00286865234375, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.09375, 0.01123046875, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.1875, 0.04296875, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.28125, 0.0869140625, 0.017578125);
		assertElement(pi, PathElementType.LINE_TO, 4.3046875, 0.06964111328125, 0.02655029296875);
		assertElement(pi, PathElementType.LINE_TO, 4.31640625, 0.09035491943359375, 0.0463409423828125);
		assertElement(pi, PathElementType.LINE_TO, 4.3193359375, 0.07635855674743652, 0.05133771896362305);
		assertElement(pi, PathElementType.LINE_TO, 4.322265625, 0.09907722473144531, 0.053791046142578125);
		assertElement(pi, PathElementType.LINE_TO, 4.3251953125, 0.10297822952270508, 0.056801795959472656);
		assertElement(pi, PathElementType.LINE_TO, 4.328125, 0.11328125, 0.0322265625);
		assertElement(pi, PathElementType.LINE_TO, 4.33984375, 0.08628082275390625, 0.039825439453125);
		assertElement(pi, PathElementType.LINE_TO, 4.345703125, 0.10619926452636719, 0.059169769287109375);
		assertElement(pi, PathElementType.LINE_TO, 4.3486328125, 0.11415767669677734, 0.065887451171875);
		assertElement(pi, PathElementType.LINE_TO, 4.3515625, 0.12774658203125, 0.0413818359375);
		assertElement(pi, PathElementType.LINE_TO, 4.357421875, 0.09561920166015625, 0.047824859619140625);
		assertElement(pi, PathElementType.LINE_TO, 4.3603515625, 0.11478376388549805, 0.06645679473876953);
		assertElement(pi, PathElementType.LINE_TO, 4.36328125, 0.135345458984375, 0.0465087890625);
		assertElement(pi, PathElementType.LINE_TO, 4.369140625, 0.13924789428710938, 0.04923248291015625);
		assertElement(pi, PathElementType.LINE_TO, 4.3720703125, 0.14122772216796875, 0.050640106201171875);
		assertElement(pi, PathElementType.LINE_TO, 4.375, 0.15625, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.3779296875, 2.484917640686035E-4, 4.5299530029296875E-5);
		assertElement(pi, PathElementType.LINE_TO, 4.380859375, 9.808540344238281E-4, 1.79290771484375E-4);
		assertElement(pi, PathElementType.LINE_TO, 4.38671875, 0.003818511962890625, 7.01904296875E-4);
		assertElement(pi, PathElementType.LINE_TO, 4.3984375, 0.014434814453125, 0.002685546875);
		assertElement(pi, PathElementType.LINE_TO, 4.421875, 0.051025390625, 0.009765625);
		assertElement(pi, PathElementType.LINE_TO, 4.4453125, 0.09454345703125, 0.0352783203125);
		assertElement(pi, PathElementType.LINE_TO, 4.45703125, 0.11737060546875, 0.05218505859375);
		assertElement(pi, PathElementType.LINE_TO, 4.4599609375, 0.09577798843383789, 0.05885887145996094);
		assertElement(pi, PathElementType.LINE_TO, 4.462890625, 0.12892532348632812, 0.06142425537109375);
		assertElement(pi, PathElementType.LINE_TO, 4.4658203125, 0.1347064971923828, 0.0661773681640625);
		assertElement(pi, PathElementType.LINE_TO, 4.46875, 0.150390625, 0.03125);
		assertElement(pi, PathElementType.LINE_TO, 4.4921875, 0.13323974609375, 0.05908203125);
		assertElement(pi, PathElementType.LINE_TO, 4.498046875, 0.12296104431152344, 0.07830810546875);
		assertElement(pi, PathElementType.LINE_TO, 4.5009765625, 0.14888906478881836, 0.1071004867553711);
		assertElement(pi, PathElementType.LINE_TO, 4.50390625, 0.1749725341796875, 0.103485107421875);
		assertElement(pi, PathElementType.LINE_TO, 4.5068359375, 0.15397357940673828, 0.11333179473876953);
		assertElement(pi, PathElementType.LINE_TO, 4.509765625, 0.1926422119140625, 0.1209869384765625);
		assertElement(pi, PathElementType.LINE_TO, 4.5126953125, 0.20057201385498047, 0.12835121154785156);
		assertElement(pi, PathElementType.LINE_TO, 4.515625, 0.21826171875, 0.0927734375);
		assertElement(pi, PathElementType.LINE_TO, 4.52734375, 0.180877685546875, 0.108551025390625);
		assertElement(pi, PathElementType.LINE_TO, 4.5302734375, 0.16729497909545898, 0.1241464614868164);
		assertElement(pi, PathElementType.LINE_TO, 4.533203125, 0.21397781372070312, 0.14199066162109375);
		assertElement(pi, PathElementType.LINE_TO, 4.5361328125, 0.2273244857788086, 0.15399742126464844);
		assertElement(pi, PathElementType.LINE_TO, 4.5390625, 0.2490234375, 0.11865234375);
		assertElement(pi, PathElementType.LINE_TO, 4.544921875, 0.20206832885742188, 0.1296844482421875);
		assertElement(pi, PathElementType.LINE_TO, 4.5478515625, 0.2318124771118164, 0.1589832305908203);
		assertElement(pi, PathElementType.LINE_TO, 4.55078125, 0.263519287109375, 0.13018798828125);
		assertElement(pi, PathElementType.LINE_TO, 4.5537109375, 0.21191787719726562, 0.1392192840576172);
		assertElement(pi, PathElementType.LINE_TO, 4.556640625, 0.2705230712890625, 0.13555908203125);
		assertElement(pi, PathElementType.LINE_TO, 4.5595703125, 0.2739582061767578, 0.13813400268554688);
		assertElement(pi, PathElementType.LINE_TO, 4.5625, 0.296875, 0.0625);
		assertElement(pi, PathElementType.LINE_TO, 4.609375, 0.234375, 0.091796875);
		assertElement(pi, PathElementType.LINE_TO, 4.62109375, 0.208160400390625, 0.12225341796875);
		assertElement(pi, PathElementType.LINE_TO, 4.6240234375, 0.19533538818359375, 0.14392662048339844);
		assertElement(pi, PathElementType.LINE_TO, 4.626953125, 0.2543182373046875, 0.17181396484375);
		assertElement(pi, PathElementType.LINE_TO, 4.6298828125, 0.2734394073486328, 0.19080734252929688);
		assertElement(pi, PathElementType.LINE_TO, 4.6328125, 0.301513671875, 0.15673828125);
		assertElement(pi, PathElementType.LINE_TO, 4.638671875, 0.25514984130859375, 0.1730194091796875);
		assertElement(pi, PathElementType.LINE_TO, 4.6416015625, 0.291290283203125, 0.20943450927734375);
		assertElement(pi, PathElementType.LINE_TO, 4.64453125, 0.32940673828125, 0.1807861328125);
		assertElement(pi, PathElementType.LINE_TO, 4.6474609375, 0.27403831481933594, 0.19216537475585938);
		assertElement(pi, PathElementType.LINE_TO, 4.650390625, 0.3417510986328125, 0.190338134765625);
		assertElement(pi, PathElementType.LINE_TO, 4.6533203125, 0.3474769592285156, 0.19440460205078125);
		assertElement(pi, PathElementType.LINE_TO, 4.65625, 0.375, 0.109375);
		assertElement(pi, PathElementType.LINE_TO, 4.6796875, 0.283935546875, 0.13330078125);
		assertElement(pi, PathElementType.LINE_TO, 4.685546875, 0.253082275390625, 0.1633453369140625);
		assertElement(pi, PathElementType.LINE_TO, 4.6884765625, 0.2998485565185547, 0.21342849731445312);
		assertElement(pi, PathElementType.LINE_TO, 4.69140625, 0.3475341796875, 0.1953125);
		assertElement(pi, PathElementType.LINE_TO, 4.6943359375, 0.2969493865966797, 0.21112060546875);
		assertElement(pi, PathElementType.LINE_TO, 4.697265625, 0.3727264404296875, 0.216583251953125);
		assertElement(pi, PathElementType.LINE_TO, 4.7001953125, 0.383453369140625, 0.2243499755859375);
		assertElement(pi, PathElementType.LINE_TO, 4.703125, 0.416015625, 0.13671875);
		assertElement(pi, PathElementType.LINE_TO, 4.7060546875, 0.15416431427001953, 0.1387500762939453);
		assertElement(pi, PathElementType.LINE_TO, 4.708984375, 0.19773101806640625, 0.1438140869140625);
		assertElement(pi, PathElementType.LINE_TO, 4.7119140625, 0.24752426147460938, 0.1710357666015625);
		assertElement(pi, PathElementType.LINE_TO, 4.71484375, 0.31060791015625, 0.1568603515625);
		assertElement(pi, PathElementType.LINE_TO, 4.7177734375, 0.2774085998535156, 0.18626785278320312);
		assertElement(pi, PathElementType.LINE_TO, 4.720703125, 0.37164306640625, 0.21624755859375);
		assertElement(pi, PathElementType.LINE_TO, 4.7236328125, 0.3951683044433594, 0.23566436767578125);
		assertElement(pi, PathElementType.LINE_TO, 4.7265625, 0.43701171875, 0.1513671875);
		assertElement(pi, PathElementType.LINE_TO, 4.7294921875, 0.2122058868408203, 0.15787124633789062);
		assertElement(pi, PathElementType.LINE_TO, 4.732421875, 0.3244171142578125, 0.169342041015625);
		assertElement(pi, PathElementType.LINE_TO, 4.7353515625, 0.38397216796875, 0.2271270751953125);
		assertElement(pi, PathElementType.LINE_TO, 4.73828125, 0.4476318359375, 0.158935546875);
		assertElement(pi, PathElementType.LINE_TO, 4.744140625, 0.452972412109375, 0.16278076171875);
		assertElement(pi, PathElementType.LINE_TO, 4.7470703125, 0.45565032958984375, 0.1647186279296875);
		assertElement(pi, PathElementType.LINE_TO, 4.75, 0.5, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 4.7529296875, 1.8186867237091064E-4, 3.415346145629883E-5);
		assertElement(pi, PathElementType.LINE_TO, 4.755859375, 7.225275039672852E-4, 1.3589859008789062E-4);
		assertElement(pi, PathElementType.LINE_TO, 4.76171875, 0.0028505325317382812, 5.37872314453125E-4);
		assertElement(pi, PathElementType.LINE_TO, 4.7734375, 0.01108551025390625, 0.002105712890625);
		assertElement(pi, PathElementType.LINE_TO, 4.796875, 0.04180908203125, 0.008056640625);
		assertElement(pi, PathElementType.LINE_TO, 4.84375, 0.14697265625, 0.029296875);
		assertElement(pi, PathElementType.LINE_TO, 4.85546875, 0.07076072692871094, 0.03900146484375);
		assertElement(pi, PathElementType.LINE_TO, 4.861328125, 0.1063532829284668, 0.06095409393310547);
		assertElement(pi, PathElementType.LINE_TO, 4.8642578125, 0.1250629425048828, 0.07538938522338867);
		assertElement(pi, PathElementType.LINE_TO, 4.8671875, 0.1515350341796875, 0.06134033203125);
		assertElement(pi, PathElementType.LINE_TO, 4.873046875, 0.14083337783813477, 0.08479118347167969);
		assertElement(pi, PathElementType.LINE_TO, 4.8759765625, 0.1737459897994995, 0.12052321434020996);
		assertElement(pi, PathElementType.LINE_TO, 4.87890625, 0.20755386352539062, 0.11548614501953125);
		assertElement(pi, PathElementType.LINE_TO, 4.8818359375, 0.18152904510498047, 0.12843585014343262);
		assertElement(pi, PathElementType.LINE_TO, 4.884765625, 0.23300743103027344, 0.13878631591796875);
		assertElement(pi, PathElementType.LINE_TO, 4.8876953125, 0.245011568069458, 0.14932584762573242);
		assertElement(pi, PathElementType.LINE_TO, 4.890625, 0.2703857421875, 0.103271484375);
		assertElement(pi, PathElementType.LINE_TO, 4.90234375, 0.230133056640625, 0.12888336181640625);
		assertElement(pi, PathElementType.LINE_TO, 4.9052734375, 0.2127748727798462, 0.1518557071685791);
		assertElement(pi, PathElementType.LINE_TO, 4.908203125, 0.28034114837646484, 0.17926979064941406);
		assertElement(pi, PathElementType.LINE_TO, 4.9111328125, 0.30132079124450684, 0.1983942985534668);
		assertElement(pi, PathElementType.LINE_TO, 4.9140625, 0.33428955078125, 0.151611328125);
		assertElement(pi, PathElementType.LINE_TO, 4.919921875, 0.2736806869506836, 0.170379638671875);
		assertElement(pi, PathElementType.LINE_TO, 4.9228515625, 0.3185279369354248, 0.21499967575073242);
		assertElement(pi, PathElementType.LINE_TO, 4.92578125, 0.36635589599609375, 0.1777496337890625);
		assertElement(pi, PathElementType.LINE_TO, 4.9287109375, 0.2960953712463379, 0.19243383407592773);
		assertElement(pi, PathElementType.LINE_TO, 4.931640625, 0.3823089599609375, 0.19109344482421875);
		assertElement(pi, PathElementType.LINE_TO, 4.9345703125, 0.39023828506469727, 0.19777965545654297);
		assertElement(pi, PathElementType.LINE_TO, 4.9375, 0.42578125, 0.09375);
		assertElement(pi, PathElementType.LINE_TO, 4.9404296875, 0.09563127160072327, 0.09418213367462158);
		assertElement(pi, PathElementType.LINE_TO, 4.943359375, 0.10106396675109863, 0.0954446792602539);
		assertElement(pi, PathElementType.LINE_TO, 4.94921875, 0.12131690979003906, 0.10025787353515625);
		assertElement(pi, PathElementType.LINE_TO, 4.9609375, 0.1905059814453125, 0.11761474609375);
		assertElement(pi, PathElementType.LINE_TO, 4.97265625, 0.2719383239746094, 0.16924285888671875);
		assertElement(pi, PathElementType.LINE_TO, 4.9755859375, 0.24852275848388672, 0.1861412525177002);
		assertElement(pi, PathElementType.LINE_TO, 4.978515625, 0.31402015686035156, 0.20258331298828125);
		assertElement(pi, PathElementType.LINE_TO, 4.9814453125, 0.33510279655456543, 0.2205357551574707);
		assertElement(pi, PathElementType.LINE_TO, 4.984375, 0.3726806640625, 0.171875);
		assertElement(pi, PathElementType.LINE_TO, 4.99609375, 0.3447151184082031, 0.224517822265625);
		assertElement(pi, PathElementType.LINE_TO, 4.9990234375, 0.32943928241729736, 0.2576427459716797);
		assertElement(pi, PathElementType.LINE_TO, 5.001953125, 0.41471195220947266, 0.30274391174316406);
		assertElement(pi, PathElementType.LINE_TO, 5.0048828125, 0.4440317153930664, 0.33360958099365234);
		assertElement(pi, PathElementType.LINE_TO, 5.0078125, 0.484771728515625, 0.29400634765625);
		assertElement(pi, PathElementType.LINE_TO, 5.013671875, 0.4286823272705078, 0.32054710388183594);
		assertElement(pi, PathElementType.LINE_TO, 5.0166015625, 0.4791266918182373, 0.37302255630493164);
		assertElement(pi, PathElementType.LINE_TO, 5.01953125, 0.53155517578125, 0.341583251953125);
		assertElement(pi, PathElementType.LINE_TO, 5.0224609375, 0.4632275104522705, 0.3585519790649414);
		assertElement(pi, PathElementType.LINE_TO, 5.025390625, 0.5523166656494141, 0.3613700866699219);
		assertElement(pi, PathElementType.LINE_TO, 5.0283203125, 0.5619611740112305, 0.3701057434082031);
		assertElement(pi, PathElementType.LINE_TO, 5.03125, 0.5986328125, 0.267578125);
		assertElement(pi, PathElementType.LINE_TO, 5.0341796875, 0.27364838123321533, 0.26855480670928955);
		assertElement(pi, PathElementType.LINE_TO, 5.037109375, 0.2904367446899414, 0.27129459381103516);
		assertElement(pi, PathElementType.LINE_TO, 5.0400390625, 0.31341683864593506, 0.2829887866973877);
		assertElement(pi, PathElementType.LINE_TO, 5.04296875, 0.34763336181640625, 0.28092193603515625);
		assertElement(pi, PathElementType.LINE_TO, 5.048828125, 0.4137601852416992, 0.31934547424316406);
		assertElement(pi, PathElementType.LINE_TO, 5.0517578125, 0.4476296901702881, 0.34438657760620117);
		assertElement(pi, PathElementType.LINE_TO, 5.0546875, 0.49676513671875, 0.30877685546875);
		assertElement(pi, PathElementType.LINE_TO, 5.060546875, 0.4611063003540039, 0.3492870330810547);
		assertElement(pi, PathElementType.LINE_TO, 5.0634765625, 0.5210700035095215, 0.41442298889160156);
		assertElement(pi, PathElementType.LINE_TO, 5.06640625, 0.5817794799804688, 0.3956451416015625);
		assertElement(pi, PathElementType.LINE_TO, 5.0693359375, 0.5214745998382568, 0.41652822494506836);
		assertElement(pi, PathElementType.LINE_TO, 5.072265625, 0.6157093048095703, 0.4265022277832031);
		assertElement(pi, PathElementType.LINE_TO, 5.0751953125, 0.6302437782287598, 0.4382143020629883);
		assertElement(pi, PathElementType.LINE_TO, 5.078125, 0.6708984375, 0.3349609375);
		assertElement(pi, PathElementType.LINE_TO, 5.0810546875, 0.3563019037246704, 0.3376636505126953);
		assertElement(pi, PathElementType.LINE_TO, 5.083984375, 0.4096040725708008, 0.34447479248046875);
		assertElement(pi, PathElementType.LINE_TO, 5.0869140625, 0.4706451892852783, 0.37833070755004883);
		assertElement(pi, PathElementType.LINE_TO, 5.08984375, 0.5477676391601562, 0.362640380859375);
		assertElement(pi, PathElementType.LINE_TO, 5.0927734375, 0.5086724758148193, 0.39896488189697266);
		assertElement(pi, PathElementType.LINE_TO, 5.095703125, 0.6230220794677734, 0.4370231628417969);
		assertElement(pi, PathElementType.LINE_TO, 5.0986328125, 0.6521425247192383, 0.4617271423339844);
		assertElement(pi, PathElementType.LINE_TO, 5.1015625, 0.70294189453125, 0.3626708984375);
		assertElement(pi, PathElementType.LINE_TO, 5.1044921875, 0.43554210662841797, 0.37071752548217773);
		assertElement(pi, PathElementType.LINE_TO, 5.107421875, 0.569976806640625, 0.3851356506347656);
		assertElement(pi, PathElementType.LINE_TO, 5.1103515625, 0.6415190696716309, 0.45488834381103516);
		assertElement(pi, PathElementType.LINE_TO, 5.11328125, 0.717803955078125, 0.374755859375);
		assertElement(pi, PathElementType.LINE_TO, 5.1162109375, 0.5801253318786621, 0.3950767517089844);
		assertElement(pi, PathElementType.LINE_TO, 5.119140625, 0.7249107360839844, 0.38028717041015625);
		assertElement(pi, PathElementType.LINE_TO, 5.1220703125, 0.7283744812011719, 0.3829078674316406);
		assertElement(pi, PathElementType.LINE_TO, 5.125, 0.78125, 0.1875);
		assertElement(pi, PathElementType.LINE_TO, 5.1279296875, 0.188188374042511, 0.1876017451286316);
		assertElement(pi, PathElementType.LINE_TO, 5.130859375, 0.1902146339416504, 0.18790197372436523);
		assertElement(pi, PathElementType.LINE_TO, 5.13671875, 0.19804763793945312, 0.18906784057617188);
		assertElement(pi, PathElementType.LINE_TO, 5.1484375, 0.227203369140625, 0.193450927734375);
		assertElement(pi, PathElementType.LINE_TO, 5.16015625, 0.2670173645019531, 0.21340179443359375);
		assertElement(pi, PathElementType.LINE_TO, 5.166015625, 0.2904367446899414, 0.2285480499267578);
		assertElement(pi, PathElementType.LINE_TO, 5.1689453125, 0.30314040184020996, 0.23764562606811523);
		assertElement(pi, PathElementType.LINE_TO, 5.171875, 0.326416015625, 0.208740234375);
		assertElement(pi, PathElementType.LINE_TO, 5.177734375, 0.24894094467163086, 0.21769237518310547);
		assertElement(pi, PathElementType.LINE_TO, 5.1806640625, 0.28333544731140137, 0.2386493682861328);
		assertElement(pi, PathElementType.LINE_TO, 5.18359375, 0.3270530700683594, 0.23805999755859375);
		assertElement(pi, PathElementType.LINE_TO, 5.1865234375, 0.31568264961242676, 0.26050448417663574);
		assertElement(pi, PathElementType.LINE_TO, 5.189453125, 0.38053131103515625, 0.2892875671386719);
		assertElement(pi, PathElementType.LINE_TO, 5.1923828125, 0.4046480655670166, 0.3110384941101074);
		assertElement(pi, PathElementType.LINE_TO, 5.1953125, 0.440582275390625, 0.27410888671875);
		assertElement(pi, PathElementType.LINE_TO, 5.201171875, 0.3981008529663086, 0.2978382110595703);
		assertElement(pi, PathElementType.LINE_TO, 5.2041015625, 0.44664525985717773, 0.346221923828125);
		assertElement(pi, PathElementType.LINE_TO, 5.20703125, 0.49887847900390625, 0.3167266845703125);
		assertElement(pi, PathElementType.LINE_TO, 5.2099609375, 0.4370872974395752, 0.3343977928161621);
		assertElement(pi, PathElementType.LINE_TO, 5.212890625, 0.5278606414794922, 0.3395805358886719);
		assertElement(pi, PathElementType.LINE_TO, 5.2158203125, 0.5421929359436035, 0.35115909576416016);
		assertElement(pi, PathElementType.LINE_TO, 5.21875, 0.583984375, 0.251953125);
		assertElement(pi, PathElementType.LINE_TO, 5.2421875, 0.517791748046875, 0.32110595703125);
		assertElement(pi, PathElementType.LINE_TO, 5.248046875, 0.4877433776855469, 0.37044715881347656);
		assertElement(pi, PathElementType.LINE_TO, 5.2509765625, 0.5546653270721436, 0.4449009895324707);
		assertElement(pi, PathElementType.LINE_TO, 5.25390625, 0.6216888427734375, 0.4337158203125);
		assertElement(pi, PathElementType.LINE_TO, 5.2568359375, 0.5647876262664795, 0.4584999084472656);
		assertElement(pi, PathElementType.LINE_TO, 5.259765625, 0.6642436981201172, 0.4763374328613281);
		assertElement(pi, PathElementType.LINE_TO, 5.2626953125, 0.6828536987304688, 0.4935894012451172);
		assertElement(pi, PathElementType.LINE_TO, 5.265625, 0.726806640625, 0.39794921875);
		assertElement(pi, PathElementType.LINE_TO, 5.2685546875, 0.4199877977371216, 0.4012415409088135);
		assertElement(pi, PathElementType.LINE_TO, 5.271484375, 0.4750833511352539, 0.4096851348876953);
		assertElement(pi, PathElementType.LINE_TO, 5.2744140625, 0.5385308265686035, 0.4459228515625);
		assertElement(pi, PathElementType.LINE_TO, 5.27734375, 0.6183242797851562, 0.4334259033203125);
		assertElement(pi, PathElementType.LINE_TO, 5.2802734375, 0.5815939903259277, 0.4718003273010254);
		assertElement(pi, PathElementType.LINE_TO, 5.283203125, 0.6983489990234375, 0.5142288208007812);
		assertElement(pi, PathElementType.LINE_TO, 5.2861328125, 0.7298083305358887, 0.5420846939086914);
		assertElement(pi, PathElementType.LINE_TO, 5.2890625, 0.78240966796875, 0.4481201171875);
		assertElement(pi, PathElementType.LINE_TO, 5.2919921875, 0.5211031436920166, 0.4567399024963379);
		assertElement(pi, PathElementType.LINE_TO, 5.294921875, 0.6559543609619141, 0.4726295471191406);
		assertElement(pi, PathElementType.LINE_TO, 5.2978515625, 0.7284908294677734, 0.5436534881591797);
		assertElement(pi, PathElementType.LINE_TO, 5.30078125, 0.8057098388671875, 0.466400146484375);
		assertElement(pi, PathElementType.LINE_TO, 5.3037109375, 0.6710782051086426, 0.48720264434814453);
		assertElement(pi, PathElementType.LINE_TO, 5.306640625, 0.8160972595214844, 0.47356414794921875);
		assertElement(pi, PathElementType.LINE_TO, 5.3095703125, 0.8209409713745117, 0.47658348083496094);
		assertElement(pi, PathElementType.LINE_TO, 5.3125, 0.875, 0.28125);
		assertElement(pi, PathElementType.LINE_TO, 5.3154296875, 0.2837638258934021, 0.28155040740966797);
		assertElement(pi, PathElementType.LINE_TO, 5.318359375, 0.2910151481628418, 0.28241729736328125);
		assertElement(pi, PathElementType.LINE_TO, 5.3212890625, 0.3014940023422241, 0.2872445583343506);
		assertElement(pi, PathElementType.LINE_TO, 5.32421875, 0.3179893493652344, 0.28564453125);
		assertElement(pi, PathElementType.LINE_TO, 5.330078125, 0.3546476364135742, 0.3033123016357422);
		assertElement(pi, PathElementType.LINE_TO, 5.3330078125, 0.3761787414550781, 0.31685924530029297);
		assertElement(pi, PathElementType.LINE_TO, 5.3359375, 0.409637451171875, 0.296630859375);
		assertElement(pi, PathElementType.LINE_TO, 5.34765625, 0.5142135620117188, 0.3541717529296875);
		assertElement(pi, PathElementType.LINE_TO, 5.3505859375, 0.47205400466918945, 0.37591981887817383);
		assertElement(pi, PathElementType.LINE_TO, 5.353515625, 0.5673599243164062, 0.39182281494140625);
		assertElement(pi, PathElementType.LINE_TO, 5.3564453125, 0.5936999320983887, 0.4119729995727539);
		assertElement(pi, PathElementType.LINE_TO, 5.359375, 0.646240234375, 0.3251953125);
		assertElement(pi, PathElementType.LINE_TO, 5.37109375, 0.5764541625976562, 0.386932373046875);
		assertElement(pi, PathElementType.LINE_TO, 5.3740234375, 0.5460402965545654, 0.4330720901489258);
		assertElement(pi, PathElementType.LINE_TO, 5.376953125, 0.6734905242919922, 0.4909934997558594);
		assertElement(pi, PathElementType.LINE_TO, 5.3798828125, 0.7129297256469727, 0.5297813415527344);
		assertElement(pi, PathElementType.LINE_TO, 5.3828125, 0.77203369140625, 0.4503173828125);
		assertElement(pi, PathElementType.LINE_TO, 5.3857421875, 0.5247106552124023, 0.4610104560852051);
		assertElement(pi, PathElementType.LINE_TO, 5.388671875, 0.6627044677734375, 0.4821891784667969);
		assertElement(pi, PathElementType.LINE_TO, 5.3916015625, 0.7391171455383301, 0.558588981628418);
		assertElement(pi, PathElementType.LINE_TO, 5.39453125, 0.819671630859375, 0.4906005859375);
		assertElement(pi, PathElementType.LINE_TO, 5.3974609375, 0.6935582160949707, 0.5132904052734375);
		assertElement(pi, PathElementType.LINE_TO, 5.400390625, 0.8391609191894531, 0.5041427612304688);
		assertElement(pi, PathElementType.LINE_TO, 5.4033203125, 0.8476943969726562, 0.5090065002441406);
		assertElement(pi, PathElementType.LINE_TO, 5.40625, 0.904296875, 0.31640625);
		assertElement(pi, PathElementType.LINE_TO, 5.4091796875, 0.32581377029418945, 0.31740689277648926);
		assertElement(pi, PathElementType.LINE_TO, 5.412109375, 0.3517951965332031, 0.32015419006347656);
		assertElement(pi, PathElementType.LINE_TO, 5.4150390625, 0.38703370094299316, 0.33682775497436523);
		assertElement(pi, PathElementType.LINE_TO, 5.41796875, 0.440032958984375, 0.3293609619140625);
		assertElement(pi, PathElementType.LINE_TO, 5.423828125, 0.5403919219970703, 0.3835258483886719);
		assertElement(pi, PathElementType.LINE_TO, 5.4267578125, 0.5913138389587402, 0.4190511703491211);
		assertElement(pi, PathElementType.LINE_TO, 5.4296875, 0.66748046875, 0.3519287109375);
		assertElement(pi, PathElementType.LINE_TO, 5.435546875, 0.5967159271240234, 0.4105186462402344);
		assertElement(pi, PathElementType.LINE_TO, 5.4384765625, 0.6908340454101562, 0.5109004974365234);
		assertElement(pi, PathElementType.LINE_TO, 5.44140625, 0.7866363525390625, 0.468353271484375);
		assertElement(pi, PathElementType.LINE_TO, 5.4443359375, 0.6772971153259277, 0.4987802505493164);
		assertElement(pi, PathElementType.LINE_TO, 5.447265625, 0.8313331604003906, 0.5047683715820312);
		assertElement(pi, PathElementType.LINE_TO, 5.4501953125, 0.8494634628295898, 0.5165309906005859);
		assertElement(pi, PathElementType.LINE_TO, 5.453125, 0.9130859375, 0.328125);
		assertElement(pi, PathElementType.LINE_TO, 5.4560546875, 0.3630239963531494, 0.3316469192504883);
		assertElement(pi, PathElementType.LINE_TO, 5.458984375, 0.4500293731689453, 0.34023284912109375);
		assertElement(pi, PathElementType.LINE_TO, 5.4619140625, 0.5488791465759277, 0.39319896697998047);
		assertElement(pi, PathElementType.LINE_TO, 5.46484375, 0.6742095947265625, 0.3607177734375);
		assertElement(pi, PathElementType.LINE_TO, 5.4677734375, 0.6031804084777832, 0.4181976318359375);
		assertElement(pi, PathElementType.LINE_TO, 5.470703125, 0.7911109924316406, 0.47409820556640625);
		assertElement(pi, PathElementType.LINE_TO, 5.4736328125, 0.8348350524902344, 0.5091972351074219);
		assertElement(pi, PathElementType.LINE_TO, 5.4765625, 0.9156494140625, 0.331787109375);
		assertElement(pi, PathElementType.LINE_TO, 5.4794921875, 0.4531278610229492, 0.34362316131591797);
		assertElement(pi, PathElementType.LINE_TO, 5.482421875, 0.6762466430664062, 0.36344146728515625);
		assertElement(pi, PathElementType.LINE_TO, 5.4853515625, 0.7924356460571289, 0.4758434295654297);
		assertElement(pi, PathElementType.LINE_TO, 5.48828125, 0.9163818359375, 0.3328857421875);
		assertElement(pi, PathElementType.LINE_TO, 5.494140625, 0.9165878295898438, 0.3332061767578125);
		assertElement(pi, PathElementType.LINE_TO, 5.4970703125, 0.9166450500488281, 0.3332977294921875);
		assertElement(pi, PathElementType.LINE_TO, 5.5, 1.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 5.5029296875, 4.5552849769592285E-5, 1.1414289474487305E-5);
		assertElement(pi, PathElementType.LINE_TO, 5.505859375, 1.8131732940673828E-4, 4.553794860839844E-5);
		assertElement(pi, PathElementType.LINE_TO, 5.51171875, 7.181167602539062E-4, 1.811981201171875E-4);
		assertElement(pi, PathElementType.LINE_TO, 5.5234375, 0.00281524658203125, 7.171630859375E-4);
		assertElement(pi, PathElementType.LINE_TO, 5.546875, 0.01080322265625, 0.0028076171875);
		assertElement(pi, PathElementType.LINE_TO, 5.59375, 0.03955078125, 0.0107421875);
		assertElement(pi, PathElementType.LINE_TO, 5.6875, 0.12890625, 0.0390625);
		assertElement(pi, PathElementType.LINE_TO, 5.78125, 0.212890625, 0.109375);
		assertElement(pi, PathElementType.LINE_TO, 5.8046875, 0.186370849609375, 0.12744140625);
		assertElement(pi, PathElementType.LINE_TO, 5.81640625, 0.21587371826171875, 0.1593475341796875);
		assertElement(pi, PathElementType.LINE_TO, 5.8193359375, 0.1983790397644043, 0.16651582717895508);
		assertElement(pi, PathElementType.LINE_TO, 5.822265625, 0.22784423828125, 0.17131805419921875);
		assertElement(pi, PathElementType.LINE_TO, 5.8251953125, 0.23303556442260742, 0.17611026763916016);
		assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.245849609375, 0.14697265625);
		assertElement(pi, PathElementType.LINE_TO, 5.83984375, 0.2116241455078125, 0.1578216552734375);
		assertElement(pi, PathElementType.LINE_TO, 5.845703125, 0.2348155975341797, 0.18191909790039062);
		assertElement(pi, PathElementType.LINE_TO, 5.8486328125, 0.24385309219360352, 0.1902475357055664);
		assertElement(pi, PathElementType.LINE_TO, 5.8515625, 0.2587890625, 0.163818359375);
		assertElement(pi, PathElementType.LINE_TO, 5.857421875, 0.2223834991455078, 0.17128753662109375);
		assertElement(pi, PathElementType.LINE_TO, 5.8603515625, 0.2427058219909668, 0.1916799545288086);
		assertElement(pi, PathElementType.LINE_TO, 5.86328125, 0.2640533447265625, 0.171112060546875);
		assertElement(pi, PathElementType.LINE_TO, 5.869140625, 0.26630401611328125, 0.17431640625);
		assertElement(pi, PathElementType.LINE_TO, 5.8720703125, 0.26731395721435547, 0.1757678985595703);
		assertElement(pi, PathElementType.LINE_TO, 5.875, 0.28125, 0.125);
		assertElement(pi, PathElementType.LINE_TO, 5.8779296875, 0.1251349151134491, 0.12505674362182617);
		assertElement(pi, PathElementType.LINE_TO, 5.880859375, 0.12553000450134277, 0.12522506713867188);
		assertElement(pi, PathElementType.LINE_TO, 5.88671875, 0.1270427703857422, 0.125885009765625);
		assertElement(pi, PathElementType.LINE_TO, 5.8984375, 0.1325531005859375, 0.12841796875);
		assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.1502685546875, 0.1376953125);
		assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.1865234375, 0.16796875);
		assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.1484375, 0.234375);
		assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.00390625, 0.203125);
		assertElement(pi, PathElementType.LINE_TO, 6.1796875, 0.04583740234375, 0.17578125);
		assertElement(pi, PathElementType.LINE_TO, 6.185546875, 0.06836509704589844, 0.1476287841796875);
		assertElement(pi, PathElementType.LINE_TO, 6.1884765625, 0.025332927703857422, 0.10175609588623047);
		assertElement(pi, PathElementType.LINE_TO, 6.19140625, -0.0189971923828125, 0.115447998046875);
		assertElement(pi, PathElementType.LINE_TO, 6.1943359375, 0.023222923278808594, 0.09995746612548828);
		assertElement(pi, PathElementType.LINE_TO, 6.197265625, -0.046966552734375, 0.0920257568359375);
		assertElement(pi, PathElementType.LINE_TO, 6.2001953125, -0.05970478057861328, 0.08226966857910156);
		assertElement(pi, PathElementType.LINE_TO, 6.203125, -0.09228515625, 0.1572265625);
		assertElement(pi, PathElementType.LINE_TO, 6.21484375, -0.013519287109375, 0.132171630859375);
		assertElement(pi, PathElementType.LINE_TO, 6.2177734375, 0.015245914459228516, 0.1024923324584961);
		assertElement(pi, PathElementType.LINE_TO, 6.220703125, -0.07762527465820312, 0.07028961181640625);
		assertElement(pi, PathElementType.LINE_TO, 6.2236328125, -0.1035909652709961, 0.04836463928222656);
		assertElement(pi, PathElementType.LINE_TO, 6.2265625, -0.1474609375, 0.12451171875);
		assertElement(pi, PathElementType.LINE_TO, 6.2294921875, 0.06384515762329102, 0.11707687377929688);
		assertElement(pi, PathElementType.LINE_TO, 6.232421875, -0.048717498779296875, 0.1030731201171875);
		assertElement(pi, PathElementType.LINE_TO, 6.2353515625, -0.11056995391845703, 0.04298973083496094);
		assertElement(pi, PathElementType.LINE_TO, 6.23828125, -0.177093505859375, 0.10516357421875);
		assertElement(pi, PathElementType.LINE_TO, 6.2412109375, -0.067962646484375, 0.08630561828613281);
		assertElement(pi, PathElementType.LINE_TO, 6.244140625, -0.1924896240234375, 0.0946044921875);
		assertElement(pi, PathElementType.LINE_TO, 6.2470703125, -0.2003498077392578, 0.08906936645507812);
		assertElement(pi, PathElementType.LINE_TO, 6.25, -0.25, 0.25);
		assertElement(pi, PathElementType.LINE_TO, 6.2529296875, 0.24964727461338043, 0.24993166327476501);
		assertElement(pi, PathElementType.LINE_TO, 6.255859375, 0.24859726428985596, 0.24972796440124512);
		assertElement(pi, PathElementType.LINE_TO, 6.26171875, 0.24445438385009766, 0.24892234802246094);
		assertElement(pi, PathElementType.LINE_TO, 6.2734375, 0.22834014892578125, 0.2457733154296875);
		assertElement(pi, PathElementType.LINE_TO, 6.296875, 0.16754150390625, 0.2337646484375);
		assertElement(pi, PathElementType.LINE_TO, 6.3203125, 0.0821533203125, 0.18927001953125);
		assertElement(pi, PathElementType.LINE_TO, 6.326171875, 0.1042027473449707, 0.1717967987060547);
		assertElement(pi, PathElementType.LINE_TO, 6.3291015625, 0.06936156749725342, 0.1376206874847412);
		assertElement(pi, PathElementType.LINE_TO, 6.33203125, 0.031101226806640625, 0.15538787841796875);
		assertElement(pi, PathElementType.LINE_TO, 6.3349609375, 0.06987380981445312, 0.141648530960083);
		assertElement(pi, PathElementType.LINE_TO, 6.337890625, 0.0031681060791015625, 0.1349029541015625);
		assertElement(pi, PathElementType.LINE_TO, 6.3408203125, -0.011479616165161133, 0.12361574172973633);
		assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.04638671875, 0.1904296875);
		assertElement(pi, PathElementType.LINE_TO, 6.3466796875, 0.18406754732131958, 0.18902736902236938);
		assertElement(pi, PathElementType.LINE_TO, 6.349609375, 0.16636896133422852, 0.18503618240356445);
		assertElement(pi, PathElementType.LINE_TO, 6.35546875, 0.10528945922851562, 0.17058181762695312);
		assertElement(pi, PathElementType.LINE_TO, 6.361328125, 0.03200674057006836, 0.12552356719970703);
		assertElement(pi, PathElementType.LINE_TO, 6.3642578125, -0.006599545478820801, 0.09582877159118652);
		assertElement(pi, PathElementType.LINE_TO, 6.3671875, -0.061309814453125, 0.124847412109375);
		assertElement(pi, PathElementType.LINE_TO, 6.373046875, -0.03978300094604492, 0.0764608383178711);
		assertElement(pi, PathElementType.LINE_TO, 6.3759765625, -0.1081535816192627, 0.0025148391723632812);
		assertElement(pi, PathElementType.LINE_TO, 6.37890625, -0.17862319946289062, 0.01319122314453125);
		assertElement(pi, PathElementType.LINE_TO, 6.3818359375, -0.12472736835479736, -0.013833284378051758);
		assertElement(pi, PathElementType.LINE_TO, 6.384765625, -0.2325429916381836, -0.03545188903808594);
		assertElement(pi, PathElementType.LINE_TO, 6.3876953125, -0.2581756114959717, -0.057691097259521484);
		assertElement(pi, PathElementType.LINE_TO, 6.390625, -0.31201171875, 0.03857421875);
		assertElement(pi, PathElementType.LINE_TO, 6.40234375, -0.23144149780273438, -0.0161590576171875);
		assertElement(pi, PathElementType.LINE_TO, 6.4052734375, -0.19502747058868408, -0.06515741348266602);
		assertElement(pi, PathElementType.LINE_TO, 6.408203125, -0.33951282501220703, -0.12374305725097656);
		assertElement(pi, PathElementType.LINE_TO, 6.4111328125, -0.38504648208618164, -0.1649627685546875);
		assertElement(pi, PathElementType.LINE_TO, 6.4140625, -0.456512451171875, -0.06549072265625);
		assertElement(pi, PathElementType.LINE_TO, 6.4169921875, -0.1575603485107422, -0.07907509803771973);
		assertElement(pi, PathElementType.LINE_TO, 6.419921875, -0.3292808532714844, -0.10658836364746094);
		assertElement(pi, PathElementType.LINE_TO, 6.4228515625, -0.4269134998321533, -0.2034296989440918);
		assertElement(pi, PathElementType.LINE_TO, 6.42578125, -0.5313873291015625, -0.12396240234375);
		assertElement(pi, PathElementType.LINE_TO, 6.4287109375, -0.3811056613922119, -0.1563568115234375);
		assertElement(pi, PathElementType.LINE_TO, 6.431640625, -0.5694065093994141, -0.15465927124023438);
		assertElement(pi, PathElementType.LINE_TO, 6.4345703125, -0.5885429382324219, -0.1703357696533203);
		assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.66796875, 0.0546875);
		assertElement(pi, PathElementType.LINE_TO, 6.4404296875, 0.0502932071685791, 0.05370527505874634);
		assertElement(pi, PathElementType.LINE_TO, 6.443359375, 0.03759193420410156, 0.05083513259887695);
		assertElement(pi, PathElementType.LINE_TO, 6.44921875, -0.0098419189453125, 0.039890289306640625);
		assertElement(pi, PathElementType.LINE_TO, 6.455078125, -0.07561826705932617, 0.0037717819213867188);
		assertElement(pi, PathElementType.LINE_TO, 6.4580078125, -0.11453402042388916, -0.02309250831604004);
		assertElement(pi, PathElementType.LINE_TO, 6.4609375, -0.172607421875, 3.96728515625E-4);
		assertElement(pi, PathElementType.LINE_TO, 6.4638671875, -0.06220799684524536, -0.014894843101501465);
		assertElement(pi, PathElementType.LINE_TO, 6.466796875, -0.18439531326293945, -0.050421714782714844);
		assertElement(pi, PathElementType.LINE_TO, 6.4697265625, -0.269977331161499, -0.13389873504638672);
		assertElement(pi, PathElementType.LINE_TO, 6.47265625, -0.3657493591308594, -0.12009429931640625);
		assertElement(pi, PathElementType.LINE_TO, 6.4755859375, -0.30961501598358154, -0.1601407527923584);
		assertElement(pi, PathElementType.LINE_TO, 6.478515625, -0.4664011001586914, -0.19861793518066406);
		assertElement(pi, PathElementType.LINE_TO, 6.4814453125, -0.5171148777008057, -0.24118471145629883);
		assertElement(pi, PathElementType.LINE_TO, 6.484375, -0.60791015625, -0.123291015625);
		assertElement(pi, PathElementType.LINE_TO, 6.4873046875, -0.16485315561294556, -0.13364088535308838);
		assertElement(pi, PathElementType.LINE_TO, 6.490234375, -0.26926088333129883, -0.16136646270751953);
		assertElement(pi, PathElementType.LINE_TO, 6.4931640625, -0.3926093578338623, -0.24071598052978516);
		assertElement(pi, PathElementType.LINE_TO, 6.49609375, -0.5449409484863281, -0.24900054931640625);
		assertElement(pi, PathElementType.LINE_TO, 6.4990234375, -0.5071747303009033, -0.32967114448547363);
		assertElement(pi, PathElementType.LINE_TO, 6.501953125, -0.7176170349121094, -0.4387474060058594);
		assertElement(pi, PathElementType.LINE_TO, 6.5048828125, -0.7905957698822021, -0.5141148567199707);
		assertElement(pi, PathElementType.LINE_TO, 6.5078125, -0.893035888671875, -0.41339111328125);
		assertElement(pi, PathElementType.LINE_TO, 6.5107421875, -0.5323711633682251, -0.4346444606781006);
		assertElement(pi, PathElementType.LINE_TO, 6.513671875, -0.7545862197875977, -0.4799633026123047);
		assertElement(pi, PathElementType.LINE_TO, 6.5166015625, -0.8833432197570801, -0.6127204895019531);
		assertElement(pi, PathElementType.LINE_TO, 6.51953125, -1.0181503295898438, -0.5321502685546875);
		assertElement(pi, PathElementType.LINE_TO, 6.5224609375, -0.8451278209686279, -0.5760102272033691);
		assertElement(pi, PathElementType.LINE_TO, 6.525390625, -1.0758075714111328, -0.5838813781738281);
		assertElement(pi, PathElementType.LINE_TO, 6.5283203125, -1.1032767295837402, -0.6075658798217773);
		assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.201171875, -0.34375);
		assertElement(pi, PathElementType.LINE_TO, 6.5341796875, -0.3602370619773865, -0.34648728370666504);
		assertElement(pi, PathElementType.LINE_TO, 6.537109375, -0.4058833122253418, -0.35418128967285156);
		assertElement(pi, PathElementType.LINE_TO, 6.5400390625, -0.46850359439849854, -0.38622212409973145);
		assertElement(pi, PathElementType.LINE_TO, 6.54296875, -0.5617637634277344, -0.3813323974609375);
		assertElement(pi, PathElementType.LINE_TO, 6.5458984375, -0.5653444528579712, -0.4280095100402832);
		assertElement(pi, PathElementType.LINE_TO, 6.548828125, -0.7430582046508789, -0.4869861602783203);
		assertElement(pi, PathElementType.LINE_TO, 6.5517578125, -0.8363900184631348, -0.5560798645019531);
		assertElement(pi, PathElementType.LINE_TO, 6.5546875, -0.971649169921875, -0.4609375);
		assertElement(pi, PathElementType.LINE_TO, 6.5576171875, -0.6063855886459351, -0.4954352378845215);
		assertElement(pi, PathElementType.LINE_TO, 6.560546875, -0.8797178268432617, -0.5731430053710938);
		assertElement(pi, PathElementType.LINE_TO, 6.5634765625, -1.0460031032562256, -0.753049373626709);
		assertElement(pi, PathElementType.LINE_TO, 6.56640625, -1.2152328491210938, -0.7034759521484375);
		assertElement(pi, PathElementType.LINE_TO, 6.5693359375, -1.0524744987487793, -0.7625565528869629);
		assertElement(pi, PathElementType.LINE_TO, 6.572265625, -1.3158111572265625, -0.7933273315429688);
		assertElement(pi, PathElementType.LINE_TO, 6.5751953125, -1.3601088523864746, -0.8290224075317383);
		assertElement(pi, PathElementType.LINE_TO, 6.578125, -1.477294921875, -0.54736328125);
		assertElement(pi, PathElementType.LINE_TO, 6.5810546875, -0.6083686351776123, -0.555593729019165);
		assertElement(pi, PathElementType.LINE_TO, 6.583984375, -0.7609539031982422, -0.5765171051025391);
		assertElement(pi, PathElementType.LINE_TO, 6.5869140625, -0.9364182949066162, -0.6747708320617676);
		assertElement(pi, PathElementType.LINE_TO, 6.58984375, -1.1582794189453125, -0.6338348388671875);
		assertElement(pi, PathElementType.LINE_TO, 6.5927734375, -1.0513756275177002, -0.7392611503601074);
		assertElement(pi, PathElementType.LINE_TO, 6.595703125, -1.380514144897461, -0.8523750305175781);
		assertElement(pi, PathElementType.LINE_TO, 6.5986328125, -1.4684109687805176, -0.9274492263793945);
		assertElement(pi, PathElementType.LINE_TO, 6.6015625, -1.61865234375, -0.652099609375);
		assertElement(pi, PathElementType.LINE_TO, 6.6044921875, -0.8632004261016846, -0.6768703460693359);
		assertElement(pi, PathElementType.LINE_TO, 6.607421875, -1.253671646118164, -0.7226181030273438);
		assertElement(pi, PathElementType.LINE_TO, 6.6103515625, -1.4647154808044434, -0.9285612106323242);
		assertElement(pi, PathElementType.LINE_TO, 6.61328125, -1.6903839111328125, -0.705657958984375);
		assertElement(pi, PathElementType.LINE_TO, 6.6162109375, -1.3021087646484375, -0.7679224014282227);
		assertElement(pi, PathElementType.LINE_TO, 6.619140625, -1.7265701293945312, -0.73284912109375);
		assertElement(pi, PathElementType.LINE_TO, 6.6220703125, -1.7447576522827148, -0.7465763092041016);
		assertElement(pi, PathElementType.LINE_TO, 6.625, -1.90625, -0.1875);
		assertElement(pi, PathElementType.LINE_TO, 6.6279296875, -0.18999579548835754, -0.187930166721344);
		assertElement(pi, PathElementType.LINE_TO, 6.630859375, -0.19734978675842285, -0.18920183181762695);
		assertElement(pi, PathElementType.LINE_TO, 6.63671875, -0.2258319854736328, -0.19415664672851562);
		assertElement(pi, PathElementType.LINE_TO, 6.642578125, -0.26738643646240234, -0.21464157104492188);
		assertElement(pi, PathElementType.LINE_TO, 6.6455078125, -0.2929013967514038, -0.23090195655822754);
		assertElement(pi, PathElementType.LINE_TO, 6.6484375, -0.3322906494140625, -0.212921142578125);
		assertElement(pi, PathElementType.LINE_TO, 6.66015625, -0.47914886474609375, -0.288482666015625);
		assertElement(pi, PathElementType.LINE_TO, 6.6630859375, -0.43821895122528076, -0.31856822967529297);
		assertElement(pi, PathElementType.LINE_TO, 6.666015625, -0.566004753112793, -0.3456897735595703);
		assertElement(pi, PathElementType.LINE_TO, 6.6689453125, -0.6132535934448242, -0.3800783157348633);
		assertElement(pi, PathElementType.LINE_TO, 6.671875, -0.6983642578125, -0.279541015625);
		assertElement(pi, PathElementType.LINE_TO, 6.6748046875, -0.32094407081604004, -0.2886608839035034);
		assertElement(pi, PathElementType.LINE_TO, 6.677734375, -0.42590904235839844, -0.3130521774291992);
		assertElement(pi, PathElementType.LINE_TO, 6.6806640625, -0.5514625310897827, -0.3900282382965088);
		assertElement(pi, PathElementType.LINE_TO, 6.68359375, -0.7110595703125, -0.38983917236328125);
		assertElement(pi, PathElementType.LINE_TO, 6.6865234375, -0.6719332933425903, -0.47227025032043457);
		assertElement(pi, PathElementType.LINE_TO, 6.689453125, -0.9085264205932617, -0.5792102813720703);
		assertElement(pi, PathElementType.LINE_TO, 6.6923828125, -0.9982402324676514, -0.6605086326599121);
		assertElement(pi, PathElementType.LINE_TO, 6.6953125, -1.13092041015625, -0.53076171875);
		assertElement(pi, PathElementType.LINE_TO, 6.6982421875, -0.6879080533981323, -0.5591864585876465);
		assertElement(pi, PathElementType.LINE_TO, 6.701171875, -0.9843740463256836, -0.6204071044921875);
		assertElement(pi, PathElementType.LINE_TO, 6.7041015625, -1.1636149883270264, -0.7993769645690918);
		assertElement(pi, PathElementType.LINE_TO, 6.70703125, -1.3567123413085938, -0.6969146728515625);
		assertElement(pi, PathElementType.LINE_TO, 6.7099609375, -1.136803150177002, -0.763486385345459);
		assertElement(pi, PathElementType.LINE_TO, 6.712890625, -1.47064208984375, -0.7874374389648438);
		assertElement(pi, PathElementType.LINE_TO, 6.7158203125, -1.527529239654541, -0.833888053894043);
		assertElement(pi, PathElementType.LINE_TO, 6.71875, -1.6845703125, -0.478515625);
		assertElement(pi, PathElementType.LINE_TO, 6.7216796875, -0.5051213502883911, -0.48448169231414795);
		assertElement(pi, PathElementType.LINE_TO, 6.724609375, -0.5788202285766602, -0.5014104843139648);
		assertElement(pi, PathElementType.LINE_TO, 6.7275390625, -0.6807585954666138, -0.5574166774749756);
		assertElement(pi, PathElementType.LINE_TO, 6.73046875, -0.8307876586914062, -0.5623397827148438);
		assertElement(pi, PathElementType.LINE_TO, 6.7333984375, -0.8480333089828491, -0.6408708095550537);
		assertElement(pi, PathElementType.LINE_TO, 6.736328125, -1.1274423599243164, -0.7483196258544922);
		assertElement(pi, PathElementType.LINE_TO, 6.7392578125, -1.280916452407837, -0.8687520027160645);
		assertElement(pi, PathElementType.LINE_TO, 6.7421875, -1.49603271484375, -0.75177001953125);
		assertElement(pi, PathElementType.LINE_TO, 6.7451171875, -0.97319495677948, -0.809668779373169);
		assertElement(pi, PathElementType.LINE_TO, 6.748046875, -1.3902616500854492, -0.9427967071533203);
		assertElement(pi, PathElementType.LINE_TO, 6.7509765625, -1.64882230758667, -1.229766845703125);
		assertElement(pi, PathElementType.LINE_TO, 6.75390625, -1.9088516235351562, -1.1913299560546875);
		assertElement(pi, PathElementType.LINE_TO, 6.7568359375, -1.6964914798736572, -1.2889370918273926);
		assertElement(pi, PathElementType.LINE_TO, 6.759765625, -2.0826549530029297, -1.3632011413574219);
		assertElement(pi, PathElementType.LINE_TO, 6.7626953125, -2.1603236198425293, -1.4349966049194336);
		assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.3359375, -1.0751953125);
		assertElement(pi, PathElementType.LINE_TO, 6.7685546875, -1.1625326871871948, -1.0890836715698242);
		assertElement(pi, PathElementType.LINE_TO, 6.771484375, -1.381178855895996, -1.1249847412109375);
		assertElement(pi, PathElementType.LINE_TO, 6.7744140625, -1.6340606212615967, -1.271064281463623);
		assertElement(pi, PathElementType.LINE_TO, 6.77734375, -1.9522018432617188, -1.228240966796875);
		assertElement(pi, PathElementType.LINE_TO, 6.7802734375, -1.814516305923462, -1.3828973770141602);
		assertElement(pi, PathElementType.LINE_TO, 6.783203125, -2.279703140258789, -1.5583992004394531);
		assertElement(pi, PathElementType.LINE_TO, 6.7861328125, -2.4112024307250977, -1.6760330200195312);
		assertElement(pi, PathElementType.LINE_TO, 6.7890625, -2.62640380859375, -1.3184814453125);
		assertElement(pi, PathElementType.LINE_TO, 6.7919921875, -1.6102542877197266, -1.3554692268371582);
		assertElement(pi, PathElementType.LINE_TO, 6.794921875, -2.1508560180664062, -1.4259452819824219);
		assertElement(pi, PathElementType.LINE_TO, 6.7978515625, -2.4465508460998535, -1.7166051864624023);
		assertElement(pi, PathElementType.LINE_TO, 6.80078125, -2.761871337890625, -1.4244384765625);
		assertElement(pi, PathElementType.LINE_TO, 6.8037109375, -2.241894245147705, -1.5132827758789062);
		assertElement(pi, PathElementType.LINE_TO, 6.806640625, -2.826923370361328, -1.4730148315429688);
		assertElement(pi, PathElementType.LINE_TO, 6.8095703125, -2.8587188720703125, -1.4960823059082031);
		assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.0859375, -0.734375);
		assertElement(pi, PathElementType.LINE_TO, 6.8154296875, -0.7457404732704163, -0.7360502481460571);
		assertElement(pi, PathElementType.LINE_TO, 6.818359375, -0.7785611152648926, -0.740910530090332);
		assertElement(pi, PathElementType.LINE_TO, 6.8212890625, -0.8262169361114502, -0.7636795043945312);
		assertElement(pi, PathElementType.LINE_TO, 6.82421875, -0.9009132385253906, -0.7591934204101562);
		assertElement(pi, PathElementType.LINE_TO, 6.8271484375, -0.9114580154418945, -0.7962543964385986);
		assertElement(pi, PathElementType.LINE_TO, 6.830078125, -1.0684070587158203, -0.8428497314453125);
		assertElement(pi, PathElementType.LINE_TO, 6.8330078125, -1.1671044826507568, -0.9065194129943848);
		assertElement(pi, PathElementType.LINE_TO, 6.8359375, -1.318878173828125, -0.82305908203125);
		assertElement(pi, PathElementType.LINE_TO, 6.8388671875, -0.9927825927734375, -0.8607804775238037);
		assertElement(pi, PathElementType.LINE_TO, 6.841796875, -1.3227577209472656, -0.9465885162353516);
		assertElement(pi, PathElementType.LINE_TO, 6.8447265625, -1.5491111278533936, -1.162865161895752);
		assertElement(pi, PathElementType.LINE_TO, 6.84765625, -1.8038177490234375, -1.09832763671875);
		assertElement(pi, PathElementType.LINE_TO, 6.8505859375, -1.6246917247772217, -1.199014663696289);
		assertElement(pi, PathElementType.LINE_TO, 6.853515625, -2.053403854370117, -1.2791938781738281);
		assertElement(pi, PathElementType.LINE_TO, 6.8564453125, -2.178163528442383, -1.376810073852539);
		assertElement(pi, PathElementType.LINE_TO, 6.859375, -2.419189453125, -1.00439453125);
		assertElement(pi, PathElementType.LINE_TO, 6.8623046875, -1.1180541515350342, -1.0294749736785889);
		assertElement(pi, PathElementType.LINE_TO, 6.865234375, -1.4031505584716797, -1.0959758758544922);
		assertElement(pi, PathElementType.LINE_TO, 6.8681640625, -1.7374632358551025, -1.3040156364440918);
		assertElement(pi, PathElementType.LINE_TO, 6.87109375, -2.1523590087890625, -1.3007965087890625);
		assertElement(pi, PathElementType.LINE_TO, 6.8740234375, -2.0235698223114014, -1.514051914215088);
		assertElement(pi, PathElementType.LINE_TO, 6.876953125, -2.606393814086914, -1.7870597839355469);
		assertElement(pi, PathElementType.LINE_TO, 6.8798828125, -2.794137477874756, -1.9727468490600586);
		assertElement(pi, PathElementType.LINE_TO, 6.8828125, -3.071044921875, -1.630615234375);
		assertElement(pi, PathElementType.LINE_TO, 6.8857421875, -1.9716308116912842, -1.6829776763916016);
		assertElement(pi, PathElementType.LINE_TO, 6.888671875, -2.606008529663086, -1.7893447875976562);
		assertElement(pi, PathElementType.LINE_TO, 6.8916015625, -2.9633851051330566, -2.148587226867676);
		assertElement(pi, PathElementType.LINE_TO, 6.89453125, -3.3406219482421875, -1.859771728515625);
		assertElement(pi, PathElementType.LINE_TO, 6.8974609375, -2.7870025634765625, -1.971480369567871);
		assertElement(pi, PathElementType.LINE_TO, 6.900390625, -3.4595260620117188, -1.94976806640625);
		assertElement(pi, PathElementType.LINE_TO, 6.9033203125, -3.5145578384399414, -1.987722396850586);
		assertElement(pi, PathElementType.LINE_TO, 6.90625, -3.787109375, -1.13671875);
		assertElement(pi, PathElementType.LINE_TO, 6.9091796875, -1.1825634241104126, -1.1425936222076416);
		assertElement(pi, PathElementType.LINE_TO, 6.912109375, -1.3093385696411133, -1.1589069366455078);
		assertElement(pi, PathElementType.LINE_TO, 6.9150390625, -1.4821019172668457, -1.2430572509765625);
		assertElement(pi, PathElementType.LINE_TO, 6.91796875, -1.7411270141601562, -1.2149810791015625);
		assertElement(pi, PathElementType.LINE_TO, 6.9208984375, -1.7374143600463867, -1.340710163116455);
		assertElement(pi, PathElementType.LINE_TO, 6.923828125, -2.2372093200683594, -1.4901885986328125);
		assertElement(pi, PathElementType.LINE_TO, 6.9267578125, -2.490731716156006, -1.670792579650879);
		assertElement(pi, PathElementType.LINE_TO, 6.9296875, -2.86578369140625, -1.3658447265625);
		assertElement(pi, PathElementType.LINE_TO, 6.9326171875, -1.7799220085144043, -1.4580473899841309);
		assertElement(pi, PathElementType.LINE_TO, 6.935546875, -2.5559730529785156, -1.6625251770019531);
		assertElement(pi, PathElementType.LINE_TO, 6.9384765625, -3.020145893096924, -2.1589651107788086);
		assertElement(pi, PathElementType.LINE_TO, 6.94140625, -3.49371337890625, -1.97552490234375);
		assertElement(pi, PathElementType.LINE_TO, 6.9443359375, -2.9877848625183105, -2.1318607330322266);
		assertElement(pi, PathElementType.LINE_TO, 6.947265625, -3.7420310974121094, -2.1835403442382812);
		assertElement(pi, PathElementType.LINE_TO, 6.9501953125, -3.847623825073242, -2.259033203125);
		assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.17138671875, -1.3818359375);
		assertElement(pi, PathElementType.LINE_TO, 6.9560546875, -1.5556890964508057, -1.401869297027588);
		assertElement(pi, PathElementType.LINE_TO, 6.958984375, -1.9898395538330078, -1.4517402648925781);
		assertElement(pi, PathElementType.LINE_TO, 6.9619140625, -2.4859113693237305, -1.7224769592285156);
		assertElement(pi, PathElementType.LINE_TO, 6.96484375, -3.1145782470703125, -1.579620361328125);
		assertElement(pi, PathElementType.LINE_TO, 6.9677734375, -2.7822389602661133, -1.8723230361938477);
		assertElement(pi, PathElementType.LINE_TO, 6.970703125, -3.7221221923828125, -2.1697235107421875);
		assertElement(pi, PathElementType.LINE_TO, 6.9736328125, -3.9561567306518555, -2.362253189086914);
		assertElement(pi, PathElementType.LINE_TO, 6.9765625, -4.3734130859375, -1.518310546875);
		assertElement(pi, PathElementType.LINE_TO, 6.9794921875, -2.125990390777588, -1.5830087661743164);
		assertElement(pi, PathElementType.LINE_TO, 6.982421875, -3.2467689514160156, -1.6968765258789062);
		assertElement(pi, PathElementType.LINE_TO, 6.9853515625, -3.841419219970703, -2.2734947204589844);
		assertElement(pi, PathElementType.LINE_TO, 6.98828125, -4.477264404296875, -1.59075927734375);
		assertElement(pi, PathElementType.LINE_TO, 6.9912109375, -3.3151674270629883, -1.7586231231689453);
		assertElement(pi, PathElementType.LINE_TO, 6.994140625, -4.529991149902344, -1.6282196044921875);
		assertElement(pi, PathElementType.LINE_TO, 6.9970703125, -4.556577682495117, -1.6473045349121094);
		assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 0.0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(Transform3D,double) no-transform")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPathIteratorTransform3DDouble_null(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi = this.shape.getPathIterator(null, SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75, 0);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0, 0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125, 0);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807);
		assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725);
		assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, -5, 0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(Transform3D,double) identity-transform")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPathIteratorTransform3DDouble_identity(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi = this.shape.getPathIterator(new Transform3D(), SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75, 0);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0, 0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125, 0);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617, 0);
		assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780, 0);
		assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542, 0);
		assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, -5, 0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(Transform3D,double) translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPathIteratorTransform3DDouble_translation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D transform = new Transform3D();
		transform.setTranslation(10, 10, 0);
		PathIterator3afp pi = this.shape.getPathIterator(transform, SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 10, 10, 0);
		assertElement(pi, PathElementType.LINE_TO, 11, 11, 0);
		assertElement(pi, PathElementType.LINE_TO, 11.484375, 10.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 11.9375, 10.75, 0);
		assertElement(pi, PathElementType.LINE_TO, 12.359375, 10.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 12.75, 11.0, 0);
		assertElement(pi, PathElementType.LINE_TO, 13.109375, 11.3125, 0);
		assertElement(pi, PathElementType.LINE_TO, 13.4375, 11.75, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.0, 13.0, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.0234375, 12.90807, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.046875, 12.819725, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.070313, 12.734895, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.09375, 12.6535034, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.11719, 12.5754766, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.14063, 12.5007401, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.16406, 12.4292192, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.1875, 12.3608398, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.234375, 12.233208, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.28125, 12.117249, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.328125, 12.012367, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.375, 11.917969, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.421875, 11.833458, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.46875, 11.758239, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.515625, 11.691719, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.5625, 11.6333, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.65625, 11.538391, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.75, 11.46875, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.84375, 11.419617, 0);
		assertElement(pi, PathElementType.LINE_TO, 14.9375, 11.38623, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.03125, 11.363831, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.125, 11.347656, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.21875, 11.332947, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.3125, 11.314941, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.40625, 11.288879, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.5, 11.25, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.59375, 11.193542, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.6875, 11.114746, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.78125, 11.00885, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.828125, 10.944252, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.875, 10.871094, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.921875, 10.788780, 0);
		assertElement(pi, PathElementType.LINE_TO, 15.96875, 10.696716, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.015625, 10.594307, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.0625, 10.480957, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.109375, 10.356071, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.15625, 10.219055, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.179688, 10.145812, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.203125, 10.069313, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.226563, 10-0.010516, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.25, 10-0.09375, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.273438, 10-0.180463, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.296875, 10-0.270729, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.320313, 10-0.364623, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.34375, 10-0.462219, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.36719, 10-0.563592, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.39063, 10-0.668816, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.41406, 10-0.7779646, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.4375, 10-0.891113, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.460938, 10-1.008336, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.484375, 10-1.129707, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.507813, 10-1.255301, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.53125, 10-1.385193, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.55469, 10-1.519456, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.57813, 10-1.658165, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.60156, 10-1.801394, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.625, 10-1.949219, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.63672, 10-2.024877, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.648438, 10-2.101712, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.660156, 10-2.179733, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.671875, 10-2.258949, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.683594, 10-2.33937, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.695313, 10-2.421, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.707031, 10-2.503862, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.71875, 10-2.587952, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.730469, 10-2.673283, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.742188, 10-2.759866, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.753906, 10-2.847709, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.765625, 10-2.936821, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.777344, 10-3.027212, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.789063, 10-3.118892, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.800781, 10-3.211869, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.8125, 10-3.306152, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.824219, 10-3.401752, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.835938, 10-3.498677, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.847656, 10-3.596937, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.859375, 10-3.696541, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.871094, 10-3.797498, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.882813, 10-3.899817, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.894531, 10-4.003509, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.90625, 10-4.108582, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.917969, 10-4.215045, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.929688, 10-4.322907, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.941406, 10-4.432179, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.953125, 10-4.54287, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.964844, 10-4.654987, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.976563, 10-4.768542, 0);
		assertElement(pi, PathElementType.LINE_TO, 16.988281, 10-4.883543, 0);
		assertElement(pi, PathElementType.LINE_TO, 17, 5, 0);
		assertNoElement(pi);
	}

	@DisplayName("getLength")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getLength(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(14.71628, this.shape.getLength());
	}

	@DisplayName("getLengthSquared")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getLengthSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(216.56892, this.shape.getLengthSquared());
	}

	@DisplayName("lineTo(double,double,double) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void lineToDoubleDoubleDouble_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(15, 145, 0);
		});
	}

	@DisplayName("lineTo(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void lineToDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.lineTo(123.456, 456.789, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@DisplayName("lineTo(Point3D) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void lineToPoint3D_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(createPoint(15, 145, 0));
		});
	}

	@DisplayName("lineTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void lineToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.lineTo(createPoint(123.456, 456.789, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@DisplayName("moveTo(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void moveToDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.moveTo(123.456, 456.789, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@DisplayName("moveTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void moveToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.moveTo(createPoint(123.456, 456.789, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@DisplayName("quadTo(double,double,double,double,double,double) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void quadToDoubleDoubleDoubleDoubleDoubleDouble_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.quadTo(15, 145, 0, 50, 20, 0);
		});
	}

	@DisplayName("quadTo(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void quadToDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.quadTo(123.456, 456.789, 0, 789.123, 159.753, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.QUAD_TO, 123.456, 456.789, 0, 789.123, 159.753, 0);
		assertNoElement(pi);
	}
	
	@DisplayName("quadTo(Point3D,Point3D) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void quadToPoint3DPoint3D_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.quadTo(createPoint(15, 145, 0), createPoint(50, 20, 0));
		});
	}

	@DisplayName("quadTo(Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void quadToPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.quadTo(createPoint(123.456, 456.789, 0), createPoint(789.123, 159.753, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.QUAD_TO, 123.456, 456.789, 0, 789.123, 159.753, 0);
		assertNoElement(pi);
	}

	@DisplayName("remove(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void removeDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.remove(5, -1, 0));
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(4, 3, 0)));
		assertTrue(this.shape.remove(1, 1, 0));
		assertTrue(this.shape.size() == 3);
		assertFalse(this.shape.remove(35, 35, 0));
	}

	@DisplayName("setLastPoint(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setLastPointDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(7, -5, 0)));
		this.shape.setLastPoint(2, 2, 0);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(2, 2, 0)));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 2, 2, 0);
		assertNoElement(pi);
	}

	@DisplayName("setLastPoint(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setLastPointPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(7, -5, 0)));
		this.shape.setLastPoint(createPoint(2, 2, 0));
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(2, 2, 0)));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 2, 2, 0);
		assertNoElement(pi);
	}

	@DisplayName("toCollection")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toCollection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Collection<? extends Point3D> collection = this.shape.toCollection();
		assertEquals(7, collection.size());
		Iterator<? extends Point3D> iterator = collection.iterator();
		assertEpsilonEquals(createPoint(0, 0, 0), iterator.next());
		assertEpsilonEquals(createPoint(1, 1, 0), iterator.next());
		assertEpsilonEquals(createPoint(3, 0, 0), iterator.next());
		assertEpsilonEquals(createPoint(4, 3, 0), iterator.next());
		assertEpsilonEquals(createPoint(5, -1, 0), iterator.next());
		assertEpsilonEquals(createPoint(6, 5, 0), iterator.next());
		assertEpsilonEquals(createPoint(7, -5, 0), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@DisplayName("transform(Transform3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void transformTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p1 = randomPoint3d();
		Point3D p2 = randomPoint3d();
		Point3D p3 = randomPoint3d();
		Point3D p4 = randomPoint3d();
		Point3D p5 = randomPoint3d();
		Point3D p6 = randomPoint3d();
		Point3D p7 = randomPoint3d();
		
		Path3afp path = createPath();
		path.moveTo(p1.getX(), p1.getY(), p1.getZ());
		path.lineTo(p2.getX(), p2.getY(), p2.getZ());
		path.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(),p4.getY(), p4.getZ());
		path.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
		path.closePath();
		
		Transform3D trans = new Transform3D(randomMatrix4d());
		
		trans.transform(p1);
		trans.transform(p2);
		trans.transform(p3);
		trans.transform(p4);
		trans.transform(p5);
		trans.transform(p6);
		trans.transform(p7);
		
		Path3afp pathTrans = createPath();
		pathTrans.moveTo(p1.getX(), p1.getY(), p1.getZ());
		pathTrans.lineTo(p2.getX(), p2.getY(), p2.getZ());
		pathTrans.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
		pathTrans.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
		pathTrans.closePath();
		
		path.transform(trans);
		
		assertTrue(path.equalsToPathIterator(pathTrans.getPathIterator()));
	}

	@DisplayName("clone")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp clone = this.shape.clone();
		PathIterator3afp pi = (PathIterator3afp) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertNoElement(pi);
	}

	@DisplayName("equals(Object)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createPath()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(this.shape.clone()));
	}

	@DisplayName("equals(Object -> PathIterator3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createPath().getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(this.shape.clone().getPathIterator()));
	}

	@DisplayName("equalsToPathIterator(PathIterator3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3afp) null));
		assertFalse(this.shape.equalsToPathIterator(createPath().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.clone().getPathIterator()));
	}

	@DisplayName("equalsToShape(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createPath()));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape(this.shape.clone()));
	}

	@DisplayName("isEmpty")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@DisplayName("clear")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		PathIterator3afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@DisplayName("contains(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(-5, 1, 0));
		assertFalse(this.shape.contains(3, 6, 0));
		assertFalse(this.shape.contains(3, -10, 0));
		assertFalse(this.shape.contains(11, 1, 0));
		assertFalse(this.shape.contains(4, 1, 0));
		assertTrue(this.shape.contains(4, 3, 0));
		this.shape.closePath();
		assertFalse(this.shape.contains(-5, 1, 0));
		assertFalse(this.shape.contains(3, 6, 0));
		assertFalse(this.shape.contains(3, -10, 0));
		assertFalse(this.shape.contains(11, 1, 0));
		assertTrue(this.shape.contains(4, 1, 0));
		assertTrue(this.shape.contains(4, 3, 0));
	}

	@DisplayName("contains(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createPoint(-5, 1, 0)));
		assertFalse(this.shape.contains(createPoint(3, 6, 0)));
		assertFalse(this.shape.contains(createPoint(3, -10, 0)));
		assertFalse(this.shape.contains(createPoint(11, 1, 0)));
		assertFalse(this.shape.contains(createPoint(4, 1, 0)));
		assertTrue(this.shape.contains(createPoint(4, 3, 0)));
		this.shape.closePath();
		assertFalse(this.shape.contains(createPoint(-5, 1, 0)));
		assertFalse(this.shape.contains(createPoint(3, 6, 0)));
		assertFalse(this.shape.contains(createPoint(3, -10, 0)));
		assertFalse(this.shape.contains(createPoint(11, 1, 0)));
		assertTrue(this.shape.contains(createPoint(4, 1, 0)));
		assertTrue(this.shape.contains(createPoint(4, 3, 0)));
	}

	@DisplayName("getClosestPointTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		result = this.shape.getClosestPointTo(createPoint(-2, 1, 0));
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = this.shape.getClosestPointTo(createPoint(1, 0, 0));
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());

		result = this.shape.getClosestPointTo(createPoint(3, 0, 0));
		assertEpsilonEquals(2.56307, result.getX());
		assertEpsilonEquals(0.91027, result.getY());

		result = this.shape.getClosestPointTo(createPoint(1, -4, 0));
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
	}

	@DisplayName("getFarthestPointTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		result = this.shape.getFarthestPointTo(createPoint(-2, 1, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = this.shape.getFarthestPointTo(createPoint(1, 0, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = this.shape.getFarthestPointTo(createPoint(3, 0, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = this.shape.getFarthestPointTo(createPoint(1, -4, 0));
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@DisplayName("getDistance(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2.23607, this.shape.getDistance(createPoint(-2, 1, 0)));
		assertEpsilonEquals(.70711, this.shape.getDistance(createPoint(1, 0, 0)));
		assertEpsilonEquals(1.00970, this.shape.getDistance(createPoint(3, 0, 0)));
		assertEpsilonEquals(4.12311, this.shape.getDistance(createPoint(1, -4, 0)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(2.23606, this.shape.getDistance(createPoint(-2, 1, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(1, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(3, 0, 0)));
		assertEpsilonEquals(2.6737, this.shape.getDistance(createPoint(1, -4, 0)));
	}

	@DisplayName("getDistanceSquared(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getDistanceSquared(createPoint(-2, 1, 0)));
		assertEpsilonEquals(.5, this.shape.getDistanceSquared(createPoint(1, 0, 0)));
		assertEpsilonEquals(1.0195, this.shape.getDistanceSquared(createPoint(3, 0, 0)));
		assertEpsilonEquals(17, this.shape.getDistanceSquared(createPoint(1, -4, 0)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(5, this.shape.getDistanceSquared(createPoint(-2, 1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(1, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(3, 0, 0)));
		assertEpsilonEquals(7.14865, this.shape.getDistanceSquared(createPoint(1, -4, 0)));
	}

	@DisplayName("getDistanceL1(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3, this.shape.getDistanceL1(createPoint(-2, 1, 0)));
		assertEpsilonEquals(1, this.shape.getDistanceL1(createPoint(1, 0, 0)));
		assertEpsilonEquals(1.3472, this.shape.getDistanceL1(createPoint(3, 0, 0)));
		assertEpsilonEquals(5, this.shape.getDistanceL1(createPoint(1, -4, 0)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(3, this.shape.getDistanceL1(createPoint(-2, 1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(1, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(3, 0, 0)));

		assertEpsilonEquals(3.72973, this.shape.getDistanceL1(createPoint(1, -4, 0)));
	}

	@DisplayName("getDistanceLinf(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2, this.shape.getDistanceLinf(createPoint(-2, 1, 0)));
		assertEpsilonEquals(.5, this.shape.getDistanceLinf(createPoint(1, 0, 0)));
		assertEpsilonEquals(.91027, this.shape.getDistanceLinf(createPoint(3, 0, 0)));
		assertEpsilonEquals(4, this.shape.getDistanceLinf(createPoint(1, -4, 0)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(2, this.shape.getDistanceLinf(createPoint(-2, 1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(1, 0, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(3, 0, 0)));
		assertEpsilonEquals(2.17568, this.shape.getDistanceLinf(createPoint(1, -4, 0)));
	}

	@DisplayName("set(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createPath());
		PathIterator3afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
		Path3afp path = createPath();
		path.moveTo(123.456, 456.789, 0);
		path.lineTo(789.123, 159.753, 0);
		this.shape.set(path);
		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
		assertElement(pi, PathElementType.LINE_TO, 789.123, 159.753, 0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertNoElement(pi);
		this.shape.closePath();
		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(Transform3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi;
		Transform3D transform = new Transform3D();

		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertNoElement(pi);
		
		transform.setIdentity();
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertNoElement(pi);

		transform.setTranslation(14, -5, 1.5);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 14, -5, 1.5);
		assertElement(pi, PathElementType.LINE_TO, 15, -4, 1.5);
		assertElement(pi, PathElementType.QUAD_TO, 17, -5, 1.5, 18, -2, 1.5);
		assertElement(pi, PathElementType.CURVE_TO, 19, -6, 1.5, 20, 0, 1.5, 21, -10, 1.5);
		assertNoElement(pi);

		this.shape.closePath();
		
		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);

		transform.setIdentity();
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);

		transform.setTranslation(14, -5, 1.5);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 14, -5, 1.5);
		assertElement(pi, PathElementType.LINE_TO, 15, -4, 1.5);
		assertElement(pi, PathElementType.QUAD_TO, 17, -5, 1.5, 18, -2, 1.5);
		assertElement(pi, PathElementType.CURVE_TO, 19, -6, 1.5, 20, 0, 1.5, 21, -10, 1.5);
		assertElement(pi, PathElementType.CLOSE, 14, -5, 1.5);
		assertNoElement(pi);
	}

	@DisplayName("createTransformedShape(Transform3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void createTransformedShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p1 = randomPoint3d();
		Point3D p2 = randomPoint3d();
		Point3D p3 = randomPoint3d();
		Point3D p4 = randomPoint3d();
		Point3D p5 = randomPoint3d();
		Point3D p6 = randomPoint3d();
		Point3D p7 = randomPoint3d();
		
		Path3afp path = createPath();
		path.moveTo(p1.getX(), p1.getY(), p1.getZ());
		path.lineTo(p2.getX(), p2.getY(), p2.getZ());
		path.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(),p4.getY(), p4.getZ());
		path.curveTo(p5.getX(),p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(),p7.getY(), p7.getZ());
		path.closePath();

		Transform3D trans = new Transform3D(randomMatrix4f());
		Path3afp transformedShape = (Path3afp) path.createTransformedShape(trans);
		path.transform(trans);		
	
		assertTrue(path.equalsToShape(transformedShape));
	}

	@DisplayName("translate(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		Path3afp p2 = createPath();
		p2.moveTo(dx, dy, dz);
		p2.lineTo(1 + dx, 1 + dy, 1 + dz);
		p2.quadTo(3 + dx, 0 + dy, 0 + dz, 4 + dx, 3 + dy, 0 + dz);
		p2.curveTo(5 + dx, -1 + dy, 0 + dz, 6 + dx, 5 + dy, 0 + dz, 7 + dx, -5 + dy, 0 + dz);
		
		this.shape.translate(dx, dy, dz);
		
		assertTrue(this.shape.equals(p2));		
	}

	@DisplayName("translate(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		this.shape.translate(dx, dy, dz);
		
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy, dz);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1, dz + 1);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dz, dx + 4, dy + 3, dz + 2);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1,  dz + 3, dx + 6, dy + 5, dz - 2, dx + 7, dy - 5, dz);
		assertNoElement(pi);
	}

	@DisplayName("contains(AlignedBox3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createAlignedBox(-5, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, 6, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, -10, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(11, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(4, 3, 0, 2, 1, 0)));
		this.shape.closePath();
		assertFalse(this.shape.contains(createAlignedBox(-5, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, 6, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, -10, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(11, 1, 0, 2, 1, 0)));
		assertTrue(this.shape.contains(createAlignedBox(3, 0, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(4, 3, 0, 2, 1, 0)));
	}

	@DisplayName("intersects(AlignedBox3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createAlignedBox(1, -2, 0, 2, 1, 0)));
		assertTrue(this.shape.intersects(createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(7, 3, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(-4, -0.5, 0, 2, 1, 0)));
	}

	@DisplayName("intersects(AlignedBox3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsAlignedBox3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertTrue(this.shape.intersects(createAlignedBox(1, -2, 0, 2, 1, 0)));
		assertTrue(this.shape.intersects(createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(7, 3, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(-4, -0.5, 0, 2, 1, 0)));
	}

	@DisplayName("intersects(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSphere(-2, -2, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(2, -2, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(2.5, -1.5, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(10, 0, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(4, 0, 0, 0.5)));
		assertTrue(this.shape.intersects(createSphere(2.5, 1, 0, 0.5)));
	}

	@DisplayName("intersects(Sphere3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSphere3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createSphere(-2, -2, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(2, -2, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(2.5, -1.5, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(10, 0, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(4, 0, 0, 0.5)));
		assertTrue(this.shape.intersects(createSphere(2.5, 1, 0, 0.5)));
	}

	@DisplayName("intersects(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSegment(1, -1, 0, 2, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 0, 2, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(4, 0, 0, 2, -3, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 5, 3, 0)));
	}

	@DisplayName("intersects(Segment3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSegment3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createSegment(1, -1, 0, 2, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 0, 2, -3, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 2, -3, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 5, 3, 0)));
	}

	@DisplayName("intersects(Path3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsPath3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0)));
	}

	@DisplayName("intersects(Path3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPath3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0)));
	}

	@DisplayName("intersects(PathIterator3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsPathIterator3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0).getPathIterator()));
	}

	@DisplayName("intersects(PathIterator3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsPathIterator3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0).getPathIterator()));
	}

	@DisplayName("intersects(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSegment(4, 0, 0, 5, 3, 0)));
		assertTrue(this.shape.intersects((Shape3D)createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
	}

	@DisplayName("p += Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		this.shape.operator_add(createVector(dx, dy, dz));
		
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy, dz);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1, dz + 1);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dz, dx + 4, dy + 3, dz + 2);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dz + 3, dx + 6, dy + 5, dz - 1, dx + 7, dy - 5, dz);
		assertNoElement(pi);
	}

	@DisplayName("p + Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		T shape = this.shape.operator_plus(createVector(dx, dy, dz));
		
		PathIterator3afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
		assertNoElement(pi);
	}

	@DisplayName("p -= Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		this.shape.operator_remove(createVector(dx, dy, dz));
		
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -dx, -dy);
		assertElement(pi, PathElementType.LINE_TO, -dx + 1, -dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, -dx + 3, -dy, -dx + 4, -dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, -dx + 5, -dy - 1, -dx + 6, -dy + 5, -dx + 7, -dy - 5);
		assertNoElement(pi);
	}

	@DisplayName("p - Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		T shape = this.shape.operator_minus(createVector(dx, dy, dz));
		
		PathIterator3afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -dx, -dy);
		assertElement(pi, PathElementType.LINE_TO, -dx + 1, -dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, -dx + 3, -dy, -dx + 4, -dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, -dx + 5, -dy - 1, -dx + 6, -dy + 5, -dx + 7, -dy - 5);
		assertNoElement(pi);
	}

	@DisplayName("p * Tranform3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p1 = randomPoint3d();
		Point3D p2 = randomPoint3d();
		Point3D p3 = randomPoint3d();
		Point3D p4 = randomPoint3d();
		Point3D p5 = randomPoint3d();
		Point3D p6 = randomPoint3d();
		Point3D p7 = randomPoint3d();
		
		Path3afp path = createPath();
		path.moveTo(p1.getX(), p1.getY(), p1.getZ());
		path.lineTo(p2.getX(), p2.getY(), p2.getZ());
		path.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
		path.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
		path.closePath();

		Transform3D trans = new Transform3D(randomMatrix4d());
		Path3afp transformedShape = (Path3afp) path.operator_multiply(trans);
		path.transform(trans);		
	
		assertTrue(path.equalsToShape(transformedShape));
	}

	@DisplayName("p && Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.operator_and(createPoint(-5, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(3, 6, 0)));
		assertFalse(this.shape.operator_and(createPoint(3, -10, 0)));
		assertFalse(this.shape.operator_and(createPoint(11, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(4, 1, 0)));
		assertTrue(this.shape.operator_and(createPoint(4, 3, 0)));
		this.shape.closePath();
		assertFalse(this.shape.operator_and(createPoint(-5, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(3, 6, 0)));
		assertFalse(this.shape.operator_and(createPoint(3, -10, 0)));
		assertFalse(this.shape.operator_and(createPoint(11, 1, 0)));
		assertTrue(this.shape.operator_and(createPoint(4, 1, 0)));
		assertTrue(this.shape.operator_and(createPoint(4, 3, 0)));
	}

	@DisplayName("p && Shape3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSegment(4, 0, 0, 5, 3, 0)));
		assertTrue(this.shape.operator_and(createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
	}

	@DisplayName("p .. Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2.23607, this.shape.operator_upTo(createPoint(-2, 1, 0)));
		assertEpsilonEquals(.70711, this.shape.operator_upTo(createPoint(1, 0, 0)));
		assertEpsilonEquals(1.00970, this.shape.operator_upTo(createPoint(3, 0, 0)));
		assertEpsilonEquals(4.12311, this.shape.operator_upTo(createPoint(1, -4, 0)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(2.23606, this.shape.operator_upTo(createPoint(-2, 1, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(1, 0, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(3, 0, 0)));
		assertEpsilonEquals(2.6737, this.shape.operator_upTo(createPoint(1, -4, 0)));
	}

	@DisplayName("isCurved")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void isCurved(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isCurved());
		this.shape.closePath();
		assertFalse(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.closePath();
		assertFalse(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isCurved());
		this.shape.curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
		assertTrue(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isCurved());
		this.shape.quadTo(7, 8, 0, 9, 10, 0);
		assertTrue(this.shape.isCurved());
	}
	
	@DisplayName("isMultiParts")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void isMultiParts(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isMultiParts());

		this.shape.clear();
		assertFalse(this.shape.isMultiParts());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.closePath();
		assertFalse(this.shape.isMultiParts());

		this.shape.clear();
		assertFalse(this.shape.isMultiParts());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
		assertFalse(this.shape.isMultiParts());

		this.shape.moveTo(1, 2, 0);
		assertTrue(this.shape.isMultiParts());
		this.shape.moveTo(3, 4, 0);
		assertTrue(this.shape.isMultiParts());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isMultiParts());
		this.shape.lineTo(5, 6, 0);
		assertTrue(this.shape.isMultiParts());
		this.shape.quadTo(7, 8, 0, 9, 10, 0);
		assertTrue(this.shape.isMultiParts());
	}
	
	@DisplayName("isPolygon")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void isPolygon(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.quadTo(7, 8, 0, 9, 10, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertFalse(this.shape.isPolygon());
	}
	
	@DisplayName("isPolyline")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void isPolyline(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolyline());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(5, 6, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolyline());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.lineTo(5, 6, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolyline());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.lineTo(5, 6, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.quadTo(7, 8, 0, 9, 10, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());
		
		this.shape.clear();
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.moveTo(5, 6, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(7, 8, 0);
		assertFalse(this.shape.isPolyline());
	}
	
	@DisplayName("getCurrentX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCurrentX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7, this.shape.getCurrentX());
		this.shape.lineTo(154, 485, 0);
		assertEpsilonEquals(154, this.shape.getCurrentX());
	}
	
	@DisplayName("getCurrentY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCurrentY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(-5, this.shape.getCurrentY());
	    this.shape.lineTo(154, 485, 0);
	    assertEpsilonEquals(485, this.shape.getCurrentY());
	}

	@DisplayName("getCurrentZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCurrentZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0, this.shape.getCurrentZ());
		this.shape.lineTo(154, 485, 10);
		assertEpsilonEquals(10, this.shape.getCurrentZ());
	}

	@DisplayName("getCurrentPoint")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCurrentPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(7, -5, 0, this.shape.getCurrentPoint());
		this.shape.lineTo(154, 485, 0);
		assertFpPointEquals(154, 485, 0, this.shape.getCurrentPoint());
	}

}
