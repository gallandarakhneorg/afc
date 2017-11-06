/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.ai;

import static org.arakhne.afc.math.geometry.GeomConstants.SHAPE_INTERSECTS;
import static org.arakhne.afc.math.geometry.GeomConstants.SPLINE_APPROXIMATION_RATIO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;

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
	public void testClone() {
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
	public void equalsObject() {
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
	public void equalsObject_withPathIterator() {
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
	public void equalsToShape() {
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
	public void equalsToPathIterator() {
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
	public void isEmpty() {
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

	@Test
	@Override
	public void clear() {
		this.shape.clear();
		assertEquals(0, this.shape.size());
	}

	@Override
	public void getPointIterator() {
		Point3D p;
		Iterator<? extends Point3D> it = this.shape.getPointIterator();
		
		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 1, p.ix());
		assertEquals(p.toString(), 1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 2, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 3, p.ix());
		assertEquals(p.toString(), 1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 3, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 5, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 5, p.ix());
		assertEquals(p.toString(), 1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 6, p.ix());
		assertEquals(p.toString(), 1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 6, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 6, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -3, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -4, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 6, p.ix());
		assertEquals(p.toString(), -4, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 5, p.ix());
		assertEquals(p.toString(), -4, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), -3, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 3, p.ix());
		assertEquals(p.toString(), -2, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 2, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 1, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		assertTrue(it.hasNext());
		p = it.next();
		assertNotNull(p);
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		assertFalse(it.hasNext());
	}
	
	@Test
	@Ignore
	public void staticComputeCrossingsFromPoint() {
		assertEquals(0, Path3ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), -2, 1, 0, null));
		assertEquals(0, Path3ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -3, 0, null));
		assertEquals(SHAPE_INTERSECTS,
				Path3ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 4, 3, 0, null));
		assertEquals(-2, Path3ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, null));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromRect() {
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

	@Test
	@Ignore
	public void staticComputeCrossingsFromSegment() {
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
	
	@Test
	@Ignore
	public void staticcomputeCrossingsFromSphere() {
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

	@Test
	@Ignore
	public void staticComputeCrossingsFromPath_notCloseable_noOnlyIntersectWhenOpen() {
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
	
	@Test
	@Ignore
	public void staticComputeCrossingsFromPath_closeable_noOnlyIntersectWhenOpen() {
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
		
	@Test
	@Ignore
	public void staticComputeCrossingsFromPath_noCloseable_onlyIntersectWhenOpen() {
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

	@Test
	@Ignore
	public void staticContainsPathIterator2iIntInt() {
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

	@Test
	@Ignore
	public void staticIntersectsPathIterator2iIntIntIntInt() {
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
	public void getClosestPointTo() {
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(0, 0, 0));
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		// remember: path is closed
		p = this.shape.getClosestPointTo(createPoint(-1, -4, 0));
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(4, 0, 0));
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(4, 2, 0));
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		p = this.shape.getClosestPointTo(createPoint(4, -1, 0));
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		p = this.shape.getClosestPointTo(createPoint(2, -3, 0));
		assertEquals(p.toString(), 3, p.ix());
		assertEquals(p.toString(), -2, p.iy());
	}
	
	@Test
	public void getCurrentX() {
		assertEquals(7, this.shape.getCurrentX());
		this.shape.lineTo(148, 752, 0);
		assertEquals(148, this.shape.getCurrentX());
	}
	
	@Test
	@Ignore
	public void getCurrentY() {
	    assertEquals(-5, this.shape.getCurrentY());
	    this.shape.lineTo(148, 752, 0);
	    assertEquals(752, this.shape.getCurrentY());
	}
	
	@Test
	@Ignore
	public void getCurrentZ() {
		assertEquals(-5, this.shape.getCurrentZ());
		this.shape.lineTo(148, 752, 0);
		assertEquals(752, this.shape.getCurrentZ());
	}

	@Test
	public void getCurrentPoint() {
		assertIntPointEquals(7, -5, 0, this.shape.getCurrentPoint());
		this.shape.lineTo(148, 752, 0);
		assertIntPointEquals(148, 752, 0, this.shape.getCurrentPoint());
	}

	@Override
	public void getFarthestPointTo() {
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0, 0));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(-1, -4, 0)); // remember: path is closed
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 3, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(4, 0, 0));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(4, 2, 0));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(4, -1, 0));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());
	}

	@Override
	public void getDistance() {
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(1, 0, 0)));
		assertEpsilonEquals(7.071067812f, this.shape.getDistance(createPoint(-5, -5, 0)));
		assertEpsilonEquals(3f, this.shape.getDistance(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(7, 0, 0)));
	}

	@Override
	public void getDistanceSquared() {
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(1, 0, 0)));
		assertEpsilonEquals(50f, this.shape.getDistanceSquared(createPoint(-5, -5, 0)));
		assertEpsilonEquals(9f, this.shape.getDistanceSquared(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(7, 0, 0)));
	}

	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(1, 0, 0)));
		assertEpsilonEquals(10f, this.shape.getDistanceL1(createPoint(-5, -5, 0)));
		assertEpsilonEquals(3f, this.shape.getDistanceL1(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(7, 0, 0)));
	}

	@Override
	public void getDistanceLinf() {
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(1, 0, 0)));
		assertEpsilonEquals(5f, this.shape.getDistanceLinf(createPoint(-5, -5, 0)));
		assertEpsilonEquals(3f, this.shape.getDistanceLinf(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(7, 0, 0)));
	}

	@Override
	public void translateIntInt() {
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
	public void translateVector3D() {
		this.shape.translate(createVector(3, 4, 0));
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4, 0);
		assertElement(pi, PathElementType.LINE_TO, 5, 6, 0);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 0, 7, 7, 0);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 0, 9, 9, 0, 10, -1, 0);
		assertElement(pi, PathElementType.CLOSE, 3, 4, 0);
		assertNoElement(pi);
	}

	@Test
	public void setWindingRule() {
		assertEquals(PathWindingRule.NON_ZERO, this.shape.getWindingRule());
		for(PathWindingRule rule : PathWindingRule.values()) {
			this.shape.setWindingRule(rule);
			assertEquals(rule, this.shape.getWindingRule());
		}
	}

	@Test
	@Ignore
	public void addIterator() {
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
	public void getPathIterator() {
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform3D() {
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
	
	@Test
	@Ignore
	public void getPathIteratorDouble() {
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

	@Test
	@Ignore
	public void transformTransform3D_translation() {
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

	@Test
	@Ignore
	public void createTransformedShape_translation() {
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

	@Test
	@Ignore
	public void transformTransform3D_rotation() {
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

	@Test
	@Ignore
	public void createTransformedShape_rotation() {
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

	@Test
	@Ignore
	public void transformTransform3D_translationRotation() {
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
	public void createTransformedShape() {
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
	public void containsIntInt() {
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
	public void containsRectangularPrism3ai() {
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
	public void intersectsRectangularPrism3ai() {
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
	public void intersectsSphere3ai() {
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
	public void intersectsSegment3ai() {
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
	public void intersectsPath3ai() {
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
	public void toBoundingBox() {
		B bb = this.shape.toBoundingBox();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(3, bb.getMaxY());
	}

	@Override
	public void toBoundingBoxB() {
		B bb = createRectangularPrism(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(3, bb.getMaxY());
	}

	@Test
	@Ignore
	public void toBoundingBoxWithCtrlPoints() {
		B bb = this.shape.toBoundingBoxWithCtrlPoints();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(5, bb.getMaxY());
	}

	@Test
	@Ignore
	public void removeLast() {
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

	@Test
	@Ignore
	public void setLastPointIntInt() {
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
	
	@Test
	@Ignore
	public void removeIntInt() {
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
	public void containsPoint3D() {
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

	@Test
	@Ignore
	public void staticContainsPathIteratorIntInt() {
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

	@Test
	@Ignore
	public void staticComputeDrawableElementBoundingBox() {
		B box = createRectangularPrism(0, 0, 0, 0, 0, 0);
		assertTrue(Path3ai.computeDrawableElementBoundingBox(this.shape.getPathIterator(), box));
		assertEquals(0, box.getMinX());
		assertEquals(-5, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(3, box.getMaxY());
	}

	@Test
	@Ignore
	public void staticGetClosestPointTo() {
		Point3D p;
		
		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 0, 0, 0, p);
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = createPoint(0, 0, 0);
		// remember: path is closed
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -1, -4, 0, p);
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, 0, 0, p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, 2, 0, p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, -1, 0, p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		p = createPoint(0, 0, 0);
		Path3ai.getClosestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 2, -3, 0, p);
		assertEquals(p.toString(), 3, p.ix());
		assertEquals(p.toString(), -2, p.iy());
	}

	@Test
	@Ignore
	public void staticGetFarthestPointTo() {
		Point3D p;
		
		p = createPoint(0, 0, 0);
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 0, 0, 0, p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = createPoint(0, 0, 0);
		// remember: path is closed
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -1, -4, 0, p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 3, p.iy());

		p = createPoint(0, 0, 0);
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, 0, 0, p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = createPoint(0, 0, 0);
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, 2, 0, p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = createPoint(0, 0, 0);
		Path3ai.getFarthestPointTo(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 4, -1, 0, p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());
	}

	@Test
	public void moveToIntInt() {
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

	@Test
	public void moveToPoint3D() {
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

	@Test(expected = IllegalStateException.class)
	public void lineToIntInt_noMoveTo() {
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.lineTo(15, 145, 0);
	}

	@Test
	@Ignore
	public void lineToIntInt_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void lineToPoint3D_noMoveTo() {
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.lineTo(createPoint(15, 145, 0));
	}

	@Test
	@Ignore
	public void lineToPoint3D_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void quadToIntIntIntInt_noMoveTo() {
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.quadTo(15, 145, 0, 50, 20, 0);
	}

	@Test
	@Ignore
	public void quadToIntIntIntInt_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void quadToPoint3DPoint3D_noMoveTo() {
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.quadTo(createPoint(15, 145, 0), createPoint(50, 20, 0));
	}

	@Test
	@Ignore
	public void quadToPoint3DPoint3D_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void curveToIntIntIntIntIntInt_noMoveTo() {
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.curveTo(15, 145, 0, 50, 20, 0, 0, 0, 0);
	}

	@Test
	@Ignore
	public void curveToIntIntIntIntIntInt_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void curveToPoint3DPoint3DPoint3D_noMoveTo() {
		Path3ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.curveTo(createPoint(15, 145, 0), createPoint(50, 20, 0), createPoint(0, 0, 0));
	}

	@Test
	@Ignore
	public void curveToPoint3DPoint3DPoint3Dt_moveTo() {
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

	@Test
	@Ignore
	public void lengthSquared() {
		assertEpsilonEquals(98, this.shape.getLengthSquared());
	}

	@Test
	@Ignore
	public void length() {
		assertEpsilonEquals(9.899494937, this.shape.getLength());
	}

	@Test
	@Ignore
	public void getCoordAt() {
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

	@Test
	@Ignore
	public void setLastPointPoint3D() {
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

	@Test
	public void toCollection() {
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
	public void setIT() {
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
	public void intersectsPathIterator3ai() {
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
	public void intersectsShape3D() {
		assertTrue(this.shape.intersects((Shape3D) createSphere(3, 0, 0, 1)));
		assertTrue(this.shape.intersects((Shape3D) createRectangularPrism(-1, -1, 0, 1, 1, 0)));
	}

	@Override
	public void operator_addVector3D() {
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
	public void operator_plusVector3D() {
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
	public void operator_removeVector3D() {
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
	public void operator_minusVector3D() {
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
	public void operator_multiplyTransform3D() {
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
	public void operator_andPoint3D() {
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
	public void operator_andShape3D() {
		assertTrue(this.shape.operator_and(createSphere(3, 0, 0, 1)));
		assertTrue(this.shape.operator_and(createRectangularPrism(-1, -1, 0, 1, 1, 0)));
	}

	@Override
	public void operator_upToPoint3D() {
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(0, 0, 0)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(1, 0, 0)));
		assertEpsilonEquals(7.071067812f, this.shape.operator_upTo(createPoint(-5, -5, 0)));
		assertEpsilonEquals(3f, this.shape.operator_upTo(createPoint(4, 6, 0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(7, 0, 0)));
	}

	@Test
	public void isCurved() {
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
	
	@Test
	@Ignore
	public void isMultiParts() {
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
	
	@Test
	@Ignore
	public void isPolygon() {
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
	
	@Test
	@Ignore
	public void isPolyline() {
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
