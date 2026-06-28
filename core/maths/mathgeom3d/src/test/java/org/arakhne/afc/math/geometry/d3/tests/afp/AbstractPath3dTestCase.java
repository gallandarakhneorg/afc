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

package org.arakhne.afc.math.geometry.d3.tests.afp;

import static org.arakhne.afc.math.geometry.base.GeomConstants.SPLINE_APPROXIMATION_RATIO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.arakhne.afc.math.geometry.base.PathElementType;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.InnerComputationPoint3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.base.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.d.Shape3d;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public abstract class AbstractPath3dTestCase<T extends Path3afp<T, ?, ?, ?, ?, B>, B extends AlignedBox3afp<?, ?, ?, ?, ?, B>>
		extends AbstractShape3dTestCase<T, B> {

	@Override
	protected T createShape() {
		T path = (T) createPath();
		path.moveTo(0, 0, 0);
		path.lineTo(1, .5, -5);
		//b=Curve((1-t)^(2) QA + 2 (1-t) t QB + t^(2) QC, t,0,1)
		path.quadTo(3, 0, 2, 4, 3, -2);
		//a=Curve(
		//	x(CA) (1-t)^(3)+3 x(CB) (1-t)^(2) t+3 x(CC) (1-t) t^(2)+x(CD) t^(3),
		//	y(CA) (1-t)^(3)+3 y(CB) (1-t)^(2) t+3 y(CC) (1-t) t^(2)+y(CD) t^(3),
		//	z(CA) (1-t)^(3)+3 z(CB) (1-t)^(2) t+3 z(CC) (1-t) t^(2)+z(CD) t^(3),
		//	t,0,1)
		path.curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2);
		return path;
	}

	@Override
	@Test
	@DisplayName("getType(Class)")
	public final void getType_Class() {
		assertSame(Shape3DType.PATH, this.shape.getType(Shape3DType.class));
	}

	@Override
	@Test
	@DisplayName("getType")
	public final void getType() {
		assertSame(Shape3DType.PATH, this.shape.getType());
	}

	@DisplayName("calculatesControlPointBoundingBox(PathIterator3afp,AlignedBox3afp)")
	@Test
	public final void calculatesControlPointBoundingBox() {
		B box = createAlignedBox(0, 0, 0, 0, 0, 0);
		Path3afp.calculatesControlPointBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(5, box.getMaxY());
		assertEpsilonEquals(5, box.getMaxZ());
	}

	@DisplayName("calculatesDrawableElementBoundingBox(PathIterator3afp,AlignedBox3afp)")
	@Test
	public final void calculatesDrawableElementBoundingBox() {
		B box = createAlignedBox(0, 0, 0, 0, 0, 0);
		Path3afp.calculatesDrawableElementBoundingBox(this.shape.getPathIterator(), box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(7, box.getMaxX());

		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(3, box.getMaxY());

		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(3.421875, box.getMaxZ());
	}

	@DisplayName("getClosestPointTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getClosestPointToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		result = this.shape.getClosestPointTo(createPoint(-2, 1, 0));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createPoint(-2, 1, 5));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createPoint(-2, 1, -5));
		assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);

		result = this.shape.getClosestPointTo(createPoint(1, 0, 0));
		assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);

		result = this.shape.getClosestPointTo(createPoint(1, 0, 5));
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		result = this.shape.getClosestPointTo(createPoint(1, 0, -5));
		assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);

		result = this.shape.getClosestPointTo(createPoint(1, 1, 0));
		assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);

		result = this.shape.getClosestPointTo(createPoint(1, 1, 5));
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		result = this.shape.getClosestPointTo(createPoint(1, 1, -5));
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = this.shape.getClosestPointTo(createPoint(3, 0, 0));
		assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);

		result = this.shape.getClosestPointTo(createPoint(3, 0, 5));
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		result = this.shape.getClosestPointTo(createPoint(3, 0, -5));
		assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		
		result = this.shape.getClosestPointTo(createPoint(1, -4, 0));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createPoint(1, -4, 5));
		assertEpsilonEquals(createPoint(6.699003011013108, -2.529020888903435, 2.7233273740365576), result);
		
		result = this.shape.getClosestPointTo(createPoint(1, -4, -5));
		assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);
	}

	@DisplayName("findsClosestPointToPoint(PathIterator3afp,double,double,double,Point3D) open path")
	@Test
	public final void findsClosestPointToPoint_open() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 5, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, -5, result);
		assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 5, result);
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, -5, result);
		assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 0, result);
		assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 5, result);
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, -5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 5, result);
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, -5, result);
		assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 5, result);
		assertEpsilonEquals(createPoint(6.699003011013108, -2.529020888903435, 2.7233273740365576), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, -5, result);
		assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);
	}

	@DisplayName("findsClosestPointToPoint(PathIterator3afp,double,double,double,Point3D) close path")
	@Test
	public final void findsClosestPointToPoint_close() {
		this.shape.closePath();

		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 5, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, -5, result);
		assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(createPoint(0.6282051282051286, -0.4487179487179489, 0.17948717948717952), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 5, result);
		assertEpsilonEquals(createPoint(1.5256410256410255, -1.0897435897435899, 0.4358974358974359), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, -5, result);
		assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 0, result);
		assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 5, result);
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, -5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 5, result);
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, -5, result);
		assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(createPoint(2.4230769230769234, -1.7307692307692308, 0.6923076923076923), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 5, result);
		assertEpsilonEquals(createPoint(3.3205128205128203, -2.371794871794872, 0.9487179487179487), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, -5, result);
		assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);
	}

	@DisplayName("findsClosestPointToSegment(PathIterator3afp,double,double,double,double,double,double,Point3D)")
	@Test
	public final void findsClosestPointToSegment() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, -5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(0.6328774470367391, 0.31643872351836955, -3.1643872351836952), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, -5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(1.202170841513377, 0.47065261978031625, -4.34131435506932), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 0, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(5.53286881, 1.2138319933, 3.022991748), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, -5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(4.840444885643851, 1.4436409922956546, 1.4300824432249362), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, -5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(4.154833414196007, 2.4774372270884744, -1.2548641941817131), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(5.489624176405976, 1.240859889746265, 2.965332235207968), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsClosestPointToSegment(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, -5, 10, 10, 10, result);
		assertEpsilonEquals(createPoint(4.6789346310202795, 1.5210313226361163, 0.9085389126696517), result);
	}

	@DisplayName("findsFarthestPointToPoint(PathIterator3afp,double,double,double,Point3D) open path")
	@Test
	public final void findsFarthestPointToPoint_open() {
		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, -5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, -5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 0, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, -5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, -5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(createPoint(4, 3, -2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, -5, result);
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
	}

	@DisplayName("findsFarthestPointToPoint(PathIterator3afp,double,double,double,Point3D) close path")
	@Test
	public final void findsFarthestPointToPoint_close() {
		this.shape.closePath();

		Point3D result;

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 0, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, 5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), -2, 1, -5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 0, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, 5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 0, -5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 0, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, 5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, 1, -5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 0, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, 5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 3, 0, -5, result);
		assertEpsilonEquals(createPoint(7, -5, 2), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 0, result);
		assertEpsilonEquals(createPoint(4, 3, -2), result);

		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, 5, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);
		
		result = createPoint(Double.NaN, Double.NaN, Double.NaN);
		Path3afp.findsFarthestPointToPoint(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), 1, -4, -5, result);
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
	}

	@DisplayName("intersectsPathIteratorRectangle(PathIterator3afp,double,double,double,double,double,double) open path")
	@Test
	public final void intersectsPathIteratorRectangle_open() {
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 0, 2, 1, 0));
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 0, 2, 1, 0));
	}

	@DisplayName("intersectsPathIteratorRectangle(PathIterator3afp,double,double,double,double,double,double) close path")
	@Test
	public final void intersectsPathIteratorRectangle_close() {
		this.shape.closePath();
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1, -2, 0, 2, 1, 0));
		assertTrue(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 1.5, 1.5, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), 7, 3, 0, 2, 1, 0));
		assertFalse(Path3afp.intersectsPathIteratorRectangle(this.shape.getPathIterator(), -4, -0.5, 0, 2, 1, 0));
	}

	@DisplayName("calculatesLength(PathIterator3afp) open path")
	@Test
	public final void calculatesLength_open() {
		assertEpsilonEquals(25.40382315, Path3afp.calculatesLength(this.shape.getPathIterator()));
	}

	@DisplayName("calculatesLength(PathIterator3afp) close path")
	@Test
	public final void calculatesLength_close() {
		this.shape.closePath();
		assertEpsilonEquals(34.23558402124097, Path3afp.calculatesLength(this.shape.getPathIterator()));
	}

	@DisplayName("add(Iterator) open path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void addIterator_open(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5, 2);
		p2.lineTo(4, 6, 18);
		p2.lineTo(0, 8, 7);
		p2.lineTo(5, -3, 8);
		p2.closePath();

		Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
		iterator.next();
		this.shape.add(iterator);

		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.LINE_TO, 4, 6, 18);
		assertElement(pi, PathElementType.LINE_TO, 0, 8, 7);
		assertElement(pi, PathElementType.LINE_TO, 5, -3, 8);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@DisplayName("add(Iterator) close-after path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void addIterator_closeAfter(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5, -6);
		p2.lineTo(4, 6, 18);
		p2.lineTo(0, 8, 7);
		p2.lineTo(5, -3, 8);
		p2.closePath();

		Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
		iterator.next();

		this.shape.add(iterator);

		this.shape.closePath();

		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.LINE_TO, 4, 6, 18);
		assertElement(pi, PathElementType.LINE_TO, 0, 8, 7);
		assertElement(pi, PathElementType.LINE_TO, 5, -3, 8);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@DisplayName("add(Iterator) close-before path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void addIterator_closeBefore(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();

		Path3afp<?, ?, ?, ?, ?, B> p2 = createPath();
		p2.moveTo(7, -5, 2);
		p2.lineTo(4, 6, 18);
		p2.lineTo(0, 8, 7);
		p2.lineTo(5, -3, 8);
		p2.closePath();

		Iterator<? extends PathElement3afp> iterator = p2.getPathIterator();
		iterator.next();

		this.shape.add(iterator);

		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 4, 6, 18);
		assertElement(pi, PathElementType.LINE_TO, 0, 8, 7);
		assertElement(pi, PathElementType.LINE_TO, 5, -3, 8);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@DisplayName("curveTo(double,double,double,double,double,double,double,double,double) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void curveToDoubleDoubleDoubleDoubleDoubleDouble_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(15, 145, 0, 50, 20, 0, 0, 0, 0);
		});
	}
	
	@DisplayName("curveTo(double,double,double,double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void curveToDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.curveTo(123.456, 456.789, 0, 789.123, 159.753, -18, 456.852, 963.789, 24);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.CURVE_TO, 123.456, 456.789, 0, 789.123, 159.753, -18, 456.852, 963.789, 24);
		assertNoElement(pi);
	}
	
	@DisplayName("curveTo(Point3D,Point3D,Point3D) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void curveToPoint3DPoint3DPoint3D_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.curveTo(createPoint(15, 145, 0), createPoint(50, 20, 0), createPoint(0, 0, 0));
		});
	}

	@DisplayName("curveTo(Point3D,Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void curveToPoint3DPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.curveTo(createPoint(123.456, 456.789, 0), createPoint(789.123, 159.753, -5), createPoint(456.852, 963.789, 45));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.CURVE_TO, 123.456, 456.789, 0, 789.123, 159.753, -5, 456.852, 963.789, 45);
		assertNoElement(pi);
	}

	@DisplayName("getCoordAt(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getCoordAt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.getCoordAt(0)==0);
		assertTrue(this.shape.getCoordAt(1)==0);
		assertTrue(this.shape.getCoordAt(2)==0);
		assertTrue(this.shape.getCoordAt(3)==1);
		assertTrue(this.shape.getCoordAt(4)==.5);
		assertTrue(this.shape.getCoordAt(5)==-5);
		assertTrue(this.shape.getCoordAt(6)==3);
		assertTrue(this.shape.getCoordAt(7)==0);
		assertTrue(this.shape.getCoordAt(8)==2);
		assertTrue(this.shape.getCoordAt(9)==4);
		assertTrue(this.shape.getCoordAt(10)==3);
		assertTrue(this.shape.getCoordAt(11)==-2);
		assertTrue(this.shape.getCoordAt(12)==5);
		assertTrue(this.shape.getCoordAt(13)==-1);
		assertTrue(this.shape.getCoordAt(14)==3);
		assertTrue(this.shape.getCoordAt(15)==6);
		assertTrue(this.shape.getCoordAt(16)==5);
		assertTrue(this.shape.getCoordAt(17)==5);
		assertTrue(this.shape.getCoordAt(18)==7);
		assertTrue(this.shape.getCoordAt(19)==-5);
		assertTrue(this.shape.getCoordAt(20)==2);
	}
	
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getPointAt")
	public final void getPointAt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(createPoint(0, 0, 0), this.shape.getPointAt(0));
		assertEpsilonEquals(createPoint(1, .5, -5), this.shape.getPointAt(1));
		assertEpsilonEquals(createPoint(3, 0, 2), this.shape.getPointAt(2));
		assertEpsilonEquals(createPoint(4, 3, -2), this.shape.getPointAt(3));
		assertEpsilonEquals(createPoint(5, -1, 3), this.shape.getPointAt(4));
		assertEpsilonEquals(createPoint(6, 5, 5), this.shape.getPointAt(5));
		assertEpsilonEquals(createPoint(7, -5, 2), this.shape.getPointAt(6));
		assertThrows(AssertionError.class, () -> {
			this.shape.getPointAt(7);
		});
		//
		this.shape.closePath();
		assertEpsilonEquals(createPoint(0, 0, 0), this.shape.getPointAt(0));
		assertEpsilonEquals(createPoint(1, .5, -5), this.shape.getPointAt(1));
		assertEpsilonEquals(createPoint(3, 0, 2), this.shape.getPointAt(2));
		assertEpsilonEquals(createPoint(4, 3, -2), this.shape.getPointAt(3));
		assertEpsilonEquals(createPoint(5, -1, 3), this.shape.getPointAt(4));
		assertEpsilonEquals(createPoint(6, 5, 5), this.shape.getPointAt(5));
		assertEpsilonEquals(createPoint(7, -5, 2), this.shape.getPointAt(6));
		assertThrows(AssertionError.class, () -> {
			this.shape.getPointAt(7);
		});
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getPathElementCount")
	public final void size(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(7, this.shape.size());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getPathElementCount")
	public final void getPathElementCount(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(4, this.shape.getPathElementCount());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("getPathElementTypeAt(int)")
	public final void getPathElementTypeAt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertSame(PathElementType.MOVE_TO, this.shape.getPathElementTypeAt(0));
		assertSame(PathElementType.LINE_TO, this.shape.getPathElementTypeAt(1));
		assertSame(PathElementType.QUAD_TO, this.shape.getPathElementTypeAt(2));
		assertSame(PathElementType.CURVE_TO, this.shape.getPathElementTypeAt(3));
		assertThrows(AssertionError.class, () -> {
			this.shape.getPathElementTypeAt(4);
		});
		//
		this.shape.closePath();
		assertSame(PathElementType.MOVE_TO, this.shape.getPathElementTypeAt(0));
		assertSame(PathElementType.LINE_TO, this.shape.getPathElementTypeAt(1));
		assertSame(PathElementType.QUAD_TO, this.shape.getPathElementTypeAt(2));
		assertSame(PathElementType.CURVE_TO, this.shape.getPathElementTypeAt(3));
		assertSame(PathElementType.CLOSE, this.shape.getPathElementTypeAt(4));
		assertThrows(AssertionError.class, () -> {
			this.shape.getPathElementTypeAt(5);
		});
	}

	@DisplayName("toBoundingBox(Box3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void toBoundingBoxB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var box = this.shape.getGeomFactory().newBox();
		this.shape.toBoundingBox(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(7, box.getMaxX());

		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(3, box.getMaxY());

		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(3.421875, box.getMaxZ());
	}

	@DisplayName("toBoundingBox")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void toBoundingBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		B box = this.shape.toBoundingBox();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(7, box.getMaxX());

		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(3, box.getMaxY());

		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(3.421875, box.getMaxZ());
	}

	@DisplayName("getPathIterator(double) open path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getPathIteratorDouble_open(CoordinateSystem3D cs) throws Exception {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var pi = this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.0, 0.5, -5.0);
		// quadTo(3, 0, 2, 4, 3, -2)
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.4296875, -3.421875);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.46875, -2.1875);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.6171875, -1.296875);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 0.875, -0.75);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.2421875, -0.546875);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.71875, -0.6875);
		assertElement(pi, PathElementType.LINE_TO, 3.734375, 2.3046875, -1.171875);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, -2.0);
		// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
		assertElement(pi, PathElementType.LINE_TO, 4.25, 2.15625, -0.796875);
		assertElement(pi, PathElementType.LINE_TO, 4.625, 1.546875, 0.734375);
		assertElement(pi, PathElementType.LINE_TO, 5.0, 1.3671875, 1.9453125);
		assertElement(pi, PathElementType.LINE_TO, 5.375, 1.3125, 2.8125);
		assertElement(pi, PathElementType.LINE_TO, 5.75, 1.078125, 3.3125);
		assertElement(pi, PathElementType.LINE_TO, 6.125, 0.359375, 3.421875);
		assertElement(pi, PathElementType.LINE_TO, 6.5, -1.1484375, 3.1171875);
		assertElement(pi, PathElementType.LINE_TO, 6.875, -3.75, 2.375);
		assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 2.0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(double) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getPathIteratorDouble_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		var pi = this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.0, 0.5, -5.0);
		// quadTo(3, 0, 2, 4, 3, -2)
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.4296875, -3.421875);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.46875, -2.1875);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.6171875, -1.296875);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 0.875, -0.75);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.2421875, -0.546875);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.71875, -0.6875);
		assertElement(pi, PathElementType.LINE_TO, 3.734375, 2.3046875, -1.171875);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, -2.0);
		// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
		assertElement(pi, PathElementType.LINE_TO, 4.25, 2.15625, -0.796875);
		assertElement(pi, PathElementType.LINE_TO, 4.625, 1.546875, 0.734375);
		assertElement(pi, PathElementType.LINE_TO, 5.0, 1.3671875, 1.9453125);
		assertElement(pi, PathElementType.LINE_TO, 5.375, 1.3125, 2.8125);
		assertElement(pi, PathElementType.LINE_TO, 5.75, 1.078125, 3.3125);
		assertElement(pi, PathElementType.LINE_TO, 6.125, 0.359375, 3.421875);
		assertElement(pi, PathElementType.LINE_TO, 6.5, -1.1484375, 3.1171875);
		assertElement(pi, PathElementType.LINE_TO, 6.875, -3.75, 2.375);
		assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 2.0);
		// closePath
		assertElement(pi, PathElementType.CLOSE, 0., 0., 0.);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(Transform3D, double) no-transform")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getPathIteratorTransform3DDouble_null(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var pi = this.shape.getPathIterator(null, SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.0, 0.5, -5.0);
		// quadTo(3, 0, 2, 4, 3, -2)
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.4296875, -3.421875);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.46875, -2.1875);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.6171875, -1.296875);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 0.875, -0.75);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.2421875, -0.546875);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.71875, -0.6875);
		assertElement(pi, PathElementType.LINE_TO, 3.734375, 2.3046875, -1.171875);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, -2.0);
		// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
		assertElement(pi, PathElementType.LINE_TO, 4.25, 2.15625, -0.796875);
		assertElement(pi, PathElementType.LINE_TO, 4.625, 1.546875, 0.734375);
		assertElement(pi, PathElementType.LINE_TO, 5.0, 1.3671875, 1.9453125);
		assertElement(pi, PathElementType.LINE_TO, 5.375, 1.3125, 2.8125);
		assertElement(pi, PathElementType.LINE_TO, 5.75, 1.078125, 3.3125);
		assertElement(pi, PathElementType.LINE_TO, 6.125, 0.359375, 3.421875);
		assertElement(pi, PathElementType.LINE_TO, 6.5, -1.1484375, 3.1171875);
		assertElement(pi, PathElementType.LINE_TO, 6.875, -3.75, 2.375);
		assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 2.0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(Transform3D,double) identity-transform")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getPathIteratorTransform3DDouble_identity(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var pi = this.shape.getPathIterator(new Transform3D(), SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 0.0, 0.0, 0.0);
		assertElement(pi, PathElementType.LINE_TO, 1.0, 0.5, -5.0);
		// quadTo(3, 0, 2, 4, 3, -2)
		assertElement(pi, PathElementType.LINE_TO, 1.484375, 0.4296875, -3.421875);
		assertElement(pi, PathElementType.LINE_TO, 1.9375, 0.46875, -2.1875);
		assertElement(pi, PathElementType.LINE_TO, 2.359375, 0.6171875, -1.296875);
		assertElement(pi, PathElementType.LINE_TO, 2.75, 0.875, -0.75);
		assertElement(pi, PathElementType.LINE_TO, 3.109375, 1.2421875, -0.546875);
		assertElement(pi, PathElementType.LINE_TO, 3.4375, 1.71875, -0.6875);
		assertElement(pi, PathElementType.LINE_TO, 3.734375, 2.3046875, -1.171875);
		assertElement(pi, PathElementType.LINE_TO, 4.0, 3.0, -2.0);
		// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
		assertElement(pi, PathElementType.LINE_TO, 4.25, 2.15625, -0.796875);
		assertElement(pi, PathElementType.LINE_TO, 4.625, 1.546875, 0.734375);
		assertElement(pi, PathElementType.LINE_TO, 5.0, 1.3671875, 1.9453125);
		assertElement(pi, PathElementType.LINE_TO, 5.375, 1.3125, 2.8125);
		assertElement(pi, PathElementType.LINE_TO, 5.75, 1.078125, 3.3125);
		assertElement(pi, PathElementType.LINE_TO, 6.125, 0.359375, 3.421875);
		assertElement(pi, PathElementType.LINE_TO, 6.5, -1.1484375, 3.1171875);
		assertElement(pi, PathElementType.LINE_TO, 6.875, -3.75, 2.375);
		assertElement(pi, PathElementType.LINE_TO, 7.0, -5.0, 2.0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(Transform3D,double) translation")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getPathIteratorTransform3DDouble_translation(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Transform3D transform = new Transform3D();
		transform.setTranslation(10, 5, -2);
		var pi = this.shape.getPathIterator(transform, SPLINE_APPROXIMATION_RATIO);
		assertElement(pi, PathElementType.MOVE_TO, 10.0, 5.0, -2.0);
		assertElement(pi, PathElementType.LINE_TO, 11.0, 5.5, -7.0);
		// quadTo(3, 0, 2, 4, 3, -2)
		assertElement(pi, PathElementType.LINE_TO, 11.484375, 5.4296875, -5.421875);
		assertElement(pi, PathElementType.LINE_TO, 11.9375, 5.46875, -4.1875);
		assertElement(pi, PathElementType.LINE_TO, 12.359375, 5.6171875, -3.296875);
		assertElement(pi, PathElementType.LINE_TO, 12.75, 5.875, -2.75);
		assertElement(pi, PathElementType.LINE_TO, 13.109375, 6.2421875, -2.546875);
		assertElement(pi, PathElementType.LINE_TO, 13.4375, 6.71875, -2.6875);
		assertElement(pi, PathElementType.LINE_TO, 13.734375, 7.3046875, -3.171875);
		assertElement(pi, PathElementType.LINE_TO, 14.0, 8.0, -4.0);
		// curveTo(5, -1, 3, 6, 5, 5, 7, -5, 2)
		assertElement(pi, PathElementType.LINE_TO, 14.25, 7.15625, -2.796875);
		assertElement(pi, PathElementType.LINE_TO, 14.625, 6.546875, -1.26563);
		assertElement(pi, PathElementType.LINE_TO, 15.0, 6.3671875, -0.0546875);
		assertElement(pi, PathElementType.LINE_TO, 15.375, 6.3125, 0.8125);
		assertElement(pi, PathElementType.LINE_TO, 15.75, 6.078125, 1.3125);
		assertElement(pi, PathElementType.LINE_TO, 16.125, 5.359375, 1.421875);
		assertElement(pi, PathElementType.LINE_TO, 16.5, 3.8515625, 1.1171875);
		assertElement(pi, PathElementType.LINE_TO, 16.875, 1.25, 0.375);
		assertElement(pi, PathElementType.LINE_TO, 17.0, 0.0, 0.0);
		assertNoElement(pi);
	}

	@DisplayName("getLength")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getLength(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(25.40382315, this.shape.getLength());
	}

	@DisplayName("getLengthSquared")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getLengthSquared(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(645.354230636, this.shape.getLengthSquared());
	}

	@DisplayName("lineTo(double,double,double) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void lineToDoubleDoubleDouble_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(15, 145, 0);
		});
	}

	@DisplayName("lineTo(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void lineToDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.lineTo(123.456, 456.789, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);	
	}

	@DisplayName("lineTo(Point3D) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void lineToPoint3D_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.lineTo(createPoint(15, 145, 0));
		});
	}

	@DisplayName("lineTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void lineToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.lineTo(createPoint(123.456, 456.789, -42));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.LINE_TO, 123.456, 456.789, -42);
		assertNoElement(pi);
	}

	@DisplayName("moveTo(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void moveToDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.moveTo(123.456, 456.789, 0);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@DisplayName("moveTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void moveToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.moveTo(createPoint(123.456, 456.789, 0));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
		assertNoElement(pi);
	}

	@DisplayName("quadTo(double,double,double,double,double,double) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void quadToDoubleDoubleDoubleDoubleDoubleDouble_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.quadTo(15, 145, 0, 50, 20, 0);
		});
	}

	@DisplayName("quadTo(double,double,double,double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void quadToDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.quadTo(123.456, 456.789, 0, 789.123, 159.753, 62);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.QUAD_TO, 123.456, 456.789, 0, 789.123, 159.753, 62);
		assertNoElement(pi);
	}
	
	@DisplayName("quadTo(Point3D,Point3D) no-move-to")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void quadToPoint3DPoint3D_noMoveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertThrows(IllegalStateException.class, () -> {
			Path3afp<?, ?, ?, ?, ?, ?> tmpShape = createPath();
			tmpShape.quadTo(createPoint(15, 145, 0), createPoint(50, 20, 0));
		});
	}

	@DisplayName("quadTo(Point3D,Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void quadToPoint3DPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.quadTo(createPoint(123.456, 456.789, -4), createPoint(789.123, 159.753, 6));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.QUAD_TO, 123.456, 456.789, -4, 789.123, 159.753, 6);
		assertNoElement(pi);
	}

	@DisplayName("removeLast - one call")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void removeLast_oneCall(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		this.shape.removeLast();

		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertNoElement(pi);
	}
	
	@DisplayName("removeLast - two calls")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public void removeLast_twoCalls(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		this.shape.removeLast();
		this.shape.removeLast();

		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertNoElement(pi);
	}

	@DisplayName("remove(double,double,double) w/ outside")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void removeDoubleDoubleDouble_outside(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	
		assertFalse(this.shape.remove(1005, -1, 0));
				
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);
	}

	@DisplayName("remove(double,double,double) w/ lineTo")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void removeDoubleDoubleDouble_lineTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	
		assertTrue(this.shape.remove(1, .5, -5));
				
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);
	}

	@DisplayName("remove(double,double,double) w/ moveTo")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void removeDoubleDoubleDouble_moveTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.remove(0., 0., 0.));
				
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);
	}

	@DisplayName("remove(double,double,double) w/ quadTo - control")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void removeDoubleDoubleDouble_quadTo_ctrl(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.remove(3, 0, 2));
				
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.LINE_TO, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);
	}

	@DisplayName("remove(double,double,double) w/ quadTo - end")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void removeDoubleDoubleDouble_quadTo_end(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.remove(4, 3, -2));
				
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);
	}

	@DisplayName("remove(double,double,double) w/ curveTo - control1")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void removeDoubleDoubleDouble_curveTo_ctrl1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.remove(5, -1, 3));
				
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.QUAD_TO, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);
	}

	@DisplayName("remove(double,double,double) w/ curveTo - control2")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void removeDoubleDoubleDouble_curveTo_ctrl2(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.remove(6, 5, 5));
				
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.QUAD_TO, 5, -1, 3, 7, -5, 2);
		assertNoElement(pi);
	}

	@DisplayName("remove(double,double,double) w/ curveTo - end")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void removeDoubleDoubleDouble_curveTo_end(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.remove(7, -5, 2));
				
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertNoElement(pi);
	}

	@DisplayName("setLastPoint(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setLastPointDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(7, -5, 2)));
		this.shape.setLastPoint(2, 2, 4);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(2, 2, 4)));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 2, 2, 4);
		assertNoElement(pi);
	}

	@DisplayName("setLastPoint(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void setLastPointPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(7, -5, 2)));
		this.shape.setLastPoint(createPoint(2, 2, -42));
		assertTrue(this.shape.getCurrentPoint().equals(createPoint(2, 2, -42)));
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 2, 2, -42);
		assertNoElement(pi);
	}

	@DisplayName("transform(Transform3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void transformTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D p1 = randomPoint3d();
		Point3D p2 = randomPoint3d();
		Point3D p3 = randomPoint3d();
		Point3D p4 = randomPoint3d();
		Point3D p5 = randomPoint3d();
		Point3D p6 = randomPoint3d();
		Point3D p7 = randomPoint3d();
		
		Path3afp path = createPath();
		path.moveTo(p1.getX(), p1.getY(), p1.getZ());
		path.lineTo(p2.getX(), p2.getY(), p2.getZ());
		path.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(),p4.getY(), p4.getZ());
		path.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
		path.closePath();
		
		Transform3D trans = new Transform3D(randomMatrix4d());
		
		trans.transform(p1);
		trans.transform(p2);
		trans.transform(p3);
		trans.transform(p4);
		trans.transform(p5);
		trans.transform(p6);
		trans.transform(p7);
		
		Path3afp pathTrans = createPath();
		pathTrans.moveTo(p1.getX(), p1.getY(), p1.getZ());
		pathTrans.lineTo(p2.getX(), p2.getY(), p2.getZ());
		pathTrans.quadTo(p3.getX(), p3.getY(), p3.getZ(), p4.getX(), p4.getY(), p4.getZ());
		pathTrans.curveTo(p5.getX(), p5.getY(), p5.getZ(), p6.getX(), p6.getY(), p6.getZ(), p7.getX(), p7.getY(), p7.getZ());
		pathTrans.closePath();
		
		path.transform(trans);
		
		assertTrue(path.equalsToPathIterator(pathTrans.getPathIterator()));
	}

	@DisplayName("clone")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void testClone(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp clone = this.shape.clone();
		PathIterator3afp pi = (PathIterator3afp) clone.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);
	}

	@DisplayName("equals(Object)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void equalsObject(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createPath()));
		assertFalse(this.shape.equals(createSegment(5, 8, 0, 5, 10, 0)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(this.shape.clone()));
	}

	@DisplayName("equals(Object -> PathIterator3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void equalsObject_withPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(this.shape.clone().getPathIterator()));

		assertFalse(this.shape.equals(createPath().getPathIterator()));

		var path = this.shape.getGeomFactory().newPath();
		path.moveTo(5, 8, 0);
		path.lineTo(5, 10, 0);
		assertFalse(this.shape.equals(path.getPathIterator()));
	}

	@DisplayName("equalsToPathIterator(PathIterator3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void equalsToPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertFalse(this.shape.equalsToPathIterator((PathIterator3afp) null));
		assertTrue(this.shape.equalsToPathIterator(this.shape.clone().getPathIterator()));

		assertFalse(this.shape.equalsToPathIterator(createPath().getPathIterator()));
		
		var path = this.shape.getGeomFactory().newPath();
		path.moveTo(5, 8, 0);
		path.lineTo(5, 10, 0);
		assertFalse(this.shape.equalsToPathIterator(path.getPathIterator()));
	}

	@DisplayName("equalsToShape(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void equalsToShape(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createPath()));
		assertTrue(this.shape.equalsToShape(this.shape));
		assertTrue(this.shape.equalsToShape(this.shape.clone()));
	}

	@DisplayName("isEmpty")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void isEmpty(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isEmpty());
		this.shape.clear();
		assertTrue(this.shape.isEmpty());
	}

	@DisplayName("clear")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void clear(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.clear();
		PathIterator3afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
	}

	@DisplayName("contains(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void containsDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.contains(0, 0, 0));
		assertTrue(this.shape.contains(1, .5, -5));
		// Control point
		assertFalse(this.shape.contains(3, 0, 2));
		assertTrue(this.shape.contains(4, 3, -2));
		// Control point
		assertFalse(this.shape.contains(5, -1, 3));
		// Control point
		assertFalse(this.shape.contains(6, 5, 5));
		assertTrue(this.shape.contains(7, -5, 2));

		assertTrue(this.shape.contains(1.484375, 0.4296875, -3.421875));
		assertTrue(this.shape.contains(1.9375, 0.46875, -2.1875));
		assertTrue(this.shape.contains(2.359375, 0.6171875, -1.296875));
		assertTrue(this.shape.contains(2.75, 0.875, -0.75));
		assertTrue(this.shape.contains(3.109375, 1.2421875, -0.546875));
		assertTrue(this.shape.contains(3.4375, 1.71875, -0.6875));
		assertTrue(this.shape.contains(3.734375, 2.3046875, -1.171875));
		assertTrue(this.shape.contains(4.0, 3.0, -2.0));
		assertTrue(this.shape.contains(4.25, 2.15625, -0.796875));
		assertTrue(this.shape.contains(4.625, 1.546875, 0.734375));
		assertTrue(this.shape.contains(5.0, 1.3671875, 1.9453125));
		assertTrue(this.shape.contains(5.375, 1.3125, 2.8125));
		assertTrue(this.shape.contains(5.75, 1.078125, 3.3125));
		assertTrue(this.shape.contains(6.125, 0.359375, 3.421875));
		assertTrue(this.shape.contains(6.5, -1.1484375, 3.1171875));
		assertTrue(this.shape.contains(6.875, -3.75, 2.375));
		assertTrue(this.shape.contains(7.0, -5.0, 2.0));

		assertFalse(this.shape.contains(-5, 1, 0));
		assertFalse(this.shape.contains(3, 6, 0));
		assertFalse(this.shape.contains(3, -10, 0));
		assertFalse(this.shape.contains(11, 1, 0));
		assertFalse(this.shape.contains(4, 1, 0));
		assertFalse(this.shape.contains(4, 3, 0));
		
		this.shape.closePath();
		
		assertTrue(this.shape.contains(0, 0, 0));
		assertTrue(this.shape.contains(1, .5, -5));
		// Control point
		assertFalse(this.shape.contains(3, 0, 2));
		assertTrue(this.shape.contains(4, 3, -2));
		// Control point
		assertFalse(this.shape.contains(5, -1, 3));
		// Control point
		assertFalse(this.shape.contains(6, 5, 5));
		assertTrue(this.shape.contains(7, -5, 2));

		assertTrue(this.shape.contains(1.484375, 0.4296875, -3.421875));
		assertTrue(this.shape.contains(1.9375, 0.46875, -2.1875));
		assertTrue(this.shape.contains(2.359375, 0.6171875, -1.296875));
		assertTrue(this.shape.contains(2.75, 0.875, -0.75));
		assertTrue(this.shape.contains(3.109375, 1.2421875, -0.546875));
		assertTrue(this.shape.contains(3.4375, 1.71875, -0.6875));
		assertTrue(this.shape.contains(3.734375, 2.3046875, -1.171875));
		assertTrue(this.shape.contains(4.0, 3.0, -2.0));
		assertTrue(this.shape.contains(4.25, 2.15625, -0.796875));
		assertTrue(this.shape.contains(4.625, 1.546875, 0.734375));
		assertTrue(this.shape.contains(5.0, 1.3671875, 1.9453125));
		assertTrue(this.shape.contains(5.375, 1.3125, 2.8125));
		assertTrue(this.shape.contains(5.75, 1.078125, 3.3125));
		assertTrue(this.shape.contains(6.125, 0.359375, 3.421875));
		assertTrue(this.shape.contains(6.5, -1.1484375, 3.1171875));
		assertTrue(this.shape.contains(6.875, -3.75, 2.375));
		assertTrue(this.shape.contains(7.0, -5.0, 2.0));

		assertFalse(this.shape.contains(-5, 1, 0));
		assertFalse(this.shape.contains(3, 6, 0));
		assertFalse(this.shape.contains(3, -10, 0));
		assertFalse(this.shape.contains(11, 1, 0));
		assertFalse(this.shape.contains(4, 1, 0));
		assertFalse(this.shape.contains(4, 3, 0));
	}
	
	@DisplayName("contains(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void containsPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.contains(createPoint(0, 0, 0)));
		assertTrue(this.shape.contains(createPoint(1, .5, -5)));
		// Control point
		assertFalse(this.shape.contains(createPoint(3, 0, 2)));
		assertTrue(this.shape.contains(createPoint(4, 3, -2)));
		// Control point
		assertFalse(this.shape.contains(createPoint(5, -1, 3)));
		// Control point
		assertFalse(this.shape.contains(createPoint(6, 5, 5)));
		assertTrue(this.shape.contains(createPoint(7, -5, 2)));

		assertTrue(this.shape.contains(createPoint(1.484375, 0.4296875, -3.421875)));
		assertTrue(this.shape.contains(createPoint(1.9375, 0.46875, -2.1875)));
		assertTrue(this.shape.contains(createPoint(2.359375, 0.6171875, -1.296875)));
		assertTrue(this.shape.contains(createPoint(2.75, 0.875, -0.75)));
		assertTrue(this.shape.contains(createPoint(3.109375, 1.2421875, -0.546875)));
		assertTrue(this.shape.contains(createPoint(3.4375, 1.71875, -0.6875)));
		assertTrue(this.shape.contains(createPoint(3.734375, 2.3046875, -1.171875)));
		assertTrue(this.shape.contains(createPoint(4.0, 3.0, -2.0)));
		assertTrue(this.shape.contains(createPoint(4.25, 2.15625, -0.796875)));
		assertTrue(this.shape.contains(createPoint(4.625, 1.546875, 0.734375)));
		assertTrue(this.shape.contains(createPoint(5.0, 1.3671875, 1.9453125)));
		assertTrue(this.shape.contains(createPoint(5.375, 1.3125, 2.8125)));
		assertTrue(this.shape.contains(createPoint(5.75, 1.078125, 3.3125)));
		assertTrue(this.shape.contains(createPoint(6.125, 0.359375, 3.421875)));
		assertTrue(this.shape.contains(createPoint(6.5, -1.1484375, 3.1171875)));
		assertTrue(this.shape.contains(createPoint(6.875, -3.75, 2.375)));
		assertTrue(this.shape.contains(createPoint(7.0, -5.0, 2.0)));

		assertFalse(this.shape.contains(createPoint(-5, 1, 0)));
		assertFalse(this.shape.contains(createPoint(3, 6, 0)));
		assertFalse(this.shape.contains(createPoint(3, -10, 0)));
		assertFalse(this.shape.contains(createPoint(11, 1, 0)));
		assertFalse(this.shape.contains(createPoint(4, 1, 0)));
		assertFalse(this.shape.contains(createPoint(4, 3, 0)));
		
		this.shape.closePath();
		
		assertTrue(this.shape.contains(createPoint(0, 0, 0)));
		assertTrue(this.shape.contains(createPoint(1, .5, -5)));
		// Control point
		assertFalse(this.shape.contains(createPoint(3, 0, 2)));
		assertTrue(this.shape.contains(createPoint(4, 3, -2)));
		// Control point
		assertFalse(this.shape.contains(createPoint(5, -1, 3)));
		// Control point
		assertFalse(this.shape.contains(createPoint(6, 5, 5)));
		assertTrue(this.shape.contains(createPoint(7, -5, 2)));

		assertTrue(this.shape.contains(createPoint(1.484375, 0.4296875, -3.421875)));
		assertTrue(this.shape.contains(createPoint(1.9375, 0.46875, -2.1875)));
		assertTrue(this.shape.contains(createPoint(2.359375, 0.6171875, -1.296875)));
		assertTrue(this.shape.contains(createPoint(2.75, 0.875, -0.75)));
		assertTrue(this.shape.contains(createPoint(3.109375, 1.2421875, -0.546875)));
		assertTrue(this.shape.contains(createPoint(3.4375, 1.71875, -0.6875)));
		assertTrue(this.shape.contains(createPoint(3.734375, 2.3046875, -1.171875)));
		assertTrue(this.shape.contains(createPoint(4.0, 3.0, -2.0)));
		assertTrue(this.shape.contains(createPoint(4.25, 2.15625, -0.796875)));
		assertTrue(this.shape.contains(createPoint(4.625, 1.546875, 0.734375)));
		assertTrue(this.shape.contains(createPoint(5.0, 1.3671875, 1.9453125)));
		assertTrue(this.shape.contains(createPoint(5.375, 1.3125, 2.8125)));
		assertTrue(this.shape.contains(createPoint(5.75, 1.078125, 3.3125)));
		assertTrue(this.shape.contains(createPoint(6.125, 0.359375, 3.421875)));
		assertTrue(this.shape.contains(createPoint(6.5, -1.1484375, 3.1171875)));
		assertTrue(this.shape.contains(createPoint(6.875, -3.75, 2.375)));
		assertTrue(this.shape.contains(createPoint(7.0, -5.0, 2.0)));

		assertFalse(this.shape.contains(createPoint(-5, 1, 0)));
		assertFalse(this.shape.contains(createPoint(3, 6, 0)));
		assertFalse(this.shape.contains(createPoint(3, -10, 0)));
		assertFalse(this.shape.contains(createPoint(11, 1, 0)));
		assertFalse(this.shape.contains(createPoint(4, 1, 0)));
		assertFalse(this.shape.contains(createPoint(4, 3, 0)));
	}

	@DisplayName("containsControlPoint(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void containsControlPointPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.containsControlPoint(createPoint(0, 0, 0)));
		assertTrue(this.shape.containsControlPoint(createPoint(1, .5, -5)));
		// Control point
		assertTrue(this.shape.containsControlPoint(createPoint(3, 0, 2)));
		assertTrue(this.shape.containsControlPoint(createPoint(4, 3, -2)));
		// Control point
		assertTrue(this.shape.containsControlPoint(createPoint(5, -1, 3)));
		// Control point
		assertTrue(this.shape.containsControlPoint(createPoint(6, 5, 5)));
		assertTrue(this.shape.containsControlPoint(createPoint(7, -5, 2)));

		assertFalse(this.shape.containsControlPoint(createPoint(1.484375, 0.4296875, -3.421875)));
		assertFalse(this.shape.containsControlPoint(createPoint(1.9375, 0.46875, -2.1875)));
		assertFalse(this.shape.containsControlPoint(createPoint(2.359375, 0.6171875, -1.296875)));
		assertFalse(this.shape.containsControlPoint(createPoint(2.75, 0.875, -0.75)));
		assertFalse(this.shape.containsControlPoint(createPoint(3.109375, 1.2421875, -0.546875)));
		assertFalse(this.shape.containsControlPoint(createPoint(3.4375, 1.71875, -0.6875)));
		assertFalse(this.shape.containsControlPoint(createPoint(3.734375, 2.3046875, -1.171875)));
		assertTrue(this.shape.containsControlPoint(createPoint(4.0, 3.0, -2.0)));
		assertFalse(this.shape.containsControlPoint(createPoint(4.25, 2.15625, -0.796875)));
		assertFalse(this.shape.containsControlPoint(createPoint(4.625, 1.546875, 0.734375)));
		assertFalse(this.shape.containsControlPoint(createPoint(5.0, 1.3671875, 1.9453125)));
		assertFalse(this.shape.containsControlPoint(createPoint(5.375, 1.3125, 2.8125)));
		assertFalse(this.shape.containsControlPoint(createPoint(5.75, 1.078125, 3.3125)));
		assertFalse(this.shape.containsControlPoint(createPoint(6.125, 0.359375, 3.421875)));
		assertFalse(this.shape.containsControlPoint(createPoint(6.5, -1.1484375, 3.1171875)));
		assertFalse(this.shape.containsControlPoint(createPoint(6.875, -3.75, 2.375)));
		assertTrue(this.shape.containsControlPoint(createPoint(7.0, -5.0, 2.0)));

		assertFalse(this.shape.containsControlPoint(createPoint(-5, 1, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(3, 6, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(3, -10, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(11, 1, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(4, 1, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(4, 3, 0)));
		
		this.shape.closePath();
		
		assertTrue(this.shape.containsControlPoint(createPoint(0, 0, 0)));
		assertTrue(this.shape.containsControlPoint(createPoint(1, .5, -5)));
		// Control point
		assertTrue(this.shape.containsControlPoint(createPoint(3, 0, 2)));
		assertTrue(this.shape.containsControlPoint(createPoint(4, 3, -2)));
		// Control point
		assertTrue(this.shape.containsControlPoint(createPoint(5, -1, 3)));
		// Control point
		assertTrue(this.shape.containsControlPoint(createPoint(6, 5, 5)));
		assertTrue(this.shape.containsControlPoint(createPoint(7, -5, 2)));

		assertFalse(this.shape.containsControlPoint(createPoint(1.484375, 0.4296875, -3.421875)));
		assertFalse(this.shape.containsControlPoint(createPoint(1.9375, 0.46875, -2.1875)));
		assertFalse(this.shape.containsControlPoint(createPoint(2.359375, 0.6171875, -1.296875)));
		assertFalse(this.shape.containsControlPoint(createPoint(2.75, 0.875, -0.75)));
		assertFalse(this.shape.containsControlPoint(createPoint(3.109375, 1.2421875, -0.546875)));
		assertFalse(this.shape.containsControlPoint(createPoint(3.4375, 1.71875, -0.6875)));
		assertFalse(this.shape.containsControlPoint(createPoint(3.734375, 2.3046875, -1.171875)));
		assertTrue(this.shape.containsControlPoint(createPoint(4.0, 3.0, -2.0)));
		assertFalse(this.shape.containsControlPoint(createPoint(4.25, 2.15625, -0.796875)));
		assertFalse(this.shape.containsControlPoint(createPoint(4.625, 1.546875, 0.734375)));
		assertFalse(this.shape.containsControlPoint(createPoint(5.0, 1.3671875, 1.9453125)));
		assertFalse(this.shape.containsControlPoint(createPoint(5.375, 1.3125, 2.8125)));
		assertFalse(this.shape.containsControlPoint(createPoint(5.75, 1.078125, 3.3125)));
		assertFalse(this.shape.containsControlPoint(createPoint(6.125, 0.359375, 3.421875)));
		assertFalse(this.shape.containsControlPoint(createPoint(6.5, -1.1484375, 3.1171875)));
		assertFalse(this.shape.containsControlPoint(createPoint(6.875, -3.75, 2.375)));
		assertTrue(this.shape.containsControlPoint(createPoint(7.0, -5.0, 2.0)));

		assertFalse(this.shape.containsControlPoint(createPoint(-5, 1, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(3, 6, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(3, -10, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(11, 1, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(4, 1, 0)));
		assertFalse(this.shape.containsControlPoint(createPoint(4, 3, 0)));
	}

	@DisplayName("getClosestPointTo(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getClosestPointToShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;
		
		result = this.shape.getClosestPointTo((Shape3d) createSphere(-2, 1, 0, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo((Shape3d) createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		var p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		result = this.shape.getClosestPointTo(p);
		assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);
	}

	@DisplayName("getDistance(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(2.1360679775, this.shape.getDistance((Shape3d) createSphere(-2, 1, 0, .1)));
		
		assertEpsilonEquals(2.0754980867, this.shape.getDistance((Shape3d) createSegment(-2, 1, 0, 10, 10, 10)));

		Path3afp p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		assertEpsilonEquals(1.1413633541, this.shape.getDistance((Shape3d) p));

		MultiShape3afp ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(1.1413633541, this.shape.getDistance((Shape3d) ms));
	}

	@DisplayName("getDistanceSquared(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceSquaredShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(4.5627864045, this.shape.getDistanceSquared((Shape3d) createSphere(-2, 1, 0, .1)));

		assertEpsilonEquals(4.3076923077, this.shape.getDistanceSquared((Shape3d) createSegment(-2, 1, 0, 10, 10, 10)));

		Path3afp p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		assertEpsilonEquals(1.3027103061, this.shape.getDistanceSquared((Shape3d) p));

		MultiShape3afp ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(1.3027103061, this.shape.getDistanceSquared((Shape3d) ms));
	}

	@DisplayName("getClosestPointTo(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getClosestPointToSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		// Outside Sphere
		
		result = this.shape.getClosestPointTo(createSphere(-2, 1, 0, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSphere(-2, 1, 5, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSphere(-2, 1, -5, .1));
		assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);

		result = this.shape.getClosestPointTo(createSphere(1, 0, 0, .1));
		assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);

		result = this.shape.getClosestPointTo(createSphere(1, 0, 5, .1));
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		result = this.shape.getClosestPointTo(createSphere(1, 0, -5, .1));
		assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);

		result = this.shape.getClosestPointTo(createSphere(1, 1, 0, .1));
		assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);

		result = this.shape.getClosestPointTo(createSphere(1, 1, 5, .1));
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		result = this.shape.getClosestPointTo(createSphere(1, 1, -5, .1));
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = this.shape.getClosestPointTo(createSphere(3, 0, 0, .1));
		assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);

		result = this.shape.getClosestPointTo(createSphere(3, 0, 5, .1));
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		result = this.shape.getClosestPointTo(createSphere(3, 0, -5, .1));
		assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		
		result = this.shape.getClosestPointTo(createSphere(1, -4, 0, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSphere(1, -4, 5, .1));
		assertEpsilonEquals(createPoint(6.699003011013108, -2.529020888903435, 2.7233273740365576), result);
		
		result = this.shape.getClosestPointTo(createSphere(1, -4, -5, .1));
		assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);

		// Inside Sphere - usually the closest point to the sphere
		
		result = this.shape.getClosestPointTo(createSphere(-2, 1, 0, 3));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSphere(-2, 1, 0, 3));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSphere(-2, 1, 0, 5));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSphere(-2, 1, 0, 7));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSphere(-2, 1, 0, 9));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSphere(-2, 1, 0, 15));
		assertEpsilonEquals(createPoint(0, 0, 0), result);
	}
	
	@DisplayName("getDistance(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(2.1360679775, this.shape.getDistance(createSphere(-2, 1, 0, .1)));
		assertEpsilonEquals(5.3772255751, this.shape.getDistance(createSphere(-2, 1, 5, .1)));
		assertEpsilonEquals(2.8936440607, this.shape.getDistance(createSphere(-2, 1, -5, .1)));
		assertEpsilonEquals(0.8807674352, this.shape.getDistance(createSphere(1, 0, 0, .1)));
		assertEpsilonEquals(4.964428645, this.shape.getDistance(createSphere(1, 0, 5, .1)));
		assertEpsilonEquals(0.3976133515, this.shape.getDistance(createSphere(1, 0, -5, .1)));
		assertEpsilonEquals(1.2835771443, this.shape.getDistance(createSphere(1, 1, 0, .1)));
		assertEpsilonEquals(4.8013709817, this.shape.getDistance(createSphere(1, 1, 5, .1)));
		assertEpsilonEquals(.4, this.shape.getDistance(createSphere(1, 1, -5, .1)));
		assertEpsilonEquals(1.0792476415, this.shape.getDistance(createSphere(3, 0, 0, .1)));
		assertEpsilonEquals(3.301839174, this.shape.getDistance(createSphere(3, 0, 5, .1)));
		assertEpsilonEquals(1.8699842474, this.shape.getDistance(createSphere(3, 0, -5, .1)));
		assertEpsilonEquals(4.0231056256, this.shape.getDistance(createSphere(1, -4, 0, .1)));
		assertEpsilonEquals(6.2107569364, this.shape.getDistance(createSphere(1, -4, 5, .1)));
		assertEpsilonEquals(4.3785201638, this.shape.getDistance(createSphere(1, -4, -5, .1)));
	}

	@DisplayName("getDistanceSquared(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceSquaredSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(4.5627864045, this.shape.getDistanceSquared(createSphere(-2, 1, 0, .1)));
		assertEpsilonEquals(28.914554885, this.shape.getDistanceSquared(createSphere(-2, 1, 5, .1)));
		assertEpsilonEquals(8.3731759498, this.shape.getDistanceSquared(createSphere(-2, 1, -5, .1)));
		assertEpsilonEquals(0.7757512749, this.shape.getDistanceSquared(createSphere(1, 0, 0, .1)));
		assertEpsilonEquals(24.645551771, this.shape.getDistanceSquared(createSphere(1, 0, 5, .1)));
		assertEpsilonEquals(0.1580963773, this.shape.getDistanceSquared(createSphere(1, 0, -5, .1)));
		assertEpsilonEquals(1.6475702854, this.shape.getDistanceSquared(createSphere(1, 1, 0, .1)));
		assertEpsilonEquals(23.0531633037, this.shape.getDistanceSquared(createSphere(1, 1, 5, .1)));
		assertEpsilonEquals(.16, this.shape.getDistanceSquared(createSphere(1, 1, -5, .1)));
		assertEpsilonEquals(1.1647754717, this.shape.getDistanceSquared(createSphere(3, 0, 0, .1)));
		assertEpsilonEquals(10.9021419308, this.shape.getDistanceSquared(createSphere(3, 0, 5, .1)));
		assertEpsilonEquals(3.4968410856, this.shape.getDistanceSquared(createSphere(3, 0, -5, .1)));
		assertEpsilonEquals(16.1853788749, this.shape.getDistanceSquared(createSphere(1, -4, 0, .1)));
		assertEpsilonEquals(38.5735017233, this.shape.getDistanceSquared(createSphere(1, -4, 5, .1)));
		assertEpsilonEquals(19.1714388244, this.shape.getDistanceSquared(createSphere(1, -4, -5, .1)));
	}

	@DisplayName("findsClosestPointToAlignedBox(PathIterator3afp, double,double,double,double,double,double, Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void findsClosestPointPathIteratorAlignedBox(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var result = new InnerComputationPoint3D();

		// Outside Box
		
		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				-2, 1, 0, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				-2, 1, 5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				-2, 1, -5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				1, 0, 0, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				1, 0, 5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				1, 0, -5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				1, 1, 0, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				1, 1, 5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				1, 1, -5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				3, 0, 0, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				3, 0, 5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				3, 0, -5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		
		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				1, -4, 0, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				1, -4, 5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(6.699003011013108, -2.529020888903435, 2.7233273740365576), result);
		
		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				1, -4, -5, 1, 1, 1, result);
		assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);

		// Inside Box
		
		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				-2, 1, 0, 3, .1, .1, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				-2, 1, 0, 3, .1, .1, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				-2, 1, 0, 5, .1, .1, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				-2, 1, 0, 7, .1, .1, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				-2, 1, 0, 9, .1, .1, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		Path3afp.findsClosestPointPathIteratorAlignedBox(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO),
				-2, 1, 0, 15, .1, .1, result);
		assertEpsilonEquals(createPoint(0, 0, 0), result);
	}

	@DisplayName("getClosestPointTo(AlignedBox3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getClosestPointToAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;

		// Outside Box
		
		result = this.shape.getClosestPointTo(createAlignedBox(-2, 1, 0, .1, .1, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createAlignedBox(-2, 1, 5, .1, .1, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createAlignedBox(-2, 1, -5, .1, .1, .1));
		assertEpsilonEquals(createPoint(0.8952380952380953, 0.44761904761904764, -4.476190476190476), result);

		result = this.shape.getClosestPointTo(createAlignedBox(1, 0, 0, .1, .1, .1));
		assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);

		result = this.shape.getClosestPointTo(createAlignedBox(1, 0, 5, .1, .1, .1));
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		result = this.shape.getClosestPointTo(createAlignedBox(1, 0, -5, .1, .1, .1));
		assertEpsilonEquals(createPoint(0.9904761904761905, 0.49523809523809526, -4.9523809523809526), result);

		result = this.shape.getClosestPointTo(createAlignedBox(1, 1, 0, .1, .1, .1));
		assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);

		result = this.shape.getClosestPointTo(createAlignedBox(1, 1, 5, .1, .1, .1));
		assertEpsilonEquals(createPoint(5.375, 1.3125, 2.8125), result);

		result = this.shape.getClosestPointTo(createAlignedBox(1, 1, -5, .1, .1, .1));
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = this.shape.getClosestPointTo(createAlignedBox(3, 0, 0, .1, .1, .1));
		assertEpsilonEquals(createPoint(2.75, 0.875, -0.75), result);

		result = this.shape.getClosestPointTo(createAlignedBox(3, 0, 5, .1, .1, .1));
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		result = this.shape.getClosestPointTo(createAlignedBox(3, 0, -5, .1, .1, .1));
		assertEpsilonEquals(createPoint(1.178117105233741,0.4741442911757473,-4.419682979722328), result);
		
		result = this.shape.getClosestPointTo(createAlignedBox(1, -4, 0, .1, .1, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createAlignedBox(1, -4, 5, .1, .1, .1));
		assertEpsilonEquals(createPoint(6.699003011013108, -2.529020888903435, 2.7233273740365576), result);
		
		result = this.shape.getClosestPointTo(createAlignedBox(1, -4, -5, .1, .1, .1));
		assertEpsilonEquals(createPoint(0.9142857142857143, 0.45714285714285713, -4.571428571428571), result);

		// Inside Box
		
		result = this.shape.getClosestPointTo(createAlignedBox(-2, 1, 0, 3, .1, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createAlignedBox(-2, 1, 0, 3, .1, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createAlignedBox(-2, 1, 0, 5, .1, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createAlignedBox(-2, 1, 0, 7, .1, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createAlignedBox(-2, 1, 0, 9, .1, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createAlignedBox(-2, 1, 0, 15, .1, .1));
		assertEpsilonEquals(createPoint(0, 0, 0), result);
	}

	@DisplayName("getClosestPointTo(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getClosestPointToSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Point3D result;
		
		result = this.shape.getClosestPointTo(createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSegment(-2, 1, 5, 10, 10, 10));
		assertEpsilonEquals(createPoint(0, 0, 0), result);

		result = this.shape.getClosestPointTo(createSegment(-2, 1, -5, 10, 10, 10));
		assertEpsilonEquals(createPoint(0.6328774470367391, 0.31643872351836955, -3.1643872351836952), result);

		result = this.shape.getClosestPointTo(createSegment(1, 0, 0, 10, 10, 10));
		assertEpsilonEquals(createPoint(0.0380952380952381, 0.01904761904761905, -0.190476190476190), result);

		result = this.shape.getClosestPointTo(createSegment(1, 0, 5, 10, 10, 10));
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		result = this.shape.getClosestPointTo(createSegment(1, 0, -5, 10, 10, 10));
		assertEpsilonEquals(createPoint(1.202170841513377, 0.47065261978031625, -4.34131435506932), result);

		result = this.shape.getClosestPointTo(createSegment(1, 1, 0, 10, 10, 10));
		assertEpsilonEquals(createPoint(0.05714285714285714, 0.02857142857142857, -0.2857142857142857), result);

		result = this.shape.getClosestPointTo(createSegment(1, 1, 5, 10, 10, 10));
		assertEpsilonEquals(createPoint(5.53286881, 1.2138319933, 3.022991748), result);

		result = this.shape.getClosestPointTo(createSegment(1, 1, -5, 10, 10, 10));
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = this.shape.getClosestPointTo(createSegment(3, 0, 0, 10, 10, 10));
		assertEpsilonEquals(createPoint(4.840444885643851, 1.4436409922956546, 1.4300824432249362), result);

		result = this.shape.getClosestPointTo(createSegment(3, 0, 5, 10, 10, 10));
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);

		result = this.shape.getClosestPointTo(createSegment(3, 0, -5, 10, 10, 10));
		assertEpsilonEquals(createPoint(4.154833414196007, 2.4774372270884744, -1.2548641941817131), result);
		
		result = this.shape.getClosestPointTo(createSegment(1, -4, 0, 10, 10, 10));
		assertEpsilonEquals(createPoint(5.489624176405976, 1.240859889746265, 2.965332235207968), result);
		
		result = this.shape.getClosestPointTo(createSegment(1, -4, 5, 10, 10, 10));
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
		
		result = this.shape.getClosestPointTo(createSegment(1, -4, -5, 10, 10, 10));
		assertEpsilonEquals(createPoint(4.6789346310202795, 1.5210313226361163, 0.9085389126696517), result);
	}

	@DisplayName("getDistance(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public void getDistanceSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		assertEpsilonEquals(2.0754980867, this.shape.getDistance(createSegment(-2, 1, 0, 10, 10, 10)));
		assertEpsilonEquals(5.4772255751, this.shape.getDistance(createSegment(-2, 1, 5, 10, 10, 10)));
		assertEpsilonEquals(2.1288602878, this.shape.getDistance(createSegment(-2, 1, -5, 10, 10, 10)));
		assertEpsilonEquals(0.9807674352, this.shape.getDistance(createSegment(1, 0, 0, 10, 10, 10)));
		assertEpsilonEquals(4.0867360173, this.shape.getDistance(createSegment(1, 0, 5, 10, 10, 10)));
		assertEpsilonEquals(0.1824217194, this.shape.getDistance(createSegment(1, 0, -5, 10, 10, 10)));
		assertEpsilonEquals(1.3835771443, this.shape.getDistance(createSegment(1, 1, 0, 10, 10, 10)));
		assertEpsilonEquals(4.328469971, this.shape.getDistance(createSegment(1, 1, 5, 10, 10, 10)));
		assertEpsilonEquals(.5, this.shape.getDistance(createSegment(1, 1, -5, 10, 10, 10)));
		assertEpsilonEquals(0.7480852002, this.shape.getDistance(createSegment(3, 0, 0, 10, 10, 10)));
		assertEpsilonEquals(2.9820600187, this.shape.getDistance(createSegment(3, 0, 5, 10, 10, 10)));
		assertEpsilonEquals(0.5490504509, this.shape.getDistance(createSegment(3, 0, -5, 10, 10, 10)));
		assertEpsilonEquals(1.3590257028, this.shape.getDistance(createSegment(1, -4, 0, 10, 10, 10)));
		assertEpsilonEquals(3.7957672532, this.shape.getDistance(createSegment(1, -4, 5, 10, 10, 10)));
		assertEpsilonEquals(0.1208788395, this.shape.getDistance(createSegment(1, -4, -5, 10, 10, 10)));
	}

	@DisplayName("getDistanceSquared(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceSquaredSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertEpsilonEquals(4.3076923077, this.shape.getDistanceSquared(createSegment(-2, 1, 0, 10, 10, 10)));
		assertEpsilonEquals(30., this.shape.getDistanceSquared(createSegment(-2, 1, 5, 10, 10, 10)));
		assertEpsilonEquals(4.532046125, this.shape.getDistanceSquared(createSegment(-2, 1, -5, 10, 10, 10)));
		assertEpsilonEquals(0.9619047619, this.shape.getDistanceSquared(createSegment(1, 0, 0, 10, 10, 10)));
		assertEpsilonEquals(16.701411275, this.shape.getDistanceSquared(createSegment(1, 0, 5, 10, 10, 10)));
		assertEpsilonEquals(0.0332776837, this.shape.getDistanceSquared(createSegment(1, 0, -5, 10, 10, 10)));
		assertEpsilonEquals(1.9142857143, this.shape.getDistanceSquared(createSegment(1, 1, 0, 10, 10, 10)));
		assertEpsilonEquals(18.7356522899, this.shape.getDistanceSquared(createSegment(1, 1, 5, 10, 10, 10)));
		assertEpsilonEquals(.25, this.shape.getDistanceSquared(createSegment(1, 1, -5, 10, 10, 10)));
		assertEpsilonEquals(0.5596314668, this.shape.getDistanceSquared(createSegment(3, 0, 0, 10, 10, 10)));
		assertEpsilonEquals(8.8926819549, this.shape.getDistanceSquared(createSegment(3, 0, 5, 10, 10, 10)));
		assertEpsilonEquals(0.3014563976, this.shape.getDistanceSquared(createSegment(3, 0, -5, 10, 10, 10)));
		assertEpsilonEquals(1.8469508609, this.shape.getDistanceSquared(createSegment(1, -4, 0, 10, 10, 10)));
		assertEpsilonEquals(14.4078490403, this.shape.getDistanceSquared(createSegment(1, -4, 5, 10, 10, 10)));
		assertEpsilonEquals(0.0146116938, this.shape.getDistanceSquared(createSegment(1, -4, -5, 10, 10, 10)));
	}

	@DisplayName("getClosestPointTo(MultiShape3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getClosestPointToMultiShape3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		MultiShape3afp ms;
		Path3afp p;
		Point3D result;

		ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		result = this.shape.getClosestPointTo(ms);
		assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);

		ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		p.lineTo(4, 6, 12);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		result = this.shape.getClosestPointTo(ms);
		assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);

		ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.quadTo(10, 10, 10, 4, 6, 12);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		result = this.shape.getClosestPointTo(ms);
		assertEpsilonEquals(createPoint(5.66793695624968, 1.1294144023439496, 3.2030826083329074), result);
	}

	@DisplayName("getDistance(MultiShape3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceMultiShape3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		MultiShape3afp ms;
		Path3afp p;

		ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(1.1413633541, this.shape.getDistance(ms));

		ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		p.lineTo(4, 6, 12);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(1.1413633541, this.shape.getDistance(ms));

		ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.quadTo(10, 10, 10, 4, 6, 12);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(1.8618918068, this.shape.getDistance(ms));
	}

	@DisplayName("getDistanceSquared(MultiShape3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceSquaredMultiShape3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		MultiShape3afp ms;
		Path3afp p;

		ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(1.3027103061, this.shape.getDistanceSquared(ms));

		ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		p.lineTo(4, 6, 12);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(1.3027103061, this.shape.getDistanceSquared(ms));

		ms = createMultiShape();
		p = createPath();
		p.moveTo(-2, -10, -4);
		p.quadTo(10, 10, 10, 4, 6, 12);
		ms.add(p);
		ms.add(createSegment(-2, 1, 0, 10, 10, 10));
		assertEpsilonEquals(3.4666411003, this.shape.getDistanceSquared(ms));
	}

	@DisplayName("findsClosestPointToPath")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void findsClosestPointToPath(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var result = new InnerComputationPoint3D();
		Path3afp p;

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		Path3afp.findsClosestPointToPath(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), p.getPathIterator(SPLINE_APPROXIMATION_RATIO), result);
		assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		p.lineTo(4, 6, 12);
		Path3afp.findsClosestPointToPath(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), p.getPathIterator(SPLINE_APPROXIMATION_RATIO), result);
		assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);
	
		p.closePath();
		Path3afp.findsClosestPointToPath(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), p.getPathIterator(SPLINE_APPROXIMATION_RATIO), result);
		assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.quadTo(10, 10, 10, 4, 6, 12);
		Path3afp.findsClosestPointToPath(this.shape.getPathIterator(SPLINE_APPROXIMATION_RATIO), p.getPathIterator(SPLINE_APPROXIMATION_RATIO), result);
		assertEpsilonEquals(createPoint(5.66793695624968, 1.1294144023439496, 3.2030826083329074), result);
	}

	@DisplayName("getClosestPointTo(Path3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getClosestPointToPath3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp p;
		Point3D result;

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		result = this.shape.getClosestPointTo(p);
		assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		p.lineTo(4, 6, 12);
		result = this.shape.getClosestPointTo(p);
		assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);

		p.closePath();
		result = this.shape.getClosestPointTo(p);
		assertEpsilonEquals(createPoint(5.62491123781028, 1.1563054763685752, 3.1457149837470397), result);

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.quadTo(10, 10, 10, 4, 6, 12);
		result = this.shape.getClosestPointTo(p);
		assertEpsilonEquals(createPoint(5.66793695624968, 1.1294144023439496, 3.2030826083329074), result);
	}

	@DisplayName("getDistance(Path3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistancePath3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp p;
		Point3D result;

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		assertEpsilonEquals(1.1413633541, this.shape.getDistance(p));

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		p.lineTo(4, 6, 12);
		assertEpsilonEquals(1.1413633541, this.shape.getDistance(p));

		p.closePath();
		assertEpsilonEquals(1.1413633541, this.shape.getDistance(p));

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.quadTo(10, 10, 10, 4, 6, 12);
		assertEpsilonEquals(1.8618918068, this.shape.getDistance(p));
	}

	@DisplayName("getDistanceSquared(Path3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getDistanceSquaredPath3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Path3afp p;
		Point3D result;

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		assertEpsilonEquals(1.3027103061, this.shape.getDistanceSquared(p));

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.lineTo(10, 10, 10);
		p.lineTo(4, 6, 12);
		assertEpsilonEquals(1.3027103061, this.shape.getDistanceSquared(p));

		p.closePath();
		assertEpsilonEquals(1.3027103061, this.shape.getDistanceSquared(p));

		p = createPath();
		p.moveTo(-2, -10, -4);
		p.quadTo(10, 10, 10, 4, 6, 12);
		assertEpsilonEquals(3.4666411003, this.shape.getDistanceSquared(p));
	}

	@DisplayName("getFarthestPointTo(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getFarthestPointTo(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		Point3D result;

		result = this.shape.getFarthestPointTo(createPoint(-2, 1, 0));
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = this.shape.getFarthestPointTo(createPoint(-2, 1, 5));
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = this.shape.getFarthestPointTo(createPoint(-2, 1, -5));
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = this.shape.getFarthestPointTo(createPoint(1, 0, 0));
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = this.shape.getFarthestPointTo(createPoint(1, 0, 5));
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = this.shape.getFarthestPointTo(createPoint(1, 0, -5));
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = this.shape.getFarthestPointTo(createPoint(1, 1, 0));
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = this.shape.getFarthestPointTo(createPoint(1, 1, 5));
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = this.shape.getFarthestPointTo(createPoint(1, 1, -5));
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = this.shape.getFarthestPointTo(createPoint(3, 0, 0));
		assertEpsilonEquals(createPoint(7, -5, 2), result);

		result = this.shape.getFarthestPointTo(createPoint(3, 0, 5));
		assertEpsilonEquals(createPoint(1, .5, -5), result);

		result = this.shape.getFarthestPointTo(createPoint(3, 0, -5));
		assertEpsilonEquals(createPoint(7, -5, 2), result);
		
		result = this.shape.getFarthestPointTo(createPoint(1, -4, 0));
		assertEpsilonEquals(createPoint(4, 3, -2), result);

		result = this.shape.getFarthestPointTo(createPoint(1, -4, 5));
		assertEpsilonEquals(createPoint(1, .5, -5), result);
		
		result = this.shape.getFarthestPointTo(createPoint(1, -4, -5));
		assertEpsilonEquals(createPoint(5.75, 1.078125, 3.3125), result);
	}

	@DisplayName("getDistance(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistancePoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		double result;

		result = this.shape.getDistance(createPoint(-2, 1, 0)); //(0, 0, 0)
		assertEpsilonEquals(2.236067977, result);

		result = this.shape.getDistance(createPoint(-2, 1, 5)); //(0, 0, 0)
		assertEpsilonEquals(5.477225575, result);

		result = this.shape.getDistance(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
		assertEpsilonEquals(2.993644061, result);

		result = this.shape.getDistance(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
		assertEpsilonEquals(.98076743, result);

		result = this.shape.getDistance(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(5.06442864, result);

		result = this.shape.getDistance(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
		assertEpsilonEquals(.4976133515, result);

		result = this.shape.getDistance(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
		assertEpsilonEquals(1.3835771443, result);

		result = this.shape.getDistance(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(4.9013709817, result);

		result = this.shape.getDistance(createPoint(1, 1, -5)); //(1, .5, -5)
		assertEpsilonEquals(.5, result);

		result = this.shape.getDistance(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
		assertEpsilonEquals(1.1792476415, result);

		result = this.shape.getDistance(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
		assertEpsilonEquals(3.401839174, result);

		result = this.shape.getDistance(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
		assertEpsilonEquals(1.9699842474, result);
		
		result = this.shape.getDistance(createPoint(1, -4, 0)); //(0, 0, 0)
		assertEpsilonEquals(4.1231056256, result);

		result = this.shape.getDistance(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
		assertEpsilonEquals(6.3107569364, result);
		
		result = this.shape.getDistance(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
		assertEpsilonEquals(4.4785201638, result);
	}

	@DisplayName("getDistanceSquared(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceSquaredPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		double result;

		result = this.shape.getDistanceSquared(createPoint(-2, 1, 0)); //(0, 0, 0)
		assertEpsilonEquals(2.236067977 * 2.236067977, result);

		result = this.shape.getDistanceSquared(createPoint(-2, 1, 5)); //(0, 0, 0)
		assertEpsilonEquals(5.477225575 * 5.477225575, result);

		result = this.shape.getDistanceSquared(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
		assertEpsilonEquals(2.993644061 * 2.993644061, result);

		result = this.shape.getDistanceSquared(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
		assertEpsilonEquals(.98076743 * .98076743, result);

		result = this.shape.getDistanceSquared(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(5.06442864 * 5.06442864, result);

		result = this.shape.getDistanceSquared(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
		assertEpsilonEquals(.4976133515 * .4976133515, result);

		result = this.shape.getDistanceSquared(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
		assertEpsilonEquals(1.3835771443 * 1.3835771443, result);

		result = this.shape.getDistanceSquared(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(4.9013709817 * 4.9013709817, result);

		result = this.shape.getDistanceSquared(createPoint(1, 1, -5)); //(1, .5, -5)
		assertEpsilonEquals(.5 * .5, result);

		result = this.shape.getDistanceSquared(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
		assertEpsilonEquals(1.1792476415 * 1.1792476415, result);

		result = this.shape.getDistanceSquared(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
		assertEpsilonEquals(3.401839174 * 3.401839174, result);

		result = this.shape.getDistanceSquared(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
		assertEpsilonEquals(1.9699842474 * 1.9699842474, result);
		
		result = this.shape.getDistanceSquared(createPoint(1, -4, 0)); //(0, 0, 0)
		assertEpsilonEquals(4.1231056256 * 4.1231056256, result);

		result = this.shape.getDistanceSquared(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
		assertEpsilonEquals(6.3107569364 * 6.3107569364, result);
		
		result = this.shape.getDistanceSquared(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
		assertEpsilonEquals(4.4785201638 * 4.4785201638, result);
	}

	@DisplayName("getDistanceL1(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceL1(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		double result;

		result = this.shape.getDistanceL1(createPoint(-2, 1, 0)); //(0, 0, 0)
		assertEpsilonEquals(2 + 1 + 0, result);

		result = this.shape.getDistanceL1(createPoint(-2, 1, 5)); //(0, 0, 0)
		assertEpsilonEquals(2 + 1 + 5, result);

		result = this.shape.getDistanceL1(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
		assertEpsilonEquals(2.8952380952 + 0.5523809524 + 0.5238095238, result);

		result = this.shape.getDistanceL1(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
		assertEpsilonEquals(0.9619047619 + 0.019047619 + 0.1904761905, result);

		result = this.shape.getDistanceL1(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(4.375 + 1.3125 + 2.1875, result);

		result = this.shape.getDistanceL1(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
		assertEpsilonEquals(0.0095238095 + 0.4952380952 + 0.0476190476, result);

		result = this.shape.getDistanceL1(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
		assertEpsilonEquals(0.9428571429 + 0.9714285714 + 0.2857142857, result);

		result = this.shape.getDistanceL1(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(4.375 + 0.3125 + 2.1875, result);

		result = this.shape.getDistanceL1(createPoint(1, 1, -5)); //(1, .5, -5)
		assertEpsilonEquals(0 + .5 + 0, result);

		result = this.shape.getDistanceL1(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
		assertEpsilonEquals(.25 + .875 + .75, result);

		result = this.shape.getDistanceL1(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
		assertEpsilonEquals(2.75 + 1.078125 + 1.6875, result);

		result = this.shape.getDistanceL1(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
		assertEpsilonEquals(1.8218828948 + .4741442912 + .5803170203, result);
		
		result = this.shape.getDistanceL1(createPoint(1, -4, 0)); //(0, 0, 0)
		assertEpsilonEquals(1 + 4 + 0, result);

		result = this.shape.getDistanceL1(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
		assertEpsilonEquals(5.699003011 + 1.4709791111 + 2.276672626, result);
		
		result = this.shape.getDistanceL1(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
		assertEpsilonEquals(0.0857142857 + 4.4571428571 + 0.4285714286, result);
	}

	@DisplayName("getDistanceLinf(Point3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void getDistanceLinf(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		double result;

		result = this.shape.getDistanceLinf(createPoint(-2, 1, 0)); //(0, 0, 0)
		assertEpsilonEquals(2, result);

		result = this.shape.getDistanceLinf(createPoint(-2, 1, 5)); //(0, 0, 0)
		assertEpsilonEquals(5, result);

		result = this.shape.getDistanceLinf(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
		assertEpsilonEquals(2.8952380952, result);

		result = this.shape.getDistanceLinf(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
		assertEpsilonEquals(0.9619047619, result);

		result = this.shape.getDistanceLinf(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(4.375, result);

		result = this.shape.getDistanceLinf(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
		assertEpsilonEquals(0.4952380952, result);

		result = this.shape.getDistanceLinf(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
		assertEpsilonEquals(0.9714285714, result);

		result = this.shape.getDistanceLinf(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(4.375, result);

		result = this.shape.getDistanceLinf(createPoint(1, 1, -5)); //(1, .5, -5)
		assertEpsilonEquals(.5, result);

		result = this.shape.getDistanceLinf(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
		assertEpsilonEquals(.875, result);

		result = this.shape.getDistanceLinf(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
		assertEpsilonEquals(2.75, result);

		result = this.shape.getDistanceLinf(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
		assertEpsilonEquals(1.8218828948, result);
		
		result = this.shape.getDistanceLinf(createPoint(1, -4, 0)); //(0, 0, 0)
		assertEpsilonEquals(4, result);

		result = this.shape.getDistanceLinf(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
		assertEpsilonEquals(5.699003011, result);
		
		result = this.shape.getDistanceLinf(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
		assertEpsilonEquals(4.4571428571, result);
	}

	@DisplayName("set(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void setIT(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.set((T) createPath());
		PathIterator3afp pi = this.shape.getPathIterator();
		assertNoElement(pi);
		Path3afp path = createPath();
		path.moveTo(123.456, 456.789, 0);
		path.lineTo(789.123, 159.753, 0);
		this.shape.set(path);
		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 123.456, 456.789, 0);
		assertElement(pi, PathElementType.LINE_TO, 789.123, 159.753, 0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getPathIterator(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);
		this.shape.closePath();
		pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);
	}

	@DisplayName("getPathIterator(Transform3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getPathIteratorTransform3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		PathIterator3afp pi;
		Transform3D transform = new Transform3D();

		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);
		
		transform.setIdentity();
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertNoElement(pi);

		transform.setTranslation(14, -5, 1.5);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 14, -5, 1.5);
		assertElement(pi, PathElementType.LINE_TO, 15, -4.5, -3.5);
		assertElement(pi, PathElementType.QUAD_TO, 17, -5, 3.5, 18, -2, -.5);
		assertElement(pi, PathElementType.CURVE_TO, 19, -6, 4.5, 20, 0, 6.5, 21, -10, 3.5);
		assertNoElement(pi);

		this.shape.closePath();
		
		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);

		transform.setIdentity();
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 0, 0, 0);
		assertElement(pi, PathElementType.LINE_TO, 1, .5, -5);
		assertElement(pi, PathElementType.QUAD_TO, 3, 0, 2, 4, 3, -2);
		assertElement(pi, PathElementType.CURVE_TO, 5, -1, 3, 6, 5, 5, 7, -5, 2);
		assertElement(pi, PathElementType.CLOSE, 0, 0, 0);
		assertNoElement(pi);

		transform.setTranslation(14, -5, 1.5);
		pi = this.shape.getPathIterator(transform);
		assertElement(pi, PathElementType.MOVE_TO, 14, -5, 1.5);
		assertElement(pi, PathElementType.LINE_TO, 15, -4.5, -3.5);
		assertElement(pi, PathElementType.QUAD_TO, 17, -5, 3.5, 18, -2, -.5);
		assertElement(pi, PathElementType.CURVE_TO, 19, -6, 4.5, 20, 0, 6.5, 21, -10, 3.5);
		assertElement(pi, PathElementType.CLOSE, 14, -5, 1.5);
		assertNoElement(pi);
	}

	@DisplayName("translate(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void translateDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		Path3afp p2 = createPath();
		p2.moveTo(dx, dy, dz);
		p2.lineTo(1 + dx, .5 + dy, -5 + dz);
		p2.quadTo(3 + dx, 0 + dy, 2 + dz, 4 + dx, 3 + dy, -2 + dz);
		p2.curveTo(5 + dx, -1 + dy, 3 + dz, 6 + dx, 5 + dy, 5 + dz, 7 + dx, -5 + dy, 2 + dz);
		
		this.shape.translate(dx, dy, dz);
		
		assertTrue(this.shape.equals(p2));		
	}

	@DisplayName("translate(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void translateVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		this.shape.translate(dx, dy, dz);
		
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, dx, dy, dz);
		assertElement(pi, PathElementType.LINE_TO, dx + 1, dy + .5, dz - 5);
		assertElement(pi, PathElementType.QUAD_TO, dx + 3, dy, dz + 2, dx + 4, dy + 3, dz - 2);
		assertElement(pi, PathElementType.CURVE_TO, dx + 5, dy - 1,  dz + 3, dx + 6, dy + 5, dz + 5, dx + 7, dy - 5, dz + 2);
		assertNoElement(pi);
	}

	@DisplayName("contains(AlignedBox3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void containsAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.contains(createAlignedBox(1.484375, 0.4296875, -3.421875, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(1.9375, 0.46875, -2.1875, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(2.359375, 0.6171875, -1.296875, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(2.75, 0.875, -0.75, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(3.109375, 1.2421875, -0.546875, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(3.4375, 1.71875, -0.6875, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(3.734375, 2.3046875, -1.171875, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(4.0, 3.0, -2.0, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(4.25, 2.15625, -0.796875, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(4.625, 1.546875, 0.734375, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(5.0, 1.3671875, 1.9453125, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(5.375, 1.3125, 2.8125, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(5.75, 1.078125, 3.3125, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(6.125, 0.359375, 3.421875, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(6.5, -1.1484375, 3.1171875, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(6.875, -3.75, 2.375, 0, 0, 0)));
		assertTrue(this.shape.contains(createAlignedBox(7.0, -5.0, 2.0, 0, 0, 0)));

		assertFalse(this.shape.contains(createAlignedBox(-5, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, 6, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, -10, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(11, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(4, 3, 0, 2, 1, 0)));

		this.shape.closePath();
		
		assertFalse(this.shape.contains(createAlignedBox(-5, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, 6, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, -10, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(11, 1, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(3, 0, 0, 2, 1, 0)));
		assertFalse(this.shape.contains(createAlignedBox(4, 3, 0, 2, 1, 0)));
	}

	@DisplayName("intersects(AlignedBox3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsAlignedBox3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createAlignedBox(1, -2, 0, 2, 1, 0)));
		assertTrue(this.shape.intersects(createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(7, 3, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(-4, -0.5, 0, 2, 1, 0)));
	}

	@DisplayName("intersects(AlignedBox3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsAlignedBox3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertTrue(this.shape.intersects(createAlignedBox(1, -2, 0, 2, 1, 0)));
		assertTrue(this.shape.intersects(createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(7, 3, 0, 2, 1, 0)));
		assertFalse(this.shape.intersects(createAlignedBox(-4, -0.5, 0, 2, 1, 0)));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public abstract void intersectsMultiShape3afp(CoordinateSystem3D cs);

		@DisplayName("intersects(Sphere3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsSphere3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSphere(-2, -2, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(2, -2, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(2.5, -1.5, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(10, 0, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(4, 0, 0, 0.5)));
		assertTrue(this.shape.intersects(createSphere(2.5, 1, 0, 0.5)));
	}

	@DisplayName("intersects(Sphere3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsSphere3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createSphere(-2, -2, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(2, -2, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(2.5, -1.5, 0, 2)));
		assertFalse(this.shape.intersects(createSphere(10, 0, 0, 2)));
		assertTrue(this.shape.intersects(createSphere(4, 0, 0, 0.5)));
		assertTrue(this.shape.intersects(createSphere(2.5, 1, 0, 0.5)));
	}

	@DisplayName("intersects(Segment3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsSegment3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createSegment(1, -1, 0, 2, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 0, 2, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(4, 0, 0, 2, -3, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 5, 3, 0)));
	}

	@DisplayName("intersects(Segment3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsSegment3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createSegment(1, -1, 0, 2, -3, 0)));
		assertFalse(this.shape.intersects(createSegment(1, -6, 0, 2, -3, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 2, -3, 0)));
		assertTrue(this.shape.intersects(createSegment(4, 0, 0, 5, 3, 0)));
	}

	@DisplayName("intersects(Path3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsPath3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0)));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0)));
	}

	@DisplayName("intersects(Path3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsPath3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0)));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0)));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0)));
	}

	@DisplayName("intersects(PathIterator3afp)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsPathIterator3afp(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0).getPathIterator()));
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0).getPathIterator()));
	}

	@DisplayName("intersects(PathIterator3afp) close path")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void intersectsPathIterator3afp_close(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.shape.closePath();
		assertFalse(this.shape.intersects(createPolyline(1, -1, 0, 4, -3, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 5, -3, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(1, -1, 0, 4, 1, 0).getPathIterator()));
		assertTrue(this.shape.intersects(createPolyline(5, 2, 0, 4, 1, 0).getPathIterator()));
	}

	@DisplayName("intersects(Shape3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void intersectsShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.intersects((Shape3D) createSegment(4, 0, 0, 5, 3, 0)));
		assertTrue(this.shape.intersects((Shape3D)createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
	}

	@DisplayName("p += Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_addVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble() * 20;
		double dy = getRandom().nextDouble() * 20;
		double dz = getRandom().nextDouble() * 20;
		
		this.shape.operator_add(createVector(dx, dy, dz));
		
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0 + dx, 0 + dy, 0 + dz);
		assertElement(pi, PathElementType.LINE_TO, 1 + dx, .5 + dy, -5 + dz);
		assertElement(pi, PathElementType.QUAD_TO, 3 + dx, 0 + dy, 2 + dz, 4 + dx, 3 + dy, -2 + dz);
		assertElement(pi, PathElementType.CURVE_TO, 5 + dx, -1 + dy, 3 + dz, 6 + dx, 5 + dy, 5 + dz, 7 + dx, -5 + dy, 2 + dz);
		assertNoElement(pi);
	}

	@DisplayName("p + Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_plusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		T shape = this.shape.operator_plus(createVector(dx, dy, dz));
		
		PathIterator3afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0 + dx, 0 + dy, 0 + dz);
		assertElement(pi, PathElementType.LINE_TO, 1 + dx, .5 + dy, -5 + dz);
		assertElement(pi, PathElementType.QUAD_TO, 3 + dx, 0 + dy, 2 + dz, 4 + dx, 3 + dy, -2 + dz);
		assertElement(pi, PathElementType.CURVE_TO, 5 + dx, -1 + dy, 3 + dz, 6 + dx, 5 + dy, 5 + dz, 7 + dx, -5 + dy, 2 + dz);
		assertNoElement(pi);
	}

	@DisplayName("p -= Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_removeVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		this.shape.operator_remove(createVector(dx, dy, dz));
		
		PathIterator3afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0 - dx, 0 - dy, 0 - dz);
		assertElement(pi, PathElementType.LINE_TO, 1 - dx, .5 - dy, -5 - dz);
		assertElement(pi, PathElementType.QUAD_TO, 3 - dx, 0 - dy, 2 - dz, 4 - dx, 3 - dy, -2 - dz);
		assertElement(pi, PathElementType.CURVE_TO, 5 - dx, -1 - dy, 3 - dz, 6 - dx, 5 - dy, 5 - dz, 7 - dx, -5 - dy, 2 - dz);
		assertNoElement(pi);
	}

	@DisplayName("p - Vector3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_minusVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double dx = getRandom().nextDouble()*20;
		double dy = getRandom().nextDouble()*20;
		double dz = getRandom().nextDouble()*20;
		
		T shape = this.shape.operator_minus(createVector(dx, dy, dz));
		
		PathIterator3afp pi = shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 0 - dx, 0 - dy, 0 - dz);
		assertElement(pi, PathElementType.LINE_TO, 1 - dx, .5 - dy, -5 - dz);
		assertElement(pi, PathElementType.QUAD_TO, 3 - dx, 0 - dy, 2 - dz, 4 - dx, 3 - dy, -2 - dz);
		assertElement(pi, PathElementType.CURVE_TO, 5 - dx, -1 - dy, 3 - dz, 6 - dx, 5 - dy, 5 - dz, 7 - dx, -5 - dy, 2 - dz);
		assertNoElement(pi);
	}

	@DisplayName("p && Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		
		assertTrue(this.shape.operator_and(createPoint(0, 0, 0)));
		assertTrue(this.shape.operator_and(createPoint(1, .5, -5)));
		// Control point
		assertFalse(this.shape.operator_and(createPoint(3, 0, 2)));
		assertTrue(this.shape.operator_and(createPoint(4, 3, -2)));
		// Control point
		assertFalse(this.shape.operator_and(createPoint(5, -1, 3)));
		// Control point
		assertFalse(this.shape.operator_and(createPoint(6, 5, 5)));
		assertTrue(this.shape.operator_and(createPoint(7, -5, 2)));

		assertTrue(this.shape.operator_and(createPoint(1.484375, 0.4296875, -3.421875)));
		assertTrue(this.shape.operator_and(createPoint(1.9375, 0.46875, -2.1875)));
		assertTrue(this.shape.operator_and(createPoint(2.359375, 0.6171875, -1.296875)));
		assertTrue(this.shape.operator_and(createPoint(2.75, 0.875, -0.75)));
		assertTrue(this.shape.operator_and(createPoint(3.109375, 1.2421875, -0.546875)));
		assertTrue(this.shape.operator_and(createPoint(3.4375, 1.71875, -0.6875)));
		assertTrue(this.shape.operator_and(createPoint(3.734375, 2.3046875, -1.171875)));
		assertTrue(this.shape.operator_and(createPoint(4.0, 3.0, -2.0)));
		assertTrue(this.shape.operator_and(createPoint(4.25, 2.15625, -0.796875)));
		assertTrue(this.shape.operator_and(createPoint(4.625, 1.546875, 0.734375)));
		assertTrue(this.shape.operator_and(createPoint(5.0, 1.3671875, 1.9453125)));
		assertTrue(this.shape.operator_and(createPoint(5.375, 1.3125, 2.8125)));
		assertTrue(this.shape.operator_and(createPoint(5.75, 1.078125, 3.3125)));
		assertTrue(this.shape.operator_and(createPoint(6.125, 0.359375, 3.421875)));
		assertTrue(this.shape.operator_and(createPoint(6.5, -1.1484375, 3.1171875)));
		assertTrue(this.shape.operator_and(createPoint(6.875, -3.75, 2.375)));
		assertTrue(this.shape.operator_and(createPoint(7.0, -5.0, 2.0)));

		assertFalse(this.shape.operator_and(createPoint(-5, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(3, 6, 0)));
		assertFalse(this.shape.operator_and(createPoint(3, -10, 0)));
		assertFalse(this.shape.operator_and(createPoint(11, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(4, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(4, 3, 0)));
		
		this.shape.closePath();
		
		assertTrue(this.shape.operator_and(createPoint(0, 0, 0)));
		assertTrue(this.shape.operator_and(createPoint(1, .5, -5)));
		// Control point
		assertFalse(this.shape.operator_and(createPoint(3, 0, 2)));
		assertTrue(this.shape.operator_and(createPoint(4, 3, -2)));
		// Control point
		assertFalse(this.shape.operator_and(createPoint(5, -1, 3)));
		// Control point
		assertFalse(this.shape.operator_and(createPoint(6, 5, 5)));
		assertTrue(this.shape.operator_and(createPoint(7, -5, 2)));

		assertTrue(this.shape.operator_and(createPoint(1.484375, 0.4296875, -3.421875)));
		assertTrue(this.shape.operator_and(createPoint(1.9375, 0.46875, -2.1875)));
		assertTrue(this.shape.operator_and(createPoint(2.359375, 0.6171875, -1.296875)));
		assertTrue(this.shape.operator_and(createPoint(2.75, 0.875, -0.75)));
		assertTrue(this.shape.operator_and(createPoint(3.109375, 1.2421875, -0.546875)));
		assertTrue(this.shape.operator_and(createPoint(3.4375, 1.71875, -0.6875)));
		assertTrue(this.shape.operator_and(createPoint(3.734375, 2.3046875, -1.171875)));
		assertTrue(this.shape.operator_and(createPoint(4.0, 3.0, -2.0)));
		assertTrue(this.shape.operator_and(createPoint(4.25, 2.15625, -0.796875)));
		assertTrue(this.shape.operator_and(createPoint(4.625, 1.546875, 0.734375)));
		assertTrue(this.shape.operator_and(createPoint(5.0, 1.3671875, 1.9453125)));
		assertTrue(this.shape.operator_and(createPoint(5.375, 1.3125, 2.8125)));
		assertTrue(this.shape.operator_and(createPoint(5.75, 1.078125, 3.3125)));
		assertTrue(this.shape.operator_and(createPoint(6.125, 0.359375, 3.421875)));
		assertTrue(this.shape.operator_and(createPoint(6.5, -1.1484375, 3.1171875)));
		assertTrue(this.shape.operator_and(createPoint(6.875, -3.75, 2.375)));
		assertTrue(this.shape.operator_and(createPoint(7.0, -5.0, 2.0)));

		assertFalse(this.shape.operator_and(createPoint(-5, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(3, 6, 0)));
		assertFalse(this.shape.operator_and(createPoint(3, -10, 0)));
		assertFalse(this.shape.operator_and(createPoint(11, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(4, 1, 0)));
		assertFalse(this.shape.operator_and(createPoint(4, 3, 0)));
	}

	@DisplayName("p && Shape3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.operator_and(createSegment(4, 0, 0, 5, 3, 0)));
		assertTrue(this.shape.operator_and(createAlignedBox(1.5, 1.5, 0, 2, 1, 0)));
	}

	@DisplayName("p .. Point3D")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@Override
	public final void operator_upToPoint3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		double result;

		result = this.shape.operator_upTo(createPoint(-2, 1, 0)); //(0, 0, 0)
		assertEpsilonEquals(2.236067977, result);

		result = this.shape.operator_upTo(createPoint(-2, 1, 5)); //(0, 0, 0)
		assertEpsilonEquals(5.477225575, result);

		result = this.shape.operator_upTo(createPoint(-2, 1, -5)); //(0.8952380952380953, 0.44761904761904764, -4.476190476190476)
		assertEpsilonEquals(2.993644061, result);

		result = this.shape.operator_upTo(createPoint(1, 0, 0)); //(0.0380952380952381, 0.01904761904761905, -0.190476190476190)
		assertEpsilonEquals(.98076743, result);

		result = this.shape.operator_upTo(createPoint(1, 0, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(5.06442864, result);

		result = this.shape.operator_upTo(createPoint(1, 0, -5)); //(0.9904761904761905, 0.49523809523809526, -4.9523809523809526)
		assertEpsilonEquals(.4976133515, result);

		result = this.shape.operator_upTo(createPoint(1, 1, 0)); //(0.05714285714285714, 0.02857142857142857, -0.2857142857142857)
		assertEpsilonEquals(1.3835771443, result);

		result = this.shape.operator_upTo(createPoint(1, 1, 5)); //(5.375, 1.3125, 2.8125)
		assertEpsilonEquals(4.9013709817, result);

		result = this.shape.operator_upTo(createPoint(1, 1, -5)); //(1, .5, -5)
		assertEpsilonEquals(.5, result);

		result = this.shape.operator_upTo(createPoint(3, 0, 0)); //(2.75, 0.875, -0.75)
		assertEpsilonEquals(1.1792476415, result);

		result = this.shape.operator_upTo(createPoint(3, 0, 5)); //(5.75, 1.078125, 3.3125)
		assertEpsilonEquals(3.401839174, result);

		result = this.shape.operator_upTo(createPoint(3, 0, -5)); //(1.178117105233741, 0.4741442911757473, -4.419682979722328)
		assertEpsilonEquals(1.9699842474, result);
		
		result = this.shape.operator_upTo(createPoint(1, -4, 0)); //(0, 0, 0)
		assertEpsilonEquals(4.1231056256, result);

		result = this.shape.operator_upTo(createPoint(1, -4, 5)); //(6.699003011013108, -2.529020888903435, 2.7233273740365576)
		assertEpsilonEquals(6.3107569364, result);
		
		result = this.shape.operator_upTo(createPoint(1, -4, -5)); //(0.9142857142857143, 0.45714285714285713, -4.571428571428571)
		assertEpsilonEquals(4.4785201638, result);
	}

	@DisplayName("isCurved")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void isCurved(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertTrue(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isCurved());
		this.shape.closePath();
		assertFalse(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.closePath();
		assertFalse(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isCurved());
		this.shape.curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
		assertTrue(this.shape.isCurved());

		this.shape.clear();
		assertFalse(this.shape.isCurved());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isCurved());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isCurved());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isCurved());
		this.shape.quadTo(7, 8, 0, 9, 10, 0);
		assertTrue(this.shape.isCurved());
	}
	
	@DisplayName("isMultiParts")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void isMultiParts(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isMultiParts());

		this.shape.clear();
		assertFalse(this.shape.isMultiParts());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.closePath();
		assertFalse(this.shape.isMultiParts());

		this.shape.clear();
		assertFalse(this.shape.isMultiParts());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isMultiParts());
		this.shape.curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
		assertFalse(this.shape.isMultiParts());

		this.shape.moveTo(1, 2, 0);
		assertTrue(this.shape.isMultiParts());
		this.shape.moveTo(3, 4, 0);
		assertTrue(this.shape.isMultiParts());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isMultiParts());
		this.shape.lineTo(5, 6, 0);
		assertTrue(this.shape.isMultiParts());
		this.shape.quadTo(7, 8, 0, 9, 10, 0);
		assertTrue(this.shape.isMultiParts());
	}
	
	@DisplayName("isPolygon")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void isPolygon(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.lineTo(5, 6, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.quadTo(7, 8, 0, 9, 10, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertTrue(this.shape.isPolygon());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolygon());
		this.shape.closePath();
		assertFalse(this.shape.isPolygon());
	}
	
	@DisplayName("isPolyline")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void isPolyline(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolyline());
		
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(5, 6, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolyline());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolygon());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.lineTo(5, 6, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.curveTo(7, 8, 0, 9, 10, 0, 11, 12, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());

		this.shape.clear();
		assertFalse(this.shape.isPolyline());

		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(3, 4, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.lineTo(5, 6, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.quadTo(7, 8, 0, 9, 10, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.closePath();
		assertFalse(this.shape.isPolyline());
		
		this.shape.clear();
		assertFalse(this.shape.isPolyline());
		this.shape.moveTo(1, 2, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(3, 4, 0);
		assertTrue(this.shape.isPolyline());
		this.shape.moveTo(5, 6, 0);
		assertFalse(this.shape.isPolyline());
		this.shape.lineTo(7, 8, 0);
		assertFalse(this.shape.isPolyline());
	}
	
	@DisplayName("getCurrentX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getCurrentX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(7, this.shape.getCurrentX());
		this.shape.lineTo(154, 485, 0);
		assertEpsilonEquals(154, this.shape.getCurrentX());
	}
	
	@DisplayName("getCurrentY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getCurrentY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
	    assertEpsilonEquals(-5, this.shape.getCurrentY());
	    this.shape.lineTo(154, 485, 0);
	    assertEpsilonEquals(485, this.shape.getCurrentY());
	}

	@DisplayName("getCurrentZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getCurrentZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(2, this.shape.getCurrentZ());
		this.shape.lineTo(154, 485, 10);
		assertEpsilonEquals(10, this.shape.getCurrentZ());
	}

	@DisplayName("getCurrentPoint")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void getCurrentPoint(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertFpPointEquals(7, -5, 2, this.shape.getCurrentPoint());
		this.shape.lineTo(154, 485, 0);
		assertFpPointEquals(154, 485, 0, this.shape.getCurrentPoint());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("toBoundingBoxWithCtrlPoints")
	public final void toBoundingBoxWithCtrlPoints(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var box = this.shape.toBoundingBoxWithCtrlPoints();
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(5, box.getMaxY());
		assertEpsilonEquals(5, box.getMaxZ());
	}

	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	@DisplayName("toBoundingBoxWithCtrlPoints(B)")
	public final void toBoundingBoxWithCtrlPointsB(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		var box = this.shape.getGeomFactory().newBox();
		this.shape.toBoundingBoxWithCtrlPoints(box);
		assertEpsilonEquals(0, box.getMinX());
		assertEpsilonEquals(-5, box.getMinY());
		assertEpsilonEquals(-5, box.getMinZ());
		assertEpsilonEquals(7, box.getMaxX());
		assertEpsilonEquals(5, box.getMaxY());
		assertEpsilonEquals(5, box.getMaxZ());
	}

	@DisplayName("toCollection")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
	public final void toCollection(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Collection<? extends Point3D> collection = this.shape.toCollection();
		assertEquals(7, collection.size());
		Iterator<? extends Point3D> iterator = collection.iterator();
		assertEpsilonEquals(createPoint(0, 0, 0), iterator.next());
		assertEpsilonEquals(createPoint(1, .5, -5), iterator.next());
		assertEpsilonEquals(createPoint(3, 0, 2), iterator.next());
		assertEpsilonEquals(createPoint(4, 3, -2), iterator.next());
		assertEpsilonEquals(createPoint(5, -1, 3), iterator.next());
		assertEpsilonEquals(createPoint(6, 5, 5), iterator.next());
		assertEpsilonEquals(createPoint(7, -5, 2), iterator.next());
		assertFalse(iterator.hasNext());
	}

}
