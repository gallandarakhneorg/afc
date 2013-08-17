/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.ui.android.zoom;

import java.util.EventListener;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.RoundRectangle2f;
import org.arakhne.afc.math.continous.object2d.Segment2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.ui.CenteringTransform;
import org.arakhne.afc.ui.ZoomableContext;
import org.arakhne.afc.ui.ZoomableContextUtil;
import org.arakhne.afc.ui.android.R;
import org.arakhne.afc.ui.event.PointerEvent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/** This abstract view provides the tools to move and scale
 * the view. It is abstract because it does not draw anything. 
 * <p>
 * The implementation of the ZoomableView handles the pointer and key events as following:
 * <table>
 * <head>
 * <tr><th>Event</th><th>Status</th><th>Callback</th><th>Note</th></tr>
 * </head>
 * <tr><td>POINTER_PRESSED</td><td>supported</td><td>{@link #onPointerPressed(PointerEvent)}</td><td>Allways called</td></tr>
 * <tr><td>POINTER_DRAGGED</td><td>supported</td><td>{@link #onPointerDragged(PointerEvent)}</td><td>Called only when the scale and move gestures are not in progress</td></tr>
 * <tr><td>POINTER_RELEASED</td><td>supported</td><td>{@link #onPointerReleased(PointerEvent)}</td><td>Called only when the scale and move gestures are not in progress</td></tr>
 * <tr><td>POINTER_MOVED</td><td>not supported</td><td></td><td>Pointer move on a touch screen cannot be detected?</td></tr>
 * <tr><td>POINTER_CLICK</td><td>not supported</td><td></td><td>See {@link #setOnClickListener(OnClickListener)}</td></tr>
 * <tr><td>POINTER_LONG_CLICK</td><td>not supported</td><td></td><td>See {@link #setOnLongClickListener(OnLongClickListener)}</td></tr>
 * <tr><td>KEY_PRESSED</td><td>not supported</td><td></td><td>See {@link #onKeyDown(int, android.view.KeyEvent)}</td></tr>
 * <tr><td>KEY_RELEASED</td><td>not supported</td><td></td><td>See {@link #onKeyUp(int, android.view.KeyEvent)}</td></tr>
 * <tr><td>KEY_TYPED</td><td>not supported</td><td></td><td>See {@link #onKeyUp(int, android.view.KeyEvent)}</td></tr>
 * <body>
 * </body>
 * </table>
 * <p>
 * The function #@link {@link #onDrawView(Canvas, float, CenteringTransform)}} may
 * use an instance of the graphical context {@link DroidZoomableGraphics2D} to draw
 * the elements according to the zooming attributes.
 * 
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see DroidZoomableGraphics2D
 */
public abstract class ZoomableView extends View implements ZoomableContext {

	/** Current position of the workspace.
	 * This position permits to scroll the view.
	 */
	private float focusX = 0f;

	/** Current position of the workspace.
	 * This position permits to scroll the view.
	 */
	private float focusY = 0f;

	/** Current scaling factor.
	 */
	private float scaleFactor = 1.f;

	/** Minimal scaling factor.
	 */
	private float minScaleFactor = 0.001f;

	/** Maximal scaling factor.
	 */
	private float maxScaleFactor = 100.f;

	/** Handler of events.
	 */
	private final EventListener globalListener;

	/** Zooming sensibility.
	 */
	private float zoomingSensitivity = 1.5f;

	/** Invert the scrolling direction.
	 */
	private boolean isInvertScrollingDirection = false;

	/** Indicates if the repaint requests are ignored.
	 */
	private boolean isIgnoreRepaint = false;
	
	/** Transformation to center the view used when rendering the view.
	 */
	private final CenteringTransform centeringTransform = new CenteringTransform();

	/** Indicates if the X axis is inverted or not.
	 */
	private boolean isXAxisInverted = false;
	
	/** Indicates if the Y axis is inverted or not.
	 */
	private boolean isYAxisInverted = false;

	/** Manager of touch events.
	 */
	private final TouchManager touchManager;
	
	/**
	 * @param context is the droid context of the view.
	 */
	public ZoomableView(Context context) {
		this(context, null, 0);
	}

	/**
	 * @param context is the droid context of the view.
	 * @param attrs are the attributes of the view.
	 */
	public ZoomableView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context is the droid context of the view.
	 * @param attrs are the attributes of the view.
	 * @param defStyle is the style of the view.
	 */
	public ZoomableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.globalListener = createEventHandler();
		this.touchManager = new TouchManager(this);
		this.touchManager.init();
	}
	
	@Override
	public final boolean isXAxisInverted() {
		return this.isXAxisInverted;
	}
	
	@Override
	public final boolean isYAxisInverted() {
		return this.isYAxisInverted;
	}

	/** Invert or not the X axis.
	 * <p>
	 * If the X axis is inverted, the positives are to the left;
	 * otherwise they are to the right (default UI standard).
	 * 
	 * @param invert
	 */
	public final void setXAxisInverted(boolean invert) {
		if (invert!=this.isXAxisInverted) {
			this.isXAxisInverted = invert;
			onUpdateViewParameters();
			repaint();
		}
	}
	
	/** Invert or not the Y axis.
	 * <p>
	 * If the Y axis is inverted, the positives are to the left;
	 * otherwise they are to the right (default UI standard).
	 * 
	 * @param invert
	 */
	public final void setYAxisInverted(boolean invert) {
		if (invert!=this.isYAxisInverted) {
			this.isYAxisInverted = invert;
			onUpdateViewParameters();
			repaint();
		}
	}

	@Override
	public final void setOnLongClickListener(OnLongClickListener l) {
		super.setOnLongClickListener(this.touchManager.setOnLongClickListener(l));
	}

	@Override
	public final void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(this.touchManager.setOnClickListener(l));
	}

	/** Set if the repaint requests are ignored or not.
	 * 
	 * @param ignore
	 */
	public final void setIgnoreRepaint(boolean ignore) {
		this.isIgnoreRepaint = ignore;
	}

	/** Replies if the repaint requests are ignored or not.
	 * 
	 * @return <code>true</code> if the repaint requests are ignored;
	 * <code>false</code> if not.
	 */
	public final boolean isIgnoreRepaint() {
		return this.isIgnoreRepaint;
	}

	/** Invalidate this view wherever this function is invoked.
	 * If the current thread is the UI-thread, {@link #invalidate()}
	 * is invoked. If not, {@link #postInvalidate()} is invoked.
	 * <p>
	 * If {@link #isIgnoreRepaint()} replies <code>false</code>, this
	 * function does nothing.
	 * 
	 * @see #isIgnoreRepaint()
	 */
	public final void repaint() {
		if (!this.isIgnoreRepaint) {
			if (Looper.getMainLooper() == Looper.myLooper()) {
				invalidate();
			}
			else {
				postInvalidate();
			}
		}
	}

	/** Invalidate this view wherever this function is invoked.
	 * If the current thread is the UI-thread, {@link #invalidate()}
	 * is invoked. If not, {@link #postInvalidate()} is invoked.
	 * <p>
	 * If {@link #isIgnoreRepaint()} replies <code>false</code>, this
	 * function does nothing.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @see #isIgnoreRepaint()
	 */
	public final void repaint(float x, float y, float width, float height) {
		if (!this.isIgnoreRepaint) {
			int l = (int)logical2pixel_x(x)-5;
			int t = (int)logical2pixel_y(y)-5;
			int r = l + (int)logical2pixel_size(width)+10;
			int b = t + (int)logical2pixel_size(height)+10;
			if (Looper.getMainLooper() == Looper.myLooper()) {
				invalidate(l, t, r, b);
			}
			else {
				postInvalidate(l, t, r, b);
			}
		}
	}

	/** Invalidate this view wherever this function is invoked.
	 * If the current thread is the UI-thread, {@link #invalidate()}
	 * is invoked. If not, {@link #postInvalidate()} is invoked.
	 * <p>
	 * If {@link #isIgnoreRepaint()} replies <code>false</code>, this
	 * function does nothing.
	 * 
	 * @param r
	 * @see #isIgnoreRepaint()
	 */
	public final void repaint(Rectangle2f r) {
		if (r!=null)
			repaint(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	/** Invalidate this view wherever this function is invoked.
	 * If the current thread is the UI-thread, {@link #invalidate()}
	 * is invoked. If not, {@link #postInvalidate()} is invoked.
	 * <p>
	 * If {@link #isIgnoreRepaint()} replies <code>false</code>, this
	 * function does nothing.
	 * 
	 * @param r
	 * @see #isIgnoreRepaint()
	 */
	public final void repaint(Rect r) {
		if (r!=null && !this.isIgnoreRepaint) {
			if (Looper.getMainLooper() == Looper.myLooper()) {
				invalidate(r.left, r.top, r.right, r.bottom);
			}
			else {
				postInvalidate(r.left, r.top, r.right, r.bottom);
			}
		}
	}

	/** Invalidate this view wherever this function is invoked.
	 * If the current thread is the UI-thread, {@link #invalidate()}
	 * is invoked. If not, {@link #postInvalidate()} is invoked.
	 * <p>
	 * If {@link #isIgnoreRepaint()} replies <code>false</code>, this
	 * function does nothing.
	 * 
	 * @param r
	 * @see #isIgnoreRepaint()
	 */
	public final void repaint(RectF r) {
		if (r!=null) {
			repaint(r.left, r.top, r.right, r.bottom);
		}
	}

	/** Invoked to create the instance of the event handler that must
	 * be used by this view.
	 * 
	 * @return the event handler.
	 */
	protected abstract EventListener createEventHandler();

	/** Replies the event handler of the given type.
	 * 
	 * @param type
	 * @return the event handler of the given type.
	 */
	public final <T> T getEventHandler(Class<T> type) {
		if (this.globalListener!=null && type.isInstance(this.globalListener))
			return type.cast(this.globalListener);
		throw new IllegalStateException(type.toString());
	}

	/** Replies if the direction of moving is inverted.
	 * 
	 * @return <code>true</code> if the direction of moving
	 * is inverted; otherwise <code>false</code>.
	 */
	public final boolean isMoveDirectionInverted() {
		return this.isInvertScrollingDirection;
	}

	/** Set if the direction of moving is inverted.
	 * 
	 * @param invert is <code>true</code> if the direction of moving
	 * is inverted; otherwise <code>false</code>.
	 */
	public final void setMoveDirectionInverted(boolean invert) {
		this.isInvertScrollingDirection = invert;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float logical2pixel_size(float l) {
		/*float s = l * this.scaleFactor;
		if ((l!=0)&&(s==0f)) s = 1f;
		return s;*/
		return ZoomableContextUtil.logical2pixel_size(l, this.scaleFactor);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float logical2pixel_x(float l) {
		/*float dx = l - this.focusX;
		dx *= getScalingFactor();
		return getViewportCenterX() + dx;*/
		return ZoomableContextUtil.logical2pixel_x(l, this.centeringTransform, this.scaleFactor);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float logical2pixel_y(float l) {
		/*float dy = l - this.focusY;
		dy *= getScalingFactor();
		return getViewportCenterY() + dy;*/
		return ZoomableContextUtil.logical2pixel_y(l, this.centeringTransform, this.scaleFactor);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float pixel2logical_size(float l) {
		//return l / this.scaleFactor;
		return ZoomableContextUtil.pixel2logical_size(l, this.scaleFactor);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float pixel2logical_x(float l) {
		/*float dx = l - getViewportCenterX();
		dx /= getScalingFactor();
		return this.focusX + dx;*/
		return ZoomableContextUtil.pixel2logical_x(l, this.centeringTransform, this.scaleFactor);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float pixel2logical_y(float l) {
		/*float dy = l - getViewportCenterY();
		dy /= getScalingFactor();
		return this.focusY + dy;*/
		return ZoomableContextUtil.pixel2logical_y(l, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public PathIterator2f logical2pixel(PathIterator2f p) {
		return ZoomableContextUtil.logical2pixel(p, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public PathIterator2f pixel2logical(PathIterator2f p) {
		return ZoomableContextUtil.pixel2logical(p, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void logical2pixel(Segment2f s) {
		ZoomableContextUtil.logical2pixel(s, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void pixel2logical(Segment2f s) {
		ZoomableContextUtil.pixel2logical(s, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void logical2pixel(RoundRectangle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void pixel2logical(RoundRectangle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void logical2pixel(Point2f p) {
		ZoomableContextUtil.logical2pixel(p, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void pixel2logical(Point2f p) {
		ZoomableContextUtil.pixel2logical(p, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void logical2pixel(Ellipse2f e) {
		ZoomableContextUtil.logical2pixel(e, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void pixel2logical(Ellipse2f e) {
		ZoomableContextUtil.pixel2logical(e, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void logical2pixel(Circle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void pixel2logical(Circle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void logical2pixel(Rectangle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public void pixel2logical(Rectangle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public Shape2f logical2pixel(Shape2f s) {
		return ZoomableContextUtil.logical2pixel(s, this.centeringTransform, this.scaleFactor);
	}

	@Override
	public Shape2f pixel2logical(Shape2f s) {
		return ZoomableContextUtil.pixel2logical(s, this.centeringTransform, this.scaleFactor);
	}


	/** {@inheritDoc}
	 */
	@Override
	public final float getScalingSensitivity() {
		return this.zoomingSensitivity;
	}

	/** Replies the sensivility of the {@code zoomIn()}
	 * and {@code zoomOut()} actions.
	 * 
	 * @param sensivility
	 */
	public final void setScalingSensitivity(float sensivility) {
		this.zoomingSensitivity = Math.max(sensivility, Float.MIN_NORMAL);
	}
	
	/** Replies the X coordinate of the center of the viewport (in screen coordinate).
	 * 
	 * @return the center of the viewport.
	 */
	public final float getViewportCenterX() {
		return getMeasuredWidth()/2f;
	}

	/** Replies the Y coordinate of the center of the viewport (in screen coordinate).
	 * 
	 * @return the center of the viewport.
	 */
	public final float getViewportCenterY() {
		return getMeasuredHeight()/2f;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getFocusX() {
		return pixel2logical_x(getViewportCenterX());
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getFocusY() {
		return pixel2logical_y(getViewportCenterY());
	}


	/** {@inheritDoc}
	 */
	@Override
	public final float getScalingFactor() {
		return this.scaleFactor;
	}

	/** Set the scaling factor.
	 * 
	 * @param factor is the scaling factor.
	 * @return <code>true</code> if the scaling factor has changed;
	 * otherwise <code>false</code>.
	 */
	public final boolean setScalingFactor(float factor) {
		if (setScalingFactorAndFocus(Float.NaN, Float.NaN, factor)) {
			repaint();
			return true;
		}
		return false;
	}

	/** Set the scaling factor. This function does not repaint.
	 *
	 * @param scalingX is the coordinate of the point (on the screen) where the focus occurs.
	 * @param scalingY is the coordinate of the point (on the screen) where the focus occurs.
	 * @param factor is the scaling factor.
	 * @return <code>true</code> if the scaling factor or the focus point has changed;
	 * otherwise <code>false</code>.
	 */
	protected final boolean setScalingFactorAndFocus(float scalingX, float scalingY, float factor) {
		// Normalize the scaling factor
		float normalizedFactor = factor;
		if (normalizedFactor<this.minScaleFactor)
			normalizedFactor = this.minScaleFactor;
		if (normalizedFactor>this.maxScaleFactor)
			normalizedFactor = this.maxScaleFactor;
		
		// Determine the new position of the focus.
		// The new position of the focus depends on the current position,
		// the new scaling factor and where the scaling occured.
		if (!Float.isNaN(scalingX) && !Float.isNaN(scalingY)) {
			// Get screen coordinates
			float screenCenterX = getViewportCenterX();
			float screenCenterY = getViewportCenterY();
			float vectorToScreenCenterX = screenCenterX - scalingX;
			float vectorToScreenCenterY = screenCenterY - scalingY;

			// Get logical coordinates
			float sX = pixel2logical_x(scalingX);
			float sY = pixel2logical_y(scalingY);

			float newX = sX + vectorToScreenCenterX / normalizedFactor;
			float newY = sY + vectorToScreenCenterY / normalizedFactor;
			
			if (normalizedFactor!=this.scaleFactor || newX!=this.focusX || newY!=this.focusY) {
				this.scaleFactor = normalizedFactor;
				this.focusX = newX;
				this.focusY = newY;
				onUpdateViewParameters();
				return true;
			}
		}
		else if (normalizedFactor!=this.scaleFactor) {
			this.scaleFactor = normalizedFactor;
			onUpdateViewParameters();
			return true;
		}
		
		return false;
	}

	/** Zoom the view in.
	 */
	public final void zoomIn() {
		if (onScale(getFocusX(), getFocusY(), getScalingSensitivity())) {
			repaint();
		}
	}

	/** Zoom the view out.
	 */
	public final void zoomOut() {
		if (onScale(getFocusX(), getFocusY(), 1f/getScalingSensitivity())) {
			repaint();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getMaxScalingFactor() {
		return this.maxScaleFactor;
	}

	/** Set the maximal scaling factor allowing in the view
	 * 
	 * @param factor is the maximal scaling factor.
	 */
	public final void setMaxScalingFactor(float factor) {
		if (factor>0f && factor!=this.maxScaleFactor) {
			this.maxScaleFactor = factor;
			if (this.minScaleFactor>this.maxScaleFactor)
				this.minScaleFactor = this.maxScaleFactor;
			if (this.scaleFactor>this.maxScaleFactor)
				this.scaleFactor = this.maxScaleFactor;
			repaint();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final float getMinScalingFactor() {
		return this.minScaleFactor;
	}

	/** Set the minimal scaling factor allowing in the view
	 * 
	 * @param factor is the minimal scaling factor.
	 */
	public final void setMinScalingFactor(float factor) {
		if (factor>0f && factor!=this.minScaleFactor) {
			this.minScaleFactor = factor;
			if (this.maxScaleFactor<this.minScaleFactor)
				this.maxScaleFactor = this.minScaleFactor;
			if (this.scaleFactor<this.minScaleFactor)
				this.scaleFactor = this.minScaleFactor;
			repaint();
		}
	}

	/** Set the position of the focus point.
	 * 
	 * @param x
	 * @param y
	 */
	public final void setFocusPoint(float x, float y) {
		if (this.focusX!=x || this.focusY!=y) {
			this.focusX = x;
			this.focusY = y;
			onUpdateViewParameters();
			repaint();
		}
	}

	/** Translate the position of the focus point.
	 * 
	 * @param dx
	 * @param dy
	 */
	public final void translateFocusPoint(float dx, float dy) {
		if (dx!=0f || dy!=0f) {
			this.focusX += dx;
			this.focusY += dy;
			onUpdateViewParameters();
			repaint();
		}
	}

	/** Update any viewing parameter according to the
	 * current value of the focus point and the scaling factor.
	 * <p>
	 * This function is invoked when the coordinates of the
	 * focus point or the scaling factor has been changed to
	 * ensure that all the drawing attributes are properly set.
	 */
	protected void onUpdateViewParameters() {
		/*float sf = getScalingFactor();
		float w = getMeasuredWidth() / sf;
		float h = getMeasuredHeight() / sf;
		this.translateToCenterX = -(getFocusX() - w/2f);
		this.translateToCenterY = -(getFocusY() - h/2f);*/
		float t;
		
		t = pixel2logical_size(getViewportCenterX());
		if (isXAxisInverted()) {
    		this.centeringTransform.setCenteringX(
    				true,
    				-1,
    				t + this.focusX);
    	}
    	else {
    		this.centeringTransform.setCenteringX(
    				false,
    				1,
    				t - this.focusX);
    	}
		
		t = pixel2logical_size(getViewportCenterY());
		if (isYAxisInverted()) {
    		this.centeringTransform.setCenteringY(
    				true,
    				-1,
    				t + this.focusY);
    	}
    	else {
    		this.centeringTransform.setCenteringY(
    				false,
    				1,
    				t - this.focusY);
    	}
	}

	/** Replies the preferred position of the focus point.
	 * 
	 * @return the preferred position of the focus point.
	 */
	protected abstract float getPreferredFocusX();

	/** Replies the preferred position of the focus point.
	 * 
	 * @return the preferred position of the focus point.
	 */
	protected abstract float getPreferredFocusY();

	/** Reset the view to the default configuration.
	 * 
	 * @return <code>true</code> if the view has changed; <code>false</code> otherwise.
	 */
	public final boolean resetView() {
		float px = getPreferredFocusX();
		float py = getPreferredFocusY();
		if (this.focusX!=px || this.focusY!=py || getScalingFactor()!=1f) {
			this.focusX = px;
			this.focusY = py;
			if (!setScalingFactor(1f)) {
				onUpdateViewParameters();
				repaint(); // Force to refresh the UI
			}

			toast(getContext().getString(R.string.reset_view_done), false);

			return true;
		}
		return false;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		onUpdateViewParameters();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(!isInEditMode()) {
			onDrawView(canvas, this.scaleFactor, this.centeringTransform);
		}
	}

	/** Invoked to paint the view after it is translated and scaled.
	 * 
	 * @param canvas is the canvas in which the view must be painted.
	 * @param scaleFactor is the scaling factor to use for drawing.
	 * @param centeringTransform is the transform to use to put the draws at the center of the view.
	 */
	protected abstract void onDrawView(Canvas canvas, float scaleFactor, CenteringTransform centeringTransform);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean onTouchEvent(MotionEvent ev) {
		byte c = this.touchManager.onTouchEvent(ev);
		if ((c&2)!=0) {
			if (super.onTouchEvent(ev)) {
				c |= 1;
			}
		}
		return ((c&1)!=0);
	}

	/** Invoked when the a touch-down event is detected.
	 * 
	 * @param e
	 */
	protected void onPointerPressed(PointerEvent e) {
		//
	}

	/** Invoked when the a touch-up event is detected.
	 * 
	 * @param e
	 */
	protected void onPointerReleased(PointerEvent e) {
		//
	}

	/** Invoked when the pointer is moved with a button down.
	 * 
	 * @param e
	 */
	protected void onPointerDragged(PointerEvent e) {
		//
	}

	/** Invoked when a long-click is detected.
	 * 
	 * @param e
	 */
	protected void onLongClick(PointerEvent e) {
		//
	}

	/** Invoked when a short-click is detected.
	 * 
	 * @param e
	 */
	protected void onClick(PointerEvent e) {
		//
	}

	/**
	 * Invoked when the view must be scaled.
	 * <p>
	 * One of the border effect if this function replies <code>true</code>
	 * is that the view will be repaint.
	 * <p>
	 * The default implementation of this function invokes
	 * {@link #setScalingFactorAndFocus(float, float, float)}.
	 * 
	 * @param focusX is the position of the focal point on the screen.
	 * @param focusY is the position of the focal point on the screen.
	 * @param requestedScaleFactor is the new scale factor.
	 * @return Whether or not the detector should consider this event as handled. 
	 * If an event was not handled, the detector will continue to accumulate movement 
	 * until an event is handled. This can be useful if an application, for example, 
	 * only wants to update scaling factors if the change is greater than 0.01.
	 * @see #setScalingFactorAndFocus(float, float, float)
	 */
	public boolean onScale(float focusX, float focusY, float requestedScaleFactor) {
		setScalingFactorAndFocus(focusX, focusY, this.scaleFactor * requestedScaleFactor);
		return true;
	}

	/** Show a toast message.
	 * 
	 * @param message is the message to display
	 * @param isLong indicates if it is a long-time (<code>true</code>)
	 * or a short-time (<code>false</code>) message. 
	 */
	public final void toast(String message, boolean isLong) {
		Context context = getContext();
		if (context instanceof Activity) {
			((Activity)context).runOnUiThread(new AsynchronousToaster(context, message, isLong));
		}
	}

	/** Show a toast message.
	 * 
	 * @param message is the message to display
	 * @param isLong indicates if it is a long-time (<code>true</code>)
	 * or a short-time (<code>false</code>) message. 
	 */
	public final void toast(int message, boolean isLong) {
		Context context = getContext();
		if (context instanceof Activity) {
			((Activity)context).runOnUiThread(new AsynchronousToaster(context, message, isLong));
		}
	}

	/** This class permits to display a toast message outside
	 * the main UI thread.
	 * <p>
	 * A toast message is a small notification message to put on the UI.
	 * The location of this message depends on the Android API.
	 * 
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @see Toast
	 */
	private static class AsynchronousToaster implements Runnable {

		private final Context context;
		private final String message;
		private final int messageId;
		private final boolean isLong;

		/**
		 * @param context
		 * @param message is the message to toast
		 * @param isLong indicates if it is a long-time (<code>true</code>)
		 * or a short-time (<code>false</code>) message. 
		 */
		public AsynchronousToaster(Context context, String message, boolean isLong) {
			this.context = context;
			this.message = message;
			this.messageId = -1;
			this.isLong = isLong;
		}

		/**
		 * @param context
		 * @param message is the message to toast
		 * @param isLong indicates if it is a long-time (<code>true</code>)
		 * or a short-time (<code>false</code>) message. 
		 */
		public AsynchronousToaster(Context context, int message, boolean isLong) {
			this.context = context;
			this.message = null;
			this.messageId = message;
			this.isLong = isLong;
		}

		@Override
		public void run() {
			if (this.message!=null && !this.message.isEmpty()) {
				Toast.makeText(this.context, this.message,
						this.isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
			}
			else if (this.messageId>=0) {
				Toast.makeText(this.context, this.messageId,
						this.isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
			}
		}
	} // class AsynchronousToaster
	
}
