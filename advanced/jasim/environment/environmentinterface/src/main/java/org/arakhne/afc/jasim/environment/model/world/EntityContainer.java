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
package org.arakhne.afc.jasim.environment.model.world;

import java.util.Iterator;


/**
 * This interface represents a container of entities.
 * 
 * @param <E> is the type of the entities
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface EntityContainer<E extends WorldEntity<?>> {
	
	/** Replies an iterator on the entities.
	 * 
	 * @return an iterator, never <code>null</code>.
	 */
	public Iterator<? extends E> entityIterator();
		
}
