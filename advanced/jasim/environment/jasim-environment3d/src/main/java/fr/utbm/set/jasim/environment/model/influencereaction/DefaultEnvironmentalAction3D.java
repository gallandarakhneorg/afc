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
package fr.utbm.set.jasim.environment.model.influencereaction;

import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.time.Clock;

/** This class describes an action inside a 3d situated environment.
 * <p>
 * <em>The embedded transformation is local.</em>
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultEnvironmentalAction3D extends AbstractEnvironmentalAction implements EnvironmentalAction3D {
	
	private static final long serialVersionUID = -7175527302328772853L;
	
	private final Transform3D transform;
	
	/**
	 * @param influencedObject is the object that must be influenced.
	 * @param clock is the date at which the action occured.
	 * @param transformation is the transformation to apply.
	 */
	public DefaultEnvironmentalAction3D(Object influencedObject, Clock clock, Transform3D transformation) {
		super(influencedObject, clock);
		this.transform = transformation;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Transform3D getTransformation() {
		return this.transform;
	}

}