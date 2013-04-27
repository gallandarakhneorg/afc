/* $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transport,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2011-12 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.util.ref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * @param <OBJ>
 * @param <COL>
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCollectionTestCase<OBJ,COL extends Collection<OBJ>> extends AbstractTestCase {

	private final Random RANDOM = new Random();
	
	/**
	 */
	protected ArrayList<OBJ> reference;
	/**
	 */
	protected ArrayList<OBJ> unreference;
	/**
	 */
	protected COL collection;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		int count = this.RANDOM.nextInt(400)+100;
		this.reference = new ArrayList<OBJ>(count);
		for(int idx=0; idx<count; idx++) {
			this.reference.add(createContentInstance());
		}
		
		count = this.RANDOM.nextInt(5)+5;
		this.unreference = new ArrayList<OBJ>(count);
		for(int idx=0; idx<count; idx++) {
			this.unreference.add(createContentInstance());
		}

		this.collection = createCollection();
	}
	
	/**
	 * @return an instance
	 */
	protected abstract OBJ createContentInstance();
	
	/**
	 * @return a collection
	 */
	protected abstract COL createCollection();
	
	/**
	 * @param toAdd
	 */
	protected abstract void initCollectionWith(Collection<OBJ> toAdd);
	/**
	 * @param toAdd
	 */
	protected abstract void fillCollectionWith(Collection<OBJ> toAdd);
	
	@Override
	public void tearDown() throws Exception {
		this.collection = null;
		this.unreference = null;
		this.reference = null;
		super.tearDown();
	}
	
	/**
	 */
	public void testAddAll() {
        assertTrue(this.collection.addAll(this.reference));
        assertEpsilonEquals(this.reference, this.collection);
	}

	/**
	 */
	public void testSize() {
        assertEquals(0, this.collection.size());
        initCollectionWith(this.reference);        
        assertEquals(this.reference.size(), this.collection.size());
    }

	/**
	 */
    public void testIsEmpty() {
        assertTrue(this.collection.isEmpty());
        initCollectionWith(this.reference);        
        assertFalse(this.collection.isEmpty());
    }

	/**
	 */
    public void testContains() {
        initCollectionWith(this.reference);        
    	
        int count = this.RANDOM.nextInt(50)+50;
        for(int idx=0; idx<count; idx++) {
        	int index = this.RANDOM.nextInt(this.reference.size());
        	
        	assertTrue(this.collection.contains(this.reference.get(index)));
        }
        
        count = this.RANDOM.nextInt(5)+5;
        int index;
        OBJ elt;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.unreference.size());
        
        	elt = this.unreference.get(index);
        	assertFalse(this.collection.contains(elt));
        }
    }

	/**
	 */
    public void testIterator() {
        initCollectionWith(this.reference);        
	
        // Test the content
    	OBJ s;
    	Iterator<OBJ> iter = this.collection.iterator();
    	boolean asOne = false;
    	while (iter.hasNext()) {
    		s = iter.next();
    		asOne = true;
    		assertTrue(this.reference.remove(s));
    	}

    	assertTrue(asOne);
    	assertTrue(this.reference.isEmpty());
    }

	/**
	 */
	public void testContainsAll() {
        initCollectionWith(this.reference);        
	
        // Test the content
    	assertTrue(this.reference.containsAll(this.collection));
    	assertTrue(this.collection.containsAll(this.reference));
	}
    
	/**
	 */
	public void testToArray() {
        initCollectionWith(this.reference);        
	
        // Test the content
    	assertEpsilonEquals(this.reference.toArray(),this.collection.toArray());
	}
	
	/**
	 */
	public void testToArrayArray() {
        initCollectionWith(this.reference);        
	
        // Test the content if the array has the right size
        Object[] tab = new Object[this.reference.size()];
    	assertEpsilonEquals(this.reference.toArray(),this.collection.toArray(tab));
    	assertEpsilonEquals(this.reference.toArray(),tab);

        // Test the content if the array is too small
        tab = new Object[this.reference.size()/2];
        Object[] tab2 = this.collection.toArray(tab);
        assertEpsilonEquals(this.reference.toArray(),tab2);
    	assertNotEpsilonEquals(tab2,tab);
	}

	/**
	 */
	public void testClear() {
        initCollectionWith(this.reference);        
    	
        // Remove elements
    	this.collection.clear();

		// Collects the objects
    	assertTrue(this.collection.isEmpty());
	}
	
	/**
	 */
	public void testAddE() {
        initCollectionWith(this.reference);        

        String msg;
        int testCount = this.RANDOM.nextInt(5)+1;
        
        for(int i=0; i<testCount; i++) {
        	msg = "test "+(i+1)+"/"+testCount; //$NON-NLS-1$ //$NON-NLS-2$
        
	        // Add an element
	        OBJ newElement = createContentInstance();
	        this.reference.add(newElement);
	        assertTrue(msg,this.collection.add(newElement));
	        assertEquals(msg,this.reference.size(), this.collection.size());
	        assertTrue(msg,this.collection.contains(newElement));
	    	assertEpsilonEquals(msg,this.reference.toArray(),this.collection.toArray());
        }
	}

	/**
	 */
	public void testRemoveObject() {
        String msg;
	    int testCount = this.RANDOM.nextInt(5)+5;
        int removalIndex;
        
        for(int i=0; i<testCount; i++) {
        	msg = "test "+(i+1)+"/"+testCount; //$NON-NLS-1$ //$NON-NLS-2$

        	removalIndex = this.RANDOM.nextInt(this.reference.size());

            initCollectionWith(this.reference);        

            OBJ toRemove = this.reference.get(removalIndex);
            
	        // Remove elements
	        assertTrue(msg,this.collection.remove(toRemove));
	
	        assertFalse(msg,this.collection.contains(toRemove));
	        this.reference.remove(toRemove);
	    	assertEpsilonEquals(msg,this.reference.toArray(),this.collection.toArray());
        }
	}
	
	/**
	 */
	public void testRemoveAll() {
        initCollectionWith(this.reference);        
        
        fillCollectionWith(this.unreference);
        assertEquals(this.reference.size()+this.unreference.size(), this.collection.size());
        
        assertTrue(this.collection.removeAll(this.reference));
        assertEquals(this.unreference.size(), this.collection.size());
    	assertEpsilonEquals(this.unreference,this.collection);
	}

	/**
	 */
	public void testRetainAll() {
        initCollectionWith(this.reference);        
        
        fillCollectionWith(this.unreference);
        assertEquals(this.reference.size()+this.unreference.size(), this.collection.size());
        
        assertTrue(this.collection.retainAll(this.reference));
        assertEquals(this.reference.size(), this.collection.size());
    	assertEpsilonEquals(this.reference,this.collection);
	}

}
