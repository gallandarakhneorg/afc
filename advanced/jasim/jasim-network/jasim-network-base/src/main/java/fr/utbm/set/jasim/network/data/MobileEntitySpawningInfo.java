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

import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

/**
 * This class is a container storing all required information
 * about a mobile/dynamic entity in the simulation
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MobileEntitySpawningInfo implements Serializable {
	
	private static final long serialVersionUID = -8716506635523025704L;

	/**
	 * Id of the entity
	 */
	private final UUID id;
	
	/**
	 * Position of the entity
	 */
	private final Point3d position;
	
	/**
	 * Pivot of the entity
	 */
	private final Vector3d pivot;
	
	/**
	 * Orientation of the entity
	 */
	private final Quat4d orientation;
	
	/**
	 * Type of the entity body
	 */		
	private final String type;

	/**
	 * @param id is the identifier of themobile entity.
	 * @param position is the position of the mobile entity.
	 * @param pivot is the pivot point associated to the mobile entity.
	 * @param orientation is the orientation of the mobile entity.
	 * @param bodyType is the type of the mobile entity.
	 */
	public MobileEntitySpawningInfo(UUID id, Point3d position, Vector3d pivot, Quat4d orientation, String bodyType) {
		assert(id != null && position != null && pivot != null && orientation != null && bodyType != null);
		this.id = id;
		this.position = position;
		this.pivot = pivot;
		this.orientation = orientation;
		this.type = bodyType;
	}
	
	/**
	 * @param id is the identifier of themobile entity.
	 * @param position is the position of the mobile entity.
	 * @param pivot is the pivot point associated to the mobile entity.
	 * @param orientation is the orientation of the mobile entity.
	 */
	public MobileEntitySpawningInfo(UUID id, Point3d position, Vector3d pivot, Quat4d orientation) {
		assert(id != null && position != null && pivot != null && orientation != null );
		this.id = id;
		this.position = position;
		this.pivot = pivot;
		this.orientation = orientation;
		this.type = null;
	}		

	/** Replies the type of the entity body.
	 * 
	 * @return the type of the body or <code>null</code> if unknown.
	 */
	public String getBodyType() { 
		return this.type;
	}

	/** Replies the identifier of the body.
	 * 
	 * @return an identifier, never <code>null</code>.
	 */
	public UUID getId() {
		return this.id;
	}

	/** Replies the position of the entity.
	 * 
	 * @return a point, never <code>null</code>
	 */
	public Point3d getPosition() {
		return this.position;
	}

	/** Replies the pivot associated to the entity.
	 * <p>
	 * The pivot point is relative to the current entity position.
	 * 
	 * @return a point, never <code>null</code>
	 */
	public Vector3d getPivot() {
		return this.pivot;
	}

	/** Replies the orientation of the entity.
	 * 
	 * @return an axis-angle object, never <code>null</code>
	 */
	public Quat4d getOrientation() {
		return this.orientation;
	}
	
}