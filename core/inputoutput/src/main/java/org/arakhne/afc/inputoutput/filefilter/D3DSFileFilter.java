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

package org.arakhne.afc.inputoutput.filefilter;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.arakhne.afc.inputoutput.endian.EndianNumbers;
import org.arakhne.afc.inputoutput.filetype.FileType;
import org.arakhne.afc.inputoutput.filetype.MagicNumber;
import org.arakhne.afc.inputoutput.filetype.MagicNumberStream;
import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.vmutil.locale.Locale;

/** File filter for the original 3D Studio model (aka, .3ds) files.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class D3DSFileFilter extends AbstractFileFilter {

	/** Default extension for the 3DS files.
	 */
	public static final String EXTENSION_3DS = "3ds"; //$NON-NLS-1$

	/** Construct.
	 */
	public D3DSFileFilter() {
		this(true);
	}

	/**
	 * @param acceptDirectories is <code>true</code> to
	 *     permit to this file filter to accept directories;
	 *     <code>false</code> if the directories should not
	 *     match.
	 */
	public D3DSFileFilter(boolean acceptDirectories) {
		super(
				acceptDirectories,
				Locale.getString(D3DSFileFilter.class, "FILE_FILTER_NAME"), //$NON-NLS-1$
				EXTENSION_3DS);
	}

	/** Replies if the specified file contains <code>.3ds</code> data.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains <code>.3ds</code> data,
	 *     otherwise <code>false</code>
	 */
	public static boolean is3DSFile(File file) {
		return FileType.isContentType(file, MimeName.MIME_3DS.getMimeConstant());
	}

	/** Replies if the specified file contains <code>.3ds</code> data.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains <code>.3ds</code> data,
	 *     otherwise <code>false</code>
	 */
	public static boolean is3DSFile(URL file) {
		return FileType.isContentType(file, MimeName.MIME_3DS.getMimeConstant());
	}

	/** Replies if the specified file contains <code>.3ds</code> data.
	 *
	 * @param file is the file to test.
	 * @return <code>true</code> if the given file contains <code>.3ds</code> data,
	 *     otherwise <code>false</code>
	 */
	public static boolean is3DSFile(String file) {
		return FileType.isContentType(file, MimeName.MIME_3DS.getMimeConstant());
	}

	static {
		//Register MIME file contents
		FileType.addContentType(new Dot3DSMagicNumber());
	}

	/** This class defines a set of informations that could distinguish
	 * a file content from another one. It is also known as Magic Number
	 * on several operating systems.
	 *
	 * <p>This magic number supports the text content.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class Dot3DSMagicNumber extends MagicNumber {

		/** Chunk id for the main dot 3ds.
		 */
		private static final int MAIN3DS_4D4D = 0x4d4d;

		/** Chunk id for the version number.
		 */
		private static final int FILE_VERSION_0002 = 0x0002;

		/** Constructor.
		 */
		Dot3DSMagicNumber() {
			super(
					MimeName.MIME_3DS,
					MimeName.MIME_OCTET_STREAM);
		}

		/** Replies if the specified stream contains data
		 * that corresponds to this magic number.
		 */
		@Override
		@SuppressWarnings("checkstyle:magicnumber")
		protected final boolean isContentType(MagicNumberStream stream) {
			try {
				byte[] bytes = stream.read(0, 2);
				short tag = EndianNumbers.toBEShort(bytes[0], bytes[1]);
				if (tag != MAIN3DS_4D4D) {
					return false;
				}
				bytes = stream.read(6, 2);
				tag = EndianNumbers.toBEShort(bytes[0], bytes[1]);
				return tag >= FILE_VERSION_0002;
			} catch (IOException e) {
				return false;
			}
		}

	}

}
