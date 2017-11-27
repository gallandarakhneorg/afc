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
package fr.utbm.set.jasim.generation.builder;

/**
 * Type of picture for heightmap
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum HeightmapPictureType {

	/** Only red component set and blue and green will be set to (0,0) vector, ie (127,127).
	 */
	RED_ZERO_VECTOR,
	
	/** Only red component set and blue and green will be set to (0,0) colors.
	 */
	RED_ZERO_PIXEL,

	/** The three components will be set with same values.
	 */
	GRAYSCALE;
	
	@Override
	public String toString() {
		switch(this) {
		case GRAYSCALE:
			return "Grayscale"; //$NON-NLS-1$
		case RED_ZERO_PIXEL:
			return "{red,0,0}"; //$NON-NLS-1$
		case RED_ZERO_VECTOR:
			return "{red,127,127}"; //$NON-NLS-1$
		default:
		}
		return super.toString();
	}
	
}
