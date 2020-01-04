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

package org.arakhne.afc.io.dbase;

import java.nio.charset.Charset;

import org.eclipse.xtext.xbase.lib.Pure;

/** Describes the supported languages in DBase fields.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum DBaseCodePage {

	/** DOS USA codepage.
	 */
	DOS_USA(0x01, "IBM437"), //$NON-NLS-1$

	/** DOS Multilingual codepage.
	 */
	DOS_MULTI_LINGUAL(0x01, "IBM850"), //$NON-NLS-1$

	/** DOS Central/East Europe codepage.
	 */
	DOS_EAST_EUROPE(0x64, "IBM852"), //$NON-NLS-1$

	/** DOS Nordic codepage.
	 */
	DOS_NORDIC(0x65, "IBM865"), //$NON-NLS-1$

	/** DOS Russian codepage.
	 */
	DOS_RUSSIAN(0x66, "IBM866"), //$NON-NLS-1$

	/** DOS Icelandic codepage.
	 */
	DOS_ICELANDIC(0x67, "IBM861"), //$NON-NLS-1$

	/** DOS Kamenicky/Czech codepage.
	 */
	DOS_KAMENICKY(0x68, "windows-1250"), //$NON-NLS-1$

	/** DOS Mazovia/Polish codepage.
	 */
	DOS_MAZOVIA(0x69, "windows-1250"), //$NON-NLS-1$

	/** DOS Greek codepage.
	 */
	DOS_GREEK(0x6A, "IBM737"), //$NON-NLS-1$

	/** DOS Turkish codepage.
	 */
	DOS_TURKISH(0x6B, "IBM857"), //$NON-NLS-1$

	/** Windows Standard codepage 1252.
	 */
	WINDOWS_STANDARD(0x03, "windows-1252"), //$NON-NLS-1$

	/** Windows East Europe codepage 1250.
	 */
	WINDOWS_EAST_EUROPE(0xC8, "windows-1250"), //$NON-NLS-1$

	/** Windows Russian codepage.
	 */
	WINDOWS_RUSSIAN(0xC9, "windows-1251"), //$NON-NLS-1$

	/** Windows Turkish codepage.
	 */
	WINDOWS_TURKISH(0xCA, "windows-1254"), //$NON-NLS-1$

	/** Windows Greek codepage.
	 */
	WINDOWS_GREEK(0xCB, "windows-1253"), //$NON-NLS-1$

	/** Standard Macintosh.
	 */
	MACINTOSH_STANDARD(0x04, "x-MacRoman"), //$NON-NLS-1$

	/** Macintosh Russian codepage.
	 */
	MACINTOSH_RUSSIAN(0x96, "x-MacCyrillic"), //$NON-NLS-1$

	/** Macintosh East Europe codepage.
	 */
	MACINTOSH_EAST_EUROPE(0x97, "x-MacCentralEurope"), //$NON-NLS-1$

	/** Macintosh Greek codepage.
	 */
	MACINTOSH_GREEK(0x98, "x-MacGreek"); //$NON-NLS-1$

	private final byte languageCode;
	private final String charSet;

	/** Constructor.
	 *
	 * @param languageCode the code of the language.
	 * @param charSet the character set.
	 */
	DBaseCodePage(int languageCode, String charSet) {
		this.languageCode = (byte) languageCode;
		this.charSet = charSet;
	}

	/** Replies the dBASE language code associated to this code page.
	 *
	 * @return the dBASE language code associated to this code page.
	 */
	@Pure
	public byte getLanguageCode() {
		return this.languageCode;
	}

	/** Replies the charset.
	 *
	 * @return the charset or <code>null</code> if unknown
	 */
	@Pure
	public Charset getChatset() {
		if (this.charSet != null) {
			try {
				return Charset.forName(this.charSet);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable exception) {
				//
			}
		}
		return null;
	}

	/** Replies the code page which is corresponding to the given dBASE language code.
	 *
	 * @param code the language code.
	 * @return the code page, or <code>null</code> if not found.
	 */
	@Pure
	public static DBaseCodePage fromLanguageCode(byte code) {
		for (final DBaseCodePage cp : values()) {
			if (cp.languageCode == code) {
				return cp;
			}
		}
		return null;
	}

	/** Replies the code page which is corresponding to the given charset.
	 *
	 * @param charset the character set.
	 * @return the code page, or <code>null</code> if not found.
	 */
	@Pure
	public static DBaseCodePage fromCharset(Charset charset) {
		assert charset != null;
		for (final DBaseCodePage cp : values()) {
			if (cp.charSet != null && cp.charSet.equalsIgnoreCase(charset.name())) {
				return cp;
			}
		}
		return null;
	}

	/** Replies the code page which is corresponding to the given charset.
	 *
	 * @param charset the character set.
	 * @return the code page, or <code>null</code> if not found.
	 */
	@Pure
	public static DBaseCodePage fromCharset(String charset) {
		assert charset != null;
		for (final DBaseCodePage cp : values()) {
			if (cp.charSet != null && cp.charSet.equalsIgnoreCase(charset)) {
				return cp;
			}
		}
		return null;
	}

}

