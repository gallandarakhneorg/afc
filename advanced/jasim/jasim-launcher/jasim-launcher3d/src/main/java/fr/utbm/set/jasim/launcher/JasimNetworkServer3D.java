/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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

import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.progress.ProgressionConsoleMonitor;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.JasimConstants;
import fr.utbm.set.jasim.controller.SimulationController3D;
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
import fr.utbm.set.jasim.environment.model.SituatedEnvironment3D;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.ground.HeightmapGround;
import fr.utbm.set.jasim.environment.model.influencereaction.EnvironmentalAction3D;
import fr.utbm.set.jasim.environment.model.place.PlaceDescription;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
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
public class JasimNetworkServer3D
implements JasimSimulationListener<EnvironmentalAction3D,
								   Entity3D<AlignedBoundingBox>,
								   MobileEntity3D<AlignedBoundingBox>>,
		   EnvironmentalProbesListener,
		   EnvironmentalProbeListener,
		   NetworkServerListener {

	/**
	 * @param args are the command line arguments.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		main(Level.WARNING, args);
	}	

	/**
	 * @param logLevel indicates the desired log level.
	 * @param args are the command line arguments.
	 * @throws IOException 
	 */
	public static void main(Level logLevel, String[] args) throws IOException {
		
		// Set the Logging configuration
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
		JasimNetworkServer3D controller = new JasimNetworkServer3D(server);
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
		
		server.closeConnections();

		logger.info("JASIM Simulator killed."); //$NON-NLS-1$
	}

	/**
	 * Controller of the Jasim simulation.
	 */
	protected final SimulationController3D<AlignedBoundingBox,AlignedBoundingBox> controller = new SimulationController3D<AlignedBoundingBox,AlignedBoundingBox>(new JanusControlBinder(), AlignedBoundingBox.class);
	
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
	public JasimNetworkServer3D(NetworkServer networkServer) throws IOException {
		super();
		assert(networkServer!=null);
		this.networkServer = new WeakReference<NetworkServer>(networkServer);
		
		SituatedEnvironment3D<AlignedBoundingBox,AlignedBoundingBox> env
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
	public void entitiesArrived(EntityLifeEvent<MobileEntity3D<AlignedBoundingBox>> event) {
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
	public void entitiesDisappeared(EntityLifeEvent<MobileEntity3D<AlignedBoundingBox>> event) {
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
	public void entityActionsApplied(EntityActionEvent<EnvironmentalAction3D> event) {
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
	public void simulationInitiated(SimulationInitEvent<Entity3D<AlignedBoundingBox>, 
														MobileEntity3D<AlignedBoundingBox>> event) {
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
						3f);
				
				int placeCount = event.getPlaceCount();
				PlaceInfo[] infos = new PlaceInfo[placeCount];
				Ground ground;
				GroundInfo gInfo;
				Collection<MobileEntitySpawningInfo> entities = new ArrayList<MobileEntitySpawningInfo>();
				Iterable<PlaceDescription<Entity3D<AlignedBoundingBox>,MobileEntity3D<AlignedBoundingBox>>> places = event.getPlaces();
				Iterator<PlaceDescription<Entity3D<AlignedBoundingBox>,MobileEntity3D<AlignedBoundingBox>>> placeIterator = places.iterator();
				PlaceDescription<Entity3D<AlignedBoundingBox>,MobileEntity3D<AlignedBoundingBox>> place;
				for(int i=0; placeIterator.hasNext() && i<placeCount; ++i) {
					place = placeIterator.next();
					
					ground = place.getGround();
					
					gInfo = null;
					
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
					else throw new RuntimeException("Unable to find ground information"); //$NON-NLS-1$
					
					entities.clear();
					Iterator<? extends MobileEntity3D<AlignedBoundingBox>> iterator = place.getMobileEntities();
					MobileEntity3D<AlignedBoundingBox> e;
					while (iterator.hasNext()) {
						e = iterator.next();
						entities.add(new MobileEntitySpawningInfo(
								e.getIdentifier(),
								e.getPosition3D(),
								e.getPivotVector(),
								e.getQuaternion()));
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
				SimulationControllerConfiguration<Entity3D<AlignedBoundingBox>,MobileEntity3D<AlignedBoundingBox>> description = 
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
		getLogger().info("Simulator termination requested"); //$NON-NLS-1$
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

		private final Iterator<? extends MobileEntity3D<AlignedBoundingBox>> entityIterator;
		private MobileEntitySpawningInfo next;
		
		/**
		 * @param iterator
		 */
		public InnerSpawningIterator(Iterator<? extends MobileEntity3D<AlignedBoundingBox>> iterator) {
			this.entityIterator = iterator;
			searchNext();
		}
		
		private void searchNext() {
			MobileEntity3D<AlignedBoundingBox> entity;
			
			this.next = null;
			while (this.next==null && this.entityIterator.hasNext()) {
				entity = this.entityIterator.next();
				this.next = new MobileEntitySpawningInfo(
						entity.getIdentifier(),
						entity.getPosition3D(),
						entity.getPivotVector(),
						entity.getQuaternion());
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

		private final Iterator<MobileEntity3D<AlignedBoundingBox>> entityIterator;
		private UUID next;
		
		/**
		 * @param iterator
		 */
		public InnerDeletionIterator(Iterator<MobileEntity3D<AlignedBoundingBox>> iterator) {
			this.entityIterator = iterator;
			searchNext();
		}
		
		private void searchNext() {
			MobileEntity3D<AlignedBoundingBox> entity;
			
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

		private final Iterator<? extends EnvironmentalAction3D> actionIterator;
		private MobileEntityMoveInfo next;
		
		/**
		 * @param iterator
		 */
		public InnerActionIterator(Iterator<? extends EnvironmentalAction3D> iterator) {
			this.actionIterator = iterator;
			searchNext();
		}
		
		private void searchNext() {
			EnvironmentalAction3D action;
			MobileEntity3D<?> entity;
			Transform3D tr;
			
			this.next = null;
			while (this.next==null && this.actionIterator.hasNext()) {
				action = this.actionIterator.next();
				entity = action.getEnvironmentalObject(MobileEntity3D.class);
				tr= action.getTransformation();
				this.next = new MobileEntityMoveInfo(
						entity.getIdentifier(),
						tr.getTranslationVector(),
						tr.getQuaternion(),
						entity.getPosition3D(),
						entity.getQuaternion(),
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