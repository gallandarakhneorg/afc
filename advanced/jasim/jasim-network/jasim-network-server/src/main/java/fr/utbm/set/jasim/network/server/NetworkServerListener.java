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

import java.util.EventListener;

import fr.utbm.set.jasim.network.data.ProbeDescription;
import fr.utbm.set.jasim.network.data.ProbeIdentifier;
import fr.utbm.set.jasim.network.data.SimulationConfiguration;

/**
 * Listener on events from a NetworkServer.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface NetworkServerListener extends EventListener {
	
	/** Invoked when a client has opened a network connection.
	 * 
	 * @param client
	 */
	public void onConnexionOpened(ClientConnection client);

	/** Invoked when a client has closed a network connection.
	 * 
	 * @param client
	 */
	public void onConnexionClosed(ClientConnection client);

	/** Invoked when an INIT message was received.
	 * 
	 * @param simulationConfiguration
	 */
	public void onInitMessageReceived(SimulationConfiguration simulationConfiguration);

	/** Invoked when an PLAY message was received.
	 */
	public void onPlayMessageReceived();

	/** Invoked when an STEP message was received.
	 */
	public void onStepMessageReceived();

	/** Invoked when an PAUSE message was received.
	 */
	public void onPauseMessageReceived();

	/** Invoked when an STOP message was received.
	 */
	public void onStopMessageReceived();

	/** Invoked when a ADD_PROBE message was received.
	 * 
	 * @param probeDescription
	 */
	public void onAddProbeMessageReceived(ProbeDescription probeDescription);

	/** Invoked when a REMOVE_PROBE message was received.
	 * 
	 * @param probeId
	 */
	public void onRemoveProbeMessageReceived(ProbeIdentifier probeId);

	/** Invoked when a SET_SIMULATION_DELAY message was received.
	 * 
	 * @param delay is the delay in milliseconds for the simulator to wait at each simulation step.
	 */
	public void onSetSimulationDelayReceived(long delay);

	/** Invoked when a KILL_SIMULATION message was received.
	 */
	public void onKillSimulationReceived();

}

