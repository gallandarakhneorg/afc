/*
 * $Id$
 * 
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.launcher;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.progress.ProgressionConsoleMonitor;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.bounds.bounds1d5.BoundingRect1D5;
import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.system.CoordinateSystem2D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.gis.road.primitive.RoadNetwork;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.JasimConstants;
import fr.utbm.set.jasim.controller.SimulationController1D5;
import fr.utbm.set.jasim.controller.SimulationControllerConfiguration;
import fr.utbm.set.jasim.controller.probe.EnvironmentalProbeFactory;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EntityActionEvent;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EntityLifeEvent;
import fr.utbm.set.jasim.environment.interfaces.internalevents.JasimSimulationListener;
import fr.utbm.set.jasim.environment.interfaces.internalevents.SimulationInitEvent;
import fr.utbm.set.jasim.environment.interfaces.internalevents.SimulationStopEvent;
import fr.utbm.set.jasim.environment.interfaces.probes.EnvironmentalProbe;
import fr.utbm.set.jasim.environment.interfaces.probes.EnvironmentalProbeListener;
import fr.utbm.set.jasim.environment.interfaces.probes.EnvironmentalProbes;
import fr.utbm.set.jasim.environment.interfaces.probes.EnvironmentalProbesListener;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment1D5;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.ground.HeightmapGround;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction1D5;
import fr.utbm.set.jasim.environment.model.place.PlaceDescription;
import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.jasim.environment.model.world.MobileEntity1D5;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.jasim.janus.controller.JanusControlBinder;
import fr.utbm.set.jasim.network.NetworkInterfaceConstants;
import fr.utbm.set.jasim.network.data.GroundInfo;
import fr.utbm.set.jasim.network.data.MobileEntityMoveInfo;
import fr.utbm.set.jasim.network.data.MobileEntitySpawningInfo;
import fr.utbm.set.jasim.network.data.PlaceInfo;
import fr.utbm.set.jasim.network.data.ProbeDescription;
import fr.utbm.set.jasim.network.data.ProbeIdentifier;
import fr.utbm.set.jasim.network.data.ProbeInfo;
import fr.utbm.set.jasim.network.data.SimulationConfiguration;
import fr.utbm.set.jasim.network.data.SimulationInfo;
import fr.utbm.set.jasim.network.server.ClientConnection;
import fr.utbm.set.jasim.network.server.NetworkServer;
import fr.utbm.set.jasim.network.server.NetworkServerListener;

/**
 * This class represents Jasim side of the gateway between a Jasim simulation and
 * a remote frontend.
 * 
 * @author $Author: sgalland$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JasimNetworkServer1D5
implements JasimSimulationListener<EnvironmentalAction1D5,
								   Entity1D5<BoundingRect1D5<RoadSegment>>,
								   MobileEntity1D5<BoundingRect1D5<RoadSegment>>>,
								   EnvironmentalProbesListener,
		   EnvironmentalProbeListener,
		   NetworkServerListener {

	/** Default up vector in 3D.
	 */
	static final Vector3d PIVOT = new Vector3d(0., 0., 0.);
	
	/**
	 * Retreive the 3D position and 3D orientation of the given 1.5D
	 * entity.
	 * 
	 * @param entity is the entity concerned by the transformation.
	 * @param previousPosition is the position of the entity BEFORE action application.
	 * @param translation is the last 3d translation applied by the entity.
	 * @param rotation is the last 3d rotation applied by the entity.
	 * @param position is the global 3d position of the entity.
	 * @param orientation is the global 3d orientation of the entity.
	 */
	static void to3D(
			Entity1D5<? extends CombinableBounds1D5<RoadSegment>> entity,
			Point1D5 previousPosition,
			Vector3d translation, Quat4d rotation,
			Point3d position, Quat4d orientation) {
		Point1D5 pos = entity.getPosition1D5();
		RoadSegment segment = (RoadSegment)pos.getSegment();
		Direction2D tangent = new Direction2D();
		CoordinateSystem2D default2dCS = CoordinateSystem2D.getDefaultCoordinateSystem();
		CoordinateSystem3D default3dCS = CoordinateSystem3D.getDefaultCoordinateSystem();
		Point2d pos2d = segment.getGeoLocationForDistance(
							pos.getCurvilineCoordinate(),
							pos.getJuttingDistance(),
							default2dCS,
							tangent);
		Point3d pos3d = default3dCS.fromCoordinateSystem2D(pos2d);
		if ((previousPosition!=null && (translation!=null||rotation!=null))) {
			Vector2d oldTangent = new Vector2d();
			RoadSegment oldSegment = (RoadSegment)previousPosition.getSegment();
			Point2d oldPos2d = oldSegment.getGeoLocationForDistance(
					previousPosition.getCurvilineCoordinate(),
					previousPosition.getJuttingDistance(),
					default2dCS,
					oldTangent);
			Point3d oldPos3d = default3dCS.fromCoordinateSystem2D(oldPos2d);
			if (translation!=null) {
				translation.sub(pos3d, oldPos3d);
			}
			if (rotation!=null) {
				Vector3d upv = default3dCS.getUpVector();
				double angle = GeometryUtil.signedAngle(
						oldTangent.x, oldTangent.y,
						tangent.x, tangent.y);
				rotation.set(upv.x, upv.y, upv.z, angle);
			}
		}
		else {
			if (translation!=null) {
				translation.set(0.,0.,0.);
			}
			if (rotation!=null) {
				Vector3d upv = default3dCS.getUpVector();
				rotation.set(upv.x, upv.y, upv.z, 0.);
			}
		}
		if (position!=null) {
			position.set(pos3d);
		}
		if (orientation!=null) {
			Direction3D aa = GeometryUtil.viewVector2rotation2D(tangent, default3dCS);
			orientation.set(aa);
		}
	}

	/**
	 * @param args are the command line arguments.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		main(Level.WARNING, args);
	}

	/**
	 * @param logLevel is the desired log level.
	 * @param args are the command line arguments.
	 * @throws IOException 
	 */
	public static void main(Level logLevel, String[] args) throws IOException {

		Logger.getAnonymousLogger().setLevel(logLevel);
		
		// Parse command line
		int port = NetworkInterfaceConstants.DEFAULT_SIMULATOR_PORT;
		if (args.length>0) {
			try {
				port = Integer.parseInt(args[1]);
			}
			catch(Exception _) {
				//
			}
		}

		// Launch the network controller
		NetworkServer server = new NetworkServer(port);
		JasimNetworkServer1D5 controller = new JasimNetworkServer1D5(server);
		Logger logger = controller.getLogger();
		
		logger.setLevel(logLevel);
		
		logger.info("JASIM Simulator started."); //$NON-NLS-1$

		while (!controller.mustStop()) {
			try {
				// Do not require additional thread!
				// So run directly the network server.
				server.run();
			}
			catch(Exception e) {
				//
			}
		}
		
		controller.sendKillMessage();

		logger.info("JASIM Simulator killed."); //$NON-NLS-1$
	}

	/**
	 * Controller of the Jasim simulation.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final SimulationController1D5<BoundingRect1D5<RoadSegment>,BoundingRect1D5<RoadSegment>> controller = new SimulationController1D5<BoundingRect1D5<RoadSegment>,BoundingRect1D5<RoadSegment>>(new JanusControlBinder(), (Class)BoundingRect1D5.class);
	
	/** List of probed values.
	 */
	private Map<ProbeIdentifier,Map<String,Object>> probes = new TreeMap<ProbeIdentifier,Map<String,Object>>();
	
	/** Network server.
	 */
	private final WeakReference<NetworkServer> networkServer;
	
	private volatile boolean stop = false;

	/**
	 * Create a socket client and open required IO streams
	 *
	 * @param networkServer is the network server which must be used to send messages.
	 * @throws IOException 
	 */
	public JasimNetworkServer1D5(NetworkServer networkServer) throws IOException {
		super();
		assert(networkServer!=null);
		this.networkServer = new WeakReference<NetworkServer>(networkServer);
		
		SituatedEnvironment1D5<BoundingRect1D5<RoadSegment>,BoundingRect1D5<RoadSegment>> env
			= this.controller.getEnvironment();
		
		networkServer.addNetworkServerListener(this);
		env.addJasimSimulationListener(this);
		env.getProbeManager().addEnvironmentalProbesListener(this);
	}

	/** Replies the logger.
	 * 
	 * @return a logger.
	 */
	protected Logger getLogger() {
		return Logger.getLogger(getClass().getCanonicalName());
	}

	/** Replies if this object must stop its life.
	 * 
	 * @return <code>true</code> if stop, otherwise <code>false</code>
	 */
	public boolean mustStop() {
		return this.stop;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void entitiesArrived(EntityLifeEvent<MobileEntity1D5<BoundingRect1D5<RoadSegment>>> event) {
		NetworkServer server = this.networkServer.get();
		if (server!=null) {
			try {
				server.sendAdditionMessage(
						event.getTime().getSimulationTime(),
						event.getEntityCount(),
						new InnerSpawningIterator(event.getEntities().iterator()));
			}
			catch(IOException e) {
				getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void entitiesDisappeared(EntityLifeEvent<MobileEntity1D5<BoundingRect1D5<RoadSegment>>> event) {
		NetworkServer server = this.networkServer.get();
		if (server!=null) {
			try {
				server.sendDeletionMessage(
						event.getTime().getSimulationTime(),
						event.getEntityCount(),
						new InnerDeletionIterator(event.getEntities().iterator()));
			}
			catch(IOException e) {
				getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void entityActionsApplied(EntityActionEvent<EnvironmentalAction1D5> event) {
		NetworkServer server = this.networkServer.get();
		if (server!=null) {
			try {
				Clock time = event.getTime();
				server.sendActionMessage(
						time.getSimulationTime(),
						time.getSimulationStepDuration(),
						event.getAppliedActionCount(),
						new InnerActionIterator(event.getAppliedActions().iterator()));
			}
			catch(IOException e) {
				getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void simulationIddle(Clock clock) {
		NetworkServer server = this.networkServer.get();
		if (server!=null) {
			try {
				server.sendIddleMessage(clock.getSimulationTime(), clock.getSimulationStepDuration());
			}
			catch(IOException e) {
				getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void simulationInitiated(SimulationInitEvent<Entity1D5<BoundingRect1D5<RoadSegment>>, 
														MobileEntity1D5<BoundingRect1D5<RoadSegment>>> event) {
		NetworkServer server = this.networkServer.get();
		if (server!=null) {
			try {
				SimulationInfo config = new SimulationInfo(
						event.getSimulationRunIdentifier(),
						event.getScenarioName(),
						event.getScenarioDate(),
						event.getScenarioAuthors(),
						event.getScenarioVersion(),
						event.getScenarioDescription(),
						event.getSimulationTimeUnit(),
						JasimConstants.DEFAULT_SPACE_UNIT,
						JasimConstants.DEFAULT_SPEED_UNIT,
						JasimConstants.DEFAULT_ROTATION_UNIT, 
						JasimConstants.DEFAULT_VIEW_VECTOR_X,
						JasimConstants.DEFAULT_VIEW_VECTOR_Y,
						JasimConstants.DEFAULT_VIEW_VECTOR_Z,
						JasimConstants.DEFAULT_UP_VECTOR_X,
						JasimConstants.DEFAULT_UP_VECTOR_Y,
						JasimConstants.DEFAULT_UP_VECTOR_Z,
						JasimConstants.DEFAULT_LEFT_VECTOR_X,
						JasimConstants.DEFAULT_LEFT_VECTOR_Y,
						JasimConstants.DEFAULT_LEFT_VECTOR_Z,
						1.5f);
				
				int placeCount = event.getPlaceCount();
				PlaceInfo[] infos = new PlaceInfo[placeCount];
				Ground ground;
				GroundInfo gInfo;
				Collection<MobileEntitySpawningInfo> entities = new ArrayList<MobileEntitySpawningInfo>();
				Iterable<PlaceDescription<Entity1D5<BoundingRect1D5<RoadSegment>>,MobileEntity1D5<BoundingRect1D5<RoadSegment>>>> places = event.getPlaces();
				Iterator<PlaceDescription<Entity1D5<BoundingRect1D5<RoadSegment>>,MobileEntity1D5<BoundingRect1D5<RoadSegment>>>> placeIterator = places.iterator();
				PlaceDescription<Entity1D5<BoundingRect1D5<RoadSegment>>,MobileEntity1D5<BoundingRect1D5<RoadSegment>>> place;
								
				for(int i=0; placeIterator.hasNext() && i<placeCount; ++i) {
					place = placeIterator.next();
					
					RoadNetwork roadNetwork = place.getWorldModel(RoadNetwork.class);

					gInfo = null;
					

					if (roadNetwork!=null) {
						Bounds2D bounds = roadNetwork.getBoundingBox();
						gInfo = new GroundInfo(
								roadNetwork.getUUID(),
								bounds.getMinX(), bounds.getMinY(), 0.,
								bounds.getMaxX(), bounds.getMaxY(), 0.,
								null);
					}
					else {
						ground = place.getGround();
						
						if (ground!=null) {
							String resourceName = null;						
							if (ground instanceof HeightmapGround) {
								resourceName = ((HeightmapGround)ground).getImageFile().toString();
							}
		
							gInfo = new GroundInfo(
									ground.getIdentifier(),
									ground.getMinX(), ground.getMinY(), ground.getMinZ(),
									ground.getMaxX(), ground.getMaxY(), ground.getMaxZ(),
									resourceName);
						}
						else throw new RuntimeException("unable to find a road nor ground data structure"); //$NON-NLS-1$
					}
					
					entities.clear();
					Iterator<? extends MobileEntity1D5<BoundingRect1D5<RoadSegment>>> iterator = place.getMobileEntities();
					MobileEntity1D5<BoundingRect1D5<RoadSegment>> e;
					while (iterator.hasNext()) {
						e = iterator.next();
						Point3d globalPosition = new Point3d();
						Quat4d globalOrientation = new Quat4d();
						to3D(e, null, null, null, globalPosition, globalOrientation);
						entities.add(new MobileEntitySpawningInfo(
								e.getIdentifier(),
								globalPosition,
								PIVOT,
								globalOrientation));
					}
					
					infos[i] = new PlaceInfo(
							place.getIdentifier(),
							place.getName(),
							gInfo, entities);
				}
				
				server.sendStartMessage(config, infos);
			}
			catch(IOException e) {
				getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void simulationStopped(SimulationStopEvent event) {
		NetworkServer server = this.networkServer.get();
		if (server!=null) {
			try {
				server.sendEndMessage();
			}
			catch(IOException e) {
				getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			if (mustStop())
				server.closeConnectionListener();
		}
	}

	private void sendKillMessage() {
		NetworkServer server = this.networkServer.get();
		if (server!=null) {
			try {
				server.sendKilledMessage();
			}
			catch(IOException e) {
				getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProbeAdded(EnvironmentalProbe probe) {
		probe.addEnvironmentalProbeListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProbeRemoved(EnvironmentalProbe probe) {
		probe.removeEnvironmentalProbeListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProbeChanged(EnvironmentalProbe probe) {
		ProbeIdentifier id = new ProbeIdentifier(
				probe.getPlace(), 
				probe.getProbeName());
		this.probes.put(id, probe.getCollectedValues());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProbesChanged() {
		Map<ProbeIdentifier, Map<String,Object>> pbs = this.probes;
		this.probes = new TreeMap<ProbeIdentifier, Map<String,Object>>();
		if (pbs!=null && !pbs.isEmpty()) {
			NetworkServer server = this.networkServer.get();
			if (server!=null) {
				try {
					server.sendProbeMessage(
							this.controller.getSimulationClock().getSimulationTime(),
							pbs.size(),
							new InnerProbeIterator(pbs.entrySet().iterator()));
				}
				catch(IOException e) {
					getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
			pbs.clear();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAddProbeMessageReceived(ProbeDescription probeDescription) {
		ProbeIdentifier id = probeDescription.getProbeId();
		EnvironmentalProbes manager = this.controller.getEnvironment().getProbeManager();
		if (manager!=null) {
			try {
				EnvironmentalProbe probe = EnvironmentalProbeFactory.newProbe(
						probeDescription.getProbeType(),
						id.getProbeName(), id.getPlaceId(), 
						this.controller, 
						probeDescription.getParameters());
				manager.addProbe(probe);
			}
			catch(Throwable e) {
				getLogger().log(Level.SEVERE,"Can't create probe", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConnexionClosed(ClientConnection client) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConnexionOpened(ClientConnection client) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInitMessageReceived(
			SimulationConfiguration simulationConfiguration) {
		String xmlConfigFileContent = simulationConfiguration.getXMLConfiguration();
		if (xmlConfigFileContent!=null && !"".equals(xmlConfigFileContent)) { //$NON-NLS-1$
			try {
				ProgressionConsoleMonitor monitor = new ProgressionConsoleMonitor();
				Progression progression = monitor.getModel();
				SimulationControllerConfiguration<Entity1D5<BoundingRect1D5<RoadSegment>>,MobileEntity1D5<BoundingRect1D5<RoadSegment>>> description = 
					this.controller.loadSimulationConfiguration(simulationConfiguration.getSearchDirectory(), xmlConfigFileContent,
							progression.subTask(30));
				
				this.controller.initSimulation(description, progression.subTask(70));
			}
			catch (IOException e) {
				getLogger().log(Level.SEVERE,"cannot initialize the controller", e); //$NON-NLS-1$
			}
		}
		else {
			getLogger().severe("configuration file content not specified"); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onKillSimulationReceived() {
		this.stop = true;
		this.controller.stopSimulation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPauseMessageReceived() {
		this.controller.pauseSimulation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPlayMessageReceived() {
		this.controller.playSimulation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRemoveProbeMessageReceived(ProbeIdentifier probeId) {
		EnvironmentalProbes manager = this.controller.getEnvironment().getProbeManager();
		if (manager!=null) {
			manager.removeProbe(probeId.getProbeName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSetSimulationDelayReceived(long delay) {
		this.controller.setSimulationExecutionDelay(delay);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStepMessageReceived() {
		this.controller.stepSimulation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStopMessageReceived() {
		this.controller.stopSimulation();
	}

	/**
	 * @author $Author: sgalland$
	 * @author $Author: rzeo$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class InnerProbeIterator implements Iterator<ProbeInfo> {

		private final Iterator<Entry<ProbeIdentifier,Map<String,Object>>> probeIterator;
		private ProbeInfo next;
		
		/**
		 * @param iterator
		 */
		public InnerProbeIterator(Iterator<Entry<ProbeIdentifier,Map<String,Object>>> iterator) {
			this.probeIterator = iterator;
			searchNext();
		}
		
		private void searchNext() {
			Entry<ProbeIdentifier,Map<String,Object>> entry;
			ProbeIdentifier id;
			
			this.next = null;
			while (this.next==null && this.probeIterator.hasNext()) {
				entry = this.probeIterator.next();
				id = entry.getKey();
				this.next = new ProbeInfo(
						id.getPlaceId(),
						id.getProbeName(),
						entry.getValue());
			}
		}

		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		@Override
		public ProbeInfo next() {
			ProbeInfo info = this.next;
			if (info==null) throw new NoSuchElementException();
			searchNext();
			return info;
		}

		@Override
		public void remove() {
			//
		}
		
	}

	/**
	 * @author $Author: sgalland$
	 * @author $Author: rzeo$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class InnerSpawningIterator implements Iterator<MobileEntitySpawningInfo> {

		private final Iterator<? extends MobileEntity1D5<BoundingRect1D5<RoadSegment>>> entityIterator;
		private MobileEntitySpawningInfo next;
		
		/**
		 * @param iterator
		 */
		public InnerSpawningIterator(Iterator<? extends MobileEntity1D5<BoundingRect1D5<RoadSegment>>> iterator) {
			this.entityIterator = iterator;
			searchNext();
		}
		
		private void searchNext() {
			MobileEntity1D5<BoundingRect1D5<RoadSegment>> entity;
			Point3d globalPosition = new Point3d();
			Quat4d globalOrientation = new Quat4d();
			
			this.next = null;
			while (this.next==null && this.entityIterator.hasNext()) {
				entity = this.entityIterator.next();
				to3D(entity, null, null, null, globalPosition, globalOrientation);
				this.next = new MobileEntitySpawningInfo(
						entity.getIdentifier(),
						globalPosition,
						PIVOT,
						globalOrientation);
			}
		}

		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		@Override
		public MobileEntitySpawningInfo next() {
			MobileEntitySpawningInfo info = this.next;
			if (info==null) throw new NoSuchElementException();
			searchNext();
			return info;
		}

		@Override
		public void remove() {
			//
		}
		
	}

	/**
	 * @author $Author: sgalland$
	 * @author $Author: rzeo$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class InnerDeletionIterator implements Iterator<UUID> {

		private final Iterator<MobileEntity1D5<BoundingRect1D5<RoadSegment>>> entityIterator;
		private UUID next;
		
		/**
		 * @param iterator
		 */
		public InnerDeletionIterator(Iterator<MobileEntity1D5<BoundingRect1D5<RoadSegment>>> iterator) {
			this.entityIterator = iterator;
			searchNext();
		}
		
		private void searchNext() {
			MobileEntity1D5<BoundingRect1D5<RoadSegment>> entity;
			
			this.next = null;
			while (this.next==null && this.entityIterator.hasNext()) {
				entity = this.entityIterator.next();
				this.next = entity.getIdentifier();
			}
		}

		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		@Override
		public UUID next() {
			UUID id = this.next;
			if (id==null) throw new NoSuchElementException();
			searchNext();
			return id;
		}

		@Override
		public void remove() {
			//
		}
		
	}

	/**
	 * @author $Author: sgalland$
	 * @author $Author: rzeo$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class InnerActionIterator implements Iterator<MobileEntityMoveInfo> {

		private final Iterator<? extends EnvironmentalAction1D5> actionIterator;
		private MobileEntityMoveInfo next;
		
		/**
		 * @param iterator
		 */
		public InnerActionIterator(Iterator<? extends EnvironmentalAction1D5> iterator) {
			this.actionIterator = iterator;
			searchNext();
		}
		
		private void searchNext() {
			EnvironmentalAction1D5 action;
			MobileEntity1D5<?> entity;
			
			Point3d globalPosition = new Point3d();
			Quat4d globalOrientation = new Quat4d();
			Vector3d lastTranslation = new Vector3d();
			Quat4d lastRotation = new Quat4d();

			this.next = null;
			while (this.next==null && this.actionIterator.hasNext()) {
				action = this.actionIterator.next();
				entity = action.getEnvironmentalObject(MobileEntity1D5.class);
				to3D(entity,
						action.getPreviousPosition(),
						lastTranslation, lastRotation,
						globalPosition, globalOrientation);
				this.next = new MobileEntityMoveInfo(
						entity.getIdentifier(),
						lastTranslation,
						lastRotation,
						globalPosition,
						globalOrientation,
						entity.getLinearSpeed(),
						entity.getAngularSpeed());
			}
		}

		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		@Override
		public MobileEntityMoveInfo next() {
			MobileEntityMoveInfo info = this.next;
			if (info==null) throw new NoSuchElementException();
			searchNext();
			return info;
		}

		@Override
		public void remove() {
			//
		}
		
	}

}