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
package fr.utbm.set.jasim.controller.probe;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.jasim.agent.hotpoint.GoalPoint;
import fr.utbm.set.jasim.agent.hotpoint.WayPoint;
import fr.utbm.set.jasim.agent.spawn.SituatedAgentSpawner;
import fr.utbm.set.jasim.controller.SimulationController;
import fr.utbm.set.jasim.environment.interfaces.probes.AbstractEnvironmentalProbe;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment;
import fr.utbm.set.jasim.spawn.SpawnLocation;
import fr.utbm.set.jasim.spawn.Spawner;

/** This class is probing the hot points of the simulated environment.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HotPointProbe extends AbstractEnvironmentalProbe {
	
	private List<WayPoint> waypoints = new LinkedList<WayPoint>();
	private List<GoalPoint> goals = new LinkedList<GoalPoint>();
	private final WeakReference<SimulationController<?,?,?>> controller;
	private final UUID place;

	/**
	 * @param name is the name of the probe.
	 * @param placeName is the name of the place to probe.
	 * @param simulationController is the controller to probe
	 */
	public HotPointProbe(String name, UUID placeName, SimulationController<?,?,?> simulationController) {
		super(name, null);
		this.place = placeName;
		this.controller = new WeakReference<SimulationController<?,?,?>>(simulationController);
	}
	
	/** Collects the values.
	 */
	@Override
	protected synchronized void collect() {
		this.waypoints = new LinkedList<WayPoint>();
		this.goals = new LinkedList<GoalPoint>();
		SimulationController<?,?,?> theController = (this.controller==null) ? null : this.controller.get();
		if (theController!=null) {
			SituatedAgentSpawner<?,?> situatedSpawner;
			EuclidianPoint referencePosition;
			SituatedEnvironment<?,?,?,?> environment = theController.getEnvironment();
			UUID placeName;
			for(SpawnLocation location : theController.getSpawners()) {
				referencePosition = location.getReferencePoint();
				placeName = environment.getPlace(referencePosition);
				
				if (placeName!=null && this.place.equals(placeName)) {
					for(Spawner spawner : location.getSpawnerList()) {
						if (spawner instanceof SituatedAgentSpawner<?,?>) {
							 situatedSpawner = (SituatedAgentSpawner<?,?>)spawner;
							 for(WayPoint point : situatedSpawner.getWaypoints()) {
								 this.waypoints.add(point);
							 }
							 for(GoalPoint point : situatedSpawner.getGoals()) {
								 this.goals.add(point);
							 }
						}
					}
				}
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public synchronized Map<String,Object> getCollectedValues() {
		Map<String,Object> map = new HashMap<String, Object>(2);
		map.put("GOALS", this.goals); //$NON-NLS-1$
		map.put("WAYPOINTS", this.waypoints); //$NON-NLS-1$
		return map;
	}
		
	/** {@inheritDoc}
	 */
	@Override
	public UUID getPlace() {
		return this.place;
	}
	
}