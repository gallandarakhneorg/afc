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

import java.net.InetSocketAddress;

import fr.utbm.set.jasim.network.data.MobileEntityMoveInfo;
import fr.utbm.set.jasim.network.data.MobileEntitySpawningInfo;
import fr.utbm.set.jasim.network.data.PlaceInfo;
import fr.utbm.set.jasim.network.data.ProbeInfo;
import fr.utbm.set.jasim.network.data.SimulationInfo;

/**
 * Listener on events from a RemoteViewer.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RemoteViewerAdapter implements RemoteViewerListener {
	
	/** {@inheritDoc}
	 */
	@Override
	public void onConnexionOpened(InetSocketAddress server) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onConnexionClosed(InetSocketAddress server) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onStartMessageReceived(SimulationInfo simulationDescription, PlaceInfo... places) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onEndMessageReceived() {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onAdditionMessageReceived(double simulationTime, MobileEntitySpawningInfo entity) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onDeletionMessageReceived(double simulationTime, String entityId) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onMoveActionMessageReceived(double simulationTime, double actionDuration, MobileEntityMoveInfo... moves) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onProbeMessageReceived(double simulationTime, ProbeInfo... probes) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onKilledMessageReceived() {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onIddleReceived(double simulationTime, double actionDuration) {
		//
	}

}