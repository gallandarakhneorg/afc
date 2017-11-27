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
package fr.utbm.set.jasim.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.arakhne.afc.progress.Progression;

import fr.utbm.set.jasim.controller.event.SimulationControllerListener;
import fr.utbm.set.jasim.environment.model.SituatedEnvironment;
import fr.utbm.set.jasim.environment.model.world.MobileEntity;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.jasim.environment.time.TimeManagerListener;
import fr.utbm.set.jasim.spawn.SpawnLocation;

/**
 * A simulation controller is an object which permits to launch, stop a simulation.
 * It is also able to dynamically add and remove entities from the simulation.
 * The SimulationController is the entry point and the bottleneck in which
 * all the actions of the simulation's user must pass. 
 *
 * @param <SE> is the type of the static entities supported by the simulation
 * @param <ME> is the type of the mobile entities supported by the simulation
 * @param <ENV> is the type of the situated environment supported by the simulation.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface SimulationController<SE extends WorldEntity<?>, ME extends MobileEntity<?>, ENV extends SituatedEnvironment<?,SE,ME,?>> {
	
	/**
	 * Load the initialization directives from the given file.
	 * This function does not initialize the simulation itself. You
	 * must invoke {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * with the value replied by this function as parameter.
	 * 
	 * @param absoluteXMLFilePath is the XML file to read.
	 * @param taskProgression is the task progression object to use to indicate
	 * the loading progression, or <code>null</code> to ignore progression
	 * notifications.
	 * @return the simulation configuration to pass to {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * @throws IOException in case of problem.
	 */
	public SimulationControllerConfiguration<SE,ME> loadSimulationConfiguration(URL absoluteXMLFilePath, Progression taskProgression) throws IOException;

	/**
	 * Load the initialization directives fro mthe given file.
	 * This function does not initialize the simulation itself. You
	 * must invoke {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * with the value replied by this function as parameter.
	 * 
	 * @param absoluteXMLFilePath is the XML file to read.
	 * @param taskProgression is the task progression object to use to indicate
	 * the loading progression, or <code>null</code> to ignore progression
	 * notifications.
	 * @return the simulation configuration to pass to {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * @throws IOException in case of problem.
	 */
	public SimulationControllerConfiguration<SE,ME> loadSimulationConfiguration(File absoluteXMLFilePath, Progression taskProgression) throws IOException;

	/**
	 * Load the initialization directives fro mthe given file.
	 * This function does not initialize the simulation itself. You
	 * must invoke {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * with the value replied by this function as parameter.
	 * 
	 * @param defaultDirectory is the default directory used to retreive
	 * the file with relative filenames. If <code>null</code>, the standard
	 * default directory will be used.
	 * @param xmlFileContent is the content of the XML configuration file.
	 * @param taskProgression is the task progression object to use to indicate
	 * the loading progression, or <code>null</code> to ignore progression
	 * notifications.
	 * @return the simulation configuration to pass to {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * @throws IOException in case of problem.
	 */
	public SimulationControllerConfiguration<SE,ME> loadSimulationConfiguration(File defaultDirectory, InputStream xmlFileContent, Progression taskProgression) throws IOException;

	/**
	 * Load the initialization directives fro mthe given file.
	 * This function does not initialize the simulation itself. You
	 * must invoke {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * with the value replied by this function as parameter.
	 * 
	 * @param defaultDirectory is the default directory used to retreive
	 * the file with relative filenames. If <code>null</code>, the standard
	 * default directory will be used.
	 * @param xmlFileContent is the content of the XML configuration file.
	 * @param taskProgression is the task progression object to use to indicate
	 * the loading progression, or <code>null</code> to ignore progression
	 * notifications.
	 * @return the simulation configuration to pass to {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * @throws IOException in case of problem.
	 */
	public SimulationControllerConfiguration<SE,ME> loadSimulationConfiguration(URL defaultDirectory, InputStream xmlFileContent, Progression taskProgression) throws IOException;

	/**
	 * Load the initialization directives from the given content.
	 * This function does not initialize the simulation itself. You
	 * must invoke {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * with the value replied by this function as parameter.
	 * 
	 * @param defaultDirectory is the default directory used to retreive
	 * the file with relative filenames. If <code>null</code>, the standard
	 * default directory will be used.
	 * @param xmlFileContent is the content of the XML configuration file.
	 * @param taskProgression is the task progression object to use to indicate
	 * the loading progression, or <code>null</code> to ignore progression
	 * notifications.
	 * @return the simulation configuration to pass to {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * @throws IOException in case of problem.
	 */
	public SimulationControllerConfiguration<SE,ME> loadSimulationConfiguration(File defaultDirectory, String xmlFileContent, Progression taskProgression) throws IOException;

	/**
	 * Load the initialization directives from the given content.
	 * This function does not initialize the simulation itself. You
	 * must invoke {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * with the value replied by this function as parameter.
	 * 
	 * @param defaultDirectory is the default directory used to retreive
	 * the file with relative filenames. If <code>null</code>, the standard
	 * default directory will be used.
	 * @param xmlFileContent is the content of the XML configuration file.
	 * @param taskProgression is the task progression object to use to indicate
	 * the loading progression, or <code>null</code> to ignore progression
	 * notifications.
	 * @return the simulation configuration to pass to {@link #initSimulation(SimulationControllerConfiguration, Progression)}
	 * @throws IOException in case of problem.
	 */
	public SimulationControllerConfiguration<SE,ME> loadSimulationConfiguration(URL defaultDirectory, String xmlFileContent, Progression taskProgression) throws IOException;

	/** Initialize the simulation with the given objects.
	 * 
	 * @param desc describes the simulation.
	 * @param taskProgression is the task progression object to use to indicate
	 * the initialisation progression, or <code>null</code> to ignore progression
	 * notifications.
	 */
	public void initSimulation(SimulationControllerConfiguration<SE,ME> desc, Progression taskProgression);
	
	/**
	 * Start and play the simulation
	 */
	public void playSimulation();
	
	/**
	 * Run one step of the simulation. 
	 */
	public void stepSimulation();
	
	/**
	 * Pause the execution of the simulation
	 */
	public void pauseSimulation();

	/**
	 * Stop the simulation
	 */
	public void stopSimulation();
	
	/**
	 * Add a spawning point.
	 *  
	 * @param spawnerPoint is the spawning point.
	 * @return <code>true</code> if the spawner was successfully added, otherwise <code>false</code>
	 */
	public boolean addSpawners(SpawnLocation spawnerPoint);
	
	/**
	 * Replies the spawners.
	 *  
	 * @return the spawners
	 */
	public Collection<? extends SpawnLocation> getSpawners();

	/**
	 * Remove a spawning point.
	 *  
	 * @param spawnerPoint is the spawning point.
	 */
	public void removeSpawners(SpawnLocation spawnerPoint);

	/** Add a listener on this simulation controller.
	 *
	 * @param listener
	 */
	public void addSimulationControllerListener(SimulationControllerListener listener);
	
	/** Remove a listener on this simulation controller.
	 *
	 * @param listener
	 */
	public void removeSimulationControllerListener(SimulationControllerListener listener);

	/** Replies the current clock from this controller.
	 * 
	 * @return the clock or <code>null</code>
	 */
	public Clock getSimulationClock();
	
	/** Add a listener on time events.
	 * 
	 * @param listener
	 */
	public void addTimeListener(TimeManagerListener listener);
	
	/** Remove a listener on time events.
	 * 
	 * @param listener
	 */
	public void removeTimeListener(TimeManagerListener listener);
	
	/** Replies the environment supported by this controller.
	 *
	 * @return an environment or <code>null</code>
	 */
	public ENV getEnvironment();

	/**
	 * Set simulation execution delay.
	 * <p>
	 * This delay is used to sleep the simulation at each simulation step.
	 * A delay of zero means that the simulator will go as fast as possible.
	 * If the given delay is negative, the value is assumed to be equal to zero.
	 * 
	 * @param delay is the execution delay in milliseconds.
	 */
	public void setSimulationExecutionDelay(long delay);

	/** Replies if the simulation is playing and not paused.
	 * <p>
	 * The simulation is playing when it is currently under run.
	 * 
	 * @return <code>true</code> if the simulation is playing.
	 */
	public boolean isPlaying();

	/** Replies if the simulation was started.
	 * <p>
	 * The simulation was started if at list one of {@link #playSimulation()}
	 * or {@link #stepSimulation()} was already invoked.
	 * 
	 * @return <code>true</code> if the simulation was started.
	 */
	public boolean isStarted();

	/** Replies if the simulation is paused.
	 * <p>
	 * The simulation is paused when it was started but not under run
	 * ie, it is standing-by.
	 * 
	 * @return <code>true</code> if the simulation is paused.
	 */
	public boolean isPaused();

	/** Replies if the simulation was stopped.
	 * <p>
	 * A simulation was stopped when when it was never started
	 * or after is was stopped.
	 * 
	 * @return <code>true</code> if the simulation is stopped.
	 */
	public boolean isStopped();

	/** Replies if the simulation was initialized but never started.
	 * 
	 * @return <code>true</code> if the simulation was initialized.
	 */
	public boolean isInitialized();

}
