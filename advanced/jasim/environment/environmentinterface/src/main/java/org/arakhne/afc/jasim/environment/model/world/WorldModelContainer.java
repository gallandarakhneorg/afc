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
 * This interface represents the data structures where 
 * world entities are stored, ie the world model.
 * 
 * @param <SE> is the type of the static entities
 * @param <ME> is the type of the mobile entities
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface WorldModelContainer<SE extends WorldEntity<?>, ME extends MobileEntity<?>> {
	
	/** Replies an iterator on the static entities.
	 * 
	 * @return an iterator, or <code>null</code>.
	 */
	public Iterator<? extends SE> getStaticEntities();
	
	/** Replies an iterator on the mobile entities.
	 * 
	 * @return an iterator.
	 */
	public Iterator<? extends ME> getMobileEntities();

	/** Replies the raw implementation of the world model.
	 * <p>
	 * The raw implementation of the world depends on the
	 * world dimension (graph, plane...).
	 * 
	 * @param <WM> is the type of the raw implementation to reply.
	 * @param type is the type of the raw implementation to reply.
	 * @return the world model of the given type or <code>null</code>.
	 */
	public <WM> WM getInnerWorldModel(Class<WM> type);
	
}
