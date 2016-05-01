/* 
 * $Id$
 * 
 * Copyright (C) 2002  St&eacute;phane GALLAND, Mahdi Hannoun
 * Copyright (C) 2004-2013  St&eacute;phane GALLAND
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
package org.arakhne.afc.ui.swing.zoom;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;

import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.RoundRectangle2f;
import org.arakhne.afc.math.continous.object2d.Segment2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.ui.CenteringTransform;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.ZoomableContext;
import org.arakhne.afc.ui.ZoomableContextUtil;
import org.arakhne.afc.ui.event.KeyEvent;
import org.arakhne.afc.ui.event.PointerEvent;
import org.arakhne.afc.ui.swing.event.KeyEventSwing;
import org.arakhne.afc.ui.swing.event.PointerEventSwing;
import org.arakhne.afc.ui.swing.zoom.ScrollingMethod.ScrollingMethodListener;

/** This abstract view provides the tools to move and scale
 * the view. It is abstract because it does not draw anything.
 * <p>
 * The implementation of the ZoomableView handles the pointer as following:
 * <table>
 * <head>
 * <tr><th>Event</th><th>Status</th><th>Callback</th><th>Note</th></tr>
 * </head>
 * <tr><td>POINTER_PRESSED</td><td>supported</td><td>{@link #onPointerPressed(PointerEvent)}</td><td>Allways called</td></tr>
 * <tr><td>POINTER_DRAGGED</td><td>supported</td><td>{@link #onPointerDragged(PointerEvent)}</td><td>Called only when the scale and move gestures are not in progress</td></tr>
 * <tr><td>POINTER_RELEASED</td><td>supported</td><td>{@link #onPointerReleased(PointerEvent)}</td><td>Called only when the scale and move gestures are not in progress</td></tr>
 * <tr><td>POINTER_MOVED</td><td>not supported</td><td></td><td>Pointer move on a touch screen cannot be detected?</td></tr>
 * <tr><td>POINTER_CLICK</td><td>not supported</td><td></td><td>See {@link #onClick(PointerEvent)}</td></tr>
 * <tr><td>POINTER_LONG_CLICK</td><td>not supported</td><td></td><td>See {@link #onLongClick(PointerEvent)}</td></tr>
 * <body>
 * </body>
 * </table>
 * <p>
 * The function {@link #onDrawView(Graphics2D, float, CenteringTransform)}} may
 * use an instance of the graphical context {@link ZoomableGraphics2D} to draw
 * the elements according to the zooming attributes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ZoomableGraphics2D
 * @deprecated see JavaFX API
 */
@Deprecated
public abstract class ZoomableView extends JPanel implements ZoomableContext, Printable {

	private static final long serialVersionUID = -5703191414584605699L;

	/** Size of the border that is automatically added
	 * when {@link #repaint(float, float, float, float)} is invoked. 
	 */
	public static final int REPAINT_BORDER = 5;
	
	/** Indicates if the anti-aliasing flag is set or not.
	 */
	private boolean antialiasing = false;

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
	float scaleFactor = 1.f;

	/** Minimal scaling factor.
	 */
	private float minScaleFactor = 0.0001f;

	/** Maximal scaling factor.
	 */
	private float maxScaleFactor = 1000.f;

	/** Zooming sensibility.
	 */
	private float zoomingSensitivity = 1.5f;

	/** Invert the scrolling direction.
	 */
	private boolean isInvertScrollingDirection = false;

	/** Indicates if the zooming area must be re-centred each time
	 * the mouse wheel was moved.
	 */
	private boolean dynamicCenteringWhenWheelMoved = false;

	/** Transformation to center the view used when rendering the view.
	 */
	final CenteringTransform centeringTransform = new CenteringTransform();

	/** Indicates if the X axis is inverted or not.
	 */
	private boolean isXAxisInverted = false;

	/** Indicates if the Y axis is inverted or not.
	 */
	private boolean isYAxisInverted = false;

	/** Scrolling method.
	 */
	private ScrollingMethod scrollingMethod = ScrollingMethod.MIDDLE_BUTTON;

	/** Participates to determine if the adviced level of details.
	 */
	private Graphics2DLOD lod = Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL;

	/** Indicates if the zoom factor could be interactively change
	 * with the mouse wheel.
	 */
	private boolean isWheelSupport = true;

	/** Wrapper to the document.
	 */
	final DocumentWrapper documentWrapper;

	/** Internal viewport when scrollbars are activated.
	 */
	private final Viewport viewport;

	/** Horizontal scroll bar.
	 */
	final JScrollBar hscroll;

	/** Vertical scroll bar.
	 */
	final JScrollBar vscroll;

	/** Boolean flag that permits to enable/disable scrollbar updating.
	 */
	final AtomicBoolean isScrollbarEnabled = new AtomicBoolean();

	/** Create a zoomable view without scrollbars.
	 */
	public ZoomableView() {
		this(null);
	}

	/**
	 * @param documentWrapper is the wrapping object to use to obtain
	 * information and notifications about the displayed document.
	 * This wrapper is used to compute the scrollbar's values and
	 * to fit the view. If it is <code>null</code>, the view will
	 * not contain scrollbar.
	 */
	public ZoomableView(DocumentWrapper documentWrapper) {
		super();

		this.documentWrapper = documentWrapper;

		SwingEventHandler handler = new SwingEventHandler();

		if (this.documentWrapper!=null) {
			this.isScrollbarEnabled.set(true);
			this.viewport = new Viewport();

			this.hscroll = new JScrollBar(Adjustable.HORIZONTAL);
			this.hscroll.setFocusable(false);
			this.hscroll.setRequestFocusEnabled(false);
			this.hscroll.setVisible(true);

			this.vscroll = new JScrollBar(Adjustable.VERTICAL);
			this.vscroll.setFocusable(false);
			this.vscroll.setRequestFocusEnabled(false);
			this.vscroll.setVisible(true);

			JPanel southPane = new JPanel();
			southPane.setLayout(new BoxLayout(southPane, BoxLayout.X_AXIS));
			JPanel corner = new JPanel();
			Dimension2D d1 = this.hscroll.getPreferredSize();
			Dimension2D d2 = this.vscroll.getPreferredSize();
			Dimension borderSize = new Dimension((int)d2.getWidth(), (int)d1.getHeight());
			corner.setMinimumSize(borderSize);
			corner.setMaximumSize(borderSize);
			corner.setPreferredSize(borderSize);

			setLayout(new BorderLayout());
			ComponentOrientation co = getComponentOrientation();
			if (co.isLeftToRight()) {
				add(BorderLayout.EAST, this.vscroll);
				southPane.add(this.hscroll);
				southPane.add(corner);
			}
			else {
				add(BorderLayout.WEST, this.vscroll);
				southPane.add(corner);
				southPane.add(this.hscroll);
			}
			add(BorderLayout.SOUTH, southPane);
			add(BorderLayout.CENTER, this.viewport);

			updateScrollbars();

			this.vscroll.addAdjustmentListener(handler);
			this.hscroll.addAdjustmentListener(handler);
			this.documentWrapper.addChangeListener(handler);
		}
		else {
			this.isScrollbarEnabled.set(false);
			this.viewport = null;
			this.hscroll = null;
			this.vscroll = null;
		}

		// Events
		JPanel viewport = getViewport();
		viewport.addComponentListener(handler);
		viewport.addMouseListener(handler);
		viewport.addMouseMotionListener(handler);
		viewport.addMouseWheelListener(handler);
		viewport.addKeyListener(handler);
	}

	/** Invoked to update the scrollbars and the graphical context
	 * according to the current scrollbar context.
	 */
	void updateScrollbars() {
		boolean oldV = this.isScrollbarEnabled.getAndSet(false);
		try {
			if (oldV) {
				assert(this.documentWrapper!=null);
				Rectangle2f documentBounds = this.documentWrapper.getDocumentBounds();
				if (documentBounds==null) {
					this.hscroll.setEnabled(false);
					this.vscroll.setEnabled(false);
				}
				else {
					updateScrollBar(this.hscroll, 
							(int)logical2pixel_x(documentBounds.getMinX()),
							(int)Math.ceil(logical2pixel_x(documentBounds.getMaxX())),
							getViewport().getWidth());
					updateScrollBar(this.vscroll, 
							(int)logical2pixel_y(documentBounds.getMinY()),
							(int)Math.ceil(logical2pixel_y(documentBounds.getMaxY())),
							getViewport().getHeight());
				}
			}
		}
		finally {
			this.isScrollbarEnabled.set(oldV);
		}
	}

	private static void updateScrollBar(JScrollBar bar, int minDocument, int maxDocument, int maxViewport) {
		int minBar = Math.min(minDocument, 0);
		int maxBar = Math.max(maxDocument, maxViewport);
		if (minBar>=0 && maxBar<=maxViewport) {
			bar.setEnabled(false);
		}
		else {
			bar.setEnabled(true);
			bar.setValues(0, maxViewport, minBar, maxBar);
		}
	}

	/** Replies the viewport. The viewport is the internal
	 * panel if there are scrollbars, or this ZoomableView
	 * if there is no scrollbar.
	 * 
	 * @return the internal viewport or this object. 
	 */
	public JPanel getViewport() {
		if (this.viewport==null) return this;
		return this.viewport;
	}

	/** Replies if this panel must be drawn with anti-aliased algorithm.
	 * 
	 * @return <code>true</code> if the anti-aliased flag was set,
	 * otherwhise <code>false</code>
	 */
	public boolean isAntiAliased() {
		return this.antialiasing;
	}

	/** Set if this panel must be drawn with anti-aliased algorithm.
	 * 
	 * @param antialiasing must be <code>true</code> if the anti-aliased flag must be set,
	 * otherwhise <code>false</code>
	 */
	public void setAntiAliased(boolean antialiasing) {
		this.antialiasing = antialiasing;
	}

	/** Replies the level of details to be used by this
	 * panel when rendering.
	 * The level of details is changed according
	 * to the internal events.
	 * 
	 * @return the level of detail
	 */
	protected Graphics2DLOD getLOD() {
		return this.lod;
	}

	/** Change the level of details.
	 * 
	 * @param lod
	 * @return the old value.
	 */
	Graphics2DLOD setLOD(Graphics2DLOD lod) {
		Graphics2DLOD old = this.lod;
		this.lod = lod;
		return old;
	}

	/** Replies if this panel allow the user to change the zoom
	 * whith the mouse wheel.
	 * 
	 * @return <code>true</code> if the mouse wheel actions are allowed,
	 * otherwise <code>false</code>
	 */
	public boolean isMouseWheelAllowed() {
		return this.isWheelSupport;
	}

	/** Sets if this panel allow the user to change the zoom
	 * whith the mouse wheel.
	 * 
	 * @param enable must be <code>true</code> if the mouse wheel actions are allowed,
	 * otherwise <code>false</code>
	 */
	public void setMouseWheelEnable(boolean enable) {
		this.isWheelSupport = enable;
	}

	/** Replies if this panel recomputes the zooming target point
	 * each time the mouse wheel was move.
	 * 
	 * @return <code>true</code> if the target point is set according to the
	 * current mouse position when wheel is used, otherwise <code>false</code>
	 */
	public boolean isFocusChangedOnMouseWheel() {
		return this.dynamicCenteringWhenWheelMoved;
	}

	/** Sets if this panel recomputes the zooming target point
	 * each time the mouse wheel was move.
	 * 
	 * @param enable must be <code>true</code> if the target point is set according to the
	 * current mouse position when wheel is used, otherwise <code>false</code>
	 */
	public void setFocusChangedOnMouseWheel(boolean enable) {
		this.dynamicCenteringWhenWheelMoved = enable;
	}

	/** Replies the scrolling method.
	 * 
	 * @return the scrolling method.
	 * @since 4.1
	 */
	public ScrollingMethod getScrollingMethod() {
		return this.scrollingMethod;
	}

	/** Set the scrolling method.
	 * 
	 * @param method is the scrolling method.
	 * @since 4.1
	 */
	public void setScrollingMethod(ScrollingMethod method) {
		this.scrollingMethod = (method==null) ? ScrollingMethod.MIDDLE_BUTTON : method;
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
	public void repaint() {
		if (this.viewport!=null) this.viewport.repaint();
		super.repaint();
	}

	/** Repaint this view wherever this function is invoked.
	 * <p>
	 * This function gets values in the logical coordinate space,
	 * not in the screen coordinate space. To repaint with screen
	 * coordinates, see {@link #repaint(int, int, int, int)}. 
	 * <p>
	 * This function the given area extended with a border of 5 pixels. 
	 * 
	 * @param x is the position in the logical space (not the screen space).
	 * @param y is the position in the logical space (not the screen space).
	 * @param width is the width in the logical space (not the screen space).
	 * @param height is the height in the logical space (not the screen space).
	 * @see #getIgnoreRepaint()
	 * @see #repaint(int, int, int, int)
	 * @see #repaint(Rectangle2f)
	 * @see #REPAINT_BORDER
	 */
	public final void repaint(float x, float y, float width, float height) {
		int l = (int)logical2pixel_x(x)-REPAINT_BORDER;
		int t = (int)logical2pixel_y(y)-REPAINT_BORDER;
		int w = (int)logical2pixel_size(width)+REPAINT_BORDER*2;
		int h = (int)logical2pixel_size(height)+REPAINT_BORDER*2;
		if (this.viewport==null)
			repaint(l, t, w, h);
		else
			this.viewport.repaint(l, t, w, h);
	}
	
	/** Repaint this view wherever this function is invoked.
	 * <p>
	 * This function gets values in the logical coordinate space,
	 * not in the screen coordinate space. To repaint with screen
	 * coordinates, see {@link #repaint(Rectangle)}. 
	 * 
	 * @param r is the rectangle to repaint (in logical coordinate space).
	 * @see #repaint(Rectangle)
	 * @see #repaint(float, float, float, float)
	 */
	public final void repaint(Rectangle2f r) {
		if (r!=null) {
			repaint(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
		}
	}

	/** Repaint this view wherever this function is invoked.
	 * <p>
	 * This function gets values in the logical coordinate space,
	 * not in the screen coordinate space. To repaint with screen
	 * coordinates, see {@link #repaint(Rectangle)}. 
	 * 
	 * @param r is the rectangle to repaint (in logical coordinate space).
	 * @see #repaint(Rectangle)
	 * @see #repaint(float, float, float, float)
	 */
	public final void repaint(Shape2f r) {
		if (r!=null) {
			Rectangle2f bounds = r.toBoundingBox();
			repaint(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
		}
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
		return getViewport().getWidth()/2f;
	}

	/** Replies the Y coordinate of the center of the viewport (in screen coordinate).
	 * 
	 * @return the center of the viewport.
	 */
	public final float getViewportCenterY() {
		return getViewport().getHeight()/2f;
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

	/** Change the scaling factor to have the specified
	 * ratio between 1 pixel and 1 unit in the document.
	 * <p>
	 * Each unit from the displayed document will
	 * have the same graphical size as the amount of 
	 * pixels specified by the <var>ratio</var>.
	 * 
	 * @param ratio 
	 */
	public void setScalingFactorForPixelRatio(float ratio) {
		float onePixel = pixel2logical_size(1);
		float factor = onePixel * ratio;
		setScalingFactor(factor);
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
		if (onScale(
				getViewportCenterX(),
				getViewportCenterY(),
				getScalingSensitivity())) {
			repaint();
		}
	}

	/** Zoom the view out.
	 */
	public final void zoomOut() {
		if (onScale(
				getViewportCenterX(),
				getViewportCenterY(),
				1f/getScalingSensitivity())) {
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

	/** Reset the view to the default configuration: scaling factor to 1.
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
			return true;
		}
		return false;
	}

	/** Replies the scaling factor that may be used
	 * to fit the content of the document to the
	 * drawing area.
	 * <p>
	 * If there is no wrapper given to {@link #ZoomableView(DocumentWrapper)},
	 * this function replies <code>1</code>.
	 * 
	 * @return the scaling factor that permits to fit the
	 * document.
	 */
	public float getScalingFactorToFit() {
		if (this.documentWrapper!=null) {
			Rectangle2f documentBounds = this.documentWrapper.getDocumentBounds();
			if (documentBounds!=null) {
				JPanel viewport = getViewport();
				float drawingAreaSize, documentSize;

				// horizontal fitting
				drawingAreaSize = viewport.getWidth();
				documentSize = documentBounds.getWidth();
				float horizontalFactor = ZoomableContextUtil.determineFactor(
						documentSize, drawingAreaSize);

				// vertical fitting
				drawingAreaSize = viewport.getHeight();
				documentSize = documentBounds.getHeight();
				float verticalFactor = ZoomableContextUtil.determineFactor(
						documentSize, drawingAreaSize);

				return Math.min(horizontalFactor, verticalFactor);
			}
		}
		return 1f;
	}

	/** Reset the view so that the document is fitting the
	 * drawing area.
	 * <p>
	 * If there is no wrapper given to {@link #ZoomableView(DocumentWrapper)},
	 * this function does the same as {@link #resetView()}.
	 * 
	 * @return <code>true</code> if the view has changed; <code>false</code> otherwise.
	 */
	public final boolean fitView() {
		float px = getPreferredFocusX();
		float py = getPreferredFocusY();
		float fitFactor = getScalingFactorToFit();
		if (this.focusX!=px || this.focusY!=py || getScalingFactor()!=fitFactor) {
			this.focusX = px;
			this.focusY = py;
			if (!setScalingFactor(fitFactor)) {
				onUpdateViewParameters();
				repaint(); // Force to refresh the UI
			}
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
		int result;
		Graphics2DLOD lod = setLOD(Graphics2DLOD.HIGH_LEVEL_OF_DETAIL);
		try {
			if (pageIndex==0) {
				Graphics2D g2d = (Graphics2D)g;

				// Shift Graphic to line up with beginning of print-imageable region
				g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

				// Be sure that the document feat to the print-imageable region
				float width = getViewport().getWidth();
				float height = getViewport().getHeight();
				float pageWidth = (float)pageFormat.getImageableWidth();
				float pageHeight = (float)pageFormat.getImageableHeight();
				float scale = 
						((pageWidth / width)>(pageHeight / height))
						? (pageHeight / height)
								: (pageWidth / width);


						g2d.setColor(Color.BLACK);
						g2d.drawRect(0,0,(int)pageWidth,(int)pageHeight);

						g2d.scale(scale, scale);

						// Print the document
						print(g2d, new Rectangle2D.Float(0, 0, width, height));			

						result = PAGE_EXISTS;
			}
			else {
				result = NO_SUCH_PAGE;
			}
		}
		finally {
			setLOD(lod);
		}
		return result;
	}

	/** Print the specified part of this panel into
	 * the specified graphical context.
	 * <p>
	 * The given print area is the part of the panel
	 * to replies inside an image. It is expressed in pixels.
	 * <p>
	 * This function assumes that the upper-left corner
	 * of the document has the coordinates (0,0) and the
	 * specified printing area is relative to this origin.
	 * 
	 * @param g is the graphical context inside which to print.
	 * @param print_area is the window coordinates to snap.
	 */
	protected final void print(Graphics2D g, Rectangle2D print_area) {
		float width = getViewport().getWidth();
		float height = getViewport().getHeight();

		// Be sure that the printing area is enclosed inside the document bounds.
		Rectangle2D r = new Rectangle2D.Double(0,0,width,height).createIntersection(print_area);

		// Be sure that the new origin (0,0) corresponds
		// to upper-left corner of the print area
		g.translate(-r.getX(),-r.getY());

		print(g);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void print(Graphics g) {
		Graphics2DLOD lod = setLOD(Graphics2DLOD.HIGH_LEVEL_OF_DETAIL);
		try {
			super.print(g);
		}
		finally {
			setLOD(lod);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void printAll(Graphics g) {
		Graphics2DLOD lod = setLOD(Graphics2DLOD.HIGH_LEVEL_OF_DETAIL);
		try {
			super.printAll(g);
		}
		finally {
			setLOD(lod);
		}
	}

	@Override
	public final void paint(Graphics g) {
		super.paint(g);
		if (this.viewport==null)
			onDrawView((Graphics2D)g, this.scaleFactor, this.centeringTransform);
	}

	/** Invoked to paint the view after it is translated and scaled.
	 * 
	 * @param canvas is the canvas in which the view must be painted.
	 * @param scaleFactor is the scaling factor to use for drawing.
	 * @param centeringTransform is the transform to use to put the draws at the center of the view.
	 */
	protected abstract void onDrawView(Graphics2D canvas, float scaleFactor, CenteringTransform centeringTransform);

	/**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     * <p>
     * The KEY_TYPED event may be unavailable on several components.
     * You must read the document of the component on which the action management
     * is applied to be sure that the KEY_TYPED event is handled.
     * 
     * @param e
     */
	protected void onKeyTyped(KeyEvent e) {
    	//
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     * <p>
     * The KEY_PRESSED event may be unavailable on several components.
     * You must read the document of the component on which the action management
     * is applied to be sure that the KEY_PRESSED event is handled.
     * 
     * @param e
     */
    protected void onKeyPressed(KeyEvent e) {
    	//
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     * <p>
     * The KEY_RELEASED event may be unavailable on several components.
     * You must read the document of the component on which the action management
     * is applied to be sure that the KEY_RELEASED event is handled.
     * 
     * @param e
     */
    protected void onKeyReleased(KeyEvent e) {
    	//
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

	/** Invoked when the pointer is moved without a button down.
	 * 
	 * @param e
	 */
	protected void onPointerMoved(PointerEvent e) {
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
	protected boolean onScale(float focusX, float focusY, float requestedScaleFactor) {
		setScalingFactorAndFocus(focusX, focusY, this.scaleFactor * requestedScaleFactor);
		return true;
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class Viewport extends JPanel {

		private static final long serialVersionUID = -4516500471311375616L;

		/**
		 */
		public Viewport() {
			//
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			onDrawView((Graphics2D)g, ZoomableView.this.scaleFactor, ZoomableView.this.centeringTransform);
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class SwingEventHandler implements ComponentListener, 
	MouseInputListener, MouseWheelListener,
	ScrollingMethodListener, AdjustmentListener, ChangeListener,
	KeyListener {

		/** Save the number of wheel clicks between two calls to
		 * {@link #mouseWheelMoved(MouseWheelEvent)}.
		 */
		private int bufferedWheelClicks = 0;

		/** Asynchronous process that is waiting before refreshing
		 * the panel with full details
		 */
		private volatile ScrollWaiter scrollWaiter = null;

		/** Indicates if the mouse scrolling feature is under progress.
		 */
		private boolean mouseScrollingUnderProgress = false;

		/** Current location of the mouse on the panel.
		 */
		private int mouseX = -1;

		/** Current location of the mouse on the panel.
		 */
		private int mouseY = -1;

		/** Save the LOD for beeing restored after scrolling.
		 */
		private Graphics2DLOD previousLOD = null;

		public SwingEventHandler() {
			//
		}

		@Override
		public void componentResized(ComponentEvent e) {
			onUpdateViewParameters();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			//
		}

		@Override
		public void componentShown(ComponentEvent e) {
			onUpdateViewParameters();
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			//
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();		
			boolean isLongClick = (e.getClickCount()>1);
			PointerEventSwing evt = new PointerEventSwing(e);
			if (isLongClick) {
				onLongClick(evt);
			}
			else {
				onClick(evt);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();		
			getScrollingMethod().tryScroll(e, this);
			if (!e.isConsumed())
				onPointerPressed(new PointerEventSwing(e));
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();		
			stopScrolling(e);
			if (!e.isConsumed())
				onPointerReleased(new PointerEventSwing(e));
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();		
		}

		@Override
		public void mouseExited(MouseEvent e) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();		
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			//
			// MOVE THE VIEW ACCORDING TO THE SCROLLING FEATURE
			//
			if (!this.mouseScrollingUnderProgress && this.scrollWaiter!=null) {
				this.scrollWaiter = null;
				startScrolling(e, 0);
			}
			if (this.mouseScrollingUnderProgress) {
				Rectangle2f document_rect = ZoomableView.this.documentWrapper.getDocumentBounds();
				if (document_rect!=null) {
					float dx, dy;
					if (isMoveDirectionInverted()) {
						dx = pixel2logical_size(this.mouseX - e.getX());
						dy = pixel2logical_size(this.mouseY - e.getY());
					}
					else {
						dx = pixel2logical_size(e.getX() - this.mouseX);
						dy = pixel2logical_size(e.getY() - this.mouseY);
					}
					if ((dx!=0)||(dy!=0)) {
						float fx = getFocusX() - dx;
						float fy = getFocusY() - dy;
						setFocusPoint(fx, fy);
					}
				}
				e.consume();
			}

			this.mouseX = e.getX();
			this.mouseY = e.getY();		

			if (!e.isConsumed()) {
				onPointerDragged(new PointerEventSwing(e));
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();		
			onPointerMoved(new PointerEventSwing(e));
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();		
			if (isMouseWheelAllowed()) {
				int clicks = e.getWheelRotation() + this.bufferedWheelClicks;
				float fx, fy;
				if (e.isControlDown() || isFocusChangedOnMouseWheel()) {
					fx = e.getX();
					fy = e.getY();
				}
				else {
					fx = getViewportCenterX();
					fy = getViewportCenterY();
				}
				float scale = getScalingSensitivity();
				if (clicks>=0) {
					scale = 1f / scale;
				}
				if (onScale(fx, fy, Math.abs(clicks) * scale)) {
					this.bufferedWheelClicks = 0;
					repaint();
				}
				else {
					this.bufferedWheelClicks = clicks;
				}
				e.consume();
			}
		}

		@Override
		public void startScrolling(MouseEvent event, int delay) {
			if (delay<=0) {
				this.mouseScrollingUnderProgress = true;
				this.previousLOD = setLOD(Graphics2DLOD.LOW_LEVEL_OF_DETAIL);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				event.consume();
				this.mouseX = event.getX();
				this.mouseY = event.getY();
			}
			else if (this.scrollWaiter==null) {
				this.scrollWaiter = new ScrollWaiter(event, System.currentTimeMillis()+delay);
				SwingUtilities.invokeLater(this.scrollWaiter);
			}
		}

		@Override
		public void stopScrolling(MouseEvent event) {
			this.scrollWaiter = null;
			if (this.mouseScrollingUnderProgress) {
				this.mouseScrollingUnderProgress = false;
				setLOD(this.previousLOD);
				setCursor(Cursor.getDefaultCursor());
				event.consume();
				repaint();
				this.mouseX = event.getX();
				this.mouseY = event.getY();
			}
		}

		/** This class handles the events for a {@link ZoomablePanel}.
		 * 
		 * @author $Author: sgalland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 */
		private class ScrollWaiter implements Runnable {

			private final long timeout;
			private final MouseEvent event;

			/**
			 * @param event
			 * @param timeout
			 */
			public ScrollWaiter(MouseEvent event, long timeout) {
				this.event = event;
				this.timeout = timeout;
			}

			/**
			 * {@inheritDoc}
			 */
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				if (System.currentTimeMillis()>=this.timeout) {
					startScrolling(this.event, 0);
				}
				else if (SwingEventHandler.this.scrollWaiter==this) {
					SwingUtilities.invokeLater(this);
				}
			}

		} // class ScrollWaiter

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (ZoomableView.this.isScrollbarEnabled.get()) {
				setLOD( 
						e.getValueIsAdjusting()
						? Graphics2DLOD.LOW_LEVEL_OF_DETAIL
								: Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL);

				float fx = getFocusX();
				float fy = getFocusY();

				Rectangle2f document_rect = ZoomableView.this.documentWrapper.getDocumentBounds();

				if (document_rect!=null) {

					Rectangle2D viewport = getViewport().getBounds();

					if (e.getSource()==ZoomableView.this.vscroll) {
						//
						// Move the target point vertically
						fy = pixel2logical_y(e.getValue() + (float)viewport.getHeight()/2f);

					}

					else if (e.getSource()==ZoomableView.this.hscroll) {
						//
						// Move the target point horizontally
						fx = pixel2logical_x(e.getValue() + (float)viewport.getWidth()/2f);
					}

					setFocusPoint(fx,fy);
				}
			}
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			updateScrollbars();
		}

		@Override
		public void keyTyped(java.awt.event.KeyEvent e) {
			onKeyTyped(new KeyEventSwing(e));
		}

		@Override
		public void keyPressed(java.awt.event.KeyEvent e) {
			onKeyPressed(new KeyEventSwing(e));
		}

		@Override
		public void keyReleased(java.awt.event.KeyEvent e) {
			onKeyReleased(new KeyEventSwing(e));
		}

	} // class SwingEventHandler

}
