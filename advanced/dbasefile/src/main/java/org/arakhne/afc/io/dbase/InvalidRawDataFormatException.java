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

package org.arakhne.afc.io.dbase;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Exception throws when the raw data extracted from the dBase file
 * does not correspond to a valid type according to the Java
 * parsing API.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class InvalidRawDataFormatException extends DBaseFileException {

	private static final long serialVersionUID = 921070660800491853L;

	private final int recordNumber;

	private final int columnNumber;

	private final CharSequence rawData;

	/** Constructor.
	 *
	 * @param recordNumber the record number.
	 * @param columnNumber the column number.
	 * @param rawData the raw data.
	 */
	public InvalidRawDataFormatException(int recordNumber, int columnNumber, CharSequence rawData) {
		this.recordNumber = recordNumber;
		this.columnNumber = columnNumber;
		this.rawData = rawData;
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder(super.toString());
		buffer.append(": record="); //$NON-NLS-1$
		buffer.append(this.recordNumber);
		buffer.append("; column="); //$NON-NLS-1$
		buffer.append(this.columnNumber);
		buffer.append("; raw="); //$NON-NLS-1$
		buffer.append(this.rawData);
		return buffer.toString();
	}

	/** Replies the index of the record on which the error occurs.
	 *
	 * @return an index between {@code 1} and the count of record (inclusives)
	 */
	@Pure
	public int getRecordNumber() {
		return this.recordNumber;
	}

	/** Replies the column index on which the error occurs.
	 *
	 * @return a column index between {@code 0} (inclusive) and {@link DBaseFileReader#getDBFFieldCount()} (exclusive).
	 */
	@Pure
	public int getColumnIndex() {
		return this.columnNumber;
	}

	/** Replies the raw data that cause this exception.
	 *
	 * @return the raw data
	 */
	@Pure
	public CharSequence getRawData() {
		return this.rawData;
	}

}
