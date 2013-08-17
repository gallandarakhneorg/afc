/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.ui.actionmode ;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.ui.MouseCursor;
import org.arakhne.afc.ui.ZoomableContext;
import org.arakhne.afc.ui.event.KeyEvent;
import org.arakhne.afc.ui.selection.Selectable;
import org.arakhne.afc.vmutil.locale.Locale;

/** This is the abstract superclass of all editor modes.  A Mode is
 *  responsible for handling most of the events that come to the
 *  Editor.  A Mode defines a context for interperting those events.
 *  For example, a mouse drag in BaseMode
 *  will define a selection rectangle, while a mouse drag in
 *  EdgeCreationMode will drag out a
 *  rubberband line.  Placing the logic for most event handing in
 *  Modes is key to keeping the Editor source code small and
 *  managable, and also key for allowing addition of new kinds of user
 *  interactions without modifying Editor and having to
 *  integrate these modifications with other contributors
 *  modifications.
 *
 * @param <DRAW> is the type of the graphical object supported by this container.
 * @param <CANVAS> is the type of the drawing canvas.
 * @param <COLOR> is the type that is representing a color.
 * @author $Author: galland$
 * @author $Author: hannoun$
 * @author $Author: baumgartner$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class ActionMode<DRAW extends Selectable, CANVAS, COLOR> {

	private WeakReference<ActionModeManager<DRAW,CANVAS,COLOR>> modeManager ;

	/** Say if the cursor has changed.
	 */
	protected boolean cursorChanged = false;
	
	private boolean isPersistent;

	/** Construct a new Mode.
	 *
	 * @param modeManager a reference to the ModeManager that
	 *                    contains this Mode.
	 */
	public ActionMode(ActionModeManager<DRAW,CANVAS,COLOR> modeManager) {
		this.modeManager = new WeakReference<ActionModeManager<DRAW,CANVAS,COLOR>>(modeManager) ;
	}

	/** Construct a new Mode.
	 */
	public ActionMode() {
		this.modeManager = null ;
	}
	
	/** Replies if this mode stay active after it has
	 * created an edge.
	 * 
	 * @return <code>true</code> if the mode stay active;
	 * <code>false</code> if it is stopping is execution.
	 */
	public boolean isPersistent() {
		return this.isPersistent;
	}
	
	/** Set if this mode stay active after it has
	 * created an edge.
	 * 
	 * @param persistent is <code>true</code> if the mode stay active;
	 * <code>false</code> if it is stopping is execution.
	 */
	public void setPersistent(boolean persistent) {
		this.isPersistent = persistent;
	}

	/** Invoked when the mode is activated, ie put in the mode stack.
	 */
	protected void onModeActivated() {
		//
	}
	
	/** Invoked when the mode is desactivated, ie removed in the mode stack.
	 */
	protected void onModeDesactivated() {
		//
	}

	/** Repaint the mode container.
	 * 
	 * @param bounds is the area to repaint.
	 */
	public void repaint(Rectangle2f bounds) {
		getModeManagerOwner().repaint(bounds);
	}

	/** Repaint the mode container.
	 */
	public void repaint() {
		getModeManagerOwner().repaint();
	}

	/** Set the parent ModeManager of this Mode.
	 * @param modeManager a reference to the ModeManager that
	 *                    contains this Mode.
	 */
	public void setModeManager(ActionModeManager<DRAW,CANVAS,COLOR> modeManager) {
		this.modeManager = modeManager==null ? null : new WeakReference<ActionModeManager<DRAW,CANVAS,COLOR>>(modeManager);
		setCursor(getInitialCursor());
	}

	/** Reply the parent ModeManager of this Mode.
	 * @return a reference to the ModeManager that
	 *         contains this Mode.
	 */
	public ActionModeManager<DRAW,CANVAS,COLOR> getModeManager() {
		return this.modeManager==null ? null : this.modeManager.get();
	}

	/** Reply the root mode container.
	 * @return a reference to the ModeContainer.
	 */
	public ActionModeManagerOwner<DRAW,CANVAS,COLOR> getModeManagerOwner() {
		ActionModeManager<DRAW,CANVAS,COLOR> manager = getModeManager();
		if (manager==null) return null;
		return manager.getModeManagerOwner();
	}

	/** Request the key focus for the Workspace parent.
	 *  <p>
	 *  This method add to the focus manager to give 
	 *  keyboard events to the container of the mode manager.
	 */
	public void requestFocus() {
		ActionModeManager<DRAW,CANVAS,COLOR> mm = getModeManager();
		if (mm!=null) mm.getModeManagerOwner().requestFocus();
	}

	/** Returns the cursor that should be shown when this Mode starts.
	 *
	 * @return a reference to a cursor.
	 */
	@SuppressWarnings("static-method")
	public MouseCursor getInitialCursor() {
		return MouseCursor.DEFAULT;
	}

	/** Set the mouse cursor to some appropriate for this mode.
	 *
	 * @param c a reference to the new mouse cursor.
	 */
	public void setCursor(MouseCursor c) {
		this.cursorChanged = true;
		ActionModeManager<DRAW,CANVAS,COLOR> mm = getModeManager();
		if (mm!=null) mm.getModeManagerOwner().setCursor(
				c==null ? MouseCursor.DEFAULT : c);
	}   

	/** Set the mouse cursor to the default.
	 */
	protected void restoreCursor() {
		if ( this.cursorChanged ) {
			setCursor( getInitialCursor() );
			this.cursorChanged = false;
		}
	}

	/** Clean and pop this mode from the
	 *  {@link ActionModeManager ModeManager} stack.
	 *
	 * @see #setExclusive(boolean)
	 * @see ActionModeManager#endMode()
	 * @see #cleanMode()
	 */
	public void done() {
		setCursor(MouseCursor.DEFAULT);
		setExclusive( false );
		cleanMode();
		ActionModeManager<DRAW,CANVAS,COLOR> mm = getModeManager();
		if (mm!=null) mm.endMode();
	}

	/** Reply is this mode is exclusive.
	 *
	 * @return <code>true</code> if this Mode is the exclusive
	 *         mode of the enclosing {@link ActionModeManager ModeManager},
	 *         otherwise <code>false</code>.
	 * @see ActionModeManager#isExclusiveMode(ActionMode)
	 */
	public boolean isExclusive() {
		ActionModeManager<DRAW,CANVAS,COLOR> mm = getModeManager();
		if (mm==null) return false;
		return mm.isExclusiveMode( this );
	}

	/** Set this mode to exclusive state or not.
	 *
	 * @param exclu <code>true</code> if this Mode must be the exclusive
	 *              mode of the enclosing {@link ActionModeManager ModeManager},
	 *              otherwise <code>false</code>.
	 * @see ActionModeManager#setExclusiveMode(ActionMode,boolean)
	 */
	public void setExclusive( boolean exclu ) {
		ActionModeManager<DRAW,CANVAS,COLOR> mm = getModeManager();
		if (mm!=null) {
			mm.setExclusiveMode( this, exclu );
		}
	}   

	/** Ask to this mode to return to a stable state.
	 *  This method was called each time this Mode
	 *  need to be in a stable state e.g., initial
	 *  mode state.
	 */
	public void cleanMode() {
		//
	}

	/** Some Mode's should never be exited, but by default any Mode can
	 *  exit. Mode's which return <code>false</code> for canExit()
	 *  will not be popped from a {@link ActionModeManager}.
	 *
	 * @return <code>true</code> if this mode can be stopped and popped
	 *         from the ModeManager's stack.
	 */
	@SuppressWarnings("static-method")
	public boolean canExit() { 
		return true; 
	}

	/** Modes can paint themselves to give to the user some feedback.
	 *
	 * @param g the graphic context.
	 */
	public void paint(CANVAS g) {
		//
	}

	/** Replies the figure under the mouse cursor.
	 * To know is the mouse cursor is inside the
	 * shape of the figure pluease invoke
	 * {@link #isPointerInFigureShape()}.
	 * 
	 * @return the figure under the mosue cursor;
	 * or <code>null</code> if none.
	 * @see #isPointerInFigureShape()
	 */
	public DRAW getPointedFigure() {
		return getModeManager().getPointedFigure();
	}
	
	/** Replies if the mouse pointer is inside the shape
	 * of the pointer figure.
	 * To determine the pointed figure, please invoke
	 * {@link #getPointedFigure()}.
	 * 
	 * @return <code>true</code> if the mouse pointer is
	 * inside the shape; otherwise <code>false</code>.
	 * @see #getPointedFigure()
	 */
	public boolean isPointerInFigureShape() {
		return getModeManager().isPointerInFigureShape();
	}
	
	/** Replies the precision of a click in the logical coordinate space.
	 * <p>
	 * This function is an helper to access to the click precision
	 * of the action mode owner, and to translate it to the logical
	 * coordinate space.
	 * @return the click precision.
	 */
	protected float getClickPrecision() {
		float prec = getModeManagerOwner().getClickPrecision();
		ZoomableContext zc = getModeManagerOwner().getZoomableContext();
		if (zc!=null) {
			prec = zc.pixel2logical_size(prec);
		}
		return prec;
	}
	
	/** Delegate the execution to the specified mode.
	 * 
	 * @param delegation is the type of the mode to create and delegate to.
	 * @return the delegated mode.
	 */
	protected <M extends ActionMode<DRAW,CANVAS,COLOR>> M createDelegation(Class<M> delegation) {
		M mode = null;
		try {
			Constructor<M> cons = delegation.getConstructor(ActionModeManager.class);
			mode = cons.newInstance(getModeManager());
		}
		catch (Throwable e) {
			//
		}
		if (mode==null) {
			try {
				mode = delegation.newInstance();
			}
			catch (Throwable e) {
				throw new ActionModeException(Locale.getString("NOT_MODE_TRIGGER"), e); //$NON-NLS-1$
			}
			mode.setModeManager(getModeManager());
		}
		assert(mode!=null);
		getModeManager().beginMode(mode);
		return mode;
	}
	
	/** Delegate the execution to the specified mode.
	 * 
	 * @param mode is the mode to delegate to.
	 */
	protected <M extends ActionMode<DRAW,CANVAS,COLOR>> void createDelegation(M mode) {
		assert(mode!=null);
		mode.setModeManager(getModeManager());
		getModeManager().beginMode(mode);
		repaint();
	}

	/** Force the mode manager to consider the specified event.
	 * 
	 * @param event
	 */
	protected void forcePointerEvent(ActionPointerEvent event) {
		getModeManager().updatePointerInfo(event, true);
	}
	
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
    public void keyTyped(KeyEvent e) {
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
    public void keyPressed(KeyEvent e) {
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
    public void keyReleased(KeyEvent e) {
    	//
    }

    /**
     * Invoked when the pointer button has been clicked (pressed
     * and released) on a component.
     * <p>
     * The POINTER_CLICKED event may be unavailable on several components.
     * You must read the document of the component on which the action management
     * is applied to be sure that the POINTER_CLICKED event is handled.
     * 
     * @param e
     * @see #pointerLongClicked(ActionPointerEvent)
     */
    public void pointerClicked(ActionPointerEvent e) {
    	//
    }

    /**
     * Invoked when the pointer button has been long clicked (pressed
     * and released) on a component.
     * <p>
     * The POINTER_LONG_CLICKED event may be unavailable on several components.
     * You must read the document of the component on which the action management
     * is applied to be sure that the POINTER_LONG_CLICKED event is handled.
     * 
     * @param e
     * @see #pointerClicked(ActionPointerEvent)
     */
    public void pointerLongClicked(ActionPointerEvent e) {
    	//
    }

    /**
     * Invoked when a pointer button has been pressed on a component.
     * <p>
     * The POINTER_PRESSED event may be unavailable on several components.
     * You must read the document of the component on which the action management
     * is applied to be sure that the POINTER_PRESSED event is handled.
     * 
     * @param e
     */
    public void pointerPressed(ActionPointerEvent e) {
    	//
    }

    /**
     * Invoked when a pointer button has been released on a component.
     * <p>
     * The POINTER_RELEASED event may be unavailable on several components.
     * You must read the document of the component on which the action management
     * is applied to be sure that the POINTER_RELEASED event is handled.
     * 
     * @param e
     */
    public void pointerReleased(ActionPointerEvent e) {
    	//
    }

    /**
     * Invoked when the mouse is moved with a button down.
     * <p>
     * The POINTER_DRAGGED event may be unavailable on several components.
     * You must read the document of the component on which the action management
     * is applied to be sure that the POINTER_DRAGGED event is handled.
     * 
     * @param e
     */
    public void pointerDragged(ActionPointerEvent e) {
    	//
    }

    /**
     * Invoked when the mouse is moved with no button down.
     * <p>
     * The POINTER_MOVED event may be unavailable on several components.
     * You must read the document of the component on which the action management
     * is applied to be sure that the POINTER_MOVED event is handled.
     * 
     * @param e
     */
    public void pointerMoved(ActionPointerEvent e) {
    	//
    }

}
