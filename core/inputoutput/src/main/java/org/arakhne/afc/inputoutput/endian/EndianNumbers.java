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

package org.arakhne.afc.inputoutput.endian;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Utility methods for Little Endian Number coding.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:booleanexpressioncomplexity"})
public final class EndianNumbers {

	private EndianNumbers() {
		//
	}

	/**
	 * Converting two bytes to a Big Endian short.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @return the conversion result.
	 */
	@Pure
	public static short toBEShort(int b1, int b2) {
        return (short) (((b1 & 0xFF) << 8) + (b2 & 0xFF));
    }

	/**
	 * Converting two bytes to a Little Endian short.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @return the conversion result
	 */
	@Pure
	public static short toLEShort(int b1, int b2) {
        return (short) (((b2 & 0xFF) << 8) + (b1 & 0xFF));
    }

	/**
	 * Converting four bytes to a Little Endian integer.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @param b3 the third byte.
	 * @param b4 the fourth byte.
	 * @return the conversion result
	 */
	@Pure
	public static int toLEInt(int b1, int b2, int b3, int b4) {
        return ((b4 & 0xFF) << 24) + ((b3 & 0xFF) << 16) + ((b2 & 0xFF) << 8) + (b1 & 0xFF);
    }

	/**
	 * Converting four bytes to a Big Endian integer.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @param b3 the third byte.
	 * @param b4 the fourth byte.
	 * @return the conversion result
	 */
	@Pure
	public static int toBEInt(int b1, int b2, int b3, int b4) {
        return ((b1 & 0xFF) << 24) + ((b2 & 0xFF) << 16) + ((b3 & 0xFF) << 8) + (b4 & 0xFF);
    }

	/**
	 * Converting eight bytes to a Little Endian integer.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @param b3 the third byte.
	 * @param b4 the fourth byte.
	 * @param b5 the fifth byte.
	 * @param b6 the sixth byte.
	 * @param b7 the seventh byte.
	 * @param b8 the eighth byte.
	 * @return the conversion result
	 */
	@Pure
	public static long toLELong(int b1, int b2, int b3, int b4, int b5, int b6, int b7, int b8) {
	    return ((b8 & 0xFF) << 56) + ((b7 & 0xFF) << 48) + ((b6 & 0xFF) << 40) + ((b5 & 0xFF) << 32)
                + ((b4 & 0xFF) << 24) + ((b3 & 0xFF) << 16) + ((b2 & 0xFF) << 8) + (b1 & 0xFF);
    }

	/**
	 * Converting eight bytes to a Big Endian integer.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @param b3 the third byte.
	 * @param b4 the fourth byte.
	 * @param b5 the fifth byte.
	 * @param b6 the sixth byte.
	 * @param b7 the seventh byte.
	 * @param b8 the eighth byte.
	 * @return the conversion result
	 */
	@Pure
	public static long toBELong(int b1, int b2, int b3, int b4, int b5, int b6, int b7, int b8) {
        return ((b1 & 0xFF) << 56) + ((b2 & 0xFF) << 48) + ((b3 & 0xFF) << 40) + ((b4 & 0xFF) << 32)
                + ((b5 & 0xFF) << 24) + ((b6 & 0xFF) << 16) + ((b7 & 0xFF) << 8) + (b8 & 0xFF);
    }

	/**
	 * Converting eight bytes to a Little Endian double.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @param b3 the third byte.
	 * @param b4 the fourth byte.
	 * @param b5 the fifth byte.
	 * @param b6 the sixth byte.
	 * @param b7 the seventh byte.
	 * @param b8 the eighth byte.
	 * @return the conversion result
	 */
	@Pure
	@Inline(value = "Double.longBitsToDouble(EndianNumbers.toLELong($1, $2, $3, $4, $5, $6, $7, $8))",
			imported = {EndianNumbers.class})
	public static double toLEDouble(int b1, int b2, int b3, int b4, int b5, int b6, int b7, int b8) {
		return Double.longBitsToDouble(toLELong(b1, b2, b3, b4, b5, b6, b7, b8));
	}

	/**
	 * Converting eight bytes to a Big Endian double.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @param b3 the third byte.
	 * @param b4 the fourth byte.
	 * @param b5 the fifth byte.
	 * @param b6 the sixth byte.
	 * @param b7 the seventh byte.
	 * @param b8 the eighth byte.
	 * @return the conversion result
	 */
	@Pure
	@Inline(value = "Double.longBitsToDouble(EndianNumbers.toBELong($1, $2, $3, $4, $5, $6, $7, $8))",
			imported = {EndianNumbers.class})
	public static double toBEDouble(int b1, int b2, int b3, int b4, int b5, int b6, int b7, int b8) {
		return Double.longBitsToDouble(toBELong(b1, b2, b3, b4, b5, b6, b7, b8));
	}

	/**
	 * Converting four bytes to a Little Endian float.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @param b3 the third byte.
	 * @param b4 the fourth byte.
	 * @return the conversion result
	 */
	@Pure
	@Inline(value = "Float.intBitsToFloat(EndianNumbers.toLEInt($1, $2, $3, $4))",
			imported = {EndianNumbers.class})
	public static float toLEFloat(int b1, int b2, int b3, int b4) {
		return Float.intBitsToFloat(toLEInt(b1, b2, b3, b4));
	}

	/**
	 * Converting four bytes to a Big Endian float.
	 *
	 * @param b1 the first byte.
	 * @param b2 the second byte.
	 * @param b3 the third byte.
	 * @param b4 the fourth byte.
	 * @return the conversion result
	 */
	@Pure
	@Inline(value = "Float.intBitsToFloat(EndianNumbers.toBEInt($1, $2, $3, $4))",
			imported = {EndianNumbers.class})
	public static float toBEFloat(int b1, int b2, int b3, int b4) {
		return Float.intBitsToFloat(toBEInt(b1, b2, b3, b4));
	}


	/**
	 * Converts a Java int to a Little Endian int on 2 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	public static byte[] parseLEShort(short value) {
		return new byte[] {
			(byte) (value & 0xFF),
			(byte) ((value >> 8) & 0xFF),
		};
    }

	/**
	 * Converts a Java int to a Little Endian int on 4 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	public static byte[] parseLEInt(int value) {
		return new byte[] {
			(byte) (value & 0xFF),
			(byte) ((value >> 8) & 0xFF),
			(byte) ((value >> 16) & 0xFF),
			(byte) ((value >> 24) & 0xFF),
		};
    }

	/**
	 * Converts a Java float to a Little Endian int on 4 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	@Inline(value = "EndianNumbers.parseLEInt(Float.floatToIntBits($1))",
			imported = {EndianNumbers.class})
	public static byte[] parseLEFloat(float value) {
		return parseLEInt(Float.floatToIntBits(value));
    }


	/**
	 * Converts a Java long to a Little Endian int on 8 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	public static byte[] parseLELong(long value) {
		return new byte[] {
			(byte) (value & 0xFF),
			(byte) ((value >> 8) & 0xFF),
			(byte) ((value >> 16) & 0xFF),
			(byte) ((value >> 24) & 0xFF),
			(byte) ((value >> 32) & 0xFF),
			(byte) ((value >> 40) & 0xFF),
			(byte) ((value >> 48) & 0xFF),
			(byte) ((value >> 56) & 0xFF),
		};
    }

	/**
	 * Converts a Java double to a Little Endian int on 8 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	@Inline(value = "EndianNumbers.parseLELong(Double.doubleToLongBits($1))",
			imported = {EndianNumbers.class})
	public static byte[] parseLEDouble(double value) {
		return parseLELong(Double.doubleToLongBits(value));
    }

	/**
	 * Converts a Java int to a Big Endian int on 2 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	public static byte[] parseBEShort(short value) {
		return new byte[] {
			(byte) ((value >> 8) & 0xFF),
			(byte) (value & 0xFF),
		};
    }

	/**
	 * Converts a Java int to a Big Endian int on 4 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	public static byte[] parseBEInt(int value) {
		return new byte[] {
			(byte) ((value >> 24) & 0xFF),
			(byte) ((value >> 16) & 0xFF),
			(byte) ((value >> 8) & 0xFF),
			(byte) (value & 0xFF),
		};
    }

	/**
	 * Converts a Java float to a Big Endian int on 4 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	@Inline(value = "EndianNumbers.parseBEInt(Float.floatToIntBits($1))",
			imported = {EndianNumbers.class})
	public static byte[] parseBEFloat(float value) {
		return parseBEInt(Float.floatToIntBits(value));
    }


	/**
	 * Converts a Java long to a Big Endian int on 8 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	public static byte[] parseBELong(long value) {
		return new byte[] {
			(byte) ((value >> 56) & 0xFF),
			(byte) ((value >> 48) & 0xFF),
			(byte) ((value >> 40) & 0xFF),
			(byte) ((value >> 32) & 0xFF),
			(byte) ((value >> 24) & 0xFF),
			(byte) ((value >> 16) & 0xFF),
			(byte) ((value >> 8) & 0xFF),
			(byte) (value & 0xFF),
		};
    }

	/**
	 * Converts a Java double to a Big Endian int on 8 bytes.
	 *
	 * @param value is the value to parse.
	 * @return the parsing result
	 */
	@Pure
	@Inline(value = "EndianNumbers.parseBELong(Double.doubleToLongBits($1))",
			imported = {EndianNumbers.class})
	public static byte[] parseBEDouble(double value) {
		return parseBELong(Double.doubleToLongBits(value));
    }

}
