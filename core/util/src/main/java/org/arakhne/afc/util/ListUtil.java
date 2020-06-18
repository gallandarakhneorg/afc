/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Utilities on lists.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.4
 */
public final class ListUtil {

	private ListUtil() {
		//
	}

	/** Remove the given element from the list using a dichotomic algorithm.
	 *
	 * <p>This function ensure that the comparator is invoked as: <code>comparator(data, dataAlreadyInList)</code>.
	 *
	 * @param <E> is the type of the elements in the list.
	 * @param list is the list to change.
	 * @param comparator is the comparator of elements.
	 * @param data is the data to remove.
	 * @return the index at which the element was removed in the list; or
	 * <code>-1</code> if the element was not removed.
	 */
	public static <E> int remove(List<E> list, Comparator<? super E> comparator, E data) {
		assert list != null;
		assert comparator != null;
		assert data != null;
		int first = 0;
		int last = list.size() - 1;
		while (last >= first) {
			final int center = (first + last) / 2;
			final E dt = list.get(center);
			final int cmpR = comparator.compare(data, dt);
			if (cmpR == 0) {
				list.remove(center);
				return center;
			} else if (cmpR < 0) {
				last = center - 1;
			} else {
				first = center + 1;
			}
		}
		return -1;
	}

	/** Add the given element in the main list using a dichotomic algorithm if the element is not already present.
	 *
	 * <p>This function ensure that the comparator is invoked as: <code>comparator(data, dataAlreadyInList)</code>.
	 *
	 * @param <E> is the type of the elements in the list.
	 * @param list is the list to change.
	 * @param comparator is the comparator of elements.
	 * @param data is the data to insert.
	 * @return the index where the element was inserted, or <code>-1</code>
	 *     if the element was not inserted.
	 * @since 14.0
	 */
	@Inline(value = "add($1, $2, $3, false, false)")
	public static <E> int addIfAbsent(List<E> list, Comparator<? super E> comparator, E data) {
		return add(list, comparator, data, false, false);
	}

	/** Add the given element in the main list using a dichotomic algorithm.
	 *
	 * <p>This function ensure that the comparator is invoked as: <code>comparator(data, dataAlreadyInList)</code>.
	 *
	 * @param <E> is the type of the elements in the list.
	 * @param list is the list to change.
	 * @param comparator is the comparator of elements.
	 * @param data is the data to insert.
	 * @param allowMultipleOccurencesOfSameValue indicates if multiple
	 *     occurrences of the same value are allowed in the list.
	 * @param allowReplacement indicates if the given {@code elt} may replace
	 *     the found element.
	 * @return the index where the element was inserted, or <code>-1</code>
	 *     if the element was not inserted.
	 */
	public static <E> int add(List<E> list, Comparator<? super E> comparator, E data,
			boolean allowMultipleOccurencesOfSameValue, boolean allowReplacement) {
		assert list != null;
		assert comparator != null;
		assert data != null;
		int first = 0;
		int last = list.size() - 1;
		while (last >= first) {
			final int center = (first + last) / 2;
			final E dt = list.get(center);
			final int cmpR = comparator.compare(data, dt);
			if (cmpR == 0 && !allowMultipleOccurencesOfSameValue) {
				if (allowReplacement) {
					list.set(center, data);
					return center;
				}
				return -1;
			}
			if (cmpR < 0) {
				last = center - 1;
			} else {
				first = center + 1;
			}
		}
		list.add(first, data);
		return first;
	}

	/** Replies if the given element is inside the list, using a dichotomic algorithm.
	 *
	 * <p>This function ensure that the comparator is invoked as: <code>comparator(data, dataAlreadyInList)</code>.
	 *
	 * @param <E> is the type of the elements in the list.
	 * @param list is the list to explore.
	 * @param comparator is the comparator of elements.
	 * @param data is the data to search for.
	 * @return <code>true</code> if the data is inside the list, otherwise <code>false</code>
	 */
	@Pure
	public static <E> boolean contains(List<E> list, Comparator<? super E> comparator, E data) {
		assert list != null;
		assert comparator != null;
		assert data != null;
		int first = 0;
		int last = list.size() - 1;
		while (last >= first) {
			final int center = (first + last) / 2;
			final E dt = list.get(center);
			final int cmpR = comparator.compare(data, dt);
			if (cmpR == 0) {
				return true;
			} else if (cmpR < 0) {
				last = center - 1;
			} else {
				first = center + 1;
			}
		}
		return false;
	}

	/** Replies the index of the given data in the given list according to a
	 * dichotomic search algorithm. Order between objects
	 * is given by {@code comparator}.
	 *
	 * <p>This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 *
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to search for.
	 * @return the index at which the element is, or <code>-1</code> if
	 *     the element was not found.
	 */
	@Pure
	public static <T> int indexOf(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert comparator != null;
			assert list != null;
			if (elt == null) {
				return -1;
			}

			int first = 0;
			int last = list.size() - 1;

			while (last >= first) {
				int center = (first + last) / 2;
				final T indata = list.get(center);
				final int cmp = comparator.compare(elt, indata);
				if (cmp == 0) {
					do {
						--center;
					}
					while (center >= 0 && comparator.compare(elt, list.get(center)) == 0);
					return center + 1;
				} else if (cmp < 0) {
					last = center - 1;
				} else {
					first = center + 1;
				}
			}
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			//
		}
		return -1;
	}

	/** Replies the last index of the given data in the given list according to a
	 * dichotomic search algorithm. Order between objects
	 * is given by {@code comparator}.
	 *
	 * <p>This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 *
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to search for.
	 * @return the last index at which the element is, or <code>-1</code> if
	 *     the element was not found.
	 */
	@Pure
	public static <T> int lastIndexOf(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert comparator != null;
			assert list != null;
			if (elt == null) {
				return -1;
			}

			int first = 0;
			int last = list.size() - 1;

			while (last >= first) {
				int c = (first + last) / 2;
				final T indata = list.get(c);
				final int cmp = comparator.compare(elt, indata);
				if (cmp == 0) {
					do {
						++c;
					}
					while (c < list.size() && comparator.compare(elt, list.get(c)) == 0);
					return c - 1;
				} else if (cmp < 0) {
					last = c - 1;
				} else {
					first = c + 1;
				}
			}
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			//
		}
		return -1;
	}

	/** Replies the index at which the given element may
	 * be added in a sorted list.
	 *
	 * <p>This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 *
	 * <p>This function assumes that the given {@code elt}
	 * may appear many times in the list.
	 *
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to add in.
	 * @param list is the list inside which the element should be added.
	 * @return the index at which the element may be added.
	 */
	@Pure
	public static <T> int getInsertionIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		return getInsertionIndex(list, comparator, elt, true);
	}

	/** Replies the index at which the given element may
	 * be added in a sorted list.
	 *
	 * <p>This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 *
	 * <p>This function assumes that the given {@code elt}
	 * may appear many times in the list.
	 *
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the element to add in.
	 * @param list is the list inside which the element should be added.
	 * @param allowMultiple indicates if the given {@code elt} may appear
	 *     many times in the list, or not.
	 * @return the index at which the element may be added.
	 */
	@Pure
	public static <T> int getInsertionIndex(List<T> list, Comparator<? super T> comparator, T elt, boolean allowMultiple) {
		try {
			assert comparator != null;
			assert list != null;
			if (elt == null) {
				return -1;
			}

			int first = 0;
			int last = list.size() - 1;

			while (last >= first) {
				final int center = (first + last) / 2;
				final T indata = list.get(center);
				final int comparison = comparator.compare(elt, indata);
				if (!allowMultiple && comparison == 0) {
					return -1;
				}
				if (comparison < 0) {
					last = center - 1;
				} else {
					first = center + 1;
				}
			}
			return first;
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			return -1;
		}
	}

	/**
	 * Returns the index of the leastest element in this list greater than or equal to
	 * the given element, or <code>-1</code> if there is no such element.
	 *
	 * <p>This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 *
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @return the index of leastest element greater than or equal to {@code elt}, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#ceiling(Object)
	 */
	@Pure
	public static <T> int ceilingIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert comparator != null;
			assert list != null;
			if (elt == null) {
				return -1;
			}
			int first = 0;
			int last = list.size() - 1;

			while (last >= first) {
				int c = (first + last) / 2;
				final T indata = list.get(c);
				final int cmp = comparator.compare(elt, indata);
				if (cmp == 0) {
					do {
						--c;
					}
					while (c >= 0 && comparator.compare(elt, list.get(c)) == 0);
					return c + 1;
				} else if (cmp < 0) {
					last = c - 1;
				} else {
					first = c + 1;
				}
			}
			if (first >= list.size()) {
				first = -1;
			}
			return first;
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			return -1;
		}
	}

	/**
	 * Returns the index of the greatest element in this list less than or equal to
	 * the given element, or <code>-1</code> if there is no such element.
	 *
	 * <p>This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 *
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @return the index of greatest element less than or equal to {@code elt}, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#floor(Object)
	 */
	@Pure
	public static <T> int floorIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert comparator != null;
			assert list != null;
			if (elt == null) {
				return -1;
			}
			int first = 0;
			int last = list.size() - 1;

			while (last >= first) {
				int center = (first + last) / 2;
				final T indata = list.get(center);
				final int cmp = comparator.compare(elt, indata);
				if (cmp == 0) {
					do {
						++center;
					}
					while (center < list.size() && comparator.compare(elt, list.get(center)) == 0);
					return center - 1;
				} else if (cmp < 0) {
					last = center - 1;
				} else {
					first = center + 1;
				}
			}
			return last;
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			return -1;
		}
	}

	/**
	 * Returns the index of the least element in this list strictly greater
	 * than the given element, or <code>-1</code> if there is no such element.
	 *
	 * <p>This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 *
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @return the index of least element strictly greater than to {@code elt}, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#higher(Object)
	 */
	@Pure
	public static <T> int higherIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert comparator != null;
			assert list != null;
			if (elt == null) {
				return -1;
			}
			int first = 0;
			int last = list.size() - 1;

			while (last >= first) {
				final int center = (first + last) / 2;
				final T indata = list.get(center);
				final int cmp = comparator.compare(elt, indata);
				if (cmp < 0) {
					last = center - 1;
				} else {
					first = center + 1;
				}
			}
			++last;
			if (last >= list.size()) {
				last = -1;
			}
			return last;
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			return -1;
		}
	}

	/**
	 * Returns the index of the greatest element in this list strictly lower
	 * than the given element, or <code>-1</code> if there is no such element.
	 *
	 * <p>This function assumes that the given list is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 *
	 * @param <T> is the type of the data to search for.
	 * @param list is the list inside which the element should be searched.
	 * @param comparator is the comparator used to sort the list.
	 * @param elt is the value to match.
	 * @return the index of greater element strictly lower than to {@code elt}, or
	 * <code>-1</code> if there is no such element.
	 * @see NavigableSet#lower(Object)
	 */
	@Pure
	public static <T> int lowerIndex(List<T> list, Comparator<? super T> comparator, T elt) {
		try {
			assert comparator != null;
			assert list != null;
			if (elt == null) {
				return -1;
			}
			int first = 0;
			int last = list.size() - 1;

			while (last >= first) {
				final int center = (first + last) / 2;
				final T indata = list.get(center);
				final int cmp = comparator.compare(elt, indata);
				if (cmp <= 0) {
					last = center - 1;
				} else {
					first = center + 1;
				}
			}
			return last;
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			return -1;
		}
	}

	/** Replies an iterator that goes from end to start of the given list.
	 *
	 * <p>The replied iterator dos not support removals.
	 *
	 * @param <T> the type of the list elements.
	 * @param list the list.
	 * @return the reverse iterator.
	 * @throws NoSuchElementException if the {@code next()} function is called when the iterator is finished.
	 * @since 14.0
	 */
	public static <T> Iterator<T> reverseIterator(final List<T> list) {
		return new Iterator<>() {

			private int next = list.size() - 1;

			@Override
			@Pure
			public boolean hasNext() {
				return this.next >= 0;
			}

			@Override
			public T next() {
				final int n = this.next;
				--this.next;
				try {
					return list.get(n);
				} catch (IndexOutOfBoundsException exception) {
					throw new NoSuchElementException();
				}
			}

		};
	}

}
