/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.progress;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream that is able to notify about the reading progression.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProgressionInputStream extends FilterInputStream {

	private final Progression monitor;

	private final int totalSize;

	private int read;

	/** Constructor.
	 * @param in is the input to read
	 * @param progression is the progression model used to notify on the reading progression.
	 */
	public ProgressionInputStream(InputStream in, Progression progression) {
		super(in);
		this.monitor = progression;
		int size;
		try {
			size = this.in.available();
		} catch (IOException exception) {
			size = 0;
		}
		this.totalSize = (size >= 0) ? size : 0;
		this.monitor.setProperties(0, 0, this.totalSize, false);
	}

	/** Constructor.
	 * @param in is the input to read
	 * @param totalSize is the total number of bytes to read from the stream.
	 * @param progression is the progression model used to notify on the reading progression.
	 */
	public ProgressionInputStream(InputStream in, int totalSize, Progression progression) {
		super(in);
		this.monitor = progression;
		this.totalSize = (totalSize >= 0) ? totalSize : 0;
		this.monitor.setProperties(0, 0, this.totalSize, false);
	}

	/** Replies the progression model used by this input stream.
	 *
	 * @return the progression model.
	 */
	public Progression getProgression() {
		return this.monitor;
	}

	@Override
	public int read() throws IOException {
		final int c = this.in.read();
		if (c >= 0) {
			this.monitor.setValue(++this.read);
		}
		return c;
	}

	@Override
	public int read(byte[] buffer) throws IOException {
		final int nr = this.in.read(buffer);
		if (nr > 0) {
			this.read += nr;
			this.monitor.setValue(this.read);
		}
		return nr;
	}

	@Override
	public int read(byte[] buffer,
			int off,
			int len) throws IOException {
		final int nr = this.in.read(buffer, off, len);
		if (nr > 0) {
			this.read += nr;
			this.monitor.setValue(this.read);
		}
		return nr;
	}

	@Override
	public long skip(long number) throws IOException {
		final long nr = this.in.skip(number);
		if (nr > 0) {
			this.read += nr;
			this.monitor.setValue(this.read);
		}
		return nr;
	}

	@Override
	public void close() throws IOException {
		this.in.close();
		this.monitor.end();
	}

	@Override
	public synchronized void reset() throws IOException {
		this.in.reset();
		this.read = this.totalSize - this.in.available();
		this.monitor.setValue(this.read);
	}

}
