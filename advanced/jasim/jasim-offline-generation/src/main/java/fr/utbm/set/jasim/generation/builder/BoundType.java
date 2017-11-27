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
package fr.utbm.set.jasim.generation.builder;

import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.BoundingCapsule;
import fr.utbm.set.geom.bounds.bounds3d.BoundingSphere;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.bounds.bounds3d.OrientedBoundingBox;

/**
 * Describe a type of bounds.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum BoundType {
	/** Aligned bounding box.
	 */
	ALIGNED(AlignedBoundingBox.class),
	/** Oriented bounding box.
	 */
	ORIENTED(OrientedBoundingBox.class),
	/** Sphere.
	 */
	SPHERE(BoundingSphere.class),
	/**
	 * Capsule
	 */
	CAPSULE(BoundingCapsule.class);

	private final Class<? extends Bounds3D> type;

	private BoundType(Class<? extends Bounds3D> type) {
		this.type = type;
	}

	/**
	 * Replies the Java type associated to this bound type.
	 * 
	 * @return the Java type associated to this bound type.
	 */
	public Class<? extends Bounds3D> type() {
		return this.type;
	}

	@Override
	public String toString() {
		return name();
	}

}
