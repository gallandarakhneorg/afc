
/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.ai;

import static org.arakhne.afc.math.MathConstants.SHAPE_INTERSECTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Path2D.ArcType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;

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
	
	@Test
	@Override
	public void testClone() {
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

	@Test
	@Override
	public void equalsObject() {
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

	@Test
	@Override
	public void equalsObject_withPathIterator() {
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

	@Test
	@Override
	public void equalsToShape() {
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

	@Test
	@Override
	public void equalsToPathIterator() {
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

	@Test
	@Override
	public void isEmpty() {
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

	@Test
	@Override
	public void clear() {
		this.shape.clear();
		assertEquals(0, this.shape.size());
	}

	@Test
	@Override
	public void getPointIterator() {
		Point2D p;
		Iterator<? extends Point2D> it = this.shape.getPointIterator();
		
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
	public void staticComputeCrossingsFromPoint() {
		assertEquals(0, Path2ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), -2, 1, null));
		assertEquals(0, Path2ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -3, null));
		assertEquals(SHAPE_INTERSECTS,
				Path2ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 4, 3, null));
		assertEquals(-2, Path2ai.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, null));
	}

	@Test
	public void staticComputeCrossingsFromRect() {
		assertEquals(0, Path2ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				-2, 1, -1, 2,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				0, 1, 3, 6,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				3, -1, 8, 0,
				null));
		assertEquals(-2, Path2ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				3, -1, 4, 0,
				null));
		assertEquals(-2, Path2ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				3, -1, 5, 0,
				null));
		assertEquals(0, Path2ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				0, -4, 3, -3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				0, -4, 4, -3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromRect(
				0,
				this.shape.getPathIterator(),
				0, -4, 3, -2,
				null));
	}

	@Test
	public void staticComputeCrossingsFromSegment() {
		assertEquals(0, Path2ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				-2, 1, -1, 2,
				null));
		assertEquals(0, Path2ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				0, 1, 3, 6,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				3, -1, 8, 0,
				null));
		assertEquals(-2, Path2ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				3, -1, 4, 0,
				null));
		assertEquals(-2, Path2ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				3, -1, 5, 0,
				null));
		assertEquals(0, Path2ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				0, -4, 3, -3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				0, -4, 4, -3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromSegment(
				0,
				this.shape.getPathIterator(),
				0, -4, 3, -2,
				null));
	}
	
	@Test
	public void staticComputeCrossingsFromCircle() {
		assertEquals(0, Path2ai.computeCrossingsFromCircle(
				0,
				this.shape.getPathIterator(),
				-2, 1, 1,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromCircle(
				0,
				this.shape.getPathIterator(),
				-2, 1, 2,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromCircle(
				0,
				this.shape.getPathIterator(),
				0, 1, 3,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromCircle(
				0,
				this.shape.getPathIterator(),
				3, -1, 8,
				null));
		assertEquals(SHAPE_INTERSECTS, Path2ai.computeCrossingsFromCircle(
				0,
				this.shape.getPathIterator(),
				3, -1, 1,
				null));
		assertEquals(-2, Path2ai.computeCrossingsFromCircle(
				0,
				this.shape.getPathIterator(),
				4, -1, 0,
				null));
		assertEquals(0, Path2ai.computeCrossingsFromCircle(
				0,
				this.shape.getPathIterator(),
				20, 0, 2,
				null));
	}

	@Test
	public void staticComputeCrossingsFromPath_notCloseable_noOnlyIntersectWhenOpen() {
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
		assertEquals(1, Path2ai.computeCrossingsFromPath(
		        0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
				CrossingComputationType.STANDARD));
	}
	
	@Test
	public void staticComputeCrossingsFromPath_closeable_noOnlyIntersectWhenOpen() {
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
				CrossingComputationType.AUTO_CLOSE));
	}
		
	@Test
	public void staticComputeCrossingsFromPath_noCloseable_onlyIntersectWhenOpen() {
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
		assertEquals(0, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
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
		assertEquals(MathConstants.SHAPE_INTERSECTS, Path2ai.computeCrossingsFromPath(
                0,
				(PathIterator2ai) path2.getPathIterator(),
				new PathShadow2ai(path1),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticContainsPathIterator2iIntInt() {
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 0, 0));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 4, 3));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 2, 2));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 2, 1));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 4, 2));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 4, 3));
		assertFalse(Path2ai.contains(this.shape.getPathIterator(), -1, -1));
		assertFalse(Path2ai.contains(this.shape.getPathIterator(), 6, 2));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 3, -2));
		assertFalse(Path2ai.contains(this.shape.getPathIterator(), 2, -2));
	}

	@Test
	public void staticIntersectsPathIterator2iIntIntIntInt() {
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 0, 0, 1, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 4, 3, 1, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 2, 2, 1, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 2, 1, 1, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 3, 0, 1, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), -1, -1, 1, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 4, -3, 1, 1));
		assertFalse(Path2ai.intersects(this.shape.getPathIterator(), -3, 4, 1, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 6, -5, 1, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 4, 0, 1, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 5, 0, 1, 1));
		assertFalse(Path2ai.intersects(this.shape.getPathIterator(), 0, -3, 1, 1));
		assertFalse(Path2ai.intersects(this.shape.getPathIterator(), 0, -3, 2, 1));
		assertTrue(Path2ai.intersects(this.shape.getPathIterator(), 0, -3, 3, 1));
	}
	
	@Test
	@Override
	public void getClosestPointTo() {
		Point2D p;
		
		p = this.shape.getClosestPointTo(createPoint(0, 0));
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		// remember: path is closed
		p = this.shape.getClosestPointTo(createPoint(-1, -4));
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(4, 0));
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = this.shape.getClosestPointTo(createPoint(4, 2));
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		p = this.shape.getClosestPointTo(createPoint(4, -1));
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		p = this.shape.getClosestPointTo(createPoint(2, -3));
		assertEquals(p.toString(), 3, p.ix());
		assertEquals(p.toString(), -2, p.iy());
	}
	
	@Test
	public void getCurrentX() {
		assertEquals(7, this.shape.getCurrentX());
		this.shape.lineTo(148, 752);
		assertEquals(148, this.shape.getCurrentX());
	}
	
	@Test
	public void getCurrentY() {
		assertEquals(-5, this.shape.getCurrentY());
		this.shape.lineTo(148, 752);
		assertEquals(752, this.shape.getCurrentY());
	}

	@Test
	public void getCurrentPoint() {
		assertIntPointEquals(7, -5, this.shape.getCurrentPoint());
		this.shape.lineTo(148, 752);
		assertIntPointEquals(148, 752, this.shape.getCurrentPoint());
	}

	@Test
	@Override
	public void getFarthestPointTo() {
		Point2D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(-1, -4)); // remember: path is closed
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 3, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(4, 0));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(4, 2));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(4, -1));
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());
	}

	@Test
	@Override
	public void getDistance() {
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(1, 0)));
		assertEpsilonEquals(7.071067812f, this.shape.getDistance(createPoint(-5, -5)));
		assertEpsilonEquals(3f, this.shape.getDistance(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(7, 0)));
	}

	@Test
	@Override
	public void getDistanceSquared() {
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(1, 0)));
		assertEpsilonEquals(50f, this.shape.getDistanceSquared(createPoint(-5, -5)));
		assertEpsilonEquals(9f, this.shape.getDistanceSquared(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(7, 0)));
	}

	@Test
	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(1, 0)));
		assertEpsilonEquals(10f, this.shape.getDistanceL1(createPoint(-5, -5)));
		assertEpsilonEquals(3f, this.shape.getDistanceL1(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(7, 0)));
	}

	@Test
	@Override
	public void getDistanceLinf() {
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(1, 0)));
		assertEpsilonEquals(5f, this.shape.getDistanceLinf(createPoint(-5, -5)));
		assertEpsilonEquals(3f, this.shape.getDistanceLinf(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(7, 0)));
	}

	@Test
	@Override
	public void translateIntInt() {
		this.shape.translate(3, 4);
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void translateVector2D() {
		this.shape.translate(createVector(3, 4));
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 3, 4);
		assertElement(pi, PathElementType.LINE_TO, 5, 6);
		assertElement(pi, PathElementType.QUAD_TO, 6, 4, 7, 7);
		assertElement(pi, PathElementType.CURVE_TO, 8, 3, 9, 9, 10, -1);
		assertElement(pi, PathElementType.CLOSE, 3, 4);
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
	public void addIterator() {
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

	@Test
	@Override
	public void getPathIterator() {
		PathIterator2ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 2, 2);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void getPathIteratorTransform2D() {
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
	
	@Test
	public void getPathIteratorDouble() {
		PathIterator2ai pi = this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
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

	@Test
	public void transformTransform2D_translation() {
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

	@Test
	public void createTransformedShape_translation() {
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

	@Test
	public void transformTransform2D_rotation() {
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

	@Test
	public void createTransformedShape_rotation() {
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

	@Test
	public void transformTransform2D_translationRotation() {
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

	@Test
	@Override
	public void createTransformedShape() {
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

	@Test
	@Override
	public void containsIntInt() {
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

	@Test
	@Override
	public void containsRectangle2ai() {
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

    @Test
    @Override
    public void containsShape2D() {
        assertFalse(this.shape.contains(createCircle(0, 0, 1)));
        assertFalse(this.shape.contains(createCircle(4, 3, 1)));
        assertFalse(this.shape.contains(createCircle(2, 2, 1)));
        assertFalse(this.shape.contains(createCircle(2, 1, 1)));
        assertFalse(this.shape.contains(createCircle(3, 0, 1)));
        assertTrue(this.shape.contains(createCircle(3, 0, 2)));
        assertFalse(this.shape.contains(createCircle(-1, -1, 1)));
        assertFalse(this.shape.contains(createCircle(4, -3, 1)));
        assertTrue(this.shape.contains(createCircle(5, -3, 1)));
        assertFalse(this.shape.contains(createCircle(-3, 4, 1)));
        assertFalse(this.shape.contains(createCircle(6, -5, 1)));
        assertTrue(this.shape.contains(createCircle(4, 0, 1)));
        assertFalse(this.shape.contains(createCircle(5, 0, 1)));
        assertFalse(this.shape.contains(createCircle(5, 0, 0)));
        assertFalse(this.shape.contains(createCircle(5, 0, 2)));
        assertFalse(this.shape.contains(createCircle(6, 0, 1)));
    }

    @Test
	@Override
	public void intersectsRectangle2ai() {
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

	@Test
	@Override
	public void intersectsCircle2ai() {
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

	@Test
	@Override
	public void intersectsSegment2ai() {
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

	@Test
	@Override
	public void intersectsPath2ai() {
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
	
	@Test
	@Override
	public void toBoundingBox() {
		B bb = this.shape.toBoundingBox();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(3, bb.getMaxY());
	}

	@Test
	@Override
	public void toBoundingBoxB() {
		B bb = (B) createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(bb);
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(3, bb.getMaxY());
	}

	@Test
	public void toBoundingBoxWithCtrlPoints() {
		B bb = this.shape.toBoundingBoxWithCtrlPoints();
		assertEquals(0, bb.getMinX());
		assertEquals(-5, bb.getMinY());
		assertEquals(7, bb.getMaxX());
		assertEquals(5, bb.getMaxY());
	}

	@Test
	public void removeLast() {
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

	@Test
	public void setLastPointIntInt() {
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
	
	@Test
	public void removeIntInt() {
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

	@Test
	@Override
	public void containsPoint2D() {
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

	@Test
	public void staticContainsPathIteratorIntInt() {
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 0, 0));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 4, 3));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 2, 2));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 2, 1));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 4, 2));
		assertFalse(Path2ai.contains(this.shape.getPathIterator(), -1, -1));
		assertFalse(Path2ai.contains(this.shape.getPathIterator(), 6, 2));
		assertTrue(Path2ai.contains(this.shape.getPathIterator(), 3, -2));
		assertFalse(Path2ai.contains(this.shape.getPathIterator(), 2, -2));
	}

	@Test
	public void staticComputeDrawableElementBoundingBox() {
		B box = (B) createRectangle(0, 0, 0, 0);
		assertTrue(Path2ai.computeDrawableElementBoundingBox(this.shape.getPathIterator(), box));
		assertEquals(0, box.getMinX());
		assertEquals(-5, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(3, box.getMaxY());
	}

	@Test
	public void staticGetClosestPointTo() {
		Point2D p;
		
		p = createPoint(0, 0);
		Path2ai.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 0, 0, p);
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = createPoint(0, 0);
		// remember: path is closed
		Path2ai.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -1, -4, p);
		assertEquals(p.toString(), 0, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = createPoint(0, 0);
		Path2ai.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 4, 0, p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 0, p.iy());

		p = createPoint(0, 0);
		Path2ai.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 4, 2, p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 2, p.iy());

		p = createPoint(0, 0);
		Path2ai.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 4, -1, p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), -1, p.iy());

		p = createPoint(0, 0);
		Path2ai.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 2, -3, p);
		assertEquals(p.toString(), 3, p.ix());
		assertEquals(p.toString(), -2, p.iy());
	}

	@Test
	public void staticGetFarthestPointTo() {
		Point2D p;
		
		p = createPoint(0, 0);
		Path2ai.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 0, 0, p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = createPoint(0, 0);
		// remember: path is closed
		Path2ai.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -1, -4, p);
		assertEquals(p.toString(), 4, p.ix());
		assertEquals(p.toString(), 3, p.iy());

		p = createPoint(0, 0);
		Path2ai.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 4, 0, p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = createPoint(0, 0);
		Path2ai.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 4, 2, p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());

		p = createPoint(0, 0);
		Path2ai.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 4, -1, p);
		assertEquals(p.toString(), 7, p.ix());
		assertEquals(p.toString(), -5, p.iy());
	}

	@Test
	public void moveToIntInt() {
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

	@Test
	public void moveToPoint2D() {
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

	@Test(expected = IllegalStateException.class)
	public void lineToIntInt_noMoveTo() {
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.lineTo(15, 145);
	}

	@Test
	public void lineToIntInt_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void lineToPoint2D_noMoveTo() {
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.lineTo(createPoint(15, 145));
	}

	@Test
	public void lineToPoint2D_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void quadToIntIntIntInt_noMoveTo() {
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.quadTo(15, 145, 50, 20);
	}

	@Test
	public void quadToIntIntIntInt_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void quadToPoint2DPoint2D_noMoveTo() {
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));
	}

	@Test
	public void quadToPoint2DPoint2D_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void curveToIntIntIntIntIntInt_noMoveTo() {
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.curveTo(15, 145, 50, 20, 0, 0);
	}

	@Test
	public void curveToIntIntIntIntIntInt_moveTo() {
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

	@Test(expected = IllegalStateException.class)
	public void curveToPoint2DPoint2DPoint2D_noMoveTo() {
		Path2ai<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));
	}

	@Test
	public void curveToPoint2DPoint2DPoint2Dt_moveTo() {
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

	@Test
	public void lengthSquared() {
		assertEpsilonEquals(98, this.shape.getLengthSquared());
	}

	@Test
	public void length() {
		assertEpsilonEquals(9.899494937, this.shape.getLength());
	}

	@Test
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
	public void setLastPointPoint2D() {
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

	@Test
	public void toCollection() {
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

	@Test
	@Override
	public void setIT() {
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
	public void intersectsPathIterator2ai() {
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
	public void intersectsShape2D() {
		assertTrue(this.shape.intersects((Shape2D) createCircle(3, 0, 1)));
		assertTrue(this.shape.intersects((Shape2D) createRectangle(-1, -1, 1, 1)));
	}

	@Override
	public void operator_addVector2D() {
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
	public void operator_plusVector2D() {
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
	public void operator_removeVector2D() {
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
	public void operator_minusVector2D() {
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
	public void operator_multiplyTransform2D() {
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
	public void operator_andPoint2D() {
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
	public void operator_andShape2D() {
		assertTrue(this.shape.operator_and(createCircle(3, 0, 1)));
		assertTrue(this.shape.operator_and(createRectangle(-1, -1, 1, 1)));
	}

	@Override
	public void operator_upToPoint2D() {
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(1, 0)));
		assertEpsilonEquals(7.071067812f, this.shape.operator_upTo(createPoint(-5, -5)));
		assertEpsilonEquals(3f, this.shape.operator_upTo(createPoint(4, 6)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(7, 0)));
	}

	@Test
	public void isCurved() {
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
	
	@Test
	public void isMultiParts() {
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
	
	@Test
	public void isPolygon() {
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
	
	@Test
	public void isPolyline() {
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

	@Test
	public void getDistanceSquaredRectangle2ai() {
		assert(false);
	}

	@Test
	public void getDistanceSquaredCircle2ai() {
		assert(false);
	}

	@Test
	public void getDistanceSquaredSegment2ai() {
		assert(false);
	}
	
	@Test
	public void getDistanceSquaredPath2ai() {
		assert(false);
	}

	@Test
	public void getClosestPointToRectangle2ai() {
		assert(false);
	}

	@Test
	public void getClosestPointToCircle2ai() {
		assert(false);
	}

	@Test
	public void getClosestPointToSegment2ai() {
		assert(false);
	}
	
	@Test
	public void getClosestPointToMultiShape2ai() {
		assert(false);
	}
		
	@Test
	public void getClosestPointToPath2ai() {
		assert(false);
	}
	
	public void generateShapeBitmap() throws IOException {
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.ARC_ONLY);
		File filename = generateTestPicture(this.shape);
		System.out.println("Filename: " + filename);
	}

	@Test
	public void arcToIntIntIntIntDoubleDoubleArcType_01_arcOnly() {
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

	@Test
	public void arcToIntIntIntIntDoubleDoubleArcType_01_lineTo() {
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

	@Test
	public void arcToIntIntIntIntDoubleDoubleArcType_01_moveTo() {
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

	@Test
	public void arcToIntIntIntIntDoubleDoubleArcType_0251_arcOnly() {
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

	@Test
	public void arcToIntIntIntIntDoubleDoubleArcType_0251_lineTo() {
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

	@Test
	public void arcToIntIntIntIntDoubleDoubleArcType_0251_moveTo() {
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

	@Test
	public void arcToPoint2DPoint2DDoubleDoubleArcType_01_arcOnly() {
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

	@Test
	public void arcToIntIntIntInt() {
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

	@Test
	public void arcToPoint2DPoint2D() {
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

}
