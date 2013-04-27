/* 
 * $Id$
 * 
 * Copyright (C) 2010 Alexandre WILLAUME, Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;

import org.arakhne.vmutil.URISchemeType;

/**
 * The class <code>Handler</code> is supporting file protocol
 * for URL streams. This stream protocol
 * handler knows how to make a connection for "file" protocol.
 * <p>
 * In most cases, an instance of a <code>URLStreamHandler</code>
 * subclass is not created directly by an application. Rather, the
 * first time a protocol name is encountered when constructing a
 * <code>URL</code>, the appropriate stream protocol handler is
 * automatically loaded.
 * <p>
 * To use this factory, invoke the following code only ONCE time:
 * <code>URL.setURLStreamHandlerFactory(new FileURLStreamHandlerFactory());</code>.
 * 
 * @author $Author: galland$
 * @author $Author: willaume$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see URLStreamHandler
 * @see HandlerFactory
 * @since 6.0
 */
public class Handler extends URLStreamHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		if (URISchemeType.FILE.isURL(url)) {
			return new URLConnection(url);
		}
		throw new UnsupportedOperationException("Unsupported protocol: "+url); //$NON-NLS-1$
	}

}
