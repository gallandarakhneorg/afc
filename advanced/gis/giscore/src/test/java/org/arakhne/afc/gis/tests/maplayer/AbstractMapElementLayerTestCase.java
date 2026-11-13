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

package org.arakhne.afc.gis.tests.maplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.gis.maplayer.MapElementLayer;
import org.arakhne.afc.gis.maplayer.MapLayer;
import org.arakhne.afc.gis.tests.mapelement.MapPolylineStub;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Abstract Unit tests for MapElement layers.
 *
 * @param <L>
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public abstract class AbstractMapElementLayerTestCase<L extends MapElementLayer<MapPolylineStub>> extends AbstractMapLayerTestCase<L> {

	/**
	 * @param elements
	 * @return the bounds of the elements
	 */
	protected static Rectangle2d computeBounds(Collection<MapPolylineStub> elements) {
		Rectangle2d expectedBounds = new Rectangle2d();
		boolean first = true;
		for (MapPolylineStub element : elements) {
			Rectangle2d elementBounds = element.getOriginalBounds().toBoundingBox();
			assertNotNull(elementBounds);
			if (first) {
				first = false;
				expectedBounds.set(elementBounds);
			} else {
				expectedBounds.setUnion(elementBounds);
			}
		}
		return first ? null : expectedBounds;
	}

	private static final int TEST_COUNT = 500;
	private static final int UPDATE_COUNT = 50;

	private Rectangle2d expectedBounds = null;
	private final ArrayList<MapPolylineStub> children = new ArrayList<>();
	private final Map<String, Object> expectedAttributes = new TreeMap<>();
	private final Map<String, AttributeType> expectedAttributeTypes = new TreeMap<>();

	@BeforeEach
	public void setUp() throws Exception {
		int elementCount = getRandom().nextInt(20)+5;
		expectedBounds = new Rectangle2d();
		boolean first = true;
		children.clear();

		for(int i=0; i<elementCount; ++i) {
			MapPolylineStub element = new MapPolylineStub();
			element.setName("ELT-"+i); //$NON-NLS-1$

			insertInExpectedChildren(children, element);

			Rectangle2d elementBounds = element.getOriginalBounds().toBoundingBox();
			assertNotNull(elementBounds);
			if (first) {
				first = false;
				expectedBounds.set(elementBounds);
			} else {
				expectedBounds.setUnion(elementBounds);
			}
		}

		setUpTest(expectedBounds);
	}

	/** Insert the element in the list of the expected children.
	 *
	 * @param list
	 * @param element
	 */
	protected void insertInExpectedChildren(List<MapPolylineStub> list, MapPolylineStub element) {
		list.add(element);
	}

	@Override
	public void setUpTest(Rectangle2d bounds) throws Exception {
		super.setUpTest(bounds);

		for(int i=0; i<children.size(); ++i) {
			getLayer().addMapElement(children.get(i));
		}

		int nbAttributes = getRandom().nextInt(200)+20;
		for(int i=0; i<nbAttributes; ++i) {
			Object o = null;
			AttributeType t = randomEnum(AttributeType.class);
			switch(t) {
			case BOOLEAN:
				o = getRandom().nextBoolean();
				break;
			case DATE:
				o = new Date(getRandom().nextLong());
				break;
			case ENUMERATION:
				o = randomEnum(AttributeType.class);
				break;
			case INET_ADDRESS:
				o = InetAddress.getByAddress(new byte[] {
					(byte)getRandom().nextInt(256),
					(byte)getRandom().nextInt(256),
					(byte)getRandom().nextInt(256),
					(byte)getRandom().nextInt(256)
				});
				break;
			case INTEGER:
				o = getRandom().nextInt();
				break;
			case OBJECT:
				o = new Object();
				break;
			case POINT:
				o = randomPoint2D();
				break;
			case POINT3D:
				o = null;
				break;
			case POLYLINE:
				o = randomPoints2D();
				break;
			case POLYLINE3D:
				o = null;
				break;
			case REAL:
				o = getRandom().nextDouble();
				break;
			case STRING:
				o = UUID.randomUUID().toString();
				break;
			case TIMESTAMP:
				o = getRandom().nextLong();
				break;
			case TYPE:
				o = AttributeType.class;
				break;
			case URI:
				o = new URI("mailto:noone@utbm.fr"); //$NON-NLS-1$
				break;
			case URL:
				o = new URL("http://set.utbm.fr"); //$NON-NLS-1$
				break;
			case UUID:
				o = UUID.randomUUID();
				break;
			default:
			}
			if (o!=null) {
				String name = UUID.randomUUID().toString();
				expectedAttributes.put(name, o);
				expectedAttributeTypes.put(name, t);
				AttributeValue val = new AttributeValueImpl(t, o);
				getLayer().setAttribute(name, val);
			}
		}
	}

	@Override
	protected final Rectangle2d getExpectedChildBounds() {
		assertNotNull(expectedBounds);
		return expectedBounds;
	}

	/**
	 * @return the expected count of children.
	 */
	protected final int getExpectedChildCount() {
		assertTrue(children.size()>0);
		return children.size();
	}

	/**
	 * @return the expected children.
	 */
	protected final List<MapPolylineStub> getExpectedChildren() {
		assertTrue(children.size()>0);
		return Collections.unmodifiableList(children);
	}

	/**
	 * @param index
	 * @return the expected child.
	 */
	protected final MapPolylineStub getExpectedChildAt(int index) {
		MapPolylineStub elt = children.get(index);
		assertNotNull(elt);
		return elt;
	}

	@DisplayName("getBoundingBox")
	@Nested
	public class GetBoundingBox {

		private L layer;
		private Shape2d<?> bounds;
		private Shape2d<?> expected;

		@BeforeEach
		public void setUp() {
			layer = getLayer();
			bounds = layer.getBoundingBox();
			expected = getExpectedChildBounds();
		}

		@DisplayName("#1")
		@Test
		public void testGetBounds_assertion1() {
			assertNotNull(bounds);
		}

		@DisplayName("#2")
		@Test
		public void testGetBounds_assertion2() {
			assertEpsilonEquals(expected, bounds);
		}
	}

	@DisplayName("size")
	@Nested
	public class Size {

		private L layer;
		private int childCount;

		@BeforeEach
		public void setUp() {
			layer = getLayer();
			childCount = getExpectedChildCount();
		}

		@DisplayName("#1")
		@Test
		public void testSize_assertion1() {
			assertEpsilonEquals(childCount, layer.size());
		}
	}

	@DisplayName("getMapElementAt")
	@Nested
	public class GetMapElementAt {

		private L layer;
		private Random rnd;

		@BeforeEach
		public void setUp() {
			layer = getLayer();
			rnd = new Random();
		}

		@DisplayName("loop iteration")
		@Test
		public void testGetMapElementAt() {
			for(int i=0; i<TEST_COUNT; ++i) {
				int index = rnd.nextInt(layer.size());
				MapPolylineStub elt = getExpectedChildAt(index);
				assertSame(elt, layer.getMapElementAt(index), "#"+i+"|"+elt); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	@DisplayName("getAllMapElements")
	@Nested
	public class GetAllMapElements {

		private L layer;
		private Collection<MapPolylineStub> allElements;

		@BeforeEach
		public void setUp() {
			layer = getLayer();
			allElements = layer.getAllMapElements();
		}

		@DisplayName("#1")
		@Test
		public void testGetAllMapElements_assertion1() {
			assertEpsilonEquals(getExpectedChildCount(),allElements.size());
		}

		@DisplayName("#2")
		@Test
		public void testGetAllMapElements_assertion2() {
			assertEpsilonEquals(getExpectedChildBounds(),layer.getBoundingBox());
		}

		@DisplayName("#3")
		@Test
		public void testGetAllMapElements_assertion3() {
			assertEpsilonEquals(getExpectedChildren(), allElements);
		}
	}

	@DisplayName("addMapElement")
	@Nested
	public class AddMapElement {

		private L layer;
		private ArrayList<MapPolylineStub> allElements;

		@BeforeEach
		public void setUp() {
			layer = getLayer();
			allElements = new ArrayList<>(getExpectedChildren());
		}

		@DisplayName("loop iteration")
		@Test
		public void testAddMapElement() {
			for(int j=0; j<UPDATE_COUNT; ++j) {
				MapPolylineStub newElement = new MapPolylineStub();
				layer.addMapElement(newElement);

				allElements.add(newElement);

				assertEpsilonEquals(allElements.size(),layer.size());
				assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

				assertEpsilonEquals(allElements, layer.getAllMapElements());
			}
		}
	}

	@DisplayName("removeMapElement")
	@Nested
	public class RemoveMapElement {

		private L layer;
		private Random rnd;
		private List<MapPolylineStub> allElements;

		@BeforeEach
		public void setUp() {
			layer = getLayer();
			rnd = new Random();
			allElements = new ArrayList<>(getExpectedChildren());
		}

		@DisplayName("loop iteration")
		@Test
		public void testRemoveMapElement() {
			for(int j=0; (j<UPDATE_COUNT)&&(!allElements.isEmpty()); ++j) {
				int idx = rnd.nextInt(allElements.size());

				layer.removeMapElement(allElements.get(idx));

				allElements.remove(idx);

				assertEpsilonEquals(allElements.size(),layer.size());
				Rectangle2d expectedBounds = computeBounds(allElements);
				assertEpsilonEquals(expectedBounds, layer.getBoundingBox());

				for(int i=0; i<allElements.size(); ++i) {
					MapPolylineStub elt = allElements.get(i);
					assertEpsilonEquals(elt, layer.getMapElementAt(i));
				}
			}
		}
	}

	@DisplayName("iterator")
	@Nested
	public class IteratorTest {

		private L layer;
		private List<MapPolylineStub> allElements;

		@BeforeEach
		public void setUp() {
			layer = getLayer();
			allElements = new ArrayList<>(getExpectedChildren());
		}

		@DisplayName("#1")
		@Test
		public void testIterator_assertion1() {
			int i;
			Iterator<MapPolylineStub> iterator = layer.iterator();
			i = 0;
			while (iterator.hasNext()) {
				MapPolylineStub elt = iterator.next();
				assertNotNull(elt);
				MapPolylineStub refElt = allElements.get(i++);
				assertEpsilonEquals(refElt, elt);
			}
			assertEpsilonEquals(allElements.size(),i);
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());
		}

		@DisplayName("#2")
		@Test
		public void testIterator_assertion2() {
			int i;
			for(int j=0; j<UPDATE_COUNT; ++j) {
				MapPolylineStub newElement = new MapPolylineStub();
				layer.addMapElement(newElement);
				insertInExpectedChildren(allElements, newElement);
				Iterator<MapPolylineStub> iterator = layer.iterator();
				i = 0;
				while (iterator.hasNext()) {
					MapPolylineStub elt = iterator.next();
					assertNotNull(elt);
					MapPolylineStub refElt = allElements.get(i++);
					assertEpsilonEquals(refElt, elt);
				}
				assertEpsilonEquals(allElements.size(),i);
				assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());
			}
		}
	}

	@DisplayName("clone")
	@Nested
	public class CloneTest {

		private L layer;
		private MapLayer clone;
		private LinkedList<MapPolylineStub> childList;
		private MapElementLayer<MapPolylineStub> elementContainer;

		@BeforeEach
		public void setUp() throws Exception {
			layer = getLayer();
			clone = layer.clone();
			childList = new LinkedList<>();
			childList.addAll(children);
			elementContainer = ((MapElementLayer<MapPolylineStub>)clone);
		}

		@DisplayName("#1")
		@Test
		public void testClone_assertion1() {
			assertNotNull(clone);
		}

		@DisplayName("#2")
		@Test
		public void testClone_assertion2() {
			assertEquals(layer.getClass(), clone.getClass());
		}

		@DisplayName("#3")
		@Test
		public void testClone_assertion3() {
			assertNotEquals(layer, clone);
		}

		@DisplayName("#4")
		@Test
		public void testClone_assertion4() {
			assertNotEquals(layer.hashCode(), clone.hashCode());
		}

		@DisplayName("#5")
		@Test
		public void testClone_assertion5() {
			assertTrue(clone instanceof MapElementLayer<?>);
		}

		@DisplayName("#6")
		@Test
		public void testClone_assertion6() {
			assertEpsilonEquals(expectedAttributes.size(), clone.getAttributeCount());
		}

		@DisplayName("#7")
		@Test
		public void testClone_assertion7() {
			for(Entry<String, Object> expectedAttr : expectedAttributes.entrySet()) {
				AttributeValue val = clone.getAttribute(expectedAttr.getKey());
				assertNotNull(val, "name="+expectedAttr.getKey()); //$NON-NLS-1$
				AttributeType expectedType = expectedAttributeTypes.get(expectedAttr.getKey());
				AttributeValue v = new AttributeValueImpl(expectedType, expectedAttr.getValue());
				assertEquals(v, val, "name="+expectedAttr.getKey()); //$NON-NLS-1$
			}
		}

		@DisplayName("#8")
		@Test
		public void testClone_assertion8() {
			for(int i=0; i<elementContainer.getMapElementCount(); ++i) {
				MapPolylineStub child = elementContainer.getMapElementAt(i);
				assertTrue(childList.remove(child));
			}
			assertTrue(childList.isEmpty());
		}
	}
}
