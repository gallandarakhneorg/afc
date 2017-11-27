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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.quadtree;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionFieldFactory;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.QuadryPartitionField;

/**
 * Defines a factory for quadry partition fields.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NonIcosepQuadryPartitionFieldFactory implements PartitionFieldFactory<QuadryPartitionField> {

	/** Singleton.
	 */
	public static final NonIcosepQuadryPartitionFieldFactory SINGLETON = new NonIcosepQuadryPartitionFieldFactory();
	
	private NonIcosepQuadryPartitionFieldFactory() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public QuadryPartitionField newPartitionField(int nodeIndex, Bounds<?,?,?> bounds, EuclidianPoint referencePoint) {
		return new NonIcosepField(referencePoint.getCoordinate(0), referencePoint.getCoordinate(1));
	}
	
	/**
	 * Defines a factory for quadry partition fields.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class NonIcosepField implements QuadryPartitionField {

		public final double x;
		public final double y;
		
		/**
		 * @param x
		 * @param y
		 */
		public NonIcosepField(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public int classifies(int index, Bounds<?,?,?> box) {
			return QuadTreeUtil.classifiesBox(this.x, this.y, box.getLower(), box.getUpper());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2d getCutPlaneIntersection() {
			return new Point2d(this.x,this.y);
		}

	}
	
}