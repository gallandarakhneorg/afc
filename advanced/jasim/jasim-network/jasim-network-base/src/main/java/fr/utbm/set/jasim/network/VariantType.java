/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.network;

/**
 * Type of a JaSIM network message.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum VariantType {

	/** empty.
	 */
	NULL,

	/** Boolean.
	 */
	BOOLEAN,

	/** Byte.
	 */
	BYTE,
	
	/** Character, is similar to byte.
	 */
	CHARACTER,

	/** Integer.
	 */
	INTEGER,
	
	/** Floating point number.
	 */
	FLOAT,
	
	/** 3D point (x,y,z).
	 */
	POINT,
	
	/** 3D vector (x,y,z).
	 */
	VECTOR,
	
	/** Quaternion (x,y,z,w).
	 */
	QUATERNION,
	
	/** Array of boolean values.
	 */
	BOOLEAN_ARRAY,

	/** Array of bytes.
	 */
	BYTE_ARRAY,
	
	/** Array of characters.
	 */
	CHARACTER_ARRAY,

	/** Array of integers.
	 */
	INTEGER_ARRAY,
	
	/** Array of floating-point numbers.
	 */
	FLOAT_ARRAY,
	
	/** String.
	 */
	STRING,
	
	/** Unique identifier.
	 */
	UUID,

	/** URL.
	 */
	URL;

	/** Replies the variant type which is corresponding to the given
	 * byte value.
	 * 
	 * @param value
	 * @return the type or <code>null</code>
	 */
	public static VariantType fromByteValue(byte value) {
		VariantType[] values = values();
		if (value<0||value>=values.length) return null;
		return values[value];
	}
	
}

