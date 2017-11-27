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

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import fr.utbm.set.jasim.network.data.MobileEntityMoveInfo;
import fr.utbm.set.jasim.network.data.MobileEntitySpawningInfo;
import fr.utbm.set.jasim.network.data.PlaceInfo;
import fr.utbm.set.jasim.network.data.ProbeInfo;
import fr.utbm.set.jasim.network.data.SimulationInfo;

/**
 * This interface is representing a remote viewer.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ViewerClientConnection extends ClientConnection {

	/**
	 * Send the START message to the remote vewier.
	 * 
	 * @param config
	 * @param places
	 * @throws IOException
	 */
	public void sendStartMessage(SimulationInfo config, PlaceInfo... places) throws IOException;
	
	/**
	 * Send the END message to the remote vewier.
	 * 
	 * @throws IOException
	 */
	public void sendEndMessage() throws IOException;

	/**
	 * Send the ACTION message to the remote vewier.
	 *
	 * @param time is the simulation time at which the actions are done.
	 * @param duration is the duration of the last simulation step.
	 * @param actionCount is the count of actions replied by the iterator.
	 * @param entityMoves
	 * @throws IOException
	 */
	public void sendActionMessage(double time, double duration, int actionCount, Iterator<MobileEntityMoveInfo> entityMoves) throws IOException;

	/**
	 * Send the ADDITION message to the remote vewier.
	 *
	 * @param time is the simulation time at which the addition was done.
	 * @param entityCount is the count of entities replied by the iterator.
	 * @param entity
	 * @throws IOException
	 */
	public void sendAdditionMessage(double time, int entityCount, Iterator<MobileEntitySpawningInfo> entity) throws IOException;

	/**
	 * Send the DELETION message to the remote vewier.
	 *
	 * @param time is the simulation time at which the deletion was done.
	 * @param entityCount is the count of entities replied by the iterator.
	 * @param entityId is the identifier of the entity.
	 * @throws IOException
	 */
	public void sendDeletionMessage(double time, int entityCount, Iterator<UUID> entityId) throws IOException;

	/**
	 * Send the PROBE message to the remote vewier.
	 *
	 * @param time is the simulation time at which the deletion was done.
	 * @param probeCount is the count of probes replied by the iterator.
	 * @param probes are the probed values to send
	 * @throws IOException
	 */
	public void sendProbeMessage(double time, int probeCount, Iterator<ProbeInfo> probes) throws IOException;

}
