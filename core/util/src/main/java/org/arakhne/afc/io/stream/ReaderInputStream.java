/* 
 * $Id$
 * 
 * Copyright (C) 2002 Stephane GALLAND, Madhi HANNOUN, Marc BAUMGARTNER.
 * Copyright (C) 2012 Stephane GALLAND.
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

package org.arakhne.afc.io.stream ;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/** An output stream that is writing inside a Writer.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ReaderInputStream extends InputStream {

	private final Reader reader;

	/**
	 * @param reader
	 */
	public ReaderInputStream(Reader reader) {
		this.reader = reader;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		this.reader.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		return this.reader.read();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean markSupported() {
		return this.reader.markSupported();
	}
	
	@Override
	public synchronized void mark(int readlimit) {
		try {
			this.reader.mark(readlimit);
		}
		catch(IOException e) {
			throw new IOError(e); 
		}
	}
	
	@Override
	public synchronized void reset() throws IOException {
		this.reader.reset();
	}
	
	@Override
	public long skip(long n) throws IOException {
		return this.reader.skip(n);
	}
	
}