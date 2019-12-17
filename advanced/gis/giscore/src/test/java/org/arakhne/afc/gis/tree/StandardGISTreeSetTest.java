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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.gis.AbstractGisTest;
import org.arakhne.afc.gis.TestGISReader;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationPoint;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ShapeFileFormatException;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.Resources;

/** Unit test for GISTreeSet.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class StandardGISTreeSetTest extends AbstractGisTest {

	private static final String SHP_RESOURCE = MapElementTreeSetTest.class.getPackage().getName().replaceAll("\\.", "/") //$NON-NLS-1$ //$NON-NLS-2$
			+ "/test.shp"; //$NON-NLS-1$

	private static final int MAX_REFERENCE_SIZE = 5;//1000;

	private ArrayList<GISPrimitive> reference = null;
	private Point2d center = null;
	private Rectangle2d worldBounds = null;

	@BeforeEach
	public void setUp() throws Exception {
		try {
			getLogger().info("Reading reference shape file..."); //$NON-NLS-1$

			this.reference = new ArrayList<>();

			InputStream is = Resources.getResourceAsStream(SHP_RESOURCE);
			assertNotNull(is);

			TestGISReader reader = new TestGISReader(is);

			MapElement element;

			ESRIBounds eb = reader.getBoundsFromHeader();
			this.worldBounds = new Rectangle2d();
			this.worldBounds.setFromCorners(
					eb.getMinX(), eb.getMinY(),
					eb.getMaxX(), eb.getMaxY());

			double x = 0;
			double y = 0;
			int i=0;
			while (i<MAX_REFERENCE_SIZE && (element=reader.read())!=null) {
				this.reference.add(element);
				x += element.getBoundingBox().getCenterX();
				y += element.getBoundingBox().getCenterY();
				++i;
			}

			reader.close();

			x /= this.reference.size();
			y /= this.reference.size();

			this.center = new Point2d(x,y);
		} catch (ShapeFileFormatException ex) {
			assumeFalse(true, "Cannot read the Shape file" + ex); //$NON-NLS-1$
		} finally {
			getLogger().info("finished"); //$NON-NLS-1$
		}
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.reference.clear();
		this.reference = null;
		this.center = null;
		this.worldBounds = null;
	}

	@Test
	public void testAddAll() {
        this.reference.clear();
        this.reference.add(new MapPoint(1000,1000));
        this.reference.add(new MapPoint(0,0));
        this.reference.add(new MapPoint(501,801));
        this.reference.add(new MapPoint(500,800));
        this.reference.add(new MapPoint(502,802));
        this.reference.add(new MapPoint(100,800));
        this.reference.add(new MapPoint(800,0));

        List<Point2d> transformed = new ArrayList<>();
        for (final GISPrimitive it : this.reference) {
        	transformed.add(((MapPoint)it).getPoint());
        }

        Rectangle2d bounds = new Rectangle2d();
        bounds.setFromPointCloud(transformed);

        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(bounds);

        assertTrue(test.addAll(this.reference));

        for(GISPrimitive p : test) {
        	assertTrue(this.reference.remove(p));
        }

        assertTrue(this.reference.isEmpty());
	}

	@Test
	public void testSize() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertEpsilonEquals(0, test.size());

        test = new StandardGISTreeSet<>(this.worldBounds);
        test.addAll(this.reference);
        assertEpsilonEquals(this.reference.size(), test.size());
    }

	@Test
    public void testIsEmpty() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.isEmpty());

        test = new StandardGISTreeSet<>(this.worldBounds);
   		test.addAll(this.reference);
        assertFalse(test.isEmpty());
    }

	@Test
    public void testSlowContains() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.addAll(this.reference));
        assertEpsilonEquals(this.reference.size(), test.size());

		assertFalse(test.slowContains(null));
		assertFalse(test.slowContains(test));

		// Test the content
		for(GISPrimitive elt : this.reference) {
			assertTrue(test.slowContains(elt));
		}

		assertFalse(test.slowContains(this.reference));
    }

    /**
     *
     * @param tree
     * @param filename
     * @param failed
     * @throws IOException
     */
    /*protected void writeTreeBounds(Tree<GISPrimitive,GISTreeSetNode<GISPrimitive>> tree, String filename, GISPrimitive failed) throws IOException {
    	System.out.print("Generating image..."); //$NON-NLS-1$

    	int imageSize = 2000;
    	int borderSize = imageSize / 2;
    	BufferedImage image = new BufferedImage(imageSize*2,imageSize*2,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2d = (Graphics2D)image.getGraphics();

    	if (failed!=null) {
    		System.out.print(failed.getGeoLocation().toString()+"..."); //$NON-NLS-1$
    	}

		Bounds2D bounds = tree.getRoot().getBounds();

		double minX = bounds.getMinX();
		double minY = bounds.getMinY();

    	double scaleX = imageSize / bounds.getWidth();
    	double scaleY = imageSize / bounds.getHeight();

		PrefixDepthFirstTreeIterator<GISPrimitive,GISTreeSetNode<GISPrimitive>> iterator
			= new PrefixDepthFirstTreeIterator<GISPrimitive,GISTreeSetNode<GISPrimitive>>(tree);
		GISPrimitive primitive;
		GISTreeSetNode<GISPrimitive> node;
		boolean fillNode;
		Bounds2D r;

		while (iterator.hasNext()) {
			node = iterator.next();

			fillNode = false;
			if (failed!=null) {
				for(int i=0; i<node.getUserDataCount(); ++i) {
					if (node.getUserDataAt(i)==failed) {
						fillNode = true;
					}
				}
			}

			r = node.getBounds();
			if (r!=null) {
				g2d.setColor(Color.BLUE);
				if (fillNode) {
					g2d.fillRect(
							(int)((r.getMinX() - minX) * scaleX + borderSize),
							(int)((r.getMinY() - minY) * scaleY + borderSize),
							(int)(r.getWidth() * scaleX),
							(int)(r.getHeight() * scaleY));
				}
				else if (failed==null) {
					g2d.drawRect(
						(int)((r.getMinX() - minX) * scaleX + borderSize),
						(int)((r.getMinY() - minY) * scaleY + borderSize),
						(int)(r.getWidth() * scaleX),
						(int)(r.getHeight() * scaleY));
				}
			}

			g2d.setColor(Color.RED);

			for(int i=0; i<node.getUserDataCount(); ++i) {
				primitive = node.getUserDataAt(i);
				r = primitive.getGeoLocation().toBounds2D();
				if (r!=null) {
					if (primitive==failed) {
						g2d.fillOval(
								(int)((r.getMinX() - minX) * scaleX + borderSize - 50),
								(int)((r.getMinY() - minY) * scaleY + borderSize - 50),
								(int)(r.getWidth() * scaleX + 100),
								(int)(r.getHeight() * scaleY + 100));
						g2d.setColor(Color.WHITE);
						g2d.fillRect(
								(int)((r.getMinX() - minX) * scaleX + borderSize),
								(int)((r.getMinY() - minY) * scaleY + borderSize),
								(int)(r.getWidth() * scaleX),
								(int)(r.getHeight() * scaleY));
					}
					else if (failed==null) {
						g2d.drawRect(
								(int)((r.getMinX() - minX) * scaleX + borderSize),
								(int)((r.getMinY() - minY) * scaleY + borderSize),
								(int)(r.getWidth() * scaleX),
								(int)(r.getHeight() * scaleY));
					}
				}
			}
		}

    	g2d.dispose();

    	ImageIO.write(image, "png", new File(filename)); //$NON-NLS-1$
    	System.out.println("done"); //$NON-NLS-1$
    }*/

	@Test
    public void testContains() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.addAll(this.reference));
        assertEpsilonEquals(this.reference.size(), test.size());

    	/*System.out.print("Generating dot file..."); //$NON-NLS-1$
        DotDotWriter writer = new DotDotWriter(new FileOutputStream("/home/sgalland/Bureau/test1.dot"));
        writer.write(test.getTree());
        writer.close();
    	System.out.println("done"); //$NON-NLS-1$*/

        //writeTreeBounds(test.getTree(), "/home/sgalland/Bureau/test2.png", null);


		assertFalse(test.contains(null));
		assertFalse(test.contains(test));

		// Test the content
		for(GISPrimitive elt : this.reference) {
			//try {
				assertTrue(test.contains(elt), elt.getGeoLocation().toBounds2D().toString());
			/*}
			catch(Throwable _) {
		        writeTreeBounds(test.getTree(), "/home/sgalland/Bureau/test3.png", elt);
				assertTrue(test.contains(elt));
			}*/
		}

		assertFalse(test.contains(this.reference));
    }

	@Test
    public void testIterator() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.addAll(this.reference));
        assertEpsilonEquals(this.reference.size(), test.size());

        // Test the content
    	GISPrimitive s;
    	Iterator<GISPrimitive> iter = test.iterator();
    	while (iter.hasNext()) {
    		s = iter.next();
    		assertTrue(this.reference.remove(s));
    	}

    	assertTrue(this.reference.isEmpty());
    }

	@Test
	public void testContainsAll() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.addAll(this.reference));
        assertEpsilonEquals(this.reference.size(), test.size());

        // Test the content
    	assertTrue(this.reference.containsAll(test));
    	assertTrue(test.containsAll(this.reference));
	}

	@Test
	public void testToArray() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.addAll(this.reference));
        assertEpsilonEquals(this.reference.size(), test.size());

        // Test the content
    	assertEpsilonEquals(this.reference.toArray(),test.toArray());
	}

	@Test
	public void testToArrayArray() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.addAll(this.reference));
        assertEpsilonEquals(this.reference.size(), test.size());

        // Test the content if the array has the right size
        GISPrimitive[] tab = new GISPrimitive[this.reference.size()];
    	assertEpsilonEquals(this.reference.toArray(),test.toArray(tab));
    	assertEpsilonEquals(this.reference.toArray(),tab);

        // Test the content if the array is too small
        tab = new GISPrimitive[this.reference.size()/2];
        GISPrimitive[] tab2 = test.toArray(tab);
    	assertEpsilonEquals(this.reference.toArray(),tab2);
    	assertNotEquals(tab2,tab);
	}

	@Test
	public void testClear() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        test.addAll(this.reference);
        assertEpsilonEquals(this.reference.size(), test.size());

        // Remove elements
    	test.clear();

		// Collects the objects
    	assertTrue(test.isEmpty());
	}

	@Test
	public void testAddE() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.addAll(this.reference));
        assertEpsilonEquals(this.reference.size(), test.size());

        String msg;
        Random rnd = new Random();
        int testCount = rnd.nextInt(5)+1;

        for(int i=0; i<testCount; ++i) {
        	msg = "test "+(i+1)+"/"+testCount; //$NON-NLS-1$ //$NON-NLS-2$
        	getLogger().info(msg+"..."); //$NON-NLS-1$

	        // Add an element
        	double x = this.worldBounds.getMinX() + rnd.nextDouble() * this.worldBounds.getWidth();
        	double y = this.worldBounds.getMinY() + rnd.nextDouble() * this.worldBounds.getHeight();
	        GISPrimitive newElement = new MapPoint(x,y);
	        assertTrue(this.reference.add(newElement), msg);
	        assertTrue(test.add(newElement), msg);
	        assertEquals(this.reference.size(), test.size(), msg);
	        assertTrue(test.slowContains(newElement), msg);
	    	assertEpsilonEquals(this.reference.toArray(),test.toArray(), msg);
	    	getLogger().info("done"); //$NON-NLS-1$
        }
	}

	/**
	 */
	public void testRemoveObject() {
        String msg;
		Random rnd = new Random();
        int testCount = rnd.nextInt(5)+5;
        int removalIndex;

        for(int i=0; i<testCount; ++i) {
        	msg = "test "+(i+1)+"/"+testCount; //$NON-NLS-1$ //$NON-NLS-2$
        	getLogger().info(msg+"..."); //$NON-NLS-1$

        	removalIndex = rnd.nextInt(this.reference.size());

            StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
            assertTrue(test.addAll(this.reference), msg);
            assertEquals(this.reference.size(), test.size(), msg);

            GISPrimitive toRemove = this.reference.get(removalIndex);
	        assertTrue(test.slowContains(toRemove), msg);

	        // Remove elements
	        assertTrue(test.remove(toRemove), msg);

	        assertFalse(test.slowContains(toRemove), msg);
	        this.reference.remove(toRemove);
	    	assertEpsilonEquals(this.reference.toArray(),test.toArray(), msg);
	    	getLogger().info("done"); //$NON-NLS-1$
        }
	}

	/**
	 */
	public void testIteratorRectangle() {
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        test.addAll(this.reference);
        assertEpsilonEquals(this.reference.size(), test.size());

        Random rnd = new Random();
        int testCount = rnd.nextInt(20)+10;
        Rectangle2d bounds;
    	Collection<GISPrimitive> inter = new TreeSet<>(new Comparator<GISPrimitive>() {
			@Override
			public int compare(GISPrimitive o1, GISPrimitive o2) {
				return System.identityHashCode(o2) - System.identityHashCode(o1);
			}
    	});
    	Iterator<GISPrimitive> iter;
    	int deltaX,deltaY,width,height;
    	String msg;

        for(int idxTest=0; idxTest<testCount; ++idxTest) {

        	msg = "test "+(idxTest+1)+"/"+testCount; //$NON-NLS-1$ //$NON-NLS-2$
        	getLogger().info(msg+"..."); //$NON-NLS-1$

        	deltaX = rnd.nextInt(1000)*(rnd.nextBoolean() ? -1 : 1);
        	deltaY = rnd.nextInt(1000)*(rnd.nextBoolean() ? -1 : 1);
        	width = rnd.nextInt(100);
        	height = rnd.nextInt(100);

	        bounds = new Rectangle2d();
	        bounds.setFromCorners(
	        		this.center.getX()+deltaX, this.center.getY()+deltaY,
	        		this.center.getX()+deltaX+width,
	        		this.center.getY()+deltaY+height);
	        inter.clear();

	    	for (GISPrimitive primitive : this.reference) {
	    		Shape2d<?> primitiveRect = primitive.getGeoLocation().toBounds2D();
				if (bounds.intersects(primitiveRect)) {
					inter.add(primitive);
				}
			}

	        iter = test.iterator(bounds);
	        GISPrimitive primitive;
	    	while (iter.hasNext()) {
	    		primitive = iter.next();
	    		assertTrue(inter.remove(primitive), msg);
	    	}

	    	if (!inter.isEmpty()) {
		    	for (GISPrimitive pr: this.reference) {
		    		Shape2d<?> primitiveRect = pr.getGeoLocation().toBounds2D();
					if (bounds.intersects(primitiveRect)) {
						inter.add(pr);
					}
				}

		        iter = test.iterator(bounds);
		    	while (iter.hasNext()) {
		    		primitive = iter.next();
		    		assertTrue(inter.remove(primitive), msg);
		    	}
	    	}

	    	//TODO: assertEquals(0, inter.size());

	    	getLogger().info("done"); //$NON-NLS-1$
        }
	}

	/**
	 */
    public void testGetGeoLocation() {
    	getLogger().info("Populate the tree..."); //$NON-NLS-1$
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.addAll(this.reference));
        assertEpsilonEquals(this.reference.size(), test.size());
        getLogger().info("finished"); //$NON-NLS-1$

        getLogger().info("Running tests..."); //$NON-NLS-1$
		assertNull(test.get((GeoLocation)null));

		// Test the content
		int count = Math.max(Math.min(getRandom().nextInt(this.reference.size()),1000),250);
		GISPrimitive p;
		for(int idx=0; idx<count; ++idx) {
			if (idx%10==0)
				getLogger().info("\t("+(idx+1)+"/"+count+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			p = this.reference.get(getRandom().nextInt(this.reference.size()));
			assertSame(p, test.get(p.getGeoLocation()));
		}

		assertNull(test.get(new GeoLocationPoint(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)));
		getLogger().info("\tfinished"); //$NON-NLS-1$
    }

	/**
	 */
    public void testGetGeoId() {
    	getLogger().info("Populate the tree..."); //$NON-NLS-1$
        StandardGISTreeSet<GISPrimitive> test = new StandardGISTreeSet<>(this.worldBounds);
        assertTrue(test.addAll(this.reference));
        assertEpsilonEquals(this.reference.size(), test.size());
        getLogger().info("finished"); //$NON-NLS-1$

        getLogger().info("Running tests..."); //$NON-NLS-1$
		assertNull(test.get((GeoId)null));

		// Test the content
		int count = Math.max(Math.min(getRandom().nextInt(this.reference.size()),1000),250);
		GISPrimitive p;
		for(int idx=0; idx<count; ++idx) {
			if (idx%10==0)
				getLogger().info("\t("+(idx+1)+"/"+count+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			p = this.reference.get(getRandom().nextInt(this.reference.size()));
			assertSame(p, test.get(p.getGeoId()));
		}

		getLogger().info("\tfinished"); //$NON-NLS-1$
    }

}
