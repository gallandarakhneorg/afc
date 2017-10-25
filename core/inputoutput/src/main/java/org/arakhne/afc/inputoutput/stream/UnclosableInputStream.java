/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/** An input stream that is ignoring all request to {@link #close()}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UnclosableInputStream extends FilterInputStream {

	/** Construct.
	 * @param in input stream.
	 * @throws IOException on error.
	 */
	public UnclosableInputStream(InputStream in) throws IOException {
		super(in);
	}

	@Override
	public void close() throws IOException {
		//
	}

	/** Call directly the function {@link InputStream#read(byte[], int, int)} on
	 * the filtered stream.
	 */
	@Override
	public int read(byte[] buffer, int offset, int len) throws IOException {
		return this.in.read(buffer, offset, len);
	}

	/** Call directly the function {@link InputStream#read(byte[])} on
	 * the filtered stream.
	 */
	@Override
	public int read(byte[] buffer) throws IOException {
		return this.in.read(buffer);
	}

}
