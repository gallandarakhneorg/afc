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
import java.util.EventListener;

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
public interface RemoteViewerListener extends EventListener {
	
	/** Invoked when a client has opened a network connection.
	 * 
	 * @param server
	 */
	public void onConnexionOpened(InetSocketAddress server);

	/** Invoked when a client has closed a network connection.
	 * 
	 * @param server
	 */
	public void onConnexionClosed(InetSocketAddress server);

	/** Invoked when a START message was received.
	 * 
	 * @param simulationDescription
	 * @param places
	 */
	public void onStartMessageReceived(SimulationInfo simulationDescription, PlaceInfo... places);

	/** Invoked when a END message was received.
	 */
	public void onEndMessageReceived();

	/** Invoked when an ADDITION message was received.
	 * 
	 * @param simulationTime
	 * @param entity
	 */
	public void onAdditionMessageReceived(double simulationTime, MobileEntitySpawningInfo entity);

	/** Invoked when a DELETION message was received.
	 * 
	 * @param simulationTime
	 * @param entityId
	 */
	public void onDeletionMessageReceived(double simulationTime, String entityId);

	/** Invoked when a MOVE_ACTION message was received.
	 * 
	 * @param simulationTime
	 * @param actionDuration
	 * @param moves
	 */
	public void onMoveActionMessageReceived(double simulationTime, double actionDuration, MobileEntityMoveInfo... moves);

	/** Invoked when a IDDLE message was received.
	 * 
	 * @param simulationTime
	 * @param actionDuration
	 */
	public void onIddleReceived(double simulationTime, double actionDuration);

	/** Invoked when a PROBE message was received.
	 * 
	 * @param simulationTime
	 * @param probes
	 */
	public void onProbeMessageReceived(double simulationTime, ProbeInfo... probes);

	/** Invoked when a KILLED message was received.
	 */
	public void onKilledMessageReceived();

}