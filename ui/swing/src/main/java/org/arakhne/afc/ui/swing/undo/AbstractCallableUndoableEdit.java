/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.ui.swing.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;

import org.arakhne.afc.ui.undo.Undoable;

/** Abstract implementation of an undoable edit that is also
 * directly callable to run/redo the action.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public abstract class AbstractCallableUndoableEdit extends AbstractUndoableEdit implements Undoable {

	private static final long serialVersionUID = -1330907958341724667L;

	/**
	 */
	public AbstractCallableUndoableEdit() {
		//
	}
	
	@Override
	public final void redo() throws CannotRedoException {
		super.redo();
		doEdit();
	}

	@Override
	public final void undo() throws CannotRedoException {
		super.undo();
		undoEdit();
	}
	
	/** Do the edition.
	 * This function is automatically invoked by {@link #redo()}.
	 * In opposite to {@link #redo()}, this function does not
	 * throw an exception when the edit was never done before.
	 */
	public abstract void doEdit();

	/** Undo the edition.
	 * This function is automatically invoked by {@link #undo()}.
	 * In opposite to {@link #undo()}, this function does not
	 * throw an exception when the edit was never done before.
	 */
	public abstract void undoEdit();

}
