/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.ui.undo ;




/** Abstract implementation of an Undoable.
 * This class checks if the undoable action is not died
 * when undo() or redo() is invoked.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractUndoable implements Undoable {

	private static final long serialVersionUID = -1576372782744665297L;
	
	private boolean died = false;
	
	/** {@inheritDoc}
	 */
	@Override
	public final void redo() {
		if (this.died) throw new IllegalArgumentException();
		doEdit();
	}
	
	/** Invoked to do the action (redo).
	 */
	protected abstract void doEdit();

	/** {@inheritDoc}
	 */
	@Override
	public final void undo() {
		if (this.died) throw new IllegalArgumentException();
		undoEdit();
	}

	/** Invoked to undo the action (undo).
	 */
	protected abstract void undoEdit();

	/** {@inheritDoc}
	 */
	@Override
	public void die() {
		this.died = true;
	}

}
