/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.text;

import java.util.Base64;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * A RFC 1521 compliant Base64 Encoder and Decoder.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated {@link Base64}
 */
@Deprecated
public final class Base64Coder {

	// Mapping table from 6-bit nibbles to Base64 characters.
	@SuppressWarnings("checkstyle:magicnumber")
	private static char[] map1 = new char[64];

	// Mapping table from Base64 characters to 6-bit nibbles.
	@SuppressWarnings("checkstyle:magicnumber")
	private static byte[] map2 = new byte[128];

	static {
		int i = 0;
		for (char c = 'A'; c <= 'Z'; ++c) {
			map1[i++] = c;
		}
		for (char c = 'a'; c <= 'z'; ++c) {
			map1[i++] = c;
		}
		for (char c = '0'; c <= '9'; ++c) {
			map1[i++] = c;
		}
		map1[i++] = '+';
		map1[i++] = '/';
		for (i = 0; i < map2.length; ++i) {
			map2[i] = -1;
		}
		for (i = 0; i < map1.length; ++i) {
			map2[map1[i]] = (byte) i;
		}
	}

	private Base64Coder() {
		//
	}

	/**
	 * Encodes a string into Base64 format.
	 * No blanks or line breaks are inserted.
	 *
	 * @param string  a String to be encoded.
	 * @return   A String with the Base64 encoded data.
	 */
	@Pure
	@Inline(value = "new String(Base64Coder.encode(($1).getBytes()))", imported = {Base64Coder.class})
	public static String encodeString(String string) {
		return new String(encode(string.getBytes()));
	}

	/**
	 * Encodes a byte array into Base64 format.
	 * No blanks or line breaks are inserted.
	 *
	 * @param in is an array containing the data bytes to be encoded.
	 * @return a character array with the Base64 encoded data.
	 */
	@Pure
	@Inline(value = "Base64Coder.encode($1, ($1).length)", imported = {Base64Coder.class})
	public static char[] encode(byte[] in) {
		return encode(in, in.length);
	}

	/**
	 * Encodes a byte array into Base64 format.
	 * No blanks or line breaks are inserted.
	 * @param in   an array containing the data bytes to be encoded.
	 * @param ilen number of bytes to process in <code>in</code>.
	 * @return     A character array with the Base64 encoded data.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public static char[] encode(byte[] in, int ilen) {
		// output length without padding
		final int oDataLen = (ilen * 4 + 2) / 3;
		// output length including padding
		final int oLen = ((ilen + 2) / 3) * 4;
		final char[] out = new char[oLen];
		int ip = 0;
		int op = 0;
		while (ip < ilen) {
			final int i0 = in[ip++] & 0xff;
			final int i1 = ip < ilen ? in[ip++] & 0xff : 0;
			final int i2 = ip < ilen ? in[ip++] & 0xff : 0;
			final int o0 = i0 >>> 2;
			final int o1 = ((i0 &   3) << 4) | (i1 >>> 4);
			final int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			final int o3 = i2 & 0x3F;
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '=';
			++op;
			out[op] = op < oDataLen ? map1[o3] : '=';
			++op;
		}
		return out;
	}

	/**
	 * Decodes a string from Base64 format.
	 *
	 * @param string  a Base64 String to be decoded.
	 * @return   A String containing the decoded data.
	 * @throws   IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	@Pure
	@Inline(value = "new String(Base64Coder.decode($1))", imported = {Base64Coder.class})
	public static String decodeString(String string) {
		return new String(decode(string));
	}

	/**
	 * Decodes a byte array from Base64 format.
	 *
	 * @param string  a Base64 String to be decoded.
	 * @return   An array containing the decoded data bytes.
	 * @throws   IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	@Pure
	@Inline(value = "Base64Coder.decode(($1).toCharArray())", imported = {Base64Coder.class})
	public static byte[] decode(String string) {
		return decode(string.toCharArray());
	}

	/**
	 * Decodes a byte array from Base64 format.
	 * No blanks or line breaks are allowed within the Base64 encoded data.
	 * @param in  a character array containing the Base64 encoded data.
	 * @return    An array containing the decoded data bytes.
	 * @throws    IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public static byte[] decode(char[] in) {
		int ilen = in.length;
		if (ilen % 4 != 0) {
			throw new IllegalArgumentException(Locale.getString("INVALID_STRING_LENGTH")); //$NON-NLS-1$
		}
		while (ilen > 0 && in[ilen - 1] == '=') {
			--ilen;
		}
		final int olen = (ilen * 3) / 4;
		final byte[] out = new byte[olen];
		int ip = 0;
		int op = 0;
		while (ip < ilen) {
			final int i0 = in[ip++];
			final int i1 = in[ip++];
			final int i2 = ip < ilen ? in[ip++] : 'A';
			final int i3 = ip < ilen ? in[ip++] : 'A';
			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
				throw new IllegalArgumentException();
			}
			final int b0 = map2[i0];
			final int b1 = map2[i1];
			final int b2 = map2[i2];
			final int b3 = map2[i3];
			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
				throw new IllegalArgumentException(Locale.getString("ILLEGAL_CHARACTER")); //$NON-NLS-1$
			}
			final int o0 = (b0 << 2) | (b1 >>> 4);
			final int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
			final int o2 = ((b2 & 3) << 6) | b3;
			out[op++] = (byte) o0;
			if (op < olen) {
				out[op++] = (byte) o1;
			}
			if (op < olen) {
				out[op++] = (byte) o2;
			}
		}
		return out;
	}

}
