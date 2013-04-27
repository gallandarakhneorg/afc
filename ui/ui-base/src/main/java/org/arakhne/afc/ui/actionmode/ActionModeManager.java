/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.ui.actionmode ;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.arakhne.afc.ui.ZoomableContext;
import org.arakhne.afc.ui.event.KeyEvent;
import org.arakhne.afc.ui.event.PointerEvent;
import org.arakhne.afc.ui.selection.Selectable;
import org.arakhne.afc.util.ListenerCollection;

/** ModeManager keeps track of all the 
 *  {@link ActionMode Modes} for a given workspace.
 *  Events are passed to the Modes for handling.  The submodes are
 *  prioritized according to their order on a stack, i.e., the last
 *  Mode added gets the first chance to handle an Event.  
 *  <p>
 *  This ModeManager takes into account an exclusive Mode. An exclusive
 *  mode is a {@link ActionMode Mode} was received all events before the
 *  stacked modes. If an event was not eated by the exclusive mode,
 *  the exclusive mode was destroy and the event was ignored.
 *
 * @param <DRAW> is the type of the data supported by this container.
 * @param <CANVAS> is the type of the drawing canvas.
 * @param <COLOR> is the type that is representing a color.
 * @author $Author: galland$
 * @author $Author: hannoun$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ActionModeManager<DRAW extends Selectable, CANVAS, COLOR> {

	private final WeakReference<ActionModeManagerOwner<DRAW,CANVAS,COLOR>> container;
	
	private final LinkedList<ActionMode<? super DRAW,CANVAS,COLOR>> modeStack = new LinkedList<ActionMode<? super DRAW,CANVAS,COLOR>>();
	
	private final UUID viewID;
	
	private ActionMode<? super DRAW,CANVAS,COLOR> exclusiveMode = null;
	
	private boolean isFigureUnderTheMouseComputed = false;
	private WeakReference<DRAW> figureUnderTheMouse = null;
	
	private boolean isForceHitResetWhenRelease = true;
	
	private final ListenerCollection<EventListener> listeners = new ListenerCollection<EventListener>();
	
	private ActionPointerEvent lastPointerEvent = null;

	/**  Construct a ModeManager with no modes.
	 *
	 * @param viewID is the identifier of the view associated to this manager.
	 * @param component is a reference to the component that is 
	 * containing this mode manager.
	 */
	public ActionModeManager(UUID viewID, ActionModeManagerOwner<DRAW,CANVAS,COLOR> component) {
		assert(viewID!=null);
		this.viewID = viewID;
		assert(component!=null);
		this.container = new WeakReference<ActionModeManagerOwner<DRAW,CANVAS,COLOR>>(component); 
	}
	
	/** Replies an unmodifiable list of the modes.
	 * 
	 * @return the modes.
	 */
	public List<ActionMode<? super DRAW,CANVAS,COLOR>> getModes() {
		return Collections.unmodifiableList(this.modeStack);
	}
	
	/** Replies the first mode of the given type from the top
	 * of the mode stack.
	 *
	 * @param type is the type of the mode to retreive.
	 * @return the mode or <code>null</code> if not found.
	 */
	public <M extends ActionMode<? super DRAW,CANVAS,COLOR>> M getModeFromTop(Class<M> type) {
		Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
		ActionMode<? super DRAW,CANVAS,COLOR> mode;
		while (iterator.hasNext()) {
			mode = iterator.next();
			if (type.isInstance(mode))
				return type.cast(mode);
		}
		return null;
	}

	/** Replies the first mode of the given type from the bottom
	 * of the mode stack.
	 *
	 * @param type is the type of the mode to retreive.
	 * @return the mode or <code>null</code> if not found.
	 */
	public <M extends ActionMode<? super DRAW,CANVAS,COLOR>> M getModeFromBottom(Class<M> type) {
		Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.iterator();
		ActionMode<? super DRAW,CANVAS,COLOR> mode;
		while (iterator.hasNext()) {
			mode = iterator.next();
			if (type.isInstance(mode))
				return type.cast(mode);
		}
		return null;
	}

	/** Add listener on the mode events.
	 * 
	 * @param listener
	 */
	public void addModeListener(ActionModeListener listener) {
		this.listeners.add(ActionModeListener.class, listener);
	}
	
	/** Remove listener on the mode events.
	 * 
	 * @param listener
	 */
	public void removeModeListener(ActionModeListener listener) {
		this.listeners.remove(ActionModeListener.class, listener);
	}
	
	/** Add listener on the figure interaction events.
	 * 
	 * @param listener
	 */
	public void addSelectableInteractionListener(SelectableInteractionListener listener) {
		this.listeners.add(SelectableInteractionListener.class, listener);
	}
	
	/** Remove listener on the figure interaction events.
	 * 
	 * @param listener
	 */
	public void removeSelectableInteractionListener(SelectableInteractionListener listener) {
		this.listeners.remove(SelectableInteractionListener.class, listener);
	}

	/** Notifies the listeners about the activation of a mode.
	 * 
	 * @param mode
	 */
	private void fireModeActivated(ActionMode<?,?,?> mode) {
		for(ActionModeListener listener : this.listeners.getListeners(ActionModeListener.class)) {
			listener.modeActivated(mode);
		}
	}

	/** Notifies the listeners about the desactivation of a mode.
	 * 
	 * @param mode
	 */
	private void fireModeDesactivated(ActionMode<?,?,?> mode) {
		for(ActionModeListener listener : this.listeners.getListeners(ActionModeListener.class)) {
			listener.modeDesactivated(mode);
		}
	}

	/** Notifies the listeners about the action performing on a figure.
	 * 
	 * @param figure is the figure on which the action was performed.
	 */
	public void fireActionPerformed(Collection<? extends DRAW> figure) {
		SelectableInteractionEvent event = new SelectableInteractionEvent(this,figure,
				getModeManagerOwner().isEditable());
		if (figure instanceof SelectableInteractionListener)
			((SelectableInteractionListener)figure).actionPerformed(event);
		for(SelectableInteractionListener listener : this.listeners.getListeners(SelectableInteractionListener.class)) {
			if (figure!=listener) listener.actionPerformed(event);
		}
	}

	/** Notifies the listeners about the action performing on a figure.
	 * 
	 * @param figure is the figure on which the action was performed.
	 * @param pointerEvent is the event causing the action performing.
	 */
	public void fireActionPerformed(Collection<? extends DRAW> figure, ActionPointerEvent pointerEvent) {
		SelectableInteractionEvent event = new SelectableInteractionEvent(this, figure, pointerEvent,
				getModeManagerOwner().isEditable());
		if (figure instanceof SelectableInteractionListener)
			((SelectableInteractionListener)figure).actionPerformed(event);
		for(SelectableInteractionListener listener : this.listeners.getListeners(SelectableInteractionListener.class)) {
			if (figure!=listener) listener.actionPerformed(event);
		}
	}

	/** Notifies the listeners about the action performing on a figure.
	 * 
	 * @param figure is the figure on which the action was performed.
	 * @param keyEvent is the event causing the action performing.
	 */
	public void fireActionPerformed(Collection<? extends DRAW> figure, KeyEvent keyEvent) {
		assert(figure!=null);
		SelectableInteractionEvent event = new SelectableInteractionEvent(this, figure, keyEvent,
				getModeManagerOwner().isEditable());
		if (figure instanceof SelectableInteractionListener)
			((SelectableInteractionListener)figure).actionPerformed(event);
		for(SelectableInteractionListener listener : this.listeners.getListeners(SelectableInteractionListener.class)) {
			if (figure!=listener) listener.actionPerformed(event);
		}
	}

	/** Notifies the listeners about the popup action performing on a figure.
	 * 
	 * @param pointerEvent is the cause of the popup opening.
	 * @param figure is the figure on which the action was performed. It may be
	 * <code>null</code> if the popup action was performed on the background.
	 */
	public void firePopupPerformed(ActionPointerEvent pointerEvent, DRAW figure) {
		SelectableInteractionEvent event = new SelectableInteractionEvent(this,
				Collections.singleton(figure), pointerEvent,
				getModeManagerOwner().isEditable());
		if (figure instanceof SelectableInteractionListener)
			((SelectableInteractionListener)figure).popupPerformed(event);
		for(SelectableInteractionListener listener : this.listeners.getListeners(SelectableInteractionListener.class)) {
			if (figure!=listener) listener.popupPerformed(event);
		}
	}

	/** Notifies the listeners about an error that occurs in one
	 * of the JFigureEditor components.
	 * This function is a wrapper to the ModeContainer function.
	 * 
	 * @param error
	 * @since 16.0
	 */
	public void fireError(Throwable error) {
		getModeManagerOwner().fireError(error);
	}

	/** Notifies the listeners about the a request to delete a figure and not of the associated
	 * model object.
	 * 
	 * @param figure is the figure to delete.
	 * @param deleteModel indicates if the underlying model object must be also deleted.
	 * @return <code>true</code> if all the listeners accept the deletion
	 * of the figure; <code>false</code> if at least one listener refuses
	 * the deletion of the figure. 
	 */
	public boolean fireFigureDeletionPerformed(DRAW figure, boolean deleteModel) {
		boolean auth = true;
		for(SelectableInteractionListener listener : this.listeners.getListeners(SelectableInteractionListener.class)) {
			if (!listener.figureDeletionPerformed(figure, deleteModel)) {
				auth = false;
			}
		}
		return auth;
	}

	/** Replies the identifier of the view associated to this mode manager.
	 * 
	 * @return the identifier of the view.
	 */
	public UUID getViewID() {
		return this.viewID;
	}
	
	/** Replies the component that is containing this mode manager.
	 * 
	 * @return the component.
	 */
	public ActionModeManagerOwner<DRAW,CANVAS,COLOR> getModeManagerOwner() {
		return this.container.get();
	}
	
	/** Paint each mode in the stack: bottom to top.
	 *
	 * @param g the graphic context.
	 */
	public void paint(CANVAS g) {
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.paint(g);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.iterator();
			while (iterator.hasNext()) {
				iterator.next().paint(g);
			}
		}
	}

	/** Reply if the given {@link ActionMode Mode} was the exclusive one.
	 *
	 * @param mode
	 * @return <code>true</code> if <var>mode</var> was the exclusive
	 *         mode, otherwise <code>false</code>.
	 */
	public boolean isExclusiveMode( ActionMode<? super DRAW,CANVAS,COLOR> mode ) {
		return this.exclusiveMode!=null && this.exclusiveMode==mode;
	}

	/** Reply if this ModeManager have an exclusive Mode.
	 *
	 * @return <code>true</code> if this ModeManager have an
	 *         exclusive mode, otherwise <code>false</code>.
	 */
	public boolean isExclusiveMode() {
		return this.exclusiveMode != null;
	}

	/** Set or unset exclusive Mode for the given Mode.
	 *  <p>
	 *  If <var>exclu</var> was <code>true</code> then
	 *  <var>mode</var> is the new exclusive mode <b>only if</b>
	 *  it is already the exclusive mode or there are no
	 *  exclusive mode.
	 *  <p>
	 *  If <var>exclu</var> was <code>false</code> then
	 *  the current exclusive mode was unset <b>only if</b>
	 *  <var>mode</var> was the current exclusive mode.
	 *
	 * @param mode a reference to the new/old exclusive mode.
	 * @param isExclusive <code>true</code> if you would like to set
	 *              <var>mode</var> as an exclusive mode,
	 *              otherwise <code>false</code>.
	 */
	void setExclusiveMode( ActionMode<? super DRAW,CANVAS,COLOR> mode, boolean isExclusive ) {
		if ( isExclusive ) {
			if ( this.exclusiveMode != null ) {
				this.exclusiveMode.cleanMode();
			}
			this.exclusiveMode = mode;
		}
		else if ( this.exclusiveMode == mode ) {
			unsetExclusiveMode();
		}
	}

	/** Unset exclusive Mode.
	 */
	void unsetExclusiveMode() { 
		this.exclusiveMode = null; 
	}


	/** Set the editor's current mode to the given Mode instance.
	 *
	 * @param mode the mode to push to the stack.
	 * @return <code>true</code> if the mode was started; otherwise <code>false</code>.
	 */
	public boolean beginMode(ActionMode<DRAW,CANVAS,COLOR> mode) {
		if (this.modeStack.contains(mode)) return false;
		mode.setModeManager(this) ;
		this.modeStack.addLast(mode) ;
		mode.onModeActivated();
		fireModeActivated(mode);
		return true;
	}

	/** Remove the current mode, and set the graph editor's mode to
	 * the most topless mode.
	 */
	void endMode() {
		if (!this.modeStack.isEmpty()) {
			ActionMode<? super DRAW,CANVAS,COLOR> mode = this.modeStack.removeLast();
			if ( isExclusiveMode( mode ) ) {
				unsetExclusiveMode();
			}
			mode.onModeDesactivated();
			fireModeDesactivated(mode);
			mode.setModeManager(null);
		}
	}

	/** This method permits to reset this manager by remove temp modes
	 *  and clean permanent modes.
	 */
	public void resetModes() {
		unsetExclusiveMode();
		
		List<ActionMode<? super DRAW,CANVAS,COLOR>> removedModes = new ArrayList<ActionMode<? super DRAW,CANVAS,COLOR>>();

		boolean stop = false;
		Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
		while (!stop && iterator.hasNext()) {
			ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
			if (!m.canExit()) {
				stop = true;
			}
			else {
				iterator.remove();
				m.cleanMode();
				removedModes.add(m);
			}
		}
		
		for(ActionMode<? super DRAW,CANVAS,COLOR> m : removedModes) {
			m.onModeDesactivated();
			fireModeDesactivated(m);
			m.setModeManager(null);
		}
	}
	
	/**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     * 
     * @param e
     */
    public void keyTyped(KeyEvent e) {
    	this.lastPointerEvent = null;
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.keyTyped(e);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
			while (iterator.hasNext()) {
				ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
				m.keyTyped(e);
				if (e.isConsumed()) return;
			}
		}
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     * 
     * @param e
     */
    public void keyPressed(KeyEvent e) {
    	this.lastPointerEvent = null;
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.keyPressed(e);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
			while (iterator.hasNext()) {
				ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
				m.keyPressed(e);
				if (e.isConsumed()) return;
			}
		}
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     * 
     * @param e
     */
    public void keyReleased(KeyEvent e) {
    	this.lastPointerEvent = null;
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.keyReleased(e);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
			while (iterator.hasNext()) {
				ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
				m.keyReleased(e);
				if (e.isConsumed()) return;
			}
		}
    }

    /**
     * Invoked when the pointer button has been clicked (pressed
     * and released) on a component.
     * 
     * @param e
     * @see #pointerLongClicked(PointerEvent)
     */
    public void pointerClicked(PointerEvent e) {
		updatePointerInfo(e, false);
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.pointerClicked(this.lastPointerEvent);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
			while (iterator.hasNext()) {
				ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
				m.pointerClicked(this.lastPointerEvent);
				if (e.isConsumed()) return;
			}
		}
    }

    /**
     * Invoked when the pointer button has been long clicked (pressed
     * and released) on a component.
     * 
     * @param e
     * @see #pointerClicked(PointerEvent)
     */
    public void pointerLongClicked(PointerEvent e) {
    	updatePointerInfo(e, false);
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.pointerLongClicked(this.lastPointerEvent);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
			while (iterator.hasNext()) {
				ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
				m.pointerLongClicked(this.lastPointerEvent);
				if (e.isConsumed()) return;
			}
		}
    }

    /**
     * Invoked when a pointer button has been pressed on a component.
     * 
     * @param e
     */
    public void pointerPressed(PointerEvent e) {
    	if (this.isForceHitResetWhenRelease) {
    		this.isFigureUnderTheMouseComputed = false;
    		this.figureUnderTheMouse = null;
    	}
		updatePointerInfo(e, false);
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.pointerPressed(this.lastPointerEvent);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
			while (iterator.hasNext()) {
				ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
				m.pointerPressed(this.lastPointerEvent);
				if (e.isConsumed()) return;
			}
		}
    }

    /**
     * Invoked when a pointer button has been released on a component.
     * 
     * @param e
     */
    public void pointerReleased(PointerEvent e) {
		updatePointerInfo(e, false);
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.pointerReleased(this.lastPointerEvent);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
			while (iterator.hasNext()) {
				ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
				m.pointerReleased(this.lastPointerEvent);
				if (e.isConsumed()) return;
			}
		}
		// This is convenient for devices that cannot handle the POINTER_MOVE events.
		if (this.isForceHitResetWhenRelease) {
			this.isFigureUnderTheMouseComputed = false;
			this.figureUnderTheMouse = null;
		}
    }
    
    /** Force or not to reset the hit figure when pointer is released.
     * <p>
     * By default, the hit figure is determine when the pointer is
     * {@link #pointerDragged(PointerEvent) dragging} and when
     * the pointer is {@link #pointerMoved(PointerEvent)}.
     * For the devices that cannot handle the Move event (eg. Android OS),
     * the hit figure must be reset when the pointer is released to ensure
     * that the mode manager will try to retreive a hit figure when
     * the pointer will be pressed again.
     * So, depending on the event manager of the component that is using this
     * mode manager, we recommend to call this function with
     * <code>true</code> as parameter when the POINTER_MOVE event cannot be fired.
     * 
     * @param resetHit
     */
    public void setResetHitFigureWhenPointerReleased(boolean resetHit) {
    	this.isForceHitResetWhenRelease = resetHit;
    }

    /** Replies if the hit figure is reset when pointer is released.
     * 
     * @return <code>true</code> when the hit figure is reset.
     */
    public boolean isResetHitFigureWhenPointerReleased() {
    	return this.isForceHitResetWhenRelease;
    }

    /**
     * Invoked when the mouse is moved with a button down.
     * 
     * @param e
     */
    public void pointerDragged(PointerEvent e) {
		updatePointerInfo(e, true);
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.pointerDragged(this.lastPointerEvent);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
			while (iterator.hasNext()) {
				ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
				m.pointerDragged(this.lastPointerEvent);
				if (e.isConsumed()) return;
			}
		}
    }

    /**
     * Invoked when the mouse is moved with no button down.
     * 
     * @param e
     */
    public void pointerMoved(PointerEvent e) {
		updatePointerInfo(e, true);
		if (this.exclusiveMode!=null) {
			this.exclusiveMode.pointerMoved(this.lastPointerEvent);
		}
		else {
			Iterator<ActionMode<? super DRAW,CANVAS,COLOR>> iterator = this.modeStack.descendingIterator();
			while (iterator.hasNext()) {
				ActionMode<? super DRAW,CANVAS,COLOR> m = iterator.next();
				m.pointerMoved(this.lastPointerEvent);
				if (e.isConsumed()) return;
			}
		}
    }

	private void updatePointedData() {
		if (!this.isFigureUnderTheMouseComputed
			&& this.lastPointerEvent!=null) {
			DRAW fig = null;
			this.figureUnderTheMouse = null;
			ActionModeManagerOwner<DRAW,CANVAS,COLOR> container = getModeManagerOwner();
			if (container!=null) {
				if (this.lastPointerEvent.isToolAreaSupported()) {
					for(int i=0; fig==null && i<this.lastPointerEvent.getPointerCount(); ++i) {
						fig = container.getFigureOn(this.lastPointerEvent.getToolArea(i));
					}
				}
				else {
					fig = container.getFigureAt(this.lastPointerEvent.getX(), this.lastPointerEvent.getY());
				}
				if (fig!=null) {
					this.figureUnderTheMouse = new WeakReference<DRAW>(fig);
				}
			}
			this.isFigureUnderTheMouseComputed = true;
		}
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
		updatePointedData();
		return this.figureUnderTheMouse==null ? 
				null : this.figureUnderTheMouse.get();
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
		updatePointedData();
		return getPointedFigure()!=null;
	}
	
	/** Update the internal data conerning the pointer.
	 * This function updates {@link #lastPointerEvent}.
	 * 
	 * @param e
	 * @param invalidateHitFigure is <code>true</code> to force
	 * to clear any information about a pointed figure.
	 */
	void updatePointerInfo(PointerEvent e, boolean invalidateHitFigure) {
		if (invalidateHitFigure) {
			this.isFigureUnderTheMouseComputed = false;
			this.figureUnderTheMouse = null;
		}
		ZoomableContext zc = null;
		ActionModeManagerOwner<DRAW,CANVAS,COLOR> container = getModeManagerOwner();
		if (container!=null) {
			zc = container.getZoomableContext();
		}
		this.lastPointerEvent = new ActionPointerEvent(e, zc);
	}
	
	/** Replies the last pointer event encountered by the manager.
	 * 
	 * @return the last pointer found.
	 */
	public ActionPointerEvent getLastPointerEvent() {
		return this.lastPointerEvent;
	}
	
}
