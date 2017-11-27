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
package org.arakhne.afc.jasim.environment.model.place;

import org.arakhne.afc.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import org.arakhne.afc.jasim.environment.model.world.MobileEntity;
import org.arakhne.afc.jasim.environment.model.world.WorldEntity;

/** This interface representes a portal between to {@link Place places}.
 * A portal is bi-directional.
 *
 * @param <EA> is the type of the environmental actions supported by this place.
 * @param <SE> is the type of the immobile entities supported by this environment.
 * @param <ME> is the type of the mobile entities supported by this environment.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Portal<EA extends EnvironmentalAction, SE extends WorldEntity<?>, ME extends MobileEntity<?>> {

	/** Replies if this portal is connected to the given place.
	 * 
	 * @param place
	 * @return <code>true</code> if one of the known places is the given one.
	 */
	public boolean isPlace(Place<EA,SE,ME> place);
	
	/** Replies the position of the portal in the given place.
	 * 
	 * @param place
	 * @return a description of the position.
	 * @throws IllegalArgumentException if the place is not known.
	 */
	public PortalPosition getPosition(Place<EA,SE,ME> place);

	/** Replies if the portal is traversable from the given place.
	 * 
	 * @param place
	 * @return <code>true</code> if the portal is traversable otherwise <code>false</code>
	 * @throws IllegalArgumentException if the place is not known.
	 */
	public boolean isTraversableFrom(Place<EA,SE,ME> place);

	/** Replies the place on the other side of the portal.
	 * 
	 * @param place
	 * @return a place or <code>null</code> if not traversable.
	 * @throws IllegalArgumentException if the place is not known.
	 */
	public Place<EA,SE,ME> getOtherSide(Place<EA,SE,ME> place);

}