/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team,
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
package org.arakhne.afc.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.SortedSet;

import org.arakhne.afc.sizediterator.CollectionSizedIterator;
import org.arakhne.afc.sizediterator.EmptyIterator;
import org.arakhne.afc.sizediterator.IteratorIterator;
import org.arakhne.afc.sizediterator.ListReverseIterator;
import org.arakhne.afc.sizediterator.SingleIterator;
import org.arakhne.afc.sizediterator.SizedIterable;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.sizediterator.SortedSetReverseIterator;
import org.arakhne.afc.sizediterator.UnmodifiableIterable;
import org.arakhne.afc.sizediterator.UnmodifiableSizedIterator;

/**
 * Some utilities functions for collections. 
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CollectionUtil {

	/** Replies the iterator that is corresponding
	 * to the given Enumeration.
	 * Remember that Iterator is replacing the Enumeration
	 * in the Java Collection API. You should use Iterator
	 * in place of Enumeration as many as possible.
	 * 
	 * @param enumeration
	 * @return the iterator.
	 * @since 4.1
	 */
	public static <T> Iterator<T> toIterator(Enumeration<T> enumeration) {
		return new EnumerationIterator<T>(enumeration);
	}
	
	/** Replies of the given element is in the list.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to search for.
	 * @param list is the list inside which the search must be done.
	 * @return <code>true</code> if the element is inside the array, otherwise <code>false</code>
	 * @since 4.0
	 */
	public static <T> boolean contains(Comparator<? super T> comparator, T elt, List<T> list) {
		try {
			assert(comparator!=null);
			assert(list!=null);
			if (elt==null) return false;
			int f = 0;
			int l = list.size()-1;
			int c, cmp;
			T indata;
			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				cmp = comparator.compare(elt, indata);
				if (cmp==0) {
					return true;
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
		catch(Throwable _) {
			//
		}
		return false;
	}

	/** Add an element in a sorted list.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to add in.
	 * @param list is the list inside which the element should be added.
	 * @return the index at which the element was added.
	 * @since 4.0
	 * @deprecated see {@link #add(Comparator, Object, List)}
	 */
	@Deprecated
	public static <T> int dichotomicAdd(Comparator<T> comparator, T elt, List<T> list) {
		return add(comparator, elt, list);
	}
	
	/** Add an element in a sorted list.
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
	 * @return the index at which the element was added.
	 * @since 4.0
	 */
	public static <T> int add(Comparator<? super T> comparator, T elt, List<T> list) {
		return add(comparator, elt, list, true);
	}
	
	/** Add an element in a sorted list.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to add in.
	 * @param list is the list inside which the element should be added.
	 * @param allowMultiple indicates if the given <var>elt</var> may appear
	 * many times in the list, or not.
	 * @return the index at which the element was added.
	 * @since 4.0
	 */
	public static <T> int add(Comparator<? super T> comparator, T elt, List<T> list, boolean allowMultiple) {
		return add(comparator, elt, list, allowMultiple, false);
	}
	
	/** Add an element in a sorted list.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to add in.
	 * @param list is the list inside which the element should be added.
	 * @param allowMultiple indicates if the given <var>elt</var> may appear
	 * many times in the list, or not.
	 * @param allowReplacement indicates if the given <vaz>elt</var> may replace
	 * the found element.
	 * @return the index at which the element was added.
	 * @since 4.1
	 */
	public static <T> int add(Comparator<? super T> comparator, T elt, List<T> list, boolean allowMultiple, boolean allowReplacement) {
		try {
			assert(comparator!=null);
			assert(elt!=null);
			assert(list!=null);
			
			int f = 0;
			int l = list.size()-1;
			int c, comparison;
			T indata;
			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				comparison = comparator.compare(elt, indata);
				if (!allowMultiple && comparison==0) {
					if (allowReplacement) {
						list.set(c, elt);
						return c;
					}
					return -1;
				}
				if (comparison<0) {
					l = c-1;
				}
				else {
					f = c+1;
				}
			}
			
			list.add(f, elt);
			return f;
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable _) {
			return -1;
		}
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
	 * @since 4.0
	 */
	public static <T> int getInsertionIndex(Comparator<? super T> comparator, T elt, List<T> list) {
		return getInsertionIndex(comparator, elt, list, true);
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
	 * @since 4.0
	 */
	public static <T> int getInsertionIndex(Comparator<? super T> comparator, T elt, List<T> list, boolean allowMultiple) {
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
		catch(Throwable _) {
			return -1;
		}
	}

	/** Remove an element from a sorted list.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to remove from.
	 * @param list is the list inside which the element should be removed.
	 * @return the index at which the element was removed.
	 * @since 4.0
	 * @deprecated see {@link #remove(Comparator, Object, List)}
	 */
	@Deprecated
	public static <T> int dichotomicRemove(Comparator<T> comparator, T elt, List<T> list) {
		return remove(comparator, elt, list);
	}

	/** Remove an element from a sorted list.
	 * <p>
	 * This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 * 
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to remove from.
	 * @param list is the list inside which the element should be removed.
	 * @return the index at which the element was removed.
	 * @since 4.0
	 */
	public static <T> int remove(Comparator<? super T> comparator, T elt, List<T> list) {
		try {
			assert(comparator!=null);
			assert(elt!=null);
			assert(list!=null);
			
			int f = 0;
			int l = list.size()-1;
			int c, cmp;
			T indata;
			while (l>=f) {
				c = (f+l)/2;
				indata = list.get(c);
				cmp = comparator.compare(elt, indata);
				if (cmp==0) {
					list.remove(c);
					return c;
				}
				if (cmp<0) {
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
		catch(Throwable _) {
			//
		}
		return -1;
	}

	/** Replies the index of the given data in the given list according to a
	 * dichotomic search algorithm. Order between objects
	 * is given by <var>comparator</var>.
	 * 
	 * @param <T> is the type of the data to search for.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to search for.
	 * @param list is the list inside which the element should be searched.
	 * @return the index at which the element is, or <code>-1</code> if
	 * the element was not found.
	 * @since 4.0
	 * @deprecated see {@link #indexOf(Comparator, Object, List)}
	 */
	@Deprecated
	public static <T> int dichotomicIndexOf(Comparator<T> comparator, T elt, List<T> list) {
		return indexOf(comparator, elt, list);
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
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to search for.
	 * @param list is the list inside which the element should be searched.
	 * @return the index at which the element is, or <code>-1</code> if
	 * the element was not found.
	 * @since 4.0
	 */
	public static <T> int indexOf(Comparator<? super T> comparator, T elt, List<T> list) {
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
		catch(Throwable _) {
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
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to search for.
	 * @param list is the list inside which the element should be searched.
	 * @return the last index at which the element is, or <code>-1</code> if
	 * the element was not found.
	 * @since 4.0
	 */
	public static <T> int lastIndexOf(Comparator<? super T> comparator, T elt, List<T> list) {
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
		catch(Throwable _) {
			//
		}
		return -1;
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
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @param list is the list inside which the element should be searched.
	 * @return the index of greatest element less than or equal to <var>elt</var>, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#floor(Object)
	 * @since 4.0
	 */
	public static <T> int floorIndex(Comparator<? super T> comparator, T elt, List<T> list) {
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
		catch(Throwable _) {
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
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @param list is the list inside which the element should be searched.
	 * @return the index of least element strictly greater than to <var>elt</var>, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#higher(Object)
	 * @since 4.0
	 */
	public static <T> int higherIndex(Comparator<? super T> comparator, T elt, List<T> list) {
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
		catch(Throwable _) {
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
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @param list is the list inside which the element should be searched.
	 * @return the index of greater element strictly lower than to <var>elt</var>, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#lower(Object)
	 * @since 4.0
	 */
	public static <T> int lowerIndex(Comparator<? super T> comparator, T elt, List<T> list) {
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
		catch(Throwable _) {
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
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @param list is the list inside which the element should be searched.
	 * @return the index of leastest element greater than or equal to <var>elt</var>, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#ceiling(Object)
	 * @since 4.0
	 */
	public static <T> int ceilingIndex(Comparator<? super T> comparator, T elt, List<T> list) {
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
		catch(Throwable _) {
			return -1;
		}
	}

	/** Replies an iterator on a single object.
	 * 
	 * @param <T> is the type of the object to iterate on.
	 * @param object is the object to iterate on.
	 * @return an iterator
	 */
	public static <T> Iterator<T> singletonIterator(T object) {
		return new SingleIterator<T>(object);
	}
	
	/** Replies an iterator which is disabling the {@code remove()} function.
	 * 
	 * @param <T> is the type of the object to iterate on.
	 * @param iterator is the iterator to protect.
	 * @return an iterator
	 */
	public static <T> Iterator<T> unmodifiableIterator(Iterator<T> iterator) {
		if (iterator instanceof SizedIterator<?>) {
			return new UnmodifiableSizedIterator<T>((SizedIterator<T>)iterator);
		}
		return new UnmodifiableIterator<T>(iterator);
	}

	/** Replies an unmodifiable iterable which is wrapping to
	 * the given iterable.
	 * 
	 * @param <T> is the type of the object to iterate on.
	 * @param iterable is the iterable to protect.
	 * @return an iterable object
	 */
	public static <T> Iterable<T> unmodifiableIterable(Iterable<T> iterable) {
		return new UnmodifiableIterable<T>(iterable);
	}

	/** Replies an empty iterator.
	 * 
	 * @param <T> is the type of the object to iterate on.
	 * @return an iterator
	 */
	public static <T> Iterator<T> emptyIterator() {
		return new EmptyIterator<T>();
	}

	/** Replies the first element of the collection.
	 * 
	 * @param <T> is the type of the element.
	 * @param collection is the collection to use.
	 * @return the first element of the <var>collection</var>.
	 */
	public static <T> T firstElement(Collection<T> collection) {
		if (collection.isEmpty()) return null;
		if (collection instanceof List<?>)
			return ((List<T>)collection).get(0);
		if (collection instanceof SortedSet<?>)
			return ((SortedSet<T>)collection).first();
		if (collection instanceof Queue<?>)
			return ((Queue<T>)collection).element();
		Iterator<T> iterator = collection.iterator();
		return (iterator.hasNext()) ? iterator.next() : null;
	}
	
	/** Replies the first element of the collection.
	 * 
	 * @param <T> is the type of the element.
	 * @param collection is the collection to use.
	 * @return the last element of the <var>collection</var>.
	 */
	public static <T> T lastElement(Collection<T> collection) {
		if (collection.isEmpty()) return null;
		if (collection instanceof List<?>)
			return ((List<T>)collection).get(collection.size()-1);
		if (collection instanceof SortedSet<?>)
			return ((SortedSet<T>)collection).last();
		Iterator<T> iterator = collection.iterator();
		T elt = null;
		while (iterator.hasNext()) {
			elt = iterator.next();
		}
		return elt;
	}

	/** Cast the specified collection.
	 * <p>
	 * The replied collection is unmodifiable. 
	 * <p>
	 * For better performances, it is recommended
	 * to use only the {@link Collection#iterator()} on the result.
	 *
	 * @param <T> is the type of the collection
	 * @param collection is the collection to use.
	 * @param elementType is the desired type of the returned elements.
	 * @return the collection restricted to the given type.
	 */
	public static <T> Collection<? extends T> restrictType(Collection<?> collection, Class<T> elementType) {
		return new CastCollection<T>(collection, elementType);
	}
	
	/** Cast the specified collection.
	 * <p>
	 * The replied collection is unmodifiable. 
	 * <p>
	 * For better performances, it is recommended
	 * to use only the {@link Collection#iterator()} on the result.
	 *
	 * @param <T> is the type of the collection
	 * @param collection is the collection to use.
	 * @param elementType is the desired type of the returned elements.
	 * @return the collection restricted to the given type.
	 * @since 4.0
	 */
	public static <T> Iterable<? extends T> restrictType(Iterable<?> collection, Class<T> elementType) {
		return new CastIterable<T>(collection, elementType);
	}

	/** Cast the specified collection.
	 * <p>
	 * For better performances, it is recommended
	 * to use only the {@link Collection#iterator()} on the result.
	 *
	 * @param <T> is the type of the collection
	 * @param collection is the collection to use.
	 * @return the collection restricted to the given type.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> restrictType(Collection<? extends T> collection) {
		return (Collection<T>)collection;
	}

	/** Cast the specified iterable.
	 * <p>
	 * For better performances, it is recommended
	 * to use only the {@link Iterable#iterator()} on the result.
	 *
	 * @param <T> is the type of the iterable
	 * @param iterable is the iterable to use.
	 * @return the iterable restricted to the given type.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterable<T> restrictType(Iterable<? extends T> iterable) {
		return (Iterable<T>)iterable;
	}

	/** Cast the specified iterable.
	 *
	 * @param <T> is the type of the iterator.
	 * @param iterator is the iterator to cast.
	 * @return the iterator restricted to the given type.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> restrictType(Iterator<? extends T> iterator) {
		return (Iterator<T>)iterator;
	}

	/** Cast the specified collection and replies an iterator on.
	 * <p>
	 * The replied iterator is unmodifiable. 
	 *
	 * @param <T> is the type of the collection
	 * @param collection is the collection to use.
	 * @param elementType is the desired type of the returned elements.
	 * @return the iterator restricted to the given type.
	 */
	public static <T> Iterator<T> restrictIterator(Iterable<?> collection, Class<T> elementType) {
		return new CastIterator<T>(collection, elementType);
	}

	/** Cast the specified collection and replies an iterator on.
	 * <p>
	 * The replied iterator is unmodifiable. 
	 *
	 * @param <T> is the type of the collection
	 * @param iterator is the original iterator to use.
	 * @param elementType is the desired type of the returned elements.
	 * @return the iterator restricted to the given type.
	 */
	public static <T> Iterator<T> restrictIterator(Iterator<?> iterator, Class<T> elementType) {
		return new CastIterator<T>(iterator, elementType);
	}

	/** Replies an iterator which is going in a reverse order on the given list.
	 * 
	 * @param <T> is the type of the collection
	 * @param list is the collection to use.
	 * @return the reverse iterator.
	 */
	public static <T> SizedIterator<T> reverseIterator(List<T> list) {
		return new ListReverseIterator<T>(list);
	}
	
	/** Replies an iterator which is going in a reverse order on the given set.
	 * 
	 * @param <T> is the type of the collection
	 * @param set is the collection to use.
	 * @return the reverse iterator.
	 */
	public static <T> SizedIterator<T> reverseIterator(SortedSet<T> set) {
		return new SortedSetReverseIterator<T>(set);
	}

	/** Replies an iterator which is iterating on the first iterator
	 * and then on the second one.
	 * 
	 * @param <T> is the type of the collection
	 * @param iterator1 is the first iterator.
	 * @param iterator2 is the first iterator.
	 * @return the merged iterator.
	 */
	public static <T> Iterator<T> mergeIterators(Iterator<? extends T> iterator1, Iterator<? extends T> iterator2) {
		return new IteratorIterator<T>(iterator1, iterator2);
	}

	/** Replies an iterator which is iterating on the first iterator
	 * and then on the second one.
	 * 
	 * @param <T> is the type of the collection
	 * @param iterators are the iterator to merge in order.
	 * @return the merged iterator.
	 */
	public static <T> Iterator<T> mergeIterators(Collection<Iterator<? extends T>> iterators) {
		return new IteratorIterator<T>(iterators);
	}

	/** Replies a collection which is the fusion of the two given
	 * collections. This function does not copy the collections inside
	 * a third one. It maintains references to the original collections
	 * to avoid memory conception.
	 * 
	 * @param <T> is the type of the collection
	 * @param col1
	 * @param col2
	 * @return the merged collection.
	 */
	public static <T> Collection<T> mergeCollections(Collection<? extends T> col1, Collection<? extends T> col2) {
		return new CollectionCollection<T>(col1, col2);
	}

	/** Replies a collection which is the fusion of the two given
	 * collections. This function does not copy the collections inside
	 * a third one. It maintains references to the original collections
	 * to avoid memory conception.
	 * The replied collection contains only the elements which has the given type.
	 * 
	 * @param <T> is the type of the collection
	 * @param type is the type of the collection
	 * @param col1
	 * @param col2
	 * @return the merged collection.
	 */
	public static <T> Collection<T> mergeCollections(Class<T> type, Collection<?> col1, Collection<?> col2) {
		return new CollectionCollection<T>(type, col1, col2);
	}

	/** Transform the given collection into a SizedIterable.
	 * 
	 * @param <T> is the type of the collection
	 * @param collection
	 * @return the wrapping collection.
	 */
	public static <T> SizedIterable<T> toSizedIterable(Collection<T> collection) {
		return new SizedIterableCollection<T>(collection);
	}

	/** Replies a sized iterator on the objects.
	 * 
	 * @param <T> is the type of the object to iterate on.
	 * @param collection are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	public static <T> SizedIterator<T> sizedIterator(Collection<T> collection) {
		return new CollectionSizedIterator<T>(collection);
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
    private static class CastCollection<E> implements Collection<E>, Serializable {

		private static final long serialVersionUID = -68735987364366487L;
		
		/**
		 * 
		 */
		private final Class<E> elementClass;
    	/**
    	 * 
    	 */
    	private final Collection<?> c;
    	private int size = -1;

    	/**
    	 * @param c is the collection to cast.
    	 * @param elementClass is the casting type
    	 */
    	public CastCollection(Collection<?> c, Class<E> elementClass) {
    		if (c==null) throw new NullPointerException();
    		if (elementClass==null) throw new NullPointerException();
    		this.elementClass = elementClass;
    		this.c = c;
    	}
    	
    	@SuppressWarnings("unused")
    	private void checkSize() {
    		if (this.size<0) {
    			this.size = 0;
    			for(E elt : this) {
    				++this.size;
    			}
    		}
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public synchronized int size() {
    		checkSize();
    		return this.size;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public synchronized boolean isEmpty() {
    		checkSize();
    		return this.size<=0;
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public boolean contains(Object o) {
    		return this.c.contains(o);
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public Object[] toArray() {
    		ArrayList<Object> list = new ArrayList<Object>(this.c.size());
			for(E elt : this) {
				list.add(elt);
			}
    		return list.toArray();
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a) {
    		checkSize();
    		T[] b;
            if (a.length < this.size) {
                // Make a new array of a's runtime type, but my contents:
            	b = (a.getClass() == Object[].class)
            	? (T[]) new Object[this.size]
            	: (T[]) Array.newInstance(a.getClass().getComponentType(), this.size);
            }
            else {
            	b = a;
            }
            int idx=0;
			for(E elt : this) {
				b[idx] = (T)elt;
				idx ++;
			}
			return b;
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public boolean containsAll(Collection<?> coll) {
    		return this.c.containsAll(coll);
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
		public String toString() {
    		return this.c.toString();
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public Iterator<E> iterator() {
    		return new CastIterator<E>(this.c, this.elementClass);
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public boolean add(E e){
    		throw new UnsupportedOperationException();
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public boolean remove(Object o) {
    		throw new UnsupportedOperationException();
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public boolean addAll(Collection<? extends E> coll) {
    		throw new UnsupportedOperationException();
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public boolean removeAll(Collection<?> coll) {
    		throw new UnsupportedOperationException();
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public boolean retainAll(Collection<?> coll) {
    		throw new UnsupportedOperationException();
    	}
    	
    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
    	public void clear() {
    		throw new UnsupportedOperationException();
    	}

    } /* class CastCollection */

	/**
	 * @param <E>
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
    private static class CastIterator<E> implements Iterator<E> {
    	
		private final Iterator<?> cIter;
		private final Class<E> elementClass;
		private E next;

		/**
		 * @param collection
		 * @param elementClass
		 */
		public CastIterator(Iterable<?> collection, Class<E> elementClass) {
			this.cIter = collection.iterator();
			this.elementClass = elementClass;
			searchNext();
		}
		
		/**
		 * @param iterator
		 * @param elementClass
		 */
		public CastIterator(Iterator<?> iterator, Class<E> elementClass) {
			this.cIter = iterator;
			this.elementClass = elementClass;
			searchNext();
		}

		private void searchNext() {
			this.next = null;
			Object elt;
			while (this.cIter.hasNext()) {
				elt = this.cIter.next();
				if (elt!=null && this.elementClass.isInstance(elt)) {
					this.next = this.elementClass.cast(elt);
					return;
				}
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public E next() {
			if (this.next==null) throw new NoSuchElementException();
			E elt = this.next;
			searchNext();
			return elt;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
    } /* class CastIterator */
    
	/**
	 * @param <E>
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
    private static class SizedIterableCollection<E> implements SizedIterable<E> {

		private final Collection<E> collection;

		/**
		 * @param collection
		 */
		public SizedIterableCollection(Collection<E> collection) {
			this.collection = collection;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			return this.collection.size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<E> iterator() {
			return this.collection.iterator();
		}
				
    } /* class SizedIterableCollection */

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
    private static class CastIterable<E> implements Iterable<E>, Serializable {

		private static final long serialVersionUID = -1709079580058128871L;
		
		private final Iterable<?> iterable;
		private final Class<E> elementClass;
    	
    	/**
    	 * @param iterable
    	 * @param elementClass
    	 */
    	public CastIterable(Iterable<?> iterable, Class<E> elementClass) {
    		this.iterable = iterable;
    		this.elementClass = elementClass;
    	}

    	/**
    	 * {@inheritDoc}
    	 */
    	@Override
		public Iterator<E> iterator() {
			return new CastIterator<E>(this.iterable, this.elementClass);
		}
    	
    }
    
	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.1
	 */
    private static class EnumerationIterator<E> implements Iterator<E> {

    	private final Enumeration<E> enumeration;
    	
    	/**
    	 * @param enumeration
    	 */
    	public EnumerationIterator(Enumeration<E> enumeration) {
    		this.enumeration = enumeration;
    	}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.enumeration.hasMoreElements();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public E next() {
			return this.enumeration.nextElement();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
    	
    }
    
}
