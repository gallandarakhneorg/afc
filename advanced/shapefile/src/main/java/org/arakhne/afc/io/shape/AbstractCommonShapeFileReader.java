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

package org.arakhne.afc.io.shape;

import static org.arakhne.afc.io.shape.ESRIFileUtil.HEADER_BYTES;
import static org.arakhne.afc.io.shape.ESRIFileUtil.SHAPE_FILE_CODE;
import static org.arakhne.afc.io.shape.ESRIFileUtil.SHAPE_FILE_VERSION;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRIWords;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRI_m;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRI_x;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRI_y;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRI_z;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.common.collect.Iterators;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.progress.Progression;

/**
 * This class is an abstract reader for all the ESRI Shape file formats.
 *
 * <p>To have a lower memory foot-print, call {@link #disableSeek()}. Indeed,
 * the seek feature forces this reader to maintain a buffer of all the file content.
 *
 * <p>The specification of the ESRI Shape file format is described in
 * <a href="./doc-files/esri_specs_0798.pdf">the July 98 specification document</a>.
 *
 * @param <E> is the type of element in the shape file.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class AbstractCommonShapeFileReader<E> implements Iterable<E>, AutoCloseable {

	/** Size of a block of data in the buffer, when the seek feature was disable.
	 */
	protected static final int BLOCK_SIZE = 512;

	/** Size in bytes of the input stream.
	 */
	protected int fileSize;

	/** Bounding box in the file header.
	 */
	protected double minx;

	/** Bounding box in the file header.
	 */
	protected double miny;

	/** Bounding box in the file header.
	 */
	protected double minz;

	/** Bounding box in the file header.
	 */
	protected double minm;

	/** Bounding box in the file header.
	 */
	protected double maxx;

	/** Bounding box in the file header.
	 */
	protected double maxy;

	/** Bounding box in the file header.
	 */
	protected double maxz;

	/** Bounding box in the file header.
	 */
	protected double maxm;

	/** Is the type of element stored inside the shape file.
	 */
	protected ShapeElementType expectedShapeType;

	/** Position of the first byte in the buffer.
	 */
	private int bufferPosition;

	/** Indicates if seek is enable.
	 */
	private boolean seekEnabled = true;

	/** This is the stream to read from.
	 */
	private final ReadableByteChannel stream;

	/** Reading buffer.
	 */
	private ByteBuffer buffer;

	/** Indicates if the header was already read.
	 */
	private boolean headerWasread;

	/** Associated task progression object.
	 */
	private Progression taskProgression;

	/** Index of the next record to read.
	 * If <code>-1</code>, the index is unknown.
	 */
	private int nextExpectedRecordIndex = -1;

	/** Constructor.
	 * @param inputStream is the stream to read
	 */
	public AbstractCommonShapeFileReader(ReadableByteChannel inputStream) {
		assert inputStream != null;
		this.stream = inputStream;
		this.buffer = null;
	}

	/** Constructor.
	 * @param inputStream is the stream to read
	 */
	@SuppressWarnings("resource")
	public AbstractCommonShapeFileReader(InputStream inputStream) {
		this(Channels.newChannel(inputStream));
	}

	/** Constructor.
	 * @param file is the file to read
	 * @throws IOException if cannot read.
	 */
	@SuppressWarnings("resource")
	public AbstractCommonShapeFileReader(File file) throws IOException {
		this(Channels.newChannel(new FileInputStream(file)));
	}

	/** Constructor.
	 * @param file is the file to read
	 * @throws IOException if cannot read.
	 */
	@SuppressWarnings("resource")
	public AbstractCommonShapeFileReader(URL file) throws IOException {
		this(Channels.newChannel(file.openStream()));
	}

	/** Replies if the header was read.
	 *
	 * @return <code>true</code> if the header was read, otherwise <code>false</code>
	 */
	@Pure
	protected boolean isHeaderRead() {
		return this.headerWasread;
	}

	/** Replies the task progression.
	 *
	 * @return the task progression object associated to this writer, or <code>null</code>
	 */
	@Pure
	public Progression getTaskProgression() {
		return this.taskProgression;
	}

	/** Set the task progression associated to this writer.
	 *
	 * @param progressBar is the task progression object associated to this writer, or <code>null</code>
	 */
	public void setTaskProgression(Progression progressBar) {
		this.taskProgression = progressBar;
	}

	/** Replies the total size of the Shape file.
	 *
	 * @return the size of the shape file in bytes
	 * @throws IOException in case of error.
	 */
	public int getFileSize() throws IOException {
		if (!this.headerWasread) {
			readHeader();
		}
		return this.fileSize;
	}

	/** Replies the type of the elements stored in the file.
	 *
	 * <p>This type of element is extracted from header.
	 *
	 * @return the type of element, or <code>null</code> if
	 *     the header could not be read.
	 */
	public ShapeElementType getShapeElementType() {
		if (!this.headerWasread) {
			try {
				readHeader();
			} catch (IOException exception) {
				return null;
			}
		}
		return this.expectedShapeType;
	}

	/** Replies the count of bytes read from the shape file.
	 *
	 * @return the count of bytes read from shape file.
	 * @throws IOException in case of error.
	 */
	public int getFileReadingPosition() throws IOException {
		readHeader();
		return this.buffer.position() + this.bufferPosition;
	}

	/** Replies the bounds read from the shape file header.
	 *
	 * @return the bounds or <code>null</code>
	 */
	public ESRIBounds getBoundsFromHeader() {
		try {
			readHeader();
		} catch (IOException exception) {
			return null;
		}
		return new ESRIBounds(
				this.minx, this.maxx,
				this.miny, this.maxy,
				this.minz, this.maxz,
				this.minm, this.maxm);
	}

	/** Read the header of the shape file.
	 *
	 * @throws IOException in case of error.
	 * @throws EOFException if the end of the file was reached before reading the header.
	 * @throws NotShapeFileException if the attached file is not a shape file, i.e.
	 *     the file's header does not correspond to a Shape file.
	 * @throws InvalidShapeFileVersionException if the version of the ShapeFile format that
	 *     is specified into the file is not compatible with the one supported by
	 *     this reader.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public final void readHeader() throws IOException {
		if (!this.headerWasread) {
			preReadingStage();

			final ByteBuffer hbuffer = ByteBuffer.allocate(100);
			final int read = this.stream.read(hbuffer);
			if (read < 0) {
				throw new EOFException();
			}
			hbuffer.rewind();
			hbuffer.limit(read);

			hbuffer.order(ByteOrder.BIG_ENDIAN);

			// File Code (Big endian)
			final int fileType = hbuffer.getInt();
			if (fileType != SHAPE_FILE_CODE) {
				throw new NotShapeFileException();
			}

			hbuffer.position(24);
			// File Length in 16-bit words (Big endian)
			final int streamFileSize = hbuffer.getInt();
			this.fileSize = fromESRIWords(streamFileSize);

			hbuffer.order(ByteOrder.LITTLE_ENDIAN);

			// Version (Little endian)
			final int version = hbuffer.getInt();
			if (version != SHAPE_FILE_VERSION) {
				throw new InvalidShapeFileVersionException(version, SHAPE_FILE_VERSION);
			}

			// Shape Type (Little endian)
			this.expectedShapeType = ShapeElementType.fromESRIInteger(hbuffer.getInt());

			// Xmin (Little endian)
			this.minx = fromESRI_x(hbuffer.getDouble());
			// Ymin (Little endian)
			this.miny = fromESRI_y(hbuffer.getDouble());
			// Xmax (Little endian)
			this.maxx = fromESRI_x(hbuffer.getDouble());
			// Ymax (Little endian)
			this.maxy = fromESRI_y(hbuffer.getDouble());
			this.minz = fromESRI_z(hbuffer.getDouble());
			this.maxz = fromESRI_z(hbuffer.getDouble());
			this.minm = fromESRI_m(hbuffer.getDouble());
			this.maxm = fromESRI_m(hbuffer.getDouble());

			if (!Double.isNaN(this.minx) && !Double.isNaN(this.maxx)
					&& this.maxx < this.minx) {
				final double t = this.maxx;
				this.maxx = this.minx;
				this.minx = t;
			}

			if (!Double.isNaN(this.miny) && !Double.isNaN(this.maxy)
					&& this.maxy < this.miny) {
				final double t = this.maxy;
				this.maxy = this.miny;
				this.miny = t;
			}

			if (!Double.isNaN(this.minz) && !Double.isNaN(this.maxz)
					&& this.maxz < this.minz) {
				final double t = this.maxz;
				this.maxz = this.minz;
				this.minz = t;
			}

			if (!Double.isNaN(this.minm) && !Double.isNaN(this.maxm)
					&& this.maxm < this.minm) {
				final double t = this.maxm;
				this.maxm = this.minm;
				this.minm = t;
			}

			this.headerWasread = true;
			this.nextExpectedRecordIndex = 0;

			postHeaderReadingStage();

			initializeContentBuffer();

			if (this.taskProgression != null) {
				this.taskProgression.setProperties(this.buffer.position() + HEADER_BYTES, 0, this.fileSize, false);
				this.taskProgression.setIndeterminate(false);
			}
		}
	}

	/** Invoked to initialize the buffer for the content of the file.
	 *
	 * @throws IOException in case of error.
	 */
	private void initializeContentBuffer() throws IOException {
		if (this.seekEnabled) {
			this.buffer = ByteBuffer.allocate(this.fileSize - HEADER_BYTES);
			final int read = this.stream.read(this.buffer);
			if (read < 0) {
				throw new EOFException();
			}
			this.buffer.rewind();
			this.buffer.limit(read);
			this.bufferPosition = HEADER_BYTES;
		} else {
			this.buffer = ByteBuffer.allocate(BLOCK_SIZE);
			final int read = this.stream.read(this.buffer);
			if (read < 0) {
				throw new EOFException();
			}
			this.buffer.rewind();
			this.buffer.limit(read);
			this.bufferPosition = HEADER_BYTES;
		}
	}

	/** Disable the seek feature to have lower memory foot-print.
	 * This function should be print before any invocation (directly
	 * or indirectly) to {@link #readHeader()}.
	 *
	 * @throws IOException in case of error.
	 */
	public void disableSeek() throws IOException {
		if (this.seekEnabled && !this.headerWasread) {
			this.seekEnabled = false;
		}
	}

	/** Replies if the seek feature is enabled.
	 *
	 * @return <code>true</code> if the seek feature is enabled,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isSeekEnabled() {
		return this.seekEnabled;
	}

	/** Move the reading head at the specified record index.
	 *
	 * <p>If the index is negative, the next record to read is assumed to be
	 * the first record.
	 * If the index is greater or equals to the count of records, the exception
	 * {@link EOFException} will be thrown.
	 *
	 * @param recordIndex is the index of record to reply at the next read.
	 * @throws IOException in case of error.
	 */
	public abstract void seek(int recordIndex) throws IOException;

	/** Read the elements for a shape file.
	 *
	 * @return the list of the elements read from the Shape file or <code>null</code> on end-of-file.
	 * @throws IOException in case of error.
	 */
	public E read() throws IOException {
		boolean status = false;
		try {

			// Read header if not already read
			readHeader();

			// Read the records
			E element;
			try {
				do {
					element = readRecord(this.nextExpectedRecordIndex);
					if (!postRecordReadingStage(element)) {
						element = null;
					}
					++this.nextExpectedRecordIndex;
				}
				while (element == null);
			} catch (EOFException e) {
				element = null;
				close();
				postReadingStage(true);
			}
			status = true;

			return element;
		} finally {
			if (this.taskProgression != null) {
				this.taskProgression.setValue(this.buffer.position() + HEADER_BYTES);
			}
			if (!status) {
				close();
				postReadingStage(status);
			}
		}
	}

	/** Close the stream.
	 *
	 * @throws IOException in case of error.
	 */
	@Override
	public void close() throws IOException {
		this.nextExpectedRecordIndex = -1;
		if (this.stream != null) {
			this.stream.close();
		}
	}

	/**
	 * Read a record according to its type.
	 *
	 * @param recordIndex is the index of the read record.
	 * @return the element extracted from the record
	 * @throws EOFException if EOF is reach to early.
	 * @throws IOException in case of error.
	 */
	protected abstract E readRecord(int recordIndex) throws EOFException, IOException;

	/** Called after the reader was initialized and before the
	 * header of the shape file was read.
	 *
	 * @throws IOException in case of error.
	 */
	protected void preReadingStage() throws IOException {
		//
	}

	/** Called after all the entries was read.
	 *
	 * @param success is <code>true</code> is the reading was successfull,
	 *     otherwhise <code>false</code>.
	 * @throws IOException in case of error.
	 */
	protected void postReadingStage(boolean success) throws IOException {
		//
	}

	/** Called just after an entry was read but just before the dBase attributes
	 * were read.
	 *
	 * @param element_representation is the value returned by the reading function.
	 * @return <code>true</code> if the object is assumed to be valid (ie. replies by
	 *     the reading function, otherwhise <code>false</code>.
	 * @throws IOException in case of error.
	 */
	protected boolean postRecordReadingStage(E element_representation) throws IOException {
		return true;
	}

	/** Called just after the header was read.
	 *
	 * @throws IOException in case of error.
	 */
	protected void postHeaderReadingStage() throws IOException {
		//
	}

	/**
	 * Returns an iterator over a set of elements of type E.
	 * The seek feature is automatically disabled when invoking this function.
	 * See {@link #iterator(boolean)} to prevent seek disabling.
	 *
	 * @return an Iterator.
	 * @see #iterator(boolean)
	 * @see #iterator(Class,boolean)
	 * @see #disableSeek()
	 */
	@Override
	public Iterator<E> iterator() {
		try {
			disableSeek();
		} catch (IOException exception) {
			//
		}
		return new ElementIterator();
	}

	/**
	 * Returns an iterator over a set of elements of type E.
	 * The seek feature is not automatically disabled when invoking this function.
	 * See {@link #iterator()} to disable seek automatically.
	 *
	 * @param disableSeek indicates if the seek feature must be disabled or not.
	 * @return an Iterator.
	 * @see #iterator()
	 * @see #iterator(Class)
	 * @see #iterator(Class,boolean)
	 * @see #disableSeek()
	 */
	public Iterator<E> iterator(boolean disableSeek) {
		if (disableSeek) {
			try {
				disableSeek();
			} catch (IOException exception) {
				//
			}
		}
		return new ElementIterator();
	}

	/**
	 * Returns an iterator over a set of elements of type E.
	 * The seek feature is automatically disabled when invoking this function.
	 * See {@link #iterator(boolean)} to prevent seek disabling.
	 *
	 * @param <EE> is the type of the elements to reply.
	 * @param type is the type of the elements to reply.
	 * @return an Iterator.
	 * @see #iterator()
	 * @see #iterator(boolean)
	 * @see #iterator(Class,boolean)
	 * @see #disableSeek()
	 */
	public <EE extends E> Iterator<EE> iterator(Class<EE> type) {
		try {
			disableSeek();
		} catch (IOException exception) {
			//
		}
		return Iterators.filter(new ElementIterator(), type);
	}

	/**
	 * Returns an iterator over a set of elements of type E.
	 * The seek feature is not automatically disabled when invoking this function.
	 * See {@link #iterator()} to disable seek automatically.
	 *
	 * @param <EE> is the type of the elements to reply.
	 * @param type is the type of the elements to reply.
	 * @param disableSeek indicates if the seek feature must be disabled or not.
	 * @return an Iterator.
	 * @see #iterator()
	 * @see #iterator(boolean)
	 * @see #iterator(Class)
	 * @see #disableSeek()
	 */
	public <EE extends E> Iterator<EE> iterator(Class<EE> type, boolean disableSeek) {
		if (disableSeek) {
			try {
				disableSeek();
			} catch (IOException exception) {
				//
			}
		}
		return Iterators.filter(new ElementIterator(), type);
	}

	/** Set the reading position, excluding the header.
	 *
	 * @param recordIndex is the index of the next record to read.
	 * @param byteIndex is the index of the next byte to read (excluding the header).
	 * @throws IOException in case of error.
	 * @throws SeekOperationDisabledException seeking operation not allowed.
	 */
	protected void setReadingPosition(int recordIndex, int byteIndex) throws IOException {
		if (this.seekEnabled) {
			this.nextExpectedRecordIndex = recordIndex;
			this.buffer.position(byteIndex);
		} else {
			throw new SeekOperationDisabledException();
		}
	}

	/** Ensure that the reading buffer is containing enoug bytes to read.
	 *
	 * @param amount is the count of expected bytes
	 * @throws IOException in case of error.
	 * @throws EOFException if the end of the file was reached.
	 */
	protected void ensureAvailableBytes(int amount) throws IOException {
		if (!this.seekEnabled && amount > this.buffer.remaining()) {
			this.bufferPosition += this.buffer.position();
			this.buffer.compact();
			int limit = this.buffer.position();
			final int read = this.stream.read(this.buffer);
			if (read < 0) {
				if (limit == 0) {
					throw new EOFException();
				}
			} else {
				limit += read;
			}
			this.buffer.rewind();
			this.buffer.limit(limit);

		}
		if (amount > this.buffer.remaining()) {
			throw new EOFException();
		}
	}

	/** Skip an amount of bytes.
	 *
	 * @param amount the amount to skip.
	 * @throws IOException in case of error.
	 */
	protected void skipBytes(int amount) throws IOException {
		ensureAvailableBytes(amount);
		this.buffer.position(this.buffer.position() + amount);
	}

	/** Read a big endian 4-byte integer.
	 *
	 * @return a big endian 4-byte integer.
	 * @throws IOException in case of error.
	 */
	protected final int readBEInt() throws IOException {
		ensureAvailableBytes(4);
		this.buffer.order(ByteOrder.BIG_ENDIAN);
		return this.buffer.getInt();
	}

	/** Read a big endian 8-byte floating point number.
	 *
	 * @return a big endian 8-byte floating point number.
	 * @throws IOException in case of error.
	 */
	protected final double readBEDouble() throws IOException {
		ensureAvailableBytes(8);
		this.buffer.order(ByteOrder.BIG_ENDIAN);
		return this.buffer.getDouble();
	}

	/** Read a little endian 4-byte integer.
	 *
	 * @return a little endian 4-byte integer.
	 * @throws IOException in case of error.
	 */
	protected final int readLEInt() throws IOException {
		ensureAvailableBytes(4);
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
		return this.buffer.getInt();
	}

	/** Read a little endian 8-byte floating point number.
	 *
	 * @return a little endian 8-byte floating point number.
	 * @throws IOException in case of error.
	 */
	protected final double readLEDouble() throws IOException {
		ensureAvailableBytes(8);
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
		return this.buffer.getDouble();
	}

	/** Iterator on the read elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class ElementIterator implements Iterator<E> {

		private E nextElement;

		/** Constructor.
		 */
		ElementIterator() {
			try {
				this.nextElement = AbstractCommonShapeFileReader.this.read();
			} catch (IOException e) {
				this.nextElement = null;
				throw new RuntimeException(e);
			}
		}

		@Override
		public boolean hasNext() {
			return this.nextElement != null;
		}

		@Override
		public E next() {
			final E toReply = this.nextElement;
			if (toReply == null) {
				throw new NoSuchElementException();
			}

			try {
				this.nextElement = AbstractCommonShapeFileReader.this.read();
			} catch (IOException e) {
				this.nextElement = null;
			}

			return toReply;
		}

		@Override
		public void remove() {
			//
		}

	} /* class ElementIterator */

}
