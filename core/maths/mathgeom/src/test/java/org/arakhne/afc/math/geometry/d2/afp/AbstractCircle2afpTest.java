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

package org.arakhne.afc.math.geometry.d2.afp;

import org.junit.Test;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;

@SuppressWarnings("all")
public abstract class AbstractCircle2afpTest<T extends Circle2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractShape2afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createCircle(5, 8, 5);
	}
	
	@Test
	@Override
	public void testClone() {
		T clone = this.shape.clone();
		assertNotNull(clone);
		assertNotSame(this.shape, clone);
		assertEquals(this.shape.getClass(), clone.getClass());
		assertEpsilonEquals(5, clone.getX());
		assertEpsilonEquals(8, clone.getY());
		assertEpsilonEquals(5, clone.getRadius());
	}

	@Test
	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createCircle(0, 0, 5)));
		assertFalse(this.shape.equals(createCircle(5, 8, 6)));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createCircle(5, 8, 5)));
	}

	@Test
	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createCircle(0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equals(createCircle(5, 8, 6).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createCircle(5, 8, 5).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createCircle(0, 0, 5).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createCircle(5, 8, 6).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 6, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createCircle(5, 8, 5).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createCircle(0, 0, 5)));
		assertFalse(this.shape.equalsToShape((T) createCircle(5, 8, 6)));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape((T) createCircle(5, 8, 5)));
	}

	@Test
	@Override
	public void isEmpty() {
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@Test
	@Override
	public void clear() {
		this.shape.clear();
		assertEpsilonEquals(0, this.shape.getX());
		assertEpsilonEquals(0, this.shape.getY());
		assertEpsilonEquals(0, this.shape.getRadius());
	}

	@Test
	@Override
	public void containsDoubleDouble() {
		assertFalse(this.shape.contains(0,0));
		assertFalse(this.shape.contains(11,10));
		assertFalse(this.shape.contains(11,50));
		assertFalse(this.shape.contains(9,12));
		assertTrue(this.shape.contains(9,11));
		assertTrue(this.shape.contains(8,12));
		assertTrue(this.shape.contains(3,7));
		assertFalse(this.shape.contains(10,11));
		assertTrue(this.shape.contains(9,10));
		
		this.shape = (T) createCircle(-1,-1,1);
		assertFalse(this.shape.contains(0,0));
	}

	@Test
	@Override
	public void containsPoint2D() {
		assertFalse(this.shape.contains(createPoint(0,0)));
		assertFalse(this.shape.contains(createPoint(11,10)));
		assertFalse(this.shape.contains(createPoint(11,50)));
		assertFalse(this.shape.contains(createPoint(9,12)));
		assertTrue(this.shape.contains(createPoint(9,11)));
		assertTrue(this.shape.contains(createPoint(8,12)));
		assertTrue(this.shape.contains(createPoint(3,7)));
		assertFalse(this.shape.contains(createPoint(10,11)));
		assertTrue(this.shape.contains(createPoint(9,10)));
		
		this.shape = (T) createCircle(-1,-1,1);
		assertFalse(this.shape.contains(createPoint(0,0)));
	}

	@Test
	@Override
	public void getClosestPointTo() {
		Point2D p;
		
		p = this.shape.getClosestPointTo(createPoint(5,8));
		assertNotNull(p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8, p.getY());
		
		p = this.shape.getClosestPointTo(createPoint(10,10));
		assertNotNull(p);
		assertEpsilonEquals(9.6424, p.getX());
		assertEpsilonEquals(9.8570, p.getY());
		
		p = this.shape.getClosestPointTo(createPoint(4,8));
		assertNotNull(p);
		assertEpsilonEquals(4, p.getX());
		assertEpsilonEquals(8, p.getY());
		
		p = this.shape.getClosestPointTo(createPoint(0,0));
		assertNotNull(p);
		assertEpsilonEquals(2.35, p.getX());
		assertEpsilonEquals(3.76, p.getY());

		p = this.shape.getClosestPointTo(createPoint(5,14));
		assertNotNull(p);
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(13, p.getY());
	}

	@Override
	public void getFarthestPointTo() {
		Point2D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEpsilonEquals(7.65, p.getX());
		assertEpsilonEquals(12.24, p.getY());
		
		p = this.shape.getFarthestPointTo(createPoint(.5, .1));
		assertEpsilonEquals(7.4748, p.getX());
		assertEpsilonEquals(12.3446, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(-1.2,-3.4));
		assertEpsilonEquals(7.3889, p.getX());
		assertEpsilonEquals(12.3924, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(-1.2,5.6));
		assertEpsilonEquals(9.6628, p.getX());
		assertEpsilonEquals(9.8050, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7.6,5.6));
		assertEpsilonEquals(1.326, p.getX());
		assertEpsilonEquals(11.3914, p.getY());
	}

	@Override
	public void getDistance() {
		double d;
		d = this.shape.getDistance(createPoint(.5,.5));
		assertEpsilonEquals(3.74643,d);

		d = this.shape.getDistance(createPoint(-1.2,-3.4));
		assertEpsilonEquals(7.9769,d);

		d = this.shape.getDistance(createPoint(-1.2,5.6));
		assertEpsilonEquals(1.6483,d);

		d = this.shape.getDistance(createPoint(7.6,5.6));
		assertEpsilonEquals(0,d);
	}

	@Override
	public void getDistanceSquared() {
		double d;
		d = this.shape.getDistanceSquared(createPoint(.5,.5));
		assertEpsilonEquals(14.03572,d);

		d = this.shape.getDistanceSquared(createPoint(-1.2,-3.4));
		assertEpsilonEquals(63.631,d);

		d = this.shape.getDistanceSquared(createPoint(-1.2,5.6));
		assertEpsilonEquals(2.7169,d);

		d = this.shape.getDistanceSquared(createPoint(7.6,5.6));
		assertEpsilonEquals(0,d);
	}

	@Override
	public void getDistanceL1() {
		double d;
		d = this.shape.getDistanceL1(createPoint(.5,.5));
		assertEpsilonEquals(5.14005,d);

		d = this.shape.getDistanceL1(createPoint(-1.2,-3.4));
		assertEpsilonEquals(10.81872,d);

		d = this.shape.getDistanceL1(createPoint(-1.2,5.6));
		assertEpsilonEquals(2.1322,d);

		d = this.shape.getDistanceL1(createPoint(7.6,5.6));
		assertEpsilonEquals(0,d);
	}

	@Override
	public void getDistanceLinf() {
		double d;
		d = this.shape.getDistanceLinf(createPoint(.5,.5));
		assertEpsilonEquals(3.2125,d);

		d = this.shape.getDistanceLinf(createPoint(-1.2,-3.4));
		assertEpsilonEquals(7.0076,d);

		d = this.shape.getDistanceLinf(createPoint(-1.2,5.6));
		assertEpsilonEquals(1.53716,d);

		d = this.shape.getDistanceLinf(createPoint(7.6,5.6));
		assertEpsilonEquals(0,d);
	}

	@Override
	public void setIT() {
		this.shape.set((T) createCircle(17, 20, 7));
		assertEpsilonEquals(17, this.shape.getX());
		assertEpsilonEquals(20, this.shape.getY());
		assertEpsilonEquals(7, this.shape.getRadius());
	}

	@Override
	public void translateDoubleDouble() {
		this.shape.translate(123.456, -789.123);
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	public void translateVector2D() {
		this.shape.translate(createVector(123.456, -789.123));
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	public void toBoundingBox() {
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(3, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
	}

	@Override
	public void toBoundingBoxB() {
		B box = createRectangle(0, 0, 0, 0);
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(3, box.getMinY());
		assertEpsilonEquals(10, box.getMaxX());
		assertEpsilonEquals(13, box.getMaxY());
	}

	@Override
	public void getPathIteratorTransform2D() {
		PathIterator2afp pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 10, 8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 7.761423749153966, 13, 5, 13);
		assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 10.76142374915397, 0, 8);
		assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 5, 3);
		assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 10, 5.238576250846034, 10, 8);
		assertElement(pi, PathElementType.CLOSE, 10, 8);
		assertNoElement(pi);

		Transform2D tr;
		
		tr = new Transform2D();
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 10, 8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 7.761423749153966, 13, 5, 13);
		assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 10.76142374915397, 0, 8);
		assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 5, 3);
		assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 10, 5.238576250846034, 10, 8);
		assertElement(pi, PathElementType.CLOSE, 10, 8);
		assertNoElement(pi);

		tr = new Transform2D();
		tr.makeTranslationMatrix(10, -10);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, -2);
		assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 17.761423749153966, 3, 15, 3);
		assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 10, 0.76142374915397, 10, -2);
		assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 12.238576250846033, -7, 15, -7);
		assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 20, -4.761423749153966, 20, -2);
		assertElement(pi, PathElementType.CLOSE, 20, -2);
		assertNoElement(pi);
	}

	@Override
	public void getPathIterator() {
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 10, 8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 10.76142374915397, 7.761423749153966, 13, 5, 13);
		assertElement(pi, PathElementType.CURVE_TO, 2.238576250846033, 13, 0, 10.76142374915397, 0, 8);
		assertElement(pi, PathElementType.CURVE_TO, 0, 5.238576250846034, 2.238576250846033, 3, 5, 3);
		assertElement(pi, PathElementType.CURVE_TO, 7.761423749153966, 3, 10, 5.238576250846034, 10, 8);
		assertElement(pi, PathElementType.CLOSE, 10, 8);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape() {
		Transform2D tr;
		Shape2afp newShape;
		
		newShape = this.shape.createTransformedShape(null);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform2D();
		newShape = this.shape.createTransformedShape(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform2D();
		tr.makeTranslationMatrix(10, -10);
		newShape = this.shape.createTransformedShape(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertTrue(newShape instanceof Path2afp);
		PathIterator2afp pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, -2);
		assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 17.761423749153966, 3, 15, 3);
		assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 10, 0.76142374915397, 10, -2);
		assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 12.238576250846033, -7, 15, -7);
		assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 20, -4.761423749153966, 20, -2);
		assertElement(pi, PathElementType.CLOSE, 20, -2);
		assertNoElement(pi);
	}

	@Override
	public void containsRectangle2afp() {
		assertFalse(this.shape.contains(createRectangle(-4, -4, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(-5, -5, 10, 10)));
		assertFalse(this.shape.contains(createRectangle(-5, -5, 5.5, 5.5)));
		assertFalse(this.shape.contains(createRectangle(-5, -4, 5.5, 1)));
		assertFalse(this.shape.contains(createRectangle(20, .5, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(-5, -5, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(-1, -100, 1, 200)));
		assertFalse(this.shape.contains(createRectangle(-1, -100, 1.0001, 200)));
		assertFalse(this.shape.contains(createRectangle(-1, 2, 1.0001, 1.0001)));
		assertTrue(this.shape.contains(createRectangle(2, 4, 6, 4)));
	}

	@Override
	public void containsShape2D() {
		assertFalse(this.shape.contains(createCircle(-4, -4, 1)));
		assertFalse(this.shape.contains(createCircle(-5, -5, 10)));
		assertFalse(this.shape.contains(createCircle(-5, -5, 5.5)));
		assertFalse(this.shape.contains(createCircle(-5, -4, 5.5)));
		assertFalse(this.shape.contains(createCircle(20, .5, 1)));
		assertFalse(this.shape.contains(createCircle(-5, -5, 1)));
		assertFalse(this.shape.contains(createCircle(-1, -100, 1)));
		assertFalse(this.shape.contains(createCircle(-1, -100, 1.0001)));
		assertFalse(this.shape.contains(createCircle(-1, 2, 1.0001)));
		assertFalse(this.shape.contains(createCircle(2, 4, 6)));
		assertTrue(this.shape.contains(createCircle(4, 8, 1)));
	}

	@Override
	public void intersectsRectangle2afp() {
		assertFalse(this.shape.intersects(createRectangle(-4, -4, 1, 1)));
		assertTrue(this.shape.intersects(createRectangle(-5, -5, 10, 10)));
		assertFalse(this.shape.intersects(createRectangle(-5, -5, 5.5, 5.5)));
		assertFalse(this.shape.intersects(createRectangle(-5, -4, 5.5, 1)));
		assertFalse(this.shape.intersects(createRectangle(20, .5, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(-5, -5, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(-1, -100, 1, 200)));
		assertTrue(this.shape.intersects(createRectangle(-1, -100, 1.0001, 200)));
		assertFalse(this.shape.intersects(createRectangle(-1, 2, 1.0001, 1.0001)));
	}

	@Override
	public void intersectsTriangle2afp() {
		Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
		assertTrue(createCircle(5, 8, 1).intersects(triangle));
		assertTrue(createCircle(-10, 1, 1).intersects(triangle));
		assertTrue(createCircle(-1, -2, 1).intersects(triangle));
		
		assertFalse(createCircle(2, 0, 1).intersects(triangle));
		assertFalse(createCircle(1.9, 0, 1).intersects(triangle));
		assertFalse(createCircle(1.8, 0, 1).intersects(triangle));
		assertFalse(createCircle(1.7, 0, 1).intersects(triangle));
		assertFalse(createCircle(1.6, 0, 1).intersects(triangle));
		assertFalse(createCircle(1.5, 0, 1).intersects(triangle));
		assertFalse(createCircle(1.4, 0, 1).intersects(triangle));
		assertTrue(createCircle(1.3, 0, 1).intersects(triangle));

		assertFalse(createCircle(5, 9, 1).intersects(triangle));
		assertTrue(createCircle(5, 8.9, 1).intersects(triangle));

		assertTrue(createCircle(-4, 1, 1).intersects(triangle));
	}

	@Test
	@Override
	public void intersectsOrientedRectangle2afp() {
		assertTrue(this.shape.intersects(createOrientedRectangle(
				// Center
				4.518, 7.166,
				// Axis 1
				0.89669, 0.44267,
				// Extent 1
				1.93825,
				// Extent 2
				1.35546)));
		assertFalse(this.shape.intersects(createOrientedRectangle(
				// Center
				9.886, 3.316,
				// Axis 1
				0.79028, 0.61275,
				// Extent 1
				3.84169,
				// Extent 2
				1.43961)));
		assertTrue(this.shape.intersects(createOrientedRectangle(
				// Center
				4.518, 7.166,
				// Axis 1
				0.89669, 0.44267,
				// Extent 1
				1.93825,
				// Extent 2
				1.35546)));
		assertTrue(this.shape.intersects(createOrientedRectangle(
				// Center
				10.216, 5.23,
				// Axis 1
				0.27204, 0.96229,
				// Extent 1
				5.41835,
				// Extent 2
				1.76987)));
	}

	@Test
	@Override
	public void intersectsParallelogram2afp() {
		Parallelogram2afp para = createParallelogram(
				6, 9,
				2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
				-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
		assertFalse(createCircle(.5, .5, .5).intersects(para));
		assertFalse(createCircle(.5, 1.5, .5).intersects(para));
		assertFalse(createCircle(.5, 2.5, .5).intersects(para));
		assertTrue(createCircle(.5, 3.5, .5).intersects(para));
		assertTrue(createCircle(4.5, 3.5, .5).intersects(para));
		assertFalse(createCircle(10, -7, .5).intersects(para));
		assertFalse(createCircle(10.1, -7, .5).intersects(para));
		assertTrue(createCircle(10.2, -7, .5).intersects(para));
		assertTrue(createCircle(10, -1, 5).intersects(para));
	}
	
	@Test
	@Override
	public void intersectsRoundRectangle2afp() {
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 1, 1, .2, .4)));
		assertFalse(this.shape.intersects(createRoundRectangle(0, 2, 1, 1, .2, .4)));
		assertFalse(this.shape.intersects(createRoundRectangle(0, 3, 1, 1, .2, .4)));
		assertFalse(this.shape.intersects(createRoundRectangle(0, 4, 1, 1, .2, .4)));
		assertFalse(this.shape.intersects(createRoundRectangle(0.1, 4, 1, 1, .2, .4)));
		assertTrue(this.shape.intersects(createRoundRectangle(0.2, 4, 1, 1, .2, .4)));
		assertTrue(this.shape.intersects(createRoundRectangle(0.3, 4, 1, 1, .2, .4)));
		assertTrue(this.shape.intersects(createRoundRectangle(0.4, 4, 1, 1, .2, .4)));
		assertTrue(this.shape.intersects(createRoundRectangle(0.5, 4, 1, 1, .2, .4)));
	}

	@Override
	public void intersectsCircle2afp() {
		assertTrue(this.shape.intersects(createCircle(10, 10, 1)));
		assertFalse(this.shape.intersects(createCircle(0, 0, 1)));
		assertFalse(this.shape.intersects(createCircle(0, .5, 1)));
		assertFalse(this.shape.intersects(createCircle(.5, 0, 1)));
		assertFalse(this.shape.intersects(createCircle(.5, .5, 1)));
		assertFalse(this.shape.intersects(createCircle(2, 0, 1)));
		assertFalse(this.shape.intersects(createCircle(12, 8, 2)));
		assertTrue(this.shape.intersects(createCircle(12, 8, 2.1)));
	}

	@Override
	public void intersectsEllipse2afp() {
		assertTrue(this.shape.intersects(createEllipse(9, 9, 2, 2)));
		assertFalse(this.shape.intersects(createEllipse(-1, -1, 2, 2)));
		assertFalse(this.shape.intersects(createEllipse(-1, -.5, 2, 2)));
		assertFalse(this.shape.intersects(createEllipse(-.5, -1, 2, 2)));
		assertFalse(this.shape.intersects(createEllipse(-.5, -.5, 2, 2)));
		assertFalse(this.shape.intersects(createEllipse(1, -1, 2, 2)));
		assertFalse(this.shape.intersects(createEllipse(10, 6, 4, 4)));
		assertTrue(this.shape.intersects(createEllipse(9.9, 5.9, 4.2, 4.2)));
	}

	@Override
	public void intersectsSegment2afp() {
		assertTrue(this.shape.intersects(createSegment(2, 10, 6, 5)));
		assertTrue(this.shape.intersects(createSegment(2, 10, 8, 14)));
		assertTrue(this.shape.intersects(createSegment(0, 4, 8, 14)));
		assertFalse(this.shape.intersects(createSegment(0, 4, 0, 6)));
		assertFalse(this.shape.intersects(createSegment(0, 4, 0, 12)));
		assertFalse(this.shape.intersects(createSegment(5, 0, 0, 6)));
	}

	@Override
	public void intersectsPath2afp() {
		Path2afp path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, -2);
		path.lineTo(-2, 2);
		path.lineTo(2, 2);
		path.lineTo(2, -2);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
		
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 8);
		path.lineTo(0, 14);
		path.lineTo(10, 14);
		path.lineTo(10, 8);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2);
		path.lineTo(12, 14);
		path.lineTo(0, 14);
		assertTrue(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2);
		path.lineTo(-2, 14);
		path.lineTo(12, 14);
		path.lineTo(12, 2);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 0);
		path.lineTo(0, 4);
		path.lineTo(14, 0);
		path.lineTo(14, 4);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-8, -7);
		path.lineTo(24, 14);
		path.lineTo(-16, 14);
		path.lineTo(20, -7);
		path.lineTo(5, 21);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		// EVENB ODD
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, -2);
		path.lineTo(-2, 2);
		path.lineTo(2, 2);
		path.lineTo(2, -2);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 8);
		path.lineTo(0, 14);
		path.lineTo(10, 14);
		path.lineTo(10, 8);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2);
		path.lineTo(12, 14);
		path.lineTo(0, 14);
		assertTrue(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(-2, 14);
		path.lineTo(12, 14);
		path.lineTo(12, 2);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertTrue(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 0);
		path.lineTo(0, 4);
		path.lineTo(14, 0);
		path.lineTo(14, 4);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-8, -7);
		path.lineTo(24, 14);
		path.lineTo(-16, 14);
		path.lineTo(20, -7);
		path.lineTo(5, 21);
		assertFalse(this.shape.intersects(path));
		path.closePath();
		assertFalse(this.shape.intersects(path));
	}

	@Override
	public void intersectsPathIterator2afp() {
		Path2afp path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, -2);
		path.lineTo(-2, 2);
		path.lineTo(2, 2);
		path.lineTo(2, -2);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		
		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 8);
		path.lineTo(0, 14);
		path.lineTo(10, 14);
		path.lineTo(10, 8);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 2);
		path.lineTo(12, 14);
		path.lineTo(0, 14);
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-2, 2);
		path.lineTo(-2, 14);
		path.lineTo(12, 14);
		path.lineTo(12, 2);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(0, 0);
		path.lineTo(0, 4);
		path.lineTo(14, 0);
		path.lineTo(14, 4);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		path = createPath(PathWindingRule.NON_ZERO);
		path.moveTo(-8, -7);
		path.lineTo(24, 14);
		path.lineTo(-16, 14);
		path.lineTo(20, -7);
		path.lineTo(5, 21);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		// EVENB ODD
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, -2);
		path.lineTo(-2, 2);
		path.lineTo(2, 2);
		path.lineTo(2, -2);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		
		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 8);
		path.lineTo(0, 14);
		path.lineTo(10, 14);
		path.lineTo(10, 8);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 2);
		path.lineTo(12, 14);
		path.lineTo(0, 14);
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-2, 2);
		path.lineTo(-2, 14);
		path.lineTo(12, 14);
		path.lineTo(12, 2);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertTrue(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(0, 0);
		path.lineTo(0, 4);
		path.lineTo(14, 0);
		path.lineTo(14, 4);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));

		path = createPath(PathWindingRule.EVEN_ODD);
		path.moveTo(-8, -7);
		path.lineTo(24, 14);
		path.lineTo(-16, 14);
		path.lineTo(20, -7);
		path.lineTo(5, 21);
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
		path.closePath();
		assertFalse(this.shape.intersects((PathIterator2afp) path.getPathIterator()));
	}

	@Test
	public void staticContainsCirclePoint() {
		assertTrue(Circle2afp.containsCirclePoint(0, 0, 1, 0, 0));
		assertTrue(Circle2afp.containsCirclePoint(0, 0, 1, 1, 0));
		assertTrue(Circle2afp.containsCirclePoint(0, 0, 1, 0, 1));
		assertFalse(Circle2afp.containsCirclePoint(0, 0, 1, 1, 1));
		assertFalse(Circle2afp.containsCirclePoint(0, 0, 1, 1.1, 0));
		assertTrue(Circle2afp.containsCirclePoint(5, 8, 1, 5, 8));
		assertTrue(Circle2afp.containsCirclePoint(5, 8, 1, 6, 8));
		assertTrue(Circle2afp.containsCirclePoint(5, 8, 1, 5, 9));
		assertFalse(Circle2afp.containsCirclePoint(5, 8, 1, 6, 9));
		assertFalse(Circle2afp.containsCirclePoint(5, 8, 1, 6.1, 8));
	}

	@Test
	public void staticContainsCircleRectangle() {
		assertTrue(Circle2afp.containsCircleRectangle(0, 0, 1, 0, 0, .5, .5));
		assertFalse(Circle2afp.containsCircleRectangle(0, 0, 1, 0, 0, 1, 1));
		assertFalse(Circle2afp.containsCircleRectangle(0, 0, 1, 0, 0, .5, 1));
	}
	
	@Test
	public void staticIntersectsCircleCircle() {
		assertFalse(Circle2afp.intersectsCircleCircle(
				0, 0, 1,
				10, 10, 1));
		assertTrue(Circle2afp.intersectsCircleCircle(
				0, 0, 1,
				0, 0, 1));
		assertTrue(Circle2afp.intersectsCircleCircle(
				0, 0, 1,
				0, .5, 1));
		assertTrue(Circle2afp.intersectsCircleCircle(
				0, 0, 1,
				.5, 0, 1));
		assertTrue(Circle2afp.intersectsCircleCircle(
				0, 0, 1,
				.5, .5, 1));
		assertFalse(Circle2afp.intersectsCircleCircle(
				0, 0, 1,
				2, 0, 1));
	}
	
	@Test
	public void staticIntersectsCircleLine() {
		assertTrue(Circle2afp.intersectsCircleLine(
				0, 0, 1,
				-5, -5, -4, -4));
		assertTrue(Circle2afp.intersectsCircleLine(
				0, 0, 1,
				-5, -5, 5, 5));
		assertTrue(Circle2afp.intersectsCircleLine(
				0, 0, 1,
				-5, -5, .5, .5));
		assertFalse(Circle2afp.intersectsCircleLine(
				0, 0, 1,
				-5, -5, .5, -4));
		assertFalse(Circle2afp.intersectsCircleLine(
				0, 0, 1,
				20, .5, 21, 1.5));
	}
	
	@Test
	public void staticIntersectsCircleRectangle() {
		assertFalse(Circle2afp.intersectsCircleRectangle(
				0, 0, 1,
				-5, -5, -4, -4));
		assertTrue(Circle2afp.intersectsCircleRectangle(
				0, 0, 1,
				-5, -5, 5, 5));
		assertTrue(Circle2afp.intersectsCircleRectangle(
				0, 0, 1,
				-5, -5, .5, .5));
		assertFalse(Circle2afp.intersectsCircleRectangle(
				0, 0, 1,
				-5, -5, .5, -4));
		assertFalse(Circle2afp.intersectsCircleRectangle(
				0, 0, 1,
				20, .5, 21, 1.5));
	}
	
	@Test
	public void staticIntersectsCircleSegment() {
		assertFalse(Circle2afp.intersectsCircleSegment(
				0, 0, 1,
				-5, -5, -4, -4));
		assertTrue(Circle2afp.intersectsCircleSegment(
				0, 0, 1,
				-5, -5, 5, 5));
		assertTrue(Circle2afp.intersectsCircleSegment(
				0, 0, 1,
				-5, -5, .5, .5));
		assertFalse(Circle2afp.intersectsCircleSegment(
				0, 0, 1,
				-5, -5, .5, -4));
		assertFalse(Circle2afp.intersectsCircleSegment(
				0, 0, 1,
				20, .5, 21, 1.5));
		assertTrue(Circle2afp.intersectsCircleSegment(
				1, 1, 1,
				.5, -1, .5, 4));
	}
	
	@Test
	public void getX() {
		assertEpsilonEquals(5, this.shape.getX());
	}

	@Test
	public void getY() {
		assertEpsilonEquals(8, this.shape.getY());
	}

	@Test
	public void getCenter() {
		Point2D center = this.shape.getCenter();
		assertEpsilonEquals(5, center.getX());
		assertEpsilonEquals(8, center.getY());
	}

	@Test
	public void setCenterPoint2D() {
		this.shape.setCenter(createPoint(123.456, 789.123));
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setCenterDoubleDouble() {
		this.shape.setCenter(123.456, 789.123);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setX() {
		this.shape.setX(123.456);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setY() {
		this.shape.setY(123.456);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(123.456, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void getRadius() {
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setRadius() {
		this.shape.setRadius(123.456);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(123.456, this.shape.getRadius());
	}

	@Test
	public void setDoubleDoubleDouble() {
		this.shape.set(123.456, 789.123, 456.789);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(456.789, this.shape.getRadius());
	}

	@Test
	public void setPoint2DDouble() {
		this.shape.set(createPoint(123.456, 789.123), 456.789);
		assertEpsilonEquals(123.456, this.shape.getX());
		assertEpsilonEquals(789.123, this.shape.getY());
		assertEpsilonEquals(456.789, this.shape.getRadius());
	}

	@Override
	public void intersectsShape2D() {
		assertTrue(this.shape.intersects((Shape2D) createCircle(10, 10, 1)));
		assertTrue(this.shape.intersects((Shape2D) createEllipse(9, 9, 2, 2)));
	}

	@Override
	public void operator_addVector2D() {
		this.shape.operator_add(createVector(123.456, -789.123));
		assertEpsilonEquals(128.456, this.shape.getX());
		assertEpsilonEquals(-781.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	public void operator_plusVector2D() {
		T shape = this.shape.operator_plus(createVector(123.456, -789.123));
		assertEpsilonEquals(128.456, shape.getX());
		assertEpsilonEquals(-781.123, shape.getY());
		assertEpsilonEquals(5, shape.getRadius());
	}

	@Override
	public void operator_removeVector2D() {
		this.shape.operator_remove(createVector(123.456, -789.123));
		assertEpsilonEquals(-118.456, this.shape.getX());
		assertEpsilonEquals(797.123, this.shape.getY());
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Override
	public void operator_minusVector2D() {
		T shape = this.shape.operator_minus(createVector(123.456, -789.123));
		assertEpsilonEquals(-118.456, shape.getX());
		assertEpsilonEquals(797.123, shape.getY());
		assertEpsilonEquals(5, shape.getRadius());
	}

	@Override
	public void operator_multiplyTransform2D() {
		Transform2D tr;
		Shape2afp newShape;
		
		newShape = this.shape.operator_multiply(null);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform2D();
		newShape = this.shape.operator_multiply(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertEquals(this.shape, newShape);

		tr = new Transform2D();
		tr.makeTranslationMatrix(10, -10);
		newShape = this.shape.operator_multiply(tr);
		assertNotNull(newShape);
		assertNotSame(this.shape, newShape);
		assertTrue(newShape instanceof Path2afp);
		PathIterator2afp pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 20, -2);
		assertElement(pi, PathElementType.CURVE_TO, 20, 0.76142374915397, 17.761423749153966, 3, 15, 3);
		assertElement(pi, PathElementType.CURVE_TO, 12.238576250846033, 3, 10, 0.76142374915397, 10, -2);
		assertElement(pi, PathElementType.CURVE_TO, 10, -4.761423749153966, 12.238576250846033, -7, 15, -7);
		assertElement(pi, PathElementType.CURVE_TO, 17.761423749153966, -7, 20, -4.761423749153966, 20, -2);
		assertElement(pi, PathElementType.CLOSE, 20, -2);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint2D() {
		assertFalse(this.shape.operator_and(createPoint(0,0)));
		assertFalse(this.shape.operator_and(createPoint(11,10)));
		assertFalse(this.shape.operator_and(createPoint(11,50)));
		assertFalse(this.shape.operator_and(createPoint(9,12)));
		assertTrue(this.shape.operator_and(createPoint(9,11)));
		assertTrue(this.shape.operator_and(createPoint(8,12)));
		assertTrue(this.shape.operator_and(createPoint(3,7)));
		assertFalse(this.shape.operator_and(createPoint(10,11)));
		assertTrue(this.shape.operator_and(createPoint(9,10)));
	}

	@Override
	public void operator_andShape2D() {
		assertTrue(this.shape.operator_and(createCircle(10, 10, 1)));
		assertTrue(this.shape.operator_and(createEllipse(9, 9, 2, 2)));
	}

	@Override
	public void operator_upToPoint2D() {
		assertEpsilonEquals(3.74643, this.shape.operator_upTo(createPoint(.5,.5)));
		assertEpsilonEquals(7.9769, this.shape.operator_upTo(createPoint(-1.2,-3.4)));
		assertEpsilonEquals(1.6483, this.shape.operator_upTo(createPoint(-1.2,5.6)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(7.6,5.6)));
	}

	@Test
	public void getHorizontalRadius() {
		assertEpsilonEquals(5, this.shape.getRadius());
	}
	
	@Test
	public void getVerticalRadius() {
		assertEpsilonEquals(5, this.shape.getRadius());
	}

	@Test
	public void setFromCenterDoubleDoubleDoubleDouble() {
		this.shape.setFromCenter(152, 148, 475, -254);
		assertEpsilonEquals(152, this.shape.getX());
		assertEpsilonEquals(148, this.shape.getY());
		assertEpsilonEquals(323, this.shape.getRadius());
	}

	@Test
	public void setFromCornersDoubleDoubleDoubleDouble() {
		this.shape.setFromCorners(-171, 550, 475, -254);
		assertEpsilonEquals(152, this.shape.getX());
		assertEpsilonEquals(148, this.shape.getY());
		assertEpsilonEquals(323, this.shape.getRadius());
	}

	@Test
	public void getMinX() {
		assertEpsilonEquals(0, this.shape.getMinX());
	}

	@Test
	public void setMinX_noSwap() {
		this.shape.setMinX(-41);
		assertEpsilonEquals(-15.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(25.5, this.shape.getRadius());
	}

	@Test
	public void setMinX_swap() {
		this.shape.setMinX(41);
		assertEpsilonEquals(25.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(15.5, this.shape.getRadius());
	}

	@Test
	public void getMaxX() {
		assertEpsilonEquals(10, this.shape.getMaxX());
	}

	@Test
	public void setMaxX_noSwap() {
		this.shape.setMaxX(41);
		assertEpsilonEquals(20.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(20.5, this.shape.getRadius());
	}

	@Test
	public void setMaxX_swap() {
		this.shape.setMaxX(-41);
		assertEpsilonEquals(-20.5, this.shape.getX());
		assertEpsilonEquals(8, this.shape.getY());
		assertEpsilonEquals(20.5, this.shape.getRadius());
	}

	@Test
	public void getMinY() {
		assertEpsilonEquals(3, this.shape.getMinY());
	}

	@Test
	public void setMinY_noSwap() {
		this.shape.setMinY(-41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(-14, this.shape.getY());
		assertEpsilonEquals(27, this.shape.getRadius());
	}

	@Test
	public void setMinY_swap() {
		this.shape.setMinY(41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(27, this.shape.getY());
		assertEpsilonEquals(14, this.shape.getRadius());
	}

	@Test
	public void getMaxY() {
		assertEpsilonEquals(13, this.shape.getMaxY());
	}

	@Test
	public void setMaxY_noSwap() {
		this.shape.setMaxY(41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(22, this.shape.getY());
		assertEpsilonEquals(19, this.shape.getRadius());
	}

	@Test
	public void setMaxY_swap() {
		this.shape.setMaxY(-41);
		assertEpsilonEquals(5, this.shape.getX());
		assertEpsilonEquals(-19, this.shape.getY());
		assertEpsilonEquals(22, this.shape.getRadius());
	}

	@Override
	@Test
	public void getClosestPointToEllipse2afp() {
		assertFpPointEquals(1.35389, 11.42139, this.shape.getClosestPointTo(createEllipse(-10, 20, 2, 1)));
		assertFpPointEquals(4.6533, 12.98797, this.shape.getClosestPointTo(createEllipse(3, 20, 2, 1)));
		assertFpPointEquals(8.86842, 11.16785, this.shape.getClosestPointTo(createEllipse(20, 20, 2, 1)));
		assertFpPointEquals(2.80524, 3.50745, this.shape.getClosestPointTo(createEllipse(-10, -20, 2, 1)));
		assertFpPointEquals(4.8312, 3.00285, this.shape.getClosestPointTo(createEllipse(3, -20, 2, 1)));
		assertFpPointEquals(7.44697, 3.63969, this.shape.getClosestPointTo(createEllipse(20, -20, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createEllipse(5, 5, 2, 1));
	}

	@Override
	@Test
	public void getDistanceSquaredEllipse2afp() {
		assertEpsilonEquals(167.88585, this.shape.getDistanceSquared(createEllipse(-10, 20, 2, 1)));
		assertEpsilonEquals(49.50091, this.shape.getDistanceSquared(createEllipse(3, 20, 2, 1)));
		assertEpsilonEquals(209.16625, this.shape.getDistanceSquared(createEllipse(20, 20, 2, 1)));
		assertEpsilonEquals(636.36568, this.shape.getDistanceSquared(createEllipse(-10, -20, 2, 1)));
		assertEpsilonEquals(484.75874, this.shape.getDistanceSquared(createEllipse(3, -20, 2, 1)));
		assertEpsilonEquals(684.16615, this.shape.getDistanceSquared(createEllipse(20, -20, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(5, 5, 2, 1)));
	}

	@Override
	@Test
	public void getClosestPointToCircle2afp() {
		assertFpPointEquals(1.095656, 11.123475, this.shape.getClosestPointTo(createCircle(-10, 20, 1)));
		assertFpPointEquals(4.17801, 12.93196, this.shape.getClosestPointTo(createCircle(3, 20, 1)));
		assertFpPointEquals(8.90434, 11.12348, this.shape.getClosestPointTo(createCircle(20, 20, 1)));
		assertFpPointEquals(2.63889, 3.5926, this.shape.getClosestPointTo(createCircle(-10, -20, 1)));
		assertFpPointEquals(4.64376, 3.01271, this.shape.getClosestPointTo(createCircle(3, -20, 1)));
		assertFpPointEquals(7.36111, 3.5926, this.shape.getClosestPointTo(createCircle(20, -20, 1)));
		assertClosestPointInBothShapes(this.shape, createCircle(5, 5, 1));
	}

	@Override
	@Test
	public void getDistanceSquaredCircle2afp() {
		assertEpsilonEquals(174.48753, this.shape.getDistanceSquared(createCircle(-10, 20, 1)));
		assertEpsilonEquals(38.0137, this.shape.getDistanceSquared(createCircle(3, 20, 1)));
		assertEpsilonEquals(174.48753, this.shape.getDistanceSquared(createCircle(20, 20, 1)));
		assertEpsilonEquals(663.82288, this.shape.getDistanceSquared(createCircle(-10, -20, 1)));
		assertEpsilonEquals(487.14395, this.shape.getDistanceSquared(createCircle(3, -20, 1)));
		assertEpsilonEquals(663.82288, this.shape.getDistanceSquared(createCircle(20, -20, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(5, 5, 1)));
	}

	@Override
	@Test
	public void getClosestPointToRectangle2afp() {
		assertFpPointEquals(1.32598, 11.3914, this.shape.getClosestPointTo(createRectangle(-10, 20, 2, 1)));
		assertFpPointEquals(5, 13, this.shape.getClosestPointTo(createRectangle(3, 20, 2, 1)));
		assertFpPointEquals(8.90434, 11.12348, this.shape.getClosestPointTo(createRectangle(20, 20, 2, 1)));
		assertFpPointEquals(2.83092, 3.49499, this.shape.getClosestPointTo(createRectangle(-10, -20, 2, 1)));
		assertFpPointEquals(5, 3, this.shape.getClosestPointTo(createRectangle(3, -20, 2, 1)));
		assertFpPointEquals(7.42821, 3.62921, this.shape.getClosestPointTo(createRectangle(20, -20, 2, 1)));
		assertClosestPointInBothShapes(this.shape, createRectangle(5, 5, 2, 1));
	}
	
	@Override
	@Test
	public void getDistanceSquaredRectangle2afp() {
		assertEpsilonEquals(161.08194, this.shape.getDistanceSquared(createRectangle(-10, 20, 2, 1)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createRectangle(3, 20, 2, 1)));
		assertEpsilonEquals(201.90627, this.shape.getDistanceSquared(createRectangle(20, 20, 2, 1)));
		assertEpsilonEquals(623.33352, this.shape.getDistanceSquared(createRectangle(-10, -20, 2, 1)));
		assertEpsilonEquals(484, this.shape.getDistanceSquared(createRectangle(3, -20, 2, 1)));
		assertEpsilonEquals(670.1311, this.shape.getDistanceSquared(createRectangle(20, -20, 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(5, 5, 2, 1)));
	}

	@Override
	@Test
	public void getClosestPointToSegment2afp() {
		assertFpPointEquals(1.27031, 11.33008, this.shape.getClosestPointTo(createSegment(-10, 20, -9, 20.5)));
		assertFpPointEquals(4.17801, 12.93197, this.shape.getClosestPointTo(createSegment(3, 20, 4, 20.5)));
		assertFpPointEquals(8.90434, 11.12348, this.shape.getClosestPointTo(createSegment(20, 20, 21, 20.5)));
		assertFpPointEquals(2.73158, 3.54418, this.shape.getClosestPointTo(createSegment(-10, -20, -9, -19.5)));
		assertFpPointEquals(4.81830, 3.0033, this.shape.getClosestPointTo(createSegment(3, -20, 4, -19.5)));
		assertFpPointEquals(7.3611, 3.5926, this.shape.getClosestPointTo(createSegment(20, -20, 21, -19.5)));
		assertClosestPointInBothShapes(this.shape, createSegment(5, 5, 6, 5.5));
	}

	@Override
	@Test
	public void getDistanceSquaredSegment2afp() {
		assertEpsilonEquals(189.56676, this.shape.getDistanceSquared(createSegment(-10, 20, -9, 20.5)));
		assertEpsilonEquals(51.34475, this.shape.getDistanceSquared(createSegment(3, 20, 4, 20.5)));
		assertEpsilonEquals(201.90627, this.shape.getDistanceSquared(createSegment(20, 20, 21, 20.5)));
		assertEpsilonEquals(668.66452, this.shape.getDistanceSquared(createSegment(-10, -20, -9, -19.5)));
		assertEpsilonEquals(507.06824, this.shape.getDistanceSquared(createSegment(3, -20, 4, -19.5)));
		assertEpsilonEquals(716.3524, this.shape.getDistanceSquared(createSegment(20, -20, 21, -19.5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(5, 5, 6, 5.5)));
	}

	@Override
	@Test
	public void getClosestPointToTriangle2afp() {
		assertFpPointEquals(1.270312, 11.33007, this.shape.getClosestPointTo(createTriangle(-10, 20, -9, 20.5, -10, 20.5)));
		assertFpPointEquals(4.17801, 12.93197, this.shape.getClosestPointTo(createTriangle(3, 20, 4, 20.5, 3, 20.5)));
		assertFpPointEquals(8.90434, 11.12348, this.shape.getClosestPointTo(createTriangle(20, 20, 21, 20.5, 20, 20.5)));
		assertFpPointEquals(2.73158, 3.54418, this.shape.getClosestPointTo(createTriangle(-10, -20, -9, -19.5, -10, -19.5)));
		assertFpPointEquals(4.8183, 3.0033, this.shape.getClosestPointTo(createTriangle(3, -20, 4, -19.5, 3, -19.5)));
		assertFpPointEquals(7.39426, 3.61052, this.shape.getClosestPointTo(createTriangle(20, -20, 21, -19.5, 20, -19.5)));
		assertClosestPointInBothShapes(this.shape, createTriangle(5, 5, 6, 5.5, 5, 5.5));
	}
	
	@Override
	@Test
	public void getDistanceSquaredTriangle2afp() {
		assertEpsilonEquals(189.56676, this.shape.getDistanceSquared(createTriangle(-10, 20, -9, 20.5, -10, 20.5)));
		assertEpsilonEquals(51.34475, this.shape.getDistanceSquared(createTriangle(3, 20, 4, 20.5, 3, 20.5)));
		assertEpsilonEquals(201.90627, this.shape.getDistanceSquared(createTriangle(20, 20, 21, 20.5, 20, 20.5)));
		assertEpsilonEquals(668.66452, this.shape.getDistanceSquared(createTriangle(-10, -20, -9, -19.5, -10, -19.5)));
		assertEpsilonEquals(507.06824, this.shape.getDistanceSquared(createTriangle(3, -20, 4, -19.5, 3, -19.5)));
		assertEpsilonEquals(693.0009, this.shape.getDistanceSquared(createTriangle(20, -20, 21, -19.5, 20, -19.5)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createTriangle(5, 5, 6, 5.5, 5, 5.5)));
	}
	
	protected Path2afp<?, ?, ?, ?, ?, ?> createNonEmptyPath(double x, double y) {
		Path2afp<?, ?, ?, ?, ?, ?> path = createPath();
		path.moveTo(x, y);
		path.lineTo(x + 1, y + .5);
		path.lineTo(x, y + 1);
		return path;
	}

	@Override
	@Test
	public void getClosestPointToPath2afp() {
		assertFpPointEquals(1.270312, 11.33007, this.shape.getClosestPointTo(createNonEmptyPath(-10, 20)));
		assertFpPointEquals(4.17801, 12.93197, this.shape.getClosestPointTo(createNonEmptyPath(3, 20)));
		assertFpPointEquals(8.90434, 11.12348, this.shape.getClosestPointTo(createNonEmptyPath(20, 20)));
		assertFpPointEquals(2.73158, 3.54418, this.shape.getClosestPointTo(createNonEmptyPath(-10, -20)));
		assertFpPointEquals(4.63064, 3.01366, this.shape.getClosestPointTo(createNonEmptyPath(3, -20)));
		assertFpPointEquals(7.42821, 3.62921, this.shape.getClosestPointTo(createNonEmptyPath(20, -20)));
		assertClosestPointInBothShapes(this.shape, createNonEmptyPath(5, 5));
	}

	@Override
	@Test
	public void getDistanceSquaredPath2afp() {
		assertEpsilonEquals(189.56676, this.shape.getDistanceSquared(createNonEmptyPath(-10, 20)));
		assertEpsilonEquals(51.34475, this.shape.getDistanceSquared(createNonEmptyPath(3, 20)));
		assertEpsilonEquals(201.90627, this.shape.getDistanceSquared(createNonEmptyPath(20, 20)));
		assertEpsilonEquals(668.66452, this.shape.getDistanceSquared(createNonEmptyPath(-10, -20)));
		assertEpsilonEquals(487.26027, this.shape.getDistanceSquared(createNonEmptyPath(3, -20)));
		assertEpsilonEquals(670.1311, this.shape.getDistanceSquared(createNonEmptyPath(20, -20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createNonEmptyPath(5, 5)));
	}

	@Override
	@Test
	public void getClosestPointToOrientedRectangle2afp() {
		Vector2D u = createVector(2, 1).toUnitVector();
		assertFpPointEquals(1.35713, 11.42484, this.shape.getClosestPointTo(createOrientedRectangle(-10, 20, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(3.44488, 12.75201, this.shape.getClosestPointTo(createOrientedRectangle(3, 20, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(9.00459, 10.99387, this.shape.getClosestPointTo(createOrientedRectangle(20, 20, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(2.68943, 3.5659, this.shape.getClosestPointTo(createOrientedRectangle(-10, -20, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(4.87445, 3.00158, this.shape.getClosestPointTo(createOrientedRectangle(3, -20, u.getX(), u.getY(), 2, 1)));
		assertFpPointEquals(7.23607, 3.52786, this.shape.getClosestPointTo(createOrientedRectangle(20, -20, u.getX(), u.getY(), 2, 1)));
		assertClosestPointInBothShapes(this.shape, createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1));
		// In multishape.ggb
		assertFpPointEquals(-3.11699, 17.32601, createCircle(-5, 18, 2).getClosestPointTo(
		        createOrientedRectangle(0, 16, -0.624695047554424, 0.780868809443032, 2, 1)));
	}

	@Override
	@Test
	public void getDistanceSquaredOrientedRectangle2afp() {
		Vector2D u = createVector(2, 1).toUnitVector();
		assertEpsilonEquals(156.72722, this.shape.getDistanceSquared(createOrientedRectangle(-10, 20, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(32.99382, this.shape.getDistanceSquared(createOrientedRectangle(3, 20, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(145.28432, this.shape.getDistanceSquared(createOrientedRectangle(20, 20, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(603.01192, this.shape.getDistanceSquared(createOrientedRectangle(-10, -20, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(450.26347, this.shape.getDistanceSquared(createOrientedRectangle(3, -20, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(663.17402, this.shape.getDistanceSquared(createOrientedRectangle(20, -20, u.getX(), u.getY(), 2, 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createOrientedRectangle(5, 5, u.getX(), u.getY(), 2, 1)));
	}

	@Override
	@Test
	public void getClosestPointToParallelogram2afp() {
		Vector2D u = createVector(2, 1).toUnitVector();
		Vector2D v = createVector(-3, 1).toUnitVector();
		assertFpPointEquals(1.50968, 11.58018, this.shape.getClosestPointTo(createParallelogram(-10, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertFpPointEquals(3.72717, 12.83528, this.shape.getClosestPointTo(createParallelogram(3, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertFpPointEquals(8.65872, 11.4079, this.shape.getClosestPointTo(createParallelogram(20, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertFpPointEquals(2.95889, 3.43559, this.shape.getClosestPointTo(createParallelogram(-10, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertFpPointEquals(4.783731, 3.00468, this.shape.getClosestPointTo(createParallelogram(3, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertFpPointEquals(7.23607, 3.52786, this.shape.getClosestPointTo(createParallelogram(20, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertClosestPointInBothShapes(this.shape, createParallelogram(5, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1));
	}

	@Override
	@Test
	public void getDistanceSquaredParallelogram2afp() {
		Vector2D u = createVector(2, 1).toUnitVector();
		Vector2D v = createVector(-3, 1).toUnitVector();
		assertEpsilonEquals(157.91489, this.shape.getDistanceSquared(createParallelogram(-10, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertEpsilonEquals(37.90748, this.shape.getDistanceSquared(createParallelogram(3, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertEpsilonEquals(138.24702, this.shape.getDistanceSquared(createParallelogram(20, 20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertEpsilonEquals(626.93619, this.shape.getDistanceSquared(createParallelogram(-10, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertEpsilonEquals(475.86981, this.shape.getDistanceSquared(createParallelogram(3, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertEpsilonEquals(678.34507, this.shape.getDistanceSquared(createParallelogram(20, -20, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createParallelogram(5, 5, u.getX(), u.getY(), 2, v.getX(), v.getY(), 1)));
	}

	@Override
	@Test
	public void getClosestPointToRoundRectangle2afp() {
		assertFpPointEquals(1.32598, 11.3914, this.shape.getClosestPointTo(createRoundRectangle(-10, 20, 2, 1, .2, .1)));
		assertFpPointEquals(4.91936, 12.99935, this.shape.getClosestPointTo(createRoundRectangle(3, 20, 2, 1, .2, .1)));
		assertFpPointEquals(8.89785, 11.13157, this.shape.getClosestPointTo(createRoundRectangle(20, 20, 2, 1, .2, .1)));
		assertFpPointEquals(2.83092, 3.49499, this.shape.getClosestPointTo(createRoundRectangle(-10, -20, 2, 1, .2, .1)));
		assertFpPointEquals(4.96350, 3.00013, this.shape.getClosestPointTo(createRoundRectangle(3, -20, 2, 1, .2, .1)));
		assertFpPointEquals(7.42821, 3.62921, this.shape.getClosestPointTo(createRoundRectangle(20, -20, 2, 1, .2, .1)));
		assertClosestPointInBothShapes(this.shape, createRoundRectangle(5, 5, 2, 1, .2, .1));
	}

	@Override
	@Test
	public void getDistanceSquaredRoundRectangle2afp() {
		assertEpsilonEquals(162.42896, this.shape.getDistanceSquared(createRoundRectangle(-10, 20, 2, 1, .2, .1)));
		assertEpsilonEquals(49.02258, this.shape.getDistanceSquared(createRoundRectangle(3, 20, 2, 1, .2, .1)));
		assertEpsilonEquals(203.34342, this.shape.getDistanceSquared(createRoundRectangle(20, 20, 2, 1, .2, .1)));
		assertEpsilonEquals(625.92327, this.shape.getDistanceSquared(createRoundRectangle(-10, -20, 2, 1, .2, .1)));
		assertEpsilonEquals(484.03212, this.shape.getDistanceSquared(createRoundRectangle(3, -20, 2, 1, .2, .1)));
		assertEpsilonEquals(672.9238, this.shape.getDistanceSquared(createRoundRectangle(20, -20, 2, 1, .2, .1)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(5, 5, 2, 1, .2, .1)));
	}

	protected MultiShape2afp<?, ?, ?, ?, ?, ?, ?> createNonEmptyMultishape(double x, double y) {
		MultiShape2afp multishape = createMultiShape();
		multishape.add(createRectangle(x, y, 2, 1));
		return multishape;
	}

	@Override
	@Test
	public void getClosestPointToMultiShape2afp() {
		assertFpPointEquals(1.32598, 11.3914, this.shape.getClosestPointTo(createNonEmptyMultishape(-10, 20)));
		assertFpPointEquals(5, 13, this.shape.getClosestPointTo(createNonEmptyMultishape(3, 20)));
		assertFpPointEquals(8.90434, 11.12348, this.shape.getClosestPointTo(createNonEmptyMultishape(20, 20)));
		assertFpPointEquals(2.83092, 3.49499, this.shape.getClosestPointTo(createNonEmptyMultishape(-10, -20)));
		assertFpPointEquals(5, 3, this.shape.getClosestPointTo(createNonEmptyMultishape(3, -20)));
		assertFpPointEquals(7.42821, 3.62921, this.shape.getClosestPointTo(createNonEmptyMultishape(20, -20)));
		assertClosestPointInBothShapes(this.shape, createNonEmptyMultishape(5, 5));
	}
	
	@Override
	@Test
	public void getDistanceSquaredMultiShape2afp() {
		assertEpsilonEquals(161.08194, this.shape.getDistanceSquared(createNonEmptyMultishape(-10, 20)));
		assertEpsilonEquals(49, this.shape.getDistanceSquared(createNonEmptyMultishape(3, 20)));
		assertEpsilonEquals(201.90627, this.shape.getDistanceSquared(createNonEmptyMultishape(20, 20)));
		assertEpsilonEquals(623.33352, this.shape.getDistanceSquared(createNonEmptyMultishape(-10, -20)));
		assertEpsilonEquals(484, this.shape.getDistanceSquared(createNonEmptyMultishape(3, -20)));
		assertEpsilonEquals(670.1311, this.shape.getDistanceSquared(createNonEmptyMultishape(20, -20)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createNonEmptyMultishape(5, 5)));
	}

}