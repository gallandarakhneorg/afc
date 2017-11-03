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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.progress.Progression;

/** Reader of GIS elements in a Java serialized form.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class GISLayerReader implements AutoCloseable, Iterable<MapLayer> {

	private final InputStream input;

	private Progression progression;

	private boolean isHeaderRead;

	private int restToRead;

	/**
	 * Constructs a GIS reader.
	 *
	 * @param filename is the file to read
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	public GISLayerReader(File filename) throws IOException {
		this(new FileInputStream(filename));
	}

	/**
	 * Constructs a GIS reader.
	 *
	 * @param url is the URL of the file to read
	 * @throws IOException in case of error.
	 */
	public GISLayerReader(URL url) throws IOException {
		this(url.openStream());
	}

	/**
	 * Constructs a GIS writer.
	 *
	 * @param channel is the file to write
	 * @throws IOException in case of error.
	 */
	public GISLayerReader(ReadableByteChannel channel) throws IOException {
		this(Channels.newInputStream(channel));
	}

	/**
	 * Constructs a GIS reader.
	 *
	 * @param stream is is the stream to read
	 * @throws IOException in case of error.
	 */
	public GISLayerReader(InputStream stream) throws IOException {
		assert stream != null;
		this.input = stream;
	}

	/** Replies the task progression.
	 *
	 * @return the task progression object associated to this reader, or <code>null</code>
	 */
	@Pure
	public Progression getProgression() {
		return this.progression;
	}

	/** Set the task progression associated to this reader.
	 *
	 * @param progressBar is the task progression object associated to this reader, or <code>null</code>
	 */
	public void setProgression(Progression progressBar) {
		this.progression = progressBar;
	}

	/** Flush temp buffers, and close the streams.
	 *
	 * @throws IOException in case of error.
	 */
	@Override
	public void close() throws IOException {
		this.input.close();
	}

	@Override
	public Iterator<MapLayer> iterator() {
		return iterator(MapLayer.class);
	}

	/** Replies an iterator on the layers of the given type and read from the input stream.
	 *
	 * @param <T> the type of the layers to reply.
	 * @param type is the type of the layers to reply.
	 * @return the iterator.
	 */
	public <T extends MapLayer> Iterator<T> iterator(Class<T> type) {
		return new LayerReaderIterator<>(type);
	}

	/** Read the next layer from the input.
	 *
	 * @return the next layer in the input stream.
	 * @throws IOException in case of error.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final MapLayer read() throws IOException {
		return read(MapLayer.class);
	}

	/** Read the next layer of the given type from the input.
	 *
	 * @param <T> is the type of the expected layer.
	 * @param type is the type of the expected layer.
	 * @return the next layer in the input stream, or <code>null</code>
	 *     if there is no more object of the given type.
	 * @throws IOException in case of error.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final <T extends MapLayer> T read(Class<T> type) throws IOException {
		// Read the header
		if (!this.isHeaderRead) {
			this.isHeaderRead = true;
			readHeader();
			if (this.progression != null) {
				this.progression.setProperties(0, 0, this.restToRead, false);
			}
		}

		T selectedObject = null;

		if (this.restToRead > 0) {
			final ObjectInputStream oos = new ObjectInputStream(this.input);
			do {
				try {
					final Object readObject = oos.readObject();
					--this.restToRead;
					if (type.isInstance(readObject)) {
						selectedObject = type.cast(readObject);
					}
				} catch (ClassNotFoundException e) {
					//
				}
			}
			while (this.restToRead > 0 && selectedObject == null);
		}

		if (this.progression != null) {
			if (this.restToRead <= 0) {
				this.progression.end();
			} else {
				this.progression.increment();
			}
		}

		return selectedObject;
	}

	/** Read the header of the file.
	 *
	 * @throws IOException in case of error.
	 */
	protected void readHeader() throws IOException {
		final byte[] key = GISLayerIOConstants.HEADER_KEY.getBytes();
		final byte[] buffer = new byte[key.length + 2];
		final int n = this.input.read(buffer);
		if (n != buffer.length) {
			throw new IOException("Invalid file header"); //$NON-NLS-1$
		}
		for (int i = 0; i < key.length; ++i) {
			if (buffer[i] != key[i]) {
				throw new IOException("Invalid file header"); //$NON-NLS-1$
			}
		}
		final byte major = buffer[buffer.length - 2];
		final byte minor = buffer[buffer.length - 1];
		if (major != 1 || minor != 0) {
			throw new IOException("Invalid file format version."); //$NON-NLS-1$
		}
		this.restToRead = this.input.read();
	}

	/** Internal iterator.
	 * @param <T> is the type of the expected layers.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 5.0
	 */
	private class LayerReaderIterator<T extends MapLayer> implements Iterator<T> {

		private final Class<T> type;

		private T next;

		/** Constructor.
		 * @param type is the expected type of the layers.
		 */
		LayerReaderIterator(Class<T> type) {
			this.type = type;
			searchNext();
		}

		private void searchNext() {
			try {
				this.next = read(this.type);
			} catch (IOException exception) {
				this.next = null;
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public T next() {
			final T n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
