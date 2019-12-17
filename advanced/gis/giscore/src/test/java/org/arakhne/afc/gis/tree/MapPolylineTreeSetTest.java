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

package org.arakhne.afc.gis.tree;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.AbstractGisTest;
import org.arakhne.afc.gis.TestGISReader;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.io.shape.ShapeFileFormatException;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.OutputParameter;
import org.arakhne.afc.vmutil.Resources;

/** Unit test for MapPolylineTreeSet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class MapPolylineTreeSetTest extends AbstractGisTest {

	private static final String SHP_RESOURCE = MapElementTreeSetTest.class.getPackage().getName().replaceAll("\\.", "/") //$NON-NLS-1$ //$NON-NLS-2$
			+ "/test.shp"; //$NON-NLS-1$

	private ArrayList<MapPolyline> reference = null;
	private Rectangle2d bounds = null;

	@BeforeEach
	public void setUp() throws Exception {
		this.reference = new ArrayList<>();

		InputStream is = Resources.getResourceAsStream(SHP_RESOURCE);
		assertNotNull(is);

		try {
			TestGISReader reader = new TestGISReader(is);
	
			MapElement element;
	
			Rectangle2d abounds = new Rectangle2d();
			boolean first = true;
			while ((element=reader.read())!=null) {
				if (element instanceof MapPolyline) {
					MapPolyline p = (MapPolyline)element;
					this.reference.add(p);
					if (first) {
						first = false;
						abounds.set(p.getBoundingBox());
					} else {
						abounds.setUnion(p.getBoundingBox());
					}
				}
			}
	
			reader.close();
	
			if (!first) this.bounds = abounds;
			else this.bounds = null;
		} catch (ShapeFileFormatException ex) {
			assumeFalse(true, "Cannot read the Shape file" + ex); //$NON-NLS-1$
		}
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.reference.clear();
		this.reference = null;
		this.bounds = null;
	}

	@Test
	public void testGetNearestEnd() {
    	MapPolylineTreeSet<MapPolyline> test;
		if (this.bounds!=null)
			test = new MapPolylineTreeSet<>(this.bounds);
		else
			test = new MapPolylineTreeSet<>();
        test.addAll(this.reference);
        assertEpsilonEquals(this.reference.size(), test.size());

        Random rnd = new Random();
    	MapPolyline nearestData;
    	double minDistance, distance, x, y;
    	final ArrayList<MapPolyline> nearest = new ArrayList<>();

    	x = rnd.nextDouble()*this.bounds.getWidth()+this.bounds.getMinX();
    	y = rnd.nextDouble()*this.bounds.getHeight()+this.bounds.getMinY();

    	minDistance = Double.MAX_VALUE;
    	for (MapPolyline line : this.reference) {
    		distance = line.distanceToEnd(new Point2d(x, y));
    		if (distance<minDistance) {
    			minDistance = distance;
    			nearest.clear();
    			nearest.add(line);
    		}
    		else if (distance==minDistance) {
    			nearest.add(line);
    		}
    	}

    	nearestData = test.getNearestEnd(x, y);
    	assertNotNull(nearestData);

    	assertTrue(nearest.contains(nearestData));
	}

	private boolean isEq(Point2d p, MapPolyline polyline) {
		Point2d p1 = polyline.getPointAt(0);
		Point2d p2 = polyline.getPointAt(polyline.getPointCount()-1);
		boolean a = isEpsilonEquals(p.getX(), p1.getX())
				&&  isEpsilonEquals(p.getY(), p1.getY());
		boolean b = isEpsilonEquals(p.getX(), p2.getX())
				&&  isEpsilonEquals(p.getY(), p2.getY());
		return a || b;
	}

	private MapPolyline extractFromReference(Point2d p) {
		for(MapPolyline pp : this.reference) {
			if (isEq(p, pp)) {
				return pp;
			}
		}
		return null;
	}

	private void assertConnected(MapPolyline base, MapPolyline neightbour, boolean isFirst) {
		assertNotSame(base, neightbour);
		Point2d p;
		if (isFirst) {
			p = base.getPointAt(0);
		}
		else {
			p = base.getPointAt(base.getPointCount()-1);
		}

		if (neightbour==null) {
			if (extractFromReference(p)==null) return;
		}
		else if (isEq(p, neightbour)) {
			return;
		}

		if (neightbour==null)
			fail("invalid connection: null"); //$NON-NLS-1$
		else
			fail("invalid connection: "+neightbour.getDistance(p)); //$NON-NLS-1$
	}

	/**
	 */
	public void testAddPDoubleOutputParameterOutputParameter() {
		assertNotNull(this.bounds);
		MapPolylineTreeSet<MapPolyline> test = new MapPolylineTreeSet<>(this.bounds);
        test.addAll(this.reference);
        assertEpsilonEquals(this.reference.size(), test.size());

        OutputParameter<MapPolyline> first = new OutputParameter<>();
        OutputParameter<MapPolyline> second = new OutputParameter<>();

        MapPolyline polyline;
        Point2d p1, p2;

        first.clear();
        second.clear();
        polyline = new MapPolyline();
        p1 = this.reference.get(0).getPointAt(this.reference.get(0).getPointCount()-1);
        p2 = new Point2d(
        		this.bounds.getMinX() + getRandom().nextDouble() * this.bounds.getWidth(),
        		this.bounds.getMinY() + getRandom().nextDouble() * this.bounds.getHeight());
        polyline.addPoint(p1.getX(), p1.getY());
        polyline.addPoint(p2.getX(), p2.getY());
        assertTrue(test.add(polyline, 0.5, first, second));
        assertConnected(polyline, first.get(), true);
        assertConnected(polyline, second.get(), false);

        first.clear();
        second.clear();
        polyline = new MapPolyline();
        p1 = new Point2d(
        		this.bounds.getMinX() + getRandom().nextDouble() * this.bounds.getWidth(),
        		this.bounds.getMinY() + getRandom().nextDouble() * this.bounds.getHeight());
        p2 = this.reference.get(1).getPointAt(this.reference.get(1).getPointCount()-1);
        polyline.addPoint(p1.getX(), p1.getY());
        polyline.addPoint(p2.getX(), p2.getY());
        assertTrue(test.add(polyline, 0.5, first, second));
        assertConnected(polyline, first.get(), true);
        assertConnected(polyline, second.get(), false);

        first.clear();
        second.clear();
        polyline = new MapPolyline();
        p1 = this.reference.get(2).getPointAt(this.reference.get(2).getPointCount()-1);
        p2 = this.reference.get(3).getPointAt(this.reference.get(3).getPointCount()-1);
        polyline.addPoint(p1.getX(), p1.getY());
        polyline.addPoint(p2.getX(), p2.getY());
        assertTrue(test.add(polyline, 0.5, first, second));
        assertConnected(polyline, first.get(), true);
        assertConnected(polyline, second.get(), false);

        first.clear();
        second.clear();
        polyline = new MapPolyline();
        p1 = new Point2d(
        		this.bounds.getMinX() + getRandom().nextDouble() * this.bounds.getWidth(),
        		this.bounds.getMinY() + getRandom().nextDouble() * this.bounds.getHeight());
        p2 = new Point2d(
        		this.bounds.getMinX() + getRandom().nextDouble() * this.bounds.getWidth(),
        		this.bounds.getMinY() + getRandom().nextDouble() * this.bounds.getHeight());
        polyline.addPoint(p1.getX(), p1.getY());
        polyline.addPoint(p2.getX(), p2.getY());
        assertTrue(test.add(polyline, 0.5, first, second));
        assertConnected(polyline, first.get(), true);
        assertConnected(polyline, second.get(), false);
	}

}
