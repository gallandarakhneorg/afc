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

package org.arakhne.afc.ui.swing.zoom;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/** This enumeration contains all the methods to start
 * scrolling.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public enum ScrollingMethod {

	/** Scroll on middle mouse press.
	 */
	MIDDLE_BUTTON {
		@Override
		void tryScroll(MouseEvent me, ScrollingMethodListener listener) {
			if (SwingUtilities.isMiddleMouseButton(me)) {
				listener.startScrolling(me, 0);
			}
		}
	},
	/** Scroll on left mouse press.
	 */
	LEFT_BUTTON {
		@Override
		void tryScroll(MouseEvent me, ScrollingMethodListener listener) {
			if (SwingUtilities.isLeftMouseButton(me)) {
				listener.startScrolling(me, 500);
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
	 * @author $Author: sgalland$
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
		public void startScrolling(MouseEvent event, int delay);
		
		/** Stop to scroll.
		 * 
		 * @param event
		 */
		public void stopScrolling(MouseEvent event);

	}
	
}
