/* 
 * $Id$
 * 
 * Copyright (c) 2005-11, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.tree.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.tree.TreeDataEvent;
import org.arakhne.afc.math.tree.TreeNode;
import org.arakhne.afc.math.tree.TreeNodeListener;


/**
 * This is the very generic implementation of a tree node which
 * does not made any implementation choice about how this
 * node is related to its parent node.
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractParentlessTreeNode<D,N extends AbstractParentlessTreeNode<D,N>> implements TreeNode<D,N>, Serializable {

	private static final long serialVersionUID = 2525868013637426514L;

	/** Replies the minimal value from the integer array.
	 * 
	 * @param array
	 * @return the minimum value.
	 */
	protected static int minInteger(int... array) {
		int min = array[0];
		for(int i=0; i<array.length; ++i) {
			if (min>array[i]) min = array[i];
		}
		return min;
	}

	/** Replies the maximal value from the integer array.
	 * 
	 * @param array
	 * @return the maximum value.
	 */
	protected static int maxInteger(int... array) {
		int max = array[0];
		for(int i=0; i<array.length; ++i) {
			if (max<array[i]) max = array[i];
		}
		return max;
	}

	/** By default, does tree nodes use a linked list or not?
	 */
	public static final boolean DEFAULT_LINK_LIST_USE = false;
	
	/** By default, does the user data are copied or not?
	 */
	public static final boolean DEFAULT_COPY_USER_DATA = true;

	/** List of the event listeners on the tree.
	 */
	protected transient List<TreeNodeListener> nodeListeners = null;
	
	/** Associated user data.
	 */
	private List<D> datas;
	
	/** Indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 */
	private final boolean linkedList;
	
	/** Temporary count of child nodes that are not null.
	 * 
	 * @see #getNotNullChildCount()
	 */
	protected int notNullChildCount = 0;
	
	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 */
	public AbstractParentlessTreeNode(boolean useLinkedList) {
		this.linkedList = useLinkedList;
		if (this.linkedList)
			this.datas = new LinkedList<D>();
		else
			this.datas = new ArrayList<D>();
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param copyDataCollection indicates if the given data collection is copied
	 * if <code>true</code> or the inner data collection will be the given
	 * collection itself if <code>false</code>.
	 * @param idata are the initial user data
	 */
	public AbstractParentlessTreeNode(boolean useLinkedList, boolean copyDataCollection, List<D> idata) {
		this.linkedList = useLinkedList;
		if (idata != null){
			if (!copyDataCollection) {
				this.datas = idata;
			}
			else {
				if (this.linkedList)
					this.datas = new LinkedList<D>(idata);
				else
					this.datas = new ArrayList<D>(idata);
			}
		}
		else {
			if (this.linkedList)
				this.datas = new LinkedList<D>();
			else
				this.datas = new ArrayList<D>();
		}
	}
	
	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param idata are the initial user data
	 */
	public AbstractParentlessTreeNode(boolean useLinkedList, Collection<D> idata) {
		this.linkedList = useLinkedList;
		if (idata != null){
			if (this.linkedList)
				this.datas = new LinkedList<D>(idata);
			else
				this.datas = new ArrayList<D>(idata);
		}
		else {
			if (this.linkedList)
				this.datas = new LinkedList<D>();
			else
				this.datas = new ArrayList<D>();
		}
	}

	/**
	 * @param useLinkedList indicates if a linked list must be used to store the data.
	 * If <code>false</code>, an ArrayList will be used.
	 * @param idata are the initial user data
	 */
	public AbstractParentlessTreeNode(boolean useLinkedList, D idata) {
		this.linkedList = useLinkedList;
		if (idata != null){
			if (this.linkedList)
				this.datas = new LinkedList<D>();
			else
				this.datas = new ArrayList<D>();
			this.datas.add(idata);
		}
		else {
			if (this.linkedList)
				this.datas = new LinkedList<D>();
			else
				this.datas = new ArrayList<D>();
		}
	}
	
	/** Cast this node to N.
	 * 
	 *  @return <code>this</code>
	 */
	@SuppressWarnings("unchecked")
	public N toN() {
		return (N)this;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return isLeaf() && (getUserDataCount()==0);
	}


	/** {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append('[');
		if (this.datas!=null) {
			for (D data : this.datas) {
				if (data==null)
					buffer.append("null"); //$NON-NLS-1$
				else
					buffer.append(data.toString());
			}
		}
		buffer.append(']');
		return buffer.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public D[] getAllUserData(D[] a) {
		if (this.datas==null) return null;
		return this.datas.toArray(a);
	}
	
	/** Replies the internal data structure which is storing user datas.
	 * <p>
	 * Use with caution because any change applied outside
	 * {@link AbstractParentlessTreeNode} will not be taken into
	 * account (no event...).
	 * 
	 * @return the internal data structure
	 */
	protected final List<D> getInternalDataStructureForUserData() {
		return this.datas;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public List<D> getAllUserData() {
		return new DataCollection();
	}

	/** {@inheritDoc}
	 */
	@Override
	public D getUserData() {
		if ((this.datas==null)||(this.datas.size()==0)) return null;
		return this.datas.get(0);
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getUserDataCount() {
		return (this.datas==null) ? 0 : this.datas.size();
	}

	/** {@inheritDoc}
	 */
	@Override
	public D getUserDataAt(int index) throws IndexOutOfBoundsException {
		if ((this.datas==null)|| (index<0)|| (index>=this.datas.size())) {
			throw new IndexOutOfBoundsException();
		}
		return this.datas.get(index);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean addUserData(Collection<? extends D> data) {
		if ((data==null)||(data.size()==0)) return false;
		
		if (this.datas==null) {
			if (this.linkedList)
				this.datas = new LinkedList<D>(data);
			else
				this.datas = new ArrayList<D>(data);
		}
		else if (!this.datas.addAll(data))
			return false;
		
		firePropertyDataChanged(null,data);
		
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final boolean addUserData(int index, Collection<? extends D> data) {
		if ((data==null)||(data.size()==0)) return false;

		if (this.datas==null) {
			if (this.linkedList)
				this.datas = new LinkedList<D>();
			else
				this.datas = new ArrayList<D>();
		}

		if (this.datas.addAll(index, data)) {
			firePropertyDataChanged(null,data);
			return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final boolean addUserData(D data) {
		return addUserData(Collections.singleton(data));
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void addUserData(int index, D data) {
		addUserData(index, Collections.singleton(data));
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean removeUserData(Collection<D> data) {
		if ((data==null)||(data.size()==0)||(this.datas==null)) return false;
		if (this.datas.removeAll(data)) {
			if (this.datas.size()==0)
				this.datas = null;
			firePropertyDataChanged(data, null);			
			return true;
		}
		return false;
	}
 
	/** {@inheritDoc}
	 */
	@Override
	public final D removeUserData(int index) {
		if (this.datas==null) throw new IndexOutOfBoundsException();
		D removedElement = this.datas.remove(index);
		if (removedElement!=null) {
			if (this.datas.size()==0)
				this.datas = null;
			firePropertyDataChanged(Collections.singleton(removedElement), null);
		}
		return removedElement;
	}


	/** {@inheritDoc}
	 */
	@Override
	public final boolean removeUserData(D data) {
		return removeUserData(Collections.singleton(data));
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeAllUserData() {
		if (this.datas!=null) {
			List<D> oldData = this.datas;
			this.datas = null;
			firePropertyDataChanged(oldData, null);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setUserData(Collection<D> data) {
		if ((data==null)&&(this.datas==null)) return false;
		
		List<D> oldData = this.datas;
		
		if ((data==null)||(data.size()==0)) {
			this.datas = null;
		}
		else {
			this.datas = new ArrayList<D>(data);
		}
		
		firePropertyDataChanged(oldData,data);
		
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final boolean setUserData(D data) {
		return setUserData(Collections.singleton(data));
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setUserDataAt(int index, D data) throws IndexOutOfBoundsException {
		int count= (this.datas==null) ? 0 : this.datas.size();
		
		if ((index<0)|| (index>count)) {
			throw new IndexOutOfBoundsException();
		}
		
		if (data!=null) {
			D oldData = null;
			if (index<count) {
				oldData = this.datas.get(index);
				this.datas.set(index,data);
			}
			else {
				if (this.datas==null) {
					if (this.linkedList)
						this.datas = new LinkedList<D>();
					else
						this.datas = new ArrayList<D>();
				}
				this.datas.add(data);
			}
			firePropertyDataChanged(oldData==null ? null : Collections.singleton(oldData), Collections.singleton(data));
			return true;
		} else if (index<count) {
			D oldData = this.datas.get(index);
			this.datas.remove(index);
			firePropertyDataChanged(oldData==null ? null : Collections.singleton(oldData), null);
			return true;
		}
		
		return false;
	}
	
	/** Fire the event for the changes of the user data associated to a node.
	 * 
	 * @param oldData is the list of old values
	 * @param newData is the list of new values
	 */
	protected final void firePropertyDataChanged(Collection<? extends D> oldData, Collection<? extends D> newData) {
		firePropertyDataChanged(new TreeDataEvent(this,oldData,newData,this.datas));
	}

	/** Fire the event for the changes of the user data associated to a node.
	 * 
	 * @param delta is the difference between the size of the data list before change
	 * and size after change.
	 */
	protected final void firePropertyDataChanged(int delta) {
		firePropertyDataChanged(new TreeDataEvent(this,delta,this.datas));
	}

	/** Fire the event for the changes of the user data associated to a node.
	 * 
	 * @param event is the event ot fire.
	 */
	void firePropertyDataChanged(TreeDataEvent event) {
		if (this.nodeListeners!=null) {
			for (TreeNodeListener listener : this.nodeListeners) {
				if (listener!=null) {
					listener.treeNodeDataChanged(event);
				}
			}
		}
		N parent = getParentNode();
		if (parent!=null) parent.firePropertyDataChanged(event);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void addTreeNodeListener(TreeNodeListener listener) {
		if (this.nodeListeners==null)
			this.nodeListeners = new ArrayList<TreeNodeListener>();
		this.nodeListeners.add(listener);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void removeTreeNodeListener(TreeNodeListener listener) {
		if (this.nodeListeners==null) return;
		this.nodeListeners.remove(listener);
		if (this.nodeListeners.isEmpty())
			this.nodeListeners = null;
	}

	/**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     *
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param   o the object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this object.
     */
	@Override
    public int compareTo(N o) {
    	return o.hashCode() - hashCode(); 
    }
    
	/** {@inheritDoc}
	 */
	@Override
	public int getDeepNodeCount() {
		int count = 1;
		for(int index=0; index<getChildCount(); ++index) {
			N child = getChildAt(index);
			if (child!=null)
				count += child.getDeepNodeCount();
		}
		return count;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public int getDeepUserDataCount() {
		int count = getUserDataCount();
		for(int index=0; index<getChildCount(); ++index) {
			N child = getChildAt(index);
			if (child!=null)
				count += child.getDeepUserDataCount();
		}
		return count;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final int[] getHeights() {
		List<Integer> list = new ArrayList<Integer>();
		getHeights(1, list);
		int[] heights = new int[list.size()];
		int idx = 0;
		for(Integer intObject : list) {
			heights[idx] = intObject.intValue();
			++idx;
		}
		list.clear();
		return heights;
	}

	/** Replies the heights of all the leaf nodes.
	 * The order of the heights is given by a depth-first iteration.
	 * 
	 * @param currentHeight is the current height of this node.
	 * @param heights is the list of heights to fill
	 */
	protected abstract void getHeights(int currentHeight, List<Integer> heights);
	
	/**
	 * This collection permits to store the data of a tree node
	 * and notify the tree node when the collection changed.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class DataCollection implements List<D>, Serializable {
		
		private static final long serialVersionUID = -8019401625554108057L;

		private final List<D> backgroundList;
		
		/**
		 * @param list
		 */
		public DataCollection(List<D> list) {
			this.backgroundList = list;
		}
		
		/**
		 */
		public DataCollection() {
			this(null);
		}

		/** {@inheritDoc}
		 */
		@Override
		public boolean add(D e) {
			if (this.backgroundList==null) return addUserData(e);
			if (this.backgroundList.add(e)) {
				firePropertyDataChanged(null, Collections.singleton(e));
				return true;
			}
			return false;
		}

		/** {@inheritDoc}
		 */
		@Override
		public void add(int index, D element) {
			if (this.backgroundList==null) {
				addUserData(index, element);
			}
			else {
				this.backgroundList.add(index, element);
				firePropertyDataChanged(null, Collections.singleton(element));
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addAll(Collection<? extends D> c) {
			if (this.backgroundList==null) return addUserData(c);
			if (this.backgroundList.addAll(c)) {
				firePropertyDataChanged(null, c);
				return true;
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean addAll(int index, Collection<? extends D> c) {
			if (this.backgroundList==null) return addUserData(index, c);
			if (this.backgroundList.addAll(index, c)) {
				firePropertyDataChanged(null, c);
				return true;
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clear() {
			if (this.backgroundList==null) {
				removeAllUserData();
			}
			else if (!this.backgroundList.isEmpty()) {
				List<D> removed = new ArrayList<D>(this.backgroundList);
				this.backgroundList.clear();
				firePropertyDataChanged(removed, null);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean contains(Object o) {
			if (this.backgroundList==null) {
				List<D> backList = getInternalDataStructureForUserData();
				if (backList==null) return false;
				return backList.contains(o);
			}
			return this.backgroundList.contains(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean containsAll(Collection<?> c) {
			if (this.backgroundList==null) {
				List<D> backList = getInternalDataStructureForUserData();
				if (backList==null) return false;
				return backList.containsAll(c);
			}
			return this.backgroundList.containsAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public D get(int index) {
			if (this.backgroundList==null) return getUserDataAt(index);
			return this.backgroundList.get(index);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int indexOf(Object o) {
			if (this.backgroundList==null) {
				List<D> backList = getInternalDataStructureForUserData();
				if (backList==null) return -1;
				return backList.indexOf(o);
			}
			return this.backgroundList.indexOf(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEmpty() {
			if (this.backgroundList==null) {
				List<D> backList = getInternalDataStructureForUserData();
				if (backList==null) return true;
				return backList.isEmpty();
			}
			return this.backgroundList.isEmpty();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int lastIndexOf(Object o) {
			if (this.backgroundList==null) {
				List<D> backList = getInternalDataStructureForUserData();
				if (backList==null) return -1;
				return backList.lastIndexOf(o);
			}
			return this.backgroundList.lastIndexOf(o);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		@SuppressWarnings("unchecked")
		public boolean remove(Object o) {
			if (this.backgroundList==null) {
				try {
					return removeUserData((D)o);
				}
				catch(ClassCastException _) {
					return false;
				}
			}
			if (this.backgroundList.remove(o)) {
				firePropertyDataChanged(Collections.singleton((D)o), null);
				return true;
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public D remove(int index) {
			if (this.backgroundList==null) return removeUserData(index);
			D oldElement = this.backgroundList.remove(index);
			if (oldElement!=null) {
				firePropertyDataChanged(Collections.singleton(oldElement), null);
			}
			return oldElement;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeAll(Collection<?> c) {
			boolean changed = false;
			for(Object o : c) {
				changed = remove(o) | changed;
			}
			return changed;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			if (this.backgroundList==null) return getUserDataCount();
			return this.backgroundList.size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object[] toArray() {
			if (this.backgroundList==null) {
				List<D> backList = getInternalDataStructureForUserData();
				if (backList==null) return new Object[0];
				return backList.toArray();
			}
			return this.backgroundList.toArray();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> T[] toArray(T[] a) {
			if (this.backgroundList==null) {
				List<D> backList = getInternalDataStructureForUserData();
				if (backList!=null) return backList.toArray(a);
				return a;
			}
			return this.backgroundList.toArray(a);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public D set(int index, D element) {
			if (this.backgroundList==null) setUserDataAt(index, element);
			D oldElement = this.backgroundList.set(index, element);
			firePropertyDataChanged(Collections.singleton(oldElement), Collections.singleton(element));
			return oldElement;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean retainAll(Collection<?> c) {
			List<D> removed = new ArrayList<D>();
			Iterator<D> iterator;
			if (this.backgroundList==null) {
				List<D> backList = getInternalDataStructureForUserData();
				if (backList==null)
					iterator = Collections.<D>emptyList().iterator();
				else
					iterator = backList.iterator();
			}
			else {
				iterator = this.backgroundList.iterator();
			}
			D data;
			while (iterator.hasNext()) {
				data = iterator.next();
				if (!c.contains(data)) {
					iterator.remove();
					removed.add(data);
				}
			}
			if (!removed.isEmpty()) {
				firePropertyDataChanged(removed, null);
				return true;
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<D> iterator() {
			return listIterator();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ListIterator<D> listIterator() {
			if (this.backgroundList==null) {
				List<D> backList = getInternalDataStructureForUserData();
				if (backList==null) return Collections.<D>emptyList().listIterator();
				return new DataIterator(backList.listIterator());
			}
			return new DataIterator(this.backgroundList.listIterator());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ListIterator<D> listIterator(int index) {
			return subList(index, size()).listIterator();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public List<D> subList(int fromIndex, int toIndex) {
			return new DataCollection(this.backgroundList.subList(fromIndex, toIndex));
		}

	} // class DataCollection
	
	/**
	 * This iterator permits to iterate on the data of a tree node
	 * and notify the tree node when the collection changed.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class DataIterator implements ListIterator<D> {
	
		private final ListIterator<D> backgroundIterator;
		private D removableElement = null;
		
		/**
		 * @param iterator
		 */
		public DataIterator(ListIterator<D> iterator) {
			this.backgroundIterator = iterator;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.backgroundIterator.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public D next() {
			return this.removableElement = this.backgroundIterator.next();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasPrevious() {
			return this.backgroundIterator.hasPrevious();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int nextIndex() {
			return this.backgroundIterator.nextIndex();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public D previous() {
			return this.removableElement = this.backgroundIterator.previous();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int previousIndex() {
			return this.backgroundIterator.previousIndex();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(D e) {
			D re = this.removableElement;
			this.removableElement = null;
			if (re==null) throw new NoSuchElementException();
			this.backgroundIterator.set(e);
			firePropertyDataChanged(Collections.singleton(re), Collections.singleton(e));
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			D re = this.removableElement;
			this.removableElement = null;
			if (re==null) throw new NoSuchElementException();
			this.backgroundIterator.remove();
			firePropertyDataChanged(Collections.singleton(re), null);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void add(D e) {
			this.backgroundIterator.add(e);
			firePropertyDataChanged(null, Collections.singleton(e));
		}

	} // class DataIterator
	
}