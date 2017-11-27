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
import fr.utbm.set.tree.node.IcosepQuadTreeNode.IcosepQuadTreeZone;

/**
 * Defines a factory for quadry partition fields.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class IcosepQuadryPartitionFieldFactory implements PartitionFieldFactory<QuadryPartitionField> {

	/** Singleton.
	 */
	public static final IcosepQuadryPartitionFieldFactory SINGLETON = new IcosepQuadryPartitionFieldFactory();
	
	private IcosepQuadryPartitionFieldFactory() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public QuadryPartitionField newPartitionField(int nodeIndex, Bounds<?,?,?> bounds, EuclidianPoint referencePoint) {
		if (nodeIndex==IcosepQuadTreeZone.ICOSEP.ordinal())
			return new IcosepField(referencePoint.getCoordinate(0), referencePoint.getCoordinate(1));
		return new StandardField(referencePoint.getCoordinate(0), referencePoint.getCoordinate(1));
	}
	
	/**
	 * Defines a factory for quadry partition fields.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class StandardField implements QuadryPartitionField {

		public final double x;
		public final double y;
		
		/**
		 * @param x
		 * @param y
		 */
		public StandardField(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public int classifies(int index, Bounds<?,?,?> box) {
			return QuadTreeUtil.classifiesBoxWithIcosep(this.x, this.y, box.getLower(), box.getUpper());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2d getCutPlaneIntersection() {
			return new Point2d(this.x,this.y);
		}

	}

	/**
	 * Defines a factory for quadry partition fields.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class IcosepField implements QuadryPartitionField {

		public final double x;
		public final double y;
		
		/**
		 * @param x
		 * @param y
		 */
		public IcosepField(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public int classifies(int index, Bounds<?,?,?> box) {
			EuclidianPoint lower = box.getLower();
			EuclidianPoint upper = box.getUpper();
			
			if (upper.getCoordinate(0)<=this.x) {
				return IcosepQuadTreeZone.NORTH_WEST.ordinal(); 
			}
			if (lower.getCoordinate(0)>=this.x) {
				return IcosepQuadTreeZone.SOUTH_EAST.ordinal(); 
			}
			if (upper.getCoordinate(1)<=this.y) {
				return IcosepQuadTreeZone.SOUTH_WEST.ordinal(); 
			}
			if (lower.getCoordinate(1)>=this.y) {
				return IcosepQuadTreeZone.NORTH_EAST.ordinal(); 
			}
			return IcosepQuadTreeZone.ICOSEP.ordinal();
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