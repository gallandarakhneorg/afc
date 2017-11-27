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
package org.arakhne.afc.jasim.environment.interfaces.body.frustums;

import java.io.Serializable;
import java.util.UUID;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.object.EuclidianPoint;

/** This interface representes a perception frustum.
 *
 * @param <B> is the type of the bounds supported by the frustum.
 * @param <P> is the type of the points suppored by this classifier as returned values
 * @param <PP> is the type of the points suppored by this classifier as parameters
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Frustum<B extends Bounds<P,PP,?>,P extends EuclidianPoint,PP> extends Cloneable, Serializable {

	/** Clone this frustum.
	 * 
	 * @return a clone
	 */
	public Frustum<B,P,PP> clone();
	
	/** Replies the identifier of the frustum.
	 * 
	 * @return the frustum identifier.
	 */
	public UUID getIdentifier();
	
	/**
	 * Classify a box with respect to the classifier.
	 * 
	 * @param box
	 * @return the value {@link IntersectionType#INSIDE} if the <var>box</var> is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the <var>box</var> is outside this classifier;
	 * {@link IntersectionType#ENCLOSING} if the <var>box</var> is enclosing this classifier;
	 * {@link IntersectionType#SPANNING} otherwhise.
	 */
	public IntersectionType classifies(B box);

	/**
	 * Replies if the specified box intersects this classifier.
	 * 
	 * @param box
	 * @return <code>true</code> if the box is intersecting this classifier,
	 * otherwise <code>false</code>
	 */
	public boolean intersects(B box);

	/**
	 * Classify a point.
	 * 
	 * @param p is a point
	 * @return the value {@link IntersectionType#INSIDE} if the point is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the point is outside this classifier;
	 * {@link IntersectionType#SPANNING} if the point is on the classifier.
	 */
	public IntersectionType classifies(PP p);

	/**
	 * Replies if the specified point intersects this classifier.
	 * 
	 * @param p is a point
	 * @return <code>true</code> if an intersection exists, otherwise <code>false</code>
	 */
	public boolean intersects(PP p);

	/**
	 * Classify an area/box. 
	 *
	 * @param l is the lower point of the box
	 * @param u is the upper point of the box
	 * @return the value {@link IntersectionType#INSIDE} if the box is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the box is outside this classifier;
	 * {@link IntersectionType#ENCLOSING} if the box is enclosing this classifier;
	 * {@link IntersectionType#SPANNING} otherwhise.
	 */
	public IntersectionType classifies(PP l, PP u);

	/**
	 * Replies if the specified area intersects this classifier.
	 *
	 * @param l is the lower point of the box
	 * @param u is the upper point of the box
	 * @return the value {@link IntersectionType#INSIDE} if the box is inside this classifier;
	 * {@link IntersectionType#OUTSIDE} if the box is outside this classifier;
	 * {@link IntersectionType#ENCLOSING} if the box is enclosing this classifier;
	 * {@link IntersectionType#SPANNING} otherwhise.
	 */
	public boolean intersects(PP l, PP u);

	/** Replies the far distance at which an object <i>could</i> be inside the frustum.
     * <p>
     * The semantic of the far distance could differs from the type of frustum.
     * 
     * @return always positive or null
     */
    public double getFarDistance();

    /** Replies the distance under which no object is assumed to be in the frustum.
     * <p>
     * The semantic of the near distance could differs from the type of frustum.
     * 
     * @return always positive or null
     */
    public double getNearDistance();

    /** Replies the location of the eye.
     * 
     * @return the list of the eye coordinates.
     */
    public P getEye();

}