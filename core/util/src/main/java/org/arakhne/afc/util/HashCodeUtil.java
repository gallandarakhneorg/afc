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

package org.arakhne.afc.util;

import java.lang.ref.Reference;
import java.util.Arrays;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/** Utilities to compute hash codes.
 *
 * <p>The utility class {@link Arrays} privides several
 * functions to compute hash codes from arrays.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see Arrays
 */
public final class HashCodeUtil {

	private static final int HASH_VALUE = 31;

	private HashCodeUtil() {
		//
	}

	/** Replies an initialized hash code.
	 *
	 * @return an initialized hash code.
	 * @deprecated since 13.0, no replacement.
	 */
	@Pure
	@Inline("1")
	@Deprecated
	public static int iddleHash() {
		return 1;
	}

	/** Compute a new hash code from the given value.
	 * If the value is {@link Reference}, the value
	 * is dereferenced while it is a {@link Reference}.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Object#hashCode()
	 */
	@Pure
	public static int hash(int oldHash, Object value) {
		final int hc;
		if (value instanceof Reference<?>) {
			final Object referenced = ((Reference<?>) value).get();
			hc = (referenced == null) ? 0 : referenced.hashCode();
		} else {
			hc = (value == null) ? 0 : value.hashCode();
		}
		return add(oldHash, hc);
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Boolean#hashCode()
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Boolean.valueOf($2).hashCode())",
			imported = {HashCodeUtil.class})
	public static int hash(int oldHash, boolean value) {
		return add(oldHash, Boolean.valueOf(value).hashCode());
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Character#hashCode()
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Character.valueOf($2).hashCode())",
			imported = {HashCodeUtil.class})
	public static int hash(int oldHash, char value) {
		return add(oldHash, Character.valueOf(value).hashCode());
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Byte#hashCode()
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Byte.valueOf($2).hashCode())",
			imported = {HashCodeUtil.class})
	public static int hash(int oldHash, byte value) {
		return add(oldHash, Byte.valueOf(value).hashCode());
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Short#hashCode()
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Short.valueOf($2).hashCode())",
			imported = {HashCodeUtil.class})
	public static int hash(int oldHash, short value) {
		return add(oldHash, Short.valueOf(value).hashCode());
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Integer#hashCode()
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Integer.valueOf($2).hashCode())",
			imported = {HashCodeUtil.class})
	public static int hash(int oldHash, int value) {
		return add(oldHash, Integer.valueOf(value).hashCode());
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Long#hashCode()
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Long.valueOf($2).hashCode())",
			imported = {HashCodeUtil.class})
	public static int hash(int oldHash, long value) {
		return add(oldHash, Long.valueOf(value).hashCode());
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Float#hashCode()
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Float.valueOf($2).hashCode())",
			imported = {HashCodeUtil.class})
	public static int hash(int oldHash, float value) {
		return add(oldHash, Float.valueOf(value).hashCode());
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Double#hashCode()
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Double.valueOf($2).hashCode())",
			imported = {HashCodeUtil.class})
	public static int hash(int oldHash, double value) {
		return add(oldHash, Double.valueOf(value).hashCode());
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Arrays#hashCode(boolean[])
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Arrays.hashCode($2))",
			imported = {HashCodeUtil.class, Arrays.class})
	public static int hash(int oldHash, boolean[] value) {
		return add(oldHash, Arrays.hashCode(value));
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Arrays#hashCode(char[])
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Arrays.hashCode($2))",
			imported = {HashCodeUtil.class, Arrays.class})
	public static int hash(int oldHash, char[] value) {
		return add(oldHash, Arrays.hashCode(value));
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Arrays#hashCode(byte[])
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Arrays.hashCode($2))",
			imported = {HashCodeUtil.class, Arrays.class})
	public static int hash(int oldHash, byte[] value) {
		return add(oldHash, Arrays.hashCode(value));
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Arrays#hashCode(short[])
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Arrays.hashCode($2))",
			imported = {HashCodeUtil.class, Arrays.class})
	public static int hash(int oldHash, short[] value) {
		return add(oldHash, Arrays.hashCode(value));
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Arrays#hashCode(int[])
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Arrays.hashCode($2))",
			imported = {HashCodeUtil.class, Arrays.class})
	public static int hash(int oldHash, int[] value) {
		return add(oldHash, Arrays.hashCode(value));
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Arrays#hashCode(long[])
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Arrays.hashCode($2))",
			imported = {HashCodeUtil.class, Arrays.class})
	public static int hash(int oldHash, long[] value) {
		return add(oldHash, Arrays.hashCode(value));
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Arrays#hashCode(double[])
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Arrays.hashCode($2))",
			imported = {HashCodeUtil.class, Arrays.class})
	public static int hash(int oldHash, float[] value) {
		return add(oldHash, Arrays.hashCode(value));
	}

	/** Compute a new hash code from the given primitive value.
	 *
	 * @param oldHash is the old hash code.
	 * @param value is the value from which the hash code may be updated.
	 * @return an new hash code.
	 * @see Arrays#hashCode(double[])
	 */
	@Pure
	@Inline(value = "HashCodeUtil.add(($1), Arrays.hashCode($2))",
			imported = {HashCodeUtil.class, Arrays.class})
	public static int hash(int oldHash, double[] value) {
		return add(oldHash, Arrays.hashCode(value));
	}

	/** Compute the hash code from the given parameters.
	 *
	 * <p>This function starts from value <code>1</code> and
	 * applies <code>h = h * 31 + a.hashCode()</code> for
	 * each non-null attribute.
	 *
	 * @param attributes the attributes for each the hash code must be computed.
	 * @return the hash code from the given parameters.
	 * @deprecated since 13.0, see {@link Objects#hash(Object...)}
	 */
	@Pure
	@Inline("Objects.hashCode($1)")
	@Deprecated
	public static int hash(Object... attributes) {
		int hash = iddleHash();
		for (final Object o : attributes) {
			hash = hash(hash, o);
		}
	    return hash;
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Byte#hashCode()
	 * @deprecated since 13.0, see {@link Byte#hashCode(byte)}
	 */
	@Pure
	@Inline(value = "Byte.hashCode($1)")
	@Deprecated
	public static int hash(byte value) {
		return Byte.valueOf(value).hashCode();
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Short#hashCode()
	 * @deprecated since 13.0, see {@link Short#hashCode(short)}
	 */
	@Pure
	@Inline(value = "Short.hashCode($1)")
	@Deprecated
	public static int hash(short value) {
		return Short.valueOf(value).hashCode();
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Long#hashCode()
	 * @deprecated since 13.0, see {@link Long#hashCode(long)}
	 */
	@Pure
	@Inline(value = "Long.hashCode($1)")
	@Deprecated
	public static int hash(long value) {
		return Long.valueOf(value).intValue();
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Float#hashCode()
	 * @deprecated since 13.0, see {@link Float#hashCode(float)}
	 */
	@Pure
	@Inline(value = "Float.hashCode($1)")
	@Deprecated
	public static int hash(float value) {
		return Float.valueOf(value).hashCode();
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Double#hashCode()
	 * @deprecated since 13.0, see {@link Double#hashCode(double)}
	 */
	@Pure
	@Inline(value = "Double.hashCode($1)")
	@Deprecated
	public static int hash(double value) {
		return Double.valueOf(value).hashCode();
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Character#hashCode()
	 * @deprecated since 13.0, see {@link Character#hashCode(char)}
	 */
	@Pure
	@Inline(value = "Character.hashCode($1)")
	@Deprecated
	public static int hash(char value) {
		return Character.valueOf(value).hashCode();
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Boolean#hashCode()
	 * @deprecated since 13.0, see {@link Boolean#hashCode(boolean)}
	 */
	@Pure
	@Inline(value = "Boolean.hashCode($1)")
	@Deprecated
	public static int hash(boolean value) {
		return Boolean.valueOf(value).hashCode();
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(byte[])
	 * @deprecated since 13.0, see {@link Arrays#hashCode(byte[])}
	 */
	@Pure
	@Inline(value = "Arrays.hashCode($1)")
	@Deprecated
	public static int hash(byte[] value) {
		return Arrays.hashCode(value);
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(short[])
	 * @deprecated since 13.0, see {@link Arrays#hashCode(short[])}
	 */
	@Pure
	@Inline(value = "Arrays.hashCode($1)")
	@Deprecated
	public static int hash(short[] value) {
		return Arrays.hashCode(value);
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(long[])
	 * @deprecated since 13.0, see {@link Arrays#hashCode(long[])}
	 */
	@Pure
	@Inline(value = "Arrays.hashCode($1)")
	@Deprecated
	public static int hash(long[] value) {
		return Arrays.hashCode(value);
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(float[])
	 * @deprecated since 13.0, see {@link Arrays#hashCode(float[])}
	 */
	@Pure
	@Inline(value = "Arrays.hashCode($1)")
	@Deprecated
	public static int hash(float[] value) {
		return Arrays.hashCode(value);
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(double[])
	 * @deprecated since 13.0, see {@link Arrays#hashCode(double[])}
	 */
	@Pure
	@Inline(value = "Arrays.hashCode($1)")
	@Deprecated
	public static int hash(double[] value) {
		return Arrays.hashCode(value);
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(char[])
	 * @deprecated since 13.0, see {@link Arrays#hashCode(char[])}
	 */
	@Pure
	@Inline(value = "Arrays.hashCode($1)")
	@Deprecated
	public static int hash(char[] value) {
		return Arrays.hashCode(value);
	}

	/** Compute the hash code from the given primitive value.
	 *
	 * @param value the value.
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(boolean[])
	 * @deprecated since 13.0, see {@link Arrays#hashCode(boolean[])}
	 */
	@Pure
	@Inline(value = "Arrays.hashCode($1)")
	@Deprecated
	public static int hash(boolean[] value) {
		return Arrays.hashCode(value);
	}

	/** Compute a new hash code from the old hash code and the hash code of a value.
	 *
	 * @param oldHash is the old hash code.
	 * @param valueHashCode is the hash code of the value from which the hash code may be updated.
	 * @return an new hash code.
	 */
	@Pure
	public static int add(int oldHash, int valueHashCode) {
		return oldHash * HASH_VALUE + valueHashCode;
	}

}
