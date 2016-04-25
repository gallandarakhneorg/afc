/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.stochastic;


/**
 * Define a mathematic inverse function.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface MathInversableFunction extends MathFunction {
	
	/** Replies the value of the inverse function.
	 * 
	 * @param y
	 * @return the value of {@code f<sup>-1</sup>(y)}.
	 * @throws MathException in case {@code f<sup>-1</sup>(y)} could not be computed
	 */
	public double inverseF(double y)  throws MathException;

}