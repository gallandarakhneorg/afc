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
package fr.utbm.set.jasim.environment.model.ground;

import java.util.UUID;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.bounds.bounds2d.OrientedBoundingRectangle;
import fr.utbm.set.jasim.environment.semantics.GroundType;

/** This class representes an indoor ground which has bounds
 * not always aligned on world axes.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedIndoorGround extends AbstractGround {

	private final OrientedBoundingRectangle area;
	
	/**
	 * Create an oriented indoor ground.
	 * 
	 * @param id is the identifier of the ground
	 * @param area is the area covered by the ground
	 * @param floorHeight is the height of the ground
	 * @param semantic is the default semantic attached to the ground
	 */
	public OrientedIndoorGround(
			UUID id,
			OrientedBoundingRectangle area,
			float floorHeight,
			GroundType semantic) {
		super(
			id,
			area.getMinX(),
			area.getMinY(),
			floorHeight,
			area.getMaxX(),
			area.getMaxY(),
			floorHeight,
			semantic);
		this.area = area;
	}

	/**
	 * Create an oriented indoor ground.
	 * 
	 * @param id is the identifier of the ground
	 * @param area is the area covered by the ground
	 * @param floorHeight is the height of the ground
	 */
	public OrientedIndoorGround(
			UUID id,
			OrientedBoundingRectangle area,
			float floorHeight) {
		this(
			id,
			area,
			floorHeight,
			null);
	}

	/**
	 * Create an oriented indoor ground.
	 * 
	 * @param id is the identifier of the ground
	 * @param area is the area covered by the ground
	 * @param semantic is the default semantic attached to the ground
	 */
	public OrientedIndoorGround(
			UUID id,
			OrientedBoundingRectangle area,
			GroundType semantic) {
		this(
			id,
			area,
			0f,
			semantic);
	}

	/**
	 * Create an oriented indoor ground.
	 * 
	 * @param id is the identifier of the ground
	 * @param area is the area covered by the ground
	 */
	public OrientedIndoorGround(
			UUID id,
			OrientedBoundingRectangle area) {
		this(
			id,
			area,
			0f,
			null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getHeightAt(double x, double y) {
		if (this.area.intersects(new Point2d(x, y))) {
			return getMinZ();
		}
		return Double.NaN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTraversable(double x, double y) {
		return this.area.intersects(new Point2d(x, y));
	}

}