/* 
 * $Id$
 * 
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
package org.arakhne.afc.inputoutput.stream ;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/** An output stream that is ignoring all request to {@link #close()}.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UnclosableOutputStream extends FilterOutputStream {

	/**
	 * @param out1
	 * @throws IOException
	 */
	public UnclosableOutputStream(OutputStream out1) throws IOException {
		super(out1);
	}

	/**
	 * Do not close but still flush the filtered stream.
	 */
	@Override
	public void close() throws IOException {
		try {
			flush();
		}
		catch (IOException exception) {
			//
		}
	}

	/** Call directly the function {@link OutputStream#write(byte[], int, int)} on the filtered stream.
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		this.out.write(b, off, len);
	}

	/** Call directly the function {@link OutputStream#write(byte[])} on the filtered stream.
	 */
	@Override
	public void write(byte[] b) throws IOException {
		this.out.write(b);
	}

}
