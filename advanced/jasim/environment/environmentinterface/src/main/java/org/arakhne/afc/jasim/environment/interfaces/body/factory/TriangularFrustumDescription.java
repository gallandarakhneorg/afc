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
public class TriangularFrustumDescription
extends FrustumDescription {

	private final double nearDistance;
	private final double farDistance;
	private final double hAngle;
	private final double vAngle;

	/**
	 * @param eyePosition is the distance between the feet and the eyes.
	 * @param nearDistance is the distance to the near plance.
	 * @param farDistance is the distance to the near plance.
	 * @param hAngle is the horizontal openness angle of the frustum. 
	 * @param vAngle is the vertical openness angle of the frustum. 
	 */
	public TriangularFrustumDescription(
			double eyePosition, double nearDistance,
			double farDistance, double hAngle, double vAngle) {
		super(FrustumType.PYRAMID, eyePosition);
		this.nearDistance = nearDistance;
		this.farDistance = farDistance;
		this.hAngle = hAngle;
		this.vAngle = vAngle;
	}
	
	/** Replies the distance to the near plane.
	 * 
	 * @return the distance to the near plane.
	 */
	public double getNearDistance() {
		return this.nearDistance;
	}
	
	/** Replies the distance to the far plane.
	 * 
	 * @return the distance to the far plane.
	 */
	public double getFarDistance() {
		return this.farDistance;
	}

	/** Replies the horizontal openness angle of the frustum.
	 * 
	 * @return the horizontal openness angle of the frustum.
	 */
	public double getHorizontalAngle() {
		return this.hAngle;
	}

	/** Replies the vertical openness angle of the frustum.
	 * 
	 * @return the vertical openness angle of the frustum.
	 */
	public double getVerticalAngle() {
		return this.vAngle;
	}

}
