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
package fr.utbm.set.jasim.environment.model.place;

import java.util.UUID;

import fr.utbm.set.geom.object.EuclidianPoint;

/**
 * A container of {@link Place places}
 * 
 * @param <PLACE> is the type of the supported places.
 * @param <P> is the type of the supported euclidian points.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PlaceContainer<PLACE extends Place<?,?,?>,P> {
	
	/**
	 * Replies the place which is the best one to manage the given location.
	 * <p>
	 * The default place selection is based on a keep on floor approach.
	 * 
	 * @param position
	 * @return the place or <code>null</code> if no place match.
	 */
	public PLACE getPlaceObject(P position);

	/**
	 * Replies the place which is the best one to manage the given location.
	 * <p>
	 * The default place selection is based on a keep on floor approach.
	 * 
	 * @param position
	 * @return the place or <code>null</code> if no place match.
	 */
	public PLACE getPlaceObject(EuclidianPoint position);

	/**
	 * Replies the place with the given identifier.
	 * 
	 * @param placeName is the name of the place to retreive.
	 * @return the place or <code>null</code> if no place match.
	 */
	public PLACE getPlaceObject(UUID placeName);

}
