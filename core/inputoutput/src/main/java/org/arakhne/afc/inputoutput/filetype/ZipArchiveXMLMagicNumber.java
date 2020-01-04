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

package org.arakhne.afc.inputoutput.filetype;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.activation.MimeType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

import org.arakhne.afc.inputoutput.xml.DefaultXMLEntityResolver;
import org.arakhne.afc.inputoutput.xml.XMLUtil;

/** This class defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * <p>This magic number supports an XML file inside a zip archive.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class ZipArchiveXMLMagicNumber extends ZipArchiveMagicNumber {

	private final String rootNode;

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param formatVersion is the version of the supported format.
	 * @param innerFile is the file to open from the inside of the Zip archive
	 *     to test the type. The filename is relative to the root of the zip file
	 *     content.
	 * @param rootNode is the name of the XML root node from which XLS information may be extracted.
	 */
	public ZipArchiveXMLMagicNumber(MimeType mimeType, String formatVersion, File innerFile, String rootNode) {
		super(mimeType, formatVersion, innerFile);
		this.rootNode = rootNode;
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param innerFile is the file to open from the inside of the Zip archive
	 *     to test the type. The filename is relative to the root of the zip file
	 *     content.
	 * @param rootNode is the name of the XML root node from which XLS information may be extracted.
	 */
	public ZipArchiveXMLMagicNumber(MimeType mimeType, File innerFile, String rootNode) {
		super(mimeType, innerFile);
		this.rootNode = rootNode;
	}

	@Override
	protected final boolean isContentType(ZipInputStream zipStream, ZipEntry zipEntry, InputStream zipEntryStream) {
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			final DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new DefaultXMLEntityResolver(true));
			final Document xmlDocument = builder.parse(zipEntryStream);
			if (xmlDocument == null) {
				return false;
			}

			final DocumentType type = xmlDocument.getDoctype();

			String xslSchema = null;
			String xslVersion = null;

			if (this.rootNode != null) {
				xslSchema = XMLUtil.getAttributeValue(xmlDocument, this.rootNode, XMLMagicNumber.XMLNS);
				xslVersion = XMLUtil.getAttributeValue(xmlDocument, this.rootNode, XMLMagicNumber.VERSION);
			}

			return isContentType(
					xslSchema,
					xslVersion,
					(type == null) ? null : type.getSystemId(),
					(type == null) ? null : type.getPublicId());
		} catch (Exception e) {
			return false;
		}
	}

	/** Replies if the specified stream contains data
	 * that corresponds to this magic number.
	 *
	 * @param schemaId is the ID of the XSL schema associated to this magic number.
	 * @param schemaVersion is the ID of the XSL schema associated to this magic number.
	 * @param systemId is the DTD system ID associated to this magic number.
	 * @param publicId is the DTD system ID associated to this magic number.
	 * @return <code>true</code> if this magic number is corresponding to the given
	 *     XML document, otherwise <code>false</code>
	 */
	protected abstract boolean isContentType(String schemaId, String schemaVersion, String systemId, String publicId);

}
