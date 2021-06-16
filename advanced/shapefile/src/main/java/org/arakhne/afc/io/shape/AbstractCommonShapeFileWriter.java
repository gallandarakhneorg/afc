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

package org.arakhne.afc.io.shape;

import static org.arakhne.afc.io.shape.ESRIFileUtil.SHAPE_FILE_CODE;
import static org.arakhne.afc.io.shape.ESRIFileUtil.SHAPE_FILE_VERSION;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRIWords;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRI_m;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRI_x;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRI_y;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRI_z;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Collection;
import java.util.Collections;

import org.arakhne.afc.progress.Progression;

/**
 * This class permits to write an ESRI Shape file.
 *
 * <p>The specification of the ESRI Shape file format is described in
 * <a href="./doc-files/esri_specs_0798.pdf">the July 98 specification document</a>.
 *
 * @param <E> is the type of element to write inside the ESRI shape file.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class AbstractCommonShapeFileWriter<E> implements AutoCloseable {

	/** Size of a block of data in the buffer, when the seek feature was disable.
	 */
	protected static final int BLOCK_SIZE = 512;

	/** Position of the first byte in the buffer.
	 */
	private int bufferPosition;

	/** This is the stream to write into.
	 */
	private final WritableByteChannel stream;

	/** Reading buffer.
	 */
	private ByteBuffer buffer;

	/** Associated task progression object.
	 */
	private Progression taskProgression;

	/** Temp file to write.
	 */
	private File tempFile;

	/** This is the temp stream to write into.
	 */
	private WritableByteChannel tempStream;

	/** Indicates if the header was red.
	 */
	private boolean headerWasWritten;

	private final ShapeElementType elementType;

	private ESRIBounds fileBounds;

	private int recordIndex;

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	public AbstractCommonShapeFileWriter(File shapeName, ShapeElementType elementType) throws IOException {
		this(new FileOutputStream(shapeName), elementType);
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	public AbstractCommonShapeFileWriter(URL shapeName, ShapeElementType elementType) throws IOException {
		this(shapeName.openConnection().getOutputStream(), elementType);
	}

	/** Constructor.
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	public AbstractCommonShapeFileWriter(OutputStream stream, ShapeElementType elementType) throws IOException {
		this(Channels.newChannel(stream), elementType);
	}

	/** Constructor.
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @throws IOException in case of error.
	 */
	public AbstractCommonShapeFileWriter(WritableByteChannel channel, ShapeElementType elementType) throws IOException {
		assert channel != null;
		this.stream = channel;
		this.buffer = null;
		this.elementType = elementType;
	}

	/** Replies the type of the elements which will be written into the output stream.
	 *
	 * @return the type of the written elements.
	 */
	public ShapeElementType getElementType() {
		return this.elementType;
	}

	/** Replies the task progression.
	 *
	 * @return the task progression object associated to this writer, or <code>null</code>
	 */
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

	/** Replies if the header was red.
	 *
	 * @return <code>true</code> if the header was red, otherwise <code>false</code>
	 */
	protected boolean isHeaderWritten() {
		return this.headerWasWritten;
	}

	/** Force this writer to write the memory buffer inside the temporary file.
	 *
	 * @throws IOException in case of error.
	 */
	protected void flush() throws IOException {
		if (this.tempStream != null && this.buffer.position() > 0) {
			final int pos = this.buffer.position();
			this.buffer.rewind();
			this.buffer.limit(pos);

			this.tempStream.write(this.buffer);

			this.buffer.rewind();
			this.buffer.limit(this.buffer.capacity());

			this.bufferPosition += pos;
		}
	}

	/** Ensure that the reading buffer is containing enoug bytes to read.
	 *
	 * @param amount is the count of expected bytes
	 * @throws IOException in case of error.
	 */
	protected void ensureAvailableBytes(int amount) throws IOException {
		if (this.buffer.remaining() < amount) {
			flush();
		}
	}

	/** Write a big endian 4-byte integer.
	 *
	 * @param v is a big endian 4-byte integer.
	 * @throws IOException in case of error.
	 */
	protected final void writeBEInt(int v) throws IOException {
		ensureAvailableBytes(4);
		this.buffer.order(ByteOrder.BIG_ENDIAN);
		this.buffer.putInt(v);
	}

	/** Write a big endian 8-byte floating point number.
	 *
	 * @param v is a big endian 8-byte floating point number.
	 * @throws IOException in case of error.
	 */
	protected final void writeBEDouble(double v) throws IOException {
		ensureAvailableBytes(8);
		this.buffer.order(ByteOrder.BIG_ENDIAN);
		this.buffer.putDouble(v);
	}

	/** Write a little endian 4-byte integer.
	 *
	 * @param v is a little endian 4-byte integer.
	 * @throws IOException in case of error.
	 */
	protected final void writeLEInt(int v) throws IOException {
		ensureAvailableBytes(4);
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
		this.buffer.putInt(v);
	}

	/** Write a little endian 8-byte floating point number.
	 *
	 * @param v is a little endian 8-byte floating point number.
	 * @throws IOException in case of error.
	 */
	protected final void writeLEDouble(double v) throws IOException {
		ensureAvailableBytes(8);
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
		this.buffer.putDouble(v);
	}

	/** Invoked to initialize the buffer for the content of the file.
	 *
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	private void initializeContentBuffer() throws IOException {
		this.tempFile = File.createTempFile("shapeWriter", ".sh_");  //$NON-NLS-1$//$NON-NLS-2$
		this.tempFile.deleteOnExit();
		this.tempStream = Channels.newChannel(new FileOutputStream(this.tempFile));
		this.buffer = ByteBuffer.allocate(BLOCK_SIZE);
		this.bufferPosition = 0;
	}

	/** Write the header of a Shape file.
	 *
	 * @param box is the bounds of the data in the shape file.
	 * @param stream is the output stream
	 * @param type is the type of the elements.
	 * @param elements are the elements which are caused the header to be written.
	 * @throws IOException in case of error.
	 */
	private void writeHeader(ESRIBounds box, ShapeElementType type, Collection<? extends E> elements) throws IOException {
		if (!this.headerWasWritten) {
			initializeContentBuffer();

			box.ensureMinMax();

			//Byte 0 : File Code (9994)
			writeBEInt(SHAPE_FILE_CODE);
			//Byte 4 : Unused (0)
			writeBEInt(0);
			//Byte 8 : Unused (0)
			writeBEInt(0);
			//Byte 12 : Unused (0)
			writeBEInt(0);
			//Byte 16 : Unused (0)
			writeBEInt(0);
			//Byte 20 : Unused (0)
			writeBEInt(0);
			//Byte 24 : File Length, fill later
			writeBEInt(0);
			//Byte 28 : Version(1000)
			writeLEInt(SHAPE_FILE_VERSION);
			//Byte 32 : ShapeType
			writeLEInt(type.shapeType);
			//Byte 36 : Xmin
			writeLEDouble(toESRI_x(box.getMinX()));
			//Byte 44 : Ymin
			writeLEDouble(toESRI_y(box.getMinY()));
			//Byte 52 : Xmax
			writeLEDouble(toESRI_x(box.getMaxX()));
			//Byte 60 : Ymax
			writeLEDouble(toESRI_y(box.getMaxY()));
			//Byte 68 : Zmin
			writeLEDouble(toESRI_z(box.getMinZ()));
			//Byte 76 : Zmax
			writeLEDouble(toESRI_z(box.getMaxZ()));
			//Byte 84 : Mmin
			writeLEDouble(toESRI_m(box.getMinM()));
			//Byte 92 : Mmax
			writeLEDouble(toESRI_m(box.getMaxM()));

			this.headerWasWritten = true;
			this.recordIndex = 0;

			onHeaderWritten(box, type, elements);
		}
	}

	/** Invoked after the header was written.
	 *
	 * @param box is the bouding box of the file
	 * @param type is the type of the elements in the file.
	 * @param elements are the elements which are caused the header to be written.
	 * @throws IOException in case of error.
	 */
	protected void onHeaderWritten(ESRIBounds box, ShapeElementType type, Collection<? extends E> elements) throws IOException {
		//
	}

	/** Flush temp buffers, write down final information in
	 * file header (file size...), and close the streams.
	 *
	 * @throws IOException in case of error.
	 */
	@Override
	@SuppressWarnings("resource")
	public void close() throws IOException {
		flush();

		if (this.tempStream != null) {
			this.tempStream.close();
			this.tempStream = null;
		}

		if (this.stream != null && this.stream.isOpen()) {

			// Copy the channels
			if (this.buffer != null && this.tempFile != null) {
				try {
					try (ReadableByteChannel in = Channels.newChannel(new FileInputStream(this.tempFile))) {
						final ByteBuffer hbuffer = ByteBuffer.allocate(100);
						in.read(hbuffer);

						hbuffer.limit(100);
						hbuffer.position(6 * 4);
						hbuffer.order(ByteOrder.BIG_ENDIAN);
						hbuffer.putInt(toESRIWords(this.bufferPosition));
						hbuffer.rewind();
						this.stream.write(hbuffer);

						int nbRead;
						this.buffer.rewind();

						while ((nbRead = in.read(this.buffer)) >= 0) {
							this.buffer.rewind();
							this.buffer.limit(nbRead);
							this.stream.write(this.buffer);
							this.buffer.rewind();
							this.buffer.limit(this.buffer.capacity());
						}
					}

				} finally {
					this.tempFile.delete();
					this.tempFile = null;
				}

			}
			this.stream.close();
		}
	}

	/** Write the Shape file and its associated files.
	 *
	 * @param element is the elements to write down
	 * @throws IOException in case of error.
	 */
	public final void write(E element) throws IOException {
		write(Collections.singleton(element));
	}

	/** Write the Shape file and its associated files.
	 *
	 * @param elements are the elements to write down
	 * @throws IOException in case of error.
	 */
	public void write(Collection<? extends E> elements) throws IOException {
		final Progression progressBar = getTaskProgression();
		Progression subTask = null;

		if (progressBar != null) {
			progressBar.setProperties(0, 0, (elements.size() + 1) * 100, false);
		}

		if (this.fileBounds == null) {
			this.fileBounds = getFileBounds();
		}

		if (this.fileBounds != null) {
			writeHeader(this.fileBounds, this.elementType, elements);

			if (progressBar != null) {
				progressBar.setValue(100);
				subTask = progressBar.subTask(elements.size() * 100);
				subTask.setProperties(0, 0, elements.size(), false);
			}

			for (final E element : elements) {

				writeElement(this.recordIndex, element, this.elementType);

				if (subTask != null) {
					subTask.setValue(this.recordIndex + 1);
				}

				++this.recordIndex;

			}
		} else {
			throw new BoundsNotFoundException();
		}

		if (progressBar != null) {
			progressBar.end();
		}
	}

	/** Write an element inside the output stream.
	 *
	 * @param recIndex is the index of the element in the file.
	 * @param element is the element to write.
	 * @param type is the expected type for the element.
	 * @throws IOException in case of error.
	 */
	protected abstract void writeElement(int recIndex, E element, ShapeElementType type) throws IOException;

	/** Invoked to retrieve the bounds of the world.
	 *
	 * @return the bounds.
	 */
	protected abstract ESRIBounds getFileBounds();

}
