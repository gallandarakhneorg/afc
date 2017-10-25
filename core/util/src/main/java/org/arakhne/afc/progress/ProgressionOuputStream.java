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

package org.arakhne.afc.progress;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * OutputStream that is able to notify about the writing progression.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProgressionOuputStream extends FilterOutputStream {

	private final Progression monitor;

	private final int totalSize;

	private int write;

	/** Construct a progression output stream.
	 *
	 * @param out is the output to write
	 * @param totalSize is the total number of bytes to write in the stream.
	 * @param progression is the progression model used to notify on the reading progression.
	 */
	public ProgressionOuputStream(OutputStream out, int totalSize, Progression progression) {
		super(out);
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
    public void write(int abyte) throws IOException {
        this.out.write(abyte);
        this.monitor.setValue(++this.write);
    }

	@Override
    public void write(byte[] buffer) throws IOException {
        this.out.write(buffer);
        this.write += buffer.length;
        this.monitor.setValue(this.write);
    }

	@Override
    public void write(byte[] buffer, int off, int len) throws IOException {
        this.out.write(buffer, off, len);
        this.write += len;
        this.monitor.setValue(this.write);
    }

	@Override
    public void close() throws IOException {
        super.close();
        this.monitor.end();
    }

}
