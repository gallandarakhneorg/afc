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
package fr.utbm.set.jasim.io.sfg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.interfaces.body.factory.BodyDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.spawn.SpawningLaw;
import fr.utbm.set.math.stochastic.StochasticLaw;

/**
 * Set of parameters to initialize a simulation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EntitySimulationParameterSet extends AbstractParameter {

	private final Class<? extends SituatedAgent<?,?,?>> agent;		
	private final Class<? extends AgentBody<?,?>> bodyType;
	private final Collection<Class<? extends Semantic>> semantics = new ArrayList<Class<? extends Semantic>>();
	private double a=0, b=0, c=0;
	private boolean isPedestrian = true;
	private final Collection<FrustumDescription> frustumDescriptions = new ArrayList<FrustumDescription>();
	
	private List<WayPointSimulationParameterSet> waypoints = null;
	private List<GoalSimulationParameterSet> goals = null;
	private Map<String, Object> attributes = null;
	private Map<String, Object> probes = null;
	private Class<?> generationLawType;
	private Map<String,String> generationLawParameters;
	private Long budget = null;
	
	/**
	 * @param <ADR>
	 * @param <INF>
	 * @param <PER>
	 * @param dimension must be one of <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 * @param agent
	 * @param body
	 */
	<ADR,INF extends Influence, PER extends Perception> EntitySimulationParameterSet(float dimension, Class<? extends SituatedAgent<ADR,INF,PER>> agent, Class<? extends AgentBody<INF,PER>> body) {
		super(dimension);
		this.agent = agent;
		this.bodyType = body;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof EntitySimulationParameterSet) {
			return hashCode() == o.hashCode();
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}

	/** Replies the type of this entity.
	 * 
	 * @return the type of this entity.
	 */
	public Class<? extends SituatedAgent<?,?,?>> getAgentType() {
		return this.agent;
	}
	
	/** Replies the type of the body of this entity.
	 * 
	 * @return the type of the body of this entity.
	 */
	public Class<? extends AgentBody<?,?>> getBodyType() {
		return this.bodyType;
	}

	/** Replies the type of the body of this entity.
	 *
	 * @param <E> is the desired super type for the replied body.
	 * @param objectType is the desired super type for the replied body.
	 * @return the type of the body of this entity, or <code>null</code>
	 */
	public <E extends WorldEntity<?>> Class<? extends E> getBodyType(Class<E> objectType) {
		if (this.bodyType!=null && objectType.isAssignableFrom(this.bodyType))
			return this.bodyType.asSubclass(objectType);
		return null;
	}

	/** Add the frustum.
	 * 
	 * @param description is the description to add.
	 */
	public void addFrustum(FrustumDescription description) {
		this.frustumDescriptions.add(description);
	}
	
	/** Replies the frustum type.
	 * 
	 * @return the type of the frustum.
	 */
	public Iterable<FrustumDescription> getFrustumDescriptions() {
		return Collections.unmodifiableCollection(this.frustumDescriptions);
	}

	/** Set the body description with pedestrian attributes.
	 * 
	 * @param height is the distance from feet to head.
	 * @param radius is the radius of the body.
	 */
	public void setPedestrianBody(double height, double radius) {
		this.a = height;
		this.b = radius;
		this.c = 0.;
		this.isPedestrian = true;
	}
	
	/** Set the body description with vehicle attributes.
	 * 
	 * @param length is the distance from back to front.
	 * @param width is the distance from left to right.
	 * @param height is the distance from road to roof.
	 */
	public void setVehicleBody(double length, double width, double height) {
		this.a = length;
		this.b = width;
		this.c = height;
		this.isPedestrian = false;
	}

	/** Replies the complete body description.
	 * 
	 * @return the body description.
	 */
	public BodyDescription getBodyDescription() {
		if (this.isPedestrian)
			return BodyDescription.createPedestrianBodyDescription(
					this.bodyType, this.a, this.b);
		return BodyDescription.createVehicleBodyDescription(
				this.bodyType, this.a, this.b, this.c);
	}

	/** Replies the count of waypoints.
	 *
	 * @return the count of waypoints.
	 */
	public int getWaypointCount() {
		return this.waypoints==null ? 0 : this.waypoints.size();
	}

	/** Add a way point.
	 * 
	 * @param name
	 * @param position
	 * @param tangentX
	 * @param tangentY
	 * @param tangentZ
	 * @param velocity
	 * @param time
	 * @return the new waypoint
	 */
	public WayPointSimulationParameterSet addWaypoint(String name, PositionParameter position, double tangentX, double tangentY, double tangentZ, double velocity, double time) {
		WayPointSimulationParameterSet waypoint = new WayPointSimulationParameterSet(
				name,position,tangentX,tangentY,tangentZ,velocity,time);
		if (this.waypoints==null) {
			this.waypoints = new ArrayList<WayPointSimulationParameterSet>(5);
		}
		this.waypoints.add(waypoint);
		return waypoint;
	}

	/** Add a way point.
	 * 
	 * @param name
	 * @param position
	 * @param direction
	 * @param velocity
	 * @param time
	 * @return the new waypoint
	 */
	public WayPointSimulationParameterSet addWaypoint(String name, PositionParameter position, Direction1D direction, double velocity, double time) {
		WayPointSimulationParameterSet waypoint = new WayPointSimulationParameterSet(
				name,position,direction,velocity,time);
		if (this.waypoints==null) {
			this.waypoints = new ArrayList<WayPointSimulationParameterSet>(5);
		}
		this.waypoints.add(waypoint);
		return waypoint;
	}

	/** Add a way point.
	 * 
	 * @param name
	 * @param position
	 * @param velocity
	 * @param time
	 * @return the new waypoint
	 */
	public WayPointSimulationParameterSet addWaypoint(String name, PositionParameter position, double velocity, double time) {
		return addWaypoint(name,position,Double.NaN,Double.NaN,Double.NaN,velocity,time);
	}

	/** Add a way point.
	 * 
	 * @param name
	 * @param position
	 * @param tangentX
	 * @param tangentY
	 * @param tangentZ
	 * @return the new waypoint
	 */
	public WayPointSimulationParameterSet addWaypoint(String name, PositionParameter position, double tangentX, double tangentY, double tangentZ) {
		return addWaypoint(name,position,tangentX,tangentY,tangentZ,Double.NaN, Double.NaN);
	}

	/** Add a way point.
	 *
	 * @param name
	 * @param position
	 * @param direction
	 * @return the new waypoint
	 */
	public WayPointSimulationParameterSet addWaypoint(String name, PositionParameter position, Direction1D direction) {
		return addWaypoint(name,position,direction,Double.NaN, Double.NaN);
	}

	/** Add a way point.
	 *
	 * @param name
	 * @param position
	 * @param velocity
	 * @return the new waypoint
	 */
	public WayPointSimulationParameterSet addWaypoint(String name, PositionParameter position, double velocity) {
		return addWaypoint(name,position,Double.NaN,Double.NaN,Double.NaN,velocity,Double.NaN);
	}
	
	/** Add a way point.
	 *
	 * @param name
	 * @param position
	 * @return the new waypoint
	 */
	public WayPointSimulationParameterSet addWaypoint(String name, PositionParameter position) {
		return addWaypoint(name, position,Double.NaN,Double.NaN,Double.NaN,Double.NaN,Double.NaN);
	}
	
	/** Replies the waypoint at the given index.
	 * 
	 * @param index
	 * @return the waypoint
	 */
	public WayPointSimulationParameterSet getWaypointAt(int index) {
		if (this.waypoints==null) throw new IndexOutOfBoundsException();
		return this.waypoints.get(index);
	}

	/** Replies the count of goals.
	 *
	 * @return the count of goals.
	 */
	public int getGoalCount() {
		return this.goals==null ? 0 : this.goals.size();
	}

	/** Add a goal.
	 * 
	 * @param name
	 * @param position
	 * @param tangentX
	 * @param tangentY
	 * @param tangentZ
	 * @param time
	 * @return the new goal
	 */
	public GoalSimulationParameterSet addGoal(String name, PositionParameter position, double tangentX, double tangentY, double tangentZ, double time) {
		GoalSimulationParameterSet goal = new GoalSimulationParameterSet(
				name,position,tangentX,tangentY,tangentZ,time);
		if (this.goals==null) {
			this.goals = new ArrayList<GoalSimulationParameterSet>(5);
		}
		this.goals.add(goal);
		return goal;
	}

	/** Add a goal.
	 * 
	 * @param name
	 * @param position
	 * @param direction
	 * @param time
	 * @return the new goal
	 */
	public GoalSimulationParameterSet addGoal(String name, PositionParameter position, Direction1D direction, double time) {
		GoalSimulationParameterSet goal = new GoalSimulationParameterSet(
				name,position,direction,time);
		if (this.goals==null) {
			this.goals = new ArrayList<GoalSimulationParameterSet>(5);
		}
		this.goals.add(goal);
		return goal;
	}

	/** Add a goal.
	 * 
	 * @param name
	 * @param position
	 * @param time
	 * @return the new goal
	 */
	public GoalSimulationParameterSet addGoal(String name, PositionParameter position, double time) {
		return addGoal(name, position, Double.NaN, Double.NaN, Double.NaN, time);
	}

	/** Add a goal.
	 * 
	 * @param name
	 * @param position
	 * @return the new goal
	 */
	public GoalSimulationParameterSet addGoal(String name, PositionParameter position) {
		return addGoal(name,position,Double.NaN,Double.NaN,Double.NaN,Double.NaN);
	}
	
	/** Replies the goal at the given index.
	 * 
	 * @param index
	 * @return the goal
	 */
	public GoalSimulationParameterSet getGoalAt(int index) {
		if (this.goals==null) throw new IndexOutOfBoundsException();
		return this.goals.get(index);
	}
	
	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, String value) {
		if (value!=null) {
			if (this.attributes==null)
				this.attributes = new TreeMap<String, Object>();
	
			{
				Date dt = XMLSimulationParserUtil.parseHour(value);
				if (dt!=null) {
					this.attributes.put(name,dt);
					return dt;
				}
			}
			
			try {
				Boolean bool = Boolean.parseBoolean(value);
				if (bool!=null) {
					this.attributes.put(name,bool);
					return bool;
				}
			}
			catch(Throwable _ ) {
				//
			}
	
			try {
				Double number = Double.parseDouble(value);
				if (number !=null) {
					this.attributes.put(name,number );
					return number ;
				}
			}
			catch(Throwable _ ) {
				//
			}
	
			try {
				Long number = Long.parseLong(value);
				if (number !=null) {
					this.attributes.put(name,number);
					return number ;
				}
			}
			catch(Throwable _ ) {
				//
			}
	
			this.attributes.put(name, value);

			return value;
		}
		return null;
	}

	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, boolean value) {
		if (this.attributes==null)
			this.attributes = new TreeMap<String, Object>();
		Object v = value;
		this.attributes.put(name, v);
		return value;
	}

	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, byte value) {
		if (this.attributes==null)
			this.attributes = new TreeMap<String, Object>();
		Object v = value;
		this.attributes.put(name, v);
		return value;
	}

	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, char value) {
		if (this.attributes==null)
			this.attributes = new TreeMap<String, Object>();
		Object v = value;
		this.attributes.put(name, v);
		return value;
	}

	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, short value) {
		if (this.attributes==null)
			this.attributes = new TreeMap<String, Object>();
		Object v = value;
		this.attributes.put(name, v);
		return value;
	}

	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, int value) {
		if (this.attributes==null)
			this.attributes = new TreeMap<String, Object>();
		Object v = value;
		this.attributes.put(name, v);
		return value;
	}

	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, long value) {
		if (this.attributes==null)
			this.attributes = new TreeMap<String, Object>();
		Object v = value;
		this.attributes.put(name, v);
		return value;
	}

	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, float value) {
		if (this.attributes==null)
			this.attributes = new TreeMap<String, Object>();
		Object v = value;
		this.attributes.put(name, v);
		return value;
	}

	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, double value) {
		if (this.attributes==null)
			this.attributes = new TreeMap<String, Object>();
		Object v = value;
		this.attributes.put(name, v);
		return value;
	}

	/** Set the value of an attribute.
	 * 
	 * @param name
	 * @param value
	 * @return the object saved as attribute.
	 */
	public Object setAttribute(String name, Object value) {
		if (value!=null) {
			if (this.attributes==null)
				this.attributes = new TreeMap<String, Object>();
			Object v = value;
			this.attributes.put(name, v);
		}
		return value;
	}

	/** Remove the value of an attribute.
	 * 
	 * @param name
	 */
	public void removeAttribute(String name) {
		if (this.attributes==null) return;
		this.attributes.remove(name);
		if (this.attributes.isEmpty())
			this.attributes = null;
	}
	
	/** Replies the value of an attribute.
	 * 
	 * @param name
	 * @return the value or <code>null</code> if the attribute does not exist.
	 */
	public Object getAttribute(String name) {
		if (this.attributes==null) return null;
		return this.attributes.get(name);
	}

	/** Replies the value of an attribute.
	 * 
	 * @param <T> is the type of the value to reply.
	 * @param name
	 * @param type is the type of the value to reply.
	 * @return the value or <code>null</code> if the attribute does not exist.
	 */
	public <T> T getAttribute(String name, Class<T> type) {
		if (this.attributes!=null) {
			Object obj = this.attributes.get(name);
			if (obj!=null && type.isInstance(obj))
				return type.cast(obj);
		}
		return null;
	}

	/** Replies all the attributes.
	 * 
	 * @return an unmodifiable map.
	 */
	public Map<String,Object> getAttributes() {
		if (this.attributes==null) return Collections.emptyMap();
		return Collections.unmodifiableMap(this.attributes);
	}

	/** Create a probe
	 * 
	 * @param name
	 */
	public void addProbe(String name) {
		if (this.probes==null)
			this.probes = new TreeMap<String, Object>();
		this.probes.put(name,null);
	}

	/** Remove a probe.
	 * 
	 * @param name
	 */
	public void removeProbe(String name) {
		if (this.probes==null) return;
		this.probes.remove(name);
		if (this.probes.isEmpty())
			this.probes = null;
	}
	
	/** Replies all the probes.
	 * 
	 * @return an unmodifiable collection.
	 */
	public Collection<String> getProbes() {
		if (this.probes==null) return Collections.emptyList();
		return Collections.unmodifiableSet(this.probes.keySet());
	}
	
	/** Set the generation law.
	 * 
	 * @param generationLawType
	 * @param parameters
	 */
	public void setStochasticGenerationLaw(Class<? extends StochasticLaw> generationLawType, Map<String,String> parameters) {
		this.generationLawType = generationLawType;
		this.generationLawParameters = parameters;
	}
	
	/** Set the generation law.
	 * 
	 * @param generationLawType
	 * @param parameters
	 */
	public void setSpawningGenerationLaw(Class<? extends SpawningLaw> generationLawType, Map<String,String> parameters) {
		this.generationLawType = generationLawType;
		this.generationLawParameters = parameters;
	}

	/** Replies the type of the generation law.
	 * 
	 * @return the type of the generation law or <code>null</code>.
	 * @see #getSpawningGenerationLawType()
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends StochasticLaw> getStochasticGenerationLawType() {
		if (StochasticLaw.class.isAssignableFrom(this.generationLawType))
			return (Class<? extends StochasticLaw>)this.generationLawType;
		return null;
	}

	/** Replies the type of the generation law.
	 * 
	 * @return the type of the generation law or <code>null</code>.
	 * @see #getStochasticGenerationLawType()
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends SpawningLaw> getSpawningGenerationLawType() {
		if (SpawningLaw.class.isAssignableFrom(this.generationLawType))
			return (Class<? extends SpawningLaw>)this.generationLawType;
		return null;
	}

	/** Replies the parameters of the generation law.
	 * 
	 * @return an unmodifiable map.
	 */
	public Map<String,String> getGenerationLawParameters() {
		if (this.generationLawParameters==null) return Collections.emptyMap();
		return Collections.unmodifiableMap(this.generationLawParameters);
	}
	
	/** Set the maximum count of entity to spawn for this type of entity.
	 *
	 * @param budget is the maximal count of entities to spawn.
	 */
	public void setBudget(long budget) {
		this.budget = budget;
	}

	/** Set to infinity the maximum count of entity to spawn for this type of entity.
	 */
	public void setInfiniteBudget() {
		this.budget = null;
	}

	/** Replies the maximum count of entity to spawn for this type of entity.
	 *
	 * @return the maximal count of entities to spawn.
	 */
	public long getBudget() {
		return this.budget == null ? Long.MAX_VALUE : this.budget;
	}

	/** Replies if the budget was set to infinity.
	 * 
	 * @return <code>true</code> if an infinite number of entities should be generated,
	 * otherwise <code>false</code> 
	 */
	public boolean isInfiniteBudget() {
		return this.budget == null;
	}

	/** Add a semantic.
	 * 
	 * @param semantic
	 */
	public void addSemantic(Class<? extends Semantic> semantic) {
		this.semantics.add(semantic);
	}

}
