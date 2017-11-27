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
package fr.utbm.set.jasim.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.logging.Logger;

import org.arakhne.afc.util.ListenerCollection;
import org.arakhne.afc.util.OutputParameter;

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
 * Remote viewer and controller for the JaSIM simulator.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class RemoteFrontEnd extends AbstractJaSIMNetworkConnection implements Runnable {

	private final InetSocketAddress serverAddress;
	private Socket socket;
	private volatile boolean stop = false;
	private final ListenerCollection<EventListener> listeners = new ListenerCollection<EventListener>();

	/**
	 * @param address
	 * @throws IOException
	 */
	public RemoteFrontEnd(InetSocketAddress address) throws IOException {
		this.serverAddress = address;
	}

	/**
	 * @param host
	 * @throws IOException
	 */
	public RemoteFrontEnd(InetAddress host) throws IOException {
		this(new InetSocketAddress(host, DEFAULT_SIMULATOR_PORT));
	}

	/**
	 * @param host
	 * @throws IOException
	 */
	public RemoteFrontEnd(String host) throws IOException {
		this(new InetSocketAddress(host, DEFAULT_SIMULATOR_PORT));
	}

	/**
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public RemoteFrontEnd(InetAddress host, int port) throws IOException {
		this(new InetSocketAddress(host, port));
	}

	/**
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public RemoteFrontEnd(String host, int port) throws IOException {
		this(new InetSocketAddress(host, port));
	}

	/** Replies the logger.
	 * 
	 * @return a logger.
	 */
	protected static Logger getLogger() {
		return Logger.getAnonymousLogger();
	}
	
	private void ensureConnection() throws IOException {
		if (this.socket==null) {
			// Open a socket connection
			this.socket = new Socket(this.serverAddress.getAddress(), this.serverAddress.getPort());
			
			// Open I/O streams for objects
			this.outputStream = new DataOutputStream(this.socket.getOutputStream());
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			
			writeIamBothMessage();			
			
			fireConnectionOpened();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			boolean nostop = true;
			this.stop = false;

			ensureConnection();
			
			while(!this.stop && nostop){
				nostop = listen();
			}

			if (this.outputStream!=null) {
				
				writeByeMessage();
				
				this.outputStream.close();
				this.outputStream = null;
			}
			if (this.inputStream!=null) {
				this.inputStream.close();
				this.inputStream = null;
			}
			if (this.socket!=null) {
				this.socket.close();
				this.socket = null;
			}
		}
		catch(IOException e) {
			getLogger().severe(e.toString());
		}

		fireConnectionClosed();
	}

	/**
	 * Close the connection supported by this client.
	 * This function is asynchronous ie, the connection close
	 * should append with a delay in time.
	 */
	public void closeConnection() {
		this.stop = true;
	}
	
	/**
	 * Returns a boolean specifying if the connection with the simulator is created or not
	 * @return a boolean specifying if the connection with the simulator is created or not
	 */
	public boolean isClientConnectionEstablished() {
		return this.socket!=null && this.socket.isConnected();
	}

	/** Add a listener on the client events.
	 *
	 * @param listener
	 */
	public void addRemoteViewerListener(RemoteViewerListener listener) {
		this.listeners.add(RemoteViewerListener.class, listener);
	}
	
	/** Remove a listener on the client events.
	 *
	 * @param listener
	 */
	public void removeRemoteViewerListener(RemoteViewerListener listener) {
		this.listeners.remove(RemoteViewerListener.class, listener);
	}

	/** Add a listener on the client events.
	 *
	 * @param listener
	 */
	public void addRemoteControllerListener(RemoteControllerListener listener) {
		this.listeners.add(RemoteControllerListener.class, listener);
	}
	
	/** Remove a listener on the client events.
	 *
	 * @param listener
	 */
	public void removeRemoteControllerListener(RemoteControllerListener listener) {
		this.listeners.remove(RemoteControllerListener.class, listener);
	}

	/** Tries to read the simulator messages.
	 * 
	 * @return <code>true</code> to continue to listen, otherwise <code>false</code>
	 */
	private boolean listen() throws IOException {
		NetworkMessageType type = readMessageHeader();
		assert(type!=null);
		
		switch(type) {
		case START:
			{
				OutputParameter<SimulationInfo> simu = new OutputParameter<SimulationInfo>();
				OutputParameter<PlaceInfo[]> places = new OutputParameter<PlaceInfo[]>();
				readStartMessage(simu,places);
				fireStartMessageReceived(simu.get(), places.get());
			}
			return true;

		case END:
			fireEndMessageReceived();
			return true;

		case ADDITION:
			{
				OutputParameter<Double> time = new OutputParameter<Double>();
				ArrayList<MobileEntitySpawningInfo> entities = new ArrayList<MobileEntitySpawningInfo>();
				readAdditionMessage(time,entities);
				for(MobileEntitySpawningInfo info : entities) {
					fireAdditionMessageReceived(time.get(), info);
				}
			}
			return true;

		case DELETION:
			{
				OutputParameter<Double> time = new OutputParameter<Double>();
				OutputParameter<String> entityId = new OutputParameter<String>();
				readDeletionMessage(time,entityId);
				fireDeletionMessageReceived(time.get(), entityId.get());
			}
			return true;
			
		case ACTION:
			{
				OutputParameter<Double> time = new OutputParameter<Double>();
				OutputParameter<Double> duration = new OutputParameter<Double>();
				OutputParameter<MobileEntityMoveInfo[]> moves = new OutputParameter<MobileEntityMoveInfo[]>();
				readMoveActionMessage(time,duration, moves);
				fireMoveActionMessageReceived(time.get(), duration.get(), moves.get());
			}
			return true;
			
		case IDDLE:
			{
				OutputParameter<Double> time = new OutputParameter<Double>();
				OutputParameter<Double> duration = new OutputParameter<Double>();
				readIddleMessage(time,duration);
				fireIddleMessageReceived(time.get(), duration.get());
			}
			return true;

		case PROBE:
			{
				OutputParameter<Double> time = new OutputParameter<Double>();
				OutputParameter<ProbeInfo[]> probes = new OutputParameter<ProbeInfo[]>();
				readProbeMessage(time,probes);
				fireProbeMessageReceived(time.get(), probes.get());
			}
			return true;

		case KILLED:
			fireKilledMessageReceived();
			return true;

		default:
			getLogger().info("Received unexpected message of type: "+type); //$NON-NLS-1$
		}
		
		return false;
	}
	
	/** Notifies the listeners about a network connection opening.
	 */
	private void fireConnectionOpened() {
		for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
			listener.onConnexionOpened(this.serverAddress);
		}
		for(RemoteControllerListener listener : this.listeners.getListeners(RemoteControllerListener.class)) {
			listener.onConnexionOpened(this.serverAddress);
		}
	}

	/** Notifies the listeners about a network connection closing.
	 */
	private void fireConnectionClosed() {
		for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
			listener.onConnexionClosed(this.serverAddress);
		}
		for(RemoteControllerListener listener : this.listeners.getListeners(RemoteControllerListener.class)) {
			listener.onConnexionClosed(this.serverAddress);
		}
	}

	/** Notifies the listeners about the arrival of a START message.
	 * 
	 * @param simulationDescription
	 * @param places
	 */
	private void fireStartMessageReceived(SimulationInfo simulationDescription, PlaceInfo... places) {
		for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
			listener.onStartMessageReceived(simulationDescription, places);
		}
	}

	/** Notifies the listeners about the arrival of a END message.
	 */
	private void fireEndMessageReceived() {
		for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
			listener.onEndMessageReceived();
		}
	}

	/** Notifies the listeners about the arrival of a ADDITION message.
	 * 
	 * @param simulationTime
	 * @param entity
	 */
	private void fireAdditionMessageReceived(double simulationTime, MobileEntitySpawningInfo entity) {
		for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
			listener.onAdditionMessageReceived(simulationTime, entity);
		}
	}

	/** Notifies the listeners about the arrival of a DELETION message.
	 * 
	 * @param simulationTime
	 * @param entityId
	 */
	private  void fireDeletionMessageReceived(double simulationTime, String entityId) {
		for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
			listener.onDeletionMessageReceived(simulationTime, entityId);
		}
	}

	/** Notifies the listeners about the arrival of a MOVE_ACTION message.
	 * 
	 * @param simulationTime
	 * @param actionDuration
	 * @param moves
	 */
	private void fireMoveActionMessageReceived(double simulationTime, double actionDuration, MobileEntityMoveInfo... moves) {
		for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
			listener.onMoveActionMessageReceived(simulationTime, actionDuration, moves);
		}
	}

	/** Notifies the listeners about the arrival of a IDDLE message.
	 * 
	 * @param simulationTime
	 * @param actionDuration
	 */
	private void fireIddleMessageReceived(double simulationTime, double actionDuration) {
		for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
			listener.onIddleReceived(simulationTime, actionDuration);
		}
	}

	/** Notifies the listeners about the arrival of a PROBE message.
	 * 
	 * @param simulationTime
	 * @param probes
	 */
	private void fireProbeMessageReceived(double simulationTime, ProbeInfo... probes) {
		for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
			listener.onProbeMessageReceived(simulationTime, probes);
		}
	}

	/** Notifies the listeners about the arrival of a KILLED message.
	 * 
	 * @param simulationTime
	 * @param probes
	 */
	private void fireKilledMessageReceived() {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners.getListeners(RemoteViewerListener.class)) {
				listener.onKilledMessageReceived();
			}
		}
	}

	/** Send a INIT message.
	 * 
	 * @param configuration
	 * @throws IOException
	 */
	public void initializeSimulation(SimulationConfiguration configuration) throws IOException {
		ensureConnection();
		writeInitMessage(configuration);
	}

	/** Send a PLAY message.
	 * 
	 * @throws IOException
	 */
	public void playSimulation() throws IOException {
		ensureConnection();
		writePlayMessage();
	}

	/** Send a STEP message.
	 * 
	 * @throws IOException
	 */
	public void stepSimulation() throws IOException {
		ensureConnection();
		writeStepMessage();
	}

	/** Send a PAUSE message.
	 * 
	 * @throws IOException
	 */
	public void pauseSimulation() throws IOException {
		ensureConnection();
		writePauseMessage();
	}

	/** Send a STOP message.
	 * 
	 * @throws IOException
	 */
	public void stopSimulation() throws IOException {
		ensureConnection();
		writeStopMessage();
	}

	/** Send a ADD_PROBE message.
	 * 
	 * @param description
	 * @throws IOException
	 */
	public void addSimulationProbe(ProbeDescription description) throws IOException {
		ensureConnection();
		writeAddProbeMessage(description);
	}

	/** Send a REMOVE_PROBE message.
	 * 
	 * @param identifier
	 * @throws IOException
	 */
	public void removeSimulationProbe(ProbeIdentifier identifier) throws IOException {
		ensureConnection();
		writeRemoveProbeMessage(identifier);
	}

	/** Send a SET_SIMULATION_DELAY message.
	 * 
	 * @param delay is the delay in milliseconds.
	 * @throws IOException
	 */
	public void setSimulationDelay(int delay) throws IOException {
		ensureConnection();
		writeSetSimulationDelayMessage(delay);
	}

	/** Send a KILL_SIMULATOR message.
	 * 
	 * @throws IOException
	 */
	public void killSimulator() throws IOException {
		ensureConnection();
		writeKillSimulatorMessage();
	}

}

