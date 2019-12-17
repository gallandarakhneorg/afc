/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.gis.maplayer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.AbstractGisTest;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

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
public abstract class AbstractMapLayerTest<L extends MapLayer> extends AbstractGisTest {

	private static final int SET_TEST_COUNT = 1000;

	private L layer;

	private GISLayerContainerStub container;

	/**
	 */
	public AbstractMapLayerTest() {
		this.layer = null;
		this.container = null;
	}

	/** Testing the clone feature.
	 *
	 * @throws Exception
	 */
	public abstract void testClone() throws Exception;

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

	@Test
	public abstract void testGetBounds();

	private static Rectangle2d newRectangle2d(double x1, double y1, double x2, double y2) {
		Rectangle2d r = new Rectangle2d();
		r.setFromCorners(x1, y1, x2, y2);
		return r;
	}

	@Test
	public void testIntersects() {
		L thelayer = getLayer();

		Rectangle2d bounds = getExpectedChildBounds();
		assertNotNull(bounds);

		double x0 = bounds.getMinX();
		double y0 = bounds.getMinY();
		double x1 = bounds.getMaxX();
		double y1 = bounds.getMaxY();
		double xc = bounds.getCenterX();
		double yc = bounds.getCenterY();
		double w = bounds.getWidth();
		double h = bounds.getHeight();

		assertTrue(thelayer.intersects(newRectangle2d(x0,y0,x0+w,y0+h)));
		assertTrue(thelayer.intersects(newRectangle2d(x0-10,y0-10,x0+10,y0+10)));
		assertTrue(thelayer.intersects(newRectangle2d(xc,yc,xc+w,yc+h)));
		assertTrue(thelayer.intersects(newRectangle2d(xc,yc,xc+1,yc+1)));
		assertTrue(thelayer.intersects(newRectangle2d(x0-10,y0-10,x0-10+w*2,y0-10+h*2)));

		// Use Math.ulp in order to avoid approximation errors.
		assertFalse(thelayer.intersects(newRectangle2d(x0-10,y0,x0-Math.ulp(x0),y0+1)));
		assertFalse(thelayer.intersects(newRectangle2d(x1+10,y0,x1+Math.ulp(x1),y0+1)));
		assertFalse(thelayer.intersects(newRectangle2d(x0,y0-10,x0+1,y0-Math.ulp(y0))));
		assertFalse(thelayer.intersects(newRectangle2d(x0,y1+10,x0+1,y1+Math.ulp(y1))));
	}

	@Test
	public void testContains() {
		L thelayer = getLayer();

		Rectangle2d bounds = getExpectedChildBounds();

		assertTrue(thelayer.contains(new Point2d(bounds.getMinX(), bounds.getCenterY()), 0));
		assertTrue(thelayer.contains(new Point2d(bounds.getMaxX()-Math.ulp(bounds.getMaxX()), bounds.getCenterY()), 0));
		assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()), 0));
		assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()-Math.ulp(bounds.getMaxY())), 0));

		assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getCenterY()), 0));
		assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getCenterY()), 0));
		assertFalse(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()-10), 0));
		assertFalse(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()+10), 0));
		assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMinY()-10), 0));
		assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMaxY()+10), 0));
		assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMinY()-10), 0));
		assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMaxY()+10), 0));

		assertTrue(thelayer.contains(new Point2d(bounds.getMinX(), bounds.getCenterY()), 5));
		assertTrue(thelayer.contains(new Point2d(bounds.getMaxX(), bounds.getCenterY()), 5));
		assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()), 5));
		assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()), 5));

		assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getCenterY()), 5));
		assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getCenterY()), 5));
		assertFalse(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()-10), 5));
		assertFalse(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()+10), 5));
		assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMinY()-10), 5));
		assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMaxY()+10), 5));
		assertFalse(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMinY()-10), 5));
		assertFalse(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMaxY()+10), 5));

		assertTrue(thelayer.contains(new Point2d(bounds.getMinX(), bounds.getCenterY()), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getMaxX(), bounds.getCenterY()), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()), 50));

		assertTrue(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getCenterY()), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getCenterY()), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMinY()-10), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getCenterX(), bounds.getMaxY()+10), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMinY()-10), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMaxY()+10), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getMaxX()+10, bounds.getMinY()-10), 50));
		assertTrue(thelayer.contains(new Point2d(bounds.getMinX()-10, bounds.getMaxY()+10), 50));
    }

	@Test
    public void testSetContainerColorUse() {
		L thelayer = getLayer();

		for(int i=0; i<SET_TEST_COUNT; ++i) {
			boolean newValue = getRandom().nextBoolean();
			thelayer.setContainerColorUse(newValue);
			assertEquals(newValue,thelayer.isContainerColorUsed());
		}
    }

	@Test
    public void testSetClickable() {
		L thelayer = getLayer();

		for(int i=0; i<SET_TEST_COUNT; ++i) {
			boolean newValue = getRandom().nextBoolean();
			thelayer.setClickable(newValue);
			assertEquals(newValue,thelayer.isClickable());
		}
    }

	@Test
    public void testSetVisible() {
		L thelayer = getLayer();

		for(int i=0; i<SET_TEST_COUNT; ++i) {
			boolean newValue = getRandom().nextBoolean();
			thelayer.setVisible(newValue);
			assertEquals(newValue,thelayer.isVisible());
		}
    }

	@Test
    public void testSetRemovable() {
		L thelayer = getLayer();

		for(int i=0; i<SET_TEST_COUNT; ++i) {
			boolean newValue = getRandom().nextBoolean();
			thelayer.setRemovable(newValue);
			assertEquals(newValue,thelayer.isRemovable());
		}
    }

	@Test
    public void testSetColor() {
		L thelayer = getLayer();

		for(int i=0; i<SET_TEST_COUNT; ++i) {
			int newValue = getRandom().nextInt(0xFFFFFF);
			thelayer.setColor(newValue);
			assertEpsilonEquals(newValue,thelayer.getColor());
		}
    }

}
