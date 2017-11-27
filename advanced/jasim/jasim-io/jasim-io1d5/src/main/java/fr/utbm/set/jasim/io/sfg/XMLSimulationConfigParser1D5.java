/*
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.io.sfg;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Parse an XML file to extract simulation parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class XMLSimulationConfigParser1D5
extends XMLSimulationConfigParser {

	/**
	 * @param resource is the path to the XML resource.
	 */
	public XMLSimulationConfigParser1D5(File resource) {
		super(resource);
	}
	
	/**
	 * @param resource is the path to the XML resource.
	 */
	public XMLSimulationConfigParser1D5(URL resource) {
		super(resource);
	}

	/**
	 * @param resource is the path to the XML resource.
	 */
	public XMLSimulationConfigParser1D5(InputStream resource) {
		super(resource);
	}

	@Override
	protected AbstractParser createParser(SFGFileType fileType) {
		if (fileType.is1D5())
			return new Parser1D5(fileType);
		return null;
	}
	
}
