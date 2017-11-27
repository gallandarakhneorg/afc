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
package fr.utbm.set.gis.road;

import java.util.Iterator;

import javax.vecmath.Point2d;

import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.gis.road.StandardRoadNetwork;
import fr.utbm.set.gis.road.primitive.RoadNetworkException;
import fr.utbm.set.gis.road.primitive.RoadSegment;

/**
 *  
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RoadNetworkStub extends StandardRoadNetwork {

	/**
	 * Creates a road network with various road connections and segments
	 * @param bounds
	 * @throws RoadNetworkException
	 */
	public RoadNetworkStub(Bounds2D bounds) throws RoadNetworkException {
		super(bounds);
		
		RoadPolyline[] rs;
		Point2d[] connec;

		connec = new Point2d[8];

		rs = new RoadPolyline[8];

		connec[0] = new Point2d(20d,0d);
		connec[1] = new Point2d(80d,10d);
		connec[2] = new Point2d(100d,20d);
		connec[3] = new Point2d(120,20d);
		connec[4] = new Point2d(130d,40d);
		connec[5] = new Point2d(220d,40d);
		connec[6]= new Point2d(110d,60d);
		connec[7] = new Point2d(125d,50d);

		for ( int i = 0 ; i < rs.length ; ++i ) {
			rs[i] = new RoadPolyline();
			rs[i].setName("RoadSegment#"+i); //$NON-NLS-1$
		}

		for ( int i = 0 ; i <= 4 ; ++i) {
			rs[i].addPoint(connec[i]);
			rs[i].addPoint(connec[i+1]);
		}

		rs[5].addPoint(connec[2]);
		rs[5].addPoint(connec[6]);

		rs[6].addPoint(connec[6]);
		rs[6].addPoint(connec[7]);

		rs[7].addPoint(connec[7]);
		rs[7].addPoint(connec[4]);


		for ( RoadPolyline s : rs )
			this.addRoadSegment(s);

	}

	/**
	 * 
	 * @return a random segment of the network
	 */
	public RoadSegment getRandomSegment() {
		int count = getSegmentCount();
		int n = (int)( Math.random() * count );
		Iterator<RoadSegment> iterator = iterator();
		RoadSegment s = null;
		for(int i=0; i<n && iterator.hasNext(); ++i) {
			s = iterator.next();
		}
		return s;
	}



}
