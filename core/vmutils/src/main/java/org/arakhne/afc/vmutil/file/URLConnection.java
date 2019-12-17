/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * The class <code>URLConnection</code> is implementing
 * connection between an URL and a local file.
 * Instances of this class can be used both to
 * read from and to write to the resource referenced by the file URL.
 *
 * <p>Supported header fields are:
 * <ul>
 * <li><code>content-type</code></li>
 * <li><code>content-length</code></li>
 * <li><code>last-modified</code></li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @author $Author: willaume$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.0
 * @see java.net.URLConnection
 */
class URLConnection extends java.net.URLConnection {

	private static final String CONTENT_TYPE = "content-type"; //$NON-NLS-1$

	private static final String CONTENT_LENGTH = "content-length"; //$NON-NLS-1$

	private static final String LAST_MODIFIED = "last-modified"; //$NON-NLS-1$

	private File file;

	private String contentType;

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
    	switch (index) {
    	case 0:
    		// content-type
    		return this.contentType;
    	case 1:
    		// content-length
    		return Long.toString(this.file.length());
    	case 2:
    		// last-modified
    		return Long.toString(this.file.lastModified());
    	default:
    	}
    	return null;
    }

	@Override
    public String getHeaderField(String name) {
		try {
			connect();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
    	if (CONTENT_TYPE.equals(name)) {
    		return this.contentType;
    	}
    	if (CONTENT_LENGTH.equals(name)) {
    		return Long.toString(this.file.length());
    	}
    	if (LAST_MODIFIED.equals(name)) {
    		return Long.toString(this.file.lastModified());
    	}
    	return null;
    }

	@Override
    public String getHeaderFieldKey(int index) {
		assert index >= 0 : AssertMessages.positiveOrZeroParameter();
    	switch (index) {
    	case 0:
    		return CONTENT_TYPE;
    	case 1:
    		return CONTENT_LENGTH;
    	case 2:
    		return LAST_MODIFIED;
    	default:
    	}
    	return null;
    }

	@Override
    public Map<String, List<String>> getHeaderFields() {
		try {
			connect();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		final Map<String, List<String>> flds = new HashMap<>();
    	flds.put(CONTENT_TYPE, singletonList(this.contentType));
    	flds.put(CONTENT_LENGTH, singletonList(Long.toString(this.file.length())));
    	flds.put(LAST_MODIFIED, singletonList(Long.toString(this.file.lastModified())));
        return flds;
    }

	private static List<String> singletonList(String value) {
		if (value == null) {
			return Collections.emptyList();
		}
		return Collections.singletonList(value);
	}

	@Override
	public void connect() throws IOException {
		if (!this.connected) {
			this.file = FileSystem.convertURLToFile(this.url);
			if (this.file == null) {
				throw new FileNotFoundException(this.url.toExternalForm());
			}
			this.contentType = new MimetypesFileTypeMap().getContentType(this.file);
			this.connected = true;
		}
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
        connect();
		if (getDoOutput()) {
			OutputStream os = new FileOutputStream(this.file);
			if (getUseCaches()) {
				os = new BufferedOutputStream(os);
			}
			return os;
		}
		throw new UnknownServiceException(Locale.getString("E1")); //$NON-NLS-1$
    }

	@Override
	public InputStream getInputStream() throws IOException {
        connect();
		if (getDoInput()) {
			InputStream is = new FileInputStream(this.file);
			if (getUseCaches()) {
				is = new BufferedInputStream(is);
			}
			return is;
		}
		throw new UnknownServiceException("E2"); //$NON-NLS-1$
    }

}
