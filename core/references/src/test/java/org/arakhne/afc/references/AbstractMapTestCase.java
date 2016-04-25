/* $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transport,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2011-12 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.references;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @param <K>
 * @param <V>
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractMapTestCase<K,V> extends AbstractTestCase {

	/** Random number generator.
	 */
	protected final Random RANDOM = new Random();
	
	/**
	 */
	protected HashMap<K,V> reference;
	/**
	 */
	protected HashMap<K,V> unreference;
	/**
	 */
	protected Map<K,V> map;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		int count = this.RANDOM.nextInt(400)+100;
		this.reference = new HashMap<>(count);
		for(int idx=0; idx<count; idx++) {
			this.reference.put(createKeyInstance("in/"), createValueInstance("in/")); //$NON-NLS-1$//$NON-NLS-2$
		}
		
		count = this.RANDOM.nextInt(5)+5;
		this.unreference = new HashMap<>(count);
		for(int idx=0; idx<count; idx++) {
			this.unreference.put(createKeyInstance("out/"), createValueInstance("out/")); //$NON-NLS-1$//$NON-NLS-2$
		}

		this.map = createMap();
	}
	
	/**
	 * @param prefix
	 * @return an instance
	 */
	protected abstract K createKeyInstance(String prefix);
	
	/**
	 * @param prefix
	 * @return an instance
	 */
	protected abstract V createValueInstance(String prefix);

	/**
	 * @return a map
	 */
	protected abstract Map<K,V> createMap();
	
	/**
	 * @param toAdd
	 */
	protected void initMapWith(Map<K,V> toAdd) {
		this.map.clear();
		this.map.putAll(toAdd);
	}
	
	/**
	 * @param toAdd
	 */
	protected void fillMapWith(Map<K,V> toAdd) {
		this.map.putAll(toAdd);
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.map = null;
		this.unreference = null;
		this.reference = null;
	}
	
	/** Replies the key at the given index.
	 * 
	 * @param map
	 * @param index
	 * @return the key
	 */
	public static <KK> KK key(Map<KK,?> map, int index) {
		int i = 0; 
		for(KK key : map.keySet()) {
			if (i==index) return key;
			++i;
		}
		throw new IndexOutOfBoundsException();
	}
	
	/** Replies the value at the given index.
	 * 
	 * @param map
	 * @param index
	 * @return the vlaue
	 */
	public static <VV> VV value(Map<?,VV> map, int index) {
		int i = 0; 
		for(VV value : map.values()) {
			if (i==index) return value;
			++i;
		}
		throw new IndexOutOfBoundsException();
	}

	/**
     */
	@Test
    public void testSize() {
        Assert.assertEquals(0, this.map.size());
        initMapWith(this.reference);        
        Assert.assertEquals(this.reference.size(), this.map.size());
    }

    /**
     */
	@Test
    public void testIsEmpty() {
		Assert.assertTrue(this.map.isEmpty());
        initMapWith(this.reference);        
        Assert.assertFalse(this.map.isEmpty());
    }

    /**
     */
	@Test
    public void testEntrySet() {
    	Set<Entry<K,V>> entries;
    	
    	entries = this.map.entrySet();
    	Assert.assertTrue(entries.isEmpty());
    	
    	initMapWith(this.reference);

    	entries = this.map.entrySet();
    	Assert.assertFalse(entries.isEmpty());
    	assertEpsilonEquals(this.reference.entrySet(), entries);
    }

    /**
     */
	@Test
    public void testContainsKey() {
    	initMapWith(this.reference);
        int count = this.RANDOM.nextInt(50)+50;
        for(int idx=0; idx<count; idx++) {
        	int index = this.RANDOM.nextInt(this.reference.size());
        	Assert.assertTrue("#"+idx, this.map.containsKey(key(this.reference,index))); //$NON-NLS-1$
        }
        count = this.RANDOM.nextInt(5)+5;
        int index;
        K elt;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.unreference.size());
        	elt = key(this.unreference, index);
        	Assert.assertFalse("#"+idx, this.map.containsKey(elt)); //$NON-NLS-1$
        }
    }

    /**
     */
	@Test
    public void testContainsValue() {
    	initMapWith(this.reference);
        int count = this.RANDOM.nextInt(50)+50;
        for(int idx=0; idx<count; idx++) {
        	int index = this.RANDOM.nextInt(this.reference.size());
        	Assert.assertTrue("#"+idx, this.map.containsValue(value(this.reference,index))); //$NON-NLS-1$
        }
        count = this.RANDOM.nextInt(5)+5;
        int index;
        V elt;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.unreference.size());
        	elt = value(this.unreference, index);
        	Assert.assertFalse("#"+idx, this.map.containsValue(elt)); //$NON-NLS-1$
        }
    }

    /**
     */
	@Test
    public void testGet() {
        int count = this.RANDOM.nextInt(50)+50;
        for(int idx=0; idx<count; idx++) {
        	int index = this.RANDOM.nextInt(this.reference.size());
        	Assert.assertNull("#"+idx, this.map.get(key(this.reference,index))); //$NON-NLS-1$
        }
        count = this.RANDOM.nextInt(5)+5;
        int index;
        K elt;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.unreference.size());
        	elt = key(this.unreference, index);
        	Assert.assertNull("#"+idx, this.map.get(elt)); //$NON-NLS-1$
        }
        
        initMapWith(this.reference);

        count = this.RANDOM.nextInt(50)+50;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.reference.size());
        	elt = key(this.reference, index);
        	Assert.assertEquals("#"+idx, this.reference.get(elt), this.map.get(elt)); //$NON-NLS-1$
        }
        count = this.RANDOM.nextInt(5)+5;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.unreference.size());
        	elt = key(this.unreference, index);
        	Assert.assertNull("#"+idx, this.map.get(elt)); //$NON-NLS-1$
        }
    }

    /**
     */
	@Test
    public void testPut() {
        int count = this.RANDOM.nextInt(50)+50;
        for(int idx=0; idx<count; idx++) {
        	K key = createKeyInstance("tmp/"); //$NON-NLS-1$
        	V value = createValueInstance("tmp/"); //$NON-NLS-1$
        	this.map.put(key, value);
        	Assert.assertSame(value, this.map.get(key));
        }
    }

    /**
     */
	@Test
    public void testRemove() {
        initMapWith(this.reference);

        int index, count;
        K elt;
        
        count = this.RANDOM.nextInt(50)+50;
        for(int idx=0; !this.reference.isEmpty() && idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.reference.size());
        	elt = key(this.reference, index);
        	Assert.assertSame("#"+idx, this.reference.get(elt), this.map.remove(elt)); //$NON-NLS-1$
        	this.reference.remove(elt);
        	Assert.assertNull("#"+idx, this.map.get(elt)); //$NON-NLS-1$
        }
        count = this.RANDOM.nextInt(5)+5;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.unreference.size());
        	elt = key(this.unreference, index);
        	Assert.assertNull("#"+idx, this.map.remove(elt)); //$NON-NLS-1$
        }
    }

    /**
     */
	@Test
    public void testPutAll() {
        int index, count;
        K elt;
        
        this.map.putAll(this.reference);

        count = this.RANDOM.nextInt(50)+50;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.reference.size());
        	elt = key(this.reference, index);
        	Assert.assertEquals("#"+idx, this.reference.get(elt), this.map.get(elt)); //$NON-NLS-1$
        }
        count = this.RANDOM.nextInt(5)+5;
        for(int idx=0; idx<count; idx++) {
        	index = this.RANDOM.nextInt(this.unreference.size());
        	elt = key(this.unreference, index);
        	Assert.assertNull("#"+idx, this.map.get(elt)); //$NON-NLS-1$
        }
    }

    /**
     */
	@Test
    public void testClear() {
		Assert.assertEquals(0, this.map.size());
		Assert.assertTrue(this.map.isEmpty());
    	
    	initMapWith(this.reference);
    	Assert.assertEquals(this.reference.size(), this.map.size());
    	Assert.assertFalse(this.map.isEmpty());
    	
    	this.map.clear();
    	
    	Assert.assertEquals(0, this.map.size());
    	Assert.assertTrue(this.map.isEmpty());
    }

    /**
     */
	@Test
    public void testKeySet() {
    	Set<K> keys;
    	
    	keys = this.map.keySet();
    	Assert.assertTrue(keys.isEmpty());
    	
    	initMapWith(this.reference);

    	keys = this.map.keySet();
    	Assert.assertFalse(keys.isEmpty());
    	assertEpsilonEquals(this.reference.keySet(), keys);
    }

    /**
     */
	@Test
    public void testValues() {
    	Collection<V> values;
    	
    	values = this.map.values();
    	Assert.assertTrue(values.isEmpty());
    	
    	initMapWith(this.reference);

    	values = this.map.values();
    	Assert.assertFalse(values.isEmpty());
    	assertEpsilonEquals(this.reference.values(), values);
    }

}
