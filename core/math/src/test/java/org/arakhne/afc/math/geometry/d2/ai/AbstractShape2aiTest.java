/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.ai;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractShape2aiTest<T extends Shape2ai<?, ?, ?, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractMathTestCase {
	
	@Rule
	public CoordinateSystem2DTestRule csTestRule = new CoordinateSystem2DTestRule();
	
	/** Is the rectangular shape to test.
	 */
	protected T shape;
	
	/** Factory of shapes.
	 */
	protected TestShapeFactory<?, ?, B> factory;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.factory = createFactory();
		this.shape = createShape();
	}
	
	protected abstract TestShapeFactory<?, ? ,B> createFactory();
	
	/** Create the shape to test.
	 * 
	 * @return the shape to test.
	 */
	protected abstract T createShape();
	
	public final Segment2ai<?, ?, ?, ?, ?, B> createSegment(int x1, int y1, int x2, int y2) {
		return this.factory.createSegment(x1, y1, x2, y2);
	}
	
	public final B createRectangle(int x, int y, int width, int height) {
		return this.factory.createRectangle(x, y, width, height);
	}

	public final Circle2ai<?, ?, ?, ?, ?, B> createCircle(int x, int y, int radius) {
		return this.factory.createCircle(x, y, radius);
	}
	
	public final Point2D createPoint(int x, int y) {
		return this.factory.createPoint(x, y);
	}

	public final Vector2D createVector(int x, int y) {
		return this.factory.createVector(x, y);
	}

	public final Path2ai<?, ?, ?, ?, ?, B> createPath() {
		return this.factory.createPath(null);
	}
	
	public final Path2ai<?, ?, ?, ?, ?, B> createPath(PathWindingRule rule) {
		return this.factory.createPath(rule);
	}
	
	public final MultiShape2ai<?, ?, ?, ?, ?, ?, B> createMultiShape() {
		return this.factory.createMultiShape();
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.shape = null;
	}
	
	/** Assert is the given path iterator has a first element with the
	 * given information.
	 * 
	 * @param pi
	 * @param type
	 * @param coords
	 */
	protected void assertElement(PathIterator2ai<?> pi, PathElementType type, int... coords) {
		if (!pi.hasNext()) {
			fail("expected path element but the iterator is empty"); //$NON-NLS-1$
		}
		PathElement2ai pe = pi.next();
		if (!type.equals(pe.getType())) {
			fail("expected: "+type+"; actual: "+pe.getType());  //$NON-NLS-1$//$NON-NLS-2$
		}
		int[] c = new int[coords.length];
		pe.toArray(c);
		if (!isEquals(c, coords)) {
			fail("expected: "+Arrays.toString(coords)+"; actual: "+Arrays.toString(c));  //$NON-NLS-1$//$NON-NLS-2$
		}
	}
	
	/**
	 * Replies if two arrays have the same values at epsilon.
	 * 
	 * @param a
	 * @param b
	 * @return <code>true</code> if the two arrays are equal, otherwise
	 * <code>false</code>.
	 */
	public boolean isEpsilonEquals(float[] a, float[] b) {
		if (a==b) return true;
		if (a==null && b!=null) return false;
		if (a!=null && b==null) return false;
		assert(a!=null && b!=null);
		if (a.length!=b.length) return false;
		for(int i=0; i<a.length; ++i) {
			if (!isEpsilonEquals(a[i], b[i])) return false;
		}
		return true;
	}
	
	/**
	 * Replies if two arrays have the same values at epsilon.
	 * 
	 * @param a
	 * @param b
	 * @return <code>true</code> if the two arrays are equal, otherwise
	 * <code>false</code>.
	 */
	protected boolean isEquals(int[] a, int[] b) {
		if (a==b) return true;
		if (a==null && b!=null) return false;
		if (a!=null && b==null) return false;
		assert(a!=null && b!=null);
		if (a.length!=b.length) return false;
		for(int i=0; i<a.length; ++i) {
			if (a[i]!=b[i]) return false;
		}
		return true;
	}

	/** Assert is the given path iterator has no element.
	 * 
	 * @param pi
	 */
	protected void assertNoElement(PathIterator2ai<?> pi) {
		if (pi.hasNext()) {
			fail("expected no path element but the iterator is not empty"); //$NON-NLS-1$
		}
	}

	@Test
	public abstract void testClone();

	@Test
	public abstract void equalsObject();

	@Test
	public abstract void equalsObject_withPathIterator();

	@Test
	public abstract void equalsToPathIterator();

	@Test
	public abstract void equalsToShape();

	@Test
	public abstract void isEmpty();

	@Test
	public abstract void clear();

	@Test
	public abstract void containsPoint2D();
	
	@Test
	public abstract void getClosestPointTo();
	
	@Test
	public abstract void getFarthestPointTo();

	@Test
	public abstract void getDistance();

	@Test
	public abstract void getDistanceSquared();

	@Test
	public abstract void getDistanceL1();

	@Test
	public abstract void getDistanceLinf();

	@Test
	public abstract void setIT();

	@Test
	public abstract void getPathIterator();

	@Test
	public abstract void getPathIteratorTransform2D();

	@Test
	public abstract void createTransformedShape();

	@Test
	public abstract void translateVector2D(); 

	@Test
	public abstract void toBoundingBox();
	
	@Test
	public abstract void toBoundingBoxB();

	@Test
	public abstract void getPointIterator();

	@Test
	public abstract void containsRectangle2ai();

	@Test
	public abstract void intersectsRectangle2ai();

	@Test
	public abstract void intersectsCircle2ai();

	@Test
	public abstract void intersectsSegment2ai();
	
	@Test
	public abstract void intersectsPath2ai();
	
	@Test
	public abstract void intersectsPathIterator2ai();

	@Test
	public abstract void translateIntInt(); 

	@Test
	public abstract void containsIntInt();

	@Test
	public void getGeomFactory() {
		assertNotNull(this.shape.getGeomFactory());
	}
	
	@Test
	public abstract void intersectsShape2D();

	@Test
	public abstract void operator_addVector2D();

	@Test
	public abstract void operator_plusVector2D();

	@Test
	public abstract void operator_removeVector2D();

	@Test
	public abstract void operator_minusVector2D();

	@Test
	public abstract void operator_multiplyTransform2D();

	@Test
	public abstract void operator_andPoint2D();

	@Test
	public abstract void operator_andShape2D();

	@Test
	public abstract void operator_upToPoint2D();

}