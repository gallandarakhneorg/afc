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

package org.arakhne.afc.math.test.geometry.d3.afp;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.RectangularPrism3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Shape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public abstract class AbstractShape3afpTest<T extends Shape3afp<?, ?, ?, ?, ?, ?>,
		B extends RectangularPrism3afp<?, ?, ?, ?, ?, B>> extends AbstractMathTestCase {
	
	/** Is the rectangular shape to test.
	 */
	protected T shape;
	
	/** Shape factory.
	 */
	protected TestShapeFactory3afp<? extends Point3D, ? extends Vector3D, B> factory;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.factory = createFactory();
		this.shape = createShape();
	}
	
	protected abstract TestShapeFactory3afp<? extends Point3D, ? extends Vector3D, B> createFactory();
	
	/** Create the shape to test.
	 * 
	 * @return the shape to test.
	 */
	protected abstract T createShape();
	
	public final Segment3afp<?, ?, ?, ?, ?, B> createSegment(double x1, double y1, double z1, double x2, double y2, double z2) {
		return this.factory.createSegment(x1, y1, z1, x2, y2, z2);
	}
	
	public final B createRectangularPrism(double x, double y, double z, double width, double height, double depth) {
		return this.factory.createRectangularPrism(x, y, z, width, height, depth);
	}

	
	public final Sphere3afp<?, ?, ?, ?, ?, B> createSphere(double x, double y, double z, double radius) {
		return this.factory.createSphere(x, y, z, radius);
	}
	
	public final MultiShape3afp<?, ?, ?, ?, ?, ?, B> createMultiShape() {
		return this.factory.createMultiShape();
	}

	public final Point3D createPoint(double x, double y, double z) {
		return this.factory.createPoint(x, y, z);
	}

	public final Vector3D createVector(double x, double y, double z) {
		return this.factory.createVector(x, y, z);
	}

	public final Path3afp<?, ?, ?, ?, ?, B> createPath() {
		return this.factory.createPath(null);
	}

	public final Path3afp<?, ?, ?, ?, ?, B> createPath(PathWindingRule rule) {
		return this.factory.createPath(rule);
	}

	public final Path3afp<?, ?, ?, ?, ?, B> createPolyline(double... coordinates) {
		Path3afp<?, ?, ?, ?, ?, B>  path = createPath();
		path.moveTo(coordinates[0], coordinates[1], coordinates[2]);
		for (int i = 3; i < coordinates.length; i += 3) {
			path.lineTo(coordinates[i], coordinates[i + 1], coordinates[i + 2]);
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
	protected void assertElement(PathIterator3afp<?> pi, PathElementType type, double... coords) {
		if (!pi.hasNext()) {
			fail("expected path element but the iterator is empty"); //$NON-NLS-1$
		}
		PathElement3afp pe = pi.next();
		if (!type.equals(pe.getType())) {
			failCompare("not same element type.", type.name(), pe.getType().name()); //$NON-NLS-1$
		}
		double[] c = new double[coords.length];
		pe.toArray(c);
		if (!isEpsilonEquals(c, coords)) {
			failCompare("not same coordinates.", //$NON-NLS-1$ 
					Arrays.toString(coords),
					Arrays.toString(c));
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
	 * @param pi a path iterator.
	 */
	protected void assertNoElement(PathIterator3afp<?> pi) {
		if (pi.hasNext()) {
			fail("expected no path element but the iterator is not empty: " //$NON-NLS-1$
					+ pi.next());
		}
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void testClone(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void equalsObject(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void equalsObject_withPathIterator(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void equalsToPathIterator(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void equalsToShape(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void isEmpty(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void clear(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void containsPoint3D(CoordinateSystem3D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void getClosestPointTo(CoordinateSystem3D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void getFarthestPointTo(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void getDistance(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void getDistanceSquared(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void getDistanceL1(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void getDistanceLinf(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void setIT(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void getPathIterator(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void getPathIteratorTransform3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void createTransformedShape(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void translateVector3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void toBoundingBox(CoordinateSystem3D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void toBoundingBoxB(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void containsRectangularPrism3afp(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void intersectsRectangularPrism3afp(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void intersectsSphere3afp(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void intersectsSegment3afp(CoordinateSystem3D cs);
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void intersectsPath3afp(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void intersectsPathIterator3afp(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void translateDoubleDouble(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void containsDoubleDouble(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void getGeomFactory(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertNotNull(this.shape.getGeomFactory());
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void intersectsShape3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void operator_addVector3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void operator_plusVector3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void operator_removeVector3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void operator_minusVector3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void operator_multiplyTransform3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void operator_andPoint3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void operator_andShape3D(CoordinateSystem3D cs);

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Disabled
	public abstract void operator_upToPoint3D(CoordinateSystem3D cs);

	/** Generate a bitmap containing the given Shape2D.
	 *
	 * @param shape.
	 * @return the filename
	 * @throws IOException Input/output exception
	 */
	public static File generateTestPicture(Shape3afp<?, ?, ?, ?, ?, ?> shape) throws IOException {
		File filename = File.createTempFile("testShape", ".png"); //$NON-NLS-1$ //$NON-NLS-2$
		RectangularPrism3afp box = shape.toBoundingBox();
		PathIterator3afp<?> iterator = shape.getPathIterator();
		Path2D path = new Path2D.Double(
				iterator.getWindingRule() == PathWindingRule.NON_ZERO ? Path2D.WIND_NON_ZERO : Path2D.WIND_EVEN_ODD);
		while (iterator.hasNext()) {
			PathElement3afp element = iterator.next();
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
