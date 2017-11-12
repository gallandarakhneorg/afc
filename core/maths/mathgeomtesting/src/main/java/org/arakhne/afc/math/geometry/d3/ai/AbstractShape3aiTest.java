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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3DTestRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

@SuppressWarnings("all")
public abstract class AbstractShape3aiTest<T extends Shape3ai<?, ?, ?, ?, ?, B>,
		B extends RectangularPrism3ai<?, ?, ?, ?, ?, B>> extends AbstractMathTestCase {
	
	@Rule
	public CoordinateSystem3DTestRule csTestRule = new CoordinateSystem3DTestRule();
	
	/** Is the rectangular shape to test.
	 */
	protected T shape;
	
	/** Factory of shapes.
	 */
	protected TestShapeFactory3ai<?, ?, B> factory;

	@Before
	public void setUp() throws Exception {
		this.factory = createFactory();
		this.shape = createShape();
	}
	
	protected abstract TestShapeFactory3ai<?, ? ,B> createFactory();
	
	/** Create the shape to test.
	 * 
	 * @return the shape to test.
	 */
	protected abstract T createShape();
	
	public final Segment3ai<?, ?, ?, ?, ?, B> createSegment(int x1, int y1, int z1, int x2, int y2, int z2) {
		return this.factory.createSegment(x1, y1, z1, x2, y2, z2);
	}
	
	public final B createRectangularPrism(int x, int y, int z, int width, int height, int depth) {
		return this.factory.createRectangularPrism(x, y, z, width, height, depth);
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

	@After
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
	 * @param a a set of coordinates.
	 * @param b a set of coordinates.
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
	 * @param pi the path iterator.
	 */
	protected void assertNoElement(PathIterator3ai<?> pi) {
		if (pi.hasNext()) {
			fail("expected no path element but the iterator is not empty"); //$NON-NLS-1$
		}
	}

	@Test
	@Ignore
	public abstract void testClone();

	@Test
	@Ignore
	public abstract void equalsObject();

	@Test
	@Ignore
	public abstract void equalsObject_withPathIterator();

	@Test
	@Ignore
	public abstract void equalsToPathIterator();

	@Test
	@Ignore
	public abstract void equalsToShape();

	@Test
	@Ignore
	public abstract void isEmpty();

	@Test
	public abstract void clear();

	@Test
	@Ignore
	public abstract void containsPoint3D();
	
	@Test
	@Ignore
	public abstract void getClosestPointTo();
	
	@Test
	@Ignore
	public abstract void getFarthestPointTo();

	@Test
	@Ignore
	public abstract void getDistance();

	@Test
	@Ignore
	public abstract void getDistanceSquared();

	@Test
	@Ignore
	public abstract void getDistanceL1();

	@Test
	@Ignore
	public abstract void getDistanceLinf();

	@Test
	@Ignore
	public abstract void setIT();

	@Test
	@Ignore
	public abstract void getPathIterator();

	@Test
	@Ignore
	public abstract void getPathIteratorTransform3D();

	@Test
	@Ignore
	public abstract void createTransformedShape();

	@Test
	@Ignore
	public abstract void translateVector3D(); 

	@Test
	@Ignore
	public abstract void toBoundingBox();
	
	@Test
	@Ignore
	public abstract void toBoundingBoxB();

	@Test
	@Ignore
	public abstract void getPointIterator();

	@Test
	@Ignore
	public abstract void containsRectangularPrism3ai();

	@Test
	@Ignore
	public abstract void intersectsRectangularPrism3ai();

	@Test
	@Ignore
	public abstract void intersectsSphere3ai();

	@Test
	@Ignore
	public abstract void intersectsSegment3ai();
	
	@Test
	@Ignore
	public abstract void intersectsPath3ai();
	
	@Test
	@Ignore
	public abstract void intersectsPathIterator3ai();

	@Test
	@Ignore
	public abstract void translateIntInt(); 

	@Test
	@Ignore
	public abstract void containsIntInt();

	@Test
	public void getGeomFactory() {
		assertNotNull(this.shape.getGeomFactory());
	}
	
	@Test
	@Ignore
	public abstract void intersectsShape3D();

	@Test
	@Ignore
	public abstract void operator_addVector3D();

	@Test
	@Ignore
	public abstract void operator_plusVector3D();

	@Test
	@Ignore
	public abstract void operator_removeVector3D();

	@Test
	@Ignore
	public abstract void operator_minusVector3D();

	@Test
	@Ignore
	public abstract void operator_multiplyTransform3D();

	@Test
	@Ignore
	public abstract void operator_andPoint3D();

	@Test
	@Ignore
	public abstract void operator_andShape3D();

	@Test
	@Ignore
	public abstract void operator_upToPoint3D();

	/** Generate a bitmap containing the given Shape2D.
	 *
	 * @param shape.
	 * @return the filename
	 * @throws IOException Input/output exception
	 */
	public static File generateTestPicture(Shape3ai<?, ?, ?, ?, ?, ?> shape) throws IOException {
		File filename = File.createTempFile("testShape", ".png"); //$NON-NLS-1$ //$NON-NLS-2$
		RectangularPrism3ai box = shape.toBoundingBox();
		PathIterator3ai<?> iterator = shape.getPathIterator();
		Path2D path = new Path2D.Double(
				iterator.getWindingRule() == PathWindingRule.NON_ZERO ? Path2D.WIND_NON_ZERO : Path2D.WIND_EVEN_ODD);
		while (iterator.hasNext()) {
			PathElement3ai element = iterator.next();
			int tox = (element.getToX() - box.getMinX()) * 2;
			int toy = (box.getMaxY() - (element.getToY() - box.getMinY())) * 2;
			switch (element.getType()) {
			case LINE_TO:
				path.lineTo(tox, toy);
				break;
			case MOVE_TO:
				path.moveTo(tox, toy);
				break;
			case CLOSE:
				path.closePath();
				break;
			case CURVE_TO:
				path.curveTo(
						(element.getCtrlX1() - box.getMinX()) * 2,
						(box.getMaxY() - (element.getCtrlY1() - box.getMinY())) * 2,
						(element.getCtrlX2() - box.getMinX()) * 2,
						(box.getMaxY() - (element.getCtrlY2() - box.getMinY())) * 2,
						tox, toy);
				break;
			case ARC_TO:
				throw new IllegalStateException();
			case QUAD_TO:
				path.quadTo(
						(element.getCtrlX1() - box.getMinX()) * 2,
						(box.getMaxY() - (element.getCtrlY1() - box.getMinY())) * 2,
						tox, toy);
				break;
			default:
			}
		}
		BufferedImage image = new BufferedImage(
				box.getWidth() * 2,
				box.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.draw(path);
		ImageIO.write(image, "png", filename); //$NON-NLS-1$
		return filename;
	}	

}
