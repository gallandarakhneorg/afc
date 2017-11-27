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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.arakhne.afc.jasim.environment.model.place.PlaceDescription;
import org.arakhne.afc.jasim.environment.model.place.PortalDescription;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;
import org.arakhne.afc.jasim.environment.time.TimeManager;

/**
 * Describes a {@link SituatedEnvironment situated environment} during
 * its initialization stage.
 * 
 * @param <SE> is the type of static entity supported by this environment.
 * @param <ME> is the type of mobile entity supported by this environment.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SituatedEnvironmentDescription<SE extends WorldEntity<?>,ME extends MobileEntity<?>> {

	private final UUID id;
	private final TimeManager tm;
	private final List<PlaceDescription<SE,ME>> places = new ArrayList<PlaceDescription<SE,ME>>();
	private final List<PortalDescription> portals = new ArrayList<PortalDescription>();
	private String name = null;
	private Date date = null;
	private String authors = null;
	private String version = null;
	private String description = null;
	
	/**
	 * @param id is the identifier of the environment.
	 * @param tm is the time manager used by the simulation, never <code>null</code>.
	 */
	public SituatedEnvironmentDescription(UUID id, TimeManager tm) {
		assert(id!=null && !"".equals(id)); //$NON-NLS-1$
		assert(tm!=null);
		this.id = id;
		this.tm = tm;
	}
	
	/** Replies the identifier of the simulation scenario.
	 * 
	 * @return the scenario id
	 */
	public UUID getIdentifier() {
		return this.id;
	}

	/** Replies the name of the scenario.
	 * 
	 * @return the scenario name
	 */
	public String getName() {
		return this.name;
	}

	/** Set the name of the scenario.
	 * 
	 * @param name is the scenario name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Replies the date of creation of the scenario.
	 * 
	 * @return the scenario date
	 */
	public Date getDate() {
		return this.date;
	}

	/** Set the date of creation of the scenario.
	 * 
	 * @param date is the scenario date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/** Replies the authors of the scenario.
	 * 
	 * @return the scenario authors
	 */
	public String getAuthors() {
		return this.authors;
	}

	/** Set the authors of the scenario.
	 * 
	 * @param authors are the scenario authors
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	/** Replies the version of the scenario.
	 * 
	 * @return the scenario version
	 */
	public String getVersion() {
		return this.version;
	}

	/** Set the version of the scenario.
	 * 
	 * @param version is the scenario version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/** Replies the description of the scenario.
	 * 
	 * @return the scenario description
	 */
	public String getDescription() {
		return this.description;
	}

	/** set the description of the scenario.
	 * 
	 * @param description is the scenario description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/** Replies the time manager used by the simulation.
	 * 
	 * @return the time manager
	 */
	public TimeManager getTimeManager() {
		return this.tm;
	}

	/** Add a place description.
	 * 
	 * @param place describes the new place
	 */
	public void addPlace(PlaceDescription<SE,ME> place) {
		this.places.add(place);
	}
	
	/** Remove a place description.
	 * 
	 * @param place describes the place to remove
	 */
	public void removePlace(PlaceDescription<SE,ME> place) {
		this.places.remove(place);
	}
	
	/** Replies the list of places.
	 *
	 * @return the list of places.
	 */
	public Iterable<PlaceDescription<SE,ME>> getPlaces() {
		return this.places;
	}

	/** Replies the count of places.
	 *
	 * @return the count of places.
	 */
	public int getPlaceCount() {
		return this.places.size();
	}

	/** Add a portal description.
	 * 
	 * @param portal describes the new portal
	 */
	public void addPortal(PortalDescription portal) {
		this.portals.add(portal);
	}
	
	/** Remove a portal description.
	 * 
	 * @param portal describes the portal to remove
	 */
	public void removePortal(PortalDescription portal) {
		this.portals.remove(portal);
	}
	
	/** Replies the list of portals.
	 *
	 * @return the list of portals.
	 */
	public Iterable<PortalDescription> getPortals() {
		return this.portals;
	}
	
	/** Replies the count of portals.
	 *
	 * @return the count of portals.
	 */
	public int getPortalCount() {
		return this.portals.size();
	}

}
