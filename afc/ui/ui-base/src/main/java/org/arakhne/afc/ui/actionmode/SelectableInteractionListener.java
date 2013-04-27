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

import java.util.EventListener;

import org.arakhne.afc.ui.selection.Selectable;

/** This interface represents a listener on the figure interaction events.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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
