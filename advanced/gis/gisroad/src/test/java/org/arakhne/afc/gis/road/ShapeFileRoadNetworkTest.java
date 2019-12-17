/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.io.shape.GISShapeFileReader;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.road.primitive.RoadNetwork;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.vmutil.Resources;

/** Unit test for ShapeFileRoadNetwork.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class ShapeFileRoadNetworkTest extends AbstractGisTest {

	private static final String SHP_RESOURCE = "/org/arakhne/afc/gis/road/test.shp"; //$NON-NLS-1$

	@BeforeEach
	public void setUp() throws Exception {
		GeoLocationUtil.setGISCoordinateSystemAsDefault();
	}

	private static RoadNetwork loadNetwork() throws IOException {
		InputStream is = Resources.getResourceAsStream(SHP_RESOURCE);
		assertNotNull(is);

		RoadNetwork network;
		
		try (GISShapeFileReader reader = new GISShapeFileReader(is,RoadPolyline.class)) {
			Rectangle2d worldRect = new Rectangle2d();
			worldRect.setFromCorners(
					reader.getBoundsFromHeader().getMinX(),
					reader.getBoundsFromHeader().getMinY(),
					reader.getBoundsFromHeader().getMaxX(),
					reader.getBoundsFromHeader().getMaxY());
	
			network = new StandardRoadNetwork(worldRect);
			MapElement element;
	
			while ((element=reader.read())!=null) {
				if (element instanceof RoadPolyline) {
					RoadPolyline sgmt = (RoadPolyline)element;
					try {
						network.addRoadSegment(sgmt);
					}
					catch(RoadNetworkException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return network;
	}

	@Test
	public void testLoadNetwork() throws IOException {
		RoadNetwork network = loadNetwork();
		assertNotNull(network);

		RoadSegment[] segments = new RoadSegment[9];

		for(RoadSegment segment : network) {
			Point2d p1 = segment.getFirstPoint();
			Point2d p2 = segment.getLastPoint();

			if (isEpsilonEquals(0, p1.getX())
				&&isEpsilonEquals(0, p1.getY())
				&&isEpsilonEquals(100, p2.getX())
				&&isEpsilonEquals(-100, p2.getY())) {
				assertNull(segments[0]);
				segments[0] = segment;
			}
			else if (isEpsilonEquals(100, p1.getX())
					&&isEpsilonEquals(-7, p1.getY())
					&&isEpsilonEquals(100, p2.getX())
					&&isEpsilonEquals(-100, p2.getY())) {
				assertNull(segments[1]);
				segments[1] = segment;
			}
			else if (isEpsilonEquals(100, p1.getX())
					&&isEpsilonEquals(-7, p1.getY())
					&&isEpsilonEquals(-100, p2.getX())
					&&isEpsilonEquals(300, p2.getY())) {
				assertNull(segments[2]);
				segments[2] = segment;
			}
			else if (isEpsilonEquals(-100, p1.getX())
					&&isEpsilonEquals(300, p1.getY())
					&&isEpsilonEquals(0, p2.getX())
					&&isEpsilonEquals(0, p2.getY())) {
				assertNull(segments[3]);
				segments[3] = segment;
			}
			else if (isEpsilonEquals(100, p1.getX())
					&&isEpsilonEquals(-7, p1.getY())
					&&isEpsilonEquals(0, p2.getX())
					&&isEpsilonEquals(-10, p2.getY())) {
				assertNull(segments[4]);
				segments[4] = segment;
			}
			else if (isEpsilonEquals(100, p1.getX())
					&&isEpsilonEquals(-7, p1.getY())
					&&isEpsilonEquals(100, p2.getX())
					&&isEpsilonEquals(-7, p2.getY())) {
				assertNull(segments[5]);
				segments[5] = segment;
			}
			else if (isEpsilonEquals(100, p1.getX())
					&&isEpsilonEquals(-7, p1.getY())
					&&isEpsilonEquals(100, p2.getX())
					&&isEpsilonEquals(10, p2.getY())) {
				assertNull(segments[6]);
				segments[6] = segment;
			}
			else if (isEpsilonEquals(170, p1.getX())
					&&isEpsilonEquals(-7, p1.getY())
					&&isEpsilonEquals(100, p2.getX())
					&&isEpsilonEquals(-7, p2.getY())) {
				assertNull(segments[7]);
				segments[7] = segment;
			}
			else if (isEpsilonEquals(120, p1.getX())
					&&isEpsilonEquals(-100, p1.getY())
					&&isEpsilonEquals(100, p2.getX())
					&&isEpsilonEquals(-7, p2.getY())) {
				assertNull(segments[8]);
				segments[8] = segment;
			}
			else
				fail("invalid segment values; "+p1.toString()+"; "+p2.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		assertFalse(segments[0].isConnectedTo(segments[0]));
		assertTrue(segments[0].isConnectedTo(segments[1]));
		assertFalse(segments[0].isConnectedTo(segments[2]));
		assertTrue(segments[0].isConnectedTo(segments[3]));
		assertFalse(segments[0].isConnectedTo(segments[4]));
		assertFalse(segments[0].isConnectedTo(segments[5]));
		assertFalse(segments[0].isConnectedTo(segments[6]));
		assertFalse(segments[0].isConnectedTo(segments[7]));
		assertFalse(segments[0].isConnectedTo(segments[8]));

		assertTrue(segments[1].isConnectedTo(segments[0]));
		assertFalse(segments[1].isConnectedTo(segments[1]));
		assertTrue(segments[1].isConnectedTo(segments[2]));
		assertFalse(segments[1].isConnectedTo(segments[3]));
		assertTrue(segments[1].isConnectedTo(segments[4]));
		assertTrue(segments[1].isConnectedTo(segments[5]));
		assertTrue(segments[1].isConnectedTo(segments[6]));
		assertTrue(segments[1].isConnectedTo(segments[7]));
		assertTrue(segments[1].isConnectedTo(segments[8]));

		assertFalse(segments[2].isConnectedTo(segments[0]));
		assertTrue(segments[2].isConnectedTo(segments[1]));
		assertFalse(segments[2].isConnectedTo(segments[2]));
		assertTrue(segments[2].isConnectedTo(segments[3]));
		assertTrue(segments[2].isConnectedTo(segments[4]));
		assertTrue(segments[2].isConnectedTo(segments[5]));
		assertTrue(segments[2].isConnectedTo(segments[6]));
		assertTrue(segments[2].isConnectedTo(segments[7]));
		assertTrue(segments[2].isConnectedTo(segments[8]));

		assertTrue(segments[3].isConnectedTo(segments[0]));
		assertFalse(segments[3].isConnectedTo(segments[1]));
		assertTrue(segments[3].isConnectedTo(segments[2]));
		assertFalse(segments[3].isConnectedTo(segments[3]));
		assertFalse(segments[3].isConnectedTo(segments[4]));
		assertFalse(segments[3].isConnectedTo(segments[5]));
		assertFalse(segments[3].isConnectedTo(segments[6]));
		assertFalse(segments[3].isConnectedTo(segments[7]));
		assertFalse(segments[3].isConnectedTo(segments[8]));

		assertFalse(segments[4].isConnectedTo(segments[0]));
		assertTrue(segments[4].isConnectedTo(segments[1]));
		assertTrue(segments[4].isConnectedTo(segments[2]));
		assertFalse(segments[4].isConnectedTo(segments[3]));
		assertFalse(segments[4].isConnectedTo(segments[4]));
		assertTrue(segments[4].isConnectedTo(segments[5]));
		assertTrue(segments[4].isConnectedTo(segments[6]));
		assertTrue(segments[4].isConnectedTo(segments[7]));
		assertTrue(segments[4].isConnectedTo(segments[8]));

		assertFalse(segments[5].isConnectedTo(segments[0]));
		assertTrue(segments[5].isConnectedTo(segments[1]));
		assertTrue(segments[5].isConnectedTo(segments[2]));
		assertFalse(segments[5].isConnectedTo(segments[3]));
		assertTrue(segments[5].isConnectedTo(segments[4]));
		assertTrue(segments[5].isConnectedTo(segments[5]));
		assertTrue(segments[5].isConnectedTo(segments[6]));
		assertTrue(segments[5].isConnectedTo(segments[7]));
		assertTrue(segments[5].isConnectedTo(segments[8]));

		assertFalse(segments[6].isConnectedTo(segments[0]));
		assertTrue(segments[6].isConnectedTo(segments[1]));
		assertTrue(segments[6].isConnectedTo(segments[2]));
		assertFalse(segments[6].isConnectedTo(segments[3]));
		assertTrue(segments[6].isConnectedTo(segments[4]));
		assertTrue(segments[6].isConnectedTo(segments[5]));
		assertFalse(segments[6].isConnectedTo(segments[6]));
		assertTrue(segments[6].isConnectedTo(segments[7]));
		assertTrue(segments[6].isConnectedTo(segments[8]));

		assertFalse(segments[7].isConnectedTo(segments[0]));
		assertTrue(segments[7].isConnectedTo(segments[1]));
		assertTrue(segments[7].isConnectedTo(segments[2]));
		assertFalse(segments[7].isConnectedTo(segments[3]));
		assertTrue(segments[7].isConnectedTo(segments[4]));
		assertTrue(segments[7].isConnectedTo(segments[5]));
		assertTrue(segments[7].isConnectedTo(segments[6]));
		assertFalse(segments[7].isConnectedTo(segments[7]));
		assertTrue(segments[7].isConnectedTo(segments[8]));

		assertFalse(segments[8].isConnectedTo(segments[0]));
		assertTrue(segments[8].isConnectedTo(segments[1]));
		assertTrue(segments[8].isConnectedTo(segments[2]));
		assertFalse(segments[8].isConnectedTo(segments[3]));
		assertTrue(segments[8].isConnectedTo(segments[4]));
		assertTrue(segments[8].isConnectedTo(segments[5]));
		assertTrue(segments[8].isConnectedTo(segments[6]));
		assertTrue(segments[8].isConnectedTo(segments[7]));
		assertFalse(segments[8].isConnectedTo(segments[8]));
	}

}
