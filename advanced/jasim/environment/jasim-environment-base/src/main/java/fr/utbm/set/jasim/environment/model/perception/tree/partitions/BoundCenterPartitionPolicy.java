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
package fr.utbm.set.jasim.environment.model.perception.tree.partitions;

import java.util.Collection;
import java.util.List;

import fr.utbm.set.collection.IntegerList;
import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * Partition heuristic which select the center of a bounding volume
 * as a reference for a {@link PartitionField partition field}.
 * <p>
 * According to the dimension of the bounding volume, the resultive
 * partition could be composed of two (1D), four (2D) or height (3D) subparts. 
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoundCenterPartitionPolicy implements PartitionPolicy {

	/** Singleton instance.
	 */
	public static final BoundCenterPartitionPolicy SINGLETON = new BoundCenterPartitionPolicy();
	
	/** 
	 */
	public BoundCenterPartitionPolicy() {
		//
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <F extends PartitionField> F computePartitionField(
			int nodeIndex,
			Bounds<?,?,?> worldBounds,
			List<? extends WorldEntity<?>> allEntities, IntegerList indexes,
			Collection<? extends F> failures,
			PartitionFieldFactory<F> fieldFactory) {
		if (failures!=null && !failures.isEmpty()) return null;
		EuclidianPoint center = worldBounds.getCenter();
		assert(center!=null);
		return fieldFactory.newPartitionField(nodeIndex, worldBounds, center);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <F extends PartitionField> F computePartitionField(
			int nodeIndex,
			Bounds<?, ?, ?> worldBounds,
			Collection<? extends WorldEntity<?>> allEntities,
			PartitionFieldFactory<F> fieldFactory) {
		EuclidianPoint center = worldBounds.getCenter();
		assert(center!=null);
		return fieldFactory.newPartitionField(nodeIndex, worldBounds, center);
	}
	
}