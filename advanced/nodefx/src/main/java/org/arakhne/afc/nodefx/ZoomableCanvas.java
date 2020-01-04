/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import org.arakhne.afc.math.geometry.d2.afp.BoundedElement2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.InformedIterable;
import org.arakhne.afc.util.ListenerCollection;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Resizeable canvas for rendering a document and that support zoom in and out.
 *
 * <p>The document elements are displayed within this {@code ZoomableCanvas} by the {@link Drawer drawers}
 * that are declared as services.
 *
 * <p>The {@code ZoomableCanvas} provides a tool for displaying document elements. It does not provide
 * advanced UI components (scroll bars, etc.) and interaction means (mouse support, etc.).
 *
 * @param <T> the type of the document.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("checkstyle:methodcount")
public class ZoomableCanvas<T extends InformedIterable<?> & BoundedElement2afp<?>>
		extends Canvas implements ZoomableViewer<T> {

	private static final boolean DEFAULT_INVERTED_X_AXIS = false;

	private static final boolean DEFAULT_INVERTED_Y_AXIS = false;

	private ListenerCollection<EventListener> listeners;

	private ZoomableGraphicsContext documentGraphicsContext;

	private ObjectProperty<T> model;

	private ObjectProperty<Drawer<? super T>> drawer;

	private BooleanProperty invertXAxis;

	private BooleanProperty invertYAxis;

	private DoubleProperty viewportX;

	private DoubleProperty viewportY;

	private DoubleProperty scaleValue;

	private DoubleProperty minScaleValue;

	private DoubleProperty maxScaleValue;

	private DoubleProperty delta;

	private IntegerProperty budget;

	private ReadOnlyObjectWrapper<Rectangle2afp<?, ?, ?, ?, ?, ?>> viewportBounds;

	private ReadOnlyObjectWrapper<Rectangle2afp<?, ?, ?, ?, ?, ?>> documentBounds;

	private final AtomicBoolean renderingEnable = new AtomicBoolean(false);

	private volatile Runnable drawRunAfterChain;

	private volatile Runnable refresher;

	private ChangeListener<? super Number> initializationListener = (it, oldValue, newValue) -> {
		if (getWidth() > 0. && getHeight() > 0) {
			final ChangeListener<? super Number> li = ZoomableCanvas.this.initializationListener;
			ZoomableCanvas.this.initializationListener = null;
			if (li != null) {
				widthProperty().removeListener(li);
				heightProperty().removeListener(li);
			}
			setRenderingEnable(true);
			drawContent();
		}
	};

	/** Constructor. The renderer is detected with the type replied by
	 * {@link InformedIterable#getElementType()} on the model.
	 *
	 * @param model the source of the elements.
	 */
	public ZoomableCanvas(T model) {
		this(model, null);
	}

	/** Constructor. The renderer is detected with the type replied by
	 * {@link InformedIterable#getElementType()} on the model.
	 *
	 * @since 16.0
	 */
	public ZoomableCanvas() {
		endConstructor();
	}

	/** Constructor.
	 *
	 * @param model the source of the elements.
	 * @param drawer the drawer.
	 */
	public ZoomableCanvas(T model, Drawer<? super T> drawer) {
		assert model != null : AssertMessages.notNullParameter(0);
		documentModelProperty().set(model);
		if (drawer != null) {
			documentDrawerProperty().set(drawer);
		}
		endConstructor();
	}

	private void endConstructor() {
		// Add listener on initialization
		widthProperty().addListener(this.initializationListener);
		heightProperty().addListener(this.initializationListener);
		// 1) Create the viewport bound property
		viewportBoundsProperty();
		// 2) Listen the created property for refreshing the canvas.
		this.viewportBounds.addListener(new ChangeListener<Rectangle2afp<?, ?, ?, ?, ?, ?>>() {
			@Override
			public void changed(ObservableValue<? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> observable,
					Rectangle2afp<?, ?, ?, ?, ?, ?> oldValue,
					Rectangle2afp<?, ?, ?, ?, ?, ?> newValue) {
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
		return getDocumentBounds().getWidth() * getScaleValue();
	}

	@Override
	public double prefHeight(double width) {
		return getDocumentBounds().getHeight() * getScaleValue();
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

	/** Replies the document graphics context.
	 *
	 * @return the graphics context.
	 */
	public ZoomableGraphicsContext getDocumentGraphicsContext2D() {
		if (this.documentGraphicsContext == null) {
			final CenteringTransform transform = new CenteringTransform(
					invertedAxisXProperty(),
					invertedAxisYProperty(),
					viewportBoundsProperty());
			this.documentGraphicsContext = new ZoomableGraphicsContext(
					getGraphicsContext2D(),
					scaleValueProperty(),
					documentBoundsProperty(),
					viewportBoundsProperty(),
					widthProperty(),
					heightProperty(),
					drawableElementBudgetProperty(),
					transform);
		}
		return this.documentGraphicsContext;
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

	/** Replies the rendering state of the canvas.
	 *
	 * <p>If the replied value is evaluated to {@code true}, the {@link #drawContent()}
	 * function does nothing.
	 *
	 * @return {@code true} if the drawing function is active; {@code false} if it is inactive.
	 * @since 15.0
	 */
	public boolean isRenderingEnable() {
		return this.renderingEnable.get();
	}

	/** Change the rendering state of the canvas.
	 *
	 * <p>If the replied value is evaluated to {@code true}, the {@link #drawContent()}
	 * function does nothing.
	 *
	 * @param newState {@code true} if the drawing function is active; {@code false} if it is inactive.
	 * @return the value of the state before the change.
	 * @since 15.0
	 */
	protected boolean setRenderingEnable(boolean newState) {
		return this.renderingEnable.getAndSet(newState);
	}

	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:nestedifdepth"})
	@Override
	public final void drawContent() {
		final boolean old = setRenderingEnable(false);
		if (old) {
			if (this.refresher == null) {
				if (this.drawRunAfterChain == null) {
					this.drawRunAfterChain = buildRunAfterChain(
						() -> fireDrawingStart(),
						() -> {
							final T model = getDocumentModel();
							if (model != null) {
								final GraphicsContext gc = getGraphicsContext2D();
								gc.clearRect(0, 0, getWidth(), getHeight());

								final Drawer<? super T> drawer = getDocumentDrawer();
								if (drawer != null) {
									final ZoomableGraphicsContext docgc = getDocumentGraphicsContext2D();
									docgc.prepareRendering();

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

									drawer.draw(docgc, model);

									/*final double fifteenPixels = docgc.fx2docSize(15);
									docgc.setStroke(Color.RED);
									docgc.setLineWidthInPixels(5);
									docgc.beginPath();
									docgc.moveTo(viewport.getMinX() + fifteenPixels,
										viewport.getMinY() + fifteenPixels);
									docgc.lineTo(viewport.getMaxX() - fifteenPixels,
										viewport.getMinY() + fifteenPixels);
									docgc.lineTo(viewport.getMaxX() - fifteenPixels,
										viewport.getMaxY() - fifteenPixels);
									docgc.lineTo(viewport.getMinX() + fifteenPixels,
										viewport.getMaxY() - fifteenPixels);
									docgc.closePath();
									docgc.stroke();*/
								}
							}
						},
						() -> {
							fireDrawingEnd();
							setRenderingEnable(old);
						});
				}
				this.refresher = this.drawRunAfterChain;
				Platform.runLater(() -> {
					try {
						if (this.refresher != null) {
							this.refresher.run();
						}
					} finally {
						this.refresher = null;
					}
				});
			} else {
				setRenderingEnable(true);
			}
		}
	}

	@Override
	public ObjectProperty<T> documentModelProperty() {
		if (this.model == null) {
			this.model = new SimpleObjectProperty<>(this, DOCUMENT_MODEL_PROPERTY) {
				@Override
				protected void invalidated() {
					assert get() != null;
				}
			};
			final ChangeListener<? super T> listener = new ChangeListener<>() {
				@Override
				public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
					if (oldValue != null) {
						unbindModel(oldValue);
					}
					if (newValue != null) {
						bindModel(newValue);
					}
				}
			};
			this.model.addListener(listener);
		}
		return this.model;
	}

	@Override
	public final T getDocumentModel() {
		return documentModelProperty().get();
	}

	@Override
	public final void setDocumentModel(T model) {
		documentModelProperty().set(model);
	}

	/** Invoked when the given model is binded to this canvas.
	 *
	 * <p>This function is defined in order to be overridden by sub-classes for
	 * adding observers on the model.
	 *
	 * @param model the model that is binded to this canvas.
	 * @since 16.0
	 */
	protected void bindModel(T model) {
		//
	}

	/** Invoked when the given model is unbinded from this canvas.
	 *
	 * <p>This function is defined in order to be overridden by sub-classes for
	 * removing observers on the model.
	 *
	 * @param model the model that is unbinded to this canvas.
	 * @since 16.0
	 */
	protected void unbindModel(T model) {
		//
	}

	@Override
	public ObjectProperty<Drawer<? super T>> documentDrawerProperty() {
		if (this.drawer == null) {
			final Drawer<? super T> defaultDrawer = Drawers.getDrawerFor(getDocumentModel());
			this.drawer = new SimpleObjectProperty<>(this, DOCUMENT_DRAWER_PROPERTY, defaultDrawer) {
				@Override
				protected void invalidated() {
					if (get() == null) {
						final Drawer<? super T> defaultDrawer = Drawers.getDrawerFor(getDocumentModel());
						set(defaultDrawer);
					}
				}
			};
		}
		return this.drawer;
	}

	@Override
	public final Drawer<? super T> getDocumentDrawer() {
		return documentDrawerProperty().get();
	}

	@Override
	public final void setDocumentDrawer(Drawer<? super T> drawer) {
		documentDrawerProperty().set(drawer);
	}

	@Override
	public ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> documentBoundsProperty() {
		if (this.documentBounds == null) {
			this.documentBounds = new ReadOnlyObjectWrapper<>(this, DOCUMENT_BOUNDS_PROPERTY);
			this.documentBounds.bind(Bindings.createObjectBinding(
					() ->  getDocumentModel().getBoundingBox(),
					documentModelProperty()));
		}
		return this.documentBounds.getReadOnlyProperty();
	}

	@Override
	public final Rectangle2afp<?, ?, ?, ?, ?, ?> getDocumentBounds() {
		return documentBoundsProperty().get();
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
		if (this.scaleValue != null || value != 1.) {
			scaleValueProperty().set(value);
		}
	}

	@Override
	public final void setScaleValue(double scaleValue, double centerX, double centerY) {
		final boolean old = setRenderingEnable(false);
		setScaleValue(scaleValue);
		setViewportCenterX(centerX);
		setViewportCenterY(centerY);
		setRenderingEnable(old);
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
		if (this.minScaleValue != null || value != MINIMUM_SCALE_VALUE) {
			minScaleValueProperty().set(value);
		}
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
		if (this.maxScaleValue != null || value != MAXIMUM_SCALE_VALUE) {
			maxScaleValueProperty().set(value);
		}
	}

	@Override
	public DoubleProperty viewportCenterXProperty() {
		if (this.viewportX == null) {
			this.viewportX = new SimpleDoubleProperty(this, VIEWPORT_CENTER_X_PROPERTY, getDocumentBounds().getCenterX());
		}
		return this.viewportX;
	}

	@Override
	public final double getViewportCenterX() {
		return this.viewportX == null ? getDocumentBounds().getCenterX() : this.viewportX.get();
	}

	@Override
	public final void setViewportCenterX(double x) {
		viewportCenterXProperty().set(x);
	}

	@Override
	public DoubleProperty viewportCenterYProperty() {
		if (this.viewportY == null) {
			this.viewportY = new SimpleDoubleProperty(this, VIEWPORT_CENTER_Y_PROPERTY, getDocumentBounds().getCenterY());
		}
		return this.viewportY;
	}

	@Override
	public final double getViewportCenterY() {
		return this.viewportY == null ? getDocumentBounds().getCenterY() : this.viewportY.get();
	}

	@Override
	public final void setViewportCenterY(double y) {
		viewportCenterYProperty().set(y);
	}

	@Override
	public final void setViewportCenter(double x, double y) {
		final boolean old = setRenderingEnable(false);
		setViewportCenterX(x);
		setViewportCenterY(y);
		setRenderingEnable(old);
		drawContent();
	}

	@Override
	public ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> viewportBoundsProperty() {
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
	public final Rectangle2afp<?, ?, ?, ?, ?, ?> getViewportBounds() {
		return viewportBoundsProperty().get();
	}

	@Override
	public double getScaleValueToFit(boolean minimizeOnly) {
		final Rectangle2afp<?, ?, ?, ?, ?, ?> document = getDocumentBounds();
		final double contentWidth = document.getWidth();
		final double contentHeight = document.getHeight();

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
		if (this.delta != null || change != DEFAULT_SCALE_CHANGE) {
			scaleChangeProperty().set(change);
		}
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
		if (this.invertXAxis != null || inverted != DEFAULT_INVERTED_X_AXIS) {
			invertedAxisXProperty().set(inverted);
		}
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
		if (this.invertYAxis != null || inverted != DEFAULT_INVERTED_Y_AXIS) {
			invertedAxisYProperty().set(inverted);
		}
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
		if (this.budget != null || budget != DEFAULT_DRAWABLE_ELEMENT_BUDGET) {
			drawableElementBudgetProperty().set(budget);
		}
	}

	@Override
	public void addDrawingListener(DrawingListener listener) {
		if (this.listeners == null) {
			this.listeners = new ListenerCollection<>();
		}
		this.listeners.add(DrawingListener.class, listener);
	}

	@Override
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

	@Override
	public double toDocumentPositionX(double x) {
		return getDocumentGraphicsContext2D().fx2docX(x);
	}

	@Override
	public double toDocumentPositionY(double y) {
		return getDocumentGraphicsContext2D().fx2docY(y);
	}

	@Override
	public double toDocumentSize(double size) {
		return getDocumentGraphicsContext2D().fx2docSize(size);
	}

	@Override
	public double toScreenPositionX(double x) {
		return getDocumentGraphicsContext2D().doc2fxX(x);
	}

	@Override
	public double toScreenPositionY(double y) {
		return getDocumentGraphicsContext2D().doc2fxY(y);
	}

	@Override
	public double toScreenSize(double size) {
		return getDocumentGraphicsContext2D().doc2fxSize(size);
	}

}
