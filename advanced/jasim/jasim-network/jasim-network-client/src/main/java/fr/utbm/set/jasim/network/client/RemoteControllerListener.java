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

/**
 * Listener on events from a RemoteController.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface RemoteControllerListener extends EventListener {
	
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

}