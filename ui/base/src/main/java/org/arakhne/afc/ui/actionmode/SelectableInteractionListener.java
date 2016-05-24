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

package org.arakhne.afc.ui.actionmode ;

import java.util.EventListener;

import org.arakhne.afc.ui.selection.Selectable;

/** This interface represents a listener on the figure interaction events.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public interface SelectableInteractionListener extends EventListener {

	/** Invoked when the user performs an action on a figure.
	 * 
	 * @param event
	 */
	public void actionPerformed(SelectableInteractionEvent event);
	
	/** Invoked when the user performs a popup action on a figure.
	 * 
	 * @param event
	 */
	public void popupPerformed(SelectableInteractionEvent event);

	/** Invoked when the user performs a deletion action of a figure and not
	 * of the associated model object.
	 * 
	 * @param figure is the figure to delete.
	 * @param deleteModel indicates if the underlying model object must be also deleted.
	 * @return <code>true</code> if the figure can be deleted, <code>false</code> otherwise.
	 */
	public boolean figureDeletionPerformed(Selectable figure, boolean deleteModel);


}
