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

package org.arakhne.afc.io.shape;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d3.Point3D;
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
public class AbstractIoShapeTest extends AbstractTestCase {

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

	protected void assertEpsilonEquals(Point2D<?, ?> expected, Point2D<?, ?> actual) {
		setDecimalPrecision(1);
		assertEpsilonEquals(expected.getX(), actual.getX());
		assertEpsilonEquals(expected.getY(), actual.getY());
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
	}

	protected void assertEpsilonEquals(Point3D<?, ?> expected, Point3D<?, ?> actual) {
		setDecimalPrecision(1);
		assertEpsilonEquals(expected.getX(), actual.getX());
		assertEpsilonEquals(expected.getY(), actual.getY());
		assertEpsilonEquals(expected.getZ(), actual.getZ());
		setDecimalPrecision(DEFAULT_DECIMAL_COUNT);
	}

	protected Rectangle2d makeBounds(Point2d min, Point2d max) {
		return new Rectangle2d(min, max);
	}

}
