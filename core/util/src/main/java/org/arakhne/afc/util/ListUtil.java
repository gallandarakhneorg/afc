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

import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;

/**
 * Utilities on lists.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.4
 */
public class ListUtil {

	/** Remove the given element from the list using a dichotomic algorithm.
	 * <p>
	 * This function ensure that the comparator is invoked as: <code>comparator(data, dataAlreadyInList)</code>.
	 * 
	 * @param <E> is the type of the elements in the list.
	 * @param list is the list to change.
	 * @param comparator is the comparator of elements.
	 * @param data is the data to remove.
	 * @return the index at which the element was removed in the list; or
	 * <code>-1</code> if the element was not removed.
	 */
	public static <E> int remove(List<E> list, Comparator<? super E> comparator, E data) {
		assert(list!=null);
		assert(comparator!=null);
		assert(data!=null);
		int f = 0;
		int l = list.size()-1;
		int c;
		E d;
		int cmpR;
		while (l>=f) {
			c = (f+l)/2;
			d = list.get(c);
			cmpR = comparator.compare(data, d);
			if (cmpR==0) {
				list.remove(c);
				return c;
			}
			else if (cmpR<0) {
				l = c-1;
			}
			else {
				f = c+1;
			}
		}
		return -1;
	}

	/** Add the given element in the main list using a dichotomic algorithm.
	 * <p>
	 * This function ensure that the comparator is invoked as: <code>comparator(data, dataAlreadyInList)</code>.
	 * <p>
	 * If the data is al
	 * 
	 * @param <E> is the type of the elements in the list.
	 * @param list is the list to change.
	 * @param comparator is the comparator of elements.
	 * @param data is the data to insert.
	 * @param allowMultipleOccurencesOfSameValue indicates if multiple
	 * occurrences of the same value are allowed in the list.
	 * @param allowReplacement indicates if the given <vaz>elt</var> may replace
	 * the found element.
	 * @return the index where the element was inserted, or <code>-1</code>
	 * if the element was not inserted.
	 */
	public static <E> int add(List<E> list, Comparator<? super E> comparator, E data, boolean allowMultipleOccurencesOfSameValue, boolean allowReplacement) {
		assert(list!=null);
		assert(comparator!=null);
		assert(data!=null);
		int f = 0;
		int l = list.size()-1;
		int c;
		E d;
		int cmpR;
		while (l>=f) {
			c = (f+l)/2;
			d = list.get(c);
			cmpR = comparator.compare(data, d);
			if (cmpR==0 && !allowMultipleOccurencesOfSameValue) {
				if (allowReplacement) {
					list.set(c, data);
					return c;
				}
				return -1;
			}
			if (cmpR<0) {
				l = c-1;
			}
			else {
				f = c+1;
			}
		}
		list.add(f, data);
		return f;
	}

	/** Replies if the given element is inside the list, using a dichotomic algorithm.
	 * <p>
	 * This function ensure that the comparator is invoked as: <code>comparator(data, dataAlreadyInList)</code>.
	 * 
	 * @param <E> is the type of the elements in the list.
	 * @param list is the list to explore.
	 * @param comparator is the comparator of elements.
	 * @param data is the data to search for.
	 * @return <code>true</code> if the data is inside the list, otherwise <code>false</code>
	 */
	public static <E> boolean contains(List<E> list, Comparator<? super E> comparator, E data) {	
		assert(list!=null);
		assert(comparator!=null);
		assert(data!=null);
		int f = 0;
		int l = list.size()-1;
		int c;
		E d;
		int cmpR;
		while (l>=f) {
			c = (f+l)/2;
			d = list.get(c);
			cmpR = comparator.compare(data, d);
			if (cmpR==0) {
				return true;
			}
			else if (cmpR<0) {
				l = c-1;
			}
			else {
				f = c+1;
			}
		}
		return false;
	}

	/** Replies the index of the given data in the given list according to a
	 * dichotomic search algorithm. Order between objects
	 * is given by <var>comparator</var>.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to search for.
	 * @return the index at which the element is, or <code>-1</code> if
	 * the element was not found.
	 */
	public static <T> int indexOf(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert(comparator!=null);
			assert(list!=null);
			if (elt==null) return -1;
			
			int f = 0;
			int l = list.size()-1;
			int c, cmp;
			T indata;
			
			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				cmp = comparator.compare(elt, indata);
				if (cmp==0) {
					do {
						--c;
					}
					while (c>=0 && comparator.compare(elt, list.get(c))==0);
					return c+1;
				}
				else if (cmp<0) {
					l = c-1;
				}
				else {
					f = c+1;
				}
			}
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable exception) {
			//
		}
		return -1;
	}

	/** Replies the last index of the given data in the given list according to a
	 * dichotomic search algorithm. Order between objects
	 * is given by <var>comparator</var>.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to search for.
	 * @return the last index at which the element is, or <code>-1</code> if
	 * the element was not found.
	 */
	public static <T> int lastIndexOf(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert(comparator!=null);
			assert(list!=null);
			if (elt==null) return -1;

			int f = 0;
			int l = list.size()-1;
			int c, cmp;
			T indata;

			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				cmp = comparator.compare(elt, indata);
				if (cmp==0) {
					do {
						++c;
					}
					while (c<list.size() && comparator.compare(elt, list.get(c))==0);
					return c-1;
				}
				else if (cmp<0) {
					l = c-1;
				}
				else {
					f = c+1;
				}
			}
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable exception) {
			//
		}
		return -1;
	}

	/** Replies the index at which the given element may
	 * be added in a sorted list.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * <p>
	 * This function assumes that the given <var>elt</var>
	 * may appear many times in the list.
	 * 
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to add in.
	 * @param list is the list inside which the element should be added.
	 * @return the index at which the element may be added.
	 */
	public static <T> int getInsertionIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		return getInsertionIndex(list, comparator, elt, true);
	}
	
	/** Replies the index at which the given element may
	 * be added in a sorted list.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * <p>
	 * This function assumes that the given <var>elt</var>
	 * may appear many times in the list.
	 * 
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to add in.
	 * @param list is the list inside which the element should be added.
	 * @param allowMultiple indicates if the given <var>elt</var> may appear
	 * many times in the list, or not.
	 * @return the index at which the element may be added.
	 */
	public static <T> int getInsertionIndex(List<T> list, Comparator<? super T> comparator, T elt, boolean allowMultiple) {
		try {
			assert(comparator!=null);
			assert(list!=null);
			if (elt==null) return -1;
			
			int f = 0;
			int l = list.size()-1;
			int c, comparison;
			T indata;
			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				comparison = comparator.compare(elt, indata);
				if (!allowMultiple && comparison==0) return -1;
				if (comparison<0) {
					l = c-1;
				}
				else {
					f = c+1;
				}
			}
			return f;
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable exception) {
			return -1;
		}
	}

	/**
	 * Returns the index of the leastest element in this list greater than or equal to 
	 * the given element, or <code>-1</code> if there is no such element.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @return the index of leastest element greater than or equal to <var>elt</var>, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#ceiling(Object)
	 */
	public static <T> int ceilingIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert(comparator!=null);
			assert(list!=null);
			if (elt==null) return -1;
			int f = 0;
			int l = list.size()-1;
			int c, cmp;
			T indata;
			
			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				cmp = comparator.compare(elt, indata);
				if (cmp==0) {
					do {
						--c;
					}
					while (c>=0 && comparator.compare(elt, list.get(c))==0);
					return c+1;
				}
				else if (cmp<0) {
					l = c-1;
				}
				else {
					f = c+1;
				}
			}
			if (f>=list.size()) f = -1;
			return f;
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable exception) {
			return -1;
		}
	}

	/**
	 * Returns the index of the greatest element in this list less than or equal to 
	 * the given element, or <code>-1</code> if there is no such element.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @return the index of greatest element less than or equal to <var>elt</var>, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#floor(Object)
	 */
	public static <T> int floorIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert(comparator!=null);
			assert(list!=null);
			if (elt==null) return -1;
			int f = 0;
			int l = list.size()-1;
			int c, cmp;
			T indata;

			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				cmp = comparator.compare(elt, indata);
				if (cmp==0) {
					do {
						++c;
					}
					while (c<list.size() && comparator.compare(elt, list.get(c))==0);
					return c-1;
				}
				else if (cmp<0) {
					l = c-1;
				}
				else {
					f = c+1;
				}
			}
			return l;
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable exception) {
			return -1;
		}
	}

	/**
	 * Returns the index of the least element in this list strictly greater 
	 * than the given element, or <code>-1</code> if there is no such element.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @return the index of least element strictly greater than to <var>elt</var>, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#higher(Object)
	 */
	public static <T> int higherIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert(comparator!=null);
			assert(list!=null);
			if (elt==null) return -1;
			int f = 0;
			int l = list.size()-1;
			int c, cmp;
			T indata;
			
			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				cmp = comparator.compare(elt, indata);
				if (cmp<0) {
					l = c-1;
				}
				else {
					f = c+1;
				}
			}
			++l;
			if (l>=list.size()) l = -1;
			return l;
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable exception) {
			return -1;
		}
	}

	/**
	 * Returns the index of the greatest element in this list strictly lower
	 * than the given element, or <code>-1</code> if there is no such element.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @return the index of greater element strictly lower than to <var>elt</var>, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#lower(Object)
	 */
	public static <T> int lowerIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert(comparator!=null);
			assert(list!=null);
			if (elt==null) return -1;
			int f = 0;
			int l = list.size()-1;
			int c, cmp;
			T indata;
			
			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				cmp = comparator.compare(elt, indata);
				if (cmp<=0) {
					l = c-1;
				}
				else {
					f = c+1;
				}
			}
			return l;
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable exception) {
			return -1;
		}
	}

}
