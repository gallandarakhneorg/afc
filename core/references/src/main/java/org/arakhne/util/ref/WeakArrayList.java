/* 
 * $Id$
 * 
 * Copyright (C) 2005-2009 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
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

package org.arakhne.util.ref;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A array-based <tt>List</tt> implementation with <em>weak keys</em>.
 * An entry in a <tt>WeakArrayList</tt> will automatically be removed when
 * it is no longer in ordinary use.
 *
 * <p> The behavior of the <tt>WeakArrayList</tt> class depends in part upon
 * the actions of the garbage collector, so several familiar (though not
 * required) <tt>List</tt> invariants do not hold for this class.  Because
 * the garbage collector may discard values at any time, a
 * <tt>WeakArrayList</tt> may behave as though an unknown thread is silently
 * removing entries.  In particular, even if you synchronize on a
 * <tt>WeakArrayList</tt> instance and invoke none of its mutator methods, it
 * is possible for the <tt>size</tt> method to return smaller values over
 * time, for the <tt>isEmpty</tt> method to return <tt>false</tt> and
 * then <tt>true</tt>, for the <tt>contains</tt> method to return
 * <tt>true</tt> and later <tt>false</tt> for a given value, for the
 * <tt>get</tt> method to return a value for a given key but later return
 * <tt>null</tt>, for the <tt>add</tt> method to return
 * <tt>null</tt> and the <tt>remove</tt> method to return
 * <tt>false</tt> for a value that previously appeared to be in the list.
 * <p>
 * If this map does not use a "deep expurge" of the released references,
 * it could contains <code>null</code> values that corresponds to
 * values that are released by the garbage collector. If a "deep expurge"
 * is used, all the values released by the garbage collector will be
 * removed from the map.
 * <p>
 * "Deep expurge" consumes much more time that "No deep expurge". This is the
 * reason why this feature is not activated by default.
 * <p>
 * The "deep expurge" feature was added to fix the uncoherent behavior
 * of the garbage collector which seems to not always enqueued the 
 * released values (sometimes the queue is empty even if a value was released).
 *
 * @param <T> is the type of the array's elements.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class WeakArrayList<T> extends AbstractList<T> {

	/** This value represents a null value given by the user.
	 */
	private static final Object NULL_VALUE = new Object();
	
	/** Replies the null value given by the user by the corresponding null object.
	 */
	@SuppressWarnings("unchecked")
	private static <T> T maskNull(T value) {
		return (value==null) ? (T)NULL_VALUE : value;
	}

	/** Replies the value given by the user.
	 */
	private static <T> T unmaskNull(T value) {
		return (value==NULL_VALUE) ? null : value;
	}

	private final transient ReferenceQueue<T> queue = new ReferenceQueue<T>();
	
	private Object[] data;
	
	private int size;
	
	private boolean enquedElement = false;
	
	private List<ReferenceListener> listeners = null;
	
    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param   initialCapacity   the initial capacity of the list
     * @exception IllegalArgumentException if the specified initial capacity
     *            is negative
     */
    public WeakArrayList(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+initialCapacity); //$NON-NLS-1$
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public WeakArrayList() {
    	this(10);
    }
    
    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public WeakArrayList(Collection<? extends T> c) {
    	this.data = new Object[c.size()];
    	this.size = this.data.length;
    	int i=0;
    	for (T t : c) {
			this.data[i] = createRef(t);
			i++;
		}
    }
    
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
    @Override
	@SuppressWarnings("unchecked")
	public String toString() {
    	StringBuilder buffer = new StringBuilder();
    	Reference<T> ref;
    	T obj;
    	for(int i=0; i<this.size; i++) {
    		ref = (Reference<T>)this.data[i];
    		if (this.data[i]==null) obj = null;
    		else obj = ref.get();
    		buffer.append('{');
    		buffer.append(obj==null ? null : obj.toString());
    		buffer.append('}');
    	}
    	return buffer.toString();
    }
	
    /** Create and replies the reference for the specified object.
     * 
     * @param obj is the object on which a weak reference must be attached.
     * @return the weak reference. 
     */
    private Reference<T> createRef(T obj) {
    	return new WeakReference<T>(maskNull(obj), this.queue);
    }
    
    /**
     * Increases the capacity of this <tt>WeakArrayList</tt> instance, if
     * necessary, to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     *
     * @param   minCapacity   the desired minimum capacity
     */
    public void ensureCapacity(int minCapacity) {
		this.modCount++;
		int oldCapacity = this.data.length;
		if (minCapacity > oldCapacity) {
			Object[] oldData = this.data;
			int newCapacity = (oldCapacity * 3)/2 + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			// minCapacity is usually close to size, so this is a win:
			this.data = Arrays.copyOf(oldData, newCapacity);
		}
    }

    /**
     * Trims the capacity of this <tt>WeakArrayList</tt> instance to be the
     * list's current size.  An application can use this operation to minimize
     * the storage of an <tt>WeakArrayList</tt> instance.
     */
    public void trimToSize() {
    	this.modCount++;
    	int oldCapacity = this.data.length;
    	if (this.size < oldCapacity) {
    		this.data = Arrays.copyOf(this.data, this.size);
    	}
    }
    
	/** Clean the references that was released.
	 * <p>
	 * Notifies the listeners if a reference was released.
	 * 
	 * @return the size
     */
	@SuppressWarnings("unchecked")
	public int expurge() {
        // clear out ref queue.
		while (this.queue.poll()!=null) {
			this.enquedElement = true;
		}
			
		int j;

		if (this.enquedElement) {
			// Clear the table
			Reference<? extends T> ref;
			j=0;
			for(int i=0; i<this.size; i++) {
				ref = (Reference<T>)this.data[i];
				if ((ref==null)||(ref.isEnqueued())||(ref.get()==null)) {
					if (ref!=null) ref.clear();
					this.data[i] = null;
				}
				else {
					if (i!=j) {
						this.data[j] = this.data[i];
						this.data[i] = null;
					}
					j++;
				}
			}
			this.enquedElement = false;
		}
		else {
			j = this.size;
		}
				
        // Allocation of array may have caused GC, which may have caused
        // additional entries to go stale.  Removing these entries from the
        // reference queue will make them eligible for reclamation.
		while (this.queue.poll()!=null) {
			this.enquedElement = true;
		}

		int oldSize = this.size;
		this.size = j;
		
		if (j<oldSize) {
			fireReferenceRelease(oldSize - j);
		}

		return this.size;
	}
    
    /** Verify if the specified index is inside the array.
     * 
     * @param index is the index totest
     * @param allowLast indicates if the last elements is assumed to be valid or not.
     */
    protected void assertRange(int index, boolean allowLast) {
    	int csize = expurge();
    	if (index<0)
    		throw new IndexOutOfBoundsException("invalid negative value: "+Integer.toString(index)); //$NON-NLS-1$
    	if ((allowLast)&&(index>csize))
    		throw new IndexOutOfBoundsException("index>"+csize+": "+Integer.toString(index)); //$NON-NLS-1$ //$NON-NLS-2$
    	if ((!allowLast)&&(index>=csize))
    		throw new IndexOutOfBoundsException("index>="+csize+": "+Integer.toString(index)); //$NON-NLS-1$ //$NON-NLS-2$
    }

	/** {@inheritDoc}
	 */
	@Override
	public int size() {
		return expurge();
	}

	/** {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T get(int index) {
		T value;
		do {
			assertRange(index,false);
			value = ((Reference<T>)this.data[index]).get();
		}
		while (value==null);
		return unmaskNull(value);
	}

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
	public T set(int index, T element) {
		T oldValue;
		Reference<T> ref;
		do {
			assertRange(index, false);
			ref = (Reference<T>)this.data[index];
			oldValue = ref.get();
		}
		while (oldValue==null);
		ref.clear();
		this.data[index] = createRef(element);
		this.modCount++;
		return unmaskNull(oldValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void add(int index, T element) {
		assertRange(index,true);
		ensureCapacity(this.size+1);
		System.arraycopy(this.data, index, this.data, index+1, this.size-index);
		this.data[index] = createRef(element);
		this.size++;
		this.modCount++;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	@Override
	public T remove(int index) {
		T oldValue;
		Reference<T> ref;
		do {
			assertRange(index, false);
			ref = (Reference<T>)this.data[index];
			oldValue = ref.get();
		}
		while (oldValue==null);
		ref.clear();
		System.arraycopy(this.data, index+1, this.data, index, this.size-index-1);
		this.data[this.size-1] = null;
		this.size--;
		this.modCount++;
		return unmaskNull(oldValue);
    }
    
    /** Add listener on reference's release.
     * 
     * @param listener
     */
    public void addReferenceListener(ReferenceListener listener) {
    	if (this.listeners==null) {
    		this.listeners = new LinkedList<ReferenceListener>();
    	}
		List<ReferenceListener> list = this.listeners;
    	synchronized(list) {
    		list.add(listener);
    	}
    }

    /** Remove listener on reference's release.
     * 
     * @param listener
     */
    public void removeReferenceListener(ReferenceListener listener) {
		List<ReferenceListener> list = this.listeners;
    	if (list!=null) {
        	synchronized(list) {
        		list.remove(listener);
            	if (list.isEmpty()) this.listeners = null;
        	}
    	}
    }
    
    /**
     * Fire the reference release event.
     * 
     * @param released is the count of released objects.
     */
    protected void fireReferenceRelease(int released) {
		List<ReferenceListener> list = this.listeners;
    	if (list!=null && !list.isEmpty()) {
    		for(ReferenceListener listener : list) {
    			listener.referenceReleased(released);
    		}
    	}
    }
    	
}
