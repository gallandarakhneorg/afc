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

package org.arakhne.afc.io.shape;

import static org.arakhne.afc.io.shape.ESRIFileUtil.HEADER_BYTES;
import static org.arakhne.afc.io.shape.ESRIFileUtil.fromESRIWords;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

/**
 * This class is a shape file index reader.
 *
 * <p>To have a lower memory foot-print, call {@link #disableSeek()}. Indeed,
 * the seek feature forces this reader to maintain a buffer of all the file content.
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
public class ShapeFileIndexReader extends AbstractCommonShapeFileReader<ShapeFileIndexRecord> {

	private static final int RECORD_SIZE = 8;

	private int recordCount = -1;

	/** Constructor.
	 * @param channel is the channel to read from.
	 */
	public ShapeFileIndexReader(ReadableByteChannel channel) {
		super(channel);
	}

	/** Constructor.
	 * @param inputStream is the stream to read
	 */
	public ShapeFileIndexReader(InputStream inputStream) {
		super(inputStream);
	}

	/** Constructor.
	 * @param file is the file to read
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	public ShapeFileIndexReader(File file) throws IOException {
		this(new FileInputStream(file));
	}

	/** Constructor.
	 * @param file is the file to read
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings("resource")
	public ShapeFileIndexReader(URL file) throws IOException {
		this(file.openStream());
	}

	@Override
	protected void postHeaderReadingStage() throws IOException {
		// The record has a length of 8 bytes
		this.recordCount = (this.fileSize - HEADER_BYTES) / RECORD_SIZE;
	}

	/** Replies the count of records in the Shape file index.
	 *
	 * @return the count of records in the Shape file index.
	 */
	public int getRecordCount() {
		try {
			readHeader();
		} catch (IOException e) {
			return 0;
		}
		return this.recordCount;
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
	@Override
	public void seek(int recordIndex) throws IOException {
		readHeader();
		int ri = recordIndex;
		if (ri < 0) {
			ri = 0;
		}
		if (ri >= this.recordCount) {
			throw new EOFException();
		}

		// Goto the record
		setReadingPosition(recordIndex, RECORD_SIZE * ri);
	}

	@Override
	protected ShapeFileIndexRecord readRecord(int recordIndex) throws EOFException, IOException {
		final int offset = fromESRIWords(readBEInt());
		final int length = fromESRIWords(readBEInt());
		return new ShapeFileIndexRecord(offset, length, true, recordIndex);
	}

}
