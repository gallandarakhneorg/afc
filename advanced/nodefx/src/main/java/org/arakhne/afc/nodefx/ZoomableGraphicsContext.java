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

import java.util.Deque;
import java.util.LinkedList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.TextAlignment;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * This class is used to issue draw calls to a {@code ZoomableCanvas} using a buffer.
 *
 * <p>The {@code ZoomableGraphicsContext} is a wrapper to the {@link GraphicsContext}
 * embedded within the {@link ZoomableCanvas}.
 *
 * <p>The coordinates that are supported by the {@code ZoomableGraphicsContext} are
 * document coordinates (not the screen coordinates). Consequently, the
 * {@code ZoomableGraphicsContext} translates any coordinates from the document standard
 * to the JavaFX standard.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 * @see ZoomableCanvas
 * @see GraphicsContext
 */
@SuppressWarnings("checkstyle:methodcount")
public class ZoomableGraphicsContext {

	private static final double LOW_DETAILLED_METER_SIZE = .2;

	private static final double HIGH_DETAILLED_METER_SIZE = 5;

	private final GraphicsContext gc;

	private final CenteringTransform centeringTransform;

	private final DoubleProperty scale;

	private final DoubleProperty canvasWidth;

	private final DoubleProperty canvasHeight;

	private final ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> documentBounds;

	private final ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> visibleArea;

	private final IntegerProperty drawableElementBudget;

	private int bugdet;

	private int state;

	private Deque<int[]> stateStack;

	private LevelOfDetails lod;

	/** Constructor.
	 *
	 * @param gc the JavaFX graphics context.
	 * @param scale is the scaling value.
	 * @param documentBounds the bounds of the document.
	 * @param visibleArea the visible part of the document.
	 * @param canvasWidth width of the graphical canvas.
	 * @param canvasHeight height of the graphical canvas.
	 * @param drawableElementBudget the drawable element budget.
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 *     system from the "global" document coordinate system to/from the "centered" document coordinate
	 *     system.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public ZoomableGraphicsContext(
			GraphicsContext gc,
			DoubleProperty scale,
			ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> documentBounds,
			ReadOnlyObjectProperty<Rectangle2afp<?, ?, ?, ?, ?, ?>> visibleArea,
			DoubleProperty canvasWidth,
			DoubleProperty canvasHeight,
			IntegerProperty drawableElementBudget,
			CenteringTransform centeringTransform) {
		assert gc != null : AssertMessages.notNullParameter(0);
		assert scale != null : AssertMessages.notNullParameter(1);
		assert documentBounds != null : AssertMessages.notNullParameter(2);
		assert visibleArea != null : AssertMessages.notNullParameter(3);
		assert canvasWidth != null : AssertMessages.notNullParameter(4);
		assert canvasHeight != null : AssertMessages.notNullParameter(5);
		assert drawableElementBudget != null : AssertMessages.notNullParameter(6);
		assert centeringTransform != null : AssertMessages.notNullParameter(7);
		this.gc = gc;
		this.scale = scale;
		this.documentBounds = documentBounds;
		this.visibleArea = visibleArea;
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.drawableElementBudget = drawableElementBudget;
		this.centeringTransform = centeringTransform;
	}

	/** Prepare the rendering by reseting the budget and the state.
	 *
	 * @see #getBudget()
	 * @see #getState()
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public void prepareRendering() {
		this.bugdet = this.drawableElementBudget.get();
		this.state = 0;
		this.lod = null;
	}

	/** Replies the budget.
	 *
	 * @return the budget.
	 */
	public int getBudget() {
		return this.bugdet;
	}

	/** Consume the budget.
	 *
	 * @return {@code true} if an unit of the budget is consumed. {@code false} if no consumption.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public boolean consumeBudget() {
		if (this.bugdet > 0) {
			--this.bugdet;
			return true;
		}
		return false;
	}

	/** Replies the user-defined state of the context.
	 *
	 * @return the state.
	 */
	public int getState() {
		return this.state;
	}

	/** Change the user-defined state of the context.
	 *
	 * @param state the state.
	 */
	public void setState(int state) {
		this.state = state;
	}

	/** Replies the current level of details.
	 *
	 * @return the level of details.
	 */
	public LevelOfDetails getLevelOfDetails() {
		if (this.lod == null) {
			final double meterSize = doc2fxSize(1);
			if (meterSize <= LOW_DETAILLED_METER_SIZE) {
				this.lod = LevelOfDetails.LOW;
			} else if (meterSize >= HIGH_DETAILLED_METER_SIZE) {
				this.lod = LevelOfDetails.HIGH;
			} else {
				this.lod = LevelOfDetails.NORMAL;
			}
		}
		return this.lod;
	}

	/** Replies the document bounds in document coordinates.
	 *
	 * @return the document bounds.
	 */
	@Pure
	public Rectangle2afp<?, ?, ?, ?, ?, ?> getDocumentBounds() {
		return this.documentBounds.get();
	}

	/** Replies the bounds of the visible area in document coordinates.
	 *
	 * @return the visible area bounds.
	 */
	@Pure
	public Rectangle2afp<?, ?, ?, ?, ?, ?> getVisibleArea() {
		return this.visibleArea.get();
	}

	/**
	 * Gets the {@code ZoomableCanvas} that the {@code GraphicsContext} is issuing draw
	 * commands to. There is only ever one {@code ZoomableCanvas} for a
	 * {@code GraphicsContext}.
	 *
	 * @return Canvas the canvas that this {@code GraphicsContext} is issuing draw
	 *     commands to.
	 */
	@Pure
	public ZoomableCanvas<?> getCanvas() {
		return (ZoomableCanvas<?>) this.gc.getCanvas();
	}

	/**
	 * Gets the {@code GraphicsContext} that is wrapped by this object.
	 *
	 * @return the wrapped {@code GraphicsContext}.
	 */
	@Pure
	public GraphicsContext getWrappedGraphicsContext2D() {
		return this.gc;
	}

	/** Parse the given RGB color.
	 *
	 * <p>Opacity is always {@code 1}.
	 *
	 * @param color the RGB color.
	 * @return the JavaFX color.
	 */
	@SuppressWarnings({ "checkstyle:magicnumber", "static-method" })
	@Pure
	public Color rgb(int color) {
		final int red = (color & 0x00FF0000) >> 16;
		final int green = (color & 0x0000FF00) >> 8;
		final int blue = color & 0x000000FF;
		return Color.rgb(red, green, blue);
	}

	/** Parse the given RGBA color.
	 *
	 * @param color the RGBA color.
	 * @return the JavaFX color.
	 */
	@SuppressWarnings({ "checkstyle:magicnumber", "static-method" })
	@Pure
	public Color rgba(int color) {
		final int alpha = (color & 0xFF000000) >> 24;
		final int red = (color & 0x00FF0000) >> 16;
		final int green = (color & 0x0000FF00) >> 8;
		final int blue = color & 0x000000FF;
		final double opacity;
		switch (alpha) {
		case 0:
			opacity = 0.;
			break;
		case 255:
			opacity = 1.;
			break;
		default:
			opacity = ((double) alpha) / 255;
		}
		return Color.rgb(red, green, blue, opacity);
	}

	/** Parse the given RGBA color.
	 *
	 * @param rgb the RGB color.
	 * @param opacity the opacity, 0 for fully transparent, 1 for fully opaque.
	 * @return the JavaFX color.
	 */
	@SuppressWarnings({ "checkstyle:magicnumber", "static-method" })
	@Pure
	public Color rgba(int rgb, double opacity) {
		final int red = (rgb & 0x00FF0000) >> 16;
		final int green = (rgb & 0x0000FF00) >> 8;
		final int blue = rgb & 0x000000FF;
		return Color.rgb(red, green, blue, opacity);
	}

	/** Parse the given RGBA color. The opacity component of the given color is replaced by the given opacity.
	 *
	 * @param rgb the color.
	 * @param opacity the opacity, 0 for fully transparent, 1 for fully opaque.
	 * @return the JavaFX color.
	 * @since 16.0
	 */
	@SuppressWarnings({ "checkstyle:magicnumber", "static-method" })
	@Pure
	public Color rgba(Color rgb, double opacity) {
		final int red = (int) (rgb.getRed() * 255) & 0xFF;
		final int green = (int) (rgb.getGreen() * 255) & 0xFF;
		final int blue = (int) (rgb.getBlue() * 255) & 0xFF;
		return Color.rgb(red, green, blue, opacity);
	}

	/** Transform a document x coordinate to its JavaFX equivalent.
	 *
	 * @param x the document x coordinate.
	 * @return the JavaFX x coordinate.
	 */
	@Pure
	public double doc2fxX(double x) {
		return this.centeringTransform.toCenterX(x) * this.scale.get() + this.canvasWidth.get() / 2.;
	}

	/** Transform a JavaFX x coordinate to its document equivalent.
	 *
	 * @param x the document x coordinate.
	 * @return the JavaFX x coordinate.
	 */
	@Pure
	public double fx2docX(double x) {
		return this.centeringTransform.toGlobalX((x - this.canvasWidth.get() / 2.) / this.scale.get());
	}

	/** Transform a document y coordinate to its JavaFX equivalent.
	 *
	 * @param y the document y coordinate.
	 * @return the JavaFX y coordinate.
	 */
	@Pure
	public double doc2fxY(double y) {
		return this.centeringTransform.toCenterY(y) * this.scale.get() + this.canvasHeight.get() / 2.;
	}

	/** Transform a JavaFX y coordinate to its document equivalent.
	 *
	 * @param y the JavaFX y coordinate.
	 * @return the document y coordinate.
	 */
	@Pure
	public double fx2docY(double y) {
		return this.centeringTransform.toGlobalY((y - this.canvasHeight.get() / 2.) / this.scale.get());
	}

	/** Transform a document size (or distance) to its JavaFX equivalent.
	 *
	 * <p>The JavaFX size {@code fx_x} is equal to:
	 * <pre><code>fx_size = size * scale</code></pre>
	 *
	 * @param size the document size.
	 * @return the JavaFX size.
	 */
	@Pure
	public double doc2fxSize(double size) {
		return size * this.scale.get();
	}

	/** Transform a JavaFX size (or distance) to its document equivalent.
	 *
	 * <p>The document size {@code doc_size} is equal to:
	 * <pre><code>doc_size = size / scale</code></pre>
	 *
	 * @param size the JavaFX size.
	 * @return the document size.
	 */
	@Pure
	public double fx2docSize(double size) {
		return size / this.scale.get();
	}

	/** Transform a document angle to its JavaFX equivalent.
	 *
	 * @param angle the document angle.
	 * @return the JavaFX angle.
	 */
	@Pure
	public double doc2fxAngle(double angle) {
		final ZoomableCanvas<?> canvas = getCanvas();
		if (canvas.isInvertedAxisX() != canvas.isInvertedAxisY()) {
			return -angle;
		}
		return angle;
	}

	/** Transform a JavaFX angle to its document equivalent.
	 *
	 * @param angle the JavaFX angle.
	 * @return the document angle.
	 */
	@Pure
	public double fx2docAngle(double angle) {
		final ZoomableCanvas<?> canvas = getCanvas();
		if (canvas.isInvertedAxisX() != canvas.isInvertedAxisY()) {
			return -angle;
		}
		return angle;
	}

	/** Transform a document rectangle's minimum point to its JavaFX equivalent.
	 *
	 * @param x the document x coordinate.
	 * @param y the document y coordinate.
	 * @param width the document width.
	 * @param height the document height.
	 * @return the rectangle's minimum point into the JavaFX coordinates.
	 * @since 16.0
	 */
	@Pure
	public Point2d doc2fxRectBase(double x, double y, double width, double height) {
		final double rx;
		if (this.centeringTransform.isInvertedAxisX()) {
			rx = x + width;
		} else {
			rx = x;
		}
		final double ry;
		if (this.centeringTransform.isInvertedAxisY()) {
			ry = y + height;
		} else {
			ry = y;
		}
		return new Point2d(doc2fxX(rx), doc2fxY(ry));
	}

	/** Transform a JavaFX rectangle's minimum point to its document equivalent.
	 *
	 * @param x the JavaFX x coordinate.
	 * @param y the JavaFX  y coordinate.
	 * @param width the JavaFX width.
	 * @param height the JavaFX height.
	 * @return the rectangle's minimum point into the document FX coordinates.
	 * @since 16.0
	 */
	@Pure
	public Point2d fx2docRectBase(double x, double y, double width, double height) {
		final double rx;
		if (this.centeringTransform.isInvertedAxisX()) {
			rx = x + width;
		} else {
			rx = x;
		}
		final double ry;
		if (this.centeringTransform.isInvertedAxisY()) {
			ry = y + height;
		} else {
			ry = y;
		}
		return new Point2d(fx2docX(rx), fx2docY(ry));
	}

	/**
	 * Saves the following attributes onto a stack.
	 * <ul>
	 *     <li>Global Alpha</li>
	 *     <li>Global Blend Operation</li>
	 *     <li>Transform</li>
	 *     <li>Fill Paint</li>
	 *     <li>Stroke Paint</li>
	 *     <li>Line Width</li>
	 *     <li>Line Cap</li>
	 *     <li>Line Join</li>
	 *     <li>Miter Limit</li>
	 *     <li>Clip</li>
	 *     <li>Font</li>
	 *     <li>Text Align</li>
	 *     <li>Text Baseline</li>
	 *     <li>Effect</li>
	 *     <li>Fill Rule</li>
	 * </ul>
	 * This method does NOT alter the current state in any way. Also, note that
	 * the current path is not saved.
	 */
	public void save() {
		if (this.stateStack == null) {
			this.stateStack = new LinkedList<>();
		}
		this.stateStack.push(new int[] {this.bugdet, this.state});
		this.gc.save();
	}

	/**
	 * Pops the state off of the stack, setting the following attributes to their
	 * value at the time when that state was pushed onto the stack. If the stack
	 * is empty then nothing is changed.
	 *
	 * <ul>
	 *     <li>Global Alpha</li>
	 *     <li>Global Blend Operation</li>
	 *     <li>Transform</li>
	 *     <li>Fill Paint</li>
	 *     <li>Stroke Paint</li>
	 *     <li>Line Width</li>
	 *     <li>Line Cap</li>
	 *     <li>Line Join</li>
	 *     <li>Miter Limit</li>
	 *     <li>Clip</li>
	 *     <li>Font</li>
	 *     <li>Text Align</li>
	 *     <li>Text Baseline</li>
	 *     <li>Effect</li>
	 *     <li>Fill Rule</li>
	 * </ul>
	 * Note that the current path is not restored.
	 */
	public void restore() {
		this.gc.restore();
		if (this.stateStack != null) {
			if (!this.stateStack.isEmpty()) {
				final int[] data = this.stateStack.pop();
				if (this.stateStack.isEmpty()) {
					this.stateStack = null;
				}
				this.bugdet = data[0];
				this.state = data[1];
			} else {
				this.stateStack = null;
			}
		}
	}

	/**
	 * Translates the current transform by x, y.
	 *
	 * @param x value to translate along the x axis.
	 * @param y value to translate along the y axis.
	 */
	public void translate(double x, double y) {
		this.gc.translate(doc2fxSize(x), doc2fxSize(y));
	}

	/**
	 * Scales the current transform by x, y.
	 *
	 * @param x value to scale in the x axis.
	 * @param y value to scale in the y axis.
	 */
	public void scale(double x, double y) {
		this.gc.scale(doc2fxSize(x), doc2fxSize(y));
	}

	/**
	 * Rotates the current transform in radians.
	 *
	 * @param radians value in radians to rotate the current transform.
	 */
	public void rotate(double radians) {
		this.gc.rotate(doc2fxAngle(Math.toDegrees(radians)));
	}

	/**
	 * Sets the global alpha of the current state.
	 * The default value is {@code 1.0}.
	 * Any valid double can be set, but only values in the range
	 * {@code [0.0, 1.0]} are valid and the nearest value in that
	 * range will be used for rendering.
	 * The global alpha is a common attribute
	 * used for nearly all rendering methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param alpha the new alpha value, clamped to {@code [0.0, 1.0]}
	 *              during actual use.
	 */
	public void setGlobalAlpha(double alpha) {
		this.gc.setGlobalAlpha(alpha);
	}

	/**
	 * Gets the current global alpha.
	 * The default value is {@code 1.0}.
	 * The global alpha is a common attribute
	 * used for nearly all rendering methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return the current global alpha.
	 */
	public double getGlobalAlpha() {
		return this.gc.getGlobalAlpha();
	}

	/**
	 * Sets the global blend mode.
	 * The default value is {@link BlendMode#SRC_OVER SRC_OVER}.
	 * A {@code null} value will be ignored and the current value will remain unchanged.
	 * The blend mode is a common attribute
	 * used for nearly all rendering methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param op the {@code BlendMode} that will be set or null.
	 */
	public void setGlobalBlendMode(BlendMode op) {
		this.gc.setGlobalBlendMode(op);
	}

	/**
	 * Gets the global blend mode.
	 * The default value is {@link BlendMode#SRC_OVER SRC_OVER}.
	 * The blend mode is a common attribute
	 * used for nearly all rendering methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return the global {@code BlendMode} of the current state.
	 */
	public BlendMode getGlobalBlendMode() {
		return this.gc.getGlobalBlendMode();
	}

	/**
	 * Sets the current fill paint attribute.
	 * The default value is {@link Color#BLACK BLACK}.
	 * The fill paint is a fill attribute
	 * used for any of the fill methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * A {@code null} value will be ignored and the current value will remain unchanged.
	 *
	 * @param paint The {@code Paint} to be used as the fill {@code Paint} or null.
	 */
	public void setFill(Paint paint) {
		this.gc.setFill(paint);
	}

	/**
	 * Gets the current fill paint attribute.
	 * The default value is {@link Color#BLACK BLACK}.
	 * The fill paint is a fill attribute
	 * used for any of the fill methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return The {@code Paint} to be used as the fill {@code Paint}.
	 */
	public Paint getFill() {
		return this.gc.getFill();
	}

	/**
	 * Sets the current stroke paint attribute.
	 * The default value is {@link Color#BLACK BLACK}.
	 * The stroke paint is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * A {@code null} value will be ignored and the current value will remain unchanged.
	 *
	 * @param paint The Paint to be used as the stroke Paint or null.
	 */
	public void setStroke(Paint paint) {
		this.gc.setStroke(paint);
	}

	/**
	 * Gets the current stroke.
	 * The default value is {@link Color#BLACK BLACK}.
	 * The stroke paint is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return the {@code Paint} to be used as the stroke {@code Paint}.
	 */
	public Paint getStroke() {
		return this.gc.getStroke();
	}

	/**
	 * Sets the current line width in pixels.
	 * The default value is {@code 1.0}.
	 * The line width is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * An infinite or non-positive value outside of the range {@code (0, +inf)}
	 * will be ignored and the current value will remain unchanged.
	 *
	 * @param lw pixel value in the range {0-positive infinity}, with any other value
	 *     being ignored and leaving the value unchanged.
	 * @see #setLineWidthInMeters(double)
	 */
	public void setLineWidthInPixels(double lw) {
		this.gc.setLineWidth(lw);
	}

	/**
	 * Sets the current line width in meters.
	 * The default value is {@code 1.0 * scale}.
	 * The line width is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * An infinite or non-positive value outside of the range {@code (0, +inf)}
	 * will be ignored and the current value will remain unchanged.
	 *
	 * @param lw metric value in the range {0-positive infinity}, with any other value
	 *     being ignored and leaving the value unchanged.
	 * @see #setLineWidthInPixels(double)
	 */
	@Inline(value = "setLineWidthInPixels($0doc2fxSize($1))")
	public void setLineWidthInMeters(double lw) {
		setLineWidthInPixels(doc2fxSize(lw));
	}

	/**
	 * Gets the current line width in pixels.
	 * The default value is {@code 1.0}.
	 * The line width is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return pixel value between 0 and infinity.
	 * @see #getLineWidthInMeters()
	 */
	public double getLineWidthInPixels() {
		return this.gc.getLineWidth();
	}

	/**
	 * Gets the current line width in meters.
	 * The default value is {@code 1.0}.
	 * The line width is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return metric value between 0 and infinity.
	 * @see #getLineWidthInPixels()
	 */
	@Inline(value = "fx2docSize($0getLineWidthInPixels())")
	public double getLineWidthInMeters() {
		return fx2docSize(getLineWidthInPixels());
	}

	/**
	 * Sets the current stroke line cap.
	 * The default value is {@link StrokeLineCap#SQUARE SQUARE}.
	 * The line cap is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * A {@code null} value will be ignored and the current value will remain unchanged.
	 *
	 * @param cap {@code StrokeLineCap} with a value of Butt, Round, or Square or null.
	 */
	public void setLineCap(StrokeLineCap cap) {
		this.gc.setLineCap(cap);
	}

	/**
	 * Gets the current stroke line cap.
	 * The default value is {@link StrokeLineCap#SQUARE SQUARE}.
	 * The line cap is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return {@code StrokeLineCap} with a value of Butt, Round, or Square.
	 */
	public StrokeLineCap getLineCap() {
		return this.gc.getLineCap();
	}

	/**
	 * Sets the current stroke line join.
	 * The default value is {@link StrokeLineJoin#MITER}.
	 * The line join is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * A {@code null} value will be ignored and the current value will remain unchanged.
	 *
	 * @param join {@code StrokeLineJoin} with a value of Miter, Bevel, or Round or null.
	 */
	public void setLineJoin(StrokeLineJoin join) {
		this.gc.setLineJoin(join);
	}

	/**
	 * Gets the current stroke line join.
	 * The default value is {@link StrokeLineJoin#MITER}.
	 * The line join is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return {@code StrokeLineJoin} with a value of Miter, Bevel, or Round.
	 */
	public StrokeLineJoin getLineJoin() {
		return this.gc.getLineJoin();
	}

	/**
	 * Sets the current miter limit in pixels.
	 * The default value is {@code 10.0}.
	 * The miter limit is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * An infinite or non-positive value outside of the range {@code (0, +inf)}
	 * will be ignored and the current value will remain unchanged.
	 *
	 * @param ml pixel miter limit value between 0 and positive infinity with
	 *     any other value being ignored and leaving the value unchanged.
	 * @see #setMiterLimitInMeters(double)
	 */
	public void setMiterLimitInPixels(double ml) {
		this.gc.setMiterLimit(ml);
	}

	/**
	 * Sets the current miter limit in meters.
	 * The default value is {@code 10.0 * scale}.
	 * The miter limit is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * An infinite or non-positive value outside of the range {@code (0, +inf)}
	 * will be ignored and the current value will remain unchanged.
	 *
	 * @param ml metric miter limit value between 0 and positive infinity with
	 *     any other value being ignored and leaving the value unchanged.
	 * @see #setMiterLimitInPixels(double)
	 */
	@Inline(value = "setMiterLimitInPixels($0doc2fxSize($1))")
	public void setMiterLimitInMeters(double ml) {
		setMiterLimitInPixels(doc2fxSize(ml));
	}

	/**
	 * Gets the current miter limit in pixels.
	 * The default value is {@code 10.0}.
	 * The miter limit is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return the pixel miter limit value in the range {@code 0.0-positive infinity}
	 * @see #getMiterLimitInMeters()
	 */
	public double getMiterLimitInPixels() {
		return this.gc.getMiterLimit();
	}

	/**
	 * Gets the current miter limit in meters.
	 * The default value is {@code 10.0 * scale}.
	 * The miter limit is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return the metric miter limit value in the range {@code 0.0-positive infinity}
	 */
	@Inline(value = "fx2docSize($0getMiterLimitInPixels())")
	public double getMiterLimitInMeters() {
		return fx2docSize(getMiterLimitInPixels());
	}

	/**
	 * Sets the current stroke line dash pattern in pixels to a normalized copy of
	 * the argument.
	 * The default value is {@code null}.
	 * The line dash array is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * If the array is {@code null} or empty or contains all {@code 0} elements
	 * then dashing will be disabled and the current dash array will be set
	 * to {@code null}.
	 * If any of the elements of the array are a negative, infinite, or NaN
	 * value outside the range {@code [0, +inf)} then the entire array will
	 * be ignored and the current dash array will remain unchanged.
	 * If the array is an odd length then it will be treated as if it
	 * were two copies of the array appended to each other.
	 *
	 * @param dashes the array of finite non-negative dash lengths.
	 * @see #setLineDashesInMeters(double...)
	 */
	public void setLineDashesInPixels(double... dashes) {
		this.gc.setLineDashes(dashes);
	}

	/**
	 * Sets the current stroke line dash pattern in meters to a normalized copy of
	 * the argument.
	 * The default value is {@code null}.
	 * The line dash array is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * If the array is {@code null} or empty or contains all {@code 0} elements
	 * then dashing will be disabled and the current dash array will be set
	 * to {@code null}.
	 * If any of the elements of the array are a negative, infinite, or NaN
	 * value outside the range {@code [0, +inf)} then the entire array will
	 * be ignored and the current dash array will remain unchanged.
	 * If the array is an odd length then it will be treated as if it
	 * were two copies of the array appended to each other.
	 *
	 * @param dashes the array of finite non-negative dash lengths
	 * @see #setLineDashesInPixels(double...)
	 */
	public void setLineDashesInMeters(double... dashes) {
		final double[] newDashes;
		if (dashes == null) {
			newDashes = dashes;
		} else {
			newDashes = new double[dashes.length];
			for (int i = 0; i < newDashes.length; ++i) {
				newDashes[i] = doc2fxSize(dashes[i]);
			}
		}
		setLineDashesInPixels(newDashes);
	}

	/**
	 * Gets a copy of the current line dash array in pixels.
	 * The default value is {@code null}.
	 * The array may be normalized by the validation tests in the
	 * {@link #setLineDashesInPixels(double...)} method.
	 * The line dash array is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return a copy of the current line dash array.
	 * @see #getLineDashesInMeters()
	 */
	public double[] getLineDashesInPixels() {
		return this.gc.getLineDashes();
	}

	/**
	 * Gets a copy of the current line dash array in meters.
	 * The default value is {@code null}.
	 * The array may be normalized by the validation tests in the
	 * {@link #setLineDashesInMeters(double...)} method.
	 * The line dash array is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return a copy of the current line dash array.
	 * @see #getLineDashesInPixels()
	 */
	public double[] getLineDashesInMeters() {
		final double[] pdashes = this.gc.getLineDashes();
		if (pdashes == null) {
			return null;
		}
		final double[] newDashes = new double[pdashes.length];
		for (int i = 0; i < newDashes.length; ++i) {
			newDashes[i] = doc2fxSize(pdashes[i]);
		}
		return newDashes;
	}

	/**
	 * Sets the line dash offset in pixels.
	 * The default value is {@code 0.0}.
	 * The line dash offset is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * An infinite or NaN value outside of the range {@code (-inf, +inf)}
	 * will be ignored and the current value will remain unchanged.
	 *
	 * @param dashOffset the line dash offset in the range {@code (-inf, +inf)}
	 * @see #setLineDashOffsetInMeters(double)
	 */
	public void setLineDashOffsetInPixels(double dashOffset) {
		this.gc.setLineDashOffset(dashOffset);
	}

	/**
	 * Sets the line dash offset in meters.
	 * The default value is {@code 0.0 * scale}.
	 * The line dash offset is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * An infinite or NaN value outside of the range {@code (-inf, +inf)}
	 * will be ignored and the current value will remain unchanged.
	 *
	 * @param dashOffset the line dash offset in the range {@code (-inf, +inf)}
	 * @see #setLineDashOffsetInPixels(double)
	 */
	@Inline(value = "setLineDashOffsetInPixels($0doc2fxSize($1))")
	public void setLineDashOffsetInMeters(double dashOffset) {
		setLineDashOffsetInPixels(doc2fxSize(dashOffset));
	}

	/**
	 * Gets the current line dash offset in pixels.
	 * The default value is {@code 0.0}.
	 * The line dash offset is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return the line dash offset in the range {@code (-inf, +inf)}
	 * @see #getLineDashOffsetInMeters()
	 */
	public double getLineDashOffsetInPixels() {
		return this.gc.getLineDashOffset();
	}

	/**
	 * Gets the current line dash offset in meters.
	 * The default value is {@code 0.0}.
	 * The line dash offset is a stroke attribute
	 * used for any of the stroke methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return the line dash offset in the range {@code (-inf, +inf)}
	 * @see #getLineDashOffsetInPixels()
	 */
	@Inline(value = "fx2docSize($0getLineDashOffsetInPixels())")
	public double getLineDashOffsetInMeters() {
		return fx2docSize(getLineDashOffsetInPixels());
	}

	/**
	 * Sets the current Font.
	 * The default value is specified by {@link Font#getDefault()}.
	 * The font is a text attribute
	 * used for any of the text methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * A {@code null} value will be ignored and the current value will remain unchanged.
	 *
	 * @param font the Font or null.
	 */
	public void setFont(Font font) {
		this.gc.setFont(font);
	}

	/**
	 * Gets the current Font.
	 * The default value is specified by {@link Font#getDefault()}.
	 * The font is a text attribute
	 * used for any of the text methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return the Font
	 */
	public Font getFont() {
		return this.gc.getFont();
	}

	/**
	 * Sets the current Font Smoothing Type.
	 * The default value is {@link FontSmoothingType#GRAY GRAY}.
	 * The font smoothing type is a text attribute
	 * used for any of the text methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * A {@code null} value will be ignored and the current value will remain unchanged.
	 *
	 * <p><b>Note</b> that the {@code FontSmoothingType} value of
	 * {@link FontSmoothingType#LCD LCD} is only supported over an opaque
	 * background.  {@code LCD} text will generally appear as {@code GRAY}
	 * text over transparent or partially transparent pixels, and in some
	 * implementations it may not be supported at all on a {@link Canvas}
	 * because the required support does not exist for surfaces which contain
	 * an alpha channel as all {@code Canvas} objects do.
	 *
	 * @param fontsmoothing the {@link FontSmoothingType} or null
	 */
	public void setFontSmoothingType(FontSmoothingType fontsmoothing) {
		this.gc.setFontSmoothingType(fontsmoothing);
	}

	/**
	 * Gets the current Font Smoothing Type.
	 * The default value is {@link FontSmoothingType#GRAY GRAY}.
	 * The font smoothing type is a text attribute
	 * used for any of the text methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return the {@link FontSmoothingType}
	 */
	public FontSmoothingType getFontSmoothingType() {
		return this.gc.getFontSmoothingType();
	}

	/**
	 * Defines horizontal text alignment, relative to the text {@code x} origin.
	 * The default value is {@link TextAlignment#LEFT LEFT}.
	 * The text alignment is a text attribute
	 * used for any of the text methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * <p>Let horizontal bounds represent the logical width of a single line of
	 * text. Where each line of text has a separate horizontal bounds.
	 *
	 * <p>Then TextAlignment is specified as:<ul>
	 * <li>Left: the left edge of the horizontal bounds will be at {@code x}.
	 * <li>Center: the center, halfway between left and right edge, of the
	 * horizontal bounds will be at {@code x}.
	 * <li>Right: the right edge of the horizontal bounds will be at {@code x}.
	 * </ul>
	 *
	 * <p>Note: Canvas does not support line wrapping, therefore the text
	 * alignment Justify is identical to left aligned text.
	 *
	 * <p>A {@code null} value will be ignored and the current value will remain unchanged.
	 *
	 * @param align {@code TextAlignment} with values of Left, Center, Right or null.
	 */
	public void setTextAlign(TextAlignment align) {
		this.gc.setTextAlign(align);
	}

	/**
	 * Gets the current {@code TextAlignment}.
	 * The default value is {@link TextAlignment#LEFT LEFT}.
	 * The text alignment is a text attribute
	 * used for any of the text methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return {@code TextAlignment} with values of Left, Center, Right, or Justify.
	 */
	public TextAlignment getTextAlign() {
		return this.gc.getTextAlign();
	}

	/**
	 * Sets the current Text Baseline.
	 * The default value is {@link VPos#BASELINE BASELINE}.
	 * The text baseline is a text attribute
	 * used for any of the text methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * A {@code null} value will be ignored and the current value will remain unchanged.
	 *
	 * @param baseline {@code VPos} with values of Top, Center, Baseline, or Bottom or null.
	 */
	public void setTextBaseline(VPos baseline) {
		this.gc.setTextBaseline(baseline);
	}

	/**
	 * Gets the current Text Baseline.
	 * The default value is {@link VPos#BASELINE BASELINE}.
	 * The text baseline is a text attribute
	 * used for any of the text methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return {@code VPos} with values of Top, Center, Baseline, or Bottom
	 */
	public VPos getTextBaseline() {
		return this.gc.getTextBaseline();
	}

	/**
	 * Fills the given string of text at position x, y
	 * with the current fill paint attribute.
	 * A {@code null} text value will be ignored.
	 *
	 * <p>This method will be affected by any of the
	 * global common, fill, or text
	 * attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param text the string of text or null.
	 * @param x position on the x axis.
	 * @param y position on the y axis.
	 */
	public void fillText(String text, double x, double y) {
		this.gc.fillText(text, doc2fxX(x), doc2fxY(y));
	}

	/**
	 * Fills text and includes a maximum width of the string.
	 * If the width of the text extends past max width, then it will be sized
	 * to fit.
	 * A {@code null} text value will be ignored.
	 *
	 * <p>This method will be affected by any of the
	 * global common, fill, or text
	 * attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param text the string of text or null.
	 * @param x position on the x axis.
	 * @param y position on the y axis.
	 * @param maxWidth  maximum width the text string can have.
	 */
	public void fillText(String text, double x, double y, double maxWidth) {
		this.gc.fillText(text, doc2fxX(x), doc2fxY(y), doc2fxSize(maxWidth));
	}

	/**
	 * Draws the given string of text at position x, y
	 * with the current stroke paint attribute.
	 * A {@code null} text value will be ignored.
	 *
	 * <p>This method will be affected by any of the
	 * global common, stroke, or text
	 * attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param text the string of text or null.
	 * @param x position on the x axis.
	 * @param y position on the y axis.
	 */
	public void strokeText(String text, double x, double y) {
		this.gc.strokeText(text, doc2fxX(x), doc2fxY(y));
	}

	/**
	 * Draws text with stroke paint and includes a maximum width of the string.
	 * If the width of the text extends past max width, then it will be sized
	 * to fit.
	 * A {@code null} text value will be ignored.
	 *
	 * <p>This method will be affected by any of the
	 * global common, stroke, or text
	 * attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param text the string of text or null.
	 * @param x position on the x axis.
	 * @param y position on the y axis.
	 * @param maxWidth  maximum width the text string can have.
	 */
	public void strokeText(String text, double x, double y, double maxWidth) {
		this.gc.strokeText(text, doc2fxX(x), doc2fxY(y), doc2fxSize(maxWidth));
	}


	/**
	 * Set the filling rule attribute for determining the interior of paths
	 * in fill or clip operations.
	 * The default value is {@code FillRule.NON_ZERO}.
	 * A {@code null} value will be ignored and the current value will remain unchanged.
	 * The fill rule is a path attribute
	 * used for any of the fill or clip path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param fillRule {@code FillRule} with a value of  Even_odd or Non_zero or null.
	 */
	public void setFillRule(FillRule fillRule) {
		this.gc.setFillRule(fillRule);
	}

	/**
	 * Get the filling rule attribute for determining the interior of paths
	 * in fill and clip operations.
	 * The default value is {@code FillRule.NON_ZERO}.
	 * The fill rule is a path attribute
	 * used for any of the fill or clip path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @return current fill rule.
	 */
	public FillRule getFillRule() {
		return this.gc.getFillRule();
	}

	/**
	 * Resets the current path to empty.
	 * The default path is empty.
	 * The current path is a path attribute
	 * used for any of the path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}
	 * and <b>is not affected</b> by the {@link #save()} and
	 * {@link #restore()} operations.
	 */
	public void beginPath() {
		this.gc.beginPath();
	}

	/**
	 * Issues a move command for the current path to the given x,y coordinate.
	 * The coordinates are transformed by the current transform as they are
	 * added to the path and unaffected by subsequent changes to the transform.
	 * The current path is a path attribute
	 * used for any of the path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}
	 * and <b>is not affected</b> by the {@link #save()} and
	 * {@link #restore()} operations.
	 *
	 * @param x0 the X position for the move to command.
	 * @param y0 the Y position for the move to command.
	 */
	public void moveTo(double x0, double y0) {
		this.gc.moveTo(doc2fxX(x0), doc2fxY(y0));
	}

	/**
	 * Adds segments to the current path to make a line to the given x,y
	 * coordinate.
	 * The coordinates are transformed by the current transform as they are
	 * added to the path and unaffected by subsequent changes to the transform.
	 * The current path is a path attribute
	 * used for any of the path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}
	 * and <b>is not affected</b> by the {@link #save()} and
	 * {@link #restore()} operations.
	 *
	 * @param x1 the X coordinate of the ending point of the line.
	 * @param y1 the Y coordinate of the ending point of the line.
	 */
	public void lineTo(double x1, double y1) {
		this.gc.lineTo(doc2fxX(x1), doc2fxY(y1));
	}

	/**
	 * Adds segments to the current path to make a quadratic Bezier curve.
	 * The coordinates are transformed by the current transform as they are
	 * added to the path and unaffected by subsequent changes to the transform.
	 * The current path is a path attribute
	 * used for any of the path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}
	 * and <b>is not affected</b> by the {@link #save()} and
	 * {@link #restore()} operations.
	 *
	 * @param xc the X coordinate of the control point
	 * @param yc the Y coordinate of the control point
	 * @param x1 the X coordinate of the end point
	 * @param y1 the Y coordinate of the end point
	 */
	public void quadraticCurveTo(double xc, double yc, double x1, double y1) {
		this.gc.quadraticCurveTo(
				doc2fxX(xc), doc2fxY(yc),
				doc2fxX(x1), doc2fxY(y1));
	}

	/**
	 * Adds segments to the current path to make a cubic Bezier curve.
	 * The coordinates are transformed by the current transform as they are
	 * added to the path and unaffected by subsequent changes to the transform.
	 * The current path is a path attribute
	 * used for any of the path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}
	 * and <b>is not affected</b> by the {@link #save()} and
	 * {@link #restore()} operations.
	 *
	 * @param xc1 the X coordinate of first Bezier control point.
	 * @param yc1 the Y coordinate of the first Bezier control point.
	 * @param xc2 the X coordinate of the second Bezier control point.
	 * @param yc2 the Y coordinate of the second Bezier control point.
	 * @param x1  the X coordinate of the end point.
	 * @param y1  the Y coordinate of the end point.
	 */
	public void bezierCurveTo(double xc1, double yc1, double xc2, double yc2, double x1, double y1) {
		this.gc.bezierCurveTo(
				doc2fxX(xc1), doc2fxY(yc1),
				doc2fxX(xc2), doc2fxY(yc2),
				doc2fxX(x1), doc2fxY(y1));
	}

	/**
	 * Adds segments to the current path to make an arc.
	 * The coordinates are transformed by the current transform as they are
	 * added to the path and unaffected by subsequent changes to the transform.
	 * The current path is a path attribute
	 * used for any of the path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}
	 * and <b>is not affected</b> by the {@link #save()} and
	 * {@link #restore()} operations.
	 *
	 * <p>If {@code p0} is the current point in the path and {@code p1} is the
	 * point specified by {@code (x1, y1)} and {@code p2} is the point
	 * specified by {@code (x2, y2)}, then the arc segments appended will
	 * be segments along the circumference of a circle of the specified
	 * radius touching and inscribed into the convex (interior) side of
	 * {@code p0->p1->p2}.  The path will contain a line segment (if
	 * needed) to the tangent point between that circle and {@code p0->p1}
	 * followed by circular arc segments to reach the tangent point between
	 * the circle and {@code p1->p2} and will end with the current point at
	 * that tangent point (not at {@code p2}).
	 * Note that the radius and circularity of the arc segments will be
	 * measured or considered relative to the current transform, but the
	 * resulting segments that are computed from those untransformed
	 * points will then be transformed when they are added to the path.
	 * Since all computation is done in untransformed space, but the
	 * pre-existing path segments are all transformed, the ability to
	 * correctly perform the computation may implicitly depend on being
	 * able to inverse transform the current end of the current path back
	 * into untransformed coordinates.
	 *
	 * <p>If there is no way to compute and inscribe the indicated circle
	 * for any reason then the entire operation will simply append segments
	 * to force a line to point {@code p1}.  Possible reasons that the
	 * computation may fail include: <ul>
	 * <li>The current path is empty.</li>
	 * <li>The segments {@code p0->p1->p2} are colinear.</li>
	 * <li>the current transform is non-invertible so that the current end
	 * point of the current path cannot be untransformed for computation.</li>
	 * </ul>
	 *
	 * @param x1 the X coordinate of the first point of the arc.
	 * @param y1 the Y coordinate of the first point of the arc.
	 * @param x2 the X coordinate of the second point of the arc.
	 * @param y2 the Y coordinate of the second point of the arc.
	 * @param radius the radius of the arc in the range {0.0-positive infinity}.
	 */
	public void arcTo(double x1, double y1, double x2, double y2, double radius) {
		this.gc.arcTo(
				doc2fxX(x1), doc2fxY(y1),
				doc2fxX(x2), doc2fxY(y2),
				doc2fxSize(radius));
	}

	/**
	 * Adds path elements to the current path to make an arc that uses Euclidean
	 * radians. This Euclidean orientation sweeps from East to North, then West,
	 * then South, then back to East.
	 * The coordinates are transformed by the current transform as they are
	 * added to the path and unaffected by subsequent changes to the transform.
	 * The current path is a path attribute
	 * used for any of the path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}
	 * and <b>is not affected</b> by the {@link #save()} and
	 * {@link #restore()} operations.
	 *
	 * @param centerX the center x position of the arc.
	 * @param centerY the center y position of the arc.
	 * @param radiusX the x radius of the arc.
	 * @param radiusY the y radius of the arc.
	 * @param startAngle the starting angle of the arc in the range {@code 0-2PI}
	 * @param length  the length of the baseline of the arc.
	 */
	public void arc(double centerX, double centerY,
			double radiusX, double radiusY,
			double startAngle, double length) {
		this.gc.arc(
				doc2fxX(centerX), doc2fxY(centerY),
				doc2fxSize(radiusX), doc2fxSize(radiusY),
				doc2fxAngle(Math.toDegrees(startAngle)), doc2fxSize(length));
	}

	/**
	 * Adds path elements to the current path to make a rectangle.
	 * The coordinates are transformed by the current transform as they are
	 * added to the path and unaffected by subsequent changes to the transform.
	 * The current path is a path attribute
	 * used for any of the path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}
	 * and <b>is not affected</b> by the {@link #save()} and
	 * {@link #restore()} operations.
	 *
	 * @param x x position of the upper left corner of the rectangle.
	 * @param y y position of the upper left corner of the rectangle.
	 * @param width width of the rectangle.
	 * @param height height of the rectangle.
	 */
	public void rect(double x, double y, double width, double height) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.rect(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height));
	}

	/**
	 * Closes the path.
	 * The current path is a path attribute
	 * used for any of the path methods as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}
	 * and <b>is not affected</b> by the {@link #save()} and
	 * {@link #restore()} operations.
	 */
	public void closePath() {
		this.gc.closePath();
	}

	/**
	 * Fills the path with the current fill paint.
	 *
	 * <p>This method will be affected by any of the
	 * global common, fill, or path
	 * attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * Note that the path segments were transformed as they were originally
	 * added to the current path so the current transform will not affect
	 * those path segments again, but it may affect other attributes in
	 * affect at the time of the {@code fill()} operation.
	 */
	public void fill() {
		this.gc.fill();
	}

	/**
	 * Strokes the path with the current stroke paint.
	 *
	 * <p>This method will be affected by any of the
	 * global common, stroke, or path
	 * attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * Note that the path segments were transformed as they were originally
	 * added to the current path so the current transform will not affect
	 * those path segments again, but it may affect other attributes in
	 * affect at the time of the {@code stroke()} operation.
	 */
	public void stroke() {
		this.gc.stroke();
	}

	/**
	 * Intersects the current clip with the current path and applies it to
	 * subsequent rendering operation as an anti-aliased mask.
	 * The current clip is a common attribute
	 * used for nearly all rendering operations as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * <p>This method will itself be affected only by the
	 * path attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * Note that the path segments were transformed as they were originally
	 * added to the current path so the current transform will not affect
	 * those path segments again, but it may affect other attributes in
	 * affect at the time of the {@code stroke()} operation.
	 */
	public void clip() {
		this.gc.clip();
	}

	/**
	 * Clears a portion of the canvas with a transparent color value.
	 *
	 * <p>This method will be affected only by the current transform, clip,
	 * and effect.
	 *
	 * @param x X position of the upper left corner of the rectangle.
	 * @param y Y position of the upper left corner of the rectangle.
	 * @param width width of the rectangle.
	 * @param height height of the rectangle.
	 */
	public void clearRect(double x, double y, double width, double height) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.clearRect(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height));
	}

	/**
	 * Fills a rectangle using the current fill paint.
	 *
	 * <p>This method will be affected by any of the
	 * global common or fill attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param x the X position of the upper left corner of the rectangle.
	 * @param y the Y position of the upper left corner of the rectangle.
	 * @param width the width of the rectangle.
	 * @param height the height of the rectangle.
	 */
	public void fillRect(double x, double y, double width, double height) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.fillRect(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height));
	}

	/**
	 * Strokes a rectangle using the current stroke paint.
	 *
	 * <p>This method will be affected by any of the
	 * global common or stroke attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param x the X position of the upper left corner of the rectangle.
	 * @param y the Y position of the upper left corner of the rectangle.
	 * @param width the width of the rectangle.
	 * @param height the height of the rectangle.
	 */
	public void strokeRect(double x, double y, double width, double height) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.strokeRect(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height));
	}

	/**
	 * Fills an oval using the current fill paint.
	 *
	 * <p>This method will be affected by any of the
	 * global common or fill attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param x the X coordinate of the upper left bound of the oval.
	 * @param y the Y coordinate of the upper left bound of the oval.
	 * @param width the width at the center of the oval.
	 * @param height the height at the center of the oval.
	 */
	public void fillOval(double x, double y, double width, double height) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.fillOval(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height));
	}

	/**
	 * Strokes an oval using the current stroke paint.
	 *
	 * <p>This method will be affected by any of the
	 * global common or stroke
	 * attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param x the X coordinate of the upper left bound of the oval.
	 * @param y the Y coordinate of the upper left bound of the oval.
	 * @param width the width at the center of the oval.
	 * @param height the height at the center of the oval.
	 */
	public void strokeOval(double x, double y, double width, double height) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.strokeOval(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height));
	}

	/**
	 * Fills an arc using the current fill paint. A {@code null} ArcType or
	 * non positive width or height will cause the render command to be ignored.
	 *
	 * <p>This method will be affected by any of the
	 * global common or fill attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 * </p>
	 *
	 * @param x the X coordinate of the arc.
	 * @param y the Y coordinate of the arc.
	 * @param width the width of the arc.
	 * @param height the height of the arc.
	 * @param startAngle the starting angle of the arc in radians.
	 * @param arcExtent the angular extent of the arc in radians.
	 * @param closure closure type (Round, Chord, Open) or null.
	 */
	public void fillArc(double x, double y, double width, double height,
			double startAngle, double arcExtent, ArcType closure) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.fillArc(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height),
				doc2fxAngle(Math.toDegrees(startAngle)),
				doc2fxAngle(Math.toDegrees(arcExtent)),
				closure);
	}

	/**
	 * Strokes an Arc using the current stroke paint. A {@code null} ArcType or
	 * non positive width or height will cause the render command to be ignored.
	 *
	 * <p>This method will be affected by any of the
	 * global common or stroke attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param x the X coordinate of the arc.
	 * @param y the Y coordinate of the arc.
	 * @param width the width of the arc.
	 * @param height the height of the arc.
	 * @param startAngle the starting angle of the arc in radians.
	 * @param arcExtent arcExtent the angular extent of the arc in radians.
	 * @param closure closure type (Round, Chord, Open) or null
	 */
	public void strokeArc(double x, double y, double width, double height,
			double startAngle, double arcExtent, ArcType closure) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.strokeArc(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height),
				doc2fxAngle(Math.toDegrees(startAngle)),
				doc2fxAngle(Math.toDegrees(arcExtent)),
				closure);
	}

	/**
	 * Fills a rounded rectangle using the current fill paint.
	 *
	 * <p>This method will be affected by any of the
	 * global common or fill attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param x the X coordinate of the upper left bound of the oval.
	 * @param y the Y coordinate of the upper left bound of the oval.
	 * @param width the width at the center of the oval.
	 * @param height the height at the center of the oval.
	 * @param arcWidth the arc width of the rectangle corners.
	 * @param arcHeight the arc height of the rectangle corners.
	 */
	public void fillRoundRect(double x, double y, double width, double height, double arcWidth, double arcHeight) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.fillRoundRect(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height),
				doc2fxSize(arcWidth), doc2fxSize(arcHeight));
	}

	/**
	 * Strokes a rounded rectangle using the current stroke paint.
	 *
	 * <p>This method will be affected by any of the
	 * global common or stroke attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param x the X coordinate of the upper left bound of the oval.
	 * @param y the Y coordinate of the upper left bound of the oval.
	 * @param width the width at the center of the oval.
	 * @param height the height at the center of the oval.
	 * @param arcWidth the arc width of the rectangle corners.
	 * @param arcHeight the arc height of the rectangle corners.
	 */
	public void strokeRoundRect(double x, double y, double width, double height,
			double arcWidth, double arcHeight) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.strokeRoundRect(
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height),
				doc2fxSize(arcWidth), doc2fxSize(arcHeight));
	}

	/**
	 * Strokes a line using the current stroke paint.
	 *
	 * <p>This method will be affected by any of the
	 * global common or stroke attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param x1 the X coordinate of the starting point of the line.
	 * @param y1 the Y coordinate of the starting point of the line.
	 * @param x2 the X coordinate of the ending point of the line.
	 * @param y2 the Y coordinate of the ending point of the line.
	 */
	public void strokeLine(double x1, double y1, double x2, double y2) {
		this.gc.strokeLine(
				doc2fxX(x1), doc2fxY(y1),
				doc2fxX(x2), doc2fxY(y2));
	}

	/**
	 * Draws an image at the given x, y position using the width
	 * and height of the given image.
	 * A {@code null} image value or an image still in progress will be ignored.
	 *
	 * <p>This method will be affected by any of the
	 * global common attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param img the image to be drawn or null.
	 * @param x the X coordinate on the destination for the upper left of the image.
	 * @param y the Y coordinate on the destination for the upper left of the image.
	 */
	public void drawImage(Image img, double x, double y) {
		final Point2d base = doc2fxRectBase(x, y, fx2docSize(img.getWidth()), fx2docSize(img.getHeight()));
		this.gc.drawImage(img, base.getX(), base.getY());
	}

	/**
	 * Draws an image into the given destination rectangle of the canvas. The
	 * Image is scaled to fit into the destination rectangle.
	 * A {@code null} image value or an image still in progress will be ignored.
	 *
	 * <p>This method will be affected by any of the
	 * global common attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param img the image to be drawn or null.
	 * @param x the X coordinate on the destination for the upper left of the image.
	 * @param y the Y coordinate on the destination for the upper left of the image.
	 * @param width the width of the destination rectangle.
	 * @param height the height of the destination rectangle.
	 */
	public void drawImage(Image img, double x, double y, double width, double height) {
		final Point2d base = doc2fxRectBase(x, y, width, height);
		this.gc.drawImage(img,
				base.getX(), base.getY(),
				doc2fxSize(width), doc2fxSize(height));
	}

	/**
	 * Draws the specified source rectangle of the given image to the given
	 * destination rectangle of the Canvas.
	 * A {@code null} image value or an image still in progress will be ignored.
	 *
	 * <p>This method will be affected by any of the
	 * global common attributes as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param img the image to be drawn or null.
	 * @param sx the source rectangle's X coordinate position.
	 * @param sy the source rectangle's Y coordinate position.
	 * @param sw the source rectangle's width.
	 * @param sh the source rectangle's height.
	 * @param dx the destination rectangle's X coordinate position.
	 * @param dy the destination rectangle's Y coordinate position.
	 * @param dw the destination rectangle's width.
	 * @param dh the destination rectangle's height.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	public void drawImage(Image img,
			double sx, double sy, double sw, double sh,
			double dx, double dy, double dw, double dh) {
		final Point2d base = doc2fxRectBase(dx, dy, dw, dh);
		this.gc.drawImage(img,
				sx, sy, sw, sh,
				base.getX(), base.getY(),
				doc2fxSize(dw), doc2fxSize(dh));
	}

	/**
	 * Returns a {@link PixelWriter} object that can be used to modify
	 * the pixels of the {@link Canvas} associated with this
	 * {@code GraphicsContext}.
	 * All coordinates in the {@code PixelWriter} methods on the returned
	 * object will be in device space since they refer directly to pixels
	 * and no other rendering attributes will be applied when modifying
	 * pixels using this object.
	 *
	 * @return the {@code PixelWriter} for modifying the pixels of this
	 *         {@code Canvas}
	 */
	public PixelWriter getPixelWriter() {
		return this.gc.getPixelWriter();
	}

	/**
	 * Sets the effect to be applied after the next draw call, or null to
	 * disable effects.
	 * The current effect is a common attribute
	 * used for nearly all rendering operations as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param effect the effect to use, or null to disable effects
	 */
	public void setEffect(Effect effect) {
		this.gc.setEffect(effect);
	}

	/**
	 * Gets a copy of the effect to be applied after the next draw call.
	 * A null return value means that no effect will be applied after subsequent
	 * rendering calls.
	 * The current effect is a common attribute
	 * used for nearly all rendering operations as specified in the
	 * Rendering Attributes Table of {@link GraphicsContext}.
	 *
	 * @param effect an {@code Effect} object that may be used to store the
	 *        copy of the current effect, if it is of a compatible type
	 * @return the current effect used for all rendering calls,
	 *         or null if there is no current effect
	 */
	public Effect getEffect(Effect effect) {
		return this.gc.getEffect(effect);
	}

	/**
	 * Applies the given effect to the entire bounds of the canvas and stores
	 * the result back into the same canvas.
	 * A {@code null} value will be ignored.
	 * The effect will be applied without any other rendering attributes and
	 * under an Identity coordinate transform.
	 * Since the effect is applied to the entire bounds of the canvas, some
	 * effects may have a confusing result, such as a Reflection effect
	 * that will apply its reflection off of the bottom of the canvas even if
	 * only a portion of the canvas has been rendered to and will not be
	 * visible unless a negative offset is used to bring the reflection back
	 * into view.
	 *
	 * @param effect the effect to apply onto the entire destination or null.
	 */
	public void applyEffect(Effect effect) {
		this.gc.applyEffect(effect);
	}

}
