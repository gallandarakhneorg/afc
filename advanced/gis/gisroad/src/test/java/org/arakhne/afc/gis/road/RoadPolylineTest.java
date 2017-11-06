/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.gis.road;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;

/** Unit test for RoadSegment.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class RoadPolylineTest extends AbstractGisTest {

	private static final int TEST_COUNT = 50;
	private static final double SHIFTING_RANGE = 50.;

	private RoadSegmentStub testedSegment;
	private RoadSegmentStub firstNeighbour;
	private RoadSegmentStub secondNeighbour;
	private RoadSegmentStub loopSegment;
	private RoadSegmentStub thirdNeighbour;
	private RoadConnectionStub connection1;
	private RoadConnectionStub connection2;
	private RoadConnectionStub culDeSac1;
	private RoadConnectionStub culDeSac2;
	private RoadConnectionStub culDeSac3;
	private RoadConnectionStub connection3;

	@Before
	public void setUp() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();

		getLogger().info("Initializing..."); //$NON-NLS-1$
		
		getLogger().fine("testedSegment"); //$NON-NLS-1$
		this.testedSegment = new RoadSegmentStub("testedSegment", true); //$NON-NLS-1$
		this.connection1 = new RoadConnectionStub("connection1"); //$NON-NLS-1$
		this.testedSegment.setStartPoint(this.connection1);
		this.connection2 = new RoadConnectionStub("connection2"); //$NON-NLS-1$
		this.testedSegment.setEndPoint(this.connection2);

		getLogger().fine("firstNeightbour"); //$NON-NLS-1$
		this.firstNeighbour = new RoadSegmentStub("firstNeightbour", true); //$NON-NLS-1$
		this.firstNeighbour.setEndPoint(this.connection1);
		this.culDeSac1 = new RoadConnectionStub("culDeSac1"); //$NON-NLS-1$
		this.firstNeighbour.setStartPoint(this.culDeSac1);

		getLogger().fine("secondNeightbour"); //$NON-NLS-1$
		this.secondNeighbour = new RoadSegmentStub("secondNeightbour", true); //$NON-NLS-1$
		this.secondNeighbour.setEndPoint(this.connection2);
		this.culDeSac2 = new RoadConnectionStub("culDeSac2"); //$NON-NLS-1$
		this.secondNeighbour.setStartPoint(this.culDeSac2);

		getLogger().fine("loopSegment"); //$NON-NLS-1$
		this.loopSegment = new RoadSegmentStub("loopSegment", true); //$NON-NLS-1$
		this.connection3 = new RoadConnectionStub("connection3"); //$NON-NLS-1$
		this.loopSegment.setStartPoint(this.connection3);
		this.loopSegment.setEndPoint(this.connection3);

		getLogger().fine("thirdNeightbour"); //$NON-NLS-1$
		this.thirdNeighbour = new RoadSegmentStub("thirdNeightbour", true); //$NON-NLS-1$
		this.thirdNeighbour.setEndPoint(this.connection3);
		this.culDeSac3 = new RoadConnectionStub("culDeSac3"); //$NON-NLS-1$
		this.thirdNeighbour.setStartPoint(this.culDeSac3);

		getLogger().info("done"); //$NON-NLS-1$
	}

	@After
	public void tearDown() {
		this.firstNeighbour = null;
		this.secondNeighbour = null;
		this.thirdNeighbour = null;
		this.culDeSac1 = null;
		this.culDeSac2 = null;
		this.culDeSac3 = null;
		this.connection3 = null;
		this.connection2 = null;
		this.connection1 = null;
		this.loopSegment = null;
		this.testedSegment = null;
	}

    @Test
	public void testGetDistanceFromStart() {
    	assertEpsilonEquals(0.,this.testedSegment.getDistanceFromStart(0));
    	assertEpsilonEquals(0.,this.firstNeighbour.getDistanceFromStart(0));
    	assertEpsilonEquals(0.,this.secondNeighbour.getDistanceFromStart(0));

    	assertEpsilonEquals(0.,this.loopSegment.getDistanceFromStart(0));
    	assertEpsilonEquals(0.,this.thirdNeighbour.getDistanceFromStart(0));

    	//

    	assertEpsilonEquals(this.testedSegment.getOriginalLength(),this.testedSegment.getDistanceFromStart(1));
    	assertEpsilonEquals(this.firstNeighbour.getOriginalLength(),this.firstNeighbour.getDistanceFromStart(1));
    	assertEpsilonEquals(this.secondNeighbour.getOriginalLength(),this.secondNeighbour.getDistanceFromStart(1));

    	assertEpsilonEquals(this.loopSegment.getOriginalLength(),this.loopSegment.getDistanceFromStart(1));
    	assertEpsilonEquals(this.thirdNeighbour.getOriginalLength(),this.thirdNeighbour.getDistanceFromStart(1));

    	//

    	Random rnd = new Random();
    	double ratio;
    	for(int i=0; i<TEST_COUNT; ++i) {
    		ratio = rnd.nextDouble();

    		assertEpsilonEquals(this.testedSegment.getOriginalStartDistance(ratio),this.testedSegment.getDistanceFromStart(ratio));
    		assertEpsilonEquals(this.firstNeighbour.getOriginalStartDistance(ratio),this.firstNeighbour.getDistanceFromStart(ratio));
    		assertEpsilonEquals(this.secondNeighbour.getOriginalStartDistance(ratio),this.secondNeighbour.getDistanceFromStart(ratio));

    		assertEpsilonEquals(this.loopSegment.getOriginalStartDistance(ratio),this.loopSegment.getDistanceFromStart(ratio));
    		assertEpsilonEquals(this.thirdNeighbour.getOriginalStartDistance(ratio),this.thirdNeighbour.getDistanceFromStart(ratio));
    	}
	}

    @Test
	public void testGetDistanceToEnd() {
    	assertEpsilonEquals(this.testedSegment.getOriginalLength(),this.testedSegment.getDistanceToEnd(0));
    	assertEpsilonEquals(this.firstNeighbour.getOriginalLength(),this.firstNeighbour.getDistanceToEnd(0));
    	assertEpsilonEquals(this.secondNeighbour.getOriginalLength(),this.secondNeighbour.getDistanceToEnd(0));

    	assertEpsilonEquals(this.loopSegment.getOriginalLength(),this.loopSegment.getDistanceToEnd(0));
    	assertEpsilonEquals(this.thirdNeighbour.getOriginalLength(),this.thirdNeighbour.getDistanceToEnd(0));

    	//

    	assertEpsilonEquals(0.,this.testedSegment.getDistanceToEnd(1));
    	assertEpsilonEquals(0.,this.firstNeighbour.getDistanceToEnd(1));
    	assertEpsilonEquals(0.,this.secondNeighbour.getDistanceToEnd(1));

    	assertEpsilonEquals(0.,this.loopSegment.getDistanceToEnd(1));
    	assertEpsilonEquals(0.,this.thirdNeighbour.getDistanceToEnd(1));

    	//

    	Random rnd = new Random();
    	double ratio;
    	for(int i=0; i<TEST_COUNT; ++i) {
    		ratio = rnd.nextDouble();

    		assertEpsilonEquals(this.testedSegment.getOriginalEndDistance(ratio),this.testedSegment.getDistanceToEnd(ratio));
    		assertEpsilonEquals(this.firstNeighbour.getOriginalEndDistance(ratio),this.firstNeighbour.getDistanceToEnd(ratio));
    		assertEpsilonEquals(this.secondNeighbour.getOriginalEndDistance(ratio),this.secondNeighbour.getDistanceToEnd(ratio));

    		assertEpsilonEquals(this.loopSegment.getOriginalEndDistance(ratio),this.loopSegment.getDistanceToEnd(ratio));
    		assertEpsilonEquals(this.thirdNeighbour.getOriginalEndDistance(ratio),this.thirdNeighbour.getDistanceToEnd(ratio));
    	}
	}

    @Test
	public void testGetAntepenulvianPoint() {
    	assertEquals(this.testedSegment.getOriginalAntepenulvian(),this.testedSegment.getAntepenulvianPoint());
    	assertEquals(this.firstNeighbour.getOriginalAntepenulvian(),this.firstNeighbour.getAntepenulvianPoint());
    	assertEquals(this.secondNeighbour.getOriginalAntepenulvian(),this.secondNeighbour.getAntepenulvianPoint());

    	assertEquals(this.loopSegment.getOriginalAntepenulvian(),this.loopSegment.getAntepenulvianPoint());
    	assertEquals(this.thirdNeighbour.getOriginalAntepenulvian(),this.thirdNeighbour.getAntepenulvianPoint());
    }

    @Test
    public void testGetOtherSidePoint() {
    	assertEquals(this.connection2,this.testedSegment.getOtherSidePoint(this.connection1));
    	assertEquals(this.connection1,this.testedSegment.getOtherSidePoint(this.connection2));
    	assertNull(this.testedSegment.getOtherSidePoint(this.connection3));
    	assertNull(this.testedSegment.getOtherSidePoint(this.culDeSac1));
    	assertNull(this.testedSegment.getOtherSidePoint(this.culDeSac2));
    	assertNull(this.testedSegment.getOtherSidePoint(this.culDeSac3));

    	assertEquals(this.culDeSac1,this.firstNeighbour.getOtherSidePoint(this.connection1));
    	assertNull(this.firstNeighbour.getOtherSidePoint(this.connection2));
    	assertNull(this.firstNeighbour.getOtherSidePoint(this.connection3));
    	assertEquals(this.connection1,this.firstNeighbour.getOtherSidePoint(this.culDeSac1));
    	assertNull(this.firstNeighbour.getOtherSidePoint(this.culDeSac2));
    	assertNull(this.firstNeighbour.getOtherSidePoint(this.culDeSac3));

    	assertNull(this.secondNeighbour.getOtherSidePoint(this.connection1));
    	assertEquals(this.culDeSac2,this.secondNeighbour.getOtherSidePoint(this.connection2));
    	assertNull(this.secondNeighbour.getOtherSidePoint(this.connection3));
    	assertNull(this.secondNeighbour.getOtherSidePoint(this.culDeSac1));
    	assertEquals(this.connection2,this.secondNeighbour.getOtherSidePoint(this.culDeSac2));
    	assertNull(this.secondNeighbour.getOtherSidePoint(this.culDeSac3));

    	assertNull(this.loopSegment.getOtherSidePoint(this.connection1));
    	assertNull(this.loopSegment.getOtherSidePoint(this.connection2));
    	assertEquals(this.connection3,this.loopSegment.getOtherSidePoint(this.connection3));
    	assertNull(this.loopSegment.getOtherSidePoint(this.culDeSac1));
    	assertNull(this.loopSegment.getOtherSidePoint(this.culDeSac2));
    	assertNull(this.loopSegment.getOtherSidePoint(this.culDeSac3));

    	assertNull(this.thirdNeighbour.getOtherSidePoint(this.connection1));
    	assertNull(this.thirdNeighbour.getOtherSidePoint(this.connection2));
    	assertEquals(this.culDeSac3,this.thirdNeighbour.getOtherSidePoint(this.connection3));
    	assertNull(this.thirdNeighbour.getOtherSidePoint(this.culDeSac1));
    	assertNull(this.thirdNeighbour.getOtherSidePoint(this.culDeSac2));
    	assertEquals(this.connection3,this.thirdNeighbour.getOtherSidePoint(this.culDeSac3));
    }

    @Test
    public void testIsConnectedTo() {
    	assertFalse(this.testedSegment.isConnectedTo(this.testedSegment));
    	assertFalse(this.testedSegment.isConnectedTo(this.loopSegment));
    	assertTrue(this.testedSegment.isConnectedTo(this.firstNeighbour));
    	assertTrue(this.testedSegment.isConnectedTo(this.secondNeighbour));
    	assertFalse(this.testedSegment.isConnectedTo(this.thirdNeighbour));

    	assertFalse(this.loopSegment.isConnectedTo(this.testedSegment));
    	assertTrue(this.loopSegment.isConnectedTo(this.loopSegment));
    	assertFalse(this.loopSegment.isConnectedTo(this.firstNeighbour));
    	assertFalse(this.loopSegment.isConnectedTo(this.secondNeighbour));
    	assertTrue(this.loopSegment.isConnectedTo(this.thirdNeighbour));

    	assertTrue(this.firstNeighbour.isConnectedTo(this.testedSegment));
    	assertFalse(this.firstNeighbour.isConnectedTo(this.loopSegment));
    	assertFalse(this.firstNeighbour.isConnectedTo(this.firstNeighbour));
    	assertFalse(this.firstNeighbour.isConnectedTo(this.secondNeighbour));
    	assertFalse(this.firstNeighbour.isConnectedTo(this.thirdNeighbour));

    	assertTrue(this.secondNeighbour.isConnectedTo(this.testedSegment));
    	assertFalse(this.secondNeighbour.isConnectedTo(this.loopSegment));
    	assertFalse(this.secondNeighbour.isConnectedTo(this.firstNeighbour));
    	assertFalse(this.secondNeighbour.isConnectedTo(this.secondNeighbour));
    	assertFalse(this.secondNeighbour.isConnectedTo(this.thirdNeighbour));

    	assertFalse(this.thirdNeighbour.isConnectedTo(this.testedSegment));
    	assertTrue(this.thirdNeighbour.isConnectedTo(this.loopSegment));
    	assertFalse(this.thirdNeighbour.isConnectedTo(this.firstNeighbour));
    	assertFalse(this.thirdNeighbour.isConnectedTo(this.secondNeighbour));
    	assertFalse(this.thirdNeighbour.isConnectedTo(this.thirdNeighbour));
    }

	private void runTestGetGeoLocationForDistance(Vector2d tangent) {
		assertEpsilonEquals(this.testedSegment.getOriginalPointAt(0),
				this.testedSegment.getGeoLocationForDistance(0,0,tangent));
		if (tangent!=null)
			assertColinear(this.testedSegment.getOriginalVectorAt(0),tangent);

		assertEpsilonEquals(this.loopSegment.getOriginalPointAt(0),
				this.loopSegment.getGeoLocationForDistance(0,0,tangent));
		if (tangent!=null)
			assertColinear(this.loopSegment.getOriginalVectorAt(0),tangent);

		assertEpsilonEquals(this.firstNeighbour.getOriginalPointAt(0),
				this.firstNeighbour.getGeoLocationForDistance(0,0,tangent));
		if (tangent!=null)
			assertColinear(this.firstNeighbour.getOriginalVectorAt(0),tangent);

		assertEpsilonEquals(this.secondNeighbour.getOriginalPointAt(0),
				this.secondNeighbour.getGeoLocationForDistance(0,0,tangent));
		if (tangent!=null)
			assertColinear(this.secondNeighbour.getOriginalVectorAt(0),tangent);

		assertEpsilonEquals(this.thirdNeighbour.getOriginalPointAt(0),
				this.thirdNeighbour.getGeoLocationForDistance(0,0,tangent));
		if (tangent!=null)
			assertColinear(this.thirdNeighbour.getOriginalVectorAt(0),tangent);

		//

		assertEpsilonEquals(this.testedSegment.getOriginalPointAt(this.testedSegment.getOriginalPointCount()-1),
				this.testedSegment.getGeoLocationForDistance(this.testedSegment.getOriginalLength(),0,tangent));
		if (tangent!=null)
			assertColinear(this.testedSegment.getOriginalVectorAt(this.testedSegment.getOriginalPointCount()-2),tangent);

		assertEpsilonEquals(this.loopSegment.getOriginalPointAt(this.loopSegment.getOriginalPointCount()-1),
				this.loopSegment.getGeoLocationForDistance(this.loopSegment.getOriginalLength(),0,tangent));
		if (tangent!=null)
			assertColinear(this.loopSegment.getOriginalVectorAt(this.loopSegment.getOriginalPointCount()-2),tangent);

		assertEpsilonEquals(this.firstNeighbour.getOriginalPointAt(this.firstNeighbour.getOriginalPointCount()-1),
				this.firstNeighbour.getGeoLocationForDistance(this.firstNeighbour.getOriginalLength(),0,tangent));
		if (tangent!=null)
			assertColinear(this.firstNeighbour.getOriginalVectorAt(this.firstNeighbour.getOriginalPointCount()-2),tangent);

		assertEpsilonEquals(this.secondNeighbour.getOriginalPointAt(this.secondNeighbour.getOriginalPointCount()-1),
				this.secondNeighbour.getGeoLocationForDistance(this.secondNeighbour.getOriginalLength(),0,tangent));
		if (tangent!=null)
			assertColinear(this.secondNeighbour.getOriginalVectorAt(this.secondNeighbour.getOriginalPointCount()-2),tangent);

		assertEpsilonEquals(this.thirdNeighbour.getOriginalPointAt(this.thirdNeighbour.getOriginalPointCount()-1),
				this.thirdNeighbour.getGeoLocationForDistance(this.thirdNeighbour.getOriginalLength(),0,tangent));
		if (tangent!=null)
			assertColinear(this.thirdNeighbour.getOriginalVectorAt(this.thirdNeighbour.getOriginalPointCount()-2),tangent);

		//

    	Random rnd = new Random();
    	double distance, ratio;
    	for(int i=0; i<TEST_COUNT; ++i) {
    		ratio = rnd.nextDouble();

    		distance = ratio * this.testedSegment.getOriginalLength();
    		assertEpsilonEquals(this.testedSegment.getOriginalPointFor(ratio),
    				this.testedSegment.getGeoLocationForDistance(distance,0,tangent));
    		if (tangent!=null)
    			assertColinear(this.testedSegment.getOriginalVectorFor(ratio),tangent);

    		distance = ratio * this.loopSegment.getOriginalLength();
    		assertEpsilonEquals(this.loopSegment.getOriginalPointFor(ratio),
    				this.loopSegment.getGeoLocationForDistance(distance,0,tangent));
    		if (tangent!=null)
    			assertColinear(this.loopSegment.getOriginalVectorFor(ratio),tangent);

    		distance = ratio * this.firstNeighbour.getOriginalLength();
    		assertEpsilonEquals(this.firstNeighbour.getOriginalPointFor(ratio),
    				this.firstNeighbour.getGeoLocationForDistance(distance,0,tangent));
    		if (tangent!=null)
    			assertColinear(this.firstNeighbour.getOriginalVectorFor(ratio),tangent);

    		distance = ratio * this.secondNeighbour.getOriginalLength();
    		assertEpsilonEquals(this.secondNeighbour.getOriginalPointFor(ratio),
    				this.secondNeighbour.getGeoLocationForDistance(distance,0,tangent));
    		if (tangent!=null)
    			assertColinear(this.secondNeighbour.getOriginalVectorFor(ratio),tangent);

    		distance = ratio * this.thirdNeighbour.getOriginalLength();
    		assertEpsilonEquals(this.thirdNeighbour.getOriginalPointFor(ratio),
    				this.thirdNeighbour.getGeoLocationForDistance(distance,0,tangent));
    		if (tangent!=null)
    			assertColinear(this.thirdNeighbour.getOriginalVectorFor(ratio),tangent);
    	}
	}

	private void runTestGetGeoLocationForDistanceWithShift(RoadSegmentStub sgmt, double ratio, double shift) {
		Point2d p = sgmt.getOriginalPointFor(ratio);
		Vector2d v = sgmt.getOriginalVectorFor(ratio);

		Vector2d vp = new Vector2d(v);
		vp.makeOrthogonal();
		vp.scale(shift);
		Point2d pp = new Point2d(p.getX(),p.getY());
		pp.add(vp);

		double distance = ratio * sgmt.getOriginalLength();
		assertEpsilonEquals(pp, sgmt.getGeoLocationForDistance(distance,shift,(Vector2d)null));
	}

    @Test
	public void testGetGeoLocationForDistance() {
		runTestGetGeoLocationForDistance(null);
		runTestGetGeoLocationForDistance(new Vector2d());

		runTestGetGeoLocationForDistanceWithShift(this.testedSegment, 0., -2.);
		runTestGetGeoLocationForDistanceWithShift(this.testedSegment, 0., 2.);

		runTestGetGeoLocationForDistanceWithShift(this.testedSegment, 1., -2.);
		runTestGetGeoLocationForDistanceWithShift(this.testedSegment, 1., 2.);

    	Random rnd = new Random();
    	double ratio, shift;
    	for(int i=0; i<TEST_COUNT; ++i) {
    		ratio = rnd.nextDouble();
    		shift = rnd.nextDouble() * SHIFTING_RANGE;

    		runTestGetGeoLocationForDistanceWithShift(this.testedSegment, ratio, -shift);
    		runTestGetGeoLocationForDistanceWithShift(this.testedSegment, ratio, shift);
       	}
	}

}
