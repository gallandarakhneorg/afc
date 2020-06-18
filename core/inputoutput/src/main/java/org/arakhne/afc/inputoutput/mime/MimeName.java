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

package org.arakhne.afc.inputoutput.mime;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

/** An utility class that declares MIME constants.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum MimeName {

	/**
	 * MIME type for unknown file formats.
	 */
	MIME_UNKNOW("content/unknown"), //$NON-NLS-1$

	/**
	 * MIME type for binary files.
	 */
	MIME_OCTET_STREAM("application/octet-stream"), //$NON-NLS-1$

	/**
	 * MIME type for plain text files.
	 */
	MIME_PLAIN_TEXT("text/plain"), //$NON-NLS-1$

	/**
	 * MIME type for serialized java objects.
	 */
	MIME_SERIALIZED_OBJECT("application/x-java-serialized-object"), //$NON-NLS-1$

	/**
	 * MIME type for XML files.
	 */
	MIME_XML("application/xml"), //$NON-NLS-1$

	/**
	 * MIME type for ZIP archive files.
	 */
	MIME_ZIP("application/zip"), //$NON-NLS-1$

	/**
	 * MIME type for JAR archive files.
	 */
	MIME_JAR("application/java-archive"), //$NON-NLS-1$

	/**
	 * MIME type for GNU zip file.
	 */
	MIME_GZIP("multipart/x-gzip"), //$NON-NLS-1$

	/**
	 * MIME type for GIF pictures.
	 */
	MIME_GIF("image/gif"), //$NON-NLS-1$

	/**
	 * MIME type for JPEG pictures.
	 */
	MIME_JPG("image/jpeg"), //$NON-NLS-1$

	/**
	 * MIME type for PNG pictures.
	 */
	MIME_PNG("image/png"), //$NON-NLS-1$

	/**
	 * MIME type for Bitmap pictures.
	 */
	MIME_BMP("image/bmp"), //$NON-NLS-1$

	/**
	 * MIME type for collection of Java objects.
	 */
	MIME_OBJECT_COLLECTION("application/x-java-object-collection; class=java.util.Collection"), //$NON-NLS-1$

	/**
	 * MIME type for list of Java objects.
	 */
	MIME_OBJECT_LIST("application/x-java-object-list; class=java.util.List"), //$NON-NLS-1$

	/**
	 * MIME type for list of files.
	 */
	MIME_FILE_LIST("application/x-java-file-list; class=java.util.List"), //$NON-NLS-1$

	/**
	 * MIME type for lists of URIs.
	 */
	MIME_URI_LIST("text/uri-list"), //$NON-NLS-1$

	/**
	 * MIME type for ATOM format.
	 */
	MIME_ATOM_XML("application/atom+xml"), //$NON-NLS-1$

	/**
	 * MIME type for CAO IGES exchange format.
	 */
	MIME_IGES("application/iges"), //$NON-NLS-1$

	/**
	 * MIME type for Javascript.
	 */
	MIME_JAVASCRIPT("application/javascript"), //$NON-NLS-1$

	/**
	 * MIME type for AutoCAD DXF file.
	 */
	MIME_DXF("application/dxf"), //$NON-NLS-1$

	/**
	 * MIME type for MPEG3 file.
	 */
	MIME_MP3("audio/mpeg"), //$NON-NLS-1$

	/**
	 * MIME type for MPEG4 file.
	 */
	MIME_MP4("application/mp4"), //$NON-NLS-1$

	/**
	 * MIME type for MPEG4 audio file.
	 */
	MIME_MP4_AUDIO("audio/mp4"), //$NON-NLS-1$

	/**
	 * MIME type for Microsoft Word.
	 */
	MIME_MSWORD("application/msword"), //$NON-NLS-1$

	/**
	 * MIME type for Portable Document Format.
	 */
	MIME_PDF("application/pdf"), //$NON-NLS-1$

	/**
	 * MIME type for Postscript.
	 */
	MIME_POSTSCRIPT("application/postscript"), //$NON-NLS-1$

	/**
	 * MIME type for Rich Text File.
	 */
	MIME_RTF("application/rtf"), //$NON-NLS-1$

	/**
	 * MIME type for SGML file.
	 */
	MIME_SGML("application/sgml"), //$NON-NLS-1$

	/**
	 * MIME type for Microsoft Excel.
	 */
	MIME_MSEXCEL("application/vnd.ms-excel"), //$NON-NLS-1$

	/**
	 * MIME type for Microsoft Power Point.
	 */
	MIME_MSPOWERPOINT("application/vnd.ms-powerpoint"), //$NON-NLS-1$

	/**
	 * MIME type for tar file.
	 */
	MIME_TAR("application/x-tar"), //$NON-NLS-1$

	/**
	 * MIME type for Audio Wave.
	 */
	MIME_WAV("audio/x-wav"), //$NON-NLS-1$

	/**
	 * MIME type for TIFF image.
	 */
	MIME_TIFF("image/tiff"), //$NON-NLS-1$

	/**
	 * MIME type for Portable Bitmap.
	 */
	MIME_PBM("image/x-portable-bitmap"), //$NON-NLS-1$

	/**
	 * MIME type for Portable Graymap.
	 */
	MIME_PGM("image/x-portable-graymap"), //$NON-NLS-1$

	/**
	 * MIME type for Portable Pixmap.
	 */
	MIME_PPM("image/x-portable-pixmap"), //$NON-NLS-1$

	/**
	 * MIME type for CSV file.
	 */
	MIME_CSV("text/csv"), //$NON-NLS-1$

	/**
	 * MIME type for HTML file.
	 */
	MIME_HTML("text/html"), //$NON-NLS-1$

	/**
	 * MIME type for CSS file.
	 */
	MIME_CSS("text/css"), //$NON-NLS-1$

	/**
	 * MIME type for rich text.
	 */
	MIME_RICH_TEXT("text/richtext"), //$NON-NLS-1$

	/**
	 * MIME type for Discreet 3D Studio file.
	 */
	MIME_3DS("application/x-3ds"), //$NON-NLS-1$

	/**
	 * MIME type for Collada file.
	 */
	MIME_COLLADA("application/x-collada"), //$NON-NLS-1$

	/**
	 * MIME type for Window Initialization file.
	 */
	MIME_INIT("application/x-winassoc-ini"), //$NON-NLS-1$

	/**
	 * MIME type for H.264 video.
	 */
	MIME_H264("video/h264"), //$NON-NLS-1$

	/**
	 * MIME type for MPEG video.
	 */
	MIME_MPEG_VIDEO("video/mpeg"), //$NON-NLS-1$

	/**
	 * MIME type for Quicktime video.
	 */
	MIME_QUICKTIME("video/quicktime"), //$NON-NLS-1$

	/**
	 * MIME type for Microsoft video.
	 */
	MIME_AVI("video/msvideo"), //$NON-NLS-1$

	/**
	 * MIME type for Digital Video (DV).
	 */
	MIME_DV("video/dv"), //$NON-NLS-1$

	/**
	 * MIME type for Quantum GIS (QGIS), an ESRI-compliant Shape file.
	 * @see #MIME_SHAPE_FILE
	 */
	MIME_QGIS("application/x-qgis"), //$NON-NLS-1$

	/**
	 * MIME type for ESRI Shape File (SHP).
	 * @see #MIME_QGIS
	 */
	MIME_SHAPE_FILE("application/x-shapefile"), //$NON-NLS-1$

	/**
	 * MIME type for dBase file (dbf).
	 */
	MIME_DBASE_FILE("application/x-dbf"), //$NON-NLS-1$

	/**
	 * MIME type for GIS project (gip).
	 */
	MIME_GIS_PROJECT("application/x-GIS-project"), //$NON-NLS-1$

	/**
	 * MIME type for Compressed GIS project (giz).
	 */
	MIME_GIS_PROJECT_ZIP("application/x-GIS-zipped-project"), //$NON-NLS-1$

	/**
	 * MIME type for SeT Framework project (fwj).
	 */
	MIME_SFC_PROJECT("application/x-setframework-project"), //$NON-NLS-1$

	/**
	 * MIME type for Compressed SeT framework project (fwz).
	 *
	 * @since 4.0
	 */
	MIME_SFC_PROJECT_ZIP("application/x-setframework-zipped-project"), //$NON-NLS-1$

	/**
	 * MIME type for SeT lab activation licenses (lic).
	 *
	 * @since 4.0
	 */
	MIME_SETLAB_ACTIVATION_LICENSE("application/x-setlab-activation-license"), //$NON-NLS-1$

	/**
	 * MIME type for SeT lab JaSIM configuration file (sfg).
	 */
	MIME_JASIM_CONFIGURATION("application/x-jasim-configuration"), //$NON-NLS-1$

	/**
	 * MIME type for SeT lab JaSIM serialized perception tree (tree).
	 */
	MIME_JASIM_SERIALIZED_TREE("application/x-jasim-serialized-tree"), //$NON-NLS-1$

	/**
	 * MIME type for Wavefront OBJ 3D models.
	 * @since 4.1
	 */
	MIME_WAVEFRONT_OBJ("application/x-tgif"); //$NON-NLS-1$


	private final String rawMime;

	MimeName(String rawMime) {
		this.rawMime = rawMime;
	}

	/** Replies the MIME type associated to this MIME constant.
	 *
	 * @return the MIME type for this MIME constant.
	 * @throws RuntimeException if the raw mime type cannot be parsed properly.
	 */
	public MimeType toMimeType() {
		try {
			return new MimeType(this.rawMime);
		} catch (MimeTypeParseException e) {
			throw new RuntimeException(e);
		}
	}

	/** Replies the MIME constant.
	 *
	 * @return the MIME constant.
	 */
	public String getMimeConstant() {
		return this.rawMime;
	}

	/** Replies if the given string corresponds to this MIME constant.
	 *
	 * @param stringMime is the string to compare to the MIME constant.
	 * @return <code>true</code> if the given Mime string corresponds
	 *     to the MIME constant of this MIME type, otherwise <code>false</code>.
	 */
	public boolean isMimeConstant(String stringMime) {
		return stringMime != null && stringMime.equalsIgnoreCase(this.rawMime);
	}

	/**
	 * Parse the given string to extract a MimeType.
	 *
	 * <p>This function differs from {@link MimeType#MimeType(String)}
	 * by the fact that this function does not throw an exception
	 * but reply <code>null</code>.
	 *
	 * @param mime the string to parse.
	 * @return the mime type or <code>null</code> if invalid.
	 */
	public static MimeType parseMimeType(String mime) {
		if (mime == null || mime.isEmpty()) {
			return null;
		}
		try {
			return new MimeType(mime);
		} catch (MimeTypeParseException e) {
			return null;
		}
	}

	/**
	 * Parse the given string to extract a MimeName.
	 *
	 * @param mime the string to parse.
	 * @return the mime name or <code>null</code> if invalid.
	 */
	public static MimeName parseMimeName(String mime) {
		if (mime == null || mime.isEmpty()) {
			return null;
		}
		MimeName directName;
		try {
			directName = MimeName.valueOf(mime);
		} catch (Exception e) {
			directName = null;
		}
		if (directName != null) {
			return directName;
		}
		for (final MimeName name : MimeName.values()) {
			if (name.isMimeConstant(mime)) {
				return name;
			}
		}
		return null;
	}

}

