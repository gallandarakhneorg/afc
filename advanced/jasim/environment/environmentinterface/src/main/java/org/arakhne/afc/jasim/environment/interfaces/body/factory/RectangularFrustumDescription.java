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
public class RectangularFrustumDescription
extends FrustumDescription {

	private final double farDistance;
	private final double leftRightDistance;
	private final double bottomUpDistance;

	/**
	 * @param eyePosition is the distance between the feet and the eyes.
	 * @param farDistance is the distance between the eye and the farest plane.
	 * @param leftRightDistance is the distance between the left and the rights planes.
	 * @param bottomUpDistance is the distance between the bottom and the up planes.
	 */
	public RectangularFrustumDescription(
			double eyePosition,
			double farDistance, double leftRightDistance, double bottomUpDistance) {
		super(FrustumType.RECTANGLE, eyePosition);
		this.farDistance = farDistance;
		this.leftRightDistance = leftRightDistance;
		this.bottomUpDistance = bottomUpDistance;
	}
	
	/** Replies the distance between the eye and the far plane.
	 * 
	 * @return the distance between the eye and the far plane.
	 */
	public double getFarDistance() {
		return this.farDistance;
	}
	
	/** Replies the distance between the left and the right planes.
	 * 
	 * @return the distance between the left and the right planes.
	 */
	public double getLeftRightDistance() {
		return this.leftRightDistance;
	}

	/** Replies the distance between the bottom and the up planes.
	 * 
	 * @return the distance between the bottom and the up planes.
	 */
	public double getBottomUpDistance() {
		return this.bottomUpDistance;
	}

}
