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
package fr.utbm.set.jasim.environment.model.perception.tree.structures.bsptree;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.BinaryPartitionField;
import fr.utbm.set.jasim.environment.model.perception.tree.partitions.PartitionFieldFactory;

/**
 * Defines a factory for binary partition fields.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class IcosepBinaryPartitionFieldFactory implements PartitionFieldFactory<BinaryPartitionField> {

	/** Singleton.
	 */
	public static final IcosepBinaryPartitionFieldFactory SINGLETON = new IcosepBinaryPartitionFieldFactory();
	
	private IcosepBinaryPartitionFieldFactory() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public BinaryPartitionField newPartitionField(int nodeIndex, Bounds<?,?,?> bounds, EuclidianPoint referencePoint) {
		EuclidianPoint lower = bounds.getLower();
		EuclidianPoint upper = bounds.getUpper();
		
		int size = Math.min(
				lower.getMathematicalDimension().mathematicalDimension,
				upper.getMathematicalDimension().mathematicalDimension);
		int coordIdx = 0;
		double max = Integer.MIN_VALUE;
		double ref = Double.NaN;
		double s;
		
		for(int i=0; i<size; ++i) {
			s = upper.getCoordinate(i) - lower.getCoordinate(i);
			if (s>max) {
				max = s;
				ref = referencePoint.getCoordinate(i);
				coordIdx = i;
			}
		}
		
		return new StandardField(ref, coordIdx);
	}
	
	/**
	 * Defines a factory for binary partition fields.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class StandardField implements BinaryPartitionField {

		public final double x;
		public final int coordinateIndex;
		
		/**
		 * @param coord
		 * @param coordinateIndex
		 */
		public StandardField(double coord, int coordinateIndex) {
			this.x = coord;
			this.coordinateIndex = coordinateIndex;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public int classifies(int index, Bounds<?,?,?> box) {
			return BspTreeUtil.classifiesOnCoordinatesWithIcosep(
					this.coordinateIndex, this.x,
					box.getLower(), box.getUpper());
		}

		/** {@inheritDoc}
		 */
		@Override
		public double getCutCoordinate() {
			return this.x;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public int getCutCoordinateIndex() {
			return this.coordinateIndex;
		}

	}
	
}