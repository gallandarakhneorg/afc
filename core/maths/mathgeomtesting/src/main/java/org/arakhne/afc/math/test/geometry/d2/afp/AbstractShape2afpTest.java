/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d2.afp;

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
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.Shape2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public abstract class AbstractShape2afpTest<T extends Shape2afp<?, ?, ?, ?, ?, ?>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractMathTestCase {
	
	/** Is the rectangular shape to test.
	 */
	protected T shape;
	
	/** Shape factory.
	 */
	protected TestShapeFactory<? extends Point2D, ? extends Vector2D, B> factory;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.factory = createFactory();
		this.shape = createShape();
	}
	
	protected abstract TestShapeFactory<? extends Point2D, ? extends Vector2D, B> createFactory();
	
	/** Create the shape to test.
	 * 
	 * @return the shape to test.
	 */
	protected abstract T createShape();
	
	public final Segment2afp<?, ?, ?, ?, ?, B> createSegment(double x1, double y1, double x2, double y2) {
		return this.factory.createSegment(x1, y1, x2, y2);
	}
	
	public final B createRectangle(double x, double y, double width, double height) {
		return this.factory.createRectangle(x, y, width, height);
	}

	public final Ellipse2afp<?, ?, ?, ?, ?, B> createEllipse(double x, double y, double width, double height) {
		return this.factory.createEllipse(x, y, width, height);
	}

	public final Ellipse2afp<?, ?, ?, ?, ?, B> createEllipseFromCorners(double minx, double miny, double maxx, double maxy) {
		double x, width, y, height;
		if (minx < maxx) {
			x = minx;
			width = maxx - minx;
		} else {
			x = maxx;
			width = minx - maxx;
		}
		if (miny < maxy) {
			y = miny;
			height = maxy - miny;
		} else {
			y = maxy;
			height = miny - maxy;
		}
		return this.factory.createEllipse(x, y, width, height);
	}

	public final RoundRectangle2afp<?, ?, ?, ?, ?, B> createRoundRectangle(double x, double y,
			double width, double height, double arcWidth, double arcHeight) {
		return this.factory.createRoundRectangle(x, y, width, height, arcWidth, arcHeight);
	}

	public final OrientedRectangle2afp<?, ?, ?, ?, ?, B> createOrientedRectangle(
			double centerX, double centerY, double axis1X, double axis1Y, double extent1, double extent2) {
		return this.factory.createOrientedRectangle(centerX, centerY, axis1X, axis1Y, extent1, extent2);
	}

	public final Parallelogram2afp<?, ?, ?, ?, ?, B> createParallelogram(
			double cx, double cy, double ux, double uy, double extent1, double vx, double vy, double extent2) {
		return this.factory.createParallelogram(cx, cy, ux, uy, extent1, vx, vy, extent2);
	}

	public final Triangle2afp<?, ?, ?, ?, ?, B> createTriangle(
			double x1, double y1, double x2, double y2, double x3, double y3) {
		return this.factory.createTriangle(x1, y1, x2, y2, x3, y3);
	}

	public final Circle2afp<?, ?, ?, ?, ?, B> createCircle(double x, double y, double radius) {
		return this.factory.createCircle(x, y, radius);
	}
	
	public final MultiShape2afp<?, ?, ?, ?, ?, ?, B> createMultiShape() {
		return this.factory.createMultiShape();
	}

	public final Point2D createPoint(double x, double y) {
		return this.factory.createPoint(x, y);
	}

	public final Vector2D createVector(double x, double y) {
		return this.factory.createVector(x, y);
	}

	public final Path2afp<?, ?, ?, ?, ?, B> createPath() {
		return this.factory.createPath(null);
	}

	public final Path2afp<?, ?, ?, ?, ?, B> createPath(PathWindingRule rule) {
		return this.factory.createPath(rule);
	}

	public final Path2afp<?, ?, ?, ?, ?, B> createPolyline(double... coordinates) {
		Path2afp<?, ?, ?, ?, ?, B>  path = createPath();
		path.moveTo(coordinates[0], coordinates[1]);
		for (int i = 2; i < coordinates.length; i += 2) {
			path.lineTo(coordinates[i], coordinates[i + 1]);
		}
		return path;
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
	public void assertElement(PathIterator2afp<?> pi, PathElementType type, double... coords) {
		if (!pi.hasNext()) {
			fail("expected path element but the iterator is empty");  //$NON-NLS-1$
		}
		PathElement2afp pe = pi.next();
		if (!type.equals(pe.getType())) {
			failCompare("not same element type.", type.name(), pe.getType().name()); //$NON-NLS-1$
		}
		double[] c = new double[coords.length];
		pe.toArray(c);
		if (!isEpsilonEquals(c, coords)) {
			failCompare("not same coordinates.",   //$NON-NLS-1$
					Arrays.toString(coords),
					Arrays.toString(c));
		}
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
	public static void assertNoElement(PathIterator2afp<?> pi) {
		if (pi.hasNext()) {
			fail("expected no path element but the iterator is not empty: "  //$NON-NLS-1$
					+ pi.next());
		}
	}

	/** Assert that the two shape are intersecting and the closest point on the first shape is also
	 * on the second shape.
	 *
	 * @param shape1 the first shape, on which the closest point is computed.
	 * @param shape2 the second point.
	 */
	public void assertClosestPointInBothShapes(Shape2afp shape1, Shape2afp shape2) {
	    final Point2D<?, ?> point = shape1.getClosestPointTo(shape2);
	    double distance;
	    //TODO: The following test may fail since MathConstants#SPLINE_APPROXIMATION_RATIO is too high; see Issue #89.
	    //distance = shape1.getDistance(point); 
	    //assertEpsilonZero("Closest point " + point + " is not in the first shape: " + shape1 + ". Distance: " + distance, distance);
        distance = shape2.getDistance(point); 
	    assertEpsilonZero("Closest point " + point + " is not in the second shape: " + shape2 + ". Distance: " + distance, distance); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
	public abstract void getClosestPointToEllipse2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToCircle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToRectangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToSegment2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToTriangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToPath2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToOrientedRectangle2afp(CoordinateSystem2D cs);
		
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToParallelogram2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToRoundRectangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getClosestPointToMultiShape2afp(CoordinateSystem2D cs);

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
	public abstract void containsRectangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void containsShape2D(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsRectangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsCircle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsTriangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsEllipse2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsSegment2afp(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsPath2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsPathIterator2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsOrientedRectangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsParallelogram2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void intersectsRoundRectangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredRectangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredCircle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredTriangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredEllipse2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredSegment2afp(CoordinateSystem2D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredPath2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredOrientedRectangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredParallelogram2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredRoundRectangle2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void getDistanceSquaredMultiShape2afp(CoordinateSystem2D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void translateDoubleDouble(CoordinateSystem2D cs); 

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
	public abstract void containsDoubleDouble(CoordinateSystem2D cs);

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
	public static File generateTestPicture(Shape2afp<?, ?, ?, ?, ?, ?> shape) throws IOException {
		File filename = File.createTempFile("testShape", ".png"); //$NON-NLS-1$ //$NON-NLS-2$
		Rectangle2afp box = shape.toBoundingBox();
		PathIterator2afp<?> iterator = shape.getPathIterator();
		Path2D path = new Path2D.Double(
				iterator.getWindingRule() == PathWindingRule.NON_ZERO ? Path2D.WIND_NON_ZERO : Path2D.WIND_EVEN_ODD);
		while (iterator.hasNext()) {
			PathElement2afp element = iterator.next();
			double tox = (element.getToX() - box.getMinX()) * 2.;
			double toy = (box.getMaxY() - (element.getToY() - box.getMinY())) * 2.;
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
						(element.getCtrlX1() - box.getMinX()) * 2.,
						(box.getMaxY() - (element.getCtrlY1() - box.getMinY())) * 2.,
						(element.getCtrlX2() - box.getMinX()) * 2.,
						(box.getMaxY() - (element.getCtrlY2() - box.getMinY())) * 2.,
						tox, toy);
				break;
			case QUAD_TO:
				path.quadTo(
						(element.getCtrlX1() - box.getMinX()) * 2.,
						(box.getMaxY() - (element.getCtrlY1() - box.getMinY())) * 2.,
						tox, toy);
				break;
			case ARC_TO:
				throw new IllegalArgumentException();
			default:
			}
		}
		BufferedImage image = new BufferedImage(
				(int) Math.floor(box.getWidth() * 2.) + 1,
				(int) Math.floor(box.getHeight() * 2.) + 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.draw(path);
		ImageIO.write(image, "png", filename); //$NON-NLS-1$
		return filename;
	}	

}