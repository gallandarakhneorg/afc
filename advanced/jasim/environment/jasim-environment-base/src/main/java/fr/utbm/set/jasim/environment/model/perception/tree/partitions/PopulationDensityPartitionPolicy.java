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

import javax.vecmath.Point3d;

import fr.utbm.set.collection.IntegerList;
import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.object.EuclidianPoint;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.jasim.environment.model.world.WorldEntity;


/**
 * Partition policy which compute a parition field according
 * to the density of the population. It tries to equilibrate
 * the different parts of the partition in term of population
 * size.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PopulationDensityPartitionPolicy implements PartitionPolicy {

	/** Singleton instance.
	 */
	public static final PopulationDensityPartitionPolicy SINGLETON = new PopulationDensityPartitionPolicy();
	
	/** 
	 */
	public PopulationDensityPartitionPolicy() {
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
		// use the 3D positions because it is the biggest dimension.
		double tx = 0.;
		double ty = 0.;
		double tz = 0.;
		Point3d p;
		for(int idx : indexes) {
			p = allEntities.get(idx).getPosition3D();
			tx += p.x;
			ty += p.y;
			tz += p.z;
		}
		tx /= indexes.size();
		ty /= indexes.size();
		tz /= indexes.size();

		EuclidianPoint pts;
		
		switch(worldBounds.getMathematicalDimension()) {
		case DIMENSION_2D:
			pts = new EuclidianPoint2D(tx,ty);
			break;
		case DIMENSION_2D5:
		case DIMENSION_3D:
			pts = new EuclidianPoint3D(tx,ty,tz);
			break;
		default:
			throw new IllegalArgumentException("the dimension of the bounds is not supported: only 2D and 3D"); //$NON-NLS-1$
		}
		
		return fieldFactory.newPartitionField(nodeIndex, worldBounds, pts);
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
		// use the 3D positions because it is the biggest dimension.
		double tx = 0.;
		double ty = 0.;
		double tz = 0.;
		Point3d p;
		for(WorldEntity<?> entity : allEntities) {
			p = entity.getPosition3D();
			tx += p.x;
			ty += p.y;
			tz += p.z;
		}
		tx /= allEntities.size();
		ty /= allEntities.size();
		tz /= allEntities.size();

		EuclidianPoint pts;
		
		switch(worldBounds.getMathematicalDimension()) {
		case DIMENSION_2D:
			pts = new EuclidianPoint2D(tx,ty);
			break;
		case DIMENSION_2D5:
		case DIMENSION_3D:
			pts = new EuclidianPoint3D(tx,ty,tz);
			break;
		default:
			throw new IllegalArgumentException("the dimension of the bounds is not supported: only 2D and 3D"); //$NON-NLS-1$
		}
		
		return fieldFactory.newPartitionField(nodeIndex, worldBounds, pts);
	}
	
}