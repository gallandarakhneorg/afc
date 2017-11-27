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

import java.util.UUID;

import fr.utbm.set.gis.road.RoadConnectionStub;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.gis.road.RoadSegmentStub;
import fr.utbm.set.unittest.AbstractExtendedTestCase;
import fr.utbm.set.geom.object.Point1D5;

/**
 * Unit test for FourDirectionFrustum1D5.
 * 
 * FIXME: Do the unit tests
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FrontFrustum1D5Test extends AbstractExtendedTestCase {

	private FrontFrustum1D5 reference;
	
	private RoadSegment s, s2, s3, s4;
	private RoadSegment bs2;

	private RoadConnectionStub connec1, connec2, connec3, connec4, connec5, connecb1;
	
	private Point1D5 eye;
	
	private double forward, backward, side;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		this.connecb1 = new RoadConnectionStub("connecb1", 190d,320d); //$NON-NLS-1$
		this.connec1 = new RoadConnectionStub("connec1", 200d,300d); //$NON-NLS-1$
		this.connec2 = new RoadConnectionStub("connec2", 140d,200d); //$NON-NLS-1$
		this.connec3 = new RoadConnectionStub("connec3", 100,300d); //$NON-NLS-1$
		this.connec4 = new RoadConnectionStub("connec4", 50d,350d); //$NON-NLS-1$
		this.connec5 = new RoadConnectionStub("connec5", 50d,-350d); //$NON-NLS-1$

		this.bs2 =  new RoadSegmentStub("bs2", this.connecb1, this.connec1); //$NON-NLS-1$
		this.s = new RoadSegmentStub("s", this.connec1, this.connec2); //$NON-NLS-1$
		this.s2 = new RoadSegmentStub("s2", this.connec2, this.connec3); //$NON-NLS-1$
		this.s3 = new RoadSegmentStub("s3", this.connec3, this.connec4); //$NON-NLS-1$
		this.s4 = new RoadSegmentStub("s4", this.connec3, this.connec5); //$NON-NLS-1$
		
		this.eye = new Point1D5(this.s, this.RANDOM.nextDouble() * this.s.getLength());

		this.forward = this.RANDOM.nextDouble() * 10.;
		this.backward = this.RANDOM.nextDouble() * 5.;
		this.side = this.RANDOM.nextDouble() * 5.;
		
		this.reference = new FrontFrustum1D5(
				UUID.randomUUID(),
				this.eye,
				this.connec1, 
				this.forward,
				this.side);
	}

	@Override
	public void tearDown() throws Exception {
		this.s = this.s2 = this.s3 = this.s4 = this.bs2 = null;
		this.connec1 = this.connec2 = this.connec3 = this.connec4 = this.connec5 = this.connecb1 = null;
		this.eye = null;
		this.reference = null;
		super.tearDown();
	}

}
