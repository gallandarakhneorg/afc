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

package org.arakhne.afc.io.shape;

import static org.arakhne.afc.io.shape.ESRIFileUtil.HEADER_BYTES;
import static org.arakhne.afc.io.shape.ESRIFileUtil.toESRIWords;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.WritableByteChannel;

/**
 * This class is a shape file index writer.
 *
 * <p>The specification of the ESRI Shape file format is described in
 * <a href="./doc-files/esri_specs_0798.pdf">the July 98 specification document</a>.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ShapeFileIndexWriter extends AbstractCommonShapeFileWriter<ShapeFileIndexRecord> {

	private final ESRIBounds hbounds;

	private int lastOffsetToWrite = HEADER_BYTES;

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds to put in the header.
	 * @throws IOException in case of error.
	 */
	public ShapeFileIndexWriter(File shapeName, ShapeElementType elementType, ESRIBounds bounds) throws IOException {
		super(shapeName, elementType);
		this.hbounds = bounds;
	}

	/** Constructor.
	 * @param shapeName is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds to put in the header.
	 * @throws IOException in case of error.
	 */
	public ShapeFileIndexWriter(URL shapeName, ShapeElementType elementType, ESRIBounds bounds) throws IOException {
		super(shapeName, elementType);
		this.hbounds = bounds;
	}

	/** Constructor.
	 * @param stream is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds to put in the header.
	 * @throws IOException in case of error.
	 */
	public ShapeFileIndexWriter(OutputStream stream, ShapeElementType elementType, ESRIBounds bounds) throws IOException {
		super(stream, elementType);
		this.hbounds = bounds;
	}

	/** Constructor.
	 * @param channel is the file to write
	 * @param elementType is the type of the elements to write.
	 * @param bounds are the bounds to put in the header.
	 * @throws IOException in case of error.
	 */
	public ShapeFileIndexWriter(WritableByteChannel channel, ShapeElementType elementType, ESRIBounds bounds) throws IOException {
		super(channel, elementType);
		this.hbounds = bounds;
	}

	@Override
	protected ESRIBounds getFileBounds() {
		return this.hbounds;
	}

	@Override
	protected void writeElement(int recIndex, ShapeFileIndexRecord element, ShapeElementType type) throws IOException {
		assert isHeaderWritten();

		writeBEInt(toESRIWords(element.getOffsetInFile()));
		writeBEInt(toESRIWords(element.getRecordContentLength()));

		this.lastOffsetToWrite += element.getEntireRecordLength();
	}

	/** Write a record with the given length at the following offset in the SHX file.
	 *
	 * <p>The length if the count of bytes inside the record (including record header and
	 * record content). Assuming record header with length of 8 bytes and record
	 * content with length of <code>l</code> bytes, the total record length
	 * is <code>l</code>+8 bytes.
	 *
	 * <p>Because the ESRI standard stores this value as a count of 16-bit words
	 * (not a count of bytes), it means that the given length may be a
	 * multiple of 2. If this record is created with a length which is not a
	 * multiple of 2, this object will assumed the upper multiple as length.
	 *
	 * @param recordLength is the length to write inside a record at the end of the SHX.
	 * @throws IOException in case of error.
	 */
	public final void write(int recordLength) throws IOException {
		write(new ShapeFileIndexRecord(this.lastOffsetToWrite, recordLength, false, -1));
	}

}
