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

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.object.EuclidianPoint;

/**
 * Defines a factory for partition fields.
 *
 * @param <F> is the type of field supported by the factory.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PartitionFieldFactory<F extends PartitionField> {

	/**
	 * Create a new instance of a partition field located at
	 * the given reference point.
	 * 
	 * @param nodeIndex is the index of the tree node for which the partition field must be computed.
	 * @param bounds is the bounds of the universe used to compute the reference point.
	 * @param referencePoint is the reference point where to put the new partition field.
	 * @return a new partition field, never <code>null</code>.
	 */
	public F newPartitionField(int nodeIndex, Bounds<?,?,?> bounds, EuclidianPoint referencePoint);
	
}