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
package org.arakhne.afc.math;

/**
 * Math exception.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.stochastic.MathException}
 */
@Deprecated
public class MathException extends org.arakhne.afc.math.stochastic.MathException {
	
	private static final long serialVersionUID = -3499177554318732766L;

	/**
	 * Create an exception without a mesage.
	 */
	public MathException() {
		super();
	}
	
	/**
	 * @param message is the message of the exception
	 */
	public MathException(String message) {
		super(message);
	}

}