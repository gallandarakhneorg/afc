/* 
 * $Id$
 * 
 * Copyright (c) 2009, Multiagent Team - Systems and Transportation Laboratory (SeT)
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

/**
 * Stub for RoadSegment class.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RoadSegmentStub extends RoadPolyline {

	private final String label;
	
	/**
	 * @param label
	 * @param c1
	 * @param c2
	 */
	public RoadSegmentStub(String label, RoadConnectionStub c1, RoadConnectionStub c2) {
		this.label = label;
		addPoint(c1.getPoint());
		addPoint(c2.getPoint());
		setStartPoint(c1);
		setEndPoint(c2);
	}
	
	/**
	 * @param c1
	 * @param c2
	 */
	public RoadSegmentStub(RoadConnectionStub c1, RoadConnectionStub c2) {
		this(null, c1, c2);
	}

	@Override
	public String toString() {
		if (this.label!=null) {
			StringBuilder str = new StringBuilder();
			str.append(this.label);
			str.append(" => "); //$NON-NLS-1$
			str.append(super.toString());
			return str.toString();
		}
		return super.toString();
	}

}
