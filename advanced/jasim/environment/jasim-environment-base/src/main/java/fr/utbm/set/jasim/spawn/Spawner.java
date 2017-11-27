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
package fr.utbm.set.jasim.spawn;

import fr.utbm.set.geom.object.EuclidianDirection;
import fr.utbm.set.geom.object.EuclidianPoint;

/**
 * This interface describes objects that are able to create an
 * instance of an entity.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Spawner {
	
	/** Replies the budget of this spawner.
	 * <p>
	 * The budget is the maximal count of entities that this spawner
	 * is able to spawn.
	 * <p>
	 * The replied value could evolve during the time.
	 * 
	 * @return the available budget.
	 */
	public long getBudget();

	/** Replies the budget of this spawner was reached or not.
	 * <p>
	 * The budget is the maximal count of entities that this spawner
	 * is able to spawn.
	 * <p>
	 * The replied value could evolve during the time.
	 * 
	 * @return <code>true</code> if the bugdet was consumed, otherwise <code>false</code>.
	 */
	public boolean isBudgetConsumed();

	/** Spawn an entity.
	 * 
	 * @param spawningPoint is the point at which the entity must be spawn.
	 * @param direction is the allowed spawning direction(s). 
	 */
	public void spawn(EuclidianPoint spawningPoint, EuclidianDirection direction);
		
	/** Replies if this spawner enables to spawn at the given position.
	 * 
	 * @param position is the position to check.
	 * @return <code>true</code> if the given point is traversable according to this spawner,
	 * otherwise <code>false</code>
	 * @deprecated
	 */
	@Deprecated
	public boolean isTraversable(EuclidianPoint position);

}
