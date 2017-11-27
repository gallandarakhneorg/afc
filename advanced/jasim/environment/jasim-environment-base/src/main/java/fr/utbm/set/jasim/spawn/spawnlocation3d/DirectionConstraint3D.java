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
package fr.utbm.set.jasim.spawn.spawnlocation3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.math.MathConstants;

/**
 * This object describes the available direction quadrant.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DirectionConstraint3D {
	
	/** Starting angle.
	 */
	public final double startAngle;
	
	/** Ending angle.
	 */
	public final double endAngle;
	
	/** Distance between the start and end angles.
	 */
	public final double delta;

	/**
	 * @param startAngle
	 * @param endAngle
	 */
	public DirectionConstraint3D(double startAngle, double endAngle) {
		this.startAngle = GeometryUtil.clampRadian0To2PI(startAngle);
		double a = GeometryUtil.clampRadian0To2PI(endAngle);
		while (a<this.startAngle) {
			a += MathConstants.TWO_PI;
		}
		this.endAngle = a;
		assert(this.endAngle>=this.startAngle);
		this.delta = this.endAngle - this.startAngle;
	}
	
}
