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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import fr.utbm.set.jasim.network.AbstractJaSIMNetworkConnection;
import fr.utbm.set.jasim.network.data.ProbeDescription;
import fr.utbm.set.jasim.network.data.ProbeIdentifier;
import fr.utbm.set.jasim.network.data.SimulationConfiguration;

/**
 * Remote controller for the JaSIM simulator.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class RemoteController extends AbstractJaSIMNetworkConnection {

	private final InetSocketAddress serverAddress;
	private Socket socket;
	private final Collection<RemoteControllerListener> listeners = new ArrayList<RemoteControllerListener>();

	/**
	 * @param address
	 * @throws IOException
	 */
	public RemoteController(InetSocketAddress address) throws IOException {
		this.serverAddress = address;
	}
	
	/**
	 * @param host
	 * @throws IOException
	 */
	public RemoteController(InetAddress host) throws IOException {
		this(new InetSocketAddress(host, DEFAULT_SIMULATOR_PORT));
	}

	/**
	 * @param host
	 * @throws IOException
	 */
	public RemoteController(String host) throws IOException {
		this(new InetSocketAddress(host, DEFAULT_SIMULATOR_PORT));
	}

	/**
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public RemoteController(InetAddress host, int port) throws IOException {
		this(new InetSocketAddress(host, port));
	}

	/**
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public RemoteController(String host, int port) throws IOException {
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
			this.inputStream = null;
			
			writeIamControllerMessage();			
			
			fireConnectionOpened();
		}
	}

	/**
	 * Close the connection supported by this client.
	 * This function is synchronous ie, the connection is closed immediately.
	 * 
	 * @throws IOException
	 */
	public void closeConnection() throws IOException {
		if (this.socket!=null) {
			if (this.outputStream!=null) {
				writeByeMessage();
				this.outputStream.close();
				this.outputStream = null;
			}
			if (this.inputStream!=null) {
				this.inputStream.close();
				this.inputStream = null;
			}
			this.socket.close();
			this.socket = null;
			fireConnectionClosed();
		}
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
	public void addRemoteControllerListener(RemoteControllerListener listener) {
		synchronized(this.listeners) {
			this.listeners.add(listener);
		}
	}
	
	/** Remove a listener on the client events.
	 *
	 * @param listener
	 */
	public void remoteRemoveControllerListener(RemoteControllerListener listener) {
		synchronized(this.listeners) {
			this.listeners.remove(listener);
		}
	}
		
	/** Notifies the listeners about a network connection opening.
	 */
	private void fireConnectionOpened() {
		synchronized(this.listeners) {
			for(RemoteControllerListener listener : this.listeners) {
				listener.onConnexionOpened(this.serverAddress);
			}
		}
	}

	/** Notifies the listeners about a network connection closing.
	 */
	private void fireConnectionClosed() {
		synchronized(this.listeners) {
			for(RemoteControllerListener listener : this.listeners) {
				listener.onConnexionClosed(this.serverAddress);
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
	public void remoteSimulationProbe(ProbeIdentifier identifier) throws IOException {
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

