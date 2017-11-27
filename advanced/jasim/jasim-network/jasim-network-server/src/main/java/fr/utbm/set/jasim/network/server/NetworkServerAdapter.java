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
public class NetworkServerAdapter implements NetworkServerListener {
	
	/** {@inheritDoc}
	 */
	@Override
	public void onConnexionOpened(ClientConnection client) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onConnexionClosed(ClientConnection client) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onInitMessageReceived(SimulationConfiguration simulationConfiguration) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onPlayMessageReceived() {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onStepMessageReceived() {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onPauseMessageReceived() {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onStopMessageReceived() {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onAddProbeMessageReceived(ProbeDescription probeDescription) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onRemoveProbeMessageReceived(ProbeIdentifier probeId) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onSetSimulationDelayReceived(long delay) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onKillSimulationReceived() {
		//
	}

}

