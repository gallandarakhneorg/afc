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

package org.arakhne.afc.math.geometry.d3.afp;

import static org.arakhne.afc.math.MathConstants.SHAPE_INTERSECTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;

@SuppressWarnings("all")
public abstract class AbstractPath3afpTest<T extends Path3afp<?, T, ?, ?, ?, B>, B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>>
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

	protected BasicPathShadow3afp createShadow(int x1, int y1, int z1, int x2, int y2, int z2) {
		T path = (T) createPath();
		path.moveTo(x1, y1, z1);
		path.lineTo(x2, y2, z2);
		return new BasicPathShadow3afp(path);
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromCircle_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), -2, -2, 0, 2, null));
		assertEquals(-2, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2, -2, 0, 2, null));
		assertEquals(-2, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2.5, -1.5, 0, 2, null));
		assertEquals(0, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 10, 0, 0, 2, null));
		assertEquals(-2, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 4, 0, 0, 0.5, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2.5, 1, 0, 0.5, null));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromCircle_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), -2, -2, 0, 2, CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2, -2, 0, 2, CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2.5, -1.5, 0, 2, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 10, 0, 0, 2, CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 4, 0, 0, 0.5, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2.5, 1, 0, 0.5, CrossingComputationType.STANDARD));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromCircle_autoClose() {
		assertEquals(0, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), -2, -2, 0, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2, -2, 0, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2.5, -1.5, 0, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 10, 0, 0, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(-2, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 4, 0, 0, 0.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2.5, 1, 0, 0.5, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromCircle_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), -2, -2, 0, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2, -2, 0, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2.5, -1.5, 0, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 10, 0, 0, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 4, 0, 0, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSphere(0, this.shape.getPathIterator(), 2.5, 1, 0, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromPath_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 4, -3, 0), null));
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 5, -3, 0), null));
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 4, 1, 0), null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(5, 2, 0, 4, 1, 0), null));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromPath_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 4, -3, 0), CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 5, -3, 0), CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 4, 1, 0), CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(5, 2, 0, 4, 1, 0), CrossingComputationType.STANDARD));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromPath_autoClose() {
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 4, -3, 0), CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 5, -3, 0), CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 4, 1, 0), CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(5, 2, 0, 4, 1, 0), CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromPath_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 4, -3, 0), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 5, -3, 0), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 0, 4, 1, 0), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(5, 2, 0, 4, 1, 0), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromPoint_null() {
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, 0, null));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, 0, null));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, 0, null));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, 0, null));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, 0, null));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, 0, null));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, 0, null));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromPoint_standard() {
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, 0, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, 0, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, 0, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, 0, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, 0, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, 0, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, 0, CrossingComputationType.STANDARD));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromPoint_autoClose() {
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, 0, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromPoint_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromRect_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1, -2, 0, 3, -1, 0, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1.5, 1.5, 0, 3.5, 2.5, 0, null));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 7, 3, 0, 9, 4, 0, null));
		assertEquals(-1, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), -4, -0.5, 0, -2, 0.5, 0, null));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromRect_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1, -2, 0, 3, -1, 0, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1.5, 1.5, 0, 3.5, 2.5, 0, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 7, 3, 0, 9, 4, 0, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), -4, -0.5, 0, -2, 0.5, 0, CrossingComputationType.STANDARD));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromRect_autoClose() {
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1, -2, 0, 3, -1, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1.5, 1.5, 0, 3.5, 2.5, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 7, 3, 0, 9, 4, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), -4, -0.5, 0, -2, 0.5, 0, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromRect_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1, -2, 0, 3, -1, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1.5, 1.5, 0, 3.5, 2.5, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 7, 3, 0, 9, 4, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), -4, -0.5, 0, -2, 0.5, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromSegment_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -1, 0, 2, -3, 0, null));
		assertEquals(-1, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -6, 0, 2, -3, 0, null));
		assertEquals(-2, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 0, 2, -3, 0, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 0, 5, 3, 0, null));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromSegment_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -1, 0, 2, -3, 0, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -6, 0, 2, -3, 0, CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 0, 2, -3, 0, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 0, 5, 3, 0, CrossingComputationType.STANDARD));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromSegment_autoClose() {
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -1, 0, 2, -3, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -6, 0, 2, -3, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 0, 2, -3, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 0, 5, 3, 0, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	@Ignore
	public void staticComputeCrossingsFromSegment_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -1, 0, 2, -3, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -6, 0, 2, -3, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 0, 2, -3, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 0, 5, 3, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticComputeControlPointBoundingBox() {
		B box = createRectangularPrism(0, 0, 0, 0, 0, 0);
		Path3afp.computeControlPointBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(5, box.getMaxY());
	}

	@Test
	@Ignore
	public void staticComputeDrawableElementBoundingBox() {
		B box = createRectangularPrism(0, 0, 0, 0, 0, 0);
		Path3afp.computeDrawableElementBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
	}

	@Test
	@Ignore
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

	@Test
	@Ignore
	public void staticContainsRectangle() {
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), -5, 1, 0, 2, 1, 0));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, 6, 0, 2, 1, 0));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, -10, 0, 2, 1, 0));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 11, 1, 0, 2, 1, 0));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, 1, 0, 2, 1, 0));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 4, 3, 0, 2, 1, 0));
		this.shape.closePath();
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), -5, 1, 0, 2, 1, 0));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, 6, 0, 2, 1, 0));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, -10, 0, 2, 1, 0));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 11, 1, 0, 2, 1, 0));
		assertTrue(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, 0, 0, 2, 1, 0));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 4, 3, 0, 2, 1, 0));
	}

	@Test
	@Ignore
	public void staticGetClosestPointTo_open() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(2.56307, result.getX());
		assertEpsilonEquals(0.91027, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
	}

	@Test
	@Ignore
	public void staticGetClosestPointTo_close() {
		this.shape.closePath();

		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(3, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(2.55405, result.getX());
		assertEpsilonEquals(-1.82432, result.getY());
	}

	@Test
	@Ignore
	public void staticGetFarthestPointTo_open() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@Test
	@Ignore
	public void staticGetFarthestPointTo_close() {
		this.shape.closePath();

		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@Test
	@Ignore
	public void staticIntersectsPathIteratorRectangle_open() {
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 0, 2, 1, 0));
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 0, 2, 1, 0));
	}

	@Test
	@Ignore
	public void staticIntersectsPathIteratorRectangle_close() {
		this.shape.closePath();
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 0, 2, 1, 0));
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 0, 2, 1, 0));
	}

	@Test
	@Ignore
	public void staticComputeLength_open() {
		assertEpsilonEquals(14.71628, Path3afp.computeLength(this.shape.getPathIterator()));
	}

	@Test
	@Ignore
	public void staticComputeLength_close() {
		this.shape.closePath();
		assertEpsilonEquals(23.31861, Path3afp.computeLength(this.shape.getPathIterator()));
	}

	@Test
	@Ignore
	public void addIterator_open() {
		Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
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

	@Test
	@Ignore
	public void addIterator_closeAfter() {
		Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
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

	@Test
	@Ignore
	public void addIterator_closeBefore() {
		this.shape.closePath();

		Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
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

	@Test(expected = IllegalStateException.class)
	public void curveToDoubleDoubleDoubleDoubleDoubleDouble_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.curveTo(15, 145, 0, 50, 20, 0, 0, 0, 0);
	}
	
	@Test
	@Ignore
	public void curveToDoubleDoubleDoubleDoubleDoubleDouble() {
		this.shape.curveTo(123.456, 456.789, 0, 789.123, 159.753, 0, 456.852, 963.789, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CURVE_TO, 123.456, 456.789, 0, 789.123, 159.753, 0, 456.852, 963.789, 0);
		assertNoElement(pi);
	}
	
	@Test(expected = IllegalStateException.class)
	public void curveToPoint3DPoint3DPoint3D_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.curveTo(createPoint(15, 145, 0), createPoint(50, 20, 0), createPoint(0, 0, 0));
	}

	@Test
	@Ignore
	public void curveToPoint3DPoint3DPoint3D() {
		this.shape.curveTo(createPoint(123.456, 456.789, 0), createPoint(789.123, 159.753, 0), createPoint(456.852, 963.789, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.CURVE_TO, 123.456, 456.789, 0, 789.123, 159.753, 0, 456.852, 963.789, 0);
		assertNoElement(pi);
	}

	@Test
	public void getCoordAt() {
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

	@Test
	@Ignore
	public void getPathIteratorDouble_open() {
		PathIterator3afp pi = this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75, 0);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0, 0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125, 0);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75, 0);
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

	@Test
	@Ignore
	public void getPathIteratorDouble_close() {
		this.shape.closePath();
		PathIterator3afp pi = this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.75, 0);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.8125, 0);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 1.0, 0);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.3125, 0);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.75, 0);
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
		assertElement(pi, PathElementType.CLOSE, 0, 0);
		assertNoElement(pi);
	}

	@Test
	@Ignore
	public void getPathIteratorTransform3DDouble_null() {
		PathIterator3afp pi = this.shape.getPathIterator(null, MathConstants.SPLINE_APPROXIMATION_RATIO);
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

	@Test
	@Ignore
	public void getPathIteratorTransform3DDouble_identity() {
		PathIterator3afp pi = this.shape.getPathIterator(new Transform3D(), MathConstants.SPLINE_APPROXIMATION_RATIO);
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

	@Test
	@Ignore
	public void getPathIteratorTransform3DDouble_translation() {
		Transform3D transform = new Transform3D();
		transform.setTranslation(10, 10, 0);
		PathIterator3afp pi = this.shape.getPathIterator(transform, MathConstants.SPLINE_APPROXIMATION_RATIO);
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

	@Test
	@Ignore
	public void getLength() {
		assertEpsilonEquals(14.71628, this.shape.getLength());
	}

	@Test
	@Ignore
	public void getLengthSquared() {
		assertEpsilonEquals(216.56892, this.shape.getLengthSquared());
	}

	@Test(expected = IllegalStateException.class)
	public void lineToIntInt_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.lineTo(15, 145, 0);
	}

	@Test
	@Ignore
	public void lineToDoubleDouble() {
		this.shape.lineTo(123.456, 456.789, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@Test(expected = IllegalStateException.class)
	public void lineToPoint3D_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.lineTo(createPoint(15, 145, 0));
	}

	@Test
	@Ignore
	public void lineToPoint3D() {
		this.shape.lineTo(createPoint(123.456, 456.789, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@Test
	@Ignore
	public void moveToDoubleDouble() {
		this.shape.moveTo(123.456, 456.789, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@Test
	@Ignore
	public void moveToPoint3D() {
		this.shape.moveTo(createPoint(123.456, 456.789, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@Test(expected = IllegalStateException.class)
	public void quadToIntIntIntInt_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.quadTo(15, 145, 0, 50, 20, 0);
	}

	@Test
	@Ignore
	public void quadToDoubleDoubleDoubleDouble() {
		this.shape.quadTo(123.456, 456.789, 0, 789.123, 159.753, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.QUAD_TO, 123.456, 456.789, 0, 789.123, 159.753, 0);
		assertNoElement(pi);
	}
	
	@Test(expected = IllegalStateException.class)
	public void quadToPoint3DPoint3D_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.quadTo(createPoint(15, 145, 0), createPoint(50, 20, 0));
	}

	@Test
	@Ignore
	public void quadToPoint3DPoint3D() {
		this.shape.quadTo(createPoint(123.456, 456.789, 0), createPoint(789.123, 159.753, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertElement(pi, PathElementType.QUAD_TO, 123.456, 456.789, 0, 789.123, 159.753, 0);
		assertNoElement(pi);
	}

	@Test
	@Ignore
	public void removeDoubleDouble() {
		assertTrue(this.shape.remove(5, -1, 0));
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(4, 3, 0)));
		assertTrue(this.shape.remove(1, 1, 0));
		assertTrue(this.shape.size() == 3);
		assertFalse(this.shape.remove(35, 35, 0));
	}

	@Test
	@Ignore
	public void setLastPointDoubleDouble() {
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

	@Test
	@Ignore
	public void setLastPointPoint3D() {
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

	@Test
	@Ignore
	public void toCollection() {
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

	@Test
	public void transformTransform3D() {
		Point3D p1 = randomPoint3f();
		Point3D p2 = randomPoint3f();
		Point3D p3 = randomPoint3f();
		Point3D p4 = randomPoint3f();
		Point3D p5 = randomPoint3f();
		Point3D p6 = randomPoint3f();
		Point3D p7 = randomPoint3f();
		
		Path3afp path = createPath();
		path.moveTo(p1.getX(), p1.getY(), p1.getZ());
		path.lineTo(p2.getX(), p2.getY(), p2.getZ());
		path.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(),p4.getY(), p4.getZ());
		path.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
		path.closePath();
		
		Transform3D trans = new Transform3D(this.randomMatrix4f());
		
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

	@Override
	public void testClone() {
		Path3afp clone = this.shape.clone();
		PathIterator3afp pi = (PathIterator3afp) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 0, 4, 3, 0);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 0, 6, 5, 0, 7, -5, 0);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createPath()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(this.shape.clone()));
	}

	@Test
	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createPath().getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(this.shape.clone().getPathIterator()));
	}

	@Test
	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createPath().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 5, 10, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.clone().getPathIterator()));
	}

	@Test
	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createPath()));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape(this.shape.clone()));
	}

	@Override
	public void isEmpty() {
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Override
	public void clear() {
		this.shape.clear();
		PathIterator3afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@Override
	public void containsDoubleDouble() {
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

	@Override
	public void containsPoint3D() {
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

	@Override
	public void getClosestPointTo() {
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

	@Override
	public void getFarthestPointTo() {
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

	@Override
	public void getDistance() {
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

	@Override
	public void getDistanceSquared() {
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

	@Override
	public void getDistanceL1() {
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

	@Override
	public void getDistanceLinf() {
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

	@Override
	public void setIT() {
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

	@Override
	public void getPathIterator() {
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

	@Override
	public void getPathIteratorTransform3D() {
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

		transform.setTranslation(14, -5, 0);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 14, -5, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, -4, 0);
		assertElement(pi, PathElementType.QUAD_TO, 17, -5, 0, 18, -2, 0);
		assertElement(pi, PathElementType.CURVE_TO, 19, -6, 0, 20, 0, 0, 21, -10, 0);
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

		transform.setTranslation(14, -5, 0);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 14, -5, 0);
		assertElement(pi, PathElementType.LINE_TO, 15, -4, 0);
		assertElement(pi, PathElementType.QUAD_TO, 17, -5, 0, 18, -2, 0);
		assertElement(pi, PathElementType.CURVE_TO, 19, -6, 0, 20, 0, 0, 21, -10, 0);
		assertElement(pi, PathElementType.CLOSE, 14, -5, 0);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape() {
		Point3D p1 = randomPoint3f();
		Point3D p2 = randomPoint3f();
		Point3D p3 = randomPoint3f();
		Point3D p4 = randomPoint3f();
		Point3D p5 = randomPoint3f();
		Point3D p6 = randomPoint3f();
		Point3D p7 = randomPoint3f();
		
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

	@Override
	public void translateDoubleDouble() {
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

	@Override
	public void translateVector3D() {
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

	@Override
	public void toBoundingBox() {
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
		assertEpsilonEquals(3, box.getMaxZ());
	}

	@Override
	public void toBoundingBoxB() {
		B box = createRectangularPrism(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
		assertEpsilonEquals(3, box.getMaxZ());
	}

	@Override
	public void containsRectangularPrism3afp() {
		assertFalse(this.shape.contains(createRectangularPrism(-5, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(3, 6, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(3, -10, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(11, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(3, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(4, 3, 0, 2, 1, 0)));
		this.shape.closePath();
		assertFalse(this.shape.contains(createRectangularPrism(-5, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(3, 6, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(3, -10, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(11, 1, 0, 2, 1, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(3, 0, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(4, 3, 0, 2, 1, 0)));
	}

	@Override
	public void intersectsRectangularPrism3afp() {
		assertFalse(this.shape.intersects(createRectangularPrism(1, -2, 0, 2, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(1.5, 1.5, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(7, 3, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-4, -0.5, 0, 2, 1, 0)));
	}

	@Test
	@Ignore
	public void intersectsRectangularPrism3afp_close() {
		this.shape.closePath();
		assertTrue(this.shape.intersects(createRectangularPrism(1, -2, 0, 2, 1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(1.5, 1.5, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(7, 3, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(-4, -0.5, 0, 2, 1, 0)));
	}

	@Override
	public void intersectsSphere3afp() {
		assertFalse(this.shape.intersects(createSphere(-2, -2, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(2, -2, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(2.5, -1.5, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(10, 0, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(4, 0, 0, 0.5)));
		assertTrue(this.shape.intersects(createSphere(2.5, 1, 0, 0.5)));
	}

	@Test
	@Ignore
	public void intersectsSphere3afp_close() {
		this.shape.closePath();
		assertFalse(this.shape.intersects(createSphere(-2, -2, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(2, -2, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(2.5, -1.5, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(10, 0, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(4, 0, 0, 0.5)));
		assertTrue(this.shape.intersects(createSphere(2.5, 1, 0, 0.5)));
	}

	@Override
	public void intersectsSegment3afp() {
		assertFalse(this.shape.intersects(createSegment(1, -1, 0, 2, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 0, 2, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(4, 0, 0, 2, -3, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 5, 3, 0)));
	}

	@Test
	@Ignore
	public void intersectsSegment3afp_close() {
		this.shape.closePath();
		assertFalse(this.shape.intersects(createSegment(1, -1, 0, 2, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 0, 2, -3, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 2, -3, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 5, 3, 0)));
	}

	@Override
	public void intersectsPath3afp() {
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0)));
	}

	@Test
	@Ignore
	public void intersectsPath3afp_close() {
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0)));
	}

	@Override
	public void intersectsPathIterator3afp() {
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0).getPathIterator()));
	}

	@Test
	@Ignore
	public void intersectsPathIterator3afp_close() {
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0).getPathIterator()));
	}

	@Override
	public void intersectsShape3D() {
		assertTrue(this.shape.intersects((Shape3D) createSegment(4, 0, 0, 5, 3, 0)));
		assertTrue(this.shape.intersects((Shape3D)createRectangularPrism(1.5, 1.5, 0, 2, 1, 0)));
	}

	@Override
	public void operator_addVector3D() {
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

	@Override
	public void operator_plusVector3D() {
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

	@Override
	public void operator_removeVector3D() {
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

	@Override
	public void operator_minusVector3D() {
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

	@Override
	public void operator_multiplyTransform3D() {
		Point3D p1 = randomPoint3f();
		Point3D p2 = randomPoint3f();
		Point3D p3 = randomPoint3f();
		Point3D p4 = randomPoint3f();
		Point3D p5 = randomPoint3f();
		Point3D p6 = randomPoint3f();
		Point3D p7 = randomPoint3f();
		
		Path3afp path = createPath();
		path.moveTo(p1.getX(), p1.getY(), p1.getZ());
		path.lineTo(p2.getX(), p2.getY(), p2.getZ());
		path.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
		path.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
		path.closePath();

		Transform3D trans = new Transform3D(randomMatrix4f());
		Path3afp transformedShape = (Path3afp) path.operator_multiply(trans);
		path.transform(trans);		
	
		assertTrue(path.equalsToShape(transformedShape));
	}

	@Override
	public void operator_andPoint3D() {
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

	@Override
	public void operator_andShape3D() {
		assertTrue(this.shape.operator_and(createSegment(4, 0, 0, 5, 3, 0)));
		assertTrue(this.shape.operator_and(createRectangularPrism(1.5, 1.5, 0, 2, 1, 0)));
	}

	@Override
	public void operator_upToPoint3D() {
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
	public void isPolygon() {
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
	
	@Test
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
	
	@Test
	public void getCurrentX() {
		assertEpsilonEquals(7, this.shape.getCurrentX());
		this.shape.lineTo(154, 485, 0);
		assertEpsilonEquals(154, this.shape.getCurrentX());
	}
	
	@Test
	public void getCurrentY() {
	    assertEpsilonEquals(-5, this.shape.getCurrentY());
	    this.shape.lineTo(154, 485, 0);
	    assertEpsilonEquals(485, this.shape.getCurrentY());
	}

	@Test
	public void getCurrentZ() {
		assertEpsilonEquals(0, this.shape.getCurrentZ());
		this.shape.lineTo(154, 485, 10);
		assertEpsilonEquals(10, this.shape.getCurrentZ());
	}

	@Test
	public void getCurrentPoint() {
		assertFpPointEquals(7, -5, 0, this.shape.getCurrentPoint());
		this.shape.lineTo(154, 485, 0);
		assertFpPointEquals(154, 485, 0, this.shape.getCurrentPoint());
	}

}
