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

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class permits to store a record of a DBF file.
 *
 * <p>No warranty for the files that are not generated
 * by an ESRI software.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class DBaseFileRecord implements Iterable<Object> {

	private final int recordIndex;

	private final long recordOffset;

	private final Vector<Object> fields = new Vector<>();

	/** Constructor.
	 *
	 * @param index is the index of this record
	 * @param recordOffset is the offset of the first byte of this record inside the dBase file.
	 */
	DBaseFileRecord(int index, long recordOffset) {
		this.recordIndex = index;
		this.recordOffset = recordOffset;
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder('@');
		buffer.append(this.recordIndex);
		buffer.append(':');
		buffer.append(this.fields.toString());
		return buffer.toString();
	}

	/** Replies the index of the record inside the dBase file when it was red.
	 *
	 * @return a value between {@code 0} and the count of record (exclusive)
	 */
	@Pure
	public int getRecordIndex() {
		return this.recordIndex;
	}

	/** Replies the offset of the first byte of the record inside the dBase file.
	 *
	 * @return the offset of the first byte of the record inside the dBase file.
	 */
	@Pure
	public long getRecordOffset() {
		return this.recordOffset;
	}

	/** Add a field value to this record.
	 *
	 * @param fieldvalue the value.
	 */
	void add(Object fieldvalue) {
		this.fields.add(fieldvalue);
	}

	/** Replies the count of columns.
	 *
	 * @return the count of columns.
	 */
	@Pure
	public int size() {
		return this.fields.size();
	}

	/** Replies the value of the specified column.
	 *
	 * @param column is the index of the column.
	 * @return the value of the specified column.
	 */
	@Pure
	public Object getFieldValue(int column) {
		return this.fields.get(column);
	}

	@Pure
	@Override
	public Iterator<Object> iterator() {
		return new FieldIterator(this.fields.iterator());
	}

	/** Iterator on DBase fields.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class FieldIterator implements Iterator<Object> {

		private final Iterator<Object> iterator;

		/** Constructor.
		 *
		 * @param it the iterator.
		 */
		FieldIterator(Iterator<Object> it) {
			this.iterator = it;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public Object next() {
			return this.iterator.next();
		}

		@Override
		public void remove() {
			//
		}

	} /* class FieldIterator */

}
