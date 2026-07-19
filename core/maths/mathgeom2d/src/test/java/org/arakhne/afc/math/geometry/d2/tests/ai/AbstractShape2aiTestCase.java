/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d2.tests.ai;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.ai.Circle2ai;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathElement2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.ai.Shape2ai;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractShape2aiTestCase<T extends Shape2ai<?, ?, ?, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractMathTestCase {
	
	/** Is the rectangular shape to test.
	 */
	protected T shape;
	
	/** Factory of shapes.
	 */
	protected TestShapeFactory<?, ?, B> factory;
	
	/** Replies the shape to be tested.
	 *
	 * @return the shape.
	 */
	public T getS() {
		return this.shape;
	}
	
	@BeforeEach
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

	@AfterEach
	public void tearDown() throws Exception {
		this.shape = null;
	}
	
	/** Assert is the given path iterator has a first element with the
	 * given information.
	 * 
	 * @param pi the path iterator.
	 * @param type the expected type.
	 * @param coords the expected coordinates.
	 */
	protected void assertElement(PathIterator2ai<?> pi, PathElementType type, int... coords) {
		if (!pi.hasNext()) {
			fail("expected path element but the iterator is empty");  //$NON-NLS-1$
		}
		PathElement2ai pe = pi.next();
		if (!type.equals(pe.getType())) {
			fail("expected: "+type+"; actual: "+pe.getType());   //$NON-NLS-1$ //$NON-NLS-2$
		}
		int[] c = new int[coords.length];
		pe.toArray(c);
		if (!isEquals(c, coords)) {
			fail("expected: "+Arrays.toString(coords)+"; actual: "+Arrays.toString(c));   //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
    /** Assert that the two shape are intersecting and the closest point on the first shape is also
     * on the second shape.
     *
     * @param shape1 the first shape, on which the closest point is computed.
     * @param shape2 the second point.
     */
    public void assertClosestPointInBothShapes(Shape2ai shape1, Shape2ai shape2) {
        final Point2D<?, ?> point = shape1.getClosestPointTo(shape2);
        double distance;
        //TODO: The following test may fail since MathConstants#SPLINE_APPROXIMATION_RATIO is too high; see Issue #89.
        //distance = shape1.getDistance(point); 
        //assertEpsilonZero("Closest point " + point + " is not in the first shape: " + shape1 + ". Distance: " + distance, distance);
        distance = shape2.getDistance(point); 
        assertEpsilonZero("Closest point " + point + " is not in the second shape: " + shape2 + ". Distance: " + distance, distance); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
	 * Replies if two arrays have the same values at epsilon.
	 * 
	 * @param a a first set of coordinates.
	 * @param b a second set of coordinates.
	 * @return {@code true} if the two arrays are equal, otherwise
	 * {@code false}.
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
	 * @param a a first set of coordinates.
	 * @param b a second set of coordinates.
	 * @return {@code true} if the two arrays are equal, otherwise
	 * {@code false}.
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
	 * @param pi the path iterator.
	 */
	protected void assertNoElement(PathIterator2ai<?> pi) {
		if (pi.hasNext()) {
			fail("expected no path element but the iterator is not empty");  //$NON-NLS-1$
		}
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getGeomFactory(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertNotNull(this.shape.getGeomFactory());
	}

}