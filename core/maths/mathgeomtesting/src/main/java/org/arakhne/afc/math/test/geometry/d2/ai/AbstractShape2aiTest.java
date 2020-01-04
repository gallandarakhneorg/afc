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

package org.arakhne.afc.math.test.geometry.d2.ai;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.Circle2ai;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathElement2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;
import org.arakhne.afc.math.geometry.d2.ai.Shape2ai;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public abstract class AbstractShape2aiTest<T extends Shape2ai<?, ?, ?, ?, ?, B>,
		B extends Rectangle2ai<?, ?, ?, ?, ?, B>> extends AbstractMathTestCase {
	
	/** Is the rectangular shape to test.
	 */
	protected T shape;
	
	/** Factory of shapes.
	 */
	protected TestShapeFactory<?, ?, B> factory;
	
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
	 * @param a a first set of coordinates.
	 * @param b a second set of coordinates.
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
	protected void assertNoElement(PathIterator2ai<?> pi) {
		if (pi.hasNext()) {
			fail("expected no path element but the iterator is not empty");  //$NON-NLS-1$
		}
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void testClone(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void equalsObject(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void equalsObject_withPathIterator(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void equalsToPathIterator(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void equalsToShape(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void isEmpty(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void clear(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void containsPoint2D(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointTo(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getFarthestPointTo(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToRectangle2ai(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToCircle2ai(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToSegment2ai(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToMultiShape2ai(CoordinateSystem2D cs);
		
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToPath2ai(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistance(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquared(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceL1(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceLinf(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void setIT(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getPathIterator(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getPathIteratorTransform2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void createTransformedShape(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void translateVector2D(CoordinateSystem2D cs); 

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void toBoundingBox(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void toBoundingBoxB(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getPointIterator(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void containsRectangle2ai(CoordinateSystem2D cs);

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public abstract void containsShape2D(CoordinateSystem2D cs);

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsRectangle2ai(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsCircle2ai(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsSegment2ai(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsPath2ai(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsPathIterator2ai(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredRectangle2ai(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredCircle2ai(CoordinateSystem2D cs);

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public abstract void getDistanceSquaredMultiShape2ai(CoordinateSystem2D cs);

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredSegment2ai(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredPath2ai(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void translateIntInt(CoordinateSystem2D cs); 

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void containsIntInt(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public void getGeomFactory(CoordinateSystem2D cs) {
		CoordinateSystem2D.setDefaultCoordinateSystem(cs);
		assertNotNull(this.shape.getGeomFactory());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsShape2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void operator_addVector2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void operator_plusVector2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void operator_removeVector2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void operator_minusVector2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void operator_multiplyTransform2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void operator_andPoint2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void operator_andShape2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void operator_upToPoint2D(CoordinateSystem2D cs);

	/** Generate a bitmap containing the given Shape2D.
	 *
	 * @param shape.
	 * @return the filename
	 * @throws IOException Input/output exception
	 */
	public static File generateTestPicture(Shape2ai<?, ?, ?, ?, ?, ?> shape) throws IOException {
		File filename = File.createTempFile("testShape", ".png"); //$NON-NLS-1$ //$NON-NLS-2$
		Rectangle2ai box = shape.toBoundingBox();
		PathIterator2ai<?> iterator = shape.getPathIterator();
		Path2D path = new Path2D.Double(
				iterator.getWindingRule() == PathWindingRule.NON_ZERO ? Path2D.WIND_NON_ZERO : Path2D.WIND_EVEN_ODD);
		while (iterator.hasNext()) {
			PathElement2ai element = iterator.next();
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