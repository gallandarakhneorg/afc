/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.network.data;

import java.io.Serializable;
import java.util.UUID;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

/**
 * This class is a container storing all required information
 * about a movement of a mobile/dynamic entity in the simulation
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MobileEntityMoveInfo implements Serializable {
	
	private static final long serialVersionUID = -208330871951913385L;

	/**
	 * Id of the entity
	 */
	private final UUID id;
	
	/**
	 * Position the entity
	 */
	private final Point3d position;
	
	/**
	 * Orientation the entity
	 */
	private final Quat4d orientation;

	/**
	 * Move of the entity
	 */
	private final Vector3d linearMove;
	
	/**
	 * Rotation of the entity
	 */
	private final Quat4d angularMove;
	
	/**
	 * Linear velocity
	 */
	private final double linearVelocity;

	/**
	 * Angular velocity
	 */
	private final double angularVelocity;

	/**
	 * @param entityId is the identifier of the entity
	 * @param move is the relative movement of the entity
	 * @param rotation is the relative rotation of the entity
	 * @param position is the global position of the entity
	 * @param orientation is the global orientation of the entity
	 * @param linearVelocity is the current linear speed of the entity.
	 * @param angularVelocity is the current angular speed of the entity.
	 */
	public MobileEntityMoveInfo(UUID entityId, Vector3d move, Quat4d rotation, Point3d position, Quat4d orientation, double linearVelocity, double angularVelocity) {
		assert(entityId != null && position != null && orientation!=null && rotation != null && move != null);
		this.id = entityId;
		this.linearMove = move;
		this.angularMove = rotation;
		this.position = position;
		this.orientation = orientation;
		this.linearVelocity = linearVelocity;
		this.angularVelocity = angularVelocity;
	}
	
	/** Replies the identifier of the body.
	 * 
	 * @return an identifier, never <code>null</code>.
	 */
	public UUID getId() {
		return this.id;
	}

	/** Replies the global position and orientation of the
	 * entity after the movement and rotation were applied.
	 * 
	 * @return a point, never <code>null</code>
	 */
	public Matrix4d getMatrix() {
		Matrix4d m = new Matrix4d();
		m.setRotation(this.orientation);
		m.setTranslation(new Vector3d(this.position));
		return m;
	}

	/** Replies the position of the entity after the movement was applied.
	 * 
	 * @return a point, never <code>null</code>
	 */
	public Point3d getPosition() {
		return this.position;
	}

	/** Replies the last move of the entity.
	 * 
	 * @return a vector, never <code>null</code>
	 */
	public Vector3d getLastMove() {
		return this.linearMove;
	}

	/** Replies the orientation of the entity after the rotation was applied.
	 * 
	 * @return an axis-angle object, never <code>null</code>
	 */
	public AxisAngle4d getAxisAngle4d() {
		AxisAngle4d aa = new AxisAngle4d();
		aa.set(this.orientation);
		return aa;
	}
	
	/** Replies the orientation of the entity after the rotation was applied.
	 * 
	 * @return an axis-angle object, never <code>null</code>
	 */
	public Quat4d getQuaternion() {
		return this.orientation;
	}

	/** Replies the last rotation of the entity.
	 * 
	 * @return an axis-angle object, never <code>null</code>
	 */
	public Quat4d getLastRotation() {
		return this.angularMove;
	}

	/** Replies the current linear velocity.
	 * 
	 * @return the linear velocity
	 */
	public double getCurrentLinearVelocity() {
		return this.linearVelocity;
	}

	/** Replies the current angular velocity.
	 * 
	 * @return the angular velocity
	 */
	public double getCurrentAngularVelocity() {
		return this.angularVelocity;
	}

}