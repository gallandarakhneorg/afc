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

package org.arakhne.afc.inputoutput.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/** An output stream that is writing inside a Writer.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WriterOutputStream extends OutputStream {

	private final Writer writer;

	/** Construct.
	 *
	 * @param writer writer.
	 */
	public WriterOutputStream(Writer writer) {
		this.writer = writer;
	}

	@Override
	public void write(int buffer) throws IOException {
		this.writer.write(buffer);
	}

	/**
	 * Write a sequence of characters.
	 *
	 * @param text text.
	 * @throws IOException on error.
	 */
	public void write(String text) throws IOException {
		this.writer.write(text);
	}

	/**
	 * Write a sequence of characters followed by a carriage return.
	 *
	 * @param text text.
	 * @throws IOException on error.
	 */
	public void writeln(String text) throws IOException {
		this.writer.write(text);
		if (text != null && text.length() > 0) {
			this.writer.write("\n"); //$NON-NLS-1$
		}
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

	@Override
	public void flush() throws IOException {
		this.writer.flush();
	}

}
