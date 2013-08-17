/* 
 * $Id$
 * 
 * Copyright (c) 2005-10 Multiagent Team, Laboratoire Systemes et Transports,
 *                       Universite de Technologie de Belfort-Montbeliard.
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
package org.arakhne.afc.util;

import java.lang.ref.Reference;
import java.util.Arrays;

/** Utilities to compute hash codes.
 * <p>
 * The utility class {@link Arrays} privides several
 * functions to compute hash codes from arrays.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see Arrays
 */
public class HashCodeUtil {

	private static final int HASH_VALUE = 31;
	
	/** Replies an initialized hash code.
	 * 
	 * @return an initialized hash code.
	 */
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
	public static int hash(int oldHash, Object value) {
		int hc;
		if (value instanceof Reference<?>) {
			Object referenced = ((Reference<?>)value).get();
			hc = (referenced==null) ? 0 : referenced.hashCode();
		}
		else {
			hc = ((value==null) ? 0 : value.hashCode());
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
	public static int hash(int oldHash, double[] value) {
		return add(oldHash, Arrays.hashCode(value));
	}
	
	/** Compute a new hash code from the old hash code and the hash code of a value.
	 * 
	 * @param oldHash is the old hash code.
	 * @param valueHashCode is the hash code of the value from which the hash code may be updated.
	 * @return an new hash code.
	 */
	public static int add(int oldHash, int valueHashCode) {
		return oldHash * HASH_VALUE + valueHashCode;
	}

	/** Compute the hash code from the given parameters.
	 * <p>
	 * This function starts from value <code>1</code> and
	 * applies <code>h = h * 31 + a.hashCode()</code> for
	 * each non-null attribute.
	 * 
	 * @param attributes
	 * @return the hash code from the given parameters.
	 */
	public static int hash(Object... attributes) {
		int hash = iddleHash();
		for(Object o : attributes) {
			hash = hash(hash, o);
		}
	    return hash;    	
	}
	
	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Byte#hashCode()
	 */
	public static int hash(byte value) {
		return Byte.valueOf(value).hashCode();    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Short#hashCode()
	 */
	public static int hash(short value) {
		return Short.valueOf(value).hashCode();    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Long#hashCode()
	 */
	public static int hash(long value) {
		return Long.valueOf(value).intValue();    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Float#hashCode()
	 */
	public static int hash(float value) {
		return Float.valueOf(value).hashCode();    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Double#hashCode()
	 */
	public static int hash(double value) {
		return Double.valueOf(value).hashCode();    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Character#hashCode()
	 */
	public static int hash(char value) {
		return Character.valueOf(value).hashCode();    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Boolean#hashCode()
	 */
	public static int hash(boolean value) {
		return Boolean.valueOf(value).hashCode();    	
	}
	
	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(byte[])
	 */
	public static int hash(byte[] value) {
		return Arrays.hashCode(value);    
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(short[])
	 */
	public static int hash(short[] value) {
		return Arrays.hashCode(value);    
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(long[])
	 */
	public static int hash(long[] value) {
		return Arrays.hashCode(value);    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(float[])
	 */
	public static int hash(float[] value) {
		return Arrays.hashCode(value);    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(double[])
	 */
	public static int hash(double[] value) {
		return Arrays.hashCode(value);    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(char[])
	 */
	public static int hash(char[] value) {
		return Arrays.hashCode(value);    	
	}

	/** Compute the hash code from the given primitive value.
	 * 
	 * @param value
	 * @return the hash code from the given primitive value.
	 * @see Arrays#hashCode(boolean[])
	 */
	public static int hash(boolean[] value) {
		return Arrays.hashCode(value);    	
	}

}