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

package org.arakhne.afc.io.dbase;

import java.io.File;
import java.net.URL;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.inputoutput.endian.EndianNumbers;
import org.arakhne.afc.inputoutput.filefilter.AbstractFileFilter;
import org.arakhne.afc.inputoutput.filetype.FileType;
import org.arakhne.afc.inputoutput.filetype.MagicNumber;
import org.arakhne.afc.inputoutput.filetype.MagicNumberStream;
import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * This class permits to filter the files to show only
 * the DBASE files. It could be used by a {@link javax.swing.JFileChooser}.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class DBaseFileFilter extends AbstractFileFilter {

	/** Extension of the DBF files (without the point).
	 */
	public static final String EXTENSION_DBASE_FILE = "dbf"; //$NON-NLS-1$

	static {
		//Register MIME file contents
		FileType.addContentType(new DBaseFileMagicNumber());
	}

	/** Constructor.
	 */
	public DBaseFileFilter() {
		this(true);
	}

	/**
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 */
	public DBaseFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString("DESCRIPTION_DBF"), //$NON-NLS-1$
				EXTENSION_DBASE_FILE);
	}

	/** Replies if the specified file content is a dBase file.
	 *
	 * @param file is the file to test
	 * @return <code>true</code> if the given file contains dBase data,
	 *     othersiwe <code>false</code>.
	 */
	@Pure
	public static boolean isDbaseFile(File file) {
		return FileType.isContentType(
				file,
				MimeName.MIME_DBASE_FILE.getMimeConstant());
	}

	/** Replies if the specified file content is a dBase file.
	 *
	 * @param file is the file to test
	 * @return <code>true</code> if the given file contains dBase data,
	 *     othersiwe <code>false</code>.
	 */
	@Pure
	public static boolean isDbaseFile(URL file) {
		return FileType.isContentType(
				file,
				MimeName.MIME_DBASE_FILE.getMimeConstant());
	}

	/** Replies if the specified file content is a dBase file.
	 *
	 * @param file is the file to test
	 * @return <code>true</code> if the given file contains dBase data,
	 *     othersiwe <code>false</code>.
	 */
	@Pure
	public static boolean isDbaseFile(String file) {
		return FileType.isContentType(
				file,
				MimeName.MIME_DBASE_FILE.getMimeConstant());
	}

	/** This class defines a set of informations that could distinguish
	 * a file content from another one. It is also known as Magic Number
	 * on several operating systems.
	 *
	 * <p>This magic number supports the byte-stream content and
	 * permits to test a portion of the bytes.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class DBaseFileMagicNumber extends MagicNumber {

		/** Constructor.
		 */
		DBaseFileMagicNumber() {
			super(
					MimeName.MIME_DBASE_FILE,
					MimeName.MIME_OCTET_STREAM);
		}

		/** Replies if the specified stream contains data
		 * that corresponds to this magic number.
		 */
		@Override
		@SuppressWarnings("checkstyle:magicnumber")
		@Pure
		protected final boolean isContentType(MagicNumberStream stream) {
			try {
				byte abyte = stream.read(0);
				if (abyte != 0x03 && abyte != 0x83 && abyte != 0x8B && abyte != 0x8E) {
					return false;
				}

				// Read the count of columns
				final byte[] bytes = stream.read(8, 2);
				final int fieldCount = (EndianNumbers.toLEShort(bytes[0], bytes[1]) - 1) / 32 - 1;
				if (fieldCount < 0) {
					return false;
				}

				// Read the final character of the header
				// Position= 32 bytes for the header
				//			+32bytes * fieldCount
				abyte = stream.read(32 + 32 * fieldCount);
				if (abyte != 0x0D) {
					return false;
				}

				return true;
			} catch (Exception exception) {
				return false;
			}
		}

	}

}
