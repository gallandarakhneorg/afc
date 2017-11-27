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
import java.util.UUID;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.jasim.environment.time.Clock;

/**
 * Spawn entities in the environment.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface SpawnLocation {
	
	/** Replies the identifier of the spawn location.
	 * 
	 * @return the identifier, never <code>null</code>.
	 */
	public UUID getIdentifier();

	/** Replies the name of the spawn location.
	 * 
	 * @return the name of the spawn location or <code>null</code>.
	 */
	public String getName();

	/** Replies the date at which this spawn point is starting to create entitied.
	 * 
	 * @return the starting date.
	 */
	public double getStartDate();
	
	/** Replies the date at which this spawn point is finishing to create entitied.
	 * 
	 * @return the starting date.
	 */
	public double getEndDate();

	/** Add a spawning law.
	 * 
	 * @param instanciator is the object that permits to create an entity instance.
	 * @param law is the law used to know when to spawn an entity.
	 */
	public void addSpawningLaw(Spawner instanciator, SpawningLaw law);
	
	/** Remove a spawning law.
	 * 
	 * @param instanciator is the object that permits to create an entity instance.
	 */
	public void removeSpawningLaw(Spawner instanciator);
	
	/** Spawn entities.
	 * 
	 * @param simulationTime is the current simulation time usable to spawn entities.
	 * @return <code>true</code> if at least one entity was spawned, otherwised <code>false</code>
	 */
	public boolean spawn(Clock simulationTime);
	
	/** Replies the list of spawners.
	 * 
	 * @return a list of spawners.
	 */
	public Collection<Spawner> getSpawnerList();
	
	/** Replies an object which is describing the current location of this spawning object.
	 * <p>
	 * The result is a point compliant with the space dimension where the spawner location
	 * is located, eg. {@link Point2d}, {@link Point1D5} or {@link Point1D}. 
	 * 
	 * @return the location of this spawning object.
	 */
	public EuclidianPoint getReferencePoint();

}
