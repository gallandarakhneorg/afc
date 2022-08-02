/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.gis;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;

import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
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
public class AbstractGisTest extends AbstractTestCase {

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
		if (expected == null) {
			assertNull(actual);
			return;
		}
		if (actual == null) {
			fail("Unexpected null bounds"); //$NON-NLS-1$
			return;
		}
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
		assertEpsilonEquals(expected.getX(), actual.getX());
		assertEpsilonEquals(expected.getY(), actual.getY());
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
	}

	protected Rectangle2d makeBounds(Point2d p) {
		return new Rectangle2d(
				((float) p.getX()-GeoLocationUtil.GIS_POINT_DEMI_SIZE),
				((float) p.getY()-GeoLocationUtil.GIS_POINT_DEMI_SIZE),
				(float) GeoLocationUtil.GIS_POINT_SIZE,
				(float) GeoLocationUtil.GIS_POINT_SIZE);
	}

	protected Rectangle2d makeBounds(Point2d min, Point2d max) {
		return new Rectangle2d(min, max);
	}

	protected void assertEpsilonEquals(MapLayer expected, MapLayer actual) {
		if (!Objects.equals(expected.getClass(), actual.getClass())) {
			failCompare("Not same type", expected.getClass().toString(), //$NON-NLS-1$
					actual.getClass().toString());
		}
		if (!Objects.equals(actual.getGeoId(), expected.getGeoId())) {
			failCompare("Not same GeoId", expected.getGeoId().toString(), //$NON-NLS-1$
					actual.getGeoId().toString());
		}
		if (!Objects.equals(expected.getClass(), actual.getClass())) {
			failCompare("Not same hashCode", expected.hashKey().toString(), //$NON-NLS-1$
					actual.getClass().toString());
		}
		if (!Objects.equals(expected, actual)) {
			failCompare("Not same objects", expected.toString(), //$NON-NLS-1$
					actual.toString());
		}
	}

	protected void assertEpsilonEquals(MapElement expected, MapElement actual) {
		if (!Objects.equals(expected.getClass(), actual.getClass())) {
			failCompare("Not same type", expected.getClass().toString(), //$NON-NLS-1$
					actual.getClass().toString());
		}
		if (!Objects.equals(actual.getGeoId(), expected.getGeoId())) {
			failCompare("Not same GeoId", expected.getGeoId().toString(), //$NON-NLS-1$
					actual.getGeoId().toString());
		}
		if (!Objects.equals(expected.getClass(), actual.getClass())) {
			failCompare("Not same hashCode", expected.hashKey().toString(), //$NON-NLS-1$
					actual.getClass().toString());
		}
		if (!Objects.equals(expected, actual)) {
			failCompare("Not same objects", expected.toString(), //$NON-NLS-1$
					actual.toString());
		}
	}

}
