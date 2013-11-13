/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
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
package org.arakhne.afc.math.tree.builder;

/**
 * An exception during the tree building. 
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TreeBuilderException extends Exception {

	private static final long serialVersionUID = -4301432338147841097L;
	
	/**
	 * This exception was thrown when the bounding box of the
	 * entities that must be insert inside a tree was empty. 
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class EmptyBoundingBoxException extends TreeBuilderException {

		private static final long serialVersionUID = 682140893184447598L;
		
	}

	/**
	 * This exception was thrown when the building function was
	 * called twice times on the same builder. 
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static class BuilderAlreadyStartedException extends TreeBuilderException {

		private static final long serialVersionUID = -8430835745057032650L;

	}

}