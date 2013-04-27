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
package org.arakhne.afc.progress;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream that is able to notify about the reading progression.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProgressionInputStream extends FilterInputStream {

	private final Progression monitor;
	private final int totalSize;
    private int read = 0;
	
	/**
	 * @param in is the input to read
	 * @param progression is the progression model used to notify on the reading progression.
	 */
	public ProgressionInputStream(InputStream in, Progression progression) {
		super(in);
		this.monitor = progression;
		int size;
        try {
            size = this.in.available();
        }
        catch(IOException _) {
            size = 0;
        }
        this.totalSize = (size>=0) ? size : 0;
        this.monitor.setProperties(0, 0, this.totalSize, false);
	}
	
	/**
	 * @param in is the input to read
	 * @param totalSize is the total number of bytes to read from the stream.
	 * @param progression is the progression model used to notify on the reading progression.
	 */
	public ProgressionInputStream(InputStream in, int totalSize, Progression progression) {
		super(in);
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
     * Overrides <code>FilterInputStream.read</code>
     * to update the progress monitor after the read.
     */
	@Override
    public int read() throws IOException {
        int c = this.in.read();
        if (c >= 0) this.monitor.setValue(++this.read);
        return c;
    }


    /**
     * Overrides <code>FilterInputStream.read</code>
     * to update the progress monitor after the read.
     */
	@Override
    public int read(byte b[]) throws IOException {
        int nr = this.in.read(b);
        if (nr > 0) this.monitor.setValue(this.read += nr);
        return nr;
    }


    /**
     * Overrides <code>FilterInputStream.read</code>
     * to update the progress monitor after the read.
     */
	@Override
    public int read(byte b[],
                    int off,
                    int len) throws IOException {
        int nr = this.in.read(b, off, len);
        if (nr > 0) this.monitor.setValue(this.read += nr);
        return nr;
    }


    /**
     * Overrides <code>FilterInputStream.skip</code>
     * to update the progress monitor after the skip.
     */
	@Override
    public long skip(long n) throws IOException {
        long nr = this.in.skip(n);
        if (nr > 0) this.monitor.setValue(this.read += nr);
        return nr;
    }


    /**
     * Overrides <code>FilterInputStream.close</code>
     * to close the progress monitor as well as the stream.
     */
	@Override
    public void close() throws IOException {
        this.in.close();
        this.monitor.end();
    }


    /**
     * Overrides <code>FilterInputStream.reset</code>
     * to reset the progress monitor as well as the stream.
     */
	@Override
    public synchronized void reset() throws IOException {
        this.in.reset();
        this.read = this.totalSize - this.in.available();
        this.monitor.setValue(this.read);
    }
	
}
