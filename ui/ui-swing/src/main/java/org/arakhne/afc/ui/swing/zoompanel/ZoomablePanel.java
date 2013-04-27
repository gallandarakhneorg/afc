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
 * version 2.1 of the License, or (at your option) any later version.
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


import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.EventListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.ui.CenteringTransform;
import org.arakhne.afc.ui.Graphics2DLOD;
import org.arakhne.afc.ui.ZoomableContext;
import org.arakhne.afc.ui.swing.zoompanel.ScrollingMethod.ScrollingMethodListener;

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
 * <p>
 * Don't forget to call {@link #refreshWorkspaceSize(Rectangle2D)} each
 * time the size of your document changed. The parameter of this function
 * is the size of the document expressed in the workspace coordinate system.
 * <p>
 * <center>
 * <img src="./doc-files/ZoomablePanel.png"><br>
 * Example of a <code>ZoomablePanel</code>.
 * </center>
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class ZoomablePanel extends JPanel implements Printable {
	
	private static final long serialVersionUID = -8971440167157593394L;

	/** Delay after which the mouse-wheel refresher display
	 * the default level of details (in milliseconds).
	 * <p>
	 * This rate will be used when anti-aliasing was not turned on.
	 */
	protected static final int REFRESH_DELAY = 400;	

	/** Delay after which the mouse-wheel refresher display
	 * the default level of details (in milliseconds).
	 * <p>
	 * This rate will be used when anti-aliasing was turned on.
	 */
	protected static final int REFRESH_DELAY_AA = 500;	

	/** Precision in pixels for the zooming action.
	 * <p>
	 * If the mouse has a move greater than this precision,
	 * the mouse location will be used as a new target point
	 * for the zoom functions.
	 */
	public static final int ZOOM_MOUSE_PRECISION = 5;
		
	/** This flag was set when this panel is printing.
	 * 
	 * @see #print(Graphics)
	 * @see #print(Graphics, PageFormat, int)
	 */
	public static final int FLAG_IS_PRINTING = 0;
	
	/** This flag was set when this panel is printing all.
	 * 
	 * @see #printAll(Graphics)
	 */
	public static final int FLAG_IS_PRINTING_ALL = 1;

	//-------------------------------------------------------------------
	// Attributes
	//-------------------------------------------------------------------
	
	/** Is a list of flags for this panel.
	 */
	private long flags = 0;

	/** Indicates if the user can scroll the view wh the middle
	 * button of the mouse was pressed.
	 */
	private boolean mouseScrollSupport = true;

	/** Indicates if the zoom factor could be interactively change
	 * with the mouse wheel.
	 */
	private boolean wheelSupport = true;
	
	/** Indicates if the zooming area must be re-centred each time
	 * the mouse wheel was moved.
	 */
	private boolean dynamicCenteringWhenWheelMoved = false;
	
	/** Zoomable panel.
	 */
	final InternalZoomablePanel zoomPanel;
	
	/** Horizontal scroll bar.
	 */
	final JScrollBar hscroll = new JScrollBar(Adjustable.HORIZONTAL);
	
	/** Vertical scroll bar.
	 */
	final JScrollBar vscroll = new JScrollBar(Adjustable.VERTICAL);
	
	/** Indicates if the scroll movements are catched.
	 */
	private boolean allowScrollMoves = true;
	
	/** Indicates if the anti-aliasing flag is set or not.
	 */
	private boolean antialiasing = false;
	
	/** Participates to determine if the adviced level of details.
	 */
	private Graphics2DLOD lod = Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL;
	
	/** Thread to refresh the panel after a mouse wheel movement.
	 */
	private MouseWheelRefresher mouseWheelRefresher = null;
	
	/** Event handler.
	 */
	private EventListener handler = null;
	
	/** Scrolling method.
	 */
	private ScrollingMethod scrollingMethod = ScrollingMethod.MIDDLE_BUTTON;
	
	/** Component at the corner of the panel.
	 */
	private Component cornerComponent = null;
	
	/** Border around the viewport.
	 */
	private Border viewportBorder = null;

	//-------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------
	
	/** Create a InternalZoomablePanel and add it into the specified
	 * scroll pane.
	 * The X and Y axis are not inverted.
	 */
	protected ZoomablePanel() {
		this(false, false);
	}
	
	/** Create a InternalZoomablePanel and add it into the specified
	 * scroll pane.
	 * <p>
	 * This constructor permits to invert the X and Y axis. If not inverted,
	 * the logical X/Y axis is directed mapped to
	 * the corresponding AWT/Swing X/Y axis. If inverted, the logical X/Y
	 * axis is assumed to have the opposite direction that the AWT/Swing
	 * X/Y axis.
	 * 
	 * @param flipX indicates if the X axis may be inverted.
	 * @param flipY indicates if the Y axis may be inverted.
	 */
	protected ZoomablePanel(boolean flipX, boolean flipY) {
		this.zoomPanel = new InternalZoomablePanel(this, flipX, flipY);
		
		this.zoomPanel.setBackground(getBackground());
		this.zoomPanel.setForeground(getForeground());
		
		this.vscroll.setFocusable(false);
		this.vscroll.setRequestFocusEnabled(false);
		this.vscroll.addAdjustmentListener(getEventHandler(AdjustmentListener.class));
		this.vscroll.setVisible(true);
		
		this.hscroll.setFocusable(false);
		this.hscroll.setRequestFocusEnabled(false);
		this.hscroll.addAdjustmentListener(getEventHandler(AdjustmentListener.class));
		this.hscroll.setVisible(true);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				revalidateWorkspace();
			}
		});
		
		setLayout(new BorderLayout());
		add(BorderLayout.EAST, this.vscroll);
		
		final JPanel southpane = new JPanel();
		southpane.setLayout(new BoxLayout(southpane,BoxLayout.X_AXIS));
		southpane.add(this.hscroll);
		final JPanel p = new JPanel();
		Dimension2D d1 = this.hscroll.getPreferredSize();
		Dimension2D d2 = this.vscroll.getPreferredSize();
		p.setMinimumSize(new Dimension((int)d2.getWidth(), (int)d1.getHeight()));
		p.setMaximumSize(new Dimension((int)d2.getWidth(), (int)d1.getHeight()));
		p.setPreferredSize(new Dimension((int)d2.getWidth(), (int)d1.getHeight()));
		southpane.add(p);
		add(BorderLayout.SOUTH, southpane);
		
		add(BorderLayout.CENTER, this.zoomPanel);
		
		addMouseWheelListener(getEventHandler(MouseWheelListener.class));
		addMouseListener(getEventHandler(MouseListener.class));
		addMouseMotionListener(getEventHandler(MouseMotionListener.class));
	}
	
	//-------------------------------------------------------------------
	// Getter/Setter
	//-------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		// To enable access from the internal zoomable panel
		super.firePropertyChange(propertyName, oldValue, newValue);
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

	/** Replies the zoomable context associated to this panel.
     * 
     * @return the zoomable context of this panel.
     */
    public ZoomableContext getZoomableContext() {
    	return this.zoomPanel;
    }

	/** Replies if the scroll moves are allowed.
	 * 
	 * @return <code>true</code> if the scrolling actions are allowed, otherwise <code>false</code>
	 */
	boolean isScrollMoveAllowed() {
		return this.allowScrollMoves;
	}
	
	/** Replies the event handler. If no event handler was already
	 * attached, create the default instance of event handler.
	 * 
	 * @param <T> is the type of event handler to reply
	 * @param clazz is the type of event handler to reply
	 * @return the event handler of the given type or <code>null</code>
	 */
	protected <T extends EventListener> T getEventHandler(Class<T> clazz) {
		if (this.handler==null)
			setEventHandler(createEventHandler());
		assert(this.handler!=null);
		return clazz.cast(this.handler);
	}
	
	/** Set the event handler associated to this
	 * event source.
	 * 
	 * @param handler
	 */
	protected void setEventHandler(EventListener handler) {
		this.handler = handler;
	}

	/** Invoked when the event handler must be created.
	 * 
	 * @return the new event handler.
	 */
	protected EventListener createEventHandler() {
		return new EventHandler();
	}
	
	/**
     * Gets the background color of this component.
     * @return this component's background color; if this component does
     *          not have a background color,
     *          the background color of its parent is returned
     * @see #setBackground
     */
	@Override
	public Color getBackground() {
		return (this.zoomPanel==null) ? super.getBackground() : this.zoomPanel.getBackground();
	}	
	
    /**
     * Sets the background color of this component.
     * <p>
     * The background color affects each component differently and the
     * parts of the component that are affected by the background color 
     * may differ between operating systems.
     *
     * @param c the color to become this component's color;
     *          if this parameter is <code>null</code>, then this
     *          component will inherit the background color of its parent
     * @see #getBackground
     */
    @Override
	public void setBackground(Color c) {
    	if (this.zoomPanel!=null)
    		this.zoomPanel.setBackground(c);
    	super.setBackground(c);    	
    }

    /** Replies if this panel allow the user to scroll with
	 * the mouse when the middle button was pressed.
	 * 
	 * @return <code>true</code> if the mouse scroll actions are allowed,
	 * otherwise <code>false</code>
	 */
	public boolean isMouseScrollEnable() {
		return this.mouseScrollSupport;
	}
	
	/** Sets if this panel allow the user to scroll with
	 * the mouse when the middle button was pressed.
	 * 
	 * @param enable must be <code>true</code> if the mouse scroll actions are allowed,
	 * otherwise <code>false</code>
	 */
	public void setMouseScrollEnable(boolean enable) {
		this.mouseScrollSupport = enable;
	}

	/** Replies if this panel allow the user to change the zoom
	 * whith the mouse wheel.
	 * 
	 * @return <code>true</code> if the mouse wheel actions are allowed,
	 * otherwise <code>false</code>
	 */
	public boolean isMouseWheelAllowed() {
		return this.wheelSupport;
	}
	
	/** Sets if this panel allow the user to change the zoom
	 * whith the mouse wheel.
	 * 
	 * @param enable must be <code>true</code> if the mouse wheel actions are allowed,
	 * otherwise <code>false</code>
	 */
	public void setMouseWheelEnable(boolean enable) {
		this.wheelSupport = enable;
	}
	
	/** Replies if this panel recomputes the zooming target point
	 * each time the mouse wheel was move.
	 * 
	 * @return <code>true</code> if the target point is set according to the
	 * current mouse position when wheel is used, otherwise <code>false</code>
	 */
	public boolean isZoomTargetComputedOnWheelMove() {
		return this.dynamicCenteringWhenWheelMoved;
	}
	
	/** Sets if this panel recomputes the zooming target point
	 * each time the mouse wheel was move.
	 * 
	 * @param enable must be <code>true</code> if the target point is set according to the
	 * current mouse position when wheel is used, otherwise <code>false</code>
	 */
	public void setZoomTargetComputedOnWheelMove(boolean enable) {
		this.dynamicCenteringWhenWheelMoved = enable;
	}
	
	/** Replies the current horizontal scroll bar.
	 * 
	 * @return the current horizontal scroll bar.
	 */
	public JScrollBar getHorizontalScrollBar() {
		return this.hscroll;
	}
	
	/** Replies the current vertical scroll bar.
	 * 
	 * @return the current vertical scroll bar.
	 */
	public JScrollBar getVerticalScrollBar() {
		return this.vscroll;
	}
	
	/** Replies the scaling factor that permits to scale the
	 * document into the screen window.
	 * 
	 * @return the scaling factor.
	 */
	public float getDocumentScalingFactor() {
		return this.zoomPanel.getFitInWindowFactor();
	}
	
	/** Replies the viewport component
	 * 
	 * @return the viewport component
	 */
	public ZoomableViewport getViewport() {
		return this.zoomPanel;
	}
	
	/** Replies the target point
	 * 
	 * @return the target point
	 */
	public Point2f getFocusPoint() {
		return this.zoomPanel.getFocusPoint();
	}

	/** Set the target point
	 * 
	 * @param x
	 * @param y
	 */
	public void setFocusPoint(float x, float y) {
		this.zoomPanel.setFocusPoint(x,y);
		revalidateWorkspace();
		repaint();
	}

	/** Replies the component at the lower right corner.
	 * 
	 * @return the component at the lower right corner.
	 */
	public Component getCorner() {
		return this.cornerComponent;
	}
	
	/** Replies the component at the lower right corner.
	 * 
	 * @param corner the component at the lower right corner.
	 */
	public void setCorner(Component corner) {
		if (this.cornerComponent!=corner) {
			Component old = this.cornerComponent;
			this.cornerComponent = corner;

			if (old!=null) remove(old);
			if (this.cornerComponent!=null) add(this.cornerComponent);
			
			firePropertyChange("corner", old, this.cornerComponent); //$NON-NLS-1$
			revalidate();
	        repaint();
		}
	}

	/** Replies the border arround the viewport.
	 * 
	 * @return the border arround the viewport.
	 */
	public Border getViewportBorder() {
		return this.viewportBorder;
	}
	
	/** Replies the border arround the viewport.
	 * 
	 * @param border the border arround the viewport.
	 */
	public void setViewportBorder(Border border) {
		if (this.viewportBorder!=border) {
			Border old = this.viewportBorder;
			this.viewportBorder = border;
			
			firePropertyChange("viewportBorder", old, this.viewportBorder); //$NON-NLS-1$
		}
	}

	/** Reply the rectangle that enclose the
	 *  current document in the document coordinate system.
	 *
	 * @return a Rectangle that correspond to the
	 *          size of the document (in workspace coordinates).
	 */
	public abstract Rectangle2D getDocumentRect();
	
	/** Reply the rectangle that enclose the visible parts of the
	 *  current document in the document coordinate system.
	 *
	 * @return a Rectangle that correspond to the
	 *          size of the document (in workspace coordinates).
	 */
	public abstract Rectangle2D getVisibleDocumentRect();

	/** Replies if this panel is allowing to
	 * display a low detail panel.
	 * <p>
	 * It occurs when the panel was scrolling
	 * with the mouse.
	 * 
	 * @return the level of detail
	 */
	public Graphics2DLOD getLOD() {
		return this.lod;
	}
	
	/** Replies if this panel is allowing to
	 * display a low detail panel.
	 * <p>
	 * It occurs when the panel was scrolling
	 * with the mouse.
	 * 
	 * @param lod
	 */
	void setLOD(Graphics2DLOD lod) {
		this.lod = lod;
	}

	/** Return an Image of this Panel.
	 * <p>
	 * The given print area is the part of the panel
	 * to replies inside an image. It is expressed in pixels.
	 * <p>
	 * This function assumes that the upper-left corner
	 * of the document has the coordinates (0,0) and the
	 * specified printing area is relative to this origin.
	 * 
	 * @param print_area is the window coordinates to snap.
	 * @return the resulting image.
	 */
	public RenderedImage getImage(Rectangle2D print_area) {
		Rectangle2D area;
		// Compute the default image size: wall document
		if (print_area==null) {
			Rectangle2D r = getDocumentRect();
			
			float width = logical2pixel_size((float)r.getWidth());
			float height = logical2pixel_size((float)r.getHeight());
			
			area = new Rectangle2D.Double(0, 0, width, height);
		}
		else {
			area = print_area;
		}
		
		//Create the image that must receive the content of the panel
		try {
			BufferedImage image = new BufferedImage(
					(int)area.getWidth(),
					(int)area.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			// Print the image
			Graphics2D g = (Graphics2D)image.getGraphics();
			g.setClip(area);
			
			boolean is_set = setFlag(FLAG_IS_PRINTING,true);
			this.zoomPanel.print(g, area);
			setFlag(FLAG_IS_PRINTING,is_set);
			
			return image;
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable e) {
			Logger.getAnonymousLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
		}
		return null;
	}
	
	/** Return an Image of this Panel.
	 * <p>
	 * The returned image corresponds to
	 * all the document area.
	 * 
	 * @return the resulting image.
	 */
	public RenderedImage getImage() {
		return getImage(null);
	}

	//-------------------------------------------------------------------
	// Flag
	//-------------------------------------------------------------------
	
	/** Set a flag.
	 * 
	 * @param flag_number is the number of the flag to set/unset.
	 * @param is_set must be <code>true</code> to set the flag or
	 * <code>false</code> to unset.
	 * @return <code>true</code> if the flag was previously set, 
	 * <code>false</code> if the flag was previously unset.
	 */
	public boolean setFlag(int flag_number, boolean is_set) {
		boolean prev = ((1<<flag_number)&this.flags)!=0;
		if (is_set) {
			this.flags = (1<<flag_number)|this.flags;
		}
		else {
			this.flags = (~(1<<flag_number))&this.flags;
		}
		return prev;
	}

	/** Replies a flag.
	 * 
	 * @param flag_number is the number of the flag to set/unset.
	 * @return <code>true</code> if the flag was set, 
	 * <code>false</code> otherwhise.
	 */
	public boolean hasFlag(int flag_number) {
		return ((1<<flag_number)&this.flags)!=0;
	}

	//-------------------------------------------------------------------
	// Paint Interface
	//-------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
		boolean is_set = setFlag(FLAG_IS_PRINTING,true);
		int result = this.zoomPanel.print(g,pageFormat,pageIndex);
		setFlag(FLAG_IS_PRINTING,is_set);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void print(Graphics g) {
		boolean is_set = setFlag(FLAG_IS_PRINTING,true);
		super.print(g);
		setFlag(FLAG_IS_PRINTING,is_set);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void printAll(Graphics g) {
		boolean is_set = setFlag(FLAG_IS_PRINTING_ALL,true);
		super.printAll(g);
		setFlag(FLAG_IS_PRINTING_ALL,is_set);
	}

	/** Draw the zoomable panel.
	 * <p>
	 * This function draws all the components: the panel itself, the mouse graphical indicators...
	 * By default this function invokes {@link #paintPanel(ZoomableGraphics2D)}.
	 * 
	 * @param gzoom is the context inside painting must be done.
	 */
	protected void paintAllComponents(ZoomableGraphics2D gzoom) {
		paintPanel(gzoom);
	}

	/** Draw the panel with the specified parameters.
	 * <p>
	 * This function is invoqued by the internal panel and
	 * must invoke {@link #paintAllComponents(ZoomableGraphics2D)}.
	 *
	 * @param target is the graphics in which draws will be done.
	 * @param target_component is the target component (used to display an icon).
	 * @param centeringTransform is the transformation to use to draw the objects at the center of the view.
	 * @param zoom is the zoom factor.
	 * @param useAntialiasing permits to force the anti-aliasing flag for the target graphical context
	 * @param is_for_printing indicates if this graphics environment is for printing or not.
	 * @param lodLevel indicates the desired LOD used by this graphical context.
	 */
	protected void paintAllComponents(Graphics2D target, Component target_component, 
			CenteringTransform centeringTransform, float zoom, 
			boolean useAntialiasing, boolean is_for_printing, Graphics2DLOD lodLevel) {
    	// Create the new graphical context
    	ZoomableGraphics2D gzoom = new ZoomableGraphics2D(
    			target, target_component,
    			centeringTransform,
    			zoom,
				useAntialiasing,
				is_for_printing,
				lodLevel,
				this.zoomPanel.getScalingSensitivity(),
				this.zoomPanel.getMinScalingFactor(),
				this.zoomPanel.getMaxScalingFactor(),
				this.zoomPanel.getFocusX(),
				this.zoomPanel.getFocusY());
		paintAllComponents(gzoom);
	}
	
	/** Draw the zoomable panel.
	 * <p>
	 * This function only draw the panel's content.
	 * 
	 * @param gzoom is the context inside painting must be done.
	 */
	protected abstract void paintPanel(ZoomableGraphics2D gzoom);
	
	/** Refresh the workspace size to be sure that it
	 * will corresponds to the specified rectangle.
	 * <p>
	 * This method calls {@link InternalZoomablePanel#refreshFactors(Rectangle2D)}
	 * which fits the document into the current window, and the method
	 * {@link InternalZoomablePanel#changeWorkspaceSize(Rectangle2D)}
	 * which permits to change the size of the workspace.
	 * 
	 * @param bounds
	 * @return <code>true</code> if the workspace bounds have changed;
	 * otherwise <code>false</code>.
	 */
	public boolean refreshWorkspaceSize(Rectangle2D bounds) {
		assert(bounds!=null);
		if (this.zoomPanel.refreshFactors(bounds))
			return this.zoomPanel.changeWorkspaceSize(bounds);
		return false;
	}
	
	/** Refresh the workspace size to be sure that it
	 * will corresponds to the current bounds of the model.
	 * <p>
	 * This function does not change the zoom factor and
	 * the target point.
	 * 
	 * @return <code>true</code> if the workspace bounds have changed;
	 * otherwise <code>false</code>.
	 * @since 4.0
	 */
	public boolean refreshWorkspaceSize() {
		return this.zoomPanel.changeWorkspaceSize(getDocumentRect());
	}
	
	/** Refresh the workspace size to be sure that it
	 * will corresponds to the specified rectangle.
	 * <p>
	 * This method calls {@link InternalZoomablePanel#refreshFactors(Rectangle2D)}
	 * which fits the document into the current window, and the method
	 * {@link InternalZoomablePanel#changeWorkspaceSize(Rectangle2D)}
	 * which permits to change the size of the workspace.
	 * 
	 * @param width
	 * @param height
	 * @return <code>true</code> if the workspace bounds have changed;
	 * otherwise <code>false</code>.
	 */
	public boolean refreshWorkspaceSize(float width, float height) {
		Rectangle2D bounds = new Rectangle2D.Double(0,0,width,height);
		if (this.zoomPanel.refreshFactors(bounds))
			return this.zoomPanel.changeWorkspaceSize(bounds);
		return false;
	}

	/** 
	 * Revalidate the components of the workspace.
	 * <p>
	 * This method is called by {@link InternalZoomablePanel}
	 * each time the workspace size was update, ie. when zooming
	 * or resizing.
	 * 
	 * @return <code>true</code> if the workspace bounds have changed;
	 * <code>false</code> otherwise.
	 */
	boolean revalidateWorkspace() {
		Rectangle2D r_viewport = this.zoomPanel.getBounds();
		Rectangle2D r_document = this.zoomPanel.getDrawingAreaRect();
		
		if ((r_viewport==null)||(r_document==null)) return false;
		
		this.allowScrollMoves = false;
		
		// Compute the union of the document's rectangle
		// and the visible rectangle
		Rectangle2D full_rect = r_viewport.createUnion(r_document);
		
		// Set the horizontal scrollbar bounds 
		this.hscroll.setValues(
				(int)(r_viewport.getX() - full_rect.getY()), 
				(int)r_viewport.getWidth(),	// size of the extent (visible part)
				0,							// minimal value
				(int)full_rect.getWidth());	// maximal value
		this.hscroll.setEnabled(
				(full_rect.getWidth()>r_viewport.getWidth())&&	// document larger than viewport 
				(r_viewport.getWidth()>0));					// has visible part

		// Set the horizontal scrollbar bounds 
		this.vscroll.setValues(
				(int)(r_viewport.getY() - full_rect.getY()), 
				(int)r_viewport.getHeight(),	// size of the extent (visible part)
				0,					// minimal value
				(int)full_rect.getHeight());	// maximal value
		this.vscroll.setEnabled(
				(full_rect.getHeight()>r_viewport.getHeight())&&	// document larger than viewport 
				(r_viewport.getHeight()>0));					// has visible part

		this.allowScrollMoves = true;
		
		this.zoomPanel.repaint();
		
		return true;
	}    
		
	//-------------------------------------------------------------------
	// Coordinate Systems
	//-------------------------------------------------------------------
	
	/** Translates the specified workspace length
	 *  into the screen length.
	 *
	 * @param l is the length in the workspace space.
	 * @return a length into the screen space.
	 */
	public float logical2pixel_size(float l) {
		return this.zoomPanel.logical2pixel_size(l);
	}
	
	/** Translates the specified workspace location
	 *  into the screen location.
	 *
	 * @param l is the coordinate along the workspace space X-axis.
	 * @return a location along the screen space X-axis.
	 */
	public float logical2pixel_x(float l) {
		return this.zoomPanel.logical2pixel_x(l);
	}
	
	/** Translates the specified workspace location
	 *  into the screen location.
	 *
	 * @param l is the coordinate along the workspace space Y-axis.
	 * @return a location along the screen space Y-axis.
	 */
	public float logical2pixel_y(float l) {
		return this.zoomPanel.logical2pixel_y(l);
	}
	
	/** Translates the specified screen length
	 *  into the logical length.
	 *
	 * @param l is the length in the screen space.
	 * @return a length into the logical space.
	 */
	public float pixel2logical_size(float l) {
		return this.zoomPanel.pixel2logical_size(l);
	}
	
	/** Translates the specified screen location
	 *  into the logical location.
	 *
	 * @param l is the location along the screen space X-axis.
	 * @return a location along the logical space X-axis.
	 */
	public float pixel2logical_x(float l) {
		return this.zoomPanel.pixel2logical_x(l);
	}
	
	/** Translates the specified screen location
	 *  into the logical location.
	 *
	 * @param l is the location along the screen space Y-axis.
	 * @return a location along the logical space Y-axis.
	 */
	public float pixel2logical_y(float l) {
		return this.zoomPanel.pixel2logical_y(l);
	}
	
	/** Translates the specified screen rectangle
	 *  into the logical rectangle.
	 *
	 * @param r_screen is the rectangle in the screen space.
	 * @return a rectangle into the logical space.
	 */
	public Rectangle2D pixel2logical(Rectangle2D r_screen) {
		return this.zoomPanel.pixel2logical(r_screen);
	}

	/** Translates the specified logical rectangle
	 *  into the screen rectangle.
	 *
	 * @param r_logic is the rectangle in the logical space.
	 * @return a rectangle into the screen space.
	 */
	public Rectangle2D logical2pixel(Rectangle2D r_logic) {
		return this.zoomPanel.logical2pixel(r_logic);
	}

	/** Translates the specified screen shape
	 *  into the logical shape.
	 *
	 * @param <T> is the type of the shape to convert.
	 * @param r_screen is the rectangle in the screen space.
	 * @return a rectangle into the logical space.
	 */
	public <T extends Shape> T pixel2logical(T r_screen) {
		return this.zoomPanel.pixel2logical(r_screen);
	}

	/** Translates the specified logical shape
	 *  into the screen shape.
	 *
	 * @param <T> is the type of the shape to convert.
	 * @param r_logic is the rectangle in the logical space.
	 * @return a rectangle into the screen space.
	 */
	public <T extends Shape> T logical2pixel(T r_logic) {
		return this.zoomPanel.logical2pixel(r_logic);
	}
	
	/** Translates the specified screen font
	 *  into the logical font.
	 *
	 * @param f_screen is the font in the screen space.
	 * @return a font into the logical space.
	 */
	public Font pixel2logical(Font f_screen) {
		return this.zoomPanel.pixel2logical(f_screen);
	}

	/** Translates the specified logical font
	 *  into the screen font.
	 *
	 * @param f_logic is the font in the logical space.
	 * @return a font into the screen space.
	 */
	public Font logical2pixel(Font f_logic) {
		return this.zoomPanel.logical2pixel(f_logic);
	}

	//-------------------------------------------------------------------
	// Zoom
	//-------------------------------------------------------------------
	
	/** Force the panel to be drawn in its initial state:
	 * no zoom, target point at center.
	 *
	 * @param forceFit is <code>true</code> ti force the panel to fit
	 * the document rect inside the entire graphical area
	 * when at zoom factor 1, otherwise <code>false</code>
	 * @param ignoreInvisibleLayers indicates if the invisible layers should be
	 * ignored to fit the document, otherwise <code>false</code>
	 */
	public void defaultView(boolean forceFit, boolean ignoreInvisibleLayers) {
		Rectangle2D r = ignoreInvisibleLayers ? getVisibleDocumentRect() : getDocumentRect();
		if (forceFit) {
			this.zoomPanel.refreshFactors(r);
			revalidateWorkspace();
		}
		else {
			if (r!=null) {
				this.zoomPanel.setFocusPoint(
						(float)r.getCenterX(), (float)r.getCenterY());
			}
			else {
				this.zoomPanel.clearFocusPoint();
			}
			setZoomFactor(1f);
		}
	}	

	/** Force the panel to be drawn in its initial state:
	 * no zoom, target point at center.
	 *
	 * @param forceFit is <code>true</code> ti force the panel to fit
	 * the document rect inside the entire graphical area
	 * when at zoom factor 1, otherwise <code>false</code>
	 */
	public void defaultView(boolean forceFit) {
		defaultView(forceFit, false);
	}	

	/** Force the panel to be drawn in its initial state:
	 * no zoom, target point at center.
	 * <p>
	 * Equivalent to <code>defaultView(false)</code>
	 */
	public final void defaultView() {
		defaultView(false);
	}	

	/** Set the zoom factor.
	 * 
	 * @param factor is the new zoom factor.
	 */
	public void setZoomFactor(float factor) {
		this.zoomPanel.setScalingFactor(factor);
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
		this.zoomPanel.setZoomFactorForPixelRatio(ratio);
	}
		
	/** Replies the step between to levels of zoom factor when zooming in/out.
     * 
     * @return the zoom factor step when zooming in/out.
     */
    public float getScalingSensitivity() {
        return this.zoomPanel.getScalingSensitivity();
    }

    /** Zoom in.
	 */
	public void zoomIn() {
		this.zoomPanel.zoomIn();
	}
	
	/** Zoom out.
	 */
	public void zoomOut() {
		this.zoomPanel.zoomOut();
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
	
	/**
	 * Force the mouse listener on mouse wheel actions.
	 * 
	 * @param r is the new listenr of mouse wheel actions.
	 */
	synchronized void setMouseWheelRefresher(MouseWheelRefresher r) {
		this.mouseWheelRefresher = r;
	}

	/**
	 * Remove the mouse listener on mouse wheel actions.
	 * 
	 * @param source is the old listenr of mouse wheel actions.
	 */
	synchronized void clearMouseWheelRefresher(MouseWheelRefresher source) {
		if (this.mouseWheelRefresher==source) {
			this.mouseWheelRefresher = null;
			this.lod = Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL;
			repaint();
		}
	}
	
	/**
	 * Replies the mouse listener on mouse wheel actions.
	 * 
	 * @return the mouse listener on mouse wheel actions.
	 */
	synchronized MouseWheelRefresher getMouseWheelRefresher() {
		return this.mouseWheelRefresher;
	}

	/** 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class MouseWheelRefresher extends Thread {

		private long refreshDate;
		
		/**
		 * @param event_date
		 */
		public MouseWheelRefresher(long event_date) {
			super("Mouse Wheel Refresher"); //$NON-NLS-1$		
			this.refreshDate = event_date + 
			(ZoomablePanel.this.isAntiAliased()
			? REFRESH_DELAY_AA : REFRESH_DELAY);
		}
		
		/**
		 * @param event_date
		 */
		public synchronized void setEventDate(long event_date) {
			this.refreshDate = event_date + 
			(ZoomablePanel.this.isAntiAliased()
			? REFRESH_DELAY_AA : REFRESH_DELAY);
		}

		/**
		 * @return the date of the event.
		 */
		public synchronized long getEventDate() {
			return this.refreshDate;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			while (System.currentTimeMillis()<=getEventDate()) {
				Thread.yield();
			}
			ZoomablePanel.this.clearMouseWheelRefresher(this);
		}

	}
	
	/** This class handles the events for a {@link ZoomablePanel}.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public class EventHandler extends MouseAdapter implements ScrollingMethodListener, AdjustmentListener {

		/** Indicates if the mouse scrolling feature is under progress.
		 */
		private boolean mouseScrollingUnderProgress = false;
		
		/** Current location of the mouse on the panel.
		 */
		private int mouseX = -1;
		
		/** Current location of the mouse on the panel.
		 */
		private int mouseY = -1;
		
		/** Location of the mouse on the panel during the last zoom.
		 */
		private int zoominMouseX = -1;
		
		/** Location of the mouse on the panel during the last zoom.
		 */
		private int zoominMouseY = -1;
		
		private volatile ScrollWaiter scrollWaiter = null;
		
		//-------------------------------------------------------------------
		// Utilities
		//-------------------------------------------------------------------

		/** Indicates if the mouse scrolling feature is under progress.
		 * 
		 * @return <code>true</code> if the scrolling is under progress;
		 * otherwise <code>false</code>.
		 */
		protected boolean isMouseScrollingUnderProgress() {
			return this.mouseScrollingUnderProgress;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public void startScolling(MouseEvent event, int delay) {
			if ((ZoomablePanel.this.hscroll.isEnabled())||(ZoomablePanel.this.vscroll.isEnabled())) {
				if (delay<=0) {
					this.mouseX = event.getX();
					this.mouseY = event.getY();
					this.mouseScrollingUnderProgress = true;
					setLOD(Graphics2DLOD.LOW_LEVEL_OF_DETAIL);
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					event.consume();
				}
				else if (this.scrollWaiter==null) {
					this.scrollWaiter = new ScrollWaiter(event, System.currentTimeMillis()+delay);
					SwingUtilities.invokeLater(this.scrollWaiter);
				}
			}
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public void stopScolling(MouseEvent event) {
			this.scrollWaiter = null;
			if (this.mouseScrollingUnderProgress) {
				this.mouseScrollingUnderProgress = false;
				setLOD(Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL);
				setCursor(Cursor.getDefaultCursor());
				event.consume();
				repaint();
			}
		}

		/** This class handles the events for a {@link ZoomablePanel}.
		 * 
		 * @author $Author: galland$
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
					EventHandler.this.startScolling(this.event, 0);
				}
				else if (EventHandler.this.scrollWaiter==this) {
					SwingUtilities.invokeLater(this);
				}
			}
			
		} // class ScrollWaiter
		
		//-------------------------------------------------------------------
		// MouseListener Interface
		//-------------------------------------------------------------------
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mousePressed(MouseEvent event) {
			//
			// START A SCROLLING FEATURE
			//
			if (isMouseScrollEnable()) {
				getScrollingMethod().tryScroll(event, this);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mouseReleased(MouseEvent event) {
			stopScolling(event);
		}
			
		//-------------------------------------------------------------------
		// MouseMotion Interface
		//-------------------------------------------------------------------
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mouseDragged(MouseEvent event) {
			//
			// MOVE THE VIEW ACCORDING TO THE SCROLLING FEATURE
			//
			if (!this.mouseScrollingUnderProgress && this.scrollWaiter!=null) {
				this.scrollWaiter = null;
				startScolling(event, 0);
			}
			if (this.mouseScrollingUnderProgress) {
				float dx = 0;
				if (ZoomablePanel.this.hscroll.isEnabled()) {
					dx = pixel2logical_size(event.getX() - this.mouseX);
				}
				float dy = 0;
				if (ZoomablePanel.this.vscroll.isEnabled()) {
					dy = pixel2logical_size(event.getY() - this.mouseY);
				}
				if ((dx!=0)||(dy!=0)) {
					Point2f target = ZoomablePanel.this.zoomPanel.getFocusPoint();
					Rectangle2D doc_rect = getDocumentRect();
					float x = target.getX()-dx;
					float y = target.getY()-dy;

					if (x<doc_rect.getMinX()) x = target.getX();
					if (x>doc_rect.getMaxX()) x = target.getX();
					if (y<doc_rect.getMinY()) y = target.getY();
					if (y>doc_rect.getMaxY()) y = target.getY();

					if ((x!=target.getX())||(y!=target.getY())) {
						ZoomablePanel.this.zoomPanel.setFocusPoint(x,y);
						revalidateWorkspace();
						event.consume();
					}
				}
			}
			this.mouseX = event.getX();
			this.mouseY = event.getY();		
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mouseMoved(MouseEvent event) {
			this.mouseX = event.getX();
			this.mouseY = event.getY();
		}
		
		//-------------------------------------------------------------------
		// MouseWheel Interface
		//-------------------------------------------------------------------
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mouseWheelMoved(MouseWheelEvent event) {
			if (isMouseWheelAllowed() && !event.isConsumed()) {
				boolean zoomIn = (event.getWheelRotation() < 0);

				// Create a thread that permits to refresh the panel
				// after a given delay to ensure that the
				// adviced low-level of details was canceled
				MouseWheelRefresher mv = getMouseWheelRefresher();
				if (mv==null) {
					mv = new MouseWheelRefresher(event.getWhen());
					setMouseWheelRefresher(mv);
					mv.start();
				}
				else {
					mv.setEventDate(event.getWhen());
				}
				
				setLOD(Graphics2DLOD.LOW_LEVEL_OF_DETAIL);
				
				if (zoomIn) {
					
					if ((isZoomTargetComputedOnWheelMove())&&
						((this.zoominMouseX==-1)||
						(this.zoominMouseY==-1)||
						(Math.abs(this.zoominMouseX-this.mouseX)>=ZOOM_MOUSE_PRECISION)||
						(Math.abs(this.zoominMouseX-this.mouseX)>=ZOOM_MOUSE_PRECISION))) {
						float x = pixel2logical_x(this.mouseX);
						float y = pixel2logical_y(this.mouseY);
						this.zoominMouseX = this.mouseX;
						this.zoominMouseY = this.mouseY;
						ZoomablePanel.this.zoomPanel.setFocusPoint(x,y);            		
					}
					
					ZoomablePanel.this.zoomPanel.zoomIn();
				}
				else 
					ZoomablePanel.this.zoomPanel.zoomOut();
			}
		}
		
		//-------------------------------------------------------------------
		// AdjustmentListener Interface
		//-------------------------------------------------------------------
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (!isScrollMoveAllowed()) return ;
			
			setLOD( 
				e.getValueIsAdjusting()
				? Graphics2DLOD.LOW_LEVEL_OF_DETAIL
				: Graphics2DLOD.NORMAL_LEVEL_OF_DETAIL);
			
			Point2f target_point = ZoomablePanel.this.zoomPanel.getFocusPoint();
			if (target_point!=null) {
				
				Rectangle2D document_rect = getDocumentRect();
				
				if (document_rect!=null) {
					
					Rectangle2D viewport = ZoomablePanel.this.zoomPanel.getBounds();
					
					if (e.getSource()==ZoomablePanel.this.vscroll) {
						//
						// Move the target point vertically
						float target_y = (float)(e.getValue() + viewport.getHeight()/2.);
						
						target_y = pixel2logical_size(target_y) + (float)document_rect.getY();
						
						ZoomablePanel.this.zoomPanel.setFocusPoint(target_point.getX(),target_y);
						
						revalidateWorkspace();
					}
					
					else if (e.getSource()==ZoomablePanel.this.hscroll) {
						//
						// Move the target point horizontally
						float target_x = (float)(e.getValue() + viewport.getWidth()/2.);
						
						target_x = pixel2logical_size(target_x) + (float)document_rect.getX();
						
						ZoomablePanel.this.zoomPanel.setFocusPoint(target_x,target_point.getY());
						
						revalidateWorkspace();
					}
					
					// Repaint the panel
					ZoomablePanel.this.zoomPanel.repaint();
				}
			}
		}

	}
	
}
