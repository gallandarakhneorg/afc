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

package org.arakhne.afc.gis.ui;

import java.util.EventListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import org.arakhne.afc.gis.mapelement.GISElementContainer;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.ui.drawers.GisContainerDrawer;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystemConstants;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.ListenerCollection;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Resizeable canvas for rendering GIS primitives.
 *
 * <p>The GIS elements are displayed within this {@code GisCanvas} by the {@link GisDrawer drawers}
 * that are declared as services.
 *
 * <p>The {@code GisCanvas} provides a tool for displaying GIS elements. It does not provide
 * advanced UI components (scroll bars, etc.) and interaction means (mouse support, etc.).
 *
 * @param <T> the type of the map elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class GisCanvas<T extends MapElement> extends Canvas implements GisViewer<T> {

	private static final boolean DEFAULT_INVERTED_X_AXIS = false;

	private static final boolean DEFAULT_INVERTED_Y_AXIS =
			CoordinateSystemConstants.GIS_2D.isLeftHanded() != CoordinateSystemConstants.JAVAFX_2D.isLeftHanded();

	private ListenerCollection<EventListener> listeners;

	private GisGraphicsContext gisGraphicsContext;

	private ObjectProperty<GISElementContainer<T>> model;

	private ObjectProperty<GisContainerDrawer<T>> drawer;

	private BooleanProperty invertXAxis;

	private BooleanProperty invertYAxis;

	private DoubleProperty viewportX;

	private DoubleProperty viewportY;

	private DoubleProperty scaleValue;

	private DoubleProperty minScaleValue;

	private DoubleProperty maxScaleValue;

	private DoubleProperty delta;

	private IntegerProperty budget;

	private ReadOnlyObjectWrapper<Rectangle2d> viewportBounds;

	private ReadOnlyObjectWrapper<Rectangle2afp<?, ?, ?, ?, ?, ?>> mapBounds;

	private final AtomicBoolean renderingEnable = new AtomicBoolean(false);

	private volatile Runnable drawRunAfterChain;

	private volatile Runnable refresher;

	private ChangeListener<? super Number> initializationListener = (it, oldValue, newValue) -> {
		if (getWidth() > 0. && getHeight() > 0) {
			final ChangeListener<? super Number> li = GisCanvas.this.initializationListener;
			GisCanvas.this.initializationListener = null;
			if (li != null) {
				widthProperty().removeListener(li);
				heightProperty().removeListener(li);
			}
			this.renderingEnable.set(true);
			drawContent();
		}
	};

	/** Constructor. The renderer is detected with the type replied by
	 * {@link GISElementContainer#getElementType()} on the model.
	 *
	 * @param model the source of the elements.
	 */
	public GisCanvas(GISElementContainer<T> model) {
		this(model, null);
	}

	/** Constructor.
	 *
	 * @param model the source of the elements.
	 * @param drawer the drawer.
	 */
	public GisCanvas(GISElementContainer<T> model, GisContainerDrawer<T> drawer) {
		assert model != null : AssertMessages.notNullParameter(0);
		mapModelProperty().set(model);
		if (drawer != null) {
			mapDrawerProperty().set(drawer);
		}
		// Add listener on initialization
		widthProperty().addListener(this.initializationListener);
		heightProperty().addListener(this.initializationListener);
		// 1) Create the viewport bound property
		viewportBoundsProperty();
		// 2) Listen the created property for refreshing the canvas.
		this.viewportBounds.addListener(new ChangeListener<Rectangle2d>() {
			@Override
			public void changed(ObservableValue<? extends Rectangle2d> observable, Rectangle2d oldValue,
					Rectangle2d newValue) {
				drawContent();
			}
		});
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public double minWidth(double height) {
		return 0;
	}

	@Override
	public double minHeight(double width) {
		return 0;
	}

	@Override
	public double maxWidth(double height) {
		return Double.MAX_VALUE;
	}

	@Override
	public double maxHeight(double width) {
		return Double.MAX_VALUE;
	}

	@Override
	public double prefWidth(double height) {
		return getMapBounds().getWidth() * getScaleValue();
	}

	@Override
	public double prefHeight(double width) {
		return getMapBounds().getHeight() * getScaleValue();
	}

	@Override
	public void resize(double width, double height) {
		setWidth(width);
		setHeight(height);
	}

	@Override
	public boolean usesMirroring() {
		// Avoid to change the coordinate origin when the node orientation changed.
		return false;
	}

	/** Replies the GIS graphics context.
	 * @return the graphics context.
	 */
	public GisGraphicsContext getGisGraphicsContext2D() {
		if (this.gisGraphicsContext == null) {
			final CenteringTransform transform = new CenteringTransform(
					invertedAxisXProperty(),
					invertedAxisYProperty(),
					viewportBoundsProperty());
			this.gisGraphicsContext = new GisGraphicsContext(
					getGraphicsContext2D(),
					scaleValueProperty(),
					mapBoundsProperty(),
					viewportBoundsProperty(),
					widthProperty(),
					heightProperty(),
					drawableElementBudgetProperty(),
					transform);
		}
		return this.gisGraphicsContext;
	}

	private static Runnable buildRunAfterChain(Runnable... runs) {
		assert runs.length > 0;
		Runnable next = null;
		for (int i = runs.length - 1; i >= 0; --i) {
			final Runnable run;
			if (next != null) {
				run = new UiRunnable(runs[i], next);
			} else {
				run = runs[i];
			}
			next = run;
		}
		assert next != null;
		return next;
	}

	/** UI Runnable Chain.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 15.0
	 */
	private static class UiRunnable implements Runnable {

		private final Runnable next;

		private final Runnable current;

		UiRunnable(Runnable current, Runnable next) {
			this.current = current;
			this.next = next;
		}

		@Override
		public void run() {
			try {
				if (this.current != null) {
					this.current.run();
				}
			} finally {
				if (this.next != null) {
					Platform.runLater(this.next);
				}
			}
		}

	}

	/** Draw the elements.
	 */
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:nestedifdepth"})
	protected void drawContent() {
		final boolean old = this.renderingEnable.getAndSet(false);
		if (old) {
			if (this.refresher == null) {
				if (this.drawRunAfterChain == null) {
					this.drawRunAfterChain = buildRunAfterChain(
						() -> fireDrawingStart(),
						() -> {
							final GISElementContainer<T> model = getMapModel();
							if (model != null) {
								final GraphicsContext gc = getGraphicsContext2D();
								gc.clearRect(0, 0, getWidth(), getHeight());

								final GisContainerDrawer<T> drawer = getMapDrawer();
								if (drawer != null) {
									final GisGraphicsContext gisgc = getGisGraphicsContext2D();
									gisgc.resetBudget();

									/*final Rectangle2afp<?, ?, ?, ?, ?, ?> viewport = getViewportBounds();
									gc.setStroke(Color.BLUE);
									gc.setLineWidth(5);
									gc.beginPath();
									gc.moveTo(5, 5);
									gc.lineTo(getWidth() - 5, 5);
									gc.lineTo(getWidth() - 5, getHeight() - 5);
									gc.lineTo(5, getHeight() - 5);
									gc.closePath();
									gc.stroke();*/

									drawer.draw(gisgc, model);

									/*final double fifteenPixels = gisgc.fx2gisSize(15);
									gisgc.setStroke(Color.RED);
									gisgc.setLineWidthInPixels(5);
									gisgc.beginPath();
									gisgc.moveTo(viewport.getMinX() + fifteenPixels, viewport.getMinY() + fifteenPixels);
									gisgc.lineTo(viewport.getMaxX() - fifteenPixels, viewport.getMinY() + fifteenPixels);
									gisgc.lineTo(viewport.getMaxX() - fifteenPixels, viewport.getMaxY() - fifteenPixels);
									gisgc.lineTo(viewport.getMinX() + fifteenPixels, viewport.getMaxY() - fifteenPixels);
									gisgc.closePath();
									gisgc.stroke();*/
								}
							}
						},
						() -> {
							fireDrawingEnd();
							this.renderingEnable.set(old);
						});
				}
				this.refresher = this.drawRunAfterChain;
				Platform.runLater(() -> {
					if (this.refresher != null) {
						this.refresher.run();
					}
					this.refresher = null;
				});
			} else {
				this.renderingEnable.getAndSet(true);
			}
		}
	}

	@Override
	public ObjectProperty<GISElementContainer<T>> mapModelProperty() {
		if (this.model == null) {
			this.model = new SimpleObjectProperty<GISElementContainer<T>>(this, MAP_MODEL_PROPERTY) {
				@Override
				protected void invalidated() {
					assert get() != null;
				}
			};
		}
		return this.model;
	}

	@Override
	public final GISElementContainer<T> getMapModel() {
		return mapModelProperty().get();
	}

	@Override
	public final void setMapModel(GISElementContainer<T> model) {
		mapModelProperty().set(model);
	}

	@Override
	public ObjectProperty<GisContainerDrawer<T>> mapDrawerProperty() {
		if (this.drawer == null) {
			final GisContainerDrawer<T> defaultDrawer = GisDrawer.getContainerDrawerFor(
					getMapModel().getElementType());
			this.drawer = new SimpleObjectProperty<GisContainerDrawer<T>>(this, MAP_DRAWER_PROPERTY, defaultDrawer) {
				@Override
				protected void invalidated() {
					if (get() == null) {
						final GisContainerDrawer<T> defaultDrawer = GisDrawer.getContainerDrawerFor(
								getMapModel().getElementType());
						set(defaultDrawer);
					}
				}
			};
		}
		return this.drawer;
	}

	@Override
	public final GisContainerDrawer<T> getMapDrawer() {
		return mapDrawerProperty().get();
	}

	@Override
	public final void setMapDrawer(GisContainerDrawer<T> drawer) {
		mapDrawerProperty().set(drawer);
	}

	@Override
	public ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> mapBoundsProperty() {
		if (this.mapBounds == null) {
			this.mapBounds = new ReadOnlyObjectWrapper<>(this, MAP_BOUNDS_PROPERTY);
			this.mapBounds.bind(Bindings.createObjectBinding(
					() ->  getMapModel().getBoundingBox(),
					mapModelProperty()));
		}
		return this.mapBounds.getReadOnlyProperty();
	}

	@Override
	public final Rectangle2afp<?, ?, ?, ?, ?, ?> getMapBounds() {
		return mapBoundsProperty().get();
	}

	@Override
	public DoubleProperty scaleValueProperty() {
		if (this.scaleValue == null) {
			this.scaleValue = new SimpleDoubleProperty(this, SCALE_VALUE_PROPERTY, 1.) {
				@Override
				protected void invalidated() {
					final double currentMin = getMinScaleValue();
					final double current = get();
					if (current < currentMin) {
						set(currentMin);
					} else {
						final double currentMax = getMaxScaleValue();
						if (current > currentMax) {
							set(currentMax);
						}
					}
				}
			};
		}
		return this.scaleValue;
	}

	@Override
	public final double getScaleValue() {
		return this.scaleValue == null ? 1. : this.scaleValue.get();
	}

	@Override
	public final void setScaleValue(double value) {
		scaleValueProperty().set(value);
	}

	@Override
	public final void setScaleValue(double scaleValue, double centerX, double centerY) {
		final boolean old = this.renderingEnable.getAndSet(false);
		scaleValueProperty().set(scaleValue);
		setViewportCenterX(centerX);
		setViewportCenterY(centerY);
		this.renderingEnable.set(old);
		drawContent();
	}

	@Override
	public DoubleProperty minScaleValueProperty() {
		if (this.minScaleValue == null) {
			this.minScaleValue = new SimpleDoubleProperty(this, MIN_SCALE_VALUE_PROPERTY, MINIMUM_SCALE_VALUE) {
				@Override
				protected void invalidated() {
					final double max = getMaxScaleValue();
					final double min = get();
					if (max < min) {
						setMaxScaleValue(min);
					} else {
						final double val = getScaleValue();
						if (val < min) {
							scaleValueProperty().set(min);
						}
					}
				}
			};
		}
		return this.minScaleValue;
	}

	@Override
	public final double getMinScaleValue() {
		return this.minScaleValue == null ? MINIMUM_SCALE_VALUE : this.minScaleValue.get();
	}

	@Override
	public final void setMinScaleValue(double value) {
		minScaleValueProperty().set(value);
	}

	@Override
	public DoubleProperty maxScaleValueProperty() {
		if (this.maxScaleValue == null) {
			this.maxScaleValue = new SimpleDoubleProperty(this, MAX_SCALE_VALUE_PROPERTY, MAXIMUM_SCALE_VALUE) {
				@Override
				protected void invalidated() {
					final double min = getMinScaleValue();
					final double max = get();
					if (max < min) {
						set(min);
					}
					final double val = getScaleValue();
					if (val > max) {
						scaleValueProperty().set(max);
					}
				}
			};
		}
		return this.maxScaleValue;
	}

	@Override
	public final double getMaxScaleValue() {
		return this.maxScaleValue == null ? MAXIMUM_SCALE_VALUE : this.maxScaleValue.get();
	}

	@Override
	public final void setMaxScaleValue(double value) {
		maxScaleValueProperty().set(value);
	}

	@Override
	public DoubleProperty viewportCenterXProperty() {
		if (this.viewportX == null) {
			this.viewportX = new SimpleDoubleProperty(this, VIEWPORT_CENTER_X_PROPERTY, getMapBounds().getCenterX());
		}
		return this.viewportX;
	}

	@Override
	public final double getViewportCenterX() {
		return this.viewportX == null ? getMapBounds().getCenterX() : this.viewportX.get();
	}

	@Override
	public final void setViewportCenterX(double x) {
		viewportCenterXProperty().set(x);
	}

	@Override
	public DoubleProperty viewportCenterYProperty() {
		if (this.viewportY == null) {
			this.viewportY = new SimpleDoubleProperty(this, VIEWPORT_CENTER_Y_PROPERTY, getMapBounds().getCenterY());
		}
		return this.viewportY;
	}

	@Override
	public final double getViewportCenterY() {
		return this.viewportY == null ? getMapBounds().getCenterY() : this.viewportY.get();
	}

	@Override
	public final void setViewportCenterY(double y) {
		viewportCenterYProperty().set(y);
	}

	@Override
	public final void setViewportCenter(double x, double y) {
		final boolean old = this.renderingEnable.getAndSet(false);
		setViewportCenterX(x);
		setViewportCenterY(y);
		this.renderingEnable.set(old);
		drawContent();
	}

	@Override
	public ReadOnlyObjectProperty<Rectangle2d> viewportBoundsProperty() {
		if (this.viewportBounds == null) {
			this.viewportBounds = new ReadOnlyObjectWrapper<>(this, VIEWPORT_BOUNDS_PROPERTY);
			this.viewportBounds.bind(Bindings.createObjectBinding(() -> {
				final double scale = getScaleValue();
				final double visibleAreaWidth = getWidth() / scale;
				final double visibleAreaHeight = getHeight() / scale;
				final double visibleAreaX = getViewportCenterX() - visibleAreaWidth / 2.;
				final double visibleAreaY = getViewportCenterY() - visibleAreaHeight / 2.;
				return new Rectangle2d(visibleAreaX, visibleAreaY, visibleAreaWidth, visibleAreaHeight);
			}, widthProperty(), heightProperty(), viewportCenterXProperty(), viewportCenterYProperty(),
					scaleValueProperty()));
		}
		return this.viewportBounds.getReadOnlyProperty();
	}

	@Override
	public final Rectangle2d getViewportBounds() {
		return viewportBoundsProperty().get();
	}

	@Override
	public double getScaleValueToFit(boolean minimizeOnly) {
		final Rectangle2afp<?, ?, ?, ?, ?, ?> map = getMapBounds();
		final double contentWidth = map.getWidth();
		final double contentHeight = map.getHeight();

		final double viewportWidth = getWidth();
		final double viewportHeight = getHeight();

		final double scaleX = contentWidth <= 0. ? 1. : viewportWidth / contentWidth;
		final double scaleY = contentHeight <= 0. ? 1. : viewportHeight / contentHeight;

		// distorted zoom: we don't want it => we search the minimum scale
		// factor and apply it
		double scale = Math.min(scaleX, scaleY);

		// check if zoom factor would be an enlargement and if so, just set
		// it to 1
		if (minimizeOnly && scale > 1.) {
			scale = 1.;
		}
		return scale;
	}

	@Override
	public DoubleProperty scaleChangeProperty() {
		if (this.delta == null) {
			this.delta = new SimpleDoubleProperty(this, SCALE_CHANGE_PROPERTY, DEFAULT_SCALE_CHANGE) {
				@Override
				protected void invalidated() {
					if (get() <= MIN_SCALE_CHANGE) {
						set(MIN_SCALE_CHANGE);
					}
				}
			};
		}
		return this.delta;
	}

	@Override
	public final double getScaleChange() {
		return this.delta == null ? DEFAULT_SCALE_CHANGE : this.delta.get();
	}

	@Override
	public final void setScaleChange(double change) {
		scaleChangeProperty().set(change);
	}

	@Override
	public BooleanProperty invertedAxisXProperty() {
		if (this.invertXAxis == null) {
			this.invertXAxis = new SimpleBooleanProperty(this, INVERTED_AXIS_X_PROPERTY, DEFAULT_INVERTED_X_AXIS);
		}
		return this.invertXAxis;
	}

	@Override
	public final boolean isInvertedAxisX() {
		return this.invertXAxis == null ? DEFAULT_INVERTED_X_AXIS : this.invertXAxis.get();
	}

	@Override
	public final void setInvertedAxisX(boolean inverted) {
		invertedAxisXProperty().set(inverted);
	}

	@Override
	public BooleanProperty invertedAxisYProperty() {
		if (this.invertYAxis == null) {
			this.invertYAxis = new SimpleBooleanProperty(this, INVERTED_AXIS_Y_PROPERTY, DEFAULT_INVERTED_Y_AXIS);
		}
		return this.invertYAxis;
	}

	@Override
	public final boolean isInvertedAxisY() {
		return this.invertYAxis == null ? DEFAULT_INVERTED_Y_AXIS : this.invertYAxis.get();
	}

	@Override
	public final void setInvertedAxisY(boolean inverted) {
		invertedAxisYProperty().set(inverted);
	}

	@Override
	public IntegerProperty drawableElementBudgetProperty() {
		if (this.budget == null) {
			this.budget = new SimpleIntegerProperty(this, DRAWABLE_ELEMENT_BUDGET_PROPERTY, DEFAULT_DRAWABLE_ELEMENT_BUDGET) {
				@Override
				protected void invalidated() {
					final int value = get();
					if (value < MIN_DRAWABLE_ELEMENT_BUDGET) {
						set(MIN_DRAWABLE_ELEMENT_BUDGET);
					}
				}
			};
		}
		return this.budget;
	}

	@Override
	public final int getDrawableElementBudget() {
		return this.budget == null ? DEFAULT_DRAWABLE_ELEMENT_BUDGET : this.budget.get();
	}

	@Override
	public final void setDrawableElementBudget(int budget) {
		drawableElementBudgetProperty().set(budget);
	}

	/** Add a drawing listener.
	 *
	 * @param listener the listener.
	 */
	public void addDrawingListener(DrawingListener listener) {
		if (this.listeners == null) {
			this.listeners = new ListenerCollection<>();
		}
		this.listeners.add(DrawingListener.class, listener);
	}

	/** Remove a drawing listener.
	 *
	 * @param listener the listener.
	 */
	public void removeDrawingListener(DrawingListener listener) {
		if (this.listeners != null) {
			this.listeners.remove(DrawingListener.class, listener);
			if (this.listeners.isEmpty()) {
				this.listeners = null;
			}
		}
	}

	/** Notifies listeners on drawing start.
	 */
	protected void fireDrawingStart() {
		final ListenerCollection<EventListener> list = this.listeners;
		if (list != null) {
			for (final DrawingListener listener : list.getListeners(DrawingListener.class)) {
				listener.onDrawingStart();
			}
		}
	}

	/** Notifies listeners on drawing finishing.
	 */
	protected void fireDrawingEnd() {
		final ListenerCollection<EventListener> list = this.listeners;
		if (list != null) {
			for (final DrawingListener listener : list.getListeners(DrawingListener.class)) {
				listener.onDrawingEnd();
			}
		}
	}

	/** Listener on drawing actions.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public interface DrawingListener extends EventListener {

		/** Drawing is starting.
		 */
		void onDrawingStart();

		/** Drawing is finished.
		 */
		void onDrawingEnd();

	}

}
