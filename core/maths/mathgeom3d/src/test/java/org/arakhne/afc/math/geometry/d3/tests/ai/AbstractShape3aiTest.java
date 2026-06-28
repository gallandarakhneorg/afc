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

package org.arakhne.afc.math.geometry.d3.tests.ai;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.PathWindingRule;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.AlignedBox3ai;
import org.arakhne.afc.math.geometry.d3.ai.MultiShape3ai;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.PathElement3ai;
import org.arakhne.afc.math.geometry.d3.ai.PathIterator3ai;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;
import org.arakhne.afc.math.geometry.d3.ai.Shape3ai;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;
import org.arakhne.afc.math.geometry.d3.tests.AbstractMathTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractShape3aiTest<T extends Shape3ai<?, ?, ?, ?, ?, B>,
		B extends AlignedBox3ai<?, ?, ?, ?, ?, B>> extends AbstractMathTestCase {
	
	/** Is the rectangular shape to test.
	 */
	protected T shape;
	
	/** Factory of shapes.
	 */
	protected TestShapeFactory3i<?, ?, ?, B> factory;

	@BeforeEach
	public void setUp() {
		this.factory = createFactory();
		this.shape = createShape();
	}
	
	protected abstract TestShapeFactory3i<?, ?, ? ,B> createFactory();
	
	/** Create the shape to test.
	 * 
	 * @return the shape to test.
	 */
	protected abstract T createShape();
	
	public final Segment3ai<?, ?, ?, ?, ?, B> createSegment(int x1, int y1, int z1, int x2, int y2, int z2) {
		return this.factory.createSegment(x1, y1, z1, x2, y2, z2);
	}
	
	public final B createAlignedBox(int x, int y, int z, int width, int height, int depth) {
		return this.factory.createAlignedBox(x, y, z, width, height, depth);
	}

	public final Sphere3ai<?, ?, ?, ?, ?, B> createSphere(int x, int y, int z, int radius) {
		return this.factory.createSphere(x, y, z, radius);
	}
	
	public final Point3D createPoint(int x, int y, int z) {
		return this.factory.createPoint(x, y, z);
	}

	public final Vector3D createVector(int x, int y, int z) {
		return this.factory.createVector(x, y, z);
	}

	public final Path3ai<?, ?, ?, ?, ?, B> createPath() {
		return this.factory.createPath(null);
	}
	
	public final Path3ai<?, ?, ?, ?, ?, B> createPath(PathWindingRule rule) {
		return this.factory.createPath(rule);
	}
	
	public final MultiShape3ai<?, ?, ?, ?, ?, ?, B> createMultiShape() {
		return this.factory.createMultiShape();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.shape = null;
	}
	
	/** Assert is the given path iterator has a first element with the
	 * given information.
	 * 
	 * @param pi path iterator.
	 * @param type the expected type.
	 * @param coords the expected coordinates.
	 */
	protected void assertElement(PathIterator3ai<?> pi, PathElementType type, int... coords) {
		if (!pi.hasNext()) {
			fail("expected path element but the iterator is empty"); //$NON-NLS-1$
		}
		PathElement3ai pe = pi.next();
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
	 * @param a a set of coordinates.
	 * @param b a set of coordinates.
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
	 * @param a a set of coordinates.
	 * @param b a set of coordinates.
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
	protected void assertNoElement(PathIterator3ai<?> pi) {
		if (pi.hasNext()) {
			fail("expected no path element but the iterator is not empty"); //$NON-NLS-1$
		}
	}

	@DisplayName("clone")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void testClone(CoordinateSystem3D cs);

	@DisplayName("equals(Object)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void equalsObject(CoordinateSystem3D cs);

	@DisplayName("equalsToShape")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void equalsToShape(CoordinateSystem3D cs);

	@DisplayName("isEmpty")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void isEmpty(CoordinateSystem3D cs);

	@DisplayName("clear")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void clear(CoordinateSystem3D cs);

	@DisplayName("contains(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void containsPoint3D(CoordinateSystem3D cs);
	
	@DisplayName("getClosestPointTo")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void getClosestPointTo(CoordinateSystem3D cs);
	
	@DisplayName("getFarthestPointTo")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void getFarthestPointTo(CoordinateSystem3D cs);

	@DisplayName("getDistance")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void getDistance(CoordinateSystem3D cs);

	@DisplayName("getDistanceSquared")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void getDistanceSquared(CoordinateSystem3D cs);

	@DisplayName("getDistanceL1")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void getDistanceL1(CoordinateSystem3D cs);

	@DisplayName("getDistanceLinf")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void getDistanceLinf(CoordinateSystem3D cs);

	@DisplayName("set(Shape)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void setIT(CoordinateSystem3D cs);

	@DisplayName("translate(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void translateVector3D(CoordinateSystem3D cs);

	@DisplayName("toBoundingBox")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void toBoundingBox(CoordinateSystem3D cs);
	
	@DisplayName("toBonudingBox(Box3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void toBoundingBoxB(CoordinateSystem3D cs);

	@DisplayName("getPointIterator")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void getPointIterator(CoordinateSystem3D cs);

	@DisplayName("contains(AlignedBox3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void containsAlignedBox3ai(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void intersectsAlignedBox3ai(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void intersectsSphere3ai(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void intersectsSegment3ai(CoordinateSystem3D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void intersectsPath3ai(CoordinateSystem3D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void intersectsPathIterator3ai(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void translateIntInt(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void containsIntInt(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getGeomFactory(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertNotNull(this.shape.getGeomFactory());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void intersectsShape3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void operator_addVector3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void operator_plusVector3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void operator_removeVector3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void operator_minusVector3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void operator_andPoint3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void operator_andShape3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void operator_upToPoint3D(CoordinateSystem3D cs);

}
