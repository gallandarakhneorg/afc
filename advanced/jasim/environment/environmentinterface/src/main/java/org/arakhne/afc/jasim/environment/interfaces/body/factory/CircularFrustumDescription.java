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
package org.arakhne.afc.jasim.environment.interfaces.body.factory;

/**
 * This class is describing a frustum to spawn.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CircularFrustumDescription
extends FrustumDescription {

	private final double radius;

	/**
	 * @param eyePosition is the distance between the feet and the eyes.
	 * @param radius is the radius of the frustum.
	 */
	public CircularFrustumDescription(double eyePosition, double radius) {
		super(FrustumType.SPHERE, eyePosition);
		this.radius = radius;
	}
	
	/** Replies the radius of the frustum.
	 * 
	 * @return the radius of the frustum.
	 */
	public double getRadius() {
		return this.radius;
	}
	
}
