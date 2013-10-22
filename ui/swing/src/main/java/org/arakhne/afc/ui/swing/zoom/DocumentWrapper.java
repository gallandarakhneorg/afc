/* $Id$
 * 
 * This is a part of NetEditor project from Arakhne.org:
 * package org.arakhne.neteditor.zoompanel.
 * 
 * Copyright (C) 2013  St&eacute;phane GALLAND
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
package org.arakhne.afc.ui.swing.zoom;

import javax.swing.event.ChangeListener;

import org.arakhne.afc.math.continous.object2d.Rectangle2f;


/** This interface represents a piece of document
 * that could be graphically rendered.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface DocumentWrapper {
	
	/** Replies the bounds of the document.
	 * 
	 * @return the bounds in the logical coordinate space (not necessary the screen space);
	 * or <code>null</code> if the bounds are not known.
	 */
	public Rectangle2f getDocumentBounds();
	
	/**
	 *  Add a listener on the changes in this document.
	 *  
	 * @param listener
	 */
	public void addChangeListener(ChangeListener listener);
	
	/**
	 *  Remove a listener on the changes in this document.
	 *  
	 * @param listener
	 */
	public void removeChangeListener(ChangeListener listener);

}
