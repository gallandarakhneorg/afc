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

import java.util.Comparator;


/** Comparator of world entities.
 *
 * @param <WE> is the type of the entities to compare.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WorldEntityComparator<WE extends WorldEntity<?>> implements Comparator<WE> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(WE o1, WE o2) {
		if (o1==o2) return 0;
		if (o1==null) return -1;
		if (o2==null) return 1;
		return o2.hashCode() - o1.hashCode();
	}


}