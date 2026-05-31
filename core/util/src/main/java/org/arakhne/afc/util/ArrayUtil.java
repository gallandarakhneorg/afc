/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.arakhne.afc.sizediterator.ArraySizedIterator;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Some utilities functions for arrays.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("checkstyle:methodcount")
public final class ArrayUtil {

	private ArrayUtil() {
		//
	}

	/** Reverse the specified array.
	 *
	 * @param <T> the type of the data in the array.
	 * @param tab the array.
	 */
	public static <T> void reverse(T[] tab) {
		for (var i = 0; i < tab.length / 2; ++i) {
			final var tmp = tab[i];
			tab[i] = tab[tab.length - i - 1];
			tab[tab.length - i - 1] = tmp;
		}
	}

	/** Replies an array that corresponds to the given collection.
	 *
	 * @param <T> is the type of the elements.
	 * @param collection is the collection to translate
	 * @param clazz is the type of the elements.
	 * @return the array.
	 */
	@Pure
	public static <T> T[] toArray(Collection<? extends T> collection, Class<T> clazz) {
		final var size = (collection == null) ? 0 : collection.size();
		final var tab = newInstance(clazz, size);
		if (collection != null && size > 0) {
			collection.toArray(tab);
		}
		return tab;
	}

	/** Replies an array that corresponds to the given collection.
	 *
	 * <p>This function clear the content of the given collection.
	 *
	 * @param <T> is the type of the elements.
	 * @param collection is the collection to translate
	 * @param clazz is the type of the elements.
	 * @return the array.
	 */
	public static <T> T[] toArrayAndClear(Collection<? extends T> collection, Class<T> clazz) {
		final var tab = toArray(collection, clazz);
		collection.clear();
		return tab;
	}

	/** Merge the arrays.
	 *
	 * <p>This function does not remove the {@code null} values.
	 *
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param arrays are the arrays to merge.
	 * @return the array.
	 */
	@Pure
	public static <T> T[] merge(Class<T> clazz, @SuppressWarnings("unchecked") T[]... arrays) {
		var length = 0;
		for (final var tab : arrays) {
			if (tab != null) {
				length += tab.length;
			}
		}
		final var result = newInstance(clazz, length);
		var i = 0;
		for (final var tab : arrays) {
			if (tab != null) {
				System.arraycopy(tab, 0, result, i, tab.length);
				i += tab.length;
			}
		}
		return result;
	}

	/** Merge the elements to make an array.
	 *
	 * <p>This function does not remove the {@code null} values.
	 *
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param elements are the elements to merge.
	 * @return the array.
	 */
	@Pure
	public static <T> T[] merge(Class<T> clazz, @SuppressWarnings("unchecked") T... elements) {
		final var result = newInstance(clazz, elements.length);
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}

	/** Merge the elements to make an array.
	 *
	 * <p>This function does not remove the {@code null} values.
	 *
	 * @param <T> is the type of the elements.
	 * @param source is the first array to merge.
	 * @param clazz is the type of the elements.
	 * @param elements are the elements to merge.
	 * @return the array.
	 */
	@Pure
	public static <T> T[] merge(Class<T> clazz, T[] source, @SuppressWarnings("unchecked") T... elements) {
		final var result = newInstance(clazz, source.length + elements.length);
		System.arraycopy(source, 0, result, 0, source.length);
		System.arraycopy(elements, 0, result, source.length, elements.length);
		return result;
	}

	/** Merge the arrays.
	 *
	 * <p>This function removes the {@code null} values.
	 *
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param arrays are the arrays to merge.
	 * @return the array.
	 */
	@Pure
	public static <T> T[] mergeWithoutNull(Class<T> clazz, @SuppressWarnings("unchecked") T[]... arrays) {
		var length = 0;
		for (final var tab : arrays) {
			if (tab != null) {
				for (final var t : tab) {
					if (t != null) {
						++length;
					}
				}
			}
		}
		final var result = newInstance(clazz, length);
		var i = 0;
		for (final var tab : arrays) {
			if (tab != null) {
				for (final var t : tab) {
					if (t != null) {
						result[i] = t;
						++i;
					}
				}
			}
		}
		return result;
	}

	/** Merge the elements to make an array.
	 *
	 * <p>This function removes the {@code null} values.
	 *
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param elements are the elements to merge.
	 * @return the array.
	 */
	@Pure
	public static <T> T[] mergeWithoutNull(Class<T> clazz, @SuppressWarnings("unchecked") T... elements) {
		var nbNotNull = 0;
		for (final var t : elements) {
			if (t != null) {
				++nbNotNull;
			}
		}
		final var result = newInstance(clazz, nbNotNull);
		var i = 0;
		for (final var t : elements) {
			if (t != null) {
				result[i++] = t;
			}
		}
		return result;
	}

	/** Merge the elements to make an array.
	 *
	 * <p>This function removes the {@code null} values.
	 *
	 * @param <T> is the type of the elements.
	 * @param source is the first array to merge.
	 * @param clazz is the type of the elements.
	 * @param elements are the elements to merge.
	 * @return the array.
	 */
	@Pure
	public static <T> T[] mergeWithoutNull(Class<T> clazz, T[] source, @SuppressWarnings("unchecked") T... elements) {
		var nbNotNull = 0;
		for (final var t : source) {
			if (t != null) {
				++nbNotNull;
			}
		}
		for (final var t : elements) {
			if (t != null) {
				++nbNotNull;
			}
		}
		final var result = newInstance(clazz, nbNotNull);
		var i = 0;
		for (final var t : source) {
			if (t != null) {
				result[i++] = t;
			}
		}
		for (final var t : elements) {
			if (t != null) {
				result[i++] = t;
			}
		}
		return result;
	}

	/** Remove the given elements from the array.
	 *
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param source is the array to scan
	 * @param toRemove are the elements to remove.
	 * @return the array without the removed elements.
	 */
	@Pure
	public static <T> T[] removeElements(Class<T> clazz, T[] source, @SuppressWarnings("unchecked") T... toRemove) {
		final var list = new ArrayList<T>();
		list.addAll(Arrays.asList(source));
		for (final var t : toRemove) {
			list.remove(t);
		}
		return toArrayAndClear(list, clazz);
	}

	/** Cast the specified array and put {@code null} is the array when
	 * the element could not be casted.
	 *
	 * @param <I> is the type of the elements before the cast.
	 * @param <T> is the type of the elements after the cast.
	 * @param originalArray is the array to cast
	 * @param clazz is the casting type
	 * @return the array in which each element was casted according to the given type.
	 * @see #castRestrictedArray(Object[], Class)
	 */
	@Pure
	public static <I, T> T[] castArray(I[] originalArray, Class<T> clazz) {
		final var size = (originalArray == null) ? 0 : originalArray.length;
		final var result = newInstance(clazz, size);
		if (originalArray != null && size > 0) {
			var index = 0;
			for (final var to : originalArray) {
				result[index] = (clazz.isInstance(to)) ? clazz.cast(to) : null;
				++index;
			}
		}
		return result;
	}

	/** Cast the specified array and put {@code null} is the array when
	 * the element could not be casted.
	 *
	 * @param <I> is the type of the elements before the cast.
	 * @param <O> is the type of the elements after the cast.
	 * @param originalArray is the array to cast
	 * @param clazz is the casting type
	 * @return the array in which each element was casted according to the given type.
	 * @see #castRestrictedArray(Object[], Class)
	 */
	@Pure
	public static <I, O> O[] castArray(Collection<I> originalArray, Class<O> clazz) {
		final var size = (originalArray == null) ? 0 : originalArray.size();
		final var result = newInstance(clazz, size);
		if (originalArray != null && size > 0) {
			var index = 0;
			for (final var to : originalArray) {
				result[index] = (clazz.isInstance(to)) ? clazz.cast(to) : null;
				++index;
			}
		}
		return result;
	}

	/** Replies an array in which all the elements must
	 * respect the given comparator.
	 *
	 * <p>The respect of the comparator is done when the
	 * comparator replies equals.
	 *
	 * @param <T> is the type of the elements
	 * @param originalArray is the array to cast
	 * @param clazz is the casting type
	 * @param comparator is filtering the elements.
	 * @return the array in which each element was casted according to the given type.
	 */
	@Pure
	public static <T> T[] restrictArray(T[] originalArray, Class<T> clazz, Filter<T> comparator) {
		final var size = (originalArray == null) ? 0 : originalArray.length;
		final var result = new ArrayList<T>();
		if (originalArray != null && size > 0) {
			for (final var to : originalArray) {
				if (comparator.filter(to)) {
					result.add(to);
				}
			}
		}
		return toArrayAndClear(result, clazz);
	}

	/** Cast the specified array and remove the elements that could not be casted.
	 *
	 * @param <I> is the type of the elements before the cast.
	 * @param <O> is the type of the elements after the cast.
	 * @param originalArray is the array to cast
	 * @param clazz is the casting type
	 * @return the array in which each element was casted according to the given type.
	 * @see #castArray(Object[], Class)
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public static <I, O> O[] castRestrictedArray(I[] originalArray, Class<O> clazz) {
		final var size = (originalArray == null) ? 0 : originalArray.length;
		final var result = new ArrayList<O>();
		if (originalArray != null && size > 0) {
			for (final var to : originalArray) {
				if (clazz.isInstance(to)) {
					result.add((O) to);
				}
			}
		}
		return toArrayAndClear(result, clazz);
	}

	/** Cast the specified array and remove the elements that could not be casted.
	 *
	 * @param <I> is the type of the elements before the cast.
	 * @param <O> is the type of the elements after the cast.
	 * @param originalArray is the array to cast
	 * @param clazz is the casting type
	 * @return the array in which each element was casted according to the given type.
	 * @see #castArray(Object[], Class)
	 */
	@SuppressWarnings("unchecked")
	@Pure
	public static <I, O> O[] castRestrictedArray(Collection<I> originalArray, Class<O> clazz) {
		final var size = (originalArray == null) ? 0 : originalArray.size();
		final var result = new ArrayList<O>();
		if (originalArray != null && size > 0) {
			for (final var to : originalArray) {
				if (clazz.isInstance(to)) {
					result.add((O) to);
				}
			}
		}
		return toArrayAndClear(result, clazz);
	}

	/** Replies of the given element is in the array.
	 *
	 * <p>This function does not call {@link Object#equals(java.lang.Object)}.
	 * It tests the equality on the object references.
	 *
	 * @param <T> is the type of the elements.
	 * @param elt is the element to search for.
	 * @param array is the array inside which the search must be done.
	 * @return {@code true} if the element is inside the array, otherwise {@code false}
	 */
	@Pure
	public static <T> boolean containsObject(T elt, T[] array) {
		for (final var t : array) {
			if (t == elt) {
				return true;
			}
		}
		return false;
	}

	/** Replies of the given element is in the array.
	 *
	 * <p>This function does not call {@link Object#equals(java.lang.Object)}.
	 * It tests the equality on the object references.
	 *
	 * @param <T> is the type of the elements.
	 * @param elts are the elements to search for.
	 * @param array is the array inside which the search must be done.
	 * @return {@code true} if the elements are inside the array, otherwise {@code false}
	 */
	@Pure
	public static <T> boolean containsAllObjects(T[] elts, T[] array) {
		for (final var elt : elts) {
			var found = false;
			for (final var t : array) {
				if (t == elt) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	/** Replies of the given element is in the array.
	 *
	 * <p>This function is based on {@link Object#equals(java.lang.Object)}.
	 *
	 * @param <T> is the type of the elements.
	 * @param elt is the element to search for.
	 * @param array is the array inside which the search must be done.
	 * @return {@code true} if the element is inside the array, otherwise {@code false}
	 */
	@Pure
	public static <T> boolean contains(T elt, @SuppressWarnings("unchecked") T... array) {
		for (final var t : array) {
			if (t == elt || t != null && t.equals(elt)) {
				return true;
			}
		}
		return false;
	}

	/** Replies of the given element is in the sorted array.
	 *
	 * <p>This function assumes that the given array is sorted
	 * according to the given comparator.
	 * A dichotomic algorithm is used.
	 *
	 * @param <T> is the type of the elements.
	 * @param comparator is the comparator used to sort the array.
	 * @param elt is the element to search for.
	 * @param array is the array inside which the search must be done.
	 * @return {@code true} if the element is inside the array, otherwise {@code false}
	 * @since 4.0
	 */
	@Pure
	public static <T> boolean contains(Comparator<T> comparator, T elt, @SuppressWarnings("unchecked") T... array) {
		assert comparator != null;
		assert elt != null;
		assert array != null;

		var first = 0;
		var last = array.length - 1;
		while (last >= first) {
			final var center = (first + last) / 2;
			final var indata = array[center];
			final var cmp = comparator.compare(elt, indata);
			if (cmp == 0) {
				return true;
			} else if (cmp < 0) {
				last = center - 1;
			} else {
				first = center + 1;
			}
		}
		return false;
	}

	/** Replies if the given elements is in the array.
	 *
	 * <p>This function is based on {@link Object#equals(java.lang.Object)}.
	 *
	 * @param <T> is the type of the elements.
	 * @param elts are the elements to search for.
	 * @param array is the array inside which the search must be done.
	 * @return {@code true} if the elements are inside the array, otherwise {@code false}
	 */
	@Pure
	public static <T> boolean containsAll(T[] elts, T[] array) {
		for (final var elt : elts) {
			var found = false;
			for (final var t : array) {
				if (t == elt || t != null && t.equals(elt)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	/** Replies of one of the given elements is in the array.
	 *
	 * <p>This function is based on {@link Object#equals(java.lang.Object)}.
	 *
	 * @param <T> is the type of the elements.
	 * @param elts is the first array.
	 * @param array is the second array.
	 * @return {@code true} if an intersection is existing, otherwise {@code false}
	 */
	@Pure
	public static <T> boolean intersects(T[] elts, T[] array) {
		for (final var t : array) {
			for (final var elt : elts) {
				if (elt == t || t != null && t.equals(elt)) {
					return true;
				}
			}
		}
		return false;
	}

	/** Create an instance of array.
	 *
	 * @param <T> is the type of the elements.
	 * @param clazz is the type of the elements.
	 * @param size is the size of the new array.
	 * @return the new array.
	 * @throws IndexOutOfBoundsException if the {@code size} is negative.
	 */
	@Pure
	@SuppressWarnings("unchecked")
	public static <T> T[] newInstance(Class<T> clazz, int size) {
		if (size < 0) {
			throw new IndexOutOfBoundsException(size + "<0"); //$NON-NLS-1$
		}
		return (T[]) Array.newInstance(clazz, size);
	}

	/** Shuffle the specified array.
	 *
	 * @param <T> is the type of the elements.
	 * @param array is the array to shuffle.
	 */
	@Pure
	@Inline(value = "ArrayUtil.shuffle($1, new Random())", imported = {ArrayUtil.class, Random.class},
            statementExpression = true)
	public static <T> void shuffle(T[] array) {
		shuffle(array, new Random());
	}

	/** Shuffle the specified array.
	 *
	 * @param <T> is the type of the elements.
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	@Pure
	public static <T> void shuffle(T[] array, Random rnd) {
		for (var i = array.length; i > 1; --i) {
			final var ir = rnd.nextInt(i);
			final var tmp = array[i - 1];
			array[i - 1] = array[ir];
			array[ir] = tmp;
		}
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 */
	@Pure
	@Inline(value = "ArrayUtil.shuffle($1, new Random())", imported = {ArrayUtil.class, Random.class},
			statementExpression = true)
	public static void shuffle(boolean[] array) {
		shuffle(array, new Random());
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	@Pure
	public static void shuffle(boolean[] array, Random rnd) {
		for (var i = array.length; i > 1; --i) {
			final var ir = rnd.nextInt(i);
			final var tmp = array[i - 1];
			array[i - 1] = array[ir];
			array[ir] = tmp;
		}
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 */
	@Pure
	@Inline(value = "ArrayUtil.shuffle($1, new Random())", imported = {ArrayUtil.class, Random.class},
            statementExpression = true)
	public static void shuffle(byte[] array) {
		shuffle(array, new Random());
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	@Pure
	public static void shuffle(byte[] array, Random rnd) {
		for (var i = array.length; i > 1; --i) {
			final var ir = rnd.nextInt(i);
			final var tmp = array[i - 1];
			array[i - 1] = array[ir];
			array[ir] = tmp;
		}
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 */
	@Pure
	@Inline(value = "ArrayUtil.shuffle($1, new Random())", imported = {ArrayUtil.class, Random.class},
            statementExpression = true)
	public static void shuffle(char[] array) {
		shuffle(array, new Random());
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	@Pure
	public static void shuffle(char[] array, Random rnd) {
		for (var i = array.length; i > 1; --i) {
			final var ir = rnd.nextInt(i);
			final var tmp = array[i - 1];
			array[i - 1] = array[ir];
			array[ir] = tmp;
		}
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 */
	@Pure
	@Inline(value = "ArrayUtil.shuffle($1, new Random())", imported = {ArrayUtil.class, Random.class},
            statementExpression = true)
	public static void shuffle(int[] array) {
		shuffle(array, new Random());
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	@Pure
	public static void shuffle(int[] array, Random rnd) {
		for (var i = array.length; i > 1; --i) {
			final var ir = rnd.nextInt(i);
			final var tmp = array[i - 1];
			array[i - 1] = array[ir];
			array[ir] = tmp;
		}
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 */
	@Pure
	@Inline(value = "ArrayUtil.shuffle($1, new Random())", imported = {ArrayUtil.class, Random.class},
            statementExpression = true)
	public static void shuffle(long[] array) {
		shuffle(array, new Random());
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	@Pure
	public static void shuffle(long[] array, Random rnd) {
		for (var i = array.length; i > 1; --i) {
			final var ir = rnd.nextInt(i);
			final var tmp = array[i - 1];
			array[i - 1] = array[ir];
			array[ir] = tmp;
		}
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 */
	@Pure
	@Inline(value = "ArrayUtil.shuffle($1, new Random())", imported = {ArrayUtil.class, Random.class},
            statementExpression = true)
	public static void shuffle(float[] array) {
		shuffle(array, new Random());
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	@Pure
	public static void shuffle(float[] array, Random rnd) {
		for (var i = array.length; i > 1; --i) {
			final var ir = rnd.nextInt(i);
			final var tmp = array[i - 1];
			array[i - 1] = array[ir];
			array[ir] = tmp;
		}
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 */
	@Pure
	@Inline(value = "ArrayUtil.shuffle($1, new Random())", imported = {ArrayUtil.class, Random.class},
            statementExpression = true)
	public static void shuffle(double[] array) {
		shuffle(array, new Random());
	}

	/** Shuffle the specified array.
	 *
	 * @param array is the array to shuffle.
	 * @param rnd is the random number generator to use.
	 */
	@Pure
	public static void shuffle(double[] array, Random rnd) {
		for (var i = array.length; i > 1; --i) {
			final var ir = rnd.nextInt(i);
			final var tmp = array[i - 1];
			array[i - 1] = array[ir];
			array[ir] = tmp;
		}
	}

	/**
	 * Replies a string representation of the given object.
	 *
	 * <p>This function supports the base type's arrays.
	 *
	 * @param obj is the object to translate.
	 * @return a string representation of the given object.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
	public static String toString(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof boolean[] arg0) {
			return Arrays.toString(arg0);
		}
		if (obj instanceof byte[] arg0) {
			return Arrays.toString(arg0);
		}
		if (obj instanceof char[] arg0) {
			return Arrays.toString(arg0);
		}
		if (obj instanceof short[] arg0) {
			return Arrays.toString(arg0);
		}
		if (obj instanceof int[] arg0) {
			return Arrays.toString(arg0);
		}
		if (obj instanceof long[] arg0) {
			return Arrays.toString(arg0);
		}
		if (obj instanceof float[] arg0) {
			return Arrays.toString(arg0);
		}
		if (obj instanceof double[] arg0) {
			return Arrays.toString(arg0);
		}
		if (obj instanceof Object[] arg0) {
			return Arrays.toString(arg0);
		}
		return obj.toString();
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param <T> is the type of the object to iterate on.
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 */
	@Pure
	@Inline(value = "new ArraySizedIterator<>($1)", imported = {ArraySizedIterator.class})
	public static <T> SizedIterator<T> sizedIterator(T[] array) {
		return new ArraySizedIterator<>(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static SizedIterator<Character> sizedIterator(char[] array) {
		return new NativeCharacterToObjectCharacterIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static SizedIterator<Byte> sizedIterator(byte[] array) {
		return new NativeByteToObjectByteIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static SizedIterator<Short> sizedIterator(short[] array) {
		return new NativeShortToObjectShortIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static SizedIterator<Integer> sizedIterator(int[] array) {
		return new NativeIntegerToObjectIntegerIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static SizedIterator<Long> sizedIterator(long[] array) {
		return new NativeLongToObjectLongIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static SizedIterator<Float> sizedIterator(float[] array) {
		return new NativeFloatToObjectFloatIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static SizedIterator<Double> sizedIterator(double[] array) {
		return new NativeDoubleToObjectDoubleIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static SizedIterator<Boolean> sizedIterator(boolean[] array) {
		return new NativeBooleanToObjectBooleanIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param <T> is the type of the object to iterate on.
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	@Inline(value = "new ArraySizedIterator<>($1)", imported = {ArraySizedIterator.class})
	public static <T> Iterator<T> iterator(T[] array) {
		return new ArraySizedIterator<>(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static Iterator<Boolean> iterator(boolean[] array) {
		return new NativeBooleanToObjectBooleanIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static Iterator<Character> iterator(char[] array) {
		return new NativeCharacterToObjectCharacterIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static Iterator<Byte> iterator(byte[] array) {
		return new NativeByteToObjectByteIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static Iterator<Short> iterator(short[] array) {
		return new NativeShortToObjectShortIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static Iterator<Integer> iterator(int[] array) {
		return new NativeIntegerToObjectIntegerIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static Iterator<Long> iterator(long[] array) {
		return new NativeLongToObjectLongIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static Iterator<Float> iterator(float[] array) {
		return new NativeFloatToObjectFloatIterator(array);
	}

	/** Replies a sized iterator on the objects.
	 *
	 * @param array are the objects to iterate on.
	 * @return an iterator
	 * @since 4.1
	 */
	@Pure
	public static Iterator<Double> iterator(double[] array) {
		return new NativeDoubleToObjectDoubleIterator(array);
	}

	/**
	 * Some utilities functions for arrays.
	 *
	 * @param <T> is the type of the value to pass to the filter.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@FunctionalInterface
	public interface Filter<T> {

		/** Filtering the objects.
		 *
		 * @param obj is the value to filter.
		 * @return {@code true} if the given value is acceptable, otherwise {@code false}
		 */
		@Pure
		boolean filter(T obj);

	}

	/** Autoboxing iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeBooleanToObjectBooleanIterator implements SizedIterator<Boolean> {

		private final boolean[] array;

		private int idx;

		/** Construct the iterator.
		 *
		 * @param data the data.
		 */
		NativeBooleanToObjectBooleanIterator(boolean[] data) {
			this.array = data;
		}

		@Override
		public boolean hasNext() {
			return this.array != null &&  this.idx >= 0 && this.idx < this.array.length;
		}

		@Override
		public Boolean next() {
			if (this.array != null && this.idx >= 0 && this.idx < this.array.length) {
				final var b = Boolean.valueOf(this.array[this.idx]);
				++this.idx;
				return b;
			}
			throw new NoSuchElementException();
		}

		@Override
		public int totalSize() {
			return this.array == null ? 0 : this.array.length;
		}

		@Override
		public int index() {
			return this.idx - 1;
		}

		@Override
		public int rest() {
			return totalSize() - (index() + 1);
		}

	}

	/** Autoboxing iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeCharacterToObjectCharacterIterator implements SizedIterator<Character> {

		private final char[] array;

		private int idx;

		/** Construct iterator.
		 *
		 * @param data the data.
		 */
		NativeCharacterToObjectCharacterIterator(char[] data) {
			this.array = data;
		}

		@Override
		public boolean hasNext() {
			return this.array != null &&  this.idx >= 0 && this.idx < this.array.length;
		}

		@Override
		public Character next() {
			if (this.array != null && this.idx >= 0 && this.idx < this.array.length) {
				final var c = Character.valueOf(this.array[this.idx]);
				++this.idx;
				return c;
			}
			throw new NoSuchElementException();
		}

		@Override
		public int totalSize() {
			return this.array == null ? 0 : this.array.length;
		}

		@Override
		public int index() {
			return this.idx - 1;
		}

		@Override
		public int rest() {
			return totalSize() - (index() + 1);
		}

	}

	/** Autoboxing iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeByteToObjectByteIterator implements SizedIterator<Byte> {

		private final byte[] array;

		private int idx;

		/** Construct the iterator.
		 *
		 * @param data the data.
		 */
		NativeByteToObjectByteIterator(byte[] data) {
			this.array = data;
		}

		@Override
		public boolean hasNext() {
			return this.array != null && this.idx >= 0 && this.idx < this.array.length;
		}

		@Override
		public Byte next() {
			if (this.array != null && this.idx >= 0 && this.idx < this.array.length) {
				final var b = Byte.valueOf(this.array[this.idx]);
				++this.idx;
				return b;
			}
			throw new NoSuchElementException();
		}

		@Override
		public int totalSize() {
			return this.array == null ? 0 : this.array.length;
		}

		@Override
		public int index() {
			return this.idx - 1;
		}

		@Override
		public int rest() {
			return totalSize() - (index() + 1);
		}

	}

	/** Autoboxing iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeShortToObjectShortIterator implements SizedIterator<Short> {

		private final short[] array;

		private int idx;

		/** Construct the iterator.
		 *
		 * @param data the data.
		 */
		NativeShortToObjectShortIterator(short[] data) {
			this.array = data;
		}

		@Override
		public boolean hasNext() {
			return this.array != null && this.idx >= 0 && this.idx < this.array.length;
		}

		@Override
		public Short next() {
			if (this.array != null && this.idx >= 0 && this.idx < this.array.length) {
				final var s = Short.valueOf(this.array[this.idx]);
				++this.idx;
				return s;
			}
			throw new NoSuchElementException();
		}

		@Override
		public int totalSize() {
			return this.array == null ? 0 : this.array.length;
		}

		@Override
		public int index() {
			return this.idx - 1;
		}

		@Override
		public int rest() {
			return totalSize() - (index() + 1);
		}

	}

	/** Autoboxing iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeIntegerToObjectIntegerIterator implements SizedIterator<Integer> {

		private final int[] array;

		private int idx;

		/** Construct the iterator.
		 *
		 * @param data the data.
		 */
		NativeIntegerToObjectIntegerIterator(int[] data) {
			this.array = data;
		}

		@Override
		public boolean hasNext() {
			return this.array != null && this.idx >= 0 && this.idx < this.array.length;
		}

		@Override
		public Integer next() {
			if (this.array != null && this.idx >= 0 && this.idx < this.array.length) {
				final var f = Integer.valueOf(this.array[this.idx]);
				++this.idx;
				return f;
			}
			throw new NoSuchElementException();
		}

		@Override
		public int totalSize() {
			return this.array == null ? 0 : this.array.length;
		}

		@Override
		public int index() {
			return this.idx - 1;
		}

		@Override
		public int rest() {
			return totalSize() - (index() + 1);
		}

	}

	/** Autoboxing iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeLongToObjectLongIterator implements SizedIterator<Long> {

		private final long[] array;

		private int idx;

		/** Construct iterator.
		 *
		 * @param data the data.
		 */
		NativeLongToObjectLongIterator(long[] data) {
			this.array = data;
		}

		@Override
		public boolean hasNext() {
			return this.array != null &&  this.idx >= 0 && this.idx < this.array.length;
		}

		@Override
		public Long next() {
			if (this.array != null && this.idx >= 0 && this.idx < this.array.length) {
				final var l = Long.valueOf(this.array[this.idx]);
				++this.idx;
				return l;
			}
			throw new NoSuchElementException();
		}

		@Override
		public int totalSize() {
			return this.array == null ? 0 : this.array.length;
		}

		@Override
		public int index() {
			return this.idx - 1;
		}

		@Override
		public int rest() {
			return totalSize() - (index() + 1);
		}

	}

	/** Autoboxing iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeFloatToObjectFloatIterator implements SizedIterator<Float> {

		private final float[] array;

		private int idx;

		/** Construct iterator.
		 *
		 * @param data the data.
		 */
		NativeFloatToObjectFloatIterator(float[] data) {
			this.array = data;
		}

		@Override
		public boolean hasNext() {
			return this.array != null && this.idx >= 0 && this.idx < this.array.length;
		}

		@Override
		public Float next() {
			if (this.array != null && this.idx >= 0 && this.idx < this.array.length) {
				final var f = Float.valueOf(this.array[this.idx]);
				++this.idx;
				return f;
			}
			throw new NoSuchElementException();
		}

		@Override
		public int totalSize() {
			return this.array == null ? 0 : this.array.length;
		}

		@Override
		public int index() {
			return this.idx - 1;
		}

		@Override
		public int rest() {
			return totalSize() - (index() + 1);
		}

	}

	/** Autoboxing iterator.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavenartifactid $ArtifactId$
	 * @mavengroupid $GroupId$
	 */
	private static class NativeDoubleToObjectDoubleIterator implements SizedIterator<Double> {

		private final double[] array;

		private int idx;

		/** Construct the iterator.
		 *
		 * @param data the data.
		 */
		NativeDoubleToObjectDoubleIterator(double[] data) {
			this.array = data;
		}

		@Override
		public boolean hasNext() {
			return this.array != null && this.idx >= 0 && this.idx < this.array.length;
		}

		@Override
		public Double next() {
			if (this.array != null && this.idx >= 0 && this.idx < this.array.length) {
				final var d = Double.valueOf(this.array[this.idx]);
				++this.idx;
				return d;
			}
			throw new NoSuchElementException();
		}

		@Override
		public int totalSize() {
			return this.array == null ? 0 : this.array.length;
		}

		@Override
		public int index() {
			return this.idx - 1;
		}

		@Override
		public int rest() {
			return totalSize() - (index() + 1);
		}

	}

}
