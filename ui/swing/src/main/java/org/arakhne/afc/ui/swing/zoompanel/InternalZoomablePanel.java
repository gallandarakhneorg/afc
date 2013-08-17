/* $Id$
 * 
 * This is a part of NetEditor project from Arakhne.org:
 * package org.arakhne.neteditor.zoompanel.
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

package org.arakhne.afc.ui.swing.zoompanel;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import javax.swing.JPanel;

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
import org.arakhne.afc.ui.ZoomableContextUtil;
import org.arakhne.afc.ui.awt.DoubleDimension;
import org.arakhne.afc.ui.awt.FloatDimension;
import org.arakhne.afc.ui.awt.ZoomableAwtContextUtil;

/** Component for displaying a zoomable graphical area.
 * <p>
 * This class is partly based on the
 * class <code>NetEditor.Workspace</code> from
 * the Arakhne.org project.
 * <p>
 * Three coordinate systems are used:
 * <ul>
 * <li>the workspace coordinates: coordinates
 * specifical to the displayed document (<code>float</code>),</li>
 * <li>the screen coordinates: the coordinates on the screen
 * (<code>int</code>) such as the mouse location on the screen,</li>
 * <li>the fitted coordinates: corresponds to the scaling of
 * the workspace coordinates to fit the entire document into the
 * current screen space.</li>
 * </ul>
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
final class InternalZoomablePanel extends JPanel implements ZoomableViewport {

	//-------------------------------------------------------------------
	// Attributes
	//-------------------------------------------------------------------

	private static final long serialVersionUID = -2740411873564016759L;

	/** Zoomable panel.
	 */
	private final WeakReference<ZoomablePanel> zoomPanel;

	/** Transformation to center the objects in the view.
	 */
	private final CenteringTransform centeringTransform = new CenteringTransform();

	/** This is the current zoom factor.
	 */
	private float zoomFactor = 1.f;

	/** This is the stepping amount eahc time the
	 * user want to zoom out or in.
	 */
	private float zoomFactorStep = 1.05f;

	/** Minimal scaling factor.
	 */
	private float minScaleFactor = 0.001f;

	/** Maximal scaling factor.
	 */
	private float maxScaleFactor = 100.f;

	/** This is the scale factor used to fit the document into
	 * the screen space.
	 */
	private float fitToWindowFactor = 1f;

	/** Last restangle passed to {@link #refreshFactors(Rectangle2D)}
	 * when the screen window was empty (typically occurs when
	 * the window was never displayed).
	 */
	private Rectangle2D lastRefreshRequestRectangle = null;

	/** This is the document point which is displayed
	 * at the center of the viewport.
	 * <p>
	 * This point corresponds to the workspace coordinate space. 
	 */
	private Point2f targetInDocument = null;

	/** Size of the drawing area.
	 */
	private Dimension2D drawingArea = null;

	/** Buffer for the drawing area.
	 */
	private SoftReference<Rectangle2D> drawRectangleBuffer = null;

	/** Indicates if the X axis is inverted.
	 */
	private final boolean flipX; 

	/** Indicates if the Y axis is inverted.
	 */
	private final boolean flipY; 

	//-------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------

	/** Create a InternalZoomablePanel and add it into the specified
	 * scroll pane.
	 * 
	 * @param zoompane is the zoom panel that owns this panel.
	 * @param flipX indicates if the X coordinates may be flipped, ie.
	 * the X axis may be inverted. 
	 * @param flipY indicates if the Y coordinates may be flipped, ie.
	 * the Y axis may be inverted. 
	 */
	public InternalZoomablePanel(ZoomablePanel zoompane, boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
		this.zoomPanel = new WeakReference<ZoomablePanel>(zoompane);
	}

	//-------------------------------------------------------------------
	// Getter/Setter
	//-------------------------------------------------------------------

	/** Replies the scaling factor to apply to have the document fitting the view.
	 * 
	 * @return s
	 */
	public float getFitInWindowFactor() {
		return this.fitToWindowFactor;
	}

	/** Replies the current zoom factor.
	 * 
	 * @return current zoom factor.
	 */
	public float getZoomFactor() {
		return this.zoomFactor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isXAxisInverted() {
		return this.flipX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isYAxisInverted() {
		return this.flipY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getScalingFactor() {
		return this.zoomFactor * this.fitToWindowFactor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point2D getFocusPointPixel() {
		Rectangle2D bounds = getBounds();
		return new Point2D.Double(bounds.getCenterX(),bounds.getCenterY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point2f getFocusPoint() {
		if (this.targetInDocument==null) return null;
		return new Point2f(this.targetInDocument);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocusPoint(float x, float y) {
		if (this.targetInDocument==null || x!=this.targetInDocument.getX()
				|| y!=this.targetInDocument.getY()) {
			Point2f old = this.targetInDocument;
			if (this.targetInDocument==null)
				this.targetInDocument = new Point2f(x,y);
			else
				this.targetInDocument.set(x, y);
			this.drawRectangleBuffer = null;
			firePropertyChange("targetPoint", old, this.targetInDocument); //$NON-NLS-1$
		}
	}

	/** Remove the focus point.
	 */
	void clearFocusPoint() {
		if (this.targetInDocument!=null) {
			Point2f old = this.targetInDocument;
			this.targetInDocument = null;
			this.drawRectangleBuffer = null;
			firePropertyChange("targetPoint", old, this.targetInDocument); //$NON-NLS-1$
		}
	}

	//-------------------------------------------------------------------
	// Paint
	//-------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex==0) {
			Graphics2D g2d = (Graphics2D)g;

			// Shift Graphic to line up with beginning of print-imageable region
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

			// Be sure that the document feat to the print-imageable region
			Rectangle2D r = getDocumentRect();
			float width = logical2pixel_size((float)r.getWidth());
			float height = logical2pixel_size((float)r.getHeight());
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

					return PAGE_EXISTS;
		}
		return NO_SUCH_PAGE;
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
	public void print(Graphics2D g, Rectangle2D print_area) {
		Rectangle2D r = getDocumentRect();
		float doc_x = logical2pixel_x((float)r.getMinX());
		float doc_y = logical2pixel_y((float)r.getMinY());
		float width = logical2pixel_size((float)r.getWidth());
		float height = logical2pixel_size((float)r.getHeight());

		// Be sure that the printing area is enclosed inside the document bounds.
		r = new Rectangle2D.Double(0,0,width,height).createIntersection(print_area);

		// Be sure that the new origin (0,0) corresponds
		// to upper-left corner of the print area
		g.translate(-doc_x-r.getX(),-doc_y-r.getY());

		print(g);
	}

	/**
	 * Invoke this method to print the component. This method will
	 * result in invocations to <code>printComponent</code>,
	 * <code>printBorder</code> and <code>printChildren</code>. It is
	 * not recommended that you override this method, instead override
	 * one of the previously mentioned methods. This method sets the
	 * component's state such that the double buffer will not be used, eg
	 * painting will be done directly on the passed in <code>Graphics</code>.
	 *
	 * @param g the <code>Graphics</code> context in which to paint
	 */
	@Override
	public void print(Graphics g) {
		synchronized(this.getTreeLock()) {
			ZoomablePanel panel = this.zoomPanel.get();

			boolean opaque = isOpaque();
			boolean antialiasing = panel.isAntiAliased();
			Graphics2DLOD lod = panel.getLOD();

			setOpaque(false);
			panel.setAntiAliased(true);
			panel.setLOD(Graphics2DLOD.HIGH_LEVEL_OF_DETAIL);

			super.print(g);

			setOpaque(opaque);
			panel.setAntiAliased(antialiasing);
			panel.setLOD(lod);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void paint(Graphics g) {

		if (getIgnoreRepaint()) return;

		Graphics2D g2d = (Graphics2D)g;

		// Compute the factors because the last
		// calls to the refreshFactors() was made
		// when the screen window is empty
		if (this.lastRefreshRequestRectangle!=null) {
			Rectangle2D r = this.lastRefreshRequestRectangle;
			if (refreshFactors(r)) changeWorkspaceSize(r);
		}

		Color color = getBackground();
		if (color!=null) g2d.setBackground(color);

		super.paint(g);

		// Be sure that the document coordinate system was computed
		if (this.targetInDocument!=null) {

			// Get the dimensions of the logical space and the screen space
			Rectangle2D screen_bounds = getBounds();

			// Compute the translation for centering the logical
			// space inside the screen space


			// Compute the translation for centering the logical
			// space inside the screen space

			double delta_prime = pixel2logical_size((int)screen_bounds.getCenterX());
			if (isXAxisInverted()) {
				this.centeringTransform.setCenteringX(
						true,
						-1,
						(float)(delta_prime + this.targetInDocument.getX()));
			}
			else {
				this.centeringTransform.setCenteringX(
						false,
						1,
						(float)(delta_prime - this.targetInDocument.getX()));
			}

			delta_prime = pixel2logical_size((int)screen_bounds.getCenterY());
			if (isYAxisInverted()) {
				this.centeringTransform.setCenteringY(
						true,
						-1,
						(float)(delta_prime + this.targetInDocument.getY()));
			}
			else {
				this.centeringTransform.setCenteringY(
						false,
						1,
						(float)(delta_prime - this.targetInDocument.getY()));
			}
		}

		ZoomablePanel panel = this.zoomPanel.get();

		if (panel!=null) {

			boolean isPrinting =
					(panel.hasFlag(ZoomablePanel.FLAG_IS_PRINTING)||
							panel.hasFlag(ZoomablePanel.FLAG_IS_PRINTING_ALL));

			// Create the new graphical context
			panel.paintAllComponents(
					g2d, this,
					this.centeringTransform,
					this.zoomFactor,
					panel.isAntiAliased(),
					isPrinting,
					panel.getLOD());
		}
	}

	//-------------------------------------------------------------------
	// Sizes
	//-------------------------------------------------------------------

	/** Refresh the factors for the zoom and the fit of the document
	 * into the screen space.
	 * <p>
	 * This method do not call {@link #changeWorkspaceSize(Rectangle2D)}
	 * to refresh the scrollbar and the size of the drawing area.
	 * You must call <code>changeWorkspaceSize(Rectangle2D)</code> just
	 * after <code>refreshFactors(Rectangle2D)</code>.
	 * 
	 * @param document_bounds is the bounds of the document (in document's units)
	 * @return a status that indicates if this function succeeded or not.
	 */
	boolean refreshFactors(Rectangle2D document_bounds) {
		Rectangle2D screen_window = getBounds();

		if ((screen_window==null)||(screen_window.isEmpty())) {
			this.lastRefreshRequestRectangle = document_bounds;
			return false;
		}
		this.lastRefreshRequestRectangle = null;
		float oldFactor = this.zoomFactor;
		this.zoomFactor = 1.f;
		Point2f oldTarget = this.targetInDocument;

		if (this.targetInDocument==null) this.targetInDocument = new Point2f();

		if ((document_bounds==null)||(document_bounds.isEmpty())) {
			this.fitToWindowFactor = 1.f;

			this.targetInDocument.set(
					(float)screen_window.getCenterX(),
					(float)screen_window.getCenterY());
		}
		else {    		

			this.fitToWindowFactor = (float)(screen_window.getWidth() / document_bounds.getWidth());
			float fit = (float)(screen_window.getHeight() / document_bounds.getHeight());
			if (fit<this.fitToWindowFactor) this.fitToWindowFactor = fit;

			this.targetInDocument.set(
					(float)document_bounds.getCenterX(),
					(float)document_bounds.getCenterY());
		}

		this.drawRectangleBuffer = null;

		if (oldFactor!=this.zoomFactor) {
			this.zoomPanel.get().firePropertyChange("zoomFactor", oldFactor, this.zoomFactor); //$NON-NLS-1$
		}
		if (!this.targetInDocument.equals(oldTarget)) {
			this.zoomPanel.get().firePropertyChange("targetPoint", oldTarget, this.targetInDocument); //$NON-NLS-1$
		}

		return true;
	}

	/** Change the workspace area if necessary.
	 *  <p>
	 *  This method is called each time the workspace
	 *  could be extended in one of the four cardinal
	 *  directions.
	 *  <p>
	 *  The size of the workspace and/or the origin point
	 *  are updated if the given Rectangle is outside
	 *  the current workspace.
	 *  <p>
	 *  The scroll bar was not refreshed.
	 *
	 * @param requiredArea the area that must be in workspace. If
	 *        <code>null</code>, the required area will be computed
	 *        with {@link #getDocumentRect()}.
	 * @return <code>true</code> if the bounds have changed; otherwise
	 * <code>false</code>.
	 */
	boolean changeWorkspaceSize(Rectangle2D requiredArea) {
		ZoomablePanel zoompanel = this.zoomPanel.get();
		if (zoompanel==null) return false;
		Rectangle2D r = requiredArea;
		if (r==null) r = getDocumentRect() ;
		if (r==null) return false;

		// ompute the dimensions of the document in the screen space
		float documentScreenWidth = logical2pixel_size((float)r.getWidth());
		float documentScreenHeight = logical2pixel_size((float)r.getHeight());

		// Reset the size of the drawing area
		if (this.drawingArea==null) this.drawingArea = new DoubleDimension(documentScreenWidth,documentScreenHeight);
		else this.drawingArea.setSize(documentScreenWidth,documentScreenHeight) ;
		this.drawRectangleBuffer = null;

		// Set the location of the scroll bars
		return zoompanel.revalidateWorkspace();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension2D getDrawingAreaSize() {
		return this.drawingArea==null ? new DoubleDimension() : this.drawingArea;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle2D getDrawingAreaRect() {
		Rectangle2D b = (this.drawRectangleBuffer==null) ? null : this.drawRectangleBuffer.get();
		if (b!=null) return b;

		Rectangle2D world = getDocumentRect();
		Rectangle2D viewport = getBounds();

		if ((world==null)||(world.isEmpty()))
			return viewport;

		if (this.targetInDocument==null) {
			refreshFactors(world);
		}

		if (this.targetInDocument==null)
			return viewport;

		if (this.drawingArea==null) {
			float documentScreenWidth = logical2pixel_size((float)world.getWidth());
			float documentScreenHeight = logical2pixel_size((float)world.getHeight());
			this.drawingArea = new DoubleDimension(documentScreenWidth, documentScreenHeight);
		}

		Rectangle2D world_relative = new Rectangle2D.Double(
				world.getX() - this.targetInDocument.getX(),
				world.getY() - this.targetInDocument.getY(),
				world.getWidth(), world.getHeight());

		float dx = (float)(this.drawingArea.getWidth() / world.getWidth());
		float dy = (float)(this.drawingArea.getHeight() / world.getHeight());

		b = new Rectangle2D.Float(
				(float)(world_relative.getX()*dx+viewport.getCenterX()),
				(float)(world_relative.getY()*dy+viewport.getCenterY()),
				(float)this.drawingArea.getWidth(),
				(float)this.drawingArea.getHeight());
		this.drawRectangleBuffer = new SoftReference<Rectangle2D>(b);
		return b;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float logical2pixel_size(float l) {
		return ZoomableContextUtil.logical2pixel_size(l, this.fitToWindowFactor * this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float logical2pixel_x(float l) {
		return ZoomableContextUtil.logical2pixel_x(l,
				this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float logical2pixel_y(float l) {
		return ZoomableContextUtil.logical2pixel_y(l,
				this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float pixel2logical_size(float l) {
		return ZoomableContextUtil.pixel2logical_size(l,
				this.fitToWindowFactor * this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float pixel2logical_x(float p) {
		return ZoomableContextUtil.pixel2logical_x(p,
				this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float pixel2logical_y(float p) {
		return ZoomableContextUtil.pixel2logical_y(p,
				this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	/**
	 * Translate the given shape from the logical coordinate space
	 * to the screen coordinate space.
	 * 
	 * @param rLogic
	 * @return the translation of rLogic
	 */
	public <T extends Shape> T logical2pixel(T rLogic) {
		return ZoomableAwtContextUtil.logical2pixel(
				rLogic, 
				this.centeringTransform,
				getScalingFactor());
	}

	/**
	 * Translate the given font from the logical coordinate space
	 * to the screen coordinate space.
	 * 
	 * @param fLogic
	 * @return the translation of fLogic
	 */
	public Font logical2pixel(Font fLogic) {
		return ZoomableAwtContextUtil.logical2pixel(
				fLogic, getScalingFactor());
	}

	/**
	 * Translate the given shape from the screen coordinate space
	 * to the logical coordinate space.
	 * 
	 * @param rScreen
	 * @return the translation of rScreen
	 */
	public <T extends Shape> T pixel2logical(T rScreen) {
		return ZoomableAwtContextUtil.pixel2logical(
				rScreen,
				this.centeringTransform,
				getScalingFactor());
	}

	/**
	 * Translate the given font from the screen coordinate space
	 * to the logical coordinate space.
	 * 
	 * @param f_screen
	 * @return the translation of f_screen
	 */
	public Font pixel2logical(Font f_screen) {
		return ZoomableAwtContextUtil.pixel2logical(
				f_screen,
				getScalingFactor());
	}

	/** Translates the specified screen rectangle
	 *  into the logical rectangle.
	 *
	 * @param r_screen is the rectangle in the screen space.
	 * @return a rectangle into the logical space.
	 */
	public Rectangle2D pixel2logical(Rectangle2D r_screen) {
		return new Rectangle2D.Double(
				pixel2logical_x((float)r_screen.getX()),
				pixel2logical_y((float)r_screen.getY()),
				pixel2logical_size((float)r_screen.getWidth()),
				pixel2logical_size((float)r_screen.getHeight()));
	}

	/** Translates the specified logical rectangle
	 *  into the screen rectangle.
	 *
	 * @param r_logic is the rectangle in the logical space.
	 * @return a rectangle into the screen space.
	 */
	public Rectangle2D logical2pixel(Rectangle2D r_logic) {
		return new Rectangle2D.Double(
				logical2pixel_x((float)r_logic.getX()),
				logical2pixel_y((float)r_logic.getY()),
				logical2pixel_size((float)r_logic.getWidth()),
				logical2pixel_size((float)r_logic.getHeight()));
	}

	//-------------------------------------------------------------------
	// Document
	//-------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle2D getDocumentRect() {
		ZoomablePanel zoompanel = this.zoomPanel.get();
		if (zoompanel==null) return null;
		return zoompanel.getDocumentRect();
	}

	//-------------------------------------------------------------------
	// Zoom
	//-------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setScalingFactor(float factor) {
		setScalingFactor(factor,true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setScalingFactor(float factor, boolean enable_repaint) {
		if (factor>0 && this.zoomFactor!=factor) {
			float old = this.zoomFactor;
			this.zoomFactor = factor;

			Rectangle2D screen_window = getBounds();    		
			Rectangle2D document_bounds = getDocumentRect();
			Point2f oldFocus = this.targetInDocument;

			if (this.targetInDocument==null) {
				this.targetInDocument = new Point2f();
				if (document_bounds==null) {
					this.targetInDocument.set(
							pixel2logical_x((float)screen_window.getCenterX()),
							pixel2logical_y((float)screen_window.getCenterY()));        			
				}
				else {
					this.targetInDocument.set(
							(float)document_bounds.getCenterX(),
							(float)document_bounds.getCenterY());
				}
			}

			if (enable_repaint) {
				FloatDimension doc_bounds = new FloatDimension();

				if (document_bounds==null) {
					doc_bounds.setSize(
							screen_window.getWidth(),
							screen_window.getHeight());
				}
				else {
					doc_bounds.setSize(
							logical2pixel_size((float)document_bounds.getWidth()),
							logical2pixel_size((float)document_bounds.getHeight()));
				}

				changeWorkspaceSize(document_bounds);
				if ((doc_bounds.getWidth()<=screen_window.getWidth())&&
						(doc_bounds.getHeight()<=screen_window.getHeight()))
					repaint();
			}

			this.zoomPanel.get().firePropertyChange("zoomFactor", old, this.zoomFactor); //$NON-NLS-1$
			if (!this.targetInDocument.equals(oldFocus)) {
				this.zoomPanel.get().firePropertyChange("targetPoint", old, this.targetInDocument); //$NON-NLS-1$
			}
		}
	}

	/** Change the zooming factor to have the specified
	 * ratio between 1 pixel and 1 unit in the document.
	 * <p>
	 * Each unit from the displayed document will
	 * have the same graphical size as the amount of 
	 * pixels specified by the <var>ratio</var>.
	 * 
	 * @param ratio 
	 */
	public void setZoomFactorForPixelRatio(float ratio) {
		float onePixel = pixel2logical_size(1);
		float factor = onePixel * ratio;
		setScalingFactor(factor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomIn() {
		zoomIn(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomOut() {
		zoomOut(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomIn(boolean enable_repaint) {
		if (this.zoomFactorStep>0) {
			setScalingFactor( this.zoomFactor * this.zoomFactorStep, enable_repaint ) ;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void zoomOut(boolean enable_repaint) {
		if (this.zoomFactorStep>0) {
			setScalingFactor( this.zoomFactor / this.zoomFactorStep, enable_repaint ) ;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshPanelContent() {
		setScalingFactor(this.zoomFactor, true);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getMaxScalingFactor() {
		return this.maxScaleFactor;
	}

	/** Set the maximal scaling factor allowing in the view
	 * 
	 * @param factor is the maximal scaling factor.
	 */
	public void setMaxScalingFactor(float factor) {
		if (factor>0f && factor!=this.maxScaleFactor) {
			this.maxScaleFactor = factor;
			if (this.minScaleFactor>this.maxScaleFactor)
				this.minScaleFactor = this.maxScaleFactor;
			if (this.zoomFactor>this.maxScaleFactor)
				this.zoomFactor = this.maxScaleFactor;
			repaint();
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getMinScalingFactor() {
		return this.minScaleFactor;
	}

	/** Set the minimal scaling factor allowing in the view
	 * 
	 * @param factor is the minimal scaling factor.
	 */
	public void setMinScalingFactor(float factor) {
		if (factor>0f && factor!=this.minScaleFactor) {
			this.minScaleFactor = factor;
			if (this.maxScaleFactor<this.minScaleFactor)
				this.maxScaleFactor = this.minScaleFactor;
			if (this.zoomFactor<this.minScaleFactor)
				this.zoomFactor = this.minScaleFactor;
			repaint();
		}
	}

	@Override
	public float getScalingSensitivity() {
		return this.zoomFactorStep;
	}

	@Override
	public float getFocusX() {
		if (this.targetInDocument==null) return Float.NaN;
		return this.targetInDocument.getX();
	}

	@Override
	public float getFocusY() {
		if (this.targetInDocument==null) return Float.NaN;
		return this.targetInDocument.getY();
	}

	@Override
	public PathIterator2f logical2pixel(PathIterator2f p) {
		return ZoomableContextUtil.logical2pixel(p, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public PathIterator2f pixel2logical(PathIterator2f p) {
		return ZoomableContextUtil.pixel2logical(p, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void logical2pixel(Segment2f s) {
		ZoomableContextUtil.logical2pixel(s, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void pixel2logical(Segment2f s) {
		ZoomableContextUtil.pixel2logical(s, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void logical2pixel(RoundRectangle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void pixel2logical(RoundRectangle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void logical2pixel(Point2f p) {
		ZoomableContextUtil.logical2pixel(p, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void pixel2logical(Point2f p) {
		ZoomableContextUtil.pixel2logical(p, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void logical2pixel(Ellipse2f e) {
		ZoomableContextUtil.logical2pixel(e, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void pixel2logical(Ellipse2f e) {
		ZoomableContextUtil.pixel2logical(e, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void logical2pixel(Circle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void pixel2logical(Circle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void logical2pixel(Rectangle2f r) {
		ZoomableContextUtil.logical2pixel(r, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public void pixel2logical(Rectangle2f r) {
		ZoomableContextUtil.pixel2logical(r, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public Shape2f logical2pixel(Shape2f s) {
		return ZoomableContextUtil.logical2pixel(s, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

	@Override
	public Shape2f pixel2logical(Shape2f s) {
		return ZoomableContextUtil.pixel2logical(s, this.centeringTransform,
				this.fitToWindowFactor * this.zoomFactor);
	}

}
