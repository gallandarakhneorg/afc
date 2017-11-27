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
import fr.utbm.set.jasim.environment.model.world.WorldEntity;

/**
 * Defines the type of partition of the population during tree building.
 * <p>
 * A {@code ParitionPolicy} computes one or more partition axis which
 * are used to dispatch the population among the differents child nodes
 * of a partitionned tree node.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PartitionPolicy {

	/** Compute the best partition field according to the current policy.
	 *
	 * @param <F> is the type of field supported by the factory.
	 * @param nodeIndex is the index of the tree node for which the partition field must be computed.
	 * @param worldBounds is the bounding volume of the universe to partition.
	 * @param allEntities is a collection of entities which could include entities outside the universe.
	 * @param indexes is the list of indexes in <var>allEntities</var> of whose entities which are inside the universe.
	 * @param failures is a collection of partition fields which were known to fail to partition the given population.
	 * @param fieldFactory is the factory which will be invoked to create the new field.
	 * @return a partition field or <code>null</code> if none could be computed.
	 */
	public <F extends PartitionField> F computePartitionField(int nodeIndex, Bounds<?,?,?> worldBounds, List<? extends WorldEntity<?>> allEntities, IntegerList indexes, Collection<? extends F> failures, PartitionFieldFactory<F> fieldFactory);
	
	/** Compute the best partition field according to the current policy.
	 *
	 * @param <F> is the type of field supported by the factory.
	 * @param nodeIndex is the index of the tree node for which the partition field must be computed.
	 * @param worldBounds is the bounding volume of the universe to partition.
	 * @param allEntities is a collection of entities which could include entities outside the universe.
	 * @param fieldFactory is the factory which will be invoked to create the new field.
	 * @return a partition field or <code>null</code> if none could be computed.
	 */
	public <F extends PartitionField> F computePartitionField(int nodeIndex, Bounds<?,?,?> worldBounds, Collection<? extends WorldEntity<?>> allEntities, PartitionFieldFactory<F> fieldFactory);

}