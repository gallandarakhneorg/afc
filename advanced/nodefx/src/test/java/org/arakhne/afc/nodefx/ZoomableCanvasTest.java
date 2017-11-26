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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.nodefx.tests.MyDoc;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.testtools.jfx.JfxRunner;
import org.arakhne.afc.testtools.jfx.TestInJfxThread;
import org.arakhne.afc.util.InformedIterable;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("all")
@RunWith(JfxRunner.class)
public class ZoomableCanvasTest extends AbstractTestCase {

	private MyDoc model;
	
	private Rectangle2d bounds;

	private ZoomableCanvas canvas;

	@Before
	public void setUp() {
		this.bounds = new Rectangle2d(1, 2, 3, 4);
		this.model = spy(new MyDoc(this.bounds));
		this.canvas = new ZoomableCanvas(this.model);
		this.canvas.setWidth(100);
		this.canvas.setHeight(100);
	}

	@After
	public void tearDown() {
		this.model = null;
		this.canvas = null;
		this.bounds = null;
	}
	
	@Test
	@TestInJfxThread
	public void documentModelProperty() {
		ObjectProperty prop = this.canvas.documentModelProperty();
		assertSame(this.model, prop.get());
	}

	@Test
	@TestInJfxThread
	public void getDocumentModel() {
		Object prop = this.canvas.getDocumentModel();
		assertSame(this.model, prop);
	}

	@Test
	@TestInJfxThread
	public void setDocumentModel() {
		InformedIterable model2 = mock(InformedIterable.class);
		this.canvas.setDocumentModel(model2);
		Object prop = this.canvas.getDocumentModel();
		assertSame(model2, prop);		
	}

	@Test
	@TestInJfxThread
	public void documentDrawerProperty() {
		ObjectProperty prop = this.canvas.documentDrawerProperty();
		assertNotNull(prop.get());
	}

	@Test
	@TestInJfxThread
	public void getDocumentDrawer() {
		Drawer prop = this.canvas.getDocumentDrawer();
		assertNotNull(prop);
	}

	@Test
	@TestInJfxThread
	public void setDocumentDrawer() {
		Drawer drawer = mock(Drawer.class);
		this.canvas.setDocumentDrawer(drawer);
		Drawer prop = this.canvas.getDocumentDrawer();
		assertSame(drawer, prop);
	}

	@Test
	@TestInJfxThread
	public void documentBoundsProperty() {
		ReadOnlyObjectProperty prop = this.canvas.documentBoundsProperty();
		assertSame(this.bounds, prop.get());
	}

	@Test
	@TestInJfxThread
	public void getDocumentBounds() {
		Rectangle2afp prop = this.canvas.getDocumentBounds();
		assertSame(this.bounds, prop);
	}

	@Test
	@TestInJfxThread
	public void scaleValueProperty() {
		DoubleProperty prop = this.canvas.scaleValueProperty();
		assertEpsilonEquals(1, prop.get());
	}

	@Test
	@TestInJfxThread
	public void getScaleValue() {
		assertEpsilonEquals(1, this.canvas.getScaleValue());
	}

	@Test
	@TestInJfxThread
	public void setScaleValue() {
		assertEpsilonEquals(1, this.canvas.getScaleValue());
		this.canvas.setScaleValue(2);
		assertEpsilonEquals(2, this.canvas.getScaleValue());
	}

	@Test
	@TestInJfxThread
	public void setScaleValueDoubleDoubleDouble() {
		assertEpsilonEquals(1, this.canvas.getScaleValue());
		this.canvas.setScaleValue(2, 3, 4);
		assertEpsilonEquals(2, this.canvas.getScaleValue());
		assertEpsilonEquals(3, this.canvas.getViewportCenterX());
		assertEpsilonEquals(4, this.canvas.getViewportCenterY());
	}

	@Test
	@TestInJfxThread
	public void minScaleValueProperty() {
		DoubleProperty prop = this.canvas.minScaleValueProperty();
		assertEpsilonEquals(ZoomableViewer.MINIMUM_SCALE_VALUE, prop.get());
	}

	@Test
	@TestInJfxThread
	public void getMinScaleValue() {
		assertEpsilonEquals(ZoomableViewer.MINIMUM_SCALE_VALUE, this.canvas.getMinScaleValue());
	}

	@Test
	@TestInJfxThread
	public void setMinScaleValue() {
		this.canvas.setMinScaleValue(0.000001);
		assertEpsilonEquals(0.000001, this.canvas.getMinScaleValue());
	}

	@Test
	@TestInJfxThread
	public void maxScaleValueProperty() {
		DoubleProperty prop = this.canvas.maxScaleValueProperty();
		assertEpsilonEquals(ZoomableViewer.MAXIMUM_SCALE_VALUE, prop.get());
	}

	@Test
	@TestInJfxThread
	public void getMaxScaleValue() {
		assertEpsilonEquals(ZoomableViewer.MAXIMUM_SCALE_VALUE, this.canvas.getMaxScaleValue());
	}

	@Test
	@TestInJfxThread
	public void setMaxScaleValue() {
		this.canvas.setMaxScaleValue(100000);
		assertEpsilonEquals(100000, this.canvas.getMaxScaleValue());
	}

	@Test
	@TestInJfxThread
	public void viewportCenterXProperty() {
		DoubleProperty prop = this.canvas.viewportCenterXProperty();
		assertEpsilonEquals(this.bounds.getCenterX(), prop.get());
	}

	@Test
	@TestInJfxThread
	public void getViewportCenterX() {
		assertEpsilonEquals(this.bounds.getCenterX(), this.canvas.getViewportCenterX());
	}

	@Test
	@TestInJfxThread
	public void setViewportCenterX() {
		this.canvas.setViewportCenterX(1234);
		assertEpsilonEquals(1234, this.canvas.getViewportCenterX());
	}

	@Test
	@TestInJfxThread
	public void viewportCenterYProperty() {
		DoubleProperty prop = this.canvas.viewportCenterYProperty();
		assertEpsilonEquals(this.bounds.getCenterY(), prop.get());
	}

	@Test
	@TestInJfxThread
	public void getViewportCenterY() {
		assertEpsilonEquals(this.bounds.getCenterY(), this.canvas.getViewportCenterY());
	}

	@Test
	@TestInJfxThread
	public void setViewportCenterY() {
		this.canvas.setViewportCenterY(1234);
		assertEpsilonEquals(1234, this.canvas.getViewportCenterY());
	}

	@Test
	@TestInJfxThread
	public void setViewportCenter() {
		this.canvas.setViewportCenter(1234, 5678);
		assertEpsilonEquals(1234, this.canvas.getViewportCenterX());
		assertEpsilonEquals(5678, this.canvas.getViewportCenterY());
	}

	@Test
	@TestInJfxThread
	public void viewportBoundsProperty() {
		ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> prop = this.canvas.viewportBoundsProperty();
		Rectangle2afp bounds = prop.get();
		assertEpsilonEquals(this.bounds.getCenterX() - 50, bounds.getMinX());
		assertEpsilonEquals(this.bounds.getCenterY() - 50, bounds.getMinY());
		assertEpsilonEquals(100, bounds.getWidth());
		assertEpsilonEquals(100, bounds.getHeight());
	}

	@Test
	@TestInJfxThread
	public void getViewportBounds() {
		Rectangle2afp bounds = this.canvas.getViewportBounds();
		assertEpsilonEquals(this.bounds.getCenterX() - 50, bounds.getMinX());
		assertEpsilonEquals(this.bounds.getCenterY() - 50, bounds.getMinY());
		assertEpsilonEquals(100, bounds.getWidth());
		assertEpsilonEquals(100, bounds.getHeight());
	}

	@Test
	@TestInJfxThread
	public void scaleChangeProperty() {
		DoubleProperty prop = this.canvas.scaleChangeProperty();
		assertEpsilonEquals(ZoomableViewer.DEFAULT_SCALE_CHANGE, prop.get());
	}

	@Test
	@TestInJfxThread
	public void getScaleChange() {
		assertEpsilonEquals(ZoomableViewer.DEFAULT_SCALE_CHANGE, this.canvas.getScaleChange());
	}

	@Test
	@TestInJfxThread
	public void setScaleChange() {
		this.canvas.setScaleChange(1234.567);
		assertEpsilonEquals(1234.567, this.canvas.getScaleChange());
	}

	@Test
	@TestInJfxThread
	public void invertedAxisXProperty() {
		BooleanProperty prop = this.canvas.invertedAxisXProperty();
		assertFalse(prop.get());
	}

	@Test
	@TestInJfxThread
	public void isInvertedAxisX() {
		assertFalse(this.canvas.isInvertedAxisX());
	}

	@Test
	@TestInJfxThread
	public void setInvertedAxisX() {
		this.canvas.setInvertedAxisX(true);
		assertTrue(this.canvas.isInvertedAxisX());
	}

	@Test
	@TestInJfxThread
	public void invertedAxisYProperty() {
		BooleanProperty prop = this.canvas.invertedAxisYProperty();
		assertFalse(prop.get());
	}

	@Test
	@TestInJfxThread
	public void isInvertedAxisY() {
		assertFalse(this.canvas.isInvertedAxisY());
	}

	@Test
	@TestInJfxThread
	public void setInvertedAxisY() {
		this.canvas.setInvertedAxisY(true);
		assertTrue(this.canvas.isInvertedAxisY());
	}

	@Test
	@TestInJfxThread
	public void drawableElementBudgetProperty() {
		IntegerProperty prop = this.canvas.drawableElementBudgetProperty();
		assertEquals(ZoomableViewer.DEFAULT_DRAWABLE_ELEMENT_BUDGET, prop.get());
	}

	@Test
	@TestInJfxThread
	public void getDrawableElementBudget() {
		assertEquals(ZoomableViewer.DEFAULT_DRAWABLE_ELEMENT_BUDGET, this.canvas.getDrawableElementBudget());
	}

	@Test
	@TestInJfxThread
	public void setDrawableElementBudget() {
		this.canvas.setDrawableElementBudget(1234);
		assertEquals(1234, this.canvas.getDrawableElementBudget());
	}

	@Test
	@TestInJfxThread
	public void getScaleValueToFit_true() {
		assertEpsilonEquals(1, this.canvas.getScaleValueToFit(true));
	}

	@Test
	@TestInJfxThread
	public void getScaleValueToFit_false() {
		assertEpsilonEquals(25, this.canvas.getScaleValueToFit(false));
	}

	@Test
	@TestInJfxThread
	public void getScaleValueToFit() {
		assertEpsilonEquals(25, this.canvas.getScaleValueToFit());
	}

	@Test
	@TestInJfxThread
	public void toDocumentPositionX() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		assertEpsilonEquals(2.5, this.canvas.toDocumentPositionX(50));
		assertEpsilonEquals(-30.0203, this.canvas.toDocumentPositionX(10));
	}

	@Test
	@TestInJfxThread
	public void toDocumentPositionY() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		assertEpsilonEquals(4, this.canvas.toDocumentPositionY(50));
		assertEpsilonEquals(-28.5203, this.canvas.toDocumentPositionY(10));
	}

	@Test
	@TestInJfxThread
	public void toDocumentSize() {
		this.canvas.setScaleValue(1.23);
		assertEpsilonEquals(8.1301, this.canvas.toDocumentSize(10));
	}

	@Test
	@TestInJfxThread
	public void toDocumentPositionPoint2D() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		Point2d p = this.canvas.toDocumentPosition(new Point2d(10, 10));
		assertEpsilonEquals(-30.0203, p.getX());
		assertEpsilonEquals(-28.5203, p.getY());
	}

	@Test
	@TestInJfxThread
	public void toDocumentPositionDoubleDouble() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		Point2d p = this.canvas.toDocumentPosition(10, 10);
		assertEpsilonEquals(-30.0203, p.getX());
		assertEpsilonEquals(-28.5203, p.getY());
	}

	@Test
	@TestInJfxThread
	public void toDocumentRectRectangle2afp() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		Rectangle2d r = this.canvas.toDocumentRect(new Rectangle2d(10, 10, 34, 56));
		assertEpsilonEquals(-30.0203, r.getMinX());
		assertEpsilonEquals(-28.5203, r.getMinY());
		assertEpsilonEquals(27.6423, r.getWidth());
		assertEpsilonEquals(45.5285, r.getHeight());
	}

	@Test
	@TestInJfxThread
	public void toDocumentRectDoubleDoubleDoubleDouble() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		Rectangle2d r = this.canvas.toDocumentRect(10, 10, 34, 56);
		assertEpsilonEquals(-30.0203, r.getMinX());
		assertEpsilonEquals(-28.5203, r.getMinY());
		assertEpsilonEquals(27.6423, r.getWidth());
		assertEpsilonEquals(45.5285, r.getHeight());
	}

	@Test
	@TestInJfxThread
	public void toScreenPositionX() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		assertEpsilonEquals(50, this.canvas.toScreenPositionX(2.5));
		assertEpsilonEquals(59.225, this.canvas.toScreenPositionX(10));
	}

	@Test
	@TestInJfxThread
	public void toScreenPositionY() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		assertEpsilonEquals(50, this.canvas.toScreenPositionY(4));
		assertEpsilonEquals(57.38, this.canvas.toScreenPositionY(10));
	}

	@Test
	@TestInJfxThread
	public void toScreenSize() {
		this.canvas.setScaleValue(1.23);
		assertEpsilonEquals(12.3, this.canvas.toScreenSize(10));
	}

	@Test
	@TestInJfxThread
	public void toScreenPositionPoint2D() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		Point2d p = this.canvas.toScreenPosition(new Point2d(10, 10));
		assertEpsilonEquals(59.225, p.getX());
		assertEpsilonEquals(57.38, p.getY());
	}

	@Test
	@TestInJfxThread
	public void toScreenPositionDoubleDouble() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		Point2d p = this.canvas.toScreenPosition(10, 10);
		assertEpsilonEquals(59.225, p.getX());
		assertEpsilonEquals(57.38, p.getY());
	}

	@Test
	@TestInJfxThread
	public void toScreenRectRectangle2afp() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		Rectangle2d r = this.canvas.toScreenRect(new Rectangle2d(10, 10, 34, 56));
		assertEpsilonEquals(59.225, r.getMinX());
		assertEpsilonEquals(57.38, r.getMinY());
		assertEpsilonEquals(41.82, r.getWidth());
		assertEpsilonEquals(68.88, r.getHeight());
	}

	@Test
	@TestInJfxThread
	public void toScreenRectDoubleDoubleDoubleDouble() {
		//Document: Rectangle2d(1, 2, 3, 4)
		//Canvas: Rectangle2d(0, 0, 100, 100)
		this.canvas.setScaleValue(1.23);
		Rectangle2d r = this.canvas.toScreenRect(10, 10, 34, 56);
		assertEpsilonEquals(59.225, r.getMinX());
		assertEpsilonEquals(57.38, r.getMinY());
		assertEpsilonEquals(41.82, r.getWidth());
		assertEpsilonEquals(68.88, r.getHeight());
	}

}
