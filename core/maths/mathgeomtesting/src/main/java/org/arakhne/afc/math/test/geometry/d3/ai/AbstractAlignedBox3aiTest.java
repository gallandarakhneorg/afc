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

package org.arakhne.afc.math.test.geometry.d3.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;
import org.arakhne.afc.math.geometry.d3.ai.AlignedBox3ai;
import org.arakhne.afc.math.geometry.d3.ai.AlignedBox3ai.Side;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractAlignedBox3aiTest<T extends AlignedBox3ai<?, T, ?, ?, ?, ?, T>>
		extends AbstractPrism3aiTest<T, T> {

	protected static Quaternion4d newAxisAngleZ(double angle) {
		final Quaternion4d q = new Quaternion4d();
		q.setAxisAngle(0, 0, 1, angle);
		return q;
	}

	@Override
	protected final T createShape() {
		return createAlignedBox(5, 8, 0, 10, 5, 0);
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
		assertEpsilonEquals(5, clone.getMinX());
		assertEpsilonEquals(8, clone.getMinY());
		assertEpsilonEquals(15, clone.getMaxX());
		assertEpsilonEquals(13, clone.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createAlignedBox(0, 0, 0, 5, 5, 0)));
		assertFalse(this.shape.equals(createAlignedBox(5, 8, 0, 10, 6, 0)));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 10, 5, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createAlignedBox(5, 8, 0, 10, 5, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createAlignedBox(0, 0, 0, 5, 5, 0).getPathIterator()));
		assertFalse(this.shape.equals(createAlignedBox(5, 8, 0, 10, 6, 0).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 10, 5, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createAlignedBox(5, 8, 0, 10, 5, 0).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape(createAlignedBox(0, 0, 0, 5, 5, 0)));
		assertFalse(this.shape.equalsToShape(createAlignedBox(5, 8, 0, 10, 6, 0)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape(createAlignedBox(5, 8, 0, 10, 5, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createAlignedBox(0, 0, 0, 5, 5, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createAlignedBox(5, 8, 0, 10, 6, 0).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 10, 5, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createAlignedBox(5, 8, 0, 10, 5, 0).getPathIterator()));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getPointIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator();
		Point3D p;
		
		int[] coords;
		
		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getPointIteratorSide_Top(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator(Side.TOP);
		Point3D p;
		
		int[] coords;
		
		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getPointIteratorSide_Right(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator(Side.RIGHT);
		Point3D p;
		
		int[] coords;
		
		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getPointIteratorSide_Bottom(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator(Side.BOTTOM);
		Point3D p;
		
		int[] coords;
		
		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getPointIteratorSide_Left(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator(Side.LEFT);
		Point3D p;
		
		int[] coords;
		
		coords = new int[] {12,11,10,9};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(5, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(8, p.iy());
		}

		coords = new int[] {9,10,11,12,13};
		for(int y : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(15, p.ix());
			assertEquals(y, p.iy());
		}

		coords = new int[] {14,13,12,11,10,9,8,7,6,5};
		for(int x : coords) {
			assertTrue(iterator.hasNext());
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(13, p.iy());
		}

		assertFalse(iterator.hasNext());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(4,8, 0)));
		assertEpsilonEquals(9.433981132f, this.shape.getDistance(createPoint(0,0, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(4,8, 0)));
		assertEpsilonEquals(89f, this.shape.getDistanceSquared(createPoint(0,0, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(4,8, 0)));
		assertEpsilonEquals(13f, this.shape.getDistanceL1(createPoint(0,0, 0)));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(4,8, 0)));
		assertEpsilonEquals(8f, this.shape.getDistanceLinf(createPoint(0,0, 0)));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getClosestPointTo(createPoint(5,8, 0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(10,10, 0));
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(10, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(4,8, 0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(0,0, 0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(5,8, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(10,10, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(4,8, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(0,0, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(24,0, 0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(0,32, 0));
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(8, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(0,0, 0));
		assertTrue(this.shape.contains(11,10, 0));
		assertFalse(this.shape.contains(11,50, 0));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsAlignedBox3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createAlignedBox(0,0, 0,1,1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(0,0, 0,8,1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(0,0, 0,8,6, 0)));
		assertFalse(this.shape.contains(createAlignedBox(0,0, 0,100,100, 0)));
		assertTrue(this.shape.contains(createAlignedBox(7,10, 0,1,1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(16,0, 0,100,100, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void intersectsAlignedBox3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createAlignedBox(0,0, 0,1,1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(0,0, 0,8,1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(0,0, 0,8,6, 0)));
		assertTrue(this.shape.intersects(createAlignedBox(0,0, 0,100,100, 0)));
		assertTrue(this.shape.intersects(createAlignedBox(7,10, 0,1,1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(16,0, 0,100,100, 0)));
	}

	@Override
	public void intersectsSphere3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSphere(0,0,0,1)));
		assertFalse(this.shape.intersects(createSphere(0,0,0,8)));
		assertTrue(this.shape.intersects(createSphere(0,0,0,100)));
		assertTrue(this.shape.intersects(createSphere(7,10,0,1)));
		assertFalse(this.shape.intersects(createSphere(16,0,0,5)));
		assertFalse(this.shape.intersects(createSphere(5,15,0,1)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void intersectsSegment3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSegment(0,0,0,1,1,0)));
		assertFalse(this.shape.intersects(createSegment(0,0,0,8,1,0)));
		assertFalse(this.shape.intersects(createSegment(0,0,0,8,6,0)));
		assertTrue(this.shape.intersects(createSegment(0,0,0,100,100,0)));
		assertTrue(this.shape.intersects(createSegment(7,10,0,1,1,0)));
		assertFalse(this.shape.intersects(createSegment(16,0,0,100,100,0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void intersectsPath3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path = createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		this.shape = createAlignedBox(0, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(4, 3, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(2, 2, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(2, 1, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(3, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(-1, -1, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(4, -3, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(-3, 4, 0, 1, 1, 0);
		assertFalse(this.shape.intersects(path));
		this.shape = createAlignedBox(6, -5, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(4, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(5, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects(path));
		this.shape = createAlignedBox(0, -3, 0, 1, 1, 0);
		assertFalse(this.shape.intersects(path));
		this.shape = createAlignedBox(0, -3, 0, 2, 1, 0);
		assertFalse(this.shape.intersects(path));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,13, 0);
		assertElement(pi, PathElementType.CLOSE, 5,8, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3ai pi;
		
		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 5,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,13, 0);
		assertElement(pi, PathElementType.CLOSE, 5,8, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f, 0);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 8,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 18,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 18,18, 0);
		assertElement(pi, PathElementType.LINE_TO, 8,18, 0);
		assertElement(pi, PathElementType.CLOSE, 8,13, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));
		
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, -2,9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,16, 0);
		assertElement(pi, PathElementType.LINE_TO, 1,20, 0);
		assertElement(pi, PathElementType.LINE_TO, -6,13, 0);
		assertElement(pi, PathElementType.CLOSE, -2,9, 0);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void createTransformedShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3ai pi;
		
		tr = new Transform3D();
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5,8);
		assertElement(pi, PathElementType.LINE_TO, 15,8);
		assertElement(pi, PathElementType.LINE_TO, 15,13);
		assertElement(pi, PathElementType.LINE_TO, 5,13);
		assertElement(pi, PathElementType.CLOSE, 5,8);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f, 0);
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 8,13);
		assertElement(pi, PathElementType.LINE_TO, 18,13);
		assertElement(pi, PathElementType.LINE_TO, 18,18);
		assertElement(pi, PathElementType.LINE_TO, 8,18);
		assertElement(pi, PathElementType.CLOSE, 8,13);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));		
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -2,9);
		assertElement(pi, PathElementType.LINE_TO, 5,16);
		assertElement(pi, PathElementType.LINE_TO, 1,20);
		assertElement(pi, PathElementType.LINE_TO, -6,13);
		assertElement(pi, PathElementType.CLOSE, -2,9);
		assertNoElement(pi);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set(createAlignedBox(10, 12, 0, 14, 16, 0));
		assertEquals(10, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(24, this.shape.getMaxX());
		assertEquals(28, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	@Disabled
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createPoint(0,0, 0)));
		assertTrue(this.shape.contains(createPoint(11,10, 0)));
		assertFalse(this.shape.contains(createPoint(11,50, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(3, 4, 0));
		assertEquals(8, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeClosestPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeClosestPoint(5, 8, 0, 15, 13, 0, 5, 8, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeClosestPoint(5, 8, 0, 15, 13, 0, 10, 10, 0, p);
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(10, p.iy());
		
		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeClosestPoint(5, 8, 0, 15, 13, 0, 4, 8, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeClosestPoint(5, 8, 0, 15, 13, 0, 0, 0, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeFarthestPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 5, 8, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 10, 10, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 4, 8, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());
		
		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 0, 0, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(13, p.iy());

		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 24, 0, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());

		p = createPoint(0, 0, 0);
		AlignedBox3ai.computeFarthestPoint(5, 8, 0, 15, 13, 0, 0, 32, 0, p);
		assertNotNull(p);
		assertEquals(15, p.ix());
		assertEquals(8, p.iy());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticIntersectsAlignedBoxAlignedBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(AlignedBox3ai.intersectsAlignedBoxAlignedBox(5, 8, 0, 15, 13, 0, 0, 0, 0, 1, 1, 0));
		assertFalse(AlignedBox3ai.intersectsAlignedBoxAlignedBox(5, 8, 0, 15, 13, 0, 0, 0, 0, 8, 1, 0));
		assertFalse(AlignedBox3ai.intersectsAlignedBoxAlignedBox(5, 8, 0, 15, 13, 0, 0, 0, 0, 8, 6, 0));
		assertTrue(AlignedBox3ai.intersectsAlignedBoxAlignedBox(5, 8, 0, 15, 13, 0, 0, 0, 0, 100, 100, 0));
		assertTrue(AlignedBox3ai.intersectsAlignedBoxAlignedBox(5, 8, 0, 15, 13, 0, 7, 10, 0, 8, 11, 0));
		assertFalse(AlignedBox3ai.intersectsAlignedBoxAlignedBox(5, 8, 0, 15, 13, 0, 16, 0, 0, 116, 100, 0));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticIntersectsAlignedBoxSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(AlignedBox3ai.intersectsAlignedBoxSegment(5, 8, 0, 15, 13, 0, 0, 0, 0, 1, 1, 0));
		assertFalse(AlignedBox3ai.intersectsAlignedBoxSegment(5, 8, 0, 15, 13, 0, 0, 0, 0, 8, 1, 0));
		assertFalse(AlignedBox3ai.intersectsAlignedBoxSegment(5, 8, 0, 15, 13, 0, 0, 0, 0, 8, 6, 0));
		assertTrue(AlignedBox3ai.intersectsAlignedBoxSegment(5, 8, 0, 15, 13, 0, 0, 0, 0, 100, 100, 0));
		assertTrue(AlignedBox3ai.intersectsAlignedBoxSegment(5, 8, 0, 15, 13, 0, 7, 10, 0, 8, 11, 0));
		assertFalse(AlignedBox3ai.intersectsAlignedBoxSegment(5, 8, 0, 15, 13, 0, 16, 0, 0, 116, 100, 0));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void inflate(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.inflate(1, 2, 0, 3, 4, 0);
		assertEquals(4, this.shape.getMinX());
		assertEquals(6, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setUnion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setUnion(createAlignedBox(0, 0, 0, 12, 1, 0));
		assertEquals(0, this.shape.getMinX());
		assertEquals(0, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void createUnion(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T union = this.shape.createUnion(createAlignedBox(0, 0, 0, 12, 1, 0));
		assertNotSame(this.shape, union);
		assertEquals(0, union.getMinX());
		assertEquals(0, union.getMinY());
		assertEquals(15, union.getMaxX());
		assertEquals(13, union.getMaxY());
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIntersection_noIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setIntersection(createAlignedBox(0, 0, 0, 12, 1, 0));
		assertTrue(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIntersection_intersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.setIntersection(createAlignedBox(0, 0, 0, 7, 10, 0));
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(7, this.shape.getMaxX());
		assertEquals(10, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void createIntersection_noIntersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T box = this.shape.createIntersection(createAlignedBox(0, 0, 0, 12, 1, 0));
		assertNotSame(this.shape, box);
		assertTrue(box.isEmpty());
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void createIntersection_intersection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		//createAlignedBox(5, 8, 10, 5);
		T box = this.shape.createIntersection(createAlignedBox(0, 0, 0, 7, 10, 0));
		assertNotSame(this.shape, box);
		assertEquals(5, box.getMinX());
		assertEquals(8, box.getMinY());
		assertEquals(7, box.getMaxX());
		assertEquals(10, box.getMaxY());
		assertEquals(5, this.shape.getMinX());
		assertEquals(8, this.shape.getMinY());
		assertEquals(15, this.shape.getMaxX());
		assertEquals(13, this.shape.getMaxY());
	}

	@Override
	public void intersectsPathIterator3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3ai path = createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(2, 2, 0);
		path.quadTo(3, 0, 0, 4, 3, 0);
		path.curveTo(5, -1, 0, 6, 5, 0, 7, -5, 0);
		path.closePath();
		this.shape = createAlignedBox(0, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(4, 3, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(2, 2, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(2, 1, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(3, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(-1, -1, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(4, -3, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(-3, 4, 0, 1, 1, 0);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(6, -5, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(4, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(5, 0, 0, 1, 1, 0);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(0, -3, 0, 1, 1, 0);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = createAlignedBox(0, -3, 0, 2, 1, 0);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
	}

	@Override
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSphere(0,0, 0,100)));
		assertTrue(this.shape.intersects((Shape3D) createAlignedBox(7,10, 0,1,1, 0)));
	}

	@Override
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(3, 4, 0));
		assertEquals(8, this.shape.getMinX());
		assertEquals(12, this.shape.getMinY());
		assertEquals(18, this.shape.getMaxX());
		assertEquals(17, this.shape.getMaxY());
	}

	@Override
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_plus(createVector(3, 4, 0));
		assertEquals(8, r.getMinX());
		assertEquals(12, r.getMinY());
		assertEquals(18, r.getMaxX());
		assertEquals(17, r.getMaxY());
	}

	@Override
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(3, 4, 0));
		assertEquals(2, this.shape.getMinX());
		assertEquals(4, this.shape.getMinY());
		assertEquals(12, this.shape.getMaxX());
		assertEquals(9, this.shape.getMaxY());
	}

	@Override
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_minus(createVector(3, 4, 0));
		assertEquals(2, r.getMinX());
		assertEquals(4, r.getMinY());
		assertEquals(12, r.getMaxX());
		assertEquals(9, r.getMaxY());
	}

	@Override
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3ai pi;
		
		tr = new Transform3D();
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,8, 0);
		assertElement(pi, PathElementType.LINE_TO, 15,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,13, 0);
		assertElement(pi, PathElementType.CLOSE, 5,8, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f, 0);
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 8,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 18,13, 0);
		assertElement(pi, PathElementType.LINE_TO, 18,18, 0);
		assertElement(pi, PathElementType.LINE_TO, 8,18, 0);
		assertElement(pi, PathElementType.CLOSE, 8,13, 0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(newAxisAngleZ(MathConstants.QUARTER_PI));		
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, -2,9, 0);
		assertElement(pi, PathElementType.LINE_TO, 5,16, 0);
		assertElement(pi, PathElementType.LINE_TO, 1,20, 0);
		assertElement(pi, PathElementType.LINE_TO, -6,13, 0);
		assertElement(pi, PathElementType.CLOSE, -2,9, 0);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.operator_and(createPoint(0,0, 0)));
		assertTrue(this.shape.operator_and(createPoint(11,10, 0)));
		assertFalse(this.shape.operator_and(createPoint(11,50, 0)));
	}

	@Override
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSphere(0,0, 0,100)));
		assertTrue(this.shape.operator_and(createAlignedBox(7,10, 0,1,1, 0)));
	}

	@Override
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(10,10, 0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(4,8, 0)));
		assertEpsilonEquals(9.433981132f, this.shape.operator_upTo(createPoint(0,0, 0)));
	}

}
