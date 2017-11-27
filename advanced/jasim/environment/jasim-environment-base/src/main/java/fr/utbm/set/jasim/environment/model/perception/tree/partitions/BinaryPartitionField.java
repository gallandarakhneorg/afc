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


/** A partition field for a binary tree.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface BinaryPartitionField extends PartitionField {
	
	/** Replies the value of the cut line/plane.
	 * 
	 * @return the value of the cut line/plane.
	 */
	public double getCutCoordinate();
	
	/** Replies the index of the coordinate of the cut line/plane.
	 * 
	 * @return the index of the coordinate of the cut line/plane.
	 */
	public int getCutCoordinateIndex();

}
