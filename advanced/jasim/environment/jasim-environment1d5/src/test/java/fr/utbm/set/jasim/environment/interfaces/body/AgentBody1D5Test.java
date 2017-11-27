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
package fr.utbm.set.jasim.environment.interfaces.body;

 import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import fr.utbm.set.geom.bounds.bounds1d5.BoundingRect1D5;
import fr.utbm.set.gis.road.RoadConnectionStub;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.gis.road.RoadSegmentStub;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence1D5;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception1D5;
import fr.utbm.set.jasim.environment.model.perception.frustum.FrontFrustum1D5;
import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum1D5;
import fr.utbm.set.unittest.AbstractExtendedTestCase;

/**
 * Unit test for AgentBody1D5 class.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AgentBody1D5Test extends AbstractExtendedTestCase {

	private RoadConnectionStub con1, con2;
	private RoadSegmentStub segment;
	private BoundingRect1D5<RoadSegment> box;
	private AgentBody1D5<Influence1D5<RoadSegment>, Perception1D5, BoundingRect1D5<RoadSegment>> body;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		this.con1 = new RoadConnectionStub(randomPoint2D());
		this.con2 = new RoadConnectionStub(randomPoint2D());
		
		this.segment = new RoadSegmentStub(this.con1, this.con2);
		
		this.box = new BoundingRect1D5<RoadSegment>(this.segment);
		double x1 = this.RANDOM.nextDouble() * this.segment.getLength();
		double x2 = x1 + this.RANDOM.nextDouble() * (this.segment.getLength()-x1);
		this.box.setBox(x1, x2, 
				(this.RANDOM.nextDouble()-this.RANDOM.nextDouble()) * 50.,
				this.RANDOM.nextDouble() * 40.);

		this.body = new AgentBody1D5Stub<Influence1D5<RoadSegment>, Perception1D5, BoundingRect1D5<RoadSegment>>(
							UUID.randomUUID(),
							Perception1D5.class,
							this.box,
							this.con2);
	}
	
	@Override
	public void tearDown() throws Exception {
		this.body = null;
		this.box = null;
		this.segment = null;
		this.con1 = null;
		this.con2 = null;
		super.tearDown();
	}
	
	/**
	 */
	public void testGetSetFrustums() {
		Collection<? extends Frustum1D5> frustums = this.body.getFrustums();
		assertNotNull(frustums);
		assertTrue(frustums.isEmpty());
		
		Frustum1D5 f1 = new FrontFrustum1D5(
							UUID.randomUUID(),
							randomPoint1D5(this.segment),
							this.con2,
							100.,
							20.);
		Frustum1D5 f2 = new FrontFrustum1D5(
							UUID.randomUUID(),
							randomPoint1D5(this.segment),
							this.con1,
							100.,
							20.);
		
		this.body.setFrustums(Arrays.asList(f1,f2));
		
		frustums = this.body.getFrustums();
		assertNotNull(frustums);
		assertFalse(frustums.isEmpty());
		assertEquals(2, frustums.size());
		assertEpsilonEquals(Arrays.asList(f1,f2),frustums);
	}
	
}
