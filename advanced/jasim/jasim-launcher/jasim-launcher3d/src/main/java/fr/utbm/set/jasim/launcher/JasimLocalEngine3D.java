/*
 * 
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
package fr.utbm.set.jasim.launcher;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.progress.ProgressionConsoleMonitor;
import org.arakhne.afc.vmutil.FileSystem;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.jasim.controller.SimulationController3D;
import fr.utbm.set.jasim.controller.SimulationControllerConfiguration;
import fr.utbm.set.jasim.environment.model.world.Entity3D;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;
import fr.utbm.set.jasim.io.sfg.SFGFileFilter;
import fr.utbm.set.jasim.io.sfg.XMLSimulationConfigParser3D;
import fr.utbm.set.jasim.janus.controller.JanusControlBinder;

/**
 * Main class that permits to launch a simulation from a
 * configuration file.
 * <p>
 * The configuration must be readable by an instance of
 * {@link XMLSimulationConfigParser3D}.
 *
 * @author $Author: sgalland$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JasimLocalEngine3D {
	
	private static URL getConfigurationFile(String...parameters) {
		File file;
		URL url;
		for(String parameter : parameters) {
			try {
				url = FileSystem.convertStringToURL(parameter, true);
				if (SFGFileFilter.isSFGFile(url)) {
					return url;
				}
			}
			catch (IllegalArgumentException _) {
				//
			}
			
			file = new File(parameter);
			if (SFGFileFilter.isSFGFile(file)) {
				try {
					return file.toURI().toURL();
				}
				catch (MalformedURLException _) {
					//
				}
			}
		}
		return null;
	}
	
	/**
	 * Main function.
	 * 
	 * @param parameters command line parameters
	 * @throws IOException 
	 */
	public static void main(String[] parameters) throws IOException {
		URL configurationFile = getConfigurationFile(parameters);
		
		if (configurationFile==null) {
			System.err.println("no configuration file specified"); //$NON-NLS-1$
			return;
		}
		
		SimulationController3D<AlignedBoundingBox,AlignedBoundingBox> controller = new SimulationController3D<AlignedBoundingBox,AlignedBoundingBox>(new JanusControlBinder(), AlignedBoundingBox.class);
		
		{
			ProgressionConsoleMonitor monitor = new ProgressionConsoleMonitor();
			Progression progression = monitor.getModel();

			SimulationControllerConfiguration<Entity3D<AlignedBoundingBox>,MobileEntity3D<AlignedBoundingBox>> description = 
				controller.loadSimulationConfiguration(configurationFile,
						progression.subTask(30));
			
			controller.initSimulation(description, progression.subTask(70));
		}
		
		controller.playSimulation();
	}
	
}
