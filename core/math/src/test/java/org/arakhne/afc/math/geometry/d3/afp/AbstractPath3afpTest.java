/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.afp;

import static org.arakhne.afc.math.MathConstants.SHAPE_INTERSECTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp.CrossingComputationType;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractPath3afpTest<T extends Path3afp<?, T, ?, ?, ?, B>, B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>>
extends AbstractShape3afpTest<T, B> {

	@Override
	protected T createShape() {
		T path = (T) createPath();
		path.moveTo(0, 0);
		path.lineTo(1, 1);
		path.quadTo(3, 0, 4, 3);
		path.curveTo(5, -1, 6, 5, 7, -5);
		return path;
	}

	protected PathShadow3afp<B> createShadow(int x1, int y1, int z1, int x2, int y2, int z2) {
		T path = (T) createPath();
		path.moveTo(x1, y1, z1);
		path.lineTo(x2, y2, z2);
		return new PathShadow3afp<>(path);
	}

	@Test
	public void staticComputeCrossingsFromCircle_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), -2, -2, 2, null));
		assertEquals(-2, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2, -2, 2, null));
		assertEquals(-2, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2.5, -1.5, 2, null));
		assertEquals(0, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 10, 0, 2, null));
		assertEquals(-2, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 4, 0, 0.5, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2.5, 1, 0.5, null));
	}

	@Test
	public void staticComputeCrossingsFromCircle_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), -2, -2, 2, CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2, -2, 2, CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2.5, -1.5, 2, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 10, 0, 2, CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 4, 0, 0.5, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2.5, 1, 0.5, CrossingComputationType.STANDARD));
	}

	@Test
	public void staticComputeCrossingsFromCircle_autoClose() {
		assertEquals(0, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), -2, -2, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2, -2, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2.5, -1.5, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 10, 0, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(-2, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 4, 0, 0.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2.5, 1, 0.5, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	public void staticComputeCrossingsFromCircle_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), -2, -2, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2, -2, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2.5, -1.5, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 10, 0, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 4, 0, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromCircle(0, this.shape.getPathIterator(), 2.5, 1, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticComputeCrossingsFromEllipse_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 1, -1.5, 2, 1, null));
		assertEquals(0, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 1, 1, 2, 1, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 4.5, -1, 2, 1, null));
		assertEquals(-1, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 0, -5.5, 2, 1, null));
	}

	@Test
	public void staticComputeCrossingsFromEllipse_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 1, -1.5, 2, 1, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 1, 1, 2, 1, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 4.5, -1, 2, 1, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 0, -5.5, 2, 1, CrossingComputationType.STANDARD));
	}

	@Test
	public void staticComputeCrossingsFromEllipse_autoClose() {
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 1, -1.5, 2, 1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 1, 1, 2, 1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 4.5, -1, 2, 1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 0, -5.5, 2, 1, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	public void staticComputeCrossingsFromEllipse_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 1, -1.5, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 1, 1, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 4.5, -1, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromEllipse(0, this.shape.getPathIterator(), 0, -5.5, 2, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticComputeCrossingsFromPath_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, -3), null));
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 5, -3), null));
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, 1), null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(5, 2, 4, 1), null));
	}

	@Test
	public void staticComputeCrossingsFromPath_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, -3), CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 5, -3), CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, 1), CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(5, 2, 4, 1), CrossingComputationType.STANDARD));
	}

	@Test
	public void staticComputeCrossingsFromPath_autoClose() {
		assertEquals(-2, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, -3), CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 5, -3), CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, 1), CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(5, 2, 4, 1), CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	public void staticComputeCrossingsFromPath_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, -3), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 5, -3), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(1, -1, 4, 1), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPath(0, this.shape.getPathIterator(),
				createShadow(5, 2, 4, 1), CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticComputeCrossingsFromPoint_null() {
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, null));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, null));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, null));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, null));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, null));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, null));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, null));
	}

	@Test
	public void staticComputeCrossingsFromPoint_standard() {
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, CrossingComputationType.STANDARD));
	}

	@Test
	public void staticComputeCrossingsFromPoint_autoClose() {
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, CrossingComputationType.AUTO_CLOSE));
		assertEquals(-1, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	public void staticComputeCrossingsFromPoint_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, -0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, -0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 7, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 2, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 5, 2, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 0, 4, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 3, 0, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromPoint(0, this.shape.getPathIterator(), 1, 1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticComputeCrossingsFromRect_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1, -2, 3, -1, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, null));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 7, 3, 9, 4, null));
		assertEquals(-1, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, null));
	}

	@Test
	public void staticComputeCrossingsFromRect_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1, -2, 3, -1, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 7, 3, 9, 4, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, CrossingComputationType.STANDARD));
	}

	@Test
	public void staticComputeCrossingsFromRect_autoClose() {
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1, -2, 3, -1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 7, 3, 9, 4, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	public void staticComputeCrossingsFromRect_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1, -2, 3, -1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), 7, 3, 9, 4, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromRect(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticComputeCrossingsFromRoundRect_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 1, -2, 3, -1, .2, .1, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, null));
		assertEquals(0, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 7, 3, 9, 4, .2, .1, null));
		assertEquals(-1, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, .1, .2, null));
	}

	@Test
	public void staticComputeCrossingsFromRoundRect_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 1, -2, 3, -1, .2, .1, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 7, 3, 9, 4, .2, .1, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, .2, .1, CrossingComputationType.STANDARD));
	}

	@Test
	public void staticComputeCrossingsFromRoundRect_autoClose() {
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 1, -2, 3, -1, .2, .1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 7, 3, 9, 4, .2, .1, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, .2, .1, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	public void staticComputeCrossingsFromRoundRect_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 1, -2, 3, -1, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 1.5, 1.5, 3.5, 2.5, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), 7, 3, 9, 4, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromRoundRect(0, this.shape.getPathIterator(), -4, -0.5, -2, 0.5, .2, .1, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticComputeCrossingsFromSegment_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -1, 2, -3, null));
		assertEquals(-1, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -6, 2, -3, null));
		assertEquals(-2, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 2, -3, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 5, 3, null));
	}

	@Test
	public void staticComputeCrossingsFromSegment_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -1, 2, -3, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -6, 2, -3, CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 2, -3, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 5, 3, CrossingComputationType.STANDARD));
	}

	@Test
	public void staticComputeCrossingsFromSegment_autoClose() {
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -1, 2, -3, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -6, 2, -3, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 2, -3, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 5, 3, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	public void staticComputeCrossingsFromSegment_simpleIntersectionWhenNotPolygon() {
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -1, 2, -3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 1, -6, 2, -3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 2, -3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromSegment(0, this.shape.getPathIterator(), 4, 0, 5, 3, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticComputeCrossingsFromTriangle_null() {
		assertEquals(-2, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				1, -1, 4, 0, 2, .5, null));
		assertEquals(0, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				9, 1, 12, 2, 10, 1.5, null));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				5, 0, 8, 1, 6, .5, null));
		assertEquals(-2, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				-1, -4, 2, -3, 0, -2.5, null));
		assertEquals(-1, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				3, -6, 6, -5, 4, -4.5, null));
	}

	@Test
	public void staticComputeCrossingsFromTriangle_standard() {
		assertEquals(-2, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				1, -1, 4, 0, 2, .5, CrossingComputationType.STANDARD));
		assertEquals(0, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				9, 1, 12, 2, 10, 1.5, CrossingComputationType.STANDARD));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				5, 0, 8, 1, 6, .5, CrossingComputationType.STANDARD));
		assertEquals(-2, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				-1, -4, 2, -3, 0, -2.5, CrossingComputationType.STANDARD));
		assertEquals(-1, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				3, -6, 6, -5, 4, -4.5, CrossingComputationType.STANDARD));
	}

	@Test
	public void staticComputeCrossingsFromTriangle_autoClose() {
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				1, -1, 4, 0, 2, .5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				9, 1, 12, 2, 10, 1.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				5, 0, 8, 1, 6, .5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				-1, -4, 2, -3, 0, -2.5, CrossingComputationType.AUTO_CLOSE));
		assertEquals(0, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				3, -6, 6, -5, 4, -4.5, CrossingComputationType.AUTO_CLOSE));
	}

	@Test
	public void staticComputeCrossingsFromTriangle_simpleIntersectionWhenNotPolygo() {
		assertEquals(0, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				1, -1, 4, 0, 2, .5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				9, 1, 12, 2, 10, 1.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(SHAPE_INTERSECTS, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				5, 0, 8, 1, 6, .5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				-1, -4, 2, -3, 0, -2.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
		assertEquals(0, Path3afp.computeCrossingsFromTriangle(0, this.shape.getPathIterator(),
				3, -6, 6, -5, 4, -4.5, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON));
	}

	@Test
	public void staticComputeControlPointBoundingBox() {
		B box = createRectangle(0, 0, 0, 0);
		Path3afp.computeControlPointBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(5, box.getMaxY());
	}

	@Test
	public void staticComputeDrawableElementBoundingBox() {
		B box = createRectangle(0, 0, 0, 0);
		Path3afp.computeDrawableElementBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
	}

	@Test
	public void staticContainsPoint() {
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), -5, 1));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 3, 6));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 3, -10));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 11, 1));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 4, 1));
		assertTrue(Path3afp.containsPoint(this.shape.getPathIterator(), 4, 3));
		this.shape.closePath();
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), -5, 1));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 3, 6));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 3, -10));
		assertFalse(Path3afp.containsPoint(this.shape.getPathIterator(), 11, 1));
		assertTrue(Path3afp.containsPoint(this.shape.getPathIterator(), 4, 1));
		assertTrue(Path3afp.containsPoint(this.shape.getPathIterator(), 4, 3));
	}

	@Test
	public void staticContainsRectangle() {
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), -5, 1, 2, 1));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, 6, 2, 1));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, -10, 2, 1));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 11, 1, 2, 1));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, 1, 2, 1));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 4, 3, 2, 1));
		this.shape.closePath();
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), -5, 1, 2, 1));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, 6, 2, 1));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, -10, 2, 1));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 11, 1, 2, 1));
		assertTrue(Path3afp.containsRectangle(this.shape.getPathIterator(), 3, 0, 2, 1));
		assertFalse(Path3afp.containsRectangle(this.shape.getPathIterator(), 4, 3, 2, 1));
	}

	@Test
	public void staticGetClosestPointTo_open() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -2, 1, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, 0, result);
		assertEpsilonEquals(.5, result.getX());
		assertEpsilonEquals(.5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 3, 0, result);
		assertEpsilonEquals(2.56307, result.getX());
		assertEpsilonEquals(0.91027, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, -4, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());
	}

	@Test
	public void staticGetClosestPointTo_close() {
		this.shape.closePath();

		Point3D result;

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -2, 1, result);
		assertEpsilonEquals(0, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, 0, result);
		assertEpsilonEquals(1, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 3, 0, result);
		assertEpsilonEquals(3, result.getX());
		assertEpsilonEquals(0, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getClosestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, -4, result);
		assertEpsilonEquals(2.55405, result.getX());
		assertEpsilonEquals(-1.82432, result.getY());
	}

	@Test
	public void staticGetFarthestPointTo_open() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -2, 1, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 3, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, -4, result);
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@Test
	public void staticGetFarthestPointTo_close() {
		this.shape.closePath();

		Point3D result;

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), -2, 1, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 3, 0, result);
		assertEpsilonEquals(7, result.getX());
		assertEpsilonEquals(-5, result.getY());

		result = createPoint(Double.NaN, Double.NaN);
		Path3afp.getFarthestPointTo(this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), 1, -4, result);
		assertEpsilonEquals(4, result.getX());
		assertEpsilonEquals(3, result.getY());
	}

	@Test
	public void staticIntersectsPathIteratorRectangle_open() {
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 2, 1));
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 2, 1));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 2, 1));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 2, 1));
	}

	@Test
	public void staticIntersectsPathIteratorRectangle_close() {
		this.shape.closePath();
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 2, 1));
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 2, 1));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 2, 1));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 2, 1));
	}

	@Test
	public void staticComputeLength_open() {
		assertEpsilonEquals(14.71628, Path3afp.computeLength(this.shape.getPathIterator()));
	}

	@Test
	public void staticComputeLength_close() {
		this.shape.closePath();
		assertEpsilonEquals(23.31861, Path3afp.computeLength(this.shape.getPathIterator()));
	}

	@Test
	public void addIterator_open() {
		Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5);
		p2.lineTo(4, 6);
		p2.lineTo(0, 8);
		p2.lineTo(5, -3);
		p2.closePath();

		Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		PathIterator3afp pi = this.shape.getPathIterator();
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

	@Test
	public void addIterator_closeAfter() {
		Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5);
		p2.lineTo(4, 6);
		p2.lineTo(0, 8);
		p2.lineTo(5, -3);
		p2.closePath();

		Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		this.shape.closePath();

		PathIterator3afp pi = this.shape.getPathIterator();
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

	@Test
	public void addIterator_closeBefore() {
		this.shape.closePath();

		Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5);
		p2.lineTo(4, 6);
		p2.lineTo(0, 8);
		p2.lineTo(5, -3);
		p2.closePath();

		Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		PathIterator3afp pi = this.shape.getPathIterator();
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

	@Test(expected = IllegalStateException.class)
	public void curveToDoubleDoubleDoubleDoubleDoubleDouble_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.curveTo(15, 145, 50, 20, 0, 0);
	}
	
	@Test
	public void curveToDoubleDoubleDoubleDoubleDoubleDouble() {
		this.shape.curveTo(123.456, 456.789, 789.123, 159.753, 456.852, 963.789);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 123.456,  456.789, 789.123, 159.753, 456.852, 963.789);
		assertNoElement(pi);
	}
	
	@Test(expected = IllegalStateException.class)
	public void curveToPoint3DPoint3DPoint3D_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.curveTo(createPoint(15, 145), createPoint(50, 20), createPoint(0, 0));
	}

	@Test
	public void curveToPoint3DPoint3DPoint3D() {
		this.shape.curveTo(createPoint(123.456, 456.789), createPoint(789.123, 159.753), createPoint(456.852, 963.789));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 123.456,  456.789, 789.123, 159.753, 456.852, 963.789);
		assertNoElement(pi);
	}
	
//	@Test
//	public void generateShapeBitmap() throws IOException {
//		this.shape.arcTo(20, 10, 10, 10, 0, false, false);
//		File filename = generateTestPicture(this.shape);
//		System.out.println("Filename: " + filename);
//	}

/*	@Test
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_01_arcOnly() {
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.ARC_ONLY);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@Test
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_01_lineTo() {
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.LINE_THEN_ARC);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@Test
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_01_moveTo() {
		this.shape.arcTo(5, 5, 20, 10, 0, 1, ArcType.MOVE_THEN_ARC);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@Test
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_0251_arcOnly() {
		this.shape.arcTo(5, 5, 20, 10, .25, 1, ArcType.ARC_ONLY);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 8.95958, 3.63357, 13.7868, 7.92893, 20, 10);
		assertNoElement(pi);
	}

	@Test
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_0251_lineTo() {
		this.shape.arcTo(5, 5, 20, 10, .25, 1, ArcType.LINE_THEN_ARC);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.LINE_TO, 7.40028, -0.71462);
		assertElement(pi, PathElementType.CURVE_TO, 8.95958, 3.63357, 13.7868, 7.92893, 20, 10);
		assertNoElement(pi);
	}

	@Test
	public void arcToDoubleDoubleDoubleDoubleDoubleDoubleArcType_0251_moveTo() {
		this.shape.arcTo(5, 5, 20, 10, .25, 1, ArcType.MOVE_THEN_ARC);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.MOVE_TO, 7.40028, -0.71462);
		assertElement(pi, PathElementType.CURVE_TO, 8.95958, 3.63357, 13.7868, 7.92893, 20, 10);
		assertNoElement(pi);
	}

	@Test
	public void arcToPoint3DPoint3DDoubleDoubleArcType_01_arcOnly() {
		this.shape.arcTo(createPoint(5, 5), createPoint(20, 10), 0, 1, ArcType.ARC_ONLY);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@Test
	public void arcToDoubleDoubleDoubleDouble() {
		this.shape.arcTo(5, 5, 20, 10);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}

	@Test
	public void arcToPoint3DPoint3D() {
		this.shape.arcTo(createPoint(5, 5), createPoint(20, 10));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5.89543, 0.52285, 11.71573, 7.23858, 20, 10);
		assertNoElement(pi);
	}*/

	@Test
	public void getCoordAt() {
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

	@Test
	public void getPathIteratorDouble_open() {
		PathIterator3afp pi = this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
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

	@Test
	public void getPathIteratorDouble_close() {
		this.shape.closePath();
		PathIterator3afp pi = this.shape.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
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

	@Test
	public void getPathIteratorTransform3DDouble_null() {
		PathIterator3afp pi = this.shape.getPathIterator(null, MathConstants.SPLINE_APPROXIMATION_RATIO);
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

	@Test
	public void getPathIteratorTransform3DDouble_identity() {
		PathIterator3afp pi = this.shape.getPathIterator(new Transform3D(), MathConstants.SPLINE_APPROXIMATION_RATIO);
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

	@Test
	public void getPathIteratorTransform3DDouble_translation() {
		Transform3D transform = new Transform3D();
		transform.setTranslation(10, 10);
		PathIterator3afp pi = this.shape.getPathIterator(transform, MathConstants.SPLINE_APPROXIMATION_RATIO);
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

	@Test
	public void getLength() {
		assertEpsilonEquals(14.71628, this.shape.getLength());
	}

	@Test
	public void getLengthSquared() {
		assertEpsilonEquals(216.56892, this.shape.getLengthSquared());
	}

	@Test(expected = IllegalStateException.class)
	public void lineToIntInt_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.lineTo(15, 145);
	}

	@Test
	public void lineToDoubleDouble() {
		this.shape.lineTo(123.456, 456.789);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789);
		assertNoElement(pi);
	}

	@Test(expected = IllegalStateException.class)
	public void lineToPoint3D_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.lineTo(createPoint(15, 145));
	}

	@Test
	public void lineToPoint3D() {
		this.shape.lineTo(createPoint(123.456, 456.789));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789);
		assertNoElement(pi);
	}

	@Test
	public void moveToDoubleDouble() {
		this.shape.moveTo(123.456, 456.789);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789);
		assertNoElement(pi);
	}

	@Test
	public void moveToPoint3D() {
		this.shape.moveTo(createPoint(123.456, 456.789));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789);
		assertNoElement(pi);
	}

	@Test(expected = IllegalStateException.class)
	public void quadToIntIntIntInt_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.quadTo(15, 145, 50, 20);
	}

	@Test
	public void quadToDoubleDoubleDoubleDouble() {
		this.shape.quadTo(123.456, 456.789, 789.123, 159.753);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.QUAD_TO, 123.456,  456.789, 789.123, 159.753);
		assertNoElement(pi);
	}
	
	@Test(expected = IllegalStateException.class)
	public void quadToPoint3DPoint3D_noMoveTo() {
		Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
		tmpShape.quadTo(createPoint(15, 145), createPoint(50, 20));
	}

	@Test
	public void quadToPoint3DPoint3D() {
		this.shape.quadTo(createPoint(123.456, 456.789), createPoint(789.123, 159.753));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertElement(pi, PathElementType.QUAD_TO, 123.456,  456.789, 789.123, 159.753);
		assertNoElement(pi);
	}

	@Test
	public void removeDoubleDouble() {
		assertTrue(this.shape.remove(5, -1));
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(4, 3)));
		assertTrue(this.shape.remove(1, 1));
		assertTrue(this.shape.size() == 3);
		assertFalse(this.shape.remove(35, 35));
	}

	@Test
	public void setLastPointDoubleDouble() {
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(7, -5)));
		this.shape.setLastPoint(2, 2);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(2, 2)));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 2, 2);
		assertNoElement(pi);
	}

	@Test
	public void setLastPointPoint3D() {
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(7, -5)));
		this.shape.setLastPoint(createPoint(2, 2));
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(2, 2)));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 2, 2);
		assertNoElement(pi);
	}

	@Test
	public void toCollection() {
		Collection<? extends Point3D> collection = this.shape.toCollection();
		assertEquals(7, collection.size());
		Iterator<? extends Point3D> iterator = collection.iterator();
		assertEpsilonEquals(createPoint(0, 0), iterator.next());
		assertEpsilonEquals(createPoint(1, 1), iterator.next());
		assertEpsilonEquals(createPoint(3, 0), iterator.next());
		assertEpsilonEquals(createPoint(4, 3), iterator.next());
		assertEpsilonEquals(createPoint(5, -1), iterator.next());
		assertEpsilonEquals(createPoint(6, 5), iterator.next());
		assertEpsilonEquals(createPoint(7, -5), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void transformTransform3D() {
		Point3D p1 = randomPoint2f();
		Point3D p2 = randomPoint2f();
		Point3D p3 = randomPoint2f();
		Point3D p4 = randomPoint2f();
		Point3D p5 = randomPoint2f();
		Point3D p6 = randomPoint2f();
		Point3D p7 = randomPoint2f();
		
		Path3afp path = createPath();
		path.moveTo(p1.getX(),p1.getY());
		path.lineTo(p2.getX(),p2.getY());
		path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		path.closePath();
		
		Transform3D trans = new Transform3D(this.randomMatrix3f());
		
		trans.transform(p1);
		trans.transform(p2);
		trans.transform(p3);
		trans.transform(p4);
		trans.transform(p5);
		trans.transform(p6);
		trans.transform(p7);
		
		Path3afp pathTrans = createPath();
		pathTrans.moveTo(p1.getX(),p1.getY());
		pathTrans.lineTo(p2.getX(),p2.getY());
		pathTrans.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		pathTrans.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		pathTrans.closePath();
		
		path.transform(trans);
		
		assertTrue(path.equalsToPathIterator(pathTrans.getPathIterator()));
	}

	@Override
	public void testClone() {
		Path3afp clone = this.shape.clone();
		PathIterator3afp pi = (PathIterator3afp) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, 1);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 4, 3);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 6, 5, 7, -5);
		assertNoElement(pi);
	}

	@Test
	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createPath()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(this.shape.clone()));
	}

	@Test
	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createPath().getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(this.shape.clone().getPathIterator()));
	}

	@Test
	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createPath().getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.clone().getPathIterator()));
	}

	@Test
	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createPath()));
		assertTrue(this.shape.equalsToShape((T) this.shape));
		assertTrue(this.shape.equalsToShape((T) this.shape.clone()));
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
	public void containsPoint3D() {
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
	public void getClosestPointTo() {
		Point3D result;

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
	public void getFarthestPointTo() {
		Point3D result;

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
	public void getDistance() {
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
	public void getDistanceSquared() {
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
	public void getDistanceL1() {
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
	public void getDistanceLinf() {
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
	public void setIT() {
		this.shape.set((T) createPath());
		PathIterator3afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
		Path3afp path = createPath();
		path.moveTo(123.456, 456.789);
		path.lineTo(789.123, 159.753);
		this.shape.set(path);
		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789);
		assertElement(pi, PathElementType.LINE_TO, 789.123, 159.753);
		assertNoElement(pi);
	}

	@Override
	public void getPathIterator() {
		PathIterator3afp pi = this.shape.getPathIterator();
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
	public void getPathIteratorTransform3D() {
		PathIterator3afp pi;
		Transform3D transform = new Transform3D();
		
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
	public void createTransformedShape() {
		Point3D p1 = randomPoint2f();
		Point3D p2 = randomPoint2f();
		Point3D p3 = randomPoint2f();
		Point3D p4 = randomPoint2f();
		Point3D p5 = randomPoint2f();
		Point3D p6 = randomPoint2f();
		Point3D p7 = randomPoint2f();
		
		Path3afp path = createPath();
		path.moveTo(p1.getX(),p1.getY());
		path.lineTo(p2.getX(),p2.getY());
		path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		path.closePath();

		Transform3D trans = new Transform3D(randomMatrix3f());
		Path3afp transformedShape = (Path3afp) path.createTransformedShape(trans);
		path.transform(trans);		
	
		assertTrue(path.equalsToShape(transformedShape));
	}

	@Override
	public void translateDoubleDouble() {
		double dx = this.random.nextDouble()*20;
		double dy = this.random.nextDouble()*20;
		
		Path3afp p2 = createPath();
		p2.moveTo(dx, dy);
		p2.lineTo(1 + dx, 1 + dy);
		p2.quadTo(3 + dx, 0 + dy, 4 + dx, 3 + dy);
		p2.curveTo(5 + dx, -1 + dy, 6 + dx, 5 + dy, 7 + dx, -5 + dy);
		
		this.shape.translate(dx, dy);
		
		assertTrue(this.shape.equals(p2));		
	}

	@Override
	public void translateVector3D() {
		double dx = this.random.nextDouble()*20;
		double dy = this.random.nextDouble()*20;
		
		this.shape.translate(dx, dy);
		
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
		assertNoElement(pi);
	}

	@Override
	public void toBoundingBox() {
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
	}

	@Override
	public void toBoundingBoxB() {
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(3, box.getMaxY());
	}

	@Override
	public void containsRectangularPrism3afp() {
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
	public void intersectsRectangularPrism3afp() {
		assertFalse(this.shape.intersects(createRectangle(1, -2, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(1.5, 1.5, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(7, 3, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(-4, -0.5, 2, 1)));
	}

	@Test
	public void intersectsRectangularPrism3afp_close() {
		this.shape.closePath();
		assertTrue(this.shape.intersects(createRectangle(1, -2, 2, 1)));
		assertTrue(this.shape.intersects(createRectangle(1.5, 1.5, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(7, 3, 2, 1)));
		assertFalse(this.shape.intersects(createRectangle(-4, -0.5, 2, 1)));
	}

	@Override
	public void intersectsSphere3afp() {
		assertFalse(this.shape.intersects(createCircle(-2, -2, 2)));
		assertFalse(this.shape.intersects(createCircle(2, -2, 2)));
		assertFalse(this.shape.intersects(createCircle(2.5, -1.5, 2)));
		assertFalse(this.shape.intersects(createCircle(10, 0, 2)));
		assertFalse(this.shape.intersects(createCircle(4, 0, 0.5)));
		assertTrue(this.shape.intersects(createCircle(2.5, 1, 0.5)));
	}

	@Test
	public void intersectsSphere3afp_close() {
		this.shape.closePath();
		assertFalse(this.shape.intersects(createCircle(-2, -2, 2)));
		assertTrue(this.shape.intersects(createCircle(2, -2, 2)));
		assertTrue(this.shape.intersects(createCircle(2.5, -1.5, 2)));
		assertFalse(this.shape.intersects(createCircle(10, 0, 2)));
		assertTrue(this.shape.intersects(createCircle(4, 0, 0.5)));
		assertTrue(this.shape.intersects(createCircle(2.5, 1, 0.5)));
	}
d

	@Override
	public void intersectsSegment3afp() {
		assertFalse(this.shape.intersects(createSegment(1, -1, 2, -3)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 2, -3)));
		assertFalse(this.shape.intersects(createSegment(4, 0, 2, -3)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 5, 3)));
	}

	@Test
	public void intersectsSegment3afp_close() {
		this.shape.closePath();
		assertFalse(this.shape.intersects(createSegment(1, -1, 2, -3)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 2, -3)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 2, -3)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 5, 3)));
	}

	@Override
	public void intersectsPath3afp() {
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, -3)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 5, -3)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, 1)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 4, 1)));
	}

	@Test
	public void intersectsPath3afp_close() {
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, -3)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 5, -3)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 4, 1)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 4, 1)));
	}

	@Override
	public void intersectsPathIterator3afp() {
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, -3).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 5, -3).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, 1).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 4, 1).getPathIterator()));
	}

	@Test
	public void intersectsPathIterator3afp_close() {
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 4, -3).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 5, -3).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 4, 1).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 4, 1).getPathIterator()));
	}

	@Override
	public void intersectsShape3D() {
		assertTrue(this.shape.intersects((Shape3D) createSegment(4, 0, 5, 3)));
		assertTrue(this.shape.intersects((Shape3D)createRectangle(1.5, 1.5, 2, 1)));
	}

	@Override
	public void operator_addVector3D() {
		double dx = this.random.nextDouble()*20;
		double dy = this.random.nextDouble()*20;
		
		this.shape.operator_add(createVector(dx, dy));
		
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
		assertNoElement(pi);
	}

	@Override
	public void operator_plusVector3D() {
		double dx = this.random.nextDouble()*20;
		double dy = this.random.nextDouble()*20;
		
		T shape = this.shape.operator_plus(createVector(dx, dy));
		
		PathIterator3afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dx + 4, dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1, dx + 6, dy + 5, dx + 7, dy - 5);
		assertNoElement(pi);
	}

	@Override
	public void operator_removeVector3D() {
		double dx = this.random.nextDouble()*20;
		double dy = this.random.nextDouble()*20;
		
		this.shape.operator_remove(createVector(dx, dy));
		
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -dx, -dy);
		assertElement(pi, PathElementType.LINE_TO, -dx + 1, -dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, -dx + 3, -dy, -dx + 4, -dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, -dx + 5, -dy - 1, -dx + 6, -dy + 5, -dx + 7, -dy - 5);
		assertNoElement(pi);
	}

	@Override
	public void operator_minusVector3D() {
		double dx = this.random.nextDouble()*20;
		double dy = this.random.nextDouble()*20;
		
		T shape = this.shape.operator_minus(createVector(dx, dy));
		
		PathIterator3afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -dx, -dy);
		assertElement(pi, PathElementType.LINE_TO, -dx + 1, -dy + 1);
		assertElement(pi, PathElementType.QUAD_TO, -dx + 3, -dy, -dx + 4, -dy + 3);
		assertElement(pi, PathElementType.CURVE_TO, -dx + 5, -dy - 1, -dx + 6, -dy + 5, -dx + 7, -dy - 5);
		assertNoElement(pi);
	}

	@Override
	public void operator_multiplyTransform3D() {
		Point3D p1 = randomPoint2f();
		Point3D p2 = randomPoint2f();
		Point3D p3 = randomPoint2f();
		Point3D p4 = randomPoint2f();
		Point3D p5 = randomPoint2f();
		Point3D p6 = randomPoint2f();
		Point3D p7 = randomPoint2f();
		
		Path3afp path = createPath();
		path.moveTo(p1.getX(),p1.getY());
		path.lineTo(p2.getX(),p2.getY());
		path.quadTo(p3.getX(),p3.getY(),p4.getX(),p4.getY());
		path.curveTo(p5.getX(),p5.getY(), p6.getX(),p6.getY(), p7.getX(),p7.getY());
		path.closePath();

		Transform3D trans = new Transform3D(randomMatrix3f());
		Path3afp transformedShape = (Path3afp) path.operator_multiply(trans);
		path.transform(trans);		
	
		assertTrue(path.equalsToShape(transformedShape));
	}

	@Override
	public void operator_andPoint3D() {
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
	public void operator_andShape3D() {
		assertTrue(this.shape.operator_and(createSegment(4, 0, 5, 3)));
		assertTrue(this.shape.operator_and(createRectangle(1.5, 1.5, 2, 1)));
	}

	@Override
	public void operator_upToPoint3D() {
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
	public void getCurrentX() {
		assertEpsilonEquals(7, this.shape.getCurrentX());
		this.shape.lineTo(154, 485);
		assertEpsilonEquals(154, this.shape.getCurrentX());
	}

	@Test
	public void getCurrentY() {
		assertEpsilonEquals(-5, this.shape.getCurrentY());
		this.shape.lineTo(154, 485);
		assertEpsilonEquals(485, this.shape.getCurrentY());
	}

	@Test
	public void getCurrentPoint() {
		assertFpPointEquals(7, -5, this.shape.getCurrentPoint());
		this.shape.lineTo(154, 485);
		assertFpPointEquals(154, 485, this.shape.getCurrentPoint());
	}

}