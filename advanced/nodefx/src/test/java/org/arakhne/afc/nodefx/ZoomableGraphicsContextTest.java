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

package org.arakhne.afc.nodefx;

import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.testtools.AbstractTestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("all")
public class ZoomableGraphicsContextTest extends AbstractTestCase {

	private ZoomableCanvas canvas;

	private GraphicsContext gcfx;
	
	private ZoomableGraphicsContext gc;

	private DoubleProperty scale;

	private DoubleProperty width;

	private DoubleProperty height;

	private IntegerProperty budget;

	private ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> documentBounds;

	private ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> visibleArea;

	private CenteringTransform transform;

	@Before
	public void setUp() throws Exception {
		this.canvas = mock(ZoomableCanvas.class);
		Constructor<GraphicsContext> cons = GraphicsContext.class.getDeclaredConstructor(Canvas.class);
		cons.setAccessible(true);
		this.gcfx = cons.newInstance(this.canvas);
		this.scale = new SimpleDoubleProperty(2);
		this.width = new SimpleDoubleProperty(60);
		this.height = new SimpleDoubleProperty(80);
		this.documentBounds = new SimpleObjectProperty<>(new Rectangle2d(10, 20, 30, 40));
		this.visibleArea = new SimpleObjectProperty<>(new Rectangle2d(11, 21, 28, 38));
		this.budget = new SimpleIntegerProperty(10);
		this.transform = new CenteringTransform(
				new SimpleBooleanProperty(false),
				new SimpleBooleanProperty(false),
				this.visibleArea);
		this.gc = new ZoomableGraphicsContext(
				this.gcfx,
				this.scale,
				this.documentBounds,
				this.visibleArea,
				this.width,
				this.height,
				this.budget,
				this.transform);
		this.gc.prepareRendering();
	}

	@After
	public void tearDown() {
		this.gc = null;
		this.gcfx = null;
		this.canvas = null;
		this.scale = null;
		this.width = null;
		this.height = null;
		this.documentBounds = null;
		this.budget = null;
		this.visibleArea = null;
		this.transform = null;
	}
	
	@Test
	public void prepareRendering() {
		this.gc.consumeBudget();
		assertEquals(9, this.gc.getBudget());
		this.gc.prepareRendering();
		assertEquals(10, this.gc.getBudget());
	}

	@Test
	public void getBudget() {
		assertEquals(10, this.gc.getBudget());
	}

	@Test
	public void consumeBudget() {
		assertTrue(this.gc.consumeBudget());
		assertEquals(9, this.gc.getBudget());
		assertTrue(this.gc.consumeBudget());
		assertEquals(8, this.gc.getBudget());
		assertTrue(this.gc.consumeBudget());
		assertEquals(7, this.gc.getBudget());
		assertTrue(this.gc.consumeBudget());
		assertEquals(6, this.gc.getBudget());
		assertTrue(this.gc.consumeBudget());
		assertEquals(5, this.gc.getBudget());
		assertTrue(this.gc.consumeBudget());
		assertEquals(4, this.gc.getBudget());
		assertTrue(this.gc.consumeBudget());
		assertEquals(3, this.gc.getBudget());
		assertTrue(this.gc.consumeBudget());
		assertEquals(2, this.gc.getBudget());
		assertTrue(this.gc.consumeBudget());
		assertEquals(1, this.gc.getBudget());
		assertTrue(this.gc.consumeBudget());
		assertEquals(0, this.gc.getBudget());
		assertFalse(this.gc.consumeBudget());
		assertEquals(0, this.gc.getBudget());
	}

	@Test
	public void getState() {
		assertEquals(0, this.gc.getState());
	}

	@Test
	public void setState() {
		this.gc.setState(1234);
		assertEquals(1234, this.gc.getState());
		this.gc.prepareRendering();
		assertEquals(0, this.gc.getState());
	}

	@Test
	public void getDocumentBounds() {
		assertSame(this.documentBounds.get(), this.gc.getDocumentBounds());
	}

	@Test
	public void getVisibleArea() {
		assertSame(this.visibleArea.get(), this.gc.getVisibleArea());
	}

	@Test
	public void getCanvas() {
		assertSame(this.canvas, this.gc.getCanvas());
	}

	@Test
	public void getWrappedGraphicsContext2D() {
		assertSame(this.gcfx, this.gc.getWrappedGraphicsContext2D());
	}

	@Test
	public void rgb() {
		Color c;
		
		c = this.gc.rgb(0);
		assertEpsilonEquals(0, c.getRed());
		assertEpsilonEquals(0, c.getGreen());
		assertEpsilonEquals(0, c.getBlue());
		assertEpsilonEquals(1, c.getOpacity());

		c = this.gc.rgb(0xFF);
		assertEpsilonEquals(0, c.getRed());
		assertEpsilonEquals(0, c.getGreen());
		assertEpsilonEquals(1, c.getBlue());
		assertEpsilonEquals(1, c.getOpacity());

		c = this.gc.rgb(0xFFFF);
		assertEpsilonEquals(0, c.getRed());
		assertEpsilonEquals(1, c.getGreen());
		assertEpsilonEquals(1, c.getBlue());
		assertEpsilonEquals(1, c.getOpacity());

		c = this.gc.rgb(0xFFFFFF);
		assertEpsilonEquals(1, c.getRed());
		assertEpsilonEquals(1, c.getGreen());
		assertEpsilonEquals(1, c.getBlue());
		assertEpsilonEquals(1, c.getOpacity());

		c = this.gc.rgb(0x07FFFFFF);
		assertEpsilonEquals(1, c.getRed());
		assertEpsilonEquals(1, c.getGreen());
		assertEpsilonEquals(1, c.getBlue());
		assertEpsilonEquals(1, c.getOpacity());
	}

	@Test
	public void rgba() {
		Color c;
		
		c = this.gc.rgba(0);
		assertEpsilonEquals(0, c.getRed());
		assertEpsilonEquals(0, c.getGreen());
		assertEpsilonEquals(0, c.getBlue());
		assertEpsilonEquals(0, c.getOpacity());

		c = this.gc.rgba(0xFF);
		assertEpsilonEquals(0, c.getRed());
		assertEpsilonEquals(0, c.getGreen());
		assertEpsilonEquals(1, c.getBlue());
		assertEpsilonEquals(0, c.getOpacity());

		c = this.gc.rgba(0xFFFF);
		assertEpsilonEquals(0, c.getRed());
		assertEpsilonEquals(1, c.getGreen());
		assertEpsilonEquals(1, c.getBlue());
		assertEpsilonEquals(0, c.getOpacity());

		c = this.gc.rgba(0xFFFFFF);
		assertEpsilonEquals(1, c.getRed());
		assertEpsilonEquals(1, c.getGreen());
		assertEpsilonEquals(1, c.getBlue());
		assertEpsilonEquals(0, c.getOpacity());

		c = this.gc.rgba(0x07FFFFFF);
		assertEpsilonEquals(1, c.getRed());
		assertEpsilonEquals(1, c.getGreen());
		assertEpsilonEquals(1, c.getBlue());
		assertEpsilonEquals((double)0x07/(double)0xFF, c.getOpacity());
	}

	@Test
	public void doc2fxX() {
		assertEpsilonEquals(-16, this.gc.doc2fxX(2));
	}

	@Test
	public void fx2docX() {
		assertEpsilonEquals(-11, this.gc.fx2docX(2));
	}

	@Test
	public void doc2fxY() {
		assertEpsilonEquals(-36, this.gc.doc2fxY(2));
	}

	@Test
	public void fx2docY() {
		assertEpsilonEquals(-21, this.gc.fx2docY(2));
	}

	@Test
	public void doc2fxSize() {
		assertEpsilonEquals(4, this.gc.doc2fxSize(2));
	}

	@Test
	public void fx2docSize() {
		assertEpsilonEquals(1, this.gc.fx2docSize(2));
	}

	@Test
	public void doc2fxAngle() {
		assertEpsilonEquals(2, this.gc.doc2fxAngle(2));
	}

	@Test
	public void fx2docAngle() {
		assertEpsilonEquals(2, this.gc.fx2docAngle(2));
	}

	@Test
	public void getLevelOfDetails() {
		assertEquals(LevelOfDetails.NORMAL, this.gc.getLevelOfDetails());
		this.scale.set(1000);
		assertEquals(LevelOfDetails.NORMAL, this.gc.getLevelOfDetails());
		this.gc.prepareRendering();
		assertEquals(LevelOfDetails.HIGH, this.gc.getLevelOfDetails());
		this.scale.set(0.001);
		this.gc.prepareRendering();
		assertEquals(LevelOfDetails.LOW, this.gc.getLevelOfDetails());
	}

}
