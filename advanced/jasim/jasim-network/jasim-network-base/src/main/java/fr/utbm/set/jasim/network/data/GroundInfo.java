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

import javax.vecmath.Point2d;

/**
 * This class is a container storing all required information
 * about a ground in the simulation
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GroundInfo implements Serializable {
	
	private static final long serialVersionUID = -6941517188539707604L;

	private final UUID id;
	
	private final double minx, miny, minz;
	
	private final double maxx, maxy, maxz;
	
	private final String resourceName;
	
	private Object groundModel = null;

	/**
	 * @param id is the identifier of the ground.
	 * @param minx is the minimum point of the ground.
	 * @param miny is the minimum point of the ground.
	 * @param minz is the minimum height of the ground.
	 * @param maxx is the maximum point of the ground.
	 * @param maxy is the maximum point of the ground.
	 * @param maxz is the maximum height of the ground.
	 * @param resource is the name of the resource used to load the terrain, or <code>null</code>.
	 */
	public GroundInfo(UUID id, double minx, double miny, double minz, double maxx, double maxy, double maxz, String resource) {
		assert(id != null);
		this.id = id;
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
		this.maxz = maxz;
		this.minz = minz;
		this.resourceName = resource;
	}
	
	/** Replies the identifier of the ground.
	 * 
	 * @return an identifier, never <code>null</code>.
	 */
	public UUID getId() {
		return this.id;
	}

	/** Replies the smallest point on the ground.
	 * 
	 * @return a point, never <code>null</code>
	 */
	public Point2d getMinPoint() {
		return new Point2d(this.minx,this.miny);
	}

	/** Replies the highest point on the ground.
	 * 
	 * @return a point, never <code>null</code>
	 */
	public Point2d getMaxPoint() {
		return new Point2d(this.maxx,this.maxy);
	}
	
	/** Replies the name of the resource used to load the terrain.
	 * 
	 * @return the name of the resource used to load the terrain.
	 */
	public String getResourceName() {
		return this.resourceName;
	}
	
	/** Replies the highest point on the ground.
	 * 
	 * @return an height
	 */
	public double getMaxHeight() {
		return this.maxz;
	}

	/** Replies the lowest point on the ground.
	 * 
	 * @return an height
	 */
	public double getMinHeight() {
		return this.minz;
	}
	
	/** Replies the ground model embedded in this info object.
	 * <p>
	 * The ground model is application dependent.
	 * 
	 * @param <T> is the type of ground model to reply
	 * @param type is the type of ground model to reply
	 * @return a ground model or <code>null</code>
	 */
	public <T> T getGroundModel(Class<T> type) {
		if (this.groundModel!=null && type.isInstance(this.groundModel)) {
			return type.cast(this.groundModel);
		}
		return null;
	}

	/** Set the ground model embedded in this info object.
	 * <p>
	 * The ground model is application dependent.
	 * 
	 * @param model is a ground model or <code>null</code>
	 */
	public void setGroundModel(Object model) {
		this.groundModel = model;
	}

}