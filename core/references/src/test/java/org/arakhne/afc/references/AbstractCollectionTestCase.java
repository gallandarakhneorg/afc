/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.references;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.testtools.AbstractTestCase;

/**
 * @param <COL>
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCollectionTestCase<COL extends Collection<String>> extends AbstractTestCase {

	private final Random RANDOM = new Random();
	
	/**
	 */
	protected ArrayList<String> reference;
	/**
	 */
	protected ArrayList<String> unreference;
	/**
	 */
	protected COL collection;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		int refCount = 10;
		this.reference = new ArrayList<>(refCount);
		for(int idx=0; idx<refCount; idx++) {
			this.reference.add("REF_"+idx); //$NON-NLS-1$
		}
		
		this.unreference = new ArrayList<>(5);
		for(int idx=0; idx<5; idx++) {
			this.unreference.add("UNREF_"+idx); //$NON-NLS-1$
		}

		this.collection = createCollection();
	}
	
	/**
	 * @return a collection
	 */
	protected abstract COL createCollection();
	
	/**
	 * @param toAdd
	 */
	protected final void initCollectionWith(Collection<String> toAdd) {
		this.collection.clear();
		this.collection.addAll(toAdd);
	}
	
	/**
	 * @param toAdd
	 */
	protected final void fillCollectionWith(Collection<String> toAdd) {
		this.collection.addAll(toAdd);
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.collection = null;
		this.unreference = null;
		this.reference = null;
	}
	
	/**
	 */
	@Test
	public void testAddAll() {
        Assert.assertTrue(this.collection.addAll(this.reference));
        assertEpsilonEquals(this.reference, this.collection);
	}

	/**
	 */
	@Test
	public void testSize() {
        Assert.assertEquals(0, this.collection.size());
        initCollectionWith(this.reference);        
        Assert.assertEquals(this.reference.size(), this.collection.size());
    }

	/**
	 */
	@Test
    public void testIsEmpty() {
		Assert.assertTrue(this.collection.isEmpty());
        initCollectionWith(this.reference);        
        Assert.assertFalse(this.collection.isEmpty());
    }

	/**
	 */
	@Test
    public void testContains() {
        initCollectionWith(this.reference);        
    	
        int count = this.RANDOM.nextInt(50)+50;
        for(int idx=0; idx<count; idx++) {
        	int index = this.RANDOM.nextInt(this.reference.size());
        	
        	Assert.assertTrue(this.collection.contains(this.reference.get(index)));
        }
        
        count = this.RANDOM.nextInt(5)+5;
        int index;
        String elt;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.unreference.size());
        
        	elt = this.unreference.get(index);
        	Assert.assertFalse(this.collection.contains(elt));
        }
    }

	/**
	 */
	@Test
    public void testIterator() {
        initCollectionWith(this.reference);        
	
        // Test the content
    	String s;
    	Iterator<String> iter = this.collection.iterator();
    	boolean asOne = false;
    	while (iter.hasNext()) {
    		s = iter.next();
    		asOne = true;
    		Assert.assertTrue(this.reference.remove(s));
    	}

    	Assert.assertTrue(asOne);
    	Assert.assertTrue(this.reference.isEmpty());
    }

	/**
	 */
	@Test
	public void testContainsAll() {
        initCollectionWith(this.reference);        
	
        // Test the content
        Assert.assertTrue(this.reference.containsAll(this.collection));
        Assert.assertTrue(this.collection.containsAll(this.reference));
	}
    
	/**
	 */
	@Test
	public void testToArray() {
        initCollectionWith(this.reference);        
	
        // Test the content
    	assertEpsilonEquals(this.reference.toArray(),this.collection.toArray());
	}
	
	/**
	 */
	@Test
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
	@Test
	public void testClear() {
        initCollectionWith(this.reference);        
    	
        // Remove elements
    	this.collection.clear();

		// Collects the objects
    	Assert.assertTrue(this.collection.isEmpty());
	}
	
	/**
	 */
	@Test
	public void testAddE() {
        initCollectionWith(this.reference);        

        String msg;
        int testCount = this.RANDOM.nextInt(5)+1;
        
        for(int i=0; i<testCount; i++) {
        	msg = "test "+(i+1)+"/"+testCount; //$NON-NLS-1$ //$NON-NLS-2$
        
	        // Add an element
	        String newElement = "NEWELT"+i; //$NON-NLS-1$
	        this.reference.add(newElement);
	        Assert.assertTrue(msg,this.collection.add(newElement));
	        Assert.assertEquals(msg,this.reference.size(), this.collection.size());
	        Assert.assertTrue(msg,this.collection.contains(newElement));
	    	assertEpsilonEquals(msg,this.reference.toArray(),this.collection.toArray());
        }
	}

	/**
	 */
	@Test
	public void testRemoveObject() {
        String msg;
	    int testCount = this.RANDOM.nextInt(5)+5;
        int removalIndex;
        
        for(int i=0; i<testCount; i++) {
        	msg = "test "+(i+1)+"/"+testCount; //$NON-NLS-1$ //$NON-NLS-2$

        	removalIndex = this.RANDOM.nextInt(this.reference.size());

            initCollectionWith(this.reference);        

            String toRemove = this.reference.get(removalIndex);
            
	        // Remove elements
            Assert.assertTrue(msg,this.collection.remove(toRemove));
	
            Assert.assertFalse(msg,this.collection.contains(toRemove));
	        this.reference.remove(toRemove);
	    	assertEpsilonEquals(msg,this.reference.toArray(),this.collection.toArray());
        }
	}
	
	/**
	 */
	@Test
	public void testRemoveAll() {
        initCollectionWith(this.reference);        
        
        fillCollectionWith(this.unreference);
        Assert.assertEquals(this.reference.size()+this.unreference.size(), this.collection.size());
        
        Assert.assertTrue(this.collection.removeAll(this.reference));
        Assert.assertEquals(this.unreference.size(), this.collection.size());
    	assertEpsilonEquals(this.unreference,this.collection);
	}

	/**
	 */
	@Test
	public void testRetainAll() {
        initCollectionWith(this.reference);        
        
        fillCollectionWith(this.unreference);
        Assert.assertEquals(this.reference.size()+this.unreference.size(), this.collection.size());
        
        Assert.assertTrue(this.collection.retainAll(this.reference));
        Assert.assertEquals(this.reference.size(), this.collection.size());
    	assertEpsilonEquals(this.reference,this.collection);
	}

}
