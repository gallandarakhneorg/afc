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
package org.arakhne.afc.ui.android.zoom;



/** Abstract implementation of a DocumentWrapper
 * that is supporting only a single change listener.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public abstract class AbstractDocumentWrapper implements DocumentWrapper {
	
	private ChangeListener listener = null;
	
	/**
	 */
	public AbstractDocumentWrapper() {
		//
	}
	
	@Override
	public void addChangeListener(ChangeListener listener) {
		if (this.listener!=null)
			throw new IllegalStateException();
		this.listener = listener;
	}

	@Override
	public void removeChangeListener(ChangeListener listener) {
		if (this.listener!=listener)
			throw new IllegalStateException();
		this.listener = null;
	}
	
	/** Notifies the listener about a change in the geometry of the document.
	 */
	public void fireChange() {
		if (this.listener!=null) {
			this.listener.stateChanged(this);
		}
	}

}
