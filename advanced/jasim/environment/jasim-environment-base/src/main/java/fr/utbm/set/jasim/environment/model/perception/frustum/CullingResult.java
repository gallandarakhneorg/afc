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

package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.util.UUID;

import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * This class is the result of the frustum culler iterator.
 * 
 * @param <D> is the type of the culled object.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CullingResult<D extends WorldEntity<?>> {

	private final UUID frustum;
	private final D object;
	private final IntersectionType classification;
	private OcclusionCullingType occlusionType;

	/**
	 * @param frustum
	 * @param type
	 * @param object
	 */
	public CullingResult(UUID frustum, IntersectionType type, D object) {
		this.frustum = frustum;
		this.classification = type;
		this.object = object;
		this.occlusionType = OcclusionCullingType.NO_OCCLUSION;
	}
	
	/** Replies the frustum which is the source of this perception.
	 * 
	 * @return the perception source
	 */
	public UUID getFrustum() {
		return this.frustum;
	}

	/** Replies the culled object.
	 * 
	 * @return the culled object.
	 */
	public D getCulledObject() {
		return this.object;
	}
	
	/** Replies the type of intersection with the frustum.
	 * 
	 * @return the type of intersection with the frustum.
	 */
	public IntersectionType getIntersectionType() {
		return this.classification;
	}

	/** Replies the type of occlusion.
	 * 
	 * @return the type of occlusion.
	 */
	public OcclusionCullingType getOcclusionType() {
		return this.occlusionType;
	}

	/** Set the type of occlusion.
	 * 
	 * @param type is the new type of occlusion.
	 */
	public void setOcclusionType(OcclusionCullingType type) {
		this.occlusionType = type==null ? OcclusionCullingType.NO_OCCLUSION : type;
	}

	/** Replies if the object is visible.
	 * An object is visible if it is not outside the frustum and
	 * if it is not totally occluded.
	 * 
	 * @return <code>true</code> if the object is visible, otherwise <code>false</code>
	 */
	public boolean isVisible() {
		return this.classification!=IntersectionType.OUTSIDE &&
			   this.occlusionType!=OcclusionCullingType.TOTAL_OCCLUSION;
	}

}
