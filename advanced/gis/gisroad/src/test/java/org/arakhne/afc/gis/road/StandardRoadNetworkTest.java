/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.gis.road.primitive.RoadNetworkException;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Unit test for RoadNetwork.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("StandardRoadNetwork")
@SuppressWarnings("all")
public class StandardRoadNetworkTest extends AbstractGisTest {

	private StandardRoadNetwork network;
	private RoadSegmentStub segment1, segment2, segment3, segment4, segment5;
	private RoadSegmentStub segment6, segment7, segment8, segment9;

	@BeforeEach
	public void setUp() throws Exception {
		try {
			GeoLocationUtil.setGISCoordinateSystemAsDefault();
			Rectangle2d worldRect = new Rectangle2d(-500, -500, 1000, 1000);
			network = new StandardRoadNetwork(worldRect, new HeapAttributeCollection());

			//-----------------------------------
			// Picture testnetwork2.png
			//-----------------------------------

			segment1 = new RoadSegmentStub("segment1",0.,0.,100.,100.); //$NON-NLS-1$
			network.addRoadSegment(segment1);

			segment2 = new RoadSegmentStub("segment2",100.,7.,100.,100.); //$NON-NLS-1$
			network.addRoadSegment(segment2);

			segment3 = new RoadSegmentStub("segment3",100.0001,7.,-100.,-300.); //$NON-NLS-1$
			network.addRoadSegment(segment3);

			segment4 = new RoadSegmentStub("segment4",-100.,-300.,0.,0.); //$NON-NLS-1$
			network.addRoadSegment(segment4);

			segment5 = new RoadSegmentStub("segment5",100.,7.,0.,10.); //$NON-NLS-1$
			network.addRoadSegment(segment5);

			segment6 = new RoadSegmentStub("segment6",new double[] {100.,7.,200.,-20.,400.,0.,150.,50.,100.,7.}); //$NON-NLS-1$
			network.addRoadSegment(segment6);

			segment7 = new RoadSegmentStub("segment7",100.,7.,100.,-10.); //$NON-NLS-1$
			network.addRoadSegment(segment7);

			segment8 = new RoadSegmentStub("segment8",170.,7.,100.,7.); //$NON-NLS-1$
			network.addRoadSegment(segment8);

			segment9 = new RoadSegmentStub("segment9",120.,100.,100.,7.); //$NON-NLS-1$
			network.addRoadSegment(segment9);
		}
		catch(RoadNetworkException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterEach
	public void tearDown() {
		segment1 = segment2 = segment3 = segment4 = segment5 = null;
		segment6 = segment7 = segment8 = segment9 = null;
		network = null;
	}

	@DisplayName("addRoadSegment")
	@Nested
	public class AddRoadSegmentTest {

		@DisplayName("(null)")
		@Test
		public void testAddRoadSegment_null() throws Exception {
			assertThrows(RoadNetworkException.class, () -> {
				network.addRoadSegment(null);
			});
		}

		@DisplayName("With multiple adds")
		@Nested
		public class MultipleAddsTest {

			@BeforeEach
			public void setUp() throws Exception {
				// Try to add a segment that has approximatively the coordinates as
				// an existing segment
				RoadSegmentStub segment3bis = new RoadSegmentStub("segment3bis",100.0001,7.,-100.,-300.); //$NON-NLS-1$
				network.addRoadSegment(segment3bis);
		
				// Must do nothing because the segment is already in the road segment.
				network.addRoadSegment(segment4);
		
				// Check the network segment list
		
				assertEquals(9, network.getSegmentCount());
	
				Collection<RoadSegmentStub> expectedList = new ArrayList<>();
				Collections.addAll(expectedList,
						segment1, segment2, segment3, segment4, segment5,
						segment6, segment7, segment8, segment9);
		
				for(RoadSegment segment : network) {
					assertTrue(expectedList.contains(segment));
					expectedList.remove(segment);
				}
				assertTrue(expectedList.isEmpty());
		
				Collections.addAll(expectedList,
						segment1, segment2, segment3, segment4, segment5,
						segment6, segment7, segment8, segment9);
				for(RoadSegment segment : network.getRoadSegments()) {
					assertTrue(expectedList.contains(segment));
					expectedList.remove(segment);
				}
				assertTrue(expectedList.isEmpty());
		
				// Check the network connections
				// -100;-300	=> segment3,segment4
				// 0;0			=> segment1,segment4
				// 0;10			=> segment5
				// 100;7		=> segment2,segment3,segment5
				// 100;100		=> segment1,segment2
			}

			@DisplayName("#1")
			@Test
			public void testAddRoadSegment_1() throws Exception {
				assertEquals(segment1.getFirstPoint(),segment4.getLastPoint());
			}

			@DisplayName("#2")
			@Test
			public void testAddRoadSegment_2() throws Exception {
				assertEquals(segment1.getLastPoint(),segment2.getLastPoint());
			}

			@DisplayName("#3")
			@Test
			public void testAddRoadSegment_3() throws Exception {
				assertEquals(segment2.getFirstPoint(),segment3.getFirstPoint());
			}

			@DisplayName("#4")
			@Test
			public void testAddRoadSegment_4() throws Exception {
				assertEquals(segment2.getFirstPoint(),segment5.getFirstPoint());
			}

			@DisplayName("#5")
			@Test
			public void testAddRoadSegment_5() throws Exception {
				assertEquals(segment2.getFirstPoint(),segment6.getFirstPoint());
			}

			@DisplayName("#6")
			@Test
			public void testAddRoadSegment_6() throws Exception {
				assertEquals(segment2.getFirstPoint(),segment6.getLastPoint());
			}

			@DisplayName("#7")
			@Test
			public void testAddRoadSegment_7() throws Exception {
				assertEquals(segment2.getFirstPoint(),segment7.getFirstPoint());
			}

			@DisplayName("#8")
			@Test
			public void testAddRoadSegment_8() throws Exception {
				assertEquals(segment2.getFirstPoint(),segment8.getLastPoint());
			}

			@DisplayName("#9")
			@Test
			public void testAddRoadSegment_9() throws Exception {
				assertEquals(segment2.getFirstPoint(),segment9.getLastPoint());
			}

			@DisplayName("#10")
			@Test
			public void testAddRoadSegment_10() throws Exception {
				assertEquals(segment2.getLastPoint(),segment1.getLastPoint());
			}

			@DisplayName("#11")
			@Test
			public void testAddRoadSegment_11() throws Exception {
				assertEquals(segment3.getFirstPoint(),segment2.getFirstPoint());
			}

			@DisplayName("#12")
			@Test
			public void testAddRoadSegment_12() throws Exception {
				assertEquals(segment3.getFirstPoint(),segment5.getFirstPoint());
			}

			@DisplayName("#13")
			@Test
			public void testAddRoadSegment_13() throws Exception {
				assertEquals(segment3.getFirstPoint(),segment6.getFirstPoint());
			}

			@DisplayName("#14")
			@Test
			public void testAddRoadSegment_14() throws Exception {
				assertEquals(segment3.getFirstPoint(),segment6.getLastPoint());
			}

			@DisplayName("#15")
			@Test
			public void testAddRoadSegment_15() throws Exception {
				assertEquals(segment3.getFirstPoint(),segment7.getFirstPoint());
			}

			@DisplayName("#16")
			@Test
			public void testAddRoadSegment_16() throws Exception {
				assertEquals(segment3.getFirstPoint(),segment8.getLastPoint());
			}

			@DisplayName("#17")
			@Test
			public void testAddRoadSegment_17() throws Exception {
				assertEquals(segment3.getFirstPoint(),segment9.getLastPoint());
			}

			@DisplayName("#18")
			@Test
			public void testAddRoadSegment_18() throws Exception {
				assertEquals(segment3.getLastPoint(),segment4.getFirstPoint());
			}

			@DisplayName("#19")
			@Test
			public void testAddRoadSegment_19() throws Exception {
				assertEquals(segment4.getFirstPoint(),segment3.getLastPoint());
			}

			@DisplayName("#20")
			@Test
			public void testAddRoadSegment_20() throws Exception {
				assertEquals(segment4.getLastPoint(),segment1.getFirstPoint());
			}

			@DisplayName("#21")
			@Test
			public void testAddRoadSegment_21() throws Exception {
				assertEquals(segment5.getFirstPoint(),segment2.getFirstPoint());
			}

			@DisplayName("#22")
			@Test
			public void testAddRoadSegment_22() throws Exception {
				assertEquals(segment5.getFirstPoint(),segment3.getFirstPoint());
			}

			@DisplayName("#23")
			@Test
			public void testAddRoadSegment_23() throws Exception {
				assertEquals(segment5.getFirstPoint(),segment6.getFirstPoint());
			}

			@DisplayName("#24")
			@Test
			public void testAddRoadSegment_24() throws Exception {
				assertEquals(segment5.getFirstPoint(),segment6.getLastPoint());
			}

			@DisplayName("#25")
			@Test
			public void testAddRoadSegment_25() throws Exception {
				assertEquals(segment5.getFirstPoint(),segment7.getFirstPoint());
			}

			@DisplayName("#26")
			@Test
			public void testAddRoadSegment_26() throws Exception {
				assertEquals(segment5.getFirstPoint(),segment8.getLastPoint());
			}

			@DisplayName("#27")
			@Test
			public void testAddRoadSegment_27() throws Exception {
				assertEquals(segment5.getFirstPoint(),segment9.getLastPoint());
			}

			@DisplayName("#28")
			@Test
			public void testAddRoadSegment_28() throws Exception {
				assertEquals(new Point2d(0.,10.), segment5.getLastPoint());
			}

			@DisplayName("#29")
			@Test
			public void testAddRoadSegment_29() throws Exception {
				assertEquals(segment6.getFirstPoint(),segment2.getFirstPoint());
			}

			@DisplayName("#30")
			@Test
			public void testAddRoadSegment_30() throws Exception {
				assertEquals(segment6.getFirstPoint(),segment3.getFirstPoint());
			}

			@DisplayName("#31")
			@Test
			public void testAddRoadSegment_31() throws Exception {
				assertEquals(segment6.getFirstPoint(),segment5.getFirstPoint());
			}

			@DisplayName("#32")
			@Test
			public void testAddRoadSegment_32() throws Exception {
				assertEquals(segment6.getFirstPoint(),segment6.getLastPoint());
			}

			@DisplayName("#33")
			@Test
			public void testAddRoadSegment_33() throws Exception {
				assertEquals(segment6.getFirstPoint(),segment7.getFirstPoint());
			}

			@DisplayName("#34")
			@Test
			public void testAddRoadSegment_34() throws Exception {
				assertEquals(segment6.getFirstPoint(),segment8.getLastPoint());
			}

			@DisplayName("#35")
			@Test
			public void testAddRoadSegment_35() throws Exception {
				assertEquals(segment6.getFirstPoint(),segment9.getLastPoint());
			}

			@DisplayName("#36")
			@Test
			public void testAddRoadSegment_36() throws Exception {
				assertEquals(segment6.getLastPoint(),segment2.getFirstPoint());
			}

			@DisplayName("#37")
			@Test
			public void testAddRoadSegment_37() throws Exception {
				assertEquals(segment6.getLastPoint(),segment3.getFirstPoint());
			}

			@DisplayName("#38")
			@Test
			public void testAddRoadSegment_38() throws Exception {
				assertEquals(segment6.getLastPoint(),segment5.getFirstPoint());
			}

			@DisplayName("#39")
			@Test
			public void testAddRoadSegment_39() throws Exception {
				assertEquals(segment6.getLastPoint(),segment6.getFirstPoint());
			}

			@DisplayName("#40")
			@Test
			public void testAddRoadSegment_40() throws Exception {
				assertEquals(segment6.getLastPoint(),segment7.getFirstPoint());
			}

			@DisplayName("#41")
			@Test
			public void testAddRoadSegment_41() throws Exception {
				assertEquals(segment6.getLastPoint(),segment8.getLastPoint());
			}

			@DisplayName("#42")
			@Test
			public void testAddRoadSegment_42() throws Exception {
				assertEquals(segment6.getLastPoint(),segment9.getLastPoint());
			}

			@DisplayName("#43")
			@Test
			public void testAddRoadSegment_43() throws Exception {
				assertEquals(segment7.getFirstPoint(),segment2.getFirstPoint());
			}

			@DisplayName("#44")
			@Test
			public void testAddRoadSegment_44() throws Exception {
				assertEquals(segment7.getFirstPoint(),segment3.getFirstPoint());
			}

			@DisplayName("#45")
			@Test
			public void testAddRoadSegment_45() throws Exception {
				assertEquals(segment7.getFirstPoint(),segment5.getFirstPoint());
			}

			@DisplayName("#46")
			@Test
			public void testAddRoadSegment_46() throws Exception {
				assertEquals(segment7.getFirstPoint(),segment6.getFirstPoint());
			}

			@DisplayName("#47")
			@Test
			public void testAddRoadSegment_47() throws Exception {
				assertEquals(segment7.getFirstPoint(),segment6.getLastPoint());
			}

			@DisplayName("#48")
			@Test
			public void testAddRoadSegment_48() throws Exception {
				assertEquals(segment7.getFirstPoint(),segment8.getLastPoint());
			}

			@DisplayName("#49")
			@Test
			public void testAddRoadSegment_49() throws Exception {
				assertEquals(segment7.getFirstPoint(),segment9.getLastPoint());
			}

			@DisplayName("#50")
			@Test
			public void testAddRoadSegment_50() throws Exception {
				assertEquals(new Point2d(100.,-10.), segment7.getLastPoint());
			}

			@DisplayName("#51")
			@Test
			public void testAddRoadSegment_51() throws Exception {
				assertEquals(new Point2d(170.,7.), segment8.getFirstPoint());
			}

			@DisplayName("#52")
			@Test
			public void testAddRoadSegment_52() throws Exception {
				assertEquals(segment8.getLastPoint(),segment2.getFirstPoint());
			}

			@DisplayName("#53")
			@Test
			public void testAddRoadSegment_53() throws Exception {
				assertEquals(segment8.getLastPoint(),segment3.getFirstPoint());
			}

			@DisplayName("#54")
			@Test
			public void testAddRoadSegment_54() throws Exception {
				assertEquals(segment8.getLastPoint(),segment5.getFirstPoint());
			}

			@DisplayName("#55")
			@Test
			public void testAddRoadSegment_55() throws Exception {
				assertEquals(segment8.getLastPoint(),segment6.getFirstPoint());
			}

			@DisplayName("#56")
			@Test
			public void testAddRoadSegment_56() throws Exception {
				assertEquals(segment8.getLastPoint(),segment6.getLastPoint());
			}

			@DisplayName("#57")
			@Test
			public void testAddRoadSegment_57() throws Exception {
				assertEquals(segment8.getLastPoint(),segment7.getFirstPoint());
			}

			@DisplayName("#58")
			@Test
			public void testAddRoadSegment_58() throws Exception {
				assertEquals(segment8.getLastPoint(),segment9.getLastPoint());
			}

			@DisplayName("#59")
			@Test
			public void testAddRoadSegment_59() throws Exception {
				assertEquals(new Point2d(120.,100.), segment9.getFirstPoint());
			}

			@DisplayName("#60")
			@Test
			public void testAddRoadSegment_60() throws Exception {
				assertEquals(segment9.getLastPoint(),segment2.getFirstPoint());
			}

			@DisplayName("#61")
			@Test
			public void testAddRoadSegment_61() throws Exception {
				assertEquals(segment9.getLastPoint(),segment3.getFirstPoint());
			}

			@DisplayName("#62")
			@Test
			public void testAddRoadSegment_62() throws Exception {
				assertEquals(segment9.getLastPoint(),segment5.getFirstPoint());
			}

			@DisplayName("#63")
			@Test
			public void testAddRoadSegment_63() throws Exception {
				assertEquals(segment9.getLastPoint(),segment6.getFirstPoint());
			}

			@DisplayName("#64")
			@Test
			public void testAddRoadSegment_64() throws Exception {
				assertEquals(segment9.getLastPoint(),segment6.getLastPoint());
			}

			@DisplayName("#65")
			@Test
			public void testAddRoadSegment_65() throws Exception {
				assertEquals(segment9.getLastPoint(),segment7.getFirstPoint());
			}

			@DisplayName("#66")
			@Test
			public void testAddRoadSegment_66() throws Exception {
				assertEquals(segment9.getLastPoint(),segment8.getLastPoint());
			}
		}
	}

	@DisplayName("toCounterclockwiseIterator at connection")
	@Nested
	public class ToCounterclockwiseIteratorAtConnectionTest {

		@DisplayName("Left Handed")
		@Nested
		public class LeftHandedTest {

			private CoordinateSystem2D system;

			@BeforeEach
			public void setUp() {
				system = CoordinateSystem2D.XY_LEFT_HAND;
			}
	
			@DisplayName("#1")
			@Test
			public void testConnectionIterator_LeftHand_1() {
				// 0;0			=> segment1 (out), segment4 (in)
				var iterator = segment1.getBeginPoint().toCounterclockwiseIterator(segment1, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#2")
			@Test
			public void testConnectionIterator_LeftHand_2() {
				// 100;100		=> segment1 (in), segment2 (in)
				var iterator = segment1.getEndPoint().toCounterclockwiseIterator(segment1, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#3")
			@Test
			public void testConnectionIterator_LeftHand_3() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment2.getBeginPoint().toCounterclockwiseIterator(segment2, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#4")
			@Test
			public void testConnectionIterator_LeftHand_4() {
				// 100;100		=> segment1 (in), segment2 (in)
				var iterator = segment2.getEndPoint().toCounterclockwiseIterator(segment2, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#5")
			@Test
			public void testConnectionIterator_LeftHand_5() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment3.getBeginPoint().toCounterclockwiseIterator(segment3, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#6")
			@Test
			public void testConnectionIterator_LeftHand_6() {
				// -100;-300	=> segment3 (in), segment4 (out)
				var iterator = segment3.getEndPoint().toCounterclockwiseIterator(segment3, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#7")
			@Test
			public void testConnectionIterator_LeftHand_7() {
				// -100;-300	=> segment3 (in), segment4 (out)
				var iterator = segment4.getBeginPoint().toCounterclockwiseIterator(segment4, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#8")
			@Test
			public void testConnectionIterator_LeftHand_8() {
				// 0;0			=> segment1 (out), segment4 (in)
				var iterator = segment4.getEndPoint().toCounterclockwiseIterator(segment4, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#9")
			@Test
			public void testConnectionIterator_LeftHand_9() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment5.getBeginPoint().toCounterclockwiseIterator(segment5, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#10")
			@Test
			public void testConnectionIterator_LeftHand_10() {
				// 0;10			=> segment5 (in)
				var iterator = segment5.getEndPoint().toCounterclockwiseIterator(segment5, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#11")
			@Test
			public void testConnectionIterator_LeftHand_11() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment6.getBeginPoint().toCounterclockwiseIterator(segment6, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#12")
			@Test
			public void testConnectionIterator_LeftHand_12() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment6.getEndPoint().toCounterclockwiseIterator(segment6, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#13")
			@Test
			public void testConnectionIterator_LeftHand_13() {
				// 100;-10		=> segment7 (in)
				var iterator = segment7.getBeginPoint().toCounterclockwiseIterator(segment7, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#14")
			@Test
			public void testConnectionIterator_LeftHand_14() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment7.getEndPoint().toCounterclockwiseIterator(segment7, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#15")
			@Test
			public void testConnectionIterator_LeftHand_15() {
				// 170;7		=> segment8 (out)
				var iterator = segment8.getBeginPoint().toCounterclockwiseIterator(segment8, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#16")
			@Test
			public void testConnectionIterator_LeftHand_16() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment8.getEndPoint().toCounterclockwiseIterator(segment8, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#17")
			@Test
			public void testConnectionIterator_LeftHand_17() {
				// 120;100		=> segment9 (out)
				var iterator = segment9.getBeginPoint().toCounterclockwiseIterator(segment9, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#18")
			@Test
			public void testConnectionIterator_LeftHand_18() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment9.getEndPoint().toCounterclockwiseIterator(segment9, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertFalse(iterator.hasNext());
			}
		}

		@DisplayName("Right Handed")
		@Nested
		public class RightHandedTest {

			private CoordinateSystem2D system;

			@BeforeEach
			public void setUp() {
				system = CoordinateSystem2D.XY_RIGHT_HAND;
			}
	
			@DisplayName("#1")
			@Test
			public void testConnectionIterator_RightHand_1() {
				// 0;0			=> segment1 (out), segment4 (in)
				var iterator = segment1.getBeginPoint().toCounterclockwiseIterator(segment1, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#2")
			@Test
			public void testConnectionIterator_RightHand_2() {
				// 100;100		=> segment1 (in), segment2 (in)
				var iterator = segment1.getEndPoint().toCounterclockwiseIterator(segment1, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#3")
			@Test
			public void testConnectionIterator_RightHand_3() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment2.getBeginPoint().toCounterclockwiseIterator(segment2, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#4")
			@Test
			public void testConnectionIterator_RightHand_4() {
				// 100;100		=> segment1 (in), segment2 (in)
				var iterator = segment2.getEndPoint().toCounterclockwiseIterator(segment2, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#5")
			@Test
			public void testConnectionIterator_RightHand_5() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment3.getBeginPoint().toCounterclockwiseIterator(segment3, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#6")
			@Test
			public void testConnectionIterator_RightHand_6() {
				// -100;-300	=> segment3 (in), segment4 (out)
				var iterator = segment3.getEndPoint().toCounterclockwiseIterator(segment3, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#7")
			@Test
			public void testConnectionIterator_RightHand_7() {
				// -100;-300	=> segment3 (in), segment4 (out)
				var iterator = segment4.getBeginPoint().toCounterclockwiseIterator(segment4, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#8")
			@Test
			public void testConnectionIterator_RightHand_8() {
				// 0;0			=> segment1 (out), segment4 (in)
				var iterator = segment4.getEndPoint().toCounterclockwiseIterator(segment4, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment4, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment1, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#9")
			@Test
			public void testConnectionIterator_RightHand_9() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment5.getBeginPoint().toCounterclockwiseIterator(segment5, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#10")
			@Test
			public void testConnectionIterator_RightHand_10() {
				// 0;10			=> segment5 (in)
				var iterator = segment5.getEndPoint().toCounterclockwiseIterator(segment5, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#11")
			@Test
			public void testConnectionIterator_RightHand_11() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment6.getBeginPoint().toCounterclockwiseIterator(segment6, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#12")
			@Test
			public void testConnectionIterator_RightHand_12() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment6.getEndPoint().toCounterclockwiseIterator(segment6, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#13")
			@Test
			public void testConnectionIterator_RightHand_13() {
				// 100;-10		=> segment7 (in)
				var iterator = segment7.getBeginPoint().toCounterclockwiseIterator(segment7, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#14")
			@Test
			public void testConnectionIterator_RightHand_14() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment7.getEndPoint().toCounterclockwiseIterator(segment7, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#15")
			@Test
			public void testConnectionIterator_RightHand_15() {
				// 170;7		=> segment8 (out)
				var iterator = segment8.getBeginPoint().toCounterclockwiseIterator(segment8, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#16")
			@Test
			public void testConnectionIterator_RightHand_16() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment8.getEndPoint().toCounterclockwiseIterator(segment8, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#17")
			@Test
			public void testConnectionIterator_RightHand_17() {
				// 120;100		=> segment9 (out)
				var iterator = segment9.getBeginPoint().toCounterclockwiseIterator(segment9, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertFalse(iterator.hasNext());
			}
			
			@DisplayName("#18")
			@Test
			public void testConnectionIterator_RightHand_18() {
				// 100;7		=> segment8 (in), segment6 (in), segment9 (in), segment2 (out), segment5 (out), segment3 (out), segment7 (out), segment6 (out)
				var iterator = segment9.getEndPoint().toCounterclockwiseIterator(segment9, system);
				assertTrue(iterator.hasNext());
				assertEquals(segment9, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment2, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment5, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment3, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment7, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment8, iterator.next());
				assertTrue(iterator.hasNext());
				assertEquals(segment6, iterator.next());
				assertFalse(iterator.hasNext());
			}
		}
	}

	@DisplayName("removeRoadSegment")
	@Nested
	public class RemoveRoadSegmentTest {

		@BeforeEach
		public void setUp() {
			// Remove the segments
			assertTrue(network.removeRoadSegment(segment4));
			assertTrue(network.removeRoadSegment(segment6));
		}

		@DisplayName("#1")
		@Test
		public void testNetworkSegmentList_1() {
			// Check the network segment list
			assertEquals(7, network.getSegmentCount());
		}

		@DisplayName("#2")
		@Test
		public void testNetworkSegmentList_2() {
			Collection<RoadSegmentStub> expectedList = new ArrayList<>();
			Collections.addAll(expectedList,
					segment1, segment2, segment3, segment5,
					segment7, segment8, segment9);
	
			for(RoadSegment segment : network) {
				assertTrue(expectedList.contains(segment));
				expectedList.remove(segment);
			}
			assertTrue(expectedList.isEmpty());
		}

		@DisplayName("#3")
		@Test
		public void testNetworkSegmentList_3() {
			Collection<RoadSegmentStub> expectedList = new ArrayList<>();
			Collections.addAll(expectedList,
					segment1, segment2, segment3, segment5,
					segment7, segment8, segment9);
			for(RoadSegment segment : network.getRoadSegments()) {
				assertTrue(expectedList.contains(segment));
				expectedList.remove(segment);
			}
			assertTrue(expectedList.isEmpty());
		}

		@DisplayName("#4")
		@Test
		public void testNetworkConnections_4() {
			assertEquals(new Point2d(0.,0.), segment1.getFirstPoint());
		}

		@DisplayName("#5")
		@Test
		public void testNetworkConnections_5() {
			assertEquals(segment1.getLastPoint(),segment2.getLastPoint());
		}

		@DisplayName("#6")
		@Test
		public void testNetworkConnections_6() {
			assertEquals(segment2.getFirstPoint(),segment3.getFirstPoint());
		}

		@DisplayName("#7")
		@Test
		public void testNetworkConnections_7() {
			assertEquals(segment2.getFirstPoint(),segment5.getFirstPoint());
		}

		@DisplayName("#8")
		@Test
		public void testNetworkConnections_8() {
			assertEquals(segment2.getFirstPoint(),segment7.getFirstPoint());
		}

		@DisplayName("#9")
		@Test
		public void testNetworkConnections_9() {
			assertEquals(segment2.getFirstPoint(),segment8.getLastPoint());
		}

		@DisplayName("#10")
		@Test
		public void testNetworkConnections_10() {
			assertEquals(segment2.getFirstPoint(),segment9.getLastPoint());
		}

		@DisplayName("#11")
		@Test
		public void testNetworkConnections_11() {
			assertEquals(segment2.getLastPoint(),segment1.getLastPoint());
		}

		@DisplayName("#12")
		@Test
		public void testNetworkConnections_12() {
			assertEquals(segment3.getFirstPoint(),segment2.getFirstPoint());
		}

		@DisplayName("#13")
		@Test
		public void testNetworkConnections_13() {
			assertEquals(segment3.getFirstPoint(),segment5.getFirstPoint());
		}

		@DisplayName("#14")
		@Test
		public void testNetworkConnections_14() {
			assertEquals(segment3.getFirstPoint(),segment7.getFirstPoint());
		}

		@DisplayName("#15")
		@Test
		public void testNetworkConnections_15() {
			assertEquals(segment3.getFirstPoint(),segment8.getLastPoint());
		}

		@DisplayName("#16")
		@Test
		public void testNetworkConnections_16() {
			assertEquals(segment3.getFirstPoint(),segment9.getLastPoint());
		}

		@DisplayName("#17")
		@Test
		public void testNetworkConnections_17() {
			assertEquals(new Point2d(-100.,-300), segment3.getLastPoint());
		}

		@DisplayName("#18")
		@Test
		public void testNetworkConnections_18() {
			assertEquals(segment5.getFirstPoint(),segment2.getFirstPoint());
		}

		@DisplayName("#19")
		@Test
		public void testNetworkConnections_19() {
			assertEquals(segment5.getFirstPoint(),segment3.getFirstPoint());
		}

		@DisplayName("#20")
		@Test
		public void testNetworkConnections_20() {
			assertEquals(segment5.getFirstPoint(),segment7.getFirstPoint());
		}

		@DisplayName("#21")
		@Test
		public void testNetworkConnections_21() {
			assertEquals(segment5.getFirstPoint(),segment8.getLastPoint());
		}

		@DisplayName("#22")
		@Test
		public void testNetworkConnections_22() {
			assertEquals(segment5.getFirstPoint(),segment9.getLastPoint());
		}

		@DisplayName("#23")
		@Test
		public void testNetworkConnections_23() {
			assertEquals(new Point2d(0.,10.), segment5.getLastPoint());
		}

		@DisplayName("#24")
		@Test
		public void testNetworkConnections_24() {
			assertEquals(segment7.getFirstPoint(),segment2.getFirstPoint());
		}

		@DisplayName("#25")
		@Test
		public void testNetworkConnections_25() {
			assertEquals(segment7.getFirstPoint(),segment3.getFirstPoint());
		}

		@DisplayName("#26")
		@Test
		public void testNetworkConnections_26() {
			assertEquals(segment7.getFirstPoint(),segment5.getFirstPoint());
		}

		@DisplayName("#27")
		@Test
		public void testNetworkConnections_27() {
			assertEquals(segment7.getFirstPoint(),segment8.getLastPoint());
		}

		@DisplayName("#28")
		@Test
		public void testNetworkConnections_28() {
			assertEquals(segment7.getFirstPoint(),segment9.getLastPoint());
		}

		@DisplayName("#29")
		@Test
		public void testNetworkConnections_29() {
			assertEquals(new Point2d(100.,-10.), segment7.getLastPoint());
		}

		@DisplayName("#30")
		@Test
		public void testNetworkConnections_30() {
			assertEquals(new Point2d(170.,7.), segment8.getFirstPoint());
		}

		@DisplayName("#31")
		@Test
		public void testNetworkConnections_31() {
			assertEquals(segment8.getLastPoint(),segment2.getFirstPoint());
		}

		@DisplayName("#32")
		@Test
		public void testNetworkConnections_32() {
			assertEquals(segment8.getLastPoint(),segment3.getFirstPoint());
		}

		@DisplayName("#33")
		@Test
		public void testNetworkConnections_33() {
			assertEquals(segment8.getLastPoint(),segment5.getFirstPoint());
		}

		@DisplayName("#34")
		@Test
		public void testNetworkConnections_34() {
			assertEquals(segment8.getLastPoint(),segment7.getFirstPoint());
		}

		@DisplayName("#35")
		@Test
		public void testNetworkConnections_35() {
			assertEquals(segment8.getLastPoint(),segment9.getLastPoint());
		}

		@DisplayName("#36")
		@Test
		public void testNetworkConnections_36() {
			assertEquals(new Point2d(120.,100.), segment9.getFirstPoint());
		}

		@DisplayName("#37")
		@Test
		public void testNetworkConnections_37() {
			assertEquals(segment9.getLastPoint(),segment2.getFirstPoint());
		}

		@DisplayName("#38")
		@Test
		public void testNetworkConnections_38() {
			assertEquals(segment9.getLastPoint(),segment3.getFirstPoint());
		}

		@DisplayName("#39")
		@Test
		public void testNetworkConnections_39() {
			assertEquals(segment9.getLastPoint(),segment5.getFirstPoint());
		}

		@DisplayName("#40")
		@Test
		public void testNetworkConnections_40() {
			assertEquals(segment9.getLastPoint(),segment7.getFirstPoint());
		}

		@DisplayName("#41")
		@Test
		public void testNetworkConnections_41() {
			assertEquals(segment9.getLastPoint(),segment8.getLastPoint());
		}

		@DisplayName("#42")
		@Test
		public void testInvalidCalls_42() {
			assertFalse(network.removeRoadSegment(null));
		}

		@DisplayName("#43")
		@Test
		public void testInvalidCalls_43() {
			assertFalse(network.removeRoadSegment(segment4));
		}
	}

	@DisplayName("toCounterclockwiseIterator")
	@Nested
	public class ToCounterclockwiseIteratorTest {

		@BeforeEach
		public void setUp() {
			network.removeRoadSegment(segment4);
			network.removeRoadSegment(segment6);
			segment4 = segment6 = null;
			// CAUTION: Remember that segment4 and segment6 were removed!
			assumeTrue(CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded());
		}

		@DisplayName("#1")
		@Test
		public void testConnectionIteratorAfterRemoval_1() {
			var iterator = segment1.getBeginPoint().toCounterclockwiseIterator(segment1);
			assertTrue(iterator.hasNext());
			assertEquals(segment1, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#2")
		@Test
		public void testConnectionIteratorAfterRemoval_2() {
			var iterator = segment1.getEndPoint().toCounterclockwiseIterator(segment1);
			assertTrue(iterator.hasNext());
			assertEquals(segment1, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment2, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#3")
		@Test
		public void testConnectionIteratorAfterRemoval_3() {
			var iterator = segment2.getBeginPoint().toCounterclockwiseIterator(segment2);
			assertTrue(iterator.hasNext());
			assertEquals(segment2, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment5, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment3, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment7, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment8, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment9, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#4")
		@Test
		public void testConnectionIteratorAfterRemoval_4() {
			var iterator = segment2.getEndPoint().toCounterclockwiseIterator(segment2);
			assertTrue(iterator.hasNext());
			assertEquals(segment2, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment1, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#5")
		@Test
		public void testConnectionIteratorAfterRemoval_5() {
			var iterator = segment3.getBeginPoint().toCounterclockwiseIterator(segment3);
			assertTrue(iterator.hasNext());
			assertEquals(segment3, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment7, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment8, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment9, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment2, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment5, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#6")
		@Test
		public void testConnectionIteratorAfterRemoval_6() {
			var iterator = segment3.getEndPoint().toCounterclockwiseIterator(segment3);
			assertTrue(iterator.hasNext());
			assertEquals(segment3, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#7")
		@Test
		public void testConnectionIteratorAfterRemoval_7() {
			var iterator = segment5.getBeginPoint().toCounterclockwiseIterator(segment5);
			assertTrue(iterator.hasNext());
			assertEquals(segment5, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment3, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment7, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment8, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment9, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment2, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#8")
		@Test
		public void testConnectionIteratorAfterRemoval_8() {
			var iterator = segment5.getEndPoint().toCounterclockwiseIterator(segment5);
			assertTrue(iterator.hasNext());
			assertEquals(segment5, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#9")
		@Test
		public void testConnectionIteratorAfterRemoval_9() {
			var iterator = segment7.getBeginPoint().toCounterclockwiseIterator(segment7);
			assertTrue(iterator.hasNext());
			assertEquals(segment7, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment8, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment9, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment2, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment5, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment3, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#10")
		@Test
		public void testConnectionIteratorAfterRemoval_10() {
			var iterator = segment7.getEndPoint().toCounterclockwiseIterator(segment7);
			assertTrue(iterator.hasNext());
			assertEquals(segment7, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#11")
		@Test
		public void testConnectionIteratorAfterRemoval_11() {
			var iterator = segment8.getBeginPoint().toCounterclockwiseIterator(segment8);
			assertTrue(iterator.hasNext());
			assertEquals(segment8, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#12")
		@Test
		public void testConnectionIteratorAfterRemoval_12() {
			var iterator = segment8.getEndPoint().toCounterclockwiseIterator(segment8);
			assertTrue(iterator.hasNext());
			assertEquals(segment8, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment9, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment2, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment5, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment3, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment7, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#13")
		@Test
		public void testConnectionIteratorAfterRemoval_13() {
			var iterator = segment9.getBeginPoint().toCounterclockwiseIterator(segment9);
			assertTrue(iterator.hasNext());
			assertEquals(segment9, iterator.next());
			assertFalse(iterator.hasNext());
		}

		@DisplayName("#14")
		@Test
		public void testConnectionIteratorAfterRemoval_14() {
			var iterator = segment9.getEndPoint().toCounterclockwiseIterator(segment9);
			assertTrue(iterator.hasNext());
			assertEquals(segment9, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment2, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment5, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment3, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment7, iterator.next());
			assertTrue(iterator.hasNext());
			assertEquals(segment8, iterator.next());
			assertFalse(iterator.hasNext());
		}
	}

	@DisplayName("getNearestConnection")
	@Nested
	public class GetNearestConnectionTest {

		@DisplayName("#1")
		@Test
		public void testGetNearestConnection_1() {
			assertEquals(segment1.getBeginPoint(),
					network.getNearestConnection(
							new Point2d(-5.,0.)));
		}

		@DisplayName("#2")
		@Test
		public void testGetNearestConnection_2() {
			assertEquals(segment9.getBeginPoint(),
					network.getNearestConnection(
							new Point2d(1000.,1000.)));
		}

		@DisplayName("#3")
		@Test
		public void testGetNearestConnection_3() {
			assertEquals(segment4.getEndPoint(),
					network.getNearestConnection(
							new Point2d(0.,0.)));
		}
	}

	@DisplayName("getNearestSegment")
	@Nested
	public class GetNearestSegmentTest {

		@DisplayName("#1")
		@Test
		public void testGetNearestSegment_1() {
			assertEquals(segment4,
					network.getNearestSegment(
							new Point2d(-5.,0.)));
		}

		@DisplayName("#2")
		@Test
		public void testGetNearestSegment_2() {
			assertEquals(segment6,
					network.getNearestSegment(
							new Point2d(1000.,1000.)));
		}

		@DisplayName("#3")
		@Test
		public void testGetNearestSegment_3() {
			assertEquals(segment6,
					network.getNearestSegment(
							new Point2d(1000.,0.)));
		}

		@DisplayName("#4")
		@Test
		public void testGetNearestSegment_4() {
			assertEquals(segment1,
					network.getNearestSegment(
							new Point2d(0.,1000.)));
		}

		@DisplayName("#5")
		@Test
		public void testGetNearestSegment_5() {
			assertTrue(Arrays.asList(segment4, segment1).contains(network.getNearestSegment(
							new Point2d(0.,0.))));
		}
	}

	@DisplayName("getNearestPosition")
	@Nested
	public class GetNearestPositionTest {

		@DisplayName("#1")
		@Test
		public void testGetNearestPosition_1() {
			var p = network.getNearestPosition(new Point2d(-5.,0.));
			assertNotNull(p);
			assertSame(segment4, p.getSegment());
			assertEpsilonEquals(314.6466271867538, p.getCurvilineCoordinate());
			assertEpsilonEquals(0., p.getLateralDistance());
		}	

		@DisplayName("#2")
		@Test
		public void testGetNearestPosition_2() {
			var p = network.getNearestPosition(new Point2d(1000.,1000.));
			assertNotNull(p);
			assertSame(segment6, p.getSegment());
			assertEpsilonEquals(304.5784, p.getCurvilineCoordinate());
			assertEpsilonEquals(0., p.getLateralDistance());
		}	

		@DisplayName("#3")
		@Test
		public void testGetNearestPosition_3() {
			var p = network.getNearestPosition(new Point2d(1000.,0.));
			assertNotNull(p);
			assertSame(segment6, p.getSegment());
			assertEpsilonEquals(304.57839869, p.getCurvilineCoordinate());
			assertEpsilonEquals(0., p.getLateralDistance());
		}
	}

	@DisplayName("getSegmentChain")
	@Nested
	public class GetSegmentChainTest {

		@DisplayName("Forward + Backward")
		@Nested
		public class ForwardBackwardTest {

			@DisplayName("#1")
			@Test
		    public void testGetSegmentChainForwardBackward_1() {
		    	var chain = segment1.getSegmentChain(true,true);
		    	assertEquals(4, chain.size());
		    	assertEquals(segment3, chain.get(0));
		    	assertEquals(segment4, chain.get(1));
		    	assertEquals(segment1, chain.get(2));
		    	assertEquals(segment2, chain.get(3));
			}

			@DisplayName("#2")
			@Test
		    public void testGetSegmentChainForwardBackward_2() {
		    	var chain = segment2.getSegmentChain(true,true);
		    	assertEquals(4, chain.size());
		    	assertEquals(segment2, chain.get(0));
		    	assertEquals(segment1, chain.get(1));
		    	assertEquals(segment4, chain.get(2));
		    	assertEquals(segment3, chain.get(3));
			}

			@DisplayName("#3")
			@Test
		    public void testGetSegmentChainForwardBackward_3() {
				var chain = segment3.getSegmentChain(true,true);
		    	assertEquals(4, chain.size());
		    	assertEquals(segment3, chain.get(0));
		    	assertEquals(segment4, chain.get(1));
		    	assertEquals(segment1, chain.get(2));
		    	assertEquals(segment2, chain.get(3));
			}

			@DisplayName("#4")
			@Test
		    public void testGetSegmentChainForwardBackward_4() {
				var chain = segment4.getSegmentChain(true,true);
		    	assertEquals(4, chain.size());
		    	assertEquals(segment3, chain.get(0));
		    	assertEquals(segment4, chain.get(1));
		    	assertEquals(segment1, chain.get(2));
		    	assertEquals(segment2, chain.get(3));
			}

			@DisplayName("#5")
			@Test
		    public void testGetSegmentChainForwardBackward_5() {
				var chain = segment5.getSegmentChain(true,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment5, chain.get(0));
			}

			@DisplayName("#6")
			@Test
		    public void testGetSegmentChainForwardBackward_6() {
				var chain = segment6.getSegmentChain(true,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment6, chain.get(0));
			}

			@DisplayName("#7")
			@Test
		    public void testGetSegmentChainForwardBackward_7() {
				var chain = segment7.getSegmentChain(true,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment7, chain.get(0));
			}

			@DisplayName("#8")
			@Test
		    public void testGetSegmentChainForwardBackward_8() {
				var chain = segment8.getSegmentChain(true,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment8, chain.get(0));
			}

			@DisplayName("#9")
			@Test
		    public void testGetSegmentChainForwardBackward_9() {
				var chain = segment9.getSegmentChain(true,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment9, chain.get(0));
		    }
		}

		@DisplayName("Forward")
		@Nested
		public class ForwardTest {

			@DisplayName("#1")
			@Test
		    public void testGetSegmentChainForward_1() {
				var chain = segment1.getSegmentChain(true,false);
		    	assertEquals(2, chain.size());
		    	assertEquals(segment1, chain.get(0));
		    	assertEquals(segment2, chain.get(1));
			}

			@DisplayName("#2")
			@Test
		    public void testGetSegmentChainForward_2() {
				var chain = segment2.getSegmentChain(true,false);
		    	assertEquals(4, chain.size());
		    	assertEquals(segment2, chain.get(0));
		    	assertEquals(segment1, chain.get(1));
		    	assertEquals(segment4, chain.get(2));
		    	assertEquals(segment3, chain.get(3));
			}

			@DisplayName("#3")
			@Test
		    public void testGetSegmentChainForward_3() {
				var chain = segment3.getSegmentChain(true,false);
		    	assertEquals(4, chain.size());
		    	assertEquals(segment3, chain.get(0));
		    	assertEquals(segment4, chain.get(1));
		    	assertEquals(segment1, chain.get(2));
		    	assertEquals(segment2, chain.get(3));
			}

			@DisplayName("#4")
			@Test
		    public void testGetSegmentChainForward_4() {
				var chain = segment4.getSegmentChain(true,false);
		    	assertEquals(3, chain.size());
		    	assertEquals(segment4, chain.get(0));
		    	assertEquals(segment1, chain.get(1));
		    	assertEquals(segment2, chain.get(2));
			}

			@DisplayName("#5")
			@Test
		    public void testGetSegmentChainForward_5() {
				var chain = segment5.getSegmentChain(true,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment5, chain.get(0));
			}

			@DisplayName("#6")
			@Test
		    public void testGetSegmentChainForward_6() {
				var chain = segment6.getSegmentChain(true,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment6, chain.get(0));
			}

			@DisplayName("#7")
			@Test
		    public void testGetSegmentChainForward_7() {
				var chain = segment7.getSegmentChain(true,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment7, chain.get(0));
			}

			@DisplayName("#8")
			@Test
		    public void testGetSegmentChainForward_8() {
				var chain = segment8.getSegmentChain(true,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment8, chain.get(0));
			}

			@DisplayName("#9")
			@Test
		    public void testGetSegmentChainForward_9() {
				var chain = segment9.getSegmentChain(true,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment9, chain.get(0));
		    }
		}
	
		@DisplayName("Backward")
		@Nested
		public class BackwardTest {

			@DisplayName("#1")
			@Test
		    public void testGetSegmentChainBackward_1() {
				var chain = segment1.getSegmentChain(false,true);
		    	assertEquals(3, chain.size());
		    	assertEquals(segment3, chain.get(0));
		    	assertEquals(segment4, chain.get(1));
		    	assertEquals(segment1, chain.get(2));
			}

			@DisplayName("#2")
			@Test
		    public void testGetSegmentChainBackward_2() {
				var chain = segment2.getSegmentChain(false,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment2, chain.get(0));
			}

			@DisplayName("#3")
			@Test
		    public void testGetSegmentChainBackward_3() {
				var chain = segment3.getSegmentChain(false,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment3, chain.get(0));
			}

			@DisplayName("#4")
			@Test
		    public void testGetSegmentChainBackward_4() {
				var chain = segment4.getSegmentChain(false,true);
		    	assertEquals(2, chain.size());
		    	assertEquals(segment3, chain.get(0));
		    	assertEquals(segment4, chain.get(1));
			}

			@DisplayName("#5")
			@Test
		    public void testGetSegmentChainBackward_5() {
				var chain = segment5.getSegmentChain(false,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment5, chain.get(0));
			}

			@DisplayName("#6")
			@Test
		    public void testGetSegmentChainBackward_6() {
				var chain = segment6.getSegmentChain(false,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment6, chain.get(0));
			}

			@DisplayName("#7")
			@Test
		    public void testGetSegmentChainBackward_7() {
		    	var chain = segment7.getSegmentChain(false,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment7, chain.get(0));
			}

			@DisplayName("#8")
			@Test
		    public void testGetSegmentChainBackward_8() {
				var chain = segment8.getSegmentChain(false,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment8, chain.get(0));
			}

			@DisplayName("#9")
			@Test
		    public void testGetSegmentChainBackward_9() {
				var chain = segment9.getSegmentChain(false,true);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment9, chain.get(0));
		    }
		}
	
		@DisplayName("No search")
		@Nested
		public class NoSearchTest {

			@DisplayName("#1")
			@Test
		    public void testGetSegmentChainNoForwardNoBackward_1() {
				var chain = segment1.getSegmentChain(false,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment1, chain.get(0));
			}

			@DisplayName("#2")
			@Test
		    public void testGetSegmentChainNoForwardNoBackward_2() {
				var chain = segment2.getSegmentChain(false,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment2, chain.get(0));
			}

			@DisplayName("#3")
			@Test
		    public void testGetSegmentChainNoForwardNoBackward_3() {
				var chain = segment3.getSegmentChain(false,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment3, chain.get(0));
			}

			@DisplayName("#4")
			@Test
		    public void testGetSegmentChainNoForwardNoBackward_4() {
				var chain = segment4.getSegmentChain(false,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment4, chain.get(0));
			}

			@DisplayName("#5")
			@Test
		    public void testGetSegmentChainNoForwardNoBackward_5() {
				var chain = segment5.getSegmentChain(false,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment5, chain.get(0));
			}

			@DisplayName("#6")
			@Test
		    public void testGetSegmentChainNoForwardNoBackward_6() {
				var chain = segment6.getSegmentChain(false,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment6, chain.get(0));
			}

			@DisplayName("#7")
			@Test
		    public void testGetSegmentChainNoForwardNoBackward_7() {
				var chain = segment7.getSegmentChain(false,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment7, chain.get(0));
			}

			@DisplayName("#8")
			@Test
		    public void testGetSegmentChainNoForwardNoBackward_8() {
				var chain = segment8.getSegmentChain(false,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment8, chain.get(0));
			}

			@DisplayName("#9")
			@Test
		    public void testGetSegmentChainNoForwardNoBackward_9() {
				var chain = segment9.getSegmentChain(false,false);
		    	assertEquals(1, chain.size());
		    	assertEquals(segment9, chain.get(0));
		    }
		}
	}

	@DisplayName("getRoadSegment")
	@Nested
	public class GetRoadSegmentTest {

		@DisplayName("(GeoId) #1")
		@Test
		public void testGetRoadSegmentString_1() {
			assertSame(segment1, network.getRoadSegment(segment1.getGeoId()));
		}

		@DisplayName("(GeoId) #2")
		@Test
		public void testGetRoadSegmentString_2() {
			assertSame(segment9, network.getRoadSegment(segment9.getGeoId()));
		}

		@DisplayName("(GeoId) #3")
		@Test
		public void testGetRoadSegmentString_3() {
			assertSame(segment6, network.getRoadSegment(segment6.getGeoId()));
		}
	
		@DisplayName("(GeoLocation) #1")
		@Test
		public void testGetRoadSegmentGeoLocation_1() {
			assertSame(segment1, network.getRoadSegment(segment1.getGeoLocation()));
		}
		
		@DisplayName("(GeoLocation) #2")
		@Test
		public void testGetRoadSegmentGeoLocation_2() {
			assertSame(segment9, network.getRoadSegment(segment9.getGeoLocation()));
		}
		
		@DisplayName("(GeoLocation) #3")
		@Test
		public void testGetRoadSegmentGeoLocation_3() {
			assertSame(segment6, network.getRoadSegment(segment6.getGeoLocation()));
		}
	}

	@DisplayName("isLeftSidedTrafficDirection")
	@Nested
	public class IsLeftSidedTrafficDirectionTest {

		@DisplayName("#1")
		@Test
		public void testIsLeftSidedCirculationDirection() {
			assertFalse(network.isLeftSidedTrafficDirection());
		}
	}

	@DisplayName("isRightSidedTrafficDirection")
	@Nested
	public class IsRightSidedTrafficDirectionTest {

		@DisplayName("#1")
		@Test
		public void testIsRightSidedCirculationDirection() {
			assertTrue(network.isRightSidedTrafficDirection());
		}
	}
}
