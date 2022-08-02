/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d2.ai;

import static org.arakhne.afc.math.geometry.GeomConstants.SHAPE_INTERSECTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Path2D.ArcType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.BasicPathShadow2ai;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.ai.Shape2ai;

@SuppressWarnings("all")
public abstract class AbstractPath2aiTest<T extends Path2ai<?, T, ?, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractShape2aiTest<T, B> {

	@Override
	protected final T createShape() {
		T path = (T) createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		return path;
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void testClone(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		PathIterator2ai pi = clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
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
		assertFalse(this.shape.equals(createRectangle(5, 8, 10, 6)));
		assertTrue(this.shape.equals(this.shape));
		Path2ai path = createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		assertTrue(this.shape.equals(path));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createPath().getPathIterator()));
		assertFalse(this.shape.equals(createRectangle(5, 8, 10, 6).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		Path2ai path = createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		assertTrue(this.shape.equals(path.getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createPath()));
		assertTrue(this.shape.equalsToShape(this.shape));
		T path = (T) createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		assertTrue(this.shape.equalsToShape(path));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createPath().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createRectangle(5, 8, 10, 6).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		Path2ai path = createPath();
		path.moveTo(0, 0);
		path.lineTo(2, 2);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		path.closePath();
		assertTrue(this.shape.equalsToPathIterator(path.getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void isEmpty(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());

		this.shape.clear();
		assertTrue(this.shape.isEmpty());
		
		this.shape.moveTo(1, 2);
		assertTrue(this.shape.isEmpty());
		this.shape.moveTo(3, 4);
		assertTrue(this.shape.isEmpty());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isEmpty());
		this.shape.closePath();
		assertFalse(this.shape.isEmpty());

		this.shape.clear();
		assertTrue(this.shape.isEmpty());

		this.shape.moveTo(1, 2);
		assertTrue(this.shape.isEmpty());
		this.shape.moveTo(3, 4);
		assertTrue(this.shape.isEmpty());
		this.shape.lineTo(3, 4);
		assertTrue(this.shape.isEmpty());
		this.shape.closePath();
		assertTrue(this.shape.isEmpty());

		this.shape.clear();
		assertTrue(this.shape.isEmpty());

		this.shape.moveTo(1, 2);
		assertTrue(this.shape.isEmpty());
		this.shape.moveTo(3, 4);
		assertTrue(this.shape.isEmpty());
		this.shape.lineTo(3, 4);
		assertTrue(this.shape.isEmpty());
		this.shape.lineTo(5, 6);
		assertFalse(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void clear(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEquals(0, this.shape.size());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getPointIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		Iterator<? extends Point2D> it = this.shape.getPointIterator();
		
		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(1, p.ix(), p.toString());
		assertEquals(1, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(2, p.ix(), p.toString());
		assertEquals(2, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(3, p.ix(), p.toString());
		assertEquals(1, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(2, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(3, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(2, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(5, p.ix(), p.toString());
		assertEquals(2, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(5, p.ix(), p.toString());
		assertEquals(1, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(6, p.ix(), p.toString());
		assertEquals(1, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(6, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(6, p.ix(), p.toString());
		assertEquals(-1, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-1, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-2, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-3, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-4, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(6, p.ix(), p.toString());
		assertEquals(-4, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(5, p.ix(), p.toString());
		assertEquals(-4, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(-3, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(3, p.ix(), p.toString());
		assertEquals(-2, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(2, p.ix(), p.toString());
		assertEquals(-1, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(1, p.ix(), p.toString());
		assertEquals(-1, p.iy(), p.toString());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		assertFalse(it.hasNext());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPointShadow(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), -2, 1, null));
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 0, -3, null));
		assertEquals(SHAPE_INTERSECTS,
				Path2ai.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 4, 3, null));
		assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorPointShadow(0, this.shape.getPathIterator(), 3, 0, null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorRectangleShadow(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
				0,
				this.shape.getPathIterator(),
				-2, 1, -1, 2,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
				0,
				this.shape.getPathIterator(),
				0, 1, 3, 6,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
				0,
				this.shape.getPathIterator(),
				3, -1, 8, 0,
				null));
		assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
				0,
				this.shape.getPathIterator(),
				3, -1, 4, 0,
				null));
		assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
				0,
				this.shape.getPathIterator(),
				3, -1, 5, 0,
				null));
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
				0,
				this.shape.getPathIterator(),
				0, -4, 3, -3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
				0,
				this.shape.getPathIterator(),
				0, -4, 4, -3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorRectangleShadow(
				0,
				this.shape.getPathIterator(),
				0, -4, 3, -2,
				null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorSegmentShadow(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
				0,
				this.shape.getPathIterator(),
				-2, 1, -1, 2,
				null));
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
				0,
				this.shape.getPathIterator(),
				0, 1, 3, 6,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
				0,
				this.shape.getPathIterator(),
				3, -1, 8, 0,
				null));
		assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
				0,
				this.shape.getPathIterator(),
				3, -1, 4, 0,
				null));
		assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
				0,
				this.shape.getPathIterator(),
				3, -1, 5, 0,
				null));
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
				0,
				this.shape.getPathIterator(),
				0, -4, 3, -3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
				0,
				this.shape.getPathIterator(),
				0, -4, 4, -3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorSegmentShadow(
				0,
				this.shape.getPathIterator(),
				0, -4, 3, -2,
				null));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorCircleShadow(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
				0,
				this.shape.getPathIterator(),
				-2, 1, 1,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
				0,
				this.shape.getPathIterator(),
				-2, 1, 2,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
				0,
				this.shape.getPathIterator(),
				0, 1, 3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
				0,
				this.shape.getPathIterator(),
				3, -1, 8,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
				0,
				this.shape.getPathIterator(),
				3, -1, 1,
				null));
		assertEquals(-2, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
				0,
				this.shape.getPathIterator(),
				4, -1, 0,
				null));
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorCircleShadow(
				0,
				this.shape.getPathIterator(),
				20, 0, 2,
				null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPathShadow_notCloseable_noOnlyIntersectWhenOpen(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai path1;
		Path2ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertEquals(1, Path2ai.calculatesCrossingsPathIteratorPathShadow(
		        0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.STANDARD));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.STANDARD));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.STANDARD));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.STANDARD));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPathShadow_closeable_noOnlyIntersectWhenOpen(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai path1;
		Path2ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.AUTO_CLOSE));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.AUTO_CLOSE));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.AUTO_CLOSE));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.AUTO_CLOSE));
	}
		
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesCrossingsPathIteratorPathShadow_noCloseable_onlyIntersectWhenOpen(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai path1;
		Path2ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertEquals(0, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path2ai.calculatesCrossingsPathIteratorPathShadow(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new BasicPathShadow2ai(path1),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsPointPathIterator2iIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 0, 0));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 4, 3));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 2, 2));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 2, 1));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 4, 2));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 4, 3));
		assertFalse(Path2ai.containsPoint(this.shape.getPathIterator(), -1, -1));
		assertFalse(Path2ai.containsPoint(this.shape.getPathIterator(), 6, 2));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 3, -2));
		assertFalse(Path2ai.containsPoint(this.shape.getPathIterator(), 2, -2));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticIntersectsRectanglePathIterator2iIntIntIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 0, 0, 1, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 4, 3, 1, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 2, 2, 1, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 2, 1, 1, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 3, 0, 1, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), -1, -1, 1, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 4, -3, 1, 1));
		assertFalse(Path2ai.intersectsRectangle(this.shape.getPathIterator(), -3, 4, 1, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 6, -5, 1, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 4, 0, 1, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 5, 0, 1, 1));
		assertFalse(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 0, -3, 1, 1));
		assertFalse(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 0, -3, 2, 1));
		assertTrue(Path2ai.intersectsRectangle(this.shape.getPathIterator(), 0, -3, 3, 1));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getClosestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = this.shape.getClosestPointTo(createPoint(0, 0));
		assertEquals(0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		// remember: path is closed
		p = this.shape.getClosestPointTo(createPoint(-1, -4));
		assertEquals(0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = this.shape.getClosestPointTo(createPoint(4, 0));
		assertEquals(4, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = this.shape.getClosestPointTo(createPoint(4, 2));
		assertEquals(4, p.ix(), p.toString());
		assertEquals(2, p.iy(), p.toString());

		p = this.shape.getClosestPointTo(createPoint(4, -1));
		assertEquals(4, p.ix(), p.toString());
		assertEquals(-1, p.iy(), p.toString());

		p = this.shape.getClosestPointTo(createPoint(2, -3));
		assertEquals(3, p.ix(), p.toString());
		assertEquals(-2, p.iy(), p.toString());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCurrentX(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(7, this.shape.getCurrentX());
		this.shape.lineTo(148, 752);
		assertEquals(148, this.shape.getCurrentX());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCurrentY(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(-5, this.shape.getCurrentY());
		this.shape.lineTo(148, 752);
		assertEquals(752, this.shape.getCurrentY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCurrentPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertIntPointEquals(7, -5, this.shape.getCurrentPoint());
		this.shape.lineTo(148, 752);
		assertIntPointEquals(148, 752, this.shape.getCurrentPoint());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getFarthestPointTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = this.shape.getFarthestPointTo(createPoint(-1, -4)); // remember: path is closed
		assertEquals(4, p.ix(), p.toString());
		assertEquals(3, p.iy(), p.toString());

		p = this.shape.getFarthestPointTo(createPoint(4, 0));
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = this.shape.getFarthestPointTo(createPoint(4, 2));
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = this.shape.getFarthestPointTo(createPoint(4, -1));
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getDistance(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(1, 0)));
		assertEpsilonEquals(7.071067812f, this.shape.getDistance(createPoint(-5, -5)));
		assertEpsilonEquals(3f, this.shape.getDistance(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(7, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getDistanceSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(1, 0)));
		assertEpsilonEquals(50f, this.shape.getDistanceSquared(createPoint(-5, -5)));
		assertEpsilonEquals(9f, this.shape.getDistanceSquared(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(7, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getDistanceL1(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(1, 0)));
		assertEpsilonEquals(10f, this.shape.getDistanceL1(createPoint(-5, -5)));
		assertEpsilonEquals(3f, this.shape.getDistanceL1(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(7, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getDistanceLinf(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(1, 0)));
		assertEpsilonEquals(5f, this.shape.getDistanceLinf(createPoint(-5, -5)));
		assertEpsilonEquals(3f, this.shape.getDistanceLinf(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(7, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void translateIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(3, 4);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void translateVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(3, 4));
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setWindingRule(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(PathWindingRule.NON_ZERO, this.shape.getWindingRule());
		for(PathWindingRule rule : PathWindingRule.values()) {
			this.shape.setWindingRule(rule);
			assertEquals(rule, this.shape.getWindingRule());
		}
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void addIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai p2 = createPath();
		p2.moveTo(3, 4);
		p2.lineTo(5, 6);
		
		this.shape.add(p2.getPathIterator());
		
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr;
		PathIterator2ai pi;
		
		tr = new Transform2D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(3, 4);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getPathIteratorDouble(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2ai pi = this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.LINE_TO, 3, 1);
		assertElement(pi, PathElementType.LINE_TO, 4, 2);
		assertElement(pi, PathElementType.LINE_TO, 4, 3);
		assertElement(pi, PathElementType.LINE_TO, 4, 2);
		assertElement(pi, PathElementType.LINE_TO, 5, 2);
		assertElement(pi, PathElementType.LINE_TO, 5, 1);
		assertElement(pi, PathElementType.LINE_TO, 6, 1);
		assertElement(pi, PathElementType.LINE_TO, 6, 0);
		assertElement(pi, PathElementType.LINE_TO, 6, -1);
		assertElement(pi, PathElementType.LINE_TO, 7, -1);
		assertElement(pi, PathElementType.LINE_TO, 7, -2);
		assertElement(pi, PathElementType.LINE_TO, 7, -3);
		assertElement(pi, PathElementType.LINE_TO, 7, -4);
		assertElement(pi, PathElementType.LINE_TO, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void transformTransform2D_translation(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr = new Transform2D();
		tr.makeTranslationMatrix(3, 4);
		
		Path2ai clone = this.shape.clone();
		clone.transform(tr);

		PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createTransformedShape_translation(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr = new Transform2D();
		tr.makeTranslationMatrix(3, 4);
		
		Shape2ai clone = this.shape.createTransformedShape(tr);
		
		assertTrue(clone instanceof Path2ai);

		PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void transformTransform2D_rotation(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr2 = new Transform2D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		
		Path2ai clone = this.shape.clone();
		clone.transform(tr2);

		PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, -2);
		assertElement(pi, PathElementType.QUAD_TO, 0, -3, 3, -4);
		assertElement(pi, PathElementType.CURVE_TO, -1, -5, 5, -6, -5, -7);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void createTransformedShape_rotation(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr2 = new Transform2D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		
		Path2ai clone = (Path2ai) this.shape.createTransformedShape(tr2);

		PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, -2);
		assertElement(pi, PathElementType.QUAD_TO, 0, -3, 3, -4);
		assertElement(pi, PathElementType.CURVE_TO, -1, -5, 5, -6, -5, -7);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void transformTransform2D_translationRotation(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr = new Transform2D();
		tr.makeTranslationMatrix(3, 4);
		Transform2D tr2 = new Transform2D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		Transform2D tr3 = new Transform2D();
		tr3.mul(tr, tr2);

		Path2ai clone = this.shape.clone();
		clone.transform(tr3);

		PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 1, 6, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2, -1, 8, -2, -2, -3);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void createTransformedShape(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr = new Transform2D();
		tr.makeTranslationMatrix(3, 4);
		Transform2D tr2 = new Transform2D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		Transform2D tr3 = new Transform2D();
		tr3.mul(tr, tr2);

		Path2ai clone = (Path2ai) this.shape.createTransformedShape(tr3);

		PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 1, 6, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2, -1, 8, -2, -2, -3);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void containsIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(0, 0));
		assertTrue(this.shape.contains(4, 3));
		assertTrue(this.shape.contains(2, 2));
		assertTrue(this.shape.contains(2, 1));
		assertTrue(this.shape.contains(4, 2));
		assertFalse(this.shape.contains(-1, -1));
		assertFalse(this.shape.contains(6, 2));
		assertTrue(this.shape.contains(3, -2));
		assertFalse(this.shape.contains(2, -2));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void containsRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangle(0, 0, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(4, 3, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(2, 2, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(2, 1, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(3, 0, 1, 1)));
		assertTrue(this.shape.contains(createRectangle(3, 0, 1, 0)));
		assertFalse(this.shape.contains(createRectangle(3, 0, 2, 1)));
		assertTrue(this.shape.contains(createRectangle(3, 0, 2, 0)));
		assertFalse(this.shape.contains(createRectangle(-1, -1, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(4, -3, 1, 1)));
		assertTrue(this.shape.contains(createRectangle(5, -3, 0, 1)));
		assertFalse(this.shape.contains(createRectangle(-3, 4, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(6, -5, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(4, 0, 1, 1)));
		assertTrue(this.shape.contains(createRectangle(4, 0, 1, 0)));
		assertFalse(this.shape.contains(createRectangle(5, 0, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(5, 0, 1, 0)));
		assertFalse(this.shape.contains(createRectangle(5, 0, 0, 1)));
		assertTrue(this.shape.contains(createRectangle(5, 0, 0, 0)));
		assertFalse(this.shape.contains(createRectangle(5, 0, 2, 1)));
		assertFalse(this.shape.contains(createRectangle(6, 0, 1, 1)));
	}

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @Override    public void containsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.contains(createCircle(0, 0, 1)));
        assertFalse(this.shape.contains(createCircle(4, 3, 1)));
        assertFalse(this.shape.contains(createCircle(2, 2, 1)));
        assertFalse(this.shape.contains(createCircle(2, 1, 1)));
        assertFalse(this.shape.contains(createCircle(3, 0, 1)));
        assertFalse(this.shape.contains(createCircle(3, 0, 2)));
        assertFalse(this.shape.contains(createCircle(-1, -1, 1)));
        assertFalse(this.shape.contains(createCircle(4, -3, 1)));
        assertFalse(this.shape.contains(createCircle(5, -3, 1)));
        assertFalse(this.shape.contains(createCircle(-3, 4, 1)));
        assertFalse(this.shape.contains(createCircle(6, -5, 1)));
        assertTrue(this.shape.contains(createCircle(4, 0, 1)));
        assertFalse(this.shape.contains(createCircle(5, 0, 1)));
        assertTrue(this.shape.contains(createCircle(5, 0, 0)));
        assertFalse(this.shape.contains(createCircle(5, 0, 2)));
        assertFalse(this.shape.contains(createCircle(6, 0, 1)));
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void intersectsRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createRectangle(0, 0, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(4, 3, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(2, 2, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(2, 1, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(3, 0, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(-1, -1, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(4, -3, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(-3, 4, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(6, -5, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(4, 0, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(5, 0, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(0, -3, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(0, -3, 2, 1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void intersectsCircle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createCircle(0, 0, 1)));
		assertTrue(this.shape.intersects(createCircle(4, 3, 1)));
		assertTrue(this.shape.intersects(createCircle(2, 2, 1)));
		assertTrue(this.shape.intersects(createCircle(2, 1, 1)));
		assertTrue(this.shape.intersects(createCircle(3, 0, 1)));
		assertFalse(this.shape.intersects(createCircle(-1, -1, 1)));
		assertTrue(this.shape.intersects(createCircle(4, -3, 1)));
		assertFalse(this.shape.intersects(createCircle(-3, 4, 1)));
		assertTrue(this.shape.intersects(createCircle(6, -5, 1)));
		assertTrue(this.shape.intersects(createCircle(4, 0, 1)));
		assertTrue(this.shape.intersects(createCircle(5, 0, 1)));
		assertTrue(this.shape.intersects(createCircle(6, 2, 1)));
		assertFalse(this.shape.intersects(createCircle(-5, 0, 3)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void intersectsSegment2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSegment(0, 0, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(4, 3, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(2, 2, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(2, 1, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(3, 0, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(-1, -1, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(4, -3, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(-3, 4, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(6, -5, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 1, 1)));
		assertTrue(this.shape.intersects(createSegment(5, 0, 1, 1)));
		assertFalse(this.shape.intersects(createSegment(-4, -4, -3, -3)));
		assertFalse(this.shape.intersects(createSegment(-1, 0, 2, 3)));
		assertFalse(this.shape.intersects(createSegment(7, 1, 18, 14)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void intersectsPath2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai path1;
		Path2ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertFalse(path1.intersects(path2));
		assertFalse(path2.intersects(path1));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertTrue(path1.intersects(path2));
		assertTrue(path2.intersects(path1));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertTrue(path1.intersects(path2));
		assertTrue(path2.intersects(path1));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertTrue(path1.intersects(path2));
		assertTrue(path2.intersects(path1));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void toBoundingBox(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B bb = this.shape.toBoundingBox();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(3, bb.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void toBoundingBoxB(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B bb = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(3, bb.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toBoundingBoxWithCtrlPoints(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B bb = this.shape.toBoundingBoxWithCtrlPoints();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(5, bb.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void removeLast(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
		
		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertNoElement(pi);

		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertNoElement(pi);

		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertNoElement(pi);

		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertNoElement(pi);

		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setLastPointIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
		
		this.shape.setLastPoint(123, 789);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 123, 789);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void removeIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
		
		this.shape.remove(2, 2);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);

		this.shape.remove(4, 3);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);

		this.shape.remove(6, 5);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);

		this.shape.remove(6, 5);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void containsPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(createPoint(0, 0)));
		assertTrue(this.shape.contains(createPoint(4, 3)));
		assertTrue(this.shape.contains(createPoint(2, 2)));
		assertTrue(this.shape.contains(createPoint( 2, 1)));
		assertTrue(this.shape.contains(createPoint(4, 2)));
		assertFalse(this.shape.contains(createPoint(-1, -1)));
		assertFalse(this.shape.contains(createPoint(6, 2)));
		assertTrue(this.shape.contains(createPoint(3, -2)));
		assertFalse(this.shape.contains(createPoint(2, -2)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticContainsPointPathIteratorIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 0, 0));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 4, 3));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 2, 2));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 2, 1));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 4, 2));
		assertFalse(Path2ai.containsPoint(this.shape.getPathIterator(), -1, -1));
		assertFalse(Path2ai.containsPoint(this.shape.getPathIterator(), 6, 2));
		assertTrue(Path2ai.containsPoint(this.shape.getPathIterator(), 3, -2));
		assertFalse(Path2ai.containsPoint(this.shape.getPathIterator(), 2, -2));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticCalculatesDrawableElementBoundingBox(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		B box = createRectangle(0, 0, 0, 0);
		assertTrue(Path2ai.calculatesDrawableElementBoundingBox(this.shape.getPathIterator(), box));
		assertEquals(0, box.getMinX());
		assertEquals(-5, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(3, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsClosestPointPathIteratorPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = createPoint(0, 0);
		Path2ai.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 0, 0, p);
		assertEquals(0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = createPoint(0, 0);
		// remember: path is closed
		Path2ai.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), -1, -4, p);
		assertEquals( 0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = createPoint(0, 0);
		Path2ai.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, 0, p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = createPoint(0, 0);
		Path2ai.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, 2, p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(2, p.iy(), p.toString());

		p = createPoint(0, 0);
		Path2ai.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, -1, p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(-1, p.iy(), p.toString());

		p = createPoint(0, 0);
		Path2ai.findsClosestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 2, -3, p);
		assertEquals(3, p.ix(), p.toString());
		assertEquals(-2, p.iy(), p.toString());
	}

    protected Path2ai createTestPath() {
        Path2ai path = createPath();
        path.moveTo(3, -20);
        path.lineTo(10, -5);
        path.lineTo(5, 25);
        path.lineTo(-4, 0);
        return path;
    }

    protected Path2ai createTestPath(PathWindingRule rule) {
        Path2ai path = createPath(rule);
        path.moveTo(3, -20);
        path.lineTo(10, -5);
        path.lineTo(5, 25);
        path.lineTo(-4, 0);
        path.closePath();
        return path;
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticFindsClosestPointPathIteratorPathIterator(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        Point2D<?, ?> result;
        result = createPoint(0, 0);
        Path2ai.findsClosestPointPathIteratorPathIterator(
                (PathIterator2ai) this.shape.getFlatteningPathIterator(),
                (PathIterator2ai) createTestPath().getPathIterator(),
                result);
        assertIntPointEquals(7, -1, result);

        result = createPoint(0, 0);
        Path2ai.findsClosestPointPathIteratorPathIterator(
                (PathIterator2ai) this.shape.getFlatteningPathIterator(),
                (PathIterator2ai) createTestPath(PathWindingRule.EVEN_ODD).getPathIterator(),
                result);
        assertIntPointEquals(7, -1, result);

        result = createPoint(0, 0);
        Path2ai.findsClosestPointPathIteratorPathIterator(
                (PathIterator2ai) this.shape.getFlatteningPathIterator(),
                (PathIterator2ai) createTestPath(PathWindingRule.NON_ZERO).getPathIterator(),
                result);
        assertIntPointEquals(7, -1, result);
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void staticFindsFarthestPointPathIteratorPoint(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Point2D p;
		
		p = createPoint(0, 0);
		Path2ai.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 0, 0, p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = createPoint(0, 0);
		// remember: path is closed
		Path2ai.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), -1, -4, p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(3, p.iy(), p.toString());

		p = createPoint(0, 0);
		Path2ai.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, 0, p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = createPoint(0, 0);
		Path2ai.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, 2, p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = createPoint(0, 0);
		Path2ai.findsFarthestPointPathIteratorPoint(this.shape.getPathIterator(GeomConstants.SPLINE_APPROXIMATION_RATIO), 4, -1, p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void moveToIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(15, 145);

		PathIterator2ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(15, 145);
		tmpShape.moveTo(-15, -954);

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -15, -954);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void moveToPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(createPoint(15, 145));

		PathIterator2ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(createPoint(15, 145));
		tmpShape.moveTo(createPoint(-15, -954));

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -15, -954);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lineToIntInt_noMoveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(15, 145);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lineToIntInt_moveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(15, 145);
		tmpShape.lineTo(189, -45);

		PathIterator2ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145);
		assertElement(pi, PathElementType.LINE_TO, 189, -45);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(15, 145);
		tmpShape.lineTo(189, -45);
		tmpShape.lineTo(-5, 0);

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145);
		assertElement(pi, PathElementType.LINE_TO, 189, -45);
		assertElement(pi, PathElementType.LINE_TO, -5, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lineToPoint2D_noMoveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(createPoint(15, 145));
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lineToPoint2D_moveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(15, 145);
		tmpShape.lineTo(createPoint(189, -45));

		PathIterator2ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145);
		assertElement(pi, PathElementType.LINE_TO, 189, -45);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(15, 145);
		tmpShape.lineTo(createPoint(189, -45));
		tmpShape.lineTo(createPoint(-5, 0));

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145);
		assertElement(pi, PathElementType.LINE_TO, 189, -45);
		assertElement(pi, PathElementType.LINE_TO, -5, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void quadToIntIntIntInt_noMoveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		assertThrows(IllegalStateException.class, () -> {
			tmpShape.quadTo(15, 145, 50, 20);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void quadToIntIntIntInt_moveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(4, 6);
		tmpShape.quadTo(15, 145, 50, 20);

		PathIterator2ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6);
		assertElement(pi, PathElementType.QUAD_TO, 15, 145, 50, 20);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(4, 6);
		tmpShape.quadTo(15, 145, 50, 20);
		tmpShape.quadTo(-42, 0, -47, -60);

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6);
		assertElement(pi, PathElementType.QUAD_TO, 15, 145, 50, 20);
		assertElement(pi, PathElementType.QUAD_TO, -42, 0, -47, -60);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void quadToPoint2DPoint2D_noMoveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void quadToPoint2DPoint2D_moveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(4, 6);
		tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));

		PathIterator2ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6);
		assertElement(pi, PathElementType.QUAD_TO, 15, 145, 50, 20);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(4, 6);
		tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));
		tmpShape.quadTo(createPoint(-42, 0), createPoint(-47, -60));

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6);
		assertElement(pi, PathElementType.QUAD_TO, 15, 145, 50, 20);
		assertElement(pi, PathElementType.QUAD_TO, -42, 0, -47, -60);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void curveToIntIntIntIntIntInt_noMoveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(15, 145, 50, 20, 0, 0);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void curveToIntIntIntIntIntInt_moveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(4, 6);
		tmpShape.curveTo(15, 145, 50, 20, 0, 0);

		PathIterator2ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6);
		assertElement(pi, PathElementType.CURVE_TO, 15, 145, 50, 20, 0, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(4, 6);
		tmpShape.curveTo(15, 145, 50, 20, 0, 0);
		tmpShape.curveTo(-42, 0, -47, -60, 1, 2);

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6);
		assertElement(pi, PathElementType.CURVE_TO, 15, 145, 50, 20, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, -42, 0, -47, -60, 1, 2);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void curveToPoint2DPoint2DPoint2D_noMoveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void curveToPoint2DPoint2DPoint2Dt_moveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(4, 6);
		tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));

		PathIterator2ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6);
		assertElement(pi, PathElementType.CURVE_TO, 15, 145, 50, 20, 0, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(4, 6);
		tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));
		tmpShape.curveTo(createPoint(-42, 0), createPoint(-47, -60), createPoint(1, 2));

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6);
		assertElement(pi, PathElementType.CURVE_TO, 15, 145, 50, 20, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, -42, 0, -47, -60, 1, 2);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void lengthSquared(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(98, this.shape.getLengthSquared());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void length(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(9.899494937, this.shape.getLength());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getCoordAt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEquals(7, this.shape.size());
		assertEquals(0, this.shape.getCoordAt(0));
		assertEquals(0, this.shape.getCoordAt(1));
		assertEquals(2, this.shape.getCoordAt(2));
		assertEquals(2, this.shape.getCoordAt(3));
		assertEquals(3, this.shape.getCoordAt(4));
		assertEquals(0, this.shape.getCoordAt(5));
		assertEquals(4, this.shape.getCoordAt(6));
		assertEquals(3, this.shape.getCoordAt(7));
		assertEquals(5, this.shape.getCoordAt(8));
		assertEquals(-1, this.shape.getCoordAt(9));
		assertEquals(6, this.shape.getCoordAt(10));
		assertEquals(5, this.shape.getCoordAt(11));
		assertEquals(7, this.shape.getCoordAt(12));
		assertEquals(-5, this.shape.getCoordAt(13));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void setLastPointPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
		
		this.shape.setLastPoint(createPoint(123, 789));

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 123, 789);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void toCollection(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Collection<Point2D> expected = Arrays.asList(
				createPoint(0, 0),
				createPoint(2, 2),
				createPoint(3, 0),
				createPoint(4, 3),
				createPoint(5, -1),
				createPoint(6, 5),
				createPoint(7, -5));
		Collection<?> points = this.shape.toCollection();
		assertCollectionEquals(expected, points);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	@Override
	public void setIT(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T newPath = (T) createPath();
		newPath.moveTo(14, -5);
		newPath.lineTo(1, 6);
		newPath.quadTo(-5, 1, 10, -1);
		newPath.curveTo(18, 19, -50, 51, 1, 0);

		this.shape.set(newPath);

		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 14, -5);
		assertElement(pi, PathElementType.LINE_TO, 1, 6);
		assertElement(pi, PathElementType.QUAD_TO, -5, 1, 10, -1);
		assertElement(pi, PathElementType.CURVE_TO, 18, 19, -50, 51, 1, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsPathIterator2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Path2ai path1;
		Path2ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertFalse(path1.intersects((PathIterator2ai) path2.getPathIterator()));
		assertFalse(path2.intersects((PathIterator2ai) path1.getPathIterator()));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		assertTrue(path1.intersects((PathIterator2ai) path2.getPathIterator()));
		assertTrue(path2.intersects((PathIterator2ai) path1.getPathIterator()));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertTrue(path1.intersects((PathIterator2ai) path2.getPathIterator()));
		assertTrue(path2.intersects((PathIterator2ai) path1.getPathIterator()));

		path1 = createPath();
		path1.moveTo(-33, 98);
		path1.lineTo(-35, 98);
		path1.lineTo(-35, 101);
		path1.lineTo(-33, 101);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99);
		path2.lineTo(-31, 99);
		path2.lineTo(-31, 103);
		path2.lineTo(-34, 103);
		path2.closePath();
		assertTrue(path1.intersects((PathIterator2ai) path2.getPathIterator()));
		assertTrue(path2.intersects((PathIterator2ai) path1.getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void intersectsShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape2D) createCircle(3, 0, 1)));
		assertTrue(this.shape.intersects((Shape2D) createRectangle(-1, -1, 1, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_addVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(3, 4));
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_plusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_plus(createVector(3, 4));
		PathIterator2ai pi = r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_removeVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(3, 4));
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -3, -4);
		assertElement(pi, PathElementType.LINE_TO, -1, -2);
		assertElement(pi, PathElementType.QUAD_TO, 0, -4, 1, -1);
		assertElement(pi, PathElementType.CURVE_TO, 2, -5, 3, 1, 4, -9);
		assertElement(pi, PathElementType.CLOSE, -3, -4);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_minusVector2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_minus(createVector(3, 4));
		PathIterator2ai pi = r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -3, -4);
		assertElement(pi, PathElementType.LINE_TO, -1, -2);
		assertElement(pi, PathElementType.QUAD_TO, 0, -4, 1, -1);
		assertElement(pi, PathElementType.CURVE_TO, 2, -5, 3, 1, 4, -9);
		assertElement(pi, PathElementType.CLOSE, -3, -4);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_multiplyTransform2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		Transform2D tr = new Transform2D();
		tr.makeTranslationMatrix(3, 4);
		Transform2D tr2 = new Transform2D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		Transform2D tr3 = new Transform2D();
		tr3.mul(tr, tr2);

		Path2ai clone = (Path2ai) this.shape.operator_multiply(tr3);

		PathIterator2ai pi = (PathIterator2ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 1, 6, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2, -1, 8, -2, -2, -3);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createPoint(0, 0)));
		assertTrue(this.shape.operator_and(createPoint(4, 3)));
		assertTrue(this.shape.operator_and(createPoint(2, 2)));
		assertTrue(this.shape.operator_and(createPoint( 2, 1)));
		assertTrue(this.shape.operator_and(createPoint(4, 2)));
		assertFalse(this.shape.operator_and(createPoint(-1, -1)));
		assertFalse(this.shape.operator_and(createPoint(6, 2)));
		assertTrue(this.shape.operator_and(createPoint(3, -2)));
		assertFalse(this.shape.operator_and(createPoint(2, -2)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_andShape2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createCircle(3, 0, 1)));
		assertTrue(this.shape.operator_and(createRectangle(-1, -1, 1, 1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void operator_upToPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(1, 0)));
		assertEpsilonEquals(7.071067812f, this.shape.operator_upTo(createPoint(-5, -5)));
		assertEpsilonEquals(3f, this.shape.operator_upTo(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(7, 0)));
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
		assertTrue(this.shape.isPolygon());

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

	public void generateShapeBitmap() throws IOException {
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.ARC_ONLY);
		File filename = generateTestPicture(this.shape);
		System.out.println("Filename: " + filename); //$NON-NLS-1$
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToIntIntIntIntDoubleDoubleArcType_01_arcOnly(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.ARC_ONLY);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToIntIntIntIntDoubleDoubleArcType_01_lineTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.LINE_THEN_ARC);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToIntIntIntIntDoubleDoubleArcType_01_moveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.MOVE_THEN_ARC);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToIntIntIntIntDoubleDoubleArcType_0251_arcOnly(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, .25, 1, ArcType.ARC_ONLY);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 9, 4, 14, 8, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToIntIntIntIntDoubleDoubleArcType_0251_lineTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, .25, 1, ArcType.LINE_THEN_ARC);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 6, 0);
		assertElement(pi, PathElementType.CURVE_TO, 9, 4, 14, 8, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToIntIntIntIntDoubleDoubleArcType_0251_moveTo(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10, .25, 1, ArcType.MOVE_THEN_ARC);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.MOVE_TO, 6, 0);
		assertElement(pi, PathElementType.CURVE_TO, 9, 4, 14, 8, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToPoint2DPoint2DDoubleDoubleArcType_01_arcOnly(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(createPoint(5, 5), createPoint(20, 10), 0, 1, ArcType.ARC_ONLY);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToIntIntIntInt(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(5, 5, 20, 10);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void arcToPoint2DPoint2D(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		this.shape.arcTo(createPoint(5, 5), createPoint(20, 10));
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 6, 1, 12, 7, 20, 10);
		assertNoElement(pi);
	}

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToCircle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertIntPointEquals(0, 0, this.shape.getClosestPointTo(createCircle(-2, 1, 1)));
        assertClosestPointInBothShapes(this.shape, createCircle(-2, 1, 2));
        assertClosestPointInBothShapes(this.shape, createCircle(0, 1, 3));
        assertClosestPointInBothShapes(this.shape, createCircle(3, -1, 8));
        assertClosestPointInBothShapes(this.shape, createCircle(3, -1, 1));
        assertClosestPointInBothShapes(this.shape, createCircle(4, -1, 0));
        assertIntPointEquals(6, 0, this.shape.getClosestPointTo(createCircle(20, 0, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredCircle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(4, this.shape.getDistanceSquared(createCircle(-2, 1, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(-2, 1, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(0, 1, 3)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(3, -1, 8)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(3, -1, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(4, -1, 0)));
        assertEpsilonEquals(144, this.shape.getDistanceSquared(createCircle(20, 0, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToSegment2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertIntPointEquals(1, 1, this.shape.getClosestPointTo(createSegment(-2, 1, 0, 2)));
        assertIntPointEquals(0, 0, this.shape.getClosestPointTo(createSegment(-2, 1, 0, 3)));
        assertIntPointEquals(0, 0, this.shape.getClosestPointTo(createSegment(0, 1, 2, 3)));
        assertClosestPointInBothShapes(this.shape, createSegment(3, -1, 8, 8));
        assertClosestPointInBothShapes(this.shape, createSegment(3, -1, 1, 1));
        assertClosestPointInBothShapes(this.shape, createSegment(4, -1, 0, 0));
        assertIntPointEquals(7, -1, this.shape.getClosestPointTo(createSegment(20, 0, 25, 4)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredSegment2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(2, this.shape.getDistanceSquared(createSegment(-2, 1, 0, 2)));
        assertEpsilonEquals(5, this.shape.getDistanceSquared(createSegment(-2, 1, 0, 3)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createSegment(0, 1, 2, 3)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(3, -1, 8, 8)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(3, -1, 1, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(4, -1, 0, 0)));
        assertEpsilonEquals(170, this.shape.getDistanceSquared(createSegment(20, 0, 25, 4)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertIntPointEquals(0, 0, this.shape.getClosestPointTo(createRectangle(-2, 1, 1, 1)));
        assertIntPointEquals(0, 0, this.shape.getClosestPointTo(createRectangle(-2, 1, 2, 2)));
        assertClosestPointInBothShapes(this.shape, createRectangle(0, 1, 3, 3));
        assertClosestPointInBothShapes(this.shape, createRectangle(3, -1, 8, 8));
        assertClosestPointInBothShapes(this.shape, createRectangle(3, -1, 1, 1));
        assertClosestPointInBothShapes(this.shape, createRectangle(4, -1, 0, 0));
        assertIntPointEquals(7, -1, this.shape.getClosestPointTo(createRectangle(20, 0, 2, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredRectangle2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(2, this.shape.getDistanceSquared(createRectangle(-2, 1, 1, 1)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createRectangle(-2, 1, 2, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(0, 1, 3, 3)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(3, -1, 8, 8)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(3, -1, 1, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(4, -1, 0, 0)));
        assertEpsilonEquals(170, this.shape.getDistanceSquared(createRectangle(20, 0, 2, 2)));
    }

    protected MultiShape2ai createTestMultiShape(int dx, int dy) {
        MultiShape2ai multishape = createMultiShape();
        Segment2ai segment = createSegment(dx - 5, dy - 4, dx - 8, dy - 1);
        Rectangle2ai rectangle = createRectangle(dx + 2, dy + 1, 3, 2);
        multishape.add(segment);
        multishape.add(rectangle);
        return multishape;
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToMultiShape2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(-2, 1));
        assertIntPointEquals(2, 2, this.shape.getClosestPointTo(createTestMultiShape(-2, 2)));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(0, 1));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(3, -1));
        assertIntPointEquals(5, 2, this.shape.getClosestPointTo(createTestMultiShape(4, -1)));
        assertIntPointEquals(7, -1, this.shape.getClosestPointTo(createTestMultiShape(20, 0)));
    }
        
    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredMultiShape2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(-2, 1)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createTestMultiShape(-2, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(0, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(3, -1)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createTestMultiShape(4, -1)));
        assertEpsilonEquals(25, this.shape.getDistanceSquared(createTestMultiShape(20, 0)));
    }

    protected Path2ai createTestPath(int dx, int dy) {
        Path2ai path = createPath();
        path.moveTo(dx + 5, dy - 5);
        path.lineTo(dx + 20, dy + 5);
        path.lineTo(dx + 0, dy + 20);
        path.lineTo(dx - 5, dy);
        return path;
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToPath2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertClosestPointInBothShapes(this.shape, createTestPath(-2, 1));
        assertClosestPointInBothShapes(this.shape, createTestPath(-2, 2));
        assertClosestPointInBothShapes(this.shape, createTestPath(0, 1));
        assertClosestPointInBothShapes(this.shape, createTestPath(3, -1));
        assertClosestPointInBothShapes(this.shape, createTestPath(4, -1));
        assertIntPointEquals(7, -1, this.shape.getClosestPointTo(createTestPath(20, 0)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredPath2ai(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(-2, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(-2, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(0, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(3, -1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(4, -1)));
        assertEpsilonEquals(65, this.shape.getDistanceSquared(createTestPath(20, 0)));
    }
    
}