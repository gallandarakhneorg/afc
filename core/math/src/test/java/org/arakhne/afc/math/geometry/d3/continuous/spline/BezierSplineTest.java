/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d3.continuous.spline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.arakhne.afc.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RunWith(Suite.class)
@SuiteClasses({
	BezierSplineTest.QuadraticBezier.class,
	BezierSplineTest.CubicBezier.class,
})
@SuppressWarnings("all")
public class BezierSplineTest {

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class QuadraticBezier extends AbstractSplineTestCase<BezierSpline> {

		private static final List<Point3f> CTRL_POINTS = new ArrayList<>();
		private static final List<Point3f> POINTS = new ArrayList<>();

		static {
			CTRL_POINTS.add(new Point3f(12, 34, 56));
			CTRL_POINTS.add(new Point3f(154, 10, 41));
			CTRL_POINTS.add(new Point3f(458, 21, 1));
			CTRL_POINTS.add(new Point3f(500, 0, 0));
			CTRL_POINTS.add(new Point3f(750, -50, 1));
			// See the Octave script for the generated points
			double[] coordinates = new double[] {
					12, 34, 56,
					42.020, 29.550, 52.750,
					75.280, 25.800, 49.000,
					111.780, 22.750, 44.750,
					151.520, 20.400, 40.000,
					194.500, 18.750, 34.750,
					240.720, 17.800, 29.000,
					290.180, 17.550, 22.750,
					342.880, 18.000, 16.000,
					398.8200, 19.1500, 8.7500,
					458.0000, 21.0000, 1.0000,
					458, 21, 1,
					468.48000, 16.51000, 0.82000,
					483.12000, 11.44000, 0.68000,
					501.92000, 5.79000, 0.58000,
					524.88000, -0.44000, 0.52000,
					552.00000, -7.25000, 0.50000,
					583.28000, -14.64000, 0.52000,
					618.72000, -22.61000, 0.58000,
					658.32000, -31.16000, 0.68000,
					702.08000, -40.29000, 0.82000,
					750.00000, -50.00000, 1.00000,
			};
			for (int i = 0; i < coordinates.length; i += 3) {
				POINTS.add(new Point3f(coordinates[i], coordinates[i + 1], coordinates[i + 2]));
			}
		}

		@Override
		protected BezierSpline createSpline() {
			return new BezierSpline(false, CTRL_POINTS);
		}
		
		@Override
		protected List<Point3f> getExpectedControlPoints() {
			return CTRL_POINTS;
		}
		
		@Override
		protected List<Point3f> getExpectedPoints() {
			return POINTS;
		}
		
		@Test
		public void isCubic() {
			assertFalse(this.spline.isCubic());
		}

		@Test
		public void isQuadratic() {
			assertTrue(this.spline.isQuadratic());
		}

		@Test
		public void setCubicBoolean() {
			assertFalse(this.spline.isCubic());
			assertTrue(this.spline.isQuadratic());
			this.spline.setCubic(true);
			assertTrue(this.spline.isCubic());
			assertFalse(this.spline.isQuadratic());
			this.spline.setCubic(false);
			assertFalse(this.spline.isCubic());
			assertTrue(this.spline.isQuadratic());
		}

		@Test
		public void setQuadraticBoolean() {
			assertFalse(this.spline.isCubic());
			assertTrue(this.spline.isQuadratic());
			this.spline.setQuadratic(false);
			assertTrue(this.spline.isCubic());
			assertFalse(this.spline.isQuadratic());
			this.spline.setQuadratic(true);
			assertFalse(this.spline.isCubic());
			assertTrue(this.spline.isQuadratic());
		}

		@Test
		public void setControlPointsPoint3DList() {
			Pair<List<Point3f>, List<Point3f>> points = randomPoints();
			this.spline.setControlPoints(points.getB());
			assertCollectionEquals(points.getA(), this.spline.getControlPoints());
		}

		@Test
		public void setControlPointsPoint3DArray() {
			Pair<List<Point3f>, Point3f[]> points = randomPointsArray();
			this.spline.setControlPoints(points.getB());
			assertCollectionEquals(points.getA(), this.spline.getControlPoints());
		}

	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class CubicBezier extends AbstractSplineTestCase<BezierSpline> {

		private static final List<Point3f> CTRL_POINTS = new ArrayList<>();
		private static final List<Point3f> POINTS = new ArrayList<>();

		static {
			CTRL_POINTS.add(new Point3f(12, 34, 56));
			CTRL_POINTS.add(new Point3f(154, 10, 41));
			CTRL_POINTS.add(new Point3f(458, 21, 1));
			CTRL_POINTS.add(new Point3f(500, 0, 0));
			CTRL_POINTS.add(new Point3f(750, -50, 1));
			CTRL_POINTS.add(new Point3f(450, 47, -114));
			CTRL_POINTS.add(new Point3f(-14, -14, -110));
			// See the Octave script for the generated points
			double[] coordinates = new double[] {
					12, 34, 56,
					59.036, 27.783, 50.814,
					113.248, 23.264, 44.512,
					172.092, 20.041, 37.478,
					233.024, 17.712, 30.096,
					293.500, 15.875, 22.750,
					350.976, 14.128, 15.824,
					402.9080, 12.0690, 9.7020,
					446.7520, 9.2960, 4.7680,
					479.9640, 5.4070, 1.4060,
					5.0000e+02, 6.9944e-15, 3.3307e-16,
					500, 0, 0,
					558.8860, -10.8950, -2.9450,
					587.088, -14.800, -11.440,
					586.922, -13.545, -24.075,
					560.7040, -8.9600, -39.4400,
					510.7500, -2.8750, -56.1250,
					439.3760, 2.8800, -72.7200,
					348.8980, 6.4750, -87.8150,
					241.6320, 6.0800,  -100.0000,
					119.89400, -0.13500, -107.86500,
					-14.000, -14.000, -110.000,
			};
			for (int i = 0; i < coordinates.length; i += 3) {
				POINTS.add(new Point3f(coordinates[i], coordinates[i + 1], coordinates[i + 2]));
			}
		}

		@Override
		protected BezierSpline createSpline() {
			return new BezierSpline(true, CTRL_POINTS);
		}
		
		@Override
		protected List<Point3f> getExpectedControlPoints() {
			return CTRL_POINTS;
		}
		
		@Override
		protected List<Point3f> getExpectedPoints() {
			return POINTS;
		}

		@Test
		public void isCubic() {
			assertTrue(this.spline.isCubic());
		}

		@Test
		public void isQuadratic() {
			assertFalse(this.spline.isQuadratic());
		}

		@Test
		public void setCubicBoolean() {
			assertTrue(this.spline.isCubic());
			assertFalse(this.spline.isQuadratic());
			this.spline.setCubic(false);
			assertFalse(this.spline.isCubic());
			assertTrue(this.spline.isQuadratic());
			this.spline.setCubic(true);
			assertTrue(this.spline.isCubic());
			assertFalse(this.spline.isQuadratic());
		}

		@Test
		public void setQuadraticBoolean() {
			assertTrue(this.spline.isCubic());
			assertFalse(this.spline.isQuadratic());
			this.spline.setQuadratic(true);
			assertFalse(this.spline.isCubic());
			assertTrue(this.spline.isQuadratic());
			this.spline.setQuadratic(false);
			assertTrue(this.spline.isCubic());
			assertFalse(this.spline.isQuadratic());
		}

		@Test
		public void setControlPointsPoint3DList() {
			Pair<List<Point3f>, List<Point3f>> points = randomPoints();
			this.spline.setControlPoints(points.getB());
			assertCollectionEquals(points.getA(), this.spline.getControlPoints());
		}

		@Test
		public void setControlPointsPoint3DArray() {
			Pair<List<Point3f>, Point3f[]> points = randomPointsArray();
			this.spline.setControlPoints(points.getB());
			assertCollectionEquals(points.getA(), this.spline.getControlPoints());
		}

	}

}