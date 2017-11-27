/* 
 * $Id$
 * 
 * Copyright (c) 2004-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.agent.spawn;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.object.EuclidianDirection;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.agent.hotpoint.GoalPoint;
import fr.utbm.set.jasim.agent.hotpoint.WayPoint;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.interfaces.body.BodyContainerEnvironment;
import fr.utbm.set.jasim.environment.interfaces.body.factory.BodyDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.model.place.PlaceContainer;
import fr.utbm.set.jasim.environment.model.world.DynamicEntityManager;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.spawn.Spawner;

/**
 * This class describes objects that are able to create an
 * instance of an agent.
 *
 * @param <B> is the type of the bounds supported by the entities.
 * @param <ME> is the type of the mobile entities supported by the environment.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class SituatedAgentSpawner<
					B extends Bounds<?,?,?>,
					ME extends MobileEntity<B>>
implements Spawner {

	/** Replies a random name for spawned agents.
	 *
	 * @return a random name.
	 */
	protected static String randomName() {
		return "SITUATED_AGENT_"+UUID.randomUUID().toString(); //$NON-NLS-1$
	}

	private final WeakReference<SituatedEnvironment<?,?,ME,?>> environment;
	private WeakReference<Place<?,?,ME>> place;
	private final UUID placeId;
	private final BodyDescription bodyDescription;
	private final Iterable<FrustumDescription> frustumDescriptions;

	private long budget;
	
	private List<? extends WayPoint> waypoints = null;
	private List<? extends GoalPoint> goals = null;
	private Map<String,Object> attributes = null;

	/**
	 * @param bodyDescription is the description of the bodies to spawn.
	 * @param frustumDescriptions are the descriptions of the frustums to spawn.
	 * @param environment the environment in which the agent bodies are spawned.
	 * @param placeId the place where the spawner is located.
	 * @param budget is the initial budget of this spawner.
	 */
	public SituatedAgentSpawner(
			BodyDescription bodyDescription,
			Iterable<FrustumDescription> frustumDescriptions,
			SituatedEnvironment<?,?,ME,?> environment, 
			UUID placeId, 
			long budget) {
		this.bodyDescription = bodyDescription;
		this.frustumDescriptions = frustumDescriptions;
		this.environment = new WeakReference<SituatedEnvironment<?,?,ME,?>>(environment);
		this.placeId = placeId;
		this.place = null;
		this.budget = budget;
	}
	
	/** Replies the body description used by this spawner.
	 * 
	 * @return the body description used by this spawner.
	 */
	public BodyDescription getBodyDescription() {
		return this.bodyDescription;
	}
	
	/** Replies the frustum descriptions used by this spawner.
	 * 
	 * @return the frustum descriptions used by this spawner.
	 */
	public Iterable<FrustumDescription> getFrustumDescriptions() {
		return this.frustumDescriptions;
	}

	@SuppressWarnings("unchecked")
	private Place<?,?,ME> getPlace() {
		Place<?,?,ME> p = null;
		if (this.place!=null) p = this.place.get();
		if (p==null) {
			PlaceContainer<Place<?,?,ME>,?> env = (PlaceContainer<Place<?,?,ME>,?>)this.environment.get();
			assert(env!=null);
			p = env.getPlaceObject(this.placeId);
			this.place = new WeakReference<Place<?,?,ME>>(p);
		}
		return p;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getBudget() {
		return this.budget;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBudgetConsumed() {
		return this.budget <= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public boolean isTraversable(EuclidianPoint position) {
		if (position.getMathematicalDimension().floatValue>=2f) {
			Place<?,?,ME> placeInstance = getPlace();
			if (placeInstance!=null) {
				Ground grd = placeInstance.getGround();
				if (grd!=null) {
					return grd.isTraversable(position.getCoordinate(0), position.getCoordinate(1));
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void spawn(EuclidianPoint spawningPoint, EuclidianDirection direction) {
		if (this.budget>0) {
			try {
				SituatedAgent<?,?,?> agent = createAgentInstance();
				if (agent!=null) {
					ME body = createAgentBody(agent, spawningPoint, direction);
					if (body instanceof AgentBody<?,?>) {
						spawn(agent,body);
					}
					else {
						Logger.getAnonymousLogger().warning("The spawner has lost the agent body"); //$NON-NLS-1$
					}
				}
			}
			catch (Throwable e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Can't spawn agent", e); //$NON-NLS-1$
			}
		}
		else {
			Logger.getAnonymousLogger().warning("The spawner has consumed its budget"); //$NON-NLS-1$
		}
	}
		
	/** Spawn the agent and its body.
	 * <p>
	 * This function is invoked by {@link #spawn(EuclidianPoint, EuclidianDirection)}
	 * 
	 * @param agent
	 * @param body
	 */
	protected void spawn(SituatedAgent<?,?,?> agent, ME body) {
		SituatedEnvironment<?,?,ME,?> env = this.environment.get();
		if (env!=null) {
			// Insert the body into the environment
			
			// Force the place field to allow the entity manager
			// to quickly retreive the target place
			body.setPlace(getPlace()); 
			DynamicEntityManager<ME> manager = env.getDynamicEntityManager();
			manager.registerMobileEntity(body);
		}
		else {
			Logger.getAnonymousLogger().severe("Spawner has lost the reference to the situated environment"); //$NON-NLS-1$
		}

		startAgent(agent);
		if (this.budget>=0) this.budget --;
	}

	/** Create a instance of the agent.
	 * 
	 * @return a new instance of agent to insert into the simulation, never <code>null</code>
	 * @throws Exception in case of error.
	 */
	protected abstract SituatedAgent<?,?,?> createAgentInstance() throws Exception;
	
	/** Create a instance of the body.
	 * 
	 * @param agent is the agent for which the body must be spawned.
	 * @param position is the initial position of the body.
	 * @param orientation is the initial orientation of the body.
	 * @return a new instance of body to insert into the simulation, or <code>null</code>
	 * @throws Exception in case of error.
	 */
	@SuppressWarnings("unchecked")
	private ME createAgentBody(SituatedAgent<?,?,?> agent, EuclidianPoint position, EuclidianDirection orientation) throws Exception {
		
		BodyContainerEnvironment container = null;
		
		Place<?,?,ME> placeInstance = getPlace();
		if (placeInstance instanceof BodyContainerEnvironment) {
			container = (BodyContainerEnvironment)placeInstance;
		}

		SituatedEnvironment<?,?,ME,?> environmentInstance = this.environment.get();
		if (environmentInstance instanceof BodyContainerEnvironment) {
			container = (BodyContainerEnvironment)environmentInstance;
		}
		
		if (container!=null) {
			return (ME)agent.createBody(container.getAgentBodyFactory(
					position, orientation,
					this.bodyDescription,
					this.frustumDescriptions));
		}
		
		return null;
	}

	/**
	 * Start the agent on the binded agent platform.
	 *  
	 * @param agent is the agent to start.
	 */
	protected abstract void startAgent(SituatedAgent<?,?,?> agent);

	//------------------------------------------
	// Several data usefull to spawn agents
	//------------------------------------------
	
	/** Set the list of way points.
	 * 
	 * @param waypoints
	 */
	public void setWayPoints(List<? extends WayPoint> waypoints) {
		this.waypoints = waypoints;
	}

	/** Set the list of goals.
	 * 
	 * @param goals
	 */
	public void setGoals(List<? extends GoalPoint> goals) {
		this.goals = goals;
	}

	/** Set the list of attributes.
	 * 
	 * @param attributes
	 */
	public void setAttributes(Map<String,Object> attributes) {
		this.attributes = attributes;
	}

	/** Replies the list of waypoints.
	 * 
	 * @return a list, never <code>null</code>
	 */
	public List<? extends WayPoint> getWaypoints() {
		if (this.waypoints==null) return Collections.emptyList();
		return Collections.unmodifiableList(this.waypoints);
	}

	/** Replies the list of goals.
	 * 
	 * @return a list, never <code>null</code>
	 */
	public List<? extends GoalPoint> getGoals() {
		if (this.goals==null) return Collections.emptyList();
		return Collections.unmodifiableList(this.goals);
	}

	/** Replies the list of attributes.
	 * 
	 * @return a map, never <code>null</code>
	 */
	public Map<String,Object> getAttributes() {
		if (this.attributes==null) return Collections.emptyMap();
		return Collections.unmodifiableMap(this.attributes);
	}

	/** Replies if this spawner has waypoints.
	 * 
	 * @return <code>true</code> if waypoints are defined, otherwise <code>false</code>
	 */
	public boolean hasWaypoints() {
		return this.waypoints!=null && !this.waypoints.isEmpty();
	}

	/** Replies if this spawner has goals.
	 * 
	 * @return <code>true</code> if goals are defined, otherwise <code>false</code>
	 */
	public boolean hasGoals() {
		return this.goals!=null && !this.goals.isEmpty();
	}
	
	/** Replies if this spawner has attributes.
	 * 
	 * @return <code>true</code> if attributes are defined, otherwise <code>false</code>
	 */
	public boolean hasAttributes() {
		return this.attributes!=null && !this.attributes.isEmpty();
	}
	
}
