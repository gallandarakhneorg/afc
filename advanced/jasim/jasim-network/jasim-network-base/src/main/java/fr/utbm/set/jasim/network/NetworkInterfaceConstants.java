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
package fr.utbm.set.jasim.network;

/**
 * Constants used by the network interface.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface NetworkInterfaceConstants {

	/** Default on which the simulator engine is listening for
	 * new clients.
	 */
	public static final int DEFAULT_SIMULATOR_PORT = 10090;

	/** Default remote simulator host used to be connected.
	 */
	public static final String DEFAULT_SIMULATOR_HOST = "127.0.0.1"; //$NON-NLS-1$

	/** Identification code for a JaSIM network message.
	 */
	public static final int JASIM_MESSAGE_CODE = 7894;

	/** Protocol version.
	 */
	public static final int JASIM_MESSAGE_PROTOCOL = 20100518;

}

