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

import java.util.Comparator;
import java.util.List;

import org.arakhne.afc.attrs.collection.HeapAttributeCollection;
import org.arakhne.afc.gis.mapelement.MapPolylineStub;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.ListUtil;

/** Unit tests for MapElement layers.
*
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class GridMapElementLayerTest extends AbstractMapElementLayerTest<GridMapElementLayer<MapPolylineStub>> {

	@Override
	protected GridMapElementLayer<MapPolylineStub> createLayer(Rectangle2d bounds) {
		return new GridMapElementLayer<>(new HeapAttributeCollection(), bounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void insertInExpectedChildren(List<MapPolylineStub> list,
			MapPolylineStub element) {
		ListUtil.add(list,
			new Comparator<MapPolylineStub>() {
			@Override
			public int compare(MapPolylineStub o1, MapPolylineStub o2) {
				if (o1==o2) return 0;
				if (o1==null) return Integer.MIN_VALUE;
				if (o2==null) return Integer.MAX_VALUE;
				return o1.getGeoId().compareTo(o2.getGeoId());
			}
		}, element, false, true);
	}

	/**
	 */
	public void testGetRowCount() {
		assertEpsilonEquals(1, getLayer().getRowCount());
	}

	/**
	 */
	public void testGetColumnCount() {
		assertEpsilonEquals(1, getLayer().getColumnCount());
	}

}
