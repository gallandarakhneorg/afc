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
package org.arakhne.afc.util;

import java.util.Map;

/** This interface defines the services for an objects
 * that is owning properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PropertyOwner {

	/** Replies the properties of this component.
	 * 
	 * @return the properties, never <code>null</code>.
	 * @see #setProperties(Map)
	 */
	public Map<String,Object> getProperties();

	/** Set the properties of the model object, except the UUID.
	 * 
	 * @param properties are the properties of this model object, except the UUID.
	 * @see #getProperties()
	 */
	public void setProperties(Map<String,Object> properties);

}
