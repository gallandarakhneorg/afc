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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Logger;

import fr.utbm.set.geom.object.EuclidianDirection;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.jasim.environment.time.Clock;

/**
 * Spawn entities in the environment.
 * 
 * @param <P> is the type of the supported euclidian points.
 * @param <D> is the type of the supported directions.
 * @param <DC> is the type of the direction constraints.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSpawnLocation<P extends EuclidianPoint,
											D extends EuclidianDirection,DC>
implements SpawnLocation {
	
	/** Default time out when trying to generate an entity.
	 * This timeout is reachable when no enough space is free for spawning.
	 */
	protected static final int SPACE_TIMEOUT = 10;
	
	private final UUID id;
	private final String name;
	
	/** Default uniform random number generator.
	 */
	protected final Random rnd = new Random();
	
	/** List of laws.
	 */
	private final Map<Spawner,SpawningLaw> laws = new HashMap<Spawner, SpawningLaw>();
	
	/** This is the date at which the spawn point is started to spawn entities.
	 */
	private final double spawnStartDate;
	
	/** This is the date at which the spawn point is finishing to spawn entities.
	 */
	private final double spawnEndDate;

	/** Reference point.
	 */
	private final P referencePoint;

	/** Reference direction.
	 */
	private final DC referenceDirection;
	
	/**
	 * @param identifier is the identifier of the spawning location.
	 * @param name is the name of the spawning location.
	 * @param position is the reference position of the spawning location.
	 * @param direction describes the allowed spawning direction(s).
	 * @param startDate is the date at which the spawn point is starting to spawn entities.
	 * @param endDate is the date at which the spawn point is finishing to spawn entities.
	 */
	public AbstractSpawnLocation(UUID identifier, String name, P position, DC direction, double startDate, double endDate) {
		assert(identifier!=null);
		this.id = identifier;
		this.name = name;
		this.spawnStartDate = (startDate<endDate) ? startDate : endDate;
		this.spawnEndDate = (startDate<endDate) ? endDate : startDate;
		this.referenceDirection = direction;
		this.referencePoint = position;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final UUID getIdentifier() {
		return this.id;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return this.name;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final P getReferencePoint() {
		return this.referencePoint;
	}

	/** Replies the spawning direction.
	 * @return the spawning direction.
	 */
	public final DC getReferenceDirection() {
		return this.referenceDirection;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getStartDate() {
		return this.spawnStartDate;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double getEndDate() {
		return this.spawnEndDate;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addSpawningLaw(Spawner instanciator, SpawningLaw law) {
		assert(instanciator!=null);
		assert(law!=null);
		this.laws.put(instanciator, law);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void removeSpawningLaw(Spawner instanciator) {
		assert(instanciator!=null);
		this.laws.remove(instanciator);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final boolean spawn(Clock simulationTime) {
		boolean spawned = false;
		double time = simulationTime.getSimulationTime();
		if ((time>=this.spawnStartDate)&&(time<=this.spawnEndDate)) {			
			SpawningLaw law;
			long realSpawned;
			long  count, budget;
			Spawner spawner;
			for(Entry<Spawner,SpawningLaw> entry : this.laws.entrySet()) {
				spawner = entry.getKey();
				if (!spawner.isBudgetConsumed()) {
					budget = spawner.getBudget();
					law = entry.getValue();
					count = law.getSpawnableAmount(simulationTime);
					if (count>budget) count = budget;
					realSpawned = 0;
					for(long idx=0; idx<count; ++idx) {
						if (spawn(spawner,idx,realSpawned,count)) ++realSpawned;
					}
					spawned |= realSpawned>0;
				}
			}
		}
		return spawned;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Collection<Spawner> getSpawnerList() {
		return Collections.unmodifiableCollection(this.laws.keySet());
	}
	
	/** Select randomly a direction.
	 * 
	 * @return the allowed spawning direction. 
	 */
	protected abstract D randomDirection();

	/** Replies the spawning position of an agent.
	 * 
	 * @param currentIdx is the current index of the spawned agent.
	 * @param spawnedEntities is the count of already spawned entities from the current spawning list.
	 * @param spawnableEntities is the count of entities from the current spawning list.
	 * @return a random position or <code>null</code> if no position is computable
	 */
	protected abstract P getPosition(long currentIdx, long spawnedEntities, long spawnableEntities);

	/**
	 * Spawn the entity according to the current spawn location space dimension.
	 * 
	 * @param spawner is the spawner to use.
	 * @param entityIndex is the index of the entity in the current spawning list.
	 * @param alreadySpawned is the count of already spawned entities from the current spawning list.
	 * @param spawnableEntities is the count of entities from the current spawning list.
	 * @return <code>true</code> if the entity was successfully spawned, otherwise <code>false</code>
	 */
	protected boolean spawn(Spawner spawner, long entityIndex, long alreadySpawned, long spawnableEntities) {
		D spawnDirection = randomDirection();
		P position = getPosition(entityIndex, alreadySpawned, spawnableEntities);
		
		if (spawnDirection!=null && position!=null) {
			spawner.spawn(position, spawnDirection);
			return true;
		}	
		getLogger().warning( "Unable to spawn an entity"); //$NON-NLS-1$
		return false;
	}
	
	/** Replies a logging object.
	 * 
	 * @return a logging object.
	 */
	protected Logger getLogger() {
		return Logger.getLogger(getClass().getCanonicalName());
	}
	
}
