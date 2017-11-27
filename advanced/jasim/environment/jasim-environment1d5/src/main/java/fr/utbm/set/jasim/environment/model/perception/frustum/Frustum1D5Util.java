/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.environment.model.perception.frustum;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.intersection.IntersectionType;

/**
 * Utility class for 1D5 frustums.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class Frustum1D5Util {

	private Frustum1D5Util() {
		//
	}

	/** Classify two bounds (projected on the same axis).
	 * 
	 * @param boundLower
	 * @param boundUpper
	 * @param otherLower
	 * @param otherUpper
	 * @return the classification.
	 */
	public static IntersectionType classifiesOnDirection(double boundLower, double boundUpper, double otherLower, double otherUpper) {
		if (GeometryUtil.epsilonCompareTo(otherLower, boundLower)<0) {
			if (GeometryUtil.epsilonCompareTo(otherUpper, boundLower)<0) 
				return IntersectionType.OUTSIDE;
			if (GeometryUtil.epsilonCompareTo(otherUpper, boundUpper)<=0)
				return IntersectionType.SPANNING;
			return IntersectionType.ENCLOSING;
		}

		if (GeometryUtil.epsilonCompareTo(otherLower, boundUpper)<=0) {
			if (GeometryUtil.epsilonCompareTo(otherUpper, boundUpper)<=0)
				return IntersectionType.INSIDE;
			return IntersectionType.SPANNING;
		}
		
		return IntersectionType.OUTSIDE;		
	}

}
