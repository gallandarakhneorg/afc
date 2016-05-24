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

package org.arakhne.afc.ui;

/** Mouse cursors. 
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public enum MouseCursor {

	/** Default mouse cursor.
	 */
	DEFAULT,
	
    /**
     * The crosshair cursor.
     */
    CROSSHAIR,

    /**
     * The text cursor.
     */
    TEXT,

    /**
     * The wait cursor.
     */
    WAIT,

    /**
     * The south-west-resize cursor.
     */
    SW_RESIZE,

    /**
     * The south-east-resize cursor.
     */
    SE_RESIZE,

    /**
     * The north-west-resize cursor.
     */
    NW_RESIZE,

    /**
     * The north-east-resize cursor.
     */
    NE_RESIZE,

    /**
     * The north-resize cursor.
     */
    N_RESIZE,

    /**
     * The south-resize cursor.
     */
    S_RESIZE,

    /**
     * The west-resize cursor.
     */
    W_RESIZE,

    /**
     * The east-resize cursor.
     */
    E_RESIZE,

    /**
     * The hand cursor.
     */
    HAND,

    /**
     * The move cursor.
     */
    MOVE,

    /** Invalid.
	 */
	INVALID;
}
