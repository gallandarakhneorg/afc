/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
 * Laboratoire Systemes et Transport,
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
package org.arakhne.afc.math.physics;

import junit.framework.AssertionFailedError;

/**
 * Assertion that owns additional logs on the local variables values.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class LogAssertion extends AssertionFailedError {

	private static final long serialVersionUID = -7322813548944851770L;

	private static String makeMessage(String mesg, Object... pairs) {
		StringBuilder str = new StringBuilder();
		if (mesg!=null) {
			str.append(mesg);
			str.append("\n"); //$NON-NLS-1$
		}
		for(int i=0; i<pairs.length-1; i+=2) {
			str.append(pairs[i]!=null ? pairs[i].toString() : "?"); //$NON-NLS-1$
			str.append("=\""); //$NON-NLS-1$
			str.append(pairs[i+1]!=null ? pairs[i+1].toString() : "?"); //$NON-NLS-1$
			str.append("\"\n"); //$NON-NLS-1$
		}
		return str.toString();
	}
	
	/**
	 * @param message
	 * @param variableValuePairs contains the pairs of names and values.
	 */
	public LogAssertion(String message, Object... variableValuePairs) {
		super(makeMessage(message, variableValuePairs));
	}
	
	/**
	 * @param message
	 * @param e is the exception that causes the problem.
	 * @param variableValuePairs contains the pairs of names and values.
	 */
	public LogAssertion(String message, Throwable e, Object... variableValuePairs) {
		super(makeMessage(message, variableValuePairs));
		initCause(e);
		setStackTrace(e.getStackTrace());
	}

}
