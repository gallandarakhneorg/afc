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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.util.Utils;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.afp.BoundedElement2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.util.InformedIterable;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * Panel that is displaying the document elements and supporting zooming.
 *
 * @param <T> the type of the document elements to display.
 * @param <DT> the type of the document.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@SuppressWarnings("checkstyle:methodcount")
public class ZoomablePane<T, DT extends InformedIterable<? super T> & BoundedElement2afp<?>> extends BorderPane
		implements ZoomableViewer<T, DT> {

	/**
	 * A static final reference to whether the platform we are on supports touch.
	 */
	protected static final boolean IS_TOUCH_SUPPORTED = Platform.isSupported(ConditionalFeature.INPUT_TOUCH);

	private static final double NO_TURN = 0.;

	private static final double TURN = 180.;

	private static final boolean DEFAULT_PANNABLE = true;

	private static final double VERY_LARGE_MOVE_FACTOR = 100.;

	private static final double LARGE_MOVE_FACTOR = 10.;

	private static final double STANDARD_MOVE_FACTOR = 1.;

	private static final double PAN_THRESHOLD = 10.;

	private static final MouseButton DEFAULT_PAN_BUTTON = MouseButton.PRIMARY;

	private static final double DEFAULT_PAN_SENSITIVITY = 1.;

	private static final double MIN_PAN_SENSITIVITY = 0.01;

	private static final String PANNABLE_PROPERTY = "pannable"; //$NON-NLS-1$

	private static final String PAN_BUTTON_PROPERTY = "panButton"; //$NON-NLS-1$

	private static final String PAN_SENSITIVITY_PROPERTY = "panSensitivity"; //$NON-NLS-1$

	private static final String LOGGER_PROPERTY = "logger"; //$NON-NLS-1$

	/**
	 * Initialize the style class to 'zoomable-view'.
	 *
	 * <p>This is the selector class from which CSS can be used to style
	 * this control.
	 */
	private static final String DEFAULT_STYLE_CLASS = "zoomable-view"; //$NON-NLS-1$

	/** Specifies whether the user should be able to pan the viewport by using
	 * the mouse. If mouse events reach the {@code ZoomablePane} (that is, if mouse
	 * events are not blocked by the contained node or one of its children)
	 * then {@link #pannableProperty() pannable} is consulted to determine if the events should be
	 * used for panning.
	 */
	private BooleanProperty pannable;

	/** The button that serves for starting the panning with mouse.
	 */
	private ObjectProperty<MouseButton> panButton;

	/** The sensitivity of the panning moves. The sensitivity is a strictly positive number
	 * that multiplied by the distance covered by the mouse for obtaining the amount of move
	 * to apply to the document.
	 * By default, the sensitivity is 1.
	 */
	private DoubleProperty panSensitivity;

	private ObjectProperty<Logger> logger;

	private final ZoomableCanvas<T, DT> documentCanvas;

	private final ScrollBar vbar;

	private final ScrollBar hbar;

	private final ColorSquare corner;

	private double pressX;

	private double pressY;

	private double hbarValue;

	private double vbarValue;

	private boolean dragDetected;

	private Cursor savedCursor;

	private boolean scrollDetected;

	/** Constructor with the model to be displayed.
	 *
	 * @param model the source of the elements.
	 */
	public ZoomablePane(DT model) {
		this(new ZoomableCanvas<>(model));
	}

	/** Constructor with the canvas.
	 *
	 * @param canvas the pre-created canvas with the model to display inside.
	 */
	@SuppressWarnings({"unchecked", "checkstyle:methodlength", "checkstyle:executablestatementcount"})
	public ZoomablePane(ZoomableCanvas<T, DT> canvas) {
		assert canvas != null : AssertMessages.notNullParameter();
		this.documentCanvas = canvas;
		//
		getStyleClass().setAll(DEFAULT_STYLE_CLASS);
		setAccessibleRole(AccessibleRole.SCROLL_PANE);
		// focusTraversable is styleable through css. Calling setFocusTraversable
		// makes it look to css like the user set the value and css will not
		// override. Initializing focusTraversable by calling applyStyle with
		// null StyleOrigin ensures that css will be able to override the value.
		((StyleableProperty<Boolean>) focusTraversableProperty()).applyStyle(null, Boolean.TRUE);
		//
		this.vbar = new ScrollBar();
		this.vbar.setOrientation(Orientation.VERTICAL);
		this.hbar = new ScrollBar();
		this.hbar.setOrientation(Orientation.HORIZONTAL);
		this.corner = new ColorSquare();
		this.corner.minWidthProperty().bind(this.vbar.widthProperty());
		this.corner.maxWidthProperty().bind(this.vbar.widthProperty());
		this.corner.minHeightProperty().bind(this.hbar.prefHeightProperty());
		this.corner.maxHeightProperty().bind(this.hbar.prefHeightProperty());
		final BorderPane bottomGroup = new BorderPane();
		bottomGroup.setCenter(this.hbar);
		bottomGroup.setRight(this.corner);
		setCenter(getDocumentCanvas());
		setRight(this.vbar);
		setBottom(bottomGroup);
		// Bind the scroll bars properties
		this.vbar.minProperty().bind(Bindings.createDoubleBinding(
				() -> getDocumentBounds().getMinY(),
				documentBoundsProperty()));
		this.vbar.maxProperty().bind(Bindings.createDoubleBinding(
				() -> getDocumentBounds().getMaxY(),
				documentBoundsProperty()));
		this.vbar.visibleAmountProperty().bind(Bindings.createDoubleBinding(
				() -> getViewportBounds().getHeight(),
				viewportBoundsProperty()));
		this.vbar.valueProperty().bindBidirectional(viewportCenterYProperty());
		if (isInvertedAxisY()) {
			this.vbar.setRotate(TURN);
		}
		getDocumentCanvas().invertedAxisYProperty().addListener(new ChangeListener<Boolean>() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue != null && newValue.booleanValue()) {
					ZoomablePane.this.vbar.setRotate(TURN);
				} else {
					ZoomablePane.this.vbar.setRotate(NO_TURN);
				}
			}
		});
		//
		this.hbar.minProperty().bind(Bindings.createDoubleBinding(
				() -> getDocumentBounds().getMinX(),
				documentBoundsProperty()));
		this.hbar.maxProperty().bind(Bindings.createDoubleBinding(
				() -> getDocumentBounds().getMaxX(),
				documentBoundsProperty()));
		this.hbar.visibleAmountProperty().bind(Bindings.createDoubleBinding(
				() -> getViewportBounds().getWidth(),
				viewportBoundsProperty()));
		this.hbar.valueProperty().bindBidirectional(viewportCenterXProperty());
		if (isInvertedAxisX()) {
			this.hbar.setRotate(TURN);
		}
		getDocumentCanvas().invertedAxisXProperty().addListener(new ChangeListener<Boolean>() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue != null && newValue.booleanValue()) {
					ZoomablePane.this.hbar.setRotate(TURN);
				} else {
					ZoomablePane.this.hbar.setRotate(NO_TURN);
				}
			}
		});

		// don't allow the ScrollBar to handle the ScrollEvent,
		// In a view pane a vertical scroll should scroll on the vertical only,
		// whereas in a horizontal ScrollBar it can scroll horizontally.

		// block the event from being passed down to children
		final EventDispatcher blockEventDispatcher = (event, tail) -> event;
		// block ScrollEvent from being passed down to scrollbar's skin
		final EventDispatcher oldHsbEventDispatcher = this.hbar.getEventDispatcher();
		this.hbar.setEventDispatcher((event, tail) -> {
			if (event.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent) event).isDirect()) {
				EventDispatchChain tail0 = tail.prepend(blockEventDispatcher);
				tail0 = tail0.prepend(oldHsbEventDispatcher);
				return tail0.dispatchEvent(event);
			}
			return oldHsbEventDispatcher.dispatchEvent(event, tail);
		});
		// block ScrollEvent from being passed down to scrollbar's skin
		final EventDispatcher oldVsbEventDispatcher = this.vbar.getEventDispatcher();
		this.vbar.setEventDispatcher((event, tail) -> {
			if (event.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent) event).isDirect()) {
				EventDispatchChain tail0 = tail.prepend(blockEventDispatcher);
				tail0 = tail0.prepend(oldVsbEventDispatcher);
				return tail0.dispatchEvent(event);
			}
			return oldVsbEventDispatcher.dispatchEvent(event, tail);
		});
		//
		setupKeying();
		setupMousing();
		setupListeners();
	}

	/** Replies the CssMetaData associated with this class, which may include the
	 * CssMetaData of its super classes.
	 *
	 * @return the metadata.
	 */
	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return StyleableProperties.STYLEABLES;
	}

	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		return getClassCssMetaData();
	}

	/** Setup the response based on listeners.
	 */
	protected void setupListeners() {
		getDocumentCanvas().addDrawingListener(new ZoomableCanvas.DrawingListener() {
			private long time;

			@Override
			public void onDrawingStart() {
				this.time = System.currentTimeMillis();
				getCorner().setColor(Color.ORANGERED);
			}

			@Override
			public void onDrawingEnd() {
				getCorner().setColor(null);
				final long duration = System.currentTimeMillis() - this.time;
				getLogger().fine("Rendering duration: " + Duration.millis(duration).toString()); //$NON-NLS-1$
			}
		});
	}

	/** Setup the response of the pane to mouse events.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:nestedifdepth"})
	protected void setupMousing() {
		final ZoomableCanvas<T, DT> canvas = getDocumentCanvas();
		canvas.setOnMousePressed(e -> {
			this.pressX = e.getX();
			this.pressY = e.getY();
			this.hbarValue = this.hbar.getValue();
			this.vbarValue = this.vbar.getValue();
		});

		canvas.setOnDragDetected(e -> {
			if (isPannable() && e.getButton() == getPanButton()) {
				this.dragDetected = true;
				if (this.savedCursor == null) {
					final ZoomableCanvas<T, DT> canvas0 = getDocumentCanvas();
					this.savedCursor = canvas0.getCursor();
					if (this.savedCursor == null) {
						this.savedCursor = Cursor.DEFAULT;
					}
					canvas0.setCursor(Cursor.MOVE);
					requestLayout();
				}
			}
		});

		canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
			if (this.dragDetected) {
				this.dragDetected = false;
				final Cursor scurs = this.savedCursor;
				this.savedCursor = null;
				if (scurs != null) {
					getDocumentCanvas().setCursor(scurs);
					requestLayout();
				}
			}
		});

		canvas.setOnDragDone(event -> {
			if (this.dragDetected) {
				this.dragDetected = false;
				final Cursor scurs = this.savedCursor;
				this.savedCursor = null;
				if (scurs != null) {
					getDocumentCanvas().setCursor(scurs);
					requestLayout();
				}
			}
		});

		canvas.setOnMouseDragged(e -> {
			// for mobile-touch we allow drag, even if not pannable
			if (this.dragDetected || IS_TOUCH_SUPPORTED) {
				if (!this.dragDetected) {
					this.dragDetected = true;
					if (this.savedCursor == null) {
						final ZoomableCanvas<T, DT> canvas0 = getDocumentCanvas();
						this.savedCursor = canvas0.getCursor();
						if (this.savedCursor == null) {
							this.savedCursor = Cursor.DEFAULT;
						}
						canvas0.setCursor(Cursor.MOVE);
						requestLayout();
					}
				}
				final double sensitivity = getPanSensitivity(e.isShiftDown(), e.isControlDown());
				// we only drag if not all of the content is visible.
				if (this.hbar.getVisibleAmount() > 0. && this.hbar.getVisibleAmount() < this.hbar.getMax()) {
					double deltaX = this.pressX - e.getX();
					if (isInvertedAxisX()) {
						deltaX = -deltaX;
					}
					if (Math.abs(deltaX) > PAN_THRESHOLD) {
						final double delta = getDocumentCanvas().getDocumentGraphicsContext2D().fx2docSize(deltaX) * sensitivity;
						double newHVal = this.hbarValue + delta;
						newHVal = Utils.clamp(this.hbar.getMin(), newHVal, this.hbar.getMax());
						this.hbar.setValue(newHVal);
					}
				}
				// we only drag if not all of the content is visible.
				if (this.vbar.getVisibleAmount() > 0. && this.vbar.getVisibleAmount() < this.vbar.getMax()) {
					double deltaY = this.pressY - e.getY();
					if (isInvertedAxisY()) {
						deltaY = -deltaY;
					}
					if (Math.abs(deltaY) >= PAN_THRESHOLD) {
						final double delta = getDocumentCanvas().getDocumentGraphicsContext2D().fx2docSize(deltaY) * sensitivity;
						double newVVal = this.vbarValue + delta;
						newVVal = Utils.clamp(this.vbar.getMin(), newVVal, this.vbar.getMax());
						this.vbar.setValue(newVVal);
					}
				}
				// we need to consume drag events, as we don't want the view pane itself to be dragged on every mouse click
				e.consume();
			}
		});

		setOnScrollStarted(event -> {
			this.scrollDetected = true;
		});

		setOnScrollFinished(event -> {
			this.scrollDetected = false;
		});

		setOnScroll(event -> {
			if (!this.scrollDetected) {
				event.consume();
				final double delta;
				if (event.getDeltaY() != 0.) {
					delta = event.getDeltaY();
				} else {
					delta = event.getDeltaX();
				}
				if (delta < 0) {
					zoomOut();
				} else {
					zoomIn();
				}
			}
		});
	}

	/** Setup the response of the pane to key events.
	 */
	protected void setupKeying() {
		setOnKeyPressed(event -> {
			switch (event.getCode()) {
			case LEFT:
				moveLeft(event.isShiftDown(), event.isControlDown(), false);
				event.consume();
				break;
			case RIGHT:
				moveRight(event.isShiftDown(), event.isControlDown(), false);
				event.consume();
				break;
			case UP:
				if (event.isAltDown()) {
					zoomIn();
				} else {
					moveUp(event.isShiftDown(), event.isControlDown(), false);
				}
				event.consume();
				break;
			case DOWN:
				if (event.isAltDown()) {
					zoomOut();
				} else {
					moveDown(event.isShiftDown(), event.isControlDown(), false);
				}
				event.consume();
				break;
			case PAGE_UP:
				if (event.isControlDown()) {
					moveLeft(false, false, true);
				} else {
					moveUp(false, false, true);
				}
				event.consume();
				break;
			case PAGE_DOWN:
				if (event.isControlDown()) {
					moveRight(false, false, true);
				} else {
					moveDown(false, false, true);
				}
				event.consume();
				break;
				//$CASES-OMITTED$
			default:
			}
		});
	}

	/** Move the viewport left.
	 *
	 * @param isUnit indicates if the move is a unit move. If {@code true}, this argument has precedence to the other arguments.
	 * @param isLarge indicates if the move is a large move. If {@code true}, this argument has precedence
	 *     to the very large argument.
	 * @param isVeryLarge indicates if the move is a very large move.
	 */
	public void moveLeft(boolean isUnit, boolean isLarge, boolean isVeryLarge) {
		double inc = isUnit ? this.hbar.getUnitIncrement()
				: (isLarge ? LARGE_MOVE_FACTOR
						: (isVeryLarge ? VERY_LARGE_MOVE_FACTOR : STANDARD_MOVE_FACTOR)) * this.hbar.getBlockIncrement();
		if (!isInvertedAxisX()) {
			inc = -inc;
		}
		this.hbar.setValue(Utils.clamp(this.hbar.getMin(), this.hbar.getValue() + inc, this.hbar.getMax()));
	}

	/** Move the viewport right.
	 *
	 * @param isUnit indicates if the move is a unit move. If {@code true}, this argument has precedence to the other arguments.
	 * @param isLarge indicates if the move is a large move. If {@code true}, this argument has precedence
	 *     to the very large argument.
	 * @param isVeryLarge indicates if the move is a very large move.
	 */
	public void moveRight(boolean isUnit, boolean isLarge, boolean isVeryLarge) {
		double inc = isUnit ? this.hbar.getUnitIncrement()
				: (isLarge ? LARGE_MOVE_FACTOR
						: (isVeryLarge ? VERY_LARGE_MOVE_FACTOR : STANDARD_MOVE_FACTOR)) * this.hbar.getBlockIncrement();
		if (isInvertedAxisX()) {
			inc = -inc;
		}
		this.hbar.setValue(Utils.clamp(this.hbar.getMin(), this.hbar.getValue() + inc, this.hbar.getMax()));
	}

	/** Move the viewport up.
	 *
	 * @param isUnit indicates if the move is a unit move. If {@code true}, this argument has precedence to the other arguments.
	 * @param isLarge indicates if the move is a large move. If {@code true}, this argument has precedence
	 *     to the very large argument.
	 * @param isVeryLarge indicates if the move is a very large move.
	 */
	public void moveUp(boolean isUnit, boolean isLarge, boolean isVeryLarge) {
		double inc = isUnit ? this.vbar.getUnitIncrement()
				: (isLarge ? LARGE_MOVE_FACTOR
						: (isVeryLarge ? VERY_LARGE_MOVE_FACTOR : STANDARD_MOVE_FACTOR)) * this.vbar.getBlockIncrement();
		if (!isInvertedAxisY()) {
			inc = -inc;
		}
		this.vbar.setValue(Utils.clamp(this.vbar.getMin(), this.vbar.getValue() + inc, this.vbar.getMax()));
	}

	/** Move the viewport down.
	 *
	 * @param isUnit indicates if the move is a unit move. If {@code true}, this argument has precedence to the other arguments.
	 * @param isLarge indicates if the move is a large move. If {@code true}, this argument has precedence
	 *     to the very large argument.
	 * @param isVeryLarge indicates if the move is a very large move.
	 */
	public void moveDown(boolean isUnit, boolean isLarge, boolean isVeryLarge) {
		double inc = isUnit ? this.vbar.getUnitIncrement()
				: (isLarge ? LARGE_MOVE_FACTOR
						: (isVeryLarge ? VERY_LARGE_MOVE_FACTOR : STANDARD_MOVE_FACTOR)) * this.vbar.getBlockIncrement();
		if (isInvertedAxisY()) {
			inc = -inc;
		}
		this.vbar.setValue(Utils.clamp(this.vbar.getMin(), this.vbar.getValue() + inc, this.vbar.getMax()));
	}

	/** Replies the property that contains the logger.
	 *
	 * @return the logger.
	 */
	public ObjectProperty<Logger> loggerProperty() {
		if (this.logger == null) {
			this.logger = new SimpleObjectProperty<Logger>(this, LOGGER_PROPERTY, Logger.getLogger(getClass().getName())) {
				@Override
				protected void invalidated() {
					final Logger log = get();
					if (log == null) {
						set(Logger.getLogger(getClass().getName()));
					}
				}
			};
		}
		return this.logger;
	}

	/** Replies the logger associated to  this pane.
	 *
	 * @return the logger.
	 */
	public Logger getLogger() {
		return loggerProperty().get();
	}

	/** Change the logger associated to  this pane.
	 *
	 * @param logger the logger.
	 */
	public void setLogger(Logger logger) {
		loggerProperty().set(logger);
	}

	/** Replies the property that indicates if the user could be able to pan the viewport by using
	 * the mouse. If mouse events reach the {@code ZoomablePane} (that is, if mouse
	 * events are not blocked by the contained node or one of its children)
	 * then {@link #pannableProperty() pannable} is consulted to determine if the events should be
	 * used for panning.
	 *
	 * @return the property.
	 */
	public BooleanProperty pannableProperty() {
		if (this.pannable == null) {
			this.pannable = new StyleableBooleanProperty(DEFAULT_PANNABLE) {
				@Override
				public void invalidated() {
					pseudoClassStateChanged(StyleableProperties.PANNABLE_PSEUDOCLASS_STATE, get());
				}

				@Override
				public CssMetaData<ZoomablePane<?, ?>, Boolean> getCssMetaData() {
					return StyleableProperties.PANNABLE;
				}

				@Override
				public Object getBean() {
					return ZoomablePane.this;
				}

				@Override
				public String getName() {
					return PANNABLE_PROPERTY;
				}
			};
		}
		return this.pannable;
	}

	/** Change the property that indicates if the user could be able to pan the viewport by using
	 * the mouse. If mouse events reach the {@code ZoomablePane} (that is, if mouse
	 * events are not blocked by the contained node or one of its children)
	 * then {@link #pannableProperty() pannable} is consulted to determine if the events should be
	 * used for panning.
	 *
	 * @param value {@code true} to enable the panning, {@code false} to disable the panning.
	 */
	public final void setPannable(boolean value) {
		pannableProperty().set(value);
	}

	/** Replies the flag that indicates if the user could be able to pan the viewport by using
	 * the mouse. If mouse events reach the {@code ZoomablePane} (that is, if mouse
	 * events are not blocked by the contained node or one of its children)
	 * then {@link #pannableProperty() pannable} is consulted to determine if the events should be
	 * used for panning.
	 *
	 * @return {@code true} to enable the panning, {@code false} to disable the panning.
	 */
	public final boolean isPannable() {
		return this.pannable == null ? DEFAULT_PANNABLE : this.pannable.get();
	}

	/** Replies the property for the button that serves for starting the mouse scrolling.
	 *
	 * @return the property.
	 */
	public ObjectProperty<MouseButton> panButtonProperty() {
		if (this.panButton == null) {
			this.panButton = new StyleableObjectProperty<MouseButton>(DEFAULT_PAN_BUTTON) {
				@SuppressWarnings("synthetic-access")
				@Override
				protected void invalidated() {
					final MouseButton button = get();
					if (button == null) {
						set(DEFAULT_PAN_BUTTON);
					}
				}

				@Override
				public CssMetaData<ZoomablePane<?, ?>, MouseButton> getCssMetaData() {
					return StyleableProperties.PAN_BUTTON;
				}

				@Override
				public Object getBean() {
					return ZoomablePane.this;
				}

				@Override
				public String getName() {
					return PAN_BUTTON_PROPERTY;
				}
			};
		}
		return this.panButton;
	}

	/** Replies the button that serves for starting the mouse scrolling.
	 *
	 * @return the mouse button that permits to start the panning.
	 */
	public final MouseButton getPanButton() {
		return this.panButton == null ? DEFAULT_PAN_BUTTON : this.panButton.get();
	}

	/** Change the button that serves for starting the mouse scrolling.
	 *
	 * @param button the mouse button that permits to start the panning.
	 */
	public final void setPanButton(MouseButton button) {
		panButtonProperty().set(button);
	}

	/** Replies the property that indicates the sensibility of the panning moves.
	 * The sensibility is a strictly positive number that is multiplied to the
	 * distance covered by the mouse motion for obtaining the move to
	 * apply to the document.
	 * The default value is 1.
	 *
	 * @return the property.
	 */
	public DoubleProperty panSensitivityProperty() {
		if (this.panSensitivity == null) {
			this.panSensitivity = new StyleableDoubleProperty(DEFAULT_PAN_SENSITIVITY) {
				@Override
				public void invalidated() {
					if (get() <= MIN_PAN_SENSITIVITY) {
						set(MIN_PAN_SENSITIVITY);
					}
				}

				@Override
				public CssMetaData<ZoomablePane<?, ?>, Number> getCssMetaData() {
					return StyleableProperties.PAN_SENSITIVITY;
				}

				@Override
				public Object getBean() {
					return ZoomablePane.this;
				}

				@Override
				public String getName() {
					return PAN_SENSITIVITY_PROPERTY;
				}
			};
		}
		return this.panSensitivity;
	}

	/** Change the sensibility of the panning moves.
	 * The sensibility is a strictly positive number that is multiplied to the
	 * distance covered by the mouse motion for obtaining the move to
	 * apply to the document.
	 * The default value is 1.
	 *
	 * @param value the sensitivity.
	 */
	public final void setPanSensitivity(double value) {
		panSensitivityProperty().set(value);
	}

	/** Replies the sensibility of the panning moves.
	 * The sensibility is a strictly positive number that is multiplied to the
	 * distance covered by the mouse motion for obtaining the move to
	 * apply to the document.
	 * The default value is 1.
	 *
	 * @return the sensitivity.
	 */
	public final double getPanSensitivity() {
		return this.panSensitivity == null ? DEFAULT_PAN_SENSITIVITY : this.panSensitivity.get();
	}

	/** Replies the sensibility of the panning moves after applying dynamic user interaction modifiers.
	 * The sensibility is a strictly positive number that is multiplied to the
	 * distance covered by the mouse motion for obtaining the move to
	 * apply to the document.
	 * The default value is 1.
	 *
	 * <p>This function is usually used for computing the sensibility within mouse handlers.
	 * The Shift and Control key flags may be used as the modifiers.
	 *
	 * <p>If {@code unitSensitivityModifier} is {@code true}, the sensibility is always {@code 1}.
	 * If {@code hugeSensivityModifier} is {@code true}, the sensibility is multiplied by {@link #LARGE_MOVE_FACTOR}.
	 * Otherwise, the value returned by {@link #getPanSensitivity()} is returned.
	 *
	 * @param unitSensitivityModifier the user chooses the unit sensitivity dynamically. If {@code true}, this
	 *     parameter has precedence to the other parameters.
	 * @param hugeSensivityModifier the user chooses a huge sensitivity dynamically.
	 * @return the sensitivity.
	 */
	public double getPanSensitivity(boolean unitSensitivityModifier, boolean hugeSensivityModifier) {
		if (unitSensitivityModifier) {
			return DEFAULT_PAN_SENSITIVITY;
		}
		final double sens = getPanSensitivity();
		if (hugeSensivityModifier) {
			return sens * LARGE_MOVE_FACTOR;
		}
		return sens;
	}

	/** Replies the document canvas within this pane.
	 *
	 * @return the document canvas.
	 */
	@Pure
	public ZoomableCanvas<T, DT> getDocumentCanvas() {
		return this.documentCanvas;
	}

	/** Replies the corner pane.
	 *
	 * @return the corner pane.
	 */
	@Pure
	public ColorSquare getCorner() {
		return this.corner;
	}

	@Override
	public final ObjectProperty<DT> documentModelProperty() {
		return getDocumentCanvas().documentModelProperty();
	}

	@Override
	public final DT getDocumentModel() {
		return getDocumentCanvas().getDocumentModel();
	}

	@Override
	public final void setDocumentModel(DT model) {
		getDocumentCanvas().setDocumentModel(model);
	}

	@Override
	public final ObjectProperty<DocumentDrawer<T, DT>> documentDrawerProperty() {
		return getDocumentCanvas().documentDrawerProperty();
	}

	@Override
	public final DocumentDrawer<T, DT> getDocumentDrawer() {
		return getDocumentCanvas().getDocumentDrawer();
	}

	@Override
	public final void setDocumentDrawer(DocumentDrawer<T, DT> drawer) {
		getDocumentCanvas().setDocumentDrawer(drawer);
	}

	@Override
	public final ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> documentBoundsProperty() {
		return getDocumentCanvas().documentBoundsProperty();
	}

	@Override
	public final Rectangle2afp<?, ?, ?, ?, ?, ?> getDocumentBounds() {
		return getDocumentCanvas().getDocumentBounds();
	}

	@Override
	public final DoubleProperty scaleValueProperty() {
		return getDocumentCanvas().scaleValueProperty();
	}

	@Override
	public final double getScaleValue() {
		return getDocumentCanvas().getScaleValue();
	}

	@Override
	public final void setScaleValue(double value) {
		getDocumentCanvas().setScaleValue(value);
	}

	@Override
	public final void setScaleValue(double scaleValue, double centerX, double centerY) {
		getDocumentCanvas().setScaleValue(scaleValue, centerX, centerY);
	}

	@Override
	public final DoubleProperty minScaleValueProperty() {
		return getDocumentCanvas().minScaleValueProperty();
	}

	@Override
	public final double getMinScaleValue() {
		return getDocumentCanvas().getMinScaleValue();
	}

	@Override
	public final void setMinScaleValue(double value) {
		getDocumentCanvas().setMinScaleValue(value);
	}

	@Override
	public final DoubleProperty maxScaleValueProperty() {
		return getDocumentCanvas().maxScaleValueProperty();
	}

	@Override
	public final double getMaxScaleValue() {
		return getDocumentCanvas().getMaxScaleValue();
	}

	@Override
	public final void setMaxScaleValue(double value) {
		getDocumentCanvas().setMaxScaleValue(value);
	}

	@Override
	public final DoubleProperty viewportCenterXProperty() {
		return getDocumentCanvas().viewportCenterXProperty();
	}

	@Override
	public final double getViewportCenterX() {
		return getDocumentCanvas().getViewportCenterX();
	}

	@Override
	public final void setViewportCenterX(double x) {
		getDocumentCanvas().setViewportCenterX(x);
	}

	@Override
	public final DoubleProperty viewportCenterYProperty() {
		return getDocumentCanvas().viewportCenterYProperty();
	}

	@Override
	public final double getViewportCenterY() {
		return getDocumentCanvas().getViewportCenterY();
	}

	@Override
	public final void setViewportCenterY(double y) {
		getDocumentCanvas().setViewportCenterY(y);
	}

	@Override
	public final void setViewportCenter(double x, double y) {
		getDocumentCanvas().setViewportCenter(x, y);
	}

	@Override
	public final ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> viewportBoundsProperty() {
		return getDocumentCanvas().viewportBoundsProperty();
	}

	@Override
	public final Rectangle2afp<?, ?, ?, ?, ?, ?> getViewportBounds() {
		return getDocumentCanvas().getViewportBounds();
	}

	@Override
	public final double getScaleValueToFit(boolean minimizeOnly) {
		return getDocumentCanvas().getScaleValueToFit(minimizeOnly);
	}

	@Override
	public final void zoomOut(double centerX, double centerY) {
		getDocumentCanvas().zoomOut(centerX, centerY);
	}

	@Override
	public final void zoomIn(double centerX, double centerY) {
		getDocumentCanvas().zoomIn(centerX, centerY);
	}

	@Override
	public final DoubleProperty scaleChangeProperty() {
		return getDocumentCanvas().scaleChangeProperty();
	}

	@Override
	public final double getScaleChange() {
		return getDocumentCanvas().getScaleChange();
	}

	@Override
	public final void setScaleChange(double change) {
		getDocumentCanvas().setScaleChange(change);
	}

	@Override
	public final BooleanProperty invertedAxisXProperty() {
		return getDocumentCanvas().invertedAxisXProperty();
	}

	@Override
	public final boolean isInvertedAxisX() {
		return getDocumentCanvas().isInvertedAxisX();
	}

	@Override
	public final void setInvertedAxisX(boolean inverted) {
		getDocumentCanvas().setInvertedAxisX(inverted);
	}

	@Override
	public final BooleanProperty invertedAxisYProperty() {
		return getDocumentCanvas().invertedAxisYProperty();
	}

	@Override
	public final boolean isInvertedAxisY() {
		return getDocumentCanvas().isInvertedAxisY();
	}

	@Override
	public final void setInvertedAxisY(boolean inverted) {
		getDocumentCanvas().setInvertedAxisY(inverted);
	}

	@Override
	public final IntegerProperty drawableElementBudgetProperty() {
		return getDocumentCanvas().drawableElementBudgetProperty();
	}

	@Override
	public final int getDrawableElementBudget() {
		return getDocumentCanvas().getDrawableElementBudget();
	}

	@Override
	public final void setDrawableElementBudget(int budget) {
		getDocumentCanvas().setDrawableElementBudget(budget);
	}

	@Override
	public void addDrawingListener(DrawingListener listener) {
		getDocumentCanvas().addDrawingListener(listener);
	}

	@Override
	public void removeDrawingListener(DrawingListener listener) {
		getDocumentCanvas().removeDrawingListener(listener);
	}

	/** The color square at the corner of a {@code ZoomablePane}.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 15.0
	 */
	public static class ColorSquare extends StackPane {

		private static final int SQUARE_SIZE = 15;

		private final Rectangle rectangle;

		private boolean isEmpty;

		/** Constructor of empty square.
		 */
		public ColorSquare() {
			this(null);
		}

		/** Constructor with the given color.
		 *
		 * @param color the color of the square.
		 */
		public ColorSquare(Color color) {
			// Add style class to handle selected color square
			getStyleClass().add("color-square"); //$NON-NLS-1$
			setFocusTraversable(false);
			this.rectangle = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
			this.rectangle.setStrokeType(StrokeType.INSIDE);
			this.rectangle.getStyleClass().add("color-rect"); //$NON-NLS-1$
			setColor(color);
			getChildren().add(this.rectangle);
		}

		@Override
		public boolean isResizable() {
			return false;
		}

		/** Replies if the square is empty and has not specific color.
		 *
		 * @return {@code true} if the square is empty.
		 */
		public boolean isEmpty() {
			return this.isEmpty;
		}

		/** Replies the color.
		 *
		 * @return the color of the square.
		 */
		public Color getPaint() {
			return (Color) this.rectangle.getFill();
		}

		/** Change the color.
		 *
		 * @param color the color of the square.
		 */
		public void setColor(Color color) {
			this.isEmpty = color == null;
			if (this.isEmpty) {
				this.rectangle.setFill(Color.TRANSPARENT);
			} else {
				this.rectangle.setFill(color);
			}
		}

	}

	/** Super-lazy instantiation pattern, inspired by the one from Bill Pugh.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 15.0
	 * @treatAsPrivate
	 */
	private static class StyleableProperties {

		/** Pannable pseudo class state.
		 */
		public static final PseudoClass PANNABLE_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("pannable"); //$NON-NLS-1$

		public static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

		/** Pannable CSS metadata.
		 */
		public static final CssMetaData<ZoomablePane<?, ?>, Boolean> PANNABLE = new CssMetaData<ZoomablePane<?, ?>, Boolean>(
				"-afc-pannable", //$NON-NLS-1$
				BooleanConverter.getInstance(), Boolean.FALSE) {

			@SuppressWarnings("synthetic-access")
			@Override
			public boolean isSettable(ZoomablePane<?, ?> pane) {
				return pane.pannable == null || !pane.pannable.isBound();
			}

			@SuppressWarnings("unchecked")
			@Override
			public StyleableProperty<Boolean> getStyleableProperty(ZoomablePane<?, ?> pane) {
				return (StyleableProperty<Boolean>) pane.pannableProperty();
			}
		};

		/** PanButton CSS metadata.
		 */
		@SuppressWarnings("synthetic-access")
		public static final CssMetaData<ZoomablePane<?, ?>, MouseButton> PAN_BUTTON =
			new CssMetaData<ZoomablePane<?, ?>, MouseButton>(
					"-afc-panbutton", //$NON-NLS-1$
					new EnumConverter<>(MouseButton.class), DEFAULT_PAN_BUTTON) {

				@Override
				public boolean isSettable(ZoomablePane<?, ?> pane) {
					return pane.pannable == null || !pane.pannable.isBound();
				}

				@SuppressWarnings("unchecked")
				@Override
				public StyleableProperty<MouseButton> getStyleableProperty(ZoomablePane<?, ?> pane) {
					return (StyleableProperty<MouseButton>) pane.panButtonProperty();
				}
			};

		/** PanButton CSS metadata.
		 */
		@SuppressWarnings("synthetic-access")
		public static final CssMetaData<ZoomablePane<?, ?>, Number> PAN_SENSITIVITY = new CssMetaData<ZoomablePane<?, ?>, Number>(
				"-afc-panbutton", //$NON-NLS-1$
				SizeConverter.getInstance(), DEFAULT_PAN_SENSITIVITY) {

			@Override
			public boolean isSettable(ZoomablePane<?, ?> pane) {
				return pane.pannable == null || !pane.pannable.isBound();
			}

			@SuppressWarnings("unchecked")
			@Override
			public StyleableProperty<Number> getStyleableProperty(ZoomablePane<?, ?> pane) {
				return (StyleableProperty<Number>) pane.panSensitivityProperty();
			}
		};

		static {
			final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
			styleables.add(PANNABLE);
			styleables.add(PAN_BUTTON);
			styleables.add(PAN_SENSITIVITY);
			STYLEABLES = Collections.unmodifiableList(styleables);
		}

		@Override
		public String toString() {
			return STYLEABLES.toString();
		}

	}

}
