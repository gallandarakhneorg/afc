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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.octree;

import javax.vecmath.Point3d;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.OctyPartitionField;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionFieldFactory;

/**
 * Defines a factory for octy partition fields.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NonIcosepOctyPartitionFieldFactory implements PartitionFieldFactory<OctyPartitionField> {

	/** Singleton.
	 */
	public static final NonIcosepOctyPartitionFieldFactory SINGLETON = new NonIcosepOctyPartitionFieldFactory();
	
	private NonIcosepOctyPartitionFieldFactory() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public OctyPartitionField newPartitionField(int nodeIndex, Bounds<?,?,?> bounds, EuclidianPoint referencePoint) {
		return new NonIcosepField(
				referencePoint.getCoordinate(0), 
				referencePoint.getCoordinate(1),
				referencePoint.getCoordinate(2));
	}
	
	/**
	 * Defines a factory for quadry partition fields.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class NonIcosepField implements OctyPartitionField {

		public final double x;
		public final double y;
		public final double z;
		
		/**
		 * @param x
		 * @param y
		 * @param z
		 */
		public NonIcosepField(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public int classifies(int index, Bounds<?,?,?> box) {
			return OctTreeUtil.classifiesBox(
					this.x, this.y, this.z,
					box.getLower(), box.getUpper());
		}

		/** {@inheritDoc}
		 */
		@Override
		public Point3d getCutPlaneIntersection() {
			return new Point3d(this.x,this.y,this.z);
		}

	}
	
}