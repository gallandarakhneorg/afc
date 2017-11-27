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
package fr.utbm.set.jasim.network.data;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class describes several parameters of a simulation
 * which are given by a controller to the JaSIM simulator.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SimulationConfiguration implements Serializable {
	
	private static final long serialVersionUID = 1709841517160326595L;
	
	private final URL defaultDirectory;
	private final String xmlConfiguration;
	
	/**
	 * @param defaultDirectory is default directory where the JaSIM simulator is supposed to search files. 
	 * @param xmlContent is an XML string which is describing the configuration of the simulation. 
	 */
	public SimulationConfiguration(File defaultDirectory, String xmlContent) {
		URL url = null;
		try {
			url = defaultDirectory.toURI().toURL();
		}
		catch(MalformedURLException _) {
			// May never occur
		}
		this.defaultDirectory = url;
		this.xmlConfiguration = xmlContent==null ? "" : xmlContent; //$NON-NLS-1$
	}
	
	/**
	 * @param defaultDirectory is default directory where the JaSIM simulator is supposed to search files. 
	 * @param xmlContent is an XML string which is describing the configuration of the simulation. 
	 */
	public SimulationConfiguration(URL defaultDirectory, String xmlContent) {
		this.defaultDirectory = defaultDirectory;
		this.xmlConfiguration = xmlContent==null ? "" : xmlContent; //$NON-NLS-1$
	}

	/** Replies the directory where to search for files.
	 * 
	 * @return a directory or <code>null</code>
	 */
	public URL getSearchDirectory() {
		return this.defaultDirectory;
	}
	
	/** Replies the XML configuration for the simulator.
	 * 
	 * @return the XML configuration for the simulator, never <code>null</code>.
	 */
	public String getXMLConfiguration() {
		return this.xmlConfiguration;
	}
	
}