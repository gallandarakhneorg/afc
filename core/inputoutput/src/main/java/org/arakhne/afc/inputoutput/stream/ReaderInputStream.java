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

package org.arakhne.afc.inputoutput.stream;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.xtext.xbase.lib.Pure;

/** An output stream that is writing inside a Writer.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ReaderInputStream extends InputStream {

	private final Reader reader;

	/** Construct.
	 * @param reader the reader.
	 */
	public ReaderInputStream(Reader reader) {
		this.reader = reader;
	}

	@Override
	public void close() throws IOException {
		this.reader.close();
	}

	@Override
	public int read() throws IOException {
		return this.reader.read();
	}

	@Pure
	@Override
	public boolean markSupported() {
		return this.reader.markSupported();
	}

	@Override
	public synchronized void mark(int readlimit) {
		try {
			this.reader.mark(readlimit);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	@Override
	public synchronized void reset() throws IOException {
		this.reader.reset();
	}

	@Override
	public long skip(long nb) throws IOException {
		return this.reader.skip(nb);
	}

}
