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
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.arakhne.afc.util.Pair;
import org.junit.Before;
import org.junit.ComparisonFailure;
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
public abstract class AbstractSplineTestCase<T extends AbstractSpline> extends AbstractMathTestCase {

	protected T spline;

	@Before
	public void setUp() {
		this.spline = createSpline();
	}

	protected abstract T createSpline();

	protected abstract List<Point3f> getExpectedControlPoints();

	protected abstract List<Point3f> getExpectedPoints();

	protected Pair<List<Point3f>, List<Point3f>> randomPoints() {
		return randomPoints(this.random.nextInt(50) + 50);
	}
	
	protected Pair<List<Point3f>, List<Point3f>> randomPoints(int size) {
		List<Point3f> expected = new ArrayList<>();
		List<Point3f> actual = new ArrayList<>();
		for (int i = 0; i < size; ++ i) {
			Point3f p = randomPoint3f();
			expected.add(p);
			actual.add(p.clone());
		}
		return new Pair<>(expected, actual);
	}

	protected Pair<List<Vector3f>, List<Vector3f>> randomVectors() {
		return randomVectors(this.random.nextInt(50) + 50);
	}

	protected Pair<List<Vector3f>, List<Vector3f>> randomVectors(int size) {
		List<Vector3f> expected = new ArrayList<>();
		List<Vector3f> actual = new ArrayList<>();
		for (int i = 0; i < size; ++ i) {
			Vector3f p = randomVector3f();
			expected.add(p);
			actual.add(p.clone());
		}
		return new Pair<>(expected, actual);
	}

	protected Pair<List<Point3f>, Point3f[]> randomPointsArray() {
		int n = this.random.nextInt(50) + 50;
		List<Point3f> expected = new ArrayList<>();
		Point3f[] actual = new Point3f[n];
		for (int i = 0; i < n; ++ i) {
			Point3f p = randomPoint3f();
			expected.add(p);
			actual[i] = p.clone();
		}
		return new Pair<>(expected, actual);
	}

	@Test
	public void getControlPointCount() {
		assertEquals(getExpectedControlPoints().size(), this.spline.getControlPointCount());
	}

	@Test
	public void getControlPoints() {
		assertCollectionEquals(getExpectedControlPoints(), this.spline.getControlPoints());
	}

	@Test
	public void testClone() {
		T c = (T) this.spline.clone();
		assertNotSame(this.spline, c);
		assertEquals(this.spline, c);
	}

	@Test
	public void setDiscretizationFactorDouble() {
		assertEpsilonEquals(0.1, this.spline.getDiscretizationFactor());
		this.spline.setDiscretizationFactor(0.01);
		assertEpsilonEquals(0.01, this.spline.getDiscretizationFactor());
	}

	@Test
	public void getDiscretizationFactor() {
		assertEpsilonEquals(0.1, this.spline.getDiscretizationFactor());
	}

	protected String toString(Iterable<? extends Tuple3D<?>> tuples) {
		StringBuilder b = new StringBuilder();
		for (Tuple3D t : tuples) {
			b.append(t.getX());
			b.append(", ");
			b.append(t.getZ());
			b.append(", ");
			b.append(t.getZ());
			b.append(";\n");
		}
		return b.toString();
	}
	
	@Test
	public void iterator() {
		StringBuilder ex = new StringBuilder();
		for (Point3f expected : getExpectedPoints()) {
			ex.append(expected.toString());
			ex.append("\n");
		}

		Iterator<Point3f> iterator = this.spline.iterator();
		int i = 0;
		for (Point3f expected : getExpectedPoints()) {
			assertTrue(iterator.hasNext());
			if (!isEpsilonEquals(expected, iterator.next())) {
				throw new ComparisonFailure("Error on point #" + i,
						toString(getExpectedPoints()), toString(this.spline));
			}
			++i;
		}
		
		if (iterator.hasNext()) {
			StringBuilder ac = new StringBuilder(ex);
			while (iterator.hasNext()) {
				ac.append(iterator.next().toString());
				ac.append("\n");
			}
			throw new ComparisonFailure("Too much in the spline", ex.toString(), ac.toString());
		}
	}

}
