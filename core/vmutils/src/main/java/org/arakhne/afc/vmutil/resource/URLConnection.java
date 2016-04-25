/* 
 * $Id$
 * 
 * Copyright (C) 2010 Alexandre WILLAUME, Stephane GALLAND.
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
package org.arakhne.afc.vmutil.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.arakhne.afc.vmutil.ResourceNotFoundException;
import org.arakhne.afc.vmutil.Resources;

/**
 * The class <code>URLConnection</code> is implementing
 * connection between an URL and a Java resource.
 * Instances of this class can be used to
 * read from the resource referenced by the resource URL. Write
 * is allowed depending on where resource is located.
 * <p>
 * Supported header fields are the same as the real resource URL
 * (basicaly, file or jar protocols).
 * 
 * @author $Author: sgalland$
 * @author $Author: willaume$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see URLConnection
 * @since 6.0
 */
class URLConnection extends java.net.URLConnection {

	private URL location = null;
	private java.net.URLConnection connection = null;
	
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
    	return this.connection.getHeaderField(n);
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
    	return this.connection.getHeaderField(name);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public String getHeaderFieldKey(int n) {
		try {
			connect();
		}
		catch(IOException e) {
			throw new IllegalStateException(e);
		}
    	return this.connection.getHeaderFieldKey(n);
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
    	return this.connection.getHeaderFields();
    }
	
    /**
	 * {@inheritDoc}
	 */
	@Override
	public void connect() throws IOException {
		if (!this.connected) {
			this.location = Resources.getResource(this.url.getFile());
			if (this.location==null)
				throw new ResourceNotFoundException(this.url.toExternalForm());
			this.connection = this.location.openConnection();
			if (this.connection==null)
				throw new ResourceNotFoundException(this.url.toExternalForm());
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
        connect();
        return this.connection.getOutputStream();
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream() throws IOException {
        connect();
        return this.connection.getInputStream();
    }
	
}
