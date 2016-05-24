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
