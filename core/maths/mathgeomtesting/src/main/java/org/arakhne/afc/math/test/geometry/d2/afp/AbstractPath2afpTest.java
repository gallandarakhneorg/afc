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

package org.arakhne.afc.math.test.geometry.d2.afp;

import static org.arakhne.afc.math.geometry.GeomConstants.SHAPE_INTERSECTS;
import static org.arakhne.afc.math.geometry.GeomConstants.SPLINE_APPROXIMATION_RATIO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Path2D.ArcType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.BasicPathShadow2afp;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;

@SuppressWarnings("all")
public abstract class AbstractPath2afpTest<T extends Path2afp<?, T, ?, ?, ?, B>, B extends Rectangle2afp<?, ?, ?, ?, ?, B>>
extends AbstractShape2afpTest<T, B> {

	@Override
	protected T createShape() {
		T path = (T) createPath();
		path.moveTo(0, 0);
		path.lineTo(1, 1);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		return path;
	}

	protected BasicPathShadow2afp createShadow(int x1, int y1, int x2, int y2) {
		T path = (T) createPath();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		return new BasicPathShadow2afp(path);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorCircleShadow_null(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), -2, -2, 2, null));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2, -2, 2, null));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2.5, -1.5, 2, null));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 10, 0, 2, null));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 4, 0, 0.5, null));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2.5, 1, 0.5, null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorCircleShadow_standard(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), -2, -2, 2, CrossingComputationType.STANDARD));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2, -2, 2, CrossingComputationType.STANDARD));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2.5, -1.5, 2, CrossingComputationType.STANDARD));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 10, 0, 2, CrossingComputationType.STANDARD));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 4, 0, 0.5, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2.5, 1, 0.5, CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorCircleShadow_autoClose(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), -2, -2, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2, -2, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2.5, -1.5, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 10, 0, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 4, 0, 0.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2.5, 1, 0.5, CrossingComputationType.AUTO_CLOSE));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorCircleShadow_simpleIntersectionWhenNotPolygon(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), -2, -2, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2, -2, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2.5, -1.5, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 10, 0, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 4, 0, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorCircleShadow(0, this.shape.getPathIterator(), 2.5, 1, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void statiCalculatesCrossingsPathIteratorCircleShadow_segmentPath(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// One side of the parallelogram in "parallelogram.ggb"
		Path2afp path = createPath();
		path.moveTo(-5.180339887498949, 9);
		path.lineTo(12.70820393249937, -8.888543819998318);
		assertEquals(-2,
				Path2afp.calculatesCrossingsPathIteratorCircleShadow(
						0,
						(PathIterator2afp) path.getPathIterator(),
						0, 2, 1,
						CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorEllipseShadow_null(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 1, -1.5, 2, 1, null));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 1, 1, 2, 1, null));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 4.5, -1, 2, 1, null));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 0, -5.5, 2, 1, null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorEllipseShadow_standard(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 1, -1.5, 2, 1, CrossingComputationType.STANDARD));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 1, 1, 2, 1, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 4.5, -1, 2, 1, CrossingComputationType.STANDARD));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 0, -5.5, 2, 1, CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorEllipseShadow_autoClose(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 1, -1.5, 2, 1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 1, 1, 2, 1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 4.5, -1, 2, 1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 0, -5.5, 2, 1, CrossingComputationType.AUTO_CLOSE));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorEllipseShadow_simpleIntersectionWhenNotPolygon(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 1, -1.5, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 1, 1, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 4.5, -1, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorEllipseShadow(0, this.shape.getPathIterator(), 0, -5.5, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPathShadow_null(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, -3), null));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 5, -3), null));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, 1), null));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(5, 2, 4, 1), null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPathShadow_standard(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, -3), CrossingComputationType.STANDARD));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 5, -3), CrossingComputationType.STANDARD));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, 1), CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(5, 2, 4, 1), CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPathShadow_autoClose(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, -3), CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 5, -3), CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, 1), CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(5, 2, 4, 1), CrossingComputationType.AUTO_CLOSE));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPathShadow_simpleIntersectionWhenNotPolygon(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, -3), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 5, -3), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, 1), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPathShadow(0, this.shape.getPathIterator(),
				createShadow(5, 2, 4, 1), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPathShadow_segmentPath(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		// One side of the parallelogram in "parallelogram.ggb"
		Path2afp path = createPath();
		path.moveTo(-5.180339887498949, 9);
		path.lineTo(12.70820393249937, -8.888543819998318);
		Circle2afp circle = createCircle(0, 2, 1);
		assertEquals(-2,
				Path2afp.calculatesCrossingsPathIteratorPathShadow(
						0,
						(PathIterator2afp) path.getPathIterator(),
						new BasicPathShadow2afp((PathIterator2afp) circle.getPathIterator(), circle.toBoundingBox()),
						CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPointShadow_null(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 1, -0.5, null));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 0, -0.5, null));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 7, 1, null));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 2, 2, null));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 5, 2, null));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 0, 4, null));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 3, 0, null));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 1, 1, null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPointShadow_standard(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 1, -0.5, CrossingComputationType.STANDARD));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 0, -0.5, CrossingComputationType.STANDARD));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 7, 1, CrossingComputationType.STANDARD));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 2, 2, CrossingComputationType.STANDARD));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 5, 2, CrossingComputationType.STANDARD));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 0, 4, CrossingComputationType.STANDARD));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 3, 0, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 1, 1, CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPointShadow_autoClose(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 1, -0.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 0, -0.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 7, 1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 2, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 5, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 0, 4, CrossingComputationType.AUTO_CLOSE));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 3, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 1, 1, CrossingComputationType.AUTO_CLOSE));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPointShadow_simpleIntersectionWhenNotPolygon(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 1, -0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 0, -0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 7, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 2, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 5, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 0, 4, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 3, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 1, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorRectangleShadow_null(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 1, -2, 3, -1, null));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, null));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 7, 3, 9, 4, null));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorRectangleShadow_standard(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 1, -2, 3, -1, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, CrossingComputationType.STANDARD));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 7, 3, 9, 4, CrossingComputationType.STANDARD));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorRectangleShadow_autoClose(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 1, -2, 3, -1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 7, 3, 9, 4, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, CrossingComputationType.AUTO_CLOSE));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorRectangleShadow_simpleIntersectionWhenNotPolygon(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 1, -2, 3, -1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), 7, 3, 9, 4, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRectangleShadow(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorRoundRectangleShadow_null(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 1, -2, 3, -1, .2, .1, null));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, null));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 7, 3, 9, 4, .2, .1, null));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, .1, .2, null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorRoundRectangleShadow_standard(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 1, -2, 3, -1, .2, .1, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, CrossingComputationType.STANDARD));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 7, 3, 9, 4, .2, .1, CrossingComputationType.STANDARD));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, .2, .1, CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorRoundRectangleShadow_autoClose(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 1, -2, 3, -1, .2, .1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 7, 3, 9, 4, .2, .1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, .2, .1, CrossingComputationType.AUTO_CLOSE));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorRoundRectangleShadow_simpleIntersectionWhenNotPolygon(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 1, -2, 3, -1, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), 7, 3, 9, 4, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorRoundRectangleShadow(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorSegmentShadow_null(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 1, -1, 2, -3, null));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 1, -6, 2, -3, null));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 4, 0, 2, -3, null));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 4, 0, 5, 3, null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorSegmentShadow_standard(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 1, -1, 2, -3, CrossingComputationType.STANDARD));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 1, -6, 2, -3, CrossingComputationType.STANDARD));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 4, 0, 2, -3, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 4, 0, 5, 3, CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorSegmentShadow_autoClose(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 1, -1, 2, -3, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 1, -6, 2, -3, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 4, 0, 2, -3, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 4, 0, 5, 3, CrossingComputationType.AUTO_CLOSE));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorSegmentShadow_simpleIntersectionWhenNotPolygon(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 1, -1, 2, -3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 1, -6, 2, -3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 4, 0, 2, -3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorSegmentShadow(0, this.shape.getPathIterator(), 4, 0, 5, 3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorTriangleShadow_null(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				1, -1, 4, 0, 2, .5, null));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				9, 1, 12, 2, 10, 1.5, null));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				5, 0, 8, 1, 6, .5, null));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				-1, -4, 2, -3, 0, -2.5, null));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				3, -6, 6, -5, 4, -4.5, null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorTriangleShadow_standard(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				1, -1, 4, 0, 2, .5, CrossingComputationType.STANDARD));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				9, 1, 12, 2, 10, 1.5, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				5, 0, 8, 1, 6, .5, CrossingComputationType.STANDARD));
		assertEquals(-2, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				-1, -4, 2, -3, 0, -2.5, CrossingComputationType.STANDARD));
		assertEquals(-1, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				3, -6, 6, -5, 4, -4.5, CrossingComputationType.STANDARD));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorTriangleShadow_autoClose(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				1, -1, 4, 0, 2, .5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				9, 1, 12, 2, 10, 1.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				5, 0, 8, 1, 6, .5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				-1, -4, 2, -3, 0, -2.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				3, -6, 6, -5, 4, -4.5, CrossingComputationType.AUTO_CLOSE));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorTriangleShadow_simpleIntersectionWhenNotPolygo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				1, -1, 4, 0, 2, .5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				9, 1, 12, 2, 10, 1.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				5, 0, 8, 1, 6, .5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				-1, -4, 2, -3, 0, -2.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path2afp.calculatesCrossingsPathIteratorTriangleShadow(0, this.shape.getPathIterator(),
				3, -6, 6, -5, 4, -4.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesControlPointBoundingBox(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = createRectangle(0, 0, 0, 0);
		Path2afp.calculatesControlPointBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(5, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDrawableElementBoundingBox(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = createRectangle(0, 0, 0, 0);
		Path2afp.calculatesDrawableElementBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsPoint(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(Path2afp.containsPoint(this.shape.getPathIterator(), -5, 1));
		assertFalse(Path2afp.containsPoint(this.shape.getPathIterator(), 3, 6));
		assertFalse(Path2afp.containsPoint(this.shape.getPathIterator(), 3, -10));
		assertFalse(Path2afp.containsPoint(this.shape.getPathIterator(), 11, 1));
		assertFalse(Path2afp.containsPoint(this.shape.getPathIterator(), 4, 1));
		assertTrue(Path2afp.containsPoint(this.shape.getPathIterator(), 4, 3));
		this.shape.closePath();
		assertFalse(Path2afp.containsPoint(this.shape.getPathIterator(), -5, 1));
		assertFalse(Path2afp.containsPoint(this.shape.getPathIterator(), 3, 6));
		assertFalse(Path2afp.containsPoint(this.shape.getPathIterator(), 3, -10));
		assertFalse(Path2afp.containsPoint(this.shape.getPathIterator(), 11, 1));
		assertTrue(Path2afp.containsPoint(this.shape.getPathIterator(), 4, 1));
		assertTrue(Path2afp.containsPoint(this.shape.getPathIterator(), 4, 3));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsRectangle(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), -5, 1, 2, 1));
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), 3, 6, 2, 1));
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), 3, -10, 2, 1));
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), 11, 1, 2, 1));
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), 3, 1, 2, 1));
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), 4, 3, 2, 1));
		this.shape.closePath();
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), -5, 1, 2, 1));
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), 3, 6, 2, 1));
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), 3, -10, 2, 1));
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), 11, 1, 2, 1));
		assertTrue(Path2afp.containsRectangle(this.shape.getPathIterator(), 3, 0, 2, 1));
		assertFalse(Path2afp.containsRectangle(this.shape.getPathIterator(), 4, 3, 2, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointPathIteratorPoint_open(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, result);
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, result);
		assertEpsilonEquals(2.56307, result.getX());
		assertEpsilonEquals(0.91027, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointPathIteratorPoint_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();

		Point2D result;

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, result);
		assertEpsilonEquals(3, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, result);
		assertEpsilonEquals(2.55405, result.getX());
		assertEpsilonEquals(-1.82432, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsFarthestPointPathIteratorPoint_open(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, result);
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsFarthestPointPathIteratorPoint_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();

		Point2D result;

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path2afp.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, result);
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsPathIteratorRectangle_open(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(Path2afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 2, 1));
		assertTrue(Path2afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 2, 1));
		assertFalse(Path2afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 2, 1));
		assertFalse(Path2afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 2, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsPathIteratorRectangle_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertTrue(Path2afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 2, 1));
		assertTrue(Path2afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 2, 1));
		assertFalse(Path2afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 2, 1));
		assertFalse(Path2afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 2, 1));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesPathLength_open(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(14.71628, Path2afp.calculatesPathLength(this.shape.getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesPathLength_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertEpsilonEquals(23.31861, Path2afp.calculatesPathLength(this.shape.getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addIterator_open(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp<?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5);
		p2.lineTo(4, 6);
		p2.lineTo(0, 8);
		p2.lineTo(5, -3);
		p2.closePath();

		Iterator<? extends PathElement2afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.LINE_TO, 4, 6);
		assertElement(pi, PathElementType.LINE_TO, 0, 8);
		assertElement(pi, PathElementType.LINE_TO, 5, -3);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addIterator_closeAfter(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp<?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5);
		p2.lineTo(4, 6);
		p2.lineTo(0, 8);
		p2.lineTo(5, -3);
		p2.closePath();

		Iterator<? extends PathElement2afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		this.shape.closePath();

		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.LINE_TO, 4, 6);
		assertElement(pi, PathElementType.LINE_TO, 0, 8);
		assertElement(pi, PathElementType.LINE_TO, 5, -3);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addIterator_closeBefore(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();

		Path2afp<?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5);
		p2.lineTo(4, 6);
		p2.lineTo(0, 8);
		p2.lineTo(5, -3);
		p2.closePath();

		Iterator<? extends PathElement2afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 6);
		assertElement(pi, PathElementType.LINE_TO, 0, 8);
		assertElement(pi, PathElementType.LINE_TO, 5, -3);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void curveToDoubleDoubleDoubleDoubleDoubleDouble_noMoveTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(15, 145, 50, 20, 0, 0);
		});
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void curveToDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.curveTo(123.456, 456.789, 789.123, 159.753, 456.852, 963.789);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 123.456,  456.789, 789.123, 159.753, 456.852, 963.789);
		assertNoElement(pi);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void curveToPoint2DPoint2DPoint2D_noMoveTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void curveToPoint2DPoint2DPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.curveTo(createPoint(123.456, 456.789), createPoint(789.123, 159.753), createPoint(456.852, 963.789));
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 123.456,  456.789, 789.123, 159.753, 456.852, 963.789);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_01_arcOnly(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.ARC_ONLY);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_01_lineTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.LINE_THEN_ARC);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_01_moveTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.MOVE_THEN_ARC);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_0251_arcOnly(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, .25, 1, ArcType.ARC_ONLY);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 8.95958, 3.63357, 13.7868, 7.92893, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_0251_lineTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, .25, 1, ArcType.LINE_THEN_ARC);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.LINE_TO, 7.40028, -0.71462);
		assertElement(pi, PathElementType.CURVE_TO, 8.95958, 3.63357, 13.7868, 7.92893, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_0251_moveTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, .25, 1, ArcType.MOVE_THEN_ARC);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.MOVE_TO, 7.40028, -0.71462);
		assertElement(pi, PathElementType.CURVE_TO, 8.95958, 3.63357, 13.7868, 7.92893, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToPoint2DPoint2DDoubleDoubleArcType_01_arcOnly(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(createPoint(5, 5), createPoint(20, 10), 0, 1, ArcType.ARC_ONLY);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToPoint2DPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(createPoint(5, 5), createPoint(20, 10));
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCoordAt(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.getCoordAt(0)==0);
		assertTrue(this.shape.getCoordAt(1)==0);
		assertTrue(this.shape.getCoordAt(2)==1);
		assertTrue(this.shape.getCoordAt(3)==1);
		assertTrue(this.shape.getCoordAt(4)==3);
		assertTrue(this.shape.getCoordAt(5)==0);
		assertTrue(this.shape.getCoordAt(6)==4);
		assertTrue(this.shape.getCoordAt(7)==3);
		assertTrue(this.shape.getCoordAt(8)==5);
		assertTrue(this.shape.getCoordAt(9)==-1);
		assertTrue(this.shape.getCoordAt(10)==6);
		assertTrue(this.shape.getCoordAt(11)==5);
		assertTrue(this.shape.getCoordAt(12)==7);
		assertTrue(this.shape.getCoordAt(13)==-5);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorDouble_open(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0);
		assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807);
		assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725);
		assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895);
		assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034);
		assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766);
		assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401);
		assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192);
		assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398);
		assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208);
		assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249);
		assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367);
		assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969);
		assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458);
		assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239);
		assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719);
		assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333);
		assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391);
		assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875);
		assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617);
		assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623);
		assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831);
		assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656);
		assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947);
		assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941);
		assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879);
		assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25);
		assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542);
		assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746);
		assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885);
		assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252);
		assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094);
		assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780);
		assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716);
		assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307);
		assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957);
		assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071);
		assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055);
		assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812);
		assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313);
		assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516);
		assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375);
		assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463);
		assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729);
		assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623);
		assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219);
		assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592);
		assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816);
		assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646);
		assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113);
		assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336);
		assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707);
		assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301);
		assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193);
		assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456);
		assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165);
		assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394);
		assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219);
		assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877);
		assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712);
		assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733);
		assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949);
		assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937);
		assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421);
		assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862);
		assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952);
		assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283);
		assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866);
		assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709);
		assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821);
		assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212);
		assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892);
		assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869);
		assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152);
		assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752);
		assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677);
		assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937);
		assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541);
		assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498);
		assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817);
		assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509);
		assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582);
		assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045);
		assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907);
		assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179);
		assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287);
		assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987);
		assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542);
		assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543);
		assertElement(pi, PathElementType.LINE_TO, 7, -5);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorDouble_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		PathIterator2afp pi = this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0);
		assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807);
		assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725);
		assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895);
		assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034);
		assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766);
		assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401);
		assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192);
		assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398);
		assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208);
		assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249);
		assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367);
		assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969);
		assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458);
		assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239);
		assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719);
		assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333);
		assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391);
		assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875);
		assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617);
		assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623);
		assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831);
		assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656);
		assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947);
		assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941);
		assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879);
		assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25);
		assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542);
		assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746);
		assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885);
		assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252);
		assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094);
		assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780);
		assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716);
		assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307);
		assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957);
		assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071);
		assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055);
		assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812);
		assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313);
		assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516);
		assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375);
		assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463);
		assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729);
		assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623);
		assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219);
		assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592);
		assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816);
		assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646);
		assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113);
		assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336);
		assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707);
		assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301);
		assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193);
		assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456);
		assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165);
		assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394);
		assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219);
		assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877);
		assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712);
		assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733);
		assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949);
		assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937);
		assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421);
		assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862);
		assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952);
		assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283);
		assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866);
		assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709);
		assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821);
		assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212);
		assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892);
		assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869);
		assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152);
		assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752);
		assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677);
		assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937);
		assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541);
		assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498);
		assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817);
		assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509);
		assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582);
		assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045);
		assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907);
		assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179);
		assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287);
		assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987);
		assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542);
		assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543);
		assertElement(pi, PathElementType.LINE_TO, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorTransform2DDouble_null(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator(null, SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0);
		assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807);
		assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725);
		assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895);
		assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034);
		assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766);
		assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401);
		assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192);
		assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398);
		assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208);
		assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249);
		assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367);
		assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969);
		assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458);
		assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239);
		assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719);
		assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333);
		assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391);
		assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875);
		assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617);
		assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623);
		assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831);
		assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656);
		assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947);
		assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941);
		assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879);
		assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25);
		assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542);
		assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746);
		assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885);
		assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252);
		assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094);
		assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780);
		assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716);
		assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307);
		assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957);
		assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071);
		assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055);
		assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812);
		assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313);
		assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516);
		assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375);
		assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463);
		assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729);
		assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623);
		assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219);
		assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592);
		assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816);
		assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646);
		assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113);
		assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336);
		assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707);
		assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301);
		assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193);
		assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456);
		assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165);
		assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394);
		assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219);
		assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877);
		assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712);
		assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733);
		assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949);
		assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937);
		assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421);
		assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862);
		assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952);
		assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283);
		assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866);
		assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709);
		assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821);
		assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212);
		assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892);
		assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869);
		assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152);
		assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752);
		assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677);
		assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937);
		assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541);
		assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498);
		assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817);
		assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509);
		assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582);
		assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045);
		assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907);
		assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179);
		assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287);
		assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987);
		assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542);
		assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543);
		assertElement(pi, PathElementType.LINE_TO, 7, -5);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorTransform2DDouble_identity(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator(new Transform2D(), SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0);
		assertElement(pi, PathElementType.LINE_TO, 4.0234375, 2.90807);
		assertElement(pi, PathElementType.LINE_TO, 4.046875, 2.819725);
		assertElement(pi, PathElementType.LINE_TO, 4.070313, 2.734895);
		assertElement(pi, PathElementType.LINE_TO, 4.09375, 2.6535034);
		assertElement(pi, PathElementType.LINE_TO, 4.11719, 2.5754766);
		assertElement(pi, PathElementType.LINE_TO, 4.14063, 2.5007401);
		assertElement(pi, PathElementType.LINE_TO, 4.16406, 2.4292192);
		assertElement(pi, PathElementType.LINE_TO, 4.1875, 2.3608398);
		assertElement(pi, PathElementType.LINE_TO, 4.234375, 2.233208);
		assertElement(pi, PathElementType.LINE_TO, 4.28125, 2.117249);
		assertElement(pi, PathElementType.LINE_TO, 4.328125, 2.012367);
		assertElement(pi, PathElementType.LINE_TO, 4.375, 1.917969);
		assertElement(pi, PathElementType.LINE_TO, 4.421875, 1.833458);
		assertElement(pi, PathElementType.LINE_TO, 4.46875, 1.758239);
		assertElement(pi, PathElementType.LINE_TO, 4.515625, 1.691719);
		assertElement(pi, PathElementType.LINE_TO, 4.5625, 1.6333);
		assertElement(pi, PathElementType.LINE_TO, 4.65625, 1.538391);
		assertElement(pi, PathElementType.LINE_TO, 4.75, 1.46875);
		assertElement(pi, PathElementType.LINE_TO, 4.84375, 1.419617);
		assertElement(pi, PathElementType.LINE_TO, 4.9375, 1.38623);
		assertElement(pi, PathElementType.LINE_TO, 5.03125, 1.363831);
		assertElement(pi, PathElementType.LINE_TO, 5.125, 1.347656);
		assertElement(pi, PathElementType.LINE_TO, 5.21875, 1.332947);
		assertElement(pi, PathElementType.LINE_TO, 5.3125, 1.314941);
		assertElement(pi, PathElementType.LINE_TO, 5.40625, 1.288879);
		assertElement(pi, PathElementType.LINE_TO, 5.5, 1.25);
		assertElement(pi, PathElementType.LINE_TO, 5.59375, 1.193542);
		assertElement(pi, PathElementType.LINE_TO, 5.6875, 1.114746);
		assertElement(pi, PathElementType.LINE_TO, 5.78125, 1.00885);
		assertElement(pi, PathElementType.LINE_TO, 5.828125, 0.944252);
		assertElement(pi, PathElementType.LINE_TO, 5.875, 0.871094);
		assertElement(pi, PathElementType.LINE_TO, 5.921875, 0.788780);
		assertElement(pi, PathElementType.LINE_TO, 5.96875, 0.696716);
		assertElement(pi, PathElementType.LINE_TO, 6.015625, 0.594307);
		assertElement(pi, PathElementType.LINE_TO, 6.0625, 0.480957);
		assertElement(pi, PathElementType.LINE_TO, 6.109375, 0.356071);
		assertElement(pi, PathElementType.LINE_TO, 6.15625, 0.219055);
		assertElement(pi, PathElementType.LINE_TO, 6.179688, 0.145812);
		assertElement(pi, PathElementType.LINE_TO, 6.203125, 0.069313);
		assertElement(pi, PathElementType.LINE_TO, 6.226563, -0.010516);
		assertElement(pi, PathElementType.LINE_TO, 6.25, -0.09375);
		assertElement(pi, PathElementType.LINE_TO, 6.273438, -0.180463);
		assertElement(pi, PathElementType.LINE_TO, 6.296875, -0.270729);
		assertElement(pi, PathElementType.LINE_TO, 6.320313, -0.364623);
		assertElement(pi, PathElementType.LINE_TO, 6.34375, -0.462219);
		assertElement(pi, PathElementType.LINE_TO, 6.36719, -0.563592);
		assertElement(pi, PathElementType.LINE_TO, 6.39063, -0.668816);
		assertElement(pi, PathElementType.LINE_TO, 6.41406, -0.7779646);
		assertElement(pi, PathElementType.LINE_TO, 6.4375, -0.891113);
		assertElement(pi, PathElementType.LINE_TO, 6.460938, -1.008336);
		assertElement(pi, PathElementType.LINE_TO, 6.484375, -1.129707);
		assertElement(pi, PathElementType.LINE_TO, 6.507813, -1.255301);
		assertElement(pi, PathElementType.LINE_TO, 6.53125, -1.385193);
		assertElement(pi, PathElementType.LINE_TO, 6.55469, -1.519456);
		assertElement(pi, PathElementType.LINE_TO, 6.57813, -1.658165);
		assertElement(pi, PathElementType.LINE_TO, 6.60156, -1.801394);
		assertElement(pi, PathElementType.LINE_TO, 6.625, -1.949219);
		assertElement(pi, PathElementType.LINE_TO, 6.63672, -2.024877);
		assertElement(pi, PathElementType.LINE_TO, 6.648438, -2.101712);
		assertElement(pi, PathElementType.LINE_TO, 6.660156, -2.179733);
		assertElement(pi, PathElementType.LINE_TO, 6.671875, -2.258949);
		assertElement(pi, PathElementType.LINE_TO, 6.683594, -2.33937);
		assertElement(pi, PathElementType.LINE_TO, 6.695313, -2.421);
		assertElement(pi, PathElementType.LINE_TO, 6.707031, -2.503862);
		assertElement(pi, PathElementType.LINE_TO, 6.71875, -2.587952);
		assertElement(pi, PathElementType.LINE_TO, 6.730469, -2.673283);
		assertElement(pi, PathElementType.LINE_TO, 6.742188, -2.759866);
		assertElement(pi, PathElementType.LINE_TO, 6.753906, -2.847709);
		assertElement(pi, PathElementType.LINE_TO, 6.765625, -2.936821);
		assertElement(pi, PathElementType.LINE_TO, 6.777344, -3.027212);
		assertElement(pi, PathElementType.LINE_TO, 6.789063, -3.118892);
		assertElement(pi, PathElementType.LINE_TO, 6.800781, -3.211869);
		assertElement(pi, PathElementType.LINE_TO, 6.8125, -3.306152);
		assertElement(pi, PathElementType.LINE_TO, 6.824219, -3.401752);
		assertElement(pi, PathElementType.LINE_TO, 6.835938, -3.498677);
		assertElement(pi, PathElementType.LINE_TO, 6.847656, -3.596937);
		assertElement(pi, PathElementType.LINE_TO, 6.859375, -3.696541);
		assertElement(pi, PathElementType.LINE_TO, 6.871094, -3.797498);
		assertElement(pi, PathElementType.LINE_TO, 6.882813, -3.899817);
		assertElement(pi, PathElementType.LINE_TO, 6.894531, -4.003509);
		assertElement(pi, PathElementType.LINE_TO, 6.90625, -4.108582);
		assertElement(pi, PathElementType.LINE_TO, 6.917969, -4.215045);
		assertElement(pi, PathElementType.LINE_TO, 6.929688, -4.322907);
		assertElement(pi, PathElementType.LINE_TO, 6.941406, -4.432179);
		assertElement(pi, PathElementType.LINE_TO, 6.953125, -4.54287);
		assertElement(pi, PathElementType.LINE_TO, 6.964844, -4.654987);
		assertElement(pi, PathElementType.LINE_TO, 6.976563, -4.768542);
		assertElement(pi, PathElementType.LINE_TO, 6.988281, -4.883543);
		assertElement(pi, PathElementType.LINE_TO, 7, -5);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorTransform2DDouble_translation(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D transform = new Transform2D();
		transform.setTranslation(10, 10);
		PathIterator2afp pi = this.shape.getPathIterator(transform, SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 10, 10);
		assertElement(pi, PathElementType.LINE_TO, 11, 11);
		assertElement(pi, PathElementType.LINE_TO, 11.484375, 10.8125);
		assertElement(pi, PathElementType.LINE_TO, 11.9375, 10.75);
		assertElement(pi, PathElementType.LINE_TO, 12.359375, 10.8125);
		assertElement(pi, PathElementType.LINE_TO, 12.75, 11.0);
		assertElement(pi, PathElementType.LINE_TO, 13.109375, 11.3125);
		assertElement(pi, PathElementType.LINE_TO, 13.4375, 11.75);
		assertElement(pi, PathElementType.LINE_TO, 14.0, 13.0);
		assertElement(pi, PathElementType.LINE_TO, 14.0234375, 12.90807);
		assertElement(pi, PathElementType.LINE_TO, 14.046875, 12.819725);
		assertElement(pi, PathElementType.LINE_TO, 14.070313, 12.734895);
		assertElement(pi, PathElementType.LINE_TO, 14.09375, 12.6535034);
		assertElement(pi, PathElementType.LINE_TO, 14.11719, 12.5754766);
		assertElement(pi, PathElementType.LINE_TO, 14.14063, 12.5007401);
		assertElement(pi, PathElementType.LINE_TO, 14.16406, 12.4292192);
		assertElement(pi, PathElementType.LINE_TO, 14.1875, 12.3608398);
		assertElement(pi, PathElementType.LINE_TO, 14.234375, 12.233208);
		assertElement(pi, PathElementType.LINE_TO, 14.28125, 12.117249);
		assertElement(pi, PathElementType.LINE_TO, 14.328125, 12.012367);
		assertElement(pi, PathElementType.LINE_TO, 14.375, 11.917969);
		assertElement(pi, PathElementType.LINE_TO, 14.421875, 11.833458);
		assertElement(pi, PathElementType.LINE_TO, 14.46875, 11.758239);
		assertElement(pi, PathElementType.LINE_TO, 14.515625, 11.691719);
		assertElement(pi, PathElementType.LINE_TO, 14.5625, 11.6333);
		assertElement(pi, PathElementType.LINE_TO, 14.65625, 11.538391);
		assertElement(pi, PathElementType.LINE_TO, 14.75, 11.46875);
		assertElement(pi, PathElementType.LINE_TO, 14.84375, 11.419617);
		assertElement(pi, PathElementType.LINE_TO, 14.9375, 11.38623);
		assertElement(pi, PathElementType.LINE_TO, 15.03125, 11.363831);
		assertElement(pi, PathElementType.LINE_TO, 15.125, 11.347656);
		assertElement(pi, PathElementType.LINE_TO, 15.21875, 11.332947);
		assertElement(pi, PathElementType.LINE_TO, 15.3125, 11.314941);
		assertElement(pi, PathElementType.LINE_TO, 15.40625, 11.288879);
		assertElement(pi, PathElementType.LINE_TO, 15.5, 11.25);
		assertElement(pi, PathElementType.LINE_TO, 15.59375, 11.193542);
		assertElement(pi, PathElementType.LINE_TO, 15.6875, 11.114746);
		assertElement(pi, PathElementType.LINE_TO, 15.78125, 11.00885);
		assertElement(pi, PathElementType.LINE_TO, 15.828125, 10.944252);
		assertElement(pi, PathElementType.LINE_TO, 15.875, 10.871094);
		assertElement(pi, PathElementType.LINE_TO, 15.921875, 10.788780);
		assertElement(pi, PathElementType.LINE_TO, 15.96875, 10.696716);
		assertElement(pi, PathElementType.LINE_TO, 16.015625, 10.594307);
		assertElement(pi, PathElementType.LINE_TO, 16.0625, 10.480957);
		assertElement(pi, PathElementType.LINE_TO, 16.109375, 10.356071);
		assertElement(pi, PathElementType.LINE_TO, 16.15625, 10.219055);
		assertElement(pi, PathElementType.LINE_TO, 16.179688, 10.145812);
		assertElement(pi, PathElementType.LINE_TO, 16.203125, 10.069313);
		assertElement(pi, PathElementType.LINE_TO, 16.226563, 10-0.010516);
		assertElement(pi, PathElementType.LINE_TO, 16.25, 10-0.09375);
		assertElement(pi, PathElementType.LINE_TO, 16.273438, 10-0.180463);
		assertElement(pi, PathElementType.LINE_TO, 16.296875, 10-0.270729);
		assertElement(pi, PathElementType.LINE_TO, 16.320313, 10-0.364623);
		assertElement(pi, PathElementType.LINE_TO, 16.34375, 10-0.462219);
		assertElement(pi, PathElementType.LINE_TO, 16.36719, 10-0.563592);
		assertElement(pi, PathElementType.LINE_TO, 16.39063, 10-0.668816);
		assertElement(pi, PathElementType.LINE_TO, 16.41406, 10-0.7779646);
		assertElement(pi, PathElementType.LINE_TO, 16.4375, 10-0.891113);
		assertElement(pi, PathElementType.LINE_TO, 16.460938, 10-1.008336);
		assertElement(pi, PathElementType.LINE_TO, 16.484375, 10-1.129707);
		assertElement(pi, PathElementType.LINE_TO, 16.507813, 10-1.255301);
		assertElement(pi, PathElementType.LINE_TO, 16.53125, 10-1.385193);
		assertElement(pi, PathElementType.LINE_TO, 16.55469, 10-1.519456);
		assertElement(pi, PathElementType.LINE_TO, 16.57813, 10-1.658165);
		assertElement(pi, PathElementType.LINE_TO, 16.60156, 10-1.801394);
		assertElement(pi, PathElementType.LINE_TO, 16.625, 10-1.949219);
		assertElement(pi, PathElementType.LINE_TO, 16.63672, 10-2.024877);
		assertElement(pi, PathElementType.LINE_TO, 16.648438, 10-2.101712);
		assertElement(pi, PathElementType.LINE_TO, 16.660156, 10-2.179733);
		assertElement(pi, PathElementType.LINE_TO, 16.671875, 10-2.258949);
		assertElement(pi, PathElementType.LINE_TO, 16.683594, 10-2.33937);
		assertElement(pi, PathElementType.LINE_TO, 16.695313, 10-2.421);
		assertElement(pi, PathElementType.LINE_TO, 16.707031, 10-2.503862);
		assertElement(pi, PathElementType.LINE_TO, 16.71875, 10-2.587952);
		assertElement(pi, PathElementType.LINE_TO, 16.730469, 10-2.673283);
		assertElement(pi, PathElementType.LINE_TO, 16.742188, 10-2.759866);
		assertElement(pi, PathElementType.LINE_TO, 16.753906, 10-2.847709);
		assertElement(pi, PathElementType.LINE_TO, 16.765625, 10-2.936821);
		assertElement(pi, PathElementType.LINE_TO, 16.777344, 10-3.027212);
		assertElement(pi, PathElementType.LINE_TO, 16.789063, 10-3.118892);
		assertElement(pi, PathElementType.LINE_TO, 16.800781, 10-3.211869);
		assertElement(pi, PathElementType.LINE_TO, 16.8125, 10-3.306152);
		assertElement(pi, PathElementType.LINE_TO, 16.824219, 10-3.401752);
		assertElement(pi, PathElementType.LINE_TO, 16.835938, 10-3.498677);
		assertElement(pi, PathElementType.LINE_TO, 16.847656, 10-3.596937);
		assertElement(pi, PathElementType.LINE_TO, 16.859375, 10-3.696541);
		assertElement(pi, PathElementType.LINE_TO, 16.871094, 10-3.797498);
		assertElement(pi, PathElementType.LINE_TO, 16.882813, 10-3.899817);
		assertElement(pi, PathElementType.LINE_TO, 16.894531, 10-4.003509);
		assertElement(pi, PathElementType.LINE_TO, 16.90625, 10-4.108582);
		assertElement(pi, PathElementType.LINE_TO, 16.917969, 10-4.215045);
		assertElement(pi, PathElementType.LINE_TO, 16.929688, 10-4.322907);
		assertElement(pi, PathElementType.LINE_TO, 16.941406, 10-4.432179);
		assertElement(pi, PathElementType.LINE_TO, 16.953125, 10-4.54287);
		assertElement(pi, PathElementType.LINE_TO, 16.964844, 10-4.654987);
		assertElement(pi, PathElementType.LINE_TO, 16.976563, 10-4.768542);
		assertElement(pi, PathElementType.LINE_TO, 16.988281, 10-4.883543);
		assertElement(pi, PathElementType.LINE_TO, 17, 5);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getLength(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(14.71628, this.shape.getLength());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getLengthSquared(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(216.56892, this.shape.getLengthSquared());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lineToIntInt_noMoveTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(15, 145);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lineToDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.lineTo(123.456, 456.789);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lineToPoint2D_noMoveTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(createPoint(15, 145));
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lineToPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.lineTo(createPoint(123.456, 456.789));
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void moveToDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.moveTo(123.456, 456.789);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void moveToPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.moveTo(createPoint(123.456, 456.789));
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void quadToIntIntIntInt_noMoveTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.quadTo(15, 145, 50, 20);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void quadToDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.quadTo(123.456, 456.789, 789.123, 159.753);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.QUAD_TO, 123.456,  456.789, 789.123, 159.753);
		assertNoElement(pi);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void quadToPoint2DPoint2D_noMoveTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void quadToPoint2DPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.quadTo(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.QUAD_TO, 123.456,  456.789, 789.123, 159.753);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void removeDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.remove(5, -1));
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(4, 3)));
		assertTrue(this.shape.remove(1, 1));
		assertTrue(this.shape.size() == 3);
		assertFalse(this.shape.remove(35, 35));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setLastPointDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(7, -5)));
		this.shape.setLastPoint(2, 2);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(2, 2)));
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 2, 2);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setLastPointPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(7, -5)));
		this.shape.setLastPoint(createPoint(2, 2));
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(2, 2)));
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 2, 2);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toCollection(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Collection<? extends Point2D> collection = this.shape.toCollection();
		assertEquals(7, collection.size());
		Iterator<? extends Point2D> iterator = collection.iterator();
		assertEpsilonEquals(createPoint(0, 0), iterator.next());
		assertEpsilonEquals(createPoint(1, 1), iterator.next());
		assertEpsilonEquals(createPoint(3, 0), iterator.next());
		assertEpsilonEquals(createPoint(4, 3), iterator.next());
		assertEpsilonEquals(createPoint(5, -1), iterator.next());
		assertEpsilonEquals(createPoint(6, 5), iterator.next());
		assertEpsilonEquals(createPoint(7, -5), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void transformTransform2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p1 = randomPoint2f();
		Point2D p2 = randomPoint2d();
		Point2D p3 = randomPoint2d();
		Point2D p4 = randomPoint2d();
		Point2D p5 = randomPoint2d();
		Point2D p6 = randomPoint2d();
		Point2D p7 = randomPoint2d();
		
		Path2afp path = createPath();
		path.moveTo(p1.getX(),p1.getY());
		path.lineTo(p2.getX(),p2.getY());
		path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		path.closePath();
		
		Transform2D trans = new Transform2D(this.randomMatrix3f());
		
		trans.transform(p1);
		trans.transform(p2);
		trans.transform(p3);
		trans.transform(p4);
		trans.transform(p5);
		trans.transform(p6);
		trans.transform(p7);
		
		Path2afp pathTrans = createPath();
		pathTrans.moveTo(p1.getX(),p1.getY());
		pathTrans.lineTo(p2.getX(),p2.getY());
		pathTrans.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		pathTrans.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		pathTrans.closePath();
		
		path.transform(trans);
		
		assertTrue(path.equalsToPathIterator(pathTrans.getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void testClone(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp clone = this.shape.clone();
		PathIterator2afp pi = (PathIterator2afp) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createPath()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(this.shape.clone()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createPath().getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(this.shape.clone().getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createPath().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.clone().getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToShape(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createPath()));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape(this.shape.clone()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isEmpty(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void clear(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		PathIterator2afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(-5, 1));
		assertFalse(this.shape.contains(3, 6));
		assertFalse(this.shape.contains(3, -10));
		assertFalse(this.shape.contains(11, 1));
		assertFalse(this.shape.contains(4, 1));
		assertTrue(this.shape.contains(4, 3));
		this.shape.closePath();
		assertFalse(this.shape.contains(-5, 1));
		assertFalse(this.shape.contains(3, 6));
		assertFalse(this.shape.contains(3, -10));
		assertFalse(this.shape.contains(11, 1));
		assertTrue(this.shape.contains(4, 1));
		assertTrue(this.shape.contains(4, 3));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createPoint(-5, 1)));
		assertFalse(this.shape.contains(createPoint(3, 6)));
		assertFalse(this.shape.contains(createPoint(3, -10)));
		assertFalse(this.shape.contains(createPoint(11, 1)));
		assertFalse(this.shape.contains(createPoint(4, 1)));
		assertTrue(this.shape.contains(createPoint(4, 3)));
		this.shape.closePath();
		assertFalse(this.shape.contains(createPoint(-5, 1)));
		assertFalse(this.shape.contains(createPoint(3, 6)));
		assertFalse(this.shape.contains(createPoint(3, -10)));
		assertFalse(this.shape.contains(createPoint(11, 1)));
		assertTrue(this.shape.contains(createPoint(4, 1)));
		assertTrue(this.shape.contains(createPoint(4, 3)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;

		result = this.shape.getClosestPointTo(createPoint(-2, 1));
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = this.shape.getClosestPointTo(createPoint(1, 0));
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());

		result = this.shape.getClosestPointTo(createPoint(3, 0));
		assertEpsilonEquals(2.56307, result.getX());
		assertEpsilonEquals(0.91027, result.getY());

		result = this.shape.getClosestPointTo(createPoint(1, -4));
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getFarthestPointTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;

		result = this.shape.getFarthestPointTo(createPoint(-2, 1));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = this.shape.getFarthestPointTo(createPoint(1, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = this.shape.getFarthestPointTo(createPoint(3, 0));
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = this.shape.getFarthestPointTo(createPoint(1, -4));
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistance(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2.23607, this.shape.getDistance(createPoint(-2, 1)));
		assertEpsilonEquals(.70711, this.shape.getDistance(createPoint(1, 0)));
		assertEpsilonEquals(1.00970, this.shape.getDistance(createPoint(3, 0)));
		assertEpsilonEquals(4.12311, this.shape.getDistance(createPoint(1, -4)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(2.23606, this.shape.getDistance(createPoint(-2, 1)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(1, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(3, 0)));
		assertEpsilonEquals(2.6737, this.shape.getDistance(createPoint(1, -4)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquared(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(5, this.shape.getDistanceSquared(createPoint(-2, 1)));
		assertEpsilonEquals(.5, this.shape.getDistanceSquared(createPoint(1, 0)));
		assertEpsilonEquals(1.0195, this.shape.getDistanceSquared(createPoint(3, 0)));
		assertEpsilonEquals(17, this.shape.getDistanceSquared(createPoint(1, -4)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(5, this.shape.getDistanceSquared(createPoint(-2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(3, 0)));
		assertEpsilonEquals(7.14865, this.shape.getDistanceSquared(createPoint(1, -4)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceL1(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(3, this.shape.getDistanceL1(createPoint(-2, 1)));
		assertEpsilonEquals(1, this.shape.getDistanceL1(createPoint(1, 0)));
		assertEpsilonEquals(1.3472, this.shape.getDistanceL1(createPoint(3, 0)));
		assertEpsilonEquals(5, this.shape.getDistanceL1(createPoint(1, -4)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(3, this.shape.getDistanceL1(createPoint(-2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(3, 0)));

		assertEpsilonEquals(3.72973, this.shape.getDistanceL1(createPoint(1, -4)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceLinf(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2, this.shape.getDistanceLinf(createPoint(-2, 1)));
		assertEpsilonEquals(.5, this.shape.getDistanceLinf(createPoint(1, 0)));
		assertEpsilonEquals(.91027, this.shape.getDistanceLinf(createPoint(3, 0)));
		assertEpsilonEquals(4, this.shape.getDistanceLinf(createPoint(1, -4)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(2, this.shape.getDistanceLinf(createPoint(-2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(3, 0)));
		assertEpsilonEquals(2.17568, this.shape.getDistanceLinf(createPoint(1, -4)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setIT(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createPath());
		PathIterator2afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
		Path2afp path = createPath();
		path.moveTo(123.456, 456.789);
		path.lineTo(789.123, 159.753);
		this.shape.set(path);
		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789);
		assertElement(pi, PathElementType.LINE_TO, 789.123, 159.753);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIterator(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertNoElement(pi);
		this.shape.closePath();
		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2afp pi;
		Transform2D transform = new Transform2D();
		
		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertNoElement(pi);
		
		transform.setIdentity();
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertNoElement(pi);

		transform.setTranslation(14, -5);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 14, -5);
		assertElement(pi, PathElementType.LINE_TO, 15, -4);
		assertElement(pi, PathElementType.QUAD_TO, 17, -5, 18, -2);
		assertElement(pi, PathElementType.CURVE_TO, 19, -6, 20, 0, 21, -10);
		assertNoElement(pi);

		this.shape.closePath();
		
		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);

		transform.setIdentity();
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);

		transform.setTranslation(14, -5);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 14, -5);
		assertElement(pi, PathElementType.LINE_TO, 15, -4);
		assertElement(pi, PathElementType.QUAD_TO, 17, -5, 18, -2);
		assertElement(pi, PathElementType.CURVE_TO, 19, -6, 20, 0, 21, -10);
		assertElement(pi, PathElementType.CLOSE, 14, -5);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createTransformedShape(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p1 = randomPoint2d();
		Point2D p2 = randomPoint2d();
		Point2D p3 = randomPoint2d();
		Point2D p4 = randomPoint2d();
		Point2D p5 = randomPoint2d();
		Point2D p6 = randomPoint2d();
		Point2D p7 = randomPoint2d();
		
		Path2afp path = createPath();
		path.moveTo(p1.getX(),p1.getY());
		path.lineTo(p2.getX(),p2.getY());
		path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		path.closePath();

		Transform2D trans = new Transform2D(randomMatrix3f());
		Path2afp transformedShape = (Path2afp) path.createTransformedShape(trans);
		path.transform(trans);		
	
		assertTrue(path.equalsToShape(transformedShape));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void translateDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		
		Path2afp p2 = createPath();
		p2.moveTo(dx, dy);
		p2.lineTo(1 + dx, 1 + dy);
		p2.quadTo(3 + dx, 0 + dy, 4 + dx, 3 + dy);
		p2.curveTo(5 + dx, -1 + dy, 6 + dx, 5 + dy, 7 + dx, -5 + dy);
		
		this.shape.translate(dx, dy);
		
		assertTrue(this.shape.equals(p2));		
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void translateVector2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		
		this.shape.translate(dx, dy);
		
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBox(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBoxB(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangle(-5, 1, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(3, 6, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(3, -10, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(11, 1, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(3, 1, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(4, 3, 2, 1)));
		this.shape.closePath();
		assertFalse(this.shape.contains(createRectangle(-5, 1, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(3, 6, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(3, -10, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(11, 1, 2, 1)));
		assertTrue(this.shape.contains(createRectangle(3, 0, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(4, 3, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void containsShape2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp path1 = createPath();
		path1.moveTo(204.0, 193.5);
		path1.lineTo(204.0, 85.5);
		path1.lineTo(268.0, 85.5);
		path1.lineTo(268.0, 149.5);
		path1.lineTo(388.0, 149.5);
		path1.lineTo(388.0, 193.5);
		path1.closePath();
		
		Path2afp path2 = createPath();
		path2.moveTo(288.0, 145.5);
		path2.lineTo(388.0, 145.5);
		path2.lineTo(388.0, 93.5);
		path2.lineTo(288.0, 93.5);
		path2.closePath();
		
		Path2afp path3 = createPath();
		path3.moveTo(292.0, 185.5);
		path3.lineTo(340.0, 185.5);
		path3.lineTo(340.0, 153.5);
		path3.lineTo(292.0, 153.5);

		Segment2afp segment1 = createSegment(372.0, 169.5, 372.0, 255.2);
		
		Path2afp path4 = createPath();
		path4.moveTo(14.0, 285.5);
		path4.lineTo(74.0, 285.5);
		path4.lineTo(74.0, 139.5);
		path4.lineTo(180.0, 139.5);
		path4.lineTo(180.0, 285.5);
		path4.lineTo(224.0, 285.5);
		path4.lineTo(224.0, 139.5);
		path4.lineTo(390.0, 139.5);
		path4.lineTo(390.0, 49.5);
		path4.lineTo(14.0, 49.5);
		path4.closePath();
		
		Path2afp path5 = createPath();
		path5.moveTo(228.0, 239.5);
		path5.lineTo(224.0, 239.5);
		path5.lineTo(224.0, 257.5);
		path5.lineTo(228.0, 257.5);
		path5.closePath();
		
		Circle2afp circle1 = createCircle(324.0, 93.5, 5.66);

		Path2afp path6 = createPath();
		path6.moveTo(286.0, 131.5);
		path6.lineTo(286.0, 111.5);
		path6.arcTo(282.0, 111.5, 2.0, 2.0, 0.0, true, false);
		path6.lineTo(282.0, 131.5);
		path6.arcTo(286.0, 131.5, 2.0, 2.0, 0.0, true, false);
		path6.closePath();
		
		Circle2afp circle2 = createCircle(284.0, 133.5, 0.725);
		
		Path2afp path7 = createPath();
		path7.moveTo(227.5, 239.5);
		path7.lineTo(223.5, 239.5);
		path7.lineTo(223.5, 257.5);
		path7.lineTo(227.5, 257.5);
		path7.closePath();

		assertFalse(path1.contains(path2));
        assertFalse(path2.contains(path1));

		assertTrue(path1.contains(path3));
        assertFalse(path3.contains(path1));
		
		assertTrue(path1.intersects(segment1));
		assertFalse(path1.contains(segment1));
        assertFalse(segment1.contains(path1));
		
		assertFalse(path4.intersects(path5));
		assertFalse(path4.contains(path5));
        assertFalse(path5.contains(path4));
		
		assertTrue(path4.intersects(path7));
		assertFalse(path4.contains(path7));
        assertFalse(path7.contains(path4));

		assertTrue(path4.intersects(circle1));
		assertTrue(path4.contains(circle1));
        assertFalse(circle1.contains(path4));

		assertTrue(path6.intersects(circle2));
		assertFalse(path6.contains(circle2));
        assertFalse(circle2.contains(path6));

		//
		
		Path2afp path00 = createPath();
		path00.moveTo(168.0, 145.0);
		path00.lineTo(200.0, 200.0);
		path00.lineTo(200.0, 129.0);
		path00.lineTo(168.0, 129.0);
		path00.closePath();
		Segment2afp segment00 = createSegment(420.0, 297.0, 420.0, 0.0);
        assertFalse(path00.contains(segment00));
		assertFalse(segment00.contains(path00));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createRectangle(1, -2, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(1.5, 1.5, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(7, 3, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(-4, -0.5, 2, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRectangle2afp_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertTrue(this.shape.intersects(createRectangle(1, -2, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(1.5, 1.5, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(7, 3, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(-4, -0.5, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsCircle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createCircle(-2, -2, 2)));
		assertFalse(this.shape.intersects(createCircle(2, -2, 2)));
		assertFalse(this.shape.intersects(createCircle(2.5, -1.5, 2)));
		assertFalse(this.shape.intersects(createCircle(10, 0, 2)));
		assertFalse(this.shape.intersects(createCircle(4, 0, 0.5)));
		assertTrue(this.shape.intersects(createCircle(2.5, 1, 0.5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsCircle2afp_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createCircle(-2, -2, 2)));
		assertTrue(this.shape.intersects(createCircle(2, -2, 2)));
		assertTrue(this.shape.intersects(createCircle(2.5, -1.5, 2)));
		assertFalse(this.shape.intersects(createCircle(10, 0, 2)));
		assertTrue(this.shape.intersects(createCircle(4, 0, 0.5)));
		assertTrue(this.shape.intersects(createCircle(2.5, 1, 0.5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsTriangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createTriangle(1, -1, 4, 0, 2, .5)));
		assertFalse(this.shape.intersects(createTriangle(9, 1, 12, 2, 10, 1.5)));
		assertTrue(this.shape.intersects(createTriangle(5, 0, 8, 1, 6, .5)));
		assertFalse(this.shape.intersects(createTriangle(-1, -4, 2, -3, 0, -2.5)));
		assertFalse(this.shape.intersects(createTriangle(3, -6, 6, -5, 4, -4.5)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsTriangle2afp_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertTrue(this.shape.intersects(createTriangle(1, -1, 4, 0, 2, .5)));
		assertFalse(this.shape.intersects(createTriangle(9, 1, 12, 2, 10, 1.5)));
		assertTrue(this.shape.intersects(createTriangle(5, 0, 8, 1, 6, .5)));
		assertFalse(this.shape.intersects(createTriangle(-1, -4, 2, -3, 0, -2.5)));
		assertFalse(this.shape.intersects(createTriangle(3, -6, 6, -5, 4, -4.5)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsEllipse2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createEllipse(1, -1.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(1, 1, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(4.5, -1, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(0, -5.5, 2, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsEllipse2afp_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertTrue(this.shape.intersects(createEllipse(1, -1.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(1, 1, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(4.5, -1, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(0, -5.5, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsSegment2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSegment(1, -1, 2, -3)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 2, -3)));
		assertFalse(this.shape.intersects(createSegment(4, 0, 2, -3)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 5, 3)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsSegment2afp_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createSegment(1, -1, 2, -3)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 2, -3)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 2, -3)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 5, 3)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPath2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, -3)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 5, -3)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, 1)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 4, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPath2afp_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, -3)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 5, -3)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 4, 1)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 4, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPathIterator2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, -3).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 5, -3).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, 1).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 4, 1).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPathIterator2afp_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, -3).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 5, -3).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 4, 1).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 4, 1).getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsOrientedRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createOrientedRectangle(0, -5, 0.5547, 0.83205, 2, 1)));
		assertFalse(this.shape.intersects(createOrientedRectangle(4, -1, 0.5547, 0.83205, 2, 1)));
		assertFalse(this.shape.intersects(createOrientedRectangle(6, 5, 0.5547, 0.83205, 2, 1)));
		assertTrue(this.shape.intersects(createOrientedRectangle(7, 2, 0.5547, 0.83205, 2, 1)));
		assertTrue(this.shape.intersects(createOrientedRectangle(-1, -1, 0.5547, 0.83205, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsParallelogram2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createParallelogram(0, -5, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
		assertFalse(this.shape.intersects(createParallelogram(4, -1, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
		assertFalse(this.shape.intersects(createParallelogram(6, 5, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
		assertTrue(this.shape.intersects(createParallelogram(7, 2, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
		assertTrue(this.shape.intersects(createParallelogram(-1, -1, 0.5547, 0.83205, 2, -0.83205, 0.5547, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRoundRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createRoundRectangle(1, -2, 2, 1, .2, .1)));
		assertTrue(this.shape.intersects(createRoundRectangle(1.5, 1.5, 2, 1, .2, .1)));
		assertFalse(this.shape.intersects(createRoundRectangle(7, 3, 2, 1, .2, .1)));
		assertFalse(this.shape.intersects(createRoundRectangle(-4, -0.5, 2, 1, .2, .1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsRoundRectangle2afp_close(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertTrue(this.shape.intersects(createRoundRectangle(1, -2, 2, 1, .2, .1)));
		assertTrue(this.shape.intersects(createRoundRectangle(1.5, 1.5, 2, 1, .2, .1)));
		assertFalse(this.shape.intersects(createRoundRectangle(7, 3, 2, 1, .2, .1)));
		assertFalse(this.shape.intersects(createRoundRectangle(-4, -0.5, 2, 1, .2, .1)));
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsShape2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape2D) createSegment(4, 0, 5, 3)));
		assertTrue(this.shape.intersects((Shape2D)createRectangle(1.5, 1.5, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addVector2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		
		this.shape.operator_add(createVector(dx, dy));
		
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusVector2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		
		T shape = this.shape.operator_plus(createVector(dx, dy));
		
		PathIterator2afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeVector2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		
		this.shape.operator_remove(createVector(dx, dy));
		
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -dx, -dy);
		assertElement(pi, PathElementType.LINE_TO, -dx + 1, -dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, -dx + 3, -dy, -dx + 4, -dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, -dx + 5, -dy - 1, -dx + 6, -dy + 5, -dx + 7, -dy - 5);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusVector2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		
		T shape = this.shape.operator_minus(createVector(dx, dy));
		
		PathIterator2afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -dx, -dy);
		assertElement(pi, PathElementType.LINE_TO, -dx + 1, -dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, -dx + 3, -dy, -dx + 4, -dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, -dx + 5, -dy - 1, -dx + 6, -dy + 5, -dx + 7, -dy - 5);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyTransform2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p1 = randomPoint2d();
		Point2D p2 = randomPoint2d();
		Point2D p3 = randomPoint2d();
		Point2D p4 = randomPoint2d();
		Point2D p5 = randomPoint2d();
		Point2D p6 = randomPoint2d();
		Point2D p7 = randomPoint2d();
		
		Path2afp path = createPath();
		path.moveTo(p1.getX(),p1.getY());
		path.lineTo(p2.getX(),p2.getY());
		path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		path.closePath();

		Transform2D trans = new Transform2D(randomMatrix3d());
		Path2afp transformedShape = (Path2afp) path.operator_multiply(trans);
		path.transform(trans);		
	
		assertTrue(path.equalsToShape(transformedShape));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.operator_and(createPoint(-5, 1)));
		assertFalse(this.shape.operator_and(createPoint(3, 6)));
		assertFalse(this.shape.operator_and(createPoint(3, -10)));
		assertFalse(this.shape.operator_and(createPoint(11, 1)));
		assertFalse(this.shape.operator_and(createPoint(4, 1)));
		assertTrue(this.shape.operator_and(createPoint(4, 3)));
		this.shape.closePath();
		assertFalse(this.shape.operator_and(createPoint(-5, 1)));
		assertFalse(this.shape.operator_and(createPoint(3, 6)));
		assertFalse(this.shape.operator_and(createPoint(3, -10)));
		assertFalse(this.shape.operator_and(createPoint(11, 1)));
		assertTrue(this.shape.operator_and(createPoint(4, 1)));
		assertTrue(this.shape.operator_and(createPoint(4, 3)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSegment(4, 0, 5, 3)));
		assertTrue(this.shape.operator_and(createRectangle(1.5, 1.5, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2.23607, this.shape.operator_upTo(createPoint(-2, 1)));
		assertEpsilonEquals(.70711, this.shape.operator_upTo(createPoint(1, 0)));
		assertEpsilonEquals(1.00970, this.shape.operator_upTo(createPoint(3, 0)));
		assertEpsilonEquals(4.12311, this.shape.operator_upTo(createPoint(1, -4)));
		
		this.shape.closePath();
		
		assertEpsilonEquals(2.23606, this.shape.operator_upTo(createPoint(-2, 1)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(1, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(3, 0)));
		assertEpsilonEquals(2.6737, this.shape.operator_upTo(createPoint(1, -4)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isCurved(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());
		
		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isCurved());
		this.shape.closePath();
		assertFalse(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(3, 4);
		assertFalse(this.shape.isCurved());
		this.shape.closePath();
		assertFalse(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(3, 4);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isCurved());
		this.shape.curveTo(7, 8, 9, 10, 11, 12);
		assertTrue(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(3, 4);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isCurved());
		this.shape.quadTo(7, 8, 9, 10);
		assertTrue(this.shape.isCurved());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isMultiParts(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isMultiParts());

		this.shape.clear();
		assertFalse(this.shape.isMultiParts());
		
		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isMultiParts());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isMultiParts());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isMultiParts());
		this.shape.closePath();
		assertFalse(this.shape.isMultiParts());

		this.shape.clear();
		assertFalse(this.shape.isMultiParts());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isMultiParts());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isMultiParts());
		this.shape.lineTo(3, 4);
		assertFalse(this.shape.isMultiParts());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isMultiParts());
		this.shape.curveTo(7, 8, 9, 10, 11, 12);
		assertFalse(this.shape.isMultiParts());

		this.shape.moveTo(1, 2);
		assertTrue(this.shape.isMultiParts());
		this.shape.moveTo(3, 4);
		assertTrue(this.shape.isMultiParts());
		this.shape.lineTo(3, 4);
		assertTrue(this.shape.isMultiParts());
		this.shape.lineTo(5, 6);
		assertTrue(this.shape.isMultiParts());
		this.shape.quadTo(7, 8, 9, 10);
		assertTrue(this.shape.isMultiParts());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isPolygon(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());
		
		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(3, 4);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(3, 4);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isPolygon());
		this.shape.curveTo(7, 8, 9, 10, 11, 12);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(3, 4);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isPolygon());
		this.shape.quadTo(7, 8, 9, 10);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());
		
		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertFalse(this.shape.isPolygon());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void isPolyline(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolyline());
		
		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(5, 6);
		assertTrue(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolyline());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4);
		assertTrue(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4);
		assertTrue(this.shape.isPolyline());
		this.shape.lineTo(5, 6);
		assertTrue(this.shape.isPolyline());
		this.shape.curveTo(7, 8, 9, 10, 11, 12);
		assertFalse(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolyline());

		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4);
		assertTrue(this.shape.isPolyline());
		this.shape.lineTo(5, 6);
		assertTrue(this.shape.isPolyline());
		this.shape.quadTo(7, 8, 9, 10);
		assertFalse(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());
		
		this.shape.clear();
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(1, 2);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4);
		assertTrue(this.shape.isPolyline());
		this.shape.moveTo(5, 6);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(7, 8);
		assertFalse(this.shape.isPolyline());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointPathIteratorPathIterator_open_onShape1(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		Path2afp path;
		
		result = createPoint(Double.NaN, Double.NaN);
		path = createPath();
		path.moveTo(0, 2);
		path.lineTo(2, 1);
		path.lineTo(0, 4);
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				(PathIterator2afp) path.getPathIterator(), result));
		assertFpPointEquals(2.0349, 0.76443, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath();
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -4);
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				(PathIterator2afp) path.getPathIterator(), result));
		assertFpPointEquals(7, -5, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath();
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -1);
		path.lineTo(-1, 2);
		path.lineTo(5, 4);
		path.lineTo(7, 1);
		path.lineTo(7.5, -5.5);
		path.lineTo(0, -4);
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				(PathIterator2afp) path.getPathIterator(), result));
		assertFpPointEquals(0, 0, result);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointPathIteratorPathIterator_close_evenOdd_onShape1(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		Path2afp path;
		
		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2);
		path.lineTo(2, 1);
		path.lineTo(0, 4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				(PathIterator2afp) path.getPathIterator(), result));
		assertFpPointEquals(2.0349, 0.76443, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				(PathIterator2afp) path.getPathIterator(), result));
		assertFpPointEquals(7, -5, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -1);
		path.lineTo(-1, 2);
		path.lineTo(5, 4);
		path.lineTo(7, 1);
		path.lineTo(7.5, -5.5);
		path.lineTo(0, -4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				(PathIterator2afp) path.getPathIterator(), result));
		assertFpPointEquals(0, 0, result);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointPathIteratorPathIterator_close_nonZero_onShape1(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		Path2afp path;
		
		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2);
		path.lineTo(2, 1);
		path.lineTo(0, 4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				(PathIterator2afp) path.getPathIterator(), result));
		assertFpPointEquals(2.0349, 0.76443, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				(PathIterator2afp) path.getPathIterator(), result));
		assertFpPointEquals(7, -5, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -1);
		path.lineTo(-1, 2);
		path.lineTo(5, 4);
		path.lineTo(7, 1);
		path.lineTo(7.5, -5.5);
		path.lineTo(0, -4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				(PathIterator2afp) path.getPathIterator(), result));
		assertFpPointEquals(0, 0, result);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointPathIteratorPathIterator_open_onShape2(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		Path2afp path;
		
		result = createPoint(Double.NaN, Double.NaN);
		path = createPath();
		path.moveTo(0, 2);
		path.lineTo(2, 1);
		path.lineTo(0, 4);
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				this.shape.getPathIterator(),
				result));
		assertFpPointEquals(2, 1, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath();
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -4);
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				this.shape.getPathIterator(),
				result));
		assertFpPointEquals(6.82353, -5.70588, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath();
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -1);
		path.lineTo(-1, 2);
		path.lineTo(5, 4);
		path.lineTo(7, 1);
		path.lineTo(7.5, -5.5);
		path.lineTo(0, -4);
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				this.shape.getPathIterator(),
				result));
		assertFpPointEquals(-.3, -.1, result);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointPathIteratorPathIterator_close_evenOdd_onShape2(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		Path2afp path;
		
		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2);
		path.lineTo(2, 1);
		path.lineTo(0, 4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				this.shape.getPathIterator(),
				result));
		assertFpPointEquals(2, 1, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				this.shape.getPathIterator(),
				result));
		assertFpPointEquals(7, -5, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -1);
		path.lineTo(-1, 2);
		path.lineTo(5, 4);
		path.lineTo(7, 1);
		path.lineTo(7.5, -5.5);
		path.lineTo(0, -4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				this.shape.getPathIterator(),
				result));
		assertFpPointEquals(-.3, -.1, result);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointPathIteratorPathIterator_close_nonZero_onShape2(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D result;
		Path2afp path;
		
		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2);
		path.lineTo(2, 1);
		path.lineTo(0, 4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				this.shape.getPathIterator(),
				result));
		assertFpPointEquals(2, 1, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				this.shape.getPathIterator(),
				result));
		assertFpPointEquals(7, -5, result);

		result = createPoint(Double.NaN, Double.NaN);
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -1);
		path.lineTo(-1, 2);
		path.lineTo(5, 4);
		path.lineTo(7, 1);
		path.lineTo(7.5, -5.5);
		path.lineTo(0, -4);
		path.closePath();
		assertTrue(Path2afp.findsClosestPointPathIteratorPathIterator(
				path.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				this.shape.getPathIterator(),
				result));
		assertFpPointEquals(0, 0, result);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToCircle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createCircle(-5, 2, 1)));
		assertFpPointEquals(5.6875, 1.11475, this.shape.getClosestPointTo(createCircle(10, 5, 1)));
		assertFpPointEquals(7, -5, this.shape.getClosestPointTo(createCircle(2, -10, 1)));
		assertClosestPointInBothShapes(this.shape, createCircle(2, 0, 1));
		assertFpPointEquals(6.01612, 0.59312, this.shape.getClosestPointTo(createCircle(7, 1, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredCircle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(19.22967, this.shape.getDistanceSquared(createCircle(-5, 2, 1)));
		assertEpsilonEquals(23.08372, this.shape.getDistanceSquared(createCircle(10, 5, 1)));
		assertEpsilonEquals(36.85786, this.shape.getDistanceSquared(createCircle(2, -10, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(2, 0, 1)));
		assertEpsilonEquals(0.0041857, this.shape.getDistanceSquared(createCircle(7, 1, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToSegment2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createSegment(-6, -2, -5, -.5)));
		assertFpPointEquals(2.0349, 0.76443, this.shape.getClosestPointTo(createSegment(0, 2, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createSegment(2, 0, 7, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredSegment2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(25.25, this.shape.getDistanceSquared(createSegment(-6, -2, -5, -.5)));
		assertEpsilonEquals(0.05671, this.shape.getDistanceSquared(createSegment(0, 2, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(2, 0, 7, 1)));
	}
	
	protected Triangle2afp createTestTriangle(double dx, double dy) {
		return createTriangle(dx, dy, dx + 3, dy + 1, dx + 1, dy + 1.5);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToTriangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestTriangle(-6, -2)));
		assertFpPointEquals(6.01612, 0.59312, this.shape.getClosestPointTo(createTestTriangle(7, 1)));
		assertClosestPointInBothShapes(this.shape, createTestTriangle(4, 0));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredTriangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(10, this.shape.getDistanceSquared(createTestTriangle(-6, -2)));
		assertEpsilonEquals(1.13358, this.shape.getDistanceSquared(createTestTriangle(7, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(4, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToEllipse2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createEllipse(-4, 6, 2, 1)));
		assertFpPointEquals(5.92188, 0.78878, this.shape.getClosestPointTo(createEllipse(7, 1, 2, 1)));
		assertFpPointEquals(2.88045, 1.11344, this.shape.getClosestPointTo(createEllipse(3, 0, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createEllipse(2, 0, 2, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredEllipse2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(37.2745, this.shape.getDistanceSquared(createEllipse(-4, 6, 2, 1)));
		assertEpsilonEquals(1.57546, this.shape.getDistanceSquared(createEllipse(7, 1, 2, 1)));
		assertEpsilonEquals(0.19513, this.shape.getDistanceSquared(createEllipse(3, 0, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(2, 0, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createRectangle(-4, 6, 2, 1)));
		assertFpPointEquals(6.01612, 0.59312, this.shape.getClosestPointTo(createRectangle(7, 1, 2, 1)));
		assertFpPointEquals(2.89236, 1.12379, this.shape.getClosestPointTo(createRectangle(3, 0, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createRectangle(2, 0, 2, 1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(34, this.shape.getDistanceSquared(createRectangle(-4, 6, 2, 1)));
		assertEpsilonEquals(1.13358, this.shape.getDistanceSquared(createRectangle(7, 1, 2, 1)));
		assertEpsilonEquals(0.026911, this.shape.getDistanceSquared(createRectangle(3, 0, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(2, 0, 2, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToRoundRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(1, 1, this.shape.getClosestPointTo(createRoundRectangle(-4, 6, 2, 1, .2, .1)));
		assertFpPointEquals(5.99476, 0.63988, this.shape.getClosestPointTo(createRoundRectangle(7, 1, 2, 1, .2, .1)));
		assertFpPointEquals(2.91121, 1.14018, this.shape.getClosestPointTo(createRoundRectangle(3, 0, 2, 1, .2, .1)));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(2, 0, 2, 1, .2, .1));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredRoundRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(34.64138, this.shape.getDistanceSquared(createRoundRectangle(-4, 6, 2, 1, .2, .1)));
		assertEpsilonEquals(1.21231, this.shape.getDistanceSquared(createRoundRectangle(7, 1, 2, 1, .2, .1)));
		assertEpsilonEquals(0.049032, this.shape.getDistanceSquared(createRoundRectangle(3, 0, 2, 1, .2, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(2, 0, 2, 1, .2, .1)));
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
		assertFpPointEquals(4, 3, this.shape.getClosestPointTo(createTestMultiShape(-4, 6)));
		assertFpPointEquals(4, 3, this.shape.getClosestPointTo(createTestMultiShape(7, 1)));
		assertFpPointEquals(5.78125, 1.00885, this.shape.getClosestPointTo(createTestMultiShape(3, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredMultiShape2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createTestMultiShape(-4, 6)));
		assertEpsilonEquals(3.34315, this.shape.getDistanceSquared(createTestMultiShape(7, 1)));
		assertEpsilonEquals(2.46773, this.shape.getDistanceSquared(createTestMultiShape(3, 0)));
	}

	protected OrientedRectangle2afp createTestOrientedRectangle(double x, double y) {
		Vector2D u = createVector(.5, .75).toUnitVector();
		return createOrientedRectangle(x, y, u.getX(), u.getY(), 2, 1);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToOrientedRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestOrientedRectangle(-4, 6)));
		assertFpPointEquals(5.59375, 1.19354, this.shape.getClosestPointTo(createTestOrientedRectangle(8, 3)));
		assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(3, 0));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredOrientedRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(32.59319, this.shape.getDistanceSquared(createTestOrientedRectangle(-4, 6)));
		assertEpsilonEquals(0.70193, this.shape.getDistanceSquared(createTestOrientedRectangle(8, 3)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(3, 0)));
	}

	protected Parallelogram2afp createTestParallelogram(double x, double y) {
		Vector2D u = createVector(.5, .75).toUnitVector();
		Vector2D v = createVector(-2, .5).toUnitVector();
		return createParallelogram(x, y, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToParallelogram2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(createTestParallelogram(-4, 6)));
		assertFpPointEquals(5.59375, 1.19354, this.shape.getClosestPointTo(createTestParallelogram(8, 3)));
		assertClosestPointInBothShapes(this.shape, createTestParallelogram(3, 0));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredParallelogram2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(33.88908, this.shape.getDistanceSquared(createTestParallelogram(-4, 6)));
		assertEpsilonEquals(0.25487, this.shape.getDistanceSquared(createTestParallelogram(8, 3)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(3, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getClosestPointToPath2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp path;
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2);
		path.lineTo(2, 1);
		path.lineTo(0, 4);
		path.closePath();
		assertFpPointEquals(2.0349, 0.76443, this.shape.getClosestPointTo(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -4);
		path.closePath();
		assertClosestPointInBothShapes(this.shape, path);

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -1);
		path.lineTo(-1, 2);
		path.lineTo(5, 4);
		path.lineTo(7, 1);
		path.lineTo(7.5, -5.5);
		path.lineTo(0, -4);
		path.closePath();
		assertFpPointEquals(0, 0, this.shape.getClosestPointTo(path));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getDistanceSquaredPath2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2afp path;
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2);
		path.lineTo(2, 1);
		path.lineTo(0, 4);
		path.closePath();
		assertEpsilonEquals(0.05671, this.shape.getDistanceSquared(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -4);
		path.closePath();
		assertEpsilonEquals(0, this.shape.getDistanceSquared(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(7, 5);
		path.lineTo(8, -6);
		path.lineTo(0, -1);
		path.lineTo(-1, 2);
		path.lineTo(5, 4);
		path.lineTo(7, 1);
		path.lineTo(7.5, -5.5);
		path.lineTo(0, -4);
		path.closePath();
		assertEpsilonEquals(0.1, this.shape.getDistanceSquared(path));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCurrentX(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7, this.shape.getCurrentX());
		this.shape.lineTo(154, 485);
		assertEpsilonEquals(154, this.shape.getCurrentX());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCurrentY(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(-5, this.shape.getCurrentY());
		this.shape.lineTo(154, 485);
		assertEpsilonEquals(485, this.shape.getCurrentY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCurrentPoint(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(7, -5, this.shape.getCurrentPoint());
		this.shape.lineTo(154, 485);
		assertFpPointEquals(154, 485, this.shape.getCurrentPoint());
	}

}