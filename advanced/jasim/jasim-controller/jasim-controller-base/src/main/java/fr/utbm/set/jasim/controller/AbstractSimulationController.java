/*
 * $Id$
 * 
 * Copyright (c) 2006-09, 2012, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;

import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.locale.Locale;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.bounds.bounds2d.OrientedBoundingRectangle;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.io.filefilter.ImageFileFilter;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.agent.hotpoint.GoalPoint;
import fr.utbm.set.jasim.agent.hotpoint.WayPoint;
import fr.utbm.set.jasim.agent.spawn.SituatedAgentSpawner;
import fr.utbm.set.jasim.controller.event.ControllerEvent;
import fr.utbm.set.jasim.controller.event.SimulationControllerListener;
import fr.utbm.set.jasim.controller.event.SimulationTimeControlEventType;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.interfaces.body.Mesh;
import fr.utbm.set.jasim.environment.interfaces.body.factory.AgentBodyFactory;
import fr.utbm.set.jasim.environment.interfaces.body.factory.BodyDescription;
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.KinematicEntity;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.SteeringEntity;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment;
import fr.utbm.set.jasim.environment.model.SituatedEnvironmentDescription;
import fr.utbm.set.jasim.environment.model.ground.AlignedIndoorGround;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.ground.HeightmapGround;
import fr.utbm.set.jasim.environment.model.ground.OrientedIndoorGround;
import fr.utbm.set.jasim.environment.model.place.DefaultPlaceDescription;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.model.place.PlaceContainer;
import fr.utbm.set.jasim.environment.model.place.PlaceDescription;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.jasim.environment.semantics.BodyType;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.environment.semantics.SemanticFactory;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.jasim.environment.time.ConstantTimeManager;
import fr.utbm.set.jasim.environment.time.RealtimeTimeManager;
import fr.utbm.set.jasim.environment.time.TimeManager;
import fr.utbm.set.jasim.environment.time.TimeManagerEvent;
import fr.utbm.set.jasim.environment.time.TimeManagerListener;
import fr.utbm.set.jasim.io.sfg.EntitySimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.EnvironmentSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.EnvironmentSimulationParameterSet.GroundType;
import fr.utbm.set.jasim.io.sfg.GoalSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.PositionParameter;
import fr.utbm.set.jasim.io.sfg.SimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.SpawnerSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.TimeSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.WayPointSimulationParameterSet;
import fr.utbm.set.jasim.io.sfg.XMLSimulationConfigParser;
import fr.utbm.set.jasim.spawn.GroundBasedSpawnLocation;
import fr.utbm.set.jasim.spawn.SpawnLocation;
import fr.utbm.set.jasim.spawn.Spawner;
import fr.utbm.set.jasim.spawn.SpawningLaw;
import fr.utbm.set.jasim.spawn.StochasticSpawningLaw;
import fr.utbm.set.math.stochastic.StochasticLaw;

/**
 * A simulation controller is an object which permits to launch, stop a simulation.
 * It is also able to dynamically add and remove entities from the simulation.
 * The SimulationController is the entry point and the bottleneck in which
 * all the actions of the simulation's user must pass. 
 *
 * @param <EA> is the type of the supported environmental actions.
 * @param <CB> is the common type of the bounds for static and mobile entities.
 * @param <SB> is the type of the bounds for static entities.
 * @param <SE> is the type of the static entities supported by the simulation.
 * @param <MB> is the type of the bounds for mobile entities.
 * @param <ME> is the type of the mobile entities supported by the simulation.
 * @param <ENV> is the type of the situated environment supported by the simulation.
 * @param <P> is the type of the euclidian points supported by this controller.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSimulationController<EA extends EnvironmentalAction,
CB extends Bounds<?,P,?>,
SB extends CB,
SE extends WorldEntity<SB>, 
MB extends CB,
ME extends MobileEntity<MB>, 
ENV extends SituatedEnvironment<EA,SE,ME,P>,
P> 
implements SimulationController<SE,ME,ENV>, TimeManagerListener {

	/** This is the platform dependent implementation of the controller.
	 */
	private final ControllableAgentPlatform ascBinder;

	/**
	 * The situated environment of the simulation
	 */
	private final ENV environment;

	/**
	 * The manager of the time inside the simulation
	 */
	private TimeManager timeManager;

	/**
	 * The logging system of this class
	 */
	private final Logger simulationLogger;

	/**
	 * The list of spawner used to generate holons in this simulation
	 */
	private final Collection<SpawnLocation> spawnPointList;

	/**
	 * The shared queue of events used to inform holon of the simulation (EnvironmentHolon)
	 */
	private final Queue<ControllerEvent> controlEventQueue;

	/**
	 * Boolean specifying if the simulation is started
	 */
	private boolean isStarted = false;

	/**
	 * Boolean specifying if the simulation is started
	 */
	private boolean isInit = false;

	/**
	 * Boolean specifying if the simulation is paused
	 * It is effectively paused if <code>isStarted</code> is also <tt>true</tt>
	 */
	private boolean isPaused = false;

	/** Listeners.
	 */
	private final Collection<SimulationControllerListener> listeners = new LinkedList<SimulationControllerListener>();

	/** List of time listeners.
	 */
	protected Collection<TimeManagerListener> timeListeners = null;

	/**
	 * Creates a new simulation controller
	 * 
	 * @param binder is the object which is permit to bind the simulation controller to an agent platform.
	 * @param environment is the environment
	 */
	public AbstractSimulationController(ControllableAgentPlatform binder, ENV environment) {
		this(binder, environment, null);
	}

	/**
	 * Creates a new simulation controller
	 * 
	 * @param binder is the object which is permit to bind the simulation controller to an agent platform.
	 * @param environment is the environment
	 * @param timeManager is the time manager
	 */
	protected AbstractSimulationController(ControllableAgentPlatform binder, ENV environment, TimeManager timeManager) {
		assert(binder!=null);
		this.ascBinder = binder;
		this.timeManager = timeManager != null ? timeManager : new RealtimeTimeManager();
		this.environment = environment;
		this.spawnPointList = new LinkedList<SpawnLocation>();
		this.simulationLogger = binder.getLogger();
		this.controlEventQueue = new ConcurrentLinkedQueue<ControllerEvent>();
	}

	/** Replies the logger associated to this controller.
	 * 
	 * @return the logger.
	 */
	public Logger getLogger() {
		return this.simulationLogger;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addSimulationControllerListener(SimulationControllerListener listener) {
		this.listeners.add(listener);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeSimulationControllerListener(SimulationControllerListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Fire the start event.
	 */
	private void fireSimulationStarted(ControllerEvent event) {
		for(SimulationControllerListener listener : this.listeners) {
			listener.simulationStarted(event);
		}
	}

	/**
	 * Fire the pause event.
	 */
	private void fireSimulationPaused(ControllerEvent event) {
		for(SimulationControllerListener listener : this.listeners) {
			listener.simulationPaused(event);
		}
	}

	/**
	 * Fire the simulation execution delay change.
	 */
	private void fireSimulationExecutionDelayChange(ControllerEvent event) {
		for(SimulationControllerListener listener : this.listeners) {
			listener.simulationExecutionDelayChange(event);
		}
	}

	/**
	 * Fire the stop event.
	 */
	private void fireSimulationStopped(ControllerEvent event) {
		for(SimulationControllerListener listener : this.listeners) {
			listener.simulationStopped(event);
		}
	}

	/**
	 * Fire the step event.
	 */
	private void fireSimulationStepped(ControllerEvent event) {
		for(SimulationControllerListener listener : this.listeners) {
			listener.simulationStepped(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTimeListener(TimeManagerListener listener) {
		if ((!this.isInit)||(this.timeManager==null)) {
			if (this.timeListeners==null)
				this.timeListeners = new ArrayList<TimeManagerListener>();
			this.timeListeners.add(listener);
		}
		else {
			this.timeManager.addTimeManagerListener(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeTimeListener(TimeManagerListener listener) {
		if ((!this.isInit)||(this.timeManager==null)) {
			if (this.timeListeners!=null) {
				this.timeListeners.remove(listener);
				if (this.timeListeners.isEmpty())
					this.timeListeners = null;
			}
		}
		if (this.timeManager!=null){
			this.timeManager.removeTimeManagerListener(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ENV getEnvironment() {
		return this.environment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Clock getSimulationClock() {
		return this.timeManager==null ? null : this.timeManager.getClock();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInitialized() {
		return this.isInit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPaused() {
		return this.isStarted && this.isPaused;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlaying() {
		return this.isStarted && !this.isPaused;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStarted() {
		return this.isStarted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStopped() {
		return !this.isStarted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSimulationExecutionDelay(long delay) {
		ControllerEvent event = new ControllerEvent(this, SimulationTimeControlEventType.EXECUTION_DELAY_CHANGE, this.timeManager.getClock(), (delay>0) ? delay : 0);
		this.controlEventQueue.add(event);
		fireSimulationExecutionDelayChange(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pauseSimulation() {
		if (this.isStarted && !this.isPaused) {
			getLogger().info("PAUSED"); //$NON-NLS-1$
			ControllerEvent event;
			this.isPaused = true;
			event = new ControllerEvent(this, SimulationTimeControlEventType.PAUSE, this.timeManager.getClock());
			this.controlEventQueue.add(event);
			fireSimulationPaused(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void playSimulation() {
		if (!this.isStarted || this.isPaused) {
			getLogger().info("PLAY"); //$NON-NLS-1$
			ControllerEvent event;
			if (!this.isStarted) {
				this.ascBinder.startPlatform(this.environment, this.controlEventQueue, this.timeManager);
			}
			this.isStarted = true;
			this.isPaused = false;
			event = new ControllerEvent(this, SimulationTimeControlEventType.START, this.timeManager.getClock());
			this.controlEventQueue.add(event);
			fireSimulationStarted(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stepSimulation() {
		if (!this.isStarted || this.isPaused) {
			getLogger().info("STEP"); //$NON-NLS-1$
			ControllerEvent event;
			if (!this.isStarted) {
				this.ascBinder.startPlatform(this.environment, this.controlEventQueue, this.timeManager);
			}
			this.isStarted = true;
			this.isPaused = true;
			event = new ControllerEvent(this, SimulationTimeControlEventType.ONESTEP, this.timeManager.getClock());
			this.controlEventQueue.add(event);
			fireSimulationStepped(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopSimulation() {
		if (this.isStarted) {
			getLogger().info("STOP"); //$NON-NLS-1$
			ControllerEvent event;
			this.isPaused = this.isStarted = false;
			event = new ControllerEvent(this, SimulationTimeControlEventType.STOP, this.timeManager.getClock());
			this.controlEventQueue.add(event);
			this.ascBinder.killPlatform();
			fireSimulationStopped(event);
		}
		else this.ascBinder.killPlatform();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSimulationClockChanged(TimeManagerEvent event) {
		for(SpawnLocation spawner : this.spawnPointList) {
			spawner.spawn(event.getClock());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean addSpawners(SpawnLocation spawnerPoint) {
		if (this.environment instanceof PlaceContainer) {
			PlaceContainer<? extends Place<EA,SE,ME>,P> placeContainer = (PlaceContainer<? extends Place<EA,SE,ME>,P>)this.environment;
			Place<EA,SE,ME> place = null;

			{
				EuclidianPoint refPoint = spawnerPoint.getReferencePoint();
				place = placeContainer.getPlaceObject(refPoint);
			}

			if (place!=null) {
				if (spawnerPoint instanceof GroundBasedSpawnLocation) {
					Ground grd = place.getGround();
					if (grd!=null) {
						((GroundBasedSpawnLocation)spawnerPoint).setGround(grd);
					}
				}
				for(Spawner spawner : spawnerPoint.getSpawnerList()) {
					if (spawner instanceof SituatedAgentSpawner) {
						SituatedAgentSpawner<MB,ME> sSpawner = (SituatedAgentSpawner<MB,ME>)spawner;
						this.ascBinder.bindSpawner(sSpawner);
					}
				}
				this.spawnPointList.add(spawnerPoint);
				return true;
			}

			getLogger().warning("Cannot add a spawner outside a place"); //$NON-NLS-1$

		}
		else {
			getLogger().warning("Cannot add a spawner inside a non-PlaceContainer environment"); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<? extends SpawnLocation> getSpawners() {
		return Collections.unmodifiableCollection(this.spawnPointList);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void removeSpawners(SpawnLocation spawnerPoint) {
		this.spawnPointList.remove(spawnerPoint);
		for(Spawner spawner : spawnerPoint.getSpawnerList()) {
			if (spawner instanceof SituatedAgentSpawner) {
				SituatedAgentSpawner<MB,ME> sSpawner = (SituatedAgentSpawner<MB,ME>)spawner;
				this.ascBinder.unbindSpawner(sSpawner);
			}
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public final SimulationControllerConfiguration<SE, ME> loadSimulationConfiguration(
			File defaultDirectory, String xmlFileContent,
			Progression taskProgression) throws IOException {
		return loadSimulationConfiguration(
				defaultDirectory.toURI().toURL(), 
				new ByteArrayInputStream(xmlFileContent.getBytes()),
				taskProgression);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public final SimulationControllerConfiguration<SE, ME> loadSimulationConfiguration(
			URL defaultDirectory, String xmlFileContent,
			Progression taskProgression) throws IOException {
		return loadSimulationConfiguration(
				defaultDirectory, 
				new ByteArrayInputStream(xmlFileContent.getBytes()),
				taskProgression);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SimulationControllerConfiguration<SE, ME> loadSimulationConfiguration(
			File absoluteXMLFilePath, Progression taskProgression)
			throws IOException {
		return loadSimulationConfiguration(
				absoluteXMLFilePath.getParentFile().toURI().toURL(),
				new FileInputStream(absoluteXMLFilePath),
				taskProgression);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SimulationControllerConfiguration<SE, ME> loadSimulationConfiguration(
			URL absoluteXMLFilePath, Progression taskProgression)
			throws IOException {
		return loadSimulationConfiguration(
				FileSystem.dirname(absoluteXMLFilePath),
				absoluteXMLFilePath.openStream(),
				taskProgression);
	}

	/** Create an instance of SFG configuration parser.
	 *
	 * @param xmlFileContent is the content of the XML file to parse.
	 * @return a SFG configuration parser.
	 */
	protected abstract XMLSimulationConfigParser createSFGParser(InputStream xmlFileContent);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SimulationControllerConfiguration<SE, ME> loadSimulationConfiguration(
			File defaultDirectory, InputStream xmlFileContent,
			Progression taskProgression) throws IOException {
		return loadSimulationConfiguration(
				defaultDirectory.toURI().toURL(), 
				xmlFileContent, 
				taskProgression);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulationControllerConfiguration<SE, ME> loadSimulationConfiguration(
			URL defaultDirectory, InputStream xmlFileContent,
			Progression taskProgression) throws IOException {
		if (taskProgression!=null) taskProgression.setProperties(0, 0, 50, false);
		try {
			XMLSimulationConfigParser parser = createSFGParser(xmlFileContent);
			parser.setDefaultDirectory(defaultDirectory);
			SimulationParameterSet loadedConfiguration = parser.parse();

			// Create the time manager
			TimeManager timeManagerInstance = this.timeManager;

			TimeSimulationParameterSet time = loadedConfiguration.getTimeManagerParameters();
			switch(time.getType()) {
			case REALTIME:
				timeManagerInstance = new RealtimeTimeManager(time.getTimeUnit());
				break;
			case STEP_BY_STEP:
				timeManagerInstance = new ConstantTimeManager(time.getTimeUnit(),time.getTimeStep());
				break;
			default:
			}

			if (taskProgression!=null) taskProgression.setValue(30);

			SituatedEnvironmentDescription<SE,ME> envDesc;
			{
				Progression subTask = null;
				if (taskProgression!=null) {
					subTask = taskProgression.subTask(20, 0, loadedConfiguration.getEnvironmentParameterCount());
				}

				// Create the environment
				envDesc = new SituatedEnvironmentDescription<SE,ME>(
						loadedConfiguration.getSimulationId(),
						timeManagerInstance);
				envDesc.setAuthors(loadedConfiguration.getSimulationAuthors());
				envDesc.setName(loadedConfiguration.getSimulationName());
				envDesc.setDate(loadedConfiguration.getSimulationDate());
				envDesc.setVersion(loadedConfiguration.getSimulationVersion());
				envDesc.setDescription(loadedConfiguration.getSimulationDescription());

				DefaultPlaceDescription<SE,ME> placeDesc;
				URL prebuiltStatic, prebuiltDynamic;
				int parameterCount = 0;

				for(EnvironmentSimulationParameterSet env : loadedConfiguration.getEnvironmentParameters()) {
					placeDesc = new DefaultPlaceDescription<SE,ME>(env.getPlaceIdentifier());
					placeDesc.setName(env.getPlaceName());
					placeDesc.setPerceptionGeneratorType(env.getPerceptionGeneratorType());

					Ground ground = createGroundFrom(env);
					placeDesc.setGround(ground);

					prebuiltStatic = env.getStaticEnvironmentPrebuiltResource();
					prebuiltDynamic = env.getDynamicEnvironmentPrebuiltResource();

					initializePlaceDescription(prebuiltStatic, prebuiltDynamic, env, placeDesc);

					envDesc.addPlace(placeDesc);

					if (subTask!=null) subTask.setValue(++parameterCount);
				}
			}

			return new SimulationControllerConfiguration<SE,ME>(
					envDesc,
					loadedConfiguration);
		}
		finally {
			if (taskProgression!=null) taskProgression.setValue(50);
		}
	}

	@Override
	public void initSimulation(SimulationControllerConfiguration<SE,ME> desc, Progression taskProgression) {
		if (taskProgression!=null)
			taskProgression.setProperties(0, 0, 100, false);
		Logger logger = getLogger();

		{
			StringBuilder introText = new StringBuilder();		
			introText.append(desc.getIdentifier());
			introText.append(", v"); //$NON-NLS-1$
			introText.append(desc.getVersion());
			introText.append('\n');
			introText.append(desc.getDescription());
			introText.append(" by "); //$NON-NLS-1$
			introText.append(desc.getAuthors());
			introText.append(" the "); //$NON-NLS-1$
			introText.append(desc.getDate().toString());
			logger.info(introText.toString());
		}

		SituatedEnvironmentDescription<SE,ME> envDesc = desc.getSituatedEnvironmentDescription();

		if (taskProgression!=null) taskProgression.setComment(Locale.getString("INIT_TIME_MANAGER")); //$NON-NLS-1$

		this.timeManager = envDesc.getTimeManager();

		if (this.timeManager==null) {
			logger.warning("use default time manager: real time manager"); //$NON-NLS-1$
			this.timeManager = new RealtimeTimeManager();
		}

		if (taskProgression!=null) taskProgression.setValue(11, Locale.getString("INIT_TIME_MANAGER")); //$NON-NLS-1$

		// Initialize the time manager 
		if (this.timeListeners!=null) {
			for(TimeManagerListener listener : this.timeListeners) {
				if (listener!=this) this.timeManager.addTimeManagerListener(listener);
			}
			this.timeListeners.clear();
			this.timeListeners = null;
		}
		this.timeManager.addTimeManagerListener(this);

		if (taskProgression!=null) taskProgression.setValue(12, Locale.getString("INIT_DYNAMIC_STRUCTURES")); //$NON-NLS-1$

		// Create the dynamic perception trees if it was not given 
		/*for(PlaceDescription<SE,ME> placeDesc : envDesc.getPlaces()) {
			initializePlace(placeDesc);
		}*/

		if (taskProgression!=null) taskProgression.setValue(17, Locale.getString("INIT_ENVIRONMENT_ACTIVATION")); //$NON-NLS-1$

		// Initialize the environment 
		Progression subTask = null;
		if (taskProgression!=null) {
			subTask = taskProgression.subTask(60);
		}
		this.environment.activate(envDesc, subTask);

		if (taskProgression!=null) taskProgression.setValue(77, Locale.getString("INIT_STARTUP_AGENTS")); //$NON-NLS-1$

		UUID id;

		// Create agents associated to the dynamic entities.
		for(PlaceDescription<SE,ME> placeDesc : desc.getPlaces()) {
			id = placeDesc.getIdentifier();
			try {
				initializeAgentBinding(
						desc.getDefaultAgentType(id),
						desc.getBodyAgentMap(id),
						desc.getAgentSemanticMap(id),
						placeDesc,
						this.environment);
			}
			catch(IOException e) {
				logger.log(Level.SEVERE, "unable to initialize the dynamic agents", e); //$NON-NLS-1$
			}
		}

		if (taskProgression!=null) taskProgression.setValue(87, Locale.getString("INIT_SPAWNERS")); //$NON-NLS-1$

		// Add the spawners
		for(SpawnerSimulationParameterSet spawn : desc.getSpawners()) {
			try {
				SpawnLocation spawner = initializeSpawner(spawn);
				if (spawner!=null) addSpawners(spawner);
			}
			catch(IOException e) {
				logger.log(Level.SEVERE, "unable to create spawner", e); //$NON-NLS-1$
			}
		}

		if (taskProgression!=null) taskProgression.setValue(100, null);

		this.isInit = true;
	}

	/** Invoked to create an instance of the ground according to the given description.
	 * 
	 * @param description
	 * @return a ground or <code>null</code>
	 * @throws IOException
	 */
	private Ground createGroundFrom(EnvironmentSimulationParameterSet parameters) throws IOException {
		Ground g = null;

		URL prebuilt = parameters.getGroundPrebuiltResource();
		if (prebuilt!=null) {
			g = deserialize(prebuilt, Ground.class);
		}

		if (g==null) {
			GroundType gtype = parameters.getGroundType();
			if (gtype!=GroundType.NONE) {
				Class<? extends Ground> type = gtype.toJavaClass();

				if (AlignedIndoorGround.class.isAssignableFrom(type)) {
					UUID id = parameters.getGroundIdentifier();
					Point3d min = parameters.getGroundMinPoint();
					Point3d max = parameters.getGroundMaxPoint();

					Ground grd = null;

					try {
						Constructor<? extends Ground> cons = type.getConstructor(
								UUID.class,
								double.class,
								double.class,
								double.class,
								double.class,
								double.class);

						grd = cons.newInstance(
								id, 
								min.x, min.y, 
								max.x-min.x, max.y-min.y,
								min.z);
					}
					catch (IllegalArgumentException e) {
						throw new IOException(e);
					}
					catch (InstantiationException e) {
						throw new IOException(e);
					}
					catch (IllegalAccessException e) {
						throw new IOException(e);
					}
					catch (InvocationTargetException e) {
						throw new IOException(e);
					}
					catch (SecurityException e) {
						throw new IOException(e);
					}
					catch (NoSuchMethodException e) {
						throw new IOException(e);
					}

					if (type.isInstance(grd)) g = grd;
				}
				else if (OrientedIndoorGround.class.isAssignableFrom(type)) {
					UUID id = parameters.getGroundIdentifier();
					Point2d center = parameters.getGroundCenterPoint();
					Vector2d R = parameters.getRAxis();
					Vector2d S = parameters.getSAxis();
					Point3d min = parameters.getGroundMinPoint();
					double Rextent = R.length();
					double Sextent = S.length();
					R.normalize();
					S.normalize();

					OrientedBoundingRectangle obr = new OrientedBoundingRectangle(
							center,
							new Vector2d[] {
									R, S
							},
							new double[] {
									Rextent, Sextent
							});
					
					Ground grd = null;

					try {
						Constructor<? extends Ground> cons = type.getConstructor(
								UUID.class,
								OrientedBoundingRectangle.class,
								float.class);

						grd = cons.newInstance(
								id, 
								obr, 
								(float)min.z);
					}
					catch (IllegalArgumentException e) {
						throw new IOException(e);
					}
					catch (InstantiationException e) {
						throw new IOException(e);
					}
					catch (IllegalAccessException e) {
						throw new IOException(e);
					}
					catch (InvocationTargetException e) {
						throw new IOException(e);
					}
					catch (SecurityException e) {
						throw new IOException(e);
					}
					catch (NoSuchMethodException e) {
						throw new IOException(e);
					}

					if (type.isInstance(grd)) g = grd;
				}
				else if (HeightmapGround.class.isAssignableFrom(type)) {
					URL entities = parameters.getGroundHeightResource();
					if (ImageFileFilter.isImageFile(entities)) {
						UUID id = parameters.getGroundIdentifier();
						Point3d min = parameters.getGroundMinPoint();
						Point3d max = parameters.getGroundMaxPoint();

						try {
							Ground grd = null;

							byte bGroundZero;
							if (parameters.isGroundZeroColor()) {
								bGroundZero = parameters.getGroundZeroColor();

								Constructor<? extends Ground> cons = type.getConstructor(
										URL.class,
										UUID.class,
										byte.class,
										Point3d.class, Point3d.class);

								grd = cons.newInstance( entities, id, bGroundZero, min, max);
							}
							else {
								double groundZero = parameters.getGroundZeroMeters();

								Constructor<? extends Ground> cons = type.getConstructor(
										URL.class,
										UUID.class,
										Point3d.class, Point3d.class,
										double.class);

								grd = cons.newInstance( entities, id, min, max, groundZero);
							}

							((HeightmapGround)grd).setImageFile(entities);

							if (type.isInstance(grd)) g = grd;
						}
						catch (IllegalArgumentException e) {
							throw new IOException(e);
						}
						catch (InstantiationException e) {
							throw new IOException(e);
						}
						catch (IllegalAccessException e) {
							throw new IOException(e);
						}
						catch (InvocationTargetException e) {
							throw new IOException(e);
						}
						catch (SecurityException e) {
							throw new IOException(e);
						}
						catch (NoSuchMethodException e) {
							throw new IOException(e);
						}
					}
					else {
						throw new IOException("Invalid resource type for ground: only image is allowed"); //$NON-NLS-1$
					}
				}
				else {
					throw new IOException("Invalid ground type: only indoor and heightmap are allowed"); //$NON-NLS-1$
				}
			}
		}

		return g;
	}

	/** Deserialize an object of the given type from the specified url.
	 * 
	 * @param <T> is the type of the deserialized resource
	 * @param url is the URL of the resource to deserialize
	 * @param type is the type of the deserialized resource
	 * @return the deserialized data or <code>null</code> if deserialization is impossible.
	 */
	protected final <T> T deserialize(URL url, Class<T> type) {
		try {
			ObjectInputStream ois = new ObjectInputStream(url.openStream());
			Object obj = ois.readObject();
			if (type.isInstance(obj)) {
				ois.close();
				return type.cast(obj);
			}
			ois.close();
		}
		catch(Exception e) {
			getLogger().log(Level.SEVERE, "unable to deserialize "+url.toString(),e); //$NON-NLS-1$
		}
		return null;
	}

	/** Create a spawner from the given configuration.
	 * 
	 * @param parameters
	 * @return the tree or <code>null</code>
	 * @throws IOException 
	 */
	private SpawnLocation initializeSpawner(SpawnerSimulationParameterSet parameters) throws IOException {
		SpawnLocation spawningLocation = createSpawner(parameters);
		if (spawningLocation!=null) {
			PositionParameter spawnerPosition = parameters.getPosition();
			for(EntitySimulationParameterSet entityParameters : parameters.getEntities()) {
				try {
					SpawningLaw lawToUse = null;
					Class<? extends StochasticLaw> stochasticGenerationLawType = entityParameters.getStochasticGenerationLawType();

					if (stochasticGenerationLawType!=null) {
						Constructor<? extends StochasticLaw> cons = stochasticGenerationLawType.getConstructor(Map.class);
						StochasticLaw law = cons.newInstance(entityParameters.getGenerationLawParameters());
						if (law!=null) lawToUse = new StochasticSpawningLaw(law);
					}
					else {
						Class<? extends SpawningLaw> spawningGenerationLawType = entityParameters.getSpawningGenerationLawType();

						if (spawningGenerationLawType!=null) {
							Constructor<? extends SpawningLaw> cons = spawningGenerationLawType.getConstructor(Map.class);
							SpawningLaw law = cons.newInstance(entityParameters.getGenerationLawParameters());
							if (law!=null) lawToUse = law;
						}
					}

					if (lawToUse!=null) {
						Class<? extends SituatedAgent<?,?,?>> agent = entityParameters.getAgentType();

						List<WayPoint> waypoints = new ArrayList<WayPoint>(entityParameters.getWaypointCount());
						{
							WayPoint wp;
							for(int i=0; i<entityParameters.getWaypointCount(); ++i) {
								wp = createWayPoint(entityParameters.getWaypointAt(i));
								if (wp!=null) waypoints.add(wp);
							}
						}

						List<GoalPoint> goals = new ArrayList<GoalPoint>(entityParameters.getGoalCount());
						{
							GoalPoint gp;
							for(int i=0; i<entityParameters.getGoalCount(); ++i) {
								gp = createGoalPoint(entityParameters.getGoalAt(i));
								if (gp!=null) goals.add(gp);
							}
						}

						SituatedAgentSpawner<MB,ME> spawner = null;

						BodyDescription bodyDescription = entityParameters.getBodyDescription();

						if (entityParameters.isInfiniteBudget())
							spawner = this.ascBinder.createSpawner(
									agent,
									bodyDescription,
									entityParameters.getFrustumDescriptions(),
									this.environment,
									spawnerPosition.getPlace());
						else
							spawner = this.ascBinder.createSpawner(
									agent,
									bodyDescription,
									entityParameters.getFrustumDescriptions(),
									this.environment,
									spawnerPosition.getPlace(),
									entityParameters.getBudget());

						if (spawner!=null) {
							spawner.setWayPoints(waypoints);
							spawner.setGoals(goals);
							spawner.setAttributes(entityParameters.getAttributes());

							spawningLocation.addSpawningLaw(spawner, lawToUse);
						}
					}
				}
				catch(Exception e) {
					throw new IOException(e);
				}
			}

			if (spawningLocation.getSpawnerList().isEmpty()) return null;
		}
		return spawningLocation;
	}

	/** Bind agent instances to each AgentBody from the given list of entities.
	 * 
	 * @param <PER> is the type of the agent's perceptions.
	 * @param <INF> is the type of the agent's influences.
	 * @param defaultType is the default type for the agents.
	 * @param agentMap specifies the type of agent to create when encountered a given type (semantic or body).
	 * @param semanticMap specified which semantic to associate to a type of agent.
	 * @param entityIterator permits to obtain all the dynamic entities from the world.
	 * @throws IOException
	 * @see #initializeAgentBinding(Class, Map, Map, PlaceDescription, SituatedEnvironment)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final <INF extends Influence, PER extends Perception> void bindAgentsNow(
			Class<? extends SituatedAgent<?,?,?>> defaultType,
					Map<Class<?>, Class<? extends SituatedAgent<?,?,?>>> agentMap,
					Map<Class<? extends SituatedAgent<?,?,?>>,
							Collection<Class<? extends Semantic>>> semanticMap,
							Iterator<? extends ME> entityIterator) throws IOException {
		Class<? extends SituatedAgent> agentType = null; 
		ME entity;
		AgentBody<INF,PER> body;
		Semantic semantic;
		SituatedAgent<?,INF,PER> agent;
		while (entityIterator.hasNext()) {
			entity = entityIterator.next();
			if (entity instanceof AgentBody) {
				body = (AgentBody<INF,PER>)entity;
				agent = null;
				semantic = entity.getType();
				if (semantic!=null) {
					agentType = (agentMap!=null) ? agentMap.get(semantic.getClass()) : null;
					if (agentType!=null) {
						try {
							agent = agentType.newInstance();
						}
						catch(Exception e) {
							throw new IOException(e);
						}
					}
				}
				if (agent==null) {
					agentType = (agentMap!=null) ? agentMap.get(body.getClass()) : null;
					if (agentType==null && defaultType!=null) agentType = defaultType;
					if (agentType!=null) {
						try {
							agent = agentType.newInstance();
						}
						catch(Exception e) {
							throw new IOException(e);
						}
					}
				}

				if (agent!=null) {
					// Add additional semantics
					if (semanticMap!=null) {
						Collection<Class<? extends Semantic>> semantics = semanticMap.get(agentType);
						if (semantics!=null) {
							for(Class<? extends Semantic> semanticClass : semantics) {
								try {
									Semantic sem = SemanticFactory.newInstance(semanticClass);
									if (sem!=null) entity.addSemantic(sem);
								}
								catch(Throwable _) {
									//
								}
							}
						}
					}
					// Bind the body
					agent.createBody(new IddleBodyFactory(body));
					this.ascBinder.launchAgent(agent);
				}
			}
		}
	}

	/** Invoked to update the environment description for a place.
	 * 
	 * @param prebuiltStatic is the URL of the prebuilt resource for static perceptions.
	 * @param prebuiltDynamic is the URL of the prebuilt resource for dynamic perceptions.
	 * @param environmentDescrption describes the environment in which the place is located
	 * @param placeDescription is the description to update.
	 */
	protected abstract void initializePlaceDescription(
			URL prebuiltStatic, 
			URL prebuiltDynamic,
			EnvironmentSimulationParameterSet environmentDescrption,
			PlaceDescription<SE,ME> placeDescription);

	/**
	 * Invoked to initialize the agents at start-up.
	 * 
	 * @param defaultType is the default type of the agents.
	 * @param agentMap is a predefined mapping from body type (Semantic) to agent type.
	 * @param semanticMap are the predefined semantics to associate to agents' bodies.
	 * @param placeDescription is the place that contains the agents to initialize.
	 * @param environmentInstance is the environment that contains the agents to initialize.
	 * @throws IOException
	 * @see #bindAgentsNow(Class, Map, Map, Iterator)
	 */
	protected abstract void initializeAgentBinding(
			Class<? extends SituatedAgent<?,?,?>> defaultType,
					Map<Class<?>, Class<? extends SituatedAgent<?,?,?>>> agentMap,
					Map<Class<? extends SituatedAgent<?,?,?>>, Collection<Class<? extends Semantic>>> semanticMap,
							PlaceDescription<SE,ME> placeDescription,
							ENV environmentInstance) throws IOException;

	/** Invoked to create the instance of a spawner.
	 * 
	 * @param description is the description of the spawner to create.
	 * @return the spawner or <code>null</code>
	 * @throws IOException
	 */
	protected abstract SpawnLocation createSpawner(SpawnerSimulationParameterSet description) throws IOException;

	/** Invoked to create a way point.
	 * 
	 * @param description
	 * @return a way point instance.
	 */
	protected abstract WayPoint createWayPoint(WayPointSimulationParameterSet description);

	/** Invoked to create a goal point.
	 * 
	 * @param description
	 * @return a goal point instance.
	 */
	protected abstract GoalPoint createGoalPoint(GoalSimulationParameterSet description);

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class IddleBodyFactory implements AgentBodyFactory {

		private final AgentBody<?,?> body;

		/**
		 * @param body
		 */
		public IddleBodyFactory(AgentBody<?,?> body) {
			this.body = body;
		}

		@Override
		public Frustum<?, ?, ?> attachCircularFrustum(AgentBody<?, ?> bodyToChange,
				double eyeVerticalPositionFromGround, double radius) {
			return null;
		}

		@Override
		public Frustum<?, ?, ?> attachPedestrianFrustum(AgentBody<?, ?> bodyToChange,
				double eyeVerticalPositionFromGround, double nearDistance,
				double farDistance, double horizontalOpennessAngle,
				double verticalOpennessAngle) {
			return null;
		}

		@Override
		public Frustum<?, ?, ?> attachRectangularFrustum(AgentBody<?, ?> bodyToChange,
				double eyeVerticalPositionFromGround, double farDistance,
				double leftRightDistance, double upDownDistance) {
			return null;
		}

		@Override
		public Frustum<?, ?, ?> attachTriangularFrustum(AgentBody<?, ?> bodyToChange,
				double eyeVerticalPositionFromGround, double nearDistance,
				double farDistance, double horizontalOpennessAngle,
				double verticalOpennessAngle) {
			return null;
		}

		@Override
		public void attachDefaultFrustums(AgentBody<?, ?> bodyToChange) {
			//			
		}

		@Override
		public void detachAllFrustums(AgentBody<?, ?> bodyToChange) {
			throw new UnsupportedOperationException();
		}

		@Deprecated
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createAgentBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				BodyType bodyType, double forwardSpeed, double backwardSpeed,
				double angularSpeed, double maxLinearAcceleration,
				double maxLinearDeceleration, double maxAngularAcceleration,
				double maxAngularDeceleration) {
			return createDefaultBody(agent, perceptionType, bodyType,
					forwardSpeed, backwardSpeed, angularSpeed,
					maxLinearAcceleration, maxLinearDeceleration,
					maxAngularAcceleration, maxAngularDeceleration);
		}

		@Deprecated
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createAgentBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				BodyType bodyType, Collection<? extends Semantic> semantics,
				double forwardSpeed, double backwardSpeed,
				double angularSpeed, double maxLinearAcceleration,
				double maxLinearDeceleration, double maxAngularAcceleration,
				double maxAngularDeceleration) {
			return createDefaultBody(agent, perceptionType, bodyType, semantics, 
					forwardSpeed, backwardSpeed, angularSpeed, maxLinearAcceleration, 
					maxLinearDeceleration, maxAngularAcceleration, maxAngularDeceleration);
		}

		@Deprecated
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createAgentBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				BodyType bodyType,
				double forwardSpeed, double backwardSpeed,
				double angularSpeed) {
			return createDefaultBody(agent, perceptionType, bodyType,
					forwardSpeed, backwardSpeed, angularSpeed);
		}

		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				BodyType bodyType, double forwardSpeed, double backwardSpeed,
				double angularSpeed, double maxLinearAcceleration,
				double maxLinearDeceleration, double maxAngularAcceleration,
				double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				BodyType bodyType, double forwardSpeed,
				double angularSpeed, double maxLinearAcceleration,
				double maxLinearDeceleration, double maxAngularAcceleration,
				double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				BodyType bodyType, Collection<? extends Semantic> semantics,
				double forwardSpeed, double backwardSpeed,
				double angularSpeed, double maxLinearAcceleration,
				double maxLinearDeceleration, double maxAngularAcceleration,
				double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				BodyType bodyType, Collection<? extends Semantic> semantics,
				double forwardSpeed,
				double angularSpeed, double maxLinearAcceleration,
				double maxLinearDeceleration, double maxAngularAcceleration,
				double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				BodyType bodyType,
				double forwardSpeed, double backwardSpeed,
				double angularSpeed) {
			if (this.body instanceof KinematicEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createDefaultBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				BodyType bodyType,
				double forwardSpeed,
				double angularSpeed) {
			if (this.body instanceof KinematicEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius,
				BodyType bodyType, Collection<? extends Semantic> semantics,
				double forwardSpeed, double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius,
				BodyType bodyType, Collection<? extends Semantic> semantics,
				double forwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double backwardSpeed,
				double angularSpeed) {
			if (this.body instanceof KinematicEntity) return (AgentBody<I,P>)this.body;
			return null;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed,
				double angularSpeed) {
			if (this.body instanceof KinematicEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				UUID bodyId,
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed, double backwardSpeed,
				double angularSpeed) {
			if (this.body instanceof KinematicEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createPedestrianBody(
				UUID bodyId,
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyHeight, double bodyRadius, BodyType bodyType,
				double forwardSpeed,
				double angularSpeed) {
			if (this.body instanceof KinematicEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createVehicleBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyLength, double bodyLateralSize, BodyType bodyType,
				double forwardSpeed, double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createVehicleBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyLength, double bodyLateralSize,
				BodyType bodyType, Collection<? extends Semantic> semantics,
				double forwardSpeed, double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createVehicleBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				double bodyLength, double bodyLateralSize, BodyType bodyType,
				double forwardSpeed, double backwardSpeed,
				double angularSpeed) {
			if (this.body instanceof KinematicEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType,
				Collection<? extends Semantic> semantics, double forwardSpeed,
				double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType,
				Collection<? extends Semantic> semantics, double forwardSpeed,
				double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType, double forwardSpeed,
				double backwardSpeed, double angularSpeed) {
			if (this.body instanceof KinematicEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType, double forwardSpeed,
				double angularSpeed) {
			if (this.body instanceof KinematicEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Deprecated
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType, double forwardSpeed,
				double backwardSpeed, double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public <I extends Influence, P extends Perception> AgentBody<I, P> createMeshedBody(
				SituatedAgent<?, I, P> agent, Class<P> perceptionType,
				Mesh<?> mesh, BodyType bodyType, double forwardSpeed,
				double angularSpeed,
				double maxLinearAcceleration, double maxLinearDeceleration,
				double maxAngularAcceleration, double maxAngularDeceleration) {
			if (this.body instanceof SteeringEntity) return (AgentBody<I,P>)this.body;
			return null;
		}

	}

}

