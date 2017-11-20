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

package org.arakhne.afc.gis.grid;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.junit.After;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.gis.TestGISReader;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.io.shape.ShapeFileFormatException;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.Resources;

/** Unit test for MapElementGridSet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class MapElementGridSetTest extends AbstractTestCase {

	private static final String SHP_RESOURCE = MapPolylineGridSetTest.class.getPackage().getName().replaceAll("\\.", "/") //$NON-NLS-1$ //$NON-NLS-2$
			+ "/test.shp"; //$NON-NLS-1$

	private ArrayList<MapPolyline> reference = null;
	private Rectangle2d bounds = null;

	@Before
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
			throw new AssumptionViolatedException("Cannot read Shape file", ex); //$NON-NLS-1$
		}
	}

	@After
	public void tearDown() throws Exception {
		this.reference.clear();
		this.reference = null;
		this.bounds = null;
	}

	@Test
	public void testGetNearest() {
		getLogger().info("Preparing the benchmark..."); //$NON-NLS-1$
		assertNotNull(this.bounds);
		MapElementGridSet<MapPolyline> test = new MapElementGridSet<>(100, 100, this.bounds);
        test.addAll(this.reference);
        assertEquals(this.reference.size(), test.size());

        getLogger().info("Run test..."); //$NON-NLS-1$

    	Random rnd = new Random();
    	MapPolyline nearestData;
    	double minDistance, distance, x, y;
    	Point2d p;
    	final ArrayList<MapPolyline> nearest = new ArrayList<>();

    	boolean found = false;
    	int tries = 5;
    	do {
    		nearest.clear();
        	x = rnd.nextDouble()*this.bounds.getWidth()+this.bounds.getMinX();
        	y = rnd.nextDouble()*this.bounds.getHeight()+this.bounds.getMinY();
        	p = new Point2d(x,y);

        	minDistance = Double.MAX_VALUE;
        	for (MapPolyline line : this.reference) {
        		distance = line.getDistance(p);
        		if (distance<minDistance) {
        			minDistance = distance;
        		}
        	}
        	for (MapPolyline line : this.reference) {
        		distance = line.getDistance(p);
        		if (isEpsilonEquals(distance, minDistance)) {
        			nearest.add(line);
        		}
        	}

        	nearestData = test.getNearest(x, y);
        	assertNotNull(nearestData);

        	found = false;
        	for (MapPolyline poly : nearest) {
        		if (poly == nearestData) {
        			found = true;
        			break;
        		}
        	}
        	--tries;
    	}
    	while (!found && tries > 0);
    	assertTrue("Polyline not found", found); //$NON-NLS-1$
	}

	@Test
	public void testAddE() {
		StandardGISGridSet<GISPrimitive> test = new StandardGISGridSet<>(100,100,this.bounds);
        assertTrue(test.addAll(this.reference));
        assertEquals(this.reference.size(), test.size());

        String msg;
        Random rnd = new Random();
        int testCount = rnd.nextInt(5)+1;

        for(int i=0; i<testCount; ++i) {
        	msg = "test "+(i+1)+"/"+testCount; //$NON-NLS-1$ //$NON-NLS-2$
        	getLogger().info(msg+"..."); //$NON-NLS-1$

	        // Add an element
        	double x = this.bounds.getMinX() + rnd.nextDouble() * this.bounds.getWidth();
        	double y = this.bounds.getMinY() + rnd.nextDouble() * this.bounds.getHeight();
	        MapPolyline newElement = new MapPolyline();
	        newElement.addPoint(x, y);
        	x = this.bounds.getMinX() + rnd.nextDouble() * this.bounds.getWidth();
        	y = this.bounds.getMinY() + rnd.nextDouble() * this.bounds.getHeight();
	        newElement.addPoint(x, y);
	        assertTrue(this.reference.add(newElement));
	        assertTrue(msg,test.add(newElement));
	        assertEquals(msg,this.reference.size(), test.size());
	        assertTrue(msg,test.slowContains(newElement));
	    	assertEpsilonEquals(msg,this.reference.toArray(),test.toArray());
	    	getLogger().info("done"); //$NON-NLS-1$
        }
	}

}