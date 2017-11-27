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
package org.arakhne.afc.jasim.environment.model.place;

import java.util.UUID;

/**
 * Describes a {@link Portal portal} in a situated environment during
 * its initialization stage.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PortalDescription {

	private final UUID placeId1;
	private final UUID placeId2;

	private PortalPosition position1;
	private PortalPosition position2;

	/**
	 * @param id1 is the identifier of the first attached place.
	 * @param id2 is the identifier of the second attached place.
	 */
	public PortalDescription(UUID id1, UUID id2) {
		assert(id1!=null);
		assert(id2!=null);
		assert(!id1.equals(id2));
		this.placeId1 = id1;
		this.placeId2 = id2;
	}
	
	/** Replies the identifier of the first attached place.
	 * 
	 * @return an identifier
	 */
	public UUID getFirstPlaceIdentifier() {
		return this.placeId1;
	}
	
	/** Replies the identifier of the second attached place.
	 * 
	 * @return an identifier
	 */
	public UUID getSecondPlaceIdentifier() {
		return this.placeId2;
	}
	
	/** Set the position of the portal on the first place.
	 * 
	 * @param position is the position of the portal on the first place.
	 */
	public void setPositionOnFirstPlace(PortalPosition position) {
		this.position1 = position;
	}
		
	/** Set the position of the portal on the second place.
	 * 
	 * @param position is the position of the portal on the second place.
	 */
	public void setPositionOnSecondPlace(PortalPosition position) {
		this.position2 = position;
	}

	/** Replies the position of the portal on the first place.
	 * 
	 * @return the position of the portal on the first place.
	 */
	public PortalPosition getPositionOnFirstPlace() {
		return this.position1;
	}

	/** Replies the position of the portal on the second place.
	 * 
	 * @return the position of the portal on the second place.
	 */
	public PortalPosition getPositionOnSecondPlace() {
		return this.position2;
	}

}
