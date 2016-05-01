/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team, Laboratoire Systemes et Transports,
 *                     Universite de Technologie de Belfort-Montbeliard.
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
    private int write = 0;
	
	/**
	 * @param out is the output to write
	 * @param totalSize is the total number of bytes to write in the stream.
	 * @param progression is the progression model used to notify on the reading progression.
	 */
	public ProgressionOuputStream(OutputStream out, int totalSize, Progression progression) {
		super(out);
		this.monitor = progression;
        this.totalSize = (totalSize>=0) ? totalSize : 0;
        this.monitor.setProperties(0, 0, this.totalSize, false);
	}

	/** Replies the progression model used by this input stream.
	 * 
	 * @return the progression model.
	 */
	public Progression getProgression() {
		return this.monitor;
	}
		
    /**
     * {@inheritDoc}
     */
	@Override
    public void write(int b) throws IOException {
        this.out.write(b);
        this.monitor.setValue(++this.write);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void write(byte b[]) throws IOException {
        this.out.write(b);
        this.monitor.setValue(this.write += b.length);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void write(byte b[], int off, int len) throws IOException {
        this.out.write(b, off, len);
        this.monitor.setValue(this.write += len);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void close() throws IOException {
        super.close();
        this.monitor.end();
    }

}
