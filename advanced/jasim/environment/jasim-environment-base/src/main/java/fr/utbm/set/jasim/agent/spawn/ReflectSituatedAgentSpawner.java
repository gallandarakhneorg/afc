/* 
 * $Id$
 * 
 * Copyright (c) 2004-08, Multiagent Team - Systems and Transportation Laboratory (SeT)
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

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.agent.hotpoint.GoalPoint;
import fr.utbm.set.jasim.agent.hotpoint.WayPoint;
import fr.utbm.set.jasim.environment.interfaces.body.factory.BodyDescription;
import fr.utbm.set.jasim.environment.interfaces.body.factory.FrustumDescription;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.geom.bounds.Bounds;

/**
 * This class describes objects that are able to create an
 * instance of an agent by the user of Java reflection. It tries
 * to locate the following constructor in this order:
 * <ul>
 * <li><code>SituatedAgent(List&lt;? extends WayPoint&gt;, List&lt;? extends GoalPoint&gt;, Frustum&lt;?,?&gt;, Map&lt;String,Object&gt;)</code>;</li>
 * <li><code>SituatedAgent(List&lt;? extends WayPoint&gt;, List&lt;? extends GoalPoint&gt;, Iterable&lt;Frustum&lt;?,?&gt;&gt;, Map&lt;String,Object&gt;)</code>;</li>
 * <li><code>SituatedAgent(List&lt;? extends WayPoint&gt;, List&lt;? extends GoalPoint&gt;, Map&lt;String,Object&gt;)</code>;</li>
 * <li><code>SituatedAgent(List&lt;? extends WayPoint&gt;, List&lt;? extends GoalPoint&gt;, Frustum&lt;?,?&gt;)</code>;</li>
 * <li><code>SituatedAgent(List&lt;? extends WayPoint&gt;, List&lt;? extends GoalPoint&gt;, Iterable&lt;Frustum&lt;?,?&gt;&gt;)</code>;</li>
 * <li><code>SituatedAgent(List&lt;? extends WayPoint&gt;, List&lt;? extends GoalPoint&gt;)</code>;</li>
 * <li><code>SituatedAgent(List&lt;? extends GoalPoint&gt;, Map&lt;String,Object&gt;)</code>;</li>
 * <li><code>SituatedAgent(List&lt;? extends GoalPoint&gt;, Frustum&lt;?,?&gt;)</code>;</li>
 * <li><code>SituatedAgent(List&lt;? extends GoalPoint&gt;, Iterable&lt;Frustum&lt;?,?&gt;&gt;)</code>;</li>
 * <li><code>SituatedAgent(Map&lt;String,Object&gt;)</code>;</li>
 * <li><code>SituatedAgent(List&lt;? extends GoalPoint&gt;)</code>;</li>
 * <li><code>SituatedAgent(Frustum&lt;?,?&gt;)</code>;</li>
 * <li><code>SituatedAgent(Iterable&lt;Frustum&lt;?,?&gt;&gt;)</code>;</li>
 * <li><code>SituatedAgent()</code>;</li>
 * </ul>
 * <p>
 * Parameter semantics are:<ul>
 * <li><code>List&lt;? extends WayPoint&gt;</code> - a list of way points from the simulation configuration file (sfg file).
 * <li><code>List&lt;? extends GoalPoint&gt;</code> - a list of goal points from the simulation configuration file (sfg file).
 * <li><code>Frustum&lt;?,?&gt;</code> - a frustum to associate to the created agent, instanciated from the simulation configuration file (sfg file).
 * <li><code>Iterable&lt;Frustum&lt;?,?&gt;&gt;</code> - a iterable data structure of frustums to associate to the created agent, instanciated from the simulation configuration file (sfg file).
 * <li><code>Map&lt;String,Object&gt;</code> - the set of user variables defined in the simulation configuration file (sfg file).
 * </ul>
 * 
 * @param <B> is the type of the bounds supported by the entities.
 * @param <ME> is the type of the mobile entities supported by the environment.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class ReflectSituatedAgentSpawner<
								B extends Bounds<?,?,?>,
								ME extends MobileEntity<B>>
extends SituatedAgentSpawner<B,ME> {

	/** Supported constructor parameters in preferred order.
	 * 
	 * The parameters are described by the following keywords:
     * List<? extends WayPoint> W;
     * List<? extends GoalPoint> G;
     * Iterable<SpawnableFrustumDescription> F;
     * SpawnableFrustumDescription f;
     * Map<String,String> A;
     */
	private static final byte[][] PATTERNS = {
			{'W','G','f','A'},	
			{'W','G','F','A'},	
			{'W','G','A'},
			{'W','G','f'},	
			{'W','G','F'},	
			{'W','G'},
			{'G','A'},
			{'G','f'},
			{'G','F'},			
			{'A'},
			{'G'},
			{'f'},
			{'F'},
			{},
	};
	
	private final Class<? extends SituatedAgent<?,?,?>> agentType;
	
	private Constructor<? extends SituatedAgent<?,?,?>> preferredConstructor = null;
	private Object[] parameters = null;
	
	/**
	 * @param agentType is the type of agent to spawn.
	 * @param body is the description of the body to spawn.
	 * @param frustumDescriptions are the descriptions of the frustums to spawn.
	 * @param environment the environment in which the agent bodies are spawned.
	 * @param place the place where the spawner is located.
	 * @param initialBudget is the initial budget of this spawner.
	 */
	public ReflectSituatedAgentSpawner(
				Class<? extends SituatedAgent<?,?,?>> agentType, 
				BodyDescription body,
				Iterable<FrustumDescription> frustumDescriptions,
				SituatedEnvironment<?,?,ME,?> environment,
				UUID place, 
				long initialBudget) {
		super(body, frustumDescriptions, environment, place, initialBudget);
		this.agentType = agentType;
	}
	
	private void findConstructor() {
		if (this.preferredConstructor==null) {
			List<? extends WayPoint> waypoints = null;
			List<? extends GoalPoint> goals = null;
			Map<String, Object> attributes = null;
			Class<?>[] types;
			for(byte[] patterns : PATTERNS) {
				try {
					types = new Class<?>[patterns.length];
					this.parameters = new Object[patterns.length];
					for(int i=0; types!=null && i<patterns.length; ++i) {
						switch(patterns[i]) {
						case 'W':
							types[i] = List.class;
							if (waypoints==null) waypoints = getWaypoints();
							this.parameters[i] = waypoints;
							break;
						case 'G':
							types[i] = List.class;
							if (goals==null) goals = getGoals();
							this.parameters[i] = goals;
							break;
						case 'F':
							types[i] = Iterable.class;
							this.parameters[i] = getFrustumDescriptions();
							break;
						case 'f':
							types[i] = FrustumDescription.class;
							this.parameters[i] = getFrustumDescriptions().iterator().next();
							break;
						case 'A':
							types[i] = Map.class;
							if (attributes==null) attributes = getAttributes();
							this.parameters[i] = attributes;
							break;
						default:
							types = null;
						}
					}
					if (types!=null) {
						this.preferredConstructor = this.agentType.getConstructor(types);
						return ; // because a constructor was found
					}
				}
				catch(Throwable _) {
					//
				}
				this.parameters = null;
			}
		}
	}
	
	/** Create a instance of the agent.
	 * 
	 * @return a new instance of agent to insert into the simulation.
	 */
	@Override
	protected SituatedAgent<?,?,?> createAgentInstance() throws Exception {
		findConstructor();
		if (this.preferredConstructor!=null) {
			try {
				return this.preferredConstructor.newInstance(this.parameters);
			}
			catch(Throwable _) {
				//
			}
		}
		return null;
	}
	
}
