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
package fr.utbm.set.jasim.network.server;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Logger;

import org.arakhne.afc.vmutil.ThreadServiceFinder;

import fr.utbm.set.jasim.network.AbstractJaSIMNetworkConnection;
import fr.utbm.set.jasim.network.NetworkMessageType;
import fr.utbm.set.jasim.network.data.MobileEntityMoveInfo;
import fr.utbm.set.jasim.network.data.MobileEntitySpawningInfo;
import fr.utbm.set.jasim.network.data.PlaceInfo;
import fr.utbm.set.jasim.network.data.ProbeDescription;
import fr.utbm.set.jasim.network.data.ProbeIdentifier;
import fr.utbm.set.jasim.network.data.ProbeInfo;
import fr.utbm.set.jasim.network.data.SimulationConfiguration;
import fr.utbm.set.jasim.network.data.SimulationInfo;

/**
 * Network server for the JaSIM simulator.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NetworkServer extends AbstractJaSIMNetworkConnection implements Runnable {

	private volatile boolean listenConnections = true;
	private final ServerSocket serverSocket;
	private Collection<ClientConnectionThread> connections = new ArrayList<ClientConnectionThread>();
	private final Collection<NetworkServerListener> listeners = new ArrayList<NetworkServerListener>();

	/**
	 * @throws IOException
	 */
	public NetworkServer() throws IOException {
		this(DEFAULT_SIMULATOR_PORT);
	}

	/**
	 * @param port is the network port on which the JaSIM server is listening new connections.
	 * @throws IOException
	 */
	public NetworkServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		getLogger().info("JaSIM NetworkServer listening on port: "+port); //$NON-NLS-1$
	}
	
	/** Replies the logger.
	 * 
	 * @return a logger.
	 */
	protected static Logger getLogger() {
		return Logger.getAnonymousLogger();
	}
	
	/** Create and replies a client connection.
	 * 
	 * @param clientSocket
	 * @return a client connection.
	 * @throws IOException
	 */
	protected ClientConnectionThread createClientConnection(Socket clientSocket) throws IOException {
		return new ClientConnectionThread(clientSocket);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		Logger logger = getLogger();
		Socket clientSocket;
		ClientConnectionThread con;
		this.listenConnections = true;
		while(this.listenConnections) {
			try {
				clientSocket = this.serverSocket.accept();
				logger.info("Accepted a connection from: "+ clientSocket.getInetAddress()); //$NON-NLS-1$
				con = createClientConnection(clientSocket);
				ThreadServiceFinder.getProvider().getExecutorService().execute(con);
				synchronized(this.connections) {
					Collection<ClientConnectionThread> col = new ArrayList<ClientConnectionThread>(this.connections);
					col.add(con);
					this.connections = col;
				}
			}
			catch(Exception e) {
				if (this.listenConnections) {
					getLogger().severe("cannot accept client connection: "+e); //$NON-NLS-1$
					this.listenConnections = false;
				}
			}			
		}
	}

	/** Stop to listen on new connections.
	 */
	public void closeConnectionListener() {
		this.listenConnections = false;
		try {
			this.serverSocket.close();
		}
		catch (IOException _) {
			//
		}
	}
	
	/**
	 * Close all clientSocket connection supported by this server
	 */
	public void closeConnections() {
		try {
			this.serverSocket.close();
		}
		catch(IOException e) {
			getLogger().severe("cannot close the server: "+e); //$NON-NLS-1$
		}
		synchronized(this.connections) {
			Collection<ClientConnectionThread> oldCol = this.connections;
			Collection<ClientConnectionThread> col = new ArrayList<ClientConnectionThread>();
			this.connections = col;
			for(ClientConnectionThread con : oldCol) {
				con.closeConnection();
			}
		}
	}
	
	/**
	 * Returns a boolean specifying if the connection with the simulator is created or not
	 * @return a boolean specifying if the connection with the simulator is created or not
	 */
	public boolean isClientConnectionEstablished() {
		synchronized(this.connections) {
			return !this.connections.isEmpty();
		}
	}

	/**
	 * Returns the clientSocket connections with the simulator if it is created
	 * @return the clientSocket connection if it is created, never <code>null</code>
	 */
	public Iterable<? extends ClientConnection> getClientConnections() {
		synchronized(this.connections) {
			return Collections.unmodifiableCollection(this.connections);
		}
	}

	/** Remove the specified connection from the list of available connections.
	 * 
	 * @param con
	 */
	void removeClientConnection(ClientConnectionThread con) {
		synchronized(this.connections) {
			Collection<ClientConnectionThread> col = new ArrayList<ClientConnectionThread>(this.connections);
			col.remove(con);
			this.connections = col;
		}
	}
	
	/** Add a listener on the server events.
	 *
	 * @param listener
	 */
	public void addNetworkServerListener(NetworkServerListener listener) {
		synchronized(this.listeners) {
			this.listeners.add(listener);
		}
	}
	
	/** Add a listener on the server events.
	 *
	 * @param listener
	 */
	public void removeNetworkServerListener(NetworkServerListener listener) {
		synchronized(this.listeners) {
			this.listeners.remove(listener);
		}
	}

	/** Notifies the listeners about a client connection.
	 * 
	 * @param client
	 */
	void fireConnexionEstablished(ClientConnectionThread client) {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onConnexionOpened(client);
			}
		}
	}

	/** Notifies the listeners about a client disconnection.
	 * 
	 * @param client
	 */
	void fireConnexionClosed(ClientConnectionThread client) {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onConnexionClosed(client);
			}
		}
	}

	/** Notifies the listeners about the arrival of an INIT message.
	 * 
	 * @param simulationConfiguration
	 */
	void fireInitMessage(SimulationConfiguration simulationConfiguration) {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onInitMessageReceived(simulationConfiguration);
			}
		}
	}

	/** Notifies the listeners about the arrival of a PLAY message.
	 */
	void firePlayMessage() {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onPlayMessageReceived();
			}
		}
	}

	/** Notifies the listeners about the arrival of a STEP message.
	 */
	void fireStepMessage() {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onStepMessageReceived();
			}
		}
	}

	/** Notifies the listeners about the arrival of a PAUSE message.
	 */
	void firePauseMessage() {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onPauseMessageReceived();
			}
		}
	}

	/** Notifies the listeners about the arrival of a STOP message.
	 */
	void fireStopMessage() {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onStopMessageReceived();
			}
		}
	}

	/** Notifies the listeners about the arrival of a ADD_PROBE message.
	 * 
	 * @param probeDescription
	 */
	void fireAddProbeMessage(ProbeDescription probeDescription) {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onAddProbeMessageReceived(probeDescription);
			}
		}
	}

	/** Notifies the listeners about the arrival of a REMOVE_PROBE message.
	 * 
	 * @param probeId
	 */
	void fireRemoveProbeMessage(ProbeIdentifier probeId) {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onRemoveProbeMessageReceived(probeId);
			}
		}
	}

	/** Notifies the listeners about the arrival of a SET_SIMULATION_DELAY message.
	 * 
	 * @param delay is the delay for the simulator to wait (in milliseconds).
	 */
	void fireSetSimulationDelayMessage(long delay) {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onSetSimulationDelayReceived(delay);
			}
		}
	}

	/** Notifies the listeners about the arrival of a KILL_SIMULATION message.
	 */
	void fireKillSimulationMessage() {
		synchronized(this.listeners) {
			for(NetworkServerListener listener : this.listeners) {
				listener.onKillSimulationReceived();
			}
		}
	}

	/**
	 * Send START message.
	 * 
	 * @param config is the simulation configuration to send.
	 * @param places are the descriptions of the places in the simulation.
	 * @throws IOException
	 */
	public void sendStartMessage(SimulationInfo config, PlaceInfo... places) throws IOException {
		synchronized(this.connections) {
			byte[] bytes;
			
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				this.outputStream = new DataOutputStream(baos);
				writeStartMessage(config, places);
				baos.close();
				bytes = baos.toByteArray();
			}
			
			for(ClientConnectionThread client : this.connections) {
				if (client.getRemoteClientType().isViewer()) {
					client.sendBytes(bytes);
				}
			}
		}
	}

	/**
	 * Send the END message to the remote vewier.
	 * 
	 * @throws IOException
	 */
	public void sendEndMessage() throws IOException {
		synchronized(this.connections) {
			for(ClientConnectionThread client : this.connections) {
				if (client.getRemoteClientType().isViewer()) {
					client.sendEndMessage();
				}
			}
		}
	}

	/**
	 * Send the ACTION message to the remote vewier.
	 *
	 * @param time is the simulation time at which the actions are done.
	 * @param duration is the duration of the last simulation step.
	 * @param actionCount is the count of actions replied by the iterator.
	 * @param entityMoves
	 * @throws IOException
	 */
	public void sendActionMessage(double time, double duration, int actionCount, Iterator<MobileEntityMoveInfo> entityMoves) throws IOException {
		synchronized(this.connections) {
			byte[] bytes;
			
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				this.outputStream = new DataOutputStream(baos);
				writeMoveActionMessage(time, duration, actionCount, entityMoves);
				baos.close();
				bytes = baos.toByteArray();
			}
			
			for(ClientConnectionThread client : this.connections) {
				if (client.getRemoteClientType().isViewer()) {
					client.sendBytes(bytes);
				}
			}
		}
	}

	/**
	 * Send the IDDLE message to the remote vewier.
	 *
	 * @param time is the simulation time at which the actions are done.
	 * @param duration is the duration of the last simulation step.
	 * @throws IOException
	 */
	public void sendIddleMessage(double time, double duration) throws IOException {
		synchronized(this.connections) {
			byte[] bytes;
			
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				this.outputStream = new DataOutputStream(baos);
				writeIddleMessage(time, duration);
				baos.close();
				bytes = baos.toByteArray();
			}
			
			for(ClientConnectionThread client : this.connections) {
				if (client.getRemoteClientType().isViewer()) {
					client.sendBytes(bytes);
				}
			}
		}
	}

	/**
	 * Send the ADDITION message to the remote vewier.
	 *
	 * @param time is the simulation time at which the addition was done.
	 * @param entityCount is the count of entities replied by the iterator.
	 * @param entities
	 * @throws IOException
	 */
	public void sendAdditionMessage(double time, int entityCount, Iterator<MobileEntitySpawningInfo> entities) throws IOException {
		synchronized(this.connections) {
			byte[] bytes;
			
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				this.outputStream = new DataOutputStream(baos);
				writeAdditionMessage(time, entityCount, entities);
				baos.close();
				bytes = baos.toByteArray();
			}
			
			for(ClientConnectionThread client : this.connections) {
				if (client.getRemoteClientType().isViewer()) {
					client.sendBytes(bytes);
				}
			}
		}
	}

	/**
	 * Send the DELETION message to the remote vewier.
	 *
	 * @param time is the simulation time at which the deletion was done.
	 * @param entityCount is the count of entities replied by the iterator.
	 * @param entityId are the identifiers of the entity.
	 * @throws IOException
	 */
	public void sendDeletionMessage(double time, int entityCount, Iterator<UUID> entityId) throws IOException {
		synchronized(this.connections) {
			byte[] bytes;
			
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				this.outputStream = new DataOutputStream(baos);
				writeDeletionMessage(time, entityCount, entityId);
				baos.close();
				bytes = baos.toByteArray();
			}
			
			for(ClientConnectionThread client : this.connections) {
				if (client.getRemoteClientType().isViewer()) {
					client.sendBytes(bytes);
				}
			}
		}
	}

	/**
	 * Send the PROBE message to the remote vewier.
	 *
	 * @param time is the simulation time at which the deletion was done.
	 * @param probeCount is the count of probes replied by the iterator.
	 * @param probes are the probed values to send
	 * @throws IOException
	 */
	public void sendProbeMessage(double time, int probeCount, Iterator<ProbeInfo> probes) throws IOException {
		synchronized(this.connections) {
			byte[] bytes;
			
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				this.outputStream = new DataOutputStream(baos);
				writeProbeMessage(time, probeCount, probes);
				baos.close();
				bytes = baos.toByteArray();
			}
			
			for(ClientConnectionThread client : this.connections) {
				if (client.getRemoteClientType().isViewer()) {
					client.sendBytes(bytes);
				}
			}
		}
	}

	/**
	 * Send the KILLED message to the remote vewier. If necessary the END message
	 * will be sent just before the KILLED message.
	 *
	 * @throws IOException
	 */
	public void sendKilledMessage() throws IOException {
		synchronized(this.connections) {
			for(ClientConnectionThread client : this.connections) {
				if (client.getRemoteClientType().isViewer()) {
					client.sendKilledMessage();
				}
			}
		}
	}

	/**
	 * This class is in charge on managing a socket-based connection between
	 * the simulator side and the remote clients
	 * 
	 * @author $Author: sgalland$
	 * @author $Author: ngaud$
	 * @author $Author: rzeo$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected class ClientConnectionThread extends AbstractJaSIMNetworkConnection implements ControllerClientConnection, ViewerClientConnection, Runnable {

		private final Socket client;
		private ClientConnectionType type = null;
		private volatile boolean stop = false;
		private boolean needEndMsg = false;
		
		/**
		 * Create a clientSocket connection thread according to the specified socket
		 * @param clientSocket
		 * @throws IOException 
		 */
		ClientConnectionThread(Socket clientSocket) throws IOException {
			assert(clientSocket!=null);
			this.client = clientSocket;
			this.inputStream = new DataInputStream(this.client.getInputStream());
			this.outputStream = null;
			getLogger().info("Client connection established from "+clientSocket.getRemoteSocketAddress().toString()); //$NON-NLS-1$
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public ClientConnectionType getRemoteClientType() {
			return this.type;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public void run() {
			try {
				getLogger().info("Client connection is running"); //$NON-NLS-1$
				fireConnexionEstablished(this);
				boolean cont = true;
				while(!this.stop && cont){
					if (this.type!=null) {
						switch(this.type) {
						case BOTH:
						case CONTROLLER:
							cont = listenController();
							break;
						case VIEWER:
							cont = listenViewer();
							break;
						default:
							cont = listenForPresentation();
						}
					}
					else {
						cont = listenForPresentation();
					}
					Thread.yield();
				}
			}	
			catch(Throwable e) {
				getLogger().severe("Client connection lost: "+e); //$NON-NLS-1$
			}

			try {
				// close streams and connections
				if (this.inputStream!=null) {
					this.inputStream.close();
					this.inputStream = null;
				}
				if (this.outputStream!=null) {
					this.outputStream.close();
					this.outputStream = null;
				}
				this.client.close();
			}
			catch(Exception e) {
				getLogger().severe("cannot close connection: "+e); //$NON-NLS-1$
			}
			
			NetworkServer.this.removeClientConnection(this);
			
			fireConnexionClosed(this);
		}		

		/** Tries to detect the IAMVIEWER or IAMCONTROLLER messages.
		 */
		private boolean listenForPresentation() throws IOException {
			NetworkMessageType messageType = readMessageHeader();
			assert(messageType!=null);
			
			switch(messageType) {
			case IAMCONTROLLER:
				this.type = ClientConnectionType.CONTROLLER;
				return true;
				
			case IAMVIEWER:
				this.type = ClientConnectionType.VIEWER;
				if (this.outputStream==null) {
					this.outputStream = new DataOutputStream(this.client.getOutputStream());
				}
				return true;
				
			case IAMBOTH:
				this.type = ClientConnectionType.BOTH;
				if (this.outputStream==null) {
					this.outputStream = new DataOutputStream(this.client.getOutputStream());
				}
				return true;

			default:
				getLogger().warning(this.type.name()+": Received unexpected message of type: "+messageType); //$NON-NLS-1$
			}
			
			return false;
		}

		/** Tries to detect the BYE message from the viewer.
		 */
		private boolean listenViewer() throws IOException {
			NetworkMessageType messageType = readMessageHeader();
			assert(messageType!=null);
			
			switch(messageType) {
			case BYE:
				// Viewer is requesting to be disconnected
				return false;
				
			default:
				getLogger().warning(this.type.name()+": Received unexpected message of type: "+messageType); //$NON-NLS-1$
			}
			
			return false;
		}
		
		/** Tries to detect the BYE message and controller messages.
		 */
		private boolean listenController() throws IOException {
			NetworkMessageType messageType = readMessageHeader();
			assert(messageType!=null);

			if (DEBUG) {
				System.err.println("RECEIVE "+messageType.toString()+" ("+messageType.toJaSIMCode()+")");  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			}
			
			switch(messageType) {
			case BYE:
				fireStopMessage();
				return false;

			case INIT:
				fireInitMessage(readInitMessage());
				return true;
				
			case PLAY:
				firePlayMessage();
				return true;
				
			case STEP:
				fireStepMessage();
				return true;

			case PAUSE:
				firePauseMessage();
				return true;

			case STOP:
				fireStopMessage();
				return true;

			case ADD_PROBE:
				fireAddProbeMessage(readAddProbeMessage());
				return true;

			case REMOVE_PROBE:
				fireRemoveProbeMessage(readRemoveProbeMessage());
				return true;

			case SET_SIMULATION_DELAY:
				fireSetSimulationDelayMessage(readSetSimulationDelayMessage());
				return true;

			case KILL_SIMULATOR:
				fireKillSimulationMessage();
				return true;

			default:
				getLogger().warning(this.type.name()+": Received unexpected message of type: "+messageType); //$NON-NLS-1$
			}
			
			return false;
		}

		/** {@inheritDoc}
		 */
		@Override
		public void closeConnection() {
			this.stop = true;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public InetAddress getRemoteHost() {
			return this.client.isClosed() ? null : this.client.getInetAddress();
		}
				
		/** {@inheritDoc}
		 */
		@Override
		public void sendStartMessage(SimulationInfo config, PlaceInfo... places) throws IOException {
			if (!this.type.isViewer())
				throw new IllegalStateException("no a remote JaSIM viewer"); //$NON-NLS-1$
			writeStartMessage(config, places);
			this.needEndMsg = true;
		}

		/** {@inheritDoc}
		 */
		@Override
		public void sendEndMessage() throws IOException {
			if (!this.type.isViewer())
				throw new IllegalStateException("no a remote JaSIM viewer"); //$NON-NLS-1$
			this.needEndMsg = false;
			writeEndMessage();
		}

		/** {@inheritDoc}
		 */
		@Override
		public void sendActionMessage(double time, double duration, int actionCount, Iterator<MobileEntityMoveInfo> entityMoves) throws IOException {
			if (!this.type.isViewer())
				throw new IllegalStateException("no a remote JaSIM viewer"); //$NON-NLS-1$
			writeMoveActionMessage(time, duration, actionCount, entityMoves);
		}

		/** {@inheritDoc}
		 */
		@Override
		public void sendAdditionMessage(double time, int entityCount, Iterator<MobileEntitySpawningInfo> entities) throws IOException {
			if (!this.type.isViewer())
				throw new IllegalStateException("no a remote JaSIM viewer"); //$NON-NLS-1$
			writeAdditionMessage(time, entityCount, entities);
		}

		/** {@inheritDoc}
		 */
		@Override
		public void sendDeletionMessage(double time, int entityCount, Iterator<UUID> entityId) throws IOException {
			if (!this.type.isViewer())
				throw new IllegalStateException("no a remote JaSIM viewer"); //$NON-NLS-1$
			writeDeletionMessage(time, entityCount, entityId);
		}

		/** {@inheritDoc}
		 */
		@Override
		public void sendProbeMessage(double time, int probeCount, Iterator<ProbeInfo> probes) throws IOException {
			if (!this.type.isViewer())
				throw new IllegalStateException("no a remote JaSIM viewer"); //$NON-NLS-1$
			writeProbeMessage(time, probeCount, probes);
		}

		/**
		 * Send the KILLED message to the remote vewier. If necessary the END message
		 * will be sent just before the KILLED message.
		 *
		 * @throws IOException
		 */
		public void sendKilledMessage() throws IOException {
			if (!this.type.isViewer())
				throw new IllegalStateException("no a remote JaSIM viewer"); //$NON-NLS-1$
			
			if (this.needEndMsg) {
				writeEndMessage();
				this.needEndMsg = false;
			}
			
			writeKilledMessage();
		}
		
		/** Send the given bytes to the remtoe client.
		 * 
		 * @param bytes
		 * @throws IOException 
		 */
		public void sendBytes(byte[] bytes) throws IOException {
			if (!this.type.isViewer())
				throw new IllegalStateException("no a remote JaSIM viewer"); //$NON-NLS-1$
			
			if (this.outputStream==null) throw new BindException("no output socket"); //$NON-NLS-1$
			this.outputStream.write(bytes);
		}

	}
	
}

