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
package fr.utbm.set.jasim.environment.model.perception.frustum;

import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.interfaces.body.frustums.Frustum;
import fr.utbm.set.geom.bounds.bounds1d5.Bounds1D5;
import fr.utbm.set.geom.object.Point1D5;

/** This interface representes a perception frustum
 * in a 1D5 space.
 *
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public interface Frustum1D5 extends Frustum<Bounds1D5<RoadSegment>,Point1D5,Point1D5> {
    
	/**
	 * Gets the translation relative to the entry point of the frustum
	 * @return the translation relative to the entry point
	 */
	public Point1D5 getTranslation();
	

    /**
     * Sets the segment on which is placed this frustum
     * @param segment the segment on which is placed this frustum
     * @param roadEntry the connection behind the frustum
     */
    public void setSegment(RoadSegment segment, RoadConnection roadEntry);
    
    /**
     * Gets the entry point of the frustum
     * @return return the connection in the back of this frustum
     */
    public RoadConnection getRoadEntry();
    
    /**
     * Gets the road where the frustum is located.
     * @return the road where the frustum is located.
     */
    public RoadSegment getRoadSegment();

    /**
     * Gets the front connexion of the frustum (connection the frustum is heading to)
     * @return the connexion in front of this frustum
     */
    public RoadConnection getHeadingPoint();
    
	/**
	 * Applies a translation on this frustrum, relative to the entry point
	 * @param v
	 */
	public void setTranslation(Point1D5 v);
	
	/**
	 * 
	 * @param segment the segment on which is placed this frustum
	 * @param roadEntry is the entry point of this frustum (connection behind it)
	 * @param v
	 */
	public void setTranslation(RoadSegment segment, RoadConnection roadEntry, Point1D5 v);
	

	/**
	 * Gets the forward view distance
	 * @return the forward view distance
	 */
	public double getForwardDistance();
	
	/**
	 * Gets the backward view distance
	 * @return the backward view distance
	 */
	public double getBackwardDistance();
	
	/** Replies if the frustum has the same oriented as the
	 * underlying road segment.
	 * 
	 * @return <code>true</code> if the segment and the frustum has
	 * the same orientation, otherwise <code>false</code>
	 */
	public boolean isSegmentOriented();

}