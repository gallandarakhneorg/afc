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

package org.arakhne.afc.gis.road;

import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.testtools.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public abstract class AbstractGisTest extends AbstractTestCase {

	protected Point2d randomPoint2D() {
		return new Point2d(getRandom().nextDouble() * 100, getRandom().nextDouble() * 100);
	}

	protected Point3d randomPoint3D() {
		return new Point3d(getRandom().nextDouble() * 100, getRandom().nextDouble() * 100, getRandom().nextDouble() * 100);
	}

	protected Point2d[] randomPoints2D() {
		Point2d[] tab = new Point2d[getRandom().nextInt(50) + 20];
		for (int i = 0; i < tab.length; ++i) {
			tab[i] = new Point2d(getRandom().nextDouble() * 100, getRandom().nextDouble() * 100);
		}
		return tab;
	}

	protected Point3d[] randomPoints3D() {
		Point3d[] tab = new Point3d[getRandom().nextInt(50) + 20];
		for (int i = 0; i < tab.length; ++i) {
			tab[i] = new Point3d(getRandom().nextDouble() * 100, getRandom().nextDouble() * 100, getRandom().nextDouble() * 100);
		}
		return tab;
	}

	protected void assertEpsilonEquals(Shape2d<?> expected, Shape2d<?> actual) {
		Rectangle2d expectedb = expected.toBoundingBox();
		Rectangle2d actualb = actual.toBoundingBox();
		setDecimalPrecision(1);
		assertEpsilonEquals(expectedb.getMinX(), actualb.getMinX());
		assertEpsilonEquals(expectedb.getMinY(), actualb.getMinY());
		assertEpsilonEquals(expectedb.getMaxX(), actualb.getMaxX());
		assertEpsilonEquals(expectedb.getMaxY(), actualb.getMaxY());
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
	}

	protected void assertEpsilonEquals(Point2D<?, ?> expected, Point2D<?, ?> actual) {
		setDecimalPrecision(1);
		final double distance = expected.getDistance(actual);
		assertEpsilonZero("Not same points: expected=" + expected //$NON-NLS-1$
				+ "; actual=" + actual //$NON-NLS-1$
				+ "; distance=" + distance, distance); //$NON-NLS-1$
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
	}

	protected Rectangle2d makeBounds(Point2d p) {
		return new Rectangle2d(
				((float)p.getX()-GeoLocationUtil.GIS_POINT_DEMI_SIZE),
				((float)p.getY()-GeoLocationUtil.GIS_POINT_DEMI_SIZE),
				(float)GeoLocationUtil.GIS_POINT_SIZE,
				(float)GeoLocationUtil.GIS_POINT_SIZE);
	}

	protected Rectangle2d makeBounds(Point2d min, Point2d max) {
		return new Rectangle2d(min, max);
	}

	protected void assertEpsilonEquals(MapLayer actual, MapLayer expected) {
		fail("not implemented"); //$NON-NLS-1$
	}

	protected void assertEpsilonEquals(MapElement actual, MapElement expected) {
		fail("not implemented"); //$NON-NLS-1$
	}

	protected void assertColinear(Vector2D<?, ?> v1, Vector2D<?, ?> v2) {
		Vector2d v1a = new Vector2d();
		Vector2d v2a = new Vector2d();
		Vector2d v2b = new Vector2d();
		v1a.normalize(v1);
		v2a.normalize(v2);
		v2b.negate(v2a);
		assertTrue(v1a.epsilonEquals(v2a, EPSILON) || v1.epsilonEquals(v2b, EPSILON));
	}

	public static int epsilonCompare(Point2d position, Point2D<?, ?> o) {
		if (position.epsilonEquals(position, 0)) {
			return 0;
		}
		if (position.getX() < o.getX()) {
			return -1;
		}
		if (position.getX() > o.getX()) {
			return 1;
		}
		if (position.getY() < o.getY()) {
			return -1;
		}
		return 1;
	}

}
