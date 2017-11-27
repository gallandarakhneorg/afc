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
package org.arakhne.afc.jasim.environment.model.place;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


/** This interface describes a portal position in a {@link Place place}.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PortalPosition {

	/** Replies the position where an entity must arrive in the place when it has
	 * traversed the portal.
	 * 
	 * @return the target point 
	 */
	public Point2d getExitPoint2D();
	
	/** Replies the position where an entity must arrive in the place when it has
	 * traversed the portal.
	 * 
	 * @return the target point 
	 */
	public Point3d getExitPoint3D();

	/** Replies the default direction of the entity when arriving on the end place.
	 * 
	 * @return the direction unit vector. 
	 */
	public Vector2d getExitVector2D();

	/** Replies the default direction of the entity when arriving on the end place.
	 * 
	 * @return the direction unit vector. 
	 */
	public Vector3d getExitVector3D();

	/** Replies the position where an entity must enter in the portal.
	 * 
	 * @return the target point 
	 */
	public Point2d getEntryPoint2D();

	/** Replies the position where an entity must enter in the portal.
	 * 
	 * @return the target point 
	 */
	public Point3d getEntryPoint3D();

	/** Replies the direction of the portal entry.
	 * <p>
	 * The replied vector is the normal to the portal primitive.
	 * It is directed to the half-plane from which the portal
	 * is traversable.
	 * 
	 * @return the direction unit vector. 
	 */
	public Vector2d getEntryVector2D();

	/** Replies the direction of the portal entry.
	 * <p>
	 * The replied vector is the normal to the portal primitive.
	 * It is directed to the half-space from which the portal
	 * is traversable.
	 * 
	 * @return the direction unit vector. 
	 */
	public Vector3d getEntryVector3D();

	/** Replies the given start position and arriving position permits to pass through
	 * the portal.
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @return the target point 
	 */
	public boolean isTraversable(double startX, double startY, double endX, double endY);

	/** Replies the given start position and arriving position permits to pass through
	 * the portal.
	 * 
	 * @param startX
	 * @param startY
	 * @param startZ
	 * @param endX
	 * @param endY
	 * @param endZ
	 * @return the target point 
	 */
	public boolean isTraversable(double startX, double startY, double startZ, double endX, double endY, double endZ);

}