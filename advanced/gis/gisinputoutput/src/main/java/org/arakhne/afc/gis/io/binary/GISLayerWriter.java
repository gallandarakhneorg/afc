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

package org.arakhne.afc.gis.io.binary;

import static org.arakhne.afc.gis.io.binary.GISLayerIOConstants.HEADER_KEY;
import static org.arakhne.afc.gis.io.binary.GISLayerIOConstants.MAJOR_SPEC_NUMBER;
import static org.arakhne.afc.gis.io.binary.GISLayerIOConstants.MINOR_SPEC_NUMBER;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.progress.Progression;

/** Writer of GIS elements in a Java serialized form.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GISLayerWriter implements AutoCloseable {

	private final OutputStream output;

	private final OutputStream tmpOutput;

	private final File tmpFile;

	private int length;

	private Progression progression;

	private boolean isHeaderWritten;

	/**
	 * Constructs a GIS writer.
	 *
	 * @param filename is the file to write
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	public GISLayerWriter(File filename) throws IOException {
		this(new FileOutputStream(filename));
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * @param url is the URL of the file to write
	 * @throws IOException in case of error.
	 */
	public GISLayerWriter(URL url) throws IOException {
		this(url.openConnection().getOutputStream());
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * @param channel is the file to write
	 * @throws IOException in case of error.
	 */
	public GISLayerWriter(WritableByteChannel channel) throws IOException {
		this(Channels.newOutputStream(channel));
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * @param stream is is the stream to write
	 * @throws IOException in case of error.
	 */
	public GISLayerWriter(OutputStream stream) throws IOException {
		assert stream != null;
		this.output = stream;
		this.tmpFile = File.createTempFile("gisLayerWriter", ".gi_");  //$NON-NLS-1$//$NON-NLS-2$
		this.tmpFile.deleteOnExit();
		this.tmpOutput = new FileOutputStream(this.tmpFile);
	}

	/** Replies the task progression.
	 *
	 * @return the task progression object associated to this writer, or <code>null</code>
	 */
	@Pure
	public Progression getProgression() {
		return this.progression;
	}

	/** Set the task progression associated to this writer.
	 *
	 * @param progressBar is the task progression object associated to this writer, or <code>null</code>
	 */
	public void setProgression(Progression progressBar) {
		this.progression = progressBar;
	}

	/** Flush temp buffers, write down final information in
	 * file header (file size...), and close the streams.
	 *
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	@Override
	public void close() throws IOException {
		if (this.tmpFile.canRead()) {
			try {
				this.tmpOutput.close();

				try (WritableByteChannel out = Channels.newChannel(this.output)) {
					try (ReadableByteChannel in = Channels.newChannel(new FileInputStream(this.tmpFile))) {
						// Write the header
						final int limit = HEADER_KEY.getBytes().length + 6;
						final ByteBuffer hBuffer = ByteBuffer.allocate(limit);
						hBuffer.limit(limit);
						in.read(hBuffer);
						hBuffer.position(HEADER_KEY.getBytes().length + 2);
						hBuffer.putInt(this.length);
						hBuffer.rewind();
						out.write(hBuffer);
						// Write the objects
						final ByteBuffer buffer = ByteBuffer.allocate(4096);
						int read;
						while ((read = in.read(buffer)) >= 0) {
							buffer.rewind();
							buffer.limit(read);
							out.write(buffer);
							buffer.rewind();
							buffer.limit(buffer.capacity());
						}
					}
				}
			} finally {
				this.tmpFile.delete();
			}
		}
	}

	/** Write the given layer into the output.
	 *
	 * @param layer is the layer to write down.
	 * @throws IOException in case of error.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final void write(MapLayer layer) throws IOException {
		write(Collections.singleton(layer));
	}

	/** Write the given layers into the output.
	 *
	 * @param layers are the layers to write.
	 * @throws IOException in case of error.
	 */
	public void write(Collection<? extends MapLayer> layers) throws IOException {
		if (this.progression != null) {
			this.progression.setProperties(0, 0, layers.size() + 1, false);
		}

		// Write the header
		if (!this.isHeaderWritten) {
			this.isHeaderWritten = true;
			writeHeader();
		}

		if (this.progression != null) {
			this.progression.increment();
		}

		final ObjectOutputStream oos = new ObjectOutputStream(this.tmpOutput);
		for (final MapLayer layer : layers) {
			oos.writeObject(layer);
			++this.length;
			if (this.progression != null) {
				this.progression.increment();
			}
		}
		if (this.progression != null) {
			this.progression.end();
		}
	}

	/** Write the header of the file.
	 *
	 * @throws IOException in case of error.
	 */
	protected void writeHeader() throws IOException {
		this.tmpOutput.write(HEADER_KEY.getBytes());
		this.tmpOutput.write(new byte[]{MAJOR_SPEC_NUMBER, MINOR_SPEC_NUMBER});
		this.tmpOutput.write(new byte[] {0, 0, 0, 0});
	}

}
