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

import java.net.InetAddress;

/**
 * This interface is representing a remote client.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ClientConnection {

	/** Replies the type of the remote client.
	 * If the client has not run the presentation protocol, this function
	 * replies <code>null</code>
	 * 
	 * @return the type of the remote client or <code>null</code> if
	 * the remote client is not yet presented.
	 */
	public ClientConnectionType getRemoteClientType();
	
	/**
	 * Close the current connection.
	 * This method is asynchronous, ie. it is requesting to close
	 * the connection but the closing should be delaying according
	 * to the connection thread.
	 */
	public void closeConnection();
	
	/** Replies the address of the remote host.
	 * 
	 * @return an address, or <code>null</code> if connection was closed
	 */
	public InetAddress getRemoteHost();
	
}
