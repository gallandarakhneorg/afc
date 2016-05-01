/* 
 * $Id$
 * 
 * Copyright (C) 2009 Stephane GALLAND This library is free software; you can redistribute it and/or
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

package org.arakhne.afc.references;

import java.util.EventListener;

/**
 * Listener on the release of a reference.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ReferenceListener extends EventListener {

	/** Invoked when at least one reference was released.
	 * <p>
	 * No reference is given to avoid strong referencing of the released objects
	 * inside the listener's source code.
	 * 
	 * @param released is the count of released objects.
	 */
	public void referenceReleased(int released);
	
}
