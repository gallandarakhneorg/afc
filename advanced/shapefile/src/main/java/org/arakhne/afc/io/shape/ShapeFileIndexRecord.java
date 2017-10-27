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

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class describes a record inside a shape file index.
 *
 * <p>The offset if the position of the first byte of the record inside
 * the file. Basically, the first record is supporsed to have offset
 * to 100 (ie, {@link ESRIFileUtil#HEADER_BYTES}). Because the
 * ESRI standard stores this value as 16-bit words, it means that the
 * offset is always a multiple of 2. If this record is created with
 * an offset which is not a multiple of 2, this object will assumed
 * the upper multiple as length.
 *
 * <p>The length is the size of the content's part of the associated
 * shape file entry. Because the ESRI standard stores this value
 * as 16-bit words, it means that the length is always a multiple of 2.
 * If this record is created with a length which is not a multiple of 2,
 * an <em>assertion</em> may be invalidated in the constructor of this object
 * (don't forget to activate assertions in your JVM to have them).
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
@SuppressWarnings("checkstyle:magicnumber")
public class ShapeFileIndexRecord implements Comparable<ShapeFileIndexRecord> {

	/** Offset of the record in the shape file (in bytes).
	 */
	private final int offset;

	/** Length of the record content in the shape file (in bytes).
	 */
	private final int length;

	/** The index of the record.
	 */
	private final int index;

	/**
	 * @param recordOffset is the offset of the record, including the file header.
	 * @param length the length of the record.
	 * @param recordContentSize if <code>true</code> indicates that the given length is
	 *     the length of the record's content (without record header), if <code>false</code>
	 *     indicates that the given length is the length of the whole record (record
	 *     header included).
	 * @param index is the index of the record.
	 */
	ShapeFileIndexRecord(int recordOffset, int length, boolean recordContentSize, int index) {
		assert recordOffset % 2 == 0;
		assert length % 2 == 0;
		this.offset = recordOffset;
		if (recordContentSize) {
			this.length = length;
		} else {
			// because length in shx does not include shp record header
			this.length = length - 8;
		}
		this.index = index;
	}

	@Override
	@Pure
	public int hashCode() {
		int x = this.offset;
		final int y = this.length;
		int rc = 0;
		for (int i = 7; i >= 0; --i) {
			if (i == 4) {
				x = y;
			}
			rc = rc * 21 + (char) x;
			x >>>= 8;
		}
		return rc;
	}

	@Override
	@Pure
	public boolean equals(Object obj) {
		if (obj instanceof ShapeFileIndexRecord) {
			final ShapeFileIndexRecord r = (ShapeFileIndexRecord) obj;
			return r.offset == this.offset && r.length == this.length;
		}
		return false;
	}

	@Override
	@Pure
	public int compareTo(ShapeFileIndexRecord record) {
		if (record == null) {
			return -1;
		}
		final int cmp = this.offset - record.offset;
		if (cmp != 0) {
			return cmp;
		}
		return this.length - record.length;
	}

	@Override
	@Pure
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("[off="); //$NON-NLS-1$
		b.append(this.offset);
		b.append(",len="); //$NON-NLS-1$
		b.append(this.length);
		b.append(']');
		return b.toString();
	}

	/** Replies the offset of the record (in bytes)
	 * from the start of the whole shape file
	 * (file header included).
	 *
	 * @return the offset from the start of the file.
	 * @see #getOffsetInContent()
	 */
	@Pure
	public int getOffsetInFile() {
		return this.offset;
	}

	/** Replies the offset of the record (in bytes)
	 * from the start of the content part of the shape file
	 * (file header excluded).
	 *
	 * @return the offset from the start of the file.
	 * @see #getOffsetInFile()
	 */
	@Pure
	public int getOffsetInContent() {
		return this.offset - ESRIFileUtil.HEADER_BYTES;
	}

	/** Replies the length of the record (in bytes)
	 * including the record's header.
	 *
	 * @return the length of the record, including the
	 *     record header part
	 * @see #getRecordContentLength()
	 */
	@Pure
	public int getEntireRecordLength() {
		return this.length + 8;
	}

	/** Replies the length of the record (in bytes)
	 * excluding the record's header.
	 *
	 * @return the length of the record, excluding the
	 *     record header part
	 * @see #getEntireRecordLength()
	 */
	@Pure
	public int getRecordContentLength() {
		return this.length;
	}

	/** Replies index of the record if it is known, or
	 * <code>-1</code> if unknown.
	 *
	 * @return the record index or <code>-1</code> if unknown.
	 */
	@Pure
	public int getRecordIndex() {
		return this.index;
	}

}
