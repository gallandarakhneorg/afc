/* 
 * $Id$
 * 
 * Copyright (C) 2005-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
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

package org.arakhne.afc.ui;

/** Express the different level of details supported
 * by a zoomable panel.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum Graphics2DLOD {

	/** The graphical elements must be drawn as they are shadows.
	 */
	SHADOW,

	/** The graphical elements must be drawn with a low level of detail.
	 */
	LOW_LEVEL_OF_DETAIL,
	
	/** The graphical elements must be drawn with a standard level of detail.
	 */
	NORMAL_LEVEL_OF_DETAIL,
	
	/** The graphical elements must be drawn with a high level of detail.
	 */
	HIGH_LEVEL_OF_DETAIL;
	
}