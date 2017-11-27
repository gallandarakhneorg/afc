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
package org.arakhne.afc.jasim.environment.model;

import java.util.UUID;

import org.arakhne.afc.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import org.arakhne.afc.jasim.environment.interfaces.internalevents.JasimSimulationListener;
import org.arakhne.afc.jasim.environment.interfaces.probes.EnvironmentalProbes;
import org.arakhne.afc.jasim.environment.model.ground.Ground;
import org.arakhne.afc.jasim.environment.model.world.DynamicEntityManager;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;
import org.arakhne.afc.jasim.environment.time.Clock;
import org.arakhne.afc.progress.Progression;

import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.object.EuclidianPoint;

/**
 * The situated environment of a multi-agent based simulation.
 * <p>
 * This class defines the inerface of a situated environment
 * for the outside of the simulator. 
 * 
 * @param <EA> is the type of supported actions inside the environment.
 * @param <SE> is the type of static entity supported by this environment.
 * @param <ME> is the type of mobile entity supported by this environment.
 * @param <P> is the type of the euclidian points supported by this environment.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface SituatedEnvironment<EA extends EnvironmentalAction, SE extends WorldEntity<? extends Bounds<?,P,?>>, ME extends MobileEntity<? extends Bounds<?,P,?>>, P> {
	
	/** Replies the space dimension suppported by this environment.
	 * 
	 * @return the preferred space dimension
	 */
	public PseudoHamelDimension getMathematicalDimension();
	
	/**
	 * Replies the ground in this environment.
	 * 
	 * @param placeId is the identifier of the place for which the ground must be replied.
	 * @return the ground or <code>null</code>
	 */
	public Ground getGround(UUID placeId);

	/**
	 * Replies the count of places in the environment.
	 * 
	 * @return the count of places.
	 */
	public int getPlaceCount();

	/**
	 * Replies all the place identifiers.
	 * 
	 * @return the place identifiers.
	 */
	public Iterable<UUID> getPlaceIdentifiers();

	/**
	 * Replies replies the place which is the best one to manage the given location.
	 * <p>
	 * The default place selection is based on a keep on floor approach.
	 * 
	 * @param position
	 * @return the place identifier or <code>null</code> if no place match.
	 */
	public UUID getPlace(P position);

	/**
	 * Replies replies the place which is the best one to manage the given location.
	 * <p>
	 * The default place selection is based on a keep on floor approach.
	 * 
	 * @param position
	 * @return the place identifier or <code>null</code> if no place match.
	 */
	public UUID getPlace(EuclidianPoint position);

	/* ********************************************** Life Cycle *** */
	
	/** Replies if the environment was activated/initialized.
	 * <p>
	 * The environment is activated with a call to {@link #activate(SituatedEnvironmentDescription, Progression)}.
	 * 
	 * @return <code>true</code> if activated, otherwise <code>false</code>
	 */
	public boolean isActivated();
	
	/**
	 * Invoked to initialize the environment.
	 * 
	 * @param description describes the situated environment to launch.
	 * @param taskProgression is the object which permits to notify the progression of
	 * the activation. It could be <code>null</code> if this notification is not requested.
	 */
	public void activate(SituatedEnvironmentDescription<SE,ME> description, Progression taskProgression);

	/**
	 * Invoked to detect influence conflicts and solve them.
	 * After solving, select the better environmental actions
	 * and apply them on environmental objects.
	 * <p>
	 * This function calls the time manager to change time.
	 */
	public void behaviour();
	
	/**
	 * Invoked to destroy the environment.
	 */
	public void end();

	/* ********************************************** Time management *** */

	/**
	 * Replies the simulation clock used by this environment.
	 * 
	 * @return the simulation clock.
	 */
	public Clock getSimulationClock();

	/* ********************************************** Probe management *** */

	/**
	 * Replies the manager of environmental probes.
	 * 
	 * @return the manager or <code>null</code> if none.
	 */
	public EnvironmentalProbes getProbeManager();
	
	/* ********************************************** Entity management *** */
	
	/**
	 * Returns the entity manager used to dynamically modify entity population.
	 * @return the entity manager used to dynamically modify entity population.
	 */
	public DynamicEntityManager<ME> getDynamicEntityManager();
	
	/* ********************************************** Events *** */
	
	/** Add simulation listener.
	 * 
	 * @param listener
	 */
	public void addJasimSimulationListener(JasimSimulationListener<EA,SE,ME> listener);
	
	/** Remove simulation listener.
	 * 
	 * @param listener
	 */
	public void removeJasimSimulationListener(JasimSimulationListener<EA,SE,ME> listener);

}
