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

package org.arakhne.afc.math.geometry.d1.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.d1.afp.RectangularShape1afp;
import org.arakhne.afc.math.geometry.d1.afp.Shape1afp;
import org.arakhne.afc.math.geometry.d1.d.Rectangle1d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Test abstract rectangular shape.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings("all")
public abstract class AbstractRectangularShape1DTestCase<
		SH extends RectangularShape1afp<?, Rectangle1d, ?, ?, ? super SG, Rectangle1d>,
		SG extends Segment1D<?, ?>>
		extends AbstractShape1DTestCase<SH, SG> {

	@Override
    protected final SH createShape() {
		return createShape(122.3, 25.75, 125.45, 426.789);
    }

    protected abstract SH createShape(double x, double y, double width, double height);

	@Test
	@DisplayName("getMinX")
	public void getMinX() {
		assertEpsilonEquals(122.3, getSH().getMinX());
	}

	@Test
	@DisplayName("setMinX(below min)")
	public void setMinX_belowmin() {
		getSH().setMinX(100.);
		assertEpsilonEquals(100., getSH().getMinX());
		assertEpsilonEquals(122.3 + 125.45, getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setMinX(between min and max)")
	public void setMinX_middle() {
		getSH().setMinX(150.);
		assertEpsilonEquals(150., getSH().getMinX());
		assertEpsilonEquals(122.3 + 125.45, getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setMinX(greater max)")
	public void setMinX_greatermax() {
		getSH().setMinX(1500.);
		assertEpsilonEquals(1500., getSH().getMinX());
		assertEpsilonEquals(1500., getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("getMaxX")
	public void getMaxX() {
		assertEpsilonEquals(122.3 + 125.45, getSH().getMaxX());
	}

	@Test
	@DisplayName("setMaxX(below min)")
	public void setMaxX_belowmin() {
		getSH().setMaxX(100.);
		assertEpsilonEquals(100., getSH().getMinX());
		assertEpsilonEquals(100., getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setMaxX(between min and max)")
	public void setMaxX_middle() {
		getSH().setMaxX(150.);
		assertEpsilonEquals(122.3, getSH().getMinX());
		assertEpsilonEquals(150., getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setMaxX(greater max)")
	public void setMaxX_greatermax() {
		getSH().setMaxX(1500.);
		assertEpsilonEquals(122.3, getSH().getMinX());
		assertEpsilonEquals(1500., getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("getMinY")
	public void getMinY() {
		assertEpsilonEquals(25.75, getSH().getMinY());
	}

	@Test
	@DisplayName("setMinY(below min)")
	public void setMinY_belowmin() {
		getSH().setMinY(5.);
		assertEpsilonEquals(122.3, getSH().getMinX());
		assertEpsilonEquals(122.3 + 125.45, getSH().getMaxX());
		assertEpsilonEquals(5., getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setMinX(between min and max)")
	public void setMinY_middle() {
		getSH().setMinX(150.);
		assertEpsilonEquals(150., getSH().getMinX());
		assertEpsilonEquals(122.3 + 125.45, getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setMinX(greater max)")
	public void setMinY_greatermax() {
		getSH().setMinX(1000.);
		assertEpsilonEquals(1000., getSH().getMinX());
		assertEpsilonEquals(1000., getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("getMaxY")
	public void getMaxY() {
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setMaxY(below min)")
	public void setMaxY_belowmin() {
		getSH().setMaxY(5.);
		assertEpsilonEquals(122.3, getSH().getMinX());
		assertEpsilonEquals(122.3 + 125.45, getSH().getMaxX());
		assertEpsilonEquals(5., getSH().getMinY());
		assertEpsilonEquals(5., getSH().getMaxY());
	}

	@Test
	@DisplayName("setMaxX(between min and max)")
	public void setMaxY_middle() {
		getSH().setMaxX(150.);
		assertEpsilonEquals(122.3, getSH().getMinX());
		assertEpsilonEquals(150., getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setMaxX(greater max)")
	public void setMaxY_greatermax() {
		getSH().setMaxX(1000.);
		assertEpsilonEquals(122.3, getSH().getMinX());
		assertEpsilonEquals(1000., getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("toBoundingBox")
	public void toBoundingBox() {
		var box = getSH().toBoundingBox();
		assertEpsilonEquals(122.3, box.getMinX());
		assertEpsilonEquals(122.3 + 125.45, box.getMaxX());
		assertEpsilonEquals(25.75, box.getMinY());
		assertEpsilonEquals(25.75 + 426.789, box.getMaxY());
	}

	@Test
	@DisplayName("toBoundingBox(box)")
	public void toBoundingBox_box() {
		var box = new Rectangle1d();
		getSH().toBoundingBox(box);
		assertEpsilonEquals(122.3, box.getMinX());
		assertEpsilonEquals(122.3 + 125.45, box.getMaxX());
		assertEpsilonEquals(25.75, box.getMinY());
		assertEpsilonEquals(25.75 + 426.789, box.getMaxY());
	}

	@Test
	@DisplayName("clear")
	public void clear() {
		getSH().clear();
		assertEpsilonEquals(0., getSH().getMinX());
		assertEpsilonEquals(0., getSH().getMaxX());
		assertEpsilonEquals(0., getSH().getMinY());
		assertEpsilonEquals(0., getSH().getMaxY());
	}

	@Test
	@DisplayName("set(double, double, double, double)")
	public void set_doubledoubledoubledouble() {
		getSH().set(123.258, 4563.365, 951.357, 4576.2145);
		assertEpsilonEquals(123.258, getSH().getMinX());
		assertEpsilonEquals(123.258 + 951.357, getSH().getMaxX());
		assertEpsilonEquals(4563.365, getSH().getMinY());
		assertEpsilonEquals(4563.365 + 4576.2145, getSH().getMaxY());
	}

	@Test
	@DisplayName("set(Shape1D)")
	public void set_Shape1D() {
		getSH().set(new Rectangle1d(getSG(), 123.258, 4563.365, 951.357, 4576.2145));
		assertEpsilonEquals(123.258, getSH().getMinX());
		assertEpsilonEquals(123.258 + 951.357, getSH().getMaxX());
		assertEpsilonEquals(4563.365, getSH().getMinY());
		assertEpsilonEquals(4563.365 + 4576.2145, getSH().getMaxY());
	}

	@Test
	@DisplayName("set(Point2D, Point2D)")
	public void set_Point2DPoint2D() {
		getSH().set(new Point2d(123.258, 4563.365), new Point2d(123.258 + 951.357, 4563.365 + 4576.2145));
		assertEpsilonEquals(123.258, getSH().getMinX());
		assertEpsilonEquals(123.258 + 951.357, getSH().getMaxX());
		assertEpsilonEquals(4563.365, getSH().getMinY());
		assertEpsilonEquals(4563.365 + 4576.2145, getSH().getMaxY());
	}

	@Test
	@DisplayName("setWidth")
	public void setWidth() {
		getSH().setWidth(123456.789);
		assertEpsilonEquals(122.3, getSH().getMinX());
		assertEpsilonEquals(122.3 + 123456.789, getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setHeight")
	public void setHeight() {
		getSH().setHeight(123456.789);
		assertEpsilonEquals(122.3, getSH().getMinX());
		assertEpsilonEquals(122.3 + 125.45, getSH().getMaxX());
		assertEpsilonEquals(25.75, getSH().getMinY());
		assertEpsilonEquals(25.75 + 123456.789, getSH().getMaxY());
	}

	@Test
	@DisplayName("setFromCorners(minx, miny, maxx, maxy)")
	public void setFromCorners_min_min() {
		getSH().setFromCorners(123.2, 456.6, 2548.2, 3053.4);
		assertEpsilonEquals(123.2, getSH().getMinX());
		assertEpsilonEquals(2548.2, getSH().getMaxX());
		assertEpsilonEquals(456.6, getSH().getMinY());
		assertEpsilonEquals(3053.4, getSH().getMaxY());
	}

	@Test
	@DisplayName("setFromCorners(minx, maxy, maxx, miny)")
	public void setFromCorners_min_max() {
		getSH().setFromCorners(123.2, 3053.4, 2548.2, 456.6);
		assertEpsilonEquals(123.2, getSH().getMinX());
		assertEpsilonEquals(2548.2, getSH().getMaxX());
		assertEpsilonEquals(456.6, getSH().getMinY());
		assertEpsilonEquals(3053.4, getSH().getMaxY());
	}

	@Test
	@DisplayName("setFromCorners(maxx, maxy, minx, miny)")
	public void setFromCorners_max_max() {
		getSH().setFromCorners(2548.2, 3053.4, 123.2, 456.6);
		assertEpsilonEquals(123.2, getSH().getMinX());
		assertEpsilonEquals(2548.2, getSH().getMaxX());
		assertEpsilonEquals(456.6, getSH().getMinY());
		assertEpsilonEquals(3053.4, getSH().getMaxY());
	}

	@Test
	@DisplayName("setFromCorners(maxx, miny, minx, maxy)")
	public void setFromCorners_max_min() {
		getSH().setFromCorners(2548.2, 456.6, 123.2, 3053.4);
		assertEpsilonEquals(123.2, getSH().getMinX());
		assertEpsilonEquals(2548.2, getSH().getMaxX());
		assertEpsilonEquals(456.6, getSH().getMinY());
		assertEpsilonEquals(3053.4, getSH().getMaxY());
	}

	@Test
	@DisplayName("setFromCenter")
	public void setFromCenter() {
		getSH().setFromCenter(123.2, 456.6, 2548.2, 3053.4);
		var demiWidth = 2548.2 - 123.2;
		var demiHeight = 3053.4 - 456.6;
		assertEpsilonEquals(123.2 - demiWidth, getSH().getMinX());
		assertEpsilonEquals(123.2 + demiWidth, getSH().getMaxX());
		assertEpsilonEquals(456.6 - demiHeight, getSH().getMinY());
		assertEpsilonEquals(456.6 + demiHeight, getSH().getMaxY());
	}

	@Test
	@DisplayName("getCenter")
	public void getCenter() {
		var c = getSH().getCenter();
		assertEpsilonEquals(122.3 + 125.45 / 2., c.getX());
		assertEpsilonEquals(25.75 + 426.789 / 2., c.getY());
	}

	@Test
	@DisplayName("getCenterX")
	public void getCenterX() {
		assertEpsilonEquals(122.3 + 125.45 / 2., getSH().getCenterX());
	}

	@Test
	@DisplayName("getCenterY")
	public void getCenterY() {
		assertEpsilonEquals(25.75 + 426.789 / 2., getSH().getCenterY());
	}

	@Test
	@DisplayName("getWidth")
	public void getWidth() {
		assertEpsilonEquals(125.45, getSH().getWidth());
	}

	@Test
	@DisplayName("getHeight")
	public void getHeight() {
		assertEpsilonEquals(426.789, getSH().getHeight());
	}

	@Test
	@DisplayName("isEmpty")
	public void isEmpty() {
		assertFalse(getSH().isEmpty());
		getSH().clear();
		assertTrue(getSH().isEmpty());
	}

	@Test
	@DisplayName("translate")
	public void translate() {
		getSH().translate(45.5, -85.45);
		assertEpsilonEquals(122.3 + 45.5, getSH().getMinX());
		assertEpsilonEquals(122.3 + 125.45 + 45.5, getSH().getMaxX());
		assertEpsilonEquals(25.75 - 85.45, getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789 - 85.45, getSH().getMaxY());
	}

	@Test
	@DisplayName("inflate")
	public void inflate() {
		getSH().inflate(45.5, -85.45, -5., 452.);
		assertEpsilonEquals(122.3 - 45.5, getSH().getMinX());
		assertEpsilonEquals(122.3 + 125.45 + (-5.), getSH().getMaxX());
		assertEpsilonEquals(25.75 - (-85.45), getSH().getMinY());
		assertEpsilonEquals(25.75 + 426.789 + 452., getSH().getMaxY());
	}

}
