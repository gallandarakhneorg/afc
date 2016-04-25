/* 
 * $Id$
 * 
 * Copyright (C) 2005-2010, 2013 Stephane GALLAND.
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
package org.arakhne.afc.vmutil;

import java.net.URL;

/**
 * Indicates that the object implementing this interface
 * owns a external resource.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.2
 */
public interface ExternalizableResource {

	/** Replies the URL where the resource data could be find.
	 * 
	 * @return the URL to the external resource.
	 */
	public URL getExternalizableResourceLocation();

	/** Replies the MIME type of the external resource.
	 * 
	 * @return the MIME of the external resource.
	 */
	public String getExternalizableResourceType();

}