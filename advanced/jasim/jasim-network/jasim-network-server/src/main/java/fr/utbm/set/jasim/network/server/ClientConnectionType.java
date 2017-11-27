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

/**
 * Type of the remote client.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum ClientConnectionType {
	/** The remote client is a controller.
	 */
	CONTROLLER,
	/** The remote client is a viewer.
	 */
	VIEWER,
	/** The remote client is both a controller and a viewer.
	 */
	BOTH;
	
	/** Replies if the current type corresponds to a viewer.
	 * 
	 * @return <code>true</code> if the current type corresponds to a viewer, otherwise <code>false</code>
	 */
	public boolean isViewer() {
		return this==VIEWER || this==BOTH;
	}

	/** Replies if the current type corresponds to a controller.
	 * 
	 * @return <code>true</code> if the current type corresponds to a controller, otherwise <code>false</code>
	 */
	public boolean isController() {
		return this==CONTROLLER || this==BOTH;
	}

}
