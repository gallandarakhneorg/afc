/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.ui.event ;

import java.io.Serializable;



/** Describe the event related to an input device.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public interface InputEvent extends Serializable {

	/** Replies the identifier of the device.
	 * 
	 * @return the identifier of the device.
	 */
	public int getDeviceId();
	
	/** Replies if this event was consumed.
	 * 
	 * @return <code>true</code> if the event was consumed.
	 */
	public boolean isConsumed();
	
	/** Consume the event.
	 */
	public void consume();
	
    /**
     * Returns whether or not the Shift modifier is down on this event.
     * 
     * @return <code>true</code> if the Shift key is down.
     */
    public boolean isShiftDown();

    /**
     * Returns whether or not the Control modifier is down on this event.
     * @return <code>true</code> if the Control key is down.
     */
    public boolean isControlDown();

    /**
     * Returns whether or not the Meta modifier is down on this event.
     * @return <code>true</code> if the Meta key is down.
     */
    public boolean isMetaDown();

    /**
     * Returns whether or not the Alt modifier is down on this event.
     * @return <code>true</code> if the Alt key is down.
     */
    public boolean isAltDown();

    /**
     * Returns whether or not the AltGraph modifier is down on this event.
     * @return <code>true</code> if the AltGr key is down.
     */
    public boolean isAltGraphDown();
    
    /** Replies if the contextual action is triggered by this event.
     * A contextual action may be a request to display a popup menu for
     * example.
     * @return <code>true</code> if the contextual action may be triggered.
     */
    public boolean isContextualActionTriggered();

}
