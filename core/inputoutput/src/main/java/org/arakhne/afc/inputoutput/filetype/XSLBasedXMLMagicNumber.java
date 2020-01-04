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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimeType;

/** This class defines a set of informations that could distinguish
 * a file content from another one. It is also known as Magic Number
 * on several operating systems.
 *
 * <p>This magic number supports XML files and permits to
 * test the XSL namespace.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class XSLBasedXMLMagicNumber extends XMLMagicNumber {

	private final String schema;

	private final Pattern regEx;

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param rootNode is the name of the XML root node from which XLS information may be extracted.
	 * @param schema is the XSL Schema ID associated to this magic number.
	 */
	public XSLBasedXMLMagicNumber(MimeType mimeType, String rootNode, String schema) {
		this(mimeType, rootNode, schema, false);
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param rootNode is the name of the XML root node from which XLS information may be extracted.
	 * @param schema is the XSL Schema ID associated to this magic number.
	 * @param useRegEx indicates if the schema string is a regular expression or not.
	 */
	public XSLBasedXMLMagicNumber(MimeType mimeType, String rootNode, String schema, boolean useRegEx) {
		super(mimeType, rootNode);
		this.schema = schema;
		this.regEx = useRegEx ? Pattern.compile(normalizeRegEx(schema)) : null;
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param rootNode is the name of the XML root node from which XLS information may be extracted.
	 * @param formatVersion is the version of the supported format.
	 * @param schema is the XSL Schema ID associated to this magic number.
	 */
	public XSLBasedXMLMagicNumber(MimeType mimeType, String rootNode, String formatVersion, String schema) {
		this(mimeType, rootNode, formatVersion, schema, false);
	}

	/** Constructor.
	 * @param mimeType is the MIME type associated to this magic number.
	 * @param rootNode is the name of the XML root node from which XLS information may be extracted.
	 * @param formatVersion is the version of the supported format.
	 * @param schema is the XSL Schema ID associated to this magic number.
	 * @param useRegEx indicates if the schema string is a regular expression or not.
	 * @since 4.0
	 */
	public XSLBasedXMLMagicNumber(MimeType mimeType, String rootNode, String formatVersion, String schema, boolean useRegEx) {
		super(mimeType, rootNode, formatVersion);
		this.schema = schema;
		this.regEx = useRegEx ? Pattern.compile(normalizeRegEx(schema)) : null;
	}

	private static String normalizeRegEx(String regEx) {
		final StringBuilder buffer = new StringBuilder();
		buffer.append("^"); //$NON-NLS-1$
		buffer.append(regEx);
		buffer.append("$"); //$NON-NLS-1$
		return buffer.toString();
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
	@Override
	protected boolean isContentType(String schemaId, String schemaVersion, String systemId, String publicId) {
		if (this.schema == null) {
			return false;
		}
		if (this.regEx != null) {
			final Matcher matcher = this.regEx.matcher(schemaId);
			return matcher != null && matcher.find();
		}
		return this.schema.equalsIgnoreCase(schemaId);
	}

}
