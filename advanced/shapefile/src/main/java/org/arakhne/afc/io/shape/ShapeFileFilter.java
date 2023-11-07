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
 * the SHAPE files. It could be used by a {@code javax.swing.JFileChooser}.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ShapeFileFilter extends AbstractFileFilter {

	/** Extension of the SHP files (without the point).
	 */
	public static final String EXTENSION_SHP = "shp"; //$NON-NLS-1$

	/** Extension of the SHP files (without the point).
	 */
	public static final String EXTENSION_SHAPE = "shape"; //$NON-NLS-1$

	static {
		//Register MIME file contents
		FileType.addContentType(new ShapeFileMagicNumber());
	}

	/** Constructor.
	 */
	public ShapeFileFilter() {
		this(true);
	}

	/** Constructor.
	 * @param acceptDirectories is {@code true} to
	 *     permit to this file filter to accept directories;
	 *     {@code false} if the directories should not
	 *     match.
	 */
	public ShapeFileFilter(boolean acceptDirectories) {
		super(acceptDirectories,
				Locale.getString("DESCRIPTION_SHP"), //$NON-NLS-1$
				EXTENSION_SHP, EXTENSION_SHAPE);
	}

	/** Replies if the specified file content is a ESRI shape file.
	 *
	 * @param file is the file to test
	 * @return {@code true} if the given file contains dBase data,
	 *     otherwise {@code false}.
	 */
	@Pure
	public static boolean isShapeFile(File file) {
		return FileType.isContentType(
				file,
				MimeName.MIME_SHAPE_FILE.getMimeConstant());
	}

	/** Replies if the specified file content is a ESRI shape file.
	 *
	 * @param file is the file to test
	 * @return {@code true} if the given file contains dBase data,
	 *     otherwise {@code false}.
	 */
	@Pure
	public static boolean isShapeFile(URL file) {
		return FileType.isContentType(
				file,
				MimeName.MIME_SHAPE_FILE.getMimeConstant());
	}

	/** Replies if the specified file content is a ESRI shape file.
	 *
	 * @param file is the file to test
	 * @return {@code true} if the given file contains dBase data,
	 *     otherwise {@code false}
	 */
	@Pure
	public static boolean isShapeFile(String file) {
		return FileType.isContentType(
				file,
				MimeName.MIME_SHAPE_FILE.getMimeConstant());
	}

	/** This clas defines a set of informations that could distinguish
	 * a file content from another one. It is also known as Magic Number
	 * on several operating systems.
	 *
	 * <p>This magic number supports the octet-stream content and
	 * permits to test a portion of the bytes.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class ShapeFileMagicNumber extends MagicNumber {

		/** Constructor.
		 */
		ShapeFileMagicNumber() {
			super(
					MimeName.MIME_SHAPE_FILE,
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
				byte[] byteContent = stream.read(0, 4);
				// Big endian
				int value = EndianNumbers.toBEInt(byteContent[0], byteContent[1], byteContent[2], byteContent[3]);
				if (value != ESRIFileUtil.SHAPE_FILE_CODE) {
					return false;
				}
				// Little endian
				byteContent = stream.read(28, 4);
				value = EndianNumbers.toLEInt(byteContent[0], byteContent[1], byteContent[2], byteContent[3]);
				return value == ESRIFileUtil.SHAPE_FILE_VERSION;
			} catch (Exception exception) {
				return false;
			}
		}

	}

}
