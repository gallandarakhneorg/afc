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
package fr.utbm.set.jasim.environment.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Logger;

import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.util.ListenerCollection;

import fr.utbm.set.collection.CollectionUtil;
import fr.utbm.set.collection.IterableCollection;
import fr.utbm.set.collection.SizedIterable;
import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EntityActionEvent;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EntityLifeEvent;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.interfaces.internalevents.JasimSimulationListener;
import fr.utbm.set.jasim.environment.interfaces.internalevents.SimulationInitEvent;
import fr.utbm.set.jasim.environment.interfaces.internalevents.SimulationStopEvent;
import fr.utbm.set.jasim.environment.interfaces.probes.EnvironmentalProbeManager;
import fr.utbm.set.jasim.environment.interfaces.probes.EnvironmentalProbes;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;
import fr.utbm.set.jasim.environment.model.dynamics.DynamicsEngine;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.influences.InfluenceSolver;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGenerator;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.model.place.PlaceContainer;
import fr.utbm.set.jasim.environment.model.place.PlaceDescription;
import fr.utbm.set.jasim.environment.model.place.PortalDescription;
import fr.utbm.set.jasim.environment.model.world.DynamicEntityManager;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.jasim.environment.model.world.WorldModelActuator;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.jasim.environment.time.ConstantTimeManager;
import fr.utbm.set.jasim.environment.time.TimeManager;
import fr.utbm.set.util.ObjectReferenceComparator;
import fr.utbm.set.util.error.ErrorLogger;

/**
 * The default implementation of a situated environment for a MABS using
 * the Influence-Reaction model to manage environmental dynamic and agent's actions/perceptions.
 *
 * @param <EA> is the type of the environmental actions supported by this environment.
 * @param <SE> is the type of the static entities supported by this environment.
 * @param <ME> is the type of the mobile entities supported by this environment.
 * @param <P> is the type of place supported by this environment.
 * @param <PT> is the type of the supported euclidian points.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSituatedEnvironment<
								EA extends EnvironmentalAction,
								SE extends WorldEntity<? extends Bounds<?,PT,?>>,
								ME extends MobileEntity<? extends Bounds<?,PT,?>>,
								P extends Place<EA,SE,ME>,
								PT> 
implements SituatedEnvironment<EA,SE,ME,PT>,
		   PlaceContainer<P,PT> {

	/** Identifier of the simulation scenario to run.
	 */
	private UUID scenarioId = null;
	
	/** Identifier of the simulation run.
	 */
	private UUID runId = null;

	/** Date of the simulation scenario to run.
	 */
	private Date scenarioDate = null;

	/** Name of the simulation scenario to run.
	 */
	private String scenarioName = null;

	/** Authors of the simulation scenario to run.
	 */
	private String scenarioAuthors = null;

	/** Version of the simulation scenario to run.
	 */
	private String scenarioVersion = null;

	/** Description of the simulation scenario to run.
	 */
	private String scenarioDescription = null;

	/** Time manager.
	 */
	private TimeManager timeManager;	

	/** Indicates if the environment was initialized/activated.
	 */
	private boolean activated = false;

	/** List of places attached to this environment.
	 */
	private final TreeMap<UUID,P> places = new TreeMap<UUID,P>();
	
	/** Listeners
	 */
	private final ListenerCollection<JasimSimulationListener<EA,SE,ME>> listeners = new ListenerCollection<JasimSimulationListener<EA,SE,ME>>();
	
	private final BufferedEntityManager entityManager = new BufferedEntityManager();	
	private final EnvironmentalProbeManager probeManager = new EnvironmentalProbeManager();

	/**
	 */
	public AbstractSituatedEnvironment() {
		this.timeManager = null;
	}
	
	/* ********************************************** Events *** */
	
	/** {@inheritDoc}
	 */
	@Override
	public void addJasimSimulationListener(JasimSimulationListener<EA,SE,ME> listener) {
		this.listeners.add(JasimSimulationListener.class,listener);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void removeJasimSimulationListener(JasimSimulationListener<EA,SE,ME> listener) {
		this.listeners.remove(JasimSimulationListener.class,listener);
	}

	/** Fire the initialization event.
	 * 
	 * @param placeCount is the count of initialized places.
	 * @param placesToInitialize are the initialized places.
	 */
	@SuppressWarnings("unchecked")
	protected void fireInitialized(int placeCount, Iterable<PlaceDescription<SE,ME>> placesToInitialize) {
		JasimSimulationListener<EA,SE,ME>[] list = this.listeners.getListeners(JasimSimulationListener.class);
		if (list.length>0) {
			SimulationInitEvent<SE,ME> event = new SimulationInitEvent<SE,ME>(
					this, this.runId, 
					this.scenarioId, this.scenarioName,
					this.scenarioDate, this.scenarioAuthors,
					this.scenarioVersion, this.scenarioDescription,
					this.timeManager.getTimeUnit(),
					placeCount,
					placesToInitialize);
			for(JasimSimulationListener<EA,SE,ME> listener : list) {
				listener.simulationInitiated(event);
			}
		}
	}

	/** Fire the stopping event.
	 * 
	 * @param clock last simulation time.
	 */
	@SuppressWarnings("unchecked")
	protected void fireStopped(Clock clock) {
		JasimSimulationListener<EA,SE,ME>[] list = this.listeners.getListeners(JasimSimulationListener.class);
		if (list.length>0) {
			SimulationStopEvent event = new SimulationStopEvent(
					this, this.runId, this.scenarioId, clock);
			for(JasimSimulationListener<EA,SE,ME> listener : list) {
				listener.simulationStopped(event);
			}
		}
	}

	/** Fire the entity action event.
	 * 
	 * @param time
	 * @param actionCount
	 * @param actions
	 */
	@SuppressWarnings("unchecked")
	protected void fireActionsApplied(Clock time, int actionCount, Iterable<EA> actions) {
		JasimSimulationListener<EA,SE,ME>[] list = this.listeners.getListeners(JasimSimulationListener.class);
		if (list.length>0) {
			if (actionCount>0) {
				EntityActionEvent<EA> event = new EntityActionEvent<EA>(
						this, this.runId, this.scenarioId, time, actionCount, actions);
				for(JasimSimulationListener<EA,SE,ME> listener : list) {
					listener.entityActionsApplied(event);
				}
			}
			else {
				Clock clock = getSimulationClock();
				for(JasimSimulationListener<EA,SE,ME> listener : list) {
					listener.simulationIddle(clock);
				}
			}
		}
	}

	/** Fire the entity addition event.
	 * 
	 * @param time
	 * @param entityCount
	 * @param entities
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void fireEntityAddition(Clock time, int entityCount, Iterable<ME> entities) {
		JasimSimulationListener<EA,SE,ME>[] list = this.listeners.getListeners(JasimSimulationListener.class);
		if (list.length>0) {
			EntityLifeEvent event = new EntityLifeEvent<ME>(
					this, this.runId, this.scenarioId, time, entityCount, entities);
			for(JasimSimulationListener<EA,SE,ME> listener : list) {
				listener.entitiesArrived(event);
			}
		}
	}

	/** Fire the entity removal event.
	 * 
	 * @param time
	 * @param entityCount
	 * @param entities
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void fireEntityRemoval(Clock time, int entityCount, Iterable<ME> entities) {
		JasimSimulationListener<EA,SE,ME>[] list = this.listeners.getListeners(JasimSimulationListener.class);
		if (list.length>0) {
			EntityLifeEvent event = new EntityLifeEvent<ME>(
					this, this.runId, this.scenarioId, time, entityCount, entities);
			for(JasimSimulationListener<EA,SE,ME> listener : list) {
				listener.entitiesDisappeared(event);
			}
		}
	}

	/* ********************************************** Life Cycle *** */
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isActivated() {
		return this.activated;
	}
	
	/** Invoked to create a place according to the given description.
	 * <p>
	 * This function is in charge to connect together all the
	 * internal components of the place (ground, influence solver...).
	 * 
	 * @param description
	 * @return a place, or <code>null</code> if it is impossible to 
	 * create a new place
	 */
	protected abstract P createPlace(PlaceDescription<SE,ME> description);
	
	/** Invoked to destroy a place.
	 * 
	 * @param place
	 */
	protected abstract void destroyPlace(P place);

	/** Invoked to create portal in the environment.
	 * 
	 * @param description is the description of the portal.
	 * @param sourcePlace is the place where the portal should start.
	 * @param targetPlace is the place where the portal should arrive.
	 */
	protected abstract void createPortal(PortalDescription description, 
			P sourcePlace, P targetPlace);

	/** {@inheritDoc}
	 */
	@Override
	public void activate(SituatedEnvironmentDescription<SE,ME> description, Progression taskProgression) {

		if (taskProgression!=null) taskProgression.setProperties(0, 0, 50, false);
		
		try {
			this.runId = UUID.randomUUID();
			
			// initialize the situated environment from the description
			this.timeManager = description.getTimeManager();
			this.scenarioId = description.getIdentifier();
			this.scenarioDate = description.getDate();
			this.scenarioName = description.getName();
			this.scenarioAuthors = description.getAuthors();
			this.scenarioVersion = description.getVersion();
			this.scenarioDescription = description.getDescription();

			// Default initializations
			this.probeManager.clear();
			if (this.timeManager==null) 
				this.timeManager = new ConstantTimeManager();			
			
			// Place creation
			this.places.clear();
	
			Progression subTask = null;
			if (taskProgression!=null) {
				taskProgression.setValue(10);
				subTask = taskProgression.subTask(10, 0, description.getPlaceCount());
			}
	
			UUID id;
			P place;
			InfluenceSolver<EA,ME> influenceSolver;
			
			int index = 0;
			for(PlaceDescription<SE,ME> placeDesc : description.getPlaces()) {
				id = placeDesc.getIdentifier();
				if (!this.places.containsKey(id)) {
					place = createPlace(placeDesc);
					if (place!=null) {
						this.places.put(id, place);
						
						// Bind the influence solver and the other components of the environment.
						influenceSolver = place.getInfluenceSolver(); 
						influenceSolver.setTimeManager(this.timeManager);
						influenceSolver.setDynamicEntityManager(this.entityManager);
					}
					
					if (subTask!=null) subTask.setValue(++index);
				}
				else throw new IllegalArgumentException("duplicated place: "+id); //$NON-NLS-1$
			}
			
			subTask = null;
			if (taskProgression!=null) {
				subTask = taskProgression.subTask(10, 0, description.getPortalCount());
			}
	
			// Bind the portals
			index = 0;
			UUID sourceId, targetId;
			for(PortalDescription portalDesc : description.getPortals()) {
				sourceId = portalDesc.getFirstPlaceIdentifier();
				targetId = portalDesc.getSecondPlaceIdentifier();
				P sourcePlace = this.places.get(sourceId);
				P targetPlace = this.places.get(targetId);
				if (sourcePlace!=null && targetPlace!=null) {
					createPortal(portalDesc, sourcePlace, targetPlace);
				}
				if (subTask!=null) subTask.setValue(++index);
			}		
			
			// Initialize associated renderers
			if (taskProgression!=null) taskProgression.setValue(40);

			this.activated = true;
			
			// Fire the event
			fireInitialized(description.getPlaceCount(), description.getPlaces());

			// Be sure that the environment behaviour is up-to-date
			behaviourBeforeAgents();
		}
		finally {
			if (taskProgression!=null) taskProgression.setValue(50);
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final void behaviour() {
		behaviourAfterAgents();
		behaviourBeforeAgents();
	}
	
	/** Behaviour to run before agent's execution.
	 */
	protected void behaviourBeforeAgents() {
		if (this.timeManager!=null)
			this.timeManager.onEnvironmentBehaviourStarted();
		
		PerceptionGenerator generator;
		
		for(P place : this.places.values()) {
			generator = place.getPerceptionGenerator();
			if (generator!=null) generator.computeAgentPerceptions();
		}
	}
	
	/** Behaviour to run after agent's execution.
	 */
	protected void behaviourAfterAgents() {
		DynamicsEngine dynamicsEngine;
		InfluenceSolver<EA,ME> influenceSolver;
		EnvironmentalActionCollector<EA> actionCollector;
		WorldModelActuator<EA,ME> worldUpdater;
		
		Clock currentTime = this.timeManager.getClock();
		int count = 0;
		IterableCollection<EA> allActions = new IterableCollection<EA>();
		
		for(P place : this.places.values()) {
			
			dynamicsEngine = place.getDynamicsEngine();
			if (dynamicsEngine!=null) dynamicsEngine.run();
			
			influenceSolver = place.getInfluenceSolver();
			if (influenceSolver!=null) influenceSolver.run();
			
			actionCollector = place.getEnvironmentalActionCollector();
			if (actionCollector!=null) {
				SizedIterable<EA> actions = actionCollector.consumeActions();

				worldUpdater = place.getWorldModelUpdater();
				if (worldUpdater!=null) {
					worldUpdater.registerActions(currentTime, actions);
					if (actions.size()>0)
						this.entityManager.registerActuatorForCommitment(worldUpdater);
				}
				
				count += actions.size();
				allActions.add(actions);
			}
		}
		
		if (this.timeManager!=null) {
			this.timeManager.onEnvironmentBehaviourFinished();
			Logger.getLogger(getClass().getCanonicalName()).finest("time is now: " //$NON-NLS-1$
					+this.timeManager.getClock().getSimulationTime());
		}

		// Finalize arrival and departure events and actions.
		this.entityManager.commit();		

		fireActionsApplied(currentTime, count, allActions);

		// Notify probe listeners that all the probed values are now available
		this.probeManager.refresh();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void end() {
		if (this.timeManager!=null)
			this.timeManager.onEnvironmentBehaviourFinished();

		this.activated = false;
		
		for(P place : this.places.values()) {
			destroyPlace(place);
		}
		
		// Stop associated renderers
		fireStopped(this.timeManager.getClock());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Clock getSimulationClock() {
		return this.timeManager==null ? null : this.timeManager.getClock();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DynamicEntityManager<ME> getDynamicEntityManager() {
		return this.entityManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ground getGround(UUID placeId) {
		Place<EA,SE,ME> place = getPlaceObject(placeId);
		if (place!=null) return place.getGround();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getPlace(PT position) {
		Place<EA,SE,ME> place = getPlaceObject(position);
		if (place!=null) return place.getIdentifier();
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public UUID getPlace(EuclidianPoint position) {
		Place<EA,SE,ME> place = getPlaceObject(position);
		if (place!=null) return place.getIdentifier();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override 
	public Iterable<UUID> getPlaceIdentifiers() {
		return CollectionUtil.unmodifiableIterable(this.places.keySet());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPlaceCount() {
		return this.places.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnvironmentalProbes getProbeManager() {
		return this.probeManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public P getPlaceObject(UUID placeName) {
		return this.places.get(placeName);
	}

	/**
	 * Replies all the places in this environment.
	 * 
	 * @return all the places in this environment.
	 */
	protected Iterable<P> getPlaceObjects() {
		return this.places.values();
	}

	/**
	 * Replies the place object which is able to contains the given entity.
	 * 
	 * @param entity
	 * @return a place or <code>null</code> if none could reserve the entity.
	 */
	public abstract P getPlaceObject(ME entity);

	/**
	 * Default implementation of an entity manager. 
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class BufferedEntityManager implements DynamicEntityManager<ME> {

		/** List of entities to insert inside the environment.
		 */
		private List<ME> additions = new LinkedList<ME>();
		
		/** List of entities to remove from the environment.
		 */
		private List<ME> removals = new LinkedList<ME>();

		private final Set<WorldModelActuator<EA,ME>> actuators = new TreeSet<WorldModelActuator<EA,ME>>(
				new ObjectReferenceComparator<WorldModelActuator<EA,ME>>());
		
		/**
		 */
		public BufferedEntityManager() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void commit() {
			List<ME> adds, dels;

			Clock currentTime = getSimulationClock();

			synchronized(this) {
				adds = this.additions;
				this.additions = new LinkedList<ME>();
				dels = this.removals;
				this.removals = new LinkedList<ME>();
			}
					
			P place;
			WorldModelActuator<EA,ME> actuator;

			for(ME toRemove : dels) {
				place = (P)toRemove.getPlace();
				if (place!=null) {
					actuator = place.getWorldModelUpdater();
					if (actuator!=null) {
						actuator.unregisterMobileEntity(toRemove);
						synchronized(this.actuators) {
							this.actuators.add(actuator);
						}
					}
				}
			}

			for(ME toAdd : adds) {
				try {
					place = (P)toAdd.getPlace();
				}
				catch(Throwable _) {
					place = null;
				}
				if (place==null) place = getPlaceObject(toAdd);
				if (place!=null) {
					actuator = place.getWorldModelUpdater();
					if (actuator!=null) {
						actuator.registerMobileEntity(toAdd);
						synchronized(this.actuators) {
							this.actuators.add(actuator);
						}
					}
				}
				else {
					ErrorLogger.logNormalError(this, "unable to find the place where the new entity should be located"); //$NON-NLS-1$
				}
			}

			synchronized(this.actuators) {
				for(WorldModelActuator<EA,ME> a : this.actuators) {
					a.commit();
				}
				this.actuators.clear();
			}

			if (!dels.isEmpty()) fireEntityRemoval(currentTime, dels.size(), dels);
			dels.clear();
			if (!adds.isEmpty()) fireEntityAddition(currentTime, adds.size(), adds);
			adds.clear();
		}
		
		/** Add the given actuator inside the list of the
		 * world model on which {@link #commit} must be invoce.
		 * 
		 * @param a
		 */
		void registerActuatorForCommitment(WorldModelActuator<EA,ME> a) {
			synchronized(this.actuators) {
				this.actuators.add(a);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized void registerMobileEntity(ME entity) {
			this.additions.add(entity);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public synchronized void unregisterMobileEntity(ME entity) {
			this.removals.add(entity);
		}

	}
	
}
