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

package org.arakhne.afc.inputoutput.stream ;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/** An output stream that is writing inside a Writer.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WriterOutputStream extends OutputStream {

	private final Writer writer;

	/**
	 * @param writer1
	 */
	public WriterOutputStream(Writer writer1) {
		this.writer = writer1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(int b) throws IOException {
		this.writer.write(b);
	}
	
	/**
	 * Write a sequence of characters.
	 * 
	 * @param text
	 * @throws IOException
	 */
	public void write(String text) throws IOException {
		this.writer.write(text);
	}

	/**
	 * Write a sequence of characters followed by a carriage return.
	 * 
	 * @param text
	 * @throws IOException
	 */
	public void writeln(String text) throws IOException {
		this.writer.write(text);
		if (text!=null && text.length()>0)
			this.writer.write("\n"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		this.writer.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() throws IOException {
		this.writer.flush();
	}
	
}