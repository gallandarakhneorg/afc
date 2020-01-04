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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.ai.GeomFactory3ai;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;

@SuppressWarnings("all")
public abstract class AbstractSphere3aiTest<T extends Sphere3ai<?, T, ?, ?, ?, B>,
		B extends RectangularPrism3ai<?, ?, ?, ?, ?, B>> extends AbstractShape3aiTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createSphere(5, 8, 0, 5);
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
		assertEpsilonEquals(5, clone.getX());
		assertEpsilonEquals(8, clone.getY());
		assertEpsilonEquals(0, clone.getZ());
		assertEpsilonEquals(5, clone.getRadius());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createSphere(0, 0, 0, 5)));
		assertFalse(this.shape.equals(createSphere(5, 8, 0, 6)));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 6, 10, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createSphere(5, 8, 0, 5)));
	}

	@Override
	public void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(createSphere(0, 0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equals(createSphere(5, 8, 0, 6).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 6, 10, 0).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createSphere(5, 8, 0, 5).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createSphere(0, 0, 0, 5)));
		assertFalse(this.shape.equalsToShape((T) createSphere(5, 8, 0, 6)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createSphere(5, 8, 0, 5)));
	}

	@Override
	public void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToPathIterator((PathIterator3ai) null));
		assertFalse(this.shape.equalsToPathIterator(createSphere(0, 0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSphere(5, 8, 0, 6).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 0, 6, 10, 0).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createSphere(5, 8, 0, 5).getPathIterator()));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		assertEquals(0, this.shape.getX());
		assertEquals(0, this.shape.getY());
		assertEquals(0, this.shape.getZ());
		assertEquals(0, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(3, 4, 0);
		assertEquals(8, this.shape.getX());
		assertEquals(12, this.shape.getY());
		assertEquals(0, this.shape.getZ());
		assertEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		RectangularPrism3ai<?, ?, ?, ?, ?, ?> r1 = this.shape.toBoundingBox();
		assertEquals(0, r1.getMinX());
		assertEquals(3, r1.getMinY());
		assertEquals(-5, r1.getMinZ());
		assertEquals(10, r1.getMaxX());
		assertEquals(13, r1.getMaxY());
		assertEquals(5, r1.getMaxZ());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPointIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Iterator<? extends Point3D> iterator = this.shape.getPointIterator();
		Point3D p;
		
		int[] coords = new int[]{
			// octant 1
			5,13,
			6,13,
			7,13,
			8,12,
			// octant 2
			10,8,
			10,9,
			10,10,
			9,11,
			// octant 3
			5,3,
			6,3,
			7,3,
			8,4,
			// octant 4 (the first point is skipped because already returns in octant 2)
			10,7,
			10,6,
			9,5,
			// octant 5 (the first point is skipped because already returns in octant 4)
			4,3,
			3,3,
			2,4,
			// octant 6 
			0,8,
			0,7,
			0,6,
			1,5,
			// octant 7 (the first point is skipped because already returns in octant 1)
			4,13,
			3,13,
			2,12,
			// octant 8 (the first point is skipped because already returns in octant 5)
			0,9,
			0,10,
			1,11,
		};
		
		for(int i=0; i<coords.length; i+=2) {
			int x = coords[i];
			int y = coords[i+1];
			assertTrue(iterator.hasNext(), "("+x+";"+y+")"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix(), "(>"+x+"<;"+y+")!=("+p.ix()+";"+p.iy()+")"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
			assertEquals(y, p.iy(), "("+x+";>"+y+"<)!=("+p.ix()+";"+p.iy()+")"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		}
		
		assertFalse(iterator.hasNext());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getPointIterator_small(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Sphere3ai<?, ?, ?, ?, ?, ?> circle = createSphere(4, 6, 0, 3);
		Iterator<? extends Point3D> iterator = circle.getPointIterator();
		Point3D p;
		
		int[] coords = new int[]{
			// octant 1
			4,9,
			5,9,
			6,8,
			// octant 2
			7,6,
			7,7,
			// 6,8, skipped because already returned
			// octant 3
			4,3,
			5,3,
			6,4,
			// octant 4 (the first point is skipped because already returns in octant 2)
			7,5,
			// 6,4, skipped because already returned
			// octant 5 (the first point is skipped because already returns in octant 4)
			3,3,
			2,4,
			// octant 6 
			1,6,
			1,5,
			// 2,4, skipped because already returned
			// octant 7 (the first point is skipped because already returns in octant 1)
			3,9,
			2,8,
			// octant 8 (the first point is skipped because already returns in octant 5)
			1,7,
			// 2,8, skipped because already returned
		};
		
		for(int i=0; i<coords.length; i+=2) {
			int x = coords[i];
			int y = coords[i+1];
			assertTrue(iterator.hasNext(), "("+x+";"+y+")"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
			p = iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix(), "(>"+x+"<;"+y+")!=("+p.ix()+";"+p.iy()+")"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
			assertEquals(y, p.iy(), "("+x+";>"+y+"<)!=("+p.ix()+";"+p.iy()+")"); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
		}
		
		assertFalse(iterator.hasNext());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
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
		assertEquals(4, p.ix());
		assertEquals(8, p.iy());
		
		p = this.shape.getClosestPointTo(createPoint(0,0, 0));
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());

		p = this.shape.getClosestPointTo(createPoint(5,14, 0));
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = this.shape.getFarthestPointTo(createPoint(5,8, 0));
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(10,10, 0));
		assertNotNull(p);
		assertEquals(0, p.ix());
		assertEquals(6, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(4,8, 0));
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(6, p.iy());
		
		p = this.shape.getFarthestPointTo(createPoint(0,0, 0));
		assertNotNull(p);
		assertEquals(7, p.ix());
		assertEquals(13, p.iy());

		p = this.shape.getFarthestPointTo(createPoint(5,14, 0));
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());
	}
	
	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void containsIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(0,0, 0));
		assertFalse(this.shape.contains(11,10, 0));
		assertFalse(this.shape.contains(11,50, 0));
		assertFalse(this.shape.contains(9,12, 0));
		assertTrue(this.shape.contains(9,11, 0));
		assertTrue(this.shape.contains(8,12, 0));
		assertTrue(this.shape.contains(3,7, 0));
		assertFalse(this.shape.contains(10,11, 0));
		assertTrue(this.shape.contains(9,10, 0));
		
		this.shape = (T) createSphere(-1,-1, 0,1);
		assertFalse(this.shape.contains(0,0, 0));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void containsRectangularPrism3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createRectangularPrism(0,0, 0,1,1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(0,0, 0,8,1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(0,0, 0,8,6, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(0,0, 0,100,100, 0)));
		assertTrue(this.shape.contains(createRectangularPrism(7,10, 0,1,1, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(16,0, 0,100,100, 0)));
		assertFalse(this.shape.contains(createRectangularPrism(9,11, 0,5,5, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsRectangularPrism3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createRectangularPrism(0,0, 0,1,1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(0,0, 0,8,1, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(0,0, 0,8,6, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(0,0, 0,100,100, 0)));
		assertTrue(this.shape.intersects(createRectangularPrism(7,10, 0,1,1, 0)));
		assertFalse(this.shape.intersects(createRectangularPrism(16,0, 0,100,100, 0)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSegment3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSegment(0,0, 0,1,1, 0)));
		assertFalse(this.shape.intersects(createSegment(0,0, 0,8,1, 0)));
		assertTrue(this.shape.intersects(createSegment(0,0, 0,8,6, 0)));
		assertTrue(this.shape.intersects(createSegment(0,0, 0,100,100, 0)));
		assertTrue(this.shape.intersects(createSegment(7,10, 0,1,1, 0)));
		assertFalse(this.shape.intersects(createSegment(16,0, 0,100,100, 0)));
		assertFalse(this.shape.intersects(createSegment(8,13, 0,10,11, 0)));
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
		this.shape = (T) createSphere(0, 0, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(4, 3, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(2, 2, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(2, 1, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(3, 0, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(-1, -1, 0, 1);
		assertFalse(this.shape.intersects(path));
		this.shape = (T) createSphere(4, -3, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(-3, 4, 0, 1);
		assertFalse(this.shape.intersects(path));
		this.shape = (T) createSphere(6, -5, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(4, 0, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(5, 0, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(6, 2, 0, 1);
		assertTrue(this.shape.intersects(path));
		this.shape = (T) createSphere(-5, 0, 0, 3);
		assertFalse(this.shape.intersects(path));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void intersectsSphere3ai(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSphere(0,0, 0,1)));
		assertTrue(this.shape.intersects(createSphere(0,0, 0,8)));
		assertTrue(this.shape.intersects(createSphere(0,0, 0,100)));
		assertTrue(this.shape.intersects(createSphere(7,10, 0,1)));
		assertFalse(this.shape.intersects(createSphere(16,0, 0,5)));
		assertFalse(this.shape.intersects(createSphere(5,15, 0,1)));
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3ai<?> pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10,8);
		assertElement(pi, PathElementType.CURVE_TO, 10,10, 7,13, 5,13);
		assertElement(pi, PathElementType.CURVE_TO, 2,13, 0,10, 0,8);
		assertElement(pi, PathElementType.CURVE_TO, 0,5, 2,3, 5,3);
		assertElement(pi, PathElementType.CURVE_TO, 7,3, 10,5, 10,8);
		assertElement(pi, PathElementType.CLOSE, 10, 8);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3ai<?> pi;
		
		tr = new Transform3D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 10,8,0);
		assertElement(pi, PathElementType.CURVE_TO, 10,10,0, 7,13,0, 5,13,0);
		assertElement(pi, PathElementType.CURVE_TO, 2,13,0, 0,10,0, 0,8,0);
		assertElement(pi, PathElementType.CURVE_TO, 0,5,0, 2,3,0, 5,3,0);
		assertElement(pi, PathElementType.CURVE_TO, 7,3,0, 10,5,0, 10,8,0);
		assertElement(pi, PathElementType.CLOSE, 10,8,0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f, 0);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 13,13,0);
		assertElement(pi, PathElementType.CURVE_TO, 13,16,0, 11,18,0, 8,18,0);
		assertElement(pi, PathElementType.CURVE_TO, 5,18,0, 3,16,0, 3,13,0);
		assertElement(pi, PathElementType.CURVE_TO, 3,10,0, 5,8,0, 8,8,0);
		assertElement(pi, PathElementType.CURVE_TO, 11,8,0, 13,10,0, 13,13,0);
		assertElement(pi, PathElementType.CLOSE, 13,13,0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 1,13,0);
		assertElement(pi, PathElementType.CURVE_TO, -1,15,0, -4,15,0, -6,13,0);
		assertElement(pi, PathElementType.CURVE_TO, -8,11,0, -8,8,0, -6,6,0);
		assertElement(pi, PathElementType.CURVE_TO, -4,4,0, -1,4,0, 1,6,0);
		assertElement(pi, PathElementType.CURVE_TO, 4,8,0, 4,11,0, 1,13,0);
		assertElement(pi, PathElementType.CLOSE, 1,13,0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void createTransformedShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3ai<?> pi;
		
		tr = new Transform3D();
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10,8,0);
		assertElement(pi, PathElementType.CURVE_TO, 10,10,0, 7,13,0, 5,13,0);
		assertElement(pi, PathElementType.CURVE_TO, 2,13,0, 0,10,0, 0,8,0);
		assertElement(pi, PathElementType.CURVE_TO, 0,5,0, 2,3,0, 5,3,0);
		assertElement(pi, PathElementType.CURVE_TO, 7,3,0, 10,5,0, 10,8,0);
		assertElement(pi, PathElementType.CLOSE, 10,8,0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f,0);
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 13,13,0);
		assertElement(pi, PathElementType.CURVE_TO, 13,16,0, 11,18,0, 8,18,0);
		assertElement(pi, PathElementType.CURVE_TO, 5,18,0, 3,16,0, 3,13,0);
		assertElement(pi, PathElementType.CURVE_TO, 3,10,0, 5,8,0, 8,8,0);
		assertElement(pi, PathElementType.CURVE_TO, 11,8,0, 13,10,0, 13,13,0);
		assertElement(pi, PathElementType.CLOSE, 13,13,0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 1,13,0);
		assertElement(pi, PathElementType.CURVE_TO, -1,15,0, -4,15,0, -6,13,0);
		assertElement(pi, PathElementType.CURVE_TO, -8,11,0, -8,8,0, -6,6,0);
		assertElement(pi, PathElementType.CURVE_TO, -4,4,0, -1,4,0, 1,6,0);
		assertElement(pi, PathElementType.CURVE_TO, 4,8,0, 4,11,0, 1,13,0);
		assertElement(pi, PathElementType.CLOSE, 1,13,0);
		assertNoElement(pi);
	}

	@Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createSphere(17, 20, 8, 7));
		assertEquals(17, this.shape.getX());
		assertEquals(20, this.shape.getY());
		assertEquals(8, this.shape.getZ());
		assertEquals(7, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.contains(createPoint(0,0, 0)));
		assertFalse(this.shape.contains(createPoint(11,10, 0)));
		assertFalse(this.shape.contains(createPoint(11,50, 0)));
		assertFalse(this.shape.contains(createPoint(9,12, 0)));
		assertTrue(this.shape.contains(createPoint(9,11, 0)));
		assertTrue(this.shape.contains(createPoint(8,12, 0)));
		assertTrue(this.shape.contains(createPoint(3,7, 0)));
		assertFalse(this.shape.contains(createPoint(10,11, 0)));
		assertTrue(this.shape.contains(createPoint(9,10, 0)));
		
		this.shape = (T) createSphere(-1,-1, 0,1);
		assertFalse(this.shape.contains(createPoint(0,0, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistance(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(10,10, 0)));
		assertEpsilonEquals(0f, this.shape.getDistance(createPoint(4,8, 0)));
		assertEpsilonEquals(4.242640687f, this.shape.getDistance(createPoint(0,0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistance(createPoint(5,14, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(10,10, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(4,8, 0)));
		assertEpsilonEquals(18f, this.shape.getDistanceSquared(createPoint(0,0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(5,14, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(10,10, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(4,8, 0)));
		assertEpsilonEquals(6f, this.shape.getDistanceL1(createPoint(0,0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(5,14, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(5,8, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(10,10, 0)));
		assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(4,8, 0)));
		assertEpsilonEquals(3f, this.shape.getDistanceLinf(createPoint(0,0, 0)));
		assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(5,14, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B r = createRectangularPrism(0, 0, 0, 0, 0, 0);
		this.shape.toBoundingBox(r);
		assertEquals(0, r.getMinX());
		assertEquals(3, r.getMinY());
		assertEquals(10, r.getMaxX());
		assertEquals(13, r.getMaxY());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.translate(createVector(3, 4, 0));
		assertEquals(8, this.shape.getX());
		assertEquals(12, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeClosestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = createPoint(0, 0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 5, 0, 5, 8, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 0, 5, 10, 10, 0, p);
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(10, p.iy());
		
		p = createPoint(0, 0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 0, 5, 4, 8, 0, p);
		assertNotNull(p);
		assertEquals(4, p.ix());
		assertEquals(8, p.iy());
		
		p = createPoint(0, 0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 0, 5, 0, 0, 0, p);
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());

		p = createPoint(0, 0, 0);
		Sphere3ai.computeClosestPointTo(5, 8, 0, 5, 5, 14, 0, p);
		assertNotNull(p);
		assertEquals(5, p.ix());
		assertEquals(13, p.iy());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticComputeFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p;
		
		p = createPoint(0, 0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 0, 5, 5, 8, 0, p);
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());
		
		p = createPoint(0, 0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 0, 5, 10, 10, 0, p);
		assertNotNull(p);
		assertEquals(0, p.ix());
		assertEquals(6, p.iy());
		
		p = createPoint(0, 0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 0, 5, 4, 8, 0, p);
		assertNotNull(p);
		assertEquals(10, p.ix());
		assertEquals(6, p.iy());
		
		p = createPoint(0, 0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 0, 5, 0, 0, 0, p);
		assertNotNull(p);
		assertEquals(7, p.ix());
		assertEquals(13, p.iy());

		p = createPoint(0, 0, 0);
		Sphere3ai.computeFarthestPointTo(5, 8, 0, 5, 5, 14, 0, p);
		assertNotNull(p);
		assertEquals(3, p.ix());
		assertEquals(3, p.iy());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticContainsIntIntIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 0, 0, 0));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 11, 10, 0));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 11, 50, 0));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 9, 12, 0));
		assertTrue(Sphere3ai.contains(5, 8, 0, 5, 9, 11, 0));
		assertTrue(Sphere3ai.contains(5, 8, 0, 5, 8, 12, 0));
		assertTrue(Sphere3ai.contains(5, 8, 0, 5, 3, 7, 0));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 10, 11, 0));
		assertTrue(Sphere3ai.contains(5, 8, 0, 5, 9, 10, 0));
		assertFalse(Sphere3ai.contains(-1, -1, 0, 1, 0, 0, 0));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public void staticContainsIntIntIntIntIntInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(Sphere3ai.contains(5, 8, 0, 5, 0, 9, 11));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 1, 9, 11));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 2, 9, 11));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 3, 9, 11));
		
		assertTrue(Sphere3ai.contains(5, 8, 0, 5, 0, 8, 12));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 1, 8, 12));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 2, 8, 12));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 3, 8, 12));
		
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 0, 3, 7));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 1, 3, 7));
		assertTrue(Sphere3ai.contains(5, 8, 0, 5, 2, 3, 7));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 3, 3, 7));

		assertTrue(Sphere3ai.contains(5, 8, 0, 5, 0, 9, 10));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 1, 9, 10));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 2, 9, 10));
		assertFalse(Sphere3ai.contains(5, 8, 0, 5, 3, 9, 10));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticGetPointIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Iterator iterator = Sphere3ai.getPointIterator(5, 8, 0, 5, 0, 8, (GeomFactory3ai) this.shape.getGeomFactory());
		Point3D p;
		
		int[] coords = new int[] {
				5, 13,
				6, 13,
				7, 13,
				8, 12,
				10, 8,
				10, 9,
				10, 10,
				9, 11,
				5, 3,
				6, 3,
				7, 3,
				8, 4,
				10, 7,
				10, 6,
				9, 5,
				4, 3,
				3, 3,
				2, 4,
				0, 8,
				0, 7,
				0, 6,
				1, 5,
				4, 13,
				3, 13,
				2, 12,
				0, 9,
				0, 10,
				1, 11,
		};
		
		for(int i = 0; i < coords.length; i += 2) {
			int x = coords[i];
			int y = coords[i + 1];
			assertTrue(iterator.hasNext());
			p = (Point3D) iterator.next();
			assertNotNull(p);
			assertEquals(x, p.ix());
			assertEquals(y, p.iy());
		}

		assertFalse(iterator.hasNext());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticintersectsSphereSphere(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(Sphere3ai.intersectsSphereSphere(5, 8, 0, 5, 0, 0, 0, 1));
		assertTrue(Sphere3ai.intersectsSphereSphere(5, 8, 0, 5, 0, 0, 0, 8));
		assertTrue(Sphere3ai.intersectsSphereSphere(5, 8, 0, 5, 0, 0, 0, 100));
		assertTrue(Sphere3ai.intersectsSphereSphere(5, 8, 0, 5, 7, 10, 0, 1));
		assertFalse(Sphere3ai.intersectsSphereSphere(5, 8, 0, 5, 16, 0, 0, 5));
		assertFalse(Sphere3ai.intersectsSphereSphere(5, 8, 0, 5, 5, 15, 0, 1));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticintersectsSphereRectangularPrism(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(Sphere3ai.intersectsSphereRectangularPrism(0, 0, 0, 1, 5, 8, 0, 15, 13, 0));
		assertFalse(Sphere3ai.intersectsSphereRectangularPrism(0, 0, 0, 8, 5, 8, 0, 15, 13, 0));
		assertTrue(Sphere3ai.intersectsSphereRectangularPrism(0, 0, 0, 100, 5, 8, 0, 15, 13, 0));
		assertTrue(Sphere3ai.intersectsSphereRectangularPrism(7, 10, 0, 1, 5, 8, 0, 15, 13, 0));
		assertFalse(Sphere3ai.intersectsSphereRectangularPrism(16, 0, 0, 5, 5, 8, 0, 15, 13, 0));
		assertFalse(Sphere3ai.intersectsSphereRectangularPrism(5, 15, 0, 1, 5, 8, 0, 15, 13, 0));
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void staticintersectsSphereSegment(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(Sphere3ai.intersectsSphereSegment(5, 8, 0, 5, 0, 0, 0, 1, 1, 0));
		assertFalse(Sphere3ai.intersectsSphereSegment(5, 8, 0, 5, 0, 0, 0, 8, 1, 0));
		assertTrue(Sphere3ai.intersectsSphereSegment(5, 8, 0, 5, 0, 0, 0, 8, 6, 0));
		assertTrue(Sphere3ai.intersectsSphereSegment(5, 8, 0, 5, 0, 0, 0, 100, 100, 0));
		assertTrue(Sphere3ai.intersectsSphereSegment(5, 8, 0, 5, 7, 10, 0, 1, 1, 0));
		assertFalse(Sphere3ai.intersectsSphereSegment(5, 8, 0, 5, 16, 0, 0, 100, 100, 0));
		assertFalse(Sphere3ai.intersectsSphereSegment(5, 8, 0, 5, 8, 13, 0, 10, 11, 0));
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
		this.shape = (T) createSphere(0, 0, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(4, 3, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(2, 2, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(2, 1, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(3, 0, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(-1, -1, 0, 1);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(4, -3, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(-3, 4, 0, 1);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(6, -5, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(4, 0, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(5, 0, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(6, 2, 0, 1);
		assertTrue(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
		this.shape = (T) createSphere(-5, 0, 0, 3);
		assertFalse(this.shape.intersects((PathIterator3ai) path.getPathIterator()));
	}

	@Override
	public void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSphere(0,0, 0,100)));
		assertTrue(this.shape.intersects((Shape3D) createRectangularPrism(0,0, 0,100,100, 0)));
	}

	@Override
	public void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_add(createVector(3, 4, 0));
		assertEquals(8, this.shape.getX());
		assertEquals(12, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
	}

	@Override
	public void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_plus(createVector(3, 4, 0));
		assertEquals(8, r.getX());
		assertEquals(12, r.getY());
		assertEquals(5, r.getRadius());
	}

	@Override
	public void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.operator_remove(createVector(3, 4, 0));
		assertEquals(2, this.shape.getX());
		assertEquals(4, this.shape.getY());
		assertEquals(5, this.shape.getRadius());
	}

	@Override
	public void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		T r = this.shape.operator_minus(createVector(3, 4, 0));
		assertEquals(2, r.getX());
		assertEquals(4, r.getY());
		assertEquals(5, r.getRadius());
	}

	@Override
	public void operator_multiplyTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D tr;
		PathIterator3ai<?> pi;
		
		tr = new Transform3D();
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10,8, 0);
		assertElement(pi, PathElementType.CURVE_TO, 10,10,0, 7,13,0, 5,13,0);
		assertElement(pi, PathElementType.CURVE_TO, 2,13,0, 0,10,0, 0,8,0);
		assertElement(pi, PathElementType.CURVE_TO, 0,5,0, 2,3,0, 5,3,0);
		assertElement(pi, PathElementType.CURVE_TO, 7,3,0, 10,5,0, 10,8,0);
		assertElement(pi, PathElementType.CLOSE, 10,8,0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeTranslationMatrix(3.4f, 4.5f,0);
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 13,13,0);
		assertElement(pi, PathElementType.CURVE_TO, 13,16,0, 11,18,0, 8,18,0);
		assertElement(pi, PathElementType.CURVE_TO, 5,18,0, 3,16,0, 3,13,0);
		assertElement(pi, PathElementType.CURVE_TO, 3,10,0, 5,8,0, 8,8,0);
		assertElement(pi, PathElementType.CURVE_TO, 11,8,0, 13,10,0, 13,13,0);
		assertElement(pi, PathElementType.CLOSE, 13,13,0);
		assertNoElement(pi);

		tr = new Transform3D();
		tr.makeRotationMatrix(MathConstants.QUARTER_PI);
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 1,13,0);
		assertElement(pi, PathElementType.CURVE_TO, -1,15,0, -4,15,0, -6,13,0);
		assertElement(pi, PathElementType.CURVE_TO, -8,11,0, -8,8,0, -6,6,0);
		assertElement(pi, PathElementType.CURVE_TO, -4,4,0, -1,4,0, 1,6,0);
		assertElement(pi, PathElementType.CURVE_TO, 4,8,0, 4,11,0, 1,13,0);
		assertElement(pi, PathElementType.CLOSE, 1,13,0);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.operator_and(createPoint(0,0,0)));
		assertFalse(this.shape.operator_and(createPoint(11,10,0)));
		assertFalse(this.shape.operator_and(createPoint(11,50,0)));
		assertFalse(this.shape.operator_and(createPoint(9,12,0)));
		assertTrue(this.shape.operator_and(createPoint(9,11,0)));
		assertTrue(this.shape.operator_and(createPoint(8,12,0)));
		assertTrue(this.shape.operator_and(createPoint(3,7,0)));
		assertFalse(this.shape.operator_and(createPoint(10,11,0)));
		assertTrue(this.shape.operator_and(createPoint(9,10,0)));
	}

	@Override
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSphere(0,0,0,100)));
		assertTrue(this.shape.operator_and(createRectangularPrism(0,0,0,100,100,0)));
	}

	@Override
	public void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(5,8,0)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(10,10,0)));
		assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(4,8,0)));
		assertEpsilonEquals(4.242640687f, this.shape.operator_upTo(createPoint(0,0,0)));
		assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(5,14,0)));
	}

}
