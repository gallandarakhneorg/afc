/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.arakhne.afc.vmutil.ResourceNotFoundException;
import org.arakhne.afc.vmutil.Resources;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * The class <code>URLConnection</code> is implementing
 * connection between an URL and a Java resource.
 * Instances of this class can be used to
 * read from the resource referenced by the resource URL. Write
 * is allowed depending on where resource is located.
 *
 * <p>Supported header fields are the same as the real resource URL
 * (basicaly, file or jar protocols).
 *
 * @author $Author: sgalland$
 * @author $Author: willaume$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.0
 * @see URLConnection
 */
public class URLConnection extends java.net.URLConnection {

	private URL location;

	private java.net.URLConnection connection;

	/** Constructor.
	 * @param url is the "file"-protocol url to use.
	 */
	protected URLConnection(URL url) {
		super(url);
	}

	@Override
    public String getHeaderField(int index) {
		assert index >= 0 : AssertMessages.positiveOrZeroParameter();
		try {
			connect();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
    	return this.connection.getHeaderField(index);
    }

	@Override
    public String getHeaderField(String name) {
		try {
			connect();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
    	return this.connection.getHeaderField(name);
    }

    @Override
    public String getHeaderFieldKey(int index) {
		assert index >= 0 : AssertMessages.positiveOrZeroParameter();
		try {
			connect();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
    	return this.connection.getHeaderFieldKey(index);
    }

	@Override
    public Map<String, List<String>> getHeaderFields() {
		try {
			connect();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
    	return this.connection.getHeaderFields();
    }

	@Override
	public void connect() throws IOException {
		if (!this.connected) {
			this.location = Resources.getResource(this.url.getFile());
			if (this.location == null) {
				throw new ResourceNotFoundException(this.url.toExternalForm());
			}
			this.connection = this.location.openConnection();
			if (this.connection == null) {
				throw new ResourceNotFoundException(this.url.toExternalForm());
			}
			this.connection.setDoInput(getDoInput());
			this.connection.setDoOutput(getDoOutput());
			this.connection.setAllowUserInteraction(getAllowUserInteraction());
			this.connection.setConnectTimeout(getConnectTimeout());
			this.connection.setDefaultUseCaches(getDefaultUseCaches());
			this.connection.setReadTimeout(getReadTimeout());
			this.connection.setIfModifiedSince(getIfModifiedSince());
			this.connected = true;
		}
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
        connect();
        return this.connection.getOutputStream();
    }

	@Override
	public InputStream getInputStream() throws IOException {
        connect();
        return this.connection.getInputStream();
    }

}
