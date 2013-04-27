/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.io.stream ;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/** An input stream that is ignoring all request to {@link #close()}.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UnclosableInputStream extends FilterInputStream {

	/**
	 * @param in
	 * @throws IOException
	 */
	public UnclosableInputStream(InputStream in) throws IOException {
		super(in);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		//
	}

	/** Call directly the function {@link InputStream#read(byte[], int, int)} on
	 * the filtered stream.
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return this.in.read(b, off, len);
	}
	
	/** Call directly the function {@link InputStream#read(byte[])} on
	 * the filtered stream.
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return this.in.read(b);
	}
	
}
