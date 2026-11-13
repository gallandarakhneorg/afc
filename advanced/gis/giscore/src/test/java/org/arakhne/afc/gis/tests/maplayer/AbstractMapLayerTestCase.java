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

package org.arakhne.afc.gis.tests.maplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Abstract unit tests for Maplayers.
 *
 * @param <L>
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public abstract class AbstractMapLayerTestCase<L extends MapLayer> extends AbstractGisTestCase {

	private static final int SET_TEST_COUNT = 1000;

	private L layer;

	private GISLayerContainerStub container;

	public AbstractMapLayerTestCase() {
		this.layer = null;
		this.container = null;
	}

	/**
	 * @return the layer
	 */
	protected final L getLayer() {
		assertNotNull(this.layer);
		return this.layer;
	}

	/**
	 * @return the container
	 */
	protected final GISLayerContainerStub getContainer() {
		assertNotNull(this.container);
		return this.container;
	}

	/**
	 * @return the expected children's bounds
	 */
	protected abstract Rectangle2d getExpectedChildBounds();

	public void setUpTest(Rectangle2d bounds) throws Exception {
		this.layer = createLayer(bounds);
		this.container = new GISLayerContainerStub();
		this.container.addMapLayer(this.layer);
	}

	/**
	 * @param bounds are the bounds of the world.
	 * @return the layer to test.
	 */
	protected abstract L createLayer(Rectangle2d bounds);

	@AfterEach
	public final void tearDown() {
		tearDownTest(this.layer);
		this.layer = null;
	}

	/**
	 * @param thelayer
	 */
	protected void tearDownTest(L thelayer) {
		//
	}

	private static Rectangle2d newRectangle2d(double x1, double y1, double x2, double y2) {
		Rectangle2d r = new Rectangle2d();
		r.setFromCorners(x1, y1, x2, y2);
		return r;
	}

	@DisplayName("intersects")
	@Nested
	public class Intersects {

		private L thelayer;
		private Rectangle2d bounds;
		private double x0;
		private double y0;
		private double x1;
		private double y1;
		private double xc;
		private double yc;
		private double w;
		private double h;

		@BeforeEach
		public void setUp() {
			thelayer = getLayer();
			bounds = getExpectedChildBounds();
			assertNotNull(bounds);
			x0 = bounds.getMinX();
			y0 = bounds.getMinY();
			x1 = bounds.getMaxX();
			y1 = bounds.getMaxY();
			xc = bounds.getCenterX();
			yc = bounds.getCenterY();
			w = bounds.getWidth();
			h = bounds.getHeight();
		}

		@DisplayName("#1")
		@Test
		public void testIntersects_assertion1() {
			assertTrue(thelayer.intersects(newRectangle2d(x0,y0,x0+w,y0+h)));
		}

		@DisplayName("#2")
		@Test
		public void testIntersects_assertion2() {
			assertTrue(thelayer.intersects(newRectangle2d(x0-10,y0-10,x0+10,y0+10)));
		}

		@DisplayName("#3")
		@Test
		public void testIntersects_assertion3() {
			assertTrue(thelayer.intersects(newRectangle2d(xc,yc,xc+w,yc+h)));
		}

		@DisplayName("#4")
		@Test
		public void testIntersects_assertion4() {
			assertTrue(thelayer.intersects(newRectangle2d(xc,yc,xc+1,yc+1)));
		}

		@DisplayName("#5")
		@Test
		public void testIntersects_assertion5() {
			assertTrue(thelayer.intersects(newRectangle2d(x0-10,y0-10,x0-10+w*2,y0-10+h*2)));
		}

		@DisplayName("#6")
		@Test
		public void testIntersects_assertion6() {
			assertFalse(thelayer.intersects(newRectangle2d(x0-10,y0,x0-Math.ulp(x0),y0+1)));
		}

		@DisplayName("#7")
		@Test
		public void testIntersects_assertion7() {
			assertFalse(thelayer.intersects(newRectangle2d(x1+10,y0,x1+Math.ulp(x1),y0+1)));
		}

		@DisplayName("#8")
		@Test
		public void testIntersects_assertion8() {
			assertFalse(thelayer.intersects(newRectangle2d(x0,y0-10,x0+1,y0-Math.ulp(y0))));
		}

		@DisplayName("#9")
		@Test
		public void testIntersects_assertion9() {
			assertFalse(thelayer.intersects(newRectangle2d(x0,y1+10,x0+1,y1+Math.ulp(y1))));
		}
	}

	@DisplayName("contains")
	@Nested
	public class Contains {

		private L thelayer;
		private Rectangle2d bounds;

		@BeforeEach
		public void setUp() {
			thelayer = getLayer();
			bounds = getExpectedChildBounds();
		}

		@DisplayName("#1")
		@Test
		public void testContains_assertion1() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMinX(), bounds.getCenterY()), 0));
		}

		@DisplayName("#2")
		@Test
		public void testContains_assertion2() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMaxX()-Math.ulp(bounds.getMaxX()), bounds.getCenterY()), 0));
		}

		@DisplayName("#3")
		@Test
		public void testContains_assertion3() {
			assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()), 0));
		}

		@DisplayName("#4")
		@Test
		public void testContains_assertion4() {
			assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()-Math.ulp(bounds.getMaxY())), 0));
		}

		@DisplayName("#5")
		@Test
		public void testContains_assertion5() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getCenterY()), 0));
		}

		@DisplayName("#6")
		@Test
		public void testContains_assertion6() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getCenterY()), 0));
		}

		@DisplayName("#7")
		@Test
		public void testContains_assertion7() {
			assertFalse(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()-10), 0));
		}

		@DisplayName("#8")
		@Test
		public void testContains_assertion8() {
			assertFalse(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()+10), 0));
		}

		@DisplayName("#9")
		@Test
		public void testContains_assertion9() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMinY()-10), 0));
		}

		@DisplayName("#10")
		@Test
		public void testContains_assertion10() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMaxY()+10), 0));
		}

		@DisplayName("#11")
		@Test
		public void testContains_assertion11() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMinY()-10), 0));
		}

		@DisplayName("#12")
		@Test
		public void testContains_assertion12() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMaxY()+10), 0));
		}

		@DisplayName("#13")
		@Test
		public void testContains_assertion13() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMinX(), bounds.getCenterY()), 5));
		}

		@DisplayName("#14")
		@Test
		public void testContains_assertion14() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMaxX(), bounds.getCenterY()), 5));
		}

		@DisplayName("#15")
		@Test
		public void testContains_assertion15() {
			assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()), 5));
		}

		@DisplayName("#16")
		@Test
		public void testContains_assertion16() {
			assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()), 5));
		}

		@DisplayName("#17")
		@Test
		public void testContains_assertion17() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getCenterY()), 5));
		}

		@DisplayName("#18")
		@Test
		public void testContains_assertion18() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getCenterY()), 5));
		}

		@DisplayName("#19")
		@Test
		public void testContains_assertion19() {
			assertFalse(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()-10), 5));
		}

		@DisplayName("#20")
		@Test
		public void testContains_assertion20() {
			assertFalse(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()+10), 5));
		}

		@DisplayName("#21")
		@Test
		public void testContains_assertion21() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMinY()-10), 5));
		}

		@DisplayName("#22")
		@Test
		public void testContains_assertion22() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMaxY()+10), 5));
		}

		@DisplayName("#23")
		@Test
		public void testContains_assertion23() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMinY()-10), 5));
		}

		@DisplayName("#24")
		@Test
		public void testContains_assertion24() {
			assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMaxY()+10), 5));
		}

		@DisplayName("#25")
		@Test
		public void testContains_assertion25() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMinX(), bounds.getCenterY()), 50));
		}

		@DisplayName("#26")
		@Test
		public void testContains_assertion26() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMaxX(), bounds.getCenterY()), 50));
		}

		@DisplayName("#27")
		@Test
		public void testContains_assertion27() {
			assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()), 50));
		}

		@DisplayName("#28")
		@Test
		public void testContains_assertion28() {
			assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()), 50));
		}

		@DisplayName("#29")
		@Test
		public void testContains_assertion29() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getCenterY()), 50));
		}

		@DisplayName("#30")
		@Test
		public void testContains_assertion30() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getCenterY()), 50));
		}

		@DisplayName("#31")
		@Test
		public void testContains_assertion31() {
			assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()-10), 50));
		}

		@DisplayName("#32")
		@Test
		public void testContains_assertion32() {
			assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()+10), 50));
		}

		@DisplayName("#33")
		@Test
		public void testContains_assertion33() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMinY()-10), 50));
		}

		@DisplayName("#34")
		@Test
		public void testContains_assertion34() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMaxY()+10), 50));
		}

		@DisplayName("#35")
		@Test
		public void testContains_assertion35() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMinY()-10), 50));
		}

		@DisplayName("#36")
		@Test
		public void testContains_assertion36() {
			assertTrue(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMaxY()+10), 50));
		}
	}

	@DisplayName("setContainerColorUse")
	@Nested
	public class SetContainerColorUse {

		private L thelayer;

		@BeforeEach
		public void setUp() {
			thelayer = getLayer();
		}

		@DisplayName("loop iteration")
		@Test
		public void testSetContainerColorUse() {
			for(int i=0; i<SET_TEST_COUNT; ++i) {
				boolean newValue = getRandom().nextBoolean();
				thelayer.setContainerColorUse(newValue);
				assertEquals(newValue,thelayer.isContainerColorUsed());
			}
		}
	}

	@DisplayName("setClickable")
	@Nested
	public class SetClickable {

		private L thelayer;

		@BeforeEach
		public void setUp() {
			thelayer = getLayer();
		}

		@DisplayName("loop iteration")
		@Test
		public void testSetClickable() {
			for(int i=0; i<SET_TEST_COUNT; ++i) {
				boolean newValue = getRandom().nextBoolean();
				thelayer.setClickable(newValue);
				assertEquals(newValue,thelayer.isClickable());
			}
		}
	}

	@DisplayName("setVisible")
	@Nested
	public class SetVisible {

		private L thelayer;

		@BeforeEach
		public void setUp() {
			thelayer = getLayer();
		}

		@DisplayName("loop iteration")
		@Test
		public void testSetVisible() {
			for(int i=0; i<SET_TEST_COUNT; ++i) {
				boolean newValue = getRandom().nextBoolean();
				thelayer.setVisible(newValue);
				assertEquals(newValue,thelayer.isVisible());
			}
		}
	}

	@DisplayName("setRemovable")
	@Nested
	public class SetRemovable {

		private L thelayer;

		@BeforeEach
		public void setUp() {
			thelayer = getLayer();
		}

		@DisplayName("loop iteration")
		@Test
		public void testSetRemovable() {
			for(int i=0; i<SET_TEST_COUNT; ++i) {
				boolean newValue = getRandom().nextBoolean();
				thelayer.setRemovable(newValue);
				assertEquals(newValue,thelayer.isRemovable());
			}
		}
	}

	@DisplayName("setColor")
	@Nested
	public class SetColor {

		private L thelayer;

		@BeforeEach
		public void setUp() {
			thelayer = getLayer();
		}

		@DisplayName("loop iteration")
		@Test
		public void testSetColor() {
			for(int i=0; i<SET_TEST_COUNT; ++i) {
				int newValue = getRandom().nextInt(0xFFFFFF);
				thelayer.setColor(newValue);
				assertEpsilonEquals(newValue,thelayer.getColor());
			}
		}
	}
}
