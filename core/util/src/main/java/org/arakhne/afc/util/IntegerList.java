/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.TreeSet;

/** This class represents a list of numbers.
 * The list is always sorted by number values.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class IntegerList implements SortedSet<Integer>, List<Integer> {
	
	/** This is the list of values.
	 * The value segments are represented by 2 values: the first value
	 * and the last value of the segment.
	 */
	private int[] values;
	
	/** This is the theorycal size of this list, ie. the count of integers.
	 */
	private int size;

	/** Create a list with the specified values.
	 */
	public IntegerList() {
		this.values = null;
		this.size = 0;
	}

	/** Create a list with the specified value.
	 * 
	 * @param value is the initial value. 
	 */
	public IntegerList(int value) {
		this.values = new int[] {value, value};
		this.size = 1;
	}

	/** Create a list with the specified values.
	 * 
	 * @param start is the first initial value
	 * @param end is the last initial value
	 */
	public IntegerList(int start, int end) {
		int s = start;
		int e = end;
		if (s>e) {
			int t = s;
			s = e;
			e = t;
		}
		this.values = new int[] {s, e};
		this.size = e-s+1;
	}
	
	/** Create a list with the specified values.
	 * 
	 * @param c is the list of initial values
	 */
	public IntegerList(Collection<? extends Integer> c) {
		this.values = null;
		this.size = 0;
		addAll(c);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append('[');
		if (this.values!=null) {
			for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
				if (idxStart>0) buffer.append(',');
				if (this.values[idxStart]==this.values[idxStart+1]) {
					buffer.append(this.values[idxStart]);
				}
				else if ((this.values[idxStart]+1)==this.values[idxStart+1]) {
					buffer.append(this.values[idxStart]);
					buffer.append(',');
					buffer.append(this.values[idxStart+1]);
				}
				else {
					buffer.append(this.values[idxStart]);
					buffer.append('-');
					buffer.append(this.values[idxStart+1]);
				}
			}
		}
		buffer.append(']');
		return buffer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Comparator<? super Integer> comparator() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer first() {
		if (this.values==null) throw new NoSuchElementException();
		return Integer.valueOf(this.values[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SortedSet<Integer> headSet(Integer toElement) {
		TreeSet<Integer> theset = new TreeSet<>();
		if (this.values!=null) {

			for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
				// The next segment is greater or equal to the given bound
				if ((this.values[idxStart]>=toElement)) break;
				
				// The given bound is inside the value segment
				for(int intToAdd=this.values[idxStart]; (intToAdd<toElement)&&(intToAdd<=this.values[idxStart+1]); ++intToAdd) {
					theset.add(intToAdd);
				}
			}
			
		}
		return theset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer last() {
		if (this.values==null) throw new NoSuchElementException();
		return this.values[this.values.length-1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
		TreeSet<Integer> theset = new TreeSet<>();
		if (this.values!=null) {

			// Search for the first matching segment
			int min, max;
			int firstSegment = -1;
			
			for(int idxSegment=0; firstSegment==-1 && idxSegment<this.values.length; idxSegment+=2) {
				max = this.values[idxSegment+1];
				if (fromElement.compareTo(max)<=0) {
					firstSegment = idxSegment;
				}
			}

			if (firstSegment!=-1) {
				// Go through the segments
				for(int idxSegment=firstSegment; idxSegment<this.values.length; idxSegment+=2) {
					min = this.values[idxSegment];
					max = this.values[idxSegment+1];
					if (toElement.compareTo(min)<=0) {
						idxSegment = this.values.length;
					}
					else {
						for(int value=min; toElement.compareTo(value)>0 && value<=max; ++value) {
							assert(toElement.compareTo(value)>0);
							if (fromElement.compareTo(value)<=0) theset.add(value);
						}
					}
				}
			}
			
		}
		return theset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SortedSet<Integer> tailSet(Integer fromElement) {
		TreeSet<Integer> theset = new TreeSet<>();
		if (this.values!=null) {

			for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
				// The next segment is lower to the given bound
				if ((this.values[idxStart+1]<fromElement)) continue;
				
				// The given bound is inside the value segment
				if (fromElement>=this.values[idxStart]) {
					for(int intToAdd=fromElement; intToAdd<=this.values[idxStart+1]; ++intToAdd) {
						theset.add(intToAdd);
					}
				}

				// The given bound is inside the value segment
				if (fromElement<this.values[idxStart]) {
					for(int intToAdd=this.values[idxStart]; intToAdd<=this.values[idxStart+1]; ++intToAdd) {
						theset.add(intToAdd);
					}
				}
			}
			
		}
		return theset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(Integer e) {
		if (this.values==null) {
			this.values = new int[] { e, e };
			this.size = 1;
		}
		else {
			int min, max;
			int f, l, c;
			
			f = 0;
			l = getSegmentCount() - 1;
			
			while (f<=l) {
				c = (f+l)/2;
				min = this.values[c*2];
				max = this.values[c*2+1];
				
				if (e.compareTo(min)>=0 && e.compareTo(max)<=0) return false;
				
				if (e.compareTo(min)<0) {
					l = c - 1;
				}
				else {
					f = c + 1;
				}
			}

			int index = f * 2;
			boolean mergeWithPrevious = (index>0 && e.compareTo(this.values[index-1]+1)==0);
			boolean mergeWithNext = (index<this.values.length && e.compareTo(this.values[index]-1)==0);

			++this.size;

			if (mergeWithPrevious && mergeWithNext) {
				this.values[index-1] = this.values[index+1];
				int[] nValues = new int[this.values.length-2];
				System.arraycopy(this.values, 0, nValues, 0, index);
				System.arraycopy(this.values, index+2, nValues, index, this.values.length - index - 2);
				this.values = nValues;
			}
			else if (mergeWithPrevious) {
				this.values[index-1] = e.intValue();
			}
			else if (mergeWithNext) {
				this.values[index] = e.intValue();
			}
			else {
				// Create a new segment
				int[] nValues = new int[this.values.length+2];
				System.arraycopy(this.values, 0, nValues, 0, index);
				nValues[index] = nValues[index+1] = e.intValue();
				System.arraycopy(this.values, index, nValues, index+2, this.values.length - index);
				this.values = nValues;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		boolean changed = false;
		for(Integer value : c) {
			changed |= add(value);
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.values = null;
		this.size = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		if ((this.values!=null)&&(o instanceof Number)) {
			int e = ((Number)o).intValue();
			
			for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
				if (e<this.values[idxStart]) return false;
				if ((e>=this.values[idxStart])&&(e<=this.values[idxStart+1]))
					return true;
			}
			
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		if (this.values==null) return false;
		
		SortedSet<Integer> elements = new TreeSet<>();
		for (Object o : c) {
			if (o instanceof Number) {
				elements.add(((Number)o).intValue());
			}
		}
		
		int idxStart = 0;
		
		for (Integer e : elements) {
			for (;idxStart<this.values.length-1; idxStart+=2) {
				if (e<this.values[idxStart]) return false;
				if ((e>=this.values[idxStart])&&(e<=this.values[idxStart+1])) {
					break;
				}
			}
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.size==0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new IntegerListIterator();
	}

    /**
     * Returns an iterator over the segments in this list in proper sequence.
     *
     * @return an iterator over the segments in this list in proper sequence
     */
	public Iterator<IntegerSegment> segmentIterator() {
		return new SegmentIterator();
	}

    /**
     * Returns an iterable object over the segments in this list in proper sequence.
     *
     * @return an iterable object over the segments in this list in proper sequence
     */
	public Iterable<IntegerSegment> toSegmentIterable() {
		return new Iterable<IntegerSegment>() {
			@Override
			public Iterator<IntegerSegment> iterator() {
				return new SegmentIterator();
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		if ((this.values!=null)&&(o instanceof Number)) {
			int e = ((Number)o).intValue();
			int segmentIndex = getSegmentIndexFor(e);
			if (segmentIndex>=0 && removeElementInSegment(segmentIndex,e))
				return true;
		}
		return false;
	}
	
	/** Remove the <var>element</var> in the segment starting at index
	 * <var>segmentIndex</var>.
	 * 
	 * @param segmentIndex is the index of the segment from which the
	 * element must be removed.
	 * @param element is the element to remove.
	 * @return <code>true</code> if the element was removed, otherwhise <code>false</code>
	 */
	protected boolean removeElementInSegment(int segmentIndex, int element) {
		if ((element==this.values[segmentIndex])&&(element==this.values[segmentIndex+1])) {
			// Remove the segment
			if (this.values.length==2) {
				this.values = null;
				this.size = 0;
			}
			else {
				int[] newTab = new int[this.values.length-2];
				System.arraycopy(this.values, 0, newTab, 0, segmentIndex);
				System.arraycopy(this.values, segmentIndex+2, newTab, segmentIndex, newTab.length-segmentIndex);
				this.values = newTab;
				--this.size;
				newTab = null;
			}
			return true;
		}
		
		if ((element>=this.values[segmentIndex])&&(element<=this.values[segmentIndex+1])) {
			if (element==this.values[segmentIndex]) {
				// Move the lower bound
				this.values[segmentIndex]++;
				--this.size;
			}
			else if (element==this.values[segmentIndex+1]) {
				// Move the upper bound
				this.values[segmentIndex+1]--;
				--this.size;
			}
			else {
				// Split the segment
				int[] newTab = new int[this.values.length+2];
				System.arraycopy(this.values, 0, newTab, 0, segmentIndex+1);
				System.arraycopy(this.values, segmentIndex+1, newTab, segmentIndex+3, newTab.length-segmentIndex-3);
				newTab[segmentIndex+1] = element-1;
				newTab[segmentIndex+2] = element+1;
				this.values = newTab;
				--this.size;
				newTab = null;
			}
			
			return true;
		}
		
		return false;
	}

	/** Remove the <var>element</var> in the segment starting at index
	 * <var>segmentIndex</var>.
	 * 
	 * @param segmentIndex is the index of the segment to remove.
	 * @return <code>true</code> if the element was removed, otherwhise <code>false</code>
	 */
	protected boolean removeSegment(int segmentIndex) {
		if ((this.values==null)||
			(segmentIndex<0)||
			(segmentIndex>=this.values.length-1)) return false;
		
		if (this.values.length==2) {
			this.values = null;
			this.size = 0;
		}
		else {
			int count = this.values[segmentIndex+1] - this.values[segmentIndex] + 1;
			int[] newTab = new int[this.values.length-2];
			System.arraycopy(this.values, 0, newTab, 0, segmentIndex);
			System.arraycopy(this.values, segmentIndex+2, newTab, segmentIndex, this.values.length-segmentIndex);
			this.values = newTab;
			this.size -= count;
			newTab = null;
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for(Object o : c) {
			changed |= remove(o);
		}
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		SortedSet<Integer> theset = toSortedSet();
		boolean changed = theset.retainAll(c);
		if (changed) {
			set(theset);
		}
		return changed;
	}
	
	/** Set the content of this list from the specified collection.
	 * 
	 * @param collection is the values to pout inside this list.
	 */
	public void set(SortedSet<? extends Number> collection) {
		this.values = null;
		this.size = 0;
		
		for (Number number : collection) {
			int e = number.intValue();

			if ((this.values!=null)&&(e==this.values[this.values.length-1]+1)) {
				// Same group
				this.values[this.values.length-1]++;
				++this.size;
			}
			if ((this.values!=null)&&(e>this.values[this.values.length-1]+1)) {
				// Create a new group
				int[] newTab = new int[this.values.length+2];
				System.arraycopy(this.values, 0, newTab, 0, this.values.length);
				newTab[newTab.length-2] = newTab[newTab.length-1] = e;
				this.values = newTab;
				++this.size;
				newTab = null;
			}
			else if (this.values==null) {
				// Add the first group
				this.values = new int[] { e, e };
				this.size = 1;
			}
			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.size;
	}

    /**
     * Replies the count of segments.
     * 
     * @return the count of segments.
     */
	protected int getSegmentCount() {
		return (this.values==null) ? 0 : (this.values.length / 2);
	}

    /**
     * Replies the last value of a segment.
     * 
     * @param idxSegment is the index of the segment. It must be a multiple of 2.
     * @return the last value in the segment.
     */
	protected int getLastValueOnSegment(int idxSegment) {
		assert(idxSegment%2==0);
		if (this.values==null) throw new IndexOutOfBoundsException();
		return this.values[idxSegment+1];
	}

    /**
     * Replies the first value of a segment.
     * 
     * @param idxSegment is the index of the segment. It must be a multiple of 2.
     * @return the first value in the segment
     */
	protected int getFirstValueOnSegment(int idxSegment) {
		assert(idxSegment%2==0);
		if (this.values==null) throw new IndexOutOfBoundsException();
		return this.values[idxSegment];
	}

    /**
     * Replies the segment index for the specified value.
     *
     * @param value is the value to search for.
     * @return the index or <code>-1</code>.  It is a multiple of 2.
     */
	protected int getSegmentIndexFor(int value) {
		if (this.values!=null) {
			int min, max;
			int f, l, c;
			
			f = 0;
			l = getSegmentCount() - 1;
			
			while (f<=l) {
				c = (f+l)/2;
				min = this.values[c*2];
				max = this.values[c*2+1];
				
				if (value>=min && value<=max) {
					return c*2;
				}
				
				if (value<min) {
					l = c - 1;
				}
				else {
					f = c + 1;
				}
			}
		}
		
		return -1;
	}

    /**
     * Replies the segment index for the specified value.
     * <p>
     * The given array must be pre-allocated with at least 2 cells.
     * The first cell will the the index of the segment. The
     * second cell will be the first integer value.
     *
     * @param offset is the number of integer values to skip from the
     * begining of the value set.
     * @param tofill is the 2-cell array to fill
     * @return <code>true</code> on success, <code>false</code> otherwise.
     */
	protected boolean get(int offset, int[] tofill) {
		if (this.values!=null) {
			int idxTab = 0;
			for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
				for(int n=this.values[idxStart]; n<=this.values[idxStart+1]; ++n) {
					if (offset==idxTab) {
						tofill[0] = idxStart;
						tofill[1] = n;
						return true;
					}
					++idxTab;
				}
			}
		}
		tofill[0] = -1;
		tofill[1] = 0;
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		Object[] tab = new Object[this.size];
		if (this.values!=null) {
			int idxTab = 0;
			for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
				for(int n=this.values[idxStart]; n<=this.values[idxStart+1]; ++n) {
					tab[idxTab++] = n;
				}
			}
		}
		return tab;
	}

	/**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must
     * allocate a new array even if this list is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this list in proper
     *         sequence
     * @see Arrays#asList(Object[])
     */
	public int[] toIntArray() {
		int[] tab = new int[this.size];
		if (this.values!=null) {
			int idxTab = 0;
			for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
				for(int n=this.values[idxStart]; n<=this.values[idxStart+1]; ++n) {
					tab[idxTab++] = n;
				}
			}
		}
		return tab;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		Class<T> clazz = (Class<T>)a.getClass().getComponentType();
		if (!clazz.isAssignableFrom(Integer.class))
			throw new ArrayStoreException();
		T[] tab = a;
		if (tab.length<this.size) {
			tab = (T[])Array.newInstance(clazz, this.size);
		}
		
		if (this.values!=null) {
			int idxTab = 0;
			for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
				for(Integer n=this.values[idxStart]; n<=this.values[idxStart+1]; ++n) {
					tab[idxTab++] = (T)n;
				}
			}
		}
		
		return tab;
	}
	
	/** Replies the complete list of stored integers.
	 * 
	 * @return the set of values.
	 */
	public SortedSet<Integer> toSortedSet() {
		SortedSet<Integer> theset = new TreeSet<>();
		if (this.values!=null) {
			for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
				for(int n=this.values[idxStart]; n<=this.values[idxStart+1]; ++n) {
					theset.add(n);
				}
			}
		}
		return theset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void add(int index, Integer element) {
		add(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean addAll(int index, Collection<? extends Integer> c) {
		return addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer get(int index) {
		if (this.values==null) throw new IndexOutOfBoundsException(Integer.toString(index));
		
		int firstIndex = 0;
		
		for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
			int endIndex = this.values[idxStart+1] - this.values[idxStart] + firstIndex;
			if ((index>=firstIndex)&&(index<=endIndex)) {
				return this.values[idxStart]+index-firstIndex;
			}
			firstIndex = endIndex + 1;
		}
		
		throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(Object o) {
		if (o instanceof Number) {
			int e = ((Number)o).intValue();
			
			if (this.values!=null) {
				int idx = 0;
				
				for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
					for(int n=this.values[idxStart]; n<=this.values[idxStart+1]; ++n) {
						if (n==e) return idx;
						++idx;
					}
				}
			}
			
			return -1;
		}
		if (o==null)
			throw new NullPointerException();
		throw new ClassCastException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int lastIndexOf(Object o) {
		if (o instanceof Number) {
			int e = ((Number)o).intValue();
			
			if (this.values!=null) {
				int idx = this.size-1;
				
				for(int idxStart=this.values.length-2; idxStart>=0; idxStart-=2) {
					for(int n=this.values[idxStart+1]; n>=this.values[idxStart]; --n) {
						if (n==e) return idx;
						--idx;
					}
				}
			}
			
			return -1;
		}
		if (o==null)
			throw new NullPointerException();
		throw new ClassCastException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<Integer> listIterator() {
		return new IntegerListIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<Integer> listIterator(int index) {
		return new IntegerListIterator(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer remove(int index) {
		if (this.values==null) throw new IndexOutOfBoundsException(Integer.toString(index));
		
		int firstIndex = 0;
		
		for(int idxStart=0; idxStart<this.values.length-1; idxStart+=2) {
			int endIndex = this.values[idxStart+1] - this.values[idxStart] + firstIndex;
			if ((index>=firstIndex)&&(index<=endIndex)) {
				int elementToRemove = this.values[idxStart]+index-firstIndex;
				if (!removeElementInSegment(idxStart, elementToRemove)) {
					throw new IndexOutOfBoundsException(Integer.toString(index));
				}
				return elementToRemove;
			}
			firstIndex = endIndex + 1;
		}
		
		throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer set(int index, Integer element) {
		Integer oldValue = remove(index);
		add(element);
		return oldValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> subList(int fromIndex, int toIndex) {
		List<Integer> theList = new ArrayList<>();
		if (this.values!=null) {

			// Search for the first matching segment
			int min, max, nb;
			int firstSegment = -1;
			int idxValue = 0;
			
			for(int idxSegment=0; firstSegment==-1 && idxSegment<this.values.length; idxSegment+=2) {
				min = this.values[idxSegment];
				max = this.values[idxSegment+1];
				nb = max - min + 1;
				if (fromIndex<idxValue+nb) {
					firstSegment = idxSegment;
				}
				else {
					idxValue += nb;
				}
			}

			if (firstSegment!=-1) {
				// Go through the segments
				for(int idxSegment=firstSegment; idxSegment<this.values.length; idxSegment+=2) {
					min = this.values[idxSegment];
					max = this.values[idxSegment+1];
					if (toIndex<=idxValue) {
						idxSegment = this.values.length;
					}
					else {
						for(int value=min; idxValue<toIndex && value<=max; ++value) {
							if (fromIndex<=idxValue) theList.add(value);
							++idxValue;
						}
					}
				}
			}
			
		}
		return theList;
	}

	@Override
	public Spliterator<Integer> spliterator() {
		return SortedSet.super.spliterator();
	}

	/** This class represents an iterator on lists of integers.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class IntegerListIterator implements ListIterator<Integer> {

		private final int offset;
		private int tabIndex;
		private int segmentIndex;
		private int number;
		
		/**
		 * @param startingIndex
		 */
		public IntegerListIterator(int startingIndex) {
			this.offset = startingIndex;
			int lsize = size();
			if ((startingIndex>=0)&&(startingIndex<lsize)) {
				this.tabIndex = startingIndex;
				int[] tab = new int[2];
				get(startingIndex, tab);
				this.segmentIndex = tab[0];
				this.number = tab[1];
			}
			else {
				this.tabIndex = -1;
				this.segmentIndex = -1;
			}
		}

		/**
		 * 
		 */
		public IntegerListIterator() {
			this.offset = 0;
			if (isEmpty()) {
				this.tabIndex = -1;
				this.segmentIndex = -1;
			}
			else {
				this.tabIndex = 0;
				this.segmentIndex = 0;
				this.number = getFirstValueOnSegment(this.segmentIndex);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return ((this.tabIndex>=this.offset)&&(this.tabIndex<size()));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int nextIndex() {
			int lsize = size();
			if ((this.tabIndex>=this.offset)&&(this.tabIndex<lsize)) {
				return this.tabIndex;
			}
			return lsize;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Integer next() {
			if ((this.tabIndex<this.offset)||(this.tabIndex>=size()))
				throw new NoSuchElementException();
			
			int n = this.number;
			int lsize = size();
			
			++this.tabIndex;
			if ((this.tabIndex>=this.offset)&&(this.tabIndex<lsize)) {
				if (this.number==getLastValueOnSegment(this.segmentIndex)) {
					this.segmentIndex += 2;
					this.number = getFirstValueOnSegment(this.segmentIndex);
				}
				else {
					++this.number;
				}
			}
			
			return n;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasPrevious() {
			int idx = this.tabIndex - 1;
			return ((idx>=this.offset)&&(idx<size()));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int previousIndex() {
			int idx = this.tabIndex - 1;
			return ((idx>=this.offset)&&(idx<size())) ? idx : -1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Integer previous() {
			int idx = this.tabIndex - 1;
			if ((idx<this.offset)||(idx>=size()))
				throw new NoSuchElementException();

			int n = get(idx);
			int lsize = size();
			
			--this.tabIndex;
			if ((this.tabIndex>=this.offset)&&(this.tabIndex<lsize)) {
				if (this.number==getFirstValueOnSegment(this.segmentIndex)) {
					this.segmentIndex -= 2;
					this.number = getLastValueOnSegment(this.segmentIndex);
				}
				else {
					--this.number;
				}
			}
			
			return n;			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void add(Integer e) {
			if (e==this.number) return;
			if (IntegerList.this.add(e)) {
				if (e<this.number) {
					++this.tabIndex;
					this.segmentIndex = getSegmentIndexFor(this.number);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(Integer e) {
			add(e);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			int lsize = size();
			if ((this.tabIndex>1)&&(this.tabIndex<lsize)) {
				IntegerList.this.remove(this.tabIndex-1);
				--this.tabIndex;
				this.segmentIndex = getSegmentIndexFor(this.number);
			}
			throw new IllegalStateException();
		}

	} /* class IntegerListIterator */
	
	/** This class describes a integers' segment.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public class IntegerSegment {

		/** First value in the segment.
		 */
		public final int first;
		/** Last value in the segment.
		 */
		public final int last;
		
		/**
		 * @param first
		 * @param last
		 */
		IntegerSegment(int first, int last) {
			this.first = first;
			this.last = last;
		}
		
	} /* class IntegerSegment */
	
	/** Iterator on segments.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class SegmentIterator implements Iterator<IntegerSegment> {

		private int index;
		private boolean removable;

		/**
		 */
		public SegmentIterator() {
			this.index = 0;
			this.removable = false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public boolean hasNext() {
			return this.index<IntegerList.this.values.length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public IntegerSegment next() {
			if (this.index>=IntegerList.this.values.length)
				throw new ConcurrentModificationException();
			
			try {
				int first = IntegerList.this.values[this.index];
				int last = IntegerList.this.values[this.index+1];
				this.index += 2;
				this.removable = true;
				return new IntegerSegment(first,last);
			}
			catch(IndexOutOfBoundsException exception) {
				throw new NoSuchElementException();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public void remove() {
			if (this.removable) {
				int idx = this.index - 2;
				if (idx<0 || idx>=IntegerList.this.values.length)
					throw new ConcurrentModificationException();
				removeSegment(idx);
				this.removable = false;
			}
			else
				throw new NoSuchElementException();
		}
		
	}

}