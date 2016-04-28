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

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Define a mathematic function.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface MathFunction {
	
	/** Replies the value of the function.
	 * 
	 * @param x
	 * @return the value of {@code f(x)}.
	 * @throws MathException in case {@code f(x)} could not be computed
	 */
	@Pure
	public double f(double x)  throws MathException;
	
	/** Replies the range of the function.
	 * 
	 * @return a array of ranges. It corresponds to a list of couples that defined the set of valid values.
	 */
	@Pure
	public MathFunctionRange[] getRange();

}