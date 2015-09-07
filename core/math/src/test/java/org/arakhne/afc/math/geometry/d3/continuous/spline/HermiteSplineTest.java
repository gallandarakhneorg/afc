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
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
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
public class HermiteSplineTest extends AbstractSplineTestCase<HermiteSpline> {

	private static final List<Point3f> CTRL_POINTS = new ArrayList<>();
	private static final List<Vector3f> TANGENTS = new ArrayList<>();
	private static final List<Point3f> POINTS = new ArrayList<>();

	static {
		CTRL_POINTS.add(new Point3f(12, 34, 56));
		CTRL_POINTS.add(new Point3f(154, 10, 41));
		CTRL_POINTS.add(new Point3f(458, 21, 1));
		CTRL_POINTS.add(new Point3f(500, 0, 0));
		CTRL_POINTS.add(new Point3f(750, -50, 1));
		CTRL_POINTS.add(new Point3f(450, 47, -114));
		CTRL_POINTS.add(new Point3f(-14, -14, -110));
		//
	    TANGENTS.add(new Vector3f(53.25000, -9.00000, -5.62500));
	    TANGENTS.add(new Vector3f(334.50000, -9.75000, -41.25000));
	    TANGENTS.add(new Vector3f(259.50000, -7.50000, -30.75000));
	    TANGENTS.add(new Vector3f(219.00000, -53.25000, 0.00000));
	    TANGENTS.add(new Vector3f(-37.50000, 35.25000, -85.50000));
	    TANGENTS.add(new Vector3f(-573.00000, 27.00000, -83.25000));
	    TANGENTS.add(new Vector3f(-174.00000, -22.87500, 1.50000));
		// See the Octave script for the generated points
		double[] coordinates = new double[] {
				 12, 34, 56,
				 15.562, 35.992, 59.74700000000001,
				 23.376, 35.536, 60.29600000000001,
				 34.73400000000001, 33.184, 58.409,
				 48.92800000000001, 29.488, 54.848,
				 65.25, 25, 50.375,
				 82.992, 20.27199999999999, 45.75199999999999,
				 101.446, 15.856, 41.741,
				 119.904, 12.304, 39.10400000000001,
				 137.658, 10.168, 38.60299999999999,
				 153.9999999999999, 10, 41,
				 154, 10, 41,
				 170.864, 10.929, 43.19199999999999,
				 190.672, 11.752, 42.056,
				 213.448, 12.523, 38.324,
				 239.216, 13.296, 32.72799999999999,
				 268, 14.125, 26,
				 299.824, 15.064, 18.872,
				 334.7119999999999, 16.167, 12.076,
				 372.6879999999999, 17.488, 6.344000000000005,
				 413.7760000000001, 19.081, 2.407999999999993,
				 457.9999999999999, 21, 1.000000000000009,
				 458, 21, 1,
				 491.774, 22.113, 1.053,
				 504.9920000000001, 21.504, 1.024,
				 502.898, 19.551, 0.9309999999999999,
				 490.736, 16.632, 0.7919999999999998,
				 473.75, 13.125, 0.625,
				 457.184, 9.407999999999998, 0.4479999999999998,
				 446.2819999999999, 5.859, 0.279,
				 446.288, 2.856000000000003, 0.1360000000000001,
				 462.446, 0.776999999999996, 0.03699999999999981,
				 499.9999999999999, 4.662936703425657e-15, 2.220446049250313e-16,
				 500, 0, 0,
				 540.75, -0.9500000000000001, 0.019,
				 566, -3.600000000000001, 0.07200000000000001,
				 580.25, -7.650000000000002, 0.153,
				 588, -12.8, 0.2560000000000001,
				 593.75, -18.75, 0.375,
				 602, -25.2, 0.5040000000000001,
				 617.2499999999999, -31.84999999999999, 0.6369999999999999,
				 644, -38.4, 0.7679999999999999,
				 686.75, -44.55, 0.8910000000000001,
				 749.9999999999999, -49.99999999999998, 0.9999999999999997,
				 750, -50, 1,
				 798.3000000000001, -51.757, -1.113000000000001,
				 800.4, -47.816, -7.184000000000003,
				 767.0999999999999, -39.35899999999999, -16.511,
				 709.1999999999998, -27.56799999999999, -28.392,
				 637.5, -13.625, -42.125,
				 562.8, 1.288000000000014, -57.00800000000002,
				 495.9, 15.989, -72.339,
				 447.6, 29.29599999999999, -87.416,
				 428.6999999999999, 40.02700000000002, -101.537,
				 450, 46.99999999999997, -114,
				 450, 47, -114,
				 473.5839999999999, 49.22499999999999, -122.132,
				 459.792, 47.12, -124.656,
				 416.8079999999999, 41.61499999999999, -122.964,
				 352.816, 33.63999999999999, -118.448,
				 276, 24.125, -112.5,
				 194.5439999999999, 13.99999999999999, -106.512,
				 116.632, 4.195000000000004, -101.876,
				 50.44800000000006, -4.359999999999993, -99.98400000000001,
				 4.175999999999912, -10.73500000000001, -102.228,
				 -13.9999999999999, -13.99999999999998, -110,
		};
		for (int i = 0; i < coordinates.length; i += 3) {
			POINTS.add(new Point3f(coordinates[i], coordinates[i + 1], coordinates[i + 2]));
		}
	}

	@Override
	protected HermiteSpline createSpline() {
		return new HermiteSpline(CTRL_POINTS, TANGENTS);
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
	public void setControlPointsPoint3DListVector3DList() {
		Pair<List<Point3f>, List<Point3f>> points = randomPoints();
		Pair<List<Vector3f>, List<Vector3f>> tangents = randomVectors(points.getA().size());
		this.spline.setControlPoints(points.getB(), tangents.getB());
		assertCollectionEquals(points.getA(), this.spline.getControlPoints());
	}
	
	@Test
	public void getControlPointTangents() {
		Pair<List<Point3f>, List<Point3f>> points = randomPoints();
		Pair<List<Vector3f>, List<Vector3f>> tangents = randomVectors(points.getA().size());
		this.spline.setControlPoints(points.getB(), tangents.getB());
		assertCollectionEquals(tangents.getA(), this.spline.getControlPointTangents());
	}

	@Test
	public void computeTangentsForCardinalSpline() {
		List<Vector3f> actual = HermiteSpline.computeTangentsForCardinalSpline(CTRL_POINTS, .25);
		assertCollectionEquals(TANGENTS, actual);
	}

}

