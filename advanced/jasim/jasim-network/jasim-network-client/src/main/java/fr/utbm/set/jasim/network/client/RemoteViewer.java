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
import java.util.Collection;
import java.util.logging.Logger;

import org.arakhne.afc.util.OutputParameter;

import fr.utbm.set.jasim.network.AbstractJaSIMNetworkConnection;
import fr.utbm.set.jasim.network.NetworkMessageType;
import fr.utbm.set.jasim.network.data.MobileEntityMoveInfo;
import fr.utbm.set.jasim.network.data.MobileEntitySpawningInfo;
import fr.utbm.set.jasim.network.data.PlaceInfo;
import fr.utbm.set.jasim.network.data.ProbeInfo;
import fr.utbm.set.jasim.network.data.SimulationInfo;

/**
 * Remote viewer for the JaSIM simulator.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class RemoteViewer extends AbstractJaSIMNetworkConnection implements Runnable {

	private final InetSocketAddress serverAddress;
	private Socket socket;
	private volatile boolean stop = false;
	private final Collection<RemoteViewerListener> listeners = new ArrayList<RemoteViewerListener>();

	/**
	 * @param address
	 * @throws IOException
	 */
	public RemoteViewer(InetSocketAddress address) throws IOException {
		this.serverAddress = address;
	}

	/**
	 * @param host
	 * @throws IOException
	 */
	public RemoteViewer(InetAddress host) throws IOException {
		this(new InetSocketAddress(host, DEFAULT_SIMULATOR_PORT));
	}

	/**
	 * @param host
	 * @throws IOException
	 */
	public RemoteViewer(String host) throws IOException {
		this(new InetSocketAddress(host, DEFAULT_SIMULATOR_PORT));
	}

	/**
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public RemoteViewer(InetAddress host, int port) throws IOException {
		this(new InetSocketAddress(host, port));
	}

	/**
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public RemoteViewer(String host, int port) throws IOException {
		this(new InetSocketAddress(host, port));
	}

	/** Replies the logger.
	 * 
	 * @return a logger.
	 */
	protected static Logger getLogger() {
		return Logger.getAnonymousLogger();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			// Open a socket connection
			this.socket = new Socket(this.serverAddress.getAddress(), this.serverAddress.getPort());
			
			// Open I/O streams for objects
			this.outputStream = new DataOutputStream(this.socket.getOutputStream());
			this.inputStream = new DataInputStream(this.socket.getInputStream());
			
			boolean nostop = true;
			this.stop = false;
			
			writeIamViewerMessage();
			
			fireConnectionOpened();
			
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
		synchronized(this.listeners) {
			this.listeners.add(listener);
		}
	}
	
	/** Remove a listener on the client events.
	 *
	 * @param listener
	 */
	public void remoteRemoveViewerListener(RemoteViewerListener listener) {
		synchronized(this.listeners) {
			this.listeners.remove(listener);
		}
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
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onConnexionOpened(this.serverAddress);
			}
		}
	}

	/** Notifies the listeners about a network connection closing.
	 */
	private void fireConnectionClosed() {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onConnexionClosed(this.serverAddress);
			}
		}
	}

	/** Notifies the listeners about the arrival of a START message.
	 * 
	 * @param simulationDescription
	 * @param places
	 */
	private void fireStartMessageReceived(SimulationInfo simulationDescription, PlaceInfo... places) {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onStartMessageReceived(simulationDescription, places);
			}
		}
	}

	/** Notifies the listeners about the arrival of a END message.
	 */
	private void fireEndMessageReceived() {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onEndMessageReceived();
			}
		}
	}

	/** Notifies the listeners about the arrival of a ADDITION message.
	 * 
	 * @param simulationTime
	 * @param entity
	 */
	private void fireAdditionMessageReceived(double simulationTime, MobileEntitySpawningInfo entity) {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onAdditionMessageReceived(simulationTime, entity);
			}
		}
	}

	/** Notifies the listeners about the arrival of a DELETION message.
	 * 
	 * @param simulationTime
	 * @param entityId
	 */
	private  void fireDeletionMessageReceived(double simulationTime, String entityId) {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onDeletionMessageReceived(simulationTime, entityId);
			}
		}
	}

	/** Notifies the listeners about the arrival of a MOVE_ACTION message.
	 * 
	 * @param simulationTime
	 * @param actionDuration
	 * @param moves
	 */
	private void fireMoveActionMessageReceived(double simulationTime, double actionDuration, MobileEntityMoveInfo... moves) {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onMoveActionMessageReceived(simulationTime, actionDuration, moves);
			}
		}
	}

	/** Notifies the listeners about the arrival of a IDDLE message.
	 * 
	 * @param simulationTime
	 * @param actionDuration
	 */
	private void fireIddleMessageReceived(double simulationTime, double actionDuration) {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onIddleReceived(simulationTime, actionDuration);
			}
		}
	}

	/** Notifies the listeners about the arrival of a PROBE message.
	 * 
	 * @param simulationTime
	 * @param probes
	 */
	private void fireProbeMessageReceived(double simulationTime, ProbeInfo... probes) {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onProbeMessageReceived(simulationTime, probes);
			}
		}
	}

	/** Notifies the listeners about the arrival of a KILLED message.
	 * 
	 * @param simulationTime
	 * @param probes
	 */
	private void fireKilledMessageReceived() {
		synchronized(this.listeners) {
			for(RemoteViewerListener listener : this.listeners) {
				listener.onKilledMessageReceived();
			}
		}
	}

}

