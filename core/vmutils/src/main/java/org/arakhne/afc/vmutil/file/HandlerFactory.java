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

package org.arakhne.afc.vmutil.file;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import org.arakhne.afc.vmutil.URISchemeType;

/**
 * This class defines a factory for <code>URL</code> stream
 * "resource" and "file" protocol handlers.
 *
 * <p>It is used by the <code>URL</code> class to create a
 * <code>URLStreamHandler</code> for a "resource" protocol.
 *
 * <p>To use this factory, invoke the following code only ONCE time:
 * <code>URL.setURLStreamHandlerFactory(new FileResourceURLStreamHandlerFactory());</code>.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.0
 * @see URLStreamHandlerFactory
 * @see URL#setURLStreamHandlerFactory(URLStreamHandlerFactory)
 * @deprecated since 17.0, see {@link HandlerProvider}.
 */
@Deprecated(forRemoval = true, since = "17.0")
public class HandlerFactory implements URLStreamHandlerFactory {

	@Override
	public URLStreamHandler createURLStreamHandler(String protocol) {
		if (URISchemeType.FILE.isScheme(protocol)) {
            return new Handler();
		}
		// Force the default factory to retreive stream handler.
		return null;
	}

}
