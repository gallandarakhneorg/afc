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

package org.arakhne.afc.gis.maplayer;

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

import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.attr.AttributeValue;
import org.arakhne.afc.attrs.attr.AttributeValueImpl;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;

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
public abstract class AbstractMultiMapLayerTest<L extends MultiMapLayer<MapLayer>> extends AbstractMapLayerTest<L> {

	private static final int TEST_COUNT = 500;
	private static final int UPDATE_COUNT = 50;

	/**
	 * @param elements
	 * @return the merged rectangle
	 */
	protected static Rectangle2d computeBounds(Collection<MapLayerStub> elements) {
		Rectangle2d expectedBounds = new Rectangle2d();
		boolean first = true;
		for (MapLayerStub element : elements) {
			Shape2d<?> elementBounds = element.getOriginalBounds();
			assertNotNull(elementBounds);
			if (first) {
				first = false;
				expectedBounds.set(elementBounds.toBoundingBox());
			} else {
				expectedBounds.setUnion(elementBounds.toBoundingBox());
			}
		}

		return first ? null : expectedBounds;
	}

	private Rectangle2d expectedBounds = null;
	private final ArrayList<MapLayerStub> children = new ArrayList<>();
	private final Map<String, Object> expectedAttributes = new TreeMap<>();
	private final Map<String, AttributeType> expectedAttributeTypes = new TreeMap<>();

	@Before
	public void setUp() throws Exception {
		int elementCount = 5;//(int)(Math.random() * 20)+1;
		this.expectedBounds = new Rectangle2d();
		boolean first = true;
		this.children.clear();

		for(int i=0; i<elementCount; ++i) {
			MapLayerStub element = new MapLayerStub();

			this.children.add(0, element);

			Shape2d<?> elementBounds = element.getOriginalBounds();
			assertNotNull(elementBounds);
			if (first) {
				first = false;
				this.expectedBounds.set(elementBounds.toBoundingBox());
			} else {
				this.expectedBounds.setUnion(elementBounds.toBoundingBox());
			}
		}

		setUpTest(this.expectedBounds);
	}

	@Override
	public void setUpTest(Rectangle2d bounds) throws Exception {
		super.setUpTest(bounds);

		for(int i=this.children.size()-1; i>=0; --i) {
			getLayer().addMapLayer(this.children.get(i));
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
				o = randomPoint3D();
				break;
			case POLYLINE:
				o = randomPoints2D();
				break;
			case POLYLINE3D:
				o = randomPoints3D();
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
				this.expectedAttributes.put(name, o);
				this.expectedAttributeTypes.put(name, t);
				AttributeValue val = new AttributeValueImpl(t, o);
				getLayer().setAttribute(name, val);
			}
		}
	}

	@Override
	protected final Rectangle2d getExpectedChildBounds() {
		assertNotNull(this.expectedBounds);
		return this.expectedBounds;
	}

	/**
	 * @return count of expected children
	 */
	protected final int getExpectedChildCount() {
		assertTrue(this.children.size()>0);
		return this.children.size();
	}

	/**
	 * @return the expected children
	 */
	protected final List<MapLayerStub> getExpectedChildren() {
		assertTrue(this.children.size()>0);
		return Collections.unmodifiableList(this.children);
	}

	/**
	 * @param index
	 * @return expected child
	 */
	protected final MapLayerStub getExpectedChildAt(int index) {
		MapLayerStub elt = this.children.get(index);
		assertNotNull(elt);
		return elt;
	}

	@Test
	public void testSize() {
		L layer = getLayer();

		int childCount = getExpectedChildCount();

		assertEpsilonEquals(childCount, layer.size());
	}

	@Test
	@Override
	public void testGetBounds() {
		L layer = getLayer();

		Shape2d<?> bounds = layer.getBoundingBox();

		Shape2d<?> expected = getExpectedChildBounds();

		assertNotNull(bounds);
		assertEpsilonEquals(expected, bounds);
	}

	@Test
	public void testGetAllMapLayers() {
		L layer = getLayer();

		List<MapLayer> allElements = layer.getAllMapLayers();

		assertEpsilonEquals(getExpectedChildCount(),allElements.size());
		assertEpsilonEquals(getExpectedChildBounds(),layer.getBoundingBox());

		for(int i=0; i<allElements.size(); ++i) {
			MapLayerStub elt = getExpectedChildAt(i);
			assertEpsilonEquals(elt, allElements.get(i));
		}
	}

	@Test
	public void testGetMapLayerAt() {
		L layer = getLayer();

		Random rnd = new Random();

		for(int i=0; i<TEST_COUNT; ++i) {
			int index = rnd.nextInt(layer.size());
			MapLayerStub elt = getExpectedChildAt(index);
			assertEpsilonEquals(elt, layer.getMapLayerAt(index));
		}
	}

	@Test
	public void testIndexOf() {
		L layer = getLayer();

		Random rnd = new Random();

		for(int i=0; i<TEST_COUNT; ++i) {
			int index = rnd.nextInt(layer.size());
			MapLayerStub elt = getExpectedChildAt(index);
			assertEpsilonEquals(index, layer.indexOf(elt));
		}
	}

	@Test
	public void testClear() {
		L layer = getLayer();

		layer.clear();

		assertEpsilonEquals(0, layer.size());
	}

	@Test
	public void testAddMapLayerMapLayer() {
		L layer = getLayer();
		ArrayList<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());

		for(int j=0; j<UPDATE_COUNT; ++j) {
			MapLayerStub newElement = new MapLayerStub();
			layer.addMapLayer(newElement);

			allElements.add(0, newElement);

			assertEpsilonEquals(allElements.size(),layer.size());
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

			for(int i=0; i<allElements.size(); ++i) {
				MapLayerStub elt = allElements.get(i);
				assertEpsilonEquals(elt, layer.getMapLayerAt(i));
			}
		}
	}

	@Test
	public void testAddMapLayerIntMapLayer() {
		L layer = getLayer();
		ArrayList<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());
		Random rnd = new Random();

		for(int j=0; j<UPDATE_COUNT; ++j) {
			MapLayerStub newElement = new MapLayerStub();
			int insertionIndex = rnd.nextInt(allElements.size());

			layer.addMapLayer(insertionIndex, newElement);

			allElements.add(insertionIndex,newElement);

			assertEpsilonEquals(allElements.size(),layer.size());
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

			for(int i=0; i<allElements.size(); ++i) {
				MapLayerStub elt = allElements.get(i);
				assertEpsilonEquals(elt, layer.getMapLayerAt(i));
			}
		}
	}

	@Test
	public void testRemoveMapLayer() {
		L layer = getLayer();
		Random rnd = new Random();
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());

		for(int j=0; (j<UPDATE_COUNT)&&(!allElements.isEmpty()); ++j) {
			int idx = rnd.nextInt(allElements.size());

			layer.removeMapLayer(allElements.get(idx));

			allElements.remove(idx);

			assertEpsilonEquals(allElements.size(),layer.size());
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

			for(int i=0; i<allElements.size(); ++i) {
				MapLayerStub elt = allElements.get(i);
				assertEpsilonEquals(elt, layer.getMapLayerAt(i));
			}
		}
	}

	@Test
	public void testRemoveMapLayerAt() {
		L layer = getLayer();
		Random rnd = new Random();
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());

		for(int j=0; (j<UPDATE_COUNT)&&(!allElements.isEmpty()); ++j) {
			int idx = rnd.nextInt(allElements.size());

			layer.removeMapLayerAt(idx);

			allElements.remove(idx);

			assertEpsilonEquals(allElements.size(),layer.size());
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

			for(int i=0; i<allElements.size(); ++i) {
				MapLayerStub elt = allElements.get(i);
				assertEpsilonEquals(elt, layer.getMapLayerAt(i));
			}
		}
	}

	@Test
	public void testMoveLayerUpMapLayer() {
		L layer = getLayer();
		Random rnd = new Random();
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());
		Shape2d<?> refBounds = computeBounds(allElements);

		for(int j=0; j<TEST_COUNT; ++j) {
			int idx = rnd.nextInt(allElements.size());

			MapLayerStub l1 = allElements.get(idx);
			MapLayerStub l2 = (idx>0) ? allElements.get(idx-1) : null;

			layer.moveLayerUp(allElements.get(idx));

			if (l2!=null) {
				allElements.set(idx-1, l1);
				allElements.set(idx, l2);
			}

			assertEpsilonEquals(allElements.size(),layer.size());
			assertEpsilonEquals(refBounds,layer.getBoundingBox());

			for(int i=0; i<allElements.size(); ++i) {
				MapLayerStub elt = allElements.get(i);
				assertEpsilonEquals(elt, layer.getMapLayerAt(i));
			}
		}
	}

	@Test
	public void testMoveLayerUpInt() {
		L layer = getLayer();
		Random rnd = new Random();
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());
		Rectangle2d refBounds = computeBounds(allElements);

		for(int j=0; j<TEST_COUNT; ++j) {
			int idx = rnd.nextInt(allElements.size());

			MapLayerStub l1 = allElements.get(idx);
			MapLayerStub l2 = (idx>0) ? allElements.get(idx-1) : null;

			layer.moveLayerUp(idx);

			if (l2!=null) {
				allElements.set(idx-1, l1);
				allElements.set(idx, l2);
			}

			assertEpsilonEquals(allElements.size(),layer.size());
			Rectangle2d actualBounds = layer.getBoundingBox().toBoundingBox();
			assertEpsilonEquals(refBounds.getMinX(),actualBounds.getMinX());
			assertEpsilonEquals(refBounds.getMaxX(),actualBounds.getMaxX());
			assertEpsilonEquals(refBounds.getMinY(),actualBounds.getMinY());
			assertEpsilonEquals(refBounds.getMaxY(),actualBounds.getMaxY());

			for(int i=0; i<allElements.size(); ++i) {
				MapLayerStub elt = allElements.get(i);
				assertEpsilonEquals(elt, layer.getMapLayerAt(i));
			}
		}
	}

	@Test
	public void testMoveLayerDownMapLayer() {
		L layer = getLayer();
		Random rnd = new Random();
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());
		Shape2d<?> refBounds = computeBounds(allElements);

		for(int j=0; j<TEST_COUNT; ++j) {
			int idx = rnd.nextInt(allElements.size());

			MapLayerStub l1 = allElements.get(idx);
			MapLayerStub l2 = (idx<allElements.size()-1) ? allElements.get(idx+1) : null;

			layer.moveLayerDown(allElements.get(idx));

			if (l2!=null) {
				allElements.set(idx, l2);
				allElements.set(idx+1, l1);
			}

			assertEpsilonEquals(allElements.size(),layer.size());
			assertEpsilonEquals(refBounds,layer.getBoundingBox());

			for(int i=0; i<allElements.size(); ++i) {
				MapLayerStub elt = allElements.get(i);
				assertEpsilonEquals(elt, layer.getMapLayerAt(i));
			}
		}
	}

	@Test
	public void testMoveLayerDownInt() {
		L layer = getLayer();
		Random rnd = new Random();
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());
		Shape2d<?> refBounds = computeBounds(allElements);

		for(int j=0; j<TEST_COUNT; ++j) {
			int idx = rnd.nextInt(allElements.size());

			MapLayerStub l1 = allElements.get(idx);
			MapLayerStub l2 = (idx<allElements.size()-1) ? allElements.get(idx+1) : null;

			layer.moveLayerDown(idx);

			if (l2!=null) {
				allElements.set(idx, l2);
				allElements.set(idx+1, l1);
			}

			assertEpsilonEquals(allElements.size(),layer.size());
			assertEpsilonEquals(refBounds,layer.getBoundingBox());

			for(int i=0; i<allElements.size(); ++i) {
				MapLayerStub elt = allElements.get(i);
				assertEpsilonEquals(elt, layer.getMapLayerAt(i));
			}
		}
	}

	@Test
	public void testIterator() {
		L layer = getLayer();
		int i;
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());

		Iterator<MapLayer> iterator = layer.iterator();
		i = 0;
		while (iterator.hasNext()) {
			MapLayer elt = iterator.next();
			assertNotNull(elt);
			MapLayerStub refElt = allElements.get(i++);
			assertEpsilonEquals(refElt, elt);
		}
		assertEpsilonEquals(allElements.size(),i);
		assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

		for(int j=0; j<UPDATE_COUNT; ++j) {
			MapLayerStub newElement = new MapLayerStub();
			layer.addMapLayer(newElement);
			allElements.add(0, newElement);
			iterator = layer.iterator();
			i = 0;
			while (iterator.hasNext()) {
				MapLayer elt = iterator.next();
				assertNotNull(elt);
				MapLayerStub refElt = allElements.get(i++);
				assertEpsilonEquals(refElt, elt);
			}
			assertEpsilonEquals(allElements.size(),i);
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());
		}
	}

	@Test
	public void testGetTopDownIterator() {
		L layer = getLayer();
		int i;
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());

		Iterator<MapLayer> iterator = layer.getTopDownIterator();
		i = 0;
		while (iterator.hasNext()) {
			MapLayer elt = iterator.next();
			assertNotNull(elt);
			MapLayerStub refElt = allElements.get(i++);
			assertEpsilonEquals(refElt, elt);
		}
		assertEpsilonEquals(allElements.size(),i);
		assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

		for(int j=0; j<UPDATE_COUNT; ++j) {
			MapLayerStub newElement = new MapLayerStub();
			layer.addMapLayer(newElement);
			allElements.add(0, newElement);
			iterator = layer.getTopDownIterator();
			i = 0;
			while (iterator.hasNext()) {
				MapLayer elt = iterator.next();
				assertNotNull(elt);
				MapLayerStub refElt = allElements.get(i++);
				assertEpsilonEquals(refElt, elt);
			}
			assertEpsilonEquals(allElements.size(),i);
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());
		}
	}

	@Test
	public void testReverseIterator() {
		L layer = getLayer();
		int i;
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());

		Iterator<MapLayer> iterator = layer.getBottomUpIterator();
		i = 0;
		while (iterator.hasNext()) {
			MapLayer elt = iterator.next();
			assertNotNull(elt);
			MapLayerStub refElt = allElements.get(allElements.size()-i-1);
			assertEpsilonEquals(refElt, elt);
			++i;
		}
		assertEpsilonEquals(allElements.size(),i);
		assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

		for(int j=0; j<UPDATE_COUNT; ++j) {
			MapLayerStub newElement = new MapLayerStub();
			layer.addMapLayer(newElement);
			allElements.add(0, newElement);
			iterator = layer.getBottomUpIterator();
			i = 0;
			while (iterator.hasNext()) {
				MapLayer elt = iterator.next();
				assertNotNull(elt);
				MapLayerStub refElt = allElements.get(allElements.size()-i-1);
				assertEpsilonEquals(refElt, elt);
				++i;
			}
			assertEpsilonEquals(allElements.size(),i);
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());
		}
	}

	@Test
	public void testGetBottomUpIterator() {
		L layer = getLayer();
		int i;
		List<MapLayerStub> allElements = new ArrayList<>(getExpectedChildren());

		Iterator<MapLayer> iterator = layer.getBottomUpIterator();
		i = 0;
		while (iterator.hasNext()) {
			MapLayer elt = iterator.next();
			assertNotNull(elt);
			MapLayerStub refElt = allElements.get(allElements.size()-i-1);
			assertEpsilonEquals(refElt, elt);
			++i;
		}
		assertEpsilonEquals(allElements.size(),i);
		assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

		for(int j=0; j<UPDATE_COUNT; ++j) {
			MapLayerStub newElement = new MapLayerStub();
			layer.addMapLayer(newElement);
			allElements.add(0, newElement);
			iterator = layer.getBottomUpIterator();
			i = 0;
			while (iterator.hasNext()) {
				MapLayer elt = iterator.next();
				assertNotNull(elt);
				MapLayerStub refElt = allElements.get(allElements.size()-i-1);
				assertEpsilonEquals(refElt, elt);
				++i;
			}
			assertEpsilonEquals(allElements.size(),i);
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());
		}
	}

	@Test
	@Override
	public void testClone() throws Exception {
		L layer = getLayer();

		MapLayer clone = layer.clone();
		assertNotNull(clone);
		assertEquals(layer.getClass(), clone.getClass());
		assertNotEquals(layer, clone);
		assertNotEquals(layer.hashCode(), clone.hashCode());
		assertTrue(clone instanceof MultiMapLayer<?>);

		assertEpsilonEquals(this.expectedAttributes.size(), clone.getAttributeCount());
		for(Entry<String, Object> expectedAttr : this.expectedAttributes.entrySet()) {
			AttributeValue val = clone.getAttribute(expectedAttr.getKey());
			assertNotNull("name="+expectedAttr.getKey(), val); //$NON-NLS-1$
			AttributeType expectedType = this.expectedAttributeTypes.get(expectedAttr.getKey());
			AttributeValue v = new AttributeValueImpl(expectedType, expectedAttr.getValue());
			assertEquals("name="+expectedAttr.getKey(), v, val); //$NON-NLS-1$
		}

		LinkedList<MapLayerStub> childList = new LinkedList<>();
		childList.addAll(this.children);

		MultiMapLayer<MapLayerStub> elementContainer = ((MultiMapLayer<MapLayerStub>)clone);

		for(int i=0; i<elementContainer.getChildCount(); ++i) {
			MapLayerStub child = elementContainer.getChildAt(i);
			Iterator<MapLayerStub> iterator = childList.iterator();
			boolean found = false;
			while (iterator.hasNext()) {
				MapLayerStub stub = iterator.next();
				if (stub.getGeoLocation().equals(child.getGeoLocation())) {
					iterator.remove();
					found = true;
				}
			}
			assertTrue("#"+i, found); //$NON-NLS-1$
		}
		assertTrue(childList.isEmpty());
	}

}
