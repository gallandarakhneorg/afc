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
package fr.utbm.set.jasim.environment.model.perception.tree.manipulators;

import java.util.Collection;

import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTree;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.world.MobileEntity3D;

/**
 * A tree manipulator is the abstract definition
 * of a modification operation on a tree or on
 * a tree node.
 * 
 * @param <T> is the type of the tree to manipulate
 * @param <MB> is the type of the bounds inside this tree 
 * @param <N> is the type of the nodes inside this tree
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface TreeManipulator3D<MB extends CombinableBounds3D, N extends DynamicPerceptionTreeNode<?,MobileEntity3D<MB>,?,N>, T extends DynamicPerceptionTree<?,?,MobileEntity3D<MB>,?,N,T,?>>
extends TreeManipulator<MobileEntity3D<MB>,N,T> {

	/** Apply the specified transformation on the specified entities.
	 * <ul>
	 * <li><strong>Caution 1:</strong> the transformation is directly applied, ie the pivot point is not taken
	 * into account;</li>
	 * <li><strong>Caution 2:</strong> you must call {@link #commit()} to finalize the action</li>
	 * </ul>
	 * 
	 * @param transform is the transformation to apply
	 * @param entity is the entity on which the transformation must be applied
	 */
	public void transform(Transform3D transform, MobileEntity3D<MB> entity);
	
	/** Apply the specified transformation on the specified entities.
	 * <ul>
	 * <li><strong>Caution 1:</strong> the transformation is directly applied, ie the pivot point is not taken
	 * into account;</li>
	 * <li><strong>Caution 2:</strong> you must call {@link #commit()} to finalize the action</li>
	 * </ul>
	 * 
	 * @param transform is the transformation to apply
	 * @param entities are the entities on which the transformation must be applied
	 */
	public void transform(Transform3D transform, Collection<MobileEntity3D<MB>> entities);

	/** Apply the specified translation on the specified entities.
	 * <p>
	 * Caution: you must call {@link #commit()} to finalize the action
	 * 
	 * @param dx is the translation to apply
	 * @param dy is the translation to apply
	 * @param dz is the translation to apply
	 * @param entity is the entity on which the transformation must be applied
	 */
	public void translate(double dx, double dy, double dz, MobileEntity3D<MB> entity);

	/** Apply the specified translation on the specified entities.
	 * <p>
	 * Caution: you must call {@link #commit()} to finalize the action
	 * 
	 * @param dx is the translation to apply
	 * @param dy is the translation to apply
	 * @param dz is the translation to apply
	 * @param entities are the entities on which the transformation must be applied
	 */
	public void translate(double dx, double dy, double dz, Collection<MobileEntity3D<MB>> entities);

	/** Apply the specified rotations on the specified entities.
	 * <p>
	 * This function assumes that the fourth first parameters are representing
	 * an axis-angle transformation.
	 * <p>
	 * Each given entity will rotate around its own pivot point. 
	 * <p>
	 * Caution: you must call {@link #commit()} to finalize the action
	 * 
	 * @param ax is the axis vector around which the rotation must take place.
	 * @param ay is the axis vector around which the rotation must take place.
	 * @param az is the axis vector around which the rotation must take place.
	 * @param angle is the rotation angle around the given axis.
	 * @param entity is the entity on which the transformation must be applied
	 */
	public void rotate(double ax, double ay, double az, double angle, MobileEntity3D<MB> entity);

	/** Apply the specified rotations on the specified entities.
	 * <p>
	 * Caution: you must call {@link #commit()} to finalize the action
	 * 
	 * @param ax is the axis vector around which the rotation must take place.
	 * @param ay is the axis vector around which the rotation must take place.
	 * @param az is the axis vector around which the rotation must take place.
	 * @param angle is the rotation angle around the given axis.
	 * @param entities are the entities on which the transformation must be applied
	 */
	public void rotate(double ax, double ay, double az, double angle, Collection<MobileEntity3D<MB>> entities);

	/** Apply the specified homothety on the specified entities.
	 * <p>
	 * Caution: you must call {@link #commit()} to finalize the action
	 * 
	 * @param sx is the scale to apply
	 * @param sy is the scale to apply
	 * @param sz is the scale to apply
	 * @param entity is the entity on which the transformation must be applied
	 */
	public void scale(double sx, double sy, double sz, MobileEntity3D<MB> entity);

	/** Apply the specified homothety on the specified entities.
	 * <p>
	 * Caution: you must call {@link #commit()} to finalize the action
	 * 
	 * @param sx is the scale to apply
	 * @param sy is the scale to apply
	 * @param sz is the scale to apply
	 * @param entities are the entities on which the transformation must be applied
	 */
	public void scale(double sx, double sy, double sz, Collection<MobileEntity3D<MB>> entities);

}