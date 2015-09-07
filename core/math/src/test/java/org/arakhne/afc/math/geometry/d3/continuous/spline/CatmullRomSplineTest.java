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
@SuppressWarnings("all")
public class CatmullRomSplineTest extends AbstractSplineTestCase<CatmullRomSpline> {

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
			   12.00000, 34.00000, 56.00000,
			   19.72000, 32.41450, 55.22000,
			   28.72000, 30.17600, 54.36000,
			   39.06000, 27.46150, 53.39000,
			   50.80000, 24.44800, 52.28000,
			   64.00000, 21.31250, 51.00000,
			   78.72000, 18.23200, 49.52000,
			   95.02000, 15.38350, 47.81000,
			   112.96000, 12.94400, 45.84000,
			   132.60000, 11.09050, 43.58000,
			   154.00000, 10.00000, 41.00000,
			   154.00000, 10.00000, 41.00000,
			   179.01800, 9.82650, 37.83700,
			   208.62400, 10.47200, 33.97600,
			   241.54600, 11.73550, 29.60900,
			   276.51200, 13.41600, 24.92800,
			   312.25000, 15.31250, 20.12500,
			   347.48800, 17.22400, 15.39200,
			   380.95400, 18.94950, 10.92100,
			   411.37600, 20.28800, 6.90400,
			   437.48200, 21.03850, 3.53300,
			   458.00000, 21.00000, 1.00000,
			   458.00000, 21.00000, 1.00000,
			   471.87500, 20.32650, -0.68850,
			   479.84000, 19.31200, -1.72800,
			   483.30500, 17.96550, -2.22950,
			   483.68000, 16.29600, -2.30400,
			   482.37500, 14.31250, -2.06250,
			   480.80000, 12.02400, -1.61600,
			   480.36500, 9.43950, -1.07550,
			   482.48000, 6.56800, -0.55200,
			   488.55500, 3.41850, -0.15650,
			   500.00000, 0.00000, -0.00000,
			   500.00000, 0.00000, 0.00000,
			   519.05100, -4.48700, 0.54100,
			   545.48800, -10.49600, 1.92800,
			   577.03700, -17.49900, 3.80700,
			   611.42400, -24.96800, 5.82400,
			   646.37500, -32.37500, 7.62500,
			   679.61600, -39.19200, 8.85600,
			   708.87300, -44.89100, 9.16300,
			   731.87200, -48.94400, 8.19200,
			   746.33900, -50.82300, 5.58900,
			   750.00000, -50.00000, 1.00000,
			   750.00000, -50.00000, 1.00000,
			   743.01300, -45.54250, -6.33750,
			   727.82400, -37.48000, -16.48000,
			   705.59100, -26.72750, -28.72250,
			   677.47200, -14.20000, -42.36000,
			   644.62500, -0.81250, -56.68750,
			   608.20800, 12.52000, -71.00000,
			   569.37900, 24.88250, -84.59250,
			   529.29600, 35.36000, -96.76000,
			   489.11700, 43.03750, -106.79750,
			   450.00000, 47.00000, -114.00000,
			   450.00000, 47.00000, -114.00000,
			   408.15400, 47.02450, -118.40150,
			   360.27200, 43.93600, -120.75200,
			   308.23800, 38.39150, -121.42050,
			   253.93600, 31.04800, -120.77600,
			   199.25000, 22.56250, -119.18750,
			   146.06400, 13.59200, -117.02400,
			   96.26200, 4.79350, -114.65450,
			   51.72800, -3.17600, -112.44800,
			   14.34600, -9.65950, -110.77350,
			   -14.00000, -14.00000, -110.00000,
		};
		for (int i = 0; i < coordinates.length; i += 3) {
			POINTS.add(new Point3f(coordinates[i], coordinates[i + 1], coordinates[i + 2]));
		}
	}

	@Override
	protected CatmullRomSpline createSpline() {
		return new CatmullRomSpline(CTRL_POINTS);
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
	
	@Test
	public void getTheta() {
		assertEpsilonEquals(.5, this.spline.getTheta());
	}

	@Test
	public void setTheta() {
		assertEpsilonEquals(.5, this.spline.getTheta());
		this.spline.setTheta(0.);
		assertEpsilonEquals(0., this.spline.getTheta());
		this.spline.setTheta(0.75);
		assertEpsilonEquals(0.75, this.spline.getTheta());
	}

}

