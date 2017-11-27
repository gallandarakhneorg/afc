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
package fr.utbm.set.jasim.io.sfg;

import java.util.UUID;

import javax.vecmath.Vector2d;

import fr.utbm.set.geom.object.Direction1D;

/**
 * Set of parameters to initialize a portal.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PortalSimulationParameterSet extends AbstractParameter {

	private final PositionParameter source1;
	private final PositionParameter source2;
	private final boolean onLeft;
	private final Direction1D portalEntrySide;
	private final PositionParameter target;
	private final Vector2d direction;
	private final Direction1D direction1d;
	private final UUID sourceId;
	private final UUID targetId;
	
	/**
	 * @param dimension must be one of <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 * @param sourcePlace
	 * @param sourceGatePosition1
	 * @param sourceGatePosition2
	 * @param openOnLeft
	 * @param targetPlace
	 * @param targetGatePosition
	 * @param vx
	 * @param vy
	 */
	PortalSimulationParameterSet(float dimension, UUID sourcePlace, PositionParameter sourceGatePosition1, PositionParameter sourceGatePosition2, boolean openOnLeft, UUID targetPlace, PositionParameter targetGatePosition, double vx, double vy) {
		super(dimension);
		this.sourceId = sourcePlace;
		this.targetId = targetPlace;
		this.source1 = sourceGatePosition1;
		this.source2 = sourceGatePosition2;
		this.onLeft = openOnLeft;
		this.target = targetGatePosition;
		this.direction = new Vector2d(vx,vy);
		this.portalEntrySide = null;
		this.direction1d = null;
	}
	
	/**
	 * @param dimension must be one of <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 * @param sourcePlace
	 * @param sourceGatePosition1
	 * @param sourceGatePosition2
	 * @param traversableDirection
	 * @param targetPlace
	 * @param targetGatePosition
	 * @param outDirection
	 */
	PortalSimulationParameterSet(float dimension, UUID sourcePlace, PositionParameter sourceGatePosition1, PositionParameter sourceGatePosition2, Direction1D traversableDirection, UUID targetPlace, PositionParameter targetGatePosition, Direction1D outDirection) {
		super(dimension);
		this.sourceId = sourcePlace;
		this.targetId = targetPlace;
		this.source1 = sourceGatePosition1;
		this.source2 = sourceGatePosition2;
		this.onLeft = false;
		this.portalEntrySide = traversableDirection;
		this.target = targetGatePosition;
		this.direction = null;
		this.direction1d = outDirection;
	}

	/** Replies the identifier of the place on which this portal
	 * is taking its source.
	 * 
	 * @return an identifier, never <code>null</code>
	 */
	public UUID getSourcePlaceIdentifier() {
		return this.sourceId;
	}	
	
	/** Replies the identifier of the place on which this portal
	 * is taking its destination.
	 * 
	 * @return an identifier, never <code>null</code>
	 */
	public UUID getTargetPlaceIdentifier() {
		return this.targetId;
	}	

	/** Replies the position of the first point of the source portal.
	 * 
	 * @return the position of the portal source.
	 */
	public PositionParameter getP1() {
		return this.source1;
	}
	
	/** Replies the position of the second point of the source portal.
	 * 
	 * @return the position of the portal source.
	 */
	public PositionParameter getP2() {
		return this.source2;
	}
	
	/** Replies if the portal source is accessible from the left or from the right,
	 * assuming that the used direction is from the point P1 to the point P2.
	 * 
	 * @return <code>true</code> if the portal entry in on the left, otherwise <code>false</code>
	 */
	public boolean isPortalEntryOnLeft2D() {
		return this.onLeft;
	}

	/** Replies if the portal source is accessible from the segment direction,
	 * the reverse direction or both.
	 * 
	 * @return the direction.
	 */
	public Direction1D getPortalEntySide1D() {
		return this.portalEntrySide;
	}

	/** Replies the position of the target portal.
	 * 
	 * @return the position of the portal target.
	 */
	public PositionParameter getTargetPosition() {
		return this.target;
	}

	/** Replies the default orientation when exiting from the target portal.
	 * 
	 * @return the orientation when exiting the portal.
	 */
	public Vector2d getTargetOrientation2D() {
		return this.direction;
	}

	/** Replies the default orientation when exiting from the target portal.
	 * 
	 * @return the orientation when exiting the portal.
	 */
	public Direction1D getTargetOrientation1D() {
		return this.direction1d;
	}

}
