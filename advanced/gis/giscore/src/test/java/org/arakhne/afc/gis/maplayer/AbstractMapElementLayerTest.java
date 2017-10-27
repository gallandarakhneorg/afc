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
import org.arakhne.afc.gis.mapelement.MapPolylineStub;
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
public abstract class AbstractMapElementLayerTest<L extends MapElementLayer<MapPolylineStub>> extends AbstractMapLayerTest<L> {

	/**
	 * @param elements
	 * @return the bounds of the elements
	 */
	protected static Rectangle2d computeBounds(Collection<MapPolylineStub> elements) {
		Rectangle2d expectedBounds = new Rectangle2d();

		for (MapPolylineStub element : elements) {
			Rectangle2d elementBounds = element.getOriginalBounds().toBoundingBox();
			assertNotNull(elementBounds);
			expectedBounds.setUnion(elementBounds);
		}

		return expectedBounds.isEmpty() ? null : expectedBounds;
	}

	private static final int TEST_COUNT = 500;
	private static final int UPDATE_COUNT = 50;

	private Rectangle2d expectedBounds = null;
	private final ArrayList<MapPolylineStub> children = new ArrayList<>();
	private final Map<String, Object> expectedAttributes = new TreeMap<>();
	private final Map<String, AttributeType> expectedAttributeTypes = new TreeMap<>();

	@Before
	public void setUp() throws Exception {
		int elementCount = getRandom().nextInt(20)+5;
		this.expectedBounds = new Rectangle2d();
		this.children.clear();

		for(int i=0; i<elementCount; ++i) {
			MapPolylineStub element = new MapPolylineStub();
			element.setName("ELT-"+i); //$NON-NLS-1$

			insertInExpectedChildren(this.children, element);

			Rectangle2d elementBounds = element.getOriginalBounds().toBoundingBox();
			assertNotNull(elementBounds);
			this.expectedBounds.setUnion(elementBounds);
		}

		setUp(this.expectedBounds);
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
	public void setUp(Rectangle2d bounds) throws Exception {
		super.setUp(bounds);

		for(int i=0; i<this.children.size(); ++i) {
			getLayer().addMapElement(this.children.get(i));
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
	 * @return the expected count of children.
	 */
	protected final int getExpectedChildCount() {
		assertTrue(this.children.size()>0);
		return this.children.size();
	}

	/**
	 * @return the expected children.
	 */
	protected final List<MapPolylineStub> getExpectedChildren() {
		assertTrue(this.children.size()>0);
		return Collections.unmodifiableList(this.children);
	}

	/**
	 * @param index
	 * @return the expected child.
	 */
	protected final MapPolylineStub getExpectedChildAt(int index) {
		MapPolylineStub elt = this.children.get(index);
		assertNotNull(elt);
		return elt;
	}

	@Override
	public void testGetBounds() {
		L layer = getLayer();

		Shape2d<?> bounds = layer.getBoundingBox();

		Shape2d<?> expected = getExpectedChildBounds();

		assertNotNull(bounds);
		assertEpsilonEquals(expected, bounds);
	}

	@Test
	public void testSize() {
		L layer = getLayer();

		int childCount = getExpectedChildCount();

		assertEpsilonEquals(childCount, layer.size());
	}

	@Test
	public void testGetMapElementAt() {
		L layer = getLayer();

		Random rnd = new Random();

		for(int i=0; i<TEST_COUNT; ++i) {
			int index = rnd.nextInt(layer.size());
			MapPolylineStub elt = getExpectedChildAt(index);
			assertSame("#"+i+"|"+elt, elt, layer.getMapElementAt(index)); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Test
	public void testGetAllMapElements() {
		L layer = getLayer();

		Collection<MapPolylineStub> allElements = layer.getAllMapElements();

		assertEpsilonEquals(getExpectedChildCount(),allElements.size());
		assertEpsilonEquals(getExpectedChildBounds(),layer.getBoundingBox());

		assertEpsilonEquals(getExpectedChildren(), allElements);
	}

	@Test
	public void testAddMapElement() {
		L layer = getLayer();
		ArrayList<MapPolylineStub> allElements = new ArrayList<>(getExpectedChildren());

		for(int j=0; j<UPDATE_COUNT; ++j) {
			MapPolylineStub newElement = new MapPolylineStub();
			layer.addMapElement(newElement);

			allElements.add(newElement);

			assertEpsilonEquals(allElements.size(),layer.size());
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

			assertEpsilonEquals(allElements, layer.getAllMapElements());
		}
	}

	@Test
	public void testRemoveMapElement() {
		L layer = getLayer();
		Random rnd = new Random();
		List<MapPolylineStub> allElements = new ArrayList<>(getExpectedChildren());

		for(int j=0; (j<UPDATE_COUNT)&&(!allElements.isEmpty()); ++j) {
			int idx = rnd.nextInt(allElements.size());

			layer.removeMapElement(allElements.get(idx));

			allElements.remove(idx);

			assertEpsilonEquals(allElements.size(),layer.size());
			assertEpsilonEquals(computeBounds(allElements),layer.getBoundingBox());

			for(int i=0; i<allElements.size(); ++i) {
				MapPolylineStub elt = allElements.get(i);
				assertEpsilonEquals(elt, layer.getMapElementAt(i));
			}
		}
	}

	@Test
	public void testIterator() {
		L layer = getLayer();
		int i;
		List<MapPolylineStub> allElements = new ArrayList<>(getExpectedChildren());

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

		for(int j=0; j<UPDATE_COUNT; ++j) {
			MapPolylineStub newElement = new MapPolylineStub();
			layer.addMapElement(newElement);
			insertInExpectedChildren(allElements, newElement);
			iterator = layer.iterator();
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

	@Test
	@Override
	public void testClone() throws Exception {
		L layer = getLayer();

		MapLayer clone = layer.clone();
		assertNotNull(clone);
		assertEquals(layer.getClass(), clone.getClass());
		assertNotEquals(layer, clone);
		assertNotEquals(layer.hashCode(), clone.hashCode());
		assertTrue(clone instanceof MapElementLayer<?>);

		assertEpsilonEquals(this.expectedAttributes.size(), clone.getAttributeCount());
		for(Entry<String, Object> expectedAttr : this.expectedAttributes.entrySet()) {
			AttributeValue val = clone.getAttribute(expectedAttr.getKey());
			assertNotNull("name="+expectedAttr.getKey(), val); //$NON-NLS-1$
			assertEquals("name="+expectedAttr.getKey(), this.expectedAttributeTypes.get(expectedAttr.getKey()), val.getType()); //$NON-NLS-1$
			AttributeValue v = new AttributeValueImpl(val.getType(), expectedAttr.getValue());
			assertEquals("name="+expectedAttr.getKey(), v, val); //$NON-NLS-1$
		}

		LinkedList<MapPolylineStub> childList = new LinkedList<>();
		childList.addAll(this.children);

		MapElementLayer<MapPolylineStub> elementContainer = ((MapElementLayer<MapPolylineStub>)clone);

		for(int i=0; i<elementContainer.getMapElementCount(); ++i) {
			MapPolylineStub child = elementContainer.getMapElementAt(i);
			assertTrue(childList.remove(child));
		}
		assertTrue(childList.isEmpty());
	}

}
