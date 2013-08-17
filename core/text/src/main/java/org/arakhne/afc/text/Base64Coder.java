/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 * 
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.text;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * A RFC 1521 compliant Base64 Encoder and Decoder.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Base64Coder {

	// Mapping table from 6-bit nibbles to Base64 characters.
	private static char[] map1 = new char[64];
	
	static {
		int i=0;
		for (char c='A'; c<='Z'; ++c) map1[i++] = c;
		for (char c='a'; c<='z'; ++c) map1[i++] = c;
		for (char c='0'; c<='9'; ++c) map1[i++] = c;
		map1[i++] = '+'; map1[i++] = '/';
	}

	// Mapping table from Base64 characters to 6-bit nibbles.
	private static byte[] map2 = new byte[128];
	
	static {
		for (int i=0; i<map2.length; ++i) map2[i] = -1;
		for (int i=0; i<64; ++i) map2[map1[i]] = (byte)i;
	}

	/**
	 * Encodes a string into Base64 format.
	 * No blanks or line breaks are inserted.
	 * @param s  a String to be encoded.
	 * @return   A String with the Base64 encoded data.
	 */
	public static String encodeString(String s) {
		return new String(encode(s.getBytes()));
	}

	/**
	 * Encodes a byte array into Base64 format.
	 * No blanks or line breaks are inserted.
	 * 
	 * @param in is an array containing the data bytes to be encoded.
	 * @return a character array with the Base64 encoded data.
	 */
	public static char[] encode(byte[] in) {
		return encode(in,in.length);
	}

	/**
	 * Encodes a byte array into Base64 format.
	 * No blanks or line breaks are inserted.
	 * @param in   an array containing the data bytes to be encoded.
	 * @param iLen number of bytes to process in <code>in</code>.
	 * @return     A character array with the Base64 encoded data.
	 */
	public static char[] encode(byte[] in, int iLen) {
		int oDataLen = (iLen*4+2)/3;       // output length without padding
		int oLen = ((iLen+2)/3)*4;         // output length including padding
		char[] out = new char[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen) {
			int i0 = in[ip++] & 0xff;
			int i1 = ip < iLen ? in[ip++] & 0xff : 0;
			int i2 = ip < iLen ? in[ip++] & 0xff : 0;
			int o0 = i0 >>> 2;
			int o1 = ((i0 &   3) << 4) | (i1 >>> 4);
			int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			int o3 = i2 & 0x3F;
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '='; ++op;
			out[op] = op < oDataLen ? map1[o3] : '='; ++op;
		}
		return out;
	}

	/**
	 * Decodes a string from Base64 format.
	 * @param s  a Base64 String to be decoded.
	 * @return   A String containing the decoded data.
	 * @throws   IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	public static String decodeString(String s) {
		return new String(decode(s));
	}

	/**
	 * Decodes a byte array from Base64 format.
	 * @param s  a Base64 String to be decoded.
	 * @return   An array containing the decoded data bytes.
	 * @throws   IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(String s) {
		return decode(s.toCharArray());
	}

	/**
	 * Decodes a byte array from Base64 format.
	 * No blanks or line breaks are allowed within the Base64 encoded data.
	 * @param in  a character array containing the Base64 encoded data.
	 * @return    An array containing the decoded data bytes.
	 * @throws    IllegalArgumentException if the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(char[] in) {
		int iLen = in.length;
		if (iLen%4 != 0)
			throw new IllegalArgumentException (Locale.getString("INVALID_STRING_LENGTH")); //$NON-NLS-1$
		while (iLen > 0 && in[iLen-1] == '=') --iLen;
		int oLen = (iLen*3) / 4;
		byte[] out = new byte[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen) {
			int i0 = in[ip++];
			int i1 = in[ip++];
			int i2 = ip < iLen ? in[ip++] : 'A';
			int i3 = ip < iLen ? in[ip++] : 'A';
			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
				throw new IllegalArgumentException ();
			int b0 = map2[i0];
			int b1 = map2[i1];
			int b2 = map2[i2];
			int b3 = map2[i3];
			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
				throw new IllegalArgumentException (Locale.getString("ILLEGAL_CHARACTER")); //$NON-NLS-1$
			int o0 = ( b0       <<2) | (b1>>>4);
			int o1 = ((b1 & 0xf)<<4) | (b2>>>2);
			int o2 = ((b2 &   3)<<6) |  b3;
			out[op++] = (byte)o0;
			if (op<oLen) out[op++] = (byte)o1;
			if (op<oLen) out[op++] = (byte)o2;
		}
		return out;
	}

}
