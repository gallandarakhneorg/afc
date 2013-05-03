/* $Id$
 * 
 * This is a part of NetEditor project from Arakhne.org:
 * package org.arakhne.neteditor.zoompanel.
 * 
 * Copyright (C) 2002  St&eacute;phane GALLAND, Mahdi Hannoun
 * Copyright (C) 2004-2012  St&eacute;phane GALLAND
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
package org.arakhne.afc.ui.swing.zoompanel;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/** This enumeration contains all the methods to start
 * scrolling.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum ScrollingMethod {

	/** Scroll on middle mouse press.
	 */
	MIDDLE_BUTTON {
		@Override
		void tryScroll(MouseEvent me, ScrollingMethodListener listener) {
			if (SwingUtilities.isMiddleMouseButton(me)) {
				listener.startScolling(me, 0);
			}
		}
	},
	/** Scroll on left mouse press.
	 */
	LEFT_BUTTON {
		@Override
		void tryScroll(MouseEvent me, ScrollingMethodListener listener) {
			if (SwingUtilities.isLeftMouseButton(me)) {
				listener.startScolling(me, 500);
			}
		}
	};
	
	/** Start to scroll if possible.
	 * 
	 * @param me is the mouse event.
	 * @param listener is the listener on the scrolling action.
	 */
	abstract void tryScroll(MouseEvent me, ScrollingMethodListener listener);

	/** Listener on scrolling start.
	 *
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	interface ScrollingMethodListener {

		/** Start to scroll.
		 * 
		 * @param event
		 * @param delay is the delay (ms) to wait before starting to scroll.
		 */
		public void startScolling(MouseEvent event, int delay);
		
		/** Stop to scroll.
		 * 
		 * @param event
		 */
		public void stopScolling(MouseEvent event);

	}
	
}
