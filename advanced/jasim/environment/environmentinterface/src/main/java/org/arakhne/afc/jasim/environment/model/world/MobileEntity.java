/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, 2012 Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package org.arakhne.afc.jasim.environment.model.world;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.physics.kinematic.angular.AngularInstantAccelerationKinematic;
import fr.utbm.set.physics.kinematic.linear.LinearInstantAccelerationKinematic;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencable;
import org.arakhne.afc.jasim.environment.model.place.Place;

/** This interface representes an object in a 1D, 2D or 3D space
 * that has the property to be mobile.
 * <p>
 * A mobile entity is stored inside a perception/action tree.
 *
 * @param <B> is the type of the bounding box associated to this entity.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface MobileEntity<B extends Bounds<?,?,?>>
extends WorldEntity<B>, Influencable,
		LinearInstantAccelerationKinematic,
		AngularInstantAccelerationKinematic {

	/** Replies the place in which this entity lies.
	 * 
	 * @return the place, never <code>null</code>
	 */
	public Place<?,?,? extends MobileEntity<B>> getPlace();
	
	/** Set the place in which this entity lies.
	 * 
	 * @param place is the place, or <code>null</code>
	 */
	public void setPlace(Place<?,?, ? extends MobileEntity<B>> place);
	
}