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
package org.arakhne.afc.jasim.environment.semantics;

import java.util.Collection;

/** This interfaces describes a provider owning semantics.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Semanticable {

	/** Replies the associated semantics excepts the type of the entity.
	 * 
	 * @return the list of semantics.
	 * @see #getType()
	 */
	public Collection<? extends Semantic> getAllSemantics();
	
	/** Replies the main semantic associated to this entity: its type.
	 * 
	 * @return the type of the entity.
	 * @see #getAllSemantics()
	 */
	public Semantic getType();

}