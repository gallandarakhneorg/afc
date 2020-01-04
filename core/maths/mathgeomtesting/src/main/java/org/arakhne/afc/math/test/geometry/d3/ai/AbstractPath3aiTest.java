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

package org.arakhne.afc.math.test.geometry.d3.ai;

import static org.arakhne.afc.math.geometry.GeomConstants.SHAPE_INTERSECTS;
import static org.arakhne.afc.math.geometry.GeomConstants.SPLINE_APPROXIMATION_RATIO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.BasicPathShadow3ai;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai;
import org.arakhne.afc.math.geometry.d3.ai.Shape3ai;

@SuppressWarnings("all")
public abstract class AbstractPath3aiTest<T extends Path3ai<?, T, ?, ?, ?, B>,
		B extends RectangularPrism3ai<?, ?, ?, ?, ?, B>> extends AbstractShape3aiTest<T, B> {

	@Override
	protected final T createShape() {
		T path = (T) createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		return path;
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		PathIterator3ai pi = clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createPath()));
		assertFalse(this.shape.equals(createRectangularPrism(5, 8, 0, 10, 6, 0)));
		assertTrue(this.shape.equals(this.shape));
		Path3ai path = createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		assertTrue(this.shape.equals(path));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createPath().getPathIterator()));
		assertFalse(this.shape.equals(createRectangularPrism(5, 8, 0, 10, 6, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		Path3ai path = createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		assertTrue(this.shape.equals(path.getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createPath()));
		assertTrue(this.shape.equalsToShape(this.shape));
		T path = (T) createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		assertTrue(this.shape.equalsToShape(path));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createPath().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createRectangularPrism(5, 8, 0, 10, 6, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		Path3ai path = createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		assertTrue(this.shape.equalsToPathIterator(path.getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());

		this.shape.clear();
		assertTrue(this.shape.isEmpty());
		
		this.shape.moveTo(1, 2, 0);
		assertTrue(this.shape.isEmpty());
		this.shape.moveTo(3, 4, 0);
		assertTrue(this.shape.isEmpty());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isEmpty());
		this.shape.closePath();
		assertFalse(this.shape.isEmpty());

		this.shape.clear();
		assertTrue(this.shape.isEmpty());

		this.shape.moveTo(1, 2, 0);
		assertTrue(this.shape.isEmpty());
		this.shape.moveTo(3, 4, 0);
		assertTrue(this.shape.isEmpty());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isEmpty());
		this.shape.closePath();
		assertTrue(this.shape.isEmpty());

		this.shape.clear();
		assertTrue(this.shape.isEmpty());

		this.shape.moveTo(1, 2, 0);
		assertTrue(this.shape.isEmpty());
		this.shape.moveTo(3, 4, 0);
		assertTrue(this.shape.isEmpty());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isEmpty());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEquals(0, this.shape.size());
	}

	@Override
	public void getPointIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		Iterator<? extends Point3D> it = this.shape.getPointIterator();
		
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
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeCrossingsFromPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path3ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), -2, 1, 0, null));
		assertEquals(0, Path3ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -3, 0, null));
		assertEquals(SHAPE_INTERSECTS,
				Path3ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 4, 3, 0, null));
		assertEquals(-2, Path3ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeCrossingsFromRect(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path3ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				-2, 1, 0, -1, 2, 0,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				0, 1, 0, 3, 6, 0,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				3, -1, 0, 8, 0, 0,
				null));
		assertEquals(-2, Path3ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				3, -1, 0, 4, 0, 0,
				null));
		assertEquals(-2, Path3ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				3, -1, 0, 5, 0, 0,
				null));
		assertEquals(0, Path3ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				0, -4, 0, 3, -3, 0,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				0, -4, 0, 4, -3, 0,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				0, -4, 0, 3, -2, 0,
				null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeCrossingsFromSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path3ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				-2, 1, 0, -1, 2, 0,
				null));
		assertEquals(0, Path3ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				0, 1, 0, 3, 6, 0,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				3, -1, 0, 8, 0, 0,
				null));
		assertEquals(-2, Path3ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				3, -1, 0, 4, 0, 0,
				null));
		assertEquals(-2, Path3ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				3, -1, 0, 5, 0, 0,
				null));
		assertEquals(0, Path3ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				0, -4, 0, 3, -3, 0,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				0, -4, 0, 4, -3, 0,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				0, -4, 0, 3, -2, 0,
				null));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticcomputeCrossingsFromSphere(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, Path3ai.computeCrossingsFromSphere(
				0,
				this.shape.getPathIterator(),
				-2, 1, 0, 1,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromSphere(
				0,
				this.shape.getPathIterator(),
				-2, 1, 0, 2,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromSphere(
				0,
				this.shape.getPathIterator(),
				0, 1, 0, 3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromSphere(
				0,
				this.shape.getPathIterator(),
				3, -1, 0, 8,
				null));
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromSphere(
				0,
				this.shape.getPathIterator(),
				3, -1, 0, 1,
				null));
		assertEquals(-2, Path3ai.computeCrossingsFromSphere(
				0,
				this.shape.getPathIterator(),
				4, -1, 0, 0,
				null));
		assertEquals(0, Path3ai.computeCrossingsFromSphere(
				0,
				this.shape.getPathIterator(),
				20, 0, 0, 2,
				null));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeCrossingsFromPath_notCloseable_noOnlyIntersectWhenOpen(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path1;
		Path3ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertEquals(1, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.STANDARD));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.STANDARD));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.STANDARD));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.STANDARD));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeCrossingsFromPath_closeable_noOnlyIntersectWhenOpen(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path1;
		Path3ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.AUTO_CLOSE));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
				0,
		        (PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.AUTO_CLOSE));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.AUTO_CLOSE));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.AUTO_CLOSE));
	}
		
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeCrossingsFromPath_noCloseable_onlyIntersectWhenOpen(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path1;
		Path3ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertEquals(0, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertEquals(SHAPE_INTERSECTS, Path3ai.computeCrossingsFromPath(
		        0,
				(PathIterator3ai) path2.getPathIterator(),
				new BasicPathShadow3ai(path1),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticContainsPathIterator2iIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 0, 0, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 4, 3, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 2, 2, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 2, 1, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 4, 2, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 4, 3, 0));
		assertFalse(Path3ai.contains(this.shape.getPathIterator(), -1, -1, 0));
		assertFalse(Path3ai.contains(this.shape.getPathIterator(), 6, 2, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 3, -2, 0));
		assertFalse(Path3ai.contains(this.shape.getPathIterator(), 2, -2, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticIntersectsPathIterator2iIntIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 0, 0, 0, 1, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 4, 3, 0, 1, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 2, 2, 0, 1, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 2, 1, 0, 1, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 3, 0, 0, 1, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), -1, -1, 0, 1, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 4, -3, 0, 1, 1, 0));
		assertFalse(Path3ai.intersects(this.shape.getPathIterator(), -3, 4, 0, 1, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 6, -5, 0, 1, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 4, 0, 0, 1, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 5, 0, 0, 1, 1, 0));
		assertFalse(Path3ai.intersects(this.shape.getPathIterator(), 0, -3, 0, 1, 1, 0));
		assertFalse(Path3ai.intersects(this.shape.getPathIterator(), 0, -3, 0, 2, 1, 0));
		assertTrue(Path3ai.intersects(this.shape.getPathIterator(), 0, -3, 0, 3, 1, 0));
	}
	
	@Override
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(0, 0, 0));
		assertEquals(0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		// remember: path is closed
		p = this.shape.getClosestPointTo(createPoint(-1, -4, 0));
		assertEquals(0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = this.shape.getClosestPointTo(createPoint(4, 0, 0));
		assertEquals(4, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = this.shape.getClosestPointTo(createPoint(4, 2, 0));
		assertEquals(4, p.ix(), p.toString());
		assertEquals(2, p.iy(), p.toString());

		p = this.shape.getClosestPointTo(createPoint(4, -1, 0));
		assertEquals(4, p.ix(), p.toString());
		assertEquals(-1, p.iy(), p.toString());

		p = this.shape.getClosestPointTo(createPoint(2, -3, 0));
		assertEquals(3, p.ix(), p.toString());
		assertEquals(-2, p.iy(), p.toString());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCurrentX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(7, this.shape.getCurrentX());
		this.shape.lineTo(148, 752, 0);
		assertEquals(148, this.shape.getCurrentX());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getCurrentY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEquals(-5, this.shape.getCurrentY());
	    this.shape.lineTo(148, 752, 0);
	    assertEquals(752, this.shape.getCurrentY());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getCurrentZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(-5, this.shape.getCurrentZ());
		this.shape.lineTo(148, 752, 0);
		assertEquals(752, this.shape.getCurrentZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getCurrentPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertIntPointEquals(7, -5, 0, this.shape.getCurrentPoint());
		this.shape.lineTo(148, 752, 0);
		assertIntPointEquals(148, 752, 0, this.shape.getCurrentPoint());
	}

	@Override
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0, 0));
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = this.shape.getFarthestPointTo(createPoint(-1, -4, 0)); // remember: path is closed
		assertEquals(4, p.ix(), p.toString());
		assertEquals(3, p.iy(), p.toString());

		p = this.shape.getFarthestPointTo(createPoint(4, 0, 0));
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = this.shape.getFarthestPointTo(createPoint(4, 2, 0));
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = this.shape.getFarthestPointTo(createPoint(4, -1, 0));
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());
	}

	@Override
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(1, 0, 0)));
		assertEpsilonEquals(7.071067812f, this.shape.getDistance(createPoint(-5, -5, 0)));
		assertEpsilonEquals(3f, this.shape.getDistance(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(7, 0, 0)));
	}

	@Override
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(1, 0, 0)));
		assertEpsilonEquals(50f, this.shape.getDistanceSquared(createPoint(-5, -5, 0)));
		assertEpsilonEquals(9f, this.shape.getDistanceSquared(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(7, 0, 0)));
	}

	@Override
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(1, 0, 0)));
		assertEpsilonEquals(10f, this.shape.getDistanceL1(createPoint(-5, -5, 0)));
		assertEpsilonEquals(3f, this.shape.getDistanceL1(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(7, 0, 0)));
	}

	@Override
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(1, 0, 0)));
		assertEpsilonEquals(5f, this.shape.getDistanceLinf(createPoint(-5, -5, 0)));
		assertEpsilonEquals(3f, this.shape.getDistanceLinf(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(7, 0, 0)));
	}

	@Override
	public void translateIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(3, 4, 0);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 0, 7, 7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 0, 9, 9, 0, 10, -1, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@Override
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(3, 4, 0));
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 0, 7, 7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 0, 9, 9, 0, 10, -1, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setWindingRule(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(PathWindingRule.NON_ZERO, this.shape.getWindingRule());
		for(PathWindingRule rule : PathWindingRule.values()) {
			this.shape.setWindingRule(rule);
			assertEquals(rule, this.shape.getWindingRule());
		}
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void addIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai p2 = createPath();
		p2.moveTo(3, 4, 0);
		p2.lineTo(5, 6, 0);
		
		this.shape.add(p2.getPathIterator());
		
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 6, 0);
		assertNoElement(pi);
	}

	@Override
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3ai pi;
		
		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3, 4, 0);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 0, 7, 7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 0, 9, 9, 0, 10, -1, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getPathIteratorDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.LINE_TO, 3, 1, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 2, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 3, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 2, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 2, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 1, 0);
		assertElement(pi, PathElementType.LINE_TO, 6, 1, 0);
		assertElement(pi, PathElementType.LINE_TO, 6, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 6, -1, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, -1, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, -2, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, -3, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, -4, 0);
		assertElement(pi, PathElementType.LINE_TO, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void transformTransform3D_translation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr = new Transform3D();
		tr.makeTranslationMatrix(3, 4, 0);
		
		Path3ai clone = this.shape.clone();
		clone.transform(tr);

		PathIterator3ai pi = (PathIterator3ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 0, 7, 7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 0, 9, 9, 0, 10, -1, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void createTransformedShape_translation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr = new Transform3D();
		tr.makeTranslationMatrix(3, 4, 0);
		
		Shape3ai clone = this.shape.createTransformedShape(tr);
		
		assertTrue(clone instanceof Path3ai);

		PathIterator3ai pi = (PathIterator3ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 0, 7, 7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 0, 9, 9, 0, 10, -1, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void transformTransform3D_rotation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr2 = new Transform3D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		
		Path3ai clone = this.shape.clone();
		clone.transform(tr2);

		PathIterator3ai pi = (PathIterator3ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, -2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 0, -3, 0, 3, -4, 0);
		assertElement(pi, PathElementType.CURVE_TO, -1, -5, 0, 5, -6, 0, -5, -7, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void createTransformedShape_rotation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr2 = new Transform3D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		
		Path3ai clone = (Path3ai) this.shape.createTransformedShape(tr2);

		PathIterator3ai pi = (PathIterator3ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, -2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 0, -3, 0, 3, -4, 0);
		assertElement(pi, PathElementType.CURVE_TO, -1, -5, 0, 5, -6, 0, -5, -7, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void transformTransform3D_translationRotation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr = new Transform3D();
		tr.makeTranslationMatrix(3, 4, 0);
		Transform3D tr2 = new Transform3D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		Transform3D tr3 = new Transform3D();
		tr3.mul(tr, tr2);

		Path3ai clone = this.shape.clone();
		clone.transform(tr3);

		PathIterator3ai pi = (PathIterator3ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 1, 0, 6, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2, -1, 0, 8, -2, 0, -2, -3, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr = new Transform3D();
		tr.makeTranslationMatrix(3, 4, 0);
		Transform3D tr2 = new Transform3D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		Transform3D tr3 = new Transform3D();
		tr3.mul(tr, tr2);

		Path3ai clone = (Path3ai) this.shape.createTransformedShape(tr3);

		PathIterator3ai pi = (PathIterator3ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 1, 0, 6, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2, -1, 0, 8, -2, 0, -2, -3, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@Override
	public void containsIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(0, 0, 0));
		assertTrue(this.shape.contains(4, 3, 0));
		assertTrue(this.shape.contains(2, 2, 0));
		assertTrue(this.shape.contains(2, 1, 0));
		assertTrue(this.shape.contains(4, 2, 0));
		assertFalse(this.shape.contains(-1, -1, 0));
		assertFalse(this.shape.contains(6, 2, 0));
		assertTrue(this.shape.contains(3, -2, 0));
		assertFalse(this.shape.contains(2, -2, 0));
	}

	@Override
	public void containsRectangularPrism3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangularPrism(0, 0, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(4, 3, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(2, 2, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(2, 1, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(3, 0, 0, 1, 1, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(3, 0, 0, 1, 0, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(3, 0, 0, 2, 1, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(3, 0, 0, 2, 0, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-1, -1, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(4, -3, 0, 1, 1, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(5, -3, 0, 0, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(-3, 4, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(6, -5, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(4, 0, 0, 1, 1, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(4, 0, 0, 1, 0, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(5, 0, 0, 1, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(5, 0, 0, 1, 0, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(5, 0, 0, 0, 1, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(5, 0, 0, 0, 0, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(5, 0, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(6, 0, 0, 1, 1, 0)));
	}

	@Override
	public void intersectsRectangularPrism3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createRectangularPrism(0, 0, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(4, 3, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(2, 2, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(2, 1, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(3, 0, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(-1, -1, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(4, -3, 0, 1, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-3, 4, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(6, -5, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(4, 0, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(5, 0, 0, 1, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(0, -3, 0, 1, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(0, -3, 0, 2, 1, 0)));
	}

	@Override
	public void intersectsSphere3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSphere(0, 0, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(4, 3, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(2, 2, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(2, 1, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(3, 0, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(-1, -1, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(4, -3, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(-3, 4, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(6, -5, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(4, 0, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(5, 0, 0, 1)));
		assertTrue(this.shape.intersects(createSphere(6, 2, 0, 1)));
		assertFalse(this.shape.intersects(createSphere(-5, 0, 0, 3)));
	}

	@Override
	public void intersectsSegment3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects(createSegment(0, 0, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 3, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(2, 2, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(2, 1, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(3, 0, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(-1, -1, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(4, -3, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(-3, 4, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(6, -5, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 1, 1, 0)));
		assertTrue(this.shape.intersects(createSegment(5, 0, 0, 1, 1, 0)));
		assertFalse(this.shape.intersects(createSegment(-4, -4, 0, -3, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(-1, 0, 0, 2, 3, 0)));
		assertFalse(this.shape.intersects(createSegment(7, 1, 0, 18, 14, 0)));
	}

	@Override
	public void intersectsPath3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path1;
		Path3ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertFalse(path1.intersects(path2));
		assertFalse(path2.intersects(path1));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertTrue(path1.intersects(path2));
		assertTrue(path2.intersects(path1));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertTrue(path1.intersects(path2));
		assertTrue(path2.intersects(path1));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertTrue(path1.intersects(path2));
		assertTrue(path2.intersects(path1));
	}
	
	@Override
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B bb = this.shape.toBoundingBox();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(3, bb.getMaxY());
	}

	@Override
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B bb = createRectangularPrism(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(3, bb.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void toBoundingBoxWithCtrlPoints(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B bb = this.shape.toBoundingBoxWithCtrlPoints();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(5, bb.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void removeLast(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
		
		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertNoElement(pi);

		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertNoElement(pi);

		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertNoElement(pi);

		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertNoElement(pi);

		this.shape.removeLast();

		pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void setLastPointIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
		
		this.shape.setLastPoint(123, 789, 0);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 123, 789, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void removeIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
		
		this.shape.remove(2, 2, 0);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);

		this.shape.remove(4, 3, 0);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);

		this.shape.remove(6, 5, 0);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);

		this.shape.remove(6, 5, 0);

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@Override
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.contains(createPoint(0, 0, 0)));
		assertTrue(this.shape.contains(createPoint(4, 3, 0)));
		assertTrue(this.shape.contains(createPoint(2, 2, 0)));
		assertTrue(this.shape.contains(createPoint(2, 1, 0)));
		assertTrue(this.shape.contains(createPoint(4, 2, 0)));
		assertFalse(this.shape.contains(createPoint(-1, -1, 0)));
		assertFalse(this.shape.contains(createPoint(6, 2, 0)));
		assertTrue(this.shape.contains(createPoint(3, -2, 0)));
		assertFalse(this.shape.contains(createPoint(2, -2, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticContainsPathIteratorIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 0, 0, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 4, 3, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 2, 2, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 2, 1, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 4, 2, 0));
		assertFalse(Path3ai.contains(this.shape.getPathIterator(), -1, -1, 0));
		assertFalse(Path3ai.contains(this.shape.getPathIterator(), 6, 2, 0));
		assertTrue(Path3ai.contains(this.shape.getPathIterator(), 3, -2, 0));
		assertFalse(Path3ai.contains(this.shape.getPathIterator(), 2, -2, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticComputeDrawableElementBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = createRectangularPrism(0, 0, 0, 0, 0, 0);
		assertTrue(Path3ai.computeDrawableElementBoundingBox(this.shape.getPathIterator(), box));
		assertEquals(0, box.getMinX());
		assertEquals(-5, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(3, box.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticGetClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 0, 0, 0, p);
		assertEquals(0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = createPoint(0, 0, 0);
		// remember: path is closed
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -1, -4, 0, p);
		assertEquals(0, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, 0, 0, p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(0, p.iy(), p.toString());

		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, 2, 0, p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(2, p.iy(), p.toString());

		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, -1, 0, p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(-1, p.iy(), p.toString());

		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 2, -3, 0, p);
		assertEquals(3, p.ix(), p.toString());
		assertEquals(-2, p.iy(), p.toString());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticGetFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = createPoint(0, 0, 0);
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 0, 0, 0, p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = createPoint(0, 0, 0);
		// remember: path is closed
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -1, -4, 0, p);
		assertEquals(4, p.ix(), p.toString());
		assertEquals(3, p.iy(), p.toString());

		p = createPoint(0, 0, 0);
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, 0, 0, p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = createPoint(0, 0, 0);
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, 2, 0, p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());

		p = createPoint(0, 0, 0);
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, -1, 0, p);
		assertEquals(7, p.ix(), p.toString());
		assertEquals(-5, p.iy(), p.toString());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void moveToIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(15, 145, 0);

		PathIterator3ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(15, 145, 0);
		tmpShape.moveTo(-15, -954, 0);

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -15, -954, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void moveToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(createPoint(15, 145, 0));

		PathIterator3ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(createPoint(15, 145, 0));
		tmpShape.moveTo(createPoint(-15, -954, 0));

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -15, -954, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void lineToIntInt_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(15, 145, 0);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void lineToIntInt_moveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(15, 145, 0);
		tmpShape.lineTo(189, -45, 0);

		PathIterator3ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145, 0);
		assertElement(pi, PathElementType.LINE_TO, 189, -45, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(15, 145, 0);
		tmpShape.lineTo(189, -45, 0);
		tmpShape.lineTo(-5, 0, 0);

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145, 0);
		assertElement(pi, PathElementType.LINE_TO, 189, -45, 0);
		assertElement(pi, PathElementType.LINE_TO, -5, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void lineToPoint3D_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(createPoint(15, 145, 0));
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void lineToPoint3D_moveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(15, 145, 0);
		tmpShape.lineTo(createPoint(189, -45, 0));

		PathIterator3ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145, 0);
		assertElement(pi, PathElementType.LINE_TO, 189, -45, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(15, 145, 0);
		tmpShape.lineTo(createPoint(189, -45, 0));
		tmpShape.lineTo(createPoint(-5, 0, 0));

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15, 145, 0);
		assertElement(pi, PathElementType.LINE_TO, 189, -45, 0);
		assertElement(pi, PathElementType.LINE_TO, -5, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void quadToIntIntIntInt_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.quadTo(15, 145, 0, 50, 20, 0);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void quadToIntIntIntInt_moveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(4, 6, 0);
		tmpShape.quadTo(15, 145, 0, 50, 20, 0);

		PathIterator3ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 15, 145, 0, 50, 20, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(4, 6, 0);
		tmpShape.quadTo(15, 145, 0, 50, 20, 0);
		tmpShape.quadTo(-42, 0, 0, -47, -60, 0);

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 15, 145, 0, 50, 20, 0);
		assertElement(pi, PathElementType.QUAD_TO, -42, 0, 0, -47, -60, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void quadToPoint3DPoint3D_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.quadTo(createPoint(15, 145, 0), createPoint(50, 20, 0));
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void quadToPoint3DPoint3D_moveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(4, 6, 0);
		tmpShape.quadTo(createPoint(15, 145, 0), createPoint(50, 20, 0));

		PathIterator3ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 15, 145, 0, 50, 20, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(4, 6, 0);
		tmpShape.quadTo(createPoint(15, 145, 0), createPoint(50, 20, 0));
		tmpShape.quadTo(createPoint(-42, 0, 0), createPoint(-47, -60, 0));

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 15, 145, 0, 50, 20, 0);
		assertElement(pi, PathElementType.QUAD_TO, -42, 0, 0, -47, -60, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void curveToIntIntIntIntIntInt_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(15, 145, 0, 50, 20, 0, 0, 0, 0);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void curveToIntIntIntIntIntInt_moveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(4, 6, 0);
		tmpShape.curveTo(15, 145, 0, 50, 20, 0, 0, 0, 0);

		PathIterator3ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.CURVE_TO, 15, 145, 0, 50, 20, 0, 0, 0, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(4, 6, 0);
		tmpShape.curveTo(15, 145, 0, 50, 20, 0, 0, 0, 0);
		tmpShape.curveTo(-42, 0, 0, -47, -60, 0, 1, 2, 0);

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.CURVE_TO, 15, 145, 0, 50, 20, 0, 0, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, -42, 0, 0, -47, -60, 0, 1, 2, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void curveToPoint3DPoint3DPoint3D_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(createPoint(15, 145, 0), createPoint(50, 20, 0), createPoint(0, 0, 0));
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void curveToPoint3DPoint3DPoint3Dt_moveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.moveTo(4, 6, 0);
		tmpShape.curveTo(createPoint(15, 145, 0), createPoint(50, 20, 0), createPoint(0, 0, 0));

		PathIterator3ai<?> pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.CURVE_TO, 15, 145, 0, 50, 20, 0, 0, 0, 0);
		assertNoElement(pi);

		tmpShape = createPath();
		tmpShape.moveTo(4, 6, 0);
		tmpShape.curveTo(createPoint(15, 145, 0), createPoint(50, 20, 0), createPoint(0, 0, 0));
		tmpShape.curveTo(createPoint(-42, 0, 0), createPoint(-47, -60, 0), createPoint(1, 2, 0));

		pi = tmpShape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 4, 6, 0);
		assertElement(pi, PathElementType.CURVE_TO, 15, 145, 0, 50, 20, 0, 0, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, -42, 0, 0, -47, -60, 0, 1, 2, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void lengthSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(98, this.shape.getLengthSquared());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void length(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(9.899494937, this.shape.getLength());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getCoordAt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
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
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void setLastPointPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
		
		this.shape.setLastPoint(createPoint(123, 789, 0));

		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 123, 789, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void toCollection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Collection<Point3D> expected = Arrays.asList(
				createPoint(0, 0, 0),
				createPoint(2, 2, 0),
				createPoint(3, 0, 0),
				createPoint(4, 3, 0),
				createPoint(5, -1, 0),
				createPoint(6, 5, 0),
				createPoint(7, -5, 0));
		Collection<?> points = this.shape.toCollection();
		assertCollectionEquals(expected, points);
	}

	@Override
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T newPath = (T) createPath();
		newPath.moveTo(14, -5, 0);
		newPath.lineTo(1, 6, 0);
		newPath.quadTo(-5, 1, 0, 10, -1, 0);
		newPath.curveTo(18, 19, 0, -50, 51, 0, 1, 0, 0);

		this.shape.set(newPath);

		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 14, -5, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, -5, 1, 0, 10, -1, 0);
		assertElement(pi, PathElementType.CURVE_TO, 18, 19, 0, -50, 51, 0, 1, 0, 0);
		assertNoElement(pi);
	}

	@Override
	public void intersectsPathIterator3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path1;
		Path3ai path2;
		
		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertFalse(path1.intersects((PathIterator3ai) path2.getPathIterator()));
		assertFalse(path2.intersects((PathIterator3ai) path1.getPathIterator()));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		assertTrue(path1.intersects((PathIterator3ai) path2.getPathIterator()));
		assertTrue(path2.intersects((PathIterator3ai) path1.getPathIterator()));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertTrue(path1.intersects((PathIterator3ai) path2.getPathIterator()));
		assertTrue(path2.intersects((PathIterator3ai) path1.getPathIterator()));

		path1 = createPath();
		path1.moveTo(-33, 98, 0);
		path1.lineTo(-35, 98, 0);
		path1.lineTo(-35, 101, 0);
		path1.lineTo(-33, 101, 0);
		path1.closePath();
		path2 = createPath();
		path2.moveTo(-33, 99, 0);
		path2.lineTo(-31, 99, 0);
		path2.lineTo(-31, 103, 0);
		path2.lineTo(-34, 103, 0);
		path2.closePath();
		assertTrue(path1.intersects((PathIterator3ai) path2.getPathIterator()));
		assertTrue(path2.intersects((PathIterator3ai) path1.getPathIterator()));
	}

	@Override
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSphere(3, 0, 0, 1)));
		assertTrue(this.shape.intersects((Shape3D) createRectangularPrism(-1, -1, 0, 1, 1, 0)));
	}

	@Override
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(3, 4, 0));
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 0, 7, 7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 0, 9, 9, 0, 10, -1, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_plus(createVector(3, 4, 0));
		PathIterator3ai pi = r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 0, 7, 7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 0, 9, 9, 0, 10, -1, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(3, 4, 0));
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -3, -4, 0);
		assertElement(pi, PathElementType.LINE_TO, -1, -2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 0, -4, 0, 1, -1, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2, -5, 0, 3, 1, 0, 4, -9, 0);
		assertElement(pi, PathElementType.CLOSE, -3, -4, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_minus(createVector(3, 4, 0));
		PathIterator3ai pi = r.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -3, -4, 0);
		assertElement(pi, PathElementType.LINE_TO, -1, -2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 0, -4, 0, 1, -1, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2, -5, 0, 3, 1, 0, 4, -9, 0);
		assertElement(pi, PathElementType.CLOSE, -3, -4, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr = new Transform3D();
		tr.makeTranslationMatrix(3, 4, 0);
		Transform3D tr2 = new Transform3D();
		tr2.makeRotationMatrix(-MathConstants.DEMI_PI);
		Transform3D tr3 = new Transform3D();
		tr3.mul(tr, tr2);

		Path3ai clone = (Path3ai) this.shape.operator_multiply(tr3);

		PathIterator3ai pi = (PathIterator3ai) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 1, 0, 6, 0, 0);
		assertElement(pi, PathElementType.CURVE_TO, 2, -1, 0, 8, -2, 0, -2, -3, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createPoint(0, 0, 0)));
		assertTrue(this.shape.operator_and(createPoint(4, 3, 0)));
		assertTrue(this.shape.operator_and(createPoint(2, 2, 0)));
		assertTrue(this.shape.operator_and(createPoint(2, 1, 0)));
		assertTrue(this.shape.operator_and(createPoint(4, 2, 0)));
		assertFalse(this.shape.operator_and(createPoint(-1, -1, 0)));
		assertFalse(this.shape.operator_and(createPoint(6, 2, 0)));
		assertTrue(this.shape.operator_and(createPoint(3, -2, 0)));
		assertFalse(this.shape.operator_and(createPoint(2, -2, 0)));
	}

	@Override
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSphere(3, 0, 0, 1)));
		assertTrue(this.shape.operator_and(createRectangularPrism(-1, -1, 0, 1, 1, 0)));
	}

	@Override
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(1, 0, 0)));
		assertEpsilonEquals(7.071067812f, this.shape.operator_upTo(createPoint(-5, -5, 0)));
		assertEpsilonEquals(3f, this.shape.operator_upTo(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(7, 0, 0)));
	}

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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void isPolygon(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.isPolygon());

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
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
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

}
