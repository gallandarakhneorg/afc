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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * This class is a container storing all required information
 * about a place in the simulation
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PlaceInfo implements Serializable {
	
	private static final long serialVersionUID = -4252483732945650733L;

	private final UUID placeId;
	private final String placeName;
	private final GroundInfo ground;
	private final Collection<MobileEntitySpawningInfo> entities;
	
	/**
	 * @param placeId is the identifier of the place. If <code>null</code> the identifier of the place will be the identifier of the ground.
	 * @param placeName is the name of the place.
	 * @param ground describes the ground of the place.
	 * @param entities are the mobile entities already spawned in the place.
	 */
	public PlaceInfo(UUID placeId, String placeName, GroundInfo ground, Collection<MobileEntitySpawningInfo> entities) {
		assert(ground!=null);
		this.placeId = placeId;
		this.placeName = placeName;
		this.ground = ground;
		this.entities = entities;
	}

	/** Replies the identifier of the place.
	 * 
	 * @return an identifier, never <code>null</code>.
	 */
	public UUID getId() {
		if (this.placeId!=null) return this.placeId;
		return this.ground.getId();
	}
	
	/** Replies the name of the place.
	 * 
	 * @return a name, or <code>null</code>.
	 */
	public String getName() {
		return this.placeName;
	}

	/** Replies the ground of the place.
	 * 
	 * @return the ground, never <code>null</code>.
	 */
	public GroundInfo getGround() {
		return this.ground;
	}

	/** Replies the list of entities spawned in the place.
	 * @return a list of entities, never <code>null</code>
	 */
	public Collection<MobileEntitySpawningInfo> getEntities() {
		if (this.entities==null) return Collections.emptyList();
		return this.entities;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		UUID id = getId();
		if (id==null) return super.toString();
		return id.toString();
	}

}