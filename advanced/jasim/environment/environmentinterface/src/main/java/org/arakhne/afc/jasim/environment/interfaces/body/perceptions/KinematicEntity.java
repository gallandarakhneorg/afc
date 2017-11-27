/*
 * 
 * $Id$
 * 
 * Copyright (c) 2006-09, 2012, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package org.arakhne.afc.jasim.environment.interfaces.body.perceptions;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.physics.kinematic.angular.AngularVelocityKinematic;
import fr.utbm.set.physics.kinematic.linear.LinearVelocityKinematic;

/**
 * This interface describes an object which has a
 * kinematic behaviour.
 * <p>
 * Kinematic behaviours include only position and orientation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface KinematicEntity extends LinearVelocityKinematic, AngularVelocityKinematic {

	//------------------------
	// POSITION
	//------------------------
	
	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 3D environment is a space point.
	 * 
	 * @return the position in the space.
	 */
	public EuclidianPoint3D getPosition3D();
	
	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 2.5D environment is a space point.
	 * 
	 * @return the position in space
	 */
	public EuclidianPoint3D getPosition2D5();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 2D environment is a plan point.
	 * 
	 * @return the position on a plane
	 */
	public EuclidianPoint2D getPosition2D();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 1.5D environment is a curviline position (x) and
	 * a shifting distance (y).
	 * 
	 * @return the curviline position (x) and a shifting distance (y)
	 */
	public Point1D5 getPosition1D5();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 1D environment is a curviline position.
	 * 
	 * @return the curviline position
	 */
	public Point1D getPosition1D();

	//------------------------
	// ORIENTATION
	//------------------------

	/**
	 * Returns the orientation of this body in its environment
	 * <p>
	 * The orientation is composed of a view direction and
	 * a rotation angle around this view axis.
	 * 
	 * @return the orientation of the situated agent in its environment
	 */
	public Direction3D getViewDirection3D();

	/**
	 * Returns the orientation of this body in its environment
	 * <p>
	 * The orientation is a 2D vector representing
	 * the view direction.
	 * 
	 * @return the orientation of the situated agent in its environment
	 */
	public Direction2D getViewDirection2D();

	/**
	 * Returns the orientation of this body in its environment
	 * 
	 * @return the orientation of the situated agent in its environment
	 */
	public Direction1D getViewDirection1D5();

	/**
	 * Returns the orientation of this body in its environment
	 * 
	 * @return the orientation of the situated agent in its environment
	 */
	public Direction1D getViewDirection1D();
	
	/**
	 * Returns the 3D movement unit vector of this body.
	 * 
	 * @return movement unit vector.
	 */
	public Vector3d getLinearUnitVelocity3D();

	/**
	 * Returns the 2D movement unit vector of this body.
	 * 
	 * @return movement unit vector.
	 */
	public Vector2d getLinearUnitVelocity2D();

	/**
	 * Returns the 1.5D movement unit vector of this body.
	 * 
	 * @return movement unit vector.
	 */
	public Vector2d getLinearUnitVelocity1D5();

	/**
	 * Returns the 1D movement unit vector of this body.
	 * <p>
	 * Same as {@link #getLinearSpeed()}.
	 * 
	 * @return movement unit vector.
	 */
	public double getLinearUnitVelocity1D();

}
