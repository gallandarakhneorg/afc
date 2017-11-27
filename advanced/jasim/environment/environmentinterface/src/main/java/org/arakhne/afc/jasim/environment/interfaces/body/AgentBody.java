/*
 * 
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
package org.arakhne.afc.jasim.environment.interfaces.body;

import javax.vecmath.Quat4d;

import fr.utbm.set.geom.PseudoHamelDimension;
import fr.utbm.set.geom.bounds.bounds1d.Bounds1D;
import fr.utbm.set.geom.bounds.bounds1d5.Bounds1D5;
import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.geom.object.Direction2D;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.object.EuclidianPoint2D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.object.Point1D;
import fr.utbm.set.geom.object.Point1D5;

import org.arakhne.afc.jasim.environment.interfaces.body.influences.Influence;
import org.arakhne.afc.jasim.environment.interfaces.body.influences.InfluenceApplicationStatus;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.InterestFilter;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.Perceiver;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.Perception;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.Perceptions;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.PhysicalPerceptionAlterator;
import org.arakhne.afc.jasim.environment.time.Clock;


/**
 * This interface describes the body of an situated agent.
 * The body is the only available interaction mean between
 * an agent and the environment.
 * 
 * @param <INF> The type of influence this body may receive and forward to the collector
 * @param <PT> The type of perception this body may receive
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AgentBody<INF extends Influence, PT extends Perception> 
extends Perceiver,
        InfluencerEntity<INF> {
		
	/** Replies the perceptions associated to this body.
	 * 
	 * @return A collection of percepts
	 */
	public Perceptions<PT> getPerceptions();

	/** Set the physical perception filter that must be used to perceive.
	 * 
	 * @param filter is a perception filter
	 */
	public void setPerceptionFilter(PhysicalPerceptionAlterator filter);
	
	/** Replies the physical perception filter that must be used to perceive.
	 * 
	 * @return a perception filter or <code>null</code> if none.
	 */
	public PhysicalPerceptionAlterator getPerceptionFilter();

	/** Change the interest filter for perceptions.
	 * 
	 * @param filter
	 */
	public void setInterestFilter(InterestFilter filter);

	/** Kill this body from the environment.
	 */
	public void kill();
	
	/** Replies the prefered space dimension that permits to an agent
	 * to interact with this body.
	 *
	 * @return the prefered space dimension.
	 */
	public PseudoHamelDimension getPreferredMathematicalDimension();

	/**
	 * Returns the current simulation time.
	 * 
	 * @return the current simulation time.
	 */
	public Clock getSimulationClock();

	/** Replies the bounds of this body.
	 * 
	 * @return the bounds of this body.
	 */
	public Bounds1D<?> getBounds1D();

	/** Replies the bounds of this body.
	 * 
	 * @return the bounds of this body.
	 */
	public Bounds1D5<?> getBounds1D5();

	/** Replies the bounds of this body.
	 * 
	 * @return the bounds of this body.
	 */
	public Bounds2D getBounds2D();

	/** Replies the bounds of this body.
	 * 
	 * @return the bounds of this body.
	 */
	public Bounds3D getBounds2D5();

	/** Replies the bounds of this body.
	 * 
	 * @return the bounds of this body.
	 */
	public Bounds3D getBounds3D();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 3D environment is a space point.
	 * 
	 * @return the position in the space.
	 */
	public EuclidianPoint3D getPosition3D();
	
	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 2.5D environment is a space point.
	 * 
	 * @return the position in space
	 */
	public EuclidianPoint3D getPosition2D5();

	/**
	 * Returns the position of this body in its environment
	 * <p>
	 * The position is a 2D environment is a plan point.
	 * 
	 * @return the position on a plane
	 */
	public EuclidianPoint2D getPosition2D();

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

	/**
	 * Returns the orientation of this body in its environment.
	 * <p>
	 * The orientation is composed of a view direction (as a
	 * 3D vector) and a rotation angle around this view axis.
	 * 
	 * @return the orientation of the situated agent in its environment
	 * @see #getOrientation3D()
	 */
	public Direction3D getViewDirection3D();

	/**
	 * Returns the orientation of this body in its environment.
	 * <p>
	 * The orientation is a 3D quaternion which corresponds
	 * to the rotation to apply to identity transformation
	 * to obtain the current orientation of the object.
	 * 
	 * @return the orientation of the situated agent in its environment
	 * @see #getViewDirection3D()
	 */
	public Quat4d getOrientation3D();

	/**
	 * Returns the orientation of this body in its environment
	 * <p>
	 * The orientation is a 2D vector representing
	 * the view direction.
	 * 
	 * @return the orientation of the situated agent in its environment
	 * @see #getOrientation2D5()
	 */
	public Direction2D getViewDirection2D5();

	/**
	 * Returns the orientation of this body in its environment.
	 * <p>
	 * The orientation is a rotation angle which corresponds
	 * to the rotation to apply to identity transformation
	 * to obtain the current orientation of the object.
	 * 
	 * @return the orientation of the situated agent in its environment (in radians)
	 * @see #getViewDirection2D5()
	 */
	public double getOrientation2D5();

	/**
	 * Returns the orientation of this body in its environment
	 * <p>
	 * The orientation is a 2D vector representing
	 * the view direction.
	 * 
	 * @return the orientation of the situated agent in its environment
	 * @see #getOrientation2D()
	 */
	public Direction2D getViewDirection2D();

	/**
	 * Returns the orientation of this body in its environment.
	 * <p>
	 * The orientation is a rotation angle which corresponds
	 * to the rotation to apply to identity transformation
	 * to obtain the current orientation of the object.
	 * 
	 * @return the orientation of the situated agent in its environment (in radians)
	 * @see #getViewDirection2D()
	 */
	public double getOrientation2D();

	/**
	 * Returns the orientation of this body in its environment.
	 * <p>
	 * The replied orientation is the direction at which the entity
	 * is looking: at the same direction as the underlying road,
	 * or not.
	 * 
	 * @return the orientation of the situated agent in its environment
	 * @see #getOrientation1D5()
	 */
	public Direction1D getViewDirection1D5();

	/**
	 * Returns the orientation of this body in its environment.
	 * <p>
	 * The orientation may be to apply to identity transformation
	 * to obtain the current orientation of the object.
	 * The replied value is <code>1</code> if the orientation
	 * corresponds to the graph segment orientation, and
	 * <code>-1</code> if the orientation corresponds to the
	 * reverse direction as the graph segment orientation.
	 * 
	 * @return the orientation of the situated agent in its environment
	 * @see #getViewDirection1D5()
	 */
	public double getOrientation1D5();

	/**
	 * Returns the orientation of this body in its environment
	 * <p>
	 * The replied orientation is the direction at which the entity
	 * is looking: at the same direction as the underlying road,
	 * or not.
	 * 
	 * @return the orientation of the situated agent in its environment
	 * @see #getOrientation1D()
	 */
	public Direction1D getViewDirection1D();

	/**
	 * Returns the orientation of this body in its environment.
	 * <p>
	 * The orientation may be to apply to identity transformation
	 * to obtain the current orientation of the object.
	 * The replied value is <code>1</code> if the orientation
	 * corresponds to the graph segment orientation, and
	 * <code>-1</code> if the orientation corresponds to the
	 * reverse direction as the graph segment orientation.
	 * 
	 * @return the orientation of the situated agent in its environment
	 * @see #getViewDirection1D()
	 */
	public double getOrientation1D();

	/** Replies the status of the last influence which was tried to be applied in the environment.
	 * 
	 * @return the status
	 */
	public InfluenceApplicationStatus getLastInfluenceStatus();
	
	/** Replies the error of the last influence which was tried to be applied in the environment.
	 * 
	 * @return the error or <code>null</code>.
	 */
	public Throwable getLastInfluenceError();

}
