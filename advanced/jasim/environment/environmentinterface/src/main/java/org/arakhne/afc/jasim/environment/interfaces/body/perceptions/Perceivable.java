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
package org.arakhne.afc.jasim.environment.interfaces.body.perceptions;

import java.util.Collection;
import java.util.UUID;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.bounds.bounds1d.Bounds1D;
import fr.utbm.set.geom.bounds.bounds1d5.Bounds1D5;
import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influencable;
import org.arakhne.afc.jasim.environment.semantics.Semantic;

/** This interface describes a perceivable object.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Perceivable {
	
	/**
	 * Replies the identifier of this object.
	 * 
	 * @return the identifier of this object.
	 */
	public UUID getIdentifier();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 3D environment is a space point.
	 * 
	 * @return the position in the space.
	 */
	public Point3d getPosition3D();
	
	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 2.5D environment is a space point.
	 * 
	 * @return the position in space
	 */
	public Point3d getPosition2D5();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 2D environment is a plan point.
	 * 
	 * @return the position on a plane
	 */
	public Point2d getPosition2D();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 1.5D environment is a curviline position (x) and
	 * a shifting distance (y).
	 * 
	 * @return the curviline position (x) and a shifting distance (y)
	 */
	public Point1D5 getPosition1D5();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 1D environment is a curviline position.
	 * 
	 * @return the curviline position
	 */
	public Point1D getPosition1D();

	/** Replies the associated semantics.
	 * 
	 * @return the list of semantics.
	 */
	public Collection<? extends Semantic> getAllSemantics();
	
	/** Replies if this perceivable object is of the given type.
	 * 
	 * @param type is thetype to test.
	 * @return <code>true</code> if this object is an object of the
	 * given type, otherwise <code>false</code>
	 */
	public boolean isA(Semantic type);
	
	/** Replies if this perceivable object is also influencable.
	 * 
	 * @return <code>true</code> if this object is influencable, otherwise <code>false</code>
	 */
	public boolean isInfluencable();

	/** Replies the influencable interface that corresponds to this object.
	 * <p>
	 * If this object is not influencable, an exception will occur.
	 * 
	 * @return the influencable part of this perceived object.
	 * @throws UnsupportedOperationException if the object is not influencable.
	 */
	public Influencable toInfluencable();
	
	/** Replies if this perceivable object is also an kinematic object.
	 * <p>
	 * kinematic object has position and orientation.
	 * 
	 * @return <code>true</code> if this object is kinematic, otherwise <code>false</code>
	 */
	public boolean isKinematic();

	/** Replies the kinematic interface that corresponds to this object.
	 * <p>
	 * If this object is not kinematic, an exception will occur.
	 * 
	 * @return the kinematic part of this perceived object.
	 * @throws UnsupportedOperationException if the object is not kinematic.
	 */
	public KinematicPerceivable toKinematic();

	/** Replies if this perceivable object is also an steering object.
	 * <p>
	 * Steering object extends kinematic objects with linear and angular accelerations.
	 * 
	 * @return <code>true</code> if this object is steering, otherwise <code>false</code>
	 */
	public boolean isSteering();

	/** Replies the steering interface that corresponds to this object.
	 * <p>
	 * If this object is not steering, an exception will occur.
	 * 
	 * @return the steering part of this perceived object.
	 * @throws UnsupportedOperationException if the object is not steering.
	 */
	public SteeringPerceivable toSteering();

	/** Replies the bounds of the perceivable.
	 * 
	 * @return the bounds of the perceivable
	 */
	public Bounds<?,?,?> getBounds();
	
	/** Replies the bounds of the perceivable.
	 * 
	 * @return the 1D bounds of the perceivable
	 */
	public Bounds1D<?> getBounds1D();

	/** Replies the bounds of the perceivable.
	 * 
	 * @return the 1.5D bounds of the perceivable
	 */
	public Bounds1D5<?> getBounds1D5();

	/** Replies the bounds of the perceivable.
	 * 
	 * @return the 2D bounds of the perceivable
	 */
	public Bounds2D getBounds2D();
	
	/** Replies the bounds of the perceivable.
	 * 
	 * @return the 2.5D bounds of the perceivable
	 */
	public Bounds3D getBounds2D5();

	/** Replies the bounds of the perceivable.
	 * 
	 * @return the 3D bounds of the perceivable
	 */
	public Bounds3D getBounds3D();
	
	/** Replies a data associated to this entity.
	 * 
	 * @param name is the name of the data to reply.
	 * @return the data or <code>null</code> if not found.
	 * @since 2.0
	 */
	public Object getUserData(String name);

	/** Replies a data associated to this entity.
	 * 
	 * @param <T> is the type of the data to reply.
	 * @param name is the name of the data to reply.
	 * @param type is the type of the data to reply.
	 * @return the data or <code>null</code> if not found or not of the specified type.
	 * @since 2.0
	 */
	public <T> T getUserData(String name, Class<T> type);
	
}