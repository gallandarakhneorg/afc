/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.vmutil.resource;

import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;

import org.arakhne.afc.vmutil.URISchemeType;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * The class <code>Handler</code> is supporting resource protocol
 * for URL streams. This stream protocol
 * handler knows how to make a connection for "resource" protocol.
 *
 * <p>In most cases, an instance of a <code>URLStreamHandler</code>
 * subclass is not created directly by an application. Rather, the
 * first time a protocol name is encountered when constructing a
 * <code>URL</code>, the appropriate stream protocol handler is
 * automatically loaded.
 *
 * <p>To use this factory, invoke the following code only ONCE time:
 * <code>URL.setURLStreamHandlerFactory(new HandlerProvider());</code>.
 *
 * @author $Author: sgalland$
 * @author $Author: willaume$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.0
 * @see URLStreamHandler
 * @see HandlerProvider
 */
public class Handler extends URLStreamHandler {

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		if (URISchemeType.RESOURCE.isURL(url)) {
			return new URLConnection(url);
		}
		throw new UnsupportedOperationException(Locale.getString("E1", url)); //$NON-NLS-1$
	}

}
