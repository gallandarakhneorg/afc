/* 
 * $Id$
 * 
 * Copyright (C) 2010-13 Stephane GALLAND.
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
package org.arakhne.vmutil.file;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import org.arakhne.vmutil.URISchemeType;

/**
 * This class defines a factory for <code>URL</code> stream
 * "resource" and "file" protocol handlers.
 * <p>
 * It is used by the <code>URL</code> class to create a
 * <code>URLStreamHandler</code> for a "resource" protocol.
 * <p>
 * To use this factory, invoke the following code only ONCE time:
 * <code>URL.setURLStreamHandlerFactory(new FileResourceURLStreamHandlerFactory());</code>.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see URLStreamHandlerFactory
 * @see URL#setURLStreamHandlerFactory(URLStreamHandlerFactory)
 * @since 6.0
 */
public class HandlerFactory
implements URLStreamHandlerFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URLStreamHandler createURLStreamHandler(String protocol) {
		if (URISchemeType.FILE.isScheme(protocol))
            return new Handler();
		// Force the default factory to retreive stream handler.
		return null;
	}

}
