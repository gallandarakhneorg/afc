/*
 * 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
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

import fr.utbm.set.geom.bounds.bounds1d5.BoundingRect1D5;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.controller.SimulationController1D5;
import fr.utbm.set.jasim.controller.SimulationControllerConfiguration;
import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.jasim.environment.model.world.MobileEntity1D5;
import fr.utbm.set.jasim.io.sfg.SFGFileFilter;
import fr.utbm.set.jasim.io.sfg.XMLSimulationConfigParser1D5;
import fr.utbm.set.jasim.janus.controller.JanusControlBinder;

/**
 * Main class that permits to launch a simulation from a
 * configuration file.
 * <p>
 * The configuration must be readable by an instance of
 * {@link XMLSimulationConfigParser1D5}.
 *
 * @author $Author: sgalland$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JasimLocalEngine1D5 {
	
	private static URL getConfigurationFile(String...parameters) {
		File file;
		URL url;
		for(String parameter : parameters) {
			file = new File(parameter);
			if (SFGFileFilter.isSFGFile(file)) {
				try {
					return file.toURI().toURL();
				}
				catch (MalformedURLException _) {
					//
				}
			}
			try {
				url = new URL(parameter);
				if (SFGFileFilter.isSFGFile(url)) {
					return url;
				}
			}
			catch (MalformedURLException _) {
				//
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] parameters) throws IOException {
		URL configurationFile = getConfigurationFile(parameters);
		
		if (configurationFile==null) {
			System.err.println("no configuration file specified"); //$NON-NLS-1$
			return;
		}
		
		SimulationController1D5<BoundingRect1D5<RoadSegment>,BoundingRect1D5<RoadSegment>> controller;
		{
			Class<BoundingRect1D5<RoadSegment>> type = (Class)BoundingRect1D5.class;
			controller = 
					new SimulationController1D5<BoundingRect1D5<RoadSegment>,
												BoundingRect1D5<RoadSegment>>(
							new JanusControlBinder(),
							type);
		}
		{
			ProgressionConsoleMonitor monitor = new ProgressionConsoleMonitor();
			Progression progression = monitor.getModel();
			
			SimulationControllerConfiguration<Entity1D5<BoundingRect1D5<RoadSegment>>,
											  MobileEntity1D5<BoundingRect1D5<RoadSegment>>> description = 
				controller.loadSimulationConfiguration(configurationFile,
						progression.subTask(30));
			
			controller.initSimulation(description, progression.subTask(70));
		}
		
		controller.playSimulation();
	}
	
}
