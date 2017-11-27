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
package fr.utbm.set.jasim.environment.model.world;

import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.graph.GraphIterationElement;
import fr.utbm.set.graph.GraphIterator;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum1D5;

/**
 * Utility class for 1D5 movement operations
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class Move1D5Util {

	private Move1D5Util() {
		//
	}
    
	/**
	 * Gets the relative distance between a frustum and an entity
	 * @param frustum the frustum
	 * @param perceivedEntity the entity
	 * @return the relative distance between the frustum and the entity
	 */
	public static double getEntityDistance(Frustum1D5 frustum, Entity1D5<?> perceivedEntity) {
		
		GraphIterator<RoadSegment,RoadConnection> iterator;
		double relativeDistance = 0d;
		boolean entityFound = false;
		boolean forward = true;
		while ( ! entityFound ) {
				
			double frustumViewDistance;
			if ( forward ) {
				frustumViewDistance = frustum.getForwardDistance();
				iterator = frustum.getRoadSegment().depthIterator(
						frustumViewDistance, 
						frustum.getTranslation().getCurvilineCoordinate(), 
						frustum.getRoadEntry(),
						false, false);
			}
			else {
				frustumViewDistance = frustum.getBackwardDistance();
				iterator = frustum.getRoadSegment().depthIterator(
						frustumViewDistance,
						frustum.getRoadSegment().getLength()-frustum.getTranslation().getCurvilineCoordinate(),
						frustum.getHeadingPoint(),
						false, false);
			}
			
			while (iterator.hasNext()) { 
				GraphIterationElement<RoadSegment,RoadConnection> element = iterator.nextElement();
				
				final RoadSegment targetSegment = element.getSegment();
				
				if ( targetSegment.equals(perceivedEntity.getBounds().getSegment())) {
					
					entityFound = true;
				
					final double distanceToReachSegment = element.getDistanceToReachSegment();			
					boolean elementConnectionEqualsStartPoint = (element.getPoint().equals(targetSegment.getBeginPoint()));
						
					double agentPos = perceivedEntity.getPosition1D5().getCurvilineCoordinate();					
					relativeDistance = distanceToReachSegment; 
					// Agent pos always relative to start point, so if the iterator placed us on the end connection we must add the remaining length
					if ( elementConnectionEqualsStartPoint )
						relativeDistance += agentPos;
					else
						relativeDistance += targetSegment.getLength() - agentPos;
	
				
					boolean isInFront = (relativeDistance>=0);
					
					// Remove the back distance of the other body 
					// to the distance
					if ( forward )
						relativeDistance -= perceivedEntity.getBounds().getSizeX()/2d;
					else
						relativeDistance += perceivedEntity.getBounds().getSizeX()/2d;
	
					
					// Try to detect collision case and force the relative distance
					// to be nul
					if (((isInFront)&&(relativeDistance<0))||
						((!isInFront)&&(relativeDistance>0))) {
						relativeDistance = 0;
					}				
				}				
			}
			forward = false;
		}
		return relativeDistance;
	}
	
	
	
}
