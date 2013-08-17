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

import org.arakhne.afc.ui.selection.Selectable;


/** This interface represents a listener on the figure interaction events.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SelectableInteractionAdapter implements SelectableInteractionListener {

	/** {@inheritDoc}
	 */
	@Override
	public void actionPerformed(SelectableInteractionEvent event) {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void popupPerformed(SelectableInteractionEvent event) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean figureDeletionPerformed(Selectable figure,
			boolean deleteModel) {
		return true;
	}

}
