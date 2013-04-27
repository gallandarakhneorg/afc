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

import java.util.Collection;
import java.util.Set;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.ui.MouseCursor;
import org.arakhne.afc.ui.ZoomableContext;
import org.arakhne.afc.ui.selection.Selectable;
import org.arakhne.afc.ui.selection.SelectionManager;
import org.arakhne.afc.ui.undo.UndoManager;
import org.arakhne.afc.ui.undo.Undoable;

/** This interface describes a container of modes.
 *
 * @param <DRAW> is the type of the data supported by this container.
 * @param <CANVAS> is the type of the drawing canvas.
 * @param <COLOR> is the type that is representing a color.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ActionModeManagerOwner<DRAW extends Selectable, CANVAS, COLOR> {

	/** Replies if the mode container want to remove the model
	 * objects when a figure is removed from the view.
	 * 
	 * @return <code>true</code> if both the figure and the
	 * model objects should be removed at the same time,
	 * <code>false</code> if only the figure should be removed
	 * (not the model object).
	 */
	public boolean isAlwaysRemovingModelObjects();
	
	/** Replies the associated UI component.
	 * 
	 * @return the associated UI component.
	 */
	public Object getUIComponent();
	
	/** Request the keyboard focus.
	 */
	public void requestFocus();
	
	/** Replies the selection manager.
	 * 
	 * @return the selection manager.
	 */
	public SelectionManager<? super DRAW> getSelectionManager();

	/** Replies the mode manager.
	 * 
	 * @return the mode manager.
	 */
	public ActionModeManager<DRAW,CANVAS,COLOR> getModeManager();
	
	/**
	 * Replies the manager of undoable actions.
	 * 
	 * @return the undo manager.
	 */
	public UndoManager getUndoManager();

	/** Change the cursor associated to the mode container.
	 * 
	 * @param cursor is the new cursor.
	 */
	public void setCursor(MouseCursor cursor);
	
	/** Repaint the mode container.
	 * 
	 * @param bounds is the area to repaint.
	 */
	public void repaint(Rectangle2f bounds);

	/** Repaint the mode container.
	 */
	public void repaint();
	
	/** Replies the precision of the clicks (in pixels).
	 * 
	 * @return the precision of the clicks.
	 */
	public float getClickPrecision();

	/** Replies the zoomable context used by this container, if one.
	 * 
	 * @return zoomable context or <code>null</code>.
	 */
	public ZoomableContext getZoomableContext();

	/**
	 * Transfers the currently selected figures
	 * to the system clipboard, removing the contents
	 * from the model.  The current selection is reset.  Does nothing
	 * for <code>null</code> selections.
	 */
	public void cut();

	/**
	 * Transfers the currently selected figures
	 * to the system clipboard, leaving the contents
	 * in the text model.  The current selection remains intact.
	 * Does nothing for <code>null</code> selections.
	 */
	public void copy();

	/**
	 * Transfers the contents of the system clipboard into the
	 * associated graph.  If the clipboard is empty, does nothing.
	 */
	public void paste();
	
	/** Replies if this viewer is interactively editable.
	 * 
	 * @return <code>true</code> if this viewer is editable; otherwise <code>false</code>.
	 */
	public boolean isEditable();

	/** Replies if the selection manager is enabled.
	 * 
	 * @return <code>true</code> if the selection manager is enabled.
	 */
	public boolean isSelectionEnabled();

	/** Notifies the listeners about an error that occurs in one
	 * of the JFigureEditor components.
	 * 
	 * @param error
	 */
	public void fireError(Throwable error);
	
	/** Replies the number of figures in the container.
	 * 
	 * @return the number of figures.
	 */
	public int getFigureCount();

	/** Replies the figures in the container.
	 * 
	 * @return the figures.
	 */
	public Collection<? extends DRAW> getFigures();

	/** Replies the figure at the specified index in the container.
	 * 
	 * @param index
	 * @return the figure.
	 */
	public DRAW getFigureAt(int index);

	/** Replies the figure that has the specified point in
	 * its shape. If you want to test the point against
	 * the bounds of the figures, please use
	 * {@link #getFigureWithBoundsAt(float, float)}.
	 * 
	 * @param x
	 * @param y
	 * @return the hit figure, or <code>null</code> if none.
	 * @see #getFigureWithBoundsAt(float, float)
	 * @see #getFigureOn(Shape2f)
	 * @see #getFigureIn(Rectangle2f)
	 * @see #getFiguresOn(Shape2f)
	 * @see #getFiguresIn(Rectangle2f)
	 */
	public DRAW getFigureAt(float x, float y);

	/** Replies the figure that is intersecting the specified
	 * area.
	 * 
	 * @param area
	 * @return the hit figure, or <code>null</code> if none.
	 * @see #getFigureAt(float, float)
	 * @see #getFigureIn(Rectangle2f)
	 * @see #getFiguresOn(Shape2f)
	 * @see #getFiguresIn(Rectangle2f)
	 */
	public DRAW getFigureOn(Shape2f area);

	/** Replies the figure that is inside the specified
	 * area.
	 * 
	 * @param area
	 * @return the hit figure, or <code>null</code> if none.
	 * @see #getFigureOn(Shape2f)
	 * @see #getFigureAt(float, float)
	 * @see #getFiguresOn(Shape2f)
	 * @see #getFiguresIn(Rectangle2f)
	 */
	public DRAW getFigureIn(Rectangle2f area);

	/** Replies the figure that has its bounds with the specified
	 * point inside. If you want to test the point against
	 * the shape of the figures, please use
	 * {@link #getFigureAt(float, float)}.
	 * 
	 * @param x
	 * @param y
	 * @return the hit figure, or <code>null</code> if none.
	 */
	public DRAW getFigureWithBoundsAt(float x, float y);

	/** Replies the figures that are intersecting the specified bounds.
	 * 
	 * @param bounds
	 * @return the hit figures, never <code>null</code>.
	 * @see #getFigureOn(Shape2f)
	 * @see #getFigureIn(Rectangle2f)
	 * @see #getFigureAt(float, float)
	 * @see #getFiguresIn(Rectangle2f)
	 */
	public Set<DRAW> getFiguresOn(Shape2f bounds);

	/** Replies the figures that are inside the specified bounds.
	 * 
	 * @param bounds
	 * @return the hit figures, never <code>null</code>.
	 * @see #getFigureOn(Shape2f)
	 * @see #getFigureIn(Rectangle2f)
	 * @see #getFiguresOn(Shape2f)
	 * @see #getFigureAt(float, float)
	 */
	public Set<DRAW> getFiguresIn(Rectangle2f bounds);

	/** Replies the background color used for the selection of objects.
	 * 
	 * @return the selection color.
	 */
	public COLOR getSelectionBackground();

	/** Replies the background color used for the selection of objects.
	 * 
	 * @return the selection color.
	 */
	public COLOR getSelectionForeground();

	/** Removes the specified figure.
	 * 
	 * @param deleteModel is <code>true</code> if the underlying model object
	 * associated with the figure must be also deleted; <code>false</code> if
	 * the underlying object must not be deleted.
	 * @param disconnectFigureAndModel indicates if the figure and the model
	 * object may be disconnected or not.
	 * @param figure is the figure to remove.
	 * @return the undo action that may be used to cancel the deletions.
	 */
	public Undoable removeFigure(boolean deleteModel, boolean disconnectFigureAndModel, DRAW figure);
	
	/** Removes the specified figure.
	 * 
	 * @param deleteModel is <code>true</code> if the underlying model object
	 * associated with the figure must be also deleted; <code>false</code> if
	 * the underlying object must not be deleted.
	 * @param disconnectFigureAndModel indicates if the figure and the model
	 * object may be disconnected or not.
	 * @param figures are the figures to remove.
	 * @return the undo action that may be used to cancel the deletions.
	 */
	public Undoable removeFigures(boolean deleteModel, boolean disconnectFigureAndModel, Iterable<? extends DRAW> figures);

	/** Add a figure.
	 * 
	 * @param figure is the figure to add.
	 * @return the undoable edit.
	 */
	public Undoable addFigure(DRAW figure);

}
