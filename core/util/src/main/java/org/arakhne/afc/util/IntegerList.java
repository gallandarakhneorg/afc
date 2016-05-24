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
@SuppressWarnings("checkstyle:methodcount")
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
		int theStart = start;
		int theEnd = end;
		if (theStart > theEnd) {
			final int tmp = theStart;
			theStart = theEnd;
			theEnd = tmp;
		}
		this.values = new int[] {theStart, theEnd};
		this.size = theEnd - theStart + 1;
	}

	/** Create a list with the specified values.
	 *
	 * @param collection is the list of initial values
	 */
	public IntegerList(Collection<? extends Integer> collection) {
		this.values = null;
		this.size = 0;
		addAll(collection);
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append('[');
		if (this.values != null) {
			for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
				if (idxStart > 0) {
					buffer.append(',');
				}
				if (this.values[idxStart] == this.values[idxStart + 1]) {
					buffer.append(this.values[idxStart]);
				} else if ((this.values[idxStart] + 1) == this.values[idxStart + 1]) {
					buffer.append(this.values[idxStart]);
					buffer.append(',');
					buffer.append(this.values[idxStart + 1]);
				} else {
					buffer.append(this.values[idxStart]);
					buffer.append('-');
					buffer.append(this.values[idxStart + 1]);
				}
			}
		}
		buffer.append(']');
		return buffer.toString();
	}

	@Override
	public Comparator<? super Integer> comparator() {
		return null;
	}

	@Override
	public Integer first() {
		if (this.values == null) {
			throw new NoSuchElementException();
		}
		return Integer.valueOf(this.values[0]);
	}

	@Override
	public SortedSet<Integer> headSet(Integer toElement) {
		final SortedSet<Integer> theset = new TreeSet<>();
		if (this.values != null) {
			for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
				// The next segment is greater or equal to the given bound
				if (this.values[idxStart] >= toElement) {
					break;
				}
				// The given bound is inside the value segment
				for (int intToAdd = this.values[idxStart]; (intToAdd < toElement)
						&& (intToAdd <= this.values[idxStart + 1]); ++intToAdd) {
					theset.add(intToAdd);
				}
			}
		}
		return theset;
	}

	@Override
	public Integer last() {
		if (this.values == null) {
			throw new NoSuchElementException();
		}
		return this.values[this.values.length - 1];
	}

	@Override
	public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
		final SortedSet<Integer> theset = new TreeSet<>();
		if (this.values != null) {

			// Search for the first matching segment
			int min;
			int max;
			int firstSegment = -1;

			for (int idxSegment = 0; firstSegment == -1 && idxSegment < this.values.length; idxSegment += 2) {
				max = this.values[idxSegment + 1];
				if (fromElement.compareTo(max) <= 0) {
					firstSegment = idxSegment;
				}
			}

			if (firstSegment != -1) {
				// Go through the segments
				for (int idxSegment = firstSegment; idxSegment < this.values.length; idxSegment += 2) {
					min = this.values[idxSegment];
					max = this.values[idxSegment + 1];
					if (toElement.compareTo(min) <= 0) {
						idxSegment = this.values.length;
					} else {
						for (int value = min; toElement.compareTo(value) > 0 && value <= max; ++value) {
							assert toElement.compareTo(value) > 0;
							if (fromElement.compareTo(value) <= 0) {
								theset.add(value);
							}
						}
					}
				}
			}

		}
		return theset;
	}

	@Override
	public SortedSet<Integer> tailSet(Integer fromElement) {
		final SortedSet<Integer> theset = new TreeSet<>();
		if (this.values != null) {

			for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
				// The next segment is lower to the given bound
				if (this.values[idxStart + 1] < fromElement) {
					continue;
				}

				// The given bound is inside the value segment
				if (fromElement >= this.values[idxStart]) {
					for (int intToAdd = fromElement; intToAdd <= this.values[idxStart + 1]; ++intToAdd) {
						theset.add(intToAdd);
					}
				}

				// The given bound is inside the value segment
				if (fromElement < this.values[idxStart]) {
					for (int intToAdd = this.values[idxStart]; intToAdd <= this.values[idxStart + 1]; ++intToAdd) {
						theset.add(intToAdd);
					}
				}
			}

		}
		return theset;
	}

	@Override
	public final void add(int index, Integer element) {
		add(element);
	}

	@Override
	public boolean add(Integer value) {
		if (this.values == null) {
			this.values = new int[] {value, value};
			this.size = 1;
		} else {
			int first = 0;
			int last = getSegmentCount() - 1;

			while (first <= last) {
				final int center = (first + last) / 2;
				final int min = this.values[center * 2];
				final int max = this.values[center * 2 + 1];

				if (value.compareTo(min) >= 0 && value.compareTo(max) <= 0) {
					return false;
				}

				if (value.compareTo(min) < 0) {
					last = center - 1;
				} else {
					first = center + 1;
				}
			}

			final int index = first * 2;
			final boolean mergeWithPrevious = index > 0 && value.compareTo(this.values[index - 1] + 1) == 0;
			final boolean mergeWithNext = index < this.values.length && value.compareTo(this.values[index] - 1) == 0;

			++this.size;

			if (mergeWithPrevious && mergeWithNext) {
				this.values[index - 1] = this.values[index + 1];
				final int[] nValues = new int[this.values.length - 2];
				System.arraycopy(this.values, 0, nValues, 0, index);
				System.arraycopy(this.values, index + 2, nValues, index, this.values.length - index - 2);
				this.values = nValues;
			} else if (mergeWithPrevious) {
				this.values[index - 1] = value.intValue();
			} else if (mergeWithNext) {
				this.values[index] = value.intValue();
			} else {
				// Create a new segment
				final int[] nValues = new int[this.values.length + 2];
				System.arraycopy(this.values, 0, nValues, 0, index);
				nValues[index] = value.intValue();
				nValues[index + 1] = nValues[index];
				System.arraycopy(this.values, index, nValues, index + 2, this.values.length - index);
				this.values = nValues;
			}
		}
		return true;
	}

	@Override
	public final boolean addAll(int index, Collection<? extends Integer> collection) {
		return addAll(collection);
	}

	@Override
	public boolean addAll(Collection<? extends Integer> collection) {
		boolean changed = false;
		for (final Integer value : collection) {
			changed |= add(value);
		}
		return changed;
	}

	@Override
	public void clear() {
		this.values = null;
		this.size = 0;
	}

	@Override
	public boolean contains(Object obj) {
		if ((this.values != null) && (obj instanceof Number)) {
			final int e = ((Number) obj).intValue();

			for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
				if (e < this.values[idxStart]) {
					return false;
				}
				if ((e >= this.values[idxStart]) && (e <= this.values[idxStart + 1])) {
					return true;
				}
			}

		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		if (this.values == null) {
			return false;
		}

		final SortedSet<Integer> elements = new TreeSet<>();
		for (final Object o : collection) {
			if (o instanceof Number) {
				elements.add(((Number) o).intValue());
			}
		}

		int idxStart = 0;

		for (final Integer e : elements) {
			for (; idxStart < this.values.length - 1; idxStart += 2) {
				if (e < this.values[idxStart]) {
					return false;
				}
				if ((e >= this.values[idxStart]) && (e <= this.values[idxStart + 1])) {
					break;
				}
			}
		}

		return true;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IntegerListIterator();
	}

	/** Returns an iterator over the segments in this list in proper sequence.
	 *
	 * @return an iterator over the segments in this list in proper sequence
	 */
	public Iterator<IntegerSegment> segmentIterator() {
		return new SegmentIterator();
	}

	/** Returns an iterable object over the segments in this list in proper sequence.
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

	@Override
	public boolean remove(Object obj) {
		if ((this.values != null) && (obj instanceof Number)) {
			final int e = ((Number) obj).intValue();
			final int segmentIndex = getSegmentIndexFor(e);
			if (segmentIndex >= 0 && removeElementInSegment(segmentIndex, e)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Integer remove(int index) {
		if (this.values == null) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}

		int firstIndex = 0;

		for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
			final int endIndex = this.values[idxStart + 1] - this.values[idxStart] + firstIndex;
			if ((index >= firstIndex) && (index <= endIndex)) {
				final int elementToRemove = this.values[idxStart] + index - firstIndex;
				if (!removeElementInSegment(idxStart, elementToRemove)) {
					throw new IndexOutOfBoundsException(Integer.toString(index));
				}
				return elementToRemove;
			}
			firstIndex = endIndex + 1;
		}

		throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	/** Remove the {@code element} in the segment starting at index
	 * {@code segmentIndex}.
	 *
	 * @param segmentIndex is the index of the segment from which the
	 *     element must be removed.
	 * @param element is the element to remove.
	 * @return <code>true</code> if the element was removed, otherwhise <code>false</code>
	 */
	protected boolean removeElementInSegment(int segmentIndex, int element) {
		if ((element == this.values[segmentIndex]) && (element == this.values[segmentIndex + 1])) {
			// Remove the segment
			if (this.values.length == 2) {
				this.values = null;
				this.size = 0;
			} else {
				final int[] newTab = new int[this.values.length - 2];
				System.arraycopy(this.values, 0, newTab, 0, segmentIndex);
				System.arraycopy(this.values, segmentIndex + 2, newTab, segmentIndex, newTab.length - segmentIndex);
				this.values = newTab;
				--this.size;
			}
			return true;
		}

		if ((element >= this.values[segmentIndex]) && (element <= this.values[segmentIndex + 1])) {
			if (element == this.values[segmentIndex]) {
				// Move the lower bound
				++this.values[segmentIndex];
				--this.size;
			} else if (element == this.values[segmentIndex + 1]) {
				// Move the upper bound
				--this.values[segmentIndex + 1];
				--this.size;
			} else {
				// Split the segment
				final int[] newTab = new int[this.values.length + 2];
				System.arraycopy(this.values, 0, newTab, 0, segmentIndex + 1);
				System.arraycopy(this.values, segmentIndex + 1, newTab, segmentIndex + 3, newTab.length - segmentIndex - 3);
				newTab[segmentIndex + 1] = element - 1;
				newTab[segmentIndex + 2] = element + 1;
				this.values = newTab;
				--this.size;
			}

			return true;
		}

		return false;
	}

	/** Remove the {@code element} in the segment starting at index
	 * {@code segmentIndex}.
	 *
	 * @param segmentIndex is the index of the segment to remove.
	 * @return <code>true</code> if the element was removed, otherwhise <code>false</code>
	 */
	protected boolean removeSegment(int segmentIndex) {
		if ((this.values == null) || (segmentIndex < 0)
				|| (segmentIndex >= this.values.length - 1)) {
			return false;
		}

		if (this.values.length == 2) {
			this.values = null;
			this.size = 0;
		} else {
			final int count = this.values[segmentIndex + 1] - this.values[segmentIndex] + 1;
			final int[] newTab = new int[this.values.length - 2];
			System.arraycopy(this.values, 0, newTab, 0, segmentIndex);
			System.arraycopy(this.values, segmentIndex + 2, newTab, segmentIndex, this.values.length - segmentIndex);
			this.values = newTab;
			this.size -= count;
		}

		return true;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean changed = false;
		for (final Object o : collection) {
			changed |= remove(o);
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		final SortedSet<Integer> theset = toSortedSet();
		final boolean changed = theset.retainAll(collection);
		if (changed) {
			set(theset);
		}
		return changed;
	}

	@Override
	public Integer set(int index, Integer element) {
		final Integer oldValue = remove(index);
		add(element);
		return oldValue;
	}

	/** Set the content of this list from the specified collection.
	 *
	 * @param collection is the values to pout inside this list.
	 */
	public void set(SortedSet<? extends Number> collection) {
		this.values = null;
		this.size = 0;

		for (final Number number : collection) {
			final int e = number.intValue();

			if ((this.values != null) && (e == this.values[this.values.length - 1] + 1)) {
				// Same group
				++this.values[this.values.length - 1];
				++this.size;
			}
			if ((this.values != null) && (e > this.values[this.values.length - 1] + 1)) {
				// Create a new group
				final int[] newTab = new int[this.values.length + 2];
				System.arraycopy(this.values, 0, newTab, 0, this.values.length);
				newTab[newTab.length - 2] = e;
				newTab[newTab.length - 1] = newTab[newTab.length - 2];
				this.values = newTab;
				++this.size;
			} else if (this.values == null) {
				// Add the first group
				this.values = new int[] {e, e};
				this.size = 1;
			}

		}
	}

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
		return (this.values == null) ? 0 : (this.values.length / 2);
	}

	/**
	 * Replies the last value of a segment.
	 *
	 * @param idxSegment is the index of the segment. It must be a multiple of 2.
	 * @return the last value in the segment.
	 */
	protected int getLastValueOnSegment(int idxSegment) {
		assert idxSegment % 2 == 0;
		if (this.values == null) {
			throw new IndexOutOfBoundsException();
		}
		return this.values[idxSegment + 1];
	}

	/**
	 * Replies the first value of a segment.
	 *
	 * @param idxSegment is the index of the segment. It must be a multiple of 2.
	 * @return the first value in the segment
	 */
	protected int getFirstValueOnSegment(int idxSegment) {
		assert idxSegment % 2 == 0;
		if (this.values == null) {
			throw new IndexOutOfBoundsException();
		}
		return this.values[idxSegment];
	}

	/**
	 * Replies the segment index for the specified value.
	 *
	 * @param value is the value to search for.
	 * @return the index or <code>-1</code>.  It is a multiple of 2.
	 */
	protected int getSegmentIndexFor(int value) {
		if (this.values != null) {
			int first = 0;
			int last = getSegmentCount() - 1;

			while (first <= last) {
				final int center = (first + last) / 2;
				final int min = this.values[center * 2];
				final int max = this.values[center * 2 + 1];

				if (value >= min && value <= max) {
					return center * 2;
				}

				if (value < min) {
					last = center - 1;
				} else {
					first = center + 1;
				}
			}
		}

		return -1;
	}

	@Override
	public Integer get(int index) {
		if (this.values == null) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}

		int firstIndex = 0;

		for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
			final int endIndex = this.values[idxStart + 1] - this.values[idxStart] + firstIndex;
			if ((index >= firstIndex) && (index <= endIndex)) {
				return this.values[idxStart] + index - firstIndex;
			}
			firstIndex = endIndex + 1;
		}

		throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	/**
	 * Replies the segment index for the specified value.
	 *
	 * <p>The given array must be pre-allocated with at least 2 cells.
	 * The first cell will the the index of the segment. The
	 * second cell will be the first integer value.
	 *
	 * @param offset is the number of integer values to skip from the
	 *     begining of the value set.
	 * @param tofill is the 2-cell array to fill
	 * @return <code>true</code> on success, <code>false</code> otherwise.
	 */
	protected boolean get(int offset, int[] tofill) {
		if (this.values != null) {
			int idxTab = 0;
			for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
				for (int n = this.values[idxStart]; n <= this.values[idxStart + 1]; ++n) {
					if (offset == idxTab) {
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

	@Override
	public Object[] toArray() {
		final Object[] tab = new Object[this.size];
		if (this.values != null) {
			int idxTab = 0;
			for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
				for (int n = this.values[idxStart]; n <= this.values[idxStart + 1]; ++n) {
					tab[idxTab++] = n;
				}
			}
		}
		return tab;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] array) {
		final Class<T> clazz = (Class<T>) array.getClass().getComponentType();
		if (!clazz.isAssignableFrom(Integer.class)) {
			throw new ArrayStoreException();
		}
		T[] tab = array;
		if (tab.length < this.size) {
			tab = (T[]) Array.newInstance(clazz, this.size);
		}

		if (this.values != null) {
			int idxTab = 0;
			for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
				for (int n = this.values[idxStart]; n <= this.values[idxStart + 1]; ++n) {
					tab[idxTab++] = (T) Integer.valueOf(n);
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
		final int[] tab = new int[this.size];
		if (this.values != null) {
			int idxTab = 0;
			for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
				for (int n = this.values[idxStart]; n <= this.values[idxStart + 1]; ++n) {
					tab[idxTab++] = n;
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
		final SortedSet<Integer> theset = new TreeSet<>();
		if (this.values != null) {
			for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
				for (int n = this.values[idxStart]; n <= this.values[idxStart + 1]; ++n) {
					theset.add(n);
				}
			}
		}
		return theset;
	}

	@Override
	public int indexOf(Object obj) {
		if (obj instanceof Number) {
			final int e = ((Number) obj).intValue();

			if (this.values != null) {
				int idx = 0;

				for (int idxStart = 0; idxStart < this.values.length - 1; idxStart += 2) {
					for (int n = this.values[idxStart]; n <= this.values[idxStart + 1]; ++n) {
						if (n == e) {
							return idx;
						}
						++idx;
					}
				}
			}

			return -1;
		}
		if (obj == null) {
			throw new NullPointerException();
		}
		throw new ClassCastException();
	}

	@Override
	public int lastIndexOf(Object obj) {
		if (obj instanceof Number) {
			final int e = ((Number) obj).intValue();

			if (this.values != null) {
				int idx = this.size - 1;

				for (int idxStart = this.values.length - 2; idxStart >= 0; idxStart -= 2) {
					for (int n = this.values[idxStart + 1]; n >= this.values[idxStart]; --n) {
						if (n == e) {
							return idx;
						}
						--idx;
					}
				}
			}

			return -1;
		}
		if (obj == null) {
			throw new NullPointerException();
		}
		throw new ClassCastException();
	}

	@Override
	public ListIterator<Integer> listIterator() {
		return new IntegerListIterator();
	}

	@Override
	public ListIterator<Integer> listIterator(int index) {
		return new IntegerListIterator(index);
	}

	@Override
	public List<Integer> subList(int fromIndex, int toIndex) {
		final List<Integer> theList = new ArrayList<>();
		if (this.values != null) {

			// Search for the first matching segment
			int firstSegment = -1;
			int idxValue = 0;

			for (int idxSegment = 0; firstSegment == -1 && idxSegment < this.values.length; idxSegment += 2) {
				final int min = this.values[idxSegment];
				final int max = this.values[idxSegment + 1];
				final int nb = max - min + 1;
				if (fromIndex < (idxValue + nb)) {
					firstSegment = idxSegment;
				} else {
					idxValue += nb;
				}
			}

			if (firstSegment != -1) {
				// Go through the segments
				for (int idxSegment = firstSegment; idxSegment < this.values.length; idxSegment += 2) {
					final int min = this.values[idxSegment];
					final int max = this.values[idxSegment + 1];
					if (toIndex <= idxValue) {
						idxSegment = this.values.length;
					} else {
						for (int value = min; idxValue < toIndex && value <= max; ++value) {
							if (fromIndex <= idxValue) {
								theList.add(value);
							}
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

		/** Construct an iterator.
		 *
		 * @param startingIndex the starting index.
		 */
		IntegerListIterator(int startingIndex) {
			this.offset = startingIndex;
			final int lsize = size();
			if ((startingIndex >= 0) && (startingIndex < lsize)) {
				this.tabIndex = startingIndex;
				final int[] tab = new int[2];
				get(startingIndex, tab);
				this.segmentIndex = tab[0];
				this.number = tab[1];
			} else {
				this.tabIndex = -1;
				this.segmentIndex = -1;
			}
		}

		/** Construct an iterator.
		 */
		IntegerListIterator() {
			this.offset = 0;
			if (isEmpty()) {
				this.tabIndex = -1;
				this.segmentIndex = -1;
			} else {
				this.tabIndex = 0;
				this.segmentIndex = 0;
				this.number = getFirstValueOnSegment(this.segmentIndex);
			}
		}

		@Override
		public boolean hasNext() {
			return (this.tabIndex >= this.offset) && (this.tabIndex < size());
		}

		@Override
		public int nextIndex() {
			final int lsize = size();
			if ((this.tabIndex >= this.offset) && (this.tabIndex < lsize)) {
				return this.tabIndex;
			}
			return lsize;
		}

		@Override
		public Integer next() {
			if ((this.tabIndex < this.offset) || (this.tabIndex >= size())) {
				throw new NoSuchElementException();
			}

			final int n = this.number;
			final int lsize = size();

			++this.tabIndex;
			if ((this.tabIndex >= this.offset) && (this.tabIndex < lsize)) {
				if (this.number == getLastValueOnSegment(this.segmentIndex)) {
					this.segmentIndex += 2;
					this.number = getFirstValueOnSegment(this.segmentIndex);
				} else {
					++this.number;
				}
			}

			return n;
		}

		@Override
		public boolean hasPrevious() {
			final int idx = this.tabIndex - 1;
			return (idx >= this.offset) && (idx < size());
		}

		@Override
		public int previousIndex() {
			final int idx = this.tabIndex - 1;
			return ((idx >= this.offset) && (idx < size())) ? idx : -1;
		}

		@Override
		public Integer previous() {
			final int idx = this.tabIndex - 1;
			if ((idx < this.offset) || (idx >= size())) {
				throw new NoSuchElementException();
			}

			final int n = get(idx);
			final int lsize = size();

			--this.tabIndex;
			if ((this.tabIndex >= this.offset) && (this.tabIndex < lsize)) {
				if (this.number == getFirstValueOnSegment(this.segmentIndex)) {
					this.segmentIndex -= 2;
					this.number = getLastValueOnSegment(this.segmentIndex);
				} else {
					--this.number;
				}
			}

			return n;
		}

		@Override
		public void add(Integer value) {
			if (value == this.number) {
				return;
			}
			if (IntegerList.this.add(value)) {
				if (value < this.number) {
					++this.tabIndex;
					this.segmentIndex = getSegmentIndexFor(this.number);
				}
			}
		}

		@Override
		public void set(Integer vallue) {
			add(vallue);
		}

		@Override
		public void remove() {
			final int lsize = size();
			if ((this.tabIndex > 1) && (this.tabIndex < lsize)) {
				IntegerList.this.remove(this.tabIndex - 1);
				--this.tabIndex;
				this.segmentIndex = getSegmentIndexFor(this.number);
			}
			throw new IllegalStateException();
		}

	}

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
		private final int first;

		/** Last value in the segment.
		 */
		private final int last;

		/** Construct a segment.
		 *
		 * @param first first value.
		 * @param last last value.
		 */
		IntegerSegment(int first, int last) {
			this.first = first;
			this.last = last;
		}

		/** Replies the first value in the segment.
		 *
		 * @return the first value.
		 */
		public int getFirst() {
			return this.first;
		}

		/** Replies the last value in the segment.
		 *
		 * @return the last value.
		 */
		public int getLast() {
			return this.last;
		}

	}

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

		/** Construct iterator.
		 */
		SegmentIterator() {
			this.index = 0;
			this.removable = false;
		}

		@Override
		@SuppressWarnings("synthetic-access")
		public boolean hasNext() {
			return this.index < IntegerList.this.values.length;
		}

		@Override
		@SuppressWarnings("synthetic-access")
		public IntegerSegment next() {
			if (this.index >= IntegerList.this.values.length) {
				throw new ConcurrentModificationException();
			}

			try {
				final int first = IntegerList.this.values[this.index];
				final int last = IntegerList.this.values[this.index + 1];
				this.index += 2;
				this.removable = true;
				return new IntegerSegment(first, last);
			} catch (IndexOutOfBoundsException exception) {
				throw new NoSuchElementException();
			}
		}

		@Override
		@SuppressWarnings("synthetic-access")
		public void remove() {
			if (this.removable) {
				final int idx = this.index - 2;
				if (idx < 0 || idx >= IntegerList.this.values.length) {
					throw new ConcurrentModificationException();
				}
				removeSegment(idx);
				this.removable = false;
			} else {
				throw new NoSuchElementException();
			}
		}

	}

}
