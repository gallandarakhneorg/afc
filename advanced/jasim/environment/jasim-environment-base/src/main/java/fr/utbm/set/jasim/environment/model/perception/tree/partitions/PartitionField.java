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

/** A partition field is an object which permits to
 * classify a population. It is used to dispatch
 * a population among a set of tree nodes.
 * Each the term &laquo;field&raquo; must be understood
 * according to the mathematical definition of a field.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PartitionField {

	/** Compute the classification of the box against the field parts
	 * 
	 * @param index is the inside-parent position of the tree node that is currently under splitting.
	 * @param box is the box to classify
	 * @return the index if the child node that corresponds to the position box according to the field
	 */
	public int classifies(int index, Bounds<?,?,?> box);

}
