/* 
 * $Id$
 * 
 * Copyright (C) 2010 Alexandre WILLAUME, Stephane GALLAND.
 * Copyright (C) 2012-13 Stephane GALLAND.
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

import org.arakhne.vmutil.FileSystem;

/**
 * The class <code>URLConnection</code> is implementing
 * connection between an URL and a local file.
 * Instances of this class can be used both to
 * read from and to write to the resource referenced by the file URL.
 * <p>
 * Supported header fields are:
 * <ul>
 * <li><code>content-type</code></li>
 * <li><code>content-length</code></li>
 * <li><code>last-modified</code></li>
 * </ul>
 * 
 * @author $Author: galland$
 * @author $Author: willaume$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see java.net.URLConnection
 * @since 6.0
 */
class URLConnection extends java.net.URLConnection {

	private File file = null;
	
	private String contentType = null;
	
	/**
	 * @param url is the "file"-protocol url to use.
	 */
	protected URLConnection(URL url) {
		super(url);
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
    public String getHeaderField(int n) {
		try {
			connect();
		}
		catch(IOException e) {
			throw new IllegalStateException(e);
		}
    	switch(n) {
    	case 0: // content-type
    		return this.contentType;
    	case 1: // content-length
    		return Long.toString(this.file.length());
    	case 2: // last-modified
    		return Long.toString(this.file.lastModified());
    	default:
    	}
    	return null;
    }
	
    /**
     * {@inheritDoc}
     */
	@Override
    public String getHeaderField(String name) {
		try {
			connect();
		}
		catch(IOException e) {
			throw new IllegalStateException(e);
		}
    	if ("content-type".equals(name)) { //$NON-NLS-1$
    		return this.contentType;
    	}
    	if ("content-length".equals(name)) { //$NON-NLS-1$
    		return Long.toString(this.file.length());
    	}
    	if ("last-modified".equals(name)) { //$NON-NLS-1$
    		return Long.toString(this.file.lastModified());
    	}
    	return null;
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public String getHeaderFieldKey(int n) {
    	switch(n) {
    	case 0:
    		return "content-type"; //$NON-NLS-1$
    	case 1:
    		return "content-length"; //$NON-NLS-1$
    	case 2:
    		return "last-modified"; //$NON-NLS-1$
    	default:
    	}
    	return null;
    }
		
    /**
     * {@inheritDoc}
     */
	@Override
    public Map<String,List<String>> getHeaderFields() {
		try {
			connect();
		}
		catch(IOException e) {
			throw new IllegalStateException(e);
		}
		Map<String, List<String>> flds = new HashMap<String,List<String>>();
    	flds.put("content-type", singletonList(this.contentType)); //$NON-NLS-1$
    	flds.put("content-length", singletonList(Long.toString(this.file.length()))); //$NON-NLS-1$
    	flds.put("last-modified", singletonList(Long.toString(this.file.lastModified()))); //$NON-NLS-1$
        return flds;
    }
	
	private static List<String> singletonList(String value) {
		if (value==null) return null;
		return Collections.singletonList(value);
	}

    /**
	 * {@inheritDoc}
	 */
	@Override
	public void connect() throws IOException {
		if (!this.connected) {
			this.file = FileSystem.convertURLToFile(this.url);
			if (this.file==null)
				throw new FileNotFoundException(this.url.toExternalForm());
			this.contentType = new MimetypesFileTypeMap().getContentType(this.file);
			this.connected = true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
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
		throw new UnknownServiceException("URL connection cannot do output"); //$NON-NLS-1$
    }
	
	/**
	 * {@inheritDoc}
	 */
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
		throw new UnknownServiceException("URL connection cannot do input"); //$NON-NLS-1$
    }
	
}
