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
package org.arakhne.afc.math.tree.iterator;

/** Indicates when a node will be treated
 * in comparison to its child nodes.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum DepthFirstNodeOrder {
	
	/** The parent node will be treated before
	 * all its children. 
	 */
	PREFIX,
	
	/** The parent node will be treated after
	 * all its children. 
	 */
	POSTFIX,
	
	/** The parent node will be treated at the same
	 * time of its children.
	 * The "at the same time" notion is ambigous.
	 * The default behavior of the iterator assumes
	 * that the parent node will be treated in
	 * the middle of its child set.
	 */
	INFIX;
	
}
